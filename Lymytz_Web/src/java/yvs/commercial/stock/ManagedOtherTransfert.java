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
import org.primefaces.event.UnselectEvent;
import yvs.MyLog;
import yvs.base.compta.CategorieComptable;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.production.UtilProd;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.Qualite;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.LotReception;
import yvs.commercial.achat.ManagedLotReception;
import yvs.commercial.creneau.Creneau;
import yvs.commercial.depot.ManagedDepot;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepotOperation;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComCoutSupDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.stock.YvsComNatureDoc;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;
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
public class ManagedOtherTransfert extends ManagedCommercial<DocStock, YvsComDocStocks> implements Serializable {

    private DocStock docStock = new DocStock();
    private List<YvsComDocStocks> documents, historiques;
    private PaginatorResult<YvsComDocStocks> p_historique = new PaginatorResult<>();
    private YvsComDocStocks selectDoc, selectEntree, selectSortie;

    private ContenuDocStock contenu = new ContenuDocStock();
    private YvsComContenuDocStock selectContenu;
    private String commentaire;
    private List<YvsComContenuDocStock> all_contenus;
    public PaginatorResult<YvsComContenuDocStock> p_contenu = new PaginatorResult<>();

    private List<YvsComContenuDocStock> selectContenus;
    private boolean memoriserDeleteContenu;

    private String motifEtape;
    YvsWorkflowValidDocStock etape;
    private boolean lastEtape;

    private List<String> typesOpe;
    private List<YvsBaseDepotOperation> operations;

    private long tranche, depot;
    private List<YvsComCreneauDepot> tranches;

    private CategorieComptable categorie = new CategorieComptable();
    private List<CategorieComptable> categories;

    boolean exist_inventaire;

    private String tabIds, tabIds_contenu, egaliteStatut = "!=", valeurBy = "E";
    private Boolean toValideLoad;
    private boolean box, withHeader = true;
    private boolean selectArt, listArt;
    private boolean entree;
    private List<String> statuts;

    private long depotSearch;
    //Parametre recherche contenu
    private boolean dateContenu = false;
    private long depotContenu, agenceContenu;
    private Date dateDebutContenu = new Date(), dateFinContenu = new Date();
    private String statutContenu, reference, article, articleContenu;

    private List<YvsComNatureDoc> natures;
    private NatureDoc nature = new NatureDoc();

