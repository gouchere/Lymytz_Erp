/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.primefaces.component.selectcheckboxmenu.SelectCheckboxMenu;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.commercial.depot.ArticleDepot;
import yvs.base.produits.Articles;
import yvs.base.produits.ManagedArticles;
import yvs.base.produits.ManagedUniteMesure;
import yvs.production.UtilProd;
import yvs.commercial.UtilCom;
import yvs.commercial.ManagedCommercial;
import static yvs.commercial.ManagedCommercial.currentParam;
import yvs.commercial.achat.DocAchat;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.vente.DocVente;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseMouvementStock;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.ration.YvsComRation;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComContenuDocStockReception;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.production.YvsProdParametre;
import yvs.entity.production.pilotage.YvsProdContenuConditionnement;
import yvs.entity.production.pilotage.YvsProdDeclarationProduction;
import yvs.entity.production.pilotage.YvsProdOfSuiviFlux;
import yvs.entity.produits.YvsBaseArticles;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.production.ManagedOrdresF;
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
public class ManagedValorisation extends ManagedCommercial<MouvementStock, YvsBaseMouvementStock> implements Serializable {

    @ManagedProperty(value = "#{mouvementStock}")
    private MouvementStock mouvementStock;
    private MouvementStock lastMouv = new MouvementStock();
    private MouvementStock lineMvt = new MouvementStock();
    private YvsBaseMouvementStock selectMouvement;
    private List<YvsBaseMouvementStock> mouvements, valorisations, historiques, selections;
    private PaginatorResult<YvsBaseMouvementStock> p_historique = new PaginatorResult<>();
    private PaginatorResult<YvsBaseMouvementStock> p_audit = new PaginatorResult<>();
    private int max = 10;

    private List<YvsGrhTrancheHoraire> tranches;

    private String element = Constantes.TRANSFERT;
    private YvsComContenuDocStock selectTransfert;
    private List<YvsComContenuDocStock> transferts;
    private PaginatorResult<YvsComContenuDocStock> p_transfert = new PaginatorResult<>();
    private YvsProdDeclarationProduction selectDeclaration;
    private List<YvsProdDeclarationProduction> declarations;
    private PaginatorResult<YvsProdDeclarationProduction> p_declaration = new PaginatorResult<>();
    private String articleSearch, orderByPrint = "code";
    private long depotsSearch, emplacementSearch;
    private Boolean dateSearch;
    private Date debutSearch = new Date(), finSearch = new Date();

    private String meth = Constantes.FIFO;
    private boolean listArt, displaySelection = false;

    private long trhSearch, uniteSearch, depotSearch, articlesSearch, conditSearch;
    private String artSearch = "", opeSearch, selectArticle, typeSearch, numeroSearch, lotSearch;
    private Boolean mouvSearch;

    String query = "select y.* from do_audit(?, ?, ?, ?, ?, 'S') y inner join yvs_base_articles a on y.element::bigint = a.id inner join yvs_base_depots d on y.depot = d.id";
    private List<ArticleDepot> audits;
    private ArticleDepot selectAudit = new ArticleDepot();

    private double prevStock = 0;
    private double stockInit = 0;

    private final String Reference = "Reference de l'article", Designation = "Designation de l'article", Date = "Date du mouvement", Depot = "Depot du mouvement", Tranche = "Tranche du mouvement", Operation = "Operation du mouvement", Lot = "Lot de reception",
            CoutEntree = "Cout d'entree", CoutMoyen = "Cout moyen", Quantite = "Quantit'e", Stock = "Stock", CoutStock = "Cout du stock", NumDoc = "Numéro du document";

    private String[] columnsName = new String[]{Reference, Designation, Date, Depot, Tranche, Operation, Lot, CoutEntree, CoutMoyen, Quantite, Stock, CoutStock, NumDoc};
    private List<String> columnsDisplay = new ArrayList<>(Arrays.asList(
            Reference,
            Designation,
            Date,
            Depot,
            Tranche,
            Lot,
            Operation,
            CoutEntree,
            CoutMoyen,
            Quantite,
            Stock,
            CoutStock
    ));    

    private SelectItem[] paginations = {
        new SelectItem((int) 0, "@"),
        new SelectItem((int) 5, "5"),
        new SelectItem((int) 10, "10"),
        new SelectItem((int) 15, "15"),
        new SelectItem((int) 25, "25"),
        new SelectItem((int) 50, "50"),
        new SelectItem((int) 100, "100"),
        new SelectItem((int) 150, "150"),
        new SelectItem((int) 200, "200"),
        new SelectItem((int) 500, "500"),
        new SelectItem((int) 1000, "1000"),
        new SelectItem((int) 5000, "1000+")
    };

    public ManagedValorisation() {
        mouvements = new ArrayList<>();
        transferts = new ArrayList<>();
        declarations = new ArrayList<>();
        audits = new ArrayList<>();
        valorisations = new ArrayList<>();
        tranches = new ArrayList<>();
        historiques = new ArrayList<>();
        selections = new ArrayList<>();
    }

    @Override
    public SelectItem[] getPaginations() {
        return paginations; //To change body of generated methods, choose Tools | Templates.
    }

    public String[] getColumnsName() {
        return columnsName;
    }

    public void setColumnsName(String[] columnsName) {
        this.columnsName = columnsName;
    }

    public List<String> getColumnsDisplay() {
        return columnsDisplay;
    }

    public void setColumnsDisplay(List<String> columnsDisplay) {
        this.columnsDisplay = columnsDisplay;
    }

    public boolean isDisplayLot() {
        return columnsDisplay.contains(Lot);
    }

    public boolean isDisplayReference() {
        return columnsDisplay.contains(Reference);
    }

    public boolean isDisplayDesignation() {
        return columnsDisplay.contains(Designation);
    }

    public boolean isDisplayDate() {
        return columnsDisplay.contains(Date);
    }

    public boolean isDisplayDepot() {
        return columnsDisplay.contains(Depot);
    }

    public boolean isDisplayTranche() {
        return columnsDisplay.contains(Tranche);
    }

    public boolean isDisplayOperation() {
        return columnsDisplay.contains(Operation);
    }

    public boolean isDisplayCoutEntree() {
        return columnsDisplay.contains(CoutEntree);
    }

    public boolean isDisplayCoutMoyen() {
        return columnsDisplay.contains(CoutMoyen);
    }

    public boolean isDisplayQuantite() {
        return columnsDisplay.contains(Quantite);
    }

    public boolean isDisplayStock() {
        return columnsDisplay.contains(Stock);
    }

    public boolean isDisplayCoutStock() {
        return columnsDisplay.contains(CoutStock);
    }

    public boolean isDisplayNumDoc() {
        return columnsDisplay.contains(NumDoc);
    }

    public List<YvsBaseMouvementStock> getSelections() {
        return selections;
    }

    public void setSelections(List<YvsBaseMouvementStock> selections) {
        this.selections = selections;
    }

    public boolean isDisplaySelection() {
        return displaySelection;
    }

    public void setDisplaySelection(boolean displaySelection) {
        this.displaySelection = displaySelection;
    }

    public String getSelectArticle() {
        return selectArticle;
    }

    public void setSelectArticle(String selectArticle) {
        this.selectArticle = selectArticle;
    }

    public String getLotSearch() {
        return lotSearch;
    }

    public void setLotSearch(String lotSearch) {
        this.lotSearch = lotSearch;
    }

    public String getOrderByPrint() {
        return orderByPrint;
    }

    public void setOrderByPrint(String orderByPrint) {
        this.orderByPrint = orderByPrint;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public String getNumeroSearch() {
        return numeroSearch;
    }

    public void setNumeroSearch(String numeroSearch) {
        this.numeroSearch = numeroSearch;
    }

    public long getEmplacementSearch() {
        return emplacementSearch;
    }

    public void setEmplacementSearch(long emplacementSearch) {
        this.emplacementSearch = emplacementSearch;
    }

    public YvsBaseMouvementStock getSelectMouvement() {
        return selectMouvement;
    }

    public void setSelectMouvement(YvsBaseMouvementStock selectMouvement) {
        this.selectMouvement = selectMouvement;
    }

    public long getConditSearch() {
        return conditSearch;
    }

    public void setConditSearch(long conditSearch) {
        this.conditSearch = conditSearch;
    }

    public long getArticlesSearch() {
        return articlesSearch;
    }

    public void setArticlesSearch(long articlesSearch) {
        this.articlesSearch = articlesSearch;
    }

    public String getOpeSearch() {
        return opeSearch;
    }

    public void setOpeSearch(String opeSearch) {
        this.opeSearch = opeSearch;
    }

    public YvsComContenuDocStock getSelectTransfert() {
        return selectTransfert;
    }

    public void setSelectTransfert(YvsComContenuDocStock selectTransfert) {
        this.selectTransfert = selectTransfert;
    }

    public YvsProdDeclarationProduction getSelectDeclaration() {
        return selectDeclaration;
    }

    public void setSelectDeclaration(YvsProdDeclarationProduction selectDeclaration) {
        this.selectDeclaration = selectDeclaration;
    }

    public String getArticleSearch() {
        return articleSearch;
    }

    public void setArticleSearch(String articleSearch) {
        this.articleSearch = articleSearch;
    }

    public long getDepotsSearch() {
        return depotsSearch;
    }

    public void setDepotsSearch(long depotsSearch) {
        this.depotsSearch = depotsSearch;
    }

    public Boolean getDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(Boolean dateSearch) {
        this.dateSearch = dateSearch;
    }

    public Date getDebutSearch() {
        return debutSearch;
    }

    public void setDebutSearch(Date debutSearch) {
        this.debutSearch = debutSearch;
    }

    public Date getFinSearch() {
        return finSearch;
    }

    public void setFinSearch(Date finSearch) {
        this.finSearch = finSearch;
    }

    public List<YvsComContenuDocStock> getTransferts() {
        return transferts;
    }

    public void setTransferts(List<YvsComContenuDocStock> transferts) {
        this.transferts = transferts;
    }

    public List<YvsProdDeclarationProduction> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<YvsProdDeclarationProduction> declarations) {
        this.declarations = declarations;
    }

