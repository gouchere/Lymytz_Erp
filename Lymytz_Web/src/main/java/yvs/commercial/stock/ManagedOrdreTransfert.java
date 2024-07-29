/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.CategorieComptable;
import yvs.base.produits.Articles;
import yvs.production.UtilProd;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.UtilCom;
import yvs.commercial.creneau.Creneau;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComCoutSupDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.produits.YvsBaseArticles;
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
public class ManagedOrdreTransfert extends ManagedCommercial<DocStock, YvsComDocStocks> implements Serializable {

    private DocStock docStock = new DocStock(), docSelect = new DocStock();
    private List<YvsComDocStocks> transferts, transfertsHist;
    private YvsComDocStocks selectDoc, selectDoc_;

    private List<YvsComContenuDocStock> contenus_;
    private YvsComContenuDocStock selectContenu, selectContenu_;
    private ContenuDocStock contenu = new ContenuDocStock(), contenu_ = new ContenuDocStock();
    private String commentaire;

    private YvsComCoutSupDocStock selectCout;
    private CoutSupDoc cout = new CoutSupDoc();

    private List<CategorieComptable> categories;

    private List<YvsBaseDepots> depotsReception, depotsSource, depotsAll;
    private List<YvsComCreneauDepot> creneauxSource, creneauxDestination;
    private long idCreneau, idDepot;

    private CategorieComptable categorie = new CategorieComptable();

    private String tabIds, tabIds_contenu, tabIds_contenu_, tabIds_cout;
    private boolean selectArt;

    List<ParametreRequete> paramsH = new ArrayList<>();
    List<ParametreRequete> params = new ArrayList<>();
    private long destSearch, sourceSearch;

