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
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.CategorieComptable;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.production.UtilProd;
import yvs.commercial.ManagedCommercial;
import static yvs.commercial.ManagedCommercial.currentParam;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.LotReception;
import yvs.commercial.achat.ManagedLotReception;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.depot.ManagedDepot;
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.dao.services.commercial.ServiceTransfert;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComContenuDocStockReception;
import yvs.entity.commercial.stock.YvsComCoutSupDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsers;
import yvs.grh.bean.TypeCout;
import yvs.parametrage.entrepot.Depots;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.PaginatorResult;
import yvs.util.ParamStocks;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedTransfertStock extends ManagedCommercial<DocStock, YvsComDocStocks> implements Serializable {

    private DocStock docStock = new DocStock();
    private YvsComDocStocks selectDoc;
    private List<YvsComDocStocks> documents, transfertsHist;

    private YvsComContenuDocStock selectContenu;
    private YvsComContenuDocStockReception selectReception;
    private ContenuDocStock contenu = new ContenuDocStock(), reception = new ContenuDocStock();
    private String commentaire;
    private List<YvsComContenuDocStock> all_contenus;
    public PaginatorResult<YvsComContenuDocStock> p_contenu = new PaginatorResult<>();

    private List<YvsComContenuDocStock> selectContenus;
    private boolean memoriserDeleteContenu;

    private YvsComCoutSupDocStock selectCout;
    private CoutSupDoc cout = new CoutSupDoc();
    private TypeCout type = new TypeCout();
    private List<YvsGrhTypeCout> types;

    private List<CategorieComptable> categories;

//    private List<YvsBaseDepots> depotsSource;
    private List<YvsBaseDepots> depotsDestination;
    private List<YvsComCreneauDepot> creneauxSource, creneauxDestination;

    private CategorieComptable categorie = new CategorieComptable();

    private String tabIds, tabIds_contenu, tabIds_cout, egaliteStatut = "!=";
    private boolean selectArt, listArt;
    // Nombre d'element a afficher dans le selectOneMenu
    private int subLenght;

    private Date dateReception = new Date();
    List<ParametreRequete> paramsH = new ArrayList<>();
    private long destSearch, sourceSearch;
    private boolean toValideLoad = true, displayQuantieRecu = false;

    //Parametre recherche contenu
    private boolean dateContenu = false, load = true;
    private long sourceContenu, destContenu, agenceContenu;
    private Date dateDebutContenu = new Date(), dateFinContenu = new Date();
    private String statutContenu, reference, article, articleContenu, statutDocSearch, egaliteStatutDoc = "=";
    private Boolean withIncoherence = null;

    public ManagedTransfertStock() {
        categories = new ArrayList<>();
        types = new ArrayList<>();
        transfertsHist = new ArrayList<>();
        documents = new ArrayList<>();
        creneauxSource = new ArrayList<>();
        creneauxDestination = new ArrayList<>();
        depotsDestination = new ArrayList<>();
        all_contenus = new ArrayList<>();
        selectContenus = new ArrayList<>();
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public List<YvsComContenuDocStock> getSelectContenus() {
        return selectContenus;
    }

    public void setSelectContenus(List<YvsComContenuDocStock> selectContenus) {
        this.selectContenus = selectContenus;
    }

    public boolean isMemoriserDeleteContenu() {
        return memoriserDeleteContenu;
    }

    public void setMemoriserDeleteContenu(boolean memoriserDeleteContenu) {
        this.memoriserDeleteContenu = memoriserDeleteContenu;
    }

    public long getAgenceContenu() {
        return agenceContenu;
    }

    public void setAgenceContenu(long agenceContenu) {
        this.agenceContenu = agenceContenu;
    }

    public Boolean getWithIncoherence() {
        return withIncoherence;
    }

    public void setWithIncoherence(Boolean withIncoherence) {
        this.withIncoherence = withIncoherence;
    }

    public boolean isDisplayQuantieRecu() {
        return displayQuantieRecu;
    }

    public void setDisplayQuantieRecu(boolean displayQuantieRecu) {
        this.displayQuantieRecu = displayQuantieRecu;
    }

    public YvsComContenuDocStockReception getSelectReception() {
        return selectReception;
    }

    public void setSelectReception(YvsComContenuDocStockReception selectReception) {
        this.selectReception = selectReception;
    }

    public boolean isFirst() {
        return _first;
    }

    public void setFirst(boolean _first) {
        this._first = _first;
    }

    public ContenuDocStock getReception() {
        return reception;
    }

    public void setReception(ContenuDocStock reception) {
        this.reception = reception;
    }

    public boolean isToValideLoad() {
        return toValideLoad;
    }

    public void setToValideLoad(boolean toValideLoad) {
        this.toValideLoad = toValideLoad;
    }

    public String getEgaliteStatutDoc() {
        return egaliteStatutDoc;
    }

    public void setEgaliteStatutDoc(String egaliteStatutDoc) {
        this.egaliteStatutDoc = egaliteStatutDoc;
    }

    public String getStatutDocSearch() {
        return statutDocSearch;
    }

    public void setStatutDocSearch(String statutDocSearch) {
        this.statutDocSearch = statutDocSearch;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public String getArticleContenu() {
        return articleContenu;
    }

    public void setArticleContenu(String articleContenu) {
        this.articleContenu = articleContenu;
    }

    public PaginatorResult<YvsComContenuDocStock> getP_contenu() {
        return p_contenu;
    }

    public void setP_contenu(PaginatorResult<YvsComContenuDocStock> p_contenu) {
        this.p_contenu = p_contenu;
    }

    public List<YvsComContenuDocStock> getAll_contenus() {
        return all_contenus;
    }

    public void setAll_contenus(List<YvsComContenuDocStock> all_contenus) {
        this.all_contenus = all_contenus;
    }

    public boolean isDateContenu() {
        return dateContenu;
    }

    public void setDateContenu(boolean dateContenu) {
        this.dateContenu = dateContenu;
    }

    public long getSourceContenu() {
        return sourceContenu;
    }

    public void setSourceContenu(long sourceContenu) {
        this.sourceContenu = sourceContenu;
    }

    public long getDestContenu() {
        return destContenu;
    }

    public void setDestContenu(long destContenu) {
        this.destContenu = destContenu;
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

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public int getSubLenght() {
        return subLenght;
    }

    public void setSubLenght(int subLenght) {
        this.subLenght = subLenght;
    }

    public TypeCout getType() {
        return type;
    }

    public void setType(TypeCout type) {
        this.type = type;
    }

    public List<YvsGrhTypeCout> getTypes() {
        return types;
    }

    public void setTypes(List<YvsGrhTypeCout> types) {
        this.types = types;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public YvsComContenuDocStock getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComContenuDocStock selectContenu) {
        this.selectContenu = selectContenu;
    }

    public YvsComCoutSupDocStock getSelectCout() {
        return selectCout;
    }

    public void setSelectCout(YvsComCoutSupDocStock selectCout) {
        this.selectCout = selectCout;
    }

    public List<YvsBaseDepots> getDepotsDestination() {
        return depotsDestination;
    }

    public void setDepotsDestination(List<YvsBaseDepots> depotsDestination) {
        this.depotsDestination = depotsDestination;
    }

    public List<YvsComCreneauDepot> getCreneauxSource() {
        return creneauxSource;
    }

    public void setCreneauxSource(List<YvsComCreneauDepot> creneauxSource) {
        this.creneauxSource = creneauxSource;
    }

    public List<YvsComCreneauDepot> getCreneauxDestination() {
        return creneauxDestination;
    }

    public void setCreneauxDestination(List<YvsComCreneauDepot> creneauxDestination) {
        this.creneauxDestination = creneauxDestination;
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

    public List<YvsComDocStocks> getTransfertsHist() {
        return transfertsHist;
    }

    public void setTransfertsHist(List<YvsComDocStocks> transfertsHist) {
        this.transfertsHist = transfertsHist;
    }

    public List<CategorieComptable> getCategories() {
        return categories;
    }

    public void setCategories(List<CategorieComptable> categories) {
        this.categories = categories;
    }

    public CategorieComptable getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieComptable categorie) {
        this.categorie = categorie;
    }

    public String getTabIds_contenu() {
        return tabIds_contenu;
    }

    public void setTabIds_contenu(String tabIds_contenu) {
        this.tabIds_contenu = tabIds_contenu;
    }

    public String getTabIds_cout() {
        return tabIds_cout;
    }

    public void setTabIds_cout(String tabIds_cout) {
        this.tabIds_cout = tabIds_cout;
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

    public CoutSupDoc getCout() {
        return cout;
    }

    public long getDestSearch() {
        return destSearch;
    }

    public void setDestSearch(long destSearch) {
        this.destSearch = destSearch;
    }

    public long getSourceSearch() {
        return sourceSearch;
    }

    public void setSourceSearch(long sourceSearch) {
        this.sourceSearch = sourceSearch;
    }

    public void setCout(CoutSupDoc cout) {
        this.cout = cout;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        if (currentParamStock == null) {
            currentParamStock = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        if (statut_ != null ? statut_.trim().length() < 1 : true) {
            this.egaliteStatut = "=";
            this.statut_ = Constantes.ETAT_SOUMIS;
            addParamStatut(false);
        }
        if (agence_ < 1) {
            agence_ = currentAgence.getId();
        }
        if (agenceContenu < 1) {
            agenceContenu = currentAgence.getId();
        }
        if (load) {
            loadInfosWarning(false);
            if (isWarning != null ? isWarning : false) {
                loadByWarning();
            } else if (documents.isEmpty()) {
                addParamToValide();
            }
        }
        //depôt servant à la recherche dynamique
        initView();
        loadTypeCout();
        _first = true;
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllTransfert(true);
    }

    boolean _first = false;

    public void load(String load) {
        _first = true;
    }

    private void initView() {
        //indiceNumsearch_ = genererPrefixe(Constantes.TYPE_FT_NAME, currentDepot != null ? currentDepot.getId() : 0);
        if (((docStock != null) ? docStock.getSource().getId() < 1 : true) && ((docStock != null) ? docStock.getDestination().getId() < 1 : true)) {
            docStock = new DocStock();
            docStock.setTypeDoc(Constantes.TYPE_FT);
            if (docStock.getDocumentLie() == null) {
                docStock.setDocumentLie(new DocStock());
            }
            docStock.setMajPr(currentParamStock != null ? currentParamStock.getCalculPr() : true);
            numSearch_ = "";
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                if (m.getDepots().size() == 1) {
                    docStock.setSource(UtilCom.buildBeanDepot(m.getDepots().get(0)));
                }
            }
            chooseDepotSource();
            docStock.setCreneauSource(UtilCom.buildBeanCreneau(currentCreneauDepot));
        }
    }

    public int buildDocByDroit(boolean contenu) {
        return buildDocByDroit((!contenu ? paginator : p_contenu), Constantes.TYPE_FT, contenu, (!contenu ? toValideLoad : false));
    }

    boolean initForm = true;

    public void loadContenus(boolean avance, boolean init) {
        buildDocByDroit(true);
        p_contenu.addParam(new ParametreRequete("y.docStock.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        p_contenu.addParam(new ParametreRequete("y.docStock.typeDoc", "typeDoc", Constantes.TYPE_FT, "=", "AND"));
        String orderBy = "y.docStock.dateDoc DESC, y.docStock.numDoc, y.article.refArt";
        all_contenus = p_contenu.executeDynamicQuery("YvsComContenuDocStock", orderBy, avance, init, dao);
        update("data_contenus_transfert_stock");
    }

    public void loadAllTransfert(boolean avance) {
        buildDocByDroit(false);
        paginator.addParam(new ParametreRequete("y.source.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_FT, "=", "AND"));
//        documents = paginator.executeDynamicQuery("YvsComDocStocks", "y.dateDoc DESC", avance, initForm, (int) imax, dao);
        documents = paginator.executeDynamicQuery("y", "y", "YvsComDocStocks y JOIN FETCH y.creneauSource JOIN FETCH y.creneauDestinataire "
                + "JOIN FETCH y.source JOIN FETCH y.destination JOIN FETCH y.creneauSource.tranche ",
                "y.dateDoc DESC, y.numDoc ASC", avance, initForm, (int) imax, dao);
        subLenght = documents.size() > 10 ? 10 : documents.size();
        if (documents.size() == 1) {
            onSelectObject(documents.get(0));
            execute("collapseForm('transfert_stock')");
        } else {
            execute("collapseList('transfert_stock')");
        }
        update("data_transfert_stock");

    }

    public void loadAllTransfertHist() {
        ParametreRequete p = new ParametreRequete("typeDoc", "typeDoc", Constantes.TYPE_FT);
        p.setOperation("=");
        p.setPredicat("AND");
        if (!paramsH.contains(p)) {
            paramsH.add(p);
        } else {
            paramsH.set(paramsH.indexOf(p), p);
        }
        p = new ParametreRequete("statut", "statut", Constantes.ETAT_EDITABLE);
        p.setOperation("=");
        p.setPredicat("AND");
        if (!paramsH.contains(p)) {
            paramsH.add(p);
        } else {
            paramsH.set(paramsH.indexOf(p), p);
        }
        paramsH.add(new ParametreRequete("source.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        nameQueri = buildDynamicQuery(paramsH, "SELECT y FROM YvsComDocStocks y WHERE", new String[]{"dateDoc"});
        transfertsHist = dao.loadEntity(nameQueri, champ, val, 0, 5);
        update("data_fiche_transfert_hist");
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

    public void loadEditableTransfert(boolean avance, boolean init) {
        int idx = paginator.getParams().indexOf(new ParametreRequete("destination"));
        if (idx > -1) {
            ParametreRequete dep = paginator.getParams().get(idx);
            idx = paginator.getParams().indexOf(new ParametreRequete("depots"));
            if (idx > -1) {
                ParametreRequete src = paginator.getParams().get(idx);
                if (_first) {
                    clearParams();
                }

                statut_ = Constantes.ETAT_EDITABLE;
                cloturer_ = false;
                destSearch = ((YvsBaseDepots) (dep.getObjet() != null ? dep.getObjet() : new YvsBaseDepots())).getId();
                sourceSearch = ((List<Integer>) (src.getObjet() != null ? src.getObjet() : new ArrayList<>(0))).get(0);

                paginator.addParam(new ParametreRequete("y.cloturer", "cloturer", false, "=", "AND"));
                paginator.addParam(new ParametreRequete("y.statut", "statut", Constantes.ETAT_EDITABLE, "=", "AND"));
                initForm = true;
                loadAllTransfert(avance);
            } else {
                documents.clear();
            }
            subLenght = documents.size() > 10 ? 10 : documents.size();
        } else {
            getErrorMessage("Selectionner le depot");
        }
    }

    public void loadSoumisContenus() {
        statutDocSearch = Constantes.ETAT_SOUMIS;
        egaliteStatutDoc = "=";
        addParamStatutDoc();
    }

    private void loadTypeCout() {
        nameQueri = "YvsGrhTypeCout.findAll";
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        types = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void init(boolean next) {
        initForm = false;
        loadAllTransfert(next);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllTransfert(true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        initForm = true;
        loadAllTransfert(true);
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

    @Override
    public void onSelectDistant(YvsComDocStocks y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Transferts Stock", "modGescom", "smenTransfert", true);
            }
        }
    }

//    public void loadAllDepots() {
//        if (currentUser.getUsers().getAccesMultiAgence()) {
//            nameQueri = "YvsBaseDepots.findAll";
//            champ = new String[]{"societe"};
//            val = new Object[]{currentAgence.getSociete()};
//        } else {
//            nameQueri = "YvsBaseDepots.findByAgence";
//            champ = new String[]{"agence"};
//            val = new Object[]{currentAgence};
//        }
//        nameQueriCount = "YvsBaseDepots.findByAgenceCount";
//    }
    private void loadDepotDestination(YvsBaseDepots y) {
        depotsDestination = loadDestination(y);
        update("data_depot_");
        update("select_destinataire_transfert_stock");
    }

    private void loadCreneauxSource(YvsBaseDepots y) {
        creneauxSource = loadCreneaux(y, docStock.getDateDoc());
        update("select_creneau_source_transfert");
    }

    private void loadCreneauxDestinataire(YvsBaseDepots y) {
        creneauxDestination = loadCreneaux(y, docStock.getDateDoc());
        update("select_creneau_destination_transfert");
    }

    public YvsComDocStocks buildDocStock(DocStock y) {
        if ((y.getCreneauDestinataire() != null) ? y.getCreneauDestinataire().getId() > 0 : false) {
            int idx = creneauxDestination.indexOf(new YvsComCreneauDepot(y.getCreneauDestinataire().getId()));
            if (idx > -1) {
                YvsComCreneauDepot c = creneauxDestination.get(idx);
                y.setCreneauDestinataire(UtilCom.buildBeanCreneau(c));
            }
        }
        if ((y.getCreneauSource() != null) ? y.getCreneauSource().getId() > 0 : false) {
            int idx = creneauxSource.indexOf(new YvsComCreneauDepot(y.getCreneauSource().getId()));
            if (idx > -1) {
                YvsComCreneauDepot c = creneauxSource.get(idx);
                y.setCreneauSource(UtilCom.buildBeanCreneau(c));
            }
        }
        if ((y.getDestination() != null) ? y.getDestination().getId() > 0 : false) {
            int idx = depotsDestination.indexOf(new YvsBaseDepots(y.getDestination().getId()));
            if (idx >= 0) {
                YvsBaseDepots d = depotsDestination.get(idx);
                y.setDestination(UtilCom.buildBeanDepot(d));
            }

        }
        if ((y.getSource() != null) ? y.getSource().getId() > 0 : false) {
            ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (service != null) {
                int idx = service.getDepots().indexOf(new YvsBaseDepots(y.getSource().getId()));
                if (idx >= 0) {
                    YvsBaseDepots d = service.getDepots().get(idx);
                    y.setSource(UtilCom.buildBeanDepot(d));
                }
            }
        }
        return UtilCom.buildDocStock(y, currentAgence.getSociete(), currentUser);
    }

    public YvsComContenuDocStock buildContenuDocStock(ContenuDocStock y) {
        YvsComContenuDocStock r = UtilCom.buildContenuDocStock(y, currentUser);
        r.setQuantiteEntree(r.getQuantiteEntree() <= 0 ? r.getQuantite() : r.getQuantiteEntree());
        r.setConditionnementEntree(r.getConditionnementEntree() == null ? r.getConditionnement() : r.getConditionnementEntree());
        return r;
    }

    public YvsComCoutSupDocStock buildCoutSupDocStock(CoutSupDoc y) {
        YvsComCoutSupDocStock c = new YvsComCoutSupDocStock();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            c.setSupp(y.isSupp());
            if (y.getType() != null ? y.getType().getId() > 0 : false) {
                c.setTypeCout(types.get(types.indexOf(new YvsGrhTypeCout(y.getType().getId()))));
            }
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
        docStock.setTypeDoc(Constantes.TYPE_FT);
        docStock.setNature(Constantes.TRANSFERT);
        return docStock;
    }

    public ContenuDocStock recopieViewContenu(DocStock doc) {
        return recopieViewContenu(contenu, doc);
    }

    public ContenuDocStock recopieViewContenu(ContenuDocStock contenu, DocStock doc) {
        contenu.setPrixTotal(contenu.getQuantite() * contenu.getPrix());
        contenu.setDocStock(doc);
        contenu.setNew_(true);
        if (contenu.getId() > 0) {
            contenu.setDateReception(contenu.getDateReception() == null ? doc.getDateReception() : contenu.getDateReception());
        } else {
            contenu.setDateReception(doc.getDateReception());
        }
        if (contenu.getId() < 1 && doc.getDestination() != null && doc.getSource() != null ? doc.getDestination().getAgence().equals(doc.getSource().getAgence()) : false) {
            contenu.setCalculPr(false);
        }
        if (contenu.getLotSortie() != null ? contenu.getLotSortie().getId() > 0 : false) {
            String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
            Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(contenu.getArticle().getId(), 1), new Options(docStock.getDestination().getId(), 2)});
            if (requiere_lot != null ? requiere_lot : false) {
                cloneObject(contenu.getLotEntree(), contenu.getLotSortie());
            }
        }
        return contenu;
    }

    public CoutSupDoc recopieViewCout(DocStock doc) {
        cout.setDoc(doc.getId());
        cout.setNew_(true);
        return cout;
    }

    @Override
    public boolean controleFiche(DocStock bean) {
        ServiceTransfert service = new ServiceTransfert(dao, currentNiveau, currentUser);
        return service._controleFiche(selectDoc, true, true).isResult();
    }

    private boolean _controleFiche_(DocStock bean, boolean c) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (!c) {
            if (bean.getStatut().equals(Constantes.ETAT_VALIDE)) {
                getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
                return false;
            }
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document est deja clôturé");
            return false;
        }
        return true;
    }

    private boolean _controleFiche_(YvsComDocStocks bean) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit être éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.getCloturer()) {
            getErrorMessage("Ce document est déjà cloturer");
            return false;
        }
        return true;
    }

    public boolean controleFicheContenu(ContenuDocStock bean, boolean principal) {
        if (!bean.getDocStock().isUpdate()) {
            selectDoc = _saveNew();
            if (selectDoc != null ? selectDoc.getId() < 1 : true) {
                return false;
            }
            bean.setDocStock(docStock);
        }
        if (bean.getQuantite() == 0) {
            getErrorMessage("Vous devez preciser la quantitée à transferer");
            return false;
        }
        if (bean.getResultante() == 0) {
            getErrorMessage("Vous devez verifier le taux de conversion des differentes unités");
            return false;
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous n'avez entrer aucun article");
            return false;
        }
        if ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le conditionnement");
            return false;
        }
        if (!bean.getDocStock().getStatut().equals(Constantes.ETAT_EDITABLE) && !bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Vous ne pouvez pas modifier cette ligne car le document est " + giveNameStatut(bean.getDocStock().getStatut()).toLowerCase() + " et la ligne est valide");
            return false;
        }
        if (bean.getArticle().isRequiereLot()) {
            if (principal) {
                if ((bean.getLotSortie() != null ? bean.getLotSortie().getId() < 1 : true) && (bean.getLots() != null ? bean.getLots().size() > 1 ? (bean.getLots().size() == 1 ? bean.getLots().get(0).getId() < 1 : false) : true : true)) {
                    getErrorMessage("Un numéro de lot est requis pour cet article dans le dépôt");
                    return false;
                }
            } else {
                if (bean.getLotSortie() != null ? bean.getLotSortie().getId() < 1 : true) {
                    getErrorMessage("Un numéro de lot est requis pour cet article dans le dépôt");
                    return false;
                }
            }
        }
        if (bean.getDocStock().getStatut().equals(Constantes.ETAT_VALIDE) || bean.getDocStock().getStatut().equals(Constantes.ETAT_ENCOURS)) {
            // Contrôle de stocks
            String result = controleStock(bean.getArticle().getId(), bean.getConditionnement().getId(), docStock.getSource().getId(), 0, bean.getQuantite(), (selectContenu != null) ? selectContenu.getQuantite() : 0, bean.getId() > 0 ? "UPDATE" : "INSERT", "S", docStock.getDateDoc(), (bean.getLotSortie() != null ? bean.getLotSortie().getId() : 0));
            if (result != null) {
                getErrorMessage("L'article '" + bean.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou pourrait entrainer un stock négatif au " + result + " dans le dépôt " + docStock.getSource().getDesignation());
                return false;
            }
            result = controleStock(bean.getArticle().getId(), bean.getUniteDestination().getId(), docStock.getDestination().getId(), 0, bean.getResultante(), (selectContenu != null) ? selectContenu.getQuantiteEntree() : 0, bean.getId() > 0 ? "UPDATE" : "INSERT", "E", docStock.getDateDoc(), (bean.getLotEntree() != null ? bean.getLotEntree().getId() : 0));
            if (result != null) {
                getErrorMessage("L'article '" + bean.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou pourrait entrainer un stock négatif au " + result + " dans le dépôt " + docStock.getDestination().getDesignation());
                return false;
            }
        } else if (bean.getDocStock().getStatut().equals(Constantes.ETAT_SOUMIS)) {
            String result = controleStock(bean.getArticle().getId(), bean.getConditionnement().getId(), docStock.getSource().getId(), 0, bean.getQuantite(), (selectContenu != null) ? selectContenu.getQuantite() : 0, bean.getId() > 0 ? "UPDATE" : "INSERT", "S", docStock.getDateDoc(), (bean.getLotSortie() != null ? bean.getLotSortie().getId() : 0));
            if (result != null) {
                getErrorMessage("L'article '" + bean.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou pourrait entrainer un stock négatif au " + result + " dans le dépôt " + docStock.getSource().getDesignation());
                return false;
            }
        }
        return _controleFiche_(bean.getDocStock(), autoriser("tr_add_content_after_valide"));
    }

    private boolean controleDleteLine(YvsComContenuDocStock bean) {
        if (bean == null) {
            getErrorMessage("vous devez supprimer une ligne");
            return false;
        }
        if (docStock.isCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("La ligne doit être editable");
            return false;
        }
        String statut = (String) dao.loadObjectByNameQueries("YvsComDocStocks.findStatutById", new String[]{"id"}, new Object[]{docStock.getId()});
        docStock.setStatut(statut);
        if (!autoriser("tr_add_content_after_valide") && (docStock.getStatut().equals(Constantes.ETAT_VALIDE))) {
            openNotAcces();
            return false;
        }
        String result = null;
        if (bean.getStatut().equals(Constantes.ETAT_VALIDE)) {
            result = controleStock(bean.getArticle().getId(), bean.getConditionnementEntree().getId(), docStock.getDestination().getId(), 0L, bean.getQuantiteEntree(), (bean != null) ? bean.getQuantite() : 0, "DELETE", "E", docStock.getDateDoc(), (bean.getLotEntree() != null ? bean.getLotEntree().getId() : 0));
            if (result != null) {
                getErrorMessage("L'article '" + bean.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                return false;
            }
        }
        if (docStock.getStatut().equals(Constantes.ETAT_SOUMIS)) {
            //confirme le droit de transmettre
            if (!ManagedTransfertStock.this.chechAutorisationActionOnDepot(selectDoc, 1)) {
                return false;
            }
        }
        return true;
    }

    private boolean controleDleteLine(YvsComContenuDocStockReception bean) {
        if (bean == null) {
            getErrorMessage("vous devez supprimer  une ligne");
            return false;
        }
        if (docStock.isCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
        if (!autoriser("tr_add_content_after_valide") && (docStock.getStatut().equals(Constantes.ETAT_VALIDE))) {
            openNotAcces();
            return false;
        }
        String result = null;
        result = controleStock(bean.getContenu().getArticle().getId(), bean.getContenu().getConditionnementEntree().getId(), docStock.getDestination().getId(), 0L, bean.getQuantite(), 0, "DELETE", "E", bean.getDateReception(), (bean.getContenu().getLotEntree() != null ? bean.getContenu().getLotEntree().getId() : 0));
        if (result != null) {
            getErrorMessage("L'article '" + bean.getContenu().getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
            return false;
        }
        return true;
    }

    public boolean controleFicheCout(CoutSupDoc bean) {
        if (bean.getDoc() < 1) {
            selectDoc = _saveNew();
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                return false;
            }
        }
        if (bean.getType() != null ? bean.getType().getId() < 1 : true) {
            getErrorMessage("Vous devez entrer le libelle");
            return false;
        }
        return _controleFiche_(docStock, autoriser("tr_add_content_after_valide"));
    }

    @Override
    public void populateView(DocStock bean) {
        bean.setUpdate(true);
        cloneObject(docStock, bean);
        if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
            ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (service != null) {
                if (service.getDepots().contains(new YvsBaseDepots(docStock.getSource().getId()))) {
                    YvsBaseDepots depS = service.getDepots().get(service.getDepots().indexOf(new YvsBaseDepots(docStock.getSource().getId())));
                    Depots d = UtilCom.buildBeanDepot(depS);
                    cloneObject(docStock.getSource(), d);
                    loadCreneauxSource(depS);
                    loadDepotDestination(depS);
                }
            }
        }
        if ((docStock.getDestination() != null) ? docStock.getDestination().getId() > 0 : false) {
            if (depotsDestination.contains(new YvsBaseDepots(docStock.getDestination().getId()))) {
                YvsBaseDepots d_ = depotsDestination.get(depotsDestination.indexOf(new YvsBaseDepots(docStock.getDestination().getId())));
                Depots d = UtilCom.buildBeanDepot(d_);
                cloneObject(docStock.getDestination(), d);
                loadCreneauxDestinataire(d_);
            } else {
                ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                if (service != null) {
                    int idx = service.getDepots().indexOf(selectDoc.getDestination());
                    if (idx >= 0) {
                        depotsDestination.add(0, service.getDepots().get(idx));
                        Depots d = UtilCom.buildBeanDepot(service.getDepots().get(idx));
                        cloneObject(docStock.getDestination(), d);
                        loadCreneauxDestinataire(service.getDepots().get(idx));
                        update("select_destinataire_transfert_stock");
                    }
                }
            }
        }
        setMontantTotalDoc(docStock, docStock.getContenusSave());
    }

    public void populateViewContenu(ContenuDocStock bean) {
        bean.setUpdate(true);
        bean.getArticle().setStock_(dao.stocks(bean.getArticle().getId(), 0, docStock.getDestination().getId(), 0, 0, docStock.getDateDoc(), bean.getConditionnement().getId(), bean.getLotEntree().getId()));
        bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docStock.getSource().getId(), 0, 0, docStock.getDateDoc(), bean.getConditionnement().getId(), bean.getLotSortie().getId()));
        bean.getArticle().setPuv(dao.getPuv(bean.getArticle().getId(), bean.getQuantite(), 0, 0, docStock.getSource().getId(), 0, docStock.getDateDoc(), bean.getConditionnement().getId()));
        bean.setPrix((bean.getPrix() <= 0) ? dao.getPua(bean.getArticle().getId(), 0) : bean.getPrix());
        bean.getArticle().setPua(bean.getPrix());
        selectArt = true;

        String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
        Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(bean.getArticle().getId(), 1), new Options(docStock.getSource().getId(), 2)});
        bean.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
        cloneObject(contenu, bean);
        if (contenu.getArticle().isRequiereLot() && (docStock.getSource() != null ? docStock.getSource().getId() > 0 : false)) {
            ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
            if (w != null) {
                contenu.setLots(w.loadList(docStock.getSource().getId(), contenu.getConditionnement().getId(), docStock.getDateDoc(), contenu.getQuantite(), bean.getArticle().getStock()));
                if (contenu.getLotSortie() != null ? (contenu.getLotSortie().getId() < 1 ? contenu.getLots().size() == 1 : false) : true) {
                    YvsComLotReception l = contenu.getLots().get(0);
                    if (l.getStock() > contenu.getQuantite()) {
                        contenu.setLotSortie(UtilCom.buildBeanLotReception(l));
                    }
                }
                update("data-contenu_ft_require_lot");
            }
        }
    }

    public void populateViewCout(CoutSupDoc bean) {
        bean.setUpdate(true);
        cloneObject(cout, bean);
    }

    @Override
    public void resetFiche() {
        transfertsHist.clear();
        resetFiche_();
        update("blog_form_transfert_stock");
    }

    public void resetFiche_() {
        docStock = new DocStock();
        docStock.setStatut(Constantes.ETAT_EDITABLE);
        docStock.setCloturer(false);
        docStock.setDocumentLie(new DocStock());
        docStock.setTypeDoc(Constantes.TYPE_FT);
        tabIds = "";
        ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (m != null) {
            if (selectDoc != null) {
                if (selectDoc.getSource() != null) {
                    if (!selectDoc.getSource().getActif() && m.getDepots().contains(selectDoc.getSource())) {
                        m.getDepots().remove(selectDoc.getSource());
                        update("select_source_transfert_stock");
                    }
                }
            }
            if (m.getDepots().size() == 1) {
                docStock.setSource(UtilCom.buildBeanDepot(m.getDepots().get(0)));
            }
        }
        selectDoc = new YvsComDocStocks();
        docStock.getContenus().clear();
        depotsDestination.clear();
        creneauxDestination.clear();
        creneauxSource.clear();
        docStock.getCouts().clear();
        chooseDepotSource();
        docStock.setCreneauSource(UtilCom.buildBeanCreneau(currentCreneauDepot));
        resetFicheContenu();
        resetFicheCout();
        update("blog_form_transfert_stock");
    }

    public void resetFicheContenu() {
        contenu = new ContenuDocStock();
        contenu.setParent(new ContenuDocStock());
        tabIds_contenu = "";
        selectArt = false;
        selectContenu = null;
        resetFicheReception();
        update("form_contenu_transfert_stock");
    }

    public void resetFicheReception() {
        reception = new ContenuDocStock();
        reception.setParent(new ContenuDocStock());
        selectReception = null;
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            reception.setDateReception(docStock.getDateReception());
            double quantite = selectContenu.getQuantiteEntree() - selectContenu.getQuantiteRecu();
            reception.setQuantite(quantite > 0 ? quantite : 0);
        }
        update("form-reception_contenu_doc_stock");
    }

    public void resetFicheCout() {
        resetFiche(cout);
        cout.setType(new TypeCout());
        tabIds_cout = "";
        selectCout = null;
    }

    @Override
    public boolean saveNew() {
        YvsComDocStocks docSaved = _saveNew();
        if (docSaved != null ? docSaved.getId() > 0 : false) {
            this.selectDoc = docSaved;
            succes();
            actionOpenOrResetAfter(this);
            return true;
        }
        return false;
    }

    public YvsComDocStocks _saveNew() {
        return _saveNew(recopieView(), true, true);
    }

    public YvsComDocStocks _saveNew(DocStock bean, boolean control_tranche_source, boolean control_tranche_destination) {
        try {
            ServiceTransfert serviceTransfert = new ServiceTransfert(dao, currentNiveau, currentUser, currentUser.getAgence().getSociete());
            selectDoc = buildDocStock(bean);
            ResultatAction resultService = serviceTransfert._controleFiche(selectDoc, true, true);
            if (!resultService.isResult()) {
                getErrorMessage(resultService.getMessage());
                return null;
            }
            if (!bean.isUpdate()) {
                selectDoc.setId(null);
                selectDoc = (YvsComDocStocks) dao.save1(selectDoc);
                bean.setId(selectDoc.getId());
                docStock.setId(selectDoc.getId());
                documents.add(0, selectDoc);
                transfertsHist.add(0, selectDoc);
            } else {
                dao.update(selectDoc);
                documents.set(documents.indexOf(selectDoc), selectDoc);
                if (transfertsHist.contains(selectDoc)) {
                    transfertsHist.set(transfertsHist.indexOf(selectDoc), selectDoc);
                }
            }
            docStock.setNumDoc(bean.getNumDoc());
            docStock.setUpdate(true);
            update("data_transfert_stock");
            update("data_fiche_transfert_hist");
            update("form_entete_transfert_stock");
            update("form_transfert_stock");
            return selectDoc;
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
            return null;
        }
    }

    public void saveContenu() {
        try {
            contenu.setDocStock(docStock);
            if (contenu.getLots().size() > 1) {
                ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                if (w != null) {
                    contenu.setLots(w.buildQuantiteLot(contenu.getLots(), contenu.getQuantite()));
                }
                openDialog("dlgListLotReception");
                update("data-contenu_ft_require_lot");
            } else {
                saveNewContenu();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveAllContenuLot() {
        try {
            if (contenu.getLots() != null ? !contenu.getLots().isEmpty() : false) {
                boolean correct = false;
                ContenuDocStock clone;
                List<YvsComLotReception> lots = new ArrayList<>(contenu.getLots());
                for (YvsComLotReception lot : lots) {
                    if (lot.getQuantitee() > 0) {
                        clone = new ContenuDocStock();
                        cloneObject(clone, contenu);
                        clone.setQuantite(lot.getQuantitee());
                        clone.getLots().clear();
                        listenChangeQuantite(clone);
                        clone.setLotSortie(UtilCom.buildBeanLotReception(lot));
                        clone.setLotEntree(UtilCom.buildBeanLotReception(lot));
                        correct = saveNewContenu(clone, false);
                    }
                }
                if (correct) {
                    succes();
                    resetFicheContenu();
                    update("data_transfert_stock");
                }
                update("data_contenu_transfert_stock");
            } else {
                saveNewContenu();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveNewContenu() {
        try {
            if (saveNewContenu(contenu, true)) {
                succes();
                resetFicheContenu();
                update("data_transfert_stock");
                update("data_contenu_transfert_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public YvsComContenuDocStock saveEntityContenu() {
        return _saveNewContenu(contenu, true);
    }

    private boolean saveNewContenu(ContenuDocStock contenu, boolean principal) {
        YvsComContenuDocStock y = _saveNewContenu(contenu, principal);
        return y != null ? y.getId() > 0 : false;
    }

    public YvsComContenuDocStock _saveNewContenu(ContenuDocStock contenu, boolean principal) {
        YvsComContenuDocStock en = null;
        try {
            //cette vérificaction empêche de modifier une fiche qui apprait editable pour la fiche chargé à l'écran et pourtant déjà validé en base de données
            String statut = (String) dao.loadObjectByNameQueries("YvsComDocStocks.findStatutById", new String[]{"id"}, new Object[]{docStock.getId()});
            docStock.setStatut(statut);
            if (!docStock.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Vous ne pouvez pas ajouter des lignes dans un transfert en cours de validation. Veuillez recharger et le rendre éditable");
                return null;
            }
            ContenuDocStock bean = recopieViewContenu(contenu, docStock);
            if (controleFicheContenu(bean, principal)) {
                en = buildContenuDocStock(bean);
                if (!bean.isUpdate()) {
                    en.setId(null);
                    en = (YvsComContenuDocStock) dao.save1(en);
                    contenu.setId(en.getId());
                    docStock.setMontantTotalCS(docStock.getMontantTotal() + bean.getPrixTotal());
                    docStock.getContenus().add(0, en);
                } else {
                    dao.update(en);
                    double m = docStock.getContenus().get(docStock.getContenus().indexOf(en)).getPrixTotal();
                    docStock.setMontantTotalCS(docStock.getMontantTotal() + bean.getPrixTotal() - m);
                    docStock.getContenus().set(docStock.getContenus().indexOf(en), en);
                }
                int idx = docStock.getContenusSave().indexOf(en);
                if (idx < 0) {
                    docStock.getContenusSave().add(en);
                } else {
                    docStock.getContenusSave().set(idx, en);
                }
                idx = documents.indexOf(en.getDocStock());
                en.getDocStock().setNew_(true);
                if (idx < 0) {
                    documents.add(0, en.getDocStock());
                }
                if (idx >= 0) {
                    if (documents.get(idx).getContenus() == null) {
                        documents.get(idx).setContenus(new ArrayList<YvsComContenuDocStock>());
                    }
                    int idx_ = documents.get(idx).getContenus().indexOf(en);
                    if (idx_ >= 0) {
                        documents.get(idx).getContenus().set(idx_, en);
                    } else {
                        documents.get(idx).getContenus().add(0, en);

                    }
                }
                setMontantTotalDoc(docStock, docStock.getContenusSave());
                update("chp_transfert_stock_net_a_payer");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Lymytz Error>>>", ex);
        }
        return en;
    }

    public void applyNewPrix() {
        //vérifier le droit
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setAuthor(currentUser);
            selectContenu.setDateUpdate(new Date());
            dao.update(selectContenu);
            docStock.getContenus().set(docStock.getContenus().indexOf(selectContenu), selectContenu);
            update("data_contenu_transfert_stock");
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void saveNewCout() {
        try {
            CoutSupDoc bean = recopieViewCout(docStock);
            if (controleFicheCout(bean)) {
                YvsComCoutSupDocStock en = buildCoutSupDocStock(bean);
                if (!bean.isUpdate()) {
                    en.setId(null);
                    en = (YvsComCoutSupDocStock) dao.save1(en);
                    docStock.getCouts().add(0, en);
                    docStock.setMontantTotalCS(docStock.getMontantTotalCS() + bean.getMontant());
                } else {
                    dao.update(en);
                    double mtant = docStock.getCouts().get(docStock.getCouts().indexOf(en)).getMontant();
                    docStock.setMontantTotalCS(docStock.getMontantTotalCS() + bean.getMontant() - mtant);
                    docStock.getCouts().set(docStock.getCouts().indexOf(en), en);
                }
                succes();
                resetFicheCout();
                update("data_transfert_stock");
                update("data_cout_transfert_stock");
                update("blog_form_montant_doc");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Lymytz Error>>>", ex);
        }
    }

    public void addCommentaireContenu(YvsComContenuDocStock y) {
        selectContenu = y;
        commentaire = y.getCommentaire();
        update("txt_commentaire_contenu_transfert_stock");
    }

    public void addCommentaireContenu() {
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setCommentaire(commentaire);
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            docStock.getContenus().set(docStock.getContenus().indexOf(selectContenu), selectContenu);
            update("data_contenu_transfert_stock");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addReceptionContenu(YvsComContenuDocStock y) {
        selectContenu = y;
        resetFicheReception();
        String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
        Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(y.getArticle().getId(), 1), new Options(docStock.getDestination().getId(), 2)});
        reception.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
        update("blog-reception_contenu_doc_stock");
    }

    public void addReceptionContenu() {
        if (!(docStock.getStatut().equals(Constantes.ETAT_SOUMIS) || docStock.getStatut().equals(Constantes.ETAT_ENCOURS) || docStock.getStatut().equals(Constantes.ETAT_VALIDE))) {
            getErrorMessage("Le document doit etre soumis ou validé");
            return;
        }
        if (reception.getQuantite() < selectContenu.getQuantite()) {
            if (!autoriser("stock_update_qte_recu")) {
                openNotAcces();
                return;
            }
        }
        if (reception.getArticle().isRequiereLot() ? reception.getLotEntree() != null ? reception.getLotEntree().getId() < 1 : true : false) {
            getErrorMessage("Un numéro de lot est requis pour l'article " + selectContenu.getArticle().getDesignation() + " dans le dépôt " + docStock.getDestination().getDesignation());
            return;
        }
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            double quantiteRecu = selectContenu.getQuantiteRecu() + reception.getQuantite() - (selectReception != null ? selectReception.getQuantite() : 0);
            if (quantiteRecu > selectContenu.getQuantiteEntree()) {
                getErrorMessage("Vous ne pouvez pas receptionner plus de ce qu'on vous a transmis");
                return;
            }
            if (reception.getDateReception().before(docStock.getDateDoc())) {
                getErrorMessage("La date de validation ne peut pas être anterieure à la date d'émission");
                return;
            }
            if (reception.getDateReception().after(new Date())) {
                getErrorMessage("Vous ne pouvez valider ce document dans le future");
                return;
            }
            reception.setParent(new ContenuDocStock(selectContenu.getId()));
            selectReception = UtilCom.buildReceptionDocStock(reception, currentUser);
            if (reception.getLotEntree() != null ? reception.getLotEntree().getId() > 0 ? (selectContenu.getLotEntree() != null ? selectContenu.getLotEntree().getId() < 1 : true) : false : false) {
                selectContenu.setLotEntree(UtilCom.buildLotReception(reception.getLotEntree(), currentAgence, currentUser));
                dao.update(selectContenu);
            }
            selectReception.setContenu(selectContenu);
            if (reception.getId() < 1) {
                selectReception = (YvsComContenuDocStockReception) dao.save1(selectReception);
                reception.setId(selectReception.getId());
            } else {
                dao.update(selectReception);
            }
            int idx = selectContenu.getReceptions().indexOf(selectReception);
            if (idx > -1) {
                selectContenu.getReceptions().set(idx, selectReception);
            } else {
                selectContenu.getReceptions().add(selectReception);
            }
            idx = docStock.getContenusSave().indexOf(selectContenu);
            if (idx > -1) {
                docStock.getContenusSave().set(idx, selectContenu);
            }
            idx = docStock.getContenus().indexOf(selectContenu);
            if (idx > -1) {
                docStock.getContenus().set(idx, selectContenu);
            }
            actualiseStatutContenu(selectContenu);
            update("data_contenu_transfert_stock");
            resetFicheReception();
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public boolean saveNewCategorie() {

        return true;
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
            getException("ManagedTransfertStock (changeCalculPr)", ex);
        }
    }

    public void openTodeleteBeanContenu_(YvsComContenuDocStock y) {
        selectContenu = y;
    }

    public void updatePrix_(YvsComContenuDocStock y) {
        selectContenu = y;
        openDialog("dlgUpdatePrix");
        update("txt_prix_contenu_stock_tr");
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("tr_delete_doc")) {
                openNotAcces();
                return;
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Integer> ids = decomposeSelection(tabIds);
                List<YvsComDocStocks> temps = new ArrayList<>();
                YvsComDocStocks bean;
                for (Integer index : ids) {
                    bean = documents.get(index);
                    if (!_controleFiche_(bean)) {
                        continue;
                    }
                    bean.setAuthor(currentUser);
                    dao.delete(bean);
                    temps.add(bean);
                }
                documents.removeAll(temps);
                transfertsHist.removeAll(temps);
                temps = null;
                succes();
                resetFiche();
                update("data_transfert_stock");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : ", ex);
        }
    }

    public void deleteBean_(YvsComDocStocks y) {
        selectDoc = y;
    }

    public void deleteBean_() {
        try {
            if (!autoriser("tr_delete_doc")) {
                openNotAcces();
                return;
            }
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                selectDoc.setAuthor(currentUser);
                dao.delete(selectDoc);
                documents.remove(selectDoc);
                if (transfertsHist.contains(selectDoc)) {
                    transfertsHist.remove(selectDoc);
                    update("data_fiche_transfert_hist");
                }
                succes();
                resetFiche();
                update("data_transfert_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenu() {
        try {
            if (!_controleFiche_(selectDoc)) {
                return;
            }
            if ((tabIds_contenu != null) ? !tabIds_contenu.equals("") : false) {
                String[] tab = tabIds_contenu.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComContenuDocStock bean = docStock.getContenus().get(docStock.getContenus().indexOf(new YvsComContenuDocStock(id)));
                    if (bean.getId() > 0 ? !controleDleteLine(bean) : false) {
                        continue;
                    }
                    bean.setAuthor(currentUser);
                    dao.delete(bean);
                    docStock.getContenus().remove(bean);
                    docStock.getContenusSave().remove(bean);
                    docStock.setMontantTotal(docStock.getMontantTotal() - bean.getPrixTotal());
                    if (id == contenu.getId()) {
                        resetFicheContenu();
                    }
                }
                actualiseStatutDoc(selectDoc);
                setMontantTotalDoc(docStock, docStock.getContenusSave());
                update("chp_transfert_stock_net_a_payer");
                update("data_contenu_transfert_stock");
                update("data_transfert_stock");
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanArticles(boolean execute) {
        if (execute || memoriserDeleteContenu) {
            if (selectContenus != null ? !selectContenus.isEmpty() : false) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                for (YvsComContenuDocStock c : selectContenus) {
                    if (c.getId() > 0 ? !controleDleteLine(c) : false) {
                        continue;
                    }
                    dao.delete(c);
                    docStock.getContenus().remove(c);
                    docStock.getContenusSave().remove(c);
                    if (contenu.getId() == c.getId()) {
                        resetFicheContenu();
                    }
                }
                setMontantTotalDoc(docStock, docStock.getContenusSave());
                update("chp_transfert_stock_net_a_payer");
                update("data_contenu_transfert_stock");
                update("data_transfert_stock");
                succes();
            }
        } else {
            openDialog("dlgConfirmDeleteArticles");
        }
    }

    public void checkCoherence() {
        if (selectDoc == null || docStock == null) {
            return;
        }
        if (selectDoc.getId() < 1 || docStock.getContenus().isEmpty()) {
            return;
        }
        String query = "SELECT DISTINCT c.id FROM yvs_com_contenu_doc_stock c INNER JOIN yvs_com_contenu_doc_stock_reception r ON r.contenu = c.id "
                + "WHERE c.doc_stock = ? GROUP BY c.id HAVING c.quantite_entree < SUM(r.quantite)";
        List<Options> params = new ArrayList<>();
        params.add(new Options(selectDoc.getId(), params.size() + 1));
        List<Long> ids = dao.loadListBySqlQuery(query, params.toArray(new Options[params.size()]));
        if (ids != null ? ids.isEmpty() : true) {
            ids = new ArrayList<Long>() {
                {
                    add(-1L);
                }
            };
        }
        for (YvsComContenuDocStock contenu : docStock.getContenus()) {
            contenu.setIncoherence(ids.contains(contenu.getId()));
        }
    }

    public void deleteBeanContenu_() {
        try {
            if (!controleDleteLine(selectContenu)) {
                return;
            }
            selectContenu.setAuthor(currentUser);
            dao.delete(selectContenu);
            docStock.getContenus().remove(selectContenu);
            docStock.getContenusSave().remove(selectContenu);
            selectDoc.getContenus().remove(selectContenu);
            actualiseStatutDoc(selectContenu.getDocStock());
            if (selectContenu.getId().equals(contenu.getId())) {
                resetFicheContenu();
            }
            setMontantTotalDoc(docStock, docStock.getContenusSave());
            update("chp_transfert_stock_net_a_payer");
            update("data_contenu_transfert_stock");
            update("data_transfert_stock");
            succes();
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanReception() {
        try {
            if (!controleDleteLine(selectReception)) {
                return;
            }
            selectReception.setAuthor(currentUser);
            dao.delete(selectReception);
            selectContenu.getReceptions().remove(selectReception);
            int idx = docStock.getContenusSave().indexOf(selectContenu);
            if (idx > -1) {
                docStock.getContenus().set(idx, selectContenu);
            }
            if (selectReception.getId().equals(reception.getId())) {
                resetFicheReception();
            }
            actualiseStatutContenu(selectContenu);
            succes();
            update("data-reception_contenu_doc_stock");
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanCout() {
        try {
            if (!_controleFiche_(selectDoc)) {
                return;
            }
            if ((tabIds_cout != null) ? !tabIds_cout.equals("") : false) {
                String[] tab = tabIds_cout.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComCoutSupDocStock bean = docStock.getCouts().get(docStock.getCouts().indexOf(new YvsComCoutSupDocStock(id)));
                    bean.setAuthor(currentUser);
                    dao.delete(bean);
                    docStock.getCouts().remove(bean);
                    docStock.setMontantTotalCS(docStock.getMontantTotalCS() - bean.getMontant());
                }
                succes();
                resetFicheCout();
                update("data_cout_transfert_stock");
                update("data_transfert_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanCout_(YvsComCoutSupDocStock y) {
        selectCout = y;
    }

    public void deleteBeanCout_() {
        try {
            if (!_controleFiche_(selectDoc)) {
                return;
            }
            if (selectCout != null) {
                selectCout.setAuthor(currentUser);
                dao.delete(selectCout);
                docStock.getCouts().remove(selectCout);
                succes();
                resetFicheCout();
                update("data_cout_transfert_stock");
                update("data_transfert_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsComDocStocks y) {
        onSelectObject(y, true);
    }

    public void onSelectObject(YvsComDocStocks y, boolean historique) {
        y.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{y}));
        selectDoc = y;
        DocStock bean = UtilCom.buildBeanDocStock(selectDoc);
        ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (m != null) {
            if (!m.getDepots().contains(selectDoc.getSource())) {
                m.getDepots().add(0, selectDoc.getSource());
                update("select_source_transfert_stock");
            }
            if (!m.getDepots().contains(selectDoc.getDestination())) {
                m.getDepots().add(0, selectDoc.getDestination());
                update("select_destinataire_transfert_stock");
            }
        }
        populateView(bean);
        resetFicheContenu();
        resetFicheCout();
        addParamDepotSource();
        addParamDepotDest();
        addParamCreneauSource();
        addParamCreneauDest();
        if (historique) {
            loadAllTransfertHist();
        }
        update("data_fiche_transfert_hist");
        update("blog_form_transfert_stock");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocStocks y = (YvsComDocStocks) ev.getObject();
            onSelectObject(y);
            tabIds = documents.indexOf(y) + "";
        }
        update("save_doc_achat");
    }

    public void loadOnViewContent(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocStock y = (YvsComContenuDocStock) ev.getObject();
            onSelectObject(y.getDocStock());
        }
    }

    public void loadOnViewHist(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocStocks y = (YvsComDocStocks) ev.getObject();
            onSelectObject(y, false);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_transfert_stock");
    }

    public void unLoadOnViewHist(UnselectEvent ev) {
        resetFiche_();
        update("blog_form_transfert_stock");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleDepot bean = (YvsBaseArticleDepot) ev.getObject();
            Articles a = UtilProd.buildBeanArticles(bean.getArticle());
            a.setRequiereLot(bean.getRequiereLot());
            a.setSellWithOutStock(bean.getSellWithoutStock());
            chooseArticle(a);
            update("form_contenu_transfert_stock");
            update("desc_article_transfert_stock");
        }
    }

    public void loadOnViewConditionnement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseConditionnement bean = (YvsBaseConditionnement) ev.getObject();
            chooseConditionnement(UtilProd.buildBeanConditionnement(bean));
            listArt = false;
            update("form_contenu_transfert_stock");
            update("desc_article_transfert_stock");
        }
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectContenu = (YvsComContenuDocStock) ev.getObject();
            if (selectContenu.getArticle().getConditionnements().isEmpty()) {
                selectContenu.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{selectContenu.getArticle()}));
            }
            populateViewContenu(UtilCom.buildBeanContenuDocStock(selectContenu));
            update("form_contenu_transfert_stock");
            update("desc_article_transfert_stock");
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheContenu();
        update("form_contenu_transfert_stock");
        update("desc_article_transfert_stock");
    }

    public void loadOnViewReception(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectReception = (YvsComContenuDocStockReception) ev.getObject();
            reception = UtilCom.buildBeanReceptionDocStock(selectReception);
            String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
            Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(selectContenu.getArticle().getId(), 1), new Options(docStock.getDestination().getId(), 2)});
            reception.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
            update("form-reception_contenu_doc_stock");
        }
    }

    public void unLoadOnViewReception(UnselectEvent ev) {
        resetFicheReception();
    }

    public void loadOnViewCout(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCoutSupDocStock bean = (YvsComCoutSupDocStock) ev.getObject();
            populateViewCout(UtilCom.buildBeanCoutSupDocStock(bean));
            update("form_cout_transfert_stock");
        }
    }

    public void unLoadOnViewCout(UnselectEvent ev) {
        resetFicheCout();
        update("form_cout_transfert_stock");
    }

    public void loadOnViewDepot(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseDepots y = (YvsBaseDepots) ev.getObject();
            Depots d = UtilCom.buildBeanDepot(y);
            cloneObject(docStock.getSource(), d);
            ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (a != null) {
                a.setEntityDepot(y);
                a.loadActifArticleByDepot(true, true);
            }
            loadDepotDestination(y);
            loadCreneauxSource(y);
            update("depot_destinataire");
        }
    }

    public void onRowToggle(ToggleEvent event) {
        YvsComContenuDocStock y = (YvsComContenuDocStock) event.getData();
        onSelectObject(y.getDocStock());
    }

    public void chooseCategorie() {
        if ((docStock.getCategorieComptable() != null) ? docStock.getCategorieComptable().getId() > 0 : false) {
            CategorieComptable d = categories.get(categories.indexOf(docStock.getCategorieComptable()));
            cloneObject(docStock.getCategorieComptable(), d);
        }
    }

    public void chooseDepotSource() {
        ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (a != null) {
            a.getArticlesResult().clear();
        }
        if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
            ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (service != null) {
                int idx = service.getDepots().indexOf(new YvsBaseDepots(docStock.getSource().getId()));
                if (idx >= 0) {
                    YvsBaseDepots y = service.getDepots().get(idx);
                    Depots d = UtilCom.buildBeanDepot(y);
                    cloneObject(docStock.getSource(), d);
                    if (a != null) {
                        a.setEntityDepot(y);
                        a.loadActifArticleByDepot(true, true);
                    }
                    loadDepotDestination(y);
                    loadCreneauxSource(y);
                    verifyOperation(d, Constantes.TRANSFERT, Constantes.TRANSFERT, false);
                } else {
                    getErrorMessage("Dépôt source introuvable !");
                }
            }
        } else {
            docStock.getCreneauSource().setId(0);
            docStock.setSource(new Depots());
            creneauxSource.clear();
            depotsDestination.clear();
        }
        addParamDepotSource();
        loadAllTransfertHist();
    }

    public void chooseDepotDestinataire() {
        if ((docStock.getDestination() != null) ? docStock.getDestination().getId() > 0 : false) {
            YvsBaseDepots d_ = depotsDestination.get(depotsDestination.indexOf(new YvsBaseDepots(docStock.getDestination().getId())));
            Depots d = UtilCom.buildBeanDepot(d_);
            cloneObject(docStock.getDestination(), d);
            loadCreneauxDestinataire(d_);
            verifyOperation(d, Constantes.TRANSFERT, Constantes.TRANSFERT, false);
        } else {
            docStock.getCreneauDestinataire().setId(0);
            docStock.setDestination(new Depots());
            creneauxDestination.clear();
        }
        addParamDepotDest();
        loadAllTransfertHist();
    }

    public void chooseCreneauSource() {
        if ((docStock.getCreneauSource() != null) ? docStock.getCreneauSource().getId() > 0 : false) {
            YvsComCreneauDepot c_ = creneauxSource.get(creneauxSource.indexOf(new YvsComCreneauDepot(docStock.getCreneauSource().getId())));
            Creneau c = UtilCom.buildBeanCreneau(c_);
            cloneObject(docStock.getCreneauSource(), c);
        } else {
            docStock.setCreneauSource(new Creneau());
        }
        addParamCreneauSource();
        loadAllTransfertHist();
    }

    public void chooseCreneauDestinataire() {
        if ((docStock.getCreneauDestinataire() != null) ? docStock.getCreneauDestinataire().getId() > 0 : false) {
            YvsComCreneauDepot y = creneauxDestination.get(creneauxDestination.indexOf(new YvsComCreneauDepot(docStock.getCreneauDestinataire().getId())));
            cloneObject(docStock.getCreneauDestinataire(), UtilCom.buildBeanCreneau(y));
            definedUsersReception();
        } else {
            docStock.setCreneauDestinataire(new Creneau());
        }
        addParamCreneauDest();
        loadAllTransfertHist();
    }

    public void chooseDateDoc() {
        docStock.setDateReception(docStock.getDateDoc());
        if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
            ManagedDepot service = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (service != null) {
                int idx = service.getDepots().indexOf(new YvsBaseDepots(docStock.getSource().getId()));
                if (idx >= 0) {
                    loadCreneauxSource(service.getDepots().get(idx));
                }
            }
        }
        if ((docStock.getDestination() != null) ? docStock.getDestination().getId() > 0 : false) {
            YvsBaseDepots d_ = depotsDestination.get(depotsDestination.indexOf(new YvsBaseDepots(docStock.getDestination().getId())));
            loadCreneauxDestinataire(d_);
            definedUsersReception();
        }
    }

    public void chooseDateReception() {
        definedUsersReception();
    }

    private void definedUsersReception() {
        if ((docStock.getCreneauDestinataire() != null) ? docStock.getCreneauDestinataire().getId() > 0 : false) {
            YvsComCreneauDepot y = new YvsComCreneauDepot(docStock.getCreneauDestinataire().getId());
            YvsUsers u = (YvsUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findUsersByCreneauDepotDate", new String[]{"creneau", "date"}, new Object[]{y, docStock.getDateReception()});
            if (u != null ? u.getId() < 1 : true) {
                u = (YvsUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findUsersByCreneauDepotPerm", new String[]{"creneau"}, new Object[]{y});
            }
            docStock.getCreneauDestinataire().setUsers(UtilUsers.buildSimpleBeanUsers(u));
        }
    }

    public void chooseStatut(ValueChangeEvent ev) {
        statut_ = ((String) ev.getNewValue());
        addParamStatut();
    }

    public void addParamStatut() {
        addParamStatut(true);
    }

    public void addParamStatut(boolean load) {
        ParametreRequete p;
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statut_, egaliteStatut, "AND");
        } else {
            p = new ParametreRequete("y.statut", "statut", null);
        }
        paginator.addParam(p);
        if (load) {
            initForm = true;
            loadAllTransfert(true);
        }
    }

    public void addParamStatutDoc() {
        ParametreRequete p;
        if (statutDocSearch != null ? statutDocSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.docStock.statut", "statut", statutDocSearch, egaliteStatutDoc, "AND");
        } else {
            p = new ParametreRequete("y.docStock.statut", "statut", null);
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("y.cloturer", "cloturer", cloturer_);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        initForm = true;
        loadAllTransfert(true);
    }

    public void addParamIncoherence() {
        try {
            ParametreRequete p = new ParametreRequete("y.id", "incoherence", null, "IN", "AND");
            if (withIncoherence != null) {
                String query = "SELECT DISTINCT * from (SELECT DISTINCT y.id FROM yvs_com_doc_stocks y INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = y.id "
                        + "INNER JOIN yvs_base_depots d ON y.source = d.id INNER JOIN yvs_agences a ON d.agence = a.id "
                        + "WHERE y.type_doc = 'FT' AND y.statut = 'V' AND c.statut != 'V' AND $1 "
                        + "UNION "
                        + "SELECT DISTINCT c.doc_stock as id FROM yvs_com_doc_stocks y INNER JOIN yvs_com_contenu_doc_stock c ON c.doc_stock = y.id "
                        + "INNER JOIN yvs_com_contenu_doc_stock_reception r ON r.contenu = c.id "
                        + "INNER JOIN yvs_base_depots d ON y.source = d.id INNER JOIN yvs_agences a ON d.agence = a.id "
                        + "WHERE y.type_doc = 'FT' AND y.statut = 'V' AND $1 "
                        + "GROUP BY c.doc_stock, c.id HAVING c.quantite_entree < SUM(r.quantite)) AS id ORDER BY id";
                List<Options> params = new ArrayList<>();
                List<String> conditions = new ArrayList<>();
                conditions.add("a.societe = CAST(? AS BIGINT)");
                params.add(new Options(currentAgence.getSociete().getId(), 1));
                if (!currentUser.getUsers().getAccesMultiAgence()) {
                    conditions.add("d.agence = CAST(? AS BIGINT)");
                    params.add(new Options(currentAgence.getId(), 2));
                }
                if (date_) {
                    conditions.add("y.date_doc BETWEEN :dateDebut AND :dateFin ");
                    params.add(new Options(dateDebut_, 3));
                    params.add(new Options(dateFin_, 4));
                }
                List<Options> _params_ = new ArrayList<>(params);
                for (Options _p_ : _params_) {
                    _p_.setPosition(params.get(params.size() - 1).getPosition() + 1);
                    params.add(_p_);
                }
                query = query.replaceAll("\\$1", yvs.util.Util.join(" AND ", conditions));
                List<Long> ids = dao.loadListBySqlQuery(query, params.toArray(new Options[params.size()]));
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                p = new ParametreRequete("y.id", "incoherence", ids, withIncoherence ? "IN" : "NOT IN", "AND");
            }
            paginator.addParam(p);
            initForm = true;
            loadAllTransfert(true);
        } catch (Exception ex) {
            getException("(addParamIncoherence) : " + ex.getMessage(), ex);
        }
    }

    public void chooseDateSearch() {
        ParametreRequete p;
        if (date_) {
            p = new ParametreRequete("y.dateDoc", "dateDoc", dateDebut_);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin_);
        } else {
            p = new ParametreRequete("y.dateDoc", "dateDoc", null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllTransfert(true);
    }

    public void chooseDestSearch(boolean load) {
        ParametreRequete p;
        if (destSearch > 0) {
            p = new ParametreRequete("y.destination", "destination", new YvsBaseDepots(destSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.destination", "destination", null);
        }
        paginator.addParam(p);
        initForm = true;
        if (load) {
            loadAllTransfert(true);
        }
    }

    public void chooseDestSearch() {
        chooseDestSearch(true);
    }

    public void addParamAgence() {
        ParametreRequete p;
        if (agence_ > 0) {
            p = new ParametreRequete("y.source.agence", "agence", new YvsAgences(agence_), "=", "AND");
        } else {
            p = new ParametreRequete("y.source.agence", "agence", null);
        }
        paginator.addParam(p);
        loadAllTransfert(true);
        _loadDepot();
    }

    public void chooseSourceSearch() {
        ParametreRequete p;
        if (sourceSearch > 0) {
            p = new ParametreRequete("y.source", "source", new YvsBaseDepots(sourceSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.source", "source", null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllTransfert(true);
    }

    public void chooseConditionnement(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            contenu.getConditionnement().setId((long) ev.getNewValue());
        }
        chooseConditionnement();
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
        loadInfosArticle(contenu.getArticle());
        listenChangeQuantite();
    }

    public void chooseConditionnementDest(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            contenu.getUniteDestination().setId((long) ev.getNewValue());
        }
        chooseConditionnementDest();
    }

    public void chooseConditionnementDest() {
        if (contenu.getUniteDestination() != null ? contenu.getUniteDestination().getId() > 0 : false) {
            if (contenu.getArticle() != null) {
                int idx = contenu.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(contenu.getUniteDestination().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = contenu.getArticle().getConditionnements().get(idx);
                    contenu.setUniteDestination(UtilProd.buildBeanConditionnement(y));
                    if (contenu.getConditionnement().getUnite().equals(contenu.getUniteDestination().getUnite())) {
                        contenu.setPrixEntree(contenu.getPrix());
                    } else {
                        double prix = convertirUnite(contenu.getUniteDestination().getUnite(), contenu.getConditionnement().getUnite(), contenu.getPrix());
//                        prix = dao.getPr(contenu.getArticle().getId(), docStock.getSource().getId(), 0, docStock.getDateDoc(), contenu.getUniteDestination().getId());
                        contenu.setPrixEntree(prix);
                    }
                }
            }
        }
        listenChangeQuantite();
    }

    public void listenChangeQuantite() {
        listenChangeQuantite(contenu);
    }

    public void listenChangeQuantite(ContenuDocStock contenu) {
        if (contenu.getConditionnement().getId() > 0 && contenu.getUniteDestination().getId() > 0 && contenu.getQuantite() > 0) {
            if (!contenu.getConditionnement().getUnite().equals(contenu.getUniteDestination().getUnite())) {
                contenu.setResultante(convertirUnite(contenu.getConditionnement().getUnite(), contenu.getUniteDestination().getUnite(), contenu.getQuantite()));
            } else {
                contenu.setResultante(contenu.getQuantite());
            }
            contenu.setPrixTotal(contenu.getQuantite() * contenu.getPrix());
            update("txt_total_article_transfert_stock");
            update("txt_qte_dest");
        }
    }

    public void loadInfosArticle(Articles art) {
        art.setStock_(dao.stocks(art.getId(), 0, docStock.getDestination().getId(), 0, 0, docStock.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLotEntree().getId()));
        art.setStock(dao.stocks(art.getId(), 0, docStock.getSource().getId(), 0, 0, docStock.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLotSortie().getId()));
        if (art.isRequiereLot() && (docStock.getSource() != null ? docStock.getSource().getId() > 0 : false)) {
            ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
            if (w != null) {
                contenu.setLots(w.loadList(docStock.getSource().getId(), contenu.getConditionnement().getId(), docStock.getDateDoc(), contenu.getQuantite(), art.getStock()));
                if (contenu.getLots().size() == 1) {
                    contenu.setLotSortie(UtilCom.buildBeanLotReception(contenu.getLots().get(0)));
                    String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                    Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(art.getId(), 1), new Options(docStock.getDestination().getId(), 2)});
                    if (requiere_lot != null ? requiere_lot : false) {
                        cloneObject(contenu.getLotEntree(), contenu.getLotSortie());
                    }
                } else {
                    contenu.setLotSortie(new LotReception(0, contenu.getLots().size() + " Lots"));
                }
                update("data-contenu_ft_require_lot");
            }
        }

        art.setPuv(dao.getPuv(art.getId(), contenu.getQuantite(), 0, 0, docStock.getSource().getId(), 0, docStock.getDateDoc(), contenu.getConditionnement().getId()));
        double prix = dao.getPr(currentAgence.getId(), art.getId(), docStock.getSource().getId(), 0, docStock.getDateDoc(), contenu.getConditionnement().getId());
        art.setPua(dao.getPua(art.getId(), 0));
        cloneObject(contenu.getArticle(), art);
        contenu.setPrix(prix);
        if (contenu.getConditionnement().getUnite().equals(contenu.getUniteDestination().getUnite())) {
            contenu.setPrixEntree(contenu.getPrix());
        } else {
            prix = convertirUnite(contenu.getUniteDestination().getUnite(), contenu.getConditionnement().getUnite(), contenu.getPrix());
//            prix = dao.getPr(art.getId(), docStock.getSource().getId(), 0, docStock.getDateDoc(), contenu.getUniteDestination().getId());
            contenu.setPrixEntree(prix);
        }
        contenu.setPrixTotal(contenu.getQuantite() * prix);
        selectArt = true;
    }

    public void chooseLot(boolean entree) {
        if (!entree) {
            if ((contenu.getLotSortie() != null) ? contenu.getLotSortie().getId() > 0 : false) {
                ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                if (m != null) {
                    int idx = m.getLots().indexOf(new YvsComLotReception(contenu.getLotSortie().getId()));
                    if (idx > -1) {
                        contenu.setLotSortie(UtilCom.buildBeanLotReception(m.getLots().get(idx)));
                    }
                }
            }
        } else {
            if ((contenu.getLotEntree() != null) ? contenu.getLotEntree().getId() > 0 : false) {
                ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                if (m != null) {
                    int idx = m.getLots().indexOf(new YvsComLotReception(contenu.getLotEntree().getId()));
                    if (idx > -1) {
                        contenu.setLotEntree(UtilCom.buildBeanLotReception(m.getLots().get(idx)));
                    }
                }
            }
        }
    }

    public void chooseArticle(Articles art) {
        if ((art != null) ? art.getId() > 0 : false) {
            YvsBaseConditionnement csrc, cdest;
            csrc = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseArticleDepot.findDefaultCond", new String[]{"article", "depot"}, new Object[]{new YvsBaseArticles(art.getId()), new YvsBaseDepots(docStock.getSource().getId())});
            cdest = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseArticleDepot.findDefaultCond", new String[]{"article", "depot"}, new Object[]{new YvsBaseArticles(art.getId()), new YvsBaseDepots(docStock.getDestination().getId())});
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(art.getId())});
            art.setConditionnements(unites);
            if (csrc != null) {
                contenu.setConditionnement(UtilProd.buildBeanConditionnement(csrc));
            } else if (art.getConditionnements() != null ? (!art.getConditionnements().isEmpty()) : false) {
                contenu.setConditionnement(UtilProd.buildBeanConditionnement(art.getConditionnements().get(0)));
            }
            if (cdest != null) {
                contenu.setUniteDestination(UtilProd.buildBeanConditionnement(cdest));
            } else if (art.getConditionnements() != null ? (!art.getConditionnements().isEmpty() && cdest == null) : false) {
                contenu.setUniteDestination(UtilProd.buildBeanConditionnement(art.getConditionnements().get(0)));
            }
            if ((contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) && (contenu.getUniteDestination() != null ? contenu.getUniteDestination().getId() > 0 : false)) {
                listenChangeQuantite();
            }
            loadInfosArticle(art);
        } else {
            resetFicheContenu();
        }
    }

    private void chooseConditionnement(Conditionnement c) {
        if (c != null ? c.getId() > 0 : false) {
            chooseArticle(c.getArticle());
            cloneObject(contenu.getConditionnement(), c);
            chooseConditionnement();
        }
    }

    public void searchArticle() {
        String num = contenu.getArticle().getRefArt();
        contenu.getArticle().setDesignation("");
        contenu.getArticle().setError(true);
        contenu.getArticle().setId(0);
        listArt = false;
        selectArt = false;
        ManagedStockArticle service = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (service != null) {
            if (service.getEntityDepot() == null) {
                service.setEntityDepot(new YvsBaseDepots(docStock.getSource().getId()));
            }
            Articles y = service.searchArticleActifByDepot(num, true);
            listArt = y.isListArt();
            selectArt = y.isSelectArt();
            if (service.getArticlesResult() != null ? !service.getArticlesResult().isEmpty() : false) {
                if (service.getArticlesResult().size() > 1) {
                    update("data_articles_transfert_stock");
                } else {
                    chooseArticle(y);
                }
                contenu.getArticle().setError(false);
            } else {
                Conditionnement c = service.searchArticleActifByCodeBarre(num);
                if (service.getConditionnements() != null ? !service.getConditionnements().isEmpty() : false) {
                    if (service.getConditionnements().size() > 1) {
                        update("data_articles_transfert_stock");
                    } else {
                        chooseConditionnement(c);
                    }
                    listArt = true;
                    selectArt = true;
                }
            }
        }
    }

    public void initArticles() {
        listArt = false;
        ManagedStockArticle service = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (service != null) {
            service.setEntityDepot(new YvsBaseDepots(docStock.getSource().getId()));
            service.initArticlesByDepot(contenu.getArticle());
            listArt = contenu.getArticle().isListArt();
        }
        update("data_articles_transfert_stock");
    }

    public boolean chechAutorisationActionOnDepot(YvsComDocStocks selectDoc, int action) {
        return chechAutorisationAction(selectDoc, action, true);
    }

    public boolean chechAutorisationAction(YvsComDocStocks selectDoc, int action, boolean msg) {
        if (autoriser("tr_valid_all")) {
            return true;
        }
        ServiceTransfert service = new ServiceTransfert(dao, currentNiveau, currentUser, currentAgence.getSociete());
        if (!service.chechAutorisationActionOnDepot(selectDoc, action, currentUser.getUsers())) {
            if (msg) {
                getErrorMessage("Vous ne pouvez pas modifier ce transfert...car vous n'êtes pas habilité à le faire");
            }
            return false;
        }
        return true;
    }

    public void findContenusByArticle() {
        docStock.getContenus().clear();
        if (Util.asString(articleContenu)) {
            for (YvsComContenuDocStock c : docStock.getContenusSave()) {
                if (c.getArticle().getRefArt().toUpperCase().contains(articleContenu.toUpperCase()) || c.getArticle().getDesignation().toUpperCase().contains(articleContenu.toUpperCase())) {
                    docStock.getContenus().add(c);
                }
            }
        } else {
            docStock.getContenus().addAll(docStock.getContenusSave());
        }
    }

    public void editer() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE) || selectDoc.getStatut().equals(Constantes.ETAT_ENCOURS) || selectDoc.getStatut().equals(Constantes.ETAT_SOUMIS)) {
                openDialog("dlgConfirmEditer");
            } else {
                if (changeStatut(Constantes.ETAT_EDITABLE)) {
                    selectDoc.setCloturer(false);
                    selectDoc.setAnnulerBy(null);
                    selectDoc.setValiderBy(null);
                    selectDoc.setCloturerBy(null);
                    selectDoc.setDateAnnuler(null);
                    selectDoc.setDateCloturer(null);
                    selectDoc.setDateValider(null);
                    selectDoc.setStatut(Constantes.ETAT_EDITABLE);
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        selectDoc.setAuthor(currentUser);
                    }
                    dao.update(selectDoc);
                }
            }
        }
    }

    public void editerOrderByForce() {
        editerOrdre(true);
    }

    public void editerOrdre() {
        editerOrdre(false);
    }

    public void editerOrdre(boolean force) {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            boolean acces = chechAutorisationActionOnDepot(selectDoc, selectDoc.getStatut().equals(Constantes.ETAT_SOUMIS) ? 1 : 2);
            if (!acces) {
                return;
            }
            boolean succes = false;
            Date dateDoc = selectDoc.getDateDoc();
            boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
            if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE) || selectDoc.getStatut().equals(Constantes.ETAT_ENCOURS)) {
                String result = null;
                List<ParamStocks> ls;
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    ls = reorganiseList(c);
                    for (ParamStocks p : ls) {
                        result = controleStock(c.getArticle().getId(), c.getConditionnementEntree().getId(), docStock.getDestination().getId(), 0L, p.getQuantite(), 0, "INSERT", "S", p.getDate(), (c.getLotEntree() != null ? c.getLotEntree().getId() : 0));
                        if (result != null) {
                            getErrorMessage("Impossible de d'invalider cette fiche de transfert car la ligne d'article " + c.getArticle().getDesignation() + " engendrera une incohérence dans le stock au " + result);
                            return;
                        }
                    }
                }
                // Vérifié qu'aucun document d'inventaire n'exite après cette date
                dateDoc = selectDoc.getDateReception() != null ? selectDoc.getDateReception() : selectDoc.getDateDoc();
                if (!force) {
                    exist_inventaire = !controleInventaire(selectDoc.getDestination().getId(), dateDoc, selectDoc.getCreneauDestinataire().getTranche().getId(), !gescom_update_stock_after_valide);
                    if (exist_inventaire) {
                        if (!gescom_update_stock_after_valide) {
                            return;
                        } else if (!force) {
                            openDialog("dlgConfirmChangeInventaireByEdit");
                            return;
                        }
                    }
                }
                succes = changeStatut(Constantes.ETAT_SOUMIS);
            } else if (selectDoc.getStatut().equals(Constantes.ETAT_SOUMIS)) {
                if (!force) {
                    exist_inventaire = !controleInventaire(selectDoc.getSource().getId(), dateDoc, selectDoc.getCreneauSource().getTranche().getId(), !gescom_update_stock_after_valide);
                    if (exist_inventaire) {
                        if (!gescom_update_stock_after_valide) {
                            return;
                        } else if (!force) {
                            openDialog("dlgConfirmChangeInventaireByEdit");
                            return;
                        }
                    }
                }
                succes = changeStatut(Constantes.ETAT_EDITABLE);
            }
            if (succes) {
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(null);
                selectDoc.setValiderBy(null);
                selectDoc.setCloturerBy(null);
                selectDoc.setDateAnnuler(null);
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(null);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    selectDoc.setAuthor(currentUser);
                }
                dao.update(selectDoc);
                if (exist_inventaire) {
                    if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE) || selectDoc.getStatut().equals(Constantes.ETAT_ENCOURS)) {
                        YvsComDocStocks inventaire = dao.lastInventaire(selectDoc.getDestination().getId(), dateDoc, selectDoc.getCreneauDestinataire().getId());
                        if (inventaire != null ? inventaire.getId() > 0 : false) {
                            for (YvsComContenuDocStock c : docStock.getContenus()) {
                                for (YvsComContenuDocStockReception r : c.getReceptions()) {
                                    majInventaire(inventaire, c.getArticle(), c.getConditionnementEntree(), r.getQuantite(), Constantes.MOUV_SORTIE);
                                }
                            }
                        }
                    } else if (selectDoc.getStatut().equals(Constantes.ETAT_SOUMIS)) {
                        champ = new String[]{"type", "depot", "tranche", "date", "statut"};
                        YvsComDocStocks inventaire = dao.lastInventaire(selectDoc.getSource().getId(), dateDoc, selectDoc.getCreneauSource().getId());
                        if (inventaire != null ? inventaire.getId() > 0 : false) {
                            for (YvsComContenuDocStock c : docStock.getContenus()) {
                                majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), Constantes.MOUV_ENTREE);
                            }
                        }
                    }
                }
                exist_inventaire = false;
            }
        }
    }

    public void annuler() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE) || selectDoc.getStatut().equals(Constantes.ETAT_ENCOURS) || selectDoc.getStatut().equals(Constantes.ETAT_SOUMIS)) {
                openDialog("dlgConfirmAnnuler");
            } else {
                if (changeStatut(Constantes.ETAT_ANNULE)) {
                    selectDoc.setCloturer(false);
                    selectDoc.setAnnulerBy(currentUser.getUsers());
                    selectDoc.setValiderBy(null);
                    selectDoc.setCloturerBy(null);
                    selectDoc.setDateAnnuler(new Date());
                    selectDoc.setDateCloturer(null);
                    selectDoc.setDateValider(null);
                    selectDoc.setStatut(Constantes.ETAT_ANNULE);
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        selectDoc.setAuthor(currentUser);
                    }
                    dao.update(selectDoc);
                }
            }
        }
    }

    public void annulerOrderByForce() {
        annulerOrdre(true);
    }

    public void annulerOrdre() {
        annulerOrdre(false);
    }

    public void annulerOrdre(boolean force) {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (!autoriser("tr_valid_all")) {
                openNotAcces();
                return;
            }
            Date dateDoc = selectDoc.getDateDoc();
            boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
            if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE) || selectDoc.getStatut().equals(Constantes.ETAT_ENCOURS)) {
                // Vérifié qu'aucun document d'inventaire n'exite après cette date
                dateDoc = selectDoc.getDateReception() != null ? selectDoc.getDateReception() : selectDoc.getDateDoc();
                if (!force) {
                    exist_inventaire = !controleInventaire(selectDoc.getDestination().getId(), dateDoc, selectDoc.getCreneauDestinataire().getTranche().getId(), !gescom_update_stock_after_valide);
                    if (exist_inventaire) {
                        if (!gescom_update_stock_after_valide) {
                            return;
                        } else if (!force) {
                            openDialog("dlgConfirmChangeInventaireByCancel");
                            return;
                        }
                    }
                }
            }
            if (selectDoc.getStatut().equals(Constantes.ETAT_SOUMIS)) {
                if (!force) {
                    exist_inventaire = !controleInventaire(selectDoc.getSource().getId(), dateDoc, selectDoc.getCreneauSource().getTranche().getId(), !gescom_update_stock_after_valide);
                    if (exist_inventaire) {
                        if (!gescom_update_stock_after_valide) {
                            return;
                        } else if (!force) {
                            openDialog("dlgConfirmChangeInventaireByEdit");
                            return;
                        }
                    }
                }
            }
            String result;
            for (YvsComContenuDocStock c : docStock.getContenus()) {
                result = controleStock(c.getArticle().getId(), c.getConditionnementEntree().getId(), docStock.getDestination().getId(), 0L, c.getQuantiteEntree(), 0, "INSERT", "S", dateDoc, (c.getLotEntree() != null ? c.getLotEntree().getId() : 0));
                if (result != null) {
                    getErrorMessage("Impossible de valider cette fiche de transfert car la ligne d'article " + c.getArticle().getDesignation() + " engendrera une incohérence dans le stock au " + result);
                    return;
                }
            }
            if (!autoriser("tr_valid_all")) {
                if (!chechAutorisationActionOnDepot(selectDoc, 1)) {
                    return;
                }
            }
            if (changeStatut(Constantes.ETAT_ANNULE)) {
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(currentUser.getUsers());
                selectDoc.setValiderBy(null);
                selectDoc.setCloturerBy(null);
                selectDoc.setDateAnnuler(new Date());
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(null);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    selectDoc.setAuthor(currentUser);
                }
                dao.update(selectDoc);
                if (exist_inventaire) {
                    if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE) || selectDoc.getStatut().equals(Constantes.ETAT_ENCOURS)) {
                        YvsComDocStocks inventaire = dao.lastInventaire(selectDoc.getDestination().getId(), dateDoc, selectDoc.getCreneauDestinataire().getId());
                        if (inventaire != null ? inventaire.getId() > 0 : false) {
                            for (YvsComContenuDocStock c : docStock.getContenus()) {
                                for (YvsComContenuDocStockReception r : c.getReceptions()) {
                                    majInventaire(inventaire, c.getArticle(), c.getConditionnementEntree(), r.getQuantite(), Constantes.MOUV_SORTIE);
                                }
                            }
                        }
                    } else if (selectDoc.getStatut().equals(Constantes.ETAT_SOUMIS)) {
                        YvsComDocStocks inventaire = dao.lastInventaire(selectDoc.getSource().getId(), dateDoc, selectDoc.getCreneauSource().getId());
                        if (inventaire != null ? inventaire.getId() > 0 : false) {
                            for (YvsComContenuDocStock c : docStock.getContenus()) {
                                majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), Constantes.MOUV_ENTREE);
                            }
                        }
                    }
                }
                exist_inventaire = false;
            }
        }
    }

    public void backOrderByForce() {
        back(true);
    }

    public void back() {
        back(false);
    }

    public void back(boolean force) {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (!chechAutorisationActionOnDepot(selectDoc, 2)) {
                return;
            }
            if (selectDoc.getStatut().equals(Constantes.ETAT_ENCOURS) || selectDoc.getStatut().equals(Constantes.ETAT_SOUMIS)) {
                if (!force) {
                    boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
                    exist_inventaire = !controleInventaire(selectDoc.getSource().getId(), selectDoc.getDateDoc(), selectDoc.getCreneauSource().getTranche().getId(), !gescom_update_stock_after_valide);
                    if (exist_inventaire) {
                        if (!gescom_update_stock_after_valide) {
                            return;
                        } else if (!force) {
                            openDialog("dlgConfirmChangeInventaireByBack");
                            return;
                        }
                    }
                }
            }
            if (changeStatut(Constantes.ETAT_RENVOYE)) {
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(currentUser.getUsers());
                selectDoc.setValiderBy(null);
                selectDoc.setCloturerBy(null);
                selectDoc.setDateAnnuler(new Date());
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(null);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    selectDoc.setAuthor(currentUser);
                }
                dao.update(selectDoc);
                if (exist_inventaire) {
                    champ = new String[]{"type", "depot", "tranche", "date", "statut"};
                    YvsComDocStocks inventaire = dao.lastInventaire(selectDoc.getSource().getId(), selectDoc.getDateDoc(), selectDoc.getCreneauSource().getId());
                    if (inventaire != null ? inventaire.getId() > 0 : false) {
                        for (YvsComContenuDocStock c : docStock.getContenus()) {
                            majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), Constantes.MOUV_ENTREE);
                        }
                    }
                }
                exist_inventaire = false;
            }
        }
    }
    boolean exist_inventaire;

    public boolean transmisOrderByForce() {
        return transmis(true);
    }

    public boolean transmis() {
        return transmis(false);
    }

    /**
     * * Faire les vérification avant transmission du document
     *
     * @param force
     * @return
     */
    public boolean transmis(boolean force) {
        if (selectDoc == null) {
            getErrorMessage("Aucun document n'a été selectionné !");
            return false;
        }
        int idx = documents.indexOf(selectDoc);
        if (idx >= 0) {
            if (documents.get(idx).getContenus().isEmpty()) {
                getErrorMessage("Vous ne pouvez transmettre un document vide !");
                return false;
            }
        } else {
            if (selectDoc.getContenus().isEmpty()) {
                getErrorMessage("Vous ne pouvez transmettre un document vide !");
                return false;
            }
        }
        // Vérifié qu'aucun document d'inventaire n'exite après cette date
        if (!force) {
            boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
            exist_inventaire = !controleInventaire(docStock.getSource().getId(), docStock.getDateDoc(), docStock.getCreneauSource().getTranche().getId(), !gescom_update_stock_after_valide);
            if (exist_inventaire) {
                if (!gescom_update_stock_after_valide) {
                    return false;
                } else if (!force) {
                    openDialog("dlgConfirmChangeInventaireBySoumis");
                    return false;
                }
            }
        }
        if (!verifyOperation(docStock.getSource(), Constantes.TRANSFERT, Constantes.TRANSFERT, true)) {
            return false;
        }
        if (!autoriser("tr_valid_all")) {   //droit de super validation des transfert
            ServiceTransfert service = new ServiceTransfert(dao, currentNiveau, currentUser);
            if (!service.chechAutorisationActionOnDepot(selectDoc, 1, currentUser.getUsers())) {
                getErrorMessage("Vous n'êtes pas habileté à réaliser cette opération !", "Vérifier vos autorisations dans le dépôt " + selectDoc.getSource().getDesignation());
                return false;
            }
        }
        String result;
        List<YvsBaseConditionnement> controls = new ArrayList<>();
        YvsBaseConditionnement cond;
        double quantite;
        for (YvsComContenuDocStock c : docStock.getContenus()) {
            quantite = c.getQuantite();
            int idxCond = controls.indexOf(c.getConditionnement());
            int idxLot = -1;
            if (idxCond > -1) {
                if (c.getLotSortie() == null ? true : c.getLotSortie().getId() < 1) {
                    //s'il y a pas e lot
                    quantite += controls.get(idxCond).getStock();
                } else {
                    idxLot = controls.get(idxCond).getLots().indexOf(c.getLotSortie());
                    if (idxLot > -1) {
                        quantite += controls.get(idxCond).getLots().get(idxLot).getStock();
                    }
                }
            }
            result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), docStock.getSource().getId(), 0L, quantite, 0, "INSERT", "S", docStock.getDateDoc(), (c.getLotSortie() != null ? c.getLotSortie().getId() : 0));
            if (result != null) {
                getErrorMessage("Impossible de valider ce document", "la ligne d'article " + c.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + result);
                return false;
            }
            if (idxCond > -1) {
                if (idxLot < 0) {
                    controls.get(idxCond).setStock(controls.get(idxCond).getStock() + c.getQuantite());
                } else {
                    //s'il y a le lot, 
                    controls.get(idxCond).getLots().get(idxLot).setStock(controls.get(idxCond).getLots().get(idxLot).getStock() + c.getQuantite());
                }
            } else {
                if (c.getLotSortie() == null ? true : c.getLotSortie().getId() < 1) {
                    //s'il y a pas de lot
                    c.getConditionnement().setStock(c.getQuantite());
                    controls.add(c.getConditionnement());
                } else {
                    cond = c.getConditionnement();
                    cond.setLots(new ArrayList<YvsComLotReception>());
                    c.getLotSortie().setStock(c.getQuantite());
                    cond.getLots().add(c.getLotSortie());
                    controls.add(cond);
                }
            }
            String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
            Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(docStock.getSource().getId(), 2)});
            if (requiere_lot != null ? (requiere_lot ? c.getLotSortie() != null ? c.getLotSortie().getId() < 1 : true : false) : false) {
                getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le dépôt " + docStock.getSource().getDesignation());
                return false;
            }
        }
        if (changeStatut(Constantes.ETAT_SOUMIS)) {
            selectDoc.setStatut(Constantes.ETAT_SOUMIS);
            selectDoc.setAuthor(currentUser);
            selectDoc.setAuthorTransmis(currentUser);
            selectDoc.setDateUpdate(new Date());
            selectDoc.setDateTransmis(new Date());
            dao.update(selectDoc);
            if (exist_inventaire) {
                YvsComDocStocks inventaire = dao.lastInventaire(selectDoc.getSource().getId(), docStock.getDateDoc(), selectDoc.getCreneauSource().getId());
                if (inventaire != null ? inventaire.getId() > 0 : false) {
                    for (YvsComContenuDocStock c : docStock.getContenus()) {
                        majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), Constantes.MOUV_SORTIE);
                    }
                }
            }
            exist_inventaire = false;
            return true;
        }
        return false;
    }

    public void validerOrderByForce() {
        valider(true, true);
    }

    public void validerOrderAll() {
        try {
            if (!autoriser("tr_valid_all")) {
                openNotAcces();
                return;
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                long succes = 0;
                for (Long ids : l) {
                    if (validerOrderOne(documents.get(ids.intValue()), false, false)) {
                        succes++;
                    }
                }
                if (succes > 0) {
                    succes();
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public boolean validerOrderOne(YvsComDocStocks entity, boolean succes, boolean controle) {
        if ((entity.getStatut().equals(Constantes.ETAT_SOUMIS) || entity.getStatut().equals(Constantes.ETAT_ENCOURS)) && !entity.getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (entity.getDateReception() == null) {
                entity.setDateReception(entity.getDateDoc());
            }
            entity.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{entity}));
            DocStock bean = UtilCom.buildBeanDocStock(entity);
            boolean response = valider(bean, entity, true, false, controle);
            if (response && succes) {
                succes();
            }
            return response;
        }
        return false;
    }

    public boolean valider() {
        return valider(true);
    }

    public boolean valider(boolean confirm) {
        return valider(confirm, false);
    }

    public boolean valider(boolean confirm, boolean force) {
        return valider(docStock, selectDoc, confirm, force);
    }

    public boolean valider(DocStock docStock, YvsComDocStocks selectDoc, boolean confirm, boolean force) {
        return valider(docStock, selectDoc, confirm, force, true);
    }

    public boolean valider(DocStock docStock, YvsComDocStocks selectDoc, boolean confirm, boolean force, boolean controle) {
        if (selectDoc == null) {
            getErrorMessage("Aucun transfert selectionné");
            return false;
        }
        if (!verifyOperation(docStock.getDestination(), Constantes.TRANSFERT, Constantes.TRANSFERT, true)) {
            return false;
        }
        ServiceTransfert service = new ServiceTransfert(dao, currentNiveau, currentUser, currentAgence.getSociete());
        if (!autoriser("tr_valid_all")) {   //droit de super validation des transfert
            if (!service.chechAutorisationActionOnDepot(selectDoc, 2, currentUser.getUsers())) {
                getErrorMessage("Vous n'êtes pas habileté à réaliser cette opération !", "Vérifiez vos autorisations dans le dépôt " + selectDoc.getDestination().getDesignation());
                return false;
            }
        }
        if (!confirm) {
            update("date-valide_transfert_stock");
            openDialog("dlgValideTrancsfert");
            return false;
        }

        if (docStock.getDateReception().before(docStock.getDateDoc())) {
            getErrorMessage("La date de validation ne peut pas être anterieure à la date d'émission");
            return false;
        }
        if (docStock.getDateReception().after(new Date())) {
            getErrorMessage("Vous ne pouvez valider ce document dans le future");
            return false;
        }
        // Vérifié qu'aucun document d'inventaire n'exite après cette date
        if (!force) {
            boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
            exist_inventaire = !controleInventaire(docStock.getDestination().getId(), docStock.getDateReception(), docStock.getCreneauDestinataire().getTranche().getId(), !gescom_update_stock_after_valide);
            if (exist_inventaire) {
                if (!gescom_update_stock_after_valide) {
                    return false;
                } else if (!force) {
                    if (controle) {
                        openDialog("dlgConfirmChangeInventaireByValid");
                    }
                    return false;
                }
            }
        }
        docStock.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{new YvsComDocStocks(docStock.getId())}));
        selectDoc.setContenus(docStock.getContenus());
        if (docStock.getContenus() != null ? !docStock.getContenus().isEmpty() : false) {
            Long count = (Long) dao.loadObjectByNameQueries("YvsBaseMouvementStock.findCountByExterne", new String[]{"externe", "table"}, new Object[]{docStock.getContenus().get(0).getId(), Constantes.yvs_com_contenu_doc_stock});
            if (count != null ? count < 1 : true) {
                //Si aucun mouvement n'a été trouvé
                String result;
                List<YvsBaseConditionnement> controls = new ArrayList<>();
                //vérification du stock à la sortie (prend en compte le fait qu'un article peut se trouver plus d'une fois dans la liste des contenus)
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                    Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(docStock.getSource().getId(), 2)});
                    if (requiere_lot != null ? (requiere_lot ? c.getLotSortie() != null ? c.getLotSortie().getId() < 1 : true : false) : false) {
                        getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le dépôt " + docStock.getSource().getDesignation());
                        return false;
                    }
                    query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                    requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(docStock.getDestination().getId(), 2)});
                    if (requiere_lot != null ? (requiere_lot ? c.getLotEntree() != null ? c.getLotEntree().getId() < 1 : true : false) : false) {
                        getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le dépôt " + docStock.getDestination().getDesignation());
                        return false;
                    }
                    double quantite = c.getQuantite();
                    int control = controls.indexOf(c.getConditionnement());
                    if (control > -1) {
                        quantite += controls.get(control).getStock();
                    }
                    result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), docStock.getSource().getId(), 0L, quantite, 0, "INSERT", "S", docStock.getDateDoc(), (c.getLotSortie() != null ? c.getLotSortie().getId() : 0));
                    if (result != null) {
                        getErrorMessage("Impossible de valider ce document", "la ligne d'article " + c.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + result);
                        return false;
                    }
                    c.getConditionnement().setStock(quantite);
                    if (control > -1) {
                        controls.set(control, c.getConditionnement());
                    } else {
                        controls.add(c.getConditionnement());
                    }
                }
            } else {
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                    Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(docStock.getDestination().getId(), 2)});
                    if (requiere_lot != null ? (requiere_lot ? c.getLotEntree() != null ? c.getLotEntree().getId() < 1 : true : false) : false) {
                        getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le dépôt " + docStock.getDestination().getDesignation());
                        return false;
                    }
                }
            }
        }
        if (changeStatut_(Constantes.ETAT_VALIDE, docStock, selectDoc)) {
            selectDoc.setCloturer(false);
            selectDoc.setAnnulerBy(null);
            selectDoc.setValiderBy(currentUser.getUsers());
            selectDoc.setDateAnnuler(null);
            selectDoc.setDateCloturer(null);
            selectDoc.setDateValider(new Date());
            selectDoc.setDateUpdate(new Date());
            selectDoc.setStatut(Constantes.ETAT_VALIDE);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                selectDoc.setAuthor(currentUser);
            }
            dao.update(selectDoc);
            if (exist_inventaire) {
                YvsComDocStocks inventaire = dao.lastInventaire(selectDoc.getDestination().getId(), docStock.getDateReception(), selectDoc.getCreneauDestinataire().getId());
                if (inventaire != null ? inventaire.getId() > 0 : false) {
                    for (YvsComContenuDocStock c : docStock.getContenus()) {
                        for (YvsComContenuDocStockReception r : c.getReceptions()) {
                            majInventaire(inventaire, c.getArticle(), c.getConditionnementEntree(), r.getQuantite(), Constantes.MOUV_ENTREE);
                        }
                    }
                }
            }
            exist_inventaire = false;
            return true;
        }
        return false;
    }

    public void cancelValider() {
        if (selectDoc == null) {
            getErrorMessage("Aucun transfert selectionné");
            return;
        }
        ServiceTransfert service = new ServiceTransfert(dao, currentNiveau, currentUser);
        if (!autoriser("tr_valid_all")) {   //droit de super validation des transfert
            if (!service.chechAutorisationActionOnDepot(selectDoc, 2, currentUser.getUsers())) {
                getErrorMessage("Vous n'êtes pas habileté à réaliser cette opération !");
                return;
            } else {

            }
        }
        if (docStock.getContenus() != null ? !docStock.getContenus().isEmpty() : false) {
            Long count = (Long) dao.loadObjectByNameQueries("YvsBaseMouvementStock.findCountByExterne", new String[]{"externe", "table"}, new Object[]{docStock.getContenus().get(0).getId(), Constantes.yvs_com_contenu_doc_stock});
            if (count != null ? count < 1 : true) {
                String result;
                List<YvsBaseConditionnement> controls = new ArrayList<>();
                List<ParamStocks> list;
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    list = reorganiseList(c);
                    for (ParamStocks p : list) {
                        result = controleStock(c.getArticle().getId(), c.getConditionnementEntree().getId(), docStock.getDestination().getId(), 0L, p.getQuantite(), 0, "DELETE", "E", p.getDate(), (c.getLotEntree() != null ? c.getLotEntree().getId() : 0));
                        if (result != null) {
                            getErrorMessage("Impossible de continuer ce document", "la ligne d'article " + c.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + result);
                            return;
                        }
                    }
                }
            }
        }
        try {
            for (YvsComContenuDocStock c : docStock.getContenus()) {
                for (YvsComContenuDocStockReception p : c.getReceptions()) {
                    p.setAuthor(currentUser);
                    dao.delete(p);
                }
                c.setStatut(Constantes.ETAT_EDITABLE);
                c.setDateUpdate(new Date());
                c.setAuthor(currentUser);
                dao.update(c);
            }
        } catch (Exception ex) {
            getException("Erreur suppresion transfert", ex);
            getErrorMessage("Action non terminé !");
            return;
        }
        selectDoc.setCloturer(false);
        selectDoc.setAnnulerBy(null);
        selectDoc.setValiderBy(null);
        selectDoc.setDateAnnuler(null);
        selectDoc.setDateCloturer(null);
        selectDoc.setDateValider(null);
        selectDoc.setStatut(Constantes.ETAT_SOUMIS);
        selectDoc.setDateUpdate(new Date());
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        docStock.setStatut(Constantes.ETAT_SOUMIS);
        update("form_entete_transfert_stock");
        update("data_contenu_transfert_stock");

    }

    public void cloturer(YvsComDocStocks y) {
        selectDoc = y;
        update("id_confirm_close_ft");
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        selectDoc.setCloturer(!selectDoc.getCloturer());
        selectDoc.setCloturerBy(selectDoc.getCloturer() ? currentUser.getUsers() : null);
        selectDoc.setDateCloturer(selectDoc.getCloturer() ? new Date() : null);
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        documents.set(documents.indexOf(selectDoc), selectDoc);
        if (transfertsHist.contains(selectDoc)) {
            transfertsHist.set(transfertsHist.indexOf(selectDoc), selectDoc);
            update("data_fiche_transfert_hist");
        }
        update("data_transfert_stock");
    }

    public boolean changeStatut(String etat) {
        if (changeStatut_(etat)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat) {
        return changeStatut_(etat, docStock, selectDoc);
    }

    public boolean changeStatut_(String etat, DocStock docStock, YvsComDocStocks selectDoc) {
        ServiceTransfert serviceTransfert = new ServiceTransfert(dao, currentNiveau, currentUser, currentUser.getAgence().getSociete());
        if (!etat.equals("") && selectDoc != null) {
            ResultatAction resultService = serviceTransfert.checkBeforChangeStatus(selectDoc, true, true);
            if (!resultService.isResult()) {
                getErrorMessage(resultService.getMessage());
                return false;
            }
            String oldEtat = docStock.getStatut();
            docStock.setStatut(etat);
            selectDoc.setStatut(etat);
            if (documents.contains(selectDoc)) {
                documents.set(documents.indexOf(selectDoc), selectDoc);
            }
            if (!etat.equals(Constantes.ETAT_EDITABLE)) {
                if (transfertsHist.contains(selectDoc)) {
                    transfertsHist.remove(transfertsHist.indexOf(selectDoc));
                    update("data_fiche_transfert_hist");
                }
            } else {
                if (!transfertsHist.contains(selectDoc)) {
                    transfertsHist.add(0, selectDoc);
                    update("data_fiche_transfert_hist");
                }
            }
            if (etat.equals(Constantes.ETAT_EDITABLE) || etat.equals(Constantes.ETAT_ANNULE) || etat.equals(Constantes.ETAT_SOUMIS)) {
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    String delete = "DELETE FROM yvs_com_contenu_doc_stock_reception WHERE contenu = ?";
                    dao.requeteLibre(delete, new Options[]{new Options(c.getId(), 1)});
                    c.setDateUpdate(new Date());
                    c.setStatut(Constantes.ETAT_EDITABLE);
                    c.setAuthor(currentUser);
                    dao.update(c);
                }
            } else if (!oldEtat.equals(Constantes.ETAT_EDITABLE) && !oldEtat.equals(Constantes.ETAT_ANNULE)) {
                if (etat.equals(Constantes.ETAT_VALIDE)) {
                    Date d_ = etat.equals(Constantes.ETAT_VALIDE) ? docStock.getDateReception() : null;
                    for (YvsComContenuDocStock c : docStock.getContenus()) {
                        validerContenu(c, d_, true, false, false);
                    }
                }
            }
            update("data_transfert_stock");
            update("data_contenu_transfert_stock");
            update("form_transfert_stock");
            update("grp_btn_etat_transfert_stock");
            return true;
        }
        return false;
    }

    public void changeStatutLine(YvsComContenuDocStock y) {
        if (!y.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            changeStatutLine(y, Constantes.ETAT_EDITABLE);
        } else {
            selectContenu = y;
            dateReception = docStock.getDateReception();
            update("date-valide_contenu_transfert_stock");
            openDialog("dlgValideContenuTransfert");
        }
    }

    public boolean changeStatutLine(YvsComContenuDocStock y, String statutDoc) {
        return changeStatutLine(y, dateReception, statutDoc, true, true);
    }

    public boolean changeStatutLine(YvsComContenuDocStock y, Date dateReception, String statutDoc, boolean actualise, boolean msg) {
        return changeStatutLine(y, dateReception, statutDoc, true, true, false);
    }

    public boolean changeStatutLine(YvsComContenuDocStock lineContent, Date dateReception, String statutDoc, boolean actualise, boolean msg, boolean allTransfert) {
        ServiceTransfert service = new ServiceTransfert(dao, currentNiveau, currentUser, currentAgence.getSociete());
        ResultatAction resultService = service.controleUpdateContent(lineContent, selectDoc, dateReception, statutDoc, allTransfert);
        if (!resultService.isResult()) {
            getErrorMessage(resultService.getMessage());
            return false;
        }
        if (!lineContent.getStatut().equals(Constantes.ETAT_EDITABLE) || statutDoc.equals(Constantes.ETAT_EDITABLE)) {
            //Cette verification évite de switcher sur les statuts des contenus lorsqu'on valide à partir du document
            if (!allTransfert || statutDoc.equals(Constantes.ETAT_EDITABLE)) {
                if (!autoriser("tr_change_statut_line")) {
                    for (YvsComContenuDocStockReception r : lineContent.getReceptions()) {
                        String result = controleStock(lineContent.getArticle().getId(), lineContent.getConditionnementEntree().getId(), lineContent.getDocStock().getDestination().getId(), 0L, r.getQuantite(), r.getQuantite(), "INSERT", "S", r.getDateReception(), (r.getContenu().getLotEntree() != null ? r.getContenu().getLotEntree().getId() : 0));
                        if (result != null) {
                            if (msg) {
                                getErrorMessage("Impossible de changer le statut cette ligne de transfert car l'article " + lineContent.getArticle().getDesignation() + " engendrera une incohérence dans le stock au " + result);
                            }
                            return false;
                        }
                    }
                }
                lineContent.setStatut(Constantes.ETAT_EDITABLE);
                lineContent.setDateUpdate(new Date());
                lineContent.setAuthor(currentUser);
                dao.update(lineContent);
                String delete = "DELETE FROM yvs_com_contenu_doc_stock_reception WHERE contenu = ?";
                dao.requeteLibre(delete, new Options[]{new Options(lineContent.getId(), 1)});
                lineContent.getReceptions().clear();
                int index = docStock.getContenus().indexOf(lineContent);
                if (index > -1) {
                    docStock.getContenus().set(index, lineContent);
                }
                index = docStock.getContenusSave().indexOf(lineContent);
                if (index > -1) {
                    docStock.getContenusSave().set(index, lineContent);
                }
                if (msg) {
                    succes();
                }
                if (actualise) {
                    actualiseStatutDoc(lineContent.getDocStock());
                }
            }
        } else {
            if (statutDoc.equals(Constantes.ETAT_SOUMIS) || statutDoc.equals(Constantes.ETAT_ENCOURS) || statutDoc.equals(Constantes.ETAT_VALIDE)) {
                return validerContenu(lineContent, dateReception, false, msg, actualise);
            } else {
                if (msg) {
                    getErrorMessage("Impossible de valider cette ligne car le document est encore editable");
                }
            }
        }
        return false;
    }

    private boolean validerContenu(YvsComContenuDocStock y, Date dateReception, boolean control, boolean msg, boolean actualise) {
        try {
            if (control ? !chechAutorisationActionOnDepot(selectDoc, 2) : false) {
                return false;
            }
            if (dateReception != null ? dateReception.before(y.getDocStock().getDateDoc()) : true) {
                if (msg) {
                    getErrorMessage("La date de validation ne peut être antérieure à la date d'émission ou absente");
                }
                return false;
            }
            y.setStatut(Constantes.ETAT_VALIDE);
            y.setDateReception(dateReception);
            y.setAuthor(currentUser);
            y.setDateUpdate(new Date());
            dao.update(y);
            double qte = y.getQuantiteEntree() - y.getQuantiteRecu();
            if (qte > 0) {
                YvsComContenuDocStockReception r = new YvsComContenuDocStockReception();
                r.setAuthor(currentUser);
                r.setContenu(y);
                r.setDateReception(dateReception);
                r.setQuantite(qte);
                r.setCalculPr(y.getCalculPr());
                r.setDateSave(new Date());
                r.setDateUpdate(new Date());
                r = (YvsComContenuDocStockReception) dao.save1(r);
                y.getReceptions().add(r);
            }
            if (docStock != null) {
                int idx = docStock.getContenus().indexOf(y);
                if (idx > -1) {
                    docStock.getContenus().set(idx, y);
                    update("data_contenu_transfert_stock");
                }
                idx = docStock.getContenusSave().indexOf(y);
                if (idx > -1) {
                    docStock.getContenusSave().set(idx, y);
                }
            }
            if (msg) {
                succes();
            }
            if (actualise) {
                actualiseStatutDoc(y.getDocStock());
            }
            return true;
        } catch (Exception ex) {
            getException("validerContenu", ex);
        }
        return false;
    }

    private void actualiseStatutContenu(YvsComContenuDocStock y) {
        if (y == null) {
            return;
        }
        if (y.getId() > 0) {
            String statut = Constantes.ETAT_EDITABLE;
            if (y.getQuantiteRecu() > 0) {
                statut = Constantes.ETAT_ENCOURS;
                if (y.getQuantiteRecu() >= y.getQuantiteEntree()) {
                    statut = Constantes.ETAT_VALIDE;
                }
            }
            y.setStatut(statut);
            dao.update(y);
            int idx = docStock.getContenusSave().indexOf(y);
            if (idx > -1) {
                docStock.getContenusSave().set(idx, y);
            }
            idx = docStock.getContenus().indexOf(y);
            if (idx > -1) {
                docStock.getContenus().set(idx, y);
                update("data_contenu_transfert_stock");
            }
            actualiseStatutDoc(y.getDocStock());
        }
    }

    private void actualiseStatutDoc(YvsComDocStocks y) {
        try {
            if (y != null ? (y.getId() > 0 ? !y.getStatut().equals(Constantes.ETAT_EDITABLE) : false) : false) {
                List<YvsComContenuDocStock> list = dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{y});
                int element_valide = 0;
                int element_attente = 0;
                int element_encours = 0;
                for (YvsComContenuDocStock c : list) {
                    if (c.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        element_valide++;
                    } else if (c.getStatut().equals(Constantes.ETAT_ENCOURS)) {
                        element_encours++;
                    } else {
                        element_attente++;
                    }
                }
                String statut = Constantes.ETAT_SOUMIS;
                if (element_encours > 0) {
                    statut = Constantes.ETAT_ENCOURS;
                } else if (element_valide > 0) {
                    if (element_attente > 0) {
                        statut = Constantes.ETAT_ENCOURS;
                    } else {
                        statut = Constantes.ETAT_VALIDE;
                    }
                }
                y.setStatut(statut);
                if (docStock != null ? docStock.getId() > 0 ? y.equals(selectDoc) : false : false) {
                    docStock.setStatut(statut);
                    selectDoc.setStatut(statut);
                    update("grp_btn_etat_transfert_stock");
                }
                dao.update(y);
                int idx = documents.indexOf(y);
                if (idx > -1) {
                    documents.set(idx, y);
                    update("data_transfert_stock");
                }
            }
        } catch (Exception ex) {
            getException("ManagedTransfertStock (actualiseStatutDoc)", ex);
        }
    }

    public void valideContenu() {
        changeStatutLine(selectContenu, docStock.getStatut());
    }

    public void clearParams() {
        load = true;
        numSearch_ = null;
        statut_ = null;
        cloturer_ = null;

        destSearch = 0;
        sourceSearch = 0;

        date_ = false;
        _first = true;
        dateDebut_ = new Date();
        dateFin_ = new Date();
        idsSearch = "";

        paginator.getParams().clear();

//        loadAllTransfert(true);
        addParamAgence();
    }

    public void searchByNum() {
        ParametreRequete p = new ParametreRequete("y.numDoc", "numDoc", null);
        if (Util.asString(numSearch_)) {
            p = new ParametreRequete(null, "reference", numSearch_ + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numDoc)", "reference", numSearch_.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numPiece)", "reference", numSearch_.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        initForm = true;
        loadAllTransfert(true);
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllTransfert(true);
    }

    public void addParamToValide() {
        ParametreRequete p = new ParametreRequete("y.destination", "todos", null, "IN", "AND");
        if (toValideLoad) {
            List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
            if (currentUser.getUsers() != null) {
                ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
            }
            if (ids.isEmpty()) {
                ids.add(-1L);
            }
            p = new ParametreRequete("y.destination.id", "todos", ids, "IN", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllTransfert(true);
    }

    private void addParamDepotSource() {
        ParametreRequete p = new ParametreRequete("source");
        if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
            p = new ParametreRequete("source", "source", new YvsBaseDepots(docStock.getSource().getId()));
            p.setOperation("=");
            p.setPredicat("AND");
            if (!paramsH.contains(p)) {
                paramsH.add(p);
            } else {
                paramsH.set(paramsH.indexOf(p), p);
            }
        } else {
            paramsH.remove(p);
        }
    }

    private void addParamDepotDest() {
        ParametreRequete p = new ParametreRequete("destination");
        if ((docStock.getDestination() != null) ? docStock.getDestination().getId() > 0 : false) {
            p = new ParametreRequete("destination", "destination", new YvsBaseDepots(docStock.getDestination().getId()));
            p.setOperation("=");
            p.setPredicat("AND");
            if (!paramsH.contains(p)) {
                paramsH.add(p);
            } else {
                paramsH.set(paramsH.indexOf(p), p);
            }
        } else {
            paramsH.remove(p);
        }
    }

    private void addParamCreneauSource() {
        ParametreRequete p = new ParametreRequete("creneauSource");
        if ((docStock.getCreneauSource() != null) ? docStock.getCreneauSource().getId() > 0 : false) {
            p = new ParametreRequete("creneauSource", "creneauSource", new YvsComCreneauDepot(docStock.getCreneauSource().getId()));
            p.setOperation("=");
            p.setPredicat("AND");
            if (!paramsH.contains(p)) {
                paramsH.add(p);
            } else {
                paramsH.set(paramsH.indexOf(p), p);
            }
        } else {
            paramsH.remove(p);
        }
    }

    private void addParamCreneauDest() {
        ParametreRequete p = new ParametreRequete("creneauDestinataire");
        if ((docStock.getCreneauDestinataire() != null) ? docStock.getCreneauDestinataire().getId() > 0 : false) {
            p = new ParametreRequete("creneauDestinataire", "creneauDestinataire", new YvsComCreneauDepot(docStock.getCreneauDestinataire().getId()));
            p.setOperation("=");
            p.setPredicat("AND");
            if (!paramsH.contains(p)) {
                paramsH.add(p);
            } else {
                paramsH.set(paramsH.indexOf(p), p);
            }
        } else {
            paramsH.remove(p);
        }
    }

    public void addParamDateContenu(SelectEvent ev) {
        findByDateContenu();
    }

    public void findByDateContenu() {
        ParametreRequete p = new ParametreRequete("y.docStock.dateDoc", "date", null, "BETWEEN", "AND");
        if (dateContenu) {
            if (dateDebutContenu != null && dateFinContenu != null) {
                if (dateDebutContenu.before(dateFinContenu) || dateDebutContenu.equals(dateFinContenu)) {
                    p = new ParametreRequete("y.docStock.dateDoc", "date", dateDebutContenu, dateFinContenu, "BETWEEN", "AND");
                }
            }
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamAgenceSearch() {
        _loadDepot();
        ParametreRequete p = new ParametreRequete("y.docStock.agence", "agence", null);
        if (agenceContenu > 0) {
            p = new ParametreRequete(null, "agence", new YvsAgences(agenceContenu), "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.docStock.destination.agence", "agence", new YvsAgences(agenceContenu), "=", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.docStock.source.agence", "agence", new YvsAgences(agenceContenu), "=", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamSourceSearch() {
        ParametreRequete p = new ParametreRequete("y.docStock.source", "source", null);
        if (sourceContenu > 0) {
            p = new ParametreRequete("y.docStock.source", "source", new YvsBaseDepots(sourceContenu), "=", "AND");
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamDestSearch() {
        ParametreRequete p = new ParametreRequete("y.docStock.destination", "destination", null);
        if (destContenu > 0) {
            p = new ParametreRequete("y.docStock.destination", "destination", new YvsBaseDepots(destContenu), "=", "AND");
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.docStock.numDoc", "reference", null);
        if (reference != null ? reference.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", reference + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docStock.numDoc)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docStock.numPiece)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamStatuts() {
        ParametreRequete p = new ParametreRequete("y.docStock.statut", "statut", null);
        if (statutContenu != null ? statutContenu.trim().length() > 0 : false) {
            p = new ParametreRequete("y.docStock.statut", "statut", statutContenu, "=", "AND");
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamArticle() {
        ParametreRequete p = new ParametreRequete("y.article.refArt", "article", null);
        if (article != null ? article.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "article", article + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", article.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", article.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void removeDoublon(YvsComDocStocks y) {
        selectDoc = y;
    }

    public void removeDoublon() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE) || selectDoc.getStatut().equals(Constantes.ETAT_ENCOURS)) {
                getErrorMessage("Le document doit etre editable pour pouvoir etre modifié");
                return;
            }
            removeDoublonStock(selectDoc.getId());
            succes();
        }
    }

    public void printListing(boolean entree) {
        if (entree ? destContenu < 1 : sourceContenu < 1) {
            getErrorMessage("Vous devez indiquer le depot");
            return;
        }
        ManagedOtherTransfert w = (ManagedOtherTransfert) giveManagedBean(ManagedOtherTransfert.class);
        if (w != null) {
            w.printListing((entree ? destContenu : sourceContenu), 0, dateDebutContenu, dateFinContenu, entree);
        }
    }

    public long countMyTransfert() {
        if (currentCreneauDepot != null && currentUser != null) {
            Long d = (Long) dao.loadObjectByNameQueries("YvsComDocStocks.countMine", new String[]{"creno", "responsable", "statut", "typeDoc"},
                    new Object[]{currentCreneauDepot, currentUser.getUsers().getEmploye(), Constantes.ETAT_SOUMIS, Constantes.TYPE_FT});
            return (d != null) ? d : 0;
        }
        return 0;
    }

    @Override
    public void cleanStock() {
        super.cleanStock();
        initForm = true;
        loadAllTransfert(true);
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void print(YvsComDocStocks y, boolean entree) {
        print(y, true, entree);
    }

    public void print(YvsComDocStocks y, boolean withHeader, boolean entree) {
        try {
            if (currentParamStock != null ? currentParamStock.getId() < 1 : true) {
                currentParamStock = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (y != null ? y.getId() > 0 : false) {
                if (currentParamStock != null ? (currentParamStock.getPrintDocumentWhenValide() && (!y.getStatut().equals(Constantes.ETAT_VALIDE) && !y.getStatut().equals(Constantes.ETAT_SOUMIS))) : false) {
                    getErrorMessage("Le document doit être validé pour pouvoir être téléchargé");
                    return;
                }
                Map<String, Object> param = new HashMap<>();
                param.put("ID", y.getId().intValue());
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(withHeader));
                String report = "fiche_transfert_" + (entree ? "entree" : "sortie");
                if (currentParam != null ? currentParam.getUseLotReception() : false) {
                    report = "fiche_transfert_" + (entree ? "entree" : "sortie") + "_by_lot";
                }
                executeReport(report, param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedTransfertStock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reBuildNumero() {
        if (tabIds != null ? tabIds.trim().length() > 0 : false) {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComDocStocks current;
                for (Integer index : ids) {
                    current = reBuildNumero(documents.get(index), true, false);
                    documents.set(index, current);
                }
                succes();
            }
            tabIds = "";
        }
    }

    public YvsComDocStocks reBuildNumero(YvsComDocStocks y, boolean save, boolean msg) {
        return reBuildNumero(y, y.getTypeDoc(), save, msg);
    }

    public YvsComDocStocks reBuildNumero(YvsComDocStocks y, String type, boolean save, boolean msg) {
        if (y != null ? (y.getId() > 0 ? (y.getSource() != null ? y.getSource().getId() > 0 : false) : false) : false) {
            String num = genererReference(giveNameType(type), y.getDateDoc(), (y.getSource() != null ? y.getSource().getId() : 0), Constantes.DEPOT);;
            if (num != null ? num.trim().length() < 1 : true) {
                return y;
            }
            y.setNumDoc(num);
            y.setAuthor(currentUser);
            if (save) {
                dao.update(y);
            }
            if (msg) {
                succes();
            }
        }
        return y;
    }

    private List<ParamStocks> reorganiseList(YvsComContenuDocStock c) {
        List<ParamStocks> re = new ArrayList<>();
        if (c != null ? c.getReceptions() != null : false) {
            ParamStocks p;
            int idx;
            for (YvsComContenuDocStockReception cr : c.getReceptions()) {
                p = new ParamStocks(cr.getDateReception(), cr.getQuantite());
                if (re.contains(p)) {
                    idx = re.indexOf(p);
                    re.get(idx).setQuantite(re.get(idx).getQuantite() + cr.getQuantite());
                } else {
                    re.add(p);
                }
            }
        }
        return re;
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllTransfert(true);
    }

    public void onRowLotEdit(RowEditEvent ev) {
        if (ev != null) {
            YvsComLotReception y = (YvsComLotReception) ev.getObject();
            if (y != null) {
                onBlurQuantiteeLot(y);
            }
        }
    }

    public void onBlurQuantiteeLot(YvsComLotReception y) {
        try {
            double quantite = y.getQuantitee();
            if (quantite > y.getStock()) {
                y.setQuantitee(0);
                getErrorMessage("La quantitée entrée ne peut pas dépasser le stock total du lot");
                update("data-contenu_ft_require_lot");
                return;
            }
            for (YvsComLotReception r : contenu.getLots()) {
                if (!r.equals(y)) {
                    quantite += r.getQuantitee();
                }
            }
            if (quantite > contenu.getQuantite()) {
                y.setQuantitee(0);
                getErrorMessage("Vous ne pouvez pas entrer cette quantitée car la somme des quantitées de lot depasse la quantité prevue");
                update("data-contenu_ft_require_lot");
                return;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onBlurQuantiteeLot", ex);
        }
    }

    public void saveLotReception() {
        try {
            ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
            if (m != null) {
                YvsComLotReception y = m._saveNew(m.getLotReception());
                if (y != null ? y.getId() > 0 : false) {
                    reception.setLotEntree(UtilCom.buildBeanLotReception(y));
                    update("select_reception_contenu_doc_stock");
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void findLotReception(ContenuDocStock c) {
        try {
            if (c != null ? c.getLotEntree() != null : false) {
                if (c.getLotEntree().getNumero() != null ? c.getLotEntree().getNumero().trim().length() > 0 : false) {
                    YvsComLotReception l = (YvsComLotReception) dao.loadOneByNameQueries("YvsComLotReception.findByNumeroArticle", new String[]{"article", "numero"}, new Object[]{new YvsBaseArticles(c.getArticle().getId()), c.getLotEntree().getNumero()});
                    if (l != null ? l.getId() < 1 : true) {
                        String query = "SELECT y.id FROM yvs_com_lot_reception y INNER JOIN yvs_base_mouvement_stock_lot l ON l.lot = y.id INNER JOIN yvs_base_mouvement_stock m ON l.mouvement = m.id "
                                + " WHERE m.article = ? AND y.numero = ? ORDER BY y.id DESC LIMIT 1";
                        Long lot = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(c.getLotEntree().getNumero(), 2)});
                        l = new YvsComLotReception(lot, c.getLotEntree().getNumero());
                    }
                    if (l != null ? l.getId() > 0 : false) {
                        c.setLotEntree(UtilCom.buildBeanLotReception(l));
                    } else {
                        c.getLotEntree().setId(0);
                        ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                        if (m != null) {
                            m.resetFiche();
                            m.getLotReception().setNumero(c.getLotEntree().getNumero());
                            m.getLotReception().setArticle(c.getArticle());
                            m.getLotReception().setUpdateArticle(false);
                            update("blog_lot_reception");
                        }
                        openDialog("dlgAddLotReception");
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayContent(YvsComDocStocks y) {
        y.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStockT", new String[]{"docStock"}, new Object[]{y}));
        update("dt_row_ex_" + y.getId());
    }

    public void tryToSendAll(YvsBaseDepots depot) {
        try {
            if (depot != null ? depot.getId() > 0 : false) {
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    load = false;
                    n.naviguationView("Transferts Stock", "modGescom", "smenTransfert", true);
                    resetFiche();
                    docStock.setSource(UtilCom.buildSimpleBeanDepot(depot));
                    chooseDepotSource();
                    update("select_source_transfert_stock");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("tryToSendAll", ex);
        }
    }

    public void sendAllStockToAnotherDepot() {
        try {
            if (!docStock.getContenus().isEmpty()) {
                getErrorMessage("Vous ne pouvez pas effectuer cette action sur un document qui a du contenu");
                return;
            }
            if (docStock.getId() < 1) {
                if (!saveNew()) {
                    return;
                }
            }
            List<YvsBaseArticleDepot> articles = dao.loadNameQueries("YvsBaseArticleDepot.findByDepot", new String[]{"depot"}, new Object[]{new YvsBaseDepots(docStock.getSource().getId())});
            double stock = 0, prix = 0;
            YvsComContenuDocStock y;
            List<YvsBaseConditionnement> conditionnements;
            for (YvsBaseArticleDepot ad : articles) {
                conditionnements = dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{ad.getArticle()});
                for (YvsBaseConditionnement c : conditionnements) {
                    stock = dao.stocks(ad.getArticle().getId(), 0, docStock.getSource().getId(), 0, 0, docStock.getDateDoc(), c.getId(), 0);
                    if (stock > 0) {
                        prix = dao.getPr(currentAgence.getId(), ad.getArticle().getId(), docStock.getSource().getId(), 0, docStock.getDateDoc(), c.getId());
                        y = new YvsComContenuDocStock();
                        y.setArticle(ad.getArticle());
                        y.setQuantite(stock);
                        y.setQuantiteEntree(stock);
                        y.setPrix(prix);
                        y.setPrixEntree(prix);
                        y.setConditionnement(c);
                        y.setConditionnementEntree(c);
                        y.setDocStock(selectDoc);
                        y.setAuthor(currentUser);

                        y = (YvsComContenuDocStock) dao.save1(y);
                        docStock.getContenus().add(y);
                    }
                }
            }
            if (!docStock.getContenus().isEmpty()) {
                succes();
                update("data_contenu_transfert_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("sendAllStockToAnotherDepot", ex);
        }
    }
}
