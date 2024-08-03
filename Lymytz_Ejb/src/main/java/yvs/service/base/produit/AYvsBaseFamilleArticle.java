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
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz Dowes
 */
public abstract class AYvsBaseFamilleArticle extends AbstractEntity {

    public AYvsBaseFamilleArticle() {
    }

    public AYvsBaseFamilleArticle(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseFamilleArticle> controle(YvsBaseFamilleArticle entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getReferenceFamille())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la référence");
            }
            if (!asString(entity.getDesignation())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la désignation");
            }
            if (entity.getSociete() != null ? entity.getSociete().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la societe");
            }
            YvsBaseFamilleArticle y = (YvsBaseFamilleArticle) dao.loadOneByNameQueries("YvsBaseFamilleArticle.findByReference", new String[]{"societe", "code"}, new Object[]{entity.getSociete(), entity.getReferenceFamille()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(entity.getId()) : false : false) {
                if (y.getDesignation().equals(entity.getDesignation())) {
                    return new ResultatAction<>(true, y, y.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Il existe deja une famille avec cette reference");
                }

            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseFamilleArticle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseFamilleArticle> save(YvsBaseFamilleArticle entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_famille_article", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseFamilleArticle) dao.loadOneByNameQueries("YvsBaseFamilleArticle.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseFamilleArticle) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseFamilleArticle.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseFamilleArticle> update(YvsBaseFamilleArticle entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseFamilleArticle) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseFamilleArticle.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseFamilleArticle> delete(YvsBaseFamilleArticle entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseFamilleArticle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
