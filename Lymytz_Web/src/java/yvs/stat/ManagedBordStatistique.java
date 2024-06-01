/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.base.produits.ManagedClasseStat;
import yvs.base.produits.ManagedFamilleArticle;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.ManagedFactureAchat;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.depot.ManagedPointLivraison;
import yvs.commercial.depot.ManagedPointVente;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.commercial.param.StatistiqueGeneral;
import yvs.commercial.statistique.SyntheseClient;
import yvs.commercial.statistique.SyntheseFournisseur;
import yvs.commercial.vente.ManagedFactureVenteV2;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.client.ManagedOperationClient;
import yvs.comptabilite.fournisseur.ManagedOperationFournisseur;
import yvs.comptabilite.tresorerie.ManagedBonProvisoire;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCreditClient;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.achat.YvsComptaCreditFournisseur;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.stat.dashboard.YvsStatDashboard;
import yvs.entity.stat.dashboard.YvsStatDashboardUsers;
import yvs.entity.tiers.YvsBaseTiersTemplate;
import yvs.entity.users.YvsUsers;
import yvs.etats.Dashboards;
import yvs.etats.Rows;
import yvs.etats.ValeursEtat;
import yvs.etats.commercial.JournalVendeur;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.mutuelle.ManagedExercice;
import yvs.production.ManagedParamProd;
import yvs.production.UtilProd;
import yvs.production.base.ManagedSiteProduction;
import yvs.users.ManagedUser;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.users.acces.AccesPage;
import yvs.util.Constantes;
import yvs.util.Managed;
import static yvs.util.Managed.formatDate;
import yvs.util.Montant;
import yvs.util.PaginatorResult;
import yvs.util.ParamDate;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedBordStatistique extends Managed<Serializable, Serializable> implements Serializable {

    private String tabNotActive = "", options = "";
    private List<Dashboard> dashboards;
    private List<YvsStatDashboardUsers> parametres;
    List<Dashboard> parametrages = new ArrayList<>();

    private boolean loadSalarial = false, loadBonProvisoire, loadSoldeCaisse, loadClassementMS, loadClassementPF, loadClassementPoint, loadClassementVendeur, loadClassementClient, loadSoldeFournisseur;

    private double masseSalarialMoisPrec, masseSalarialAnnuel,
            cotisationSalarialMoisPrec, cotisationSalarialAnnuel,
            taxeSalarialMoisPrec, taxeSalarialAnnuel;

    private Date dateDebut = new Date(), dateFin = new Date();
    private Date debutP = new Date(), finP = new Date(); // période Année précedente
    private Date jourP = new Date(); // jour Année précedente
    private String periode = "M", type = "ca", afficherPar = "C", nature = "A", typeListing, element = "V", comptes = "", groupBy, categorie = "PF", valoriserBy = "R", natureProgress = "A";
    private String typeMontantPerf = "ca", typeMontantComp = "ca", typeMontantSuiv = "ca", typeMontantSuivF = "ca", etat = "", articles;
    private long agence, point, classe, famille, site, users, zone, depot, exercice, unite;
    private int cumulBy = 0, vueType = 0, colonneAge = 4, ecartAge = 0, mois, nombreEtat = 0;
    private boolean cumule, byFamille, byPoint, grouper, grouperF, choixClient = false, displayName = false;
    private Long countTransfert;
    private Client client = new Client();
    private Fournisseur fournisseur = new Fournisseur();
    private YvsBaseTiersTemplate template = new YvsBaseTiersTemplate();
    private YvsBaseExercice exo = new YvsBaseExercice();
    private YvsBaseDepots selectedDepot = new YvsBaseDepots();

    private List<Dashboards.InstanceDashboards> saves;

    private StatistiqueGeneral commercial = new StatistiqueGeneral();

    private Dashboards tabPointAgence = new Dashboards();
    private Dashboards tabAgence = new Dashboards();
    private Dashboards tabArticleAgence = new Dashboards();
    private Dashboards tabArticlePv = new Dashboards();
    private Dashboards tabVendeur = new Dashboards();
    private Dashboards tabArticleVendeur = new Dashboards();
    private Dashboards tabArticleClient = new Dashboards();
    private Dashboards tabMargeMs = new Dashboards("A");
    private Dashboards tabMargePf = new Dashboards("A");
    private Dashboards tabMargeClasse = new Dashboards("C");
    private Dashboards tabMargePoint = new Dashboards("P");
    private Dashboards tabMargeFamille = new Dashboards("F");
    private Dashboards tabClients = new Dashboards();
    private Dashboards soldeClients = new Dashboards();
    private Dashboards soldeCaisses = new Dashboards();
    private Dashboards soldesFssseurs = new Dashboards();
    private Dashboards creanceClient = new Dashboards("A");

    private List<String> header;
    private List<yvs.util.Options> colonnes;
    private Object[][] donnees;
    private Users vendeur = new Users();
    private Articles article = new Articles();
    private List<Object> list;
    private List<Object[]> listing_facture, listing_facture_achat;
    private Dashboards age = new Dashboards();
    private Dashboards etats = new Dashboards();
    private Dashboards progress = new Dashboards();
    private Dashboards journal = new Dashboards();
    private Dashboards journal_achat = new Dashboards();
    private Dashboards caPeriode = new Dashboards();
    private Dashboards caAchatPeriode = new Dashboards();
    private Dashboards bonus = new Dashboards();
    private Dashboards ristourne = new Dashboards();
    private Dashboards listingClient = new Dashboards();
    private Dashboards listingFsseur = new Dashboards();
    private Dashboards ecarts = new Dashboards();
    private List<YvsComDocVentes> listings;
    private List<YvsComContenuDocAchat> listings_achat;
    private Dashboards listing = new Dashboards();
    private Dashboards listing_achat = new Dashboards();
    private List<Montant[]> valuesDashboard = new ArrayList<>();
    private List<Object[]> ventesClients = new ArrayList<>();
    private List<ParamDate> coloneClient;
    private Montant[] footerTotaux;
    private List<Long> selectionArticle = new ArrayList<>();
    private long idClient, idFsseur;
    private int indexClient = -1, indexFsseur = -1;
    private double totalListingAchat, totalListingFactureAchat, coefficient = 1;
    private double totalCaByPeriode;
    private double totalCaByArticle;

    private String codeClient, codeFsseur, editeurs, valoriseMp = "PUA", valoriseMs = "PUV", valorisePf = "PUV", valorisePsf = "PR";
    private String whatValeurDisplay = "MANQUANT+EXCEDENT";//-MANQUANT+EXCEDENT -MANQUANT%EXCEDENT -EXCEDENT -MANQUANT
    private boolean valoriseExcedent;
    private SyntheseClient synthese = new SyntheseClient();
    private SyntheseFournisseur syntheseF = new SyntheseFournisseur();
    private double creditFsseur, debitFsseur, soldeFsseur;

    private PaginatorResult<YvsComDocVentes> p_listing = new PaginatorResult<>();
    private PaginatorResult<YvsComContenuDocAchat> p_listing_achat = new PaginatorResult<>();

    private YvsComDocAchats achat = new YvsComDocAchats();

    private List<YvsUsers> selectedUsers;
    private List<YvsComptaBonProvisoire> bonsProvisoires;
    private List<YvsBaseModeReglement> models;
    private List<YvsBaseCaisse> caisses;
    private List<ValeursEtat> generales;
    private List<Object[]> bonNotJustifier;
    private String groupJustifyBy = "B";
    private List<Object[]> impayesAchat;
    private String groupImpayeBy = "F";

    private Dashboards journalProd = new Dashboards("A");
    private Dashboards journalProdByEquipe = new Dashboards("A");
    private Dashboards journalProdByTranche = new Dashboards("A");
    private Dashboards recapOf = new Dashboards("A");
    private Dashboards recapOfByEquipe = new Dashboards("A");
    private Dashboards recapOfByTranche = new Dashboards("A");
    private Dashboards consommation = new Dashboards("A");
    private Dashboards tabArticleStock = new Dashboards("A");
    private Dashboards syntheseDist = new Dashboards("CLASSE");
    private Dashboards productionVente = new Dashboards(null);
    private Dashboards prodConso = new Dashboards(null);
    private Dashboards prodConsoEquipe = new Dashboards(null);
    private Dashboards ecart = new Dashboards("MANQUANT");
    private Dashboards valorise = new Dashboards("");
    private Dashboards ration = new Dashboards("");

    public ManagedBordStatistique() {
        selectedUsers = new ArrayList<>();
        generales = new ArrayList<>();
        models = new ArrayList<>();
        caisses = new ArrayList<>();
        bonsProvisoires = new ArrayList<>();
        impayesAchat = new ArrayList<>();
        bonNotJustifier = new ArrayList<>();
        saves = new ArrayList<>();
        parametres = new ArrayList<>();
        dashboards = new ArrayList<>();
        header = new ArrayList<>();
        colonnes = new ArrayList<>();
        listings = new ArrayList<>();
        listings_achat = new ArrayList<>();
        listing_facture = new ArrayList<>();
        listing_facture_achat = new ArrayList<>();
        list = new ArrayList<Object>() {
            {
                add(new Object());
            }
        };

        tabArticleAgence.setType("caq");
        tabArticlePv.setType("caq");
        tabArticleVendeur.setType("caq");
        tabArticleClient.setType("caq");

        journal.setType(JournalVendeur.TYPE_VALEUR);
        journal_achat.setType(JournalVendeur.TYPE_VALEUR);
        caPeriode.setType(JournalVendeur.TYPE_VALEUR);
        caAchatPeriode.setType(JournalVendeur.TYPE_VALEUR);
        ecarts.setType(JournalVendeur.TYPE_VALEUR);
        bonus.setType(JournalVendeur.TYPE_VALEUR);
        ristourne.setType(JournalVendeur.TYPE_VALEUR);
        listingClient.setType(JournalVendeur.TYPE_VALEUR);
        listingFsseur.setType(JournalVendeur.TYPE_VALEUR);
        soldeCaisses.setType(JournalVendeur.TYPE_TAUX);
        soldesFssseurs.setType(JournalVendeur.TYPE_TAUX);

        tabMargeMs.setCategorie("MARCHANDISE");
        tabMargePf.setCategorie("PF");

        syntheseDist.setByValue(true);
        consommation.setByValue(true);
        consommation.setDisplayCA(false);
        creanceClient.setCumule(true);
        ecart.setCategorie("PRODUCTION");
        journalProd.setCumulBy(0);
        journalProd.setType(JournalVendeur.TYPE_QUANTITE);
        journalProdByEquipe.setCumulBy(1);
        journalProdByEquipe.setType(JournalVendeur.TYPE_QUANTITE);
        journalProdByTranche.setCumulBy(2);
        journalProdByTranche.setType(JournalVendeur.TYPE_QUANTITE);
    }

    public String getWhatValeurDisplay() {
        return whatValeurDisplay;
    }

    public void setWhatValeurDisplay(String whatValeurDisplay) {
        this.whatValeurDisplay = whatValeurDisplay;
    }

    public List<YvsUsers> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<YvsUsers> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public String getEditeurs() {
        return editeurs;
    }

    public void setEditeurs(String editeurs) {
        this.editeurs = editeurs;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public String getValoriseMp() {
        return valoriseMp;
    }

    public void setValoriseMp(String valoriseMp) {
        this.valoriseMp = valoriseMp;
    }

    public String getValoriseMs() {
        return valoriseMs;
    }

    public void setValoriseMs(String valoriseMs) {
        this.valoriseMs = valoriseMs;
    }

    public String getValorisePf() {
        return valorisePf;
    }

    public void setValorisePf(String valorisePf) {
        this.valorisePf = valorisePf;
    }

    public String getValorisePsf() {
        return valorisePsf;
    }

    public void setValorisePsf(String valorisePsf) {
        this.valorisePsf = valorisePsf;
    }

    public boolean isValoriseExcedent() {
        return valoriseExcedent;
    }

    public void setValoriseExcedent(boolean valoriseExcedent) {
        this.valoriseExcedent = valoriseExcedent;
    }

    public Dashboards getValorise() {
        return valorise;
    }

    public void setValorise(Dashboards valorise) {
        this.valorise = valorise;
    }

    public double getTotalListingFactureAchat() {
        return totalListingFactureAchat;
    }

    public void setTotalListingFactureAchat(double totalListingFactureAchat) {
        this.totalListingFactureAchat = totalListingFactureAchat;
    }

    public double getTotalListingAchat() {
        return totalListingAchat;
    }

    public void setTotalListingAchat(double totalListingAchat) {
        this.totalListingAchat = totalListingAchat;
    }

    public boolean isLoadSalarial() {
        return loadSalarial;
    }

    public void setLoadSalarial(boolean loadSalarial) {
        this.loadSalarial = loadSalarial;
    }

    public boolean isLoadBonProvisoire() {
        return loadBonProvisoire;
    }

    public void setLoadBonProvisoire(boolean loadBonProvisoire) {
        this.loadBonProvisoire = loadBonProvisoire;
    }

    public boolean isLoadSoldeCaisse() {
        return loadSoldeCaisse;
    }

    public void setLoadSoldeCaisse(boolean loadSoldeCaisse) {
        this.loadSoldeCaisse = loadSoldeCaisse;
    }

    public boolean isLoadClassementMS() {
        return loadClassementMS;
    }

    public void setLoadClassementMS(boolean loadClassementMS) {
        this.loadClassementMS = loadClassementMS;
    }

    public boolean isLoadClassementPF() {
        return loadClassementPF;
    }

    public void setLoadClassementPF(boolean loadClassementPF) {
        this.loadClassementPF = loadClassementPF;
    }

    public boolean isLoadClassementPoint() {
        return loadClassementPoint;
    }

    public void setLoadClassementPoint(boolean loadClassementPoint) {
        this.loadClassementPoint = loadClassementPoint;
    }

    public boolean isLoadClassementVendeur() {
        return loadClassementVendeur;
    }

    public void setLoadClassementVendeur(boolean loadClassementVendeur) {
        this.loadClassementVendeur = loadClassementVendeur;
    }

    public boolean isLoadClassementClient() {
        return loadClassementClient;
    }

    public void setLoadClassementClient(boolean loadClassementClient) {
        this.loadClassementClient = loadClassementClient;
    }

    public boolean isLoadSoldeFournisseur() {
        return loadSoldeFournisseur;
    }

    public void setLoadSoldeFournisseur(boolean loadSoldeFournisseur) {
        this.loadSoldeFournisseur = loadSoldeFournisseur;
    }

    public double getMasseSalarialMoisPrec() {
        return masseSalarialMoisPrec;
    }

    public void setMasseSalarialMoisPrec(double masseSalarialMoisPrec) {
        this.masseSalarialMoisPrec = masseSalarialMoisPrec;
    }

    public double getMasseSalarialAnnuel() {
        return masseSalarialAnnuel;
    }

    public void setMasseSalarialAnnuel(double masseSalarialAnnuel) {
        this.masseSalarialAnnuel = masseSalarialAnnuel;
    }

    public double getCotisationSalarialMoisPrec() {
        return cotisationSalarialMoisPrec;
    }

    public void setCotisationSalarialMoisPrec(double cotisationSalarialMoisPrec) {
        this.cotisationSalarialMoisPrec = cotisationSalarialMoisPrec;
    }

    public double getCotisationSalarialAnnuel() {
        return cotisationSalarialAnnuel;
    }

    public void setCotisationSalarialAnnuel(double cotisationSalarialAnnuel) {
        this.cotisationSalarialAnnuel = cotisationSalarialAnnuel;
    }

    public double getTaxeSalarialMoisPrec() {
        return taxeSalarialMoisPrec;
    }

    public void setTaxeSalarialMoisPrec(double taxeSalarialMoisPrec) {
        this.taxeSalarialMoisPrec = taxeSalarialMoisPrec;
    }

    public double getTaxeSalarialAnnuel() {
        return taxeSalarialAnnuel;
    }

    public void setTaxeSalarialAnnuel(double taxeSalarialAnnuel) {
        this.taxeSalarialAnnuel = taxeSalarialAnnuel;
    }

    public Dashboards getSoldesFssseurs() {
        return soldesFssseurs;
    }

    public void setSoldesFssseurs(Dashboards soldesFssseurs) {
        this.soldesFssseurs = soldesFssseurs;
    }

    public Dashboards getSoldeCaisses() {
        return soldeCaisses;
    }

    public void setSoldeCaisses(Dashboards soldeCaisses) {
        this.soldeCaisses = soldeCaisses;
    }

    public List<Object[]> getListing_facture_achat() {
        return listing_facture_achat;
    }

    public void setListing_facture_achat(List<Object[]> listing_facture_achat) {
        this.listing_facture_achat = listing_facture_achat;
    }

    public String getAfficherPar() {
        return afficherPar;
    }

    public void setAfficherPar(String afficherPar) {
        this.afficherPar = afficherPar;
    }

    public Dashboards getListing_achat() {
        return listing_achat;
    }

    public void setListing_achat(Dashboards listing_achat) {
        this.listing_achat = listing_achat;
    }

    public PaginatorResult<YvsComContenuDocAchat> getP_listing_achat() {
        return p_listing_achat;
    }

    public void setP_listing_achat(PaginatorResult<YvsComContenuDocAchat> p_listing_achat) {
        this.p_listing_achat = p_listing_achat;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<YvsComContenuDocAchat> getListings_achat() {
        return listings_achat;
    }

    public void setListings_achat(List<YvsComContenuDocAchat> listings_achat) {
        this.listings_achat = listings_achat;
    }

    public YvsComDocAchats getAchat() {
        return achat;
    }

    public void setAchat(YvsComDocAchats achat) {
        this.achat = achat;
    }

    public String getArticles() {
        return articles;
    }

    public void setArticles(String articles) {
        this.articles = articles;
    }

    public Dashboards getProdConsoEquipe() {
        return prodConsoEquipe;
    }

    public void setProdConsoEquipe(Dashboards prodConsoEquipe) {
        this.prodConsoEquipe = prodConsoEquipe;
    }

    public long getUnite() {
        return unite;
    }

    public void setUnite(long unite) {
        this.unite = unite;
    }

    public Dashboards getProdConso() {
        return prodConso;
    }

    public void setProdConso(Dashboards prodConso) {
        this.prodConso = prodConso;
    }

    public long getSite() {
        return site;
    }

    public void setSite(long site) {
        this.site = site;
    }

    public Dashboards getRecapOf() {
        return recapOf;
    }

    public void setRecapOf(Dashboards recapOf) {
        this.recapOf = recapOf;
    }

    public Dashboards getRecapOfByEquipe() {
        return recapOfByEquipe;
    }

    public void setRecapOfByEquipe(Dashboards recapOfByEquipe) {
        this.recapOfByEquipe = recapOfByEquipe;
    }

    public Dashboards getRecapOfByTranche() {
        return recapOfByTranche;
    }

    public void setRecapOfByTranche(Dashboards recapOfByTranche) {
        this.recapOfByTranche = recapOfByTranche;
    }

    public Dashboards getRation() {
        return ration;
    }

    public void setRation(Dashboards ration) {
        this.ration = ration;
    }

    public Dashboards getCreanceClient() {
        return creanceClient;
    }

    public void setCreanceClient(Dashboards creanceClient) {
        this.creanceClient = creanceClient;
    }

    public Dashboards getTabArticleStock() {
        return tabArticleStock;
    }

    public void setTabArticleStock(Dashboards tabArticleStock) {
        this.tabArticleStock = tabArticleStock;
    }

    public List<YvsBaseModeReglement> getModels() {
        return models;
    }

    public void setModels(List<YvsBaseModeReglement> models) {
        this.models = models;
    }

    public StatistiqueGeneral getCommercial() {
        return commercial;
    }

    public void setCommercial(StatistiqueGeneral commercial) {
        this.commercial = commercial;
    }

    public Dashboards getTabPointAgence() {
        return tabPointAgence;
    }

    public void setTabPointAgence(Dashboards tabPointAgence) {
        this.tabPointAgence = tabPointAgence;
    }

    public Dashboards getTabAgence() {
        return tabAgence;
    }

    public void setTabAgence(Dashboards tabAgence) {
        this.tabAgence = tabAgence;
    }

    public Dashboards getTabArticleAgence() {
        return tabArticleAgence;
    }

    public void setTabArticleAgence(Dashboards tabArticleAgence) {
        this.tabArticleAgence = tabArticleAgence;
    }

    public Dashboards getTabArticlePv() {
        return tabArticlePv;
    }

    public void setTabArticlePv(Dashboards tabArticlePv) {
        this.tabArticlePv = tabArticlePv;
    }

    public Dashboards getTabArticleVendeur() {
        return tabArticleVendeur;
    }

    public void setTabArticleVendeur(Dashboards tabArticleVendeur) {
        this.tabArticleVendeur = tabArticleVendeur;
    }

    public Dashboards getTabArticleClient() {
        return tabArticleClient;
    }

    public void setTabArticleClient(Dashboards tabArticleClient) {
        this.tabArticleClient = tabArticleClient;
    }

    public Long getCountTransfert() {
        return countTransfert;
    }

    public void setCountTransfert(Long countTransfert) {
        this.countTransfert = countTransfert;
    }

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }

    public String getCodeFsseur() {
        return codeFsseur;
    }

    public void setCodeFsseur(String codeFsseur) {
        this.codeFsseur = codeFsseur;
    }

    public int getNombreEtat() {
        return nombreEtat;
    }

    public void setNombreEtat(int nombreEtat) {
        this.nombreEtat = nombreEtat;
    }

    public int getEcartAge() {
        return ecartAge;
    }

    public void setEcartAge(int ecartAge) {
        this.ecartAge = ecartAge;
    }

    public boolean isChoixClient() {
        return choixClient;
    }

    public void setChoixClient(boolean choixClient) {
        this.choixClient = choixClient;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getComptes() {
        return comptes;
    }

    public void setComptes(String comptes) {
        this.comptes = comptes;
    }

    public String getNatureProgress() {
        return natureProgress;
    }

    public void setNatureProgress(String natureProgress) {
        this.natureProgress = natureProgress;
    }

    public String getTypeMontantPerf() {
        return typeMontantPerf;
    }

    public void setTypeMontantPerf(String typeMontantPerf) {
        this.typeMontantPerf = typeMontantPerf;
    }

    public String getTypeMontantComp() {
        return typeMontantComp;
    }

    public void setTypeMontantComp(String typeMontantComp) {
        this.typeMontantComp = typeMontantComp;
    }

    public String getTypeMontantSuiv() {
        return typeMontantSuiv;
    }

    public void setTypeMontantSuiv(String typeMontantSuiv) {
        this.typeMontantSuiv = typeMontantSuiv;
    }

    public String getTypeMontantSuivF() {
        return typeMontantSuivF;
    }

    public void setTypeMontantSuivF(String typeMontantSuivF) {
        this.typeMontantSuivF = typeMontantSuivF;
    }

    public long getExercice() {
        return exercice;
    }

    public void setExercice(long exercice) {
        this.exercice = exercice;
    }

    public int getColonneAge() {
        return colonneAge;
    }

    public void setColonneAge(int colonneAge) {
        this.colonneAge = colonneAge;
    }

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public boolean isGrouper() {
        return grouper;
    }

    public void setGrouper(boolean grouper) {
        this.grouper = grouper;
    }

    public boolean isGrouperF() {
        return grouperF;
    }

    public void setGrouperF(boolean grouperF) {
        this.grouperF = grouperF;
    }

    public YvsBaseTiersTemplate getTemplate() {
        return template;
    }

    public void setTemplate(YvsBaseTiersTemplate template) {
        this.template = template;
    }

    public YvsBaseExercice getExo() {
        return exo;
    }

    public void setExo(YvsBaseExercice exo) {
        this.exo = exo;
    }

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<yvs.util.Options> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<yvs.util.Options> colonnes) {
        this.colonnes = colonnes;
    }

    public Object[][] getDonnees() {
        return donnees;
    }

    public void setDonnees(Object[][] donnees) {
        this.donnees = donnees;
    }

    public Users getVendeur() {
        return vendeur;
    }

    public void setVendeur(Users vendeur) {
        this.vendeur = vendeur;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public List<Object[]> getListing_facture() {
        return listing_facture;
    }

    public void setListing_facture(List<Object[]> listing_facture) {
        this.listing_facture = listing_facture;
    }

    public Dashboards getAge() {
        return age;
    }

    public void setAge(Dashboards age) {
        this.age = age;
    }

    public Dashboards getEtats() {
        return etats;
    }

    public void setEtats(Dashboards etats) {
        this.etats = etats;
    }

    public Dashboards getProgress() {
        return progress;
    }

    public void setProgress(Dashboards progress) {
        this.progress = progress;
    }

    public Dashboards getJournal() {
        return journal;
    }

    public void setJournal(Dashboards journal) {
        this.journal = journal;
    }

    public Dashboards getJournal_achat() {
        return journal_achat;
    }

    public void setJournal_achat(Dashboards journal_achat) {
        this.journal_achat = journal_achat;
    }

    public Dashboards getCaAchatPeriode() {
        return caAchatPeriode;
    }

    public void setCaAchatPeriode(Dashboards caAchatPeriode) {
        this.caAchatPeriode = caAchatPeriode;
    }

    public Dashboards getCaPeriode() {
        return caPeriode;
    }

    public void setCaPeriode(Dashboards caPeriode) {
        this.caPeriode = caPeriode;
    }

    public Dashboards getBonus() {
        return bonus;
    }

    public void setBonus(Dashboards bonus) {
        this.bonus = bonus;
    }

    public Dashboards getRistourne() {
        return ristourne;
    }

    public void setRistourne(Dashboards ristourne) {
        this.ristourne = ristourne;
    }

    public Dashboards getListingFsseur() {
        return listingFsseur;
    }

    public void setListingFsseur(Dashboards listingFsseur) {
        this.listingFsseur = listingFsseur;
    }

    public Dashboards getListingClient() {
        return listingClient;
    }

    public void setListingClient(Dashboards listingClient) {
        this.listingClient = listingClient;
    }

    public Dashboards getEcarts() {
        return ecarts;
    }

    public void setEcarts(Dashboards ecarts) {
        this.ecarts = ecarts;
    }

    public boolean isDisplayName() {
        return displayName;
    }

    public void setDisplayName(boolean displayName) {
        this.displayName = displayName;
    }

    public List<YvsComDocVentes> getListings() {
        return listings;
    }

    public void setListings(List<YvsComDocVentes> listings) {
        this.listings = listings;
    }

    public Dashboards getListing() {
        return listing;
    }

    public void setListing(Dashboards listing) {
        this.listing = listing;
    }

    public List<Montant[]> getValuesDashboard() {
        return valuesDashboard;
    }

    public void setValuesDashboard(List<Montant[]> valuesDashboard) {
        this.valuesDashboard = valuesDashboard;
    }

    public List<Object[]> getVentesClients() {
        return ventesClients;
    }

    public void setVentesClients(List<Object[]> ventesClients) {
        this.ventesClients = ventesClients;
    }

    public List<ParamDate> getColoneClient() {
        return coloneClient;
    }

    public void setColoneClient(List<ParamDate> coloneClient) {
        this.coloneClient = coloneClient;
    }

    public Montant[] getFooterTotaux() {
        return footerTotaux;
    }

    public void setFooterTotaux(Montant[] footerTotaux) {
        this.footerTotaux = footerTotaux;
    }

    public List<Long> getSelectionArticle() {
        return selectionArticle;
    }

    public void setSelectionArticle(List<Long> selectionArticle) {
        this.selectionArticle = selectionArticle;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public long getIdFsseur() {
        return idFsseur;
    }

    public void setIdFsseur(long idFsseur) {
        this.idFsseur = idFsseur;
    }

    public int getIndexClient() {
        return indexClient;
    }

    public void setIndexClient(int indexClient) {
        this.indexClient = indexClient;
    }

    public int getIndexFsseur() {
        return indexFsseur;
    }

    public void setIndexFsseur(int indexFsseur) {
        this.indexFsseur = indexFsseur;
    }

    public double getTotalCaByPeriode() {
        return totalCaByPeriode;
    }

    public void setTotalCaByPeriode(double totalCaByPeriode) {
        this.totalCaByPeriode = totalCaByPeriode;
    }

    public double getTotalCaByArticle() {
        return totalCaByArticle;
    }

    public void setTotalCaByArticle(double totalCaByArticle) {
        this.totalCaByArticle = totalCaByArticle;
    }

    public SyntheseClient getSynthese() {
        return synthese;
    }

    public void setSynthese(SyntheseClient synthese) {
        this.synthese = synthese;
    }

    public SyntheseFournisseur getSyntheseF() {
        return syntheseF;
    }

    public void setSyntheseF(SyntheseFournisseur syntheseF) {
        this.syntheseF = syntheseF;
    }

    public double getCreditFsseur() {
        return creditFsseur;
    }

    public void setCreditFsseur(double creditFsseur) {
        this.creditFsseur = creditFsseur;
    }

    public double getDebitFsseur() {
        return debitFsseur;
    }

    public void setDebitFsseur(double debitFsseur) {
        this.debitFsseur = debitFsseur;
    }

    public double getSoldeFsseur() {
        return soldeFsseur;
    }

    public void setSoldeFsseur(double soldeFsseur) {
        this.soldeFsseur = soldeFsseur;
    }

    public PaginatorResult<YvsComDocVentes> getP_listing() {
        return p_listing;
    }

    public void setP_listing(PaginatorResult<YvsComDocVentes> p_listing) {
        this.p_listing = p_listing;
    }

    public List<Dashboard> getParametrages() {
        return parametrages;
    }

    public void setParametrages(List<Dashboard> parametrages) {
        this.parametrages = parametrages;
    }

    public Dashboards getJournalProd() {
        return journalProd;
    }

    public void setJournalProd(Dashboards journalProd) {
        this.journalProd = journalProd;
    }

    public Dashboards getJournalProdByEquipe() {
        return journalProdByEquipe;
    }

    public void setJournalProdByEquipe(Dashboards journalProdByEquipe) {
        this.journalProdByEquipe = journalProdByEquipe;
    }

    public Dashboards getJournalProdByTranche() {
        return journalProdByTranche;
    }

    public void setJournalProdByTranche(Dashboards journalProdByTranche) {
        this.journalProdByTranche = journalProdByTranche;
    }

    public Dashboards getConsommation() {
        return consommation;
    }

    public void setConsommation(Dashboards consommation) {
        this.consommation = consommation;
    }

    public Dashboards getSyntheseDist() {
        return syntheseDist;
    }

    public void setSyntheseDist(Dashboards syntheseDist) {
        this.syntheseDist = syntheseDist;
    }

    public Dashboards getProductionVente() {
        return productionVente;
    }

    public void setProductionVente(Dashboards productionVente) {
        this.productionVente = productionVente;
    }

    public Dashboards getEcart() {
        return ecart;
    }

    public void setEcart(Dashboards ecart) {
        this.ecart = ecart;
    }

    public String getEtat() {
        return etat != null ? etat : "";
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValoriserBy() {
        return valoriserBy;
    }

    public void setValoriserBy(String valoriserBy) {
        this.valoriserBy = valoriserBy;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public long getDepot() {
        return depot;
    }

    public void setDepot(long depot) {
        this.depot = depot;
    }

    public boolean isByFamille() {
        return byFamille;
    }

    public void setByFamille(boolean byFamille) {
        this.byFamille = byFamille;
    }

    public int getVueType() {
        return vueType;
    }

    public void setVueType(int vueType) {
        this.vueType = vueType;
    }

    public long getZone() {
        return zone;
    }

    public void setZone(long zone) {
        this.zone = zone;
    }

    public int getCumulBy() {
        return cumulBy;
    }

    public void setCumulBy(int cumulBy) {
        this.cumulBy = cumulBy;
    }

    public String getTypeListing() {
        return typeListing;
    }

    public void setTypeListing(String typeListing) {
        this.typeListing = typeListing;
    }

    public boolean isCumule() {
        return cumule;
    }

    public void setCumule(boolean cumule) {
        this.cumule = cumule;
    }

    public long getUsers() {
        return users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public boolean isByPoint() {
        return byPoint;
    }

    public void setByPoint(boolean byPoint) {
        this.byPoint = byPoint;
    }

    public String getTabNotActive() {
        return tabNotActive;
    }

    public void setTabNotActive(String tabNotActive) {
        this.tabNotActive = tabNotActive;
    }

    public List<Dashboard> getDashboards() {
        return dashboards;
    }

    public void setDashboards(List<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }

    public List<YvsStatDashboardUsers> getParametres() {
        return parametres;
    }

    public void setParametres(List<YvsStatDashboardUsers> parametres) {
        this.parametres = parametres;
    }

    public List<Dashboards.InstanceDashboards> getSaves() {
        return saves;
    }

    public void setSaves(List<Dashboards.InstanceDashboards> saves) {
        this.saves = saves;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getClasse() {
        return classe;
    }

    public void setClasse(long classe) {
        this.classe = classe;
    }

    public long getFamille() {
        return famille;
    }

    public void setFamille(long famille) {
        this.famille = famille;
    }

    public Dashboards getTabMargeClasse() {
        return tabMargeClasse;
    }

    public void setTabMargeClasse(Dashboards tabMargeClasse) {
        this.tabMargeClasse = tabMargeClasse;
    }

    public Dashboards getTabMargePoint() {
        return tabMargePoint;
    }

    public void setTabMargePoint(Dashboards tabMargePoint) {
        this.tabMargePoint = tabMargePoint;
    }

    public Dashboards getTabMargeFamille() {
        return tabMargeFamille;
    }

    public void setTabMargeFamille(Dashboards tabMargeFamille) {
        this.tabMargeFamille = tabMargeFamille;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupJustifyBy() {
        return groupJustifyBy;
    }

    public void setGroupJustifyBy(String groupJustifyBy) {
        this.groupJustifyBy = groupJustifyBy;
    }

    public String getGroupImpayeBy() {
        return groupImpayeBy;
    }

    public void setGroupImpayeBy(String groupImpayeBy) {
        this.groupImpayeBy = groupImpayeBy;
    }

    public List<Object[]> getBonNotJustifier() {
        return bonNotJustifier;
    }

    public void setBonNotJustifier(List<Object[]> bonNotJustifier) {
        this.bonNotJustifier = bonNotJustifier;
    }

    public List<Object[]> getImpayesAchat() {
        return impayesAchat;
    }

    public void setImpayesAchat(List<Object[]> impayesAchat) {
        this.impayesAchat = impayesAchat;
    }

    public Dashboards getSoldeClients() {
        return soldeClients;
    }

    public void setSoldeClients(Dashboards soldeClients) {
        this.soldeClients = soldeClients;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Dashboards getTabMargeMs() {
        return tabMargeMs;
    }

    public void setTabMargeMs(Dashboards tabMargeMs) {
        this.tabMargeMs = tabMargeMs;
    }

    public Dashboards getTabMargePf() {
        return tabMargePf;
    }

    public void setTabMargePf(Dashboards tabMargePf) {
        this.tabMargePf = tabMargePf;
    }

    public Date getDebutP() {
        return debutP;
    }

    public void setDebutP(Date debutP) {
        this.debutP = debutP;
    }

    public Date getFinP() {
        return finP;
    }

    public void setFinP(Date finP) {
        this.finP = finP;
    }

    public Date getJourP() {
        return jourP;
    }

    public void setJourP(Date jourP) {
        this.jourP = jourP;
    }

    public List<YvsComptaBonProvisoire> getBonsProvisoires() {
        return bonsProvisoires;
    }

    public void setBonsProvisoires(List<YvsComptaBonProvisoire> bonsProvisoires) {
        this.bonsProvisoires = bonsProvisoires;
    }

    public List<ValeursEtat> getGenerales() {
        return generales;
    }

    public void setGenerales(List<ValeursEtat> generales) {
        this.generales = generales;
    }

    public long getAgence() {
        return agence;
    }

    public void setAgence(long agence) {
        this.agence = agence;
    }

    public Dashboards getTabVendeur() {
        return tabVendeur;
    }

    public void setTabVendeur(Dashboards tabVendeur) {
        this.tabVendeur = tabVendeur;
    }

    public Dashboards getTabClients() {
        return tabClients;
    }

    public void setTabClients(Dashboards tabClients) {
        this.tabClients = tabClients;
    }

    public List<YvsBaseCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsBaseCaisse> caisses) {
        this.caisses = caisses;
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean addVendeur() {
        return options.contains("-V");
    }

    public boolean addClient() {
        return options.contains("-L");
    }

    public boolean addCumule() {
        return options.contains("-U");
    }

    public boolean addCumulerBy() {
        return options.contains("-B");
    }

    public boolean addZone() {
        return options.contains("-Z");
    }

    public boolean addPrixLower() {
        return options.contains("-W");
    }

    public boolean addArticle() {
        return options.contains("-A");
    }

    public boolean addNature() {
        return options.contains("-N");
    }

    public boolean addFamille() {
        return options.contains("-F");
    }

    public boolean addClasse() {
        return options.contains("-C");
    }

    public boolean addPoint() {
        return options.contains("-P");
    }

    public boolean addSite() {
        return options.contains("-S");
    }

    public boolean addDepot() {
        return options.contains("-D");
    }

    public boolean addCategorie() {
        return options.contains("-CA");
    }

    public boolean addGroupeBy() {
        return options.contains("-GP");
    }

    public boolean addByFamille() {
        return options.contains("-BF");
    }

    public boolean addByPoint() {
        return options.contains("-BP");
    }

    public boolean addValoriserBy() {
        return options.contains("-VB");
    }

    private void loadVariable() {
        AccesPage acces = (AccesPage) giveManagedBean(AccesPage.class);
        parametrages.add(new Dashboard("MARGE_MS_ARTICLE", "../com/sub/marges/yvs_marge_ms_article.xhtml", "-F,-C,-P", (acces != null ? acces.isStat_marge_article() : false)));
        parametrages.add(new Dashboard("MARGE_PF_ARTICLE", "../com/sub/marges/yvs_marge_pf_article.xhtml", "-F,-C,-P", (acces != null ? acces.isStat_marge_article() : false)));
        parametrages.add(new Dashboard("MARGE_CLASSE", "../com/sub/marges/yvs_marge_classe.xhtml", "-F,-C,-P", (acces != null ? acces.isStat_marge_article() : false)));
        parametrages.add(new Dashboard("MARGE_FAMILLE", "../com/sub/marges/yvs_marge_famille.xhtml", "-F,-C,-P", (acces != null ? acces.isStat_marge_article() : false)));
        parametrages.add(new Dashboard("MARGE_POINT", "../com/sub/marges/yvs_marge_point.xhtml", "-F,-C,-P", (acces != null ? acces.isStat_marge_article() : false)));

        parametrages.add(new Dashboard("CA_ARTICLE_AGENCE", "../com/sub/ca/yvs_ca_article_agence.xhtml", "", (acces != null ? acces.isStat_ca_article() : false)));
        parametrages.add(new Dashboard("CA_ARTICLE_POINT", "../com/sub/ca/yvs_ca_article_point.xhtml", "-P", (acces != null ? acces.isStat_ca_article() : false)));
        parametrages.add(new Dashboard("CA_ARTICLE_VENDEUR", "../com/sub/ca/yvs_ca_article_vendeur.xhtml", "-V", (acces != null ? acces.isStat_ca_article() : false)));
        parametrages.add(new Dashboard("CA_POINT_AGENCE", "../com/sub/ca/yvs_ca_point_agence.xhtml", "", (acces != null ? acces.isStat_ca_article() : false)));
        parametrages.add(new Dashboard("CA_VENDEUR_AGENCE", "../com/sub/ca/yvs_ca_vendeur_agence.xhtml", "", (acces != null ? acces.isStat_ca_article() : false)));
        parametrages.add(new Dashboard("CA_GENERAL", "../com/sub/ca/yvs_ca_general.xhtml", "", (acces != null ? acces.isStat_ca() : false)));
        parametrages.add(new Dashboard("CA_RESUME", "../com/sub/ca/yvs_ca_resume.xhtml", "", (acces != null ? acces.isStat_ca() : false)));

        parametrages.add(new Dashboard("LISTING_ARTICLE", "../com/sub/listing/yvs_listing_article.xhtml", "-V,-L,-U,-W,-Z,-B", (acces != null ? acces.isStat_listing_vente_article() : false)));
        parametrages.add(new Dashboard("LISTING_CLIENT", "../com/sub/listing/yvs_listing_client.xhtml", "-V,-L,-U,-W,-Z,-B", (acces != null ? acces.isStat_listing_vente_client() : false)));
        parametrages.add(new Dashboard("LISTING_FACTURE", "../com/sub/listing/yvs_listing_facture.xhtml", "-V,-L,-U,-W,-Z,-B", (acces != null ? acces.isStat_listing_vente() : false)));
        parametrages.add(new Dashboard("LISTING_GROUPE", "../com/sub/listing/yvs_listing_groupe.xhtml", "-V,-L,-U,-W,-Z,-B", (acces != null ? acces.isStat_listing_vente() : false)));
        parametrages.add(new Dashboard("LISTING_RISTOURNE", "../com/sub/listing/yvs_listing_ristourne.xhtml", "-V,-L,-Z,-B", (acces != null ? acces.isStat_ristourne_client() : false)));
        parametrages.add(new Dashboard("LISTING_IMPAYE_ACHAT", "../com/sub/listing/yvs_listing_impaye_achat.xhtml", "-V,-L,-Z,-B", (acces != null ? acces.isStat_facture_achat_impaye() : false)));

        parametrages.add(new Dashboard("CLASSEMENT_CLIENT", "../com/sub/classement/yvs_classement_client.xhtml", "", (acces != null ? acces.isStat_classement_client() : false)));
        parametrages.add(new Dashboard("CLASSEMENT_VENDEUR", "../com/sub/classement/yvs_classement_vendeur.xhtml", "", (acces != null ? acces.isStat_classement_vendeur() : false)));

        parametrages.add(new Dashboard("SYNTHESE_DISTRIBUTION", "../com/sub/stock/yvs_synthese_distribution.xhtml", "-D", (acces != null ? (acces.isStat_distribution_stock() || acces.isStat_synthese_production()) : false)));
        parametrages.add(new Dashboard("VALORISATION_STOCK", "../com/sub/stock/yvs_valorisation_stock.xhtml", "-CA,-GP", (acces != null ? acces.isStat_valorisation_stock() : false)));

        parametrages.add(new Dashboard("CREANCE_VENDEUR", "../com/sub/journal/yvs_creance_vendeur.xhtml", "-U,-V", (acces != null ? acces.isStat_creance_vendeur() : false)));
        parametrages.add(new Dashboard("JOURNAL_VENTE", "../com/sub/journal/yvs_journal_vente.xhtml", "-BF", (acces != null ? acces.isStat_journal_vente_vendeur() : false)));
        parametrages.add(new Dashboard("ECART_VENDEUR", "../com/sub/journal/yvs_ecart_vendeur.xhtml", "-V,-BP", (acces != null ? acces.isStat_ecart_vendeur() : false)));

        parametrages.add(new Dashboard("BON_ENCOURS", "../compta/sub/caisse/yvs_bon_encours.xhtml", "", (acces != null ? acces.isStat_bon_provisoire_encours() : false)));
        parametrages.add(new Dashboard("SOLDE_CAISSE", "../compta/sub/caisse/yvs_solde_caisse.xhtml", "", (acces != null ? acces.isStat_solde_caisse() : false)));
        parametrages.add(new Dashboard("SOLDE_BANQUE", "../compta/sub/caisse/yvs_solde_banque.xhtml", "", (acces != null ? acces.isStat_solde_caisse() : false)));

        parametrages.add(new Dashboard("JOURNAL_PRODUCTION", "../production/sub/journal/yvs_journal_production.xhtml", "-D,-VB,-CA,-N", (acces != null ? acces.isStat_journal_production() : false)));
        parametrages.add(new Dashboard("JOURNAL_PRODUCTION_EQUIPE", "../production/sub/journal/yvs_journal_production_equipe.xhtml", "-D,-VB,-CA,-N", (acces != null ? acces.isStat_journal_production() : false)));
        parametrages.add(new Dashboard("JOURNAL_PRODUCTION_TRANCHE", "../production/sub/journal/yvs_journal_production_tranche.xhtml", "-D,-VB,-CA,-N", (acces != null ? acces.isStat_journal_production() : false)));
        parametrages.add(new Dashboard("RECAPITULATIF_OF", "../production/sub/journal/yvs_recapitulatif_of.xhtml", "-S,-VB,-CA,-B", (acces != null ? acces.isStat_journal_production() : false)));
        parametrages.add(new Dashboard("CONSOMMATION_PRODUCTION", "../production/sub/journal/yvs_consommation_production.xhtml", "-VB,-A", (acces != null ? acces.isStat_journal_production() : false)));
        parametrages.add(new Dashboard("PRODUCTION_CONSOMMATION_EQUIPE", "../production/sub/journal/yvs_production_consommation_equipe.xhtml", "-D,-VB,-S", (acces != null ? acces.isStat_journal_production() : false)));

        parametrages.add(new Dashboard("SYNTHESE_CONSO_MP", "../production/sub/synthese/yvs_synthese_conso_mp.xhtml", "-D", (acces != null ? acces.isStat_synthese_production() : false)));

        parametrages.add(new Dashboard("ECART_PRODUCTION", "../production/sub/general/yvs_ecart_inventaire.xhtml", "-U", (acces != null ? acces.isStat_ecart_production() : false)));
        parametrages.add(new Dashboard("PRODUCTION_VENTE", "../production/sub/general/yvs_production_vente.xhtml", "-VB", (acces != null ? acces.isStat_production_vente() : false)));
    }

    public void chooseDepot() {
        if (depot > 0) {
            ManagedDepot externe = (ManagedDepot) giveManagedBean("managedDepot");
            if (externe != null) {
                int index = externe.getDepots_all().indexOf(new YvsBaseDepots(depot));
                if (index > -1) {
                    selectedDepot = externe.getDepots_all().get(index);
                }
            }
        } else {
            selectedDepot = new YvsBaseDepots();
        }
    }

    public void chooseUser(ValueChangeEvent ev) {
        if (ev == null) {
            return;
        }
        long newValue = (long) ev.getNewValue();
        if (newValue > 0) {
            int index = selectedUsers.indexOf(new YvsUsers(newValue));
            if (index < 0) {
                ManagedUser externe = (ManagedUser) giveManagedBean("managedUser");
                if (externe != null) {
                    index = externe.getListAllUser().indexOf(new YvsUsers(newValue));
                    if (index > -1) {
                        YvsUsers selected = externe.getListAllUser().get(index);
                        selectedUsers.add(selected);
                    }
                }
            }
        } else {
            long oldValue = (long) ev.getNewValue();
            int index = selectedUsers.indexOf(new YvsUsers(oldValue));
            if (index > -1) {
                selectedUsers.remove(index);
            }
        }
    }

    public boolean displayMoreOption() {
        if (addClasse() || addFamille() || addPoint() || addVendeur() || addClient() || addCumule() || addCumulerBy() || addZone() || addPrixLower()
                || addDepot() || addCategorie() || addByFamille() || addGroupeBy() || addByPoint() || addValoriserBy() || addSite() || addArticle() || addNature()) {
            return true;
        }
        return false;
    }

    @Override
    public void loadAll() {
        if (agence < 1) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            dateDebut = c.getTime();
            dateFin = new Date();
            //Calcule le même moi de l'année dernière en partant de date fin
            c.setTime(dateDebut);
            c.add(Calendar.YEAR, -1);
            debutP = c.getTime();
            c.setTime(dateFin);
            c.add(Calendar.YEAR, -1);
            finP = c.getTime();
            // jour
            jourP = finP;
        }
        if (agence < 1) {
            agence = currentAgence.getId();
        }
        if (currentAgence.getSociete() != null) {
            journal.setSociete(currentAgence.getSociete().getId());
        }
        if (currentAgence != null ? journal.getAgence() < 1 : false) {
            journal.setAgence(currentAgence.getId());
        }
        loadVariable();
        dashboards.clear();
        tabNotActive = "";
        options = "";
        ManagedParamProd ws = (ManagedParamProd) giveManagedBean(ManagedParamProd.class);
        if (ws != null) {
            valoriserBy = ws.getCurrentParam().getValoriserBy();
        }
        List<YvsStatDashboardUsers> list = dao.loadNameQueries("YvsStatDashboardUsers.findByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
        for (int i = 0; i < list.size(); i++) {
            addDashboard(list.get(i), i);
        }
        nombreEtat = list.size();
        if (addClasse()) {
            ManagedClasseStat w = (ManagedClasseStat) giveManagedBean(ManagedClasseStat.class);
            if (w != null) {
                w.loadAll();
            }
        }
        if (addFamille()) {
            ManagedFamilleArticle w = (ManagedFamilleArticle) giveManagedBean(ManagedFamilleArticle.class);
            if (w != null) {
                w.loadAlls();
            }
        }
        if (addPoint()) {
            ManagedPointVente w = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (w != null) {
                w.loadAllPointVente();
            }
        }
        if (addVendeur()) {
            ManagedUser w = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (w != null) {
                w.loadAllUserSociete_();
            }
        }
        if (addZone()) {
            ManagedPointLivraison w = (ManagedPointLivraison) giveManagedBean(ManagedPointLivraison.class);
            if (w != null) {
                w.loadAll();
            }
        }
        if (addDepot()) {
            ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (w != null) {
                w.loadAllDepot();
            }
        }
        if (addSite()) {
            ManagedSiteProduction w = (ManagedSiteProduction) giveManagedBean(ManagedSiteProduction.class);
            if (w != null) {
                w.loadAll();
            }
        }
        models = dao.loadNameQueries("YvsBaseModeReglement.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        update("dashboard");
    }

    private void addDashboard(YvsStatDashboardUsers p, int position) {
        int index = parametrages.indexOf(new Dashboard(p.getDashboard().getCode()));
        if (index > -1) {
            Dashboard d = parametrages.get(index);
            d.setGroupe(p.getDashboard().getGroupe());
            if (Util.asString(d.getOptions())) {
                if (Util.asString(options)) {
                    options += "," + d.getOptions();
                } else {
                    options = d.getOptions();
                }
            }
            dashboards.add(d);
            if (!p.getActive()) {
                if (Util.asString(tabNotActive)) {
                    tabNotActive += "," + position;
                } else {
                    tabNotActive = "" + position;
                }
            }
        }
    }

    long id = -100000;

    public void loadParametres() {
        parametres.clear();
        List<YvsStatDashboard> list = dao.loadNameQueries("YvsStatDashboard.findAll", new String[]{}, new Object[]{});
        YvsStatDashboardUsers d;
        boolean acces = true;
        for (YvsStatDashboard p : list) {
            d = (YvsStatDashboardUsers) dao.loadOneByNameQueries("YvsStatDashboardUsers.findOne", new String[]{"users", "dashboard"}, new Object[]{currentUser.getUsers(), p});
            if (d != null ? d.getId() < 1 : true) {
                d = new YvsStatDashboardUsers(id--);
                d.setAuthor(currentUser);
                d.setDashboard(p);
                d.setUsers(currentUser.getUsers());
            }
            int index = parametrages.indexOf(new Dashboard(p.getCode()));
            if (index > -1) {
                acces = parametrages.get(index).isAcces();
            }
            d.setAcces(acces);
            parametres.add(d);
        }
    }

    public void actionParametre(YvsStatDashboardUsers y) {
        if (y != null) {
            long id = y.getId();
            if (y.getId() < 1) {
                y.setId(null);
                y = (YvsStatDashboardUsers) dao.save1(y);
                addDashboard(y, dashboards.size() - 1);
                nombreEtat++;
            } else {
                dao.delete(y);
                y.setId(this.id--);
                dashboards.remove(new Dashboard(y.getDashboard().getCode()));
                nombreEtat--;
            }
            int index = parametres.indexOf(new YvsStatDashboardUsers(id));
            if (index > -1) {
                parametres.set(index, y);
            }
        }
    }

    public void activeParametre(YvsStatDashboardUsers y) {
        if (y != null) {
            y.setActive(!y.getActive());
            dao.update(y);
            int index = parametres.indexOf(y);
            if (index > -1) {
                if (!y.getActive()) {
                    if (Util.asString(tabNotActive)) {
                        tabNotActive += "," + index;
                    } else {
                        tabNotActive = "" + index;
                    }
                } else {
                    String[] tabs = tabNotActive.split(",");
                    tabNotActive = "";
                    for (String tab : tabs) {
                        if (!tab.equals(index + "")) {
                            if (Util.asString(tabNotActive)) {
                                tabNotActive += "," + tab;
                            } else {
                                tabNotActive = "" + tab;
                            }
                        }
                    }
                }
                parametres.set(index, y);
            }
        }
    }

    //*** BEGIN BASE ***//
    public void loadArticlesSelect() {
        ManagedArticles w = (ManagedArticles) giveManagedBean("Marticle");
        if (w != null) {
            switch (etat) {
                case "MP": {
                    consommation.setComptes("");
                    for (YvsBaseArticles a : w.getSelectArticles()) {
                        if (Util.asString(consommation.getComptes())) {
                            consommation.setComptes(consommation.getComptes() + "-" + a.getId());
                        } else {
                            consommation.setComptes(a.getId() + "");
                        }
                    }
                    if (Util.asString(consommation.getComptes())) {
                        consommation.setLength(consommation.getComptes().split("-").length);
                    } else {
                        consommation.setLength(0);
                    }
                    update("label-nombre_article_select_mp");
                    break;
                }
                case "PV": {
                    productionVente.setComptes("");
                    for (YvsBaseArticles a : w.getSelectArticles()) {
                        if (Util.asString(productionVente.getComptes())) {
                            productionVente.setComptes(productionVente.getComptes() + "-" + a.getId());
                        } else {
                            productionVente.setComptes(a.getId() + "");
                        }
                    }
                    if (Util.asString(productionVente.getComptes())) {
                        productionVente.setLength(productionVente.getComptes().split("-").length);
                    } else {
                        productionVente.setLength(0);
                    }
                    break;
                }
                case "AA": {
                    tabArticleAgence.setComptes("");
                    for (YvsBaseArticles a : w.getSelectArticles()) {
                        if (Util.asString(tabArticleAgence.getComptes())) {
                            tabArticleAgence.setComptes(tabArticleAgence.getComptes() + "," + a.getRefArt());
                        } else {
                            tabArticleAgence.setComptes(a.getRefArt() + "");
                        }
                    }
                    if (Util.asString(tabArticleAgence.getComptes())) {
                        tabArticleAgence.setLength(tabArticleAgence.getComptes().split(",").length);
                    } else {
                        tabArticleAgence.setLength(0);
                    }
                    break;
                }
                case "AP": {
                    tabArticlePv.setComptes("");
                    for (YvsBaseArticles a : w.getSelectArticles()) {
                        if (Util.asString(tabArticlePv.getComptes())) {
                            tabArticlePv.setComptes(tabArticlePv.getComptes() + "," + a.getRefArt());
                        } else {
                            tabArticlePv.setComptes(a.getRefArt() + "");
                        }
                    }
                    if (Util.asString(tabArticlePv.getComptes())) {
                        tabArticlePv.setLength(tabArticlePv.getComptes().split(",").length);
                    } else {
                        tabArticlePv.setLength(0);
                    }
                    break;
                }
                case "AV": {
                    tabArticleVendeur.setComptes("");
                    for (YvsBaseArticles a : w.getSelectArticles()) {
                        if (Util.asString(tabArticleVendeur.getComptes())) {
                            tabArticleVendeur.setComptes(tabArticleVendeur.getComptes() + "," + a.getRefArt());
                        } else {
                            tabArticleVendeur.setComptes(a.getRefArt() + "");
                        }
                    }
                    if (Util.asString(tabArticleVendeur.getComptes())) {
                        tabArticleVendeur.setLength(tabArticleVendeur.getComptes().split(",").length);
                    } else {
                        tabArticleVendeur.setLength(0);
                    }
                    break;
                }
                case "AC": {
                    tabArticleClient.setComptes("");
                    for (YvsBaseArticles a : w.getSelectArticles()) {
                        if (Util.asString(tabArticleClient.getComptes())) {
                            tabArticleClient.setComptes(tabArticleClient.getComptes() + "," + a.getRefArt());
                        } else {
                            tabArticleClient.setComptes(a.getRefArt() + "");
                        }
                    }
                    if (Util.asString(tabArticleClient.getComptes())) {
                        tabArticleClient.setLength(tabArticleClient.getComptes().split(",").length);
                    } else {
                        tabArticleClient.setLength(0);
                    }
                    break;
                }
                case "MG-MS":
                case "MG-PF": {
                    articles = "";
                    for (YvsBaseArticles a : w.getSelectArticles()) {
                        if (!yvs.util.Util.asString(articles)) {
                            articles = a.getRefArt();
                        } else {
                            articles += "," + a.getRefArt();
                        }
                    }
                    break;
                }
            }
            update("label-nombre_article_select");
        }
    }

    public void loadArticleSelect() {
        ManagedArticles w = (ManagedArticles) giveManagedBean("Marticle");
        if (w != null) {
            switch (etat) {
                case "ROF":
                    recapOf.setArticle(0);
                    if (w.getSelectArticle() != null ? w.getSelectArticle().getId() > 0 : false) {
                        recapOf.setArticle(w.getSelectArticle().getId());
                    }
                    break;
            }
            update("label-nombre_article_select");
        }
    }

    public void searchArticle() {
        String code = article.getRefArt();
        article = new Articles("", code);
        unite = 0;
        if (code != null ? code.trim().length() > 0 : false) {
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                Articles y = m.searchArticleActif("", code, true);
                if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                    if (m.getArticlesResult().size() > 1) {
                        update("board_data_articles");
                    } else {
                        chooseArticle(y);
                    }
                    article.setError(false);
                }
            }
        }
        update("blog_bord_article");
    }

    public void chooseArticle(Articles y) {
        article = y;
        if (article.getConditionnements() != null ? !article.getConditionnements().isEmpty() : false) {
            unite = article.getConditionnements().get(0).getId();
        }
    }

    public void loadDataMargeByClasse(long id, String categorie) {
        if (id > 0) {
            classe = id;
            point = 0;
            famille = 0;
            if (categorie.equals("PF")) {
                loadDataMarge(tabMargePf);
                update("data_marge_pf");
            } else {
                loadDataMarge(tabMargeMs);
                update("data_marge_ms");
            }
        }
    }

    public void loadDataMargeByPoint(long id, String categorie) {
        if (id > 0) {
            classe = 0;
            point = id;
            famille = 0;
            if (categorie.equals("PF")) {
                loadDataMarge(tabMargePf);
                update("data_marge_pf");
            } else {
                loadDataMarge(tabMargeMs);
                update("data_marge_ms");
            }
        }
    }

    public void loadDataMargeByFamille(long id, String categorie) {
        if (id > 0) {
            classe = 0;
            point = 0;
            famille = id;
            if (categorie.equals("PF")) {
                loadDataMarge(tabMargePf);
                update("data_marge_pf");
            } else {
                loadDataMarge(tabMargeMs);
                update("data_marge_ms");
            }
        }
    }

    public void reLoadDataMarge(Dashboards etat) {
        String element = famille + "" + point + "" + classe;
        Dashboards.InstanceDashboards save = new Dashboards.InstanceDashboards(element, etat.getNature(), etat.getCategorie());
        int index = saves.indexOf(save);
        if (index > -1) {
            switch (etat.getNature()) {
                case "A":
                    if (etat.getCategorie().equals("PF")) {
                        tabMargePf = saves.get(index).getSave();
                    } else {
                        tabMargeMs = saves.get(index).getSave();
                    }
                    break;
                case "C":
                    tabMargeClasse = saves.get(index).getSave();
                    break;
                case "P":
                    tabMargePoint = saves.get(index).getSave();
                    break;
                case "F":
                    tabMargeFamille = saves.get(index).getSave();
                    break;
            }
        }
    }

    //Charge le récap des vente par point de vente
    public void loadDataMargeCurrentExo(int limit, Dashboards etat) {
        if (etat.getCategorie().equals("MS") || etat.getCategorie().equals("MARCHANDISE")) {
            loadClassementMS = true;
        } else {
            loadClassementPF = true;
        }
        etat.setSociete(currentAgence.getSociete().getId());
        etat.setDateDebut(currentExo.getDateDebut());
        etat.setDateFin(currentExo.getDateFin());
        etat.setPeriode("");
        etat.returnArticles(limit, dao);
    }

    public void loadDataMarge(Dashboards etat) {
        try {
            if ((etat.getCategorie().equals("PF") && this.getEtat().equals("MG-PF")) || ((etat.getCategorie().equals("MS") || etat.getCategorie().equals("MARCHANDISE")) && this.getEtat().equals("MG-MS"))) {
                etat.setComptes(articles);
            }
            etat.setSociete(currentAgence.getSociete().getId());
            etat.setAgence(agence);
            etat.setDateDebut(dateDebut);
            etat.setDateFin(dateFin);
            etat.setPeriode(periode);
            etat.setFamille(famille);
            etat.setClasse(classe);
            etat.setPoint(point);
            etat.setVendeur(users);
            etat.returnArticles(dao);

            String element = famille + "" + point + "" + classe;
            Dashboards.InstanceDashboards save = new Dashboards.InstanceDashboards(element, etat.getNature(), etat.getCategorie(), etat.deepClone());
            int index = saves.indexOf(save);
            if (index > -1) {
                saves.set(index, save);
            } else {
                saves.add(save);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void downloadMarge(Dashboards etat) {
        downloadMarge(etat, "pdf");
    }

    public void downloadMarge(Dashboards etat, String format) {
        try {
            if ((etat.getCategorie().equals("PF") && this.etat.equals("MG-PF")) || ((etat.getCategorie().equals("MS") || etat.getCategorie().equals("MARCHANDISE")) && this.etat.equals("MG-MS"))) {
                etat.setComptes(articles);
            }
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);
            param.put("FAMILLE", (int) famille);
            param.put("CLASSE", (int) classe);
            param.put("POINT", (int) point);
            param.put("VENDEUR", 0);
            param.put("CLIENT", 0);
            param.put("TYPE", etat.getNature());
            param.put("REFERENCE", etat.getComptes());
            param.put("CATEGORIE", etat.getCategorie());
            param.put("DISPLAY_QTE", etat.isDisplayQte());
            param.put("DISPLAY_CA", etat.isDisplayCA());
            param.put("DISPLAY_REVIENT", etat.isDisplayRevient());
            param.put("DISPLAY_MARGE", etat.isDisplayMarge());
            param.put("DISPLAY_TAUX", etat.isDisplayTaux());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport(format.equals("xls") ? "dashboard_total_artilces_no_header" : "dashboard_total_artilces", param, "", format, false);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //*** END BASE ***//

    //*** BEGIN COMMERCIALE ***//
    public void loadDashbordGenerale() {
        dao.getEntityManager().clear();
        Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(agence, 2),
            new Options(dateDebut, 3), new Options(dateFin, 4), new Options(debutP, 5), new Options(finP, 6), new Options(jourP, 7)};
        String query = "SELECT  y.code, y.libelle,y.valeur, y.rang, y.lien FROM public.com_et_dashboard_generale(?,?,?,?,?,?,?) y ORDER BY y.rang";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            generales.clear();
            Object[] o;

            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    generales.add(new ValeursEtat((String) o[0], (String) o[1], (Double) o[2], 0d, "", "", 0d, 0, 0, 0, (Integer) o[3]));
                }
            }
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadCaPeriodes() {
        caPeriode.setSociete(currentAgence.getSociete().getId());
        caPeriode.setAgence(agence);
        caPeriode.setDateDebut(dateDebut);
        caPeriode.setDateFin(dateFin);
        caPeriode.setPeriode(periode);
        caPeriode.setComptes(comptes);
        caPeriode.setNature(nature);
        caPeriode.loadCaPeriodes(dao);
    }

    public void loadCaAchatPeriodes() {
        caAchatPeriode.setSociete(currentAgence.getSociete().getId());
        caAchatPeriode.setAgence(agence);
        caAchatPeriode.setDateDebut(dateDebut);
        caAchatPeriode.setDateFin(dateFin);
        caAchatPeriode.setPeriode(periode);
        caAchatPeriode.setComptes(comptes);
        caAchatPeriode.setNature(nature);
        caAchatPeriode.loadCaAchatPeriodes(dao);
    }

    public void loadJournauxVentes() {
        journal.setSociete(currentAgence.getSociete().getId());
        journal.setAgence(agence);
        journal.setDateDebut(dateDebut);
        journal.setDateFin(dateFin);
        journal.setPeriode(periode);
        journal.setByValue(byFamille);
        journal.setNature(afficherPar);
        journal.loadJournauxVentes(dao);
    }

    public void loadJournauxAchats() {
        journal_achat.setSociete(currentAgence.getSociete().getId());
        journal_achat.setAgence(agence);
        journal_achat.setDateDebut(dateDebut);
        journal_achat.setDateFin(dateFin);
        journal_achat.setPeriode(periode);
        journal_achat.setNature(afficherPar);
        journal_achat.setOrdres(groupBy);
        journal_achat.loadJournauxAchats(dao);
    }

    public void downloadJournal() {
        Map<String, Object> param = new HashMap<>();
        param.put("BY_FAMILLE", byFamille);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport("journal_vente", param);
    }

    public void downloadJournalAchat() {
        Map<String, Object> param = new HashMap<>();
        param.put("TYPE", afficherPar);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("PERIODE", periode);
        param.put("GROUP", groupBy);
        param.put("AGENCE", (int) agence);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport("journal_achat", param);
    }

    public void downloadJournal(String format) {
        Map<String, Object> param = new HashMap<>();
        param.put("BY_FAMILLE", byFamille);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        System.err.println("time = " + new Date().getTime());
        executeReport(format.equals("xls") ? "journal_vente_no_header" : "journal_vente", param, "", format, false);
    }

    public void downloadJournalAchat(String format) {
        Map<String, Object> param = new HashMap<>();
        param.put("TYPE", nature);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("PERIODE", periode);
        param.put("GROUP", groupBy);
        param.put("AGENCE", (int) agence);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport(format.equals("xls") ? "journal_achat_no_header" : "journal_achat", param, "", format, false);
    }

    public void loadEcartsVentes() {
        ecarts.setSociete(currentAgence.getSociete().getId());
        ecarts.setAgence(agence);
        ecarts.setDateDebut(dateDebut);
        ecarts.setDateFin(dateFin);
        ecarts.setVendeur(users);
        ecarts.setPoint(point);
        ecarts.setPeriode(periode);
        ecarts.setByValue(byPoint);
        ecarts.loadEcartsVentes(dao);

    }

    public void loadEcratVendeur() {
        //Calcul la date de début
        if (currentAgence != null) {
            ecarts.setSociete(currentAgence.getSociete().getId());
            ecarts.setAgence(agence);
            ecarts.setDateDebut(dateDebut);
            ecarts.setDateFin(dateFin);
            ecarts.setVendeur(currentUser.getUsers().getId());
            ecarts.setPoint(0);
            ecarts.setPeriode(periode);
            ecarts.setByValue(false);
            ecarts.loadEcartsVentes(dao);
        }
    }

    public void initDataCurrent() {
        users = currentUser.getUsers().getId();
        cumulBy = 0;
    }

    public void loadEcratVendeur_() {
        //Calcul la date de début
        if (currentAgence != null) {
            Integer dayInit = (Integer) dao.loadObjectByNameQueries("YvsComParametre.findInitJour", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            dayInit = (dayInit != null) ? dayInit : 1;
            Calendar c = Calendar.getInstance();
            int currentDay = c.get(Calendar.DAY_OF_MONTH);
            c.set(Calendar.DAY_OF_MONTH, dayInit);
            if (currentDay < dayInit) {
                c.add(Calendar.MONTH, -1);
            }
            dateDebut = c.getTime();
            c.add(Calendar.DAY_OF_MONTH, 30);
            dateFin = c.getTime();
            ecarts.setSociete(currentAgence.getSociete().getId());
            ecarts.setAgence(agence);
            ecarts.setDateDebut(dateDebut);
            ecarts.setDateFin(dateFin);
            ecarts.setVendeur(currentUser.getUsers().getId());
            ecarts.setPoint(0);
            ecarts.setPeriode(periode);
            ecarts.setByValue(false);
            ecarts.loadEcartsVentes(dao);
        }
    }

    public void downloadEcarts() {
        Map<String, Object> param = new HashMap<>();
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("PERIODE", periode);
        param.put("VENDEUR", (int) (byPoint ? point : users));
        param.put("BY_POINT", byPoint);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("USERS", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport("ecart_vendeur", param);
    }

    public void loadListingFacture() {
        Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(currentAgence.getId(), 2), new Options(dateDebut, 3), new Options(dateFin, 4)};
        String query = "SELECT e.date_entete, d.num_doc, CONCAT(CONCAT('[', y.code_client, ']'), ' ', d.nom_client), u.nom_users, get_ttc_vente(d.id) "
                + "FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id "
                + "INNER JOIN yvs_com_client y ON d.client = y.id INNER JOIN yvs_users u ON h.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id "
                + "INNER JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id INNER JOIN yvs_base_point_vente p ON cp.point = p.id "
                + "WHERE d.type_doc = 'FV' AND d.statut = 'V' AND a.societe = ? AND a.id = ? AND e.date_entete BETWEEN ? AND ? "
                + "ORDER BY e.date_entete, d.num_doc";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            listing_facture.clear();
            Object[] o;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Date date_entete = new Date(((java.sql.Date) o[0]).getTime());
                    String num_doc = (String) o[1];
                    String client = (String) o[2];
                    String users = (String) o[3];
                    Double ttc = (Double) o[4];
                    listing_facture.add(new Object[]{date_entete, num_doc, client, users, ttc});
                }
            }
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printListing() {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("CLIENT", null);
        param.put("POINT", point > 0 ? (int) point : null);
        param.put("VENDEUR", null);
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
        executeReport("listing_facture_vente", param);
    }

    public void printListing(String format) {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("CLIENT", null);
        param.put("POINT", point > 0 ? (int) point : null);
        param.put("VENDEUR", null);
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
//        executeReport("listing_facture_vente", param);
        executeReport("listing_facture_vente_no_header", param, "", format, false);

    }

    public void loadListingFactureAchat() {
        Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(currentAgence.getId(), 2), new Options(dateDebut, 3), new Options(dateFin, 4)};
        String query = "SELECT d.date_doc, d.num_doc, CONCAT(CONCAT('[', y.code_fsseur, ']'), ' ', CONCAT(y.nom, ' ', y.prenom)), e.designation, get_ttc_achat(d.id) "
                + "FROM yvs_com_doc_achats d INNER JOIN yvs_base_fournisseur y ON d.fournisseur = y.id INNER JOIN yvs_agences a ON d.agence = a.id "
                + "LEFT JOIN yvs_base_depots e ON d.depot_reception = e.id "
                + "WHERE d.type_doc = 'FA' AND d.statut = 'V' AND a.societe = ? AND a.id = ? AND d.date_doc BETWEEN ? AND ? "
                + "ORDER BY d.date_doc, d.num_doc";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            listing_facture_achat.clear();
            totalListingFactureAchat = 0;
            Object[] o;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Date date_doc = new Date(((java.sql.Date) o[0]).getTime());
                    String num_doc = (String) o[1];
                    String fournisseur = (String) o[2];
                    String depot = (String) o[3];
                    Double ttc = (Double) o[4];
                    totalListingFactureAchat += ttc != null ? ttc : 0;
                    listing_facture_achat.add(new Object[]{date_doc, num_doc, fournisseur, depot, ttc});
                }
            }
        } catch (NoResultException ex) {
            Logger.getLogger(Dashboards.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printListingFactureAchat() {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("FOURNISSEUR", null);
        param.put("DEPOT", depot > 0 ? (int) depot : null);
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR(true));
        executeReport("listing_facture_achat", param);
    }

    public void printListingFactureAchat(String format) {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("FOURNISSEUR", null);
        param.put("DEPOT", depot > 0 ? (int) depot : null);
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR(false));
        executeReport("listing_facture_achat_no_header", param, "", format, false);
    }

    public void recalculeRistourne() {
        if (!autoriser("rist_recaculer_ventes")) {
            openNotAcces();
            return;
        }
        String query = "SELECT com_recalcule_ristourne_periode(?,?,?,?,?,?) ";
        double total = dao.callFonction(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(agence, 2), new Options(client.getId(), 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(0, 6)});
        getInfoMessage("Traitement effectué.", " Total ristourne de la période : " + total);
    }

    public void recalculeRemise() {
        if (!autoriser("rist_recaculer_ventes")) {
            openNotAcces();
            return;
        }
        String query = "SELECT com_recalcule_remise_periode(?,?,?,?,?,?) ";
        double total = dao.callFonction(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(agence, 2), new Options(client.getId(), 3), new Options(dateDebut, 4), new Options(dateFin, 5), new Options(0, 6)});
        getInfoMessage("Traitement effectué.", "Total remise de la période : " + total);
    }

    public void loadRistournes() {
        if (!autoriser("rist_change_agence") && !currentAgence.getId().equals(agence)) {
            getErrorMessage("Vous ne pouvez pas modifier l'agence du calcul des ristourne");
            return;
        }
        ristourne.setSociete(currentAgence.getSociete().getId());
        ristourne.setAgence(agence);
        ristourne.setZone(zone);
        ristourne.setDateDebut(dateDebut);
        ristourne.setDateFin(dateFin);
        ristourne.setVendeur(users);
        ristourne.setClient(client.getId());
        ristourne.setPeriode(periode);
        ristourne.setCumulBy(cumulBy);
        ristourne.loadRistourneVentes(dao);
    }

    public void downloadRistournes(boolean total) {
        if (!autoriser("rist_change_agence") && !currentAgence.getId().equals(agence)) {
            getErrorMessage("Vous ne pouvez pas modifier l'agence du calcul des ristourne");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("PERIODE", total ? "" : periode);
        param.put("VENDEUR", (int) users);
        param.put("CLIENT", (int) client.getId());
        param.put("CUMULE", cumulBy);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport(total ? "ristourne_vente_total" : (cumulBy == 0 ? "ristourne_vente_with_facture" : (cumulBy == 4 ? "ristourne_vente_by_facture" : "ristourne_vente")), param);
    }

    public void downloadRistournes(boolean total, String format) {
        Map<String, Object> param = new HashMap<>();
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("PERIODE", total ? "" : periode);
        param.put("VENDEUR", (int) users);
        param.put("CLIENT", (int) client.getId());
        param.put("CUMULE", cumulBy);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport(total ? "ristourne_vente_total_no_header" : (cumulBy == 0 ? "ristourne_vente_with_facture_no_header" : (cumulBy == 4 ? "ristourne_vente_by_facture_no_header" : "ristourne_vente_no_header")), param, "", format, false);
    }

    public void loadBonus() {
        bonus.setSociete(currentAgence.getSociete().getId());
        bonus.setAgence(agence);
        bonus.setZone(zone);
        bonus.setDateDebut(dateDebut);
        bonus.setDateFin(dateFin);
        bonus.setVendeur(users);
        bonus.setClient(client.getId());
        bonus.setPeriode(periode);
        bonus.setCumulBy(cumulBy);
        bonus.loadBonusVentes(dao);
    }

    public void downloadBonus(boolean total) {
        Map<String, Object> param = new HashMap<>();
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("PERIODE", total ? "" : periode);
        param.put("VENDEUR", (int) users);
        param.put("CLIENT", (int) client.getId());
        param.put("CUMULE", cumulBy);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport((cumulBy == 0 ? "bonus_vente_with_facture" : (cumulBy == 3 ? "bonus_vente_by_facture" : "bonus_vente")), param);
    }

    public void downloadBonus(boolean total, String format) {
        Map<String, Object> param = new HashMap<>();
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("PERIODE", total ? "" : periode);
        param.put("VENDEUR", (int) users);
        param.put("CLIENT", (int) client.getId());
        param.put("CUMULE", cumulBy);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport((cumulBy == 0 ? "bonus_vente_with_facture_no_header" : (cumulBy == 3 ? "bonus_vente_by_facture_no_header" : "bonus_vente_no_header")), param, "", format, false);
    }

    public void loadListingByClient() {
        if (cumulBy != 1) {
            vueType = 0;
        }
        listingClient.setSociete(currentAgence.getSociete().getId());
        listingClient.setAgence(agence);
        listingClient.setZone(zone);
        listingClient.setDateDebut(dateDebut);
        listingClient.setDateFin(dateFin);
        listingClient.setVendeur(users);
        listingClient.setClient(client.getId());
        listingClient.setPeriode(periode);
        listingClient.setCumulBy(cumulBy);
        listingClient.setVueType(vueType);
        listingClient.loadListingVenteByClient(dao);
    }

    public void downloadListingByClient() {
        Map<String, Object> param = new HashMap<>();
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("PERIODE", periode);
        param.put("VENDEUR", (int) users);
        param.put("CLIENT", (int) client.getId());
        param.put("CUMULE", cumulBy);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("DISPLAY_NAME", listingClient.isDisplayAnotherName());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport((vueType == 1 ? "listing_vente_by_client_dates" : (vueType == 2 ? "listing_vente_by_client_articles" : "listing_vente_by_client")), param);
    }

    public void downloadListingByClient(String format) {
        Map<String, Object> param = new HashMap<>();
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("PERIODE", periode);
        param.put("VENDEUR", (int) users);
        param.put("CLIENT", (int) client.getId());
        param.put("CUMULE", cumulBy);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("DISPLAY_NAME", listingClient.isDisplayAnotherName());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport((vueType == 1 ? "listing_vente_by_client_dates_no_header" : (vueType == 2 ? "listing_vente_by_client_articles_no_header" : "listing_vente_by_client_no_header")), param, "", format, false);
    }

    public void loadListingByFournisseur() {
        if (cumulBy != 1) {
            vueType = 0;
        }
        listingFsseur.setSociete(currentAgence.getSociete().getId());
        listingFsseur.setAgence(agence);
        listingFsseur.setDateDebut(dateDebut);
        listingFsseur.setDateFin(dateFin);
        listingFsseur.setFournisseur(fournisseur.getId());
        listingFsseur.setPeriode(periode);
        listingFsseur.setCumulBy(cumulBy);
        listingFsseur.setVueType(vueType);
        listingFsseur.loadListingAchatByFournisseur(dao);
    }

    public void downloadListingByFournisseur() {
        Map<String, Object> param = new HashMap<>();
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("PERIODE", periode);
        param.put("VENDEUR", (int) users);
        param.put("CLIENT", (int) client.getId());
        param.put("CUMULE", cumulBy);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("DISPLAY_NAME", listingClient.isDisplayAnotherName());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport((vueType == 1 ? "listing_achat_by_fournisseur_dates" : (vueType == 2 ? "listing_achat_by_fournisseur_articles" : "listing_achat_by_fournisseur")), param);
    }

    public void downloadListingByFournisseur(String format) {
        Map<String, Object> param = new HashMap<>();
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("AGENCE", (int) agence);
        param.put("PERIODE", periode);
        param.put("VENDEUR", (int) users);
        param.put("CLIENT", (int) client.getId());
        param.put("CUMULE", cumulBy);
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("DISPLAY_NAME", listingClient.isDisplayAnotherName());
        param.put("LOGO", returnLogo());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport((vueType == 1 ? "listing_achat_by_fournisseur_dates_no_header" : (vueType == 2 ? "listing_achat_by_fournisseur_articles_no_header" : "listing_achat_by_fournisseur_no_header")), param, "", format, false);
    }

    public void chooseAgence() {
        if (agence > 0) {
            ManagedUser s = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (s != null) {
                s.loadAgence(agence);
                update("data_vendeur_journal_vente");
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsUsers bean = (YvsUsers) ev.getObject();
            setVendeur(UtilUsers.buildBeanUsers(bean));
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildSimpleBeanArticles(bean));
            update("blog_bord_article");
        }
    }

    public void loadOnViewClient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient bean = (YvsComClient) ev.getObject();
            idClient = bean.getId();
            ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
            if (w != null) {
                indexClient = w.getIds_clients().indexOf(idClient);
            }
            loadDataClient(bean);
        }
    }

    public void loadOnViewFournisseur(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseFournisseur bean = (YvsBaseFournisseur) ev.getObject();
            idFsseur = bean.getId();
            ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            if (w != null) {
                indexFsseur = w.getIds_fournisseurs().indexOf(idFsseur);
            }
            loadDataFsseur(bean);
        }
    }

    public void searchClient() {
        String num = getClient().getCodeClient();
        getClient().setId(0);
        getClient().setError(true);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
            if (m != null) {
                Client e = m.searchClient(num, false);
                if (e != null) {
                    if (m.getClients() != null ? m.getClients().size() > 1 : false) {
                        getErrorMessage("Entrer le code d'un seul client");
                    } else {
                        setClient(e);
                    }
                    getClient().setError(false);
                }
            }
        }
    }

    public void searchFournisseur() {
        String num = getFournisseur().getCodeFsseur();
        getFournisseur().setId(0);
        getFournisseur().setError(true);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            if (m != null) {
                Fournisseur e = m.searchFsseur(num, false);
                if (e != null) {
                    if (m.getFournisseurs() != null ? m.getFournisseurs().size() > 1 : false) {
                        getErrorMessage("Entrer le code d'un seul fournisseur");
                    } else {
                        setFournisseur(e);
                    }
                    getFournisseur().setError(false);
                }
            }
        }
    }

    public void searchVendeur() {
        String num = getVendeur().getCodeUsers();
        getVendeur().setId(0);
        getVendeur().setError(true);
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (m != null) {
                Users e = m.searchUsersActif(num, true);
                if (e != null) {
                    if (m.getListUser() != null ? m.getListUser().size() > 1 : false) {
                    }
                    setVendeur(e);
                    getVendeur().setError(false);
                }
            }
        } else {
            setVendeur(new Users());
        }
    }

    public void initVendeurs() {
        ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
        if (m != null) {
            m.initUsers(getVendeur());
        }
    }

    public Object getValue(String row, String col) {
        int r = colonnes.indexOf(row);
        int c = header.indexOf(col);
        if (c > -1 && r > -1) {
            Object v = donnees[r][c];
            return v;
        }
        return 0;
    }

    public int getIndexCol(String col) {
        return header.indexOf(col);
    }

    public int getIndexRow(String row) {
        return colonnes.indexOf(row);
    }

    public void print(String element) {
        String file = "";
        Map<String, Object> param = new HashMap<>();
        switch (getNature()) {
            case "O": {//Journal de vente d'un vendeur
                if (vendeur != null ? vendeur.getId() > 0 : false) {
                    param.put("VENDEUR", new Long(vendeur.getId()).intValue());
                    file = "journal_vendeur";
                } else {
                    getErrorMessage("Vous devez specifier le vendeur");
                    return;
                }
                break;
            }
            default: {
                param.put("AGENCE", new Long(agence).intValue());
                file = "journal_vendeurs";
                break;
            }
        }
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("LOGO", returnLogo());
        param.put("NAME_TITLE", "JOURNAL DE VENTE " + (periode.equals("A") ? "ANNUEL" : (periode.equals("T") ? "TRIMESTRIEL" : (periode.equals("M") ? "MENSUEL" : (periode.equals("S") ? "HEBDOMADAIRE" : "JOURNALIER")))));
        param.put("USERS", currentUser.getUsers().getNomUsers());
        param.put("PERIODE", periode);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        if (file != null ? file.trim().length() > 0 : false) {
            executeReport(file, param);
        }
    }

    public void printDashBoardClient(String etat, boolean grouper) {
        this.grouper = grouper;
        printDashBoardClient(etat);
    }

    public void printDashBoardClient(String etat) {
        Map<String, Object> param = new HashMap<>();
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("LOGO", returnLogo());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("PERIODE", periode);
        param.put("CLIENT", (int) idClient);
        param.put("GROUPER", grouper);
        param.put("ETAT", etat);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport("dashboard_client", param);
    }

    public void printDashBoardFsseur(String etat, boolean grouper) {
        this.grouperF = grouper;
        printDashBoardFsseur(etat);
    }

    public void printDashBoardFsseur(String etat) {
        Map<String, Object> param = new HashMap<>();
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("LOGO", returnLogo());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("PERIODE", periode);
        param.put("FOURNISSEUR", (int) idFsseur);
        param.put("GROUPER", grouperF);
        param.put("ETAT", etat);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport("dashboard_fournisseur", param);
    }

    public void loadListingAchat() {
        if (!cumule) {
            loadListingAchat(true, true);
        } else {
            listing_achat.setSociete(currentAgence.getSociete().getId());
            listing_achat.setAgence(agence);
            listing_achat.setDepot(depot);
            listing_achat.setFournisseur(fournisseur.getId());
            listing_achat.setFamille(0);
            listing_achat.setGroupe(0);
            listing_achat.setComptes("");
            listing_achat.setCategorie("");
            listing_achat.setDateDebut(dateDebut);
            listing_achat.setDateFin(dateFin);
            listing_achat.setPeriode("");
            listing_achat.setOffset(0);
            listing_achat.setLimit(0);
            listing_achat.setAddTotal(false);

            listing_achat.returnArticlesAchat(dao);
        }
    }

    public void loadListingVente() {
        if (!cumule) {
            loadListingVente(true, true);
        } else {
            listing.setSociete(currentAgence.getSociete().getId());
            listing.setAgence(agence);
            listing.setPoint(point);
            listing.setVendeur(users);
            listing.setCommercial(0);
            listing.setClient(client.getId());
            listing.setFamille(0);
            listing.setGroupe(0);
            listing.setComptes("");
            listing.setCategorie("");
            listing.setDateDebut(dateDebut);
            listing.setDateFin(dateFin);
            listing.setPeriode("");
            listing.setOffset(0);
            listing.setLimit(0);
            listing.setAddTotal(false);

            listing.returnArticles(dao);
        }
    }

    public void loadListingVente(boolean avancer, boolean init) {
        p_listing.clear();
        p_listing.addParam(new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        p_listing.addParam(new ParametreRequete("y.docVente.typeDoc", "typeDoc", Constantes.TYPE_FV, "=", "AND"));
        p_listing.addParam(new ParametreRequete("y.docVente.enteteDoc.dateEntete", "dateEntete", dateDebut, dateFin, "BETWEEN", "AND"));
        if (agence > 0) {
            p_listing.addParam(new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.agence.id", "agence", agence, "=", "AND"));
        }
        if (point > 0) {
            p_listing.addParam(new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.id", "point", point, "=", "AND"));
        }
        if (users > 0) {
            p_listing.addParam(new ParametreRequete("y.docVente.enteteDoc.creneau.users.id", "users", users, "=", "AND"));
        }
        if (client != null ? client.getId() > 0 : false) {
            p_listing.addParam(new ParametreRequete("y.docVente.client.id", "client", client.getId(), "=", "AND"));
        }
        if (Util.asString(typeListing)) {
            if (typeListing.contains("R")) {
                ParametreRequete r = new ParametreRequete("y.prix", "pr", "y.pr", "<", "AND");
                r.setCompareAttribut(true);
                p_listing.addParam(r);
            }
            if (typeListing.contains("M")) {
                ParametreRequete r = new ParametreRequete("y.prix", "pm", "y.puvMin", "<", "AND");
                r.setCompareAttribut(true);
                p_listing.addParam(r);
            }
        }
//        listings = p_listing.executeDynamicQuery("YvsComContenuDocVente", "y.docVente.enteteDoc.dateEntete", avancer, init, dao);
        listings = p_listing.executeDynamicQuery("DISTINCT(y.docVente)", "DISTINCT(y.docVente)", "YvsComContenuDocVente y", "y.docVente.enteteDoc.dateEntete DESC", avancer, init, (int) imax, dao);
    }

    public void loadListingAchat(boolean avancer, boolean init) {
        p_listing_achat.clear();
        p_listing_achat.addParam(new ParametreRequete("y.docAchat.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        p_listing_achat.addParam(new ParametreRequete("y.docAchat.typeDoc", "typeDoc", Constantes.TYPE_FA, "=", "AND"));
        p_listing_achat.addParam(new ParametreRequete("y.docAchat.dateDoc", "dateDoc", dateDebut, dateFin, "BETWEEN", "AND"));
        p_listing_achat.addParam(new ParametreRequete("y.docAchat.statut", "statut", Constantes.ETAT_VALIDE, "=", "AND"));
        if (agence > 0) {
            p_listing_achat.addParam(new ParametreRequete("y.docAchat.agence.id", "agence", agence, "=", "AND"));
        }
        if (point > 0) {
            p_listing_achat.addParam(new ParametreRequete("y.docAchat.depotReception.id", "depot", point, "=", "AND"));
        }
        if (fournisseur != null ? fournisseur.getId() > 0 : false) {
            p_listing_achat.addParam(new ParametreRequete("y.docAchat.fournisseur.id", "fournisseur", fournisseur.getId(), "=", "AND"));
        }
        if (Util.asString(typeListing)) {

        }
        listings_achat = p_listing_achat.executeDynamicQuery("YvsComContenuDocAchat", "y.docAchat.dateDoc", avancer, init, dao);
        nameQueriCount = p_listing_achat.getNameQueriCount();
        Double value = null;
        if (nameQueriCount != null) {
            nameQueriCount = nameQueriCount.replace("COUNT(y)", "SUM(y.prixTotal)");
            champ = p_listing_achat.getChamp();
            val = p_listing_achat.getVal();
            value = (Double) dao.loadObjectByEntity(nameQueriCount, champ, val);
        }
        totalListingAchat = value != null ? value : 0;
    }

    public void gotoPagePaginatorListing() {
        p_listing.gotoPage(p_listing.getRows());
        loadListingVente(true, true);
    }

    public void gotoPagePaginatorListingAchat() {
        p_listing_achat.gotoPage(p_listing_achat.getRows());
        loadListingAchat(true, true);
    }

    public void choosePaginatorListing(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        p_listing.setRows((int) imax);
        loadListingVente(true, true);
    }

    public void choosePaginatorListingAchat(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        p_listing_achat.setRows((int) imax);
        loadListingAchat(true, true);
    }

    public void printListingAchat() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATEDEBUT", dateDebut);
            param.put("DATEFIN", dateFin);
            param.put("LOGO", returnLogo());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("TYPE", typeListing);
            param.put("FOURNISSEUR", (int) fournisseur.getId());
            param.put("DEPOT", (int) depot);
            param.put("ARTICLE", 0);
            param.put("UNITE", 0);
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport(!cumule ? "listing_achat" : "listing_achat_cumule", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printListingVente() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATEDEBUT", dateDebut);
            param.put("DATEFIN", dateFin);
            param.put("LOGO", returnLogo());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("TYPE", typeListing);
            param.put("CLIENT", (int) client.getId());
            param.put("POINT", (int) point);
            param.put("VENDEUR", (int) users);
            param.put("ARTICLE", 0);
            param.put("UNITE", 0);
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport(!cumule ? "listing_vente" : "listing_vente_cumule", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printListingAchat(String format) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATEDEBUT", dateDebut);
            param.put("DATEFIN", dateFin);
            param.put("LOGO", returnLogo());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("TYPE", typeListing);
            param.put("FOURNISSEUR", (int) fournisseur.getId());
            param.put("DEPOT", (int) depot);
            param.put("ARTICLE", 0);
            param.put("UNITE", 0);
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport(!cumule ? "listing_achat_no_header" : "listing_achat_cumule_no_header", param, "", format, false);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printListingVente(String format) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATEDEBUT", dateDebut);
            param.put("DATEFIN", dateFin);
            param.put("LOGO", returnLogo());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("TYPE", typeListing);
            param.put("CLIENT", (int) client.getId());
            param.put("POINT", (int) point);
            param.put("VENDEUR", (int) users);
            param.put("ARTICLE", 0);
            param.put("UNITE", 0);
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport(!cumule ? "listing_vente_no_header" : "listing_vente_cumule_no_header", param, "", format, false);
//            executeReport(!cumule ? "listing_vente" : "listing_vente_cumule", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void choixArticles(SelectEvent ev) {
        if (ev != null) {
            YvsBaseArticles art = (YvsBaseArticles) ev.getObject();
            if (art != null) {
                if (!selectionArticle.contains(art.getId())) {
                    selectionArticle.add(art.getId());
                } else {
                    selectionArticle.remove(art.getId());
                }
                loadDataClient(new YvsComClient(idClient));
            }
        }
    }

    public double returnValeur(int row, int col) {
        if (valuesDashboard != null ? valuesDashboard.get(row)[col] != null : false) {
            return valuesDashboard.get(row)[col].valeur(typeMontantPerf);
        }
        return 0;
    }

    public void loadDataVenteClient() {
        Options[] param = new Options[]{new Options(dateDebut, 1), new Options(dateFin, 2), new Options(currentAgence.getSociete().getId(), 3)};
        String rq = "SELECT * FROM et_total_clients(?, ?, false, ?) ORDER BY ca DESC";
        ventesClients = dao.loadListBySqlQuery(rq, param);
    }

    public void loadInfos(boolean load) {
        loadInfos(load, "C");
    }

    public void loadInfos(boolean load, String tiers) {
        if (exo != null ? exo.getId() != null ? exo.getId() < 1 : true : true) {
            exo = giveExerciceActif();
            exercice = exo.getId();
            Calendar c = Calendar.getInstance();
            c.set(c.get(Calendar.YEAR), 0, 1);
            dateDebut = c.getTime();
            dateDebut = c.getTime();
        }
        if (agence < 1) {
            agence = currentAgence.getId();
        }
    }

    public void navigateInViewClient(boolean next) {
        if (synthese.getClient().getCodeClient() == null) {
            synthese.getClient().setCodeClient("");
        }
        ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (w != null ? (w.getIds_clients() != null ? !w.getIds_clients().isEmpty() : false) : false) {
            if (indexClient > w.getIds_clients().size()) {
                indexClient = -1;
            }
            if (next) {
                if (indexClient < w.getIds_clients().size() - 1) {
                    indexClient++;
                } else {
                    indexClient = 0;
                }
            } else {
                if (indexClient > 0) {
                    indexClient--;
                } else {
                    indexClient = w.getIds_clients().size() - 1;
                }
            }
            Long id = w.getIds_clients().get(indexClient);
            loadOneClient(id);
        }
    }

    public void navigateInViewFsseur(boolean next) {
        if (syntheseF.getFournisseur().getCodeFsseur() == null) {
            syntheseF.getFournisseur().setCodeFsseur("");
        }
        ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (w != null ? (w.getIds_fournisseurs() != null ? !w.getIds_fournisseurs().isEmpty() : false) : false) {
            if (indexFsseur > w.getIds_fournisseurs().size()) {
                indexFsseur = -1;
            }
            if (next) {
                if (indexFsseur < w.getIds_fournisseurs().size() - 1) {
                    indexFsseur++;
                } else {
                    indexFsseur = 0;
                }
            } else {
                if (indexFsseur > 0) {
                    indexFsseur--;
                } else {
                    indexFsseur = w.getIds_fournisseurs().size() - 1;
                }
            }
            Long id = w.getIds_fournisseurs().get(indexFsseur);
            loadOneFournisseur(id);
        }
    }

    public void gotoPageClient() {
        ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (w != null) {
            if (w.getIds_clients().size() > 0 && indexClient > 0) {
                if (w.getIds_clients().size() > indexClient) {
                    indexClient = indexClient - 1;
                } else {
                    indexClient = w.getIds_clients().size() - 1;
                }
                Long id = w.getIds_clients().get(indexClient);
                loadOneClient(id);
            } else {
                indexClient = -1;
            }
        }
    }

    public void loadClient(ManagedClient w) {
        if (w != null) {
            indexClient = -1;
            if (w.getIds_clients().size() > 0) {
                indexClient = 0;
                Long id = w.getIds_clients().get(indexClient);
                loadOneClient(id);
            } else {
                loadDataClient(new YvsComClient());
            }
        }
    }

    private void loadOneClient(Long id) {
        boolean load = false;
        if (id != null ? id > 0 : false) {
            idClient = id;
            YvsComClient bean = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{id});
            if (bean != null) {
                loadDataClient(bean);
                load = true;
            }
        }
        if (!load) {
            loadDataClient(new YvsComClient());
        }
    }

    public void findOneClient(String clt) {
        ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (w != null) {
            w.addParamCode(clt);
            loadClient(w);
        }
    }

    public void addParamGroupeClient() {
        ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (w != null) {
            w.addParamGroupe();
            loadClient(w);
        }
    }

    public void addParamCompteClient() {
        ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (w != null) {
            w.addParamCompte();
            loadClient(w);
        }
    }

    public void addParamCategorieClient() {
        ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (w != null) {
            w.addParamCategorie();
            loadClient(w);
        }
    }

    public void addParamPaysClient() {
        ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (w != null) {
            w.addParamPays();
            loadClient(w);
        }
    }

    public void addParamVilleClient() {
        ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (w != null) {
            w.addParamVille();
            loadClient(w);
        }
    }

    public void gotoPageFsseur() {
        ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (w != null) {
            if (w.getIds_fournisseurs().size() > 0 && indexFsseur > 0) {
                if (w.getIds_fournisseurs().size() > indexFsseur) {
                    indexFsseur = indexFsseur - 1;
                } else {
                    indexFsseur = w.getIds_fournisseurs().size() - 1;
                }
                Long id = w.getIds_fournisseurs().get(indexFsseur);
                loadOneFournisseur(id);
            } else {
                indexFsseur = -1;
            }
        }
    }

    public void loadFournisseur(ManagedFournisseur w) {
        if (w != null) {
            indexFsseur = -1;
            if (w.getIds_fournisseurs().size() > 0) {
                indexFsseur = 0;
                Long id = w.getIds_fournisseurs().get(indexFsseur);
                loadOneFournisseur(id);
            } else {
                loadDataFsseur(new YvsBaseFournisseur());
            }
        }
    }

    private void loadOneFournisseur(Long id) {
        boolean load = false;
        if (id != null ? id > 0 : false) {
            idFsseur = id;
            YvsBaseFournisseur bean = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findById", new String[]{"id"}, new Object[]{id});
            if (bean != null) {
                loadDataFsseur(bean);
                load = true;
            }
        }
        if (!load) {
            loadDataFsseur(new YvsBaseFournisseur());
        }
    }

    public void findOneFsseur(String fsseur) {
        ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (w != null) {
            w.addParamCode(fsseur);
            loadFournisseur(w);
        }
    }

    public void addParamGroupeFsseur() {
        ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (w != null) {
            w.addParamGroupe();
            loadFournisseur(w);
        }
    }

    public void addParamCategorieFsseur() {
        ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (w != null) {
            w.addParamCategorie();
            loadFournisseur(w);
        }
    }

    public void addParamCompteFsseur() {
        ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedClient.class);
        if (w != null) {
            w.addParamCompte();
            loadFournisseur(w);
        }
    }

    public void addParamPaysFsseur() {
        ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (w != null) {
            w.addParamPays();
            loadFournisseur(w);
        }
    }

    public void addParamVilleFsseur() {
        ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (w != null) {
            w.addParamVille();
            loadFournisseur(w);
        }
    }

    public void loadDataClient() {
        loadDataClient(new YvsComClient(idClient));
    }

    public void loadDataFsseur() {
        loadDataFsseur(new YvsBaseFournisseur(idFsseur));
    }

    public void loadDataClient(YvsComClient bean) {
        if (bean != null ? bean.getId() > 0 : false) {
            bean = (YvsComClient) dao.loadOneByNameQueries("YvsComClient.findById", new String[]{"id"}, new Object[]{bean.getId()});
            synthese.setClient(UtilCom.buildSimpleBeanClient(bean));
            loadInfosArticleClient();
            loadDataEcheancierClient();
            loadDataImpayesClient();
            loadDataAcompteClient();
            loadDataCreditClient();
            loadProgressClient(true);
            update("form_tb_clients");
        } else {
            synthese = new SyntheseClient();
        }
    }

    public void loadDataFsseur(YvsBaseFournisseur bean) {
        if (bean != null ? bean.getId() > 0 : false) {
            bean = (YvsBaseFournisseur) dao.loadOneByNameQueries("YvsBaseFournisseur.findById", new String[]{"id"}, new Object[]{bean.getId()});
            syntheseF.setFournisseur(UtilCom.buildBeanFournisseur(bean));
            loadDataEcheancierFsseur();
            loadDataImpayesFsseur();
            loadDataAcompteFsseur();
            loadDataCreditFsseur();
            loadProgressFsseur(true);
            update("form_tb_fsseur");
        } else {
            syntheseF = new SyntheseFournisseur();
        }
    }

    public void loadInfosArticleClient() {
        etats.returnArticles(currentAgence.getSociete().getId(), agence, 0, 0, 0, synthese.getClient().getId(), 0, 0, dateDebut, dateFin, periode, "", "", "", 0, 0, dao);
    }

    public void loadDataEcheancierClient() {
        if (agence > 0) {
            champ = new String[]{"agence", "client", "etat", "dateDebut", "dateFin"};
            val = new Object[]{new YvsAgences(agence), new YvsComClient(synthese.getClient().getId()), "P", dateDebut, dateFin};
            nameQueri = "YvsComMensualiteFactureVente.findByClientDatesNotEtatAgence";
        } else {
            champ = new String[]{"client", "etat", "dateDebut", "dateFin"};
            val = new Object[]{new YvsComClient(synthese.getClient().getId()), "P", dateDebut, dateFin};
            nameQueri = "YvsComMensualiteFactureVente.findByClientDatesNotEtat";
        }
        synthese.setMensualites(dao.loadNameQueries(nameQueri, champ, val));
    }

    private double soldeInitial(YvsBaseFournisseur y, Date date) {
        try {
            Calendar dd = Calendar.getInstance();
            dd.set(Calendar.YEAR, 2015);
            double solde = 0;
            ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            if (w != null) {
                if (agence > 0) {
                    solde = -(w.creance(new YvsAgences(agence), y, dd.getTime(), date, false));
                } else {
                    solde = -(w.creance(y, dd.getTime(), date, false));
                }
            }
            return solde != 0 ? solde : 0;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private double soldeInitial(YvsComClient y, Date date) {
        try {
            Calendar dd = Calendar.getInstance();
            dd.set(Calendar.YEAR, 2015);
            double solde = 0;
            ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
            if (w != null) {
                if (agence > 0) {
                    solde = -(w.creance(new YvsAgences(agence), y, dd.getTime(), date, false));
                } else {
                    solde = -(w.creance(y, dd.getTime(), date, false));
                }
            }
            return solde != 0 ? solde : 0;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void loadDataImpayesClient() {
        if (agence > 0) {
            champ = new String[]{"agence", "client", "typeDoc", "dateDebut", "dateFin"};
            val = new Object[]{new YvsAgences(agence), new YvsComClient(synthese.getClient().getId()), Constantes.TYPE_FV, dateDebut, dateFin};
            nameQueri = "YvsComDocVentes.findImpayeByClientDatesAgence";
        } else {
            champ = new String[]{"client", "typeDoc", "dateDebut", "dateFin"};
            val = new Object[]{new YvsComClient(synthese.getClient().getId()), Constantes.TYPE_FV, dateDebut, dateFin};
            nameQueri = "YvsComDocVentes.findImpayeByClientDates";
        }
        synthese.setFactures(dao.loadNameQueries(nameQueri, champ, val));
        synthese.setSoldeImpaye(0);
        for (YvsComDocVentes f : synthese.getFactures()) {
            setMontantTotalDoc(f);
            synthese.setSoldeImpaye(synthese.getSoldeImpaye() + f.getMontantResteApayer());
        }

        Calendar c = Calendar.getInstance();
        c.setTime(dateDebut);
        c.add(Calendar.DATE, -1);
        synthese.setDateDebut(c.getTime());
        synthese.setSoldeInitial(soldeInitial(new YvsComClient(synthese.getClient().getId()), c.getTime()));

        if (agence > 0) {
            nameQueri = "YvsComContenuDocVente.findTTCByClientDatesAgence";
            champ = new String[]{"agence", "client", "dateDebut", "dateFin"};
            val = new Object[]{new YvsAgences(agence), new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin};
        } else {
            nameQueri = "YvsComContenuDocVente.findTTCByClientDates";
            champ = new String[]{"client", "dateDebut", "dateFin"};
            val = new Object[]{new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin};
        }
        Double ttc = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);

        if (agence > 0) {
            nameQueri = "YvsComContenuDocVente.findTTCAvoirByClientDatesAgence";
            champ = new String[]{"agence", "client", "dateDebut", "dateFin"};
            val = new Object[]{new YvsAgences(agence), new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin};
        } else {
            nameQueri = "YvsComContenuDocVente.findTTCAvoirByClientDates";
            champ = new String[]{"client", "dateDebut", "dateFin"};
            val = new Object[]{new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin};
        }
        Double avoir = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);

        if (agence > 0) {
            nameQueri = "YvsComptaCaissePieceVente.findSumAvoirByClientDatesAgence";
            champ = new String[]{"agence", "client", "dateDebut", "dateFin", "mouvement"};
            val = new Object[]{new YvsAgences(agence), new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin, Constantes.MOUV_CAISS_SORTIE.charAt(0)};
        } else {
            nameQueri = "YvsComptaCaissePieceVente.findSumAvoirByClientDates";
            champ = new String[]{"client", "dateDebut", "dateFin", "mouvement"};
            val = new Object[]{new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin, Constantes.MOUV_CAISS_SORTIE.charAt(0)};
        }
        Double aad = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        if (agence > 0) {
            val = new Object[]{new YvsAgences(agence), new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin, Constantes.MOUV_CAISS_ENTREE.charAt(0)};
        } else {
            val = new Object[]{new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin, Constantes.MOUV_CAISS_ENTREE.charAt(0)};
        }
        Double aar = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        synthese.setSoldeAvoir((avoir != null ? avoir : 0) - ((aar != null ? aar : 0) - (aad != null ? aad : 0)));

        if (agence > 0) {
            nameQueri = "YvsComCoutSupDocVente.findSumServiceByClientDatesAgence";
            champ = new String[]{"agence", "client", "sens", "service", "dateDebut", "dateFin"};
            val = new Object[]{new YvsAgences(agence), new YvsComClient(synthese.getClient().getId()), true, true, dateDebut, dateFin};
        } else {
            nameQueri = "YvsComCoutSupDocVente.findSumServiceByClientDates";
            champ = new String[]{"client", "sens", "service", "dateDebut", "dateFin"};
            val = new Object[]{new YvsComClient(synthese.getClient().getId()), true, true, dateDebut, dateFin};
        }
        Double cp = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        if (agence > 0) {
            val = new Object[]{new YvsAgences(agence), new YvsComClient(synthese.getClient().getId()), false, true, dateDebut, dateFin};
        } else {
            val = new Object[]{new YvsComClient(synthese.getClient().getId()), false, true, dateDebut, dateFin};
        }
        Double cm = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        double cs = (cp != null ? cp : 0) - (cm != null ? cm : 0);
        synthese.setSoldeImpayes((ttc != null ? ttc : 0) + cs);

        if (agence > 0) {
            nameQueri = "YvsComptaCaissePieceVente.findSumByClientDatesAgence";
            champ = new String[]{"agence", "client", "dateDebut", "dateFin", "mouvement"};
            val = new Object[]{new YvsAgences(agence), new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin, Constantes.MOUV_CAISS_SORTIE.charAt(0)};
        } else {
            nameQueri = "YvsComptaCaissePieceVente.findSumByClientDates";
            champ = new String[]{"client", "dateDebut", "dateFin", "mouvement"};
            val = new Object[]{new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin, Constantes.MOUV_CAISS_SORTIE.charAt(0)};
        }

        Double avd = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        if (agence > 0) {
            val = new Object[]{new YvsAgences(agence), new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin, Constantes.MOUV_CAISS_ENTREE.charAt(0)};
        } else {
            val = new Object[]{new YvsComClient(synthese.getClient().getId()), dateDebut, dateFin, Constantes.MOUV_CAISS_ENTREE.charAt(0)};
        }
        Double avr = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        synthese.setSoldeAvance((avr != null ? avr : 0) - (avd != null ? avd : 0));
    }

    public void loadDataAcompteClient() {
        synthese.getAcomptes().clear();
        List<Long> ids;
        if (agence > 0) {
            String query = "SELECT y.id FROM yvs_compta_acompte_client y INNER JOIN yvs_base_caisse c ON y.caisse = c.id INNER JOIN yvs_compta_journaux j ON c.journal = j.id WHERE y.montant > COALESCE((SELECT SUM(r.montant) FROM yvs_compta_caisse_piece_vente r INNER JOIN yvs_compta_notif_reglement_vente n ON n.piece_vente = r.id WHERE n.acompte = y.id AND r.statut_piece = 'P'), 0) AND y.statut = 'P' AND y.client = ? AND COALESCE(y.date_paiement, y.date_acompte) BETWEEN ? AND ? AND j.agence = ?";
            ids = dao.loadListBySqlQuery(query, new Options[]{new Options(synthese.getClient().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3), new Options(agence, 4)});
        } else {
            String query = "SELECT y.id FROM yvs_compta_acompte_client y WHERE y.montant > COALESCE((SELECT SUM(r.montant) FROM yvs_compta_caisse_piece_vente r INNER JOIN yvs_compta_notif_reglement_vente n ON n.piece_vente = r.id WHERE n.acompte = y.id AND r.statut_piece = 'P'), 0) AND y.statut = 'P' AND y.client = ? AND COALESCE(y.date_paiement, y.date_acompte) BETWEEN ? AND ?";
            ids = dao.loadListBySqlQuery(query, new Options[]{new Options(synthese.getClient().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)});
        }
        if (ids != null ? !ids.isEmpty() : false) {
            synthese.setAcomptes(dao.loadNameQueries("YvsComptaAcompteClient.findByIds", new String[]{"ids"}, new Object[]{ids}));
        }
        synthese.setSoldeAcompte(0);
        for (YvsComptaAcompteClient f : synthese.getAcomptes()) {
            synthese.setSoldeAcompte(synthese.getSoldeAcompte() + f.getReste());
        }
    }

    public void loadDataCreditClient() {
        synthese.getCredits().clear();
        List<Long> ids;
        if (agence > 0) {
            String query = "SELECT y.id FROM yvs_compta_credit_client y INNER JOIN yvs_compta_journaux j ON y.journal = j.id WHERE y.montant > COALESCE((SELECT SUM(r.valeur) FROM yvs_compta_reglement_credit_client r WHERE r.credit = y.id AND r.statut = 'P'), 0) AND y.statut = 'V' AND y.client = ? AND y.date_credit BETWEEN ? AND ? AND j.agence = ?";
            ids = dao.loadListBySqlQuery(query, new Options[]{new Options(synthese.getClient().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3), new Options(agence, 4)});
        } else {
            String query = "SELECT y.id FROM yvs_compta_credit_client y WHERE y.montant > COALESCE((SELECT SUM(r.valeur) FROM yvs_compta_reglement_credit_client r WHERE r.credit = y.id AND r.statut = 'P'), 0) AND y.statut = 'V' AND y.client = ? AND y.date_credit BETWEEN ? AND ?";
            ids = dao.loadListBySqlQuery(query, new Options[]{new Options(synthese.getClient().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)});
        }
        if (ids != null ? !ids.isEmpty() : false) {
            synthese.setCredits(dao.loadNameQueries("YvsComptaCreditClient.findByIds", new String[]{"ids"}, new Object[]{ids}));
        }
        synthese.setSoldeCredit(0);
        for (YvsComptaCreditClient f : synthese.getCredits()) {
            synthese.setSoldeCredit(synthese.getSoldeCredit() + f.getReste());
        }
    }

    public void loadDataEcheancierFsseur() {
        if (agence > 0) {
            champ = new String[]{"agence", "fournisseur", "etat", "dateDebut", "dateFin"};
            val = new Object[]{new YvsAgences(agence), new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), "P", dateDebut, dateFin};
            nameQueri = "YvsComMensualiteFactureAchat.findByClientDatesNotEtatAgence";
        } else {
            champ = new String[]{"fournisseur", "etat", "dateDebut", "dateFin"};
            val = new Object[]{new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), "P", dateDebut, dateFin};
            nameQueri = "YvsComMensualiteFactureAchat.findByClientDatesNotEtat";
        }
        syntheseF.setMensualites(dao.loadNameQueries(nameQueri, champ, val));
    }

    public void loadFacturesImpaye() {
        impayesAchat = loadDatasImpayesFsseur(agence, dateDebut, dateFin, groupImpayeBy);
    }

    public List<Object[]> loadDatasImpayesFsseur(long agence, Date dateDebut, Date dateFin, String groupBy) {
        List<Object[]> result = new ArrayList<>();
        debitFsseur = 0;
        creditFsseur = 0;
        soldeFsseur = 0;
        try {
            if (Util.asString(groupBy) ? groupBy.equals("F") : true) {
                if (agence > 0) {
                    champ = new String[]{"typeDoc", "agence", "dateDebut", "dateFin"};
                    val = new Object[]{Constantes.TYPE_FA, new YvsAgences(agence), dateDebut, dateFin};
                    nameQueri = "YvsComDocAchats.findFournisseurByImpayeDatesAgence";
                } else {
                    champ = new String[]{"typeDoc", "societe", "dateDebut", "dateFin"};
                    val = new Object[]{Constantes.TYPE_FA, currentAgence.getSociete(), dateDebut, dateFin};
                    nameQueri = "YvsComDocAchats.findFournisseurByImpayeDates";
                }
                List<YvsBaseFournisseur> fournisseurs = dao.loadNameQueries(nameQueri, champ, val);
                Object[] data;
                YvsComDocAchats initial;
                List<YvsComDocAchats> documents;
                double debit = 0;
                double credit = 0;
                double solde = 0;
                for (YvsBaseFournisseur f : fournisseurs) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(dateDebut);
                    c.add(Calendar.DATE, -1);
                    initial = new YvsComDocAchats();
                    initial.setNumDoc("SOLDE INITIAL");
                    initial.setDateDoc(c.getTime());
                    if (agence > 0) {
                        champ = new String[]{"fournisseur", "agence", "typeDoc", "dateFin"};
                        val = new Object[]{f, new YvsAgences(agence), Constantes.TYPE_FA, initial.getDateDoc()};
                        nameQueri = "YvsComContenuDocAchat.findTTCByFournisseurAgenceDate";
                        Double ttc = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                        initial.setMontantTTC((ttc != null ? ttc : 0));
                        champ = new String[]{"fournisseur", "agence", "dateFin"};
                        val = new Object[]{f, new YvsAgences(agence), initial.getDateDoc()};
                        nameQueri = "YvsComptaCaissePieceAchat.sumByFournisseurAgenceDate";
                        Double avance = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                        initial.setMontantAvance((avance != null ? avance : 0));

                        champ = new String[]{"fournisseur", "agence", "typeDoc", "dateDebut", "dateFin"};
                        val = new Object[]{f, new YvsAgences(agence), Constantes.TYPE_FA, dateDebut, dateFin};
                        nameQueri = "YvsComDocAchats.findImpayesByFactureDatesAgence";
                    } else {
                        champ = new String[]{"fournisseur", "typeDoc", "dateFin"};
                        val = new Object[]{f, Constantes.TYPE_FA, initial.getDateDoc()};
                        nameQueri = "YvsComContenuDocAchat.findTTCByFournisseurDate";
                        Double ttc = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                        initial.setMontantTTC((ttc != null ? ttc : 0));
                        champ = new String[]{"fournisseur", "dateFin"};
                        val = new Object[]{f, initial.getDateDoc()};
                        nameQueri = "YvsComptaCaissePieceAchat.sumByFournisseurDate";
                        Double avance = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                        initial.setMontantAvance((avance != null ? avance : 0));

                        champ = new String[]{"fournisseur", "typeDoc", "dateDebut", "dateFin"};
                        val = new Object[]{f, Constantes.TYPE_FA, dateDebut, dateFin};
                        nameQueri = "YvsComDocAchats.findImpayesByFactureDates";
                    }
                    documents = dao.loadNameQueries(nameQueri, champ, val);

                    debit = 0;
                    credit = 0;
                    solde = 0;

                    for (YvsComDocAchats d : documents) {
                        setMontantTotalDoc(d);
                        debit += d.getMontantTotal();
                        credit += d.getMontantAvance();
                        solde += d.getMontantResteApayer();
                    }
                    if (initial.getMontantResteApayer() > 0) {
                        documents.add(0, initial);
                        debit += initial.getMontantTotal();
                        credit += initial.getMontantAvance();
                        solde += initial.getMontantResteApayer();
                    }
                    debitFsseur += debit;
                    creditFsseur += credit;
                    soldeFsseur += solde;

                    data = new Object[5];
                    data[0] = "[" + f.getCodeFsseur() + "] " + f.getNom_prenom();
                    data[1] = documents;
                    data[2] = debit;
                    data[3] = credit;
                    data[4] = solde;
                    result.add(data);
                }
            } else {
                List<Options> params = new ArrayList<>();
                params.add(new Options(currentAgence.getSociete().getId(), 1));
                params.add(new Options(dateDebut, 2));
                params.add(new Options(dateFin, 3));
                String colonne = "y.date_doc";
                if (Util.asString(groupBy) ? groupBy.equals("M") : true) {
                    colonne = "SUBSTRING(CAST(y.date_doc AS text), 1, 7)";
                } else if (Util.asString(groupBy) ? groupBy.equals("A") : true) {
                    colonne = "SUBSTRING(CAST(y.date_doc AS text), 1, 4)";
                }
                String query = " FROM yvs_com_doc_achats y INNER JOIN yvs_agences a ON y.agence = a.id WHERE y.statut_regle != 'P' AND y.type_doc = 'FA' AND a.societe = ? AND y.date_doc BETWEEN ? AND ?";
                if (agence > 0) {
                    params.add(new Options(agence, 4));
                    query += " AND y.agence = ?";
                }
                if (Util.asString(groupBy) ? groupBy.equals("M") : true) {
                    query += " ORDER BY SUBSTRING(CAST(y.date_doc AS text), 1, 7)";
                } else if (Util.asString(groupBy) ? groupBy.equals("A") : true) {
                    query += " ORDER BY SUBSTRING(CAST(y.date_doc AS text), 1, 4)";
                } else {
                    query += " ORDER BY y.date_doc";
                }
                query = "SELECT DISTINCT " + colonne + query;
                List<Object> dates = dao.loadListBySqlQuery(query, params.toArray(new Options[params.size()]));
                Object[] data;
                List<YvsComDocAchats> documents;
                double debit = 0;
                double credit = 0;
                double solde = 0;
                Date debut, fin = new Date();
                for (Object f : dates) {
                    debit = 0;
                    credit = 0;
                    solde = 0;
                    if (Util.asString(groupBy) ? groupBy.equals("M") : true) {
                        debut = formatDateReverse.parse((String) f + "-01");
                        Calendar time = Calendar.getInstance();
                        time.setTime(debut);
                        time.set(Calendar.DATE, time.getActualMaximum(Calendar.DAY_OF_MONTH));
                        fin = time.getTime();
                    } else if (Util.asString(groupBy) ? groupBy.equals("A") : true) {
                        debut = formatDateReverse.parse((String) f + "-01-01");
                        fin = formatDateReverse.parse((String) f + "-12-31");
                    } else {
                        debut = (Date) f;
                        fin = (Date) f;
                    }
                    if (fin.after(dateFin)) {
                        fin = dateFin;
                    }
                    if (debut.before(dateDebut)) {
                        debut = dateDebut;
                    }
                    if (agence > 0) {
                        champ = new String[]{"dateDebut", "dateFin", "agence", "typeDoc"};
                        val = new Object[]{debut, fin, new YvsAgences(agence), Constantes.TYPE_FA};
                        nameQueri = "YvsComDocAchats.findImpayesByFactureDate2Agence";
                    } else {
                        champ = new String[]{"dateDebut", "dateFin", "societe", "typeDoc"};
                        val = new Object[]{debut, fin, currentAgence.getSociete(), Constantes.TYPE_FA};
                        nameQueri = "YvsComDocAchats.findImpayesByFactureDate2";
                    }
                    documents = dao.loadNameQueries(nameQueri, champ, val);
                    for (YvsComDocAchats d : documents) {
                        setMontantTotalDoc(d);
                        debit += d.getMontantTotal();
                        credit += d.getMontantAvance();
                        solde += d.getMontantResteApayer();
                    }
                    debitFsseur += debit;
                    creditFsseur += credit;
                    soldeFsseur += solde;

                    data = new Object[5];
                    if (Util.asString(groupBy) ? groupBy.equals("M") : true) {
                        data[0] = formatMonthString.format(debut);
                    } else if (Util.asString(groupBy) ? groupBy.equals("A") : true) {
                        data[0] = formatYear.format(debut);
                    } else {
                        data[0] = formatDate.format(debut);
                    }
                    data[1] = documents;
                    data[2] = debit;
                    data[3] = credit;
                    data[4] = solde;
                    result.add(data);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void loadDataImpayesFsseur() {
        if (agence > 0) {
            champ = new String[]{"agence", "fournisseur", "typeDoc", "dateDebut", "dateFin"};
            val = new Object[]{new YvsAgences(agence), new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), Constantes.TYPE_FA, dateDebut, dateFin};
            nameQueri = "YvsComDocAchats.findImpayesByFactureDatesAgence";
        } else {
            champ = new String[]{"fournisseur", "typeDoc", "dateDebut", "dateFin"};
            val = new Object[]{new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), Constantes.TYPE_FA, dateDebut, dateFin};
            nameQueri = "YvsComDocAchats.findImpayesByFactureDates";
        }
        syntheseF.setFactures(dao.loadNameQueries(nameQueri, champ, val));
        syntheseF.setSoldeImpaye(0);
        for (YvsComDocAchats f : syntheseF.getFactures()) {
            setMontantTotalDoc(f);
            syntheseF.setSoldeImpaye(syntheseF.getSoldeImpaye() + f.getMontantResteApayer());
        }

        Calendar df = Calendar.getInstance();
        df.setTime(dateDebut);
        df.add(Calendar.DATE, -1);
        syntheseF.setDateDebut(df.getTime());
        syntheseF.setSoldeInitial(soldeInitial(new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), df.getTime()));

        if (agence > 0) {
            nameQueri = "YvsComContenuDocAchat.findTTCByFournisseurAgenceDates";
            champ = new String[]{"agence", "fournisseur", "dateDebut", "dateFin"};
            val = new Object[]{new YvsAgences(agence), new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), dateDebut, dateFin};
        } else {
            nameQueri = "YvsComContenuDocAchat.findTTCByFournisseurDates";
            champ = new String[]{"fournisseur", "dateDebut", "dateFin"};
            val = new Object[]{new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), dateDebut, dateFin};
        }
        Double ttc = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);

        if (agence > 0) {
            nameQueri = "YvsComCoutSupDocAchat.findSumByFournisseurDatesAgence";
            champ = new String[]{"agence", "fournisseur", "sens", "dateDebut", "dateFin"};
            val = new Object[]{new YvsAgences(agence), new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), true, dateDebut, dateFin};
        } else {
            nameQueri = "YvsComCoutSupDocAchat.findSumByFournisseurDates";
            champ = new String[]{"fournisseur", "sens", "dateDebut", "dateFin"};
            val = new Object[]{new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), true, dateDebut, dateFin};
        }

        Double p = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        if (agence > 0) {
            val = new Object[]{new YvsAgences(agence), new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), false, dateDebut, dateFin};
        } else {
            val = new Object[]{new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), false, dateDebut, dateFin};
        }
        Double m = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        double o = (p != null ? p : 0) - (m != null ? m : 0);
        syntheseF.setSoldeImpayes((ttc != null ? ttc : 0) + o);

        if (agence > 0) {
            nameQueri = "YvsComptaCaissePieceAchat.findSumByFournisseurDatesAgence";
            champ = new String[]{"agence", "fournisseur", "statut", "dateDebut", "dateFin"};
            val = new Object[]{new YvsAgences(agence), new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), Constantes.STATUT_DOC_PAYER, dateDebut, dateFin};
        } else {
            nameQueri = "YvsComptaCaissePieceAchat.findSumByFournisseurDates";
            champ = new String[]{"fournisseur", "statut", "dateDebut", "dateFin"};
            val = new Object[]{new YvsBaseFournisseur(syntheseF.getFournisseur().getId()), Constantes.STATUT_DOC_PAYER, dateDebut, dateFin};
        }
        Double av = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        syntheseF.setSoldeAvance(av != null ? av : 0);
    }

    public void loadDataAcompteFsseur() {
        syntheseF.getAcomptes().clear();
        List<Long> ids;
        if (agence > 0) {
            String query = "SELECT y.id FROM yvs_compta_acompte_fournisseur y INNER JOIN yvs_base_caisse c ON y.caisse = c.id INNER JOIN yvs_compta_journaux j ON c.journal = j.id WHERE y.montant > COALESCE((SELECT SUM(r.montant) FROM yvs_compta_caisse_piece_achat r INNER JOIN yvs_compta_notif_reglement_achat n ON n.piece_achat = r.id WHERE n.acompte = y.id AND r.statut_piece = 'P'), 0) AND y.statut = 'P' AND y.fournisseur = ? AND y.date_acompte BETWEEN ? AND ? AND j.agence = ?";
            ids = dao.loadListBySqlQuery(query, new Options[]{new Options(syntheseF.getFournisseur().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3), new Options(agence, 4)});
        } else {
            String query = "SELECT y.id FROM yvs_compta_acompte_fournisseur y WHERE y.montant > COALESCE((SELECT SUM(r.montant) FROM yvs_compta_caisse_piece_achat r INNER JOIN yvs_compta_notif_reglement_achat n ON n.piece_achat = r.id WHERE n.acompte = y.id AND r.statut_piece = 'P'), 0) AND y.statut = 'P' AND y.fournisseur = ? AND y.date_acompte BETWEEN ? AND ?";
            ids = dao.loadListBySqlQuery(query, new Options[]{new Options(syntheseF.getFournisseur().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)});
        }
        if (ids != null ? !ids.isEmpty() : false) {
            syntheseF.setAcomptes(dao.loadNameQueries("YvsComptaAcompteFournisseur.findByIds", new String[]{"ids"}, new Object[]{ids}));
        }
        syntheseF.setSoldeAcompte(0);
        for (YvsComptaAcompteFournisseur f : syntheseF.getAcomptes()) {
            syntheseF.setSoldeAcompte(syntheseF.getSoldeAcompte() + f.getReste());
        }
    }

    public void loadDataCreditFsseur() {
        syntheseF.getCredits().clear();
        List<Long> ids;
        if (agence > 0) {
            String query = "SELECT y.id FROM yvs_compta_credit_fournisseur y INNER JOIN yvs_compta_journaux j ON y.journal = j.id WHERE y.montant > COALESCE((SELECT SUM(r.valeur) FROM yvs_compta_reglement_credit_fournisseur r WHERE r.credit = y.id AND r.statut = 'P'), 0) AND y.statut = 'V' AND y.fournisseur = ? AND y.date_credit BETWEEN ? AND ? AND j.agence = ?";
            ids = dao.loadListBySqlQuery(query, new Options[]{new Options(syntheseF.getFournisseur().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3), new Options(agence, 4)});
        } else {
            String query = "SELECT y.id FROM yvs_compta_credit_fournisseur y WHERE y.montant > COALESCE((SELECT SUM(r.valeur) FROM yvs_compta_reglement_credit_fournisseur r WHERE r.credit = y.id AND r.statut = 'P'), 0) AND y.statut = 'V' AND y.fournisseur = ? AND y.date_credit BETWEEN ? AND ?";
            ids = dao.loadListBySqlQuery(query, new Options[]{new Options(syntheseF.getFournisseur().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)});
        }
        if (ids != null ? !ids.isEmpty() : false) {
            syntheseF.setCredits(dao.loadNameQueries("YvsComptaCreditFournisseur.findByIds", new String[]{"ids"}, new Object[]{ids}));
        }
        syntheseF.setSoldeCredit(0);
        for (YvsComptaCreditFournisseur f : syntheseF.getCredits()) {
            syntheseF.setSoldeCredit(syntheseF.getSoldeCredit() + f.getReste());
        }
    }

    public void loadProgressClient(boolean nw) {
        try {
            synthese.setBarModel(new CartesianChartModel());
            if (nw || progress.getLignes().isEmpty()) {
                switch (natureProgress) {
                    case "A":
                        progress.returnArticles(currentAgence.getSociete().getId(), agence, 0, 0, 0, synthese.getClient().getId(), 0, 0, dateDebut, dateFin, periode, "", "", "", 0, 0, dao);
                        break;
                    default:
                        progress.returnYearClient(agence, synthese.getClient().getId(), dateDebut, dateFin, periode, dao);
                        break;
                }
            }
            ChartSeries serie;
            for (int r = 0; r < progress.getLignes().size() - 1; r++) {
                Rows row = (Rows) progress.getLignes().get(r);
                serie = new ChartSeries(row.getTitre());
                for (int c = 0; c < progress.getPeriodes().size() - 1; c++) {
                    String col = progress.getPeriodes().get(c);
                    serie.set(col, Double.valueOf(progress.valeur(r, c, type).toString()));
                }
                synthese.getBarModel().addSeries(serie);
            }
        } catch (NumberFormatException ex) {
        }
    }

    public void loadProgressFsseur(boolean nw) {
        try {
            syntheseF.setBarModel(new CartesianChartModel());
        } catch (NumberFormatException ex) {
        }
    }

    public void chooseExercice() {
        if (exercice > 0) {
            ManagedExercice m = (ManagedExercice) giveManagedBean(ManagedExercice.class);
            if (m != null) {
                int idx = m.getExercices().indexOf(new YvsBaseExercice(exercice));
                if (idx > -1) {
                    exo = m.getExercices().get(idx);
                    dateDebut = exo.getDateDebut();
                    dateFin = exo.getDateFin();

                    dateDebut = exo.getDateDebut();
                    dateFin = exo.getDateFin();
                }
            }
        }
        update("table_table_fsseur_db");
        update("table_table_client_db");
    }

    public void seeDetailsAchat(YvsComDocAchats y) {
        this.achat = y;
    }

    public void gotoFactureVente(YvsComDocVentes y) {
        try {
            ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
            if (w != null) {
                w.onSelectDistant(y);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoFactureAchat(YvsComDocAchats y) {
        try {
            ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (w != null) {
                w.onSelectDistant(y);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void changeDatesByExo(YvsBaseExercice exo, Date dateDebut, Date dateFin) {
        dateDebut = exo.getDateDebut();
        dateFin = exo.getDateFin();
    }

    public void choosePeriode() {
        if (periode != null) {
            Calendar c = Calendar.getInstance();
            c.set(c.get(Calendar.YEAR), 00, 01);
            dateDebut = c.getTime();
            c.set(c.get(Calendar.YEAR), 11, 31);
            dateFin = c.getTime();
            switch (periode) {
                case "A":
                    ManagedExercice m = (ManagedExercice) giveManagedBean(ManagedExercice.class);
                    if (m != null) {
                        if (!m.getExercices().isEmpty()) {
                            changeDatesByExo(m.getExercices().get(m.getExercices().size() - 1), dateDebut, dateFin);
                            changeDatesByExo(m.getExercices().get(m.getExercices().size() - 1), dateDebut, dateFin);
                        }
                    }
                    break;
                case "M":
                    if (exo != null ? exo.getId() > 0 : false) {
                        changeDatesByExo(exo, dateDebut, dateFin);
                        changeDatesByExo(exo, dateDebut, dateFin);
                    }
                    break;
                case "J":
                    chooseMois();
                    break;
                default:
                    break;
            }
        }
    }

    private void changeDatesByFinExo(YvsBaseExercice exo, Date dateDebut, Date dateFin) {
        Calendar c = Calendar.getInstance();
        c.setTime(exo.getDateFin());

        c.set(Calendar.MONTH, mois);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        dateDebut = c.getTime();

        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        dateFin = c.getTime();
    }

    public void chooseMois() {
        if (periode != null ? ("A".equals(periode) || "J".equals(periode)) : false) {
            if (exo != null ? exo.getId() > 0 : false) {
                changeDatesByFinExo(exo, dateDebut, dateFin);
                changeDatesByFinExo(exo, dateDebut, dateFin);

                update("date_table_fsseur_imp_dash");
                update("date_table_fsseur_ret_dash");
                update("date_table_fsseur_acomp_dash");

                update("date_table_vente_compare");
                update("date_table_client_ret_dash");
                update("linear_table_client");
                update("table_table_client_db");
            }
        }
    }

    /*
     BEGIN BALANCE AGE CLIENT
     */
    public void initBalanceAgeClient() {
        type = "ca";
        periode = "M";
        if (exo != null ? exo.getId() != null ? exo.getId() < 1 : true : true) {
            exo = giveExerciceActif();
        }
        if (exo != null ? exo.getId() != null ? exo.getId() > 0 : false : false) {
            dateDebut = exo.getDateDebut();
            dateFin = exo.getDateFin();
        } else {
            Calendar c = Calendar.getInstance();
            c.set(c.get(Calendar.YEAR), 0, 1);
            dateDebut = c.getTime();
        }
    }

    public void loadBalanceAgeClient() {
        age.returnBAgeClient(currentAgence.getSociete().getId(), ecartAge, colonneAge, dateDebut, dateFin, dao);
    }

    public void gotoCreditFournisseur() {
        if (syntheseF != null ? syntheseF.getSoldeCredit() > 0 : false) {
            ManagedOperationFournisseur s = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (s != null) {
                s.onSelectObjectByFournisseur(new YvsBaseFournisseur(syntheseF.getFournisseur().getId(), syntheseF.getFournisseur().getCodeFsseur(), syntheseF.getFournisseur().getNom(), syntheseF.getFournisseur().getPrenom()));
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Crédit Fournisseur", "modCompta", "smenCreditFsseur", true);
                }
            }
        }
    }

    public void gotoCreditClient() {
        if (synthese != null ? synthese.getSoldeCredit() > 0 : false) {
            ManagedOperationClient s = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
            if (s != null) {
                s.onSelectObjectByClient(new YvsComClient(synthese.getClient().getId(), synthese.getClient().getCodeClient(), synthese.getClient().getNom(), synthese.getClient().getPrenom()));
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Crédit Client", "modCompta", "smenCreditClient", true);
                }
            }
        }
    }
    /*
     END BALANCE AGE CLIENT
     */

    public void loadDataSoldesFsseurCurrentExo(int limit) {
        loadSoldeFournisseur = true;
        soldesFssseurs.setSociete(currentAgence.getSociete().getId());
        soldesFssseurs.setDateDebut(currentExo.getDateDebut());
        soldesFssseurs.setDateFin(currentExo.getDateFin());
        soldesFssseurs.setPeriode("");
        soldesFssseurs.returnSoldeFournisseur(limit, dao);
    }

    public void loadDataSoldesFsseur() {
        soldesFssseurs.setSociete(currentAgence.getSociete().getId());
        soldesFssseurs.setDateDebut(dateDebut);
        soldesFssseurs.setDateFin(dateFin);
        soldesFssseurs.setPeriode(periode);
        soldesFssseurs.returnSoldeFournisseur(dao);
    }

    public void loadDataClientsCurrentExo(int limit) {
        loadClassementClient = true;
        tabClients.setSociete(currentAgence.getSociete().getId());
        tabClients.setDateDebut(currentExo.getDateDebut());
        tabClients.setDateFin(currentExo.getDateFin());
        tabClients.setPeriode("");
        tabClients.returnTotalClients(limit, dao);
    }

    public void loadDataClients() {
        tabClients.setSociete(currentAgence.getSociete().getId());
        tabClients.setDateDebut(dateDebut);
        tabClients.setDateFin(dateFin);
        tabClients.setPeriode(periode);
        tabClients.returnTotalClients(dao);
    }

    public void downloadTotalClient() {
        try {
            Map<String, Object> param = new HashMap<>();
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);
            param.put("REFERENCE", "");
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
            executeReport("dashboard_total_client", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSoldeClients() {
        soldeClients.setSociete(currentAgence.getSociete().getId());
        soldeClients.setDateDebut(dateDebut);
        soldeClients.setDateFin(dateFin);
        soldeClients.setPeriode(periode);

        soldeClients.returnDashboardClients(dao);
    }

    public void downloadSoldeClients() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport("dashboard_solde_client", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void downloadImpayesAchat() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport(groupImpayeBy.equals("F") ? "impayes_achats_by_fournisseur" : "impayes_achats_by_date", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String giveLibeleCa(String code) {
        if (code != null) {
            switch (code) {
                case "caMois":
                    return "Chiffre d'affaire du";
                case "caJour":
                    return "Chiffre d'affaire du";
                case "caJourP":
                    return "Chiffre d'affaire du";
                case "caMoisP":
                    return "Chiffre d'affaire du";
                default:
                    return null;
            }
        }
        return null;
    }

    public String giveLibeleCaDate(String code) {
        if (code != null) {
            switch (code) {
                case "caMois":
                    return formatDate.format(dateDebut).concat(" Au ").concat(formatDate.format(dateFin));
                case "caJour":
                    return (formatDate.format(dateFin));
                case "caJourP":
                    return (formatDate.format(finP));
                case "caMoisP":
                    return formatDate.format(debutP).concat(" Au ").concat(formatDate.format(finP));
                default:
                    break;
            }
        }
        return null;
    }
    //*** END COMMERCIALE ***//

    //*** BEGIN PRODUCTION ***//
    public void loadProductionVente() {
        productionVente.setSociete(currentAgence.getSociete().getId());
        productionVente.setAgence(agence);
        productionVente.setDateDebut(dateDebut);
        productionVente.setDateFin(dateFin);
        productionVente.setPeriode(periode);
        productionVente.setValorise_by(valoriserBy);
        productionVente.returnProductionVente(dao);
    }

    public void downloadProductionVente() {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", (int) agence);
        param.put("ARTICLES", productionVente.getComptes());
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("PERIODE", periode);
        param.put("TYPE", productionVente.getNature());
        param.put("VALORISE_BY", valoriserBy);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport("production_vente", param);
    }

    public void loadEcartInventaire() {
        if (agence < 1) {
            getErrorMessage("Vous devez precisez l'agence");
            return;
        }
        ecart.setSociete(currentAgence.getSociete().getId());
        ecart.setAgence(agence);
        ecart.setDateDebut(dateDebut);
        ecart.setDateFin(dateFin);
        ecart.setPeriode(periode);
        ecart.setCumule(cumule);
        ecart.loadEcartInventaire(dao);
    }

    public void downloadEcartInventaire() {
        if (ecart.getAgence() < 1) {
            getErrorMessage("Vous devez precisez l'agence");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        param.put("AGENCE", (int) agence);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("PERIODE", cumule ? periode : "");
        param.put("NATURE", ecart.getNature());
        param.put("GROUPE", ecart.getCategorie());
        param.put("COEFFICIENT", ecart.getCoefficient());
        param.put("USERS", 0);
        executeReport(ecart.isCumule() ? "ecart_inventaire_cumule" : "ecart_inventaire", param);
    }

    public void loadValeurInventaire() {
        if (selectedUsers.size() < 1 && depot < 1) {
            getErrorMessage("Vous devez precisez le depot ou un/plusieurs editeurs");
            return;
        }
        if (selectedUsers.size() > 0 && depot > 0) {
            getErrorMessage("Vous ne pouvez pas selectionner le depot et l'editeur");
            return;
        }
        String editeurs = "";
        for (YvsUsers u : selectedUsers) {
            editeurs += (editeurs.equals("") ? "" : ",") + u.getId();
        }
        valorise.setSociete(currentAgence.getSociete().getId());
        valorise.setAgence(agence);
        valorise.setDepot(depot);
        valorise.setEditeurs(editeurs);
        valorise.setValoriseMs(valoriseMs);
        valorise.setValorisePf(valorisePf);
        valorise.setValorisePsf(valorisePsf);
        valorise.setValoriseMp(valoriseMp);
        valorise.setCoefficient(coefficient);
        valorise.setDateDebut(dateDebut);
        valorise.setDateFin(dateFin);
        valorise.setValoriseExcedent(valoriseExcedent);
        valorise.loadValeurInventaire(dao);
    }

    public void downloadValeurInventaire() {
        if (selectedUsers.size() < 1 && depot < 1) {
            getErrorMessage("Vous devez precisez le depot ou un/plusieurs editeurs");
            return;
        }
        if (selectedUsers.size() > 0 && depot > 0) {
            getErrorMessage("Vous ne pouvez pas selectionner le depot et l'editeur");
            return;
        }
        String editeurs = "";
        for (YvsUsers u : selectedUsers) {
            editeurs += (editeurs.equals("") ? "" : ",") + u.getId();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        param.put("AGENCE", (int) agence);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("DEPOT_NOM", asString(selectedDepot.getDesignation()) ? selectedDepot.getDesignation() : "AUCUN");
        param.put("VALORISE_MP", valoriseMp);
        param.put("VALORISE_PF", valorisePf);
        param.put("VALORISE_PSF", valorisePsf);
        param.put("VALORISE_MS", valoriseMs);
        param.put("VALORISE_EXCEDENT", valoriseExcedent);
        param.put("COEFFICIENT", coefficient);
        param.put("DEPOT", (int) depot);
        param.put("EDITEUR", editeurs);
        String report = "valorisation_inventaire";
        if (displayExcedent()) {
            report = "valorisation_inventaire_excedent";
        } else if (displayManquant()) {
            report = "valorisation_inventaire_manquant";
        } else if (whatValeurDisplay.equals("MANQUANT%EXCEDENT")) {
            report = "valorisation_inventaire_manquant_excedent";
        }
        executeReport(report, param);
    }

    public void loadStockArticle() {
        tabArticleStock.setSociete(currentAgence.getSociete().getId());
        tabArticleStock.setAgence(agence);
        tabArticleStock.setDepots(depot + "");
        tabArticleStock.setNature(groupBy);
        tabArticleStock.returnStockArticle(dao);
    }

    public void downloadStockArticle() {
        downloadStockArticle("pdf");
    }

    public void downloadStockArticle(String format) {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("DEPOT", depot + "");
        param.put("TYPE", "V");
        param.put("DATE", dateDebut);
        param.put("CATEGORIE", "");
        param.put("GROUP", groupBy);
        param.put("AGENCE", (int) agence);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport("valorisation_stock", param, "", format, false);
    }

    public void loadSyntheseDistribution() {
        syntheseDist.setSociete(currentAgence.getSociete().getId());
        syntheseDist.setDepot(depot);
        syntheseDist.setAgence(agence);
        syntheseDist.setDateDebut(dateDebut);
        syntheseDist.setDateFin(dateFin);
        syntheseDist.setPeriode(periode);
        syntheseDist.returnSyntheseDistribution(dao);
    }

    public void downloadSyntheseDistribution() {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", (int) agence);
        param.put("DEPOT", (int) depot);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("FOR_PROD", true);
        param.put("TYPE", syntheseDist.getNature());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport("synthese_approvision_distibution", param);
    }

    public void loadSyntheseRation() {
        ration.setSociete(currentAgence.getSociete().getId());
        ration.setDepot(depot);
        ration.setAgence(agence);
        ration.setDateDebut(dateDebut);
        ration.setDateFin(dateFin);
        ration.setPeriode(periode);
        ration.setValorise_by(valoriserBy);
        ration.returnSyntheseRation(dao);
    }

    public void downloadSyntheseRation() {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", (int) agence);
        param.put("DEPOT", (int) depot);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("PERIODE", periode);
        param.put("VALORISER_BY", valoriserBy);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport("synthese_ration", param);
    }

    public void loadConsommationMP() {
        consommation.setSociete(currentAgence.getSociete().getId());
        consommation.setDepot(depot);
        consommation.setAgence(agence);
        consommation.setDateDebut(dateDebut);
        consommation.setDateFin(dateFin);
        if (consommation.isByValue()) {
            switch (vueType) {
                case 1:
                    consommation.setPeriode(periode);
                    break;
                case 2:
                    consommation.setPeriode("CL");
                    break;
                case 3:
                    consommation.setPeriode("PF");
                    break;
                case 4:
                    consommation.setPeriode("CP");
                    break;
            }
        } else {
            consommation.setPeriode("");
        }
        consommation.returnConsommationMP(dao);

        try {
            List<String> statuts = new ArrayList<String>() {
                {
                    add(Constantes.ETAT_SOUMIS);
                    add(Constantes.ETAT_ENCOURS);
                }
            };
            nameQueri = "YvsComDocStocks.countByStatutsTypeDate";
            champ = new String[]{"dateDebut", "dateFin", "statuts", "typeDoc", "societe"};
            val = new Object[]{dateDebut, dateFin, statuts, Constantes.TYPE_FT, currentAgence.getSociete()};
            if (depot > 0) {
                nameQueri = "YvsComDocStocks.countByStatutsTypeDepot";
                champ = new String[]{"dateDebut", "dateFin", "statuts", "typeDoc", "depot"};
                val = new Object[]{dateDebut, dateFin, statuts, Constantes.TYPE_FT, new YvsBaseDepots(depot)};
            }
            countTransfert = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void downloadConsommationMP() {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", (int) agence);
        param.put("DEPOT", (int) depot);
        param.put("ARTICLES", consommation.getComptes());
        param.put("TYPE", consommation.getNature());
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        switch (vueType) {
            case 1:
                consommation.setPeriode(periode);
                break;
            case 2:
                consommation.setPeriode("CL");
                break;
            case 3:
                consommation.setPeriode("PF");
                break;
            case 4:
                consommation.setPeriode("CP");
                break;
        }
        param.put("PERIODE", consommation.isByValue() ? consommation.getPeriode() : "");
        param.put("VALORISE_BY", consommation.getValorise_by());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        if (consommation.isDisplayCA() && consommation.isDisplayQte()) {
            executeReport("synthese_consommation", param);
        } else if (consommation.isDisplayCA()) {
            executeReport("synthese_consommation_by_valeur", param);
        } else if (consommation.isDisplayQte()) {
            executeReport("synthese_consommation_by_qte", param);
        }
    }

    public void loadJournalProduction(Dashboards journal) {
        journal.setSociete(currentAgence.getSociete().getId());
        journal.setAgence(agence);
        journal.setDepot(depot);
        journal.setDateDebut(dateDebut);
        journal.setDateFin(dateFin);
        journal.setPeriode(periode);
        journal.setValorise_by(valoriserBy);
        journal.setNature(nature);
        journal.setCategorie(categorie);
        journal.loadJournalProduction(dao);
    }

    public void downloadJournalProduction(Dashboards journal) {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", (int) agence);
        param.put("DEPOT", (int) depot);
        param.put("ARTICLES", "");
        param.put("CATEGORIE", categorie);
        param.put("VALOTISE_BY", valoriserBy);
        param.put("NATURE", nature);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("CUMULE_BY", journal.getCumulBy());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        param.put("LOGO", returnLogo());
        executeReport("journal_production", param);
    }

    public void loadRecapitulatifOF(Dashboards recapitulatif) {
        recapitulatif.setSociete(currentAgence.getSociete().getId());
        recapitulatif.setAgence(agence);
        recapitulatif.setSite(site);
        recapitulatif.setDateDebut(dateDebut);
        recapitulatif.setDateFin(dateFin);
        recapitulatif.setPeriode(periode);
        recapitulatif.setValorise_by(valoriserBy);
        recapitulatif.setNature(nature);
        recapitulatif.setCategorie(categorie);
        recapitulatif.loadRecapitulatifOF(dao);
    }

    public void downloadRecapitulatifOF(Dashboards recapitulatif) {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", (int) agence);
        param.put("SITE", (int) site);
        param.put("ARTICLE", recapitulatif.getArticle());
        param.put("CATEGORIE", categorie);
        param.put("VALOTISE_BY", valoriserBy);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("CUMULE_BY", recapitulatif.getCumulBy());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        param.put("LOGO", returnLogo());
        executeReport("recapitulatif_of", param);
    }

    public void loadProductionConsommation() {
        if (unite < 1) {
            getErrorMessage("Vous devez precisez l'article");
            return;
        }
        prodConso.setSociete(currentAgence.getSociete().getId());
        prodConso.setAgence(agence);
        prodConso.setArticle(unite);
        prodConso.setDateDebut(dateDebut);
        prodConso.setDateFin(dateFin);
        prodConso.setPeriode(periode);
        prodConso.setValorise_by(valoriserBy);
        prodConso.loadProductionConsommation(dao);
    }

    public void downloadProductionConsommation() {
        if (unite < 1) {
            getErrorMessage("Vous devez precisez l'article");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("ARTICLE", unite);
        param.put("VALOTISE_BY", valoriserBy);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("PERIODE", periode);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        param.put("LOGO", returnLogo());
        executeReport("production_consommation", param);
    }

    public void loadProductionConsommationEquipe() {
        prodConsoEquipe.setSociete(currentAgence.getSociete().getId());
        prodConsoEquipe.setAgence(agence);
        prodConsoEquipe.setSite(site);
        prodConsoEquipe.setDepot(depot);
        prodConsoEquipe.setDateDebut(dateDebut);
        prodConsoEquipe.setDateFin(dateFin);
        prodConsoEquipe.setValorise_by(valoriserBy);
        prodConsoEquipe.loadProductionConsommationByEquipe(dao);
    }

    public void downloadProductionConsommationEquipe() {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", (int) agence);
        param.put("SITE", (int) site);
        param.put("DEPOT", (int) depot);
        param.put("VALORISE_BY", valoriserBy);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        param.put("LOGO", returnLogo());
        executeReport("production_consommation_equipe", param);
    }
    //*** END PRODUCTION ***//

    public void loadDataSalarialCurrentExo() {
        try {
            loadSalarial = true;
            String query = "SELECT SUM(coalesce(d.montant_payer, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id "
                    + "inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id "
                    + "inner join yvs_grh_ordre_calcul_salaire o ON b.entete = o.id inner join yvs_grh_element_salaire e ON d.element_salaire = e.id "
                    + "where o.societe = ? and o.debut_mois BETWEEN ? AND ? AND e.visible_bulletin IS TRUE and e.retenue IS FALSE ";
            Double value = (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(currentExo.getDateDebut(), 2), new Options(currentExo.getDateFin(), 3)});
            masseSalarialAnnuel = value != null ? value : 0;
            query = "SELECT SUM(coalesce(d.retenu_salariale, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id "
                    + "inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id "
                    + "inner join yvs_grh_ordre_calcul_salaire o ON b.entete = o.id inner join yvs_grh_element_salaire e ON d.element_salaire = e.id "
                    + "where o.societe = ? and o.debut_mois BETWEEN ? AND ? AND e.visible_bulletin IS TRUE and e.retenue IS TRUE ";
            value = (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(currentExo.getDateDebut(), 2), new Options(currentExo.getDateFin(), 3)});
            taxeSalarialAnnuel = value != null ? value : 0;
            query = "SELECT SUM(coalesce(d.montant_employeur, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id "
                    + "inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id "
                    + "inner join yvs_grh_ordre_calcul_salaire o ON b.entete = o.id inner join yvs_grh_element_salaire e ON d.element_salaire = e.id "
                    + "where o.societe = ? and o.debut_mois BETWEEN ? AND ? AND e.visible_bulletin IS TRUE ";
            value = (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(currentExo.getDateDebut(), 2), new Options(currentExo.getDateFin(), 3)});
            cotisationSalarialAnnuel = value != null ? value : 0;

            Calendar time = Calendar.getInstance();
            time.set(Calendar.DAY_OF_MONTH, 1);
            time.add(Calendar.MONTH, -1);
            System.err.println("" + time.getTime());
            YvsGrhOrdreCalculSalaire ordre = (YvsGrhOrdreCalculSalaire) dao.loadOneByNameQueries("YvsGrhOrdreCalculSalaire.findByContainDate", new String[]{"date", "societe"}, new Object[]{time.getTime(), currentAgence.getSociete()});
            if (ordre != null ? ordre.getId() > 0 : false) {
                query = "SELECT SUM(coalesce(d.montant_payer, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id "
                        + "inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id inner join yvs_grh_element_salaire e ON d.element_salaire = e.id "
                        + "where b.entete = ? AND e.visible_bulletin IS TRUE and e.retenue IS FALSE ";
                value = (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(ordre.getId(), 1)});
                masseSalarialMoisPrec = value != null ? value : 0;
                query = "SELECT SUM(coalesce(d.retenu_salariale, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id "
                        + "inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id inner join yvs_grh_element_salaire e ON d.element_salaire = e.id "
                        + "where b.entete = ? AND e.visible_bulletin IS TRUE and e.retenue IS TRUE ";
                value = (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(ordre.getId(), 1)});
                taxeSalarialMoisPrec = value != null ? value : 0;
                query = "SELECT SUM(coalesce(d.montant_employeur, 0)) from yvs_grh_detail_bulletin d inner join yvs_grh_bulletins b on d.bulletin = b.id "
                        + "inner join yvs_grh_contrat_emps c on b.contrat = c.id INNER JOIN yvs_grh_employes y ON c.employe = y.id inner join yvs_grh_element_salaire e ON d.element_salaire = e.id "
                        + "where b.entete = ? AND e.visible_bulletin IS TRUE ";
                value = (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(ordre.getId(), 1)});
                cotisationSalarialMoisPrec = value != null ? value : 0;
            }

        } catch (Exception ex) {
            getException("loadDataSalarial", ex);
        }
    }

    //*** BEGIN COMPTABILITE ***//
    public void loadBonEncours() {
        ManagedBonProvisoire service = new ManagedBonProvisoire();
        if (service != null) {
            service.setImax(0);
            service.dao = dao;
            service.setStatutJustify(Constantes.ETAT_JUSTIFIE);
            service.setEgaliteJustify("!=");
            service.setStatutSearch(Constantes.ETAT_VALIDE);
            service.setEgaliteStatut("=");
            service.setStatutPaiement(Constantes.ETAT_REGLE);
            service.setEgalitePaiement("=");
            service.addParamStatut(false);
            service.addParamStatutJustify(false);
            service.addParamStatutPaiement(true);
        }
    }

    public void loadSoldesCaisses() {
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service != null) {
            service.getPaginator().addParam(new ParametreRequete("y.principal", "principal", true, "=", "AND"));
            service.setDateSolde(dateFin);
            service.loadSoldeCaisse(true, true, true);
        }
    }

    public void loadSoldesBanque() {
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service != null) {
            service.getPaginator().addParam(new ParametreRequete("y.principal", "principal", true, "=", "AND"));
            service.setDateSolde(dateFin);
            service.loadSoldeCaisse(true, true, false);
        }
    }

    public void loadBonProvisoireNonJustifiesCurrentExo(int limit) {
        ManagedBonProvisoire service = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
        if (service != null) {
            loadBonProvisoire = true;
            bonsProvisoires = service.loadBonProvisoireNonJustifies(currentExo.getDateDebut(), currentExo.getDateFin(), limit);
        }
    }

    public void loadDatasBonProvisoire() {
        ManagedBonProvisoire service = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
        if (service != null) {
            bonNotJustifier = service.loadDatasBonProvisoire(agence, dateDebut, dateFin, groupJustifyBy);
        }
    }

    public void downloadBonProvisoire() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport(groupJustifyBy.equals("F") ? "bon_not_jusfifier_by_tiers" : "bon_not_jusfifier_by_date", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //*** END COMPTABILITE ***//

    public void loadDateVente() {
        loadDataVente(dateDebut, dateFin);
    }

    //Charge le récap des vente par point de vente
    public void loadDashBoardVendeur() {
        creanceClient.setSociete(currentAgence.getSociete().getId());
        creanceClient.setAgence(agence);
        creanceClient.setClient(client.getId());
        creanceClient.setDateDebut(dateDebut);
        creanceClient.setDateFin(dateFin);
        creanceClient.setPeriode(periode);
        creanceClient.setCumule(cumule);
        creanceClient.returnDashBoardVendeur(dao);
    }

    public void downloadDashBoardVendeur() {
        downloadDashBoardVendeur(creanceClient.getVendeur());
    }

    public void downloadDashBoardVendeur(long vendeur) {
        Map<String, Object> param = new HashMap<>();
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("AGENCE", (int) agence);
        param.put("CLIENT", (int) client.getId());
        param.put("VENDEUR", (int) vendeur);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("GROUPER", cumule);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        param.put("LOGO", returnLogo());
        executeReport("dashboard_vendeur", param);
    }

    private void loadDataVente(Date dateDebut, Date dateFin) {
        for (YvsBaseModeReglement m : models) {
            champ = new String[]{"model", "dateDebut", "dateFin"};
            val = new Object[]{m, dateDebut, dateFin};
            nameQueriCount = "YvsComptaCaissePieceVente.findAvanceByModeDates";
            Double c = (Double) dao.loadObjectByNameQueries(nameQueriCount, champ, val);
            m.setCa(c != null ? c : 0);
        }
        Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(agence, 2), new Options(0, 3), new Options(dateDebut, 4), new Options(dateFin, 5)};
        String query = "select y.nombre, y.valeur, y.code, y.rang from public.com_et_dashboard(?,?,?,?,?) y";
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        try {
            Object[] o;
            for (Object y : qr.getResultList()) {
                o = (Object[]) y;
                if (o != null ? o.length > 0 : false) {
                    Long nombre = (Long) o[0];
                    Double valeur = (Double) o[1];
                    String code = (String) o[2];
                    Integer rang = (Integer) o[3];

                    switch (code) {
                        case "ca":
                            commercial.nbreVente = nombre != null ? nombre : 0;
                            break;
                        case "caVenteAvoir":
                            commercial.nbreVenteAvoir = nombre != null ? nombre : 0;
                            commercial.caVenteAvoir = valeur != null ? valeur : 0;
                            break;
                        case "caVenteValide":
                            commercial.nbreVenteValide = nombre != null ? nombre : 0;
                            commercial.caVenteValide = valeur != null ? valeur : 0;
                            break;
                        case "caVenteValideSS":
                            commercial.caVenteValideSS = valeur != null ? valeur : 0;
                            break;
                        case "caVenteValideCS":
                            commercial.caVenteValideCS = valeur != null ? valeur : 0;
                            break;
                        case "caVente":
                            commercial.nbreVente = nombre != null ? nombre : 0;
                            commercial.caVente = valeur != null ? valeur : 0;
                            break;
                        case "caVenteAttence":
                            commercial.nbreVenteAttence = nombre != null ? nombre : 0;
                            commercial.caVenteAttence = valeur != null ? valeur : 0;
                            break;
                        case "caVenteEnCours":
                            commercial.nbreVenteEnCours = nombre != null ? nombre : 0;
                            commercial.caVenteEnCours = valeur != null ? valeur : 0;
                            break;
                        case "caVenteAnnule":
                            commercial.nbreVenteAnnule = nombre != null ? nombre : 0;
                            commercial.caVenteAnnule = valeur != null ? valeur : 0;
                            break;
                        case "caVenteLivre":
                            commercial.nbreVenteLivre = nombre != null ? nombre : 0;
                            commercial.caVenteLivre = valeur != null ? valeur : 0;
                            break;
                        case "caVenteEnCoursLivre":
                            commercial.nbreVenteEnCoursLivre = nombre != null ? nombre : 0;
                            commercial.caVenteEnCoursLivre = valeur != null ? valeur : 0;
                            break;
                        case "caVenteNotLivre":
                            commercial.nbreVenteNotLivre = nombre != null ? nombre : 0;
                            commercial.caVenteNotLivre = valeur != null ? valeur : 0;
                            break;
                        case "caVenteRetardLivr":
                            commercial.nbreVenteRetardLivr = nombre != null ? nombre : 0;
                            commercial.caVenteRetardLivr = valeur != null ? valeur : 0;
                            break;
                        case "caVenteRegle":
                            commercial.nbreVenteRegle = nombre != null ? nombre : 0;
                            commercial.caVenteRegle = valeur != null ? valeur : 0;
                            break;
                        case "caVenteEnCoursRegle":
                            commercial.nbreVenteEnCoursRegle = nombre != null ? nombre : 0;
                            commercial.caVenteEnCoursRegle = valeur != null ? valeur : 0;
                            break;
                        case "caVenteNotRegle":
                            commercial.nbreVenteNotRegle = nombre != null ? nombre : 0;
                            commercial.caVenteNotRegle = valeur != null ? valeur : 0;
                            break;
                    }
                }
            }
        } catch (NoResultException e) {

        }
    }

    public void loadDataSoldeCaisseCurrentExo(int limit) {
        loadSoldeCaisse = true;
        soldeCaisses.setSociete(currentAgence.getSociete().getId());
        soldeCaisses.setDateDebut(currentExo.getDateDebut());
        soldeCaisses.setDateFin(currentExo.getDateFin());
        soldeCaisses.setPeriode("");
        soldeCaisses.returnSoldeCaisses(limit, dao);
    }

    public void loadDataSoldeCaisse() {
        soldeCaisses.setSociete(currentAgence.getSociete().getId());
        soldeCaisses.setAgence(agence);
        soldeCaisses.setDateDebut(dateDebut);
        soldeCaisses.setDateFin(dateFin);
        soldeCaisses.setPeriode(periode);
        soldeCaisses.returnSoldeCaisses(dao);
    }

    public void downloadDataVente() {
        downloadDataVente("pdf");
    }

    public void downloadDataVente(String extension) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR(extension.equals("pdf")));
            executeReport("dashboard_commercial" + (extension.equals("pdf") ? "" : "_no_header"), param, "", extension);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Charge le récap des vente par point de vente
    public void loadDataPointAgenceCurrentExo(int limit) {
        loadClassementPoint = true;
        tabPointAgence.setSociete(currentAgence.getSociete().getId());
        tabPointAgence.setDateDebut(currentExo.getDateDebut());
        tabPointAgence.setDateFin(currentExo.getDateFin());
        tabPointAgence.setPeriode("");
        tabPointAgence.returnTotalPoints(limit, dao);
    }

    public void loadDataPointAgence() {
        tabPointAgence.setSociete(currentAgence.getSociete().getId());
        tabPointAgence.setAgence(agence);
        tabPointAgence.setDateDebut(dateDebut);
        tabPointAgence.setDateFin(dateFin);
        tabPointAgence.setPeriode(periode);

        tabPointAgence.returnTotalPoints(dao);
    }

    public void downloadTotalPoint() {
        downloadTotalPoint("pdf");
    }

    public void downloadTotalPoint(String extension) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);
            param.put("REFERENCE", tabPointAgence.getCompteDebut());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR(extension.equals("pdf")));
            executeReport("dashboard_total_pointvente" + (extension.equals("pdf") ? "" : "_no_header"), param, "", extension);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDataArticleByAgence() {
        tabArticleAgence.setSociete(currentAgence.getSociete().getId());
        tabArticleAgence.setAgence(agence);
        tabArticleAgence.setDateDebut(dateDebut);
        tabArticleAgence.setDateFin(dateFin);
        tabArticleAgence.setPeriode(periode);

        tabArticleAgence.returnArticleAgence(dao);
    }

    public void downloadArticleByAgence() {
        downloadArticleByAgence("pdf");
    }

    public void downloadArticleByAgence(String extension) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);
            param.put("REFERENCE", tabArticleAgence.getComptes());
            param.put("CATEGORIE", tabArticleAgence.getCategorie());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR(extension.equals("pdf")));
            executeReport("dashboard_total_artilces" + (extension.equals("pdf") ? "" : "_no_header"), param, "", extension);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDataArticleByPv() {
        if (point < 1) {
            getErrorMessage("Vous devez selectionner un point de vente");
            return;
        }
        tabArticlePv.setSociete(currentAgence.getSociete().getId());
        tabArticlePv.setAgence(!autoriser("pv_view_all_societe") ? (agence > 0 ? (int) agence : currentAgence.getId().intValue()) : (int) agence);
        tabArticlePv.setPoint(point);
        tabArticlePv.setDateDebut(dateDebut);
        tabArticlePv.setDateFin(dateFin);
        tabArticlePv.setPeriode(periode);

        tabArticlePv.returnArticlePoints(dao);
    }

    public void downloadArticleByPv() {
        downloadArticleByPv("pdf");
    }

    public void downloadArticleByPv(String extension) {
        try {
            if (point < 1) {
                getErrorMessage("Vous devez selectionner un point de vente");
                return;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", !autoriser("pv_view_all_societe") ? (agence > 0 ? (int) agence : currentAgence.getId().intValue()) : (int) agence);
            param.put("POINT", (int) point);
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);
            param.put("REFERENCE", tabArticlePv.getCompteDebut());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR(extension.equals("pdf")));
            executeReport("dashboard_article_pointvente" + (extension.equals("pdf") ? "" : "_no_header"), param, "", extension);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDataVendeurCurrentExo(int limit) {
        loadClassementVendeur = true;
        tabVendeur.setSociete(currentAgence.getSociete().getId());
        tabVendeur.setDateDebut(currentExo.getDateDebut());
        tabVendeur.setDateFin(currentExo.getDateFin());
        tabVendeur.setPeriode("");
        tabVendeur.returnTotalVendeurs(limit, dao);
    }

    public void loadDataVendeur() {
        tabVendeur.setSociete(currentAgence.getSociete().getId());
        tabVendeur.setAgence(agence);
        tabVendeur.setDateDebut(dateDebut);
        tabVendeur.setDateFin(dateFin);
        tabVendeur.setPeriode(periode);
        tabVendeur.returnTotalVendeurs(dao);
    }

    public void downloadVendeur() {
        downloadVendeur("pdf");
    }

    public void downloadVendeur(String extension) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);
            param.put("REFERENCE", tabVendeur.getCompteDebut());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR(extension.equals("pdf")));
            executeReport("dashboard_total_vendeurs" + (extension.equals("pdf") ? "" : "_no_header"), param, "", extension);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDataArticelByVendeur() {
        if (users < 1) {
            getErrorMessage("Vous devez selectionner un vendeur");
            return;
        }
        tabArticleVendeur.setSociete(currentAgence.getSociete().getId());
        tabArticleVendeur.setAgence(agence);
        tabArticleVendeur.setVendeur(users);
        tabArticleVendeur.setDateDebut(dateDebut);
        tabArticleVendeur.setDateFin(dateFin);
        tabArticleVendeur.setPeriode(periode);

        tabArticleVendeur.returnArticleVendeurs(dao);
    }

    public void downloadArticelByVendeur() {
        downloadArticelByVendeur("pdf");
    }

    public void downloadArticelByVendeur(String extension) {
        try {
            if (users < 1) {
                getErrorMessage("Vous devez selectionner un vendeur");
                return;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("VENDEUR", (int) users);
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);
            param.put("REFERENCE", tabArticleVendeur.getCompteDebut());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR(extension.equals("pdf")));
            executeReport("dashboard_article_vendeurs" + (extension.equals("pdf") ? "" : "_no_header"), param, "", extension);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDataArticelByClient() {
        if (client != null ? client.getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner un client");
            return;
        }
        tabArticleClient.setSociete(currentAgence.getSociete().getId());
        tabArticleClient.setAgence(agence);
        tabArticleClient.setClient(client.getId());
        tabArticleClient.setDateDebut(dateDebut);
        tabArticleClient.setDateFin(dateFin);
        tabArticleClient.setPeriode(periode);

        tabArticleClient.returnArticleClient(dao);
    }

    public void downloadArticelByClient() {
        downloadArticelByClient("pdf");
    }

    public void downloadArticelByClient(String extension) {
        try {
            if (client != null ? client.getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un client");
                return;
            }
            Map<String, Object> param = new HashMap<>();
            param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
            param.put("AGENCE", (int) agence);
            param.put("CLIENT", (int) client.getId());
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);
            param.put("REFERENCE", tabArticleClient.getCompteDebut());
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR(extension.equals("pdf")));
            executeReport("dashboard_article_client" + (extension.equals("pdf") ? "" : "_no_header"), param, "", extension);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedBordStatistique.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadDashbordGenerale_from_mdash() {
        Calendar c = Calendar.getInstance();
        if (dateDebut.equals(dateFin)) {
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            dateDebut = c.getTime();
            dateFin = new Date();
            //Calcule le même moi de l'année dernière en partant de date fin
        }
        c.setTime(dateDebut);
        c.add(Calendar.YEAR, -1);
        debutP = c.getTime();
        c.setTime(dateFin);
        c.add(Calendar.YEAR, -1);
        finP = c.getTime();
        // jour
        jourP = finP;
        loadDashbordGenerale();
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean getValMarge(Long id, double marge) {
        YvsBaseConditionnement con = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{id});
        boolean margeSupp = false;
        try {
            if (con != null ? con.getId() > 0 : false) {
                if (con.getMargeMin() > marge) {
                    margeSupp = true;
                } else {
                    margeSupp = false;
                }
            }

        } catch (Exception e) {
        }
        return margeSupp;
    }

    public Double getTotalValeurInventaire(JournalVendeur row) {
        //total_ = quantite_ * prix_ * COALESCE(coefficient_, 1);
        double result = row.getPrixvente();
        if (displayManquant()) {
            result = row.getAttente();
        } else if (displayExcedent()) {
            result = row.getValeur();
        }
        return result;
    }

    public Double getSumTotalValeurInventaire(JournalVendeur row) {
        //total_ = quantite_ * prix_ * COALESCE(coefficient_, 1);
        double result = valorise.summaryGroupResume(row.getSous(), "puv");
        if (displayManquant()) {
            result = valorise.summaryGroupResume(row.getSous(), "attente");
        } else if (displayExcedent()) {
            result = valorise.summaryGroupResume(row.getSous(), "valeur");
        }
        return result;
    }

    public boolean displayManquant() {
        //-MANQUANT+EXCEDENT -MANQUANT%EXCEDENT -EXCEDENT -MANQUANT
        return whatValeurDisplay.equals("MANQUANT%EXCEDENT") || whatValeurDisplay.equals("MANQUANT");
    }

    public boolean displayExcedent() {
        //-MANQUANT+EXCEDENT -MANQUANT%EXCEDENT -EXCEDENT -MANQUANT
        return whatValeurDisplay.equals("MANQUANT%EXCEDENT") || whatValeurDisplay.equals("EXCEDENT");
    }

    public boolean displayManquantExcedent() {
        //-MANQUANT+EXCEDENT -MANQUANT%EXCEDENT -EXCEDENT -MANQUANT
        return !asString(whatValeurDisplay) || whatValeurDisplay.equals("MANQUANT%EXCEDENT") || whatValeurDisplay.equals("MANQUANT+EXCEDENT");
    }
}
