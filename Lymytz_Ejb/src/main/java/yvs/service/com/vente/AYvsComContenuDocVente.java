/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.produits.YvsBaseArticles;
import yvs.service.AbstractEntity;
import yvs.service.UtilRebuild;
import yvs.service.com.param.IYvsComLotReception;
import yvs.service.com.param.SYvsComLotReception;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComContenuDocVente extends AbstractEntity {
    
    public AYvsComContenuDocVente() {
    }
    
    public AYvsComContenuDocVente(DaoInterfaceWs dao) {
        this.dao = dao;
    }
    
    public ResultatAction<YvsComContenuDocVente> controle(YvsComContenuDocVente entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getDocVente() == null ? true : entity.getDocVente().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Le documente de vente ne peut pas etre null");
            }
            if ((entity.getArticle() != null) ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier l'article");
            }
            if ((entity.getConditionnement() != null) ? entity.getConditionnement().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le conditionnement");
            }
            if (entity.getQuantite() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer la quantitée");
            }
            if (entity.getPrix() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le prix de vente");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComContenuDocVente.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }
    
    public ResultatAction<YvsComContenuDocVente> save(YvsComContenuDocVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_contenu_doc_vente", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComContenuDocVente) dao.loadOneByNameQueries("YvsComContenuDocVente.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    //met à jour le pr
                    if (Util.asLong(entity.getDocVente().getId())) {
                        YvsComDocVentes dv = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{entity.getDocVente().getId()});
                        if (dv != null ? dv.getEnteteDoc().getAgence() != null ? dv.getDepotLivrer() != null : false : false) {
                            try {
                                entity.setPr(dao.getPr(dv.getEnteteDoc().getAgence().getId(), entity.getArticle().getId(), dv.getDepotLivrer().getId(), 0L, dv.getEnteteDoc().getDateEntete(), entity.getConditionnement().getId()));
                            } catch (Exception ex) {
                                Logger.getLogger(AYvsComContenuDocVente.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    entity = (YvsComContenuDocVente) dao.save1(entity);
                }
                entity.setDocVente(new YvsComDocVentes(entity.getDocVente().getId()));
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContenuDocVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }
    
    public ResultatAction<YvsComContenuDocVente> update(YvsComContenuDocVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComContenuDocVente) dao.update(entity);
                entity.setDocVente(new YvsComDocVentes(entity.getDocVente().getId()));
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContenuDocVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }
    
    public ResultatAction<YvsComContenuDocVente> delete(YvsComContenuDocVente entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            entity.setDocVente(new YvsComDocVentes(entity.getDocVente().getId()));
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContenuDocVente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public ResultatAction<YvsComContenuDocVente> returnQuantiteLivre(YvsComContenuDocVente contenu) {
        try {
            String[] champ = new String[]{"article", "unite", "docVente"};
            Object[] val = new Object[]{contenu.getArticle(), contenu.getConditionnement(), contenu.getDocVente()};
            Double bonus = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteBonusByArticle", champ, val);
            bonus = bonus != null ? bonus : 0;
            Double qte = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", champ, val);
            qte = qte != null ? qte : 0;
            if (contenu.isBonus()) {
                qte += bonus;
            }
            if (qte > 0) {
                Double rem = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findRemByArticle", champ, val);
                rem = rem != null ? rem : 0;
                Double tax = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findTaxeByArticle", champ, val);
                tax = tax != null ? tax : 0;
                Double pt = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findPTByArticle", champ, val);
                pt = pt != null ? pt : 0;
                
                double prix = (pt - tax + rem) / qte;
                String[] ch = new String[]{"docVente", "typeDoc", "statut", "article", "unite"};
                Object[] v = new Object[]{contenu.getDocVente(), Constantes.TYPE_BLV, Constantes.ETAT_VALIDE, contenu.getArticle(), contenu.getConditionnement()};
                Double liv = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", ch, v);
                liv = liv != null ? liv : 0;
                contenu.setQuantite_(contenu.isBonus() ? bonus : qte);
                contenu.setTaxe(tax);
                contenu.setRemise(rem);
                contenu.setPrix(prix);
                contenu.setPrixTotal(pt);
                contenu.setParent(new YvsComContenuDocVente(contenu.getId()));
                if (qte > liv) {
                    double reste = qte - liv;
                    contenu.setQteLivree(liv);
                    contenu.setQuantite(!contenu.isBonus() ? reste : (reste > bonus ? bonus : reste));
                } else {
                    contenu.setQuantite(0.0);
                    contenu.setQteLivree(contenu.getQuantite_());
                }
                YvsBaseDepots depot = contenu.getDepoLivraisonPrevu();
                if (depot != null ? depot.getId() > 0 : false) {
                    String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                    Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(contenu.getArticle().getId(), 1), new Options(depot.getId(), 2)});
                    contenu.getArticle().setUseLotInDepot(requiere_lot);
                    if (contenu.getArticle().isUseLotInDepot()) {
                        IYvsComLotReception service = new SYvsComLotReception((DaoInterfaceWs) dao);
                        if (service != null) {
                            contenu.setLots(service.loadList(depot.getId(), contenu.getConditionnement().getId(), null, 0, 0));
                        }
                    }
                }
                return new ResultatAction(true, UtilRebuild.reBuildContenuVente(contenu), contenu.getId(), "Succès");
            }
            return new ResultatAction(true, UtilRebuild.reBuildContenuVente(contenu), contenu.getId(), "Déja livré");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction(false, UtilRebuild.reBuildContenuVente(contenu), contenu.getId(), "Action Impossible");
    }
}
