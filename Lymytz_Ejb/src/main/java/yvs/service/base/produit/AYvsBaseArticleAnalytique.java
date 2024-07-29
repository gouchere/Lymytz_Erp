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
import yvs.entity.base.YvsBaseArticleAnalytique;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseArticleAnalytique extends AbstractEntity {

    public AYvsBaseArticleAnalytique() {
    }

    public AYvsBaseArticleAnalytique(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseArticleAnalytique> controle(YvsBaseArticleAnalytique entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getArticle() != null ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner l'article");
            }
            if (entity.getCentre() != null ? entity.getCentre().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner le centre analytique");
            }

            double taux = entity.getCoefficient();
            for (YvsBaseArticleAnalytique a : entity.getArticle().getAnalytiques()) {
                if (!a.getId().equals(entity.getId()) ? a.getCentre().getId().equals(entity.getCentre().getId()) : false) {
                    return new ResultatAction<>(false, entity, 0L, "Vous avez deja associé ce centre analytique");
                }
                if (!a.getId().equals(entity.getId())) {
                    taux += a.getCoefficient();
                }
            }
            if (taux > 100) {
                return new ResultatAction<>(false, entity, 0L, "La repartition analytique ne peut pas etre supérieur à 100%");
            }
            YvsBaseArticleAnalytique t = (YvsBaseArticleAnalytique) dao.loadOneByNameQueries("YvsBaseArticleAnalytique.findByArticleCentres", new String[]{"article", "centre"}, new Object[]{entity.getArticle(), entity.getCentre()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(true, t, t.getId(), "Succès", false);
            }

            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleAnalytique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseArticleAnalytique> save(YvsBaseArticleAnalytique entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_article_analytique", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseArticleAnalytique) dao.loadOneByNameQueries("YvsBaseArticleAnalytique.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseArticleAnalytique) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleAnalytique.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseArticleAnalytique> update(YvsBaseArticleAnalytique entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseArticleAnalytique) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleAnalytique.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseArticleAnalytique> delete(YvsBaseArticleAnalytique entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleAnalytique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
