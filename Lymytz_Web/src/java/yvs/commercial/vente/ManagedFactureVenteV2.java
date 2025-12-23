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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import lymytz.navigue.Navigations;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.ModelReglement;
import yvs.base.compta.UtilCompta;
import yvs.comptabilite.caisse.ManagedReglementVente;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.production.UtilProd;
import yvs.base.tiers.Tiers;
import yvs.commercial.Commerciales;
import yvs.commercial.ManagedCatCompt;
import yvs.commercial.ManagedCommercial;

import static yvs.commercial.ManagedCommercial.currentParam;

import yvs.commercial.ManagedCommerciaux;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.ManagedTaxes;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.ManagedFactureAchat;
import yvs.commercial.achat.ManagedLotReception;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.creneau.ManagedTypeCreneau;
import yvs.commercial.depot.ManagedPointLivraison;
import yvs.commercial.depot.ManagedPointVente;
import yvs.commercial.depot.PointVente;
import yvs.commercial.rrr.GrilleRabais;
import yvs.commercial.rrr.ManagedRemise;
import yvs.commercial.rrr.Remise;
import yvs.commercial.stock.CoutSupDoc;
import yvs.commercial.stock.ManagedReservation;
import yvs.commercial.stock.ManagedStockArticle;
import yvs.commercial.stock.ReservationStock;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBasePointLivraison;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.YvsComCommercialPoint;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.rrr.YvsComGrilleRemise;
import yvs.entity.commercial.rrr.YvsComGrilleRistourne;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.commercial.rrr.YvsComRemise;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.stock.YvsComReservationStock;
import yvs.entity.commercial.vente.YvsComCommercialVente;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComContenuDocVenteEtat;
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComDocVentesInformations;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.commercial.vente.YvsComRemiseDocVente;
import yvs.entity.commercial.vente.YvsComTaxeContenuVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.entity.print.YvsPrintFactureVente;
import yvs.entity.produits.YvsBaseArticleContenuPack;
import yvs.entity.produits.YvsBaseArticlePack;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ManagedTypeCout;
import yvs.grh.bean.TypeCout;
import yvs.grh.bean.TypeElementAdd;
import yvs.grh.paie.ManagedRetenue;
import yvs.grh.presence.TrancheHoraire;

import static yvs.init.Initialisation.FILE_SEPARATOR;

import yvs.parametrage.PlanPrelevement;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.parametrage.entrepot.Depots;
import yvs.parametrage.formulaire.ComposantsEditable;
import yvs.parametrage.formulaire.ComposantsObligatoire;
import yvs.parametrage.formulaire.ComposantsVisible;
import yvs.print.ManagedPrintFactureVente;
import yvs.service.IEntitySax;
import yvs.service.com.vente.IYvsComContenuDocVenteEtat;
import yvs.service.com.vente.IYvsComDocVentes;
import yvs.service.com.vente.IYvsComDocVentesInformations;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;

import static yvs.util.Managed.ldf;
import static yvs.util.Managed.time;

import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;
import yvs.util.Utilitaire;
import yvs.util.enume.Nombre;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedFactureVenteV2 extends ManagedCommercial<DocVente, YvsComDocVentes> implements Serializable {

    private final static Logger logger = Logger.getLogger(ManagedFactureVenteV2.class.getName());

    private YvsComEnteteDocVente selectEntete;
    private DocVente docVente = new DocVente();
    private List<YvsComDocVentes> documents, selections;
    private YvsComDocVentes docLie;
    private YvsComDocVentes selectDoc, retour;
    private YvsComClient defaultClient;
    private long bon;
    private String notes;
    private boolean changeNumeroWhenChangeDate = true, needConfirmation, validAndClear = false, majPrixContenu = false, cancelReglement = false;
    private Date dateEntete = new Date();
    private YvsComContenuDocVenteEtat selectEtat;
    private ContenuDocVenteEtat etat = new ContenuDocVenteEtat();
    private YvsComContenuDocVente selectContenuRetour = new YvsComContenuDocVente();

    private List<YvsComContenuDocVente> contenus_bcv, all_contenus, contenusRequireLot, selectContenus;
    private YvsComContenuDocVente selectContenu;
    private ContenuDocVente contenu = new ContenuDocVente();
    private ContenuDocVente bonus = new ContenuDocVente();
    private YvsBaseArticlePack pack = new YvsBaseArticlePack();
    public PaginatorResult<YvsComContenuDocVente> p_contenu = new PaginatorResult<>();
    private boolean on_rabais, remise_is_taux, rememberChoixWithOutStock = false, valueContinueWithOutStock;
    private long remiseContenu;
    private double montant_rabais, montant_rabais_total, montant_remise, taux_remise;
    private String commentaire, numSerie;

    private CommercialVente commercial = new CommercialVente();
    private YvsComCommercialVente selectCommercial;

    private CoutSupDoc cout = new CoutSupDoc();
    private YvsComCoutSupDocVente selectCout;

    private List<YvsGrhTrancheHoraire> tranches;
    private TrancheHoraire trancheChange = new TrancheHoraire();
    private List<YvsBaseTaxes> taxes;

    private List<YvsUsers> caissiers;
    private PieceTresorerie reglement = new PieceTresorerie(), avance = new PieceTresorerie();
    private YvsComptaCaissePieceVente selectReglement;
    private YvsComTaxeContenuVente selectedTaxe;
    private boolean checkAvance, onEncaisement;
    private boolean newMensualite;

    private RemiseDocVente remise = new RemiseDocVente();
    private List<YvsComRemiseDocVente> remisesFacture;
    private YvsComRemiseDocVente selectRemiseFacture;

    private String tabIds, tabIds_contenu, tabIds_cout, tabIds_mensualite, tabIds_remise, tabIds_commercial;
    private boolean correct = false, selectArt, isBon, listArt;
    private Date dateClean = new Date();

    //Parametre recherche
    private boolean paramDate = false, _first = true, toValideLoad = true, displayAuteurContenu = false, displayDepotContenu;
    private Date dateDebut = new Date(), dateFin = new Date();
    private String statut = null, numBon, egaliteDepot = "!=", egaliteStatut = "!=", egaliteStatutL = "=", egaliteStatutR = "=", typeSearch;
    private String operateuClt = " LIKE ", operateurVend = " LIKE ", operateurRef = " LIKE ";
    private Boolean comptaSearch, withTiersSearch, autoLivreSearch;
    private long nbrComptaSearch;
    private int heightFacture = 260;

    //Parametre recherche contenu
    private boolean dateContenu = false;
    private boolean memoriseActionCompta = false;
    private Date dateDebutContenu = new Date(), dateFinContenu = new Date();
    private String egaliteStatutLContenu = "=", statutLivreContenu;
    private String statutContenu, idsContenu, reference, article, articleContenu, pointvente, clientF, vendeurF;

    // Nombre d'element a afficher dans le selectOneMenu
//    private int subLenght;
    private Date dateLivraison = new Date();
    private String statutLivraison = Constantes.ETAT_VALIDE;
    private List<YvsBaseDepots> depotsLivraison;
    private List<YvsUsers> vendeurs;
    private List<YvsDictionnaire> lieux;

    private String motifEtape;
    YvsWorkflowValidFactureVente etape;
    private boolean lastEtape;

    @ManagedProperty(value = "#{composantsEditable}")
    private ComposantsEditable modelFormEditable;
    @ManagedProperty(value = "#{composantsVisible}")
    private ComposantsVisible modelFormCompVisible;
    @ManagedProperty(value = "#{composantsObligatoire}")
    private ComposantsObligatoire modelFormCompOblig;

    private boolean displayId, displayCodeClient;
    private boolean changeNumdocAuto = true;
    private boolean displayDetailClient = false, displayDetailReglement = false;
    private long nbreFacture = 0, agence;
    private boolean lotBL, lotPiece, lotFV, venteDirecte;
    protected String statutLotBL = Constantes.ETAT_VALIDE, statutLotPiece = Constantes.ETAT_REGLE, statutLotFV = Constantes.ETAT_VALIDE, natureVente = Constantes.VENTE, natureSearch;
    private String page = "V2";
    private List<String> statuts;

    private boolean listing, memoriserDeleteContenu = false;
    private boolean loadOnlyDepotPoint = true;

    private String model = "facture_vente_v1";
    private boolean withHeader = true;

    public ManagedFactureVenteV2() {
        contenus_bcv = new ArrayList<>();
        taxes = new ArrayList<>();
        documents = new ArrayList<>();
        selections = new ArrayList<>();
        all_contenus = new ArrayList<>();
        remisesFacture = new ArrayList<>();
        depotsLivraison = new ArrayList<>();
        tranches = new ArrayList<>();
        statuts = new ArrayList<>();
        vendeurs = new ArrayList<>();
        caissiers = new ArrayList<>();
        lieux = new ArrayList<>();
        contenusRequireLot = new ArrayList<>();
        selectContenus = new ArrayList<>();
        natureVente = Constantes.VENTE;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public boolean isCancelReglement() {
        return cancelReglement;
    }

    public void setCancelReglement(boolean cancelReglement) {
        this.cancelReglement = cancelReglement;
    }

    public List<YvsComDocVentes> getSelections() {
        return selections;
    }

    public void setSelections(List<YvsComDocVentes> selections) {
        this.selections = selections;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isWithHeader() {
        return withHeader;
    }

    public void setWithHeader(boolean withHeader) {
        this.withHeader = withHeader;
    }

    public YvsBaseArticlePack getPack() {
        return pack;
    }

    public void setPack(YvsBaseArticlePack pack) {
        this.pack = pack;
    }

    public YvsComClient getDefaultClient() {
        return defaultClient;
    }

    public void setDefaultClient(YvsComClient defaultClient) {
        this.defaultClient = defaultClient;
    }

    public YvsComContenuDocVenteEtat getSelectEtat() {
        return selectEtat;
    }

    public void setSelectEtat(YvsComContenuDocVenteEtat selectEtat) {
        this.selectEtat = selectEtat;
    }

    public ContenuDocVenteEtat getEtat() {
        return etat;
    }

    public void setEtat(ContenuDocVenteEtat etat) {
        this.etat = etat;
    }

    public YvsComContenuDocVente getSelectContenuRetour() {
        return selectContenuRetour;
    }

    public void setSelectContenuRetour(YvsComContenuDocVente selectContenuRetour) {
        this.selectContenuRetour = selectContenuRetour;
    }

    public boolean isValueContinueWithOutStock() {
        return valueContinueWithOutStock;
    }

    public void setValueContinueWithOutStock(boolean valueContinueWithOutStock) {
        this.valueContinueWithOutStock = valueContinueWithOutStock;
    }

    public YvsComTaxeContenuVente getSelectedTaxe() {
        return selectedTaxe;
    }

    public void setSelectedTaxe(YvsComTaxeContenuVente selectedTaxe) {
        this.selectedTaxe = selectedTaxe;
    }

    public boolean isMemoriseActionCompta() {
        return memoriseActionCompta;
    }

    public void setMemoriseActionCompta(boolean memoriseActionCompta) {
        this.memoriseActionCompta = memoriseActionCompta;
    }

    public YvsComDocVentes getRetour() {
        return retour;
    }

    public void setRetour(YvsComDocVentes retour) {
        this.retour = retour;
    }

    public boolean isMajPrixContenu() {
        return majPrixContenu;
    }

    public void setMajPrixContenu(boolean majPrixContenu) {
        this.majPrixContenu = majPrixContenu;
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

    public long getAgence() {
        return agence;
    }

    public void setAgence(long agence) {
        this.agence = agence;
    }

    public List<YvsComContenuDocVente> getContenusRequireLot() {
        return contenusRequireLot;
    }

    public void setContenusRequireLot(List<YvsComContenuDocVente> contenusRequireLot) {
        this.contenusRequireLot = contenusRequireLot;
    }

    public String getEgaliteDepot() {
        return egaliteDepot;
    }

    public void setEgaliteDepot(String egaliteDepot) {
        this.egaliteDepot = egaliteDepot;
    }

    public boolean isDisplayDetailReglement() {
        return displayDetailReglement;
    }

    public void setDisplayDetailReglement(boolean displayDetailReglement) {
        this.displayDetailReglement = displayDetailReglement;
    }

    public boolean isDisplayAuteurContenu() {
        return displayAuteurContenu;
    }

    public void setDisplayAuteurContenu(boolean displayAuteurContenu) {
        this.displayAuteurContenu = displayAuteurContenu;
    }

    public boolean isDisplayDepotContenu() {
        return displayDepotContenu;
    }

    public void setDisplayDepotContenu(boolean displayDepotContenu) {
        this.displayDepotContenu = displayDepotContenu;
    }

    public String getNatureSearch() {
        return natureSearch;
    }

    public void setNatureSearch(String natureSearch) {
        this.natureSearch = natureSearch;
    }

    public String getIdsContenu() {
        return idsContenu;
    }

    public void setIdsContenu(String idsContenu) {
        this.idsContenu = idsContenu;
    }

    public boolean isValidAndClear() {
        return validAndClear;
    }

    public void setValidAndClear(boolean validAndClear) {
        this.validAndClear = validAndClear;
    }

    public boolean isNeedConfirmation() {
        return needConfirmation;
    }

    public void setNeedConfirmation(boolean needConfirmation) {
        this.needConfirmation = needConfirmation;
    }

    public boolean isRemise_is_taux() {
        return remise_is_taux;
    }

    public void setRemise_is_taux(boolean remise_is_taux) {
        this.remise_is_taux = remise_is_taux;
    }

    public String getNatureVente() {
        return natureVente;
    }

    public void setNatureVente(String natureVente) {
        this.natureVente = natureVente;
    }

    public int getHeightFacture() {
        return heightFacture;
    }

    public void setHeightFacture(int heightFacture) {
        this.heightFacture = heightFacture;
    }

    public long getNbrComptaSearch() {
        return nbrComptaSearch;
    }

    public void setNbrComptaSearch(long nbrComptaSearch) {
        this.nbrComptaSearch = nbrComptaSearch;
    }

    public Date getDateClean() {
        return dateClean;
    }

    public void setDateClean(Date dateClean) {
        this.dateClean = dateClean;
    }

    public boolean isChangeNumeroWhenChangeDate() {
        return changeNumeroWhenChangeDate;
    }

    public void setChangeNumeroWhenChangeDate(boolean changeNumeroWhenChangeDate) {
        this.changeNumeroWhenChangeDate = changeNumeroWhenChangeDate;
    }

    public TrancheHoraire getTrancheChange() {
        return trancheChange;
    }

    public void setTrancheChange(TrancheHoraire trancheChange) {
        this.trancheChange = trancheChange;
    }

    public long getNbreFacture() {
        return nbreFacture;
    }

    public void setNbreFacture(long nbreFacture) {
        this.nbreFacture = nbreFacture;
    }

    public boolean isVenteDirecte() {
        return venteDirecte;
    }

    public void setVenteDirecte(boolean venteDirecte) {
        this.venteDirecte = venteDirecte;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public boolean isRememberChoixWithOutStock() {
        return rememberChoixWithOutStock;
    }

    public void setRememberChoixWithOutStock(boolean rememberChoixWithOutStock) {
        this.rememberChoixWithOutStock = rememberChoixWithOutStock;
    }

    public List<YvsUsers> getCaissiers() {
        return caissiers;
    }

    public void setCaissiers(List<YvsUsers> caissiers) {
        this.caissiers = caissiers;
    }

    public Boolean getAutoLivreSearch() {
        return autoLivreSearch;
    }

    public void setAutoLivreSearch(Boolean autoLivreSearch) {
        this.autoLivreSearch = autoLivreSearch;
    }

    public YvsComDocVentes getDocLie() {
        return docLie;
    }

    public void setDocLie(YvsComDocVentes docLie) {
        this.docLie = docLie;
    }

    public String getArticleContenu() {
        return articleContenu;
    }

    public void setArticleContenu(String articleContenu) {
        this.articleContenu = articleContenu;
    }

    public String getEgaliteStatutLContenu() {
        return egaliteStatutLContenu;
    }

    public void setEgaliteStatutLContenu(String egaliteStatutLContenu) {
        this.egaliteStatutLContenu = egaliteStatutLContenu;
    }

    public String getStatutLivreContenu() {
        return statutLivreContenu;
    }

    public void setStatutLivreContenu(String statutLivreContenu) {
        this.statutLivreContenu = statutLivreContenu;
    }

    public Boolean getWithTiersSearch() {
        return withTiersSearch;
    }

    public void setWithTiersSearch(Boolean withTiersSearch) {
        this.withTiersSearch = withTiersSearch;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public boolean isLastEtape() {
        return lastEtape;
    }

    public void setLastEtape(boolean lastEtape) {
        this.lastEtape = lastEtape;
    }

    public String getMotifEtape() {
        return motifEtape;
    }

    public void setMotifEtape(String motifEtape) {
        this.motifEtape = motifEtape;
    }

    public List<String> getStatuts() {
        return statuts;
    }

    public void setStatuts(List<String> statuts) {
        this.statuts = statuts;
    }

    public boolean isLotBL() {
        return lotBL;
    }

    public void setLotBL(boolean lotBL) {
        this.lotBL = lotBL;
    }

    public boolean isLotFV() {
        return lotFV;
    }

    public void setLotFV(boolean lotFV) {
        this.lotFV = lotFV;
    }

    public boolean isLotPiece() {
        return lotPiece;
    }

    public void setLotPiece(boolean lotPiece) {
        this.lotPiece = lotPiece;
    }

    public String getStatutLotBL() {
        return statutLotBL;
    }

    public void setStatutLotBL(String statutLotBL) {
        this.statutLotBL = statutLotBL;
    }

    public String getStatutLotFV() {
        return statutLotFV;
    }

    public void setStatutLotFV(String statutLotFV) {
        this.statutLotFV = statutLotFV;
    }

    public String getStatutLotPiece() {
        return statutLotPiece;
    }

    public void setStatutLotPiece(String statutLotPiece) {
        this.statutLotPiece = statutLotPiece;
    }

    public boolean isToValideLoad() {
        return toValideLoad;
    }

    public void setToValideLoad(boolean toValideLoad) {
        this.toValideLoad = toValideLoad;
    }

    public String getEgaliteStatutL() {
        return egaliteStatutL;
    }

    public void setEgaliteStatutL(String egaliteStatutL) {
        this.egaliteStatutL = egaliteStatutL;
    }

    public String getOperateuClt() {
        return operateuClt;
    }

    public void setOperateuClt(String operateuClt) {
        this.operateuClt = operateuClt;
    }

    public String getOperateurVend() {
        return operateurVend;
    }

    public void setOperateurVend(String operateurVend) {
        this.operateurVend = operateurVend;
    }

    public String getOperateurRef() {
        return operateurRef;
    }

    public void setOperateurRef(String operateurRef) {
        this.operateurRef = operateurRef;
    }

    public String getEgaliteStatutR() {
        return egaliteStatutR;
    }

    public void setEgaliteStatutR(String egaliteStatutR) {
        this.egaliteStatutR = egaliteStatutR;
    }

    public String getEgaliteStatut() {
        return egaliteStatut;
    }

    public void setEgaliteStatut(String egaliteStatut) {
        this.egaliteStatut = egaliteStatut;
    }

    public List<YvsComContenuDocVente> getAll_contenus() {
        return all_contenus;
    }

    public void setAll_contenus(List<YvsComContenuDocVente> all_contenus) {
        this.all_contenus = all_contenus;
    }

    public String getVendeurF() {
        return vendeurF;
    }

    public void setVendeurF(String vendeurF) {
        this.vendeurF = vendeurF;
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

    public String getPointvente() {
        return pointvente;
    }

    public void setPointvente(String pointvente) {
        this.pointvente = pointvente;
    }

    public String getClientF() {
        return clientF;
    }

    public void setClientF(String clientF) {
        this.clientF = clientF;
    }

    public boolean isIsBonus() {
        return isBonus;
    }

    public void setIsBonus(boolean isBonus) {
        this.isBonus = isBonus;
    }

    public PaginatorResult<YvsComContenuDocVente> getP_contenu() {
        return p_contenu;
    }

    public void setP_contenu(PaginatorResult<YvsComContenuDocVente> p_contenu) {
        this.p_contenu = p_contenu;
    }

    public ContenuDocVente getBonus() {
        return bonus;
    }

    public void setBonus(ContenuDocVente bonus) {
        this.bonus = bonus;
    }

    public boolean isDisplayId() {
        return displayId;
    }

    public void setDisplayId(boolean displayId) {
        this.displayId = displayId;
    }

    public boolean isDisplayCodeClient() {
        return displayCodeClient;
    }

    public void setDisplayCodeClient(boolean displayCodeClient) {
        this.displayCodeClient = displayCodeClient;
    }

    public Date getDateEntete() {
        return dateEntete;
    }

    public void setDateEntete(Date dateEntete) {
        this.dateEntete = dateEntete;
    }

    public long getRemiseContenu() {
        return remiseContenu;
    }

    public void setRemiseContenu(long remiseContenu) {
        this.remiseContenu = remiseContenu;
    }

    public String getTabIds_commercial() {
        return tabIds_commercial;
    }

    public void setTabIds_commercial(String tabIds_commercial) {
        this.tabIds_commercial = tabIds_commercial;
    }

    public CommercialVente getCommercial() {
        return commercial;
    }

    public void setCommercial(CommercialVente commercial) {
        this.commercial = commercial;
    }

    public YvsComCommercialVente getSelectCommercial() {
        return selectCommercial;
    }

    public void setSelectCommercial(YvsComCommercialVente selectCommercial) {
        this.selectCommercial = selectCommercial;
    }

    public ComposantsEditable getModelFormEditable() {
        return modelFormEditable;
    }

    public void setModelFormEditable(ComposantsEditable modelFormEditable) {
        this.modelFormEditable = modelFormEditable;
    }

    public ComposantsVisible getModelFormCompVisible() {
        return modelFormCompVisible;
    }

    public void setModelFormCompVisible(ComposantsVisible modelFormCompVisible) {
        this.modelFormCompVisible = modelFormCompVisible;
    }

    public ComposantsObligatoire getModelFormCompOblig() {
        return modelFormCompOblig;
    }

    public void setModelFormCompOblig(ComposantsObligatoire modelFormCompOblig) {
        this.modelFormCompOblig = modelFormCompOblig;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getStatutLivraison() {
        return statutLivraison;
    }

    public void setStatutLivraison(String statutLivraison) {
        this.statutLivraison = statutLivraison;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public YvsWorkflowValidFactureVente getCurrentEtape() {
        return currentEtape;
    }

    public void setCurrentEtape(YvsWorkflowValidFactureVente currentEtape) {
        this.currentEtape = currentEtape;
    }

    public boolean isFirst() {
        return _first;
    }

    public void setFirst(boolean _first) {
        this._first = _first;
    }

    public double getMontant_rabais_total() {
        return montant_rabais_total;
    }

    public void setMontant_rabais_total(double montant_rabais_total) {
        this.montant_rabais_total = montant_rabais_total;
    }

    public String getTabIds_cout() {
        return tabIds_cout;
    }

    public void setTabIds_cout(String tabIds_cout) {
        this.tabIds_cout = tabIds_cout;
    }

    public CoutSupDoc getCout() {
        return cout;
    }

    public void setCout(CoutSupDoc cout) {
        this.cout = cout;
    }

    public YvsComCoutSupDocVente getSelectCout() {
        return selectCout;
    }

    public void setSelectCout(YvsComCoutSupDocVente selectCout) {
        this.selectCout = selectCout;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public long getBon() {
        return bon;
    }

    public void setBon(long bon) {
        this.bon = bon;
    }

    public double getTaux_remise() {
        return taux_remise;
    }

    public void setTaux_remise(double taux_remise) {
        this.taux_remise = taux_remise;
    }

    public List<YvsBaseDepots> getDepotsLivraison() {
        return depotsLivraison;
    }

    public void setDepotsLivraison(List<YvsBaseDepots> depotsLivraison) {
        this.depotsLivraison = depotsLivraison;
    }

    public List<YvsBaseTaxes> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsBaseTaxes> taxes) {
        this.taxes = taxes;
    }

    public PieceTresorerie getReglement() {
        return reglement;
    }

    public void setReglement(PieceTresorerie reglement) {
        this.reglement = reglement;
    }

    public PieceTresorerie getAvance() {
        return avance;
    }

    public void setAvance(PieceTresorerie avance) {
        this.avance = avance;
    }

    public YvsComptaCaissePieceVente getSelectReglement() {
        return selectReglement;
    }

    public void setSelectReglement(YvsComptaCaissePieceVente selectReglement) {
        this.selectReglement = selectReglement;
    }

    public boolean isOnEncaisement() {
        return onEncaisement;
    }

    public void setOnEncaisement(boolean onEncaisement) {
        this.onEncaisement = onEncaisement;
    }

    public double getMontant_remise() {
        return montant_remise;
    }

    public void setMontant_remise(double montant_remise) {
        this.montant_remise = montant_remise;
    }

    public double getMontant_rabais() {
        return montant_rabais;
    }

    public void setMontant_rabais(double montant_rabais) {
        this.montant_rabais = montant_rabais;
    }

    public boolean isOn_rabais() {
        return on_rabais;
    }

    public void setOn_rabais(boolean on_rabais) {
        this.on_rabais = on_rabais;
    }

    public boolean isNewMensualite() {
        return newMensualite;
    }

    public void setNewMensualite(boolean newMensualite) {
        this.newMensualite = newMensualite;
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

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public boolean isCheckAvance() {
        return checkAvance;
    }

    public void setCheckAvance(boolean checkAvance) {
        this.checkAvance = checkAvance;
    }

    public boolean isIsBon() {
        return isBon;
    }

    public void setIsBon(boolean isBon) {
        this.isBon = isBon;
    }

    public List<YvsComContenuDocVente> getContenus_bcv() {
        return contenus_bcv;
    }

    public void setContenus_bcv(List<YvsComContenuDocVente> contenus_bcv) {
        this.contenus_bcv = contenus_bcv;
    }

    public List<YvsComDocVentes> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocVentes> documents) {
        this.documents = documents;
    }

    public YvsComContenuDocVente getSelectContenu() {
        return selectContenu;
    }

    public void setSelectContenu(YvsComContenuDocVente selectContenu) {
        this.selectContenu = selectContenu;
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

    public boolean isParamDate() {
        return paramDate;
    }

    public void setParamDate(boolean paramDate) {
        this.paramDate = paramDate;
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

    public RemiseDocVente getRemise() {
        return remise;
    }

    public void setRemise(RemiseDocVente remise) {
        this.remise = remise;
    }

    public List<YvsComRemiseDocVente> getRemisesFacture() {
        return remisesFacture;
    }

    public void setRemisesFacture(List<YvsComRemiseDocVente> remisesFacture) {
        this.remisesFacture = remisesFacture;
    }

    public YvsComRemiseDocVente getSelectRemiseFacture() {
        return selectRemiseFacture;
    }

    public void setSelectRemiseFacture(YvsComRemiseDocVente selectRemiseFacture) {
        this.selectRemiseFacture = selectRemiseFacture;
    }

    public String getTabIds_remise() {
        return tabIds_remise;
    }

    public void setTabIds_remise(String tabIds_remise) {
        this.tabIds_remise = tabIds_remise;
    }

    public String getTabIds_mensualite() {
        return tabIds_mensualite;
    }

    public void setTabIds_mensualite(String tabIds_mensualite) {
        this.tabIds_mensualite = tabIds_mensualite;
    }

    public DocVente getDocVente() {
        return docVente;
    }

    public void setDocVente(DocVente docVente) {
        this.docVente = docVente;
    }

    public ContenuDocVente getContenu() {
        return contenu;
    }

    public void setContenu(ContenuDocVente contenu) {
        this.contenu = contenu;
    }

    public String getTabIds_contenu() {
        return tabIds_contenu;
    }

    public void setTabIds_contenu(String tabIds_contenu) {
        this.tabIds_contenu = tabIds_contenu;
    }

    public boolean getCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public YvsComEnteteDocVente getSelectEntete() {
        return selectEntete;
    }

    public void setSelectEntete(YvsComEnteteDocVente selectEntete) {
        this.selectEntete = selectEntete;
    }

    public YvsComDocVentes getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComDocVentes selectDoc) {
        this.selectDoc = selectDoc;
        setDateEntete(selectDoc.getEnteteDoc().getDateEntete());
    }

    public boolean isDisplayDetailClient() {
        return displayDetailClient;
    }

    public void setDisplayDetailClient(boolean displayDetailClient) {
        this.displayDetailClient = displayDetailClient;
    }

    public List<YvsUsers> getVendeurs() {
        return vendeurs;
    }

    public void setVendeurs(List<YvsUsers> vendeurs) {
        this.vendeurs = vendeurs;
    }

    public boolean isChangeNumdocAuto() {
        return changeNumdocAuto;
    }

    public void setChangeNumdocAuto(boolean changeNumdocAuto) {
        this.changeNumdocAuto = changeNumdocAuto;
    }

    public List<YvsDictionnaire> getLieux() {
        return lieux;
    }

    public void setLieux(List<YvsDictionnaire> lieux) {
        this.lieux = lieux;
    }

    public boolean isListing() {
        return listing;
    }

    public void setListing(boolean listing) {
        this.listing = listing;
    }

    public boolean isLoadOnlyDepotPoint() {
        return loadOnlyDepotPoint;
    }

    public void setLoadOnlyDepotPoint(boolean loadOnlyDepotPoint) {
        this.loadOnlyDepotPoint = loadOnlyDepotPoint;
    }

    public void initView(boolean venteDirecte) {
        initView(venteDirecte, Constantes.VENTE);
    }

    public void initView(boolean venteDirecte, String natureVente) {
        initView(venteDirecte, natureVente, "V2");
    }

    public void initView(boolean venteDirecte, String natureVente, String page) {
        this.page = page;
        this.natureVente = natureVente;
        if (docVente != null ? (docVente.getClient().getId() < 1 && docVente.getCategorieComptable().getId() < 1) : false) {
            docVente.setNature(natureVente != null ? !natureVente.trim().isEmpty() ? natureVente : Constantes.VENTE : Constantes.VENTE);
        }
        setClientDefaut(); //(Ok)
        this.venteDirecte = venteDirecte;
        initView();
        if (docVente != null ? docVente.getId() < 1 : false && currentParamVente != null) {
            docVente.setLivraisonAuto(currentParamVente.getLivraisonAuto());
        }
        if (venteDirecte) {
            if (natureVente != null ? !natureVente.trim().isEmpty() : false) {
                champ = new String[]{"vendeur", "typeDoc", "nature"};
                val = new Object[]{currentUser.getUsers(), Constantes.TYPE_FV, natureVente};
                Long count = (Long) dao.loadObjectByNameQueries("YvsComDocVentes.countByVendeurNature", champ, val);
                nbreFacture = count != null ? count : 0;
            }
            docVente.setLivraisonAuto(true);
        }
        lieux = dao.loadNameQueries("YvsDictionnaire.findSecteurs", new String[]{}, new Object[]{});
    }

    @Override
    public void loadAll() {
        initView(false);
        initDroit();
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {

        }
    }

    public void collapseContenu() {
        if (isWarning ? (modelWarning != null ? modelWarning.equals("LOWER_MARGIN") : false) : false) {
            openDlgFactures(true);
            execute("collapseGrid('facture_vente')");
        }
        loadInfosWarning(true);
    }

    private void loadByWarning() {
        if (modelWarning != null ? modelWarning.equals("LOWER_MARGIN") : false) {
            idsContenu = idsSearch;
            idsSearch = "";
            p_contenu.clear();
            addParamIdsContenus();
        } else {
            paginator.clear();
            addParamIds(true);
            loadAllFacture(true);
        }
    }

    public void load(Boolean livraison) {

    }

    public void initView() {
        loadInfosWarning(false);
        if (((docVente != null) ? (docVente.getClient().getId() < 1 && docVente.getCategorieComptable().getId() < 1) : true)) {
            docVente = new DocVente();
            docVente.setTypeDoc(Constantes.TYPE_FV);
            if (docVente.getDocumentLie() == null) {
                docVente.setDocumentLie(new DocVente());
            }
            docVente.setNature(natureVente != null ? !natureVente.trim().isEmpty() ? natureVente : Constantes.VENTE : Constantes.VENTE);
            numSearch_ = "";
            docVente.setEnteteDoc(new EnteteDocVente());

        }
        if (docVente.getEnteteDoc().getCrenauHoraire() != null ? docVente.getEnteteDoc().getCrenauHoraire().getId_() < 1 : true) {
            if (currentPlanning != null ? !currentPlanning.isEmpty() : false) {
                docVente.getEnteteDoc().setCrenauHoraire(UtilCom.buildBeanCreneauUsers(currentPlanning.get(0)));
            }
        }
        if (docVente.getEnteteDoc().getPoint() != null ? docVente.getEnteteDoc().getPoint().getId() < 1 : true) {
            if (currentPlanning != null ? !currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauPoint() != null : false : false) {
                YvsBasePointVente p = currentPlanning.get(0).getCreneauPoint().getPoint();
                docVente.getEnteteDoc().setPoint(UtilCom.buildSimpleBeanPointVente(p));
                choosePoint(p);
            }
        } else if (depotsLivraison.isEmpty()) {
            loadDepotByPoint(new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId()));
        }
        if (docVente.getDepot() != null ? docVente.getDepot().getId() < 1 : true) {
            if (!depotsLivraison.isEmpty()) {
                if (currentPlanning != null ? !currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false : false) {
                    if (depotsLivraison.contains(currentPlanning.get(0).getCreneauDepot().getDepot())) {
                        docVente.setDepot(UtilCom.buildSimpleBeanDepot(currentPlanning.get(0).getCreneauDepot().getDepot()));
                    } else {
                        docVente.setDepot(UtilCom.buildSimpleBeanDepot(depotsLivraison.get(0)));
                    }
                } else {
                    docVente.setDepot(UtilCom.buildSimpleBeanDepot(depotsLivraison.get(0)));
                }
            }
            if (!venteDirecte) {
                chooseDepot();
            }
        }
        if (docVente.getTranche() != null ? docVente.getTranche().getId() < 1 : true) {
            if (currentPlanning != null ? !currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false : false) {
                docVente.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
            }
        }
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service != null) {
            YvsBaseCaisse y = service.findByResponsable(currentUser.getUsers());
            reglement.setCaisse(UtilCompta.buildSimpleBeanCaisse(y));
            loadCaissiers(y);
        }

        if (docVente.getEnteteDoc() != null ? docVente.getEnteteDoc().getId() > 0 : false) {
            ManagedPointVente w = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (w != null) {
                YvsBasePointVente y = new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId());
                int idx = w.getPointsvente().indexOf(y);
                if (idx < 0) {
                    y = (YvsBasePointVente) dao.loadOneByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{y.getId()});
                    w.getPointsvente().add(y);
                }
            }
        }
        if (currentParam == null) {
            currentParam = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        }
        currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        _first = true;
        if (reglement != null ? reglement.getId() < 1 : true) {
            reglement = new PieceTresorerie();
        }
        if (reglement.getMode() != null ? reglement.getMode().getId() < 1 : true) {
            reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        }
        if (agence_ < 1) {
            agence_ = currentAgence.getId();
        }
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

    public void loadContenus(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_FV)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                p_contenu.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.agence", "agence", currentAgence, "=", "AND");
                p_contenu.addParam(p);
                break;
            case 3: { //charge tous les document des points de vente où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"}, new Object[]{currentUser.getUsers(), (Utilitaire.getIniTializeDate(new Date()).getTime()), Constantes.getPreviewDate(new Date())});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, "IN", "AND");
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
                p = new ParametreRequete("y.docVente.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, "IN", "AND");
                p_contenu.addParam(p);
                break;
            }
            default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                p = new ParametreRequete("y.docVente.enteteDoc.creneau.users", "users", currentUser.getUsers(), "=", "AND");
                p_contenu.addParam(p);
                break;
        }

        p_contenu.addParam(new ParametreRequete("y.docVente.typeDoc", "typeDoc", Constantes.TYPE_FV, "=", "AND"));
        String orderBy = "y.docVente.enteteDoc.dateEntete DESC, y.docVente.numDoc";
        all_contenus = p_contenu.executeDynamicQuery("YvsComContenuDocVente", orderBy, avance, init, dao);
        update("data_contenu_fv");
    }

    boolean initForm = true;

    private boolean initDroit() {
        if (currentAgence != null ? currentAgence.getSociete() != null ? currentAgence.getSociete().getId() < 1 : true : true) {
            all_contenus.clear();
            documents.clear();
            getErrorMessage("Session expiré. Veuillez actualiser la page");
            return false;
        }
        ParametreRequete p;
        int action = buildDocByDroit(Constantes.TYPE_FV);
        switch (action) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                paginator.addParam(p);
                p_contenu.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                controlListAgence();
                p = new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.point.agence", "agences", listIdAgences, "IN", "AND");
                p_contenu.addParam(p);
                paginator.addParam(p);
                break;
            case 3: { //charge tous les document des points de vente où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"}, new Object[]{currentUser.getUsers(), (Utilitaire.getIniTializeDate(new Date()).getTime()), Constantes.getPreviewDate(new Date())});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                p_contenu.addParam(p);
                paginator.addParam(p);
                break;
            }
            case 4: {//charge tous les document des depots où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur rattaché au depot
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
                p = new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                p_contenu.addParam(p);
                paginator.addParam(p);
                break;
            }
            default:    //charge les document de l'utilisateur connecté dans les restriction de paramDate données
                p = new ParametreRequete(pre + "enteteDoc.creneau.users ", "users", currentUser.getUsers(), "=", "AND");
                p_contenu.addParam(p);
                paginator.addParam(p);
                break;
        }
        return true;
    }

    public void addParamNonComptabiliser() {
        paginator.addParam(new ParametreRequete("CO.id", "idCompta", "X", " IS NULL", "AND"));
        loadAllFacture(true);
    }

    private String pre = "y.";

    public void loadAllFacture(boolean avance) {
        boolean init = initDroit();
        if (!init) {
            return;
        }
        addParamType(false);
        String query;
        if (listing) {
            query = "YvsComContenuDocVente y JOIN FETCH y.article JOIN FETCH y.conditionnement "
                    + "JOIN FETCH y.conditionnement.unite "
                    + "JOIN FETCH y.docVente ";
        } else {
            query = "YvsComDocVentes y ";
        }
        query += "LEFT JOIN FETCH " + pre + "adresse "
                + "LEFT JOIN FETCH " + pre + "depotLivrer "
                + "LEFT JOIN FETCH " + pre + "trancheLivrer "
                + "LEFT JOIN FETCH " + pre + "author "
                + "LEFT JOIN FETCH " + pre + "author.users "
                + "JOIN FETCH " + pre + "categorieComptable "
                + "JOIN FETCH " + pre + "client "
                + "JOIN FETCH " + pre + "enteteDoc "
                + "JOIN FETCH " + pre + "enteteDoc.creneau  "
                + "JOIN FETCH " + pre + "enteteDoc.creneau.creneauPoint "
                + "JOIN FETCH " + pre + "enteteDoc.creneau.creneauPoint.point ";
        if (listing) {
            p_contenu.addParam(new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            if (natureVente != null ? !natureVente.trim().isEmpty() : false) {
                ParametreRequete p = new ParametreRequete(null, "nature", natureVente, "=", "AND");
                p.getOtherExpression().add(new ParametreRequete(pre + "nature", "nature", natureVente, "=", "OR"));
                p.getOtherExpression().add(new ParametreRequete(pre + "nature", "nature", "IS NULL", "IS NULL", "OR"));
                p_contenu.addParam(p);
            }
            if (currentAgence != null ? currentAgence.getSociete() != null ? currentAgence.getSociete().getId() > 0 : false : false) {
                all_contenus = p_contenu.executeDynamicQuery("y", "y", query,
                        pre + "enteteDoc.dateEntete DESC, " + pre + "id, " + pre + "heureDoc DESC, " + pre + "numDoc DESC", avance, initForm, (int) imax, dao);
            } else {
                all_contenus.clear();
                getErrorMessage("Session expiré. Veuillez actualiser la page");
            }
            update("data_contenu_fv");
        } else {
            paginator.addParam(new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            if (natureVente != null ? !natureVente.trim().isEmpty() : false) {
                ParametreRequete p = new ParametreRequete(null, "nature", natureVente, "=", "AND");
                p.getOtherExpression().add(new ParametreRequete(pre + "nature", "nature", Constantes.VENTE, natureVente.equals(Constantes.VENTE) ? "=" : "!=", "OR"));
                p.getOtherExpression().add(new ParametreRequete(pre + "nature", "nature", "IS NULL", "IS NULL", "OR"));
                paginator.addParam(p);
            }
            if (currentAgence != null ? currentAgence.getSociete() != null ? currentAgence.getSociete().getId() > 0 : false : false) {
                documents = paginator.executeDynamicQuery("y", "y", query,
                        pre + "enteteDoc.dateEntete DESC, " + pre + "heureDoc DESC, " + pre + "numDoc DESC", avance, initForm, (int) imax, dao);
            } else {
                documents.clear();
                getErrorMessage("Session expiré. Veuillez actualiser la page");
            }
            update("data_facture_vente");
        }
        if (!venteDirecte) {
            if (documents != null ? documents.size() == 1 : false) {
                onSelectObject(documents.get(0));
                execute("collapseForm('facture_vente')");
            } else {
                if (!listing) {
                    execute("collapseList('facture_vente')");
                } else {
                    execute("collapseGrid('facture_vente')");
                }
            }
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        parcoursInAllResult(avancer, true);
    }

    public void parcoursInAllResult(boolean avancer, boolean complet) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComDocVentes> re = paginator.parcoursDynamicData("YvsComDocVentes", "y", "y.enteteDoc.dateEntete DESC, y.heureDoc DESC, y.numDoc DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0), complet);
        }
    }

    public void loadFactureNonLivre(boolean avance, boolean init) {
        if (_first) {
            clearParams();
        }
        _first = false;
        egaliteStatut = egaliteStatutL = egaliteStatutR = "=";
        operateuClt = operateurRef = operateurVend = " LIKE ";
        statutLivre_ = Constantes.ETAT_ATTENTE;
        paginator.addParam(new ParametreRequete("y.statutLivre", "statutLivre", Constantes.ETAT_LIVRE, "!=", "AND"));
        initForm = init;
        loadAllFacture(avance);
    }

    public void loadContenusNonLivre(boolean load) {
        egaliteStatutLContenu = "=";
        statutLivreContenu = Constantes.ETAT_ATTENTE;
        paginator.addParam(new ParametreRequete("y.docVente.statutLivre", "statutLivre", Constantes.ETAT_LIVRE, "!=", "AND"));
        if (load) {
            loadContenus(true, true);
        }
    }

    public void loadContenusNonLivre(boolean avance, boolean init) {
        egaliteStatutLContenu = "=";
        statutLivreContenu = Constantes.ETAT_ATTENTE;
        paginator.addParam(new ParametreRequete("y.docVente.statutLivre", "statutLivre", Constantes.ETAT_LIVRE, "!=", "AND"));
        loadContenus(avance, init);
    }

    public void loadFactureNonRegle(boolean avance, boolean init) {
        if (_first) {
            clearParams();
        }
        _first = false;
        statutRegle_ = Constantes.ETAT_ATTENTE;
        paginator.addParam(new ParametreRequete("y.statutRegle", "statutRegle", Constantes.ETAT_REGLE, "!=", "AND"));
        initForm = init;
        loadAllFacture(avance);
    }

    public void loadFactureRegle(boolean avance, boolean init) {
        if (_first) {
            clearParams();
        }
        _first = false;
        statutRegle_ = Constantes.ETAT_REGLE;
        paginator.addParam(new ParametreRequete("y.statutRegle", "statutRegle", Constantes.ETAT_REGLE, "=", "AND"));
        initForm = init;
        loadAllFacture(avance);
    }

    public void loadFactureStatut(String statut) {
        if (_first) {
            clearParams();
        }
        _first = false;
        statut_ = statut;
        paginator.addParam(new ParametreRequete("y.statut", "statut", statut, "=", "AND"));
        initForm = true;
        loadAllFacture(true);
    }

    public void loadFactureTypeStatut(String type, String statut) {
        if (_first) {
            clearParams();
        }
        _first = false;
        statut_ = statut;
        typeSearch = type;
        paginator.addParam(new ParametreRequete("y.statut", "statut", statut, "=", "AND"));
        addParamType();
    }

    public void loadFactureType(String typeDoc) {
        if (_first) {
            clearParams();
        }
        _first = false;
        typeSearch = typeDoc;
        addParamType();
    }

    public void clearParams(String statut) {
        _first = true;
        loadFactureStatut(statut);
    }

    public void clearParams() {
        codeClient_ = null;
        entete_ = 0;
        idsSearch = "";
        idsContenu = "";
        tranche_ = 0;
        point_ = 0;
        numSearch_ = null;
        statut = null;
        codeVendeur_ = null;
        statutLivre_ = null;
        statutRegle_ = null;
        cloturer_ = null;
        withTiersSearch = null;
        paramDate = false;
        dateDebut = new Date();
        dateFin = new Date();
        date_ = false;
        toValideLoad = false;
        dateDebut_ = new Date();
        dateFin_ = new Date();
        natureSearch = null;
        p_contenu.getParams().clear();
        paginator.getParams().clear();
        _first = true;
        initForm = true;
//        loadAllFacture(true);
        addParamAgence();
        update("blog_entete_facture_vente");
    }

    public int colonneContenu() {
        ComposantsVisible w = (ComposantsVisible) giveManagedBean(ComposantsVisible.class);
        int colonne = 5;
        if (contenu.getRabais() > 0) {
            colonne += 1;
        }
        if (w != null) {
            if (w.fv_conditionnement) {
                colonne += 1;
            }
            if (w.fv_depot_content) {
                colonne += 1;
            }
        }
        return colonne;
    }

    private void loadOthersDetailDoc(YvsComDocVentes y) {
        loadRemise(y);
//        loadTaxesVente(y);
//        docVente.setContenus(loadContenus(y));
//        docVente.setCouts(loadCouts(y));
        if (page.equals("V3")) {
            update("blog_form_contenu_facture_vente");
            update("blog_form_cout_facture_vente");
            update("form_mensualite_facture_vente");
        } else {
            update("tabview_facture_vente:blog_form_contenu_facture_vente");
            update("tabview_facture_vente:blog_form_cout_facture_vente");
            update("tabview_facture_vente:form_mensualite_facture_vente");
        }
        update("data_livraison_facture_vente");
        update("blog_commercial_vente");
    }

    private void loadRemise(YvsComDocVentes y) {
        nameQueri = "YvsComRemiseDocVente.findByDocVente";
        champ = new String[]{"docVente"};
        val = new Object[]{y};
//        remisesFacture = dao.loadNameQueries(nameQueri, champ, val);
//        List<YvsComRemiseDocVente> l = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComRemiseDocVente r : remisesFacture) {
            r.setMontant(getMontantRemise(r.getRemise(), y));
        }
        update("data_remise_vente");
    }

    public void loadTaxesVente(YvsComDocVentes y) {
        taxes.clear();
        List<Object[]> l = dao.getTaxeVente(y.getId());
        for (Object[] o : l) {
            YvsBaseTaxes t = (YvsBaseTaxes) dao.loadOneByNameQueries("YvsBaseTaxes.findById", new String[]{"id"}, new Object[]{o[0]});
            if (t != null ? t.getId() > 0 : false) {
                t.setTaux((double) o[1]);
                taxes.add(t);
            }
        }
        update("data_taxes_facture_vente");
    }

    public void loadTaxesVente(YvsComContenuDocVente y) {
        contenu.setName(y.getArticle().getDesignation());
        contenu.setTaxes(new ArrayList<YvsComTaxeContenuVente>());
        contenu.getTaxes().addAll(y.getTaxes());
        ManagedTaxes service = (ManagedTaxes) giveManagedBean(ManagedTaxes.class);
        if (service != null) {
            service.loadAllTaxes("V");
            List<YvsBaseTaxes> l = new ArrayList<>(service.getTaxesList());
            for (YvsComTaxeContenuVente tc : y.getTaxes()) {
                l.remove(tc.getTaxe());
            }
            YvsComTaxeContenuVente taxe;
            for (YvsBaseTaxes t : l) {
                if (t.getActif()) {
                    taxe = new YvsComTaxeContenuVente();
                    taxe.setAuthor(currentUser);
                    taxe.setTaxe(t);
                    taxe.setContenu(y);
                    taxe.setMontant(0d);
                    contenu.getTaxes().add(taxe);
                }
            }
        }
        update("tbl_data_taxes");
    }

    protected List<YvsComContenuDocVente> loadContenusStay(YvsComDocVentes y, String type) {
        List<YvsComContenuDocVente> list = new ArrayList<>();
        y.setInt_(false);
        List<YvsComContenuDocVente> contenus = dao.loadNameQueries("YvsComContenuDocVente.findByDocVenteCanSend", new String[]{"docVente"}, new Object[]{y});
        String[] ch = new String[]{"parent", "typeDoc", "statut"};
        Object[] v;
        Double qte;
        for (YvsComContenuDocVente c : contenus) {
            v = new Object[]{c, type, Constantes.ETAT_VALIDE};
            qte = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByTypeStatutParent", ch, v);
            if (c.getQuantite() > (qte != null ? qte : 0)) {
                c.setQuantite_(c.getQuantite());
                c.setQuantite(c.getQuantite() - (qte != null ? qte : 0));
                c.setPrixTotal(c.getPrix() * c.getQuantite());
                c.setParent(new YvsComContenuDocVente(c.getId()));
                c.getEtats().clear();
                int index = contenusRequireLot.indexOf(c);
                if (index > -1) {
                    c.setLots(contenusRequireLot.get(index).getLots());
                }
                list.add(c);
            }
        }
        return list;
    }

    public List<YvsComContenuDocVente> loadContenusStayForRetour(YvsComDocVentes y) {
        List<YvsComContenuDocVente> list = new ArrayList<>();
        y.setInt_(false);
        nameQueri = "YvsComContenuDocVente.findByDocVente";
        champ = new String[]{"docVente"};
        val = new Object[]{y};
        List<YvsComContenuDocVente> contenus = dao.loadNameQueries(nameQueri, champ, val);
        Double qteRetour;
        Double qteLivre;
        for (YvsComContenuDocVente c : contenus) {
            champ = new String[]{"parent", "typeDoc", "statut"};
            val = new Object[]{c, Constantes.TYPE_BLV, Constantes.ETAT_VALIDE};
            qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByTypeStatutParent", champ, val);
            val = new Object[]{c, Constantes.TYPE_BRL, Constantes.ETAT_VALIDE};
            qteRetour = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByTypeStatutParent", champ, val);
            if (c.getQuantite() > (qteRetour != null ? qteRetour : 0)) {
                c.setQuantite_(c.getQuantite());
                c.setQteLivree(qteLivre != null ? qteLivre : 0);
                c.setQuantite(c.getQteLivree() - (qteRetour != null ? qteRetour : 0));
                c.setPrixTotal(c.getPrix() * c.getQuantite());
                c.setParent(new YvsComContenuDocVente(c.getId()));
                c.setId(Long.valueOf(-(list.size() + 1)));
                c.getEtats().clear();
                list.add(c);
            }
        }
        return list;
    }

    public void loadDepotByPointById(long id) {
        loadDepotByPoint(new YvsBasePointVente(id));
    }

    public void loadDepotByPoint(YvsBasePointVente y) {
        loadDepotByPoint(null, y);
    }

    public void loadDepotByPoint(String code, YvsBasePointVente y) {
        PaginatorResult<YvsBaseDepots> p_depot = new PaginatorResult<>();
        p_depot.addParam(new ParametreRequete("y.pointVente.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        if (!autoriser("fv_livrer_in_all_depot")) {
            p_depot.addParam(new ParametreRequete("y.pointVente", "point", y, "=", "AND"));
        } else {
            controlListAgence();
            p_depot.addParam(new ParametreRequete("y.pointVente.agence.id", "agences", listIdAgences, "IN", "AND"));
        }
        if (Util.asString(code)) {
            ParametreRequete p = new ParametreRequete(null, "code", code.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.depot.code)", "code", code.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.depot.designation)", "designation", code.trim().toUpperCase() + "%", "LIKE", "OR"));
            p_depot.addParam(p);
        }
        p_depot.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        p_depot.addParam(new ParametreRequete("y.depot.actif", "actifDepot", true, "=", "AND"));
        if (loadOnlyDepotPoint) {
            if (docVente.getEnteteDoc().getCrenauHoraire().getPersonnel().getId() > 0) {
                //récupère les id des dépôts où je suis planifié            
                List<Long> l = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{new YvsUsers(docVente.getEnteteDoc().getCrenauHoraire().getPersonnel().getId())});
                if (l != null ? !l.isEmpty() : false) {
                    p_depot.addParam(new ParametreRequete("y.depot.id", "ids", l, " IN ", "AND"));
                }
            }
        } else {
            p_depot.addParam(new ParametreRequete("y.depot.id", "ids", null, " IN ", "AND"));
        }
        depotsLivraison = p_depot.executeDynamicQuery("DISTINCT(y.depot)", "DISTINCT(y.depot)", "YvsBasePointVenteDepot y", "y.principal DESC , y.depot.code", true, true, 0, dao);
        if (depotsLivraison != null ? !depotsLivraison.isEmpty() : false) {
            if (currentPlanning != null ? !currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false : false) {
                if (depotsLivraison.contains(currentPlanning.get(0).getCreneauDepot().getDepot())) {
                    docVente.setDepot(UtilCom.buildSimpleBeanDepot(currentPlanning.get(0).getCreneauDepot().getDepot()));
                    loadAllTranche(currentPlanning.get(0).getCreneauDepot().getDepot(), docVente.getDateLivraisonPrevu());
                } else {
                    docVente.setDepot(UtilCom.buildSimpleBeanDepot(depotsLivraison.get(0)));
                    loadAllTranche(depotsLivraison.get(0), docVente.getDateLivraisonPrevu());
                }
            } else {
                docVente.setDepot(UtilCom.buildSimpleBeanDepot(depotsLivraison.get(0)));
                loadAllTranche(depotsLivraison.get(0), docVente.getDateLivraisonPrevu());
            }
        } else {
            docVente.setDepot(new Depots());
        }
        update("data_depot_facture_vente");
        update("select_depot_liv_prevu");
        update("select_tranche_livraison_fv");
    }
//

    public void loadAllTranche(YvsBaseDepots depot, Date date) {
        if (docVente.getDepot() != null ? depot != null ? docVente.getDepot().getId() != depot.getId() : true : true) {
            docVente.setTranche(new TrancheHoraire());
        }
        tranches = loadTranche(depot, date);
        if (tranches != null ? !tranches.isEmpty() : false) {
            if (docVente.getTranche() != null ? docVente.getTranche().getId() < 0 : false) {
                docVente.setTranche(UtilCom.buildBeanTrancheHoraire(tranches.get(0)));
            } else {
                if (!tranches.contains(new YvsGrhTrancheHoraire(docVente.getTranche().getId()))) {
                    docVente.setTranche(UtilCom.buildBeanTrancheHoraire(tranches.get(0)));
                }
            }
        }
        update("data_tranche_fv");
    }

    public YvsComptaCaissePieceDivers buildPieceTresorerie(PieceTresorerie y) {
        YvsComptaCaissePieceDivers p = new YvsComptaCaissePieceDivers();
        if (y != null) {
            p.setDatePiece((y.getDatePiece() != null) ? y.getDatePiece() : new Date());
            p.setId(y.getId());
            p.setMontant(y.getMontant());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                p.setAuthor(currentUser);
            }
        }
        return p;
    }

    public YvsComRemiseDocVente buildRemiseDocVente(RemiseDocVente y) {
        YvsComRemiseDocVente r = new YvsComRemiseDocVente();
        if (y != null) {
            r.setId(y.getId());
            if ((y.getRemise() != null) ? y.getRemise().getId() > 0 : false) {
                r.setRemise(new YvsComRemise(y.getRemise().getId(), y.getRemise().getReference()));
            }
            r.setDocVente(selectDoc);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
        }
        return r;
    }

    public YvsComRemise buildRemise(Remise y) {
        YvsComRemise r = new YvsComRemise();
        if (y != null) {
            r.setId(y.getId());
            r.setRefRemise(y.getReference());
            r.setActif(y.isActif());
            r.setPermanent(y.isPermanent());
            r.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            r.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            r.setSociete(currentAgence.getSociete());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
        }
        return r;
    }

    public YvsComGrilleRemise buildGrilleRemise(GrilleRabais y) {
        YvsComGrilleRemise r = new YvsComGrilleRemise();
        if (y != null) {
            r.setId(y.getId());
            r.setMontantMaximal(y.getMontantMaximal());
            r.setMontantMinimal(y.getMontantMinimal());
            r.setMontantRemise(y.getMontantRabais());
            r.setNatureMontant((y.getNatureMontant() != null) ? y.getNatureMontant() : Constantes.NATURE_MTANT);
            r.setBase((y.getBase() != null) ? y.getBase() : Constantes.BASE_QTE);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
        }
        return r;
    }

    public YvsComCoutSupDocVente buildCoutSupDocVente(CoutSupDoc y) {
        YvsComCoutSupDocVente c = new YvsComCoutSupDocVente();
        if (y != null) {
            c.setId(y.getId());
            c.setActif(y.isActif());
            c.setSupp(y.isSupp());
            if (y.getType() != null ? y.getType().getId() > 0 : false) {
                ManagedTypeCout s = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
                if (s != null) {
                    c.setTypeCout(s.getTypes().get(s.getTypes().indexOf(new YvsGrhTypeCout(y.getType().getId()))));
                } else {
                    c.setTypeCout(new YvsGrhTypeCout(y.getType().getId()));
                }
            }
            c.setService(y.isService());
            c.setMontant(y.getMontant());
            if (y.getDoc() > 0) {
                c.setDocVente(new YvsComDocVentes(y.getDoc()));
            }
            c.setDateSave(y.getDateSave());
            c.setAuthor(currentUser);
            c.setNew_(true);
        }
        return c;
    }

    @Override
    public DocVente recopieView() {
        docVente.setEnteteDoc(docVente.getId() > 0 ? docVente.getEnteteDoc() : docVente.getEnteteDoc());
        return docVente;
    }

    public EnteteDocVente recopieViewEntete() {
        docVente.getEnteteDoc().setUsers(docVente.getEnteteDoc().getId() > 0 ? docVente.getEnteteDoc().getUsers() : docVente.getEnteteDoc().getUsers());
        docVente.getEnteteDoc().setDateUpdate(new Date());
        return docVente.getEnteteDoc();
    }

    public YvsBaseCategorieClient getDefautCategorie() {
        YvsBaseCategorieClient c = new YvsBaseCategorieClient();
        c.setDescription("Catégorie Par Défaut");
        c.setCode("Defaut");
        c.setLibelle("Autres");
        c.setLierClient(false);
        c.setDefaut(true);
        return c;
    }

    public CommercialVente recopieViewCommercial(DocVente facture) {
        if (commercial != null) {
            commercial.setDateUpdate(new Date());
            commercial.setFacture(facture);
        }
        return commercial;
    }

    public RemiseDocVente recopieViewRemise() {
        remise.setDocVente(docVente);
        remise.setNew_(true);
        return remise;
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
            if (reglement.getId() < 1) {
                reglement.setDatePiece(reglement.getDatePaiementPrevu());
            }
        }
        return reglement;
    }

    public CoutSupDoc recopieViewCoutSupDoc() {
        if (cout != null) {
            cout.setDoc(cout.isUpdate() ? cout.getDoc() : docVente.getId());
            cout.setDateUpdate(new Date());
        }
        return cout;
    }

    public void resetView(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            int idx = getDocuments().indexOf(y);
            if (idx > -1) {
                getDocuments().set(idx, y);
                update("data_facture_vente");
            }
            if (docVente.getId() == y.getId()) {
                onSelectObject(y);
            }
        }
    }

    @Override
    public boolean controleFiche(DocVente bean) {
        return controleFiche(bean, true);
    }

    private boolean controleFiche(DocVenteInformation bean) {
        if (bean == null) {
            getErrorMessage("L'élément ne peut pas etre null");
            return false;
        }
        if (getDocVente().getNature().equals(Constantes.LOCATION)) {
            if ((bean.getAdresseLivraison() != null) ? bean.getAdresseLivraison().getId() < 1 : true) {
                getErrorMessage("L'adresse de livraison ne peut pas etre null");
                return false;
            }
        }
        if (!getDocVente().getNature().equals(Constantes.VENTE)) {
            if (bean.getDateFin() == null) {
                getErrorMessage("La date de fin ne peut pas etre null");
                return false;
            }
        }
        return true;
    }

    public boolean controleFiche(DocVente bean, boolean acces) {
        if (!_controleFiche_(bean, acces)) {
            return false;
        }
        if (!saveNewEntete()) {
            return false;
        }
        if ((selectEntete != null) ? selectEntete.getId() < 1 : true) {
            getErrorMessage("Vous ne disposé pas d'une entête");
            return false;
        }
        if ((bean.getClient() != null) ? bean.getClient().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le client!");
            return false;
        }
        if (bean.getTiers().getId() < 1) {
            if (bean.getClient().isSuiviComptable()) {
                bean.setTiers(bean.getClient());
            } else {
                if (selectEntete.getCreneau().getUsers() != null ? (selectEntete.getCreneau().getUsers().getCommercial() != null ? (selectEntete.getCreneau().getUsers().getCommercial().getTiers() != null) : false) : false) {
                    if (selectEntete.getCreneau().getUsers().getCommercial().getTiers().getClients() != null ? !selectEntete.getCreneau().getUsers().getCommercial().getTiers().getClients().isEmpty() : false) {
                        bean.setTiers(new Client(selectEntete.getCreneau().getUsers().getCommercial().getTiers().getClients().get(0).getId()));
                    }
                }
            }
        }
        if (bean.getTiers().getId() <= 0) {
            getWarningMessage("Aucun code tiers n'a été trouvé pour cette facture");
        }
        if (bean.getClient().isDefaut()) {
            if (modelFormCompOblig.fv_nom_client) {
                if (!bean.getEnteteDoc().getPoint().isAcceptClientNoName()) {
                    if (bean.getNomClient() != null ? (bean.getNomClient().trim().length() < 1 || bean.getNomClient().equals(bean.getClient().getNom())) : true) {
                        getErrorMessage("Vous devez specifier le nom du client!");
                        return false;
                    }
                }
                if ((bean.getAdresse() != null) ? bean.getAdresse().getId() < 1 : true) {
                    getErrorMessage("Vous devez specifier l'adresse de la vente!");
                    return false;
                }
            }
        }
        if (modelFormCompOblig.fv_adresse_client) {
            if ((bean.getAdresse() != null) ? bean.getAdresse().getId() < 1 : true) {
                getErrorMessage("Vous devez specifier l'adresse de la vente!");
                return false;
            }
        }
        if (modelFormCompOblig.fv_model_reg) {
            if ((bean.getModeReglement() != null) ? bean.getModeReglement().getId() < 1 : true) {
                getErrorMessage("Vous devez specifier le modèle de règlement");
                return false;
            }
        }
        if (modelFormCompOblig.fv_depot_liv) {
            if ((bean.getDepot() != null) ? bean.getDepot().getId() < 1 : true) {
                getErrorMessage("Vous devez specifier le dépôt de livraison");
                return false;
            }
        }
        if (modelFormCompOblig.fv_tranche_liv) {
            if ((bean.getTranche() != null) ? bean.getTranche().getId() < 1 : true) {
                getErrorMessage("Vous devez specifier la tranche de livraison");
                return false;
            }
        }
        if (modelFormCompOblig.fv_date_liv) {
            if ((bean.getTranche() != null) ? bean.getTranche().getId() < 1 : true) {
                getErrorMessage("Vous devez specifier la date de livraison");
                return false;
            }
        }
        if ((bean.getCategorieComptable() != null) ? bean.getCategorieComptable().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier la catégorie comptable!");
            return false;
        }
        if (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false) {
            if (!bean.getDocumentLie().getStatut().equals(Constantes.ETAT_VALIDE)) {
                getWarningMessage("Cette facture est rattachée à la commande N° " + bean.getDocumentLie().getNumDoc() + " qui n'est pas validée");
            }
        }
//        if ((selectDoc != null ? (selectDoc.getId() > 0
//                ? ((selectEntete.getDateEntete() != null
//                && (selectDoc.getEnteteDoc() != null ? selectDoc.getEnteteDoc().getDateEntete() != null : false)))
//                ? !selectEntete.getDateEntete().equals(selectDoc.getEnteteDoc().getDateEntete()) : false : false) : false)
//                || (bean.getNumDoc() == null || bean.getNumDoc().trim().length() < 1)) {
//            String ref = genererReference(Constantes.TYPE_FV_NAME, selectEntete.getDateEntete(), docVente.getEnteteDoc().getPoint().getId(), Constantes.POINTVENTE);
//            if ((ref != null) ? ref.trim().equals("") : true) {
//                return false;
//            }
//            bean.setNumDoc(ref);
//        }
        bean.setEnteteDoc(bean.getId() > 0 ? bean.getEnteteDoc() : docVente.getEnteteDoc());
        if (!verifyDateVente(selectEntete.getDateEntete(), bean.isUpdate())) {
            return false;
        }

        return true;
    }

    private boolean _controleFiche_(DocVente bean, boolean acces) {
        if (bean == null) {
            getErrorMessage("Vous devez selectionner un document");
            return false;
        }
        if (!bean.isUpdate()) {
            if (!bean.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
                return false;
            }
        } else {
            if (acces) {
                int prec = -1;
                for (YvsWorkflowValidFactureVente e : bean.getEtapesValidations()) {
                    if (e.isEtapeActive()) {
                        if (prec > -1 && prec < bean.getEtapesValidations().size()) {
                            if (!asDroitValideEtape(bean.getEtapesValidations().get(prec).getEtape().getAutorisations(), currentUser.getUsers().getNiveauAcces())) {
                                openNotAcces();
                                return false;
                            }
                        }
                    }
                    prec++;
                }
            }
            if (bean.getStatut().equals(Constantes.ETAT_VALIDE)) {
                getErrorMessage("Vous ne pouvez pas modifer cette facture ! Elle est déja validée");
                return false;
            }
            if (bean.getStatut().equals(Constantes.ETAT_ANNULE)) {
                getErrorMessage("Vous ne pouvez pas modifer cette facture ! Elle est déja annulée");
                return false;
            }
            if (acces) {
                if (bean.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
                    getErrorMessage("Vous ne pouvez pas modifer cette facture ! Elle est déja livrée");
                    return false;
                }
            }
        }
        if (bean.isConsigner()) {
            getErrorMessage("Vous ne pouvez pas modifier cette facture... car elle est consignée");
            return false;
        }
        if (bean.isCloturer()) {
            getErrorMessage("Ce document est vérouillé");
            return false;
        }
//        if (bean.getId() > 0) {
//            boolean comptabilise = dao.isComptabilise(bean.getId(), Constantes.SCR_VENTE);
//            if (comptabilise) {
//                getErrorMessage("Cette facture est déja comptabilisé");
//                return false;
//            }
//        }
        // contrôle que les delais de saisie demandé sont respectés
//        if (!controleEcartVente(bean.getEnteteDoc().getUsers().getId(), bean.getEnteteDoc().getDateEntete(), true)) {
//            return false;
//        }
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
        if (bean.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
            getErrorMessage("Cette facture est déja livrée");
            return false;
        }
        if (bean.getConsigner()) {
            getErrorMessage("Vous ne pouvez pas modifier cette facture... car elle est consignée");
            return false;
        }
        if (bean.getCloturer()) {
            getErrorMessage("Ce document est vérouillé");
            return false;
        }
        return hasDroitUpdateFacture(bean);
    }

    public boolean controleFiche(CommercialVente bean, boolean responsable, boolean msg) {
        if ((bean.getFacture() != null) ? bean.getFacture().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez selectionner la facture!");
            }
            return false;
        }
        if (bean.getFacture().getStatut().equals(Constantes.ETAT_VALIDE) && (!autoriser("fv_save_doc"))) {
            if (msg) {
                getErrorMessage("La facture sélectionnée est déjà validé!");
            }
            return false;
        }
        if ((bean.getCommercial() != null) ? bean.getCommercial().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez selectionner le commercial!");
            }
            return false;
        }
        if (bean.getTaux() <= 0 || bean.getTaux() > 100) {
            getErrorMessage("Vous devez entrer la bonne valeur du taux");
            if (msg) {
                return false;
            }
        }
        if (bean.getId() < 1 && responsable) { //pas encore affecté
            bean.setResponsable(true);
        }

        YvsComCommercialVente y = (YvsComCommercialVente) dao.loadOneByNameQueries("YvsComCommercialVente.findByFactureCommercial", new String[]{"facture", "commercial"}, new Object[]{new YvsComDocVentes(docVente.getId()), new YvsComComerciale(bean.getCommercial().getId())});
        if (y != null ? !y.getId().equals(bean.getId()) : false) {
            if (msg) {
                getErrorMessage("Vous avez deja associé ce commercial");
            }
            return false;
        }
        Long id = (Long) dao.loadObjectByNameQueries("YvsComCommercialVente.findIdResponsable", new String[]{"facture"}, new Object[]{new YvsComDocVentes(docVente.getId())});
        if (id != null ? !id.equals(bean.getId()) : false) {
            bean.setResponsable(false);
        }
        if (!bean.getFacture().getClient().isSuiviComptable() && bean.isResponsable()) {
            if (bean.getCommercial().getTiers().getClients() != null ? !bean.getCommercial().getTiers().getClients().isEmpty() : false) {
                bean.getFacture().setTiers(new Client(bean.getCommercial().getTiers().getClients().get(0).getId()));
            }
        }
        Double taux = (Double) dao.loadObjectByNameQueries("YvsComCommercialVente.sumTauxByFactureNotId", new String[]{"facture", "id"}, new Object[]{new YvsComDocVentes(docVente.getId()), bean.getId()});
        taux = (taux != null ? taux : 0) + bean.getTaux();
        if (taux > 100) {
            if (msg) {
                getErrorMessage("Le total des pourcentage ne peut pas exceder 100%");
            }
            return false;
        }
        return true;
    }

    public boolean controleFicheContenu(ContenuDocVente bean, boolean continuSave) {
        if (bean.getDocVente() != null ? !bean.getDocVente().isUpdate() : true) {
            if (isService ? !saveNewService() : !_saveNew(true)) {
                return false;
            }
            bean.setDocVente(docVente);
        }
        if (!bean.controlTotal()) {
            applyTotal(bean);
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le conditionnement");
            return false;
        }
        if (docVente.getNature().equals(Constantes.LOCATION)) {
            if ((bean.getQualite() != null) ? bean.getQualite().getId() < 1 : true) {
                getErrorMessage("Vous devez specifier la qualité");
                return false;
            }
        }
        if (bean.getQuantite() <= 0) {
            getErrorMessage("Vous devez entrer la quantitée");
            return false;
        }
        if (!bean.isAdditionnel() && bean.getPrix() <= 0) {
            getErrorMessage("Vous devez entrer le prix de vente");
            return false;
        }
        boolean controlAcces = false;
        if (!bean.isAdditionnel() && !continuSave) {
            if (bean.getRabais() <= 0) {
                if (bean.getPrix() < bean.getPrixMin()) {
                    openDialog("dlgConfirmAlertRabais");
                    update("dlg_confirm_rabais_fv");
                    on_rabais = true;
                    return false;
                }
            } else {
                controlAcces = true;
            }
        } else {
            controlAcces = true;
        }
        if (currentParamVente == null) {
            getErrorMessage("Vous devez Initialiser les paramètres commerciaux de l'agence!");
            return false;
        }
        if (!bean.isAdditionnel() && bean.getRabais() <= 0 ? !currentParamVente.getSellLowerPr() ? bean.getPrix() < bean.getPr() : false : false) {
            getErrorMessage("Vous ne pouvez vendre cet article en dessous du prix de revient !");
            return false;
        }
        return _controleFiche_(bean.getDocVente(), true);
    }

    public boolean controleFicheClient(Client bean) {
        if ((bean.getTiers() != null) ? bean.getTiers().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le tiers");
            return false;
        }
        return true;
    }

    public boolean controleFicheTiers(Tiers bean) {
        if (bean.getNom() == null || bean.getNom().equals("")) {
            getErrorMessage("Vous devez entrer le nom");
            return false;
        }
        return true;
    }

    public boolean controleFicheRemise(RemiseDocVente bean) {
        if (!bean.getDocVente().isUpdate()) {
            getErrorMessage("Vous devez d'abord enregistrer le document");
            return false;
        }
        if (bean.getDocVente().getStatutRegle().equals(Constantes.ETAT_REGLE)) {
            getErrorMessage("Cette facture est déja reglée");
            return false;
        }
        if ((bean.getRemise() != null) ? bean.getRemise().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier la remise");
            return false;
        }
        for (YvsComRemiseDocVente r : remisesFacture) {
            if (r.getRemise().getId().equals(bean.getRemise().getId())) {
                getErrorMessage("Cette remise est deja attribuée!");
                return false;
            }
        }
        return true;
    }

    public boolean controleFicheRemise_(Remise bean) {
        if (bean.getReference() == null || bean.getReference().trim().equals("")) {
            getErrorMessage("vous devez entrer la reference");
            return false;
        }
        return true;
    }

    public boolean controleFicheCout(CoutSupDoc bean) {
        if (bean.getDoc() < 1) {
            if (!_saveNew(true)) {
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

        if (docVente != null) {
            if (docVente.getId() <= 0) {
                getErrorMessage("Vous devez selectionner un document");
                return false;
            }
            if (docVente.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                getErrorMessage("Le document est déjà marqué réglé !");
                return false;
            }
        } else {
            getErrorMessage("Vous devez selectionner un document");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(DocVente bean) {
        cloneObject(docVente, bean);
        docVente.getClient().setNom(bean.getClient().getNom_prenom());
//        docVente.setEtapesValidations(ordonneEtapes(bean.getEtapesValidations()));
        bon = 0;
        isBon = (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false);
        if (isBon) {
            bon = bean.getDocumentLie().getId();
            contenus_bcv = loadContenusStay(new YvsComDocVentes(bon), Constantes.TYPE_FV);
            update("data_contenu_bcv_fv");
        } else {
            checkAvance = false;
        }
        ManagedPointVente wp = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (wp != null) {
            wp.setAgenceFind(bean.getEnteteDoc().getPoint().getAgence().getId());
            wp.onChooseAgenceForAction();
        }
        if (bean.getAdresse() != null ? bean.getAdresse().getId() > 0 : false) {
            Dictionnaire ville = bean.getAdresse().getParent();
            cloneObject(docVente.getVille(), ville);
//            chooseVille();
            cloneObject(docVente.getAdresse(), bean.getAdresse());
        } else {
            Client d = bean.getClient();
            if (d.getTiers() != null ? d.getTiers().getId() > 0 : false) {
                boolean correct = false;
                if (d.getTiers().getVille() != null ? d.getTiers().getVille().getId() > 0 : false) {
                    cloneObject(docVente.getVille(), d.getTiers().getVille());
                    chooseVille();
                    if (d.getTiers().getSecteur() != null ? d.getTiers().getSecteur().getId() > 0 : false) {
                        cloneObject(docVente.getAdresse(), d.getTiers().getSecteur());
                        chooseAdresse();
                        correct = true;
                    }
                }
                if (!correct) {
                    cloneObject(docVente.getAdresse(), d.getTiers().getSecteur_());
                }
            }
        }
        if (bean.getEtapesValidations() != null ? !bean.getEtapesValidations().isEmpty() : false) {
            docVente.setFirstEtape(bean.getEtapesValidations().get(0).getEtape().getLabelStatut());
        }
        ManagedBonVente w = (ManagedBonVente) giveManagedBean(ManagedBonVente.class);
        if (docVente.getTypeDoc().equals(Constantes.TYPE_FV) || w == null) {
            setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
        } else {
            w.setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
        }
        update("infos_document_facture_vente");
        update("chp_fv_net_a_payer");
        update("value_ttc_facture");
        update("value_reste_a_payer_facture");
    }

    public void populateViewEntete(EnteteDocVente bean, boolean complet) {
        cloneObject(docVente.getEnteteDoc(), bean);
//        if (complet) {
//            setMontantEntete(null);
//        }
        docVente.getEnteteDoc().setTranche(bean.getTranchePoint());
    }

    public void populateViewContenu(ContenuDocVente bean) {
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docVente.getDepot().getId(), 0, 0, docVente.getEnteteDoc().getDateEntete(), bean.getConditionnement().getId(), bean.getLot().getId()));
        } else {
            bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, 0, currentAgence.getId(), 0, docVente.getEnteteDoc().getDateEntete(), bean.getConditionnement().getId(), bean.getLot().getId()));
        }
        bean.getArticle().setPuv(dao.getPuv(bean.getArticle().getId(), bean.getQuantite(), bean.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), bean.getConditionnement().getId()));
        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
        selectArt = true;
        cloneObject(contenu, bean);
        YvsBaseArticles t = new YvsBaseArticles(bean.getArticle().getId());
        contenu.setPrixMin(dao.getPuvMin(t.getId(), bean.getQuantite(), bean.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), bean.getConditionnement().getId()));
        champ = new String[]{"article", "point"};
        val = new Object[]{t, new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId())};
        List<YvsBaseArticlePoint> la = dao.loadNameQueries("YvsBaseArticlePoint.findByArticlePoint", champ, val, 0, 1);
        if (la != null ? !la.isEmpty() : false) {
            contenu.getArticle().setChangePrix(la.get(0).getChangePrix());
        }
        if (contenu.getPrix() < 1) {
            contenu.getArticle().setChangePrix(true);
        }
        if (page.equals("V3")) {
            update("desc_article_facture_vente");
        } else {

        }
    }

    public void populateViewRemise(RemiseDocVente bean) {
        cloneObject(remise, bean);
    }

    public void populateView(CommercialVente bean) {
        cloneObject(commercial, bean);
    }

    public void populateViewCout(CoutSupDoc bean) {
        cloneObject(cout, bean);
    }

    @Override
    public void resetFiche() {
        EnteteDocVente e = docVente.getEnteteDoc();
        try {
            if (docVente.getEnteteDoc().getCrenauHoraire().getPersonnel() != null ? docVente.getEnteteDoc().getCrenauHoraire().getPersonnel().getId() < 1 : true) {
                if (vendeurs != null ? !vendeurs.isEmpty() : false) {
                    docVente.getEnteteDoc().getCrenauHoraire().setPersonnel(new Users(vendeurs.get(0).getId()));
                }
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
        Dictionnaire adresse = new Dictionnaire();
        cloneObject(adresse, docVente.getAdresse());
        docVente = new DocVente();
        docVente.setAdresse(adresse);
        docVente.setNature(natureVente != null ? !natureVente.trim().isEmpty() ? natureVente : Constantes.VENTE : Constantes.VENTE);
        docVente.setEnteteDoc(e);
        if (!depotsLivraison.isEmpty()) {
            docVente.setDepot(UtilCom.buildBeanDepot(depotsLivraison.get(0)));
        }
        docVente.setDateLivraisonPrevu(docVente.getEnteteDoc().getDateEntete());
        docVente.setDateLivraison(docVente.getEnteteDoc().getDateEntete());
        if (venteDirecte) {
            docVente.setLivraisonAuto(true);
        }
        docVente.setValidationReglement(docVente.getEnteteDoc() != null ? docVente.getEnteteDoc().getPoint() != null ? docVente.getEnteteDoc().getPoint().isValidationReglement() : false : false);
        docVente.setVille(docVente.getAdresse() != null ? docVente.getAdresse().getParent() != null ? docVente.getAdresse().getParent() : new Dictionnaire() : new Dictionnaire());
        chooseDepot();
        docVente.getContenus().clear();
        contenus_bcv.clear();
        selectDoc = new YvsComDocVentes();
        selectDoc.setEnteteDoc(selectEntete);
        selections.clear();
        isBon = false;
        bon = 0;
        checkAvance = false;
        onEncaisement = false;
        taxes.clear();
        remisesFacture.clear();
        displayDetailClient = false;
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            m.resetFiche();
        }
        resetSubFiche();
        setClientDefaut();
        update("blog_form_facture_vente");
        if (venteDirecte) {
            update("blog_btn_action_facture");
        }
    }

    public void resetFiche(boolean validAndClear) {
        this.validAndClear = validAndClear;
        if (validAndClear) {
            resetFiche();
        }
    }

    public void resetFicheEntete() {
        if (!autoriser("fv_update_header")) {
            openNotAcces();
            return;
        }
        docVente.setEnteteDoc(new EnteteDocVente());
        if (selectEntete != null && !venteDirecte) {
            ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (m != null) {
                m.loadPointVenteByDroit(true, true);
            }
        }
        selectEntete = new YvsComEnteteDocVente();
        docVente.getEnteteDoc().setDateEntete(new Date());
        docVente.getEnteteDoc().setUsers(UtilUsers.buildBeanUsers(currentUser.getUsers()));
        vendeurs.clear();
        if (currentPlanning != null ? !currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauPoint() != null : false : false) {
            docVente.getEnteteDoc().setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauPoint().getTranche()));
            docVente.getEnteteDoc().setPoint(UtilCom.buildSimpleBeanPointVente(currentPlanning.get(0).getCreneauPoint().getPoint()));
            choosePoint(currentPlanning.get(0).getCreneauPoint().getPoint());
        }
        if (!depotsLivraison.isEmpty()) {
            docVente.setDepot(UtilCom.buildBeanDepot(depotsLivraison.get(0)));
        }
        chooseDepot();
        resetFiche();
        update("value_date_entete_fv");
        update("save_entete_facture_vente");
        update("data_facture_vente");
        update("blog_form_facture_vente");
    }

    public void resetSubFiche() {
        resetFicheRemise();
        resetFicheContenu();
        resetFicheCommercial();
        resetFicheReglement(false);
        if (page.equals("V3")) {
            update("blog_form_contenu_facture_vente");
            update("blog_form_cout_facture_vente");
            update("form_mensualite_facture_vente");
        } else {
            update("tabview_facture_vente:blog_form_contenu_facture_vente");
            update("tabview_facture_vente:blog_form_cout_facture_vente");
            update("tabview_facture_vente:form_mensualite_facture_vente");
        }
        update("data_livraison_facture_vente");
        update("blog_commercial_vente");
    }

    public void resetFicheContenu() {
        contenu = new ContenuDocVente();
        contenu.setQuantite(1);
        bonus = new ContenuDocVente();
        pack = new YvsBaseArticlePack();

        selectContenu = new YvsComContenuDocVente();
        selectArt = false;
        listArt = false;
        on_rabais = false;

        tabIds_contenu = "";
        if (page.equals("V3")) {
            update("form_contenu_facture_vente");
            update("desc_article_facture_vente");
        } else {
            update("tabview_facture_vente:form_contenu_facture_vente");
        }
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

    public void resetFicheRemise() {
        resetFiche(remise);
        remise.setRemise(new Remise());
        tabIds_remise = "";
    }

    public void resetFicheCommercial() {
        commercial = new CommercialVente();
    }

    public void resetFicheCout() {
        resetFiche(cout);
        cout.setType(new TypeCout());
        cout.setService(false);
        tabIds_cout = "";
        selectCout = null;
        if (page.equals("V3")) {
            update("blog_form_cout_facture_vente");
        } else {
            update("tabview_facture_vente:blog_form_cout_facture_vente");
        }
    }

    @Override
    public boolean saveNew() {
        return saveNew(false);
    }

    public boolean saveNew(boolean controle) {
        if (!controle) {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                if (!docVente.getContenusSave().isEmpty()) {
                    if (!selectDoc.getClient().getId().equals(docVente.getClient().getId()) || !Objects.equals(selectDoc.getCategorieComptable().getId(), docVente.getCategorieComptable().getId())) {
                        openDialog("dlgConfirmUpdatePrix");
                        return false;
                    }
                }
            }
        }
        if (_saveNew(true)) {
            succes();
            actionOpenOrResetAfter(this);
            return true;
        }
        return false;
    }

    public boolean _saveNew(boolean acces) {
        try {
            if (!Util.asString(docVente.getTypeDoc())) {
                docVente.setTypeDoc(Constantes.TYPE_FV);
            }
            if (controleFiche(docVente, acces)) {
                selectDoc = UtilCom.buildDocVente(docVente, selectEntete, currentUser);
                selectDoc.setOperateur(currentUser.getUsers());
                if (!docVente.isUpdate()) {
                    selectDoc.setId(null);
                    IYvsComDocVentes impl = (IYvsComDocVentes) (new IEntitySax()).createInstance("IYvsComDocVentes", dao);
                    if (impl != null) {
                        impl.setNiveauAcces(currentNiveau);
                        impl.setAgence(currentAgence);
                        ResultatAction<YvsComDocVentes> re = impl.save(selectDoc);
                        if (re != null ? re.isResult() : false) {
                            selectDoc = (YvsComDocVentes) re.getData();
                        } else {
                            getErrorMessage(re.getMessage());
                            return false;
                        }
                    } else {
                        getErrorMessage("Erreur Système !");
                        return false;
                    }
                    if (selectDoc != null) {
                        docVente.setId(selectDoc.getId());
                        docVente.setNumDoc(selectDoc.getNumDoc());
                        docVente.setEtapesValidations(new ArrayList<>(this.selectDoc.getEtapesValidations()));
                        docVente.setEtapeTotal(docVente.getEtapesValidations().size());
                        documents.add(0, this.selectDoc);
                        if (documents.size() > imax) {
                            documents.remove(documents.size() - 1);
                        }
                        nbreFacture++;
                        saveCurrentCommercial(false);
                    }
                } else {
                    if (!autoriser("fv_update_doc") || (!autoriser("fv_update_header")
                            && (docVente.getEnteteDoc().getDateEntete().compareTo(selectEntete.getDateEntete()) < 0))) {
                        openNotAcces();
                        return false;
                    }
                    dao.update(selectDoc);
                    //si la facture a déjà un contenu, et que le client ou la catégorie comptable a changé, on recalcule les éléments du contenu
                    if (!docVente.getContenusSave().isEmpty() && majPrixContenu) {
                        int idx = documents.indexOf(selectDoc);
                        if (idx >= 0) {
                            YvsComDocVentes old = documents.get(idx);
                            if (!old.getClient().equals(selectDoc.getClient()) || !Objects.equals(old.getCategorieComptable(), selectDoc.getCategorieComptable())) {
                                for (int i = 0; i < docVente.getContenusSave().size(); i++) {
                                    ContenuDocVente c = UtilCom.buildBeanContenuDocVente(docVente.getContenusSave().get(i));
                                    c.setUpdate(false);
                                    findPrixArticle(c, true, false);

                                    YvsComContenuDocVente cc = UtilCom.buildContenuDocVente(c, currentUser);
                                    dao.update(cc);
                                    int index = docVente.getContenusSave().indexOf(cc);
                                    if (index > -1) {
                                        docVente.getContenusSave().set(index, cc);
                                    }
                                    index = docVente.getContenus().indexOf(cc);
                                    if (index > -1) {
                                        docVente.getContenus().set(index, cc);
                                    }
                                }
                                setMontantTotalDoc(docVente, docVente.getContenusSave());
                                if (page.equals("V3")) {
                                    update("data_contenu_facture_vente");
                                } else {
                                    update("tabview_facture_vente:data_contenu_facture_vente");
                                }
                                update("chp_fv_net_a_payer");
                                update("value_ttc_facture");
                                update("value_reste_a_payer_facture");
                            }
                        }
                    }
                    if (documents.contains(selectDoc)) {
                        documents.set(documents.indexOf(selectDoc), selectDoc);
                    }
                }
                docVente.setUpdate(true);
                update("form_entete_facture_vente");
                update("blog_choose_commande_fv");
                update("data_facture_vente");
//                update("data_facture_vente_hist");
                if (venteDirecte) {
                    update("etapes_valide_facture_vente");
                    update("grp_btn_etat_facture_vente");
                    update("blog_btn_action_facture");
                    update("txt_reference_document_facture_vente");
                }
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean saveNewService() {
        return saveNewService(false);

    }

    public boolean saveNewService(boolean controle) {
        if (!controle) {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                if (!docVente.getContenusSave().isEmpty()) {
                    if (!selectDoc.getClient().getId().equals(docVente.getClient().getId()) || !Objects.equals(selectDoc.getCategorieComptable().getId(), docVente.getCategorieComptable().getId())) {
                        openDialog("dlgConfirmUpdatePrix");
                        return false;
                    }
                }
            }
        }
        //To change body of generated methods, choose Tools | Templates.$
        try {
            if (controleFiche(getDocVente().getInformation())) {
                boolean succes = _saveNew(true);
                if (succes) {
                    IYvsComDocVentesInformations impl = (IYvsComDocVentesInformations) (new IEntitySax()).createInstance("IYvsComDocVentesInformations", dao);
                    if (impl != null) {
                        impl.setNiveauAcces(currentNiveau);
                        impl.setAgence(currentAgence);
                        getDocVente().getInformation().setFacture(getDocVente());
                        YvsComDocVentesInformations entity = UtilCom.buildDocVenteInformation(getDocVente().getInformation(), currentUser);
                        if (entity != null) {
                            ResultatAction<YvsComDocVentesInformations> result;
                            if (entity.getId() < 1) {
                                result = impl.save(entity);
                            } else {
                                result = impl.update(entity);
                            }
                            if (result != null ? result.isResult() : false) {
                                getDocVente().getInformation().setId(result.getIdEntity());
                                succes();
                                actionOpenOrResetAfter(this);
                                return true;
                            } else {
                                getErrorMessage(result != null ? result.getMessage() : "Action Impossible!!!");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void saveNewContenu(boolean continuSave) {
        saveNewContenu(continuSave, false);
    }

    public void saveNewContenu(boolean continuSave, boolean isService) {
        boolean reset = pack != null ? pack.getContenus() != null ? pack.getContenus().isEmpty() : true : true;
        boolean succes = saveNewContenu(contenu, selectContenu, continuSave, isService, reset);
        if (succes && !reset) {
            ContenuDocVente contenu;
            for (YvsBaseArticleContenuPack c : pack.getContenus()) {
                contenu = new ContenuDocVente();
                contenu.setActif(true);
                contenu.setArticle(UtilProd.buildSimpleBeanArticles(c.getArticle().getArticle()));
                contenu.setArticleBonus(null);
                contenu.setBonus(null);
                contenu.setCalculPr(true);
                contenu.setCommentaire("");
                contenu.setCommission(0);
                contenu.setConditionnement(UtilProd.buildSimpleBeanConditionnement(c.getArticle()));
                contenu.setConditionnementBonus(null);
                contenu.setDateContenu(new Date());
                contenu.setDateSave(new Date());
                contenu.setDateUpdate(new Date());
                contenu.setDepoLivraisonPrevu(null);
                contenu.setDocVente(docVente);
                contenu.setLot(null);
                contenu.setMouvStock(true);
                contenu.setNumSerie("");
                contenu.setParent(null);
                contenu.setPr(0);
                contenu.setPrix(c.getMontant());
                contenu.setQuantite(this.contenu.getQuantite() * c.getQuantite());
                contenu.setPrixMin(0);
                contenu.setPrixRabaix(0);
                contenu.setPrixTaxe(0);
                contenu.setPrixTotal(contenu.getQuantite() * contenu.getPrix());
                contenu.setQualite(null);
                contenu.setQuantiteBonus(0);
                contenu.setRabais(0);
                contenu.setRemise(0);
                contenu.setRistourne(0);
                contenu.setAdditionnel(true);
                contenu.setStatut(Constantes.ETAT_EDITABLE);
                saveNewContenu(contenu, null, true, isService, false);
            }
            succes();
            resetFicheContenu();
        }
    }

    public boolean saveNewContenu(ContenuDocVente contenu, YvsComContenuDocVente selectContenu, boolean continuSave, boolean isService, boolean reset) {
        this.isService = isService;
        try {
            on_rabais = false;
            contenu.setDepoLivraisonPrevu((contenu.getDepoLivraisonPrevu() != null ? contenu.getDepoLivraisonPrevu().getId() <= 0 : true) ? docVente.getDepot() : contenu.getDepoLivraisonPrevu());
            contenu.setDocVente(docVente);
            if (controleFicheContenu(contenu, continuSave)) {
                selectContenu = UtilCom.buildContenuDocVente(contenu, currentUser);
                if (contenu.getId() < 1) {
                    selectContenu.setId(null);
                    selectContenu = (YvsComContenuDocVente) dao.save1(selectContenu);
                    contenu.setId(selectContenu.getId());
                    docVente.getContenus().add(0, selectContenu);
                } else {
                    dao.update(selectContenu);
                    if (docVente.getContenus().contains(selectContenu)) {
                        docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
                    }
                }
                int idx = docVente.getContenusSave().indexOf(selectContenu);
                if (idx < 0) {
                    docVente.getContenusSave().add(selectContenu);
                } else {
                    docVente.getContenusSave().set(idx, selectContenu);
                }
                idx = documents.indexOf(new YvsComDocVentes(docVente.getId()));
                if (idx >= 0) {
                    int idx1 = documents.get(idx).getContenus().indexOf(selectContenu);
                    if (idx1 >= 0) {
                        documents.get(idx).getContenus().set(idx1, selectContenu);
                    } else {
                        documents.get(idx).getContenus().add(0, selectContenu);
                    }
                }
                saveAllTaxe(selectContenu);
                setMontantTotalDoc(docVente, docVente.getContenusSave(), currentAgence.getSociete().getId(), null, null, dao);
                if (reset) {
                    succes();
                    resetFicheContenu();
                }
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
                update("data_facture_vente");
                update("blog_form_montant_doc");
                if (page.equals("V3")) {
                    update("blog_form_contenu_facture_vente");
                } else {
                    update("tabview_facture_vente:blog_form_contenu_facture_vente");
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Lymytz Error  >>> ", ex);
        }
        return false;
    }

    public boolean saveNewEtat() {
        //To change body of generated methods, choose Tools | Templates.$
        try {
            etat.setContenu(new ContenuDocVente(getSelectContenu().getId()));
            YvsComContenuDocVenteEtat entity = UtilCom.buildContenuDocVenteEtat(etat, currentUser);
            IYvsComContenuDocVenteEtat impl = (IYvsComContenuDocVenteEtat) (new IEntitySax()).createInstance("IYvsComContenuDocVenteEtat", dao);
            if (impl != null) {
                impl.setNiveauAcces(currentNiveau);
                impl.setAgence(currentAgence);
                if (entity != null) {
                    entity.setContenu(getSelectContenu());
                    ResultatAction<YvsComContenuDocVenteEtat> result;
                    if (entity.getId() < 1) {
                        if (getSelectContenu() != null ? getSelectContenu().getId() > 0 : false) {
                            result = impl.save(entity);
                        } else {
                            result = new ResultatAction<>(true, entity, entity.getId(), "");
                        }
                    } else {
                        if (getSelectContenu() != null ? getSelectContenu().getId() > 0 : false) {
                            result = impl.update(entity);
                        } else {
                            result = new ResultatAction<>(true, entity, entity.getId(), "");
                        }
                    }
                    if (result != null ? result.isResult() : false) {
                        entity.setId(result.getIdEntity());
                        int idx = getSelectContenu().getEtats().indexOf(entity);
                        if (idx < 0) {
                            getSelectContenu().getEtats().add(entity);
                        } else {
                            getSelectContenu().getEtats().set(idx, entity);
                        }
                        if (getSelectContenu() != null ? getSelectContenu().getId() > 0 : false) {
                            idx = getDocVente().getContenus().indexOf(getSelectContenu());
                            if (idx > -1) {
                                getDocVente().getContenus().set(idx, getSelectContenu());
                            }
                        } else {
                            idx = retour.getContenus().indexOf(getSelectContenu());
                            if (idx > -1) {
                                retour.getContenus().set(idx, getSelectContenu());
                            }
                        }
                        etat = new ContenuDocVenteEtat();
                        succes();
                        return true;
                    } else {
                        getErrorMessage(result != null ? result.getMessage() : "Action Impossible!!!");
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void saveNewRetour() {
        try {
            if (retour != null) {
                if (retour.getDepotLivrer() != null ? retour.getDepotLivrer().getId() < 1 : true) {
                    getErrorMessage("Aucun dépôt de livraison n'a été trouvé !");
                    return;
                }
                if (retour.getTrancheLivrer() != null ? retour.getTrancheLivrer().getId() < 1 : true) {
                    getErrorMessage("Aucune tranche de livraison n'a été trouvé !");
                    return;
                }
                if (retour.getDateLivraison() != null ? retour.getDateLivraison().after(new Date()) : true) {
                    getErrorMessage("La date de livraison est incorrecte !");
                    return;
                }
                if (retour.getDocumentLie() != null ? retour.getDocumentLie().getId() < 1 : true) {
                    getErrorMessage("Le retour doit etre rattaché a une facture !");
                    return;
                }
                if (retour.getDocumentLie().getStatutLivre().equals(Constantes.ETAT_ATTENTE)) {
                    getErrorMessage("La facture doit etre livrée ou en cours de livraison");
                    return;
                }
                if (!verifyOperation(new Depots(retour.getDepotLivrer().getId(), retour.getDepotLivrer().getDesignation()), Constantes.ENTREE, Constantes.RETOUR, false)) {
                    return;
                }
                if (!verifyInventaire(retour.getDepotLivrer(), retour.getTrancheLivrer(), retour.getDateLivraison())) {
                    return;
                }
                String num = genererReference(Constantes.TYPE_BRL_NAME, retour.getDateLivraison(), retour.getDepotLivrer().getId(), Constantes.DEPOT);
                if (!Util.asString(num)) {
                    return;
                }
                IYvsComDocVentes impl = (IYvsComDocVentes) (new IEntitySax()).createInstance("IYvsComDocVentes", dao);
                if (impl != null) {
                    retour.setNumDoc(num);
                    retour.setDescription("Retour de la location N° " + getDocVente().getNumDoc() + " le " + ldf.format(retour.getDateLivraison()) + " à " + time.format(retour.getDateLivraison()));
                    if (retour.getId() > 0 ? retour.getId().equals(getSelectDoc().getId()) : true) {
                        List<YvsComContenuDocVente> contenus = new ArrayList<>(retour.getContenus());
                        retour.getContenus().clear();

                        retour.setId(null);
                        retour = (YvsComDocVentes) dao.save1(retour);
                        for (int i = 0; i < contenus.size(); i++) {
                            YvsComContenuDocVente c = contenus.get(i);
                            if (c.getQuantite() > 0) {
                                List<YvsComContenuDocVenteEtat> etats = new ArrayList<>(c.getEtats());
                                c.getEtats().clear();

                                c.setId(null);
                                c.setDateSave(new Date());
                                c.setDateUpdate(new Date());
                                c.setAuthor(currentUser);
                                c.setDocVente(retour);
                                c = (YvsComContenuDocVente) dao.save1(c);
                                for (int j = 0; j < etats.size(); j++) {
                                    YvsComContenuDocVenteEtat e = etats.get(j);
                                    e.setId(null);
                                    e.setContenu(c);
                                }
                                retour.getContenus().add(c);
                            }
                        }
                    } else {
                        retour.setDateSave(new Date());
                        retour.setDateUpdate(new Date());
                        retour.setAuthor(currentUser);
                        dao.update(retour);
                    }
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanEtat(YvsComContenuDocVenteEtat y, boolean delete) {
        selectEtat = y;
        if (delete) {
            try {
                dao.delete(y);
                if (Objects.equals(etat.getId(), y.getId())) {
                    etat = new ContenuDocVenteEtat();
                }
                getSelectContenu().getEtats().remove(y);
                succes();
            } catch (Exception ex) {
                getErrorMessage("Operation Impossible !");
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    public void loadOnViewEtat(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                etat = UtilCom.buildBeanContenuDocVenteEtat((YvsComContenuDocVenteEtat) ev.getObject());
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void unLoadOnViewEtat(UnselectEvent ev) {
        try {
            etat = new ContenuDocVenteEtat();
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void loadOnViewRetour(SelectEvent ev) {
        try {
            if (ev != null ? ev.getObject() != null : false) {
                retour = (YvsComDocVentes) ev.getObject();
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void unLoadOnViewRetour(UnselectEvent ev) {
        try {
            retour = new YvsComDocVentes();
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteContenuRetour(YvsComContenuDocVente y, boolean delete) {
        if (y != null) {
            if (!retour.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre editable");
                return;
            }
            if (y.getId() < 1 || (y.getParent() != null ? y.getParent().getId().equals(y.getId()) : false)) {
                retour.getContenus().remove(y);
            } else {
                selectContenuRetour = y;
                if (!delete) {
                    openDialog("dlgConfirmDeleteArticleRetour");
                } else {
                    dao.delete(y);
                    retour.getContenus().remove(y);
                }
            }
        }
    }

    public void changeQuantite(CellEditEvent ev) {
        int idx = ev.getRowIndex();
        Double oldValue = (Double) ev.getOldValue();
        Double newValue = (Double) ev.getNewValue();
        if (idx >= 0 && newValue > 0) {
            if (newValue > oldValue) {
                retour.getContenus().get(idx).setQuantite(oldValue);
                getErrorMessage("Vous ne pouvez pas faire un retour de plus de " + DNA(oldValue));
                update("table-contenu_retour");
            }
        }
    }

    private long returnCategorie(DocVente docVente, YvsComDocVentes selectDoc) {
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
        return categorie;
    }

    public void saveAllTaxe(YvsComContenuDocVente y) {
        saveAllTaxe(y, docVente, selectDoc);
    }

    public void saveAllTaxe(YvsComContenuDocVente y, DocVente docVente, YvsComDocVentes selectDoc) {
        long categorie = returnCategorie(docVente, selectDoc);
        saveAllTaxe(y, docVente, selectDoc, categorie, true);
    }

    public boolean saveNewEntete() {
        ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (w != null) {
            if (selectDoc != null ? selectDoc.getId() > 0 : false) {
                selectEntete = selectDoc.getEnteteDoc();
            } else {
                selectEntete = w.saveNewEntete(docVente);
            }
            if (selectEntete != null ? (selectEntete.getId() > 0 && docVente.getEnteteDoc().getId() < 1) : false) {
                docVente.setEnteteDoc(UtilCom.buildBeanEnteteDocVente(selectEntete));
                docVente.getEnteteDoc().setTranche(docVente.getEnteteDoc().getTranchePoint());
            }
            if (selectEntete != null ? selectEntete.getId() > 0 : false) {
                docVente.getEnteteDoc().setId(selectEntete.getId());
                docVente.getEnteteDoc().setUpdate(true);
                docVente.getEnteteDoc().setNew_(true);
                docVente.setEnteteDoc(docVente.getEnteteDoc());
                update("select_depot_liv_prevu");
                update("save_entete_facture_vente");
                return true;
            }
        }
        update("save_entete_facture_vente");
        return false;
    }

    public void updateEntete() {
        if (!autoriser("fv_update_header")) {
            openNotAcces();
            return;
        }
        if (selections != null ? !selections.isEmpty() : false) {
            for (YvsComDocVentes y : selections) {
                updateEntete(y, selections.size() == 1);
            }
            if (selections.size() > 1) {
                succes();
            }
        } else {
            updateEntete(selectDoc, true);
        }
    }

    public void updateEntete(YvsComDocVentes y, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                boolean comptabilised = w.isComptabilise(y.getId(), Constantes.SCR_VENTE);
                if (comptabilised) {
                    if (msg) {
                        getErrorMessage("Ce document est deja comptabilisée");
                    }
                    return;
                }
            }
            YvsComCreneauHoraireUsers cr = y.getEnteteDoc().getCreneau();
            if (cr != null) {
                champ = new String[]{"date", "creanau"};
                val = new Object[]{dateEntete, cr.getId()};
                YvsComEnteteDocVente e = (YvsComEnteteDocVente) dao.loadOneByNameQueries("YvsComEnteteDocVente.findByCrenauDate", champ, val);
                if (e != null ? (e.getId() != null ? e.getId() < 1 : true) : true) {
                    e = new YvsComEnteteDocVente(y.getEnteteDoc());
                    e.setId(null);
                    e.setDateEntete(dateEntete);
                    e.setDateUpdate(new Date());
                    e.setAuthor(currentUser);
                    e.setAgence(currentAgence);
                    e = (YvsComEnteteDocVente) dao.save1(e);
                }
                if (e != null ? (e.getId() != null ? e.getId() > 0 : false) : false) {
                    String numero = y.getNumDoc();
                    if (changeNumeroWhenChangeDate) {
                        numero = genererReference(Constantes.TYPE_FV_NAME, e.getDateEntete(), cr.getCreneauPoint().getPoint().getId(), Constantes.POINTVENTE);
                        if ((numero != null) ? numero.trim().equals("") : true) {
                            return;
                        }
                    }
                    y.setEnteteDoc(e);
                    y.setNumDoc(numero);
                    y.setDateUpdate(new Date());
                    y.setAuthor(currentUser);
                    dao.update(y);
                    int idx = documents.indexOf(y);
                    if (idx > -1) {
                        documents.set(idx, y);
                        update("data_facture_vente");
                    }
                    if (msg) {
                        succes();
                    }
                }
            }
        }
    }

    public void saveDefautClient() {
        try {
            ManagedClient s = (ManagedClient) giveManagedBean(ManagedClient.class);
            if (s != null) {
                YvsComClient e = s.saveDefautClient();
                if (e != null ? e.getId() > 0 : false) {
                    cloneObject(docVente.getClient(), UtilCom.buildBeanClient(e));
                    update("select_client_facture_vente");
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Lymytz Error >>>", ex);
        }
    }

    public void saveNewRemise() {
        try {
            if (!autoriser("fv_apply_rem_all")) {
                openNotAcces();
                return;
            }
            RemiseDocVente bean = recopieViewRemise();
            if (controleFicheRemise(bean)) {
                YvsComRemiseDocVente en = buildRemiseDocVente(bean);
                if (!bean.isUpdate()) {
                    en.setId(null);
                    en = (YvsComRemiseDocVente) dao.save1(en);
                    setMontantRemise(en, docVente);
                    remisesFacture.add(0, en);
                    docVente.setMontantRemises(docVente.getMontantRemises() + en.getMontant());
                } else {
                    dao.update(en);
                    setMontantRemise(en, docVente);
                    YvsComRemiseDocVente r = remisesFacture.get(remisesFacture.indexOf(en));
                    setMontantRemise(r, docVente);
                    remisesFacture.set(remisesFacture.indexOf(en), en);
                    docVente.setMontantRemises(docVente.getMontantRemises() + en.getMontant() - r.getMontant());
                }
                succes();
                resetFicheRemise();
                update("blog_form_montant_doc_fv");
                update("data_remise_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveNewCommercial(CommercialVente bean, boolean responsable, boolean msg) {
        try {
            if (controleFiche(bean, responsable, msg)) {
                YvsComCommercialVente y = UtilCom.buildCommercialVente(bean, currentUser);
                if (y.getId() < 1) {
                    y.setId(null);
                    y = (YvsComCommercialVente) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = docVente.getCommerciaux().indexOf(y);
                if (idx > -1) {
                    docVente.getCommerciaux().set(idx, y);
                } else {
                    docVente.getCommerciaux().add(0, y);
                }
                idx = selectDoc.getCommerciaux().indexOf(y);
                if (idx > -1) {
                    selectDoc.getCommerciaux().set(idx, y);
                } else {
                    selectDoc.getCommerciaux().add(0, y);
                }
                if (bean.isResponsable() && !docVente.getClient().isSuiviComptable()) {
                    if (selectDoc != null && (bean.getFacture() != null ? bean.getCommercial().getTiers() != null : false)) {
                        if (selectDoc.getTiers() != null ? !selectDoc.getTiers().getId().equals(bean.getCommercial().getTiers().getId()) : true) {
                            YvsComClient tiers = null;
                            if (bean.getCommercial().getTiers().getId() > 0 ? bean.getCommercial().getTiers().getClients() != null ? !bean.getCommercial().getTiers().getClients().isEmpty() : false : false) {
                                tiers = bean.getCommercial().getTiers().getClients().get(0);
                            }
                            Options[] param = new Options[]{new Options(selectDoc.getId(), 1)};
                            String query = "update yvs_com_doc_ventes set tiers = null where id = ?";
                            if (tiers != null ? tiers.getId() > 0 : false) {
                                param = new Options[]{new Options(tiers.getId(), 1), new Options(selectDoc.getId(), 2)};
                                query = "update yvs_com_doc_ventes set tiers = ? where id = ?";
                            }
                            dao.requeteLibre(query, param);
                            if (tiers != null ? tiers.getId() > 0 : false) {
                                docVente.setTiers(new Client(tiers.getId()));
                            }
                            selectDoc.setTiers(new YvsComClient(docVente.getTiers().getId()));
                        }
                    }
                }
                idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
                if (msg) {
                    succes();
                }
                resetFicheCommercial();
                docVente.setNbreCommerciaux(docVente.getCommerciaux().size());
                update("btn-open_commercial");
                update("tabview_facture_vente:blog_commerciaux_vente");
            }
        } catch (Exception ex) {
            if (msg) {
                getErrorMessage("Operation Impossible !");
            }
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveNewCommercial() {
        saveNewCommercial(recopieViewCommercial(docVente), true, true);
    }

    public void saveCurrentCommercial(boolean msg) {
        if (selectDoc != null ? selectDoc.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez enregistrer la facture de vente");
            }
            return;
        }
        if (selectDoc.getEnteteDoc() != null ? selectDoc.getEnteteDoc().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez enregistrer le journal de vente");
            }
            return;
        }
        if (selectDoc.getEnteteDoc().getCreneau() != null ? selectDoc.getEnteteDoc().getCreneau().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez enregistrer le journal de vente");
            }
            return;
        }
        char commissionFor = 'C';
        YvsBasePointVente pv = null;
        YvsComCreneauPoint cr = selectDoc.getEnteteDoc().getCreneau().getCreneauPoint();
        if (cr != null ? cr.getId() > 0 : false) {
            pv = cr.getPoint();
            if (pv != null ? pv.getId() > 0 : false) {
                pv = (YvsBasePointVente) dao.loadOneByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{pv.getId()});
                if (pv != null ? pv.getId() > 0 : false) {
                    commissionFor = pv.getCommissionFor();
                }
            }
        }
        if (commissionFor == 'C') { //Commerciale est celui rattaché au user en cours
            YvsComComerciale y = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findByUser", new String[]{"user"}, new Object[]{selectDoc.getEnteteDoc().getCreneau().getUsers()});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                CommercialVente bean = new CommercialVente();
                bean.setFacture(docVente);
                bean.setTaux(100);
                bean.setResponsable(true);
                bean.setCommercial(UtilCom.buildBeanCommerciales(y));
                saveNewCommercial(bean, true, msg);
            } else if (msg) {
                getErrorMessage("Le vendeur n'a pas de compte en tant que commercial");
            }
        } else {
            if (pv != null ? pv.getId() > 0 : false) {
                double taux = pv.getCommerciaux().size() > 0 ? (100 / pv.getCommerciaux().size()) : 0;
                CommercialVente bean;
                for (YvsComCommercialPoint y : pv.getCommerciaux()) {
                    bean = new CommercialVente();
                    bean.setFacture(docVente);
                    bean.setTaux(taux);
                    bean.setResponsable(true);
                    bean.setCommercial(UtilCom.buildBeanCommerciales(y.getCommercial()));
                    saveNewCommercial(bean, true, msg);
                }
            }
        }
    }

    public void saveNewCout() {
        try {
            CoutSupDoc bean = recopieViewCoutSupDoc();
            if (controleFicheCout(bean) && hasDroitUpdateFacture(selectDoc)) {
                selectCout = buildCoutSupDocVente(bean);
                if (!bean.isUpdate()) {
                    selectCout.setId(null);
                    selectCout = (YvsComCoutSupDocVente) dao.save1(selectCout);
                    cout.setId(selectCout.getId());
                } else {
                    dao.update(selectCout);
                }
                int idx = docVente.getCouts().indexOf(selectCout);
                double montant = (selectCout.getTypeCout().getAugmentation() ? selectCout.getMontant() : 0);
                if (idx > -1) {
                    YvsComCoutSupDocVente c = docVente.getCouts().get(docVente.getCouts().indexOf(selectCout));
                    docVente.getCouts().set(idx, selectCout);
                    docVente.setMontantCS(docVente.getMontantCS() + montant + (c.getTypeCout().getAugmentation() ? -c.getMontant() : 0));

                } else {
                    docVente.getCouts().add(0, selectCout);
                    docVente.setMontantCS(docVente.getMontantCS() + montant);
                }
                resetFicheCout();
                setMontantTotalDoc(docVente, docVente.getContenusSave(), currentAgence.getSociete().getId(), null, null, dao);
                docVente.setNbreCout(docVente.getCouts().size());
                update("btn-open_service");
                succes();
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
                update("blog_form_montant_doc_fv");
                if (page.equals("V3")) {
                    update("blog_form_cout_facture_vente");
                } else {
                    update("tabview_facture_vente:blog_form_cout_facture_vente");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveNewReglement(boolean deletePhase) {
        boolean update = reglement.getId() > 0;
        try {
            ManagedReglementVente service = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
            if (service != null) {
                PieceTresorerie bean = recopieViewPiece();
                //si on est en train de modifier un règlement, on vérifie si l'enregistrement précédent avait des phases de règlement bancaire
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

                YvsComptaCaissePieceVente piec = UtilCompta.buildTresoreriVente(bean, currentUser);
                //si on est en cr&ation, il vaut mieu que le statut de départ soit 'W'
                if (bean.getId() < 1 ? (selectReglement != null ? !selectReglement.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) : true) : true) {
                    piec.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                }
                //seul les paiement espèces peuvent passer avec un statut payer immédiatement
                if (piec.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && !bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
                    piec.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    getWarningMessage("Seuls les règlements en espèces peuvent être validé avec ce schéma !");
                } else if (piec.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
                    if (!autoriser("encais_piece_comp")) {
                        openNotAccesAction("Vous ne disposez pas de privillège pour effectuer cette opération !");
                        return;
                    }
                }
                if (bean.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                    if (currentParamVente != null ? !currentParamVente.getPaieWithoutValide() ? !docVente.getStatut().equals(Constantes.ETAT_VALIDE) : false : false) {
                        piec.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                        getWarningMessage("La facture doit etre au préalable validée");
                    } else {
//                        if (controleAccesCaisse(piec.getCaisse(), true)) {
                        piec.setValideBy(currentUser.getUsers());
                        if (piec.getCaissier() != null ? piec.getCaissier().getId() < 1 : true) {
                            piec.setCaissier(currentUser.getUsers());
                        }
                        piec.setDatePaiement(piec.getDatePaimentPrevu());
                        piec.setDateValide(new Date());
//                        } else {
//                            piec.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
//                            getWarningMessage("Vous n'avez pas d'autorisation pour efectuer des règlements dans cette caisse");
//                        }
                    }
                }
                piec.setVente(selectDoc);
                piec = service.createNewPieceCaisse(docVente, piec, deletePhase);
                if (piec != null ? piec.getId() > 0 : false) {
                    if (!update) {
                        docVente.getReglements().add(0, piec);
                    } else {
                        int idx = docVente.getReglements().indexOf(piec);
                        if (idx >= 0) {
                            docVente.getReglements().set(idx, piec);
                        } else {
                            docVente.getReglements().add(piec);
                        }
                    }
                    int idx = documents.indexOf(selectDoc);
                    if (idx >= 0) {
                        int idx1 = documents.get(idx).getReglements().indexOf(piec);
                        if (idx1 >= 0) {
                            documents.get(idx).getReglements().set(idx1, piec);
                        } else {
                            documents.get(idx).getReglements().add(0, piec);
                        }
                    }
                    if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) && reglement.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                        service.reglerPieceTresorerie(docVente, piec, "F", true);
                    } else {
                        succes();
                    }
                }
                selectDoc.setStatutRegle(docVente.getStatutRegle());
                selectDoc.setStatutLivre(docVente.getStatutLivre());
                docVente.setNbreReglement(docVente.getReglements().size());
                update("btn-open_reglement");
                resetFicheReglement(true);
            }
        } catch (Exception ex) {
            getErrorMessage(update ? "Modification" : "Insertion" + " Impossible !");
            getException("Lymytz Error...", ex);
        }
    }

    public void saveNewAllMensualite() {
        try {
            ManagedReglementVente m = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
            if (m != null) {
                boolean correct_ = false;
                for (YvsComptaCaissePieceVente r : docVente.getReglements()) {
                    if (r != null) {
                        int pos = docVente.getReglements().indexOf(r);
                        r.setId((long) 0);
                        r = m.createNewPieceCaisse(docVente, r, false);
                        if (r != null ? r.getId() > 0 : false) {
                            docVente.getReglements().set(pos, r);
                            int idx = documents.indexOf(new YvsComDocVentes(docVente.getId()));
                            if (idx >= 0) {
                                int idx1 = documents.get(idx).getReglements().indexOf(r);
                                if (idx1 >= 0) {
                                    documents.get(idx).getReglements().set(idx1, r);
                                } else {
                                    documents.get(idx).getReglements().add(0, r);
                                }
                            }
                            correct_ = true;
                        }
                    }
                }
                newMensualite = false;
                if (correct_) {
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void genereMensualite() {
        if ((docVente != null) ? docVente.getId() > 0 : false) {
            docVente.getReglements().clear();

            if ((docVente.getModeReglement() != null) ? docVente.getModeReglement().getId() > 0 : false) {
                ManagedReglementVente m = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                if (m != null) {
                    Caisses caisse = reglement.getCaisse();
                    if (caisse != null ? caisse.getId() < 1 : true) {
                        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                        if (service != null) {
                            reglement.setCaisse(UtilCompta.buildSimpleBeanCaisse(service.findByResponsable(selectEntete.getCreneau().getUsers())));
                        }
                    }
                    docVente.setReglements(m.generetedPiecesFromModel(new YvsBaseModelReglement(docVente.getModeReglement().getId(), docVente.getModeReglement().getDesignation()), docVente, new YvsBaseCaisse(caisse.getId())));
                    newMensualite = true;
                    reglement.setMontant(0);
                    reglement.setDatePaiement(new Date());
                    succes();
                    if (page.equals("V3")) {
                        update("form_mensualite_facture_vente");
                    } else {
                        update("tabview_facture_vente:form_mensualite_facture_vente");
                    }
                }
            } else {
                getErrorMessage("Vous devez preciser le model de reglement");
            }
        } else {
            getErrorMessage("Vous devez au préalable enregistrer la facture");
        }
    }

    public void addCommentaireContenu(YvsComContenuDocVente y) {
        on_rabais = false;
        selectContenu = y;
        commentaire = y.getCommentaire();
        update("txt_commentaire_contenu_facture_vente");
    }

    public void addCommentaireContenu() {
        if (!on_rabais) {
            if (selectContenu != null ? selectContenu.getId() > 0 : false) {
                if (selectContenu.getRabais() > 0 && (commentaire != null ? commentaire.trim().length() < 1 : true)) {
                    getErrorMessage("Impossible d'effacer ce commentaire");
                    return;
                }
                selectContenu.setCommentaire(commentaire);
                selectContenu.setDateUpdate(new Date());
                selectContenu.setAuthor(currentUser);
                dao.update(selectContenu);
                if (docVente.getContenus().contains(selectContenu)) {
                    docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
                }
                if (page.equals("V3")) {
                    update("data_contenu_facture_vente");
                } else {
                    update("tabview_facture_vente:data_contenu_facture_vente");
                }
                succes();
            } else {
                getErrorMessage("Vous devez selectionner un contenu");
            }
        } else {
            if (!autoriser("fv_apply_rabais")) {
                openNotAcces();
                return;
            }
            contenu.setRabais(contenu.getPrixMin() - contenu.getPrix());
            contenu.setPrix(contenu.getPrixMin());
            contenu.setCommentaire(commentaire);
            saveNewContenu(true);
        }
    }

    public void addNumSerieContenu(YvsComContenuDocVente y) {
        selectContenu = y;
        numSerie = y.getNumSerie();
        update("txt_num_serie_contenu_facture_vente");
    }

    public void addNumSerieContenu() {
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setNumSerie(numSerie);
            selectContenu.setAuthor(currentUser);
            dao.update(selectContenu);
            if (docVente.getContenus().contains(selectContenu)) {
                docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
            }
            if (page.equals("V3")) {
                update("data_contenu_facture_vente");
            } else {
                update("tabview_facture_vente:data_contenu_facture_vente");
            }
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addRabaisContenu(YvsComContenuDocVente y) {
        selectContenu = y;
        montant_rabais = y.getRabais();
        commentaire = y.getCommentaire();
        update("txt_rabais_contenu_facture_vente");
    }

    public void addRabaisContenu() {
        if (!autoriser("fv_apply_rabais")) {
            openNotAcces();
            return;
        }
        if ((selectContenu.getPrix() - montant_rabais) < selectContenu.getPuvMin()) {
            if (docVente.getEnteteDoc().getPoint().isPrixMinStrict() ? !autoriser("fv_can_reduce_prix") : false) {
                getErrorMessage("Vous ne pouvez réduire le prix de vente de cet article au delà de prix minimale !", "A cause d'une restriction d'accès ou d'une limitation au niveau du point de vente");
                return;
            }
        }
        if (docVente.getStatut().equals(Constantes.ETAT_VALIDE)) {
            getErrorMessage("Le document doit etre éditable pour pouvoir etre modifié");
            return;
        }
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            if (montant_rabais > 0 && (commentaire != null ? commentaire.trim().length() < 1 : true)) {
                getErrorMessage("Impossible d'effacer ce commentaire");
                return;
            }
            selectContenu.setRabais(montant_rabais);
            selectContenu.setCommentaire(montant_rabais > 0 ? commentaire : null);
            selectContenu.setDateUpdate(new Date());
            selectContenu.setAuthor(currentUser);
            //recalcul la taxe et applique le rabais au total
            selectContenu.setPrixTotal(prixTotal(selectContenu));
            selectContenu.setTaxe(dao.getTaxe(selectContenu.getArticle().getId(), selectContenu.getDocVente().getCategorieComptable().getId(), 0, selectContenu.getRemise(), selectContenu.getQuantite(), (selectContenu.getPrix() - montant_rabais), true, 0));
            dao.update(selectContenu);
            if (docVente.getContenus().contains(selectContenu)) {
                docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
            }
            if (docVente.getContenusSave().contains(selectContenu)) {
                docVente.getContenusSave().set(docVente.getContenusSave().indexOf(selectContenu), selectContenu);
            }
            setMontantTotalDoc(docVente, docVente.getContenusSave(), currentAgence.getSociete().getId(), null, null, dao);
            update("chp_fv_net_a_payer");
            update("value_ttc_facture");
            update("value_reste_a_payer_facture");
            if (page.equals("V3")) {
                update("data_contenu_facture_vente");
            } else {
                update("tabview_facture_vente:data_contenu_facture_vente");
            }
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addRemiseContenu(YvsComContenuDocVente y) {
        ManagedRemise w = (ManagedRemise) giveManagedBean(ManagedRemise.class);
        if (w != null) {
            w.loadAllRemiseActif(true);
            update("value-select_remise_fv");
        }
        selectContenu = y;
        montant_remise = y.getRemise();
        taux_remise = y.getTauxRemise();
        update("txt_remise_contenu_facture_vente");
    }

    public void addRemiseContenu() {
        if (docVente.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
            getErrorMessage("Impossible d'appliquer une remise sur une facture déjà Réglé !");
            return;
        }
        if (!autoriser("fv_apply_remise")) {
            openNotAcces();
            return;
        }
        //vérifier si j'ai le droit d'accès sur cette remise    
        ManagedRemise m = (ManagedRemise) giveManagedBean(ManagedRemise.class);
        YvsComRemise y = null;
        int idx = 0;
        if (m != null) {
            idx = m.getRemises().indexOf(new YvsComRemise(remiseContenu));
            if (idx >= 0) {
                y = m.getRemises().get(idx);
                if (!acces(y.getCodeAcces())) {
                    getErrorMessage("Vous n'êtes pas autorisé à accéder à cet élément !");
                    return;
                }
            }
        }

        if (docVente.isPropagerRemise()) {
            if (remiseContenu > 0) {
                if (m != null && y != null) {
                    for (YvsComContenuDocVente c : docVente.getContenus()) {
                        double remise = getMontantRemise(y, c);
                        c.setRemise(remise);
                        c.setAuthor(currentUser);
                        c.setPrixTotal(prixTotal(c));
                        c.setDateUpdate(new Date());
                        dao.update(c);
                    }
//                    loadRemise(selectDoc);
                    update("chp_fv_net_a_payer");
                    update("value_ttc_facture");
                    update("value_reste_a_payer_facture");
                    if (page.equals("V3")) {
                        update("data_contenu_facture_vente");
                    } else {
                        update("tabview_facture_vente:data_contenu_facture_vente");
                    }
                    succes();
                }
            }
        } else if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setAuthor(currentUser);
            selectContenu.setDateUpdate(new Date());
            selectContenu.setPrixTotal(selectContenu.getPrixTotal() + (selectContenu.getRemise()) - montant_remise);
            selectContenu.setRemise(montant_remise);
            dao.update(selectContenu);
            if (docVente.getContenus().contains(selectContenu)) {
                docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
            }
            if (docVente.getContenusSave().contains(selectContenu)) {
                docVente.getContenusSave().set(docVente.getContenusSave().indexOf(selectContenu), selectContenu);
            }
            setMontantTotalDoc(docVente, docVente.getContenusSave(), currentAgence.getSociete().getId(), null, null, dao);
//            loadRemise(selectDoc);
            update("chp_fv_net_a_payer");
            update("value_ttc_facture");
            update("value_reste_a_payer_facture");
            if (page.equals("V3")) {
                update("data_contenu_facture_vente");
            } else {
                update("tabview_facture_vente:data_contenu_facture_vente");
            }
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addBonusContenu(YvsComContenuDocVente y) {
        bonus = UtilCom.buildBeanContenuDocVente(y);
        if (bonus.getArticleBonus() != null ? bonus.getArticleBonus().getId() < 1 : true) {
            cloneObject(bonus.getArticleBonus(), bonus.getArticle());
        }
        if (bonus.getConditionnementBonus() != null ? bonus.getConditionnementBonus().getId() < 1 : true) {
            cloneObject(bonus.getConditionnementBonus(), bonus.getConditionnement());
        }
        bonus.getArticleBonus().setSelectArt(true);
        searchArticle(true);
        update("txt_bonus_contenu_facture_vente");
    }

    public void addBonusContenu() {
        if (!autoriser("fv_apply_remise")) {
            openNotAcces();
            return;
        }
        if (docVente.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
            getErrorMessage("Impossible d'ajouter un bonus sur une facture déjà livré !");
            return;
        }
        if (bonus != null ? bonus.getId() > 0 : false) {
            if ((bonus.getArticleBonus() != null) ? bonus.getArticleBonus().getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner l' article");
                return;
            }
            if ((bonus.getConditionnementBonus() != null) ? bonus.getConditionnementBonus().getId() < 1 : true) {
                getErrorMessage("Vous devez specifier le conditionnement");
                return;
            }
            YvsComContenuDocVente y = UtilCom.buildContenuDocVente(bonus, currentUser);
            dao.update(y);
            y.getArticle().getConditionnements().addAll(bonus.getArticle().getConditionnements());
            int idx = docVente.getContenus().indexOf(y);
            if (idx > -1) {
                docVente.getContenus().set(idx, y);
            }
            if (page.equals("V3")) {
                update("data_contenu_facture_vente");
            } else {
                update("tabview_facture_vente:data_contenu_facture_vente");
            }
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    @Override
    public void deleteBean() {
        try {
            if (!autoriser("fv_delete_doc")) {
                openNotAcces();
                return;
            }
            if (selections != null) {
                List<YvsComDocVentes> list = new ArrayList<>();
                for (YvsComDocVentes bean : selections) {
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    if (!_controleFiche_(bean)) {
                        continue;
                    }
                    dao.delete(bean);

                    if (bean.getId().equals(docVente.getId())) {
                        resetFiche();
                        update("blog_form_facture_vente");
                    }
                    nbreFacture--;
                }
                documents.removeAll(list);
                succes();
                selections.clear();
                update("data_facture_vente");
                if (venteDirecte) {
                    update("blog_btn_action_facture");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComDocVentes y) {
        selectDoc = y;
    }

    public void deleteBean_() {
        try {
            if (!autoriser("fv_delete_doc")) {
                openNotAcces();
                return;
            }
            if (selectDoc != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                selectDoc.setAuthor(currentUser);
                selectDoc.setDateUpdate(new Date());
                dao.delete(selectDoc);
                documents.remove(selectDoc);
                if (selectDoc.getId() == docVente.getId()) {
                    resetFiche();
                    update("blog_form_facture_vente");
                }
                nbreFacture--;
                succes();
                update("data_facture_vente");
                if (venteDirecte) {
                    update("blog_btn_action_facture");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenu() {
        try {
            if ((tabIds_contenu != null) ? !tabIds_contenu.equals("") : false) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                String[] tab = tabIds_contenu.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComContenuDocVente bean = docVente.getContenus().get(docVente.getContenus().indexOf(new YvsComContenuDocVente(id)));
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    dao.delete(bean);
                    docVente.getContenus().remove(bean);
                    docVente.getContenusSave().remove(bean);
                    if (id == contenu.getId()) {
                        resetFicheContenu();
                        update("tabview_facture_vente:form_contenu_facture_vente");
                        update("form_contenu_facture_vente");
                    }
                }
                setMontantTotalDoc(docVente, docVente.getContenusSave(), currentAgence.getSociete().getId(), null, null, dao);
                succes();
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
                if (page.equals("V3")) {
                    update("data_contenu_facture_vente");
                } else {
                    update("tabview_facture_vente:data_contenu_facture_vente");
                }
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
                for (YvsComContenuDocVente c : selectContenus) {
                    dao.delete(c);
                    docVente.getContenus().remove(c);
                    if (contenu.getId() == c.getId()) {
                        resetFicheContenu();
                    }
                }
                succes();
            }
        } else {
            openDialog("dlgConfirmDeleteArticles");
        }
    }

    public void deleteBeanContenu_(YvsComContenuDocVente y) {
        selectContenu = y;
    }

    public void deleteBeanContenu_() {
        try {
            if (selectContenu != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                selectContenu.setAuthor(currentUser);
                selectContenu.setDateUpdate(new Date());
                dao.delete(selectContenu);
                docVente.getContenus().remove(selectContenu);
                docVente.getContenusSave().remove(selectContenu);
                if (selectContenu.getId() == contenu.getId()) {
                    resetFicheContenu();
                    update("tabview_facture_vente:form_contenu_facture_vente");
                    update("form_contenu_facture_vente");
                }
                setMontantTotalDoc(docVente, docVente.getContenusSave(), currentAgence.getSociete().getId(), null, null, dao);
                succes();
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
                if (page.equals("V3")) {
                    update("data_contenu_facture_vente");
                } else {
                    update("tabview_facture_vente:data_contenu_facture_vente");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void activeValidationMultiple(YvsComDocVentes dv) {
        selectDoc = dv;
        if (dv.getStatut().equals(Constantes.ETAT_EDITABLE)) {
            if (dv.getEtapesValidations().isEmpty()) {
                List<YvsWorkflowEtapeValidation> etapes = getAllEtapeValidation();
                selectDoc.setEtapeTotal(etapes != null ? etapes.size() : 0);
                selectDoc.setEtapesValidations(saveEtapesValidation(dv, etapes));
                docVente.setEtapesValidations(new ArrayList<>(selectDoc.getEtapesValidations()));
                dv.setEtapesValidations(new ArrayList<>(selectDoc.getEtapesValidations()));
                update("etapes_valide_facture_vente");
                if (!dv.getEtapesValidations().isEmpty()) {
                    succes();
                } else {
                    getWarningMessage("Le workflow de validation des ventes n'a pas été paramétré !");
                }
            } else {
                getErrorMessage("Ce document est déjà soumis à la validation multiple !");
            }
        } else {
            getErrorMessage("Le document selectionné n'est plus editable !");
        }
    }

    public void deleteBeanRemise() {
        try {
            if (!_controleFiche_(selectDoc)) {
                return;
            }
            if ((tabIds_remise != null) ? !tabIds_remise.equals("") : false) {
                String[] tab = tabIds_remise.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComRemiseDocVente bean = remisesFacture.get(remisesFacture.indexOf(new YvsComRemiseDocVente(id)));
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    dao.delete(bean);
                    remisesFacture.remove(bean);
                }
                succes();
                resetFicheRemise();
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
                update("data_remise_vente");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanRemise_(YvsComRemiseDocVente y) {
        selectRemiseFacture = y;
    }

    public void deleteBeanRemise_() {
        try {
            if (!_controleFiche_(selectDoc)) {
                return;
            }
            if (selectRemiseFacture != null) {
                selectRemiseFacture.setAuthor(currentUser);
                selectRemiseFacture.setDateUpdate(new Date());
                dao.delete(selectRemiseFacture);
                remisesFacture.remove(selectRemiseFacture);

                succes();
                resetFicheRemise();
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
                update("data_remise_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanCommercial() {

    }

    public void deleteBeanCommercial_(YvsComCommercialVente y) {
        selectCommercial = y;
    }

    public void deleteBeanCommercial_() {
        try {
            if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE) && (!autoriser("fv_save_doc"))) {
                getErrorMessage("La facture sélectionnée est déjà validé!");
                return;
            }
            if (selectCommercial != null) {
                selectCommercial.setAuthor(currentUser);
                selectCommercial.setDateUpdate(new Date());
                dao.delete(selectCommercial);
                docVente.getCommerciaux().remove(selectCommercial);
                selectDoc.getCommerciaux().remove(selectCommercial);
                if (selectCommercial.getResponsable() && !docVente.getClient().isSuiviComptable()) {
                    docVente.setTiers(new Client());
                    YvsComClient tiers = null;
                    if (selectEntete.getCreneau().getUsers() != null ? (selectEntete.getCreneau().getUsers().getCommercial() != null ? (selectEntete.getCreneau().getUsers().getCommercial().getTiers() != null) : false) : false) {
                        if (selectEntete.getCreneau().getUsers().getCommercial().getTiers().getClients() != null ? !selectEntete.getCreneau().getUsers().getCommercial().getTiers().getClients().isEmpty() : false) {
                            tiers = selectEntete.getCreneau().getUsers().getCommercial().getTiers().getClients().get(0);
                        }
                    }
                    if (tiers != null ? tiers.getId() > 0 : false) {
                        docVente.setTiers(new Client(tiers.getId()));
                    }
                    Options[] param = new Options[]{new Options(docVente.getId(), 1)};
                    String query = "update yvs_com_doc_ventes set tiers = null where id = ?";
                    if (docVente.getTiers().getId() > 0) {
                        param = new Options[]{new Options(docVente.getTiers().getId(), 1), new Options(docVente.getId(), 2)};
                        query = "update yvs_com_doc_ventes set tiers = ? where id = ?";
                    }
                    dao.requeteLibre(query, param);
                    selectDoc.setTiers(new YvsComClient(docVente.getTiers().getId()));
                }
                int idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
                if (commercial.getId() == selectCommercial.getId()) {
                    resetFicheCommercial();
                }
                docVente.setNbreCommerciaux(docVente.getCommerciaux().size());
                update("btn-open_commercial");
                succes();
                update("data_commercial_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanReglement() {
        try {
            if ((tabIds_mensualite != null) ? !tabIds_mensualite.equals("") : false) {
                String[] tab = tabIds_mensualite.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComptaCaissePieceVente bean = docVente.getReglements().get(docVente.getReglements().indexOf(new YvsComptaCaissePieceVente(id)));
                    boolean comptabilise = dao.isComptabilise(bean.getId(), Constantes.SCR_CAISSE_ACHAT);
                    if (comptabilise) {
                        getErrorMessage("Cette piece est déja comptabilisée");
                        return;
                    }
                    if (bean.getId() > 0) {
                        bean.setAuthor(currentUser);
                        bean.setDateUpdate(new Date());
                        dao.delete(bean);
                    }
                    docVente.getReglements().remove(bean);
                }
                succes();
                setMontantTotalDoc(docVente, docVente.getContenusSave(), currentAgence.getSociete().getId(), null, null, dao);
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
                if (selectReglement.getVerouille()) {
                    getErrorMessage("Cette piece est vérouillé !");
                    return;
                }
                if (!selectReglement.getStatutPiece().equals(Constantes.STATUT_DOC_ATTENTE)) {
                    getErrorMessage("La pièce de règlement n'est pas dans un état éditable !");
                    return;
                }
                boolean comptabilise = dao.isComptabilise(selectReglement.getId(), Constantes.SCR_CAISSE_VENTE);
                if (comptabilise) {
                    getErrorMessage("Cette piece est déja comptabilisé !");
                    return;
                }
                if (selectReglement.getId() > 0) {
                    selectReglement.setAuthor(currentUser);
                    selectReglement.setDateUpdate(new Date());
                    dao.delete(selectReglement);
                }
                docVente.getReglements().remove(selectReglement);
                docVente.setNbreReglement(docVente.getReglements().size());
                setMontantTotalDoc(docVente, docVente.getContenusSave(), currentAgence.getSociete().getId(), null, null, dao);
                update("btn-open_reglement");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanCout() {
        try {
            if ((tabIds_cout != null) ? !tabIds_cout.equals("") : false) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                String[] tab = tabIds_cout.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComCoutSupDocVente bean = docVente.getCouts().get(docVente.getCouts().indexOf(new YvsComCoutSupDocVente(id)));
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    dao.delete(bean);
                    docVente.getCouts().remove(bean);
                    docVente.setMontantCS(docVente.getMontantCS() - (bean.getTypeCout().getAugmentation() ? bean.getMontant() : 0));
                    if (cout.getId() == id) {
                        resetFicheCout();
                    }
                }
                succes();
                setMontantTotalDoc(docVente, docVente.getContenusSave(), currentAgence.getSociete().getId(), null, null, dao);
                update("blog_form_montant_doc_fv");
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanCout_(YvsComCoutSupDocVente y) {
        selectCout = y;
    }

    public void deleteBeanCout_() {
        try {
            if (selectCout != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                docVente.setMontantCS(docVente.getMontantCS() - (selectCout.getTypeCout().getAugmentation() ? selectCout.getMontant() : 0));
                selectCout.setAuthor(currentUser);
                selectCout.setDateUpdate(new Date());
                dao.delete(selectCout);
                docVente.getCouts().remove(selectCout);
                if (cout.getId() == selectCout.getId()) {
                    resetFicheCout();
                }
                docVente.setNbreCout(docVente.getCouts().size());
                succes();
                setMontantTotalDoc(docVente, docVente.getContenusSave(), currentAgence.getSociete().getId(), null, null, dao);
                update("blog_form_montant_doc_fv");
                update("btn-open_service");
                update("tabview_livraison_vente:data_cout_facture_vente");
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
                n.naviguationView("Factures Vente", "modGescom", "smenFactureVente", true);
            }
        }
    }

    // Chargement de la vue.... meme a distance
    @Override
    public void onSelectObject(YvsComDocVentes y) {
        onSelectObject(y, true);
    }

    public void displayDetailsClient() {
        if (selectDoc != null ? selectDoc.getClient() != null : false) {
            ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
            if (w != null) {
                w.creance(selectDoc.getClient());
            }
            selectDoc.getClient().setNbrFactureImpayee((Long) dao.loadObjectByNameQueries("YvsComDocVentes.countFactureImpayeByClient", new String[]{"client"}, new Object[]{selectDoc.getClient()}));
            displayDetailClient = true;
        }
    }

    public void onSelectObject(YvsComDocVentes y, boolean complet) {
        y.setContenus(dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{y}));
//        if (l != null ? !l.isEmpty() : false) {
//            y = l.get(0).getDocVente();

////        }
        selectDoc = y;
////        entete_ = selectDoc.getEnteteDoc().getId();
////        selectEntete = selectDoc.getEnteteDoc();
        if (venteDirecte) {
            complet = false;
        }
        if (complet) {
            loadOthersDetailDoc(selectDoc);
        }
////        equilibre(selectDoc);
        if (selectDoc.getEtapeTotal() > 0) {
            selectDoc.setEtapesValidations(ordonneEtapes(dao.loadNameQueries("YvsWorkflowValidFactureVente.findByFacture", new String[]{"facture"}, new Object[]{selectDoc})));
        }
        DocVente doc = UtilCom.buildSimpleBeanDocVente(selectDoc);
        if (doc.getNature().equals(Constantes.LOCATION)) {
//            YvsComDocVentesInformations information = (YvsComDocVentesInformations)dao.loadOneByNameQueries("YvsComDocVentesInformations.findByFacture", new String[]{"facture"}, new Object[]{y});
//            doc.setInformation(UtilCom.buildBeanDocVenteInformation(information));
        }
        if (doc.getDepot().getId() > 0 && !depotsLivraison.contains(y.getDepotLivrer())) {
            depotsLivraison.add(y.getDepotLivrer());
        }
        populateView(doc);
        Long count = (Long) dao.loadObjectByNameQueries("YvsComptaCaissePieceVente.countByMensualite", new String[]{"vente"}, new Object[]{selectDoc});
        docVente.setNbreReglement(count != null ? count : 0);
        count = (Long) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.countByDocVente", new String[]{"docVente"}, new Object[]{selectDoc});
        docVente.setNbreCout(count != null ? count : 0);
        count = (Long) dao.loadObjectByNameQueries("YvsComCommercialVente.countByFacture", new String[]{"facture"}, new Object[]{selectDoc});
        docVente.setNbreCommerciaux(count != null ? count : 0);
        count = (Long) dao.loadObjectByNameQueries("YvsComDocVentes.countBLVByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
        docVente.setNbreLivraison(count != null ? count : 0);
        count = (Long) dao.loadObjectByNameQueries("YvsComDocVentes.countNotBLVByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
        docVente.setNbreDocLie(count != null ? count : 0);
        if (y.getDocumentLie() != null ? y.getDocumentLie().getId() > 0 : false) {
            docVente.setNbreDocLie(docVente.getNbreDocLie() + 1);
        }
        docVente.setComptabilise(dao.isComptabilise(docVente.getId(), Constantes.SCR_VENTE));
////        if (y.getDepotLivrer() != null ? y.getDepotLivrer().getId() > 0 : false) {
////            loadAllTranche(y.getDepotLivrer(), docVente.getDateLivraisonPrevu());
////        }
////        populateViewEntete(docVente.getEnteteDoc(), complet);
        ManagedStockArticle s = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (s != null) {
            s.setEntityPoint(y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint());
        }
//        chooseEntete(selectEntete);
        if (!vendeurs.contains(y.getEnteteDoc().getCreneau().getUsers())) {
            vendeurs.add(y.getEnteteDoc().getCreneau().getUsers());
        }
        loadCaisseFromVendeur(y.getEnteteDoc().getCreneau().getUsers());
        resetSubFiche();
        codeVendeur_ = docVente.getEnteteDoc().getUsers().getCodeUsers();
        operateurVend = "LIKE";
        update("save_entete_facture_vente");
        update("blog_form_facture_vente");
        update("blog_search_fv");
        if (venteDirecte) {
            update("blog_btn_action_facture");
            execute("slideZoneOnClick(null, 'zone_fv_net_a_payer');");
        }
        if (natureVente != null ? natureVente.trim().isEmpty() : true) {
            execute("_slideShow('zone_show_detail_produit')");
        }
        displayDetailClient = false;
    }

    public void onSelectDistantObject(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedBonVente s = (ManagedBonVente) giveManagedBean(ManagedBonVente.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void onSelectDistantHeader(YvsComDocVentes y) {
        if (!autoriser("fv_super_update_header")) {
            openNotAcces();
            return;
        }
        if (y != null ? y.getId() > 0 : false) {
            ManagedVente s = (ManagedVente) giveManagedBean(ManagedVente.class);
            if (s != null) {
                s.onSelectObjectFacture(y);
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Modifier les ventes", "modGescom", "smenUpdateVente", true);
                }
            }
        }
    }

    public void onSelectDistantReglement(YvsComptaCaissePieceVente y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getModel().getTypeReglement()) {
                case Constantes.MODE_PAIEMENT_BANQUE: {
                    ManagedReglementVente s = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (s != null) {
                        s.onSelectObjectForCheque(y);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Suivi des chèques", "modCompta", "smenSuiviRegVente", true);
                        }
                    }
                    break;
                }
                case Constantes.MODE_PAIEMENT_COMPENSATION: {
                    ManagedReglementVente s = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (s != null) {
                        s.onSelectDistant(y);
                    }
                    break;
                }
                case Constantes.MODE_PAIEMENT_SALAIRE: {
                    ManagedRetenue s = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                    ManagedReglementVente sr = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (sr != null) {
                        sr.setSelectedPiece(y);
                    }
                    if (s != null) {
                        s.onSelectObjectByVente(y);
                        Navigations n = (Navigations) giveManagedBean(Navigations.class);
                        if (n != null) {
                            n.naviguationView("Prélèvement", "modRh", "smenPrelevement", true);
                        }
                    }
                    break;
                }
            }
        }
    }

    public void onLoadFactureAchat(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureAchat s = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (s != null) {
                s.onSelectDistant(y);
            }
        }
    }

    public void deleteBeanLie(YvsComDocVentes y) {
        docLie = y;
    }

    public void deleteBeanLie() {
        if (docLie != null ? docLie.getId() > 0 : false) {
            boolean correct = false;
            switch (docLie.getTypeDoc()) {
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        s.setSelectDoc(docLie);
                        correct = s.deleteBean_();
                    }
                    break;
                }
                case Constantes.TYPE_BRV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        s.setType(Constantes.TYPE_BRV);
                        s.setSelectDoc(docLie);
                        correct = s.deleteBean_();
                    }
                    break;
                }
                case Constantes.TYPE_FAV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        s.setType(Constantes.TYPE_FAV);
                        s.setSelectDoc(docLie);
                        correct = s.deleteBean_();
                    }
                    break;
                }
            }
            if (correct) {
                docVente.getDocuments().remove(docLie);
                selectDoc.getDocuments().remove(docLie);
                int idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
                update("tabview_facture_vente:data_livraison_facture_vente");
                update("data_livraison_facture_vente");
            }
        }
    }

    public void onSelectDistantLivraison(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_FV:
                case Constantes.TYPE_BCV: {
                    onSelectObject(y);
                    break;
                }
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        s.onSelectDistant(y);
                    }
                    break;
                }
                case Constantes.TYPE_BRV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        s.onSelectDistant(y, Constantes.TYPE_BRV);
                    }
                    break;
                }
                case Constantes.TYPE_FAV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        s.onSelectDistant(y, Constantes.TYPE_FAV);
                    }
                    break;
                }
            }
        }
    }

    public void onValideDistantLivraisonByForce() {
        if (distant != null ? distant.getId() > 0 : false) {
            switch (distant.getTypeDoc()) {
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        if (s.validerOrderByForce()) {
                            selectDoc = ((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"},
                                    new Object[]{selectDoc.getId()}));
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_facture_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_FAV:
                case Constantes.TYPE_BRV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {

                    }
                    break;
                }
            }
        }
    }

    YvsComDocVentes distant;

    public void onValideDistantLivraison(YvsComDocVentes y) {
        distant = y;
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        y.setDateLivraison(docVente.getDateLivraisonPrevu());
                        if (s.validerOrder(y, false, false, true, null, false)) {
                            selectDoc = ((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"},
                                    new Object[]{selectDoc.getId()}));
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_facture_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_FAV:
                case Constantes.TYPE_BRV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        if (s.validerOrder(y)) {
                            selectDoc = ((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"},
                                    new Object[]{selectDoc.getId()}));
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            update("grp_btn_etat_facture_vente");
                        }
                    }
                    break;
                }
            }
        }
    }

    public void onRefuserDistantLivraison(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_FV: {
                    if (refuserOrder(y, null, true, true)) {
                        selectDoc = ((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"},
                                new Object[]{selectDoc.getId()}));
                        int idx = documents.indexOf(selectDoc);
                        if (idx > -1) {
                            documents.set(idx, selectDoc);
                        }
                        docVente.setStatutLivre(selectDoc.getStatutLivre());
                        update("grp_btn_etat_facture_vente");
                    }
                    break;
                }
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        if (s.refuserOrder(y, false, true, false, false)) {
                            selectDoc = ((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"},
                                    new Object[]{selectDoc.getId()}));
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_facture_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BRV:
                case Constantes.TYPE_FAV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        if (s.refuserOrder(y, true, false, false)) {
                            selectDoc = ((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"},
                                    new Object[]{selectDoc.getId()}));
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public void onAnnulerDistantLivraisonByForce() {
        if (distant != null ? distant.getId() > 0 : false) {
            switch (distant.getTypeDoc()) {
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        if (s.annulerOrderByForce()) {
                            selectDoc = ((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"},
                                    new Object[]{selectDoc.getId()}));
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_facture_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BRV:
                case Constantes.TYPE_FAV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {

                    }
                    break;
                }
            }
        }
    }

    public void onAnnulerDistantLivraison(YvsComDocVentes y) {
        distant = y;
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_FV: {
                    if (annulerOrder(y, null, true)) {
                        selectDoc = ((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"},
                                new Object[]{selectDoc.getId()}));
                        int idx = documents.indexOf(selectDoc);
                        if (idx > -1) {
                            documents.set(idx, selectDoc);
                        }
                        docVente.setStatutLivre(selectDoc.getStatutLivre());
                        update("grp_btn_etat_facture_vente");
                    }
                    break;
                }
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        if (s.annulerOrder(y, false, true, false, false)) {
                            Map<String, String> statuts = dao.getEquilibreVente(selectDoc.getId());
                            if (statuts != null) {
                                selectDoc.setStatutLivre(statuts.get("statut_livre"));
                                selectDoc.setStatutRegle(statuts.get("statut_regle"));
                            }
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                            docVente.setStatutLivre(selectDoc.getStatutLivre());
                            update("grp_btn_etat_facture_vente");
                        }
                    }
                    break;
                }
                case Constantes.TYPE_BRV:
                case Constantes.TYPE_FAV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        if (s.annulerOrder(y, true, false, false)) {
                            selectDoc = ((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"},
                                    new Object[]{selectDoc.getId()}));
                            int idx = documents.indexOf(selectDoc);
                            if (idx > -1) {
                                documents.set(idx, selectDoc);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public void onPrintDistant(YvsComDocVentes y) {
        onPrintDistant(y, true);
    }

    public void onPrintDistant(YvsComDocVentes y, boolean withHeader) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        s.print(y, withHeader);
                    }
                    break;
                }
                case Constantes.TYPE_FAV:
                case Constantes.TYPE_BRV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        s.print(y, withHeader);
                    }
                    break;
                }
            }
        }
    }

    public void selectDocs(YvsComDocVentes doc) {
        if (doc != null ? doc.getId() > 0 : false) {
            selectDoc = doc;
            notes = selectDoc.getNotes();

        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            if (listing) {
                YvsComContenuDocVente y = (YvsComContenuDocVente) ev.getObject();
                onSelectObject(y.getDocVente());
            } else {
                YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
                onSelectObject(y);
            }

        }
    }

    public void loadOnViewSimple(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
            onSelectObject(y, false);
        }
    }

    public void loadOnViewContent(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocVente y = (YvsComContenuDocVente) ev.getObject();
            onSelectObject(y.getDocVente());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_facture_vente");
    }

    public void loadOnViewContenu(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComContenuDocVente bean = (YvsComContenuDocVente) ev.getObject();
//            bean.setArticle((YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{bean.getArticle().getId()}));
            bean.getArticle().setConditionnements(dao.loadNameQueries("YvsBaseConditionnement.findByArticle", new String[]{"article"}, new Object[]{bean.getArticle()}));
            populateViewContenu(UtilCom.buildBeanContenuDocVente(bean));
            if (page.equals("V3")) {
                update("form_contenu_facture_vente");
                update("desc_article_facture_vente");
            } else {
                update("tabview_facture_vente:form_contenu_facture_vente");
            }
        }
    }

    public void unLoadOnViewContenu(UnselectEvent ev) {
        resetFicheContenu();
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

    public void loadOnViewRemise(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComRemiseDocVente bean = (YvsComRemiseDocVente) ev.getObject();
            populateViewRemise(UtilCom.buildBeanRemiseDocVente(bean));
            update("form_remise_vente");
        }
    }

    public void unLoadOnViewRemise(UnselectEvent ev) {
        resetFicheRemise();
        update("form_remise_vente");
    }

    public void loadOnViewCommercialVente(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCommercialVente y = (YvsComCommercialVente) ev.getObject();
            populateView(UtilCom.buildBeanCommercialVente(y));
        }
    }

    public void unLoadOnViewCommercialVente(UnselectEvent ev) {
        resetFicheCommercial();
    }

    public void loadOnViewCout(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCoutSupDocVente bean = (YvsComCoutSupDocVente) ev.getObject();
            populateViewCout(UtilCom.buildBeanCoutSupDocVente(bean));
        }
    }

    public void unLoadOnViewCout(UnselectEvent ev) {
        resetFicheCout();
    }

    public void loadOnViewCommercial(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComComerciale bean = (YvsComComerciale) ev.getObject();
            commercial.setCommercial(UtilCom.buildBeanCommerciales(bean));
            update("txt_commercial_vente");
            update("select_commercial_vente");
        }
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles bean = (YvsBaseArticles) ev.getObject();
            chooseArticle(UtilProd.buildBeanArticles(bean));
            listArt = false;
            if (page.equals("V3")) {
                update("form_contenu_facture_vente");
            } else {
                update("tabview_facture_vente:form_contenu_facture_vente");
            }
        }
    }

    public void loadOnViewConditionnement(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseConditionnement bean = (YvsBaseConditionnement) ev.getObject();
            chooseConditionnement(UtilProd.buildBeanConditionnement(bean));
            listArt = false;
            if (page.equals("V3")) {
                update("form_contenu_facture_vente");
            } else {
                update("tabview_facture_vente:form_contenu_facture_vente");
            }
        }
    }

    public void loadOnViewArticlePack(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            pack = (YvsBaseArticlePack) ev.getObject();
            contenu.setPrix(pack.getMontant());
            if (!contenu.controlTotal()) {
                applyTotal(contenu);
            }
            listArt = false;
            if (page.equals("V3")) {
                update("form_contenu_facture_vente");
            } else {
                update("tabview_facture_vente:form_contenu_facture_vente");
            }
        }
    }

    public void loadOnViewArticleBon(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            selectArt = true;
            YvsComContenuDocVente bean = (YvsComContenuDocVente) ev.getObject();
            boolean _deja = false;
            for (YvsComContenuDocVente c : docVente.getContenus()) {
                if (c.getArticle().equals(bean.getArticle())) {
                    getErrorMessage("Vous avez déja ajouter cet article");
                    _deja = true;
                    break;
                }
            }
            if (!_deja) {
                ContenuDocVente c = UtilCom.buildBeanContenuDocVente(bean);
                c.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, 0, 0, 0, docVente.getEnteteDoc().getDateEntete(), bean.getConditionnement().getId(), bean.getLot().getId()));
                c.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));

                c.setDocVente(null);
                c.setUpdate(false);
                c.setParent(new ContenuDocVente(bean.getId()));
                cloneObject(contenu, c);
                if (page.equals("V3")) {
                    update("form_contenu_facture_vente");
                    update("desc_article_facture_vente");
                } else {
                    update("tabview_facture_vente:form_contenu_facture_vente");
                }
            }
        }
    }

    public void loadOnViewClient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient y = (YvsComClient) ev.getObject();
            chooseClient(UtilCom.buildBeanClient(y));
            update("infos_facture_vente");
            update("txt_zone_client");
        }
    }

    public void loadOnViewCategorie(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseCategorieComptable y = (YvsBaseCategorieComptable) ev.getObject();
            chooseCategorie(UtilCom.buildBeanCategorieComptable(y));
            if (page.equals("V3")) {
                update("blog_form_contenu_facture_vente");
            } else {
                update("tabview_facture_vente:blog_form_contenu_facture_vente");
            }
        }
    }

    public void loadOnViewPointVente(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBasePointVente y = (YvsBasePointVente) ev.getObject();
            docVente.getEnteteDoc().setPoint(UtilCom.buildSimpleBeanPointVente(y));
            choosePoint(y);
            update("save_entete_facture_vente");
        }
    }

    public void loadOnViewDepot(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseDepots y = (YvsBaseDepots) ev.getObject();
            chooseDepot(y);
            update("blog_depot_livraison_fv");
        }
    }

    public void loadOnViewModel(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseModelReglement y = (YvsBaseModelReglement) ev.getObject();
            docVente.setModeReglement(UtilCom.buildBeanModelReglement(y));
            update("select_model_facture_vente");
        }
    }

    public void loadOnViewBon(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocVentes bean = (YvsComDocVentes) ev.getObject();
            bon = bean.getId();
            onSelectBon(bean);
        }
    }

    public void loadOnViewModelPrint(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsPrintFactureVente bean = (YvsPrintFactureVente) ev.getObject();
            model = bean.getNom();
            ManagedPrintFactureVente w = (ManagedPrintFactureVente) giveManagedBean(ManagedPrintFactureVente.class);
            if (w != null) {
                w.setModel(bean.getModel());
                w.onSelectObject(bean);
            }
        }
    }

    public YvsComDocVentes genererRetour(YvsComDocVentes facture, boolean message) {
        YvsComDocVentes retour = new YvsComDocVentes();
        try {
            if (!autoriser("fv_livrer")) {
                if (message) {
                    openNotAcces();
                }
                return retour;
            }
            if (facture == null) {
                if (message) {
                    getErrorMessage("Vous devez selectionner la facture");
                }
                return retour;
            }

            if (facture.getEnteteDoc() == null) {
                if (message) {
                    getErrorMessage("La facture n'a pas de journal de vente");
                }
                return retour;
            }
            List<YvsComContenuDocVente> contenus = loadContenusStayForRetour(facture);
            if (contenus != null ? !contenus.isEmpty() : false) {
                retour = new YvsComDocVentes(facture);
                retour.setId(facture.getId());
                retour.setEnteteDoc(facture.getEnteteDoc());
                retour.setAuthor(currentUser);
                retour.setTypeDoc(Constantes.TYPE_BRL);
                retour.setNumPiece("BRL N° " + facture.getNumDoc());
                retour.setDepotLivrer(facture.getDepotLivrer());
                retour.setTrancheLivrer(facture.getTrancheLivrer());
                retour.setLivreur(currentUser.getUsers());
                retour.setDocumentLie(new YvsComDocVentes(facture.getId(), facture.getNumDoc(), facture.getStatut(), facture.getStatutLivre(), facture.getStatutRegle()));
                retour.setHeureDoc(new Date());
                retour.setStatut(Constantes.ETAT_EDITABLE);
                retour.setStatutLivre(Constantes.ETAT_ATTENTE);
                retour.setStatutRegle(Constantes.ETAT_ATTENTE);
                retour.setValiderBy(currentUser.getUsers());
                retour.setDateValider(facture.getDateLivraisonPrevu());
                retour.setDateSave(new Date());
                retour.setDateUpdate(new Date());
                retour.setCloturer(false);
                retour.setContenus(contenus);
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
        return retour;
    }

    public boolean annulerRetour(YvsComDocVentes entity) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                entity.setCloturer(false);
                entity.setAnnulerBy(null);
                entity.setCloturerBy(null);
                entity.setValiderBy(null);
                entity.setDateAnnuler(null);
                entity.setDateCloturer(null);
                entity.setDateValider(null);
                entity.setStatut(Constantes.ETAT_EDITABLE);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    entity.setAuthor(currentUser);
                }
                List<YvsComContenuDocVente> contenus = new ArrayList<>(entity.getContenus());
                entity.getContenus().clear();
                dao.update(entity);
                entity.setContenus(contenus);
                if (getDocVente().getDocuments() != null) {
                    int idx = getDocVente().getDocuments().indexOf(entity);
                    if (idx > -1) {
                        getDocVente().getDocuments().set(idx, entity);
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedLocationVente (annulerRetour)", ex);
        }
        return false;
    }

    public boolean refuserRetour(YvsComDocVentes entity) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                entity.setCloturer(false);
                entity.setAnnulerBy(null);
                entity.setCloturerBy(null);
                entity.setValiderBy(null);
                entity.setDateAnnuler(null);
                entity.setDateCloturer(null);
                entity.setDateValider(null);
                entity.setStatut(Constantes.ETAT_ANNULE);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    entity.setAuthor(currentUser);
                }
                List<YvsComContenuDocVente> contenus = new ArrayList<>(entity.getContenus());
                entity.getContenus().clear();
                dao.update(entity);
                entity.setContenus(contenus);
                if (getDocVente().getDocuments() != null) {
                    int idx = getDocVente().getDocuments().indexOf(entity);
                    if (idx > -1) {
                        getDocVente().getDocuments().set(idx, entity);
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedLocationVente (refuserRetour)", ex);
        }
        return false;
    }

    public boolean validerRetour(YvsComDocVentes entity) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                entity.setCloturer(false);
                entity.setCloturerBy(null);
                entity.setDateCloturer(null);
                entity.setAnnulerBy(null);
                entity.setDateAnnuler(null);
                entity.setValiderBy(currentUser.getUsers());
                entity.setDateValider(new Date());
                entity.setStatut(Constantes.ETAT_VALIDE);
                if (currentUser != null ? currentUser.getId() > 0 : false) {
                    entity.setAuthor(currentUser);
                }
                List<YvsComContenuDocVente> contenus = new ArrayList<>(entity.getContenus());
                entity.getContenus().clear();
                dao.update(entity);
                entity.setContenus(contenus);
                if (getDocVente().getDocuments() != null) {
                    int idx = getDocVente().getDocuments().indexOf(entity);
                    if (idx > -1) {
                        getDocVente().getDocuments().set(idx, entity);
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedLocationVente (validerRetour)", ex);
        }
        return false;
    }

    public void onSelectBon(YvsComDocVentes y) {
        ManagedBonVente m = (ManagedBonVente) giveManagedBean(ManagedBonVente.class);
        if (m != null) {
            List<YvsComContenuDocVente> contenus = dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{y});
            if (contenus != null ? !contenus.isEmpty() : false) {
                y = contenus.get(0).getDocVente();
                y.setContenus(contenus);
            }
            m.getDocuments().remove(y);
            m.getDocuments().add(0, y);
        }
        if (y.getContenus() != null ? !y.getContenus().isEmpty() : false) {
            contenus_bcv = loadContenusStay(y, Constantes.TYPE_FV);
            update("data_contenu_bcv_fv");
            if (!contenus_bcv.isEmpty()) {
                if (y != null ? y.getId() > 0 : false) {
                    long id = y.getId();
                    String state = y.getStatutLivre();
                    String num = y.getNumDoc();

                    YvsComDocVentes d = y;
                    d.setHeureDoc(new Date());
                    d.setDateLivraison(new Date());
                    d.setDocumentLie(new YvsComDocVentes(id, num, state));
                    d.setTypeDoc(Constantes.TYPE_FV);
                    d.setNumDoc(null);
                    d.setNew_(true);
                    d.setStatut(Constantes.ETAT_EDITABLE);
                    d.setDateSave(new Date());
                    d.setNumPiece("BL N° " + num);
                    d.setDescription("Facturation de la commande N° " + num + " le " + ldf.format(new Date()) + " à " + time.format(new Date()));
                    d.setId((long) -1);
                    d.setContenus(new ArrayList<YvsComContenuDocVente>());
                    cloneObject(docVente, UtilCom.buildBeanDocVente(d));
                    docVente.setUpdate(false);
                    update("chp_fv_net_a_payer");
                    update("value_ttc_facture");
                    update("value_reste_a_payer_facture");
                    update("infos_document_commande_vente");
                }
            } else {
                Map<String, String> statuts = dao.getEquilibreVente(y.getId());
                if (statuts != null) {
                    y.setStatutLivre(statuts.get("statut_livre"));
                    y.setStatutRegle(statuts.get("statut_regle"));
                }
                bon = 0;
                update("blog_is_bon");
                getErrorMessage("Cette commande est déja facturée.. Vérifier les factures");
            }
        } else {
            bon = 0;
            update("blog_is_bon");
            getErrorMessage("Cette commande n'a pas de contenu");
        }
    }

    public void loadOnViewZone(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsDictionnaire bean = (YvsDictionnaire) ev.getObject();
            if (bean.getTitre().equals(Constantes.T_SECTEURS)) {
                if (bean.getParent() != null) {
                    docVente.setVille(UtilGrh.buildBeanDictionnaire(bean.getParent()));
                }
                docVente.setAdresse(UtilGrh.buildBeanDictionnaire(bean));
            } else {
                docVente.setVille(UtilGrh.buildBeanDictionnaire(bean));
            }
            chooseVille();
        }
    }

    public void chooseBon() {
        if (bon > 0) {
            ManagedBonVente m = (ManagedBonVente) giveManagedBean(ManagedBonVente.class);
            if (m != null) {
                if (m.getDocuments().contains(new YvsComDocVentes(bon))) {
                    YvsComDocVentes y = m.getDocuments().get(m.getDocuments().indexOf(new YvsComDocVentes(bon)));
                    onSelectBon(y);
                }
            }
        } else {
            if (bon < 0) {
                openDialog("dlgListBonCommande");
            } else {
                resetFiche();
            }
        }
    }

    public void chooseConditionnement(boolean isBonus) {
        pack = new YvsBaseArticlePack();
        if (!isBonus) {
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
            if (contenu.getConditionnement().getPacks() != null ? !contenu.getConditionnement().getPacks().isEmpty() : false) {
                openDialog("dlgListArticlePack");
                update("data_articles_pack_facture_vente");
            }
        } else {
            if (bonus.getConditionnementBonus() != null ? bonus.getConditionnementBonus().getId() > 0 : false) {
                if (bonus.getArticle() != null) {
                    int idx = bonus.getArticleBonus().getConditionnements().indexOf(new YvsBaseConditionnement(bonus.getConditionnementBonus().getId()));
                    if (idx > -1) {
                        YvsBaseConditionnement y = bonus.getArticleBonus().getConditionnements().get(idx);
                        bonus.setConditionnementBonus(UtilProd.buildBeanConditionnement(y));
                    }
                }
            }
        }
    }

    public void loadInfosArticle(Articles art) {
        selectArt = true;
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
        if (contenu.getConditionnement().getPacks() != null ? !contenu.getConditionnement().getPacks().isEmpty() : false) {
            openDialog("dlgListArticlePack");
            update("data_articles_pack_facture_vente");
            return;
        }
    }

    /*Charge les prix de l'articles*/
    private void loadInfosAfterChooseArticle(ContenuDocVente contenu, Articles art) {
        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
            if (!art.getCategorie().equals(Constantes.CAT_SERVICE)) {
                if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                    art.setStock(dao.stocks(art.getId(), 0, docVente.getDepot().getId(), 0, 0, docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
                } else {
                    art.setStock(dao.stocks(art.getId(), 0, 0, currentAgence.getId(), 0, docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
                }
            }
            art.setPuv(dao.getPuv(art.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId()));
//            contenu.setPrixMin(dao.getPuvMin(art.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId()));
            art.setPua(dao.getPua(art.getId(), 0));
            if (!contenu.isUpdate()) {
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
                update("tabview_facture_vente:form_contenu_facture_vente");
            }
            contenu.setRabais(rabais);
        }
    }

    public ContenuDocVente findPrixArticle(ContenuDocVente c, boolean findPrix) {
        return findPrixArticle(c, findPrix, true);
    }

    public ContenuDocVente findPrixArticle(ContenuDocVente c, boolean findPrix, boolean findStock) {
        if (!isBon) {
            if ((docVente.getClient() != null) ? docVente.getClient().getId() > 0 : false) {
                if ((pack != null ? pack.getId() < 1 : true) && !c.isUpdate() ? findPrix : false) {
                    c.setPrix(dao.getPuv(c.getArticle().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), c.getConditionnement().getId()));
                }
                double prix = c.getPrix() - c.getRabais();
                double _remise = dao.getRemiseVente(c.getArticle().getId(), c.getQuantite(), prix, docVente.getClient().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getUnite().getId());
                double _ristourne = dao.getRistourne(c.getConditionnement().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDateEntete());
                if (selectContenu != null ? selectContenu.getId() > 0 : false) {
                    if (c.getId() > 0 && (selectContenu.getQuantite() == c.getQuantite() && selectContenu.getPrix() == c.getPrix())) {
                        _remise = selectContenu.getRemise();
                    }
                }
                c.setRistourne(_ristourne);
                c.setRemise(_remise);
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
                } else {
                    getWarningMessage("Selectionner la catégorie comptable!");
                }
                applyTotal(c);
                if (findStock ? !c.getArticle().getCategorie().equals(Constantes.CAT_SERVICE) && c.getArticle().getStock() < c.getQuantite() : false) {
                    if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                        champ = new String[]{"article", "depot"};
                        val = new Object[]{new YvsBaseArticles(c.getArticle().getId()), new YvsBaseDepots(docVente.getDepot().getId())};
                        Boolean sellWithOutStock = (Boolean) dao.loadObjectByNameQueries("YvsBaseArticleDepot.findIfSellWithOutStock", champ, val);
                        if (sellWithOutStock != null ? !sellWithOutStock : false) {
                            getErrorMessage("Cet article ne peut etre vendu sans stock dans ce dépot");
                            resetFicheContenu();
                            return c;
                        }
                    }
                    if (!rememberChoixWithOutStock) {
                        openDialog("dlgConfirmContinueWithOutStock");
                    } else {
                        if (!valueContinueWithOutStock) {
                            resetFicheContenu();
                        }
                    }
                }
            } else {
                getWarningMessage("Selectionner le client!");
            }
        }
        return c;
    }

    private void applyTotal(ContenuDocVente c) {
        double prix = c.getPrix() - c.getRabais();
        double total = c.getQuantite() * prix;
        c.setPrixTotal((total - c.getRemise()) + (c.getArticle().isPuvTtc() ? 0 : c.getTaxe()));
    }

    public void continueWithOutStock(boolean valueContinueWithOutStock) {
        if (rememberChoixWithOutStock) {
            this.valueContinueWithOutStock = valueContinueWithOutStock;
        }
        if (!valueContinueWithOutStock) {
            resetFicheContenu();
        }
    }

    public void chooseArticle(Articles art) {
        if ((art != null) ? art.getId() > 0 : false) {
            List<YvsBaseConditionnement> unites = dao.loadNameQueries("YvsBaseConditionnement.findByActifArticle", new String[]{"article"}, new Object[]{new YvsBaseArticles(art.getId())});
            art.setConditionnements(unites != null ? unites : new ArrayList<YvsBaseConditionnement>());
//charge le conditionnement par défaut de la vente      
            if (!isBonus) {
                if (modelFormEditable.fv_conditionnement) { //si le modèle de formulaire autorise la modification de cette information
//                    champ = new String[]{"article", "point"};
//                    val = new Object[]{new YvsBaseArticles(art.getId()), new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId())};
//                    nameQueri = "YvsBaseConditionnementPoint.findConditionnement";
//                    List<YvsBaseConditionnement> la = dao.loadNameQueries(nameQueri, champ, val);
//                    if (la != null ? !la.isEmpty() : false) {
//                        for (YvsBaseConditionnement c : la) {
//                            if (c.getByVente() && c.getDefaut()) {
//                                contenu.setConditionnement(UtilProd.buildBeanConditionnement(c));
//                                break;
//                            }
//                        }
//                        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() < 1 : true) {
//                            contenu.setConditionnement(UtilProd.buildBeanConditionnement(la.get(0)));
//                        }
//                    }
                }
                loadInfosArticle(art);
            } else {
                cloneObject(bonus.getArticleBonus(), art);
                update("txt_bonus_contenu_facture_vente");
            }
        } else {
            resetFicheContenu();
        }
        if (page.equals("V3")) {
            update("desc_article_facture_vente");
        } else {

        }
    }

    private void chooseConditionnement(Conditionnement c) {
        if (c != null ? c.getId() > 0 : false) {
            chooseArticle(c.getArticle());
            cloneObject(contenu.getConditionnement(), c);
            chooseConditionnement(false);
        }
    }

    boolean isBonus, isService;

    public void searchArticle(boolean isBonus) {
        searchArticle(isBonus, false);
    }

    public void searchArticle(boolean isBonus, boolean isService) {
        this.isBonus = isBonus;
        this.isService = isService;
        pack = new YvsBaseArticlePack();
        String num = null;
        if (!isBonus) {
            num = contenu.getArticle().getRefArt();
            contenu.getArticle().setDesignation("");
            contenu.getArticle().setError(true);
            contenu.getArticle().setId(0);
            listArt = false;
            selectArt = false;
        } else {
            num = bonus.getArticleBonus().getRefArt();
            bonus.getArticleBonus().setDesignation("");
            bonus.getArticleBonus().setError(true);
            bonus.getArticleBonus().setId(0);
        }
        if (num != null ? num.trim().length() > 0 : false) {
            ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (m != null) {
                Articles y = null;
                if (!isService) {
                    y = m.searchArticleActifByPoint(num, true);
                } else {
                    y = m.searchArticleActif(num, true);
                }
                if (!isBonus) {
                    listArt = y.isListArt();
                    selectArt = y.isSelectArt();
                }
                if (m.getArticles() != null ? !m.getArticles().isEmpty() : false) {
                    if (m.getArticles().size() > 1) {
                        update("data_articles_facture_vente");
                    } else {
                        chooseArticle(y);
                    }
                    if (!isBonus) {
                        contenu.getArticle().setError(false);
                    } else {
                        bonus.getArticleBonus().setError(false);
                    }
                } else {
                    Conditionnement c = m.searchArticleActifByCodeBarre(num);
                    if (m.getConditionnements() != null ? !m.getConditionnements().isEmpty() : false) {
                        if (m.getConditionnements().size() > 1) {
                            update("data_conditionnements_facture_vente");
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

    public void initArticles(boolean isBonus) {
        this.isBonus = isBonus;
        listArt = false;
        ManagedStockArticle a = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (a != null) {
            if (!isBonus) {
                a.initArticlesByPoint(contenu.getArticle());
                listArt = contenu.getArticle().isListArt();
            } else {
                a.initArticlesByPoint(bonus.getArticleBonus());
            }
        }
        update("data_articles_facture_vente");
    }

    public void openDlgFactures(boolean listing) {
        openDlgFactures(listing, Constantes.VENTE);
    }

    public void openDlgFactures(boolean listing, String natureVente) {
        this.natureVente = natureVente;
        //choisir les documents à charger
        pre = (listing) ? "y.docVente." : "y.";
        this.listing = listing;
//        addParamVendeur(true);
//        initForm = true;
//        loadAllFacture(true);
    }

    public void openDocumentLies() {
        docVente.setDocuments(dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc}));
        if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getId() > 0 : false) {
            docVente.getDocuments().add(0, selectDoc.getDocumentLie());
        }
        update("data_livraison_facture_vente");
    }

    public void openDlgReglement() {
        if (docVente.getEnteteDoc() != null ? docVente.getEnteteDoc().getCrenauHoraire() != null ? docVente.getEnteteDoc().getCrenauHoraire().getPersonnel() != null : false : false) {
            loadCaisseFromVendeur(new YvsUsers(docVente.getEnteteDoc().getCrenauHoraire().getPersonnel().getId()));
        }
        ManagedModeReglement service = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
        if (service != null) {
            service.loadModels("C");
        }
        docVente.setReglements(dao.loadNameQueries("YvsComptaCaissePieceVente.findRegFactByFacture", new String[]{"facture"}, new Object[]{selectDoc}));
        update("form_mensualite_facture_vente");
        update("select_model_mens_facture_vente");
    }

    public void openDlgLivraison() {
        docVente.setDocuments(dao.loadNameQueries("YvsComDocVentes.findBLVByParent", new String[]{"documentLie"}, new Object[]{selectDoc}));
        update("data_livraison_facture_vente");
    }

    public void openDlgDocument() {
        docVente.setDocuments(dao.loadNameQueries("YvsComDocVentes.findNotBLVByParent", new String[]{"documentLie"}, new Object[]{selectDoc}));
        update("data_livraison_facture_vente");
    }

    public void openDlgCommerciaux() {
        docVente.setCommerciaux(dao.loadNameQueries("YvsComCommercialVente.findByFacture", new String[]{"facture"}, new Object[]{selectDoc}));
        update("blog_commercial_vente");
    }

    public void openDlgCoutS() {
        ManagedTypeCout w = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
        if (w != null) {
            w.loadTypeByType(Constantes.COUT_VENTE);
        }
        docVente.setCouts(dao.loadNameQueries("YvsComCoutSupDocVente.findByDocVente", new String[]{"docVente"}, new Object[]{selectDoc}));
        update("blog_form_cout_facture_vente");
    }

    public void searchPointVente() {
        String num = docVente.getEnteteDoc().getPoint().getLibelle();
        docVente.getEnteteDoc().getPoint().setId(0);
        docVente.getEnteteDoc().getPoint().setError(true);
        ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (m != null) {
            PointVente y = m.search(num, true);
            if (m.getPointsvente() != null ? !m.getPointsvente().isEmpty() : false) {
                if (m.getPointsvente().size() > 1) {
                    update("data_point_vente_facture_vente");
                } else {
                    docVente.getEnteteDoc().setPoint(y);
                    choosePoint(m.getPointsvente().get(0));
                }
                docVente.getEnteteDoc().getPoint().setError(false);
            }
        }
    }

    public void searchDepot() {
        String num = docVente.getDepot().getDesignation();
        docVente.getDepot().setId(0);
        docVente.getDepot().setError(true);
        loadDepotByPoint(num, new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId()));
        if (depotsLivraison != null ? !depotsLivraison.isEmpty() : false) {
            if (depotsLivraison.size() > 1) {
                openDialog("dlgListDepots");
                update("data_depot_facture_vente");
            } else {
                docVente.setDepot(UtilCom.buildSimpleBeanDepot(depotsLivraison.get(0)));
                chooseDepot();
            }
            docVente.getDepot().setError(false);
        }
    }

    public void searchClient() {
        String num = docVente.getClient().getNom();
        docVente.getClient().setId(0);
        docVente.getClient().setError(true);
        docVente.getClient().setTiers(new Tiers());
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            Client y = m.searchClient(num, true);
            if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                if (m.getClients().size() > 1) {
                    update("data_client_facture_vente");
                } else {
                    chooseClient(y);
                }
                docVente.getClient().setError(false);
            }
        }
    }

    public void initClients() {
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            m.initClients(docVente.getClient());
        }
        update("data_client_facture_vente");
    }

    public void searchCategorie() {
        String num = docVente.getCategorieComptable().getDesignation();
        docVente.getCategorieComptable().setId(0);
        docVente.getCategorieComptable().setError(true);
        ManagedCatCompt m = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
        if (m != null) {
            CategorieComptable y = m.searchCategorieVente(num, true);
            if (m.getCategoriesVente() != null ? !m.getCategoriesVente().isEmpty() : false) {
                if (m.getCategoriesVente().size() > 1) {
                    update("data_categorie_vente_facture_vente");
                } else {
                    chooseCategorie(y);
                }
                docVente.getCategorieComptable().setError(false);
            }
        }
    }

    public void initCategories() {
        ManagedCatCompt m = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
        if (m != null) {
            m.loadAllCategorieVente();
        }
        update("data_categorie_vente_facture_vente");
    }

    public void searchModelReglement() {
        String num = docVente.getModeReglement().getDesignation();
        docVente.getModeReglement().setId(0);
        docVente.getModeReglement().setError(true);
        ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
        if (m != null) {
            ModelReglement y = m.searchModel(num, true);
            if (m.getModels() != null ? !m.getModels().isEmpty() : false) {
                if (m.getModels().size() > 1) {
                    update("data_model_reglement_facture_vente");
                } else {
                    docVente.setModeReglement(y);
                }
                docVente.getModeReglement().setError(false);
            }
        }
    }

    public void initModelReglements() {
        ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
        if (m != null) {
            m.loadModel("C");
        }
        update("data_model_reglement_facture_vente");
    }

    public void searchCommercial() {
        String num = commercial.getCommercial().getCode();
        commercial.getCommercial().setId(0);
        commercial.getCommercial().setError(true);
        commercial.getCommercial().setNomPrenom("");
        ManagedCommerciaux m = (ManagedCommerciaux) giveManagedBean(ManagedCommerciaux.class);
        if (m != null) {
            Commerciales y = m.searchCommercial(num, true);
            if (m.getCommerciaux() != null ? !m.getCommerciaux().isEmpty() : false) {
                if (m.getCommerciaux().size() > 1) {
                    update("data_commerciaux_facture_vente");
                } else {
                    commercial.setCommercial(y);
                }
                commercial.getCommercial().setError(false);
            }
        }
        update("txt_commercial_vente");
        update("tabview_facture_vente:select_commercial_vente");
    }

    public void initCommerciaux() {
        ManagedCommerciaux m = (ManagedCommerciaux) giveManagedBean(ManagedCommerciaux.class);
        if (m != null) {
            m.initCommerciaux(commercial.getCommercial());
        }
        update("data_commerciaux_facture_vente");
    }

    public void initFacture(DocVente a, Client c) {
        if (a == null) {
            a = new DocVente();
        }
        paginator.addParam(new ParametreRequete("y.numDoc", "numDoc", null));
        if (c != null ? c.getId() > 0 : false) {
            codeClient_ = c.getCodeClient();
            searchByClient();
        } else {
            initForm = true;
            loadAllFacture(true);
        }
        a.setList(true);
    }

    public void init(boolean next) {
        initForm = false;
        loadAllFacture(next);
    }

    public void definedResponsable(YvsComCommercialVente y) {
        if (selectDoc.getStatut().equals(Constantes.ETAT_VALIDE) && (!autoriser("fv_save_doc"))) {
            getErrorMessage("La facture sélectionnée est déjà validé!");
            return;
        }
        if (y != null) {
            if (!y.getResponsable()) {
                for (YvsComCommercialVente c : docVente.getCommerciaux()) {
                    if (c.getResponsable()) {
                        getErrorMessage("Il y'a déja un commercial responsable");
                        return;
                    }
                }
            }
            y.setResponsable(!y.getResponsable());
            String rq = "UPDATE yvs_com_commercial_vente SET responsable= " + y.getResponsable() + " WHERE id=?";
            Options[] param = new Options[]{new Options(y.getId(), 1)};
            dao.requeteLibre(rq, param);
            int idx = docVente.getCommerciaux().indexOf(y);
            if (idx > -1) {
                docVente.getCommerciaux().set(idx, y);
            }
            idx = selectDoc.getCommerciaux().indexOf(y);
            if (idx > -1) {
                selectDoc.getCommerciaux().set(idx, y);
            }
            if (!docVente.getClient().isSuiviComptable()) {
                YvsComClient tiers = null;
                if (y.getResponsable()) {
                    if (y.getCommercial().getTiers().getId() > 0 ? y.getCommercial().getTiers().getClients() != null ? !y.getCommercial().getTiers().getClients().isEmpty() : false : false) {
                        tiers = y.getCommercial().getTiers().getClients().get(0);
                    }
                } else {
                    if (selectEntete.getCreneau().getUsers() != null ? (selectEntete.getCreneau().getUsers().getCommercial() != null ? (selectEntete.getCreneau().getUsers().getCommercial().getTiers() != null) : false) : false) {
                        if (selectEntete.getCreneau().getUsers().getCommercial().getTiers().getClients() != null ? !selectEntete.getCreneau().getUsers().getCommercial().getTiers().getClients().isEmpty() : false) {
                            tiers = selectEntete.getCreneau().getUsers().getCommercial().getTiers().getClients().get(0);
                        }
                    }
                }
                param = new Options[]{new Options(docVente.getId(), 1)};
                String query = "update yvs_com_doc_ventes set tiers = null where id = ?";
                if (tiers != null ? (tiers.getId() != null ? tiers.getId() > 0 : false) : false) {
                    param = new Options[]{new Options(tiers.getId(), 1), new Options(docVente.getId(), 2)};
                    query = "update yvs_com_doc_ventes set tiers = ? where id = ?";
                }
                dao.requeteLibre(query, param);

                selectDoc.setTiers(tiers != null ? tiers : new YvsComClient());
                docVente.setTiers(tiers != null ? new Client(tiers.getId()) : new Client());
            }
            idx = documents.indexOf(selectDoc);
            if (idx > -1) {
                documents.set(idx, selectDoc);
            }
            update("data_commercial_vente");
        }
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllFacture(true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        initForm = true;
        loadAllFacture(true);
    }

    public void chooseCloturer(ValueChangeEvent ev) {
        cloturer_ = ((Boolean) ev.getNewValue());
        ParametreRequete p = new ParametreRequete("y.cloturer", "cloturer", cloturer_);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
    }

    public void chooseStatut(ValueChangeEvent ev) {
        String statut = ((String) ev.getNewValue());
        if (statut != null ? statut.trim().equals("Z") : false) {
            openDialog("dlgMoreStatuts");
        } else {
            if (statut != null) {
                String[] mots = statut.split("-");
                if (mots[0].equals("E")) {
                    egaliteStatut = "=";
                } else {
                    egaliteStatut = "!=";
                }
                this.statut = mots[1];
            } else {
                this.statut = (String) ev.getNewValue();
            }
            addParamStatut();
        }
    }

    public void chooseStatuts() {
        ParametreRequete p = new ParametreRequete(pre + "statut", "statut", null);
        if (statuts != null ? !statuts.isEmpty() : false) {
            boolean add = true;
            for (String s : statuts) {
                if (s != null ? s.trim().length() < 1 : true) {
                    add = false;
                    break;
                }
            }
            if (add) {
                p = new ParametreRequete(pre + "statut", "statut", statuts, "IN", "AND");
            }
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        loadAllFacture(true);
    }

    public void addParamStatut() {
        addParamStatut(true);
    }

    public void addParamStatut(boolean load) {
        ParametreRequete p;
        if (statut != null ? statut.trim().length() > 0 : false) {
            p = new ParametreRequete(pre + "statut", "statut", statut, egaliteStatut, "AND");
        } else {
            p = new ParametreRequete(pre + "statut", "statut", Constantes.ETAT_ANNULE, "!=", "AND");
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        if (load) {
            initForm = true;
            if (searchAutomatique) {
                loadAllFacture(true);
            }
        }
    }

    public void addParamToValide() {
        ParametreRequete p = new ParametreRequete("(" + pre + "etapeValide+1)", "etape", null, "IN", "AND");
        if (toValideLoad) {
            List<Integer> lnum = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepe", new String[]{"document", "niveau"}, new Object[]{Constantes.DOCUMENT_FACTURE_VENTE, currentUser.getUsers().getNiveauAcces()});
            if ((lnum != null) ? !lnum.isEmpty() : false) {
                p = new ParametreRequete("(" + pre + "etapeValide+1)", "etape", lnum, "IN", "AND");
            }
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void chooseStatutRegle(ValueChangeEvent ev) {
        statutRegle_ = ((String) ev.getNewValue());
        addParamStatutRegle();
    }

    private void addParamStatutDoc() {
        ParametreRequete p;
        if (statutRegle_ != null ? statutRegle_.trim().length() > 0 : false) {
            p = new ParametreRequete(pre + "statutRegle", "statutRegle", statutRegle_, egaliteStatutR, "AND");
        } else {
            p = new ParametreRequete(pre + "statutRegle", "statutRegle", null);
        }
        p_contenu.addParam(p);
        paginator.addParam(p);
    }

    public void addParamStatutRegle() {
        addParamStatutDoc();
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void chooseStatutLivre(ValueChangeEvent ev) {
        statutLivre_ = ((String) ev.getNewValue());
        addParamStatutLivre();
    }

    public void addParamStatutLivre() {
        ParametreRequete p;
        if (statutLivre_ != null ? statutLivre_.trim().length() > 0 : false) {
            p = new ParametreRequete(pre + "statutLivre", "statutLivre", statutLivre_, egaliteStatutL, "AND");
        } else {
            p = new ParametreRequete(pre + "statutLivre", "statutLivre", null);
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void addParamAutoLivre() {
        ParametreRequete p = new ParametreRequete(pre + "livraisonAuto", "livraisonAuto", null);
        if (autoLivreSearch != null) {
            p = new ParametreRequete(pre + "livraisonAuto", "livraisonAuto", autoLivreSearch, "=", "AND");
        }
        p_contenu.addParam(p);
        paginator.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void chooseDateSearch() {
        ParametreRequete p;
        if (date_) {
            p = new ParametreRequete(pre + "heureDoc", "heureDoc", dateDebut_);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin_);
        } else {
            p = new ParametreRequete(pre + "heureDoc", "heureDoc", null);
        }
        p_contenu.addParam(p);
        paginator.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    //méthode de recherche par header
    public void chooseEntete(YvsComEnteteDocVente selectEntete) {
        if (selectEntete != null ? selectEntete.getId() > 0 : false) {
            ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (m != null && (selectEntete.getCreneau() != null ? selectEntete.getCreneau().getCreneauPoint() != null ? selectEntete.getCreneau().getCreneauPoint().getPoint() != null : false : false)) {
                YvsBasePointVente y = selectEntete.getCreneau().getCreneauPoint().getPoint();
                if (!m.getPointsvente().contains(y)) {
                    m.getPointsvente().add(0, y);
                }
                loadDepotByPoint(y);
            }
            docVente.getEnteteDoc().setNew_(true);
            update("save_entete_facture_vente");
            update("data_facture_vente");
            update("blog_form_facture_vente");
        }
    }

    @Override
    public void _chooseSociete() {
        super._chooseSociete();
    }

    @Override
    public void _chooseAgence() {
        super._loadPoint();
        addParamAgence();
    }

    public void addParamAgence() {
        ParametreRequete p;
        if (agence_ > 0) {
            p = new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.point.agence", "agence", new YvsAgences(agence_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.point.agence", "agence", null);
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    @Override
    public void _chooseDepot() {
        super._chooseDepot();
        ParametreRequete p;
        if (depot_ > 0) {
            p = new ParametreRequete(pre + "enteteDoc.creneau.creneauDepot.depot", "depot", new YvsBaseDepots(depot_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete(pre + "enteteDoc.creneau.creneauDepot.depot", "depot", null);
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    @Override
    public void _choosePoint() {
        super._choosePoint();
        ParametreRequete p;
        if (point_ > 0) {
            p = new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.point", "point", new YvsBasePointVente(point_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.point", "point", null);
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void _chooseTranche() {
        ParametreRequete p;
        if (tranche_ > 0) {
            p = new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.tranche", "tranche", new YvsGrhTrancheHoraire(tranche_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete(pre + "enteteDoc.creneau.creneauPoint.tranche", "tranche", null);
        }
        p_contenu.addParam(p);
        paginator.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    // Recherche des factures en selectionnant le vendeur
    public void _chooseVendeur() {
        addParamVendeur(false);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
        update("_select_entete_facture_vente");
    }

    public void changeOperateurVendeur(ValueChangeEvent ev) {
        if (ev != null) {
            operateurVend = (String) ev.getNewValue();
            addParamVendeur(true);
            initForm = true;
            if (searchAutomatique) {
                loadAllFacture(true);
            }
        }
    }

    // Recherche des factures en ecrivant le nom du vendeur
    public void _searchVendeur() {
        addParamVendeur(true);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
//        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
//        if (m != null) {
//            m.addParamVendeur(codeVendeur_);
//        }
        update("_select_entete_facture_vente");
    }

    public void addParamVendeur(boolean code) {
        String predicat = (operateurVend.trim().equals("LIKE")) ? "OR" : "AND";
        ParametreRequete p;
        if (!code) {
            if (users_ > 0) {
                p = new ParametreRequete(pre + "enteteDoc.creneau.users", "vendeur", new YvsUsers(users_));
                p.setOperation("=");
                p.setPredicat("AND");
            } else {
                p = new ParametreRequete(pre + "enteteDoc.creneau.users", "vendeur", null);
            }
        } else {
            if (codeVendeur_ != null ? codeVendeur_.trim().length() > 0 : false) {
                p = new ParametreRequete(null, "vendeur", codeVendeur_ + "%", operateurVend, "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "enteteDoc.creneau.users.nomUsers)", "vendeur", codeVendeur_.toUpperCase() + "%", operateurVend, predicat));
                p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "enteteDoc.creneau.users.codeUsers)", "vendeur", codeVendeur_.toUpperCase() + "%", operateurVend, predicat));
            } else {
                p = new ParametreRequete(pre + "enteteDoc.creneau.users.codeUsers", "vendeur", null);
            }
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete(pre + "enteteDoc.dateEntete", "dateEntete", dateDebut_);
        p.setOperation("=");
        p.setPredicat("AND");
        p_contenu.addParam(p);
        paginator.addParam(p);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void changeOperateurClient(ValueChangeEvent ev) {
        if (ev != null) {
            operateuClt = (String) ev.getNewValue();
            addParamVendeur(true);
        }
    }

    public void searchByClient() {
        String predicat = (operateuClt.trim().equals("LIKE")) ? "OR" : "AND";
        ParametreRequete p;
        if (codeClient_ != null ? codeClient_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "client", codeClient_.toUpperCase() + "%", operateuClt, "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "nomClient)", "client", codeClient_.toUpperCase() + "%", operateuClt, predicat));
            p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "client.codeClient)", "client", codeClient_.toUpperCase() + "%", operateuClt, predicat));
            p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "client.nom)", "client", codeClient_.toUpperCase() + "%", operateuClt, predicat));
        } else {
            p = new ParametreRequete(pre + "client.codeClient", "client", null);
        }
        p_contenu.addParam(p);
        paginator.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void searchByRepr() {
        ParametreRequete p;
        if (otherSearch_ != null ? otherSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "representant", otherSearch_.toUpperCase() + "%", " LIKE ", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "client.representant.codeUsers)", "representant", otherSearch_.toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "client.representant.nomUsers)", "representant", otherSearch_.toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p = new ParametreRequete(pre + "client.representant.codeUsers", "representant", null);
        }
        p_contenu.addParam(p);
        paginator.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void _chooseStatut(ValueChangeEvent ev) {
        this.statut = ((String) ev.getNewValue());
        ParametreRequete p;
        if (statut != null ? statut.trim().length() > 0 : false) {
            p = new ParametreRequete(pre + "statut", "statut", statut, "=", "AND");
        } else {
            p = new ParametreRequete(pre + "statut", "statut", Constantes.ETAT_ANNULE, "!=", "AND");
        }
        p_contenu.addParam(p);
        paginator.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
        update("_select_entete_facture_vente");
    }

    public void chooseCommande() {
        ParametreRequete p;
        if (numBon != null ? numBon.trim().length() > 0 : false) {
            p = new ParametreRequete(pre + "documentLie.numDoc", "numDoc", "%" + numBon + "%", " LIKE ", "AND");
        } else {
            p = new ParametreRequete(pre + "documentLie.numDoc", "numDoc", null, " LIKE ", "AND");
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void _chooseDateSearch(ValueChangeEvent ev) {
        paramDate = (Boolean) ev.getNewValue();
        addParaDate(paramDate);
    }

    public void addParamDate1(SelectEvent ev) {
        addParaDate(paramDate);
    }

    public void addParamDate2() {
        addParaDate(paramDate);
    }

    private void addParaDate(boolean b) {
        ParametreRequete p = new ParametreRequete(pre + "enteteDoc.dateEntete", "dateEntete", null, " BETWEEN ", "AND");
        if (b) {
            if (dateDebut != null && dateFin != null) {
                if (dateDebut.before(dateFin) || dateDebut.equals(dateFin)) {
                    p.setObjet(dateDebut);
                    p.setOtherObjet(dateFin);
                }
            }
        }
        p_contenu.addParam(p);
        paginator.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }

    }

    public void addParamNature() {
        ParametreRequete p = new ParametreRequete(pre + "nature", "nature", null, " LIKE ", "AND");
        if (natureSearch != null ? natureSearch.trim().length() > 0 : false) {
            p = new ParametreRequete(pre + "nature", "nature", natureSearch, "=", "AND");
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    private void loadCaissiers(YvsBaseCaisse y) {
        caissiers.clear();
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null && !venteDirecte) {
            caissiers = w.loadCaissiers(y);
        }
        if (y != null ? y.getId() > 0 : false) {
            if (reglement.getCaissier() != null ? reglement.getCaissier().getId() < 1 : true) {
                reglement.setCaissier(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
            }
            if ((reglement.getCaissier() != null ? reglement.getCaissier().getId() < 1 : true) && (!venteDirecte ? caissiers.contains(currentUser.getUsers()) : true)) {
                reglement.setCaissier(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
            }
        }
        update("chmp_caissier_reglement_fv");
        update("tabview_facture_vente:chmp_caissier_reglement_fv");
    }

    public void chooseCaisses(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            // trouve les caisses parent d'une caisse données
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            long id = (long) ev.getNewValue();
            caissiers.clear();
            reglement.setCaissier(new Users());
            if (service != null) {
                if (id > 0) {
                    int idx = service.getCaisses().indexOf(new YvsBaseCaisse(id));
                    if (idx > -1) {
                        YvsBaseCaisse y = service.getCaisses().get(idx);
                        reglement.setCaisse(UtilCompta.buildSimpleBeanCaisse(y));
                        loadCaissiers(y);
                    }
                } else if (id == -1) {
                    List<YvsBaseCaisse> list = service.loadCaisses(currentUser.getUsers());
                    if (list != null ? !list.isEmpty() : false) {
                        for (YvsBaseCaisse c : list) {
                            if (!service.getCaisses().contains(c)) {
                                service.getCaisses().add(c);
                            }
                        }
                        update("tabview_facture_vente:chmp_caisse_reglement_fv");
                    }
                }
            }
            update("chmp_caissier_reglement_fv");
            update("tabview_facture_vente:chmp_caissier_reglement_fv");
        }
    }

    public void chooseCategorie() {
        if ((docVente.getCategorieComptable() != null) ? docVente.getCategorieComptable().getId() > 0 : false) {
            ManagedCatCompt m = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
            if (m != null) {
                int idx = m.getCategoriesVente().indexOf(new YvsBaseCategorieComptable(docVente.getCategorieComptable().getId()));
                if (idx > -1) {
                    YvsBaseCategorieComptable y = m.getCategoriesVente().get(idx);
                    cloneObject(docVente.getCategorieComptable(), UtilCom.buildBeanCategorieComptable(y));
                }
            }
        } else {
            docVente.setCategorieComptable(new CategorieComptable());
        }
        chooseCategorie(docVente.getCategorieComptable());
    }

    public void chooseCategorie(CategorieComptable d) {
        docVente.setCategorieComptable(d);
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getCategorieComptable() != null ? !selectDoc.getCategorieComptable().getId().equals(docVente.getCategorieComptable().getId()) : docVente.getCategorieComptable().getId() > 0) {
                if (docVente.getContenus() != null ? !docVente.getContenus().isEmpty() : false) {
                    if (!autoriser("fv_change_categorie")) {
                        openNotAcces();
                        noChangeCategorie();
                        return;
                    }
                    openDialog("dlgConfirmChangeCategorie");
                }
            }
        }
        update("chp_fv_net_a_payer");
        update("value_ttc_facture");
        update("value_reste_a_payer_facture");
        update("select_categorie_comptable_facture_vente");
    }

    public void chooseClient(Client d) {
        if ((d != null) ? d.getId() > 0 : false) {
            if (!venteDirecte) {
                d.setNbrFactureImpayee((Long) dao.loadObjectByNameQueries("YvsComDocVentes.countFactureImpayeByClient", new String[]{"client"}, new Object[]{new YvsComClient(d.getId())}));
            }
            cloneObject(docVente.getClient(), d);
            if (d.getCategorieComptable() != null) {
                cloneObject(docVente.getCategorieComptable(), d.getCategorieComptable());
                update("select_categorie_comptable_facture_vente");
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
            update("select_model_facture_vente");
        }
    }

    public void openTochangeNumero(YvsComDocVentes doc) {
        this.selectDoc = (doc);
        this.selectDoc.setOldReference(doc.getNumDoc());
        openDialog("dlgChangeNumDoc");
        update("form_num_doc");
    }

    public void changeNumero() {
        if (isChangeNumdocAuto()) {
            ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
            if (w != null) {
                w.reBuildNumero(selectDoc, true, true);
            }
        } else {
            if (autoriser("fv_change_num_doc")) {
                Long nb = (Long) dao.loadObjectByNameQueries("YvsComDocVentes.countDocByNumDoc", new String[]{"id", "numDoc", "societe"}, new Object[]{selectDoc.getId(), selectDoc.getNumDoc(), currentAgence.getSociete()});
                if (nb != null ? nb <= 0 : true) {
                    dao.update(selectDoc);
                } else {
                    selectDoc.setNumDoc(selectDoc.getOldReference());
                    getErrorMessage("Ce numéro est déjà attribué");
                    return;
                }
            } else {
                openNotAcces();
                return;
            }
        }
        update("data_facture_vente");
    }

    public void reBuildNumero() {
        if (selections != null ? !selections.isEmpty() : false) {
            ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
            if (w != null) {
                YvsComDocVentes current;
                int index = -1;
                for (YvsComDocVentes y : selections) {
                    current = w.reBuildNumero(y, true, false);
                    index = documents.indexOf(y);
                    if (index > -1) {
                        documents.set(index, current);
                    }
                }
                succes();
            }
            selections.clear();
        }
    }

    public void definedTiersVente() {
        if (selections != null ? !selections.isEmpty() : false) {
            for (YvsComDocVentes current : selections) {
                definedTiers(current, false);
            }
            succes();
            selections.clear();
        }
    }

    public void definedTiers(YvsComDocVentes y, boolean succes) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    boolean comptabilised = w.isComptabilise(y.getId(), Constantes.SCR_VENTE);
                    if (comptabilised) {
                        if (succes) {
                            getErrorMessage("Ce document est deja comptabilisée");
                        }
                        return;
                    }
                }
                //choisir le code tiers à utiliser
                if (y.getClient().getSuiviComptable()) {
                    y.setTiers(y.getClient());
                } else {
                    //récupère le code tiers du commerciale sur la facture
                    if (y.getCommerciaux() != null ? y.getCommerciaux().isEmpty() : false) {
                        for (YvsComCommercialVente c : y.getCommerciaux()) {
                            if (c.getCommercial().getTiers() != null) {
                                if (c.getCommercial().getTiers().getClients() != null ? !c.getCommercial().getTiers().getClients().isEmpty() : false) {
                                    y.setTiers(c.getCommercial().getTiers().getClients().get(0));
                                }
                                break;
                            }
                        }
                    }
                    if (y.getTiers() != null ? y.getTiers().getId() < 1 : true) {
                        //récupère le code tiers du commerciale ayant le point de vente
                        if (y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getId() > 0) {
                            ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                            if (service != null) {
                                int idx = service.getPointsvente().indexOf(y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint());
                                if (idx >= 0) {
                                    List<YvsComCommercialPoint> l = service.getPointsvente().get(idx).getCommerciaux();
                                    if (!l.isEmpty()) {
                                        for (YvsComCommercialPoint c : l) {
                                            if (c.getCommercial().getTiers() != null) {
                                                if (c.getCommercial().getTiers().getClients() != null ? !c.getCommercial().getTiers().getClients().isEmpty() : false) {
                                                    y.setTiers(c.getCommercial().getTiers().getClients().get(0));
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (y.getTiers() != null ? y.getTiers().getId() > 0 : true) {
                    String query = "UPDATE yvs_com_doc_ventes SET tiers=?, date_update=?, author=? WHERE id=? ";
                    dao.requeteLibre(query, new Options[]{new Options(y.getTiers().getId(), 1), new Options(new Date(), 2),
                            new Options(currentUser.getId(), 3), new Options(y.getId(), 4)});
                    if (succes) {
                        succes();
                    }
                }
//                update("data_facture_vente");
            }
        } catch (Exception ex) {
            getException("Facture... Maj tiers", ex);
        }
    }

    public void genererFicheEntree() {
        if (selections != null ? !selections.isEmpty() : false) {
            for (YvsComDocVentes current : selections) {
                genererFicheEntree(current, selections.size() == 1);
            }
            succes();
            selections.clear();
        }
    }

    public void genererFicheEntree(YvsComDocVentes y, boolean msg) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (y.getContenus() != null ? y.getContenus().isEmpty() : true) {
                    if (msg) {
                        getErrorMessage("Cette facture est vide");
                    }
                    return;
                }
                if (y.getDepotLivrer() != null ? y.getDepotLivrer().getId() < 1 : true) {
                    if (msg) {
                        getErrorMessage("Cette facture n'est pas rattachée a un depot de livraison");
                    }
                    return;
                }
                Date dateEntree = y.getEnteteDoc().getDateEntete();
                long depotSearch = y.getDepotLivrer().getId();
                String numero = genererReference(Constantes.TYPE_ES_NAME, dateEntree, depotSearch);
                if (numero == null || numero.trim().equals("")) {
                    return;
                }
                YvsComDocStocks docStock = new YvsComDocStocks();
                docStock.setActif(true);
                docStock.setAuthor(currentUser);
                docStock.setCloturer(false);
                docStock.setCreneauDestinataire(currentCreneauDepot);
                docStock.setCreneauSource(currentCreneauDepot);
                docStock.setDateDoc(dateEntree);
                docStock.setDateReception(dateEntree);
                docStock.setDateValider(new Date());
                docStock.setDestination(new YvsBaseDepots(depotSearch));
                docStock.setSource(new YvsBaseDepots(depotSearch));
                docStock.setEditeur(currentUser.getUsers());
                docStock.setEtapeTotal(0);
                docStock.setEtapeValide(0);
                docStock.setHeureDoc(new Date());
                docStock.setMouvement(Constantes.MOUV_ENTREE);
                docStock.setNature(Constantes.OP_AJUSTEMENT_STOCK);
                docStock.setNumDoc(numero);
                docStock.setNumPiece(numero);
                docStock.setSociete(currentAgence.getSociete());
                docStock.setStatut(Constantes.ETAT_VALIDE);
                docStock.setTypeDoc(Constantes.TYPE_ES);
                docStock.setValiderBy(currentUser.getUsers());

                docStock = (YvsComDocStocks) dao.save1(docStock);
                if (docStock != null ? docStock.getId() > 0 : false) {
                    YvsComContenuDocStock contenu;
                    for (YvsComContenuDocVente c : y.getContenus()) {
                        contenu = new YvsComContenuDocStock(null);
                        contenu.setActif(true);
                        contenu.setArticle(c.getArticle());
                        contenu.setAuthor(currentUser);
                        contenu.setCalculPr(true);
                        contenu.setCommentaire("");
                        contenu.setConditionnement(c.getConditionnement());
                        contenu.setConditionnementEntree(c.getConditionnement());
                        contenu.setDateContenu(new Date());
                        contenu.setDateReception(y.getEnteteDoc().getDateEntete());
                        contenu.setPrix(c.getPrix());
                        contenu.setPrixEntree(c.getPrix());
                        contenu.setPrixTotal(c.getQuantite() * c.getPrix());
                        contenu.setQteAttente(c.getQuantite());
                        contenu.setQteRestant(c.getQuantite());
                        contenu.setQuantite(c.getQuantite());
                        contenu.setQuantiteEntree(c.getQuantite());
                        contenu.setStatut(Constantes.ETAT_VALIDE);
                        contenu.setSupp(false);
                        contenu.setDocStock(docStock);
                        dao.save(contenu);
                    }
                    if (msg) {
                        succes();
                    }
                }
            }
        } catch (Exception ex) {
            if (msg) {
                getErrorMessage("Action Impossible");
            }
            getException("ManagedFactureVente (genererFicheEntree)", ex);
        }
    }

    public void chooseVille() {
        docVente.setAdresse(new Dictionnaire());
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseVille(new Dictionnaire(1), docVente.getVille().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(docVente.getVille(), d);
                ManagedPointLivraison w = (ManagedPointLivraison) giveManagedBean(ManagedPointLivraison.class);
                if (w != null) {
                    w.loadByVille(new YvsDictionnaire(d.getId()));
                    update("select_adresse_livraison");
                }
            }
        }
    }

    public void chooseAdresse() {
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseSecteur(docVente.getVille(), docVente.getAdresse().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(docVente.getAdresse(), d);
                ManagedPointLivraison w = (ManagedPointLivraison) giveManagedBean(ManagedPointLivraison.class);
                if (w != null) {
                    w.loadByVille(new YvsDictionnaire(d.getParent().getId()));
                    update("select_adresse_livraison");
                }
            }
        }
    }

    public void chooseDate() {
        if (!autoriser("fv_update_header")) {
            docVente.getEnteteDoc().setDateEntete(new Date());
            openNotAcces();
            return;
        }
        selectEntete = new YvsComEnteteDocVente();
//        loadCurrentEntete();
    }

    public void chooseTranche() {
        selectEntete = new YvsComEnteteDocVente();
        if (docVente.getEnteteDoc().getTranche() != null ? docVente.getEnteteDoc().getTranche().getId() > 0 : false) {
            for (YvsComCreneauPoint cp : docVente.getEnteteDoc().getPoint().getListTranche()) {
                if (cp.getTranche().getId().equals(docVente.getEnteteDoc().getTranche().getId())) {
                    TrancheHoraire t = UtilCom.buildBeanTrancheHoraire(cp.getTranche());
                    cloneObject(docVente.getEnteteDoc().getTranche(), t);
                    break;
                }
            }
        }
    }

    public void chooseTrancheDepot() {
        if (docVente.getTranche() != null ? docVente.getTranche().getId() > 0 : false) {
            int idx = tranches.indexOf(new YvsGrhTrancheHoraire(docVente.getTranche().getId()));
            if (idx > -1) {
                YvsGrhTrancheHoraire t = tranches.get(idx);
                docVente.setTranche(UtilGrh.buildTrancheHoraire(t));
                update("select_tranche_livraison_fv");
            }
        }
    }

    public void chooseVendeur(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long idVendeur = (long) ev.getNewValue();
            chooseVendeurBill(new YvsUsers(idVendeur));
        }
    }

    private void chooseVendeurBill(YvsUsers vendeur) {
        if (vendeur != null ? vendeur.getId() > 0 : false) {
            docVente.getEnteteDoc().getCrenauHoraire().getPersonnel().setId(vendeur.getId());
            ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (service != null) {
                int idx = service.getPointsvente().indexOf(new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId()));
                if (idx >= 0) {
                    YvsBasePointVente y = service.getPointsvente().get(idx);
                    loadDepotByPoint(y);
                }
            }
            loadCaisseFromVendeur(vendeur);
        }
    }

    private void loadCaisseFromVendeur(YvsUsers vendeur) {
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service != null) {
            YvsBaseCaisse y = service.findByResponsable(vendeur);
            reglement.setCaisse(UtilCompta.buildSimpleBeanCaisse(y));
            List<YvsBaseCaisse> list = service.loadCaisses(vendeur);
            if (list != null ? !list.isEmpty() : false) {
                loadCaissiers(y);
                reglement.setCaissier(new Users(vendeur.getId()));
                service.setCaisses(list);
            }
            if (page.equals("V3")) {
                update("form_mensualite_facture_vente");
            } else {
                update("tabview_facture_vente:form_mensualite_facture_vente");
            }
        }
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
                    docVente.getEnteteDoc().setUsers(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
                } else {
                    if (!vendeurs.isEmpty()) {
                        docVente.getEnteteDoc().getCrenauHoraire().setPersonnel(UtilUsers.buildSimpleBeanUsers(vendeurs.get(0)));
                    }
                }
                docVente.setAdresse(y.getSecteur() != null ? UtilGrh.buildSimpleBeanDictionnaire(y.getSecteur()) : new Dictionnaire());
                docVente.setVille(y.getSecteur() != null ? y.getSecteur().getParent() != null ? UtilGrh.buildSimpleBeanDictionnaire(y.getSecteur().getParent()) : new Dictionnaire() : new Dictionnaire());
                if (docVente.getId() < 1) {
                    Client client = null;
                    if (y.getClient() != null ? y.getClient().getId() > 0 : false) {
                        client = UtilCom.buildBeanClient(y.getClient());
                    } else {
                        client = UtilCom.buildBeanClient(defaultClient);
                    }
                    chooseClient(client);
                    update("select_client_facture_vente");
                }
//charge les dépôts
                loadDepotByPoint(y);
                update("txt_zone_client");
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

    public boolean chooseDepot(Depots depot, Date date) {
        docVente.setTranche(new TrancheHoraire());
        if (depot != null ? depot.getId() > 0 : false) {
            int idx = depotsLivraison.indexOf(new YvsBaseDepots(depot.getId()));
            if (idx > -1) {
                YvsBaseDepots y = depotsLivraison.get(idx);
                cloneObject(depot, UtilCom.buildBeanDepot(y));
                loadAllTranche(y, date);
                if (!verifyOperation(depot, Constantes.SORTIE, Constantes.VENTE, false)) {
                    return false;
                }
                if (currentPlanning != null ? !currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false : false) {
                    docVente.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
                }
                update("select_tranche_livraison_fv");
                return true;
            }
        }
        return false;
    }

    public void chooseDepot() {
        docVente.setTranche(new TrancheHoraire());
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            int idx = depotsLivraison.indexOf(new YvsBaseDepots(docVente.getDepot().getId()));
            if (idx > -1) {
                chooseDepot(depotsLivraison.get(idx));
            }
        }
    }

    public void chooseDepot(YvsBaseDepots y) {
        docVente.setTranche(new TrancheHoraire());
        if (y != null) {
            docVente.setDepot(UtilCom.buildSimpleBeanDepot(y));
            loadAllTranche(y, (loadOnlyDepotPoint ? docVente.getDateLivraisonPrevu() : null));
            if ((tranches != null ? !tranches.isEmpty() : false)) {
                if (currentPlanning != null ? !currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false : false) {
                    if (!tranches.contains(currentPlanning.get(0).getCreneauDepot().getTranche())) {
                        docVente.setTranche(UtilCom.buildBeanTrancheHoraire(tranches.get(0)));
                    } else {
                        docVente.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
                    }
                }
            }
            if (docVente.getTranche() != null ? docVente.getTranche().getId() < 1 : true) {
                if (currentPlanning != null ? !currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false : false) {
                    docVente.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
                }
            }
            update("select_tranche_livraison_fv");
        }
    }

    public void choosePointLivraison() {
        ManagedPointLivraison w = (ManagedPointLivraison) giveManagedBean(ManagedPointLivraison.class);
        if (w != null) {
            int index = w.getPoints().indexOf(new YvsBasePointLivraison(docVente.getInformation().getAdresseLivraison().getId()));
            if (index > -1) {
                YvsBasePointLivraison y = w.getPoints().get(index);
                docVente.getInformation().setAdresseLivraison(UtilCom.buildBeanPointLivraison(y));
                docVente.setAdresse(UtilGrh.buildSimpleBeanDictionnaire(y.getVille()));
            }
        }
    }

    public void chooseCategorieClt() {
        update("select_categorie_client");
    }

    public void chooseRemise() {
        if ((remise.getRemise() != null)) {
            if (remise.getRemise().getId() > 0) {
                ManagedRemise m = (ManagedRemise) giveManagedBean(ManagedRemise.class);
                if (m != null) {
                    YvsComRemise d_ = m.getRemises().get(m.getRemises().indexOf(new YvsComRemise(remise.getRemise().getId())));
                    Remise d = UtilCom.buildBeanRemise(d_);
                    cloneObject(remise.getRemise(), d);
                    remise.setMontant(getMontantRemise(d_, docVente));
                }
            } else if (remise.getRemise().getId() < 0) {
                openDialog("dlgAddPlanRemise");
            }
        }
    }

    public void chooseRemiseContenu() {
        montant_remise = 0;
        if (remiseContenu > 0) {
            ManagedRemise m = (ManagedRemise) giveManagedBean(ManagedRemise.class);
            if (m != null) {
                int idx = m.getRemises().indexOf(new YvsComRemise(remiseContenu));
                if (idx > -1) {
                    YvsComRemise y = m.getRemises().get(idx);
                    if (!acces(y.getCodeAcces())) {
                        openNotAccesByCode();
                        remiseContenu = 0;
                        return;
                    }
                    Object[] result = getValeursRemise(y, selectContenu);
                    if (result != null ? result.length > 0 : false) {
                        taux_remise = (Double) result[0];
                        remise_is_taux = (Boolean) result[1];
                        montant_remise = (Double) result[2];
                    }
                }
            }
        }
    }

    public void initInfosContenu(YvsComContenuDocVente y) {
        selectContenu = y;
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            Double quantite = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findSumByUniteDocLieTypeStatut", new String[]{"facture", "conditionnement", "statut", "typeDoc"}, new Object[]{y.getDocVente(), y.getConditionnement(), Constantes.ETAT_VALIDE, Constantes.TYPE_BLV});
            char statut = Constantes.STATUT_DOC_ATTENTE;
            if (quantite != null ? quantite > 0 : false) {
                if (quantite >= y.getQuantite()) {
                    statut = Constantes.STATUT_DOC_LIVRER;
                } else {
                    statut = Constantes.STATUT_DOC_ENCOUR;
                }
            }
            y.setQteLivree(quantite != null ? quantite : 0);
            if (!y.getStatutLivree().equals(statut)) {
                y.setStatutLivree(statut);
                selectContenu.setStatutLivree(statut);
                dao.update(y);
                if (docVente.getContenus().contains(y)) {
                    update("tabview_facture_vente:data_contenu_facture_vente");
                }
                if (all_contenus.contains(y)) {
                    update("data_contenu_fv");
                }
            }
            update("blog_form_infos_contenu");
        }
    }

    public void initFactureVente() {
        if ((selectDoc != null) ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
            update("blog_form_infos_contenu");
        }
    }

    public void setTotalReste(double montant) {
        docVente.setMontantCS(docVente.getMontantCS() + montant);
        docVente.setMontantResteApayer(docVente.getMontantResteApayer() - montant);
        update("blog_form_montant_doc");
    }

    public void setClientDefaut() {
        if ((docVente.getClient() != null ? docVente.getClient().getId() < 1 : true) && defaultClient == null) {
            defaultClient = currentClientDefault();
            if (defaultClient != null ? defaultClient.getId() > 0 : false) {
                ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
                if (w != null && !venteDirecte) {
                    w.creance(defaultClient);
                }
                chooseClient(UtilCom.buildSimpleBeanClient(defaultClient));
            } else {
                openDialog("dlgConfirmAddClient");
                docVente.setClient(new Client());
            }
            update("select_client_facture_vente");
        } else if (defaultClient != null) {
            if (!defaultClient.getId().equals(docVente.getClient().getId())) {
                chooseClient(UtilCom.buildSimpleBeanClient(defaultClient));
            }
        }
    }

    public void setCommercialDefaut() {
        if (commercial.getCommercial() != null ? commercial.getCommercial().getId() < 1 : true) {
            YvsComComerciale c = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findDefaut", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (c != null ? c.getId() > 0 : false) {
                commercial.setCommercial(UtilCom.buildBeanCommerciales(c));
            }
            update("select_commercial_vente");
        }
    }

    public void onArticleSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseArticles t_ = (YvsBaseArticles) ev.getObject();
            Articles t = UtilCom.buildBeanArticle(t_);
            if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
                t.setStock(dao.stocks(t_.getId(), 0, 0, 0, 0, docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
                t.setPuv(dao.getPuv(t_.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), docVente.getEnteteDoc().getDepot().getId(), docVente.getEnteteDoc().getPoint().getId(), docVente.getEnteteDoc().getDateEntete(), contenu.getConditionnement().getId()));
            }
            cloneObject(contenu.getArticle(), t);
            contenu.setPrix(t.getPuv());
            contenu.setQuantite(0.0);
            selectArt = true;
        } else {
            resetFicheContenu();
        }
    }

    public void onClientSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient t_ = (YvsComClient) ev.getObject();
            Client t = UtilCom.buildBeanClient(t_);
            cloneObject(docVente.getClient(), t);
            cloneObject(docVente.getCategorieComptable(), t.getCategorieComptable());
        }
    }

    public void onTauxCommercialBlur() {
        double ca = docVente.getMontantNetAPayer();
        if (ca != 0) {
            double montant = ca * (commercial.getTaux() / 100);
            commercial.setMontant(montant);
        }
    }

    public void onMontantCommercialBlur() {
        double ca = docVente.getMontantNetAPayer();
        if (ca != 0) {
            double taux = (commercial.getMontant() * 100) / ca;
            commercial.setTaux(taux);
        }
    }

    public void onPrixBlur() {
        if (!isBon) {
            if ((docVente.getClient() != null) ? docVente.getClient().getId() > 0 : false) {
                findPrixArticle(contenu, false);
            } else {
                getWarningMessage("Selectionner le client!");
            }
        }
    }

    public void onQuantiteBlur() {
        findPrixArticle(contenu, true);
        //cherche le bonus
        champ = new String[]{"plan", "article", "unite", "date", "quantite", "nature"};
        val = new Object[]{new YvsComPlanRistourne(docVente.getClient().getRistourne().getId()), new YvsBaseArticles(contenu.getArticle().getId()), new YvsBaseConditionnement(contenu.getConditionnement().getId()), docVente.getEnteteDoc().getDateEntete(), contenu.getQuantite(), 'B'};
        List<YvsComGrilleRistourne> gr = dao.loadNameQueries("YvsComGrilleRistourne.findByBonus", champ, val);
        if (gr.size() > 1) {
            getWarningMessage("Plus d'un plan de bonus a été trouvé pour cet article veuiller vérifier la configuration");
        } else if (gr.size() == 1) {
            contenu.setArticleBonus(UtilCom.buildBeanArticle(gr.get(0).getArticle()));
            contenu.setConditionnementBonus(UtilProd.buildBeanConditionnement(gr.get(0).getConditionnement()));
            double quantiteBonus = 0;
            if (gr.get(0).getNatureMontant().equals(Constantes.NATURE_MTANT)) {
                quantiteBonus = gr.get(0).getMontantRistourne();
            } else {
                quantiteBonus = (int) ((contenu.getQuantite() * gr.get(0).getMontantRistourne()) / 100);
            }
            contenu.setQuantiteBonus(quantiteBonus);
        } else if (contenu.getId() < 1) {
            contenu.setArticleBonus(new Articles());
            contenu.setConditionnementBonus(new Conditionnement());
            contenu.setQuantiteBonus(0);
        }
    }

    public void onRemiseBlur(boolean taux) {
        double total = selectContenu.getQuantite() * (selectContenu.getPrix() - selectContenu.getRabais());
        if (taux) {
            if (taux_remise > 100) {
                getErrorMessage("Le taux ne peut pas excéder 100%");
                onRemiseBlur(false);
                return;
            }
            montant_remise = (total * taux_remise) / 100;
        } else {
            taux_remise = (montant_remise * 100) / total;
        }
    }

    public void onRabaisBlur(boolean total) {
        if (total) {
            montant_rabais = montant_rabais_total / selectContenu.getQuantite();
        } else {
            montant_rabais_total = montant_rabais * selectContenu.getQuantite();
        }
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

    public void openDlgToConfirmSuspend(YvsComDocVentes y) {
        selectDoc = y;
        populateView(UtilCom.buildBeanDocVente(y));

        if (y.getTypeDoc().equals(Constantes.TYPE_BCV)) {
            ManagedBonVente w = (ManagedBonVente) giveManagedBean(ManagedBonVente.class
            );
            if (w
                    != null) {
                w.refuserOrder(selectDoc, docVente, Constantes.TYPE_BCV);
                openDialog("dlgAnnuleCommande");
            }
        } else {
            openDialog("dlgSuspendFact");
        }
    }

    public void lettrer(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (w
                    != null) {
                boolean comptabilise = w.isComptabilise(y.getId(), Constantes.SCR_VENTE);
                if (!comptabilise) {
                    getInfoMessage("Cette facture n'est pas comptabilisée");
                    return;
                }
                w.setContenusLettrer(w.lettrerVente(y));
                if (w.getContenusLettrer() != null ? !w.getContenusLettrer().isEmpty() : false) {
                    openDialog("dlgLettrage");
                    update("data_contenu_journal");
                }
            }
        }
    }

    public void lettrerCaisse(YvsComptaCaissePieceVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (w
                    != null) {
                boolean comptabilise = w.isComptabilise(y.getId(), Constantes.SCR_CAISSE_VENTE);
                if (!comptabilise) {
                    getInfoMessage("Cette pièce n'est pas comptabilisée");
                    return;
                }
                w.setContenusLettrer(w.lettrerCaisseVente(y));
                if (w.getContenusLettrer() != null ? !w.getContenusLettrer().isEmpty() : false) {
                    openDialog("dlgLettrage");
                    update("data_contenu_journal");
                }
            }
        }
    }

    public boolean annulerOrder(boolean continueSave) {
        return annulerOrder(selectDoc, docVente, continueSave);
    }

    public boolean controlToAnnulerOrder(YvsComDocVentes selectDoc, DocVente docVente, boolean continueSave) {
        try {
            if (selectDoc != null ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
                if (selectDoc.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
                    getErrorMessage("Ce document est deja livré");
                    return false;
                }
                List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                if (l != null ? !l.isEmpty() : false) {
                    for (YvsComDocVentes d : l) {
                        if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                            getErrorMessage("Impossible d'annuler cet ordre car il possède des documents de livraison déja valide");
                            return false;
                        }
                    }
                    if (!continueSave) {
                        openDialog("dlgConfirmAnnuler");
                    } else {
                        annulerOrderForce(selectDoc, docVente);
                    }
                }
                if (docVente != null ? docVente.getMontantAvance() > 0 : false) {
                    getErrorMessage("Cette facture est déjà en cours de règlement !");
                    return false;
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("ManagedFactureVenteV2 (controlToAnnulerOrder)", ex);
        }
        return false;
    }

    public boolean annulerOrder(YvsComDocVentes selectDoc, DocVente docVente, boolean continueSave) {
        if (selectDoc != null ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
            if (!controlToAnnulerOrder(selectDoc, docVente, continueSave)) {
                return false;
            }
            if (changeStatut(Constantes.ETAT_EDITABLE, docVente, selectDoc)) {
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
                return true;
            }
        }
        return false;
    }

    public void annulerOrder_() {
        annulerOrderForce(selectDoc, docVente);
    }

    public void annulerOrderForce(YvsComDocVentes selectDoc, DocVente docVente) {
        try {
            if (selectDoc != null ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
                List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                for (YvsComDocVentes d : l) {
                    dao.delete(d);
                }
                annulerOrder(selectDoc, docVente, false);
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("Lymytz Error >>>", ex);
        }
    }

    public void _annulerOrder_() {
        try {
            if (selectDoc != null ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
                for (YvsComptaCaissePieceVente p : selectDoc.getReglements()) {
                    dao.delete(p);
                }
                docVente.setMontantAvance(0);
                annulerOrder(false);
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("Lymytz Error >>>", ex);
        }
    }

    public boolean refuserOrder(boolean force, boolean passe) {
        return refuserOrder(selectDoc, docVente, force, passe);
    }

    public boolean refuserOrder(YvsComDocVentes selectDoc, DocVente docVente, boolean force, boolean passe) {
        try {
            if (selectDoc != null ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
                if (!passe) {
                    List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                    for (YvsComDocVentes d : l) {
                        dao.delete(d);
                        if (docVente != null) {
                            docVente.getDocuments().remove(d);
                        }
                    }
                }
                cancelEtapeFacture(selectDoc, docVente, force, true, passe);
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("Lymytz Error >>>", ex);
        }
        return false;
    }

    public void cancelOrder(boolean force, boolean passe) {
        try {
            if (selectDoc != null ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
                if (!passe) {
                    List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                    for (YvsComDocVentes d : l) {
                        dao.delete(d);
                        docVente.getDocuments().remove(d);
                    }
                }
                cancelEtapeFacture(force, false, passe);
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible");
            getException("Lymytz Error >>>", ex);
        }
    }

    public void validerFacture(boolean force) {
        if (selectDoc.getTypeDoc().equals(Constantes.TYPE_FV)) {
            if (checkAvance && !force) {
                openDialog("dlgConfirmValide");
            } else {
                if (_saveNew(true)) {
                    validerOrder();
                }
            }
        } else {
            ManagedBonVente w = (ManagedBonVente) giveManagedBean(ManagedBonVente.class);
            if (w != null) {
                docVente.setReglements(dao.loadNameQueries("YvsComptaCaissePieceVente.findRegFactByFacture", new String[]{"facture"}, new Object[]{selectDoc}));
                w.setSelectDoc(selectDoc);
                w.setDocVente(docVente);
                w.setType(Constantes.TYPE_FV);
                w.validerOrder(true);
            }
        }
    }

    public void validerOrderAll() {
        try {
            if (!autoriser("fv_valide_doc")) {
                openNotAcces();
                return;
            }
            if (currentParamVente == null) {
                currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (selections != null ? !selections.isEmpty() : false) {
                long succes = 0;
                for (YvsComDocVentes y : selections) {
                    if (validerOrderOne(y, false)) {
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

    public boolean validerOrderOne(YvsComDocVentes entity, boolean succes) {
        if (!entity.getStatut().equals(Constantes.ETAT_VALIDE)) {
            DocVente bean = UtilCom.buildBeanDocVente(entity);
            setMontantTotalDoc(bean, entity);
            entity.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidFactureVente.findByFacture", new String[]{"facture"}, new Object[]{entity}));
            bean.setEtapesValidations(ordonneEtapes(entity.getEtapesValidations()));
            boolean response = false;
            if (bean.getEtapesValidations() != null ? bean.getEtapesValidations().isEmpty() : true) {
                response = validerOrder(bean, entity, true, false, true);
            } else {
                for (YvsWorkflowValidFactureVente r : bean.getEtapesValidations()) {
                    if (!r.getEtapeValid()) {
                        etape = r;
                        break;
                    }
                }
                if (etape != null ? etape.getId() > 0 : false) {
                    response = validEtapeFacture(entity, bean, currentUser, etape, true);
                } else {
                    response = validerOrder(bean, entity, true, false, true);
                }
            }
            if (response && succes) {
                succes();
            }
        }
        return false;
    }

    public boolean controleValidation(DocVente docVente, YvsComDocVentes selectDoc) {
        if (selectDoc == null) {
            getErrorMessage("Vous devez selectionner la facture");
            return false;
        }
        if (selectDoc.getCloturer()) {
            getErrorMessage("Ce document est vérouillé");
            return false;
        }
        if (docVente.getContenus().isEmpty()) {
            getErrorMessage("Vous ne pouvez valider un document vide !");
            return false;
        }
        if (docVente.getMontantResteApayer() < 0) {
            getErrorMessage("Vous ne pouvez pas valider cette facture...car la somme des reglements est superieure au montant de la facture");
            return false;
        }
        if (!controleEcartVente(selectDoc.getEnteteDoc().getCreneau().getUsers().getId(), selectDoc.getEnteteDoc().getDateEntete(), true)) {
            return false;
        }
        //compare la quantité livré et la quantité facturé

//        if (selectDoc.getDocumentLie() != null ? selectDoc.getDocumentLie().getId() > 0 : false) {// s'il y a des BL
//            YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getDocumentLie().getId()});
//            if (y != null ? y.getId() > 0 : false) {
////                if (y.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
////                    getErrorMessage("Cette facture est rattachée à la commande N° " + y.getNumDoc() + " qui est déja livrée");
////                    return false;
////                }
////                if (y.getStatut().equals(Constantes.ETAT_ANNULE)) {
////                    getErrorMessage("Cette facture est rattachée à la commande N° " + y.getNumDoc() + " qui est annulée");
////                    return false;
////                }
////                if (!y.getStatut().equals(Constantes.ETAT_VALIDE)) {
////                    getErrorMessage("Cette facture est rattachée à la commande N° " + y.getNumDoc() + " qui n'est pas validée");
////                    return false;
////                }
//        for (YvsComContenuDocVente c : docVente.getContenus()) {
//            //controle les quantités déjà facturé
//            Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{selectDoc, Constantes.ETAT_VALIDE, Constantes.TYPE_FV, c.getArticle(), c.getConditionnement()});
//            qteFacture = (qteFacture != null) ? qteFacture : 0;
//            //trouve la livré
//            Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByDocLivre", new String[]{"facture", "article", "typeDoc", "statut", "unite"}, new Object[]{selectDoc, c.getArticle(), Constantes.TYPE_BLV, Constantes.ETAT_VALIDE, c.getConditionnement()});
//            qteLivre = (qteLivre != null) ? qteLivre : 0;
//            if (qteFacture < (qteLivre)) {
//                getErrorMessage("Impossible de valider la facture! l'article " + c.getArticle().getRefArt() + " est livré au delà de la quantité facturé !");
//                return false;
//            }
//        }
////            }
//        }
        return true;
    }

    public boolean validerOrder() {
        return validerOrder(docVente, selectDoc, true, true);
    }

    public boolean validerOrder(YvsComDocVentes selectDoc) {
        DocVente docVente = UtilCom.buildBeanDocVente(selectDoc);
        setMontantTotalDoc(docVente, selectDoc);
        return validerOrder(docVente, selectDoc, true, true);
    }

    public boolean validerOrder(DocVente docVente, YvsComDocVentes selectDoc) {
        return validerOrder(docVente, selectDoc, true, true);
    }

    public boolean validerOrder(DocVente docVente, YvsComDocVentes selectDoc, boolean msg, boolean succes) {
        return validerOrder(docVente, selectDoc, msg, succes, false);
    }

    public boolean validerOrder(DocVente docVente, YvsComDocVentes selectDoc, boolean msg, boolean succes, boolean byList) {
        if (!autoriser("fv_valide_doc")) {
            openNotAcces();
            return false;
        }
        if (!controleValidation(docVente, selectDoc)) {
            return false;
        }
        Caisses caisse = null;
        if (docVente.isValidationReglement() && selectDoc.getMontantResteAPlanifier() > 0) {
            caisse = reglement.getCaisse();
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (w != null ? (caisse != null ? caisse.getId() < 1 : false) : false) {
                if (selectDoc != null ? selectDoc.getEnteteDoc() != null ? selectDoc.getEnteteDoc().getCreneau() != null ? selectDoc.getEnteteDoc().getCreneau().getUsers() != null : false : false : false) {
                    caisse = UtilCompta.buildSimpleBeanCaisse(w.findByResponsable(selectDoc.getEnteteDoc().getCreneau().getUsers()));
                }
            }
            if (caisse != null ? caisse.getId() < 1 : false) {
                getErrorMessage("Vous devez preciser la caisse car la mention reglement automatique est active sur la facture");
                return false;
            }
            reglement.setCaisse(caisse);
        }

        // Générère l'echéancier prévu de règlement
        generatedEcheancierReg(selectDoc, false);
        boolean result = changeStatut_(Constantes.ETAT_VALIDE, docVente, selectDoc);
        if (result) {
            selectDoc.setCloturer(false);
            selectDoc.setAnnulerBy(null);
            selectDoc.setValiderBy(currentUser.getUsers());
            selectDoc.setCloturerBy(null);
            selectDoc.setDateAnnuler(null);
            selectDoc.setDateCloturer(null);
            selectDoc.setDateValider(new Date());
            selectDoc.setDateUpdate(new Date());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                selectDoc.setAuthor(currentUser);
            }
            dao.update(selectDoc);
            //valider aussi les BL liés
            List<YvsComDocVentes> lv = dao.loadNameQueries("YvsComDocVentes.findByParentTypeDoc", new String[]{"documentLie", "typeDoc"}, new Object[]{selectDoc, Constantes.TYPE_BLV});
            if (selectDoc.getLivraisonAuto()) {
                ManagedLivraisonVente service = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                if (service != null) {
                    if (lv.isEmpty()) {
                        transmisOrder(selectDoc, selectDoc.getDateLivraisonPrevu(), Constantes.ETAT_VALIDE, true, false);
                        if (docVente != null) {
                            docVente.setStatutLivre(Constantes.ETAT_LIVRE);
                            docVente.setConsigner(false);
                            docVente.setDateConsigner(null);
                            if (page.equals("V3")) {
                                update("blog_form_contenu_facture_vente");
                                update("blog_form_cout_facture_vente");
                                update("form_mensualite_facture_vente");
                            } else {
                                update("tabview_facture_vente:blog_form_contenu_facture_vente");
                                update("tabview_facture_vente:blog_form_cout_facture_vente");
                                update("tabview_facture_vente:form_mensualite_facture_vente");
                            }
                            update("data_livraison_facture_vente");
                            update("blog_commercial_vente");
                        }
                    }
                    DocVente d;
                    for (YvsComDocVentes dLiv : lv) {
                        if (!dLiv.getStatut().equals(Constantes.ETAT_ANNULE) && !dLiv.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            d = UtilCom.buildSimpleBeanDocVente(dLiv, false);
                            d.setLivreur(UtilUsers.buildBeanUsers(selectDoc.getEnteteDoc().getCreneau().getUsers()));
                            d.setDocumentLie(docVente);
                            dLiv.setDocumentLie(selectDoc);
                            service.validerOrder(dLiv, d, false, false, false, null, false);
                        }
                    }
                }
            }
            if (docVente.isValidationReglement()) {
                ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                if (w != null) {
                    if (caisse != null ? caisse.getId() > 0 : false) {
                        List<YvsComptaCaissePieceVente> pieces = w.generetedPiecesFromModel(new YvsBaseModelReglement(docVente.getModeReglement().getId(), docVente.getModeReglement().getDesignation()), docVente, UtilCompta.buildBeanCaisse(caisse));
                        for (YvsComptaCaissePieceVente y : pieces) {
                            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                y.setVente(selectDoc);
                                if (y.getId() < 1 && ((y.getCaisse() != null) ? y.getCaisse().getId() > 0 : false) && ((y.getModel() != null) ? y.getModel().getId() > 0 : false) && y.getMontant() > 0) {
                                    y.setId(null);
                                    y = (YvsComptaCaissePieceVente) dao.save1(y);
                                    docVente.getReglements().add(y);
                                    if (!selectDoc.getReglements().contains(y)) {
                                        selectDoc.getReglements().add(y);
                                    }
                                }
                            }
                        }
                    }
                    if (!docVente.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                        //Si le reste à payer est positif   
                        if (docVente.getReglements() != null ? !docVente.getReglements().isEmpty() : false) {
                            ManagedRetenue wr = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                            for (YvsComptaCaissePieceVente y : docVente.getReglements()) {
                                if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                    y.setVente(selectDoc);
                                    if (y.getId() < 1 && ((y.getCaisse() != null) ? y.getCaisse().getId() > 0 : false) && ((y.getModel() != null) ? y.getModel().getId() > 0 : false) && y.getMontant() > 0) {
                                        y.setId(null);
                                        y = (YvsComptaCaissePieceVente) dao.save1(y);
                                        if (!selectDoc.getReglements().contains(y)) {
                                            selectDoc.getReglements().add(y);
                                        }
                                    }
                                    if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
                                        w.openConfirmPaiement(y, "F", false, false, false);
                                        w.reglerPieceTresorerie(true, false);
                                    } else if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_SALAIRE)) {
                                        YvsGrhEmployes emp = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCompteTiers", new String[]{"tiers", "societe"}, new Object[]{y.getVente().getClient().getTiers(), currentAgence.getSociete()});
                                        if (emp != null) {
                                            if (wr != null) {
                                                wr.loadAllTypeElementAddActif();
                                                if ((wr.getListTypesElts() != null ? wr.getListTypesElts().size() == 1 : false) && (wr.getPlansRetenues() != null ? wr.getPlansRetenues().size() == 1 : false)) {
                                                    wr.getElementAdd().setContrat(UtilGrh.buildBeanContrat(emp.getContrat()));
                                                    //charge les retenue déjà rattaché à cette facture
                                                    wr.getElementAdd().setListPrelevement(dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findByDocVente", new String[]{"docVente"}, new Object[]{y.getVente()}));
                                                    wr.setDebutPlanif(selectDoc.getEnteteDoc().getDateEntete());
                                                    wr.getElementAdd().setDebut(selectDoc.getEnteteDoc().getDateEntete());
                                                    wr.getElementAdd().setTypeElt(new TypeElementAdd(wr.getListTypesElts().get(0).getId()));
                                                    wr.getElementAdd().setPlan(new PlanPrelevement(wr.getPlansRetenues().get(0).getId()));
                                                    wr.placerRetenu(y.getMontant());
                                                    w.valideReglementFacture(y, false);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (currentParamVente == null) {
                currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            YvsComCreneauHoraireUsers creneau = (YvsComCreneauHoraireUsers) dao.loadOneByNameQueries("YvsComCreneauHoraireUsers.findById", new String[]{"id"}, new Object[]{selectDoc.getEnteteDoc().getCreneau().getId()});
            if (creneau != null) {
                if (creneau.getCreneauPoint().getPoint().getComptabilisationAuto()) {
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        w.comptabiliserVente(selectDoc, false, false);
                    }
                }
            }
            afterChangeStatut(statut, docVente, selectDoc);
            if (!byList) {
                if (!needConfirmation) {
                    openDialog("dlgConfirmResetFiche");
                } else if (validAndClear) {
                    resetFiche();
                }
            }
            if (this.docVente != null ? selectDoc != null ? selectDoc.getId().equals(this.docVente.getId()) : false : false) {
                update("tabview_facture_vente");
            }
            if (succes) {
                succes();
            }
            return true;
        }
        return result;
    }

    public boolean generatedEcheancierReg(YvsComDocVentes y, boolean addList) {
        if (y != null) {
            ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
            if (m != null) {
                List<YvsComMensualiteFactureVente> list = m.generatedEcheancierReg(y);
                if (list != null ? list.isEmpty() : true) {
                    return false;
                }
                for (YvsComMensualiteFactureVente e : list) {
                    e.setId(null);
                    e = (YvsComMensualiteFactureVente) dao.save1(e);
                }
                if (addList) {
                    y.setMensualites(list);
                }
            }
        }
        return true;
    }

    public void genereLivraison() {
        try {
            if (selections != null ? !selections.isEmpty() : false) {
                boolean succes = false;
                for (YvsComDocVentes y : selections) {
                    Long count = (Long) dao.loadObjectByNameQueries("YvsComDocVentes.findByParentTypeDocCount", new String[]{"documentLie", "typeDoc"}, new Object[]{y, Constantes.TYPE_BLV});
                    if (count != null ? count < 1 : true) {
                        transmisOrder(y, y.getDateLivraisonPrevu(), Constantes.ETAT_VALIDE, false, false);
                        succes = true;
                    }
                }
                if (succes) {
                    succes();
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Génération impossible!");
        }
    }

    public void transmisOrderByForce() {
        transmisOrder(true);
    }

    public void transmisOrder() {
        transmisOrder(false);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void addNote() {
        try {
            if (notes != null && !notes.isEmpty()) {
                selectDoc.setNotes(notes);
                dao.update(selectDoc);
                succes();
                notes = "";
            } else {
                System.err.println("Null");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur survenue lors de l'ajout d'une note", e);
        }
    }

    public void transmisOrder(boolean force) {
        selectDoc.setDepotLivrer(UtilProd.buildBeanDepot(docVente.getDepot()));
        selectDoc.setTrancheLivrer(UtilGrh.buildTrancheHoraire(docVente.getTranche(), currentUser));
        selectDoc.setDateLivraisonPrevu(docVente.getDateLivraisonPrevu());
        if (selectDoc.getTypeDoc().equals(Constantes.TYPE_FV)) {
            transmisOrder(selectDoc, dateLivraison, statutLivraison, true, force);
        } else {
            ManagedBonVente w = (ManagedBonVente) giveManagedBean(ManagedBonVente.class);
            if (w != null) {
                if (docVente.getDocuments() == null || docVente.getDocuments().isEmpty()) {
                    openDocumentLies();
                }
                if (docVente.getReglements() == null || docVente.getReglements().isEmpty()) {
                    openDlgReglement();
                }
                w.setDateLivraison(dateLivraison);
                w.setSelectDoc(selectDoc);
                w.setDocVente(docVente);
                w.setType(Constantes.TYPE_FV);
                w.transmisOrder();
            }
        }
        docVente.setConsigner(selectDoc.getConsigner());
        docVente.setStatutLivre(selectDoc.getStatutLivre());
        docVente.setDateConsigner(selectDoc.getDateConsigner());
        docVente.setDocuments(selectDoc.getDocuments());
        docVente.setNbreDocLie(docVente.getDocuments().size());
        update("btn-open_reglement");
        update("infos_document_facture_vente");
        update("grp_btn_etat_facture_vente");
        update("data_livraison_facture_vente");
        update("blog_commercial_vente");
        if (page.equals("V3")) {
            update("blog_form_contenu_facture_vente");
            update("blog_form_cout_facture_vente");
            update("form_mensualite_facture_vente");
        } else {
            update("tabview_facture_vente:blog_form_contenu_facture_vente");
            update("tabview_facture_vente:blog_form_cout_facture_vente");
            update("tabview_facture_vente:form_mensualite_facture_vente");
        }
    }

    private boolean controlContentForTransmis(YvsComContenuDocVente c, YvsBaseDepots depot, Date dateLivraison, String statut, boolean message) {
        champ = new String[]{"article", "depot"};
        val = new Object[]{c.getArticle(), depot};
        YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
        if (y == null || y.getId() < 1) {
            if (message) {
                getErrorMessage("Impossible d'effectuer cette action... Car le depot " + depot.getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
            }
            return false;
        }
        if (y.getRequiereLot() && (c.getLots() != null ? c.getLots().isEmpty() : true)) {
            if (message) {
                getErrorMessage("Un numéro de lot est requis pour l'article " + c.getArticle().getDesignation() + "  dans le depot " + depot.getDesignation());
            }
            return false;
        }
        c.setRequiereLot(y.getRequiereLot());
        if (statut.equals(Constantes.ETAT_VALIDE)) {
            String result = controleStock(c.getArticle().getId(), c.getConditionnement().getId(), depot.getId(), 0L, c.getQuantite(), 0, "INSERT", "S", dateLivraison, (c.getLot() != null ? c.getLot().getId() : 0));
            if (result != null) {
                if (message) {
                    getErrorMessage("Impossible de valider ce document", "la ligne d'article " + c.getArticle().getDesignation() + " engendrera une incohérence dans le stock à la date du " + result);
                }
                return false;
            }
        }
        return true;
    }

    Boolean exist_inventaire;

    public void transmisOrder(YvsComDocVentes facture, Date dateLivraison, String statut, boolean message, boolean force) {
        String query = "SELECT action FROM yvs_com_doc_ventes WHERE id = ?";
        String action = (String) dao.loadObjectBySqlQuery(query, new Options[]{new Options(facture.getId(), 1)});
        if (Util.asString(action) && action.equals("L")) {
            if (message) {
                getErrorMessage("L'operation de livraison est déja en cours d'execution!!!");
            }
            return;
        }
        query = "UPDATE yvs_com_doc_ventes SET action = 'L' WHERE id = ?";
        dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
        try {
            statut = statut != null ? !statut.trim().isEmpty() ? statut : Constantes.ETAT_VALIDE : Constantes.ETAT_VALIDE;
            if (!force) {
                if (!autoriser("fv_livrer")) {
                    if (message) {
                        openNotAcces();
                    }
                    query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                    dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                    return;
                }
                if (facture.getModelReglement() != null && (facture.getModelReglement().getPayeBeforeValide() && !facture.getStatutRegle().equals(Constantes.ETAT_REGLE))) {
                    if (message) {
                        getErrorMessage("La facture doit étre payée!");
                    }
                    query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                    dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                    return;
                }
                if (facture.getEnteteDoc() != null && (facture.getEnteteDoc().getCreneau() != null && (facture.getEnteteDoc().getCreneau().getCreneauPoint() != null && (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null)))) {
                    switch (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getLivraisonOn()) {
                        case 'R': {
                            if (!facture.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                                if (message) {
                                    getErrorMessage("Cette facture doit etre reglée avant de pouvoir générer une livraison");
                                }
                                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                                return;
                            }
                        }
                        case 'V': {
                            if (!facture.getStatut().equals(Constantes.ETAT_VALIDE)) {
                                if (message) {
                                    getErrorMessage("Cette facture doit etre validée avant de pouvoir générer une livraison");
                                }
                                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                                return;
                            }
                        }
                    }
                }
                if (dateLivraison == null || dateLivraison.after(new Date())) {
                    if (message) {
                        getErrorMessage("La date de livraison est incorrecte !");
                    }
                    query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                    dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                    return;
                }
                if (facture.getDepotLivrer() == null || facture.getDepotLivrer().getId() < 1) {
                    if (message) {
                        getErrorMessage("Aucun dépôt de livraison n'a été trouvé !");
                    }
                    query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                    dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                    return;
                }
                if (facture.getTrancheLivrer() == null || facture.getTrancheLivrer().getId() < 1) {
                    List<YvsGrhTrancheHoraire> list = loadTranche(facture.getDepotLivrer(), dateLivraison);
                    if (list != null && list.size() == 1) {
                        facture.setTrancheLivrer(list.get(0));
                    } else {
                        if (message) {
                            getErrorMessage("Aucune tranche de livraison n'a été trouvé !");
                        }
                        query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                        return;
                    }
                } else if (!Util.asString(facture.getTrancheLivrer().getTypeJournee())) {
                    facture.setTrancheLivrer((YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findById", new String[]{"id"}, new Object[]{facture.getTrancheLivrer().getId()}));
                }
                if (!verifyOperation(new Depots(facture.getDepotLivrer().getId(), facture.getDepotLivrer().getDesignation()), Constantes.SORTIE, Constantes.VENTE, false)) {
                    query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                    dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                    return;
                }

                //contrôle la cohérence avec les inventaires
                boolean gescom_update_stock_after_valide = autoriser("gescom_update_stock_after_valide");
                exist_inventaire = !verifyInventaire(facture.getDepotLivrer(), facture.getTrancheLivrer(), dateLivraison, (gescom_update_stock_after_valide ? false : message));
                if (exist_inventaire) {
                    if (!gescom_update_stock_after_valide) {
                        query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                        return;
                    } else if (!force) {
                        openDialog("dlgConfirmChangeInventaire");
                        query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                        dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                        return;
                    }
                }
            }
            String num = genererReference(Constantes.TYPE_BLV_NAME, dateLivraison, facture.getDepotLivrer().getId(), Constantes.DEPOT);
            if (num != null && !num.trim().isEmpty()) {
                List<YvsComContenuDocVente> l = loadContenusStay(facture, Constantes.TYPE_BLV);
                if (l != null && !l.isEmpty()) {
                    List<YvsBaseDepots> depotsLivraison = new ArrayList<>();
                    List<YvsComContenuDocVente> list = new ArrayList<>();
                    YvsBaseDepots depot;
                    for (YvsComContenuDocVente c : l) {
                        depot = facture.getDepotLivrer();
                        if (c.getDepoLivraisonPrevu() != null && (c.getDepoLivraisonPrevu().getId() != null && c.getDepoLivraisonPrevu().getId() > 0)) {
                            depot = c.getDepoLivraisonPrevu();
                        }
                        if (!depotsLivraison.contains(depot)) {
                            if (!verifyTranche(facture.getTrancheLivrer(), depot, dateLivraison)) {
                                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                                return;
                            }
                            depotsLivraison.add(depot);
                        }
                        if (c.getQuantite() > 0) {
                            if (!controlContentForTransmis(c, depot, dateLivraison, statut, message)) {
                                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                                return;
                            }
                            list.add(c);
                        }
                        if (c.getQuantiteBonus() > 0) {
                            YvsComContenuDocVente a = new YvsComContenuDocVente(c);
                            a.setArticle(new YvsBaseArticles(c.getArticleBonus().getId(), c.getArticleBonus().getRefArt(), c.getArticleBonus().getDesignation()));
                            a.setConditionnement(new YvsBaseConditionnement(c.getConditionnementBonus().getId(), new YvsBaseUniteMesure(c.getConditionnementBonus().getUnite().getId(), c.getConditionnementBonus().getUnite().getReference(), c.getConditionnementBonus().getUnite().getLibelle())));
                            a.setQuantite(c.getQuantiteBonus());
                            a.setArticleBonus(null);
                            a.setConditionnementBonus(null);
                            a.setQuantiteBonus(0.0);
                            a.setPrix(0.0);
                            a.setPrixTotal(0.0);
                            a.setComission(0.0);
                            a.setRabais(0.0);
                            a.setRemise(0.0);
                            a.setRistourne(0.0);
                            a.setTaxe(0.0);

                            if (!controlContentForTransmis(a, depot, dateLivraison, statut, message)) {
                                query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
                                dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
                                return;
                            }
                            list.add(a);
                        }
                    }
                    if (facture.getEnteteDoc() != null && !list.isEmpty()) {
                        boolean continu = true;
                        for (YvsBaseDepots d : depotsLivraison) {
                            num = genererReference(Constantes.TYPE_BLV_NAME, dateLivraison, d.getId(), Constantes.DEPOT);
                            distant = new YvsComDocVentes(facture);
                            distant.setEnteteDoc(facture.getEnteteDoc());
                            distant.setDateSave(new Date());
                            distant.setAuthor(currentUser);
                            distant.setTypeDoc(Constantes.TYPE_BLV);
                            distant.setNumDoc(num);
                            distant.setNumPiece("BL N° " + facture.getNumDoc());
                            distant.setDepotLivrer(d);
                            distant.setTrancheLivrer(facture.getTrancheLivrer());
                            distant.setLivreur(currentUser.getUsers());
                            distant.setDateLivraison(dateLivraison);
                            distant.setDateLivraisonPrevu(facture.getDateLivraisonPrevu());
                            distant.setDocumentLie(new YvsComDocVentes(facture.getId()));
                            distant.setHeureDoc(new Date());
                            distant.setStatut(Constantes.ETAT_EDITABLE);
                            distant.setStatutLivre(Constantes.ETAT_ATTENTE);
                            distant.setStatutRegle(Constantes.ETAT_ATTENTE);
                            distant.setValiderBy(currentUser.getUsers());
                            distant.setDateValider(facture.getDateLivraisonPrevu());
                            distant.setDateSave(new Date());
                            distant.setDateUpdate(new Date());
                            distant.setCloturer(false);
                            distant.setDescription("Livraison de la facture N° " + facture.getNumDoc() + " le " + ldf.format(dateLivraison) + " à " + time.format(dateLivraison));
                            distant.getContenus().clear();
                            distant.setId(null);
                            distant = (YvsComDocVentes) dao.save1(distant);
                            for (YvsComContenuDocVente c : list) {
                                if (c.getDepoLivraisonPrevu() == null || (c.getDepoLivraisonPrevu().getId() > 0 ? c.getDepoLivraisonPrevu().equals(d) : true)) {
                                    c.setDocVente(distant);
                                    c.setStatut(Constantes.ETAT_VALIDE);
                                    c.setAuthor(currentUser);
                                    if (c.getLots() == null || c.getLots().isEmpty()) {
                                        c.setParent(new YvsComContenuDocVente(c.getId()));
                                        c.setId(null);
                                        distant.getContenus().add((YvsComContenuDocVente) dao.save1(c));
                                    } else {
                                        List<YvsComLotReception> lots = new ArrayList<>(c.getLots());
                                        long id = c.getId();
                                        double quantite = c.getQuantite();
                                        double prixTotal = c.getPrixTotal();
                                        c.getLots().clear();
                                        for (int i = 0; i < lots.size(); i++) {
                                            if (lots.get(i).getQuantitee() > 0) {
                                                c.setQuantite(lots.get(i).getQuantitee());
                                                c.setPrixTotal((prixTotal / quantite) * c.getQuantite());
                                                c.setParent(new YvsComContenuDocVente(id));
                                                c.setLot(lots.get(i));
                                                c.setId(null);
                                                distant.getContenus().add((YvsComContenuDocVente) dao.save1(c));
                                            }
                                        }
                                    }
                                }
                            }
                            distant.setDocumentLie(facture);
                            boolean livrer;

                            if (statut.equals(Constantes.ETAT_VALIDE)) {
                                ManagedLivraisonVente service = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                                if (service != null) {
                                    livrer = service.validerOrder(distant, false, false, true, exist_inventaire, force);
                                    if (!livrer) {
                                        continu = false;
                                    }
                                }
                            }
                            facture.getDocuments().add(distant);
                        }
                        if (continu) {
                            String rq = "UPDATE yvs_com_doc_ventes SET statut_livre = ? WHERE id=?";
                            Options[] param = new Options[]{new Options(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE, 1), new Options(facture.getId(), 2)};
                            dao.requeteLibre(rq, param);
                            if (statut.equals(Constantes.ETAT_VALIDE)) {
                                rq = "UPDATE yvs_com_reservation_stock SET statut = 'L' WHERE id IN (SELECT id_reservation FROM yvs_com_contenu_doc_vente WHERE doc_vente = ?)";
                                param = new Options[]{new Options(facture.getId(), 1)};
                                dao.requeteLibre(rq, param);
                            }
                            facture.setConsigner(!statut.equals(Constantes.ETAT_VALIDE) && facture.getConsigner());
                            facture.setDateConsigner(statut.equals(Constantes.ETAT_VALIDE) ? null : facture.getDateConsigner());
                            facture.setStatutLivre(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                            if (statut.equals(Constantes.ETAT_VALIDE)) {
                                for (YvsComContenuDocVente c : facture.getContenus()) {
                                    if (c.getIdReservation() != null && (c.getIdReservation().getId() != null ? c.getIdReservation().getId() > 0 : false)) {
                                        long id = c.getIdReservation().getId();
                                        c.setIdReservation(null);
                                        rq = "UPDATE yvs_com_contenu_doc_vente SET id_reservation = null WHERE id = ?";
                                        param = new Options[]{new Options(c.getId(), 1)};
                                        dao.requeteLibre(rq, param);
                                        rq = "DELETE FROM yvs_com_reservation_stock WHERE id = ? AND id NOT IN (SELECT id_reservation FROM yvs_com_contenu_doc_vente WHERE id_reservation IS NOT NULL)";
                                        param = new Options[]{new Options(id, 1)};
                                        dao.requeteLibre(rq, param);
                                    }
                                }
                            }
                        }
                        if (documents.contains(facture)) {
                            documents.set(documents.indexOf(facture), facture);
                            update("data_facture_vente");
                        }
                        if (message) {
                            succes();
                        }
                        Map<String, String> statuts = dao.getEquilibreVente(facture.getId());
                        if (statuts != null) {
                            facture.setStatutLivre(statuts.get("statut_livre"));
                            facture.setStatutRegle(statuts.get("statut_regle"));
                        }
                    }
                } else {
                    if (!facture.getContenus().isEmpty()) {
                        facture.setConsigner(false);
                        facture.setDateConsigner(null);
                        facture.setStatutLivre(Constantes.ETAT_LIVRE);
                        if (documents.contains(facture)) {
                            documents.set(documents.indexOf(facture), facture);
                            update("data_facture_vente");
                        }
                        if (message) {
                            getInfoMessage("Cette facture est deja livrée à sa totalité");
                        }
                    } else {
                        if (message) {
                            getErrorMessage("Vous ne pouvez pas livrer cette facture car elle est vide");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        query = "UPDATE yvs_com_doc_ventes SET action = null WHERE id = ?";
        dao.requeteLibre(query, new Options[]{new Options(facture.getId(), 1)});
    }

    public void cloturer(YvsComDocVentes y) {
        selectDoc = y;
        update("id_confirm_close_fv");
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
        if (documents.contains(selectDoc)) {
            documents.set(documents.indexOf(selectDoc), selectDoc);
            update("data_facture_vente");
        }
    }

    public boolean changeStatut(String etat) {
        if (changeStatut_(etat, docVente, selectDoc)) {
            succes();
            return true;
        }
        return false;
    }

    //    public boolean changeStatutWithOutSucces(String etat) {
//        return ;
//    }
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

    //Fonction d'equilibre des statut des documents de vente
    public void afterChangeStatut(String etat, DocVente bean, YvsComDocVentes entity) {
        Map<String, String> statuts = dao.getEquilibreVente(entity.getId());
        if (statuts != null) {
            entity.setStatutLivre(statuts.get("statut_livre"));
            entity.setStatutRegle(statuts.get("statut_regle"));
        }
        bean.setStatut(entity.getStatut());
        bean.setStatutLivre(entity.getStatutLivre());
        bean.setStatutRegle(entity.getStatutRegle());
        //Si la méthode est appelé avec un document autre que la facture
        if (entity.getDocumentLie() != null ? entity.getDocumentLie().getId() > 0 : false) {
            statuts = dao.getEquilibreVente(entity.getDocumentLie().getId());
            if (statuts != null) {
                entity.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
                entity.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
            }
        }
        if (documents.contains(entity)) {
            documents.set(documents.indexOf(entity), entity);
        }
        update("data_facture_vente");
        update("infos_document_facture_vente");
        update("grp_btn_etat_facture_vente");
    }

    public boolean changeStatut_(String etat, DocVente bean, YvsComDocVentes entity) {
        if (!etat.equals("")) {
            if (entity.getCloturer()) {
                getErrorMessage("Ce document est vérouillé");
                return false;
            }
            String rq = "UPDATE yvs_com_doc_ventes SET statut = '" + etat + "'  WHERE id=?";
            Options[] param = new Options[]{new Options(docVente.getId(), 1)};
            dao.requeteLibre(rq, param);
            if (bean != null) {
                bean.setStatut(etat);
            }
            entity.setStatut(etat);

//             Map<String, String> statuts = dao.getEquilibreVente(doc_.getId());
//                if (statuts != null) {
//                    doc_.setStatutLivre(statuts.get("statut_livre"));
//                    doc_.setStatutRegle(statuts.get("statut_regle"));
//                }
//            YvsComDocVentes y = doc_;
//            if (y != null ? y.getId() > 0 : false) {
//                doc_.setStatut(y.getStatut());
//                doc_.setStatutLivre(y.getStatutLivre());
//                doc_.setStatutRegle(y.getStatutRegle());
//
//                doc.setStatut(y.getStatut());
//                doc.setStatutLivre(y.getStatutLivre());
//                doc.setStatutRegle(y.getStatutRegle());
//            }
//            if (doc_.getDocumentLie() != null ? doc_.getDocumentLie().getId() > 0 : false) {
//                statuts = dao.getEquilibreVente(doc_.getDocumentLie().getId());
//                if (statuts != null) {
//                    doc_.getDocumentLie().setStatutLivre(statuts.get("statut_livre"));
//                    doc_.getDocumentLie().setStatutRegle(statuts.get("statut_regle"));
//                }
//                doc_.getDocumentLie().setStatut(y.getStatut());
//                doc_.getDocumentLie().setStatutLivre(y.getStatutLivre());
//                doc_.getDocumentLie().setStatutRegle(y.getStatutRegle());
//            }
            if (documents.contains(entity)) {
                documents.set(documents.indexOf(entity), entity);
            }
            update("data_facture_vente");
            update("infos_document_facture_vente");
            update("grp_btn_etat_facture_vente");
            return true;
        }
        return false;
    }

    public boolean isConsigner(YvsComDocVentes y) {
        //se parcours mise sur le fait que le contenu de la facture est toujours très concis
        if (y.getContenus() != null) {
            for (YvsComContenuDocVente c : y.getContenus()) {
                if (c.getIdReservation() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public void consigner(YvsComDocVentes y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (y.getCloturer()) {
                    getErrorMessage("Ce document est vérouillé");
                    return;
                }
                ManagedReservation service = (ManagedReservation) giveManagedBean(ManagedReservation.class);
                if (service != null) {
                    if (!y.getConsigner()) {
                        if (!controleValidation(UtilCom.buildBeanDocVente(y), y)) {
                            return;
                        }
                        if (y.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
                            getErrorMessage("Vous ne pouvez pas consigner cette facture car elle est déja livrée");
                            return;
                        }
                        if (y.getContenus().isEmpty()) {
                            getErrorMessage("Vous ne pouvez pas consigner cette facture car elle est vide");
                            return;
                        }
                        long depot = (y.getDepotLivrer() != null) ? y.getDepotLivrer().getId() : docVente.getDepot().getId();
                        if (depot > 0) {
                            for (YvsComContenuDocVente c : y.getContenus()) {
                                YvsComReservationStock r = service.saveNew(new ReservationStock(y.getNumDoc(), new Depots(y.getDepotLivrer().getId()), new Articles(c.getArticle().getId(), c.getArticle().getDesignation()), new Conditionnement(c.getConditionnement().getId()), c.getQuantite()));
                                c.setIdReservation(r);
                                dao.update(c);
                                if (r != null ? (r.getId() != null ? r.getId() > 0 : false) : false) {
                                    y.getReservations().add(r);
                                }
                            }
                        } else {
                            getErrorMessage("Aucun dépôt n'a été trouvé pour la consignation!");
                        }
                    } else {
                        for (YvsComReservationStock r : y.getReservations()) {
                            service.deleteBean(r);
                        }
                        y.getReservations().clear();
                    }
                }
                if (documents.contains(y)) {
                    documents.set(documents.indexOf(y), y);
                }
                update("data_facture_vente");
                succes();
            }
        } catch (Exception ex) {

        }
    }

    public void consignerContent(YvsComContenuDocVente y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (y.getDocVente().getCloturer()) {
                    getErrorMessage("Ce document est vérouillé");
                    return;

                }
                ManagedReservation mr = (ManagedReservation) giveManagedBean(ManagedReservation.class
                );
                if (mr
                        != null) {
                    if (y.getIdReservation() == null) {
                        if (y.getDocVente().getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
                            getErrorMessage("Vous ne pouvez pas consigner cette facture car elle est déja livrée");
                            return;
                        }
                        long depot = docVente.getDepot().getId();
                        if (y.getDepoLivraisonPrevu() != null ? (y.getDepoLivraisonPrevu().getId() != null ? y.getDepoLivraisonPrevu().getId() > 0 : false) : false) {
                            depot = y.getDepoLivraisonPrevu().getId();
                        } else if (y.getDocVente().getDepotLivrer() != null ? (y.getDocVente().getDepotLivrer().getId() != null ? y.getDocVente().getDepotLivrer().getId() > 0 : false) : false) {
                            depot = y.getDocVente().getDepotLivrer().getId();
                        }
                        if (depot > 0) {
                            YvsComReservationStock r = mr.saveNew(new ReservationStock(y.getDocVente().getNumDoc(), new Depots(y.getDocVente().getDepotLivrer().getId()), new Articles(y.getArticle().getId(), y.getArticle().getDesignation()), new Conditionnement(y.getConditionnement().getId()), y.getQuantite()));
                            y.setIdReservation(r);
                            dao.update(y);
                            if (r != null ? (r.getId() != null ? r.getId() > 0 : false) : false) {
                                y.getDocVente().getReservations().add(r);
                                int idx = documents.indexOf(y.getDocVente());
                                if (idx > -1) {
                                    documents.get(idx).getReservations().add(r);
                                }
                            }
                        } else {
                            getErrorMessage("Aucun dépôt n'a été trouvé pour la consignation!");
                        }
                    } else {
                        try {
                            YvsComReservationStock r = y.getIdReservation();
                            y.setIdReservation(null);
                            y.setDateUpdate(new Date());
                            dao.update(y);
                            mr.deleteBean(r);
                            y.getDocVente().getReservations().remove(r);
                            int idx = documents.indexOf(y.getDocVente());
                            if (idx > -1) {
                                documents.get(idx).getReservations().remove(r);
                            }
                            r = null;
                        } catch (Exception ex) {
                            getException("Lymytz Error>>> ", ex);
                            getErrorMessage("Impossible d'effectuer cette opération");
                        }
                    }
                }

                succes();
            }
        } catch (Exception ex) {

        }
    }

    public void print(YvsComDocVentes y) {
        print(y, true);
    }

    public void print(YvsComDocVentes y, boolean withHeader) {
        print(y, null, withHeader);
    }

    public void print(YvsComDocVentes y, String model, boolean withHeader) {
        try {
            if (currentParamVente == null) {
                currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (y != null ? y.getId() > 0 : false) {
                if (currentParamVente != null ? (currentParamVente.getPrintDocumentWhenValide() && !y.getStatut().equals(Constantes.ETAT_VALIDE)) : false) {
                    getErrorMessage("Le document doit être validé pour pouvoir être téléchargé");
                    return;
                }
                Double ca = dao.loadCaVente(y.getId());
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(true));
                param.put("MONTANT", Nombre.CALCULATE.getValue(ca));
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutRegle().equals(Constantes.ETAT_REGLE) ? "solde.png" : "empty.png"));
                param.put("IMG_LIVRE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutLivre().equals(Constantes.ETAT_LIVRE) ? "livre.png" : "empty.png"));
                String IMG_QRC = Util.createQRCode(y.getNumDoc());
                if (Util.asString(IMG_QRC)) {
                    param.put("IMG_QRC", IMG_QRC);
                }
                param.put("MODEL", model);
                param.put("VIEW_HEADER", withHeader);
                String report = currentParamVente != null ? currentParamVente.getModelFactureVente() : "facture_vente_v1";
                String fileName = executeReport(report, param, true);
                docVente.setPrintPath(fileName);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void print_OLD(YvsComDocVentes y, boolean withHeader) {
        try {
            if (currentParamVente == null) {
                currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (y != null ? y.getId() > 0 : false) {
                Double ca = dao.loadCaVente(y.getId());
                Double tx = dao.loadTaxeVente(y.getId());
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutRegle().equals(Constantes.ETAT_REGLE) ? "solde.png" : "empty.png"));
                param.put("IMG_LIVRE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutLivre().equals(Constantes.ETAT_LIVRE) ? "livre.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(ca));
                String AUTEUR = currentUser.getUsers().getNomUsers();
                String report = currentParamVente != null ? currentParamVente.getModelFactureVente() : "facture_vente";
                if (report.equals("facture_vente_by_code")) {
                    AUTEUR = currentUser.getUsers().getAbbreviation();
                }
                param.put("AUTEUR", AUTEUR);
                param.put("TAXE", tx != null ? tx > 0 : false);
                param.put("LOGO", returnLogo());
                String IMG_QRC = Util.createQRCode(y.getNumDoc());
                if (Util.asString(IMG_QRC)) {
                    param.put("IMG_QRC", IMG_QRC);
                }
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(withHeader));
                String fileName = executeReport(report, param, true);
                docVente.setPrintPath(fileName);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printTicket(YvsComDocVentes y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Double ca = dao.loadCaVente(y.getId());
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                Map<String, Object> param = new HashMap<>();
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutRegle().equals(Constantes.ETAT_REGLE) ? "solde.png" : "empty.png"));
                param.put("IMG_LIVRE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutLivre().equals(Constantes.ETAT_LIVRE) ? "livre.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(ca));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                String IMG_QRC = Util.createQRCode(y.getNumDoc());
                if (Util.asString(IMG_QRC)) {
                    param.put("IMG_QRC", IMG_QRC);
                }
                String fileName = executeReport("ticket_vente", param, true);
                System.err.println("fileName : " + fileName);
                docVente.setPrintPath(fileName);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printSource(YvsComDocVentes y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Double ca = dao.loadCaVente(y.getId());
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                Map<String, Object> param = new HashMap<>();
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutRegle().equals(Constantes.ETAT_REGLE) ? "solde.png" : "empty.png"));
                param.put("IMG_LIVRE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutLivre().equals(Constantes.ETAT_LIVRE) ? "livre.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(ca));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                String IMG_QRC = Util.createQRCode(y.getNumDoc());
                if (Util.asString(IMG_QRC)) {
                    param.put("IMG_QRC", IMG_QRC);
                }
//                valuePrint = "data:application/pdf;base64," + Base64.encodeBase64String(sourceReport("ticket_vente", param));
//                System.out.println("valuePrint " + valuePrint);
//                execute("print(" + valuePrint + ")");
                execute("window.open(D:/Documents/CV dowes 2003.pdf)");
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printListing() {
        Map<String, Object> param = new HashMap<>();
        param.put("AUTEUR", currentUser.getUsers().getNomUsers());
        param.put("LOGO", returnLogo());
        param.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("CLIENT", null);
        param.put("POINT", point_ > 0 ? (int) point_ : null);
        param.put("VENDEUR", null);
        param.put("AGENCE", currentAgence.getId().intValue());
        param.put("DATEDEBUT", dateDebut);
        param.put("DATEFIN", dateFin);
        param.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
        executeReport("listing_facture_vente", param);
    }

    public void openPrintDialog() {
        if (Util.asString(docVente.getPrintPath())) {
            openDialog("dlgPrint");
            update("media-print");
        }
    }

    public void changeOperateurRef(ValueChangeEvent ev) {
        if (ev != null) {
            operateurRef = (String) ev.getNewValue();
            addParamVendeur(true);
        }
    }

    private void addParamNumDoc() {
        ParametreRequete p;
        String predicat = (operateurRef.trim().equals("LIKE")) ? "OR" : "AND";
        if (numSearch_ != null ? numSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numDoc", "%" + numSearch_ + "%", operateurRef, "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "numDoc)", "numDoc", "%" + numSearch_.trim().toUpperCase() + "%", operateurRef, predicat));
            p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "numPiece)", "numDoc", "%" + numSearch_.trim().toUpperCase() + "%", operateurRef, predicat));
            p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "numeroExterne)", "numDoc", "%" + numSearch_.trim().toUpperCase() + "%", operateurRef, predicat));
        } else {
            p = new ParametreRequete(pre + "numDoc", "numDoc", null);
        }
        p_contenu.addParam(p);
        paginator.addParam(p);
    }

    public void searchByNum() {
        addParamNumDoc();
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
        if (documents.size() == 1) {
            onSelectObject(documents.get(0));
            execute("collapseForm('facture_vente')");
        } else {
            execute("collapseList('facture_vente')");
        }
    }

    public void addParamIds() {
        addParamIds(true);
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_facture_vente c RIGHT JOIN yvs_com_doc_ventes y ON c.facture = y.id "
                    + "INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id "
                    + "INNER JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id INNER JOIN yvs_base_point_vente p ON cp.point = p.id "
                    + "INNER JOIN yvs_agences a ON p.agence = a.id WHERE y.type_doc = 'FV' AND y.statut = 'V' AND a.societe = ? "
                    + "AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (date_) {
                query += " AND e.date_entete BETWEEN ? AND ?";
                param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)};
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, param);
            nbrComptaSearch = count != null ? count : 0;
        }
        paginator.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void addParamWithTiers() {
        ParametreRequete p = new ParametreRequete("y.tiers", "withTiers", null, "IN", "AND");
        if (withTiersSearch != null) {
            p = new ParametreRequete("y.tiers", "withTiers", "tiers", withTiersSearch ? "IS NOT NULL" : "IS NULL", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        if (searchAutomatique) {
            loadAllFacture(true);
        }
    }

    public void addParamType() {
        addParamType(true);
    }

    public void addParamType(boolean load) {
        ParametreRequete p;
        if (Util.asString(typeSearch)) {
            p = new ParametreRequete(pre + "typeDoc", "typeDoc", typeSearch, "=", "AND");
        } else {
            p = new ParametreRequete(null, "typeDoc", "XXXX", "=", "AND");
            p.getOtherExpression().add(new ParametreRequete(pre + "typeDoc", "type", Constantes.TYPE_FV, "=", "OR"));
            p.getOtherExpression().add(new ParametreRequete(pre + "typeDoc", "type1", Constantes.TYPE_BCV, "=", "OR"));
        }
        p_contenu.addParam(p);
        paginator.addParam(p);
        if (load) {
            initForm = true;
            if (searchAutomatique) {
                loadAllFacture(true);
            }
        }
    }

    public void addParamDepotLivraison() {
        try {
            ParametreRequete p = new ParametreRequete("y.id", "out_in_depot", null, "IN", "AND");
            if (depot_ > 0) {
                String query = "SELECT DISTINCT y.id FROM yvs_com_doc_ventes y INNER JOIN yvs_com_doc_ventes d ON d.document_lie = y.id INNER JOIN yvs_base_depots e ON d.depot_livrer = e.id INNER JOIN yvs_agences a ON e.agence = a.id "
                        + " INNER JOIN yvs_com_entete_doc_vente v ON y.entete_doc = v.id INNER JOIN yvs_com_creneau_horaire_users h ON v.creneau = h.id INNER JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id"
                        + " WHERE y.type_doc = 'FV' AND d.type_doc = 'BLV' AND a.societe = ? AND d.depot_livrer " + egaliteDepot + " ?";
                List<Options> params = new ArrayList<>();
                params.add(new Options(currentAgence.getSociete().getId(), params.size() + 1));
                params.add(new Options(depot_, params.size() + 1));
                if (!currentUser.getUsers().getAccesMultiAgence()) {
                    query += " AND e.agence = ?";
                    params.add(new Options(currentAgence.getId(), params.size() + 1));
                }
                if (point_ > 0) {
                    query += " AND cp.point = ?";
                    params.add(new Options(point_, params.size() + 1));
                }
                if (paramDate) {
                    query += " AND d.date_livraison BETWEEN ? AND ?";
                    params.add(new Options(dateDebut, params.size() + 1));
                    params.add(new Options(dateFin, params.size() + 1));
                } else {
                    depot_ = 0;
                    getErrorMessage("Vous devez préciser une période");
                    update("_select_depot_2");
                    return;
                }
                List<Long> ids = dao.loadListBySqlQuery(query, params.toArray(new Options[params.size()]));
                if (ids != null ? ids.isEmpty() : true) {
                    ids = new ArrayList<Long>() {
                        {
                            add(-1L);
                        }
                    };
                }
                if (ids.size() > 15000) {
                    depot_ = 0;
                    getErrorMessage("Trop de factures à analyser. veuillez reduire la période de recherche");
                    paginator.addParam(p);
                    update("_select_depot_2");
                    return;
                }
                p = new ParametreRequete("y.id", "out_in_depot", ids, "IN", "AND");
            } else {
                int index = paginator.getParams().indexOf(p);
                if (index < 0) {
                    return;
                }
            }
            paginator.addParam(p);
            initForm = true;
            if (searchAutomatique) {
                loadAllFacture(true);
            }
        } catch (Exception ex) {
            getException("(addParamDepotLivraison) : " + ex.getMessage(), ex);
        }
    }

    public void comptabiliseByDate(Date dateDebut, Date dateFin) {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            champ = new String[]{"societe", "statut", "dateDebut", "dateFin", "type"};
            val = new Object[]{currentAgence.getSociete(), Constantes.ETAT_VALIDE, dateDebut, dateFin, Constantes.TYPE_FV};
            List<YvsComDocVentes> list = dao.loadNameQueries("YvsComDocVentes.findByStatutDates", champ, val);
            if (list != null ? !list.isEmpty() : false) {
                if (list.size() > 10000) {
                    getErrorMessage("Veuillez entrer une période plus petite");
                    return;
                }
                for (YvsComDocVentes y : list) {
                    w.comptabiliserVente(y, false, false);
                }
                succes();
            }
        }
    }

    public void findFactureByNumAndNotRegle() {
        addParamNumDoc();
        statutRegle_ = Constantes.ETAT_REGLE;
        egaliteStatutR = "!=";
        addParamStatutDoc();
        initForm = true;
        loadAllFacture(true);
    }

    public DocVente searchFacture(String num, Client c, boolean open) {
        DocVente a = new DocVente();
        a.setNumDoc(num);
        a.setError(true);
        if (num != null ? num.trim().length() > 0 : false) {
            ParametreRequete p = new ParametreRequete("UPPER(y.numDoc)", "numDoc", num.trim().toUpperCase() + "%", "LIKE", "AND");
            paginator.addParam(p);
        } else {
            paginator.addParam(new ParametreRequete("y.numDoc", "numDoc", null));
        }
        if (c != null ? c.getId() > 0 : false) {
            codeClient_ = c.getCodeClient();
            searchByClient();
        } else {
            initForm = true;
            loadAllFacture(true);
        }
        a = chechFactureResult(open);
        if (a != null ? a.getId() < 1 : true) {
            a.setNumDoc(num);
            a.setError(true);
        }
        return a;
    }

    private DocVente chechFactureResult(boolean open) {
        DocVente a = new DocVente();
        if (documents != null ? !documents.isEmpty() : false) {
            if (documents.size() > 1) {
                if (open) {
                    openDialog("dlgListFactures");
                }
                a.setList(true);
            } else {
                YvsComDocVentes c = documents.get(0);
                a = UtilCom.buildBeanDocVente(c);
            }
            a.setError(false);
        }
        return a;
    }

    public void removeDoublon(YvsComDocVentes y) {
        selectDoc = y;
        removeDoublon();
    }

    public void removeDoublon() {
        if ((selectDoc != null) ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
            if (!selectDoc.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                getErrorMessage("Le document doit etre editable pour pouvoir etre modifié");
                return;
            }
            removeDoublonVente(selectDoc.getId());
            succes();
        }
    }

    public void maintenanceComptable() {
        try {
            String query = "SELECT y.facture AS count FROM yvs_compta_content_journal_facture_vente y GROUP BY y.facture HAVING (COUNT(y.piece)) > 1";
            List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{});

            if (ids != null ? !ids.isEmpty() : false) {
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                );
                if (w
                        != null) {
                    for (Long id : ids) {
                        w.unComptabiliserVente(new YvsComDocVentes(id, "", Constantes.SCR_VENTE, ""), false);
                    }
                }
            }
            query = "SELECT DISTINCT y.ref_externe AS externe FROM yvs_compta_content_journal y INNER JOIN yvs_compta_pieces_comptable p ON y.piece = p.id"
                    + "	INNER JOIN yvs_compta_journaux j ON p.journal = j.id INNER JOIN yvs_com_doc_ventes d ON y.ref_externe = d.id"
                    + " WHERE y.table_externe = 'DOC_VENTE' AND j.agence = ? AND d.type_doc != 'FV'";
            ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getId(), 1)});

            if (ids != null ? !ids.isEmpty() : false) {
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                );
                if (w
                        != null) {
                    for (Long id : ids) {
                        w.unComptabiliserVente(new YvsComDocVentes(id, "", Constantes.SCR_VENTE, ""), false);
                    }
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedFactureVente (maintenanceComptable)", ex);
        }
    }

    @Override
    public void cleanEnteteVente() {
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.cleanEnteteVente();
        }
    }

    @Override
    public void cleanVente() {
        if (!autoriser("fv_clean")) {
            openNotAcces();
            return;
        }
        super.cleanVente();
        initForm = true;
        loadAllFacture(true);
    }

    public void cleanVenteByDate() {
        if (!autoriser("fv_clean")) {
            openNotAcces();
            return;
        }
        super.cleanVenteByDate(dateClean);
        initForm = true;
        loadAllFacture(true);
    }

    public void recalculPr() {
        if (!autoriser("recalcul_pr")) {
            openNotAcces();
            return;
        }
        if (!paramDate || dateDebut == null || dateFin == null) {
            getErrorMessage("Vous devez precisez une période");
            return;
        }
        String query = "SELECT com_recalcule_pr_periode_vente(?,?,?)";
        dao.callFonction(query, new Options[]{new Options(currentAgence.getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)});
        succes();
    }

    public void equilibreByDate() {
        dao.getEquilibreVente(currentAgence.getSociete().getId(), dateDebut, dateFin);
    }

    public void equilibre() {
        equilibre(selectDoc);
    }

    public void equilibre(YvsComDocVentes selectDoc) {
        equilibre(selectDoc, true);
    }

    public void equilibreAll() {
        try {
            if (selections != null ? !selections.isEmpty() : false) {
                for (YvsComDocVentes bean : selections) {
                    equilibre(bean, false);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void equilibre(YvsComDocVentes selectDoc, boolean msg) {
        if ((selectDoc != null) ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
            if (selectDoc.getTypeDoc().equals(Constantes.TYPE_FV)) {
                Map<String, String> statuts = dao.getEquilibreVente(selectDoc.getId());
                if (statuts != null) {
                    selectDoc.setStatutLivre(statuts.get("statut_livre"));
                    selectDoc.setStatutRegle(statuts.get("statut_regle"));
                }
            } else {
                ManagedBonVente w = (ManagedBonVente) giveManagedBean(ManagedBonVente.class);
                if (w != null) {
                    w.equilibre(selectDoc, false);
                }
            }
            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
            int idx = documents.indexOf(selectDoc);
            if (idx > -1) {
                documents.set(idx, selectDoc);
                update("data_facture_vente");
            }
            if (msg) {
                succes();
            }
        }
    }

    public void maintenance(YvsComDocVentes dv) {
        try {
            dv.setAction(null);
            dv.setDateUpdate(new Date());
            dv.setAuthor(currentUser);
            dao.update(dv);
            succes();
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void maintenance() {
        String query = "SELECT y.id FROM yvs_com_doc_ventes y INNER JOIN yvs_com_entete_doc_vente e ON y.entete_doc = e.id INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id INNER JOIN yvs_users u ON c.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id"
                + " WHERE a.societe = ? AND y.type_doc = 'FV' AND y.statut = 'V' AND y.livraison_auto IS TRUE AND y.id NOT IN (SELECT d.document_lie FROM yvs_com_doc_ventes d WHERE d.type_doc = 'BLV' AND d.document_lie = y.id)";
        List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(currentAgence.getSociete().getId(), 1)});
        if (ids != null ? !ids.isEmpty() : false) {
            YvsComDocVentes y;
            for (Long id : ids) {
                y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{id});
                if (y != null ? y.getId() > 0 : false) {
                    transmisOrder(y, y.getEnteteDoc().getDateEntete(), Constantes.ETAT_VALIDE, false, false);
                }
            }
        }
        update("data_facture_vente");
    }

    public void traitementLot() {
        try {
            if (!selections.isEmpty()) {
                DocVente d;
                ManagedReglementVente wr = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                ManagedLivraisonVente wl = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                ManagedSaisiePiece wp = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                for (YvsComDocVentes y : selections) {
                    if (y.getTypeDoc().equals(Constantes.TYPE_BCV)) {
                        getWarningMessage("Vous ne pouvez pas faire de traitement en lot des commandes sur cette page");
                        return;
                    } else {
                        if (lotFV) {
                            d = UtilCom.buildBeanDocVente(y);
                            if (statutLotFV.equals(Constantes.ETAT_VALIDE)) {
                                validerOrder(d, y, false, false);
                            } else {
                                cancelEtapeFacture(y, d, false, false, false);
                            }
                        }
                        if (lotBL) {
                            if (statutLotBL.equals(Constantes.ETAT_ANNULE)) {
                                if (wl != null) {
                                    List<YvsComDocVentes> livraisons = dao.loadNameQueries("YvsComDocVentes.findBLVByParent", new String[]{"documentLie"}, new Object[]{y});
                                    if (livraisons != null ? !livraisons.isEmpty() : false) {
                                        for (YvsComDocVentes l : livraisons) {
                                            if (l.getStatut().equals(Constantes.ETAT_VALIDE) || l.getStatut().equals(Constantes.ETAT_ENCOURS)) {
                                                if (!wl.controlToAnnulerOrder(l, false, false, false, false)) {
                                                    continue;
                                                }
                                            }
                                            dao.delete(l);
                                        }
                                        String statut_livre = dao.getEquilibreVenteLivre(y.getId());
                                        y.setStatutLivre(statut_livre);
                                        int index = documents.indexOf(y);
                                        if (index > -1) {
                                            documents.set(index, y);
                                        }
                                    }
                                }
                            } else {
                                transmisOrder(y, y.getEnteteDoc().getDateEntete(), statutLotBL, false, false);
                            }
                        }
                        if (lotPiece) {
                            if (wr != null) {
                                if (statutLotPiece.equals(Constantes.ETAT_ANNULE)) {
                                    List<YvsComptaCaissePieceVente> reglements = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFacture", new String[]{"facture"}, new Object[]{y});
                                    if (reglements != null ? !reglements.isEmpty() : false) {
                                        for (YvsComptaCaissePieceVente r : reglements) {
                                            if (r.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) || r.getStatutPiece().equals(Constantes.STATUT_DOC_ENCOUR)) {
                                                if (!wr.verifyCancelPieceCaisse(r.getDatePaiement())) {
                                                    continue;
                                                }
                                                if (dao.isComptabilise(r.getId(), Constantes.SCR_CAISSE_VENTE)) {
                                                    if (!autoriser("compta_od_annul_comptabilite")) {
                                                        continue;
                                                    }
                                                    if (!wp.unComptabiliserCaisseVente(r, false)) {
                                                        continue;
                                                    }
                                                }
                                            }
                                            dao.delete(r);
                                        }
                                        String statut_regle = dao.getEquilibreVenteRegle(y.getId());
                                        y.setStatutRegle(statut_regle);
                                        int index = documents.indexOf(y);
                                        if (index > -1) {
                                            documents.set(index, y);
                                        }
                                    }
                                } else {
                                    Caisses caisse = null;
                                    ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                                    if (w != null) {
                                        //trouve la caisse par defaut
                                        YvsBaseCaisse c = w.findByResponsable(y.getEnteteDoc().getCreneau().getUsers());
                                        caisse = UtilCompta.buildSimpleBeanCaisse_(c);
                                    }
                                    if (caisse != null ? caisse.getId() > 0 : false) {
                                        setMontantTotalDoc(y);
                                        DocVente vente = UtilCom.buildSimpleBeanOnlyDocVente(y);
                                        if (y.getReglements() == null) {
                                            y.setReglements(new ArrayList<YvsComptaCaissePieceVente>());
                                        }
                                        y.getReglements().addAll(wr.generetedPiecesFromModel(new YvsBaseModelReglement(y.getModelReglement() != null ? y.getModelReglement().getId() : 0), vente, new YvsBaseCaisse(caisse.getId())));
                                        for (YvsComptaCaissePieceVente p : y.getReglements()) {
                                            if (!p.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                                p.setVente(y);
                                                if (p.getId() < 1 && (p.getCaisse() != null) ? p.getCaisse().getId() > 0 : false && (p.getModel() != null) ? p.getModel().getId() > 0 : false && p.getMontant() > 0) {
                                                    p.setId(null);
                                                    p = (YvsComptaCaissePieceVente) dao.save1(p);
                                                }
                                                if (p.getId() > 0 ? statutLotPiece.equals(Constantes.ETAT_REGLE) ? p.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) : false : false) {
                                                    wr.openConfirmPaiement(p, "F", false, false, false);
                                                    wr.reglerPieceTresorerie(vente, p, "F", false);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                getErrorMessage("Vous n'avez selectionné aucunne facture");
            }
        } catch (NumberFormatException ex) {
        }
        update("data_facture_vente");
    }

    public void gotoViewBl(YvsComDocVentes dv) {
        if (dv != null) {
            ManagedLivraisonVente service = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class
            );
            if (service
                    != null) {
                service.onSelectFacture(dv);
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Livraisons Vente", "modGescom", "smenBonLivraisonVente", true);
                }
            }
        }
    }

    public void gotoViewReglementsVente(YvsComDocVentes dv) {
        if (dv != null) {
            ManagedReglementVente service = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class
            );
            if (service
                    != null) {
                service.onSelectedFacture(dv);
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Règlements ventes", "modCompta", "smenRegVente", true);
                }
            }
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setMontantEntete(DocVente bean) {
        if (docVente.getEnteteDoc() != null ? docVente.getEnteteDoc().getId() > 0 : false) {
            docVente.getEnteteDoc().setMontantRemise(0);
            docVente.getEnteteDoc().setMontantTaxe(0);
            docVente.getEnteteDoc().setMontantRistourne(0);
            docVente.getEnteteDoc().setMontantCommission(0);
            docVente.getEnteteDoc().setMontantHT(0);
            docVente.getEnteteDoc().setMontantTTC(0);
            docVente.getEnteteDoc().setMontantRemises(0);
            docVente.getEnteteDoc().setMontantCoutSup(0);
            docVente.getEnteteDoc().setMontantAvance(0);
            docVente.getEnteteDoc().setMontantTaxeR(0);
            docVente.getEnteteDoc().setAvanceCommande(0);

            List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByTypeDocEntete", new String[]{"entete", "typeDoc", "societe"}, new Object[]{new YvsComEnteteDocVente(docVente.getEnteteDoc().getId()), Constantes.TYPE_FV, currentAgence.getSociete()});
            for (YvsComDocVentes d : l) {
                setMontantTotalDoc(d, d.getContenus(), currentAgence.getSociete().getId(), null, null, dao);

                docVente.getEnteteDoc().setMontantRemise(docVente.getEnteteDoc().getMontantRemise() + d.getMontantRemise());
                docVente.getEnteteDoc().setMontantTaxe(docVente.getEnteteDoc().getMontantTaxe() + d.getMontantTaxe());
                docVente.getEnteteDoc().setMontantRistourne(docVente.getEnteteDoc().getMontantRistourne() + d.getMontantRistourne());
                docVente.getEnteteDoc().setMontantCommission(docVente.getEnteteDoc().getMontantCommission() + d.getMontantCommission());
                docVente.getEnteteDoc().setMontantHT(docVente.getEnteteDoc().getMontantHT() + d.getMontantHT());
                docVente.getEnteteDoc().setMontantTTC(docVente.getEnteteDoc().getMontantTTC() + d.getMontantTTC());
                docVente.getEnteteDoc().setMontantRemises(docVente.getEnteteDoc().getMontantRemises() + d.getMontantRemises());
                docVente.getEnteteDoc().setMontantCoutSup(docVente.getEnteteDoc().getMontantCoutSup() + d.getMontantCS());
                docVente.getEnteteDoc().setMontantAvance(docVente.getEnteteDoc().getMontantAvance() + d.getMontantAvance());
                docVente.getEnteteDoc().setMontantTaxeR(docVente.getEnteteDoc().getMontantTaxeR() + d.getMontantTaxeR());
            }
            String query = "SELECT DISTINCT y.id FROM yvs_com_doc_ventes y WHERE y.type_doc= ? AND y.entete_doc = ? AND y.statut = ? AND y.document_lie IS NULL";
            Options[] params = new Options[]{new Options(Constantes.TYPE_BCV, 1), new Options(docVente.getEnteteDoc().getId(), 2), new Options(Constantes.ETAT_VALIDE, 3)};
            List<Long> ids = dao.loadListBySqlQuery(query, params);
            if (ids != null ? !ids.isEmpty() : false) {
                nameQueri = "YvsComptaCaissePieceVente.findSumByVentesStatut";
                champ = new String[]{"ventes", "statut"};
                val = new Object[]{ids, Constantes.STATUT_DOC_PAYER};
                Double valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                docVente.getEnteteDoc().setAvanceCommande(valeur != null ? valeur : 0);
            }
            docVente.getEnteteDoc().setVersementAttendu(dao.loadVersementAttendu(docVente.getEnteteDoc().getUsers().getId(), docVente.getEnteteDoc().getDateEntete(), docVente.getEnteteDoc().getDateEntete()));
            update("blog_form_montant_entete");
        }
        if (bean != null ? bean.getId() > 0 : false) {
//            setMontantTotalDoc(bean);
            setMontantTotalDoc(bean, bean.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
        }
    }

    public Date echeance(YvsComDocVentes y) {
        if (y != null ? y.getId() != null : false) {
            champ = new String[]{"facture", "etat"};
            val = new Object[]{y, Constantes.ETAT_REGLE};
            nameQueri = "YvsComMensualiteFactureVente.findByVenteNotEtat";
            List<YvsComMensualiteFactureVente> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
            if (l != null ? !l.isEmpty() : false) {
                return l.get(0).getDateMensualite();
            }
        }
        return new Date();
    }

    public double chiffreAffaire(YvsComDocVentes y) {
        if (y != null ? y.getId() != null : false) {
            return dao.loadCaVente(y.getId());
        }
        return 0;
    }

    public double netAPayer(YvsComDocVentes y) {
        if (y != null ? y.getId() != null : false) {
            return dao.loadNetAPayerVente(y.getId());
        }
        return 0;
    }

    public void saveZone() {
        ManagedDico a = (ManagedDico) giveManagedBean("Mdico");
        if (a != null) {
            YvsDictionnaire y = a.save();
            if (y != null) {
                docVente.setAdresse(UtilGrh.buildBeanDictionnaire(y));
            }
            a.resetFiche();
        }
    }

    public void findZone() {
        ManagedDico a = (ManagedDico) giveManagedBean("Mdico");
        docVente.getAdresse().setError(true);
        docVente.getAdresse().setLibelle("");
        docVente.getAdresse().setId(0);
        if (a != null) {
            if (docVente.getAdresse() != null) {
                Dictionnaire y = a.findZone(docVente.getAdresse().getAbreviation(), true);
                if (a.getDictionnaires() != null ? a.getDictionnaires().size() > 1 : false) {
                    update("data_zone_fv");
                } else {
                    docVente.setAdresse(y);
                }
                docVente.getAdresse().setError(false);
            }
        }
    }

    public void initZones() {
        ManagedDico a = (ManagedDico) giveManagedBean("Mdico");
        if (a != null) {
            a.initZones(docVente.getAdresse());
            update("data_zone_fv");
        }
    }

    public void initCreateClient() {
        ManagedDico a = (ManagedDico) giveManagedBean("Mdico");
        if (a != null) {
            a.loadPays();
            update("pays_client_facture_vente");

        }
        ManagedClient s = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (s != null) {
            s.resetFiche();
            s.getClient().setRepresentant(new Users(currentUser.getUsers().getId(), currentUser.getUsers().getNomUsers()));
        }
    }

    /*
     DEBUT WORKFLOW
     */
    private YvsWorkflowValidFactureVente currentEtape;

    public List<YvsWorkflowEtapeValidation> getAllEtapeValidation() {
        champ = new String[]{"titre", "societe"};
        val = new Object[]{Constantes.DOCUMENT_FACTURE_VENTE, currentAgence.getSociete()};
        return dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", champ, val);
    }

    public List<YvsWorkflowValidFactureVente> saveEtapesValidation(YvsComDocVentes m, List<YvsWorkflowEtapeValidation> model) {
        //charge les étape de vailidation
        List<YvsWorkflowValidFactureVente> re = new ArrayList<>();
        if (!model.isEmpty()) {
            YvsWorkflowValidFactureVente vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    champ = new String[]{"facture", "etape"};
                    val = new Object[]{m, et};
                    YvsWorkflowValidFactureVente w = (YvsWorkflowValidFactureVente) dao.loadOneByNameQueries("YvsWorkflowValidFactureVente.findByEtapeFacture", champ, val);
                    if (w != null ? w.getId() < 1 : true) {
                        vm = new YvsWorkflowValidFactureVente();
                        vm.setAuthor(currentUser);
                        vm.setEtape(et);
                        vm.setEtapeValid(false);
                        vm.setFactureVente(m);
                        vm.setId(null);
                        vm.setOrdreEtape(et.getOrdreEtape());
                        vm = (YvsWorkflowValidFactureVente) dao.save1(vm);
                        re.add(vm);
                    }
                }
            }
        }
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidFactureVente> ordonneEtapes(List<YvsWorkflowValidFactureVente> l) {
        return YvsWorkflowValidFactureVente.ordonneEtapes(l);
    }

    public boolean validFirstEtapeFacture(long facture, YvsUsersAgence users) {
        if (facture > 0) {
            YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{facture});
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                List<YvsWorkflowEtapeValidation> etapes = getAllEtapeValidation();
                y.setEtapeTotal(etapes != null ? etapes.size() : 0);
                List<YvsWorkflowValidFactureVente> list = saveEtapesValidation(y, etapes);
                if (list != null ? !list.isEmpty() : false) {
                    return validEtapeFacture(y, null, users, list.get(0));
                }
            }
        }
        return false;
    }

    public boolean validEtapeFacture(YvsComDocVentes current, DocVente vente, YvsUsersAgence users, YvsWorkflowValidFactureVente etape) {
        return validEtapeFacture(current, vente, users, etape, dao);
    }

    public boolean validEtapeFacture(YvsComDocVentes current, DocVente vente, YvsUsersAgence users, YvsWorkflowValidFactureVente etape, boolean byList) {
        return validEtapeFacture(current, vente, users, etape, dao, byList);
    }

    public boolean validEtapeFacture(YvsComDocVentes current, DocVente vente, YvsUsersAgence users, YvsWorkflowValidFactureVente etape, DaoInterfaceLocal dao) {
        return validEtapeFacture(current, vente, users, etape, dao, false);
    }

    public boolean validEtapeFacture(YvsComDocVentes current, DocVente vente, YvsUsersAgence users, YvsWorkflowValidFactureVente etape, DaoInterfaceLocal dao, boolean byList) {
        if (etape != null) {
            if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
                openNotAcces();
            } else {
                //contrôle la cohérence des dates
                if (current != null ? (current.getId() != null ? current.getId() < 1 : true) : true) {
                    current = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{vente.getId()});
                }
//            if (vente != null ? vente.getId() < 1 : true) {
//                vente = UtilCom.buildBeanDocVente(current);
//            }
                current.setEtapesValidations(ordonneEtapes(current.getEtapesValidations()));
                etape.setFactureVente(current);
                int idx = current.getEtapesValidations().indexOf(etape);
                if (idx >= 0) {
                    //cas de la validation de la dernière étapes
                    if (etape.getEtapeSuivante() == null) {
                        if (validerOrder(vente, current, true, false, byList)) {
                            etape.setAuthor(users);
                            etape.setEtapeValid(true);
                            etape.setMotif(null);
                            etape.setEtapeActive(false);
                            etape.setDateUpdate(new Date());
                            if (current.getEtapesValidations().size() > (idx + 1)) {
                                current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                            }
                            dao.update(etape);
                            current.setEtapeValide(current.getEtapeValide() + 1);
                            if (documents != null ? documents.contains(current) : false) {
                                int idx_ = documents.indexOf(current);
                                documents.get(idx_).setEtapeValide(current.getEtapeValide());
                            }
                            vente.setEtapesValidations(current.getEtapesValidations());
                            if (page.equals("V3")) {
                                update("blog_form_contenu_facture_vente");
                                update("blog_form_cout_facture_vente");
                                update("form_mensualite_facture_vente");
                            } else {
                                update("tabview_facture_vente:blog_form_contenu_facture_vente");
                                update("tabview_facture_vente:blog_form_cout_facture_vente");
                                update("tabview_facture_vente:form_mensualite_facture_vente");
                            }
                            update("data_livraison_facture_vente");
                            update("blog_commercial_vente");
                            return true;
                        }
                    } else {
                        etape.setAuthor(users);
                        etape.setEtapeValid(true);
                        etape.setEtapeActive(false);
                        etape.setMotif(null);
                        etape.setDateUpdate(new Date());
                        if (current.getEtapesValidations().size() > (idx + 1)) {
                            current.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                        }
                        dao.update(etape);

                        vente.setStatut(Constantes.ETAT_ENCOURS);
                        current.setStatut(Constantes.ETAT_ENCOURS);
                        current.setDateUpdate(new Date());
                        current.setEtapeValide(current.getEtapeValide() + 1);
                        dao.update(current);
                        vente.setEtapeValide(current.getEtapeValide());
                        vente.setEtapesValidations(current.getEtapesValidations());
                        if (documents != null ? documents.contains(current) : false) {
                            int idx_ = documents.indexOf(current);
                            documents.get(idx_).setEtapeValide(current.getEtapeValide());
                            documents.get(idx_).setStatut(current.getStatut());
                        }
                        getInfoMessage("Validation effectué avec succès !");
                        if (page.equals("V3")) {
                            update("blog_form_contenu_facture_vente");
                            update("blog_form_cout_facture_vente");
                            update("form_mensualite_facture_vente");
                        } else {
                            update("tabview_facture_vente:blog_form_contenu_facture_vente");
                            update("tabview_facture_vente:blog_form_cout_facture_vente");
                            update("tabview_facture_vente:form_mensualite_facture_vente");
                        }
                        update("data_livraison_facture_vente");
                        update("blog_commercial_vente");
                        return true;
                    }
                } else {
                    getErrorMessage("Impossible de continuer !");
                }
            }
        }
        return false;
    }

    public boolean annulEtapeFacture(YvsComDocVentes current, DocVente vente, YvsUsersAgence users, YvsWorkflowValidFactureVente etape, boolean lastEtape, String motif) {
        if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            if (motif != null ? motif.trim().isEmpty() : true) {
                getErrorMessage("Vous devez précisez le motif");
                return false;
            }
            if (current != null ? (current.getId() != null ? current.getId() < 1 : true) : true) {
                current = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{vente.getId()});
            }
            if (vente != null ? vente.getId() < 1 : true) {
                vente = UtilCom.buildBeanDocVente(current);
            }
            int idx = vente.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() != null) {
                    champ = new String[]{"etape", "facture"};
                    val = new Object[]{etape.getEtapeSuivante().getEtape(), current};
                    YvsWorkflowValidFactureVente y = (YvsWorkflowValidFactureVente) dao.loadOneByNameQueries("YvsWorkflowValidFactureVente.findByEtapeFacture", champ, val);
                    if (y != null ? (y.getId() > 0 ? y.getEtapeValid() : false) : false) {
                        getErrorMessage("Vous devez au préalable annuler l'étape suivante");
                        return false;
                    }
                }
                if (dao.isComptabilise(vente.getId(), Constantes.SCR_VENTE)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return false;
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        if (!w.unComptabiliserVente(current, false)) {
                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                            return false;
                        }
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

                vente.setStatut(current.getStatut());
                vente.setEtapeValide(current.getEtapeValide());
                if (documents != null ? documents.contains(current) : false) {
                    int idx_ = documents.indexOf(current);
                    documents.get(idx_).setEtapeValide(current.getEtapeValide());
                    documents.get(idx_).setStatut(current.getStatut());
                }
                getInfoMessage("Annulation effectué avec succès !");
                if (page.equals("V3")) {
                    update("blog_form_contenu_facture_vente");
                    update("blog_form_cout_facture_vente");
                    update("form_mensualite_facture_vente");
                } else {
                    update("tabview_facture_vente:blog_form_contenu_facture_vente");
                    update("tabview_facture_vente:blog_form_cout_facture_vente");
                    update("tabview_facture_vente:form_mensualite_facture_vente");
                }
                update("data_livraison_facture_vente");
                update("blog_commercial_vente");
                return true;
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        }
        return false;
    }

    private boolean headerChange(YvsComDocVentes d) {
        if (d.getClient().getId() != docVente.getClient().getId()) {
            return true;
        }
        if (d.getCategorieComptable().getId() != docVente.getCategorieComptable().getId()) {
            return true;
        }
        if (d.getModelReglement() != null ? d.getModelReglement().getId() != docVente.getModeReglement().getId() : false) {
            return true;
        }
        if (d.getAdresse() != null ? d.getAdresse().getId() != docVente.getAdresse().getId() : false) {
            return true;
        }
        if (d.getTelephone() != null ? !d.getTelephone().equals(docVente.getTelephone()) : false) {
            return true;
        }
        if ((d.getDepotLivrer() != null) ? d.getDepotLivrer().getId() != docVente.getDepot().getId() : false) {
            return true;
        }
        if ((d.getLivraisonAuto() != null) ? d.getLivraisonAuto() != docVente.isLivraisonAuto() : false) {
            return true;
        }
        if ((d.getNumeroExterne() != null) ? d.getNumeroExterne() != docVente.getNumeroExterne() : false) {
            return true;
        }
        return false;
    }

    public boolean validEtapeFacture(YvsWorkflowValidFactureVente etape) {
        //Modifier la facture si des élément ont changés
        int idx = documents.indexOf(selectDoc);
        if (idx >= 0) {
            if (headerChange(documents.get(idx))) {
                _saveNew(false);
            }
            return validEtapeFacture(selectDoc, docVente, currentUser, etape);
        }
        return false;
    }

    public void motifEtapeFacture(String motifEtape, boolean lastEtape) {
        this.motifEtape = motifEtape;
        this.lastEtape = lastEtape;
    }

    public void annulEtapeFacture(YvsWorkflowValidFactureVente etape, boolean lastEtape) {
        this.etape = etape;
        this.lastEtape = lastEtape;
        this.motifEtape = null;
        if (etape.getEtapeSuivante() == null) {
            if (dao.isComptabilise(docVente.getId(), Constantes.SCR_VENTE)) {
                openDialog("dlgConfirmAnnuleDoc");
                return;
            }
        }
        openDialog("dglMotifCancelEtape");
    }

    public boolean annulEtapeFacture() {
        return annulEtapeFacture(selectDoc, docVente, currentUser, etape, lastEtape, motifEtape);
    }

    public void cancelEtapeFacture(boolean force, boolean suspend) {
        if (docVente.getTypeDoc().equals(Constantes.TYPE_BCV)) {
            ManagedBonVente w = (ManagedBonVente) giveManagedBean(ManagedBonVente.class);
            if (w != null) {
                w.annulerOrder(selectDoc, docVente, Constantes.TYPE_FV, false);
                update("blog-annule_bcv_fv");
            }
        } else {
            if (dao.isComptabilise(docVente.getId(), Constantes.SCR_VENTE)) {
                if (dao.isComptabilise(docVente.getId(), Constantes.SCR_VENTE, false)) {
                    openDialog("dlgConfirmAnnulerDoc");
                } else {
                    getErrorMessage("Vous ne pouvez pas annuler ce document. Il est comptabilisé à partir de son journal de vente");
                }
                return;
            }
            cancelEtapeFacture(force, suspend, false);
        }
    }

    public void cancelEtapeFacture(boolean force, boolean suspend, boolean passe) {
        cancelEtapeFacture(selectDoc, docVente, force, suspend, passe);
    }

    public void cancelEtapeFacture(YvsComDocVentes selectDoc, DocVente docVente, boolean force, boolean suspend, boolean passe) {
        //vérifie le droit
        if (!autoriser("fv_cancel_doc_valid") && selectDoc.getStatut().equals(Constantes.ETAT_VALIDE)) {
            openNotAcces();
            return;
        }
        suspend = selectDoc.getStatut().equals(Constantes.ETAT_ANNULE) ? false : suspend;
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (!selectDoc.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
                List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                if (l != null ? l.isEmpty() : true || passe) {
                    //annule toute les validation acquise
                    int i = 0;
                    boolean update = force;
                    if (!force) {
                        for (YvsWorkflowValidFactureVente vm : docVente.getEtapesValidations()) {
                            //si on trouve une étape non valide (on ne peut annuler un ordre de docVente complètement valide)
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
                    if (dao.isComptabilise(selectDoc.getId(), Constantes.SCR_VENTE)) {
                        if (!autoriser("compta_od_annul_comptabilite")) {
                            getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                            return;
                        }
                        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                        if (w != null) {
                            if (!w.unComptabiliserVente(selectDoc, false)) {
                                getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                                return;
                            }
                        }
                    }
                    if (update) {
                        for (YvsWorkflowValidFactureVente vm : docVente.getEtapesValidations()) {
                            vm.setEtapeActive(false);
                            if (i == 0) {
                                vm.setEtapeActive(true);
                            }
                            vm.setAuthor(currentUser);
                            vm.setDateUpdate(new Date());
                            vm.setEtapeValid(false);
                            dao.update(vm);
                            i++;
                        }
                    } else if (!docVente.getEtapesValidations().isEmpty()) {
                        openDialog(suspend ? "dlgConfirmRefuserForcer" : "dlgConfirmCancelForcer");
                        return;
                    }
                    //désactive la docVente
                    if (changeStatut((suspend) ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE, docVente, selectDoc)) {
                        selectDoc.setCloturer(false);
                        selectDoc.setAnnulerBy(currentUser.getUsers());
                        selectDoc.setValiderBy(null);
                        selectDoc.setDateAnnuler(new Date());
                        selectDoc.setDateUpdate(new Date());
                        selectDoc.setDateCloturer(null);
                        selectDoc.setDateValider(null);
                        selectDoc.setCloturerBy(null);
                        selectDoc.setAuthor(currentUser);
                        selectDoc.setEtapeValide(0);
                        docVente.setEtapeValide(0);
                        if (cancelReglement) {
                            if (autoriser("encais_piece_espece")) {
                                long payer = 0;
                                List<YvsComptaCaissePieceVente> reglements = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFacture", new String[]{"facture"}, new Object[]{selectDoc});
                                for (YvsComptaCaissePieceVente p : reglements) {
                                    if (p.getStatutPiece().equals(Constantes.ETAT_REGLE.charAt(0)) && verifyCancelPieceCaisse(p.getDatePaiement())) {
                                        p.setStatutPiece(Constantes.ETAT_ATTENTE.charAt(0));
                                        p.setDateUpdate(new Date());
                                        p.setAuthor(currentUser);
                                        dao.update(p);
                                    }
                                    if (p.getStatutPiece().equals(Constantes.ETAT_REGLE.charAt(0))) {
                                        payer++;
                                    }
                                }
                                docVente.setReglements(new ArrayList<>(reglements));
                                String statut = Constantes.ETAT_ATTENTE;
                                if (payer > 0) {
                                    statut = Constantes.ETAT_ENCOURS;
                                    if (payer >= reglements.size()) {
                                        statut = Constantes.ETAT_REGLE;
                                    }
                                }
                                docVente.setStatutRegle(statut);
                                selectDoc.setStatutRegle(statut);
                            }
                        }
                        dao.update(selectDoc);
                    }
                } else {
                    for (YvsComDocVentes d : l) {
                        if (d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            getErrorMessage("Impossible d'annuler cet ordre car il possède un document déja valide");
                            return;
                        }
                    }
                    openDialog(suspend ? "dlgConfirmRefuser" : "dlgConfirmCancel");
                }
            } else {
                getErrorMessage("Ce document est deja livré");
            }
        }
        if (page.equals("V3")) {
            update("blog_form_contenu_facture_vente");
            update("blog_form_cout_facture_vente");
            update("form_mensualite_facture_vente");
        } else {
            update("tabview_facture_vente:blog_form_contenu_facture_vente");
            update("tabview_facture_vente:blog_form_cout_facture_vente");
            update("tabview_facture_vente:form_mensualite_facture_vente");
        }
        update("data_livraison_facture_vente");
        update("blog_commercial_vente");
    }
    /*
     FIN WORKFLOW
     */

    public void initChangeTrancheFacture() {
        ManagedTypeCreneau w = (ManagedTypeCreneau) giveManagedBean(ManagedTypeCreneau.class);
        if (w != null) {
            w.loadAllTranche();
        }
        update("data-list_tranches_fv");
    }

    public void changeTrancheFacture() {
        try {
            if (!selections.isEmpty()) {
                if (trancheChange != null ? trancheChange.getId() < 1 : true) {
                    getErrorMessage("Vous devez precisez la tranche horaire");
                    return;
                }
                for (YvsComDocVentes y : selections) {
                    y.setTrancheLivrer(new YvsGrhTrancheHoraire(trancheChange.getId()));
                    dao.update(y);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("ManagedFactureVente (changeTrancheFacture)", ex);
        }
    }

    public void changeDepotContenu() {
        ManagedLivraisonVente m = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
        if (m != null) {
            if (m.getSelectContenu() != null ? m.getSelectContenu().getId() > 0 : false) {
                YvsComContenuDocVente y = m.changeDepotContenu();
                int idx = docVente.getContenus().indexOf(y);
                if (idx > -1) {
                    docVente.getContenus().set(idx, y);
                }
                idx = docVente.getContenusSave().indexOf(y);
                if (idx > -1) {
                    docVente.getContenusSave().set(idx, y);
                }
            } else {
                YvsComContenuDocVente _y_;
                if (m.getSelectContenus() != null ? !m.getSelectContenus().isEmpty() : false) {
                    for (YvsComContenuDocVente y : m.getSelectContenus()) {
                        _y_ = m.changeDepotContenu(y, false);
                        int idx = docVente.getContenus().indexOf(_y_);
                        if (idx > -1) {
                            docVente.getContenus().set(idx, _y_);
                        }
                        idx = docVente.getContenusSave().indexOf(_y_);
                        if (idx > -1) {
                            docVente.getContenusSave().set(idx, _y_);
                        }
                    }
                } else {
                    for (YvsComContenuDocVente y : docVente.getContenus()) {
                        _y_ = m.changeDepotContenu(y, false);
                        int idx = docVente.getContenus().indexOf(_y_);
                        if (idx > -1) {
                            docVente.getContenus().set(idx, _y_);
                        }
                        idx = docVente.getContenusSave().indexOf(_y_);
                        if (idx > -1) {
                            docVente.getContenusSave().set(idx, _y_);
                        }
                    }
                }
                succes();
            }
        }
    }

    public void activeLivraisonAuto(YvsComDocVentes bean) {
        if (bean != null) {
            if (bean.getStatut().equals(Constantes.ETAT_VALIDE)) {
                getErrorMessage("Cette facture est déjà livrée");
                return;
            }
            if (bean.getDepotLivrer() != null ? bean.getDepotLivrer().getId() < 1 : true) {
                getErrorMessage("Vous devez precisez le depot de livraison prevu");
                return;
            }
            if (bean.getTrancheLivrer() != null ? bean.getTrancheLivrer().getId() < 1 : true) {
                getErrorMessage("Aucune tranche de livraison n'a été trouvé !");
                return;
            }
            bean.setLivraisonAuto(!bean.getLivraisonAuto());
            documents.get(documents.indexOf(bean)).setLivraisonAuto(bean.getLivraisonAuto());
            String rq = "UPDATE yvs_com_doc_ventes SET livraison_auto=" + bean.getLivraisonAuto() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
            succes();
            update("data_facture_vente");
        }
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete(pre + "numDoc", "reference", null);
        if (reference != null ? reference.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", reference + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "numDoc)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(" + pre + "numeroExterne)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        loadAllFacture(true);
    }

    public void addParamStatuts() {
        ParametreRequete p = new ParametreRequete(pre + "statut", "statut", null);
        if (statutContenu != null ? statutContenu.trim().length() > 0 : false) {
            p = new ParametreRequete(pre + "statut", "statut", statutContenu, "=", "AND");
        }
        paginator.addParam(p);
        p_contenu.addParam(p);
        loadAllFacture(true);
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

    public void addParamIdsContenus() {
        ParametreRequete p = new ParametreRequete("y.id", "ids_warning", null, "IN", "AND");
        if (Util.asString(idsContenu)) {
            List<Long> ids = new ArrayList<>();
            for (String id : idsContenu.split(",")) {
                ids.add(Util.isNumeric(id) ? Long.valueOf(id.trim()) : 0);
            }
            p = new ParametreRequete("y.id", "ids_warning", ids, "IN", "AND");
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

    public void addParamClient() {
        ParametreRequete p = new ParametreRequete("y.docVente.client", "client", null);
        if (clientF != null ? clientF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "client", clientF + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.nomClient)", "client", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.codeClient)", "code", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.nom)", "nom", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.prenom)", "prenom", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.tiers.codeTiers)", "codeT", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(CONCAT(y.docVente.client.nom, ' ', y.docVente.client.prenom))", "nom_pre", clientF.trim().toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamVendeur() {
        ParametreRequete p = new ParametreRequete("y.docVente.enteteDoc.creneau.users.codeUsers", "vendeur", null);
        if (vendeurF != null ? vendeurF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "vendeur", vendeurF + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.enteteDoc.creneau.users.nomUsers)", "vendeur", vendeurF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.enteteDoc.creneau.users.codeUsers)", "vendeur", vendeurF.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamDateContenu(SelectEvent ev) {
        findByDateContenu();
    }

    public void findByDateContenu() {
        ParametreRequete p = new ParametreRequete("y.docVente.enteteDoc.dateEntete", "date", null, "BETWEEN", "AND");
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

    public void findContenusByArticle() {
        docVente.getContenus().clear();
        if (Util.asString(articleContenu)) {
            for (YvsComContenuDocVente c : docVente.getContenusSave()) {
                if (c.getArticle().getRefArt().toUpperCase().contains(articleContenu.toUpperCase()) || c.getArticle().getDesignation().toUpperCase().contains(articleContenu.toUpperCase())) {
                    docVente.getContenus().add(c);
                }
            }
        } else {
            docVente.getContenus().addAll(docVente.getContenusSave());
        }
    }

    public void findContenusErrorComptabilite() {
        YvsBaseArticleCategorieComptable y;
        for (YvsComContenuDocVente c : docVente.getContenus()) {
            c.setErrorComptabilise(true);
            Long count = (Long) dao.loadObjectByNameQueries("YvsBaseArticleCategorieComptable.countByCategorieArticle", new String[]{"categorie", "article"}, new Object[]{selectDoc.getCategorieComptable(), c.getArticle()});
            if (count != null ? count > 0 : false) {
                if (count == 1) {
                    y = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries("YvsBaseArticleCategorieComptable.findByCategorieArticle", new String[]{"categorie", "article"}, new Object[]{selectDoc.getCategorieComptable(), c.getArticle()});
                    if (y != null ? (y.getId() > 0 ? (y.getCompte() != null ? y.getCompte().getId() > 0 : false) : false) : false) {
                        c.setErrorComptabilise(false);
                        c.setCompte(y.getCompte());
                    }
                } else {
                    c.setMessageError("Cet article est rattaché plusieurs fois la catégorie comptable de la facture");
                }
            } else {
                c.setMessageError("Cet article n'est pas bien paramètré avec cette catégorie comptable pour la comptabilité");
            }
        }
        if (page.equals("V3")) {
            update("data_contenu_facture_vente");
        } else {
            update("tabview_facture_vente:data_contenu_facture_vente");
        }
    }

    public void findAllContenusErrorComptabilite() {
        YvsBaseArticleCategorieComptable y;
        for (YvsComContenuDocVente c : all_contenus) {
            c.setErrorComptabilise(true);
            Long count = (Long) dao.loadObjectByNameQueries("YvsBaseArticleCategorieComptable.countByCategorieArticle", new String[]{"categorie", "article"}, new Object[]{c.getDocVente().getCategorieComptable(), c.getArticle()});
            if (count != null ? count > 0 : false) {
                if (count == 1) {
                    y = (YvsBaseArticleCategorieComptable) dao.loadOneByNameQueries("YvsBaseArticleCategorieComptable.findByCategorieArticle", new String[]{"categorie", "article"}, new Object[]{c.getDocVente().getCategorieComptable(), c.getArticle()});
                    if (y != null ? (y.getId() > 0 ? (y.getCompte() != null ? y.getCompte().getId() > 0 : false) : false) : false) {
                        c.setErrorComptabilise(false);
                        c.setCompte(y.getCompte());
                    }
                } else {
                    c.setMessageError("Cet article est rattaché plusieurs fois la catégorie comptable de la facture");
                }
            } else {
                c.setMessageError("Cet article n'est pas bien paramètré avec cette catégorie comptable pour la comptabilité");
            }
        }
        update("data_contenu_fv");
    }

    public void noChangeCategorie() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            CategorieComptable d = UtilCom.buildBeanCategorieComptable(selectDoc.getCategorieComptable());
            cloneObject(docVente.getCategorieComptable(), d);
            update("select_categorie_comptable_facture_vente");
        }
    }

    public void updatePrixArticle() {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            long newCategorie = docVente.getCategorieComptable() != null ? docVente.getCategorieComptable().getId() : 0;
            long oldCategorie = selectDoc.getCategorieComptable() != null ? selectDoc.getCategorieComptable().getId() : 0;
            if (selectDoc.getCategorieComptable() != null ? !Objects.equals(oldCategorie, newCategorie) : newCategorie > 0) {
                selectDoc.setCategorieComptable(UtilCom.buildCategorieComptable(docVente.getCategorieComptable(), currentUser, currentAgence.getSociete()));
                String query = "UPDATE yvs_com_doc_ventes SET categorie_comptable = ? WHERE id = ?";
                dao.requeteLibre(query, new Options[]{new Options(newCategorie, 1), new Options(docVente.getId(), 2)});
                for (int i = 0; i < docVente.getContenus().size(); i++) {
                    YvsComContenuDocVente c = docVente.getContenus().get(i);
                    for (YvsComTaxeContenuVente t : c.getTaxes()) {
                        dao.delete(t);
                    }
                    c.getTaxes().clear();
                    double oldTaxe = saveAllTaxe(c, docVente, selectDoc, oldCategorie, false);
                    double newTaxe = saveAllTaxe(c, docVente, selectDoc, newCategorie, true);
                    c.setPrixTotal(c.getPrixTotal() - oldTaxe + newTaxe);
                    dao.update(c);
                    int idx = selectDoc.getContenus().indexOf(c);
                    if (idx > -1) {
                        selectDoc.getContenus().set(idx, c);
                    }
                }
                int idx = documents.indexOf(selectDoc);
                if (idx > -1) {
                    documents.set(idx, selectDoc);
                }
                if (page.equals("V3")) {
                    update("data_contenu_facture_vente");
                } else {
                    update("tabview_facture_vente:data_contenu_facture_vente");
                }
            }
        }
    }

    public void verifyComptabilised(Boolean comptabilised) {
        initForm = true;
        loadAllFacture(true);

        if (comptabilised != null) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (w
                    != null) {
                List<YvsComDocVentes> list = new ArrayList<>();
                list.addAll(documents);
                for (YvsComDocVentes y : list) {
                    y.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_VENTE));
                    if (comptabilised ? !y.isComptabilised() : y.isComptabilised()) {
                        documents.remove(y);
                    }
                }
            }
        }
        update("data_facture_vente");
    }

    public void canLivrer() {
        for (YvsComContenuDocVente c : docVente.getContenus()) {
            c.setMessageError(controleStock(c.getArticle().getId(), c.getConditionnement().getId(), c.getDocVente().getDepotLivrer().getId(), 0L, c.getQuantite(), 0, "INSERT", "S", c.getDocVente().getEnteteDoc().getDateEntete(), (c.getLot() != null ? c.getLot().getId() : 0)));
        }
        docVente.setError(true);
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
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_VENTE));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_VENTE));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public void applyOrRemoveTaxe(YvsComTaxeContenuVente taxe) {
        selectedTaxe = taxe;
        if (taxe != null) {
            if (docVente.getId() > 0) {
                if (taxe.getId() != null ? taxe.getId() > 0 : false) {
                    openDialog("dlgConfirmdeleteTaxe");
                } else {
                    applyNewTaxe(taxe, taxe.getTaxe(), selectDoc);
                    //change la ligne de contenu                   
                    setMontantTotalDoc(docVente);
                    loadTaxesVente(taxe.getContenu());
                    succes();
                    update("chp_fv_net_a_payer");
                    if (page.equals("V3")) {
                        update("data_contenu_facture_vente");
                    } else {
                        update("tabview_facture_vente:data_contenu_facture_vente");
                    }
                }
            } else {
                getErrorMessage("La facture séléctionné n'est pas enregistré !");
            }
        } else {
            getErrorMessage("Aucune taxe n'a été selectionné !");
        }
    }

    public double applyNewTaxe(YvsComTaxeContenuVente c, YvsBaseTaxes t, YvsComDocVentes doc) {
        Double taxe = 0d;
        Double prixU = 0d;
        Double valeur = 0d;
        int cpt = 0;
        if (c != null && t != null) {
            if (hasDroitUpdateFacture(doc)) {
                if (c.getContenu().getArticle().getPuvTtc()) {
                    //récupère l'ensemble des taux de taxe
                    for (YvsComTaxeContenuVente ct : c.getContenu().getTaxes()) {
                        if (ct.getId() != null ? ct.getId() > 0 : false) {
                            taxe += ct.getTaxe().getTaux();
                        }
                    }
                    taxe += t.getTaux();
                    //PU HT
                    prixU = c.getContenu().getPrix() / (1 + (taxe / 100));

                } else {
                    prixU = c.getContenu().getPrix();
                }

                //modifie les autre taxes   
                valeur = c.getContenu().getQuantite() * prixU;
                for (YvsComTaxeContenuVente ct : c.getContenu().getTaxes()) {
                    if (ct.getId() != null ? (ct.getId() > 0 && ct.getTaxe().getTaux() > 0) : false) {
                        taxe = (((valeur - c.getContenu().getRemise()) * ct.getTaxe().getTaux()) / 100);
                        taxe = dao.arrondi(currentAgence.getSociete().getId(), taxe);
                        ct.setMontant(taxe);
                        ct.setDateUpdate(new Date());
                        ct.setAuthor(currentUser);
                        dao.update(ct);
                    }
                }
                taxe = (((valeur - c.getContenu().getRemise()) * t.getTaux()) / 100);
                taxe = dao.arrondi(currentAgence.getSociete().getId(), taxe);
                c.setMontant(taxe);
                c.setDateSave(new Date());
                c.setDateUpdate(new Date());
                c.setAuthor(currentUser);
                c = (YvsComTaxeContenuVente) dao.save1(c);
                int idx = docVente.getContenus().indexOf(c.getContenu());
                if (idx >= 0) {
                    docVente.getContenus().get(idx).setTaxes(dao.loadNameQueries("YvsComTaxeContenuVente.findByContenu", new String[]{"contenu"}, new Object[]{c.getContenu()}));
                    loadTaxesVente(docVente.getContenus().get(idx));
                }
            }
        }
        return taxe;
    }

    public void beginLivraison() {
        ManagedLivraisonVente w = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
        if (w != null) {
            w.setSelectContenus(selectContenus);
            w.beginLivraison(selectDoc, null, true);
        }
    }

    public void confirmDeleteTaxe() {
        if (selectedTaxe != null) {
            if (hasDroitUpdateFacture(selectDoc)) {
                dao.delete(selectedTaxe);
                selectedTaxe.setId(null);
                selectedTaxe.setMontant(0d);
                setMontantTotalDoc(docVente);
                int idx = selectedTaxe.getContenu().getTaxes().indexOf(selectedTaxe);
                if (idx >= 0) {
                    selectedTaxe.getContenu().getTaxes().set(idx, selectedTaxe);
                }
                if (selectedTaxe.getContenu().getArticle().getPuvTtc()) {
                    Double taxe = 0d;
                    Double prixU = 0d;
                    Double valeur = 0d;
                    //récupère l'ensemble des taux de taxe
                    for (YvsComTaxeContenuVente ct : contenu.getTaxes()) {
                        if (ct.getId() != null ? ct.getId() > 0 : false) {
                            taxe += ct.getTaxe().getTaux();
                        }
                    }
                    taxe += selectedTaxe.getTaxe().getTaux();
                    //PU HT
                    prixU = selectedTaxe.getContenu().getPrix() / (1 + (taxe / 100));
                    //modifie les autre taxes
                    for (YvsComTaxeContenuVente ct : selectedTaxe.getContenu().getTaxes()) {
                        if (ct.getId() != null ? ct.getId() > 0 : false) {
                            valeur = selectedTaxe.getContenu().getQuantite() * prixU;
                            taxe = (((valeur - selectedTaxe.getContenu().getRemise()) * ct.getTaxe().getTaux()) / 100);
                            taxe = dao.arrondi(currentAgence.getSociete().getId(), taxe);
                            ct.setMontant(taxe);
                            dao.update(ct);
                        }
                    }
                }
                idx = docVente.getContenus().indexOf(selectedTaxe.getContenu());
                if (idx >= 0) {
                    docVente.getContenus().get(idx).setTaxes(dao.loadNameQueries("YvsComTaxeContenuVente.findByContenu", new String[]{"contenu"}, new Object[]{selectedTaxe.getContenu()}));
                    loadTaxesVente(docVente.getContenus().get(idx));
                }
                succes();
                update("tbl_data_taxes");
                update("chp_fv_net_a_payer");
                if (page.equals("V3")) {
                    update("data_contenu_facture_vente");
                } else {
                    update("tabview_facture_vente:data_contenu_facture_vente");
                }
            }
        }
    }

    public boolean hasDroitUpdateFacture(YvsComDocVentes doc) {
        if (doc == null) {
            getErrorMessage("Impossible de modifier, ", "La facture n'est pas enregistré");
            return false;
        }
        if (doc.getId() != null ? doc.getId() <= 0 : true) {
            getErrorMessage("Impossible de modifier, ", "La facture n'est pas enregistré");
            return false;
        }
        boolean etape = false;
        if (doc.getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (dao.isComptabilise(doc.getId(), Constantes.SCR_VENTE)) {
                getErrorMessage("Impossible de modifier, ", "La facture de vente est dejà comptabilisé");
                return false;
            }
            if (!autoriser("fv_update_doc")) {
                getErrorMessage("Impossible de modifier, ", "La facture de vente est dejà validé");
                return false;
            }
        } else if (doc.getStatut().equals(Constantes.ETAT_ENCOURS)) {
            //teste le droit à une étape
            etape = (doc.getEtapesValidations().size() > 0) && !autoriser("fv_update_doc");
        }
        if (etape) {
            int prec = -1;
            for (YvsWorkflowValidFactureVente e : doc.getEtapesValidations()) {
                if (e.isEtapeActive()) {
                    if (prec > -1 && prec < doc.getEtapesValidations().size()) {
                        if (!asDroitValideEtape(doc.getEtapesValidations().get(prec).getEtape().getAutorisations(), currentUser.getUsers().getNiveauAcces())) {
                            openNotAcces();
                            return false;
                        }
                    }
                }
                prec++;
            }
        }
        return true;
    }

    public void uncomptabiliseFacture() {
        ManagedSaisiePiece service = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (service != null) {
            for (YvsComDocVentes y : selections) {
                service.unComptabiliserVente(y, false);
            }
            getInfoMessage("Action terminée avec succès");
        }
    }

    public void comptabiliseFacture() {
        ManagedSaisiePiece service = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (service != null) {
            for (YvsComDocVentes y : selections) {
                service.comptabiliserVente(y, false, false);
            }
            getInfoMessage("Action terminée avec succès");
        }
    }

    public void buildToSend() {
        try {
            if ((docVente.getDocumentLie() != null ? docVente.getDocumentLie().getId() > 0 : false) || docVente.getTypeDoc().equals(Constantes.TYPE_BCV)) {
                if (currentParamVente == null) {
                    currentParamVente = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
                }
                if (!currentParamVente.getLivreBcvWithoutPaye() && !docVente.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                    getErrorMessage("Cette commande doit etre reglée avant d'etre livrée");
                    return;
                }
            }
            contenusRequireLot.clear();
            setDateLivraison(docVente.getEnteteDoc().getDateEntete());
            if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                Boolean requiere_lot = false;
                String query = "SELECT requiere_lot FROM yvs_base_article_depot WHERE article = ? AND depot = ?";
                long depot = 0;
                for (YvsComContenuDocVente c : docVente.getContenus()) {
                    depot = docVente.getDepot().getId();
                    if (c.getDepoLivraisonPrevu() != null ? c.getDepoLivraisonPrevu().getId() > 0 : false) {
                        depot = c.getDepoLivraisonPrevu().getId();
                    }
                    requiere_lot = (Boolean) dao.loadObjectBySqlQuery(query, new Options[]{new Options(c.getArticle().getId(), 1), new Options(depot, 2)});
                    c.setRequiereLot(requiere_lot != null ? requiere_lot : false);
                    if (c.isRequiereLot()) {
                        ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
                        if (w != null) {
                            c.setLots(w.loadList(depot, c.getConditionnement().getId(), dateLivraison, c.getQuantite(), 0));
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
            update("date-livraison_fv");
            update("blog-contenu_fv_require_lot");
            openDialog("dlgConfirmSoumis");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onChangeDateLivraison() {
        try {
            ManagedLotReception w = (ManagedLotReception) giveManagedBean(ManagedLotReception.class);
            if (w != null) {
                for (YvsComContenuDocVente c : contenusRequireLot) {
                    c.setLots(w.loadList(docVente.getDepot().getId(), c.getConditionnement().getId(), dateLivraison, c.getQuantite(), 0));
                    double stock = 0;
                    for (YvsComLotReception y : c.getLots()) {
                        stock += y.getStock();
                    }
                    c.getArticle().setQuantite(stock);
                }
                update("blog-contenu_fv_require_lot");
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowLotEdit(RowEditEvent ev) {
        if (ev != null) {
            YvsComLotReception y = (YvsComLotReception) ev.getObject();
            if (y != null) {
//                onBlurQuantiteeLot(y, lot);
            }
        }
    }

    public void onBlurQuantiteeLot(YvsComContenuDocVente contenu, YvsComLotReception y) {
        try {
            double quantite = y.getQuantitee();
            if (quantite > y.getStock()) {
                y.setQuantitee(0);
                getErrorMessage("La quantitée entrée ne peut pas dépasser le stock total du lot");
                update("blog-contenu_fv_require_lot");
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
                update("blog-contenu_fv_require_lot");
                return;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("onBlurQuantiteeLot", ex);
        }
    }

    public double getMontantTotal(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            return setMontantTotalDoc(y, y.getContenus(), null, null);
        }
        return 0;
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
