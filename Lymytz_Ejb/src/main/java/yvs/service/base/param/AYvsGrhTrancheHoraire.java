/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsGrhTrancheHoraire extends AbstractEntity {

    public AYvsGrhTrancheHoraire() {
    }

    public AYvsGrhTrancheHoraire(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsGrhTrancheHoraire> controle(YvsGrhTrancheHoraire entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getTitre())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer un code pour la tranche");
            }
            if (!asString(entity.getTypeJournee())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez Choisir le type de journée");
            }
            if (entity.getHeureDebut() == null) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser l'heure de début");
            }
            if (entity.getHeureFin() == null) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser l'heure de fin");
            }
            if (entity.getHeureDebut().equals(entity.getHeureFin()) || entity.getHeureDebut().after(entity.getHeureFin())) {
                return new ResultatAction<>(false, entity, 0L, "L'heure de fin et de début ne correspondent pas");
            }

            YvsGrhTrancheHoraire t = (YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findByTitres", new String[]{"titre", "societe"}, new Object[]{entity.getTitre(), entity.getSociete()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                if (t.getTypeJournee().equals(entity.getTypeJournee())) {
                    return new ResultatAction<>(true, t, t.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Vous avez déja crée cette tranche");
                }

            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsGrhTrancheHoraire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsGrhTrancheHoraire> save(YvsGrhTrancheHoraire entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_grh_tranche_horaire", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsGrhTrancheHoraire) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsGrhTrancheHoraire.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsGrhTrancheHoraire> update(YvsGrhTrancheHoraire entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsGrhTrancheHoraire) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsGrhTrancheHoraire.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsGrhTrancheHoraire> delete(YvsGrhTrancheHoraire entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsGrhTrancheHoraire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
