/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.commercial.UtilCom;
import yvs.commercial.depot.ArticleDepot;
import yvs.base.produits.ManagedArticles;
import yvs.production.UtilProd;
import yvs.commercial.ManagedCommercial;
import static yvs.commercial.ManagedCommercial.currentParam;
import yvs.commercial.achat.ManagedLotReception;
import yvs.commercial.depot.ArticleEmplacement;
import yvs.commercial.depot.ConditionnementDepot;
import yvs.commercial.depot.Emplacement;
import yvs.commercial.depot.ManagedDepot;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticleEmplacement;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseConditionnementDepot;
import yvs.entity.base.YvsBaseDepotOperation;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseEmplacementDepot;
import yvs.entity.base.YvsBaseMouvementStock;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.etats.Dashboards;
import yvs.etats.SeuilStocks;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedStockArticle extends ManagedCommercial<MouvementStock, YvsBaseArticleDepot> implements Serializable {

    @ManagedProperty(value = "#{mouvementStock}")
    private MouvementStock mouvementStock;

    private Depots depotPr = new Depots();
    private String modeAppro = Constantes.APPRO_ACHT_PROD_EN;
    public YvsAgences agence = new YvsAgences();

    private List<YvsBaseArticleDepot> articles_stock, articlesDebut, articlesFin;
    private List<YvsBaseArticleDepot> articles_stock_accueil;
    private ArticleDepot article = new ArticleDepot();
    private long idArtDeb, idArtFin;
    private String articleDebut, articleFin;
    private String artSearch, catSearch, statutEntree = Constantes.ETAT_VALIDE, typeEntreeOn = "R", orderBy = "code", articlesSearch;
    private long famSearch, grpSearch, depotSearch, trancheSearch, emplacementSearch;
    boolean select;
    private Date dateSearch = new Date(), dateEntree = new Date();
    private Boolean actSearch = true;
    private boolean withChildSearch = false;

    private ConditionnementDepot conditionnement = new ConditionnementDepot();
    private PaginatorResult<YvsBaseArticleDepot> p_article = new PaginatorResult<>();
    private List<YvsBaseArticleDepot> articles_depot, articlesSelect;
    private YvsBaseArticleDepot selectArticle, stock;
    private YvsBaseDepots entityDepot;

    private long famille;
    private String categorie;
    private YvsBasePointVente entityPoint;
    private YvsBaseArticles entityArticle;
    private Boolean onArticle;
    private boolean suiviParLot;
    private List<YvsGrhTrancheHoraire> tranches;

    private List<YvsBaseConditionnement> conditionnements;
    private List<YvsBaseArticleDepot> articlesResult;
    private List<YvsBaseArticles> articles;
    private PaginatorResult<YvsBaseArticles> pv = new PaginatorResult<>();

    private Emplacement emplacement = new Emplacement();
    private List<YvsBaseEmplacementDepot> emplacements, emplacementsArticle, emplacementsParents;
    private YvsBaseEmplacementDepot selectEmplacement;

    private double totaux = 0;
    private Boolean suiviEnStock, depotActif = true, withLot;
    private boolean stock_, selectArt, listArt, multi;
    private String tabIds, tabIds_emplacement, numSearch, categorieSearch, soldeSearch = "", typeUnite = "V";
    private Long familleSearch;
    private Boolean paramActifF;

    private boolean needConfirmation = false;

    private List<YvsBaseArticleDepot> listArticle = new ArrayList<YvsBaseArticleDepot>();
    private List<YvsComLotReception> lots = new ArrayList<YvsComLotReception>();

    private PaginatorResult<YvsBaseArticleDepot> pa = new PaginatorResult<>();
    private Dashboards inventaire = new Dashboards();

    private boolean displayPrixRevient = false, displayResteALivrer = false, displayAvgPuv = false;

    public ManagedStockArticle() {
        tranches = new ArrayList<>();
        emplacements = new ArrayList<>();
        articlesResult = new ArrayList<>();
        articles = new ArrayList<>();
        emplacementsArticle = new ArrayList<>();
        emplacementsParents = new ArrayList<>();
        articles_depot = new ArrayList<>();
        articlesSelect = new ArrayList<>();
        articles_stock = new ArrayList<>();
        articles_stock_accueil = new ArrayList<>();
        articlesDebut = new ArrayList<>();
        articlesFin = new ArrayList<>();
        conditionnements = new ArrayList<>();
    }

    public boolean isDisplayPrixRevient() {
        return displayPrixRevient;
    }

    public void setDisplayPrixRevient(boolean displayPrixRevient) {
        this.displayPrixRevient = displayPrixRevient;
    }

    public boolean isDisplayResteALivrer() {
        return displayResteALivrer;
    }

    public void setDisplayResteALivrer(boolean displayResteALivrer) {
        this.displayResteALivrer = displayResteALivrer;
    }

    public boolean isDisplayAvgPuv() {
        return displayAvgPuv;
    }

    public void setDisplayAvgPuv(boolean displayAvgPuv) {
        this.displayAvgPuv = displayAvgPuv;
    }

    public Long getFamilleSearch() {
        return familleSearch;
    }

    public void setFamilleSearch(Long familleSearch) {
        this.familleSearch = familleSearch;
    }

    public String getArticlesSearch() {
        return articlesSearch;
    }

    public void setArticlesSearch(String articlesSearch) {
        this.articlesSearch = articlesSearch;
    }

    public double getTotaux() {
        return totaux;
    }

    public void setTotaux(double totaux) {
        this.totaux = totaux;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }

    public Dashboards getInventaire() {
        return inventaire;
    }

    public void setInventaire(Dashboards inventaire) {
        this.inventaire = inventaire;
    }

    public YvsBaseArticleDepot getStock() {
        return stock;
    }

    public void setStock(YvsBaseArticleDepot stock) {
        this.stock = stock;
    }

    public List<YvsComLotReception> getLots() {
        return lots;
    }

    public void setLots(List<YvsComLotReception> lots) {
        this.lots = lots;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getModeAppro() {
        return modeAppro;
    }

    public void setModeAppro(String modeAppro) {
        this.modeAppro = modeAppro;
    }

    public boolean isWithChildSearch() {
        return withChildSearch;
    }

    public void setWithChildSearch(boolean withChildSearch) {
        this.withChildSearch = withChildSearch;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public List<YvsBaseConditionnement> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnement> conditionnements) {
        this.conditionnements = conditionnements;
    }

    public long getEmplacementSearch() {
        return emplacementSearch;
    }

    public void setEmplacementSearch(long emplacementSearch) {
        this.emplacementSearch = emplacementSearch;
    }

    public Depots getDepotPr() {
        return depotPr;
    }

    public void setDepotPr(Depots depotPr) {
        this.depotPr = depotPr;
    }

    public long getFamille() {
        return famille;
    }

    public void setFamille(long famille) {
        this.famille = famille;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public boolean isSuiviParLot() {
        return suiviParLot;
    }

    public void setSuiviParLot(boolean suiviParLot) {
        this.suiviParLot = suiviParLot;
    }

    public String getCategorieSearch() {
        return categorieSearch;
    }

    public void setCategorieSearch(String categorieSearch) {
        this.categorieSearch = categorieSearch;
    }

    public String getTypeEntreeOn() {
        return typeEntreeOn;
    }

    public void setTypeEntreeOn(String typeEntreeOn) {
        this.typeEntreeOn = typeEntreeOn;
    }

    public String getStatutEntree() {
        return statutEntree;
    }

    public void setStatutEntree(String statutEntree) {
        this.statutEntree = statutEntree;
    }

    public Date getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(Date dateEntree) {
        this.dateEntree = dateEntree;
    }

    public Date getDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(Date dateSearch) {
        this.dateSearch = dateSearch;
    }

    public long getTrancheSearch() {
        return trancheSearch;
    }

    public void setTrancheSearch(long trancheSearch) {
        this.trancheSearch = trancheSearch;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public String getTypeUnite() {
        return typeUnite != null ? typeUnite.trim().length() > 0 ? typeUnite : "V" : "V";
    }

    public void setTypeUnite(String typeUnite) {
        this.typeUnite = typeUnite;
    }

    public boolean isNeedConfirmation() {
        return needConfirmation;
    }

    public void setNeedConfirmation(boolean needConfirmation) {
        this.needConfirmation = needConfirmation;
    }

    public String getSoldeSearch() {
        return soldeSearch != null ? soldeSearch : "";
    }

    public void setSoldeSearch(String soldeSearch) {
        this.soldeSearch = soldeSearch;
    }

    public ConditionnementDepot getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(ConditionnementDepot conditionnement) {
        this.conditionnement = conditionnement;
    }

    public List<YvsBaseArticleDepot> getArticlesSelect() {
        return articlesSelect;
    }

    public void setArticlesSelect(List<YvsBaseArticleDepot> articlesSelect) {
        this.articlesSelect = articlesSelect;
    }

    public PaginatorResult<YvsBaseArticleDepot> getP_article() {
        return p_article;
    }

    public void setP_article(PaginatorResult<YvsBaseArticleDepot> p_article) {
        this.p_article = p_article;
    }

    public long getIdArtDeb() {
        return idArtDeb;
    }

    public void setIdArtDeb(long idArtDeb) {
        this.idArtDeb = idArtDeb;
    }

    public long getIdArtFin() {
        return idArtFin;
    }

    public void setIdArtFin(long idArtFin) {
        this.idArtFin = idArtFin;
    }

    public Boolean getActSearch() {
        return actSearch;
    }

    public void setActSearch(Boolean actSearch) {
        this.actSearch = actSearch;
    }

    public long getGrpSearch() {
        return grpSearch;
    }

    public void setGrpSearch(long grpSearch) {
        this.grpSearch = grpSearch;
    }

    public long getFamSearch() {
        return famSearch;
    }

    public void setFamSearch(long famSearch) {
        this.famSearch = famSearch;
    }

    public String getCatSearch() {
        return catSearch;
    }

    public void setCatSearch(String catSearch) {
        this.catSearch = catSearch;
    }

    public String getArtSearch() {
        return artSearch;
    }

    public void setArtSearch(String artSearch) {
        this.artSearch = artSearch;
    }

    public Boolean getParamActifF() {
        return paramActifF;
    }

    public void setParamActifF(Boolean paramActifF) {
        this.paramActifF = paramActifF;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    public PaginatorResult<YvsBaseArticles> getPv() {
        return pv;
    }

    public void setPv(PaginatorResult<YvsBaseArticles> pv) {
        this.pv = pv;
    }

    public PaginatorResult<YvsBaseArticleDepot> getPa() {
        return pa;
    }

    public void setPa(PaginatorResult<YvsBaseArticleDepot> pa) {
        this.pa = pa;
    }

    public YvsBasePointVente getEntityPoint() {
        return entityPoint;
    }

    public void setEntityPoint(YvsBasePointVente entityPoint) {
        this.entityPoint = entityPoint;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public Boolean getOnArticle() {
        return onArticle;
    }

    public void setOnArticle(Boolean onArticle) {
        this.onArticle = onArticle;
    }

    public YvsBaseDepots getEntityDepot() {
        return entityDepot;
    }

    public void setEntityDepot(YvsBaseDepots entityDepot) {
        this.entityDepot = entityDepot;
    }

    public YvsBaseArticles getEntityArticle() {
        return entityArticle;
    }

    public void setEntityArticle(YvsBaseArticles entityArticle) {
        this.entityArticle = entityArticle;
    }

    public String getTabIds_emplacement() {
        return tabIds_emplacement;
    }

    public void setTabIds_emplacement(String tabIds_emplacement) {
        this.tabIds_emplacement = tabIds_emplacement;
    }

    public Emplacement getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(Emplacement emplacement) {
        this.emplacement = emplacement;
    }

    public List<YvsBaseEmplacementDepot> getEmplacements() {
        return emplacements;
    }

    public void setEmplacements(List<YvsBaseEmplacementDepot> emplacements) {
        this.emplacements = emplacements;
    }

    public List<YvsBaseEmplacementDepot> getEmplacementsArticle() {
        return emplacementsArticle;
    }

    public void setEmplacementsArticle(List<YvsBaseEmplacementDepot> emplacementsArticle) {
        this.emplacementsArticle = emplacementsArticle;
    }

    public List<YvsBaseEmplacementDepot> getEmplacementsParents() {
        return emplacementsParents;
    }

    public void setEmplacementsParents(List<YvsBaseEmplacementDepot> emplacementsParents) {
        this.emplacementsParents = emplacementsParents;
    }

    public YvsBaseEmplacementDepot getSelectEmplacement() {
        return selectEmplacement;
    }

    public void setSelectEmplacement(YvsBaseEmplacementDepot selectEmplacement) {
        this.selectEmplacement = selectEmplacement;
    }

    public List<YvsBaseArticleDepot> getArticles_stock() {
        return articles_stock;
    }

    public void setArticles_stock(List<YvsBaseArticleDepot> articles_stock) {
        this.articles_stock = articles_stock;
    }

    public List<YvsBaseArticleDepot> getArticlesDebut() {
        return articlesDebut;
    }

    public void setArticlesDebut(List<YvsBaseArticleDepot> articlesDebut) {
        this.articlesDebut = articlesDebut;
    }

    public List<YvsBaseArticleDepot> getArticlesFin() {
        return articlesFin;
    }

    public void setArticlesFin(List<YvsBaseArticleDepot> articlesFin) {
        this.articlesFin = articlesFin;
    }

    public YvsBaseArticleDepot getSelectArticle() {
        return selectArticle;
    }

    public void setSelectArticle(YvsBaseArticleDepot selectArticle) {
        this.selectArticle = selectArticle;
    }

    public ArticleDepot getArticle() {
        return article;
    }

    public void setArticle(ArticleDepot article) {
        this.article = article;
    }

    public String getTabIds() {
        return tabIds;
    }

    public List<YvsBaseArticleDepot> getArticlesResult() {
        return articlesResult;
    }

    public void setArticlesResult(List<YvsBaseArticleDepot> articlesResult) {
        this.articlesResult = articlesResult;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public List<YvsGrhTrancheHoraire> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsGrhTrancheHoraire> tranches) {
        this.tranches = tranches;
    }

    public String getArticleDebut() {
        return articleDebut;
    }

    public void setArticleDebut(String articleDebut) {
        this.articleDebut = articleDebut;
    }

    public String getArticleFin() {
        return articleFin;
    }

    public void setArticleFin(String articleFin) {
        this.articleFin = articleFin;
    }

    public boolean isStock_() {
        return stock_;
    }

    public void setStock_(boolean stock_) {
        this.stock_ = stock_;
    }

    public Boolean getSuiviEnStock() {
        return suiviEnStock;
    }

    public void setSuiviEnStock(Boolean suiviEnStock) {
        this.suiviEnStock = suiviEnStock;
    }

    public Boolean getDepotActif() {
        return depotActif;
    }

    public void setDepotActif(Boolean depotActif) {
        this.depotActif = depotActif;
    }

    public Boolean getWithLot() {
        return withLot;
    }

    public void setWithLot(Boolean withLot) {
        this.withLot = withLot;
    }

    public List<YvsBaseArticleDepot> getArticles_depot() {
        return articles_depot;
    }

    public void setArticles_depot(List<YvsBaseArticleDepot> articles_depot) {
        this.articles_depot = articles_depot;
    }

    public MouvementStock getMouvementStock() {
        return mouvementStock;
    }

    public void setMouvementStock(MouvementStock mouvementStock) {
        this.mouvementStock = mouvementStock;
    }

    public List<YvsBaseArticleDepot> getArticles_stock_accueil() {
        return articles_stock_accueil;
    }

    public void setArticles_stock_accueil(List<YvsBaseArticleDepot> articles_stock_accueil) {
        this.articles_stock_accueil = articles_stock_accueil;
    }

    @Override
    public void loadAll() {
        load(null);
    }

    // Faire la distinction entre les traitements d'un article ou d'un depot
    public void load(Boolean onArticle) {
        this.onArticle = onArticle;
        if (agence_ < 1) {
            agence_ = currentAgence.getId();
        }
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramCom", currentParam);
        }
        if (paramCommercial == null) {
            paramCommercial = currentParam;
        }
        if (mouvementStock.getDepot().getId() < 1) {
            mouvementStock.setDepot(UtilCom.buildSimpleBeanDepot(currentDepot));
        }
        if (mouvementStock.getTranche().getId() < 1) {
            if (currentPlanning != null ? !currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false : false) {
                mouvementStock.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
            }
        }
        paginator.addParam(new ParametreRequete("y.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.depot.actif", "depot_actif", depotActif, "=", "AND"));
        addParamSuivieStock();
    }

    public void loadAllArticle(YvsBaseDepots y) {
        if (suiviEnStock != null) {
            articlesDebut = dao.loadNameQueries("YvsBaseArticleDepot.findByDepotSuiviStock", new String[]{"depot", "suiviStock"}, new Object[]{y, suiviEnStock});
        } else {
            articlesDebut = dao.loadNameQueries("YvsBaseArticleDepot.findByDepot", new String[]{"depot"}, new Object[]{y});
        }
        update("select_article_debut");
    }

    public void loadAllArticle(YvsBaseArticles y) {
        articles_depot = dao.loadNameQueries("YvsBaseArticleDepot.findByArticle", new String[]{"article"}, new Object[]{y});
    }

    public void loadAllArticle(YvsBaseArticles y, YvsBaseDepots d) {
        articles_depot = dao.loadNameQueries("YvsBaseArticleDepot.findByArticleNoDepot", new String[]{"article", "depot"}, new Object[]{y, d});
    }

    private void loadAllTranche(YvsBaseDepots y) {
        mouvementStock.setTranche(new TrancheHoraire());
        tranches = loadTranche(y, mouvementStock.getDateDoc());
        update("select_tranche_stock_article");
    }

    public void loadArticle(YvsBaseDepots d, YvsBaseArticles a, boolean avance, boolean init) {
        articles_depot.clear();
        if (onArticle != null) {

            String fieldOrder;
            if (onArticle != null ? onArticle : false) {
                fieldOrder = "y.depot.agence.codeagence, y.depot.designation";
                p_article.addParam(new ParametreRequete("y.article", "article", (a == null) ? new YvsBaseArticles((long) -1) : a, "=", "AND"));
                p_article.addParam(new ParametreRequete("y.depot", "depot", null));
                controlListAgence();
                p_article.addParam(new ParametreRequete("y.depot.agence.id", "agences", listIdAgences, "IN", "AND"));
            } else {
                fieldOrder = "y.article.refArt";
                p_article.addParam(new ParametreRequete("y.depot", "depot", (d == null) ? new YvsBaseDepots(-1L) : d, "=", "AND"));
                p_article.addParam(new ParametreRequete("y.article", "article", null));
            }
            articles_depot = p_article.executeDynamicQuery("y", "y", "YvsBaseArticleDepot y JOIN FETCH y.article JOIN FETCH y.depot", fieldOrder, avance, init, (int) imax, dao);
            for (YvsBaseArticleDepot y : articles_depot) {
                if (y.getArticle().getConditionnements().size() == 1) {
                    y.setQuantiteStock(dao.stocks(y.getArticle().getId(), 0, y.getDepot().getId(), currentAgence.getId(), currentAgence.getSociete().getId(), new Date(), y.getArticle().getConditionnements().get(0).getId(), 0));
                } else {
                    y.setQuantiteStock(Double.NaN);
                }
            }
        }
        update("data_stock_article_depot");
    }

    public void onCalculerStock() {
        for (YvsBaseArticleDepot y : articles_depot) {
            onCalculerStock(y);
        }
    }

    public void onCalculerStock(YvsBaseArticleDepot y) {
        y.setQuantiteStock(dao.stocks(y.getArticle().getId(), 0, y.getDepot().getId(), currentAgence.getId(), currentAgence.getSociete().getId(), new Date(), 0, 0));
    }

    public void loadArticle_(YvsBaseDepots d, YvsAgences a, YvsBaseArticles art, boolean avance, boolean init) {
        articles_depot.clear();
        if (onArticle != null) {
            String fieldOrder;
//            fieldOrder = "y.depot.agence.designation";
            fieldOrder = "y.depot.agence.codeagence, y.depot.designation";
            p_article.addParam(new ParametreRequete("y.article", "article", (a == null) ? new YvsBaseArticles((long) -1) : art, "=", "AND"));
//            p_article.addParam(new ParametreRequete("y.depot", "depot", (d == null) ? new YvsBaseDepots(-1L) : d, "=", "AND"));
            if (a != null ? a.getId() > 0 : false) {
                p_article.addParam(new ParametreRequete("y.depot.agence", "agence", (a == null) ? new YvsAgences(-1L) : a, "=", "AND"));
            }

            articles_depot = p_article.executeDynamicQuery("y", "y", "YvsBaseArticleDepot y JOIN FETCH y.article JOIN FETCH y.depot JOIN FETCH y.depot.agence", fieldOrder, avance, init, (int) imax, dao);
            System.err.println("articles_depot =  " + articles_depot);
            for (YvsBaseArticleDepot y : articles_depot) {
                if (y.getArticle().getConditionnements().size() == 1) {
                    y.setQuantiteStock(dao.stocksReel(y.getArticle().getId(), 0, y.getDepot().getId(), 0, 0, new Date(), y.getArticle().getConditionnements().get(0).getId(), 0));
                } else {
                    y.setQuantiteStock(Double.NaN);
                }
            }

        }
        update("data_stock_article_depot");
        update("stockages:article_depot_table");
    }

    public void init(boolean next) {
        loadArticle(entityDepot, entityArticle, next, false);
    }

    public void searchArticleByRefArtActif() {
        ParametreRequete p = new ParametreRequete("y.article.refArt", "designation", null);
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "designation", numSearch + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "designation", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refArt", numSearch.toUpperCase() + "%", "LIKE", "OR"));
        }
        pa.addParam(p);
        loadActifArticleByDepot(false, false);
    }

    public void loadActifArticleByDepot(boolean avancer, boolean init) {
        if ((entityDepot != null) ? entityDepot.getId() > 0 : false) {
            pa.addParam(new ParametreRequete("y.depot", "depot", entityDepot, "=", "AND"));
            pa.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
            pa.addParam(new ParametreRequete("y.article.actif", "active", true, "=", "AND"));
            articlesResult = pa.executeDynamicQuery("y", "y", " YvsBaseArticleDepot y JOIN FETCH y.article JOIN FETCH y.depot JOIN FETCH y.depot.agence ", "y.article.refArt", avancer, init, (int) imax, dao);
        } else {
            getErrorMessage("Aucun dépôt n'a été trouvé !");
        }
    }

    //indique si l'article est dans le dépôt
    public String articleInDepot(YvsComContenuDocStock d, YvsBaseDepots dest) {
        if (d != null && dao != null) {
            YvsBaseArticleDepot re = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot",
                    new String[]{"article", "depot"}, new Object[]{d.getArticle(), dest});
            if (re != null) {
                return (re.getActif() ? null : "L'article n'est pas actif dans " + dest.getDesignation());
            } else {
                return " L'article n'existe pas dans " + dest.getDesignation();
            }
        }
        return null;
    }

    public void loadActifArticleByPoint(boolean avancer, boolean init) {
        if (entityPoint != null ? entityPoint.getId() > 0 : false) {
            String queryFrom = "YvsBaseArticleDepot y JOIN FETCH y.article JOIN FETCH y.depot.points LP JOIN FETCH LP.pointVente ";
            pv.addParam(new ParametreRequete("LP.pointVente", "point", entityPoint, " = ", "AND"));
            pv.addParam(new ParametreRequete("y.actif", "actifDep", true, "=", "AND"));
            pv.addParam(new ParametreRequete("y.article.actif", "actifArt", true, "=", "AND"));
            articles = pv.executeDynamicQuery("DISTINCT(y.article)", "DISTINCT(y.article)", queryFrom, "y.article.refArt", avancer, init, (int) imax, dao);
        } else {
            getErrorMessage("Vous devez preciser le point de vente");
        }
    }

    public void loadActifArticle(boolean avancer, boolean init) {
        String queryFrom = "YvsBaseArticles y JOIN FETCH y.famille ";
        pv.addParam(new ParametreRequete("y.actif", "actifArt", true, "=", "AND"));
        articles = pv.executeDynamicQuery("y", "y", queryFrom, "y.refArt", avancer, init, (int) imax, dao);
    }

    public YvsBaseArticleEmplacement buildArticleEmplacement(ArticleEmplacement y) {
        YvsBaseArticleEmplacement a = new YvsBaseArticleEmplacement();
        if (y != null) {
            a.setId(y.getId());
            a.setQuantite(y.getQuantite());
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                a.setArticle(new YvsBaseArticleDepot(y.getArticle().getId()));
            }
            if ((y.getEmplacement() != null) ? y.getEmplacement().getId() > 0 : false) {
                a.setEmplacement(new YvsBaseEmplacementDepot(y.getEmplacement().getId()));
            }
            a.setAuthor(currentUser);
        }
        return a;
    }

    @Override
    public MouvementStock recopieView() {
        MouvementStock m = new MouvementStock();
        m.setId(mouvementStock.getId());
        m.setDepot(mouvementStock.getDepot());
        m.setTranche(mouvementStock.getTranche());
        m.setDateDebut(mouvementStock.getDateDebut());
        m.setDateFin(mouvementStock.getDateFin());
        m.setDateDoc(mouvementStock.getDateDoc());
        return m;
    }

    public ArticleDepot recopieViewArticle() {
        ArticleDepot a = new ArticleDepot();
        a.setId(article.getId());
        a.setActif(article.isActif());
        a.setArticle(article.getArticle());
        a.setModeAppro(article.getModeAppro());
        a.setModeReappro(article.getModeReappro());
        a.setNameModeAppro(article.getNameModeAppro());
        a.setNameModeReappro(article.getNameModeReappro());
        a.setIntervalApprov(article.getIntervalApprov());
        a.setPr(article.getPr());
        a.setPuvMin(article.getPuvMin());
        a.setNaturePrixMin(article.getNaturePrixMin());
        a.setStockMax(article.getStockMax());
        a.setStockMin(article.getStockMin());
        a.setStockAlert(article.getStockAlert());
        a.setStockSecurite(article.getStockSecurite());
        a.setLotLivraison(article.getLotLivraison());
        a.setUniteInterval(article.getUniteInterval());
        a.setStockInitial(article.getStockInitial());
        a.setQuantiteStock(article.getQuantiteStock());
        a.setDateAppro((article.getDateAppro() != null) ? article.getDateAppro() : new Date());
        a.setActif(article.isActif());
        a.setUpdate(article.isUpdate());
        a.setChangePrix(article.isChangePrix());
        a.setGenererDocument(article.isGenererDocument());
        a.setTypeDocumentGenerer(article.getTypeDocumentGenerer());
        a.setSupp(false);
        a.setNew_(true);
        return a;
    }

    public ArticleDepot recopieViewArticle(Depots y) {
        ArticleDepot a = recopieViewArticle();
        a.setDepot(y);
        return a;
    }

    public ArticleDepot recopieViewArticle(Articles y) {
        ArticleDepot a = recopieViewArticle();
        a.setArticle(y);
        return a;
    }

    public ConditionnementDepot recopieViewConditionnement(ArticleDepot article) {
        ConditionnementDepot r = new ConditionnementDepot();
        if (conditionnement != null) {
            r.setArticle(article);
            r.setConditionnement((conditionnement.getConditionnement()));
            r.setId(conditionnement.getId());
            r.setOperation((conditionnement.getOperation()));
        }
        return r;
    }

    @Override
    public boolean controleFiche(MouvementStock bean) {
        if (bean.getDepot() != null ? bean.getDepot().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner le depot");
            return false;
        }
        if (!select) {
            if (idArtDeb < 1) {
                articleDebut = "A";
                articleFin = "Z";
            } else if (idArtFin < 1) {
                articleFin = articleDebut;
            }
        }
        return true;
    }

    public boolean controleFicheArticle(ArticleDepot bean) {
        if (bean.getDepot() != null ? bean.getDepot().getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer le depot");
            return false;
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if (bean.getStockMax() < bean.getStockMin()) {
            getErrorMessage("Le stock maximal ne peut pas etre inferieur au stock minimal");
            return false;
        }
        champ = new String[]{"article", "depot"};
        val = new Object[]{new YvsBaseArticles(bean.getArticle().getId()), new YvsBaseDepots(bean.getDepot().getId())};
        nameQueri = "YvsBaseArticleDepot.findOneByArticleDepot";
        YvsBaseArticleDepot a = (YvsBaseArticleDepot) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (a != null ? (a.getId() > 0 ? !a.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja associé cet element!");
            return false;
        }
        if (bean.getId() > 0 && a != null ? a.getId() > 0 : false) {
            if (a.getRequiereLot() != bean.isRequiereLot()) {
                if (!autoriser("change_requiere_lot_article_depot")) {
                    if (bean.isRequiereLot()) {
                        double stock = dao.stocks(bean.getArticle().getId(), 0, bean.getDepot().getId(), 0, 0, new Date(), 0, 0);
                        if (stock > 0) {
                            getErrorMessage("Vous ne pouvez pas activer la gestion du lot car il y'a du stock sans lot dans ce dépot");
                            return false;
                        }
                    } else {
                        ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                        if (w != null) {
                            List<YvsComLotReception> list = w.loadList(bean.getDepot().getId(), bean.getArticle().getId(), 0, new Date(), 0, 0);
                            if (list != null ? !list.isEmpty() : false) {
                                getErrorMessage("Vous ne pouvez pas désactiver la gestion du lot car il y'a du stock avec lot dans ce dépot");
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean controleFiche(ConditionnementDepot bean) {
        if (bean.getOperation() != null ? bean.getOperation().getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer l'operation");
            return false;
        }
        if (bean.getConditionnement() != null ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer le condionnement");
            return false;
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        champ = new String[]{"article", "operation", "unite"};
        val = new Object[]{new YvsBaseArticleDepot(bean.getArticle().getId()), new YvsBaseDepotOperation(bean.getOperation().getId()), new YvsBaseConditionnement(bean.getConditionnement().getId())};
        nameQueri = "YvsBaseConditionnementDepot.findOne";
        YvsBaseConditionnementDepot a = (YvsBaseConditionnementDepot) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (a != null ? (a.getId() > 0 ? !a.getId().equals(bean.getId()) : false) : false) {
            getErrorMessage("Vous avez deja associé cet element!");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(MouvementStock bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void populateViewArticle(ArticleDepot bean) {
        cloneObject(article, bean);

        nameQueri = "YvsBaseArticleEmplacement.findByArticle";
        champ = new String[]{"article"};
        val = new Object[]{new YvsBaseArticleDepot(bean.getId())};
        List<YvsBaseArticleEmplacement> la = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsBaseArticleEmplacement a : la) {
            article.setEmplacements(article.getEmplacements() + a.getEmplacement().getDesignation() + ";");
        }
    }

    public void populateView(ConditionnementDepot bean) {
        cloneObject(conditionnement, bean);
    }

    @Override
    public void resetFiche() {
        mouvementStock.setDepot(new Depots());
        resetFiche_();
    }

    public void resetFiche_() {
        idArtDeb = 0;
        idArtFin = 0;

        articleDebut = "";
        articleFin = "";

        mouvementStock.setArticle(new Articles());
        mouvementStock.setTranche(new TrancheHoraire());

        articlesDebut.clear();
        articlesFin.clear();
        articles_stock.clear();
        tranches.clear();

        select = false;
        update("select_article_debut");
        update("select_article_fin");
        update("data_stock_article");
        update("select_tranche_stock_article");
        update("select_depot_stock_article");
    }

    public void resetFicheArticle() {
        article = new ArticleDepot();
        article.setModeReappro(Constantes.REAPPRO_PERIODE);
        article.setUniteInterval(Constantes.UNITE_JOUR);
        article.setNaturePrixMin(Constantes.NATURE_MTANT);
        article.setConditionnement(new Conditionnement());
        listArt = false;
        selectArt = false;
    }

    public void resetFicheConditionnement() {
        conditionnement = new ConditionnementDepot();
    }

    public void loadArticleStock(boolean avance, boolean init) {
        loadArticleStock(avance, init, true);
    }

    public void calculePrFromArtcleStock() {
        if (articles_stock != null) {
            if (!displayPrixRevient) {
                for (YvsBaseArticleDepot a : articles_stock) {
                    a.setPrixRevient(findLastPr(a.getArticle().getId(), a.getDepot().getId(), dateSearch, a.getConditionnement().getId()));
                    String query = "SELECT AVG(COALESCE(cout_entree, 0)) FROM yvs_base_mouvement_stock WHERE conditionnement = ? AND depot = ? AND date_doc <= ?";
                    Double prixEntree = (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(a.getConditionnement().getId(), 1), new Options(a.getDepot().getId(), 2), new Options(dateSearch, 3)});
                    a.setPrixEntree(prixEntree != null ? prixEntree : 0);
                }
            }
            setDisplayPrixRevient(!displayPrixRevient);
            update("data_stock_article");
        }
    }

    public void calculeResteALivreFromArtcleStock() {
        if (articles_stock != null) {
            if (!displayResteALivrer) {
                for (YvsBaseArticleDepot a : articles_stock) {
                    a.setResteALivrer(dao.getResteALivrer(a.getArticle().getId(), a.getDepot().getId(), dateSearch, a.getConditionnement().getId()));
                }
            }
            setDisplayResteALivrer(!displayResteALivrer);
            update("data_stock_article");
        }
    }

    public void calculeAvgPuvFromArtcleStock() {
        if (articles_stock != null) {
            if (!displayAvgPuv) {
                Double sr;
                for (YvsBaseArticleDepot a : articles_stock) {
                    sr = (Double) dao.loadOneByNameQueries("YvsBaseMouvementStock.findPrixByArticle", new String[]{"unite", "date"}, new Object[]{a.getConditionnement(), dateSearch});
                    a.getArticle().setPuv(sr != null ? sr : 0);
                }
            }
            setDisplayAvgPuv(!displayAvgPuv);
            update("data_stock_article");
        }
    }

    public void loadArticleStock(boolean avance, boolean init, boolean all) {
        articles_stock.clear();
        totaux = 0;
        if (!paginator.getParams().isEmpty()) {
            controlListAgence();
            paginator.addParam(new ParametreRequete("y.depot.agence.id", "agence", listIdAgences, "IN", "AND"));
            List<YvsBaseArticleDepot> list = paginator.executeDynamicQuery("YvsBaseArticleDepot", "y.article.designation, y.article.refArt", avance, init, (int) imax, dao);
            YvsBaseArticleDepot y;
            for (YvsBaseArticleDepot a : list) {
                a.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{a.getArticle()}));
                for (YvsBaseConditionnement c : a.getArticle().getConditionnements()) {
                    y = new YvsBaseArticleDepot(a);
                    y.setDepot(a.getDepot());
                    y.setConditionnement(c);
                    y.setStockInitial(dao.stocks(a.getArticle().getId(), trancheSearch, a.getDepot().getId(), 0, 0, dateSearch, c.getId(), 0));
                    y.setStock(y.getStockInitial());
                    boolean insert = false;
                    if (stock_) {
                        switch (getSoldeSearch()) {
                            case "P":
                                if (y.getStockInitial() > 0) {
                                    insert = true;
                                }
                                break;
                            case "N":
                                if (y.getStockInitial() < 0) {
                                    insert = true;
                                }
                                break;
                            default:
                                if (y.getStockInitial() != 0) {
                                    insert = true;
                                }
                                break;
                        }
                    } else {
                        insert = true;
                    }
                    if (insert) {
                        if (withLot != null ? a.getRequiereLot() : false) {
                            List<YvsComLotReception> lots = new ArrayList<>();
                            ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                            if (w != null) {
                                lots = w.loadList(a.getDepot().getId(), c.getId(), dateSearch, 0, 0);
                            }
                            double stock = y.getStockInitial();
                            if (withLot) {
                                stock = 0;
                                for (YvsComLotReception l : lots) {
                                    stock += l.getStock();
                                }
                            } else {
                                for (YvsComLotReception l : lots) {
                                    stock -= l.getStock();
                                }
                            }
                            if (all ? stock < 1 : false) {
                                continue;
                            }
                            y.setLots(new ArrayList<>(lots));
                        }
                        totaux += y.getPrixRevient() * y.getStockInitial();
                        articles_stock.add(y);
                    }
                }
            }
        } else {
            getInfoMessage("Vous devez ajouter un paramètre de recherche");
        }
        update("data_stock_article");
    }

    public void loadArticleStockForHome(Long article) {
        depotActif = true;
        paginator.clear();
        paginator.addParam(new ParametreRequete("y.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.depot.actif", "depot_actif", depotActif, "=", "AND"));
        loadArticleStock(article);
    }

    public void loadArticleStock(Long article) {
        articles_stock_accueil.clear();
        totaux = 0;
        paginator.addParam(new ParametreRequete("y.article", "refLike", new YvsBaseArticles(article), "=", "AND"));
        if (!paginator.getParams().isEmpty()) {
            controlListAgence();
            paginator.addParam(new ParametreRequete("y.depot.agence.id", "agence", listIdAgences, "IN", "AND"));
            List<YvsBaseArticleDepot> list = paginator.executeDynamicQuery("YvsBaseArticleDepot", "y.article.designation, y.article.refArt", true, true, (int) imax, dao);
            Double sr;
            YvsBaseArticleDepot y;
            String query = "SELECT AVG(COALESCE(cout_entree, 0)) FROM yvs_base_mouvement_stock WHERE conditionnement = ? AND depot = ? AND date_doc <= ?";
            for (YvsBaseArticleDepot a : list) {
                a.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{a.getArticle()}));
                for (YvsBaseConditionnement c : a.getArticle().getConditionnements()) {
                    y = new YvsBaseArticleDepot(a);
                    y.setDepot(a.getDepot());
                    y.setConditionnement(c);
                    y.setStockInitial(dao.stocks(a.getArticle().getId(), trancheSearch, a.getDepot().getId(), 0, 0, dateSearch, c.getId(), 0));
                    y.setPrixRevient(findLastPr(a.getArticle().getId(), a.getDepot().getId(), dateSearch, c.getId()));
                    Double prixEntree = (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getId(), 1), new Options(a.getDepot().getId(), 2), new Options(dateSearch, 3)});
                    y.setPrixEntree(prixEntree != null ? prixEntree : 0);
                    //stock reservé à une date
                    y.setResteALivrer(dao.getResteALivrer(a.getArticle().getId(), a.getDepot().getId(), dateSearch, c.getId()));
                    //calcule le stock reservé
                    sr = (Double) dao.loadOneByNameQueries("YvsComReservationStock.findQuantite", new String[]{"article", "depot", "date", "statut", "unite"}, new Object[]{a.getArticle(), a.getDepot(), dateSearch, Constantes.ETAT_VALIDE, c});
                    y.setQuantiteReserve(sr != null ? sr : 0);
                    //calcule la moyenne des couts d'entree
                    sr = (Double) dao.loadOneByNameQueries("YvsBaseMouvementStock.findPrixByArticle", new String[]{"unite", "date"}, new Object[]{c, dateSearch});
                    y.getArticle().setPuv(sr != null ? sr : 0);
                    boolean insert = false;
                    if (stock_) {
                        switch (getSoldeSearch()) {
                            case "P":
                                if (y.getStockInitial() > 0) {
                                    insert = true;
                                }
                                break;
                            case "N":
                                if (y.getStockInitial() < 0) {
                                    insert = true;
                                }
                                break;
                            default:
                                if (y.getStockInitial() != 0) {
                                    insert = true;
                                }
                                break;
                        }
                    } else {
                        insert = true;
                    }
                    if (insert) {
                        totaux += y.getPrixRevient() * y.getStockInitial();
                        articles_stock_accueil.add(y);
                    }
                }
            }
        } else {
            getInfoMessage("Vous devez ajouter un paramètre de recherche");
        }
        update("data_stock_article");
    }

    public void loadArticleStock() {
        try {
            inventaire.returnInventaire(currentAgence.getSociete().getId(), agence_, 0, famSearch, catSearch, grpSearch, dateSearch, !stock_, soldeSearch, typeUnite, articlesSearch, 0, 0, false, true, dao);
            update("data_stock_article_multi");
        } catch (Exception ex) {
            getException("loadArticleStock", ex);
        }
    }

    public void loadArticleStockOLD(boolean avance, boolean init) {
        articles_stock.clear();
        if (stock_) {
            paginator.addParam(new ParametreRequete("y.depot", "depot", null, "=", "AND"));
            List<Long> ids;
            if (select) {
                String _ids = null;
                for (YvsBaseArticleDepot a : articles_depot) {
                    if (_ids == null || _ids.trim().equals("")) {
                        _ids = "" + a.getId();
                    } else {
                        _ids += "," + a.getId();
                    }
                }
                String req = "SELECT y.id FROM yvs_base_article_depot y WHERE ((select get_stock(y.article, y.depot, ?))!=0) and y.id::character varying in (select val from regexp_split_to_table(?,',') val)";
                ids = dao.loadListBySqlQuery(req, new Options[]{new Options(mouvementStock.getDateDoc(), 1), new Options(_ids, 2)});
            } else {
                String req = "select y.id from yvs_base_article_depot y inner join yvs_base_articles a on a.id = y.article where ((select get_stock(y.article, y.depot, ?))!=0)"
                        + " and UPPER(a.designation) between UPPER(?) and UPPER(?) and depot = ?";
                Options[] params = new Options[]{new Options(mouvementStock.getDateDoc(), 1), new Options(articleDebut, 2), new Options(articleFin, 3), new Options(mouvementStock.getDepot().getId(), 4)};
                if (!Util.asString(articleDebut) && !Util.asString(articleFin)) {
                    req = "select y.id from yvs_base_article_depot y inner join yvs_base_articles a on a.id = y.article where ((select get_stock(y.article, y.depot, ?))!=0)"
                            + " and depot = ?";
                    params = new Options[]{new Options(mouvementStock.getDateDoc(), 1), new Options(mouvementStock.getDepot().getId(), 2)};
                } else if (!Util.asString(articleDebut) && Util.asString(articleFin)) {
                    req = "select y.id from yvs_base_article_depot y inner join yvs_base_articles a on a.id = y.article where ((select get_stock(y.article, y.depot, ?))!=0)"
                            + " and UPPER(a.designation) <= UPPER(?) and depot = ?";
                    params = new Options[]{new Options(mouvementStock.getDateDoc(), 1), new Options(articleFin, 2), new Options(mouvementStock.getDepot().getId(), 3)};
                } else if (Util.asString(articleDebut) && !Util.asString(articleFin)) {
                    req = "select y.id from yvs_base_article_depot y inner join yvs_base_articles a on a.id = y.article where ((select get_stock(y.article, y.depot, ?))!=0)"
                            + " and UPPER(a.designation) => UPPER(?) and depot = ?";
                    params = new Options[]{new Options(mouvementStock.getDateDoc(), 1), new Options(articleDebut, 2), new Options(mouvementStock.getDepot().getId(), 3)};
                }
                if (suiviEnStock) {
                    req += " AND y.suivi_stock IS TRUE";
                }
                ids = dao.loadListBySqlQuery(req, params);
            }
            if (ids != null ? !ids.isEmpty() : false) {
                paginator.addParam(new ParametreRequete("y.id", "ids", ids, " IN ", "AND"));
            } else {
                paginator.getParams().clear();
            }
        } else {
            if (select) {
                List<Long> ids = new ArrayList<>();
                for (YvsBaseArticleDepot a : articles_depot) {
                    ids.add(a.getId());
                }
                if (ids != null ? !ids.isEmpty() : false) {
                    paginator.addParam(new ParametreRequete("y.id", "ids", ids, " IN ", "AND"));
                } else {
                    paginator.getParams().clear();
                }
            } else {
                paginator.addParam(new ParametreRequete("y.id", "ids", null, " IN ", "AND"));
                paginator.addParam(new ParametreRequete("y.depot", "depot", new YvsBaseDepots(mouvementStock.getDepot().getId()), "=", "AND"));
                paginator.addParam(new ParametreRequete("UPPER(y.article.designation)", "designation", articleDebut.toUpperCase(), articleFin.toUpperCase(), " BETWEEN ", "AND"));
            }
        }
        if (!paginator.getParams().isEmpty()) {
            articles_stock.clear();
            List<YvsBaseArticleDepot> list = paginator.executeDynamicQuery("YvsBaseArticleDepot", "y.article.designation, y.article.refArt", avance, init, (int) imax, dao);
            Double sr;
            YvsBaseArticleDepot y;
            for (YvsBaseArticleDepot a : list) {
                for (YvsBaseConditionnement c : a.getArticle().getConditionnements()) {
                    y = new YvsBaseArticleDepot(a);
                    y.setConditionnement(c);
                    y.setStockInitial(dao.stocks(a.getArticle().getId(), mouvementStock.getTranche().getId(), mouvementStock.getDepot().getId(), 0, 0, mouvementStock.getDateDoc(), c.getId(), mouvementStock.getLot().getId()));

                    //stock reservé à une date
                    y.setResteALivrer(dao.getResteALivrer(a.getArticle().getId(), mouvementStock.getDepot().getId(), mouvementStock.getDateDebut(), c.getId()));
                    //calcule le stock reservé
                    sr = (Double) dao.loadOneByNameQueries("YvsComReservationStock.findQuantite", new String[]{"article", "depot", "date", "statut", "unite"}, new Object[]{a.getArticle(), a.getDepot(), mouvementStock.getDateDebut(), Constantes.ETAT_VALIDE, c});
                    y.setQuantiteReserve(sr != null ? sr : 0);
                    //calcule la moyenne des couts d'entree
                    sr = (Double) dao.loadOneByNameQueries("YvsBaseMouvementStock.findPrixByArticle", new String[]{"unite", "date"}, new Object[]{c, mouvementStock.getDateDoc()});
                    y.getArticle().setPuv(sr != null ? sr : 0);
                    if (stock_) {
                        switch (getSoldeSearch()) {
                            case "P":
                                if (y.getStockInitial() > 0) {
                                    articles_stock.add(y);
                                }
                                break;
                            case "N":
                                if (y.getStockInitial() < 0) {
                                    articles_stock.add(y);
                                }
                                break;
                            default:
                                if (y.getStockInitial() != 0) {
                                    articles_stock.add(y);
                                }
                                break;
                        }
                    } else {
                        articles_stock.add(y);
                    }
                }
            }
        }
        update("data_stock_article");
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadArticleStock(true, true);
    }

    public void choosePaginator_(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadArticleStock(true, false);
    }

    public void calculerStock() {
        calculerStock(false);
    }

    public void calculerStock(boolean multi) {
        calculerStock(multi, true);
    }

    public void calculerStock(boolean multi, boolean all) {
        imax = 0;
        this.multi = multi;
        if (multi) {
            loadArticleStock();
        } else {
            loadArticleStock(true, true, all);
        }
        update("blog_plus_option_stock_article");
    }

    @Override
    public boolean saveNew() {
        MouvementStock bean = recopieView();
        if (controleFiche(bean)) {
            loadArticleStock(true, true);
        }
        return false;
    }

    public boolean saveNewByList() {
        select = false;
        if (articles_depot != null ? !articles_depot.isEmpty() : false) {
            select = true;
        }
        return saveNew();
    }

    public void saveNewArticle() {
        String action = article.isUpdate() ? "Modification" : "Insertion";
        try {
            if (!autoriser("base_article_add_depot")) {
                openNotAcces();
                return;
            }
            if (onArticle != null ? onArticle : false) {
                article.setArticle(UtilProd.buildSimpleBeanArticles(entityArticle));
            } else {
                article.setDepot(UtilCom.buildBeanDepot(entityDepot));
            }
            if (controleFicheArticle(article)) {
                if (article.getDepotPr() != null ? article.getDepotPr().getId() < 1 : true) {
                    cloneObject(article.getDepotPr(), article.getDepot());
                }
                YvsBaseArticleDepot entity = UtilCom.buildArticleDepot(article, currentUser);
                if (article.getId() < 1) {
                    entity.setId(null);
                    entity = (YvsBaseArticleDepot) dao.save1(entity);
                    article.setId(entity.getId());
                    articles_depot.add(0, entity);
                } else {
                    dao.update(entity);
                    if (articles_depot.contains(entity)) {
                        articles_depot.set(articles_depot.indexOf(entity), entity);
                    }
                }
                succes();
                resetFicheArticle();
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void saveNewArticleEmplacement() {
        try {
            if ((article != null) ? article.getId() > 0 : false) {
                ArticleEmplacement a = new ArticleEmplacement();
                a.setEmplacement(emplacement);
                a.setArticle(new ArticleDepot(a.getId()));
                a.setSelectActif(true);
                YvsBaseArticleEmplacement y = buildArticleEmplacement(a);
                y.setId(null);
                y = (YvsBaseArticleEmplacement) dao.save1(y);
                a.setId(y.getId());
                emplacementsArticle.get(emplacementsArticle.indexOf(new YvsBaseEmplacementDepot(emplacement.getId()))).setSelectActif(true);
//                article.setEmplacements(article.getEmplacements() + emplArticle.getDesignation() + ";");
                succes();
            } else {
                getErrorMessage("Vous devez selectionner l'article!");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void saveNewConditionnement() {
        try {
            ConditionnementDepot bean = recopieViewConditionnement(article);
            if (controleFiche(bean)) {
                YvsBaseConditionnementDepot y = UtilCom.buildConditionnement(bean, currentUser);
                if (y != null ? y.getId() > 0 : false) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsBaseConditionnementDepot) dao.save1(y);
                }
                int idx = article.getConditionnements_().indexOf(y);
                if (idx > -1) {
                    article.getConditionnements_().set(idx, y);
                } else {
                    article.getConditionnements_().add(0, y);
                }
                resetFicheConditionnement();
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteBeanArticle() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsBaseArticleDepot bean = articles_depot.get(articles_depot.indexOf(new YvsBaseArticleDepot(id)));
                    dao.delete(new YvsBaseArticleDepot(id));
                    articles_depot.remove(bean);
                    if (article.getId() == id) {
                        resetFicheArticle();
                    }
                }
                succes();
                update("tabview_depot:data_article_depot ");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanArticle_(YvsBaseArticleDepot y) {
        if (!autoriser("base_article_add_depot")) {
            openNotAcces();
            return;
        }
        selectArticle = y;
        if (needConfirmation) {
            deleteBeanArticle_();
        } else {
            openDialog("dlgConfirmDeleteArticle_");
        }
    }

    public void deleteBeanArticle_() {
        try {
            if (!autoriser("base_article_add_depot")) {
                openNotAcces();
                return;
            }
            if (selectArticle != null) {
                dao.delete(selectArticle);
                articles_depot.remove(selectArticle);
                succes();
                if (article.getId() == selectArticle.getId()) {
                    resetFicheArticle();
                }
                update("tabview_depot:data_article_depot ");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanConditionnement(YvsBaseConditionnementDepot y) {
        try {
            if (y != null) {
                dao.delete(y);
                article.getConditionnements_().remove(y);
                if (conditionnement.getId() == y.getId()) {
                    resetFicheConditionnement();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteAllBeanArticle() {
        try {
            if (articlesSelect != null ? !articlesSelect.isEmpty() : false) {
                for (YvsBaseArticleDepot ad : articlesSelect) {
                    ad.setAuthor(currentUser);
                    dao.delete(ad);
                    if (articles_depot.contains(ad)) {
                        articles_depot.remove(ad);
                    }
                }
                articlesSelect.clear();
                succes();
            } else {
                getErrorMessage("Vous devez selectionner des éléments");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void applyDepotPr_(YvsBaseArticleDepot ad) {
        applyDepotPr_(ad, true);
    }

    public void applyDepotPr_(YvsBaseArticleDepot ad, boolean msg) {
        applyDepotPr_(ad, true, msg);
    }

    public void applyDepotPr_(YvsBaseArticleDepot ad, boolean principal, boolean msg) {
        try {
            if (ad != null) {
                String query = "UPDATE yvs_base_article_depot ad SET default_pr=false FROM yvs_base_depots d WHERE article=? AND d.agence=? AND ad.depot=d.id";
                dao.requeteLibre(query, new yvs.dao.Options[]{new yvs.dao.Options(ad.getArticle().getId(), 1), new yvs.dao.Options(ad.getDepot().getAgence().getId(), 2)});
                for (YvsBaseArticleDepot item : articles_depot) {
                    if (item.getArticle().equals(ad.getArticle()) && item.getDepot().getAgence().equals(ad.getDepot().getAgence())) {
                        item.setDefaultPr(false);
                    }
                }
                if (principal) {
                    query = "UPDATE yvs_base_article_depot SET default_pr=true,date_update=?,author=? WHERE article=? AND depot=?";
                    dao.requeteLibre(query, new yvs.dao.Options[]{new yvs.dao.Options(new Date(), 1), new yvs.dao.Options(currentUser.getId(), 2), new yvs.dao.Options(ad.getArticle().getId(), 3), new yvs.dao.Options(ad.getDepot().getId(), 4)});
                    ad.setDefaultPr(true);
                }
                int index = articles_depot.indexOf(ad);
                if (index > -1) {
                    articles_depot.set(index, ad);
                }
            }

        } catch (NumberFormatException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        if (msg) {
            succes();
        }
    }

    public void definedDepotPrPrincipal() {
        definedDepotPrPrincipal(true);
    }

    public void definedDepotPrPrincipal(boolean principal) {
        try {
            if (articlesSelect != null ? !articlesSelect.isEmpty() : false) {
                for (YvsBaseArticleDepot ad : articlesSelect) {
                    applyDepotPr_(ad, principal, false);
                }
                succes();
            } else {
                getErrorMessage("Vous devez selectionner les lignes à modifier");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void toogleActiveAllBeanArticle(boolean suivi) {
        try {
            if (articlesSelect != null ? !articlesSelect.isEmpty() : false) {
                String query = "UPDATE yvs_base_article_depot SET actif=?, author=?, date_update=? WHERE id IN (";
                int i = 0;
                for (YvsBaseArticleDepot ad : articlesSelect) {
                    if (i < articlesSelect.size() - 1) {
                        query += "'" + ad.getId() + "',";
                    } else {
                        query += "'" + ad.getId() + "')";
                    }
                    ad.setAuthor(currentUser);
                    ad.setActif(!ad.getActif());
//                    dao.update(ad);
                    if (articles_depot.contains(ad)) {
                        articles_depot.set(articles_depot.indexOf(ad), ad);
                    }
                    i++;
                }
                dao.requeteLibre(query, new Options[]{new Options(suivi, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3)});
            } else {
                getErrorMessage("Vous devez selectionner les lignes à modifier");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void toogleActiveAllSuiviAllArticle(boolean suivi) {
        try {
            if (articlesSelect != null ? !articlesSelect.isEmpty() : false) {
                String query = "UPDATE yvs_base_article_depot SET suivi_stock=?, author=?, date_update=? WHERE id IN (";
                int i = 0;
                for (YvsBaseArticleDepot ad : articlesSelect) {
                    if (i < articlesSelect.size() - 1) {
                        query += "'" + ad.getId() + "',";
                    } else {
                        query += "'" + ad.getId() + "')";
                    }
                    ad.setAuthor(currentUser);
                    ad.setSuiviStock(suivi);
//                    dao.update(ad);
                    if (articles_depot.contains(ad)) {
                        articles_depot.set(articles_depot.indexOf(ad), ad);
                    }
                    i++;
                }
                dao.requeteLibre(query, new Options[]{new Options(suivi, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3)});
            } else {
                getErrorMessage("Vous devez selectionner les lignes à modifier");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void toogleActiveAllRequireLot(boolean suivi) {
        try {
            if (articlesSelect != null ? !articlesSelect.isEmpty() : false) {
                String query = "UPDATE yvs_base_article_depot SET requiere_lot=?, author=?, date_update=? WHERE id IN (";
                int i = 0;
                for (YvsBaseArticleDepot ad : articlesSelect) {
                    if (i < articlesSelect.size() - 1) {
                        query += "'" + ad.getId() + "',";
                    } else {
                        query += "'" + ad.getId() + "')";
                    }
                    ad.setAuthor(currentUser);
                    ad.setRequiereLot(suivi);
//                    dao.update(ad);
                    if (articles_depot.contains(ad)) {
                        articles_depot.set(articles_depot.indexOf(ad), ad);
                    }
                    i++;
                }
                dao.requeteLibre(query, new Options[]{new Options(suivi, 1), new Options(currentUser.getId(), 2), new Options(new Date(), 3)});
            } else {
                getErrorMessage("Vous devez selectionner les lignes à modifier");
            }
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteArticleEmplacement() {
        try {
            try {
                dao.delete(new YvsBaseArticleEmplacement(emplacement.getIdExterne()));
                emplacementsArticle.get(emplacementsArticle.indexOf(new YvsBaseEmplacementDepot(emplacement.getId()))).setSelectActif(false);
                String[] t = article.getEmplacements().split(";");
                article.setEmplacements("");
                for (String s : t) {
                    if (!s.equals(emplacement.getDesignation())) {
                        article.setEmplacements(article.getEmplacements() + s + ";");
                    }
                }
                succes();
            } catch (Exception ex) {
                getErrorMessage("Insertion Impossible !");
                log.log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {

    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleDepot bean = (YvsBaseArticleDepot) ev.getObject();
            bean.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{bean.getArticle()}));
            populateViewArticle(UtilCom.buildBeanArticleDepot(bean));
        }
    }

    public void loadOnViewConditionnement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseConditionnementDepot bean = (YvsBaseConditionnementDepot) ev.getObject();
            populateView(UtilCom.buildBeanConditionnement(bean));
            update("form_conditionnement_depot");
        }
    }

    public void unLoadOnViewArticle(UnselectEvent ev) {
        resetFicheArticle();
    }

    public void unLoadOnViewConditionnement(UnselectEvent ev) {
        resetFicheConditionnement();
        update("form_conditionnement_depot");
    }

    public void loadOnViewOneArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(bean);
        }
    }

    public void chooseDepotDefautPr() {
        try {
            if (article.getDepot() != null ? article.getDepot().getId() > 0 : false) {
                YvsBaseDepots depot = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{article.getDepot().getId()});
                if (depot.getAgence() != null ? depot.getAgence().getId() > 0 : false) {
                    YvsAgences agence = new YvsAgences(depot.getAgence().getId());
                    YvsBaseDepots dep = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByPr", new String[]{"agence"}, new Object[]{agence});
                    if (dep != null ? dep.getId() > 0 : false) {
                        ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                        if (m != null) {
                            int index = m.getDepots().indexOf(dep);
                            if (index > -1) {
                                Depots bean = UtilCom.buildBeanDepot(dep);
                                article.setDepotPr(bean);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseDepotArticle() {
        if ((article.getDepot() != null) ? article.getDepot().getId() > 0 : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                int idx = m.getDepots().indexOf(new YvsBaseDepots(article.getDepot().getId()));
                if (idx > -1) {
                    YvsBaseDepots y = m.getDepots().get(idx);
                    Depots bean = UtilCom.buildBeanDepot(y);
                    cloneObject(article.getDepot(), bean);
                    if (entityArticle != null ? article.getId() < 1 : false) {
                        if (entityArticle.getConditionnements() != null ? !entityArticle.getConditionnements().isEmpty() : false) {
                            article.setConditionnement(new Conditionnement(entityArticle.getConditionnements().get(0).getId()));
                        }
                        article.setRequiereLot(y.getRequiereLot() && (entityArticle != null ? entityArticle.getRequiereLot() : false));
                    }
                }
            }
            if (article.getId() < 1) {
                cloneObject(article.getDepotPr(), article.getDepot());
            }
        }
    }

    public void chooseDepot() {
        resetFiche_();
        if ((mouvementStock.getDepot() != null) ? mouvementStock.getDepot().getId() > 0 : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                if (m.getDepots_all().contains(new YvsBaseDepots(mouvementStock.getDepot().getId()))) {
                    YvsBaseDepots y = m.getDepots_all().get(m.getDepots_all().indexOf(new YvsBaseDepots(mouvementStock.getDepot().getId())));
                    Depots bean = UtilCom.buildBeanDepot(y);
                    cloneObject(mouvementStock.getDepot(), bean);
                    loadAllArticle(y);
                    loadAllTranche(y);
                }
            }
        }
        update("data_stock_article");
        update("data_stock_articles_art_depot");
        update("select_tranche_stock_article");
    }

    public void chooseTranche() {
        if ((mouvementStock.getTranche() != null) ? mouvementStock.getTranche().getId() > 0 : false) {
            YvsGrhTrancheHoraire y = tranches.get(tranches.indexOf(new YvsGrhTrancheHoraire(mouvementStock.getTranche().getId())));
            TrancheHoraire bean = UtilCom.buildBeanTrancheHoraire(y);
            cloneObject(mouvementStock.getTranche(), bean);
        }
    }

    public YvsBaseArticles chooseArticle(YvsBaseArticles y) {
        if ((y != null) ? y.getId() > 0 : false) {
            // on recherche les conditionnements actif 
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{y});
            y.setConditionnements(unites);
            cloneObject(article.getArticle(), UtilProd.buildBeanArticles(y));
            if (!article.isUpdate()) {
                article.setPr(y.getPuv());
                article.setPuvMin(y.getPrixMin());
                article.setChangePrix(y.getChangePrix());
                article.setActif(y.getActif());
            }
            selectArt = true;
            listArt = false;
            return y;
        }
        return null;
    }

    public void chooseModeReApprovisionnement() {
        if ((article.getModeReappro() != null)) {
            switch (article.getModeReappro()) {
                case Constantes.REAPPRO_SEUIL:
                    article.setNameModeReappro("Seuil Alert");
                    article.setIntervalApprov(0);
                    break;
                case Constantes.REAPPRO_PERIODE:
                    article.setNameModeReappro("Delai Approvisionnement");
                    break;
                case Constantes.REAPPRO_RUPTURE:
                    article.setNameModeReappro("Rupture Stock");
                    article.setIntervalApprov(0);
                    break;
                default:
                    break;
            }
        }
    }

    public void chooseModeApprovisionnement() {
        if ((article.getModeAppro() != null)) {
            switch (article.getModeAppro()) {
                case Constantes.APPRO_ACHTON:
                    article.setNameModeAppro("Achat Uniquement");
                    break;
                case Constantes.APPRO_ENON:
                    article.setNameModeAppro("Entrée Uniquement");
                    break;
                case Constantes.APPRO_PRODON:
                    article.setNameModeAppro("Production Uniquement");
                    break;
                case Constantes.APPRO_ACHT_PROD:
                    article.setNameModeAppro("Achat - Production");
                    break;
                case Constantes.APPRO_ACHT_EN:
                    article.setNameModeAppro("Achat - Entrée");
                    break;
                case Constantes.APPRO_PROD_EN:
                    article.setNameModeAppro("Production - Entrée");
                    break;
                case Constantes.APPRO_ACHT_PROD_EN:
                    article.setNameModeAppro("Achat - Production - Entrée");
                    break;
                default:
                    break;
            }
        }
    }

    public void chooseArticleDebut() {
        select = false;
        articlesFin.clear();
        if (idArtDeb > 0) {
            YvsBaseArticleDepot a = articlesDebut.get(articlesDebut.indexOf(new YvsBaseArticleDepot(idArtDeb)));
            if (a != null ? a.getId() > 0 : false) {
                articleDebut = a.getArticle().getDesignation();
                for (YvsBaseArticleDepot d : articlesDebut) {
                    if (d.getArticle().getDesignation().compareToIgnoreCase(articleDebut) > 0) {
                        articlesFin.add(d);
                    }
                }
            }
        }
        update("select_article_fin");
    }

    public void chooseArticleFin() {
        if (idArtFin > 0) {
            YvsBaseArticleDepot a = articlesFin.get(articlesFin.indexOf(new YvsBaseArticleDepot(idArtFin)));
            if (a != null ? a.getId() > 0 : false) {
                articleFin = a.getArticle().getDesignation();
            }
        }
    }

    public void chooseOperation(Depots depot) {
        if (conditionnement.getOperation() != null) {
            int idx = depot.getOperations().indexOf(new YvsBaseDepotOperation(conditionnement.getOperation().getId()));
            if (idx > -1) {
                YvsBaseDepotOperation y = depot.getOperations().get(idx);
                conditionnement.setOperation(UtilCom.buildBeanDepotOperation(y));
            }
        }
    }

    public void chooseConditionnement() {
        if (conditionnement.getConditionnement() != null) {
            int idx = article.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(conditionnement.getConditionnement().getId()));
            if (idx > -1) {
                YvsBaseConditionnement y = article.getArticle().getConditionnements().get(idx);
                conditionnement.setConditionnement(UtilProd.buildBeanConditionnement(y));
            }
        }
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadArticle(entityDepot, entityArticle, true, true);
    }

    // Choosepaginator pour les articles actifs
    public void _choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadActifArticleByPoint(true, true);
    }

    public void choosePaginatorActif(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadActifArticle(true, true);
    }

    public void choosePaginatorAchat(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadActifArticleByDepot(true, true);
    }

    public void openArticle() {
        if (mouvementStock.getDepot() != null ? mouvementStock.getDepot().getId() > 0 : false) {
            openDialog("dlgListArticles");
            update("data_stock_articles_art_depot");
        } else {
            getErrorMessage("Vous devez selectionner le dépot");
        }
    }

    public void selectAllArticle() {
        if (articlesDebut != null ? !articlesDebut.isEmpty() : false) {
            if (articles_depot.isEmpty()) {
                articles_depot.addAll(articlesDebut);
            } else {
                if (articles_depot.size() == articlesDebut.size()) {
                    articles_depot.clear();
                } else {
                    articles_depot.clear();
                    articles_depot.addAll(articlesDebut);
                }
            }
            update("data_stock_articles_art_depot");
        }
    }

    public void loadArticlesSelect() {
        articlesSearch = "";
        articles_depot.clear();
        ManagedArticles w = (ManagedArticles) giveManagedBean("Marticle");
        if (w != null) {
            List<Long> ids = new ArrayList<>();
            for (YvsBaseArticles a : w.getSelectArticles()) {
                articles_depot.add(new YvsBaseArticleDepot((long) (articles_depot.size() + 1), a));
                ids.add(a.getId());
                articlesSearch += (articlesSearch.equals("") ? "" : ",") + a.getId();
            }
            if (!ids.isEmpty()) {
                artSearch = "";
                paginator.addParam(new ParametreRequete("y.article.refArt", "refLike", null, "IN", "AND"));
                paginator.addParam(new ParametreRequete("y.article.id", "articles", ids, "IN", "AND"));
            } else {
                paginator.addParam(new ParametreRequete("y.article.id", "articles", null, "IN", "AND"));
            }
            update("label-nombre_article_select");
        }
    }

    public void clearParams() {
        paginator.getParams().clear();
        resetFiche();
        paginator.addParam(new ParametreRequete("y.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.depot.actif", "depot_actif", depotActif, "=", "AND"));
        addParamSuivieStock();
        artSearch = "";
        catSearch = "";
        famSearch = 0;
        grpSearch = 0;
        depotSearch = 0;
        trancheSearch = 0;
        update("blog_plus_option_stock_article");
    }

    public void addParamCategorieArt() {
        ParametreRequete p = new ParametreRequete("y.article.categorie", "categorie", null, "=", "AND");
        if (catSearch != null ? catSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.article.categorie", "categorie", catSearch, "=", "AND");
        }
        paginator.addParam(p);
    }

    public void addParamFamilleArt() {
        ParametreRequete p = new ParametreRequete("y.article.famille", "famille", null, "=", "AND");
        if (famSearch > 0) {
            p = new ParametreRequete("y.article.famille", "famille", new YvsBaseFamilleArticle(famSearch), "=", "AND");
        }
        paginator.addParam(p);
    }

    public void addParamGroupeArt() {
        ParametreRequete p = new ParametreRequete("y.article.groupe", "groupe", null, "=", "AND");
        if (famSearch > 0) {
            p = new ParametreRequete("y.article.groupe", "groupe", new YvsBaseGroupesArticle(grpSearch), "=", "AND");
        }
        paginator.addParam(p);
    }

    public void addParamActifArt() {
        paginator.addParam(new ParametreRequete("y.article.actif", "actif", actSearch, "=", "AND"));
    }

    public void addParamActifDepot() {
        paginator.addParam(new ParametreRequete("y.depot.actif", "depot_actif", depotActif, "=", "AND"));
    }

    public void addParamArticle() {
        articlesSearch = "";
        ParametreRequete p = new ParametreRequete("UPPER(y.article.refArt)", "refLike", null);
        if (artSearch != null ? artSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "refLike", artSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refLike", artSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "refLike", artSearch.toUpperCase() + "%", "LIKE", "OR"));
            YvsBaseArticles y = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findByCode", new String[]{"code", "societe"}, new Object[]{artSearch, currentAgence.getSociete()});
            if (y != null ? y.getId() > 0 : false) {
                articlesSearch = y.getId() + "";
            }
        }
        paginator.addParam(p);
    }

    public void addParamDepot() {
        ParametreRequete p = new ParametreRequete("y.depot", "depot", null);
        if (depotSearch > 0) {
            p = new ParametreRequete("y.depot", "depot", new YvsBaseDepots(depotSearch), "=", "AND");
        }
        paginator.addParam(p);
    }

    public void addParamSuivieStock() {
        paginator.addParam(new ParametreRequete("COALESCE(y.suiviStock, false)", "suiviStock", suiviEnStock, "=", "AND"));
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void changeSelectEmplacement(YvsBaseEmplacementDepot y) {
        Emplacement bean = UtilCom.buildBeanEmplacement(y);
        cloneObject(emplacement, bean);
        nameQueri = "YvsBaseArticleEmplacement.findByArticleEmplacement";
        champ = new String[]{"article", "emplacement"};
        val = new Object[]{new YvsBaseArticleDepot(article.getId()), y};
        List<YvsBaseArticleEmplacement> la = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        if (bean.isSelectActif()) {
            if (la != null ? !la.isEmpty() : false) {
                emplacement.setIdExterne(la.get(0).getId());
                openDialog("dlgConfirmDeleteArticleEmplacement");
            }
        } else {
            if (la != null ? la.isEmpty() : true) {
                saveNewArticleEmplacement();
            }
        }
    }

    public void activeSellWithoutStock(YvsBaseArticleDepot bean) {
        if (bean != null) {
            bean.setSellWithoutStock(!bean.getSellWithoutStock());
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);
            dao.update(bean);
            articles_depot.set(articles_depot.indexOf(bean), bean);
            succes();
        }
    }

    public void activeSuiviEnStock(YvsBaseArticleDepot bean) {
        if (bean != null) {
            bean.setSuiviStock(!bean.getSuiviStock());
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);
            dao.update(bean);
            articles_depot.set(articles_depot.indexOf(bean), bean);
            succes();
        }
    }

    public void activeRequiredLot(YvsBaseArticleDepot bean) {
        activeRequiredLot(bean, true);
    }

    public void activeRequiredLot(YvsBaseArticleDepot bean, boolean force) {
        if (bean != null) {
            if (!autoriser("change_requiere_lot_article_depot")) {
                if (!bean.getRequiereLot()) {
                    double stock = dao.stocks(bean.getArticle().getId(), 0, bean.getDepot().getId(), 0, 0, new Date(), 0, 0);
                    if (stock > 0) {
                        getErrorMessage("Vous ne pouvez pas activer la gestion du lot car il y'a du stock sans lot dans ce dépot");
                        return;
                    }
                } else {
                    ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                    if (w != null) {
                        List<YvsComLotReception> list = w.loadList(bean.getDepot().getId(), bean.getArticle().getId(), 0, new Date(), 0, 0);
                        if (list != null ? !list.isEmpty() : false) {
                            getErrorMessage("Vous ne pouvez pas désactiver la gestion du lot car il y'a du stock avec lot dans ce dépot");
                            return;
                        }
                    }
                }
            } else if (!force) {
                if (!bean.getRequiereLot()) {
                    double stock = dao.stocks(bean.getArticle().getId(), 0, bean.getDepot().getId(), 0, 0, new Date(), 0, 0);
                    if (stock > 0) {
                        selectArticle = bean;
                        openDialog("dlgConfirmChangeRequiereLot");
                        return;
                    }
                } else {
                    ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                    if (w != null) {
                        List<YvsComLotReception> list = w.loadList(bean.getDepot().getId(), bean.getArticle().getId(), 0, new Date(), 0, 0);
                        if (list != null ? !list.isEmpty() : false) {
                            selectArticle = bean;
                            openDialog("dlgConfirmChangeRequiereLot");
                            return;
                        }
                    }
                }
            }
            bean.setRequiereLot(!bean.getRequiereLot());
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);
            dao.update(bean);
            articles_depot.set(articles_depot.indexOf(bean), bean);
            succes();
        }
    }

    public void activeActicle(YvsBaseArticleDepot bean) {
        if (bean != null) {
            if (!bean.getActif() && !bean.getArticle().getActif()) {
                getErrorMessage("Cet article est inactif");
                return;
            }
            bean.setActif(!bean.getActif());
            bean.setDateUpdate(new Date());
            bean.setAuthor(currentUser);
            dao.update(bean);
            articles_depot.set(articles_depot.indexOf(bean), bean);
            succes();
        }
    }

    public void initEmplacement() {
        if ((article != null) ? article.getId() > 0 : false) {
            for (YvsBaseEmplacementDepot e : emplacementsArticle) {
                nameQueri = "YvsBaseArticleEmplacement.findByArticleEmplacement";
                champ = new String[]{"article", "emplacement"};
                val = new Object[]{new YvsBaseArticleDepot(article.getId()), e};
                List<YvsBaseArticleEmplacement> la = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
                e.setSelectActif(la != null ? !la.isEmpty() : false);
            }
        } else {
            getWarningMessage("Vous devez dabord selectionner un article!");
        }
        update("data_emplacement_article");
    }

    public void insertAllArticle(YvsBaseDepots yd, YvsBaseArticles ya) {
        try {
            if (!autoriser("base_article_add_depot")) {
                openNotAcces();
                return;
            }
            boolean art = ya != null ? ya.getId() > 0 : false;
            boolean dep = !art ? yd != null ? yd.getId() > 0 : false : false;
            if (art || dep) {
                boolean bien = false;
                champ = new String[]{"societe"};
                val = new Object[]{currentAgence.getSociete()};
                if (onArticle != null ? onArticle : false) {
                    if (ya != null ? ya.getId() > 0 : false) {
                        List<YvsBaseDepots> list = dao.loadNameQueries("YvsBaseDepots.findAllActif", champ, val);
                        Long count = 0L;
                        for (YvsBaseDepots a : list) {
                            count = (Long) dao.loadObjectByNameQueries("YvsBaseArticleDepot.counByArticleDepot", new String[]{"depot", "article"}, new Object[]{a, ya});
                            if (count != null ? count < 1 : true) {
                                YvsBaseArticleDepot y = new YvsBaseArticleDepot();
                                y.setDepot(a);
                                y.setDepotPr(a);
                                y.setArticle(ya);
                                y.setAuthor(currentUser);
                                y.setActif(a.getActif());
                                y.setSupp(false);
                                y.setDateSave(new Date());
                                y.setDateUpdate(new Date());
                                y.setRequiereLot(a.getRequiereLot() && ya.getRequiereLot());
                                y.setNew_(true);
                                y.setSuiviStock(ya.getSuiviEnStock());
                                y = (YvsBaseArticleDepot) dao.save1(y);
                                articles_depot.add(y);
                                bien = true;
                            }
                        }
                    }
                } else {
                    if (yd != null ? yd.getId() > 0 : false) {
                        PaginatorResult<YvsBaseArticles> p_articles = new PaginatorResult<>();
                        p_articles.addParam(new ParametreRequete("y.famille.societe", "societe", currentAgence.getSociete(), "=", "AND"));
                        p_articles.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
                        if (famille > 0) {
                            p_articles.addParam(new ParametreRequete("y.famille", "famille", new YvsBaseFamilleArticle(famille), "=", "AND"));
                        }
                        if (Util.asString(categorie)) {
                            p_articles.addParam(new ParametreRequete("y.categorie", "categorie", categorie, "=", "AND"));
                        }
                        List<YvsBaseArticles> list = p_articles.executeDynamicQuery("YvsBaseArticles", "y.refArt", true, true, 0, dao);
                        Long count = 0L;
                        for (YvsBaseArticles a : list) {
                            count = (Long) dao.loadObjectByNameQueries("YvsBaseArticleDepot.counByArticleDepot", new String[]{"depot", "article"}, new Object[]{yd, a});
                            if (count != null ? count < 1 : true) {
                                YvsBaseArticleDepot y = new YvsBaseArticleDepot();
                                y.setDepot(yd);
                                y.setDepotPr(yd);
                                y.setArticle(a);
                                y.setAuthor(currentUser);
                                y.setActif(a.getActif());
                                y.setSuiviStock(a.getSuiviEnStock());
                                y.setDateSave(new Date());
                                y.setDateUpdate(new Date());
                                y.setNew_(true);
                                y.setRequiereLot(suiviParLot);

                                y = (YvsBaseArticleDepot) dao.save1(y);
                                articles_depot.add(y);
                                bien = true;
                            }
                        }
                    }
                }
                if (bien) {
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Initialisation Impossible!");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void searchArticleByRef() {
        ParametreRequete p;
        p = new ParametreRequete("y.depot.designation", "reference", null);
        if (numSearch != null ? numSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch + "%", "LIKE", "AND");
            if (onArticle != null ? onArticle : false) {
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.depot.designation)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.depot.abbreviation)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            } else {
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "reference", numSearch.toUpperCase() + "%", "LIKE", "OR"));
            }
        }
        p_article.addParam(p);
        loadArticle(entityDepot, entityArticle, true, true);
    }

    public void searchArticleByAgence() {
        ParametreRequete p;
        p = new ParametreRequete("y.depot.agence.designation", "reference", null);
        if (agence != null ? agence.getId() > 0 : false) {
            agence = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{agence.getId()});
            p = new ParametreRequete(null, "reference", agence.getDesignation() + "%", "LIKE", "AND");
            if (onArticle != null ? onArticle : false) {
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.depot.agence.designation)", "reference", agence.getDesignation().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.depot.agence.abbreviation)", "reference", agence.getDesignation().toUpperCase() + "%", "LIKE", "OR"));
            } else {
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "reference", agence.getDesignation().toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "reference", agence.getDesignation().toUpperCase() + "%", "LIKE", "OR"));
            }
            p_article.addParam(p);
            update("article_depot_table");
            loadArticle_(entityDepot, agence, entityArticle, true, true);
        } else {
            articles_depot = dao.loadNameQueries("YvsBaseArticleDepot.findByArticle", new String[]{"article"}, new Object[]{entityArticle});

            for (YvsBaseArticleDepot y : articles_depot) {
                if (y.getArticle().getConditionnements().size() == 1) {
                    y.setQuantiteStock(dao.stocksReel(y.getArticle().getId(), 0, y.getDepot().getId(), 0, 0, new Date(), y.getArticle().getConditionnements().get(0).getId(), 0));
                } else {
                    y.setQuantiteStock(Double.NaN);
                }
            }

        }

    }

    public void addParamActif(ValueChangeEvent ev) {
        p_article.addParam(new ParametreRequete("y.actif", "actif", (Boolean) ev.getNewValue(), "=", "AND"));
        loadArticle(entityDepot, entityArticle, true, true);
    }

    public void searchArticleByCategorie() {
        ParametreRequete p = new ParametreRequete("y.article.categorie", "categorie", null, "=", "AND");
        if (categorieSearch != null ? categorieSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.article.categorie", "categorie", categorieSearch, "=", "AND");
        }
        p_article.addParam(p);
        loadArticle(entityDepot, entityArticle, true, true);
    }

    public void searchArticleByFamille() {
        ParametreRequete p = new ParametreRequete("y.article.famille", "famille", null, "=", "AND");
        if (familleSearch != null ? familleSearch > 0 : false) {
            p.setObjet(new YvsBaseFamilleArticle(familleSearch));
        }
        p_article.addParam(p);
        loadArticle(entityDepot, entityArticle, true, true);
    }

    public void searchArticle() {
        String num = article.getArticle().getRefArt();
        article.getArticle().setDesignation("");
        article.getArticle().setError(true);
        article.getArticle().setId(0);
        listArt = false;

        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            Articles y = m.searchArticleActif(null, num, false);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data_stock_articles_depot");
                } else {
                    article.setArticle(y);
                    if (!article.isUpdate()) {
                        article.setPr(y.getPuv());
                        article.setPuvMin(y.getPuvMin());
                        article.setChangePrix(y.isChangePrix());
                        article.setActif(y.isActif());
                    }
                    if (y.getConditionnements() != null ? !y.getConditionnements().isEmpty() : false) {
                        article.setConditionnement(new Conditionnement(y.getConditionnements().get(0).getId()));
                    }
                    if (m.getArticlesResult().size() == 1) {
                        initDepotPr(m.getArticlesResult().get(0));
                    }
                    article.setRequiereLot(y.isRequiereLot() && (entityDepot != null ? entityDepot.getRequiereLot() : false));
                }
                article.getArticle().setError(false);
            }
            listArt = y.isListArt();
        }
    }

    public Articles searchArticleActifByDepot(boolean open) {
        return searchArticleActifByDepot(article.getArticle().getRefArt(), open);
    }

    public ArticleDepot _searchArticleActifByDepot(String num, boolean open) {
        return _searchArticleActifByDepot(num, (entityDepot != null ? entityDepot.getId() : 0), open);
    }

    public Articles searchArticleActifByDepot(String num, boolean open) {
        return searchArticleActifByDepot(num, (entityDepot != null ? entityDepot.getId() : 0), open);
    }

    public ArticleDepot _searchArticleActifByDepot(String num, long depot, boolean open) {
        ArticleDepot a = new ArticleDepot();
        a.setRefArt(num);
        a.setError(true);
        if (depot > 0) {
            if (num != null ? num.trim().length() > 0 : false) {
                ParametreRequete p = new ParametreRequete(null, "refArt", num + "%", " LIKE ", "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "designation", num.toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refArt", num.toUpperCase() + "%", "LIKE", "OR"));
                pa.addParam(p);
                setEntityDepot(new YvsBaseDepots(depot));
                loadActifArticleByDepot(true, true);
                Articles s = chechArticleResult(open, false);
                if (s != null ? s.getId() < 1 : true) {
                    a.setRefArt(num);
                    a.setError(true);
                } else {
                    YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", new String[]{"article", "depot"}, new Object[]{new YvsBaseArticles(s.getId()), new YvsBaseDepots(depot)});
                    a = UtilCom.buildBeanArticleDepot(y);
                    a.setSelectArt(s.isSelectArt());
                    a.setListArt(s.isListArt());
                }
            }
        } else {
            getErrorMessage("Vous devez precisez le depot");
        }
        return a;
    }

    public Articles searchArticleActifByDepot(String num, long depot, boolean open) {
        Articles a = new Articles();
        a.setRefArt(num);
        a.setError(true);
        if (depot > 0) {
            if (num != null ? num.trim().length() > 0 : false) {
                ParametreRequete p = new ParametreRequete(null, "refArt", num + "%", " LIKE ", "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "designation", num.toUpperCase() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refArt", num.toUpperCase() + "%", "LIKE", "OR"));
                pa.addParam(p);
                setEntityDepot(new YvsBaseDepots(depot));
                loadActifArticleByDepot(true, true);
                a = chechArticleResult(open, false);
                if (a != null ? a.getId() < 1 : true) {
                    a.setRefArt(num);
                    a.setError(true);
                }
            }
        } else {
            getErrorMessage("Vous devez precisez le depot");
        }
        return a;
    }

    public Articles searchArticleActifByPoint(boolean open) {
        return searchArticleActifByPoint(article.getArticle().getRefArt(), open);
    }

    public Articles searchArticleActif(boolean open) {
        return searchArticleActif(article.getArticle().getRefArt(), open);
    }

    public Articles searchArticleActifByPoint(String num, boolean open) {
        return searchArticleActifByPoint(num, (entityPoint != null ? entityPoint.getId() : 0), open);
    }

    public Articles searchArticleActif(String num, boolean open) {
        Articles a = new Articles();
        a.setRefArt(num);
        a.setError(true);
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete(null, "refArt", num + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.designation)", "designation", num.toUpperCase(), "LIKE", " OR "));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.refArt)", "refArt", num.toUpperCase(), "LIKE", " OR "));
            pv.addParam(p);
            loadActifArticle(true, true);
            a = chechArticleResult(open, true);
            if (a != null ? a.getId() < 1 : true) {
                a.setRefArt(num);
                a.setError(true);
            }
        }
        return a;
    }

    public Articles searchArticleActifByPoint(String num, long point, boolean open) {
        Articles a = new Articles();
        a.setRefArt(num);
        a.setError(true);
        if (point > 0) {
            if (num != null ? num.trim().length() > 0 : false) {
                ParametreRequete p = new ParametreRequete(null, "refArt", num + "%", "LIKE", "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "designation", num.toUpperCase(), "LIKE", " OR "));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refArt", num.toUpperCase(), "LIKE", " OR "));
                pv.addParam(p);
                setEntityPoint(new YvsBasePointVente(point));
                loadActifArticleByPoint(true, true);
                a = chechArticleResult(open, true);
                if (a != null ? a.getId() < 1 : true) {
                    a.setRefArt(num);
                    a.setError(true);
                }
            }
        } else {
            getErrorMessage("Vous devez precisez le point de vente");
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
                    result.getArticle().setListArt(true);
                    result.getArticle().setSelectArt(true);
                }
            }
        }
        return result;
    }

    private Articles chechArticleResult(boolean open, boolean art) {
        Articles a = new Articles();
        ManagedArticles service = (ManagedArticles) giveManagedBean("Marticle");
        if (!art) {
            if (articlesResult != null ? !articlesResult.isEmpty() : false) {
                if (articlesResult.size() > 1) {
                    if (open) {
                        openDialog("dlgListArticle");
                    }
                    a.setListArt(true);
                } else {
                    if (service != null) {
                        YvsBaseArticleDepot d = articlesResult.get(0);
                        a = service.chechArticleResult(d.getArticle());
                        a.setRequiereLot(d.getRequiereLot());
                        a.setSellWithOutStock(d.getSellWithoutStock());
                        a.setSelectArt(true);
                    }
                }
                a.setError(false);
            }
        } else {
            if (articles != null ? !articles.isEmpty() : false) {
                if (articles.size() > 1) {
                    if (open) {
                        openDialog("dlgListArticle");
                    }
                    a.setListArt(true);
                } else {
                    YvsBaseArticles c = articles.get(0);
                    a = service.chechArticleResult(c);
//                    if (entityDepot != null ? entityDepot.getId() > 0 : false) {
//                        if (c.getYvsBaseArticleDepotList() != null ? !c.getYvsBaseArticleDepotList().isEmpty() : false) {
//                            for (YvsBaseArticleDepot d : c.getYvsBaseArticleDepotList()) {
//                                if (d.getDepot().equals(entityDepot)) {
//                                    a.setRequiereLot(d.getRequiereLot());
//                                    a.setSellWithOutStock(d.getSellWithoutStock());
//                                    break;
//                                }
//                            }
//                        }
//                    }
                    a.setSelectArt(true);
                }
                a.setError(false);
            }
        }
        return a;
    }

    public void initArticlesByDepot(ArticleDepot a) {
        if (a == null) {
            a = new ArticleDepot();
        }
        pa.addParam(new ParametreRequete("y.article.refArt", "refArt", null));
        loadActifArticleByDepot(true, true);
        a.setSelectArt(false);
        a.setListArt(true);
    }

    public void initArticlesByDepot(Articles a) {
        if (a == null) {
            a = new Articles();
        }
        pa.addParam(new ParametreRequete("y.article.refArt", "refArt", null));
        loadActifArticleByDepot(true, true);
        a.setSelectArt(false);
        a.setListArt(true);
    }

    public void initArticlesByPoint(Articles a) {
        if (a == null) {
            a = new Articles();
        }
        pv.addParam(new ParametreRequete("y.article.refArt", "refArt", null));
        loadActifArticleByPoint(true, true);
        a.setSelectArt(false);
        a.setListArt(true);
    }

    public void initArticles() {
        listArt = false;
        ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
        if (m != null) {
            m.initArticles(null, article.getArticle());
            listArt = article.getArticle().isListArt();
        }
        update("data_stock_articles_commission");
    }

    public void initConditionnement(YvsBaseArticleDepot y) {
        if (y != null ? y.getId() > 0 : false) {
            y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findById", new String[]{"id"}, new Object[]{y.getId()});
            article = UtilCom.buildBeanArticleDepot(y);
            update("blog_conditionnement_depot");
        }
    }

    public void print() {
        print(multi);
    }

    public void print(boolean multi) {
        if (depotSearch < 1 && !multi) {
            getErrorMessage("Vous devez selectionner un dépot");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", !multi ? 0 : (int) agence_);
        param.put("DEPOT", (int) depotSearch);
        param.put("FAMILLE", (int) famSearch);
        param.put("WITH_CHILD", withChildSearch);
        param.put("ARTICLES", articlesSearch);
        param.put("TYPE", typeUnite);
        param.put("DATE", dateSearch);
        param.put("PRINT_ALL", !stock_);
        param.put("gescom_stock_view_totaux", autoriser("gescom_stock_view_totaux"));
        param.put("SOLDE_PRINT", getSoldeSearch());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport(!multi ? "stock_article" : "stock_article_multi", param);
    }

    public void printByFormat(String format) {
        print(format, multi);
    }

    public void print(String format, boolean multi) {
        if (depotSearch < 1 && !multi) {
            getErrorMessage("Vous devez selectionner un dépot");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", !multi ? 0 : (int) agence_);
        param.put("DEPOT", (int) depotSearch);
        param.put("FAMILLE", (int) famSearch);
        param.put("WITH_CHILD", withChildSearch);
        param.put("ARTICLES", articlesSearch);
        param.put("TYPE", typeUnite);
        param.put("DATE", dateSearch);
        param.put("PRINT_ALL", !stock_);
        param.put("gescom_stock_view_totaux", autoriser("gescom_stock_view_totaux"));
        param.put("SOLDE_PRINT", getSoldeSearch());
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        executeReport(!multi ? "stock_article_no_header" : "stock_article_no_header_multi", param, "", format, false);
    }

    public void printInventaire() {
        if (dateSearch == null) {
            getErrorMessage("Vous devez précisez la date de fin");
            return;
        }
        if (depotSearch < 1) {
            getErrorMessage("Vous devez indiquer le depot");
            return;
        }
        ManagedInventaire w = (ManagedInventaire) giveManagedBean(ManagedInventaire.class);
        if (w != null) {
            w.printInventaire(depotSearch, emplacementSearch, famSearch, catSearch, grpSearch, typeUnite, dateSearch, !stock_, getSoldeSearch(), withChildSearch, orderBy);
        }
    }

    public void printInventairePreparatoire() {
        if (dateSearch == null) {
            getErrorMessage("Vous devez précisez la date de fin");
            return;
        }
        if (depotSearch < 1) {
            getErrorMessage("Vous devez indiquer le depot");
            return;
        }
        ManagedInventaire w = (ManagedInventaire) giveManagedBean(ManagedInventaire.class);
        if (w != null) {
            w.printInventairePreparatoire(depotSearch, emplacementSearch, famSearch, catSearch, grpSearch, typeUnite, dateSearch, !stock_, "", withChildSearch, orderBy);
        }
    }

    public void openListLot(YvsBaseArticleDepot ad) {
        this.stock = stock;
        ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
        if (w != null) {
            lots = w.loadList(ad.getDepot().getId(), ad.getConditionnement().getId(), dateSearch, ad.getStockInitial(), ad.getStockInitial());
        }
        update("data-stock_require_lot");
    }

    public void openToGenerated(YvsBaseArticleDepot ad) {
        listArticle.clear();
        if (ad != null) {
            listArticle.add(ad);
        } else {
            listArticle.addAll(articles_depot);
        }
        openDialog("dlgGenererEntree");
    }

    public void genererFicheEntree() {
        try {
            if (!autoriser("gescom_stock_generer_entree")) {
                openNotAcces();
                return;
            }
            if (depotSearch < 1) {
                getErrorMessage("Vous devez entrer un depot");
                return;
            }
            if (articles_stock != null ? articles_stock.isEmpty() : true) {
                getErrorMessage("La liste des articles à ajuster est vide");
                return;
            }
            String numero = genererReference(Constantes.TYPE_ES_NAME, dateEntree, depotSearch);
            if (numero == null || numero.trim().equals("")) {
                return;
            }
            boolean with_reste_a_livre = Util.asString(typeEntreeOn) ? typeEntreeOn.equals("R") : true;
            boolean with_stock_negatif = Util.asString(typeEntreeOn) ? typeEntreeOn.equals("N") : true;
            List<YvsComContenuDocStock> list = new ArrayList<>();
            YvsComContenuDocStock contenu;
            for (YvsBaseArticleDepot y : articles_stock) {
                if ((with_reste_a_livre && y.getResteALivrer() > 0) || (with_stock_negatif && y.getStockInitial() < 0)) {
                    contenu = new YvsComContenuDocStock(null);
                    double quantite = 0;
                    if (with_reste_a_livre) {
                        quantite = y.getResteALivrer();
                    }
                    if (with_stock_negatif) {
                        quantite += -(y.getStockInitial());
                    }
                    contenu.setActif(true);
                    contenu.setArticle(y.getArticle());
                    contenu.setAuthor(currentUser);
                    contenu.setCalculPr(true);
                    contenu.setCommentaire("");
                    contenu.setConditionnement(y.getConditionnement());
                    contenu.setConditionnementEntree(y.getConditionnement());
                    contenu.setDateContenu(new Date());
                    contenu.setDateReception(dateEntree);
                    contenu.setPrix(y.getPrixRevient());
                    contenu.setPrixEntree(y.getPrixRevient());
                    contenu.setPrixTotal(quantite * y.getPrixRevient());
                    contenu.setQteAttente(quantite);
                    contenu.setQteRestant(quantite);
                    contenu.setQuantite(quantite);
                    contenu.setQuantiteEntree(quantite);
                    contenu.setStatut(statutEntree);
                    contenu.setSupp(false);

                    list.add(contenu);
                }
            }

            if (list.isEmpty()) {
                getErrorMessage("Aucun article n'a été trouvé");
                return;
            }

            YvsComDocStocks docStock = new YvsComDocStocks();
            docStock.setActif(true);
            docStock.setAuthor(currentUser);
            docStock.setCloturer(false);
            docStock.setCreneauDestinataire(currentCreneauDepot);
            docStock.setCreneauSource(currentCreneauDepot);
            docStock.setDateDoc(dateEntree);
            docStock.setDateReception(dateEntree);
            docStock.setDateValider(new Date());
            docStock.setDestination(new YvsBaseDepots(depotSearch));
            docStock.setSource(new YvsBaseDepots(depotSearch));
            docStock.setEditeur(currentUser.getUsers());
            docStock.setEtapeTotal(0);
            docStock.setEtapeValide(0);
            docStock.setHeureDoc(new Date());
            docStock.setMouvement(Constantes.MOUV_ENTREE);
            docStock.setNature(Constantes.OP_AJUSTEMENT_STOCK);
            docStock.setNumDoc(numero);
            docStock.setNumPiece(numero);
            docStock.setSociete(currentAgence.getSociete());
            docStock.setStatut(statutEntree);
            docStock.setTypeDoc(Constantes.TYPE_ES);
            docStock.setValiderBy(currentUser.getUsers());

            docStock = (YvsComDocStocks) dao.save1(docStock);
            if (docStock != null ? docStock.getId() > 0 : false) {
                for (YvsComContenuDocStock c : list) {
                    c.setDocStock(docStock);
                    dao.save(c);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedStockArticle (genererFicheEntree)", ex);
        }
    }

    public void changeDepotPr() {
        try {
            if (depotPr != null ? depotPr.getId() > 0 : false) {
                if (articlesSelect != null ? !articlesSelect.isEmpty() : false) {
                    for (YvsBaseArticleDepot y : articlesSelect) {
                        y.setDepotPr(new YvsBaseDepots(depotPr.getId(), depotPr.getDesignation()));
                        y.setDateUpdate(new Date());
                        y.setAuthor(currentUser);
                        dao.update(y);
                        int idx = articles_depot.indexOf(y);
                        if (idx > -1) {
                            articles_depot.set(idx, y);
                        }
                    }
                    succes();
                } else {
                    getWarningMessage("Vous devez selectionner au moins 1 article");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedStockArticle (changeDepotPr)", ex);
        }
    }

    public void changeModeAppro() {
        try {
            if (modeAppro != null) {
                if (articlesSelect != null ? !articlesSelect.isEmpty() : false) {
                    for (YvsBaseArticleDepot y : articlesSelect) {
                        y.setModeAppro(modeAppro);
                        y.setDateUpdate(new Date());
                        y.setAuthor(currentUser);
                        dao.update(y);
                        int idx = articles_depot.indexOf(y);
                        if (idx > -1) {
                            articles_depot.set(idx, y);
                        }
                    }
                    succes();
                } else {
                    getWarningMessage("Vous devez selectionner au moins 1 article");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedStockArticle (changeModeAppro)", ex);
        }
    }

    public void chooseDepotPr() {
        if ((depotPr != null) ? depotPr.getId() > 0 : false) {
            ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            int idx = w.getDepots_all().indexOf(new YvsBaseDepots(depotPr.getId()));
            if (idx > -1) {
                YvsBaseDepots y = w.getDepots_all().get(idx);
                depotPr = UtilCom.buildBeanDepot(y);
            }
        }
    }

    private Date periodeDebut = new Date(), periodeFin = new Date();
    private String listExclude;
    private String methode;
    private SeuilStocks selectedSeuil;
    private List<SeuilStocks> listeSeuil = new ArrayList<>();
    private List<SeuilStocks> detailDay = new ArrayList<>();
    private List<YvsBaseMouvementStock> detailStocks = new ArrayList<>();

    public Date getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(Date periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public Date getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(Date periodeFin) {
        this.periodeFin = periodeFin;
    }

    public String getListExclude() {
        return listExclude;
    }

    public void setListExclude(String listExclude) {
        this.listExclude = listExclude;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public List<SeuilStocks> getListeSeuil() {
        return listeSeuil;
    }

    public void setListeSeuil(List<SeuilStocks> listeSeuil) {
        this.listeSeuil = listeSeuil;
    }

    public List<SeuilStocks> getDetailDay() {
        return detailDay;
    }

    public void setDetailDay(List<SeuilStocks> detailDay) {
        this.detailDay = detailDay;
    }

    public List<YvsBaseMouvementStock> getDetailStocks() {
        return detailStocks;
    }

    public void setDetailStocks(List<YvsBaseMouvementStock> detailStocks) {
        this.detailStocks = detailStocks;
    }

    public SeuilStocks getSelectedSeuil() {
        return selectedSeuil;
    }

    public void setSelectedSeuil(SeuilStocks selectedSeuil) {
        this.selectedSeuil = selectedSeuil;
    }

    long depot = 0;

    public void loadSeuilsStocks() {
        String articles = "";
        listExclude = "";
        for (YvsBaseArticleDepot ad : articlesSelect) {
            if (depot <= 0) {
                depot = ad.getDepot().getId();
            }
            articles += ad.getArticle().getId() + ",";
        }
        listeSeuil = loadSeuilsStocks(articles, depot);
        if (!listeSeuil.isEmpty()) {
            openDialog("dlgCalculSeuils");
            update("liste_seuil");
        }
    }

    public void loadSeuilsStocksOne() {
        if (selectedSeuil != null) {
            List<SeuilStocks> l = loadSeuilsStocks(selectedSeuil.getId().toString(), depot);
            System.err.println(" Exclude :" + listExclude);
            if (l != null ? !l.isEmpty() : false) {
                int idx = listeSeuil.indexOf(l.get(0));
                if (idx >= 0) {
                    listeSeuil.set(idx, l.get(0));
                    update("liste_seuil");
                    succes();
                }
            }
        }
    }

    private List<SeuilStocks> loadSeuilsStocks(String articles, long depot) {
        List<SeuilStocks> result = new ArrayList<>();
        if (articles != null ? !articles.isEmpty() : false) {
            String query = "SELECT id, ref_art, designation, besoin_moy, delai_moy, besoin_max, delai_max, stock_sec, point_cmde, "
                    + "coefficient, ecart_type, categorie,conditionnement, unite FROM base_maj_seuil_stocks(?,?,?,?,?,?)";
            List<Object[]> re = dao.loadListBySqlQuery(query, new Options[]{new Options(articles, 1), new Options(depot, 2),
                new Options(periodeDebut, 3),
                new Options(periodeFin, 4), new Options(listExclude, 5), new Options("2", 6)});
            SeuilStocks ob;
            for (Object[] line : re) {
                ob = new SeuilStocks((Long) line[0], (String) line[1], (String) line[2]);
                ob.setBesoinMoy((Double) line[3]);
                ob.setDelaiMoy((Double) line[4]);
                ob.setBesoinMax((Double) line[5]);
                ob.setDelaiMax((Double) line[6]);
                ob.setStockSecurite((Double) line[7]);
                ob.setPointCmde((Double) line[8]);
                ob.setCoefficient((Double) line[9]);
                ob.setEcartType((Double) line[10]);
                ob.setCategorie((String) line[11]);
                ob.setConditionnement((Long) line[12]);
                ob.setRefUnite((String) line[13]);
                result.add(ob);
            }
        }
        return result;
    }

    public void loadDetailMvtDay(SeuilStocks s) {
        if (s != null) {
            listExclude = "";
            selectedSeuil = s;
            String query = null;
            if (s.getCategorie().equals(Constantes.CAT_MP)) {
                query = "SELECT  SUM(quantite) quantite, date_doc, conditionnement  FROM yvs_base_mouvement_stock m "
                        + "WHERE  m.article=?  AND m.mouvement='S' AND m.table_externe='yvs_prod_of_suivi_flux' AND date_doc BETWEEN ? AND ? "
                        + "group by date_doc, conditionnement "
                        + "ORDER BY date_doc, conditionnement";
            } else if (s.getCategorie().equals(Constantes.CAT_MARCHANDISE)) {
                query = "SELECT  SUM(quantite) quantite, date_doc, conditionnement  FROM yvs_base_mouvement_stock m "
                        + "WHERE  m.article=?  AND m.mouvement='S' AND m.table_externe='yvs_com_contenu_doc_vente' AND date_doc BETWEEN ? AND ? "
                        + "group by date_doc, conditionnement "
                        + "ORDER BY date_doc, conditionnement";
            }
            List<Object[]> re = dao.loadListBySqlQuery(query, new Options[]{new Options(s.getId(), 1), new Options(periodeDebut, 2), new Options(periodeFin, 3)});
            SeuilStocks ob;
            detailDay.clear();
            for (Object[] line : re) {
                ob = new SeuilStocks(s.getId(), s.getRefARt(), s.getDesignation());
                ob.setPointCmde((Double) line[0]);
                ob.setDate((Date) line[1]);
                ob.setConditionnement((Long) line[2]);
                if (s.getConditionnement() > 0 ? s.getConditionnement() != ob.getConditionnement() : false) {
                    Long uniteCible = (Long) dao.loadObjectByNameQueries("YvsBaseConditionnement.findIdUniteById", new String[]{"id"}, new Object[]{s.getConditionnement()});
                    Long uniteMvt = (Long) dao.loadObjectByNameQueries("YvsBaseConditionnement.findIdUniteById", new String[]{"id"}, new Object[]{ob.getConditionnement()});
                    if (uniteCible != null && uniteMvt != null) {
                        //convertir
                        Double coef = (Double) dao.loadObjectByNameQueries("YvsBaseTableConversion.findTauxByUniteCorrespondance", new String[]{"unite", "uniteE"}, new Object[]{new YvsBaseUniteMesure(uniteMvt), new YvsBaseUniteMesure(uniteCible)});
                        if (coef != null ? coef > 0 : false) {
                            ob.setPointCmde(ob.getPointCmde() * coef);
                        }
                    }
                }
                detailDay.add(ob);
            }
        }
    }

    public void loadDetailMvt(SeuilStocks s) {
        if (s != null) {
            String table;
            switch (selectedSeuil.getCategorie()) {
                case Constantes.CAT_MP:
                    table = "yvs_prod_of_suivi_flux";
                    break;
                case Constantes.CAT_MARCHANDISE:
                    table = "yvs_com_contenu_doc_vente";
                    break;
                default:
                    table = "";
            }
            detailStocks = dao.loadNameQueries("YvsBaseMouvementStock.findByArticleDate", new String[]{"article", "date", "table"},
                    new Object[]{new YvsBaseArticles(s.getId()), s.getDate(), table});
            openDialog("dlgDetailMvtStock");
            update("liste_seuil_detail_mvt");
        }
    }

    public void includeMvt(YvsBaseMouvementStock s) {
        if (s != null) {
            listExclude += s.getIdExterne() + ",";
            System.err.println(listExclude);
            loadSeuilsStocksOne();
        }
    }

    public void excludeMvt(YvsBaseMouvementStock s) {
        if (s != null) {
            listExclude.replace(s.getIdExterne() + "", "");
            System.err.println(listExclude);
            loadSeuilsStocksOne();
        }
    }

    public boolean contains(YvsBaseMouvementStock s) {
        if (s != null) {
            return listExclude.contains(s.getIdExterne() + "");
        }
        return false;
    }

    public void excludeDay(Date date) {
        String table;
        switch (selectedSeuil.getCategorie()) {
            case Constantes.CAT_MP:
                table = "yvs_prod_of_suivi_flux";
                break;
            case Constantes.CAT_MARCHANDISE:
                table = "yvs_com_contenu_doc_vente";
                break;
            default:
                table = "";
        }
        List<Long> ids = dao.loadNameQueries("YvsBaseMouvementStock.findIdExtByArticleDate", new String[]{"article", "date", "table"},
                new Object[]{new YvsBaseArticles(selectedSeuil.getId()), date, table});
        for (Long id : ids) {
            listExclude.concat(id.toString()).concat(",");
        }
        loadSeuilsStocksOne();
    }

    public void applySeuilStocks() {
        String query = "UPDATE yvs_base_article_depot SET stock_min=?,stock_alert=? WHERE article=? and depot=? ";
        if (!listeSeuil.isEmpty()) {
            for (SeuilStocks c : listeSeuil) {
                dao.requeteLibre(query, new Options[]{new Options(c.getStockSecurite(), 1), new Options(c.getPointCmde(), 2),
                    new Options(c.getId(), 3), new Options(depot, 4)});
            }
            succes();
        }
    }

    public Double giveStockStock(Long art, Long cond, Long depot) {
        if (art != null && depot != null && cond != null) {
            Double d = dao.stocksReel(art, -1, depot, 0L, 0L, new Date(), cond, 0L);
            return d != null ? d : 0d;
        }
        return 0.0;
    }

    public void initDepotPr(YvsBaseArticles art) {
        if (art != null) {
            ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (service != null ? service.getSelectDepot() != null : false) {
                YvsBaseDepots depotPr = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticlePr", new String[]{"article", "agence"}, new Object[]{art, service.getSelectDepot().getAgence()});
                if (depotPr != null) {
                    article.setDepotPr(UtilProd.buildBeanDepot(depotPr));
                }
            }
        }
    }
}
