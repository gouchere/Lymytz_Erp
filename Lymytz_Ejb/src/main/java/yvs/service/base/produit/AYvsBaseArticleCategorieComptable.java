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
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseArticleCategorieComptable extends AbstractEntity {

    public AYvsBaseArticleCategorieComptable() {
    }

    public AYvsBaseArticleCategorieComptable(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseArticleCategorieComptable> controle(YvsBaseArticleCategorieComptable entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getArticle() != null ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner l'article");
            }

            if (entity.getCategorie() != null ? entity.getCategorie().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner la categorie comptable");
            }

            YvsBaseArticleCategorieComptable t = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries("YvsBaseArticleCategorieComptable.findByCategorieArticle", new String[]{"article", "categorie"}, new Object[]{entity.getArticle(), entity.getCategorie()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(true, t, t.getId(), "Succès", false);
            }

            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleEquivalent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseArticleCategorieComptable> save(YvsBaseArticleCategorieComptable entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_article_categorie_comptable", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries("YvsBaseArticleCategorieComptable.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseArticleCategorieComptable) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleCategorieComptable.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseArticleCategorieComptable> update(YvsBaseArticleCategorieComptable entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseArticleCategorieComptable) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleCategorieComptable.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseArticleCategorieComptable> delete(YvsBaseArticleCategorieComptable entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleCategorieComptable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
