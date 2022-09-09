/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

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
import lymytz.navigue.Navigations;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.ManagedQualite;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.LotReception;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.depot.ManagedDepot;
import yvs.dao.Options;
import yvs.dao.services.commercial.ServiceClotureVente;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.YvsComQualite;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComCoutSupDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.stock.YvsComDocStocksEcart;
import yvs.entity.commercial.stock.YvsComDocStocksValeur;
import yvs.entity.commercial.stock.YvsComReglementEcartStock;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsers;
import yvs.parametrage.entrepot.Depots;
import yvs.production.UtilProd;
import yvs.users.Users;
import yvs.users.UtilUsers;
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
public class ManagedInventaire extends ManagedCommercial<DocStock, YvsComDocStocks> implements Serializable {

    private DocStock docStock = new DocStock();
    private YvsComDocStocks selectDoc;
    private YvsComDocStocks selectDocEntree;
    private YvsComDocStocks selectDocSortie;
    private List<YvsComDocStocks> documents, historiques;
    private PaginatorResult<YvsComDocStocks> p_historique = new PaginatorResult<>();

    private List<YvsComContenuDocStock> contenusEntree, contenusEntreeSave;
    private List<YvsComContenuDocStock> contenusSortie, contenusSortieSave;
    private YvsComContenuDocStock selectContenu;
    private ContenuDocStock contenu = new ContenuDocStock();

    private YvsComDocStocksEcart selectEcart;

    private YvsComParametreStock currentParamStock;

    private List<YvsBaseArticleDepot> articles;
    private PaginatorResult<YvsComContenuDocStock> p_contenu = new PaginatorResult<>();
    private String artSearch;
    private int max = 10;

    private String articleSortie, articleEntree, usersSearch;
    private Boolean haveEcart = null;
    private boolean impactValeurInventaire = false, displayPrixEntree = false, displayPrixSortie = false, displayQteJustifEntree = false, displayQteJustifSortie = false;
    private double montantRetenu = 0;
    private long agenceTiers;

    private String tabIds, tabIds_contenu, egaliteStatut = "!=", valeurBy = "E", ecartBy = "RE"/*/"CR"/*/;
    private boolean selectArt, listArt, deleteRetenue = true, showMontantDetail = true, deleteIfExist = false;
    private long depotSearch;

    private boolean printAll;
    private Date datePrint = new Date();
    private String optionPrint = "V", categoriePrint, orderByPrint = "code";
    private long depotPrint, emplamtPrint;

    private double quantiteeJustifiee = 0;
    private List<Object[]> alertes;

    ServiceClotureVente service;

    private String articleFind;
    private YvsBaseArticleDepot selectArticleDepot;

    public ManagedInventaire() {
        contenusEntree = new ArrayList<>();
        contenusEntreeSave = new ArrayList<>();
        contenusSortie = new ArrayList<>();
        contenusSortieSave = new ArrayList<>();
        documents = new ArrayList<>();
        historiques = new ArrayList<>();
        articles = new ArrayList<>();
        alertes = new ArrayList<>();
    }

    public YvsBaseArticleDepot getSelectArticleDepot() {
        return selectArticleDepot;
    }

    public void setSelectArticleDepot(YvsBaseArticleDepot selectArticleDepot) {
        this.selectArticleDepot = selectArticleDepot;
    }

    public String getArticleFind() {
        return articleFind;
    }

    public void setArticleFind(String articleFind) {
        this.articleFind = articleFind;
    }

    public boolean isDeleteIfExist() {
        return deleteIfExist;
    }

    public void setDeleteIfExist(boolean deleteIfExist) {
        this.deleteIfExist = deleteIfExist;
    }

    public String getOrderByPrint() {
        return orderByPrint;
    }

    public void setOrderByPrint(String orderByPrint) {
        this.orderByPrint = orderByPrint;
    }

    public boolean isDisplayQteJustifEntree() {
        return displayQteJustifEntree;
    }

    public void setDisplayQteJustifEntree(boolean displayQteJustifEntree) {
        this.displayQteJustifEntree = displayQteJustifEntree;
    }

    public boolean isDisplayQteJustifSortie() {
        return displayQteJustifSortie;
    }

    public void setDisplayQteJustifSortie(boolean displayQteJustifSortie) {
        this.displayQteJustifSortie = displayQteJustifSortie;
    }

    public List<Object[]> getAlertes() {
        return alertes;
    }

    public void setAlertes(List<Object[]> alertes) {
        this.alertes = alertes;
    }

    public boolean isDisplayPrixEntree() {
        return displayPrixEntree;
    }

    public double getQuantiteeJustifiee() {
        return quantiteeJustifiee;
    }

    public void setQuantiteeJustifiee(double quantiteeJustifiee) {
        this.quantiteeJustifiee = quantiteeJustifiee;
    }

    public void setDisplayPrixEntree(boolean displayPrixEntree) {
        this.displayPrixEntree = displayPrixEntree;
    }

    public boolean isDisplayPrixSortie() {
        return displayPrixSortie;
    }

    public void setDisplayPrixSortie(boolean displayPrixSortie) {
        this.displayPrixSortie = displayPrixSortie;
    }

    public long getAgenceTiers() {
        return agenceTiers;
    }

    public void setAgenceTiers(long agenceTiers) {
        this.agenceTiers = agenceTiers;
    }

    public boolean isImpactValeurInventaire() {
        return impactValeurInventaire;
    }

    public void setImpactValeurInventaire(boolean impactValeurInventaire) {
        this.impactValeurInventaire = impactValeurInventaire;
    }

    public boolean isShowMontantDetail() {
        return showMontantDetail;
    }

    public void setShowMontantDetail(boolean showMontantDetail) {
        this.showMontantDetail = showMontantDetail;
    }

    public double getMontantRetenu() {
        return montantRetenu;
    }

    public void setMontantRetenu(double montantRetenu) {
        this.montantRetenu = montantRetenu;
    }

    public boolean isDeleteRetenue() {
        return deleteRetenue;
    }

    public void setDeleteRetenue(boolean deleteRetenue) {
        this.deleteRetenue = deleteRetenue;
    }

    public Boolean getHaveEcart() {
        return haveEcart;
    }

    public void setHaveEcart(Boolean haveEcart) {
        this.haveEcart = haveEcart;
    }

    public YvsComDocStocksEcart getSelectEcart() {
        return selectEcart;
    }

    public void setSelectEcart(YvsComDocStocksEcart selectEcart) {
        this.selectEcart = selectEcart;
    }

    public String getUsersSearch() {
        return usersSearch;
    }

    public void setUsersSearch(String usersSearch) {
        this.usersSearch = usersSearch;
    }

    public String getEcartBy() {
        return ecartBy;
    }

    public void setEcartBy(String ecartBy) {
        this.ecartBy = ecartBy;
    }

    public long getEmplamtPrint() {
        return emplamtPrint;
    }

    public void setEmplamtPrint(long emplamtPrint) {
        this.emplamtPrint = emplamtPrint;
    }

    public List<YvsComContenuDocStock> getContenusEntreeSave() {
        return contenusEntreeSave;
    }

    public void setContenusEntreeSave(List<YvsComContenuDocStock> contenusEntreeSave) {
        this.contenusEntreeSave = contenusEntreeSave;
    }

    public List<YvsComContenuDocStock> getContenusSortieSave() {
        return contenusSortieSave;
    }

    public void setContenusSortieSave(List<YvsComContenuDocStock> contenusSortieSave) {
        this.contenusSortieSave = contenusSortieSave;
    }

    public String getArticleSortie() {
        return articleSortie;
    }

    public void setArticleSortie(String articleSortie) {
        this.articleSortie = articleSortie;
    }

    public String getArticleEntree() {
        return articleEntree;
    }

    public void setArticleEntree(String articleEntree) {
        this.articleEntree = articleEntree;
    }

    public String getCategoriePrint() {
        return categoriePrint;
    }

    public void setCategoriePrint(String categoriePrint) {
        this.categoriePrint = categoriePrint;
    }

    public String getValeurBy() {
        return valeurBy;
    }

    public void setValeurBy(String valeurBy) {
        this.valeurBy = valeurBy;
    }

    public boolean isPrintAll() {
        return printAll;
    }

    public void setPrintAll(boolean printAll) {
        this.printAll = printAll;
    }

    public Date getDatePrint() {
        return datePrint;
    }

    public void setDatePrint(Date datePrint) {
        this.datePrint = datePrint;
    }

    public String getOptionPrint() {
        return optionPrint;
    }

    public void setOptionPrint(String optionPrint) {
        this.optionPrint = optionPrint;
    }

    public long getDepotPrint() {
        return depotPrint;
    }

    public void setDepotPrint(long depotPrint) {
        this.depotPrint = depotPrint;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public PaginatorResult<YvsComDocStocks> getP_historique() {
        return p_historique;
    }

    public void setP_historique(PaginatorResult<YvsComDocStocks> p_historique) {
        this.p_historique = p_historique;
    }

    public List<YvsComDocStocks> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsComDocStocks> historiques) {
        this.historiques = historiques;
    }

