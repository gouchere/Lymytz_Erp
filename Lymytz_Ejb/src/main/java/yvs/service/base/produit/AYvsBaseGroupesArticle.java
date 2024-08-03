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
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz Dowes
 */
public abstract class AYvsBaseGroupesArticle extends AbstractEntity {

    public AYvsBaseGroupesArticle() {
    }

    public AYvsBaseGroupesArticle(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseGroupesArticle> controle(YvsBaseGroupesArticle entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getRefgroupe())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la référence");
            }
            if (!asString(entity.getDesignation())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner la désignation");
            }
            YvsBaseGroupesArticle y = (YvsBaseGroupesArticle) dao.loadOneByNameQueries("YvsBaseGroupesArticle.findByRefgroupes", new String[]{"refgroupe", "societe"}, new Object[]{entity.getRefgroupe(), entity.getSociete()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(entity.getId()) : false : false) {
                if (y.getDesignation().equals(entity.getDesignation())) {
                    return new ResultatAction<>(false, y, y.getId(), "Succès", true);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Il exite déjà un groupe avec cette reference");
                }

            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseGroupesArticle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseGroupesArticle> save(YvsBaseGroupesArticle entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_groupes_article", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseGroupesArticle) dao.loadOneByNameQueries("YvsBaseGroupesArticle.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseGroupesArticle) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseGroupesArticle.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseGroupesArticle> update(YvsBaseGroupesArticle entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseGroupesArticle) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseGroupesArticle.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseGroupesArticle> delete(YvsBaseGroupesArticle entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseGroupesArticle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
