/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.stocks;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComContenuDocStock extends AbstractEntity {

    public AYvsComContenuDocStock() {
    }

    public AYvsComContenuDocStock(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComContenuDocStock> controle(YvsComContenuDocStock entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getDocStock() != null ? entity.getDocStock().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier un document");
            }
            if ((entity.getArticle() != null) ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier l'article");
            }
            if ((entity.getConditionnement() != null) ? entity.getConditionnement().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le conditionnement");
            }
            if (!entity.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                return new ResultatAction<>(false, entity, 0L, "La ligne doit être editable");
            }
            if (entity.getDocStock().getStatut().equals(Constantes.ETAT_VALIDE)) {
                String result = null;
                if (entity.getDocStock().getTypeDoc().equals(Constantes.TYPE_SS)) {
                    result = controleStock(entity.getArticle().getId(), entity.getConditionnement().getId(), entity.getConditionnement().getId(), entity.getDocStock().getSource().getId(), 0L, entity.getQuantite(), (entity != null ? entity.getQuantite() : 0), entity.getId() > 0 ? "UPDATE" : "INSERT", "S", entity.getDocStock().getDateDoc(), (entity.getLotSortie() != null ? entity.getLotSortie().getId() : 0));
                }
                if (result != null) {
                    return new ResultatAction<>(false, entity, 0L, "L'article '" + entity.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComContenuDocStock.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsComContenuDocStock> save(YvsComContenuDocStock entity) {
        ResultatAction result = controle(entity);
        System.err.println("result controle contenu = " + result.getMessage());
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_contenu_doc_stock", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComContenuDocStock) dao.loadOneByNameQueries("YvsComContenuDocStock.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComContenuDocStock) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, result.getMessage());
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContenuDocStock.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, result.getMessage());
        }
        return result;
    }

    public ResultatAction<YvsComContenuDocStock> update(YvsComContenuDocStock entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComContenuDocStock) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContenuDocStock.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComContenuDocStock> delete(YvsComContenuDocStock entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContenuDocStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public String controleStock(long article, long conditionnement, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return dao.controleStock(article, conditionnement, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }

    public String controleStock(long article, long conditionnement, long oldCond, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return dao.controleStock(article, conditionnement, oldCond, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }
}