    public List<YvsBaseArticleDepot> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticleDepot> articles) {
        this.articles = articles;
    }

    public PaginatorResult<YvsComContenuDocStock> getP_contenu() {
        return p_contenu;
    }

    public void setP_contenu(PaginatorResult<YvsComContenuDocStock> p_contenu) {
        this.p_contenu = p_contenu;
    }

    public String getArtSearch() {
        return artSearch;
    }

    public void setArtSearch(String artSearch) {
        this.artSearch = artSearch;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public List<YvsComContenuDocStock> getContenusEntree() {
        return contenusEntree;
    }

    public void setContenusEntree(List<YvsComContenuDocStock> contenusEntree) {
        this.contenusEntree = contenusEntree;
    }

    public List<YvsComContenuDocStock> getContenusSortie() {
        return contenusSortie;
    }

    public void setContenusSortie(List<YvsComContenuDocStock> contenusSortie) {
        this.contenusSortie = contenusSortie;
    }

    public YvsComContenuDocStock getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComContenuDocStock selectContenu) {
        this.selectContenu = selectContenu;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public YvsComDocStocks getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocStocks selectDoc) {
        this.selectDoc = selectDoc;
    }

    public String getTabIds_contenu() {
        return tabIds_contenu;
    }

    public void setTabIds_contenu(String tabIds_contenu) {
        this.tabIds_contenu = tabIds_contenu;
    }

    public DocStock getDocStock() {
        return docStock;
    }

    public void setDocStock(DocStock docStock) {
        this.docStock = docStock;
    }

    public List<YvsComDocStocks> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocStocks> documents) {
        this.documents = documents;
    }

    public ContenuDocStock getContenu() {
        return contenu;
    }

    public void setContenu(ContenuDocStock contenu) {
        this.contenu = contenu;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public YvsComDocStocks getSelectDocEntree() {
        return selectDocEntree;
    }

    public void setSelectDocEntree(YvsComDocStocks selectDocEntree) {
        this.selectDocEntree = selectDocEntree;
    }

    public YvsComDocStocks getSelectDocSortie() {
        return selectDocSortie;
    }

    public void setSelectDocSortie(YvsComDocStocks selectDocSortie) {
        this.selectDocSortie = selectDocSortie;
    }

    public void init() {
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramCom", currentParam);
        }
        if (paramCommercial == null) {
            paramCommercial = currentParam;
        }
    }

    @Override
    public void loadAll() {
        if (currentParamStock == null) {
            currentParamStock = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        loadInfosWarning(false);
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
            if (statut_ != null ? statut_.trim().length() < 1 : true) {
                this.egaliteStatut = "!=";
                this.statut_ = Constantes.ETAT_VALIDE;
                addParamStatut();
            }
        }
        if (agence_ < 1) {
            agence_ = currentAgence.getId();
        }
        load();
        if (agenceTiers < 1) {
            agenceTiers = currentAgence.getId();
        }
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllInventaire(true, true);
    }

    private void load() {
        indiceNumsearch_ = genererPrefixe(Constantes.TYPE_IN_NAME, currentDepot != null ? currentDepot.getId() : 0);
        if ((docStock != null) ? docStock.getSource().getId() < 1 : true) {
            docStock = new DocStock();
            docStock.setMajPr(currentParamStock != null ? currentParamStock.getCalculPr() : true);
            docStock.setTypeDoc(Constantes.TYPE_IN);
            if (docStock.getDocumentLie() == null) {
                docStock.setDocumentLie(new DocStock());
            }
            numSearch_ = "";
        }
        init();
        if (currentParam != null) {
            ecartBy = currentParam.getDocumentGenererFromEcart();
        }
        service = new ServiceClotureVente(dao, currentNiveau, currentUser, currentAgence.getSociete());
    }

    public void loadAllInventaire(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_IN)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                controlListAgence();
                paginator.addParam(new ParametreRequete("y.source.agence.id", "agences", listIdAgences, "IN", "AND"));
                break;
            case 3: //charge tous les document des points de vente où l'utilisateurs est responsable
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                if (!ids.isEmpty()) {
                    p = new ParametreRequete("y.source.id", "depots", ids, " IN ", "AND");
                    paginator.addParam(p);
                } else {
                    paginator.getParams().clear();
                }
                break;
            default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                p = new ParametreRequete("y.source.responsable", "responsable", currentUser.getUsers().getEmploye(), "=", "AND");
                paginator.addParam(p);
                if (currentUser.getUsers().getEmploye() == null) {
                    return;
                }
                break;

        }
        p = new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND");
        paginator.addParam(p);
        paginator.addParam(new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_IN, "=", "AND"));
        documents = paginator.executeDynamicQuery("YvsComDocStocks", "y.dateDoc DESC, y.numDoc DESC", avance, init, (int) imax, dao);
        update("data_inventaire");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComDocStocks> re = paginator.parcoursDynamicData("YvsComDocStocks", "y", "y.dateDoc DESC, y.numDoc DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void init(boolean next) {
        loadAllInventaire(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllInventaire(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllInventaire(true, true);
    }

    public void _choosePaginator(ValueChangeEvent ev) {
        max = (int) ev.getNewValue();
        loadContenus(true, true);
    }

    public void loadHistorique(boolean avance, boolean init) {
        if (p_historique.getParams().contains(new ParametreRequete("source"))) {
            p_historique.addParam(new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_IN, "=", "AND"));
            historiques = p_historique.executeDynamicQuery("YvsComDocStocks", "y.dateDoc", avance, init, 5, dao);
        }
        update("data_inventaire_hist");
    }

    public void loadContenus(boolean avance, boolean init) {
        p_contenu.addParam(new ParametreRequete("y.docStock", "docStock", selectDoc, "=", "AND"));
        contenusEntree = p_contenu.executeDynamicQuery("YvsComContenuDocStock", "y.article.refArt", avance, init, max, dao);
        for (YvsComContenuDocStock c : contenusEntree) {
            c.setQteRestant(c.getQteAttente() - c.getQuantite());
        }
        contenusEntreeSave = new ArrayList<>(contenusEntree);
    }

    public YvsComDocStocks buildDocStock(DocStock y) {
        y.setDestination(y.getSource());
        y.setCreneauDestinataire(y.getCreneauSource());
        return UtilCom.buildDocStock(y, currentAgence.getSociete(), currentUser);
    }

    public YvsComContenuDocStock buildContenuDocStock(ContenuDocStock y) {
        return UtilCom.buildContenuDocStock(y, currentUser);
    }

    public YvsComCoutSupDocStock buildCoutSupDocStock(CoutSupDoc y) {
        YvsComCoutSupDocStock c = new YvsComCoutSupDocStock();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            c.setSupp(y.isSupp());
//            c.setLibelle(y.getLibelle());
            c.setMontant(y.getMontant());
            c.setDocStock(selectDoc);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                c.setAuthor(currentUser);
            }
        }
        return c;
    }

    @Override
    public DocStock recopieView() {
        docStock.setTypeDoc(Constantes.TYPE_IN);
        docStock.setNature(Constantes.INVENTAIRE);
        return docStock;
    }

    public DocStock recopieView(String type) {
        docStock.setTypeDoc(type);
        docStock.setNature(Constantes.INVENTAIRE);
        return docStock;
    }

    public ContenuDocStock recopieViewContenu(DocStock doc) {
        return recopieViewContenu(contenu, doc);
    }

    public ContenuDocStock recopieViewContenu(ContenuDocStock contenu, DocStock doc) {
        contenu.setPrixEntree(contenu.getPrix());
        contenu.setPrixTotal(contenu.getQuantite() * contenu.getPrix());
        cloneObject(contenu.getUniteDestination(), contenu.getConditionnement());
        cloneObject(contenu.getLotEntree(), contenu.getLotSortie());
        contenu.setNew_(true);
        return contenu;
    }

    @Override
    public boolean controleFiche(DocStock bean) {
        if (!_controleFiche_(bean)) {
            return false;
        }
        if ((bean.getSource() != null) ? bean.getSource().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier la source");
            return false;
        }
        if ((bean.getCreneauSource() != null) ? bean.getCreneauSource().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le créneau de source");
            return false;
        }
        if (!bean.isUpdate()) {
            String ref = genererReference(Constantes.TYPE_IN_NAME, bean.getDateDoc(), bean.getSource().getId());
            bean.setNumDoc(ref);
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
        }
        if (!verifyDateStock(bean.getDateDoc(), bean.isUpdate())) {
            return false;
        }
        return bean.getCreneauSource() != null ? verifyTranche(bean.getCreneauSource().getTranche(), bean.getSource(), bean.getDateDoc()) : true;
    }

    private boolean _controleFiche_(DocStock bean) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
        if (!verifyDateStock(bean.getDateDoc(), bean.isUpdate())) {
            return false;
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean _controleFiche_(YvsComDocStocks bean, boolean delete) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.getCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
        return delete ? true : bean.getCreneauSource() != null ? verifyTranche(bean.getCreneauSource().getTranche(), bean.getSource(), bean.getDateDoc()) : true;
    }

    public boolean controleFicheContenu(ContenuDocStock bean) {
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez spécifier l'article");
            return false;
        }
        if ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez spécifier le conditionnement de l'article");
            return false;
        }
        if (bean.getArticle().isRequiereLot()) {
            if (bean.getLotSortie() != null ? bean.getLotSortie().getId() < 1 : true) {
                getErrorMessage("Un numéro de lot est requis pour cet article dans le dépôt");
                return false;
            }
        }
        if (docStock.getId() < 1) {
            if (!_saveNew(Constantes.TYPE_IN)) {
                return false;
            }
            docStock.setId(selectDoc.getId());
        } else {
            if (!_controleFiche_(docStock)) {
                return false;
            }
        }
        if (bean.getQuantite_() == bean.getArticle().getStock()) {
            getWarningMessage("La différence à sauvegarder est nulle!");
            return false;
        }
        YvsComContenuDocStock y;
        if (bean.getArticle().isRequiereLot()) {
            y = (YvsComContenuDocStock) dao.loadOneByNameQueries("YvsComContenuDocStock.findByInventaireUniteLot", new String[]{"conditionnement", "lot", "document"}, new Object[]{new YvsBaseConditionnement(bean.getConditionnement().getId()), new YvsComLotReception(bean.getLotSortie().getId()), selectDoc});
        } else {
            y = (YvsComContenuDocStock) dao.loadOneByNameQueries("YvsComContenuDocStock.findByInventaireUnite", new String[]{"conditionnement", "document"}, new Object[]{new YvsBaseConditionnement(bean.getConditionnement().getId()), selectDoc});
        }
        if (y != null ? y.getId() > 0 ? !y.getId().equals(bean.getId()) : false : false) {
            if (deleteIfExist) {
                dao.delete(y);
                switch (bean.getDocStock().getTypeDoc()) {
                    case Constantes.TYPE_ES:
                        contenusEntree.remove(y);
                        contenusEntreeSave.remove(y);
                        break;
                    case Constantes.TYPE_SS:
                        contenusSortie.remove(y);
                        contenusSortieSave.remove(y);
                        break;
                }
            } else {
                getErrorMessage("Vous avez déjà ajouté cet article");
                return false;
            }
        }
        //Si quantité supérieure au stock théorique alors on constate un exédent dans un doc d'entréé en stock
        if (bean.getQuantite_() > bean.getArticle().getStock()) {
            if (selectDocEntree == null) {
                selectDocEntree = saveDocInvenatire(new YvsComDocStocks(selectDoc), Constantes.TYPE_ES);
                if (selectDocEntree != null ? selectDocEntree.getId() != null ? selectDocEntree.getId() <= 0 : true : true) {
                    getErrorMessage("Le document d'ajustement de stocks n'a pas pue être généré !");
                    return false;
                }
            }
            bean.setDocStock(UtilCom.buildBeanDocStock(selectDocEntree));
        } else if (bean.getQuantite_() < bean.getArticle().getStock()) {
            if (selectDocSortie == null) {
                selectDocSortie = saveDocInvenatire(new YvsComDocStocks(selectDoc), Constantes.TYPE_SS);
                if (selectDocSortie != null ? selectDocSortie.getId() != null ? selectDocSortie.getId() <= 0 : true : true) {
                    getErrorMessage("Le document d'ajustement de stocks n'a pas pue être généré !");
                    return false;
                }
            }
            bean.setDocStock(UtilCom.buildBeanDocStock(selectDocSortie));
        }
        return true;
    }

    @Override
    public void onSelectDistant(YvsComDocStocks y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Inventaires", "modGescom", "smenInventaire", true);
            }
        }
    }

    @Override
    public void populateView(DocStock bean) {
        cloneObject(docStock, bean);
        ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (s != null) {
            ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
                int index = s.getDepots().indexOf(new YvsBaseDepots(docStock.getSource().getId()));
                if (index > -1) {
                    YvsBaseDepots y = s.getDepots().get(index);
                    Depots d = UtilCom.buildBeanDepot(y);
                    cloneObject(docStock.getSource(), d);
                    if (a != null) {
                        a.loadAllArticle(y);
                    }
                    s.loadCreneauxByDate(y, docStock.getDateDoc());
                }
            } else {
                docStock.getCreneauSource().setId(0);
                docStock.setSource(new Depots());
                s.getCreneaux().clear();
                if (a != null) {
                    a.getArticlesDebut().clear();
                }
            }
        }
        p_historique.addParam(new ParametreRequete("y.source", "source", new YvsBaseDepots(docStock.getSource().getId()), "=", "AND"));
        chooseCreneauSource();
    }

    public void populateViewContenu(ContenuDocStock bean) {
        bean.setUpdate(true);
        bean.getArticle().setStock_(dao.stocks(bean.getArticle().getId(), docStock.getCreneauDestinataire().getTranche().getId(), docStock.getDestination().getId(), 0, 0, docStock.getDateDoc(), bean.getConditionnement().getId(), bean.getLotSortie().getId()));
        bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), docStock.getCreneauSource().getTranche().getId(), docStock.getSource().getId(), 0, 0, docStock.getDateDoc(), bean.getConditionnement().getId(), bean.getLotSortie().getId()));
        bean.getArticle().setPuv(dao.getPuv(bean.getArticle().getId(), bean.getQuantite(), 0, 0, docStock.getSource().getId(), 0, docStock.getDateDoc(), bean.getConditionnement().getId()));
        bean.setPrix(dao.getPua(bean.getArticle().getId(), 0));
        String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
        Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(bean.getArticle().getId(), 1), new Options(docStock.getSource().getId(), 2)});
        bean.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
        selectArt = true;
        cloneObject(contenu, bean);
    }

    @Override
    public void resetFiche() {
        _resetFiche();
        historiques.clear();
        selectDocEntree = selectDocSortie = null;
        contenusEntree.clear();
        contenusEntreeSave.clear();
        contenusSortie.clear();
        contenusSortieSave.clear();
        update("blog_form_inventaire");
    }

    public void _resetFiche() {
        docStock = new DocStock();
        selectDoc = new YvsComDocStocks();
        contenusEntree.clear();
        contenusEntreeSave.clear();
        ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (w != null) {
            w.getCreneaux().clear();
        }
        tabIds = "";
        update("form_inventaire");
        update("blog_form_contenu_inventaire");
    }

    public void resetFicheContenu() {
        resetFiche(contenu);
        contenu.setArticle(new Articles());
        contenu.setConditionnement(new Conditionnement());
        contenu.setLotSortie(new LotReception());
        tabIds_contenu = "";
        selectArt = false;
        listArt = false;
        selectContenu = null;
        update("blog_form_contenu_transfert_stock");
    }

    @Override
    public boolean saveNew() {
        if (_saveNew(Constantes.TYPE_IN)) {
            succes();
            actionOpenOrResetAfter(this);
            return true;
        }
        return false;
    }

    public boolean _saveNew(String type) {
        try {
            DocStock bean = recopieView(type);
            if (bean.getId() > 0) {
                if (!autoriser("gescom_inv_update")) {
                    openNotAcces();
                    return false;
                }
            }
            if (controleFiche(bean)) {
                selectDoc = buildDocStock(bean);
                if (bean.getId() < 1) {
                    selectDoc.setId(null);
                    selectDoc = (YvsComDocStocks) dao.save1(selectDoc);
                    bean.setId(selectDoc.getId());
                    docStock.setId(selectDoc.getId());
//                    saveContenus();
                } else {
                    dao.update(selectDoc);
                    if (selectDocEntree != null) {
                        selectDocEntree.setDateDoc(selectDoc.getDateDoc());
                        selectDocEntree.setSource(selectDoc.getSource());
                        selectDocEntree.setDestination(selectDoc.getSource());
                        selectDocEntree.setCreneauSource(selectDoc.getCreneauSource());
                        selectDocEntree.setCreneauDestinataire(selectDoc.getCreneauSource());
                        dao.update(selectDocEntree);
                    }
                    if (selectDocSortie != null) {
                        selectDocSortie.setDateDoc(selectDoc.getDateDoc());
                        selectDocSortie.setSource(selectDoc.getSource());
                        selectDocSortie.setDestination(selectDoc.getSource());
                        selectDocSortie.setCreneauSource(selectDoc.getCreneauSource());
                        selectDocSortie.setCreneauDestinataire(selectDoc.getCreneauSource());
                        dao.update(selectDocSortie);
                    }
                }
                int idx = documents.indexOf(selectDoc);
                if (idx >= 0) {
                    documents.set(idx, selectDoc);
                } else {
                    documents.add(0, selectDoc);
                }
                docStock.setNumDoc(bean.getNumDoc());
                docStock.setUpdate(true);
                update("data_inventaire");
                update("data_inventaire_hist");
                update("form_entete_inventaire");
                update("form_inventaire");
                update("blog_form_contenu_inventaire");
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
            return false;
        }
    }

    public YvsComDocStocks saveDocInvenatire(YvsComDocStocks doc, String type) {
        return saveDocInvenatire(doc, new YvsComDocStocks(docStock.getId()), type);
    }

    public YvsComDocStocks saveDocInvenatire(YvsComDocStocks entity, YvsComDocStocks inv, String type) {
        YvsComDocStocks doc = new YvsComDocStocks(entity);
        doc.setAuthor(currentUser);
        doc.setValiderBy(null);
        doc.setDateValider(null);
        doc.setTypeDoc(type);
        doc.setDescription("Document d'ajustement d'inventaire !");
        doc.setNature(Constantes.OP_AJUSTEMENT_STOCK);
        doc.setCreneauDestinataire(doc.getCreneauSource());
        doc.setDestination(doc.getSource());
        doc.setSociete(currentAgence.getSociete());
        doc.setDocumentLie(inv);
        String ref;
        if (type.equals(Constantes.TYPE_ES)) {
            ref = genererReference(Constantes.TYPE_ES_NAME, doc.getDateDoc(), doc.getSource().getId());
        } else {
            ref = genererReference(Constantes.TYPE_SS_NAME, doc.getDateDoc(), doc.getSource().getId());
        }
        if (ref == null || ref.trim().equals("")) {
            return null;
        }
        doc.setNumDoc(ref);
        doc.setId(null);
        return (YvsComDocStocks) dao.save1(doc);
    }

    public void saveNewContenu() {
        saveNewContenu(contenu, true);
    }

    public void saveNewContenu(ContenuDocStock contenu, boolean msg) {
        try {
            ContenuDocStock bean = recopieViewContenu(contenu, docStock);
            if (controleFicheContenu(bean)) {
                bean.setQuantite(Math.abs(bean.getQuantite_() - bean.getArticle().getStock()));
                YvsComContenuDocStock en = buildContenuDocStock(bean);
                if (!bean.isUpdate()) {
                    en.setId(null);
                    en.setImpactValeurInventaire(bean.getDocStock().getTypeDoc().equals(Constantes.TYPE_SS) ? true : impactValeurInventaire);
                    en = (YvsComContenuDocStock) dao.save1(en);
                    if (bean.getDocStock().getTypeDoc().equals(Constantes.TYPE_ES)) {
                        contenusEntree.add(0, en);
                        contenusEntreeSave.add(0, en);
                    } else if (bean.getDocStock().getTypeDoc().equals(Constantes.TYPE_SS)) {
                        contenusSortie.add(0, en);
                        contenusSortieSave.add(0, en);
                    }
                } else {
                    dao.update(en);
                    if (bean.getDocStock().getTypeDoc().equals(Constantes.TYPE_ES)) {
                        int idx = contenusEntree.indexOf(en);
                        if (idx >= 0) {
                            contenusEntree.set(idx, en);
                        } else {
                            contenusEntree.add(0, en);
                        }
                        idx = contenusEntreeSave.indexOf(en);
                        if (idx >= 0) {
                            contenusEntreeSave.set(idx, en);
                        } else {
                            contenusEntreeSave.add(0, en);
                        }
                    } else if (bean.getDocStock().getTypeDoc().equals(Constantes.TYPE_SS)) {
                        int idx = contenusSortie.indexOf(en);
                        if (idx >= 0) {
                            contenusSortie.set(idx, en);
                        } else {
                            contenusSortie.add(0, en);
                        }
                        idx = contenusSortieSave.indexOf(en);
                        if (idx >= 0) {
                            contenusSortieSave.set(idx, en);
                        } else {
                            contenusSortieSave.add(0, en);
                        }
                    }
                }
                if (msg) {
                    succes();
                }
                resetFicheContenu();
                update("blog_form_contenu_inventaire");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveContenus() {
        if (contenusEntreeSave != null) {
            List<YvsComContenuDocStock> l = new ArrayList<>();
            l.addAll(contenusEntreeSave);
            for (YvsComContenuDocStock c : l) {
                c.setQuantite(c.getQteAttente() - c.getQteRestant());
                c.setDocStock(selectDoc);
                if (c.getQuantite() != 0) {
                    c.setId(null);
                    c = (YvsComContenuDocStock) dao.save1(c);
                }
            }
            loadContenus(true, true);

            update("data_contenu_inventaire");
        }
    }

    public void updateContenu(RowEditEvent ev) {
        YvsComContenuDocStock c = (YvsComContenuDocStock) ev.getObject();
        ManagedQualite s = (ManagedQualite) giveManagedBean(ManagedQualite.class);
        if (s != null) {
            int idx = s.getQualites().indexOf(new YvsComQualite(c.getIdQualite()));
            if (idx > -1) {
                c.setQualite(s.getQualites().get(idx));
            }
        }
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (!_controleFiche_(selectDoc, false)) {
                return;
            }
            champ = new String[]{"docStock", "id"};
            val = new Object[]{selectDoc, c.getId()};
            List<YvsComContenuDocStock> lc = dao.loadNameQueries("YvsComContenuDocStock.findByDocId", champ, val);
            if (lc != null ? !lc.isEmpty() : false) {
                dao.update(c);
            } else {
                int i = contenusEntree.indexOf(c);
                c.setId(null);
                c = (YvsComContenuDocStock) dao.save1(c);
                contenusEntree.set(i, c);
                i = contenusEntreeSave.indexOf(c);
                contenusEntreeSave.set(i, c);
            }
            succes();
            update("data_contenu_inventaire");
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("gescom_inv_delete")) {
                openNotAcces();
                return;
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComDocStocks> list = new ArrayList<>();
                YvsComDocStocks bean;
                for (Long ids : l) {
                    bean = documents.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    if (!_controleFiche_(bean, true)) {
                        return;
                    }
                    dao.delete(bean);
                    if (bean.getId() == docStock.getId()) {
                        resetFiche();
                    }
                }
                documents.removeAll(list);
                succes();
                update("data_inventaire");
                tabIds = "";
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComDocStocks y) {
        selectDoc = y;
        selectDocEntree = null;
        selectDocSortie = null;
        if (y.getDocuments() != null) {
            for (YvsComDocStocks d : y.getDocuments()) {
                if (d.getTypeDoc().equals(Constantes.TYPE_ES)) {
                    selectDocEntree = d;
                } else if (d.getTypeDoc().equals(Constantes.TYPE_SS)) {
                    selectDocSortie = d;
                }
            }
        }
    }

    public void deleteBean_() {
        try {
            if (!autoriser("gescom_inv_delete")) {
                openNotAcces();
                return;
            }
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc, true)) {
                    return;
                }
                if (selectDocEntree != null) {
                    selectDocEntree.setAuthor(currentUser);
                    dao.delete(selectDocEntree);
                    selectDocEntree = null;
                    selectDoc.getDocuments().remove(selectDocEntree);
                }
                if (selectDocSortie != null) {
                    selectDocSortie.setAuthor(currentUser);
                    dao.delete(selectDocSortie);
                    selectDocSortie = null;
                    selectDoc.getDocuments().remove(selectDocSortie);
                }
                try {
                    selectDoc.setAuthor(currentUser);
                    dao.delete(selectDoc);
                } catch (Exception ex) {
                    dao.delete(new YvsComDocStocks(selectDoc.getId()));
                    getException("Error Suppression : " + ex.getMessage(), ex);
                }
                documents.remove(selectDoc);
                if (selectDoc.getId().equals(docStock.getId())) {
                    resetFiche();
                }
                succes();
                update("data_inventaire");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenu() {
        try {
            if (!_controleFiche_(selectDoc, false)) {
                return;
            }
            if ((tabIds_contenu != null) ? !tabIds_contenu.equals("") : false) {
                String[] tab = tabIds_contenu.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComContenuDocStock bean = contenusEntree.get(contenusEntree.indexOf(new YvsComContenuDocStock(id)));
                    bean.setAuthor(currentUser);
                    dao.delete(bean);
                    contenusEntree.remove(bean);
                    contenusEntreeSave.remove(bean);
                    docStock.setMontantTotal(docStock.getMontantTotal() - bean.getPrixTotal());
                    if (id == contenu.getId()) {
                        resetFicheContenu();
                    }
                }
                succes();
                update("data_contenu_ordre_transfert");
                update("data_ordre_transfert");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenu_(YvsComContenuDocStock y) {
        selectContenu = y;
    }

    public void deleteBeanContenu_() {
        try {
            if (!_controleFiche_(selectDoc, false)) {
                return;
            }
            if (selectContenu != null) {
                selectContenu.setAuthor(currentUser);
                dao.delete(selectContenu);
                if (selectContenu.getDocStock().getTypeDoc().equals(Constantes.TYPE_ES)) {
                    contenusEntree.remove(selectContenu);
                    contenusEntreeSave.remove(selectContenu);
                    update("data_contenu_inventaire_E");
                } else {
                    contenusSortie.remove(selectContenu);
                    contenusSortieSave.remove(selectContenu);
                    update("data_contenu_inventaire_S");
                }
                if (selectContenu.getId() == contenu.getId()) {
                    resetFicheContenu();
                    update("form_contenu_entree_stock");
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void onSelectObject(YvsComDocStocks y, boolean historique) {
        selectDoc = y;
        selectDocEntree = null;
        selectDocSortie = null;
        populateView(UtilCom.buildBeanDocStock(selectDoc));
        if (y.getDocuments() != null) {
            for (YvsComDocStocks d : y.getDocuments()) {
                if (d.getTypeDoc().equals(Constantes.TYPE_ES)) {
                    selectDocEntree = d;
                } else if (d.getTypeDoc().equals(Constantes.TYPE_SS)) {
                    selectDocSortie = d;
                }
            }
        }
        contenusEntree.clear();
        if (selectDocEntree != null) {
            contenusEntree = selectDocEntree.getContenus();
            if (contenusEntree != null ? contenusEntree.isEmpty() : true) {
                contenusEntree = dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{selectDocEntree});
            }
        }
        contenusEntreeSave = new ArrayList<>(contenusEntree);
        contenusSortie.clear();
        if (selectDocSortie != null) {
            contenusSortie = selectDocSortie.getContenus();
            if (contenusSortie != null ? contenusSortie.isEmpty() : true) {
                contenusSortie = dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{selectDocSortie});
            }
        }
        contenusSortieSave = new ArrayList<>(contenusSortie);
        resetFicheContenu();
        update("blog_form_inventaire");
    }

    @Override
    public void onSelectObject(YvsComDocStocks y) {
        onSelectObject(y, true);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocStocks y = (YvsComDocStocks) ev.getObject();
            onSelectObject(y, true);
            tabIds = "" + documents.indexOf(y);
        }
    }

    public void loadOnView_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocStocks y = (YvsComDocStocks) ev.getObject();
            onSelectObject(y, false);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        _resetFiche();
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocStock bean = (YvsComContenuDocStock) ev.getObject();
            populateViewContenu(UtilCom.buildBeanContenuDocStock(bean));
            update("form_contenu_transfert_stock");
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheContenu();
        update("form_contenu_transfert_stock");
    }

    public void changeAllImpactInValorise(boolean impact, List<YvsComContenuDocStock> contenus) {
        try {
            if (!autoriser("gescom_inv_update_valeur")) {
                openNotAcces();
                return;
            }
            for (YvsComContenuDocStock y : contenus) {
                y.setImpactValeurInventaire(impact);
                y.setAuthor(currentUser);
                y.setDateUpdate(new Date());
                dao.update(y);
            }
            if (selectDoc.getValeur() != null ? selectDoc.getValeur().getId() > 0 : false) {
                double montant = selectDoc.calculMontantTotal(dao, selectDoc.getValeur(), true);
                selectDoc.getValeur().setMontant(montant);
                selectDoc.getValeur().setAuthor(currentUser);
                selectDoc.getValeur().setDateUpdate(new Date());
                dao.update(selectDoc.getValeur());
            } else {
                selectDoc.setValeur(new YvsComDocStocksValeur(null));
                double montant = selectDoc.calculMontantTotal(dao, selectDoc.getValeur(), true);
                selectDoc.getValeur().setDocStock(selectDoc);
                selectDoc.getValeur().setMontant(montant);
                selectDoc.getValeur().setAuthor(currentUser);
                selectDoc.getValeur().setDateUpdate(new Date());
                selectDoc.setValeur((YvsComDocStocksValeur) dao.save1(selectDoc.getValeur()));
            }
            docStock.getValeur().setMontant(selectDoc.getValeur().getMontant());
            docStock.getValeur().setId(selectDoc.getValeur().getId());
            update("blog-ecart_value");
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void changeImpactInValorise(YvsComContenuDocStock y, List<YvsComContenuDocStock> contenus) {
        changeImpactInValorise(!y.getImpactValeurInventaire(), y, contenus);
    }

    public void changeImpactInValorise(boolean impact, YvsComContenuDocStock y, List<YvsComContenuDocStock> contenus) {
        try {
            if (!autoriser("gescom_inv_update_valeur")) {
                openNotAcces();
                return;
            }
            y.setImpactValeurInventaire(impact);
            y.setAuthor(currentUser);
            y.setDateUpdate(new Date());
            dao.update(y);
            int index = contenus.indexOf(y);
            if (index > -1) {
                contenus.set(index, y);
            }
            if (selectDoc.getValeur() != null ? selectDoc.getValeur().getId() > 0 : false) {
                double montant = selectDoc.calculMontantTotal(dao, selectDoc.getValeur(), true);
                selectDoc.getValeur().setMontant(montant);
                selectDoc.getValeur().setAuthor(currentUser);
                selectDoc.getValeur().setDateUpdate(new Date());
                dao.update(selectDoc.getValeur());
            } else {
                selectDoc.setValeur(new YvsComDocStocksValeur(null));
                double montant = selectDoc.calculMontantTotal(dao, selectDoc.getValeur(), true);
                selectDoc.getValeur().setDocStock(selectDoc);
                selectDoc.getValeur().setMontant(montant);
                selectDoc.getValeur().setAuthor(currentUser);
                selectDoc.getValeur().setDateUpdate(new Date());
                selectDoc.setValeur((YvsComDocStocksValeur) dao.save1(selectDoc.getValeur()));
            }
            docStock.getValeur().setMontant(selectDoc.getValeur().getMontant());
            docStock.getValeur().setId(selectDoc.getValeur().getId());
            update("blog-ecart_value");
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void changeCalculPr(YvsComContenuDocStock y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (!autoriser("recalcul_pr")) {
                    openNotAcces();;
                    return;
                }
                y.setCalculPr(!y.getCalculPr());
                y.setAuthor(currentUser);
                y.setDateUpdate(new Date());
                dao.update(y);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!");
            getException("ManagedInventaire (changeCalculPr)", ex);
        }
    }

    public void chooseDepotSource() {
        ParametreRequete p = new ParametreRequete("y.source", "source", null);
        ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (s != null) {
            ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
                int idx = s.getDepots().indexOf(new YvsBaseDepots(docStock.getSource().getId()));
                if (idx > -1) {
                    YvsBaseDepots y = s.getDepots().get(idx);
                    cloneObject(docStock.getSource(), UtilCom.buildBeanDepot(y));
                    if (a != null) {
                        a.loadAllArticle(y);
                    }
                    s.loadCreneauxByDate(y, docStock.getDateDoc());
                    p = new ParametreRequete("y.source", "source", y, "=", "AND");
                }
                if (docStock.isPassation()) {
                    determinedCurrentTranche();
                }
            } else {
                docStock.getCreneauSource().setId(0);
                docStock.setSource(new Depots());
                s.getCreneaux().clear();
                if (a != null) {
                    a.getArticlesDebut().clear();
                }
            }
        }

        if (selectDoc != null) {
            selectDoc.setSource(UtilCom.buildDepot(docStock.getSource(), currentUser, currentAgence));
        } else {
            selectDoc = new YvsComDocStocks();
            selectDoc.setSource(UtilCom.buildDepot(docStock.getSource(), currentUser, currentAgence));
        }
        p_historique.addParam(p);
        loadHistorique(true, true);
    }

    public void chooseCreneauSource() {
        ParametreRequete p = new ParametreRequete("y.creneauSource", "creneauSource", null);
        if ((docStock.getCreneauSource() != null) ? docStock.getCreneauSource().getId() > 0 : false) {
            ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (s != null) {
                int idx = s.getCreneaux().indexOf(new YvsComCreneauDepot(docStock.getCreneauSource().getId()));
                if (idx > -1) {
                    YvsComCreneauDepot y = s.getCreneaux().get(idx);
                    cloneObject(docStock.getCreneauSource(), UtilCom.buildBeanCreneau(y));
                    p = new ParametreRequete("y.creneauSource", "creneauSource", y, "=", "AND");
                    s.loadUsers(y, docStock.getDateDoc());
                }
                if (s.getUsers() != null ? !s.getUsers().isEmpty() : false) {
                    docStock.setEditeur(UtilUsers.buildSimpleBeanUsers(s.getUsers().get(0)));
                }
            }
        } else {
            docStock.setCreneauSource(new Creneau());
        }
        p_historique.addParam(p);
        loadHistorique(true, true);
    }

    public void chooseResponsable() {
        ParametreRequete p = new ParametreRequete("y.editeur", "editeur", null);
        if ((docStock.getEditeur() != null) ? docStock.getEditeur().getId() > 0 : false) {
            ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (s != null) {
                int idx = s.getUsers().indexOf(new YvsComCreneauDepot(docStock.getCreneauSource().getId()));
                if (idx > -1) {
                    YvsUsers y = s.getUsers().get(idx);
                    cloneObject(docStock.getEditeur(), UtilUsers.buildSimpleBeanUsers(y));
                    p = new ParametreRequete("y.editeur", "editeur", y, "=", "AND");
                }
            }
        } else {
            docStock.setEditeur(new Users());
        }
        p_historique.addParam(p);
        loadHistorique(true, true);
    }

    public void chooseDateDoc() {
        if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
            ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (s != null) {
                int index = s.getDepots().indexOf(new YvsBaseDepots(docStock.getSource().getId()));
                if (index > -1) {
                    YvsBaseDepots y = s.getDepots().get(index);
                    s.loadCreneauxByDate(y, docStock.getDateDoc());
                    if (docStock.isPassation()) {
                        determinedCurrentTranche();
                    }
                }
            }
        }
    }

    private void determinedCurrentTranche() {
        if (docStock.getSource() != null ? docStock.getSource().getId() > 0 : false) {
            boolean continu = true;
            YvsBaseDepots depot = new YvsBaseDepots(docStock.getSource().getId());
            YvsComDocStocks y = null;
            List<YvsComDocStocks> list = dao.loadNameQueries("YvsComDocStocks.findBySourceDate", new String[]{"depot", "dateDoc", "typeDoc"}, new Object[]{depot, docStock.getDateDoc(), Constantes.TYPE_IN});
            if (list != null ? !list.isEmpty() : false) {
                y = list.get(0);
            }
            if (y != null ? y.getId() > 0 : false) {
                ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                if (s != null ? !s.getCreneaux().isEmpty() : false) {
                    boolean trouve = false;
                    boolean current = false;
                    for (YvsComCreneauDepot c : s.getCreneaux()) {
                        if (current) {
                            docStock.setCreneauSource(new Creneau(c.getId()));
                            trouve = true;
                            break;
                        }
                        if (y.getCreneauSource().equals(c)) {
                            current = true;
                        }
                    }
                    if (!trouve) {
                        docStock.setCreneauSource(new Creneau(s.getCreneaux().get(0).getId()));

                        Calendar c = Calendar.getInstance();
                        c.setTime(docStock.getDateDoc());
                        c.add(Calendar.DATE, 1);
                        docStock.setDateDoc(c.getTime());
                        continu = false;
                        chooseDateDoc();
                    }
                }
            } else {
                ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                if (s != null ? !s.getCreneaux().isEmpty() : false) {
                    docStock.setCreneauSource(new Creneau(s.getCreneaux().get(0).getId()));
                }
            }
            if (continu) {
                chooseCreneauSource();
            }
        }
    }

    public void chooseStatut(ValueChangeEvent ev) {
        statut_ = ((String) ev.getNewValue());
        addParamStatut();
    }

    public void addParamStatut() {
        ParametreRequete p;
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statut_, egaliteStatut, "AND");
        } else {
            p = new ParametreRequete("y.statut", "statut", null);
        }
        paginator.addParam(p);
        loadAllInventaire(true, true);
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        paginator.addParam(new ParametreRequete("y.cloturer", "cloturer", cloturer_, "=", "AND"));
        loadAllInventaire(true, true);
    }

    public void chooseDateSearch() {
        ParametreRequete p;
        if (date_) {
            p = new ParametreRequete("y.dateDoc", "dateDoc", dateDebut_, dateFin_, "BETWEEN", "AND");
        } else {
            p = new ParametreRequete("y.dateDoc", "dateDoc", null);
        }
        paginator.addParam(p);
        loadAllInventaire(true, true);
    }

    public void addParamAgence() {
        ParametreRequete p;
        if (agence_ > 0) {
            p = new ParametreRequete("y.source.agence", "agence", new YvsAgences(agence_), "=", "AND");
        } else {
            p = new ParametreRequete("y.source.agence", "agence", null);
        }
        paginator.addParam(p);
        loadAllInventaire(true, true);
        _loadDepot();
    }

    public void chooseDepotSearch() {
        ParametreRequete p;
        if (depotSearch > 0) {
            p = new ParametreRequete("y.source", "source", new YvsBaseDepots(depotSearch), "=", "AND");
        } else {
            p = new ParametreRequete("y.source", "source", null);
        }
        paginator.addParam(p);
        loadAllInventaire(true, true);
    }

    public void addParamArticle() {
        ParametreRequete p;
        if (artSearch != null ? artSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "refArt", artSearch.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "refArt", artSearch.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "refArt", artSearch.toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p = new ParametreRequete("y.article.refArt", "refArt", null);
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void searchArticle() {
        if (contenu != null ? contenu.getArticle() != null : false) {
            String num = contenu.getArticle().getRefArt();
            contenu.getArticle().setDesignation("");
            contenu.getArticle().setError(true);
            contenu.getArticle().setId(0);
            listArt = false;
            selectArt = false;
            ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (m != null ? selectDoc != null ? selectDoc.getSource() != null : false : false) {
                m.setEntityDepot(selectDoc.getSource());
                Articles y = m.searchArticleActifByDepot(num, true);
                listArt = y.isListArt();
                selectArt = y.isSelectArt();
                if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                    if (m.getArticlesResult().size() > 1) {
                        update("data_articles_inventaire");
                    } else {
                        loadInfosArticle(y, true);
                    }
                    contenu.getArticle().setError(false);
                } else {
                    Conditionnement c = m.searchArticleActifByCodeBarre(num);
                    if (m.getConditionnements() != null ? !m.getConditionnements().isEmpty() : false) {
                        if (m.getConditionnements().size() > 1) {
                            update("data_conditionnements_inventaire");
                        } else {
                            chooseConditionnement(c);
                        }
                        listArt = true;
                        selectArt = true;
                    }
                }
            }
        }
    }

    public void selectDepot() {
        System.err.println(selectDoc.getSource().getDesignation() + " select");
    }

    public void initArticles() {
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            selectDoc.setSource(UtilCom.buildDepot(docStock.getSource(), currentUser, currentAgence));
            m.setEntityDepot(UtilCom.buildDepot(docStock.getSource(), currentUser, currentAgence));
            m.initArticlesByDepot(contenu.getArticle());
            listArt = contenu.getArticle().isListArt();
            selectArt = contenu.getArticle().isSelectArt();
        }
        update("data_articles_inventaire");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleDepot bean = (YvsBaseArticleDepot) ev.getObject();
            Articles a = UtilProd.buildBeanArticles(bean.getArticle());
            a.setRequiereLot(bean.getRequiereLot());
            a.setSellWithOutStock(bean.getSellWithoutStock());
            loadInfosArticle(a, false);
            listArt = false;
            update("data_articles_inventaire");
        }
    }

    public void loadOnViewConditionnement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseConditionnement bean = (YvsBaseConditionnement) ev.getObject();
            chooseConditionnement(UtilProd.buildBeanConditionnement(bean));
            listArt = false;
            update("data_articles_inventaire");
        }
    }

    public void loadInfosArticle(Articles art, boolean init) {
        if ((art != null) ? art.getId() > 0 : false) {
            //charge le conditionnement
            if (init) {
                if (!art.getConditionnements().isEmpty()) {
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(art.getConditionnements().get(0)));
                }
            }
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(art.getId())});
            art.setConditionnements(unites);
            art.setStock(dao.stocks(art.getId(), (docStock.isPassation() ? docStock.getCreneauSource().getTranche().getId() : 0), docStock.getSource().getId(), 0, currentAgence.getSociete().getId(), docStock.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLotSortie().getId()));
            contenu.setQuantite(art.getStock());
            art.setPuv(dao.getPuv(art.getId(), contenu.getQuantite(), 0, 0, docStock.getSource().getId(), 0, docStock.getDateDoc(), contenu.getConditionnement().getId()));
            contenu.setPrix(dao.getPr(art.getId(), docStock.getSource().getId(), 0, docStock.getDateDoc(), contenu.getConditionnement().getId()));
            art.setPua(dao.getPua(art.getId(), 0));
            selectArt = true;
            cloneObject(contenu.getArticle(), art);
            update("fos-value_unite_inventaire");
        } else {
            contenu.getArticle().setError(true);
        }
    }

    private void chooseConditionnement(Conditionnement c) {
        if (c != null ? c.getId() > 0 : false) {
            loadInfosArticle(c.getArticle(), false);
            cloneObject(contenu.getConditionnement(), c);
            chooseConditionnement();
        }
    }

    public void chooseConditionnement() {
        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
            if (contenu.getArticle() != null) {
                int idx = contenu.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(contenu.getConditionnement().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = contenu.getArticle().getConditionnements().get(idx);
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(y));
                }
            }
        }
        loadInfosArticle(contenu.getArticle(), false);
    }

    public void openArticle() {
        if (selectDoc != null ? selectDoc.getId() < 1 : true) {
            if (docStock.getSource() != null ? docStock.getSource().getId() > 0 : false) {
                articles.clear();
                openDialog("dlgListArticles");
                update("data_articles_inventaire");
            } else {
                getErrorMessage("Vous devez selectionner le dépot");
            }
        } else {
            getErrorMessage("Vous ne pouvez pas ajouter des articles dans cet inventaire");
        }
    }

    public void selectAllArticle() {
        ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (a != null) {
            if (articles.isEmpty()) {
                articles.addAll(a.getArticlesDebut());
            } else {
                if (articles.size() == a.getArticlesDebut().size()) {
                    articles.clear();
                } else {
                    articles.clear();
                    articles.addAll(a.getArticlesDebut());
                }
            }
        }
        update("data_articles_inventaire");
    }

    public void calculStock() {
        if (!articles.isEmpty()) {
            if (docStock.getSource() != null ? docStock.getSource().getId() > 0 : false) {
                if (docStock.getCreneauSource() != null ? docStock.getCreneauSource().getId() > 0 : false) {
                    if (!docStock.isUpdate()) {
                        contenusEntree.clear();
                    }
                    for (YvsBaseArticleDepot y : articles) {
                        champ = new String[]{"docStock", "article"};
                        val = new Object[]{selectDoc, y.getArticle()};
                        List<YvsComContenuDocStock> lc = dao.loadNameQueries("YvsComContenuDocStock.findByDocArticle", champ, val);
                        if (lc != null ? lc.isEmpty() : true) {
                            YvsBaseArticles a = y.getArticle();
                            for (YvsBaseConditionnement s : a.getConditionnements()) {
                                double stock = dao.stocks(a.getId(), docStock.getCreneauSource().getTranche().getId(), docStock.getSource().getId(), 0, 0, docStock.getDateDoc(), s.getId(), 0);
                                YvsComContenuDocStock c = new YvsComContenuDocStock((long) contenusEntree.size() + 1);
                                c.setArticle(a);
                                c.setConditionnement(s);
                                c.setQteAttente(stock);
                                c.setQteRestant(stock);
                                c.setDateContenu(docStock.getDateDoc());
                                c.setStatut(Constantes.ETAT_EDITABLE);
                                c.setSupp(false);
                                c.setActif(true);
                                c.setPrix(dao.getPua(c.getArticle().getId(), 0));
                                c.setDateSave(new Date());
                                if (currentUser != null ? currentUser.getId() > 0 : false) {
                                    c.setAuthor(currentUser);
                                }
                                contenusEntree.add(c);
                            }
                        }
                    }
                } else {
                    getErrorMessage("Vous devez selectionner le créneau");
                }
            } else {
                getErrorMessage("Vous devez selectionner le dépot");
            }
            update("data_contenu_inventaire");
        }
    }

    public void editer() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
                openDialog("dlgConfirmEditer");
            } else {
                for (YvsComContenuDocStock c : contenusEntreeSave) {
                    double stock = dao.stocks(c.getArticle().getId(), selectDoc.getCreneauDestinataire().getTranche().getId(), selectDoc.getDestination().getId(), 0, 0, selectDoc.getDateDoc(), c.getConditionnement().getId(), c.getLotSortie().getId());
                    if (stock < c.getQuantite()) {
                        getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
                        return;
                    }
                }
                if (changeStatut(Constantes.ETAT_EDITABLE)) {
                    selectDoc.setCloturer(false);
                    selectDoc.setAnnulerBy(null);
                    selectDoc.setValiderBy(null);
                    selectDoc.setDateAnnuler(null);
                    selectDoc.setDateCloturer(null);
                    selectDoc.setDateValider(null);
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        selectDoc.setAuthor(currentUser);
                    }
                    dao.update(selectDoc);
                }
            }
        }
    }

    public void editer_() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            for (YvsComContenuDocStock c : contenusEntreeSave) {
                double stock = dao.stocks(c.getArticle().getId(), selectDoc.getCreneauDestinataire().getTranche().getId(), selectDoc.getDestination().getId(), 0, 0, selectDoc.getDateDoc(), c.getConditionnement().getId(), c.getLotSortie().getId());
                if (stock < c.getQuantite()) {
                    getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
                    return;
                }
            }
            if (changeStatut(Constantes.ETAT_EDITABLE)) {
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(null);
                selectDoc.setValiderBy(null);
                selectDoc.setDateAnnuler(null);
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(null);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    selectDoc.setAuthor(currentUser);
                }
                dao.update(selectDoc);
            }
        }
    }

    public void annuler() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
                openDialog("dlgConfirmAnnuler");
            } else {
                for (YvsComContenuDocStock c : contenusEntreeSave) {
                    double stock = dao.stocks(c.getArticle().getId(), selectDoc.getCreneauDestinataire().getTranche().getId(), selectDoc.getDestination().getId(), 0, 0, selectDoc.getDateDoc(), c.getConditionnement().getId(), c.getLotSortie().getId());
                    if (stock < c.getQuantite()) {
                        getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
                        return;
                    }
                }
                if (changeStatut(Constantes.ETAT_ANNULE)) {
                    selectDoc.setCloturer(false);
                    selectDoc.setAnnulerBy(currentUser.getUsers());
                    selectDoc.setValiderBy(null);
                    selectDoc.setDateAnnuler(new Date());
                    selectDoc.setDateCloturer(null);
                    selectDoc.setDateValider(null);
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        selectDoc.setAuthor(currentUser);
                    }
                    dao.update(selectDoc);
                }
            }
        }
    }

    public void annuler_() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            for (YvsComContenuDocStock c : contenusEntreeSave) {
                double stock = dao.stocks(c.getArticle().getId(), selectDoc.getCreneauDestinataire().getTranche().getId(), selectDoc.getDestination().getId(), 0, 0, selectDoc.getDateDoc(), c.getConditionnement().getId(), c.getLotSortie().getId());
                if (stock < c.getQuantite()) {
                    getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
                    return;
                }
            }
            if (changeStatut(Constantes.ETAT_ANNULE)) {
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(currentUser.getUsers());
                selectDoc.setValiderBy(null);
                selectDoc.setDateAnnuler(new Date());
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(null);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    selectDoc.setAuthor(currentUser);
                }
                dao.update(selectDoc);
            }
        }
    }

    public void valider() {
        if (selectDoc == null) {
            return;
        }
        //Vérifie que tous les stocks sont validé avant la date de l'inventaire
        List<String> etats = new ArrayList<>();
        etats.add(Constantes.ETAT_EDITABLE);
        etats.add(Constantes.ETAT_ATTENTE);
        etats.add(Constantes.ETAT_ENCOURS);
        etats.add(Constantes.ETAT_SOUMIS);
        //trouve les tranches précédent une tranche donnée
        List<Long> ids = dao.loadNameQueries("YvsComCreneauDepot.findByIdsPrecedent", new String[]{"heure", "depot"}, new Object[]{docStock.getCreneauSource().getTranche().getHeureDebut(), new YvsBaseDepots(docStock.getSource().getId())});
        if (ids.isEmpty()) {
            ids.add(0L);
        }
        champ = new String[]{"statuts", "depot", "date", "tranches"};
        val = new Object[]{etats, new YvsBaseDepots(docStock.getSource().getId()), docStock.getDateDoc(), ids};
        Long nb = (Long) dao.loadObjectByNameQueries("YvsComDocStocks.findByDocNonValide", champ, val);
        if (nb != null ? nb > 0 : false) {
            getErrorMessage("Vous ne pouvez pas poursuivre ce traitement. des documents de stocks non validés ont été trouvé dans ce dépôt");
            return;
        }
        for (YvsComContenuDocStock c : contenusEntreeSave) {
            double stock = dao.stocks(c.getArticle().getId(), selectDoc.getCreneauSource().getTranche().getId(), selectDoc.getSource().getId(), 0, 0, selectDoc.getDateDoc(), c.getConditionnement().getId(), c.getLotSortie().getId());
            if (stock < c.getQuantite()) {
                getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action");
                return;
            }
        }
        if (changeStatut(Constantes.ETAT_VALIDE)) {
            selectDoc.setCloturer(false);
            selectDoc.setAnnulerBy(null);
            selectDoc.setValiderBy(currentUser.getUsers());
            selectDoc.setDateAnnuler(null);
            selectDoc.setDateCloturer(null);
            selectDoc.setDateValider(new Date());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                selectDoc.setAuthor(currentUser);
            }
            dao.update(selectDoc);
        }
    }

    public void cloturer(YvsComDocStocks y) {
        selectDoc = y;
        selectDocEntree = null;
        selectDocSortie = null;
        if (y.getDocuments() != null) {
            for (YvsComDocStocks d : y.getDocuments()) {
                if (d.getTypeDoc().equals(Constantes.TYPE_ES)) {
                    selectDocEntree = d;
                } else if (d.getTypeDoc().equals(Constantes.TYPE_SS)) {
                    selectDocSortie = d;
                }
            }
        }
        update("id_confirm_close_inv");
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        if (selectDoc.getCloturer()) {
            getErrorMessage("Ce document est verrouilé");
            return;
        }
        docStock.setCloturer(!docStock.isCloturer());
        selectDoc.setCloturer(docStock.isCloturer());
        selectDoc.setDateCloturer(docStock.isCloturer() ? new Date() : null);
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        documents.set(documents.indexOf(selectDoc), selectDoc);
        update("data_inventaire");
    }

    public boolean changeStatut(String etat) {
        if (etat.equals(Constantes.ETAT_EDITABLE)) {
            if (!autoriser("gescom_inv_editer")) {
                openNotAcces();
                return false;
            }
        } else if (etat.equals(Constantes.ETAT_VALIDE)) {
            if (!autoriser("gescom_inv_valider")) {
                openNotAcces();
                return false;
            }
            if (docStock.getCreneauSource() != null ? docStock.getCreneauSource().getId() < 1 : true) {
                getErrorMessage("Vous devez renseigner la tranche");
                return false;
            }
            if (!controleDocStock(docStock.getId(), docStock.getCreneauSource().getTranche().getHeureDebut(), docStock.getSource().getId(), docStock.getDateDoc())) {
                return false;
            }
        }
        if (changeStatut_(etat)) {
            if (etat.equals(Constantes.ETAT_VALIDE)) {
                double montant = selectDoc.calculMontantTotal(dao, false);
                if (montant > 0) {
                    if (docStock.getValeur() != null ? docStock.getValeur().getId() < 1 : true) {
                        YvsComDocStocksValeur value = new YvsComDocStocksValeur();
                        value.setAuthor(currentUser);
                        value.setCoefficient(1.0);
                        value.setDocStock(selectDoc);
                        value.setMontant(montant);
                        value = (YvsComDocStocksValeur) dao.save1(value);
                        selectDoc.setValeur(value);
                        docStock.setValeur(UtilCom.buildBeanDocStockValeur(value));
                    } else {
                        selectDoc.getValeur().setMontant(montant);
                        selectDoc.getValeur().setDateUpdate(new Date());
                        selectDoc.getValeur().setAuthor(currentUser);
                        dao.update(selectDoc.getValeur());
                        docStock.getValeur().setMontant(montant);
                    }
                } else {
                    if (selectDoc != null ? selectDoc.getValeur() != null ? selectDoc.getValeur().getId() > 0 : false : false) {
                        selectDoc.getValeur().setAuthor(currentUser);
                        dao.delete(selectDoc.getValeur());
                        if (docStock != null) {
                            docStock.setValeur(null);
                        }
                        selectDoc.setValeur(null);
                    }
                }
            }
            succes();
            return true;
        }
        return false;
    }

    private boolean controleValideInventaire() {
        return true;
    }

    public boolean changeStatut_(String etat) {
        if (etat.equals(Constantes.ETAT_VALIDE)) {
            //controle les stocks
            String result = null;
            YvsComContenuDocStock cc = null;
            for (YvsComContenuDocStock c : contenusSortieSave) {
                cc = c;
                result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), docStock.getSource().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", docStock.getDateDoc(), (c.getLotSortie() != null ? c.getLotSortie().getId() : 0));
                if (result != null) {
                    break;
                }
            }
            if (result != null) {
                getErrorMessage("Impossible de valider cette fiche d'inventaire car la ligne d'article " + cc.getArticle().getDesignation() + " engendrera une incohérence dans le stock");
                return false;
            }
        }
        if (!etat.equals("") && selectDoc != null) {
            if (selectDoc.getCloturer()) {
                getErrorMessage("Ce document est verrouilé");
                return false;
            }
            String rq = "UPDATE yvs_com_doc_stocks SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(selectDoc.getId(), 1)};
            dao.requeteLibre(rq, param);
            docStock.setStatut(etat);
            selectDoc.setStatut(etat);
            if (selectDocEntree != null) {
                selectDocEntree.setStatut(etat);
                selectDocEntree.setAuthor(currentUser);
                selectDocEntree.setValiderBy(currentUser.getUsers());
                selectDocEntree.setDateValider(docStock.getDateDoc());
                dao.update(selectDocEntree);
                rq = "UPDATE yvs_com_contenu_doc_stock SET statut = '" + etat + "' WHERE doc_stock=?";
                param = new Options[]{new Options(selectDocEntree.getId(), 1)};
                dao.requeteLibre(rq, param);
            }
            if (selectDocSortie != null) {
                selectDocSortie.setStatut(etat);
                selectDocSortie.setAuthor(currentUser);
                selectDocSortie.setValiderBy(currentUser.getUsers());
                selectDocSortie.setDateValider(docStock.getDateDoc());
                dao.update(selectDocSortie);
                rq = "UPDATE yvs_com_contenu_doc_stock SET statut = '" + etat + "' WHERE doc_stock=?";
                param = new Options[]{new Options(selectDocSortie.getId(), 1)};
                dao.requeteLibre(rq, param);
            }
            int idx = documents.indexOf(selectDoc);
            if (idx >= 0) {
                documents.set(idx, selectDoc);
            } else {
                documents.add(0, selectDoc);
            }
            update("data_inventaire");
            return true;
        }
        return false;
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllInventaire(true, true);
    }

    public void searchByNum() {
        ParametreRequete p;
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.numDoc", "numDoc", "%" + numSearch_ + "%");
            p.setOperation(" LIKE ");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.numDoc", "numDoc", null);
        }
        paginator.addParam(p);
        loadAllInventaire(true, true);
    }

    public void removeDoublon(YvsComDocStocks y) {
        selectDoc = y;
        selectDocEntree = null;
        selectDocSortie = null;
        if (y.getDocuments() != null) {
            for (YvsComDocStocks d : y.getDocuments()) {
                if (d.getTypeDoc().equals(Constantes.TYPE_ES)) {
                    selectDocEntree = d;
                } else if (d.getTypeDoc().equals(Constantes.TYPE_SS)) {
                    selectDocSortie = d;
                }
            }
        }
        removeDoublon();
    }

    public void removeDoublon() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            if (!selectDoc.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre editable pour pouvoir etre modifié");
                return;
            }
            removeDoublonStock(selectDoc.getId());
            succes();
        }
    }

