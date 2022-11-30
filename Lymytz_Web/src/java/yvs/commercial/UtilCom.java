/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial;

import yvs.commercial.commission.PlanCommission;
import yvs.base.compta.Taxes;
import yvs.commercial.param.Parametre;
import yvs.commercial.rrr.Ristourne;
import yvs.commercial.rrr.Remise;
import yvs.commercial.depot.ArticleDepot;
import yvs.commercial.creneau.TypeCreneau;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.CategorieFournisseur;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import yvs.base.compta.ArticleTaxe;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.base.compta.InfosModeReglement;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.ModeReglementBanque;
import yvs.base.compta.ModelReglement;
import yvs.base.compta.TrancheReglement;
import yvs.base.compta.UtilCompta;
import static yvs.base.compta.UtilCompta.buildBeanCompte;
import static yvs.base.compta.UtilCompta.buildBeanTypeDoc;
import yvs.base.produits.ArticleFournisseur;
import yvs.base.produits.Articles;
import yvs.base.produits.ArticlesCatComptable;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.FamilleArticle;
import yvs.base.produits.TemplateArticles;
import yvs.production.UtilProd;
import yvs.commercial.achat.ArticleApprovisionnement;
import yvs.commercial.achat.ArticleFourniAchat;
import yvs.commercial.achat.ContenuDocAchat;
import yvs.commercial.achat.DocAchat;
import yvs.commercial.achat.FicheApprovisionnement;
import yvs.commercial.achat.MensualiteFactureAchat;
import yvs.commercial.achat.CritereLot;
import yvs.commercial.achat.LotReception;
import yvs.comptabilite.tresorerie.MensualiteDocDivers;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.commercial.client.CategorieClient;
import yvs.commercial.client.Client;
import yvs.commercial.client.PlanReglementClient;
import yvs.commercial.client.TrancheReglementClient;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.creneau.CreneauUsers;
import yvs.commercial.fournisseur.OpeCptFsseur;
import yvs.commercial.fournisseur.PlanReglementFournisseur;
import yvs.commercial.fournisseur.TrancheReglementFournisseur;
import yvs.commercial.param.ParametreCom;
import yvs.commercial.rrr.GrilleRabais;
import yvs.commercial.rrr.PlanRistourne;
import yvs.commercial.stock.ContenuDocStock;
import yvs.commercial.stock.CoutSupDoc;
import yvs.commercial.stock.DocStock;
import yvs.commercial.stock.MouvementStock;
import yvs.commercial.vente.ContenuDocVente;
import yvs.commercial.vente.DocVente;
import yvs.commercial.vente.EnteteDocVente;
import yvs.commercial.vente.MensualiteFactureVente;
import yvs.commercial.vente.RemiseDocVente;
import yvs.commercial.vente.RistourneDocVente;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseCategorieFournisseur;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseEmplacementDepot;
import yvs.entity.compta.divers.YvsComptaCaisseMensualiteDocDivers;
import yvs.entity.base.YvsBaseMouvementStock;
import yvs.entity.base.YvsBasePlanReglementFournisseur;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBasePointVenteDepot;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseTrancheReglementFournisseur;
import yvs.entity.commercial.YvsBaseOperationCompteFsseur;
import yvs.entity.commercial.YvsComCategoriePersonnel;
import yvs.entity.commercial.YvsComCritereLot;
import yvs.entity.users.YvsUsers;
import yvs.entity.commercial.YvsComLiaisonDepot;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreAchat;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.commission.YvsComPlanCommission;
//import yvs.entity.commercial.rrr.YvsComRemise;
import yvs.entity.commercial.achat.YvsComArticleApprovisionnement;
import yvs.entity.commercial.achat.YvsComArticleFourniAchat;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.achat.YvsComMensualiteFactureAchat;
import yvs.entity.commercial.client.YvsComPlanReglementClient;
import yvs.entity.commercial.client.YvsComTrancheReglementClient;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.rrr.YvsComGrilleRemise;
import yvs.entity.commercial.rrr.YvsComGrilleRistourne;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.commercial.rrr.YvsComRistourne;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComCoutSupDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.commercial.vente.YvsComRemiseDocVente;
import yvs.entity.commercial.vente.YvsComRistourneDocVente;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;

import yvs.base.tiers.Tiers;
import yvs.base.tiers.TiersTemplate;
import yvs.base.tiers.UtilTiers;
import yvs.commercial.client.CategorieTarifaire;
import yvs.commercial.client.ContratsClient;
import yvs.commercial.client.ElementAddContratsClient;
import yvs.commercial.client.ElementContratsClient;
import yvs.commercial.client.PlanTarifaireClient;
import yvs.commercial.commission.CibleFacteur;
import yvs.commercial.commission.FacteurTaux;
import yvs.commercial.commission.Periodicite;
import yvs.commercial.commission.TypeGrille;
import yvs.commercial.depot.ArticleEmplacement;
import yvs.commercial.depot.ConditionnementDepot;
import yvs.commercial.depot.ConditionnementPoint;
import yvs.commercial.depot.DepotOperation;
import yvs.commercial.depot.Emplacement;
import yvs.commercial.depot.PointLivraison;
import yvs.commercial.depot.PointVente;
import yvs.commercial.depot.PointVenteDepot;
import yvs.commercial.fournisseur.ConditionnementFsseur;
import yvs.commercial.objectifs.ModelObjectif;
import yvs.commercial.objectifs.PeriodesObjectifs;
import yvs.commercial.rations.DocRations;
import yvs.commercial.rations.Rations;
import yvs.commercial.rrr.Rabais;
import yvs.commercial.stock.DocStockValeur;
import yvs.commercial.stock.NatureDoc;
import yvs.commercial.stock.ReservationStock;
import yvs.commercial.vente.CommercialVente;
import yvs.commercial.vente.ContenuDocVenteEtat;
import yvs.commercial.vente.DocVenteInformation;
import yvs.commercial.vente.EcartVenteOrStock;
import yvs.commercial.vente.ProformaVente;
import yvs.commercial.vente.ProformaVenteContenu;
import yvs.commercial.vente.ReglementEcart;
import yvs.entity.base.YvsBaseArticleEmplacement;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseConditionnementDepot;
import yvs.entity.base.YvsBaseConditionnementFournisseur;
import yvs.entity.base.YvsBaseConditionnementPoint;
import yvs.entity.base.YvsBaseDepotOperation;
import yvs.entity.base.YvsBaseElementReference;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModeReglementBanque;
import yvs.entity.base.YvsBaseModeReglementInformations;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.base.YvsBasePointLivraison;
import yvs.entity.base.YvsBaseTrancheReglement;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.YvsComQualite;
import yvs.entity.commercial.achat.YvsComCoutSupDocAchat;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.commercial.client.YvsBasePlanTarifaireTranche;
import yvs.entity.commercial.client.YvsComCategorieTarifaire;
import yvs.entity.commercial.client.YvsComContratsClient;
import yvs.entity.commercial.client.YvsComElementAddContratsClient;
import yvs.entity.commercial.client.YvsComElementContratsClient;
import yvs.entity.commercial.client.YvsComPlanReglementCategorie;
import yvs.entity.commercial.commission.YvsComCibleFacteurTaux;
import yvs.entity.commercial.commission.YvsComFacteurTaux;
import yvs.entity.commercial.commission.YvsComPeriodeValiditeCommision;
import yvs.entity.commercial.commission.YvsComTrancheTaux;
import yvs.entity.commercial.commission.YvsComTypeGrille;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.objectifs.YvsComModeleObjectif;
import yvs.entity.commercial.objectifs.YvsComPeriodeObjectif;
import yvs.entity.commercial.ration.YvsComDocRation;
import yvs.entity.commercial.ration.YvsComPeriodeRation;
import yvs.entity.commercial.ration.YvsComRation;
import yvs.entity.commercial.rrr.YvsComGrilleRabais;
import yvs.entity.commercial.rrr.YvsComRabais;
import yvs.entity.commercial.rrr.YvsComRemise;
import yvs.entity.commercial.stock.YvsComContenuDocStockReception;
import yvs.entity.commercial.stock.YvsComDocStocksEcart;
import yvs.entity.commercial.stock.YvsComDocStocksValeur;
import yvs.entity.commercial.stock.YvsComNatureDoc;
import yvs.entity.commercial.stock.YvsComReglementEcartStock;
import yvs.entity.commercial.stock.YvsComReservationStock;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.entity.commercial.vente.YvsComContenuDocVenteEtat;
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;
import yvs.entity.commercial.vente.YvsComDocVentesInformations;
import yvs.entity.commercial.vente.YvsComEcartEnteteVente;
import yvs.entity.commercial.vente.YvsComProformaVente;
import yvs.entity.commercial.vente.YvsComProformaVenteContenu;
import yvs.entity.commercial.vente.YvsComReglementEcartVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceCommission;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.tiers.YvsBaseTiersTemplate;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.JoursOuvres;
import yvs.grh.UtilGrh;
import static yvs.grh.UtilGrh.buildBeanDictionnaire;
import yvs.grh.bean.Employe;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.agence.Agence;
import static yvs.parametrage.agence.UtilAgence.buildBeanAgence;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.entrepot.Depots;
import yvs.parametrage.entrepot.LiaisonDepot;
import yvs.theme.Theme;
import yvs.users.Users;
import yvs.users.UtilUsers;
import static yvs.users.UtilUsers.buildSimpleBeanUsers;
import yvs.util.Constantes;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
public class UtilCom {

    public static ProformaVenteContenu buildBeanProformaVenteContenu(YvsComProformaVenteContenu y) {
        ProformaVenteContenu p = new ProformaVenteContenu();
        if (y != null) {
            p.setId(y.getId());
            p.setQuantite(y.getQuantite());
            p.setPrix(y.getPrix());
            p.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            p.setProforma(new ProformaVente(y.getProforma().getId()));
            p.setDateSave(y.getDateSave());
        }
        return p;
    }

