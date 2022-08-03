/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.emplacements;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepots;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseDepots extends AbstractEntity {

    public AYvsBaseDepots() {
    }

    public AYvsBaseDepots(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseDepots> controle(YvsBaseDepots entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getCode())) {
                return new ResultatAction<>(false, entity, 0L, "Le code ne peut pas etre vide");
            }
            if ((entity.getAgence() != null) ? entity.getAgence().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "L'agence ne peut pas etre vide");
            }
            if (!asString(entity.getDesignation())) {
                return new ResultatAction<>(false, entity, 0L, "La désignation ne peut pas etre vide");
            }
            YvsBaseDepots t = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findByCode", new String[]{"societe", "code"}, new Object[]{entity.getAgence().getSociete(), entity.getCode()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                if (t.getDesignation().equals(entity.getDesignation())) {
                    return new ResultatAction<>(true, t, t.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Ce depot existe déjà, veuillez changer de code");
                }

            }

            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseDepots.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsBaseDepots> save(YvsBaseDepots entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_depots", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseDepots) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseDepots.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseDepots> update(YvsBaseDepots entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseDepots) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseDepots.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseDepots> delete(YvsBaseDepots entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseDepots.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
