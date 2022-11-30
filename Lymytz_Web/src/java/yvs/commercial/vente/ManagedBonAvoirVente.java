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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.UtilCompta;
import yvs.base.produits.Articles;
import yvs.base.tiers.Tiers;
import yvs.users.Users;
import yvs.commercial.UtilCom;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Util;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.depot.ManagedPointVente;
import yvs.commercial.stock.CoutSupDoc;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.caisse.ManagedReglementVente;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBasePointVenteDepot;
import yvs.entity.commercial.YvsComCommercialPoint;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.param.YvsAgences;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsers;
import yvs.grh.UtilGrh;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.entrepot.Depots;
import yvs.production.UtilProd;
import yvs.users.ManagedUser;
import static yvs.util.Managed.ldf;
import static yvs.util.Managed.time;
import yvs.util.ParametreRequete;
import yvs.util.Utilitaire;
import yvs.util.enume.Nombre;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedBonAvoirVente extends ManagedCommercial<DocVente, YvsComDocVentes> implements Serializable {

    private DocVente docVente = new DocVente();
    private DocVente docSelect = new DocVente();
    private DocVente facture = new DocVente();
    private List<YvsComDocVentes> documents;
    private List<YvsUsers> vendeurs;
    private YvsComDocVentes selectDoc;
    private YvsComEnteteDocVente selectEntete;

    private List<YvsComContenuDocVente> contenus, contenus_fv, selectContenus;
    private YvsComContenuDocVente selectContenu;
    private ContenuDocVente contenu = new ContenuDocVente();
    private ContenuDocVente contenu_fv = new ContenuDocVente();

    private List<YvsBasePointVenteDepot> depots_retour;
    private List<YvsGrhTrancheHoraire> tranches_retour;

    private List<YvsUsers> caissiers;
    private YvsComptaCaissePieceVente selectReglement;
    private PieceTresorerie reglement = new PieceTresorerie();
    private boolean newMensualite, memoriserDeleteContenu = false;

    private String tabIds, tabIds_mensualite, tabIds_article, type = Constantes.TYPE_FAV;
    private boolean existFacture, update, selectArt;
    private boolean recalculTaxe;

    //Parametre recherche
    private boolean date = false;
    private Date dateDebut = new Date(), dateFin = new Date(), dateGenerer = new Date();
    private String statut = null, numFacture, typeGenerer;
    private long livreur_, trancheSearch, depotSearch;

    public ManagedBonAvoirVente() {
        documents = new ArrayList<>();
        contenus = new ArrayList<>();
        contenus_fv = new ArrayList<>();
        tranches_retour = new ArrayList<>();
        depots_retour = new ArrayList<>();
        caissiers = new ArrayList<>();
        vendeurs = new ArrayList<>();
        selectContenus = new ArrayList<>();
    }

    public Date getDateGenerer() {
        return dateGenerer;
    }

    public List<YvsComContenuDocVente> getSelectContenus() {
        return selectContenus;
    }

    public void setSelectContenus(List<YvsComContenuDocVente> selectContenus) {
        this.selectContenus = selectContenus;
    }

    public boolean isMemoriserDeleteContenu() {
        return memoriserDeleteContenu;
    }

    public void setMemoriserDeleteContenu(boolean memoriserDeleteContenu) {
        this.memoriserDeleteContenu = memoriserDeleteContenu;
    }

    public void setDateGenerer(Date dateGenerer) {
        this.dateGenerer = dateGenerer;
    }

    public String getTypeGenerer() {
        return typeGenerer;
    }

    public void setTypeGenerer(String typeGenerer) {
        this.typeGenerer = typeGenerer;
    }

    public List<YvsUsers> getVendeurs() {
        return vendeurs;
    }

    public void setVendeurs(List<YvsUsers> vendeurs) {
        this.vendeurs = vendeurs;
    }

    public List<YvsUsers> getCaissiers() {
        return caissiers;
    }

    public void setCaissiers(List<YvsUsers> caissiers) {
        this.caissiers = caissiers;
    }

    public PieceTresorerie getReglement() {
        return reglement;
    }

    public void setReglement(PieceTresorerie reglement) {
        this.reglement = reglement;
    }

    public boolean isNewMensualite() {
        return newMensualite;
    }

    public void setNewMensualite(boolean newMensualite) {
        this.newMensualite = newMensualite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public YvsComEnteteDocVente getSelectEntete() {
        return selectEntete;
    }

    public void setSelectEntete(YvsComEnteteDocVente selectEntete) {
        this.selectEntete = selectEntete;
    }

    public YvsComptaCaissePieceVente getSelectReglement() {
        return selectReglement;
    }

    public void setSelectReglement(YvsComptaCaissePieceVente selectReglement) {
        this.selectReglement = selectReglement;
    }

    public String getTabIds_mensualite() {
        return tabIds_mensualite;
    }

    public void setTabIds_mensualite(String tabIds_mensualite) {
        this.tabIds_mensualite = tabIds_mensualite;
    }

    public List<YvsBasePointVenteDepot> getDepots_retour() {
        return depots_retour;
    }

    public void setDepots_retour(List<YvsBasePointVenteDepot> depots_retour) {
        this.depots_retour = depots_retour;
    }

    public List<YvsGrhTrancheHoraire> getTranches_retour() {
        return tranches_retour;
    }

    public void setTranches_retour(List<YvsGrhTrancheHoraire> tranches_retour) {
        this.tranches_retour = tranches_retour;
    }

    public ContenuDocVente getContenu_fv() {
        return contenu_fv;
    }

    public void setContenu_fv(ContenuDocVente contenu_fv) {
        this.contenu_fv = contenu_fv;
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

    public List<YvsComContenuDocVente> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocVente> contenus) {
        this.contenus = contenus;
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
        if (!docVente.getTypeDoc().equals(type)) {
            resetFiche();
        }
        docVente.setTypeDoc(type);
        loadAll();
    }

    public void initView() {
        loadInfosWarning(false);
        if (facture != null ? facture.getId() > 0 : false) {
//            loadContenuFacture(new YvsComDocVentes(facture.getId()), false);
        }
        if (((docVente != null) ? docVente.getClient().getId() < 1 : true)) {
            docVente = new DocVente();
            if (docVente.getDocumentLie() == null) {
                docVente.setDocumentLie(new DocVente());
            }
            numSearch_ = "";
            docVente.setEnteteDoc(new EnteteDocVente());
            docVente.setTypeDoc(type);
        }
        if (docVente.getTypeDoc() != null ? docVente.getTypeDoc().trim().length() < 1 : true) {
            docVente.setTypeDoc(type);
        }
        if (currentParamVente == null) {
            currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        update("txt_indice_num_search");
    }

    public void loadAllFacture(boolean avance, boolean init) {
        //choisir les documents à charger
        ParametreRequete p;
        if (type.equals(Constantes.TYPE_BRV)) {
            switch (buildDocByDroit(Constantes.TYPE_BLV)) {
                case 1:  //charge tous les documents de la société
                    p = new ParametreRequete("y.depotLivrer.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                    paginator.addParam(p);
                    break;
                case 2: //charge tous les documents de l'agence
                    p = new ParametreRequete("y.depotLivrer.agence", "agence", currentAgence, "=", "AND");
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
            paginator.addParam(new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_BRV, "=", "AND"));
        } else {
            switch (buildDocByDroit(Constantes.TYPE_FV)) {
                case 1:  //charge tous les documents de la société
                    p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                    paginator.addParam(p);
                    break;
                case 2: //charge tous les documents de l'agence
                    p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence", "agence", currentAgence, "=", "AND");
                    paginator.addParam(p);
                    break;
                case 3: //charge tous les document des points de vente où l'utilisateurs est responsable
                    //cherche les points de vente de l'utilisateur
                    List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"}, new Object[]{currentUser.getUsers(), (Utilitaire.getIniTializeDate(new Date()).getTime()), Constantes.getPreviewDate(new Date())});
                    if (ids.isEmpty()) {
                        ids.add(-1L);
                    }
                    p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                    paginator.addParam(p);
                    break;
                default:    //charge les document de l'utilisateur connecté dans les restriction de paramDate données
                    p = new ParametreRequete("y.enteteDoc.creneau.users ", "users", currentUser.getUsers(), "=", "AND");
                    paginator.addParam(p);
                    break;
            }
            paginator.addParam(new ParametreRequete("y.typeDoc", "typeDoc", Constantes.TYPE_FAV, "=", "AND"));
        }
        documents = paginator.executeDynamicQuery("YvsComDocVentes", "y.enteteDoc.dateEntete DESC, y.numDoc DESC", avance, init, (int) imax, dao);
        if (type.equals(Constantes.TYPE_FAV)) {
            update("data_avoir_vente");
        } else {
            update("data_retour_vente");
        }
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

    public void loadContenuFacture(YvsComDocVentes y, boolean facture) {
        if (y != null ? y.getId() > 0 : false) {
            long id = y.getId();
            String state = y.getStatut();
            String state_livre = y.getStatutLivre();
            if (facture) {
                String num = y.getNumDoc();
                YvsComDocVentes d = new YvsComDocVentes(y);
                d.setHeureDoc(new Date());
                d.setDateLivraison(new Date());
                d.setDocumentLie(new YvsComDocVentes(id, num, state));
                d.getDocumentLie().setStatutLivre(state_livre);
                d.setTypeDoc(docVente.getTypeDoc());
                d.setNumDoc(null);
                d.setNew_(true);
                d.setStatut(Constantes.ETAT_EDITABLE);
                d.setDateSave(new Date());
                if (docVente.getTypeDoc().equals(Constantes.TYPE_BRV)) {
                    d.setNumPiece("BRV N° " + num);
                    d.setDescription("Bon Retour de la facture N° " + num + " le " + ldf.format(new Date()) + " à " + time.format(new Date()));
                } else {
                    d.setNumPiece("FAV N° " + num);
                    d.setDescription("Facture Avoir de la facture N° " + num + " le " + ldf.format(new Date()) + " à " + time.format(new Date()));
                }
                d.setId((long) -1);
                d.getContenus().clear();
                cloneObject(docVente, UtilCom.buildBeanDocVente(d));
                docVente.getEnteteDoc().setTranche(docVente.getEnteteDoc().getTranchePoint());
                docVente.setUpdate(false);
                setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
                if (type.equals(Constantes.TYPE_FAV)) {
                }
            }
            contenus_fv = loadContenus(new YvsComDocVentes(id));
            if (type.equals(Constantes.TYPE_FAV)) {
                update("infos_document_avoir_vente");
                update("data_contenu_fv_fav");
            } else {
                update("infos_document_retour_vente");
                update("data_contenu_fv_brv");
            }
        }
    }

    public void loadDepotByPoint(YvsBasePointVente y) {
        if (!autoriser("fv_livrer_in_all_depot")) {
            champ = new String[]{"pointVente"};
            val = new Object[]{y};
            nameQueri = "YvsBasePointVenteDepot.findByPointVente";
        } else {
            champ = new String[]{"agence"};
            val = new Object[]{currentAgence};
            nameQueri = "YvsBasePointVenteDepot.findDepotByAgence";
        }
        depots_retour = dao.loadNameQueries(nameQueri, champ, val);
    }

    public void loadAllTrancheLivraison(YvsBaseDepots depot, Date date) {
        tranches_retour = loadTranche(depot, date);
        update("select_tranche_retour_vente");
    }

    public void init(boolean next) {
        loadAllFacture(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAllFacture(true, true);
    }

    public PieceTresorerie recopieViewPiece() {
        if (reglement != null) {
            if (reglement.getMode() != null ? reglement.getMode().getId() > 0 : false) {
                ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (m != null) {
                    int idx = m.getModes().indexOf(new YvsBaseModeReglement((long) reglement.getMode().getId()));
                    if (idx > -1) {
                        YvsBaseModeReglement o = m.getModes().get(idx);
                        reglement.setMode(new ModeDeReglement(o.getId().intValue(), o.getDesignation(), o.getTypeReglement()));
                    }
                }
            }
            if (reglement.getCaisse() != null ? reglement.getCaisse().getId() > 0 : false) {
                ManagedCaisses m = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                if (m != null) {
                    int idx = m.getCaisses().indexOf(new YvsBaseCaisse(reglement.getCaisse().getId()));
                    if (idx > -1) {
                        YvsBaseCaisse o = m.getCaisses().get(idx);
                        reglement.setCaisse(new Caisses(o.getId(), o.getCode(), o.getIntitule()));
                    }
                }
            }
            if (reglement.getCaissier() != null ? reglement.getCaissier().getId() > 0 : false) {
                int idx = caissiers.indexOf(new YvsUsers(reglement.getCaissier().getId()));
                if (idx > -1) {
                    YvsUsers o = caissiers.get(idx);
                    reglement.setCaissier(new Users(o.getId(), o.getCodeUsers(), o.getNomUsers()));
                }
            }
            reglement.setMouvement(Constantes.MOUV_DEBIT);
            reglement.setUpdate(true);
            reglement.setDateUpdate(new Date());
            reglement.setDatePaiement(reglement.getDatePaiementPrevu());
        }
        return reglement;
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

    public YvsComEnteteDocVente saveNewEntete(DocVente docVente) {
        YvsComEnteteDocVente y = null;
        ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (w != null) {
            try {
                y = w.saveNewEntete(docVente.getEnteteDoc(), docVente, true);
                if (!docVente.isFromFacture()) {
                    selectEntete = y;
                }
                if (selectEntete != null ? (selectEntete.getId() > 0 && docVente.getEnteteDoc().getId() < 1) : false) {
                    docVente.setEnteteDoc(UtilCom.buildBeanEnteteDocVente(selectEntete));
                    docVente.getEnteteDoc().setTranche(docVente.getEnteteDoc().getTranchePoint());
                }
                if (selectEntete != null ? selectEntete.getId() > 0 : false) {
                    docVente.getEnteteDoc().setId(y.getId());
                }
            } catch (Exception ex) {
                getErrorMessage("Création du header impossible !");
                getException("Lymytz Error  >>> ", ex);
                return null;
            }
        }
        return y;
    }

    @Override
    public boolean controleFiche(DocVente bean) {
        if (!_controleFiche_(bean)) {
            return false;
        }
        selectEntete = saveNewEntete(bean);
        if (selectEntete != null ? selectEntete.getId() < 1 : true) {
            return false;
        }
        if (bean.isFromFacture() ? !bean.getDocumentLie().getStatut().equals(Constantes.ETAT_VALIDE) : false) {
            getWarningMessage("La facture n'est pas validé");
        }
        if ((bean.getEnteteDoc() != null) ? bean.getEnteteDoc().getId() < 1 : true) {
            getErrorMessage("Vous ne disposé pas d'une entête");
            return false;
        }

        //modifie le numéro de document si la date change   
        if (type.equals(Constantes.TYPE_BRV)) {
            if ((bean.getDepot() != null) ? bean.getDepot().getId() < 1 : true) {
                getErrorMessage("Vous devez preciser le dépot");
                return false;
            }
            if ((bean.getTranche() != null) ? bean.getTranche().getId() < 1 : true) {
                getErrorMessage("Vous devez preciser la tranche");
                return false;
            }
            if ((selectDoc != null ? (selectDoc.getId() > 0 ? !bean.getDateLivraison().equals(selectDoc.getDateLivraison()) : false) : false)
                    || (bean.getNumDoc() == null || bean.getNumDoc().trim().length() < 1)) {
                String ref = genererReference(Constantes.TYPE_BRV_NAME, bean.getDateLivraison(), bean.getDepot().getId(), Constantes.DEPOT);
                if (ref == null || ref.trim().equals("")) {
                    return false;
                }
                bean.setNumDoc(ref);
            }
        } else {
            if ((selectDoc != null ? (selectDoc.getId() > 0
                    ? ((selectEntete.getDateEntete() != null
                    && (selectDoc.getEnteteDoc() != null ? selectDoc.getEnteteDoc().getDateEntete() != null : false)))
                    ? !selectEntete.getDateEntete().equals(selectDoc.getEnteteDoc().getDateEntete()) : false : false) : false)
                    || (bean.getNumDoc() == null || bean.getNumDoc().trim().length() < 1)) {
                String ref = genererReference(Constantes.TYPE_FAV_NAME, selectEntete.getDateEntete(), bean.getEnteteDoc().getPoint().getId(), Constantes.POINTVENTE);
                if (ref == null || ref.trim().equals("")) {
                    return false;
                }
                bean.setNumDoc(ref);
            }
        }
        return true;
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
            getErrorMessage("Ce document a été verouillé !");
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
        if (bean.getDocVente() != null ? !bean.getDocVente().isUpdate() : true) {
            if (!_saveNew()) {
                return false;
            }
            bean.setDocVente(docVente);
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if (docVente.getTypeDoc().equals(Constantes.TYPE_BRV) ? bean.getArticle().isRequiereLot() && (bean.getLot() != null ? bean.getLot().getId() < 1 : true) : false) {
            getErrorMessage("Un numéro de lot est requis pour cet article dans ce dépôt");
            return false;
        }
        if (docVente.getTypeDoc().equals(Constantes.TYPE_FAV)) {
            if (bean.getPrix() == 0 && bean.getRemise() == 0 && bean.getTaxe() == 0) {
                getErrorMessage("L'avoir doit porter sur un élément comptable");
                return false;
            }
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
        facture = new DocVente();
        cloneObject(docVente, bean);
        docVente.setFromFacture(bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false);
        docVente.setDateLivraison((docVente.getDateLivraison() == null) ? new Date() : docVente.getDateLivraison());
        docVente.getEnteteDoc().setTranche(docVente.getEnteteDoc().getTranchePoint());
        setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
    }

    public void populateViewArticle(ContenuDocVente bean) {
        bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docVente.getDepot().getId(), 0, 0, docVente.getDateLivraison(), bean.getConditionnement().getId(), bean.getLot().getId()));
        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
        cloneObject(contenu, bean);
        if (bean.getParent() != null ? bean.getParent().getId() > 0 : false) {
            YvsComContenuDocVente c = (YvsComContenuDocVente) dao.loadOneByNameQueries("YvsComContenuDocVente.findById", new String[]{"id"}, new Object[]{bean.getParent().getId()});
            cloneObject(contenu_fv, UtilCom.buildBeanContenuDocVente(c));
            contenu.setQuantite_(c.getQuantite());
            contenu.setPrix_(c.getPrix());
            contenu.setRemise_(c.getRemise());
            contenu.setTaxe_(c.getTaxe());
            contenu.setPrixTotal_(c.getPrixTotal());
        }
        String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
        Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(bean.getArticle().getId(), 1), new Options(docVente.getDepot().getId(), 2)});
        contenu.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
        if (contenu.getArticle().isRequiereLot()) {
            champ = new String[]{"typeDoc", "parent", "conditionnement"};
            val = new Object[]{Constantes.TYPE_BLA, new YvsComDocVentes(docVente.getDocumentLie().getId()), new YvsBaseConditionnement(bean.getConditionnement().getId())};
            contenu.setLots(dao.loadNameQueries("YvsComContenuDocVente.findLotByArticleParent", champ, val));
        }
        selectArt = true;
    }

    @Override
    public void resetFiche() {
        docVente = new DocVente();
        docVente.setDocumentLie(new DocVente());
        docVente.setStatut(Constantes.ETAT_EDITABLE);
        docVente.setTypeDoc(type);

        facture = new DocVente();
        tabIds = "";
        update = false;
        contenus.clear();
        contenus_fv.clear();
        selectDoc = null;
        selectEntete = new YvsComEnteteDocVente();

        resetFicheArticle();
        if (type.equals(Constantes.TYPE_FAV)) {
            update("blog_form_avoir_vente");
        } else {
            update("blog_form_retour_vente");
        }
    }

    public void resetFicheArticle() {
        contenu = new ContenuDocVente();
        contenu.setArticle(new Articles());
        contenu.setDocVente(new DocVente());
        contenu_fv = new ContenuDocVente();
        selectArt = false;
        tabIds_article = "";
        selectContenu = null;
    }

    public void resetFicheArticle_() {
        resetFiche(contenu);
        contenu.setArticle(new Articles());
        contenu.setDocVente(new DocVente());
        tabIds_article = "";
    }

    public void resetFicheReglement(boolean save) {
        reglement = new PieceTresorerie();
        reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        tabIds_mensualite = "";
        if (!save) {
            newMensualite = false;
        }
        if (docVente != null) {
            if (docVente.getMontantResteApayer() > 0) {
                double m = docVente.getMontantResteApayer();
                for (YvsComptaCaissePieceVente r : docVente.getReglements()) {
                    m -= r.getMontant();
                }
                reglement.setMontant(m > 0 ? m : 0);
            }
        }
        selectReglement = new YvsComptaCaissePieceVente();
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
            docVente.setTypeDoc(type);
            if (controleFiche(docVente)) {
                selectDoc = buildDocVente(docVente);
                selectDoc.setEnteteDoc(selectEntete);
                if (!docVente.isUpdate()) {
                    selectDoc.setId(null);
                    selectDoc = (YvsComDocVentes) dao.save1(selectDoc);
                    docVente.setId(selectDoc.getId());
                    documents.add(0, selectDoc);
                } else {
                    dao.update(selectDoc);
                    documents.set(documents.indexOf(selectDoc), selectDoc);
                }
                //save contenu if not save
                saveContenu(contenus, selectDoc);
                setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
                docVente.setUpdate(true);
                if (type.equals(Constantes.TYPE_FAV)) {
                    update("data_avoir_vente");
                    update("form_entete_avoir_vente");
                } else {
                    update("data_retour_vente");
                    update("form_entete_retour_vente");
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

    private void saveContenu(List<YvsComContenuDocVente> contenus, YvsComDocVentes d) {
        for (YvsComContenuDocVente c : contenus) {
            if (c.getId() != null ? c.getId() <= 0 : false) {
                c.setDocVente(d);
                c.setDateSave(new Date());
                c.setDateUpdate(new Date());
                c = (YvsComContenuDocVente) dao.save1(c);
            }
        }
    }

    public void saveNewArticle() {
        try {
            contenu.setDocVente(docVente);
            if (controleFicheContenu(contenu)) {
                YvsComContenuDocVente en = UtilCom.buildContenuDocVente(contenu, currentUser);
                if (contenu.getId() < 1) {
                    en.setId(null);
                    en = (YvsComContenuDocVente) dao.save1(en);
                    saveAllTaxe(en, docVente, en.getDocVente(), docVente.getCategorieComptable().getId(), true);
                    contenu.setId(en.getId());
                } else {
                    dao.update(en);
                }
                int index = contenus.indexOf(en);
                if (index < 0) {
                    contenus.add(0, en);
                } else {
                    contenus.set(index, en);
                }
                resetFicheArticle();
                succes();
                if (type.equals(Constantes.TYPE_FAV)) {
                    update("data_avoir_vente");
                } else {
                    update("data_retour_vente");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Gescom Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveNewReglement(boolean deletePhase) {
        boolean update = reglement.getId() > 0;
        try {
            ManagedReglementVente m = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
            if (m != null) {
                PieceTresorerie bean = recopieViewPiece();
                if (bean.getId() > 0 ? (selectReglement != null ? selectReglement.getStatutPiece() == Constantes.STATUT_DOC_PAYER : false) : false) {
                    getErrorMessage("La pièce en cours est déjà payé !");
                    return;
                }
                if (!bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE) && !deletePhase) {
                    for (YvsComptaPhasePiece pp : reglement.getPhases()) {
                        if (pp.getPhaseOk()) {
                            openDialog("dlgConfirmChangeMode");
                            return;
                        }
                    }
                    if (!reglement.getPhases().isEmpty()) {
                        openDialog("dlgConfirmDeletePhase");
                        return;
                    }
                }
                YvsComptaCaissePieceVente piece = UtilCom.buildPieceVente(bean, currentUser);
                piece.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{bean.getCaisse().getId()}));
                if (bean.getId() < 1 ? (selectReglement != null ? !selectReglement.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) : true) : true) {
                    piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                }
                if (piece.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && !bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
                    piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    getWarningMessage("Seules les règlements en espèces peuvent être validé avec ce schéma !");
                }
                if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) && bean.getCaisse().getId() <= 0) {

                }
                if (bean.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                    if (currentParamVente != null ? !currentParamVente.getPaieWithoutValide() ? !docVente.getStatut().equals(Constantes.ETAT_VALIDE) : false : false) {
                        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                        getWarningMessage("La facture doit etre au préalable validée");
                    } else {
                        if (controleAccesCaisse(piece.getCaisse(), true)) {
                            piece.setValideBy(currentUser.getUsers());
                            if (piece.getCaissier() != null ? piece.getCaissier().getId() < 1 : true) {
                                piece.setCaissier(currentUser.getUsers());
                            }
                            piece.setDatePaiement(piece.getDatePaimentPrevu());
                            piece.setDateValide(new Date());
                        } else {
                            piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            getWarningMessage("Vous n'avez pas d'autorisation pour efectuer des règlements dans cette caisse");
                        }
                    }
                }
                piece.setVente(selectDoc);
                piece = m.createNewPieceCaisse(docVente, piece, deletePhase);
                if (piece != null ? piece.getId() > 0 : false) {
                    if (!update) {
                        docVente.getReglements().add(0, piece);
                    } else {
                        int idx = docVente.getReglements().indexOf(piece);
                        if (idx >= 0) {
                            docVente.getReglements().set(idx, piece);
                        } else {
                            docVente.getReglements().add(piece);
                        }
                    }
                    int idx = documents.indexOf(selectDoc);
                    if (idx >= 0) {
                        int idx1 = documents.get(idx).getReglements().indexOf(piece);
                        if (idx1 >= 0) {
                            documents.get(idx).getReglements().set(idx1, piece);
                        } else {
                            documents.get(idx).getReglements().add(0, piece);
                        }
                    }
                    if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) && reglement.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                        m.reglerPieceTresorerie(docVente, piece, "F", true);
                    } else {
                        succes();
                    }
                }
                selectDoc.setStatutRegle(docVente.getStatutRegle());
                selectDoc.setStatutLivre(docVente.getStatutLivre());
                resetFicheReglement(true);
            }
        } catch (Exception ex) {
            getErrorMessage(update ? "Modification" : "Insertion" + " Impossible !");
            getException("Lymytz Error...", ex);
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser(type.equals(Constantes.TYPE_BRV) ? "brv_delete_doc" : "fav_delete_doc")) {
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
                resetFiche();

                if (type.equals(Constantes.TYPE_FAV)) {
                    update("data_avoir_vente");
                } else {
                    update("data_retour_vente");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
        tabIds = "";
    }

    public void deleteBean_(YvsComDocVentes y) {
        selectDoc = y;
    }

    public boolean deleteBean_() {
        try {
            if (!autoriser(type.equals(Constantes.TYPE_BRV) ? "brv_delete_doc" : "fav_delete_doc")) {
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
                if (type.equals(Constantes.TYPE_FAV)) {
                    update("data_avoir_vente");
                } else {
                    update("data_retour_vente");
                }
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
                    YvsComContenuDocVente bean = contenus.get(contenus.indexOf(new YvsComContenuDocVente(id)));
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
                for (YvsComContenuDocVente c : selectContenus) {
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
                contenus.remove(selectContenu);
                if (contenu.getId() == selectContenu.getId()) {
                    resetFicheArticle();
                }
                succes();
                update("data_contenu_avoir_vente");
                update("chp_fav_net_a_payer");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanReglement_(YvsComptaCaissePieceVente y) {
        selectReglement = y;
    }

    public void deleteBeanReglement_() {
        try {
            if (selectReglement != null) {
                if (selectReglement.getId() > 0) {
                    dao.delete(selectReglement);
                }
                docVente.getReglements().remove(selectReglement);
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void onSelectObject(YvsComDocVentes y) {
        selectDoc = y;
        populateView(UtilCom.buildBeanDocVente(selectDoc));
        resetFicheArticle();
        contenus = loadContenus(y);
        if (!documents.contains(y)) {
            documents.add(0, y);
        }
        if (!vendeurs.contains(y.getEnteteDoc().getCreneau().getUsers())) {
            vendeurs.add(y.getEnteteDoc().getCreneau().getUsers());
        }
        if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getId() > 0 : false) {
            champ = new String[]{"id"};
            val = new Object[]{selectDoc.getDocumentLie().getId()};
            YvsComDocVentes d_ = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", champ, val);
            if (d_ != null ? d_.getId() > 0 : false) {
                facture = UtilCom.buildSimpleBeanDocVente(d_);
                loadContenuFacture(d_, false);
            }
            ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            if (m != null) {
                if (!m.getDocuments().subList(0, m.getSubLenght()).contains(selectDoc.getDocumentLie())) {
                    m.getDocuments().remove(selectDoc.getDocumentLie());
                    m.getDocuments().add(0, selectDoc.getDocumentLie());
                }
            }
        }
        if (y.getEnteteDoc() != null ? (y.getEnteteDoc().getCreneau() != null ? (y.getEnteteDoc().getCreneau().getCreneauPoint() != null ? y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null : false) : false) : false) {
            ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (w != null) {
                w.setEntityPoint(y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint());
            }
            loadDepotByPoint(y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint());
        }
        chooseDepotLivraison();
        if (type.equals(Constantes.TYPE_FAV)) {
            update("blog_form_avoir_vente");
            update("other_avoir_vente");
            update("data_contenu_fv_fav");
        } else {
            update("blog_form_retour_vente");
            update("other_retour_vente");
            update("data_contenu_fv_brv");
        }
    }

    @Override
    public void onSelectDistant(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                if (this.type.equals(Constantes.TYPE_BRV)) {
                    n.naviguationView("Bon Retour Vente", "modGescom", "smenFactureRetourVente", true);
                } else {
                    n.naviguationView("Factures Avoir Vente", "modCompta", "smenFactureAvoirVente", true);
                }
            }
        }
    }

    public void onSelectDistant(YvsComDocVentes y, String type) {
        this.type = type;
        onSelectDistant(y);;
    }

    public void onSelectObject(YvsComDocVentes y, String type) {
        this.type = type;
        onSelectObject(y);
    }

    public void onSelectDistantObject(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureVente s = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    private void loadCaissiers(YvsBaseCaisse y) {
        caissiers.clear();
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            caissiers = w.loadCaissiers(y);
        }
        if (y != null ? y.getId() > 0 : false) {
            if (reglement.getCaissier() != null ? reglement.getCaissier().getId() < 1 : true) {
                reglement.setCaissier(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
            }
            if ((reglement.getCaissier() != null ? reglement.getCaissier().getId() < 1 : true) && caissiers.contains(currentUser.getUsers())) {
                reglement.setCaissier(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
            }
        }
        update("tabview_facture_vente:chmp_caissier_reglement_fv");
    }

    public void chooseCaisses(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            // trouve les caisses parent d'une caisse données
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            long id = (long) ev.getNewValue();
            caissiers.clear();
            reglement.setCaissier(new Users());
            if (service != null && id > 0) {
                int idx = service.getCaisses().indexOf(new YvsBaseCaisse(id));
                if (idx > -1) {
                    YvsBaseCaisse y = service.getCaisses().get(idx);
                    reglement.setCaisse(UtilCompta.buildBeanCaisse(y));
                    loadCaissiers(y);
                }
            }
            update("tabview_facture_vente:chmp_caissier_reglement_fv");
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
        if (type.equals(Constantes.TYPE_FAV)) {
            update("blog_form_avoir_vente");
        } else {
            update("blog_form_retour_vente");
        }
    }

    public void loadOnViewDoc(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocVentes bean = (YvsComDocVentes) ev.getObject();
            onSelectFacture(bean);
        }
    }

    public void loadOnViewEntete(SelectEvent ev) {

    }

    public void loadOnViewMensualite(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectReglement = (YvsComptaCaissePieceVente) ev.getObject();
            reglement = UtilCom.buildBeanPieceVente(selectReglement);
            if (reglement.getMode() != null ? reglement.getMode().getId() < 1 : true) {
                reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
            }
            loadCaissiers(selectReglement.getCaisse());
            update("blog_form_mensualite_facture_vente");
        }
    }

    public void unLoadOnViewMensualite(UnselectEvent ev) {
        resetFicheReglement(false);
        update("blog_form_mensualite_facture_vente");
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

    public void loadOnViewArticle(SelectEvent ev) {
        selectArt = false;
        if (!docVente.getTypeDoc().equals(Constantes.TYPE_FAV)) {
            if (docVente.getDepot() != null ? docVente.getDepot().getId() <= 0 : true) {
                getErrorMessage("Vous devez preciser le dépôt de livraison");
                resetFicheArticle();
                return;
            }
        }
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocVente bean = (YvsComContenuDocVente) ev.getObject();
            cloneObject(contenu_fv, UtilCom.buildBeanContenuDocVente(bean));
            cloneObject(contenu, contenu_fv);
            contenu.setId(-1);
//            if (!contenu.isUpdate()) {
//                cloneObject(contenu, contenu_fv);
//            } else {
//                cloneObject(contenu.getArticle(), contenu_fv.getArticle());
//            }
            cloneObject(contenu.getParent(), contenu_fv);
            contenu.setQuantite(bean.getQuantite());
            contenu.setPrix(bean.getPrix());
            contenu.setRemise(0);
            contenu.setTaxe(bean.getTaxe());
            recalculTaxe = true;
            contenu.setPrixTotal(bean.getPrixTotal());
            if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                contenu.getArticle().setStock(dao.stocks(contenu.getArticle().getId(), 0, docVente.getDepot().getId(), 0, 0, docVente.getDateLivraison(), contenu.getConditionnement().getId(), contenu.getLot().getId()));

                String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                Boolean requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(bean.getArticle().getId(), 1), new Options(docVente.getDepot().getId(), 2)});
                contenu.getArticle().setRequiereLot(requiere_lot != null ? requiere_lot : false);
                if (contenu.getArticle().isRequiereLot()) {
                    champ = new String[]{"typeDoc", "parent", "conditionnement"};
                    val = new Object[]{Constantes.TYPE_BLV, new YvsComDocVentes(docVente.getDocumentLie().getId()), bean.getConditionnement()};
                    contenu.setLots(dao.loadNameQueries("YvsComContenuDocVente.findLotByArticleParent", champ, val));
                }
            } else {
                contenu.getArticle().setStock(dao.stocks(contenu.getArticle().getId(), 0, 0, currentAgence.getId(), 0, docVente.getDateLivraison(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
            }
            contenu.getArticle().setPuv(dao.getPuv(contenu.getArticle().getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), docVente.getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getDateLivraison(), contenu.getConditionnement().getId()));
            selectArt = true;
            contenu.setUpdate(false);
            contenu.setDateContenu(new Date());
            contenu.setParent(new ContenuDocVente(bean.getId()));
            if (type.equals(Constantes.TYPE_FAV)) {
                update("blog_form_article_avoir_vente");
            } else {
                update("blog_form_article_retour_vente");
            }
        }
        if (type.equals(Constantes.TYPE_FAV)) {
            update("desc_contenu_avoir_vente");
        } else {
            update("desc_contenu_retour_vente");
        }
    }

    public void unLoadOnViewArticle(UnselectEvent ev) {
        if (contenu.getId() <= 0) {
            resetFicheArticle();
        } else {
            YvsComContenuDocVente c = contenus.get(contenus.indexOf(new YvsComContenuDocVente(contenu.getId())));
            cloneObject(contenu, UtilCom.buildBeanContenuDocVente(c));
        }
        if (type.equals(Constantes.TYPE_FAV)) {
            update("blog_form_article_avoir_vente");
        } else {
            update("blog_form_article_retour_vente");
        }
    }

    public void loadOnViewArticles(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean));
        }
    }

    public void loadOnViewClient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient y = (YvsComClient) ev.getObject();
            chooseClient(UtilCom.buildBeanClient(y));
        }
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
                    if (type.equals(Constantes.TYPE_FAV)) {
                        update("data_fv_retour_fav");
                    } else {
                        update("data_fv_retour_brv");
                    }
                } else {
                    chooseDocVente(y);
                }
                facture.setError(false);
            }
        }
    }

    public void actualiseAvoir() {
        cloneObject(docVente.getClient(), facture.getClient());
        cloneObject(docVente.getTiers(), facture.getTiers());
        cloneObject(docVente.getCategorieComptable(), facture.getCategorieComptable());
    }

    public void searchClient() {
        String num = docVente.getClient().getCodeClient();
        docVente.getClient().setId(0);
        docVente.getClient().setError(true);
        docVente.getClient().setTiers(new Tiers());
        docVente.setNomClient("");
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            Client y = m.searchClient(num, true);
            if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                if (m.getClients().size() > 1) {
                    if (type.equals(Constantes.TYPE_FAV)) {
                        update("data_client_fav");
                    } else {
                        update("data_client_brv");
                    }
                } else {
                    chooseClient(y);
                }
                docVente.getClient().setError(false);
            }
        }
    }

    public void searchArticle() {
        String num = contenu.getArticle().getRefArt();
        contenu.getArticle().setDesignation("");
        contenu.getArticle().setError(true);
        contenu.getArticle().setId(0);
        selectArt = false;
        ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (m != null) {
            Articles y = m.searchArticleActifByPoint(num, true);
            if (m.getArticles() != null ? !m.getArticles().isEmpty() : false) {
                if (m.getArticles().size() > 1) {
                    if (type.equals(Constantes.TYPE_FAV)) {
                        update("data_articles_fav");
                    } else {
                        update("data_articles_brv");
                    }
                } else {
                    chooseArticle(y);
                }
                contenu.getArticle().setError(false);
            }
        }
    }

    public void chooseClient(Client d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(docVente.getClient(), d);
            if (d.getCategorieComptable() != null) {
                cloneObject(docVente.getCategorieComptable(), d.getCategorieComptable());
                update("select_categorie_comptable_brv");
            }
            if (docVente.getClient() != null ? docVente.getClient().getModel() != null : false) {
                docVente.setModeReglement(docVente.getClient().getModel());
            } else {
                YvsBaseCategorieClient c = currentCategorieClient(d);
                if (c != null ? c.getModel() != null : false) {
                    docVente.setModeReglement(UtilCom.buildBeanModelReglement(c.getModel()));
                } else {
                    getWarningMessage("Ce client n'a pas de catégorie client!");
                }
            }
            docVente.getClient().setNom(d.getNom_prenom());
            docVente.setNomClient(d.getNom_prenom());
            docVente.setTelephone(d.getTiers().getTelephone());
            if (docVente.getVille() != null ? docVente.getVille().getId() < 1 : true) {
                if (d.getTiers() != null ? d.getTiers().getId() > 0 : false) {
                    if (d.getTiers().getVille() != null ? d.getTiers().getVille().getId() > 0 : false) {
                        cloneObject(docVente.getVille(), d.getTiers().getVille());
                    }
                }
            }
            if (docVente.getAdresse() != null ? docVente.getAdresse().getId() < 1 : true) {
                if (d.getTiers() != null ? d.getTiers().getId() > 0 : false) {
                    if (d.getTiers().getSecteur() != null ? d.getTiers().getSecteur().getId() > 0 : false) {
                        cloneObject(docVente.getAdresse(), d.getTiers().getSecteur());
                    }
                }
            }
            //choisir le code tiers à utiliser
            if (d.isSuiviComptable()) {
                docVente.setTiers(d);
            } else {
                //récupère le code tiers du commerciale ayant le point de vente
                if (docVente.getEnteteDoc().getPoint().getId() > 0) {
                    ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                    if (service != null) {
                        int idx = service.getPointsvente().indexOf(new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId()));
                        if (idx >= 0) {
                            List<YvsComCommercialPoint> l = service.getPointsvente().get(idx).getCommerciaux();
                            if (!l.isEmpty()) {
                                if (l.get(0).getCommercial().getTiers() != null) {
                                    if (l.get(0).getCommercial().getTiers().getClients() != null ? !l.get(0).getCommercial().getTiers().getClients().isEmpty() : false) {
                                        docVente.setTiers(UtilCom.buildBeanClient(l.get(0).getCommercial().getTiers().getClients().get(0)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            update("select_model_brv");
            update("txt_client_retour_vente");
        }
    }

    public void chooseArticle(Articles art) {
        if ((art != null) ? art.getId() > 0 : false) {
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(art.getId())});
            art.setConditionnements(unites != null ? unites : new ArrayList<YvsBaseConditionnement>());
            if ((contenu.getConditionnement() != null) ? contenu.getConditionnement().getId() <= 0 : true) {
                if (art.getConditionnements() != null ? !art.getConditionnements().isEmpty() : false) {
                    YvsBaseConditionnement cd = art.getConditionnements().get(0);
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(cd));
                    if (cd.getPrixMin() != 0) {
                        contenu.getArticle().setPuvMin(cd.getPrixMin());
                    }
                }
            }
            loadInfosAfterChooseArticle(contenu, art);
            cloneObject(contenu.getArticle(), art);
            contenu.setPrixMin(contenu.getArticle().getPuvMin());
            if (contenu.getPrix() < 1) {
                contenu.getArticle().setChangePrix(true);
            }
            if (contenu.getQuantite() > 0 && contenu.getConditionnement().getId() > 0) {
                onPrixBlur();
            }
            selectArt = true;
        } else {
            resetFicheArticle();
        }
        if (type.equals(Constantes.TYPE_FAV)) {
            update("tabView_fav:form_contenu_avoir_vente");
        } else {
            update("form_contenu_retour_vente");
        }
    }

    private void loadInfosAfterChooseArticle(ContenuDocVente contenu, Articles art) {
        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
            if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                art.setStock(dao.stocks(art.getId(), 0, docVente.getDepot().getId(), 0, 0, docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
            } else {
                art.setStock(dao.stocks(art.getId(), 0, 0, currentAgence.getId(), 0, docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
            }
            art.setPuv(dao.getPuv(art.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId()));
            contenu.setPrixMin(dao.getPuvMin(art.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId()));
            art.setPua(dao.getPua(art.getId(), 0));
            if (contenu.getId() < 1) {
                contenu.setPrix(art.getPuv());
            }
            YvsBaseDepots depot;
            if (docVente.getDepot() != null ? docVente.getDepot().getId() < 1 : true) {
                depot = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticlePr", new String[]{"article", "agence"}, new Object[]{new YvsBaseArticles(art.getId()), new YvsAgences(docVente.getEnteteDoc().getAgence().getId())});
            } else {
                depot = new YvsBaseDepots(docVente.getDepot().getId());
            }
            contenu.setPr(dao.getPr(docVente.getEnteteDoc().getAgence().getId(), art.getId(), depot != null ? depot.getId() : 0, 0, docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId()));
            double rabais = returnRabais(contenu.getConditionnement().getId(), new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId()), docVente.getEnteteDoc().getDateEntete(), contenu.getQuantite(), contenu.getPrix());
            if (contenu.getRabais() != rabais) {
                if (type.equals(Constantes.TYPE_FAV)) {
                    update("tabView_fav:form_article_avoir_vente");
                } else {
                    update("form_contenu_retour_vente");
                }
            }
            contenu.setRabais(rabais);
        }
    }

    public void onPrixBlur() {
        if ((docVente.getClient() != null) ? docVente.getClient().getId() > 0 : false) {
            findPrixArticle(contenu, false);
        }
    }

    public ContenuDocVente findPrixArticle(ContenuDocVente c, boolean findPrix) {
        if ((docVente.getClient() != null) ? docVente.getClient().getId() > 0 : false) {
            if (c.getId() < 1 ? findPrix : false) {
                c.setPrix(dao.getPuv(c.getArticle().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), c.getConditionnement().getId()));
            }
            double prix = c.getPrix() - c.getRabais();
            double total = c.getQuantite() * prix;
            double _remise = dao.getRemiseVente(c.getArticle().getId(), c.getQuantite(), prix, docVente.getClient().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getUnite().getId());
            double _ristourne = dao.getRistourne(c.getConditionnement().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDateEntete());
            if (selectContenu != null ? selectContenu.getId() > 0 : false) {
                if (c.getId() > 0 && (selectContenu.getQuantite() == c.getQuantite() && selectContenu.getPrix() == c.getPrix())) {
                    _remise = selectContenu.getRemise();
                }
            }
            c.setRistourne(_ristourne);
            c.setRemise(_remise);
            c.setPrixTotal(total - c.getRemise());
            c.setPrixMin(dao.getPuvMin(c.getArticle().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId()));
            long categorie = 0;
            if (selectDoc != null ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
                if (selectDoc.getCategorieComptable() != null ? selectDoc.getCategorieComptable().getId() > 0 : false) {
                    categorie = selectDoc.getCategorieComptable().getId();
                }
            } else {
                if (docVente.getCategorieComptable() != null ? docVente.getCategorieComptable().getId() > 0 : false) {
                    categorie = docVente.getCategorieComptable().getId();
                }
            }
            if (categorie > 0) {
                c.setTaxe(dao.getTaxe(c.getArticle().getId(), categorie, 0, c.getRemise(), c.getQuantite(), prix, true, 0));
                c.setPrixTotal(c.getPrixTotal() + (c.getArticle().isPuvTtc() ? 0 : c.getTaxe()));
            } else {
                getWarningMessage("Selectionner la catégorie comptable!");
            }
            if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                champ = new String[]{"article", "depot"};
                val = new Object[]{new YvsBaseArticles(c.getArticle().getId()), new YvsBaseDepots(docVente.getDepot().getId())};
                Boolean sellWithOutStock = (Boolean) dao.loadObjectByNameQueries("YvsBaseArticleDepot.findIfSellWithOutStock", champ, val);
                if (sellWithOutStock != null ? !sellWithOutStock : false) {
                    getErrorMessage("Cet article ne peut etre vendu sans stock dans ce dépot");
                    resetFicheArticle();
                    return c;
                }
            }
        } else {
            getWarningMessage("Selectionner le client!");
        }
        return c;
    }

    public void choosePoint_(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                docVente.getEnteteDoc().getPoint().setId(id);
                choosePointById(id);
            } else {
                boolean pagine = false;
                boolean next = false;
                if (id == -1) {
                    pagine = true;
                    next = false;
                } else if (id == -2) {
                    pagine = true;
                    next = true;
                }
                if (pagine) {
                    ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                    if (m != null) {
                        m.loadPointVenteByDroit(next, false);
                    }
                }
                docVente.getEnteteDoc().getPoint().setId(0);
            }
        }
    }

    public void choosePointById(long id) {
        if (id > 0) {
            docVente.getEnteteDoc().getPoint().setId(id);
            ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (service != null) {
                int idx = service.getPointsvente().indexOf(new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId()));
                if (idx >= 0) {
                    YvsBasePointVente y = service.getPointsvente().get(idx);
                    choosePoint(y);
                }
            }
        }
    }

    public void choosePoint(YvsBasePointVente y) {
        selectEntete = new YvsComEnteteDocVente();
        docVente.getEnteteDoc().getPoint().getListTranche().clear();
        vendeurs.clear();
        if (docVente.getEnteteDoc().getPoint() != null ? docVente.getEnteteDoc().getPoint().getId() > 0 : false) {
            if (y != null) {
                docVente.getEnteteDoc().setPoint(UtilCom.buildSimpleBeanPointVente(y));
                docVente.getEnteteDoc().getPoint().setSecteur(UtilGrh.buildBeanDictionnaire(y.getSecteur()));
                docVente.setValidationReglement(y.getValidationReglement());
                //trouve le vendeur
                if (autoriser("fv_can_save_for_other")) {
                    vendeurs = dao.loadNameQueries("YvsComCreneauHoraireUsers.findUsersByPoint", new String[]{"point"}, new Object[]{y});
                } else {
                    vendeurs.add(currentUser.getUsers());
                }
                if (vendeurs.contains(currentUser.getUsers())) {
                    docVente.getEnteteDoc().getCrenauHoraire().setPersonnel(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
                } else {
                    if (!vendeurs.isEmpty()) {
                        docVente.getEnteteDoc().getCrenauHoraire().setPersonnel(UtilUsers.buildSimpleBeanUsers(vendeurs.get(0)));
                    }
                }
                docVente.getEnteteDoc().setUsers(docVente.getEnteteDoc().getCrenauHoraire().getPersonnel());
                docVente.setAdresse(y.getSecteur() != null ? UtilGrh.buildSimpleBeanDictionnaire(y.getSecteur()) : new Dictionnaire());
                docVente.setVille(y.getSecteur() != null ? y.getSecteur().getParent() != null ? UtilGrh.buildSimpleBeanDictionnaire(y.getSecteur().getParent()) : new Dictionnaire() : new Dictionnaire());
                //charge les dépôts
                loadDepotByPoint(y);
                update("txt_zone_client");
//                }
            }
            ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (w != null) {
                w.setEntityPoint(y);
            }
            if (docVente.getEnteteDoc().getPoint().getListTranche() != null ? !docVente.getEnteteDoc().getPoint().getListTranche().isEmpty() : false) {
                docVente.getEnteteDoc().setTranche(UtilCom.buildBeanTrancheHoraire(docVente.getEnteteDoc().getPoint().getListTranche().get(0).getTranche()));
            }
        }
    }

    public void chooseVendeur(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long idVendeur = (long) ev.getNewValue();
            if (idVendeur > 0) {
                docVente.getEnteteDoc().getCrenauHoraire().getPersonnel().setId(idVendeur);
                ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                if (service != null) {
                    int idx = service.getPointsvente().indexOf(new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId()));
                    if (idx >= 0) {
                        YvsBasePointVente y = service.getPointsvente().get(idx);
                        loadDepotByPoint(y);
                    }
                }

            }
        }
    }

    public void chooseConditionnement() {
        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
            if (contenu.getArticle() != null) {
                int idx = contenu.getArticle().getConditionnements().indexOf(new YvsBaseConditionnement(contenu.getConditionnement().getId()));
                if (idx > -1) {
                    YvsBaseConditionnement y = contenu.getArticle().getConditionnements().get(idx);
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(y));
                    if (y.getPrixMin() != 0) {
                        contenu.setPrixMin(y.getPrixMin());
                    }
                }
            }
        }
        loadInfosAfterChooseArticle(contenu, contenu.getArticle());
        findPrixArticle(contenu, false);
    }

    public void changeSource() {
        if (!docVente.isFromFacture()) {
            if (docVente.getClient() != null ? docVente.getClient().getId() < 1 : true) {
                setClientDefaut();
            }
        }
    }

    public void setClientDefaut() {
        if (docVente.getClient() != null ? docVente.getClient().getId() < 1 : true) {
            YvsComClient c = currentClientDefault();
            if (c != null ? c.getId() > 0 : false) {
                chooseClient(UtilCom.buildSimpleBeanClient(c));
            }
            update("txt_client_retour_vente");
        }
    }

    public void chooseDocVente(DocVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            if (m != null) {
                int idx = m.getDocuments().indexOf(new YvsComDocVentes(y.getId()));
                if (idx > -1) {
                    YvsComDocVentes entity = m.getDocuments().get(idx);
                    YvsComDocVentes bean = new YvsComDocVentes(entity);
                    bean.setCategorieComptable(entity.getCategorieComptable());
                    bean.setClient(entity.getClient());
                    bean.setTiers(entity.getTiers());
                    onSelectFacture(bean);
                }
            }
        } else {
            resetFiche();
        }
    }

    public void onSelectFacture(YvsComDocVentes y) {
        List<YvsComContenuDocVente> list = new ArrayList<>();
        list.addAll(y.getContenus());
        facture = UtilCom.buildBeanDocVente(y);
        loadContenuFacture(y, true);
        facture.setContenus(list);
        setMontantTotalDoc(docVente);
        loadDepotByPoint(y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint());
        chooseDepotLivraison();
        resetFicheArticle();
        docVente.getContenus().clear();
        if (type.equals(Constantes.TYPE_FAV)) {
            update("data_contenu_avoir_vente");
            update("infos_document_avoir_vente");
        } else {
            update("data_contenu_retour_vente");
            update("infos_document_retour_vente");
        }
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
        ParametreRequete p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence", "agence", null, "=", "AND");
        if (agence_ > 0) {
            p.setObjet(new YvsAgences(agence_));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
//        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
//        if (m != null) {
//            m.addParamAgence(agence_);
//        }
    }

    @Override
    public void _chooseDepot() {
        super._chooseDepot();
        ParametreRequete p;
        if (depot_ > 0) {
            p = new ParametreRequete("enteteDoc.creneau.creneauDepot.depot", "depot", new YvsBaseDepots(depot_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("enteteDoc.creneau.creneauDepot.depot", "depot", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    @Override
    public void _choosePoint() {
        super._choosePoint();
        ParametreRequete p;
        if (point_ > 0) {
            p = new ParametreRequete("enteteDoc.creneau.creneauPoint.point", "point", new YvsBasePointVente(point_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("enteteDoc.creneau.creneauPoint.point", "point", null);
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
            p = new ParametreRequete("enteteDoc.creneau.creneauDepot.tranche", "tranche", new YvsGrhTrancheHoraire(tranche_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("enteteDoc.creneau.creneauDepot.tranche", "tranche", null);
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

    public void searchByClient() {
        ParametreRequete p;
        if (codeClient_ != null ? codeClient_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "client", codeClient_.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nomClient)", "client", codeClient_.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.codeClient)", "client", codeClient_.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.nom)", "client", codeClient_.toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p = new ParametreRequete("y.client.codeClient", "client", null);
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }

    public void addParamVendeur(boolean code) {
        ParametreRequete p;
        if (!code) {
            if (users_ > 0) {
                p = new ParametreRequete("enteteDoc.creneau.users", "vendeur", new YvsUsers(users_));
                p.setOperation("=");
                p.setPredicat("AND");
            } else {
                p = new ParametreRequete("enteteDoc.creneau.users", "vendeur", null);
            }
        } else {
            if (codeVendeur_ != null ? codeVendeur_.trim().length() > 0 : false) {
                p = new ParametreRequete("enteteDoc.creneau.users.codeUsers", "vendeur", codeVendeur_ + "%", " LIKE ", "OR");
                ParametreRequete p1 = new ParametreRequete("enteteDoc.creneau.users.nomUser", "vendeur", codeVendeur_ + "%", " LIKE ", "AND");
//                p.getOtherExpression().add(p1);
            } else {
                p = new ParametreRequete("enteteDoc.creneau.users.codeUsers", "vendeur", null);
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

    public void activeMouvStock(YvsComContenuDocVente y) {
        if (y != null ? y.getId() > 0 : false) {
            y.setMouvStock(!y.getMouvStock());
            y.setAuthor(currentUser);
            dao.update(y);
            contenus.set(contenus.indexOf(y), y);
            succes();
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

    public void chooseTrancheLivraison() {
        if (docVente.getTranche() != null ? docVente.getTranche().getId() > 0 : false) {
            int idx = tranches_retour.indexOf(new YvsGrhTrancheHoraire(docVente.getTranche().getId()));
            if (idx > -1) {
                YvsGrhTrancheHoraire y = tranches_retour.get(idx);
                TrancheHoraire t = UtilCom.buildBeanTrancheHoraire(y);
                cloneObject(docVente.getTranche(), t);
            }
        }
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

    public void chooseDepotLivraison() {
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            YvsBaseDepots y = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{docVente.getDepot().getId()});
            Depots t = UtilCom.buildBeanDepot(y);
            cloneObject(docVente.getDepot(), t);
            if (!verifyOperation(docVente.getDepot(), Constantes.SORTIE, Constantes.VENTE, true)) {
                return;
            }
            loadAllTrancheLivraison(y, docVente.getDateLivraison());
        }
    }

    public void chooseDateLivraison() {
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            loadAllTrancheLivraison(new YvsBaseDepots(docVente.getDepot().getId()), docVente.getDateLivraison());
        }
    }

    public void chooseIsBon() {
        initView();
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
                    contenus_fv = loadContenus(l.get(0));
                } else {
                    docSelect = UtilCom.buildBeanDocVente(selectDoc);
                    docSelect.setDocumentLie(new DocVente(selectDoc.getId(), selectDoc.getNumDoc(), selectDoc.getStatut()));
                    docSelect.setHeureDoc(new Date());
                    docSelect.setUpdate(false);
                    docSelect.setTypeDoc(Constantes.TYPE_FAV);
//                    docSelect.setNumDoc(genererReference(Constantes.TYPE_FAV_NAME, new Date()));
                    cloneObject(docSelect.getCategorieComptable(), docSelect.getClient().getCategorieComptable());
                    docSelect.setStatut(Constantes.ETAT_EDITABLE);

                    contenus_fv = loadContenus(selectDoc);
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
            update("data_livreur_avoir_vente");
        }
    }

    public void initClient() {

    }

    YvsComptaCaissePieceVente piece;
    String source;
    boolean emission;

    public void openConfirmPaiement(YvsComptaCaissePieceVente pc, String source, boolean emission) {
        this.piece = pc;
        this.source = source;
        this.emission = emission;
        if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_VENTE)) {
            openDialog("dlgConfirmAnnulePiece");
            return;
        }
        ManagedReglementVente service = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
        if (service != null) {
            pc = service.openConfirmPaiement(pc, source, true, true, emission);
            int idx = docVente.getReglements().indexOf(pc);
            if (idx > -1) {
                docVente.getReglements().set(idx, pc);
            }
        }
    }

    public void openConfirmPaiement() {
        ManagedReglementVente service = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
        if (service != null) {
            piece = service.openConfirmPaiement(piece, source, true, true, emission);
            int idx = docVente.getReglements().indexOf(piece);
            if (idx > -1) {
                docVente.getReglements().set(idx, piece);
            }
        }
    }

    public void onPrixOrQteBlur() {
        if (recalculTaxe) {
            contenu.setTaxe(dao.getTaxe(contenu.getArticle().getId(), docVente.getCategorieComptable().getId(), 0, 0, contenu.getQuantite(), (contenu.getPrix()), true, 0));
        } else {
            contenu.setTaxe(contenu_fv.getTaxe());
        }
        if (contenu_fv.getArticle().isPuvTtc()) {
            contenu.setPrixTotal((contenu.getPrix() * contenu.getQuantite()));
        } else {
            contenu.setPrixTotal((contenu.getPrix() * contenu.getQuantite()) + contenu.getTaxe());
        }
    }

    public void apllyOrNotTaxes(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            if (!(Boolean) ev.getNewValue()) {
                contenu.setTaxe(dao.getTaxe(contenu.getArticle().getId(), docVente.getCategorieComptable().getId(), 0, 0, contenu.getQuantite(), (contenu.getPrix()), true, 0));
            } else {
                contenu.setTaxe(contenu_fv.getTaxe());
            }
            onPrixOrQteBlur();
        }
    }

    public void onTaxeBlur() {
        if (!contenu_fv.getArticle().isPuvTtc()) {
            contenu.setPrixTotal((contenu.getPrix() * contenu.getQuantite()) + contenu.getTaxe());
        }
    }
//
//    public void onQteBlur(boolean reste) {
//        if (reste) {
//            contenu.setQuantite_(contenu_fv.getQuantite() - contenu.getQuantite());
//        } else {
//            contenu.setQuantite(contenu_fv.getQuantite() - contenu.getQuantite_());
//        }
//        onContenuBlur();
//    }
//
//    public void onRemiseBlur(boolean reste) {
//        if (reste) {
//            contenu.setRemise_(contenu_fv.getRemise() - contenu.getRemise());
//        } else {
//            contenu.setRemise(contenu_fv.getRemise() - contenu.getRemise_());
//        }
//        onContenuBlur();
//    }
//

//
//    public void onTotalBlur(boolean reste) {
//        if (reste) {
//            contenu.setPrixTotal_(contenu_fv.getPrixTotal() - contenu.getPrixTotal());
//        } else {
//            contenu.setPrixTotal(contenu_fv.getPrixTotal() - contenu.getPrixTotal_());
//        }
//        onTotalBlur();
//    }
//
//    public void onContenuBlur() {
//        contenu.setPrixTotal_((contenu.getQuantite_() * (contenu.getPrix_() - contenu.getRabais())) - contenu.getRemise_() + contenu.getTaxe_());
//        contenu.setPrixTotal(contenu_fv.getPrixTotal() - contenu.getPrixTotal_());
//        update("txt_total_contenu_fav");
//    }
//
//    public void onTotalBlur() {
//        contenu.setPrix_(((contenu.getPrixTotal_() / contenu.getQuantite_()) + contenu.getRabais()) + contenu.getRemise_() - contenu.getTaxe_());
//        contenu.setPrix(contenu_fv.getPrix() - contenu.getPrix_());
//        update("txt_puv_contenu_fav");
//    }
    public void imprimerBonAvoir(DocVente bean) {
        if ((bean != null) ? bean.getId() > 0 : false) {

        }
    }

    public void loadOnViewLivreur(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsUsers bean = (YvsUsers) ev.getObject();
            docVente.setLivreur(UtilUsers.buildBeanUsers(bean));
            update("txt_livreur_avoir_vente");
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
                    update("data_livreur_avoir_vente");
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
                    changeStatut(Constantes.ETAT_SOUMIS, selectDoc);
                } else {
                    getErrorMessage("Impossible d'envoyer! Email Incorrect");
                }
            } else {
                getErrorMessage("Impossible d'envoyer! Le fournisseur n'a pas d'email");
            }
        }
    }

    public void print(YvsComDocVentes y) {
        print(y, true);
    }

    public void print(YvsComDocVentes y, boolean withHeader) {
        try {
            if (currentParamVente != null ? currentParamVente.getId() < 1 : true) {
                currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (y != null ? y.getId() > 0 : false) {
                if (currentParamVente != null ? (currentParamVente.getPrintDocumentWhenValide() && !y.getStatut().equals(Constantes.ETAT_VALIDE)) : false) {
                    getErrorMessage("Le document doit être validé pour pouvoir être téléchargé");
                    return;
                }
                Map<String, Object> param = new HashMap<>();
                param.put("ID", y.getId().intValue());
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(withHeader));
                String report = "bon_retour_vente";
                if (y.getTypeDoc().equals(Constantes.TYPE_BRV)) {
                    if (currentParam != null ? currentParam.getUseLotReception() : false) {
                        report = "bon_retour_vente_by_lot";
                    }
                } else {
                    Double ca = dao.loadCaVente(y.getId());
                    Double tx = dao.loadTaxeVente(y.getId());
                    param.put("TAXE", tx != null ? tx > 0 : false);
                    param.put("MONTANT", Nombre.CALCULATE.getValue(ca));
                    report = "facture_avoir_vente";
                }
                executeReport(report, param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean annulerOrder(boolean force) {
        System.err.println("HERE");
        return annulerOrder(selectDoc, docVente, true, true, force);
    }

    public boolean annulerOrder(YvsComDocVentes y, boolean msg, boolean open, boolean force) {
        return annulerOrder(selectDoc, UtilCom.buildSimpleBeanDocVente(y), msg, open, force);
    }

    public boolean annulerOrder(YvsComDocVentes entity, DocVente bean, boolean msg, boolean open, boolean force) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                if (dao.isComptabilise(entity.getId(), Constantes.SCR_AVOIR_VENTE)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return false;
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        if (!w.unComptabiliserVente(entity, false)) {
                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                            return false;
                        }
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
                    if (changeStatut(Constantes.ETAT_EDITABLE, bean, entity)) {
                        entity.setStatut(Constantes.ETAT_EDITABLE);
                        entity.setCloturer(false);
                        entity.setAnnulerBy(null);
                        entity.setCloturerBy(null);
                        entity.setValiderBy(null);
                        entity.setDateAnnuler(null);
                        entity.setDateCloturer(null);
                        entity.setDateValider(null);
                        entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_ATTENTE));
                        entity.setDateLivraison(null);
                        if (currentUser != null ? currentUser.getId() > 0 : false) {
                            entity.setAuthor(currentUser);
                        }
                        YvsComDocVentes y = new YvsComDocVentes(entity);
                        y.getContenus().clear();
                        dao.update(y);
                        if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() > 0 : false) {
                            Map<String, String> statuts = dao.getEquilibreVente(entity.getDocumentLie().getId());
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
        return refuserOrder(selectDoc, docVente, true, true, force);
    }

    public boolean refuserOrder(YvsComDocVentes y, boolean msg, boolean open, boolean force) {
        return refuserOrder(selectDoc, UtilCom.buildSimpleBeanDocVente(y), msg, open, force);
    }

    public boolean refuserOrder(YvsComDocVentes entity, DocVente bean, boolean msg, boolean open, boolean force) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                if (dao.isComptabilise(entity.getId(), Constantes.SCR_AVOIR_VENTE)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return false;
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        if (!w.unComptabiliserVente(entity, false)) {
                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                            return false;
                        }
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
                    if (changeStatut(Constantes.ETAT_ANNULE, bean, entity)) {
                        entity.setStatut(Constantes.ETAT_ANNULE);
                        entity.setCloturer(false);
                        entity.setAnnulerBy(currentUser.getUsers());
                        entity.setValiderBy(null);
                        entity.setDateAnnuler(new Date());
                        entity.setDateCloturer(null);
                        entity.setDateValider(null);
                        entity.setLivreur(null);
                        entity.setTrancheLivrer(null);
                        entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_ATTENTE));
                        entity.setDateLivraison(null);
                        if (currentUser != null ? currentUser.getId() > 0 : false) {
                            entity.setAuthor(currentUser);
                        }
                        YvsComDocVentes y = new YvsComDocVentes(entity);
                        y.getContenus().clear();
                        dao.update(y);
                        if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() > 0 : false) {
                            Map<String, String> statuts = dao.getEquilibreVente(entity.getDocumentLie().getId());
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
            getException("Erreur : ", ex);
        }
        return false;
    }

    public void transmisOrder() {
        if (selectDoc == null) {
            return;
        }
        if (changeStatut(Constantes.ETAT_SOUMIS)) {
            if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getId() > 0 : false) {
                Map<String, String> statuts = dao.getEquilibreVente(selectDoc.getDocumentLie().getId());
                if (statuts != null) {
                    selectDoc.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                    selectDoc.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                }
            }
        }
    }

    public boolean validerOrder(YvsComDocVentes y) {
        if (validerOrder(y, UtilCom.buildSimpleBeanDocVente(y))) {
            if (type.equals(Constantes.TYPE_FAV)) {
                update("data_avoir_vente");
            } else {
                update("data_retour_vente");
            }
            return true;
        }
        return false;
    }

    public boolean validerOrder() {
        System.err.println("INt");
        if (validerOrder(selectDoc, docVente)) {
            if (type.equals(Constantes.TYPE_FAV)) {
                update("data_avoir_vente");
            } else {
                update("data_retour_vente");
            }
            return true;
        }
        return false;
    }

    public boolean validerOrder(YvsComDocVentes entity, DocVente bean) {
        if (entity == null) {
            return false;
        }
        if (!verifyOperation(bean.getDepot(), Constantes.SORTIE, Constantes.VENTE, true)) {
            return false;
        }
        if (changeStatut(Constantes.ETAT_VALIDE, bean, entity)) {
            entity.setCloturer(false);
            entity.setAnnulerBy(null);
            entity.setValiderBy(currentUser.getUsers());
            entity.setDateAnnuler(null);
            entity.setDateCloturer(null);
            entity.setDateValider(new Date());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                entity.setAuthor(currentUser);
            }
            entity.setStatut(Constantes.ETAT_VALIDE);
            YvsComDocVentes y = new YvsComDocVentes(entity);
            y.getContenus().clear();
            dao.update(y);
            if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() > 0 : false) {
                Map<String, String> statuts = dao.getEquilibreVente(entity.getDocumentLie().getId());
                if (statuts != null) {
                    entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                    entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
                }
            }
//            if (type.equals(Constantes.TYPE_FAV)) {
//                if (bean.getMontantTotal() <= 0) {
//                    setMontantTotalDoc(bean);
//                }
//                if (bean.getMontantResteApayer() > 0) {
//                    ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
//                    if (service != null) {
//                        YvsBaseCaisse caisse = service.findByResponsable(currentUser.getUsers());
//                        YvsBaseModeReglement espece = modeEspece();
//                        ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
//                        if (w != null) {
//                            YvsComptaCaissePieceVente piece = w.buildPieceFromModel(0, espece, entity, caisse, entity.getEnteteDoc().getDateEntete(), bean.getMontantResteApayer(), Constantes.MOUV_CAISS_SORTIE.charAt(0));
//                            if (piece != null) {
//                                piece.setId(null);
//                                piece = (YvsComptaCaissePieceVente) dao.save1(piece);
//                                bean.getReglements().add(piece);
//
//                                int idx = entity.getReglements().indexOf(piece);
//                                if (idx < 0) {
//                                    entity.getReglements().add(piece);
//                                }
//                                update("tabView_fav");
//                            }
//                        }
//                    }
//                }
//            }
            return true;
        }
        return false;
    }

    public boolean livrer(YvsComDocVentes entity, DocVente bean) {
        if (entity == null) {
            return false;
        }
        if (bean.getDepot() != null ? bean.getDepot().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le dépot de livraison");
            return false;
        }
        if (bean.getTranche() != null ? bean.getTranche().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier la tranche de livraison");
            return false;
        }
        if (bean.getLivreur() != null ? bean.getLivreur().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le livreur");
            return false;
        }
        if (!bean.getDocumentLie().getStatut().equals(Constantes.ETAT_VALIDE)) {
            getErrorMessage("La facture n'est pas valide");
            return false;
        }
        if (_saveNew()) {
            String result = null;
            YvsComContenuDocVente cc = null;
            for (YvsComContenuDocVente c : entity.getContenus()) {
                cc = c;
                result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), bean.getDepot().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", bean.getDateLivraison(), (c.getLot() != null ? c.getLot().getId() : 0));
                if (result != null) {
                    break;
                }
                //controle les quantités déjà livré
                Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{c.getDocVente().getDocumentLie(), Constantes.ETAT_VALIDE, Constantes.TYPE_FAV, c.getArticle(), c.getConditionnement()});
                qteLivre = (qteLivre != null) ? qteLivre : 0;
                //trouve la quantité d'article facturé 
                Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{c.getDocVente().getDocumentLie(), c.getArticle(), c.getConditionnement()});
                qteFacture = (qteFacture != null) ? qteFacture : 0;
                //récupère la quantité de l'article dans le document de livraison en cours. (Le pb viens du fait que la ref d'un article peut se trouver plusieurs fois dans la liste d'un bl non encore livré)
                Double qteEncour = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByArticle", new String[]{"docVente", "article", "unite"}, new Object[]{c.getDocVente(), c.getArticle(), c.getConditionnement()});
                qteEncour = (qteEncour != null) ? qteEncour : 0;
                if (qteEncour > (qteFacture - qteLivre)) {
                    getErrorMessage("Vous ne pouvez livrer l'article " + c.getArticle().getRefArt() + " au delà de la quantité facturée !");
                    return false;
                }

                champ = new String[]{"article", "depot"};
                val = new Object[]{c.getArticle(), new YvsBaseDepots(bean.getDepot().getId())};
                YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
                if (y != null ? y.getId() < 1 : true) {
                    getErrorMessage("Impossible d'effectuer cette action... Car le depot " + bean.getDepot().getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
                    return false;
                }
            }
            if (result != null) {
                getErrorMessage("Impossible de valider ce document car la ligne d'article " + cc.getArticle().getDesignation() + " engendrera une incohérence dans le stock");
                return false;
            }
            bean.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entity.setStatutLivre(String.valueOf(Constantes.STATUT_DOC_LIVRER));
            entity.setDateLivraison(bean.getStatutLivre().equals(String.valueOf(Constantes.STATUT_DOC_LIVRER)) ? new Date() : null);
            return true;
        }
        return false;
    }

    public void cloturer(YvsComDocVentes y) {
        selectDoc = y;
        if (type.equals(Constantes.TYPE_FAV)) {
            update("id_confirm_close_fav");
        } else {
            update("id_confirm_close_brv");
        }
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
        if (type.equals(Constantes.TYPE_FAV)) {
            update("data_avoir_vente");
        } else {
            update("data_retour_vente");
        }
        update("data_facture_vente");
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
        if (type.equals(Constantes.TYPE_FAV)) {
            update("data_avoir_vente");
        } else {
            update("data_retour_vente");
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
        return changeStatut_(etat, docVente, selectDoc);
    }

    public boolean changeStatut(String etat, YvsComDocVentes entity) {
        if (changeStatut_(etat, entity)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, YvsComDocVentes entity) {
        return changeStatut_(etat, UtilCom.buildBeanDocVente(entity), entity);
    }

    public boolean changeStatut(String etat, DocVente bean, YvsComDocVentes entity) {
        if (changeStatut_(etat, bean, entity)) {
            succes();
            return true;
        }
        return false;
    }

    public boolean changeStatut_(String etat, DocVente bean, YvsComDocVentes entity) {
        return changeStatut_(etat, bean, entity, true);
    }

    public boolean changeStatut_(String etat, DocVente bean, YvsComDocVentes entity, boolean isBon) {
        if (!etat.equals("")) {
            if (bean.isCloturer()) {
                getErrorMessage("Le document est provisoirement vérouillé !");
                return false;
            }
            String rq = "UPDATE yvs_com_doc_ventes SET statut = '" + etat + "' WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            bean.setStatut(etat);
            entity.setStatut(etat);
            int idx = documents.indexOf(entity);
            if (idx > -1) {
                documents.set(idx, entity);
            }
            ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            if (m != null) {
                m.loadFactureNonLivre(true, true);
                update("data_fv_livraison");
            }
            if (type.equals(Constantes.TYPE_FAV)) {
                update("infos_document_avoir_vente");
                update("data_avoir_vente");
            } else {
                update("infos_document_retour_vente");
                update("data_retour_vente");
            }
            return true;
        }
        return false;
    }

    public void onBuildGenererOther(String type) {
        try {
            this.typeGenerer = type;
            if (typeGenerer.equals(Constantes.TYPE_BRV)) {
                this.dateGenerer = docVente.getDateLivraison();
            } else {
                this.dateGenerer = docVente.getEnteteDoc().getDateEntete();
            }
            update("main-generer_other");
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onBuildGenererOther", ex);
        }
    }

    public void onGenererOther() {
        try {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                YvsComEnteteDocVente _e_ = selectDoc.getEnteteDoc();
                if (typeGenerer.equals(Constantes.TYPE_FAV)) {
                    if (_e_.getCreneau() != null ? _e_.getCreneau().getId() < 1 : true) {
                        getErrorMessage("Aucun planing trouvé pour ce document");
                        return;
                    }
                    if (_e_.getCreneau().getUsers() != null ? _e_.getCreneau().getUsers().getId() < 1 : true) {
                        getErrorMessage("Aucun vendeur pour ce planing trouvé sur ce document");
                        return;
                    }
                    if (_e_.getCreneau().getCreneauPoint() != null ? _e_.getCreneau().getCreneauPoint().getId() < 1 : true) {
                        getErrorMessage("Aucun point de vente pour ce planing trouvé sur ce document");
                        return;
                    }
                    if (_e_.getCreneau().getCreneauPoint().getPoint() != null ? _e_.getCreneau().getCreneauPoint().getPoint().getId() < 1 : true) {
                        getErrorMessage("Aucun point de vente pour ce planing trouvé sur ce document");
                        return;
                    }
                }
                YvsComDocVentes y = new YvsComDocVentes(null, selectDoc);
                y.getContenus().clear();
                y.getReglements().clear();
                y.getEtapesValidations().clear();
                y.getCouts().clear();
                y.getMensualites().clear();

                y.setTypeDoc(typeGenerer);
                y.setStatut(Constantes.ETAT_EDITABLE);
                if (typeGenerer.equals(Constantes.TYPE_FAV)) {
                    YvsComEnteteDocVente e = dao.getEntete(_e_.getCreneau().getUsers(), _e_.getCreneau().getCreneauPoint().getPoint(), _e_.getCreneau().getCreneauPoint().getTranche(), dateGenerer, currentNiveau, currentAgence, currentUser);
                    if (e != null ? e.getId() < 1 : true) {
                        return;
                    }
                    y.setEnteteDoc(e);
                } else {
                    y.setDateLivraison(dateGenerer);
                }
                String titre = Constantes.TYPE_FAV_NAME;
                if (typeGenerer.equals(Constantes.TYPE_BRV)) {
                    titre = Constantes.TYPE_BRV_NAME;
                }
                String numero = null;
                //modifie le numéro de document si la date change   
                if (docVente.getDepot() != null ? docVente.getDepot().getId() < 1 : true) {
                    numero = genererReference(titre, dateGenerer, _e_.getCreneau().getCreneauPoint().getPoint().getId(), Constantes.POINTVENTE);
                } else {
                    numero = genererReference(titre, dateGenerer, docVente.getDepot().getId(), Constantes.DEPOT);
                }
                if (numero == null || numero.trim().equals("")) {
                    return;
                }
                y.setNumDoc(numero);
                y.setDateSave(new Date());
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                y = (YvsComDocVentes) dao.save1(y);
                if (y != null ? y.getId() < 1 : true) {
                    return;
                }
                YvsComContenuDocVente c;
                for (int i = 0; i < contenus.size(); i++) {
                    c = new YvsComContenuDocVente(null, contenus.get(i));
                    c.setDocVente(y);
                    c.setDateSave(new Date());
                    c.setDateUpdate(new Date());
                    c.setAuthor(currentUser);
                    c = (YvsComContenuDocVente) dao.save1(c);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onGenererOther", ex);
        }
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

    public void addParamIds() {
        addParamIds(true);
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
    public void cleanVente() {
        super.cleanVente();
        loadAllFacture(true, true);
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /* Géstion des retours d'article*/
    public void openDelgRetourStock(YvsComContenuDocVente c) {
        selectContenu = c;
    }

    public void generatedFAvoirFromBRetour(YvsComDocVentes d) {
        if (d != null) {
            resetFiche();
            docVente = UtilCom.buildBeanDocVente(d);
            docVente.setStatut(Constantes.ETAT_EDITABLE);
            docVente.setTypeDoc(Constantes.TYPE_FAV);
            docVente.setCategorieComptable(UtilCom.buildBeanCategorieComptable(d.getDocumentLie().getCategorieComptable()));
            docVente.setId(0);
            docVente.setDocumentLie(UtilCom.buildSimpleBeanOnlyDocVente(d.getDocumentLie()));
            docVente.setNumPiece(docVente.getNumPiece().replace("BRV", "FAV"));
            docVente.setNumDoc(null);
            docVente.setDateSave(new Date());
            docVente.setTiers(UtilCom.buildSimpleBeanClient(d.getDocumentLie().getClient()));
            facture = UtilCom.buildSimpleBeanDocVente(d.getDocumentLie());
            System.err.println(" --- Contenu retourn " + d.getContenus().size() + " --- " + docVente.getContenus().size());
            contenus_fv = dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{d.getDocumentLie()});
            List<YvsComContenuDocVente> contentBon = dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{d});
            docVente.getContenus().clear();
            for (YvsComContenuDocVente c : contentBon) {
                c.setId(null);
                contenus.add(c);
            }
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Factures Avoir Vente", "modCompta", "smenFactureAvoirVente", true);
            }
        }
    }

    private List<YvsComptaPiecesComptable> contentCompta = new ArrayList<>();

    public List<YvsComptaPiecesComptable> getContentCompta() {
        return contentCompta;
    }

    public void setContentCompta(List<YvsComptaPiecesComptable> contentCompta) {
        this.contentCompta = contentCompta;
    }

    public void displayPieceComptableFacture() {
        if (docVente != null ? docVente.getId() > 0 : false) {
            String query = "SELECT p.id,p.num_piece,p.date_piece,c.id,c.num_piece,c.compte_general, c.compte_tiers, c.table_tiers,c.debit, c.credit "
                    + "FROM yvs_compta_content_journal_facture_vente cf INNER JOIN yvs_compta_pieces_comptable p ON p.id=cf.piece LEFT JOIN yvs_compta_content_journal c ON c.piece=p.id "
                    + "WHERE cf.facture=? ORDER BY p.id";

            List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(docVente.getId(), 1)});
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

    private String numCompte(Long id) {
        if (id != null) {
            return (String) dao.loadObjectByNameQueries("YvsBasePlanComptable.findNumeroById", new String[]{"id"}, new Object[]{id});
        }
        return null;
    }

    public boolean isComptabiliseBean(DocVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_AVOIR_VENTE));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_AVOIR_VENTE));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllFacture(true, true);
    }
}
