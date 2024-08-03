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
import yvs.entity.compta.analytique.YvsComptaPlanAnalytique;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaPlanAnalytique extends AbstractEntity {

    public AYvsComptaPlanAnalytique() {
    }

    public AYvsComptaPlanAnalytique(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComptaPlanAnalytique> controle(YvsComptaPlanAnalytique entity) {
        try {
            if (entity == null) {

            }
            if (!asString(entity.getCodePlan())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer le code du plan analytique");
            }
            if (!asString(entity.getIntitule())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer l'intitulé du plan analytique !");
            }

            YvsComptaPlanAnalytique y = (YvsComptaPlanAnalytique) dao.loadOneByNameQueries("YvsComptaPlanAnalytique.findByCodePlan", new String[]{"codePlan", "societe"}, new Object[]{entity.getCodePlan(), entity.getSociete()});
            if (y != null ? (y.getId() != null ? !y.getId().equals(entity.getId()) : false) : false) {
                if (y.getIntitule().equals(entity.getIntitule())) {
                    return new ResultatAction<>(true, y, y.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Il existe deja un plan analytique avec ce code");
                }

            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaPlanAnalytique.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComptaPlanAnalytique> save(YvsComptaPlanAnalytique entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_compta_plan_analytique", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComptaPlanAnalytique) dao.loadOneByNameQueries("YvsComptaPlanAnalytique.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComptaPlanAnalytique) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaPlanAnalytique.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaPlanAnalytique> update(YvsComptaPlanAnalytique entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaPlanAnalytique) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaPlanAnalytique.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaPlanAnalytique> delete(YvsComptaPlanAnalytique entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaPlanAnalytique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
