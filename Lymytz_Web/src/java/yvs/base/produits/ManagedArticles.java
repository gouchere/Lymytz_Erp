/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import yvs.production.UtilProd;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.CentreAnalytique;
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.Taxes;
import yvs.base.compta.UtilCompta;
import yvs.base.tiers.ManagedTiers;
import yvs.base.tiers.Tiers;
import yvs.base.tiers.UtilTiers;
import yvs.commercial.ManagedCatCompt;
import yvs.commercial.ManagedTaxes;
import yvs.commercial.UtilCom;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedCategorieClt;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.client.PlanTarifaireClient;
import yvs.commercial.depot.ManagedPointVente;
import yvs.commercial.fournisseur.ConditionnementFsseur;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.commercial.rrr.GrilleRabais;
import yvs.commercial.rrr.Rabais;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.commercial.vente.ManagedFactureVente;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleAnalytique;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.base.YvsBaseArticleEquivalent;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseArticleSubstitution;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseConditionnementPoint;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBaseTableConversion;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsBasePlanTarifaire;
import yvs.entity.commercial.client.YvsBasePlanTarifaireTranche;
import yvs.entity.commercial.rrr.YvsComRabais;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsBaseTarifPointLivraison;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsDictionnaireInformations;
import yvs.entity.produits.*;
import yvs.entity.produits.group.*;
import yvs.entity.stat.export.YvsStatExportEtat;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.init.Initialisation;
import static yvs.init.Initialisation.USER_DOWNLOAD;
import static yvs.init.Initialisation.USER_DOWNLOAD_LINUX;
import yvs.parametrage.dico.ManagedDico;
import yvs.service.IEntitySax;
import yvs.service.base.produit.IYvsBaseArticles;
import yvs.service.com.rrr.IYvsComRabais;
import yvs.stat.export.ManagedExportImport;
import yvs.util.*;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean(name = "Marticle")
public class ManagedArticles extends Managed<Articles, YvsBaseArticles> implements Serializable {

    @ManagedProperty(value = "#{articles}")
    private Articles article;
    private YvsAgences agence = new YvsAgences();
    private List<YvsBaseArticles> listArticle, articlesResult, secondaires, selectArticles;
    private PaginatorResult<YvsBaseArticles> pa = new PaginatorResult<>();
    private long max = imax;
    private YvsBaseDepots dep = new YvsBaseDepots();
//    private boolean updateArticle;
    YvsBaseArticles entityArticle;
    List<String> tags = new ArrayList<>();
    private YvsBaseArticles selectArticle;
    private Conditionnement conditionnement = new Conditionnement();
    private YvsBaseConditionnement selectCondit = new YvsBaseConditionnement();
    private List<YvsBaseConditionnement> conditionnements;
    private String numSearch;
    private boolean calculPr;
    private List<YvsAgences> listAgences = new ArrayList<>();

    private ArticleDescription description = new ArticleDescription();

    private ArticleFournisseur articleFsseur = new ArticleFournisseur();
    private ConditionnementFsseur condFsseur = new ConditionnementFsseur();
    private YvsBaseArticleFournisseur selectArtFsseur = new YvsBaseArticleFournisseur();

    private boolean stockage, template;
    private String unite = "V";
    private String[] typesUnites = new String[]{Constantes.UNITE_QUANTITE};

    private String categorieApplyPr, typePuApplyPr = "V";
    private double puMinApplyPr = 0, puMaxApplyPr = 0, ecartApplyPr;
    private long familleApplyPr = 0;

    private BorneTranche newBorne = new BorneTranche();
    private boolean updateBorne;
    private TrancheVal tranche = new TrancheVal();
//    private List<BorneTranche> listBorneTranche, listSelectBorneTranche;

    private boolean onTarif;
    private GrilleRabais grille = new GrilleRabais();
    private Rabais rabais = new Rabais();
    private YvsComRabais selectRabais;

    private PlanTarifaireClient planTarif = new PlanTarifaireClient();
    private YvsBasePlanTarifaire selectTarif;

    private boolean renderOnglet;
    private String defaultButon = "A-groupP-save";
    private String fusionneTo, fusionneUniteTo;
    private List<String> fusionnesBy, fusionnesUniteBy;

    private ArticleAnalytique analytique = new ArticleAnalytique();
    private YvsBaseArticleAnalytique selectAnalytique = new YvsBaseArticleAnalytique();
    private ArticlesCatComptable articleCategorieC = new ArticlesCatComptable();
    private List<ArticlesCatComptable> listArticleCatC;
    private YvsBaseArticleCategorieComptable selectArticleComptable;
    private List<YvsBaseArticleCategorieComptableTaxe> listTaxes;
//    private List<YvsBasePlanComptable> comptesResult, comptes;
    private List<YvsBaseTaxes> taxes;
    private YvsBaseArticlesTemplate selectTemplate = new YvsBaseArticlesTemplate();/*
     * information sur les borne du plan tarifaire
     */

    private List<YvsBaseConditionnement> equivalencesUnite;

    private CodeBarre codeBarre = new CodeBarre();

    private ArticlePack pack = new ArticlePack();
    private ContenuPack contenu = new ContenuPack();

    private double borne;
    private double valremise;
    private double prix;
    private String modelTranche = "Valeurs"; //indique le model tarifaire(valeur ou quantité)
    /**
     * informations sur la liaison avec le dépot
     */

    /**
     * Creates a new instance of ManagedGroupeProduit
     */
//    private List<YvsDictionnaire> listPays, listVilles;
    private List<CentreAnalytique> listCentreAnal;

    private CentreAnalytique familleCout = new CentreAnalytique();

    private List<YvsBaseArticles> articlesLoad;
    private List<YvsTranches> tranchesRemise;

    private AutoComplete autoC = new AutoComplete();
    /*
     --------------------------------------
     BEGIN ARTICLE
     --------------------------------------
     */
    private List<YvsStatExportEtat> exports;
    private String tabIds, model, docIds;
    private String chaineSelectArt, searchArticle, searchArticles;
    private short colSearch;
    private int currentPageArt = 1, currentSmallPageArt = 0; //numéro de la page courante
    private int totalPageArt = 1, totalSmallPageArt = 1;// nombre total de page
    private int firtResultArt; //position dans les réponses
    private List<Articles> listArticle_ = new ArrayList<>();
    private List<ArticleOrdre> articlesOrdres = new ArrayList<>();
    private boolean disNextArt, disPrevArt;
    private List<String> images = new ArrayList<>();
    private String currentPhoto = Constantes.DEFAULT_PHOTO(), currentPhotoExtension = "png";

    private long groupeSearch, familleSearch, uniteSearch, classeSearch, familySearch;
    private long famillePrint, groupePrint, pointPrint;
    private int niveauSearch;
    private String categorieSearch, categoriePrint, cat;
    private Boolean actifSearch, displayId = false;
    List<YvsBaseArticles> listOrder = new ArrayList<>();
    int niveauMax;
    private YvsBaseArticles bean = new YvsBaseArticles();
    List<Integer> niveau = new ArrayList<>();

    private boolean paramDate;
    private Date dateDebut = new Date(), dateFin = new Date();
    private String methValSearch;

    //Simulation de prix
    private String typePrix = "V";
    private YvsBaseConditionnement selectUnite;
    private Date dateAction = new Date();
    private long trancheDepot = 0, point, depot;
    private double quantite = 0, prixTotal = 0, prixResult = 0, remiseResult, ristourneResult;
    private Client client = new Client();
    private Fournisseur fournisseur = new Fournisseur();
    private List<YvsGrhTrancheHoraire> tranches;
    private String tag;

    //Tarif de livraison
    private long paysTarif, villeTarif;
    private List<YvsBaseConditionnementPoint> pointsVentes;

    public ManagedArticles() {
        articlesResult = new ArrayList<>();
        tranches = new ArrayList<>();
        listTaxes = new ArrayList<>();
        secondaires = new ArrayList<>();
        articlesLoad = new ArrayList<>();
        listArticleCatC = new ArrayList<>();
        listArticle = new ArrayList<>();
        listCentreAnal = new ArrayList<>();
        taxes = new ArrayList<>();
        tranchesRemise = new ArrayList<>();
        fusionnesBy = new ArrayList<>();
        fusionnesUniteBy = new ArrayList<>();
        exports = new ArrayList<>();
        selectArticles = new ArrayList<>();
        equivalencesUnite = new ArrayList<>();
        conditionnements = new ArrayList<>();
        pointsVentes = new ArrayList<>();
    }

    public double getEcartApplyPr() {
        return ecartApplyPr;
    }

    public void setEcartApplyPr(double ecartApplyPr) {
        this.ecartApplyPr = ecartApplyPr;
    }

    public String getCategorieApplyPr() {
        return categorieApplyPr;
    }

    public void setCategorieApplyPr(String categorieApplyPr) {
        this.categorieApplyPr = categorieApplyPr;
    }

    public String getTypePuApplyPr() {
        return typePuApplyPr;
    }

    public void setTypePuApplyPr(String typePuApplyPr) {
        this.typePuApplyPr = typePuApplyPr;
    }

    public double getPuMinApplyPr() {
        return puMinApplyPr;
    }

    public void setPuMinApplyPr(double puMinApplyPr) {
        this.puMinApplyPr = puMinApplyPr;
    }

    public double getPuMaxApplyPr() {
        return puMaxApplyPr;
    }

    public void setPuMaxApplyPr(double puMaxApplyPr) {
        this.puMaxApplyPr = puMaxApplyPr;
    }

    public long getFamilleApplyPr() {
        return familleApplyPr;
    }

    public void setFamilleApplyPr(long familleApplyPr) {
        this.familleApplyPr = familleApplyPr;
    }

    public List<YvsBaseConditionnementPoint> getPointsVentes() {
        return pointsVentes;
    }

    public void setPointsVentes(List<YvsBaseConditionnementPoint> pointsVentes) {
        this.pointsVentes = pointsVentes;
    }

    public List<YvsAgences> getListAgences() {
        return listAgences;
    }