    public ManagedOrdreTransfert() {
        categories = new ArrayList<>();
        transferts = new ArrayList<>();
        transfertsHist = new ArrayList<>();
        creneauxSource = new ArrayList<>();
        creneauxDestination = new ArrayList<>();
        depotsReception = new ArrayList<>();
        contenus_ = new ArrayList<>();
        depotsAll = new ArrayList<>();
        depotsSource = new ArrayList<>();
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public long getSourceSearch() {
        return sourceSearch;
    }

    public void setSourceSearch(long sourceSearch) {
        this.sourceSearch = sourceSearch;
    }

    public long getDestSearch() {
        return destSearch;
    }

    public void setDestSearch(long destSearch) {
        this.destSearch = destSearch;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public long getIdDepot() {
        return idDepot;
    }

    public void setIdDepot(long idDepot) {
        this.idDepot = idDepot;
    }

    public long getIdCreneau() {
        return idCreneau;
    }

    public void setIdCreneau(long idCreneau) {
        this.idCreneau = idCreneau;
    }

    public YvsComContenuDocStock getSelectContenu_() {
        return selectContenu_;
    }

    public void setSelectContenu_(YvsComContenuDocStock selectContenu_) {
        this.selectContenu_ = selectContenu_;
    }

    public ContenuDocStock getContenu_() {
        return contenu_;
    }

    public void setContenu_(ContenuDocStock contenu_) {
        this.contenu_ = contenu_;
    }

    public YvsComDocStocks getSelectDoc_() {
        return selectDoc_;
    }

    public void setSelectDoc_(YvsComDocStocks selectDoc_) {
        this.selectDoc_ = selectDoc_;
    }

    public List<YvsBaseDepots> getDepotsSource() {
        return depotsSource;
    }

    public void setDepotsSource(List<YvsBaseDepots> depotsSource) {
        this.depotsSource = depotsSource;
    }

    public List<YvsComContenuDocStock> getContenus() {
        return docStock.getContenus();
    }

    public List<YvsComContenuDocStock> getContenus_() {
        return contenus_;
    }

    public void setContenus_(List<YvsComContenuDocStock> contenus_) {
        this.contenus_ = contenus_;
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

    public List<YvsComCreneauDepot> getCreneauxDestination() {
        return creneauxDestination;
    }

    public void setCreneauxDestination(List<YvsComCreneauDepot> creneauxDestination) {
        this.creneauxDestination = creneauxDestination;
    }

    public YvsComDocStocks getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocStocks selectDoc) {
        this.selectDoc = selectDoc;
    }

    public String getTabIds_contenu_() {
        return tabIds_contenu_;
    }

    public void setTabIds_contenu_(String tabIds_contenu_) {
        this.tabIds_contenu_ = tabIds_contenu_;
    }

    public DocStock getDocSelect() {
        return docSelect;
    }

    public void setDocSelect(DocStock docSelect) {
        this.docSelect = docSelect;
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

    public List<YvsBaseDepots> getDepotsReception() {
        return depotsReception;
    }

    public void setDepotsReception(List<YvsBaseDepots> depotsReception) {
        this.depotsReception = depotsReception;
    }

    public List<YvsBaseDepots> getDepotsAll() {
        return depotsAll;
    }

    public void setDepotsAll(List<YvsBaseDepots> depotsAll) {
        this.depotsAll = depotsAll;
    }

    public List<YvsComCreneauDepot> getCreneauxSource() {
        return creneauxSource;
    }

    public void setCreneauxSource(List<YvsComCreneauDepot> creneauxSource) {
        this.creneauxSource = creneauxSource;
    }

    public DocStock getDocStock() {
        return docStock;
    }

    public void setDocStock(DocStock docStock) {
        this.docStock = docStock;
    }

    public List<YvsComDocStocks> getTransferts() {
        return transferts;
    }

    public void setTransferts(List<YvsComDocStocks> transferts) {
        this.transferts = transferts;
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
        loadAllTransfert();
        loadAllDepots(true, true);
        load_();
    }

    private void load_() {
        indiceNumsearch_ = genererPrefixe(Constantes.TYPE_OT_NAME, currentDepot != null ? currentDepot.getId() : 0);
        if ((docStock != null) ? docStock.getSource().getId() < 1 : true) {
            docStock = new DocStock();
            docStock.setTypeDoc(Constantes.TYPE_OT);
            if (docStock.getDocumentLie() == null) {
                docStock.setDocumentLie(new DocStock());
            }
            numSearch_ = "";
        }
    }

    public List<YvsComDocStocks> loadTranferts() {
        ParametreRequete p = new ParametreRequete("typeDoc", "typeDoc", Constantes.TYPE_OT);
        p.setOperation("=");
        p.setPredicat("AND");
        if (!params.contains(p)) {
            params.add(p);
        } else {
            params.set(params.indexOf(p), p);
        }
        if (currentUser.getUsers().getAccesMultiAgence()) {
            p = new ParametreRequete("societe", "societe", currentAgence.getSociete());
            p.setOperation("=");
            p.setPredicat("AND");
            if (!params.contains(p)) {
                params.add(p);
            } else {
                params.set(params.indexOf(p), p);
            }
        } else {
            if (currentUser.getUsers().getSuperAdmin()) {
                p = new ParametreRequete("author.agence", "agence", currentAgence);
                p.setOperation("=");
                p.setPredicat("AND");
                if (!params.contains(p)) {
                    params.add(p);
                } else {
                    params.set(params.indexOf(p), p);
                }
            } else {
                p = new ParametreRequete("destination", "destination", currentDepot);
                p.setOperation("=");
                p.setPredicat("AND");
                if (!params.contains(p)) {
                    params.add(p);
                } else {
                    params.set(params.indexOf(p), p);
                }
            }
        }
        nameQueriCount = buildDynamicQuery(params, "SELECT COUNT(y) FROM YvsComDocStocks y WHERE");
        nameQueri = buildDynamicQuery(params, "SELECT y FROM YvsComDocStocks y WHERE", new String[]{"dateDoc"});
        Long m = (Long) dao.loadObjectByEntity(nameQueriCount, champ, val);
        imax = (m != null ? m : 0);
        return dao.loadEntity(nameQueri, champ, val, idebut, ifin);
    }

    public void loadAllTransfert() {
        transferts = loadTranferts();

        update("data_ordre_transfert");
    }

    public void init(boolean next) {

        loadAllTransfert();
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllTransfert();
        setDisablePrev(true);
    }

    public void loadAllTransfertHist() {
        ParametreRequete p = new ParametreRequete("typeDoc", "typeDoc", Constantes.TYPE_OT);
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
        nameQueri = buildDynamicQuery(paramsH, "SELECT y FROM YvsComDocStocks y WHERE", new String[]{"dateDoc"});
        transfertsHist = dao.loadEntity(nameQueri, champ, val, 0, 5);
        update("data_ordre_transfert_hist");
    }

    public void loadAllDepots(boolean avancer, boolean init) {
        depotsReception.clear();
        depotsAll.clear();
        nameQueriCount = "YvsBaseDepots.findByAgenceCount";
        nameQueri = "YvsBaseDepots.findByAgence";
        champ = new String[]{"agence"};
        val = new Object[]{currentAgence};
        depotsAll = loadAllDepot(avancer, init);
        depotsReception.addAll(depotsAll.subList(0, (depotsAll.size() > 15) ? 15 : depotsAll.size()));
    }

    private List<YvsBaseDepots> loadAllDepot(boolean avancer, boolean init) {
        if (!init) {
            if (avancer) {
                setFirtResult(getFirtResult() + getNbMax());
            } else {
                setFirtResult(getFirtResult() - getNbMax());
            }
            if (getTotalPage() == getCurrentPage()) {
                setFirtResult(0);
            }
        } else {
            setFirtResult(0);
        }
        PaginatorResult<YvsBaseDepots> pa = new PaginatorResult<>();
        pa = pa.loadResult(nameQueriCount, nameQueri, champ, val, getFirtResult(), getNbMax(), dao);
        setDisableNext(pa.isDisNext());
        setDisablePrev(pa.isDisPrev());
        setTotalPage(pa.getNbPage());
        setCurrentPage(pa.getCurrentPage());
        return pa.getResult();
    }

    private void loadDepotSource(YvsBaseDepots y) {
        nameQueri = "YvsComLiaisonDepot.findDepotByDepotLier";
        champ = new String[]{"depot"};
        val = new Object[]{y};
        depotsSource = dao.loadNameQueries(nameQueri, champ, val);
        update("data_depot_order_transfert");
        update("select_source_ordre_transfert");
    }

    private void loadContenus_(YvsComDocStocks y) {
        docStock.getContenus().clear();
        nameQueri = "YvsComContenuDocStock.findByDocStock";
        champ = new String[]{"docStock"};
        val = new Object[]{y};
        List<YvsComContenuDocStock> lc = dao.loadNameQueries(nameQueri, champ, val);

        for (YvsComContenuDocStock c : lc) {
            nameQueri = "YvsComContenuDocStock.findByParent";
            champ = new String[]{"parent"};
            val = new Object[]{c};
            List<YvsComContenuDocStock> l = dao.loadNameQueries(nameQueri, champ, val);
            double qte = c.getQuantite();
            for (YvsComContenuDocStock c_ : l) {
                YvsComDocStocks d = c_.getDocStock();
                switch (d.getStatut()) {
                    case Constantes.ETAT_VALIDE:
                        qte -= c_.getQuantite();
                        break;
                    case Constantes.ETAT_EDITABLE:
                        c.setQteAttente(c.getQteAttente() + c_.getQuantite());
                        break;
                    default:
                        break;
                }
            }
            c.setParent(c);
            c.setQteRestant(qte);
            docStock.getContenus().add(c);
        }
    }

    private void loadCreneauxSource(YvsBaseDepots y, boolean ordre) {
        creneauxSource = loadCreneaux(y, ((ordre) ? docStock.getDateDoc() : docSelect.getDateDoc()));
    }

    private void loadCreneauxDestinataire(YvsBaseDepots y, boolean ordre) {
        creneauxDestination = loadCreneaux(y, ((ordre) ? docStock.getDateDoc() : docSelect.getDateDoc()));
    }

    public YvsComDocStocks buildDocStock(DocStock y) {
        YvsComDocStocks d = new YvsComDocStocks();
        if (y != null) {
            d.setId(y.getId());
            d.setActif(y.isActif());
            d.setSupp(y.isSupp());
            if ((y.getCategorieComptable() != null) ? y.getCategorieComptable().getId() > 0 : false) {
                d.setCategorieComptable(new YvsBaseCategorieComptable(y.getCategorieComptable().getId()));
            }
            if ((y.getCreneauDestinataire() != null) ? y.getCreneauDestinataire().getId() > 0 : false) {
                d.setCreneauDestinataire(new YvsComCreneauDepot(y.getCreneauDestinataire().getId(), new YvsJoursOuvres(y.getCreneauDestinataire().getJour().getId(), y.getCreneauDestinataire().getJour().getJour()), new YvsGrhTrancheHoraire(y.getCreneauDestinataire().getTranche().getId(), y.getCreneauDestinataire().getTranche().getHeureDebut(), y.getCreneauDestinataire().getTranche().getHeureFin())));
            }
            if ((y.getCreneauSource() != null) ? y.getCreneauSource().getId() > 0 : false) {
                d.setCreneauSource(new YvsComCreneauDepot(y.getCreneauSource().getId(), new YvsJoursOuvres(y.getCreneauSource().getJour().getId(), y.getCreneauSource().getJour().getJour()), new YvsGrhTrancheHoraire(y.getCreneauSource().getTranche().getId(), y.getCreneauSource().getTranche().getHeureDebut(), y.getCreneauSource().getTranche().getHeureFin())));
            }
            if ((y.getDestination() != null) ? y.getDestination().getId() > 0 : false) {
                d.setDestination(new YvsBaseDepots(y.getDestination().getId(), y.getDestination().getDesignation()));
            }
            if ((y.getSource() != null) ? y.getSource().getId() > 0 : false) {
                d.setSource(new YvsBaseDepots(y.getSource().getId(), y.getSource().getDesignation()));
            }
            d.setDateDoc((y.getDateDoc() != null) ? y.getDateDoc() : new Date());
            d.setNumPiece(y.getNumPiece());
            d.setNumDoc(y.getNumDoc());
            d.setStatut((y.getStatut() != null) ? y.getStatut() : Constantes.ETAT_EDITABLE);
            d.setTypeDoc(y.getTypeDoc());
            d.setNature(y.getNature());
            if ((y.getDocumentLie() != null) ? y.getDocumentLie().getId() > 0 : false) {
                d.setDocumentLie(new YvsComDocStocks(y.getDocumentLie().getId()));
            }
            d.setDateSave(new Date());
            d.setSociete(currentAgence.getSociete());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                d.setAuthor(currentUser);
            }
            d.setNew_(true);
        }
        return d;
    }

    public YvsComContenuDocStock buildContenuDocStock(ContenuDocStock y) {
        YvsComContenuDocStock c = new YvsComContenuDocStock();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            c.setSupp(y.isSupp());
            if ((y.getArticle() != null) ? y.getArticle().getId() > 0 : false) {
                c.setArticle(new YvsBaseArticles(y.getArticle().getId(), y.getArticle().getRefArt(), y.getArticle().getDesignation()));
            }
            c.setPrix(y.getPrix());
            c.setQuantite(y.getQuantite());
            if ((y.getDocStock() != null) ? y.getDocStock().getId() > 0 : false) {
                c.setDocStock(new YvsComDocStocks(y.getDocStock().getId()));
            }
            if ((y.getParent() != null) ? y.getParent().getId() > 0 : false) {
                c.setParent(new YvsComContenuDocStock(y.getParent().getId()));
            }
            c.setDateSave(new Date());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                c.setAuthor(currentUser);
            }
            c.setNew_(true);
        }
        return c;
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
        if (docStock.getCreneauSource() != null) {
            if (docStock.getCreneauSource().getId() < 1) {
                docStock.getCreneauSource().setId(idCreneau);
            }
        } else {
            docStock.setCreneauSource(new Creneau(idCreneau));
        }
        if (docStock.getSource() != null) {
            if (docStock.getSource().getId() < 1) {
                docStock.getSource().setId(idDepot);
            }
        } else {
            docStock.setSource(new Depots(idDepot));
        }
        docStock.setTypeDoc(Constantes.TYPE_OT);
        docStock.setNature("ORDRE");
        return recopieView(docStock);
    }

