/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

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
import yvs.commercial.depot.ArticleDepot;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.production.UtilProd;
import yvs.users.Users;
import yvs.commercial.UtilCom;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.users.YvsUsers;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Util;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.Qualite;
import yvs.commercial.achat.LotReception;
import yvs.commercial.achat.ManagedLotReception;
import yvs.commercial.depot.ManagedDepot;
import yvs.commercial.stock.CoutSupDoc;
import yvs.commercial.stock.ManagedOtherTransfert;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBasePointVenteDepot;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.users.ManagedUser;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Utilitaire;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedLivraisonVente extends ManagedCommercial<DocVente, YvsComDocVentes> implements Serializable {

    private DocVente docVente = new DocVente();
    private DocVente docSelect = new DocVente();
    private DocVente facture = new DocVente();
    private List<YvsComDocVentes> documents, livraisons;
    private YvsComDocVentes selectDoc;
    private YvsComDocVentes livraison = new YvsComDocVentes();

    private List<YvsComContenuDocVente> contenus_fv, all_contenus, contenusRequireLot, selectContenus;
    private YvsComContenuDocVente selectContenu;
    private ContenuDocVente contenu = new ContenuDocVente();
    public PaginatorResult<YvsComContenuDocVente> p_contenu = new PaginatorResult<>();
    private int page = 50;

    private List<YvsBasePointVenteDepot> depots_livraison;
    private List<YvsGrhTrancheHoraire> tranches, tranches_livraison;
    private List<YvsBasePointVente> points;

    private String tabIds, tabIds_article;
    private Date dateLivraison = new Date();
    private double quantite_livree;
    private Depots depotContenu = new Depots();
    private Depots depotChange = new Depots();
    private long agenceChange = -1;
    private boolean existFacture, update, selectArt, memoriserDeleteContenu = false;

    //Parametre recherche
    private boolean date = false;
    private Date dateDebut = new Date(), dateFin = new Date();
    private String statut = null, numFacture, egaliteStatut = "!=";
    private long livreur_, trancheSearch, depotSearch;
    private List<YvsBaseDepots> depotsLivraison;

    //Parametre recherche contenu
    private boolean dateContenu = false;
    private long agenceContenu = -1;
    private Date dateDebutContenu = new Date(), dateFinContenu = new Date();
    private String statutContenu, reference, article, pointvente, clientF, statutReg;
    private String opeStatut = "=", opStatutLiv = "!=", opStatutReg = "=";
    private Character statutLiv = Constantes.STATUT_DOC_LIVRER;

    public ManagedLivraisonVente() {
        documents = new ArrayList<>();
        all_contenus = new ArrayList<>();
        livraisons = new ArrayList<>();
        documents = new ArrayList<>();
        depotsLivraison = new ArrayList<>();
        depots_livraison = new ArrayList<>();
        tranches = new ArrayList<>();
        tranches_livraison = new ArrayList<>();
        points = new ArrayList<>();
        contenus_fv = new ArrayList<>();
        contenusRequireLot = new ArrayList<>();
        selectContenus = new ArrayList<>();
    }

    public boolean isMemoriserDeleteContenu() {
        return memoriserDeleteContenu;
    }

    public void setMemoriserDeleteContenu(boolean memoriserDeleteContenu) {
        this.memoriserDeleteContenu = memoriserDeleteContenu;
    }

    public List<YvsComContenuDocVente> getSelectContenus() {
        return selectContenus;
    }

    public void setSelectContenus(List<YvsComContenuDocVente> selectContenus) {
        this.selectContenus = selectContenus;
    }

    public long getAgenceChange() {
        return agenceChange;
    }

    public void setAgenceChange(long agenceChange) {
        this.agenceChange = agenceChange;
    }

    public long getAgenceContenu() {
        return agenceContenu;
    }

    public void setAgenceContenu(long agenceContenu) {
        this.agenceContenu = agenceContenu;
    }

    public List<YvsComContenuDocVente> getContenusRequireLot() {
        return contenusRequireLot;
    }

    public void setContenusRequireLot(List<YvsComContenuDocVente> contenusRequireLot) {
        this.contenusRequireLot = contenusRequireLot;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public String getClientF() {
        return clientF;
    }

    public void setClientF(String clientF) {
        this.clientF = clientF;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
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

    public String getPointvente() {
        return pointvente;
    }

    public void setPointvente(String pointvente) {
        this.pointvente = pointvente;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public PaginatorResult<YvsComContenuDocVente> getP_contenu() {
        return p_contenu;
    }

    public void setP_contenu(PaginatorResult<YvsComContenuDocVente> p_contenu) {
        this.p_contenu = p_contenu;
    }

    public List<YvsComContenuDocVente> getAll_contenus() {
        return all_contenus;
    }

    public void setAll_contenus(List<YvsComContenuDocVente> all_contenus) {
        this.all_contenus = all_contenus;
    }

    public YvsComDocVentes getLivraison() {
        return livraison;
    }

    public void setLivraison(YvsComDocVentes livraison) {
        this.livraison = livraison;
    }

    public List<YvsBaseDepots> getDepotsLivraison() {
        return depotsLivraison;
    }

    public void setDepotsLivraison(List<YvsBaseDepots> depotsLivraison) {
        this.depotsLivraison = depotsLivraison;
    }

    public List<YvsComDocVentes> getLivraisons() {
        return livraisons;
    }

    public void setLivraisons(List<YvsComDocVentes> livraisons) {
        this.livraisons = livraisons;
    }

    public double getQuantite_livree() {
        return quantite_livree;
    }

    public void setQuantite_livree(double quantite_livree) {
        this.quantite_livree = quantite_livree;
    }

    public Depots getDepotChange() {
        return depotChange;
    }

    public void setDepotChange(Depots depotChange) {
        this.depotChange = depotChange;
    }

    public Depots getDepotContenu() {
        return depotContenu;
    }

    public void setDepotContenu(Depots depotContenu) {
        this.depotContenu = depotContenu;
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

    public long getDepotSearch() {
        return depotSearch;
    }

    public void setDepotSearch(long depotSearch) {
        this.depotSearch = depotSearch;
    }

    public long getTrancheSearch() {
        return trancheSearch;
    }

    public void setTrancheSearch(long trancheSearch) {
        this.trancheSearch = trancheSearch;
    }

    public List<YvsBasePointVenteDepot> getDepots_livraison() {
        return depots_livraison;
    }

    public void setDepots_livraison(List<YvsBasePointVenteDepot> depots_livraison) {
        this.depots_livraison = depots_livraison;
    }

    public List<YvsGrhTrancheHoraire> getTranches_livraison() {
        return tranches_livraison;
    }

    public void setTranches_livraison(List<YvsGrhTrancheHoraire> tranches_livraison) {
        this.tranches_livraison = tranches_livraison;
    }

    public List<YvsGrhTrancheHoraire> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsGrhTrancheHoraire> tranches) {
        this.tranches = tranches;
    }

    public List<YvsBasePointVente> getPoints() {
        return points;
    }

    public void setPoints(List<YvsBasePointVente> points) {
        this.points = points;
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

    public YvsComDocVentes getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocVentes selectDoc) {
        this.selectDoc = selectDoc;
    }

    public YvsComContenuDocVente getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComContenuDocVente selectContenu) {
        this.selectContenu = selectContenu;
    }

    public List<YvsComContenuDocVente> getContenus_fv() {
        return contenus_fv;
    }

    public void setContenus_fv(List<YvsComContenuDocVente> contenus_fv) {
        this.contenus_fv = contenus_fv;
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

    public DocVente getFacture() {
        return facture;
    }

    public void setFacture(DocVente facture) {
        this.facture = facture;
    }

    public DocVente getDocSelect() {
        return docSelect;
    }

    public void setDocSelect(DocVente docSelect) {
        this.docSelect = docSelect;
    }

    public ContenuDocVente getContenu() {
        return contenu;
    }

    public void setContenu(ContenuDocVente contenu) {
        this.contenu = contenu;
    }

    public String getTabIds_article() {
        return tabIds_article;
    }

    public void setTabIds_article(String tabIds_article) {
        this.tabIds_article = tabIds_article;
    }

    public DocVente getDocVente() {
        return docVente;
    }

    public void setDocVente(DocVente docVente) {
        this.docVente = docVente;
    }

    public List<YvsComDocVentes> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocVentes> documents) {
        this.documents = documents;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getStatutReg() {
        return statutReg;
    }

    public void setStatutReg(String statutReg) {
        this.statutReg = statutReg;
    }

    public String getOpeStatut() {
        return opeStatut;
    }

    public void setOpeStatut(String opeStatut) {
        this.opeStatut = opeStatut;
    }

    public Character getStatutLiv() {
        return statutLiv;
    }

    public void setStatutLiv(Character statutLiv) {
        this.statutLiv = statutLiv;
    }

    public String getOpStatutReg() {
        return opStatutReg;
    }

    public void setOpStatutReg(String opStatutReg) {
        this.opStatutReg = opStatutReg;
    }

    public String getOpStatutLiv() {
        return opStatutLiv;
    }

    public void setOpStatutLiv(String opStatutLiv) {
        this.opStatutLiv = opStatutLiv;
    }

    @Override
    public void loadAll() {
        _load();
        initView();
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
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadAllFacture(true, true);
    }

    public void initView() {
        loadInfosWarning(false);
        if (facture != null ? facture.getId() > 0 : false) {
//            loadContenuFacture(new YvsComDocVentes(facture.getId()), false);
        }
        if (((docVente != null) ? docVente.getClient().getId() < 1 : true)) {
            docVente = new DocVente();
            docVente.setTypeDoc(Constantes.TYPE_BLV);
            if (docVente.getDocumentLie() == null) {
                docVente.setDocumentLie(new DocVente());
            }
            numSearch_ = "";
            docVente.setEnteteDoc(new EnteteDocVente());
        }

        if (docVente.getLivreur() != null ? (docVente.getLivreur().getId() < 1) : true) {
            if (currentUser.getUsers() != null ? currentUser.getUsers().getId() > 0 : false) {
                Users u = UtilUsers.buildBeanUsers(currentUser.getUsers());
                cloneObject(docVente.getLivreur(), u);
            } else {
                getWarningMessage("Vous n'etes pas associé à un livreur... Vous ne pourez pas livrer ce bon de livraison");
            }
        }
        if (agenceContenu < 0) {
            agenceContenu = currentAgence.getId();
        }
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        update("txt_indice_num_search");
    }

    public void loadContenus(boolean avance, boolean init) {
        controlListAgence();
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_FV)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                p_contenu.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.agence", "agences", listIdAgences, "IN", "AND");
                p_contenu.addParam(p);
                break;
            case 3: { //charge tous les document des points de vente où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"}, new Object[]{currentUser.getUsers(), (Utilitaire.getIniTializeDate(new Date()).getTime()), Constantes.getPreviewDate(new Date())});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                p_contenu.addParam(p);
                break;
            }
            case 4: {//charge tous les document des points de vente où l'utilisateurs est responsable
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                if (!ids.isEmpty()) {
                    ids = dao.loadNameQueries("YvsBasePointVenteDepot.findIdPointByDepot", new String[]{"ids"}, new Object[]{ids});
                    if (ids.isEmpty()) {
                        ids.add(-1L);
                    }
                } else {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                p_contenu.addParam(p);
                break;
            }
            default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                p = new ParametreRequete("y.docVente.enteteDoc.creneau.users ", "users", currentUser.getUsers(), "=", "AND");
                p_contenu.addParam(p);
                break;

        }

        p_contenu.addParam(new ParametreRequete("y.docVente.typeDoc", "typeDoc", Constantes.TYPE_FV, "=", "AND"));
        String orderBy = "y.docVente.dateLivraisonPrevu DESC, y.docVente.numDoc, y.docVente.enteteDoc.dateEntete";
        all_contenus = p_contenu.executeDynamicQuery("YvsComContenuDocVente", orderBy, avance, init, page, dao);
        update("data_contenu_blv");
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        loadAllFacture(true, true);
    }

    public void gotoPagePaginatorContenu() {
        p_contenu.gotoPage(page);
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
            page = (int) v;
            loadContenus(true, true);
        }
    }

    public void loadAllFacture(boolean avance, boolean init) {
        controlListAgence();
        //choisir les documents à charger
        List<Long> idres;
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_BLV)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.depotLivrer.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.depotLivrer.agence", "agences", listIdAgences, "IN", "AND");
                paginator.addParam(p);
                break;
            case 3: //charge tous les document des points de vente où l'utilisateurs est responsable
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.depotLivrer.id", "depots", ids, " IN ", "AND");
                paginator.addParam(p);
                break;
            default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                p = new ParametreRequete("y.author.users ", "users", currentUser.getUsers(), "=", "AND");
                paginator.addParam(p);
                break;

        }
        p = new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_BLV, "=", "AND");
        paginator.addParam(p);
        documents = paginator.executeDynamicQuery("YvsComDocVentes", "y.enteteDoc.dateEntete DESC, y.numDoc DESC", avance, init, (int) imax, dao);
        update("data_livraison_vente");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComDocVentes> re = paginator.parcoursDynamicData("YvsComDocVentes", "y", "y.enteteDoc.dateEntete DESC, y.numDoc DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadAllTranche(YvsBaseDepots depot, Date date) {
        tranches = loadTranche(depot, date);
        update("select_tranche_blv");
    }

    public boolean loadAllDepotLivraison(YvsBasePointVente y) {
        if (!autoriser("fv_livrer_in_all_depot")) {
            champ = new String[]{"pointVente"};
            val = new Object[]{y};
            nameQueri = "YvsBasePointVenteDepot.findActifByPointVente";
        } else {
            champ = new String[]{"agence"};
            val = new Object[]{currentAgence};
            nameQueri = "YvsBasePointVenteDepot.findActifByAgence";
        }
        depots_livraison = dao.loadNameQueries(nameQueri, champ, val);
        List<YvsBaseDepots> lp = dao.loadNameQueries("YvsBasePointVenteDepot.findDepotByPointPrincipal", new String[]{"point"}, new Object[]{y}, 0, 1);
        if (lp != null ? !lp.isEmpty() : false) {
            docVente.setDepot(new Depots(lp.get(0).getId()));
            chooseDepotLivraison();
            return false;
        }
        update("select_depot_livraison_vente");
        return true;
    }

    public void loadDepotByPoint(YvsBasePointVente y, YvsAgences a) {
        if (!autoriser("fv_livrer_in_all_depot")) {
            champ = new String[]{"pointVente"};
            val = new Object[]{y};
            nameQueri = "YvsBasePointVenteDepot.findDepotByPoint";
        } else {
            champ = new String[]{"agence"};
            val = new Object[]{a};
            nameQueri = "YvsBasePointVenteDepot.findDepotByAgence";
        }
        depotsLivraison = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAllTrancheLivraison(YvsBaseDepots depot, Date date) {
        tranches_livraison = loadTranche(depot, date);
        update("select_tranche_livraison_vente");
    }

    public void loadContenuFacture(YvsComDocVentes y, boolean facture) {
        if (y != null ? y.getId() > 0 : false) {
            long id = y.getId();
            String state_livre = y.getStatutLivre();
            if (facture) {
                String num = y.getNumDoc();
                docVente = UtilCom.buildBeanDocVente(y);
                docVente.setHeureDoc(new Date());
                docVente.setDateLivraison(new Date());
                docVente.setDocumentLie(new DocVente(docVente.getId(), docVente.getNumDoc(), docVente.getStatut()));
                docVente.getDocumentLie().setStatutLivre(state_livre);
                docVente.setTypeDoc(Constantes.TYPE_BLV);
                docVente.setNumDoc(null);
                docVente.setNew_(true);
                docVente.setUpdate(false);
                docVente.setStatut(Constantes.ETAT_EDITABLE);
                docVente.setDateSave(new Date());
                docVente.setNumPiece("BL N° " + num);
                docVente.setDescription("Livraison de la facture N° " + num + " le " + ldf.format(new Date()) + " à " + time.format(new Date()));
                docVente.setId((long) -1);
                docVente.getContenus().clear();
                docVente.setUpdate(false);
                setMontantTotalDoc(docVente);
                update("infos_document_livraison_vente");
            }
            contenus_fv.clear();
            List<YvsComContenuDocVente> lc = _loadContenus(new YvsComDocVentes(id));
            for (YvsComContenuDocVente c : lc) {
                boolean deja = false;
                for (YvsComContenuDocVente n : contenus_fv) {
                    if ((n.getArticle().equals(c.getArticle()) && n.getConditionnement().equals(c.getConditionnement())) ? !c.isBonus() : false) {
                        deja = true;
                        break;
                    }
                }
                if (!deja) {
                    champ = new String[]{"article", "unite", "docVente"};
                    val = new Object[]{c.getArticle(), c.getConditionnement(), new YvsComDocVentes(y.getId())};
                    Double bonus = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteBonusByArticle", champ, val);
                    bonus = bonus != null ? bonus : 0;
                    Double qte = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", champ, val);
                    qte = qte != null ? qte : 0;
                    if (c.isBonus()) {
                        qte += bonus;
                    }
                    if (qte > 0) {
                        Double rem = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findRemByArticle", champ, val);
                        rem = rem != null ? rem : 0;
                        Double tax = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findTaxeByArticle", champ, val);
                        tax = tax != null ? tax : 0;
                        Double pt = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findPTByArticle", champ, val);
                        pt = pt != null ? pt : 0;

                        double prix = (pt - tax + rem) / qte;
                        String[] ch = new String[]{"docVente", "typeDoc", "statut", "article", "unite"};
                        Object[] v = new Object[]{new YvsComDocVentes(id), Constantes.TYPE_BLV, Constantes.ETAT_VALIDE, c.getArticle(), c.getConditionnement()};
                        Double liv = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", ch, v);
                        liv = liv != null ? liv : 0;
                        if (qte > liv) {
                            c.setQuantite_(c.isBonus() ? bonus : qte);
                            double reste = qte - liv;
                            c.setQuantite(!c.isBonus() ? reste : (reste > bonus ? bonus : reste));
                            c.setTaxe(tax);
                            c.setRemise(rem);
                            c.setPrix(prix);
                            c.setParent(new YvsComContenuDocVente(c.getId()));
                            c.setPrixTotal(pt);
                            contenus_fv.add(c);
                        }
                    }

                }
            }
            if ((lc != null ? !lc.isEmpty() : false) && (contenus_fv != null ? contenus_fv.isEmpty() : true)) {
                getInfoMessage("Cette facture est déja livrée");
                if (!state_livre.equals(Constantes.ETAT_LIVRE)) {
                    String rq = "UPDATE yvs_com_doc_ventes SET statut_livre = 'L' WHERE id=?";
                    Options[] param = new Options[]{new Options(id, 1)};
                    dao.requeteLibre(rq, param);
                    int idx = documents.indexOf(new YvsComDocVentes(id));
                    if (idx > -1) {
                        documents.get(idx).setStatutLivre(Constantes.ETAT_LIVRE);
                        update("data_livraison_vente");
                    }
                }
            }
            update("data_contenu_fv_blv");
        }
    }

    private List<YvsComContenuDocVente> _loadContenus(YvsComDocVentes y) {
        nameQueri = "YvsComContenuDocVente.findByDocVente";
        champ = new String[]{"docVente"};
        val = new Object[]{y};
        List<YvsComContenuDocVente> list = dao.loadNameQueries(nameQueri, champ, val);
        List<YvsComContenuDocVente> temps = new ArrayList<>();
        for (YvsComContenuDocVente c : list) {
            c.setSave(c.getId());
            if (c.getQuantiteBonus() > 0) {
                YvsComContenuDocVente bean = new YvsComContenuDocVente((long) -(temps.size() + 1), c);
                bean.setArticle(c.getArticleBonus());
                bean.setConditionnement(c.getConditionnementBonus());
                bean.setQuantite(c.getQuantiteBonus());
                bean.setDocVente(c.getDocVente());
                bean.setBonus(true);

                bean.setQuantiteBonus(0.0);
                bean.setPrix(0.0);
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

    public void init(boolean next) {
        loadAllFacture(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllFacture(true, true);
    }

    public YvsComDocVentes buildDocVente(DocVente y) {
        YvsComDocVentes d = new YvsComDocVentes();
        if (y != null) {
            d = UtilCom.buildDocVente(y, currentUser);
            if ((d.getLivreur() != null) ? d.getLivreur().getId() > 0 : false) {
                if (users.contains(new YvsUsers(y.getLivreur().getId()))) {
                    d.setLivreur(users.get(users.indexOf(new YvsUsers(y.getLivreur().getId()))));
                }
            }
        }
        return d;
    }

    @Override
    public DocVente recopieView() {
        docVente.setTypeDoc(Constantes.TYPE_BLV);
        return docVente;
    }

    @Override
    public boolean controleFiche(DocVente bean) {
        if (!_controleFiche_(bean)) {
            return false;
        }
        if (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() < 1 : true) {
            getErrorMessage("Cette livraison n'est pas rattachée à une facture");
            return false;
        }
        if (!bean.getDocumentLie().getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (bean.getDocumentLie().getEnteteDoc() != null ? (bean.getDocumentLie().getEnteteDoc().getPoint() != null) : false) {
                switch (bean.getDocumentLie().getEnteteDoc().getPoint().getLivraisonOn()) {
                    case 'R': {
                        if (!bean.getDocumentLie().getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                            getErrorMessage("Cette facture doit etre reglée avant de pouvoir générer une livraison");
                            return false;
                        }
                    }
                    case 'V': {
                        if (!bean.getDocumentLie().getStatut().equals(Constantes.ETAT_VALIDE)) {
                            getErrorMessage("Cette facture doit etre validée avant de pouvoir générer une livraison");
                            return false;
                        }
                    }
                }
            }
            getWarningMessage("La facture n'est pas validé");
        }
        if ((bean.getEnteteDoc() != null) ? bean.getEnteteDoc().getId() < 1 : true) {
            getErrorMessage("Vous ne disposé pas d'une entête");
            return false;
        }
        if ((bean.getLivreur() != null) ? bean.getLivreur().getId() < 1 : true) {
            getWarningMessage("Vous devez specifier le livreur!");
        }
        if ((bean.getDepot() != null) ? bean.getDepot().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le dépôt de livraison!");
            return false;
        }
        if ((bean.getTranche() != null) ? bean.getTranche().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier la tranche de livraison!");
            return false;
        }
        if ((selectDoc != null ? (selectDoc.getId() > 0 ? !bean.getDateLivraison().equals(selectDoc.getDateLivraison()) : false) : false)
                || (bean.getNumDoc() == null || bean.getNumDoc().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_BLV_NAME, bean.getDateLivraison(), docVente.getDepot().getId(), Constantes.DEPOT);
            bean.setNumDoc(ref);
            if (ref == null || ref.trim().equals("")) {
                return false;
            }
        }
        //contrôle la cohérence avec les inventaires
        boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
        boolean exist_inventaire = !verifyInventaire(bean.getDepot(), bean.getTranche(), bean.getDateLivraison(), !gescom_update_stock_after_valide);
        if (exist_inventaire && !gescom_update_stock_after_valide) {
            return false;
        }
        return verifyTranche(bean.getTranche(), bean.getDepot(), bean.getDateLivraison());
    }

    private boolean _controleFiche_(DocVente bean) {
        if (bean == null) {
            getErrorMessage("Le devez selectionner un document");
            return false;
        }
        if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return false;
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document a été verouillé");
            return false;
        }
//        return writeInExercice(bean.getDateDoc());
        return true;
    }

    private boolean _controleFiche_(YvsComDocVentes bean) {
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

    public boolean controleFicheContenu(ContenuDocVente bean) {
        return controleFicheContenu(bean, true);
    }

    public boolean controleFicheContenu(ContenuDocVente bean, boolean principal) {
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if (bean.getQuantite() <= 0) {
            getErrorMessage("Vous devez indiquer la quantitée livrée");
            return false;
        }
        if (bean.getQuantite() > bean.getQuantite_()) {
            getErrorMessage("Vous ne pouvez pas livrer cette quantitée");
            return false;
        }
        champ = new String[]{"article", "depot"};
        val = new Object[]{new YvsBaseArticles(bean.getArticle().getId()), new YvsBaseDepots(docVente.getDepot().getId())};
        YvsBaseArticleDepot a = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
        if (a != null ? a.getId() < 1 : true) {
            getErrorMessage("Impossible d'enregistre car le dépôt ne possede pas cet article");
            return false;
        }
        if (a.getRequiereLot()) {
            if (principal) {
                if ((bean.getLot() != null ? bean.getLot().getId() < 1 : true) && (bean.getLots() != null ? bean.getLots().size() > 1 ? (bean.getLots().size() == 1 ? bean.getLots().get(0).getId() < 1 : false) : true : true)) {
                    getErrorMessage("Un numéro de lot est requis pour cet article dans le dépôt");
                    return false;
                }
            } else {
                if (bean.getLot() != null ? bean.getLot().getId() < 1 : true) {
                    getErrorMessage("Un numéro de lot est requis pour cet article dans le dépôt");
                    return false;
                }
            }
        }
        if (bean.getDocVente() != null ? !bean.getDocVente().isUpdate() : true) {
            if (!_saveNew()) {
                return false;
            }
            bean.setDocVente(docVente);
        }
        return _controleFiche_(bean.getDocVente());
    }

    public boolean controleFicheCout(CoutSupDoc bean) {
        if (bean.getDoc() < 1) {
            if (!_saveNew()) {
                return false;
            }
            bean.setDoc(docVente.getId());
        }
        if ((bean.getType() != null) ? bean.getType().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le type de coût");
            return false;
        }
        if (bean.getMontant() < 1) {
            getErrorMessage("Vous devez entrer un montant");
            return false;
        }
        return _controleFiche_(docVente);
    }

    @Override
    public void populateView(DocVente bean) {
        cloneObject(docVente, bean);
        docVente.setDateLivraison((docVente.getDateLivraison() == null) ? new Date() : docVente.getDateLivraison());
        setMontantTotalDoc(docVente);
    }

    public void populateViewArticle(ContenuDocVente bean) {
        if (docVente.getDepot().getId() > 0) {
            bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docVente.getDepot().getId(), 0, 0, docVente.getDateLivraison(), bean.getConditionnement().getId(), bean.getLot().getId()));
            bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
            selectArt = true;
            cloneObject(contenu, bean);
            //controle les quantités déjà livré
            Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{new YvsComDocVentes(facture.getId()), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, selectContenu.getArticle(), selectContenu.getConditionnement()});
            qteLivre = (qteLivre != null) ? qteLivre : 0;
            contenu.setSaveLivre(qteLivre);
            //triuve la quantité d'article facturé 
            Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{new YvsComDocVentes(facture.getId()), selectContenu.getArticle(), selectContenu.getConditionnement()});
            qteFacture = (qteFacture != null) ? qteFacture : 0;
            contenu.setSaveFacture(qteFacture);

            double qteEncour = (qteFacture - qteLivre);
            contenu.setSaveEncours(qteEncour);
            contenu.setQuantite_(qteEncour < 0 ? 0 : qteEncour);

            String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
            Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(bean.getArticle().getId(), 1), new Options(docVente.getDepot().getId(), 2)});
            contenu.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
            if (contenu.getArticle().isRequiereLot() && (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false)) {
                ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                if (w != null) {
                    contenu.setLots(w.loadList(docVente.getDepot().getId(), contenu.getConditionnement().getId(), docVente.getDateLivraison(), contenu.getQuantite(), bean.getArticle().getStock()));
                    update("data-contenu_blv_require_lot");
                }
            }
        } else {
            getErrorMessage("Aucun dépôt n'a été selectionné !");
        }
    }

    @Override
    public void resetFiche() {
        docVente = new DocVente();
        docVente.setTranche(new TrancheHoraire());
        docVente.setDepot(new Depots());
        docVente.setStatut(Constantes.ETAT_EDITABLE);

        facture = new DocVente();
        tabIds = "";
        update = false;
        docVente.getContenus().clear();
        contenus_fv.clear();
        depots_livraison.clear();
        tranches_livraison.clear();
        selectDoc = new YvsComDocVentes();
        selectDoc = new YvsComDocVentes();

        if (currentUser.getUsers() != null ? currentUser.getUsers().getId() > 0 : false) {
            Users u = UtilUsers.buildBeanUsers(currentUser.getUsers());
            cloneObject(docVente.getLivreur(), u);
        } else {
            getWarningMessage("Vous n'etes pas associé à un livreur... Vous ne pourez pas livrer ce bon de livraison");
        }
        resetFicheArticle();
        update("blog_form_livraison_vente");
    }

    public void resetFicheArticle() {
        resetFiche(contenu);
        contenu.setArticle(new Articles());
        contenu.setDocVente(new DocVente());
        contenu.setQualite(new Qualite());
        contenu.setConditionnement(new Conditionnement());
        contenu.setLot(new LotReception());
        contenu.getLots().clear();
        selectArt = false;
        tabIds_article = "";
        selectContenu = null;
    }

    public void resetFicheArticle_() {
        resetFiche(contenu);
        contenu.setArticle(new Articles());
        contenu.setDocVente(new DocVente());
        contenu.setQualite(new Qualite());
        contenu.setConditionnement(new Conditionnement());
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
        return _saveNew(recopieView());
    }

    public boolean _saveNew(DocVente bean) {
        try {
            if (controleFiche(bean)) {
                selectDoc = buildDocVente(bean);
                if (bean.getId() < 1) {
                    selectDoc.setId(null);
                    selectDoc = (YvsComDocVentes) dao.save1(selectDoc);
                    docVente.setId(selectDoc.getId());
                    documents.add(0, selectDoc);
                    if (documents.size() > imax) {
                        documents.remove(documents.size() - 1);
                    }
                } else {
                    dao.update(selectDoc);
                    int idx = documents.indexOf(selectDoc);
                    if (idx > -1) {
                        documents.set(idx, selectDoc);
                    }
                }
                setMontantTotalDoc(docVente);
                docVente.setNumDoc(bean.getNumDoc());
                docVente.setUpdate(true);
                update("data_livraison_vente");
                update("form_entete_livraison_vente");
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
            contenu.setDocVente(docVente);
            if (controleFicheContenu(contenu)) {
                if (contenu.getLots().size() > 1) {
                    ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                    if (w != null) {
                        contenu.setLots(w.buildQuantiteLot(contenu.getLots(), contenu.getQuantite()));
                    }
                    openDialog("dlgListLotReception");
                    update("data-contenu_blv_require_lot");
                    return;
                }
                saveNewArticle(contenu, true);
                Map<String, String> statuts = dao.getEquilibreVente(facture.getId());
                if (statuts != null) {
                    facture.setStatutLivre(statuts.get("statut_livre"));
                    facture.setStatutRegle(statuts.get("statut_regle"));
                }
                resetFicheArticle();
                succes();
                update("data_livraison_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveAllContenuLot() {
        try {
            boolean correct = false;
            if (contenu.getLots() != null ? !contenu.getLots().isEmpty() : false) {
                ContenuDocVente clone;
                List<YvsComLotReception> lots = new ArrayList<>(contenu.getLots());
                for (int i = 0; i < lots.size(); i++) {
                    if (lots.get(i).getQuantitee() > 0) {
                        clone = new ContenuDocVente();
                        cloneObject(clone, contenu);
                        clone.setQuantite(lots.get(i).getQuantitee());
                        clone.getLots().clear();
                        onQuantiteBlur(clone);
                        clone.setLot(UtilCom.buildBeanLotReception(lots.get(i)));
                        correct = saveNewArticle(clone, false);
                    }
                }
            } else {
                correct = saveNewArticle(contenu, true);
            }
            if (correct) {
                Map<String, String> statuts = dao.getEquilibreVente(facture.getId());
                if (statuts != null) {
                    facture.setStatutLivre(statuts.get("statut_livre"));
                    facture.setStatutRegle(statuts.get("statut_regle"));
                }
                resetFicheArticle();
                succes();
                update("data_livraison_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public boolean saveNewArticle(ContenuDocVente contenu, boolean principal) {
        try {
            contenu.setDocVente(docVente);
            if (controleFicheContenu(contenu, principal)) {
                YvsComContenuDocVente en = UtilCom.buildContenuDocVente(contenu, currentUser);
                if (!contenu.isUpdate()) {
                    en.setId(null);
                    en = (YvsComContenuDocVente) dao.save1(en);
                    contenu.setId(en.getId());
                    docVente.getContenus().add(0, en);
                } else {
                    dao.update(en);
                    docVente.getContenus().set(docVente.getContenus().indexOf(en), en);
                }
                if (en.getParent() != null) {
                    int index = contenus_fv.indexOf(en.getParent());
                    if (index > -1) {
                        YvsComContenuDocVente y = contenus_fv.get(index);
                        y.setQuantite(y.getQuantite() - en.getQuantite());
                        if (y.getQuantite() <= 0) {
                            contenus_fv.remove(index);
                        } else {
                            contenus_fv.set(index, y);
                        }
                        update("data_contenu_fv_blv");
                    }
                }
                if (selectDoc != null) {
                    int idx = selectDoc.getContenus().indexOf(en);
                    if (idx > -1) {
                        selectDoc.getContenus().set(idx, en);
                    } else {
                        selectDoc.getContenus().add(0, en);
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
        return false;
    }

    public void buildToSend() {
        try {
            contenusRequireLot.clear();
            if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                Boolean requiere_lot = false;
                String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                for (YvsComContenuDocVente c : contenus_fv) {
                    requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(docVente.getDepot().getId(), 2)});
                    c.setRequiereLot(requiere_lot != null ? requiere_lot : false);
                    if (c.isRequiereLot()) {
                        ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                        if (w != null) {
                            c.setLots(w.loadList(docVente.getDepot().getId(), c.getConditionnement().getId(), docVente.getDateLivraison(), c.getQuantite(), 0));
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
            update("blog-contenu_blv_require_lot");
        }
    }

    public void saveAllArticle() {
        try {
            if (contenus_fv != null ? contenus_fv.isEmpty() : true) {
                getInfoMessage("Cette facture est déja livrée ou n'a pas de contenu");
                return;
            }
            if (docVente != null ? !docVente.isUpdate() : true) {
                if (!_saveNew()) {
                    return;
                }
            }
            if (!_controleFiche_(docVente)) {
                return;
            }
            YvsComContenuDocVente en = null;
            List<YvsComContenuDocVente> contenus = new ArrayList<>();
            for (YvsComContenuDocVente c : contenus_fv) {
                en = saveOneArticle(c, false, false);
                if (en != null ? en.getId() > 0 : false) {
                    contenus.add(en);
                }
            }
            contenus_fv.removeAll(contenus);
            Map<String, String> statuts = dao.getEquilibreVente(facture.getId());
            if (statuts != null) {
                facture.setStatutLivre(statuts.get("statut_livre"));
                facture.setStatutRegle(statuts.get("statut_regle"));
            }
            resetFicheArticle();
            succes();
            update("data_contenu_fv_blv");
            update("data_livraison_vente");
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public YvsComContenuDocVente saveOneArticle(YvsComContenuDocVente c, boolean control, boolean succes) {
        YvsComContenuDocVente en = null;
        try {
            if (control) {
                if (docVente != null ? !docVente.isUpdate() : true) {
                    if (!_saveNew()) {
                        return en;
                    }
                }
                if (!_controleFiche_(docVente)) {
                    return en;
                }
            }
            champ = new String[]{"article", "depot"};
            val = new Object[]{c.getArticle(), new YvsBaseDepots(docVente.getDepot().getId())};
            YvsBaseArticleDepot a = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
            if (a != null ? a.getId() < 1 : true) {
                getErrorMessage("Impossible d'enregistre car le dépôt ne possede pas l'article " + c.getArticle().getDesignation());
                return en;
            }
            if (a.getRequiereLot()) {
                int index = contenusRequireLot.indexOf(c);
                if (index < 0) {
                    getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le depot");
                    return en;
                }
                List<YvsComLotReception> lots = contenusRequireLot.get(index).getLots();
                if (lots != null ? lots.isEmpty() : true) {
                    getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le depot");
                    return en;
                }
                double quantite = 0;
                for (YvsComLotReception y : lots) {
                    if (y.getQuantitee() > 0) {
                        en = new YvsComContenuDocVente(null, c);
                        en.setLot(y);
                        en.setQuantite(y.getQuantitee());
                        en.setAuthor(currentUser);
                        en.setDateSave(new Date());
                        en.setDateUpdate(new Date());
                        en.setParent(new YvsComContenuDocVente(c.getId()));
                        en.setDocVente(selectDoc);
                        en.setNew_(true);
                        en = (YvsComContenuDocVente) dao.save1(en);
                        quantite += y.getQuantitee();
                        if (en != null ? en.getId() > 0 : false) {
                            contenu.setId(en.getId());
                            docVente.getContenus().add(0, en);
                        }
                    }
                }
                if (succes) {
                    if (quantite >= c.getQuantite()) {
                        contenus_fv.remove(c);
                    }
                }
                return en;
            }
            en = new YvsComContenuDocVente(null, c);
            en.setAuthor(currentUser);
            en.setDateSave(new Date());
            en.setDateUpdate(new Date());
            en.setParent(new YvsComContenuDocVente(c.getId()));
            en.setDocVente(selectDoc);
            en.setNew_(true);
            en = (YvsComContenuDocVente) dao.save1(en);
            if (succes) {
                contenus_fv.remove(c);
            }
            if (en != null ? en.getId() > 0 : false) {
                contenu.setId(en.getId());
                docVente.getContenus().add(0, en);
            }
            if (succes) {
                Map<String, String> statuts = dao.getEquilibreVente(facture.getId());
                if (statuts != null) {
                    facture.setStatutLivre(statuts.get("statut_livre"));
                    facture.setStatutRegle(statuts.get("statut_regle"));
                }
                resetFicheArticle();
                succes();
                update("data_contenu_fv_blv");
                update("data_livraison_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
        return en;
    }

    public void updateContenu() {
        try {
            if (!docVente.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
                return;
            }
            if (docVente.isCloturer()) {
                getErrorMessage("Ce document est deja cloturer");
                return;
            }
            dao.update(selectContenu);
            int idx = selectDoc.getContenus().indexOf(selectContenu);
            if (idx > -1) {
                selectDoc.getContenus().set(idx, selectContenu);
            }
            succes();
            update("data_contenu_livraison_vente");
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("blv_delete_doc")) {
                openNotAcces();
                return;
            }
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComDocVentes> list = new ArrayList<>();
                YvsComDocVentes bean;
                for (Long ids : l) {
                    bean = documents.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(bean);
                    if (docVente.getId() == bean.getId()) {
                        resetFiche();
                    }
                }
                documents.removeAll(list);
                succes();
                tabIds = "";
                update("data_livraison_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComDocVentes y) {
        selectDoc = y;
    }

    public boolean deleteBean_() {
        try {
            if (!autoriser("blv_delete_doc")) {
                openNotAcces();
                return false;
            }
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc)) {
                    return false;
                }
                dao.delete(selectDoc);
                documents.remove(selectDoc);
                succes();
                if (docVente.getId() == selectDoc.getId()) {
                    resetFiche();
                }
                update("data_livraison_vente");
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
                    YvsComContenuDocVente bean = docVente.getContenus().get(docVente.getContenus().indexOf(new YvsComContenuDocVente(id)));
                    dao.delete(bean);
                    docVente.getContenus().remove(bean);
                    selectDoc.getContenus().remove(bean);
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
                for (YvsComContenuDocVente c : selectContenus) {
                    dao.delete(c);
                    docVente.getContenus().remove(c);
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

    public void deleteBeanArticle_(YvsComContenuDocVente y) {
        selectContenu = y;
    }

    public void deleteBeanArticle_() {
        try {
            if (selectContenu != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                dao.delete(selectContenu);
                docVente.getContenus().remove(selectContenu);
                if (contenu.getId() == selectContenu.getId()) {
                    resetFicheArticle();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectDistant(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Livraisons Vente", "modGescom", "smenBonLivraisonVente", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsComDocVentes y) {
        selectDoc = y;
        populateView(UtilCom.buildBeanDocVente(selectDoc));
        resetFicheArticle();
        docVente.setContenus(loadContenus(y));
        if (!documents.contains(y)) {
            documents.add(0, y);
        }
        if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getId() > 0 : false) {
            champ = new String[]{"id"};
            val = new Object[]{selectDoc.getDocumentLie().getId()};
            YvsComDocVentes d = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", champ, val);
            if (d != null ? d.getId() > 0 : false) {
                facture = UtilCom.buildSimpleBeanDocVente(d);
                loadContenuFacture(d, false);
//                loadAllDepotLivraison(d.getEnteteDoc() != null ? (d.getEnteteDoc().getCreneau() != null ? (d.getEnteteDoc().getCreneau().getCreneauPoint() != null ? d.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() : new YvsBasePointVente()) : new YvsBasePointVente()) : new YvsBasePointVente());
            }
        }
        ManagedDepot w = (ManagedDepot) giveManagedBean(ManagedDepot.class);
        if (w != null) {
            if (y.getDepotLivrer() != null ? y.getDepotLivrer().getId() > 0 ? y.getDepotLivrer().getAgence() != null ? y.getDepotLivrer().getAgence().getId() > 0 : false : false : false) {
                if (w.getAgenceFind() != y.getDepotLivrer().getAgence().getId()) {
                    w.setAgenceFind(y.getDepotLivrer().getAgence().getId());
                    w.loadDepotsByAgence(w.getAgenceFind(), true, false);
                }
                if (!w.getDepots().contains(y.getDepotLivrer())) {
                    w.getDepots().add(y.getDepotLivrer());
                }
            }
        }
        loadAllTrancheLivraison(selectDoc.getDepotLivrer(), selectDoc.getDateLivraison());
        update("data_contenu_fv_blv");
        update("blog_form_livraison_vente");
    }

    public void onSelectDistantObject(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureVente s = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
            onSelectObject(y);
            tabIds = documents.indexOf(y) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_livraison_vente");
    }

    public void loadOnViewDoc(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocVentes bean = (YvsComDocVentes) ev.getObject();
            onSelectFacture(bean);
        }
    }

    public void loadOnViewContenuDoc(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocVente bean = (YvsComContenuDocVente) ev.getObject();
            onSelectFacture(bean.getDocVente());
        }
    }

    public void loadOnViewContenuFacture(SelectEvent ev) {
        if (ev != null) {
            YvsComContenuDocVente c = (YvsComContenuDocVente) ev.getObject();
            onSelectFacture(c.getDocVente());
            loadOnViewContent(c);
        }
    }

    public void loadOnViewEntete(SelectEvent ev) {

    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectContenu = (YvsComContenuDocVente) ev.getObject();
            populateViewArticle(UtilCom.buildBeanContenuDocVente(selectContenu));
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheArticle();
    }

    private ContenuDocVente buildContent(YvsComContenuDocVente bean) {
        ContenuDocVente c = new ContenuDocVente();
        if (!this.contenu.isUpdate()) {
            cloneObject(c, UtilCom.buildBeanContenuDocVente(bean));
            c.setId(0);
        } else {
            cloneObject(c.getArticle(), UtilProd.buildBeanArticles(bean.getArticle()));
            c.setQuantite(bean.getQuantite());
            c.setPrix(bean.getPrix());
            c.setRemise(bean.getRemise());
            c.setTaxe(bean.getTaxe());
            c.setQuantite_(bean.getQuantite());
            c.setPrixMin(bean.getPrix());
            c.setRemise_(bean.getRemise());
            c.setTaxe_(bean.getTaxe());
        }
        c.setParent(new ContenuDocVente(bean.getSave() > 0 ? bean.getSave() : bean.getId()));
        if (c.getConditionnement() != null ? c.getConditionnement().getId() > 0 : false) {
            if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                c.getArticle().setStock(dao.stocks(c.getArticle().getId(), 0, docVente.getDepot().getId(), 0, 0, docVente.getDateLivraison(), c.getConditionnement().getId(), (c.getLot() != null) ? c.getLot().getId() : -1));
            } else {
                c.getArticle().setStock(dao.stocks(c.getArticle().getId(), 0, 0, currentAgence.getId(), 0, docVente.getDateLivraison(), c.getConditionnement().getId(), (c.getLot() != null) ? c.getLot().getId() : -1));
            }
            if (bean.isRequiereLot() && (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false)) {
                ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                if (w != null) {
                    c.setLots(w.loadList(docVente.getDepot().getId(), c.getConditionnement().getId(), docVente.getDateLivraison(), c.getQuantite(), c.getArticle().getStock()));
                    if (c.getLots().size() == 1) {
                        c.setLot(UtilCom.buildBeanLotReception(c.getLots().get(0)));
                    } else {
                        c.setLot(new LotReception(0, c.getLots().size() + " Lots"));
                    }
                }
            }
            c.getArticle().setPuv(dao.getPuv(c.getArticle().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), docVente.getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getDateLivraison(), c.getConditionnement().getId()));
        }
        //controle les quantités déjà livré
        Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{new YvsComDocVentes(facture.getId()), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, bean.getArticle(), bean.getConditionnement()});
        qteLivre = (qteLivre != null) ? qteLivre : 0;
        c.setSaveLivre(qteLivre);
        //trouve la quantité d'article facturé 
        Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{new YvsComDocVentes(facture.getId()), bean.getArticle(), bean.getConditionnement()});
        qteFacture = (qteFacture != null) ? qteFacture : 0;
        c.setSaveFacture(qteFacture);
        Double qteBonus = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteBonusByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{new YvsComDocVentes(facture.getId()), bean.getArticle(), bean.getConditionnement()});
        qteBonus = (qteBonus != null) ? qteBonus : 0;
        c.setSaveBonus(qteBonus);
        if (bean.isBonus()) {
            c.setSaveFacture(qteBonus + qteFacture);
        }
        //récupère la quantité de l'article dans le document de livraison en cours. (Le pb viens du fait que la ref d'un article peut se trouver plusieurs fois dans la liste d'un bl non encore livré)
        Double qteEncour = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{new YvsComDocVentes(docVente.getId()), bean.getArticle(), bean.getConditionnement()});
        qteEncour = (qteEncour != null) ? qteEncour : 0;
        c.setSaveEncours(qteEncour);
        if (!bean.getDocVente().getStatutRegle().equals(Constantes.ETAT_REGLE)) {
            double reste = (c.getSaveFacture() - c.getSaveLivre() - c.getSaveEncours());
            c.setQuantite_(reste < 0 ? 0 : (!bean.isBonus() ? reste : reste > c.getSaveBonus() ? c.getSaveBonus() : reste));
        } else {
            c.setQuantite(c.getQuantite_());
        }
        selectArt = true;
        c.setUpdate(false);
        c.setDateContenu(new Date());
        return c;
    }

    public void loadOnViewArticle(SelectEvent ev) {
        selectArt = false;
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            if ((ev != null) ? ev.getObject() != null : false) {
                YvsComContenuDocVente y = (YvsComContenuDocVente) ev.getObject();
                loadOnViewContent(y);
            }
        } else {
            getErrorMessage("Vous devez preciser le depot de livraison");
            resetFicheArticle();
        }
        update("desc_contenu_livraison_vente");
    }

    public void loadOnViewContent(YvsComContenuDocVente y) {
        selectArt = false;
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            if (y != null) {
                champ = new String[]{"article", "depot"};
                val = new Object[]{y.getArticle(), new YvsBaseDepots(docVente.getDepot().getId())};
                YvsBaseArticleDepot a = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                if (a != null ? a.getId() > 0 : false) {
                    YvsComContenuDocVente bean = new YvsComContenuDocVente(y);
                    bean.setRequiereLot(a.getRequiereLot());
                    contenu = buildContent(bean);
                    contenu.getArticle().setRequiereLot(a.getRequiereLot());
                    update("blog_form_article_livraison_vente");
                } else {
                    ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                    if (m != null) {
                        ArticleDepot b = new ArticleDepot();
                        b.setArticle(UtilProd.buildBeanArticles(y.getArticle()));
                        b.setDepot(docVente.getDepot());
                        m.setEntityDepot(new YvsBaseDepots(b.getDepot().getId()));
                        m.setOnArticle(false);
                        m.setArticle(b);
                    }
                    openDialog("dlgConfirmAddArticle");
                    update("blog_form_article_depot_blv");
                }
            }
        } else {
            getErrorMessage("Vous devez preciser le depot de livraison");
            resetFicheArticle();
        }
        update("desc_contenu_livraison_vente");
    }

    public void unLoadOnViewArticle(UnselectEvent ev) {
        if (!contenu.isUpdate()) {
            resetFiche(contenu);
            contenu.setArticle(new Articles());
            contenu.setDocVente(new DocVente());
        } else {
            YvsComContenuDocVente c = docVente.getContenus().get(docVente.getContenus().indexOf(new YvsComContenuDocVente(contenu.getId())));
            cloneObject(contenu, UtilCom.buildBeanContenuDocVente(c));
        }
        update("blog_form_article_livraison_vente");
    }

    public void searchFacture() {
        String num = facture.getNumDoc();
        facture.setId(0);
        facture.setError(true);
        ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
        if (m != null) {
            DocVente y = m.searchFacture(num, null, true);
            if (m.getDocuments() != null ? !m.getDocuments().isEmpty() : false) {
                if (m.getDocuments().size() > 1) {
                    update("data_fv_livraison");
                } else {
                    chooseDocVente(y);
                }
                facture.setError(false);
            }
        }
    }

    public void chooseDocVente(DocVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            if (m != null) {
                int idx = m.getDocuments().indexOf(new YvsComDocVentes(y.getId()));
                if (idx > -1) {
                    YvsComDocVentes bean = m.getDocuments().get(idx);
                    onSelectFacture(new YvsComDocVentes(bean));
                }
            }
        } else {
            resetFiche();
        }
    }

    public void onSelectFacture(YvsComDocVentes y) {
        facture = UtilCom.buildSimpleBeanDocVente(y);
        facture.setContenus(new ArrayList<>(y.getContenus()));
        //charge la liste restante à livrer
        loadContenuFacture(y, true);
//        if (loadAllDepotLivraison(y.getEnteteDoc() != null ? (y.getEnteteDoc().getCreneau() != null ? (y.getEnteteDoc().getCreneau().getCreneauPoint() != null ? y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() : new YvsBasePointVente()) : new YvsBasePointVente()) : new YvsBasePointVente())) {
//            chooseDepotLivraison();
//        }
        chooseDepotLivraison();
        resetFicheArticle();
        docVente.getContenus().clear();
        update("data_contenu_livraison_vente");
        update("select_document_facture_vente");
    }

    public void changeCalculPr(YvsComContenuDocVente y) {
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
            getException("ManagedLivraisonVente (changeCalculPr)", ex);
        }
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
            p = new ParametreRequete("y.heureDoc", "heureDoc", dateDebut_);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin_);
        } else {
            p = new ParametreRequete("y.heureDoc", "heureDoc", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    @Override
    public void _chooseAgence() {
        super._chooseAgence();
        loadAllFacture(true, true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamAgence(agence_);
        }
    }

    @Override
    public void _chooseDepot() {
        super._chooseDepot();
        ParametreRequete p;
        if (depot_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.depot", "depot", new YvsBaseDepots(depot_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.depot", "depot", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    @Override
    public void _choosePoint() {
        super._choosePoint();
        ParametreRequete p;
        if (point_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point", "point", new YvsBasePointVente(point_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point", "point", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamPoint(point_);
        }
    }

    public void _chooseTranche() {
        ParametreRequete p;
        if (tranche_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.tranche", "tranche", new YvsGrhTrancheHoraire(tranche_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.tranche", "tranche", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamTranche(tranche_, false);
        }
    }

    // Recherche des factures en selectionnant le vendeur
    public void _chooseVendeur() {
        addParamVendeur(false);
        loadAllFacture(true, true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamVendeur(codeVendeur_);
        }
    }

    // Recherche des factures en ecrivant le nom du vendeur
    public void _searchVendeur() {
        addParamVendeur(true);
        loadAllFacture(true, true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamVendeur(codeVendeur_);
        }
    }

    public void addParamVendeur(boolean code) {
        ParametreRequete p;
        if (!code) {
            if (users_ > 0) {
                p = new ParametreRequete("y.enteteDoc.creneau.users", "vendeur", new YvsUsers(users_));
                p.setOperation("=");
                p.setPredicat("AND");
            } else {
                p = new ParametreRequete("y.enteteDoc.creneau.users", "vendeur", null);
            }
        } else {
            if (codeVendeur_ != null ? codeVendeur_.trim().length() > 0 : false) {
                p = new ParametreRequete("y.enteteDoc.creneau.users.codeUsers", "vendeur", codeVendeur_ + "%", " LIKE ", "OR");
                ParametreRequete p1 = new ParametreRequete("enteteDoc.creneau.users.nomUser", "vendeur", codeVendeur_ + "%", " LIKE ", "AND");
//                p.getOtherExpression().add(p1);
            } else {
                p = new ParametreRequete("y.enteteDoc.creneau.users.codeUsers", "vendeur", null);
            }
        }
        paginator.addParam(p);
    }

    public void _chooseStatut(ValueChangeEvent ev) {
        statut = ((String) ev.getNewValue());
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamStatut(statut_);
        }
    }

    public void findByDate(boolean find) {
        ParametreRequete p = new ParametreRequete("y.dateLivraison", "dateLivraison", null, " BETWEEN ", "AND");
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

    public void choixParamDate(ValueChangeEvent ev) {
        date_ = (boolean) ev.getNewValue();
        findByDate(date_);
    }

    public void clearParams() {
        numSearch_ = null;
        codeLivreur_ = null;
        statut_ = null;
        cloturer_ = null;

        agence_ = currentAgence.getId();
        depotSearch = 0;

        date_ = false;
        dateDebut = new Date();
        dateFin = new Date();
        idsSearch = "";

        paginator.getParams().clear();

        loadAllFacture(true, true);
    }

    public void addParamDate(SelectEvent ev) {
        findByDate(date_);
    }
//
//    public void _chooseDateSearch() {
//        ParametreRequete p=new ParametreRequete();
//        if (date) {
//
//            p.setOperation("BETWEEN");
//            p.setPredicat("AND");
//            p.setOtherObjet(dateFin);
//        } else {
//            p = new ParametreRequete("yenteteDoc.dateEntete", "dateEntete", null);
//        }
//        paginator.addParam(p);
//        loadAllFacture(true, true);
//        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
//        if (m != null) {
//            m.addParamDate(date, dateDebut, dateFin);
//        }
//        update("_select_entete_facture_vente");
//    }

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

    public void chooseLivreur(boolean code) {
        ParametreRequete p;
        if (!code) {
            if (livreur_ > 0) {
                p = new ParametreRequete("y.livreur", "livreur", new YvsUsers(livreur_));
                p.setOperation("=");
                p.setPredicat("AND");
            } else {
                p = new ParametreRequete("y.livreur", "livreur", null);
            }
        } else {
            if (codeLivreur_ != null ? codeLivreur_.trim().length() > 0 : false) {
                p = new ParametreRequete("y.livreur.codeUsers", "livreur", codeVendeur_ + "%", " LIKE ", "OR");
                ParametreRequete p1 = new ParametreRequete("y.livreur.nomUser", "livreur", codeVendeur_ + "%", " LIKE ", "AND");
//                p.getOtherExpression().add(p1);
            } else {
                p = new ParametreRequete("y.livreur.codeUsers", "livreur", null);
            }
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void addParamAgence() {
        ParametreRequete p;
        if (agence_ > 0) {
            p = new ParametreRequete("y.depotLivrer.agence", "agence", new YvsAgences(agence_), "=", "AND");
        } else {
            p = new ParametreRequete("y.depotLivrer.agence", "agence", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
        _loadDepot();
    }

    public void chooseDepotLivr() {
        ParametreRequete p;
        if (depotSearch > 0) {
            p = new ParametreRequete("y.depotLivrer", "depotLivrer", new YvsBaseDepots(depotSearch));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.depotLivrer", "depotLivrer", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void chooseIsBon() {
        initView();
    }

    public void chooseTrancheLivraison() {
        if (docVente.getTranche() != null ? docVente.getTranche().getId() > 0 : false) {
            int idx = tranches_livraison.indexOf(new YvsGrhTrancheHoraire(docVente.getTranche().getId()));
            if (idx > -1) {
                YvsGrhTrancheHoraire y = tranches_livraison.get(idx);
                cloneObject(docVente.getTranche(), UtilCom.buildBeanTrancheHoraire(y));
            }
        }
    }

    public void chooseDepotLivraison() {
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            YvsBaseDepots y = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{docVente.getDepot().getId()});
            cloneObject(docVente.getDepot(), UtilCom.buildBeanDepot(y));
            if (!verifyOperation(docVente.getDepot(), Constantes.SORTIE, Constantes.VENTE, false)) {
                return;
            }
            loadAllTrancheLivraison(y, docVente.getDateLivraison());
        }
    }

    public void initFactureVente() {
        setExistFacture(true);
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            if (!selectDoc.isFacture()) {
                String[] ch = new String[]{"documentLie", "typeDoc"};
                Object[] v = new Object[]{new YvsComDocVentes(selectDoc.getId()), Constantes.TYPE_FAV};
                List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParentTypeDoc", ch, v);
                if ((l != null) ? !l.isEmpty() : false) {
                    getWarningMessage("Ce document a deja une facture!");
                    docSelect = UtilCom.buildBeanDocVente(l.get(0));
                    contenus_fv = _loadContenus(l.get(0));
                } else {
                    docSelect = UtilCom.buildBeanDocVente(selectDoc);
                    docSelect.setDocumentLie(new DocVente(selectDoc.getId(), selectDoc.getNumDoc(), selectDoc.getStatut()));
                    docSelect.setHeureDoc(new Date());
                    docSelect.setUpdate(false);
                    docSelect.setTypeDoc(Constantes.TYPE_FAV);
//                    docSelect.setNumDoc(genererReference(Constantes.TYPE_FAV_NAME, new Date()));
                    cloneObject(docSelect.getCategorieComptable(), docSelect.getClient().getCategorieComptable());
                    docSelect.setStatut(Constantes.ETAT_EDITABLE);

                    contenus_fv = _loadContenus(selectDoc);
                    setExistFacture(false);
                }
                docSelect.setMontantHT(0);
                for (YvsComContenuDocVente c : contenus_fv) {
                    c.setQuantite_(c.getQuantite());
                    c.setPrix_(c.getPrix());
                    c.setRemise_(c.getRemise());
                    c.setTaxe_(c.getTaxe());
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
                docSelect = UtilCom.buildBeanDocVente(selectDoc);
                openDialog("dlgStatut");
                update("grp_btn_etat_");
            } else {
                getErrorMessage("Cette option est associée aux factures");
            }
        }
    }

    public void initLivreurs() {
        ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
        if (m != null) {
            m.initUsers(docVente.getLivreur());
            update("data_livreur_livraison_vente");
        }
    }

    public void onQuantiteBlur() {
        onQuantiteBlur(contenu);
    }

    public void onQuantiteBlur(ContenuDocVente contenu) {
        double prix = contenu.getPrix() - contenu.getRabais();
        if ((docVente.getClient() != null) ? docVente.getClient().getId() > 0 : false) {
            if ((contenu.getConditionnement() != null) ? contenu.getConditionnement().getId() > 0 : false) {
                contenu.setRemise(dao.getRemiseVente(contenu.getArticle().getId(), contenu.getQuantite(), prix, docVente.getClient().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId()));
            }
        }
        double total = contenu.getQuantite() * prix;
        contenu.setPrixTotal(total - contenu.getRemise());
        if (docVente.getCategorieComptable() != null ? docVente.getCategorieComptable().getId() > 0 : false) {
            contenu.setTaxe(dao.getTaxe(contenu.getArticle().getId(), docVente.getCategorieComptable().getId(), 0, contenu.getRemise(), contenu.getQuantite(), prix, true, 0));
            contenu.setPrixTotal(contenu.getPrixTotal() + contenu.getTaxe());
        }
    }

    public void print(YvsComDocVentes y) {
        print(y, true);
    }

    public void print(YvsComDocVentes y, boolean withHeader) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                param.put("ID", y.getId().intValue());
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(withHeader));
                String report = "bon_livraison";
                if (currentParam != null ? currentParam.getUseLotReception() : false) {
                    report = "bon_livraison_by_lot";
                }
                executeReport(report, param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedOtherTransfert.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadOnViewLivreur(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsUsers bean = (YvsUsers) ev.getObject();
            docVente.setLivreur(UtilUsers.buildBeanUsers(bean));
            update("txt_livreur_livraison_vente");
        }
    }

    public void searchLivreur() {
        String num = docVente.getLivreur().getCodeUsers();
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedUser m = (ManagedUser) giveManagedBean(ManagedUser.class);
            if (m != null) {
                Users e = m.searchUsersActif(num, true);
                docVente.setLivreur(e);
                if (m.getListUser() != null ? m.getListUser().size() > 1 : false) {
                    update("data_livreur_livraison_vente");
                }
            }
        }
    }

    public void sendByMail() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            YvsComClient fsseur = selectDoc.getClient();
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

    public boolean annulerOrderByForce() {
        return annulerOrder(_entity_, _bean_, _load_, _msg_, _open_, true);
    }

    public boolean annulerOrder(boolean force) {
        return annulerOrder(selectDoc, docVente, false, true, true, force);
    }

    public boolean annulerOrder(YvsComDocVentes y, boolean load, boolean msg, boolean open, boolean force) {
        return annulerOrder(y, UtilCom.buildSimpleBeanDocVente(y), load, msg, open, force);
    }

    public boolean controlToAnnulerOrder(YvsComDocVentes y, boolean load, boolean msg, boolean open, boolean force) {
        return controlToAnnulerOrder(y, UtilCom.buildSimpleBeanDocVente(y), load, msg, open, force);
    }

    public boolean controlToAnnulerOrder(YvsComDocVentes entity, DocVente bean, boolean load, boolean msg, boolean open, boolean force) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                if (dao.isComptabilise(entity.getId(), Constantes.SCR_VENTE)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return false;
                    }
                }
                List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{entity});
                if (force) {
                    List<YvsComDocVentes> list = new ArrayList<>();
                    list.addAll(l);
                    for (YvsComDocVentes d : list) {
                        dao.delete(d);
                        l.remove(d);
                    }
                }
                // Vérifié qu'aucun document d'inventaire n'exite après cette date
                boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
                if (!force || _exist_inventaire_ == null) {
                    _exist_inventaire_ = !controleInventaire(bean.getDepot().getId(), bean.getDateLivraison(), bean.getTranche().getId(), !gescom_update_stock_after_valide);
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
                            getWarningMessage("Un inventaire est déja passé dans le dépot " + bean.getDepot().getDesignation() + " apres le " + formatDate.format(bean.getDateLivraison()));
                            return false;
                        }
                    }
                }
                if (l != null ? !l.isEmpty() : false) {
                    for (YvsComDocVentes d : l) {
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
                    return false;
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedLivraisonVente (controlToAnnulerOrder)", ex);
        }
        return false;
    }

    public boolean annulerOrder(YvsComDocVentes entity, DocVente bean, boolean load, boolean msg, boolean open, boolean force) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                if (!controlToAnnulerOrder(entity, bean, load, msg, open, force)) {
                    return false;
                }
                if (changeStatut(Constantes.ETAT_EDITABLE, bean, entity, load, true)) {
                    entity.setCloturer(false);
                    entity.setAnnulerBy(null);
                    entity.setCloturerBy(null);
                    entity.setValiderBy(null);
                    entity.setDateAnnuler(null);
                    entity.setDateCloturer(null);
                    entity.setDateValider(null);
                    entity.setStatut(Constantes.ETAT_EDITABLE);
                    entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_ATTENTE));
                    entity.setDateLivraison(null);
                    if (currentUser != null ? currentUser.getId() > 0 : false) {
                        entity.setAuthor(currentUser);
                    }
                    if (_exist_inventaire_) {
                        YvsComDocStocks inventaire = dao.lastInventaire(entity.getDepotLivrer().getId(), bean.getDateLivraison(), entity.getTrancheLivrer().getId());
                        if (inventaire != null ? inventaire.getId() > 0 : false) {
                            for (YvsComContenuDocVente c : entity.getContenus()) {
                                majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), Constantes.MOUV_ENTREE);
                            }
                        }
                    }
                    YvsComDocVentes y = new YvsComDocVentes(entity);
                    y.getContenus().clear();
                    dao.update(y);
                    if (entity.getDocumentLie() != null ? (entity.getDocumentLie().getId() != null ? entity.getDocumentLie().getId() > 0 : false) : false) {
                        Map<String, String> statuts = dao.getEquilibreVente(entity.getDocumentLie().getId());
                        if (statuts != null) {
                            entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                            entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                        }
                    }
                    if (documents != null) {
                        int idx = documents.indexOf(entity);
                        if (idx > -1) {
                            documents.set(idx, entity);
                        }
                    }
                    _exist_inventaire_ = null;
                    return true;
                }
                if (facture != null ? entity.getDocumentLie() != null ? !entity.getDocumentLie().getId().equals(facture.getId()) : false : false) {
                    Map<String, String> statuts = dao.getEquilibreVente(facture.getId());
                    if (statuts != null) {
                        facture.setStatutLivre(statuts.get("statut_livre"));
                        facture.setStatutRegle(statuts.get("statut_regle"));
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedLivraisonVente (annulerOrder)", ex);
        }
        return false;
    }

    public boolean refuserOrderByForce() {
        return refuserOrder(_entity_, _bean_, _load_, _msg_, _open_, true);
    }

    public boolean refuserOrder(boolean force) {
        return refuserOrder(selectDoc, docVente, false, true, true, force);
    }

    public boolean refuserOrder(YvsComDocVentes y, boolean load, boolean msg, boolean open, boolean force) {
        return refuserOrder(y, UtilCom.buildSimpleBeanDocVente(y), load, msg, open, force);
    }

    public boolean refuserOrder(YvsComDocVentes entity, DocVente bean, boolean load, boolean msg, boolean open, boolean force) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                if (dao.isComptabilise(entity.getId(), Constantes.SCR_VENTE)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return false;
                    }
                }
                List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{entity});
                if (force) {
                    List<YvsComDocVentes> list = new ArrayList<>();
                    list.addAll(l);
                    for (YvsComDocVentes d : list) {
                        dao.delete(d);
                        l.remove(d);
                    }
                }
                if (l != null ? l.isEmpty() : true) {
                    if (changeStatut(Constantes.ETAT_ANNULE, bean, entity, load, true)) {
                        entity.setCloturer(false);
                        entity.setAnnulerBy(currentUser.getUsers());
                        entity.setValiderBy(null);
                        entity.setDateAnnuler(new Date());
                        entity.setDateCloturer(null);
                        entity.setDateValider(null);
                        entity.setLivreur(null);
                        entity.setTrancheLivrer(null);
                        entity.setStatut(Constantes.ETAT_ANNULE);
                        entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_ATTENTE));
                        entity.setDateLivraison(null);
                        YvsComDocVentes y = new YvsComDocVentes(entity);
                        y.getContenus().clear();
                        dao.update(y);
                        Map<String, String> statuts = dao.getEquilibreVente(facture.getId());
                        if (statuts != null) {
                            facture.setStatutLivre(statuts.get("statut_livre"));
                            facture.setStatutRegle(statuts.get("statut_regle"));
                        }
                        if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() > 0 : false) {
                            statuts = dao.getEquilibreVente(entity.getDocumentLie().getId());
                            if (statuts != null) {
                                entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                                entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                            }
                        }
                        return true;
                    }
                } else {
                    for (YvsComDocVentes d : l) {
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
            System.err.println("Erreur : " + ex.getMessage());
        }
        return false;
    }

    public void transmisOrder() {
        if (selectDoc == null) {
            return;
        }
        if (changeStatut(Constantes.ETAT_SOUMIS)) {
            if (selectDoc.getDocumentLie() != null ? (selectDoc.getDocumentLie().getId() != null ? selectDoc.getDocumentLie().getId() > 0 : false) : false) {
                Map<String, String> statuts = dao.getEquilibreVente(selectDoc.getDocumentLie().getId());
                if (statuts != null) {
                    selectDoc.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                    selectDoc.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                }
            }
        }
    }
    YvsComDocVentes _entity_;
    DocVente _bean_;
    boolean _save_, _load_, _msg_, _open_;
    Boolean _exist_inventaire_;

    public void setExistInventaire(Boolean _exist_inventaire_) {
        this._exist_inventaire_ = _exist_inventaire_;
    }

    public void validerOrder() {
        validerOrder(selectDoc, docVente, true, false, true, null, false);
    }

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

    public boolean validerOrderOne(YvsComDocVentes entity, boolean succes, boolean controle) {
        if (!entity.getStatut().equals(Constantes.ETAT_VALIDE)) {
            DocVente bean = UtilCom.buildBeanDocVente(entity);
            boolean response = validerOrder(entity, bean, false, false, true, null, false, controle);
            if (response && succes) {
                succes();
            }
            return response;
        }
        return false;
    }

    public boolean validerOrder(YvsComDocVentes y, boolean save, boolean load, boolean msg, Boolean exist_inventaire, boolean force) {
        return validerOrder(y, UtilCom.buildSimpleBeanDocVente(y, true), save, load, msg, exist_inventaire, force);
    }

    public boolean validerOrderByForce() {
        return validerOrder(_entity_, _bean_, _save_, _load_, _msg_, _exist_inventaire_, true);
    }

    public boolean validerOrder(YvsComDocVentes entity, DocVente bean, boolean save, boolean load, boolean msg, Boolean exist_inventaire, boolean force) {
        return validerOrder(entity, bean, save, load, msg, exist_inventaire, force, true);
    }

    public boolean validerOrder(YvsComDocVentes entity, DocVente bean, boolean save, boolean load, boolean msg, Boolean exist_inventaire, boolean force, boolean controle) {
        if (entity == null) {
            return false;
        }
        if (!autoriser("blv_valide_doc")) {
            openNotAcces();
            return false;
        }
        if (!verifyOperation(bean.getDepot(), Constantes.SORTIE, Constantes.VENTE, msg)) {
            return false;
        }
        //contrôle la cohérence avec les inventaires
        boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
        if (exist_inventaire == null) {
            exist_inventaire = !verifyInventaire(bean.getDepot(), bean.getTranche(), bean.getDateLivraison(), (gescom_update_stock_after_valide ? false : msg));
        }
        if (exist_inventaire) {
            if (!gescom_update_stock_after_valide) {
                return false;
            } else if (!force) {
                _entity_ = entity;
                _bean_ = bean;
                _save_ = save;
                _load_ = load;
                _msg_ = msg;
                _exist_inventaire_ = exist_inventaire;
                if (controle) {
                    openDialog("dlgConfirmChangeInventaireByValid");
                }
                getWarningMessage("Un inventaire est déja passé dans le dépot " + bean.getDepot().getDesignation() + " apres le " + formatDate.format(bean.getDateLivraison()));
                return false;
            }
        }
        boolean continu = !save;
        if (save) {
            continu = _saveNew(bean);
        }
        if (continu) {
            if (livrer(entity, bean, msg, exist_inventaire, force)) {
                if (changeStatut(Constantes.ETAT_VALIDE, bean, entity, load, false)) {
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
                    YvsComDocVentes y = new YvsComDocVentes(entity);
                    y.getContenus().clear();
                    dao.update(y);
                    Map<String, String> statuts = dao.getEquilibreVente(facture.getId());
                    if (statuts != null) {
                        facture.setStatutLivre(statuts.get("statut_livre"));
                        facture.setStatutRegle(statuts.get("statut_regle"));
                    }
                    if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() > 0 : false) {
                        statuts = dao.getEquilibreVente(entity.getDocumentLie().getId());
                        if (statuts != null) {
                            entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                            entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean livrer(YvsComDocVentes entity, DocVente bean, boolean msg, Boolean exist_inventaire, boolean force) {
        if (!autoriser("fv_livrer")) {
            openNotAcces();
            return false;
        }
        if (entity == null) {
            return false;
        }
        if (bean.getDepot() != null ? bean.getDepot().getId() < 1 : true) {
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
        }
        if (bean.getLivreur() != null ? bean.getLivreur().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez specifier le livreur");
            }
            return false;
        }
        //verifier la cohérence avec les inventaires
        boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
        if (exist_inventaire == null) {
            exist_inventaire = !controleInventaire(entity.getDepotLivrer().getId(), entity.getDateLivraison(), entity.getTrancheLivrer().getId(), (gescom_update_stock_after_valide ? false : msg));
        }
        if (exist_inventaire) {
            if (!gescom_update_stock_after_valide) {
                return false;
            } else if (!force) {
                _entity_ = entity;
                _bean_ = bean;
                _msg_ = msg;
                openDialog("dlgConfirmChangeInventaireByValid");
                getWarningMessage("Un inventaire est déja passé dans le dépot " + entity.getDepotLivrer().getDesignation() + " apres le " + formatDate.format(entity.getDateLivraison()));
                return false;
            }
        }
        YvsComDocVentes facture = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{bean.getDocumentLie().getId()});
        if (facture.getEnteteDoc() != null ? (facture.getEnteteDoc().getCreneau() != null ? (facture.getEnteteDoc().getCreneau().getCreneauPoint() != null ? (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null) : false) : false) : false) {
            switch (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getLivraisonOn()) {
                case 'R': {
                    if (!facture.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                        if (msg) {
                            getErrorMessage("Cette facture doit etre reglée avant de pouvoir générer une livraison");
                        }
                        return false;
                    }
                }
                case 'V': {
                    if (!facture.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        if (msg) {
                            getErrorMessage("Cette facture doit etre validée avant de pouvoir générer une livraison");
                        }
                        return false;
                    }
                }
            }
        }
        if (facture.getModelReglement() != null ? (facture.getModelReglement().getPayeBeforeValide() ? !facture.getStatutRegle().equals(Constantes.ETAT_REGLE) : false) : false) {
            if (msg) {
                getErrorMessage("La facture doit étre payée!");
            }
            return false;
        }
        if (entity.getContenus() != null ? !entity.getContenus().isEmpty() : false) {
            if (currentParamVente != null ? currentParamVente.getId() < 1 : true) {
                currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            for (YvsComContenuDocVente c : entity.getContenus()) {
                champ = new String[]{"article", "depot"};
                val = new Object[]{c.getArticle(), new YvsBaseDepots(bean.getDepot().getId())};
                YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                if (y != null ? y.getId() < 1 : true) {
                    if (msg) {
                        getErrorMessage("Impossible d'effectuer cette action... Car le depot " + bean.getDepot().getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
                    }
                    return false;
                }
                if (y.getRequiereLot() ? (c.getLot() != null ? c.getLot().getId() < 1 : true) : false) {
                    if (msg) {
                        getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + " dans le depot " + bean.getDepot().getDesignation());
                    }
                    return false;
                }
                if (!exist_inventaire) {
                    String result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), bean.getDepot().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", bean.getDateLivraison(), (c.getLot() != null ? c.getLot().getId() : 0));
                    if (result != null) {
                        if (msg) {
                            getErrorMessage("Impossible de valider ce document", "la ligne d'article " + c.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + result);
                        }
                        return false;
                    }
                }
                //controle les quantités dans le bon de livraison
                Double quantite = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{entity, c.getArticle(), c.getConditionnement()});
                quantite = (quantite != null) ? quantite : 0;
                //controle les quantités déjà livré
                Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{facture, Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, c.getArticle(), c.getConditionnement()});
                qteLivre = (qteLivre != null) ? qteLivre : 0;
                //trouve la quantité d'article facturé 
                Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{facture, c.getArticle(), c.getConditionnement()});
                qteFacture = (qteFacture != null) ? qteFacture : 0;
                if (facture != null ? (currentParamVente != null ? currentParamVente.getGiveBonusInStatus().equals("R") ? facture.getStatutRegle().equals(Constantes.ETAT_REGLE) : true : false) : false) {
                    //trouve la quantité d'article facturé 
                    Double qteBonusFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteBonusByFacture", new String[]{"docVente", "article", "unite"}, new Object[]{facture, c.getArticle(), c.getConditionnement()});
                    qteBonusFacture = (qteBonusFacture != null) ? qteBonusFacture : 0;
                    if (quantite > ((qteFacture + qteBonusFacture) - qteLivre)) {
                        if (msg) {
                            getErrorMessage("Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !");
                        }
                        return false;
                    }
                } else {
                    //si la facture n'est pas encore réglé, on ne dois pas inclure la quantité bonus dans la quantité à livrer
                    if (quantite > (qteFacture - qteLivre)) {
                        if (msg) {
                            getErrorMessage("Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !");
                        }
                        return false;
                    }
                }
            }
            if (exist_inventaire) {
                YvsComDocStocks inventaire = dao.lastInventaire(entity.getDepotLivrer().getId(), entity.getDateLivraison(), entity.getTrancheLivrer().getId());
                if (inventaire != null ? inventaire.getId() > 0 : false) {
                    for (YvsComContenuDocVente c : entity.getContenus()) {
                        majInventaire(inventaire, c.getArticle(), c.getConditionnement(), c.getQuantite(), Constantes.MOUV_SORTIE);
                    }
                }
            }
            bean.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entity.setDateLivraison(bean.getStatutLivre().equals(String.valueOf(Constantes.STATUT_DOC_LIVRER)) ? new Date() : null);
            return true;
        } else {
            if (msg) {
                getErrorMessage("Vous ne pouvez pas valider un document vide");
            }
        }
        return false;
    }

    public void cloturer(YvsComDocVentes y) {
        selectDoc = y;
        update("id_confirm_close_blv");
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }
        docVente.setCloturer(!docVente.isCloturer());
        selectDoc.setCloturer(docVente.isCloturer());
        selectDoc.setDateCloturer(docVente.isCloturer() ? new Date() : null);
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        documents.set(documents.indexOf(selectDoc), selectDoc);
        succes();
        update("data_livraison_vente");
    }

    public void _cloturer() {
        if (selectDoc == null) {
            return;
        }
        docVente.setCloturer(!docSelect.isCloturer());
        selectDoc.setCloturer(docSelect.isCloturer());
        selectDoc.setDateCloturer(docSelect.isCloturer() ? new Date() : null);
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        documents.set(documents.indexOf(selectDoc), selectDoc);
        succes();
        update("data_livraison_vente");
    }

    public boolean _changeStatut(String etat) {
        saveNew();
        if (changeStatut_(etat, docSelect, selectDoc, true, true)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut(String etat) {
        if (changeStatut_(etat, true)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, boolean load) {
        return changeStatut_(etat, docVente, selectDoc, load);
    }

    public boolean changeStatut(String etat, YvsComDocVentes entity, boolean load) {
        if (changeStatut_(etat, entity, load)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, YvsComDocVentes entity, boolean load) {
        return changeStatut_(etat, UtilCom.buildBeanDocVente(entity), entity, load);
    }

    public boolean changeStatut(String etat, DocVente bean, YvsComDocVentes entity, boolean load, boolean msg) {
        if (changeStatut_(etat, bean, entity, load)) {
            if (msg) {
                succes();
            }
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, DocVente bean, YvsComDocVentes entity, boolean load) {
        return changeStatut_(etat, bean, entity, true, load);
    }

    public boolean changeStatut_(String etat, DocVente bean, YvsComDocVentes entity, boolean isBon, boolean load) {
        if (!etat.equals("")) {
            if (bean.isCloturer()) {
                getErrorMessage("Le document est provisoirement vérouillé !");
                return false;
            }
            String rq = "UPDATE yvs_com_doc_ventes SET statut =?, date_livraison=? WHERE id=?";
            Options[] param = new Options[]{new Options(etat, 1), new Options(entity.getDateLivraison(), 2), new Options(bean.getId(), 3)};
//            dao.requeteLibre(rq, param);
            bean.setStatut(etat);
            entity.setStatut(etat);
            int idx = documents.indexOf(entity);
            if (idx > -1) {
                documents.set(idx, entity);
            }
            if (load) {
                ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
                if (m != null) {
                    m.loadFactureNonLivre(true, true);
                    update("data_fv_livraison");
                }
            }
            update("infos_document_livraison_vente");
            update("data_livraison_vente");
            return true;
        }
        return false;
    }

    public void addParamIds() {
        addParamIds(true);
        loadAllFacture(true, true);
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
        loadAllFacture(true, true);
    }

    public void removeDoublon() {
        if ((selectDoc != null) ? selectDoc.getId() > 0 : false) {
            if (!selectDoc.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre editable pour pouvoir etre modifié");
                return;
            }
            removeDoublonVente(selectDoc.getId());
            succes();
        }
    }

    @Override
    public double setMontantTotalDoc(DocVente doc) {
        if (doc != null ? doc.getId() > 0 : false) {
            if (doc.getDocumentLie() != null ? doc.getDocumentLie().getId() > 0 : false) {
                DocVente d = doc.getDocumentLie();
                super.setMontantTotalDoc(d);
                doc.setMontantRemise(d.getMontantRemise());
                doc.setMontantTaxe(d.getMontantTaxe());
                doc.setMontantRistourne(d.getMontantRistourne());
                doc.setMontantCommission(d.getMontantCommission());
                doc.setMontantHT(d.getMontantHT());
                doc.setMontantRemises(d.getMontantRemise());
                doc.setMontantAvance(d.getMontantAvance());
                doc.setMontantCS(d.getMontantCS());

                champ = new String[]{"docVente", "sens"};
                val = new Object[]{new YvsComDocVentes(doc.getId()), true};
                Double p = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumByDocVente", champ, val);
                val = new Object[]{new YvsComDocVentes(doc.getId()), false};
                Double m = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumByDocVente", champ, val);
                double s = (p != null ? p : 0) - (m != null ? m : 0);
                doc.setMontantCS(d.getMontantCS() + s);
            }
            update("blog_form_montant_doc_blv");
        }
        return doc.getMontantTotal();
    }

    @Override
    public void cleanVente() {
        super.cleanVente();
        loadAllFacture(true, true);
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.docVente.numDoc", "reference", null);
        if (reference != null ? reference.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", reference + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.numDoc)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.nomClient)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.codeClient)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.nom)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.enteteDoc.creneau.users.codeUsers)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.enteteDoc.creneau.users.nomUsers)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamStatutContenu() {
        ParametreRequete p = new ParametreRequete("y.docVente.statut", "statut", null);
        if (statutContenu != null ? statutContenu.trim().length() > 0 : false) {
            p = new ParametreRequete("y.docVente.statut", "statut", statutContenu, opeStatut, "AND");
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamStatutLivContenu() {
        ParametreRequete p = new ParametreRequete("y.statutLivree", "statutLiv", null);
        if (statutLiv != null ? statutLiv > 0 : false) {
            p = new ParametreRequete("y.statutLivree", "statutLiv", statutLiv, opStatutLiv, "AND");
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamStatutRegContenu() {
        ParametreRequete p = new ParametreRequete("y.docVente.statutRegle", "statutRegle", null);
        if (statutReg != null ? statutReg.trim().length() > 0 : false) {
            p = new ParametreRequete("y.docVente.statutRegle", "statutRegle", statutReg, opStatutReg, "AND");
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

    public void addParamPoint() {
        ParametreRequete p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.code", "point", null);
        if (pointvente != null ? pointvente.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "point", pointvente + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.enteteDoc.creneau.creneauPoint.point.code)", "point", pointvente.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.enteteDoc.creneau.creneauPoint.point.libelle)", "point", pointvente.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamAgenceContenu() {
        ParametreRequete p;
        if (agenceContenu > 0) {
            p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.agence", "agence", new YvsAgences(agenceContenu));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.agence", "agence", null);
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamClient() {
        ParametreRequete p = new ParametreRequete("y.docVente.client", "client", null);
        if (clientF != null ? clientF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "client", clientF + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.codeClient)", "code", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.nom)", "nom", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.prenom)", "prenom", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.tiers.codeTiers)", "codeT", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(CONCAT(y.docVente.client.nom, ' ', y.docVente.client.prenom))", "nom_pre", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamDateContenu(SelectEvent ev) {
        findByDateContenu();
    }

    public void findByDateContenu() {
        ParametreRequete p = new ParametreRequete("y.docVente.dateLivraisonPrevu", "date", null, "BETWEEN", "AND");
        if (dateContenu) {
            if (dateDebutContenu != null && dateFinContenu != null) {
                if (dateDebutContenu.before(dateFinContenu) || dateDebutContenu.equals(dateFinContenu)) {
                    p = new ParametreRequete(null, "date", dateDebutContenu, dateFinContenu, "BETWEEN", "AND");
                    p.getOtherExpression().add(new ParametreRequete("y.docVente.dateLivraisonPrevu", "date", dateDebutContenu, dateFinContenu, "BETWEEN", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("y.docVente.enteteDoc.dateEntete", "date", dateDebutContenu, dateFinContenu, "BETWEEN", "OR"));
                    p.getOtherExpression().add(new ParametreRequete("y.docVente.dateSave", "date", dateDebutContenu, dateFinContenu, "BETWEEN", "OR"));
                }
            }
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void clearParamContenu() {
        p_contenu.getParams().clear();
        loadContenus(true, true);
    }

    public void chooseAgenceLivraison() {
        if (agenceChange > 0) {
            if ((selectDoc != null ? (selectDoc.getEnteteDoc() != null ? (selectDoc.getEnteteDoc().getCreneau() != null ? (selectDoc.getEnteteDoc().getCreneau().getCreneauPoint() != null ? selectDoc.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null : false) : false) : false) : false)) {
                loadDepotByPoint(selectDoc.getEnteteDoc().getCreneau().getCreneauPoint().getPoint(), new YvsAgences(agenceChange));
            }
        }
    }

    public void beginLivraison(YvsComContenuDocVente y, boolean loadDepot) {
        beginLivraison(y != null ? y.getDocVente() : null, y, loadDepot);
    }

    public void beginLivraison(YvsComDocVentes facture, YvsComContenuDocVente y, boolean loadDepot) {
        if (agenceChange < 1) {
            agenceChange = currentAgence.getId();
        }
        selectContenu = y;
        selectDoc = facture;
        if (y != null ? y.getId() > 0 : false) {
            if (y.getDepoLivraisonPrevu() != null ? y.getDepoLivraisonPrevu().getId() > 0 : false) {
                depotContenu = new Depots(y.getDepoLivraisonPrevu().getId(), y.getDepoLivraisonPrevu().getDesignation());
            } else if (y.getDocVente().getDepotLivrer() != null ? y.getDocVente().getDepotLivrer().getId() > 0 : false) {
                depotContenu = new Depots(y.getDocVente().getDepotLivrer().getId(), y.getDocVente().getDepotLivrer().getDesignation());
            }
            cloneObject(depotChange, depotContenu);
            //controle les quantités déjà livré
            Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByParentTypeStatutArticleS", new String[]{"parent", "statut", "typeDoc"}, new Object[]{y, Constantes.ETAT_VALIDE, Constantes.TYPE_BLV});
            if (qteLivre != null ? qteLivre < 1 : true) {
                qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{y.getDocVente(), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV, y.getArticle(), y.getConditionnement()});
            }
            qteLivre = (qteLivre != null) ? qteLivre : 0;
            y.setQteLivree(qteLivre);
            y.setQteRestant(y.getQuantite() - y.getQteLivree());
            dateLivraison = (selectDoc.getDateLivraisonPrevu() != null) ? selectDoc.getDateLivraisonPrevu() : new Date();
        }
        if ((loadDepot) && (selectDoc != null ? (selectDoc.getEnteteDoc() != null ? (selectDoc.getEnteteDoc().getCreneau() != null ? (selectDoc.getEnteteDoc().getCreneau().getCreneauPoint() != null ? selectDoc.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null : false) : false) : false) : false)) {
            loadDepotByPoint(selectDoc.getEnteteDoc().getCreneau().getCreneauPoint().getPoint(), new YvsAgences(agenceChange));
        }
        chooseDepotContenu();
    }

    public YvsComContenuDocVente changeDepotContenu() {
        return changeDepotContenu(selectContenu, true);
    }

    public YvsComContenuDocVente changeDepotContenu(YvsComContenuDocVente selectContenu, boolean succes) {
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            int idx = depotsLivraison.indexOf(new YvsBaseDepots(depotChange.getId()));
            if (idx > -1) {
                selectContenu.setDepoLivraisonPrevu(depotsLivraison.get(idx));
            } else {
                selectContenu.setDepoLivraisonPrevu(new YvsBaseDepots(depotChange.getId()));
            }
            dao.update(selectContenu);
            if (docVente != null ? docVente.getContenus() != null : false) {
                idx = docVente.getContenus().indexOf(selectContenu);
                if (idx > -1) {
                    docVente.getContenus().set(idx, selectContenu);
                }
                idx = all_contenus.indexOf(selectContenu);
                if (idx > -1) {
                    all_contenus.set(idx, selectContenu);
                }
            }
            if (succes) {
                succes();
            }
        }
        return selectContenu;
    }

    public void chooseDepotContenu() {
        double stock = 0;
        if (selectContenu != null) {
            Date date = selectDoc.getEnteteDoc().getDateEntete();
            if (selectDoc.getDateLivraisonPrevu() != null) {
                date = selectDoc.getDateLivraisonPrevu();
            }
            stock = dao.stocks(selectContenu.getArticle().getId(), 0, depotChange.getId(), 0, 0, date, selectContenu.getConditionnement().getId(), (selectContenu.getLot() != null ? selectContenu.getLot().getId() : 0));
        }
        depotChange.setStock(stock);
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

    public void loadLivraisonByClient(YvsComContenuDocVente y) {
        loadLivraisonByClient(y, new Date());
    }

    public void loadLivraisonByClient(YvsComContenuDocVente y, Date date) {
        if (y != null ? y.getId() > 0 : false) {
            dateLivraison = date;
            y.getDocVente().setDateLivraisonPrevu(date);
            beginLivraison(y, false);
            YvsBaseDepots d = null;
            if (y.getDepoLivraisonPrevu() != null ? y.getDepoLivraisonPrevu().getId() > 0 : false) {
                d = y.getDepoLivraisonPrevu();
            } else if (y.getDocVente().getDepotLivrer() != null ? y.getDocVente().getDepotLivrer().getId() > 0 : false) {
                getWarningMessage("Cette ligne n'est pas de dépôt de livraison personnalisé");
                d = y.getDocVente().getDepotLivrer();
            }
            if (d != null ? d.getId() > 0 : false) {
                champ = new String[]{"depot", "client"};
                val = new Object[]{d, new YvsComClient(docVente.getClient().getId())};
                nameQueri = "YvsComDocVentes.findByClientDepotInvalid";
                livraisons = dao.loadNameQueries(nameQueri, champ, val);
                if (livraisons != null ? !livraisons.isEmpty() : false) {
                    insertInLivraison = true;
                    openDialog("dlgOpenLivraison");
                } else {
                    getWarningMessage("Ce client n'a pas de bon de livraison invalide pour le dépôt '" + d.getDesignation() + "'");
                    generedLivraison(y, date);
                    update("form_defined_contenu_blv");
                }
            } else {
                getErrorMessage("Il n y'a pas de dépôt de livraison");
            }
        }
    }

    public void generedLivraison(YvsComContenuDocVente y) {
        generedLivraison(y, new Date());
    }

    public void generedLivraison(YvsComContenuDocVente y, Date date) {
        if (y != null ? y.getId() > 0 : false) {
            dateLivraison = date;
            y.getDocVente().setDateLivraisonPrevu(date);
            beginLivraison(y, true);
            Depots d = null;
            if (y.getDepoLivraisonPrevu() != null ? y.getDepoLivraisonPrevu().getId() > 0 : false) {
                d = new Depots(y.getDepoLivraisonPrevu().getId(), y.getDepoLivraisonPrevu().getDesignation());
            } else if (y.getDocVente().getDepotLivrer() != null ? y.getDocVente().getDepotLivrer().getId() > 0 : false) {
                getWarningMessage("Cette ligne n'a pas de dépôt de livraison personnalisé");
                d = new Depots(y.getDocVente().getDepotLivrer().getId(), y.getDocVente().getDepotLivrer().getDesignation());
            }
            if (d != null ? d.getId() > 0 : false) {
                depotContenu = d;
                insertInLivraison = false;
                openDialog("dlgDefinedContenu");
            } else {
                getErrorMessage("Il n y'a pas de dépôt de livraison");
            }
        }
    }

    public void loadOnViewLivraison(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            livraison = (YvsComDocVentes) ev.getObject();
        }
    }

    public void unLoadOnViewLivraison(SelectEvent ev) {
        livraison = new YvsComDocVentes();
    }

    boolean insertInLivraison = true;

    public void insertInLivraison() {
        if (!autoriser("fv_livrer")) {
            openNotAcces();
            return;
        }
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            if (quantite_livree > 0) {
                if (quantite_livree <= selectContenu.getQteRestant()) {
                    if (insertInLivraison) {
                        if (livraison != null ? livraison.getId() > 0 : false) {
                            YvsComContenuDocVente y = new YvsComContenuDocVente(null, selectContenu);
                            y.setQuantite(quantite_livree);
                            y.setDocVente(livraison);
                            y.setParent(selectContenu);
                            y.setStatut(Constantes.ETAT_EDITABLE);
                            y.setAuthor(currentUser);
                            y.setPrixTotal(prixTotal(y));
                            y.setId(null);
                            dao.save(y);
                        }
                    } else {
                        selectContenu.setDepoLivraisonPrevu(new YvsBaseDepots(depotContenu.getId()));
                        if (selectContenu.getDepoLivraisonPrevu() != null ? selectContenu.getDepoLivraisonPrevu().getId() < 1 : true) {
                            getErrorMessage("Génération impossible... car dépôt absent");
                            return;
                        }
                        if (!verifyOperation(new Depots(selectContenu.getDepoLivraisonPrevu().getId()), Constantes.SORTIE, Constantes.VENTE, false)) {
                            return;
                        }
                        String num = genererReference(Constantes.TYPE_BLV_NAME, dateLivraison, selectContenu.getDepoLivraisonPrevu().getId(), Constantes.DEPOT);
                        if (num != null ? num.trim().length() < 1 : true) {
                            return;
                        }
                        String result = controleStock(selectContenu.getArticle().getId(), selectContenu.getConditionnement().getId(), selectContenu.getDepoLivraisonPrevu().getId(), 0L, quantite_livree, 0, "INSERT", "S", dateLivraison, (selectContenu.getLot() != null ? selectContenu.getLot().getId() : 0));
                        if (result != null) {
                            getErrorMessage("Impossible de valider ce document", "la ligne d'article " + selectContenu.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + result);
                            return;
                        }
                        champ = new String[]{"article", "depot"};
                        val = new Object[]{selectContenu.getArticle(), selectContenu.getDepoLivraisonPrevu()};
                        YvsBaseArticleDepot a = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                        if (a != null ? a.getId() < 1 : true) {
                            getErrorMessage("Impossible d'effectuer cette action... Car le depot " + selectContenu.getDepoLivraisonPrevu().getDesignation() + " ne possède pas l'article " + selectContenu.getArticle().getDesignation());
                            return;
                        }

                        if (selectDoc.getEnteteDoc() != null ? selectDoc.getEnteteDoc().getId() < 1 : true) {
                            return;
                        }

                        YvsComDocVentes e = new YvsComDocVentes(null, selectDoc);;
                        e.setEnteteDoc(selectDoc.getEnteteDoc());
                        e.setDateSave(new Date());
                        e.setAuthor(currentUser);
                        e.setTypeDoc(Constantes.TYPE_BLV);
                        e.setNumDoc(num);
                        e.setNumPiece("BL N° " + selectDoc.getNumDoc());
                        e.setDepotLivrer(selectContenu.getDepoLivraisonPrevu());
                        if (selectDoc.getTrancheLivrer() != null ? selectDoc.getTrancheLivrer().getId() > 0 : false) {
                            e.setTrancheLivrer(selectDoc.getTrancheLivrer());
                        }
                        e.setLivreur(currentUser.getUsers());
                        e.setDateLivraison(dateLivraison);
                        e.setDateLivraisonPrevu(selectDoc.getDateLivraisonPrevu());
                        e.setDocumentLie(new YvsComDocVentes(selectDoc.getId()));
                        e.setHeureDoc(new Date());
                        e.setStatut(Constantes.ETAT_EDITABLE);
                        e.setStatutLivre(Constantes.ETAT_LIVRE);
                        e.setStatutRegle(Constantes.ETAT_ATTENTE);
                        e.setValiderBy(currentUser.getUsers());
                        e.setDateValider(selectDoc.getDateLivraisonPrevu());
                        e.setDescription("Livraison de la facture N° " + selectDoc.getNumDoc() + " le " + ldf.format(new Date()) + " à " + time.format(new Date()));
                        e.setId(null);
                        e = (YvsComDocVentes) dao.save1(e);

                        YvsComContenuDocVente y = new YvsComContenuDocVente(null, selectContenu);
                        y.setQuantite(quantite_livree);
                        y.setDocVente(e);
                        y.setParent(selectContenu);
                        y.setStatut(Constantes.ETAT_VALIDE);
                        y.setAuthor(currentUser);
                        y.setPrixTotal(prixTotal(y));
                        y.setId(null);
                        dao.save(y);

                        ManagedFactureVente w = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
                        if (w != null) {
                            int idx = w.getDocVente().getDocuments().indexOf(e);
                            if (idx < 0) {
                                w.getDocVente().getDocuments().add(e);
                            }
                            update("tabview_facture_vente");
                        }
                    }
                    if (selectContenu.getQteRestant() <= quantite_livree) {
                        all_contenus.remove(selectContenu);
                    }
                    update("data_contenu_blv");
                    succes();
                } else {
                    getErrorMessage("Vous ne pouvez pas entrer une quantitée supérieure au reste à livrer");
                }
            } else {
                getErrorMessage("Vous devez precisez la quantitée");
            }
        }
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
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
                update("data-contenu_blv_require_lot");
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
                update("data-contenu_blv_require_lot");
                return;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onBlurQuantiteeLot", ex);
        }
    }

    public void onBlurQuantiteeLot(YvsComContenuDocVente contenu, YvsComLotReception y) {
        try {
            double quantite = y.getQuantitee();
            if (quantite > y.getStock()) {
                y.setQuantitee(0);
                getErrorMessage("La quantitée entrée ne peut pas dépasser le stock total du lot");
                update("blog-contenu_blv_require_lot");
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
                update("blog-contenu_blv_require_lot");
                return;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onBlurQuantiteeLot", ex);
        }
    }

    public void displayContent(YvsComDocVentes y) {
        y.setContenus(dao.loadNameQueries("YvsComContenuDocVente.findByDocVente", new String[]{"docVente"}, new Object[]{y}));
        for (YvsComContenuDocVente c : y.getContenus()) {
            y.setMontantTaxe(y.getMontantTaxe() + c.getTaxe());
            y.setMontantRistourne(y.getMontantRistourne() + c.getRistourne());
            y.setMontantTTC(y.getMontantTTC() + c.getPrixTotal());
        }
        update("dt_row_ex_" + y.getId());
    }

}
