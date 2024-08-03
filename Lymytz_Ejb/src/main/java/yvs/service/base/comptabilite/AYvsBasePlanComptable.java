/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.service.AbstractEntity;
import yvs.service.base.tiers.AYvsBaseTiers;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBasePlanComptable extends AbstractEntity {

    public AYvsBasePlanComptable() {
    }

    public AYvsBasePlanComptable(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBasePlanComptable> controle(YvsBasePlanComptable entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getNumCompte())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le numeco de compte");
            }
            if (!asString(entity.getIntitule())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer l'intitule du compte");
            }
            if (entity.getNatureCompte() != null ? entity.getNatureCompte().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez spécifier la nature de compte");
            }
            YvsBasePlanComptable t = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findByNumCompte", new String[]{"numCompte", "societe"}, new Object[]{entity.getNumCompte(), entity.getNatureCompte().getSociete()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                if (t.getIntitule().equals(entity.getIntitule())) {
                    return new ResultatAction<>(true, t, t.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Vous avez déja crée un plan comptable avec ce numéro de compte");
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBasePlanComptable.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsBasePlanComptable> save(YvsBasePlanComptable entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_plan_comptable", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBasePlanComptable) dao.loadOneByNameQueries("YvsBasePlanComptable.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBasePlanComptable) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, result.getMessage());
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePlanComptable.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBasePlanComptable> update(YvsBasePlanComptable entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBasePlanComptable) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePlanComptable.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBasePlanComptable> delete(YvsBasePlanComptable entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePlanComptable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