    public DocStock recopieView(DocStock docStock) {
        DocStock d = new DocStock();
        d.setId(docStock.getId());
        d.setActif(docStock.isActif());
        d.setSupp(docStock.isSupp());
        d.setCategorieComptable(docStock.getCategorieComptable());
        d.setCreneauDestinataire(docStock.getCreneauDestinataire());
        d.setCreneauSource(docStock.getCreneauSource());
        d.setDateDoc((docStock.getDateDoc() != null) ? docStock.getDateDoc() : new Date());
        d.setDestination(docStock.getDestination());
        d.setSource(docStock.getSource());
        d.setNumPiece(docStock.getNumPiece());
        d.setNumDoc(docStock.getNumDoc());
        d.setStatut((docStock.getStatut() != null) ? docStock.getStatut() : Constantes.ETAT_EDITABLE);
        d.setTypeDoc((docStock.getTypeDoc() != null) ? docStock.getTypeDoc() : Constantes.TYPE_FT);
        d.setDocumentLie(docStock.getDocumentLie());
        d.setDocAchat(docStock.getDocAchat());
        d.setUpdate(docStock.isUpdate());
        d.setNature(docStock.getNature());
        return d;
    }

    public ContenuDocStock recopieViewContenu(DocStock doc, ContenuDocStock contenu) {
        ContenuDocStock c = new ContenuDocStock();
        c.setId(contenu.getId());
        c.setActif(contenu.isActif());
        c.setSupp(contenu.isSupp());
        c.setArticle(contenu.getArticle());
        c.setPrix(contenu.getPrix());
        c.setQuantite(contenu.getQuantite());
        c.setUpdate(contenu.isUpdate());
        contenu.setPrixTotal(contenu.getQuantite() * contenu.getPrix());
        c.setPrixTotal(contenu.getPrixTotal());
        c.setDocStock(doc);
        c.setParent(contenu);
        c.setNew_(true);
        return c;
    }

    public ContenuDocStock recopieViewContenu(DocStock doc) {
        ContenuDocStock c = new ContenuDocStock();
        c.setId(contenu.getId());
        c.setActif(contenu.isActif());
        c.setSupp(contenu.isSupp());
        c.setArticle(contenu.getArticle());
        c.setPrix(contenu.getPrix());
        c.setQuantite(contenu.getQuantite());
        c.setUpdate(contenu.isUpdate());
        contenu.setPrixTotal(contenu.getQuantite() * contenu.getPrix());
        c.setPrixTotal(contenu.getPrixTotal());
        c.setDocStock(doc);
        c.setNew_(true);
        return c;
    }

    public CoutSupDoc recopieViewCout(DocStock doc) {
        CoutSupDoc c = new CoutSupDoc();
        c.setId(cout.getId());
        c.setActif(cout.isActif());
        c.setSupp(cout.isSupp());
        c.setType(cout.getType());
        c.setMontant(cout.getMontant());
        c.setUpdate(cout.isUpdate());
        c.setDoc(doc.getId());
        c.setNew_(true);
        return c;
    }

    @Override
    public boolean controleFiche(DocStock bean) {
        if (!_controleFiche(bean)) {
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre editable pour pouvoir etre modifié");
            return false;
        }
        return true;
    }

