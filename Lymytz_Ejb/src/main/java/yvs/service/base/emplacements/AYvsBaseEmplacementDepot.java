/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.emplacements;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseEmplacementDepot;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseEmplacementDepot extends AbstractEntity {

    public AYvsBaseEmplacementDepot() {
    }

    public AYvsBaseEmplacementDepot(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseEmplacementDepot> controle(YvsBaseEmplacementDepot entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getDepot() != null ? entity.getDepot().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner un depot");
            }
            if (!asString(entity.getCode())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner le code");
            }
            if (!asString(entity.getDesignation())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la désignation");
            }
            YvsBaseDepots depot = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{entity.getDepot().getId()});
            if (depot != null ? depot.getId() > 0 : false) {
                YvsBaseEmplacementDepot y = (YvsBaseEmplacementDepot) dao.loadOneByNameQueries("YvsBaseEmplacementDepot.findByCurrentCode", new String[]{"code", "societe"}, new Object[]{entity.getCode(), depot.getAgence().getSociete()});
                if (y != null ? y.getId() > 0 ? !y.getId().equals(entity.getId()) : false : false) {
                    if (y.getDesignation().equals(entity.getDesignation())) {
                        return new ResultatAction<>(true, y, y.getId(), "Succès", false);
                    } else {
                        return new ResultatAction<>(false, entity, 0L, "Il existe deja un emplacement avec ce code");
                    }

                }
            } else {
                return new ResultatAction<>(false, entity, 0L, "Ce dépot n'existe pas !");
            }

            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseEmplacementDepot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseEmplacementDepot> save(YvsBaseEmplacementDepot entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_emplacement_depot", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseEmplacementDepot) dao.loadOneByNameQueries("YvsBaseEmplacementDepot.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseEmplacementDepot) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseEmplacementDepot.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseEmplacementDepot> update(YvsBaseEmplacementDepot entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseEmplacementDepot) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseEmplacementDepot.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseEmplacementDepot> delete(YvsBaseEmplacementDepot entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseEmplacementDepot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
