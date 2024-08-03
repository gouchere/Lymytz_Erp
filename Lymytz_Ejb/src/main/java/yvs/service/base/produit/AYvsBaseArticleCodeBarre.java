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
import yvs.entity.produits.YvsBaseArticleCodeBarre;

import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseArticleCodeBarre extends AbstractEntity {

    public AYvsBaseArticleCodeBarre() {
    }

    public AYvsBaseArticleCodeBarre(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseArticleCodeBarre> controle(YvsBaseArticleCodeBarre entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (!asString(entity.getCodeBarre())) {
                return new ResultatAction<>(false, entity, 0L, "Le code barre ne peut pas etre vide");
            }
            if ((entity.getConditionnement() != null) ? entity.getConditionnement().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Le conditionnement ne peut pas etre null");
            }
            YvsBaseArticleCodeBarre t = (YvsBaseArticleCodeBarre) dao.loadOneByNameQueries("YvsBaseArticleCodeBarre.findByCodeConditionnement", new String[]{"conditionnement", "codeBarre"}, new Object[]{entity.getConditionnement(), entity.getCodeBarre()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(true, t, t.getId(), "Succès", false);
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleCodeBarre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseArticleCodeBarre> save(YvsBaseArticleCodeBarre entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_article_code_barre", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseArticleCodeBarre) dao.loadOneByNameQueries("YvsBaseArticleCodeBarre.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseArticleCodeBarre) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleCodeBarre.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseArticleCodeBarre> update(YvsBaseArticleCodeBarre entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseArticleCodeBarre) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleCodeBarre.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseArticleCodeBarre> delete(YvsBaseArticleCodeBarre entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleCodeBarre.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