    private boolean _controleFiche(DocStock bean) {
        if (bean.getNumDoc() == null || bean.getNumDoc().equals("")) {
            String ref = genererReference(Constantes.TYPE_OT_NAME, bean.getDateDoc());
            bean.setNumDoc(ref);
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
        }
        if ((bean.getDestination() != null) ? bean.getDestination().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier la destination");
            return false;
        }
        if (!verifyDateStock(bean.getDateDoc(), bean.isUpdate())) {
            return false;
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
        if (!verifyTranche(bean.getCreneauSource().getTranche(), bean.getSource(), bean.getDateDoc())) {
            return false;
        }
        return verifyTranche(bean.getCreneauDestinataire().getTranche(), bean.getDestination(), bean.getDateDoc());
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

    public boolean controleFiche_(DocStock bean) {
        String ref = genererReference(Constantes.TYPE_FT_NAME, bean.getDateDoc());
        bean.setNumDoc(ref);
        if (ref == null || ref.trim().equals("")) {
            return false;
        }
        if ((bean.getDestination() != null) ? bean.getDestination().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier la destination");
            return false;
        }
        if ((bean.getCreneauDestinataire() != null) ? bean.getCreneauDestinataire().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le créneau de reception");
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
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if ((bean.getDocumentLie() != null) ? bean.getDocumentLie().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'ordre de transfert");
            return false;
        }
        if (bean.getDocumentLie().getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("L'ordre de transfert doit etre validé");
            return false;
        }
        if (bean.getDocumentLie().getStatut().equals(Constantes.ETAT_ANNULE)) {
            getErrorMessage("cet ordre de transfert est annulé");
            return false;
        }
        if (bean.getDocumentLie().isCloturer()) {
            getErrorMessage("cet ordre de transfert est déja cloturé");
            return false;
        }
//      return writeInExercice(bean.getDateDoc()));
        return true;
    }

    public boolean controleFicheContenu(ContenuDocStock bean) {
        if (!bean.getDocStock().isUpdate()) {
            getErrorMessage("Vous devez d'abord enregistrer la fiche");
            return false;
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        return _controleFiche_(bean.getDocStock());
    }

    public boolean controleFicheCout(CoutSupDoc bean) {
        if (bean.getDoc() < 1) {
            getErrorMessage("Vous devez d'abord enregistrer une fiche");
            return false;
        }
//        if (bean.getLibelle() == null || bean.getLibelle().trim().equals("")) {
//            getErrorMessage("Vous devez entrer le libelle");
//            return false;
//        }
        return _controleFiche_(docStock);
    }

    @Override
    public void populateView(DocStock bean) {
        bean.setUpdate(true);
        idDepot = 0;
        idCreneau = 0;
        cloneObject(docStock, bean);
        if ((docStock.getDestination() != null) ? docStock.getDestination().getId() > 0 : false) {
            if (depotsReception != null ? !depotsReception.isEmpty() : false) {
                if (depotsReception.contains(new YvsBaseDepots(docStock.getDestination().getId()))) {
                    YvsBaseDepots d_ = depotsReception.get(depotsReception.indexOf(new YvsBaseDepots(docStock.getDestination().getId())));
                    Depots d = UtilCom.buildBeanDepot(d_);
                    cloneObject(docStock.getDestination(), d);
                    loadDepotSource(d_);
//                    loadArticles(d_);
                    loadCreneauxDestinataire(d_, true);
                }
            }
        }
        if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
            if (depotsSource != null ? !depotsSource.isEmpty() : false) {
                if (depotsSource.contains(new YvsBaseDepots(docStock.getSource().getId()))) {
                    YvsBaseDepots d_ = depotsSource.get(depotsSource.indexOf(new YvsBaseDepots(docStock.getSource().getId())));
                    Depots d = UtilCom.buildBeanDepot(d_);
                    cloneObject(docStock.getSource(), d);
                    loadCreneauxSource(d_, true);
                    idDepot = bean.getSource() != null ? bean.getSource().getId() : 0;
                    idCreneau = bean.getCreneauSource() != null ? bean.getCreneauSource().getId() : 0;
                }
            }
        }
    }

    public void populateViewO(DocStock bean) {
        cloneObject(docSelect, bean);
        docSelect.setUpdate(false);
        docSelect.setId(0);
        docSelect.setStatut(Constantes.ETAT_EDITABLE);

        if ((docSelect.getDestination() != null) ? docSelect.getDestination().getId() > 0 : false) {
            if (depotsReception != null ? !depotsReception.isEmpty() : false) {
                YvsBaseDepots d_ = depotsReception.get(depotsReception.indexOf(new YvsBaseDepots(docSelect.getDestination().getId())));
                Depots d = UtilCom.buildBeanDepot(d_);
                cloneObject(docSelect.getDestination(), d);
                loadDepotSource(d_);
                loadCreneauxDestinataire(d_, false);
            }
        }
        if ((docSelect.getSource() != null) ? docSelect.getSource().getId() > 0 : false) {
            if (depotsSource != null ? !depotsSource.isEmpty() : false) {
                YvsBaseDepots d_ = depotsSource.get(depotsSource.indexOf(new YvsBaseDepots(docSelect.getSource().getId())));
                Depots d = UtilCom.buildBeanDepot(d_);
                cloneObject(docSelect.getSource(), d);
                loadCreneauxSource(d_, false);
            }
        }
    }

    public void populateViewContenu(ContenuDocStock bean) {
        bean.setUpdate(true);
        bean.getArticle().setStock_(dao.stocks(bean.getArticle().getId(), docStock.getCreneauDestinataire().getTranche().getId(), docStock.getDestination().getId(), 0, 0, docStock.getDateDoc(), bean.getConditionnement().getId(), bean.getLotEntree().getId()));

        bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), docStock.getCreneauSource().getTranche().getId(), docStock.getSource().getId(), 0, 0, docStock.getDateDoc(), bean.getConditionnement().getId(), bean.getLotSortie().getId()));
        bean.getArticle().setPuv(dao.getPuv(bean.getArticle().getId(), bean.getQuantite(), 0, 0, docStock.getSource().getId(), 0, docStock.getDateDoc(), bean.getConditionnement().getId()));
        contenu.setPrix(dao.getPua(bean.getArticle().getId(), 0));

        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
        selectArt = true;
        cloneObject(contenu, bean);
    }

    public void populateViewContenu_(ContenuDocStock bean) {
        bean.setUpdate(true);
        cloneObject(contenu_, bean);
    }

    public void populateViewCout(CoutSupDoc bean) {
        bean.setUpdate(true);
        cloneObject(cout, bean);
    }

    @Override
    public void resetFiche() {
        transfertsHist.clear();
        resetFiche_();
        update("blog_form_ordre_transfert");
    }

    public void resetFiche_(DocStock docStock) {
        resetFiche(docStock);
        docStock.setCategorieComptable(new CategorieComptable());
        docStock.setContenus(new ArrayList<YvsComContenuDocStock>());
        docStock.setCouts(new ArrayList<YvsComCoutSupDocStock>());
        docStock.setCreneauDestinataire(new Creneau());
        docStock.setCreneauSource(new Creneau());
        docStock.setDestination(new Depots());
        docStock.setDocumentLie(new DocStock());
        docStock.setSource(new Depots());
        docStock.setStatut(Constantes.ETAT_EDITABLE);
    }

    public void resetFiche_() {
        resetFiche_(docStock);
        resetFiche_(docSelect);
        docStock.getContenus().clear();
        docStock.getCouts().clear();
        idCreneau = 0;
        idDepot = 0;
        selectDoc = null;
        tabIds = "";
        resetFicheContenu();
        resetFicheCout();
        update("blog_form_ordre_transfert");
    }

    public void resetFicheContenu() {
        resetFiche(contenu);
        contenu.setArticle(new Articles());
        selectContenu = null;
        tabIds_contenu = "";
        selectArt = false;
        tabIds_contenu_ = "";
        update("form_contenu_ordre_transfert");
    }

    public void resetFicheContenu_() {
        resetFiche(contenu_);
        contenu_.setArticle(new Articles());
        selectContenu_ = null;
        tabIds_contenu_ = "";
    }

