/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.produits.YvsBaseArticles;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseUniteMesure extends AbstractEntity {

    public AYvsBaseUniteMesure() {
    }

    public AYvsBaseUniteMesure(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseUniteMesure> controle(YvsBaseUniteMesure entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getReference())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la référence");
            }
            if (!asString(entity.getLibelle())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la désignation");
            }
            if (entity.getSociete() != null ? entity.getSociete().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la société");
            }
            YvsBaseUniteMesure y = (YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findByCode", new String[]{"reference", "societe"}, new Object[]{entity.getReference(), entity.getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(entity.getId()) : false : false) {
                if (y.getLibelle().equals(entity.getLibelle())) {
                    return new ResultatAction<>(true, y, y.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Il existe deja une unite avec cette reference");
                }

            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseUniteMesure.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseUniteMesure> save(YvsBaseUniteMesure entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_unite_mesure", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseUniteMesure) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseUniteMesure.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseUniteMesure> update(YvsBaseUniteMesure entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseUniteMesure) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseUniteMesure.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseUniteMesure> delete(YvsBaseUniteMesure entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseUniteMesure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