//    public void insertNewLine() {
//        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
//            if (selectContenu.getNbre() > 0) {
//                int idx = contenusEntree.indexOf(selectContenu);
//                YvsComContenuDocStock y;
//                for (int i = 0; i < selectContenu.getNbre(); i++) {
//                    y = new YvsComContenuDocStock(-i, selectContenu);
//                    contenusEntree.add(idx + 1, y);
//                }
//                succes();
//            } else {
//                getErrorMessage("Entrez le nombre de ligne a insérer");
//            }
//        } else {
//            getErrorMessage("Effectez cette option avec une ligne réelle");
//        }
//    }
    @Override
    public void cleanStock() {
        super.cleanStock();
        loadAllInventaire(true, true);
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void print(YvsComDocStocks y) {
        if (y != null ? y.getId() > 0 : false) {
            Map<String, Object> param = new HashMap<>();
            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("ID", y.getId().intValue());
            param.put("VALEUR_BY", valeurBy);
            param.put("SUBREPORT_DIR", SUBREPORT_DIR());
            String report = "fiche_inventaire";
            if (currentParam != null ? currentParam.getUseLotReception() : false) {
                report = "fiche_inventaire_by_lot";
            }
            executeReport(report, param);
        }
    }

    public void printInventairePreparatoire() {
        printInventairePreparatoire(depotPrint, emplamtPrint, 0, categoriePrint, 0, optionPrint, datePrint, printAll, "", false, orderByPrint);
    }

    public void printInventaire(long depot, long emplacement, long famille, String categorie, long groupe, String typeUnite, Date datePrint, boolean printAll, String optionPrint, boolean withChild, String orderBy) {
        if (depot < 1) {
            getErrorMessage("Vous devez selectionner un dépot");
            return;
        }
        if (!autoriser("gescom_print_without_transfert")) {
            YvsBaseDepots y = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{depot});
            if (y.getVerifyAllValidInventaire()) {
                if (!controleDocStock(0, null, depot, datePrint)) {
                    return;
                }
            }
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", 0);
        param.put("DEPOT", (int) depot);
        param.put("EMPLACEMENT", (int) emplacement);
        param.put("FAMILLE", (int) famille);
        param.put("GROUPE", (int) groupe);
        param.put("WITH_CHILD", withChild);
        param.put("CATEGORIE", categorie);
        param.put("TYPE", typeUnite);
        param.put("DATE", datePrint);
        param.put("PRINT_ALL", printAll);
        param.put("SOLDE_PRINT", optionPrint);
        param.put("ORDER_BY", orderBy);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        String report = "inventaire_preparatoire";
        if (autoriser("gescom_inventaire_print_with_pr")) {
            report = currentParam != null ? currentParam.getUseLotReception() ? "inventaire_by_lot" : "inventaire" : "inventaire";
        } else {
            report = currentParam != null ? currentParam.getUseLotReception() ? "inventaire_no_pr_by_lot" : "inventaire_no_pr" : "inventaire_no_pr";
        }
        executeReport(report, param);
    }

    public void printInventairePreparatoire(long depot, long emplacement, long famille, String categorie, long groupe, String typeUnite, Date datePrint, boolean printAll, String optionPrint, boolean withChild, String orderBy) {
        if (depot < 1) {
            getErrorMessage("Vous devez selectionner un dépot");
            return;
        }
        if (!autoriser("gescom_print_without_transfert")) {
            YvsBaseDepots y = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{depot});
            if (y.getVerifyAllValidInventaire()) {
                if (!controleDocStock(0, null, depot, datePrint)) {
                    return;
                }
            }
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", 0);
        param.put("DEPOT", (int) depot);
        param.put("EMPLACEMENT", (int) emplacement);
        param.put("FAMILLE", (int) famille);
        param.put("GROUPE", (int) groupe);
        param.put("WITH_CHILD", withChild);
        param.put("CATEGORIE", categorie);
        param.put("TYPE", typeUnite);
        param.put("DATE", datePrint);
        param.put("PRINT_ALL", printAll);
        param.put("SOLDE_PRINT", optionPrint);
        param.put("ORDER_BY", orderBy);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        String report = "inventaire_preparatoire";
        if (autoriser("gescom_inventaire_print_with_pr")) {
            report = currentParam != null ? currentParam.getUseLotReception() ? "inventaire_preparatoire_by_lot" : "inventaire_preparatoire" : "inventaire_preparatoire";
        } else {
            report = currentParam != null ? currentParam.getUseLotReception() ? "inventaire_preparatoire_no_pr_by_lot" : "inventaire_preparatoire_no_pr" : "inventaire_preparatoire";
        }
        executeReport(report, param);
    }

    public void findEntreeByArticle() {
        contenusEntree.clear();
        if (Util.asString(articleEntree)) {
            for (YvsComContenuDocStock c : contenusEntreeSave) {
                if (c.getArticle().getRefArt().toUpperCase().contains(articleEntree.toUpperCase()) || c.getArticle().getDesignation().toUpperCase().contains(articleEntree.toUpperCase())) {
                    contenusEntree.add(c);
                }
            }
        } else {
            contenusEntree.addAll(contenusEntreeSave);
        }
    }

    public void findSortieByArticle() {
        contenusSortie.clear();
        if (Util.asString(articleSortie)) {
            for (YvsComContenuDocStock c : contenusSortieSave) {
                if (c.getArticle().getRefArt().toUpperCase().contains(articleSortie.toUpperCase()) || c.getArticle().getDesignation().toUpperCase().contains(articleSortie.toUpperCase())) {
                    contenusSortie.add(c);
                }
            }
        } else {
            contenusSortie.addAll(contenusSortieSave);
        }
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllInventaire(true, true);
    }

    public void gotoStockValue() {
        try {
            showMontantDetail = true;
            if (docStock.getValeur().getMontant() == 0) {
                docStock.getValeur().setMontant(selectDoc.getMontantTotal());
            }
            update("form-stock_value");
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveStockValue() {
        try {
            YvsComDocStocksValeur y = UtilCom.buildDocStockValeur(docStock.getValeur(), currentUser);
            y.setDocStock(selectDoc);
            if (y.getId() < 1) {
                y = (YvsComDocStocksValeur) dao.save1(y);
                docStock.getValeur().setId(y.getId());
            } else {
                if (!autoriser("gescom_inv_update_valeur")) {
                    openNotAcces();
                    return;
                }
                if (updateValeur(y)) {
                    y.setMontant(selectDoc.calculMontantTotal(dao, y));
                    docStock.getValeur().setMontant(y.getMontant());
                }
                dao.update(y);
            }
            selectDoc.setValeur(y);
            int index = documents.indexOf(selectDoc);
            if (index > -1) {
                documents.set(index, selectDoc);
            }
            succes();
            update("blog-ecart_value");
            update("tab_users_cible");
            update("data_contenu_inventaire_E");
            update("data_contenu_inventaire_S");
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveQuantiteeJustifiee() {
        try {
            if (quantiteeJustifiee > selectContenu.getQuantite()) {
                getErrorMessage("La quantitée justifiée ne peut exceder la quantité de l'inventaire");
                return;
            }
            selectContenu.setQteAttente(quantiteeJustifiee);
            selectContenu.setAuthor(currentUser);
            selectContenu.setDateUpdate(new Date());
            dao.update(selectContenu);

            if (selectDoc.getValeur() != null ? selectDoc.getValeur().getId() > 0 : false) {
                double montant = selectDoc.calculMontantTotal(dao, selectDoc.getValeur(), true);
                selectDoc.getValeur().setMontant(montant);
                selectDoc.getValeur().setAuthor(currentUser);
                selectDoc.getValeur().setDateUpdate(new Date());
                dao.update(selectDoc.getValeur());
            } else {
                selectDoc.setValeur(new YvsComDocStocksValeur(null));
                double montant = selectDoc.calculMontantTotal(dao, selectDoc.getValeur(), true);
                selectDoc.getValeur().setDocStock(selectDoc);
                selectDoc.getValeur().setMontant(montant);
                selectDoc.getValeur().setAuthor(currentUser);
                selectDoc.getValeur().setDateUpdate(new Date());
                selectDoc.setValeur((YvsComDocStocksValeur) dao.save1(selectDoc.getValeur()));
            }
            docStock.getValeur().setMontant(selectDoc.getValeur().getMontant());
            docStock.getValeur().setId(selectDoc.getValeur().getId());
            int index = documents.indexOf(selectDoc);
            if (index > -1) {
                documents.set(index, selectDoc);
            }
            index = contenusSortie.indexOf(selectContenu);
            if (index > -1) {
                contenusSortie.set(index, selectContenu);
                update("data_contenu_inventaire_S");
            }
            index = contenusEntree.indexOf(selectContenu);
            if (index > -1) {
                contenusEntree.set(index, selectContenu);
                update("data_contenu_inventaire_E");
            }
            succes();
            update("blog-ecart_value");
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public boolean updateValeur(YvsComDocStocksValeur y) {
        try {
            if (selectDoc.getValeur() != null ? selectDoc.getValeur().getId() > 0 : false) {
                if (!y.getCoefficient().equals(selectDoc.getValeur().getCoefficient())) {
                    return true;
                }
                if (!y.getValoriseMpBy().equals(selectDoc.getValeur().getValoriseMpBy())) {
                    return true;
                }
                if (!y.getValoriseMsBy().equals(selectDoc.getValeur().getValoriseMsBy())) {
                    return true;
                }
                if (!y.getValorisePfBy().equals(selectDoc.getValeur().getValorisePfBy())) {
                    return true;
                }
                if (!y.getValorisePfsBy().equals(selectDoc.getValeur().getValorisePfsBy())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
        return false;
    }

    public double calculMontantTotal() {
        double excedent = 0, manquant = 0;
        try {
            if (selectDoc != null) {
                selectDoc.setValeurInventaire(null);
                for (YvsComDocStocks d : selectDoc.getDocuments()) {
                    if (d.getTypeDoc().equals(yvs.dao.salaire.service.Constantes.TYPE_ES)) {
                        excedent = d.calculMontantTotal(dao, selectDoc.getValeur(), false);
                    } else {
                        manquant = d.calculMontantTotal(dao, selectDoc.getValeur(), false);
                    }
                }
                selectDoc.setMontantTotal(manquant);
                double montant = manquant - excedent;
                if (docStock.getValeur().getId() < 1) {
                    docStock.getValeur().setMontant(montant);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
        return manquant;
    }

    private List<YvsComDocStocksEcart> loadAllTiers(YvsComDocStocks y) {
        List<YvsComDocStocksEcart> list = new ArrayList<>();
        String query = "SELECT DISTINCT u.id, u.code_tiers, u.nom, u.prenom, e.id, e.matricule, u.agence, a.codeagence, a.designation FROM yvs_base_tiers u "
                + "LEFT JOIN yvs_com_doc_stocks_ecart d ON (u.id = d.tiers AND d.doc_stock = ?) "
                + "LEFT JOIN yvs_grh_employes e ON u.id = e.compte_tiers LEFT JOIN yvs_agences a ON u.agence = a.id "
                + "WHERE u.actif IS TRUE AND u.employe IS TRUE AND d.id IS NULL AND u.societe = ?";
        List<Options> params = new ArrayList<>();
        params.add(new Options(y.getId(), params.size() + 1));
        params.add(new Options(currentAgence.getSociete().getId(), params.size() + 1));
        if (Util.asString(usersSearch)) {
            query += " AND UPPER(u.nom) LIKE ?";
            params.add(new Options(usersSearch.toUpperCase(), params.size() + 1));
        }
        if (agenceTiers > 0) {
            query += " AND u.agence = ?";
            params.add(new Options(agenceTiers, params.size() + 1));
        }
        query += " ORDER BY u.nom, u.prenom";
        List<Object[]> result = dao.loadListBySqlQuery(query, params.toArray(new Options[params.size()]));
        YvsBaseTiers tiers;
        YvsComDocStocksEcart ecart;
        for (Object[] data : result) {
            tiers = new YvsBaseTiers((Long) data[0], (String) data[1], (String) data[2], (String) data[3]);
            tiers.setCurrentEmploye(new YvsGrhEmployes((Long) data[4], (String) data[5]));
            tiers.setAgence(new YvsAgences((Long) data[6], (String) data[7], (String) data[8]));

            ecart = new YvsComDocStocksEcart(YvsComDocStocksEcart.ids--);
            ecart.setNumero(y.getNumDoc());
            ecart.setAuthor(currentUser);
            ecart.setDocStock(new YvsComDocStocks(y.getId()));
            ecart.setTiers(tiers);
            ecart.setTaux(0D);
            list.add(ecart);
        }
        return list;
    }

    public void onBuildEcart(YvsComDocStocks y, boolean load) {
        try {
            if (!autoriser("gescom_inv_attrib_ecart")) {
                openNotAcces();
                return;
            }
            if (load) {
                onSelectObject(y);
                tabIds = "" + documents.indexOf(y);
            }
            Double taux = (Double) dao.loadObjectByNameQueries("YvsComDocStocksEcart.sumByStock", new String[]{"docStock"}, new Object[]{selectDoc});
            selectDoc.setQteRestant(taux != null ? taux : 0);
            selectDoc.setEcarts(dao.loadNameQueries("YvsComDocStocksEcart.findAll", new String[]{"docStock"}, new Object[]{selectDoc}));
            selectDoc.setFilters(new ArrayList<>(loadAllTiers(y)));
            update("data-ecart_stock");
            openDialog("dlgBuildEcart");
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void drollable(DragDropEvent ev) {
        YvsComDocStocksEcart y = ((YvsComDocStocksEcart) ev.getData());
        selectDoc.getFilters().remove(y);
        if (!selectDoc.getEcarts().contains(y)) {
            selectDoc.getEcarts().add(0, y);
        }
    }

    public void removeDrollable(YvsComDocStocksEcart y) {
        selectEcart = y;
    }

    public void removeDrollable() {
        if (selectEcart.getId() > 0) {
            selectEcart.setAuthor(currentUser);
            dao.delete(selectEcart);
            if (deleteRetenue ? !onDeleteRetenue(selectEcart) : false) {
                return;
            }
        }
        selectEcart.getReglements().clear();
        selectDoc.setQteRestant(selectDoc.getQteRestant() - selectEcart.getTaux());
        selectDoc.getEcarts().remove(selectEcart);
        selectEcart.setId(YvsComDocStocksEcart.ids--);
        selectEcart.setTaux(0.0);
        selectDoc.getFilters().add(0, selectEcart);
        update("txt-taux_a_repartir");
        update("tab_users_source");
        update("tab_users_cible");
    }

    public void onSearchUsersByName() {
        selectDoc.setFilters(new ArrayList<>(loadAllTiers(selectDoc)));
        update("tab_users_source");
    }

    public void onSearchUsersByAgence() {
        selectDoc.setFilters(new ArrayList<>(loadAllTiers(selectDoc)));
        update("tab_users_source");
    }

    public void cellEditEcart(CellEditEvent ev) {
        try {
            if (ev != null ? ev.getRowIndex() > -1 : false) {
                int index = ev.getRowIndex();
                YvsComDocStocksEcart y = selectDoc.getEcarts().get(index);
                if (!docStock.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    y.setTaux((Double) ev.getOldValue());
                    if (index > -1) {
                        selectDoc.getEcarts().set(index, y);
                    }
                    update("data-ecart_stock");
                    getErrorMessage("L'inventaire doit etre validé");
                    return;
                }
                if (docStock.getValeur().getMontant() <= 0) {
                    y.setTaux((Double) ev.getOldValue());
                    if (index > -1) {
                        selectDoc.getEcarts().set(index, y);
                    }
                    update("data-ecart_stock");
                    getErrorMessage("Le valeur de l'inventaire ne peut pas etre negative");
                    return;
                }
                if ((Double) ev.getNewValue() <= 0) {
                    if (y.getId() > 0) {
                        y.setAuthor(currentUser);
                        dao.delete(y);
                        y.getReglements().clear();
                        y.setId(YvsComDocStocksEcart.ids--);
                        Double taux = (Double) ev.getOldValue();
                        selectDoc.setQteRestant(selectDoc.getQteRestant() - (taux != null ? taux : 0));
                    }
                } else {
                    Double taux = (Double) dao.loadObjectByNameQueries("YvsComDocStocksEcart.sumByStockNotId", new String[]{"docStock", "id"}, new Object[]{selectDoc, y.getId()});
                    if (((taux != null ? taux : 0) + (Double) ev.getNewValue()) > 100) {
                        y.setTaux((Double) ev.getOldValue());
                        if (index > -1) {
                            selectDoc.getEcarts().set(index, y);
                        }
                        update("data-ecart_stock");
                        getErrorMessage("Vous ne pouvez pas exceder 100%");
                        return;
                    }
                    y.setTaux((Double) ev.getNewValue());
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    if (y.getId() > 0) {
                        dao.update(y);
                        String delete = "DELETE FROM yvs_com_reglement_ecart_stock WHERE piece = ?";
                        dao.requeteLibre(delete, new Options[]{new Options(y.getId(), 1)});
                    } else {
                        y.setId(null);
                        y.setNumero(docStock.getNumDoc());
                        y = (YvsComDocStocksEcart) dao.save1(y);
                    }
                    onGenererReglement(y, false);
                    selectDoc.setQteRestant((taux != null ? taux : 0) + y.getTaux());
                }
                if (index > -1) {
                    selectDoc.getEcarts().set(index, y);
                }
                succes();
                update("data-ecart_stock");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onEquilibreEcart() {
        try {
            if (selectDoc.getEcarts() != null ? !selectDoc.getEcarts().isEmpty() : false) {
                if (!docStock.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    getErrorMessage("L'inventaire doit etre validé");
                    return;
                }
                if (docStock.getValeur().getMontant() <= 0) {
                    getErrorMessage("Le valeur de l'inventaire ne peut pas etre negative");
                    return;
                }
                double taux = (double) 100 / (double) selectDoc.getEcarts().size();
                for (YvsComDocStocksEcart y : selectDoc.getEcarts()) {
                    y.setTaux(taux);
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    if (y.getId() < 1) {
                        y.setNumero(docStock.getNumDoc());
                        y.setId(null);
                        y = (YvsComDocStocksEcart) dao.save1(y);
                    } else {
                        dao.update(y);
                        y.getReglements().clear();
                        String delete = "DELETE FROM yvs_com_reglement_ecart_stock WHERE piece = ?";
                        dao.requeteLibre(delete, new Options[]{new Options(y.getId(), 1)});
                    }
                    onGenererReglement(y, false);
                }
                succes();
                update("tab_users_cible");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onGenererReglement(YvsComDocStocksEcart y, boolean msg) {
        try {
            double montant = (y.getTaux() * docStock.getValeur().getMontant()) / 100;
            YvsComReglementEcartStock r = new YvsComReglementEcartStock();
            r.setNumero("PC-" + y.getNumero() + "-01");
            r.setAuthor(currentUser);
            r.setDateReglement(new Date());
            r.setMontant(montant);
            r.setPiece(y);
            r.setStatut(Constantes.STATUT_DOC_ATTENTE);
            r = (YvsComReglementEcartStock) dao.save1(r);
            y.getReglements().add(r);
            if (msg) {
                succes();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean onDeleteRetenue(YvsComDocStocksEcart y) {
        try {
            if (y != null ? (y.getId() > 0 ? y.getReglements() != null ? !y.getReglements().isEmpty() : false : false) : false) {
                boolean delete = true;
                for (YvsComReglementEcartStock r : y.getReglements()) {
                    if (r.getRetenue() != null ? r.getRetenue().getRetenue() != null : false) {
                        delete = r.getRetenue().getRetenue().getStatut().equals('E');
                        if (delete) {
                            for (YvsGrhDetailPrelevementEmps d : r.getRetenue().getRetenue().getListPrelevement()) {
                                if (d.getStatutReglement().equals('P')) {
                                    delete = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (delete) {
                    for (YvsComReglementEcartStock r : y.getReglements()) {
                        if (r.getRetenue() != null ? r.getRetenue().getRetenue() != null : false) {
                            r.getRetenue().getRetenue().setAuthor(currentUser);
                            dao.delete(r.getRetenue().getRetenue());
                            r.setRetenue(null);
                        }
                    }
                    succes();
                    update("data-ecart_stock");
                } else {
                    getErrorMessage("Impossible de supprimer. La retenue est payée ou en cours de paiement");
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void onDeleteCredit(YvsComDocStocksEcart y) {
        try {
            if (y != null ? y.getId() > 0 : false) {

            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getPrix(YvsComContenuDocStock y) {
        return y.getPrix(dao, selectDoc != null ? selectDoc.getValeur() : null);
    }

    public void onQuantiteeJustifiee(YvsComContenuDocStock y) {
        selectContenu = y;
        quantiteeJustifiee = y.getQteAttente();
        update("txt_quantitee_justifiee_inventaire");
    }

    public int totalAlertes() {
        int total = 0;
        try {
            alertes.clear();
            String query = "SELECT ARRAY_AGG(y.id) FROM yvs_com_doc_stocks y INNER JOIN yvs_com_doc_stocks_valeur v ON v.doc_stock = y.id INNER JOIN yvs_base_depots d ON y.source = d.id INNER JOIN yvs_agences a ON d.agence = a.id "
                    + "LEFT JOIN yvs_com_doc_stocks_ecart e ON y.id = e.doc_stock WHERE COALESCE(v.montant, 0) > 0 AND e.id IS NULL AND a.societe = ?";
            Long[] ids = (Long[]) dao.loadObjectBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
            if (ids != null ? ids.length > 0 : false) {
                total += ids.length;
                alertes.add(new Object[]{"Manquants non traités", ids.length, ids});
            }
            query = "SELECT ARRAY_AGG(y.id) FROM yvs_com_doc_stocks y INNER JOIN yvs_com_doc_stocks_valeur v ON v.doc_stock = y.id INNER JOIN yvs_base_depots d ON y.source = d.id INNER JOIN yvs_agences a ON d.agence = a.id "
                    + "INNER JOIN yvs_com_doc_stocks_ecart e ON y.id = e.doc_stock WHERE COALESCE(v.montant, 0) > 0 AND a.societe = ? HAVING SUM(e.taux) < 100";
            ids = (Long[]) dao.loadObjectBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
            if (ids != null ? ids.length > 0 : false) {
                total += ids.length;
                alertes.add(new Object[]{"Ecarts d'inventaire non équilibrés", ids.length, ids});
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
        return total;
    }

    public void gotoAlerte(int index) {
        try {
            if (index < alertes.size()) {
                paginator.clear();
                Object[] value = alertes.get(index);
                idsSearch = "0";
                for (Long id : (Long[]) value[2]) {
                    idsSearch += "," + id;
                }
                addParamIds();
                update("data_inventaire");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void findLotReception(ContenuDocStock c) {
        try {
            if (c != null ? c.getLotSortie() != null : false) {
                long id = c.getLotSortie().getId();
                if (c.getLotSortie().getNumero() != null ? c.getLotSortie().getNumero().trim().length() > 0 : false) {
                    YvsComLotReception l = (YvsComLotReception) dao.loadOneByNameQueries("YvsComLotReception.findByNumeroArticle", new String[]{"article", "numero"}, new Object[]{new YvsBaseArticles(c.getArticle().getId()), c.getLotSortie().getNumero()});
                    if (l != null ? l.getId() < 1 : true) {
                        String query = "SELECT y.id FROM yvs_com_lot_reception y INNER JOIN yvs_base_mouvement_stock_lot l ON l.lot = y.id INNER JOIN yvs_base_mouvement_stock m ON l.mouvement = m.id "
                                + " WHERE m.article = ? AND y.numero = ? ORDER BY y.id DESC LIMIT 1";
                        Long lot = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(c.getLotSortie().getNumero(), 2)});
                        l = new YvsComLotReception(lot, c.getLotSortie().getNumero());
                    }
                    if (l != null ? l.getId() > 0 : false) {
                        c.setLotSortie(UtilCom.buildBeanLotReception(l));
                    } else {
                        c.setLotSortie(new LotReception());
                    }
                } else {
                    c.setLotSortie(new LotReception());
                }
                if (c.getLotSortie().getId() != id) {
                    double stock = dao.stocks(c.getArticle().getId(), (docStock.isPassation() ? docStock.getCreneauSource().getTranche().getId() : 0), docStock.getSource().getId(), 0, currentAgence.getSociete().getId(), docStock.getDateDoc(), c.getConditionnement().getId(), c.getLotSortie().getId());
                    c.getArticle().setStock(stock);
                    if (c.getId() < 1) {
                        c.setQuantite(stock);
                    }
                    update("form_contenu_entree_stock");
                    update("desc_artcile_entree_stock");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cellEditStock(CellEditEvent ev) {
        ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        try {
            if (w != null && ev != null ? ev.getRowIndex() > -1 : false) {
                int index = ev.getRowIndex();
                YvsBaseArticleDepot y = articles.get(index);
                y.setStock(ev.getNewValue() != null ? (Double) ev.getNewValue() : 0);
                if (y.getRequiereLot()) {
                    selectArticleDepot = y;
                    openDialog("dlgListLots");
                    update("data-article_stock_lot");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void applyStock() {
        try {
            ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (w != null) {
                boolean succes = false;
                for (YvsBaseArticleDepot y : w.getArticles_stock()) {
                    if (y.getStockInitial() != y.getStock()) {
                        addContenuFormEdit(y, y.getLot(), false);
                        succes = true;
                    }
                }
                if(succes){
                    succes();
                }
            }
        } catch (Exception ex) {
            getException("applyStock", ex);
        }
    }

    public void loadOnViewLot(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            if (selectArticleDepot != null ? selectArticleDepot.getId() > 0 : false) {
                selectArticleDepot.setLot((YvsComLotReception) ev.getObject());
                ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (w != null) {
                    int index = w.getArticles_stock().indexOf(selectArticleDepot);
                    if (index > -1) {
                        w.getArticles_stock().set(index, selectArticleDepot);
                    }
                }
            }
        }
    }

    private void addContenuFormEdit(YvsBaseArticleDepot y, YvsComLotReception lot, boolean msg) {
        try {
            ContenuDocStock contenu = new ContenuDocStock();
            contenu.setArticle(UtilProd.buildSimpleBeanArticles(y.getArticle()));
            contenu.getArticle().setStock(y.getStockInitial());
            contenu.getArticle().setRequiereLot(y.getRequiereLot());
            contenu.setConditionnement(UtilProd.buildSimpleBeanConditionnement(y.getConditionnement()));
            contenu.setQuantite(y.getStock());
            contenu.setPrix(y.getPrixRevient());
            contenu.setLotSortie(UtilCom.buildBeanLotReception(lot));
            saveNewContenu(contenu, msg);
        } catch (Exception ex) {
            getException("addContenuFormEdit", ex);
        }
    }

    public void findArticle() {
        try {
            ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (w != null) {
                if (articleFind != null ? articleFind.trim().length() > 0 : false) {
                    String value = articleFind.replace("%", "").toUpperCase();
                    articles.clear();
                    String reference = "";
                    String designation = "";
                    for (YvsBaseArticleDepot y : w.getArticles_stock()) {
                        reference = y.getArticle().getRefArt().toUpperCase();
                        designation = y.getArticle().getDesignation().toUpperCase();
                        if (articleFind.startsWith("%") && articleFind.endsWith("%")) {
                            if (reference.contains(value) || designation.contains(value)) {
                                articles.add(y);
                            }
                        } else if (articleFind.startsWith("%")) {
                            if (reference.endsWith(value) || designation.endsWith(value)) {
                                articles.add(y);
                            }
                        } else if (articleFind.endsWith("%")) {
                            if (reference.startsWith(value) || designation.startsWith(value)) {
                                articles.add(y);
                            }
                        } else {
                            if (reference.equals(value) || designation.equals(value)) {
                                articles.add(y);
                            }
                        }
                    }
                    update("data_stock_article");
                }
            }
        } catch (Exception ex) {
            getException("findArticle", ex);
        }
    }

    public void calculerStock() {
        try {
            ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (w != null) {
                w.setDateSearch(docStock.getDateDoc());
                if (docStock.getSource() != null ? docStock.getSource().getId() < 1 : true) {
                    getErrorMessage("Vous devez selectionner un dépot");
                    return;
                }
                w.setDepotSearch(docStock.getSource().getId());
                w.setAgence_(docStock.getSource().getAgence().getId());
                w.setStock_(false);
                ManagedDepot wd = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                if (wd != null) {
                    if (!wd.getDepots_all().contains(new YvsBaseDepots(docStock.getSource().getId()))) {
                        w._loadDepot();
                    }
                }
                w.addParamDepot();
                if (docStock.isPassation()) {
                    if (docStock.getCreneauSource() != null ? docStock.getCreneauSource().getId() < 1 : true) {
                        getErrorMessage("Vous devez selectionner un créneau horaire (tranche)");
                        return;
                    }
                    w.setTrancheSearch(docStock.getCreneauSource().getTranche().getId());
                }
                w.setImax(0);
                w.calculerStock(false, false);
                articles = new ArrayList<>(w.getArticles_stock());
                update("data_stock_article");
            }
        } catch (Exception ex) {
            getException("calculerStock", ex);
        }
    }
}