    public ManagedOtherTransfert() {
        categories = new ArrayList<>();
        operations = new ArrayList<>();
        historiques = new ArrayList<>();
        documents = new ArrayList<>();
        tranches = new ArrayList<>();
        typesOpe = new ArrayList<>();
        statuts = new ArrayList<>();
        all_contenus = new ArrayList<>();
        natures = new ArrayList<>();
        selectContenus = new ArrayList<>();
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

    public boolean isWithHeader() {
        return withHeader;
    }

    public void setWithHeader(boolean withHeader) {
        this.withHeader = withHeader;
    }

    public long getAgenceContenu() {
        return agenceContenu;
    }

    public void setAgenceContenu(long agenceContenu) {
        this.agenceContenu = agenceContenu;
    }

    public String getValeurBy() {
        return valeurBy;
    }

    public void setValeurBy(String valeurBy) {
        this.valeurBy = valeurBy;
    }

    public String getArticleContenu() {
        return articleContenu;
    }

    public void setArticleContenu(String articleContenu) {
        this.articleContenu = articleContenu;
    }

    public boolean isDateContenu() {
        return dateContenu;
    }

    public void setDateContenu(boolean dateContenu) {
        this.dateContenu = dateContenu;
    }

    public long getDepotContenu() {
        return depotContenu;
    }

    public void setDepotContenu(long depotContenu) {
        this.depotContenu = depotContenu;
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

    public YvsWorkflowValidDocStock getCurrentEtape() {
        return currentEtape;
    }

    public void setCurrentEtape(YvsWorkflowValidDocStock currentEtape) {
        this.currentEtape = currentEtape;
    }

    public List<YvsComContenuDocStock> getAll_contenus() {
        return all_contenus;
    }

    public void setAll_contenus(List<YvsComContenuDocStock> all_contenus) {
        this.all_contenus = all_contenus;
    }

    public PaginatorResult<YvsComContenuDocStock> getP_contenu() {
        return p_contenu;
    }

    public void setP_contenu(PaginatorResult<YvsComContenuDocStock> p_contenu) {
        this.p_contenu = p_contenu;
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

    public Boolean getToValideLoad() {
        return toValideLoad;
    }

    public void setToValideLoad(Boolean toValideLoad) {
        this.toValideLoad = toValideLoad;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public PaginatorResult<YvsComDocStocks> getP_historique() {
        return p_historique;
    }

    public void setP_historique(PaginatorResult<YvsComDocStocks> p_historique) {
        this.p_historique = p_historique;
    }

    public boolean isListArt() {
        return listArt;
    }

    public void setListArt(boolean listArt) {
        this.listArt = listArt;
    }

    public YvsComDocStocks getSelectEntree() {
        return selectEntree;
    }

    public void setSelectEntree(YvsComDocStocks selectEntree) {
        this.selectEntree = selectEntree;
    }

    public YvsComDocStocks getSelectSortie() {
        return selectSortie;
    }

    public void setSelectSortie(YvsComDocStocks selectSortie) {
        this.selectSortie = selectSortie;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public long getDepot() {
        return depot;
    }

    public void setDepot(long depot) {
        this.depot = depot;
    }

    public long getTranche() {
        return tranche;
    }

    public void setTranche(long tranche) {
        this.tranche = tranche;
    }

    public boolean getEntree() {
        return entree;
    }

    public void setEntree(boolean entree) {
        this.entree = entree;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public YvsComContenuDocStock getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComContenuDocStock selectContenu) {
        this.selectContenu = selectContenu;
    }

    public List<String> getTypesOpe() {
        return typesOpe;
    }

    public void setTypesOpe(List<String> typesOpe) {
        this.typesOpe = typesOpe;
    }

    public boolean isBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    public YvsComDocStocks getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocStocks selectDoc) {
        this.selectDoc = selectDoc;
    }

    public List<YvsComDocStocks> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsComDocStocks> historiques) {
        this.historiques = historiques;
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

    public List<YvsComCreneauDepot> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsComCreneauDepot> tranches) {
        this.tranches = tranches;
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

    public List<YvsBaseDepotOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsBaseDepotOperation> operations) {
        this.operations = operations;
    }

    public List<YvsComNatureDoc> getNatures() {
        return natures;
    }

    public void setNatures(List<YvsComNatureDoc> natures) {
        this.natures = natures;
    }

    public NatureDoc getNature() {
        return nature;
    }

    public void setNature(NatureDoc nature) {
        this.nature = nature;
    }

    public void loadAll(boolean entree_) {
        entree = entree_;
        exist_inventaire = false;
        loadAll();
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
            this.egaliteStatut = "!=";
            this.statut_ = Constantes.ETAT_VALIDE;
            addParamStatut(false);
        }
        loadInfosWarning(false);
        if (entree) {
            toValideLoad = null;
        } else {
            toValideLoad = toValideLoad != null ? toValideLoad : true;
        }
        if (agence_ < 1) {
            agence_ = currentAgence.getId();
        }
        if (agenceContenu < 1) {
            agenceContenu = currentAgence.getId();
        }
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
            addParamToValide();
        }
        initView();
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllTransfert(true, true);
    }

    private void initView() {
        boolean initialise = false;
        if (entree) {
            if (docStock.getTypeDoc().equals(Constantes.TYPE_SS)) {
                resetFiche();
            }
            if (((docStock.getDestination() != null) ? docStock.getDestination().getId() < 1 : true)) {
                initialise = true;
                docStock = new DocStock();
                if (docStock.getDocumentLie() == null) {
                    docStock.setDocumentLie(new DocStock());
                }
                docStock.setDestination(UtilCom.buildBeanDepot(currentDepot));
                docStock.setCreneauDestinataire(UtilCom.buildBeanCreneau(currentCreneauDepot));
            }
            if (selectEntree != null ? selectEntree.getId() > 0 : false) {
                _loadOnView(selectEntree);
            }
        } else {
            if (docStock.getTypeDoc().equals(Constantes.TYPE_ES)) {
                resetFiche();
            }
            if (((docStock.getSource() != null) ? docStock.getSource().getId() < 1 : true)) {
                initialise = true;
                docStock = new DocStock();
                if (docStock.getDocumentLie() == null) {
                    docStock.setDocumentLie(new DocStock());
                }
                docStock.setSource(UtilCom.buildBeanDepot(currentDepot));
                docStock.setCreneauSource(UtilCom.buildBeanCreneau(currentCreneauDepot));
            }
            if (selectSortie != null ? selectSortie.getId() > 0 : false) {
                _loadOnView(selectSortie);
            }
        }
        if (docStock.getId() < 1) {
            docStock.setTypeDoc(entree ? Constantes.TYPE_ES : Constantes.TYPE_SS);
            docStock.setMouvement(entree ? Constantes.MOUV_ENTREE : Constantes.MOUV_SORTIE);
            docStock.setMajPr(currentParamStock != null ? currentParamStock.getCalculPr() : true);
        }
        if (initialise) {
            numSearch_ = "";
            depot = currentDepot != null ? currentDepot.getId() : 0L;
            tranche = currentCreneauDepot != null ? currentCreneauDepot.getId() : 0L;
            docStock.setNature(Constantes.OP_AJUSTEMENT_STOCK);
            all_contenus.clear();
            tranches.clear();
            historiques.clear();
            resetFicheContenu();
            ManagedStockArticle s = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (s != null) {
                s.setEntityDepot(currentDepot);
                s.loadActifArticleByDepot(true, true);
            }
            if (currentDepot != null) {
                loadAllTranche(new Depots(currentDepot.getId()));
            }
        }
        loadTypeOpe();
    }

    public void loadAllNature() {
        natures = dao.loadNameQueries("YvsComNatureDoc.findAll", new String[]{"societe", "actif"}, new Object[]{currentAgence.getSociete(), true});
    }

    public int buildDocByDroit(boolean contenu) {
        return buildDocByDroit((!contenu ? paginator : p_contenu), (entree ? Constantes.TYPE_ES : Constantes.TYPE_SS), contenu, (!contenu ? (toValideLoad != null ? toValideLoad : false) : false));
    }

    public void loadContenus(boolean avance, boolean init) {
        buildDocByDroit(true);
        p_contenu.addParam(new ParametreRequete("y.docStock.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        p_contenu.addParam(new ParametreRequete("y.docStock.typeDoc", "typeDoc", entree ? Constantes.TYPE_ES : Constantes.TYPE_SS, "=", "AND"));
        String orderBy = "y.docStock.dateDoc DESC, y.docStock.numDoc";
        all_contenus = p_contenu.executeDynamicQuery("YvsComContenuDocStock", orderBy, avance, init, dao);
        update(entree ? "data_contenus_entree_stock" : "data_contenus_sortie_stock");
    }

    public void loadAllTransfert(boolean avance, boolean init) {
        buildDocByDroit(false);
        paginator.addParam(new ParametreRequete("y.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.typeDoc", "typeDoc", entree ? Constantes.TYPE_ES : Constantes.TYPE_SS, "=", "AND"));
        documents = paginator.executeDynamicQuery("YvsComDocStocks", "y.dateDoc DESC, y.numDoc DESC", avance, init, (int) imax, dao);
        update(entree ? "data_entree_stock" : "data_sortie_stock");
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
        loadAllTransfert(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllTransfert(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllTransfert(true, true);
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

    public void loadTypeOpe() {
        typesOpe.clear();
        if (entree) {
            typesOpe.add(Constantes.OP_DONS);
            typesOpe.add(Constantes.OP_AJUSTEMENT_STOCK);
            typesOpe.add(Constantes.OP_INITIALISATION);
        } else {
            typesOpe.add(Constantes.OP_DONS);
            typesOpe.add(Constantes.OP_RATIONS);
            typesOpe.add(Constantes.OP_AJUSTEMENT_STOCK);
            typesOpe.add(Constantes.OP_DEPRECIATION);
        }
    }

    public void loadHistorique(boolean avance, boolean init) {
        p_historique.addParam(new ParametreRequete("y.typeDoc", "typeDoc", (entree) ? Constantes.TYPE_ES : Constantes.TYPE_SS, "=", "AND"));
        p_historique.addParam(new ParametreRequete("y.statut", "statut", Constantes.ETAT_EDITABLE, "=", "AND"));
        historiques = p_historique.executeDynamicQuery("YvsComDocStocks", "y.dateDoc DESC", avance, init, 5, dao);
    }

    public void loadAllTranche(Depots depot) {
        tranches.clear();
        champ = new String[]{"permanent", "depot"};
        val = new Object[]{true, new YvsBaseDepots((depot.getId()))};
        List<YvsComCreneauDepot> lc = dao.loadNameQueries("YvsComCreneauDepot.findByDepotPermanent", champ, val);
        for (YvsComCreneauDepot c : lc) {
            if (!tranches.contains(c)) {
                tranches.add(c);
            }
        }

        String jour = Util.getDay(docStock.getDateDoc());
        champ = new String[]{"jour"};
        val = new Object[]{jour};
//        List<YvsJoursOuvres> lj = dao.loadNameQueries("YvsJoursOuvres.findByJour", champ, val);
//        for (YvsJoursOuvres j : lj) {
//            champ = new String[]{"jour", "depot"};
//            val = new Object[]{j, new YvsBaseDepots((depot.getId()))};
//            lc = dao.loadNameQueries("YvsComCreneauDepot.findByJourDepot", champ, val);
//            for (YvsComCreneauDepot c : lc) {
//                if (!tranches.contains(c)) {
//                    tranches.add(c);
//                }
//            }
//        }
    }

    public YvsComDocStocks buildDocStock(DocStock y) {
        int idx;
        if ((y.getCreneauDestinataire() != null) ? y.getCreneauDestinataire().getId() > 0 : false) {
            idx = tranches.indexOf(new YvsComCreneauDepot(y.getCreneauDestinataire().getId()));
            YvsComCreneauDepot c = null;
            if (idx >= 0) {
                c = tranches.get(idx);
            }
            y.setCreneauDestinataire(UtilCom.buildBeanCreneau(c));
        }
        if ((y.getCreneauSource() != null) ? y.getCreneauSource().getId() > 0 : false) {
            idx = tranches.indexOf(new YvsComCreneauDepot(y.getCreneauSource().getId()));
            YvsComCreneauDepot c = null;
            if (idx >= 0) {
                c = tranches.get(idx);
            }
            y.setCreneauSource(UtilCom.buildBeanCreneau(c));
        }
        return UtilCom.buildDocStock(y, currentAgence.getSociete(), currentUser);
    }

    public YvsComContenuDocStock buildContenuDocStock(ContenuDocStock y) {
        YvsComContenuDocStock r = UtilCom.buildContenuDocStock(y, currentUser);
        r.setQuantiteEntree(r.getQuantite());
        r.setConditionnementEntree(r.getConditionnement());
        r.setQualiteEntree(r.getQualite());
        r.setLotEntree(r.getLotSortie());
        return r;
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
        docStock.setTypeDoc(Util.asString(docStock.getTypeDoc()) ? docStock.getTypeDoc() : Constantes.TYPE_ES);
        return docStock;
    }

    public ContenuDocStock recopieViewContenu(DocStock doc) {
        return recopieViewContenu(contenu, doc);
    }

    public ContenuDocStock recopieViewContenu(ContenuDocStock contenu, DocStock doc) {
        contenu.setPrixTotal(contenu.getQuantite() * contenu.getPrix());
        contenu.setDocStock(doc);
        contenu.setNew_(true);
        return contenu;
    }

    @Override
    public boolean controleFiche(DocStock bean) {
        if (!_controleFiche_(bean, true)) {
            return false;
        }
        if ((entree && !bean.getTypeDoc().equals(Constantes.TYPE_ES)) || (!entree && !bean.getTypeDoc().equals(Constantes.TYPE_SS))) {
            getErrorMessage("Impossible de poursuivre cette opération... veuillez cliquer sur nouveau et recommencer");
            return false;
        }
        if (!Util.asString(bean.getTypeDoc())) {
            bean.setTypeDoc(entree ? Constantes.TYPE_ES : Constantes.TYPE_SS);
        }
        if (!Util.asString(bean.getMouvement())) {
            bean.setMouvement(entree ? Constantes.MOUV_ENTREE : Constantes.MOUV_SORTIE);
        }
        if (bean.getNumDoc() == null || bean.getNumDoc().equals("")) {
            String ref;
            if (entree) {
                ref = genererReference(Constantes.TYPE_ES_NAME, bean.getDateDoc(), bean.getDestination().getId());
            } else {
                ref = genererReference(Constantes.TYPE_SS_NAME, bean.getDateDoc(), bean.getSource().getId());
            }
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
            bean.setNumDoc(ref);
        }
        if (!verifyDateStock(bean.getDateDoc(), bean.isUpdate())) {
            return false;
        }
        // Vérifié qu'aucun document d'inventaire n'exite après cette date
        boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
        Long y_depot = (entree) ? bean.getDestination().getId() : bean.getSource().getId();
        Long y_creneau = (entree) ? bean.getCreneauDestinataire().getId() : bean.getCreneauSource().getId();
        //récupère la tranche d'inventaire
        Long y_tranche = (Long) dao.loadOneByNameQueries("YvsComCreneauDepot.findIdTrancheById", new String[]{"id"}, new Object[]{y_creneau});
        boolean exist_inventaire = !controleInventaire(y_depot, docStock.getDateDoc(), y_tranche, !gescom_update_stock_after_valide);
        if (exist_inventaire && !gescom_update_stock_after_valide) {
            return false;
        }
        switch (bean.getMouvement()) {
            case Constantes.MOUV_ENTREE:
                if ((bean.getDestination() != null) ? bean.getDestination().getId() < 1 : true) {
                    getErrorMessage("Vous devez specifier le depot");
                    return false;
                }
                bean.setSource(bean.getDestination());
                if ((bean.getCreneauDestinataire() != null) ? bean.getCreneauDestinataire().getId() < 1 : true) {
                    getErrorMessage("Vous devez specifier le créneau");
                    return false;
                }
                bean.setCreneauSource(bean.getCreneauDestinataire());
                break;
            case Constantes.MOUV_SORTIE:
                if ((bean.getSource() != null) ? bean.getSource().getId() < 1 : true) {
                    getErrorMessage("Vous devez specifier le depot");
                    return false;
                }
                bean.setDestination(bean.getSource());
                if ((bean.getCreneauSource() != null) ? bean.getCreneauSource().getId() < 1 : true) {
                    getErrorMessage("Vous devez specifier le créneau");
                    return false;
                }
                bean.setCreneauDestinataire(bean.getCreneauSource());
                break;
            default:
                getErrorMessage("Vous devez préciser le mouvement");
                return false;
        }
        return verifyTranche(new TrancheHoraire(y_tranche), new Depots(y_depot), bean.getDateDoc());
    }

    private boolean _controleFiche_(DocStock bean, boolean c) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (!c) {
            if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
                return false;
            }
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
        if (bean.getDescription() != null ? bean.getDescription().isEmpty() : true) {
            getErrorMessage("Vous devez entrer le motif de cette opération !");
            return false;
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean _controleFiche_(YvsComDocStocks bean) {
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
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    public boolean controleFicheContenu(ContenuDocStock bean) {
        return controleFicheContenu(bean, true);
    }

    public boolean controleFicheContenu(ContenuDocStock bean, boolean principal) {
        if (bean.getQuantite() <= 0) {
            getErrorMessage("Vous devez entrer une quantité valide");
            return false;
        }
        if (bean.getDocStock() != null ? bean.getDocStock().getId() < 1 : true) {
            if (!_saveNew()) {
                return false;
            }
            bean.setDocStock(docStock);
        }
        if (selectDoc != null ? selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getTypeDoc().equals(Constantes.TYPE_IN) : false : false) {
            if (!autoriser("d_stock_edit_when_doc_valid")) {
                getErrorMessage("La validation de ce document est lié  à celle de l'inventaire " + selectDoc.getDocumentLie().getNumDoc() + " l'ayant généré");
                return false;
            }
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le conditionnement");
            return false;
        }
        if (!bean.getDocStock().getStatut().equals(Constantes.ETAT_EDITABLE)) {
            if (autoriser("d_stock_edit_when_doc_valid")) {
                if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                    getErrorMessage("Vous ne pouvez pas modifier cette ligne car le document est " + giveNameStatut(bean.getDocStock().getStatut()).toLowerCase() + " et la ligne est valide");
                    return false;
                }
            } else {
                getErrorMessage("Vous ne pouvez pas modifier cette ligne car le document n'est plus editable");
                return false;
            }
        }
        if (bean.getArticle().isRequiereLot()) {
            if (principal && !entree) {
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
        if (bean.getDocStock().getStatut().equals(Constantes.ETAT_VALIDE)) {
            String result;
            if (!entree) {
                result = controleStock(bean.getArticle().getId(), bean.getConditionnement().getId(), (selectContenu != null ? selectContenu.getConditionnement().getId() : bean.getConditionnement().getId()), docStock.getSource().getId(), 0L, bean.getQuantite(), (selectContenu != null ? selectContenu.getQuantite() : 0), bean.getId() > 0 ? "UPDATE" : "INSERT", "S", docStock.getDateDoc(), (selectContenu != null ? selectContenu.getLotSortie() != null ? selectContenu.getLotSortie().getId() : 0 : 0));
            } else {
                result = controleStock(bean.getArticle().getId(), bean.getConditionnement().getId(), (selectContenu != null ? selectContenu.getConditionnement().getId() : bean.getConditionnement().getId()), docStock.getDestination().getId(), 0L, bean.getQuantite(), (selectContenu != null ? selectContenu.getQuantite() : 0), bean.getId() > 0 ? "UPDATE" : "INSERT", "E", docStock.getDateDoc(), (selectContenu != null ? selectContenu.getLotEntree() != null ? selectContenu.getLotEntree().getId() : 0 : 0));
            }
            if (result != null) {
                getErrorMessage("L'article '" + bean.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                return false;
            }
        }
        return _controleFiche_(bean.getDocStock(), true);
    }

    private boolean controleDleteLine(YvsComContenuDocStock bean) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (bean.getDocStock() != null ? bean.getDocStock().getDocumentLie() != null ? bean.getDocStock().getDocumentLie().getTypeDoc().equals(Constantes.TYPE_IN) : false : false) {
            getErrorMessage("La validation de ce document est lié  à celle l'inventaire " + bean.getDocStock().getDocumentLie().getNumDoc() + " l'ayant généré");
            return false;
        }
        if (docStock.getStatut().equals(Constantes.ETAT_VALIDE)) {
            //il faut le droit de modifier les ligne de stock déjà validé
            if (!autoriser("d_stock_edit_when_doc_valid")) {
                getErrorMessage("Le document doit être editable pour être modifié !");
                return false;
            }
        }
        if (docStock.isCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("La ligne doit être editable");
            return false;
        }
        String result = null;
        if (entree && docStock.getStatut().equals(Constantes.ETAT_VALIDE)) {
            result = controleStock(bean.getArticle().getId(), bean.getConditionnement().getId(), docStock.getDestination().getId(), 0L, bean.getQuantite(), (bean != null) ? bean.getQuantite() : 0, "DELETE", "E", docStock.getDateDoc(), (bean.getLotEntree() != null ? bean.getLotEntree().getId() : 0));
            if (result != null) {
                getErrorMessage("L'article '" + bean.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheCout(CoutSupDoc bean) {
        if (bean.getDoc() < 1) {
            if (!_saveNew()) {
                return false;
            }
        }
        if (bean.getType() != null ? bean.getType().getId() > 0 : false) {
            getErrorMessage("Vous devez entrer le type de cout");
            return false;
        }
        return _controleFiche_(docStock, false);
    }

    @Override
    public void populateView(DocStock bean) {
        cloneObject(docStock, bean);
        champ = new String[]{"docStock"};
        val = new Object[]{new YvsComDocStocks(bean.getId())};
        docStock.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", champ, val));
        docStock.setContenusSave(new ArrayList<>(docStock.getContenus()));
        docStock.setEtapesValidations(ordonneEtapes(docStock.getEtapesValidations()));
        if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                if (m.getDepots().contains(new YvsBaseDepots(docStock.getSource().getId()))) {
                    YvsBaseDepots y = m.getDepots().get(m.getDepots().indexOf(new YvsBaseDepots(docStock.getSource().getId())));
                    Depots d = UtilCom.buildBeanDepot(y);
                    cloneObject(docStock.getSource(), d);
                    depot = d.getId();
                    ManagedStockArticle s = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                    if (s != null) {
                        s.setEntityDepot(y);
                        s.loadActifArticleByDepot(true, true);
                    }
                    loadAllTranche(d);
                }
            }
        }
        if ((docStock.getDestination() != null) ? docStock.getDestination().getId() > 0 : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                if (m.getDepots().contains(new YvsBaseDepots(docStock.getDestination().getId()))) {
                    YvsBaseDepots y = m.getDepots().get(m.getDepots().indexOf(new YvsBaseDepots(docStock.getDestination().getId())));
                    Depots d = UtilCom.buildBeanDepot(y);
                    cloneObject(docStock.getDestination(), d);
                    depot = d.getId();
                    ManagedStockArticle s = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                    if (s != null) {
                        s.setEntityDepot(y);
                        s.loadActifArticleByDepot(true, true);
                    }
                    loadAllTranche(d);
                }
            }
        }
        tranche = 0;
        if ((docStock.getCreneauDestinataire() != null) ? docStock.getCreneauDestinataire().getId() > 0 : false) {
            tranche = docStock.getCreneauDestinataire().getId();
        }
        if ((docStock.getCreneauSource() != null) ? docStock.getCreneauSource().getId() > 0 : false) {
            tranche = docStock.getCreneauSource().getId();
        }
        if (bean.getEtapesValidations() != null ? !bean.getEtapesValidations().isEmpty() : false) {
            docStock.setFirstEtape(bean.getEtapesValidations().get(0).getEtape().getLabelStatut());
        }
        setMontantTotalDoc(docStock, docStock.getContenusSave());
    }

    public void populateViewContenu(ContenuDocStock bean) {
        cloneObject(contenu, bean);
        if (entree) {
            bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docStock.getDestination().getId(), 0, 0, docStock.getDateDoc(), bean.getConditionnement().getId(), bean.getLotSortie().getId()));
            bean.getArticle().setPuv(dao.getPuv(bean.getArticle().getId(), bean.getQuantite(), 0, 0, docStock.getDestination().getId(), 0, docStock.getDateDoc(), bean.getConditionnement().getId()));
            contenu.setPrix((bean.getPrix() <= 0) ? dao.getPua(bean.getArticle().getId(), 0) : bean.getPrix());
        } else {
            bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docStock.getSource().getId(), 0, 0, docStock.getDateDoc(), bean.getConditionnement().getId(), bean.getLotSortie().getId()));
            bean.getArticle().setPuv(dao.getPuv(bean.getArticle().getId(), bean.getQuantite(), 0, 0, docStock.getSource().getId(), 0, docStock.getDateDoc(), bean.getConditionnement().getId()));
            contenu.setPrix((bean.getPrix() <= 0) ? dao.getPua(bean.getArticle().getId(), 0) : bean.getPrix());
        }
        contenu.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(bean.getArticle().getId())}));
        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));

        String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
        Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(bean.getArticle().getId(), 1), new Options(docStock.getDestination().getId(), 2)});
        contenu.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
        if (!entree) {
            if (contenu.getArticle().isRequiereLot() && (docStock.getSource() != null ? docStock.getSource().getId() > 0 : false)) {
                ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                if (w != null) {
                    contenu.setLots(w.loadList(docStock.getSource().getId(), contenu.getConditionnement().getId(), docStock.getDateDoc(), contenu.getQuantite(), bean.getArticle().getStock()));
                    update("data-contenu_ss_require_lot");
                }
            }
        }
        selectArt = true;
    }

    @Override
    public void resetFiche() {
        historiques.clear();
        resetFiche_();
        if (entree) {
            update("blog_form_entree_stock");
        } else {
            update("blog_form_sortie_stock");
        }
    }

    public void resetFiche_() {
        resetFiche(docStock);
        docStock.setCategorieComptable(new CategorieComptable());
        docStock.setContenus(new ArrayList<YvsComContenuDocStock>());
        docStock.setContenusSave(new ArrayList<YvsComContenuDocStock>());
        docStock.setCouts(new ArrayList<YvsComCoutSupDocStock>());
        docStock.setCreneauDestinataire(new Creneau());
        docStock.setCreneauSource(new Creneau());
        docStock.setDestination(new Depots());
        docStock.setDocumentLie(new DocStock());
        docStock.setSource(new Depots());
        docStock.setStatut(Constantes.ETAT_EDITABLE);
        docStock.setCloturer(false);
        docStock.setNature(Constantes.OP_AJUSTEMENT_STOCK);
        docStock.setEtapesValidations(new ArrayList<YvsWorkflowValidDocStock>());
        docStock.setTypeDoc(entree ? Constantes.TYPE_ES : Constantes.TYPE_SS);
        tabIds = "";
        if (operations != null) {
            operations.clear();
        } else {
            operations = new ArrayList<>();
        }

        ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (m != null && selectDoc != null) {
            if (entree) {
                if (selectDoc.getDestination() != null) {
                    if (!selectDoc.getDestination().getActif() && m.getDepots().contains(selectDoc.getDestination())) {
                        m.getDepots().remove(selectDoc.getDestination());
                        update("select_depot_entree_stock");
                    }
                }
            } else {
                if (selectDoc.getSource() != null) {
                    if (!selectDoc.getSource().getActif() && m.getDepots().contains(selectDoc.getSource())) {
                        m.getDepots().remove(selectDoc.getSource());
                        update("select_depot_sortie_stock");
                    }
                }
            }
        }
        selectDoc = new YvsComDocStocks();
        tranches.clear();
        if (entree) {
            selectEntree = null;
            docStock.setDestination(UtilCom.buildBeanDepot(currentDepot));
            docStock.setCreneauDestinataire(UtilCom.buildBeanCreneau(currentCreneauDepot));
        } else {
            selectSortie = null;
            docStock.setSource(UtilCom.buildBeanDepot(currentDepot));
            docStock.setCreneauSource(UtilCom.buildBeanCreneau(currentCreneauDepot));
        }
        ManagedStockArticle s = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (s != null && currentDepot != null) {
            s.setEntityDepot(currentDepot);
            s.loadActifArticleByDepot(true, true);
        }
        loadAllTranche(new Depots(currentDepot != null ? currentDepot.getId() : 0));
        depot = 0;
        tranche = 0;
        resetFicheContenu();
        update(entree ? "blog_form_entree_stock" : "blog_form_sortie_stock");
    }

    public void resetFicheContenu() {
        resetFiche(contenu);
        contenu.setArticle(new Articles());
        contenu.setQualite(new Qualite());
        contenu.setQualiteEntree(new Qualite());
        contenu.setConditionnement(new Conditionnement());
        contenu.setUniteDestination(new Conditionnement());
        contenu.setLotSortie(new LotReception());
        contenu.setLotEntree(new LotReception());
        contenu.setCalculPr(docStock.isMajPr());
        contenu.getLots().clear();
        tabIds_contenu = "";
        selectContenu = null;
        selectArt = false;
        listArt = false;
        update(entree ? "desc_artcile_entree_stock" : "desc_artcile_sortie_stock");
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
        try {
            DocStock bean = recopieView();
            if (controleFiche(bean)) {
                selectDoc = buildDocStock(bean);
                List<YvsComContenuDocStock> list = new ArrayList<>(selectDoc.getContenus());
                selectDoc.getContenus().clear();
                if (!bean.isUpdate()) {
                    List<YvsWorkflowEtapeValidation> etapes = saveEtapesValidation(selectDoc);
                    docStock.setEtapeTotal(etapes != null ? etapes.size() : 0);
                    selectDoc.setEtapeTotal(docStock.getEtapeTotal());
                    selectDoc.setId(null);
                    selectDoc.setHeureDoc(new Date());
                    selectDoc = (YvsComDocStocks) dao.save1(selectDoc);
                    docStock.setId(selectDoc.getId());
                    if (!entree) {
                        selectDoc.setEtapesValidations(saveEtapesValidation(selectDoc, etapes));
                        docStock.setEtapesValidations(new ArrayList<>(selectDoc.getEtapesValidations()));
                    }
                    documents.add(0, selectDoc);
                    if (documents.size() > imax) {
                        documents.remove(documents.size() - 1);
                    }
                    if (historiques.size() > 5) {
                        historiques.remove(historiques.size() - 1);
                    }
                } else {
                    dao.update(selectDoc);
                    if (documents.contains(selectDoc)) {
                        documents.set(documents.indexOf(selectDoc), selectDoc);
                    }
                    if (historiques.contains(selectDoc)) {
                        historiques.set(historiques.indexOf(selectDoc), selectDoc);
                    }
                }
                selectDoc.setContenus(list);
                docStock.setContenusSave(list);
                docStock.setNumDoc(bean.getNumDoc());
                docStock.setUpdate(true);
                update(entree ? "data_entree_stock" : "data_sortie_stock");
                update(entree ? "form_entete_entree_stock" : "form_entete_sortie_stock");
                update(entree ? "form_entree_stock" : "form_sortie_stock");
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
            return false;
        }
    }

    public void generateEtapeValidation(YvsComDocStocks ds) {
        if (!ds.getTypeDoc().equals(Constantes.TYPE_ES)) {
            List<YvsWorkflowEtapeValidation> etapes = saveEtapesValidation(ds);
            ds.setEtapeTotal(etapes != null ? etapes.size() : 0);
            ds.setEtapesValidations(saveEtapesValidation(ds, etapes));
            ds.setEtapeValide(0);
            update("data_sortie_stock");
        }
    }

    public void saveSortieContenu() {
        try {
            contenu.setDocStock(docStock);
            if (controleFicheContenu(contenu)) {
                if (contenu.getLots().size() > 1) {
                    ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                    if (w != null) {
                        contenu.setLots(w.buildQuantiteLot(contenu.getLots(), contenu.getQuantite()));
                    }
                    openDialog("dlgListLotReception");
                    update("data-contenu_ss_require_lot");
                } else {
                    saveNewContenu();
                }
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
                update(entree ? "data_entree_stock" : "data_sortie_stock");
                update(entree ? "data_contenu_entree_stock" : "data_contenu_sortie_stock");
                update(entree ? "chp_entree_stock_net_a_payer" : "chp_sortie_stock_net_a_payer");
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
                for (int i = 0; i < lots.size(); i++) {
                    if (lots.get(i).getQuantitee() > 0) {
                        clone = new ContenuDocStock();
                        cloneObject(clone, contenu);
                        clone.setQuantite(lots.get(i).getQuantitee());
                        clone.getLots().clear();
                        calculePrixTotal(clone);
                        clone.setLotSortie(UtilCom.buildBeanLotReception(lots.get(i)));
                        cloneObject(clone.getLotEntree(), clone.getLotSortie());
                        correct = saveNewContenu(clone, false);
                    }
                }
                if (correct) {
                    succes();
                    resetFicheContenu();
                    update(entree ? "data_entree_stock" : "data_sortie_stock");
                }
                update(entree ? "data_contenu_entree_stock" : "data_contenu_sortie_stock");
                update(entree ? "chp_entree_stock_net_a_payer" : "chp_sortie_stock_net_a_payer");
            } else {
                saveNewContenu();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public boolean saveNewContenu(ContenuDocStock contenu, boolean principal) {
        try {
            contenu = recopieViewContenu(contenu, docStock);
            if (controleFicheContenu(contenu, principal)) {
                YvsComContenuDocStock en = buildContenuDocStock(contenu);
                if (!contenu.isUpdate()) {
                    en.setId(null);
                    en = (YvsComContenuDocStock) dao.save1(en);
                    contenu.setId(en.getId());
                    docStock.getContenus().add(0, en);
                } else {
                    dao.update(en);
                    docStock.getContenus().set(docStock.getContenus().indexOf(en), en);
                }
                int idx = docStock.getContenusSave().indexOf(en);
                if (idx < 0) {
                    docStock.getContenusSave().add(en);
                } else {
                    docStock.getContenusSave().set(idx, en);
                }
                idx = documents.indexOf(en.getDocStock());
                if (idx < 0) {
                    idx = 0;
                    documents.add(idx, en.getDocStock());
                }
                if (documents.get(idx).getContenus() == null) {
                    documents.get(idx).setContenus(new ArrayList<YvsComContenuDocStock>());
                }
                int idx_ = documents.get(idx).getContenus().indexOf(en);
                if (idx_ >= 0) {
                    documents.get(idx).getContenus().set(idx_, en);
                } else {
                    documents.get(idx).getContenus().add(0, en);
                }
                setMontantTotalDoc(docStock, docStock.getContenusSave());
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
        return false;
    }

    public void addCommentaireContenu(YvsComContenuDocStock y) {
        selectContenu = y;
        commentaire = y.getCommentaire();
        update(entree ? "txt_commentaire_contenu_entree_stock" : "txt_commentaire_contenu_sortie_stock");
    }

    public void addCommentaireContenu() {
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setCommentaire(commentaire);
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            docStock.getContenus().set(docStock.getContenus().indexOf(selectContenu), selectContenu);
            update(entree ? "data_contenu_entree_stock" : "data_contenu_sortie_stock");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void applyNewPrix() {
        //vérifier le droit
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setAuthor(currentUser);
            selectContenu.setDateUpdate(new Date());
            selectContenu.setPrixEntree(selectContenu.getPrix());
            dao.update(selectContenu);
            docStock.getContenus().set(docStock.getContenus().indexOf(selectContenu), selectContenu);
            update(entree ? "data_contenu_entree_stock" : "data_contenu_sortie_stock");
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
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
            getException("ManagedOtherTransfert (changeCalculPr)", ex);
        }
    }

    public void toogleCancelContentDoc(YvsComContenuDocStock c) {
        //controle le droit
        if (c.getDocStock().getCloturer()) {
            getErrorMessage("Ce document est verrouillé");
            return;
        }
        switch (c.getStatut()) {
            case Constantes.ETAT_EDITABLE:
                //le doc est editable, on valide l'entrée
                //1. Le document doit être validé et le user doit avoir le dreoit de valider la ligne
                if (c.getDocStock().getStatut().equals(Constantes.ETAT_VALIDE)) {
                    if (!checkOperationArticle(c.getArticle().getId(), docStock.getDestination().getId(), Constantes.ENTREE)) {
                        getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' ne fait pas de sortie en stock dans le depot '" + docStock.getSource().getDesignation() + "'");
                        return;
                    }
                    if (!entree) {
                        String result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), docStock.getSource().getId(), 0L, c.getQuantite(), c.getQuantite(), "DELETE", "S", docStock.getDateDoc(), (c.getLotSortie() != null ? c.getLotSortie().getId() : 0));
                        if (result != null) {
                            getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                            return;
                        }
                    }
                    c.setStatut(Constantes.ETAT_VALIDE);
                    c.setAuthor(currentUser);
                    dao.update(c);
                } else {
                    getErrorMessage("Le document d'entrée n'a pas été validé !");
                }

                break;
            case Constantes.ETAT_VALIDE:
                //le doc est validé, annulé l'entré
                String result = null;
                if (entree) {
                    result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), docStock.getDestination().getId(), 0L, c.getQuantite(), c.getQuantite(), "DELETE", "E", docStock.getDateDoc(), (c.getLotEntree() != null ? c.getLotEntree().getId() : 0));
                    if (result != null) {
                        getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                        return;
                    }
                }
                c.setStatut(Constantes.ETAT_EDITABLE);
                c.setAuthor(currentUser);
                dao.update(c);
                break;
        }
    }

    public boolean saveNewCategorie() {

        return true;
    }

    @Override
    public void onSelectDistant(YvsComDocStocks y) {
        if (y != null ? y.getId() > 0 : false) {
            entree = y.getTypeDoc().equals(Constantes.TYPE_ES);
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView(entree_ ? "Entrées Stock" : "Sorties Stock", "modGescom", entree_ ? "smenEntree" : "smenSortie", true);
            }
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser(entree ? "gescom_delete_entree" : "gescom_delete_sortie")) {
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
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(bean);
                    if (historiques.contains(bean)) {
                        historiques.remove(bean);
                    }

                }
                documents.removeAll(list);
                succes();
                resetFiche();
                tabIds = "";
                update(entree ? "data_entree_stock" : "data_sortie_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComDocStocks y) {
        selectDoc = y;
    }

    public void deleteBean_() {
        try {
            if (!autoriser(entree ? "gescom_delete_entree" : "gescom_delete_sortie")) {
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
                if (historiques.contains(selectDoc)) {
                    historiques.remove(selectDoc);
                }
                succes();
                resetFiche();
                update(entree ? "data_entree_stock" : "data_sortie_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenu() {
        try {
            if ((tabIds_contenu != null) ? !tabIds_contenu.equals("") : false) {
                String[] tab = tabIds_contenu.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComContenuDocStock bean = docStock.getContenus().get(docStock.getContenus().indexOf(new YvsComContenuDocStock(id)));
                    if (controleDleteLine(bean)) {
                        bean.setAuthor(currentUser);
                        dao.delete(bean);
                        docStock.getContenus().remove(bean);
                        docStock.getContenusSave().remove(bean);
                    }
                }
                setMontantTotalDoc(docStock, docStock.getContenusSave());
                succes();
                resetFicheContenu();
                update(entree ? "data_contenu_entree_stock" : "data_contenu_sortie_stock");
                update(entree ? "data_entree_stock" : "data_sortie_stock");
                update(entree ? "chp_entree_stock_net_a_payer" : "chp_sortie_stock_net_a_payer");
            }
        } catch (Exception ex) {
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
                    dao.delete(c);
                    docStock.getContenus().remove(c);
                    if (contenu.getId() == c.getId()) {
                        resetFicheContenu();
                    }
                }
                succes();
                update(entree ? "data_contenu_entree_stock" : "data_contenu_sortie_stock");
                update(entree ? "data_entree_stock" : "data_sortie_stock");
            }
        } else {
            openDialog("dlgConfirmDeleteArticles");
        }
    }

    public void updatePrix_(YvsComContenuDocStock y) {
        selectContenu = y;
        openDialog("dlgUpdatePrix");
        update("txt_prix_contenu_entree_stock");
    }

    public void deleteBeanContenu_(YvsComContenuDocStock y) {
        selectContenu = y;
    }

    public void deleteBeanContenu_() {
        try {
            if (selectContenu != null) {
                if (!controleDleteLine(selectContenu)) {
                    return;
                }
                selectContenu.setAuthor(currentUser);
                dao.delete(selectContenu);
                docStock.getContenus().remove(selectContenu);
                succes();
                resetFicheContenu();
                update(entree ? "data_contenu_entree_stock" : "data_contenu_sortie_stock");
                update(entree ? "data_entree_stock" : "data_sortie_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void openDlgToConfirmSuspend(YvsComDocStocks y) {
        y.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{y}));
        selectDoc = y;
        populateView(UtilCom.buildBeanDocStock(y));
    }

    @Override
    public void onSelectObject(YvsComDocStocks y) {
        selectDoc = y;
        if (entree) {
            selectEntree = selectDoc;
        } else {
            selectSortie = selectDoc;
        }
        _loadOnView(selectDoc);
        loadHistorique(true, true);
        update("blog_form_entree_stock");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (selectDoc == null && ev != null) {
            selectDoc = (YvsComDocStocks) ev.getObject();
            tabIds = "" + documents.indexOf(selectDoc);

        }
        onSelectObject(selectDoc);
        tabIds = "" + documents.indexOf(selectDoc);
    }

    public void loadOnViewContent(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocStock y = (YvsComContenuDocStock) ev.getObject();
            onSelectObject(y.getDocStock());
        }
    }

    public void _loadOnView(YvsComDocStocks y) {
        y.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidDocStock.findByFacture", new String[]{"facture"}, new Object[]{y}));
        y.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{y}));
        populateView(UtilCom.buildBeanDocStock(y));
        if (y != null) {
            if (!tranches.contains(y.getCreneauDestinataire())) {
                tranches.add(y.getCreneauDestinataire());
            }
        }
        ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (m != null) {
            if (entree) {
                operations = y.getDestination().getOperations();
                if (!m.getDepots().contains(y.getDestination())) {
                    m.getDepots().add(0, y.getDestination());
                    update("select_depot_entree_stock");
                }
            } else {
                operations = y.getSource().getOperations();
                if (!m.getDepots().contains(y.getSource())) {
                    m.getDepots().add(0, y.getSource());
                    update("select_depot_sortie_stock");
                }
            }
        }
        addParamCreneauH();
        addParamDepotH();
        resetFicheContenu();
        update(entree ? "blog_form_entree_stock" : "blog_form_sortie_stock");
    }

    public void loadOnView_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectDoc = (YvsComDocStocks) ev.getObject();
            _loadOnView(selectDoc);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update(entree ? "blog_form_entree_stock" : "blog_form_sortie_stock");
    }

    public void unLoadOnView_(UnselectEvent ev) {
        resetFiche_();
        update(entree ? "blog_form_entree_stock" : "blog_form_sortie_stock");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleDepot bean = (YvsBaseArticleDepot) ev.getObject();
            Articles a = UtilProd.buildBeanArticles(bean.getArticle());
            a.setRequiereLot(bean.getRequiereLot());
            a.setSellWithOutStock(bean.getSellWithoutStock());
            chooseArticle(a);
            listArt = false;
            update(entree ? "form_contenu_entree_stock" : "form_contenu_sortie_stock");
            update(entree ? "desc_artcile_entree_stock" : "desc_artcile_sortie_stock");
        }
    }

    public void loadOnViewConditionnement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseConditionnement bean = (YvsBaseConditionnement) ev.getObject();
            chooseConditionnement(UtilProd.buildBeanConditionnement(bean));
            listArt = false;
            update(entree ? "form_contenu_entree_stock" : "form_contenu_sortie_stock");
            update(entree ? "desc_artcile_entree_stock" : "desc_artcile_sortie_stock");
        }
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectContenu = (YvsComContenuDocStock) ev.getObject();
            populateViewContenu(UtilCom.buildBeanContenuDocStock(selectContenu));
            update(entree ? "form_contenu_entree_stock" : "form_contenu_sortie_stock");
            update(entree ? "desc_artcile_entree_stock" : "desc_artcile_sortie_stock");
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheContenu();
        update(entree ? "form_contenu_entree_stock" : "form_contenu_sortie_stock");
    }

    public void chooseDate() {
        if (entree) {
            loadAllTranche(docStock.getDestination());
        } else {
            loadAllTranche(docStock.getSource());
        }
    }

    public void chooseLot(boolean entree) {
        if ((contenu.getLotSortie() != null) ? contenu.getLotSortie().getId() > 0 : false) {
            ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
            if (m != null) {
                int idx = m.getLots().indexOf(new YvsComLotReception(contenu.getLotSortie().getId()));
                if (idx > -1) {
                    contenu.setLotSortie(UtilCom.buildBeanLotReception(m.getLots().get(idx)));
                }
            }
        }
    }

    public void chooseDepot() {
        docStock.setDestination(new Depots());
        docStock.setSource(new Depots());
        docStock.setCreneauDestinataire(new Creneau());
        docStock.setCreneauSource(new Creneau());
        tranche = 0; 
        if (entree) {
            docStock.setMouvement(Constantes.MOUV_ENTREE);
            docStock.setDestination(new Depots(depot));
            chooseDepotDestinataire();
        } else {
            docStock.setMouvement(Constantes.MOUV_SORTIE);
            docStock.setSource(new Depots(depot));
            chooseDepotSource();
        }
        if (depot > 0) {
            addParamDepotH();
//            chooseNature();
        } else {
            tranches.clear();
            historiques.clear();
        }
        update(entree ? "data_operation_depot_entree" : "data_operation_depot_sortie");
    }

    public void chooseDepotSource() {
        operations.clear();
        if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                YvsBaseDepots y = m.getDepots().get(m.getDepots().indexOf(new YvsBaseDepots(docStock.getSource().getId())));
                Depots d = UtilCom.buildSimpleBeanDepot(y);
                cloneObject(docStock.getSource(), d);
                ManagedStockArticle s = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (s != null) {
                    s.setEntityDepot(y);
                    s.loadActifArticleByDepot(true, true);
                }
                loadAllTranche(d);
                operations = y.getOperations();
            }
        } else {
            docStock.setSource(new Depots());
        }
    }

    public void chooseDepotDestinataire() {
        operations.clear();
        if ((docStock.getDestination() != null) ? docStock.getDestination().getId() > 0 : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                YvsBaseDepots y = m.getDepots().get(m.getDepots().indexOf(new YvsBaseDepots(docStock.getDestination().getId())));
                Depots d = UtilCom.buildSimpleBeanDepot(y);
                cloneObject(docStock.getDestination(), d);
                ManagedStockArticle s = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (s != null) {
                    s.setEntityDepot(y);
                    s.loadActifArticleByDepot(true, true);
                }
                loadAllTranche(d);
                operations = y.getOperations();
            }
        } else {
            docStock.setDestination(new Depots());
        }
    }

    public void chooseCreneau() {
        docStock.setCreneauDestinataire(new Creneau());
        docStock.setCreneauSource(new Creneau());
        if (entree) {
            docStock.setCreneauDestinataire(new Creneau(tranche));
            chooseCreneauDestinataire();
        } else {
            docStock.setCreneauSource(new Creneau(tranche));
            chooseCreneauSource();
        }
        addParamCreneauH();
    }

    public void chooseCreneauSource() {
        if ((docStock.getCreneauSource() != null) ? docStock.getCreneauSource().getId() > 0 : false) {
            YvsComCreneauDepot c_ = tranches.get(tranches.indexOf(new YvsComCreneauDepot(docStock.getCreneauSource().getId())));
            Creneau c = UtilCom.buildBeanCreneau(c_);
            cloneObject(docStock.getCreneauSource(), c);
        }
    }

    public void chooseCreneauDestinataire() {
        if ((docStock.getCreneauDestinataire() != null) ? docStock.getCreneauDestinataire().getId() > 0 : false) {
            YvsComCreneauDepot c_ = tranches.get(tranches.indexOf(new YvsComCreneauDepot(docStock.getCreneauDestinataire().getId())));
            Creneau c = UtilCom.buildBeanCreneau(c_);
            cloneObject(docStock.getCreneauDestinataire(), c);
        }
    }

