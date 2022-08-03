/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaJournaux extends AbstractEntity {

    public AYvsComptaJournaux() {
    }

    public AYvsComptaJournaux(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComptaJournaux> controle(YvsComptaJournaux entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getCodeJournal())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer le code journal !");
            }
            if (!asString(entity.getIntitule())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer l'intitulé du journal !");
            }
            if (entity.getDefaultFor()) {
                String[] champ = new String[]{"agence", "type", "default"};
                Object[] val = new Object[]{entity.getAgence(), entity.getTypeJournal(), true};
                YvsComptaJournaux jrn = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findByDefaut", champ, val);
                if (jrn != null ? (jrn.getId() != null ? !jrn.getId().equals(entity.getId()) : false) : false) {
                    return new ResultatAction<>(false, entity, 0L, "Il existe deja un journal par défaut pour ce type");

                }
            }

            String[] champ = new String[]{"societe", "code"};
            Object[] val = new Object[]{entity.getAgence().getSociete(), entity.getCodeJournal()};
            YvsComptaJournaux t = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findIdByCodejournale", champ, val);
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                if (t.getIntitule().equals(entity.getIntitule())) {
                    return new ResultatAction<>(true, t, t.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Un journal a déjà été trouvé avec le même code !");
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaJournaux.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComptaJournaux> save(YvsComptaJournaux entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu():false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_compta_journaux", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComptaJournaux) dao.loadOneByNameQueries("YvsComptaJournaux.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComptaJournaux) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaJournaux.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaJournaux> update(YvsComptaJournaux entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaJournaux) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaJournaux.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaJournaux> delete(YvsComptaJournaux entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaJournaux.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