    public void setListAgences(List<YvsAgences> listAgences) {
        this.listAgences = listAgences;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsBaseDepots getDep() {
        return dep;
    }

    public void setDep(YvsBaseDepots dep) {
        this.dep = dep;
    }

    public long getPaysTarif() {
        return paysTarif;
    }

    public void setPaysTarif(long paysTarif) {
        this.paysTarif = paysTarif;
    }

    public long getVilleTarif() {
        return villeTarif;
    }

    public void setVilleTarif(long villeTarif) {
        this.villeTarif = villeTarif;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<YvsBaseConditionnement> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnement> conditionnements) {
        this.conditionnements = conditionnements;
    }

    public List<YvsBaseConditionnement> getEquivalencesUnite() {
        return equivalencesUnite;
    }

    public void setEquivalencesUnite(List<YvsBaseConditionnement> equivalencesUnite) {
        this.equivalencesUnite = equivalencesUnite;
    }

    public List<YvsBaseArticles> getSelectArticles() {
        return selectArticles;
    }

    public void setSelectArticles(List<YvsBaseArticles> selectArticles) {
        this.selectArticles = selectArticles;
    }

    public long getClasseSearch() {
        return classeSearch;
    }

    public void setClasseSearch(long classeSearch) {
        this.classeSearch = classeSearch;
    }

    public String getMethValSearch() {
        return methValSearch;
    }

    public void setMethValSearch(String methValSearch) {
        this.methValSearch = methValSearch;
    }

    public YvsBaseArticleAnalytique getSelectAnalytique() {
        return selectAnalytique;
    }

    public void setSelectAnalytique(YvsBaseArticleAnalytique selectAnalytique) {
        this.selectAnalytique = selectAnalytique;
    }

    public ArticleAnalytique getAnalytique() {
        return analytique;
    }

    public void setAnalytique(ArticleAnalytique analytique) {
        this.analytique = analytique;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public double getRemiseResult() {
        return remiseResult;
    }

    public void setRemiseResult(double remiseResult) {
        this.remiseResult = remiseResult;
    }

    public double getRistourneResult() {
        return ristourneResult;
    }

    public void setRistourneResult(double ristourneResult) {
        this.ristourneResult = ristourneResult;
    }

    public List<YvsGrhTrancheHoraire> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsGrhTrancheHoraire> tranches) {
        this.tranches = tranches;
    }

    public long getTrancheDepot() {
        return trancheDepot;
    }

    public void setTrancheDepot(long trancheDepot) {
        this.trancheDepot = trancheDepot;
    }

    public String getTypePrix() {
        return typePrix;
    }

    public void setTypePrix(String typePrix) {
        this.typePrix = typePrix;
    }

    public YvsBaseConditionnement getSelectUnite() {
        return selectUnite;
    }

    public void setSelectUnite(YvsBaseConditionnement selectUnite) {
        this.selectUnite = selectUnite;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getDepot() {
        return depot;
    }

    public void setDepot(long depot) {
        this.depot = depot;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Date getDateAction() {
        return dateAction;
    }

    public void setDateAction(Date dateAction) {
        this.dateAction = dateAction;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getPrixResult() {
        return prixResult;
    }

    public void setPrixResult(double prixResult) {
        this.prixResult = prixResult;
    }

    public ArticlePack getPack() {
        return pack;
    }

    public void setPack(ArticlePack pack) {
        this.pack = pack;
    }

    public ContenuPack getContenu() {
        return contenu;
    }

    public void setContenu(ContenuPack contenu) {
        this.contenu = contenu;
    }

    public YvsBaseArticlesTemplate getSelectTemplate() {
        return selectTemplate;
    }

    public void setSelectTemplate(YvsBaseArticlesTemplate selectTemplate) {
        this.selectTemplate = selectTemplate;
    }

    public ArticleDescription getDescription() {
        return description;
    }

    public void setDescription(ArticleDescription description) {
        this.description = description;
    }

    public boolean isParamDate() {
        return paramDate;
    }

    public void setParamDate(boolean paramDate) {
        this.paramDate = paramDate;
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

    public YvsBasePlanTarifaire getSelectTarif() {
        return selectTarif;
    }

    public void setSelectTarif(YvsBasePlanTarifaire selectTarif) {
        this.selectTarif = selectTarif;
    }

    public CodeBarre getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(CodeBarre codeBarre) {
        this.codeBarre = codeBarre;
    }

    public Boolean getDisplayId() {
        return displayId;
    }

    public void setDisplayId(Boolean displayId) {
        this.displayId = displayId;
    }

    public String getCurrentPhoto() {
        return currentPhoto;
    }

    public void setCurrentPhoto(String currentPhoto) {
        this.currentPhoto = currentPhoto;
    }

    public String getCurrentPhotoExtension() {
        return currentPhotoExtension;
    }

    public void setCurrentPhotoExtension(String currentPhotoExtension) {
        this.currentPhotoExtension = currentPhotoExtension;
    }

    public long getFamillePrint() {
        return famillePrint;
    }

    public void setFamillePrint(long famillePrint) {
        this.famillePrint = famillePrint;
    }

    public long getPointPrint() {
        return pointPrint;
    }

    public void setPointPrint(long pointPrint) {
        this.pointPrint = pointPrint;
    }

    public long getGroupePrint() {
        return groupePrint;
    }

    public void setGroupePrint(long groupePrint) {
        this.groupePrint = groupePrint;
    }

    public String getCategoriePrint() {
        return categoriePrint;
    }

    public void setCategoriePrint(String categoriePrint) {
        this.categoriePrint = categoriePrint;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public long getGroupeSearch() {
        return groupeSearch;
    }

    public void setGroupeSearch(long groupeSearch) {
        this.groupeSearch = groupeSearch;
    }

    public long getUniteSearch() {
        return uniteSearch;
    }

    public void setUniteSearch(long uniteSearch) {
        this.uniteSearch = uniteSearch;
    }

    public String getCategorieSearch() {
        return categorieSearch;
    }

    public void setCategorieSearch(String categorieSearch) {
        this.categorieSearch = categorieSearch;
    }

    public long getFamilleSearch() {
        return familleSearch;
    }

    public void setFamilleSearch(long familleSearch) {
        this.familleSearch = familleSearch;
    }

    public Boolean getActifSearch() {
        return actifSearch;
    }

    public void setActifSearch(Boolean actifSearch) {
        this.actifSearch = actifSearch;
    }

    public YvsBaseArticles getEntityArticle() {
        return entityArticle;
    }

    public void setEntityArticle(YvsBaseArticles entityArticle) {
        this.entityArticle = entityArticle;
    }

    public int getCurrentSmallPageArt() {
        return currentSmallPageArt;
    }

    public void setCurrentSmallPageArt(int currentSmallPageArt) {
        this.currentSmallPageArt = currentSmallPageArt;
    }

    public int getTotalSmallPageArt() {
        return totalSmallPageArt;
    }

    public void setTotalSmallPageArt(int totalSmallPageArt) {
        this.totalSmallPageArt = totalSmallPageArt;
    }

    public List<Object[]> getListCat() {
        return listCat;
    }

    public void setListCat(List<Object[]> listCat) {
        this.listCat = listCat;
    }

    public List<Long> getListIdCat() {
        return listIdCat;
    }

    public void setListIdCat(List<Long> listIdCat) {
        this.listIdCat = listIdCat;
    }

    public List<YvsBaseArticleCategorieComptableTaxe> getListGroupeTaxe() {
        return listGroupeTaxe;
    }

    public void setListGroupeTaxe(List<YvsBaseArticleCategorieComptableTaxe> listGroupeTaxe) {
        this.listGroupeTaxe = listGroupeTaxe;
    }

    public List<YvsArticleCompte> getListGroupeCompte() {
        return listGroupeCompte;
    }

    public void setListGroupeCompte(List<YvsArticleCompte> listGroupeCompte) {
        this.listGroupeCompte = listGroupeCompte;
    }

    public YvsBaseArticleCategorieComptableTaxe[] getTabTaxe() {
        return tabTaxe;
    }

    public void setTabTaxe(YvsBaseArticleCategorieComptableTaxe[] tabTaxe) {
        this.tabTaxe = tabTaxe;
    }

//    public List<YvsGroupeArtCatTaxes> getLtCompare() {
//        return ltCompare;
//    }
//
//    public void setLtCompare(List<YvsGroupeArtCatTaxes> ltCompare) {
//        this.ltCompare = ltCompare;
//    }
    public int getCurrentPageArt() {
        return currentPageArt;
    }

    public void setCurrentPageArt(int currentPageArt) {
        this.currentPageArt = currentPageArt;
    }

    public int getTotalPageArt() {
        return totalPageArt;
    }

    public void setTotalPageArt(int totalPageArt) {
        this.totalPageArt = totalPageArt;
    }

    public int getFirtResultArt() {
        return firtResultArt;
    }

    public void setFirtResultArt(int firtResultArt) {
        this.firtResultArt = firtResultArt;
    }

    public String getChaineSelectArt() {
        return chaineSelectArt;
    }

    public void setChaineSelectArt(String chaineSelectArt) {
        this.chaineSelectArt = chaineSelectArt;
    }

    public String getSearchArticle() {
        return searchArticle;
    }

    public void setSearchArticle(String searchArticle) {
        this.searchArticle = searchArticle;
    }

    public short getColSearch() {
        return colSearch;
    }

    public void setColSearch(short colSearch) {
        this.colSearch = colSearch;
    }

    public boolean isDisNextArt() {
        return disNextArt;
    }

    public void setDisNextArt(boolean disNextArt) {
        this.disNextArt = disNextArt;
    }

    public boolean isDisPrevArt() {
        return disPrevArt;
    }

    public void setDisPrevArt(boolean disPrevArt) {
        this.disPrevArt = disPrevArt;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<YvsStatExportEtat> getExports() {
        return exports;
    }

    public void setExports(List<YvsStatExportEtat> exports) {
        this.exports = exports;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDocIds() {
        return docIds;
    }

    public void setDocIds(String docIds) {
        this.docIds = docIds;
    }

    public Rabais getRabais() {
        return rabais;
    }

    public void setRabais(Rabais rabais) {
        this.rabais = rabais;
    }

    public YvsComRabais getSelectRabais() {
        return selectRabais;
    }

    public void setSelectRabais(YvsComRabais selectRabais) {
        this.selectRabais = selectRabais;
    }

    public String getFusionneUniteTo() {
        return fusionneUniteTo;
    }

    public void setFusionneUniteTo(String fusionneUniteTo) {
        this.fusionneUniteTo = fusionneUniteTo;
    }

    public List<String> getFusionnesUniteBy() {
        return fusionnesUniteBy;
    }

    public void setFusionnesUniteBy(List<String> fusionnesUniteBy) {
        this.fusionnesUniteBy = fusionnesUniteBy;
    }

    public String getFusionneTo() {
        return fusionneTo;
    }

    public void setFusionneTo(String fusionneTo) {
        this.fusionneTo = fusionneTo;
    }

    public List<String> getFusionnesBy() {
        return fusionnesBy;
    }

    public void setFusionnesBy(List<String> fusionnesBy) {
        this.fusionnesBy = fusionnesBy;
    }

    public List<YvsBaseArticles> getSecondaires() {
        return secondaires;
    }

    public void setSecondaires(List<YvsBaseArticles> secondaires) {
        this.secondaires = secondaires;
    }

    public YvsBaseArticleFournisseur getSelectArtFsseur() {
        return selectArtFsseur;
    }

    public void setSelectArtFsseur(YvsBaseArticleFournisseur selectArtFsseur) {
        this.selectArtFsseur = selectArtFsseur;
    }

    public ConditionnementFsseur getCondFsseur() {
        return condFsseur;
    }

    public void setCondFsseur(ConditionnementFsseur condFsseur) {
        this.condFsseur = condFsseur;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsBaseConditionnement getSelectCondit() {
        return selectCondit;
    }

    public void setSelectCondit(YvsBaseConditionnement selectCondit) {
        this.selectCondit = selectCondit;
    }

    public YvsBaseArticles getSelectArticle() {
        return selectArticle;
    }

    public void setSelectArticle(YvsBaseArticles selectArticle) {
        this.selectArticle = selectArticle;
    }

    public boolean isOnTarif() {
        return onTarif;
    }

    public void setOnTarif(boolean onTarif) {
        this.onTarif = onTarif;
    }

    public boolean isTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public String[] getTypesUnites() {
        return typesUnites;
    }

    public void setTypesUnites(String[] typesUnites) {
        this.typesUnites = typesUnites;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public boolean isStockage() {
        return stockage;
    }

    public void setStockage(boolean stockage) {
        this.stockage = stockage;
    }

    public PaginatorResult<YvsBaseArticles> getPa() {
        return pa;
    }

    public void setPa(PaginatorResult<YvsBaseArticles> pa) {
        this.pa = pa;
    }

    public List<YvsBaseArticles> getArticlesResult() {
        return articlesResult;
    }

    public void setArticlesResult(List<YvsBaseArticles> articlesResult) {
        this.articlesResult = articlesResult;
    }

    public List<YvsBaseArticleCategorieComptableTaxe> getListTaxes() {
        return listTaxes;
    }

    public void setListTaxes(List<YvsBaseArticleCategorieComptableTaxe> listTaxes) {
        this.listTaxes = listTaxes;
    }

    public YvsBaseArticleCategorieComptable getSelectArticleComptable() {
        return selectArticleComptable;
    }

    public void setSelectArticleComptable(YvsBaseArticleCategorieComptable selectArticleComptable) {
        this.selectArticleComptable = selectArticleComptable;
        update("data_taxe_article_categorie");
    }

    public GrilleRabais getGrille() {
        return grille;
    }

    public void setGrille(GrilleRabais grille) {
        this.grille = grille;
    }

    public PlanTarifaireClient getPlanTarif() {
        return planTarif;
    }

    public void setPlanTarif(PlanTarifaireClient planTarif) {
        this.planTarif = planTarif;
    }

    public boolean getOnTarif() {
        return onTarif;
    }

    public boolean isUpdateBorne() {
        return updateBorne;
    }

    public void setUpdateBorne(boolean updateBorne) {
        this.updateBorne = updateBorne;
    }

    public AutoComplete getAutoC() {
        return autoC;
    }

    public void setAutoC(AutoComplete autoC) {
        this.autoC = autoC;
    }

    public List<Articles> getListArticle_() {
        return listArticle_;
    }

    public void setListArticle_(List<Articles> listArticle_) {
        this.listArticle_ = listArticle_;
    }

    public TrancheVal getTranche() {
        return tranche;
    }

    public void setTranche(TrancheVal tranche) {
        this.tranche = tranche;
    }

    public List<YvsTranches> getTranchesRemise() {
        return tranchesRemise;
    }

    public void setTranchesRemise(List<YvsTranches> tranchesRemise) {
        this.tranchesRemise = tranchesRemise;
    }

    public List<YvsBaseArticles> getArticlesLoad() {
        return articlesLoad;
    }

    public void setArticlesLoad(List<YvsBaseArticles> articlesLoad) {
        this.articlesLoad = articlesLoad;
    }

    public CentreAnalytique getFamilleCout() {
        return familleCout;
    }

    public void setFamilleCout(CentreAnalytique familleCout) {
        this.familleCout = familleCout;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    public ArticlesCatComptable getArticleCategorieC() {
        return articleCategorieC;
    }

    public void setArticleCategorieC(ArticlesCatComptable articleCategorieC) {
        this.articleCategorieC = articleCategorieC;
    }

    public List<YvsBaseTaxes> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsBaseTaxes> taxes) {
        this.taxes = taxes;
    }

    public List<CentreAnalytique> getListCentreAnal() {
        return listCentreAnal;
    }

    public void setListCentreAnal(List<CentreAnalytique> listCentreAnal) {
        this.listCentreAnal = listCentreAnal;
    }

    public ArticleFournisseur getArticleFsseur() {
        return articleFsseur;
    }

    public void setArticleFsseur(ArticleFournisseur articleFsseur) {
        this.articleFsseur = articleFsseur;
    }

    public List<ArticlesCatComptable> getListArticleCatC() {
        return listArticleCatC;
    }

    public void setListArticleCatC(List<ArticlesCatComptable> listArticleCatC) {
        this.listArticleCatC = listArticleCatC;
    }

    public long getIdCatC() {
        return idCatC;
    }

    public void setIdCatC(long idCatC) {
        this.idCatC = idCatC;
    }

    public long getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(long idCompte) {
        this.idCompte = idCompte;
    }

    public List<GroupeDeProduit> getListGroupe() {
        return listGroupe;
    }

    public void setListGroupe(List<GroupeDeProduit> listGroupe) {
        this.listGroupe = listGroupe;
    }

    public List<GroupeDeProduit> getListClassStat() {
        return listClassStat;
    }

    public void setListClassStat(List<GroupeDeProduit> listClassStat) {
        this.listClassStat = listClassStat;
    }

    public List<GroupeDeProduit> getListCond() {
        return listCond;
    }

    public void setListCond(List<GroupeDeProduit> listCond) {
        this.listCond = listCond;
    }

    public List<YvsBaseArticles> getListArticle() {
        return listArticle;
    }

    public void setListArticle(List<YvsBaseArticles> listArticle) {
        this.listArticle = listArticle;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public double getBorne() {
        return borne;
    }

    public void setBorne(double borne) {
        this.borne = borne;
    }

    public double getValremise() {
        return valremise;
    }

    public void setValremise(double valremise) {
        this.valremise = valremise;
    }

    public String getModelTranche() {
        return modelTranche;
    }

    public void setModelTranche(String modelTranche) {
        this.modelTranche = modelTranche;
    }

    public boolean isRenderOnglet() {
        return renderOnglet;
    }

    public void setRenderOnglet(boolean renderOnglet) {
        this.renderOnglet = renderOnglet;
    }

    public String getDefaultButon() {
        return defaultButon;
    }

    public void setDefaultButon(String defaultButon) {
        this.defaultButon = defaultButon;
    }

    public BorneTranche getNewBorne() {
        return newBorne;
    }

    public void setNewBorne(BorneTranche newBorne) {
        this.newBorne = newBorne;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public YvsBaseParametre getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsBaseParametre currentParam) {
        this.currentParam = currentParam;
    }

    public boolean isCalculPr() {
        return calculPr;
    }

    public void setCalculPr(boolean calculPr) {
        this.calculPr = calculPr;
    }

//    public List<YvsDictionnaire> getListVilles() {
//        return listVilles;
//    }
//
//    public void setListVilles(List<YvsDictionnaire> listVilles) {
//        this.listVilles = listVilles;
//    }
//
//    public List<YvsDictionnaire> getListPays() {
//        return listPays;
//    }
//
//    public void setListPays(List<YvsDictionnaire> listPays) {
//        this.listPays = listPays;
//    }
    /**
     * **Template articles
     *
     **
     * @return
     */
    public void loadAlls_art() {
        if (currentParam == null) {
            loadParametreBase();
        }
    }

    public void loadAlls() {
        loadAlls_art();
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.loadArticle(null, entityArticle, true, true);
        }
    }

    @Override
    public void loadAll() {
        loadInfosWarning(false);
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
            loadAllArticle(true, true);
        }
    }

    public void loadAllAsCatalogue() {
        loadOrder(true, true);
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllArticle(true, true);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbResult())) {
            setOffset(0);
        }
        List<YvsBaseArticles> re = paginator.parcoursDynamicData("YvsBaseArticles", "y", "y.refArt", getOffset(), dao);
        if (!re.isEmpty()) {
            if (re.get(0).getTauxEcartPr() > 0) {
                calculPr = true;
            } else {
                calculPr = false;
            }
            onSelectObject(re.get(0));
        }
    }

    public void loadOneArticle(boolean avance) {
        if (avance) {
            if (currentSmallPageArt < totalSmallPageArt) {
                currentSmallPageArt++;
                YvsBaseArticles a = listArticle.get(currentSmallPageArt);
                loadArticle(a);
            }
        } else {
            if (currentSmallPageArt > 0) {
                currentSmallPageArt--;
                YvsBaseArticles a = listArticle.get(currentSmallPageArt);
                loadArticle(a);
            }
        }
    }

    public void addParamCategorie() {
        ParametreRequete p = new ParametreRequete("y.categorie", "categorie", null);
        if (categorieSearch != null ? categorieSearch.trim().length() > 0 : false) {
            p.setObjet(categorieSearch);
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void _addParamCategorie() {
        ParametreRequete p = new ParametreRequete("y.categorie", "categorie", null);
        if (categorieSearch != null ? categorieSearch.trim().length() > 0 : false) {
            p.setObjet(categorieSearch);
            p.setOperation("=");
            p.setPredicat("AND");
        }
        pa.addParam(p);
        loadActifArticle(false, true);
    }

    public void addParamCategorie_(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.categorie", "categorie", null);
        if (ev.getNewValue() != null) {
            p.setObjet((String) ev.getNewValue());
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void addParamGroupe() {
        ParametreRequete p = new ParametreRequete("y.groupe", "groupe", null);
        if (groupeSearch > 0) {
            p.setObjet(new YvsBaseGroupesArticle(groupeSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void _addParamGroupe() {
        ParametreRequete p = new ParametreRequete("y.groupe", "groupe", null);
        if (groupeSearch > 0) {
            p.setObjet(new YvsBaseGroupesArticle(groupeSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        pa.addParam(p);
        loadActifArticle(false, true);
    }

    public void addParamFamille() {
        ParametreRequete p = new ParametreRequete("y.famille", "famille", null);
        if (familleSearch > 0) {
            p.setObjet(new YvsBaseFamilleArticle(familleSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void _addParamFamille() {
        ParametreRequete p = new ParametreRequete("y.famille", "famille", null);
        if (familleSearch > 0) {
            p.setObjet(new YvsBaseFamilleArticle(familleSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        pa.addParam(p);
        loadActifArticle(true, true);
    }

    public void addParamActif() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", null);
        if (actifSearch != null) {
            p.setObjet(actifSearch);
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void addParamConditionnement() {
        ParametreRequete p = new ParametreRequete("y.uniteStockage", "stockage", null);
        if (uniteSearch > 0) {
            p.setObjet(new YvsBaseUniteMesure(uniteSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void addParamClasseStat() {
        ParametreRequete p = new ParametreRequete("y.classe1", "classe", null);
        paginator.addParam(new ParametreRequete("y.id", "classe", null));
        if (classeSearch > 0) {
            p = new ParametreRequete(null, "classe", classeSearch, "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.classe1", "classe", new YvsBaseClassesStat(classeSearch), "=", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.classe2", "classe", new YvsBaseClassesStat(classeSearch), "=", "OR"));
        } else if (classeSearch == -1) {
            /**
             * Nous soe obligé d'employer deux requetes parceque JPA
             * n'interprète pas bien les LEFT JOIN*
             */
//            p = new ParametreRequete(null, "classe", classeSearch, "=", "AND");
//            p.getOtherExpression().add(new ParametreRequete("y.classe1.id", "classe", "IS NULL", "IS NULL", "AND"));
//            p.getOtherExpression().add(new ParametreRequete("y.classe2.id", "classe", "IS NULL", "IS NULL", "AND"));
            List<Long> ids = dao.loadNameQueries("YvsBaseArticles.findIdByNotClassStat", new String[]{"societe"}, new Object[]{currentAgence.getSociete().getId()});
            paginator.clear();
            if (ids.isEmpty()) {
                ids.add(-1L);
            } else if (ids.size() > 200) {
                ids = ids.subList(0, 200);
            }
            p = new ParametreRequete("y.id", "ids", ids, "IN", "AND");
        }
        paginator.addParam(p);
        loadAllArticle(false, true);

    }

    public void addParamIds() {
        addParamIds(true);
        loadAllArticle(false, true);
    }

    public void addParamMethVal() {
        ParametreRequete p = new ParametreRequete("y.methodeVal", "methodeVal", null, "=", "AND");
        if (Util.asString(methValSearch)) {
            p.setObjet(methValSearch);
            if (methValSearch.equals(Constantes.NOTHING)) {
                p = new ParametreRequete(null, "methodeVal", methValSearch, "=", "AND");
                p.getOtherExpression().add(new ParametreRequete("y.methodeVal", "methodeVal", methValSearch, "IS NULL", "OR"));
                p.getOtherExpression().add(new ParametreRequete("TRIM(y.methodeVal)", "methodeVal_Empty", "", "=", "OR"));
            }
        }
        paginator.addParam(p);
        loadAllArticle(false, true);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllArticle(true, true);
    }

    public void chooseDateSearch(ValueChangeEvent ev) {
        paramDate = (Boolean) ev.getNewValue();
        addParaDate(paramDate);
    }

    public void addParamDate1(SelectEvent ev) {
        addParaDate(paramDate);
    }

    public void addParamDate2() {
        addParaDate(paramDate);
    }

    private void addParaDate(boolean b) {
        ParametreRequete p = new ParametreRequete("y.dateSave", "dateSave", null, " BETWEEN ", "AND");
        if (b) {
            if (dateDebut != null && dateFin != null) {
                if (dateDebut.before(dateFin) || dateDebut.equals(dateFin)) {
                    p.setObjet(dateDebut);
                    p.setOtherObjet(dateFin);
                }
            }
        }
        paginator.addParam(p);
        loadAllArticle(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllArticle(true, true);
    }

    // Choosepaginator pour les articles actifs
    public void _choosePaginator(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            try {
                Long v = (Long) ev.getNewValue();
                pa.setRows((v != null) ? ((v.intValue())) : 0);
            } catch (Exception ex) {
                int v = (int) ev.getNewValue();
                pa.setRows(v);
            }
        }
        loadActifArticle(true, true);
    }

    @Override
    public void onSelectDistant(YvsBaseArticles y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Articles", "modDonneBase", "smenProduit", true);
            }
        }
    }

    public void loadAllArticle(boolean avancer, boolean init) {
//        listAgences = dao.loadNameQueries("YvsAgences.findBySocieteAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        listArticle_.clear();
        paginator.addParam(new ParametreRequete("y.famille.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        listArticle = paginator.executeDynamicQuery("y", "y", "YvsBaseArticles y JOIN FETCH y.famille LEFT JOIN FETCH y.groupe "
                + " LEFT JOIN FETCH y.classe1 C1 LEFT JOIN FETCH y.classe2 C2", "y.refArt", avancer, init, (int) imax, dao);
        totalSmallPageArt = listArticle.size();
        images.clear();
        images.add("temp/1.png");
        images.add("temp/2.png");
        images.add("temp/3.png");
        if (listArticle != null ? listArticle.size() == 1 : false) {
            selectOneArticle(listArticle.get(0));
            update("head_form_article");
            update("main_grid_article");
            update("middle_form_article");
            update("tbvArticle");
            execute("collapseForm('article')");
        } else {
            execute("collapseList('article');collapseList('nav_article')");
        }
    }

    public void loadActifArticlePFandNEGOCE() {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", true);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        ParametreRequete p0 = new ParametreRequete(null, "categorie", "XXX", "=", "AND");
        p0.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie", Constantes.CAT_PF, "=", "OR"));
        p0.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie1", Constantes.CAT_MARCHANDISE, "=", "OR"));
        p0.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie2", Constantes.CAT_SERVICE, "=", "OR"));
        paginator.addParam(p0);
        loadAllArticle(true, true);
    }

    public void loadActifArticle(String type) {
        if (type != null ? type.trim().length() > 0 : false) {
            switch (type) {
                case "A":
                    addParamCategorieMp();
                    break;
                case "V":
                    addParamCategoriePf();
                    break;
                case "P":
                    addParamCategorieProd();
                    break;
                case "PP":
                    addParamCategoriePlanProd();
                    break;
            }
        }
        loadActifArticle(true, true);
    }

    public void loadActifArticle(boolean avancer, boolean init) {
        pa.addParam(new ParametreRequete("y.famille.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        pa.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        articlesResult = pa.executeDynamicQuery("y", "y", "YvsBaseArticles y JOIN FETCH y.famille", "y.refArt", avancer, init, dao);
    }

    @Override
    public boolean controleFiche(Articles bean) {
        if ((bean.getRefArt() == null) ? true : bean.getRefArt().trim().equals("")) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner la Référence");
            return false;
        }
        if ((bean.getDesignation() == null) ? true : bean.getDesignation().trim().equals("")) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner la Désignation");
            return false;
        }
        if (bean.getCategorie() == null) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner la catégorie de l'article");
            return false;
        }

        if (bean.getFamille().getId() <= 0) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner la famille");
            return false;
        }
        champ = new String[]{"societe", "code", "id"};
        val = new Object[]{currentAgence.getSociete(), bean.getRefArt(), bean.getId()};
        Long nb = (Long) dao.loadObjectByNameQueries("YvsBaseArticles.controlByCode", champ, val);
        if (nb != null ? nb > 0 : false) {
            getErrorMessage("Cette reference existe déja");
            return false;
        }
        if (entityArticle != null ? entityArticle.getId() > 0 : false) {
            if ((entityArticle.getCategorie().equals(Constantes.CAT_SERVICE) || bean.getCategorie().equals(Constantes.CAT_SERVICE)) && !entityArticle.getCategorie().equals(bean.getCategorie())) {
                champ = new String[]{"article"};
                val = new Object[]{entityArticle};
                nb = (Long) dao.loadObjectByNameQueries("YvsComContenuDocStock.countByArticle", champ, val);
                nb = nb != null ? nb : 0;
                if (nb != null ? nb < 1 : true) {
                    if (entityArticle.getCategorie().equals(Constantes.CAT_MARCHANDISE) || entityArticle.getCategorie().equals(Constantes.CAT_PF) || entityArticle.getCategorie().equals(Constantes.CAT_SERVICE)) {
                        Long count = (Long) dao.loadObjectByNameQueries("YvsComContenuDocVente.countByArticle", champ, val);
                        nb += (count != null ? count : 0);
                    }
                }
                if (nb != null ? nb < 1 : true) {
                    if (entityArticle.getCategorie().equals(Constantes.CAT_MARCHANDISE) || entityArticle.getCategorie().equals(Constantes.CAT_MP)) {
                        Long count = (Long) dao.loadObjectByNameQueries("YvsComContenuDocAchat.countByArticle", champ, val);
                        nb += (count != null ? count : 0);
                    }
                }
                if (nb != null ? nb < 1 : true) {
                    if (entityArticle.getCategorie().equals(Constantes.CAT_PF) || entityArticle.getCategorie().equals(Constantes.CAT_PSF)) {
                        Long count = (Long) dao.loadObjectByNameQueries("YvsProdDeclarationProduction.countByArticle", champ, val);
                        nb += (count != null ? count : 0);
                    }
                }
                if (nb != null ? nb < 1 : true) {
                    if (entityArticle.getCategorie().equals(Constantes.CAT_MP)) {
                        Long count = (Long) dao.loadObjectByNameQueries("YvsProdOfSuiviFlux.countByArticle", champ, val);
                        nb += (count != null ? count : 0);
                    }
                }
                if (nb != null ? nb > 0 : false) {
                    if (bean.getCategorie().equals(Constantes.CAT_SERVICE)) {
                        getErrorMessage("Vous ne pouvez pas définir cette article en tant que service. car il est déjà utilisé comme élément matériel");
                    } else {
                        getErrorMessage("Vous ne pouvez pas définir cette article en tant qu'élément matériel. car il est déjà utilisé comme service");
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public boolean controleFiche(Conditionnement bean) {
        if (bean.getPrix() <= 0 && bean.getPrixAchat() <= 0) {
            getErrorMessage("Vous devez precisez le prix");
            return false;
        }
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            if (saveNew()) {
                bean.setArticle(article);
            }
        }
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner l'article");
            return false;
        }
        if (bean.getUnite() != null ? bean.getUnite().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner le conditionnement");
            return false;
        }
        if (bean.getId() > 0) {
            if (!autoriser("base_conditionnement_update")) {
                openNotAcces();
                return false;
            }
        }
        champ = new String[]{"article", "unite"};
        val = new Object[]{new YvsBaseArticles(bean.getArticle().getId()), new YvsBaseUniteMesure(bean.getUnite().getId())};
        YvsBaseConditionnement la = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findByArticleUnite", champ, val);
        if (la != null) {
            if (la.getId() != bean.getId()) {
                getErrorMessage("Ce conditionnement existe déja");
                return false;
            }
        }
        return true;
    }

    public boolean controleFiche(ArticleDescription bean) {
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner l'article");
            return false;
        }
        if (bean.getTitre() != null ? bean.getTitre().trim().length() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner le titre");
            return false;
        }
        if (bean.getDescription() != null ? bean.getDescription().trim().length() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner la description");
            return false;
        }
        return true;
    }

    public boolean controleFiche(ArticlePack bean) {
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner l'article");
            return false;
        }
        if (bean.getDesignation() != null ? bean.getDesignation().trim().length() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner la désignation");
            return false;
        }
        return true;
    }

    public boolean controleFiche(ContenuPack bean) {
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner l'article");
            return false;
        }
        if (bean.getPack() != null ? bean.getPack().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez selectionner le pack");
            return false;
        }
        return true;
    }

    public boolean controleFiche(ArticleAnalytique bean) {
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner l'article");
            return false;
        }
        if (bean.getCentre() != null ? bean.getCentre().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez selectionner le centre analytique");
            return false;
        }
        double taux = bean.getTaux();
        for (YvsBaseArticleAnalytique a : article.getAnalytiques()) {
            if (!a.getId().equals(bean.getId()) ? a.getCentre().getId().equals(bean.getCentre().getId()) : false) {
                getErrorMessage("Formulaire invalide", "Vous avez deja associé ce centre analytique");
                return false;
            }
            if (!a.getId().equals(bean.getId())) {
                taux += a.getCoefficient();
            }
        }
        if (taux > 100) {
            getErrorMessage("Formulaire invalide", "La repartition analytique ne peut pas etre supérieur à 100%");
            return false;
        }
        return true;
    }

    public boolean controleFiche(Rabais bean, YvsBaseConditionnementPoint conditionnement) {
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez entrer une valeur de rabais positive");
            return false;
        }
        if (conditionnement != null ? conditionnement.getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer le conditionnement");
            return false;
        }
        //vérifie l'existence d'un plan de rabais dans une période qui se chevauche
        champ = new String[]{"article", "unite", "point", "debut", "fin"};
        val = new Object[]{selectUnite.getArticle(), selectUnite, new YvsBasePointVente(rabais.getPoint().getId()), bean.getDateDebut(), bean.getDateFin()};
        YvsComRabais r = (YvsComRabais) dao.loadOneByNameQueries("YvsComRabais.findOtherByPointArticleUnit", champ, val);
        if (r != null) {
            if (r.getId() != bean.getId()) {
                getErrorMessage("Vous avez déjà planifié un rabais pour cet article dans une période semblable !");
                return false;
            }
        }

        return true;
    }

    @Override
    public Articles recopieView() {
        Articles ar = new Articles();
        ar.setId(article.getId());
        ar.setRefArt(article.getRefArt());
        ar.setActif(article.isActif());
        ar.setFabricant(article.getFabricant());
        ar.setFamille(article.getFamille());
        ar.setMethodeVal(article.getMethodeVal());
        ar.setGroupe(article.getGroupe());
        ar.setCategorie(article.getCategorie());
        ar.setChangePrix(article.isChangePrix());
        ar.setClasseStat(article.getClasseStat());
        ar.setCoefficient(article.getCoefficient());
        ar.setTemplate(article.getTemplate());
        ar.setDescription(article.getDescription());
        ar.setDesignation(article.getDesignation());
        ar.setMasseNet(article.getMasseNet());
        ar.setPhoto1(article.getPhoto1());
        ar.setPhoto2(article.getPhoto2());
        ar.setPhoto3(article.getPhoto3());
        ar.setPua(article.getPua());
        ar.setPuv(article.getPuv());
        ar.setPuvTtc(article.isPuvTtc());
        ar.setPuaTtc(article.isPuaTtc());
        ar.setPuvMin(article.getPuvMin());
        ar.setSuiviEnStock(article.isSuiviEnStock());
        ar.setRemise(article.getRemise());
        ar.setUnite(article.getUnite());
        ar.setUniteVolume(article.getUniteVolume());
        ar.setUniteStockage(article.getUniteStockage());
        ar.setUniteVente(article.getUniteVente());
//                bean.setFamille(UtilProd.buildBeanFamilleArticle(listFamilleArticle.get(listFamilleArticle.indexOf(new YvsBaseFamilleArticle(bean.getFamille().getId())))));
        return ar;
    }

    @Override
    public boolean saveNew() {
        try {
            champ = new String[]{"article"};
            val = new Object[]{new YvsBaseArticles(article.getId())};
            if (controleFiche(article)) {
                IYvsBaseArticles impl = (IYvsBaseArticles) (new IEntitySax()).createInstance("IYvsBaseArticles", dao);
                entityArticle = UtilProd.buildEntityArticle(article);
                entityArticle.setAuthor(currentUser);
                if (article.getFamille().getId() > 0) {
                    ManagedFamilleArticle service = (ManagedFamilleArticle) giveManagedBean(ManagedFamilleArticle.class);
                    if (service != null) {
                        int idx = service.getFamilles().indexOf(entityArticle.getFamille());
                        if (idx >= 0) {
                            entityArticle.setFamille(service.getFamilles().get(idx));
                        }
                    }
                }
                if (article.getTemplate().getId() >= 0) {
                    ManagedTemplateArticle mt = (ManagedTemplateArticle) giveManagedBean(ManagedTemplateArticle.class);
                    if (mt != null) {
                        int idx = mt.getTemplates().indexOf(new YvsBaseArticlesTemplate(article.getTemplate().getId()));
                        if (idx >= 0) {
                            entityArticle.setTemplate(mt.getTemplates().get(idx));
                        }
                    }
                }
                if (impl != null) {
                    impl.setNiveauAcces(currentNiveau);
                    impl.setAgence(currentAgence);
                    if (article.getId() <= 0) {
                        entityArticle.setId(null);
                        //charge les catégorie comptable    
                        ResultatAction<YvsBaseArticles> re = impl.save(entityArticle);
                        if (re != null ? re.isResult() : false) {
                            entityArticle = re.getEntity();
                            article.setId(entityArticle.getId());
                            article.setListArtDepots(entityArticle.getYvsBaseArticleDepotList());
                            ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                            if (w != null) {
                                w.setArticles_depot(article.getListArtDepots());
                            }
                        } else {
                            getErrorMessage(re.getMessage());
                        }
                        update("tableArticleCatC");
                    } else {
                        ResultatAction<YvsBaseArticles> re = impl.update(entityArticle);
                        if (re != null ? re.isResult() : false) {
                            entityArticle = re.getEntity();
                        } else {
                            getErrorMessage(re.getMessage());
                        }
                    }
                } else {
                    getErrorMessage("Erreur Système !");
                    return false;
                }
                int idx = listArticle.indexOf(entityArticle);
                if (idx > -1) {
                    listArticle.set(idx, entityArticle);
                } else {
                    listArticle.add(0, entityArticle);
                }
                ManagedStockArticle service = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (service != null) {
                    service.setEntityArticle(entityArticle);
                }
                saveEquivalence(entityArticle);
                actionOpenOrResetAfter(this);
                succes();
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossivble");
            getException("Action", ex);
        }
        return false;
    }

    private void saveEquivalence(YvsBaseArticles y) {
        if (y != null) {
            if ((y.getUniteStockage() != null ? y.getUniteStockage().getId() > 0 : false) && (y.getUniteVente() != null ? y.getUniteVente().getId() > 0 : false)) {
                champ = new String[]{"unite", "uniteE"};
                val = new Object[]{y.getUniteStockage(), y.getUniteVente()};
                nameQueri = "YvsBaseTableConversion.findUniteCorrespondance";
                YvsBaseTableConversion t = (YvsBaseTableConversion) dao.loadOneByNameQueries(nameQueri, champ, val);
                if (t != null ? (t.getId() != null ? t.getId() < 1 : true) : true) {
                    ManagedUniteMesure s = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
                    if (s != null) {
                        if (s.saveNewEquivalence(y.getUniteStockage(), y.getUniteVente(), article.getTauxEquivalenceStock())) {

                        }
                    }
                }
            }
        }
    }

    public boolean saveNewConditionnement() {
        try {
            conditionnement.setArticle(article);
            if (controleFiche(conditionnement)) {
                selectCondit = UtilProd.buildConditionnement(conditionnement, currentUser);
                if (conditionnement.getId() > 0) {
                    dao.update(selectCondit);
                } else {
                    //initialise le champs conditionnement vente à false
                    if (selectCondit.getByVente()) {
                        initCondVenteTofalse(article.getId());
                    }
                    selectCondit.setId(null);
                    selectCondit.setActif(true);
                    selectCondit = (YvsBaseConditionnement) dao.save1(selectCondit);
                    if (article.canSale()) {
                        if ((YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findCondVente", champ, val) == null) {
                            getWarningMessage("Vous devez paramétrer le conditionnement utiliser pour la vente !");
                        }
                    }
                    initConditionnementPoint(selectCondit);
                }
                int idx = article.getConditionnements().indexOf(selectCondit);
                if (idx > -1) {
                    article.getConditionnements().set(idx, selectCondit);
                } else {
                    article.getConditionnements().add(0, selectCondit);
                }
                idx = listArticle.indexOf(new YvsBaseArticles(article.getId()));
                if (idx > -1) {
                    int _idx = listArticle.get(idx).getConditionnements().indexOf(selectCondit);
                    if (_idx > -1) {
                        listArticle.get(idx).getConditionnements().set(_idx, selectCondit);
                    } else {
                        listArticle.get(idx).getConditionnements().add(0, selectCondit);
                    }
                }
                resetFicheCondit();
                succes();
                update("tbvArticle:blog_form_conditionnement_article");
                update("tbvArticle:data_conditionnement_article");
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage("Action impossivble");
            getException("Action", ex);
            return false;
        }
    }

    public void initConditionnementPoint(YvsBaseConditionnement unite) {
        try {
            if (currentAgence.getSociete().getVenteOnline()) {
                List<YvsBasePointVente> pointsOnline = null;
                switch (article.getCategorie()) {
                    case yvs.dao.salaire.service.Constantes.CAT_PF:
                    case yvs.dao.salaire.service.Constantes.CAT_MARCHANDISE:
                        pointsOnline = dao.loadNameQueries("YvsBasePointVente.findByVenteOnline", new String[]{"societe", "venteOnline"}, new Object[]{currentAgence.getSociete(), true});
                        break;
                }
                if (pointsOnline != null ? !pointsOnline.isEmpty() : false) {
                    YvsBaseArticlePoint y_a;
                    YvsBaseConditionnementPoint y_c;
                    for (YvsBasePointVente point : pointsOnline) {
                        y_a = (YvsBaseArticlePoint) dao.loadOneByNameQueries("YvsBaseArticlePoint.findByArticlePoint", new String[]{"article", "point"}, new Object[]{entityArticle, point});
                        if (y_a != null ? y_a.getId() < 1 : true) {
                            y_a = new YvsBaseArticlePoint(entityArticle, point);
                            y_a.setPuv(unite.getPrix());
                            y_a.setPuvMin(unite.getPrixMin());
                            y_a.setAuthor(currentUser);
                            y_a.setActif(true);
                            y_a.setSupp(false);
                            y_a.setNew_(true);
                            y_a.setConditionementVente(unite);
                            y_a = (YvsBaseArticlePoint) dao.save1(y_a);
                        }

                        y_c = new YvsBaseConditionnementPoint(y_a, unite);
                        y_c.setAuthor(currentUser);
                        y_c.setNaturePrixMin(unite.getNaturePrixMin());
                        y_c.setNatureRemise(Constantes.NATURE_MTANT);
                        y_c.setPrixMin(unite.getPrixMin());
                        y_c.setPuv(unite.getPrix());
                        y_c.setRemise(unite.getRemise());
                        y_c = (YvsBaseConditionnementPoint) dao.save1(y_c);
                    }
                }
            }
        } catch (Exception ex) {
            getException("ManagaedArticle (initConditionnementPoint)", ex);
        }
    }

    public void applyEcartPr() {
        try {
            List<Integer> ids = decomposeSelection(chaineSelectArt);
            String idString = "0";
            for (Integer index : ids) {
                idString += ", " + listArticle.get(index).getId();
            }
            String query = "UPDATE yvs_base_articles y SET taux_ecart_pr = ? FROM yvs_base_articles a INNER JOIN yvs_base_famille_article f ON a.famille = f.id";
            List<yvs.dao.Options> params = new ArrayList<yvs.dao.Options>() {
                {
                    add(new yvs.dao.Options(ecartApplyPr, 1));
                    add(new yvs.dao.Options(currentAgence.getSociete().getId(), 2));
                }
            };
            if (puMaxApplyPr > 0) {
                query += " INNER JOIN yvs_base_conditionnement c ON c.article = a.id";
            }
            query += " WHERE y.id = a.id AND f.societe = ?";
            if (puMaxApplyPr > 0) {
                if (typePuApplyPr.equals("V")) {
                    query += " AND c.prix";
                } else {
                    query += " AND c.prix_achat";
                }
                query += " BETWEEN ? AND ?";
                params.add(new yvs.dao.Options(puMinApplyPr, params.size() + 1));
                params.add(new yvs.dao.Options(puMaxApplyPr, params.size() + 1));
            }

            if (!idString.equals("0")) {
                query += " AND a.id IN (" + idString + ")";
            }
            if (Util.asString(categorieApplyPr)) {
                query += " AND a.categorie = ?";
                params.add(new yvs.dao.Options(categorieApplyPr, params.size() + 1));
            }
            if (familleApplyPr > 0) {
                query += " AND a.famille = ?";
                params.add(new yvs.dao.Options(familleApplyPr, params.size() + 1));
            }
            dao.requeteLibre(query, params.toArray(new yvs.dao.Options[params.size()]));
            succes();
        } catch (Exception ex) {
            getErrorMessage("Action impossivble");
            getException("applyEcartPr", ex);
        }
    }

    public void addTags() {
        try {
            article.setTags(Util.asString(article.getTags()) ? article.getTags() + ";" + tag : tag);
            tag = "";

            succes();
        } catch (Exception ex) {
            getErrorMessage("Action impossivble");
            getException("addTags", ex);
        }
    }

    public void selectArticleTag(YvsBaseArticles art) {
        bean = art;
        article = UtilProd.buildBeanArticles(bean);
        listTags();

    }

    public void addTag() {
        try {
            bean.setTags(Util.asString(bean.getTags()) ? bean.getTags() + ";" + tag : tag);
            dao.update(bean);
            tag = "";
            succes();
            bean = null;
        } catch (Exception ex) {
            getErrorMessage("Action impossivble");
            getException("addTags", ex);
        }
    }

    public void listTags() {
        if (bean != null) {
            article = UtilProd.buildBeanArticles(bean);
            if (yvs.dao.Util.asString(article.getTags())) {
                tags = Arrays.asList(article.getTags().split(";"));
            }
        } else {
            tags = new ArrayList<>();
        }
    }

    public void removeTags(String tag) {
        try {
            List<String> tags = Arrays.asList(article.getTags().split(";"));
            article.setTags("");
            for (String t : tags) {
                if (!t.equals(tag)) {
                    article.setTags(Util.asString(article.getTags()) ? article.getTags() + ";" + t : t);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossivble");
            getException("removeTags", ex);
        }
    }

    public void definedPrixProduction() {
        try {
            selectUnite.setDateUpdate(new Date());
            selectUnite.setAuthor(currentUser);
            dao.update(selectUnite);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Action impossivble");
            getException("definedPrixProduction", ex);
        }
    }

    private void initCondVenteTofalse(long idArt) {
        dao.requeteLibre("UPDATE yvs_base_conditionnement SET cond_vente=false WHERE article=?", new yvs.dao.Options[]{new yvs.dao.Options(idArt, 1)});
    }

    public void applyValueTemplate() {
        if (chaineSelectArt != null ? chaineSelectArt.trim().length() > 0 : false) {
            List<Integer> ids = decomposeSelection(chaineSelectArt);
            if (!ids.isEmpty()) {
                ManagedTemplateArticle service = (ManagedTemplateArticle) giveManagedBean(ManagedTemplateArticle.class);
                if (service != null) {
                    service.getSelectArticles().clear();
                    for (Integer index : ids) {
                        service.getSelectArticles().add(listArticle.get(index));
                    }
                    service.setSelecTemplate(selectTemplate);
                    TemplateArticles t = UtilProd.buildBeanTemplateArticles(selectTemplate);
                    service.cloneObject(service.getTemplateArticles(), t);
                    service.cloneObject(service.getTemp(), t);
                    for (YvsBaseArticleCategorieComptable y : service.getTemplateArticles().getComptes()) {
                        for (YvsBaseArticleCategorieComptableTaxe tax : y.getTaxes()) {
                            tax.setNew_(true);
                        }
                    }
                    service.applyArticleOnTemplate();
                }
                chaineSelectArt = "";
            }
        } else {
            if (article != null ? article.getTemplate() != null : false) {
                YvsBaseArticles y = saveAll(entityArticle, selectTemplate, selectTemplate.getComptes(), selectTemplate.getPlans_tarifaires(), true);
                if (y != null) {
                    succes();
                }
            }
        }
    }

    public void saveAll() {
        saveAll(entityArticle, entityArticle.getTemplate(), article.getListArticleCatComptable(), article.getPlans_tarifaires(), false);
    }

    public YvsBaseArticles saveAll(YvsBaseArticles current, YvsBaseArticlesTemplate template, List<YvsBaseArticleCategorieComptable> comptes, List<YvsBasePlanTarifaire> plans, boolean add) {
        try {
            if (current != null ? (current.getId() > 0 ? (template != null ? template.getId() > 0 : false) : false) : false) {
                List<YvsBaseArticleCategorieComptableTaxe> lt = new ArrayList<>();
                YvsBaseArticleCategorieComptable yc;
                champ = new String[]{"categorie", "compte", "article"};
                for (YvsBaseArticleCategorieComptable y : comptes) {
                    val = new Object[]{y.getCategorie(), y.getCompte(), current};
                    nameQueri = "YvsBaseArticleCategorieComptable.findByCategorieArticleCompte";
                    yc = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
                    if (yc != null ? (yc.getId() != null ? yc.getId() < 1 : true) : true) {
                        lt.clear();
                        lt.addAll(y.getTaxes());
                        y.getTaxes().clear();
                        y.setTemplate(null);
                        y.setArticle(current);
                        y.setId(null);
                        y = (YvsBaseArticleCategorieComptable) dao.save1(y);
                        for (YvsBaseArticleCategorieComptableTaxe s : lt) {
                            s.setArticleCategorie(y);
                            s.setId(null);
                            s = (YvsBaseArticleCategorieComptableTaxe) dao.save1(s);
                            y.getTaxes().add(s);
                        }
                        int idx = current.getComptes().indexOf(y);
                        if (idx > -1) {
                            current.getComptes().set(idx, y);
                        } else {
                            current.getComptes().add(0, y);
                        }
                        if (add ? article != null : false) {
                            idx = article.getListArticleCatComptable().indexOf(y);
                            if (idx > -1) {
                                article.getListArticleCatComptable().set(idx, y);
                            } else {
                                article.getListArticleCatComptable().add(0, y);
                            }
                            update("tbvArticle:tableArticleCatC");
                        }
                    } else {
                    }
                }
                YvsBasePlanTarifaire yp;
                List<YvsBasePlanTarifaireTranche> lp = new ArrayList<>();
                champ = new String[]{"categorie", "article"};
                for (YvsBasePlanTarifaire y : plans) {
                    val = new Object[]{y.getCategorie(), current};
                    nameQueri = "YvsBasePlanTarifaire.findByCategorieArticle";
                    yp = (YvsBasePlanTarifaire) dao.loadOneByNameQueries(nameQueri, champ, val);
                    if (yp != null ? (yp.getId() != null ? yp.getId() < 1 : true) : true) {
                        lp.clear();
                        lp.addAll(y.getGrilles());
                        y.getGrilles().clear();

                        y.setArticle(current);
                        y.setId(null);
                        y = (YvsBasePlanTarifaire) dao.save1(y);
                        for (YvsBasePlanTarifaireTranche s : lp) {
                            s.setPlan(y);
                            s.setId(null);
                            s = (YvsBasePlanTarifaireTranche) dao.save1(s);
                            y.getGrilles().add(s);
                        }
                        int idx = current.getPlans_tarifaires().indexOf(y);
                        if (idx > -1) {
                            current.getPlans_tarifaires().set(idx, y);
                        } else {
                            current.getPlans_tarifaires().add(0, y);
                        }
                        if (add ? article != null : false) {
                            idx = article.getPlans_tarifaires().indexOf(y);
                            if (idx > -1) {
                                article.getPlans_tarifaires().set(idx, y);
                            } else {
                                article.getPlans_tarifaires().add(0, y);
                            }
                            update("tbvArticle:data_plan_tarifaire_article");
                        }
                    }
                }
            }
            return current;
        } catch (Exception ex) {
            return null;
        }
    }

    public void saveNewArticle() {
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.setEntityArticle(entityArticle);
            m.setOnArticle(true);
            m.saveNewArticle();
        }
    }

    public void saveDescription() {
        try {
            description.setArticle(article);
            if (controleFiche(description)) {
                YvsBaseArticleDescription y = UtilProd.buildArticleDescription(description, currentUser);
                if (description.getId() < 1) {
                    y = (YvsBaseArticleDescription) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = article.getDescriptions().indexOf(y);
                if (idx < 0) {
                    article.getDescriptions().add(y);
                } else {
                    article.getDescriptions().set(idx, y);
                }
                update("tbvArticle:data_description_article");
                description = new ArticleDescription();
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible!!!");
            getException("saveDescription", ex);
        }
    }

    public void saveNewPack() {
        try {
            if (controleFiche(pack)) {
                YvsBaseArticlePack y = UtilProd.buildArticlePack(pack, currentUser);
                if (pack.getId() < 1) {
                    y = (YvsBaseArticlePack) dao.save1(y);
                    pack.setId(y.getId());
                } else {
                    dao.update(y);
                }
                int idx = article.getPacks().indexOf(y);
                if (idx < 0) {
                    article.getPacks().add(y);
                } else {
                    article.getPacks().set(idx, y);
                }
                update("tbvArticle:data_pack_article");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible!!!");
            getException("saveNewPark", ex);
        }
    }

    public void saveNewContenuPack() {
        try {
            contenu.setPack(pack);
            if (controleFiche(contenu)) {
                YvsBaseArticleContenuPack y = UtilProd.buildContenuPack(contenu, currentUser);
                if (contenu.getId() < 1) {
                    y = (YvsBaseArticleContenuPack) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = pack.getContenus().indexOf(y);
                if (idx < 0) {
                    pack.getContenus().add(y);
                } else {
                    pack.getContenus().set(idx, y);
                }
                update("tbvArticle:data_pack_article_contenu");
                succes();
                contenu = new ContenuPack();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible!!!");
            getException("saveNewPark", ex);
        }
    }

    public void saveNewRabais() {
        try {
            ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (article != null ? article.getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un article");
                return;
            }
            if (rabais.getPoint() != null ? rabais.getPoint().getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un point de vente");
                return;
            }
            if (selectUnite != null ? selectUnite.getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner un conditionnement");
                return;
            }
            //Vérifie si l'article est déjà rattaché au point de vente
            champ = new String[]{"article", "unite", "point"};
            val = new Object[]{selectUnite.getArticle(), selectUnite, new YvsBasePointVente(rabais.getPoint().getId())};
            YvsBaseConditionnementPoint y = (YvsBaseConditionnementPoint) dao.loadOneByNameQueries("YvsBaseConditionnementPoint.findArticlePointUnite", champ, val);
            if (y != null ? y.getId() < 1 : true) {
                //Save le conditionnement dans le point de vente                
                if (service != null) {
                    Articles art = new Articles();
                    cloneObject(art, article);
                    service.getArticle().setArticle(art);
                    service.getArticle().setConditionnement(UtilProd.buildSimpleBeanConditionnement(selectUnite));
                    service.getArticle().setActif(true);
                    service.setPointVente(rabais.getPoint());
                    service.getArticle().setPuv(selectUnite.getPrix());
                    service.getArticle().setRemise(selectUnite.getRemise());
                    service.setSelectPoint(UtilCom.buildPointVente(rabais.getPoint(), currentUser, currentAgence));
                    y = service.saveNewArticle(false);
//                    service.setSelectArticle(y);
                }
            }
            if (controleFiche(rabais, y)) {
                selectRabais = UtilCom.buildRabais(rabais, currentUser);
                IYvsComRabais impl = (IYvsComRabais) IEntitiSax.createInstance("IYvsComRabais", dao);
                selectRabais.setArticle(y);
                if (impl != null) {
                    if (selectRabais != null ? selectRabais.getId() > 0 : false) {
                        impl.update(selectRabais);
                    } else {
                        selectRabais.setId(null);
                        ResultatAction result = impl.save(selectRabais);
                        selectRabais = result != null ? result.isResult() ? (YvsComRabais) result.getData() : selectRabais : selectRabais;
                    }
                }
                if (service != null && selectRabais.getId() > 0) {
                    int idx = service.getListRabais().indexOf(selectRabais);
                    if (idx > -1) {
                        service.getListRabais().set(idx, selectRabais);
                    } else {
                        service.getListRabais().add(0, selectRabais);
                    }
                }
                update("data-rabais_article_point");
//                resetFicheRabais();
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanArticle() {
        System.err.println("tabIds = " + chaineSelectArt);
        if (chaineSelectArt != null) {
            List<Long> l = decomposeIdSelection(chaineSelectArt);
            List<YvsBaseArticles> list = new ArrayList<>();
            YvsBaseArticles bean;
            try {
                for (Long ids : l) {
                    bean = listArticle.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                }
                listArticle.removeAll(list);
                chaineSelectArt = "";
                succes();
                resetFiche();
            } catch (Exception ex) {
                chaineSelectArt = "";
                getErrorMessage("Impossible de terminer cette opération !", "des élément de votre sélection doivent être encore en liaison");
            }
        }
    }

    public void deleteBeanRabais(YvsComRabais y) {
        try {
            if (y != null) {
                dao.delete(y);
                ManagedPointVente w = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                if (w != null) {
                    w.getListRabais().remove(y);
                }
                if (y.getId().equals(rabais.getId())) {
                    resetFicheRabais();
                }
                succes();
                update("data_rabais_article_point");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : ", ex);
        }
    }

    public void resetFicheCondit() {
        conditionnement = new Conditionnement();
        if (article != null) {
            conditionnement.setPrixAchat(article.getPua());
            conditionnement.setPrix(article.getPuv());
            conditionnement.setPrixMin(article.getPuvMin());
        }
        selectCondit = null;
        update("tbvArticle:blog_form_conditionnement_article");
    }

    public void openViewArticleToUpdate(SelectEvent ev) {
        YvsBaseArticles art = (YvsBaseArticles) ev.getObject();
        if (art.getTauxEcartPr() > 0) {
            calculPr = true;
        } else {
            calculPr = false;
        }
        //récupère l'article et ses relations
        art = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findAllById", new String[]{"id"}, new Object[]{art.getId()});
        loadArticle(art);
        currentSmallPageArt = listArticle.indexOf(art);
        update("save_nav_article");
        execute("collapseForm('article')");
        execute("collapseForm('nav_article')");
        execute("collapseForm('conditionnement')");
        chaineSelectArt = listArticle.indexOf(art) + "";
    }

    public void selectOneArticle(YvsBaseArticles art) {
        loadArticle(art);
        currentSmallPageArt = listArticle.indexOf(art);
        update("save_nav_article");
    }

    public void openVieArtToUp(YvsBaseArticles art) {
        if (art != null) {
            if (!autoriser("base_article_delete")) {
                openNotAcces();
                return;
            }
            loadArticle(art);
            currentSmallPageArt = listArticle.indexOf(art);
            openDialog("dlgConfirmDeleteArt");
        }
    }

    public void toogleActiveArticle(YvsBaseArticles art) {
        if (art != null) {
            art.setActif(!art.getActif());
            art.setAuthor(currentUser);
            dao.update(art);
        }
    }

    public void toogleActiveCondi(YvsBaseConditionnement cond) {
        if (cond != null) {
            cond.setActif(!cond.getActif());
            cond.setAuthor(currentUser);
            dao.update(cond);
            succes();
        }
    }

    @Override
    public void onSelectObject(YvsBaseArticles y) {
        loadArticle(y);
        update("blog_form_article");
    }

    public void loadArticle(YvsBaseArticles art) {
        art.setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle1", new String[]{"article"}, new Object[]{art}));
//        art.setYvsBaseArticleDepotList(dao.loadNameQueries("YvsBaseArticleDepot.findByArticle", new String[]{"article"}, new Object[]{art}));
//        art.setPlans_tarifaires(dao.loadNameQueries("YvsBasePlanTarifaire.findByArticle", new String[]{"article"}, new Object[]{art}));
//        art.setFournisseurs(dao.loadNameQueries("YvsBaseArticleFournisseur.findByArticle1", new String[]{"article"}, new Object[]{art}));
//        art.setArticlesEquivalents(dao.loadNameQueries("YvsBaseArticleEquivalent.findByArticle1", new String[]{"article"}, new Object[]{art}));
//        art.setArticlesSubstitutions(dao.loadNameQueries("YvsBaseArticleSubstitution.findByArticle", new String[]{"article"}, new Object[]{art}));
//        art.setComptes(dao.loadNameQueries("YvsBaseArticleCategorieComptable.findByArticle", new String[]{"article"}, new Object[]{art}));
//        art.setDescriptions(dao.loadNameQueries("YvsBaseArticleDescription.findByArticle", new String[]{"article"}, new Object[]{art}));
//        art.setNomenclatures(dao.loadNameQueries("YvsProdNomenclature.findByCompose", new String[]{"article"}, new Object[]{art}));
//        art.setAnalytiques(dao.loadNameQueries("YvsBaseArticleAnalytique.findByArticle", new String[]{"article"}, new Object[]{art}));
//        art.setAvis(dao.loadNameQueries("YvsBaseArticlesAvis.findByArticle", new String[]{"article"}, new Object[]{art}));
//        art.setGammes(dao.loadNameQueries("YvsProdGammeArticle.findByArticle", new String[]{"article"}, new Object[]{art}));
//        art.setTarifs(dao.loadNameQueries("YvsBaseTarifPointLivraison.findByArticle", new String[]{"article"}, new Object[]{art}));

        entityArticle = art;
        Articles ar = UtilProd.buildBeanArticles(art);
        cloneObject(article, ar);
        images = article.getPhotos();
        currentPhoto = article.getPhoto();
        currentPhotoExtension = article.getPhotoExtension();
        if (article.getFamille().getId() > 0) {
            ManagedFamilleArticle m = (ManagedFamilleArticle) giveManagedBean(ManagedFamilleArticle.class);
            if (m != null) {
                if (!m.getFamilles().contains(art.getFamille())) {
                    m.getFamilles().add(art.getFamille());
                }
            }
        }
        if (article.getFabricant().getId() > 0) {
            ManagedTiers m = (ManagedTiers) giveManagedBean(ManagedTiers.class);
            if (m != null) {
                if (!m.getListTiers().contains(new YvsBaseTiers(article.getFabricant().getId()))) {
                    m.getListTiers().add(art.getFabriquant());
                }
            }
        }
        resetArticleFournisseur();
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.setOnArticle(true);
            m.resetFicheArticle();
            m.setEntityArticle(art);
            m.loadArticle(null, art, true, true);
        }
        resetFichePlanTarifaire();
        resetFicheCondit();
        resetFichePack();
        update("data_article_code_barre");
        update("formulaire_mainArticle");
    }

    private void addParamRef() {
        ParametreRequete p;
        if ((searchArticle == null) ? false : !searchArticle.trim().equals("")) {
            p = new ParametreRequete(null, "refArt", searchArticle + "%", "LIKE ", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.refArt)", "refArt", searchArticle.toUpperCase() + "%", "LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "designation", searchArticle.toUpperCase() + "%", "LIKE ", "OR"));
        } else {
            p = new ParametreRequete("refArt", "refArt", null);
        }
        paginator.addParam(p);
    }

    public void findArticle() {
        addParamRef();
        loadAllArticle(false, true);
    }

    public void findArticle_() {
        addParamRef();
        addParamActif();
    }

    public Articles findArticleActif(String num, boolean open) {
        Articles a = new Articles();
        a.setRefArt(num);
        a.setError(true);
        ParametreRequete p = new ParametreRequete("y.refArt", "refArt", null, "LIKE", "AND");
        if (num != null ? num.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "refArt", num.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.refArt)", "refArt", num.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "designation", num.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadActifArticlePFandNEGOCE();
        if (listArticle.size() > 1) {
            if (open) {
                openDialog("dlgListArticle");
            }
        } else {
            a = (!listArticle.isEmpty()) ? UtilProd.buildSimpleBeanArticles(listArticle.get(0)) : null;
        }
        if (a != null ? a.getId() < 1 : false) {
            a.setRefArt(num);
            a.setError(true);
        }
        return a;
    }
    /*
     --------------------------------------
     END ARTICLE
     --------------------------------------
     */
    /*
     --------------------------------------
     BEGIN ARTICLE SUBSTITUTION
     --------------------------------------
     */

    private String choixSourceArt;

    public String getChoixSourceArt() {
        return choixSourceArt;
    }

    public void setChoixSourceArt(String choixSourceArt) {
        this.choixSourceArt = choixSourceArt;
    }

    public void loadSubstitutionOrDerive(String type) {
        if (type != null ? type.trim().length() > 0 : false) {
            choixSourceArt = type;
            if (article.getId() > 0) {
                secondaires = dao.loadNameQueries("YvsBaseArticles.findByNotId", new String[]{"societe", "id"}, new Object[]{currentAgence.getSociete(), article.getId()});
                openDialog("dlgArticleList");
                update("tab_article_choix");
            } else {
                getErrorMessage("Aucun article principal n'a été selectionné !");
            }
        }
    }

    public boolean possibleAddSubstitutionOrDerive(YvsBaseArticles art) {
        if (choixSourceArt != null) {
            if (article.getId() > 0) {
                if (choixSourceArt.equals("AS")) {
                    for (YvsBaseArticleSubstitution y : art.getArticlesSubstitutions()) {
                        if (y.getArticleSubstitution().equals(art) && y.getArticle().getId().equals(article.getId())) {
                            return false;
                        }
                    }
                } else {
                    for (YvsBaseArticleEquivalent y : art.getArticlesEquivalents()) {
                        if (y.getArticleEquivalent().equals(art) && y.getArticle().getId().equals(article.getId())) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean addArticleSubstitutionOrDerive(YvsBaseArticles art) {
        if (choixSourceArt != null) {
            if (article.getId() > 0) {
                if (choixSourceArt.equals("AS")) {
                    YvsBaseArticleSubstitution entity = new YvsBaseArticleSubstitution();
                    entity.setArticle(new YvsBaseArticles(article.getId()));
                    entity.setArticleSubstitution(art);
                    entity.setAuthor(currentUser);
                    entity = (YvsBaseArticleSubstitution) dao.save1(entity);
                    article.getArticlesSubstitutions().add(entity);
                    art.getArticlesSubstitutions().add(entity);
                    succes();
                } else {
                    YvsBaseArticleEquivalent entity = new YvsBaseArticleEquivalent();
                    entity.setArticle(new YvsBaseArticles(article.getId()));
                    entity.setArticleEquivalent(art);
                    entity.setAuthor(currentUser);
                    entity = (YvsBaseArticleEquivalent) dao.save1(entity);
                    article.getArticlesEquivalents().add(entity);
                    art.getArticlesEquivalents().add(entity);
                    succes();
                }
                update("tab_article_choix");
            } else {
                getErrorMessage("Aucun article principal n'a été selectionné !");
            }
        } else {
            getErrorMessage("Aucun article principal n'a été selectionné !");
        }
        return true;
    }

    public boolean removeArticleSubstitutionOrDerive(YvsBaseArticles art) {
        try {
            if (choixSourceArt != null) {
                if (article.getId() > 0) {
                    if (choixSourceArt.equals("AS")) {
                        List<YvsBaseArticleSubstitution> list = new ArrayList<>(art.getArticlesSubstitutions());
                        for (YvsBaseArticleSubstitution y : list) {
                            if (y.getArticleSubstitution().equals(art) && y.getArticle().equals(new YvsBaseArticles(article.getId()))) {
                                removeArtSubstitution(y);
                                art.getArticlesSubstitutions().remove(y);
                                break;
                            }
                        }
                    } else {
                        List<YvsBaseArticleEquivalent> list = new ArrayList<>(art.getArticlesEquivalents());
                        for (YvsBaseArticleEquivalent y : list) {
                            if (y.getArticleEquivalent().equals(art) && y.getArticle().equals(new YvsBaseArticles(article.getId()))) {
                                removeArtDerive(y);
                                art.getArticlesEquivalents().remove(y);
                                break;
                            }
                        }
                    }
                    update("tab_article_choix");
                } else {
                    getErrorMessage("Aucun article principal n'a été selectionné !");
                }
            } else {
                getErrorMessage("Aucun article principal n'a été selectionné !");
            }
            return true;
        } catch (Exception e) {
            Logger.getLogger(ManagedArticles.class.getName()).log(Level.SEVERE, null, e);
            getErrorMessage("impossible de terminer ! ");
        }
        return false;
    }

    public void removeArtSubstitution(YvsBaseArticleSubstitution art) {
        try {
            art.setAuthor(currentUser);
            dao.delete(art);
            article.getArticlesSubstitutions().remove(art);
            succes();
        } catch (Exception e) {
            Logger.getLogger(ManagedArticles.class.getName()).log(Level.SEVERE, null, e);
            getErrorMessage("impossible de terminer ! ");
        }

    }

    public void removeArtDerive(YvsBaseArticleEquivalent art) {
        try {
            art.setAuthor(currentUser);
            dao.delete(art);
            article.getArticlesEquivalents().remove(art);
            succes();
        } catch (Exception e) {
            Logger.getLogger(ManagedArticles.class.getName()).log(Level.SEVERE, null, e);
            getErrorMessage("impossible de terminer ! ");
        }

    }


    /*
     --------------------------------------
     END ARTICLE SUBSTITUTION
     --------------------------------------
     */
    /*
     --------------------------------------
     BEGIN ARTICLE EQUIVALENT
     --------------------------------------
     */
//    public boolean addArticleEquvalent() {
//        if (article.getId() != 0 && article1Equivalent.getId() != 0) {
//            
//        } else {
//            getErrorMessage("Impossible de continuer !", "la fiche article est incohérente, ou l'article equivalent n'a pas été choisi !");
//        }
//        article1Equivalent = new Articles();
//        return true;
//    }
//    public void removeArtEq(int indexLine) {
//        try {
//            Articles a = article.getArticlesEquivalents().remove(indexLine);
//            dao.delete(new YvsBaseArticleEquivalent(a.getIdArtEquivalent()));
//            article.getArticlesEquivalents().remove(indexLine);
//            update("tab_art_equivalent");
//        } catch (Exception e) {
//            getErrorMessage("impossible de terminer ! ");
//        }
//
//    }
//
//    public void removeAllArtEq() {
//        for (int i = 0; i < article.getArticlesEquivalents().size(); i++) {
//            removeArtEq(i);
//        }
//
//    }

    /*
     --------------------------------------
     END ARTICLE EQUIVALENT
     --------------------------------------
     */
    public ArticleFournisseur recopieViewArticleFournisseur() {
        ArticleFournisseur r = new ArticleFournisseur();
        r.setDelaiLivraison(articleFsseur.getDelaiLivraison());
        r.setDureeGarantie(articleFsseur.getDureeGarantie());
        return r;
    }

//    private ArticleDepot buildArticleEmplacement(YvsBaseArticleDepot gr) {
//        ArticleDepot g = new ArticleDepot(gr.getId());
//        g.setId(gr.getId());
//        g.setEmplacement(UtilProd.buildBeanEmplacement(gr.getDepot()));
//        g.setModeAppro(gr.getModeAppro());
//        g.setNombreJour(gr.getIntervalApprov());
//        g.setStockMax(gr.getStockMax());
//        g.setStockMin(gr.getStockMin());
//        return g;
//    }
//
//    private PlanTarifGroup buildPlanTarif(YvsPlanTarifGroupe ta) {
//        PlanTarifGroup t = new PlanTarifGroup();
//        TrancheVal tranch = null;
//        //si le plan tarifaire est en tranche, on charge les bornes
//        if (ta.getIdTranche() != null) {
//            tranch = new TrancheVal();
//            tranch.setId(ta.getIdTranche().getId());
//            tranch.setModelTranche(ta.getIdTranche().getModelTranche());
//            //récupération des bornes
//            List<BorneTranche> lb = new ArrayList<>();
//            for (YvsBorneTranches b : ta.getIdTranche().getYvsBorneTranchesList()) {
//                lb.add(buildBorne(b));
//            }
//            tranch.setListBorne(lb);
//        }
//        t.setIdCategorie(ta.getYvsCatTarif().getId());
//        t.setRefAgence(ta.getYvsAgences().getCodeagence());
//        t.setIdAgence(ta.getYvsAgences().getId());
//        t.setIdTranche(tranch);
//        t.setPrix(ta.getPrix());
//        t.setRefCategorie(ta.getYvsCatTarif().getDesignation());
//        t.setRemise(ta.getRemiseVente());
//        return t;
//    }
    @Override
    public void updateBean() {

    }

    @Override
    public void populateView(Articles bean) {
        cloneObject(article, bean);
    }

    public void populateViewArticleFsseur(ArticleFournisseur bean) {
        cloneObject(articleFsseur, bean);
    }

    public void populateViewCondit(Conditionnement bean) {
        cloneObject(conditionnement, bean);
    }

    @Override
    public void selectOnView(Articles bean) {

    }

    public void resetFichePlanTarifaire() {
        planTarif = new PlanTarifaireClient();
        planTarif.setNatureCoefAugmentation(Constantes.NATURE_TAUX);
        planTarif.setRemise(article.getRemise());
        planTarif.setPuv(article.getPuv());
    }

    public void addAllCategories() {
        if (article.getId() > 0) {
            if (!autoriser("base_article_save_tarifaire")) {
                openNotAcces();
                return;
            }
            ManagedCategorieClt s = (ManagedCategorieClt) giveManagedBean(ManagedCategorieClt.class);
            if (s != null) {
                for (YvsBaseCategorieClient cc : s.getCategories()) {
                    champ = new String[]{"categorie", "article"};
                    val = new Object[]{cc, new YvsBaseArticles(article.getId())};
                    List<YvsBasePlanTarifaire> lp = dao.loadNameQueries("YvsBasePlanTarifaire.findByCategorieArticle", champ, val);
                    if (lp != null ? lp.isEmpty() : true) {
                        YvsBasePlanTarifaire c = new YvsBasePlanTarifaire();
                        c.setArticle(new YvsBaseArticles(article.getId()));
                        c.setPuv(article.getPuv());
                        c.setRemise(article.getRemise());
                        c.setActif(false);
                        c.setNatureCoefAugmentation(Constantes.NATURE_MTANT);
                        c.setCoefAugmentation(0.0);
                        c.setCategorie(cc);
                        c.setAuthor(currentUser);
                        c = (YvsBasePlanTarifaire) dao.save1(c);
                        article.getPlans_tarifaires().add(0, c);
                    }
                }
            }
            succes();
            resetFichePlanTarifaire();
            update("data_plan_tarifaire_article");
        } else {
            getErrorMessage("Vous devez precisez l'article");
        }
    }

    public boolean saveNewPlanTarifaire() {
        String action = planTarif.isUpdate() ? "Modification" : "Insertion";
        try {
            if (!autoriser("base_article_save_tarifaire")) {
                openNotAcces();
                return false;
            }
            ManagedCategorieClt service = (ManagedCategorieClt) giveManagedBean(ManagedCategorieClt.class);
            if (service != null) {
                int idx = service.getCategories().indexOf(new YvsBaseCategorieClient(planTarif.getCategorie().getId()));
                if (idx > -1) {
                    planTarif.setCategorie(UtilCom.buildBeanCategorieClient(service.getCategories().get(idx)));
                }
                planTarif.setArticle(article);
                YvsBasePlanTarifaire entity = service.saveNewPlanTarifaire(planTarif, true);
                if (!planTarif.isUpdate()) {
                    article.getPlans_tarifaires().add(0, entity);
                } else {
                    idx = article.getPlans_tarifaires().indexOf(entity);
                    if (idx >= 0) {
                        article.getPlans_tarifaires().set(idx, entity);
                    }
                }
                succes();
                resetFichePlanTarifaire();
                update("data_plan_tarifaire_article");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public void deleteBeanPlanTarif(YvsBasePlanTarifaire tarifSelect) {
        try {
            if (!autoriser("base_article_save_tarifaire")) {
                openNotAcces();
                return;
            }
            if (tarifSelect != null) {
                dao.delete(tarifSelect);
                article.getPlans_tarifaires().remove(tarifSelect);
                succes();
                update("data_plan_tarifaire_article");
            } else {
                getErrorMessage("Vous devez selectionner le tarif");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void loadOnViewPlanTarif(SelectEvent ev) {
        if (ev != null) {
            selectTarif = (YvsBasePlanTarifaire) ev.getObject();
            planTarif = UtilCom.buildBeanPlanTarifaireClient(selectTarif);
            update("blog_form_plan_tarifaire_article");
        }
    }

    public void unLoadOnViewPlanTarif(UnselectEvent ev) {
        resetFichePlanTarifaire();
        update("blog_form_plan_tarifaire_article");
    }

    public void loadOnViewRabais(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectRabais = (YvsComRabais) ev.getObject();
            rabais = UtilCom.buildBeanRabais(selectRabais);
            update("form_rabais_article_point");
        }
    }

    public void unLoadOnViewRabais(UnselectEvent ev) {
        resetFicheRabais();
    }

    public void genererPrix() {
        double puv = planTarif.getPuv();
        if (planTarif.getNatureCoefAugmentation().equals(Constantes.NATURE_TAUX)) {
            puv = puv + ((puv * planTarif.getCoefAugmentation()) > 0 ? ((puv * planTarif.getCoefAugmentation()) / 100) : 0);
        } else {
            puv = puv + planTarif.getCoefAugmentation();
        }
        planTarif.setPuvMin(puv - (planTarif.getPuv() - planTarif.getPuvMin()));
        planTarif.setPuv(puv);
    }

    public void genererReference() {
        if (article != null ? (article.getFamille() != null ? (article.getFamille().getId() > 0 ? Util.asString(article.getFamille().getDesignation()) : false) : false) && !Util.asString(article.getRefArt()) : false) {
            if (currentParam == null) {
                currentParam = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            }
            if (currentParam != null ? currentParam.getGenererReferenceArticle() : false) {
                int tailleNumero = currentParam.getTailleNumeroReferenceArticle();
                int tailleLettre = currentParam.getTailleLettreReferenceArticle();

                String designation = Util.asString(article.getFamille().getPrefixe()) ? article.getFamille().getPrefixe() : article.getFamille().getDesignation();
//                String designation = article.getDesignation();
                String code = designation.trim().length() > tailleLettre ? designation.substring(0, tailleLettre) : designation;
                String result = (String) dao.loadObjectByNameQueries("YvsBaseArticles.findLastCodeLikeCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), code + "%"});
                if (Util.asString(result)) {
                    Integer numero = Integer.valueOf(result.replace(code, "").trim()) + 1;
                    if (Integer.toString(numero).length() > tailleNumero) {
                        return;
                    }
                    for (int i = 0; i < (tailleNumero - Integer.toString(numero).length()); i++) {
                        code += "0";
                    }
                    code += numero;
                } else {
                    for (int i = 0; i < (tailleNumero - 1); i++) {
                        code += "0";
                    }
                    code += "1";
                }
                article.setRefArt(code);
                update("txt-reference_article");
            }
        }
    }

    public void reBuildReference() {
        if (familleSearch > 0) {
            if (currentParam == null) {
                currentParam = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            }
            if (currentParam != null ? currentParam.getTailleLettreReferenceArticle() > 0 : false) {
                List<YvsBaseArticles> list = dao.loadNameQueries("YvsBaseArticles.findByFamille", new String[]{"famille"}, new Object[]{new YvsBaseFamilleArticle(familleSearch)});
                if (list != null ? !list.isEmpty() : false) {
                    int tailleNumero = currentParam.getTailleNumeroReferenceArticle();
                    int tailleLettre = currentParam.getTailleLettreReferenceArticle();
                    YvsBaseArticles y;
                    for (int index = 0; index < list.size(); index++) {
                        y = list.get(index);
                        int numero = index + 1;
                        String designation = y.getFamille().getDesignation();
                        String code = designation.trim().length() > tailleLettre ? designation.substring(0, tailleLettre) : designation;
                        for (int i = 0; i < (tailleNumero - Integer.toString(numero).length()); i++) {
                            code += "0";
                        }
                        code += numero;
                        y.setRefArt(code);
                        dao.update(y);
                        int idx = listArticle.indexOf(y);
                        if (idx > -1) {
                            listArticle.set(index, y);
                        }
                        if (y.getId().equals(article.getId())) {
                            article.setRefArt(code);
                            update("txt-reference_article");
                        }
                    }
                    update("art_dgrig");
                    update("tab_article");
                    succes();
                }
            } else {
                getErrorMessage("Vous devez definir la taille des parties de la reference de l'article");
            }
        } else {
            getErrorMessage("Vous devez entrer une famille");
        }
    }

    public void chooseFamille(ValueChangeEvent ev) {
        if (ev != null) {
            if (ev.getNewValue() != null) {
                long id = (long) ev.getNewValue();
                article.setFamille(new FamilleArticle());
                ManagedFamilleArticle service = (ManagedFamilleArticle) giveManagedBean(ManagedFamilleArticle.class);
                if (id > 0) {
                    int idx = service.getFamilles().indexOf(new YvsBaseFamilleArticle(id));
                    if (idx >= 0) {
                        YvsBaseFamilleArticle y = service.getFamilles().get(idx);
                        article.setFamille(UtilProd.buildBeanFamilleArticle(y));
                        genererReference();
                    }
                } else if (id == -1) {
                    openDialog("dlgArtFamArt");
                } else if (id == -2) {
                    service.init(true);
                    update("txt_famArt");
                } else if (id == -3) {
                    service.init(false);
                    update("txt_famArt");
                }
            }
        }
    }

    public void chooseCategorie() {
        try {
            if (article != null ? article.getCategorie() != null : false) {
                if (article.getCategorie().equals(Constantes.CAT_SERVICE)) {
                    article.setSuiviEnStock(false);
                    article.setRequiereLot(false);
                }
            }
        } catch (Exception ex) {
            getException("chooseCategorie", ex);
        }
    }

    public void chooseConditionnementTarif() {
        if (article != null ? article.getId() > 0 : false) {
            if (planTarif.getId() < 1) {
                planTarif.setRemise(article.getRemise());
                planTarif.setPuv(article.getPuv());
            }
            if (planTarif != null ? planTarif.getConditionnement() != null ? planTarif.getConditionnement().getId() > 0 : false : false) {
                int idx = article.getConditionnements().indexOf(new YvsBaseConditionnement(planTarif.getConditionnement().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = article.getConditionnements().get(idx);
                    if (planTarif.getId() < 1) {
                        planTarif.setPuv(y.getPrix());
                        planTarif.setPuvMin(y.getPrixMin());
                        planTarif.setRemise(y.getRemise());
                    }
                    planTarif.setConditionnement(UtilProd.buildBeanConditionnement(y));
                }
            }
        }
    }

    public void chooseArticlePack() {
        if (article != null ? article.getId() > 0 : false) {
            if (pack != null ? pack.getArticle() != null ? pack.getArticle().getId() > 0 : false : false) {
                int idx = article.getConditionnements().indexOf(new YvsBaseConditionnement(pack.getArticle().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = article.getConditionnements().get(idx);
                    pack.setArticle(UtilProd.buildBeanConditionnement(y));
                }
            }
        }
    }

    public void chooseContenuPack() {
        if (pack != null ? pack.getId() > 0 : false) {
            if (contenu != null ? contenu.getUnite() != null ? contenu.getUnite().getId() > 0 : false : false) {
                int idx = contenu.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(contenu.getUnite().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = contenu.getArticle().getConditionnements().get(idx);
                    contenu.setUnite(UtilProd.buildBeanConditionnement(y));
                    contenu.setMontant(y.getPrix());
                }
            }
        }
    }

    public void choosePointVente() {
        ManagedPointVente w = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (w != null) {
            w.getListRabais().clear();
            if (rabais.getPoint() != null ? rabais.getPoint().getId() > 0 : false) {
                int idx = w.getPointsvente().indexOf(new YvsBasePointVente(rabais.getPoint().getId()));
                if (idx > -1) {
                    YvsBasePointVente y = w.getPointsvente().get(idx);
                    rabais.setPoint(UtilCom.buildSimpleBeanPointVente(y));
                    //vérifier l'existence des rabais dans le point de vente et pour l'article
                    w.setListRabais(dao.loadNameQueries("YvsComRabais.findByPointArticleUnit", new String[]{"article", "unite", "point"}, new Object[]{selectUnite.getArticle(), selectUnite, y}));
                }
            }
            update("data-rabais_article_point");
        }
    }

    public void loadOnViewCondit(SelectEvent ev) {
        if (ev != null) {
            selectCondit = (YvsBaseConditionnement) ev.getObject();
            populateViewCondit(UtilProd.buildBeanConditionnement(selectCondit));
            update("blog_form_conditionnement_article");
        }
    }

    public void unLoadOnViewCondit(UnselectEvent ev) {
        resetFicheCondit();
        update("blog_form_conditionnement_article");
    }

    public void loadOnViewDescription(SelectEvent ev) {
        if (ev != null) {
            YvsBaseArticleDescription y = (YvsBaseArticleDescription) ev.getObject();
            description = UtilProd.buildBeanArticleDescription(y);
            update("blog_form_description_article");
        }
    }

    public void unLoadOnViewDescription(UnselectEvent ev) {
        description = new ArticleDescription();
        update("blog_form_description_article");
    }

    public void loadOnViewPack(SelectEvent ev) {
        if (ev != null) {
            YvsBaseArticlePack y = (YvsBaseArticlePack) ev.getObject();
            pack = UtilProd.buildBeanArticlePack(y);
            contenu = new ContenuPack();
            update("tbvArticle:blog_form_pack_article");
        }
    }

    public void unLoadOnViewPack(UnselectEvent ev) {
        resetFichePack();
        update("tbvArticle:blog_form_pack_article");
    }

    public void loadOnViewContenuPack(SelectEvent ev) {
        if (ev != null) {
            YvsBaseArticleContenuPack y = (YvsBaseArticleContenuPack) ev.getObject();
            contenu = UtilProd.buildBeanContenuPack(y);
        }
    }

    public void unLoadOnViewContenuPack(UnselectEvent ev) {
        contenu = new ContenuPack();
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            contenu.setArticle(UtilProd.buildBeanArticles(bean));
        }
    }

    public void findArticlePack() {
        String num = contenu.getArticle().getRefArt();
        contenu.getArticle().setDesignation("");
        contenu.getArticle().setError(true);
        contenu.getArticle().setId(0);

        if (num != null ? num.trim().length() > 0 : false) {
            Articles y = searchArticleActif(null, num, true);
            if (getArticlesResult() != null ? !getArticlesResult().isEmpty() : false) {
                if (getArticlesResult().size() > 1) {
                    update("data_articles_contenu_pack");
                } else {
                    contenu.setArticle(y);
                    contenu.setMontant(y.getPuv());
                }
                contenu.getArticle().setError(false);
            }
        }
    }

    public void activePlanTarifaire(YvsBasePlanTarifaire bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            String rq = "UPDATE yvs_base_plan_tarifaire SET actif= ? WHERE id=?";
            yvs.dao.Options[] param = new yvs.dao.Options[]{new yvs.dao.Options(bean.getActif(), 1), new yvs.dao.Options(bean.getId(), 2)};
            dao.requeteLibre(rq, param);
            article.getPlans_tarifaires().set(article.getPlans_tarifaires().indexOf(bean), bean);
        }
    }

    public YvsBaseArticleFournisseur buildArticleFournisseur(ArticleFournisseur f) {
        YvsBaseArticleFournisseur r = new YvsBaseArticleFournisseur();
        if (f != null) {
            r.setId(f.getId());
            r.setDelaiLivraison(f.getDelaiLivraison());
            r.setUniteDelai(f.getUniteDelaisLiv());
            r.setDureeGarantie(f.getDureeGarantie());
            r.setUniteGarantie(f.getUniteDureeGaranti());
            r.setDureeVie(f.getDureeVie());
            r.setUniteVie(f.getUniteDureeVie());
            r.setPuv(f.getPrix());
            r.setRemise(f.getRemise());
            r.setNatureRemise(Constantes.NATURE_TAUX);
            r.setRefArtExterne(f.getRefArtExterne());
            r.setDesArtExterne(f.getDesArtExterne());
            r.setPuaTtc(f.isPuaTtc());
            r.setNew_(true);
            r.setDateSave(f.getDateSave());
            if (f.getFournisseur() != null ? f.getFournisseur().getId() > 0 : false) {
                r.setFournisseur(new YvsBaseFournisseur(f.getFournisseur().getId(), f.getFournisseur().getCodeFsseur(), new YvsBaseTiers(f.getFournisseur().getTiers().getId(), f.getFournisseur().getTiers().getNom(), f.getFournisseur().getTiers().getPrenom())));
            }
            r.setArticle(new YvsBaseArticles(article.getId()));
            r.setAuthor(currentUser);
        }
        return r;
    }

    public void searchFournisseur() {
        searchFournisseur(false);
    }

    public void searchFournisseur(boolean simulation) {
        if (!simulation) {
            String num = articleFsseur.getFournisseur().getCodeFsseur();
            articleFsseur.getFournisseur().setId(0);
            articleFsseur.getFournisseur().setError(true);
            articleFsseur.getFournisseur().setTiers(new Tiers());
            ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            if (m != null && (num != null) ? num.trim().length() > 0 : false) {
                Fournisseur y = m.searchFsseur(num, true);
                if (m.getFournisseurs() != null ? !m.getFournisseurs().isEmpty() : false) {
                    if (m.getFournisseurs().size() > 1) {
                        update("data_fournisseur_article");
                    } else {
                        articleFsseur.setFournisseur(y);
                    }
                    articleFsseur.getFournisseur().setError(false);
                }
            }
        } else {
            String num = fournisseur.getCodeFsseur();
            fournisseur.setId(0);
            fournisseur.setError(true);
            fournisseur.setTiers(new Tiers());
            ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            if (m != null) {
                Fournisseur y = m.searchFsseur(num, false);
                if (m.getFournisseurs() != null ? !m.getFournisseurs().isEmpty() : false) {
                    if (m.getFournisseurs().size() == 1) {
                        cloneObject(fournisseur, y);
                        update("value-fournisseur_simulation_prix");
                    }
                    fournisseur.setError(false);
                }
            }
        }
    }

    public void loadOnViewFournisseur(SelectEvent ev) {
        if (ev != null) {
            YvsBaseFournisseur f = (YvsBaseFournisseur) ev.getObject();
            articleFsseur.setFournisseur(UtilCom.buildBeanFournisseur(f));
        }
    }

    public void resetArticleFournisseur() {
        articleFsseur = new ArticleFournisseur();
        articleFsseur.setPrix(article.getPua());
        articleFsseur.setRemise(article.getRemise());
        articleFsseur.setPuaTtc(article.isPuaTtc());
    }

    public void addArticleFournisseur() {
        if (article.getId() > 0 && articleFsseur.getFournisseur().getId() > 0) {
            YvsBaseArticleFournisseur af = buildArticleFournisseur(articleFsseur);
            af.setDateUpdate(new Date());
            if (articleFsseur.getId() < 1) {
                af.setDateSave(new Date());
                af.setId(null);
                af = (YvsBaseArticleFournisseur) dao.save1(af);
                articleFsseur.setId(af.getId());
                article.getFournisseurs().add(0, af);
            } else {
                dao.update(af);
                article.getFournisseurs().set(article.getFournisseurs().indexOf(af), af);
            }
            resetArticleFournisseur();
        }
    }

    public void loadOnViewArticleFsseur(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            articleFsseur = UtilProd.buildArticleFournisseur((YvsBaseArticleFournisseur) ev.getObject());
        }
    }

    public void unloadOnViewArticleFsseur(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            resetArticleFournisseur();
        }
    }

    public void deleteBeanArticleFsseur(YvsBaseArticleFournisseur y) {
        try {
            if (y != null) {
                dao.delete(y);
                article.getFournisseurs().remove(y);
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
        }
    }

    public void deleteBeanCondit() {
        try {
            if (selectCondit != null) {
                dao.delete(selectCondit);
                int idx = article.getConditionnements().indexOf(selectCondit);
                if (idx > -1) {
                    article.getConditionnements().remove(selectCondit);
                }
                idx = listArticle.indexOf(selectArticle);
                if (idx > -1) {
                    int _idx = listArticle.get(idx).getConditionnements().indexOf(selectCondit);
                    if (_idx > -1) {
                        listArticle.get(idx).getConditionnements().remove(selectCondit);
                    }
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
        }
    }

    public void deleteBeanCondit(YvsBaseConditionnement y) {
        try {
            if (y != null) {
                dao.delete(y);
                int idx = article.getConditionnements().indexOf(y);
                if (idx > -1) {
                    article.getConditionnements().remove(y);
                }
                idx = listArticle.indexOf(selectArticle);
                if (idx > -1) {
                    int _idx = listArticle.get(idx).getConditionnements().indexOf(y);
                    if (_idx > -1) {
                        listArticle.get(idx).getConditionnements().remove(y);
                    }
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
            getException("deleteBeanDescription", ex);
        }
    }

    public void deleteBeanDescription(YvsBaseArticleDescription y) {
        try {
            if (y != null) {
                dao.delete(y);
                int idx = article.getDescriptions().indexOf(y);
                if (idx > -1) {
                    article.getDescriptions().remove(y);
                }
                idx = selectArticle.getDescriptions().indexOf(y);
                if (idx > -1) {
                    selectArticle.getDescriptions().remove(y);
                }
                idx = listArticle.indexOf(selectArticle);
                if (idx > -1) {
                    int _idx = listArticle.get(idx).getDescriptions().indexOf(y);
                    if (_idx > -1) {
                        listArticle.get(idx).getDescriptions().remove(y);
                    }
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
            getException("deleteBeanDescription", ex);
        }
    }

    public void deleteBeanPack(YvsBaseArticlePack y) {
        try {
            if (y != null) {
                dao.delete(y);
                int idx = article.getPacks().indexOf(y);
                if (idx > -1) {
                    article.getPacks().remove(y);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
            getException("deleteBeanPack", ex);
        }
    }

    public void deleteBeanContenuPack(YvsBaseArticleContenuPack y) {
        try {
            if (y != null) {
                dao.delete(y);
                int idx = pack.getContenus().indexOf(y);
                if (idx > -1) {
                    pack.getContenus().remove(y);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
            getException("deleteBeanContenuPack", ex);
        }
    }

    public void principalArticleFsseur(YvsBaseArticleFournisseur y) {
        y.setPrincipal(!y.getPrincipal());
        dao.update(y);
        article.getFournisseurs().get(article.getFournisseurs().indexOf(y)).setPrincipal(y.getPrincipal());
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            Articles bean = (Articles) ev.getObject();
            selectOnView(bean);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            Articles bean = (Articles) ev.getObject();
            selectOnView(bean);
        }
    }

    public void loadOnViewAnalytique(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectAnalytique = (YvsBaseArticleAnalytique) ev.getObject();
            analytique = UtilProd.buildBeanArticleAnalytique(selectAnalytique);
            update("tbvArticle:form-article_analytique");
        }
    }

    public void unLoadOnViewAnalytique(UnselectEvent ev) {
        resetFicheAnalytique();
    }

    public void chooseCentre() {
        if (analytique.getCentre() != null ? analytique.getCentre().getId() > 0 : false) {
            ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (service != null) {
                int idx = service.getCentres().indexOf(new YvsComptaCentreAnalytique(analytique.getCentre().getId()));
                if (idx > -1) {
                    YvsComptaCentreAnalytique y = service.getCentres().get(idx);
                    analytique.setCentre(UtilCompta.buildBeanCentreAnalytique(y));
                }
            }
        }
    }

    @Override
    public void deleteBean() {
        if (!autoriser("base_article_delete")) {
            openNotAcces();
            return;
        }
        if (article.getId() > 0) {
            YvsBaseArticles art = new YvsBaseArticles(article.getId());
            try {
                dao.delete(art);
                if (listArticle.contains(art)) {
                    listArticle.remove(art);
                    resetFicheArticle();
                }
                update("grid_article");
                update("tab_article");
                succes();
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cet élément !");
                log.log(Level.SEVERE, null, ex);
            }

        } else {
            getErrorMessage("Aucun article n'a été selecrtionné !");
        }
    }

    public void deleteAnalytique(YvsBaseArticleAnalytique y, boolean delete) {
        try {
            if (y != null) {
                if (!delete) {
                    selectAnalytique = y;
                    openDialog("dlgConfirmDeleteAnalytique");
                    return;
                }
                dao.delete(y);
                article.getAnalytiques().remove(y);
                if (analytique.getId() == y.getId()) {
                    resetFicheAnalytique();
                }
                update("tbvArticle:data-article_analytique");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedArticles (deleteAnalytique)", ex);
        }
    }

    public void resetFicheArticle() {
        resetFiche(article);
        article.setControleFournisseur(false);
        article.setRequiereLot(false);
        article.setClasse1(new ClassesStat());
        article.setClasse2(new ClassesStat());
        article.setFabricant(new Tiers());
        article.setFamille(new FamilleArticle());
        article.setGroupe(new GroupeArticle());
        article.setTemplate(new TemplateArticles());
        article.setUnite(new UniteMesure());
        article.setUniteVolume(new UniteMesure());
        article.setUniteStockage(new UniteMesure());
        article.setUniteVente(new UniteMesure());
        article.setMethodeVal(Constantes.CMP1);
        article.getListArtDepots().clear();
        article.getListArticleCatComptable().clear();
        article.getPlans_tarifaires().clear();
        article.getPhotos().clear();
        article.getConditionnements().clear();
        article.getArticlesEquivalents().clear();
        article.getArticlesSubstitutions().clear();
        article.getFournisseurs().clear();
        article.getAnalytiques().clear();
        article.getPacks().clear();

        images.clear();
        currentPhoto = Constantes.DEFAULT_PHOTO();
        currentPhotoExtension = "png";

        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.getArticles_depot().clear();
            m.resetFicheArticle();
            m.setEntityArticle(null);
        }
        selectArticle = null;
        entityArticle = null;
        selectTemplate = null;
    }

    @Override
    public void resetFiche() {
        resetFicheArticle();
        resetFicheCondit();
        resetFicheArticleFsseur();
        resetFichePack();
        resetFicheAnalytique();
        update("image_art_prod");
        update("tbvArticle");
        update("fiche_articles");
        update("image_art_prod");
        update("fiche_articles");
//        execute("collapseForm('article')");
    }

    public void resetFicheArticleFsseur() {
        articleFsseur = new ArticleFournisseur();
    }

    public void resetFichePack() {
        pack = new ArticlePack();
        contenu = new ContenuPack();
    }

    public void resetFicheAnalytique() {
        analytique = new ArticleAnalytique();
        selectAnalytique = new YvsBaseArticleAnalytique();
        update("tbvArticle:form-article_analytique");
    }

    public void resetFicheRabais() {
        rabais = new Rabais();
        selectRabais = new YvsComRabais();
        ManagedPointVente w = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (w != null) {
            w.resetFicheGrilleRabais();
        }
    }

    @Override
    public void changeView() {
        resetFiche();
    }
    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void handleFileUpload1(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminArticle(article).substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminArticle(article).length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminArticle(article);
            String imgFile = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
            try {
                //copie dans le dossier de l'application
                Util.copyFile(imgFile, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                //copie dans le dossier de sauvegarde
                Util.copySVGFile(imgFile, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                File f = new File(new StringBuilder(repDest).append(imgFile).toString());
                if (!f.exists()) {
                    File doc = new File(repDest);
                    if (!doc.exists()) {
                        doc.mkdirs();
                        f.createNewFile();
                    } else {
                        f.createNewFile();
                    }
                }
                article.setPhoto1(imgFile);
                images.set(1, imgFile);
                getInfoMessage("Chargé !");

            } catch (IOException ex) {
                Logger.getLogger(ManagedArticles.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void handleFileUpload2(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminArticle(article).substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminArticle(article).length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminArticle(article);
            String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
            try {
                //copie dans le dossier de l'application
                Util.copyFile(file, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                //copie dans le dossier de sauvegarde
                Util.copySVGFile(file, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                File f = new File(new StringBuilder(repDest).append(file).toString());
                if (!f.exists()) {
                    File doc = new File(repDest);
                    if (!doc.exists()) {
                        doc.mkdirs();
                        f.createNewFile();
                    } else {
                        f.createNewFile();
                    }
                }
                article.setPhoto2(file);
                getInfoMessage("Charger !");

            } catch (IOException ex) {
                Logger.getLogger(ManagedArticles.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void handleFileUpload3(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminArticle(article).substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminArticle(article).length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminArticle(article);
            String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
            try {
                //copie dans le dossier de l'application
                Util.copyFile(file, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                //copie dans le dossier de sauvegarde
                Util.copySVGFile(file, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                File f = new File(new StringBuilder(repDest).append(file).toString());
                if (!f.exists()) {
                    File doc = new File(repDest);
                    if (!doc.exists()) {
                        doc.mkdirs();
                        f.createNewFile();
                    } else {
                        f.createNewFile();
                    }
                }
                article.setPhoto3(file);
                getInfoMessage("Charger !");

            } catch (IOException ex) {
                Logger.getLogger(ManagedArticles.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            InputStream is = event.getFile().getInputstream();
            String extension = Util.getExtension(event.getFile().getFileName());
            byte[] bytes = IOUtils.toByteArray(is);
//            String file = Base64.getEncoder().encodeToString(bytes);
            String file = new String(Base64.encodeBase64(bytes));
            article.setPhoto(file);
            article.setPhotoExtension(extension);
            images.add(file);
            if (images.size() == 1) {
                article.setPhoto1(file);
                article.setPhoto1Extension(extension);
            } else if (images.size() == 2) {
                article.setPhoto2(file);
                article.setPhoto2Extension(extension);
            } else if (images.size() == 3) {
                article.setPhoto3(file);
                article.setPhoto3Extension(extension);
            }
            currentPhoto = article.getPhoto();
            currentPhotoExtension = article.getPhotoExtension();
            getInfoMessage("Charger !");
        } catch (IOException ex) {
            getErrorMessage("Action impossible!!!");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleFileUpload_OLD(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminArticle(article).substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminArticle(article).length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminArticle(article);
            String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
            try {
                //copie dans le dossier de l'application
                Util.copyFile(file, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                //copie dans le dossier de sauvegarde
                Util.copySVGFile(file, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                File f = new File(new StringBuilder(repDest).append(file).toString());
                if (!f.exists()) {
                    File doc = new File(repDest);
                    if (!doc.exists()) {
                        doc.mkdirs();
                        f.createNewFile();
                    } else {
                        f.createNewFile();
                    }
                }
                article.setPhoto(file);
                images.add(file);
                if (images.size() == 1) {
                    article.setPhoto1(file);
                } else if (images.size() == 2) {
                    article.setPhoto2(file);
                } else if (images.size() == 3) {
                    article.setPhoto3(file);
                }
                currentPhoto = article.getPhoto();
                getInfoMessage("Charger !");

            } catch (IOException ex) {
                Logger.getLogger(ManagedArticles.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void handleFileUniteUpload(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminArticle(article).substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminArticle(article).length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminArticle(article);
            String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
            try {
                //copie dans le dossier de l'application
                Util.copyFile(file, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                //copie dans le dossier de sauvegarde
                Util.copySVGFile(file, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                File f = new File(new StringBuilder(repDest).append(file).toString());
                if (!f.exists()) {
                    File doc = new File(repDest);
                    if (!doc.exists()) {
                        doc.mkdirs();
                        f.createNewFile();
                    } else {
                        f.createNewFile();
                    }
                }
                conditionnement.setPhoto(file);
                getInfoMessage("Charger !");

            } catch (IOException ex) {
                Logger.getLogger(ManagedArticles.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void handleFilePackUpload(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminArticle(article).substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminArticle(article).length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminArticle(article);
            String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
            try {
                //copie dans le dossier de l'application
                Util.copyFile(file, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                //copie dans le dossier de sauvegarde
                Util.copySVGFile(file, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                File f = new File(new StringBuilder(repDest).append(file).toString());
                if (!f.exists()) {
                    File doc = new File(repDest);
                    if (!doc.exists()) {
                        doc.mkdirs();
                        f.createNewFile();
                    } else {
                        f.createNewFile();
                    }
                }
                pack.setPhoto(file);
                getInfoMessage("Charger !");

            } catch (IOException ex) {
                Logger.getLogger(ManagedArticles.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void buildUploadImage() {
        images.clear();
    }

    public void valideUploadImage() {
        int taille = images.size();
        for (int i = taille; i < article.getPhotos().size(); i++) {
            images.add(article.getPhotos().get(i));
        }
        article.setPhotos(images);
        currentPhoto = article.getPhoto();
        currentPhotoExtension = article.getPhotoExtension();
        saveNew();
    }

    public void handleFileUploadFichier(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminArticle(article).substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminArticle(article).length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminArticle(article);
            String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
            try {
                //copie dans le dossier de l'application
                Util.copyFile(file, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                //copie dans le dossier de sauvegarde
                Util.copySVGFile(file, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
                File f = new File(new StringBuilder(repDest).append(file).toString());
                if (!f.exists()) {
                    File doc = new File(repDest);
                    if (!doc.exists()) {
                        doc.mkdirs();
                        f.createNewFile();
                    } else {
                        f.createNewFile();
                    }
                }
                article.setFichier(file);
                getInfoMessage("Charger !");

            } catch (IOException ex) {
                Logger.getLogger(ManagedArticles.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void navigerPhoto(boolean next) {
        int idx = article.getPhotos().indexOf(currentPhoto);
        if (idx > -1) {
            if (next) {
                idx = (idx == article.getPhotos().size() - 1) ? 0 : idx + 1;
            } else {
                idx = (idx == 0) ? article.getPhotos().size() - 1 : idx - 1;
            }
            currentPhoto = article.getPhotos().get(idx);
            currentPhotoExtension = article.getPhotosExtension().get(idx);
        }
    }

    /**
     *
     *
     *
     * AVANT *Tarif Article*
     */
    public void loadTrancheRemise() {
        tranchesRemise = dao.loadNameQueries("YvsTranches.findAllActif", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
    }

    public void createGammeRemise() {
        if (tranche.getReference() != null) {
            YvsTranches tr = new YvsTranches();
            tr.setActif(true);
            tr.setAuthor(currentUser);
            tr.setDateSave(new Date());
            tr.setId(null);
            tr.setReferenceTranche(tranche.getReference());
            tr = (YvsTranches) dao.save1(tr);
            tranche.setId(tr.getId());
            tranche.setListBorne(new ArrayList<YvsBorneTranches>());
            tranchesRemise.add(0, tr);
        }
    }

    public void addBorne() {
        if (tranche.getId() > 0) {
            YvsBorneTranches borne_ = new YvsBorneTranches();
            borne_.setAuthor(currentUser);
            borne_.setBorne(newBorne.getBorne());
            borne_.setPrix(newBorne.getPrix());
            borne_.setRemise(newBorne.getRemise());
            borne_.setTranche(new YvsTranches(tranche.getId()));
            borne_.setDateUpdate(new Date());
            borne_.setDateSave(newBorne.getDateSave());
            if (!updateBorne) {
                borne_.setDateSave(new Date());
                borne_ = (YvsBorneTranches) dao.save1(borne_);
                tranche.getListBorne().add(borne_);
            } else {
                borne_.setId(newBorne.getId());
                dao.update(borne_);
                tranche.getListBorne().set(tranche.getListBorne().indexOf(borne_), borne_);
            }
        }
        newBorne = new BorneTranche();
        updateBorne = false;
    }

    public void choixGammeremise(SelectEvent ev) {
        if (ev != null) {
            YvsTranches tr = (YvsTranches) ev.getObject();
            tranche = UtilProd.buildGammeRemise(tr);
        }
    }

    public void deleteBorneGamme(YvsBorneTranches b) {
        try {
            dao.delete(b);
            tranche.getListBorne().remove(b);
            newBorne = new BorneTranche();
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette ligne !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void updateGamme(RowEditEvent ev) {
        if (ev != null) {
            YvsTranches tr = (YvsTranches) ev.getObject();
            dao.update(tr);
            succes();
        }
    }

    public void deleteGamme(YvsTranches tr) {
        try {
            dao.delete(tr);
            tranchesRemise.remove(tr);
        } catch (Exception ex) {
            getErrorMessage("Impossible de terminer cette opération !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void choixBorneGamme(SelectEvent ev) {
        if (ev != null) {
            YvsBorneTranches b = (YvsBorneTranches) ev.getObject();
            newBorne = UtilProd.buildBorneGamme(b);
            updateBorne = true;
        }
    }

    private boolean controleFormTarif() {

        return true;
    }

    public void addTarifGroupe() {
////        Articles art = listArticle.get(listArticle.indexOf(new Articles(article.getId())));              
////        String refCatt = art.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getRefCategorie();
////        long idCatt = art.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getIdCategorie();
////        long idAgence = art.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getIdAgence();
////        String refAgence = art.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getRefAgence();
////        System.out.println("Ref Cat " + refCatt + " IdCat " + idCatt + " Id Agence " + idAgence + " Ref Agence " + refAgence);
//        if (tarif.getRefCategorie() != null) {
//            String[] ch = {"designation"};
//            Object[] val = {tarif.getRefCategorie()};
//            planTarif = new PlanTarifArticles();
//            yvsPlanTarif = new YvsComPlanRemise();
//            dao.setEntityClass(YvsBaseCategorieClient.class);
//            YvsBaseCategorieClient catT = (YvsBaseCategorieClient) dao.getOne(ch, val);
//            planTarif.setRefCategorie(catT.getCode());
//            planTarif.setIdCategorie(catT.getId());
//            planTarif.setRemise(tarif.getRemiseVente());
//            planTarif.setPrix(tarif.getPrix());
//            if (article.getListTarifArt() == null) {
//                article.setListTarifArt(new ArrayList<YvsBasePlanTarifaire>());
//            }
//            if (!article.getListTarifArt().contains(planTarif)) {
//                if (tranche && !listBorneTranche.isEmpty()) {
//                    TrancheVal tr = new TrancheVal(0, modelTranche);
//                    //je persiste la tranche
//                    YvsTranches tranch = buildTranche();
//                    tranch = (YvsTranches) dao.save1(tranch);
//                    tr.setId(tranch.getId());
//                    //parcour la liste des borne
//                    int pos = 0;
//                    for (BorneTranche b : listBorneTranche) {
//                        YvsBorneTranches bt = buildBorne(b);
//                        bt.setId(null);
//                        bt.setTranche(tranch);
//                        bt = (YvsBorneTranches) dao.save1(bt);
//                        b.setIdTranche(bt.getId());
//                        b.setId(bt.getId());
//                        listBorneTranche.set(pos, b);
//                        pos++;
//                    }
//                    List<BorneTranche> l = new ArrayList<>();
//                    l.addAll(listBorneTranche);
//                    tr.setListBorne(l);
//
//                    planTarif.setIdTranche(tr);
//                    yvsPlanTarif.setTranche(tranch);
//                } else if (tranche && listBorneTranche.isEmpty()) {
//                    getMessage("Vous avez indiqué que ce plan était repartie en tranche, cependant vous n'avez mis à jour aucune tranches", FacesMessage.SEVERITY_WARN);
//                    return;
//                } else {
//                    yvsPlanTarif.setTranche(null);
//                    planTarif.setIdTranche(null);
//                }
//                yvsPlanTarif.setPrix(planTarif.getPrix());
//                yvsPlanTarif.setRemise(planTarif.getRemiseVente());
//                dao.save(yvsPlanTarif);
////                article.getListTarifArt().add(0, yvsPlanTarif);
////                int idx = listArticle.indexOf(selectArticle);
//                int idx = 0;
////                if (listArticle.get(idx).getListTarifArt() == null) {
////                    listArticle.get(idx).setListTarifArt(article.getListTarifArt());
////                }
////                if (selectArticle.getListTarifArt() == null) {
////                    List<PlanTarifArticles> l = new ArrayList<>();
////                    selectArticle.setListTarifArt(l);
////                }
////            selectArticle.getListTarifArt().add(planTarif);
////                listArticle.set(listArticle.indexOf(selectArticle), selectArticle);
//                listBorneTranche.clear();
//                borne = 1;
//                remise = 1;
//                tarif.setRemise(1);
//                tranche = false;
//                update("A-ta-tarif");
//                update("A-bo-tarif");
//                update("A-groupP-table");
//            } else {
//                //modification du tarif-groupe
//                //on vérifie si la ligne tarif-categorie était lié à une tranche.
//                int index = article.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie()));
////                toUpdate = article.getListTarifArt().get(index);
//                if (toUpdate.getIdTranche() != null && !tranche) {
//                    //s'il y avait une tranche et qu'il y en a plus, on informe l'utilisateur                    
//                    getMessage("annullation de tranches", FacesMessage.SEVERITY_ERROR);
//                    openDialog("delTranche");
//                } else {
//                    //si la forma du plan tarifaire est inchangé,    
//                    if (toUpdate.getIdTranche() != null) {
//                        //on modifie les tranches s'il y en a   
//                        List<BorneTranche> lb = toUpdate.getIdTranche().getListBorne();
//                        int pos = 0;
//                        for (BorneTranche b : listBorneTranche) {
//                            YvsBorneTranches bb = buildBorne(b);
//                            bb.setTranche(new YvsTranches(toUpdate.getIdTranche().getId()));
//                            bb = (YvsBorneTranches) dao.update(bb);
//                            long id = b.getId();
//                            b.setId(bb.getId());
//                            //modifie sur la vue
//                            listBorneTranche.set(pos, b);
//                            //modifie l'objet toUpdate
//                            if (toUpdate.getIdTranche().getListBorne().contains(new BorneTranche(id))) {
//                                toUpdate.getIdTranche().getListBorne().set(toUpdate.getIdTranche().getListBorne().indexOf(new BorneTranche(id)), b);
//                            } else {
//                                toUpdate.getIdTranche().getListBorne().add(b);
//                            }
//                            pos++;
//                            if (lb.contains(b));
//                            lb.remove(b);
//                        }
//                        supprimeBorne(lb);
////                        article.getListTarifArt().set(article.getListTarifArt().indexOf(toUpdate), toUpdate);
////                        article.getListTarifArt().get(art.getListTarifArt().indexOf(new PlanTarifArticles(tarif.getRefCategorie(), tarif.getRefAgence()))).getIdTranche().setListBorne(listBorneTranche);
//                    } else {
//                        //s'il y a des tranches
//                        if (tranche && !listBorneTranche.isEmpty()) {
//                            TrancheVal tr = new TrancheVal(0, modelTranche);
//                            YvsTranches tranch = buildTranche();
//                            tranch = (YvsTranches) dao.save1(tranch);
//                            tr.setId(tranch.getId());
//                            //parcour la liste des borne
//                            int pos = 0;
//                            for (BorneTranche b : listBorneTranche) {
//                                YvsBorneTranches bt = buildBorne(b);
//                                bt.setId(null);
//                                bt.setTranche(tranch);
//                                bt = (YvsBorneTranches) dao.save1(bt);
//                                b.setIdTranche(bt.getId());
//                                b.setId(bt.getId());
//                                listBorneTranche.set(pos, b);
//                            }
//                            List<BorneTranche> l = new ArrayList<>();
//                            l.addAll(listBorneTranche);
//                            tr.setListBorne(l);
//                            toUpdate.setIdTranche(tr);
//                            YvsComPlanRemise pt = new YvsComPlanRemise();
//                            pt.setTranche(tranch);
//                            pt.setPrix(0);
//                            pt.setRemise(0);
//                            dao.update(pt);
////                            article.getListTarifArt().set(article.getListTarifArt().indexOf(pt), pt);
//                            toUpdate = null;
//                        } else {
//                            //s'il n'y a pas de tranche,                             
//                            toUpdate.setRemise(tarif.getRemiseVente());
//                            toUpdate.setPrix(tarif.getPrix());
//                            YvsComPlanRemise pt = new YvsComPlanRemise();
//                            pt.setRemise(tarif.getRemiseVente());
//                            pt.setPrix(tarif.getPrix());
//                            dao.update(pt);
////                            article.getListTarifArt().set(article.getListTarifArt().indexOf(pt), pt);
//                        }
//                    }
//                    succes();
//                    toUpdate = null;
//                }
//            }
//        } else {
////            getMessage(message.getMessage("choose_catTarif"), FacesMessage.SEVERITY_ERROR);
//        }
    }

//support à la modification du plan tarifaire
    public void updatePt() {

    }

    public void selectTarif(SelectEvent ev) {

    }

    public void changeVal(ValueChangeEvent ev) {
        if (ev.getOldValue() != ev.getNewValue()) {
//            if (tranche) {
//                tranche = false;
//            } else {
////                tarif.setRemise(1);
//            }
        }
    }

    /**
     * Supprimer les tranches
     */
    public void deletTarifGroup() {

    }
//supprime une borne

    /*GERER LES TARIFS PAR CATEGORIES CLIENT*/
    public void bindAllCategorieCLient() {
//        if (article.getId() > 0) {
//            champ = new String[]{"societe"};
//            val = new Object[]{currentUser.getAgence().getSociete()};
//            List<YvsBaseCategorieClient> l = dao.loadNameQueries("YvsBaseCategorieClient.findAllActif", champ, val);
//            boolean trouve = false;
//            for (YvsBaseCategorieClient cc : l) {
//                if (!article.getListTarifArt().isEmpty()) {
//                    for (YvsComPlanRemise pt : article.getListTarifArt()) {
//                        if (pt.getCategorie().equals(cc)) {
//                            trouve = true;
//                            break;
//                        }
//                    }
//                    if (!trouve) {
//                        //lier la catégorie à l'article
//                        YvsComPlanRemise plan = new YvsComPlanRemise();
//                        plan.setActif(true);
//                        plan.setArticle(new YvsBaseArticles(article.getId()));
//                        plan.setAuthor(currentUser);
//                        plan.setCategorie(cc);
//                        plan.setPrixVente(article.getPuv());
////                        plan.setRemise(article.getRemiseVente());
//                        plan = (YvsComPlanRemise) dao.save1(plan);
//                        article.getListTarifArt().add(plan);
//                    }
//                }
//            }
//        } else {
//            getErrorMessage("Aucun article n'a été selectionné !");
//        }
    }

    /**
     * Gestion des stocks par dépôts*
     */
    public void linkToAllDepot() {
        if (!autoriser("base_article_add_depot")) {
            openNotAcces();
            return;
        }
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.setOnArticle(true);
            m.insertAllArticle(null, entityArticle);
        }
    }

    /**
     * *Ajouter les catégories comptable
     */
    private long idCatC;
    private long idCompte;

    List<Object[]> listCat; //tableau de trois élmt Categorie comptable disposé dans l'ordre: idCat, codeCat, codeAppel
    List<Long> listIdCat;
    List<YvsBaseArticleCategorieComptableTaxe> listGroupeTaxe;
    List<YvsArticleCompte> listGroupeCompte;
    YvsBaseArticleCategorieComptableTaxe[] tabTaxe;
    YvsBasePlanComptable compte;
//    List<YvsGroupeArtCatTaxes> ltCompare;

    //on cherche la catégorie dans la liste des catgégories comptable préchargé
    public void findCatC(String cat) {
        if (cat != null) {
            if (cat.length() != 0) {
                for (Object[] o : listCat) {
                    if (cat.equals((String) o[1]) || cat.equals((String) o[2])) {
                        setTexte((String) o[1]);
//                        catC.setCategorie((String) o[1]);
                        idCatC = (long) o[0];
                        execute("giveFocus('A-vue-group:A-catC_compte')");
                        update("A-texte-grpeProd");
                        update("A-vue-group:A-display-grpe");
                        return;
                    }
                }
                idCatC = 0;
                setTexte("Aucune catégorie comptable avec ce code");
                articleCategorieC.setCategorie(null);
                execute("giveFocus('A-vue-group:A-catC_categorie')");
                update("A-texte-grpeProd");
                update("A-vue-group:A-display-grpe");
            }
        }
    }

    public void findCompte(String num) {
        if (num != null) {
            if (num.length() != 0) {
                champ = new String[]{"societe", "numCompte"};
                val = new Object[]{currentUser.getAgence().getSociete(), num};
//                Object[] c = (Object[]) dao.loadOneByNameQueries("YvsBasePlanComptable.findByNum", champ, val);
//                if (c != null) {
//                    idCompte = (Long) c[0];
//                    setTexte((String) c[1]);
////                    catC.setCompte((String) c[1]);
//                    execute("giveFocus('A-vue-group:A-catC_taxe1')");
//                } else {
//                    idCompte = 0;
//                    setTexte("Aucun compte avec cet identifiant");
////                    catC.setCompte(null);
//                    execute("giveFocus('A-vue-group:A-catC_compte')");
//                }
//                update("A-vue-group:A-display-grpe");
//                update("A-texte-grpeProd");
            }
        }
    }

    public void addGroupeCompte() {
        //controle l'id de l'article
        if (article.getId() != 0) {
            //controle de l'id de la catégorie
            if (idCatC != 0) {
                if (idCompte != 0) {
                    YvsArticleCompte garc = new YvsArticleCompte(new YvsArticleComptePk(article.getId(), idCatC));
//                    garc.setCategorie(new YvsBaseCategorieComptable(idCompte));
                    dao.update(garc);
                    idCatC = 0;
                    update("A-vue-group:A-tabCatC");
                    update("A-vue-group:A-display-grpe");
                    execute("giveFocus('A-vue-group:A-catC_categorie')");
                }
            }
        }
    }

    public void choixArtCatC(SelectEvent ev) {
        GroupeCatC c = (GroupeCatC) ev.getObject();
//        cloneObject(articleCategorieC, c);
        update("A-vue-group:A-display-grpe");
        execute("giveFocus('A-vue-group:A-catC_categorie')");
    }
    //objets supplémentaire
    private List<GroupeDeProduit> listGroupe;
    private List<GroupeDeProduit> listClassStat;
    private List<GroupeDeProduit> listCond;

//charge tous les pays 
//    public void loadAllStateScte() {
//        champ = new String[]{"societe", "titre"};
//        val = new Object[]{currentAgence.getSociete(), Constantes.T_PAYS};
//        listPays = dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val);
//    }
////charge toutes les villes d'une société
//
//    public void loadCityByScte(ValueChangeEvent ev) {
//        if (ev.getNewValue() != null) {
//            Long idPays = (Long) ev.getNewValue();
//            champ = new String[]{"parent"};
//            val = new Object[]{new YvsDictionnaire(idPays)};
//            listVilles = dao.loadNameQueries("YvsDictionnaire.findVilleOnePays", champ, val);
//        }
//    }
    public boolean controleFicheTaxe(Taxes bean) {
        if (bean.getCodeTaxe() == null || bean.getCodeTaxe().equals("")) {
            getErrorMessage("vous devez entrer le code de la taxe");
            return false;
        }
        return true;
    }

    public boolean controleFicheCategorie(CategorieComptable bean) {
        if (bean.getCodeCategorie() == null || bean.getCodeCategorie().equals("")) {
            getErrorMessage("vous devez entrer le code de la categorie");
            return false;
        }
        return true;
    }

    public
            boolean saveNewTaxe() {
        try {
            ManagedTaxes mt = (ManagedTaxes) giveManagedBean(ManagedTaxes.class
            );
            if (mt
                    != null) {
                YvsBaseTaxes entity = mt.saveReturn();
                if (entity != null ? (entity.getId() != null ? entity.getId() > 0 : false) : false) {
                    YvsBaseArticleCategorieComptableTaxe a = new YvsBaseArticleCategorieComptableTaxe(Long.valueOf(-(listTaxes.size())));
                    a.setActif(true);
                    a.setAppRemise(false);
                    a.setArticleCategorie(selectArticleComptable);
                    a.setNew_(true);
                    a.setTaxe(entity);
                    a.setAuthor(currentUser);
                    listTaxes.add(a);

                    succes();
                    update("select_taxe_taxe");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            Logger.getLogger(ManagedArticles.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void addAllCategoriesComptable() {
        if (!autoriser("base_article_defined_comptable")) {
            openNotAcces();
            return;
        }
        if (article.getId() > 0) {
            ManagedCatCompt service = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
            if (service != null) {
                for (YvsBaseCategorieComptable cc : service.getCategories()) {
                    champ = new String[]{"categorie", "article"};
                    val = new Object[]{cc, new YvsBaseArticles(article.getId())};
                    List<YvsBaseArticleCategorieComptable> lp = dao.loadNameQueries("YvsBaseArticleCategorieComptable.findByCategorieArticle", champ, val);
                    if (lp != null ? lp.isEmpty() : true) {
                        YvsBaseArticleCategorieComptable c = new YvsBaseArticleCategorieComptable();
                        c.setActif(true);
                        c.setArticle(new YvsBaseArticles(article.getId()));
                        c.setAuthor(currentUser);
                        c.setCategorie(cc);
                        c.setDateSave(new Date());
                        c.setDateUpdate(new Date());
                        c = (YvsBaseArticleCategorieComptable) dao.save1(c);
                        article.getListArticleCatComptable().add(c);
                    }
                }
                succes();
            }
        } else {
            getErrorMessage("Vous devez precisez l'article");
        }
    }

    public void saveNewAnalytique() {
        try {
            analytique.setArticle(article);
            if (controleFiche(analytique)) {
                selectAnalytique = UtilProd.buildArticleAnalytique(analytique, currentUser);
                if (analytique.getId() < 1) {
                    selectAnalytique = (YvsBaseArticleAnalytique) dao.save1(selectAnalytique);
                } else {
                    dao.update(selectAnalytique);
                }
                int idx = article.getAnalytiques().indexOf(selectAnalytique);
                if (idx < 0) {
                    article.getAnalytiques().add(0, selectAnalytique);
                } else {
                    article.getAnalytiques().set(idx, selectAnalytique);
                }
                resetFicheAnalytique();
                succes();
                update("tbvArticle:data-article_analytique");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedArticles (saveNewAnalytique)", ex);
        }
    }

    //dès qu'on choisit une catégorie, une boite de dialogue s'ouvre pour qu'on puisse selectionner les taxes
    public void choixCategorieComptableTaxe(ValueChangeEvent ev) {
        if (ev != null) {
            long idCat = (long) ev.getNewValue();
            if (idCat > 0) {
                for (YvsBaseArticleCategorieComptable at : article.getListArticleCatComptable()) {
//                    if (at.getCategorie().equals(new CategorieComptable(idCat))) {
//                        for (ArticlesCatTaxes tx : at.getTaxes()) {
//                            taxes.get(taxes.indexOf(tx)).setSelectActif(true);
//                        }
//                    }
                }
                articleCategorieC = new ArticlesCatComptable();
//                articleCategorieC.setCategorie(new CategorieComptable(idCat));
//                articleCategorieC.getCategorie().setCategorie(new CategorieComptable(idCat));
                openDialog("dlgAddCatTaxes");
                update("select_taxe_taxe");
            }
        }
    }

    public void _addTaxeToCategorieArticle(SelectEvent ev) {
        if (selectArticleComptable != null ? selectArticleComptable.getId() > 0 : false) {
            YvsBaseTaxes tax = (YvsBaseTaxes) ev.getObject();
            boolean deja = false;
            for (YvsBaseArticleCategorieComptableTaxe t : selectArticleComptable.getTaxes()) {
                if (tax.equals(t.getTaxe())) {
                    deja = true;
                    break;
                }
            }
            if (!deja) {
                YvsBaseArticleCategorieComptableTaxe y = new YvsBaseArticleCategorieComptableTaxe();
                y.setActif(true);
                y.setAppRemise(false);
                y.setArticleCategorie(selectArticleComptable);
                y.setTaxe(tax);
                y = (YvsBaseArticleCategorieComptableTaxe) dao.save1(y);
                selectArticleComptable.getTaxes().add(0, y);
                article.getListArticleCatComptable().set(article.getListArticleCatComptable().indexOf(selectArticleComptable), selectArticleComptable);
                succes();
            } else {
                getErrorMessage("Vous avez deja associé cette taxe");
            }
        } else {
            getErrorMessage("Veuillez choisir une catégorie comptable !");
        }
    }

    public void addTaxeToCategorieArticle(YvsBaseArticleCategorieComptableTaxe y) {
        try {
            if (!autoriser("base_article_defined_comptable")) {
                openNotAcces();
                return;
            }
            if (y != null) {
                y.setNew_(!y.isNew_());
                int pos = listTaxes.indexOf(y);
                y.setId(null);
                y = (YvsBaseArticleCategorieComptableTaxe) dao.save1(y);
                listTaxes.set(pos, y);
                selectArticleComptable.getTaxes().add(0, y);
                article.getListArticleCatComptable().set(article.getListArticleCatComptable().indexOf(selectArticleComptable), selectArticleComptable);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible d'ajouter cet element");
        }
    }

    public void removeTaxeToCategorieArticle(YvsBaseArticleCategorieComptableTaxe y) {
        try {
            if (!autoriser("base_article_defined_comptable")) {
                openNotAcces();
                return;
            }
            if (y != null) {
                dao.delete(y);
                y.setNew_(!y.isNew_());
                listTaxes.set(listTaxes.indexOf(y), y);
                selectArticleComptable.getTaxes().remove(y);
                article.getListArticleCatComptable().set(article.getListArticleCatComptable().indexOf(selectArticleComptable), selectArticleComptable);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
        }
    }

    public void appRemiseTaxeToCategorieArticle(YvsBaseArticleCategorieComptableTaxe y) {
        try {
            if (y != null) {
                y.setAppRemise(!y.getAppRemise());
                dao.update(y);
                selectArticleComptable.getTaxes().set(selectArticleComptable.getTaxes().indexOf(y), y);
                article.getListArticleCatComptable().set(article.getListArticleCatComptable().indexOf(selectArticleComptable), selectArticleComptable);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
        }
    }

    public void onCompteEdit(RowEditEvent ev) {
        if (ev != null) {
            YvsBaseArticleCategorieComptable y = (YvsBaseArticleCategorieComptable) ev.getObject();
            dao.update(y);
            succes();
        }
    }

    public void ecouteSaisieCG(YvsBaseArticleCategorieComptable art) {
        //trouve le compte à partir du numéro ou de l'intitule ou du code appel        
        ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class
        );
        if (service
                != null) {
            service.findCompteByNum(art.getNumCompte());
            art.setError(service.getListComptes().isEmpty());
            if (service.getListComptes() != null) {
                if (!service.getListComptes().isEmpty()) {
                    if (service.getListComptes().size() == 1) {
                        art.setError(false);
                        art.setCompte(service.getListComptes().get(0));
                        art.setNumCompte(art.getCompte().getNumCompte());
                    } else {
                        openDialog("dlgListComptes");
                        selectArticleComptable = art;
                        update("data_comptes_article_categorie");
                    }
                } else {
                    art.setError(true);
                }
            } else {
                art.setError(true);
            }
        }

    }

    public void initComptes(YvsBaseArticleCategorieComptable y) {
//        System.err.println("y " + y);
//        comptesResult.clear();
//        comptesResult.addAll(comptes);
        selectArticleComptable = y;
        update("data_comptes_article_categorie");
    }

    public void loadOnViewCompte(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            if (selectArticleComptable != null ? selectArticleComptable.getId() > 0 : false) {
                YvsBasePlanComptable bean = (YvsBasePlanComptable) ev.getObject();
                selectArticleComptable.setCompte(bean);
                selectArticleComptable.setNumCompte(bean.getNumCompte());
                dao.update(selectArticleComptable);
                article.getListArticleCatComptable().set(article.getListArticleCatComptable().indexOf(selectArticleComptable), selectArticleComptable);
                succes();
                selectArticleComptable = new YvsBaseArticleCategorieComptable();
            }
        }
    }

    public void removeArticleCategorie(YvsBaseArticleCategorieComptable y) {
        try {
            if (!autoriser("base_article_defined_comptable")) {
                openNotAcces();
                return;
            }
            if (y != null) {
                dao.delete(y);
                article.getListArticleCatComptable().remove(y);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet element");
        }
    }

    public void uploadFile(FileUploadEvent event) {
        if (event != null) {
            String path = (isWindows() ? USER_DOWNLOAD : USER_DOWNLOAD_LINUX);
            try {
                file = event.getFile();
                File f = Util.createRessource(file);
                if (f != null) {
                    try {
                        path = f.getAbsolutePath();
                        buildData(path);
                    } finally {
                        f.delete();
                    }
                }
            } catch (Exception ex) {
                getException("ManagedArticle (path = " + path + ")", ex);
            }
        }
        update("txt_name_file");
    }

    private void buildData(String path) {
        articlesLoad.clear();
        if (new File(path).exists()) {
            List<List<Object>> data = readFileXLS(path);
            if (data != null) {
                if (!data.isEmpty()) {
                    List<Object> head = data.get(0);
                    if (head != null) {
                        if (!head.isEmpty()) {
                            for (int i = 1; i < data.size(); i++) {
                                YvsBaseArticles c = new YvsBaseArticles((long) articlesLoad.size());
                                if (data.get(i).size() > 0) {
                                    c.setRefArt((String) data.get(i).get(0));
                                    if (data.get(i).size() > 1) {
                                        c.setDesignation((String) data.get(i).get(1));
                                        if (data.get(i).size() > 3) {
                                            YvsBaseFamilleArticle p = new YvsBaseFamilleArticle((String) data.get(i).get(2));
                                            c.setFamille(p);
                                            if (data.get(i).size() > 4) {
                                                String valeur = data.get(i).get(3).toString();
                                                if (Util.isNumeric(valeur)) {
                                                    c.setPua(Double.valueOf(valeur));
                                                    c.setPuaTtc(true);
                                                }
                                                if (data.get(i).size() > 5) {
                                                    valeur = data.get(i).get(4).toString();
                                                    if (Util.isNumeric(valeur)) {
                                                        c.setPuv(Double.valueOf(valeur));
                                                        c.setPuvTtc(true);
                                                    }
                                                    if (data.get(i).size() > 6) {
                                                        valeur = data.get(i).get(5).toString();
                                                        if (Util.isNumeric(valeur)) {
                                                            c.setPrixMin(Double.valueOf(valeur));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    champ = new String[]{"societe", "code"};
                                    val = new Object[]{currentAgence.getSociete(), c.getRefArt()};
                                    nameQueri = "YvsBaseArticles.findByCode";
                                    List<YvsBaseArticles> l = dao.loadNameQueries(nameQueri, champ, val);
                                    c.setNew_(l != null ? !l.isEmpty() : false);
                                    articlesLoad.add(c);
                                }
                            }
                            openDialog("dlgLoadArticles");
                            update("data_articles_load_");
                        } else {
                            getErrorMessage("Fichier Vide");
                        }
                    } else {
                        getErrorMessage("Fichier Incorrect");
                    }
                } else {
                    getErrorMessage("Fichier Vide");
                }
            } else {
                getErrorMessage("Fichier Incorrect");
            }
        } else {
            getErrorMessage("Le fichier n'existe pas");
        }
    }

    public void fullArticles() {
        if (!articlesLoad.isEmpty()) {
            for (YvsBaseArticles t : articlesLoad) {
//                if (!t.isNew_()) {
                if (true) {
                    if (t.getFamille() != null ? !t.getFamille().getReferenceFamille().equals("") : false) {
                        YvsBaseFamilleArticle c = t.getFamille();
                        champ = new String[]{"societe", "code"};
                        val = new Object[]{currentAgence.getSociete(), c.getReferenceFamille()};
                        YvsBaseFamilleArticle c_ = (YvsBaseFamilleArticle) dao.loadOneByNameQueries("YvsBaseFamilleArticle.findByReference", champ, val);
                        if (c_ != null ? c_.getId() < 1 : true) {
                            c.setDesignation(c.getDesignation().equals("") ? c.getReferenceFamille() : c.getDesignation());
                            c.setSociete(currentAgence.getSociete());
                            if (currentUser != null ? currentUser.getId() > 0 : false) {
                                c.setAuthor(currentUser);
                            }
                            c.setId(null);
                            c = (YvsBaseFamilleArticle) dao.save1(c);
                        } else {
                            c = c_;
                        }
                        t.setFamille(c);
                    }
                    t.setActif(true);
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        t.setAuthor(currentUser);
                    }
                    champ = new String[]{"societe", "code"};
                    val = new Object[]{currentAgence.getSociete(), t.getRefArt()};
                    nameQueri = "YvsBaseArticles.findByCode";
                    List<YvsBaseArticles> l = dao.loadNameQueries(nameQueri, champ, val);
                    if (l != null ? l.isEmpty() : true) {
                        t.setId(null);
                        t = (YvsBaseArticles) dao.save1(t);
                        listArticle.add(t);
                    } else {
                        YvsBaseArticles a = l.get(0);
                        if (t.getPua() != 0) {
                            a.setPua(t.getPua());
                            a.setPuaTtc(true);
                        }
                        if (t.getPuv() != 0) {
                            a.setPuv(t.getPuv());
                            a.setPuvTtc(true);
                        }
                        if (t.getPrixMin() != 0) {
                            a.setPrixMin(t.getPrixMin());
                        }
                        dao.update(a);
                        t = a;
                    }
                    champ = new String[]{"societe"};
                    val = new Object[]{currentAgence.getSociete()};
                    YvsBaseUniteMesure y = (YvsBaseUniteMesure) dao.loadOneByNameQueries("YvsBaseUniteMesure.findByDefaut", champ, val);
                    if (y != null ? y.getId() > 0 : false) {
                        champ = new String[]{"article", "unite"};
                        val = new Object[]{t, y};
                        YvsBaseConditionnement c = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findByArticleUnite", champ, val);
                        if (c != null ? c.getId() < 1 : true) {
                            c = new YvsBaseConditionnement(null, t, y);
                            c.setAuthor(currentUser);
                            c.setPrixMin(t.getPrixMin());
                            c.setPrix(t.getPuv());
                            c.setPrixAchat(t.getPua());
                            c.setByVente(true);
                            c.setDefaut(true);
                            dao.save(c);
                        }
                    }
                }
            }
            update("tab_article");
            succes();
        }
        articlesLoad.clear();
    }

    public void initTaxesArticleComptable(YvsBaseArticleCategorieComptable y) {
        selectArticleComptable = y;
        taxes = dao.loadNameQueries("YvsBaseTaxes.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        champ = new String[]{"articleCategorie"};
        val = new Object[]{y};
        listTaxes = dao.loadNameQueries("YvsBaseArticleCategorieComptableTaxe.findByArticleCategorie", champ, val);
        for (YvsBaseTaxes t : taxes) {
            boolean deja = false;
            for (YvsBaseArticleCategorieComptableTaxe a : listTaxes) {
                if (a.getTaxe().equals(t)) {
                    a.setNew_(false);
                    deja = true;
                    break;
                }
            }
            if (!deja) {
                YvsBaseArticleCategorieComptableTaxe a = new YvsBaseArticleCategorieComptableTaxe(Long.valueOf(-(listTaxes.size())));
                a.setActif(true);
                a.setAppRemise(false);
                a.setArticleCategorie(y);
                a.setNew_(true);
                a.setTaxe(t);
                a.setAuthor(currentUser);
                listTaxes.add(a);
            }
        }
        update("data_taxe_article_categorie");
    }

    public void chooseUnite(UniteMesure y) {
        switch (unite) {
            case "A":
                if (article != null) {
                    if (conditionnement.getId() < 1) {
                        conditionnement.setPrixAchat(article.getPua());
                        conditionnement.setPrix(article.getPuv());
                        conditionnement.setPrixMin(article.getPuvMin());
                        if (y.isDefaut()) {
                            conditionnement.setByVente(true);
                            conditionnement.setDefaut(true);
                        }
                    } else {
                        if (conditionnement.getPrixAchat() == 0) {
                            conditionnement.setPrixAchat(article.getPua());
                        }
                        if (conditionnement.getPrix() == 0) {
                            conditionnement.setPrix(article.getPuv());
                        }
                        if (conditionnement.getPrixMin() == 0) {
                            conditionnement.setPrixMin(article.getPuvMin());
                        }
                    }
                }
                conditionnement.setUnite(y);
                controleEquivalence();
                break;
            case "V":
                article.setUniteVolume(y);
                break;
            case "C":
                if (stockage) {
                    article.setUniteStockage(y);
                } else {
                    article.setUniteVente(y);
                }
                if ((article.getUniteStockage() != null) && (article.getUniteVente() != null)) {
                    champ = new String[]{"unite", "uniteE"};
                    val = new Object[]{new YvsBaseUniteMesure(article.getUniteStockage().getId()), new YvsBaseUniteMesure(article.getUniteVente().getId())};
                    nameQueri = "YvsBaseTableConversion.findUniteCorrespondance";
                    YvsBaseTableConversion t = (YvsBaseTableConversion) dao.loadOneByNameQueries(nameQueri, champ, val);
                    if (t != null ? (t.getId() != null ? t.getId() > 0 : false) : false) {
                        article.setTauxEquivalenceStock(t.getTauxChange());
                    }
                }
                break;
            default:
                article.setUnite(y);
                break;
        }
        if (!template) {
            switch (unite) {
                case "A":
                    update("tbvArticle:blog_form_conditionnement_article");
                    break;
                case "V":
                    update("txt_unite_volume_article");
                    break;
                case "C":
                    if (stockage) {
                        update("txt_unite_stockage_article");
                    } else {
                        update("txt_unite_vente_article");
                    }
                    update("txt_equivalence_unite_article");
                    break;
                default:
                    article.setUnite(y);
                    update("txt_unite_masse_article");
                    break;
            }
        } else {
            switch (unite) {
                case "A":
                    update("tbvArticle:txt_unite_conditionnement_template");
                    break;
                case "V":
                    update("txt_unite_volume_template");
                    break;
                case "C":
                    if (stockage) {
                        update("txt_unite_stockage_template");
                    } else {
                        update("txt_unite_vente_template");
                    }
                    update("txt_equivalence_unite_template");
                    break;
                default:
                    update("txt_unite_masse_template");
                    break;
            }
        }
    }

    public void loadOnViewUnite(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseUniteMesure bean = (YvsBaseUniteMesure) ev.getObject();
            chooseUnite(UtilProd.buildBeanUniteMesure(bean));
        }
    }

    public void searchUniteMesure(String unite, boolean stockage, boolean template) {
        this.unite = unite;
        this.stockage = stockage;
        this.template = template;
        String num = "";
        switch (unite) {
            case "A":
                num = conditionnement.getUnite().getReference();
                conditionnement.getUnite().setLibelle("");
                conditionnement.getUnite().setError(true);
                conditionnement.getUnite().setId(0);
                break;
            case "V":
                num = article.getUniteVolume().getReference();
                article.getUniteVolume().setLibelle("");
                article.getUniteVolume().setError(true);
                article.getUniteVolume().setId(0);
                break;
            case "C":
                if (stockage) {
                    num = article.getUniteStockage().getReference();
                    article.getUniteStockage().setLibelle("");
                    article.getUniteStockage().setError(true);
                    article.getUniteStockage().setId(0);
                } else {
                    num = article.getUniteVente().getReference();
                    article.getUniteVente().setLibelle("");
                    article.getUniteVente().setError(true);
                    article.getUniteVente().setId(0);
                }
                break;
            default:
                num = article.getUnite().getReference();
                article.getUnite().setLibelle("");
                article.getUnite().setError(true);
                article.getUnite().setId(0);
                break;

        }
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedUniteMesure m = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
            if (m != null) {
                UniteMesure y = m.findUnite(num, true);
                if (m.getUnites() != null ? !m.getUnites().isEmpty() : false) {
                    if (m.getUnites().size() > 1) {
                        update("data_unite_article");
                    } else {
                        chooseUnite(y);
                    }
                    switch (unite) {
                        case "A":
                            conditionnement.getUnite().setError(false);
                            break;
                        case "V":
                            article.getUniteVolume().setError(false);
                            break;
                        case "C":
                            if (stockage) {
                                article.getUniteStockage().setError(false);
                            } else {
                                article.getUniteVente().setError(false);
                            }
                            break;
                        default:
                            article.getUnite().setError(false);
                            break;
                    }
                }
            }
        }
    }

    public void initUnites(String unite, boolean stockage, boolean template) {
        this.unite = unite;
        this.stockage = stockage;
        this.template = template;
        ManagedUniteMesure a = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class
        );
        if (a
                != null) {
            switch (unite) {
                case "A":
                    a.initUnites(conditionnement.getUnite());
                    break;
                case "V":
                    a.initUnites(article.getUniteVolume());
                    break;
                case "C":
                    if (stockage) {
                        a.initUnites(article.getUniteStockage());
                    } else {
                        a.initUnites(article.getUniteVente());
                    }
                    break;
                default:
                    a.initUnites(article.getUnite());
                    break;
            }
        }

        update("data_unite_template_article");
    }

    private void controleEquivalence() {
        equivalencesUnite.clear();
        nameQueri = "YvsBaseTableConversion.coundUniteCorrespondance";
        champ = new String[]{"unite", "equivalent"};
        for (YvsBaseConditionnement c : article.getConditionnements()) {
            if (conditionnement != null ? conditionnement.getUnite() != null ? conditionnement.getUnite().getId() > 0 ? !c.getUnite().getId().equals(conditionnement.getUnite().getId()) : false : false : false) {
                val = new Object[]{new YvsBaseUniteMesure(conditionnement.getUnite().getId()), c.getUnite()};
                Long count = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
                if (count != null ? count < 1 : true) {
                    equivalencesUnite.add(c);
                }
            }
        }
        if (equivalencesUnite != null ? !equivalencesUnite.isEmpty() : false) {
            openDialog("dlgEquivalenceUnite");
            update("data-equivalence_unite");
        }
    }

    public void onEquivalenceUniteEdit(CellEditEvent ev) {
        if (ev != null) {
            int index = ev.getRowIndex();
            if (conditionnement != null ? conditionnement.getUnite() != null ? conditionnement.getUnite().getId() > 0 : false : false) {
                YvsBaseConditionnement equivalent = equivalencesUnite.get(index);
                Double coefficient = (Double) ev.getNewValue();
                YvsBaseUniteMesure unite = new YvsBaseUniteMesure(conditionnement.getUnite().getId());
                nameQueri = "YvsBaseTableConversion.findUniteCorrespondance";
                champ = new String[]{"unite", "uniteE"};
                val = new Object[]{unite, equivalent.getUnite()};
                YvsBaseTableConversion y = (YvsBaseTableConversion) dao.loadOneByNameQueries(nameQueri, champ, val);
                if (y != null ? y.getId() > 0 : false) {
                    y.setTauxChange(coefficient);
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                } else {
                    y = new YvsBaseTableConversion(unite, equivalent.getUnite(), coefficient);
                    y.setDateUpdate(new Date());
                    dao.save(y);
                }
            }
        }
    }

    public YvsBaseArticles findLastArticles() {
        return (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findLast", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public Articles searchArticleActif(boolean open) {
        return searchArticleActif(null, article.getRefArt(), open);
    }

    public Articles searchArticleActif(String type, String num, boolean open) {
        return searchArticleActif(null, type, num, open);
    }

    public Articles searchArticleActif(List<String> categories, String type, String num, boolean open) {
        Articles a = new Articles();
        a.setRefArt(num);
        a.setError(true);
        ParametreRequete p = new ParametreRequete("y.refArt", "reference", null, "LIKE", "AND");
        if (num != null ? num.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", num.toUpperCase(), "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.refArt)", "reference", num.toUpperCase(), "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "reference", num.toUpperCase(), "LIKE", "OR"));

        }
        pa.addParam(p);
        if (categories != null ? categories.isEmpty() : true) {
            loadActifArticle(type);
        } else {
            pa.addParam(new ParametreRequete("y.categorie", "categorie", categories, "IN", "AND"));
            loadActifArticle(true, true);
        }
        a = chechArticleResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setRefArt(num);
            a.setError(true);
        }
        return a;
    }

    private Articles chechArticleResult(boolean open) {
        Articles a = new Articles();
        if (articlesResult != null ? !articlesResult.isEmpty() : false) {
            if (articlesResult.size() > 1) {
                if (open) {
                    openDialog("dlgListArticle");
                }
                a.setListArt(true);
            } else {
                return chechArticleResult(articlesResult.get(0));
            }
            a.setError(false);
        }
        return a;
    }

    /*Méthode de chargement des informations d'un article sur des formulaire de transaction*/
    public Articles chechArticleResult(YvsBaseArticles art) {
        Articles a = new Articles();
        if (art != null) {
            art.setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle1", new String[]{"article"}, new Object[]{art}));
            a = UtilProd.buildBeanArticleForForm(art);
            a.setSelectArt(true);
        } else {
            a.setSelectArt(false);
        }
        return a;
    }

    public Conditionnement searchArticleActifByCodeBarre(String num) {
        Conditionnement result = new Conditionnement();
        if (num != null ? num.trim().length() > 0 : false) {
            conditionnements = dao.loadNameQueries("YvsBaseArticleCodeBarre.findArticleByCodeBarre", new String[]{"societe", "codeBarre"}, new Object[]{currentAgence.getSociete(), num});
            if (conditionnements != null ? !conditionnements.isEmpty() : false) {
                if (conditionnements.size() > 1) {
                    openDialog("dlgListConditionnement");
                    return result;
                }
                YvsBaseConditionnement y = conditionnements.get(0);
                if (y.getArticle().getActif()) {
                    result = UtilProd.buildBeanConditionnement(y);
                }
            }
        }
        return result;
    }

    private void addParamCategoriePf() {
        ParametreRequete p = new ParametreRequete(null, "categorie", "XXX", "=", "AND");
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie", Constantes.CAT_PF, "=", "OR"));
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie1", Constantes.CAT_MARCHANDISE, "=", "OR"));
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie2", Constantes.CAT_SERVICE, "=", "OR"));
        pa.addParam(p);
    }

    private void addParamCategorieProd() {
        ParametreRequete p = new ParametreRequete(null, "categorie", "XXX", "=", "AND");
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie", Constantes.CAT_PF, "=", "OR"));
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie1", Constantes.CAT_PSF, "=", "OR"));
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie2", Constantes.CAT_MP, "=", "OR"));
        pa.addParam(p);
    }

    private void addParamCategoriePlanProd() {
        ParametreRequete p = new ParametreRequete(null, "categorie", "XXX", "=", "AND");
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie", Constantes.CAT_PF, "=", "OR"));
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie1", Constantes.CAT_PSF, "=", "OR"));
        pa.addParam(p);
    }

    private void addParamCategorieMp() {
        ParametreRequete p = new ParametreRequete(null, "categorie", "XXX", "=", "AND");
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie", Constantes.CAT_MARCHANDISE, "=", "OR"));
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie1", Constantes.CAT_MP, "=", "OR"));
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie2", Constantes.CAT_EMBALLAGE, "=", "OR"));
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie3", Constantes.CAT_FOURNITURE, "=", "OR"));
        p.getOtherExpression().add(new ParametreRequete("y.categorie", "categorie4", Constantes.PIECE_DE_RECHANGE, "=", "OR"));
        pa.addParam(p);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.refArt", "reference", null, "=", "AND");
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch.toUpperCase() + "%", "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.refArt)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        pa.addParam(p);
        loadActifArticle(true, true);
    }

    public void initArticles(String type, Articles a) {
        if (a == null) {
            a = new Articles();
        }
        pa.addParam(new ParametreRequete("y.refArt", "refArt", null));
        if (type != null ? type.trim().length() > 0 : false) {
            switch (type) {
                case "A":
                    addParamCategorieMp();
                    break;
                case "V":
                    addParamCategoriePf();
                    break;
                case "P":
                    addParamCategorieProd();
                    break;
            }
        }
        loadActifArticle(true, true);
        a.setSelectArt(false);
        a.setListArt(true);
    }

    public void loadOnViewTiers(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseTiers bean = (YvsBaseTiers) ev.getObject();
            article.setFabricant(UtilTiers.buildBeanTiers(bean));
            update("select_fabricant");
        }
    }

    public void chooseTemplate(ValueChangeEvent ev) {
        if (ev != null) {
            Long id = (Long) ev.getNewValue();
            if (id >= 0) {
                ManagedTemplateArticle mt = (ManagedTemplateArticle) giveManagedBean(ManagedTemplateArticle.class);
                if (mt != null) {
                    int idx = mt.getTemplates().indexOf(new YvsBaseArticlesTemplate(id));
                    if (idx >= 0) {
                        selectTemplate = mt.getTemplates().get(idx);
                        mt.cloneValue(article, selectTemplate, (article.getId() <= 0));
                    }
                }
            }
        }
    }

    public void loadOnViewTemplate(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectTemplate = (YvsBaseArticlesTemplate) ev.getObject();
            ManagedTemplateArticle mt = (ManagedTemplateArticle) giveManagedBean(ManagedTemplateArticle.class);
            if (mt != null) {
                if (chaineSelectArt != null ? chaineSelectArt.trim().length() > 0 : false) {
                    openDialog("dlgConfirmApplyTemplate");
                } else {
                    if (article.getId() > 0) {
                        mt.cloneValue(article, selectTemplate, false);
                        openDialog("dlgConfirmApplyTemplate");
                    } else {
                        mt.cloneValue(article, selectTemplate, true);
                    }
                }
            }
        }
    }

    public void unLoadOnViewTemplate(UnselectEvent ev) {
        clearTemplate();
    }

    public void clearTemplate() {
        article.setTemplate(new TemplateArticles());
    }

    public void findTiers() {
        String num = article.getFabricant().getCodeTiers();
        article.getFabricant().setNom("");
        article.getFabricant().setPrenom("");
        article.getFabricant().setError(true);

        if (num != null ? num.trim().length() > 0 : false) {
            ManagedTiers mt = (ManagedTiers) giveManagedBean(ManagedTiers.class
            );
            if (mt
                    != null) {
                Tiers t = mt.findTiers(num, true);
                if (mt.getListTiers() != null ? !mt.getListTiers().isEmpty() : false) {
                    if (mt.getListTiers().size() > 1) {
                        update("data_tiers_article");
                    } else {
                        article.setFabricant(t);
                    }
                    article.getFabricant().setError(false);
                }
            }
        }
    }

    public
            void initTiers() {
        ManagedTiers a = (ManagedTiers) giveManagedBean(ManagedTiers.class
        );
        if (a
                != null) {
            a.initTiers(article.getFabricant());
        }

        update(
                "data_tiers_article");
    }

    public void genererTemplate() {
        try {
            if (selectArticle != null ? (selectArticle.getId() != null ? selectArticle.getId() > 0 : false) : false) {
                ManagedTemplateArticle mt = (ManagedTemplateArticle) giveManagedBean(ManagedTemplateArticle.class);
                if (mt != null) {
                    TemplateArticles t = mt.generer(UtilProd.buildBeanArticles(selectArticle));
                    if (t != null ? t.getId() == -1 : false) {
                        List<YvsBaseArticleCategorieComptable> lc = new ArrayList<>();
                        lc.addAll(t.getComptes());
                        t.getComptes().clear();
                        List<YvsBasePlanTarifaire> ly = new ArrayList<>();
                        ly.addAll(t.getTarifaires());
                        t.getTarifaires().clear();

                        YvsBaseArticlesTemplate y = mt.saveNew(t);
                        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                            List<YvsBaseArticleCategorieComptableTaxe> lt = new ArrayList<>();
                            for (YvsBaseArticleCategorieComptable c : lc) {
                                lt.clear();
                                lt.addAll(c.getTaxes());
                                c.getTaxes().clear();
                                c.setTemplate(y);
                                c.setId(null);
                                c = (YvsBaseArticleCategorieComptable) dao.save1(c);
                                for (YvsBaseArticleCategorieComptableTaxe a : lt) {
                                    a.setArticleCategorie(c);
                                    a.setId(null);
                                    a = (YvsBaseArticleCategorieComptableTaxe) dao.save1(a);
                                }
                            }
                            List<YvsBasePlanTarifaireTranche> lp = new ArrayList<>();
                            for (YvsBasePlanTarifaire c : ly) {
                                lp.clear();
                                lp.addAll(c.getGrilles());
                                c.getGrilles().clear();
                                c.setTemplate(y);
                                c.setId(null);
                                c = (YvsBasePlanTarifaire) dao.save1(c);
                                for (YvsBasePlanTarifaireTranche a : lp) {
                                    a.setPlan(c);
                                    a.setId(null);
                                    a = (YvsBasePlanTarifaireTranche) dao.save1(a);
                                }
                            }
                            succes();
                            update("data_templates_article");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Generation impossible");
            getException("Generation", ex);
        }
    }

    public void print() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("FAMILLE", (int) famillePrint);
            param.put("GROUPE", (int) groupePrint);
            param.put("CATEGORIE", categoriePrint);
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport("articles", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void print(String format) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("FAMILLE", (int) famillePrint);
            param.put("GROUPE", (int) groupePrint);
            param.put("CATEGORIE", categoriePrint);
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport("articles_no_header", param, "", format, false);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printTicket(String model) {
        try {
            if (currentParam == null) {
                currentParam = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            }
            String IDS = "";
            List<Integer> ids = decomposeSelection(chaineSelectArt);
            if (!ids.isEmpty() && !listArticle.isEmpty()) {
                for (Integer i : ids) {
                    IDS += listArticle.get(i).getId() + ",";
                }
            }
            Map<String, Object> param = new HashMap<>();
            param.put("societe", currentAgence.getSociete().getId().intValue());
            param.put("categorie", categoriePrint);
            param.put("famille", (long) famillePrint);
            param.put("groupe", (long) groupePrint);
            param.put("point", (long) pointPrint);
            param.put("model", model);
            if (IDS != null ? !IDS.isEmpty() : false) {
                param.put("IDS", IDS);
            }
            param.put("BACKCOLOR", currentParam != null ? "#" + currentParam.getBackColorEtiquette() : "#FFAF02");
            param.put("FORECOLOR", currentParam != null ? "#" + currentParam.getForeColorEtiquette() : "#C78801");
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport("etiquettes", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoPrintEtiquette() {
        System.err.println("chaineSelectArt : " + chaineSelectArt);

    }

    public void printInactf() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("DUREE_INACTIVE", currentParam != null ? currentParam.getDureeInactivArticle() : 30);
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport("articles_inactif", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printSansPua() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("CATEGORIE", categorieSearch);
            param.put("FAMILLE", (int) familleSearch);
            param.put("GROUPE", (int) groupeSearch);
            param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport("articles_no_pua", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printSansPuv() {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("CATEGORIE", categorieSearch);
            param.put("FAMILLE", (int) familleSearch);
            param.put("GROUPE", (int) groupeSearch);
            param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            executeReport("articles_no_puv", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private long choixDepot;
    private long choixAgence;

    public long getChoixDepot() {
        return choixDepot;
    }

    public void setChoixDepot(long choixDepot) {
        this.choixDepot = choixDepot;
    }

    public long getChoixAgence() {
        return choixAgence;
    }

    public void setChoixAgence(long choixAgence) {
        this.choixAgence = choixAgence;
    }

    public void addToDepot() {
        try {
            List<Integer> index = decomposeSelection(chaineSelectArt);
            if (!index.isEmpty()) {
                ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (choixDepot > 0) {
                    YvsBaseArticles a;
                    YvsBaseArticleDepot y;
                    YvsBaseDepots d = new YvsBaseDepots(choixDepot);
                    Long count = 0L;
                    for (int idx : index) {
                        a = listArticle.get(idx);
                        if (a != null ? a.getId() > 0 : false) {
                            count = (Long) dao.loadObjectByNameQueries("YvsBaseArticleDepot.counByArticleDepot", new String[]{"depot", "article"}, new Object[]{d, a});
                            if (count != null ? count < 1 : true) {
                                y = new YvsBaseArticleDepot();
                                y.setDepot(d);
                                y.setDepotPr(d);
                                y.setArticle(a);
                                y.setAuthor(currentUser);
                                y.setActif(a.getActif());
                                y.setSupp(false);
                                y.setDateSave(new Date());
                                y.setDateUpdate(new Date());
                                y.setRequiereLot(a.getRequiereLot() && a.getRequiereLot());
                                y.setNew_(true);
                                y.setSuiviStock(a.getSuiviEnStock());
                                y = (YvsBaseArticleDepot) dao.save1(y);
                                if (w != null) {
                                    w.getArticles_depot().add(y);
                                }
                            }
                        }
                    }
                    succes();
                    update(":formulaire_mainArticle:tbvArticle:article_depot_table");
                } else {
                    getErrorMessage("Veuillez choisir un dépôt !");
                }

            } else {
                getErrorMessage("Vous devez selectionner au moins un article");
            }
        } catch (NumberFormatException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void applyDepotPr() {
        try {
            List<Integer> index = decomposeSelection(chaineSelectArt);
            if (!index.isEmpty()) {
                long id;
                String query;
                if (choixDepot > 0) {
                    for (int idx : index) {
                        id = listArticle.get(idx).getId();
                        if (id > 0) {
                            query = "UPDATE yvs_base_article_depot ad SET default_pr=false "
                                    + "FROM yvs_base_depots d "
                                    + "WHERE (ad.depot=d.id) AND ad.article=? AND d.agence=? AND default_pr IS TRUE";
                            dao.requeteLibre(query, new yvs.dao.Options[]{new yvs.dao.Options(id, 1), new yvs.dao.Options(choixAgence, 2)});
                            query = "UPDATE yvs_base_article_depot SET default_pr=true WHERE article=? AND depot=?";
                            dao.requeteLibre(query, new yvs.dao.Options[]{new yvs.dao.Options(id, 1), new yvs.dao.Options(choixDepot, 2)});
                        }
                    }
                } else {
                    getErrorMessage("Veuillez choisir un dépôt !");
                }

            } else {
                getErrorMessage("Vous devez selectionner au moins un article");
            }
        } catch (NumberFormatException ex) {
            getException(tag, ex);
        }
        succes();
    }

    public void applyDepotPr_(YvsBaseArticleDepot ad) {
        try {
            if (ad != null) {
                ManagedStockArticle service = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (service != null) {
                    service.applyDepotPr_(ad);
                }
                ad.setDefaultPr(true);
            }

        } catch (NumberFormatException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void fusionner(boolean fusionne) {
        try {
            if (!autoriser("base_user_fusion")) {
                openNotAcces();
                return;
            }

            fusionneTo = "";
            fusionnesBy.clear();
            List<Integer> ids = decomposeSelection(chaineSelectArt);
            if (!ids.isEmpty() && !listArticle.isEmpty()) {
                long newValue = listArticle.get(ids.get(0)).getId();
                if (!fusionne) {
                    String oldValue = "0";
                    for (int i : ids) {
                        if (listArticle.get(i).getId() != newValue) {
                            oldValue += "," + listArticle.get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_base_articles", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                listArticle.remove(new YvsBaseArticles(id));
                            }
                        }
                    }
                    chaineSelectArt = "";
                    //Delete les doublons de article_depot
                    List<YvsBaseArticleDepot> idsDel = new ArrayList<>();
                    List<YvsBaseDepots> listIn = new ArrayList<>();
                    List<YvsBaseArticleDepot> liste = dao.loadNameQueries("YvsBaseArticleDepot.findByArticle", new String[]{"article"}, new Object[]{listArticle.get(ids.get(0))});
                    for (YvsBaseArticleDepot ad : liste) {
                        if (!listIn.contains(ad.getDepot())) {
                            listIn.add(ad.getDepot());
                        } else {
                            idsDel.add(ad);
                        }
                    }
                    for (YvsBaseArticleDepot ad : idsDel) {
                        dao.delete(ad);
                    }
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneTo = listArticle.get(idx).getDesignation();
                    } else {
                        YvsBaseArticles c = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneTo = c.getDesignation();
                        }
                    }
                    YvsBaseArticles c;
                    for (int i : ids) {
                        long oldValue = listArticle.get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesBy.add(listArticle.get(i).getDesignation());
                            }
                        } else {
                            c = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesBy.add(c.getDesignation());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 articles");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public void fusionnerUnite(boolean fusionne) {
        try {
            if (!autoriser("base_user_fusion")) {
                openNotAcces();
                return;
            }
            fusionneUniteTo = "";
            fusionnesUniteBy.clear();
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                long newValue = article.getConditionnements().get(ids.get(0)).getId();
                if (!fusionne) {
                    String oldValue = "0";
                    for (int i : ids) {
                        if (article.getConditionnements().get(i).getId() != newValue) {
                            oldValue += "," + article.getConditionnements().get(i).getId();
                        }
                    }
                    if (dao.fusionneData("yvs_base_conditionnement", newValue, oldValue)) {
                        for (String i : oldValue.split(",")) {
                            Long id = Long.valueOf(i);
                            if (id > 0 ? !id.equals(newValue) : false) {
                                article.getConditionnements().remove(new YvsBaseConditionnement(id));
                            }
                        }
                    }
                    tabIds = "";
                    succes();
                } else {
                    int idx = ids.get(0);
                    if (idx > -1) {
                        fusionneUniteTo = article.getConditionnements().get(idx).getUnite().getLibelle();
                    } else {
                        YvsBaseConditionnement c = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{newValue});
                        if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                            fusionneUniteTo = c.getUnite().getLibelle();
                        }
                    }
                    YvsBaseConditionnement c;
                    for (int i : ids) {
                        long oldValue = article.getConditionnements().get(i).getId();
                        if (i > -1) {
                            if (oldValue != newValue) {
                                fusionnesUniteBy.add(article.getConditionnements().get(i).getUnite().getLibelle());
                            }
                        } else {
                            c = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseConditionnement.findById", new String[]{"id"}, new Object[]{oldValue});
                            if (c != null ? (c.getId() != null ? c.getId() > 0 : false) : false) {
                                fusionnesUniteBy.add(c.getUnite().getLibelle());
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous devez selectionner au moins 2 conditionnements");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public void loadOnExport(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsStatExportEtat y = (YvsStatExportEtat) ev.getObject();
            model = y.getReference();
        }
    }

    public void unLoadOnExport(UnselectEvent ev) {
        model = null;
    }

    public void loadExport() {
        champ = new String[]{"code", "societe"};
        val = new Object[]{Constantes.EXPORT_ARTICLE, currentAgence.getSociete()};
        exports = dao.loadNameQueries("YvsStatExportEtat.findByCode", champ, val);
        update("data_model_export_article");
    }

    public void export(boolean force) {
        ManagedExportImport w = (ManagedExportImport) giveManagedBean(ManagedExportImport.class);
        if (w == null) {
            return;
        }
        List<Long> ids = new ArrayList<>();
        List<Integer> re = decomposeSelection(tabIds);
        for (Integer i : re) {
            ids.add(listArticle.get(i).getId());
        }
        Map<String, Object> donnees = new HashMap<>();
        donnees.put("ids", ids);
        if (force) {
            if (model != null ? model.trim().length() > 0 : false) {
                w.onExporter(model, donnees);
            } else {
                getErrorMessage("Vous devez selectionner le model d'exportation");
            }
        } else {
            loadExport();
            if (exports != null ? !exports.isEmpty() : false) {
                if (exports.size() > 1) {
                    openDialog("dlgListExport");
                } else {
                    model = exports.get(0).getReference();
                    w.onExporter(model, donnees);
                }
            }
        }
    }

    public boolean controleFiche(CodeBarre bean) {
        if (bean == null) {

            return false;
        }
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser l'article");
            return false;
        }
        if (bean.getCodeBarre() != null ? bean.getCodeBarre().trim().length() < 1 : true) {
            getErrorMessage("Vous devez préciser le code barre");
            return false;
        }
        return true;
    }

    public void resetCodeBarre() {
        codeBarre = new CodeBarre();
        update("form_code_barre");
    }

    public void addCodeBarre() {
        try {
            ManagedCondArticles w = (ManagedCondArticles) giveManagedBean(ManagedCondArticles.class);
            if (w != null) {
                YvsBaseArticleCodeBarre code = w.saveNewCode(entityArticle.getId(), selectCondit.getId(), codeBarre.getCodeBarre());
                if (code != null ? code.getId() > 0 : false) {
                    int index = selectCondit.getCodesBarres().indexOf(code);
                    if (index > -1) {
                        selectCondit.getCodesBarres().set(index, code);
                    } else {
                        selectCondit.getCodesBarres().add(0, code);
                    }
                    resetCodeBarre();
                }
            }
        } catch (Exception ex) {
            getException("Error", ex);
        }
    }

    public void deleteCodeBarre(YvsBaseArticleCodeBarre y) {
        try {
            if (y != null) {
                if (y.getId() > 0) {
                    dao.delete(y);
                }
                selectCondit.getCodesBarres().remove(y);
                if (y.getId() == codeBarre.getId()) {
                    resetCodeBarre();
                }
                succes();
            }
        } catch (Exception ex) {
            getException("Error", ex);
        }
    }

    public void loadOnViewCodeBarre(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseArticleCodeBarre y = (YvsBaseArticleCodeBarre) ev.getObject();
            codeBarre = UtilProd.buildBeanCodeBarre(y);
        }
    }

    public void unLoadOnViewCodeBarre(UnselectEvent ev) {
        resetCodeBarre();;
    }

    //Simulation des prix
    public void chooseDepot() {
        tranches = loadTranche(new YvsBaseDepots(depot), dateAction);
    }

    public void searchClient() {
        String num = client.getCodeClient();
        client.setId(0);
        client.setError(true);
        client.setTiers(new Tiers());
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            Client y = m.searchClient(num, false);
            if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                if (m.getClients().size() == 1) {
                    cloneObject(client, y);
                    update("value-client_simulation_prix");
                }
                client.setError(false);
            }
        }
    }

    public void simulationPrix() {
        if (selectUnite != null ? selectUnite.getId() > 0 : false) {
            ristourneResult = 0;
            remiseResult = 0;
            switch (typePrix) {
                case "V":
                    prixResult = dao.getPuv(selectUnite.getArticle().getId(), quantite, selectUnite.getPrix(), client.getId(), 0, point, dateAction, selectUnite.getId());
                    remiseResult = dao.getRemiseVente(selectUnite.getArticle().getId(), quantite, prixResult, client.getId(), point, dateAction, selectUnite.getUnite().getId());
                    ristourneResult = dao.getRistourne(selectUnite.getId(), quantite, prixResult, client.getId(), dateAction);
                    break;
                case "A":
                    prixResult = dao.getPua(selectUnite.getArticle().getId(), fournisseur.getId(), depot, selectUnite.getId(), dateAction, currentAgence.getId());
                    remiseResult = dao.getRemiseAchat(selectUnite.getArticle().getId(), quantite, selectUnite.getPrixAchat(), fournisseur.getId());
                    break;
                default:
                    prixResult = dao.getPr(selectUnite.getArticle().getId(), depot, trancheDepot, dateAction, selectUnite.getId());
                    break;
            }
            prixTotal = quantite * prixResult;
        }
    }

    private Object managedBean;

    public Object getManagedBean() {
        return managedBean;
    }

    public void setManagedBean(Object managedBean) {
        this.managedBean = managedBean;
    }

    public void onselectArticle(SelectEvent ev) {
        YvsBaseArticles art = (YvsBaseArticles) ev.getObject();
        Articles a = UtilProd.buildBeanArticles(art);
        invoqueMethodeLoad(a);
        currentSmallPageArt = listArticle.indexOf(art);
        update("save_nav_article");
    }

    public void drollable(DragDropEvent ev) {
        YvsBaseArticles y = ((YvsBaseArticles) ev.getData());
        listArticle.remove(y);
        selectArticles.add(0, y);
    }

    public void drollableAll() {
        for (YvsBaseArticles y : listArticle) {
            selectArticles.add(0, y);
        }
        listArticle.clear();
    }

    public void removeDrollable(YvsBaseArticles y) {
        listArticle.add(0, y);
        selectArticles.remove(y);
    }

    public void removeAllDrollable() {
        for (YvsBaseArticles y : selectArticles) {
            listArticle.add(0, y);
        }
        selectArticles.clear();
    }

    public void invoqueMethodeLoad(Articles art) {
        if (managedBean != null) {
            try {
                Method method = managedBean.getClass().getMethod("chooseArticle", Articles.class);
                method.invoke(managedBean, art);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(ManagedArticles.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(ManagedArticles.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ManagedArticles.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ManagedArticles.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ManagedArticles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private List<Object[]> articlesByCompte = new ArrayList<>();

    public List<Object[]> getArticlesByCompte() {
        return articlesByCompte;
    }

    public void setArticlesByCompte(List<Object[]> articlesByCompte) {
        this.articlesByCompte = articlesByCompte;
    }

    public void loadArticleByCompte(YvsBaseArticleCategorieComptable c) {
        if (c != null ? c.getCompte() != null : false) {
            articlesByCompte = dao.loadNameQueries("YvsBaseArticleCategorieComptable.findAllArticleByCompte",
                    new String[]{"compte"}, new Object[]{c.getCompte()});
            for (Object[] line : articlesByCompte) {
                System.err.println(" --- " + line[0]);
            }
            update("table_art_compte");
        }
    }

    public void deleteImg(String photo) {
        try {
            if (photo != null ? photo.trim().length() > 0 : false) {
                int index = article.getPhotos().indexOf(photo);
                if (index > -1) {
                    if (index == 0) {
                        article.setPhoto1("");
                        article.setPhoto1Extension("");
                    } else if (index == 1) {
                        article.setPhoto2("");
                        article.setPhoto2Extension("");
                    } else if (index == 2) {
                        article.setPhoto3("");
                        article.setPhoto3Extension("");
                    } else {
                        article.setPhoto("");
                        article.setPhotoExtension("");
                    }
                    YvsBaseArticles articles = UtilProd.buildEntityArticle(article);
                    dao.update(articles);
                    getInfoMessage("Photo effacer");
                    update("form_delete_img");
                }
            }
        } catch (Exception e) {
            e.getMessage();
            getErrorMessage("Erreur");
        }
    }

    public void openToaddPromotion(YvsBaseConditionnement cond) {
        ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (service != null) {
            service.setImax(200);
            service.loadAllPointVente(true, true);
            setSelectUnite(cond);
            choosePointVente();
        }
    }

    public void buildCodeBarre(YvsBaseConditionnement cond) {
        selectCondit = cond;
        selectCondit.setCodesBarres(dao.loadNameQueries("YvsBaseArticleCodeBarre.findByConditionnement", new String[]{"conditionnement"}, new Object[]{cond}));
        resetCodeBarre();
    }

    private long ids = -100000;

    public void loadTarifLivraison() {
        try {
            if (paysTarif < 1) {
                getErrorMessage("Vous devez selectionner un pays");
                return;
            }
            if (entityArticle != null ? entityArticle.getId() < 1 : true) {
                getErrorMessage("Vous devez choisir un article");
                return;
            }
            champ = new String[]{"article"};
            val = new Object[]{entityArticle};
            article.setTarifs(dao.loadNameQueries("YvsBaseTarifPointLivraison.findByArticle", champ, val));
            YvsBaseTarifPointLivraison tarif;
            List<YvsDictionnaire> lieux;
            ManagedDico service = (ManagedDico) giveManagedBean("Mdico");
//            List<YvsDictionnaire> lieux = dao.loadNameQueries("YvsDictionnaire.findByParent", new String[]{"parent"}, new Object[]{new YvsDictionnaire(villeTarif > 0 ? villeTarif : paysTarif)});
            champ = new String[]{"article"};
            if (villeTarif > 0) {
                lieux = new ArrayList<>(service.getSecteurs());
            } else {
                lieux = new ArrayList<>(service.getVilles());
            }
            boolean have;
            YvsDictionnaireInformations dico;
            for (YvsDictionnaire lieu : lieux) {
                have = false;
                for (YvsBaseTarifPointLivraison y : article.getTarifs()) {
                    if (y.getLieuxLiv().getLieux().equals(lieu)) {
                        have = true;
                        break;
                    }
                }
                if (!have) {
                    //vérifie l'existence de la relation dictionnaireInfo
                    dico = (YvsDictionnaireInformations) dao.loadOneByNameQueries("YvsDictionnaireInformations.findOne", new String[]{"lieux", "societe"}, new Object[]{lieu, currentAgence.getSociete()});
                    if (dico == null) {
                        lieu.setInformation(new YvsDictionnaireInformations(-1L));
                        service.activeLivraison(lieu);
                        dico = lieu.getInformation();
                    }
                    tarif = new YvsBaseTarifPointLivraison(ids--);
                    tarif.setArticle(entityArticle);
                    tarif.setAuthor(currentUser);
                    tarif.setLieuxLiv(dico);
                    tarif.setLivraisonDomicile(false);
                    article.getTarifs().add(tarif);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ERROR (loadTarifLivraison)", ex);
        }
    }

    public void removeTarifLivraison(YvsBaseTarifPointLivraison y) {
        try {
            if (y != null) {
                int index = article.getTarifs().indexOf(y);
                dao.delete(y);
                y.setId(ids--);
                article.getTarifs().set(index, y);
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ERROR (addTarifLivraison)", ex);
        }
    }

    public void actifLivraisonTarifLivraison(YvsBaseTarifPointLivraison y) {
        try {
            if (y != null) {
                y.setLivraisonDomicile(!y.getLivraisonDomicile());
                int index = article.getTarifs().indexOf(y);
                dao.update(y);
                article.getTarifs().set(index, y);
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ERROR (addTarifLivraison)", ex);
        }
    }

    public void onRowTarifLivraison(RowEditEvent ev) {
        try {
            if (ev != null) {
                YvsBaseTarifPointLivraison y = (YvsBaseTarifPointLivraison) ev.getObject();
                if (y != null) {
                    int index = article.getTarifs().indexOf(y);
                    if (y.getId() > 0) {
                        y.setLivraisonDomicile(true);
                        dao.update(y);
                    } else {
                        y.setId(null);
                        y.setLivraisonDomicile(true);
                        y = (YvsBaseTarifPointLivraison) dao.save1(y);
                        article.getTarifs().set(index, y);
                    }
                    succes();
                    update("data_warning_doc");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ERROR (onRowTarifLivraison)", ex);
        }
    }

    public List<YvsBaseArticles> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<YvsBaseArticles> listOrder) {
        this.listOrder = listOrder;
    }

    public int getNiveauSearch() {
        return niveauSearch;
    }

    public void setNiveauSearch(int niveauSearch) {
        this.niveauSearch = niveauSearch;
    }

    public List<Integer> getNiveau() {
        return niveau;
    }

    public void setNiveau(List<Integer> niveau) {
        this.niveau = niveau;
    }

    public List<ArticleOrdre> getArticlesOrdres() {
        return articlesOrdres;
    }

    public void setArticlesOrdres(List<ArticleOrdre> articlesOrdres) {
        this.articlesOrdres = articlesOrdres;
    }

    public int getNiveauMax() {
        return niveauMax;
    }

    public void setNiveauMax(int niveauMax) {
        this.niveauMax = niveauMax;
    }

    public void selectArt(YvsBaseArticles art) {
        bean = art;
    }

    public void mettreEnAvant(int niveaux) {
        try {
            Integer ordre = bean.getOrdre();
            if (bean != null ? bean.getId() > 0 : false) {
                bean.setOrdre(niveaux);
                dao.update(bean);

                int old_index = articlesOrdres.indexOf(new ArticleOrdre(ordre, currentAgence.getSociete()));
                if (old_index > -1) {
                    articlesOrdres.get(old_index).getArticles().remove(bean);
                }
                int new_index = articlesOrdres.indexOf(new ArticleOrdre(niveaux, currentAgence.getSociete()));
                if (new_index > -1) {
                    articlesOrdres.get(new_index).getArticles().add(bean);

                }
                niveau = dao.loadNameQueries("YvsBaseArticles.findAllOrder", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                niveau = niveau != null ? niveau : new ArrayList<Integer>();
                niveauMax = (niveau.get(0) != null ? niveau.get(0) : 0) + 1;
                succes();
                update("tab_article");

            } else {
                getErrorMessage("Aucun article selectionner !");
            }
        } catch (Exception e) {
            getErrorMessage("Action impossible");
            getException("ERROR (onRowTarifLivraison)", e);
        }
    }

    public YvsBaseArticles getBean() {
        return bean;
    }

    public void setBean(YvsBaseArticles bean) {
        this.bean = bean;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public long getFamilySearch() {
        return familySearch;
    }

    public void setFamilySearch(long familySearch) {
        this.familySearch = familySearch;
    }

    public String getSearchArticles() {
        return searchArticles;
    }

    public void setSearchArticles(String searchArticles) {
        this.searchArticles = searchArticles;
    }

    public void loadOrder(boolean avancer, boolean init) {
        try {
            niveau = dao.loadNameQueries("YvsBaseArticles.findAllOrder", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            niveau = niveau != null ? niveau : new ArrayList<Integer>();
            niveauMax = (niveau.get(0) != null ? niveau.get(0) : 0) + 1;
            ArticleOrdre articleOrdre;
            articlesOrdres.clear();

            if (niveauSearch < 0) {
                niveau = dao.loadNameQueries("YvsBaseArticles.findAllOrder", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                niveau = niveau != null ? niveau : new ArrayList<Integer>();
                niveauMax = (niveau.get(0) != null ? niveau.get(0) : 0) + 1;
                for (int i = 0; i < niveau.size(); i++) {
                    articleOrdre = new ArticleOrdre(niveau.get(i), currentAgence.getSociete());
                    articleOrdre.setNiveau(niveau.get(i));
                    ParametreRequete p;
                    if (Util.asString(cat)) {
                        p = new ParametreRequete("y.categorie", "categorie", cat);
                        p.setOperation("=");
                        p.setPredicat("AND");
                        articleOrdre.paginator.addParam(p);
                    }
                    if (familySearch > 0) {
                        p = new ParametreRequete("y.famille", "famille", null);
                        p.setObjet(new YvsBaseFamilleArticle(familySearch));
                        p.setOperation("=");
                        p.setPredicat("AND");
                        articleOrdre.paginator.addParam(p);
                    }

                    if (Util.asString(searchArticles)) {
                        p = new ParametreRequete(null, "refArt", searchArticles + "%", "LIKE ", "AND");
                        p.getOtherExpression().add(new ParametreRequete("UPPER(y.refArt)", "refArt", searchArticles.toUpperCase() + "%", "LIKE ", "OR"));
                        p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "designation", searchArticles.toUpperCase() + "%", "LIKE ", "OR"));
                        articleOrdre.paginator.addParam(p);
                    }

                    List<YvsBaseArticles> articleses = articleOrdre.paginator.executeDynamicQuery("y", "y", "YvsBaseArticles y LEFT JOIN FETCH y.groupe "
                            + " JOIN FETCH y.famille ", "y.ordre DESC", avancer, init, (int) imax, dao);
                    articleOrdre.setArticles(articleses);
                    articlesOrdres.add(articleOrdre);
                }

            } else {
                niveau = dao.loadNameQueries("YvsBaseArticles.findAllOrder", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                niveau = niveau != null ? niveau : new ArrayList<Integer>();
                niveauMax = (niveau.get(0) != null ? niveau.get(0) : 0) + 1;
                articleOrdre = new ArticleOrdre(niveauSearch, currentAgence.getSociete());
                articleOrdre.setNiveau(niveauSearch);
                ParametreRequete p;
                if (Util.asString(cat)) {
                    p = new ParametreRequete("y.categorie", "categorie", cat);
                    p.setOperation("=");
                    p.setPredicat("AND");
                    articleOrdre.paginator.addParam(p);
                }
                if (familySearch > 0) {
                    p = new ParametreRequete("y.famille", "famille", null);
                    p.setObjet(new YvsBaseFamilleArticle(familySearch));
                    p.setOperation("=");
                    p.setPredicat("AND");
                    articleOrdre.paginator.addParam(p);
                }
                if (Util.asString(searchArticles)) {
                    p = new ParametreRequete(null, "refArt", searchArticles + "%", "LIKE ", "AND");
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.refArt)", "refArt", searchArticles.toUpperCase() + "%", "LIKE ", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "designation", searchArticles.toUpperCase() + "%", "LIKE ", "OR"));
                    articleOrdre.paginator.addParam(p);
                }
                List<YvsBaseArticles> articleses = articleOrdre.paginator.executeDynamicQuery("y", "y", "YvsBaseArticles y LEFT JOIN FETCH y.groupe "
                        + " JOIN FETCH y.famille ", "y.ordre DESC", avancer, init, (int) imax, dao);
                articleOrdre.setArticles(articleses);
                articlesOrdres.add(articleOrdre);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("erreur = " + e.getMessage());
        }

    }

    public void loadOrdre(ArticleOrdre articleOrdre, boolean avancer, boolean init) {
        System.err.println(articleOrdre + " paginator");
        List<YvsBaseArticles> articleses = articleOrdre.paginator.executeDynamicQuery("y", "y", "YvsBaseArticles y LEFT JOIN FETCH y.groupe "
                + " JOIN FETCH y.famille ", "y.ordre DESC", avancer, init, (int) articleOrdre.paginator.getRows(), dao);
        articleOrdre.setArticles(articleses);

    }

    public void addNiveau() {
        ParametreRequete p = new ParametreRequete("y.ordre", "ordre", null);
        p.setObjet(niveauSearch);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadOrder(false, true);

    }

    public void loadPointDeVente(YvsBaseConditionnement entity) {
        pointsVentes.clear();
        if (entity != null ? entity.getId() > 0 : false) {
            selectCondit = entity;
            List<YvsBasePointVente> list;
            if (!autoriser("pv_view_all_societe")) {
                list = dao.loadNameQueries("YvsBasePointVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            } else {
                list = dao.loadNameQueries("YvsBasePointVente.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            }
            YvsBaseConditionnementPoint y;
            YvsBaseArticlePoint ap;
            for (YvsBasePointVente p : list) {
                y = (YvsBaseConditionnementPoint) dao.loadOneByNameQueries("YvsBaseConditionnementPoint.findArticlePointUnite", new String[]{"article", "unite", "point"}, new Object[]{entity.getArticle(), entity, p});
                if (y != null ? y.getId() < 1 : true) {
                    ap = (YvsBaseArticlePoint) dao.loadOneByNameQueries("YvsBaseArticlePoint.findArticlePoint", new String[]{"article", "point"}, new Object[]{entity.getArticle(), p});
                    if (ap != null ? ap.getId() < 1 : true) {
                        ap = new YvsBaseArticlePoint(entity.getArticle(), p);
                        ap.setActif(true);
                        ap.setPuv(entity.getPrix());
                        ap.setPuvMin(entity.getPrixMin());
                        ap.setRemise(entity.getRemise());
                        ap.setNaturePrixMin(entity.getNaturePrixMin());
                        ap.setChangePrix(entityArticle.getChangePrix());
                        ap.setConditionementVente(entity);
                        ap.setAuthor(currentUser);
                    }
                    y = new YvsBaseConditionnementPoint(YvsBaseConditionnementPoint.ids--, ap, entity);
                    y.setAvanceCommance(0.0);
                    y.setActif(true);
                    y.setPuv(entity.getPrix());
                    y.setPrixMin(entity.getPrixMin());
                    y.setRemise(entity.getRemise());
                }
                y.setAuthor(currentUser);
                pointsVentes.add(y);
            }
            openDialog("dlgPtarticle");
            update("form_update_art_pt");
        }
    }

    public void activeConditionnementPoint(YvsBaseConditionnementPoint y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setDateUpdate(new Date());
                y.setActif(!y.getActif());
                y.setAuthor(currentUser);
                y.setDateUpdate(new Date());
                dao.update(y);
                y.getArticle().setActif(y.getActif());
                y.getArticle().setAuthor(currentUser);
                y.getArticle().setDateUpdate(new Date());
                dao.update(y.getArticle());
                int index = pointsVentes.indexOf(y);
                if (index > -1) {
                    pointsVentes.set(index, y);
                }
                succes();
            }
        } catch (Exception ex) {
            getException("activeConditionnementPoint", ex);
        }
    }

    public void removeConditionnementPoint(YvsBaseConditionnementPoint y) {
        try {
            if (!autoriser("base_article_update_puv")) {
                openNotAcces();
                return;
            }
            if (y != null ? y.getId() > 0 : false) {
                int index = pointsVentes.indexOf(y);
                y.setAuthor(currentUser);
                y.setDateUpdate(new Date());
                dao.delete(y);
                y.setId(YvsBaseConditionnementPoint.ids--);
                if (index > -1) {
                    pointsVentes.set(index, y);
                }
                succes();
            }
        } catch (Exception ex) {
            getException("removeConditionnementPoint", ex);
        }
    }

    public void addConditionnementPoint(YvsBaseConditionnementPoint y) {
        try {
            if (!autoriser("base_article_update_puv")) {
                openNotAcces();
                return;
            }
            if (y != null ? y.getId() < 1 : true) {
                int index = pointsVentes.indexOf(y);
                if (y.getArticle() == null) {
                    getErrorMessage("Action impossible!!!");
                    return;
                }
                if (y.getArticle() != null ? y.getArticle().getId() < 1 : true) {
                    y.getArticle().setId(null);
                    y.getArticle().setDateUpdate(new Date());
                    y.getArticle().setAuthor(currentUser);
                    y.setArticle((YvsBaseArticlePoint) dao.save1(y.getArticle()));
                }
                if (!y.getArticle().getActif()) {
                    y.getArticle().setActif(true);
                    dao.update(y.getArticle());
                }
                y.setId(null);
                y.setAuthor(currentUser);
                y.setDateUpdate(new Date());
                y = (YvsBaseConditionnementPoint) dao.save1(y);
                if (index > -1) {
                    pointsVentes.set(index, y);
                }
                succes();
            }
        } catch (Exception ex) {
            getException("addConditionnementPoint", ex);
        }
    }

    public void updateConditionnementPoint(RowEditEvent ev) {
        if (ev != null) {
            if (!autoriser("base_article_update_puv")) {
                openNotAcces();
                return;
            }
            YvsBaseConditionnementPoint y = (YvsBaseConditionnementPoint) ev.getObject();
            y.setDateUpdate(new Date());
            dao.update(y);
            succes();
        }
    }

    public double getPua(YvsBaseConditionnement y) {
        return dao.getPua(article.getId(), 0, 0, y.getId(), new Date(), currentAgence.getId());
    }

    public void filterByCategorie() {
        loadOrder(false, true);
    }

    public void filterByFamille() {
        loadOrder(false, true);
    }

    public void filterByRef() {
        loadOrder(false, true);
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private boolean changePuaTTc, changePuvTTc, changeCategorie, changeMethodeVal, changeFamille, changeSuiviStock, changepuvVariable, changeGestionLot;
    private boolean pptePuvTTC, pptePuaTTC, ppteSuiviStock, ppteGestionLot, ppteSuiviPr, pptePuvVariable;
    private String ppteCategorie, ppteMethodeVal;
    private Long ppteFamille;

    public boolean isChangePuaTTc() {
        return changePuaTTc;
    }

    public void setChangePuaTTc(boolean chagePuaTTc) {
        this.changePuaTTc = chagePuaTTc;
    }

    public boolean isChangePuvTTc() {
        return changePuvTTc;
    }

    public void setChangePuvTTc(boolean changePuvTTc) {
        this.changePuvTTc = changePuvTTc;
    }

    public boolean isChangeCategorie() {
        return changeCategorie;
    }

    public void setChangeCategorie(boolean changeCategorie) {
        this.changeCategorie = changeCategorie;
    }

    public boolean isChangeMethodeVal() {
        return changeMethodeVal;
    }

    public void setChangeMethodeVal(boolean changeMethodeVal) {
        this.changeMethodeVal = changeMethodeVal;
    }

    public boolean isChangeFamille() {
        return changeFamille;
    }

    public void setChangeFamille(boolean changeFamille) {
        this.changeFamille = changeFamille;
    }

    public boolean isChangeSuiviStock() {
        return changeSuiviStock;
    }

    public void setChangeSuiviStock(boolean changeSuiviStock) {
        this.changeSuiviStock = changeSuiviStock;
    }

    public boolean isChangepuvVariable() {
        return changepuvVariable;
    }

    public void setChangepuvVariable(boolean changepuvVariable) {
        this.changepuvVariable = changepuvVariable;
    }

    public boolean isChangeGestionLot() {
        return changeGestionLot;
    }

    public void setChangeGestionLot(boolean changeGestionLot) {
        this.changeGestionLot = changeGestionLot;
    }

    public boolean isPptePuvTTC() {
        return pptePuvTTC;
    }

    public void setPptePuvTTC(boolean pptePuvTTC) {
        this.pptePuvTTC = pptePuvTTC;
    }

    public boolean isPptePuaTTC() {
        return pptePuaTTC;
    }

    public void setPptePuaTTC(boolean pptePuaTTC) {
        this.pptePuaTTC = pptePuaTTC;
    }

    public boolean isPpteSuiviStock() {
        return ppteSuiviStock;
    }

    public void setPpteSuiviStock(boolean ppteSuiviStock) {
        this.ppteSuiviStock = ppteSuiviStock;
    }

    public boolean isPpteGestionLot() {
        return ppteGestionLot;
    }

    public void setPpteGestionLot(boolean ppteGestionLot) {
        this.ppteGestionLot = ppteGestionLot;
    }

    public boolean isPpteSuiviPr() {
        return ppteSuiviPr;
    }

    public void setPpteSuiviPr(boolean ppteSuiviPt) {
        this.ppteSuiviPr = ppteSuiviPt;
    }

    public boolean isPptePuvVariable() {
        return pptePuvVariable;
    }

    public void setPptePuvVariable(boolean pptePuvVariable) {
        this.pptePuvVariable = pptePuvVariable;
    }

    public String getPpteCategorie() {
        return ppteCategorie;
    }

    public void setPpteCategorie(String ppteCategorie) {
        this.ppteCategorie = ppteCategorie;
    }

    public Long getPpteFamille() {
        return ppteFamille;
    }

    public void setPpteFamille(Long ppteFamille) {
        this.ppteFamille = ppteFamille;
    }

    public String getPpteMethodeVal() {
        return ppteMethodeVal;
    }

    public void setPpteMethodeVal(String ppteMethodeVal) {
        this.ppteMethodeVal = ppteMethodeVal;
    }

    public void applyChangeForArticle() {
        List<Integer> keys = decomposeSelection(chaineSelectArt);
        if (!keys.isEmpty()) {
            YvsBaseArticles a;
            for (int i : keys) {
                a = listArticle.get(i);
                if (changeCategorie) {
                    a.setCategorie(ppteCategorie);
                }
                if (changeFamille) {
                    ManagedFamilleArticle service = (ManagedFamilleArticle) giveManagedBean(ManagedFamilleArticle.class);
                    if (service != null) {
                        int idx = service.getFamilles().indexOf(new YvsBaseFamilleArticle(ppteFamille));
                        if (idx >= 0) {
                            a.setFamille(service.getFamilles().get(idx));
                        }
                    }
                }
                if (changeGestionLot) {
                    a.setRequiereLot(ppteGestionLot);
                }
                if (changeMethodeVal) {
                    a.setMethodeVal(ppteMethodeVal);
                }
                if (changePuaTTc) {
                    a.setPuaTtc(pptePuaTTC);
                }
                if (changePuvTTc) {
                    a.setPuvTtc(pptePuvTTC);
                }
                if (changepuvVariable) {
                    a.setChangePrix(pptePuvVariable);
                }
                if (changeSuiviStock) {
                    a.setSuiviEnStock(ppteSuiviStock);
                }
                a.setDateUpdate(new Date());
                a.setAuthor(currentUser);
                dao.update(a);
                listArticle.set(i, a);
            }
            succes();
        } else {
            getWarningMessage("Aucune selection n'a été trouvé !");
        }
    }

}
