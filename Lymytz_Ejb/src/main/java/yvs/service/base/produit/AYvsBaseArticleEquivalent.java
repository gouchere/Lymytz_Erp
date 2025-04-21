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
import yvs.entity.base.YvsBaseArticleEquivalent;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseArticleEquivalent extends AbstractEntity {

    public AYvsBaseArticleEquivalent() {
    }

    public AYvsBaseArticleEquivalent(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseArticleEquivalent> controle(YvsBaseArticleEquivalent entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getArticle() != null ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner l'article");
            }

            if (entity.getArticleEquivalent() != null ? entity.getArticleEquivalent().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner l'article équivalent");
            }
            YvsBaseArticleEquivalent y = (YvsBaseArticleEquivalent) dao.loadOneByNameQueries("YvsBaseArticleEquivalent.findByEquivalence", new String[]{"article", "article"}, new Object[]{entity.getArticle(), entity.getArticleEquivalent()});
            if (y != null ? (y.getId() > 0 ? y.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez pas lier le même article");
            }

            y = (YvsBaseArticleEquivalent) dao.loadOneByNameQueries("YvsBaseArticleEquivalent.findByArticle", new String[]{"article", "articleEquivalent"}, new Object[]{entity.getArticle(), entity.getArticleEquivalent()});
            if (y != null ? (y.getId() > 0 ? y.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(true, y, y.getId(), "Succès", false);
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleEquivalent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseArticleEquivalent> save(YvsBaseArticleEquivalent entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_article_equivalent", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseArticleEquivalent) dao.loadOneByNameQueries("YvsBaseArticleEquivalent.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseArticleEquivalent) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleEquivalent.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseArticleEquivalent> update(YvsBaseArticleEquivalent entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseArticleEquivalent) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleEquivalent.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseArticleEquivalent> delete(YvsBaseArticleEquivalent entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleEquivalent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
