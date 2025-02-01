/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
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
import yvs.commercial.ManagedCommerciaux;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.ManagedFactureAchat;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.creneau.ManagedTypeCreneau;
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
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseArticleDepot;
import yvs.entity.base.YvsBaseArticlePoint;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.YvsComCommercialPoint;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.achat.YvsComDocAchats;
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
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.commercial.vente.YvsComRemiseDocVente;
import yvs.entity.commercial.vente.YvsComTaxeContenuVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
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
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
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
public class ManagedFactureVente extends ManagedCommercial<DocVente, YvsComDocVentes> implements Serializable {

    private YvsComEnteteDocVente selectEntete;
    private EnteteDocVente entete = new EnteteDocVente();

    private DocVente docVente = new DocVente();
    private List<YvsComDocVentes> documents;
    private YvsComDocVentes selectDoc, docLie;
    private long bon;
    private boolean changeNumeroWhenChangeDate = true;
    private Date dateEntete = new Date();

    private YvsComParametreVente currentParam;

    private List<YvsComContenuDocVente> contenus_bcv, all_contenus;
    private YvsComContenuDocVente selectContenu;
    private ContenuDocVente contenu = new ContenuDocVente();
    private ContenuDocVente bonus = new ContenuDocVente();
    public PaginatorResult<YvsComContenuDocVente> p_contenu = new PaginatorResult<>();
    private boolean on_rabais, rememberChoixWithOutStock = false, valueContinueWithOutStock;
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
    private boolean checkAvance, onEncaisement;
    private boolean newMensualite;

    private RemiseDocVente remise = new RemiseDocVente();
    private List<YvsComRemiseDocVente> remisesFacture;
    private YvsComRemiseDocVente selectRemiseFacture;

    private String tabIds, tabIds_contenu, tabIds_cout, tabIds_mensualite, tabIds_remise, tabIds_commercial;
    private boolean correct = false, selectArt, isBon, listArt;
    private Date dateClean = new Date();

    //Parametre recherche
    private boolean paramDate = false, _first = true, toValideLoad = true;
    private Date dateDebut = new Date(), dateFin = new Date();
    private String statut = null, numBon, egaliteStatut = "!=", egaliteStatutL = "=", egaliteStatutR = "=", typeSearch, natureSearch;
    private String operateuClt = " LIKE ", operateurVend = " LIKE ", operateurRef = " LIKE ";
    private Boolean comptaSearch, withTiersSearch, autoLivreSearch;
    private long nbrComptaSearch;

    //Parametre recherche contenu
    private boolean dateContenu = false;
    private Date dateDebutContenu = new Date(), dateFinContenu = new Date();
    private String egaliteStatutLContenu = "=", statutLivreContenu;
    private String statutContenu, reference, article, articleContenu, pointvente, clientF, vendeurF;

    // Nombre d'element a afficher dans le selectOneMenu
    private int subLenght;

    private Date dateLivraison = new Date();
    private String statutLivraison = Constantes.ETAT_VALIDE;
    private List<YvsBaseDepots> depotsLivraison;
    private List<YvsUsers> vendeurs;

    private String motifEtape;
    YvsWorkflowValidFactureVente etape;
    private boolean lastEtape;

    @ManagedProperty(value = "#{composantsEditable}")
    private ComposantsEditable modelFormEditable;
    @ManagedProperty(value = "#{composantsVisible}")
    private ComposantsVisible modelFormCompVisible;
    @ManagedProperty(value = "#{composantsObligatoire}")
    private ComposantsObligatoire modelFormCompOblig;

    private boolean displayId;
    private boolean changeNumdocAuto = true;
    private boolean displayDetailClient = false;
    private long nbreFacture = 0;
    private boolean lotBL, lotPiece, lotFV, venteDirecte;
    private String statutLotBL = Constantes.ETAT_VALIDE, statutLotPiece = Constantes.ETAT_REGLE, statutLotFV = Constantes.ETAT_VALIDE;
    private List<String> statuts;

    public ManagedFactureVente() {
        contenus_bcv = new ArrayList<>();
        taxes = new ArrayList<>();
        documents = new ArrayList<>();
        all_contenus = new ArrayList<>();
        remisesFacture = new ArrayList<>();
        depotsLivraison = new ArrayList<>();
        tranches = new ArrayList<>();
        statuts = new ArrayList<>();
        vendeurs = new ArrayList<>();
        caissiers = new ArrayList<>();
    }

    public String getNatureSearch() {
        return natureSearch;
    }

    public void setNatureSearch(String natureSearch) {
        this.natureSearch = natureSearch;
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

    public int getSubLenght() {
        return subLenght;
    }

    public void setSubLenght(int subLenght) {
        this.subLenght = subLenght;
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

    public EnteteDocVente getEntete() {
        return entete;
    }

    public void setEntete(EnteteDocVente entete) {
        this.entete = entete;
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

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
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

    public void initView(boolean venteDirecte) {
        setClientDefaut(); //(Ok)
        this.venteDirecte = venteDirecte;
        initView();
        if (venteDirecte) {
            Long count = (Long) dao.loadObjectByNameQueries("YvsComDocVentes.countByVendeur", new String[]{"vendeur", "typeDoc"}, new Object[]{currentUser.getUsers(), Constantes.TYPE_FV});
            nbreFacture = count != null ? count : 0;
            docVente.setLivraisonAuto(true);
        }
    }

    @Override
    public void loadAll() {
        setSociete_(currentAgence.getSociete().getId());
        _loadAgence();  //(ok)
        initView(false);
        setCommercialDefaut(); //(Ok)
        if (statut != null ? statut.trim().length() < 1 : true) {
            this.egaliteStatut = "!=";
            this.statut = Constantes.ETAT_VALIDE;
            addParamStatut(false);
        }
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
            addParamToValide();
        }
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);

        statuts.add(Constantes.ETAT_EDITABLE);
        statuts.add(Constantes.ETAT_ENCOURS);
        paginator.addParam(new ParametreRequete("y.statut", "statut", statuts, "IN", "AND"));

        paramDate = true;
        dateDebut = debutWarning;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -getEcartModelWarning(modelWarning));
        dateFin = c.getTime();

        addParamDate2();
    }

    public void load(Boolean livraison) {

    }

