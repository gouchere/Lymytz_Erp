/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.UtilCompta;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.ManagedArticles;
import yvs.base.tiers.Tiers;
import yvs.commercial.UtilCom;
import yvs.dao.Options;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.util.Constantes;
import yvs.util.Util;
import yvs.commercial.ManagedCommercial;
import static yvs.commercial.ManagedCommercial.currentParam;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.commercial.param.ManagedTypeDocDivers;
import yvs.commercial.param.TypeDocDivers;
import yvs.commercial.stock.CoutSupDoc;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseTypeDocCategorie;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreAchat;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.grh.presence.TrancheHoraire;
import yvs.production.UtilProd;
import static yvs.util.Managed.ldf;
import static yvs.util.Managed.time;
import yvs.util.ParametreRequete;
import yvs.util.enume.Nombre;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedBonRetourAchat extends ManagedCommercial<DocAchat, YvsComDocAchats> implements Serializable {

    private DocAchat docAchat = new DocAchat();
    private DocAchat docSelect = new DocAchat();
    private DocAchat facture = new DocAchat();
    private List<YvsComDocAchats> documents;
    private YvsComDocAchats selectDoc;

    private List<YvsComContenuDocAchat> contenus, contenus_fa, selectContenus;
    private YvsComContenuDocAchat selectContenu;
    private ContenuDocAchat contenu = new ContenuDocAchat();
    private ContenuDocAchat contenu_fa = new ContenuDocAchat();

    private List<YvsGrhTrancheHoraire> tranches_retour;
    private List<YvsBaseDepots> depots_retour;

    private String tabIds, tabIds_article, type = Constantes.TYPE_FAA;
    private boolean existFacture, update, selectArt;
    private boolean recalculTaxe, memoriserDeleteContenu = false;

    //Parametre recherche
    private boolean date = false;
    private Date dateDebut = new Date(), dateFin = new Date(), dateGenerer = new Date();
    private String statut = null, numFacture, fournisseurF, typeGenerer;
    private long livreur_, trancheSearch, depotSearch, agenceSearch;

    public ManagedBonRetourAchat() {
        documents = new ArrayList<>();
        contenus = new ArrayList<>();
        contenus_fa = new ArrayList<>();
        tranches_retour = new ArrayList<>();
        depots_retour = new ArrayList<>();
        selectContenus = new ArrayList<>();
    }

    public List<YvsComContenuDocAchat> getSelectContenus() {
        return selectContenus;
    }

    public void setSelectContenus(List<YvsComContenuDocAchat> selectContenus) {
        this.selectContenus = selectContenus;
    }

    public boolean isMemoriserDeleteContenu() {
        return memoriserDeleteContenu;
    }

    public void setMemoriserDeleteContenu(boolean memoriserDeleteContenu) {
        this.memoriserDeleteContenu = memoriserDeleteContenu;
    }

    public String getTypeGenerer() {
        return typeGenerer;
    }

    public void setTypeGenerer(String typeGenerer) {
        this.typeGenerer = typeGenerer;
    }

    public Date getDateGenerer() {
        return dateGenerer;
    }

    public void setDateGenerer(Date dateGenerer) {
        this.dateGenerer = dateGenerer;
    }

    public long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public String getFournisseurF() {
        return fournisseurF;
    }

    public void setFournisseurF(String fournisseurF) {
        this.fournisseurF = fournisseurF;
    }

    public List<YvsBaseDepots> getDepots_retour() {
        return depots_retour;
    }

    public void setDepots_retour(List<YvsBaseDepots> depots_retour) {
        this.depots_retour = depots_retour;
    }

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public List<YvsGrhTrancheHoraire> getTranches_retour() {
        return tranches_retour;
    }

    public void setTranches_retour(List<YvsGrhTrancheHoraire> tranches_retour) {
        this.tranches_retour = tranches_retour;
    }

    public ContenuDocAchat getContenu_fa() {
        return contenu_fa;
    }

    public void setContenu_fa(ContenuDocAchat contenu_fa) {
        this.contenu_fa = contenu_fa;
    }

    public String getNumFacture() {
        return numFacture;
    }

    public void setNumFacture(String numFacture) {
        this.numFacture = numFacture;
    }

    public long getLivreur_() {
        return livreur_;
    }

    public void setLivreur_(long livreur_) {
        this.livreur_ = livreur_;
    }

    public long getDepotReceptionSearch() {
        return depotSearch;
    }

    public void setDepotReceptionSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public long getTrancheSearch() {
        return trancheSearch;
    }

    public void setTrancheSearch(long trancheSearch) {
        this.trancheSearch = trancheSearch;
    }

    public boolean isDate() {
        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public YvsComDocAchats getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocAchats selectDoc) {
        this.selectDoc = selectDoc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public YvsComContenuDocAchat getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComContenuDocAchat selectContenu) {
        this.selectContenu = selectContenu;
    }

    public List<YvsComContenuDocAchat> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocAchat> contenus) {
        this.contenus = contenus;
    }

    public List<YvsComContenuDocAchat> getContenus_fa() {
        return contenus_fa;
    }

    public void setContenus_fa(List<YvsComContenuDocAchat> contenus_fa) {
        this.contenus_fa = contenus_fa;
    }

    public boolean isExistFacture() {
        return existFacture;
    }

    public void setExistFacture(boolean existFacture) {
        this.existFacture = existFacture;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public DocAchat getFacture() {
        return facture;
    }

    public void setFacture(DocAchat facture) {
        this.facture = facture;
    }

    public DocAchat getDocSelect() {
        return docSelect;
    }

    public void setDocSelect(DocAchat docSelect) {
        this.docSelect = docSelect;
    }

    public ContenuDocAchat getContenu() {
        return contenu;
    }

    public void setContenu(ContenuDocAchat contenu) {
        this.contenu = contenu;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public DocAchat getDocAchat() {
        return docAchat;
    }

    public void setDocAchat(DocAchat docAchat) {
        this.docAchat = docAchat;
    }

    public List<YvsComDocAchats> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocAchats> documents) {
        this.documents = documents;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public boolean isRecalculTaxe() {
        return recalculTaxe;
    }

    public void setRecalculTaxe(boolean recalculTaxe) {
        this.recalculTaxe = recalculTaxe;
    }

    @Override
    public void loadAll() {
        _load();
        initView();
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
            loadAllFacture(true, true);
        }
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllFacture(true, true);
    }

    public void loadAll(String type) {
        this.type = type;
        if (!docAchat.getTypeDoc().equals(type)) {
            resetFiche();
        }
        docAchat.setTypeDoc(type);
        if (agenceSearch < 1) {
            agenceSearch = currentAgence.getId();
        }
        loadAll();
    }

    public void initView() {
        loadInfosWarning(false);
        if (facture != null ? facture.getId() < 1 : false) {
//            loadContenuFacture(new YvsComDocAchats(facture), false);
        }
        if (((docAchat != null) ? docAchat.getFournisseur().getId() < 1 : true)) {
            docAchat = new DocAchat();
            if (docAchat.getDocumentLie() == null) {
                docAchat.setDocumentLie(new DocAchat());
            }
            numSearch_ = "";
            docAchat.setTypeDoc(type);
        }
        if (docAchat.getTypeDoc() != null ? docAchat.getTypeDoc().trim().length() < 1 : true) {
            docAchat.setTypeDoc(Constantes.TYPE_FAA);
        }
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        if (currentParamAchat == null) {
            currentParamAchat = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        update("txt_indice_num_search");
    }

    public void loadAllFacture(boolean avance, boolean init) {
        //choisir les documents à charger
        ParametreRequete p;
        switch (buildDocByDroit(this.type)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND");
                paginator.addParam(p);
                break;
            case 3: //charge tous les document des points de achat où l'utilisateurs est responsable
                //cherche les points de achat de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.depotReception.id", "ids", ids, " IN ", "AND");
                paginator.addParam(p);
                break;
            default:    //charge les document de l'utilisateur connecté dans les restriction de paramDate données
                p = new ParametreRequete("y.author.users ", "users", currentUser.getUsers(), "=", "AND");
                paginator.addParam(p);
                break;
        }
        p = new ParametreRequete("y.typeDoc", "typeDoc", type, "=", "AND");
        paginator.addParam(p);
        documents = paginator.executeDynamicQuery("y", "y", "YvsComDocAchats y JOIN FETCH y.depotReception JOIN FETCH y.fournisseur LEFT JOIN FETCH y.documentLie JOIN FETCH y.agence", "y.dateDoc DESC, y.numDoc DESC", avance, init, (int) imax, dao);
        if (type.equals(Constantes.TYPE_FAA)) {
            update("data_avoir_achat");
        } else {
            update("data_retour_achat");
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComDocAchats> re = paginator.parcoursDynamicData("YvsComDocAchats y JOIN FETCH y.depotReception JOIN FETCH y.fournisseur JOIN FETCH y.documentLie JOIN FETCH y.agence", "y", "", "y.dateDoc DESC, y.numDoc DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadContenuFacture(YvsComDocAchats y, boolean facture) {
        if (y != null ? y.getId() > 0 : false) {
            long id = y.getId();
            String state = y.getStatut();
            String state_livre = y.getStatutLivre();
            if (facture) {
                String num = y.getNumDoc();
                YvsComDocAchats d = new YvsComDocAchats(y);
                d.setDateDoc(new Date());
                d.setDateLivraison(new Date());
                d.setDocumentLie(new YvsComDocAchats(id, num, state));
                d.getDocumentLie().setStatutLivre(state_livre);
                d.setTypeDoc(docAchat.getTypeDoc());
                d.setNumDoc(null);
                d.setNew_(true);
                d.setUpdate(false);
                d.setStatut(Constantes.ETAT_EDITABLE);
                d.setDateSave(new Date());
                if (docAchat.getTypeDoc().equals(Constantes.TYPE_BRA)) {
                    d.setNumPiece("BRA N° " + num);
                    d.setDescription("Bon Retour de la facture N° " + num + " le " + ldf.format(new Date()) + " à " + time.format(new Date()));
                } else {
                    d.setNumPiece("FAA N° " + num);
                    d.setDescription("Facture Avoir de la facture N° " + num + " le " + ldf.format(new Date()) + " à " + time.format(new Date()));
                }
                d.setId((long) -1);
                cloneObject(docAchat, UtilCom.buildBeanDocAchat(d));
                docAchat.setUpdate(false);
                setMontantTotalDoc(docAchat);
            }
            contenus_fa = loadContenus(new YvsComDocAchats(id));
            if (type.equals(Constantes.TYPE_FAA)) {
                update("data_contenu_fa_faa");
                update("infos_document_avoir_achat");
            } else {
                update("data_contenu_fa_bra");
                update("infos_document_retour_achat");
            }
        }
    }

    public void loadAllTrancheLivraison(YvsBaseDepots depot, Date date) {
        tranches_retour = loadTranche(depot, date);
        update("select_tranche_retour_achat");
    }

    public void init(boolean next) {
        loadAllFacture(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllFacture(true, true);
    }

    @Override
    public DocAchat recopieView() {
        docAchat.setTypeDoc(docAchat.getTypeDoc());
        docAchat.setStatut(docAchat.isUpdate() ? docAchat.getStatut() : Constantes.ETAT_EDITABLE);
        docAchat.setNew_(true);
        return docAchat;
    }

    public ContenuDocAchat recopieViewContenu(DocAchat doc) {
        contenu.setDocAchat(doc);
        contenu.setNew_(true);
        return contenu;
    }

    @Override
    public boolean controleFiche(DocAchat bean) {
        if (!_controleFiche_(bean)) {
            return false;
        }
        if (bean.isFromFacture() ? !bean.getDocumentLie().getStatut().equals(Constantes.ETAT_VALIDE) : false) {
            getWarningMessage("La facture n'est pas validé");
        }
        if (bean.getTypeDoc().equals(Constantes.TYPE_BRA)) {
            if ((bean.getDepotReception() != null) ? bean.getDepotReception().getId() < 1 : true) {
                getErrorMessage("Vous devez preciser le dépot");
                return false;
            }
            if ((bean.getTranche() != null) ? bean.getTranche().getId() < 1 : true) {
                getErrorMessage("Vous devez preciser la tranche");
                return false;
            }
        }
        //modifie le numéro de document si la date change   
        if ((selectDoc != null ? (selectDoc.getId() > 0 ? !bean.getDateDoc().equals(selectDoc.getDateDoc()) : false) : false)
                || (bean.getNumDoc() == null || bean.getNumDoc().trim().length() < 1)) {
            String titre = Constantes.TYPE_FAA_NAME;
            if (bean.getTypeDoc().equals(Constantes.TYPE_BRA)) {
                titre = Constantes.TYPE_BRA_NAME;
            }
            String ref = genererReference(titre, bean.getDateDoc(), bean.getDepotReception().getId());
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
            bean.setNumDoc(ref);
        }
        return true;
    }

    private boolean _controleFiche_(DocAchat bean) {
        if (bean == null) {
            getErrorMessage("Le devez selectionner un document");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document a été verouillé !");
            return false;
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean _controleFiche_(YvsComDocAchats bean) {
        if (bean == null) {
            getErrorMessage("vous devez selectionner un document");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.getCloturer()) {
            getErrorMessage("Ce document a été verouillé");
            return false;
        }
//        return writeInExercice(bean.getDateDoc()); 
        return true;
    }

    public boolean controleFicheContenu(ContenuDocAchat bean) {
        if (bean.getDocAchat() != null ? !bean.getDocAchat().isUpdate() : true) {
            if (!_saveNew()) {
                return false;
            }
            bean.setDocAchat(docAchat);
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if (docAchat.getTypeDoc().equals(Constantes.TYPE_BRA) ? bean.getArticle().isRequiereLot() && (bean.getLot() != null ? bean.getLot().getId() < 1 : true) : false) {
            getErrorMessage("Un numéro de lot est requis pour cet article dans ce dépôt");
            return false;
        }
        if (docAchat.getTypeDoc().equals(Constantes.TYPE_FAA)) {
            if (bean.getPrixAchat() == 0 && bean.getRemiseRecu() == 0 && bean.getTaxe() == 0) {
                getErrorMessage("L'avoir doit porter sur un élément comptable");
                return false;
            }
        }
        return _controleFiche_(bean.getDocAchat());
    }

    public boolean controleFicheCout(CoutSupDoc bean) {
        if (bean.getDoc() < 1) {
            if (!_saveNew()) {
                return false;
            }
            bean.setDoc(docAchat.getId());
        }
        if ((bean.getType() != null) ? bean.getType().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le type de coût");
            return false;
        }
        if (bean.getMontant() < 1) {
            getErrorMessage("Vous devez entrer un montant");
            return false;
        }
        return _controleFiche_(docAchat);
    }

    @Override
    public void populateView(DocAchat bean) {
        cloneObject(docAchat, bean);
        docAchat.setFromFacture(bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false);
        docAchat.setDateLivraison((docAchat.getDateLivraison() == null) ? docAchat.getDateDoc() : docAchat.getDateLivraison());
        setMontantTotalDoc(docAchat);
    }

    @Override
    public void onSelectDistant(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            this.type = y.getTypeDoc();
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView(this.type.equals(Constantes.TYPE_FAA) ? "Factures Avoir Achat" : "Bon Retour Achat", this.type.equals(Constantes.TYPE_FAA) ? "modCompta" : "modGescom", this.type.equals(Constantes.TYPE_FAA) ? "smenFactureAvoirAchat" : "smenFactureRetourAchat", true);
            }
        }
    }

    public void populateViewArticle(ContenuDocAchat bean) {
        bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docAchat.getDepotReception().getId(), 0, 0, docAchat.getDateLivraison(), bean.getConditionnement().getId(), bean.getLot().getId()));
        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
        cloneObject(contenu, bean);
        if (bean.getParent() != null ? bean.getParent().getId() > 0 : false) {
            YvsComContenuDocAchat c = (YvsComContenuDocAchat) dao.loadOneByNameQueries("YvsComContenuDocAchat.findById", new String[]{"id"}, new Object[]{bean.getParent().getId()});
            cloneObject(contenu_fa, UtilCom.buildBeanContenuDocAchat(c));
            contenu.setQuantiteCommende(c.getQuantiteRecu());
            contenu.setRemiseRecu(c.getRemise());
            contenu.setTaxeRecu(c.getTaxe());
            contenu.setPrixTotalAttendu(c.getPrixTotal());
        }
        String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
        Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(bean.getArticle().getId(), 1), new Options(docAchat.getDepotReception().getId(), 2)});
        contenu.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
        if (contenu.getArticle().isRequiereLot()) {
            champ = new String[]{"typeDoc", "parent", "conditionnement"};
            val = new Object[]{Constantes.TYPE_BLA, new YvsComDocAchats(docAchat.getDocumentLie().getId()), new YvsBaseConditionnement(bean.getConditionnement().getId())};
            contenu.setLots(dao.loadNameQueries("YvsComContenuDocAchat.findLotByArticleParent", champ, val));
        }
        selectArt = true;
    }

    @Override
    public void resetFiche() {
        docAchat = new DocAchat();
        docAchat.setDocumentLie(new DocAchat());
        docAchat.setStatut(Constantes.ETAT_EDITABLE);
        docAchat.setTypeDoc(type);

        facture = new DocAchat();
        tabIds = "";
        update = false;
        contenus.clear();
        contenus_fa.clear();
        selectDoc = null;
        selectDoc = null;

        resetFicheArticle();
        if (type.equals(Constantes.TYPE_FAA)) {
            update("blog_form_avoir_achat");
        } else {
            update("blog_form_retour_achat");
        }
    }

    public void resetFicheArticle() {
        resetFiche(contenu);
        contenu.setArticle(new Articles());
        contenu.setDocAchat(new DocAchat());
        contenu_fa = new ContenuDocAchat();
        selectArt = false;
        tabIds_article = "";
        selectContenu = null;
        if (type.equals(Constantes.TYPE_FAA)) {
            update("form_contenu_avoir_achat");
        } else {
            update("form_contenu_retour_achat");
        }
    }

    public void resetFicheArticle_() {
        resetFiche(contenu);
        contenu.setArticle(new Articles());
        contenu.setDocAchat(new DocAchat());
        tabIds_article = "";
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
            DocAchat bean = recopieView();
            if (controleFiche(bean)) {
                selectDoc = UtilCom.buildDocAchat(bean, currentUser, currentAgence);
                if (!bean.isUpdate()) {
                    selectDoc.setDateLivraison(null);
                    selectDoc.setId(null);
                    selectDoc.getContenus().clear();
                    selectDoc = (YvsComDocAchats) dao.save1(selectDoc);
                    docAchat.setId(selectDoc.getId());
                    documents.add(0, selectDoc);
                    if (documents.size() > imax) {
                        documents.remove(documents.size() - 1);
                    }
                } else {
                    dao.update(selectDoc);
                    documents.set(documents.indexOf(selectDoc), selectDoc);
                }
                setMontantTotalDoc(docAchat);
                docAchat.setNumDoc(bean.getNumDoc());
                docAchat.setUpdate(true);
                if (type.equals(Constantes.TYPE_FAA)) {
                    update("data_avoir_achat");
                    update("form_entete_avoir_achat");
                } else {
                    update("data_retour_achat");
                    update("form_entete_retour_achat");
                }
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
            return false;
        }
    }

    public void saveNewArticle() {
        try {
            ContenuDocAchat bean = recopieViewContenu(docAchat);
            if (controleFicheContenu(bean)) {
                YvsComContenuDocAchat en = UtilCom.buildContenuDocAchat(bean, currentUser);
                en.setQuantiteRecu(en.getQuantiteCommande());
                if (bean.getId() <= 0) {
                    en.setId(null);
                    en = (YvsComContenuDocAchat) dao.save1(en);
                    //save taxes
                    saveAllTaxe(en, docAchat, en.getDocAchat(), docAchat.getCategorieComptable().getId(), true);
                    contenu.setId(en.getId());
                    contenus.add(0, en);
                } else {
                    dao.update(en);
                    int idx = contenus.indexOf(en);
                    if (idx >= 0) {
                        contenus.set(idx, en);
                    }
                }
                resetFicheArticle();
                succes();
                if (type.equals(Constantes.TYPE_FAA)) {
                    update("data_avoir_achat");
                } else {
                    update("data_retour_achat");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser(type.equals(Constantes.TYPE_BRA) ? "bra_delete_doc" : "faa_delete_doc")) {
                openNotAcces();
                return;
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComDocAchats> list = new ArrayList<>();
                YvsComDocAchats bean;
                for (Long ids : l) {
                    bean = documents.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    list.add(bean);
                    bean.setDateUpdate(new Date());
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(bean);
                    if (docAchat.getId() == bean.getId()) {
                        resetFiche();
                        update("blog_form_retour_achat");
                    }
                }
                documents.removeAll(list);
                succes();
                resetFiche();
                if (type.equals(Constantes.TYPE_FAA)) {
                    update("data_avoir_achat");
                } else {
                    update("data_retour_achat");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
        tabIds = "";

    }

    public void deleteBean_(YvsComDocAchats y) {
        selectDoc = y;
    }

    public boolean deleteBean_() {
        try {
            if (!autoriser(type.equals(Constantes.TYPE_BRA) ? "bra_delete_doc" : "faa_delete_doc")) {
                openNotAcces();
                return false;
            }
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc)) {
                    return false;
                }
                dao.delete(selectDoc);
                documents.remove(selectDoc);
                if (docAchat.getId() == selectDoc.getId()) {
                    resetFiche();
                    update("blog_form_retour_achat");
                }
                if (type.equals(Constantes.TYPE_FAA)) {
                    update("data_avoir_achat");
                } else {
                    update("data_retour_achat");
                }
                succes();
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
        return false;
    }

    public void deleteBeanArticle() {
        try {
            if ((tabIds_article != null) ? !tabIds_article.equals("") : false) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                String[] tab = tabIds_article.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComContenuDocAchat bean = contenus.get(contenus.indexOf(new YvsComContenuDocAchat(id)));
                    dao.delete(bean);
                    contenus.remove(bean);
                    if (contenu.getId() == id) {
                        resetFicheArticle();
                    }
                }
                succes();
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
                for (YvsComContenuDocAchat c : selectContenus) {
                    dao.delete(c);
                    contenus.remove(c);
                    if (contenu.getId() == c.getId()) {
                        resetFicheArticle();
                    }
                }
                succes();
            }
        } else {
            openDialog("dlgConfirmDeleteArticles");
        }
    }

    public void deleteBeanArticle_(YvsComContenuDocAchat y) {
        selectContenu = y;
    }

    public void deleteBeanArticle_() {
        System.err.println("supression -----");
        try {
            if (selectContenu != null) {
                if (!_controleFiche_(selectDoc)) {
                    getErrorMessage("Suppression Impossible !");
                    return;
                }
                dao.delete(selectContenu);

                contenus.remove(selectContenu);
                if (contenu.getId() == selectContenu.getId()) {
                    resetFicheArticle();
                }
                succes();
                update("data_contenu_avoir_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void onSelectObject(YvsComDocAchats y, String type) {
        this.type = type;
        onSelectObject(y);
    }

    @Override
    public void onSelectObject(YvsComDocAchats y) {
        facture = new DocAchat();
        selectDoc = y;
        selectDoc.setUpdate(true);
        populateView(UtilCom.buildBeanDocAchat(selectDoc));
        resetFicheArticle();
        contenus = loadContenus(y);
        if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getId() > 0 : false) {
            champ = new String[]{"id"};
            val = new Object[]{selectDoc.getDocumentLie().getId()};
            YvsComDocAchats d_ = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", champ, val);
            if (d_ != null ? d_.getId() > 0 : false) {
                facture = UtilCom.buildBeanDocAchat(d_);
                loadContenuFacture(d_, false);
            }
            ManagedFactureAchat m = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (m != null) {
                if (!m.getDocuments().subList(0, m.getSubLenght()).contains(selectDoc.getDocumentLie())) {
                    m.getDocuments().remove(selectDoc.getDocumentLie());
                    m.getDocuments().add(0, selectDoc.getDocumentLie());
                }
            }
            depots_retour = dao.loadNameQueries("YvsComDocAchats.findDepotByParentType", new String[]{"documentLie", "type"}, new Object[]{d_, Constantes.TYPE_BLA});
            if (y.getDepotReception() != null ? !depots_retour.contains(y.getDepotReception()) : false) {
                depots_retour.add(y.getDepotReception());
            }
        }
        if (!docAchat.isFromFacture()) {
            chooseDepot();
        }
        if (type.equals(Constantes.TYPE_FAA)) {
            update("blog_form_avoir_achat");
            update("other_avoir_achat");
            update("data_contenu_fa_faa");
        } else {
            update("blog_form_retour_achat");
            update("other_retour_achat");
            update("data_contenu_fa_bra");
        }
    }

    public void onSelectDistantObject(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureAchat s = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocAchats y = (YvsComDocAchats) ev.getObject();
            onSelectObject(y);
            tabIds = documents.indexOf(y) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        if (type.equals(Constantes.TYPE_FAA)) {
            update("blog_form_avoir_achat");
        } else {
            update("blog_form_retour_achat");
        }
    }

    public void loadOnViewDoc(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocAchats bean = (YvsComDocAchats) ev.getObject();
            onSelectFacture(bean);
        }
    }

    public void loadOnViewEntete(SelectEvent ev) {

    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectContenu = (YvsComContenuDocAchat) ev.getObject();
            populateViewArticle(UtilCom.buildBeanContenuDocAchat(selectContenu));
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheArticle();
    }

    public void loadOnViewArticle(SelectEvent ev) {
        selectArt = false;
        if (docAchat.getDepotReception() != null ? docAchat.getDepotReception().getId() > 0 : false) {
            if ((ev != null) ? ev.getObject() != null : false) {
                YvsComContenuDocAchat bean = (YvsComContenuDocAchat) ev.getObject();
                cloneObject(contenu_fa, UtilCom.buildBeanContenuDocAchat(bean));
//                if (contenu.getId() <= 0) {
                cloneObject(contenu, contenu_fa);
                contenu.setId(-1);
//                } else {
//                    cloneObject(contenu.getArticle(), contenu_fa.getArticle());
//                }
                cloneObject(contenu.getParent(), contenu_fa);
                contenu.setQuantiteRecu(bean.getQuantiteRecu());
                contenu.setPrixAchat(bean.getPrixAchat());
                contenu.setRemiseRecu(0);
                contenu.setTaxe(bean.getTaxe());
                recalculTaxe = true;
                contenu.setPrixTotalRecu(bean.getPrixTotal());
                if (docAchat.getDepotReception() != null ? docAchat.getDepotReception().getId() > 0 : false) {
                    contenu.getArticle().setStock(dao.stocks(contenu.getArticle().getId(), 0, docAchat.getDepotReception().getId(), 0, 0, docAchat.getDateLivraison(), contenu.getConditionnement().getId(), contenu.getLot().getId()));

                    String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                    Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(bean.getArticle().getId(), 1), new Options(docAchat.getDepotReception().getId(), 2)});
                    contenu.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
                    if (contenu.getArticle().isRequiereLot()) {
                        champ = new String[]{"typeDoc", "parent", "conditionnement"};
                        val = new Object[]{Constantes.TYPE_BLA, new YvsComDocAchats(docAchat.getDocumentLie().getId()), bean.getConditionnement()};
                        contenu.setLots(dao.loadNameQueries("YvsComContenuDocAchat.findLotByArticleParent", champ, val));
                    }
                } else {
                    contenu.getArticle().setStock(dao.stocks(contenu.getArticle().getId(), 0, 0, currentAgence.getId(), 0, docAchat.getDateLivraison(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
                }
                contenu.getArticle().setPua(dao.getPua(contenu.getArticle().getId(), docAchat.getFournisseur().getId()));
                selectArt = true;
                contenu.setDateContenu(new Date());
                contenu.setParent(new ContenuDocAchat(bean.getId()));
            }
        } else {
            getErrorMessage("Vous devez preciser le depot de livraison");
            resetFicheArticle();
        }
        if (type.equals(Constantes.TYPE_FAA)) {
            update("desc_contenu_avoir_achat");
            update("blog_form_article_avoir_achat");
        } else {
            update("desc_contenu_retour_achat");
            update("blog_form_article_retour_achat");
        }
    }

    public void unLoadOnViewArticle(UnselectEvent ev) {
        if (contenu.getId() <= 0) {
            resetFicheArticle();
        } else {
            YvsComContenuDocAchat c = contenus.get(contenus.indexOf(new YvsComContenuDocAchat(contenu.getId())));
            cloneObject(contenu, UtilCom.buildBeanContenuDocAchat(c));
        }
        if (type.equals(Constantes.TYPE_FAA)) {
            update("blog_form_article_avoir_achat");
        } else {
            update("blog_form_article_retour_achat");
        }
    }

    public void actualiseAvoir() {
        cloneObject(docAchat.getFournisseur(), facture.getFournisseur());
        cloneObject(docAchat.getCategorieComptable(), facture.getCategorieComptable());
    }

    public void searchFacture() {
        String num = facture.getNumDoc();
        facture.setId(0);
        facture.setError(true);
        ManagedFactureAchat m = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
        if (m != null) {
            DocAchat y = m.searchFacture(num, null, true);
            if (m.getDocuments() != null ? !m.getDocuments().isEmpty() : false) {
                if (m.getDocuments().size() > 1) {
                    if (type.equals(Constantes.TYPE_FAA)) {
                        update("data_fa_avoir");
                    } else {
                        update("data_fa_retour");
                    }
                } else {
                    chooseDocAchat(y);
                }
                facture.setError(false);
            }
        }
    }

    public void loadOnViewFsseur(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseFournisseur y = (YvsBaseFournisseur) ev.getObject();
            chooseFsseur(UtilCom.buildBeanFournisseur(y));
        }
    }

    public void loadOnViewArticles(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean));
        }
    }

    public void loadOnViewConditionnement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseConditionnement bean = (YvsBaseConditionnement) ev.getObject();
            chooseConditionnement(UtilProd.buildBeanConditionnement(bean));
        }
    }

    public void chooseTypeAchat() {
        if (docAchat.getTypeAchat() != null ? docAchat.getTypeAchat().getId() > 0 : false) {
            ManagedTypeDocDivers w = (ManagedTypeDocDivers) giveManagedBean(ManagedTypeDocDivers.class);
            if (w != null) {
                int index = w.getTypesDocDivers().indexOf(new YvsBaseTypeDocDivers(docAchat.getTypeAchat().getId()));
                if (index > -1) {
                    YvsBaseTypeDocDivers y = w.getTypesDocDivers().get(index);
                    docAchat.setTypeAchat(UtilCompta.buildBeanTypeDoc(y));
                }
            }
        } else {
            docAchat.setTypeAchat(new TypeDocDivers());
        }
        update("infos_facture_achat");
    }

    public void searchFsseur() {
        String num = docAchat.getFournisseur().getCodeFsseur();
        docAchat.getFournisseur().setId(0);
        docAchat.getFournisseur().setError(true);
        docAchat.getFournisseur().setTiers(new Tiers());
        if ((num != null) ? !num.trim().isEmpty() : false) {
            ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            if (m != null) {
                Fournisseur y = m.searchFsseur(num, true);
                if (m.getFournisseurs() != null ? !m.getFournisseurs().isEmpty() : false) {
                    if (m.getFournisseurs().size() > 1) {
                        update("data_fournisseur_bra");
                    } else {
                        chooseFsseur(y);
                    }
                    docAchat.getFournisseur().setError(false);
                }
            }
        }
    }

    public void searchArticle() {
        List<String> categories = new ArrayList<>();
        if (docAchat.getTypeAchat() != null ? docAchat.getTypeAchat().getId() > 0 : false) {
            for (YvsBaseTypeDocCategorie c : docAchat.getTypeAchat().getCategories()) {
                categories.add(c.getCategorie());
            }
        }
        String num = contenu.getArticle().getRefArt();
        contenu.getArticle().setDesignation("");
        contenu.getArticle().setError(true);
        contenu.getArticle().setId(0);
        selectArt = false;
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedArticles m = (ManagedArticles) giveManagedBean("Marticle");
            if (m != null) {
                Articles y = m.searchArticleActif(categories, "A", num, true);
                if (m.getArticlesResult() != null ? !m.getArticlesResult().isEmpty() : false) {
                    if (m.getArticlesResult().size() > 1) {
                        if (type.equals(Constantes.TYPE_FAA)) {
                            update("data_articles_faa");
                        } else {
                            update("data_articles_bra");
                        }
                        update("data_articles_facture_achat");
                    } else {
                        chooseArticle(y);
                    }
                    contenu.getArticle().setError(false);
                } else {
                    Conditionnement c = m.searchArticleActifByCodeBarre(num);
                    if (m.getConditionnements() != null ? !m.getConditionnements().isEmpty() : false) {
                        if (m.getConditionnements().size() > 1) {
                            update("data_conditionnements_facture_achat");
                        } else {
                            chooseConditionnement(c);
                        }
                        selectArt = true;
                    }
                }
            }
        }
    }

    private void findInfosArticle(Conditionnement c) {
        if (c != null) {
            contenu.getArticle().setPua(dao.getPua(contenu.getArticle().getId(), docAchat.getFournisseur().getId(), c.getId()));
            contenu.getArticle().setLastPua(contenu.getArticle().getPua());
            if (docAchat.getDepotReception() != null ? docAchat.getDepotReception().getId() > 0 : false) {
                contenu.getArticle().setPr(dao.getPr(contenu.getArticle().getId(), docAchat.getDepotReception().getId(), 0, docAchat.getDateDoc(), c.getId()));
                contenu.getArticle().setDateLastMvt((Date) dao.loadObjectByNameQueries("YvsBaseArticleDepot.findLastDateByArticleDepot", new String[]{"article", "depot"}, new Object[]{new YvsBaseArticles(contenu.getArticle().getId()), new YvsBaseDepots(docAchat.getDepotReception().getId())}));
            } else {
                contenu.getArticle().setDateLastMvt(null);
            }

            if (contenu != null ? contenu.getId() < 1 : false) {
                contenu.setPrixAchat(contenu.getArticle().getPua());
            }
            contenu.setConditionnement(c);
            if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
                contenu.getArticle().setStock(dao.stocks(contenu.getArticle().getId(), 0, 0, 0, 0, docAchat.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
            }
            if (contenu.getQuantiteCommende() > 0) {
                onPrixBlur();
            }
        }
    }

    public void findPrixArticle(ContenuDocAchat c) {
        findPrixArticle(c, selectDoc);
    }

    public void findPrixArticle(ContenuDocAchat c, YvsComDocAchats doc) {
        findPrixArticle(c, selectContenu, doc, true);
    }

    public void findPrixArticle(ContenuDocAchat c, YvsComContenuDocAchat y, YvsComDocAchats doc, boolean msg) {
        double total = c.getQuantiteCommende() * c.getPrixAchat();
        double _remise = dao.getRemiseAchat(c.getArticle().getId(), c.getQuantiteCommende(), c.getPrixAchat(), docAchat.getFournisseur().getId());
        if (y != null ? y.getId() > 0 : false) {
            if (c.getId() > 0 && (y.getQuantiteRecu() == c.getQuantiteCommende())) {
                _remise = y.getRemise();
            }
        }

        c.setRemiseRecu(_remise);
        c.setPrixTotalRecu(total - c.getRemiseRecu());
        long categorie = returnCategorie(docAchat, doc);
        if (categorie > 0) {
            c.setTaxe(dao.getTaxe(c.getArticle().getId(), categorie, 0, c.getRemiseRecu(), c.getQuantiteCommende(), c.getPrixAchat(), false, docAchat.getFournisseur().getId()));
            String query = "SELECT pua_ttc FROM yvs_base_article_fournisseur WHERE article = ? AND fournisseur = ?";
            Boolean pua_ttc = (Boolean) dao.loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(c.getArticle().getId(), 1), new yvs.dao.Options(docAchat.getFournisseur().getId(), 2)});
            if (pua_ttc == null) {
                pua_ttc = c.getArticle().isPuaTtc();
            }
            c.setPrixTotalRecu(c.getPrixTotalRecu() + (pua_ttc ? 0 : c.getTaxe()));
        } else {
            if (msg) {
                getWarningMessage("Selectionner la catégorie comptable!");
            }
        }
    }

    private long returnCategorie(DocAchat docAchat, YvsComDocAchats selectDoc) {
        long categorie = 0;
        if (selectDoc != null ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
            if (selectDoc.getCategorieComptable() != null ? selectDoc.getCategorieComptable().getId() > 0 : false) {
                categorie = selectDoc.getCategorieComptable().getId();
            }
        } else {
            if (docAchat.getCategorieComptable() != null ? docAchat.getCategorieComptable().getId() > 0 : false) {
                categorie = docAchat.getCategorieComptable().getId();
            }
        }
        return categorie;
    }

    public void onPrixBlur() {
        findPrixArticle(contenu);
        contenu.setPrixTotalRecu(contenu.getPrixTotalRecu() > 0 ? contenu.getPrixTotalRecu() : 0);
        contenu.setPrixTotalAttendu(contenu.getPrixTotalRecu());
    }

    public void chooseArticle(Articles t) {
        if (((docAchat.getFournisseur() != null) ? docAchat.getFournisseur().getId() > 0 : false)) {
            if (!checkOperationArticle(t.getId(), docAchat.getDepotReception().getId(), Constantes.ACHAT)) {
                getWarningMessage("L'article '" + t.getDesignation() + "' ne fait pas d'achat dans le depot '" + docAchat.getDepotReception().getDesignation() + "'");
            }
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(t.getId())});
            t.setConditionnements(unites);
            t.setSelectArt(true);
            cloneObject(contenu.getArticle(), t);
            if (unites != null ? !unites.isEmpty() : false) {
                findInfosArticle(UtilProd.buildBeanConditionnement(unites.get(0)));
            } else {
                findInfosArticle(null);
            }
            selectArt = t.isSelectArt();
            String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
            Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(contenu.getArticle().getId(), 1), new Options(docAchat.getDepotReception().getId(), 2)});
            contenu.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
        } else {
            getErrorMessage("Vous devez selectionner le fournisseur");
            resetFicheArticle();
        }
        if (type.equals(Constantes.TYPE_FAA)) {
            update("form_contenu_avoir_achat");
        } else {
            update("form_contenu_retour_achat");
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
            findInfosArticle(contenu.getConditionnement());
            if (type.equals(Constantes.TYPE_FAA)) {
                update("form_contenu_avoir_achat");
            } else {
                update("form_contenu_retour_achat");
            }
        }
    }

    private void chooseConditionnement(Conditionnement c) {
        if (c != null ? c.getId() > 0 : false) {
            chooseArticle(c.getArticle());
            cloneObject(contenu.getConditionnement(), c);
            chooseConditionnement();
        }
    }

    public void chooseFsseur(Fournisseur d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(docAchat.getFournisseur(), d);
            if (d.getCategorieComptable() != null) {
                cloneObject(docAchat.getCategorieComptable(), d.getCategorieComptable());
            }
            if (docAchat.getFournisseur() != null ? docAchat.getFournisseur().getModel() != null : false) {
                docAchat.setModeReglement(docAchat.getFournisseur().getModel());
            }
        }
        update("infos_document_retour_achat");
    }

    public void setFournisseurDefaut() {
        if (docAchat.getFournisseur() != null ? docAchat.getFournisseur().getId() < 1 : true) {
            YvsBaseFournisseur c = currentFournisseurDefault();
            if (c != null ? c.getId() > 0 : false) {
                chooseFsseur(UtilCom.buildBeanFournisseur(c));
            }
            update("infos_facture_achat");
        }
    }

    public void changeSource() {
        if (!docAchat.isFromFacture()) {
            if (docAchat.getFournisseur() != null ? docAchat.getFournisseur().getId() < 1 : true) {
                setFournisseurDefaut();
            }
            ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (w != null) {
                depots_retour = new ArrayList<>(w.getDepots());
            }
        }
    }

    public void chooseDepot() {
        if ((docAchat.getDepotReception() != null) ? docAchat.getDepotReception().getId() > 0 : false) {
            if (docAchat.isFromFacture() && type.equals(Constantes.TYPE_BRA)) {
                int index = depots_retour.indexOf(new YvsBaseDepots(docAchat.getDepotReception().getId()));
                if (index > -1) {
                    YvsBaseDepots d = depots_retour.get(index);
                    cloneObject(docAchat.getDepotReception(), UtilCom.buildBeanDepot(d));
                    loadAllTranche(d, docAchat.getDateDoc());
                }
            } else {
                ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
                if (w != null) {
                    int index = w.getDepots().indexOf(new YvsBaseDepots(docAchat.getDepotReception().getId()));
                    if (index > -1) {
                        YvsBaseDepots d = w.getDepots().get(index);
                        cloneObject(docAchat.getDepotReception(), UtilCom.buildBeanDepot(d));
                        loadAllTranche(d, docAchat.getDateDoc());
                    }
                }
            }
        }
    }

    public void loadAllTranche(YvsBaseDepots depot, Date date) {
        if (docAchat.getDepotReception() != null ? depot != null ? docAchat.getDepotReception().getId() != depot.getId() : true : true) {
            docAchat.setTranche(new TrancheHoraire());
        }
        tranches_retour = loadTranche(depot, date);
        if (tranches_retour != null ? tranches_retour.size() == 1 : false) {
            if (docAchat.getTranche() != null ? docAchat.getTranche().getId() < 0 : false) {
                docAchat.setTranche(UtilCom.buildBeanTrancheHoraire(tranches_retour.get(0)));
            } else {
                if (!tranches_retour.contains(new YvsGrhTrancheHoraire(docAchat.getTranche().getId()))) {
                    docAchat.setTranche(UtilCom.buildBeanTrancheHoraire(tranches_retour.get(0)));
                }
            }
        }
        update("select_tranche_retour_achat");
    }

    public void chooseDocAchat(DocAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureAchat m = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (m != null) {
                int idx = m.getDocuments().indexOf(new YvsComDocAchats(y.getId()));
                if (idx > -1) {
                    YvsComDocAchats bean = m.getDocuments().get(idx);
                    onSelectFacture(new YvsComDocAchats(bean));
                }
            }
        } else {
            resetFiche();
        }
    }

    public void onSelectFacture(YvsComDocAchats y) {
        List<YvsComContenuDocAchat> list = new ArrayList<>();
        list.addAll(y.getContenus());
        facture = UtilCom.buildBeanDocAchat(y);
        loadContenuFacture(y, true);
        facture.setContenus(list);
        setMontantTotalDoc(docAchat);
        depots_retour = dao.loadNameQueries("YvsComDocAchats.findDepotByParentType", new String[]{"documentLie", "type"}, new Object[]{y, Constantes.TYPE_BLA});
        if (y.getDepotReception() != null ? !depots_retour.contains(y.getDepotReception()) : false) {
            depots_retour.add(y.getDepotReception());
        }
        chooseDepotLivraison();
    }

    public void chooseStatut(ValueChangeEvent ev) {
        statut_ = ((String) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null, "=", "AND");
        if (!(statut_ == null || statut_.trim().equals(""))) {
            p.setObjet(statut_);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void addParamLivreur(String str) {
        ParametreRequete p = new ParametreRequete(null, "livreur", "XXX", "=", "AND");
        if ((str != null) ? !str.trim().isEmpty() : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.livreur.codeUsers)", "livreur", str.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.livreur.nomUsers)", "livreur", str.trim().toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("y.cloturer", "cloturer", cloturer_);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadAllFacture(true, true);
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
        loadAllFacture(true, true);
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllFacture(true, true);
    }

    @Override
    public void _chooseAgence() {
        super._chooseAgence();
        ParametreRequete p;
        if (agence_ > 0) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agence_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.agence", "agence", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    @Override
    public void _chooseDepot() {
        super._chooseDepot();
        ParametreRequete p;
        if (depot_ > 0) {
            p = new ParametreRequete("y.depotReception", "depot", new YvsBaseDepots(depot_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.depotReception", "depot", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void _chooseTranche() {
        ParametreRequete p;
        if (tranche_ > 0) {
            p = new ParametreRequete("y.tranche", "tranche", new YvsGrhTrancheHoraire(tranche_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.tranche", "tranche", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void findByDate(boolean find) {
        ParametreRequete p = new ParametreRequete("y.dateDoc", "dateDoc", null, " BETWEEN ", "AND");
        if (find) {
            if (dateDebut != null && dateFin != null) {
                if (dateDebut.before(dateFin) || dateDebut.equals(dateFin)) {
                    p.setObjet(dateDebut);
                    p.setOtherObjet(dateFin);
                }
            }
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void clearsParams() {
        paginator.clear();
        loadAllFacture(true, true);
    }

    public void choixParamDate(ValueChangeEvent ev) {
        date_ = (boolean) ev.getNewValue();
        findByDate(date_);
    }

    public void addParamDate(SelectEvent ev) {
        findByDate(date_);
    }

    public void chooseFacture() {
        ParametreRequete p;
        if (numFacture != null ? numFacture.trim().length() > 0 : false) {
            p = new ParametreRequete("y.documentLie.numDoc", "numDoc", "%" + numFacture + "%", " LIKE ", "AND");
        } else {
            p = new ParametreRequete("y.documentLie.numDoc", "numDoc", null, " LIKE ", "AND");
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void chooseDepotLivr() {
        ParametreRequete p;
        if (depotSearch > 0) {
            p = new ParametreRequete("y.depotReception", "depotReception", new YvsBaseDepots(depotSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.depotReception", "depotReception", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void chooseLotReception() {
        if (contenu.getLot() != null ? contenu.getLot().getId() > 0 : false) {
            int idx = contenu.getLots().indexOf(new YvsComLotReception(contenu.getLot().getId()));
            if (idx > -1) {
                YvsComLotReception y = contenu.getLots().get(idx);
                contenu.setLot(UtilCom.buildBeanLotReception(y));
            }
        }
    }

    public void chooseTrancheLivraison() {
        if (docAchat.getTranche() != null ? docAchat.getTranche().getId() > 0 : false) {
            int idx = tranches_retour.indexOf(new YvsGrhTrancheHoraire(docAchat.getTranche().getId()));
            if (idx > -1) {
                YvsGrhTrancheHoraire y = tranches_retour.get(idx);
                cloneObject(docAchat.getTranche(), UtilCom.buildBeanTrancheHoraire(y));
            }
        }
    }

    public void chooseDepotLivraison() {
        if (docAchat.getDepotReception() != null ? docAchat.getDepotReception().getId() > 0 : false) {
            YvsBaseDepots y = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{docAchat.getDepotReception().getId()});
            cloneObject(docAchat.getDepotReception(), UtilCom.buildBeanDepot(y));
            if (!verifyOperation(docAchat.getDepotReception(), Constantes.SORTIE, Constantes.RETOUR, false)) {
                return;
            }
            loadAllTrancheLivraison(y, docAchat.getDateLivraison());
        }
    }

    public void chooseIsBon() {
        initView();
    }

    public void initFactureAchat() {
        setExistFacture(true);
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            if (!selectDoc.isFacture()) {
                String[] ch = new String[]{"documentLie", "typeDoc"};
                Object[] v = new Object[]{new YvsComDocAchats(selectDoc.getId()), Constantes.TYPE_FAA};
                List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParentTypeDoc", ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    getWarningMessage("Ce document a deja une facture!");
                    docSelect = UtilCom.buildBeanDocAchat(l.get(0));
                    contenus_fa = loadContenus(l.get(0));
                } else {
                    docSelect = UtilCom.buildBeanDocAchat(selectDoc);
                    docSelect.setDocumentLie(new DocAchat(selectDoc.getId(), selectDoc.getNumDoc(), selectDoc.getStatut()));
                    docSelect.setDateDoc(new Date());
                    docSelect.setUpdate(false);
                    docSelect.setTypeDoc(Constantes.TYPE_FAA);
//                    docSelect.setNumDoc(genererReference(Constantes.TYPE_FAA_NAME, new Date()));
                    cloneObject(docSelect.getCategorieComptable(), docSelect.getFournisseur().getCategorieComptable());
                    docSelect.setStatut(Constantes.ETAT_EDITABLE);

                    contenus_fa = loadContenus(selectDoc);
                    setExistFacture(false);
                }
                docSelect.setMontantHT(0);
                for (YvsComContenuDocAchat c : contenus_fa) {
                    c.setQuantiteCommande(c.getQuantiteRecu());
                    c.setTaxeRecu(c.getTaxe());
                    docSelect.setMontantHT(docSelect.getMontantHT() + c.getPrixTotal());
                }
                update("blog_montant");
            } else {
                getErrorMessage("Cette option est associée aux bons");
            }
        }
    }

    public void initChangeStatut() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            if (selectDoc.isFacture()) {
                docSelect = UtilCom.buildBeanDocAchat(selectDoc);
                openDialog("dlgStatut");
                update("grp_btn_etat_");
            } else {
                getErrorMessage("Cette option est associée aux factures");
            }
        }
    }

    public void onPrixOrQteBlur() {
        if (recalculTaxe) {
            contenu.setTaxe(dao.getTaxe(contenu.getArticle().getId(), docAchat.getCategorieComptable().getId(), 0, 0, contenu.getQuantiteCommende(), (contenu.getPrixAchat()), false, docAchat.getFournisseur().getId()));
        } else {
            contenu.setTaxe(contenu_fa.getTaxe());
        }
        if (contenu_fa.getArticle().isPuvTtc()) {
            contenu.setPrixTotalRecu((contenu.getPrixAchat() * contenu.getQuantiteCommende()) - contenu.getRemiseRecu());
        } else {
            contenu.setPrixTotalRecu((contenu.getPrixAchat() * contenu.getQuantiteCommende()) + contenu.getTaxe() - contenu.getRemiseRecu());
        }
    }

    public void onCorrectifPrixTotal() {
        if (dao.isComptabilise(docAchat.getId(), Constantes.SCR_AVOIR_ACHAT)) {
            getErrorMessage("Impossible de corriger les prix car ce document est comptabilisé");
            return;
        }
        for (YvsComContenuDocAchat c : contenus) {
            if (c.getArticle().getPuvTtc()) {
                c.setPrixTotal((c.getPrixAchat() * c.getQuantiteCommande()) - c.getRemise());
            } else {
                c.setPrixTotal((c.getPrixAchat() * c.getQuantiteCommande()) + c.getTaxe() - c.getRemise());
            }
            dao.update(c);
        }
    }

    public void apllyOrNotTaxes(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            if (!(Boolean) ev.getNewValue()) {
                contenu.setTaxe(dao.getTaxe(contenu.getArticle().getId(), docAchat.getCategorieComptable().getId(), 0, 0, contenu.getQuantiteCommende(), (contenu.getPrixAchat()), false, docAchat.getFournisseur().getId()));
            } else {
                contenu.setTaxe(contenu_fa.getTaxe());
            }
            onPrixOrQteBlur();
        }
    }

    public void onTaxeBlur() {
        if (!contenu_fa.getArticle().isPuvTtc()) {
            contenu.setPrixTotalRecu((contenu.getPrixAchat() * contenu.getQuantiteCommende()) + contenu.getTaxe());
        }
    }

    public void imprimerBonAvoir(DocAchat bean) {
        if ((bean != null) ? bean.getId() > 0 : false) {

        }
    }

    public void sendByMail() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            YvsBaseFournisseur fsseur = selectDoc.getFournisseur();
            String email = fsseur.getTiers().getEmail();
            if ((email != null) ? !email.equals("") : false) {
                if (Util.correctEmail(email)) {
                    changeStatut(Constantes.ETAT_SOUMIS, selectDoc);
                } else {
                    getErrorMessage("Impossible d'envoyer! Email Incorrect");
                }
            } else {
                getErrorMessage("Impossible d'envoyer! Le fournisseur n'a pas d'email");
            }
        }
    }

    public boolean annulerOrder(boolean force) {
        return annulerOrder(selectDoc, docAchat, true, true, force);
    }

    public boolean annulerOrder(YvsComDocAchats y, boolean msg, boolean open, boolean force) {
        return annulerOrder(y, UtilCom.buildBeanDocAchat(y), msg, open, force);
    }

    public boolean annulerOrder(YvsComDocAchats entity, DocAchat bean, boolean msg, boolean open, boolean force) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                if (dao.isComptabilise(entity.getId(), Constantes.SCR_AVOIR_ACHAT)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return false;
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        if (!w.unComptabiliserAchat(entity, false)) {
                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                            return false;
                        }
                    }
                }
                List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{entity});
                if (force) {
                    List<YvsComDocAchats> list = new ArrayList<>();
                    list.addAll(l);
                    for (YvsComDocAchats d : list) {
                        dao.delete(d);
                        l.remove(d);
                    }
                }
                if (l != null ? l.isEmpty() : true) {
                    if (changeStatut(Constantes.ETAT_EDITABLE, bean, entity)) {
                        entity.setCloturer(false);
                        entity.setAnnulerBy(null);
                        entity.setCloturerBy(null);
                        entity.setValiderBy(null);
                        entity.setDateAnnuler(null);
                        entity.setDateCloturer(null);
                        entity.setDateValider(null);
                        entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_ATTENTE));
                        entity.setDateLivraison(null);
                        entity.setStatut(Constantes.ETAT_EDITABLE);
                        if (currentUser != null ? currentUser.getId() > 0 : false) {
                            entity.setAuthor(currentUser);
                        }
                        YvsComDocAchats y = new YvsComDocAchats(entity);
                        y.getContenus().clear();
                        dao.update(y);
                        if (entity.getDocumentLie() != null ? (entity.getDocumentLie().getId() != null ? entity.getDocumentLie().getId() > 0 : false) : false) {
                            Map<String, String> statuts = dao.getEquilibreAchat(entity.getDocumentLie().getId());
                            if (statuts != null) {
                                entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                                entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                            }
                        }
                        return true;
                    }
                } else {
                    for (YvsComDocAchats d : l) {
                        if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                            if (msg) {
                                getErrorMessage("Impossible d'annuler cet ordre car il possède un transfert déja valide");
                            }
                            return false;
                        }
                    }
                    if (open) {
                        openDialog("dlgConfirmAnnuler");
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("Erreur : ", ex);
        }
        return false;
    }

    public boolean refuserOrder(boolean force) {
        return refuserOrder(selectDoc, docAchat, true, true, force);
    }

    public boolean refuserOrder(YvsComDocAchats y, boolean msg, boolean open, boolean force) {
        return refuserOrder(selectDoc, UtilCom.buildBeanDocAchat(y), msg, open, force);
    }

    public boolean refuserOrder(YvsComDocAchats entity, DocAchat bean, boolean msg, boolean open, boolean force) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                if (dao.isComptabilise(entity.getId(), Constantes.SCR_AVOIR_ACHAT)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return false;
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        if (!w.unComptabiliserAchat(entity, false)) {
                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                            return false;
                        }
                    }
                }
                List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{entity});
                if (force) {
                    List<YvsComDocAchats> list = new ArrayList<>();
                    list.addAll(l);
                    for (YvsComDocAchats d : list) {
                        dao.delete(d);
                        l.remove(d);
                    }
                }
                if (l != null ? l.isEmpty() : true) {
                    if (changeStatut(Constantes.ETAT_ANNULE, bean, entity)) {
                        entity.setStatut(Constantes.ETAT_ANNULE);
                        entity.setCloturer(false);
                        entity.setAnnulerBy(currentUser.getUsers());
                        entity.setValiderBy(null);
                        entity.setDateAnnuler(new Date());
                        entity.setDateCloturer(null);
                        entity.setDateValider(null);
                        entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_ATTENTE));
                        entity.setDateLivraison(null);
                        if (currentUser != null ? currentUser.getId() > 0 : false) {
                            entity.setAuthor(currentUser);
                        }
                        YvsComDocAchats y = new YvsComDocAchats(entity);
                        y.getContenus().clear();
                        dao.update(y);
                        if (entity.getDocumentLie() != null ? (entity.getDocumentLie().getId() != null ? entity.getDocumentLie().getId() > 0 : false) : false) {
                            Map<String, String> statuts = dao.getEquilibreAchat(entity.getDocumentLie().getId());
                            if (statuts != null) {
                                entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                                entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                            }
                        }
                        return true;
                    }
                } else {
                    for (YvsComDocAchats d : l) {
                        if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                            if (msg) {
                                getErrorMessage("Impossible d'annuler cet ordre car il possède un transfert déja valide");
                            }
                            return false;
                        }
                    }
                    if (open) {
                        openDialog("dlgConfirmRefuser");
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("Erreur : ", ex);
        }
        return false;
    }

    public void transmisOrder() {
        if (selectDoc == null) {
            return;
        }
        if (changeStatut(Constantes.ETAT_SOUMIS)) {
            if (selectDoc.getDocumentLie() != null ? (selectDoc.getDocumentLie().getId() != null ? selectDoc.getDocumentLie().getId() > 0 : false) : false) {
                Map<String, String> statuts = dao.getEquilibreAchat(selectDoc.getDocumentLie().getId());
                if (statuts != null) {
                    selectDoc.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                    selectDoc.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                }
            }
        }
    }

    public boolean validerOrder(YvsComDocAchats y) {
        if (validerOrder(y, UtilCom.buildBeanDocAchat(y))) {
            if (type.equals(Constantes.TYPE_FAA)) {
                update("data_avoir_vente");
            } else {
                update("data_retour_vente");
            }
            return true;
        }
        return false;
    }

    public void validerOrder() {
        validerOrder(selectDoc, docAchat);
        if (type.equals(Constantes.TYPE_FAA)) {
            update("data_avoir_achat");
        } else {
            update("data_retour_achat");
        }
    }

    public boolean validerOrder(YvsComDocAchats entity, DocAchat bean) {
        if (entity == null) {
            return false;
        }
        if (!verifyOperation(bean.getDepotReception(), Constantes.SORTIE, Constantes.RETOUR, true)) {
            return false;
        }
        for (YvsComContenuDocAchat y : entity.getContenus()) {
            if (y.getLot() != null ? y.getLot().getId() > 0 : false) {
                champ = new String[]{"typeDoc", "parent", "lot", "conditionnement"};
                val = new Object[]{Constantes.TYPE_BLA, entity.getDocumentLie(), y.getLot(), y.getConditionnement()};
                Long count = (Long) dao.loadObjectByNameQueries("YvsComContenuDocAchat.countByLotParent", champ, val);
                if (count != null ? count < 1 : true) {
                    getErrorMessage("Le numero de lot " + y.getLot().getNumero() + " n'est pas associé à l'article " + y.getArticle().getDesignation() + " sur la facture " + entity.getDocumentLie().getNumDoc());
                    return false;
                }
            }
        }
        if (changeStatut(Constantes.ETAT_VALIDE)) {
            entity.setCloturer(false);
            entity.setAnnulerBy(null);
            entity.setValiderBy(currentUser.getUsers());
            entity.setDateAnnuler(null);
            entity.setDateCloturer(null);
            entity.setDateValider(new Date());
            entity.setDateLivraison(entity.getDateDoc());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                entity.setAuthor(currentUser);
            }
            entity.setStatut(Constantes.ETAT_VALIDE);
            YvsComDocAchats y = new YvsComDocAchats(entity);
            y.getContenus().clear();
            dao.update(y);
            if (entity.getDocumentLie() != null) {
                Map<String, String> statuts = dao.getEquilibreAchat(entity.getDocumentLie().getId());
                if (statuts != null) {
                    entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                    entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                }
            }
            return true;
        }
        return false;
    }

    public boolean changeStatutDoc(YvsComDocAchats entity, String statut) {
        System.err.println(" Agence " + entity.getAgence());
        if (changeStatut(statut)) {
            if (statut.equals(Constantes.ETAT_ANNULE)) {
                entity.setCloturer(false);
                entity.setAnnulerBy(currentUser.getUsers());
                entity.setValiderBy(null);
                entity.setDateAnnuler(new Date());
                entity.setDateCloturer(null);
                entity.setDateValider(null);
                entity.setDateLivraison(null);
            } else if (statut.equals(Constantes.ETAT_EDITABLE)) {
                entity.setCloturer(false);
                entity.setAnnulerBy(null);
                entity.setValiderBy(null);
                entity.setDateAnnuler(null);
                entity.setDateCloturer(null);
                entity.setDateValider(null);
                entity.setDateLivraison(null);
            }
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                entity.setAuthor(currentUser);
            }
            entity.setDateUpdate(new Date());
            YvsComDocAchats y = new YvsComDocAchats(entity);
            y.getContenus().clear();
            dao.update(y);
            if (entity.getDocumentLie() != null) {
                Map<String, String> statuts = dao.getEquilibreAchat(entity.getDocumentLie().getId());
                if (statuts != null) {
                    entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                    entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                }
            }
            return true;
        }
        return false;
    }

    public boolean livrer(YvsComDocAchats entityBL, DocAchat docLiv) {
        if (entityBL == null) {
            return false;
        }
        if (docLiv.getDepotReception() != null ? docLiv.getDepotReception().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le dépot de livraison");
            return false;
        }
        if (docLiv.getTranche() != null ? docLiv.getTranche().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier la tranche de livraison");
            return false;
        }
        if (!docLiv.getDocumentLie().getStatut().equals(Constantes.ETAT_VALIDE)) {
            getErrorMessage("La facture n'est pas valide");
            return false;
        }
        if (_saveNew()) {
            String result = null;
            YvsComContenuDocAchat cc = null;
            for (YvsComContenuDocAchat c : entityBL.getContenus()) {
                cc = c;
                result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), docLiv.getDepotReception().getId(), 0L, c.getQuantiteRecu(), 0, "INSERT", "S", docLiv.getDateLivraison(), (c.getLot() != null ? c.getLot().getId() : 0));
                if (result != null) {
                    break;
                }
                //controle les quantités déjà livré
                Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findByDocLierTypeStatutArticleS", new String[]{"docAchat", "statut", "typeDoc", "article", "unite"}, new Object[]{c.getDocAchat().getDocumentLie(), Constantes.ETAT_VALIDE, Constantes.TYPE_FAA, c.getArticle(), c.getConditionnement()});
                qteLivre = (qteLivre != null) ? qteLivre : 0;
                //trouve la quantité d'article facturé 
                Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticle", new String[]{"docAchat", "article", "unite"}, new Object[]{c.getDocAchat().getDocumentLie(), c.getArticle(), c.getConditionnement()});
                qteFacture = (qteFacture != null) ? qteFacture : 0;
                //récupère la quantité de l'article dans le document de livraison en cours. (Le pb viens du fait que la ref d'un article peut se trouver plusieurs fois dans la liste d'un bl non encore livré)
                Double qteEncour = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticle", new String[]{"docAchat", "article", "unite"}, new Object[]{c.getDocAchat(), c.getArticle(), c.getConditionnement()});
                qteEncour = (qteEncour != null) ? qteEncour : 0;
                if (qteEncour > (qteFacture - qteLivre)) {
                    getErrorMessage("Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !");
                    return false;
                }

                champ = new String[]{"article", "depot"};
                val = new Object[]{c.getArticle(), new YvsBaseDepots(docLiv.getDepotReception().getId())};
                YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepotReception", champ, val);
                if (y != null ? y.getId() < 1 : true) {
                    getErrorMessage("Impossible d'effectuer cette action... Car le depot " + docLiv.getDepotReception().getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
                    return false;
                }
            }
            if (result != null) {
                getErrorMessage("Impossible de valider ce document", "la ligne d'article " + cc.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + result);
                return false;
            }
            docLiv.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entityBL.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entityBL.setDateLivraison(docLiv.getStatutLivre().equals(String.valueOf(Constantes.STATUT_DOC_LIVRER)) ? new Date() : null);
            return true;
        }
        return false;
    }

    public void cloturer(YvsComDocAchats y) {
        selectDoc = y;
        if (type.equals(Constantes.TYPE_FAA)) {
            update("id_confirm_close_faa");
        } else {
            update("id_confirm_close_bra");
        }
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        docAchat.setCloturer(!docAchat.isCloturer());
        selectDoc.setCloturer(docAchat.isCloturer());
        selectDoc.setDateCloturer(docAchat.isCloturer() ? new Date() : null);
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        documents.set(documents.indexOf(selectDoc), selectDoc);
        succes();
        if (type.equals(Constantes.TYPE_FAA)) {
            update("data_avoir_achat");
        } else {
            update("data_retour_achat");
        }
        update("data_facture_achat");
    }

    public void _cloturer() {
        if (selectDoc == null) {
            return;
        }
        docAchat.setCloturer(!docSelect.isCloturer());
        selectDoc.setCloturer(docSelect.isCloturer());
        selectDoc.setDateCloturer(docSelect.isCloturer() ? new Date() : null);
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        documents.set(documents.indexOf(selectDoc), selectDoc);
        succes();
        if (type.equals(Constantes.TYPE_FAA)) {
            update("data_avoir_achat");
        } else {
            update("data_retour_achat");
        }
    }

    public boolean _changeStatut(String etat) {
        if (changeStatut_(etat, docSelect, selectDoc, true)) {
            succes();
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

    public boolean changeStatut_(String etat) {
        selectDoc.setContenus(new ArrayList<YvsComContenuDocAchat>(contenus));
        return changeStatut_(etat, docAchat, selectDoc);
    }

    public boolean changeStatut(String etat, YvsComDocAchats doc_) {
        if (changeStatut_(etat, doc_)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, YvsComDocAchats doc_) {
        return changeStatut_(etat, UtilCom.buildBeanDocAchat(doc_), doc_);
    }

    public boolean changeStatut(String etat, DocAchat doc, YvsComDocAchats doc_) {
        if (changeStatut_(etat, doc, doc_)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, DocAchat doc, YvsComDocAchats doc_) {
        return changeStatut_(etat, doc, doc_, true);
    }

    public boolean changeStatut_(String etat, DocAchat doc, YvsComDocAchats doc_, boolean isBon) {
        if (!etat.equals("")) {
            if (doc.isCloturer()) {
                getErrorMessage("Le document est provisoirement vérouillé !");
                return false;
            }
            if (this.type.equals(Constantes.TYPE_BRA)) {
                if (!doc_.getStatut().equals(Constantes.ETAT_VALIDE) && etat.equals(Constantes.ETAT_VALIDE)) {
                    //contrôle de stocks
                    if (doc_.getContenus().isEmpty()) {
                        //récupère la liste des contenus
                        doc_.setContenus(dao.loadNameQueries("YvsComContenuDocAchat.findByDocAchat", new String[]{"docAchat"}, new Object[]{doc_}));
                    }
                    String result;

                    for (YvsComContenuDocAchat c : doc_.getContenus()) {
                        result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), doc_.getDepotReception().getId(), 0L, c.getQuantiteCommande(), 0, "INSERT", "S", doc_.getDateDoc(), (c.getLot() != null ? c.getLot().getId() : 0));
                        if (result != null) {
                            getErrorMessage("Impossible de valider ce document", "la ligne d'article " + c.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + result);
                            return false;
                        }
                    }
                }
            }
            String rq = "UPDATE yvs_com_doc_achats SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(doc.getId(), 1)};
//            dao.requeteLibre(rq, param);
            doc.setStatut(etat);
            doc_.setStatut(etat);
            if (isBon ? documents.contains(doc_) : false) {
                documents.set(documents.indexOf(doc_), doc_);
            }
//            ManagedFactureAchat m = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
//            if (m != null) {
//                m.loadFactureNonLivre(true, true);
//            }
            if (type.equals(Constantes.TYPE_FAA)) {
                update("data_avoir_achat");
                update("infos_document_avoir_achat");
                update("data_fa_avoir");
            } else {
                update("data_retour_achat");
                update("infos_document_retour_achat");
                update("data_fa_retour");
            }
            return true;
        }
        return false;
    }

    public void searchByNum() {
        ParametreRequete p;
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.numDoc)", "numDoc", "%" + numSearch_.toUpperCase() + "%");
            p.setOperation(" LIKE ");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.numDoc", "numDoc", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void removeDoublon() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            if (!selectDoc.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre editable pour pouvoir etre modifié");
                return;
            }
            removeDoublonAchat(selectDoc.getId());
            succes();
        }
    }

    @Override
    public void cleanAchat() {
        super.cleanAchat();
        loadAllFacture(true, true);
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /* Géstion des retours d'article*/
    public void openDelgRetourStock(YvsComContenuDocAchat c) {
        selectContenu = c;
    }

    private List<YvsComptaPiecesComptable> contentCompta = new ArrayList<>();

    public List<YvsComptaPiecesComptable> getContentCompta() {
        return contentCompta;
    }

    public void setContentCompta(List<YvsComptaPiecesComptable> contentCompta) {
        this.contentCompta = contentCompta;
    }

    public void displayPieceComptableFacture() {
        if (docAchat != null ? docAchat.getId() > 0 : false) {
            String query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit "
                    + "FROM yvs_compta_content_journal_facture_achat cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                    + "WHERE cf.facture=? ORDER BY p.id";

            List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(docAchat.getId(), 1)});
            long idPiece = 0;
            YvsComptaPiecesComptable pc = null;
            YvsComptaContentJournal c;
            contentCompta.clear();
            for (Object[] line : result) {
                if (line[0] != null ? ((Long) line[0] != idPiece) : false) {
                    idPiece = (Long) line[0];
                    pc = new YvsComptaPiecesComptable(idPiece);
                    pc.setNumPiece((String) line[1]);
                    pc.setDatePiece((line[2] != null) ? (Date) line[2] : null);
                    contentCompta.add(pc);
                } else {
                    if (line[0] == null) {
                        pc = null;
                        continue;
                    }
//                    if(pc!=null){
//                        contentCompta.add(pc);
//                    }
                }
                if (pc != null) {
                    if (pc.getContentsPiece() == null) {
                        pc.setContentsPiece(new ArrayList<YvsComptaContentJournal>());
                    }
                    c = new YvsComptaContentJournal((Long) line[3]);
                    c.setNumPiece((String) line[4]);
                    if (line[5] != null) {
                        c.setCompteGeneral(new YvsBasePlanComptable((Long) line[5], numCompte((Long) line[5])));
                    }
                    c.setCompteTiers((line)[6] != null ? (Long) line[6] : null);
                    c.setTableTiers((String) line[7]);
                    c.setDebit((line)[8] != null ? (Double) line[8] : null);
                    c.setCredit((line)[9] != null ? (Double) line[9] : null);
                    pc.getContentsPiece().add(c);
                }
            }
        }
    }

    public void onBuildGenererOther(String type) {
        try {
            this.typeGenerer = type;
            this.dateGenerer = docAchat.getDateDoc();
            update("main-generer_other");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onBuildGenererOther", ex);
        }
    }

    public void onGenererOther() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                YvsComDocAchats y = new YvsComDocAchats(null, selectDoc);
                y.getContenus().clear();
                y.getReglements().clear();
                y.getEtapesValidations().clear();
                y.getCouts().clear();
                y.getMensualites().clear();

                y.setTypeDoc(typeGenerer);
                y.setDateDoc(dateGenerer);
                y.setStatut(Constantes.ETAT_EDITABLE);
                String titre = Constantes.TYPE_FAA_NAME;
                if (typeGenerer.equals(Constantes.TYPE_BRA)) {
                    titre = Constantes.TYPE_BRA_NAME;
                }
                String numero = null;
                if (docAchat.getDepotReception() != null ? docAchat.getDepotReception().getId() < 1 : true) {
                    numero = genererReference(titre, dateGenerer, currentAgence.getId(), Constantes.AGENCE);
                } else {
                    numero = genererReference(titre, dateGenerer, docAchat.getDepotReception().getId(), Constantes.DEPOT);
                }
                if (numero == null || numero.trim().equals("")) {
                    return;
                }
                y.setNumDoc(numero);
                y.setDateSave(new Date());
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                y = (YvsComDocAchats) dao.save1(y);
                if (y != null ? y.getId() < 1 : true) {
                    return;
                }
                YvsComContenuDocAchat c;
                for (int i = 0; i < contenus.size(); i++) {
                    c = new YvsComContenuDocAchat(null, contenus.get(i));
                    c.setDocAchat(y);
                    c.setDateSave(new Date());
                    c.setDateUpdate(new Date());
                    c.setAuthor(currentUser);
                    c = (YvsComContenuDocAchat) dao.save1(c);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onGenererOther", ex);
        }
    }

    private String numCompte(Long id) {
        if (id != null) {
            return (String) dao.loadObjectByNameQueries("YvsBasePlanComptable.findNumeroById", new String[]{"id"}, new Object[]{id});
        }
        return null;
    }

    public void print(YvsComDocAchats y) {
        print(y, true);
    }

    public void print(YvsComDocAchats y, boolean withHeader) {
        try {
            if (currentParamAchat != null ? currentParamAchat.getId() < 1 : true) {
                currentParamAchat = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (y != null ? y.getId() > 0 : false) {
                if (currentParamAchat != null ? (currentParamAchat.getPrintDocumentWhenValide() && !y.getStatut().equals(Constantes.ETAT_VALIDE)) : false) {
                    getErrorMessage("Le document doit être validé pour pouvoir être téléchargé");
                    return;
                }
                Map<String, Object> param = new HashMap<>();
                param.put("ID", y.getId().intValue());
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(withHeader));
                String report = "bon_retour_achat";
                if (y.getTypeDoc().equals(Constantes.TYPE_BRA)) {
                    param.put("TITRE_DOC", Constantes.DOCUMENT_RETOUR_ACHAT);
                    if (currentParam != null ? currentParam.getUseLotReception() : false) {
                        report = "bon_retour_achat_by_lot";
                    }
                } else {
                    Double ca = dao.loadCaAchat(y.getId());
                    Double taxe = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findTaxeByFacture", new String[]{"docAchat"}, new Object[]{y});
                    List<Object[]> taxes = dao.getTaxeAchat(y.getId());
                    param.put("AFFICHE_TAXE", taxes != null ? taxes.size() > 1 : false);
                    param.put("MONTANT", Nombre.CALCULATE.getValue(ca));
                    param.put("TTC", ca);
                    param.put("TAXE", taxe);
                    param.put("TITRE_DOC", Constantes.DOCUMENT_AVOIR_ACHAT);
                    report = "facture_avoir_achat";
                }
                executeReport(report, param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isComptabiliseBean(DocAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_AVOIR_ACHAT));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_AVOIR_ACHAT));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.agence", "agence", null);
        if (agenceSearch > 0) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
        ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (w != null) {
            w.onSelectAgenceForFind(agenceSearch);
        }
    }

    public void addParamFournisseur() {
        ParametreRequete p = new ParametreRequete("y.fournisseur.codeFsseur", "fournisseur", null);
        if (codeFsseur_ != null ? codeFsseur_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "fournisseur", codeFsseur_.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fournisseur.codeFsseur)", "fournisseur", codeFsseur_.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fournisseur.nom)", "fournisseur", codeFsseur_.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }
}