    public static YvsComProformaVenteContenu buildProformaVenteContenu(ProformaVenteContenu y, YvsUsersAgence ua) {
        YvsComProformaVenteContenu p = new YvsComProformaVenteContenu();
        if (y != null) {
            p.setId(y.getId());
            p.setQuantite(y.getQuantite());
            p.setPrix(y.getPrix());
            if ((y.getConditionnement() != null) ? y.getConditionnement().getId() > 0 : false) {
                p.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseArticles(y.getConditionnement().getArticle().getId(), y.getConditionnement().getArticle().getRefArt(), y.getConditionnement().getArticle().getDesignation()), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            if (y.getProforma() != null ? y.getProforma().getId() > 0 : false) {
                p.setProforma(new YvsComProformaVente(y.getProforma().getId()));
            }
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
            p.setAuthor(ua);
            p.setNew_(true);
        }
        return p;
    }

    public static ProformaVente buildBeanProformaVente(YvsComProformaVente y) {
        ProformaVente p = new ProformaVente();
        if (y != null) {
            p.setId(y.getId());
            p.setNumero(y.getNumero());
            p.setStatut(y.getStatut());
            p.setTelephone(y.getTelephone());
            p.setDateDoc(y.getDateDoc());
            p.setDateExpiration(y.getDateExpiration());
            p.setClient(y.getClient());
            p.setAdresse(y.getAdresse());
            p.setDateSave(y.getDateSave());
        }
        return p;
    }

    public static YvsComProformaVente buildProformaVente(ProformaVente y, YvsAgences agence, YvsUsersAgence ua) {
        YvsComProformaVente p = new YvsComProformaVente();
        if (y != null) {
            p.setId(y.getId());
            p.setNumero(y.getNumero());
            p.setStatut(y.getStatut());
            p.setTelephone(y.getTelephone());
            p.setDateDoc(y.getDateDoc());
            p.setDateExpiration(y.getDateExpiration());
            p.setClient(y.getClient());
            p.setAdresse(y.getAdresse());
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
            p.setAgence(agence);
            p.setAuthor(ua);
            p.setNew_(true);
        }
        return p;
    }

    public static ParametreCom buildBeanParametreAchat(YvsComParametreAchat y) {
        ParametreCom p = new ParametreCom();
        if (y != null) {
            p.setId(y.getId());
            p.setComptabilisationAuto(y.getComptabilisationAuto());
            p.setComptabilisationMode(y.getComptabilisationMode());
            p.setJourAnterieur(y.getJourAnterieur());
            p.setGenererFactureAuto(y.getGenererFactureAuto());
            p.setPaieWithoutValide(y.getPaieWithoutValide());
            p.setPrintDocumentWhenValide(y.getPrintDocumentWhenValide());
            p.setDateSave(y.getDateSave());
            p.setJournal(UtilCompta.buildBeanJournaux(y.getJournal()));
            p.setUpdate(true);
        }
        return p;
    }

    public static ParametreCom buildBeanParametreVente(YvsComParametreVente y) {
        ParametreCom p = new ParametreCom();
        if (y != null) {
            p.setId(y.getId());
            p.setComptabilisationAuto(y.getComptabilisationAuto());
            p.setComptabilisationMode(y.getComptabilisationMode());
            p.setLivraisonAuto(y.getLivraisonAuto());
            p.setJourAnterieur(y.getJourAnterieur());
            p.setPrintDocumentWhenValide(y.getPrintDocumentWhenValide());
            p.setPaieWithoutValide(y.getPaieWithoutValide());
            p.setNbFicheMax(y.getNbFicheMax());
            p.setGenererFactureAuto(y.getGenererFactureAuto());
            p.setDateSave(y.getDateSave());
            p.setJournal(UtilCompta.buildBeanJournaux(y.getJournal()));
            p.setModelFactureVente(y.getModelFactureVente());
            p.setSellLowerPr(y.getSellLowerPr());
            p.setLivreBcvWithoutPaye(y.getLivreBcvWithoutPaye());
            p.setGiveBonusInStatus(y.getGiveBonusInStatus());
            p.setUpdate(true);
        }
        return p;
    }

    public static ParametreCom buildBeanParametreStock(YvsComParametreStock y) {
        ParametreCom p = new ParametreCom();
        if (y != null) {
            p.setId(y.getId());
            p.setComptabilisationAuto(y.getComptabilisationAuto());
            p.setComptabilisationMode(y.getComptabilisationMode());
            p.setJourAnterieur(y.getJourAnterieur());
            p.setTailleCodeRation(y.getTailleCodeRation());
            p.setActiveRation(y.getActiveRation());
            p.setPrintDocumentWhenValide(y.getPrintDocumentWhenValide());
            p.setCalculPr(y.getCalculPr());
            p.setUpdate(true);
            p.setDateSave(y.getDateSave());
        }
        return p;
    }

    public static Parametre buildBeanParametre(YvsComParametre y) {
        Parametre p = new Parametre();
        if (y != null) {
            p.setId(y.getId());
            p.setReglementAuto((y.getReglementAuto() != null) ? y.getReglementAuto() : false);
            p.setDocumentMouvAchat(y.getDocumentMouvAchat());
            p.setDocumentMouvVente(y.getDocumentMouvVente());
            p.setModeInventaire(y.getModeInventaire());
            p.setDocumentGenererFromEcart(y.getDocumentGenererFromEcart());
            p.setSeuilClient(y.getSeuilClient());
            p.setSeuilFsseur(y.getSeuilFsseur());
            p.setDureeInactiv(y.getDureeInactiv());
            p.setConverter(y.getConverter());
            p.setConverterCs(y.getConverterCs());
            p.setTauxMargeSur(y.getTauxMargeSur());
            p.setDateSave(y.getDateSave());
            p.setUpdate(true);
            p.setJourUsine(y.getJourUsine());
            p.setUseLotReception(y.getUseLotReception());
        }
        return p;
    }

    public static Articles buildBeanArticle(YvsBaseArticles y) {
        Articles a = UtilProd.buildBeanArticles(y);
        return a;
    }

    public static List<Articles> buildBeanListArticle(List<YvsBaseArticles> list) {
        List<Articles> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseArticles a : list) {
                r.add(buildBeanArticle(a));
            }
        }
        return r;
    }

    public static TiersTemplate buildBeanTiersTemplate(YvsBaseTiersTemplate y) {
        TiersTemplate t = new TiersTemplate();
        if (y != null) {
            t.setId(y.getId());
            t.setAddNom(y.getAddNom());
            t.setAddPrenom(y.getAddPrenom());
            t.setAddSecteur(y.getAddSecteur());
            t.setAddSeparateur(y.getAddSeparateur());
            t.setApercu(y.getApercu());
            t.setSeparateur(y.getSeparateur());
            t.setLibelle(y.getLibelle());
            t.setTailleNom(y.getTailleNom());
            t.setTaillePrenom(y.getTaillePrenom());
            t.setTailleSecteur(y.getTailleSecteur());
            t.setType(y.getType());
            t.setDateSave(y.getDateSave());
            t.setCategorieComptable(y.getCategorieComptable() != null ? buildBeanCategorieComptable(y.getCategorieComptable()) : new CategorieComptable());
//            t.setCategorieTarifaire(y.getCategorieTarifaire() != null ? buildBeanPlanTarifaireClient(y.getCategorieTarifaire()) : new PlanTarifaireClient());
            t.setCompteCollectif(y.getCompteCollectif() != null ? buildBeanCompte(y.getCompteCollectif()) : new Comptes());
            t.setModel(y.getMdr() != null ? buildBeanModeReglement(y.getMdr()) : new ModeDeReglement());
            t.setCommission(y.getComission() != null ? buildBeanPlanCommission(y.getComission()) : new PlanCommission());
            t.setRistourne(y.getRistourne() != null ? buildBeanPlanRistourne(y.getRistourne()) : new PlanRistourne());
            t.setSecteur(y.getSecteur() != null ? buildBeanDictionnaire(y.getSecteur()) : new Dictionnaire());
            t.setVille(y.getVille() != null ? buildBeanDictionnaire(y.getVille()) : new Dictionnaire());
            t.setPays(y.getPays() != null ? buildBeanDictionnaire(y.getPays()) : new Dictionnaire());
            t.setAgence(y.getAgence() != null ? buildBeanAgence(y.getAgence()) : new Agence());
            t.setUpdate(true);
        }
        return t;
    }

    /*
     DEBUT GESTION PERSONNEL
     */
    public static CategoriePerso buildBeanCategoriePerso(YvsComCategoriePersonnel y) {
        CategoriePerso c = new CategoriePerso();
        if (y != null) {
            c.setId(y.getId());
            c.setDescription(y.getDescription());
            c.setCode(y.getCode());
            c.setLibelle(y.getLibelle());
            if (y.getParent() != null) {
                c.setParent(new CategoriePerso(y.getParent().getId(), y.getParent().getLibelle()));
            }
            c.setUpdate(true);
            c.setDateSave(y.getDateSave());
        }
        return c;
    }

    public static YvsComCategoriePersonnel buildCategoriePerso(CategoriePerso y, YvsUsersAgence ua, YvsSocietes ste) {
        YvsComCategoriePersonnel c = new YvsComCategoriePersonnel();
        if (y != null) {
            c.setId(y.getId());
            c.setDescription(y.getDescription());
            c.setCode(y.getCode());
            c.setLibelle(y.getLibelle());
            c.setSociete(ste);
            if ((y.getParent() != null) ? y.getParent().getId() > 0 : false) {
                c.setParent(new YvsComCategoriePersonnel(y.getParent().getId(), y.getParent().getLibelle()));
            }
            if (ua != null ? ua.getId() > 0 : false) {
                c.setAuthor(ua);
            }
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
        }
        return c;
    }

    /*
     FIN GESTION PERSONNEL
     */

    /*
     DEBUT CONVERTED THEME
     */
    public static Theme buildBeanThemeArticle(YvsBaseArticles y) {
        Theme t = new Theme();
        if (y != null) {
            t.setId(y.getId().intValue());
            t.setDisplayName(y.getDesignation());
            t.setName(y.getRefArt());
        }
        return t;
    }

    public static List<Theme> buildBeanListThemeArticle(List<YvsBaseArticles> l) {
        List<Theme> r = new ArrayList<>();
        if (l != null) {
            for (YvsBaseArticles a : l) {
                r.add(buildBeanThemeArticle(a));
            }
        }
        return r;
    }

    public static Theme buildBeanThemeEmploye(YvsUsers y) {
        Theme t = new Theme();
        if (y != null) {
            t.setId(y.getId().intValue());
            t.setDisplayName(y.getEmploye().getPrenom() + " " + y.getEmploye().getNom());
            t.setName(y.getEmploye().getMatricule());
        }
        return t;
    }

    public static List<Theme> buildBeanListThemeEmploye(List<YvsUsers> l) {
        List<Theme> r = new ArrayList<>();
        for (YvsUsers a : l) {
            r.add(buildBeanThemeEmploye(a));
        }
        return r;
    }

    public static Theme buildBeanThemeClient(YvsComClient y) {
        Theme t = new Theme();
        if (y != null) {
            t.setId(y.getId().intValue());
            t.setDisplayName(y.getTiers().getNom());
            t.setName(y.getTiers().getNom());
        }
        return t;
    }

    public static List<Theme> buildBeanListThemeClient(List<YvsComClient> l) {
        List<Theme> r = new ArrayList<>();
        for (YvsComClient a : l) {
            r.add(buildBeanThemeClient(a));
        }
        return r;
    }

    public static Theme buildBeanThemeFsseur(YvsBaseFournisseur y) {
        Theme t = new Theme();
        if (y != null) {
            t.setId(y.getId().intValue());
            t.setDisplayName(y.getTiers().getNom());
            t.setName(y.getTiers().getNom());
        }
        return t;
    }

    public static List<Theme> buildBeanListThemeFsseur(List<YvsBaseFournisseur> l) {
        List<Theme> r = new ArrayList<>();
        for (YvsBaseFournisseur a : l) {
            r.add(buildBeanThemeFsseur(a));
        }
        return r;
    }

    /*
     FIN CONVERTED THEME
     */

    /*
     *
     *DEBUT GESTION FOURNISSEUR
     *
     */
    public static YvsBaseCategorieFournisseur buildCategorieFournisseur(CategorieFournisseur y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        YvsBaseCategorieFournisseur c = new YvsBaseCategorieFournisseur();
        if (y != null) {
            c.setId(y.getId());
            c.setDescription(y.getDescription());
            c.setCode(y.getCode());
            c.setLibelle(y.getLibelle());
            c.setSociete(currentScte);
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
            if (y.getParent() != null ? y.getParent().getId() > 0 : false) {
                c.setParent(new YvsBaseCategorieFournisseur(y.getParent().getId(), y.getParent().getLibelle()));
            }
            c.setAuthor(currentUser);
        }
        return c;
    }

    public static CategorieFournisseur buildBeanCategorieFournisseur(YvsBaseCategorieFournisseur y) {
        CategorieFournisseur c = new CategorieFournisseur();
        if (y != null) {
            c.setId(y.getId());
            c.setDescription(y.getDescription());
            c.setCode(y.getCode());
            c.setLibelle(y.getLibelle());
            c.setDateSave(y.getDateSave());
            c.setParent(y.getParent() != null ? new CategorieFournisseur(y.getParent().getId(), y.getParent().getLibelle()) : new CategorieFournisseur());
        }
        return c;
    }

    public static OpeCptFsseur buildBeanOpeCptFsseur(YvsBaseOperationCompteFsseur y) {
        OpeCptFsseur o = new OpeCptFsseur();
        if (y != null) {
            o.setId(y.getId());
            o.setDateOperation((y.getDateOperation() != null) ? y.getDateOperation() : new Date());
            o.setHeureOperation((y.getHeureOperation() != null) ? y.getHeureOperation() : new Date());
            o.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            o.setUpdate(true);
        }
        return o;
    }

    public static List<OpeCptFsseur> buildBeanListOpeCptFsseur(List<YvsBaseOperationCompteFsseur> list) {
        List<OpeCptFsseur> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseOperationCompteFsseur a : list) {
                r.add(buildBeanOpeCptFsseur(a));
            }
        }
        return r;
    }

    public static Fournisseur buildSimpleBeanFournisseur(YvsBaseFournisseur y) {
        Fournisseur f = new Fournisseur();
        if (y != null) {
            f.setId(y.getId());
            f.setCodeFsseur(y.getCodeFsseur());
            f.setActif(y.getActif());
            f.setNom(y.getNom());
            f.setPrenom(y.getPrenom());
            f.setSeuilSolde(y.getSeuilSolde());
            f.setDateSave(y.getDateSave());
            f.setDateUpdate(y.getDateUpdate());
            f.setDefaut(y.getDefaut());
            f.setExclusForHome(y.getExclusForHome());
            f.setTiers((y.getTiers() != null) ? UtilTiers.buildBeanTiers(y.getTiers()) : new Tiers());
        }
        return f;
    }

    public static Fournisseur buildBeanFournisseur(YvsBaseFournisseur y) {
        Fournisseur f = new Fournisseur();
        if (y != null) {
            f = buildSimpleBeanFournisseur(y);
            y.setCategorieComptable(y.getCategorieComptable() != null ? y.getCategorieComptable() : y.getTiers() != null ? y.getTiers().getCategorieComptable() : null);
            f.setCategorieComptable((y.getCategorieComptable() != null) ? buildBeanCategorieComptable(y.getCategorieComptable()) : new CategorieComptable());
            f.setCategorie((y.getCategorie() != null) ? buildBeanCategorieFournisseur(y.getCategorie()) : new CategorieFournisseur());
            f.setModel(buildBeanModelReglement(y.getModel()));
            f.setCompteCollectif(buildBeanCompte(y.getCompte()));
            f.setAcompte(y.getAcompte());
        }
        return f;
    }

    public static YvsBaseFournisseur buildFournisseur(Fournisseur y, YvsUsersAgence currentUser) {
        YvsBaseFournisseur f = new YvsBaseFournisseur();
        if (y != null) {
            f.setId(y.getId());
            f.setCodeFsseur(y.getCodeFsseur());
            f.setActif(y.isActif());
            f.setNom(y.getNom());
            f.setPrenom(y.getPrenom());
            f.setDefaut(y.isDefaut());
            f.setExclusForHome(y.isExclusForHome());
            if (y.getTiers() != null ? y.getTiers().getId() > 0 : false) {
                f.setTiers(new YvsBaseTiers(y.getTiers().getId(), y.getTiers().getNom(), y.getTiers().getPrenom()));
            }
            if (y.getCategorie() != null ? y.getCategorie().getId() > 0 : false) {
                f.setCategorie(new YvsBaseCategorieFournisseur(y.getCategorie().getId(), y.getCategorie().getLibelle()));
            }
            if (y.getCategorieComptable() != null ? y.getCategorieComptable().getId() > 0 : false) {
                f.setCategorieComptable(new YvsBaseCategorieComptable(y.getCategorieComptable().getId(), y.getCategorie().getCode()));
            }
            if (y.getCompteCollectif() != null ? y.getCompteCollectif().getId() > 0 : false) {
                f.setCompte(new YvsBasePlanComptable(y.getCompteCollectif().getId(), y.getCompteCollectif().getNumCompte(), y.getCompteCollectif().getIntitule()));
            }
            if (y.getModel() != null ? y.getModel().getId() > 0 : false) {
                f.setModel(new YvsBaseModelReglement(y.getModel().getId(), y.getModel().getDesignation()));
            }
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                f.setAuthor(currentUser);
            }
            f.setDateSave(y.getDateSave());
            f.setDateUpdate(new Date());
            f.setSeuilSolde(y.getSeuilSolde());
        }
        return f;
    }

    public static List<Fournisseur> buildBeanListFournisseur(List<YvsBaseFournisseur> list) {
        List<Fournisseur> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseFournisseur a : list) {
                r.add(buildBeanFournisseur(a));
            }
        }
        return r;
    }

    public static ArticleFournisseur buildBeanArticleFournisseur(YvsBaseArticleFournisseur y) {
        ArticleFournisseur r = new ArticleFournisseur();
        if (y != null) {
            r.setId(y.getId());
            r.setDelaiLivraison((y.getDelaiLivraison() != null) ? y.getDelaiLivraison() : 0);
            r.setDureeGarantie((y.getDureeGarantie() != null) ? y.getDureeGarantie() : 0);
            r.setDureeVie((y.getDureeVie() != null) ? y.getDureeVie() : 0);
            r.setPrix((y.getPuv() != null) ? y.getPuv() : 0);
            r.setRemise((y.getRemise() != null) ? y.getRemise() : 0);
            r.setArticle((y.getArticle() != null) ? UtilProd.buildBeanArticles(y.getArticle()) : new Articles());
            r.setUniteDelaisLiv(y.getUniteDelai());
            r.setUniteDureeGaranti(y.getUniteGarantie());
            r.setUniteDureeVie(y.getUniteVie());
            r.setNatureRemise(y.getNatureRemise());
            r.setRefArtExterne(y.getRefArtExterne());
            r.setDesArtExterne(y.getDesArtExterne());
            r.setConditionnement(y.getConditionnement() != null ? UtilProd.buildBeanConditionnement(y.getConditionnement().getConditionnement()) : new Conditionnement());

            Calendar cal = Calendar.getInstance();
            switch (r.getUniteDelaisLiv()) {
                case Constantes.UNITE_HEURE:
                    cal.add(Calendar.HOUR_OF_DAY, (int) r.getDelaiLivraison());
                    break;
                case Constantes.UNITE_JOUR:
                    cal.add(Calendar.DATE, (int) r.getDelaiLivraison());
                    break;
                case Constantes.UNITE_SEMAINE:
                    cal.add(Calendar.WEEK_OF_YEAR, (int) r.getDelaiLivraison());
                    break;
                case Constantes.UNITE_MOIS:
                    cal.add(Calendar.MONTH, (int) r.getDelaiLivraison());
                    break;
                case Constantes.UNITE_ANNEE:
                    cal.add(Calendar.YEAR, (int) r.getDelaiLivraison());
                    break;
            }
            r.setDateLivraison(cal.getTime());
        }
        return r;
    }

    public static List<ArticleFournisseur> buildBeanListArticleFournisseur(List<YvsBaseArticleFournisseur> list) {
        List<ArticleFournisseur> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseArticleFournisseur c : list) {
                r.add(buildBeanArticleFournisseur(c));
            }
        }
        return r;
    }

    public static TrancheReglementFournisseur buildBeanTrancheReglementFournisseur(YvsBaseTrancheReglementFournisseur y) {
        TrancheReglementFournisseur t = new TrancheReglementFournisseur();
        if (y != null) {
            t.setId(y.getId());
            t.setIntervalJour(y.getIntervalJour());
            t.setTaux((y.getTaux() != null) ? y.getTaux() : 0);
            t.setNumero(y.getNumero());
            t.setUpdate(true);
        }
        return t;
    }

    public static List<TrancheReglementFournisseur> buildBeanListTrancheReglementFournisseur(List<YvsBaseTrancheReglementFournisseur> list) {
        List<TrancheReglementFournisseur> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseTrancheReglementFournisseur c : list) {
                r.add(buildBeanTrancheReglementFournisseur(c));
            }
        }
        return r;
    }

    public static PlanReglementFournisseur buildBeanPlanReglementFournisseur(YvsBasePlanReglementFournisseur y) {
        PlanReglementFournisseur p = new PlanReglementFournisseur();
        if (y != null) {
            p.setActif((y.getActif() != null) ? y.getActif() : false);
            p.setId(y.getId());
            p.setModel((y.getModel() != null) ? buildBeanModeReglement(y.getModel()) : new ModeDeReglement());
            p.setMontantMaximal((y.getMontantMaximal() != null) ? y.getMontantMaximal() : 0);
            p.setMontantMinimal((y.getMontantMinimal() != null) ? y.getMontantMinimal() : 0);
            p.setTranches((y.getTranches() != null)
                    ? buildBeanListTrancheReglementFournisseur(y.getTranches())
                    : new ArrayList<TrancheReglementFournisseur>());
            Collections.sort(p.getTranches());
        }
        return p;
    }

    public static List<PlanReglementFournisseur> buildBeanListPlanReglementFournisseur(List<YvsBasePlanReglementFournisseur> list) {
        List<PlanReglementFournisseur> r = new ArrayList<>();
        if (list != null) {
            for (YvsBasePlanReglementFournisseur c : list) {
                r.add(buildBeanPlanReglementFournisseur(c));
            }
        }
        return r;
    }
    /*
     *
     *FIN GESTION FOURNISSEUR
     *
     */

    /*
     *
     *DEBUT GESTION CLIENT
     *
     */
    public static GrilleRabais buildBeanGrilleTarifaireClient(YvsBasePlanTarifaireTranche y) {
        GrilleRabais r = new GrilleRabais();
        if (y != null) {
            r.setId(y.getId());
            r.setMontantMaximal(y.getValeurMax() >= Double.MAX_VALUE ? 0 : y.getValeurMax());
            r.setMontantMinimal((y.getValeurMin() != null) ? y.getValeurMin() : 0);
            r.setMontantRabais((y.getRemise() != null) ? y.getRemise() : 0);
            r.setNatureMontant((y.getNatureRemise() != null) ? y.getNatureRemise() : Constantes.NATURE_MTANT);
            r.setBase((y.getBase() != null) ? y.getBase() : Constantes.BASE_QTE);
            r.setValeur(y.getPuv());
            r.setParent(y.getPlan().getId());
            r.setDateSave(y.getDateSave());
            r.setUpdate(true);
        }
        return r;
    }

    public static YvsBasePlanTarifaireTranche buildGrilleTarifaireClient(GrilleRabais y, YvsUsersAgence ua) {
        YvsBasePlanTarifaireTranche r = new YvsBasePlanTarifaireTranche();
        if (y != null) {
            r.setId(y.getId());
            r.setValeurMax(y.getMontantMaximal());
            r.setValeurMin(y.getMontantMinimal());
            r.setRemise(y.getMontantRabais());
            r.setNatureRemise((y.getNatureMontant() != null) ? y.getNatureMontant() : Constantes.NATURE_MTANT);
            r.setBase((y.getBase() != null) ? y.getBase() : Constantes.BASE_QTE);
            r.setPuv(y.getValeur());
            if (y.getParent() > 0) {
                r.setPlan(new YvsBasePlanTarifaire(y.getParent()));
            }
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static PlanTarifaireClient buildBeanPlanTarifaireClient(YvsBasePlanTarifaire y) {
        PlanTarifaireClient c = new PlanTarifaireClient();
        if (y != null) {
            c.setId(y.getId());
            c.setArticle((y.getArticle() != null) ? buildBeanArticle(y.getArticle()) : new Articles());
            c.setFamille(y.getFamille() != null ? UtilProd.buildBeanFamilleArticle(y.getFamille()) : new FamilleArticle());
            c.setCategorie(y.getCategorie() != null ? buildBeanCategorieClient(y.getCategorie()) : new CategorieClient());
            c.setConditionnement(y.getConditionnement() != null ? UtilProd.buildBeanConditionnement(y.getConditionnement()) : new Conditionnement());
            c.setPuv((y.getPuv() != null) ? y.getPuv() : 0);
            c.setPuvMin((y.getPuvMin() != null) ? y.getPuvMin() : 0);
            c.setRemise((y.getRemise() != null) ? y.getRemise() : 0);
            c.setRistourne((y.getRistourne() != null) ? y.getRistourne() : 0);
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setCoefAugmentation(y.getCoefAugmentation());
            c.setNatureCoefAugmentation(y.getNatureCoefAugmentation());
            c.setNatureRemise(y.getNatureRemise());
            c.setNatureRistourne(y.getNatureRistourne());
            c.setDateSave(y.getDateSave());
            c.setForArticle(y.getArticle() != null);
            c.setUpdate(true);
        }
        return c;
    }

    public static YvsBasePlanTarifaire buildPlanTarifaireClient(PlanTarifaireClient y, YvsUsersAgence ua) {
        YvsBasePlanTarifaire c = new YvsBasePlanTarifaire();
        if (y != null) {
            c.setId(y.getId());
            if (y.getTemplate() != null ? y.getTemplate().getId() > 0 : false) {
                c.setTemplate(new YvsBaseArticlesTemplate(y.getTemplate().getId()));
            }
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                c.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            if (y.getFamille() != null ? y.getFamille().getId() > 0 : false) {
                c.setFamille(new YvsBaseFamilleArticle(y.getFamille().getId(), y.getFamille().getReference(), y.getFamille().getDesignation()));
            }
            if (y.getCategorie() != null ? y.getCategorie().getId() > 0 : false) {
                c.setCategorie(new YvsBaseCategorieClient(y.getCategorie().getId(), y.getCategorie().getCode(), y.getCategorie().getLibelle()));
            }
            if (y.getConditionnement() != null ? y.getConditionnement().getId() > 0 : false) {
                c.setConditionnement(UtilProd.buildConditionnement(y.getConditionnement(), ua));
            }
            c.setPuv(y.getPuv());
            c.setPuvMin(y.getPuvMin());
            c.setRemise(y.getRemise());
            c.setRistourne(y.getRistourne());
            c.setActif(y.isActif());
            c.setCoefAugmentation(y.getCoefAugmentation());
            c.setNatureCoefAugmentation(y.getNatureCoefAugmentation());
            c.setNatureRemise(y.getNatureRemise());
            c.setNatureRistourne(y.getNatureRistourne());
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
            c.setAuthor(ua);
            c.setNew_(true);
        }
        return c;
    }

    public static CategorieClient buildBeanCategorieClient(YvsBaseCategorieClient y) {
        CategorieClient c = new CategorieClient();
        if (y != null) {
            c.setId(y.getId());
            c.setDescription(y.getDescription());
            c.setCode(y.getCode());
            c.setLibelle(y.getLibelle());
            c.setLierClient((y.getLierClient() != null) ? y.getLierClient() : false);
            c.setDefaut((y.getDefaut() != null) ? y.getDefaut() : false);
            c.setModel(buildBeanModelReglement(y.getModel()));
            c.setUpdate(true);
            c.setVenteOnline(y.getVenteOnline());
            c.setDateSave(y.getDateSave());
            c.setParent(y.getParent() != null ? new CategorieClient(y.getParent().getId(), y.getParent().getLibelle()) : new CategorieClient());
        }
        return c;
    }

    public static YvsBaseCategorieClient buildCategorieClient(CategorieClient y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        YvsBaseCategorieClient c = new YvsBaseCategorieClient();
        if (y != null) {
            c.setId(y.getId());
            c.setDescription(y.getDescription());
            c.setCode(y.getCode());
            c.setLibelle(y.getLibelle());
            c.setLierClient(y.isLierClient());
            c.setDefaut(y.isDefaut());
            c.setSociete(currentScte);
            c.setActif(y.isActif());
            c.setVenteOnline(y.isVenteOnline());
            c.setNew_(true);
            if (y.getModel() != null ? y.getModel().getId() > 0 : false) {
                c.setModel(new YvsBaseModelReglement(y.getModel().getId(), y.getModel().getDesignation()));
            }
            if ((y.getParent() != null) ? y.getParent().getId() > 0 : false) {
                c.setParent(new YvsBaseCategorieClient(y.getParent().getId(), y.getParent().getCode(), y.getParent().getLibelle()));
            }
            c.setAuthor(currentUser);
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
        }
        return c;
    }

    public static CategorieTarifaire buildBeanCategorieTarifaire(YvsComCategorieTarifaire y) {
        CategorieTarifaire c = new CategorieTarifaire();
        if (y != null) {
            c.setId(y.getId());
            c.setPriorite(y.getPriorite());
            c.setDateDebut(y.getDateDebut());
            c.setDateFin(y.getDateFin());
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setPermanent((y.getPermanent() != null) ? y.getPermanent() : false);
            c.setClient(y.getClient() != null ? buildBeanClient(y.getClient()) : new Client());
            c.setCategorie(y.getCategorie() != null ? buildBeanCategorieClient(y.getCategorie()) : new CategorieClient());
            c.setUpdate(true);
        }
        return c;
    }

    public static YvsComCategorieTarifaire buildTarifaire(CategorieTarifaire y, YvsUsersAgence currentUser) {
        YvsComCategorieTarifaire c = new YvsComCategorieTarifaire();
        if (y != null) {
            c.setId(y.getId());
            c.setPriorite(y.getPriorite());
            c.setDateDebut(y.getDateDebut());
            c.setDateFin(y.getDateFin());
            c.setActif(y.isActif());
            c.setPermanent(y.isPermanent());
            if (y.getClient() != null ? y.getClient().getId() > 0 : false) {
                c.setClient(new YvsComClient(y.getClient().getId(), y.getClient().getNom(), y.getClient().getPrenom()));
            }
            if (y.getCategorie() != null ? y.getCategorie().getId() > 0 : false) {
                c.setCategorie(new YvsBaseCategorieClient(y.getCategorie().getId()));
            }
            c.setAuthor(currentUser);
            c.setNew_(true);
        }
        return c;
    }

    public static Client buildInfoBaseClient(YvsComClient y) {
        Client cl = new Client();
        if (y != null) {
            cl.setId(y.getId());
            cl.setDefaut((y.getDefaut() != null) ? y.getDefaut() : false);
            cl.setSuiviComptable(y.getSuiviComptable());
            cl.setCodeClient(y.getCodeClient());
            cl.setActif(y.getActif());
            cl.setNom(y.getNom());
            cl.setPrenom(y.getPrenom());
            cl.setSolde(y.getSolde());
            cl.setDebit(y.getDebit());
            cl.setConfirmer(y.getConfirmer());
            cl.setCredit(y.getCredit());
            cl.setCreance(y.getCreance());
            cl.setAcompte(y.getAcompte());
            cl.setDateCreation(y.getDateCreation());
            cl.setDateUpdate(y.getDateUpdate());
            cl.setAcompte(y.getAcompte());
            cl.setNbrFactureImpayee(y.getNbrFactureImpayee());
            cl.setExclusForHome(y.getExclusForHome());
        }
        return cl;
    }

    public static Client buildSimpleBeanClient(YvsComClient y) {
        Client cl = new Client();
        if (y != null) {
            cl.setId(y.getId());
            cl.setDefaut((y.getDefaut() != null) ? y.getDefaut() : false);
            cl.setSuiviComptable(y.getSuiviComptable());
            cl.setCodeClient(y.getCodeClient());
            cl.setActif(y.getActif());
            cl.setNom(y.getNom());
            cl.setPrenom(y.getPrenom());
            cl.setSolde(y.getSolde());
            cl.setDebit(y.getDebit());
            cl.setConfirmer(y.getConfirmer());
            cl.setCredit(y.getCredit());
            cl.setCreance(y.getCreance());
            cl.setAcompte(y.getAcompte());
            cl.setDateCreation(y.getDateCreation());
            cl.setDateUpdate(y.getDateUpdate());
            cl.setAcompte(y.getAcompte());
            cl.setVenteOnline(y.getVenteOnline());
            cl.setExclusForHome(y.getExclusForHome());
            cl.setNbrFactureImpayee(y.getNbrFactureImpayee());
            y.setCategorieComptable(y.getCategorieComptable() != null ? y.getCategorieComptable() : (y.getTiers() != null) ? y.getTiers().getCategorieComptable() : null);
            cl.setCategorieComptable((y.getCategorieComptable() != null) ? buildBeanCategorieComptable(y.getCategorieComptable()) : new CategorieComptable());
            cl.setTiers((y.getTiers() != null) ? UtilTiers.buildSimpleBeanTiers(y.getTiers()) : new Tiers());
            cl.setModel(buildBeanModelReglement(y.getModel()));
            cl.setRistourne(buildBeanPlanRistourne(y.getPlanRistourne()));
        }
        return cl;
    }

    public static Client buildBeanClient(YvsComClient y) {
        Client f = buildSimpleBeanClient(y);
        if (y != null) {
            f.setCategorie(y.getCategorie());
            f.setCompteCollectif(y.getCompte() != null ? buildBeanCompte(y.getCompte()) : new Comptes());
            f.setRepresentant(UtilUsers.buildSimpleBeanUsers(y.getRepresentant()));
            f.setCreateBy(UtilUsers.buildSimpleBeanUsers(y.getCreateBy()));
            f.setContrats(y.getContrats());
            f.setLigne(UtilCom.buildBeanPointLivraison(y.getLigne()));
        }
        return f;
    }

    public static ContratsClient buildBeanContratsClient(YvsComContratsClient y) {
        ContratsClient r = new ContratsClient();
        if (y != null) {
            r.setId(y.getId());
            r.setActif(y.getActif());
            r.setClient(new Client(y.getClient() != null ? y.getClient().getId() : 0));
            r.setDateDebutFacturation(y.getDateDebutFacturation());
            r.setDateExpiration(y.getDateExpiration());
            r.setDateSave(y.getDateSave());
            r.setIntervalFacturation(y.getIntervalFacturation());
            r.setPeriodeFacturation(y.getPeriodeFacturation());
            r.setReference(y.getReference());
            r.setReferenceExterne(y.getReferenceExterne());
            r.setType(y.getType());
            r.setElements(y.getElements());
            r.setAdditionnels(y.getAdditionnels());
        }
        return r;
    }

    public static YvsComContratsClient buildContratsClient(ContratsClient y, YvsUsersAgence ua) {
        YvsComContratsClient r = new YvsComContratsClient();
        if (y != null) {
            r.setId(y.getId());
            r.setActif(y.isActif());
            if (y.getClient() != null ? y.getClient().getId() > 0 : false) {
                r.setClient(new YvsComClient(y.getClient().getId()));
            }
            r.setDateDebutFacturation(y.getDateDebutFacturation());
            r.setDateExpiration(y.getDateExpiration());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setIntervalFacturation(y.getIntervalFacturation());
            r.setPeriodeFacturation(y.getPeriodeFacturation());
            r.setReference(y.getReference());
            r.setReferenceExterne(y.getReferenceExterne());
            r.setType(y.getType());
            r.setAuthor(ua);
        }
        return r;
    }

    public static ElementContratsClient buildBeanElementContratsClient(YvsComElementContratsClient y) {
        ElementContratsClient r = new ElementContratsClient();
        if (y != null) {
            r.setId(y.getId());
            r.setQuantite(y.getQuantite());
            r.setPrix(y.getPrix());
            r.setContrat(new ContratsClient(y.getContrat() != null ? y.getContrat().getId() : 0));
            r.setArticle(UtilProd.buildBeanConditionnement(y.getArticle()));
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static YvsComElementContratsClient buildElementContratsClient(ElementContratsClient y, YvsUsersAgence ua) {
        YvsComElementContratsClient r = new YvsComElementContratsClient();
        if (y != null) {
            r.setId(y.getId());
            r.setQuantite(y.getQuantite());
            r.setPrix(y.getPrix());
            if (y.getContrat() != null ? y.getContrat().getId() > 0 : false) {
                r.setContrat(new YvsComContratsClient(y.getContrat() != null ? y.getContrat().getId() : 0));
            }
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(UtilProd.buildConditionnement(y.getArticle(), ua));
            }
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
        }
        return r;
    }

    public static ElementAddContratsClient buildBeanElementAddContratsClient(YvsComElementAddContratsClient y) {
        ElementAddContratsClient r = new ElementAddContratsClient();
        if (y != null) {
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setApplication(y.getApplication());
            r.setPeriodiciteApplication(y.getPeriodiciteApplication());
            r.setContrat(new ContratsClient(y.getContrat() != null ? y.getContrat().getId() : 0));
            r.setTypeCout(UtilGrh.buildBeanTypeCout(y.getTypeCout()));
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static YvsComElementAddContratsClient buildElementAddContratsClient(ElementAddContratsClient y, YvsUsersAgence ua) {
        YvsComElementAddContratsClient r = new YvsComElementAddContratsClient();
        if (y != null) {
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setApplication(y.getApplication());
            r.setPeriodiciteApplication(y.getPeriodiciteApplication());
            if (y.getContrat() != null ? y.getContrat().getId() > 0 : false) {
                r.setContrat(new YvsComContratsClient(y.getContrat() != null ? y.getContrat().getId() : 0));
            }
            if (y.getTypeCout() != null ? y.getTypeCout().getId() > 0 : false) {
                r.setTypeCout(new YvsGrhTypeCout(y.getTypeCout().getId(), y.getTypeCout().getLibelle()));
            }
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
        }
        return r;
    }

    public static List<Client> buildBeanListClient(List<YvsComClient> list) {
        List<Client> r = new ArrayList<>();
        if (list != null) {
            for (YvsComClient a : list) {
                r.add(buildBeanClient(a));
            }
        }
        return r;
    }

    public static TrancheReglementClient buildBeanTrancheReglementClient(YvsComTrancheReglementClient y) {
        TrancheReglementClient t = new TrancheReglementClient();
        if (y != null) {
            t.setId(y.getId());
            t.setIntervalJour(y.getIntervalJour());
            t.setTaux((y.getTaux() != null) ? y.getTaux() : 0);
            t.setNumero(y.getNumero());
            t.setUpdate(true);
        }
        return t;
    }

    public static List<TrancheReglementClient> buildBeanListTrancheReglementClient(List<YvsComTrancheReglementClient> list) {
        List<TrancheReglementClient> r = new ArrayList<>();
        if (list != null) {
            for (YvsComTrancheReglementClient c : list) {
                r.add(buildBeanTrancheReglementClient(c));
            }
        }
        return r;
    }

    public static PlanReglementClient buildBeanPlanReglementClient(YvsComPlanReglementClient y) {
        PlanReglementClient p = new PlanReglementClient();
        if (y != null) {
            p.setActif((y.getActif() != null) ? y.getActif() : false);
            p.setId(y.getId());
            p.setModel((y.getModel() != null) ? buildBeanModeReglement(y.getModel()) : new ModeDeReglement());
            p.setMontantMaximal((y.getMontantMaximal() != null) ? y.getMontantMaximal() : 0);
            p.setMontantMinimal((y.getMontantMinimal() != null) ? y.getMontantMinimal() : 0);
        }
        return p;
    }

    public static PlanReglementClient buildBeanPlanReglementCategorie(YvsComPlanReglementCategorie y) {
        PlanReglementClient p = new PlanReglementClient();
        if (y != null) {
            p.setActif((y.getActif() != null) ? y.getActif() : false);
            p.setId(y.getId());
            p.setModel((y.getModel() != null) ? buildBeanModeReglement(y.getModel()) : new ModeDeReglement());
            p.setMontantMaximal((y.getMontantMaximal() != null) ? y.getMontantMaximal() : 0);
            p.setMontantMinimal((y.getMontantMinimal() != null) ? y.getMontantMinimal() : 0);
        }
        return p;
    }

    public static List<PlanReglementClient> buildBeanListPlanReglementClient(List<YvsComPlanReglementClient> list) {
        List<PlanReglementClient> r = new ArrayList<>();
        if (list != null) {
            for (YvsComPlanReglementClient c : list) {
                r.add(buildBeanPlanReglementClient(c));
            }
        }
        return r;
    }

    public static ModeReglementBanque buildBeanModeReglementBanque(YvsBaseModeReglementBanque y) {
        ModeReglementBanque c = new ModeReglementBanque();
        if (y != null) {
            c.setId(y.getId());
            c.setCodeBanque(y.getCodeBanque());
            c.setCodeGuichet(y.getCodeGuichet());
            c.setNumero(y.getNumero());
            c.setCle(y.getCle());
            if (c.getMode() != null) {
                c.setMode(new ModeDeReglement(c.getMode().getId()));
            }
            c.setDateSave(y.getDateSave());
        }
        return c;
    }

    public static YvsBaseModeReglementBanque buildModeReglementBanque(ModeReglementBanque y, YvsUsersAgence ua) {
        YvsBaseModeReglementBanque c = new YvsBaseModeReglementBanque();
        if (y != null) {
            c.setId(y.getId());
            c.setCodeBanque(y.getCodeBanque());
            c.setCodeGuichet(y.getCodeGuichet());
            c.setNumero(y.getNumero());
            c.setCle(y.getCle());
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                c.setMode(new YvsBaseModeReglement((long) y.getMode().getId()));
            }
            c.setDateSave(y.getDateSave());
            if (ua != null ? ua.getId() > 0 : false) {
                c.setAuthor(ua);
            }
            c.setDateUpdate(new Date());
        }
        return c;
    }

    public static InfosModeReglement buildBeanInfosModeReglement(YvsBaseModeReglementInformations y) {
        InfosModeReglement c = new InfosModeReglement();
        if (y != null) {
            c.setId(y.getId());
            c.setAuthorisationHeader(y.getAuthorisationHeader());
            c.setMerchantKey(y.getMerchantKey());
            c.setChannelUserMsisdn(y.getChannelUserMsisdn());
            c.setChannelUserPin(y.getChannelUserPin());
            c.setCurrency(y.getCurrency());
            if (c.getMode() != null) {
                c.setMode(new ModeDeReglement(c.getMode().getId()));
            }
            c.setDateSave(y.getDateSave());
        }
        return c;
    }

    public static YvsBaseModeReglementInformations buildInfosModeReglement(InfosModeReglement y, YvsUsersAgence ua) {
        YvsBaseModeReglementInformations c = new YvsBaseModeReglementInformations();
        if (y != null) {
            c.setId(y.getId());
            c.setAuthorisationHeader(y.getAuthorisationHeader());
            c.setMerchantKey(y.getMerchantKey());
            c.setChannelUserMsisdn(y.getChannelUserMsisdn());
            c.setChannelUserPin(y.getChannelUserPin());
            c.setCurrency(y.getCurrency());
            if (y.getMode() != null ? y.getMode().getId() > 0 : false) {
                c.setMode(new YvsBaseModeReglement((long) y.getMode().getId()));
            }
            c.setDateSave(y.getDateSave());
            if (ua != null ? ua.getId() > 0 : false) {
                c.setAuthor(ua);
            }
            c.setDateUpdate(new Date());
        }
        return c;
    }

    public static YvsBaseModeReglement buildModeReglement(ModeDeReglement y, YvsUsersAgence ua, YvsSocietes currentScte) {
        YvsBaseModeReglement c = new YvsBaseModeReglement();
        if (y != null) {
            c.setId((long) y.getId());
            c.setDesignation(y.getDesignation());
            c.setDescription(y.getDescription());
            c.setTypeReglement(y.getTypeReglement());
            c.setCodePaiement(y.getCodePaiement());
            c.setNumeroMarchand(y.getNumeroMarchand());
            c.setDefaultMode(y.isDefaut());
            c.setActif(y.isActif());
            c.setVisibleOnPrintVente(y.isVisibleOnPrintVente());
            c.setDateSave(y.getDateSave());
            c.setSociete(currentScte);
            c.setNew_(true);
            if (ua != null ? ua.getId() > 0 : false) {
                c.setAuthor(ua);
            }
            c.setDateUpdate(new Date());
        }
        return c;
    }

    public static ModeDeReglement buildBeanModeReglement(YvsBaseModeReglement y) {
        ModeDeReglement c = new ModeDeReglement();
        if (y != null) {
            c.setId(y.getId().intValue());
            c.setDescription(y.getDescription());
            c.setDescription(y.getDescription());
            c.setDesignation(y.getDesignation());
            c.setDefaut(y.getDefaultMode());
            c.setTypeReglement(y.getTypeReglement());
            c.setCodePaiement(y.getCodePaiement());
            c.setNumeroMarchand(y.getNumeroMarchand());
            c.setDateSave(y.getDateSave());
            c.setActif(y.getActif());
            c.setVisibleOnPrintVente(y.getVisibleOnPrintVente());
            c.setInfos(buildBeanInfosModeReglement(y.getInfos()));
            c.setBanque(buildBeanModeReglementBanque(y.getBanque()));
            c.setUpdate(true);
        }
        return c;
    }

    public static YvsBaseModelReglement buildModelReglement(ModelReglement y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        YvsBaseModelReglement c = new YvsBaseModelReglement();
        if (y != null) {
            c.setId(y.getId());
            c.setReference(y.getDesignation());
            c.setDescription(y.getDescription());
            c.setActif(y.isActif());
            c.setPayeBeforeValide(y.isPayeBeforeValide());
            c.setTranches(y.getTranches());
            c.setSociete(currentScte);
            c.setType(String.valueOf(y.getType()) != null ? (String.valueOf(y.getType()).trim().length() > 0 ? y.getType() : 'M') : 'M');
            c.setNew_(true);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                c.setAuthor(currentUser);
            }
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
        }
        return c;
    }

    public static ModelReglement buildBeanModelReglement(YvsBaseModelReglement y) {
        ModelReglement c = new ModelReglement();
        if (y != null) {
            c.setId(y.getId().intValue());
            c.setDesignation(y.getReference());
            c.setDescription(y.getDescription());
            c.setActif(y.getActif());
            c.setPayeBeforeValide(y.getPayeBeforeValide());
            c.setTranches(y.getTranches());
            c.setType(y.getType());
            c.setUpdate(true);
            c.setDateSave(y.getDateSave());
            c.setCodeAcces(y.getCodeAcces() != null ? y.getCodeAcces().getCode() : "");
        }
        return c;
    }

    public static TrancheReglement buildBeanTrancheReglement(YvsBaseTrancheReglement y) {
        TrancheReglement t = new TrancheReglement();
        if (y != null) {
            t.setId(y.getId());
            t.setIntervalJour(y.getIntervalJour());
            t.setTaux((y.getTaux() != null) ? y.getTaux() : 0);
            t.setNumero(y.getNumero());
            t.setMode(buildBeanModeReglement(y.getMode()));
            t.setModel(buildBeanModelReglement(y.getModel()));
            t.setUpdate(true);
            t.setDateUpdate(y.getDateUpdate());
        }
        return t;
    }

    public static YvsBaseTrancheReglement buildTrancheReglement(TrancheReglement y, YvsUsersAgence currentUser) {
        YvsBaseTrancheReglement t = new YvsBaseTrancheReglement();
        if (y != null) {
            t.setId(y.getId());
            t.setIntervalJour(y.getIntervalJour());
            t.setTaux(y.getTaux());
            t.setNumero(y.getNumero());
            if ((y.getMode() != null) ? y.getMode().getId() > 0 : false) {
                t.setMode(new YvsBaseModeReglement((long) y.getMode().getId(), y.getMode().getDesignation()));
            }
            if ((y.getModel() != null) ? y.getModel().getId() > 0 : false) {
                t.setModel(new YvsBaseModelReglement(y.getModel().getId(), y.getModel().getDesignation()));
            }
            t.setNew_(true);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                t.setAuthor(currentUser);
            }
            t.setDateSave(new Date());
            t.setDateUpdate(y.getDateUpdate());
        }
        return t;
    }
    /*
     *
     *FIN GESTION CLIENT
     *
     */

    /*
     *
     *DEBUT GESTION Depots
     *
     */
    public static ArticleDepot buildBeanArticleDepot(YvsBaseArticleDepot y) {
        ArticleDepot a = new ArticleDepot();
        if (y != null) {
            a.setId(y.getId());
            a.setActif((y.getActif() != null) ? y.getActif() : false);
            a.setArticle((y.getArticle() != null) ? UtilProd.buildSimpleBeanArticles(y.getArticle()) : new Articles());
            a.setDesignation(a.getArticle().getDesignation());
            a.setRefArt(a.getArticle().getRefArt());
            a.setModeAppro(y.getModeAppro());
            a.setModeReappro(y.getModeReappro());
            a.setIntervalApprov((y.getIntervalApprov() != null) ? y.getIntervalApprov() : 1);
            a.setLotLivraison((y.getLotLivraison() != null) ? y.getLotLivraison() : 0);
            a.setDateAppro((y.getDateAppro() != null) ? y.getDateAppro() : new Date());
            a.setStockInitial((y.getStockInitial() != null) ? y.getStockInitial() : 0);
            a.setQuantiteStock((y.getQuantiteStock() != null) ? y.getQuantiteStock() : 0);
            a.setStockMax((y.getStockMax() != null) ? y.getStockMax() : 0);
            a.setStockMin((y.getStockMin() != null) ? y.getStockMin() : 0);
            a.setStockAlert((y.getStockAlert() != null) ? y.getStockAlert() : 0);
            a.setStockSecurite(y.getStockAlert() - a.getStockMin());
            a.setUniteInterval(y.getUniteInterval());
            a.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            a.setRequiereLot(y.getRequiereLot());
            a.setDepot((y.getDepot() != null) ? buildSimpleBeanDepot(y.getDepot()) : new Depots());
            a.setDepotPr((y.getDepotPr() != null) ? buildSimpleBeanDepot(y.getDepotPr()) : new Depots());
            a.setUpdate(true);
            a.setArticles(y.getArticles());
            a.setConditionnements_(y.getConditionnements());
            a.setGenererDocument(y.getGenererDocument());
            a.setTypeDocumentGenerer(y.getTypeDocumentGenerer());
            a.setDateSave(y.getDateSave());
            a.setDateUpdate(y.getDateUpdate());
            a.setAuthor(y.getAuthor());
            a.setSuiviStock(y.getSuiviStock());
            a.setSellWithoutStock(y.getSellWithoutStock());
            a.setConditionnement(UtilProd.buildBeanConditionnement(y.getDefaultCond()));
            a.setDefaultPr(y.getDefaultPr());
            a.setCategorie(y.getCategorie());
        }
        return a;
    }

    public static YvsBaseArticleDepot buildArticleDepot(ArticleDepot y, YvsUsersAgence currentUser) {
        return buildArticleDepot(y, currentUser, buildDepot(y.getDepot(), currentUser, currentUser.getAgence()));
    }

    public static YvsBaseArticleDepot buildArticleDepot(ArticleDepot y, YvsUsersAgence currentUser, YvsBaseDepots depot) {
        YvsBaseArticleDepot a = new YvsBaseArticleDepot();
        if (y != null) {
            a.setId(y.getId());
            a.setActif(y.getArticle().getId() > 0 ? y.getArticle().isActif() : y.isActif());
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                a.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation(), y.getArticle().isPuvTtc(), y.getArticle().isActif()));
            }
            if ((y.getDepotPr() != null) ? y.getDepotPr().getId() > 0 : false) {
                a.setDepotPr(new YvsBaseDepots(y.getDepotPr().getId(), y.getDepotPr().getDesignation()));
            }
            a.setModeAppro(y.getModeAppro());
            a.setModeReappro(y.getModeReappro());
            a.setIntervalApprov(y.getIntervalApprov());
            a.setQuantiteStock(y.getQuantiteStock());
            a.setStockMax(y.getStockMax());
            a.setStockMin(y.getStockMin());
            a.setStockAlert(y.getStockAlert());
            a.setStockInitial(y.getStockInitial());
            a.setLotLivraison(y.getLotLivraison());
            a.setDateAppro((y.getDateAppro() != null) ? y.getDateAppro() : new Date());
            a.setUniteInterval(y.getUniteInterval());
            if ((depot != null) ? depot.getId() > 0 : false) {
                a.setDepot(depot);
            }
            a.setRequiereLot(y.isRequiereLot());
            a.setGenererDocument(y.isGenererDocument());
            a.setTypeDocumentGenerer(y.getTypeDocumentGenerer());
            a.setSupp(false);
            a.setAuthor(currentUser);
            a.setDateSave(y.getDateSave());
            a.setDateUpdate(new Date());
            a.setSuiviStock(y.isSuiviStock());
            a.setSellWithoutStock(y.isSellWithoutStock());
            a.setCategorie(y.getCategorie());
            a.setDefaultPr(y.isDefaultPr());
            if (y.getConditionnement().getId() > 0) {
                a.setDefaultCond(new YvsBaseConditionnement(y.getConditionnement().getId()));
            } else {
                a.setDefaultCond(null);
            }
        }
        return a;
    }

    public static List<ArticleDepot> buildBeanListArticleDepot(List<YvsBaseArticleDepot> list) {
        List<ArticleDepot> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseArticleDepot a : list) {
                r.add(buildBeanArticleDepot(a));
            }
        }
        return r;
    }

    public static LiaisonDepot buildBeanLiaisonDepot(YvsComLiaisonDepot y) {
        LiaisonDepot l = new LiaisonDepot();
        if (y != null) {
            l.setId(y.getId());
            l.setDepot((y.getDepotLier() != null) ? buildBeanDepot(y.getDepotLier()) : new Depots());
            l.setDateSave(y.getDateSave());
        }
        return l;
    }

    public static List<LiaisonDepot> buildBeanListLiaisonDepot(List<YvsComLiaisonDepot> list) {
        List<LiaisonDepot> r = new ArrayList<>();
        if (list != null) {
            for (YvsComLiaisonDepot a : list) {
                r.add(buildBeanLiaisonDepot(a));
            }
        }
        return r;
    }

    public static DepotOperation buildBeanDepotOperation(YvsBaseDepotOperation y) {
        DepotOperation d = new DepotOperation();
        if (y != null) {
            d.setId(y.getId());
            d.setOperation(y.getOperation());
            d.setType(y.getType());
            d.setUpdate(true);
            d.setDateSave(y.getDateSave());
        }
        return d;
    }

    public static ArticleEmplacement buildBeanArticleEmplacement(YvsBaseArticleEmplacement y) {
        ArticleEmplacement a = new ArticleEmplacement();
        if (y != null) {
            a.setId(y.getId());
            a.setQuantite((y.getQuantite() != null) ? a.getQuantite() : 0);
            a.setArticle((y.getArticle() != null) ? new ArticleDepot(y.getArticle().getId()) : new ArticleDepot());
            a.setEmplacement((y.getEmplacement() != null) ? new Emplacement(y.getEmplacement().getId()) : new Emplacement());
        }
        return a;
    }

    public static List<ArticleEmplacement> buildBeanListArticleEmplacement(List<YvsBaseArticleEmplacement> list) {
        List<ArticleEmplacement> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseArticleEmplacement a : list) {
                r.add(buildBeanArticleEmplacement(a));
            }
        }
        return r;
    }

    public static Emplacement buildBeanEmplacement(YvsBaseEmplacementDepot y) {
        Emplacement e = new Emplacement();
        if (y != null) {
            e.setId(y.getId());
            e.setActif((y.getActif() != null) ? y.getActif() : false);
            e.setSelectActif(y.isSelectActif());
            e.setCode(y.getCode());
            e.setDefaut((y.getDefaut() != null) ? y.getDefaut() : false);
            e.setDescription(y.getDescription());
            e.setDesignation(y.getDesignation());
            e.setUpdate(true);
            e.setDateSave(y.getDateSave());
        }
        return e;
    }

    public static List<Emplacement> buildBeanListEmplacement(List<YvsBaseEmplacementDepot> list) {
        List<Emplacement> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseEmplacementDepot a : list) {
                r.add(buildBeanEmplacement(a));
            }
        }
        return r;
    }

    public static PointVenteDepot buildBeanPointVenteDepot(YvsBasePointVenteDepot y) {
        PointVenteDepot p = new PointVenteDepot();
        if (y != null) {
            p.setActif((y.getActif() != null) ? y.getActif() : false);
            p.setId(y.getId());
            p.setPointVente((y.getPointVente() != null) ? buildBeanPointVente(y.getPointVente()) : new PointVente());
            p.setDepot((y.getDepot() != null) ? buildBeanDepot(y.getDepot()) : new Depots());
            p.setPrincipal(y.getPrincipal());
            p.setUpdate(true);
            p.setDateSave(y.getDateSave());
        }
        return p;
    }

    public static List<PointVenteDepot> buildBeanListPointVenteDepot(List<YvsBasePointVenteDepot> list) {
        List<PointVenteDepot> r = new ArrayList<>();
        if (list != null) {
            for (YvsBasePointVenteDepot a : list) {
                r.add(buildBeanPointVenteDepot(a));
            }
        }
        return r;
    }

    public static ArticleDepot buildBeanArticlePoint(YvsBaseConditionnementPoint y) {
        ArticleDepot a = new ArticleDepot();
        if (y != null) {
            a.setId(y.getId());
            a.setActif((y.getArticle().getActif() != null) ? y.getArticle().getActif() : false);
            a.setArticle((y.getArticle() != null) ? UtilProd.buildBeanArticles(y.getArticle().getArticle()) : new Articles());
            a.setDesignation(a.getArticle().getDesignation());
            a.setRefArt(a.getArticle().getRefArt());
            a.setPuv((y.getPuv() != null) ? y.getPuv() : 0);
            a.setPuvMin(y.getArticle().getPuvMin());
            a.setNaturePrixMin(y.getNaturePrixMin());
            a.setAvanceCommance(y.getAvanceCommance());
            a.setPrixMinIsTaux(a.getNaturePrixMin().equals(Constantes.NATURE_TAUX));
            a.setPoint(buildSimpleBeanPointVente(y.getArticle().getPoint()));
            a.setRemise(y.getRemise());
            a.setNatureRemise(y.getNatureRemise());
            a.setSupp((y.getArticle().getSupp() != null) ? y.getArticle().getSupp() : false);
            a.setChangePrix(y.getArticle().getChangePrix());
            a.setPrioritaire(y.getArticle().getPrioritaire());
            a.getArticle().setConditionnements(y.getArticle().getArticle().getConditionnements());
            a.setConditionnement(UtilProd.buildSimpleBeanConditionnement(y.getConditionnement()));
            a.setTauxPua(y.getTauxPua());
            a.setProportionPua(y.getProportionPua());
            a.setUpdate(true);
            a.setRabais(y.getRabais());
        }
        return a;
    }

    public static PointVente buildSimpleBeanPointVente(YvsBasePointVente y) {
        PointVente p = new PointVente();
        if (y != null) {
            p.setAdresse(y.getAdresse());
            p.setTelephone(y.getTelephone());
            p.setCode(y.getCode());
            p.setColor(Util.asString(y.getColor()) ? y.getColor().replace("#", "") : "");
            p.setCodePin(y.getCodePin());
            p.setId(y.getId());
            p.setLibelle(y.getLibelle());
            p.setValidationReglement(y.getValidationReglement());
            p.setReglementAuto(y.getReglementAuto());
            p.setAcceptClientNoName(y.getAcceptClientNoName());
            p.setComptabilisationAuto(y.getComptabilisationAuto());
            p.setType(y.getType());
//            p.setArticles(y.getArticle().getArticles());
            p.setActif(y.getActif());
            p.setLivraisonOn(y.getLivraisonOn());
            p.setCommissionFor(y.getCommissionFor());
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(y.getDateUpdate());
            p.setPhoto(y.getPhoto());
            p.setPrixMinStrict(y.getPrixMinStrict());
            p.setVenteOnline(y.getVenteOnline());
            p.setActiverNotification(y.getActiverNotification());
            p.setMiminumActiveLivraison(y.getMiminumActiveLivraison());
            p.setSaisiePhoneObligatoire(y.getSaisiePhoneObligatoire());
            if (y.getClient() != null ? y.getClient().getId() > 0 : false) {
                p.setClient(new Client(y.getClient().getId(), y.getClient().getCodeClient(), y.getClient().getNom(), y.getClient().getPrenom()));
            }
            if (y.getParent() != null ? y.getParent().getId() > 0 : false) {
                p.setParent(new PointVente(y.getParent().getId(), y.getParent().getLibelle()));
            } else {
                p.setParent(new PointVente());
            }
            p.setAgence((y.getAgence() != null) ? buildBeanAgence(y.getAgence()) : new Agence());
            p.setUpdate(true);
        }
        return p;
    }

    public static PointVente buildBeanPointVente(YvsBasePointVente y) {
        PointVente p = buildSimpleBeanPointVente(y);
        if (y != null) {
            p.setSecteur((y.getSecteur() != null) ? buildBeanDictionnaire(y.getSecteur()) : new Dictionnaire());
            p.setResponsable(UtilGrh.buildBeanSimplePartialEmploye(y.getResponsable()));
            p.setListTranche(y.getListTranche());
            p.setCommerciaux(y.getCommerciaux());
        }
        return p;
    }

    public static YvsBasePointVente buildPointVente(PointVente y, YvsUsersAgence currentUser, YvsAgences currentAgence) {
        YvsBasePointVente p = new YvsBasePointVente();
        if (y != null) {
            p.setAdresse(y.getAdresse());
            p.setTelephone(y.getTelephone());
            if (y.getAgence() != null ? y.getAgence().getId() > 0 : false) {
                p.setAgence(new YvsAgences(y.getAgence().getId()));
            } else {
                p.setAgence(currentAgence);
            }
            p.setAcceptClientNoName(y.isAcceptClientNoName());
            p.setComptabilisationAuto(y.isComptabilisationAuto());
            p.setCode(y.getCode());
            p.setId(y.getId());
            p.setLibelle(y.getLibelle());
            p.setAuthor(currentUser);
            p.setActif(y.isActif());
            p.setType(y.getType());
            p.setColor(Util.asString(y.getColor()) ? ("#" + y.getColor()) : y.getColor());
            p.setCodePin(y.getCodePin());
//            p.setArticles(y.getArticles());
            p.setMiminumActiveLivraison(y.getMiminumActiveLivraison());
            p.setLivraisonOn(y.getLivraisonOn());
            p.setPhoto(y.getPhoto());
            p.setCommissionFor(y.getCommissionFor());
            p.setValidationReglement(y.isValidationReglement());
            p.setReglementAuto(y.isReglementAuto());
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
            p.setPrixMinStrict(y.isPrixMinStrict());
            p.setVenteOnline(y.isVenteOnline());
            p.setActiverNotification(y.isActiverNotification());
            p.setSaisiePhoneObligatoire(y.isSaisiePhoneObligatoire());
            if (y.getClient() != null ? y.getClient().getId() > 0 : false) {
                p.setClient(new YvsComClient(y.getClient().getId(), y.getClient().getCodeClient(), y.getClient().getNom(), y.getClient().getPrenom()));
            }
            if (y.getParent() != null ? y.getParent().getId() > 0 : false) {
                p.setParent(new YvsBasePointVente(y.getParent().getId(), y.getParent().getLibelle()));
            }
            if (y.getSecteur() != null ? y.getSecteur().getId() > 0 : false) {
                p.setSecteur(new YvsDictionnaire(y.getSecteur().getId(), y.getSecteur().getLibelle(), y.getSecteur().getTitre()));
            }
            if (y.getResponsable() != null ? y.getResponsable().getId() > 0 : false) {
                p.setResponsable(new YvsGrhEmployes(y.getResponsable().getId(), y.getResponsable().getNom(), y.getResponsable().getPrenom()));
            }
        }
        return p;
    }

    public static List<PointVente> buildBeanListPointVente(List<YvsBasePointVente> list) {
        List<PointVente> r = new ArrayList<>();
        if (list != null) {
            for (YvsBasePointVente a : list) {
                r.add(buildBeanPointVente(a));
            }
        }
        return r;
    }

    public static ConditionnementDepot buildBeanConditionnement(YvsBaseConditionnementDepot y) {
        ConditionnementDepot r = new ConditionnementDepot();
        if (y != null) {
            r.setArticle(buildBeanArticleDepot(y.getArticle()));
            r.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            r.setId(y.getId());
            r.setOperation(buildBeanDepotOperation(y.getOperation()));
        }
        return r;
    }

    public static YvsBaseConditionnementDepot buildConditionnement(ConditionnementDepot y, YvsUsersAgence ua) {
        YvsBaseConditionnementDepot r = new YvsBaseConditionnementDepot();
        if (y != null) {
            r.setId(y.getId());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticleDepot(y.getArticle().getId()));
            }
            if (y.getConditionnement() != null ? y.getConditionnement().getId() > 0 : false) {
                r.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            if (y.getOperation() != null ? y.getOperation().getId() > 0 : false) {
                r.setOperation(new YvsBaseDepotOperation(y.getOperation().getId(), y.getOperation().getOperation(), y.getOperation().getType()));
            }
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static ConditionnementPoint buildBeanConditionnement(YvsBaseConditionnementPoint y) {
        ConditionnementPoint r = new ConditionnementPoint();
        if (y != null) {
            r.setId(y.getId());
            r.setArticle(buildBeanArticlePoint(y));
            r.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            r.setNaturePrixMin(y.getNaturePrixMin());
            r.setNatureRemise(y.getNatureRemise());
            r.setPrixMin(y.getPrixMin());
            r.setPuv(y.getPuv());
            r.setRemise(y.getRemise());
            r.setTauxPua(y.getTauxPua());
            r.setProportionPua(y.getProportionPua());
        }
        return r;
    }

    public static YvsBaseConditionnementPoint buildConditionnement(ConditionnementPoint y, YvsUsersAgence ua) {
        YvsBaseConditionnementPoint r = new YvsBaseConditionnementPoint();
        if (y != null) {
            r.setId(y.getId());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticlePoint(y.getArticle().getId()));
            }
            if (y.getConditionnement() != null ? y.getConditionnement().getId() > 0 : false) {
                r.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            r.setNaturePrixMin(y.getNaturePrixMin());
            r.setNatureRemise(y.getNatureRemise());
            r.setPrixMin(y.getPrixMin());
            r.setPuv(y.getPuv());
            r.setRemise(y.getRemise());
            r.setAuthor(ua);
            r.setTauxPua(y.getTauxPua());
            r.setProportionPua(y.isProportionPua());
            r.setNew_(true);
        }
        return r;
    }

    public static ConditionnementFsseur buildBeanConditionnement(YvsBaseConditionnementFournisseur y) {
        ConditionnementFsseur r = new ConditionnementFsseur();
        if (y != null) {
            r.setId(y.getId());
            r.setArticle(buildBeanArticleFournisseur(y.getArticle()));
            r.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            r.setUnite(UtilProd.buildBeanUniteMesure(y.getConditionnement().getUnite()));
            r.setPua(y.getPua());
            r.setPrincipal(y.getPrincipal());
        }
        return r;
    }

    public static YvsBaseConditionnementFournisseur buildConditionnement(ConditionnementFsseur y, YvsUsersAgence ua) {
        YvsBaseConditionnementFournisseur r = new YvsBaseConditionnementFournisseur();
        if (y != null) {
            r.setId(y.getId());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticleFournisseur(y.getArticle().getId()));
            }
            if (y.getConditionnement() != null ? y.getConditionnement().getId() > 0 : false) {
                r.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            r.setDateUpdate(new Date());
            r.setPua(y.getPua());
            r.setPrincipal(y.isPrincipal());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static Depots buildBeanDepot(YvsBaseDepots y) {
        Depots d = buildSimpleBeanDepot(y);
        if (y != null) {
            d.setAgence((y.getAgence() != null) ? buildBeanAgence(y.getAgence()) : new Agence());
            d.setResponsable((y.getResponsable() != null) ? UtilGrh.buildBeanSimplePartialEmploye(y.getResponsable()) : new Employe());
            d.setOperations(y.getOperations());
            d.setCodeAcces((y.getCodeAcces() != null ? y.getCodeAcces().getCode() : ""));
        }
        return d;
    }

    public static Depots buildSimpleBeanDepot(YvsBaseDepots y) {
        Depots d = new Depots();
        if (y != null) {
            d.setId(y.getId());
            d.setAbbreviation(y.getAbbreviation());
            d.setActif(y.getActif());
            d.setAdresse(y.getAdresse());
            d.setCode(y.getCode());
            d.setControlStock(y.getControlStock());
            d.setCrenau(y.getCrenau());
            d.setDesignation(y.getDesignation());
            d.setOpAchat(y.getOpAchat());
            d.setOpProduction(y.getOpProduction());
            d.setOpTransit(y.getOpTransit());
            d.setOpVente(y.getOpVente());
            d.setOpRetour(y.getOpRetour());
            d.setOpReserv(y.getOpReserv());
            d.setOpTechnique(y.getOpTechnique());
            d.setDescription(y.getDescription());
            d.setDateSave(d.getDateSave());
            d.setDateUpdate(d.getDateUpdate());
            d.setTypeMp(y.getTypeMp());
            d.setTypeNegoce(y.getTypeNe());
            d.setTypePf(y.getTypePf());
            d.setTypePsf(y.getTypePsf());
            d.setVerifyAppro(y.getVerifyAppro());
            d.setVerifyAllValidInventaire(y.getVerifyAllValidInventaire());
            d.setRequiereLot(y.getRequiereLot());
        }
        return d;
    }

    public static List<Depots> buildBeanListDepot(List<YvsBaseDepots> list) {
        List<Depots> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseDepots a : list) {
                r.add(buildBeanDepot(a));
            }
        }
        return r;
    }

    public static YvsBaseEmplacementDepot buildEmplacement(Emplacement y, YvsUsersAgence currentUser, YvsBaseDepots selectDepot) {
        YvsBaseEmplacementDepot e = new YvsBaseEmplacementDepot();
        if (y != null) {
            e.setId(y.getId());
            e.setActif(y.isActif());
            e.setCode(y.getCode());
            e.setDefaut(y.isDefaut());
            e.setDescription(y.getDescription());
            e.setDesignation(y.getDesignation());
            if ((y.getParent() != null) ? y.getParent().getId() > 0 : false) {
                e.setParent(new YvsBaseEmplacementDepot(y.getParent().getId()));
            }
            e.setDepot(selectDepot);
            e.setAuthor(currentUser);
            e.setDateSave(y.getDateSave());
            e.setDateUpdate(new Date());
        }
        return e;
    }

    public static YvsBaseDepots buildDepot(Depots y, YvsUsersAgence currentUser, YvsAgences currentAgence) {
        YvsBaseDepots d = new YvsBaseDepots();
        if (y != null) {
            d.setAbbreviation(y.getAbbreviation());
            d.setActif(y.isActif());
            d.setAdresse(y.getAdresse());
            d.setCode(y.getCode());
            d.setControlStock(y.isControlStock());
            d.setCrenau(y.isCrenau());
            d.setDesignation(y.getDesignation());
            d.setId(y.getId());
            d.setDescription(y.getDescription());
            d.setOpProduction(y.isOpProduction());
            d.setOpAchat(y.isOpAchat());
            d.setOpTransit(y.isOpTransit());
            d.setOpVente(y.isOpVente());
            d.setOpRetour(y.isOpRetour());
            d.setOpReserv(y.isOpReserv());
            d.setOpTechnique(y.isOpTechnique());
            d.setArticles(y.getArticles());
            d.setDateSave(y.getDateSave());
            d.setDateUpdate(new Date());
            d.setTypeMp(y.isTypeMp());
            d.setTypeNe(y.isTypeNegoce());
            d.setTypePf(y.isTypePf());
            d.setTypePsf(y.isTypePsf());
            d.setVerifyAppro(y.isVerifyAppro());
            d.setVerifyAllValidInventaire(y.isVerifyAllValidInventaire());
            d.setRequiereLot(y.isRequiereLot());
            d.setSupp(false);
            if ((y.getResponsable() != null) ? y.getResponsable().getId() > 0 : false) {
                d.setResponsable(new YvsGrhEmployes(y.getResponsable().getId(), y.getResponsable().getNom(), y.getResponsable().getPrenom()));
            }
            if (y.getAgence() != null ? y.getAgence().getId() > 0 : false) {
                d.setAgence(new YvsAgences(y.getAgence().getId(), y.getAgence().getDesignation()));
            } else {
                d.setAgence(currentAgence);
            }
            d.setAuthor(currentUser);
        }
        return d;
    }

    public static YvsComLiaisonDepot buildLiaisonDepot(LiaisonDepot y, YvsUsersAgence currentUser, YvsBaseDepots selectDepot, YvsAgences agence) {
        YvsComLiaisonDepot l = new YvsComLiaisonDepot();
        if (y != null) {
            l.setId(y.getId());
            if ((y.getDepot() != null) ? y.getDepot().getId() > 0 : false) {
                l.setDepotLier(new YvsBaseDepots(y.getDepot().getId(), y.getDepot().getDesignation(), agence));
            }
            l.setDepot(selectDepot);
            l.setAuthor(currentUser);
            l.setDateSave(y.getDateSave());
            l.setDateUpdate(new Date());
        }
        return l;
    }

    public static YvsBasePointVenteDepot buildPointVenteDepot(PointVenteDepot y, YvsUsersAgence currentUser, YvsBasePointVente selectPoint) {
        return buildPointVenteDepot(y, currentUser, new YvsBaseDepots(y.getDepot().getId(), y.getDepot().getDesignation()), selectPoint);
    }

    public static YvsBasePointVenteDepot buildPointVenteDepot(PointVenteDepot y, YvsUsersAgence currentUser, YvsBaseDepots selectDepot, YvsBasePointVente selectPoint) {
        YvsBasePointVenteDepot p = new YvsBasePointVenteDepot();
        if (y != null) {
            p.setActif(y.isActif());
            p.setPrincipal(y.isPrincipal());
            p.setId(y.getId());
            if ((selectPoint != null) ? selectPoint.getId() > 0 : false) {
                p.setPointVente(selectPoint);
            }
            if ((selectDepot != null) ? selectDepot.getId() > 0 : false) {
                p.setDepot(selectDepot);
            }
            p.setAuthor(currentUser);
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
        }
        return p;
    }

    public static PointLivraison buildBeanPointLivraison(YvsBasePointLivraison y) {
        PointLivraison r = new PointLivraison();
        if (y != null) {
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setLibelle(y.getLibelle());
            r.setTelephone(y.getTelephone());
            r.setLieuDit(y.getLieuDit());
            r.setDescription(y.getDescription());
            r.setVille(buildBeanDictionnaire(y.getVille()));
            if (y.getClient() != null) {
                r.setClient(new Client(y.getClient().getId(), y.getClient().getCodeClient(), y.getClient().getNom(), y.getClient().getPrenom()));
            } else {
                r.setClient(new Client());
            }
            if (r.getVille().getId() > 0) {
                r.setPays(r.getVille().getParent());
            }
        }
        return r;
    }

    public static YvsBasePointLivraison buildPointLivraison(PointLivraison y, YvsSocietes ste, YvsUsersAgence ua) {
        YvsBasePointLivraison r = null;
        if (y != null) {
            r = new YvsBasePointLivraison();
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setLibelle(y.getLibelle());
            r.setTelephone(y.getTelephone());
            r.setLieuDit(y.getLieuDit());
            r.setDescription(y.getDescription());
            if (y.getVille() != null ? y.getVille().getId() > 0 : false) {
                r.setVille(new YvsDictionnaire(y.getVille().getId(), y.getVille().getLibelle()));
            }
            if (y.getClient() != null ? y.getClient().getId() > 0 : false) {
                r.setClient(new YvsComClient(y.getClient().getId(), y.getClient().getCodeClient(), y.getClient().getNom(), y.getClient().getPrenom()));
            }
            r.setSociete(ste);
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static YvsGrhTrancheHoraire buildTrancheHoraire(TrancheHoraire y, YvsUsersAgence currentUser) {
        YvsGrhTrancheHoraire t = new YvsGrhTrancheHoraire();
        if (y != null) {
            t.setActif(y.isActif());
            t.setHeureDebut((y.getHeureDebut() != null) ? y.getHeureDebut() : new Date());
            t.setHeureFin((y.getHeureFin() != null) ? y.getHeureFin() : new Date());
            t.setId(y.getId());
            t.setTitre(y.getTitre());
            t.setTypeJournee(y.getTypeJournee());
            t.setAuthor(currentUser);
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());
            t.setVenteOnline(y.isVenteOnline());
        }
        return t;
    }

    public static YvsBaseArticlePoint buildArticlePoint(ArticleDepot y, YvsUsersAgence currentUser, YvsBasePointVente selectPoint) {
        YvsBaseArticlePoint a = new YvsBaseArticlePoint();
        if (y != null) {
            a.setId(y.getId());
            a.setActif(y.isActif());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                a.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            a.setPoint(selectPoint);
            a.setSupp(y.isSupp());
            a.setChangePrix(y.isChangePrix());
            a.setPrioritaire(y.isPrioritaire());
            a.setAuthor(currentUser);
            a.setNew_(true);
            a.setDateSave(y.getDateSave());
            a.setDateUpdate(new Date());
        }
        return a;
    }

    public static YvsComCreneauPoint buildCreneau(Creneau y, YvsUsersAgence currentUser, YvsBasePointVente selectPoint) {
        YvsComCreneauPoint c = new YvsComCreneauPoint();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            c.setPermanent(y.isPermanent());
            c.setCritere(y.getCritere());
            c.setCritere_(y.getCritere_());
            if ((y.getTranche() != null) ? y.getTranche().getId() > 0 : false) {
                c.setTranche(buildTrancheHoraire(y.getTranche(), currentUser));
            }
            if ((y.getJour() != null) ? y.getJour().getId() > 0 : false) {
                c.setJour(new YvsJoursOuvres(y.getJour().getId(), y.getJour().getJour()));
            }
            if ((selectPoint != null) ? selectPoint.getId() > 0 : false) {
                c.setPoint(selectPoint);
            }
            c.setAuthor(currentUser);
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
        }
        return c;
    }

    public static YvsComCreneauDepot buildCreneau(Creneau y, YvsUsersAgence currentUser, YvsBaseDepots selectDepot) {
        YvsComCreneauDepot c = new YvsComCreneauDepot();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            c.setPermanent(y.isPermanent());
            c.setCritere(y.getCritere());
            c.setCritere_(y.getCritere_());
            if ((y.getTranche() != null) ? y.getTranche().getId() > 0 : false) {
                c.setTranche(buildTrancheHoraire(y.getTranche(), currentUser));
            }
            if ((y.getJour() != null) ? y.getJour().getId() > 0 : false) {
                c.setJour(new YvsJoursOuvres(y.getJour().getId(), y.getJour().getJour()));
            }
            if ((selectDepot != null) ? selectDepot.getId() > 0 : false) {
                c.setDepot(selectDepot);
            }
            c.setAuthor(currentUser);
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
        }
        return c;
    }

    public static YvsBaseDepotOperation buildBeanDepotOperation(DepotOperation y, YvsUsersAgence currentUser, YvsBaseDepots selectDepot) {
        YvsBaseDepotOperation d = new YvsBaseDepotOperation();
        if (y != null) {
            d.setId(y.getId());
            d.setOperation(y.getOperation());
            d.setType(y.getType());
            if ((selectDepot != null) ? selectDepot.getId() > 0 : false) {
                d.setDepot(selectDepot);
            }
            d.setAuthor(currentUser);
            d.setDateSave(y.getDateSave());
            d.setDateUpdate(new Date());
        }
        return d;
    }


    /*
     *
     *FIN GESTION Depots
     *
     */
    /*
     *
     *DEBUT GESTION RISTOURNE, COMMISON ET REMISE
     *
     */
    public static GrilleRabais buildBeanGrilleRemise(YvsComGrilleRemise y) {
        GrilleRabais r = new GrilleRabais();
        if (y != null) {
            r.setId(y.getId());
            r.setMontantMaximal(y.getMontantMaximal() >= Double.MAX_VALUE ? 0 : y.getMontantMaximal());
            r.setMontantMinimal((y.getMontantMinimal() != null) ? y.getMontantMinimal() : 0);
            r.setMontantRabais((y.getMontantRemise() != null) ? y.getMontantRemise() : 0);
            r.setNatureMontant((y.getNatureMontant() != null) ? y.getNatureMontant() : Constantes.NATURE_MTANT);
            r.setBase((y.getBase() != null) ? y.getBase() : Constantes.BASE_QTE);
            r.setUnique(y.isUnique());
            r.setParent(y.getRemise().getId());
            r.setDateSave(y.getDateSave());
            r.setUpdate(true);
        }
        return r;
    }

    public static List<GrilleRabais> buildBeanListGrilleRemise(List<YvsComGrilleRemise> list) {
        List<GrilleRabais> r = new ArrayList<>();
        if (list != null) {
            for (YvsComGrilleRemise a : list) {
                r.add(buildBeanGrilleRemise(a));
            }
        }
        return r;
    }

    public static Remise buildBeanRemise(YvsComRemise y) {
        Remise r = new Remise();
        if (y != null) {
            r.setId(y.getId());
            r.setReference(y.getRefRemise());
            r.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            r.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            r.setActif((y.getActif() != null) ? y.getActif() : false);
            r.setDateSave(y.getDateSave());
            r.setPermanent((y.getPermanent() != null) ? y.getPermanent() : false);
            r.setGrilles((y.getGrilles() != null) ? buildBeanListGrilleRemise(y.getGrilles()) : new ArrayList<GrilleRabais>());
            r.setCodeAcces(y.getCodeAcces() != null ? y.getCodeAcces().getCode() : "");
            r.setDescription(y.getDescription());
            r.setUpdate(true);
        }
        return r;
    }

    public static List<Remise> buildBeanListRemise(List<YvsComRemise> list) {
        List<Remise> r = new ArrayList<>();
        if (list != null) {
            for (YvsComRemise a : list) {
                r.add(buildBeanRemise(a));
            }
        }
        return r;
    }

    public static GrilleRabais buildBeanGrilleRistourne(YvsComGrilleRistourne y) {
        GrilleRabais r = new GrilleRabais();
        if (y != null) {
            r.setId(y.getId());
            r.setMontantMaximal(y.getMontantMaximal() >= Double.MAX_VALUE ? 0 : y.getMontantMaximal());
            r.setMontantMinimal((y.getMontantMinimal() != null) ? y.getMontantMinimal() : 0);
            r.setMontantRabais((y.getMontantRistourne() != null) ? y.getMontantRistourne() : 0);
            r.setNatureMontant((y.getNatureMontant() != null) ? y.getNatureMontant() : Constantes.NATURE_MTANT);
            r.setBase((y.getBase() != null) ? y.getBase() : Constantes.BASE_QTE);
            r.setUnique(y.isUnique());
            r.setParent(y.getRistourne().getId());
            r.setArticle(buildBeanArticle(y.getArticle()));
            r.setDateSave(y.getDateSave());
            r.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            r.setUpdate(true);
        }
        return r;
    }

    public static YvsComGrilleRistourne buildGrilleRistourne(GrilleRabais y, YvsUsersAgence currentUser) {
        YvsComGrilleRistourne r = new YvsComGrilleRistourne();
        if (y != null) {
            r.setId(y.getId());
            if (y.getParent() > 0) {
                r.setRistourne(new YvsComRistourne(y.getParent()));
            }
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            if ((y.getConditionnement() != null) ? y.getConditionnement().getId() > 0 : false) {
                r.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            r.setMontantMaximal(y.getMontantMaximal());
            r.setMontantMinimal(y.getMontantMinimal());
            r.setMontantRistourne(y.getMontantRabais());
            r.setNatureMontant(y.getNatureMontant());
            r.setDateSave(y.getDateSave());
            r.setBase(y.getBase());
            r.setAuthor(currentUser);
            r.setNew_(true);
            r.setDateUpdate(new Date());
        }
        return r;
    }

    public static Ristourne buildBeanRistourne(YvsComRistourne y) {
        Ristourne r = new Ristourne();
        if (y != null) {
            r.setId(y.getId());
            r.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            r.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            r.setActif((y.getActif() != null) ? y.getActif() : false);
            r.setPermanent((y.getPermanent() != null) ? y.getPermanent() : false);
            r.setTranches(y.getTranches());
            r.setPlan(buildBeanPlanRistourne(y.getPlan()));
            r.setArticle(UtilProd.buildBeanArticles(y.getArticle()));
            r.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            r.setFamille(UtilProd.buildBeanFamilleArticle(y.getFamille()));
            r.setNature(y.getNature());
            r.setDateSave(y.getDateSave());
            r.setForArticle(y.getArticle() != null);
            r.setUpdate(true);
            for (YvsComGrilleRistourne g : y.getTranches()) {
                if (!g.isUnique()) {
                    r.setMontant(g.getMontantRistourne());
                    r.setBase(g.getBase());
                    r.setNatureMontant(g.getNatureMontant());
                    break;
                }
            }
        }
        return r;
    }

    public static YvsComRistourne buildRistourne(Ristourne y, YvsUsersAgence currentUser) {
        YvsComRistourne r = new YvsComRistourne();
        if (y != null) {
            r.setId(y.getId());
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            if ((y.getFamille() != null) ? y.getFamille().getId() > 0 : false) {
                r.setFamille(new YvsBaseFamilleArticle(y.getFamille().getId(), y.getFamille().getReference(), y.getFamille().getDesignation()));
            }
            if ((y.getPlan() != null) ? y.getPlan().getId() > 0 : false) {
                r.setPlan(new YvsComPlanRistourne(y.getPlan().getId()));
            }
            if ((y.getConditionnement() != null) ? y.getConditionnement().getId() > 0 : false) {
                r.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            r.setActif(y.isActif());
            r.setPermanent(y.isPermanent());
            r.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            r.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            r.setAuthor(currentUser);
            r.setNature(y.getNature());
            r.setNew_(true);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
        }
        return r;
    }

    public static YvsComPlanRistourne buildPlanRistourne(PlanRistourne y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        YvsComPlanRistourne r = new YvsComPlanRistourne();
        if (y != null) {
            r.setId(y.getId());
            r.setReference(y.getReference());
            r.setActif(y.isActif());
            r.setSociete(currentScte);
            r.setAuthor(currentUser);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }

    public static PlanRistourne buildBeanPlanRistourne(YvsComPlanRistourne y) {
        PlanRistourne r = new PlanRistourne();
        if (y != null) {
            r.setId(y.getId());
            r.setActif((y.getActif() != null) ? y.getActif() : false);
            r.setReference(y.getReference());
            r.setRistournes(y.getRistournes());
            r.setDateSave(y.getDateSave());
            r.setUpdate(true);
        }
        return r;
    }

    public static GrilleRabais buildBeanGrilleRabais(YvsComGrilleRabais y) {
        GrilleRabais r = new GrilleRabais();
        if (y != null) {
            r.setId(y.getId());
            r.setMontantMaximal(y.getMontantMaximal() >= Double.MAX_VALUE ? 0 : y.getMontantMaximal());
            r.setMontantMinimal((y.getMontantMinimal() != null) ? y.getMontantMinimal() : 0);
            r.setMontantRabais((y.getMontantRabais() != null) ? y.getMontantRabais() : 0);
            r.setNatureMontant((y.getNatureMontant() != null) ? y.getNatureMontant() : Constantes.NATURE_MTANT);
            r.setBase((y.getBase() != null) ? y.getBase() : Constantes.BASE_QTE);
            r.setUnique(y.isUnique());
            r.setParent(y.getRabais().getId());
            r.setDateSave(y.getDateSave());
            r.setUpdate(true);
        }
        return r;
    }

    public static YvsComGrilleRabais buildGrilleRabais(GrilleRabais y, YvsUsersAgence currentUser) {
        YvsComGrilleRabais r = new YvsComGrilleRabais();
        if (y != null) {
            r.setId(y.getId());
            if (y.getParent() > 0) {
                r.setRabais(new YvsComRabais(y.getParent()));
            }
            r.setMontantMaximal(y.getMontantMaximal());
            r.setMontantMinimal(y.getMontantMinimal());
            r.setMontantRabais(y.getMontantRabais());
            r.setNatureMontant(y.getNatureMontant());
            r.setDateSave(y.getDateSave());
            r.setBase(y.getBase());
            r.setAuthor(currentUser);
            r.setNew_(true);
            r.setDateUpdate(new Date());
        }
        return r;
    }

    public static Rabais buildBeanRabais(YvsComRabais y) {
        Rabais r = new Rabais();
        if (y != null) {
            r.setId(y.getId());
            r.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            r.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            r.setActif((y.getActif() != null) ? y.getActif() : false);
            r.setPermanent((y.getPermanent() != null) ? y.getPermanent() : false);
            r.setTranches(y.getTranches());
            r.setPoint(UtilCom.buildSimpleBeanPointVente(y.getArticle().getArticle().getPoint()));
            r.setConditionnement(UtilProd.buildBeanConditionnement(y.getArticle().getConditionnement()));
            r.getArticle().setArticle(UtilProd.buildSimpleBeanArticles(y.getArticle().getArticle().getArticle()));
            r.setMontant(y.getMontant());
            r.setDateSave(y.getDateSave());
            r.setUpdate(true);
        }
        return r;
    }

    public static YvsComRabais buildRabais(Rabais y, YvsUsersAgence currentUser) {
        YvsComRabais r = new YvsComRabais();
        if (y != null) {
            r.setId(y.getId());
            r.setActif(y.isActif());
            r.setPermanent(y.isPermanent());
            r.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            r.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            r.setMontant(y.getMontant());
            r.setNew_(true);
            r.setAuthor(currentUser);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
        }
        return r;
    }

    public static CibleFacteur buildBeanCibleFacteur(YvsComCibleFacteurTaux y) {
        CibleFacteur r = new CibleFacteur();
        if (y != null) {
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setFacteur(buildBeanFacteurTaux(y.getFacteur()));
            r.setIdExterne(y.getIdExterne());
            r.setLibelle(y.getLibelle());
            r.setTableExterne(y.getTableExterne());
        }
        return r;
    }

    public static YvsComCibleFacteurTaux buildCibleFacteur(CibleFacteur y, YvsUsersAgence ua) {
        YvsComCibleFacteurTaux r = new YvsComCibleFacteurTaux();
        if (y != null) {
            r.setId(y.getId());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            if (y.getFacteur() != null ? y.getFacteur().getId() > 0 : false) {
                r.setFacteur(new YvsComFacteurTaux(y.getFacteur().getId()));
            }
            r.setIdExterne(y.getIdExterne());
            r.setLibelle(y.getLibelle());
            r.setTableExterne(y.getTableExterne());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static FacteurTaux buildBeanFacteurTaux(YvsComFacteurTaux y) {
        FacteurTaux r = new FacteurTaux();
        if (y != null) {
            r.setId(y.getId());
            r.setBase(y.getBase());
            r.setChampApplication(y.getChampApplication());
            r.setGeneral(y.getGeneral());
            r.setNouveauClient(y.getNouveauClient());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setTaux(y.getTaux());
            r.setByGrille(y.isByGrille());
            r.setActif(y.getActif());
            r.setPermanent(y.getPermanent());
            r.setPlan(buildBeanPlanCommission(y.getPlan()));
            r.setTypeGrille(buildBeanTypeGrille(y.getTypeGrille()));
            r.setCibles(y.getCibles());
            r.setPeriodicites(y.getPeriodicites());
        }
        return r;
    }

    public static YvsComFacteurTaux buildFacteurTaux(FacteurTaux y, YvsUsersAgence ua) {
        YvsComFacteurTaux r = new YvsComFacteurTaux();
        if (y != null) {
            r.setId(y.getId());
            r.setBase(y.getBase());
            r.setChampApplication(y.getChampApplication());
            r.setGeneral(y.isGeneral());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNouveauClient(y.isNouveauClient());
            r.setTaux(y.getTaux());
            r.setActif(y.isActif());
            r.setPermanent(y.isPermanent());
            r.setByGrille(y.isByGrille());
            if (y.getPlan() != null ? y.getPlan().getId() > 0 : false) {
                r.setPlan(new YvsComPlanCommission(y.getPlan().getId(), y.getPlan().getReference(), y.getPlan().getIntitule()));
            }
            if (y.getTypeGrille() != null ? y.getTypeGrille().getId() > 0 : false) {
                r.setTypeGrille(new YvsComTypeGrille(y.getTypeGrille().getId(), y.getTypeGrille().getReference()));
            }
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static TypeGrille buildBeanTypeGrille(YvsComTypeGrille y) {
        TypeGrille r = new TypeGrille();
        if (y != null) {
            r.setId(y.getId());
            r.setCible(y.getCible());
            r.setReference(y.getReference());
            r.setTranches(y.getTranches());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
        }
        return r;
    }

    public static YvsComTypeGrille buildTypeGrille(TypeGrille y, YvsUsersAgence ua, YvsSocietes ste) {
        YvsComTypeGrille r = new YvsComTypeGrille();
        if (y != null) {
            r.setId(y.getId());
            r.setCible(y.getCible());
            r.setReference(y.getReference());
            r.setAuthor(ua);
            r.setSociete(ste);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }

    public static Periodicite buildBeanPeriodeValideCommission(YvsComPeriodeValiditeCommision y) {
        Periodicite r = new Periodicite();
        if (y != null) {
            r.setId(y.getId());
            r.setDateDebut(y.getDateDebut());
            r.setDateFin(y.getDateFin());
            r.setPeriodicite(y.getPeriodicite());
            r.setFacteur(buildBeanFacteurTaux(y.getFacteur()));
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
        }
        return r;
    }

    public static YvsComPeriodeValiditeCommision buildPeriodeValideCommission(Periodicite y, YvsUsersAgence ua) {
        YvsComPeriodeValiditeCommision r = new YvsComPeriodeValiditeCommision();
        if (y != null) {
            r.setId(y.getId());
            r.setDateDebut(y.getDateDebut());
            r.setDateFin(y.getDateFin());
            r.setPeriodicite(y.getPeriodicite());
            if (y.getFacteur() != null ? y.getFacteur().getId() > 0 : false) {
                r.setFacteur(new YvsComFacteurTaux(y.getFacteur().getId()));
            }
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static PlanCommission buildBeanPlanCommission(YvsComPlanCommission y) {
        PlanCommission r = new PlanCommission();
        if (y != null) {
            r.setId(y.getId());
            r.setReference(y.getReference());
            r.setIntitule(y.getIntitule());
            r.setActif((y.getActif() != null) ? y.getActif() : false);
            r.setFacteurs(y.getFacteurs());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
        }
        return r;
    }

    public static YvsComPlanCommission buildPlanCommission(PlanCommission y, YvsUsersAgence ua, YvsSocietes ste) {
        YvsComPlanCommission r = new YvsComPlanCommission();
        if (y != null) {
            r.setId(y.getId());
            r.setReference(y.getReference());
            r.setIntitule(y.getIntitule());
            r.setActif(y.isActif());
            r.setSociete(ste);
            r.setAuthor(ua);
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }

    public static GrilleRabais buildBeanTrancheTaux(YvsComTrancheTaux y) {
        GrilleRabais r = new GrilleRabais();
        if (y != null) {
            r.setId(y.getId());
            r.setMontantMaximal(y.getTrancheMaximal() >= Double.MAX_VALUE ? 0 : y.getTrancheMaximal());
            r.setMontantMinimal((y.getTrancheMinimal() != null) ? y.getTrancheMinimal() : 0);
            r.setMontantRabais((y.getTaux() != null) ? y.getTaux() : 0);
            r.setNatureMontant(y.getNature().toString());
            r.setParent(y.getTypeGrille().getId());
            r.setUnique(y.isUnique());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setUpdate(true);
        }
        return r;
    }

    public static YvsComTrancheTaux buildTrancheTaux(GrilleRabais y, YvsUsersAgence ua) {
        YvsComTrancheTaux r = new YvsComTrancheTaux();
        if (y != null) {
            r.setId((int) y.getId());
            r.setTrancheMaximal(y.getMontantMaximal());
            r.setTrancheMinimal(y.getMontantMinimal());
            r.setTaux(y.getMontantRabais());
            r.setNature(y.getNatureMontant() != null ? y.getNatureMontant().trim().length() > 0 ? y.getNatureMontant().charAt(0) : Constantes.NATURE_POURCENTAGE : Constantes.NATURE_POURCENTAGE);
            if (y.getParent() > 0) {
                r.setTypeGrille(new YvsComTypeGrille((int) y.getParent()));
            }
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    /*
     *
     *FIN GESTION RISTOURNE, COMMISON ET REMISE
     *
     */
    /*
     *
     *DEBUT GESTION CRENEAU
     *
     */
    public static TypeCreneau buildBeanTypeCrenau(YvsGrhTrancheHoraire y) {
        TypeCreneau t = new TypeCreneau();
        if (y != null) {
            t.setActif((y.getActif() != null) ? y.getActif() : false);
            t.setHeureDebut((y.getHeureDebut() != null) ? y.getHeureDebut() : new Date());
            t.setHeureFin((y.getHeureFin() != null) ? y.getHeureFin() : new Date());
            t.setId(y.getId());
            t.setReference(y.getTitre());
            t.setCritere(y.getTypeJournee());
            t.setDateSave(y.getDateSave());
            t.setVenteOnline(y.getVenteOnline());
        }
        return t;
    }

    public static YvsGrhTrancheHoraire buildTypeCrenau(TypeCreneau y, YvsUsersAgence ua, YvsSocietes ste) {
        YvsGrhTrancheHoraire t = new YvsGrhTrancheHoraire();
        if (y != null) {
            t.setActif(y.isActif());
            t.setHeureDebut((y.getHeureDebut() != null) ? y.getHeureDebut() : new Date());
            t.setHeureFin((y.getHeureFin() != null) ? y.getHeureFin() : new Date());
            t.setId(y.getId());
            t.setTitre(y.getReference());
            t.setTypeJournee(y.getCritere());
            t.setAuthor(ua);
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());
            t.setVenteOnline(y.isVenteOnline());
            t.setSociete(ste);
        }
        return t;
    }

    public static Creneau buildBeanCreneau(YvsComCreneauDepot y) {
        Creneau c = new Creneau();
        if (y != null) {
            c.setId(y.getId());
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setPermanent(y.getPermanent());
            c.setDepot((y.getDepot() != null) ? buildSimpleBeanDepot(y.getDepot()) : new Depots());
            c.setTranche((y.getTranche() != null) ? UtilGrh.buildTrancheHoraire(y.getTranche()) : new TrancheHoraire());
            c.setJour((y.getJour() != null) ? UtilGrh.buildBeanJoursOuvree(y.getJour()) : new JoursOuvres());
            c.setCritere_(c.getTranche().getTypeJournee());
            c.setCritere(c.getTranche().getTypeJournee());
            c.setSemiReference(y.getSemiReference());
            c.setReference(y.getReference());
            c.setDateSave(y.getDateSave());
        }
        return c;
    }

    public static Creneau buildBeanCreneau(YvsComCreneauPoint y) {
        Creneau c = new Creneau();
        if (y != null) {
            c.setId(y.getId());
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setPermanent(y.getPermanent());
            c.setPoint((y.getPoint() != null) ? buildSimpleBeanPointVente(y.getPoint()) : new PointVente());
            c.setTranche((y.getTranche() != null) ? UtilGrh.buildTrancheHoraire(y.getTranche()) : new TrancheHoraire());
            c.setJour((y.getJour() != null) ? UtilGrh.buildBeanJoursOuvree(y.getJour()) : new JoursOuvres());
            c.setCritere_(c.getTranche().getTypeJournee());
            c.setCritere(c.getTranche().getTypeJournee());
            c.setSemiReference(y.getSemiReference());
            c.setReference(y.getReference());
        }
        return c;
    }

    public static List<Creneau> buildBeanListCreneau(List<YvsComCreneauDepot> list) {
        List<Creneau> r = new ArrayList<>();
        if (list != null) {
            for (YvsComCreneauDepot a : list) {
                r.add(buildBeanCreneau(a));
            }
        }
        return r;
    }

    public static CreneauUsers buildBeanCreneauUsers(YvsComCreneauHoraireUsers y) {
        CreneauUsers c = new CreneauUsers();
        if (y != null) {
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setId_(y.getId());
            c.setCreneauDepot((y.getCreneauDepot() != null) ? buildBeanCreneau(y.getCreneauDepot()) : new Creneau());
            c.setCreneauPoint((y.getCreneauPoint() != null) ? buildBeanCreneau(y.getCreneauPoint()) : new Creneau());
            c.setPersonnel(new Users(y.getUsers().getId(), y.getUsers().getCodeUsers(), y.getUsers().getNomUsers()));
            c.setDateTravail((y.getDateTravail() != null) ? y.getDateTravail() : new Date());
            c.setJour(y.getJour());
            c.setPermanent(y.getPermanent());
            c.setType(y.getType());
            c.setUpdate(true);
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(y.getDateUpdate());
        }
        return c;
    }

    public static CreneauUsers buildSimpleBeanCreneauUsers(YvsComCreneauHoraireUsers y) {
        CreneauUsers c = new CreneauUsers();
        if (y != null) {
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setId_(y.getId());
            c.setCreneauPoint((y.getCreneauPoint() != null) ? buildBeanCreneau(y.getCreneauPoint()) : new Creneau());
            c.setCreneauDepot((y.getCreneauDepot() != null) ? buildBeanCreneau(y.getCreneauDepot()) : new Creneau());
            c.setDateTravail((y.getDateTravail() != null) ? y.getDateTravail() : new Date());
            c.setJour(y.getJour());
            c.setPermanent(y.getPermanent());
            c.setType(y.getType());
            c.setUpdate(true);
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(y.getDateUpdate());
            c.setPersonnel(new Users(y.getUsers().getId(), y.getUsers().getCodeUsers(), y.getUsers().getNomUsers()));
        }
        return c;
    }

    public static List<CreneauUsers> buildBeanListCreneauUsers(List<YvsComCreneauHoraireUsers> list) {
        List<CreneauUsers> r = new ArrayList<>();
        if (list != null) {
            for (YvsComCreneauHoraireUsers a : list) {
                r.add(buildBeanCreneauUsers(a));
            }
        }
        return r;
    }


    /*
     *
     *FIN GESTION CRENEAU
     *
     */

    /*
     *
     *DEBUT GESTION FICHE APPROVISIONNEMENT
     *
     */
    public static YvsComContenuDocAchat buildContenuDocAchat(ContenuDocAchat y, YvsUsersAgence u) {
        YvsComContenuDocAchat c = new YvsComContenuDocAchat();
        if (y != null) {
            c.setActif(true);
            c.setSupp(false);
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                c.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            if ((y.getArticleBonus() != null) ? y.getArticleBonus().getId() > 0 : false) {
                c.setArticleBonus(new YvsBaseArticles(y.getArticleBonus().getId(), y.getArticleBonus().getRefArt(), y.getArticleBonus().getDesignation()));
            }
            if ((y.getQualite() != null) ? y.getQualite().getId() > 0 : false) {
                c.setQualite(new YvsComQualite(y.getQualite().getId(), y.getQualite().getCode(), y.getQualite().getLibelle()));
            }
            if ((y.getConditionnement() != null) ? y.getConditionnement().getId() > 0 : false) {
                c.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            if ((y.getConditionnementBonus() != null) ? y.getConditionnementBonus().getId() > 0 : false) {
                c.setConditionnementBonus(new YvsBaseConditionnement(y.getConditionnementBonus().getId(), new YvsBaseUniteMesure(y.getConditionnementBonus().getUnite().getId(), y.getConditionnementBonus().getUnite().getReference(), y.getConditionnementBonus().getUnite().getLibelle())));
            }
            if ((y.getLot() != null) ? y.getLot().getId() > 0 : false) {
                c.setLot(buildLotReception(y.getLot(), u.getAgence(), u));
            }
            if ((y.getParent() != null) ? y.getParent().getId() > 0 : false) {
                c.setParent(new YvsComContenuDocAchat(y.getParent().getId()));
            }
            if ((y.getDocAchat() != null) ? y.getDocAchat().getId() > 0 : false) {
                c.setDocAchat(new YvsComDocAchats(y.getDocAchat().getId()));
            }
            c.setCommentaire(y.getCommentaire());
            c.setId(y.getId());
            c.setPrixAchat(y.getPrixAchat());
            c.setTaxe(y.getTaxe());
            c.setRemise(y.getRemiseRecu());
            c.setQuantiteRecu(y.getQuantiteRecu());
            c.setQuantiteCommande(y.getQuantiteCommende());
            c.setQuantiteBonus(y.getQuantiteBonus());
            c.setDateContenu((y.getDateContenu() != null) ? y.getDateContenu() : new Date());
            c.setDateLivraison((y.getDateLivraison() != null) ? y.getDateLivraison() : new Date());
            c.setStatut(y.getStatut());
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
            c.setCommentaire(y.getCommentaire());
            c.setPrixTotal(y.getPrixTotalRecu());
            c.setNumSerie(y.getNumSerie());
            c.setCalculPr(y.isCalculPr());
            if (y.getExterne() != null ? y.getExterne().getId() > 0 : false) {
                c.setExterne(new YvsComArticleApprovisionnement(y.getExterne().getId()));
            }
            c.setAuthor(u);
            c.setNew_(true);
        }
        return c;
    }

    public static ContenuDocAchat buildBeanContenuDocAchat(YvsComContenuDocAchat y) {
        ContenuDocAchat c = new ContenuDocAchat();
        if (y != null) {
            c.setId(y.getId());
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setArticle((y.getArticle() != null) ? buildBeanArticle(y.getArticle()) : new Articles());
            c.setArticleBonus((y.getArticleBonus() != null) ? buildBeanArticle(y.getArticleBonus()) : new Articles());
            c.setQualite(buildBeanQualite(y.getQualite()));
            c.setCommentaire(y.getCommentaire());
            c.setPrixAchat(y.getPrixAchat());
            c.setTaxe(y.getTaxe());
            c.setRemiseRecu(y.getRemise());
            c.setQuantiteCommende(y.getQuantiteCommande());
            c.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            c.setConditionnementBonus(UtilProd.buildBeanConditionnement(y.getConditionnementBonus()));
            c.setQuantiteRecu(y.getQuantiteRecu());
            c.setQuantiteBonus(y.getQuantiteBonus());
            c.setDateLivraison((y.getDateLivraison() != null) ? y.getDateLivraison() : new Date());
            c.setDateContenu((y.getDateContenu() != null) ? y.getDateContenu() : new Date());
            c.setStatut((y.getStatut() != null) ? y.getStatut() : Constantes.ETAT_EDITABLE);
            c.setLot((y.getLot() != null) ? buildBeanLotReception(y.getLot()) : new LotReception());
            c.setPrixTotalAttendu(y.getPrixTotal());
            c.setPrixTotalRecu(y.getPrixTotal());
            c.setCoutAchat(c.getCoutAchat());
            c.setParent(y.getParent() != null ? new ContenuDocAchat(y.getParent().getId()) : new ContenuDocAchat());
            c.setExterne(buildBeanArticleApprovisionnement(y.getExterne()));
            c.setDateSave(Util.currentTimeStamp(y.getDateSave()));
            c.setNumSerie(y.getNumSerie());
            c.setCalculPr(y.getCalculPr());
            c.setDocAchat(buildBeanDocAchat(y.getDocAchat()));
            c.setTaxes(y.getTaxes());
        }
        return c;
    }

    public static List<ContenuDocAchat> buildBeanListContenuDocAchat(List<YvsComContenuDocAchat> list) {
        List<ContenuDocAchat> r = new ArrayList<>();
        if (list != null) {
            for (YvsComContenuDocAchat a : list) {
                r.add(buildBeanContenuDocAchat(a));
            }
        }
        return r;
    }

    public static CoutSupDoc buildBeanCoutSupDocAchat(YvsComCoutSupDocAchat y) {
        CoutSupDoc c = new CoutSupDoc();
        if (y != null) {
            c.setId(y.getId());
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            c.setType(UtilGrh.buildBeanTypeCout(y.getTypeCout()));
            c.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            c.setDoc(y.getDocAchat() != null ? y.getDocAchat().getId() : 0);
            c.setUpdate(true);
        }
        return c;
    }

    public static YvsComCoutSupDocAchat buildCoutSupDocAchat(CoutSupDoc y, YvsUsersAgence currentUser) {
        YvsComCoutSupDocAchat c = new YvsComCoutSupDocAchat();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            c.setSupp(y.isSupp());
            if (y.getType() != null ? y.getType().getId() > 0 : false) {
                c.setTypeCout(new YvsGrhTypeCout(y.getType().getId(), y.getType().getLibelle()));
            }
            c.setMontant(y.getMontant());
            if (y.getDoc() > 0) {
                c.setDocAchat(new YvsComDocAchats(y.getDoc()));
            }
            c.setAuthor(currentUser);
            c.setNew_(true);
        }
        return c;
    }

    public static DocAchat recopieAchat(DocAchat doc, String type) {
        DocAchat d = new DocAchat();
        d.setId(doc.getId());
        d.setActif(doc.isActif());
        d.setSupp(doc.isSupp());
        d.setTypeDoc(type);
        d.setNumDoc(doc.getNumDoc());
        d.setDateDoc((doc.getDateDoc() != null) ? doc.getDateDoc() : new Date());
        d.setDateLivraison(doc.getDateLivraison());
        d.setStatut(doc.getStatut());
        d.setStatutLivre(doc.getStatutLivre());
        d.setStatutRegle(doc.getStatutRegle());
        d.setLegendeType(doc.getLegendeType());
        d.setNumPiece(doc.getNumPiece());
        d.setNameEtat(doc.getNameEtat());
        d.setFichier(doc.getFichier());
        d.setReferenceExterne(doc.getReferenceExterne());
        d.setFournisseur(doc.getFournisseur());
        d.setDepotReception(doc.getDepotReception());
        d.setCategorieComptable(doc.getCategorieComptable());
        d.setModeReglement(doc.getModeReglement());
        d.setTranche(doc.getTranche());
        d.setDocumentLie(doc.getDocumentLie());
        d.setDateSave(doc.getDateSave());
        d.setCloturer(doc.isCloturer());
        d.setDateCloturer(doc.getDateCloturer());
        d.setCloturerBy(doc.getCloturerBy());
        d.setDateAnnuler(doc.getDateAnnuler());
        d.setAnnulerBy(doc.getAnnulerBy());
        d.setDateValider(doc.getDateValider());
        d.setValiderBy(doc.getValiderBy());
        d.setQuantiteTotal(doc.getQuantiteTotal());
        d.setMontantTaxe(doc.getMontantTaxe());
        d.setMontantHT(doc.getMontantHT());
        d.setMontantTTC(doc.getMontantTTC());
        d.setDateUpdate(doc.getDateUpdate());
        d.setUpdate(doc.isUpdate());
        d.setCouts(doc.getCouts());
        d.setContenus(doc.getContenus());
        d.setEtapesValidations(doc.getEtapesValidations());
        d.setEtapeTotal(doc.getEtapeTotal());
        d.setEtapeValide(doc.getEtapeValide());
        return d;
    }

    public static YvsComDocAchats buildDocAchat(DocAchat y, YvsUsersAgence u, YvsAgences ag) {
        YvsComDocAchats d = new YvsComDocAchats();
        if (y != null) {
            d.setDateDoc((y.getDateDoc() != null) ? y.getDateDoc() : new Date());
            if ((y.getFournisseur() != null) ? y.getFournisseur().getId() > 0 : false) {
                d.setFournisseur(buildFournisseur(y.getFournisseur(), u));
            }
            if ((y.getDepotReception() != null) ? y.getDepotReception().getId() > 0 : false) {
                d.setDepotReception(new YvsBaseDepots(y.getDepotReception().getId(), y.getDepotReception().getDesignation()));
            }
            if ((y.getTranche() != null) ? y.getTranche().getId() > 0 : false) {
                d.setTranche(new YvsGrhTrancheHoraire(y.getTranche().getId(), y.getTranche().getTitre(), y.getTranche().getTypeJournee(), y.getTranche().getHeureDebut(), y.getTranche().getHeureFin()));
            }
            if ((y.getCategorieComptable() != null) ? y.getCategorieComptable().getId() > 0 : false) {
                d.setCategorieComptable(new YvsBaseCategorieComptable(y.getCategorieComptable().getId(), y.getCategorieComptable().getCodeCategorie()));
            }
            if ((y.getModeReglement() != null) ? y.getModeReglement().getId() > 0 : false) {
                d.setModelReglement(new YvsBaseModelReglement(y.getModeReglement().getId(), y.getModeReglement().getDesignation()));
            }
            if ((y.getDocumentLie() != null) ? y.getDocumentLie().getId() > 0 : false) {
                d.setDocumentLie(new YvsComDocAchats(y.getDocumentLie().getId(), y.getDocumentLie().getNumDoc(), y.getDocumentLie().getStatut(), y.getDocumentLie().getStatutLivre(), y.getDocumentLie().getStatutRegle()));
            }
            if (y.getTypeAchat() != null ? y.getTypeAchat().getId() > 0 : false) {
                d.setTypeAchat(buildBeanTypeDoc(y.getTypeAchat(), u));
            }
            d.setId(y.getId());
            d.setComptabilise(y.isComptabilise());
            d.setNumPiece(y.getNumPiece());
            d.setNumDoc(y.getNumDoc());
            d.setAutomatique(y.isAutomatique());
            d.setStatut((y.getStatut() != null) ? y.getStatut() : Constantes.ETAT_EDITABLE);
            d.setStatutLivre(y.getStatutLivre());
            d.setStatutRegle(y.getStatutRegle());
            d.setTypeDoc(y.getTypeDoc());
            d.setFichier(y.getFichier());
            d.setReferenceExterne(y.getReferenceExterne());
            d.setDescription((y.getDescription() != null) ? y.getDescription() : "");
            d.setDateCloturer(y.getDateCloturer());
            if ((y.getCloturerBy() != null) ? y.getCloturerBy().getId() > 0 : false) {
                d.setCloturerBy(new YvsUsers(y.getCloturerBy().getId()));
            }
            d.setDateValider(y.getDateValider());
            if ((y.getValiderBy() != null) ? y.getValiderBy().getId() > 0 : false) {
                d.setValiderBy(new YvsUsers(y.getValiderBy().getId()));
            }
            d.setDateAnnuler(y.getDateAnnuler());
            if ((y.getAnnulerBy() != null) ? y.getAnnulerBy().getId() > 0 : false) {
                d.setAnnulerBy(new YvsUsers(y.getAnnulerBy().getId()));
            }
            d.setDateLivraison(y.getDateLivraison());
            d.setDateSolder(y.getDateSolder());
            d.setCloturer(y.isCloturer());
            d.setGenererFactureAuto(y.isGenererFactureAuto());
            d.setDateSave(y.getDateSave());
            d.setDateUpdate(new Date());
            d.setActif(true);
            d.setSupp(false);
            d.setContenus(y.getContenus());
            d.setCouts(y.getCouts());
            d.setAuthor(u);
            d.setAgence(ag);
            d.setLibEtapes(y.getLibEtapes());
            d.setEtapesValidations(y.getEtapesValidations());
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setMontantRemise(y.getMontantRemise());
            d.setMontantTaxe(y.getMontantTaxe());
            d.setMontantHT(y.getMontantHT());
            d.setMontantTTC(y.getMontantTTC());
            d.setMontantCS(y.getMontantCS());
            d.setMontantAvance(y.getMontantAvance());
            d.setMontantTaxeR(y.getMontantTaxeR());
            d.setMontantNetApayer(y.getMontantNetApayer());
            d.setNew_(true);
        }
        return d;
    }

    public static DocAchat buildSimpleBeanDocAchat(YvsComDocAchats y) {
        DocAchat d = new DocAchat();
        if (y != null) {
            d.setActif((y.getActif() != null) ? y.getActif() : false);
            d.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            d.setDateDoc((y.getDateDoc() != null) ? y.getDateDoc() : new Date());
            d.setId(y.getId());
            d.setNumPiece((y.getNumPiece() != null) ? y.getNumPiece() : "");
            d.setNumDoc((y.getNumDoc() != null) ? y.getNumDoc() : "");
            d.setReferenceExterne((y.getReferenceExterne() != null) ? y.getReferenceExterne() : "");
            d.setFichier((y.getFichier() != null) ? y.getFichier() : "");
            d.setDescription((y.getDescription() != null) ? y.getDescription() : "");
            d.setStatut(y.getStatut());
            d.setStatutLivre(y.getStatutLivre());
            d.setStatutRegle(y.getStatutRegle());
            d.setTypeDoc(y.getTypeDoc());
            d.setAutomatique(y.getAutomatique());
            d.setComptabilise(y.getComptabilise());
            d.setDateSave(y.getDateSave());
            d.setCloturer(y.getCloturer());
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setLivraisonDo(y.isLivraisonDo());
            d.setDateAnnuler(y.getDateAnnuler());
            d.setGenererFactureAuto(y.getGenererFactureAuto());
            d.setDateCloturer(y.getDateCloturer());
            d.setDateValider(y.getDateValider());
            d.setDateLivraison(y.getDateLivraison());
            d.setDateSolder(y.getDateSolder());
            d.setTypeAchat(buildBeanTypeDoc(y.getTypeAchat()));
            d.setUpdate(true);
            d.setFournisseur((y.getFournisseur() != null) ? buildBeanFournisseur(y.getFournisseur()) : new Fournisseur());
            d.setCategorieComptable((y.getCategorieComptable() != null) ? buildBeanCategorieComptable(y.getCategorieComptable()) : new CategorieComptable());

        }
        return d;
    }

    public static DocAchat buildBeanDocAchat(YvsComDocAchats y) {
        DocAchat d = buildSimpleBeanDocAchat(y);
        if (y != null && d != null) {
            d.setDepotReception((y.getDepotReception() != null) ? buildBeanDepot(y.getDepotReception()) : new Depots());
            d.setCloturerBy(UtilUsers.buildBeanUsers(y.getCloturerBy()));
            d.setValiderBy(UtilUsers.buildSimpleBeanUsers(y.getValiderBy()));
            d.setAnnulerBy(UtilUsers.buildBeanUsers(y.getAnnulerBy()));
            d.setModeReglement(buildBeanModelReglement(y.getModelReglement()));
            String name = d.getFournisseur().getTiers().getNom_prenom() + " : " + d.getTypeDoc() + "-" + Util.getDateToString_(d.getDateDoc());
            d.setNameDoc(name);
            d.setTranche(y.getTranche() != null ? UtilGrh.buildTrancheHoraire(y.getTranche()) : new TrancheHoraire());
            d.setDocumentLie(y.getDocumentLie() != null ? new DocAchat(y.getDocumentLie().getId(), y.getDocumentLie().getNumDoc(), y.getDocumentLie().getStatut()) : new DocAchat());
            d.setContenus(y.getContenus());
            d.setContenusSave(new ArrayList<>(y.getContenus()));
            d.setEtapesValidations(y.getEtapesValidations());
            d.setReglements(y.getReglements());
            d.setCouts(y.getCouts());
            d.setLibEtapes(y.getLibEtapes());
            d.setDocuments(y.getDocuments());
            d.setMontantRemise(y.getMontantRemise());
            d.setMontantTaxe(y.getMontantTaxe());
            d.setMontantHT(y.getMontantHT());
            d.setMontantTTC(y.getMontantTTC());
            d.setMontantCS(y.getMontantCS());
            d.setMontantAvance(y.getMontantAvance());
            d.setMontantTaxeR(y.getMontantTaxeR());
            d.setMontantNetApayer(y.getMontantNetApayer());
            if (y.getEtapesValidations() != null ? !y.getEtapesValidations().isEmpty() : false) {
                d.setFirstEtape(y.getEtapesValidations().get(0).getEtape().getLabelStatut());
            }
        }
        return d;
    }

    public static ArticleFourniAchat buildBeanArticleFourniAchat(YvsComArticleFourniAchat y) {
        ArticleFourniAchat a = new ArticleFourniAchat();
        if (y != null) {
            a.setId(y.getId());
            a.setDateLivraison((y.getDateLivraison() != null) ? y.getDateLivraison() : new Date());
            a.setEtat((y.getEtat() != null) ? y.getEtat() : Constantes.ETAT_EDITABLE);
            a.setPua((y.getPua() != null) ? y.getPua() : 0);
            a.setRemise((y.getRemise() != null) ? y.getRemise() : 0);
            a.setQuantite((y.getQuantite() != null) ? y.getQuantite() : 0);
            a.setFournisseur((y.getFournisseur() != null) ? buildBeanFournisseur(y.getFournisseur()) : new Fournisseur());
            a.setArticle((y.getArticle() != null) ? buildBeanArticleApprovisionnement(y.getArticle()) : new ArticleApprovisionnement());
        }
        return a;
    }

    public static YvsComArticleFourniAchat buildArticleFourniAchat(ArticleFourniAchat y, YvsUsersAgence currentUser) {
        YvsComArticleFourniAchat a = new YvsComArticleFourniAchat();
        if (y != null) {
            a.setId(y.getId());
            a.setDateLivraison((y.getDateLivraison() != null) ? y.getDateLivraison() : new Date());
            a.setEtat((y.getEtat() != null) ? y.getEtat() : Constantes.ETAT_EDITABLE);
            a.setPua(y.getPua());
            a.setRemise(y.getRemise());
            a.setQuantite(y.getQuantite());
            if ((y.getFournisseur() != null) ? y.getFournisseur().getId() > 0 : false) {
                a.setFournisseur(new YvsBaseFournisseur(y.getFournisseur().getId()));
            }
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                a.setArticle(new YvsComArticleApprovisionnement(y.getArticle().getId()));
            }
            a.setAuthor(currentUser);
        }
        return a;
    }

    public static List<ArticleFourniAchat> buildBeanListArticleFourniAchat(List<YvsComArticleFourniAchat> list) {
        List<ArticleFourniAchat> r = new ArrayList<>();
        if (list != null) {
            for (YvsComArticleFourniAchat a : list) {
                r.add(buildBeanArticleFourniAchat(a));
            }
        }
        return r;
    }

    public static ArticleApprovisionnement buildBeanArticleApprovisionnement(YvsComArticleApprovisionnement y) {
        ArticleApprovisionnement a = new ArticleApprovisionnement();
        if (y != null) {
            a.setArticle((y.getArticle() != null) ? buildBeanArticleDepot(y.getArticle()) : new ArticleDepot());
            a.setDateLivraison((y.getDateLivraison() != null) ? y.getDateLivraison() : new Date());
            a.setId(y.getId());
            a.setQuantite(y.getQuantite());
            a.setQuantiteRest(y.getQuantite());
            a.setStock(y.getStock());
            a.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            a.setUpdate(true);
            a.setDateSave(y.getDateSave());
        }
        return a;
    }

    public static YvsComArticleApprovisionnement buildArticleApprovisionnement(ArticleApprovisionnement y, YvsUsersAgence currentUser) {
        YvsComArticleApprovisionnement a = new YvsComArticleApprovisionnement();
        if (y != null) {
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                a.setArticle(new YvsBaseArticleDepot(y.getArticle().getId(), new YvsBaseArticles(y.getArticle().getArticle().getId(), y.getArticle().getArticle().getRefArt(), y.getArticle().getArticle().getDesignation())));
            }
            a.setDateLivraison((y.getDateLivraison() != null) ? y.getDateLivraison() : new Date());
            a.setId(y.getId());
            a.setQuantite(y.getQuantite());
            a.setConditionnement(UtilProd.buildConditionnement(y.getConditionnement(), currentUser));
            if ((y.getFiche() != null) ? y.getFiche().getId() > 0 : false) {
                a.setFiche(new YvsComFicheApprovisionnement(y.getFiche().getId()));
            }
            a.setAuthor(currentUser);
        }
        return a;
    }

    public static List<ArticleApprovisionnement> buildBeanListArticleApprovisionnement(List<YvsComArticleApprovisionnement> list) {
        List<ArticleApprovisionnement> r = new ArrayList<>();
        if (list != null) {
            for (YvsComArticleApprovisionnement a : list) {
                r.add(buildBeanArticleApprovisionnement(a));
            }
        }
        return r;
    }

    public static FicheApprovisionnement buildBeanFicheApprovisionnement(YvsComFicheApprovisionnement y) {
        FicheApprovisionnement f = new FicheApprovisionnement();
        if (y != null) {
            f.setId(y.getId());
            f.setDateApprovisionnement((y.getDateApprovisionnement() != null) ? y.getDateApprovisionnement() : new Date());
            f.setHeureApprovisionnement((y.getHeureApprovisionnement() != null) ? y.getHeureApprovisionnement() : new Date());
            f.setDepot((y.getDepot() != null) ? buildSimpleBeanDepot(y.getDepot()) : new Depots());
            f.setEtat((y.getEtat() != null) ? y.getEtat() : Constantes.ETAT_EDITABLE);
            f.setStatutTerminer(y.getStatutTerminer());
            f.setCreneau((y.getCreneau() != null) ? buildBeanCreneau(y.getCreneau()) : new Creneau());
            f.setReference(y.getReference());
            f.setCloturer(y.getCloturer());
            f.setAuto(y.getAuto());
            f.setDateSave(y.getDateSave());
            f.setDateUpdate(y.getDateUpdate());
            f.setArticles(y.getArticles());
            f.setEtapeTotal(y.getEtapeTotal());
            f.setEtapeValide(y.getEtapeValide());
            f.setEtapesValidations(y.getEtapesValidations());
            if (y.getEtapesValidations() != null ? !y.getEtapesValidations().isEmpty() : false) {
                f.setFirstEtape(y.getEtapesValidations().get(0).getEtape().getLabelStatut());
            }
            f.setUpdate(true);
        }
        return f;
    }

    public static YvsComFicheApprovisionnement buildFicheApprovisionnement(FicheApprovisionnement y, YvsUsersAgence currentUser) {
        YvsComFicheApprovisionnement b = new YvsComFicheApprovisionnement();
        if (y != null) {
            b.setDateApprovisionnement((y.getDateApprovisionnement() != null) ? y.getDateApprovisionnement() : new Date());
            b.setHeureApprovisionnement((y.getHeureApprovisionnement() != null) ? y.getHeureApprovisionnement() : new Date());
            if ((y.getDepot() != null) ? y.getDepot().getId() > 0 : false) {
                b.setDepot(new YvsBaseDepots(y.getDepot().getId(), y.getDepot().getDesignation()));
            }
            if ((y.getCreneau() != null) ? y.getCreneau().getId() > 0 : false) {
                b.setCreneau(new YvsComCreneauDepot(y.getCreneau().getId(), new YvsJoursOuvres(y.getCreneau().getJour().getId(), y.getCreneau().getJour().getJour()), new YvsGrhTrancheHoraire(y.getCreneau().getTranche().getId(), y.getCreneau().getTranche().getHeureDebut(), y.getCreneau().getTranche().getHeureFin())));
            }
            b.setAuto(y.isAuto());
            b.setId(y.getId());
            b.setEtat(y.getEtat());
            b.setStatutTerminer(y.getStatutTerminer());
            b.setReference(y.getReference());
            b.setDateSave(y.getDateSave());
            b.setDateUpdate(new Date());
            b.setEtapeTotal(y.getEtapeTotal());
            b.setEtapeValide(y.getEtapeValide());
            b.setAuthor(currentUser);
        }
        return b;
    }

    public static List<FicheApprovisionnement> buildBeanListFicheApprovisionnement(List<YvsComFicheApprovisionnement> list) {
        List<FicheApprovisionnement> r = new ArrayList<>();
        if (list != null) {
            for (YvsComFicheApprovisionnement a : list) {
                r.add(buildBeanFicheApprovisionnement(a));
            }
        }
        return r;
    }
    /*
     *
     *FIN GESTION FICHE APPROVISIONNEMENT
     *
     */

    /*
     *
     *DEBUT GESTION RECEPTION CONTENU DOC ACHAT
     *
     */
    public static YvsComCritereLot buildCritereLot(CritereLot y, YvsUsersAgence ua, YvsSocietes ste) {
        YvsComCritereLot c = new YvsComCritereLot();
        if (y != null) {
            c.setActif(y.isActif());
            c.setDescription(y.getDescription());
            c.setId(y.getId());
            c.setValeur(y.getValeur());
            c.setComparable(y.isComparable());
            c.setAuthor(ua);
            c.setSociete(ste);
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
        }
        return c;
    }

    public static CritereLot buildBeanCritereLot(YvsComCritereLot y) {
        CritereLot c = new CritereLot();
        if (y != null) {
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setDescription(y.getDescription());
            c.setId(y.getId());
            c.setValeur(y.getValeur());
            c.setComparable(y.getComparable());
            c.setDateSave(y.getDateSave());
        }
        return c;
    }

    static int lot = 100;

    public static YvsComLotReception buildLotReception(LotReception y, YvsAgences agence, YvsUsersAgence currentUser) {
        YvsComLotReception l = new YvsComLotReception();
        if (y != null) {
            l.setId(y.getId());
            l.setNumero(y.getNumero());
            l.setStatut(y.getStatut());
            l.setActif(y.isActif());
            l.setDateFabrication((y.getDateFabrication() != null) ? y.getDateFabrication() : new Date());
            l.setDateExpiration((y.getDateExpiration() != null) ? y.getDateExpiration() : new Date());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                l.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            l.setAuthor(currentUser);
            l.setAgence(agence);
            l.setDateSave(y.getDateSave());
            l.setDateUpdate(new Date());
            l.setNew_(true);
            lot--;
        }
        return l;
    }

    public static LotReception buildBeanLotReception(YvsComLotReception y) {
        LotReception l = new LotReception();
        if (y != null) {
            l.setId(y.getId());
            l.setNumero(y.getNumero());
            l.setStatut(y.getStatut());
            l.setActif(y.getActif());
            l.setDateFabrication((y.getDateFabrication() != null) ? y.getDateFabrication() : new Date());
            l.setDateExpiration((y.getDateExpiration() != null) ? y.getDateExpiration() : new Date());
            l.setArticle(y.getArticle() != null ? new Articles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()) : new Articles());
            l.setDateSave(y.getDateSave());
        }
        return l;
    }

    public static List<LotReception> buildBeanListLotReception(List<YvsComLotReception> list) {
        List<LotReception> r = new ArrayList<>();
        if (list != null) {
            for (YvsComLotReception a : list) {
                r.add(buildBeanLotReception(a));
            }
        }
        return r;
    }

    /*
     *
     *FIN GESTION RECEPTION CONTENU DOC ACHAT
     *
     */

    /*
     *
     *DEBUT GESTION REGLEMENT FACTURE ACHAT
     *
     */
    public static MensualiteFactureAchat buildBeanMensualiteFactureAchat(YvsComMensualiteFactureAchat y) {
        MensualiteFactureAchat r = new MensualiteFactureAchat();
        if (y != null) {
            r.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
            r.setEtat((y.getEtat() != null) ? y.getEtat() : Constantes.ETAT_EDITABLE);
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setFacture(new DocAchat(y.getFacture().getId(), y.getFacture().getNumDoc(), y.getFacture().getStatut()));
            r.setMontantRest(r.getMontant());
            r.setOutDelai(y.isOutDelai());
            r.setNameMens(y.getNameMens());
            r.setUpdate(true);
        }
        return r;
    }

    public static MensualiteFactureAchat buildBeanMensualiteFactureAchat(YvsComMensualiteFactureAchat y, DaoInterfaceLocal dao) {
        MensualiteFactureAchat r = new MensualiteFactureAchat();
        if (y != null) {
            r.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
            r.setEtat((y.getEtat() != null) ? y.getEtat() : Constantes.ETAT_EDITABLE);
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setFacture(new DocAchat(y.getFacture().getId(), y.getFacture().getNumDoc()));
            List<YvsComptaCaissePieceDivers> l = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByTableIdExterne",
                    new String[]{"tableExterne", "idExterne"},
                    new Object[]{Constantes.yvs_com_mensualite_facture_achat, y.getId()});
            r.setReglements((l != null)
                    ? buildBeanListPieceTresorerie(l, Constantes.SCR_ACHAT)
                    : new ArrayList<PieceTresorerie>());
            r.setMontantRest(r.getMontant());
            Collections.sort(r.getReglements(), Collections.reverseOrder());
            for (PieceTresorerie m : r.getReglements()) {
                r.setMontantRest(r.getMontantRest() - m.getMontant());
            }
            r.setMontantRest((r.getMontantRest() > 0) ? r.getMontantRest() : 0);

            r.setOutDelai(Util.dateToCalendar(r.getDateMensualite()).before(Util.dateToCalendar(new Date())));
            r.setNameMens(Util.getDateToString(r.getDateMensualite()) + " : " + Util.getDouble(r.getMontant()));
            r.setUpdate(true);
        }
        return r;
    }

    public static List<MensualiteFactureAchat> buildBeanListMensualiteFactureAchat(List<YvsComMensualiteFactureAchat> list, DaoInterfaceLocal dao) {
        List<MensualiteFactureAchat> r = new ArrayList<>();
        if (list != null) {
            for (YvsComMensualiteFactureAchat a : list) {
                r.add(buildBeanMensualiteFactureAchat(a, dao));
            }
        }
        return r;
    }

    /*
     *
     *FIN GESTION REGLEMENT FACTURE ACHAT
     *
     */
    /*
     *
     *DEBUT GESTION TAXES
     *
     */
    public static Taxes buildBeanTaxes(YvsBaseTaxes y) {
        return UtilCompta.buildBeanTaxes(y);
    }

    public static YvsBaseTaxes buildTaxes(Taxes y, YvsSocietes s, YvsUsersAgence ua) {
        return UtilCompta.buildTaxes(y, s, ua);
    }

    public static ArticlesCatComptable buildBeanArticleCategorie(YvsBaseArticleCategorieComptable y) {
        ArticlesCatComptable a = new ArticlesCatComptable();
        if (y != null) {
            a.setActif((y.getActif() != null) ? y.getActif() : false);
            a.setArticle((y.getArticle() != null) ? buildBeanArticle(y.getArticle()) : new Articles());
            a.setTemplate((y.getTemplate() != null) ? UtilProd.buildBeanTemplateArticles(y.getTemplate()) : new TemplateArticles());
            a.setCategorie((y.getCategorie() != null) ? new CategorieComptable(y.getCategorie().getId()) : new CategorieComptable());
            a.setCompte((y.getCompte() != null) ? buildBeanCompte(y.getCompte()) : new Comptes());
            a.setId(y.getId());
            a.setUpdate(true);
            a.setTaxes(y.getTaxes());
        }
        return a;
    }

    public static YvsBaseArticleCategorieComptable buildArticleCategorie(ArticlesCatComptable cat, YvsUsersAgence ua) {
        YvsBaseArticleCategorieComptable re = new YvsBaseArticleCategorieComptable();
        if (cat != null) {
            re.setId(cat.getId());
            re.setActif(cat.isActif());
            if (cat.getTemplate() != null ? cat.getTemplate().getId() > 0 : false) {
                re.setTemplate(new YvsBaseArticlesTemplate(cat.getTemplate().getId()));
            }
            if (cat.getArticle() != null ? cat.getArticle().getId() > 0 : false) {
                re.setArticle(new YvsBaseArticles(cat.getArticle().getId(), cat.getArticle().getRefArt(), cat.getArticle().getDesignation()));
            }
            if (cat.getCategorie() != null ? cat.getCategorie().getId() > 0 : false) {
                re.setCategorie(new YvsBaseCategorieComptable(cat.getCategorie().getId(), cat.getCategorie().getCodeCategorie()));
            }
            if (cat.getCompte() != null ? cat.getCompte().getId() > 0 : false) {
                re.setCompte(new YvsBasePlanComptable(cat.getCompte().getId(), cat.getCompte().getNumCompte()));
            }
            re.setTaxes(cat.getTaxes());
            re.setAuthor(ua);
            re.setNew_(true);
        }
        return re;
    }

    public static List<ArticlesCatComptable> buildBeanListArticleCategorie(List<YvsBaseArticleCategorieComptable> list) {
        List<ArticlesCatComptable> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseArticleCategorieComptable a : list) {
                r.add(buildBeanArticleCategorie(a));
            }
        }
        return r;
    }

    public static ArticleTaxe buildBeanTaxeArticle(YvsBaseArticleCategorieComptableTaxe y) {
        ArticleTaxe t = new ArticleTaxe();
        if (y != null) {
            t.setActif((y.getActif() != null) ? y.getActif() : false);
            t.setAppRemise((y.getAppRemise() != null) ? y.getAppRemise() : false);
            t.setId(y.getId());
            t.setUpdate(true);
            t.setTaxe((y.getTaxe() != null) ? buildBeanTaxes(y.getTaxe()) : new Taxes());
            t.setArticle((y.getArticleCategorie() != null) ? new ArticlesCatComptable(y.getArticleCategorie().getId()) : new ArticlesCatComptable());
        }
        return t;
    }

    public static List<ArticleTaxe> buildBeanListTaxeArticle(List<YvsBaseArticleCategorieComptableTaxe> list) {
        List<ArticleTaxe> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseArticleCategorieComptableTaxe a : list) {
                r.add(buildBeanTaxeArticle(a));
            }
        }
        return r;
    }

    public static CategorieComptable buildBeanCategorieComptable(YvsBaseCategorieComptable y) {
        CategorieComptable c = new CategorieComptable();
        if (y != null) {
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setCodeCategorie(y.getCode());
            c.setCodeAppel(y.getCodeAppel());
            c.setId(y.getId());
            c.setNature(y.getNature());
            c.setDesignation(y.getDesignation());
            c.setDateSave(y.getDateSave());
            c.setVenteOnline(y.getVenteOnline());
            c.setUpdate(true);
        }
        return c;
    }

    public static YvsBaseCategorieComptable buildCategorieComptable(CategorieComptable y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        YvsBaseCategorieComptable c = new YvsBaseCategorieComptable();
        if (y != null) {
            c.setActif(y.isActif());
            c.setCode(y.getCodeCategorie());
            c.setCodeAppel(y.getCodeAppel());
            c.setId(y.getId());
            c.setDesignation(y.getDesignation());
            c.setNature(y.getNature());
            c.setDateSave(y.getDateSave());
            c.setVenteOnline(y.isVenteOnline());
            c.setDateUpdate(new Date());
            c.setSociete(currentScte);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                c.setAuthor(currentUser);
            }
            c.setNew_(true);
        }
        return c;
    }

    /*
     *
     *FIN GESTION TAXES
     *
     */

    /*
     *
     *DEBUT GESTION TRANSFERT
     *
     */
    public static YvsComContenuDocStock buildContenuDocStock(ContenuDocStock y, YvsUsersAgence a) {
        YvsComContenuDocStock c = new YvsComContenuDocStock();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            c.setSupp(y.isSupp());
            c.setPrix(y.getPrix());
            c.setPrixEntree(y.getPrixEntree());
            c.setQuantite(y.getQuantite());
            c.setQuantiteEntree(y.getResultante());
            c.setQteAttente(y.getQteAttente());
            c.setCommentaire(y.getCommentaire());
            c.setNumSerie(y.getNumSerie());
            c.setStatut(y.getStatut());
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                c.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
                c.getArticle().setConditionnements(y.getArticle().getConditionnements());
            }
            if ((y.getConditionnement() != null) ? y.getConditionnement().getId() > 0 : false) {
                c.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            if ((y.getUniteDestination() != null) ? y.getUniteDestination().getId() > 0 : false) {
                c.setConditionnementEntree(new YvsBaseConditionnement(y.getUniteDestination().getId(), new YvsBaseUniteMesure(y.getUniteDestination().getUnite().getId(), y.getUniteDestination().getUnite().getReference(), y.getUniteDestination().getUnite().getLibelle())));
            }
            if ((y.getQualite() != null) ? y.getQualite().getId() > 0 : false) {
                c.setQualite(new YvsComQualite(y.getQualite().getId(), y.getQualite().getCode(), y.getQualite().getLibelle()));
            }
            if ((y.getQualiteEntree() != null) ? y.getQualiteEntree().getId() > 0 : false) {
                c.setQualiteEntree(new YvsComQualite(y.getQualiteEntree().getId(), y.getQualiteEntree().getCode(), y.getQualiteEntree().getLibelle()));
            }
            if ((y.getDocStock() != null) ? y.getDocStock().getId() > 0 : false) {
                c.setDocStock(buildDocStock(y.getDocStock(), a.getAgence().getSociete(), a));
            }
            if (y.getParent() != null ? y.getParent().getId() > 0 : false) {
                c.setParent(new YvsComContenuDocStock(y.getParent().getId()));
            }
            if ((y.getLotSortie() != null) ? y.getLotSortie().getId() > 0 : false) {
                c.setLotSortie(buildLotReception(y.getLotSortie(), a.getAgence(), a));
            }
            if ((y.getLotEntree() != null) ? y.getLotEntree().getId() > 0 : false) {
                c.setLotEntree(buildLotReception(y.getLotEntree(), a.getAgence(), a));
            }
            c.setDateSave(y.getDateSave());
            c.setCalculPr(y.isCalculPr());
            c.setDateUpdate(new Date());
            c.setDateContenu((y.getDateContenu() != null) ? y.getDateContenu() : new Date());
            c.setDateReception(y.getDateReception());
            c.setImpactValeurInventaire(y.isImpactValeurInventaire());
            if (a != null ? a.getId() > 0 : false) {
                c.setAuthor(a);
            }
            c.setNew_(true);
        }
        return c;
    }

    public static ContenuDocStock buildBeanContenuDocStock(YvsComContenuDocStock y) {
        ContenuDocStock c = new ContenuDocStock();
        if (y != null) {
            c.setId(y.getId());
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            c.setDateContenu((y.getDateContenu() != null) ? y.getDateContenu() : new Date());
            c.setArticle((y.getArticle() != null) ? buildBeanArticle(y.getArticle()) : new Articles());
            c.setQualite(buildBeanQualite(y.getQualite()));
            c.setQualiteEntree(buildBeanQualite(y.getQualiteEntree()));
            c.setResultante(y.getQuantiteEntree());
            c.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            c.setUniteDestination(UtilProd.buildBeanConditionnement(y.getConditionnementEntree()));
            c.setLotSortie((y.getLotSortie() != null) ? buildBeanLotReception(y.getLotSortie()) : new LotReception());
            c.setLotEntree((y.getLotEntree() != null) ? buildBeanLotReception(y.getLotEntree()) : new LotReception());
            c.setPrix((y.getPrix() != null) ? y.getPrix() : 0);
            c.setPrixEntree((y.getPrixEntree() != null) ? y.getPrixEntree() : 0);
            c.setQuantite((y.getQuantite() != null) ? y.getQuantite() : 0);
            c.setQteAttente((y.getQteAttente() != null) ? y.getQteAttente() : 0);
            c.setPrixTotal(c.getPrix() * c.getQuantite());
            c.setParent((y.getParent() != null) ? new ContenuDocStock(y.getParent().getId()) : new ContenuDocStock());
            c.setCommentaire(y.getCommentaire());
            c.setCalculPr(y.getCalculPr());
            c.setNumSerie(y.getNumSerie());
            c.setStatut(y.getStatut());
            c.setDateSave(y.getDateSave());
            c.setDateReception(y.getDateReception());
            c.setImpactValeurInventaire(y.getImpactValeurInventaire());
            c.setUpdate(true);
        }
        return c;
    }

    public static ContenuDocStock buildBeanReceptionDocStock(YvsComContenuDocStockReception y) {
        ContenuDocStock c = new ContenuDocStock();
        if (y != null) {
            c.setId(y.getId());
            c.setQuantite((y.getQuantite() != null) ? y.getQuantite() : 0);
            c.setDateSave(y.getDateSave());
            c.setDateReception(y.getDateReception());
            c.setParent((y.getContenu() != null) ? new ContenuDocStock(y.getContenu().getId()) : new ContenuDocStock());
            c.setCalculPr(y.getCalculPr());
        }
        return c;
    }

    public static YvsComContenuDocStockReception buildReceptionDocStock(ContenuDocStock y, YvsUsersAgence ua) {
        YvsComContenuDocStockReception c = new YvsComContenuDocStockReception();
        if (y != null) {
            c.setId(y.getId());
            c.setQuantite(y.getQuantite());
            c.setDateSave(y.getDateSave());
            c.setDateReception(y.getDateReception());
            if (y.getParent() != null ? y.getParent().getId() > 0 : false) {
                c.setContenu(new YvsComContenuDocStock(y.getParent().getId()));
            }
            c.setCalculPr(y.isCalculPr());
            c.setDateUpdate(new Date());
            c.setNew_(true);
            c.setAuthor(ua);
        }
        return c;
    }

    public static List<ContenuDocStock> buildBeanListContenuDocStock(List<YvsComContenuDocStock> list) {
        List<ContenuDocStock> r = new ArrayList<>();
        if (list != null) {
            for (YvsComContenuDocStock a : list) {
                r.add(buildBeanContenuDocStock(a));
            }
        }
        return r;
    }

    public static CoutSupDoc buildBeanCoutSupDocStock(YvsComCoutSupDocStock y) {
        CoutSupDoc c = new CoutSupDoc();
        if (y != null) {
            c.setId(y.getId());
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            c.setType(UtilGrh.buildBeanTypeCout(y.getTypeCout()));
            c.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            c.setDoc(y.getDocStock() != null ? y.getDocStock().getId() : 0);
            c.setUpdate(true);
        }
        return c;
    }

    public static List<CoutSupDoc> buildBeanListCoutSupDocStock(List<YvsComCoutSupDocStock> list) {
        List<CoutSupDoc> r = new ArrayList<>();
        if (list != null) {
            for (YvsComCoutSupDocStock a : list) {
                r.add(buildBeanCoutSupDocStock(a));
            }
        }
        return r;
    }

    public static DocStockValeur buildBeanDocStockValeur(YvsComDocStocksValeur y) {
        DocStockValeur r = new DocStockValeur();
        if (y != null) {
            r.setId(y.getId());
            r.setCoefficient(y.getCoefficient());
            r.setDateSave(y.getDateSave());
            r.setMontant(y.getMontant());
            r.setValoriseMpBy(y.getValoriseMpBy());
            r.setValoriseMsBy(y.getValoriseMsBy());
            r.setValorisePfBy(y.getValorisePfBy());
            r.setValorisePfsBy(y.getValorisePfsBy());
        }
        return r;
    }

    public static YvsComDocStocksValeur buildDocStockValeur(DocStockValeur y, YvsUsersAgence u) {
        YvsComDocStocksValeur r = new YvsComDocStocksValeur();
        if (y != null) {
            r.setId(y.getId());
            r.setCoefficient(y.getCoefficient());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setMontant(y.getMontant());
            r.setValoriseMpBy(y.getValoriseMpBy());
            r.setValoriseMsBy(y.getValoriseMsBy());
            r.setValorisePfBy(y.getValorisePfBy());
            r.setValorisePfsBy(y.getValorisePfsBy());
            r.setAuthor(u);
        }
        return r;
    }

    public static YvsComDocStocks buildDocStock(DocStock y, YvsSocietes s, YvsUsersAgence u) {
        YvsComDocStocks d = new YvsComDocStocks();
        if (y != null) {
            d.setId(y.getId());
            d.setNumDoc(y.getNumDoc());
            d.setTypeDoc(y.getTypeDoc());
            d.setNumPiece(y.getNumPiece());
            d.setDateDoc((y.getDateDoc() != null) ? y.getDateDoc() : new Date());
            d.setDateReception((y.getDateReception() != null) ? y.getDateReception() : new Date());
            d.setStatut((y.getStatut() != null) ? y.getStatut() : Constantes.ETAT_EDITABLE);
            d.setActif(y.isActif());
            d.setAutomatique(y.isAutomatique());
            d.setSupp(y.isSupp());
            d.setHeureDoc(y.getDateSave());
            if ((y.getCategorieComptable() != null) ? y.getCategorieComptable().getId() > 0 : false) {
                d.setCategorieComptable(new YvsBaseCategorieComptable(y.getCategorieComptable().getId(), y.getCategorieComptable().getCodeCategorie()));
            }
            if ((y.getDestination() != null) ? y.getDestination().getId() > 0 : false) {
                d.setDestination(buildDepot(y.getDestination(), u, null));
            }
            if ((y.getSource() != null) ? y.getSource().getId() > 0 : false) {
                d.setSource(buildDepot(y.getSource(), u, null));
            }
            if ((y.getCreneauDestinataire() != null) ? y.getCreneauDestinataire().getId() > 0 : false) {
                d.setCreneauDestinataire(new YvsComCreneauDepot(y.getCreneauDestinataire().getId(), new YvsGrhTrancheHoraire(y.getCreneauDestinataire().getTranche().getId(), y.getCreneauDestinataire().getTranche().getTitre(), y.getCreneauDestinataire().getTranche().getHeureDebut(), y.getCreneauDestinataire().getTranche().getHeureFin()), d.getDestination()));
            }
            if ((y.getCreneauSource() != null) ? y.getCreneauSource().getId() > 0 : false) {
                d.setCreneauSource(new YvsComCreneauDepot(y.getCreneauSource().getId(), new YvsGrhTrancheHoraire(y.getCreneauSource().getTranche().getId(), y.getCreneauSource().getTranche().getTitre(), y.getCreneauSource().getTranche().getHeureDebut(), y.getCreneauSource().getTranche().getHeureFin()), d.getSource()));
            }
            if ((y.getDocumentLie() != null) ? y.getDocumentLie().getId() > 0 : false) {
                d.setDocumentLie(new YvsComDocStocks(y.getDocumentLie().getId(), y.getDocumentLie().getNumDoc(), y.getDocumentLie().getStatut()));
            }
            if (y.getNatureDoc() != null ? y.getNatureDoc().getId() > 0 : false) {
                d.setNatureDoc(new YvsComNatureDoc(y.getNatureDoc().getId(), y.getNatureDoc().getNature()));
            }
            d.setDescription(y.getDescription());
            d.setNature(y.getNature());
            d.setDateUpdate(new Date());
            d.setDateSave(y.getDateSave());
            d.setSociete(s);
            if (u != null ? u.getId() > 0 : false) {
                d.setAuthor(u);
            }
            d.setCloturer(y.isCloturer());
            d.setDateCloturer(y.getDateCloturer());
            if ((y.getCloturerBy() != null) ? y.getCloturerBy().getId() > 0 : false) {
                d.setCloturerBy(new YvsUsers(y.getCloturerBy().getId()));
            }
            d.setDateValider(y.getDateValider());
            if ((y.getValiderBy() != null) ? y.getValiderBy().getId() > 0 : false) {
                d.setValiderBy(new YvsUsers(y.getValiderBy().getId()));
            }
            d.setDateAnnuler(y.getDateAnnuler());
            if ((y.getAnnulerBy() != null) ? y.getAnnulerBy().getId() > 0 : false) {
                d.setAnnulerBy(new YvsUsers(y.getAnnulerBy().getId()));
            }
            if ((y.getEditeur() != null) ? y.getEditeur().getId() > 0 : false) {
                d.setEditeur(new YvsUsers(y.getEditeur().getId(), y.getEditeur().getNomUsers()));
            }
            d.setDateSave(y.getDateSave());
            d.setContenus(y.getContenus());
            d.setCouts(y.getCouts());
            d.setAutomatique(false);
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setTauxEcartInventaire(y.getTauxEcartInventaire());
            d.setNew_(true);
        }
        return d;
    }

    public static DocStock buildSimpleBeanDocStock(YvsComDocStocks y) {
        DocStock d = new DocStock();
        if (y != null) {
            d.setId(y.getId());
            d.setActif((y.getActif() != null) ? y.getActif() : false);
            d.setAutomatique(y.getAutomatique());
            d.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            d.setCreneauDestinataire((y.getCreneauDestinataire() != null) ? buildBeanCreneau(y.getCreneauDestinataire()) : new Creneau());
            d.setCreneauSource((y.getCreneauSource() != null) ? buildBeanCreneau(y.getCreneauSource()) : new Creneau());
            d.setDateDoc((y.getDateDoc() != null) ? y.getDateDoc() : new Date());
            d.setDateReception((y.getDateReception() != null) ? y.getDateReception() : new Date());
            d.setStatut((y.getStatut() != null) ? y.getStatut() : Constantes.ETAT_EDITABLE);
            if (d.getDateReception().before(d.getDateDoc()) && !d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                d.setDateReception(d.getDateDoc());
            }
            d.setDestination((y.getDestination() != null) ? buildSimpleBeanDepot(y.getDestination()) : new Depots());
            d.setSource((y.getSource() != null) ? buildSimpleBeanDepot(y.getSource()) : new Depots());
            d.setNumPiece(y.getNumPiece());
            d.setNumDoc(y.getNumDoc());
            d.setTypeDoc(y.getTypeDoc());
            d.setDescription(y.getDescription());
            d.setMouvement((d.getTypeDoc().equals(Constantes.TYPE_SS)) ? Constantes.MOUV_SORTIE : Constantes.MOUV_ENTREE);
            d.setDateSave(y.getDateSave());
            d.setCloturer(y.getCloturer());
            d.setDateCloturer(y.getDateCloturer());
            d.setCloturerBy(UtilUsers.buildSimpleBeanUsers(y.getCloturerBy()));
            d.setDateValider(y.getDateValider());
            d.setDateAnnuler(y.getDateAnnuler());
            d.setNature(y.getNature());
            d.setEditeur(UtilUsers.buildSimpleBeanUsers(y.getEditeur()));
            d.setDocumentLie((y.getDocumentLie() != null) ? new DocStock(y.getDocumentLie().getId(), y.getDocumentLie().getNumDoc(), y.getDocumentLie().getStatut()) : new DocStock());
            d.setEtapesValidations(y.getEtapesValidations());
            d.setAuthor((y.getAuthor() != null) ? y.getAuthor().getUsers().getNomUsers() : null);
            d.setDateUpdate(y.getDateUpdate());
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setTauxEcartInventaire(y.getTauxEcartInventaire());
            d.setUpdate(true);
        }
        return d;
    }

    public static DocStock buildBeanDocStock(YvsComDocStocks y) {
        DocStock d = new DocStock();
        if (y != null) {
            d.setId(y.getId());
            d.setActif((y.getActif() != null) ? y.getActif() : false);
            d.setAutomatique(y.getAutomatique());
            d.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            d.setContenus(y.getContenus());
            if (y.getContenus() != null) {
                d.setContenusSave(new ArrayList<>(y.getContenus()));
                for (YvsComContenuDocStock c : y.getContenus()) {
                    d.setMontantTotal(d.getMontantTotal() + c.getPrixTotal());
                }
            }
            if (y.getCouts() != null) {
                d.setCouts(y.getCouts());
                for (YvsComCoutSupDocStock c : y.getCouts()) {
                    d.setMontantTotalCS(d.getMontantTotalCS() + c.getMontant());
                }
            }
            d.setCreneauDestinataire((y.getCreneauDestinataire() != null) ? buildBeanCreneau(y.getCreneauDestinataire()) : new Creneau());
            d.setCreneauSource((y.getCreneauSource() != null) ? buildBeanCreneau(y.getCreneauSource()) : new Creneau());
            d.setDateDoc((y.getDateDoc() != null) ? y.getDateDoc() : new Date());
            d.setDateReception((y.getDateReception() != null) ? y.getDateReception() : new Date());
            d.setStatut((y.getStatut() != null) ? y.getStatut() : Constantes.ETAT_EDITABLE);
            if (d.getDateReception().before(d.getDateDoc()) && !d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                d.setDateReception(d.getDateDoc());
            }
            d.setDestination((y.getDestination() != null) ? buildSimpleBeanDepot(y.getDestination()) : new Depots());
            d.setSource((y.getSource() != null) ? buildSimpleBeanDepot(y.getSource()) : new Depots());
            d.setNumPiece(y.getNumPiece());
            d.setNumDoc(y.getNumDoc());
            d.setTypeDoc(y.getTypeDoc());
            d.setDescription(y.getDescription());
            d.setMouvement((d.getTypeDoc().equals(Constantes.TYPE_SS)) ? Constantes.MOUV_SORTIE : Constantes.MOUV_ENTREE);
            d.setDateSave(y.getDateSave());
            d.setCloturer(y.getCloturer());
            d.setDateCloturer(y.getDateCloturer());
            d.setCloturerBy(UtilUsers.buildSimpleBeanUsers(y.getCloturerBy()));
            d.setDateValider(y.getDateValider());
            d.setValiderBy(UtilUsers.buildSimpleBeanUsers(y.getValiderBy()));
            d.setDateAnnuler(y.getDateAnnuler());
            d.setAnnulerBy(UtilUsers.buildSimpleBeanUsers(y.getAnnulerBy()));
            d.setNature(y.getNature());
            d.setEditeur(UtilUsers.buildSimpleBeanUsers(y.getEditeur()));
            d.setDocumentLie((y.getDocumentLie() != null) ? new DocStock(y.getDocumentLie().getId(), y.getDocumentLie().getNumDoc(), y.getDocumentLie().getStatut()) : new DocStock());
            d.setEtapesValidations(y.getEtapesValidations());
            d.setAuthor((y.getAuthor() != null) ? y.getAuthor().getUsers().getNomUsers() : null);
            d.setDateUpdate(y.getDateUpdate());
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setTauxEcartInventaire(y.getTauxEcartInventaire());
            d.setValeur(buildBeanDocStockValeur(y.getValeur()));
            d.setUpdate(true);
            if (y.getNatureDoc() != null) {
                d.setNatureDoc(new NatureDoc(y.getNatureDoc().getId(), y.getNatureDoc().getNature(), y.getNatureDoc().getDescription(), y.getNatureDoc().getActif(), y.getNatureDoc().getDateSave()));
            } else {
                d.setNatureDoc(new NatureDoc());
            }
            if (y.getEtapesValidations() != null ? !y.getEtapesValidations().isEmpty() : false) {
                d.setFirstEtape(y.getEtapesValidations().get(0).getEtape().getLabelStatut());
            }
        }
        return d;
    }

    public static List<DocStock> buildBeanListDocStock(List<YvsComDocStocks> list) {
        List<DocStock> r = new ArrayList<>();
        if (list != null) {
            for (YvsComDocStocks a : list) {
                r.add(buildBeanDocStock(a));
            }
        }
        return r;
    }
    /*
     *
     *FIN GESTION TRANSFERT
     *
     */

    /*
     *
     *DEBUT GESTION VENTE
     *
     */
    public static RistourneDocVente buildBeanRistourneDocVente(YvsComRistourneDocVente y) {
        RistourneDocVente r = new RistourneDocVente();
        if (y != null) {
            r.setId(y.getId());
            r.setRistourne((y.getRistourne() != null) ? buildBeanRistourne(y.getRistourne()) : new Ristourne());
            r.setMontant(y.getMontant());
            r.setUpdate(true);
        }
        return r;
    }

    public static List<RistourneDocVente> buildBeanListRistourneDocVente(List<YvsComRistourneDocVente> list) {
        List<RistourneDocVente> r = new ArrayList<>();
        if (list != null) {
            for (YvsComRistourneDocVente a : list) {
                r.add(buildBeanRistourneDocVente(a));
            }
        }
        return r;
    }

    public static RemiseDocVente buildBeanRemiseDocVente(YvsComRemiseDocVente y) {
        RemiseDocVente r = new RemiseDocVente();
        if (y != null) {
            r.setId(y.getId());
            r.setRemise((y.getRemise() != null) ? buildBeanRemise(y.getRemise()) : new Remise());
            r.setMontant(y.getMontant());
            r.setUpdate(true);
        }
        return r;
    }

    public static List<RemiseDocVente> buildBeanListRemiseDocVente(List<YvsComRemiseDocVente> list) {
        List<RemiseDocVente> r = new ArrayList<>();
        if (list != null) {
            for (YvsComRemiseDocVente a : list) {
                r.add(buildBeanRemiseDocVente(a));
            }
        }
        return r;
    }

    public static CoutSupDoc buildBeanCoutSupDocVente(YvsComCoutSupDocVente y) {
        CoutSupDoc c = new CoutSupDoc();
        if (y != null) {
            c.setId(y.getId());
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            c.setType(UtilGrh.buildBeanTypeCout(y.getTypeCout()));
            c.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            c.setDoc(y.getDocVente() != null ? y.getDocVente().getId() : 0);
            c.setService(y.getService());
            c.setDateSave(y.getDateSave());
            c.setUpdate(true);
        }
        return c;
    }

    public static YvsComContenuDocVente buildContenuDocVente(ContenuDocVente y, YvsUsersAgence currentUser) {
        YvsComContenuDocVente c = new YvsComContenuDocVente();
        if (y != null) {
            c.setActif(y.isActif());
            c.setSupp(y.isSupp());
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                c.setArticle(UtilProd.buildEntityArticle(y.getArticle()));
            }
            if ((y.getArticleBonus() != null) ? y.getArticleBonus().getId() > 0 : false) {
                c.setArticleBonus(new YvsBaseArticles(y.getArticleBonus().getId(), y.getArticleBonus().getRefArt(), y.getArticleBonus().getDesignation(), y.getArticleBonus().isPuvTtc()));
            }
            if ((y.getQualite() != null) ? y.getQualite().getId() > 0 : false) {
                c.setQualite(new YvsComQualite(y.getQualite().getId(), y.getQualite().getCode(), y.getQualite().getLibelle()));
            }
            if ((y.getConditionnement() != null) ? y.getConditionnement().getId() > 0 : false) {
                c.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            if ((y.getConditionnementBonus() != null) ? y.getConditionnementBonus().getId() > 0 : false) {
                c.setConditionnementBonus(new YvsBaseConditionnement(y.getConditionnementBonus().getId(), new YvsBaseUniteMesure(y.getConditionnementBonus().getUnite().getId(), y.getConditionnementBonus().getUnite().getReference(), y.getConditionnementBonus().getUnite().getLibelle())));
            }
            if ((y.getDepoLivraisonPrevu() != null) ? y.getDepoLivraisonPrevu().getId() > 0 : false) {
                c.setDepoLivraisonPrevu(new YvsBaseDepots(y.getDepoLivraisonPrevu().getId(), y.getDepoLivraisonPrevu().getDesignation()));
            }
            if ((y.getLot() != null) ? y.getLot().getId() > 0 : false) {
                c.setLot(buildLotReception(y.getLot(), currentUser.getAgence(), currentUser));
            }
            c.setComission(y.getCommission());
            c.setDateContenu((y.getDateContenu() != null) ? y.getDateContenu() : new Date());
            c.setId(y.getId());
            c.setPrix(y.getPrix());
            c.setQuantite(y.getQuantite());
            c.setRemise(y.getRemise());
            c.setTaxe(y.getTaxe());
            c.setRistourne(y.getRistourne());
            c.setRabais(y.getRabais());
            c.setPr(y.getPr());
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
            c.setPuvMin(y.getPrixMin());
            c.setStatut(y.getStatut());
            c.setMouvStock(y.isMouvStock());
            c.setCalculPr(y.isCalculPr());
            if ((y.getParent() != null) ? y.getParent().getId() > 0 : false) {
                c.setParent(new YvsComContenuDocVente(y.getParent().getId()));
            }
            if ((y.getDocVente() != null) ? y.getDocVente().getId() > 0 : false) {
                c.setDocVente(buildDocVente(y.getDocVente(), currentUser));
            }
            c.setPrixTotal(Constantes.arrondi(y.getPrixTotal(), 2));
            c.setCommentaire(y.getCommentaire());
            c.setNumSerie(y.getNumSerie());
            c.setQuantiteBonus(y.getQuantiteBonus());
            c.setNew_(true);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                c.setAuthor(currentUser);
            }
        }
        return c;
    }

    public static ContenuDocVenteEtat buildBeanContenuDocVenteEtat(YvsComContenuDocVenteEtat y) {
        ContenuDocVenteEtat r = new ContenuDocVenteEtat();
        if (y != null) {
            r.setContenu(new ContenuDocVente(y.getContenu().getId()));
            r.setDateUpdate(y.getDateUpdate());
            r.setId(y.getId());
            r.setLibelle(y.getLibelle());
            r.setValeur(y.getValeur());
        }
        return r;
    }

    public static YvsComContenuDocVenteEtat buildContenuDocVenteEtat(ContenuDocVenteEtat y, YvsUsersAgence ua) {
        YvsComContenuDocVenteEtat r = new YvsComContenuDocVenteEtat();
        if (y != null) {
            r.setContenu(new YvsComContenuDocVente(y.getContenu().getId()));
            r.setDateUpdate(y.getDateUpdate());
            r.setId(y.getId());
            r.setLibelle(y.getLibelle());
            r.setValeur(y.getValeur());
            r.setAuthor(ua);
            r.setDateSave(new Date());
            r.setNew_(true);
        }
        return r;
    }

    public static ContenuDocVente buildBeanContenuDocVente(YvsComContenuDocVente y) {
        ContenuDocVente c = new ContenuDocVente();
        if (y != null) {
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            c.setArticle((y.getArticle() != null) ? UtilProd.buildBeanArticleForForm(y.getArticle()) : new Articles());
            c.setArticleBonus((y.getArticleBonus() != null) ? UtilProd.buildBeanArticleForForm(y.getArticleBonus()) : new Articles());
            c.setQualite(buildBeanQualite(y.getQualite()));
            c.setDepoLivraisonPrevu(buildBeanDepot(y.getDepoLivraisonPrevu()));
            c.setCommission((y.getComission() != null) ? y.getComission() : 0);
            c.setDateContenu((y.getDateContenu() != null) ? y.getDateContenu() : new Date());
            c.setLot((y.getLot() != null) ? buildBeanLotReception(y.getLot()) : new LotReception());
            c.setId(y.getId());
            c.setPrix(y.getPrix());
            c.setQuantite(y.getQuantite());
            c.setQuantiteBonus(y.getQuantiteBonus());
            c.setRemise(y.getRemise());
            c.setTaxe(y.getTaxe());
            c.setMouvStock(y.getMouvStock());
            c.setCalculPr(y.getCalculPr());
            c.setPrixMin(y.getPuvMin());
            c.setRabais(y.getRabais());
            c.setDocVente((y.getDocVente() != null) ? new DocVente(y.getDocVente().getId()) : new DocVente());
            c.setPr(y.getPr());
            c.setPrix_(y.getPrix_());
            c.setQuantite_(y.getQuantite_());
            c.setRemise_(y.getRemise_());
            c.setTaxe_(y.getTaxe_());
            c.setRistourne((y.getRistourne() != null) ? y.getRistourne() : 0);
            c.setPrixTotal(y.getPrixTotal());
            c.setCommentaire(y.getCommentaire());
            c.setStatut(y.getStatut());
            c.setToBonus(y.isBonus());
            c.setUpdate(true);
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(y.getDateUpdate());
            c.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            c.setConditionnementBonus(UtilProd.buildBeanConditionnement(y.getConditionnementBonus()));
            c.setNumSerie(y.getNumSerie());
            c.setDateSave(Util.currentTimeStamp(y.getDateSave()));
            c.setParent(y.getParent() != null ? new ContenuDocVente(y.getParent().getId()) : new ContenuDocVente());
            c.setTaxes(y.getTaxes());
            c.setEtats(y.getEtats());
        }
        return c;
    }

    public static List<ContenuDocVente> buildBeanListContenuDocVente(List<YvsComContenuDocVente> list) {
        List<ContenuDocVente> r = new ArrayList<>();
        if (list != null) {
            for (YvsComContenuDocVente a : list) {
                r.add(buildBeanContenuDocVente(a));
            }
        }
        return r;
    }

    public static YvsComDocVentesInformations buildDocVenteInformation(DocVenteInformation y, YvsUsersAgence ua) {
        YvsComDocVentesInformations r = new YvsComDocVentesInformations();
        if (y != null) {
            if (y.getAdresseLivraison() != null ? y.getAdresseLivraison().getId() > 0 : false) {
                r.setAdresseLivraison(new YvsBasePointLivraison(y.getAdresseLivraison().getId(), y.getAdresseLivraison().getLibelle()));
            }
            if (y.getFacture() != null ? y.getFacture().getId() > 0 : false) {
                r.setFacture(new YvsComDocVentes(y.getFacture().getId()));
            }
            r.setDateFin(y.getDateFin());
            r.setDateUpdate(y.getDateUpdate());
            r.setHeureDebut(y.getHeureDebut());
            r.setHeureFin(y.getHeureFin());
            r.setId(y.getId());
            r.setNomPersonneSupplementaire(y.getNomPersonneSupplementaire());
            r.setNombrePersonne(y.getNombrePersonne());
            r.setNumCni(y.getNumCni());
            r.setModeleVehicule(y.getModeleVehicule());
            r.setNumImmatriculation(y.getNumImmatriculation());
            r.setAuthor(ua);
            r.setDateSave(new Date());
            r.setNew_(true);
        }
        return r;
    }

    public static DocVenteInformation buildBeanDocVenteInformation(YvsComDocVentesInformations y) {
        DocVenteInformation r = new DocVenteInformation();
        if (y != null) {
            r.setAdresseLivraison(buildBeanPointLivraison(y.getAdresseLivraison()));
            r.setFacture(y.getFacture() != null ? new DocVente(y.getFacture().getId()) : new DocVente());
            r.setDateFin(y.getDateFin());
            r.setDateUpdate(y.getDateUpdate());
            r.setHeureDebut(y.getHeureDebut());
            r.setHeureFin(y.getHeureFin());
            r.setId(y.getId());
            r.setNomPersonneSupplementaire(y.getNomPersonneSupplementaire());
            r.setNombrePersonne(y.getNombrePersonne());
            r.setNumCni(y.getNumCni());
            r.setModeleVehicule(y.getModeleVehicule());
            r.setNumImmatriculation(y.getNumImmatriculation());
        }
        return r;
    }

    public static YvsComDocVentes buildDocVente(DocVente y, YvsComEnteteDocVente e, YvsUsersAgence a) {
        YvsComDocVentes d = new YvsComDocVentes();
        if (y != null) {
            d.setId(y.getId());
            d.setSupp(y.isSupp());
            if ((y.getCategorieComptable() != null) ? y.getCategorieComptable().getId() > 0 : false) {
                d.setCategorieComptable(new YvsBaseCategorieComptable(y.getCategorieComptable().getId(), y.getCategorieComptable().getCodeCategorie()));
            }
            if ((y.getClient() != null) ? y.getClient().getId() > 0 : false) {
                d.setClient(new YvsComClient(y.getClient().getId(), y.getClient().getCodeClient(), y.getClient().getNom(), y.getClient().getPrenom(), new YvsBaseTiers(y.getClient().getTiers().getId())));
            }
            if ((y.getDepot() != null) ? y.getDepot().getId() > 0 : false) {
                d.setDepotLivrer(new YvsBaseDepots(y.getDepot().getId(), y.getDepot().getDesignation()));
            }
            if ((y.getAdresse() != null) ? y.getAdresse().getId() > 0 : false) {
                d.setAdresse(new YvsDictionnaire(y.getAdresse().getId(), y.getAdresse().getLibelle()));
            }
            if ((y.getTranche() != null) ? y.getTranche().getId() > 0 : false) {
                d.setTrancheLivrer(new YvsGrhTrancheHoraire(y.getTranche().getId(), y.getTranche().getHeureDebut(), y.getTranche().getHeureFin()));
            }
            if ((y.getDocumentLie() != null) ? y.getDocumentLie().getId() > 0 : false) {
                d.setDocumentLie(new YvsComDocVentes(y.getDocumentLie().getId(), y.getDocumentLie().getNumDoc(), y.getDocumentLie().getStatut(), y.getDocumentLie().getStatutLivre(), y.getDocumentLie().getStatutRegle()));
            }
            d.setComptabilise(y.isComptabilise());
            d.setHeureDoc((y.getHeureDoc() != null) ? y.getHeureDoc() : new Date());
            d.setNumDoc(y.getNumDoc());
            d.setTelephone(y.getTelephone());
            d.setNumeroExterne(y.getNumeroExterne());
            d.setCommision(y.getCommision());
            d.setNumPiece(y.getNumPiece());
            d.setNomClient(y.getNomClient());
            d.setStatut((y.getStatut() != null) ? y.getStatut() : Constantes.ETAT_EDITABLE);
            d.setStatutLivre(y.getStatutLivre());
            d.setStatutRegle(y.getStatutRegle());
            d.setTypeDoc(y.getTypeDoc());
            d.setNature(y.getNature());
            d.setEnteteDoc(e);
            d.setMontantAvance(y.getMontantAvance());
            d.setDateSave(y.getDateSave());
            d.setDateUpdate(new Date());
            d.setDateLivraisonPrevu(y.getDateLivraisonPrevu());
            d.setCloturer(y.isCloturer());
            d.setDateCloturer(y.getDateCloturer());
            d.setConsigner(y.isConsigner());
            d.setLivraisonAuto(y.isLivraisonAuto());
            d.setDateConsigner(y.getDateConsigner());
            if (y.getCloturerBy() != null ? y.getCloturerBy().getId() > 0 : false) {
                d.setCloturerBy(new YvsUsers(y.getCloturerBy().getId()));
            }
            d.setDateLivraison(y.getDateLivraison());
            if (y.getLivreur() != null ? y.getLivreur().getId() > 0 : false) {
                d.setLivreur(new YvsUsers(y.getLivreur().getId(), y.getLivreur().getCodeUsers(), y.getLivreur().getCivilite(), y.getLivreur().getNomUsers()));
            }
            d.setDateAnnuler(y.getDateAnnuler());
            if (y.getAnnulerBy() != null ? y.getAnnulerBy().getId() > 0 : false) {
                d.setAnnulerBy(new YvsUsers(y.getAnnulerBy().getId()));
            }
            d.setDateValider(y.getDateValider());
            if (y.getValiderBy() != null ? y.getValiderBy().getId() > 0 : false) {
                d.setValiderBy(new YvsUsers(y.getValiderBy().getId()));
            }
            d.setDescription(y.getDescription());
            d.setContenus(y.getContenus());
            if (y.getModeReglement() != null ? y.getModeReglement().getId() > 0 : false) {
                d.setModelReglement(new YvsBaseModelReglement(y.getModeReglement().getId()));
            }
            if (a != null ? a.getId() > 0 : false) {
                d.setAuthor(a);
            }
            if (y.getTiers() != null ? y.getTiers().getId() > 0 : false) {
                d.setTiers(new YvsComClient(y.getTiers().getId()));
            }
            d.setEtapesValidations(y.getEtapesValidations());
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setOperateur(a != null ? a.getUsers() : null);
            d.setNew_(true);
        }
        return d;
    }

    public static YvsComDocVentes buildDocVente(DocVente y, YvsUsersAgence a) {
        return buildDocVente(y, new YvsComEnteteDocVente(y.getEnteteDoc().getId()), a);
    }

    public static DocVente buildSimpleBeanDocVente(YvsComDocVentes y) {
        return buildSimpleBeanDocVente(y, false);
    }

    public static DocVente buildSimpleBeanDocVente(YvsComDocVentes y, boolean withLivreur) {
        DocVente d = new DocVente();
        if (y != null) {
            d = buildSimpleBeanOnlyDocVente(y);
            if (y.getAdresse() != null) {
                d.setAdresse(buildBeanDictionnaire(y.getAdresse()));
                if (y.getAdresse().getParent() != null) {
                    d.setVille(buildBeanDictionnaire(y.getAdresse().getParent()));
                }
            }
            if (withLivreur) {
//                d.setLivreur(UtilUsers.buildBeanUsers(y.getLivreur()));
                d.setLivreur(UtilUsers.buildSimpleBeanUsers(y.getLivreur()));
            }
            d.setDocumentLie(y.getDocumentLie() != null ? new DocVente(y.getDocumentLie().getId(), y.getDocumentLie().getNumDoc(), y.getDocumentLie().getStatut()) : new DocVente());
            d.setEtapesValidations(y.getEtapesValidations());
            d.setContenus(y.getContenus());
            d.setContenusSave(new ArrayList<>(d.getContenus()));
            if (y.getEtapesValidations() != null ? !y.getEtapesValidations().isEmpty() : false) {
                d.setFirstEtape(y.getEtapesValidations().get(0).getEtape().getLabelStatut());
            }
        }
        return d;
    }

    public static DocVente buildInfosBaseDocVente(YvsComDocVentes y) {
        DocVente d = new DocVente();
        if (y != null) {
            d.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            d.setClient((y.getClient() != null) ? buildInfoBaseClient(y.getClient()) : new Client());
            d.setId(y.getId());
            d.setComptabilise(y.getComptabilise());
            d.setNumDoc(y.getNumDoc());
            d.setTelephone(y.getTelephone());
            d.setNumeroExterne(y.getNumeroExterne());
            d.setNumPiece(y.getNumPiece());
            d.setNomClient(y.getNomClient());
            d.setCommision(y.getCommision());
            d.setStatut((y.getStatut() != null) ? y.getStatut() : Constantes.ETAT_EDITABLE);
            d.setStatutLivre(y.getStatutLivre());
            d.setStatutRegle(y.getStatutRegle());
            d.setLivraisonAuto(y.getLivraisonAuto());
            d.setTypeDoc(y.getTypeDoc());
            d.setHeureDoc((y.getHeureDoc() != null) ? y.getHeureDoc() : new Date());
            d.setMontantAvance((y.getMontantAvance() != null) ? y.getMontantAvance() : 0);
            d.setUpdate(y.isUpdate());
            d.setDescription(y.getDescription());
            d.setDateSave(Util.currentTimeStamp(y.getDateSave()));
            d.setDateLivraisonPrevu(y.getDateLivraisonPrevu());
            d.setConsigner(y.getConsigner());
            d.setDateConsigner(y.getDateConsigner());
            d.setCloturer(y.getCloturer());
            d.setDateCloturer(y.getDateCloturer());
            d.setDateLivraison(y.getDateLivraison());
            d.setDateAnnuler(y.getDateAnnuler());
            d.setDateValider(y.getDateValider());
            d.setDateUpdate(new Date());
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setNature(y.getNature());

            d.setMontantRemise(y.getMontantRemise());
            d.setMontantTaxe(y.getMontantTaxe());
            d.setMontantRistourne(y.getMontantRistourne());
            d.setMontantCommission(y.getMontantCommission());
            d.setMontantHT(y.getMontantHT());
            d.setMontantTTC(y.getMontantTTC());
            d.setMontantRemises(y.getMontantRemises());
            d.setMontantCS(y.getMontantCS());
            d.setMontantAvance(y.getMontantAvance());
            d.setMontantTaxeR(y.getMontantTaxeR());
            d.setMontantResteApayer(y.getMontantResteApayer());
            d.setMontantPlanifier(y.getMontantPlanifier());

        }
        return d;
    }

    public static DocVente buildSimpleBeanOnlyDocVente(YvsComDocVentes y) {
        DocVente d = new DocVente();
        if (y != null) {
            d.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            d.setCategorieComptable((y.getCategorieComptable() != null) ? buildBeanCategorieComptable(y.getCategorieComptable()) : new CategorieComptable());
            d.setClient((y.getClient() != null) ? buildSimpleBeanClient(y.getClient()) : new Client());
            d.setId(y.getId());
            d.setComptabilise(y.getComptabilise());
            d.setNumDoc(y.getNumDoc());
            d.setTelephone(y.getTelephone());
            d.setNumeroExterne(y.getNumeroExterne());
            d.setNumPiece(y.getNumPiece());
            d.setNomClient(y.getNomClient());
            d.setCommision(y.getCommision());
            d.setStatut((y.getStatut() != null) ? y.getStatut() : Constantes.ETAT_EDITABLE);
            d.setStatutLivre(y.getStatutLivre());
            d.setStatutRegle(y.getStatutRegle());
            d.setLivraisonAuto(y.getLivraisonAuto());
            d.setTypeDoc(y.getTypeDoc());
            d.setHeureDoc((y.getHeureDoc() != null) ? y.getHeureDoc() : new Date());
            d.setMontantAvance((y.getMontantAvance() != null) ? y.getMontantAvance() : 0);
            d.setUpdate(y.isUpdate());
            d.setEnteteDoc(UtilCom.buildSimpleBeanEnteteDocVente(y.getEnteteDoc()));
            d.setDepot((y.getDepotLivrer() != null) ? buildSimpleBeanDepot(y.getDepotLivrer()) : new Depots());
            d.setTranche((y.getTrancheLivrer() != null) ? UtilGrh.buildTrancheHoraire(y.getTrancheLivrer()) : new TrancheHoraire());
            d.setDescription(y.getDescription());
            d.setDateSave(Util.currentTimeStamp(y.getDateSave()));
            d.setDateLivraisonPrevu(y.getDateLivraisonPrevu());
            d.setConsigner(y.getConsigner());
            d.setDateConsigner(y.getDateConsigner());
            d.setCloturer(y.getCloturer());
            d.setDateCloturer(y.getDateCloturer());
            d.setDateLivraison(y.getDateLivraison());
            d.setDateAnnuler(y.getDateAnnuler());
            d.setDateValider(y.getDateValider());
            d.setDateUpdate(new Date());
            d.setModeReglement(buildBeanModelReglement(y.getModelReglement()));
            d.setInformation(buildBeanDocVenteInformation(y.getInformation()));
            if (y.getEnteteDoc() != null ? y.getEnteteDoc().getCreneau() != null ? y.getEnteteDoc().getCreneau().getCreneauPoint() != null ? y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null : false : false : false) {
                d.setValidationReglement(y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getValidationReglement());
            }
            if (y.getTiers() != null ? (y.getTiers().getId() != null ? y.getTiers().getId() > 0 : false) : false) {
                d.setTiers(new Client(y.getTiers().getId()));
            }
            d.setNature(y.getNature());
            d.setEtapeTotal(y.getEtapeTotal());
            d.setEtapeValide(y.getEtapeValide());
            d.setMontantRemise(y.getMontantRemise());
            d.setMontantTaxe(y.getMontantTaxe());
            d.setMontantRistourne(y.getMontantRistourne());
            d.setMontantCommission(y.getMontantCommission());
            d.setMontantHT(y.getMontantHT());
            d.setMontantTTC(y.getMontantTTC());
            d.setMontantRemises(y.getMontantRemises());
            d.setMontantCS(y.getMontantCS());
            d.setMontantAvance(y.getMontantAvance());
            d.setMontantTaxeR(y.getMontantTaxeR());
            d.setMontantResteApayer(y.getMontantResteApayer());
            d.setMontantPlanifier(y.getMontantPlanifier());
        }
        return d;
    }

    public static DocVente buildBeanDocVente(YvsComDocVentes y) {
        DocVente d = buildSimpleBeanDocVente(y);
        if (y != null) {
            d.setLivreur(UtilUsers.buildSimpleBeanUsers(y.getLivreur()));
            d.setOperateur(UtilUsers.buildSimpleBeanUsers(y.getOperateur()));
            d.setDocumentLie(y.getDocumentLie() != null ? buildSimpleBeanDocVente(y.getDocumentLie()) : new DocVente());
            d.setCommerciaux(y.getCommerciaux());
            d.setDocuments(y.getDocuments());
            d.setMensualites(y.getMensualites());
            d.setReglements(y.getReglements());
            d.setCouts(y.getCouts());
            d.setMens(!d.getMensualites().isEmpty());

        }
        return d;
    }

    public static Commerciales buildBeanCommerciales(YvsComComerciale y) {
        Commerciales r = new Commerciales();
        if (y != null) {
            r.setId(y.getId());
            r.setCode(y.getCodeRef());
            r.setNom(y.getNom());
            r.setPrenom(y.getPrenom());
            r.setNomPrenom(y.getNom_prenom());
            r.setTelephone(y.getTelephone());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setActif(y.getActif());
            r.setDefaut(y.getDefaut());
            r.setCommission(buildBeanPlanCommission(y.getCommission()));
            r.setUser(UtilUsers.buildBeanUsers(y.getUtilisateur()));
            r.setTiers(UtilTiers.buildBeanTiers(y.getTiers()));
        }
        return r;
    }

    public static YvsComComerciale buildBeanCommerciales(Commerciales y, YvsUsersAgence ua, YvsAgences ag) {
        YvsComComerciale r = new YvsComComerciale();
        if (y != null) {
            r.setId(y.getId());
            r.setCodeRef(y.getCode());
            r.setNom(y.getNom());
            r.setPrenom(y.getPrenom());
            r.setTelephone(y.getTelephone());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setActif(y.isActif());
            r.setDefaut(y.isDefaut());
            if (y.getCommission() != null ? y.getCommission().getId() > 0 : false) {
                r.setCommission(new YvsComPlanCommission(y.getCommission().getId(), y.getCommission().getReference()));
            }
            if (y.getUser() != null ? y.getUser().getId() > 0 : false) {
                r.setUtilisateur(new YvsUsers(y.getUser().getId()));
            }
            if (y.getTiers() != null ? y.getTiers().getId() > 0 : false) {
                r.setTiers(new YvsBaseTiers(y.getTiers().getId(), y.getTiers().getCodeTiers()));
            }
            r.setAuthor(ua);
            r.setAgence(ag);
            r.setNew_(true);
        }
        return r;
    }

    public static CommercialVente buildBeanCommercialVente(YvsComCommercialVente y) {
        CommercialVente r = new CommercialVente();
        if (y != null) {
            r.setId(y.getId());
            r.setCommercial(buildBeanCommerciales(y.getCommercial()));
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setFacture(buildBeanDocVente(y.getFacture()));
            r.setResponsable(y.getResponsable());
            r.setDiminueCa(y.getDiminueCa());
            r.setTaux(y.getTaux());
        }
        return r;
    }

    public static YvsComCommercialVente buildCommercialVente(CommercialVente y, YvsUsersAgence ua) {
        YvsComCommercialVente r = new YvsComCommercialVente();
        if (y != null) {
            r.setId(y.getId());
            if (y.getCommercial() != null ? y.getCommercial().getId() > 0 : false) {
                r.setCommercial(buildBeanCommerciales(y.getCommercial(), ua, ua.getAgence()));
            }
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            if (y.getFacture() != null ? y.getFacture().getId() > 0 : false) {
                r.setFacture(new YvsComDocVentes(y.getFacture().getId()));
            }
            r.setResponsable(y.isResponsable());
            r.setDiminueCa(y.isDiminueCa());
            r.setTaux(y.getTaux());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static PieceTresorerie buildBeanPieceCommission(YvsComptaCaissePieceCommission y) {
        return UtilCompta.buildBeanTresoreri(y);
    }

    public static YvsComptaCaissePieceCommission buildPieceCommission(PieceTresorerie y, YvsUsersAgence u) {
        return UtilCompta.buildTresoreriCommission(y, u);
    }

    public static PieceTresorerie buildBeanPieceVente(YvsComptaCaissePieceVente y) {
        return UtilCompta.buildBeanTresoreri(y);
    }

    public static YvsComptaCaissePieceVente buildPieceVente(PieceTresorerie y, YvsUsersAgence u) {
        return UtilCompta.buildTresoreriVente(y, u);
    }

    public static PieceTresorerie buildBeanPieceAchat(YvsComptaCaissePieceAchat y) {
        return UtilCompta.buildBeanTresoreri(y);
    }

    public static YvsComptaCaissePieceAchat buildPieceAchat(PieceTresorerie y, YvsUsersAgence u) {
        return UtilCompta.buildTresoreriAchat(y, u);
    }

    public static EnteteDocVente buildBeanEnteteDocVente(YvsComEnteteDocVente y) {
        EnteteDocVente e = buildSimpleBeanEnteteDocVente(y);
        if (y != null) {
            e.setDocuments(y.getDocuments());
            e.setCloturerBy(UtilUsers.buildBeanUsers(y.getCloturerBy()));
            e.setValiderBy(UtilUsers.buildBeanUsers(y.getValiderBy()));
        }
        return e;
    }

    public static EnteteDocVente buildSimpleBeanEnteteDocVente(YvsComEnteteDocVente y) {
        EnteteDocVente e = new EnteteDocVente();
        if (y != null) {
            e.setCrenauHoraire((y.getCreneau() != null) ? buildSimpleBeanCreneauUsers(y.getCreneau()) : new CreneauUsers());
            if (e.getCrenauHoraire() != null ? e.getCrenauHoraire().getId_() > 0 : false) {
                if (e.getCrenauHoraire().getCreneauPoint() != null ? e.getCrenauHoraire().getCreneauPoint().getId() > 0 : false) {
                    e.setPoint(e.getCrenauHoraire().getCreneauPoint().getPoint());
                    e.setTranchePoint(e.getCrenauHoraire().getCreneauPoint().getTranche());
                }
                e.setUsers(e.getCrenauHoraire().getPersonnel());
            }
            e.setComptabilise(y.getComptabilise());
            e.setDateEntete((y.getDateEntete() != null) ? y.getDateEntete() : new Date());
            e.setEtat((y.getEtat() != null) ? y.getEtat() : Constantes.ETAT_EDITABLE);
            e.setId(y.getId());
            e.setDateCloturer(y.getDateCloturer());
            e.setDateValider(y.getDateValider());
            e.setReference(y.getReference());
//            if (y.getAgence() != null ? y.getAgence().getId() > 0 : false) {
//                e.setAgence(new Agence(y.getAgence().getId()));
//            }
            e.setNumero(y.getNumero());
            e.setUpdate(true);
            e.setDateSave(y.getDateSave());
            e.setDateUpdate(y.getDateUpdate());
        }
        return e;
    }

    /*
     *
     *FIN GESTION VENTE
     *
     */

    /*
     *
     *DEBUT GESTION REGLEMENT FACTURE VENTE
     *
     */
    public static MensualiteFactureVente buildBeanMensualiteFactureVente(YvsComMensualiteFactureVente y) {
        MensualiteFactureVente r = new MensualiteFactureVente();
        if (y != null) {
            r.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
            r.setEtat((y.getEtat() != null) ? y.getEtat() : Constantes.ETAT_EDITABLE);
            r.setId(y.getId());
            r.setFacture(new DocVente(y.getFacture().getId(), y.getFacture().getNumDoc(), y.getFacture().getStatut()));
            r.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            r.setMontantRest(y.getReste());
            r.setOutDelai(y.isOutDelai());
            r.setNameMens(y.getNameMens());
            r.setModeReglement(buildBeanModeReglement(y.getModeReglement()));
//            r.setReglements(y.getReglements());
            r.setUpdate(true);
        }
        return r;
    }

    /*
     *
     *FIN GESTION REGLEMENT FACTURE VENTE
     *
     */
    /*
     *
     *DEBUT GESTION MOUVEMENT STOCK
     *
     */
    public static MouvementStock buildBeanMouvementStock(YvsBaseMouvementStock y) {
        MouvementStock m = new MouvementStock();
        if (y != null) {
            m.setActif((y.getActif() != null) ? y.getActif() : false);
            m.setSupp((y.getSupp() != null) ? y.getSupp() : false);
            m.setArticle((y.getArticle() != null) ? buildBeanArticle(y.getArticle()) : new Articles());
            m.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            m.setDateDoc((y.getDateDoc() != null) ? y.getDateDoc() : new Date());
            m.setDepot((y.getDepot() != null) ? buildBeanDepot(y.getDepot()) : new Depots());
            m.setDescription(y.getDescription());
            m.setId(y.getId());
            m.setIdExterne((y.getIdExterne() != null) ? y.getIdExterne() : 0);
            m.setMouvement(y.getMouvement());
            m.setTranche(y.getTranche() != null ? UtilGrh.buildTrancheHoraire(y.getTranche()) : new TrancheHoraire());
            m.setQuantite((y.getQuantite() != null) ? y.getQuantite() : 0);
            m.setTableExterne(y.getTableExterne());
            m.setCout((y.getCoutEntree() != null) ? y.getCoutEntree() : 0);
            m.setCoutStock((y.getCoutStock() != null) ? y.getCoutStock() : 0);
            m.setReste(y.getReste());
            m.setCoutEntree(y.getCoutEntree());
        }
        return m;
    }

    public static List<MouvementStock> buildBeanListMouvementStock(List<YvsBaseMouvementStock> list) {
        List<MouvementStock> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseMouvementStock a : list) {
                r.add(buildBeanMouvementStock(a));
            }
        }
        Collections.sort(r, Collections.reverseOrder());
        return r;
    }
    /*
     *
     *FIN GESTION MOUVEMENT STOCK
     *
     */

    /*
     *
     *DEBUT GESTION CAISSE
     *
     */
    public static MensualiteDocDivers buildBeanMensualiteDocDivers(YvsComptaCaisseMensualiteDocDivers y) {
        MensualiteDocDivers r = new MensualiteDocDivers();
        if (y != null) {
            r.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
            r.setEtat(y.getEtat());
            r.setId(y.getId());
            r.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            r.setMontantRest(r.getMontant());
            r.setMontantRest((r.getMontantRest() > 0) ? r.getMontantRest() : 0);
            r.setUpdate(true);
        }
        return r;
    }

    public static MensualiteDocDivers buildBeanMensualiteDocDivers(YvsComptaCaisseMensualiteDocDivers y, DaoInterfaceLocal dao) {
        MensualiteDocDivers r = new MensualiteDocDivers();
        if (y != null) {
            r.setDateMensualite((y.getDateMensualite() != null) ? y.getDateMensualite() : new Date());
            r.setEtat(y.getEtat());
            r.setId(y.getId());
            r.setMontant((y.getMontant() != null) ? y.getMontant() : 0);
            List<YvsComptaCaissePieceDivers> l = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByTableIdExterne",
                    new String[]{"tableExterne", "idExterne"},
                    new Object[]{Constantes.yvs_base_mensualite_doc_divers, y.getId()});
            r.setPieces((l != null) ? buildBeanListPieceTresorerie(l, Constantes.SCR_DIVERS) : new ArrayList<PieceTresorerie>());
            Collections.sort(r.getPieces(), Collections.reverseOrder());
            r.setMontantRest(r.getMontant());
            for (PieceTresorerie m : r.getPieces()) {
                r.setMontantRest(r.getMontantRest() - m.getMontant());
            }
            r.setMontantRest((r.getMontantRest() > 0) ? r.getMontantRest() : 0);

            r.setOutDelai(Util.dateToCalendar(r.getDateMensualite()).before(Util.dateToCalendar(new Date())));
            r.setNameMens(Util.getDateToString(r.getDateMensualite()) + " : " + Util.getDouble(r.getMontant()));
            r.setUpdate(true);
        }
        return r;
    }

    public static List<MensualiteDocDivers> buildBeanListMensualiteDocDivers(List<YvsComptaCaisseMensualiteDocDivers> list, DaoInterfaceLocal dao) {
        List<MensualiteDocDivers> r = new ArrayList<>();
        if (list != null) {
            for (YvsComptaCaisseMensualiteDocDivers a : list) {
                r.add(buildBeanMensualiteDocDivers(a, dao));
            }
        }
        return r;
    }

    public static PieceTresorerie buildBeanPieceTresorerie(YvsComptaCaissePieceDivers y, String source) {
        PieceTresorerie p = new PieceTresorerie();
        if (y != null) {
            p.setDatePiece((y.getDatePiece() != null) ? y.getDatePiece() : new Date());
            p.setId(y.getId());
            p.setSource(source);
            p.setMontant(y.getMontant());
            p.setUpdate(true);
        }
        return p;
    }

    public static List<PieceTresorerie> buildBeanListPieceTresorerie(List<YvsComptaCaissePieceDivers> list, String source) {
        List<PieceTresorerie> r = new ArrayList<>();
        if (list != null) {
            for (YvsComptaCaissePieceDivers a : list) {
                r.add(buildBeanPieceTresorerie(a, source));
            }
        }
        Collections.sort(r, Collections.reverseOrder());
        return r;
    }

    public static PieceTresorerie buildBeanPieceTresorerie(YvsComptaCaissePieceDivers y) {
        PieceTresorerie p = new PieceTresorerie();
        if (y != null) {
            p.setDatePiece((y.getDatePiece() != null) ? y.getDatePiece() : new Date());
            p.setId(y.getId());
            p.setNumPiece(y.getNumPiece());
            p.setMontant(y.getMontant());
            p.setUpdate(true);
        }
        return p;
    }

    public static PieceTresorerie buildBeanPieceTresorerie(YvsComptaCaissePieceDivers y, DaoInterfaceLocal dao) {
        PieceTresorerie p = new PieceTresorerie();
        if (y != null) {
            p.setDatePiece((y.getDatePiece() != null) ? y.getDatePiece() : new Date());
            p.setId(y.getId());
            p.setMontant(y.getMontant());
            p.setUpdate(true);
//            if (y.getTableExterne() != null) {
//                switch (y.getTableExterne()) {
//                    case Constantes.yvs_com_mensualite_facture_achat:
//                        p.setSource(Constantes.SCR_ACHAT);
//                        p.setNameTable(Constantes.SCR_ACHAT_NAME);
//                        YvsComMensualiteFactureAchat ma = (YvsComMensualiteFactureAchat) dao.loadOneByNameQueries(
//                                "YvsComMensualiteFactureAchat.findById", new String[]{"id"}, new Object[]{p.getIdExterne()});
//                        p.setAchat((ma != null) ? buildBeanMensualiteFactureAchat(ma, dao) : new MensualiteFactureAchat());
//                        p.getAchat().setFacture((ma.getFacture() != null) ? buildBeanDocAchat(ma.getFacture(), dao) : new DocAchat());
//                        p.setMontantMens(p.getAchat().getMontant());
//                        break;
//                    case Constantes.yvs_com_mensualite_facture_vente:
//                        p.setSource(Constantes.SCR_VENTE);
//                        p.setNameTable(Constantes.SCR_VENTE_NAME);
//                        YvsComMensualiteFactureVente mv = (YvsComMensualiteFactureVente) dao.loadOneByNameQueries(
//                                "YvsComMensualiteFactureVente.findById", new String[]{"id"}, new Object[]{p.getIdExterne()});
//                        p.setVente((mv != null) ? buildBeanMensualiteFactureVente(mv, dao) : new MensualiteFactureVente());
//                        p.getVente().setFacture((mv.getFacture() != null) ? buildBeanDocVente(mv.getFacture(), dao) : new DocVente());
//                        p.setMontantMens(p.getVente().getMontant());
//                        break;
//                    case Constantes.yvs_base_mensualite_doc_divers:
//                        p.setSource(Constantes.SCR_DIVERS);
//                        p.setNameTable(Constantes.SCR_DIVERS_NAME);
//                        YvsComptaCaisseMensualiteDocDivers md = (YvsComptaCaisseMensualiteDocDivers) dao.loadOneByNameQueries(
//                                "YvsComptaCaisseMensualiteDocDivers.findById", new String[]{"id"}, new Object[]{p.getIdExterne()});
//                        p.setDivers((md != null) ? buildBeanMensualiteDocDivers(md, dao) : new MensualiteDocDivers());
//                        p.getDivers().setDocDivers((md.getDocDivers() != null) ? buildBeanDocDivers(md.getDocDivers(), dao) : new DocDivers());
//                        p.setMontantMens(p.getDivers().getMontant());
//                        break;
//                    default:
//                        break;

//            }
        }
        return p;
    }

    public static List<PieceTresorerie> buildBeanListPieceTresorerie(List<YvsComptaCaissePieceDivers> list, DaoInterfaceLocal dao) {
        List<PieceTresorerie> r = new ArrayList<>();
        if (list != null) {
            for (YvsComptaCaissePieceDivers a : list) {
                r.add(buildBeanPieceTresorerie(a, dao));
            }
        }
        Collections.sort(r, Collections.reverseOrder());
        return r;
    }

    /*
     *
     *FIN GESTION CAISSE
     *
     */
    /**
     * *******DEBUT GESTION DES REFERENCES
     *
     **********
     * @param t
     * @return
     */
    public static ModeleReference buildBeanModele(YvsBaseModeleReference t) {
        ModeleReference r = new ModeleReference();
        if (t != null) {
            r.setId(t.getId());
            r.setElement((t.getElement() != null) ? buildBeanElement(t.getElement()) : new ElementReference());
            r.setAnnee((t.getAnnee() != null) ? t.getAnnee() : false);
            r.setJour((t.getJour() != null) ? t.getJour() : false);
            r.setMois((t.getMois() != null) ? t.getMois() : false);
            r.setPrefix(t.getPrefix());
            r.setSeparateur(t.getSeparateur());
            r.setTaille(t.getTaille());
            r.setModule(t.getModule());
            r.setElementCode(t.getElementCode());
            r.setCodePoint((t.getCodePoint() != null) ? t.getCodePoint() : false);
            r.setLongueurCodePoint((t.getLongueurCodePoint() != null) ? t.getLongueurCodePoint() : 0);
            Calendar cal = Calendar.getInstance();
            String inter = ((r.getPrefix() != null) ? r.getPrefix() : "");
            if (r.isCodePoint()) {
                String code = "_X";
                for (int i = 1; i < r.getLongueurCodePoint(); i++) {
                    code += "X";
                }
                inter += code;
            }
            inter += r.getSeparateur();
            if (r.isJour()) {
                if (cal.get(Calendar.DATE) > 9) {
                    inter += Integer.toString(cal.get(Calendar.DATE));
                }
                if (cal.get(Calendar.DATE) < 10) {
                    inter += ("0" + Integer.toString(cal.get(Calendar.DATE)));
                }
            }
            if (r.isMois()) {
                if (cal.get(Calendar.MONTH) + 1 > 9) {
                    inter += Integer.toString(cal.get(Calendar.MONTH) + 1);
                }
                if (cal.get(Calendar.MONTH) + 1 < 10) {
                    inter += ("0" + Integer.toString(cal.get(Calendar.MONTH) + 1));
                }
            }
            if (r.isAnnee()) {
                inter += Integer.toString(cal.get(Calendar.YEAR)).substring(2);
            }
            inter += r.getSeparateur();
            for (int i = 0; i < r.getTaille(); i++) {
                inter += "0";
            }
            r.setApercu(inter);
            r.setUpdate(true);
            r.setDateSave(t.getDateSave());
        }
        return r;
    }

    public static YvsBaseModeleReference buildModele(ModeleReference y, YvsUsersAgence currentUser, YvsSocietes currentScte) {
        YvsBaseModeleReference p = new YvsBaseModeleReference();
        if (y != null) {
            p.setId(y.getId());
            p.setAnnee(y.isAnnee());
            if ((y.getElement() != null) ? y.getElement().getId() > 0 : false) {
                p.setElement(new YvsBaseElementReference(y.getElement().getId(), y.getElement().getDesignation()));
                y.setModule(y.getElement().getModule());
            }
            p.setJour(y.isJour());
            p.setMois(y.isMois());
            p.setPrefix(y.getPrefix());
            p.setSeparateur(y.getSeparateur());
            p.setTaille(y.getTaille());
            p.setCodePoint(y.isCodePoint());
            p.setElementCode(y.getElementCode());
            p.setLongueurCodePoint(y.getLongueurCodePoint());
            p.setModule(y.getModule());
            p.setAuthor(currentUser);
            p.setSociete(currentScte);
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
        }
        return p;
    }

    public static ElementReference buildBeanElement(YvsBaseElementReference t) {
        ElementReference r = new ElementReference();
        if (t != null) {
            r.setId(t.getId());
            r.setModule(t.getModule());
            r.setDesignation(t.getDesignation());
        }
        return r;
    }

    /**
     * *******FIN GESTION DES REFERENCES**********
     */
    public static TrancheHoraire buildBeanTrancheHoraire(YvsGrhTrancheHoraire y) {
        TrancheHoraire c = new TrancheHoraire();
        if (y != null) {
            c.setActif((y.getActif() != null) ? y.getActif() : false);
            c.setTypeJournee(y.getTypeJournee());
            c.setHeureDebut(y.getHeureDebut());
            c.setHeureFin(y.getHeureFin());
            c.setId(y.getId());
            c.setTitre(y.getTitre());
            c.setDateSave(y.getDateSave());
            c.setVenteOnline(y.getVenteOnline());
        }
        return c;
    }

    public static ReservationStock buildBeanReservation(YvsComReservationStock y) {
        ReservationStock r = new ReservationStock();
        if (y != null) {
            r.setId(y.getId());
            r.setActif(y.getActif());
            r.setArticle(buildBeanArticle(y.getArticle()));
            r.setDateEcheance(y.getDateEcheance());
            r.setDateReservation(y.getDateReservation());
            r.setDepot(buildBeanDepot(y.getDepot()));
            r.setNumReference(y.getNumReference());
            r.setNumExterne(y.getNumExterne());
            r.setDescription(y.getDescription());
            r.setQuantite(y.getQuantite());
            r.setStatut(y.getStatut());
            r.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
        }
        return r;
    }

    public static YvsComReservationStock buildReservation(ReservationStock y, YvsUsersAgence ua) {
        YvsComReservationStock r = new YvsComReservationStock();
        if (y != null) {
            r.setId(y.getId());
            r.setActif(y.isActif());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            if (y.getConditionnement() != null ? y.getConditionnement().getId() > 0 : false) {
                r.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            r.setDateEcheance(y.getDateEcheance());
            r.setDateReservation(y.getDateReservation());
            if (y.getDepot() != null ? y.getDepot().getId() > 0 : false) {
                r.setDepot(new YvsBaseDepots(y.getDepot().getId(), y.getDepot().getDesignation()));
            }
            r.setDescription(y.getDescription());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setNumReference(y.getNumReference());
            r.setNumExterne(y.getNumExterne());
            r.setQuantite(y.getQuantite());
            r.setStatut(y.getStatut());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static Qualite buildBeanQualite(YvsComQualite y) {
        Qualite c = new Qualite();
        if (y != null) {
            c.setActif(y.getActif());
            c.setCode(y.getCode());
            c.setId(y.getId());
            c.setLibelle(y.getLibelle());
            c.setDateSave(y.getDateSave());
        }
        return c;
    }

    public static YvsComModeleObjectif buildBeanObjectifs(ModelObjectif model) {
        YvsComModeleObjectif re = null;
        if (model != null) {
            re = new YvsComModeleObjectif();
            re.setActif(model.isActif());
            re.setCodeRef(model.getCodeid());
            re.setDescription(model.getTitre());
            re.setId(model.getId());
            re.setIndicateur(model.getIndicateur());
            re.setDateSave(model.getDateSave());
            re.setDateUpdate(new Date());
        }
        return re;
    }

    public static YvsComPeriodeObjectif buildBeanPeriode(PeriodesObjectifs period) {
        YvsComPeriodeObjectif re = null;
        if (period != null) {
            re = new YvsComPeriodeObjectif();
            re.setCodeRef(period.getReference());
            re.setDateDebut(period.getDebut());
            re.setDateFin(period.getFin());
            re.setId(period.getId());
        }
        return re;
    }

    public static PeriodesObjectifs buildBeanPeriode(YvsComPeriodeObjectif period) {
        PeriodesObjectifs re = new PeriodesObjectifs();
        if (period != null) {
            re.setReference(period.getCodeRef());
            re.setDebut(period.getDateDebut());
            re.setFin(period.getDateFin());
            re.setId(period.getId());
            re.setDateSave(period.getDateSave());
            re.setDateUpdate(period.getDateUpdate());
            re.setAuteur((period.getAuthor() != null) ? period.getAuthor().getUsers().getNomUsers() : null);
        }
        return re;
    }

    public static ModelObjectif buildBeanObjectifs(YvsComModeleObjectif model) {
        ModelObjectif re = new ModelObjectif();
        if (model != null) {
            re.setActif(model.getActif());
            re.setCodeid(model.getCodeRef());
            re.setTitre(model.getDescription());
            re.setId(model.getId());
            re.setIndicateur(model.getIndicateur());
            if (model.getAuthor() != null) {
                re.setAuthor(model.getAuthor().getUsers().getNomUsers());
            }
            re.setDateSave(model.getDateSave());
            re.setDateUpdate(model.getDateUpdate());
            re.setCiblesObjectifs(model.getCibles());
        }
        return re;
    }

    public static PeriodesObjectifs buildBeanPeriode(YvsComPeriodeRation p) {
        PeriodesObjectifs re = new PeriodesObjectifs();
        if (p != null) {
            re.setId(p.getId());
            re.setCloturer(p.getCloturer());
            re.setDebut(p.getDateDebut());
            re.setFin(p.getFin());
            re.setHdebut(p.getHeureDebut());
            re.setHfin(p.getHeureFin());
            re.setReference(p.getReferencePeriode());
        }
        return re;
    }

    public static YvsComPeriodeRation buildBeanPeriodeRa(PeriodesObjectifs p) {
        YvsComPeriodeRation re = null;
        if (p != null) {
            re = new YvsComPeriodeRation();
            re.setId(p.getId());
            re.setCloturer(p.isCloturer());
            re.setDateDebut(p.getDebut());
            re.setFin(p.getFin());
            re.setHeureDebut(p.getHdebut());
            re.setHeureFin(p.getHfin());
            re.setReferencePeriode(p.getReference());
        }
        return re;
    }

    public static Rations buildBeanRations(YvsComRation y) {
        Rations r = new Rations();
        if (y != null) {
            r.setId(y.getId());
            r.setArticle(buildBeanArticle(y.getArticle()));
            r.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            r.setLot(buildBeanLotReception(y.getLot()));
            r.setDateRation(y.getDateRation());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            r.setPersonnel(UtilTiers.buildBeanTiers(y.getPersonnel()));
            r.setQuantite(y.getQuantite());
            r.setStock(y.getStock_());
            r.setCalculPr(y.getCalculPr());
        }
        return r;
    }

    public static YvsComRation buildRations(Rations y, YvsUsersAgence ua) {
        YvsComRation r = new YvsComRation();
        if (y != null) {
            r.setId(y.getId());
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            if (y.getConditionnement() != null ? y.getConditionnement().getId() > 0 : false) {
                r.setConditionnement(new YvsBaseConditionnement(y.getConditionnement().getId(), new YvsBaseUniteMesure(y.getConditionnement().getUnite().getId(), y.getConditionnement().getUnite().getReference(), y.getConditionnement().getUnite().getLibelle())));
            }
            if (y.getLot() != null ? y.getLot().getId() > 0 : false) {
                r.setLot(buildLotReception(y.getLot(), ua.getAgence(), ua));
            }
            r.setDateRation(y.getDateRation());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(y.getDateUpdate());
            if (y.getPersonnel() != null ? y.getPersonnel().getId() > 0 : false) {
                r.setPersonnel(new YvsBaseTiers(y.getPersonnel().getId(), y.getPersonnel().getNom(), y.getPersonnel().getPrenom()));
            }
            r.setQuantite(y.getQuantite());
            r.setCalculPr(y.isCalculPr());
            r.setAuthor(ua);
        }
        return r;
    }

    public static YvsComDocRation buildDocRation(DocRations d, YvsUsersAgence ua) {
        YvsComDocRation re = new YvsComDocRation();
        re.setAuthor(ua);
        re.setDepot(UtilProd.buildBeanDepot(d.getDepot()));
        re.setPeriode(UtilCom.buildBeanPeriodeRa(d.getPeriode()));
        re.setId(d.getId());
        re.setNbrJrUsine(d.getNbrJrUsine());
        if (d.getValidBy() != null ? d.getValidBy().getId() > 0 : false) {
            re.setMagasinier(new YvsUsers(d.getValidBy().getId(), d.getValidBy().getCodeUsers(), d.getValidBy().getNomUsers()));
        } else {
            re.setMagasinier(ua.getUsers());
        }
        re.setNumDoc(d.getNumDoc());
        re.setNumReference(d.getNumReference());
        re.setStatut(d.getStatut());
        re.setDateSave(d.getDateSave());
        re.setDateFiche(d.getDateFiche());
        re.setCreneauHoraire(buildCreneau(d.getCreneauHoraire(), ua, re.getDepot()));
        re.setDateUpdate(new Date());
        re.setCloturer(d.isCloturer());
        return re;
    }

    public static DocRations buildBeanDocRation(YvsComDocRation d) {
        DocRations re = new DocRations();
        if (d != null) {
            re.setDepot(UtilProd.buildBeanDepot(d.getDepot()));
            re.setPeriode(UtilCom.buildBeanPeriode(d.getPeriode()));
            re.setId(d.getId());
            re.setNbrJrUsine(d.getNbrJrUsine());
            re.setNumDoc(d.getNumDoc());
            re.setNumReference(d.getNumReference());
            re.setStatut(d.getStatut());
            re.setValidBy(UtilUsers.buildSimpleBeanUsers(d.getMagasinier()));
            re.setDateSave(d.getDateSave());
            re.setDateFiche(d.getDateFiche());
            re.setCreneauHoraire(UtilCom.buildBeanCreneau(d.getCreneauHoraire()));
            re.setCloturer(d.getCloturer());
        }
        return re;
    }

    public static YvsBaseTiers buildTiers(Tiers c) {
        YvsBaseTiers y = new YvsBaseTiers();
        y.setId(c.getId());
        y.setCodeTiers(c.getCodeTiers());
        y.setNom(c.getNom());
        y.setPrenom(c.getPrenom());
        y.setResponsable(c.getResponsable());
        y.setCodePostal(c.getCodePostal());
        y.setCivilite(c.getCivilite());
        y.setAdresse(c.getAdresse());
        y.setEmail(c.getEmail());
        y.setSite(c.getSite());
        y.setBp(c.getBp());
        y.setLogo(c.getLogo());
        y.setTel(c.getTelephone());
        y.setDateSave(c.getDateSave());
        y.setDateUpdate(new Date());
        y.setCodeRation(c.getCodeRation());
        if (c.getCategorieComptable().getId() > 0) {
            y.setCategorieComptable(new YvsBaseCategorieComptable(c.getCategorieComptable().getId()));
        }
        if (c.getAgence().getId() > 0) {
            y.setAgence(new YvsAgences(c.getAgence().getId()));
        }
        if (c.getModelDeRglt() > 0) {
            y.setMdr(new YvsBaseModeReglement((long) c.getModelDeRglt()));
        }
//        if (c.getPlanComission() > 0) {
//            y.setComission(new YvsComPlanCommission(c.getPlanComission()));
//        }
//        if (c.getPlanRistourne() > 0) {
//            y.setRistourne(new YvsComPlanRistourne(c.getPlanRistourne()));
//        }
        if (c.getCompteCollectif() > 0) {
            y.setCompteCollectif(new YvsBasePlanComptable(c.getCompteCollectif()));
        }

        y.setStSociete(c.isSociete());
        y.setClient(c.isClient());
        y.setFournisseur(c.isFournisseur());
        y.setFabriquant(c.isFabricant());
        y.setRepresentant(c.isRepresentant());
        y.setEmploye(c.isEmploye());
        y.setPersonnel(c.isPersonnel());
        y.setActif(c.isActif());
        return y;
    }

    public static EcartVenteOrStock buildBeanEcartStock(YvsComDocStocksEcart y) {
        EcartVenteOrStock r = new EcartVenteOrStock();
        if (y != null) {
            r.setNumero(y.getNumero());
            r.setDateSave(y.getDateSave());
            r.setId(y.getId());
            r.setStatut(y.getStatut());
            r.setStatutRegle(y.getStatutRegle());
            r.setMontant(y.getTaux());
            r.setTiers(UtilTiers.buildSimpleBeanTiers(y.getTiers()));
            r.setInventaire(buildBeanDocStock(y.getDocStock()));
            r.setReglementsStock(y.getReglements());
        }
        return r;
    }

    public static YvsComDocStocksEcart buildEcartStock(EcartVenteOrStock y, YvsUsersAgence ua) {
        YvsComDocStocksEcart r = new YvsComDocStocksEcart();
        if (y != null) {
            r.setNumero(y.getNumero());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setId(y.getId());
            r.setStatut(y.getStatut());
            r.setStatutRegle(y.getStatutRegle());
            r.setTaux(y.getMontant());
            if (y.getTiers() != null ? y.getTiers().getId() > 0 : false) {
                r.setTiers(new YvsBaseTiers(y.getTiers().getId(), y.getTiers().getCodeTiers(), y.getTiers().getNom(), y.getTiers().getPrenom()));
            }
            if (y.getInventaire() != null ? y.getInventaire().getId() > 0 : false) {
                r.setDocStock(new YvsComDocStocks(y.getInventaire().getId(), y.getInventaire().getNumDoc(), y.getInventaire().getStatut(), y.getInventaire().getDateDoc()));
            }
            r.setAuthor(ua);
        }
        return r;
    }

    public static EcartVenteOrStock buildBeanEcartVente(YvsComEcartEnteteVente y) {
        EcartVenteOrStock r = new EcartVenteOrStock();
        if (y != null) {
            r.setNumero(y.getNumero());
            r.setDateDebut(y.getDateDebut());
            r.setDateEcart(y.getDateEcart());
            r.setDateSave(y.getDateSave());
            r.setId(y.getId());
            r.setStatut(y.getStatut());
            r.setStatutRegle(y.getStatutRegle());
            r.setMontant(Math.abs(y.getMontant()));
            r.setUsers(UtilUsers.buildSimpleBeanUsers(y.getUsers()));
            r.setAgence(new Agence(y.getAgence() != null ? y.getAgence().getId() : 0));
            r.setReglementsVente(y.getReglements());
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(UtilCompta.buildSimpleBeanCaisse(y.getCaisse().getCaisse()));
            }
        }
        return r;
    }

    public static YvsComEcartEnteteVente buildEcartVente(EcartVenteOrStock y, YvsUsersAgence ua) {
        YvsComEcartEnteteVente r = new YvsComEcartEnteteVente();
        if (y != null) {
            r.setId(y.getId());
            r.setNumero(y.getNumero());
            r.setDateDebut(y.getDateDebut());
            r.setDateEcart(y.getDateEcart());
            if (y.getUsers() != null ? y.getUsers().getId() > 0 : false) {
                r.setUsers(new YvsUsers(y.getUsers().getId(), y.getUsers().getCodeUsers(), y.getUsers().getNomUsers()));
            }
            if (y.getAgence() != null ? y.getAgence().getId() > 0 : false) {
                r.setAgence(new YvsAgences(y.getAgence().getId(), y.getAgence().getDesignation()));
            }
            r.setMontant(y.getMontant());
            r.setStatut(y.getStatut());
            r.setStatutRegle(y.getStatutRegle());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static ReglementEcart buildBeanReglementEcart(YvsComReglementEcartVente y) {
        ReglementEcart r = new ReglementEcart();
        if (y != null) {
            r.setNumero(y.getNumero());
            r.setCaisse(UtilCompta.buildSimpleBeanCaisse(y.getCaisse()));
            r.setDateReglement(y.getDateReglement());
            r.setDateSave(y.getDateSave());
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setPiece(y.getPiece() != null ? new EcartVenteOrStock(y.getPiece().getId()) : new EcartVenteOrStock());
            r.setStatut(y.getStatut());
        }
        return r;
    }

    public static YvsComReglementEcartVente buildReglementEcartVente(ReglementEcart y, YvsUsersAgence ua) {
        YvsComReglementEcartVente r = new YvsComReglementEcartVente();
        if (y != null) {
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(new YvsBaseCaisse(y.getCaisse().getId(), y.getCaisse().getIntitule()));
            }
            r.setNumero(y.getNumero());
            r.setDateReglement(y.getDateReglement());
            r.setDateSave(y.getDateSave());
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            if (y.getPiece() != null ? y.getPiece().getId() > 0 : false) {
                r.setPiece(new YvsComEcartEnteteVente(y.getPiece().getId()));
            }
            r.setStatut(y.getStatut());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }

    public static ReglementEcart buildBeanReglementEcart(YvsComReglementEcartStock y) {
        ReglementEcart r = new ReglementEcart();
        if (y != null) {
            r.setNumero(y.getNumero());
            r.setCaisse(UtilCompta.buildSimpleBeanCaisse(y.getCaisse()));
            r.setDateReglement(y.getDateReglement());
            r.setDateSave(y.getDateSave());
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            r.setPiece(y.getPiece() != null ? new EcartVenteOrStock(y.getPiece().getId()) : new EcartVenteOrStock());
            r.setStatut(y.getStatut());
        }
        return r;
    }

    public static YvsComReglementEcartStock buildReglementEcartStock(ReglementEcart y, YvsUsersAgence ua) {
        YvsComReglementEcartStock r = new YvsComReglementEcartStock();
        if (y != null) {
            if (y.getCaisse() != null ? y.getCaisse().getId() > 0 : false) {
                r.setCaisse(new YvsBaseCaisse(y.getCaisse().getId(), y.getCaisse().getIntitule()));
            }
            r.setNumero(y.getNumero());
            r.setDateReglement(y.getDateReglement());
            r.setDateSave(y.getDateSave());
            r.setId(y.getId());
            r.setMontant(y.getMontant());
            if (y.getPiece() != null ? y.getPiece().getId() > 0 : false) {
                r.setPiece(new YvsComDocStocksEcart(y.getPiece().getId()));
            }
            r.setStatut(y.getStatut());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
}