    public void initView() {
        if (((docVente != null) ? (docVente.getClient().getId() < 1 && docVente.getCategorieComptable().getId() < 1) : true)) {
            docVente = new DocVente();
            docVente.setTypeDoc(Constantes.TYPE_FV);
            if (docVente.getDocumentLie() == null) {
                docVente.setDocumentLie(new DocVente());
            }
            numSearch_ = "";
            docVente.setEnteteDoc(new EnteteDocVente());
        }

        if (entete.getPoint() != null ? entete.getPoint().getId() < 1 : true) {
            if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauPoint() != null : false) {
                entete.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauPoint().getTranche()));
            }
            entete.setPoint(UtilCom.buildBeanPointVente(currentPoint));
            choosePoint();
        }
        if (docVente.getDepot() != null ? docVente.getDepot().getId() < 1 : true) {
            if (!depotsLivraison.isEmpty()) {
                docVente.setDepot(UtilCom.buildBeanDepot(depotsLivraison.get(0)));
            }
            if (!venteDirecte) {
                chooseDepot();
            }
        }
        if (docVente.getTranche() != null ? docVente.getTranche().getId() < 1 : true) {
            if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
                docVente.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
            }
        }
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service != null) {
            YvsBaseCaisse y = service.findByResponsable(currentUser.getUsers());
            reglement.setCaisse(UtilCompta.buildBeanCaisse(y));
            loadCaissiers(y);
        }
        if (entete != null ? entete.getId() > 0 : false) {
            ManagedPointVente w = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (w != null) {
                YvsBasePointVente y = new YvsBasePointVente(entete.getPoint().getId());
                int idx = w.getPointsvente().indexOf(y);
                if (idx < 0) {
                    y = (YvsBasePointVente) dao.loadOneByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{y.getId()});
                    w.getPointsvente().add(y);
                }
            }
        }
        if (currentParam == null) {
            currentParam = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        _first = true;
        if (reglement != null ? reglement.getId() < 1 : true) {
            reglement = new PieceTresorerie();
        }
        if (reglement.getMode() != null ? reglement.getMode().getId() < 1 : true) {
            reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
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

    private void initDroit() {
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_FV)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence", "agence", currentAgence, "=", "AND");
                paginator.addParam(p);
                break;
            case 3: { //charge tous les document des points de vente où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"}, new Object[]{currentUser.getUsers(), (Utilitaire.getIniTializeDate(new Date()).getTime()), Constantes.getPreviewDate(new Date())});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
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
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                paginator.addParam(p);
                break;
            }
            default:    //charge les document de l'utilisateur connecté dans les restriction de paramDate données
                p = new ParametreRequete("y.enteteDoc.creneau.users ", "users", currentUser.getUsers(), "=", "AND");
                paginator.addParam(p);
                break;
        }
    }

    public void addParamNonComptabiliser() {
        paginator.addParam(new ParametreRequete("CO.id", "idCompta", "X", " IS NULL", "AND"));
        loadAllFacture(true);
    }

    public void loadAllFacture(boolean avance) {
        //choisir les documents à charger
        boolean matchCompta = paginator.getParams().contains(new ParametreRequete("", "idCompta", "X"));
        initDroit();
        addParamType(false);
        String query = "YvsComDocVentes y JOIN FETCH y.client JOIN y.enteteDoc E INNER JOIN E.creneau C JOIN FETCH C.users "
                + " INNER JOIN C.creneauPoint CP JOIN FETCH CP.point";
        if (venteDirecte) {
            query = "YvsComDocVentes y JOIN FETCH y.client JOIN y.enteteDoc E";
        }
        if (matchCompta) {
            query = "YvsComDocVentes y JOIN FETCH y.client JOIN y.enteteDoc E INNER JOIN E.creneau C JOIN FETCH C.users "
                    + " INNER JOIN C.creneauPoint CP JOIN FETCH CP.point LEFT JOIN YvsComptaContentJournalFactureVente CO ON CO.facture=y";
        }
        documents = paginator.executeDynamicQuery("y", "y", query, "E.dateEntete DESC, y.heureDoc DESC, y.numDoc DESC", avance, initForm, (int) imax, dao);
//        documents = paginator.executeDynamicQuery(YvsComDocVentes.query(), new YvsComDocVentes(), "y.enteteDoc.dateEntete DESC, y.heureDoc DESC, y.numDoc DESC", avance, initForm, (int) imax, ds);
        subLenght = documents.size() > 10 ? 10 : documents.size();
        if (!venteDirecte) {
            if (documents != null ? documents.size() == 1 : false) {
                onSelectObject(documents.get(0));
                execute("collapseForm('facture_vente')");
            } else {
                execute("collapseList('facture_vente')");
            }
        }
        update("data_facture_vente");
    }

    public void parcoursInAllResult(boolean avancer) {
        parcoursInAllResult(avancer, true);
    }

    public void parcoursInAllResult(boolean avancer, boolean complet) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        if (!paginator.getParams().isEmpty()) {
            initDroit();
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
        natureSearch = Constantes.VENTE;
        paginator.addParam(new ParametreRequete("y.statutLivre", "statutLivre", Constantes.ETAT_LIVRE, "!=", "AND"));
        paginator.addParam(new ParametreRequete("y.nature", "nature", Constantes.SERVICE, "!=", "AND"));
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
        agence_ = 0;
        paginator.getParams().clear();
        idsSearch = "";
        _first = true;
        initForm = true;
        natureSearch = null;
        loadAllFacture(true);
        update("blog_entete_facture_vente");
    }

    private void loadOthersDetailDoc(YvsComDocVentes y) {
        loadRemise(y);
        loadTaxesVente(y);
//        docVente.setContenus(loadContenus(y));
//        docVente.setCouts(loadCouts(y));
        update("blog_form_contenu_facture_vente");
        update("blog_form_cout_facture_vente");
        update("form_mensualite_facture_vente");
        update("data_livraison_facture_vente");
        update("blog_commercial_vente");
        update("tabview_facture_vente:data_contenu_facture_vente");
    }

    private void loadRemise(YvsComDocVentes y) {
        nameQueri = "YvsComRemiseDocVente.findByDocVente";
        champ = new String[]{"docVente"};
        val = new Object[]{y};
        remisesFacture = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComRemiseDocVente r : remisesFacture) {
            setMontantRemise(r, docVente);
        }
        update("data_remise_vente");
    }

    private void loadTaxesVente(YvsComDocVentes y) {
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
        update("data_taxes_contenu_facture_vente");
    }

    private List<YvsComContenuDocVente> loadContenusStay(YvsComDocVentes y, String type) {
        List<YvsComContenuDocVente> list = new ArrayList<>();
        y.setInt_(false);
        nameQueri = "YvsComContenuDocVente.findByDocVente";
        champ = new String[]{"docVente"};
        val = new Object[]{y};
        List<YvsComContenuDocVente> contenus = dao.loadNameQueries(nameQueri, champ, val);
        String[] ch;
        Object[] v;
        Double qte;
        for (YvsComContenuDocVente c : contenus) {
            ch = new String[]{"parent", "typeDoc", "statut"};
            v = new Object[]{c, type, Constantes.ETAT_VALIDE};
            qte = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByTypeStatutParent", ch, v);
            if (c.getQuantite() > (qte != null ? qte : 0)) {
                c.setQuantite_(c.getQuantite());
                c.setQuantite(c.getQuantite() - (qte != null ? qte : 0));
                c.setPrixTotal(c.getPrix() * c.getQuantite());
                c.setParent(new YvsComContenuDocVente(c.getId()));
                list.add(c);
            }
        }
        return list;
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
            p_depot.addParam(new ParametreRequete("y.pointVente.agence", "agence", currentAgence, "=", "AND"));
        }
        if (Util.asString(code)) {
            ParametreRequete p = new ParametreRequete(null, "code", code.toUpperCase() + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.depot.code)", "code", code.trim().toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.depot.designation)", "designation", code.trim().toUpperCase() + "%", "LIKE", "OR"));
            p_depot.addParam(p);
        }
        p_depot.addParam(new ParametreRequete("y.actif", "actif", true, "=", "AND"));
        p_depot.addParam(new ParametreRequete("y.depot.actif", "actifDepot", true, "=", "AND"));

        depotsLivraison = p_depot.executeDynamicQuery("DISTINCT(y.depot)", "DISTINCT(y.depot)", "YvsBasePointVenteDepot y", "y.principal DESC , y.depot.code", true, true, 0, dao);
        if (depotsLivraison != null ? !depotsLivraison.isEmpty() : false) {
            if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
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
        docVente.setEnteteDoc(docVente.getId() > 0 ? docVente.getEnteteDoc() : entete);
        return docVente;
    }

    public EnteteDocVente recopieViewEntete() {
        entete.setUsers(entete.getId() > 0 ? entete.getUsers() : entete.getUsers());
        entete.setDateUpdate(new Date());
        return entete;
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
            if (bean.getDateLivraison() == null) {
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
//            String ref = genererReference(Constantes.TYPE_FV_NAME, selectEntete.getDateEntete(), entete.getPoint().getId(), Constantes.POINTVENTE);
//            if ((ref != null) ? ref.trim().equals("") : true) {
//                return false;
//            }
//            bean.setNumDoc(ref);
//        }
        bean.setEnteteDoc(bean.getId() > 0 ? bean.getEnteteDoc() : entete);
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
        boolean comptabilise = dao.isComptabilise(bean.getId(), Constantes.SCR_VENTE);
        if (comptabilise) {
            getErrorMessage("Cette facture est déja comptabilisé");
            return false;
        }
        if (!controleEcartVente(bean.getEnteteDoc().getUsers().getId(), bean.getEnteteDoc().getDateEntete(), true)) {
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
        boolean comptabilise = dao.isComptabilise(bean.getId(), Constantes.SCR_VENTE);
        if (comptabilise) {
            getErrorMessage("Cette facture est déja comptabilisé");
            return false;
        }
//        return writeInExercice(bean.getDateDoc()); 
        return true;
    }

    public boolean controleFiche(CommercialVente bean, boolean responsable, boolean msg) {
        return controleFiche(bean, responsable, false, msg);
    }

    public boolean controleFiche(CommercialVente bean, boolean responsable, boolean addTaux, boolean msg) {
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
        if (bean.getTaux() < 0 || bean.getTaux() > 100) {
            getErrorMessage("Vous devez entrer la bonne valeur du taux");
            if (msg) {
                return false;
            }
        }
        if (bean.getId() < 1 && responsable) { //pas encore affecté
            bean.setResponsable(true);
        }
        double taux = bean.getTaux();
        for (YvsComCommercialVente y : docVente.getCommerciaux()) {
            if (bean.getCommercial().getId() == y.getCommercial().getId() ? bean.getId() != y.getId() : false) {
                if (msg) {
                    getErrorMessage("Vous avez deja associé ce commercial");
                }
                return false;
            }
            if (bean.getCommercial().getId() != y.getCommercial().getId()) {
                taux += y.getTaux();
            }
            if (bean.getId() < 1 && responsable && y.getResponsable()) {//empêche le changement du responsable
                bean.setResponsable(false);
            }
        }
        if (addTaux) {
            if (bean.getTaux() <= 0) {
                if (msg) {
                    getErrorMessage("Vous devez entrer la bonne valeur du taux");
                }
                return false;
            }
        } else {
            if (bean.getId() < 1 && responsable) {
                bean.setTaux(100 - taux);
            }
        }
        if (!bean.getFacture().getClient().isSuiviComptable() && bean.isResponsable()) {
            if (bean.getCommercial().getTiers().getClients() != null ? !bean.getCommercial().getTiers().getClients().isEmpty() : false) {
                bean.getFacture().setTiers(new Client(bean.getCommercial().getTiers().getClients().get(0).getId()));
            }
        }
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
            if (!_saveNew(true)) {
                return false;
            }
            bean.setDocVente(docVente);
        }
        if ((bean.getArticle() != null) ? bean.getArticle().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier l'article");
            return false;
        }
        if ((bean.getConditionnement() != null) ? bean.getConditionnement().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le conditionnement");
            return false;
        }
        if (bean.getQuantite() <= 0) {
            getErrorMessage("Vous devez entrer la quantitée");
            return false;
        }
        if (bean.getPrix() <= 0) {
            getErrorMessage("Vous devez entrer le prix de vente");
            return false;
        }
        boolean controlAcces = false;
        if (!continuSave) {
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
        if (controlAcces) {
            if ((bean.getPrix() - bean.getRabais()) < bean.getPrixMin()) {
                if (!autoriser("fv_can_reduce_prix") || entete.getPoint().isPrixMinStrict()) {
                    getErrorMessage("Vous ne pouvez réduire le prix de vente de cet article au delà de prix minimale !", "A cause d'une restriction d'accès ou d'une limitation au niveau du point de vente");
                    return false;
                }
            }
        }
        if (!currentParam.getSellLowerPr() ? bean.getPrix() < bean.getPr() : false) {
            getErrorMessage("Vous ne pouvez réduire le prix de vente de cet article au delà de prix de revient !");
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
        docVente.setEtapesValidations(ordonneEtapes(bean.getEtapesValidations()));
        bon = 0;
        isBon = (bean.getDocumentLie() != null ? bean.getDocumentLie().getId() > 0 : false);
        if (isBon) {
            bon = bean.getDocumentLie().getId();
            contenus_bcv = loadContenusStay(new YvsComDocVentes(bon), Constantes.TYPE_FV);
            update("data_contenu_bcv_fv");
        } else {
            checkAvance = false;
        }
        if (bean.getAdresse() != null ? bean.getAdresse().getId() > 0 : false) {
            Dictionnaire ville = bean.getAdresse().getParent();
            cloneObject(docVente.getVille(), ville);
            chooseVille();
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
        update("chp_fv_net_a_payer");
        update("value_ttc_facture");
        update("value_reste_a_payer_facture");
    }

    public void populateViewEntete(EnteteDocVente bean, boolean complet) {
        cloneObject(entete, bean);
//        if (complet) {
//            setMontantEntete(null);
//        }
        entete.setTranche(bean.getTranchePoint());
    }

    public void populateViewContenu(ContenuDocVente bean) {
        if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
            bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, docVente.getDepot().getId(), 0, 0, entete.getDateEntete(), bean.getConditionnement().getId(), bean.getLot().getId()));
        } else {
            bean.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, 0, currentAgence.getId(), 0, entete.getDateEntete(), bean.getConditionnement().getId(), bean.getLot().getId()));
        }
        bean.getArticle().setPuv(dao.getPuv(bean.getArticle().getId(), bean.getQuantite(), bean.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), bean.getConditionnement().getId()));
        bean.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));
        selectArt = true;
        cloneObject(contenu, bean);

        YvsBaseArticles t = new YvsBaseArticles(bean.getArticle().getId());
        contenu.setPrixMin(dao.getPuvMin(t.getId(), bean.getQuantite(), bean.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), bean.getConditionnement().getId()));
        champ = new String[]{"article", "point"};
        val = new Object[]{t, new YvsBasePointVente(entete.getPoint().getId())};
        List<YvsBaseArticlePoint> la = dao.loadNameQueries("YvsBaseArticlePoint.findByArticlePoint", champ, val, 0, 1);
        if (la != null ? !la.isEmpty() : false) {
            contenu.getArticle().setChangePrix(la.get(0).getChangePrix());
        }
        if (contenu.getPrix() < 1) {
            contenu.getArticle().setChangePrix(true);
        }
        update("desc_article_facture_vente");
        update("tabview_facture_vente:desc_article_facture_vente");
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
        docVente = new DocVente();
        if (!depotsLivraison.isEmpty()) {
            docVente.setDepot(UtilCom.buildBeanDepot(depotsLivraison.get(0)));
        }
        docVente.setDateLivraisonPrevu(entete.getDateEntete());
        docVente.setDateLivraison(entete.getDateEntete());
        if (venteDirecte) {
            docVente.setLivraisonAuto(true);
        }
        docVente.setValidationReglement(entete != null ? entete.getPoint() != null ? entete.getPoint().isValidationReglement() : false : false);
        chooseDepot();
        docVente.getContenus().clear();
        contenus_bcv.clear();
        selectDoc = new YvsComDocVentes();
        selectDoc.setEnteteDoc(selectEntete);
        tabIds = "";
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

    public void resetFicheEntete() {
        if (!autoriser("fv_update_header")) {
            openNotAcces();
            return;
        }
        entete = new EnteteDocVente();
        if (selectEntete != null && !venteDirecte) {
            ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (m != null) {
                m.loadPointVenteByDroit(true, true);
            }
        }
        selectEntete = new YvsComEnteteDocVente();
        entete.setDateEntete(new Date());
        entete.setUsers(UtilUsers.buildBeanUsers(currentUser.getUsers()));
        if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauPoint() != null : false) {
            entete.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauPoint().getTranche()));
        }
        entete.setPoint(UtilCom.buildBeanPointVente(currentPoint));
        vendeurs.clear();
        choosePoint();
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
        update("blog_form_contenu_facture_vente");
        update("blog_form_cout_facture_vente");
        update("form_mensualite_facture_vente");
        update("data_livraison_facture_vente");
        update("blog_commercial_vente");
    }

    public void resetFicheContenu() {
        contenu = new ContenuDocVente();
        contenu.setQuantite(1);
        bonus = new ContenuDocVente();

        selectContenu = new YvsComContenuDocVente();
        selectArt = false;
        listArt = false;
        on_rabais = false;

        tabIds_contenu = "";
        update("form_contenu_facture_vente");
        update("desc_article_facture_vente");
        update("tabview_facture_vente:form_contenu_facture_vente");
        update("tabview_facture_vente:desc_article_facture_vente");
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
        update("blog_form_cout_facture_vente");
    }

    @Override
    public boolean saveNew() {
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
                    if (!autoriser("fv_save_doc")) {
                        openNotAcces();
                        return false;
                    }
                    List<YvsWorkflowEtapeValidation> etapes = getAllEtapeValidation();
                    docVente.setEtapeTotal(etapes != null ? etapes.size() : 0);
                    selectDoc.setEtapeTotal(docVente.getEtapeTotal());
                    selectDoc.setId(null);
                    selectDoc = (YvsComDocVentes) dao.save1(selectDoc);
                    docVente.setId(selectDoc.getId());
                    selectDoc.setEtapesValidations(saveEtapesValidation(selectDoc, etapes));
                    docVente.setEtapesValidations(new ArrayList<>(selectDoc.getEtapesValidations()));
                    documents.add(0, selectDoc);
                    if (documents.size() > imax) {
                        documents.remove(documents.size() - 1);
                    }
                    nbreFacture++;
                    saveCurrentCommercial(false);
                } else {
                    if (!autoriser("fv_update_doc") || (!autoriser("fv_update_header") && (entete.getDateEntete().compareTo(selectEntete.getDateEntete()) < 0))) {
                        openNotAcces();
                        return false;
                    }
                    dao.update(selectDoc);
                    //si la facture a déjà un contenu, et que le client ou la catégorie comptable a changé, on recalcule les éléments du contenu
                    if (documents.contains(selectDoc)) {
                        YvsComDocVentes old = documents.get(documents.indexOf(selectDoc));
                        if (!old.getClient().equals(selectDoc.getClient()) || !Objects.equals(old.getCategorieComptable(), selectDoc.getCategorieComptable())) {
                            for (YvsComContenuDocVente co : docVente.getContenus()) {
                                ContenuDocVente c = UtilCom.buildBeanContenuDocVente(co);
                                c.setUpdate(false);
                                findPrixArticle(c, true);
                                YvsComContenuDocVente cc = UtilCom.buildContenuDocVente(c, currentUser);
                                dao.update(cc);
//                                docVente.getContenus().set(docVente.getContenus().indexOf(co), cc);
                            }
                            if (!docVente.getContenus().isEmpty()) {
                                update("data_contenu_facture_vente");
                                update("tabview_facture_vente:data_contenu_facture_vente");
                                setMontantEntete(docVente);
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
                setClientDefaut();
                update("form_entete_facture_vente");
                update("blog_choose_commande_fv");
                update("data_facture_vente");
                update("data_facture_vente_hist");
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

    public void saveNewContenu(boolean continuSave) {
        try {
            on_rabais = false;
            contenu.setDepoLivraisonPrevu((contenu.getDepoLivraisonPrevu().getId() <= 0) ? docVente.getDepot() : contenu.getDepoLivraisonPrevu());
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
                setMontantEntete(docVente);
                loadRemise(selectContenu.getDocVente());
                loadTaxesVente(selectContenu.getDocVente());
                succes();
                resetFicheContenu();
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
                update("data_facture_vente");
                update("blog_form_montant_doc");
                update("tabview_facture_vente:data_contenu_facture_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Lymytz Error  >>> ", ex);
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
                selectEntete = w.saveNewEntete(recopieViewEntete(), docVente, false);
            }
            if (selectEntete != null ? (selectEntete.getId() > 0 && entete.getId() < 1) : false) {
                entete = UtilCom.buildBeanEnteteDocVente(selectEntete);
                entete.setTranche(entete.getTranchePoint());
            }
            if (selectEntete != null ? selectEntete.getId() > 0 : false) {
                entete.setId(selectEntete.getId());
                entete.setUpdate(true);
                entete.setNew_(true);
                docVente.setEnteteDoc(entete);
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
        List<Integer> ids = decomposeSelection(tabIds);
        if (ids != null ? !ids.isEmpty() : false) {
            for (Integer index : ids) {
                updateEntete(documents.get(index), ids.size() == 1);
            }
            if (ids.size() > 1) {
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

    public void saveNewCommercial(CommercialVente bean, boolean responsable, boolean addTaux, boolean msg) {
        try {
            if (controleFiche(bean, responsable, addTaux, msg)) {
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
                update("blog_commercial_vente");
            }
        } catch (Exception ex) {
            if (msg) {
                getErrorMessage("Operation Impossible !");
            }
            getException("Error  : " + ex.getMessage(), ex);
        }
    }

    public void saveNewCommercial() {
        saveNewCommercial(recopieViewCommercial(docVente), true, false, true);
    }

    public void saveCurrentCommercial(boolean msg) {
        if (selectEntete != null ? (selectEntete.getId() != null ? selectEntete.getId() > 0 : false) : false) {
            if (selectEntete.getCreneau() != null ? (selectEntete.getCreneau().getId() != null ? selectEntete.getCreneau().getId() > 0 : false) : false) {
                char commissionFor = 'C';
                YvsBasePointVente pv = null;
                YvsComCreneauPoint cr = selectEntete.getCreneau().getCreneauPoint();
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
                    YvsComComerciale y = (YvsComComerciale) dao.loadOneByNameQueries("YvsComComerciale.findByUser", new String[]{"user"}, new Object[]{selectEntete.getCreneau().getUsers()});
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        CommercialVente bean = new CommercialVente();
                        bean.setFacture(docVente);
                        bean.setTaux(100);
                        bean.setResponsable(true);
                        bean.setCommercial(UtilCom.buildBeanCommerciales(y));
                        saveNewCommercial(bean, true, false, msg);
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
                            saveNewCommercial(bean, true, true, msg);
                        }
                    }
                }
            } else if (msg) {
                getErrorMessage("Vous devez enregistrer le journal de vente");
            }
        } else if (msg) {
            getErrorMessage("Vous devez enregistrer le journal de vente");
        }
    }

    public void saveNewCout() {
        try {
            CoutSupDoc bean = recopieViewCoutSupDoc();
            if (controleFicheCout(bean)) {
                selectCout = buildCoutSupDocVente(bean);
                if (!bean.isUpdate()) {
                    selectCout.setId(null);
                    selectCout = (YvsComCoutSupDocVente) dao.save1(selectCout);
                    cout.setId(selectCout.getId());
                } else {
                    dao.update(selectCout);
                }
                int idx = docVente.getCouts().indexOf(selectCout);
                double montant = (selectCout.getTypeCout().getAugmentation() ? selectCout.getMontant() : -selectCout.getMontant());
                if (idx > -1) {
                    YvsComCoutSupDocVente c = docVente.getCouts().get(docVente.getCouts().indexOf(selectCout));
                    docVente.getCouts().set(idx, selectCout);
                    docVente.setMontantCS(docVente.getMontantCS() + montant + (c.getTypeCout().getAugmentation() ? -c.getMontant() : c.getMontant()));

                } else {
                    docVente.getCouts().add(0, selectCout);
                    docVente.setMontantCS(docVente.getMontantCS() + montant);
                }
                resetFicheCout();
                docVente.setNbreCout(docVente.getCouts().size());
                update("btn-open_service");
                succes();
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
                update("blog_form_montant_doc_fv");
                update("blog_form_cout_facture_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Operation Impossible !");
            getException("Error  : " + ex.getMessage(), ex);
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
//                if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) && bean.getCaisse().getId() <= 0) {
//
//                }
                if (bean.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                    if (currentParam != null ? !currentParam.getPaieWithoutValide() ? !docVente.getStatut().equals(Constantes.ETAT_VALIDE) : false : false) {
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
                docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
                update("data_contenu_facture_vente");
                update("tabview_facture_vente:data_contenu_facture_vente");
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
            docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
            update("tabview_facture_vente:data_contenu_facture_vente");
            update("data_contenu_facture_vente");
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
            docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
            setMontantEntete(docVente);
            loadRemise(selectDoc);
            update("chp_fv_net_a_payer");
            update("value_ttc_facture");
            update("value_reste_a_payer_facture");
            update("tabview_facture_vente:data_contenu_facture_vente");
            update("data_contenu_facture_vente");
            succes();
        } else {
            getErrorMessage("Vous devez selectionner un contenu");
        }
    }

    public void addRemiseContenu(YvsComContenuDocVente y) {
        ManagedRemise w = (ManagedRemise) giveManagedBean(ManagedRemise.class);
        if (w != null) {
            w.loadAllRemise();
            update("value-select_remise_fv");
        }
        selectContenu = y;
        montant_remise = y.getRemise();
        taux_remise = y.getTauxRemise();
        update("txt_remise_contenu_facture_vente");
    }

    public void addRemiseContenu() {
        if (!autoriser("fv_apply_remise")) {
            openNotAcces();
            return;
        }
        if (docVente.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
            getErrorMessage("Impossible d'appliquer une remise sur une facture déjà Réglé !");
            return;
        }
        if (docVente.isPropagerRemise()) {
            if (remiseContenu > 0) {
                ManagedRemise m = (ManagedRemise) giveManagedBean(ManagedRemise.class);
                if (m != null) {
                    int idx = m.getRemises().indexOf(new YvsComRemise(remiseContenu));
                    if (idx > -1) {
                        YvsComRemise y = m.getRemises().get(idx);
                        for (YvsComContenuDocVente c : docVente.getContenus()) {
                            double remise = getMontantRemise(y, c);
                            c.setRemise(remise);
                            c.setAuthor(currentUser);
                            c.setPrixTotal(prixTotal(c));
                            c.setDateUpdate(new Date());
                            dao.update(c);
                        }
                        setMontantEntete(docVente);
                        loadRemise(selectDoc);
                        update("chp_fv_net_a_payer");
                        update("value_ttc_facture");
                        update("value_reste_a_payer_facture");
                        update("tabview_facture_vente:data_contenu_facture_vente");
                        update("data_contenu_facture_vente");
                        succes();
                    }
                }
            }
        } else if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            selectContenu.setAuthor(currentUser);
            selectContenu.setDateUpdate(new Date());
            selectContenu.setPrixTotal(selectContenu.getPrixTotal() + (selectContenu.getRemise()) - montant_remise);
            selectContenu.setRemise(montant_remise);
            dao.update(selectContenu);
            docVente.getContenus().set(docVente.getContenus().indexOf(selectContenu), selectContenu);
            setMontantEntete(docVente);
            loadRemise(selectDoc);
            update("chp_fv_net_a_payer");
            update("value_ttc_facture");
            update("value_reste_a_payer_facture");
            update("tabview_facture_vente:data_contenu_facture_vente");
            update("data_contenu_facture_vente");
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
            update("tabview_facture_vente:data_contenu_facture_vente");
            update("data_contenu_facture_vente");
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
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComDocVentes bean = documents.get(documents.indexOf(new YvsComDocVentes(id)));
                    if (!_controleFiche_(bean)) {
                        return;
                    }
                    dao.delete(bean);
                    documents.remove(bean);
                    if (id == docVente.getId()) {
                        resetFiche();
                        update("blog_form_facture_vente");
                    }
                    nbreFacture--;
                }
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
                    dao.delete(bean);
                    docVente.getContenus().remove(bean);
                    docVente.getContenusSave().remove(bean);
                    if (id == contenu.getId()) {
                        resetFicheContenu();
                        update("tabview_facture_vente:form_contenu_facture_vente");
                        update("form_contenu_facture_vente");
                    }
                }
                setMontantEntete(docVente);
                loadRemise(selectDoc);
                succes();
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
                update("tabview_facture_vente:data_contenu_facture_vente");
                update("data_contenu_facture_vente");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanContenu_(YvsComContenuDocVente y) {
        selectContenu = y;
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

    public void deleteBeanContenu_() {
        try {
            if (selectContenu != null) {
                if (!_controleFiche_(selectDoc)) {
                    return;
                }
                dao.delete(selectContenu);
                docVente.getContenus().remove(selectContenu);
                if (selectContenu.getId() == contenu.getId()) {
                    resetFicheContenu();
                    update("tabview_facture_vente:form_contenu_facture_vente");
                    update("form_contenu_facture_vente");
                }
                setMontantEntete(docVente);
                loadRemise(selectDoc);
                succes();
                update("chp_fv_net_a_payer");
                update("value_ttc_facture");
                update("value_reste_a_payer_facture");
                update("tabview_facture_vente:data_contenu_facture_vente");
                update("data_contenu_facture_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
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
                    dao.delete(new YvsComRemiseDocVente(bean.getId()));
                    remisesFacture.remove(bean);
                }
                setMontantEntete(docVente);
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
                dao.delete(selectRemiseFacture);
                remisesFacture.remove(selectRemiseFacture);

                setMontantEntete(docVente);
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
                        dao.delete(new YvsComptaCaissePieceVente(bean.getId()));
                    }
                    docVente.getReglements().remove(bean);
                }
                succes();
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
                    dao.delete(selectReglement);
                }
                docVente.getReglements().remove(selectReglement);
                docVente.setNbreReglement(docVente.getReglements().size());
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
                    dao.delete(bean);
                    docVente.getCouts().remove(bean);
                    docVente.setMontantCS(docVente.getMontantCS() - bean.getMontant());
                    if (cout.getId() == id) {
                        resetFicheCout();
                    }
                }
                succes();
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
                docVente.setMontantCS(docVente.getMontantCS() - selectCout.getMontant());
                dao.delete(selectCout);
                docVente.getCouts().remove(selectCout);
                if (cout.getId() == selectCout.getId()) {
                    resetFicheCout();
                }
                docVente.setNbreCout(docVente.getCouts().size());
                update("btn-open_service");
                succes();
                update("blog_form_montant_doc_fv");
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
        if (selectDoc != null) {
            ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
            if (w != null) {
                w.creance(selectDoc.getClient());
            }
            selectDoc.getClient().setNbrFactureImpayee((Long) dao.loadObjectByNameQueries("YvsComDocVentes.countFactureImpayeByClient", new String[]{"client"}, new Object[]{selectDoc.getClient()}));
            displayDetailClient = true;
        }
    }

    public void onSelectObject(YvsComDocVentes y, boolean complet) {
        List<YvsComContenuDocVente> l = dao.loadNameQueries("YvsComContenuDocVente.findByFacture", new String[]{"docVente"}, new Object[]{y});
        if (l != null ? !l.isEmpty() : false) {
            y = l.get(0).getDocVente();
            y.setContenus(l);
        }
        y.setEtapesValidations(dao.loadNameQueries("YvsWorkflowValidFactureVente.findByFacture", new String[]{"facture"}, new Object[]{y}));
        selectDoc = y;
        entete_ = selectDoc.getEnteteDoc().getId();
        selectEntete = selectDoc.getEnteteDoc();
        if (venteDirecte) {
            complet = false;
        }
        if (complet) {
            loadOthersDetailDoc(selectDoc);
        }
        equilibre(selectDoc);
        populateView(UtilCom.buildSimpleBeanDocVente(selectDoc));
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
        if (y.getDepotLivrer() != null ? y.getDepotLivrer().getId() > 0 : false) {
            loadAllTranche(y.getDepotLivrer(), docVente.getDateLivraisonPrevu());
        }
        populateViewEntete(docVente.getEnteteDoc(), complet);
        ManagedStockArticle s = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
        if (s != null) {
            s.setEntityPoint(selectEntete.getCreneau().getCreneauPoint().getPoint());
        }
        chooseEntete(selectEntete);
        if (!vendeurs.contains(selectEntete.getCreneau().getUsers())) {
            vendeurs.add(selectEntete.getCreneau().getUsers());
        }
        resetSubFiche();
        update("save_entete_facture_vente");
        update("blog_form_facture_vente");
        if (venteDirecte) {
            update("blog_btn_action_facture");
            execute("slideZoneOnClick(null, 'zone_fv_net_a_payer');");
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
            switch (docLie.getTypeDoc()) {
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        s.setSelectDoc(docLie);
                        s.deleteBean_();
                    }
                    break;
                }
                case Constantes.TYPE_BRV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        s.setSelectDoc(docLie);
                        s.deleteBean_();
                    }
                    break;
                }
                case Constantes.TYPE_FAV: {
                    ManagedBonAvoirVente s = (ManagedBonAvoirVente) giveManagedBean(ManagedBonAvoirVente.class);
                    if (s != null) {
                        s.setSelectDoc(docLie);
                        s.deleteBean_();
                    }
                    break;
                }
            }
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

    public void onSelectDistantLivraison(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
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

    public void onValideDistantLivraison(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_BLV: {
                    ManagedLivraisonVente s = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                    if (s != null) {
                        if (s.validerOrder(y, false, false, true, null, false)) {
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
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
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
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
                        selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
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
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
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
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
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

    public void onAnnulerDistantLivraison(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getTypeDoc()) {
                case Constantes.TYPE_FV: {
                    if (annulerOrder(y, null, true)) {
                        selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
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
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
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
                            selectDoc = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{selectDoc.getId()});
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

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
            onSelectObject(y);
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
            bean.setArticle((YvsBaseArticles) dao.loadOneByNameQueries("YvsBaseArticles.findById", new String[]{"id"}, new Object[]{bean.getArticle().getId()}));
            populateViewContenu(UtilCom.buildBeanContenuDocVente(bean));
            update("tabview_facture_vente:form_contenu_facture_vente");
            update("tabview_facture_vente:desc_article_facture_vente");
            update("form_contenu_facture_vente");
            update("desc_article_facture_vente");
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
            update("tabview_facture_vente:form_contenu_facture_vente");
            update("form_contenu_facture_vente");
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
                c.getArticle().setStock(dao.stocks(bean.getArticle().getId(), 0, 0, 0, 0, entete.getDateEntete(), bean.getConditionnement().getId(), bean.getLot().getId()));
                c.getArticle().setPua(dao.getPua(bean.getArticle().getId(), 0));

                c.setDocVente(null);
                c.setUpdate(false);
                c.setParent(new ContenuDocVente(bean.getId()));
                cloneObject(contenu, c);

                update("tabview_facture_vente:form_contenu_facture_vente");
                update("tabview_facture_vente:desc_article_facture_vente");
                update("form_contenu_facture_vente");
                update("desc_article_facture_vente");
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
            update("blog_form_contenu_facture_vente");
        }
    }

    public void loadOnViewPointVente(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBasePointVente y = (YvsBasePointVente) ev.getObject();
            entete.setPoint(UtilCom.buildBeanPointVente(y));
            choosePoint();
            update("save_entete_facture_vente");
        }
    }

    public void loadOnViewDepot(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseDepots y = (YvsBaseDepots) ev.getObject();
            docVente.setDepot(UtilCom.buildSimpleBeanDepot(y));
            chooseDepot();
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
                    setMontantEntete(docVente);

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
        champ = new String[]{"article", "point"};
        val = new Object[]{new YvsBaseArticles(art.getId()), new YvsBasePointVente(entete.getPoint().getId())};
        YvsBaseArticlePoint la = (YvsBaseArticlePoint) dao.loadOneByNameQueries("YvsBaseArticlePoint.findByArticlePoint", champ, val);
        if (la != null) {
            contenu.getArticle().setChangePrix(la.getChangePrix());
            contenu.getArticle().setPuvTtc(la.getArticle().getPuvTtc());
            contenu.getArticle().setPuvMin(la.getArticle().getPrixMin());
            contenu.setConditionnement(UtilProd.buildBeanConditionnement(la.getConditionementVente()));
        }
        if ((contenu.getConditionnement() != null) ? contenu.getConditionnement().getId() <= 0 : true) {
            //récupère le conditionnement de vente au niveau de l'article
            for (YvsBaseConditionnement cd : art.getConditionnements()) {
                if (cd.getByVente() && cd.getDefaut()) {
                    contenu.setConditionnement(UtilProd.buildBeanConditionnement(cd));
                    if (cd.getPrixMin() != 0) {
                        contenu.getArticle().setPuvMin(cd.getPrixMin());
                    }
                    break;
                }
            }
        }
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
    }

    private void loadInfosAfterChooseArticle(ContenuDocVente contenu, Articles art) {
        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() > 0 : false) {
            if (docVente.getDepot() != null ? docVente.getDepot().getId() > 0 : false) {
                art.setStock(dao.stocks(art.getId(), 0, docVente.getDepot().getId(), 0, 0, entete.getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
            } else {
                art.setStock(dao.stocks(art.getId(), 0, 0, currentAgence.getId(), 0, entete.getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
            }
            art.setPuv(dao.getPuv(art.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId()));
            contenu.setPrixMin(dao.getPuvMin(art.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId()));
            art.setPua(dao.getPua(art.getId(), 0));
            if (!contenu.isUpdate()) {
                contenu.setPrix(art.getPuv());
            }
            contenu.setPr(dao.getPr(art.getId(), entete.getDepot().getId(), 0, entete.getDateEntete(), contenu.getConditionnement().getId()));
            double rabais = returnRabais(contenu.getConditionnement().getId(), new YvsBasePointVente(entete.getPoint().getId()), entete.getDateEntete(), contenu.getQuantite(), contenu.getPrix());
            contenu.setRabais(rabais);
        }
    }

    public ContenuDocVente findPrixArticle(ContenuDocVente c, boolean findPrix) {
        if (!isBon) {
            if ((docVente.getClient() != null) ? docVente.getClient().getId() > 0 : false) {
                if (!c.isUpdate() ? findPrix : false) {
                    c.setPrix(dao.getPuv(c.getArticle().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), c.getConditionnement().getId()));
                }
                double prix = c.getPrix() - c.getRabais();
                double total = c.getQuantite() * prix;
                double _remise = dao.getRemiseVente(c.getArticle().getId(), c.getQuantite(), prix, docVente.getClient().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getUnite().getId());
                double _ristourne = dao.getRistourne(c.getConditionnement().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), entete.getDateEntete());
                if (selectContenu != null ? selectContenu.getId() > 0 : false) {
                    if (c.getId() > 0 && (selectContenu.getQuantite() == c.getQuantite() && selectContenu.getPrix() == c.getPrix())) {
                        _remise = selectContenu.getRemise();
                    }
                }
                c.setRistourne(_ristourne);
                c.setRemise(_remise);
                c.setPrixTotal(total - c.getRemise());
                c.setPrixMin(dao.getPuvMin(c.getArticle().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId()));
                c.getArticle().setPuvMin(c.getPrixMin());
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
                if (c.getArticle().getStock() < c.getQuantite()) {
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
            //charge le conditionnement par défaut de la vente      
            if (!isBonus) {
                if (modelFormEditable.fv_conditionnement) { //si le modèle de formulaire autorise la modification de cette information
                    champ = new String[]{"article", "point"};
                    val = new Object[]{new YvsBaseArticles(art.getId()), new YvsBasePointVente(entete.getPoint().getId())};
                    nameQueri = "YvsBaseConditionnementPoint.findConditionnement";
                    List<YvsBaseConditionnement> la = dao.loadNameQueries(nameQueri, champ, val);
                    if (la != null ? !la.isEmpty() : false) {
                        for (YvsBaseConditionnement c : la) {
                            if (c.getByVente() && c.getDefaut()) {
                                contenu.setConditionnement(UtilProd.buildBeanConditionnement(c));
                                break;
                            }
                        }
                        if (contenu.getConditionnement() != null ? contenu.getConditionnement().getId() < 1 : true) {
                            contenu.setConditionnement(UtilProd.buildBeanConditionnement(la.get(0)));
                        }
                    } else {
                        contenu.setConditionnement(art.getUniteVenteByDefault());
                    }
                }
                loadInfosArticle(art);
            } else {
                cloneObject(bonus.getArticleBonus(), art);
                update("txt_bonus_contenu_facture_vente");
            }
        } else {
            resetFicheContenu();
        }
        update("desc_article_facture_vente");
        update("tabview_facture_vente:desc_article_facture_vente");
    }

    boolean isBonus;

    public void searchArticle(boolean isBonus) {
        this.isBonus = isBonus;
        if (!isBonus) {
            String num = contenu.getArticle().getRefArt();
            contenu.getArticle().setDesignation("");
            contenu.getArticle().setError(true);
            contenu.getArticle().setId(0);
            listArt = false;
            selectArt = false;
            ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (m != null) {
                Articles y = m.searchArticleActifByPoint(num, true);
                if (m.getArticles() != null ? !m.getArticles().isEmpty() : false) {
                    if (m.getArticles().size() > 1) {
                        update("data_articles_facture_vente");
                    } else {
                        chooseArticle(y);
                    }
                    contenu.getArticle().setError(false);
                }
                listArt = y.isListArt();
                selectArt = y.isSelectArt();
            }
        } else {
            String num = bonus.getArticleBonus().getRefArt();
            bonus.getArticleBonus().setDesignation("");
            bonus.getArticleBonus().setError(true);
            bonus.getArticleBonus().setId(0);
            if (num != null ? num.trim().length() > 0 : false) {
                ManagedStockArticle m = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
                if (m != null) {
                    Articles y = m.searchArticleActifByPoint(num, true);
                    if (m.getArticles() != null ? !m.getArticles().isEmpty() : false) {
                        if (m.getArticles().size() > 1) {
                            update("data_articles_facture_vente");
                        } else {
                            chooseArticle(y);
                        }
                        bonus.getArticleBonus().setError(false);
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

    public void openDlgFactures() {
        codeVendeur_ = entete.getUsers().getCodeUsers();
        operateurVend = "LIKE";
        addParamVendeur(true);
        initForm = true;
        loadAllFacture(true);
    }

    public void openDocumentLies() {
        docVente.setDocuments(dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc}));
        update("data_livraison_facture_vente");
    }

    public void openDlgReglement() {
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            w.loadAllCaisseActif(true);
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
        String num = entete.getPoint().getLibelle();
        entete.getPoint().setId(0);
        entete.getPoint().setError(true);
        ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (m != null) {
            PointVente y = m.search(num, true);
            if (m.getPointsvente() != null ? !m.getPointsvente().isEmpty() : false) {
                if (m.getPointsvente().size() > 1) {
                    update("data_point_vente_facture_vente");
                } else {
                    entete.setPoint(y);
                    choosePoint();
                }
                entete.getPoint().setError(false);
            }
        }
    }

    public void searchDepot() {
        String num = docVente.getDepot().getDesignation();
        docVente.getDepot().setId(0);
        docVente.getDepot().setError(true);
        loadDepotByPoint(num, new YvsBasePointVente(entete.getPoint().getId()));
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
        String num = commercial.getCommercial().getNomPrenom();
        commercial.getCommercial().setId(0);
        commercial.getCommercial().setError(true);
        commercial.getCommercial().setCode("");
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
        update("select_commercial_vente");
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
            this.statut = (String) ev.getNewValue();
            addParamStatut();
        }
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
                p = new ParametreRequete("y.etat", "statut", statuts, "IN", "AND");
            }
        }
        paginator.addParam(p);
        loadAllFacture(true);
    }

    public void addParamStatut() {
        addParamStatut(true);
    }

    public void addParamStatut(boolean load) {
        ParametreRequete p;
        if (statut != null ? statut.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statut, egaliteStatut, "AND");
        } else {
            p = new ParametreRequete("y.statut", "statut", Constantes.ETAT_ANNULE, "!=", "AND");
        }
        paginator.addParam(p);
        if (load) {
            initForm = true;
            loadAllFacture(true);
        }
    }

    public void addParamToValide() {
        ParametreRequete p = new ParametreRequete("(y.etapeValide+1)", "etape", null, "IN", "AND");
        if (toValideLoad) {
            List<Integer> lnum = dao.loadNameQueries("YvsWorkflowAutorisationValidDoc.findOrdreStepe", new String[]{"document", "niveau"}, new Object[]{Constantes.DOCUMENT_FACTURE_VENTE, currentUser.getUsers().getNiveauAcces()});
            if ((lnum != null) ? !lnum.isEmpty() : false) {
                p = new ParametreRequete("(y.etapeValide+1)", "etape", lnum, "IN", "AND");
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
    }

    public void chooseStatutRegle(ValueChangeEvent ev) {
        statutRegle_ = ((String) ev.getNewValue());
        addParamStatutRegle();
    }

    private void addParamStatutDoc() {
        ParametreRequete p;
        if (statutRegle_ != null ? statutRegle_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutRegle", "statutRegle", statutRegle_, egaliteStatutR, "AND");
        } else {
            p = new ParametreRequete("y.statutRegle", "statutRegle", null);
        }
        paginator.addParam(p);
    }

    public void addParamStatutRegle() {
        addParamStatutDoc();
        initForm = true;
        loadAllFacture(true);
    }

    public void chooseStatutLivre(ValueChangeEvent ev) {
        statutLivre_ = ((String) ev.getNewValue());
        addParamStatutLivre();
    }

    public void addParamStatutLivre() {
        ParametreRequete p;
        if (statutLivre_ != null ? statutLivre_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutLivre", "statutLivre", statutLivre_, egaliteStatutL, "AND");
        } else {
            p = new ParametreRequete("y.statutLivre", "statutLivre", null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
    }

    public void addParamAutoLivre() {
        ParametreRequete p = new ParametreRequete("y.livraisonAuto", "livraisonAuto", null);
        if (autoLivreSearch != null) {
            p = new ParametreRequete("y.livraisonAuto", "livraisonAuto", autoLivreSearch, "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
    }

    public void chooseDateSearch() {
        ParametreRequete p;
        if (date_) {
            p = new ParametreRequete("heureDoc", "heureDoc", dateDebut_);
            p.setOperation("BETWEEN");
            p.setPredicat("AND");
            p.setOtherObjet(dateFin_);
        } else {
            p = new ParametreRequete("heureDoc", "heureDoc", null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
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
            entete.setNew_(true);
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
        super._chooseAgence();
        initForm = true;
        loadAllFacture(true);
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
        initForm = true;
        loadAllFacture(true);
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
        initForm = true;
        loadAllFacture(true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamPoint(point_);
        }
    }

    public void _chooseTranche() {
        ParametreRequete p;
        if (tranche_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.tranche", "tranche", new YvsGrhTrancheHoraire(tranche_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.tranche", "tranche", null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamTranche(tranche_, true);
        }
    }

    // Recherche des factures en selectionnant le vendeur
    public void _chooseVendeur() {
        addParamVendeur(false);
        initForm = true;
        loadAllFacture(true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamVendeur(codeVendeur_);
        }
        update("_select_entete_facture_vente");
    }

    public void changeOperateurVendeur(ValueChangeEvent ev) {
        if (ev != null) {
            operateurVend = (String) ev.getNewValue();
            addParamVendeur(true);
            initForm = true;
            loadAllFacture(true);
        }
    }

    // Recherche des factures en ecrivant le nom du vendeur
    public void _searchVendeur() {
        addParamVendeur(true);
        initForm = true;
        loadAllFacture(true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamVendeur(codeVendeur_);
        }
        update("_select_entete_facture_vente");
    }

    public void addParamVendeur(boolean code) {
        String predicat = (operateurVend.trim().equals("LIKE")) ? "OR" : "AND";
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
                p = new ParametreRequete(null, "vendeur", codeVendeur_ + "%", operateurVend, "AND");
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.enteteDoc.creneau.users.nomUsers)", "vendeur", codeVendeur_.toUpperCase() + "%", operateurVend, predicat));
                p.getOtherExpression().add(new ParametreRequete("UPPER(y.enteteDoc.creneau.users.codeUsers)", "vendeur", codeVendeur_.toUpperCase() + "%", operateurVend, predicat));
            } else {
                p = new ParametreRequete("y.enteteDoc.creneau.users.codeUsers", "vendeur", null);
            }
        }
        paginator.addParam(p);
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("enteteDoc.dateEntete", "dateEntete", dateDebut_);
        p.setOperation("=");
        p.setPredicat("AND");
        paginator.addParam(p);
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
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nomClient)", "client", codeClient_.toUpperCase() + "%", operateuClt, predicat));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.codeClient)", "client", codeClient_.toUpperCase() + "%", operateuClt, predicat));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.nom)", "client", codeClient_.toUpperCase() + "%", operateuClt, predicat));
        } else {
            p = new ParametreRequete("y.client.codeClient", "client", null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
    }

    public void searchByRepr() {
        ParametreRequete p;
        if (otherSearch_ != null ? otherSearch_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "representant", otherSearch_.toUpperCase() + "%", " LIKE ", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.representant.codeUsers)", "representant", otherSearch_.toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.representant.nomUsers)", "representant", otherSearch_.toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p = new ParametreRequete("y.client.representant.codeUsers", "representant", null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
    }

    public void _chooseStatut(ValueChangeEvent ev) {
        this.statut = ((String) ev.getNewValue());
        ParametreRequete p;
        if (statut != null ? statut.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statut, "=", "AND");
        } else {
            p = new ParametreRequete("y.statut", "statut", Constantes.ETAT_ANNULE, "!=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (m != null) {
            m.addParamStatut(statut);
        }
        update("_select_entete_facture_vente");
    }

    public void chooseCommande() {
        ParametreRequete p;
        if (numBon != null ? numBon.trim().length() > 0 : false) {
            p = new ParametreRequete("y.documentLie.numDoc", "numDoc", "%" + numBon + "%", " LIKE ", "AND");
        } else {
            p = new ParametreRequete("y.documentLie.numDoc", "numDoc", null, " LIKE ", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
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
        ParametreRequete p = new ParametreRequete("y.enteteDoc.dateEntete", "dateEntete", null, " BETWEEN ", "AND");
        if (b) {
            if (dateDebut != null && dateFin != null) {
                if (dateDebut.before(dateFin) || dateDebut.equals(dateFin)) {
                    p.setObjet(dateDebut);
                    p.setOtherObjet(dateFin);
                }
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);

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
            if (service != null && id > 0) {
                int idx = service.getCaisses().indexOf(new YvsBaseCaisse(id));
                if (idx > -1) {
                    YvsBaseCaisse y = service.getCaisses().get(idx);
                    reglement.setCaisse(UtilCompta.buildSimpleBeanCaisse(y));
                    loadCaissiers(y);
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
            docVente.setVille(new Dictionnaire());
            docVente.setAdresse(new Dictionnaire());
            if (d.getTiers() != null ? d.getTiers().getId() > 0 : false) {
                boolean correct = false;
                if (d.getTiers().getVille() != null ? d.getTiers().getVille().getId() > 0 : false) {
                    cloneObject(docVente.getVille(), d.getTiers().getVille());
                    if (!venteDirecte) {
                        chooseVille();
                    }
                    if (d.getTiers().getSecteur() != null ? d.getTiers().getSecteur().getId() > 0 : false) {
                        cloneObject(docVente.getAdresse(), d.getTiers().getSecteur());
                        if (!venteDirecte) {
                            chooseAdresse();
                        }
                        correct = true;
                    }
                }
                if (!correct) {
                    cloneObject(docVente.getAdresse(), d.getTiers().getSecteur_());
                }
            }
            //choisir le code tiers à utiliser
            if (d.isSuiviComptable()) {
                docVente.setTiers(d);
            } else {
                //récupère le code tiers du commerciale ayant le point de vente
                if (entete.getPoint().getId() > 0) {
                    ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                    if (service != null) {
                        int idx = service.getPointsvente().indexOf(new YvsBasePointVente(entete.getPoint().getId()));
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
        this.selectDoc = doc;
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
        if (tabIds != null ? tabIds.trim().length() > 0 : false) {
            ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
            if (w != null) {
                List<Integer> ids = decomposeSelection(tabIds);
                if (!ids.isEmpty()) {
                    YvsComDocVentes current;
                    for (Integer index : ids) {
                        current = w.reBuildNumero(documents.get(index), true, false);
                        documents.set(index, current);
                    }
                    succes();
                }
            }
            tabIds = "";
        }
    }

    public void definedTiersVente() {
        if (tabIds != null ? tabIds.trim().length() > 0 : false) {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComDocVentes current;
                for (Integer index : ids) {
                    current = documents.get(index);
                    definedTiers(current, false);
                }
                succes();
            }
            tabIds = "";
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
                    y.setDateUpdate(new Date());
                    y.setAuthor(currentUser);
                    dao.update(y);
                    if (succes) {
                        succes();
                    }
                }
                update("data_facture_vente");
            }
        } catch (Exception ex) {
        }
    }

    public void genererFicheEntree() {
        if (tabIds != null ? tabIds.trim().length() > 0 : false) {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComDocVentes current;
                for (Integer index : ids) {
                    current = documents.get(index);
                    genererFicheEntree(current, ids.size() == 1);
                }
                succes();
            }
            tabIds = "";
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
            }
        }
    }

    public void chooseAdresse() {
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            Dictionnaire d = m.chooseSecteur(docVente.getVille(), docVente.getAdresse().getId());
            if (d != null ? d.getId() > 0 : false) {
                cloneObject(docVente.getAdresse(), d);
            }
        }
    }

    public void chooseDate() {
        if (!autoriser("fv_update_header")) {
            entete.setDateEntete(new Date());
            openNotAcces();
            return;
        }
        selectEntete = new YvsComEnteteDocVente();
//        loadCurrentEntete();
    }

    public void chooseTranche() {
        selectEntete = new YvsComEnteteDocVente();
        if (entete.getTranche() != null ? entete.getTranche().getId() > 0 : false) {
            for (YvsComCreneauPoint cp : entete.getPoint().getListTranche()) {
                if (cp.getTranche().getId().equals(entete.getTranche().getId())) {
                    TrancheHoraire t = UtilCom.buildBeanTrancheHoraire(cp.getTranche());
                    cloneObject(entete.getTranche(), t);
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

    public void choosePoint_(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                entete.getPoint().setId(id);
                choosePoint();
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
                entete.getPoint().setId(0);
            }
        }
    }

    public void choosePoint(long id) {
        if (id > 0) {
            entete.getPoint().setId(id);
            choosePoint();
        }
    }

    public void choosePoint() {
        selectEntete = new YvsComEnteteDocVente();
        entete.getPoint().getListTranche().clear();
        if (entete.getPoint() != null ? entete.getPoint().getId() > 0 : false) {
            ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
            if (service != null) {
                int idx = service.getPointsvente().indexOf(new YvsBasePointVente(entete.getPoint().getId()));
                if (idx >= 0) {
                    YvsBasePointVente y = service.getPointsvente().get(idx);
                    entete.setPoint(UtilCom.buildBeanPointVente(y));
                    //charge les tranches du point de vente
                    entete.getPoint().setListTranche(dao.loadNameQueries("YvsComCreneauPoint.findByDepot", new String[]{"point"}, new Object[]{y}));
                    if (!venteDirecte) {
                        //charge les dépôts
                        loadDepotByPoint(y);
                    }
                    docVente.setValidationReglement(y.getValidationReglement());
                    if (!venteDirecte) {
                        //trouve le vendeur
                        vendeurs = dao.loadNameQueries("YvsComCreneauHoraireUsers.findUsersByPoint", new String[]{"point"}, new Object[]{y});
                        if (vendeurs.contains(currentUser.getUsers())) {
                            entete.setUsers(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
                        }
                    }
                }
            }
            ManagedStockArticle w = (ManagedStockArticle) giveManagedBean(ManagedStockArticle.class);
            if (w != null) {
                w.setEntityPoint(UtilCom.buildPointVente(entete.getPoint(), currentUser, currentAgence));
            }
            if (venteDirecte) {
                entete.setUsers(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
            }
            if (entete.getPoint().getListTranche() != null ? !entete.getPoint().getListTranche().isEmpty() : false) {
                entete.setTranche(UtilCom.buildBeanTrancheHoraire(entete.getPoint().getListTranche().get(0).getTranche()));
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
                if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
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
                YvsBaseDepots y = depotsLivraison.get(idx);
                docVente.setDepot(UtilCom.buildBeanDepot(y));
                loadAllTranche(y, docVente.getDateLivraisonPrevu());
                if (!verifyOperation(docVente.getDepot(), Constantes.SORTIE, Constantes.VENTE, false)) {

                }
            }
            if ((tranches != null ? !tranches.isEmpty() : false)) {
                if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
                    if (!tranches.contains(currentPlanning.get(0).getCreneauDepot().getTranche())) {
                        docVente.setTranche(UtilCom.buildBeanTrancheHoraire(tranches.get(0)));
                    } else {
                        docVente.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
                    }
                }
            }
            if (docVente.getTranche() != null ? docVente.getTranche().getId() < 1 : true) {
                if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
                    docVente.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
                }
            }
            update("select_tranche_livraison_fv");
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
                    montant_remise = getMontantRemise(y, selectContenu);
                    onRemiseBlur(false);
                }
            }
        }
    }

    public void initInfosContenu(YvsComContenuDocVente y) {
        selectContenu = y;
        if (selectContenu != null ? selectContenu.getId() > 0 : false) {
            Double quantite = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findSumByParentTypeStatut", new String[]{"parent", "statut", "typeDoc"}, new Object[]{y, Constantes.ETAT_VALIDE, Constantes.TYPE_BLV});
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
        if (docVente.getClient() != null ? docVente.getClient().getId() < 1 : true) {
            YvsComClient c = currentClientDefault();
            if (c != null ? c.getId() > 0 : false) {
                ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
                if (w != null && !venteDirecte) {
                    w.creance(c);
                }
                chooseClient(UtilCom.buildSimpleBeanClient(c));
            } else {
                openDialog("dlgConfirmAddClient");
                docVente.setClient(new Client());
            }
            update("select_client_facture_vente");
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
                t.setStock(dao.stocks(t_.getId(), 0, 0, 0, 0, entete.getDateEntete(), contenu.getConditionnement().getId(), contenu.getLot().getId()));
                t.setPuv(dao.getPuv(t_.getId(), contenu.getQuantite(), contenu.getPrix(), docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), contenu.getConditionnement().getId()));
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
        val = new Object[]{new YvsComPlanRistourne(docVente.getClient().getRistourne().getId()), new YvsBaseArticles(contenu.getArticle().getId()), new YvsBaseConditionnement(contenu.getConditionnement().getId()), entete.getDateEntete(), contenu.getQuantite(), 'B'};
        List<YvsComGrilleRistourne> gr = dao.loadNameQueries("YvsComGrilleRistourne.findByBonus", champ, val);
        if (gr.size() > 1) {
            getWarningMessage("Plus d'un plan de bonus a été trouvé pour cet article veuiller vérifier la configuration");
        } else if (gr.size() == 1) {
            contenu.setArticleBonus(UtilCom.buildBeanArticle(gr.get(0).getArticle()));
            contenu.setConditionnementBonus(UtilProd.buildBeanConditionnement(gr.get(0).getConditionnement()));
            contenu.setQuantiteBonus(gr.get(0).getMontantRistourne());
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
        if (service
                != null) {
            piece = service.openConfirmPaiement(piece, source, true, true, emission);
            int idx = docVente.getReglements().indexOf(piece);
            if (idx > -1) {
                docVente.getReglements().set(idx, piece);
            }
        }
    }

    public void genereMensualite() {
        if ((docVente != null) ? docVente.getId() > 0 : false) {
            docVente.getReglements().clear();

            if ((docVente.getModeReglement() != null) ? docVente.getModeReglement().getId() > 0 : false) {
                ManagedReglementVente m = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class
                );
                if (m
                        != null) {
                    Caisses caisse = reglement.getCaisse();
                    if (caisse != null ? caisse.getId() < 1 : true) {
                        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                        if (service != null) {
                            reglement.setCaisse(UtilCompta.buildSimpleBeanCaisse(service.findByResponsable(currentUser.getUsers())));
                        }
                    }
                    docVente.setReglements(m.generetedPiecesFromModel(new YvsBaseModelReglement(docVente.getModeReglement().getId(), docVente.getModeReglement().getDesignation()), docVente, new YvsBaseCaisse(caisse.getId())));
                    newMensualite = true;
                    reglement.setMontant(0);
                    reglement.setDatePaiement(new Date());
                    succes();
                    update("form_mensualite_facture_vente");
                }
            } else {
                getErrorMessage("Vous devez preciser le model de reglement");
            }
        } else {
            getErrorMessage("Vous devez au préalable enregistrer la facture");
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

    public
            void lettrer(YvsComDocVentes y) {
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

    public
            void lettrerCaisse(YvsComptaCaissePieceVente y) {
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

    public boolean annulerOrder(YvsComDocVentes selectDoc, DocVente docVente, boolean continueSave) {
        System.err.println("selectDoc : " + selectDoc);
        if (selectDoc != null ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {

            if (dao.isComptabilise(selectDoc.getId(), Constantes.SCR_AVOIR_VENTE)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return false;
                }

            }

            if (!selectDoc.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
                List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{selectDoc});
                if (l != null ? l.isEmpty() : true) {
                    if (docVente != null ? docVente.getMontantAvance() > 0 : false) {
                        getErrorMessage("Cette facture est déjà en cours de règlement !");
                    } else {
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
                } else {
                    for (YvsComDocVentes d : l) {
                        if (!d.getStatut().equals(Constantes.ETAT_EDITABLE)) {
                            getErrorMessage("Impossible d'annuler cet ordre car il possède des documents de livraison déja valide");
                            return false;
                        }
                    }
                    System.err.println("ENTER");
                    if (!continueSave) {
                        openDialog("dlgConfirmAnnuler");
                    } else {
                        annulerOrderForce(selectDoc, docVente);
                    }
                }
            } else {
                getErrorMessage("Ce document est deja livré");
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
        if (checkAvance && !force) {
            openDialog("dlgConfirmValide");
        } else {
            if (_saveNew(true)) {
                validerOrder();
            }
        }
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
        if (docVente.getMontantResteApayer() < 0) {
            getErrorMessage("Vous ne pouvez pas valider cette facture...car la somme des reglements est superieure au montant de la facture");
            return false;
        }
        if (docVente.getContenus().isEmpty()) {
            getErrorMessage("Vous ne pouvez valider un document vide !");
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
        for (YvsComContenuDocVente c : docVente.getContenus()) {
            //controle les quantités déjà facturé
            Double qteFacture = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findByDocLierTypeStatutArticleS", new String[]{"docVente", "statut", "typeDoc", "article", "unite"}, new Object[]{selectDoc, Constantes.ETAT_VALIDE, Constantes.TYPE_FV, c.getArticle(), c.getConditionnement()});
            qteFacture = (qteFacture != null) ? qteFacture : 0;
            //trouve la livré
            Double qteLivre = (Double) dao.loadObjectByNameQueries("YvsComContenuDocVente.findQteByDocLivre", new String[]{"facture", "article", "typeDoc", "statut", "unite"}, new Object[]{selectDoc, c.getArticle(), Constantes.TYPE_BLV, Constantes.ETAT_VALIDE, c.getConditionnement()});
            qteLivre = (qteLivre != null) ? qteLivre : 0;
            if (qteFacture < (qteLivre)) {
                getErrorMessage("Impossible de valider la facture! l'article " + c.getArticle().getRefArt() + " est livré au delà de la quantité facturé !");
                return false;
            }
        }
////            }
//        }
        return true;
    }

    public boolean validerOrder() {
        return validerOrder(selectDoc);
    }

    public boolean validerOrder(YvsComDocVentes selectDoc) {
        return validerOrder(docVente, selectDoc, true, true);
    }

    public boolean validerOrder(DocVente docVente, YvsComDocVentes selectDoc, boolean msg, boolean succes) {
        if (!autoriser("fv_valide_doc")) {
            openNotAcces();
            return false;
        }
        if (!controleValidation(docVente, selectDoc)) {
            return false;
        }

        // Générère l'echéancier prévu de règlement
        generatedEcheancierReg(selectDoc, false);
        if (changeStatut(Constantes.ETAT_VALIDE, docVente, selectDoc)) {
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
                        transmisOrder(selectDoc, selectDoc.getDateLivraisonPrevu(), Constantes.ETAT_VALIDE, true);
                        docVente.setStatutLivre(Constantes.ETAT_LIVRE);
                        docVente.setConsigner(false);
                        docVente.setDateConsigner(null);
                        update("blog_form_contenu_facture_vente");
                        update("blog_form_cout_facture_vente");
                        update("form_mensualite_facture_vente");
                        update("data_livraison_facture_vente");
                        update("blog_commercial_vente");
                    }
                    DocVente d;
                    for (YvsComDocVentes dLiv : lv) {
                        if (!dLiv.getStatut().equals(Constantes.ETAT_ANNULE) && !dLiv.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            d = UtilCom.buildBeanDocVente(dLiv);
                            d.setLivreur(UtilUsers.buildBeanUsers(selectDoc.getEnteteDoc().getCreneau().getUsers()));
                            d.setDocumentLie(docVente);
                            dLiv.setDocumentLie(selectDoc);
                            service.validerOrder(dLiv, d, false, false, false, null, false);
                        }
                    }
                }
            }
            if (docVente.isValidationReglement()) {
                ManagedReglementVente m = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                if (m != null) {
                    Caisses caisse = reglement.getCaisse();
                    ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                    if (w != null) {
                        if (caisse != null ? caisse.getId() < 1 : true) {
                            caisse = UtilCompta.buildSimpleBeanCaisse(w.findByResponsable(selectDoc.getEnteteDoc().getCreneau().getUsers()));
                            reglement.setCaisse(caisse);
                        }
                        if (caisse != null ? caisse.getId() < 1 : false) {
                            List<YvsBaseCaisse> caisses = w.loadCaisses(selectDoc.getEnteteDoc().getCreneau().getUsers());
                            if (caisses != null ? !caisses.isEmpty() : false) {
                                caisse = UtilCompta.buildSimpleBeanCaisse(caisses.get(0));
                                reglement.setCaisse(caisse);
                            }
                        }
                    }
                    if (caisse != null ? caisse.getId() > 0 : false) {
                        List<YvsComptaCaissePieceVente> pieces = m.generetedPiecesFromModel(new YvsBaseModelReglement(docVente.getModeReglement().getId(), docVente.getModeReglement().getDesignation()), docVente, UtilCompta.buildBeanCaisse(caisse));
                        for (YvsComptaCaissePieceVente y : pieces) {
                            if (!y.getStatutPiece().equals(Constantes.ETAT_REGLE)) {
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
                }
            }
            if (docVente.isValidationReglement() && !docVente.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
//                if (docVente.getReglements() != null ? docVente.getReglements().isEmpty() : true) { //Si le reste à payer est positif   

                if (docVente.getReglements() != null ? !docVente.getReglements().isEmpty() : false) {
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (w != null) {
                        ManagedRetenue wr = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                        for (YvsComptaCaissePieceVente y : docVente.getReglements()) {
                            if (!y.getStatutPiece().equals(Constantes.ETAT_REGLE)) {
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
            if (currentParam == null) {
                currentParam = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (currentParam != null) {
                if (currentParam.getComptabilisationAuto()) {
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        w.comptabiliserVente(selectDoc, false, false);
                    }
                }
            }
            afterChangeStatut(statut, docVente, selectDoc);
            if (this.docVente != null ? selectDoc != null ? selectDoc.getId().equals(this.docVente.getId()) : false : false) {
                update("tabview_facture_vente");
            }
            return true;
        }
        return false;
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
            if (tabIds != null ? tabIds.trim().length() > 0 : false) {
                List<Integer> ids = decomposeSelection(tabIds);
                boolean succes = false;
                for (Integer idx : ids) {
                    if (idx > -1) {
                        YvsComDocVentes y = documents.get(idx);
                        Long count = (Long) dao.loadObjectByNameQueries("YvsComDocVentes.findByParentTypeDocCount", new String[]{"documentLie", "typeDoc"}, new Object[]{y, Constantes.TYPE_BLV});
                        if (count != null ? count < 1 : true) {
                            transmisOrder(y, y.getDateLivraisonPrevu(), Constantes.ETAT_VALIDE, false);
                            succes = true;
                        }
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

    public void transmisOrder() {
        selectDoc.setDepotLivrer(UtilProd.buildBeanDepot(docVente.getDepot()));
        selectDoc.setTrancheLivrer(UtilGrh.buildTrancheHoraire(docVente.getTranche(), currentUser));
        selectDoc.setDateLivraisonPrevu(docVente.getDateLivraisonPrevu());
        if (selectDoc.getTypeDoc().equals(Constantes.TYPE_FV)) {
            transmisOrder(selectDoc, dateLivraison, statutLivraison, true);
        } else {
            ManagedBonVente w = (ManagedBonVente) giveManagedBean(ManagedBonVente.class);
            if (w != null) {
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
        update("blog_form_contenu_facture_vente");
        update("blog_form_cout_facture_vente");
        update("form_mensualite_facture_vente");
        update("data_livraison_facture_vente");
        update("blog_commercial_vente");
    }

    private boolean controlContentForTransmis(YvsComContenuDocVente c, YvsBaseDepots depot, Date dateLivraison, String statut, boolean message) {
        champ = new String[]{"article", "depot"};
        val = new Object[]{c.getArticle(), depot};
        YvsBaseArticleDepot y = (YvsBaseArticleDepot) dao.loadOneByNameQueries("YvsBaseArticleDepot.findByArticleDepot", champ, val);
        if (y != null ? y.getId() < 1 : true) {
            if (message) {
                getErrorMessage("Impossible d'effectuer cette action... Car le depot " + depot.getDesignation() + " ne possède pas l'article " + c.getArticle().getDesignation());
            }
            return false;
        }
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

    public void transmisOrder(YvsComDocVentes facture, Date dateLivraison, String statut, boolean message) {
        statut = statut != null ? statut.trim().length() > 0 ? statut : Constantes.ETAT_VALIDE : Constantes.ETAT_VALIDE;
        if (!autoriser("fv_livrer")) {
            if (message) {
                openNotAcces();
            }
            return;
        }
        if (facture == null) {
            if (message) {
                getErrorMessage("Vous devez selectionner la facture");
            }
            return;
        }
        if (facture.getEnteteDoc() != null ? (facture.getEnteteDoc().getCreneau() != null ? (facture.getEnteteDoc().getCreneau().getCreneauPoint() != null ? (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint() != null) : false) : false) : false) {
            switch (facture.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getLivraisonOn()) {
                case 'R': {
                    if (!facture.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                        if (message) {
                            getErrorMessage("Cette facture doit etre reglée avant de pouvoir générer une livraison");
                        }
                        return;
                    }
                }
                case 'V': {
                    if (!facture.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        if (message) {
                            getErrorMessage("Cette facture doit etre validée avant de pouvoir générer une livraison");
                        }
                        return;
                    }
                }
            }
        }
        if (dateLivraison != null ? dateLivraison.after(new Date()) : true) {
            if (message) {
                getErrorMessage("La date de livraison est incorrecte !");
            }
            return;
        }
        if (facture.getDepotLivrer() != null ? facture.getDepotLivrer().getId() < 1 : true) {
            if (message) {
                getErrorMessage("Aucun dépôt de livraison n'a été trouvé !");
            }
            return;
        }
        if (facture.getTrancheLivrer() != null ? facture.getTrancheLivrer().getId() < 1 : true) {
            List<YvsGrhTrancheHoraire> list = loadTranche(facture.getDepotLivrer(), dateLivraison);
            if (list != null ? list.size() == 1 : false) {
                facture.setTrancheLivrer(list.get(0));
            } else {
                if (message) {
                    getErrorMessage("Aucune tranche de livraison n'a été trouvé !");
                }
                return;
            }
        } else if (!Util.asString(facture.getTrancheLivrer().getTypeJournee())) {
            facture.setTrancheLivrer((YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findById", new String[]{"id"}, new Object[]{facture.getTrancheLivrer().getId()}));
        }
        if (!verifyOperation(new Depots(facture.getDepotLivrer().getId(), facture.getDepotLivrer().getDesignation()), Constantes.SORTIE, Constantes.VENTE, false)) {
            return;
        }

        if (!verifyInventaire(facture.getDepotLivrer(), facture.getTrancheLivrer(), dateLivraison)) {
            return;
        }
        String num = genererReference(Constantes.TYPE_BLV_NAME, dateLivraison, facture.getDepotLivrer().getId(), Constantes.DEPOT);
        if (num != null ? num.trim().length() > 0 : false) {
            List<YvsComContenuDocVente> l = loadContenusStay(facture, Constantes.TYPE_BLV);
            if (l != null ? !l.isEmpty() : false) {
                List<YvsBaseDepots> depotsLivraison = new ArrayList<>();
                List<YvsComContenuDocVente> list = new ArrayList<>();
                YvsBaseDepots depot;
                for (YvsComContenuDocVente c : l) {
                    depot = facture.getDepotLivrer();
                    if (c.getDepoLivraisonPrevu() != null ? (c.getDepoLivraisonPrevu().getId() != null ? c.getDepoLivraisonPrevu().getId() > 0 : false) : false) {
                        depot = c.getDepoLivraisonPrevu();
                    }
                    if (!depotsLivraison.contains(depot)) {
                        if (!verifyTranche(facture.getTrancheLivrer(), depot, dateLivraison)) {
                            return;
                        }
                        depotsLivraison.add(depot);
                    }
                    if (!controlContentForTransmis(c, depot, dateLivraison, statut, message)) {
                        return;
                    }
                    list.add(c);
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
                            return;
                        }
                        list.add(a);
                    }
                }
                if (facture.getEnteteDoc() != null) {
                    YvsComDocVentes y;
                    boolean continu = true;
                    for (YvsBaseDepots d : depotsLivraison) {
                        y = new YvsComDocVentes(facture);
                        y.setEnteteDoc(facture.getEnteteDoc());
                        y.setDateSave(new Date());
                        y.setAuthor(currentUser);
                        y.setTypeDoc(Constantes.TYPE_BLV);
                        y.setNumDoc(num);
                        y.setNumPiece("BL N° " + facture.getNumDoc());
                        y.setDepotLivrer(d);
                        y.setTrancheLivrer(facture.getTrancheLivrer());
                        y.setLivreur(currentUser.getUsers());
                        y.setDateLivraison(dateLivraison);
                        y.setDateLivraisonPrevu(facture.getDateLivraisonPrevu());
                        y.setDocumentLie(new YvsComDocVentes(facture.getId()));
                        y.setHeureDoc(new Date());
                        y.setStatut(Constantes.ETAT_EDITABLE);
                        y.setStatutLivre(Constantes.ETAT_ATTENTE);
                        y.setStatutRegle(Constantes.ETAT_ATTENTE);
                        y.setValiderBy(currentUser.getUsers());
                        y.setDateValider(facture.getDateLivraisonPrevu());
                        y.setDateSave(new Date());
                        y.setDateUpdate(new Date());
                        y.setCloturer(false);
                        y.setDescription("Livraison de la facture N° " + facture.getNumDoc() + " le " + ldf.format(dateLivraison) + " à " + time.format(dateLivraison));
                        y.setId(null);
                        y = (YvsComDocVentes) dao.save1(y);
                        for (YvsComContenuDocVente c : list) {
                            if (c.getDepoLivraisonPrevu() != null ? (c.getDepoLivraisonPrevu().getId() != null ? c.getDepoLivraisonPrevu().getId() > 0 : false) : false) {
                                if (c.getDepoLivraisonPrevu().equals(d)) {
                                    c.setDocVente(y);
                                    c.setStatut(Constantes.ETAT_VALIDE);
                                    c.setAuthor(currentUser);
                                    c.setParent(new YvsComContenuDocVente(c.getId()));
                                    c.setId(null);
                                    dao.save(c);
                                }
                            } else if (facture.getDepotLivrer().equals(d)) {
                                c.setDocVente(y);
                                c.setStatut(Constantes.ETAT_VALIDE);
                                c.setAuthor(currentUser);
                                c.setParent(new YvsComContenuDocVente(c.getId()));
                                c.setId(null);
                                dao.save(c);
                            }
                        }
                        y.setDocumentLie(facture);
                        boolean livrer;

                        if (statut.equals(Constantes.ETAT_VALIDE)) {
                            ManagedLivraisonVente service = (ManagedLivraisonVente) giveManagedBean(ManagedLivraisonVente.class);
                            if (service != null) {
                                livrer = service.validerOrder(y, false, false, true, null, false);
                                if (!livrer) {
                                    continu = false;
                                }
                            }
                        }
                        facture.getDocuments().add(y);
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
                        facture.setConsigner(statut.equals(Constantes.ETAT_VALIDE) ? false : facture.getConsigner());
                        facture.setDateConsigner(statut.equals(Constantes.ETAT_VALIDE) ? null : facture.getDateConsigner());
                        facture.setStatutLivre(statut.equals(Constantes.ETAT_VALIDE) ? Constantes.ETAT_LIVRE : Constantes.ETAT_ATTENTE);
                        if (statut.equals(Constantes.ETAT_VALIDE)) {
                            for (YvsComContenuDocVente c : facture.getContenus()) {
                                if (c.getIdReservation() != null ? c.getIdReservation().getId() != null ? c.getIdReservation().getId() > 0 : false : false) {
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
        YvsComDocVentes y = entity;
        if (y != null ? y.getId() > 0 : false) {
            entity.setStatut(y.getStatut());
            entity.setStatutLivre(y.getStatutLivre());
            entity.setStatutRegle(y.getStatutRegle());

            bean.setStatut(y.getStatut());
            bean.setStatutLivre(y.getStatutLivre());
            bean.setStatutRegle(y.getStatutRegle());
        }
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

//            dao.getEquilibreVente(doc_.getId());
//            YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{doc_.getId()});
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
//                dao.getEquilibreVente(doc_.getDocumentLie().getId());
//                y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{doc_.getDocumentLie().getId()});
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
        try {
            if (currentParam == null) {
                currentParam = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
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
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("TAXE", tx != null ? tx > 0 : false);
                param.put("LOGO", returnLogo());
                String IMG_QRC = Util.createQRCode(y.getNumDoc());
                if (Util.asString(IMG_QRC)) {
                    param.put("IMG_QRC", IMG_QRC);
                }
                param.put("SUBREPORT_DIR", SUBREPORT_DIR(withHeader));
                String fileName = currentParam != null ? currentParam.getModelFactureVente() : "facture_vente";
                fileName = executeReport(fileName, param, true);
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
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numDoc)", "numDoc", "%" + numSearch_.trim().toUpperCase() + "%", operateurRef, predicat));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numPiece)", "numDoc", "%" + numSearch_.trim().toUpperCase() + "%", operateurRef, predicat));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numeroExterne)", "numDoc", "%" + numSearch_.trim().toUpperCase() + "%", operateurRef, predicat));
        } else {
            p = new ParametreRequete("y.numDoc", "numDoc", null);
        }
        paginator.addParam(p);
    }

    public void searchByNum() {
        addParamNumDoc();
        initForm = true;
        loadAllFacture(true);
        if (documents.size() == 1) {
            onSelectObject(documents.get(0));
            execute("collapseForm('facture_vente')");
        } else {
            execute("collapseList('facture_vente')");
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
        loadAllFacture(true);
    }

    public void addParamWithTiers() {
        ParametreRequete p = new ParametreRequete("y.tiers", "withTiers", null, "IN", "AND");
        if (withTiersSearch != null) {
            p = new ParametreRequete("y.tiers", "withTiers", "tiers", withTiersSearch ? "IS NOT NULL" : "IS NULL", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllFacture(true);
    }

    public void addParamType() {
        addParamType(true);
    }

    public void addParamType(boolean load) {
        ParametreRequete p;
        if (Util.asString(typeSearch)) {
            p = new ParametreRequete("y.typeDoc", "typeDoc", typeSearch, "=", "AND");
        } else {
            p = new ParametreRequete(null, "type", "XXXX", "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.typeDoc", "type", Constantes.TYPE_FV, "=", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.typeDoc", "type1", Constantes.TYPE_BCV, "=", "OR"));
        }
        paginator.addParam(p);
        if (load) {
            initForm = true;
            loadAllFacture(true);
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
    public
            void cleanEnteteVente() {
        ManagedVente m = (ManagedVente) giveManagedBean(ManagedVente.class
        );
        if (m
                != null) {
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

    public void equilibreByDate() {
        dao.getEquilibreVente(currentAgence.getSociete().getId(), dateDebut, dateFin);
    }

    public void equilibre(YvsComDocVentes selectDoc) {
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
            int idx = documents.indexOf(selectDoc);
            if (idx > -1) {
                documents.set(idx, selectDoc);
                update("data_facture_vente");
            }
            succes();
        }
    }

    public void equilibre() {
        equilibre(selectDoc);
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
                    transmisOrder(y, y.getEnteteDoc().getDateEntete(), Constantes.ETAT_VALIDE, false);
                }
            }
        }
        update("data_facture_vente");
    }

    public void traitementLot() {
        try {
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                YvsComDocVentes y;
                DocVente d;
                ManagedReglementVente m = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                ManagedBonVente wb = (ManagedBonVente) giveManagedBean(ManagedBonVente.class);
                for (Integer id : ids) {
                    if (id != -1 ? documents.size() > id : false) {
                        y = documents.get(id);
                        if (y.getTypeDoc().equals(Constantes.TYPE_BCV)) {

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
                                transmisOrder(y, y.getEnteteDoc().getDateEntete(), statutLotBL, false);
                            }
                            if (lotPiece) {
                                if (m != null) {
                                    Caisses caisse = null;
                                    ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                                    if (w != null) {
                                        //trouve la caisse par defaut
                                        caisse = UtilCompta.buildSimpleBeanCaisse(w.findByResponsable(y.getEnteteDoc().getCreneau().getUsers()));
                                    }
                                    if (caisse != null ? caisse.getId() > 0 : false) {
                                        setMontantTotalDoc(y);
                                        DocVente vente = UtilCom.buildSimpleBeanDocVente(y);
                                        if (y.getReglements() == null) {
                                            y.setReglements(new ArrayList<YvsComptaCaissePieceVente>());
                                        }
                                        y.getReglements().addAll(m.generetedPiecesFromModel(new YvsBaseModelReglement(y.getModelReglement() != null ? y.getModelReglement().getId() : 0), vente, new YvsBaseCaisse(caisse.getId())));
                                        for (YvsComptaCaissePieceVente p : y.getReglements()) {
                                            if (!p.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                                p.setVente(y);
                                                if (p.getId() < 1 && (p.getCaisse() != null) ? p.getCaisse().getId() > 0 : false && (p.getModel() != null) ? p.getModel().getId() > 0 : false && p.getMontant() > 0) {
                                                    p.setId(null);
                                                    p = (YvsComptaCaissePieceVente) dao.save1(p);
                                                }
                                                if (p.getId() > 0 ? statutLotPiece.equals(Constantes.ETAT_REGLE) ? p.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE) : false : false) {
                                                    m.openConfirmPaiement(p, "F", false, false, false);
                                                    m.reglerPieceTresorerie(true, false);
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

    public
            void gotoViewBl(YvsComDocVentes dv) {
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

    public
            void gotoViewReglementsVente(YvsComDocVentes dv) {
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
        if (entete != null ? entete.getId() > 0 : false) {
            entete.setMontantRemise(0);
            entete.setMontantTaxe(0);
            entete.setMontantRistourne(0);
            entete.setMontantCommission(0);
            entete.setMontantHT(0);
            entete.setMontantTTC(0);
            entete.setMontantRemises(0);
            entete.setMontantCoutSup(0);
            entete.setMontantAvance(0);
            entete.setMontantTaxeR(0);
            entete.setAvanceCommande(0);

            List<YvsComDocVentes> l = dao.loadNameQueries("YvsComDocVentes.findByTypeDocEntete", new String[]{"entete", "typeDoc", "societe"}, new Object[]{new YvsComEnteteDocVente(entete.getId()), Constantes.TYPE_FV, currentAgence.getSociete()});
            for (YvsComDocVentes d : l) {
                setMontantTotalDoc(d, d.getContenus(), currentAgence.getSociete().getId(), null, null, dao);

                entete.setMontantRemise(entete.getMontantRemise() + d.getMontantRemise());
                entete.setMontantTaxe(entete.getMontantTaxe() + d.getMontantTaxe());
                entete.setMontantRistourne(entete.getMontantRistourne() + d.getMontantRistourne());
                entete.setMontantCommission(entete.getMontantCommission() + d.getMontantCommission());
                entete.setMontantHT(entete.getMontantHT() + d.getMontantHT());
                entete.setMontantTTC(entete.getMontantTTC() + d.getMontantTTC());
                entete.setMontantRemises(entete.getMontantRemises() + d.getMontantRemises());
                entete.setMontantCoutSup(entete.getMontantCoutSup() + d.getMontantCS());
                entete.setMontantAvance(entete.getMontantAvance() + d.getMontantAvance());
                entete.setMontantTaxeR(entete.getMontantTaxeR() + d.getMontantTaxeR());
            }
            String query = "SELECT DISTINCT y.id FROM yvs_com_doc_ventes y WHERE y.type_doc= ? AND y.entete_doc = ? AND y.statut = ? AND y.document_lie IS NULL";
            Options[] params = new Options[]{new Options(Constantes.TYPE_BCV, 1), new Options(entete.getId(), 2), new Options(Constantes.ETAT_VALIDE, 3)};
            List<Long> ids = dao.loadListBySqlQuery(query, params);
            if (ids != null ? !ids.isEmpty() : false) {
                nameQueri = "YvsComptaCaissePieceVente.findSumByVentesStatut";
                champ = new String[]{"ventes", "statut"};
                val = new Object[]{ids, Constantes.STATUT_DOC_PAYER};
                Double valeur = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                entete.setAvanceCommande(valeur != null ? valeur : 0);
            }
            entete.setVersementAttendu(dao.loadVersementAttendu(entete.getUsers().getId(), entete.getDateEntete(), entete.getDateEntete()));
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
        ManagedClient s = (ManagedClient) giveManagedBean(ManagedClient.class
        );
        if (s
                != null) {
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

    public boolean validEtapeFacture(YvsComDocVentes current, DocVente vente, YvsUsersAgence users, YvsWorkflowValidFactureVente etape, DaoInterfaceLocal dao) {
        if (!asDroitValideEtape(etape.getEtape(), users.getUsers())) {
            openNotAcces();
        } else {
            //contrôle la cohérence des dates
            if (current != null ? (current.getId() != null ? current.getId() < 1 : true) : true) {
                current = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{vente.getId()});
            }
            if (vente != null ? vente.getId() < 1 : true) {
                vente = UtilCom.buildBeanDocVente(current);
            }
            etape.setFactureVente(current);
            int idx = vente.getEtapesValidations().indexOf(etape);
            if (idx >= 0) {
                //cas de la validation de la dernière étapes
                if (etape.getEtapeSuivante() == null) {
                    if (validerOrder(current)) {
                        etape.setEtapeValid(true);
                        etape.setMotif(null);
                        etape.setEtapeActive(false);
                        etape.setDateUpdate(new Date());
                        if (vente.getEtapesValidations().size() > (idx + 1)) {
                            vente.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                        }
                        dao.update(etape);
                        current.setEtapeValide(current.getEtapeValide() + 1);
                        if (documents != null ? documents.contains(current) : false) {
                            int idx_ = documents.indexOf(current);
                            documents.get(idx_).setEtapeValide(current.getEtapeValide());
                        }
                        update("blog_form_contenu_facture_vente");
                        update("blog_form_cout_facture_vente");
                        update("form_mensualite_facture_vente");
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
                    if (vente.getEtapesValidations().size() > (idx + 1)) {
                        vente.getEtapesValidations().get(idx + 1).setEtapeActive(true);
                    }
                    dao.update(etape);

                    vente.setStatut(Constantes.ETAT_ENCOURS);
                    current.setStatut(Constantes.ETAT_ENCOURS);
                    current.setDateUpdate(new Date());
                    current.setEtapeValide(current.getEtapeValide() + 1);
                    dao.update(current);
                    vente.setEtapeValide(current.getEtapeValide());
                    if (documents != null ? documents.contains(current) : false) {
                        int idx_ = documents.indexOf(current);
                        documents.get(idx_).setEtapeValide(current.getEtapeValide());
                        documents.get(idx_).setStatut(current.getStatut());
                    }
                    getInfoMessage("Validation effectué avec succès !");
                    update("blog_form_contenu_facture_vente");
                    update("blog_form_cout_facture_vente");
                    update("form_mensualite_facture_vente");
                    update("data_livraison_facture_vente");
                    update("blog_commercial_vente");
                    return true;
                }
            } else {
                getErrorMessage("Impossible de continuer !");
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
                update("blog_form_contenu_facture_vente");
                update("blog_form_cout_facture_vente");
                update("form_mensualite_facture_vente");
                update("data_livraison_facture_vente");
                update("blog_commercial_vente");
                return true;
            } else {
                getErrorMessage("Impossible de continuer !");
            }
        }
        return false;
    }

    public boolean validEtapeFacture(YvsWorkflowValidFactureVente etape) {
        if (_saveNew(false)) {
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

        if (dao.isComptabilise(selectDoc.getId(), Constantes.SCR_AVOIR_VENTE)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return;
            }

        }

        suspend = selectDoc.getStatut().equals(Constantes.ETAT_ANNULE) ? false : suspend;
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (dao.isComptabilise(selectDoc.getId(), Constantes.SCR_VENTE)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return;
                }

            }
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
                        dao.update(selectDoc);

                        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                        if (w != null) {
                            w.unComptabiliserVente(selectDoc, false);
                        }
                    }
                } else {
                    for (YvsComDocVentes d : l) {
                        if (d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                            getErrorMessage("Impossible d'annuler cet ordre car il possède un document valide déja valide");
                            return;
                        }
                    }
                    openDialog(suspend ? "dlgConfirmRefuser" : "dlgConfirmCancel");
                }
            } else {
                getErrorMessage("Ce document est deja livré");
            }
        }
        update("blog_form_contenu_facture_vente");
        update("blog_form_cout_facture_vente");
        update("form_mensualite_facture_vente");
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
            List<Integer> ids = decomposeSelection(tabIds);
            if (!ids.isEmpty()) {
                if (trancheChange != null ? trancheChange.getId() < 1 : true) {
                    getErrorMessage("Vous devez precisez la tranche horaire");
                    return;
                }
                YvsComDocVentes y;
                for (Integer id : ids) {
                    if (id != -1 ? documents.size() > id : false) {
                        y = documents.get(id);
                        y.setTrancheLivrer(new YvsGrhTrancheHoraire(trancheChange.getId()));
                        dao.update(y);
                    }
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
            } else {
                for (YvsComContenuDocVente y : docVente.getContenus()) {
                    m.changeDepotContenu(y, false);
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
        ParametreRequete p = new ParametreRequete("y.docVente.numDoc", "reference", null);
        if (reference != null ? reference.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "reference", reference + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.numDoc)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.numeroExterne)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.nomClient)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.codeClient)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.client.nom)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.enteteDoc.creneau.users.codeUsers)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.docVente.enteteDoc.creneau.users.nomUsers)", "reference", reference.toUpperCase() + "%", "LIKE", "OR"));
        }
        p_contenu.addParam(p);
        loadContenus(true, true);
    }

    public void addParamStatuts() {
        ParametreRequete p = new ParametreRequete("y.docVente.statut", "statut", null);
        if (statutContenu != null ? statutContenu.trim().length() > 0 : false) {
            p = new ParametreRequete("y.docVente.statut", "statut", statutContenu, "=", "AND");
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
        update("data_contenu_facture_vente");
        update("tabview_facture_vente:data_contenu_facture_vente");
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
                update("data_contenu_facture_vente");
                update("tabview_facture_vente:data_contenu_facture_vente");
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
}