    public void resetFicheCout() {
        resetFiche(cout);
        tabIds_cout = "";
        selectCout = null;
    }

    @Override
    public boolean saveNew() {
        try {
            DocStock bean = recopieView();
            if (controleFiche(bean)) {
                selectDoc = buildDocStock(bean);
                if (!bean.isUpdate()) {
                    selectDoc.setId(null);
                    selectDoc = (YvsComDocStocks) dao.save1(selectDoc);
                    bean.setId(selectDoc.getId());
                    docStock.setId(selectDoc.getId());
                    transferts.add(0, selectDoc);
                    transfertsHist.add(0, selectDoc);
                } else {
                    dao.update(selectDoc);
                    transferts.set(transferts.indexOf(selectDoc), selectDoc);
                    if (transfertsHist.contains(selectDoc)) {
                        transfertsHist.set(transfertsHist.indexOf(selectDoc), selectDoc);
                    }
                }
                docStock.setNumDoc(bean.getNumDoc());
                docStock.setUpdate(true);
                succes();
                actionOpenOrResetAfter(this);
                update("data_ordre_transfert");
                update("data_fiche_transfert_hist");
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
            return false;
        }
    }

    public boolean saveNew_() {
        if (saveNew_0()) {
            succes();
            return true;
        }
        return false;
    }

    public boolean saveNew_0() {
        try {
            docSelect.setTypeDoc(Constantes.TYPE_FT);
            docSelect.setNature(Constantes.TRANSFERT);
            DocStock bean = recopieView(docSelect);
            if (controleFiche_(bean)) {
                YvsComDocStocks en = buildDocStock(bean);
                if (!bean.isUpdate()) {
                    en.setId(null);
                    en = (YvsComDocStocks) dao.save1(en);
                    docSelect.setId(en.getId());
                    selectDoc.getDocuments().add(en);
                } else {
                    dao.update(en);
                }
                docSelect.setNumDoc(bean.getNumDoc());
                docSelect.setUpdate(true);
                update("data_ordre_transfert");
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
            return false;
        }
    }

    public void saveAll() {
        if (saveNew_0()) {
            System.err.println(" ---- Doc stock " + docSelect.getId());
            if (docSelect.getId() > 0) {
                for (YvsComContenuDocStock c : docStock.getContenus()) {
                    System.err.println(" ---- Save contenue " + c.getId());
                    c.setQuantite(c.getQteRestant());
                    c.setDocStock(new YvsComDocStocks(docSelect.getId()));
                    c.setId(null);
                    YvsComContenuDocStock c_ = (YvsComContenuDocStock) dao.save1(c);
                    contenus_.add(c_);
                }
                changeStatut(Constantes.ETAT_SOUMIS);
                update("data_contenu_order_stock");
            }
        }
    }

    public void saveNewContenu() {
        try {
            ContenuDocStock bean = recopieViewContenu(docStock);
            if (controleFicheContenu(bean)) {
                YvsComContenuDocStock en = buildContenuDocStock(bean);
                if (!bean.isUpdate()) {
                    en.setId(null);
                    en = (YvsComContenuDocStock) dao.save1(en);
                    contenu.setId(en.getId());
                    docStock.getContenus().add(0, en);
                } else {
                    dao.update(en);
                    docStock.getContenus().set(docStock.getContenus().indexOf(en), en);
                }
                succes();
                resetFicheContenu();
                update("data_ordre_transfert");
                update("data_contenu_ordre_transfert");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveNewContenu_() {
        try {
            contenu_.setUpdate(false);
            ContenuDocStock bean = recopieViewContenu(docSelect, contenu_);
            if (controleFicheContenu(bean)) {
                YvsComContenuDocStock en = buildContenuDocStock(bean);
                if (!bean.isUpdate()) {
                    en.setId(null);
                    en = (YvsComContenuDocStock) dao.save1(en);
                    contenus_.add(0, en);
                } else {
                    dao.update(en);
                    contenus_.set(contenus_.indexOf(en), en);
                }
                succes();
                resetFicheContenu_();
                update("data_contenu_order_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
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
                    cout.setId(en.getId());
                    docStock.getCouts().add(0, en);
                } else {
                    dao.update(en);
                    docStock.getCouts().set(docStock.getCouts().indexOf(en), en);
                }
                succes();
                resetFicheCout();
                update("data_ordre_transfert");
                update("data_cout_ordre_transfert");
                update("blog_form_montant_doc");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
        }
    }

    public void addCommentaireContenu(YvsComContenuDocStock y) {
        selectContenu = y;
        commentaire = y.getCommentaire();
        update("txt_commentaire_contenu_ordre_transfer");
    }

    public void addCommentaireContenu() {
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setCommentaire(commentaire);
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            docStock.getContenus().set(docStock.getContenus().indexOf(selectContenu), selectContenu);
            update("data_contenu_ordre_transfert");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public boolean saveNewCategorie() {

        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComDocStocks bean = transferts.get(transferts.indexOf(new YvsComDocStocks(id)));
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(new YvsComDocStocks(bean.getId()));
                    transferts.remove(bean);
                    if (transfertsHist.contains(bean)) {
                        transfertsHist.remove(bean);
                    }
                }
                succes();
                resetFiche();
                update("data_ordre_transfert");
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
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                dao.delete(selectDoc);
                transferts.remove(selectDoc);
                if (transfertsHist.contains(selectDoc)) {
                    transfertsHist.remove(selectDoc);
                    update("data_fiche_transfert_hist");
                }
                succes();
                resetFiche();
                update("data_ordre_transfert");
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
                    dao.delete(new YvsComContenuDocStock(bean.getId()));
                    docStock.getContenus().remove(bean);
                    docStock.setMontantTotal(docStock.getMontantTotal() - bean.getPrixTotal());
                }
                succes();
                resetFicheContenu();
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
            if (!_controleFiche_(selectDoc)) {
                return;
            }
            if (selectContenu != null) {
                dao.delete(selectContenu);
                docStock.getContenus().remove(selectContenu);
                succes();
                resetFicheContenu();
                update("data_contenu_ordre_transfert");
                update("data_ordre_transfert");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenuO() {
        try {
            if ((tabIds_contenu_ != null) ? !tabIds_contenu_.equals("") : false) {
                String[] tab = tabIds_contenu_.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComContenuDocStock bean = contenus_.get(contenus_.indexOf(new YvsComContenuDocStock(id)));
                    dao.delete(new YvsComContenuDocStock(bean.getId()));
                    contenus_.remove(bean);
                }
                succes();
                resetFicheContenu_();
                update("data_contenu_order_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenuO_(YvsComContenuDocStock y) {
        selectContenu_ = y;
    }

    public void deleteBeanContenuO_() {
        try {
            if (selectContenu_ != null) {
                dao.delete(selectContenu_);
                contenus_.remove(selectContenu_);
                succes();
                resetFicheContenu_();
                update("data_contenu_order_stock");
            }
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
                    dao.delete(new YvsComCoutSupDocStock(bean.getId()));
                    docStock.getCouts().remove(bean);
                    docStock.setMontantTotalCS(docStock.getMontantTotalCS() - bean.getMontant());
                }
                succes();
                resetFicheCout();
                update("data_cout_ordre_transfert");
                update("data_ordre_transfert");
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
                dao.delete(selectCout);
                docStock.getCouts().remove(selectCout);
                succes();
                resetFicheCout();
                update("data_cout_ordre_transfert");
                update("data_ordre_transfert");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsComDocStocks y) {
        y.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{y}));
        selectDoc = y;
        DocStock bean = UtilCom.buildBeanDocStock(selectDoc);
        populateView(bean);
        addParamDepotSource();
        addParamDepotDest();
        addParamCreneauSource();
        addParamCreneauDest();
        loadAllTransfertHist();
        resetFicheContenu();
        resetFicheCout();
        update("blog_form_ordre_transfert");
        update("save_doc_achat");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            onSelectObject((YvsComDocStocks) ev.getObject());
        }
    }

    public void loadOnView_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectDoc = (YvsComDocStocks) ev.getObject();
            selectDoc.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{selectDoc}));
            DocStock bean = UtilCom.buildBeanDocStock(selectDoc);
            populateView(bean);
            resetFicheContenu();
            resetFicheCout();
            update("blog_form_ordre_transfert");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_ordre_transfert");
    }

    public void unLoadOnView_(UnselectEvent ev) {
        resetFiche_();
        update("blog_form_ordre_transfert");
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocStock bean = (YvsComContenuDocStock) ev.getObject();
            populateViewContenu(UtilCom.buildBeanContenuDocStock(bean));
            update("form_contenu_ordre_transfert");
            update("form_contenu_ordre_transfert");
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheContenu();
        update("form_contenu_ordre_transfert");
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            contenu.setArticle(UtilProd.buildBeanArticles(bean));
            chooseArticle(bean);
            update("form_contenu_ordre_transfert");
            update("form_contenu_ordre_transfert");
        }
    }

    public void loadOnViewContenu_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocStock bean = (YvsComContenuDocStock) ev.getObject();
            bean.setQuantite(bean.getQteRestant());
            populateViewContenu_(UtilCom.buildBeanContenuDocStock(bean));
            update("form_contenu_transfert_stock");
        }
    }