//    public void chooseNature() {
//        if (entree) {
//            verifyOperation(docStock.getDestination(), Constantes.ENTREE, docStock.getNature(), false);
//        } else {
//            verifyOperation(docStock.getSource(), Constantes.SORTIE, docStock.getNature(), false);
//        }
//    }
    public void chooseStatut(ValueChangeEvent ev) {
        String statut = ((String) ev.getNewValue());
        if (statut != null ? statut.trim().equals("Z") : false) {
            openDialog("dlgMoreStatuts");
        } else {
            statut_ = ((String) ev.getNewValue());
            addParamStatut();
        }
    }

    public void addParamStatut() {
        addParamStatut(true);
    }

    public void chooseStatuts() {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null);
        if (statuts != null ? !statuts.isEmpty() : false) {
            boolean add = true;
            for (String s : statuts) {
                if (s != null ? s.trim().length() < 1 : true) {
                    add = false;
                    break;
                }
            }
            if (add) {
                p = new ParametreRequete("y.statut", "statut", statuts, "IN", "AND");
            }
        }
        paginator.addParam(p);
        loadAllTransfert(true, true);
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
            loadAllTransfert(true, true);
        }
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllTransfert(true, true);
    }

    public void addParamToValide() {
        ParametreRequete p = new ParametreRequete("(y.etapeValide+1)", "etape", null, "IN", "AND");
        if (toValideLoad != null ? toValideLoad : false) {
            List<Integer> lnum = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepe", new String[]{"document", "niveau"}, new Object[]{Constantes.DOCUMENT_SORTIE, currentUser.getUsers().getNiveauAcces()});
            if ((lnum != null) ? !lnum.isEmpty() : false) {
                p = new ParametreRequete("(y.etapeValide+1)", "etape", lnum, "IN", "AND");
            }
        }
        paginator.addParam(p);
        loadAllTransfert(true, true);
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("y.cloturer", "cloturer", cloturer_);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadAllTransfert(true, true);
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
        loadAllTransfert(true, true);
    }

    public void chooseDepotSearch() {
        ParametreRequete p;
        if (depotSearch > 0) {
            p = new ParametreRequete(entree ? "y.destination" : "y.source", "depot", new YvsBaseDepots(depotSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete(entree ? "y.destination" : "y.source", "depot", null);
        }
        paginator.addParam(p);
        loadAllTransfert(true, true);
    }

    public void chooseAgenceSearch() {
        _loadDepot();
        addParamAgence();
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete(entree ? "y.destination.agence" : "y.source.agence", "agence", null);
        if (agence_ > 0) {
            p = new ParametreRequete(entree ? "y.destination.agence" : "y.source.agence", "agence", new YvsAgences(agence_), "=", "AND");
        }
        paginator.addParam(p);
        loadAllTransfert(true, true);
    }

    public void chooseConditionnement() {
        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
            if (contenu.getArticle() != null) {
                int idx = contenu.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(contenu.getConditionnement().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = contenu.getArticle().getConditionnements().get(idx);
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(y));
                    contenu.getArticle().setPua(contenu.getConditionnement().getPrixAchat());
                    contenu.getArticle().setPuv(contenu.getConditionnement().getPrix());
                }
            }
        }
        loadInfosArticle(contenu.getArticle());
    }

    public void loadInfosArticle(Articles art) {
        Double prix;
        if (entree) {
            art.setStock(dao.stocks(art.getId(), 0, docStock.getDestination().getId(), 0, 0, docStock.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLotSortie().getId()));
            prix = (Double) dao.loadObjectByNameQueries("YvsBaseMouvementStock.findLastPrixEntree", new String[]{"article", "unite", "depot"}, new Object[]{new YvsBaseArticles(art.getId()), new YvsBaseConditionnement(contenu.getConditionnement().getId()), new YvsBaseDepots(docStock.getSource().getId())});
            if (!checkOperationArticle(art.getId(), docStock.getDestination().getId(), Constantes.ENTREE)) {
                getWarningMessage("L'article '" + art.getDesignation() + "' ne fait pas d'entrée en stock dans le depot '" + docStock.getDestination().getDesignation() + "'");
            }
            prix = prix != null ? prix > 0 ? prix : art.getPua() : art.getPua();
        } else {
            art.setStock(dao.stocks(art.getId(), 0, docStock.getSource().getId(), 0, 0, docStock.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLotSortie().getId()));
            if (art.isRequiereLot() && (docStock.getSource() != null ? docStock.getSource().getId() > 0 : false)) {
                ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                if (w != null) {
                    contenu.setLots(w.loadList(docStock.getSource().getId(), contenu.getConditionnement().getId(), docStock.getDateDoc(), contenu.getQuantite(), art.getStock()));
                    if (contenu.getLots().size() == 1) {
                        contenu.setLotSortie(UtilCom.buildBeanLotReception(contenu.getLots().get(0)));
                    } else {
                        contenu.setLotSortie(new LotReception(0, contenu.getLots().size() + " Lots"));
                    }
                    update("data-contenu_ss_require_lot");
                }
            }
            prix = dao.getPr(art.getId(), docStock.getSource().getId(), 0, docStock.getDateDoc(), contenu.getConditionnement().getId());
            prix = prix > 0 ? prix : art.getPuv();
        }
        contenu.setPrix(prix);
        selectArt = true;
        cloneObject(contenu.getArticle(), art);
    }

    public void chooseArticle(Articles art) {
        if ((art != null) ? art.getId() > 0 : false) {
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(art.getId())});
            art.setConditionnements(unites);
            YvsBaseConditionnement cond = null;
            if (entree) {
                cond = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseArticleDepot.findDefaultCond", new String[]{"article", "depot"}, new Object[]{new YvsBaseArticles(art.getId()), new YvsBaseDepots(docStock.getDestination().getId())});
            } else {
                cond = (YvsBaseConditionnement) dao.loadOneByNameQueries("YvsBaseArticleDepot.findDefaultCond", new String[]{"article", "depot"}, new Object[]{new YvsBaseArticles(art.getId()), new YvsBaseDepots(docStock.getSource().getId())});
            }
            if (cond != null) {
                contenu.setConditionnement(UtilProd.buildBeanConditionnement(cond));
            } else if (art.getConditionnements() != null ? (!art.getConditionnements().isEmpty()) : false) {
                contenu.setConditionnement(UtilProd.buildBeanConditionnement(art.getConditionnements().get(0)));
            }
            loadInfosArticle(art);
        } else {
            contenu.getArticle().setError(true);
        }
    }

    private void chooseConditionnement(Conditionnement c) {
        if (c != null ? c.getId() > 0 : false) {
            chooseArticle(c.getArticle());
            cloneObject(contenu.getConditionnement(), c);
            chooseConditionnement();
        }
    }

    public void calculePrixTotal() {
        calculePrixTotal(contenu);
    }

    public void calculePrixTotal(ContenuDocStock contenu) {
        contenu.setPrixTotal(contenu.getQuantite() * contenu.getPrix());
    }

    public void searchArticle() {
        String num = contenu.getArticle().getRefArt();
        contenu.getArticle().setDesignation("");
        contenu.getArticle().setError(true);
        contenu.getArticle().setId(0);
        listArt = false;
        selectArt = false;
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.setEntityDepot(new YvsBaseDepots(entree ? docStock.getDestination().getId() : docStock.getSource().getId()));
            Articles y = m.searchArticleActifByDepot(num, true);
            listArt = y.isListArt();
            selectArt = y.isSelectArt();
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update(entree ? "data_articles_entree_stock" : "data_articles_sortie_stock");
                } else {
                    chooseArticle(y);
                }
                contenu.getArticle().setError(false);
            } else {
                Conditionnement c = m.searchArticleActifByCodeBarre(num);
                if (m.getConditionnements() != null ? !m.getConditionnements().isEmpty() : false) {
                    if (m.getConditionnements().size() > 1) {
                        update(entree ? "data_conditionnements_entree_stock" : "data_conditionnements_sortie_stock");
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
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.setEntityDepot(new YvsBaseDepots(entree ? docStock.getDestination().getId() : docStock.getSource().getId()));
            m.initArticlesByDepot(contenu.getArticle());
            listArt = contenu.getArticle().isListArt();
            selectArt = contenu.getArticle().isSelectArt();
        }
        update(entree ? "data_articles_entree_stock" : "data_articles_sortie_stock");
        update(entree ? "data_articles_entree_stock_" : "data_articles_sortie_stock_");
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

    public void editerOrderByForce() {
        editer(true);
    }

    public void editer() {
        editer(false);
    }

    public void editer(boolean force) {
        if (!autoriser(entree ? "gescom_editer_entree" : "gescom_editer_sortie")) {
            openNotAcces();
            return;
        }
        Long y_depot = (entree) ? selectDoc.getDestination().getId() : selectDoc.getSource().getId();
        Long y_tranche = (entree) ? selectDoc.getCreneauDestinataire().getTranche().getId() : selectDoc.getCreneauSource().getTranche().getId();
        if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
            boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
            if (!force) {
                exist_inventaire = !controleInventaire(y_depot, docStock.getDateDoc(), y_tranche, !gescom_update_stock_after_valide);
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
        if (selectDoc == null) {
            return;
        } else {
            if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getTypeDoc().equals(Constantes.TYPE_IN) : false) {
                getErrorMessage("La validation de ce document est lié  à celle l'inventaire " + selectDoc.getDocumentLie().getNumDoc() + " l'ayant généré");
                return;
            }
        }
        if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (entree) {
                if (!exist_inventaire) {
                    for (YvsComContenuDocStock c : docStock.getContenus()) {
                        String result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), selectDoc.getDestination().getId(), 0, c.getQuantite(), 0, "DELETE", "E", selectDoc.getDateDoc(), (c.getLotEntree() != null ? c.getLotEntree().getId() : 0));
                        if (result != null) {
                            getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou sera négatif à la date " + result);
                            return;
                        }
                    }
                }
            }
        }
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
            YvsComDocStocks inventaire = null;
            if (exist_inventaire) {
                inventaire = dao.lastInventaire(y_depot, selectDoc.getDateDoc(), y_tranche);
            }
            for (YvsComContenuDocStock c : docStock.getContenus()) {
                c.setStatut(Constantes.ETAT_EDITABLE);
                dao.update(c);
                if (inventaire != null ? inventaire.getId() > 0 : false) {
                    majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), (entree ? Constantes.MOUV_SORTIE : Constantes.MOUV_ENTREE));
                }
            }
            exist_inventaire = false;
        }
    }

    public void annuler(boolean force) {
        if (selectDoc == null) {
            return;
        }
        Long y_depot = (entree) ? selectDoc.getDestination().getId() : selectDoc.getSource().getId();
        Long y_tranche = (entree) ? selectDoc.getCreneauDestinataire().getTranche().getId() : selectDoc.getCreneauSource().getTranche().getId();
        if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
            boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
            if (!controleInventaire(y_depot, docStock.getDateDoc(), y_tranche)) {
                return;
            }
        }
        if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (entree) {
                String result = null;
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), docStock.getDestination().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", docStock.getDateDoc(), (c.getLotEntree() != null ? c.getLotEntree().getId() : 0));
                    if (result != null) {
                        getErrorMessage("Impossible de valider ce document", "la ligne d'article " + c.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + result);
                        return;
                    }
                }
            }
        }
        if (!entree) {
            cancelEtapeFacture(force, true);
        } else if (changeStatut(Constantes.ETAT_EDITABLE)) {
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

    public boolean validerOrderByForce() {
        return valider(true);
    }

    public void validerOrderAll() {
        try {
            if (!autoriser(entree ? "gescom_valide_entree" : "gescom_valide_sortie")) {
                openNotAcces();
                return;
            }
            if (currentParamStock == null) {
                currentParamStock = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
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
        if (currentParamStock == null) {
            currentParamStock = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        if (!entity.getStatut().equals(Constantes.ETAT_VALIDE)) {
            entity.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{entity}));
            DocStock bean = UtilCom.buildBeanDocStock(entity);
            entity.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidDocStock.findByFacture", new String[]{"facture"}, new Object[]{entity}));
            bean.setEtapesValidations(ordonneEtapes(entity.getEtapesValidations()));
            boolean response = false;
            if (bean.getEtapesValidations() != null ? bean.getEtapesValidations().isEmpty() : true) {
                response = valider(bean, entity, false, false);
            } else {
                for (YvsWorkflowValidDocStock r : bean.getEtapesValidations()) {
                    if (!r.getEtapeValid()) {
                        etape = r;
                        break;
                    }
                }
                if (etape != null ? etape.getId() > 0 : false) {
                    response = validEtapeFacture(bean, entity, etape, false);
                } else {
                    response = valider(bean, entity, false, false, controle);
                }
            }
            if (response && succes) {
                succes();
            }
        }
        return false;
    }

    public boolean valider() {
        return valider(false);
    }

    public boolean valider(boolean force) {
        return valider(docStock, selectDoc, force, true);
    }

    public boolean valider(DocStock docStock, YvsComDocStocks selectDoc, boolean force, boolean succes) {
        return valider(docStock, selectDoc, force, succes, true);
    }

    public boolean valider(DocStock docStock, YvsComDocStocks selectDoc, boolean force, boolean succes, boolean controle) {
        if (!autoriser(entree ? "gescom_valide_entree" : "gescom_valide_sortie")) {
            openNotAcces();
            return false;
        }
        if (selectDoc == null) {
            return false;
        } else {
            if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getTypeDoc().equals(Constantes.TYPE_IN) : false) {
                getErrorMessage("La validation de ce document est lié  à celle l'inventaire " + selectDoc.getDocumentLie().getNumDoc() + " l'ayant généré");
                return false;
            }
        }
        if (docStock.getContenus().isEmpty()) {
            getErrorMessage("Le document de sortie de stocks n'a pas de contenu!", " veuillez enregistrer des lignes de contenue");
            return false;
        }
        // Vérifié qu'aucun document d'inventaire n'exite après cette date
        boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
        Long y_depot = (entree) ? docStock.getDestination().getId() : docStock.getSource().getId();
        Long y_tranche = (entree) ? docStock.getCreneauDestinataire().getTranche().getId() : docStock.getCreneauSource().getTranche().getId();
        if (!force) {
            exist_inventaire = !controleInventaire(y_depot, docStock.getDateDoc(), y_tranche, !gescom_update_stock_after_valide);
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
        if (!entree) {
            if (!verifyOperation(docStock.getSource(), Constantes.SORTIE, docStock.getNature(), true)) {
                return false;
            }
            if (!exist_inventaire) {
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
                }
            }
        } else {
            if (!verifyOperation(docStock.getDestination(), Constantes.ENTREE, docStock.getNature(), true)) {
                return false;
            }
            for (YvsComContenuDocStock c : docStock.getContenus()) {
                if (!checkOperationArticle(c.getArticle().getId(), docStock.getDestination().getId(), Constantes.ENTREE)) {
                    getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' ne fait pas d'entrée en stock dans le depot '" + docStock.getDestination().getDesignation() + "'");
                    return false;
                }
            }
        }
        YvsComDocStocks inventaire = null;
        if (exist_inventaire) {
            inventaire = dao.lastInventaire(y_depot, docStock.getDateDoc(), y_tranche);
        }
        if (exist_inventaire ? inventaire != null ? inventaire.getId() > 0 : false : true) {
            if (changeStatut_(Constantes.ETAT_VALIDE, docStock, selectDoc)) {
                selectDoc.setCloturer(false);
                selectDoc.setAnnulerBy(null);
                selectDoc.setValiderBy(currentUser.getUsers());
                selectDoc.setCloturerBy(null);
                selectDoc.setDateAnnuler(null);
                selectDoc.setDateCloturer(null);
                selectDoc.setDateValider(new Date());
                selectDoc.setStatut(Constantes.ETAT_VALIDE);
                selectDoc.setAuthor(currentUser);
                dao.update(selectDoc);
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    c.setStatut(Constantes.ETAT_VALIDE);
                    dao.update(c);
                    if (inventaire != null ? inventaire.getId() > 0 : false) {
                        majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), (entree ? Constantes.MOUV_ENTREE : Constantes.MOUV_SORTIE));
                    }
                }
                exist_inventaire = false;
                update(entree ? "data_contenu_entree_stock" : "data_contenu_sortie_stock");
                if (succes) {
                    succes();
                }
                return true;
            }
        }
        return false;
    }

    public void cloturer(YvsComDocStocks y) {
        selectDoc = y;
        update(entree ? "id_confirm_close_es" : "id_confirm_close_ss");
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        selectDoc.setCloturer(!selectDoc.getCloturer());
        selectDoc.setDateCloturer(selectDoc.getCloturer() ? new Date() : null);
        selectDoc.setCloturerBy(selectDoc.getCloturer() ? currentUser.getUsers() : null);
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        documents.set(documents.indexOf(selectDoc), selectDoc);
        if (historiques.contains(selectDoc)) {
            historiques.set(historiques.indexOf(selectDoc), selectDoc);
        }
        update(entree ? "data_entree_stock" : "data_sortie_stock");
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
        if (!etat.equals("") && selectDoc != null) {
            MyLog.Write(getClass(), "changeStatut", true, true);
            if (docStock.isCloturer()) {
                getErrorMessage("Ce document est verrouillé");
                return false;
            }
            docStock.setStatut(etat);
            selectDoc.setStatut(etat);
            if (documents.contains(selectDoc)) {
                documents.set(documents.indexOf(selectDoc), selectDoc);
            }
            if (!etat.equals(Constantes.ETAT_EDITABLE)) {
                if (historiques.contains(selectDoc)) {
                    historiques.remove(historiques.indexOf(selectDoc));
                }
            } else {
                if (!historiques.contains(selectDoc)) {
                    historiques.add(0, selectDoc);
                }
            }
            if (etat.equals(Constantes.ETAT_VALIDE)) {
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    c.setStatut(Constantes.ETAT_VALIDE);
                    c.setAuthor(currentUser);
                    dao.update(c);
                }
            } else {
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    c.setStatut(Constantes.ETAT_EDITABLE);
                    c.setAuthor(currentUser);
                    dao.update(c);
                }
            }
            update(entree ? "data_contenu_entree_stock" : "data_contenu_sortie_stock");
            update(entree ? "data_entree_stock" : "data_sortie_stock");
            update(entree ? "form_entree_stock" : "form_sortie_stock");
            MyLog.Write(getClass(), "changeStatut", true, false);
            return true;
        } else {
            getErrorMessage("Vous devez selectionner un document");
        }
        return false;
    }

    public void changeStatutLine(YvsComContenuDocStock y) {
        if (y != null ? y.getId() > 0 : false) {
            if (selectDoc != null ? selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getTypeDoc().equals(Constantes.TYPE_IN) : false : false) {
                getErrorMessage("La validation de ce document est lié  à celle l'inventaire " + selectDoc.getDocumentLie().getNumDoc() + " l'ayant généré");
                return;
            }
            if (y.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                if (!docStock.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    getErrorMessage("Impossible de valider cette ligne car le document n'est pas valide");
                    return;
                }
            } else {
                if (!autoriser(entree ? "gescom_editer_entree" : "gescom_editer_sortie")) {
                    openNotAcces();
                    return;
                }
                //Contrôle inventaire
                YvsComCreneauDepot cr;
                if (entree) {
                    cr = (YvsComCreneauDepot) dao.loadOneByNameQueries("YvsComCreneauDepot.findById", new String[]{"id"}, new Object[]{y.getDocStock().getCreneauDestinataire().getId()});
                    if (!controleInventaire(cr.getDepot().getId(), docStock.getDateDoc(), cr.getTranche().getId())) {
                        return;
                    }
                } else {
                    cr = (YvsComCreneauDepot) dao.loadOneByNameQueries("YvsComCreneauDepot.findById", new String[]{"id"}, new Object[]{y.getDocStock().getCreneauSource().getId()});
                    if (!controleInventaire(cr.getDepot().getId(), docStock.getDateReception(), cr.getTranche().getId())) {
                        return;
                    }
                }

            }
            y.setStatut(y.getStatut().equals(Constantes.ETAT_EDITABLE) ? Constantes.ETAT_VALIDE : Constantes.ETAT_EDITABLE);
            y.setDateUpdate(new Date());
            y.setAuthor(currentUser);
            dao.update(y);
            succes();
        }
    }

    public void changeBox() {
        box = !box;
    }

    public void searchByNum() {
        ParametreRequete p;
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch_ + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numDoc)", "reference", numSearch_.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numPiece)", "reference", numSearch_.toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p = new ParametreRequete("y.numDoc", "numDoc", null);
        }
        paginator.addParam(p);
        loadAllTransfert(true, true);
        update(entree ? "data_entree_stock" : "data_sortie_stock");
    }

    public void removeDoublon(YvsComDocStocks y) {
        selectDoc = y;
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
            selectDoc = new YvsComDocStocks();
        }
    }

    public void addParamDepotH() {
//        if (entree) {
//            ParametreRequete p;
//            if ((docStock.getDestination() != null) ? docStock.getDestination().getId() > 0 : false) {
//                p = new ParametreRequete("y.destination", "destination", new YvsBaseDepots(docStock.getDestination().getId()), "=", "AND");
//                p_historique.addParam(new ParametreRequete("y.source", "source", null, "=", "AND"));
//            } else {
//                p = new ParametreRequete("y.destination", "destination", null);
//            }
//            p_historique.addParam(p);
//        } else {
//            ParametreRequete p;
//            if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
//                p = new ParametreRequete("y.source", "source", new YvsBaseDepots(docStock.getSource().getId()), "=", "AND");
//                p_historique.addParam(new ParametreRequete("y.destination", "destination", null, "=", "AND"));
//            } else {
//                p = new ParametreRequete("y.source", "source", null);
//            }
//            p_historique.addParam(p);
//        }
//        loadHistorique(true, true);
    }

    public void addParamCreneauH() {
//        if (entree) {
//            ParametreRequete p;
//            if ((docStock.getCreneauDestinataire() != null) ? docStock.getCreneauDestinataire().getId() > 0 : false) {
//                p = new ParametreRequete("y.creneauDestinataire", "creneauDestinataire", new YvsComCreneauDepot(docStock.getCreneauDestinataire().getId()));
//                p.setOperation("=");
//                p.setPredicat("AND");
//            } else {
//                p = new ParametreRequete("y.creneauDestinataire", "creneauDestinataire", null);
//            }
//            p_historique.addParam(p);
//        } else {
//            ParametreRequete p;
//            if ((docStock.getCreneauSource() != null) ? docStock.getCreneauSource().getId() > 0 : false) {
//                p = new ParametreRequete("y.creneauSource", "creneauSource", new YvsComCreneauDepot(docStock.getCreneauSource().getId()));
//                p.setOperation("=");
//                p.setPredicat("AND");
//            } else {
//                p = new ParametreRequete("y.creneauSource", "creneauSource", null);
//            }
//            p_historique.addParam(p);
//        }
//        loadHistorique(true, true);
    }

    @Override
    public void cleanStock() {
        super.cleanStock();
        loadAllTransfert(true, true);
    }

    public void clearParams() {
        date_ = toValideLoad = false;
        cloturer_ = null;
        dateDebut_ = dateFin_ = null;
        depotSearch = -1;
        statut_ = numSearch_ = null;
        paginator.getParams().clear();
//        loadAllTransfert(true, true);
        addParamAgence();
    }

    @Override
    public void updateBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void print(YvsComDocStocks y, boolean entree) {
        print(y, withHeader, entree);
    }

    public void print(YvsComDocStocks y, boolean withHeader, boolean entree) {
        try {
            if (currentParamStock != null ? currentParamStock.getId() < 1 : true) {
                currentParamStock = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (y != null ? y.getId() > 0 : false) {
                if (currentParamStock != null ? (currentParamStock.getPrintDocumentWhenValide() && !y.getStatut().equals(Constantes.ETAT_VALIDE)) : false) {
                    getErrorMessage("Le document doit être validé pour pouvoir être téléchargé");
                    return;
                }
                Map<String, Object> param = new HashMap<>();
                param.put("ID", y.getId().intValue());
                if (entree) {
                    param.put("VALEUR_BY", valeurBy);
                }
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(withHeader));
                String report = entree ? "fiche_entree" : "fiche_sortie";
                if (currentParam != null ? currentParam.getUseLotReception() : false) {
                    report = entree ? "fiche_entree_by_lot" : "fiche_sortie_by_lot";
                }
                executeReport(report, param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedOtherTransfert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printListing() {
        printListing(depotContenu, 0, dateDebutContenu, dateFinContenu, entree);
    }

    public void printListing(long depot, long tranche, Date dateDebut, Date dateFin, boolean entree) {
        if (depot < 1) {
            getErrorMessage("Vous devez selectionner un dépôt");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("DEPOT", (int) depot);
        param.put("TRANCHE", (int) tranche);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        String report = "listing_sortie";
        if (entree) {
            report = "listing_entree";
        }
        executeReport(report, param);
    }

    public void printListing(long depot, long tranche, Date dateDebut, Date dateFin, boolean entree, String format) {
        if (depot < 1) {
            getErrorMessage("Vous devez selectionner un dépôt");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("DEPOT", (int) depot);
        param.put("TRANCHE", (int) tranche);
        param.put("DATE_DEBUT", dateDebut);
        param.put("DATE_FIN", dateFin);
        param.put("SUBREPORT_DIR", SUBREPORT_DIR());
        String report = "listing_sortie_header";
        if (entree) {
            report = "listing_entree_no_header";
        }
        executeReport(report, param, "", format, false);
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
        ParametreRequete p = new ParametreRequete(entree ? "y.docStock.destination.agence" : "y.docStock.source.agence", "agence", null);
        if (agenceContenu > 0) {
            p = new ParametreRequete(entree ? "y.docStock.destination.agence" : "y.docStock.source.agence", "agence", new YvsAgences(agenceContenu), "=", "AND");
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamDepotSearch() {
        ParametreRequete p = new ParametreRequete(entree ? "y.docStock.destination" : "y.docStock.source", "depot", null);
        if (depotContenu > 0) {
            System.err.println("depotContenu " + depotContenu);
            p = new ParametreRequete(entree ? "y.docStock.destination" : "y.docStock.source", "depot", new YvsBaseDepots(depotContenu), "=", "AND");
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
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", article.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", article.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
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
            String num = genererReference(giveNameType(type), y.getDateDoc(), (entree ? (y.getDestination() != null ? y.getDestination().getId() : 0) : (y.getSource() != null ? y.getSource().getId() : 0)), Constantes.DEPOT);;
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
    /*
     DEBUT WORKFLOW
     */
    private YvsWorkflowValidDocStock currentEtape;

    private List<YvsWorkflowEtapeValidation> saveEtapesValidation(YvsComDocStocks m) {
        if (m != null ? m.getNatureDoc() != null ? m.getNatureDoc().getId() > 0 : false : false) {
            champ = new String[]{"titre", "nature", "societe"};
            val = new Object[]{Constantes.DOCUMENT_SORTIE, m.getNatureDoc(), currentAgence.getSociete()};
            return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByModelNatureDocSortie", champ, val);
        }
        return new ArrayList<>();
    }

    private List<YvsWorkflowValidDocStock> saveEtapesValidation(YvsComDocStocks m, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidDocStock> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidDocStock vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    champ = new String[]{"facture", "etape"};
                    val = new Object[]{m, et};
                    YvsWorkflowValidDocStock w = (YvsWorkflowValidDocStock) dao.loadOneByNameQueries("YvsWorkflowValidDocStock.findByEtapeFacture", champ, val);
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidDocStock();
                        vm.setAuthor(currentUser);
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setDocStock(m);
                        vm.setOrdreEtape(et.getOrdreEtape());
                        vm = (YvsWorkflowValidDocStock) dao.save1(vm);
                        re.add(vm);
                    }
                }
            }
        }
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidDocStock> ordonneEtapes(List<YvsWorkflowValidDocStock> l) {
        return YvsWorkflowValidDocStock.ordonneEtapes(l);
    }

    public boolean validEtapeFacture(YvsWorkflowValidDocStock etape, boolean lastEtape) {
        return validEtapeFacture(docStock, selectDoc, etape, lastEtape);
    }

    public boolean validEtapeFacture(DocStock docStock, YvsComDocStocks selectDoc, YvsWorkflowValidDocStock etape, boolean lastEtape) {
        //vérifier que la personne qui valide l'étape a le droit 
        if (!asDroitValideEtape(etape.getEtape())) {
            openNotAcces();
            return false;
        } else {
            if (!docStock.getContenus().isEmpty()) {
                //contrôle la cohérence des dates
                YvsComDocStocks current = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{docStock.getId()});
                int idx = docStock.getEtapesValidations().indexOf(etape);
                if (idx >= 0) {
                    //cas de la validation de la dernière étapes
                    if (etape.getEtapeSuivante() == null) {
                        if (valider()) {
                            etape.setAuthor(currentUser);
                            etape.setEtapeValid(true);
                            etape.setEtapeActive(false);
                            etape.setMotif(null);
                            etape.setDateUpdate(new Date());
                            if (docStock.getEtapesValidations().size() > (idx + 1)) {
                                docStock.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                            }
                            dao.update(etape);
                            selectDoc.setEtapeValide(current.getEtapeValide() + 1);
                            docStock.setStatut(Constantes.ETAT_VALIDE);
                            docStock.setEtapeValide(selectDoc.getEtapeValide());
                            dao.update(selectDoc);
                            if (documents.contains(selectDoc)) {
                                documents.get(documents.indexOf(selectDoc)).setEtapeValide(selectDoc.getEtapeValide());
                            }
                            return true;
                        }
                    } else {
                        etape.setAuthor(currentUser);
                        etape.setEtapeValid(true);
                        etape.setEtapeActive(false);
                        etape.setMotif(null);
                        etape.setDateUpdate(new Date());
                        if (docStock.getEtapesValidations().size() > (idx + 1)) {
                            docStock.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                        }
                        dao.update(etape);
                        current.setStatut(Constantes.ETAT_ENCOURS);
                        current.setAuthor(currentUser);
                        current.setEtapeValide(current.getEtapeValide() + 1);
                        dao.update(current);
                        docStock.setStatut(Constantes.ETAT_ENCOURS);
                        docStock.setEtapeValide(current.getEtapeValide());
                        if (documents.contains(current)) {
                            int idx_ = documents.indexOf(current);
                            documents.get(idx_).setEtapeValide(current.getEtapeValide());
                            documents.get(idx_).setStatut(current.getStatut());
                        }
                        getInfoMessage("Validation effectué avec succès !");
                        String result = null;
                        YvsComContenuDocStock cc = null;
                        for (YvsComContenuDocStock c : docStock.getContenus()) {
                            cc = c;
                            result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), docStock.getSource().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", docStock.getDateDoc(), (c.getLotSortie() != null ? c.getLotSortie().getId() : 0));
                            if (result != null) {
                                break;
                            }
                        }
                        if (result != null) {
                            getWarningMessage("", "la ligne d'article " + cc.getArticle().getDesignation() + " pourrait engendrer une incohérence dans le stock à la date du " + result);
                        }
                        return true;
                    }
                } else {
                    getErrorMessage("Impossible de continuer !");
                }
            } else {
                getErrorMessage("Le document de sortie de stocks n'a pas de contenu!", " veuillez enregistrer des lignes de contenue");
            }
        }
        return false;
    }

    public void motifEtapeOrdre(String motifEtape, boolean lastEtape) {
        this.motifEtape = motifEtape;
        this.lastEtape = lastEtape;
    }

    public void annulEtapeFacture(YvsWorkflowValidDocStock etape, boolean lastEtape) {
        this.etape = etape;
        this.lastEtape = lastEtape;
        this.motifEtape = null;
    }

    public boolean annulEtapeOrdre() {
        return annulEtapeFacture(selectSortie, docStock, currentUser, etape, lastEtape, motifEtape);
    }

    public boolean annulEtapeFacture(YvsComDocStocks current, DocStock stock, YvsUsersAgence users, YvsWorkflowValidDocStock etape, boolean lastEtape, String motif) {
        if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                getErrorMessage("Vous devez précisez le motif");
                return false;
            }
            if (current != null ? (current.getId() != null ? current.getId() < 1 : true) : true) {
                current = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findById", new String[]{"id"}, new Object[]{stock.getId()});
            }
            if (stock != null ? stock.getId() < 1 : true) {
                stock = UtilCom.buildSimpleBeanDocStock(current);
            }
            int idx = current.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtapeSuivante().getEtape(), current};
                    YvsWorkflowValidDocStock y = (YvsWorkflowValidDocStock) dao.loadOneByNameQueries("YvsWorkflowValidDocStock.findByEtapeFacture", champ, val);
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
                current.setStatut(current.getEtapeValide() < 1 ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ENCOURS);
                dao.update(current);

                stock.setStatut(current.getStatut());
                stock.setEtapeValide(current.getEtapeValide());
                if (documents != null ? documents.contains(current) : false) {
                    int idx_ = documents.indexOf(current);
                    documents.get(idx_).setEtapeValide(current.getEtapeValide());
                    documents.get(idx_).setStatut(current.getStatut());
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
    boolean suspend;

    public void cancelOrderByForce() {
        cancelEtapeFacture(true, suspend);
    }

    public void cancelEtapeFacture(boolean force, boolean suspend) {
        this.suspend = suspend;
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            List<YvsComDocStocks> l = dao.loadNameQueries("YvsComDocStocks.findByDocumentLier", new String[]{"document"}, new Object[]{selectDoc});
            if (l != null ? l.isEmpty() : true) {
                //annule toute les validation acquise
                int i = 0;
                boolean update = force;
                if (!force && !exist_inventaire) {
                    for (YvsWorkflowValidDocStock vm : docStock.getEtapesValidations()) {
                        //si on trouve une étape non valide (on ne peut annuler un ordre de docAchat complètement valide)
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
                    for (YvsWorkflowValidDocStock vm : docStock.getEtapesValidations()) {
                        vm.setEtapeActive(false);
                        if (i == 0) {
                            vm.setEtapeActive(true);
                        }
                        vm.setAuthor(currentUser);
                        vm.setEtapeValid(false);
                        dao.update(vm);
                        i++;
                    }
                } else if (!docStock.getEtapesValidations().isEmpty()) {
                    openDialog("dlgConfirmRefuserForcer");
                    return;
                }
                // Vérifié qu'aucun document d'inventaire n'exite après cette date
                boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
                Long y_depot = (entree) ? docStock.getDestination().getId() : docStock.getSource().getId();
                Long y_tranche = (entree) ? docStock.getCreneauDestinataire().getTranche().getId() : docStock.getCreneauSource().getTranche().getId();
                if (!force && !exist_inventaire) {
                    exist_inventaire = !controleInventaire(y_depot, docStock.getDateDoc(), y_tranche, !gescom_update_stock_after_valide);
                    if (exist_inventaire) {
                        if (!gescom_update_stock_after_valide) {
                            return;
                        } else if (!force) {
                            openDialog("dlgConfirmChangeInventaireByCancel");
                            return;
                        }
                    }
                }
                //désactive la docAchat
                if (changeStatut((suspend) ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE)) {
                    selectDoc.setCloturer(false);
                    selectDoc.setAnnulerBy(currentUser.getUsers());
                    selectDoc.setValiderBy(null);
                    selectDoc.setDateAnnuler(new Date());
                    selectDoc.setDateCloturer(null);
                    selectDoc.setDateValider(null);
                    selectDoc.setCloturerBy(null);
                    selectDoc.setStatut((suspend) ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE);
                    selectDoc.setAuthor(currentUser);
                    selectDoc.setEtapeValide(0);
                    docStock.setEtapeValide(0);
                    dao.update(selectDoc);
                    YvsComDocStocks inventaire = null;
                    if (exist_inventaire) {
                        inventaire = dao.lastInventaire(y_depot, selectDoc.getDateDoc(), y_tranche);
                    }
                    for (YvsComContenuDocStock c : docStock.getContenus()) {
                        c.setAuthor(currentUser);
                        c.setStatut(Constantes.ETAT_EDITABLE);
                        if (selectDoc.getContenus().contains(c)) {
                            selectDoc.getContenus().set(selectDoc.getContenus().indexOf(c), c);
                        }
                        dao.update(c);
                        if (inventaire != null ? inventaire.getId() > 0 : false) {
                            majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), (entree ? Constantes.MOUV_SORTIE : Constantes.MOUV_ENTREE));
                        }
                    }
                    exist_inventaire = false;
                }
            } else {
                for (YvsComDocStocks d : l) {
                    if (d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        getErrorMessage("Impossible d'annuler cett sortie car elle possède des éléments déja valide");
                        return;
                    }
                }
                openDialog("dlgConfirmRefuser");
            }
        }
    }
    /*
     FIN WORKFLOW
     */

    public void savenewNatureDoc() {
        YvsComNatureDoc entity = new YvsComNatureDoc(nature.getId());
        entity.setNature(nature.getNature());
        entity.setDescription(nature.getDescription());
        entity.setAuthor(currentUser);
        entity.setActif(nature.isActif());
        entity.setDateSave(new Date());
        entity.setDateUpdate(new Date());
        entity.setSociete(currentAgence.getSociete());
        if (nature.getId() <= 0) {
            entity = (YvsComNatureDoc) dao.save1(entity);
            natures.add(0, entity);
        } else {
            int idx = natures.indexOf(entity);
            if (idx >= 0) {
                natures.set(idx, entity);
            }
        }
    }

    public void toogleActivNature(YvsComNatureDoc nat) {
        if (nat != null) {
            nat.setActif(!nat.getActif());
            nat.setDateUpdate(new Date());
            nat.setAuthor(currentUser);
            dao.update(nat);
        }
    }

    public void deleteNatureDoc(YvsComNatureDoc nat) {
        if (nat != null) {
            nat.setDateUpdate(new Date());
            nat.setAuthor(currentUser);
            dao.delete(nat);
            natures.remove(nat);
        }
    }

    public void displayAllNatureDoc() {
        natures = dao.loadNameQueries("YvsComNatureDoc.findAlls", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void openDlEditNatureDoc(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id == -1) {
                openDialog("dlgNatures");
            }
        }
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllTransfert(true, true);
    }

    public void findLotReception(ContenuDocStock c) {
        try {
            if (c != null ? c.getLotSortie() != null : false) {
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
                        c.getLotSortie().setId(0);
                        ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                        if (m != null) {
                            m.resetFiche();
                            m.getLotReception().setNumero(c.getLotSortie().getNumero());
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

    public void saveLotReception() {
        try {
            ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
            if (m != null) {
                YvsComLotReception y = m._saveNew(m.getLotReception());
                if (y != null ? y.getId() > 0 : false) {
                    contenu.setLotSortie(UtilCom.buildBeanLotReception(y));
                    update("select_lot_entree_stock");
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
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
                update("data-contenu_ss_require_lot");
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
                update("data-contenu_ss_require_lot");
                return;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onBlurQuantiteeLot", ex);
        }
    }

    public void displayContent(YvsComDocStocks y) {
        y.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{y}));
        update("dt_row_ex_" + y.getId());
    }
}
