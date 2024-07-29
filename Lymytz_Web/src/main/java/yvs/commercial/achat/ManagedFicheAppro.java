/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.CategorieComptable;
import yvs.base.produits.ArticleFournisseur;
import yvs.production.UtilProd;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.UtilCom;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.depot.ArticleDepot;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.commercial.stock.ContenuDocStock;
import yvs.commercial.stock.DocStock;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.commercial.stock.ManagedTransfertStock;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticleFournisseur;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.commercial.achat.YvsComArticleApprovisionnement;
import yvs.entity.commercial.achat.YvsComArticleFourniAchat;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidApprovissionnement;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedFicheAppro extends ManagedCommercial<FicheApprovisionnement, YvsComFicheApprovisionnement> implements Serializable {

    @ManagedProperty(value = "#{ficheApprovisionnement}")
    private FicheApprovisionnement ficheApprovisionnement;
    private List<YvsComFicheApprovisionnement> documents;
    private YvsComFicheApprovisionnement selectFiche;

    private List<YvsComArticleApprovisionnement> contenus, all_contenus;
    private YvsComArticleApprovisionnement selectContenu;
    private YvsBaseArticleFournisseur selectArticleF;
    private ArticleApprovisionnement contenu = new ArticleApprovisionnement();
    private PaginatorResult<YvsComArticleApprovisionnement> pa = new PaginatorResult<>();
    public PaginatorResult<YvsComArticleApprovisionnement> p_contenu = new PaginatorResult<>();

    private String motifEtape;
    YvsWorkflowValidApprovissionnement etape;
    private boolean lastEtape;

    private long id_art_fsseur;
    private List<YvsComArticleFourniAchat> articles;
    private YvsComDocAchats selectBon;
    private List<ContenuDocAchat> contenusDocAchat;

    private double quantitee, reste;

    private boolean back;

    private long id_art_depot;
    private YvsComDocStocks selectOrdre;
    private List<ContenuDocStock> contenusDocStock;

    private List<YvsComCreneauDepot> creneaux;

    private String tabIds, tabIds_article;
    private boolean updateFiche_, selectArt, listArt;
    boolean correctArticle;
    private int max = 10;

    private boolean toValideLoad = true;
    private Boolean autoSearch;
    private String terminerSearch, egaliteStatut = "!=";
    private long creneauSearch, depotSearch;
    //Parametre recherche contenu
    private boolean dateContenu = false, addPrix;
    private Date dateDebutContenu = new Date(), dateFinContenu = new Date();
    private String statutContenu, reference, article, depot, fournisseurF, comparer = ">=";
    private double prixSearch, prix2Search;

    private List<String> statuts;

    public ManagedFicheAppro() {
        documents = new ArrayList<>();
        contenus = new ArrayList<>();
        all_contenus = new ArrayList<>();
        creneaux = new ArrayList<>();
        contenusDocAchat = new ArrayList<>();
        contenusDocStock = new ArrayList<>();
        articles = new ArrayList<>();
        statuts = new ArrayList<>();
    }

    public String getMotifEtape() {
        return motifEtape;
    }

    public void setMotifEtape(String motifEtape) {
        this.motifEtape = motifEtape;
    }

    public boolean isLastEtape() {
        return lastEtape;
    }

    public void setLastEtape(boolean lastEtape) {
        this.lastEtape = lastEtape;
    }

    public List<String> getStatuts() {
        return statuts;
    }

    public void setStatuts(List<String> statuts) {
        this.statuts = statuts;
    }

    public List<YvsComArticleApprovisionnement> getAll_contenus() {
        return all_contenus;
    }

    public void setAll_contenus(List<YvsComArticleApprovisionnement> all_contenus) {
        this.all_contenus = all_contenus;
    }

    public long getId_art_fsseur() {
        return id_art_fsseur;
    }

    public void setId_art_fsseur(long id_art_fsseur) {
        this.id_art_fsseur = id_art_fsseur;
    }

    public boolean isBack() {
        return back;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public long getId_art_depot() {
        return id_art_depot;
    }

    public void setId_art_depot(long id_art_depot) {
        this.id_art_depot = id_art_depot;
    }

    public boolean isCorrectArticle() {
        return correctArticle;
    }

    public void setCorrectArticle(boolean correctArticle) {
        this.correctArticle = correctArticle;
    }

    public boolean isDateContenu() {
        return dateContenu;
    }

    public void setDateContenu(boolean dateContenu) {
        this.dateContenu = dateContenu;
    }

    public boolean isAddPrix() {
        return addPrix;
    }

    public void setAddPrix(boolean addPrix) {
        this.addPrix = addPrix;
    }

    public Date getDateDebutContenu() {
        return dateDebutContenu;
    }

    public void setDateDebutContenu(Date dateDebutContenu) {
        this.dateDebutContenu = dateDebutContenu;
    }

    public Date getDateFinContenu() {
        return dateFinContenu;
    }

    public void setDateFinContenu(Date dateFinContenu) {
        this.dateFinContenu = dateFinContenu;
    }

    public String getStatutContenu() {
        return statutContenu;
    }

    public void setStatutContenu(String statutContenu) {
        this.statutContenu = statutContenu;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getFournisseurF() {
        return fournisseurF;
    }

    public void setFournisseurF(String fournisseurF) {
        this.fournisseurF = fournisseurF;
    }

    public String getComparer() {
        return comparer;
    }

    public void setComparer(String comparer) {
        this.comparer = comparer;
    }

    public double getPrixSearch() {
        return prixSearch;
    }

    public void setPrixSearch(double prixSearch) {
        this.prixSearch = prixSearch;
    }

    public double getPrix2Search() {
        return prix2Search;
    }

    public void setPrix2Search(double prix2Search) {
        this.prix2Search = prix2Search;
    }

    public YvsWorkflowValidApprovissionnement getCurrentEtape() {
        return currentEtape;
    }

    public void setCurrentEtape(YvsWorkflowValidApprovissionnement currentEtape) {
        this.currentEtape = currentEtape;
    }

    public PaginatorResult<YvsComArticleApprovisionnement> getP_contenu() {
        return p_contenu;
    }

    public void setP_contenu(PaginatorResult<YvsComArticleApprovisionnement> p_contenu) {
        this.p_contenu = p_contenu;
    }

    public boolean isToValideLoad() {
        return toValideLoad;
    }

    public void setToValideLoad(boolean toValideLoad) {
        this.toValideLoad = toValideLoad;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public String getTerminerSearch() {
        return terminerSearch;
    }

    public void setTerminerSearch(String terminerSearch) {
        this.terminerSearch = terminerSearch;
    }

    public double getQuantitee() {
        return quantitee;
    }

    public void setQuantitee(double quantitee) {
        this.quantitee = quantitee;
    }

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Boolean getAutoSearch() {
        return autoSearch;
    }

    public void setAutoSearch(Boolean autoSearch) {
        this.autoSearch = autoSearch;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public long getCreneauSearch() {
        return creneauSearch;
    }

    public void setCreneauSearch(long creneauSearch) {
        this.creneauSearch = creneauSearch;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public PaginatorResult<YvsComArticleApprovisionnement> getPa() {
        return pa;
    }

    public void setPa(PaginatorResult<YvsComArticleApprovisionnement> pa) {
        this.pa = pa;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public List<YvsComCreneauDepot> getCreneaux() {
        return creneaux;
    }

    public void setCreneaux(List<YvsComCreneauDepot> creneaux) {
        this.creneaux = creneaux;
    }

    public List<YvsComArticleApprovisionnement> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComArticleApprovisionnement> contenus) {
        this.contenus = contenus;
    }

    public YvsComArticleApprovisionnement getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComArticleApprovisionnement selectContenu) {
        this.selectContenu = selectContenu;
    }

    public YvsComFicheApprovisionnement getSelectFiche() {
        return selectFiche;
    }

    public void setSelectFiche(YvsComFicheApprovisionnement selectFiche) {
        this.selectFiche = selectFiche;
    }

    public YvsComDocStocks getSelectOrdre() {
        return selectOrdre;
    }

    public void setSelectOrdre(YvsComDocStocks selectOrdre) {
        this.selectOrdre = selectOrdre;
    }

    public List<ContenuDocStock> getContenusDocStock() {
        return contenusDocStock;
    }

    public void setContenusDocStock(List<ContenuDocStock> contenusDocStock) {
        this.contenusDocStock = contenusDocStock;
    }

    public YvsComDocAchats getSelectBon() {
        return selectBon;
    }

    public void setSelectBon(YvsComDocAchats selectBon) {
        this.selectBon = selectBon;
    }

    public List<ContenuDocAchat> getContenusDocAchat() {
        return contenusDocAchat;
    }

    public void setContenusDocAchat(List<ContenuDocAchat> contenusDocAchat) {
        this.contenusDocAchat = contenusDocAchat;
    }

    public List<YvsComArticleFourniAchat> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsComArticleFourniAchat> articles) {
        this.articles = articles;
    }

    public boolean isUpdateFiche_() {
        return updateFiche_;
    }

    public void setUpdateFiche_(boolean updateFiche_) {
        this.updateFiche_ = updateFiche_;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public ArticleApprovisionnement getContenu() {
        return contenu;
    }

    public void setContenu(ArticleApprovisionnement contenu) {
        this.contenu = contenu;
    }

    public FicheApprovisionnement getFicheApprovisionnement() {
        return ficheApprovisionnement;
    }

    public void setFicheApprovisionnement(FicheApprovisionnement ficheApprovisionnement) {
        this.ficheApprovisionnement = ficheApprovisionnement;
    }

    public List<YvsComFicheApprovisionnement> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComFicheApprovisionnement> documents) {
        this.documents = documents;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public YvsBaseArticleFournisseur getSelectArticleF() {
        return selectArticleF;
    }

    public void setSelectArticleF(YvsBaseArticleFournisseur selectArticleF) {
        this.selectArticleF = selectArticleF;
    }

    @Override
    public void loadAll() {
        loadInfosWarning(false);
        if (terminerSearch != null ? terminerSearch.trim().length() < 1 : true) {
            this.egaliteStatut = "!=";
            this.terminerSearch = Constantes.ETAT_SOUMIS;
            chooseTerminer(false);
        }
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
            addParamToValide();
        }
        loadContenus(true, true);
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllFiche(true, true);
    }

    private int buildDocByDroit() {
        return 1;
//        if (autoriser("fv_view_all_doc")) {
//            return 1;
//        } else if (autoriser("fv_view_only_doc_agence")) {
//            return 2;
//        } else if (autoriser("fv_view_only_doc_pv")) {
//            return 3;
//        } else {
//            return 4;
//        }
    }

    public void loadAllFiche(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit()) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.depot.agence", "agence", currentAgence, "=", "AND");
                paginator.addParam(p);
                break;
            case 3: //charge tous les document des depots où l'utilisateurs est responsable
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                if (!ids.isEmpty()) {
                    p = new ParametreRequete("y.depot.id", "depot", ids, "IN", "AND");
                    paginator.addParam(p);
                } else {
                    paginator.getParams().clear();
                }
                break;
            default:    //charge les document du depot donc l'utilisateur connecté est responsable
                p = new ParametreRequete("y.depot", "depot", currentDepot, "=", "AND");
                paginator.addParam(p);
                break;

        }
        documents = paginator.executeDynamicQuery("YvsComFicheApprovisionnement", "y.dateApprovisionnement DESC, y.reference DESC", avance, init, (int) imax, dao);
        update("data_fiche_approv");
    }

    public void gotoPagePaginatorContenu() {
        p_contenu.gotoPage(p_contenu.getPage());
        loadContenus(true, true);
    }

    public void choosePaginatorContenu(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            long v;
            try {
                v = (long) ev.getNewValue();
            } catch (Exception ex) {
                v = (int) ev.getNewValue();
            }
            p_contenu.setRows((int) v);
            loadContenus(true, true);
        }
    }

    public void loadContenus(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit()) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.fiche.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                p_contenu.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.fiche.depot.agence", "agence", currentAgence, "=", "AND");
                p_contenu.addParam(p);
                break;
            case 3: { //charge tous les document des points de vente où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                if (!ids.isEmpty()) {
                    p = new ParametreRequete("y.fiche.depot.id", "depot", ids, " IN ", "AND");
                    p_contenu.addParam(p);
                } else {
                    p_contenu.getParams().clear();
                }
                break;
            }
            default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                p = new ParametreRequete("y.fiche.depot", "depot", currentDepot, "=", "AND");
                p_contenu.addParam(p);
                break;

        }
        String orderBy = "y.fiche.dateApprovisionnement DESC, y.fiche.reference";
        all_contenus = p_contenu.executeDynamicQuery("YvsComArticleApprovisionnement", orderBy, avance, init, dao);
        update("data_contenu_approv");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComFicheApprovisionnement> re = paginator.parcoursDynamicData("YvsComFicheApprovisionnement", "y", "y.dateApprovisionnement DESC, y.reference DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void init(boolean next) {
        loadAllFiche(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllFiche(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllFiche(true, true);
    }

    public void _choosePaginator(ValueChangeEvent ev) {
        max = (int) ev.getNewValue();
        loadContenu(true, true);
    }

    @Override
    public void onSelectDistant(YvsComFicheApprovisionnement y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Approvissionnements", "modGescom", "smenFicheAppro", true);
            }
        }
    }

    public void loadAllFicheByDepotDate() {
//        if ((ficheApprovisionnement.getDepot() != null) ? ficheApprovisionnement.getDepot().getId() > 0 : false) {
//            if (!ficheApprovisionnement.isUpdate()) {
//                String[] ch = new String[]{"depot", "dateApprovisionnement", "statut"};
//                Object[] v = new Object[]{new YvsBaseDepots(ficheApprovisionnement.getDepot().getId()), ficheApprovisionnement.getDateApprovisionnement(), Constantes.ETAT_EDITABLE};
//                String query = "YvsComFicheApprovisionnement.findByDepotDateStatut";
//                List<YvsComFicheApprovisionnement> l = dao.loadNameQueries(query, ch, v, 0, 1);
//                if ((l != null) ? l.size() > 0 : false) {
//                    getWarningMessage("Une fiche existe deja pour ce dépôt à ce jour! Vous passez en mode modification");
//                    onnSelectView(l.get(0));
//                }
//            }
//        }
    }

    public void loadArticleFsseurByArticle() {
        if ((selectContenu != null) ? selectContenu.getId() > 0 : false) {
            ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            if (m != null) {
                m.loadAllArticleAndFournisseur(selectContenu.getArticle().getArticle());
            }
        }
    }

    public void loadBackArticleByArticle() {
        articles.clear();
        if ((selectContenu != null) ? selectContenu.getId() > 0 : false) {
            String[] ch = new String[]{"article"};
            Object[] v = new Object[]{new YvsComArticleApprovisionnement(selectContenu.getId())};
            String query = "YvsComArticleFourniAchat.findByArticle";
            articles = dao.loadNameQueries(query, ch, v);
        }
    }

    public void loadAllDepotByArticle() {
        if ((selectContenu != null) ? selectContenu.getId() > 0 : false) {
            ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (m != null) {
                m.loadAllArticle(selectContenu.getArticle().getArticle(), new YvsBaseDepots(ficheApprovisionnement.getDepot().getId()));
                for (YvsBaseArticleDepot a : m.getArticles_depot()) {
                    double s = dao.stocks(a.getArticle().getId(), 0, a.getDepot().getId(), 0, 0, new Date());
                    a.setQuantiteStock(s);
                }
            }
        }
        update("data_depot_article");
    }

    private void loadContenu(YvsComFicheApprovisionnement y, boolean avance, boolean init) {
        pa.addParam(new ParametreRequete("y.fiche", "fiche", y, "=", "AND"));
        loadContenu(avance, init);
    }

    public void loadContenu(boolean avance, boolean init) {
        if (pa.getParams().contains(new ParametreRequete("fiche"))) {
            contenus = pa.executeDynamicQuery("DISTINCT y ", "DISTINCT y", "YvsComArticleApprovisionnement y LEFT OUTER JOIN y.achats a", "y.article.article.refArt", avance, init, max, dao);
            update("data_contenu_fiche_approv");
        }
    }

    private void loadCreneaux(YvsBaseDepots y) {
        creneaux = loadCreneaux(y, ficheApprovisionnement.getDateApprovisionnement());
        update("select_creneau_fiche_approv");
    }

    public YvsComArticleApprovisionnement buildArticleApprovisionnement(ArticleApprovisionnement y) {
        YvsComArticleApprovisionnement a = UtilCom.buildArticleApprovisionnement(y, currentUser);
        if ((selectFiche != null) ? selectFiche.getId() > 0 : false) {
            a.setFiche(selectFiche);
        }
        return a;
    }

    public YvsComArticleFourniAchat buildArticleFourniAchat(ArticleFourniAchat y) {
        YvsComArticleFourniAchat a = UtilCom.buildArticleFourniAchat(y, currentUser);
        if ((selectContenu != null) ? selectContenu.getId() > 0 : false) {
            a.setArticle(selectContenu);
        }
        return a;
    }

    public YvsComContenuDocAchat buildContenuDocAchat(ContenuDocAchat y) {
        YvsComContenuDocAchat c = UtilCom.buildContenuDocAchat(y, currentUser);
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            c.setExterne(selectContenu);
        }
        return c;
    }

    public YvsComContenuDocStock buildContenuDocStock(ContenuDocStock y) {
        YvsComContenuDocStock c = UtilCom.buildContenuDocStock(y, currentUser);
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            c.setExterne(selectContenu);
        }
        return c;
    }

    @Override
    public FicheApprovisionnement recopieView() {
        ficheApprovisionnement.setNew_(true);
        return ficheApprovisionnement;
    }

    public ArticleFourniAchat recopieViewArticleAchat(ArticleFournisseur y) {
        ArticleFourniAchat a = new ArticleFourniAchat();
        a.setId(y.getId());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, (int) y.getDelaiLivraison());
        a.setDateLivraison(cal.getTime());
        a.setEtat(Constantes.ETAT_EDITABLE);
        a.setPua(y.getPrix());
        a.setRemise(y.getRemise());
        a.setFournisseur(y.getFournisseur());
        a.setQuantite(quantitee);
        return a;
    }

    public DocAchat recopieViewDocAchat(YvsBaseFournisseur y) {
        String num = genererReference(Constantes.TYPE_FA_NAME, selectContenu != null ? selectContenu.getDateLivraison() : new Date());
        if (num != null ? num.trim().length() > 0 : false) {
            DocAchat d = new DocAchat();
            d.setActif(true);
            d.setAutomatique(false);
            d.setSupp(false);
            d.setDateDoc(selectContenu != null ? selectContenu.getDateLivraison() : new Date());
            d.setFournisseur(new Fournisseur(y.getId()));
            d.setCategorieComptable(new CategorieComptable(y.getCategorieComptable().getId()));
            d.setId(0);
            d.setLegendeType("");
            d.setNumPiece("");
            d.setStatut(Constantes.ETAT_EDITABLE);
            d.setTypeDoc(Constantes.TYPE_FA);
            d.setNumDoc(num);
            d.setDepotReception(ficheApprovisionnement.getDepot());
            return d;
        }
        return null;
    }

    public DocStock recopieViewDocStock(YvsBaseDepots y) {
        String num = genererReference(Constantes.TYPE_FT_NAME, selectContenu != null ? selectContenu.getDateLivraison() : new Date(), y.getId());
        if (num != null ? num.trim().length() > 0 : false) {
            DocStock d = new DocStock();
            d.setActif(true);
            d.setAutomatique(false);
            d.setSupp(false);
            d.setDateDoc(selectContenu != null ? selectContenu.getDateLivraison() : new Date());
            d.setSource(new Depots(y.getId()));
            d.setDestination(ficheApprovisionnement.getDepot());
            d.setCreneauDestinataire(ficheApprovisionnement.getCreneau());
            d.setId(-1);
            d.setNumPiece("");
            d.setStatut(Constantes.ETAT_EDITABLE);
            d.setTypeDoc(Constantes.TYPE_FT);
            d.setNumDoc(num);
            //trouve un créneau source
            List<YvsComCreneauDepot> l = dao.loadNameQueries("YvsComCreneauDepot.findByDepot", new String[]{"depot"}, new Object[]{y}, 0, 1);
            if (l != null ? !l.isEmpty() : false) {
                d.setCreneauSource(UtilCom.buildBeanCreneau(l.get(0)));
            } else {
                getErrorMessage("Le créneau source n'a pas été trouvé !");
                return null;
            }
            return d;
        } else {
            getErrorMessage("Le numéro du document n'a pas pu être généré !");
        }
        return null;
    }

    public ContenuDocAchat recopieViewContenuDocAchat(YvsBaseArticleFournisseur y, double qte) {
        ContenuDocAchat c = new ContenuDocAchat();
        if (y != null ? (y.getArticle() != null) : false) {
            c.setActif(true);
            c.setArticle(UtilCom.buildBeanArticle(y.getArticle()));
            c.setCommentaire("");
            c.setId(y.getId());
            c.setPrixAchat(y.getPuv());
            c.setRemiseRecu(y.getRemise());
            c.setQuantiteCommende(qte);
            c.setQuantiteRecu(qte);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, y.getDelaiLivraison().intValue());
            c.setDateLivraison(cal.getTime());
            c.setStatut(Constantes.ETAT_EDITABLE);
            c.setConditionnement(UtilProd.buildBeanConditionnement(selectContenu.getConditionnement()));
        }
        return c;
    }

    public ContenuDocAchat recopieViewContenuDocAchat(YvsComArticleFourniAchat y, double qte) {
        ContenuDocAchat c = new ContenuDocAchat();
        if (y != null ? (y.getArticle() != null ? (y.getArticle().getArticle() != null ? y.getArticle().getArticle().getArticle() != null : false) : false) : false) {
            c.setActif(true);
            c.setArticle(UtilCom.buildBeanArticle(selectContenu.getArticle().getArticle()));
            c.setCommentaire("");
            c.setId(y.getId());
            c.setPrixAchat(selectContenu.getPrixAchat());
            c.setRemiseRecu(selectContenu.getRemise());
            c.setQuantiteCommende(qte);
            c.setQuantiteRecu(qte);
            c.setDateLivraison((y.getDateLivraison() != null) ? y.getDateLivraison() : new Date());
            c.setStatut(Constantes.ETAT_EDITABLE);
        }
        return c;
    }

    public ContenuDocStock recopieViewContenuDocStock(YvsBaseArticleDepot y, double qte) {
        ContenuDocStock c = new ContenuDocStock();
        if (y != null ? (y.getArticle() != null) : false) {
            c.setActif(true);
            c.setSupp(false);
            c.setArticle(UtilCom.buildBeanArticle(y.getArticle()));
            c.setConditionnement(UtilProd.buildBeanConditionnement(selectContenu.getConditionnement()));
            c.setId(y.getId());
            c.setPrix(y.getArticle().getPua());
            c.setQuantite(qte);
            c.setQteAttente(qte);
            c.setDateContenu(new Date());
        }
        return c;
    }

    public ContenuDocAchat recopieViewContenuDocAchat(ArticleApprovisionnement y, double qte) {
        ContenuDocAchat c = new ContenuDocAchat();
        if (y != null ? (y.getArticle() != null ? (y.getArticle().getArticle() != null) : false) : false) {
            c.setActif(true);
            YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{y.getArticle().getArticle().getId()});
            c.setArticle(UtilCom.buildBeanArticle(a));
            c.setConditionnement(y.getConditionnement());
            c.setCommentaire("");
            c.setId(y.getId());
            c.setPrixAchat(y.getArticle().getArticle().getPua());
            c.setRemiseRecu(y.getArticle().getArticle().getRemise());
            c.setQuantiteCommende(qte);
            c.setQuantiteRecu(qte);
            c.setDateLivraison(new Date());
            c.setStatut(Constantes.ETAT_EDITABLE);
        }
        return c;
    }

    public ContenuDocStock recopieViewContenuDocStock(ArticleApprovisionnement y, double qte) {
        ContenuDocStock c = new ContenuDocStock();
        if (y != null ? (y.getArticle() != null ? (y.getArticle().getArticle() != null) : false) : false) {
            c.setActif(true);
            c.setSupp(false);
            YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{y.getArticle().getArticle().getId()});
            c.setArticle(UtilCom.buildBeanArticle(a));
            c.setConditionnement(y.getConditionnement());
            c.setId(y.getId());
            c.setPrix(y.getArticle().getArticle().getPua());
            c.setQuantite(qte);
            c.setQteAttente(qte);
            c.setDateContenu(new Date());
        }
        return c;
    }

    @Override
    public boolean controleFiche(FicheApprovisionnement bean) {
        if (!_controleFiche_(bean, false)) {
            return false;
        }
        if ((bean.isAuto() && (bean.getReference() == null || bean.getReference().equals(""))) || !bean.isUpdate()
                || (selectFiche != null ? !selectFiche.getDateApprovisionnement().equals(bean.getDateApprovisionnement()) : true)) {
            String num = genererReference(Constantes.TYPE_FiA_NAME, bean.getDateApprovisionnement());
            if (num == null || num.trim().length() < 1) {
                return false;
            }
            bean.setReference(num);
        }
        if ((bean.getDepot() != null) ? bean.getDepot().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le depot");
            return false;
        }
        if (!verifyDateAchat(bean.getDateApprovisionnement(), bean.isUpdate())) {
            return false;
        }
        return true;
    }

    private boolean _controleFiche_(YvsComFicheApprovisionnement bean) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (!bean.getEtat().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.getCloturer()) {
            getErrorMessage("Ce document est vérouillé");
            return false;
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean _controleFiche_(FicheApprovisionnement bean, boolean valide) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (!bean.getEtat().equals(valide ? Constantes.ETAT_VALIDE : Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre " + (valide ? "valide" : "éditable") + " pour pouvoir etre modifié");
            return false;
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document est vérouillé");
            return false;
        }
//        return writeInExercice(bean.getDateApprovisionnement());
        return true;
    }

    public boolean controleFicheArticle(ArticleApprovisionnement bean) {
        if (ficheApprovisionnement != null ? ficheApprovisionnement.getId() < 1 : false) {
            if (!_saveNew()) {
                return false;
            }
            bean.setFiche(ficheApprovisionnement);
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        return _controleFiche_(bean.getFiche(), false);
    }

    public boolean controleFicheArticle(YvsComArticleApprovisionnement bean) {
        if (selectFiche != null ? selectFiche.getId() < 1 : true) {
            getErrorMessage("Vous devez enregistrer une fiche");
            return false;
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if (!selectFiche.getEtat().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        return _controleFiche_(selectFiche);
    }

    @Override
    public void populateView(FicheApprovisionnement bean) {
        cloneObject(ficheApprovisionnement, bean);
        ficheApprovisionnement.setEtapesValidations(ordonneEtapes(bean.getEtapesValidations()));
    }

    public void populateViewArticle(ArticleApprovisionnement bean) {
        bean.setStock(dao.stocks(bean.getArticle().getId(), ficheApprovisionnement.getCreneau().getTranche().getId(), ficheApprovisionnement.getDepot().getId(), 0, 0, ficheApprovisionnement.getDateApprovisionnement()));
        bean.getArticle().setPr(dao.getPuv(bean.getArticle().getId(), bean.getQuantite(), 0, 0, ficheApprovisionnement.getDepot().getId(), 0, ficheApprovisionnement.getDateApprovisionnement()));
        selectArt = true;
        cloneObject(contenu, bean);
    }

    @Override
    public void resetFiche() {
        resetFichePartial();
        update("blog_form_fiche_approv");
    }

    public void resetFichePartial() {
        resetFiche(ficheApprovisionnement);
        ficheApprovisionnement.setCloturer(false);
        ficheApprovisionnement.setDateApprovisionnement(new Date());
        ficheApprovisionnement.setHeureApprovisionnement(new Date());
        ficheApprovisionnement.setDepot(new Depots());
        ficheApprovisionnement.setCreneau(new Creneau());
        ficheApprovisionnement.setEtat(Constantes.ETAT_EDITABLE);
        ficheApprovisionnement.setStatutTerminer(Constantes.ETAT_ATTENTE);
        ficheApprovisionnement.setEtapesValidations(new ArrayList<YvsWorkflowValidApprovissionnement>());

        selectFiche = new YvsComFicheApprovisionnement();
        tabIds = "";

        contenus.clear();
        creneaux.clear();

        if ((ficheApprovisionnement.getDepot() != null) ? ficheApprovisionnement.getDepot().getId() < 1 : true) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null ? m.getDepots_all().contains(currentDepot) : false) {
                ficheApprovisionnement.setDepot(UtilCom.buildBeanDepot(currentDepot));
                loadCreneaux(currentDepot);
                ficheApprovisionnement.setCreneau(UtilCom.buildBeanCreneau(currentCreneauDepot));
                ManagedStockArticle y = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (y != null) {
                    y.setEntityDepot(currentDepot);
                    y.loadActifArticleByDepot(true, true);
                }
            }
        }

        setUpdateFiche_(false);
        resetFicheArticle();
        update("blog_form_fiche_approv");
    }

    public void resetFicheArticle() {
        contenu = new ArticleApprovisionnement();
        tabIds_article = "";
        selectContenu = null;
        selectArt = false;
        update("blog_form_contenu_fiche_approv");
        update("desc_artcile_fiche_approv");
    }

    @Override
    public boolean saveNew() {
        if (_saveNew()) {
            succes();
            actionOpenOrResetAfter(this);
            return true;
        }
        return false;
    }

    public boolean _saveNew() {
        String action = ficheApprovisionnement.isUpdate() ? "Modification" : "Insertion";
        try {
            if (controleFiche(ficheApprovisionnement)) {
                selectFiche = UtilCom.buildFicheApprovisionnement(ficheApprovisionnement, currentUser);
                if (!ficheApprovisionnement.isUpdate()) {
                    List<YvsWorkflowEtapeValidation> etapes = saveEtapesValidation();
                    selectFiche.setEtapeTotal(etapes != null ? etapes.size() : 0);
                    selectFiche.setId(null);
                    selectFiche = (YvsComFicheApprovisionnement) dao.save1(selectFiche);
                    ficheApprovisionnement.setId(selectFiche.getId());
                    selectFiche.setEtapesValidations(saveEtapesValidation(selectFiche, etapes));
                    ficheApprovisionnement.setEtapesValidations(new ArrayList<>(selectFiche.getEtapesValidations()));
                    documents.add(0, selectFiche);
                    if (documents.size() > imax) {
                        documents.remove(documents.size() - 1);
                    }
                } else {
                    dao.update(selectFiche);
                    if (documents.contains(selectFiche)) {
                        documents.set(documents.indexOf(selectFiche), selectFiche);
                    }
                }
                ficheApprovisionnement.setReference(ficheApprovisionnement.getReference());
                ficheApprovisionnement.setUpdate(true);
                update("data_fiche_approv");
                update("data_depot_fiche_approv");
                update("entete_form_fiche_approv");
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            log.log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    public boolean saveNewArticle() {
        String action = contenu.isUpdate() ? "Modification" : "Insertion";
        try {
            if (controleFicheArticle(contenu)) {
                if (!contenu.getArticle().getArticle().getConditionnements().isEmpty() && contenu.getConditionnement().getId() > 0) {
                    int idx = contenu.getArticle().getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(contenu.getConditionnement().getId()));
                    if (idx >= 0) {
                        contenu.setConditionnement(UtilProd.buildBeanConditionnement(contenu.getArticle().getArticle().getConditionnements().get(idx)));
                    }
                }
                YvsComArticleApprovisionnement entity = buildArticleApprovisionnement(contenu);
                entity.setDateUpdate(new Date());
                if (!contenu.isUpdate()) {
                    entity.setQuantiteRest(entity.getQuantite());
                    entity.setId(null);
                    entity = (YvsComArticleApprovisionnement) dao.save1(entity);
                    contenu.setId(entity.getId());
                    contenus.add(0, entity);
                } else {
                    dao.update(entity);
                    contenus.set(contenus.indexOf(entity), entity);
                }
                succes();
                resetFicheArticle();
                update("data_contenu_fiche_approv");
                update("data_fiche_approv");
                update("blog_form_contenu_fiche_approv");
                update("data_depot_fiche_approv");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            log.log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void createBonAchat() {
        if (!ficheApprovisionnement.getEtat().equals(Constantes.ETAT_SOUMIS)) {
            if (quantitee <= 0) {
                getErrorMessage("Vous devez entrer une quantitée");
                return;
            }
            if (quantitee > reste) {
                getErrorMessage("Vous ne pouvez pas commander cette quantitée");
                return;
            }
            if (!_controleFiche_(ficheApprovisionnement, true)) {
                closeDialog("dlgByQteLivreeByFsseur");
                return;
            }
            ContenuDocAchat bean;
            YvsBaseFournisseur fsseur;
            if (!back) {
                bean = recopieViewContenuDocAchat(selectArticleF, quantitee);
                fsseur = selectArticleF.getFournisseur();
            } else {
                YvsComArticleFourniAchat y = (YvsComArticleFourniAchat) dao.loadOneByNameQueries("YvsComArticleFourniAchat.findById", new String[]{"id"}, new Object[]{id_art_fsseur});
                bean = recopieViewContenuDocAchat(y, quantitee);
                fsseur = y.getFournisseur();
            }
            if ((fsseur != null ? fsseur.getId() > 0 : false) && (bean != null)) {
                YvsComDocAchats doc = null;
                ManagedFactureAchat s = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                if (s != null) {
                    doc = s._saveNew(recopieViewDocAchat(fsseur), true, true, true);
                }
                if (doc != null ? doc.getId() > 0 : false) {
                    saveNewContenuDoc(doc, bean);
                    closeDialog("dlgByQteLivreeByFsseur");
                }
            }
        } else {
            getErrorMessage("Vous ne pouvez modifier le contenu car la fiche a deja ete soumise");
        }
    }

    public void createTransfert() {
        if (!ficheApprovisionnement.getEtat().equals(Constantes.ETAT_SOUMIS)) {
            if (quantitee <= 0) {
                getErrorMessage("Vous devez entrer une quantitée");
                return;
            }
            if (quantitee > reste) {
                getErrorMessage("Vous ne pouvez pas demander cette quantitée");
                return;
            }
            if (!_controleFiche_(ficheApprovisionnement, true)) {
                closeDialog("dlgQteLivreeByDepot");
                return;
            }
            YvsComDocStocks doc = null;
            YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findById", new String[]{"id"}, new Object[]{id_art_depot});
            if (y != null ? y.getId() > 0 : false) {
                ManagedTransfertStock s = (ManagedTransfertStock) giveManagedBean(ManagedTransfertStock.class);
                if (s != null) {
                    DocStock bean = recopieViewDocStock(y.getDepot());
                    if (bean != null) {
                        doc = s._saveNew(bean, false, true);
                    }
                }
                if (doc != null ? doc.getId() > 0 : false) {
                    ContenuDocStock bean = recopieViewContenuDocStock(y, quantitee);
                    saveNewContenuDoc(doc, bean);
                    closeDialog("dlgQteLivreeByDepot");
                }
            }
        } else {
            getErrorMessage("Vous ne pouvez modifier le contenu car la fiche a deja ete soumise");
        }
    }

    public void insertOnBonAchat() {
        if (!ficheApprovisionnement.getEtat().equals(Constantes.ETAT_SOUMIS)) {
            if (quantitee <= 0) {
                getErrorMessage("Vous devez entrer une quantitée");
                return;
            }
            if (quantitee > reste) {
                getErrorMessage("Vous ne pouvez pas commander cette quantitée");
                return;
            }
            if (!_controleFiche_(ficheApprovisionnement, true)) {
                closeDialog("dlgQteLivreeByBon");
                return;
            }
            ContenuDocAchat bean = recopieViewContenuDocAchat(UtilCom.buildBeanArticleApprovisionnement(selectContenu), quantitee);
            saveNewContenuDoc(selectBon, bean);
            closeDialog("dlgQteLivreeByBon");
        } else {
            getErrorMessage("Vous ne pouvez modifier le contenu car la fiche a deja ete soumise");
        }
    }

    public void insertOnOrdre() {
        if (!ficheApprovisionnement.getEtat().equals(Constantes.ETAT_SOUMIS) && _controleFiche_(ficheApprovisionnement, true)) {
            if (quantitee <= 0) {
                getErrorMessage("Vous devez entrer une quantitée");
                return;
            }
            if (quantitee > reste) {
                getErrorMessage("Vous ne pouvez pas demander cette quantitée");
                return;
            }
            if (!_controleFiche_(ficheApprovisionnement, true)) {
                closeDialog("dlgQteLivreeByOrdre");
                return;
            }
            ContenuDocStock bean = recopieViewContenuDocStock(UtilCom.buildBeanArticleApprovisionnement(selectContenu), quantitee);
            saveNewContenuDoc(selectOrdre, bean);
            closeDialog("dlgQteLivreeByOrdre");
        } else {
            getErrorMessage("Vous ne pouvez modifier le contenu car la fiche a deja ete soumise");
        }
    }

    public boolean saveNewContenuDoc(YvsComDocAchats doc, ContenuDocAchat bean) {
        try {
            double q = bean.getQuantiteCommende();
            boolean update = false;
            List<YvsComContenuDocAchat> l = dao.loadNameQueries("YvsComContenuDocAchat.findByDocAchat", new String[]{"docAchat"}, new Object[]{doc});
            if ((l != null) ? !l.isEmpty() : false) {
                ContenuDocAchat c_;
                for (YvsComContenuDocAchat c : l) {
                    if (c.getArticle().getId().equals(bean.getArticle().getId()) && c.getPrixAchat() == bean.getPrixAchat() && c.getRemise() == bean.getRemiseRecu()) {
                        c_ = UtilCom.buildBeanContenuDocAchat(c);
                        double qte = bean.getQuantiteCommende();
                        cloneObject(bean, c_);
                        bean.setQuantiteRecu(bean.getQuantiteRecu() + qte);
                        update = true;
                        break;
                    }
                }
            }
            ManagedFactureAchat s = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (s != null) {
                s.findPrixArticle(bean, null, doc, false);
            }
            bean.setPrixTotalRecu(bean.getPrixTotalRecu() > 0 ? bean.getPrixTotalRecu() : 0);
            bean.setPrixTotalAttendu(bean.getPrixTotalRecu());
            YvsComContenuDocAchat en = buildContenuDocAchat(bean);
            en.setDocAchat(doc);
            en.setDateUpdate(new Date());
            if (!update) {
                en.setId(null);
                en = (YvsComContenuDocAchat) dao.save1(en);
                bean.setId(en.getId());
            } else {
                dao.update(en);
            }
            if (s != null) {
                s.saveAllTaxe(en, new DocAchat(doc.getId()), doc);
            }
            contenusDocAchat.add(bean);
            selectContenu.getAchats().add(en);
            contenus.set(contenus.indexOf(selectContenu), selectContenu);
            equilibre(false);
            succes();
            update("data_contenu_fiche_approv");
            update("data_fiche_approv");
            update("data_depot_fiche_approv");
            update("data_art_fsseur_fich_approv");
            return true;
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return false;
        }
    }

    public boolean saveNewContenuDoc(YvsComDocStocks doc, ContenuDocStock bean) {
        try {
            double q = bean.getQuantite();
            boolean update = false;
            List<YvsComContenuDocStock> l = dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{doc});
            if ((l != null) ? !l.isEmpty() : false) {
                ContenuDocStock c_;
                for (YvsComContenuDocStock c : l) {
                    if (c.getArticle().getId().equals(bean.getArticle().getId()) && c.getPrix() == bean.getPrix()) {
                        c_ = UtilCom.buildBeanContenuDocStock(c);
                        double qte = bean.getQuantite();
                        cloneObject(bean, c_);
                        bean.setQuantite(bean.getQuantite() + qte);
                        update = true;
                        break;
                    }
                }
            }
            YvsComContenuDocStock en = buildContenuDocStock(bean);
            en.setDocStock(doc);
            if (!update) {
                en.setId(null);
                en = (YvsComContenuDocStock) dao.save1(en);
                bean.setId(en.getId());
            } else {
                dao.update(en);
            }
            contenusDocStock.add(bean);
            selectContenu.getStocks().add(en);
            contenus.set(contenus.indexOf(selectContenu), selectContenu);
            equilibre(false);
            succes();
            update("data_contenu_fiche_approv");
            update("data_fiche_approv");
            update("data_depot_fiche_approv");
            update("data_art_fsseur_fich_approv");
            return true;
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("appro_delete_fiche")) {
                openNotAcces();
                return;
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComFicheApprovisionnement> list = new ArrayList<>();
                YvsComFicheApprovisionnement bean;
                for (Long ids : l) {
                    bean = documents.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(bean);

                    if (ficheApprovisionnement.getId() == bean.getId()) {
                        resetFiche();
                    }
                }
                documents.removeAll(list);
                succes();
                update("data_fiche_approv");
                update("data_depot_fiche_approv");
                tabIds = "";
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComFicheApprovisionnement y) {
        selectFiche = y;
    }

    public void deleteBean_() {
        try {
            if (!autoriser("appro_delete_fiche")) {
                openNotAcces();
                return;
            }
            if ((selectFiche != null) ? selectFiche.getId() > 0 : false) {
                if (!_controleFiche_(selectFiche)) {
                    return;
                }
                dao.delete(selectFiche);
                documents.remove(selectFiche);
                if (ficheApprovisionnement.getId() == selectFiche.getId()) {
                    resetFiche();
                }
                succes();
                update("data_fiche_approv");
                update("data_depot_fiche_approv");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanArticle() {
        try {
            if (!_controleFiche_(selectFiche)) {
                return;
            }
            if ((tabIds_article != null) ? !tabIds_article.equals("") : false) {
                String[] tab = tabIds_article.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    dao.delete(new YvsComArticleApprovisionnement(id));
                    contenus.remove(new YvsComArticleApprovisionnement(id));
                    if (contenu.getId() == id) {
                        resetFicheArticle();
                    }
                }
                succes();
                update("data_contenu_fiche_approv");
                update("data_fiche_approv");
                update("data_depot_fiche_approv");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanArticle_(YvsComArticleApprovisionnement y) {
        selectContenu = y;
    }

    public void deleteBeanArticle_() {
        try {
            if (!_controleFiche_(selectFiche)) {
                return;
            }
            if (selectContenu != null) {
                dao.delete(selectContenu);
                contenus.remove(selectContenu);
                succes();
                if (contenu.getId() == selectContenu.getId()) {
                    resetFicheArticle();
                }
                update("data_contenu_fiche_approv");
                update("data_fiche_approv");
                update("data_depot_fiche_approv");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComFicheApprovisionnement y = (YvsComFicheApprovisionnement) ev.getObject();
            onnSelectView(y);
            tabIds = documents.indexOf(y) + "";
        }
    }

    public void loadOnViewContent(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComArticleApprovisionnement y = (YvsComArticleApprovisionnement) ev.getObject();
            onSelectObject(y.getFiche());
        }
    }

    @Override
    public void onSelectObject(YvsComFicheApprovisionnement y) {
        onnSelectView(y);
    }

    public void onnSelectView(YvsComFicheApprovisionnement ev) {
        if ((ev != null) ? ev.getId() > 0 : false) {
            ev.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidApprovissionnement.findByFacture", new String[]{"facture"}, new Object[]{ev}));
            selectFiche = ev;
            populateView(UtilCom.buildBeanFicheApprovisionnement(selectFiche));
            if (ev.getDepot() != null ? ev.getDepot().getId() > 0 : false) {
                ManagedTransfertStock s = (ManagedTransfertStock) giveManagedBean(ManagedTransfertStock.class);
                if (s != null) {
                    s.getPaginator().addParam(new ParametreRequete("y.destination", "destination", ev.getDepot(), "=", "AND"));
                    s.loadEditableTransfert(true, true);
                }
                ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (m != null) {
                    m.setEntityDepot(ev.getDepot());
                    m.loadActifArticleByDepot(true, true);
                }
                loadCreneaux(ev.getDepot());
            }
            loadContenu(selectFiche, true, true);
            resetFicheArticle();
            update("blog_form_fiche_approv");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFichePartial();
        update("blog_form_fiche_approv");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleDepot bean = (YvsBaseArticleDepot) ev.getObject();
            chooseArticle(UtilCom.buildBeanArticleDepot(bean));
            listArt = false;
            update("form_contenu_fiche_approv");
            update("desc_artcile_fiche_approv");
        }
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComArticleApprovisionnement bean = (YvsComArticleApprovisionnement) ev.getObject();
            populateViewArticle(UtilCom.buildBeanArticleApprovisionnement(bean));
            update("form_contenu_fiche_approv");
            update("desc_artcile_fiche_approv");
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheArticle();
        update("form_contenu_fiche_approv");
    }

    public void loadOnViewDepot(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            Depots bean = (Depots) ev.getObject();
            cloneObject(ficheApprovisionnement.getDepot(), bean);
            update("select_depot");
        }
    }

    public void loadOnViewBon(SelectEvent ev) {
        if (ev != null && selectBon == null) {
            selectBon = (YvsComDocAchats) ev.getObject();
        }
        double qteWait = (selectContenu.getQteWaitFacture() + selectContenu.getQteWaitTransfert());
        quantitee = selectContenu.getQuantiteRest() - qteWait;
        reste = selectContenu.getQuantiteRest() - qteWait;
        update("txt_quantite_livre_by_bon");
    }

    public void loadOnViewOrdre(SelectEvent ev) {
        if (ev != null && selectOrdre == null) {
            selectOrdre = (YvsComDocStocks) ev.getObject();
        }
        double qteWait = (selectContenu.getQteWaitFacture() + selectContenu.getQteWaitTransfert());
        quantitee = selectContenu.getQuantiteRest() - qteWait;
        reste = selectContenu.getQuantiteRest() - qteWait;
        update("txt_quantite_livre_by_ordre");
    }

    public void chooseDepot(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                creneaux.clear();
                ManagedDepot service = (ManagedDepot) giveManagedBean("managedDepot");
                if (service != null) {
                    YvsBaseDepots bean_ = service.getDepots_all().get(service.getDepots_all().indexOf(new YvsBaseDepots(id)));
                    Depots bean = UtilCom.buildBeanDepot(bean_);
                    ficheApprovisionnement.setDepot(bean);
                }
                loadCreneaux(new YvsBaseDepots(id));
                loadAllFicheByDepotDate();
                ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (m != null) {
                    m.setEntityDepot(new YvsBaseDepots(id));
//                    m.loadActifArticleByDepot(true, true);
                }
//                ManagedTransfertStock s = (ManagedTransfertStock) giveManagedBean(ManagedTransfertStock.class);
//                if (s != null) {
//                    s.getPaginator().addParam(new ParametreRequete("y.destination", "destination", new YvsBaseDepots(id), "=", "AND"));
//                    s.loadEditableTransfert(true, true);
//                }
                update("data_depot_fiche_approv");
                update("article_id");
                update("select_creneau_fiche_approv");
            }
        }
    }

    public void chooseCreneau() {
        if ((ficheApprovisionnement.getCreneau() != null) ? ficheApprovisionnement.getCreneau().getId() > 0 : false) {
            YvsComCreneauDepot c_ = creneaux.get(creneaux.indexOf(new YvsComCreneauDepot(ficheApprovisionnement.getCreneau().getId())));
            Creneau c = UtilCom.buildBeanCreneau(c_);
            cloneObject(ficheApprovisionnement.getCreneau(), c);
        }
    }

    public void chooseArticle() {
        if ((contenu.getArticle() != null) ? contenu.getArticle().getId() > 0 : false) {
            YvsBaseArticleDepot d = ficheApprovisionnement.getDepot().getArticles().get(ficheApprovisionnement.getDepot().getArticles().indexOf(new YvsBaseArticleDepot(contenu.getArticle().getId())));
            ArticleDepot bean = UtilCom.buildBeanArticleDepot(d);
            cloneObject(contenu.getArticle(), bean);
            double qte = bean.getLotLivraison();
            if (qte == 0) {
                qte = (int) (bean.getStockMax() - bean.getQuantiteStock());
            }
            contenu.setQuantite(qte);
        }
    }

    public void chooseDate() {
        loadAllFicheByDepotDate();
        if ((ficheApprovisionnement.getDepot() != null) ? ficheApprovisionnement.getDepot().getId() > 0 : false) {
            loadCreneaux(new YvsBaseDepots(ficheApprovisionnement.getDepot().getId()));
        }
    }

    public void chooseEtat(FicheApprovisionnement fiche) {
        if (!fiche.getEtat().equals("")) {
            String rq = "UPDATE yvs_com_fiche_approvisionnement SET etat = '" + fiche.getEtat() + "' WHERE id=?";
            Options[] param = new Options[]{new Options(fiche.getId(), 1)};
            dao.requeteLibre(rq, param);
            succes();
        }
    }

    public void addParamDepot() {
        ParametreRequete p;
        if (depotSearch > 0) {
            p = new ParametreRequete("y.depot", "depot", new YvsBaseDepots(depotSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.depot", "depot", null);
        }
        paginator.addParam(p);
        loadAllFiche(true, true);
    }

    public void addParmCreneau() {
        ParametreRequete p;
        if (creneauSearch > 0) {
            p = new ParametreRequete("y.creneau.tranche", "creneau", new YvsGrhTrancheHoraire(creneauSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.creneau.tranche", "creneau", null);
        }
        paginator.addParam(p);
        loadAllFiche(true, true);
    }

    public void addParamStatut(ValueChangeEvent ev) {
        String statut = ((String) ev.getNewValue());
        if (statut != null ? statut.trim().equals("Z") : false) {
            openDialog("dlgMoreStatuts");
        } else {
            chooseStatut(statut);
        }
    }

    public void chooseStatut(String statut) {
        statut_ = statut;
        chooseStatut();
    }

    public void chooseStatut() {
        ParametreRequete p = new ParametreRequete("y.etat", "statut", null);
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.etat", "statut", statut_, "=", "AND");
        }
        paginator.addParam(p);
        loadAllFiche(true, true);
    }

    public void chooseStatuts() {
        ParametreRequete p = new ParametreRequete("y.etat", "statut", null);
        if (statuts != null ? !statuts.isEmpty() : false) {
            boolean add = true;
            for (String s : statuts) {
                if (s != null ? s.trim().length() < 1 : true) {
                    add = false;
                    break;
                }
            }
            if (add) {
                p = new ParametreRequete("y.etat", "statut", statuts, "IN", "AND");
            }
        }
        paginator.addParam(p);
        loadAllFiche(true, true);
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("y.cloturer", "cloturer", cloturer_, "=", "AND");
        paginator.addParam(p);
        loadAllFiche(true, true);
    }

    public void addParamTerminer(ValueChangeEvent ev) {
        String terminer = ((String) ev.getNewValue());
        chooseTerminer(terminer);
    }

    public void chooseTerminer(String terminer) {
        terminerSearch = terminer;
        chooseTerminer(true);
    }

    public void chooseTerminer(boolean load) {
        ParametreRequete p = new ParametreRequete("y.statutTerminer", "terminer", null, "=", "AND");
        if (terminerSearch != null ? terminerSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutTerminer", "terminer", terminerSearch, egaliteStatut, "AND");
        }
        paginator.addParam(p);
        if (load) {
            loadAllFiche(true, true);
        }
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllFiche(true, true);
    }

    public void addParamToValide() {
        ParametreRequete p = new ParametreRequete("(y.etapeValide+1)", "etape", null, "IN", "AND");
        if (toValideLoad) {
            List<Integer> lnum = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepe", new String[]{"document", "niveau"}, new Object[]{Constantes.DOCUMENT_APPROVISIONNEMENT, currentUser.getUsers().getNiveauAcces()});
            if ((lnum != null) ? !lnum.isEmpty() : false) {
                p = new ParametreRequete("(y.etapeValide+1)", "etape", lnum, "IN", "AND");
            }
        }
        paginator.addParam(p);
        loadAllFiche(true, true);
    }

    public void chooseDateSearch() {
        ParametreRequete p;
        if (date_) {
            p = new ParametreRequete("y.dateApprovisionnement", "dateApprovisionnement", dateDebut_);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin_);
        } else {
            p = new ParametreRequete("y.dateApprovisionnement", "dateApprovisionnement", null);
        }
        paginator.addParam(p);
        loadAllFiche(true, true);
    }

    public void clearParams() {
        date_ = toValideLoad = false;
        cloturer_ = null;
        dateDebut_ = dateFin_ = null;
        depotSearch = creneauSearch = -1;
        statut_ = terminerSearch = numSearch_ = null;
        paginator.getParams().clear();
        loadAllFiche(true, true);
    }

    public void initGenerationBons(YvsComFicheApprovisionnement y) {

    }

    public void initGenerationBon(YvsComArticleApprovisionnement y) {
        selectContenu = y;
        initGenerationBon();
    }

    public void initGenerationBon() {
        if (!ficheApprovisionnement.getEtat().equals(Constantes.ETAT_SOUMIS)) {
            if (ficheApprovisionnement.getEtat().equals(Constantes.ETAT_VALIDE)) {
                if ((selectContenu != null) ? selectContenu.getId() > 0 : false) {
                    if (selectContenu.isOnAchat()) {
                        //récupère les fournisseurs de l'articles
                        loadArticleFsseurByArticle();
                        //récupère les quantité déjà approvisionné de l'article                        
                        loadBackArticleByArticle();
                        openDialog("dlgListFournisseur");
                        update("data_art_fsseur_fich_approv");
                    } else {
                        getErrorMessage("Cet article n'est pas approvisionné par achat");
                    }
                } else {
                    getErrorMessage("Aucune selection n'a été trouvé !");
                }
            } else {
                getErrorMessage("La fiche doit d'abord être validée");
            }
        } else {
            getErrorMessage("Cette fiche a deja été soumise!");
        }
    }

    public void initGenerationOrdres(YvsComFicheApprovisionnement y) {

    }

    public void initGenerationOrdre(YvsComArticleApprovisionnement y) {
        selectContenu = y;
        initGenerationOrdre();
    }

    public void initGenerationOrdre() {
        if (!ficheApprovisionnement.getEtat().equals(Constantes.ETAT_SOUMIS)) {
            if (ficheApprovisionnement.getEtat().equals(Constantes.ETAT_VALIDE)) {
                if ((selectContenu != null) ? selectContenu.getId() > 0 : false) {
                    if (selectContenu.isOnTransfert()) {
                        loadAllDepotByArticle();
                        openDialog("dlgListDepot");
                    } else {
                        getErrorMessage("Cet article n'est pas approvisionné par transfert");
                    }
                }
            } else {
                getErrorMessage("La fiche doit d'abord être validée");
            }
        } else {
            getErrorMessage("Cette fiche a deja été soumise!");
        }
    }

    public void initTransfertInBon(YvsComArticleApprovisionnement y) {
        selectContenu = y;
        initTransfertInBon();
    }

    public void initTransfertInBon() {
        if (!ficheApprovisionnement.getEtat().equals(Constantes.ETAT_SOUMIS)) {
            if (ficheApprovisionnement.getEtat().equals(Constantes.ETAT_VALIDE)) {
                if (selectContenu.isOnAchat()) {
                    ManagedFactureAchat s = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (s != null) {
                        List<Long> ids = dao.loadNameQueries("YvsBaseArticleFournisseur.findIdFsseurByArticle", new String[]{"article"}, new Object[]{selectContenu.getArticle().getArticle()});
                        if (!ids.isEmpty()) {
                            ParametreRequete p = new ParametreRequete("y.fournisseur.id", "fournisseurs", ids, "IN", "AND");
                            s.getPaginator().addParam(p);
                        } else {
                            s.getPaginator().addParam(new ParametreRequete("y.fournisseur.id", "fournisseurs", null, "IN", "AND"));
                        }
                        s.loadEditableFacture(true, true);
                    }
                    openDialog("dlgListBons");
                    update("data_bon_fiche_approv");
                } else {
                    getErrorMessage("Cet article n'est pas approvisionné par achat");
                }
            } else {
                getErrorMessage("La fiche doit d'abord être validée");
            }
        } else {
            getErrorMessage("Cette fiche a deja été soumise!");
        }
    }

    public void initTransfertInOrdres(YvsComArticleApprovisionnement y) {

    }

    public void initTransfertInOrdre(YvsComArticleApprovisionnement y) {
        selectContenu = y;
        initTransfertInOrdre();
    }

    public void initTransfertInOrdre() {
        if (!ficheApprovisionnement.getEtat().equals(Constantes.ETAT_SOUMIS)) {
            if (ficheApprovisionnement.getEtat().equals(Constantes.ETAT_VALIDE)) {
                if (selectContenu.isOnTransfert()) {
                    ManagedTransfertStock s = (ManagedTransfertStock) giveManagedBean(ManagedTransfertStock.class);
                    if (s != null) {
                        List<Long> ids = dao.loadNameQueries("YvsBaseArticleDepot.findIdDepotByArticle", new String[]{"article"}, new Object[]{selectContenu.getArticle().getArticle()});
                        if (!ids.isEmpty()) {
                            ParametreRequete p = new ParametreRequete("y.source.id", "depots", ids, "IN", "AND");
                            s.getPaginator().addParam(p);
                        } else {
                            s.getPaginator().addParam(new ParametreRequete("y.source.id", "depots", null, "IN", "AND"));
                        }
                        s.getPaginator().addParam(new ParametreRequete("y.destination", "destination", new YvsBaseDepots(ficheApprovisionnement.getDepot().getId()), "=", "AND"));
                        s.loadEditableTransfert(true, true);
                    }
                    openDialog("dlgListOrdres");
                } else {
                    getErrorMessage("Cet article n'est pas approvisionné par transfert");
                }
            } else {
                getErrorMessage("La fiche doit d'abord être validée");
            }
        } else {
            getErrorMessage("Cette fiche a deja été soumise!");
        }
    }

    public void cloneArticleInFsseur(YvsBaseArticleFournisseur bean) {
        selectArticleF = bean;
        id_art_fsseur = bean.getId();
        double qteWait = (selectContenu.getQteWaitFacture() + selectContenu.getQteWaitTransfert());
        quantitee = selectContenu.getQuantiteRest() - qteWait;
        reste = selectContenu.getQuantiteRest() - qteWait;
        selectContenu.setPrixAchat(bean.getPuv());
        back = false;
        update("txt_quantite_livre_by_fsseur");
    }

    public void cloneArticleInBackFsseur(YvsComArticleFourniAchat bean) {
        id_art_fsseur = bean.getId();
        double qteWait = (selectContenu.getQteWaitFacture() + selectContenu.getQteWaitTransfert());
        quantitee = selectContenu.getQuantiteRest() - qteWait;
        reste = selectContenu.getQuantiteRest() - qteWait;
        back = true;
        update("txt_quantite_livre_by_fsseur");
    }

    public void cloneArticleInDepot(YvsBaseArticleDepot bean) {
        id_art_depot = bean.getId();
        double qte = (bean.getQuantiteStock() - bean.getStockMin());
        double qteWait = (selectContenu.getQteWaitFacture() + selectContenu.getQteWaitTransfert());
        if (qte > (selectContenu.getQuantiteRest() - qteWait)) {
            qte = selectContenu.getQuantiteRest() - qteWait;
        }
        if (qte < 0) {
            qte = 0;
        }
        quantitee = qte;
        reste = qte;
        update("txt_quantite_livre_by_depot");
    }

    public void cloturer(YvsComFicheApprovisionnement y) {
        selectFiche = y;
        update("id_confirm_close_fia");
    }

    public void cloturer() {
        if (selectFiche == null) {
            return;
        }
        ficheApprovisionnement.setCloturer(!ficheApprovisionnement.isCloturer());
        selectFiche.setCloturer(ficheApprovisionnement.isCloturer());
        selectFiche.setDateCloturer(ficheApprovisionnement.isCloturer() ? new Date() : null);
        selectFiche.setAuthor(currentUser);
        dao.update(selectFiche);
        if (documents.contains(selectFiche)) {
            documents.set(documents.indexOf(selectFiche), selectFiche);
        }
        update("data_fiche_approv");
    }

    public void annulerOrder(boolean force, boolean passe) {
        try {
            if (selectFiche != null ? selectFiche.getId() > 0 : false) {
                cancelEtapeFacture(force, false, passe);
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            System.err.println("Erreur : " + ex.getMessage());
        }
    }

    public void refuserOrder(boolean force, boolean passe) {
        try {
            if (selectFiche != null ? selectFiche.getId() > 0 : false) {
                cancelEtapeFacture(force, true, passe);
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            System.err.println("Erreur : " + ex.getMessage());
        }
    }

    public void changeStatut(String etat) {
        if (changeStatut_(etat)) {
            succes();
        }
    }

    public boolean changeStatut_(String etat) {
        if (!etat.equals("") && selectFiche != null) {
            if (ficheApprovisionnement.isCloturer()) {
                getErrorMessage("Ce document est vérouillé");
                return false;
            }
            String rq = "UPDATE yvs_com_fiche_approvisionnement SET etat = '" + etat + "', reference = '" + ficheApprovisionnement.getReference() + "' WHERE id=?";
            Options[] param = new Options[]{new Options(ficheApprovisionnement.getId(), 1)};
            dao.requeteLibre(rq, param);
            ficheApprovisionnement.setEtat(etat);
            selectFiche.setEtat(etat);
            selectFiche.setReference(ficheApprovisionnement.getReference());
            if (documents.contains(selectFiche)) {
                documents.set(documents.indexOf(selectFiche), selectFiche);
            }
            update("data_fiche_approv");
            return true;
        }
        return false;
    }

    public void searchByNum() {
        ParametreRequete p;
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.reference", "reference", "%" + numSearch_ + "%");
            p.setOperation(" LIKE ");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.reference", "reference", null);
        }
        paginator.addParam(p);
        loadAllFiche(true, true);
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void chooseArticle(ArticleDepot t) {
        if ((t != null) ? t.getArticle() != null : false) {
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(t.getArticle().getId())});
            System.out.print("unites = " + unites);
            t.getArticle().setStock(dao.stocks(t.getArticle().getId(), 0, ficheApprovisionnement.getDepot().getId(), 0, 0, ficheApprovisionnement.getDateApprovisionnement()));
            t.getArticle().setPuv(dao.getPuv(t.getArticle().getId(), contenu.getQuantite(), 0, 0, ficheApprovisionnement.getDepot().getId(), 0, ficheApprovisionnement.getDateApprovisionnement()));
            t.getArticle().setPua(dao.getPua(t.getArticle().getId(), 0));

            selectArt = true;
            t.getArticle().setConditionnements(unites);
            cloneObject(contenu.getArticle(), t);
        } else {
            contenu.getArticle().setError(true);
        }
    }

    public void searchArticle() {
        String num = contenu.getArticle().getArticle().getRefArt();
        contenu.getArticle().setDesignation("");
        contenu.getArticle().setError(true);
        contenu.getArticle().setId(0);
        listArt = false;
        selectArt = false;
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            if (m.getEntityDepot() == null) {
                m.setEntityDepot(new YvsBaseDepots(ficheApprovisionnement.getDepot().getId()));
            }
            ArticleDepot y = m._searchArticleActifByDepot(num, true);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data_articles_fiche_approv");
                } else {
                    chooseArticle(y);
                }
                contenu.getArticle().setError(false);
            }
            listArt = y.isListArt();
            selectArt = y.isSelectArt();
        }
    }

    public void initArticles() {
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.initArticlesByDepot(contenu.getArticle());
            listArt = contenu.getArticle().isListArt();
            selectArt = contenu.getArticle().isSelectArt();
        }
        update("data_articles_fiche_approv");
    }

    public void equilibre(YvsComFicheApprovisionnement y) {
        selectFiche = y;
        equilibre(true);
    }

    public void equilibre(boolean msg) {
        if ((selectFiche != null) ? selectFiche.getId() > 0 : false) {
            dao.getEquilibreApprovision(selectFiche.getId());
            selectFiche = (YvsComFicheApprovisionnement) dao.loadOneByNameQueries("YvsComFicheApprovisionnement.findById", new String[]{"id"}, new Object[]{selectFiche.getId()});
            if (!terminerSearch.equals(Constantes.ETAT_SOUMIS) && selectFiche.getStatutTerminer().equals(Constantes.ETAT_SOUMIS)) {
                documents.remove(selectFiche);
            } else {
                int idx = documents.indexOf(selectFiche);
                if (idx > -1) {
                    documents.set(idx, selectFiche);
                }
            }
            update("data_fiche_approv");
            if (msg) {
                succes();
            }
        }
    }

    /*
     DEBUT WORKFLOW
     */
    private YvsWorkflowValidApprovissionnement currentEtape;

    private YvsWorkflowValidApprovissionnement giveEtapeActuelle(List<YvsWorkflowValidApprovissionnement> etapes) {
        if (!etapes.isEmpty()) {
            List<YvsWorkflowValidApprovissionnement> l = ordonneEtapes(etapes);
            int i = 0;
            for (YvsWorkflowValidApprovissionnement e : l) {
                if (e.getEtapeValid()) {
                    if (l.size() > (i + 1)) {
                        if (!l.get(i + 1).getEtapeValid()) {
                            return e;
                        }
                    }
                }
                i++;
            }
            return l.get(0);
        } else {
            return null;
        }
    }

    private List<YvsWorkflowEtapeValidation> saveEtapesValidation() {
        champ = new String[]{"titre", "societe"};
        val = new Object[]{Constantes.DOCUMENT_APPROVISIONNEMENT, currentAgence.getSociete()};
        return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", champ, val);
    }

    private List<YvsWorkflowValidApprovissionnement> saveEtapesValidation(YvsComFicheApprovisionnement m, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidApprovissionnement> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidApprovissionnement vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    champ = new String[]{"facture", "etape"};
                    val = new Object[]{m, et};
                    YvsWorkflowValidApprovissionnement w = (YvsWorkflowValidApprovissionnement) dao.loadOneByNameQueries("YvsWorkflowValidApprovissionnement.findByEtapeFacture", champ, val);
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidApprovissionnement();
                        vm.setAuthor(currentUser);
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setDocument(m);
                        vm.setOrdreEtape(et.getOrdreEtape());
                        vm = (YvsWorkflowValidApprovissionnement) dao.save1(vm);
                        re.add(vm);
                    }
                }
            }
        }
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidApprovissionnement> ordonneEtapes(List<YvsWorkflowValidApprovissionnement> l) {
        return YvsWorkflowValidApprovissionnement.ordonneEtapes(l);
    }

    public void motifEtapeFacture(String motifEtape, boolean lastEtape) {
        this.motifEtape = motifEtape;
        this.lastEtape = lastEtape;
    }

    public void annulEtapeFacture(YvsWorkflowValidApprovissionnement etape, boolean lastEtape) {
        this.etape = etape;
        this.lastEtape = lastEtape;
        this.motifEtape = null;
    }

    public boolean annulEtapeFacture() {
        return annulEtapeFacture(selectFiche, ficheApprovisionnement, currentUser, etape, lastEtape, motifEtape);
    }

    public boolean annulEtapeFacture(YvsComFicheApprovisionnement current, FicheApprovisionnement fiche, YvsUsersAgence users, YvsWorkflowValidApprovissionnement etape, boolean lastEtape, String motif) {
        if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                getErrorMessage("Vous devez précisez le motif");
                return false;
            }
            if (current != null ? (current.getId() != null ? current.getId() < 1 : true) : true) {
                current = (YvsComFicheApprovisionnement) dao.loadOneByNameQueries("YvsComFicheApprovisionnement.findById", new String[]{"id"}, new Object[]{fiche.getId()});
            }
            if (fiche != null ? fiche.getId() < 1 : true) {
                fiche = UtilCom.buildBeanFicheApprovisionnement(current);
            }
            int idx = fiche.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtapeSuivante().getEtape(), current};
                    YvsWorkflowValidApprovissionnement y = (YvsWorkflowValidApprovissionnement) dao.loadOneByNameQueries("YvsWorkflowValidApprovissionnement.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        getErrorMessage("Vous devez au préalable annuler l'étape suivante");
                        return false;
                    }
                }
                etape.setAuthor(users);
                etape.setEtapeValid(false);
                etape.setEtapeActive(true);
                etape.setMotif(motif);
                dao.update(etape);

                current.setEtapeValide(current.getEtapeValide() - 1);
                current.setEtat(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                dao.update(current);

                fiche.setEtat(current.getEtat());
                fiche.setEtapeValide(current.getEtapeValide());
                if (documents != null ? documents.contains(current) : false) {
                    int idx_ = documents.indexOf(current);
                    documents.get(idx_).setEtapeValide(current.getEtapeValide());
                    documents.get(idx_).setEtat(current.getEtat());
                }
                getInfoMessage("Annulation effectué avec succès !");
                update("tabview_facture_achat");
                return true;
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        }
        return false;
    }

    public void validEtapeFacture(YvsWorkflowValidApprovissionnement etape, boolean lastEtape) {
        //vérifier que la personne qui valide l'étape a le droit 
        if (!asDroitValideEtape(etape.getEtape())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            YvsComFicheApprovisionnement current = (YvsComFicheApprovisionnement) dao.loadOneByNameQueries("YvsComFicheApprovisionnement.findById", new String[]{"id"}, new Object[]{ficheApprovisionnement.getId()});
            int idx = ficheApprovisionnement.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                current.setAuthor(currentUser);
                current.setDateUpdate(new Date());
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() == null) {
                    if (changeStatut_(Constantes.ETAT_VALIDE)) {
                        etape.setAuthor(currentUser);
                        etape.setEtapeValid(true);
                        etape.setMotif(null);
                        etape.setEtapeActive(false);
                        if (ficheApprovisionnement.getEtapesValidations().size() > (idx + 1)) {
                            ficheApprovisionnement.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                        }
                        dao.update(etape);

                        current.setEtat(Constantes.ETAT_VALIDE);
                        current.setEtapeValide(current.getEtapeValide() + 1);
                        current.setEtapeTotal(ficheApprovisionnement.getEtapesValidations().size());
                        ficheApprovisionnement.setEtat(current.getEtat());
                        ficheApprovisionnement.setEtapeValide(current.getEtapeValide());
                        ficheApprovisionnement.setEtapeTotal(current.getEtapeTotal());
                        dao.update(current);
                        if (documents.contains(current)) {
                            int idx_ = documents.indexOf(current);
                            documents.get(idx_).setEtapeValide(current.getEtapeValide());
                            documents.get(idx_).setEtat(current.getEtat());
                        }
                        succes();
                    }
                } else {
                    etape.setAuthor(currentUser);
                    etape.setEtapeValid(true);
                    etape.setMotif(null);
                    etape.setEtapeActive(false);
                    if (ficheApprovisionnement.getEtapesValidations().size() > (idx + 1)) {
                        ficheApprovisionnement.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    dao.update(etape);
                    current.setEtat(Constantes.ETAT_ENCOURS);
                    current.setAuthor(currentUser);
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    current.setEtapeTotal(ficheApprovisionnement.getEtapesValidations().size());
                    dao.update(current);

                    ficheApprovisionnement.setEtat(Constantes.ETAT_ENCOURS);
                    ficheApprovisionnement.setEtapeValide(current.getEtapeValide());
                    ficheApprovisionnement.setEtapeTotal(current.getEtapeTotal());
                    if (documents.contains(current)) {
                        int idx_ = documents.indexOf(current);
                        documents.get(idx_).setEtapeValide(current.getEtapeValide());
                        documents.get(idx_).setEtat(current.getEtat());
                    }
                    getInfoMessage("Validation effectué avec succès !");
                }
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        }
    }

    public void cancelEtapeFacture(boolean force, boolean suspend, boolean passe) {
        //vérifie le droit
        if (!autoriser("appro_cancel_fiche")) {
            openNotAcces();
            return;
        }
        suspend = selectFiche.getEtat().equals(Constantes.ETAT_ANNULE) ? false : suspend;
        if (selectFiche != null ? selectFiche.getId() > 0 : false) {
            //annule toute les validation acquise
            int i = 0;
            boolean update = force;
            if (!force) {
                for (YvsWorkflowValidApprovissionnement vm : ficheApprovisionnement.getEtapesValidations()) {
                    //si on trouve une étape non valide (on ne peut annuler un ordre de ficheApprovisionnement complètement valide)
                    if (!vm.getEtapeValid()) {
                        update = true;
                    } else {
                        //ais-je un droit de validation pour cet étape?
                        if (!asDroitValideEtape(vm.getEtape().getAutorisations(), currentUser.getUsers().getNiveauAcces())) {
                            getErrorMessage("Vous ne pouvez annuler cette facture ! Elle requière un niveau suppérieur");
                            return;
                        }
                    }
                }
            }
            if (update) {
                for (YvsWorkflowValidApprovissionnement vm : ficheApprovisionnement.getEtapesValidations()) {
                    vm.setEtapeActive(false);
                    if (i == 0) {
                        vm.setEtapeActive(true);
                    }
                    vm.setAuthor(currentUser);
                    vm.setEtapeValid(false);
                    dao.update(vm);
                    i++;
                }
            } else if (!ficheApprovisionnement.getEtapesValidations().isEmpty()) {
                openDialog(suspend ? "dlgConfirmRefuserForcer" : "dlgConfirmAnnulerForcer");
                return;
            }
            //désactive la ficheApprovisionnement
            if (changeStatut_((suspend) ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE)) {
                selectFiche.setCloturer(false);
                selectFiche.setDateCloturer(null);
                selectFiche.setAuthor(currentUser);
                selectFiche.setEtapeValide(0);
                ficheApprovisionnement.setEtapeValide(0);
                dao.update(selectFiche);
            }
        }
    }
    /*
     FIN WORKFLOW
     */

    public void addParamReferenceContenu() {
        ParametreRequete p = new ParametreRequete("y.fiche.reference", "reference", null);
        if (reference != null ? reference.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", reference + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fiche.reference)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamStatutContenu() {
        ParametreRequete p = new ParametreRequete("y.fiche.statut", "statut", null);
        if (statutContenu != null ? statutContenu.trim().length() > 0 : false) {
            p = new ParametreRequete("y.fiche.etat", "statut", statutContenu, "=", "AND");
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamArticleContenu() {
        ParametreRequete p = new ParametreRequete("y.article.refArt", "article", null);
        if (article != null ? article.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "article", article + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.article.refArt)", "article", article.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.article.designation)", "article", article.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamDepotContenu() {
        ParametreRequete p = new ParametreRequete("y.fiche.depot.code", "depot", null);
        if (depot != null ? depot.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "depot", depot + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fiche.depot.code)", "depot", depot.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fiche.depot.designation)", "depot", depot.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamDateContenu(SelectEvent ev) {
        findByDateContenu();
    }

    public void findByDateContenu() {
        ParametreRequete p = new ParametreRequete("y.docAchat.dateDoc", "date", null, "BETWEEN", "AND");
        if (dateContenu) {
            if (dateDebutContenu != null && dateFinContenu != null) {
                if (dateDebutContenu.before(dateFinContenu) || dateDebutContenu.equals(dateFinContenu)) {
                    p = new ParametreRequete(null, "date", dateDebutContenu, dateFinContenu, "BETWEEN", "AND");
                    p.getOtherExpression().add(new ParametreRequete("y.fiche.dateApprovisionnement", "date", dateDebutContenu, dateFinContenu, "BETWEEN", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("y.fiche.dateSave", "date", dateDebutContenu, dateFinContenu, "BETWEEN", "OR"));
                }
            }
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamQuantiteContenu() {
        ParametreRequete p = new ParametreRequete("y.quantite", "quantite", null, "=", "AND");
        if (addPrix) {
            if (comparer.equals("BETWEEN")) {
                if (prixSearch <= prix2Search) {
                    p = new ParametreRequete("y.quantite", "quantite", prixSearch, prix2Search, comparer, "AND");
                }
            } else {
                double prix = comparer.equals(Constantes.SYMBOLE_SUP_EGALE) ? prixSearch : prix2Search;
                p = new ParametreRequete("y.quantite", "quantite", prix, comparer, "AND");
            }
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllFiche(true, true);
    }
}
