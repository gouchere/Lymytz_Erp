/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBasePlanTarifaire extends AbstractEntity {

    String[] champ;
    Object[] val;
    String nameQueri;

    public AYvsBasePlanTarifaire() {
    }

    public AYvsBasePlanTarifaire(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBasePlanTarifaire> controle(YvsBasePlanTarifaire entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getArticle() != null ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner l'article");
            }
            if (entity.getConditionnement() != null ? entity.getConditionnement().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le conditionnement de l'article");
            }

            if (entity.getCategorie() != null ? entity.getCategorie().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier la categorie");
            }
            if (entity.getArticle() != null ? entity.getArticle().getId() > 0 : false) {
                champ = new String[]{"categorie", "article"};
                val = new Object[]{new YvsBaseCategorieClient(entity.getCategorie().getId()), new YvsBaseArticles(entity.getArticle().getId())};
                nameQueri = "YvsBasePlanTarifaire.findByCategorieArticle";
            } else {
                champ = new String[]{"categorie", "famille"};
                val = new Object[]{new YvsBaseCategorieClient(entity.getCategorie().getId()), new YvsBaseFamilleArticle(entity.getFamille().getId())};
                nameQueri = "YvsBasePlanTarifaire.findByCategorieFamille";
            }
            List<YvsBasePlanTarifaire> lp = dao.loadNameQueries(nameQueri, champ, val);
            if (lp != null ? !lp.isEmpty() : false) {
                if (!lp.get(0).getId().equals(entity.getId())) {
                    return new ResultatAction<>(true, lp.get(0), lp.get(0).getId(), "Succes", false);
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePlanTarifaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBasePlanTarifaire> save(YvsBasePlanTarifaire entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_plan_tarifaire", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBasePlanTarifaire) dao.loadOneByNameQueries("YvsBasePlanTarifaire.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBasePlanTarifaire) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePlanTarifaire.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBasePlanTarifaire> update(YvsBasePlanTarifaire entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBasePlanTarifaire) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePlanTarifaire.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBasePlanTarifaire> delete(YvsBasePlanTarifaire entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePlanTarifaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