    public void unLoadOnViewContenu_(UnselectEvent ev) {
        resetFicheContenu_();
        update("form_contenu_transfert_stock");
    }

    public void loadOnViewCout(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCoutSupDocStock bean = (YvsComCoutSupDocStock) ev.getObject();
            populateViewCout(UtilCom.buildBeanCoutSupDocStock(bean));
            update("blog_form_cout_ordre_transfert");
        }
    }

    public void unLoadOnViewCout(UnselectEvent ev) {
        resetFicheCout();
        update("blog_form_cout_ordre_transfert");
    }

    public void loadOnViewDepot(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseDepots bean_ = (YvsBaseDepots) ev.getObject();
            loadDepotSource(bean_);
            Depots bean = UtilCom.buildBeanDepot(bean_);
            cloneObject(docStock.getDestination(), bean);
            update("depot_destinataire");
        }
    }

    public void loadOnViewDepot_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseDepots bean_ = (YvsBaseDepots) ev.getObject();
            Depots bean = UtilCom.buildBeanDepot(bean_);
            cloneObject(docStock.getSource(), bean);
            update("depot_source");
        }
    }

    public void chooseCategorie(boolean ordre) {
        if (ordre) {
            if ((docStock.getCategorieComptable() != null) ? docStock.getCategorieComptable().getId() > 0 : false) {
                CategorieComptable d = categories.get(categories.indexOf(docStock.getCategorieComptable()));
                cloneObject(docStock.getCategorieComptable(), d);
            }
        } else {
            if ((docSelect.getCategorieComptable() != null) ? docSelect.getCategorieComptable().getId() > 0 : false) {
                CategorieComptable d = categories.get(categories.indexOf(docSelect.getCategorieComptable()));
                cloneObject(docSelect.getCategorieComptable(), d);
            }
        }
    }

    public void chooseDepotSource() {
        docStock.setCreneauSource(new Creneau());
        docStock.setSource(new Depots(idDepot));
        if ((docStock.getSource() != null) ? docStock.getSource().getId() > 0 : false) {
            YvsBaseDepots d_ = depotsSource.get(depotsSource.indexOf(new YvsBaseDepots(docStock.getSource().getId())));
            Depots d = UtilCom.buildBeanDepot(d_);
            cloneObject(docStock.getSource(), d);
            loadCreneauxSource(d_, true);
        }
        addParamDepotSource();
        loadAllTransfertHist();
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

    public void chooseDepotSource_() {
        docSelect.setCreneauSource(new Creneau());
        if ((docSelect.getSource() != null) ? docSelect.getSource().getId() > 0 : false) {
            YvsBaseDepots d_ = depotsSource.get(depotsSource.indexOf(new YvsBaseDepots(docSelect.getSource().getId())));
            Depots d = UtilCom.buildBeanDepot(d_);
            cloneObject(docSelect.getSource(), d);
            loadCreneauxSource(d_, false);
        }
    }

    public void chooseDepotDestinataire() {
        docStock.setCreneauSource(new Creneau());
        docStock.setSource(new Depots());
        docStock.setCreneauDestinataire(new Creneau());

        if ((docStock.getDestination() != null) ? docStock.getDestination().getId() > 0 : false) {
            YvsBaseDepots d_ = depotsReception.get(depotsReception.indexOf(new YvsBaseDepots(docStock.getDestination().getId())));
            Depots d = UtilCom.buildBeanDepot(d_);
            cloneObject(docStock.getDestination(), d);
            loadDepotSource(d_);
//            loadArticles(d_);
            loadCreneauxDestinataire(d_, true);
        } else {
            docStock.getCreneauDestinataire().setId(0);
            docStock.setDestination(new Depots());
        }
        addParamDepotDest();
        loadAllTransfertHist();
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

    public void chooseDepotDestinataire_() {
        docSelect.setCreneauDestinataire(new Creneau());
        if ((docSelect.getDestination() != null) ? docSelect.getDestination().getId() > 0 : false) {
            YvsBaseDepots d_ = depotsReception.get(depotsReception.indexOf(new YvsBaseDepots(docSelect.getDestination().getId())));
            Depots d = UtilCom.buildBeanDepot(d_);
            cloneObject(docSelect.getDestination(), d);
            loadCreneauxDestinataire(d_, false);
        } else {
            docSelect.getCreneauDestinataire().setId(0);
            docSelect.setDestination(new Depots());
        }
    }

    public void chooseCreneauSource() {
        docStock.setCreneauSource(new Creneau(idCreneau));
        if ((docStock.getCreneauSource() != null) ? docStock.getCreneauSource().getId() > 0 : false) {
            YvsComCreneauDepot c_ = creneauxSource.get(creneauxSource.indexOf(new YvsComCreneauDepot(docStock.getCreneauSource().getId())));
            Creneau c = UtilCom.buildBeanCreneau(c_);
            cloneObject(docStock.getCreneauSource(), c);
        }
        addParamCreneauSource();
        loadAllTransfertHist();
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

    public void chooseCreneauSource_() {
        if ((docSelect.getCreneauSource() != null) ? docSelect.getCreneauSource().getId() > 0 : false) {
            YvsComCreneauDepot c_ = creneauxSource.get(creneauxSource.indexOf(new YvsComCreneauDepot(docSelect.getCreneauSource().getId())));
            Creneau c = UtilCom.buildBeanCreneau(c_);
            cloneObject(docSelect.getCreneauSource(), c);
        }
    }

    public void chooseCreneauDestinataire() {
        if ((docStock.getCreneauDestinataire() != null) ? docStock.getCreneauDestinataire().getId() > 0 : false) {
            YvsComCreneauDepot c_ = creneauxDestination.get(creneauxDestination.indexOf(new YvsComCreneauDepot(docStock.getCreneauDestinataire().getId())));
            Creneau c = UtilCom.buildBeanCreneau(c_);
            cloneObject(docStock.getCreneauDestinataire(), c);
        }
        addParamCreneauDest();
        loadAllTransfertHist();
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

    public void chooseCreneauDestinataire_() {
        if ((docSelect.getCreneauDestinataire() != null) ? docSelect.getCreneauDestinataire().getId() > 0 : false) {
            YvsComCreneauDepot c_ = creneauxDestination.get(creneauxDestination.indexOf(new YvsComCreneauDepot(docSelect.getCreneauDestinataire().getId())));
            Creneau c = UtilCom.buildBeanCreneau(c_);
            cloneObject(docSelect.getCreneauDestinataire(), c);
        }
    }

    public void chooseDateDoc() {
        if ((docStock.getDestination() != null) ? docStock.getDestination().getId() > 0 : false) {
            YvsBaseDepots d_ = depotsReception.get(depotsReception.indexOf(new YvsBaseDepots(docStock.getDestination().getId())));
            loadCreneauxDestinataire(d_, true);
        }
    }

    public void chooseDateDoc_() {
        if ((docSelect.getSource() != null) ? docSelect.getSource().getId() > 0 : false) {
            YvsBaseDepots d_ = depotsSource.get(depotsSource.indexOf(new YvsBaseDepots(docSelect.getSource().getId())));
            loadCreneauxSource(d_, false);
        }
    }

    public void chooseStatut(ValueChangeEvent ev) {
        statut_ = ((String) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("statut");
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p = new ParametreRequete("statut", "statut", statut_);
            p.setOperation("=");
            p.setPredicat("AND");
            if (!params.contains(p)) {
                params.add(p);
            } else {
                params.set(params.indexOf(p), p);
            }
        } else {
            params.remove(p);
        }
        loadAllTransfert();
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("cloturer");
        if (cloturer_ != null) {
            p = new ParametreRequete("cloturer", "cloturer", cloturer_);
            p.setOperation("=");
            p.setPredicat("AND");
            if (!params.contains(p)) {
                params.add(p);
            } else {
                params.set(params.indexOf(p), p);
            }
        } else {
            params.remove(p);
        }
        loadAllTransfert();
    }

    public void chooseDateSearch() {
        ParametreRequete p = new ParametreRequete("dateDoc");
        if (date_) {
            p = new ParametreRequete("dateDoc", "dateDoc", dateDebut_);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin_);
            if (!params.contains(p)) {
                params.add(p);
            } else {
                params.set(params.indexOf(p), p);
            }
        } else {
            params.remove(p);
        }
        loadAllTransfert();
    }

    public void chooseDestSearch() {
        ParametreRequete p = new ParametreRequete("destination");
        if (destSearch > 0) {
            p = new ParametreRequete("destination", "destination", new YvsBaseDepots(destSearch));
            p.setOperation("=");
            p.setPredicat("AND");
            if (!params.contains(p)) {
                params.add(p);
            } else {
                params.set(params.indexOf(p), p);
            }
        } else {
            params.remove(p);
        }
        loadAllTransfert();
    }

    public void chooseSourceSearch() {
        ParametreRequete p = new ParametreRequete("source");
        if (destSearch > 0) {
            p = new ParametreRequete("source", "source", new YvsBaseDepots(sourceSearch));
            p.setOperation("=");
            p.setPredicat("AND");
            if (!params.contains(p)) {
                params.add(p);
            } else {
                params.set(params.indexOf(p), p);
            }
        } else {
            params.remove(p);
        }
        loadAllTransfert();
    }

    public void initTransfertStock(YvsComDocStocks y) {
        selectDoc_ = y;
        initTransfertStock();
    }

    public void initTransfertStock() {
        creneauxDestination.clear();
        creneauxSource.clear();
        docStock.getContenus().clear();
        contenus_.clear();

        if ((selectDoc_ != null) ? selectDoc_.getId() > 0 : false) {
            selectDoc_.setContenus(dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", new String[]{"docStock"}, new Object[]{selectDoc_}));
            DocStock bean = UtilCom.buildBeanDocStock(selectDoc_);
            cloneObject(docStock, bean);
            populateViewO(bean);
            loadContenus_(selectDoc_);
            if (docSelect.getDocumentLie() == null) {
                docSelect.setDocumentLie(new DocStock());
            }
            docSelect.setDocumentLie(bean);
        }
        update("blog_form_transfert_stock_");
    }

    public void chooseArticle(YvsBaseArticles y) {
        if ((contenu.getArticle() != null) ? contenu.getArticle().getId() > 0 : false) {
            Articles t = UtilCom.buildBeanArticle(y);

            t.setStock_(dao.stocks(y.getId(), docStock.getCreneauDestinataire().getTranche().getId(), docStock.getDestination().getId(), 0, 0, docStock.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLotEntree().getId()));

            t.setStock(dao.stocks(y.getId(), docStock.getCreneauSource().getTranche().getId(), docStock.getSource().getId(), 0, 0, docStock.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLotSortie().getId()));
            t.setPuv(dao.getPuv(y.getId(), contenu.getQuantite(), 0, 0, docStock.getSource().getId(), 0, docStock.getDateDoc(), contenu.getConditionnement().getId()));
            contenu.setPrix(dao.getPr(y.getId(), docStock.getDestination().getId(), 0, docStock.getDateDoc(), contenu.getConditionnement().getId()));

            t.setPua(dao.getPua(y.getId(), 0));
            selectArt = true;
            cloneObject(contenu.getArticle(), t);
        } else {
            resetFicheContenu();
        }
    }

    public void searchArticle() {
        String num = contenu.getArticle().getRefArt();
        contenu.getArticle().setId(0);
        if (num != null ? num.trim().length() > 0 : false) {
//            searchArticles(num, docStock.getDestination().getId());
//            if (articlesResult != null ? !articlesResult.isEmpty() : false) {
//                if (articlesResult.size() > 1) {
//                    openDialog("dlgListArticle");
//                    update("data_articles_ordre_stock");
//                } else {
//                    YvsBaseArticles c = articlesResult.get(0);
//                    contenu.setArticle(UtilProd.buildSimpleBeanArticles(c));
//                    chooseArticle();
//                }
//                contenu.getArticle().setError(false);
//            } else {
//                contenu.getArticle().setDesignation("");
//                contenu.getArticle().setError(true);
//            }
        }
    }

    public void initArticles() {
//        articlesResult.clear();
//        articlesResult.addAll(articles);
        update("data_articles_ordre_stock");
    }

    public void annulerOrder() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            List<YvsComDocStocks> l = dao.loadNameQueries("YvsComDocStocks.findByDocumentLier", new String[]{"document"}, new Object[]{selectDoc});
            if (l != null ? l.isEmpty() : true) {
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
            } else {
                for (YvsComDocStocks d : l) {
                    if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                        getErrorMessage("Impossible d'annuler cet ordre car il possède un transfert déja valide");
                        return;
                    }
                }
                openDialog("dlgConfirmAnnuler");
            }
        }
    }

    public void annulerOrder_() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                List<YvsComDocStocks> l = dao.loadNameQueries("YvsComDocStocks.findByDocumentLier", new String[]{"document"}, new Object[]{selectDoc});
                for (YvsComDocStocks d : l) {
                    for (YvsComContenuDocStock c : d.getContenus()) {
                        dao.delete(c);
                    }
                }
                String rq = "DELETE FROM yvs_com_doc_stocks WHERE document_lie=?";
                Options[] lp = new Options[]{new Options(selectDoc.getId(), 1)};
                dao.requeteLibre(rq, lp);
                selectDoc.getDocuments().clear();
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
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            System.err.println("Erreur : " + ex.getMessage());
        }
    }

    public void refuserOrder() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            List<YvsComDocStocks> l = dao.loadNameQueries("YvsComDocStocks.findByDocumentLier", new String[]{"document"}, new Object[]{selectDoc});
            if (l != null ? l.isEmpty() : true) {
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
            } else {
                for (YvsComDocStocks d : l) {
                    if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                        getErrorMessage("Impossible d'annuler cet ordre car il possède un transfert déja valide");
                        return;
                    }
                }
                openDialog("dlgConfirmRefuser");
            }
        }
    }

    public void refuserOrder_() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                List<YvsComDocStocks> l = dao.loadNameQueries("YvsComDocStocks.findByDocumentLier", new String[]{"document"}, new Object[]{selectDoc});
                for (YvsComDocStocks d : l) {
                    for (YvsComContenuDocStock c : d.getContenus()) {
                        dao.delete(c);
                    }
                }
                String rq = "DELETE FROM yvs_com_doc_stocks WHERE document_lie=?";
                Options[] lp = new Options[]{new Options(selectDoc.getId(), 1)};
                dao.requeteLibre(rq, lp);
                selectDoc.getDocuments().clear();
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
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            System.err.println("Erreur : " + ex.getMessage());
        }
    }

    public void validerOrder() {
        if (selectDoc == null) {
            return;
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

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        docStock.setCloturer(!docStock.isCloturer());
        selectDoc.setCloturer(docStock.isCloturer());
        selectDoc.setDateCloturer(docStock.isCloturer() ? new Date() : null);
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        transferts.set(transferts.indexOf(selectDoc), selectDoc);
        if (transfertsHist.contains(selectDoc)) {
            transfertsHist.set(transfertsHist.indexOf(selectDoc), selectDoc);
            update("data_ordre_transfert_hist");
        }
        succes();
        update("data_ordre_transfert");
    }

    public void transmisOrder() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            initTransfertStock();
            saveAll();
        }
    }

    public boolean changeStatut_(String etat) {
        if (!etat.equals("") && selectDoc != null) {
            if (!_controleFiche(docStock)) {
                return false;
            }
            String rq = "UPDATE yvs_com_doc_stocks SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(docStock.getId(), 1)};
            dao.requeteLibre(rq, param);
            docStock.setStatut(etat);
            selectDoc.setStatut(etat);
            transferts.set(transferts.indexOf(selectDoc), selectDoc);
            if (!etat.equals(Constantes.ETAT_EDITABLE)) {
                if (transfertsHist.contains(selectDoc)) {
                    transfertsHist.remove(transfertsHist.indexOf(selectDoc));
                    update("data_ordre_transfert_hist");
                }
            } else {
                if (!transfertsHist.contains(selectDoc)) {
                    transfertsHist.add(0, selectDoc);
                    update("data_ordre_transfert_hist");
                }
            }
            update("data_ordre_transfert");
            update("infos_document_order_transfert");
            update("form_entete_order_transfert");
            return true;
        }
        return false;
    }

    public boolean changeStatut(String etat) {
        if (changeStatut_(etat)) {
            succes();
            return true;
        }
        return false;
    }

    public void searchTranferts() {
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            if (currentUser.getUsers().getAccesMultiAgence()) {
                champ = new String[]{"societe", "typeDoc", "numDoc"};
                val = new Object[]{currentAgence.getSociete(), Constantes.TYPE_OT, "%" + numSearch_ + "%"};
                nameQueri = "YvsComDocStocks.findByReference_";
            } else {
                if (currentUser.getUsers().getSuperAdmin()) {
                    champ = new String[]{"agence", "typeDoc", "numDoc"};
                    val = new Object[]{currentAgence, Constantes.TYPE_OT, "%" + numSearch_ + "%"};
                    nameQueri = "YvsComDocStocks.findByAgenceDestinationReference";
                } else {
                    champ = new String[]{"destination", "typeDoc", "numDoc"};
                    val = new Object[]{currentDepot, Constantes.TYPE_OT, "%" + numSearch_ + "%"};
                    nameQueri = "YvsComDocStocks.findByDestinationReference";
                }
            }
            transferts = dao.loadNameQueries(nameQueri, champ, val, idebut, ifin);
        } else {
            loadAllTransfert();
        }
        update("data_ordre_transfert");
        update("infos_document_order_transfert");
    }

    public void removeDoublon(YvsComDocStocks y) {
        selectDoc_ = y;
        removeDoublon();
    }

    public void removeDoublon() {
        if ((selectDoc_ != null) ? selectDoc_.getId() > 0 : false) {
            if (!selectDoc_.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre editable pour pouvoir etre modifié");
                return;
            }
            removeDoublonStock(selectDoc_.getId());
            succes();
            selectDoc_ = new YvsComDocStocks();
        }
    }

    @Override
    public void cleanStock() {
        super.cleanStock();
        loadAllTransfert();
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
