/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.produits.YvsBaseArticleDescription;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseArticleDescription extends AbstractEntity {

    public AYvsBaseArticleDescription() {
    }

    public AYvsBaseArticleDescription(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseArticleDescription> controle(YvsBaseArticleDescription entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getArticle() != null ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner l'article");
            }
            if (!asString(entity.getTitre())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner le titre");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleDescription.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseArticleDescription> save(YvsBaseArticleDescription entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_article_description", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseArticleDescription) dao.loadOneByNameQueries("YvsBaseArticleDescription.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseArticleDescription) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleDescription.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseArticleDescription> update(YvsBaseArticleDescription entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseArticleDescription) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleDescription.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseArticleDescription> delete(YvsBaseArticleDescription entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleDescription.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
