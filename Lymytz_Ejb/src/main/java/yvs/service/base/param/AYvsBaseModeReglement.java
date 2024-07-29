/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseModeReglement extends AbstractEntity {

    public AYvsBaseModeReglement() {
    }

    public AYvsBaseModeReglement(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseModeReglement> controle(YvsBaseModeReglement entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getDesignation())) {
                return new ResultatAction<>(false, entity, 0L, "vous  devez renseigner la désignation");
            }
            if (entity.getDefaultMode()) {
                String nameQueri = "YvsBaseModeReglement.findByDefault";
                String[] champ = new String[]{"societe", "actif", "type", "defaut"};
                Object[] val = new Object[]{entity.getSociete(), true, entity.getTypeReglement(), true};
                YvsBaseModeReglement y = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
                if (y != null ? (y.getId() != null ? !y.getId().equals(entity.getId()) : false) : false) {
                    return new ResultatAction<>(false, entity, 0L, "Vous avez déjà un mode par défaut pour ce type");
                }
            }

            String nameQueri = "YvsBaseModeReglement.findByDesignation";
            String[] champ = new String[]{"societe", "designation"};
            Object[] val = new Object[]{entity.getSociete(), entity.getDesignation()};
            YvsBaseModeReglement y = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (y != null ? (y.getId() != null ? !y.getId().equals(entity.getId()) : false) : false) {
                if (y.getDesignation().equals(entity.getDesignation())) {
                    return new ResultatAction<>(true, y, Long.valueOf(y.getId()), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Vous avez déjà un mode avec cette désignation");
                }

            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseModeReglement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseModeReglement> save(YvsBaseModeReglement entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_mode_reglement", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseModeReglement) dao.loadOneByNameQueries("YvsBaseModeReglement.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseModeReglement) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, Long.valueOf(entity.getId()), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseModeReglement.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseModeReglement> update(YvsBaseModeReglement entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseModeReglement) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, Long.valueOf(entity.getId()), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseModeReglement.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseModeReglement> delete(YvsBaseModeReglement entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, Long.valueOf(entity.getId()), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseModeReglement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
