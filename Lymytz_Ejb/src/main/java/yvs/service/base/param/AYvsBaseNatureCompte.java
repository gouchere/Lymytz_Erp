/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBaseNatureCompte;
import yvs.service.AbstractEntity;
import yvs.service.base.tiers.AYvsBaseTiers;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseNatureCompte extends AbstractEntity {

    public AYvsBaseNatureCompte() {
    }

    public AYvsBaseNatureCompte(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseNatureCompte> controle(YvsBaseNatureCompte entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getDesignation())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer la désignation !");
            }
            if (!entity.getNature().equals(Constantes.NAT_AUTRE)) {
                String nameQueri = "YvsBaseNatureCompte.findByNature";
                String[] champ = new String[]{"societe", "nature"};
                Object[] val = new Object[]{entity.getSociete(), entity.getNature()};
                YvsBaseNatureCompte y = (YvsBaseNatureCompte) dao.loadOneByNameQueries(nameQueri, champ, val);
                if (y != null ? y.getId() > 0 ? !y.getId().equals(entity.getId()) : false : false) {
                    if (y.getDesignation().equals(entity.getDesignation())) {
                        return new ResultatAction<>(true, y, y.getId(), "Succès", false);
                    } else {
                        return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez pas associer deux natures de compte à cette nature");

                    }
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseNatureCompte.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseNatureCompte> save(YvsBaseNatureCompte entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_nature_compte", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseNatureCompte) dao.loadOneByNameQueries("YvsBaseNatureCompte.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseNatureCompte) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseNatureCompte.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseNatureCompte> update(YvsBaseNatureCompte entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseNatureCompte) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseNatureCompte.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseNatureCompte> delete(YvsBaseNatureCompte entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseNatureCompte.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
