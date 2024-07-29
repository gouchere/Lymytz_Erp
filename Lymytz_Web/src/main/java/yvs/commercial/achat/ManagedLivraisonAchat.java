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
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.UtilCompta;
import yvs.commercial.depot.ArticleDepot;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.ManagedArticles;
import yvs.base.tiers.Tiers;
import yvs.production.UtilProd;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.UtilCom;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.commercial.param.ManagedTypeDocDivers;
import yvs.commercial.param.TypeDocDivers;
import yvs.commercial.stock.ContenuDocStock;
import yvs.commercial.stock.DocStock;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.commercial.stock.ManagedTransfertStock;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseTypeDocCategorie;
import yvs.entity.commercial.YvsComCritereLot;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreAchat;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.entity.produits.YvsBaseArticles;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;
import static yvs.util.Managed.ldf;
import static yvs.util.Managed.time;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;
import yvs.util.Utilitaire;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedLivraisonAchat extends ManagedCommercial<DocAchat, YvsComDocAchats> implements Serializable {

    private DocAchat docAchat = new DocAchat();
    private DocAchat docSelect = new DocAchat();
    private DocAchat facture = new DocAchat();
    private List<YvsComDocAchats> documents, livraisons;
    private YvsComDocAchats selectDoc;
    private YvsComDocAchats livraison = new YvsComDocAchats();

    private List<YvsComContenuDocAchat> contenus_fa, all_contenus, contenusRequireLot;
    private YvsComContenuDocAchat selectContenu;
    private ContenuDocAchat contenu = new ContenuDocAchat();
    private boolean isBon;
    public PaginatorResult<YvsComContenuDocAchat> p_contenu = new PaginatorResult<>(50);

    private List<YvsGrhTrancheHoraire> tranches;

    private CritereLot critereSearch = new CritereLot();
    private List<YvsComCritereLot> criteres;

    private DocStock docStock = new DocStock();
    private ContenuDocStock transfert = new ContenuDocStock();
    private List<YvsBaseDepots> destinations;

    private String tabIds, tabIds_article, numFacture, egaliteStatut = "!=";
    private Boolean lieSearch;
    private boolean update, isFacture, selectArt;
    private double quantite_livree;
    private Date dateLivraison = new Date();

    //Parametre recherche contenu
    private boolean dateContenu = false;
    private Date dateDebutContenu = new Date(), dateFinContenu = new Date();
    private String statutContenu, reference, article, depot, articleContenu, fournisseurF;

    public ManagedLivraisonAchat() {
        tranches = new ArrayList<>();
        contenus_fa = new ArrayList<>();
        criteres = new ArrayList<>();
        documents = new ArrayList<>();
        livraisons = new ArrayList<>();
        all_contenus = new ArrayList<>();
        destinations = new ArrayList<>();
        contenusRequireLot = new ArrayList<>();
    }

    public List<YvsComContenuDocAchat> getContenusRequireLot() {
        return contenusRequireLot;
    }

    public void setContenusRequireLot(List<YvsComContenuDocAchat> contenusRequireLot) {
        this.contenusRequireLot = contenusRequireLot;
    }

    public Boolean getLieSearch() {
        return lieSearch;
    }

    public void setLieSearch(Boolean lieSearch) {
        this.lieSearch = lieSearch;
    }

    public String getArticleContenu() {
        return articleContenu;
    }

    public void setArticleContenu(String articleContenu) {
        this.articleContenu = articleContenu;
    }

    public List<YvsBaseDepots> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<YvsBaseDepots> destinations) {
        this.destinations = destinations;
    }

    public DocStock getDocStock() {
        return docStock;
    }

    public void setDocStock(DocStock docStock) {
        this.docStock = docStock;
    }

    public ContenuDocStock getTransfert() {
        return transfert;
    }

    public void setTransfert(ContenuDocStock transfert) {
        this.transfert = transfert;
    }

    public boolean isIsBon() {
        return isBon;
    }

    public void setIsBon(boolean isBon) {
        this.isBon = isBon;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public double getQuantite_livree() {
        return quantite_livree;
    }

    public void setQuantite_livree(double quantite_livree) {
        this.quantite_livree = quantite_livree;
    }

    public List<YvsComDocAchats> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(List<YvsComDocAchats> livraisons) {
        this.livraisons = livraisons;
    }

    public YvsComDocAchats getLivraison() {
        return livraison;
    }

    public void setLivraison(YvsComDocAchats livraison) {
        this.livraison = livraison;
    }

    public boolean isDateContenu() {
        return dateContenu;
    }

    public void setDateContenu(boolean dateContenu) {
        this.dateContenu = dateContenu;
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

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getStatutContenu() {
        return statutContenu != null ? statutContenu.trim().length() > 0 ? statutContenu : Constantes.ETAT_VALIDE : Constantes.ETAT_VALIDE;
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

    public String getFournisseurF() {
        return fournisseurF;
    }

    public void setFournisseurF(String fournisseurF) {
        this.fournisseurF = fournisseurF;
    }

    public List<YvsComContenuDocAchat> getAll_contenus() {
        return all_contenus;
    }

    public void setAll_contenus(List<YvsComContenuDocAchat> all_contenus) {
        this.all_contenus = all_contenus;
    }

    public PaginatorResult<YvsComContenuDocAchat> getP_contenu() {
        return p_contenu;
    }

    public void setP_contenu(PaginatorResult<YvsComContenuDocAchat> p_contenu) {
        this.p_contenu = p_contenu;
    }

    public String getNumFacture() {
        return numFacture;
    }

    public void setNumFacture(String numFacture) {
        this.numFacture = numFacture;
    }

    public List<YvsGrhTrancheHoraire> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsGrhTrancheHoraire> tranches) {
        this.tranches = tranches;
    }

    public boolean isSelectArt() {
        return selectArt;
    }

    public void setSelectArt(boolean selectArt) {
        this.selectArt = selectArt;
    }

    public List<YvsComContenuDocAchat> getContenus_fa() {
        return contenus_fa;
    }

    public void setContenus_fa(List<YvsComContenuDocAchat> contenus_fa) {
        this.contenus_fa = contenus_fa;
    }

    public YvsComContenuDocAchat getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComContenuDocAchat selectContenu) {
        this.selectContenu = selectContenu;
    }

    public DocAchat getFacture() {
        return facture;
    }

    public void setFacture(DocAchat facture) {
        this.facture = facture;
    }

    public YvsComDocAchats getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocAchats selectDoc) {
        this.selectDoc = selectDoc;
    }

    public boolean isIsFacture() {
        return isFacture;
    }

    public void setIsFacture(boolean isFacture) {
        this.isFacture = isFacture;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public DocAchat getDocSelect() {
        return docSelect;
    }

    public void setDocSelect(DocAchat docSelect) {
        this.docSelect = docSelect;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public ContenuDocAchat getContenu() {
        return contenu;
    }

    public void setContenu(ContenuDocAchat contenu) {
        this.contenu = contenu;
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

    public CritereLot getCritereSearch() {
        return critereSearch;
    }

    public void setCritereSearch(CritereLot critereSearch) {
        this.critereSearch = critereSearch;
    }

    public DocAchat getReceptionArticleAchat() {
        return docAchat;
    }

    public void setReceptionArticleAchat(DocAchat receptionArticleAchat) {
        this.docAchat = receptionArticleAchat;
    }

    public List<YvsComCritereLot> getCriteres() {
        return criteres;
    }

    public void setCriteres(List<YvsComCritereLot> criteres) {
        this.criteres = criteres;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        _load();
        loadAllCritere();
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
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        if (currentParamAchat == null) {
            currentParamAchat = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        if (docAchat.getId() < 1 ? (currentParamAchat != null) : false) {
            docAchat.setGenererFactureAuto(currentParamAchat.getGenererFactureAuto());
        }
//        loadContenus(true, true);
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllBons(true, true);
    }

    public void load() {
        indiceNumsearch_ = genererPrefixe(Constantes.TYPE_BLA_NAME, currentDepot != null ? currentDepot.getId() : 0);
        if (((docAchat != null) ? docAchat.getFournisseur().getId() < 1 : true)) {
            docAchat = new DocAchat();
            docAchat.setTypeDoc(Constantes.TYPE_BLA);
            if (docAchat.getDocumentLie() == null) {
                docAchat.setDocumentLie(new DocAchat());
            }
            numSearch_ = "";
        }
        if ((docAchat.getDepotReception() != null) ? docAchat.getDepotReception().getId() < 1 : true) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null ? m.getDepots().contains(currentDepot) : false) {
                docAchat.setDepotReception(UtilCom.buildBeanDepot(currentDepot));
                loadAllTranche(currentDepot);
                if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
                    docAchat.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
                }
            }
        }
    }

    public void loadAllBons(boolean avance, boolean init) {
        controlListAgence();
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_BLA)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.depotReception.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.depotReception.agence", "agences", listIdAgences, "IN", "AND");
                paginator.addParam(p);
                break;
            case 3: //charge tous les document des depots où l'utilisateurs est responsable
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.depotReception.id", "depot", ids, " IN ", "AND");
                paginator.addParam(p);
                break;
            default:    //charge les document du depot donc l'utilisateur connecté est responsable
                p = new ParametreRequete("y.depotReception", "depot", currentDepot, "=", "AND");
                paginator.addParam(p);
                break;

        }
        p = new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_BLA, "=", "AND");
        paginator.addParam(p);
        documents = paginator.executeDynamicQuery("YvsComDocAchats", "y.dateDoc DESC, y.numDoc DESC", avance, init, (int) imax, dao);
        if (documents.size() == 1) {
            onSelectObject(documents.get(0));
            execute("collapseForm('livraison_achat');");
        } else {
            execute("collapseList('livraison_achat')");
        }
    }

    public void loadContenus(boolean avance, boolean init) {
        controlListAgence();
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_FA)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.docAchat.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                p_contenu.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.docAchat.agence", "agences", listIdAgences, "IN", "AND");
                p_contenu.addParam(p);
                break;
            case 3: { //charge tous les document des points de achat où l'utilisateurs est responsable
                //cherche les points de achat de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"}, new Object[]{currentUser.getUsers(), (Utilitaire.getIniTializeDate(new Date()).getTime()), Constantes.getPreviewDate(new Date())});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.docAchat.depotReception.id", "ids", ids, " IN ", "AND");
                p_contenu.addParam(p);
                break;
            }
            default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                p = new ParametreRequete("y.docAchat.author.users", "users", currentUser.getUsers(), "=", "AND");
                p_contenu.addParam(p);
                break;

        }

        p_contenu.addParam(new ParametreRequete("y.docAchat.typeDoc", "typeDoc", Constantes.TYPE_FA, "=", "AND"));
        p_contenu.addParam(new ParametreRequete("y.docAchat.statut", "statut", getStatutContenu(), "=", "AND"));
        p_contenu.addParam(new ParametreRequete("y.docAchat.statutLivre", "statutLivre", Constantes.ETAT_LIVRE, "!=", "AND"));
        p_contenu.addParam(new ParametreRequete("y.statutLivree", "statutLivree", Constantes.ETAT_LIVRE, "!=", "AND"));
        String orderBy = "y.docAchat.dateDoc DESC, y.docAchat.numDoc";
        all_contenus = p_contenu.executeDynamicQuery("YvsComContenuDocAchat", orderBy, avance, init, dao);
        update("data_contenu_bla");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComDocAchats> re = paginator.parcoursDynamicData("YvsComDocAchats", "y", "y.dateDoc DESC, y.numDoc DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void init(boolean next) {
        loadAllBons(next, false);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllBons(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllBons(true, true);
    }

    @Override
    public void _chooseAgence() {
        super._chooseAgence();
        ParametreRequete p;
        if (agence_ > 0) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agence_));
            p.setOperation("=");
            p.setPredicat("AND");
            if (paginator.getParams().contains(new ParametreRequete("societe"))) {
                paginator.getParams().remove(new ParametreRequete("societe"));
            }
            if (paginator.getParams().contains(new ParametreRequete("depot"))) {
                paginator.getParams().remove(new ParametreRequete("depot"));
            }
        } else {
            p = new ParametreRequete("y.agence", "agence", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
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
        loadAllBons(true, true);
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
        loadAllBons(true, true);
    }

    public void loadAllCritere() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        criteres = dao.loadNameQueries("YvsComCritereLot.findAll", champ, val);
    }

    public void loadContenuFacture(YvsComDocAchats facture, boolean recopie) {
        if (facture != null ? facture.getId() > 0 : false) {
            long id = facture.getId();
            String state = facture.getStatut();
            String state_livre = facture.getStatutLivre();
            String num = facture.getNumDoc();
            if (recopie) {
                docAchat = UtilCom.buildBeanDocAchat(facture);
                docAchat.setDateDoc(new Date());
                docAchat.setDateLivraison(new Date());
                docAchat.setDocumentLie(new DocAchat(id, num, state));
                docAchat.getDocumentLie().setStatutLivre(state_livre);
                docAchat.setTypeDoc(Constantes.TYPE_BLA);
                docAchat.setNumDoc(null);
                docAchat.setNew_(true);
                docAchat.setUpdate(false);
                docAchat.setStatut(Constantes.ETAT_EDITABLE);
                docAchat.setDateSave(new Date());
                docAchat.setNumPiece("BL N° " + num);
                docAchat.setDescription("Livraison de la facture N° " + num + " le " + ldf.format(new Date()) + " à " + time.format(new Date()));
                docAchat.setId((long) -1);
                docAchat.getContenus().clear();
                docAchat.getContenusSave().clear();
                docAchat.setUpdate(false);
                setMontantTotalDoc(docAchat);
                update("infos_document_livraison_achat");
            }
            docAchat.setDocumentLie(new DocAchat(id, num, state));

            contenus_fa.clear();
            List<YvsComContenuDocAchat> lc = _loadContenus(new YvsComDocAchats(id));
            for (YvsComContenuDocAchat c : lc) {
                boolean deja = false;
                for (YvsComContenuDocAchat n : contenus_fa) {
                    if ((n.getArticle().equals(c.getArticle()) && n.getConditionnement().equals(c.getConditionnement())) ? !c.isBonus() : false) {
                        deja = true;
                        break;
                    }
                }
                if (!deja) {
                    champ = new String[]{"article", "unite", "docAchat"};
                    val = new Object[]{c.getArticle(), c.getConditionnement(), new YvsComDocAchats(facture.getId())};
                    Double bonus = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteBonusByArticle", champ, val);
                    bonus = bonus != null ? bonus : 0;
                    Double qte = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticle", champ, val);
                    qte = qte != null ? qte : 0;
                    if (c.isBonus()) {
                        qte += bonus;
                    }
                    if (qte > 0) {
                        Double rem = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findRemByArticle", champ, val);
                        rem = rem != null ? rem : 0;
                        Double tax = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findTaxeByArticle", champ, val);
                        tax = tax != null ? tax : 0;
                        Double pt = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findPTByArticle", champ, val);
                        pt = pt != null ? pt : 0;

                        double prix = (pt - tax + rem) / qte;

                        String[] ch = new String[]{"docAchat", "typeDoc", "statut", "article", "unite"};
                        Object[] v = new Object[]{new YvsComDocAchats(id), Constantes.TYPE_BLA, Constantes.ETAT_VALIDE, c.getArticle(), c.getConditionnement()};
                        Double liv = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findByDocLierTypeStatutArticleS", ch, v);
                        liv = liv != null ? liv : 0;
                        if (qte > liv) {
                            c.setQuantiteCommande(c.isBonus() ? bonus : qte);
                            double reste = qte - liv;
                            c.setQuantiteRecu(!c.isBonus() ? reste : (reste > bonus ? bonus : reste));
                            c.setTaxe(tax);
                            c.setRemise(rem);
                            c.setPrixAchat(prix);
                            c.setParent(new YvsComContenuDocAchat(c.getId()));
                            contenus_fa.add(c);
                        }
                    }
                }
            }
            if ((lc != null ? !lc.isEmpty() : false) && (contenus_fa != null ? contenus_fa.isEmpty() : true)) {
                getInfoMessage("Cette facture est déja livrée");
                if (!state_livre.equals(Constantes.ETAT_LIVRE)) {
                    String rq = "UPDATE yvs_com_doc_achats SET statut_livre = 'L' WHERE id=?";
                    Options[] param = new Options[]{new Options(id, 1)};
                    dao.requeteLibre(rq, param);
                    int idx = documents.indexOf(new YvsComDocAchats(id));
                    if (idx > -1) {
                        documents.get(idx).setStatutLivre(Constantes.ETAT_LIVRE);
                        update("data_livraison_achat");
                    }
                }
            }
            update("data_article_bon_achat");
        }
    }

    private List<YvsComContenuDocAchat> _loadContenus(YvsComDocAchats y) {
        nameQueri = "YvsComContenuDocAchat.findByDocAchat";
        champ = new String[]{"docAchat"};
        val = new Object[]{y};
        List<YvsComContenuDocAchat> list = dao.loadNameQueries(nameQueri, champ, val);
        List<YvsComContenuDocAchat> temps = new ArrayList<>();
        for (YvsComContenuDocAchat c : list) {
            c.setSave(c.getId());
            if (c.getQuantiteBonus() > 0) {
                YvsComContenuDocAchat bean = new YvsComContenuDocAchat((long) -(temps.size() + 1), c);
                bean.setArticle(c.getArticleBonus());
                bean.setConditionnement(c.getConditionnementBonus());
                bean.setQuantiteCommande(c.getQuantiteBonus());
                bean.setDocAchat(c.getDocAchat());
                bean.setBonus(true);

                bean.setQuantiteBonus(0.0);
                bean.setPrixAchat(0.0);
                bean.setPrixTotal(0.0);
                bean.setRemise(0.0);
                bean.setTaxe(0.0);

                temps.add(bean);
            }
        }
        if (!temps.isEmpty()) {
            list.addAll(temps);
        }
        return list;
    }

    private void loadAllTranche(YvsBaseDepots y) {
        tranches = loadTranche(null, y, docAchat.getDateDoc());
        update("select_tranche_livraison_achat");
    }

    @Override
    public DocAchat recopieView() {
        docAchat.setTypeDoc(Constantes.TYPE_BLA);
        if (docAchat.getId() < 1) {
            docAchat.setDateLivraison(docAchat.getDateDoc());
        }
//        return UtilCom.recopieAchat(docAchat, Constantes.TYPE_BLA);
        return docAchat;
    }

    public ContenuDocAchat recopieViewArticle(DocAchat doc) {
        double total = contenu.getQuantiteRecu() * contenu.getPrixAchat();
        contenu.setPrixTotalRecu(total + (contenu.getArticle().isPuaTtc() ? 0 : contenu.getTaxe()) - contenu.getRemiseRecu());
        contenu.setPrixTotalAttendu(contenu.getPrixTotalRecu());
        contenu.setDocAchat(doc);
        return contenu;
    }

    @Override
    public boolean controleFiche(DocAchat bean) {
        if (!_controleFiche_(bean, true)) {
            return false;
        }
        if (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false) {
            if (!bean.getDocumentLie().getStatut().equals(Constantes.ETAT_VALIDE)) {
                getWarningMessage("La facture n'est pas validé");
            }
        }
        if ((bean.getDepotReception() != null) ? bean.getDepotReception().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer le depot");
            return false;
        }
        if ((bean.getTranche() != null) ? bean.getTranche().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer la tranche horaire");
            return false;
        }
        if ((bean.getFournisseur() != null) ? bean.getFournisseur().getId() < 1 : true) {
            getErrorMessage("Vous devez indiquer le fournisseur");
            return false;
        }
        if (bean.getDateLivraison() == null) {
            bean.setDateLivraison(new Date());
        }
        if (bean.getDateDoc() == null) {
            bean.setDateDoc(bean.getDateLivraison());
        }
        // on change la référence si on est en modif et que le dépôt de livr ou la date sont différents
        String ref = bean.getNumDoc();
        if (selectDoc != null ? (selectDoc.getId() > 0 && bean.getId() > 0) : false) {
            if (!bean.getDateDoc().equals(selectDoc.getDateDoc()) || bean.getDepotReception().getId() != selectDoc.getDepotReception().getId()) {
                ref = genererReference(Constantes.TYPE_BLA_NAME, bean.getDateDoc(), docAchat.getDepotReception().getId());
            }
        } else {
            //génère un nouveau document
            ref = genererReference(Constantes.TYPE_BLA_NAME, bean.getDateDoc(), docAchat.getDepotReception().getId());
        }
//        if ((selectDoc != null ? (selectDoc.getId() > 0 ? !bean.getDateLivraison().equals(selectDoc.getDateLivraison()) : false) : false)
//                || (bean.getNumDoc() == null || bean.getNumDoc().trim().length() < 1)) {
//            String ref = genererReference(Constantes.TYPE_BLA_NAME, bean.getDateLivraison(), docAchat.getDepotReception().getId(), Constantes.DEPOT);
//           
//            
//        }
        bean.setNumDoc(ref);
        if (ref == null || ref.trim().equals("")) {
            return false;
        }
        if (!verifyDateAchat(bean.getDateDoc(), bean.getId() > 0)) {
            return false;
        }
        if (!verifyInventaire(bean.getDepotReception(), bean.getTranche(), bean.getDateLivraison())) {
            return false;
        }
        return verifyTranche(bean.getTranche(), bean.getDepotReception(), bean.getDateLivraison());
    }

    private boolean _controleFiche_(DocAchat bean, boolean control) {
        if (bean == null) {
            getErrorMessage("Le devez selectionner un document");
            return false;
        }
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false) {
                if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getId() < 1 : true) {
                    control = false;
                }
            }

        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE) && control) {
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
            getErrorMessage("Ce document est deja cloturer");
            return false;
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean _controleFiche_(YvsComContenuDocAchat bean, boolean msg) {
        if (bean == null) {
            return false;
        }
        if (selectDoc.getStatutLivre().equals(Constantes.ETAT_LIVRE) && !autoriser("fa_update_when_receive")) {
//            if (msg) {
//                openNotAccesAction("Cette ligne ne peux pas etre supprimé car la facture est déjà livrée");
//            }
//            return false;
        }
        if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
            String result = controleStock(bean.getArticle().getId(), (bean.getConditionnement() != null ? bean.getConditionnement().getId() : 0), selectDoc.getDepotReception().getId(), 0L, bean.getQuantiteRecu(), 0, "DELETE", "E", selectDoc.getDateLivraison(), (bean.getLot() != null ? bean.getLot().getId() : 0));
            if (result != null) {
                getErrorMessage("L'article '" + bean.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                return false;
            }
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            if (msg) {
                getErrorMessage("Cette ligne ne peux pas etre supprimé car elle est validée");
            }
            return false;
        }
        return true;
    }

    public boolean controleFicheContenu(ContenuDocAchat bean) {
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner un article");
            return false;
        }
        if (bean.getConditionnement() != null ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner un conditionnement");
            return false;
        }
        champ = new String[]{"article", "depot"};
        val = new Object[]{new YvsBaseArticles(bean.getArticle().getId()), new YvsBaseDepots(docAchat.getDepotReception().getId())};
        YvsBaseArticleDepot a = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
        if (a != null ? a.getId() < 1 : true) {
            getErrorMessage("Impossible d'enregistre car le dépôt ne possede pas cet article");
            return false;
        }
        if (a.getRequiereLot() ? (bean.getLot() != null ? bean.getLot().getId() < 1 : true) : false) {
            getErrorMessage("Un numéro de lot est requis pour cet article dans le dépôt");
            return false;
        }
        if (!bean.getDocAchat().getStatut().equals(Constantes.ETAT_EDITABLE) && !bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Vous ne pouvez pas modifier cette ligne car le document est " + giveNameStatut(bean.getDocAchat().getStatut()).toLowerCase() + " et la ligne est valide");
            return false;
        }
        if (bean.getDocAchat() != null ? bean.getDocAchat().getId() < 1 : true) {
            if (!_saveNew()) {
                return false;
            }
        }
        return _controleFiche_(bean.getDocAchat(), false);
    }

    @Override
    public void onSelectDistant(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Receptions Achat", "modGescom", "smenBonLivraisonAchat", true);
            }
        }
    }

    @Override
    public void populateView(DocAchat bean) {
        cloneObject(docAchat, bean);
        setIsBon(false);
        if (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false) {
            setIsBon(true);
        }
        update = true;
    }

    public void populateViewArticle(ContenuDocAchat bean) {
        selectArt = true;
        bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docAchat.getDepotReception().getId(), 0, 0, docAchat.getDateDoc(), bean.getConditionnement().getId(), bean.getLot().getId()));
        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
        cloneObject(contenu, bean);
        //controle les quantités déjà livré
        Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findByDocLierTypeStatutArticleS", new String[]{"docAchat", "statut", "typeDoc", "article", "unite"}, new Object[]{new YvsComDocAchats(facture.getId()), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, selectContenu.getArticle(), selectContenu.getConditionnement()});
        qteLivre = (qteLivre != null) ? qteLivre : 0;
        contenu.setSaveLivre(qteLivre);
        //trouve la quantité d'article facturé 
        Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticleFacture", new String[]{"docAchat", "article", "unite"}, new Object[]{new YvsComDocAchats(facture.getId()), selectContenu.getArticle(), selectContenu.getConditionnement()});
        qteFacture = (qteFacture != null) ? qteFacture : 0;
        contenu.setSaveFacture(qteFacture);
        //récupère la quantité de l'article dans le document de livraison en cours. (Le pb viens du fait que la ref d'un article peut se trouver plusieurs fois dans la liste d'un bl non encore livré)
        Double qteEncour = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticle", new String[]{"docAchat", "article", "unite"}, new Object[]{new YvsComDocAchats(docAchat.getId()), selectContenu.getArticle(), selectContenu.getConditionnement()});
        qteEncour = (qteEncour != null) ? qteEncour : 0;
        contenu.setSaveEncours(qteEncour);
        contenu.setQuantiteReste((qteFacture - qteLivre - qteEncour + bean.getQuantiteRecu()) < 0 ? 0 : (qteFacture - qteLivre - qteEncour + bean.getQuantiteRecu()));
        if (contenu.getArticle().getConditionnements().isEmpty()) {
            contenu.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(bean.getArticle().getId())}));
        }
        String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
        Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(bean.getArticle().getId(), 1), new Options(docAchat.getDepotReception().getId(), 2)});
        contenu.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
        update("desc_contenu_livraison_achat");
    }

    @Override
    public void resetFiche() {
        resetFiche_();
    }

    public void resetFiche_() {
        docAchat = new DocAchat();
        docAchat.setStatut(Constantes.ETAT_EDITABLE);
        docAchat.setGenererFactureAuto(currentParamAchat != null ? currentParamAchat.getGenererFactureAuto() : false);
        ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (m != null ? m.getDepots().contains(currentDepot) : false) {
            docAchat.setDepotReception(UtilCom.buildBeanDepot(currentDepot));
            loadAllTranche(currentDepot);
            if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
                docAchat.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
            }
        }
        tabIds = "";
        update = false;
        isBon = false;
        facture = new DocAchat();

        selectDoc = new YvsComDocAchats();

        contenus_fa.clear();
        resetFicheArticle();
        update("blog_form_livraison_achat");
    }

    public void resetFicheArticle() {
        contenu = new ContenuDocAchat();
        selectContenu = new YvsComContenuDocAchat();
        tabIds_article = "";
        selectArt = false;
        update("blog_article");
        update("desc_contenu_livraison_achat");
    }

    @Override
    public boolean saveNew() {
        if (_saveNew()) {
            actionOpenOrResetAfter(this);
            succes();
            return true;
        }
        return false;
    }

    public boolean _saveNew() {
        return _saveNew(recopieView());
    }

    public boolean _saveNew(DocAchat bean) {
        String action = bean.isUpdate() ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean)) {
                selectDoc = UtilCom.buildDocAchat(bean, currentUser, currentAgence);
                selectDoc.setDateLivraison(bean.getDateDoc());
                if (bean.getId() <= 0) {
                    selectDoc.setId(null);
                    selectDoc = (YvsComDocAchats) dao.save1(selectDoc);
                    docAchat.setId(selectDoc.getId());
                    documents.add(0, selectDoc);
                } else {
                    dao.update(selectDoc);
                    if (documents.contains(selectDoc)) {
                        documents.set(documents.indexOf(selectDoc), selectDoc);
                    }
                }
                docAchat.setUpdate(true);
                docAchat.setNumDoc(bean.getNumDoc());
                if (!docAchat.getContenus().isEmpty() && selectDoc.getContenus().isEmpty()) {
                    selectDoc.setContenus(new ArrayList<>(docAchat.getContenus()));
                }
                update("data_livraison_achat");
                update("form_entete_livraison_achat");
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return false;
    }

    public void saveNewArticle() {
        try {
            ContenuDocAchat bean = recopieViewArticle(docAchat);
            if (controleFicheContenu(bean)) {
                YvsComContenuDocAchat en = UtilCom.buildContenuDocAchat(bean, currentUser);
                if (bean.getId() <= 0) {
                    en.setId(null);
                    en = (YvsComContenuDocAchat) dao.save1(en);
                } else {
                    dao.update(en);
                }
                int idx = docAchat.getContenus().indexOf(en);
                if (idx >= 0) {
                    docAchat.getContenus().set(idx, en);
                } else {
                    docAchat.getContenus().add(0, en);
                }
                idx = docAchat.getContenusSave().indexOf(en);
                if (idx >= 0) {
                    docAchat.getContenusSave().set(idx, en);
                } else {
                    docAchat.getContenusSave().add(0, en);
                }
                idx = selectDoc.getContenus().indexOf(en);
                if (idx > -1) {
                    selectDoc.getContenus().set(idx, en);
                } else {
                    selectDoc.getContenus().add(0, en);
                }
                idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
                succes();
                super.setMontantTotalDoc(docAchat);
                resetFicheArticle();
                update("data_contenu_livraison_achat");
                update("data_livraison_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void buildToSend() {
        try {
            contenusRequireLot.clear();
            if (docAchat.getDepotReception() != null ? docAchat.getDepotReception().getId() > 0 : false) {
                Boolean requiere_lot = false;
                String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                for (YvsComContenuDocAchat c : contenus_fa) {
                    requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(docAchat.getDepotReception().getId(), 2)});
                    c.setRequiereLot(requiere_lot != null ? requiere_lot : false);
                    if (c.isRequiereLot()) {
                        ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                        if (w != null) {
                            c.setLots(w.loadList(docAchat.getDepotReception().getId(), c.getConditionnement().getId(), docAchat.getDateLivraison(), c.getQuantiteCommande(), 0));
                            double stock = 0;
                            for (YvsComLotReception y : c.getLots()) {
                                stock += y.getStock();
                            }
                            c.getArticle().setQuantite(stock);
                        }
                        contenusRequireLot.add(c);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void tryToSaveAllArticle() {
        buildToSend();
        if (contenusRequireLot != null ? contenusRequireLot.isEmpty() : true) {
            saveAllArticle();
        } else {
            openDialog("dlgListLotReception");
            update("blog-contenu_bla_require_lot");
        }
    }

    public void saveAllArticle() {
        try {
            if (contenus_fa != null ? contenus_fa.isEmpty() : true) {
                getInfoMessage("Cette facture est déja livrée ou n'a pas de contenu");
                return;
            }
            if (docAchat != null ? !docAchat.isUpdate() : true) {
                if (!_saveNew()) {
                    return;
                }
            }
            if (!_controleFiche_(docAchat, false)) {
                return;
            }
            ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
            YvsComContenuDocAchat en = null;
            List<YvsComContenuDocAchat> contenus = new ArrayList<>();
            for (YvsComContenuDocAchat c : contenus_fa) {
                champ = new String[]{"article", "depot"};
                val = new Object[]{c.getArticle(), new YvsBaseDepots(docAchat.getDepotReception().getId())};
                YvsBaseArticleDepot a = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                if (a != null ? a.getId() < 1 : true) {
                    getErrorMessage("Impossible d'enregistre car le dépôt ne possede pas l'article " + c.getArticle().getDesignation());
                    return;
                }
                if (a.getRequiereLot()) {
                    int index = contenusRequireLot.indexOf(c);
                    if (index < 0) {
                        getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le depot");
                        return;
                    }
                    YvsComLotReception lot = contenusRequireLot.get(index).getLot();
                    if (m != null && (lot != null ? lot.getId() < 1 : false)) {
                        c.setLot(m._saveNew(lot.getNumero(), new Articles(c.getArticle().getId(), c.getArticle().getRefArt(), c.getArticle().getDesignation()), lot.getDateFabrication(), lot.getDateExpiration()));
                    }
                    if (c.getLot() != null ? c.getLot().getId() < 1 : true) {
                        getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le depot");
                        return;
                    }
                }
                en = new YvsComContenuDocAchat(null, c);
                en.setAuthor(currentUser);
                en.setDateSave(new Date());
                en.setDateUpdate(new Date());
                en.setParent(new YvsComContenuDocAchat(c.getId()));
                en.setDocAchat(selectDoc);
                en.setNew_(true);
                en = (YvsComContenuDocAchat) dao.save1(en);
                if (en != null ? en.getId() > 0 : false) {
                    contenu.setId(en.getId());
                    docAchat.getContenus().add(0, en);
                    contenus.add(c);
                }
            }
            contenus_fa.removeAll(contenus);
            Map<String, String> statuts = dao.getEquilibreAchat(facture.getId());
            if (statuts != null) {
                facture.setStatutLivre(statuts.get("statut_livre"));
                facture.setStatutRegle(statuts.get("statut_regle"));
            }
            resetFicheArticle();
            succes();
            update("data_contenu_fa_bla");
            update("data_livraison_achat");
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void definedLotBonus() {
        try {
            if (!docAchat.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
                return;
            }
            if (docAchat.isCloturer()) {
                getErrorMessage("Ce document est deja cloturer");
                return;
            }
            dao.update(selectContenu);
            int idx = selectDoc.getContenus().indexOf(selectContenu);
            if (idx > -1) {
                selectDoc.getContenus().set(idx, selectContenu);
            }
            succes();
            update("data_contenu_livraison_achat");
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("bla_delete_doc")) {
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
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(bean);
                    if (bean.getId() == docAchat.getId()) {
                        resetFiche();
                    }
                }
                documents.removeAll(list);
                succes();
                update("data_livraison_achat");
                tabIds = "";
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComDocAchats y) {
        selectDoc = y;
    }

    public boolean deleteBean_() {
        try {
            if (!autoriser("bla_delete_doc")) {
                openNotAcces();
                return false;
            }
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc)) {
                    return false;
                }
                dao.delete(selectDoc);
                if (documents.contains(selectDoc)) {
                    documents.remove(selectDoc);
                }
                if (selectDoc.getId() == docAchat.getId()) {
                    resetFiche();
                }
                succes();
                update("data_livraison_achat");
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
                    dao.delete(new YvsComContenuDocAchat(id));
                    docAchat.getContenus().remove(new YvsComContenuDocAchat(id));
                    docAchat.getContenusSave().remove(new YvsComContenuDocAchat(id));
                    if (id == contenu.getId()) {
                        resetFicheArticle();
                    }
                }
                succes();
                update("data_contenu_livraison_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanArticle_(YvsComContenuDocAchat y) {
        selectContenu = y;
    }

    public void deleteBeanArticle_() {
        try {
            if (_controleFiche_(selectContenu, true)) {
                dao.delete(selectContenu);
                docAchat.getContenus().remove(selectContenu);
                docAchat.getContenusSave().remove(selectContenu);
                if (selectContenu.getId() == contenu.getId()) {
                    resetFicheArticle();
                }
                super.setMontantTotalDoc(docAchat);
                equilibre();
                update("chp_bla_net_a_payer");
                update("data_contenu_livraison_achat");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsComDocAchats y) {
        selectDoc = y;
        facture = new DocAchat();
        populateView(UtilCom.buildBeanDocAchat(y));
        resetFicheArticle();
        docAchat.setContenus(loadContenus(y));
        if (!documents.contains(y)) {
            documents.add(0, y);
        }
        isFacture = selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getId() > 0 : false;
        if (isFacture) {
            champ = new String[]{"id"};
            val = new Object[]{selectDoc.getDocumentLie().getId()};
            YvsComDocAchats d = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", champ, val);
            if (d != null ? d.getId() > 0 : false) {
                facture = UtilCom.buildBeanDocAchat(d);
                super.setMontantTotalDoc(this.facture);
                loadContenuFacture(d, false);
                docAchat.setDocumentLie(facture);
            }
        }
        ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (w != null) {
            if (y.getDepotReception() != null ? y.getDepotReception().getId() > 0 ? y.getDepotReception().getAgence() != null ? y.getDepotReception().getAgence().getId() > 0 : false : false : false) {
                if (w.getAgenceFind() != y.getDepotReception().getAgence().getId()) {
                    w.setAgenceFind(y.getDepotReception().getAgence().getId());
                    w.loadDepotsByAgence(w.getAgenceFind(), true, false);
                }
                if (!w.getDepots().contains(y.getDepotReception())) {
                    w.getDepots().add(y.getDepotReception());
                }
            }
        }
        super.setMontantTotalDoc(docAchat);
        loadAllTranche(selectDoc.getDepotReception());
        update("blog_form_livraison_achat");
        update("blog_form_lot_livraison_achat");
        update("select_lot_livraison_achat");
        update("data_contenu_livraison_achat");
        update("save_livraison_achat");
    }

    public void onSelectDistantObject(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureAchat s = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (s != null) {
                s.onSelectDistant((YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{y.getId()}));
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocAchats y = (YvsComDocAchats) ev.getObject();
            onSelectObject((y));
            tabIds = "" + documents.indexOf(y);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche_();
        update("blog_form_livraison_achat");
    }

    public void loadOnViewDoc(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocAchats bean = (YvsComDocAchats) ev.getObject();
            chooseDocAchat(new DocAchat(bean.getId()));
//            onSelectFacture(new YvsComDocAchats(bean), docAchat.getId() < 1);
        }
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectContenu = (YvsComContenuDocAchat) ev.getObject();
            populateViewArticle(UtilCom.buildBeanContenuDocAchat(selectContenu));
            update("blog_form_contenu_livraison_achat");
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheArticle();
        update("blog_form_contenu_livraison_achat");
    }

    public ContenuDocAchat buildContent(YvsComContenuDocAchat bean) {
        ContenuDocAchat c = new ContenuDocAchat();
        if (this.contenu.getId() <= 0) {
            cloneObject(c, UtilCom.buildBeanContenuDocAchat(bean));
            c.setId(0);
        } else {
            cloneObject(c.getArticle(), UtilProd.buildBeanArticles(bean.getArticle()));
            c.setQuantiteCommende(bean.getQuantiteCommande());
            c.setPrixAchat(bean.getPrixAchat());
            c.setRemiseRecu(bean.getRemise());
            c.setTaxe(bean.getTaxe());
            c.setQuantiteRecu(bean.getQuantiteRecu());
            c.setTaxeRecu(bean.getTaxe());
        }
        c.setParent(new ContenuDocAchat(bean.getSave() > 0 ? bean.getSave() : bean.getId()));
        if (c.getConditionnement() != null ? c.getConditionnement().getId() > 0 : false) {
            if (docAchat.getDepotReception() != null ? docAchat.getDepotReception().getId() > 0 : false) {
                c.getArticle().setStock(dao.stocks(c.getArticle().getId(), 0, docAchat.getDepotReception().getId(), 0, 0, docAchat.getDateLivraison(), c.getConditionnement().getId(), c.getLot().getId()));
            } else {
                c.getArticle().setStock(dao.stocks(c.getArticle().getId(), 0, 0, currentAgence.getId(), 0, docAchat.getDateLivraison(), c.getConditionnement().getId(), c.getLot().getId()));
            }
        }
        c.getArticle().setPua(dao.getPua(c.getArticle().getId(), docAchat.getFournisseur().getId()));
        //controle les quantités déjà livré
        Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findByDocLierTypeStatutArticleS", new String[]{"docAchat", "statut", "typeDoc", "article", "unite"}, new Object[]{new YvsComDocAchats(facture.getId()), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, bean.getArticle(), bean.getConditionnement()});
        qteLivre = (qteLivre != null) ? qteLivre : 0;
        c.setSaveLivre(qteLivre);
        //triuve la quantité d'article facturé 
        Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticleFacture", new String[]{"docAchat", "article", "unite"}, new Object[]{new YvsComDocAchats(facture.getId()), bean.getArticle(), bean.getConditionnement()});
        qteFacture = (qteFacture != null) ? qteFacture : 0;
        c.setSaveFacture(qteFacture);
        Double qteBonus = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteBonusByArticle", new String[]{"docAchat", "article", "unite"}, new Object[]{new YvsComDocAchats(facture.getId()), bean.getArticle(), bean.getConditionnement()});
        qteBonus = (qteBonus != null) ? qteBonus : 0;
        c.setSaveBonus(qteBonus);
        if (bean.isBonus()) {
            c.setSaveFacture(qteBonus + qteFacture);
        }
        //récupère la quantité de l'article dans le document de livraison en cours. (Le pb viens du fait que la ref d'un article peut se trouver plusieurs fois dans la liste d'un bl non encore livré)
        Double qteEncour = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticle", new String[]{"docAchat", "article", "unite"}, new Object[]{new YvsComDocAchats(docAchat.getId()), bean.getArticle(), bean.getConditionnement()});
        qteEncour = (qteEncour != null) ? qteEncour : 0;
        c.setSaveEncours(qteEncour);
        if (!bean.getDocAchat().getStatutRegle().equals(Constantes.ETAT_REGLE)) {
            double reste = (c.getSaveFacture() - c.getSaveLivre() - c.getSaveEncours());
            c.setQuantiteRecu(reste < 0 ? 0 : (!bean.isBonus() ? reste : reste > c.getSaveBonus() ? c.getSaveBonus() : reste));
        } else {
            c.setQuantiteCommende(c.getQuantiteRecu());
        }
        selectArt = true;
        c.setDateContenu(new Date());
        return c;
    }

    public void loadOnViewArticleContenu(SelectEvent ev) {
        selectArt = false;
        if (docAchat.getDepotReception() != null ? docAchat.getDepotReception().getId() > 0 : false) {
            if ((ev != null) ? ev.getObject() != null : false) {
                YvsComContenuDocAchat y = (YvsComContenuDocAchat) ev.getObject();
                if (!checkOperationArticle(y.getArticle().getId(), docAchat.getDepotReception().getId(), Constantes.ACHAT)) {
                    getWarningMessage("L'article '" + y.getArticle().getDesignation() + "' ne fait pas d'achat dans le depot '" + docAchat.getDepotReception().getDesignation() + "'");
                }

                ContenuDocAchat c = UtilCom.buildBeanContenuDocAchat(y);
                champ = new String[]{"article", "depot"};
                val = new Object[]{y.getArticle(), new YvsBaseDepots(docAchat.getDepotReception().getId())};
                YvsBaseArticleDepot a = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                if (a != null ? a.getId() > 0 : false) {
                    YvsComContenuDocAchat bean = new YvsComContenuDocAchat(y);
                    bean.setRequiereLot(a.getRequiereLot());
                    contenu = buildContent(bean);
                    contenu.getArticle().setRequiereLot(a.getRequiereLot());
                    update("blog_form_article_livraison_achat");
                } else {
                    ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                    if (m != null) {
                        ArticleDepot b = new ArticleDepot();
                        b.setArticle(c.getArticle());
                        b.setDepot(docAchat.getDepotReception());
                        m.setEntityDepot(new YvsBaseDepots(b.getDepot().getId()));
                        m.setOnArticle(false);
                        m.setArticle(b);
                    }
                    openDialog("dlgConfirmAddArticle");
                    update("blog_form_article_depot_bla");
                }
            } else {
                resetFicheArticle();
            }
        } else {
            getErrorMessage("Vous devez preciser le depot de reception");
            resetFicheArticle();
        }
        update("desc_contenu_livraison_achat");
    }

    public void unLoadOnViewArticle(UnselectEvent ev) {
        contenu = new ContenuDocAchat();
        contenu.setArticle(new Articles());
        contenu.setDocAchat(new DocAchat());
        update("blog_form_contenu_livraison_achat");
    }

    public void onSelectFacture(YvsComDocAchats y, boolean recopie) {
        List<YvsComContenuDocAchat> list = new ArrayList<>();
        list.addAll(y.getContenus());
        if (docAchat.getId() > 0) {
            for (YvsComContenuDocAchat c : docAchat.getContenusSave()) {
                boolean existe = false;
                for (YvsComContenuDocAchat cf : list) {
                    if (c.getConditionnement().equals(cf.getConditionnement())) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    getErrorMessage("Ce bon ne peut pas étre rattaché à cette facture.. car il y'a incoherence sur l'article " + c.getArticle().getDesignation());
                    return;
                }
            }
        }

        setIsBon(true);
        this.facture = UtilCom.buildBeanDocAchat(y);
        super.setMontantTotalDoc(this.facture);
        loadContenuFacture(y, recopie);
        this.facture.setContenus(list);
        chooseDepot();
        resetFicheArticle();
        if (docAchat.getId() < 1) {
            docAchat.getContenus().clear();
        }
        update("select_lot_livraison_achat");
        update("data_contenu_livraison_achat");
        update("blog_article_livraison_achat");
        update("select_document_facture_achat");
    }

    public void searchFacture() {
        String num = facture.getNumDoc();
        facture = new DocAchat(num, true);
        contenus_fa.clear();
        setIsBon(false);
        ManagedFactureAchat m = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
        if (m != null) {
            DocAchat y = m.searchFacture(num, null, true);
            if (m.getDocuments() != null ? !m.getDocuments().isEmpty() : false) {
                if (m.getDocuments().size() > 1) {
                    update("data_fa_livraison");
                } else {
                    chooseDocAchat(y);
                }
                facture.setError(false);
            }
        }
    }

    public void chooseDocAchat(DocAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureAchat m = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (m != null) {
                int idx = m.getDocuments().indexOf(new YvsComDocAchats(y.getId()));
                if (idx > -1) {
                    YvsComDocAchats bean = m.getDocuments().get(idx);
                    onSelectFacture(new YvsComDocAchats(bean), docAchat.getId() < 1);
                }
            }
        } else {
            resetFiche();
        }
    }

    public void chooseDepot() {
        if ((docAchat.getDepotReception() != null) ? docAchat.getDepotReception().getId() > 0 : false) {
            ManagedDepot m = (ManagedDepot) giveManagedBean(ManagedDepot.class);
            if (m != null) {
                YvsBaseDepots y = m.getDepots().get(m.getDepots().indexOf(new YvsBaseDepots(docAchat.getDepotReception().getId())));
                Depots d = UtilCom.buildBeanDepot(y);
                cloneObject(docAchat.getDepotReception(), d);
                loadAllTranche(y);
                update("blog_form_lot_livraison_achat");
                update("blog_form_lot_depot_bla");
            }
        }
    }

    public void chooseLot(boolean bonus) {
        if ((contenu.getLot() != null) ? contenu.getLot().getId() > 0 : false) {
            ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
            if (m != null) {
                int idx = m.getLots().indexOf(new YvsComLotReception(contenu.getLot().getId()));
                if (idx > -1) {
                    contenu.setLot(UtilCom.buildBeanLotReception(m.getLots().get(idx)));
                }
            }
        }
    }

    public void chooseFacture() {
        ParametreRequete p;
        if (numFacture != null ? numFacture.trim().length() > 0 : false) {
            p = new ParametreRequete("y.documentLie.numDoc", "numDoc", "%" + numFacture + "%", " LIKE ", "AND");
        } else {
            p = new ParametreRequete("y.documentLie.numDoc", "numDoc", null, " LIKE ", "AND");
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void chooseStatut(ValueChangeEvent ev) {
        statut_ = ((String) ev.getNewValue());
        addParamStatut();
    }

    public void addParamStatut() {
        ParametreRequete p;
        if (!(statut_ == null || statut_.trim().equals(""))) {
            p = new ParametreRequete("y.statut", "statut", statut_, egaliteStatut, "AND");
        } else {
            p = new ParametreRequete("statut", "statut", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllBons(true, true);
    }

    public void addParamLie() {
        ParametreRequete p = new ParametreRequete("y.documentLie", "documentLie", null, "=", "AND");
        if (lieSearch != null) {
            p = new ParametreRequete("y.documentLie", "documentLie", (lieSearch ? "IS NOT NULL" : "IS NULL"), (lieSearch ? "IS NOT NULL" : "IS NULL"), "AND");
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("y.cloturer", "cloturer", cloturer_);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void chooseDateSearch() {
        ParametreRequete p;
        if (date_) {
            p = new ParametreRequete("y.dateDoc", "date", dateDebut_);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin_);
        } else {
            p = new ParametreRequete("y.dateDoc", "date", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
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
                param.put("TITRE_DOC", Constantes.DOCUMENT_BON_LIVRAISON_ACHAT);
                String report = "bon_reception";
                if (currentParam != null ? currentParam.getUseLotReception() : false) {
                    report = "bon_reception_by_lot";
                }
                executeReport(report, param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendByMail() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            YvsBaseFournisseur fsseur = selectDoc.getFournisseur();
            String email = fsseur.getTiers().getEmail();
            if ((email != null) ? !email.equals("") : false) {
                if (Util.correctEmail(email)) {
                    changeStatut(Constantes.ETAT_SOUMIS, selectDoc, false);
                } else {
                    getErrorMessage("Impossible d'envoyer! Email Incorrect");
                }
            } else {
                getErrorMessage("Impossible d'envoyer! Le fournisseur n'a pas d'email");
            }
        }
    }

    public void clearParams() {
        entete_ = 0;
        tranche_ = 0;
        depot_ = 0;
        agence_ = 0;

        codeFsseur_ = null;
        numSearch_ = null;
        statut_ = null;
        codeVendeur_ = null;
        statutLivre_ = null;
        statutRegle_ = null;
        cloturer_ = null;
        idsSearch = "";

        date_ = false;
        dateDebut_ = new Date();
        dateFin_ = new Date();
        paginator.getParams().clear();
        loadAllBons(true, true);
        update("blog_plus_option_livraison_achat");
    }

    public boolean annulerOrderByForce() {
        return annulerOrder(_entity_, _bean_, _load_, _msg_, _open_, true);
    }

    public boolean annulerOrder(boolean force) {
        return annulerOrder(selectDoc, docAchat, true, true, true, force);
    }

    public boolean annulerOrder(YvsComDocAchats y, boolean load, boolean msg, boolean open, boolean force) {
        return annulerOrder(y, UtilCom.buildBeanDocAchat(y), load, msg, open, force);
    }

    public boolean annulerOrder(YvsComDocAchats entity, DocAchat bean, boolean load, boolean msg, boolean open, boolean force) {
        return annulerOrder(entity, bean, load, msg, open, force, true);
    }

    public boolean refuserOrder(boolean force) {
        return refuserOrder(selectDoc, docAchat, true, true, true, force);
    }

    public boolean refuserOrder(YvsComDocAchats y, boolean load, boolean msg, boolean open, boolean force) {
        return refuserOrder(y, UtilCom.buildBeanDocAchat(y), load, msg, open, force);
    }

    public boolean refuserOrder(YvsComDocAchats entity, DocAchat bean, boolean load, boolean msg, boolean open, boolean force) {
        return annulerOrder(entity, bean, load, msg, open, force, false);
    }

    public boolean annulerOrder(YvsComDocAchats entity, DocAchat bean, boolean load, boolean msg, boolean open, boolean force, boolean annuler) {
        try {
            if (!autoriser("bla_editer_doc")) {
                openNotAcces();
                return false;
            }
            if (entity != null ? entity.getId() > 0 : false) {
                // Vérifié qu'aucun document d'inventaire n'exite après cette date
                boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
                List<YvsComDocAchats> l = dao.loadNameQueries("YvsComDocAchats.findByParent", new String[]{"documentLie"}, new Object[]{entity});
                if (force) {
                    List<YvsComDocAchats> list = new ArrayList<>();
                    list.addAll(l);
                    for (YvsComDocAchats d : list) {
                        dao.delete(d);
                        l.remove(d);
                    }
                }

                if (!force || _exist_inventaire_ == null) {
                    _exist_inventaire_ = !controleInventaire(bean.getDepotReception().getId(), bean.getDateLivraison(), bean.getTranche().getId(), !gescom_update_stock_after_valide);
                    if (_exist_inventaire_) {
                        if (!gescom_update_stock_after_valide) {
                            return false;
                        } else if (!force) {
                            _entity_ = entity;
                            _bean_ = bean;
                            _open_ = open;
                            _load_ = load;
                            _msg_ = msg;
                            openDialog("dlgConfirmChangeInventaireByCancel");
                            return false;
                        }
                    }
                }
                if (l != null ? !l.isEmpty() : false) {
                    for (YvsComDocAchats d : l) {
                        if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                            if (msg) {
                                getErrorMessage("Impossible d'annuler cet ordre car il possède un transfert déja valide");
                            }
                            return false;
                        }
                    }
                    if (open) {
                        openDialog(annuler ? "dlgConfirmAnnuler" : "dlgConfirmRefuser");
                    }
                    return false;
                }
                //controle la cohérence des stocks
                String result;
                for (YvsComContenuDocAchat c : entity.getContenus()) {
                    result = controleStock(c.getArticle().getId(), (c.getConditionnement() != null ? c.getConditionnement().getId() : 0), entity.getDepotReception().getId(), 0L, c.getQuantiteRecu(), 0, "INSERT", "S", entity.getDateLivraison(), (c.getLot() != null ? c.getLot().getId() : 0));
                    if (result != null) {
                        getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' est insuffisant en stock pour effectuer cette action ou entrainera un stock négatif à la date " + result);
                        return false;
                    }
                }

                YvsComDocStocks inventaire = null;
                if (_exist_inventaire_) {
                    inventaire = dao.lastInventaire(entity.getDepotReception().getId(), entity.getDateLivraison(), entity.getTranche().getId());
                }
                if (_exist_inventaire_ ? inventaire != null ? inventaire.getId() > 0 : false : true) {
                    if (changeStatut(annuler ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ANNULE, bean, entity, load)) {
                        entity.setCloturer(false);
                        entity.setAnnulerBy(null);
                        entity.setValiderBy(null);
                        entity.setCloturerBy(null);
                        entity.setDateAnnuler(null);
                        entity.setDateCloturer(null);
                        entity.setDateValider(null);
                        entity.setStatut(annuler ? Constantes.ETAT_EDITABLE : Constantes.ETAT_ANNULE);
                        entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_ATTENTE));
                        entity.setDateLivraison(null);
                        entity.setAuthor(currentUser);
                        if (_exist_inventaire_) {
                            if (inventaire != null ? inventaire.getId() > 0 : false) {
                                for (YvsComContenuDocAchat c : entity.getContenus()) {
                                    majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantiteCommande(), Constantes.MOUV_SORTIE);
                                }
                            }
                        }
                        YvsComDocAchats y = new YvsComDocAchats(entity);
                        List<YvsComContenuDocAchat> temps = new ArrayList<>(y.getContenus());
                        y.getContenus().clear();
                        dao.update(y);
                        if (entity.getDocumentLie() != null ? (entity.getDocumentLie().getId() != null ? entity.getDocumentLie().getId() > 0 : false) : false) {
                            Map<String, String> statuts = dao.getEquilibreAchat(entity.getDocumentLie().getId());
                            if (statuts != null) {
                                entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                                entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                            }
                        }
                        y.setContenus(temps);
                        entity.setContenus(temps);
                        bean.setContenus(temps);
                        for (YvsComContenuDocAchat c : temps) {
                            c.setStatut(Constantes.ETAT_VALIDE);
                            changeStatutLine(c, false);
                        }
                        _exist_inventaire_ = null;
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
        return false;
    }
    YvsComDocAchats _entity_;
    DocAchat _bean_;
    boolean _save_, _load_, _msg_, _open_;
    Boolean _exist_inventaire_;

    public void validerOrderAll() {
        try {
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

    public boolean validerOrderOne(YvsComDocAchats entity, boolean succes, boolean controle) {
        if (!entity.getStatut().equals(Constantes.ETAT_VALIDE)) {
            DocAchat bean = UtilCom.buildBeanDocAchat(entity);
            boolean response = validerOrder(entity, bean, false, false, true, null, false, controle);
            if (response && succes) {
                succes();
            }
            return response;
        }
        return false;
    }

    public void validerOrder() {
        validerOrder(selectDoc, docAchat, true, true, true, null, false);
    }

    public boolean validerOrder(YvsComDocAchats y, boolean save, boolean load, boolean msg, Boolean exist_inventaire, boolean force) {
        return validerOrder(y, UtilCom.buildBeanDocAchat(y), save, load, msg, exist_inventaire, force);
    }

    public boolean validerOrderByForce() {
        return validerOrder(_entity_, _bean_, _save_, _load_, _msg_, _exist_inventaire_, true);
    }

    public boolean validerOrder(YvsComDocAchats entity, DocAchat bean, boolean save, boolean load, boolean msg, Boolean exist_inventaire, boolean force) {
        return validerOrder(entity, bean, save, load, msg, exist_inventaire, force, true);
    }

    public boolean validerOrder(YvsComDocAchats entity, DocAchat bean, boolean save, boolean load, boolean msg, Boolean exist_inventaire, boolean force, boolean controle) {
        if (entity == null) {
            return false;
        }
        if (!autoriser("bla_valide_doc")) {
            openNotAcces();
            return false;
        }
        if (!verifyOperation(bean.getDepotReception(), Constantes.ENTREE, Constantes.ACHAT, msg)) {
            return false;
        }
        //contrôle la cohérence avec les inventaires
        boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
        if (exist_inventaire == null) {
            exist_inventaire = !verifyInventaire(bean.getDepotReception(), bean.getTranche(), bean.getDateDoc(), (gescom_update_stock_after_valide ? false : msg));
        }
        if (exist_inventaire) {
            if (!gescom_update_stock_after_valide) {
                return false;
            } else if (!force) {
                _entity_ = entity;
                _bean_ = bean;
                _save_ = save;
                _load_ = load;
                _exist_inventaire_ = exist_inventaire;
                _msg_ = msg;
                if (controle) {
                    openDialog("dlgConfirmChangeInventaireByValid");
                }
                return false;
            }
        }
        boolean continu = !save;
        if (save) {
            continu = _saveNew(bean);
        }
        if (continu) {
            if (livrer(entity, bean, msg, exist_inventaire, force)) {
                if (changeStatut(Constantes.ETAT_VALIDE, bean, entity, load)) {
                    entity.setCloturer(false);
                    entity.setAnnulerBy(null);
                    entity.setValiderBy(currentUser.getUsers());
                    entity.setDateAnnuler(null);
                    entity.setDateCloturer(null);
                    entity.setDateValider(bean.getDateLivraison());
                    entity.setDateLivraison(bean.getDateLivraison());
                    entity.setDateUpdate(new Date());
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        entity.setAuthor(currentUser);
                    }
                    entity.setStatut(Constantes.ETAT_VALIDE);
                    YvsComDocAchats y = new YvsComDocAchats(entity);
                    List<YvsComContenuDocAchat> temps = new ArrayList<>(y.getContenus());
                    y.getContenus().clear();
                    dao.update(y);
                    if (entity.getDocumentLie() != null ? (entity.getDocumentLie().getId() != null ? entity.getDocumentLie().getId() > 0 : false) : false) {
                        Map<String, String> statuts = dao.getEquilibreAchat(entity.getDocumentLie().getId());
                        if (statuts != null) {
                            entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                            entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                        }
                    }
                    y.setContenus(temps);
                    entity.setContenus(temps);
                    bean.setContenus(temps);
                    for (YvsComContenuDocAchat c : temps) {
                        c.setStatut(Constantes.ETAT_EDITABLE);
                        changeStatutLine(c, false);
                    }
                    if (bean.isGenererFactureAuto()) {
                        YvsComDocAchats d = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findLieById", new String[]{"id"}, new Object[]{bean.getId()});
                        if (d != null ? d.getId() < 1 : true) {
                            genereFactureAchat(entity, true);
                        }
                    }
                    return true;
                }
            }
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

    public boolean livrer(YvsComDocAchats entity, DocAchat bean, boolean msg, Boolean exist_inventaire, boolean force) {
        if (entity == null) {
            return false;
        }
        if (bean.getDepotReception() != null ? bean.getDepotReception().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez specifier le dépot de livraison");
            }
            return false;
        }
        if (bean.getTranche() != null ? bean.getTranche().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez specifier la tranche de livraison");
            }
            return false;
        }//verifier la cohérence avec les inventaires
        boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
        if (exist_inventaire == null) {
            exist_inventaire = !controleInventaire(entity.getDepotReception().getId(), entity.getDateLivraison(), entity.getTranche().getId(), (gescom_update_stock_after_valide ? false : msg));
        }
        if (exist_inventaire) {
            if (!gescom_update_stock_after_valide) {
                return false;
            } else if (!force) {
                _entity_ = entity;
                _bean_ = bean;
                _msg_ = msg;
                openDialog("dlgConfirmChangeInventaireByValid");
                return false;
            }
        }
        ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
        if (service != null && bean.getDocumentLie() != null) {
            bean.getDocumentLie().setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidFactureAchat.findByFacture", new String[]{"facture"}, new Object[]{entity.getDocumentLie()}));
            bean.getDocumentLie().setEtapesValidations(service.ordonneEtapes(bean.getDocumentLie().getEtapesValidations()));
            for (YvsWorkflowValidFactureAchat etape : bean.getDocumentLie().getEtapesValidations()) {
                if (etape.getEtape().getLivraisonHere() && etape.getEtapeValid()) {
                    bean.getDocumentLie().setLivraisonDo(true);
                }
            }

        }
        if (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false) {
            if (!bean.getDocumentLie().isLivraisonDo() && !bean.getDocumentLie().getStatut().equals(Constantes.ETAT_VALIDE)) {
                if (msg) {
                    getErrorMessage("La facture n'est pas encore au niveau possible de livraison");
                }
                return false;
            }
        }
        if (entity.getContenus() != null ? !entity.getContenus().isEmpty() : false) {
            for (YvsComContenuDocAchat c : entity.getContenus()) {
                //controle les quantités déjà livré
                Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findByDocLierTypeStatutArticleS", new String[]{"docAchat", "statut", "typeDoc", "article", "unite"}, new Object[]{entity.getDocumentLie(), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, c.getArticle(), c.getConditionnement()});
                qteLivre = (qteLivre != null) ? qteLivre : 0;
                //trouve la quantité d'article facturé 
                Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticleFacture", new String[]{"docAchat", "article", "unite"}, new Object[]{entity.getDocumentLie(), c.getArticle(), c.getConditionnement()});
                qteFacture = (qteFacture != null) ? qteFacture : 0;
                //récupère la quantité de l'article dans le document de livraison en cours. (Le pb viens du fait que la ref d'un article peut se trouver plusieurs fois dans la liste d'un bl non encore livré)
                Double qteEncour = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findQteByArticle", new String[]{"docAchat", "article", "unite"}, new Object[]{entity, c.getArticle(), c.getConditionnement()});
                qteEncour = (qteEncour != null) ? qteEncour : 0;
                if (qteEncour > (qteFacture - qteLivre)) {
                }
                champ = new String[]{"article", "depot"};
                val = new Object[]{c.getArticle(), new YvsBaseDepots(bean.getDepotReception().getId())};
                YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                if (y != null ? y.getId() < 1 : true) {
                    if (msg) {
                        getErrorMessage("Impossible d'effectuer cette action... Car le depot " + bean.getDepotReception().getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
                    }
                    return false;
                }
                if (y.getRequiereLot() ? (c.getLot() != null ? c.getLot().getId() < 1 : true) : false) {
                    if (msg) {
                        getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le depot " + bean.getDepotReception().getDesignation());
                    }
                    return false;
                }
                if (!checkOperationArticle(c.getArticle().getId(), bean.getDepotReception().getId(), Constantes.ACHAT)) {
                    if (msg) {
                        getErrorMessage("L'article '" + c.getArticle().getDesignation() + "' ne fait pas d'achat dans le depot '" + bean.getDepotReception().getDesignation() + "'");
                    }
                    return false;
                }
            }
            if (exist_inventaire) {
                YvsComDocStocks inventaire = dao.lastInventaire(entity.getDepotReception().getId(), entity.getDateLivraison(), entity.getTranche().getId());
                if (inventaire != null ? inventaire.getId() > 0 : false) {
                    for (YvsComContenuDocAchat c : entity.getContenus()) {
                        majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantiteCommande(), Constantes.MOUV_ENTREE);
                    }
                }
            }
            exist_inventaire = null;
            bean.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entity.setDateLivraison(bean.getStatutLivre().equals(String.valueOf(Constantes.STATUT_DOC_LIVRER)) ? new Date() : null);
            update("data_livraison_achat");
            return true;
        } else {
            if (msg) {
                getErrorMessage("Vous ne pouvez pas valider un document vide");
            }
        }
        return false;
    }

    public void cloturer(YvsComDocAchats y) {
        selectDoc = y;
        update("id_confirm_close_bla");
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        docAchat.setCloturer(!docAchat.isCloturer());
        selectDoc.setCloturer(docAchat.isCloturer());
        selectDoc.setDateCloturer(docAchat.isCloturer() ? new Date() : null);
        selectDoc.setAuthor(currentUser);
        dao.update(selectDoc);
        if (documents.contains(selectDoc)) {
            documents.set(documents.indexOf(selectDoc), selectDoc);
        }
        update("data_livraison_achat");
    }

    public boolean changeStatut(String etat) {
        if (changeStatut_(etat, true)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, boolean load) {
        return changeStatut_(etat, docAchat, selectDoc, load);
    }

    public boolean changeStatut(String etat, YvsComDocAchats entity, boolean load) {
        if (changeStatut_(etat, entity, load)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, YvsComDocAchats entity, boolean load) {
        return changeStatut_(etat, UtilCom.buildBeanDocAchat(entity), entity, load);
    }

    public boolean changeStatut(String etat, DocAchat bean, YvsComDocAchats entity, boolean load) {
        if (changeStatut_(etat, bean, entity, load)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, DocAchat bean, YvsComDocAchats entity, boolean load) {
        if (!etat.equals("")) {
            if (bean.isCloturer()) {
                getErrorMessage("Ce document est vérouillé");
                return false;
            }
            String rq = "UPDATE yvs_com_doc_achats SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
//            dao.requeteLibre(rq, param);
            bean.setStatut(etat);
            entity.setStatut(etat);
            int idx = documents.indexOf(entity);
            if (idx > -1) {
                documents.set(idx, entity);
            }
            if (load) {
                ManagedFactureAchat m = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                if (m != null) {
                    m.loadFactureNonLivre(true, true);
                    update("data_fa_livraison");
                }
            }
            update("infos_document_livraison_achat");
            update("data_livraison_achat");
            update("data_contenu_livraison_achat");
            return true;
        }
        return false;
    }

    public void changeStatutLine(YvsComContenuDocAchat y) {
        changeStatutLine(y, true);
    }

    public void changeStatutLine(YvsComContenuDocAchat y, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            if (!y.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                if (!autoriser("bla_editer_doc")) {
                    openNotAcces();
                    return;
                }
                y.setStatut(Constantes.ETAT_EDITABLE);
                y.setAuthor(currentUser);
                dao.update(y);
                int idx = docAchat.getContenus().indexOf(y);
                if (idx > -1) {
                    docAchat.getContenus().set(idx, y);
                    update("data_contenu_livraison_achat");
                }
                idx = documents.indexOf(new YvsComDocAchats(docAchat.getId()));
                if (idx > -1) {
                    int i = documents.get(idx).getContenus().indexOf(y);
                    if (i > -1) {
                        documents.get(idx).getContenus().set(i, y);
                    }
                }
                if (msg) {
                    succes();
                }
            } else {
                if (docAchat.getStatut().equals(Constantes.ETAT_VALIDE)) {
                    y.setStatut(Constantes.ETAT_VALIDE);
                    y.setAuthor(currentUser);
                    dao.update(y);
                    int idx = docAchat.getContenus().indexOf(y);
                    if (idx > -1) {
                        docAchat.getContenus().set(idx, y);
                        update("data_contenu_livraison_achat");
                    }
                    idx = documents.indexOf(new YvsComDocAchats(docAchat.getId()));
                    if (idx > -1) {
                        int i = documents.get(idx).getContenus().indexOf(y);
                        if (i > -1) {
                            documents.get(idx).getContenus().set(i, y);
                        }
                    }
                    if (msg) {
                        succes();
                    }
                } else {
                    if (msg) {
                        getErrorMessage("Impossible de valider cette ligne car le document n'est pas valide");
                    }
                }
            }
            equilibre();
        }
    }

    public void searchTranferts() {
        ParametreRequete p;
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numDoc", "%" + numSearch_ + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numDoc)", "numDoc", "%" + numSearch_.trim().toUpperCase() + "%", "LIKE", "AND"));
        } else {
            p = new ParametreRequete("y.numDoc", "numDoc", null);
        }
        paginator.addParam(p);
        loadAllBons(true, true);
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
                        update("data_fournisseur_bla");
                    } else {
                        chooseFsseur(y);
                    }
                    docAchat.getFournisseur().setError(false);
                }
            }
        }
    }

    public void initFsseurs() {
        ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (m != null) {
            m.initFsseurs(docAchat.getFournisseur());
        }
        update("data_fournisseur_bla");
    }

    public void chooseFsseur(Fournisseur d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(docAchat.getFournisseur(), d);
            if (d.getCategorieComptable() != null) {
                cloneObject(docAchat.getCategorieComptable(), d.getCategorieComptable());
                update("txt_cc_livraison_achat");
            }
            if (docAchat.getFournisseur() != null ? docAchat.getFournisseur().getModel() != null : false) {
                docAchat.setModeReglement(docAchat.getFournisseur().getModel());
            }
            update("select_model_livraison_achat");
        }
        update("infos_document_livraison_achat");
    }

    public void chooseArticle(Articles t) {
        if (((docAchat.getFournisseur() != null) ? docAchat.getFournisseur().getId() > 0 : false)) {
            if (!checkOperationArticle(t.getId(), docAchat.getDepotReception().getId(), Constantes.ACHAT)) {
                getWarningMessage("L'article '" + t.getDesignation() + "' ne fait pas d'achat dans le depot '" + docAchat.getDepotReception().getDesignation() + "'");
            }
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(t.getId())});
            t.setConditionnements(unites);
            t.setSelectArt(true);
            selectArt = true;
            cloneObject(contenu.getArticle(), t);
            if (unites != null ? !unites.isEmpty() : false) {
                findInfosArticle(UtilProd.buildBeanConditionnement(unites.get(0)));
            } else {
                findInfosArticle(null);
            }
            String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
            Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(contenu.getArticle().getId(), 1), new Options(docAchat.getDepotReception().getId(), 2)});
            contenu.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
        } else {
            getErrorMessage("Vous devez selectionner le fournisseur");
            resetFicheArticle();
        }
        update("form_contenu_livraison_achat");
    }

    private void chooseConditionnement(Conditionnement c) {
        if (c != null ? c.getId() > 0 : false) {
            chooseArticle(c.getArticle());
            cloneObject(contenu.getConditionnement(), c);
            chooseConditionnement();
        }
    }

    private void findInfosArticle(Conditionnement c) {
        contenu.getArticle().setPua(dao.getPua(contenu.getArticle().getId(), docAchat.getFournisseur().getId(), c.getId()));
        contenu.getArticle().setLastPua(dao.getPua(contenu.getArticle().getId(), docAchat.getFournisseur().getId()));
        contenu.setRemiseRecu(dao.getRemiseAchat(contenu.getArticle().getId(), contenu.getQuantiteRecu(), contenu.getPrixAchat(), docAchat.getFournisseur().getId()));

        if (contenu != null ? contenu.getId() < 1 : false) {
            contenu.setPrixAchat(contenu.getArticle().getPua());
        }
        contenu.setConditionnement(c);
        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
            contenu.getArticle().setStock(dao.stocks(contenu.getArticle().getId(), 0, 0, 0, 0, docAchat.getDateDoc(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
        }
        if (contenu.getQuantiteRecu() > 0) {
            onPrixBlur(true);
        }
    }

    public void onPrixBlur(boolean select) {
        findPrixArticle(contenu, select);
        contenu.setPrixTotalRecu(contenu.getPrixTotalRecu() > 0 ? contenu.getPrixTotalRecu() : 0);
        contenu.setPrixTotalAttendu(contenu.getPrixTotalRecu());
    }

    public void onQuantiteBlur() {
        findPrixArticle(contenu, false);
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
        }
    }

    public void findPrixArticle(ContenuDocAchat c, boolean select) {
        findPrixArticle(c, selectDoc, select);
    }

    public void findPrixArticle(ContenuDocAchat c, YvsComDocAchats doc, boolean select) {
        findPrixArticle(c, selectContenu, doc, select, true);
    }

    public void findPrixArticle(ContenuDocAchat c, YvsComContenuDocAchat y, YvsComDocAchats doc, boolean select, boolean msg) {
        if (select) {
            c.getArticle().setPua(dao.getPua(c.getArticle().getId(), docAchat.getFournisseur().getId(), c.getConditionnement().getId()));
            c.setPrixAchat(c.getArticle().getPua());
        }
        double total = c.getQuantiteRecu() * c.getPrixAchat();
        double _remise = dao.getRemiseAchat(c.getArticle().getId(), c.getQuantiteRecu(), c.getPrixAchat(), docAchat.getFournisseur().getId());
        if (y != null ? y.getId() > 0 : false) {
            if (c.getId() > 0 && (y.getQuantiteRecu() == c.getQuantiteRecu())) {
                _remise = y.getRemise();
            }
        }

        c.setRemiseRecu(_remise);
        c.setPrixTotalRecu(total - c.getRemiseRecu());
        long categorie = returnCategorie(docAchat, doc);
        if (categorie > 0) {
            c.setTaxe(dao.getTaxe(c.getArticle().getId(), categorie, 0, c.getRemiseRecu(), c.getQuantiteRecu(), c.getPrixAchat(), false, docAchat.getFournisseur().getId()));
            c.setPrixTotalRecu(c.getPrixTotalRecu() + (c.getArticle().isPuaTtc() ? 0 : c.getTaxe()));
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

    @Override
    public double setMontantTotalDoc(DocAchat doc) {
        if (doc != null ? doc.getId() > 0 : false) {
            if (doc.getDocumentLie() != null ? doc.getDocumentLie().getId() > 0 : false) {
                DocAchat d = doc.getDocumentLie();
                super.setMontantTotalDoc(d);
                doc.setMontantRemise(d.getMontantRemise());
                doc.setMontantTaxe(d.getMontantTaxe());
                doc.setMontantHT(d.getMontantHT());
                doc.setMontantAvance(d.getMontantAvance());
                doc.setMontantCS(d.getMontantCS());
            }
            update("blog_form_montant_doc_bla");
        }
        return doc.getMontantTotal();
    }

    @Override
    public void cleanAchat() {
        super.cleanAchat();
        loadAllBons(true, true);
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.docAchat.numDoc", "reference", null);
        if (reference != null ? reference.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", reference + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.numDoc)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.nomClient)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.codeFsseur)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.nom)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.enteteDoc.creneau.users.codeUsers)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.enteteDoc.creneau.users.nomUsers)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamStatutContenu() {
        ParametreRequete p = new ParametreRequete("y.docAchat.statut", "statut", null);
        if (statutContenu != null ? statutContenu.trim().length() > 0 : false) {
            p = new ParametreRequete("y.docAchat.statut", "statut", statutContenu, "=", "AND");
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

    public void addParamDepot() {
        ParametreRequete p = new ParametreRequete("y.docAchat.depotReception.code", "depot", null);
        if (depot != null ? depot.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "depot", depot + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.depotReception.code)", "depot", depot.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.depotReception.designation)", "depot", depot.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamClient() {
        ParametreRequete p = new ParametreRequete("y.docAchat.fournisseur", "fournisseur", null);
        if (fournisseurF != null ? fournisseurF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "fournisseur", fournisseurF + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.codeFsseur)", "code", fournisseurF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.nom)", "nom", fournisseurF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.prenom)", "prenom", fournisseurF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docAchat.fournisseur.tiers.codeTiers)", "codeT", fournisseurF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(CONCAT(y.docAchat.fournisseur.nom, ' ', y.docAchat.fournisseur.prenom))", "nom_pre", fournisseurF.trim().toUpperCase() + "%", "LIKE", "OR"));
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
                    p.setObjet(dateDebutContenu);
                    p.setOtherObjet(dateFinContenu);
                }
            }
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
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
                        update("data_articles_livraison_achat");
                    } else {
                        chooseArticle(y);
                    }
                    contenu.getArticle().setError(false);
                } else {
                    Conditionnement c = m.searchArticleActifByCodeBarre(num);
                    if (m.getConditionnements() != null ? !m.getConditionnements().isEmpty() : false) {
                        if (m.getConditionnements().size() > 1) {
                            update("data_conditionnements_livraison_achat");
                        } else {
                            chooseConditionnement(c);
                        }
                        selectArt = true;
                    }
                }
            }
        }
    }

    public void initArticles() {
        ManagedArticles a = (ManagedArticles) giveManagedBean("Marticle");
        selectArt = false;
        if (a != null) {
            a.initArticles("A", contenu.getArticle());
        }
        update("data_articles_livraison_achat");
    }

    public void loadOnViewArticle(SelectEvent ev) {
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

    public void loadOnViewFsseur(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseFournisseur y = (YvsBaseFournisseur) ev.getObject();
            chooseFsseur(UtilCom.buildBeanFournisseur(y));
        }
    }

    public void loadOnViewLivraison(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            livraison = (YvsComDocAchats) ev.getObject();
        }
    }

    public void unLoadOnViewLivraison(SelectEvent ev) {
        livraison = new YvsComDocAchats();
    }
    boolean insertInLivraison = true;

    public void beginLivraison(YvsComContenuDocAchat y, boolean loadDepot) {
        if (y != null ? y.getId() > 0 : false) {
            //controle les quantités déjà livré
            Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findByParentTypeStatutArticleS", new String[]{"parent", "statut", "typeDoc"}, new Object[]{y, Constantes.ETAT_VALIDE, Constantes.TYPE_BLV});
            if (qteLivre != null ? qteLivre < 1 : true) {
                qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocAchat.findByDocLierTypeStatutArticleS", new String[]{"docAchat", "statut", "typeDoc", "article", "unite"}, new Object[]{y.getDocAchat(), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, y.getArticle(), y.getConditionnement()});
            }
            qteLivre = (qteLivre != null) ? qteLivre : 0;
            y.setQteLivree(qteLivre);
            y.setQteRestant(y.getQuantiteRecu() - y.getQteLivree());
            selectContenu = y;
            selectDoc = y.getDocAchat();
        }
    }

    public void loadLivraisonByFournisseur(YvsComContenuDocAchat y, DocAchat bean, Date date) {
        if (y != null ? y.getId() > 0 : false) {
            dateLivraison = date;
            y.setDocAchat(UtilCom.buildDocAchat(bean, currentUser, currentAgence));
            y.getDocAchat().setDateLivraison(date);
            beginLivraison(y, false);
            if (bean.getDepotReception() != null ? bean.getDepotReception().getId() > 0 : false) {
                champ = new String[]{"depot", "fournisseur"};
                val = new Object[]{y.getDocAchat().getDepotReception(), y.getDocAchat().getFournisseur()};
                nameQueri = "YvsComDocAchats.findByFsseurDepotInvalid";
                livraisons = dao.loadNameQueries(nameQueri, champ, val);
                if (livraisons != null ? !livraisons.isEmpty() : false) {
                    insertInLivraison = true;
                    openDialog("dlgOpenLivraison");
                } else {
                    getWarningMessage("Ce client n'a pas de bon de livraison invalide pour le dépôt '" + y.getDocAchat().getDepotReception().getDesignation() + "'");
                    generedLivraison(y, date);
                    update("form_defined_contenu_bla");
                }
            } else {
                getErrorMessage("Il n y'a pas de dépôt de livraison");
            }
        }
    }

    public void loadLivraisonByFournisseur(YvsComContenuDocAchat y, Date date) {
        loadLivraisonByFournisseur(y, UtilCom.buildBeanDocAchat(y.getDocAchat()), date);
    }

    public void generedLivraison(YvsComContenuDocAchat y, DocAchat bean, Date date) {
        if (y != null ? y.getId() > 0 : false) {
            dateLivraison = date;
            y.setDocAchat(UtilCom.buildDocAchat(bean, currentUser, currentAgence));
            y.getDocAchat().setDateLivraison(date);
            beginLivraison(y, true);
            if (bean.getDepotReception() != null ? bean.getDepotReception().getId() > 0 : false) {
                insertInLivraison = false;
                openDialog("dlgDefinedContenu");
            } else {
                getErrorMessage("Il n y'a pas de dépôt de livraison");
            }
        }
    }

    public void generedLivraison(YvsComContenuDocAchat y, Date date) {
        generedLivraison(y, UtilCom.buildBeanDocAchat(y.getDocAchat()), date);
    }

    public void insertInLivraison() {
        if (!autoriser("fa_livrer")) {
//            openNotAcces();
//            return;
        }
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            if (quantite_livree > 0) {
                if (quantite_livree <= selectContenu.getQteRestant()) {
                    if (insertInLivraison) {
                        if (livraison != null ? livraison.getId() > 0 : false) {
                            YvsComContenuDocAchat y = new YvsComContenuDocAchat(null, selectContenu);
                            y.setQuantiteCommande(quantite_livree);
                            y.setDocAchat(livraison);
                            y.setParent(selectContenu);
                            y.setStatut(Constantes.ETAT_EDITABLE);
                            y.setAuthor(currentUser);
                            y.setPrixTotal(prixTotal(y));
                            dao.save(y);
                        }
                    } else {
                        if (selectDoc.getDepotReception() != null ? selectDoc.getDepotReception().getId() < 1 : true) {
                            getErrorMessage("Génération impossible... car dépôt absent");
                            return;
                        }
                        if (!verifyOperation(new Depots(selectDoc.getDepotReception().getId()), Constantes.ENTREE, Constantes.ACHAT, false)) {
                            return;
                        }
                        String num = genererReference(Constantes.TYPE_BLA_NAME, dateLivraison, selectDoc.getDepotReception().getId());
                        if (num != null ? num.trim().length() < 1 : true) {
                            return;
                        }
                        champ = new String[]{"article", "depot"};
                        val = new Object[]{selectContenu.getArticle(), selectDoc.getDepotReception()};
                        YvsBaseArticleDepot a = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                        if (a != null ? a.getId() < 1 : true) {
                            getErrorMessage("Impossible d'effectuer cette action... Car le depot " + selectDoc.getDepotReception().getDesignation() + " ne possède pas l'article " + selectContenu.getArticle().getDesignation());
                            return;
                        }

                        YvsComDocAchats e = new YvsComDocAchats(null, selectDoc);;
                        e.setDateSave(new Date());
                        e.setAuthor(currentUser);
                        e.setTypeDoc(Constantes.TYPE_BLV);
                        e.setNumDoc(num);
                        e.setNumPiece("BL N° " + selectDoc.getNumDoc());
                        e.setDepotReception(selectDoc.getDepotReception());
                        if (selectDoc.getTranche() != null ? selectDoc.getTranche().getId() > 0 : false) {
                            e.setTranche(selectDoc.getTranche());
                        }
                        e.setDateLivraison(dateLivraison);
                        e.setDocumentLie(new YvsComDocAchats(selectDoc.getId()));
                        e.setStatut(Constantes.ETAT_EDITABLE);
                        e.setStatutLivre(Constantes.ETAT_LIVRE);
                        e.setStatutRegle(Constantes.ETAT_ATTENTE);
                        e.setValiderBy(currentUser.getUsers());
                        e.setDateValider(selectDoc.getDateLivraison());
                        e.setDescription("Livraison de la facture N° " + selectDoc.getNumDoc() + " le " + ldf.format(new Date()) + " à " + time.format(new Date()));
                        e.setId(null);
                        e = (YvsComDocAchats) dao.save1(e);

                        YvsComContenuDocAchat y = new YvsComContenuDocAchat(null, selectContenu);
                        y.setQuantiteCommande(quantite_livree);
                        y.setDocAchat(e);
                        y.setParent(selectContenu);
                        y.setStatut(Constantes.ETAT_VALIDE);
                        y.setAuthor(currentUser);
                        y.setPrixTotal(prixTotal(y));
                        dao.save(y);

                        ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                        if (w != null) {
                            int idx = w.getDocAchat().getDocuments().indexOf(e);
                            if (idx < 0) {
                                w.getDocAchat().getDocuments().add(e);
                            }
                            update("tabview_facture_achat");
                        }
                    }
                    if (selectContenu.getQteRestant() <= quantite_livree) {
                        all_contenus.remove(selectContenu);
                    }
                    update("data_contenu_bla");
                    succes();
                } else {
                    getErrorMessage("Vous ne pouvez pas entrer une quantitée supérieure au reste à livrer");
                }
            } else {
                getErrorMessage("Vous devez precisez la quantitée");
            }
        }
    }

    public YvsComDocAchats reBuildNumero(YvsComDocAchats y, boolean save, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            String num = genererReference(Constantes.TYPE_BLA_NAME, y.getDateDoc(), y.getDepotReception().getId());
            if (num != null ? num.trim().length() > 0 : false) {
                y.setNumDoc(num);
            }
            if (save) {
                dao.update(y);
            }
            update("data_livraison_achat");
            if (msg) {
                succes();
            }
        }
        return y;
    }

    public void genereFactureAchat(YvsComDocAchats bean) {
        genereFactureAchat(bean, true);
    }

    public void genereFactureAchat(YvsComDocAchats bean, boolean msg) {
        if (bean != null ? bean.getId() > 0 : false) {
            Long id = bean.getId();
            if (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false) {
                if (msg) {
                    getErrorMessage("Cette commande est déjà liée à une facture");
                }
                return;
            }
            String num = genererReference(Constantes.TYPE_FA_NAME, bean.getDateLivraison());
            if (num != null ? num.trim().length() > 0 : false) {
                List<YvsComContenuDocAchat> list = new ArrayList<>();
                if (bean.getContenus() != null ? !bean.getContenus().isEmpty() : false) {
                    list.addAll(bean.getContenus());

                    YvsComDocAchats y = new YvsComDocAchats(bean);
                    y.setAgence(currentAgence);
                    y.setDateSave(new Date());
                    y.setDateUpdate(new Date());
                    y.setAuthor(currentUser);
                    y.setValiderBy(currentUser.getUsers());
                    y.setTypeDoc(Constantes.TYPE_FA);
                    y.setNumDoc(num);
                    y.setNumPiece("FA N° " + bean.getNumDoc());
                    y.setDateDoc(bean.getDateLivraison());
                    y.setStatut(Constantes.ETAT_EDITABLE);
                    y.setStatutLivre(Constantes.ETAT_ATTENTE);
                    y.setStatutRegle(Constantes.ETAT_ATTENTE);
                    y.setDescription("Facture d'achat N° " + bean.getNumDoc() + " le " + ldf.format(bean.getDateLivraison()));
                    y.getContenus().clear();
                    y.setId(null);
                    y = (YvsComDocAchats) dao.save1(y);
                    YvsComContenuDocAchat c;
                    for (YvsComContenuDocAchat c_ : list) {
                        c = new YvsComContenuDocAchat(null, c_);
                        c.setArticle(c_.getArticle());
                        c.setConditionnement(c_.getConditionnement());
                        c.setExterne(null);
                        c.setDocAchat(y);
                        c.setStatut(Constantes.ETAT_VALIDE);
                        c.setAuthor(currentUser);
                        c.setId(null);
                        c = (YvsComContenuDocAchat) dao.save1(c);
                        y.getContenus().add(c);

                        c_.setParent(new YvsComContenuDocAchat(c.getId()));
                        c_.setDocAchat(new YvsComDocAchats(id));
                        dao.update(c_);
                    }
                    ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (w != null) {
                        List<YvsWorkflowEtapeValidation> etapes = w.saveEtapesValidation();
                        y.setEtapeTotal(etapes != null ? etapes.size() : 0);
                        y.setEtapesValidations(w.saveEtapesValidation(y, etapes));
                    }
                    bean.setDocumentLie(y);
                    dao.update(bean);
                    bean.setContenus(list);
                    if (docAchat.getId() == id) {
                        facture = UtilCom.buildBeanDocAchat(y);
                        docAchat.setDocumentLie(facture);
                        update("zone_facture_bla");
                        if (docAchat.getContenus().isEmpty()) {
                            docAchat.setContenus(list);
                            update("data_contenu_livraison_achat");
                        }
                    }
                    if (documents.contains(bean)) {
                        documents.set(documents.indexOf(bean), bean);
                        update("data_livraison_achat");
                    }
                    if (msg) {
                        succes();
                    }
                    Map<String, String> statuts = dao.getEquilibreAchat(y.getId());
                    if (statuts != null) {
                        y.setStatutLivre(statuts.get("statut_livre"));
                        y.setStatutRegle(statuts.get("statut_regle"));
                    }
                }
            }
        }
    }

    public void findContenusByArticle() {
        docAchat.getContenus().clear();
        if (Util.asString(articleContenu)) {
            for (YvsComContenuDocAchat c : docAchat.getContenusSave()) {
                if (c.getArticle().getRefArt().toUpperCase().contains(articleContenu.toUpperCase()) || c.getArticle().getDesignation().toUpperCase().contains(articleContenu.toUpperCase())) {
                    docAchat.getContenus().add(c);
                }
            }
        } else {
            docAchat.getContenus().addAll(docAchat.getContenusSave());
        }
    }

    public void buildTransfertArticle(YvsComContenuDocAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            docStock = new DocStock();
            docStock.setTypeDoc(Constantes.TYPE_FT);
            docStock.setNature(Constantes.TRANSFERT);
            docStock.setDateDoc(docAchat.getDateDoc());
            docStock.setSource(docAchat.getDepotReception());
            docStock.getSource().setTranches(loadCreneaux(new YvsBaseDepots(docStock.getSource().getId()), docStock.getDateDoc()));
            destinations = loadDestination(new YvsBaseDepots(docStock.getSource().getId()));

            transfert = new ContenuDocStock();
            transfert.setArticle(UtilProd.buildSimpleBeanArticles(y.getArticle()));
            transfert.setConditionnement(UtilProd.buildBeanConditionnement(y.getConditionnement()));
            cloneObject(transfert.getUniteDestination(), transfert.getConditionnement());
            transfert.setLotSortie(UtilCom.buildBeanLotReception(y.getLot()));
            cloneObject(transfert.getLotEntree(), transfert.getLotSortie());
            transfert.setQualite(UtilCom.buildBeanQualite(y.getQualite()));
            cloneObject(transfert.getQualiteEntree(), transfert.getQualite());
            transfert.setQuantite(y.getQuantiteRecu());
            transfert.setPrix(y.getPrixAchat());
            transfert.setPrixEntree(y.getPrixAchat());
            listenChangeQuantite();
            update("blog_send_article_bla");
        }
    }

    public void changeCalculPr(YvsComContenuDocAchat y) {
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
            getException("ManagedLivraisonAchat (changeCalculPr)", ex);
        }
    }

    public void chooseDestination() {
        if (docStock.getDestination() != null ? docStock.getDestination().getId() > 0 : false) {
            int idx = destinations.indexOf(new YvsBaseDepots(docStock.getDestination().getId()));
            if (idx > -1) {
                YvsBaseDepots d = destinations.get(idx);
                docStock.setDestination(UtilCom.buildSimpleBeanDepot(d));
                docStock.getDestination().setTranches(loadCreneaux(d, docStock.getDateDoc()));
            }
        }
    }

    public void saveSendArticle() {
        ManagedTransfertStock w = (ManagedTransfertStock) giveManagedBean(ManagedTransfertStock.class);
        if (w != null) {
            YvsComDocStocks y = w._saveNew(docStock, true, true);
            if (y != null ? y.getId() > 0 : false) {
                if (w.chechAutorisationAction(y, 1, false)) {
                    y.setStatut(Constantes.ETAT_SOUMIS);
                    dao.update(y);
                }
                docStock.setId(y.getId());
                transfert.setDocStock(docStock);
                transfert.setPrixTotal(transfert.getQuantite() * transfert.getPrix());
                if (w.controleFicheContenu(transfert, true)) {
                    YvsComContenuDocStock en = w.buildContenuDocStock(transfert);
                    en.setDocStock(y);
                    if (transfert.getId() < 1) {
                        en.setId(null);
                        dao.save(en);
                    } else {
                        dao.update(en);
                    }
                    succes();
                    w.transmis();
                    closeDialog("dlgSendArticle");
                }
            }
        }
    }

    public void listenChangeQuantite() {
        if (transfert.getConditionnement().getId() > 0 && transfert.getUniteDestination().getId() > 0 && transfert.getQuantite() > 0) {
            if (!transfert.getConditionnement().getUnite().equals(transfert.getUniteDestination().getUnite())) {
                transfert.setResultante(convertirUnite(transfert.getConditionnement().getUnite(), transfert.getUniteDestination().getUnite(), transfert.getQuantite()));
            } else {
                transfert.setResultante(transfert.getQuantite());
            }
        }
    }

    public void equilibre() {
        equilibre(selectDoc);
    }

    public void equilibre(YvsComDocAchats selectDoc) {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getId() > 0 : false) {
                Map<String, String> statuts = dao.getEquilibreAchat(selectDoc.getDocumentLie().getId());
                if (statuts != null) {
                    selectDoc.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                    selectDoc.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                }
            }
        }
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllBons(true, true);
    }

    public void findLotReceptionByEntity(YvsComContenuDocAchat c) {
        try {
            if (c != null ? c.getLot() != null : false) {
                if (c.getLot().getNumero() != null ? c.getLot().getNumero().trim().length() > 0 : false) {
                    YvsComLotReception l = (YvsComLotReception) dao.loadOneByNameQueries("YvsComLotReception.findByNumeroArticle", new String[]{"article", "numero"}, new Object[]{new YvsBaseArticles(c.getArticle().getId()), c.getLot().getNumero()});
                    if (l != null ? l.getId() < 1 : true) {
                        String query = "SELECT y.id FROM yvs_com_lot_reception y INNER JOIN yvs_base_mouvement_stock_lot l ON l.lot = y.id INNER JOIN yvs_base_mouvement_stock m ON l.mouvement = m.id "
                                + " WHERE m.article = ? AND y.numero = ? ORDER BY y.id DESC LIMIT 1";
                        Long lot = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(c.getLot().getNumero(), 2)});
                        l = new YvsComLotReception(lot, c.getLot().getNumero());
                    }
                    if (l != null ? l.getId() > 0 : false) {
                        c.setLot(l);
                        update("data-contenu_bla_require_lot");
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void findLotReception(ContenuDocAchat c) {
        try {
            if (c != null ? c.getLot() != null : false) {
                if (c.getLot().getNumero() != null ? c.getLot().getNumero().trim().length() > 0 : false) {
                    YvsComLotReception l = (YvsComLotReception) dao.loadOneByNameQueries("YvsComLotReception.findByNumeroArticle", new String[]{"article", "numero"}, new Object[]{new YvsBaseArticles(c.getArticle().getId()), c.getLot().getNumero()});
                    if (l != null ? l.getId() < 1 : true) {
                        String query = "SELECT y.id FROM yvs_com_lot_reception y INNER JOIN yvs_base_mouvement_stock_lot l ON l.lot = y.id INNER JOIN yvs_base_mouvement_stock m ON l.mouvement = m.id "
                                + " WHERE m.article = ? AND y.numero = ? ORDER BY y.id DESC LIMIT 1";
                        Long lot = (Long) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(c.getLot().getNumero(), 2)});
                        l = new YvsComLotReception(lot, c.getLot().getNumero());
                    }
                    if (l != null ? l.getId() > 0 : false) {
                        c.setLot(UtilCom.buildBeanLotReception(l));
                    } else {
                        c.getLot().setId(0);
                        ManagedLotReception m = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                        if (m != null) {
                            m.resetFiche();
                            m.getLotReception().setNumero(c.getLot().getNumero());
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
                    contenu.setLot(UtilCom.buildBeanLotReception(y));
                    update("select_lot_livraison_achat");
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayContent(YvsComDocAchats y) {
        y.setContenus(dao.loadNameQueries("YvsComContenuDocAchat.findByDocAchat", new String[]{"docAchat"}, new Object[]{y}));
        update("dt_row_ex_" + y.getId());
    }

}