    public String getElement() {
        return element != null ? element.trim().length() > 0 ? element : Constantes.TRANSFERT : Constantes.TRANSFERT;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public PaginatorResult<YvsComContenuDocStock> getP_transfert() {
        return p_transfert;
    }

    public void setP_transfert(PaginatorResult<YvsComContenuDocStock> p_transfert) {
        this.p_transfert = p_transfert;
    }

    public PaginatorResult<YvsProdDeclarationProduction> getP_declaration() {
        return p_declaration;
    }

    public void setP_declaration(PaginatorResult<YvsProdDeclarationProduction> p_declaration) {
        this.p_declaration = p_declaration;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public long getUniteSearch() {
        return uniteSearch;
    }

    public void setUniteSearch(long uniteSearch) {
        this.uniteSearch = uniteSearch;
    }

    public double getPrevStock() {
        return prevStock;
    }

    public void setPrevStock(double prevStock) {
        this.prevStock = prevStock;
    }

    public double getStockInit() {
        return stockInit;
    }

    public void setStockInit(double stockInit) {
        this.stockInit = stockInit;
    }

    public ArticleDepot getSelectAudit() {
        return selectAudit;
    }

    public void setSelectAudit(ArticleDepot selectAudit) {
        if (!selectAudit.getAudits().contains(selectAudit)) {
            selectAudit.getAudits().add(selectAudit);
        }
        this.selectAudit = selectAudit;
    }

    public List<ArticleDepot> getAudits() {
        return audits;
    }

    public void setAudits(List<ArticleDepot> audits) {
        this.audits = audits;
    }

    public PaginatorResult<YvsBaseMouvementStock> getP_audit() {
        return p_audit;
    }

    public void setP_audit(PaginatorResult<YvsBaseMouvementStock> p_audit) {
        this.p_audit = p_audit;
    }

    public MouvementStock getLineMvt() {
        return lineMvt;
    }

    public void setLineMvt(MouvementStock lineMvt) {
        this.lineMvt = lineMvt;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public PaginatorResult<YvsBaseMouvementStock> getP_historique() {
        return p_historique;
    }

    public void setP_historique(PaginatorResult<YvsBaseMouvementStock> p_historique) {
        this.p_historique = p_historique;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public long getTrhSearch() {
        return trhSearch;
    }

    public void setTrhSearch(long trhSearch) {
        this.trhSearch = trhSearch;
    }

    public String getArtSearch() {
        return artSearch;
    }

    public void setArtSearch(String artSearch) {
        this.artSearch = artSearch;
    }

    public Boolean getMouvSearch() {
        return mouvSearch;
    }

    public void setMouvSearch(Boolean mouvSearch) {
        this.mouvSearch = mouvSearch;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public List<YvsGrhTrancheHoraire> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsGrhTrancheHoraire> tranches) {
        this.tranches = tranches;
    }

    public MouvementStock getLastMouv() {
        return lastMouv;
    }

    public void setLastMouv(MouvementStock lastMouv) {
        this.lastMouv = lastMouv;
    }

    public String getMeth() {
        return meth;
    }

    public void setMeth(String meth) {
        this.meth = meth;
    }

    public List<YvsBaseMouvementStock> getValorisations() {
        return valorisations;
    }

    public void setValorisations(List<YvsBaseMouvementStock> valorisations) {
        this.valorisations = valorisations;
    }

    public MouvementStock getMouvementStock() {
        return mouvementStock;
    }

    public void setMouvementStock(MouvementStock mouvementStock) {
        this.mouvementStock = mouvementStock;
    }

    public List<YvsBaseMouvementStock> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<YvsBaseMouvementStock> mouvements) {
        this.mouvements = mouvements;
    }

    public List<YvsBaseMouvementStock> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsBaseMouvementStock> historiques) {
        this.historiques = historiques;
    }

    public void actualiser() {
        try {
//            if (!autoriser("gescom_mouv_delete")) {
//                openNotAcces();
//                return;
//            }
            if (selectMouvement != null ? selectMouvement.getId() > 0 : false) {
                selectMouvement.setAuthor(currentUser);
                dao.delete(selectMouvement);
                mouvements.remove(selectMouvement);
                changeCalculPr(selectMouvement, selectMouvement.getCalculPr());
                succes();
                update("data_mouvement");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ManagedValorisation (deleteBean)", ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("gescom_mouv_delete")) {
                openNotAcces();
                return;
            }
            if (selectMouvement != null ? selectMouvement.getId() > 0 : false) {
                selectMouvement.setAuthor(currentUser);
                dao.delete(selectMouvement);
                mouvements.remove(selectMouvement);
                deleteSource(selectMouvement);
                succes();
                update("data_mouvement");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ManagedValorisation (deleteBean)", ex);
        }
    }

    @Override
    public void populateView(MouvementStock bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void initView() {
        loadInfosWarning(false);
        if (depotSearch < 1) {
            depotSearch = currentDepot != null ? currentDepot.getId() : 0;
            addParamDepot(false);
        }
        if (trhSearch < 1) {
            if (currentPlanning != null ? !currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false : false) {
                trhSearch = currentPlanning.get(0).getCreneauDepot().getTranche() != null ? currentPlanning.get(0).getCreneauDepot().getTranche().getId() : 0;
            }
            addParamTranche(false);
        }
    }

    @Override
    public void loadAll() {
        initView();
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {

        }
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramCom", currentParam);
        }
        if (paramProduction == null) {
            paramProduction = (YvsProdParametre) dao.loadOneByNameQueries("YvsProdParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        if (paramCommercial == null) {
            paramCommercial = currentParam;
        }
        if (agence_ < 1) {
            agence_ = currentAgence.getId();
        }
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllMouvement(true, true);
    }

    public void loadAllTransfert(boolean avance, boolean init) {
        if (currentUser != null ? currentUser.getUsers() != null : false) {
            switch (buildDocByDroit(Constantes.TYPE_FT)) {
                case 1:  //charge tous les documents de la société
                    p_transfert.addParam(new ParametreRequete("y.docStock.destination.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
                    break;
                case 2: //charge tous les documents de l'agence
                    p_transfert.addParam(new ParametreRequete("y.docStock.destination.agence", "agence", currentAgence, "=", "AND"));
                    break;
                case 3: //charge tous les document des points de vente où l'utilisateurs est responsable                    
                    List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                    if (ids.isEmpty()) {
                        ids.add(-1L);
                    }
                    p_transfert.addParam(new ParametreRequete("y.docStock.destination.id", "depots", ids, "IN", "AND"));
                    break;
                default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                    p_transfert.addParam(new ParametreRequete("y.author.users ", "users", currentUser.getUsers(), "=", "AND"));
                    break;

            }
            p_transfert.addParam(new ParametreRequete("y.statut", "statut", Constantes.ETAT_VALIDE, "!=", "AND"));
            p_transfert.addParam(new ParametreRequete("y.docStock.statut", "statut", Constantes.ETAT_VALIDE, "!=", "AND"));
            p_transfert.addParam(new ParametreRequete("y.docStock.typeDoc", "type", Constantes.TYPE_FT, "=", "AND"));
            transferts = p_transfert.executeDynamicQuery("YvsComContenuDocStock", "y.docStock.dateDoc", avance, init, dao);
        }
    }

    public void loadAllDeclaration(boolean avance, boolean init) {
        if (currentUser != null ? currentUser.getUsers() != null : false) {
            switch (buildDocByDroit(Constantes.TYPE_FT)) {
                case 1:  //charge tous les documents de la société
                    p_declaration.addParam(new ParametreRequete("y.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
                    break;
                case 2: //charge tous les documents de l'agence
                    p_declaration.addParam(new ParametreRequete("y.depot.agence", "agence", currentAgence, "=", "AND"));
                    break;
                case 3: //charge tous les document des points de vente où l'utilisateurs est responsable                    
                    List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                    if (ids.isEmpty()) {
                        ids.add(-1L);
                    }
                    p_declaration.addParam(new ParametreRequete("y.depot.id", "depots", ids, "IN", "AND"));
                    break;
                default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                    p_declaration.addParam(new ParametreRequete("y.author.users ", "users", currentUser.getUsers(), "=", "AND"));
                    break;

            }
            p_declaration.addParam(new ParametreRequete("y.statut", "statut", Constantes.STATUT_DOC_VALIDE, "!=", "AND"));
            declarations = p_declaration.executeDynamicQuery("YvsProdDeclarationProduction", "y.dateDeclaration", avance, init, dao);
        }
    }

    public void loadAllMouvement(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_IN)) {
            case 1:  //charge tous les documents de la société
                paginator.addParam(new ParametreRequete("y.depot.agence.societe", "societe", currentAgence.getSociete(), "IN", "AND"));
                break;
            case 2: //charge tous les documents de l'agence
                controlListAgence();
                paginator.addParam(new ParametreRequete("y.depot.agence.id", "agences", listIdAgences, "IN", "AND"));
                break;
            case 3: //charge tous les document des points de vente où l'utilisateurs est responsable
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                if (!ids.isEmpty()) {
                    p = new ParametreRequete("y.depot.id", "depots", ids, " IN ", "AND");
                    paginator.addParam(p);
                } else {
                    paginator.getParams().clear();
                }
                break;
            default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                p = new ParametreRequete(null, "responsable", currentUser.getUsers().getEmploye(), "=", "AND");
                p.getOtherExpression().add(new ParametreRequete("y.depot.responsable", "responsable", currentUser.getUsers().getEmploye(), "=", "OR"));
                p.getOtherExpression().add(new ParametreRequete("y.depot", "current_depot", currentDepot, "=", "OR"));
                paginator.addParam(p);
                break;

        }
        int index = paginator.getParams().indexOf(new ParametreRequete("date"));
        if (index < 0) {
            getErrorMessage("Vous devez mettre une plage de date");
            return;
        }
        paginator.addParam(new ParametreRequete("y.depot.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        mouvements = paginator.executeDynamicQuery("y", "y", "YvsBaseMouvementStock y LEFT JOIN FETCH y.depot LEFT JOIN FETCH y.tranche "
                + "JOIN FETCH y.article JOIN FETCH y.conditionnement JOIN FETCH y.conditionnement.unite LEFT JOIN FETCH y.lot", "y.article.refArt, COALESCE(y.dateDoc, current_date), y.mouvement, y.id", avance, init, (int) imax, dao);
        calculPrevStock();
        update("data_mouvement");
    }

    public void calculPrevStock() {
        prevStock = giveStockInit(!mouvements.isEmpty() ? mouvements.get(0) : null);
    }

    public double previousStock(int index) {
//        if (index > 0) {
//            prevStock += (mouvements.get(index - 1).getMouvement().equals("E")) ? mouvements.get(index - 1).getQuantite() : -mouvements.get(index - 1).getQuantite();
        return prevStock += (mouvements.get(index).getMouvement().equals("E")) ? mouvements.get(index).getQuantite() : -mouvements.get(index).getQuantite();
//        }
    }

    public double giveStockInit(YvsBaseMouvementStock mv) {
        if (mv != null) {
            Calendar c = null;
//        Date d=(dateDebut_!=null && date_)?dateDebut_:mv.getDateDoc();
            Date d = (mv != null) ? mv.getDateDoc() : (dateDebut_ != null) ? dateDebut_ : new Date();
            if (d != null) {
                c = Calendar.getInstance();
                c.setTime(d);
                c.add(Calendar.DAY_OF_MONTH, -1);
            }
            if (c != null) {
                if (lotSearch != null ? lotSearch.trim().length() > 0 : false) {
                    String query = "SELECT DISTINCT m.lot FROM yvs_base_mouvement_stock_lot m INNER JOIN yvs_com_lot_reception r ON m.lot = r.id INNER JOIN yvs_agences a ON r.agence = a.id WHERE UPPER(r.numero) LIKE ? AND a.societe = ?";
                    List<Long> lots = dao.loadListBySqlQuery(query, new Options[]{new Options(lotSearch.toUpperCase() + "%", 1), new Options(currentAgence.getSociete().getId(), 2)});
                    stockInit = 0;
                    Double st = 0D;
                    for (Long lot : lots) {
                        st = dao.stocksReel(mv.getArticle().getId(), 0, mv.getDepot().getId(), 0, 0, c.getTime(), (mv.getConditionnement() != null ? mv.getConditionnement().getId() : 0), lot);
                        if (st != null ? st > 0 : false) {
                            stockInit += (st != null ? st : 0);
                        }
                    }
                } else {
                    stockInit = dao.stocksReel(mv.getArticle().getId(), 0, mv.getDepot().getId(), 0, 0, c.getTime(), (mv.getConditionnement() != null ? mv.getConditionnement().getId() : 0), 0);
                }
                return stockInit;
            } else {
                return 0;
            }
        }
        return 0;
    }

    private void loadAllTranche(YvsBaseDepots y) {
        mouvementStock.setTranche(new TrancheHoraire());
        tranches = loadTranche(y, mouvementStock.getDateDoc());
        update("select_tranche");
    }

    public void loadHistorique(boolean avance, boolean init) {
        historiques.clear();
        if (p_historique.getParams().contains(new ParametreRequete("depot"))) {
            historiques = p_historique.executeDynamicQuery("YvsBaseMouvementStock", "y.dateDoc DESC, y.article.refArt, y.id", avance, init, (int) getMax(), dao);
        }
        update("data_mouvement_hist");
    }

    @Override
    public MouvementStock recopieView() {
        MouvementStock m = new MouvementStock();
        m.setArticle(mouvementStock.getArticle());
        m.setDepot(mouvementStock.getDepot());
        m.setQuantite(mouvementStock.getQuantite());
        return m;
    }

    @Override
    public boolean saveNew() {
        try {
            valorisations.clear();
            YvsBaseMouvementStock bean;
            double cout = 0;
            switch (meth) {
                case Constantes.FIFO:
                case Constantes.LIFO:
                    double qte = mouvementStock.getQuantite();
                    for (YvsBaseMouvementStock m : historiques) {
                        if (qte > 0) {
                            if (m.getReste() > 0 && m.getMouvement().equals(Constantes.MOUV_ENTREE)) {
                                bean = new YvsBaseMouvementStock((long) valorisations.size() + 1);
                                double stock = 0;
                                if (m.getReste() > qte) {
                                    stock = qte;
                                    qte = 0;
                                } else {
                                    stock = m.getReste();
                                    qte -= stock;
                                }
                                bean.setCoutEntree(m.getCoutEntree());
                                bean.setCoutStock(m.getCoutStock());
                                bean.setQuantite(stock);
                                bean.setCout(m.getCout());
                                m.setNew_(true);
                                valorisations.add(bean);
                            }
                        } else {
                            break;
                        }
                    }
                    break;
                case Constantes.CMP1:
                    bean = new YvsBaseMouvementStock((long) valorisations.size() + 1);
                    cout = (((lastMouv.getQuantite() * lastMouv.getCout()) + (mouvementStock.getQuantite() * mouvementStock.getCout())) / (lastMouv.getQuantite() + mouvementStock.getQuantite()));
                    bean.setQuantite(mouvementStock.getQuantite());
                    bean.setCoutEntree(lastMouv.getCoutEntree());
                    bean.setCoutStock(lastMouv.getCoutStock());
                    bean.setCout(cout);
                    valorisations.add(bean);
                    break;
                case Constantes.CMP2:
                    if (mouvementStock.getPeriode() > 0) {
                        if (mouvementStock.getPeriode() <= historiques.size()) {
                            List<YvsBaseMouvementStock> l = historiques.subList(historiques.size() - mouvementStock.getPeriode(), historiques.size());
                            bean = new YvsBaseMouvementStock((long) valorisations.size() + 1);
                            YvsBaseMouvementStock m = l.get(0);
                            l.get(0).setNew_(true);
                            for (int i = 1; i < l.size(); i++) {
                                YvsBaseMouvementStock y = l.get(i);
                                y.setNew_(true);
                                cout = (((m.getQuantite() * m.getCout()) + (y.getQuantite() * y.getCout())) / (m.getQuantite() + y.getQuantite()));
                                m.setCout(cout);
                                m.setQuantite(y.getQuantite());
                            }
                            cout = (((m.getQuantite() * m.getCout()) + (mouvementStock.getQuantite() * mouvementStock.getCout())) / (m.getQuantite() + mouvementStock.getQuantite()));
                            bean.setQuantite(mouvementStock.getQuantite());
                            bean.setCoutEntree(m.getCoutEntree());
                            bean.setCoutStock(m.getCoutStock());
                            bean.setCout(cout);
                            valorisations.add(bean);
                        } else {
                            getErrorMessage("Vous devez entrer une periode inferieur au nombre d'entrée");
                        }
                    } else {
                        getErrorMessage("Vous devez entrer une periode");
                    }
                    break;
            }
            actionOpenOrResetAfter(this);
            return true;
        } catch (Exception ex) {
            System.err.println("Error : " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean controleFiche(MouvementStock bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        resetFiche(mouvementStock);
        mouvementStock.setDepot(new Depots());
        mouvementStock.setTranche(new TrancheHoraire());
        mouvementStock.setArticle(new Articles());
        valorisations.clear();
        historiques.clear();
        p_historique.getParams().clear();
        update("blog_form_valorisation");
    }

    public void chooseDepot() {
        ParametreRequete p = new ParametreRequete("y.depot", "depot", null);
        ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (s != null) {
            ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if ((mouvementStock.getDepot() != null) ? mouvementStock.getDepot().getId() > 0 : false) {
                YvsBaseDepots y = s.getDepots_all().get(s.getDepots_all().indexOf(new YvsBaseDepots(mouvementStock.getDepot().getId())));
                Depots d = UtilCom.buildBeanDepot(y);
                cloneObject(mouvementStock.getDepot(), d);
                if (a != null) {
                    a.loadAllArticle(y);
                    a.setEntityDepot(y);
                    a.loadActifArticleByDepot(true, true);
                }
                loadAllTranche(y);
                p = new ParametreRequete("y.depot", "depot", y, "=", "AND");
            } else {
                mouvementStock.getTranche().setId(0);
                tranches.clear();
                if (a != null) {
                    a.getArticlesDebut().clear();
                }
            }
        }
        p_historique.addParam(p);
        if (mouvementStock.getArticle() != null ? mouvementStock.getArticle().getId() > 0 : false) {
            loadHistorique(true, true);
        }
    }

    public void chooseTranche() {
        ParametreRequete p = new ParametreRequete("y.tranche", "tranche", null);
        if ((mouvementStock.getTranche() != null) ? mouvementStock.getTranche().getId() > 0 : false) {
            YvsGrhTrancheHoraire y = tranches.get(tranches.indexOf(new YvsGrhTrancheHoraire(mouvementStock.getTranche().getId())));
            TrancheHoraire d = UtilCom.buildBeanTrancheHoraire(y);
            cloneObject(mouvementStock.getTranche(), d);
            p = new ParametreRequete("y.tranche", "tranche", y, "=", "AND");
        }
        p_historique.addParam(p);
        if (mouvementStock.getArticle() != null ? mouvementStock.getArticle().getId() > 0 : false) {
            loadHistorique(true, true);
        }
    }

    public void chooseMethode() {
        switch (meth) {
            case Constantes.FIFO:
                Collections.sort(historiques);
                for (YvsBaseMouvementStock m : historiques) {
                    m.setNew_(false);
                }
                break;
            case Constantes.LIFO:
                Collections.sort(historiques, Collections.reverseOrder());
                for (YvsBaseMouvementStock m : historiques) {
                    m.setNew_(false);
                }
                break;
            case Constantes.CMP1:
                Collections.sort(historiques);
                for (int i = 0; i < historiques.size(); i++) {
                    YvsBaseMouvementStock m = historiques.get(i);
                    if (i < historiques.size() - 1) {
                        m.setNew_(false);
                    } else {
                        m.setNew_(true);
                        MouvementStock m_ = UtilCom.buildBeanMouvementStock(m);
                        cloneObject(lastMouv, m_);
                    }
                }
                break;
            case Constantes.CMP2:
                Collections.sort(historiques);
                break;
        }
        valorisations.clear();
        update("data_mouvement_hist");
        update("data_valorisation");
        update("txt_cout_entree");
        update("txt_period_entree");
    }

    public void chooseArticle(Articles y) {
        if ((y != null) ? y.getId() > 0 : false) {
            cloneObject(mouvementStock.getArticle(), y);
            p_historique.addParam(new ParametreRequete("y.article", "article", new YvsBaseArticles(y.getId()), "=", "AND"));
            if (y.getMethodeVal() != null ? y.getMethodeVal().trim().length() > 0 : false) {
                meth = y.getMethodeVal();
                chooseMethode();
            }
            loadHistorique(true, true);
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleDepot bean = (YvsBaseArticleDepot) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean.getArticle()));
            listArt = false;
            update("select_article_valorisation");
        }
    }

    public void loadDetailMvt(YvsBaseMouvementStock mv) {
        lineMvt.setId(mv.getId());
        lineMvt.setVente(new DocVente());
        lineMvt.setAchat(new DocAchat());
        lineMvt.setDestination(new Depots());
        lineMvt.setStock(new DocStock());
        lineMvt.setDateSave(mv.getDateMouvement());
        lineMvt.setQuantite(mv.getQuantite());
        lineMvt.setConditionnement(UtilProd.buildBeanConditionnement(mv.getConditionnement()));
        lineMvt.setTableExterne(mv.getTableExterne());
        lineMvt.setLastDatePr((Date) dao.loadObjectByNameQueries("YvsBaseArticleDepot.findLastDateByArticleDepot", new String[]{"article", "depot"}, new Object[]{mv.getArticle(), mv.getDepot()}));
        if (mv.getParent() != null ? mv.getParent().getId() > 0 : false) {
            lineMvt.setParent(new MouvementStock(mv.getParent().getId()));
        } else {
            lineMvt.setParent(new MouvementStock());
        }
        switch (mv.getTableExterne()) {
            case Constantes.yvs_com_contenu_doc_achat:
                YvsComContenuDocAchat ca = (YvsComContenuDocAchat) dao.loadOneByNameQueries("YvsComContenuDocAchat.findById", new String[]{"id"}, new Object[]{mv.getIdExterne()});
                lineMvt.setArticle(UtilProd.buildSimpleBeanArticles(ca.getArticle()));
                lineMvt.setCoutEntree(ca.getPrixAchat());
                lineMvt.setTableExterne(ca.getDocAchat().getNumDoc());
                lineMvt.setQuantite(ca.getQuantiteCommande());
                lineMvt.setDescription("Achat");
                lineMvt.setAchat(UtilCom.buildBeanDocAchat(ca.getDocAchat()));
                lineMvt.setDepot(UtilProd.buildBeanDepot(ca.getDocAchat().getDepotReception()));
                break;
            case Constantes.yvs_com_contenu_doc_stock:
                YvsComContenuDocStock ds = (YvsComContenuDocStock) dao.loadOneByNameQueries("YvsComContenuDocStock.findById", new String[]{"id"}, new Object[]{mv.getIdExterne()});
                lineMvt.setArticle(UtilProd.buildSimpleBeanArticles(ds.getArticle()));
                lineMvt.setCoutEntree(ds.getPrix());
                lineMvt.setTableExterne(ds.getDocStock().getNumDoc());
                lineMvt.setStock(UtilCom.buildSimpleBeanDocStock(ds.getDocStock()));
                lineMvt.setDepot(UtilProd.buildBeanDepot(ds.getDocStock().getSource()));
                lineMvt.setDestination(UtilProd.buildBeanDepot(ds.getDocStock().getDestination()));
                lineMvt.setDescription((ds.getDocStock().getTypeDoc().equals(Constantes.TYPE_FT)) ? "Transfert" : (mv.getMouvement().equals("E") ? "Entrée" : "Sortie"));
                if (ds.getDocStock().getTypeDoc().equals(Constantes.TYPE_FT)) {
                    lineMvt.setQuantite(ds.getQuantite());
                    lineMvt.setConditionnement(UtilProd.buildBeanConditionnement(ds.getConditionnement()));
                    lineMvt.setQuantiteEntree(ds.getQuantiteRecu());
                    lineMvt.setConditionnementEntree(UtilProd.buildBeanConditionnement(ds.getConditionnementEntree()));
                    lineMvt.setCoutStock(ds.getPrixEntree());
                    lineMvt.setCoutEntree(ds.getPrix());
                }
                break;
            case Constantes.yvs_com_contenu_doc_stock_reception:
                YvsComContenuDocStockReception dr = (YvsComContenuDocStockReception) dao.loadOneByNameQueries("YvsComContenuDocStockReception.findById", new String[]{"id"}, new Object[]{mv.getIdExterne()});
                lineMvt.setArticle(UtilProd.buildSimpleBeanArticles(dr.getContenu().getArticle()));
                lineMvt.setCoutEntree(dr.getContenu().getPrix());
                lineMvt.setTableExterne(dr.getContenu().getDocStock().getNumDoc());
                lineMvt.setStock(UtilCom.buildSimpleBeanDocStock(dr.getContenu().getDocStock()));
                lineMvt.setDepot(UtilProd.buildBeanDepot(dr.getContenu().getDocStock().getSource()));
                lineMvt.setDestination(UtilProd.buildBeanDepot(dr.getContenu().getDocStock().getDestination()));
                lineMvt.setDescription((dr.getContenu().getDocStock().getTypeDoc().equals(Constantes.TYPE_FT)) ? "Transfert" : (mv.getMouvement().equals("E") ? "Entrée" : "Sortie"));
                if (dr.getContenu().getDocStock().getTypeDoc().equals(Constantes.TYPE_FT)) {
                    lineMvt.setQuantite(dr.getContenu().getQuantite());
                    lineMvt.setConditionnement(UtilProd.buildBeanConditionnement(dr.getContenu().getConditionnement()));
                    lineMvt.setQuantiteEntree(dr.getQuantite());
                    lineMvt.setConditionnementEntree(UtilProd.buildBeanConditionnement(dr.getContenu().getConditionnementEntree()));
                    lineMvt.setCoutStock(dr.getContenu().getPrixEntree());
                    lineMvt.setCoutEntree(dr.getContenu().getPrix());
                }
                break;
            case Constantes.yvs_com_contenu_doc_vente:
                YvsComContenuDocVente cv = (YvsComContenuDocVente) dao.loadOneByNameQueries("YvsComContenuDocVente.findById", new String[]{"id"}, new Object[]{mv.getIdExterne()});
                lineMvt.setArticle(UtilProd.buildSimpleBeanArticles(cv.getArticle()));
                lineMvt.setCoutEntree(cv.getPrix());
                lineMvt.setTableExterne(cv.getDocVente().getNumDoc());
                lineMvt.setQuantite(cv.getQuantite());
                lineMvt.setDepot(UtilProd.buildBeanDepot(cv.getDocVente().getDepotLivrer()));
                lineMvt.setDescription("Vente");
                lineMvt.setVente(UtilCom.buildBeanDocVente(cv.getDocVente()));
                break;
            case Constantes.yvs_com_ration:
                YvsComRation ra = (YvsComRation) dao.loadOneByNameQueries("YvsComRation.findById", new String[]{"id"}, new Object[]{mv.getIdExterne()});
                lineMvt.setArticle(UtilProd.buildSimpleBeanArticles(ra.getArticle()));
                lineMvt.setCoutEntree(0);
                lineMvt.setTableExterne(ra.getDocRation().getNumDoc());
                lineMvt.setQuantite(ra.getQuantite());
                lineMvt.setDepot(UtilProd.buildBeanDepot(ra.getDocRation().getDepot()));
                lineMvt.setDescription(ra.getPersonnel().getNom_prenom());
                lineMvt.setRation(UtilCom.buildBeanDocRation(ra.getDocRation()));
            case Constantes.yvs_prod_declaration_production:
                YvsProdDeclarationProduction order = (YvsProdDeclarationProduction) dao.loadOneByNameQueries("YvsProdDeclarationProduction.findById", new String[]{"id"}, new Object[]{mv.getIdExterne()});
                if (order != null) {
                    lineMvt.setArticle(UtilProd.buildSimpleBeanArticles(order.getOrdre().getArticle()));
                    lineMvt.setCoutEntree(order.getCoutProduction());
                    lineMvt.setTableExterne(order.getOrdre().getCodeRef());
                    lineMvt.setQuantite(order.getQuantite());
                }
                break;
            case Constantes.yvs_prod_of_suivi_flux:
                YvsProdOfSuiviFlux suiviOf = (YvsProdOfSuiviFlux) dao.loadOneByNameQueries("YvsProdOfSuiviFlux.findById", new String[]{"id"}, new Object[]{mv.getIdExterne()});
                if (suiviOf != null) {
                    lineMvt.setArticle(UtilProd.buildSimpleBeanArticles(suiviOf.getComposant().getComposant().getArticle()));
                    lineMvt.setCoutEntree(suiviOf.getCout());
                    lineMvt.setTableExterne(suiviOf.getIdOperation().getOperationOf().getOrdreFabrication().getCodeRef());
                    lineMvt.setQuantite(suiviOf.getQuantite());
                }
                break;
        }
        update("zone_detail_mvt_s");
        update("dateSave_mvt");
        openDialog("dlgDetailMvt");
    }

    public void changeCalculPrAll(boolean calculPr) {
        if (selections != null ? !selections.isEmpty() : false) {
            if (!autoriser("recalcul_pr")) {
                openNotAcces();;
                return;
            }
            for (YvsBaseMouvementStock mvt : selections) {
                changeCalculPr(mvt, calculPr);
            }
            succes();
        }
    }

    public void changeCalculPr(YvsBaseMouvementStock mvt) {
        if (mvt != null ? mvt.getId() > 0 : false) {
            if (!autoriser("recalcul_pr")) {
                openNotAcces();;
                return;
            }
            changeCalculPr(mvt, !mvt.getCalculPr());
            succes();
        }
    }

    public void changeCalculPr(YvsBaseMouvementStock mvt, boolean calculPr) {
        try {
            if (mvt != null ? mvt.getId() > 0 : false) {
                champ = new String[]{"id"};
                val = new Object[]{mvt.getIdExterne()};
                switch (mvt.getTableExterne()) {
                    case Constantes.yvs_com_contenu_doc_achat: {
                        YvsComContenuDocAchat y = (YvsComContenuDocAchat) dao.loadOneByNameQueries("YvsComContenuDocAchat.findById", champ, val);
                        y.setCalculPr(calculPr);
                        y.setAuthor(currentUser);
                        y.setDateUpdate(new Date());
                        dao.update(y);
                        break;
                    }
                    case Constantes.yvs_com_contenu_doc_stock: {
                        YvsComContenuDocStock y = (YvsComContenuDocStock) dao.loadOneByNameQueries("YvsComContenuDocStock.findById", champ, val);
                        y.setCalculPr(calculPr);
                        y.setAuthor(currentUser);
                        y.setDateUpdate(new Date());
                        dao.update(y);
                        break;
                    }
                    case Constantes.yvs_com_contenu_doc_stock_reception: {
                        YvsComContenuDocStockReception y = (YvsComContenuDocStockReception) dao.loadOneByNameQueries("YvsComContenuDocStockReception.findById", champ, val);
                        y.setCalculPr(calculPr);
                        y.setAuthor(currentUser);
                        y.setDateUpdate(new Date());
                        dao.update(y);
                        break;
                    }
                    case Constantes.yvs_com_contenu_doc_vente: {
                        YvsComContenuDocVente y = (YvsComContenuDocVente) dao.loadOneByNameQueries("YvsComContenuDocVente.findById", champ, val);
                        y.setCalculPr(calculPr);
                        y.setAuthor(currentUser);
                        y.setDateUpdate(new Date());
                        dao.update(y);
                        break;
                    }
                    case Constantes.yvs_com_ration: {
                        YvsComRation y = (YvsComRation) dao.loadOneByNameQueries("YvsComRation.findById", champ, val);
                        y.setCalculPr(calculPr);
                        y.setAuthor(currentUser);
                        y.setDateUpdate(new Date());
                        dao.update(y);
                        break;
                    }
                    case Constantes.yvs_prod_of_suivi_flux: {
                        YvsProdOfSuiviFlux y = (YvsProdOfSuiviFlux) dao.loadOneByNameQueries("YvsProdOfSuiviFlux.findById", champ, val);
                        y.setCalculPr(!y.getCalculPr());
                        y.setAuthor(currentUser);
                        y.setDateUpdate(new Date());
                        dao.update(y);
                        break;
                    }
                    case Constantes.yvs_prod_declaration_production: {
                        YvsProdDeclarationProduction y = (YvsProdDeclarationProduction) dao.loadOneByNameQueries("YvsProdDeclarationProduction.findById", champ, val);
                        y.setCalculPr(calculPr);
                        y.setAuthor(currentUser);
                        y.setDateUpdate(new Date());
                        dao.update(y);
                        break;
                    }
                    case Constantes.yvs_prod_contenu_conditionnement: {
                        YvsProdContenuConditionnement y = (YvsProdContenuConditionnement) dao.loadOneByNameQueries("YvsProdContenuConditionnement.findById", champ, val);
                        y.setCalculPr(calculPr);
                        y.setAuthor(currentUser);
                        y.setDateUpdate(new Date());
                        dao.update(y);
                        break;
                    }
                    case Constantes.yvs_prod_fiche_conditionnement: {

                        break;
                    }
                }
                mvt.setCalculPr(calculPr);
                dao.update(mvt);
                int index = mouvements.indexOf(mvt);
                if (index > -1) {
                    mouvements.set(index, mvt);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!");
            getException("ManagedValorisation (changeCalculPr)", ex);
        }
    }

    public void deleteSource(YvsBaseMouvementStock mvt) {
        try {
            if (mvt != null ? mvt.getId() > 0 : false) {
                switch (mvt.getTableExterne()) {
                    case Constantes.yvs_com_contenu_doc_achat: {
                        dao.delete(new YvsComContenuDocAchat(mvt.getIdExterne(), currentUser));
                        break;
                    }
                    case Constantes.yvs_com_contenu_doc_stock: {
                        dao.delete(new YvsComContenuDocStock(mvt.getIdExterne(), currentUser));
                        break;
                    }
                    case Constantes.yvs_com_contenu_doc_stock_reception: {
                        dao.delete(new YvsComContenuDocStockReception(mvt.getIdExterne(), currentUser));
                        break;
                    }
                    case Constantes.yvs_com_contenu_doc_vente: {
                        dao.delete(new YvsComContenuDocVente(mvt.getIdExterne(), currentUser));
                        break;
                    }
                    case Constantes.yvs_com_ration: {
                        dao.delete(new YvsComRation(mvt.getIdExterne(), currentUser));
                        break;
                    }
                    case Constantes.yvs_prod_of_suivi_flux: {
                        dao.delete(new YvsProdOfSuiviFlux(mvt.getIdExterne(), currentUser));
                        break;
                    }
                    case Constantes.yvs_prod_declaration_production: {
                        dao.delete(new YvsProdDeclarationProduction(mvt.getIdExterne(), currentUser));
                        break;
                    }
                    case Constantes.yvs_prod_contenu_conditionnement: {
                        dao.delete(new YvsProdContenuConditionnement(mvt.getIdExterne(), currentUser));
                        break;
                    }
                    case Constantes.yvs_prod_fiche_conditionnement: {

                        break;
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!");
            getException("ManagedValorisation (deleteSource)", ex);
        }
    }

    public void searchArticle() {
        paginator.addParam(new ParametreRequete("y.article", "article", null, "=", "AND"));
        String num = mouvementStock.getArticle().getRefArt();
        mouvementStock.getArticle().setDesignation("");
        mouvementStock.getArticle().setError(true);
        mouvementStock.getArticle().setId(0);
        listArt = false;
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            if (m.getEntityDepot() == null) {
                m.setEntityDepot(new YvsBaseDepots(mouvementStock.getDepot().getId()));
            }
            Articles y = m.searchArticleActifByDepot(num, true);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data_article_valorisation");
                } else {
                    chooseArticle(y);
                }
                mouvementStock.getArticle().setError(false);
            }
            listArt = y.isListArt();
        }
    }

    public void initArticles() {
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.initArticlesByDepot(mouvementStock.getArticle());
            listArt = mouvementStock.getArticle().isListArt();
        }
        update("data_articles_valorisation");
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void init(boolean next) {
        loadAllMouvement(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllMouvement(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllMouvement(true, true);
    }

    public void _choosePaginator(ValueChangeEvent ev) {
        setMax((int) ev.getNewValue());
        loadHistorique(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateDoc", "date", null, null, "BETWEEN", "AND");
        if (date_) {
            p = new ParametreRequete("y.dateDoc", "date", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllMouvement(true, true);
    }

    public void addParamDepot(boolean load) {
        ParametreRequete p = new ParametreRequete("y.depot", "depot", null, "=", "AND");
        if (depotSearch > 0) {
            p = new ParametreRequete("y.depot", "depot", new YvsBaseDepots(depotSearch), "=", "AND");
        }
        paginator.addParam(p);
        if (load) {
            loadAllMouvement(true, true);
        }
    }

    public void addParamTranche(boolean load) {
        ParametreRequete p = new ParametreRequete("y.tranche", "tranche", null, "=", "AND");
        if (trhSearch > 0) {
            p = new ParametreRequete("y.tranche", "tranche", new YvsGrhTrancheHoraire(trhSearch), "=", "AND");
        }
        paginator.addParam(p);
        if (load) {
            loadAllMouvement(true, true);
        }
    }

    public void addParamUnite() {
        ParametreRequete p = new ParametreRequete("y.conditionnement", "unite", null, "=", "AND");
        if (uniteSearch > 0) {
            p = new ParametreRequete("y.conditionnement.unite", "unite", new YvsBaseUniteMesure(uniteSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllMouvement(true, true);
    }

    public void addParamLot() {
        ParametreRequete p = new ParametreRequete("y.id", "lot", null, "=", "AND");
        if (lotSearch != null ? lotSearch.trim().length() > 0 : false) {
            String query = "SELECT m.mouvement FROM yvs_base_mouvement_stock_lot m INNER JOIN yvs_com_lot_reception r ON m.lot = r.id INNER JOIN yvs_agences a ON r.agence = a.id WHERE UPPER(r.numero) LIKE ? AND a.societe = ?";
            List<Long> lots = dao.loadListBySqlQuery(query, new Options[]{new Options(lotSearch.toUpperCase() + "%", 1), new Options(currentAgence.getSociete().getId(), 2)});
            if (lots != null ? lots.isEmpty() : true) {
                lots = new ArrayList<Long>() {
                    {
                        add(-1L);
                    }
                };
            }
            p = new ParametreRequete("y.id", "lot", lots, "IN", "AND");
        }
        paginator.addParam(p);
        loadAllMouvement(true, true);
    }

    public void addParamArticle() {
        articlesSearch = 0;
        selectArticle = null;
        ParametreRequete p = new ParametreRequete("y.article", "article", null, "=", "AND");
        if (artSearch != null ? artSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "article", artSearch.toUpperCase(), "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", artSearch.toUpperCase(), "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", artSearch.toUpperCase(), "LIKE", "OR"));

            Long id = (Long) dao.loadObjectByNameQueries("YvsBaseArticles.findIdByCodeL", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), artSearch.toUpperCase()});
            if (id != null) {
                articlesSearch = id;
                selectArticle = id + "";
            }
        }
        ManagedUniteMesure w = (ManagedUniteMesure) giveManagedBean(ManagedUniteMesure.class);
        if (w != null) {
            w.loadByArticle(artSearch, Constantes.UNITE_QUANTITE);
        }
        paginator.addParam(p);
        paginator.addParam(new ParametreRequete("y.article.refArt", "references", null, "=", "AND"));
        loadAllMouvement(true, true);
    }

    public void addParamOperation() {
        ParametreRequete p = new ParametreRequete("y.description", "description", null, "=", "AND");
        if (opeSearch != null ? opeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.description)", "description", opeSearch.toUpperCase() + "%", "LIKE", "AND");
        }
        paginator.addParam(p);
        loadAllMouvement(true, true);
    }

    public void addParamTypeDoc() {
        ParametreRequete p = new ParametreRequete("y.typeDoc", "typeDoc", null, "=", "AND");
        if (typeSearch != null ? typeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.typeDoc)", "typeDoc", typeSearch.toUpperCase(), "=", "AND");
        }
        paginator.addParam(p);
        loadAllMouvement(true, true);
    }

    public void addParamNumeroDoc() {
        ParametreRequete p = new ParametreRequete("y.numDoc", "numDoc", null, "=", "AND");
        if (numeroSearch != null ? numeroSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.numDoc)", "numDoc", numeroSearch.toUpperCase() + "%", "LIKE", "AND");
        }
        paginator.addParam(p);
        loadAllMouvement(true, true);
    }

    public void addParamMouv() {
        ParametreRequete p = new ParametreRequete("y.mouvement", "mouvement", null, "=", "AND");
        if (mouvSearch != null) {
            p = new ParametreRequete("y.mouvement", "mouvement", (mouvSearch ? "E" : "S"), "=", "AND");
        }
        paginator.addParam(p);
        loadAllMouvement(true, true);
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllMouvement(true, true);
    }

    public void clearParams() {
        date_ = false;
        depotSearch = 0L;
        trhSearch = 0L;
        artSearch = null;
        paginator.clear();
        loadAllMouvement(true, true);

    }
    /*
     DEBUT AUDIT STOCK
     */

    public void loadAuditStock() {
        audits.clear();
        if (!date_) {
            dateDebut_ = currentExo != null ? currentExo.getDateDebut() : new Date();
        }
        Options[] param = new Options[]{new Options(depotSearch, 1), new Options(currentAgence.getId(), 2), new Options(currentAgence.getSociete().getId(), 3), new Options(dateDebut_, 4), new Options(dateFin_, 5)};
        List<Object[]> lines = dao.loadListBySqlQuery(query, param);
        ArticleDepot y;
        ArticleDepot a;
        YvsBaseArticleDepot e;
        for (Object[] line : lines) {
            y = objectOnArticle(line, audits.size() + 1);
            boolean deja = false;
            for (int i = 0; i < audits.size(); i++) {
                a = audits.get(i);
                if (a.getArticle().equals(y.getArticle()) && a.getDepot().equals(y.getDepot())) {
                    if (a.getQteReel() < 1) {
                        a.setQteReel(y.getQteReel());
                    }
                    a.setSupp(y.isSupp() ? y.isSupp() : a.isSupp());
                    deja = true;
                    break;
                }
            }
            if (!deja) {
                e = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", new String[]{"article", "depot"}, new Object[]{new YvsBaseArticles(y.getArticle().getId()), new YvsBaseDepots(y.getDepot().getId())});
                if (e != null ? e.getId() > 0 : false) {
                    y.setStockMin(e.getStockMin());
                    y.setStockMax(e.getStockMax());
                    y.setStockNet(e.getStockNet());
                    y.setMargStockMoyen(e.getMargStockMoyen());
                }
                audits.add(y);
            }
        }
    }

    public void addParamDatesAudit() {
        if (!date_) {
            dateFin_ = new Date();
        }
        loadAuditStock();
    }

    public void addParamDepotAudit() {
        loadAuditStock();
    }

    public void addParamArticleAudit() {
        query = "select y.* from do_audit(?, ?, ?, ?, ?, 'S') y inner join yvs_base_articles a on y.element::bigint = a.id inner join yvs_base_depots d on y.depot = d.id";
        if (artSearch != null ? artSearch.trim().length() > 0 : false) {
            query += " where (upper(a.ref_art) like '" + artSearch.trim().toUpperCase() + "%' or upper(a.designation) like '" + artSearch.trim().toUpperCase() + "%')";
        }
        loadAuditStock();
    }

    private ArticleDepot objectOnArticle(Object[] line, long id) {
        if (line != null ? line.length > 0 : false) {
            ArticleDepot y = new ArticleDepot(id);
            if (line.length > 0 ? (Long) line[0] > 0 : false) {
                YvsBaseDepots d = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{(Long) line[0]});
                y.setDepot(UtilCom.buildBeanDepot(d));
                if (line.length > 1 ? Long.valueOf(line[1].toString()) > 0 : false) {
                    YvsBaseArticles a = (YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{Long.valueOf(line[1].toString())});
                    y.setArticle(UtilProd.buildBeanArticles(a));
                    if (line.length > 2 ? line[2] != null : false) {
                        y.setQteReel(Double.valueOf(line[2].toString()));//Stock reel
                        if (line.length > 3 ? line[3] != null : false) {
                            y.setValeur_prevu(line[3].toString());
                            if (line.length > 4 ? line[4] != null : false) {
                                String v = line[4].toString();
                                if (Util.isNumeric(v)) {
                                    y.setQuantiteStock(Double.valueOf(v));//Moyenne de stock
                                } else {
                                    y.setSupp(Boolean.valueOf(v));
                                }
                                if (line.length > 5 ? line[5] != null : false) {
                                    y.setOperation(line[5].toString());
                                    if (line.length > 6 ? line[6] != null : false) {
                                        y.setNature(line[6].toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return y;
        }
        return null;
    }
    /*
     FIN AUDIT STOCK
     */
    /*
     DEBUT VALIDATION STOCK
     */

    public int nbPage() {
        switch (element) {
            case Constantes.TRANSFERT:
                return p_transfert.getNbPage();
            case Constantes.DECLARATION:
                return p_declaration.getNbPage();
        }
        return 0;
    }

    public int page() {
        switch (element) {
            case Constantes.TRANSFERT:
                return p_transfert.getPage();
            case Constantes.DECLARATION:
                return p_declaration.getPage();
        }
        return 0;
    }

    public int currentPage() {
        switch (element) {
            case Constantes.TRANSFERT:
                return p_transfert.getCurrentPage();
            case Constantes.DECLARATION:
                return p_declaration.getCurrentPage();
        }
        return 0;
    }

    public int rows() {
        switch (element) {
            case Constantes.TRANSFERT:
                return p_transfert.getRows();
            case Constantes.DECLARATION:
                return p_declaration.getRows();
        }
        return 0;
    }

    public boolean disPrev() {
        switch (element) {
            case Constantes.TRANSFERT:
                return p_transfert.isDisPrev();
            case Constantes.DECLARATION:
                return p_declaration.isDisPrev();
        }
        return false;
    }

    public boolean disNext() {
        switch (element) {
            case Constantes.TRANSFERT:
                return p_transfert.isDisNext();
            case Constantes.DECLARATION:
                return p_declaration.isDisNext();
        }
        return false;
    }

    public void paginerValid(boolean next) {
        switch (element) {
            case Constantes.TRANSFERT:
                loadAllTransfert(next, false);
                break;
            case Constantes.DECLARATION:
                loadAllDeclaration(next, false);
                break;
        }
    }

    public void choosePaginatorValid(ValueChangeEvent ev) {
        switch (element) {
            case Constantes.TRANSFERT:
                p_transfert.choosePaginator(ev);
                loadAllTransfert(true, true);
                break;
            case Constantes.DECLARATION:
                p_declaration.choosePaginator(ev);
                loadAllDeclaration(true, true);
                break;
        }
    }

    public void gotoPagePaginatorValid() {
        switch (element) {
            case Constantes.TRANSFERT:
                p_transfert.gotoPage((int) p_transfert.getRows());
                loadAllTransfert(true, true);
                break;
            case Constantes.DECLARATION:
                p_declaration.gotoPage((int) p_transfert.getRows());
                loadAllDeclaration(true, true);
                break;
        }
    }

    public void resetValidation() {
        lineMvt = new MouvementStock();
        selectDeclaration = new YvsProdDeclarationProduction();
        selectTransfert = new YvsComContenuDocStock();
        update("form_validation");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        lineMvt = new MouvementStock();
        switch (element) {
            case Constantes.TRANSFERT:
                selectTransfert = (YvsComContenuDocStock) ev.getObject();
                lineMvt.setArticle(UtilProd.buildSimpleBeanArticles(selectTransfert.getArticle()));
                lineMvt.setConditionnement(UtilProd.buildBeanConditionnement(selectTransfert.getConditionnement()));
                lineMvt.setDepot(UtilCom.buildSimpleBeanDepot(selectTransfert.getDocStock().getDestination()));
                lineMvt.setDateDoc(selectTransfert.getDocStock().getDateDoc());
                lineMvt.setDescription(selectTransfert.getDocStock().getNumDoc());
                lineMvt.setQuantite(selectTransfert.getQuantite());
                lineMvt.setIdExterne(selectTransfert.getId());
                lineMvt.setTableExterne(Constantes.TRANSFERT);
                break;
            case Constantes.DECLARATION:
                selectDeclaration = (YvsProdDeclarationProduction) ev.getObject();
                lineMvt.setArticle(UtilProd.buildSimpleBeanArticles(selectDeclaration.getOrdre().getArticle()));
                lineMvt.setConditionnement(UtilProd.buildBeanConditionnement(selectDeclaration.getConditionnement()));
                lineMvt.setDepot(UtilCom.buildSimpleBeanDepot(selectDeclaration.getSessionOf().getSessionProd().getDepot()));
                lineMvt.setDateDoc(selectDeclaration.getSessionOf().getSessionProd().getDateSession());
                lineMvt.setDescription(selectDeclaration.getOrdre().getNumeroIdentification());
                lineMvt.setQuantite(selectDeclaration.getQuantite());
                lineMvt.setIdExterne(selectDeclaration.getId());
                lineMvt.setTableExterne(Constantes.DECLARATION);
                break;
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetValidation();
    }

    public void chooseElement() {
        clearParamsValidation();
    }

    public void clearParamsValidation() {
        articleSearch = "";
        depotsSearch = 0;
        dateSearch = null;
        debutSearch = new Date();
        finSearch = new Date();
        switch (element) {
            case Constantes.TRANSFERT:
                p_transfert.clear();
                loadAllTransfert(true, true);
                break;
            case Constantes.DECLARATION:
                p_declaration.clear();
                loadAllDeclaration(true, true);
                break;
        }
        update("blog_search_validation");
    }

    public void valider() {
        switch (element) {
            case Constantes.TRANSFERT:
                valideTransfert();
                break;
            case Constantes.DECLARATION:
                valideDeclaration();
                break;
        }
    }

    public void valideTransfert(YvsComContenuDocStock y) {
        selectTransfert = y;
        valideTransfert();
    }

    public void valideTransfert() {
        if (selectTransfert != null ? selectTransfert.getId() > 0 : false) {
            double stock = dao.stocks(selectTransfert.getArticle().getId(), 0, selectTransfert.getDocStock().getSource().getId(), 0, 0, selectTransfert.getDocStock().getDateDoc(), (selectTransfert.getConditionnement() != null) ? selectTransfert.getConditionnement().getId() : -1, (selectTransfert.getLotSortie() != null) ? selectTransfert.getLotSortie().getId() : -1);
            if (stock < selectTransfert.getQuantite()) {
                getErrorMessage("L'article '" + selectTransfert.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
                return;
            }
            stock = dao.stocks(selectTransfert.getArticle().getId(), 0, selectTransfert.getDocStock().getSource().getId(), 0, 0, new Date(), (selectTransfert.getConditionnement() != null) ? selectTransfert.getConditionnement().getId() : -1, (selectTransfert.getLotSortie() != null) ? selectTransfert.getLotSortie().getId() : -1);
            if (stock < selectTransfert.getQuantite()) {
                getErrorMessage("L'article '" + selectTransfert.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
                return;
            }

            selectTransfert.setStatut(Constantes.ETAT_VALIDE);
            selectTransfert.setDateUpdate(new Date());
            selectTransfert.setAuthor(currentUser);
            dao.update(selectTransfert);
            transferts.remove(selectTransfert);

            boolean valide = true;
            List<YvsComContenuDocStock> list = dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{selectTransfert.getDocStock()});
            for (YvsComContenuDocStock c : list) {
                if (!c.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    valide = false;
                    break;
                }
            }
            if (valide) {
                selectTransfert.getDocStock().setStatut(Constantes.ETAT_VALIDE);
                selectTransfert.getDocStock().setDateUpdate(new Date());
                selectTransfert.getDocStock().setAuthor(currentUser);
                dao.update(selectTransfert.getDocStock());
            }
            resetValidation();
            succes();
        }
    }

    public void valideDeclaration(YvsProdDeclarationProduction y) {
        selectDeclaration = y;
        valideDeclaration();
    }

    public void valideDeclaration() {
        if (selectDeclaration != null ? selectDeclaration.getId() > 0 : false) {
            ManagedOrdresF w = (ManagedOrdresF) giveManagedBean(ManagedOrdresF.class);
            if (w != null) {
                if (!w.canDeclared(selectDeclaration.getOrdre())) {
                    getErrorMessage("Vous ne pouvre pas valider cette déclaration de production... Toutes les opérations de l'ordre de fabrication doivent etre en cours");
                    return;
                }
                selectDeclaration.setStatut(Constantes.STATUT_DOC_VALIDE);
                selectDeclaration.setDateUpdate(new Date());
                selectDeclaration.setAuthor(currentUser);
                dao.update(selectDeclaration);
                declarations.remove(selectDeclaration);

                resetValidation();
                succes();
            }
        }
    }

    public void addParamValidDates() {
        switch (element) {
            case Constantes.TRANSFERT: {
                ParametreRequete p = new ParametreRequete("y.docStock.dateDoc", "date", null, null, "BETWEEN", "AND");
                if (dateSearch) {
                    p = new ParametreRequete("y.docStock.dateDoc", "date", dateDebut_, dateFin_, "BETWEEN", "AND");
                }
                p_transfert.addParam(p);
                loadAllTransfert(true, true);
                break;
            }
            case Constantes.DECLARATION: {
                ParametreRequete p = new ParametreRequete("y.dateDeclaration", "date", null, null, "BETWEEN", "AND");
                if (dateSearch) {
                    p = new ParametreRequete("y.dateDeclaration", "date", dateDebut_, dateFin_, "BETWEEN", "AND");
                }
                p_declaration.addParam(p);
                loadAllDeclaration(true, true);
                break;
            }
        }
    }

    public void addParamValidDepot() {
        switch (element) {
            case Constantes.TRANSFERT: {
                ParametreRequete p = new ParametreRequete("y.docStock.destination", "depot", null, "=", "AND");
                if (depotsSearch > 0) {
                    p = new ParametreRequete("y.docStock.destination", "depot", new YvsBaseDepots(depotsSearch), "=", "AND");
                }
                p_transfert.addParam(p);
                loadAllTransfert(true, true);
                break;
            }
            case Constantes.DECLARATION: {
                ParametreRequete p = new ParametreRequete("y.depot", "depot", null, "=", "AND");
                if (depotsSearch > 0) {
                    p = new ParametreRequete("y.depot", "depot", new YvsBaseDepots(depotsSearch), "=", "AND");
                }
                p_declaration.addParam(p);
                loadAllDeclaration(true, true);
                break;
            }
        }
    }

    public void addParamValidArticle() {
        switch (element) {
            case Constantes.TRANSFERT: {
                ParametreRequete p = new ParametreRequete("y.article", "article", null, "=", "AND");
                if (artSearch != null ? artSearch.trim().length() > 0 : false) {
                    p = new ParametreRequete(null, "article", artSearch.toUpperCase(), "LIKE", "AND");
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                }
                p_transfert.addParam(p);
                loadAllTransfert(true, true);
                break;
            }
            case Constantes.DECLARATION: {
                ParametreRequete p = new ParametreRequete("y.article", "article", null, "=", "AND");
                if (artSearch != null ? artSearch.trim().length() > 0 : false) {
                    p = new ParametreRequete(null, "article", artSearch.toUpperCase(), "LIKE", "AND");
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", artSearch.toUpperCase() + "%", "LIKE", "OR"));
                }
                p_declaration.addParam(p);
                loadAllDeclaration(true, true);
                break;
            }
        }
    }

    public void printInventaire() {
        if (dateFin_ == null) {
            getErrorMessage("Vous devez précisez la date de fin");
            return;
        }
        if (depotSearch < 1) {
            getErrorMessage("Vous devez indiquer le depot");
            return;
        }
        ManagedInventaire w = (ManagedInventaire) giveManagedBean(ManagedInventaire.class);
        if (w != null) {
            w.printInventaire(depotSearch, emplacementSearch, 0, "", 0, "V", dateFin_, true, "", false, orderByPrint);
        }
    }

    public void printInventairePreparatoire() {
        if (dateFin_ == null) {
            getErrorMessage("Vous devez précisez la date de fin");
            return;
        }
        if (depotSearch < 1) {
            getErrorMessage("Vous devez indiquer le depot");
            return;
        }
        ManagedInventaire w = (ManagedInventaire) giveManagedBean(ManagedInventaire.class);
        if (w != null) {
            w.printInventairePreparatoire(depotSearch, emplacementSearch, 0, "", 0, "V", dateFin_, true, "", false, orderByPrint);
        }
    }

    public void printListing(boolean entree) {
        if (depotSearch < 1) {
            getErrorMessage("Vous devez indiquer le depot");
            return;
        }
        ManagedOtherTransfert w = (ManagedOtherTransfert) giveManagedBean(ManagedOtherTransfert.class);
        if (w != null) {
            w.printListing(depotSearch, trhSearch, dateDebut_, dateFin_, entree);
        }
    }

    public void printListing(boolean entree, String format) {
        if (depotSearch < 1) {
            getErrorMessage("Vous devez indiquer le depot");
            return;
        }
        ManagedOtherTransfert w = (ManagedOtherTransfert) giveManagedBean(ManagedOtherTransfert.class);
        if (w != null) {
            w.printListing(depotSearch, trhSearch, dateDebut_, dateFin_, entree, format);
        }
    }

    public void print() {
        print("");
    }

    public void printValoriser() {
        if (!date_) {
            getErrorMessage("Vous devez précisez une période");
            return;
        }
        if (dateDebut_ == null) {
            getErrorMessage("Vous devez précisez la date de debut");
            return;
        }
        if (dateFin_ == null) {
            getErrorMessage("Vous devez précisez la date de fin");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("DEPOT", (int) depotSearch);
        param.put("DATE_DEBUT", date_ ? dateDebut_ : null);
        param.put("DATE_FIN", date_ ? dateFin_ : null);

        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        String report = "etat_valoriser_mouvement_stock";
        executeReport(report, param);
    }

    public void print(String type) {
        conditSearch = 0;
        if (articlesSearch > 0 && uniteSearch > 0) {
            Long id = (Long) dao.loadObjectByNameQueries("YvsBaseConditionnement.findIdByArticleUnite", new String[]{"article", "unite"}, new Object[]{new YvsBaseArticles(articlesSearch), new YvsBaseUniteMesure(uniteSearch)});
            if (id != null) {
                conditSearch = id;
            }
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("DEPOT", (int) depotSearch);
        param.put("TRANCHE", (int) trhSearch);
        param.put("ARTICLE", (int) articlesSearch);
        param.put("UNITE", (int) conditSearch);
        param.put("DATE_DEBUT", date_ ? dateDebut_ : null);
        param.put("DATE_FIN", date_ ? dateFin_ : null);
        param.put("MOUVEMENT", mouvSearch != null ? (mouvSearch ? "E" : "S") : null);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        String report = "mouvement_stock";
        if (!autoriser("gescom_inventaire_print_with_pr")) {
            report = "mouvement_stock_sans_pr";
        } else if (type.equals("2_colonnes")) {
            report = "mouvement_stock_2_colonnes";
        }
        executeReport(report, param);
    }

    public void loadArticlesSelect() {
        selectArticle = null;
        artSearch = null;
        ManagedArticles w = (ManagedArticles) giveManagedBean("Marticle");
        if (w != null) {
            List<String> references = new ArrayList<>();
            for (YvsBaseArticles a : w.getSelectArticles()) {
                if (!Util.asString(selectArticle)) {
                    selectArticle = a.getId() + "";
                } else {
                    selectArticle += "," + a.getId();
                }
                if (!Util.asString(artSearch)) {
                    artSearch = a.getRefArt();
                } else {
                    artSearch += "-" + a.getRefArt();
                }
                references.add(a.getRefArt());
            }
            update("label-nombre_article_select");
            ParametreRequete p = new ParametreRequete("y.article.refArt", "references", null, "=", "AND");
            if (!references.isEmpty()) {
                p = new ParametreRequete("y.article.refArt", "references", references, "IN", "AND");
            }
            paginator.addParam(p);
            paginator.addParam(new ParametreRequete("y.article", "article", null, "=", "AND"));
            loadAllMouvement(true, true);
        }
    }

    public void recalculPr() {
        if (!autoriser("recalcul_pr")) {
            openNotAcces();
            return;
        }
        if (depotSearch < 1 || !Util.asString(selectArticle)) {
            getErrorMessage("Vous devez precisez le depot et l'article");
            return;
        }
        if (!date_ || dateDebut_ == null || dateFin_ == null) {
            getErrorMessage("Vous devez precisez une période");
            return;
        }
        String query = "SELECT com_recalcule_pr_periode(?,?,?,?,?,?)";
        double total = dao.callFonction(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(-1, 2), new Options(depotSearch, 3), new Options(selectArticle, 4), new Options(dateDebut_, 5), new Options(dateFin_, 6)});
//        //repositionner les prix de vente
        query = "SELECT update_pr_article_(?,?,?,?)";
        boolean re = dao.callFonctionBool(query, new Options[]{new Options(currentAgence.getId(), 1), new Options(dateDebut_, 2), new Options(dateFin_, 3), new Options(mouvements.get(0).getConditionnement().getId(), 4)});
        getInfoMessage("Traitement effectué.", " Total prix revient de la période : " + total);
    }

    public void recalculPr(Long article, Long unite, Date dateDebut, Date dateFin) {
        recalculPr(currentAgence.getId(), article, unite, dateDebut, dateFin);
    }

    public void recalculPr(Long agence, Long article, Long unite, Date dateDebut, Date dateFin) {
        if (!autoriser("recalcul_pr")) {
            openNotAcces();
            return;
        }
        if (article != null ? article < 1 : true) {
            getErrorMessage("Vous devez precisez l'article");
            return;
        }
        if (dateDebut == null || dateFin == null) {
            getErrorMessage("Vous devez precisez une période");
            return;
        }
        Long depot = (Long) dao.loadObjectByNameQueries("YvsBaseArticleDepot.findIdByArticlePr", new String[]{"article", "agence"}, new Object[]{new YvsBaseArticles(article), new YvsAgences(agence)});
        if (depot != null ? depot < 1 : true) {
            getErrorMessage("Aucun dépôt par défaut pour cet article");
            return;
        }
        String query = "SELECT com_recalcule_pr_periode(?,?,?,?,?,?)";
        double total = dao.callFonction(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(-1, 2), new Options(depot, 3), new Options(article.toString(), 4), new Options(dateDebut, 5), new Options(dateFin, 6)});
//        //repositionner les prix de vente
        query = "SELECT update_pr_article_(?,?,?,?)";
        boolean re = dao.callFonctionBool(query, new Options[]{new Options(agence, 1), new Options(dateDebut, 2), new Options(dateFin, 3), new Options(unite, 4)});
        getInfoMessage("Traitement effectué.", " Total prix revient de la période : " + total);
    }

    public void recalculPrixTransfert() {
        try {
            if (!autoriser("recalcul_pr")) {
                openNotAcces();
                return;
            }
            for (YvsBaseMouvementStock mvt : mouvements) {
                if (mvt.getTableExterne().equals(Constantes.yvs_com_contenu_doc_stock) && (mvt.getTypeDoc().equals(Constantes.TYPE_FT) || mvt.getTypeDoc().equals(Constantes.TYPE_RE))) {
                    recalculPrixTransfert(mvt, false);
                }
            }
            succes();
        } catch (Exception ex) {
            getException("recalculPrixTransfert", ex);
        }
    }

    public void recalculPrixTransfert(YvsBaseMouvementStock mvt, boolean succes) {
        try {
            if (mvt.getTableExterne().equals(Constantes.yvs_com_contenu_doc_stock) && (mvt.getTypeDoc().equals(Constantes.TYPE_FT) || mvt.getTypeDoc().equals(Constantes.TYPE_RE))) {
                champ = new String[]{"id"};
                val = new Object[]{mvt.getIdExterne()};
                YvsComContenuDocStock y = (YvsComContenuDocStock) dao.loadOneByNameQueries("YvsComContenuDocStock.findById", champ, val);
                if (y != null ? y.getId() > 0 : false) {
                    double prix = dao.getPr(mvt.getDepot().getAgence().getId(), mvt.getArticle().getId(), mvt.getDepot().getId(), 0, mvt.getDateDoc(), mvt.getConditionnement().getId());
                    y.setPrix(prix);
                    if (y.getConditionnement().getUnite().equals(y.getConditionnementEntree().getUnite())) {
                        y.setPrixEntree(y.getPrix());
                    } else {
                        prix = convertirUnites(y.getConditionnementEntree().getUnite(), y.getConditionnement().getUnite(), y.getPrix());
                        y.setPrixEntree(prix);
                    }
                    y.setPrixTotal(y.getQuantite() * prix);
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                    mvt.setCoutEntree(prix);
                    mvt.setAuthor(currentUser);
                    mvt.setDateUpdate(new Date());
                    dao.update(mvt);
                    int index = mouvements.indexOf(mvt);
                    if (index > -1) {
                        mouvements.set(index, mvt);
                    }
                    if (succes) {
                        succes();
                    }
                }
            }
        } catch (Exception ex) {
            getException("recalculPrixTransfert", ex);
        }
    }

    public void applyDesactiveSuiviPR(int option, boolean action) {
        String query = null;
        Options[] options = null;
        switch (option) {
            case 0: //tout
                query = "UPDATE yvs_base_mouvement_stock SET calcul_pr=?, execute_trigger='OK' WHERE id IN (SELECT m.id FROM yvs_base_mouvement_stock m "
                        + " INNER JOIN yvs_base_depots d ON d.id=m.depot "
                        + " WHERE m.date_doc<=? AND d.agence=? )";
                options = new Options[]{new Options(action, 1), new Options(dateDebut_, 2), new Options(currentAgence.getId(), 3)};
                break;
            case 1: //article
                if (!mouvements.isEmpty()) {
                    query = "UPDATE yvs_base_mouvement_stock SET calcul_pr=?, execute_trigger='OK' WHERE article= ? AND date_doc<=? ";
                    options = new Options[]{new Options(action, 1), new Options(mouvements.get(0).getArticle().getId(), 2), new Options(dateDebut_, 3)};
                }
                break;
            case 2: //PSF
                query = "UPDATE yvs_base_mouvement_stock SET calcul_pr=?, execute_trigger='OK' WHERE id IN (SELECT m.id FROM yvs_base_mouvement_stock m"
                        + " INNER JOIN yvs_base_articles a ON a.id=m.article "
                        + " INNER JOIN yvs_base_depots d ON d.id=m.depot "
                        + " WHERE m.date_doc<=? AND a.categorie=? AND d.agence=? )";
                options = new Options[]{new Options(action, 1), new Options(dateDebut_, 2), new Options(Constantes.CAT_PSF, 3), new Options(currentAgence.getId(), 4)};

                break;
            case 3://PF
                query = "UPDATE yvs_base_mouvement_stock SET calcul_pr=?, execute_trigger='OK' WHERE id IN (SELECT m.id FROM yvs_base_mouvement_stock m"
                        + " INNER JOIN yvs_base_articles a ON a.id=m.article "
                        + " INNER JOIN yvs_base_depots d ON d.id=m.depot "
                        + " WHERE m.date_doc<=? AND a.categorie=? AND d.agence=? )";
                options = new Options[]{new Options(action, 1), new Options(dateDebut_, 2), new Options(Constantes.CAT_PF, 3), new Options(currentAgence.getId(), 4)};
                break;
            case 4://MP
                query = "UPDATE yvs_base_mouvement_stock SET calcul_pr=?, execute_trigger='OK' WHERE id IN (SELECT m.id FROM yvs_base_mouvement_stock m"
                        + " INNER JOIN yvs_base_articles a ON a.id=m.article "
                        + " INNER JOIN yvs_base_depots d ON d.id=m.depot "
                        + " WHERE m.date_doc<=? AND a.categorie=? AND d.agence=? )";
                options = new Options[]{new Options(action, 1), new Options(dateDebut_, 2), new Options(Constantes.CAT_MP, 3), new Options(currentAgence.getId(), 4)};
                break;
            default:
                query = null;
                break;
        }
        if (query != null) {
            dao.requeteLibre(query, options);
        }
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllMouvement(true, true);
    }

    public Converter getConverterMouv(YvsBaseMouvementStock y) {
        if (y != null ? y.getDescription() != null : false) {
            if (y.getDescription().equals("Consommation") || y.getDescription().equals("Production")) {
                return getCproduction();
            }
        }
        return getConverterStock();
    }
}
