/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.produits.YvsBaseArticles;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseArticlePoint extends AbstractEntity {

    public AYvsBaseArticlePoint() {
    }

    public AYvsBaseArticlePoint(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseArticlePoint> controle(YvsBaseArticlePoint entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getArticle() != null ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner l'article");
            }
            if (entity.getPoint() != null ? entity.getPoint().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner le depot");
            }
            String[] champ = new String[]{"article", "point"};
            Object[] val = new Object[]{new YvsBaseArticles(entity.getArticle().getId()), new YvsBasePointVente(entity.getPoint().getId())};
            String nameQueri = "YvsBaseArticlePoint.findByArticlePoint";
            YvsBaseArticlePoint a = (YvsBaseArticlePoint) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (a != null ? (a.getId() > 0 ? !a.getId().equals(entity.getId()) : false) : false) {
                System.err.println("entity = " + entity);
                return new ResultatAction<>(true, a, a.getId(), "Succès", false);
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticlePoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseArticlePoint> save(YvsBaseArticlePoint entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_article_point", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseArticlePoint) dao.loadOneByNameQueries("YvsBaseArticlePoint.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseArticlePoint) dao.save1(entity);
                }

                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticlePoint.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseArticlePoint> update(YvsBaseArticlePoint entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseArticlePoint) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticlePoint.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseArticlePoint> delete(YvsBaseArticlePoint entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticlePoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
