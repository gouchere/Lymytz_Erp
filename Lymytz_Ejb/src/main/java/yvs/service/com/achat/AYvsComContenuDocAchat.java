/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.achat;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.produits.YvsBaseArticles;
import yvs.service.AbstractEntity;
import yvs.service.UtilRebuild;
import yvs.service.com.vente.AYvsComContenuDocVente;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComContenuDocAchat extends AbstractEntity {

    public AYvsComContenuDocAchat() {
    }

    public AYvsComContenuDocAchat(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComContenuDocAchat> controle(YvsComContenuDocAchat entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getDocAchat() != null ? entity.getDocAchat().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier la facture");
            }
            if ((entity.getArticle() != null) ? entity.getArticle().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier l'article");
            }
            if ((entity.getConditionnement() != null) ? entity.getConditionnement().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le conditionnement");
            }

            if (entity.getPrixAchat() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier un prix d'achat");
            }
            if (entity.getQuantiteCommande() < 1) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer une quantité valide");
            }

            if (!entity.getDocAchat().getStatut().equals(Constantes.ETAT_EDITABLE) && !entity.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez pas modifier cette ligne car le document est " + giveNameStatut(entity.getDocAchat().getStatut()).toLowerCase() + " et la ligne est valide");
            }
            String[] champ = new String[]{"article", "fournisseur"};
            System.err.println("article = " + entity.getArticle().getId());

            YvsComDocAchats docAchat = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{entity.getDocAchat().getId()});
            System.err.println("fournisseur = " + docAchat.getFournisseur().getId());
            Object[] val = new Object[]{new YvsBaseArticles(entity.getArticle().getId()), new YvsBaseFournisseur(docAchat.getFournisseur().getId())};
            YvsBaseArticleFournisseur a = (YvsBaseArticleFournisseur) dao.loadOneByNameQueries("YvsBaseArticleFournisseur.findByFsseurArt", champ, val);
            if (a != null ? a.getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Impossible d'enregistre car le fournisseur ne possède pas cet article");
            }
            System.err.println("statut facture = " + docAchat.getStatutLivre());
            if (docAchat.getStatutLivre().equals(Constantes.ETAT_LIVRE) && entity.getId() > 0) {
                String result = dao.controleStock(entity.getArticle().getId(), (entity.getConditionnement() != null ? entity.getConditionnement().getId() : 0), docAchat.getDepotReception().getId(), 0L, entity.getQuantiteCommande(), entity.getQuantiteRecu(), "UPDATE", "S", docAchat.getDateDoc(), (entity.getLot() != null ? entity.getLot().getId() : 0));
                if (result != null) {
                    return new ResultatAction<>(false, entity, 0L, "L'article '" + entity.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                }
            }
            System.err.println("statut controle");
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComContenuDocAchat.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsComContenuDocAchat> save(YvsComContenuDocAchat entity) {

        ResultatAction result = controle(entity);
        System.err.println("result controle contenu = " + result.getMessage());
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_contenu_doc_achat", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComContenuDocAchat) dao.loadOneByNameQueries("YvsComContenuDocAchat.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComContenuDocAchat) dao.save1(entity);
                }
                System.err.println(" id contenu achat = " + entity.getId());
                entity.setDocAchat(new YvsComDocAchats(entity.getDocAchat().getId()));
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContenuDocAchat.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComContenuDocAchat> update(YvsComContenuDocAchat entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComContenuDocAchat) dao.update(entity);
                entity.setDocAchat(new YvsComDocAchats(entity.getDocAchat().getId()));
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContenuDocAchat.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComContenuDocAchat> delete(YvsComContenuDocAchat entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            entity.setDocAchat(new YvsComDocAchats(entity.getDocAchat().getId()));
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComContenuDocAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public String giveNameStatut(String statut) {
        switch (statut) {
            case Constantes.ETAT_ATTENTE:
                return "EN ATTENTE";
            case Constantes.ETAT_RENVOYE:
                return "RENVOYE";
            case Constantes.ETAT_ANNULE:
                return "ANNULE";
            case Constantes.ETAT_ENCOURS:
                return "EN COURS";
            case Constantes.ETAT_REGLE:
                return "REGLE";
            case Constantes.ETAT_SUSPENDU:
                return "SUSPENDU";
            case Constantes.ETAT_TERMINE:
                return "TERMINE";
            case Constantes.ETAT_VALIDE:
                return "VALIDE";
            case Constantes.ETAT_SOUMIS:
                return "TRANSMIS";
            case Constantes.ETAT_LIVRE:
                return "LIVRE";
            case Constantes.ETAT_CLOTURE:
                return "CLOTURE";
            default:
                return "EDITABLE";
        }
    }

    public ResultatAction<YvsComContenuDocAchat> returnQuantiteLivre(YvsComContenuDocAchat contenu) {
        try {
            String[] champ = new String[]{"article", "unite", "docAchat"};
            Object[] val = new Object[]{contenu.getArticle(), contenu.getConditionnement(), new YvsComDocAchats(contenu.getDocAchat().getId())};
            Double bonus = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteBonusByArticle", champ, val);
            bonus = bonus != null ? bonus : 0;
            Double qte = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticles", new String[]{"article", "docAchat", "unite"}, new Object[]{contenu.getArticle(), contenu.getDocAchat(), contenu.getConditionnement()});
            qte = qte != null ? qte : 0;
            System.err.println("qte = " + qte);
            if (contenu.isBonus()) {
                qte += bonus;
            }

            Double rem = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findRemByArticle", champ, val);
            rem = rem != null ? rem : 0;
            Double tax = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findTaxeByArticle", champ, val);
            tax = tax != null ? tax : 0;
            Double pt = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findPTByArticle", champ, val);
            pt = pt != null ? pt : 0;

            double prix = (pt - tax + rem) / qte;
            System.err.println("prix = " + prix);
            contenu.setQuantiteCommande(contenu.isBonus() ? bonus : qte);
            contenu.setTaxe(tax);
            contenu.setRemise(rem);
            contenu.setPrixAchat(prix);
            contenu.setPrixTotal(pt);
            contenu.setParent(new YvsComContenuDocAchat(contenu.getId()));
            contenu.setQuantiteRecu(0.0);
            if (qte > 0) {
                String[] ch = new String[]{"docAchat", "typeDoc", "statut", "article", "unite"};
                Object[] v = new Object[]{new YvsComDocAchats(contenu.getDocAchat().getId()), Constantes.TYPE_BLA, Constantes.ETAT_VALIDE, contenu.getArticle(), contenu.getConditionnement()};
                Double liv = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findByDocLierTypeStatutArticleS", ch, v);
                liv = liv != null ? liv : 0;

                if (qte > liv) {
                    double reste = qte - liv;
                    contenu.setQuantiteRecu(!contenu.isBonus() ? reste : (reste > bonus ? bonus : reste));
                }
                return new ResultatAction(true, UtilRebuild.reBuildContenuAchat(contenu), contenu.getId(), "Succès");
            }
            return new ResultatAction(true, UtilRebuild.reBuildContenuAchat(contenu), contenu.getId(), "Déja livré");
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction(false, UtilRebuild.reBuildContenuAchat(contenu), contenu.getId(), "Action Impossible");
    }

}
