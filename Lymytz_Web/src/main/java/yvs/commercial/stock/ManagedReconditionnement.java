/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.production.UtilProd;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.UtilCom;
import yvs.commercial.depot.ManagedDepot;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseTableConversion;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.parametrage.entrepot.Depots;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;
import yvs.util.Utilitaire;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedReconditionnement extends ManagedCommercial<DocStock, YvsComDocStocks> implements Serializable {

    private DocStock docStock = new DocStock();
    private List<YvsComDocStocks> documents, historiques;
    private YvsComDocStocks selectDoc = new YvsComDocStocks();
    private ContenuDocStock contenu = new ContenuDocStock();
    private List<YvsComCreneauDepot> tranches;
    private String tabIds, egaliteStatut = "!=";

    private YvsComParametreStock currentParamStock;

    private List<YvsComContenuDocStock> all_contenus;
    public PaginatorResult<YvsComContenuDocStock> p_contenu = new PaginatorResult<>();

    //Parametre recherche contenu
    private boolean dateContenu = false;
    private long sourceContenu, destContenu, agenceContenu = -1;
    private Date dateDebutContenu = new Date(), dateFinContenu = new Date();
    private String statutContenu, reference, article, articleContenu;

    public ManagedReconditionnement() {
        documents = new ArrayList<>();
        historiques = new ArrayList<>();
        tranches = new ArrayList<>();
        all_contenus = new ArrayList<>();
    }

    public long getAgenceContenu() {
        return agenceContenu;
    }

    public void setAgenceContenu(long agenceContenu) {
        this.agenceContenu = agenceContenu;
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

    public String getArticleContenu() {
        return articleContenu;
    }

    public void setArticleContenu(String articleContenu) {
        this.articleContenu = articleContenu;
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

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public List<YvsComCreneauDepot> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsComCreneauDepot> tranches) {
        this.tranches = tranches;
    }

    public List<YvsComDocStocks> getHistoriques() {
        return historiques;
    }

    public void setHistoriques(List<YvsComDocStocks> historiques) {
        this.historiques = historiques;
    }

    public YvsComDocStocks getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocStocks selectDoc) {
        this.selectDoc = selectDoc;
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

    @Override
    public void loadAll() {
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        if (currentParamStock == null) {
            currentParamStock = (YvsComParametreStock) dao.loadOneByNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        if (agence_ < 1) {
            agence_ = currentAgence.getId();
        }
        if (agenceContenu < 1) {
            agenceContenu = currentAgence.getId();
        }
        docStock.setTypeDoc(Constantes.TYPE_RE);
        docStock.setEditeur(UtilUsers.buildBeanUsers(currentUser.getUsers()));
        docStock.setNature(Constantes.OP_RECONDITIONNEMENT);
        if ((docStock != null) ? docStock.getId() < 1 : true) {
            docStock.setMajPr(currentParamStock != null ? currentParamStock.getCalculPr() : true);
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
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAll(true, true);
    }

    public void loadContenus(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_IN)) {
            case 1:
                p = new ParametreRequete("y.docStock.source.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                break;
            case 2:
                controlListAgence();
                p = new ParametreRequete("y.docStock.source.agence.id", "agences", listIdAgences, "IN", "AND");
                break;
            case 3:
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdSourceByUsers", new String[]{"users", "date"}, new Object[]{currentUser.getUsers(), (Utilitaire.getIniTializeDate(new Date()).getTime())});
                p = new ParametreRequete("y.docStock.source.id", "depots", ids, "IN", "AND");
                break;
            default:
                p = new ParametreRequete("y.docStock.source.responsable", "users", currentUser.getUsers().getEmploye(), "=", "AND");
                break;
        }
        paginator.addParam(p);
        p_contenu.addParam(new ParametreRequete("y.docStock.typeDoc", "typeDoc", Constantes.TYPE_RE, "=", "AND"));
        String orderBy = "y.docStock.dateDoc DESC, y.docStock.numDoc";
        all_contenus = p_contenu.executeDynamicQuery("YvsComContenuDocStock", orderBy, avance, init, dao);
        update("data_contenus_reconditionnement");
    }

    public void loadAll(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_IN)) {
            case 1:
                p = new ParametreRequete("y.source.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                break;
            case 2:
                controlListAgence();
                p = new ParametreRequete("y.source.agence.id", "agences", listIdAgences, "IN", "AND");
                break;
            case 3:
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdSourceByUsers", new String[]{"users", "date"}, new Object[]{currentUser.getUsers(), (Utilitaire.getIniTializeDate(new Date()).getTime())});
                p = new ParametreRequete("y.source.id", "depots", ids, "IN", "AND");
                break;
            default:
                p = new ParametreRequete("y.source.responsable", "users", currentUser.getUsers().getEmploye(), "=", "AND");
                break;
        }
        paginator.addParam(p);
        paginator.addParam(new ParametreRequete("y.source.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        paginator.addParam(new ParametreRequete("y.typeDoc", "type", Constantes.TYPE_RE, "=", "AND"));
        documents = paginator.executeDynamicQuery("YvsComDocStocks", "y.dateDoc DESC, y.numDoc", avance, init, (int) imax, dao);
    }

    public void loadHistoriques(YvsBaseDepots y) {
        historiques.clear();
        if (y != null ? y.getId() > 0 : false) {
            champ = new String[]{"depot", "statut", "typeDoc"};
            val = new Object[]{y, Constantes.ETAT_EDITABLE, Constantes.TYPE_RE};
            nameQueri = "YvsComDocStocks.findBySourceStatut";
            historiques = dao.loadNameQueries(nameQueri, champ, val, 0, 5);
        }
        update("historique_transformation");
    }

    public void loadAllTranche(YvsBaseDepots depot) {
        tranches.clear();
        champ = new String[]{"permanent", "depot"};
        val = new Object[]{true, depot};
        List<YvsComCreneauDepot> lc = dao.loadNameQueries("YvsComCreneauDepot.findByDepotPermanent", champ, val);
        for (YvsComCreneauDepot c : lc) {
            if (!tranches.contains(c)) {
                tranches.add(c);
            }
        }

        String jour = Util.getDay(docStock.getDateDoc());
        champ = new String[]{"jour"};
        val = new Object[]{jour};
        List<YvsJoursOuvres> lj = dao.loadNameQueries("YvsJoursOuvres.findByJour", champ, val);
        for (YvsJoursOuvres j : lj) {
            champ = new String[]{"jour", "depot"};
            val = new Object[]{j, depot};
            lc = dao.loadNameQueries("YvsComCreneauDepot.findByJourDepot", champ, val);
            for (YvsComCreneauDepot c : lc) {
                if (!tranches.contains(c)) {
                    tranches.add(c);
                }
            }
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComDocStocks> re = paginator.parcoursDynamicData("YvsComDocStocks", "y", "y.dateDoc DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    @Override
    public void onSelectDistant(YvsComDocStocks y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Reconditionnement", "modGescom", "smenReconditionnement", true);
            }
        }
    }

    public void paginer(boolean next) {
        loadAll(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAll(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
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
    public void populateView(DocStock bean) {
        cloneObject(docStock, bean);
        resetFicheContenu();
    }

    public void populateView(ContenuDocStock bean) {
        cloneObject(contenu, bean);
        loadInfosArticle(contenu.getArticle());
        String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
        Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(bean.getArticle().getId(), 1), new Options(docStock.getSource().getId(), 2)});
        contenu.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
    }

    @Override
    public boolean controleFiche(DocStock bean) {
        if (bean.getSource() != null ? bean.getSource().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le depot");
            return false;
        }
        if (bean.getEditeur() != null ? bean.getEditeur().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le responsable");
            return false;
        }
        if ((bean.getCreneauSource() != null) ? bean.getCreneauSource().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le créneau");
            return false;
        }
        if ((selectDoc != null ? (!selectDoc.getDateDoc().equals(bean.getDateDoc())) : false)
                || (bean.getNumDoc() == null || bean.getNumDoc().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_RE_NAME, bean.getDateDoc(), bean.getSource().getId());
            bean.setNumDoc(ref);
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
        }
        return verifyTranche(bean.getCreneauSource().getTranche(), bean.getSource(), bean.getDateDoc());
    }

    public boolean controleFiche(ContenuDocStock bean) {
        if (bean.getDocStock() != null ? bean.getDocStock().getId() < 1 : true) {
            if (!_saveNew()) {
                return false;
            }
            bean.setDocStock(docStock);
        }
        if (bean.getArticle() != null ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez l'article");
            return false;
        }
        if (bean.getQuantite() <= 0 || bean.getResultante() <= 0) {
            getErrorMessage("Le coefficient de conversion n'est pas paramétré");
            return false;
        }
        if (bean.getConditionnement() != null ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le conditionnement source");
            return false;
        }
        if (bean.getUniteDestination() != null ? bean.getUniteDestination().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le conditionnement de destination");
            return false;
        }
        if (bean.getArticle().isRequiereLot()) {
            if (bean.getLotSortie() != null ? bean.getLotSortie().getId() < 1 : true) {
                getErrorMessage("Un numéro de lot est requis pour cet article dans le dépôt");
                return false;
            }
        }
        return true;
    }

    @Override
    public void resetFiche() {
        docStock = new DocStock();
        docStock.setTypeDoc(Constantes.TYPE_RE);
        docStock.setEditeur(UtilUsers.buildBeanUsers(currentUser.getUsers()));
        docStock.setNature(Constantes.OP_RECONDITIONNEMENT);
        selectDoc = new YvsComDocStocks();
        tabIds = "";
        resetFicheContenu();
        update("blog_transformation");
    }

    public void resetFicheContenu() {
        contenu = new ContenuDocStock();
        update("form_reconditionnement");
        update("desc_article_reconditionnement");
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

    private boolean _saveNew() {
        String action = docStock.getId() > 0 ? "Modification" : "Insertion";
        try {
            docStock.setTypeDoc(Constantes.TYPE_RE);
            if (controleFiche(docStock)) {
                docStock.setDestination(docStock.getSource());
                docStock.setCreneauDestinataire(docStock.getCreneauSource());
                YvsComDocStocks y = UtilCom.buildDocStock(docStock, currentAgence.getSociete(), currentUser);
                if (docStock.getId() > 0) {
                    y.setDateUpdate(new Date());
                    dao.update(y);
                } else {
                    y.setDateSave(new Date());
                    y.setDateUpdate(new Date());
                    y.setId(null);
                    y = (YvsComDocStocks) dao.save1(y);
                    docStock.setId(y.getId());
                }
                selectDoc = y;
                int idx = documents.indexOf(y);
                if (idx > -1) {
                    documents.set(idx, y);
                } else {
                    documents.add(0, y);
                }
                return true;
            }
        } catch (Exception ex) {
            getException(action + " Impossible !", ex);
        }
        return false;
    }

    public boolean saveNewContenu() {
        String action = contenu.getId() > 0 ? "Modification" : "Insertion";
        try {
            contenu.setDocStock(docStock);
            cloneObject(contenu.getLotSortie(), contenu.getLotSortie());
            if (controleFiche(contenu)) {
                YvsComContenuDocStock y = UtilCom.buildContenuDocStock(contenu, currentUser);
                if (contenu.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsComContenuDocStock) dao.save1(y);
                    contenu.setId(y.getId());
                }
                int idx = docStock.getContenus().indexOf(y);
                if (idx > -1) {
                    docStock.getContenus().set(idx, y);
                } else {
                    docStock.getContenus().add(0, y);
                }
                idx = selectDoc.getContenus().indexOf(y);
                if (idx > -1) {
                    selectDoc.getContenus().set(idx, y);
                } else {
                    selectDoc.getContenus().add(0, y);
                }
                idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                } else {
                    documents.add(0, selectDoc);
                }
                succes();
                resetFicheContenu();
                return true;
            }
        } catch (Exception ex) {
            getException(action + " Impossible !", ex);
        }
        return false;
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("gescom_delete_recond")) {
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
                    if (bean.canDelete()) {
                        dao.delete(bean);
                        if (docStock.getId() == bean.getId()) {
                            resetFiche();
                        }
                    }
                }
                documents.removeAll(list);
                succes();
                resetFiche();
                tabIds = "";
            }
        } catch (Exception ex) {
            getException("Suppression Impossible !", ex);
        }
    }

    public void openTodeleteBean(YvsComDocStocks y) {
        tabIds = "" + documents.indexOf(y);
        openDialog("dlgConfirmDelete");
    }

    public void deleteBean(YvsComDocStocks y) {
        try {
            if (!autoriser("gescom_delete_recond")) {
                openNotAcces();
                return;
            }
            if (y != null) {
                if (!y.canDelete()) {
                    getErrorMessage("Vous ne pouvez pas supprimer cette transformation");
                    return;
                }
                dao.delete(y);
                if (documents.contains(y)) {
                    documents.remove(y);
                }
                if (docStock.getId() == y.getId()) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !", "Il est possible que l'élément soit en relation avec d'autres ressources");
            getException("Lymytz Erp Error...", ex);
        }
    }

    public void deleteBeanContenu(YvsComContenuDocStock y) {
        try {
            if (y != null) {
                if (!selectDoc.canDelete()) {
                    getErrorMessage("Vous ne pouvez pas supprimer cette ligne de reconditionnement");
                    return;
                }
                dao.delete(y);
                docStock.getContenus().remove(y);
                selectDoc.getContenus().remove(y);
                int idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                } else {
                    documents.add(0, selectDoc);
                }
                if (contenu.getId() == y.getId()) {
                    resetFicheContenu();
                }
                succes();
            }
        } catch (NumberFormatException ex) {
            getException("Suppression Impossible !", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectObject(YvsComDocStocks y) {
        y.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{y}));
        selectDoc = y;
        populateView(UtilCom.buildBeanDocStock(y));
        loadHistoriques(y.getSource());
        if (!tranches.contains(y.getCreneauSource())) {
            tranches.add(y.getCreneauSource());
        }
        update("blog_transformation");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComDocStocks y = (YvsComDocStocks) ev.getObject();
            onSelectObject(y);
            tabIds = documents.indexOf(y) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewContent(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocStock y = (YvsComContenuDocStock) ev.getObject();
            onSelectObject(y.getDocStock());
        }
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComContenuDocStock y = (YvsComContenuDocStock) ev.getObject();
            populateView(UtilCom.buildBeanContenuDocStock(y));
            update("blog_reconditionnement");
            update("desc_article_reconditionnement");
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheContenu();
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticleDepot bean = (YvsBaseArticleDepot) ev.getObject();
            Articles a = UtilProd.buildBeanArticles(bean.getArticle());
            a.setRequiereLot(bean.getRequiereLot());
            chooseArticle(a);
        }
    }

    public void loadOnViewConditionnement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseConditionnement bean = (YvsBaseConditionnement) ev.getObject();
            chooseConditionnement(UtilProd.buildBeanConditionnement(bean));
        }
    }

    public void chooseUniteSortie() {
        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
            if (contenu.getArticle() != null) {
                int idx = contenu.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(contenu.getConditionnement().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = contenu.getArticle().getConditionnements().get(idx);
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(y));
//                    contenu.setPrix(dao.getPuv(contenu.getArticle().getId(), 0, 0, 0, docStock.getSource().getId(), 0, docStock.getDateDoc(), y.getId()));
                    contenu.setPrix(dao.getPr(contenu.getArticle().getId(), docStock.getSource().getId(), 0, docStock.getDateDoc(), y.getId()));
                }
            }
        }
        blurQuantite();
        loadInfosArticle(contenu.getArticle());
    }

    public void chooseUniteEntree(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            long idEntree = (long) ev.getNewValue();
            contenu.getUniteDestination().setId(((long) ev.getNewValue()));
            if (contenu.getArticle() != null) {
                int idx = contenu.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(idEntree));
                if (idx > -1) {
                    YvsBaseConditionnement y = contenu.getArticle().getConditionnements().get(idx);
                    contenu.setUniteDestination(UtilProd.buildBeanConditionnement(y));
                    YvsBaseTableConversion tc = (YvsBaseTableConversion) dao.loadOneByNameQueries("YvsBaseTableConversion.findUniteCorrespondance", new String[]{"unite", "uniteE"},
                            new Object[]{new YvsBaseUniteMesure(contenu.getConditionnement().getUnite().getId()), y.getUnite()});
                    double taux = (tc != null) ? tc.getTauxChange() : 0;
                    if (contenu.getPrix() > 0 && taux > 0) {
                        contenu.setPrixEntree(contenu.getPrix() / taux);
                    } else {
                        contenu.setPrixEntree(dao.getPr(contenu.getArticle().getId(), docStock.getSource().getId(), 0, docStock.getDateDoc(), y.getId()));
                    }
                    contenu.setResultante(convertirUnites(new YvsBaseUniteMesure(contenu.getConditionnement().getUnite().getId()), new YvsBaseUniteMesure(contenu.getUniteDestination().getUnite().getId()), contenu.getQuantite(), taux));
                }
                if (contenu.getQuantite() <= 0 || contenu.getResultante() <= 0) {
                    getErrorMessage("Le coefficient de conversion n'est pas paramétré");
                }
            }
        }
    }

    public void loadInfosArticle(Articles art) {
        List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(art.getId())});
        art.setConditionnements(unites);
        if (docStock.getSource() != null ? docStock.getSource().getId() > 0 : false) {
            art.setStock(dao.stocks(art.getId(), 0, docStock.getSource().getId(), 0, 0, docStock.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLotSortie().getId()));
        } else {
            art.setStock(dao.stocks(art.getId(), 0, 0, currentAgence.getId(), 0, docStock.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLotSortie().getId()));
        }
        contenu.setArticle(art);
        contenu.getArticle().setSelectArt(true);
    }

    public void chooseSource() {
        ManagedDepot s = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (s != null) {
            ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
                YvsBaseDepots y = s.getDepots().get(s.getDepots().indexOf(new YvsBaseDepots(docStock.getSource().getId())));
                Depots d = UtilCom.buildBeanDepot(y);
                cloneObject(docStock.getSource(), d);
                if (a != null) {
                    a.loadAllArticle(y);
                    a.setEntityDepot(y);
                    a.loadActifArticleByDepot(true, true);
                }
                loadHistoriques(y);
                loadAllTranche(y);
            } else {
                if (a != null) {
                    a.getArticlesDebut().clear();
                }
            }
        }
    }

    public void chooseCreneau() {
        if ((docStock.getCreneauSource() != null) ? docStock.getCreneauSource().getId() > 0 : false) {
            YvsComCreneauDepot y = tranches.get(tranches.indexOf(new YvsComCreneauDepot(docStock.getCreneauSource().getId())));
            docStock.setCreneauSource(UtilCom.buildBeanCreneau(y));
        }
    }

    public void chooseArticle(Articles art) {
        loadInfosArticle(art);
//        update("select_conditionnement_reconditionnement");
//        update("select_article_reconditionnement");
    }

    private void chooseConditionnement(Conditionnement c) {
        if (c != null ? c.getId() > 0 : false) {
            chooseArticle(c.getArticle());
            cloneObject(contenu.getConditionnement(), c);
            chooseUniteSortie();
        }
    }

    public void blurQuantite() {
        contenu.setResultante(convertirUnite(contenu.getConditionnement().getUnite(), contenu.getUniteDestination().getUnite(), contenu.getQuantite()));
    }

    public void searchArticle() {
        paginator.addParam(new ParametreRequete("y.article", "article", null, "=", "AND"));
        String num = contenu.getArticle().getRefArt();
        contenu.getArticle().setDesignation("");
        contenu.getArticle().setError(true);
        contenu.getArticle().setId(0);
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            if (m.getEntityDepot() == null) {
                m.setEntityDepot(new YvsBaseDepots(docStock.getSource().getId()));
            }
            Articles y = m.searchArticleActifByDepot(num, true);
            if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                if (m.getArticlesResult().size() > 1) {
                    update("data_articles_reconditionnement");
                } else {
                    chooseArticle(y);
                }
                contenu.getArticle().setError(false);
            } else {
                Conditionnement c = m.searchArticleActifByCodeBarre(num);
                if (m.getConditionnements() != null ? !m.getConditionnements().isEmpty() : false) {
                    if (m.getConditionnements().size() > 1) {
                        update("data_conditionnements_reconditionnement");
                    } else {
                        chooseConditionnement(c);
                    }
                }
            }
        }
    }

    public void initArticles() {
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            m.initArticlesByDepot(contenu.getArticle());
        }
        update("data_articles_reconditionnement");
    }

    public boolean canAutorisation(int action) {
        //action : valide 0 -- Annule 1 -- Editer 2
        switch (action) {
            case 0:
                if (autoriser("gescom_valide_recond")) {
                    return true;
                }
                if (currentUser != null ? (currentUser.getUsers() != null) : false) {
                    if (selectDoc != null ? selectDoc.getSource() != null : false) {
                        if (currentUser.getUsers().getEmploye() != null && selectDoc.getSource().getResponsable() != null) {
                            // Controle si le employé courant est le responsable du depot destinataire
                            if (!selectDoc.getSource().getResponsable().equals(currentUser.getUsers().getEmploye())) {
                                openNotAccesAction("Vous ne pouvez pas modifier cette transformation...car vous n'êtes pas habilité à le faire");
                                return false;
                            }
                        } else {
                            openNotAccesAction("Vous ne pouvez pas modifier cette transformation...car vous n'êtes pas habilité à le faire");
                            return false;
                        }
                    }
                } else {
                    openNotAccesAction("Vous ne pouvez pas modifier cette transformation...car vous n'êtes pas habilité à le faire");
                    return false;
                }
                break;
            case 1:
            //        if(autoriser("")){
            //            return true;
            //        }
            case 2:
                //        if(autoriser("")){
                //            return true;
                //        }
                break;
        }
        return true;
    }

    public void terminer() {
        if (selectDoc == null) {
            return;
        }
        if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (changeStatut(Constantes.ETAT_TERMINE)) {
                succes();
            }
        } else {
            getErrorMessage("Cette transformation doit etre validée");
        }
    }

    public void annulerOrderByForce() {
        annulerByForce(suspend, true);
    }
    boolean suspend;

    public void annulerByForce(boolean suspend) {
        annulerByForce(suspend, false);
    }

    public void annulerByForce(boolean suspend, boolean force) {
        this.suspend = suspend;
        if (selectDoc == null) {
            return;
        }
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
                // Vérifié qu'aucun document d'inventaire n'exite après cette date
                if (!force) {
                    boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
                    exist_inventaire = !controleInventaire(selectDoc.getSource().getId(), selectDoc.getDateDoc(), selectDoc.getCreneauSource().getTranche().getId(), !gescom_update_stock_after_valide);
                    if (exist_inventaire) {
                        if (!gescom_update_stock_after_valide) {
                            return;
                        } else if (!force) {
                            openDialog("dlgConfirmChangeInventaireByCancel");
                            return;
                        }
                    }
                }
                if (!exist_inventaire) {
                    //contrôle le stock dans l'unité cible
                    String result = null;
                    for (YvsComContenuDocStock c : selectDoc.getContenus()) {
                        result = controleStock(c.getArticle().getId(), c.getConditionnementEntree().getId(), selectDoc.getSource().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", selectDoc.getDateDoc(), (c.getLotEntree() != null ? c.getLotEntree().getId() : 0));
                        if (result != null) {
                            getErrorMessage("Impossible de changer le statut de ce document", "la ligne d'article " + c.getArticle().getDesignation() + " en (" + c.getConditionnementEntree().getUnite().getLibelle() + ") engendrera une incohérence dans le stock à la date du " + result);
                            return;
                        }
                    }
                }
            }
            selectDoc.setAnnulerBy(currentUser.getUsers());
            selectDoc.setDateAnnuler(new Date());
            if (changeStatut(suspend ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE)) {
                if (exist_inventaire) {
                    YvsComDocStocks inventaire = dao.lastInventaire(selectDoc.getSource().getId(), selectDoc.getDateDoc(), selectDoc.getCreneauSource().getTranche().getId());
                    if (inventaire != null ? inventaire.getId() > 0 : false) {
                        for (YvsComContenuDocStock c : selectDoc.getContenus()) {
                            majInventaire(inventaire, c.getArticle(), c.getConditionnementEntree(), c.getQuantiteEntree(), Constantes.MOUV_SORTIE);
                            majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), Constantes.MOUV_ENTREE);
                        }
                    }
                }
                succes();
            }
            exist_inventaire = false;
        }
    }

    public void annuler(boolean suspend) {
        if (selectDoc == null) {
            return;
        }
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (!canAutorisation(suspend ? 1 : 2)) {
                return;
            }
            if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
                openDialog(suspend ? "dlgConfirmAnnuler" : "dlgConfirmEditer");
            } else {
                selectDoc.setAnnulerBy(currentUser.getUsers());
                selectDoc.setDateAnnuler(new Date());
                if (changeStatut(suspend ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE)) {
                    succes();
                }
            }
        }
    }

    public void validerOrderByForce() {
        valider(true);
    }

    public void valider() {
        valider(false);
    }

    boolean exist_inventaire;

    public void valider(boolean force) {
        if (selectDoc == null) {
            return;
        }
        if (!canAutorisation(0)) {
            return;
        }
        // Vérifié qu'aucun document d'inventaire n'exite après cette date
        if (!force) {
            boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
            exist_inventaire = !controleInventaire(selectDoc.getSource().getId(), selectDoc.getDateDoc(), selectDoc.getCreneauSource().getTranche().getId(), !gescom_update_stock_after_valide);
            if (exist_inventaire) {
                if (!gescom_update_stock_after_valide) {
                    return;
                } else if (!force) {
                    openDialog("dlgConfirmChangeInventaireByValid");
                    return;
                }
            }
        }
        if (!exist_inventaire) {
            String result = null;
            for (YvsComContenuDocStock c : selectDoc.getContenus()) {
                result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), docStock.getSource().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", docStock.getDateDoc(), (c.getLotSortie() != null ? c.getLotSortie().getId() : 0));
                if (result != null) {
                    getErrorMessage("Impossible de changer le statut de ce document", "la ligne d'article " + c.getArticle().getDesignation() + " en (" + c.getConditionnement().getUnite().getLibelle() + ") engendrera une incohérence dans le stock à la date du " + result);
                    return;
                }
            }
        }
        selectDoc.setValiderBy(currentUser.getUsers());
        selectDoc.setDateValider(new Date());
        if (changeStatut(Constantes.ETAT_VALIDE)) {
            if (exist_inventaire) {
                YvsComDocStocks inventaire = dao.lastInventaire(selectDoc.getSource().getId(), selectDoc.getDateDoc(), selectDoc.getCreneauSource().getTranche().getId());
                if (inventaire != null ? inventaire.getId() > 0 : false) {
                    for (YvsComContenuDocStock c : selectDoc.getContenus()) {
                        majInventaire(inventaire, c.getArticle(), c.getConditionnementEntree(), c.getQuantiteEntree(), Constantes.MOUV_ENTREE);
                        majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), Constantes.MOUV_SORTIE);
                    }
                }
            }
            succes();
        }
        exist_inventaire = false;
    }

    public boolean changeStatut(String etat) {
        if (!etat.equals("") && selectDoc != null) {
            selectDoc.setStatut(etat);
            selectDoc.setAuthor(currentUser);
            dao.update(selectDoc);
            if (documents.contains(selectDoc)) {
                documents.set(documents.indexOf(selectDoc), selectDoc);
            }
            docStock.setStatut(etat);
            update("data_transformation");
            update("form_transformation");
            update("grp_btn_etat_transformation");
            return true;
        }
        return false;
    }

    public void addParamActif() {
        paginator.addParam(new ParametreRequete("y.actif", "actif", actif_, "=", "AND"));
        loadAll(true, true);
    }

    public void addParamAgence() {
        ParametreRequete p;
        if (agence_ > 0) {
            p = new ParametreRequete("y.source.agence", "agence", new YvsAgences(agence_), "=", "AND");
        } else {
            p = new ParametreRequete("y.source.agence", "agence", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
        _loadDepot();
    }

    public void addParamSource() {
        ParametreRequete p = new ParametreRequete("y.source", "depot", null, "=", "AND");
        if (depot_ > 0) {
            p = new ParametreRequete("y.source", "depot", new YvsBaseDepots(depot_), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamIds() {
        addParamIds(true);
        loadAll(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.numDoc", "reference", null, "LIKE", "AND");
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", numSearch_.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numDoc)", "reference", numSearch_.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numPiece)", "reference", numSearch_.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateDoc", "dates", null, "=", "AND");
        if (date_) {
            p = new ParametreRequete("y.dateDoc", "dates", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
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
        loadAll(true, true);
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

    public void addParamAgenceContenu() {
        _loadDepot();
        ParametreRequete p;
        if (agenceContenu > 0) {
            p = new ParametreRequete("y.docStock.source.agence", "agence", new YvsAgences(agenceContenu));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.docStock.source.agence", "agence", null);
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

    public void addParamNumero() {
        ParametreRequete p = new ParametreRequete("y.docStock.numDoc", "reference", null);
        if (reference != null ? reference.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", reference + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docStock.numDoc)", "reference", reference.toUpperCase().trim() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docStock.numPiece)", "reference", reference.toUpperCase().trim() + "%", "LIKE", "OR"));
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
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.refArt)", "article", article.toUpperCase().trim() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.article.designation)", "article", article.toUpperCase().trim() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    @Override
    public DocStock recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

}
