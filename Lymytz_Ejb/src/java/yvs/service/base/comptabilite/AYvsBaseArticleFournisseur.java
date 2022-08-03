/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleFournisseur;

import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseArticleFournisseur extends AbstractEntity {

    public AYvsBaseArticleFournisseur() {
    }

    public AYvsBaseArticleFournisseur(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseArticleFournisseur> controle(YvsBaseArticleFournisseur entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getArticle() != null ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "L'article ne peut pas etre null");
            }
            if (entity.getFournisseur() != null ? entity.getFournisseur().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Le fournisseur ne peut pas etre null");
            }
            YvsBaseArticleFournisseur a = (YvsBaseArticleFournisseur) dao.loadOneByNameQueries("YvsBaseArticleFournisseur.findByFsseurArt", new String[]{"fournisseur", "article"}, new Object[]{entity.getFournisseur(), entity.getArticle()});
            if (a != null ? (a.getId() > 0 ? !a.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(true, a, a.getId(), "Succès",false);
            }
            return new ResultatAction<>(true, entity, entity.getId(), "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseArticleFournisseur.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsBaseArticleFournisseur> save(YvsBaseArticleFournisseur entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_article_fournisseur", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseArticleFournisseur) dao.loadOneByNameQueries("YvsBaseArticleFournisseur.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseArticleFournisseur) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleFournisseur.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseArticleFournisseur> update(YvsBaseArticleFournisseur entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseArticleFournisseur) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleFournisseur.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseArticleFournisseur> delete(YvsBaseArticleFournisseur entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseArticleFournisseur.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
