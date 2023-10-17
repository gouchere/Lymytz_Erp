/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import lymytz.navigue.Navigations;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.ArticlePack;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.UniteMesure;
import yvs.base.tiers.Profil;
import yvs.base.tiers.Tiers;
import yvs.commercial.ModeleReference;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.DocAchat;
import yvs.commercial.achat.ManagedBonAvoirAchat;
import yvs.commercial.stock.DocStock;
import yvs.comptabilite.tresorerie.DocCaissesDivers;
import yvs.commercial.vente.DocVente;
import yvs.connexion.Loggin;
import yvs.converter.ConverterNumber;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Fonctions;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComParametreAchat;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.achat.YvsComContenuDocAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.achat.YvsComFicheApprovisionnement;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.commercial.stock.YvsComContenuDocStock;
import yvs.entity.commercial.stock.YvsComDocStocks;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.base.YvsBaseTableConversion;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.compta.divers.YvsComptaCaisseMensualiteDocDivers;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.client.YvsComContratsClient;
import yvs.entity.commercial.commission.YvsComCommissionCommerciaux;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.ration.YvsComDocRation;
import yvs.entity.commercial.rrr.YvsComGrilleRemise;
import yvs.entity.commercial.rrr.YvsComRemise;
import yvs.entity.commercial.stock.YvsComReservationStock;
import yvs.entity.commercial.vente.YvsComRemiseDocVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaCreditClient;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.compta.YvsComptaPhaseReglementAutorisation;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.achat.YvsComptaCreditFournisseur;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.salaire.YvsComptaCaissePieceSalaire;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowAutorisationValidDoc;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.production.YvsProdParametre;
import yvs.entity.production.base.YvsProdCreneauEquipeProduction;
import yvs.entity.production.pilotage.YvsProdOrdreFabrication;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.stat.export.YvsStatExportColonne;
import yvs.entity.stat.export.YvsStatExportEtat;
import yvs.entity.users.YvsAutorisationRessourcesPage;
import yvs.entity.users.YvsBaseUsersAcces;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.bean.Employe;
import yvs.grh.bean.mission.Mission;
import yvs.grh.presence.TrancheHoraire;
import yvs.grh.statistique.ContentDuree;
import yvs.init.Initialisation;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.mutuelle.Parametre;
import yvs.parametrage.agence.ManagedAgence;
import yvs.parametrage.entrepot.Depots;
import yvs.parametrage.report.BeanDate;
import yvs.service.IEntitySax;
import yvs.stat.ManagedAccueil;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.users.acces.AccesRessource;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 * @param <S>
 */
//@SessionScoped
//@ManagedBean
public abstract class Managed<T extends Serializable, S extends Serializable> implements Serializable {

    public final Logger log = Logger.getLogger(getClass().getName());
    public String[] champ;
    public Object[] val;
    private boolean disableSave;
    public int numero = 1; //servira à   numéroter les ligne d'un tableau
    public static DecimalFormat nf = new DecimalFormat("#,##0");
    public static DecimalFormat Lnf = new DecimalFormat("#,###,##0.##");
    public static DateFormat dft = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public static DateFormat sdf = new SimpleDateFormat("ddMMyy");
    public static DateFormat dfML = new SimpleDateFormat("EEEE, MMMM yyyy");
    public static DateFormat ldf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
    public static DateFormat formatHeure = new SimpleDateFormat("HH:mm:ss");
    public static DateFormat time = new SimpleDateFormat("HH:mm");
    public static DateFormat formatMonthYear = new SimpleDateFormat("MMM yy");
    public static DateFormat fmdMy = new SimpleDateFormat("dd MMM yyyy");
    public static DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
    public static DateFormat formatDateReverse = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat formatDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static DateFormat formatDateTimeReverse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateFormat formatDay = new SimpleDateFormat("dd");
    public static DateFormat formatJour = new SimpleDateFormat("EEEE");
    public static DateFormat formatDayM = new SimpleDateFormat("dd-MM");
    public static DateFormat formatMonth = new SimpleDateFormat("MM-yyyy");
    public static DateFormat formatMonthString = new SimpleDateFormat("MMMM yyyy");
    public static DateFormat formatMonth_ = new SimpleDateFormat("dd MMM yy");
    public static DateFormat formatYear = new SimpleDateFormat("yyyy");
    public Messages message = new Messages();
    public Fonctions fonction = new Fonctions();

    public Boolean isWarning = ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isWarning"));
    public String modelWarning = ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("modelWarning"));
    public Date debutWarning = ((Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("debutWarning"));
    public Object idsWarning = ((Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idsWarning"));
    public String natureWarning = ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("natureWarning"));

    public YvsAgences currentAgence = ((YvsAgences) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("agenc"));
    public YvsUsersAgence currentUser = ((YvsUsersAgence) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
    public YvsBaseParametre currentParam = ((YvsBaseParametre) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("paramBase"));
    public YvsParametreGrh paramGrh = ((YvsParametreGrh) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("paramGrh"));
    public YvsComptaParametre paramCompta = ((YvsComptaParametre) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("paramCompta"));
    public YvsComParametre paramCommercial = ((YvsComParametre) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("paramCom"));
    public YvsProdParametre paramProduction = ((YvsProdParametre) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("paramProd"));
    public YvsBasePointVente currentPoint = ((YvsBasePointVente) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("point"));
    public YvsBaseDepots currentDepot = ((YvsBaseDepots) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("depot"));
    public List<YvsComCreneauHoraireUsers> currentPlanning = ((List<YvsComCreneauHoraireUsers>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("planning"));
    public YvsComCreneauDepot currentCreneauDepot = ((YvsComCreneauDepot) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("creneauDepot"));
    public YvsComCreneauPoint currentCreneauPoint = ((YvsComCreneauPoint) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("creneauPoint"));
    public YvsMutMutuelle currentMutuel = ((YvsMutMutuelle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mutuel"));
    public YvsBaseExercice currentExo = ((YvsBaseExercice) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("exo"));
    public YvsNiveauAcces currentNiveau = ((YvsNiveauAcces) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("nivoAcces"));
    public YvsProdCreneauEquipeProduction currentCreneauEquipe = ((YvsProdCreneauEquipeProduction) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("creneauEquipe"));

    public Long NBRE_ONLINE = ((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("nbreOnline"));
    public List<Object[]> WORKFLOWS = ((List<Object[]>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("workflow"));
    public List<Object[]> WARNINGS = ((List<Object[]>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("warning"));
    public List<Object[]> INFORMATIONS = ((List<Object[]>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("information"));
    //*****Programmation des droits d'accès**/

    @EJB
    public DaoInterfaceLocal dao;
    private String texte;
    private String defaultButton;
    private String styleError;
    private String search;
    @Resource(name = "lymytz_erp", mappedName = "lymytz_erp")
    public DataSource ds;
    public IEntitySax IEntitiSax = new IEntitySax();

    private int currentPage = 1; //numéro de la page courante
    private int offset = -1; //début de la selection pour le parcours linéaire des éléments d'une table
    private int totalPage;// nombre total de page
    private int firtResult = 0; //position dans les réponses
    private int nbMax = 15;//nombre de ligne par page;
    private boolean disableNext, disablePrev;
    public long nombreEmploye;
    public int optionSearch = 1, optionSearch2 = 2;
    private String dlgNotAccesAction = "Vous ne disposez pas de privillège pour effectuer cette opération !";
    private String dlgAction = "Voulez-vous continuer ?";
    public FacesContext context = FacesContext.getCurrentInstance();
    //Parametre de recherche
    public String statut_;
    public List<YvsAgences> agences_ = new ArrayList<>();
    public List<YvsSocietes> societes_ = new ArrayList<>();
    public List<YvsBaseDepots> depots_ = new ArrayList<>();
    public List<YvsGrhTrancheHoraire> tranches_point_ = new ArrayList<>();
    public List<YvsGrhTrancheHoraire> tranches_depot_ = new ArrayList<>();
    public List<YvsBasePointVente> points_ = new ArrayList<>();
    public List<YvsUsers> userss_ = new ArrayList<>();
    public String nameQueriCount, nameQueri;

    public String idsSearch;
    public PaginatorResult<S> paginator = new PaginatorResult<>();
    protected boolean searchAutomatique = true;
    protected long agence_;
    public int idebut = 0, ifin = nbMax;
    public long imax = 15;
    private boolean toogleDisplayId;

    private long nbMissionEnCours = 0;
    private long nbCongeEnCours = 0;
    public Parametre parametreMutuelle;
    private YvsBaseModeReglement modeByEspece = null;

    protected boolean resetAfterSave, memoriseActionAfterSave;
    public String valuePrint = "";

    protected static Managed instance;
    protected static List<Long> listIdAgences = new ArrayList<>();

    private SelectItem[] paginations = {
        new SelectItem((int) 0, "@"),
        new SelectItem((int) 5, "5"),
        new SelectItem((int) 10, "10"),
        new SelectItem((int) 15, "15"),
        new SelectItem((int) 25, "25"),
        new SelectItem((int) 50, "50"),
        new SelectItem((int) 100, "100"),
        new SelectItem((int) 150, "150"),
        new SelectItem((int) 200, "200"),
        new SelectItem((int) 500, "500"),
        new SelectItem((int) 1000, "1000"),
        new SelectItem((int) 5000, "1000+")
    };

    public Managed getInstance() {
        return instance;
    }

    public void setInstance(Managed instance) {
        Managed.instance = instance;
        update("blog-analyse_query");
    }

    public boolean isSearchAutomatique() {
        return searchAutomatique;
    }

    public void setSearchAutomatique(boolean searchAutomatique) {
        this.searchAutomatique = searchAutomatique;
    }

    public List<Long> getListIdAgences() {
        return listIdAgences;
    }

    public void setListIdAgences(List<Long> listIdAgences) {
        Managed.listIdAgences = listIdAgences;
    }

    public String getIdsSearch() {
        return idsSearch;
    }

    public void setIdsSearch(String idsSearch) {
        this.idsSearch = idsSearch;
    }

    public String getValuePrint() {
        return valuePrint;
    }

    public void setValuePrint(String valuePrint) {
        this.valuePrint = valuePrint;
    }

    public String getDlgNotAccesAction() {
        return dlgNotAccesAction;
    }

    public void setDlgNotAccesAction(String dlgNotAccesAction) {
        this.dlgNotAccesAction = dlgNotAccesAction;
    }

    public YvsBaseModeReglement getModeByEspece() {
        return modeByEspece;
    }

    public String getDlgAction() {
        return dlgAction;
    }

    public void setDlgAction(String dlgAction) {
        this.dlgAction = dlgAction;
    }

    public void setModeEspece(YvsBaseModeReglement modeByEspece) {
        this.modeByEspece = modeByEspece;
    }

    public YvsProdCreneauEquipeProduction getCurrentCreneauEquipe() {
        return currentCreneauEquipe;
    }

    public void setCurrentCreneauEquipe(YvsProdCreneauEquipeProduction currentCreneauEquipe) {
        this.currentCreneauEquipe = currentCreneauEquipe;
    }

    public List<Object[]> getWARNINGS() {
        return WARNINGS;
    }

    public boolean isResetAfterSave() {
        return resetAfterSave;
    }

    public void setResetAfterSave(boolean resetAfterSave) {
        this.resetAfterSave = resetAfterSave;
    }

    public boolean isMemoriseActionAfterSave() {
        return memoriseActionAfterSave;
    }

    public void setMemoriseActionAfterSave(boolean memoriseActionAfterSave) {
        this.memoriseActionAfterSave = memoriseActionAfterSave;
    }

    public void setWARNINGS(List<Object[]> WARNINGS) {
        this.WARNINGS = WARNINGS;
    }

    public List<Object[]> getINFORMATIONS() {
        return INFORMATIONS;
    }

    public void setINFORMATIONS(List<Object[]> INFORMATIONS) {
        this.INFORMATIONS = INFORMATIONS;
    }

    public List<Object[]> getWORKFLOWS() {
        return WORKFLOWS;
    }

    public void setWORKFLOWS(List<Object[]> WORKFLOWS) {
        this.WORKFLOWS = WORKFLOWS;
    }

    public Long getNBRE_ONLINE() {
        return NBRE_ONLINE;
    }

    public void setNBRE_ONLINE(Long NBRE_ONLINE) {
        this.NBRE_ONLINE = NBRE_ONLINE;
    }

    public long getNbCongeEnCours() {
        return nbCongeEnCours;
    }

    public void setNbCongeEnCours(long nbCongeEnCours) {
        this.nbCongeEnCours = nbCongeEnCours;
    }

    public long getNbMissionEnCours() {
        return nbMissionEnCours;
    }

    public void setNbMissionEnCours(long nbMissionEnCours) {
        this.nbMissionEnCours = nbMissionEnCours;
    }

    public DateFormat getLdf() {
        return ldf;
    }

    public PaginatorResult<S> getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorResult<S> paginator) {
        this.paginator = paginator;
    }

    public List<Integer> listIdSubDepartement = new ArrayList<>();

    public SelectItem[] getPaginations() {
        return paginations;
    }

    public void setPaginations(SelectItem[] paginations) {
        this.paginations = paginations;
    }

    public boolean isDisableSave() {
        return disableSave;
    }

    public void setDisableSave(boolean disableSave) {
        this.disableSave = disableSave;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public void resetText() {
        texte = null;
    }

    public String getDefaultButton() {
        return defaultButton;
    }

    public void setDefaultButton(String defaultButton) {
        this.defaultButton = defaultButton;
    }

    public YvsUsersAgence getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(YvsUsersAgence currentUser) {
        this.currentUser = currentUser;
    }

    public YvsBasePointVente getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(YvsBasePointVente currentPoint) {
        this.currentPoint = currentPoint;
    }

    public YvsBaseDepots getCurrentDepot() {
        return currentDepot;
    }

    public void setCurrentDepot(YvsBaseDepots currentDepot) {
        this.currentDepot = currentDepot;
    }

    public YvsComCreneauDepot getCurrentCreneauDepot() {
        return currentCreneauDepot;
    }

    public void setCurrentCreneauDepot(YvsComCreneauDepot currentCreneauDepot) {
        this.currentCreneauDepot = currentCreneauDepot;
    }

    public YvsMutMutuelle getCurrentMutuel() {
        return currentMutuel;
    }

    public void setCurrentMutuel(YvsMutMutuelle currentMutuel) {
        this.currentMutuel = currentMutuel;
    }

    public YvsBaseExercice getCurrentExo() {
        return currentExo;
    }

    public void setCurrentExo(YvsBaseExercice currentExo) {
        this.currentExo = currentExo;
    }

    public YvsAgences getCurrentAgence() {
        return currentAgence;
    }

    public YvsSocietes getCurrentScte() {
        return currentAgence != null ? currentAgence.getSociete() : null;
    }

    public String getStatut_() {
        return statut_;
    }

    public void setStatut_(String statut_) {
        this.statut_ = statut_;
    }

    public List<YvsSocietes> getSocietes_() {
        return societes_;
    }

    public void setSocietes_(List<YvsSocietes> societes_) {
        this.societes_ = societes_;
    }

    public List<YvsBaseDepots> getDepots_() {
        return depots_;
    }

    public void setDepots_(List<YvsBaseDepots> depots_) {
        this.depots_ = depots_;
    }

    public List<YvsGrhTrancheHoraire> getTranches_point_() {
        return tranches_point_;
    }

    public void setTranches_point_(List<YvsGrhTrancheHoraire> tranches_point_) {
        this.tranches_point_ = tranches_point_;
    }

    public List<YvsBasePointVente> getPoints_() {
        return points_;
    }

    public void setPoints_(List<YvsBasePointVente> points_) {
        this.points_ = points_;
    }

    public List<YvsUsers> getUserss_() {
        return userss_;
    }

    public void setUserss_(List<YvsUsers> userss_) {
        this.userss_ = userss_;
    }

    public List<YvsAgences> getAgences_() {
        return agences_;
    }

    public void setAgences_(List<YvsAgences> agences_) {
        this.agences_ = agences_;
    }

    public long getAgence_() {
        return agence_;
    }

    public void setAgence_(long agence_) {
        this.agence_ = agence_;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public DateFormat getFmdMy() {
        return fmdMy;
    }

    public DateFormat getFormatDate() {
        return formatDate;
    }

    public DateFormat getFormatMonth() {
        return formatMonth;
    }

    public DateFormat getFormatMonthString() {
        return formatMonthString;
    }

    public void doNothing() {

    }

    public void loadAll() {
        dao.loadInfos(currentAgence.getSociete(), currentAgence, currentUser, null, currentPoint, currentExo);
    }

    public abstract boolean controleFiche(T bean);

    public boolean saveNew() {
        setDisableSave(true);
        //save intérêt
        succes();
        return true;
    }

    public void actionOpenOrResetAfter(Object managedBean) {
        Constantes.managedBean = managedBean;
        ManagedAccueil w = (ManagedAccueil) giveManagedBean(ManagedAccueil.class);
        if (w != null) {
            w.setMemoriseActionAfterSave(memoriseActionAfterSave);
        }
        if (memoriseActionAfterSave ? resetAfterSave : false) {
            resetFiche();
        }
        if (!memoriseActionAfterSave) {
            if (managedBean != null) {
                update("main-action-after");
                openDialog("dlgConfirmActionAfter");
            }
        }
    }

    public void resetFicheAfterSave(boolean resetAfterSave) {
        if (Constantes.managedBean != null) {
            Managed w = (Managed) Constantes.managedBean;
            w.setResetAfterSave(resetAfterSave);
            w.setMemoriseActionAfterSave(memoriseActionAfterSave);
            if (resetAfterSave) {
                (w).resetFiche();
            }
        }
    }

    public abstract void resetFiche();

//    public void resetFiche() {
//
//    }
    public void resetPage() {

    }

    public void changeView() {

    }

    public boolean isToogleDisplayId() {
        return toogleDisplayId;
    }

    public void setToogleDisplayId(boolean toogleDisplayId) {
        this.toogleDisplayId = toogleDisplayId;
    }

    public Parametre getParametreMutuelle() {
        return parametreMutuelle;
    }

    public void setParametreMutuelle(Parametre parametreMutuelle) {
        this.parametreMutuelle = parametreMutuelle;
    }

    public T recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void populateView(T bean) {

    }

    public void deleteBean() {

    }

    public void deleteBean(S y) {

    }

    public void deleteBean(S y, boolean delete) {

    }

    public void updateBean() {

    }

    public void selectOnView(T bean) {

    }

    public void onSelectObject(S y) {

    }

    public void onSelectDistant(S y) {

    }

    public abstract void loadOnView(SelectEvent ev);

    public abstract void unLoadOnView(UnselectEvent ev);

    public void displayIdTab() {
        toogleDisplayId = !toogleDisplayId;
    }

    public void loadParametreSession() {
        loadParametreBase();
        loadParametreCom();
        loadParametreCompta();
        loadParametreGrh();
        loadParametreProd();
    }

    public void loadParametreBase() {
        currentParam = (YvsBaseParametre) dao.loadOneByNameQueries("YvsBaseParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramBase", currentParam);
    }

    public void loadParametreCom() {
        paramCommercial = (YvsComParametre) dao.loadOneByNameQueries("YvsComParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramCom", paramCommercial);
    }

    public void loadParametreCompta() {
        paramCompta = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramCompta", paramCompta);
    }

    public void loadParametreGrh() {
        paramGrh = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramGrh", paramGrh);
    }

    public void loadParametreProd() {
        paramProduction = (YvsProdParametre) dao.loadOneByNameQueries("YvsProdParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paramProd", paramProduction);
    }

    public void resetFiche(Object o) {
        Class t = o.getClass();
        Field[] lf = t.getDeclaredFields();
        for (Field f : lf) {
            f.setAccessible(true);
            Class type = f.getType();
            try {
                switch (type.getSimpleName()) {
                    case "long":
                    case "Long":
                        f.set(o, 0);
                        break;
                    case "boolean":
                    case "Boolean":
                        if (f.getName().equals("supp") || f.getName().equals("update") || f.getName().equals("new_") || f.getName().equals("cloturer_")) {
                            f.set(o, false);
                        } else {
                            f.set(o, true);
                        }
                        break;
                    case "double":
                    case "Double":
                        f.set(o, 0);
                        break;
                    case "int":
                    case "Integer":
                        f.set(o, 0);
                        break;
                    case "short":
                        f.set(o, (short) 0);
                        break;
                    case "List":
                    case "ArrayList":
                    case "Collections":
                        String methodName = "clear";
                        try {
                            Object fieldValue = f.get(o);
                            if (fieldValue != null) {
                                Method method = fieldValue.getClass().getDeclaredMethod(methodName, new Class[]{});
                                method.invoke(fieldValue); // explicit cast
                            }
                        } catch (NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "Date":
                        f.set(o, new Date());
                        break;
                    case "char":
                    case "Character":
                        f.set(o, ' ');
                        break;
                    case "String":
                        f.set(o, null);
                        break;
                    default:
                        if (f.getType().getClass().equals(Class.class)) {
                            try {
                                f.set(o, Class.forName(type.getName()).newInstance());
                            } catch (InstantiationException | ClassNotFoundException ex) {
                                Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
                                f.set(o, null);
                            }
                        } else {
                            f.set(o, null);
                        }
                        break;
                }

            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setDisableSave(false);
    }

    public void resetFiche(Object o, String str) {
        resetFiche(o);
        update(str);
    }

    /**
     * copie les propriétés de l'objet source dans l'objet cible sans en changer la référence
     *
     * @param cible
     * @param source
     */
    public void cloneObject(Object cible, Object source) {
        Field[] lf0 = cible.getClass().getDeclaredFields();
        Class t = source.getClass();
        Field[] lf = t.getDeclaredFields();
        int i = 0;
        for (Field f : lf) {
            String[] tab = f.getName().split(".");
            if (tab.length > 0 ? !tab[tab.length - 1].equals("serialVersionUID") : true) {
                try {
                    lf0[i].setAccessible(true);
                    f.setAccessible(true);
                    lf0[i].set(cible, f.get(source));
                    i++;
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void succes() {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succes !", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getException(String message, Throwable ex) {
        log.log(Level.SEVERE, message, ex);
    }

    public void getMessage(String message, FacesMessage.Severity severity) {
        FacesMessage msg = new FacesMessage(severity, message, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getErrorMessage(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getFatalMessage(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, message, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getFatalMessage(String message, String detail) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, message, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getErrorMessage(String message, String detail) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getInfoMessage(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getInfoMessage(String message, String details) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, details);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getWarningMessage(String message) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, message, "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getWarningMessage(String message, String details) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, message, details);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getErrorMessageDefaultStock(String article, String depot, Date date) {
        getErrorMessage("L'article '" + article + "' est insuffisant en stock pour effectuer cette action", " Dépôt :" + depot + " Date :" + formatMonth_.format(date));
    }

    public void update(String id) {
        try {
            RequestContext.getCurrentInstance().update(id);
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void execute(String id) {
        try {
            RequestContext.getCurrentInstance().execute(id);
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUserOnLine() {
        String str;
        str = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("codeUser");
        return str;
    }

    public Users getUsersOnLine() {
        Users user = UtilUsers.buildBeanUsers(currentUser.getUsers());
        return user;
    }

    public int getLongueurCompte() {
//        int t;
//        t = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("longueurCompte");
//        return (t == 0) ? 9 : t;
        return 8;
    }

    public String getDevise() {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("devise");
    }

    public String getStyleError() {
        return styleError;
    }

    public void setStyleError() {
        this.styleError = "style_error";
    }

    public void clearStyleError() {
        this.styleError = "";
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getOptionSearch() {
        return optionSearch;
    }

    public void setOptionSearch(int optionSearch) {
        this.optionSearch = optionSearch;
    }

    public int getOptionSearch2() {
        return optionSearch2;
    }

    public void setOptionSearch2(int optionSearch2) {
        this.optionSearch2 = optionSearch2;
    }

    public T initValue(Class clas, int i, String str) {
        T o = null;
        Field[] lf = clas.getDeclaredFields();
        for (Field f : lf) {
            f.setAccessible(true);
            Class type = f.getType();
            try {
                try {
                    o = (T) clas.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
                }
                switch (type.getSimpleName()) {
                    case "long":
                        f.set(o, 10 + i);
                        break;
                    case "boolean":
                        if (f.getName().equals("actif")) {
                            f.set(o, true);
                        } else {
                            f.set(o, false);
                        }
                        break;
                    case "String":
                        f.set(o, "CLIENT" + i);
                        System.out.println(f.get(o));
                        break;
                    case "Date":
                        f.set(o, new Date());
                        break;
                    default:
                        f.set(o, new Object());
                        break;
                }

            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return o;
    }

    public void openDialog(String widget) {
        try {
            RequestContext.getCurrentInstance().execute(widget + ".show()");
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openNotAcces() {
        try {
            RequestContext.getCurrentInstance().execute("dlgNotAcces.show()");
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openNotAccesAction(String message) {
        ManagedAccueil w = (ManagedAccueil) giveManagedBean(ManagedAccueil.class);
        if (w != null) {
            w.setDlgNotAccesAction(message);
        }
        RequestContext.getCurrentInstance().execute("dlgNotAccesAction.show()");
        update("frm-dlgNotAccesAction");
    }

    public void openAction(String message) {
        ManagedAccueil w = (ManagedAccueil) giveManagedBean(ManagedAccueil.class);
        if (w != null) {
            w.setDlgAction(message);
        }
        RequestContext.getCurrentInstance().execute("dlgAction.show()");
        update("frm-dlgAction");
    }

    public void openNotAccesByCode() {
        try {
            RequestContext.getCurrentInstance().execute("dlgNotAccesByCode.show()");
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeDialog(String widget) {
        try {
            RequestContext.getCurrentInstance().execute(widget + ".hide()");
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh(String widget) {
        try {
            RequestContext.getCurrentInstance().execute(widget + ".update()");
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void openDialogView(String outcome) {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", true);
        options.put("width", 700);
        options.put("contentHeight", 500);
        options.put("closeOnescape", true);
        RequestContext.getCurrentInstance().execute(outcome + ".show()");
    }

    public void closeDialogView(Object object) {
        try {
            RequestContext.getCurrentInstance().execute(object + ".hide()");
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //fabrique de table
    public void factoryDataTable(List<String> colum) {
        List<T> l = null;
        DataTable td = new DataTable();
        td.setValue(l);
        for (String str : colum) {
            Column c = new Column();
            c.setHeaderText(str);
            td.getColumns().add(c);
        }
    }

    public DataSource getDs() {
        return ds;
    }

    public void setDs(DataSource ds) {
        this.ds = ds;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getFirtResult() {
        return firtResult;
    }

    public void setFirtResult(int firtResult) {
        this.firtResult = firtResult;
    }

    public int getNbMax() {
        return nbMax;
    }

    public void setNbMax(int nbMax) {
        this.nbMax = nbMax;
    }

    public boolean isDisableNext() {
        return disableNext;
    }

    public void setDisableNext(boolean disableNext) {
        this.disableNext = disableNext;
    }

    public boolean isDisablePrev() {
        return disablePrev;
    }

    public void setDisablePrev(boolean disablePrev) {
        this.disablePrev = disablePrev;
    }
    private List<YvsComptaPiecesComptable> contentCompta = new ArrayList<>();

    public List<YvsComptaPiecesComptable> getContentCompta() {
        return contentCompta;
    }

    public void setContentCompta(List<YvsComptaPiecesComptable> contentCompta) {
        this.contentCompta = contentCompta;
    }

    public Object giveManagedBean(String name) {
        return (Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(name);
    }

    public Object giveManagedBean(Class classe) {
        String st = classe.getSimpleName();
        String f = st.substring(0, 1);
        if (st != null) {
            return (Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(f.toLowerCase().concat(st.substring(1)));
        }
        return null;
    }

    public Object giveObjectOnView(String id) {
        return (Object) FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
    }

    public int getIdebut() {
        return idebut;
    }

    public void setIdebut(int idebut) {
        this.idebut = idebut;
    }

    public int getIfin() {
        return ifin;
    }

    public void setIfin(int ifin) {
        this.ifin = ifin;
    }

    public long getImax() {
        return imax;
    }

    public void setImax(long imax) {
        this.imax = imax;
    }

    public String buildRequeteExport(String report, List<Long> donnees) {
        return buildRequeteExport(report, donnees.toArray(new Long[]{}));
    }

    public String buildRequeteExport(String report, Long[] donnees) {
        String query = null;
        champ = new String[]{"reference", "societe"};
        val = new Object[]{report, currentAgence.getSociete()};
        YvsStatExportEtat etat = (YvsStatExportEtat) dao.loadOneByNameQueries("YvsStatExportEtat.findByReference", champ, val);
        if (etat != null ? etat.getId() > 0 : false) {
            if (etat.getTypeFormule().equals('S')) {
                champ = new String[]{"etat", "integrer"};
                val = new Object[]{etat, true};
                List<YvsStatExportColonne> colonnes = dao.loadNameQueries("YvsStatExportColonne.findByIntegrer", champ, val);
                if (colonnes != null ? !colonnes.isEmpty() : false) {
                    String select = "*";
                    String from = etat.getTablePrincipal();
                    String order = "";
                    String where = " WHERE " + etat.getTablePrincipal() + "." + etat.getColonnePrincipal() + " IN (-1";
                    boolean with_orderby = etat.getOrderBy() != null ? etat.getOrderBy().trim().length() > 0 : false;
                    if (with_orderby) {
                        order = " ORDER BY " + etat.getOrderBy();
                    }

                    boolean _fisrt = true;
                    for (YvsStatExportColonne colonne : colonnes) {
                        if (colonne.getVisible()) {
                            if (_fisrt) {
                                select = (colonne.getColonneDate() ? "to_char(" + colonne.getTableName() + "." + colonne.getColonne() + ", '" + colonne.getFormatDate() + "')" : colonne.getTableName() + "." + colonne.getColonne());
                            } else {
                                select += ", " + (colonne.getColonneDate() ? "to_char(" + colonne.getTableName() + "." + colonne.getColonne() + ", '" + colonne.getFormatDate() + "')" : colonne.getTableName() + "." + colonne.getColonne());
                            }
                            _fisrt = false;
                        }
                        if (colonne.getContrainte()) {
                            String jointure = colonne.getSensContrainte().equals('N') ? colonne.getTableNameLiee() + " ON " + colonne.getTableName() + "." + colonne.getColonne() + " = " + colonne.getTableNameLiee() + "." + colonne.getColonneLiee()
                                    : colonne.getTableName() + " ON " + colonne.getTableName() + "." + colonne.getColonne() + " = " + colonne.getTableNameLiee() + "." + colonne.getColonneLiee();
                            if (!from.contains(jointure)) {
                                from += " LEFT JOIN " + jointure;
                            }
                        }
                        if (!with_orderby) {
                            if (colonne.getOrderBy() != null ? String.valueOf(colonne.getOrderBy()).trim().length() > 0 : false) {
                                if (order != null ? order.trim().length() < 1 : true) {
                                    order = " ORDER BY " + colonne.getTableName() + "." + colonne.getColonne() + " " + (colonne.getOrderBy().equals('D') ? "DESC" : "ASC");
                                } else {
                                    order += ", " + colonne.getTableName() + "." + colonne.getColonne() + " " + (colonne.getOrderBy().equals('D') ? "DESC" : "ASC");
                                }
                            }
                        }
                    }
                    if (donnees != null ? donnees.length > 0 : false) {
                        for (Long p : donnees) {
                            where += ", " + p;
                        }
                        where += ")";
                        query = "SELECT " + select + " FROM " + from + "" + where + " " + (order.trim().length() > 0 ? order : "");
                    }
                }
            } else {
                if (donnees != null ? donnees.length > 0 : false) {
                    String where = " " + etat.getTablePrincipal() + "." + etat.getColonnePrincipal() + " IN (-1";
                    if (etat.getFormule().contains("?")) {
                        for (Long p : donnees) {
                            where += ", " + p;
                        }
                        where += ")";
                        query = etat.getFormule().replace("?", where);
                    }
                }
            }
        }
        return query;
    }

    public String executeExport(String report, List<Long> donnees) {
        return executeExport(report, donnees.toArray(new Long[]{}));
    }

    public String executeExport(String report, Long[] donnees) {
        String result = null;
        try {
            champ = new String[]{"reference", "societe"};
            val = new Object[]{report, currentAgence.getSociete()};
            YvsStatExportEtat etat = (YvsStatExportEtat) dao.loadOneByNameQueries("YvsStatExportEtat.findByCode", champ, val);
            if (etat != null ? etat.getId() > 0 : false) {
                if (etat.getFormat().equals(Constantes.FILE_PDF)) {
                    String ids = "";
                    for (long id : donnees) {
                        if (ids == "") {
                            ids = id + "";
                        } else {
                            ids += "," + id;
                        }
                    }
                    Map<String, Object> param = new HashMap<>();
                    String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                    param.put("AGENCE", currentAgence.getId().intValue());
                    param.put("NAME_AUTEUR", currentUser.getUsers().getNomUsers());
                    param.put("TITLE_RAPPORT", "LISTE DES " + giveNameExport(etat.getCode()).toUpperCase());
                    param.put("LOGO", returnLogo());
                    param.put("REPORT", report);
                    param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                    param.put("IDS", ids);
                    executeReport("exportation", param);
                    succes();
                } else {
                    String query = buildRequeteExport(report, donnees);
                    if (query != null ? query.trim().length() > 0 : false) {
                        try {
                            List<Object> data = dao.loadListBySqlQuery(query, new yvs.dao.Options[]{});
                            if (data == null) {
                                result = "Erreur sur la requete";
                            } else {
                                if (data != null ? !data.isEmpty() : false) {
                                    String file = Initialisation.getCheminAllDoc() + Initialisation.FILE_SEPARATOR + etat.getFileName();
                                    switch (etat.getFormat()) {
                                        case Constantes.FILE_CSV:
                                            file += ".csv";
                                            Util.createFileCSVByObject(file, data);
                                            succes();
                                            break;
                                        default:
                                            file += ".txt";
                                            Util.createFileTXTByObject(file, data, etat.getSeparateur());
                                            succes();
                                            break;
                                    }
                                    Util.getDownloadFile(file, etat.getFileName());
                                }
                            }
                        } catch (IOException ex) {
                            getException("Error ", ex);
                            result = ex.getMessage();
                        }
                    }
                }
            } else {
                getErrorMessage("Aucun modèle d'exportation pour ce document n'a été trouvé");
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
            result = ex.getMessage();
        }
        return result;
    }

    public String executeReport(String report, Map<String, Object> param) {
        return executeReport(report, param, "", false);
    }

    public String executeReport(String report, Map<String, Object> param, boolean print) {
        return executeReport(report, param, "", print);
    }

    public String executeReport(String report, Map<String, Object> param, String nameFile) {
        return executeReport(report, param, nameFile, false);
    }

    public String executeReport(String report, Map<String, Object> param, String nameFile, String extension) {
        return executeReport(report, param, nameFile, extension, false);
    }

    public String executeReport(String report, Map<String, Object> param, String nameFile, boolean print) {
        return executeReport(report, param, nameFile, "pdf", print);
    }

    public String executeReport(String report, Map<String, Object> param, String nameFile, String extension, boolean print) {
        String path = null;
        try {
            byte[] bytes;
            switch (extension) {
                case "xls":
                    bytes = executeReportXls(report, param, nameFile);
                    break;
                default:
                    bytes = executeReportPdf(report, param, nameFile);
                    break;
            }
            if (bytes != null && print) {
                try {
                    String fileName = Util.giveFileName();
                    String destination = FacesContext.getCurrentInstance().getExternalContext().getRealPath(Initialisation.getCheminDownload()) + FILE_SEPARATOR + fileName + "." + extension;
                    File file = new File(destination);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    OutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.flush();
                    path = Initialisation.getCheminDownload() + FILE_SEPARATOR + fileName + "." + extension;
                } catch (IOException ex) {
                    Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
        return path != null ? path.replace("\\", "/") : null;
    }

    public byte[] executeReportXls(String report, Map<String, Object> param, String nameFile) {
        Connection con = null;
        byte[] bytes = null;
        try {
            JasperPrint j = new JasperPrint();
            try {
                ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    //cette methode permet de récupérer le chemin absolu du repertoire report suivant.
                    con = ds.getConnection();

                } catch (SQLException ex) {
                    Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
                }
                File file = new File(ext.getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report"));
                j = JasperFillManager.fillReport(new FileInputStream(new File(file, "" + report + ".jasper")), param, con);//                       
            } catch (FileNotFoundException ex) {
                log.log(Level.SEVERE, null, ex);
            }
            String PATH_REPORT_FILE = FacesContext.getCurrentInstance().getExternalContext().getRealPath(Initialisation.getCheminDownload()) + FILE_SEPARATOR;
            //tableau de byte avec l'objet jasperPrint
            JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setExporterInput(new SimpleExporterInput(j));
//            exporterXLS.getPropertiesUtil().setProperty(JRCrosstab.PROPERTY_IGNORE_WIDTH, "true");
            String destination = report + (nameFile != null ? nameFile.trim().length() > 0 ? "_" + nameFile : "" : "");
            File outputFile = new File(PATH_REPORT_FILE + destination + ".xls");
            exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setIgnoreCellBorder(false);
            configuration.setWhitePageBackground(false);
            configuration.setCollapseRowSpan(false);
            configuration.setRemoveEmptySpaceBetweenRows(false);
            configuration.setRemoveEmptySpaceBetweenColumns(false);
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);//Set configuration as you like it!!
            configuration.setImageBorderFixEnabled(true);
            configuration.setIgnoreGraphics(false);
            exporterXLS.setConfiguration(configuration);
            exporterXLS.exportReport();
            bytes = Util.read(outputFile);
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            /*
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * *
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * *
             * * * * *
             * Pour afficher une boîte de dialogue pour enregistrer le fichier
             * sous le nom rapport.xls * * * * * * * * * * * * * * * * * * * * *
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * * * * * * * * * * * * * * *
             */
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-disposition", "attachment;filename=" + destination + ".xls");
            response.setContentLength(bytes.length);
            try {
                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
            }
            faces.responseComplete();
        } catch (JRException ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                    con = null;
                } catch (SQLException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bytes;
    }

    public byte[] executeReportPdf(String report, Map<String, Object> param, String nameFile) {
        Connection con = null;
        byte[] bytes = null;
        try {
            JasperPrint j = new JasperPrint();
            try {
                ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    //cette methode permet de récupérer le chemin absolu du repertoire report suivant.
                    con = ds.getConnection();

                } catch (SQLException ex) {
                    Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
                }
                File file = new File(ext.getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report"));
                j = JasperFillManager.fillReport(new FileInputStream(new File(file, "" + report + ".jasper")), param, con);//                       
            } catch (FileNotFoundException ex) {
                log.log(Level.SEVERE, null, ex);
            }
            //tableau de byte avec l'objet jasperPrint
            bytes = JasperExportManager.exportReportToPdf(j);
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            /*
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * *
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * *
             * * * * *
             * Pour afficher une boîte de dialogue pour enregistrer le fichier
             * sous le nom rapport.pdf * * * * * * * * * * * * * * * * * * * * *
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * * * * * * * * * * * * * * *
             */
            response.setContentType("application/pdf");
            String destination = report + (nameFile != null ? nameFile.trim().length() > 0 ? "_" + nameFile : "" : "");
            response.addHeader("Content-disposition", "attachment;filename=" + destination + ".pdf");
            response.setContentLength(bytes.length);
            try {
                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
            }
            faces.responseComplete();
        } catch (JRException ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                    con = null;
                } catch (SQLException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bytes;
    }

    public void executeReport(String report, Map<String, Object> param, int height, int width) {
        Connection con = null;
        try {
            JasperPrint j = new JasperPrint();
            try {
                ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    //cette methode permet de récupérer le chemin absolu du repertoire report suivant.
                    con = ds.getConnection();

                } catch (SQLException ex) {
                    Logger.getLogger(Managed.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                File file = new File(ext.getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report"));
                j = JasperFillManager.fillReport(new FileInputStream(new File(file, "" + report + ".jasper")), param, con);//                       
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            //tableau de byte avec l'objet jasperPrint
            j.setPageHeight(height);
            j.setPageWidth(width);
            byte[] bytes = JasperExportManager.exportReportToPdf(j);
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            /*
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * *
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * *
             * * * * *
             * Pour afficher une boîte de dialogue pour enregistrer le fichier
             * sous le nom rapport.pdf * * * * * * * * * * * * * * * * * * * * *
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * * * * * * * * * * * * * * *
             */
            response.setContentType("application/pdf");
            response.addHeader("Content-disposition", "attachment;filename=" + report + ".pdf");
            response.setContentLength(bytes.length);
            try {
                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            faces.responseComplete();

        } catch (JRException ex) {
            Logger.getLogger(Managed.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                    con = null;

                } catch (SQLException ex) {
                    Logger.getLogger(Managed.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void executeReportWithBeanDs(String report, Map<String, Object> param) {
        Connection con = null;
        try {
            JasperPrint j = new JasperPrint();
            try {
                ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
                try {
                    //cette methode permet de récupérer le chemin absolu du repertoire report suivant.
                    con = ds.getConnection();

                } catch (SQLException ex) {
                    Logger.getLogger(Managed.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
                File file = new File(ext.getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report"));
                j = JasperFillManager.fillReport(new FileInputStream(new File(file, "" + report + ".jasper")), param, new JRBeanCollectionDataSource(BeanDate.fillDate()));
            } catch (FileNotFoundException ex) {
                log.log(Level.SEVERE, null, ex);
            }
            //tableau de byte avec l'objet jasperPrint
            byte[] bytes = JasperExportManager.exportReportToPdf(j);
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
            /*
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * *
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * *
             * * * * *
             * Pour afficher une boîte de dialogue pour enregistrer le fichier
             * sous le nom rapport.pdf * * * * * * * * * * * * * * * * * * * * *
             * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
             * * * * * * * * * * * * * * *
             */
            response.setContentType("application/pdf");
            response.addHeader("Content-disposition",
                    "attachment;filename=" + report + ".pdf");
            response.setContentLength(bytes.length);
            try {
                response.getOutputStream().write(bytes);
                response.getOutputStream().flush();
            } catch (IOException ex) {
                log.log(Level.SEVERE, null, ex);
            }
            faces.responseComplete();

        } catch (JRException ex) {
            Logger.getLogger(Managed.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                    con = null;

                } catch (SQLException ex) {
                    Logger.getLogger(Util.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public List<Integer> giveAllSupDepartement(YvsGrhDepartement de) {
        if (de != null) {
            if (!listIdSubDepartement.contains(de.getId())) {
                listIdSubDepartement.add(de.getId());
            }
            for (YvsGrhDepartement d : de.getSubDepartements()) {
                if (!listIdSubDepartement.contains(d.getId())) {
                    listIdSubDepartement.add(d.getId());
                }
                giveAllSupDepartement(d);
            }
        }
        return listIdSubDepartement;
    }

    public YvsGrhOrdreCalculSalaire findPeriodeSalaire(Date date) {
        champ = new String[]{"date", "societe"};
        val = new Object[]{date, currentAgence.getSociete()};
        YvsGrhOrdreCalculSalaire od = (YvsGrhOrdreCalculSalaire) dao.loadOneByNameQueries("YvsGrhOrdreCalculSalaire.findByContainDate", champ, val);
        return od;
    }

    public boolean asDroitValideEtape(YvsWorkflowEtapeValidation etape) {
        return asDroitValideEtape(etape, currentUser.getUsers());
    }

    public boolean asDroitValidePhase(YvsComptaPhaseReglement etape) {
        return asDroitValidePhase(etape, currentUser.getUsers());
    }

    public boolean asDroitValideEtape(YvsWorkflowEtapeValidation etape, YvsUsers users) {
        return asDroitValideEtape(etape.getAutorisations(), users.getNiveauAcces());
    }

    public boolean asDroitValidePhase(YvsComptaPhaseReglement etape, YvsUsers users) {
        return asDroitValidePhase(etape.getAutorisations(), users.getNiveauAcces());
    }

    public boolean asDroitValideEtape(List<YvsWorkflowAutorisationValidDoc> lau, YvsNiveauAcces n) {
        if (lau != null ? !lau.isEmpty() : false) {
            for (YvsWorkflowAutorisationValidDoc au : lau) {
                if (au.getNiveauAcces().equals(n)) {
                    return au.getCanValide();
                }
            }
            return false;
        }
        return true;
    }

    public boolean asDroitValidePhase(List<YvsComptaPhaseReglementAutorisation> lau, YvsNiveauAcces n) {
        if (lau != null ? !lau.isEmpty() : false) {
            for (YvsComptaPhaseReglementAutorisation au : lau) {
                if (au.getNiveauAcces().equals(n)) {
                    return au.getCanValide();
                }
            }
            return false;
        }
        return true;
    }

    public boolean autoriser(String ressource) {
        AccesRessource ar = (AccesRessource) giveManagedBean(AccesRessource.class);
        boolean b = false;
        if (ar != null) {
            try {
                Field field = ar.getClass().getDeclaredField(ressource);
                if (field != null) {
                    field.setAccessible(true);
                    b = field.getBoolean(ar);
                }
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return b;
    }

    public boolean autoriser(String ressource, YvsUsers user) {
        // récupère le niveau d'accès
        YvsNiveauAcces n = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauUsers.findNiveauByUser", new String[]{"user", "societe"}, new Object[]{user, currentAgence.getSociete()});
        if (n != null) {
            YvsAutorisationRessourcesPage au = (YvsAutorisationRessourcesPage) dao.loadOneByNameQueries("YvsAutorisationRessourcesPage.findByRessourceNiveau", new String[]{"ressource", "niveau"}, new Object[]{ressource, n});
            return (au != null) ? au.getAcces() : false;
        }
        return false;
    }

    public boolean autoriser_OLD(String ressource) {
        AccesRessource ar = (AccesRessource) giveManagedBean(AccesRessource.class);
        boolean b = false;
        if (ar != null) {
            Class<AccesRessource> cl = AccesRessource.class;
            Field[] listChamp = cl.getDeclaredFields();
            for (Field f : listChamp) {
                if (f.getType().getName().toLowerCase().equals("boolean")) {
                    f.setAccessible(true);
                    try {
                        if (f.getName().equals(ressource)) {
                            b = f.getBoolean(ar);
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return b;
//        Boolean b = (Boolean)dao.loadObjectByNameQueries("YvsAutorisationRessourcesPage.findAccesRessource", new String[]{"reference", "niveau"}, new Object[]{ressource, currentNiveau});        
//        System.err.println(ressource + " ---- " + currentNiveau.getId()+" Result "+b);
//        return b != null ? b : false;
    }

    public List<YvsComDocAchats> returnErrorContenuAchat(YvsComDocAchats y) {
        List<YvsComDocAchats> documentsError = new ArrayList<>();
        y.getContenus().clear();
        if ((y != null) ? y.getId() > 0 : false) {
            if ((y.getDocumentLie() != null) ? y.getDocumentLie().getId() > 0 : false) {
                YvsComDocAchats b = y.getDocumentLie();

                YvsComDocAchats bonRetour = new YvsComDocAchats(), bonAvoir = new YvsComDocAchats();
                boolean dejaA = false, dejaR = false;

                nameQueri = "YvsComContenuDocAchat.findByDocAchat";
                champ = new String[]{"docAchat"};
                val = new Object[]{y};
                List<YvsComContenuDocAchat> lf = dao.loadNameQueries(nameQueri, champ, val);

                //On teste si le document fils est un bon de livraison
                switch (b.getTypeDoc()) {
                    case Constantes.TYPE_FA: {
                        //On se balade dans les contenus de la facture
                        for (YvsComContenuDocAchat cf : lf) {
                            boolean retour = false;
                            //On recherche l'article concerné dans le contenu du bon de livraison
                            nameQueri = "YvsComContenuDocAchat.findByArticleDocAchat";
                            champ = new String[]{"docAchat", "article"};
                            val = new Object[]{b, cf.getArticle()};
                            List<YvsComContenuDocAchat> lb = dao.loadNameQueries(nameQueri, champ, val);

                            for (YvsComContenuDocAchat cb : lb) {
                                if (cf.getQuantiteRecu() < cb.getQuantiteRecu()) {
                                    cb.setQuantiteCommande(cf.getQuantiteRecu());
                                    cf.setQuantiteCommande(cb.getQuantiteRecu());
                                    retour = true;
                                    cb.setErrorQte(true);
                                } else {
                                    cb.setQuantiteCommande(cf.getQuantiteRecu());
                                    cb.setQuantiteRecu(cf.getQuantiteRecu());
                                }
                                //On verifi s'il y'a erreur sur la quantite
                                if (retour) {
                                    //On verifi si l'on a deja cree un bon d'avoir
                                    if (!dejaR) {
                                        bonRetour = y;
                                        bonRetour.setDateDoc(new Date());
                                        bonRetour.setDocumentLie(new YvsComDocAchats(y.getId(), y.getNumDoc(), y.getStatut()));
                                        bonRetour.setTypeDoc(Constantes.TYPE_BRA);
                                        bonRetour.setNumDoc(genererReference(Constantes.TYPE_BRA_NAME, bonRetour.getDateDoc()));
                                        bonRetour.setId((long) 2);
                                        bonRetour.setNew_(true);
                                        bonRetour.setUpdate(false);
                                        bonRetour.setStatut(Constantes.ETAT_EDITABLE);
                                        bonAvoir.setDateSave(new Date());
                                        dejaR = true;
                                    }
                                    cb.setId(cf.getId());
                                    cb.setRemise(cf.getRemise());
                                    cb.setPrixAchat(cf.getPrixAchat());
                                    cb.setDocAchat(bonRetour);
                                    cb.setParent(cf);
                                    bonRetour.getContenus().add(cb);
                                }
                            }
                        }
                        break;
                    }
                    case Constantes.TYPE_BCA: {
                        //On se balade dans les contenus de la facture
                        for (YvsComContenuDocAchat cf : lf) {
                            boolean avoir = false;
                            //On recherche l'article concerné dans le contenu du bon de livraison
                            nameQueri = "YvsComContenuDocAchat.findByArticleDocAchat";
                            champ = new String[]{"docAchat", "article"};
                            val = new Object[]{b, cf.getArticle()};
                            List<YvsComContenuDocAchat> lb = dao.loadNameQueries(nameQueri, champ, val);

                            for (YvsComContenuDocAchat cb : lb) {
                                if (cf.getPrixAchat() > cb.getPrixAchat()) {
                                    cb.setPrixAchat(cb.getPrixAchat());
                                    cb.setErrorPua(true);
                                    avoir = true;
                                } else {
                                    cb.setPrixAchat(cf.getPrixAchat());
                                }
                                //On teste si la remise de la facture correspond a la remise du bon
                                if (cf.getRemise() < cb.getRemise()) {
                                    cb.setRemise(cb.getRemise());
                                    avoir = true;
                                    cb.setErrorRemise(true);
                                } else {
                                    cb.setRemise(cf.getRemise());
                                }
                                //On verifi s'il y'a erreur sur le prix ou la remise
                                if (avoir) {
                                    //On verifi si l'on a deja cree un bon d'avoir
                                    if (!dejaA) {
                                        bonAvoir = y;
                                        bonAvoir.setDateDoc(new Date());
                                        bonAvoir.setDocumentLie(new YvsComDocAchats(y.getId(), y.getNumDoc(), y.getStatut()));
                                        bonAvoir.setTypeDoc(Constantes.TYPE_BAA);
                                        bonAvoir.setNumDoc(genererReference(Constantes.TYPE_BAA_NAME, bonAvoir.getDateDoc()));
                                        bonAvoir.setId((long) 1);
                                        bonAvoir.setNew_(true);
                                        bonAvoir.setUpdate(false);
                                        bonAvoir.setStatut(Constantes.ETAT_EDITABLE);
                                        bonAvoir.setDateSave(new Date());
                                        dejaA = true;
                                    }
                                    cb.setId(cf.getId());
                                    cb.setQuantiteCommande(cf.getQuantiteRecu());
                                    cb.setQuantiteRecu(cf.getQuantiteRecu());
                                    cb.setDocAchat(bonAvoir);
                                    cb.setParent(cf);
                                    bonAvoir.getContenus().add(cb);
                                }
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }
                if (bonRetour.getId() != null ? bonRetour.getId() > 0 : false) {
                    documentsError.add(bonRetour);
                }
                if (bonAvoir.getId() != null ? bonAvoir.getId() > 0 : false) {
                    documentsError.add(bonAvoir);
                }
            }
        }
        return documentsError;
    }

    public List<YvsComContenuDocStock> removeDoublonStock(long doc) {
        List<YvsComContenuDocStock> l = new ArrayList<>();
        List<YvsComContenuDocStock> l_ = new ArrayList<>();
        nameQueri = "YvsComContenuDocStock.findByDocStock";
        champ = new String[]{"docStock"};
        val = new Object[]{new YvsComDocStocks(doc)};
        List<YvsComContenuDocStock> lc = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComContenuDocStock c : lc) {
            boolean add = false;
            for (YvsComContenuDocStock c_ : l) {
                if (c.getArticle().getId().equals(c_.getArticle().getId())) {
                    add = true;
                    l_.add(c);
                    break;
                }
            }
            if (!add) {
                l.add(c);
            }
        }
        for (YvsComContenuDocStock c_ : l_) {
            for (YvsComContenuDocStock c : l) {
                if (c.getArticle().getId().equals(c_.getArticle().getId())) {
                    c.setQuantite(c.getQuantite() + c_.getQuantite());
                }
            }
        }
        for (YvsComContenuDocStock c : l_) {
            dao.delete(c);
        }
        for (YvsComContenuDocStock c : l) {
            dao.update(c);
        }
        return l;
    }

    public List<YvsComContenuDocAchat> removeDoublonAchat(long doc) {
        List<YvsComContenuDocAchat> l = new ArrayList<>();
        List<YvsComContenuDocAchat> l_ = new ArrayList<>();
        nameQueri = "YvsComContenuDocAchat.findByDocAchat";
        champ = new String[]{"docAchat"};
        val = new Object[]{new YvsComDocAchats(doc)};
        List<YvsComContenuDocAchat> lc = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComContenuDocAchat c : lc) {
            boolean add = false;
            for (YvsComContenuDocAchat c_ : l) {
                if (c.getArticle().getId().equals(c_.getArticle().getId())) {
                    add = true;
                    l_.add(c);
                    break;
                }
            }
            if (!add) {
                l.add(c);
            }
        }
        for (YvsComContenuDocAchat c_ : l_) {
            for (YvsComContenuDocAchat c : l) {
                if (c.getArticle().getId().equals(c_.getArticle().getId())) {
                    c.setQuantiteCommande(c.getQuantiteRecu() + c_.getQuantiteRecu());
                }
            }
        }
        for (YvsComContenuDocAchat c : l_) {
            dao.delete(c);
        }
        for (YvsComContenuDocAchat c : l) {
            dao.update(c);
        }
        return l;
    }

    public List<YvsComContenuDocVente> removeDoublonVente(long doc) {
        List<YvsComContenuDocVente> l = new ArrayList<>();
        List<YvsComContenuDocVente> l_ = new ArrayList<>();
        nameQueri = "YvsComContenuDocVente.findByDocVente";
        champ = new String[]{"docVente"};
        val = new Object[]{new YvsComDocVentes(doc)};
        List<YvsComContenuDocVente> lc = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComContenuDocVente c : lc) {
            boolean add = false;
            for (YvsComContenuDocVente c_ : l) {
                if (c.getArticle().getId().equals(c_.getArticle().getId())) {
                    add = true;
                    l_.add(c);
                    break;
                }
            }
            if (!add) {
                l.add(c);
            }
        }
        for (YvsComContenuDocVente c_ : l_) {
            for (YvsComContenuDocVente c : l) {
                if (c.getArticle().getId().equals(c_.getArticle().getId())) {
                    c.setQuantite(c.getQuantite() + c_.getQuantite());
                }
            }
        }
        for (YvsComContenuDocVente c : l_) {
            dao.delete(c);
        }
        for (YvsComContenuDocVente c : l) {
            dao.update(c);
        }
        return l;
    }

    public void cleanEnteteVente() {
        try {
            String req = "DELETE FROM yvs_com_entete_doc_vente WHERE id IN "
                    + "(SELECT e.id FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users c ON e.creneau = c.id "
                    + "INNER JOIN yvs_users u ON c.users = u.id INNER JOIN yvs_agences a ON u.agence = a.id "
                    + "WHERE a.societe = ? AND e.id NOT IN (SELECT DISTINCT y.entete_doc FROM yvs_com_doc_ventes y))";
            dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getSociete().getId(), 1)});
            succes();
        } catch (Exception ex) {
            getErrorMessage("Impossible de nettoyer");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void cleanVente() {
        boolean cleanAll = autoriser("fv_clean_all_doc_societe");
        try {
            String req;
            if (cleanAll) {
                req = " DELETE FROM yvs_com_doc_ventes WHERE id IN (SELECT d.id FROM yvs_com_contenu_doc_vente c right join yvs_com_doc_ventes d ON d.id=c.doc_vente "
                        + "INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc "
                        + "INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau "
                        + "INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point INNER JOIN yvs_base_point_vente p "
                        + "ON p.id=cp.point INNER JOIN yvs_agences a ON a.id=p.agence WHERE a.societe=? AND c.id IS NULL )";
                dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getSociete().getId(), 1)});
            } else {
                req = "DELETE FROM yvs_com_doc_ventes WHERE id IN (SELECT d.id FROM yvs_com_contenu_doc_vente c right join yvs_com_doc_ventes d ON d.id=c.doc_vente "
                        + "INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc "
                        + "INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau "
                        + "INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point INNER JOIN yvs_base_point_vente p "
                        + "ON p.id=cp.point INNER JOIN yvs_agences a ON a.id=p.agence WHERE a.id=? AND c.id IS NULL)";
                dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getId(), 1)});
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Impossible de nettoyer");
        }
    }

    public void cleanVenteByDate(Date date) {
        boolean cleanAll = autoriser("fv_clean_all_doc_societe");
        try {
            String req;
            if (cleanAll) {
                req = " DELETE FROM yvs_com_doc_ventes WHERE id IN (SELECT d.id FROM yvs_com_contenu_doc_vente c right join yvs_com_doc_ventes d ON d.id=c.doc_vente "
                        + "INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc "
                        + "INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau "
                        + "INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point INNER JOIN yvs_base_point_vente p "
                        + "ON p.id=cp.point INNER JOIN yvs_agences a ON a.id=p.agence WHERE a.societe=? AND c.id IS NULL AND e.date_entete <= ?)";
                dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getSociete().getId(), 1), new yvs.dao.Options(date, 2)});
            } else {
                req = "DELETE FROM yvs_com_doc_ventes WHERE id IN (SELECT d.id FROM yvs_com_contenu_doc_vente c right join yvs_com_doc_ventes d ON d.id=c.doc_vente "
                        + "INNER JOIN yvs_com_entete_doc_vente e ON e.id=d.entete_doc "
                        + "INNER JOIN yvs_com_creneau_horaire_users cu ON cu.id=e.creneau "
                        + "INNER JOIN yvs_com_creneau_point cp ON cp.id=cu.creneau_point INNER JOIN yvs_base_point_vente p "
                        + "ON p.id=cp.point INNER JOIN yvs_agences a ON a.id=p.agence WHERE a.id=? AND c.id IS NULL AND e.date_entete <= ?)";
                dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getId(), 1), new yvs.dao.Options(date, 2)});
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Impossible de nettoyer");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void cleanAchat() {
        boolean cleanAll = autoriser("fa_clean_all_doc_societe");
        try {
            String req = null;
//            = "DELETE FROM yvs_com_doc_achats WHERE id IN "
//                    + "(SELECT d.id FROM yvs_com_doc_achats d INNER JOIN yvs_agences a ON d.agence = a.id "
//                    + "WHERE a.societe = ? AND d.id NOT IN (SELECT DISTINCT y.doc_achat FROM yvs_com_contenu_doc_achat y))";
            if (cleanAll) {
                req = "DELETE FROM yvs_com_doc_achats WHERE id IN (SELECT d.id FROM yvs_com_contenu_doc_achat c right join yvs_com_doc_achats d "
                        + " ON d.id=c.doc_achat inner join yvs_agences a on a.id=d.agence "
                        + " WHERE a.societe=? AND c.id IS NULL)";
                dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getSociete().getId(), 1)});
            } else {
                req = "DELETE FROM yvs_com_doc_achats WHERE id IN (SELECT d.id FROM yvs_com_contenu_doc_achat c right join yvs_com_doc_achats d "
                        + " ON d.id=c.doc_achat inner join yvs_agences a on a.id=d.agence "
                        + " WHERE a.id=? AND c.id IS NULL)";
                dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getId(), 1)});
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Impossible de nettoyer");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void cleanStock() {
        boolean cleanAll = autoriser("stock_clean_all_doc_societe");
        try {
            String req;
            if (cleanAll) {
                req = "DELETE FROM yvs_com_doc_stocks WHERE ID IN(SELECT d.id FROM yvs_com_contenu_doc_stock  c "
                        + "RIGHT JOIN yvs_com_doc_stocks d ON d.id=c.doc_stock "
                        + "INNER JOIN yvs_base_depots de ON de.id=d.source "
                        + "INNER JOIN yvs_agences a ON a.id=de.agence WHERE a.societe=? AND c.id IS NULL AND d.type_doc!='IN')";
                dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getSociete().getId(), 1)});
            } else {
                req = "DELETE FROM yvs_com_doc_stocks WHERE ID IN(SELECT d.id FROM yvs_com_contenu_doc_stock  c "
                        + "RIGHT JOIN yvs_com_doc_stocks d ON d.id=c.doc_stock "
                        + "INNER JOIN yvs_base_depots de ON de.id=d.source "
                        + "INNER JOIN yvs_agences a ON a.id=de.agence WHERE a.id=? AND c.id IS NULL AND d.type_doc!='IN')";
                dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentAgence.getId(), 1)});
            }
//            String req = "DELETE FROM yvs_com_doc_stocks WHERE id IN "
//                    + "(SELECT d.id FROM yvs_com_doc_stocks d "
//                    + "WHERE d.societe = ? AND d.id NOT IN (SELECT DISTINCT y.doc_stock FROM yvs_com_contenu_doc_stock y))";            
            succes();
        } catch (Exception ex) {
            getErrorMessage("Impossible de nettoyer");
            System.err.println("Error " + ex.getMessage());
        }
    }

    public void equilibreOther() {
        if (currentUser.getUsers().getAccesMultiAgence()) {
            champ = new String[]{"societe", "typeDoc", "cloturer"};
            val = new Object[]{currentAgence.getSociete(), Constantes.TYPE_OD, false};
//            nameQueri = "YvsBaseDocDivers.findByTypeDocCloturer";
        } else {
            if (currentUser.getUsers().getSuperAdmin()) {
                champ = new String[]{"agence", "typeDoc", "cloturer"};
                val = new Object[]{currentAgence, Constantes.TYPE_OD, false};
                nameQueri = "YvsBaseDocDivers.findByAgenceCloturer";
            } else {
//                champ = new String[]{"depot", "typeDoc", "cloturer"};
//                val = new Object[]{currentDepot, Constantes.TYPE_OD, false};
//                nameQueri = "YvsBaseDocDivers.findByDepotCloturer";
            }
        }
        List<YvsComptaCaisseDocDivers> ld = dao.loadNameQueries(nameQueri, champ, val);
        for (YvsComptaCaisseDocDivers d : ld) {
            equilibreOther(d.getId());
        }
        succes();
    }

    public void equilibreOther(long id) {
        try {
            DocCaissesDivers d = new DocCaissesDivers(id);
//            setMontantTotalDoc(d);

            champ = new String[]{"docDivers"};
            val = new Object[]{new YvsComptaCaisseDocDivers(id)};
            nameQueri = "YvsComptaCaisseMensualiteDocDivers.findByDocDivers";
            List<YvsComptaCaisseMensualiteDocDivers> lm = dao.loadNameQueries(nameQueri, champ, val);
            for (YvsComptaCaisseMensualiteDocDivers m : lm) {
                champ = new String[]{"facture", "s1", "s2"};
                val = new Object[]{m.getId(), Constantes.ETAT_VALIDE, Constantes.ETAT_REGLE};
                nameQueri = "YvsBasePieceTresorerie.findByOtherStatut2S";
                Double p = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                if (m.getMontant() < (p != null ? p : 0)) {
                    m.setEtat(Constantes.ETAT_REGLE);
                } else if ((p != null ? p : 0) > 0) {
                    m.setEtat(Constantes.ETAT_ENCOURS);
                } else {
                    m.setEtat(Constantes.ETAT_EDITABLE);
                }
                dao.update(m);
            }

            double s = 0;
            String rq;
            if (d.getMontant() <= s) {
                rq = "UPDATE yvs_base_doc_divers SET statut = 'REGLE' WHERE id=? AND statut != 'ANNULE' AND cloturer = false";
            } else if (s > 0) {
                rq = "UPDATE yvs_base_doc_divers SET statut = 'ENCOURS' WHERE id=? AND statut != 'ANNULE' AND cloturer = false";
            } else {
                rq = "UPDATE yvs_base_doc_divers SET statut = 'EDITABLE' WHERE id=? AND statut != 'ANNULE' AND cloturer = false";
            }
            yvs.dao.Options[] param = new yvs.dao.Options[]{new yvs.dao.Options(id, 1)};
            dao.requeteLibre(rq, param);
        } catch (Exception ex) {
            getErrorMessage("Opération Impossible");
            System.err.println("Erreur " + ex.getMessage());
        }
    }

    public List<List<Object>> readFileXLS(String path) {
        FileInputStream fichier = null;
        try {
            List<List<Object>> r = new ArrayList<>();
            fichier = new FileInputStream(new File(path));
            //créer une instance workbook qui fait référence au fichier xlsx
            try {
//                org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fichier);
                org.apache.poi.ss.usermodel.Workbook workbook;
                if (path.endsWith(".xls")) {
                    workbook = new HSSFWorkbook(fichier);
                } else {
                    workbook = new XSSFWorkbook(fichier);
                }
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                for (Row ligne : sheet) {//parcourir les lignes 
                    List<Object> a = new ArrayList<>();
                    for (Cell cell : ligne) {//parcourir les colonnes
                        //évaluer le type de la cellule
                        switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                a.add(cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_STRING:
                                a.add(cell.getStringCellValue());
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                a.add(cell.getBooleanCellValue());
                                break;
                        }
                    }
                    r.add(a);

                }
            } catch (IOException ex) {
                Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
                getErrorMessage("Fichier Incorrect");
            }
            return r;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
            getErrorMessage("Fichier Incorrect");
            return null;
        } finally {
            try {
                fichier.close();
            } catch (IOException ex) {
                Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
                getErrorMessage("Fichier Incorrect");
            }
        }
    }

    private ModeleReference rechercheModeleReference(String mot) {
        if (!mot.toLowerCase().equals("")) {
            String[] ch = new String[]{"designation", "societe"};
            Object[] v = new Object[]{mot, currentAgence.getSociete()};
            String query = "YvsBaseModeleReference.findByElement";
            List<YvsBaseModeleReference> l = dao.loadNameQueries(query, ch, v);
            if ((l != null) ? !l.isEmpty() : false) {
                return UtilCom.buildBeanModele(l.get(0));
            } else {
                if (mot.equals(Constantes.TYPE_OD_RECETTE_NAME) || mot.equals(Constantes.TYPE_OD_DEPENSE_NAME)) {
                    return rechercheModeleReference(Constantes.TYPE_OD_NAME);
                } else if (mot.equals(Constantes.TYPE_PC_ACHAT_NAME)
                        || mot.equals(Constantes.TYPE_PC_VENTE_NAME)
                        || mot.equals(Constantes.TYPE_PC_DIVERS_NAME)
                        || mot.equals(Constantes.TYPE_PC_MISSION_NAME)
                        || mot.equals(Constantes.TYPE_PC_SALAIRE_NAME)) {
                    return rechercheModeleReference(Constantes.TYPE_PC_NAME);
                }
            }
        }
        return null;
    }

    public String genererPrefixe(String element, long id) {
        ModeleReference modele = rechercheModeleReference(element);
        if ((modele != null) ? modele.getId() > 0 : false) {
            return genererPrefixe(modele, id);
        } else {
            getErrorMessage("Cet élément n'a pas de modele de reference!");
            return "";
        }
    }

    public String genererPrefixe(ModeleReference modele, long id) {
        String inter = modele.getPrefix();
        String code = null;
        if (modele.isCodePoint()) {
            code = genererPrefixeCodeElement(modele, id);
        }
        if (code != null ? code.trim().length() > 0 : false) {
            inter += modele.getSeparateur() + code;
        }
        inter += modele.getSeparateur();
        return inter != null ? inter : "";
    }

    public String genererPrefixeCodeElement(ModeleReference modele, long id) {
        if (modele.isCodePoint()) {
            String code = "";
            switch (modele.getElementCode()) {
                case Constantes.SOCIETE: {
                    if (currentAgence.getSociete() != null ? currentAgence.getSociete().getCodeAbreviation().trim().length() > 0 : false) {
                        code = currentAgence.getSociete().getCodeAbreviation();
                    }
                    break;
                }
                case Constantes.AGENCE: {
                    if (currentAgence != null ? currentAgence.getAbbreviation().trim().length() > 0 : false) {
                        code = currentAgence.getAbbreviation();
                    }
                    break;
                }
                case Constantes.AUTRE: {
                    switch (modele.getElement().getDesignation()) {
                        case Constantes.TYPE_FA_NAME:
                        case Constantes.TYPE_BLA_NAME:
                        case Constantes.TYPE_BLV_NAME:
                        case Constantes.PROD_TYPE_PROD_NAME:
                        case Constantes.TYPE_RA_NAME:
                        case Constantes.TYPE_RS_NAME:
                        case Constantes.TYPE_FiA_NAME:
                        case Constantes.TYPE_FT_NAME:
                        case Constantes.TYPE_SS_NAME:
                        case Constantes.TYPE_ES_NAME:
                        case Constantes.TYPE_IN_NAME:
                        case Constantes.TYPE_RE_NAME:
                        case Constantes.TYPE_OT_NAME:
                        case Constantes.TYPE_BRV_NAME:
                        case Constantes.TYPE_BAV_NAME:
                        case Constantes.TYPE_BCV_NAME:
                        case Constantes.TYPE_BRA_NAME:
                        case Constantes.TYPE_BAA_NAME:
                        case Constantes.TYPE_BCA_NAME:
                            YvsBaseDepots d = null;
                            if (id > 0) {
                                d = (YvsBaseDepots) dao.loadObjectByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (d != null ? d.getId() > 0 : false) {
                                code = d.getAbbreviation();
                            }
                            break;
                        case Constantes.TYPE_FV_NAME:
                            YvsBasePointVente p = null;
                            if (id > 0) {
                                p = (YvsBasePointVente) dao.loadObjectByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (p != null ? p.getId() > 0 : false) {
                                code = p.getCode();
                            } else {
                                if (currentPoint != null ? currentPoint.getCode() != null : false) {
                                    code = currentPoint.getCode();
                                }
                            }
                            break;
                        case Constantes.TYPE_OD_NAME:
                        case Constantes.TYPE_PT_NAME:
                        case Constantes.TYPE_BP_NAME:
                        case Constantes.TYPE_PC_NAME:
                        case Constantes.TYPE_PC_ACHAT_NAME:
                        case Constantes.TYPE_PC_VENTE_NAME:
                        case Constantes.TYPE_PC_DIVERS_NAME:
                        case Constantes.TYPE_PC_MISSION_NAME:
                        case Constantes.TYPE_PC_SALAIRE_NAME:
                        case Constantes.TYPE_DOC_MISSION_NAME:
                        case Constantes.TYPE_PT_AVANCE_ACHAT:
                        case Constantes.TYPE_PT_AVANCE_VENTE:
                            YvsBaseCaisse c = null;
                            if (id > 0) {
                                c = (YvsBaseCaisse) dao.loadObjectByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (c != null ? c.getId() > 0 : false) {
                                code = c.getCode();
                            }
                            break;
                        case Constantes.MUT_ACTIVITE_CREDIT:
                        case Constantes.MUT_TRANSACTIONS_MUT:
                            YvsMutCaisse en = null;
                            if (id > 0) {
                                en = (YvsMutCaisse) dao.loadObjectByNameQueries("YvsMutCaisse.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (en != null ? en.getId() > 0 : false) {
                                code = en.getReferenceCaisse();
                            }
                            break;
                        case Constantes.TYPE_PIECE_COMPTABLE_NAME:
                        case Constantes.TYPE_FRV_NAME:
                        case Constantes.TYPE_FAV_NAME:
                        case Constantes.TYPE_FRA_NAME:
                        case Constantes.TYPE_FAA_NAME:
                            YvsComptaJournaux j = null;
                            if (id > 0) {
                                j = (YvsComptaJournaux) dao.loadObjectByNameQueries("YvsComptaJournaux.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (j != null ? j.getId() > 0 : false) {
                                code = j.getCodeJournal();
                            }
                            break;
                        case Constantes.TYPE_CGE_NAME:
                            YvsGrhEmployes e = null;
                            if (id > 0) {
                                e = (YvsGrhEmployes) dao.loadObjectByNameQueries("YvsGrhEmployes.findById", new String[]{"id"}, new Object[]{id});
                            }
                            if (e != null ? e.getId() > 0 : false) {
                                code = e.getMatricule();
                            }
                            break;
                    }
                    break;
                }
                default: {
                    break;
                }
            }
            if (code != null) {
                if (modele.getLongueurCodePoint() > 0 ? code.length() > modele.getLongueurCodePoint() : false) {
                    modele.setCodePointvente(code.substring(0, modele.getLongueurCodePoint()));
                } else {
                    modele.setCodePointvente(code);
                }
            }
        }
        return modele.getCodePointvente();
    }

    public String genererPrefixeComplet(ModeleReference modele, Date date, long id) {
        String prefixe = genererPrefixe(modele, id);
        if (prefixe != null ? prefixe.trim().length() > 0 : false) {
            Calendar cal = Util.dateToCalendar(date);
            if (modele.isJour()) {
                if (cal.get(Calendar.DATE) > 9) {
                    prefixe += Integer.toString(cal.get(Calendar.DATE));
                }
                if (cal.get(Calendar.DATE) < 10) {
                    prefixe += ("0" + Integer.toString(cal.get(Calendar.DATE)));
                }
            }
            if (modele.isMois()) {
                if (cal.get(Calendar.MONTH) + 1 > 9) {
                    prefixe += Integer.toString(cal.get(Calendar.MONTH) + 1);
                }
                if (cal.get(Calendar.MONTH) + 1 < 10) {
                    prefixe += ("0" + Integer.toString(cal.get(Calendar.MONTH) + 1));
                }
            }
            if (modele.isAnnee()) {
                prefixe += Integer.toString(cal.get(Calendar.YEAR)).substring(2);
            }
            prefixe += modele.getSeparateur();
        }
        return prefixe != null ? prefixe : "";
    }

    public String genererReference(String element, Date date) {
        return genererReference(element, date, 0);
    }

    public String genererReference(String element, Date date, long id, String type) {
        return genererReference(element, date, id);
    }

    public String genererReference(String element, Date date, String code) {
        return genererReference(element, date, 0);
    }

    public String genererReference(String element, Date date, long id) {
        ModeleReference model = rechercheModeleReference(element);
        if ((model != null) ? model.getId() > 0 : false) {
            return getReferenceElement(model, date, id);
        } else {
            getErrorMessage("Cette ressource n'a pas de model de référence!", "Veuillez en créer");
            return "";
        }
    }

    private String getReferenceElement(ModeleReference modele, Date date, long id) {
        String motRefTable = "";
        String inter = genererPrefixeComplet(modele, date, id);
        switch (modele.getElement().getDesignation()) {
            case "Employe": {
                break;
            }
            case Constantes.TYPE_CGE_NAME: {
                String[] ch = new String[]{"numDoc", "societe", "numDoc1"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete(), inter + "99999999"};
                String query = "YvsGrhCongeEmps.findReferenceByReference";
                motRefTable = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = (motRefTable != null) ? motRefTable : "";
                break;
            }
            case Constantes.TYPE_COM_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComCommissionCommerciaux.findByNumero";
                List<YvsComCommissionCommerciaux> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_CONTRAT_CLIENT_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComContratsClient.findLikeReference";
                List<YvsComContratsClient> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_BP_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaBonProvisoire.findByReference";
                List<YvsComptaBonProvisoire> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_FiA_NAME: {
                String[] ch = new String[]{"reference", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComFicheApprovisionnement.findByReference";
                List<YvsComFicheApprovisionnement> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_BLA_NAME:
            case Constantes.TYPE_BRA_NAME:
            case Constantes.TYPE_BAA_NAME:
            case Constantes.TYPE_BCA_NAME:
            case Constantes.TYPE_FRA_NAME:
            case Constantes.TYPE_FAA_NAME:
            case Constantes.TYPE_FA_NAME: {
                String[] ch = new String[]{"numRefDoc", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComDocAchats.findByReference";
                List<YvsComDocAchats> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_BLV_NAME:
            case Constantes.TYPE_BRV_NAME:
            case Constantes.TYPE_BAV_NAME:
            case Constantes.TYPE_BCV_NAME:
            case Constantes.TYPE_FRV_NAME:
            case Constantes.TYPE_FAV_NAME:
            case Constantes.TYPE_FV_NAME: {
                String[] ch = new String[]{"numDoc", "societe", "numDoc1"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete(), inter + "99999999"};
                String query = "YvsComDocVentes.findReferenceByReference";
//                List<YvsComDocVentes> l = dao.loadNameQueries(query, ch, v);
                motRefTable = (String) dao.loadObjectByNameQueries(query, ch, v);
//                if ((l != null) ? !l.isEmpty() : false) {
//                    motRefTable = l.get(0).getNumDoc();
//                } else {
//                    motRefTable = "";
//                }
                motRefTable = (motRefTable != null) ? motRefTable : "";
                break;
            }
            case Constantes.TYPE_FT_NAME:
            case Constantes.TYPE_SS_NAME:
            case Constantes.TYPE_ES_NAME:
            case Constantes.TYPE_IN_NAME:
            case Constantes.TYPE_RE_NAME:
            case Constantes.TYPE_OT_NAME: {
                String[] ch = new String[]{"numDoc", "societe", "numDoc1"};
                Object[] v = new Object[]{inter, currentAgence.getSociete(), inter + "99999998"};
                String query = "YvsComDocStocks.findReferenceByReference";
//                List<YvsComDocStocks> l = dao.loadNameQueries(query, ch, v);
//                if ((l != null) ? !l.isEmpty() : false) {
//                    motRefTable = l.get(0).getNumDoc();
//                } else {
//                    motRefTable = "";
//                }
                motRefTable = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = (motRefTable != null) ? motRefTable : "";
                break;
            }
            case Constantes.TYPE_RS_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComReservationStock.findByReference";
                List<YvsComReservationStock> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_RA_NAME: {
                String[] ch = new String[]{"numDoc", "agence"};
                Object[] v = new Object[]{inter + "%", currentAgence};
                String query = "YvsComDocRation.findByNumDocL";
                List<YvsComDocRation> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumDoc();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_OD_NAME: {
                String[] ch = new String[]{"numPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaCaisseDocDivers.findByNumPieceLike_";
                String ref = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = ref != null ? ref : "";
                break;
            }
            case Constantes.TYPE_OD_RECETTE_NAME: {
                String[] ch = new String[]{"numPiece", "societe", "mouvement"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete(), "R"};
                String query = "YvsComptaCaisseDocDivers.findByNumPieceLikeMouv";
                String ref = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = ref != null ? ref : "";
                break;
            }
            case Constantes.TYPE_OD_DEPENSE_NAME: {
                String[] ch = new String[]{"numPiece", "societe", "mouvement"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete(), "D"};
                String query = "YvsComptaCaisseDocDivers.findByNumPieceLikeMouv";
                String ref = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = ref != null ? ref : "";
                break;
            }
            case Constantes.TYPE_PT_NAME: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaCaissePieceVirement.findByNumeroPiece";
                List<YvsComptaCaissePieceVirement> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaMouvementCaisse.findByNumeroPiece";
                List<YvsComptaMouvementCaisse> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumero();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_ACHAT_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaCaissePieceAchat.findByNumPiece";
                List<YvsComptaCaissePieceAchat> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_VENTE_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaCaissePieceVente.findByNumPiece";
                List<YvsComptaCaissePieceVente> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_DIVERS_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaCaissePieceDivers.findByNumPiece";
                List<YvsComptaCaissePieceDivers> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_MISSION_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaCaissePieceMission.findByNumPiece";
                List<YvsComptaCaissePieceMission> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PC_SALAIRE_NAME: {
                String[] ch = new String[]{"numero", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaCaissePieceSalaire.findByNumPiece";
                List<YvsComptaCaissePieceSalaire> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumeroPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_AVANCE_VENTE: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaAcompteClient.findByNumPiece";
                List<YvsComptaAcompteClient> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumRefrence();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_AVANCE_ACHAT: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaAcompteFournisseur.findByNumPiece";
                List<YvsComptaAcompteFournisseur> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumRefrence();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_CREDIT_VENTE: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaCreditClient.findByNumPiece";
                List<YvsComptaCreditClient> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PT_CREDIT_ACHAT: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaCreditFournisseur.findByNumPiece";
                List<YvsComptaCreditFournisseur> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_PIECE_COMPTABLE_NAME: {
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsComptaPiecesComptable.findByNumPiece";
                List<YvsComptaPiecesComptable> l = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((l != null) ? !l.isEmpty() : false) {
                    motRefTable = l.get(0).getNumPiece();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.TYPE_DOC_MISSION_NAME:
                String[] ch = new String[]{"numeroPiece", "societe"};
                Object[] v = new Object[]{inter + "%", currentAgence.getSociete()};
                String query = "YvsGrhMissions.findByNumPiece";
                List<YvsGrhMissions> lm = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((lm != null) ? !lm.isEmpty() : false) {
                    motRefTable = lm.get(0).getNumeroMission();
                } else {
                    motRefTable = "";
                }
                break;
            case Constantes.MUT_TRANSACTIONS_MUT: {
                ch = new String[]{"numeroPiece", "mutuelle"};
                v = new Object[]{inter + "%", currentMutuel};
                query = "YvsMutOperationCompte.findByNumOp";
                List<YvsMutOperationCompte> lop = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((lop != null) ? !lop.isEmpty() : false) {
                    motRefTable = lop.get(0).getReferenceOperation();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.MUT_ACTIVITE_CREDIT: {
                ch = new String[]{"numeroPiece", "mutuelle"};
                v = new Object[]{inter + "%", currentMutuel};
                query = "YvsMutCredit.findByNumOp";
                List<YvsMutCredit> lop = dao.loadNameQueries(query, ch, v, 0, 1);
                if ((lop != null) ? !lop.isEmpty() : false) {
                    motRefTable = lop.get(0).getReference();
                } else {
                    motRefTable = "";
                }
                break;
            }
            case Constantes.PROD_TYPE_PROD_NAME: {
                ch = new String[]{"codeRef", "societe", "codeRef1"};
                v = new Object[]{inter + "%", currentAgence.getSociete(), inter + "99999999"};
                query = "YvsProdOrdreFabrication.findReferenceByReference";
//                List<YvsProdOrdreFabrication> lop = dao.loadNameQueries(query, ch, v);
//                if ((lop != null) ? !lop.isEmpty() : false) {
//                    motRefTable = lop.get(0).getCodeRef();
//                } else {
//                    motRefTable = "";
//                }
                motRefTable = (String) dao.loadObjectByNameQueries(query, ch, v);
                motRefTable = (motRefTable != null) ? motRefTable : "";
                break;
            }
            default: {
                break;
            }
        }
        String partieNum = motRefTable.replaceFirst(inter, "");
        if (partieNum != null ? partieNum.trim().length() > 0 : false) {
            int num = Integer.valueOf(partieNum.trim().replace("°", ""));
            if (Integer.toString(num + 1).length() > modele.getTaille()) {
                getErrorMessage("Vous ne pouvez plus ajouter ce type de document", "Erreur génération référence");
                return "";
            } else {
                for (int i = 0; i < (modele.getTaille() - Integer.toString(num + 1).length()); i++) {
                    inter += "0";
                }
            }
            inter += Long.toString(Long.valueOf(partieNum.trim().replace("°", "")) + 1);
        } else {
            for (int i = 0; i < modele.getTaille() - 1; i++) {
                inter += "0";
            }
            inter += "1";
        }
        return inter;
    }

    public void navigue(String id) {
        Navigations w = (Navigations) giveManagedBean(Navigations.class);
        if (w != null) {
            w.navigue(w.urlMapping(id));
        }
    }

    public Object[] returnInfosReference(YvsBaseModeleReference bean, String reference) {
        String type = null;
        if (bean == null && Util.asString(reference)) {
            String separateur = "_";
            if (reference.contains("/")) {
                separateur = "/";
            } else if (reference.contains("-")) {
                separateur = "-";
            }
            String[] tab = reference.split(separateur);
            if (tab != null ? tab.length > 0 : false) {
                String prefix = tab[0];
                bean = (YvsBaseModeleReference) dao.loadOneByNameQueries("YvsBaseModeleReference.findByPrefix", new String[]{"prefix", "societe"}, new Object[]{prefix, currentAgence.getSociete()});
                if (bean != null) {
                    type = bean.getElement().getDesignation();
                }
            }
        }
        return returnInfosByReferenceAndtype(reference, type);
    }

    public Object[] returnInfosByReferenceAndtype(String reference, String type) {
        Object[] result = null;
        if (asString(reference) && asString(type)) {
            result = new Object[9];
            switch (type) {
                case "Employe": {
                    result[0] = "YvsGrhEmployes.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsGrhEmployes.findOneByMatricule";
                    result[4] = new String[]{"matricule", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsGrhEmployes.class;
                    result[7] = "smenEmploye";
                    result[8] = "MEmps";
                    break;
                }
                case Constantes.TYPE_BP_NAME: {
                    result[0] = "YvsComptaBonProvisoire.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaBonProvisoire.findByReference";
                    result[4] = new String[]{"reference", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaBonProvisoire.class;
                    result[7] = "smenBonProvisoire";
                    result[8] = "managedBonProvisoire";
                    break;
                }
                case Constantes.TYPE_FiA_NAME: {
                    result[0] = "YvsComFicheApprovisionnement.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComFicheApprovisionnement.findByReference";
                    result[4] = new String[]{"reference", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComFicheApprovisionnement.class;
                    result[7] = "smenFicheAppro";
                    result[8] = "managedFicheAppro";
                    break;
                }
                case Constantes.TYPE_BLA_NAME: {
                    result[0] = "YvsComDocAchats.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_BLA, currentAgence.getSociete()};
                    result[3] = "YvsComDocAchats.findByReference";
                    result[4] = new String[]{"numRefDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocAchats.class;
                    result[7] = "smenBonLivraisonAchat";
                    result[8] = "managedLivraisonAchat";
                    break;
                }
                case Constantes.TYPE_BRA_NAME: {
                    result[0] = "YvsComDocAchats.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_BRA, currentAgence.getSociete()};
                    result[3] = "YvsComDocAchats.findByReference";
                    result[4] = new String[]{"numRefDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocAchats.class;
                    result[7] = "smenFactureRetourAchat";
                    result[8] = "managedBonAvoirAchat";
                    break;
                }
                case Constantes.TYPE_BAA_NAME: {
                    result[0] = "YvsComDocAchats.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_BAA, currentAgence.getSociete()};
                    result[3] = "YvsComDocAchats.findByReference";
                    result[4] = new String[]{"numRefDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocAchats.class;
                    result[7] = "smenFactureAvoirAchat";
                    result[8] = "managedBonAvoirAchat";
                    break;
                }
                case Constantes.TYPE_BCA_NAME: {
                    result[0] = "YvsComDocAchats.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_BCA, currentAgence.getSociete()};
                    result[3] = "YvsComDocAchats.findByReference";
                    result[4] = new String[]{"numRefDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocAchats.class;
                    result[7] = "smenBonCommandAchat";
                    result[8] = "managedBonAchat";
                    break;
                }
                case Constantes.TYPE_FRA_NAME: {
                    result[0] = "YvsComDocAchats.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_FRA, currentAgence.getSociete()};
                    result[3] = "YvsComDocAchats.findByReference";
                    result[4] = new String[]{"numRefDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocAchats.class;
                    result[7] = "smenFactureRetourAchat";
                    result[8] = ManagedBonAvoirAchat.class;
                    break;
                }
                case Constantes.TYPE_FAA_NAME: {
                    result[0] = "YvsComDocAchats.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_FAA, currentAgence.getSociete()};
                    result[3] = "YvsComDocAchats.findByReference";
                    result[4] = new String[]{"numRefDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocAchats.class;
                    result[7] = "smenFactureAvoirAchat";
                    result[8] = ManagedBonAvoirAchat.class;
                    break;
                }
                case Constantes.TYPE_FA_NAME: {
                    result[0] = "YvsComDocAchats.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_FA, currentAgence.getSociete()};
                    result[3] = "YvsComDocAchats.findByReference";
                    result[4] = new String[]{"numRefDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocAchats.class;
                    result[7] = "smenFactureAchat";
                    result[8] = "managedFactureAchat";
                    break;
                }
                case Constantes.TYPE_BLV_NAME: {
                    result[0] = "YvsComDocVentes.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_BLV, currentAgence.getSociete()};
                    result[3] = "YvsComDocVentes.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocVentes.class;
                    result[7] = "smenBonLivraisonVente";
                    result[8] = "managedLivraisonVente";
                    break;
                }
                case Constantes.TYPE_BRV_NAME: {
                    result[0] = "YvsComDocVentes.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_BRV, currentAgence.getSociete()};
                    result[3] = "YvsComDocVentes.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocVentes.class;
                    result[7] = "smenFactureRetourVente";
                    result[8] = "managedBonAvoirVente";
                    break;
                }
                case Constantes.TYPE_BAV_NAME: {
                    result[0] = "YvsComDocVentes.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_BAV, currentAgence.getSociete()};
                    result[3] = "YvsComDocVentes.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocVentes.class;
                    result[7] = "smenFactureAvoirVente";
                    result[8] = "managedBonAvoirVente";
                    break;
                }
                case Constantes.TYPE_BCV_NAME: {
                    result[0] = "YvsComDocVentes.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_BCV, currentAgence.getSociete()};
                    result[3] = "YvsComDocVentes.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocVentes.class;
                    result[7] = "smenBonCommandVente";
                    result[8] = "managedBonVente";
                    break;
                }
                case Constantes.TYPE_FRV_NAME: {
                    result[0] = "YvsComDocVentes.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_FRV, currentAgence.getSociete()};
                    result[3] = "YvsComDocVentes.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocVentes.class;
                    result[7] = "smenFactureRetourVente";
                    result[8] = "managedBonAvoirVente";
                    break;
                }
                case Constantes.TYPE_FAV_NAME: {
                    result[0] = "YvsComDocVentes.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_FAV, currentAgence.getSociete()};
                    result[3] = "YvsComDocVentes.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocVentes.class;
                    result[7] = "smenFactureAvoirVente";
                    result[8] = "managedBonAvoirVente";
                    break;
                }
                case Constantes.TYPE_FV_NAME: {
                    result[0] = "YvsComDocVentes.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_FV, currentAgence.getSociete()};
                    result[3] = "YvsComDocVentes.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocVentes.class;
                    result[7] = "smenFactureVente";
                    result[8] = "managedFactureVente";
                    break;
                }
                case Constantes.TYPE_FT_NAME: {
                    result[0] = "YvsComDocStocks.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_FT, currentAgence.getSociete()};
                    result[3] = "YvsComDocStocks.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocStocks.class;
                    result[7] = "smenTransfert";
                    result[8] = "managedTransfertStock";
                    break;
                }
                case Constantes.TYPE_SS_NAME: {
                    result[0] = "YvsComDocStocks.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_SS, currentAgence.getSociete()};
                    result[3] = "YvsComDocStocks.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocStocks.class;
                    result[7] = "smenSortie";
                    result[8] = "managedOtherTransfert";
                    break;
                }
                case Constantes.TYPE_ES_NAME: {
                    result[0] = "YvsComDocStocks.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_ES, currentAgence.getSociete()};
                    result[3] = "YvsComDocStocks.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocStocks.class;
                    result[7] = "smenEntree";
                    result[8] = "managedOtherTransfert";
                    break;
                }
                case Constantes.TYPE_IN_NAME: {
                    result[0] = "YvsComDocStocks.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_IN, currentAgence.getSociete()};
                    result[3] = "YvsComDocStocks.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocStocks.class;
                    result[7] = "smenInventaire";
                    result[8] = "managedInventaire";
                    break;
                }
                case Constantes.TYPE_RE_NAME: {
                    result[0] = "YvsComDocStocks.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_RE, currentAgence.getSociete()};
                    result[3] = "YvsComDocStocks.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocStocks.class;
                    result[7] = "smenReconditionnement";
                    result[8] = "managedReconditionnement";
                    break;
                }
                case Constantes.TYPE_OT_NAME: {
                    result[0] = "YvsComDocStocks.findByTypeDocCount";
                    result[1] = new String[]{"typeDoc", "societe"};
                    result[2] = new Object[]{Constantes.TYPE_OT, currentAgence.getSociete()};
                    result[3] = "YvsComDocStocks.findByReference";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocStocks.class;
                    result[7] = "smenTransfert";
                    result[8] = "managedOrdreTransfert";
                    break;
                }
                case Constantes.TYPE_RS_NAME: {
                    result[0] = "YvsComReservationStock.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComReservationStock.findByReference";
                    result[4] = new String[]{"numero", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComReservationStock.class;
                    result[7] = "smenReservStock";
                    result[8] = "managedReservation";
                    break;
                }
                case Constantes.TYPE_RA_NAME: {
                    result[0] = "YvsComDocRation.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComDocRation.findByNumDoc";
                    result[4] = new String[]{"numDoc", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComDocRation.class;
                    result[7] = "smenAttribRation";
                    result[8] = "managedRations";
                    break;
                }
                case Constantes.TYPE_OD_NAME: {
                    result[0] = "YvsComptaCaisseDocDivers.findAllC";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaCaisseDocDivers.findByNumPieceLike_";
                    result[4] = new String[]{"numPiece", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaCaisseDocDivers.class;
                    result[7] = "smenOperationDivers";
                    result[8] = "managedDocDivers";
                    break;
                }
                case Constantes.TYPE_OD_RECETTE_NAME: {
                    result[0] = "YvsComptaCaisseDocDivers.countByMouvement";
                    result[1] = new String[]{"mouvement", "societe"};
                    result[2] = new Object[]{"R", currentAgence.getSociete()};
                    result[3] = "YvsComptaCaisseDocDivers.findByNumPieceLikeMouv";
                    result[4] = new String[]{"numPiece", "societe", "mouvement"};
                    result[5] = new Object[]{reference, currentAgence.getSociete(), "R"};
                    result[6] = YvsComptaCaisseDocDivers.class;
                    result[7] = "smenOperationDivers";
                    result[8] = "managedDocDivers";
                    break;
                }
                case Constantes.TYPE_OD_DEPENSE_NAME: {
                    result[0] = "YvsComptaCaisseDocDivers.countByMouvement";
                    result[1] = new String[]{"mouvement", "societe"};
                    result[2] = new Object[]{"D", currentAgence.getSociete()};
                    result[3] = "YvsComptaCaisseDocDivers.findByNumPieceLikeMouv";
                    result[4] = new String[]{"numPiece", "societe", "mouvement"};
                    result[5] = new Object[]{reference, currentAgence.getSociete(), "D"};
                    result[6] = YvsComptaCaisseDocDivers.class;
                    result[7] = "smenOperationDivers";
                    result[8] = "managedDocDivers";
                    break;
                }
                case Constantes.TYPE_PT_NAME: {
                    result[0] = "YvsComptaCaissePieceVirement.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaCaissePieceVirement.findByNumeroPiece";
                    result[4] = new String[]{"numeroPiece", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaCaissePieceVirement.class;
                    result[7] = "smenRegVirement";
                    result[8] = "managedVirement";
                    break;
                }
                case Constantes.TYPE_PC_NAME: {
                    result[0] = "YvsComptaMouvementCaisse.findAllC";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaMouvementCaisse.findByNumeroPiece";
                    result[4] = new String[]{"numero", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaMouvementCaisse.class;
                    result[7] = "smenPieceCaiss";
                    result[8] = "managedPieceCaisse";
                    break;
                }
                case Constantes.TYPE_PC_ACHAT_NAME: {
                    result[0] = "YvsComptaCaissePieceAchat.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaCaissePieceAchat.findByNumPiece";
                    result[4] = new String[]{"numero", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaCaissePieceAchat.class;
                    result[7] = "smenRegAchat";
                    result[8] = "managedReglementAchat";
                    break;
                }
                case Constantes.TYPE_PC_VENTE_NAME: {
                    result[0] = "YvsComptaCaissePieceVente.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaCaissePieceVente.findByNumPiece";
                    result[4] = new String[]{"numero", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaCaissePieceVente.class;
                    result[7] = "smenRegVente";
                    result[8] = "managedReglementVente";
                    break;
                }
                case Constantes.TYPE_PC_MISSION_NAME: {
                    result[0] = "YvsComptaCaissePieceMission.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaCaissePieceMission.findByNumPiece";
                    result[4] = new String[]{"numero", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaCaissePieceMission.class;
                    result[7] = "smenRegMiss";
                    result[8] = "managedReglementMission";
                    break;
                }
                case Constantes.TYPE_PC_DIVERS_NAME: {
                    result[0] = "YvsComptaCaissePieceDivers.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaCaissePieceDivers.findDocDiversByNumPiece";
                    result[4] = new String[]{"numero", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaCaissePieceDivers.class;
                    result[7] = "smenOperationDivers";
                    result[8] = "managedDocDivers";
                    break;
                }
                case Constantes.TYPE_PT_AVANCE_VENTE: {
                    result[0] = "YvsComptaAcompteClient.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaAcompteClient.findByNumPiece";
                    result[4] = new String[]{"numeroPiece", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaAcompteClient.class;
                    result[7] = "smenAcompteClient";
                    result[8] = "managedOperationClient";
                    break;
                }
                case Constantes.TYPE_PT_AVANCE_ACHAT: {
                    result[0] = "YvsComptaAcompteFournisseur.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaAcompteFournisseur.findByNumPiece";
                    result[4] = new String[]{"numeroPiece", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaAcompteFournisseur.class;
                    result[7] = "smenAcompteFsseur";
                    result[8] = "managedOperationFournisseur";
                    break;
                }
                case Constantes.TYPE_PIECE_COMPTABLE_NAME: {
                    result[0] = "YvsComptaPiecesComptable.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsComptaPiecesComptable.findByNumPiece";
                    result[4] = new String[]{"numeroPiece", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsComptaPiecesComptable.class;
                    result[7] = "smenSaisieJournal";
                    result[8] = "managedSaisiePiece";
                    break;
                }
                case Constantes.TYPE_DOC_MISSION_NAME:
                    result[0] = "YvsMissions.findBySocieteC";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsGrhMissions.findByNumPiece";
                    result[4] = new String[]{"numeroPiece", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsGrhMissions.class;
                    result[7] = "smenMission";
                    result[8] = "managedMission";
                    break;
                case Constantes.MUT_TRANSACTIONS_MUT: {
                    result[0] = "YvsMutOperationCompte.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsMutOperationCompte.findByNumOp";
                    result[4] = new String[]{"numeroPiece", "mutuelle"};
                    result[5] = new Object[]{reference, currentMutuel};
                    result[6] = YvsMutOperationCompte.class;
                    result[7] = "smenOperation";
                    result[8] = "managedOperationCompte";
                    break;
                }
                case Constantes.MUT_ACTIVITE_CREDIT: {
                    result[0] = "YvsMutCredit.countAll";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsMutCredit.findByNumOp";
                    result[4] = new String[]{"numeroPiece", "mutuelle"};
                    result[5] = new Object[]{reference, currentMutuel};
                    result[6] = YvsMutCredit.class;
                    result[7] = "smenCredit";
                    result[8] = "managedCredit";
                    break;
                }
                case Constantes.PROD_TYPE_PROD_NAME: {
                    result[0] = "YvsProdOrdreFabrication.findAllC";
                    result[1] = new String[]{"societe"};
                    result[2] = new Object[]{currentAgence.getSociete()};
                    result[3] = "YvsProdOrdreFabrication.findByReference";
                    result[4] = new String[]{"codeRef", "societe"};
                    result[5] = new Object[]{reference, currentAgence.getSociete()};
                    result[6] = YvsProdOrdreFabrication.class;
                    result[7] = "smenOfSuivi2";
                    result[8] = "managedOrdresF";
                    break;
                }
                default:
                    result = null;
            }
        }
        return result;
    }

    public double setMontantTotalDoc(DocAchat bean) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(bean, societe, null, null, dao);
    }

    public double setMontantTotalDoc(DocAchat bean, YvsComDocAchats entity) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(bean, entity, societe, null, null, dao);
    }

    public double setMontantTotalDoc(DocAchat bean, Date dateDebut, Date dateFin) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(bean, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocAchat bean, YvsComDocAchats entity, Date dateDebut, Date dateFin) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(bean, entity, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocAchat bean, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        champ = new String[]{"docAchat"};
        val = new Object[]{new YvsComDocAchats(bean.getId())};
        List<YvsComContenuDocAchat> contenus = dao.loadNameQueries("YvsComContenuDocAchat.findByDocAchat", champ, val);
        return setMontantTotalDoc(bean, contenus, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocAchat bean, YvsComDocAchats entity, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        champ = new String[]{"docAchat"};
        val = new Object[]{entity};
        List<YvsComContenuDocAchat> contenus = dao.loadNameQueries("YvsComContenuDocAchat.findByDocAchat", champ, val);
        return setMontantTotalDoc(bean, entity, contenus, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(YvsComDocAchats entity) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(entity, societe, null, null, dao);
    }

    public double setMontantTotalDoc(YvsComDocAchats entity, Date dateDebut, Date dateFin) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(entity, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(YvsComDocAchats entity, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        champ = new String[]{"docAchat"};
        val = new Object[]{entity};
        List<YvsComContenuDocAchat> lc = dao.loadNameQueries("YvsComContenuDocAchat.findByDocAchat", champ, val);
        return setMontantTotalDoc(entity, lc, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocAchat bean, List<YvsComContenuDocAchat> contenus, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        return setMontantTotalDoc(bean, new YvsComDocAchats(bean.getId(), new YvsBaseFournisseur(bean.getFournisseur().getId())), contenus, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocAchat bean, YvsComDocAchats entity, List<YvsComContenuDocAchat> contenus, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        setMontantTotalDoc(entity, contenus, societe, dateDebut, dateFin, dao);

        bean.setMontantAvoir(entity.getMontantAvoir());
        bean.setMontantAvanceAvoir(entity.getMontantAvanceAvoir());
        bean.setMontantRemise(entity.getMontantRemise());
        bean.setMontantTaxe(entity.getMontantTaxe());
        bean.setMontantHT(entity.getMontantHT());
        bean.setMontantTTC(entity.getMontantTTC());
        bean.setMontantCS(entity.getMontantCS());
        bean.setMontantAvance(entity.getMontantAvance());
        bean.setMontantTaxeR(entity.getMontantTaxeR());
        bean.setMontantNetApayer(entity.getMontantNetApayer());

        update("blog_form_montant_doc_fa");
        update("blog_form_montant_doc_bca");
        update("blog_form_montant_doc_bla");

        return bean.getMontantTotal();
    }

    public double setMontantTotalDoc(YvsComDocAchats entity, List<YvsComContenuDocAchat> contenus, Date dateDebut, Date dateFin) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(entity, contenus, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(YvsComDocAchats entity, List<YvsComContenuDocAchat> contenus, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        if (entity != null) {
            entity.setMontantRemise(0);
            entity.setMontantTaxe(0);
            entity.setMontantHT(0);
            entity.setMontantTTC(0);
            entity.setMontantCS(0);
            entity.setMontantAvance(0.0);
            entity.setMontantTaxeR(0);
            entity.setMontantNetApayer(0);

            if (contenus != null ? !contenus.isEmpty() : false) {
                String query = "SELECT pua_ttc FROM yvs_base_article_fournisseur WHERE article = ? AND fournisseur = ?";
                Boolean pua_ttc;
                for (YvsComContenuDocAchat c : contenus) {
                    pua_ttc = (Boolean) dao.loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(c.getArticle().getId(), 1), new yvs.dao.Options(entity.getFournisseur().getId(), 2)});
                    if (pua_ttc == null) {
                        pua_ttc = c.getArticle().getPuaTtc();
                    }
                    entity.setMontantTTC(entity.getMontantTTC() + (c.getPrixTotal()));
                    entity.setMontantTaxe(entity.getMontantTaxe() + c.getTaxe());
                    entity.setMontantRemise(entity.getMontantRemise() + c.getRemise());
                    entity.setMontantTaxeR(entity.getMontantTaxeR() + ((pua_ttc) ? (c.getTaxe()) : 0));
                }
            }

            champ = new String[]{"docAchat", "sens"};
            val = new Object[]{entity, true};
            Double p = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocAchat.findSumByDocAchat", champ, val);
            double s = (p != null ? p : 0);
            entity.setMontantCS(s);

            if (dateFin == null) {
                champ = new String[]{"facture", "statut"};
                val = new Object[]{entity, Constantes.STATUT_DOC_PAYER};
                nameQueri = "YvsComptaCaissePieceAchat.findByFactureStatutS";
            } else {
                champ = new String[]{"facture", "statut", "date"};
                val = new Object[]{entity, Constantes.STATUT_DOC_PAYER, dateFin};
                nameQueri = "YvsComptaCaissePieceAchat.findByMensualiteStatutS";
            }
            Double r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            entity.setMontantAvance(r != null ? r : 0);

            double caAvoir = 0, avanceAvoir = 0;
            List<YvsComDocAchats> avoirs = dao.loadNameQueries("YvsComDocAchats.findByParentTypeDocStatut_", new String[]{"documentLie", "typeDoc", "statut"}, new Object[]{entity, Constantes.TYPE_FAA, Constantes.ETAT_VALIDE});
            for (YvsComDocAchats av : avoirs) {
                caAvoir += setMontantTotalDoc(av);
                avanceAvoir += av.getMontantAvance();
            }
            entity.setMontantAvoir(caAvoir);
            entity.setMontantAvanceAvoir(avanceAvoir);
            entity.setMontantRemise(arrondi(entity.getMontantRemise(), societe, dao));
            entity.setMontantTaxe(arrondi(entity.getMontantTaxe(), societe, dao));
            entity.setMontantHT(arrondi(entity.getMontantHT(), societe, dao));
            entity.setMontantTTC(arrondi(entity.getMontantTTC(), societe, dao));
            entity.setMontantCS(arrondi(entity.getMontantCS(), societe, dao));
            entity.setMontantAvance(arrondi(entity.getMontantAvance(), societe, dao));
            entity.setMontantTaxeR(arrondi(entity.getMontantTaxeR(), societe, dao));
            entity.setMontantNetApayer(arrondi(entity.getMontantNetApayer(), societe, dao));
            return entity.getMontantTotal();
        }
        return 0;
    }

    public double setMontantTotalDoc(DocVente bean) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(bean, societe, null, null, dao);
    }

    public double setMontantTotalDoc(DocVente bean, YvsComDocVentes entity) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(bean, entity, societe, null, null, dao);
    }

    public double setMontantTotalDoc(DocVente bean, Date dateDebut, Date dateFin) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(bean, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocVente bean, YvsComDocVentes entity, Date dateDebut, Date dateFin) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(bean, entity, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocVente bean, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        champ = new String[]{"docVente"};
        val = new Object[]{new YvsComDocVentes(bean.getId())};
        List<YvsComContenuDocVente> entity = dao.loadNameQueries("YvsComContenuDocVente.findByDocVente", champ, val);
        return setMontantTotalDoc(bean, entity, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocVente bean, YvsComDocVentes entity, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        champ = new String[]{"docVente"};
        val = new Object[]{entity};
        List<YvsComContenuDocVente> contenus = dao.loadNameQueries("YvsComContenuDocVente.findByDocVente", champ, val);
        return setMontantTotalDoc(bean, entity, contenus, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(YvsComDocVentes entity) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(entity, societe, null, null, dao);
    }

    public double setMontantTotalDoc(YvsComDocVentes entity, Date dateDebut, Date dateFin) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(entity, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(YvsComDocVentes entity, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        champ = new String[]{"docVente"};
        val = new Object[]{entity};
        List<YvsComContenuDocVente> contenus = dao.loadNameQueries("YvsComContenuDocVente.findByDocVente", champ, val);
        return setMontantTotalDoc(entity, contenus, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocVente bean, List<YvsComContenuDocVente> contenus) {
        return setMontantTotalDoc(bean, contenus, null, null);
    }

    public double setMontantTotalDoc(DocVente bean, List<YvsComContenuDocVente> contenus, Date dateDebut, Date dateFin) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(bean, contenus, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocVente bean, YvsComDocVentes entity, List<YvsComContenuDocVente> contenus, Date dateDebut, Date dateFin) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(bean, entity, contenus, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocVente bean, List<YvsComContenuDocVente> contenus, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        return setMontantTotalDoc(bean, new YvsComDocVentes(bean.getId()), contenus, societe, dateDebut, dateFin, dao);
    }

    public double setMontantTotalDoc(DocVente bean, YvsComDocVentes entity, List<YvsComContenuDocVente> contenus, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        setMontantTotalDoc(entity, contenus, societe, dateDebut, dateFin, dao);

        bean.setMontantRemise(entity.getMontantRemise());
        bean.setMontantTaxe(entity.getMontantTaxe());
        bean.setMontantRistourne(entity.getMontantRistourne());
        bean.setMontantCommission(entity.getMontantCommission());
        bean.setMontantHT(entity.getMontantHT());
        bean.setMontantTTC(entity.getMontantTTC());
        bean.setMontantRemises(entity.getMontantRemises());
        bean.setMontantCS(entity.getMontantCS());
        bean.setMontantAvance(entity.getMontantAvance());
        bean.setMontantPlanifier(entity.getMontantPlanifier());
        bean.setMontantAvoir(entity.getMontantAvoir());
        bean.setMontantAvanceAvoir(entity.getMontantAvanceAvoir());
        bean.setMontantTaxeR(entity.getMontantTaxeR());
        bean.setMontantResteApayer(entity.getMontantResteApayer());

        update("blog_form_montant_doc_blv");
        update("blog_form_montant_doc_bcv");
        update("blog_form_montant_doc_fv");
        update("blog_form_montant_doc_fav");

        return bean.getMontantTotal();
    }

    public double setMontantTotalDoc(YvsComDocVentes entity, List<YvsComContenuDocVente> contenus, Date dateDebut, Date dateFin) {
        long societe = currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0;
        return setMontantTotalDoc(entity, contenus, societe, dateDebut, dateFin, dao);
    }

    public void majMontantTotalDocAfterReg(YvsComDocVentes entity) {
        champ = new String[]{"facture", "statut"};
        val = new Object[]{entity, Constantes.STATUT_DOC_PAYER};
        nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutS";
        Double a = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        entity.setMontantAvance((a != null ? a : 0));
        entity.setMontantResteApayer(entity.getMontantResteApayer());
    }

    public double setMontantTotalDoc(YvsComDocVentes entity, List<YvsComContenuDocVente> contenus, long societe, Date dateDebut, Date dateFin, DaoInterfaceLocal dao) {
        if (entity != null) {
            entity.setMontantRemise(0);
            entity.setMontantTaxe(0);
            entity.setMontantRistourne(0);
            entity.setMontantCommission(0);
            entity.setMontantHT(0);
            entity.setMontantTTC(0);
            entity.setMontantRemises(0);
            entity.setMontantCS(0);
            entity.setMontantAvance(0.0);
            entity.setMontantTaxeR(0);
            entity.setMontantResteApayer(0);
            entity.setMontantPlanifier(0);
            if (contenus != null ? !contenus.isEmpty() : false) {
                for (YvsComContenuDocVente c : contenus) {
                    entity.setMontantRemise(entity.getMontantRemise() + c.getRemise());
                    entity.setMontantRistourne(entity.getMontantRistourne() + c.getRistourne());
                    entity.setMontantCommission(entity.getMontantCommission() + c.getComission());
                    entity.setMontantTTC(entity.getMontantTTC() + c.getPrixTotal());
                    entity.setMontantTaxe(entity.getMontantTaxe() + c.getTaxe());
                    entity.setMontantTaxeR(entity.getMontantTaxeR() + ((c.getArticle().getPuvTtc()) ? (c.getTaxe()) : 0));
                }
            }

            champ = new String[]{"facture", "statut"};
            val = new Object[]{entity, Constantes.STATUT_DOC_SUSPENDU};
            nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutSDiff";
            Double a = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            entity.setMontantPlanifier(a != null ? a : 0);

            if (dateFin == null) {
                champ = new String[]{"facture", "statut"};
                val = new Object[]{entity, Constantes.STATUT_DOC_PAYER};
                nameQueri = "YvsComptaCaissePieceVente.findByFactureStatutS";
                a = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                entity.setMontantAvance((a != null ? a : 0));
            } else {
                champ = new String[]{"facture", "statut", "date"};
                val = new Object[]{entity, Constantes.STATUT_DOC_PAYER, dateFin};
                nameQueri = "YvsComptaCaissePieceVente.findByMensualiteStatutS";
                a = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                entity.setMontantAvance(a != null ? a : 0);
            }
            champ = new String[]{"docVente", "sens", "service"};
            val = new Object[]{entity, true, true};
            Double p = (Double) dao.loadObjectByNameQueries("YvsComCoutSupDocVente.findSumServiceByDocVente", champ, val);
            double s = (p != null ? p : 0);
            entity.setMontantCS(s);
            double caAvoir = 0, avanceAvoir = 0;
            List<YvsComDocVentes> avoirs = dao.loadNameQueries("YvsComDocVentes.findByParentTypeDocStatut", new String[]{"documentLie", "typeDoc", "statut"}, new Object[]{entity, Constantes.TYPE_FAV, Constantes.ETAT_VALIDE});;
            for (YvsComDocVentes av : avoirs) {
                caAvoir += setMontantTotalDoc(av);
                avanceAvoir += av.getMontantAvance();
            }
            entity.setMontantAvoir(caAvoir);
            entity.setMontantAvanceAvoir(avanceAvoir);
            entity.setMontantRemise(arrondi(entity.getMontantRemise(), societe, dao));
            entity.setMontantTaxe(arrondi(entity.getMontantTaxe(), societe, dao));
            entity.setMontantRistourne(arrondi(entity.getMontantRistourne(), societe, dao));
            entity.setMontantCommission(arrondi(entity.getMontantCommission(), societe, dao));
            entity.setMontantHT(arrondi(entity.getMontantHT(), societe, dao));
            entity.setMontantTTC(arrondi(entity.getMontantTTC(), societe, dao));
            entity.setMontantRemises(arrondi(entity.getMontantRemises(), societe, dao));
            entity.setMontantCS(arrondi(entity.getMontantCS(), societe, dao));
            entity.setMontantAvance(arrondi(entity.getMontantAvance(), societe, dao));
            entity.setMontantTaxeR(arrondi(entity.getMontantTaxeR(), societe, dao));
            entity.setMontantResteApayer(arrondi(entity.getMontantResteApayer(), societe, dao));
            return entity.getMontantTotal();
        }
        return 0;
    }

    public double setMontantTotalDoc(DocStock doc, List<YvsComContenuDocStock> lc) {
        //total des bons planifié rattaché à la mission
        YvsComDocStocks y = new YvsComDocStocks(doc.getId());
        setMontantTotalDoc(y, lc);
        doc.setMontantTotal(y.getMontantTotal());
        doc.setMontantTotalES(y.getMontantTotalES());
        return doc.getMontantTotal();
    }

    public double setMontantTotalDoc(YvsComDocStocks doc) {
        champ = new String[]{"docStock"};
        val = new Object[]{doc};
        List<YvsComContenuDocStock> lc = dao.loadNameQueries("YvsComContenuDocStock.findByDocStock", champ, val);
        return setMontantTotalDoc(doc, lc);
    }

    public double setMontantTotalDoc(YvsComDocStocks doc, List<YvsComContenuDocStock> lc) {
        //total des bons planifié rattaché à la mission
        doc.setMontantTotal(0);
        doc.setMontantTotalES(0);
        for (YvsComContenuDocStock c : lc) {
            doc.setMontantTotal(doc.getMontantTotal() + c.getPrixTotal());
            doc.setMontantTotalES(doc.getMontantTotalES() + (c.getQuantiteEntree() * c.getPrixEntree()));
        }
        return doc.getMontantTotal();
    }

    public YvsGrhMissions setMontantTotalMission(YvsGrhMissions mi, Mission mission) {
        //total des bons planifié rattaché à la mission
        Double montant = (Double) dao.loadObjectByNameQueries("YvsComptaJustifBonMission.getTotalBon", new String[]{"mission", "statut"}, new Object[]{mi, Constantes.ETAT_ANNULE});
        mission.setTotalBon(montant != null ? montant : 0d);
        //total des bons déjà payé rattaché à la mission
        montant = (Double) dao.loadObjectByNameQueries("YvsComptaJustifBonMission.getTotalBonPaye", new String[]{"mission", "statut", "statutP"}, new Object[]{mi, Constantes.ETAT_ANNULE, Constantes.ETAT_REGLE});
        mission.setTotalBonPaye(montant != null ? montant : 0d);
        //total des pièce de caisse mission
        montant = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceMission.findTotalPieceByMission", new String[]{"mission", "statut"}, new Object[]{mi, Constantes.STATUT_DOC_ANNULE});
        mission.setTotalPiece(montant != null ? montant : 0d);
        //total des pièce de caisse mission dejà payé
        montant = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceMission.findTotalPiecePayeByMission", new String[]{"mission", "statut"}, new Object[]{mi, Constantes.STATUT_DOC_PAYER});
        mission.setTotalPiecePaye(montant != null ? montant : 0d);
        mission.setTotalPaye(mission.getTotalBonPaye() + mission.getTotalPiecePaye());
        montant = (Double) dao.loadObjectByNameQueries("YvsGrhFraisMission.sumByMission", new String[]{"mission"}, new Object[]{mi});
        mission.setTotalFraisMission(montant != null ? montant : 0d);
        mission.setRestPlanifier(mission.getTotalFraisMission() - mission.getTotalPiece() - mission.getTotalBon());
        mi.setTotalBon(mission.getTotalBon());
        mi.setTotalFraisMission(mission.getTotalFraisMission());
        mi.setTotalPiece(mission.getTotalPiece());
        mi.setTotalRegle(mission.getTotalPaye());
        mi.setTotalReste(mission.getTotalFraisMission() - mission.getTotalPaye());
        mi.setTotalResteAPlanifier(mission.getRestPlanifier());
        mi.setTotalBonPaye(mission.getTotalBonPaye());
        return mi;
    }

    public void setMontantRemise(YvsComRemiseDocVente r, DocVente d) {
        r.setMontant(getMontantRemise(r.getRemise(), d));
    }

    public void setMontantRemise(YvsComRemiseDocVente r, YvsComDocVentes d) {
        r.setMontant(getMontantRemise(r.getRemise(), d));
    }

    public double getMontantRemise(YvsComRemise r, DocVente d) {
        return getMontantRemise(r, new YvsComDocVentes(d.getId(), d.getMontantTTC()));
    }

    public double getMontantRemise(YvsComRemise r, YvsComDocVentes d) {
        double remise = 0, ttc = d.getMontantTTC();

        champ = new String[]{"docVente"};
        val = new Object[]{d};
        nameQueri = "YvsComContenuDocVente.findQteByDocVente";
        Double c = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        double valeur = (c != null ? c : 0);

        champ = new String[]{"remise", "base1", "valeur1", "base2", "valeur2"};
        val = new Object[]{r, Constantes.BASE_QTE, valeur, Constantes.BASE_CA, ttc};
        nameQueri = "YvsComGrilleRemise.findByRemiseMontantBase";
        List<YvsComGrilleRemise> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        if (l != null ? !l.isEmpty() : false) {
            YvsComGrilleRemise g = l.get(0);
            switch (g.getNatureMontant()) {
                case Constantes.NATURE_MTANT:
                    remise = g.getMontantRemise();
                    break;
                case Constantes.NATURE_TAUX:
                    remise = (ttc * g.getMontantRemise()) / 100;
                    break;
                default:
                    if (g.getMontantRemise() > 100) {
                        remise = g.getMontantRemise();
                    } else {
                        remise = (ttc * g.getMontantRemise()) / 100;
                    }
                    break;
            }
        }
        return remise;
    }

    public double getMontantRemise(YvsComRemise r, YvsComContenuDocVente d) {
        return (Double) getValeursRemise(r, d)[2];
    }

    public Object[] getValeursRemise(YvsComRemise r, YvsComContenuDocVente d) {
        Object[] result = new Object[3];
        double remise = 0, ttc = d.getPrixTotal(), valeur = d.getQuantite();
        champ = new String[]{"remise", "base1", "valeur1", "base2", "valeur2"};
        val = new Object[]{r, Constantes.BASE_QTE, valeur, Constantes.BASE_CA, ttc};
        nameQueri = "YvsComGrilleRemise.findByRemiseMontantBase";
        List<YvsComGrilleRemise> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        if (l != null ? !l.isEmpty() : false) {
            YvsComGrilleRemise g = l.get(0);
            result[0] = g.getMontantRemise();
            switch (g.getNatureMontant()) {
                case Constantes.NATURE_MTANT:
                    if (g.getBase().equals(Constantes.BASE_QTE)) {
                        remise = valeur * g.getMontantRemise();
                    } else {
                        remise = g.getMontantRemise();
                    }
                    result[1] = false;
                    break;
                case Constantes.NATURE_TAUX:
                    remise = (ttc * g.getMontantRemise()) / 100;
                    result[1] = true;
                    break;
                default:
                    if (g.getMontantRemise() > 100) {
                        remise = g.getMontantRemise();
                        result[1] = false;
                    } else {
                        remise = (ttc * g.getMontantRemise()) / 100;
                        result[1] = true;
                    }
                    break;
            }
        }
        result[2] = remise;
        return result;
    }

    public void choosePaginator(ValueChangeEvent ev) {
        try {
            if ((ev != null) ? ev.getNewValue() != null : false) {
                long v;
                try {
                    v = (long) ev.getNewValue();
                } catch (Exception ex) {
                    v = (int) ev.getNewValue();
                }
                this.nbMax = (int) v;
                this.imax = this.nbMax;
                this.idebut = 0;
                paginator.setRows((int) imax);
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    public boolean controleDocStock(long id, Date heure, long depot, Date date) {
        return controleDocStock(id, heure, depot, date, true);
    }

    public boolean controleDocStock(long id, Date heure, long depot, Date date, boolean error) {
        List<Object[]> nb = controleDocsStock(id, heure, depot, date);
        if (nb != null ? nb.size() > 0 : false) {
            if (error) {
                getErrorMessage("Vous ne pouvez pas poursuivre ce traitement. des documents de stocks non validés ont été trouvé dans ce dépôt");
            } else {
                getWarningMessage("Vous ne pouvez pas poursuivre ce traitement. des documents de stocks non validés ont été trouvé dans ce dépôt");
            }
            return false;
        }
        return true;
    }

    public List<Object[]> controleDocsStock(long id, Date heure, long depot, Date date) {
        //Vérifie que tous les stocks sont validé avant la date de l'inventaire
        List<String> etats = new ArrayList<>();
        etats.add(Constantes.ETAT_EDITABLE);
        etats.add(Constantes.ETAT_ATTENTE);
        etats.add(Constantes.ETAT_ENCOURS);
        etats.add(Constantes.ETAT_SOUMIS);
        //trouve les tranches précédent une tranche donnée
        if (heure != null) {
            champ = new String[]{"heure", "depot"};
            val = new Object[]{heure, new YvsBaseDepots(depot)};
            nameQueri = "YvsComCreneauDepot.findByIdsPrecedent";
        } else {
            champ = new String[]{"depot"};
            val = new Object[]{new YvsBaseDepots(depot)};
            nameQueri = "YvsComCreneauDepot.findByIdsByDepot";
        }
        List<Long> ids = dao.loadNameQueries(nameQueri, champ, val);
        if (ids.isEmpty()) {
            ids.add(0L);
        }
        champ = new String[]{"statuts", "depot", "date", "tranches", "id"};
        val = new Object[]{etats, new YvsBaseDepots(depot), date, ids, id};
        return dao.loadListByNameQueries("YvsComDocStocks.findByNumAndTypeNonValideNoId", champ, val);
    }

    public boolean controleInventaire(long depot, Date date, Long tranches) {
        return controleInventaire(depot, date, tranches, true);
    }

    public boolean controleInventaire(long depot, Date date, Long tranches, boolean message) {
        if (!dao.controleInventaire(depot, date, (tranches != null ? tranches : 0))) {
            if (message) {
                getErrorMessage("Vous ne pouvez créer une fiche de stock à cette date car un ou plusieurs inventaires ont déjà été réalisés après dans ce dépot");
            }
            return false;
        }
        return true;
    }

    public boolean controleEcartVente(long users, Date date, boolean message) {
        if (!dao.controleEcartVente(users, date)) {
            if (message) {
                getErrorMessage("Vous ne pouvez créer une fiche de vente à cette date car un ou plusieurs manquants ont déjà été réalisés après pour ce vendeur");
            }
            return false;
        }
        return true;
    }

    public boolean verifyTransfer(long depot, Date date) {
        Long nb = (Long) dao.loadObjectByNameQueries("YvsComDocStocks.countTrNonValide", new String[]{"type", "depot", "statut", "date"}, new Object[]{Constantes.TYPE_FT, new YvsBaseDepots(depot), Constantes.ETAT_VALIDE, date});
        return (nb != null) ? nb <= 0 : true;
    }

    public boolean verifyInventaire(Depots depot, TrancheHoraire tranche, Date date) {
        return controleInventaire(depot.getId(), date, tranche.getId(), true);
    }

    public boolean verifyInventaire(Depots depot, TrancheHoraire tranche, Date date, boolean message) {
        return controleInventaire(depot.getId(), date, tranche.getId(), message);
    }

    public boolean verifyInventaire(YvsBaseDepots depot, YvsGrhTrancheHoraire tranche, Date date) {
        return controleInventaire(depot.getId(), date, tranche.getId(), true);
    }

    public boolean verifyInventaire(YvsBaseDepots depot, YvsGrhTrancheHoraire tranche, Date date, boolean message) {
        return controleInventaire(depot.getId(), date, tranche.getId(), message);
    }

    public boolean verifyTranche(TrancheHoraire tranche, Depots depot, Date date) {
        if (date == null) {
            getErrorMessage("Vous devez precisez la date de traitement");
            return false;
        }
        if (depot != null ? depot.getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le dépôt");
            return false;
        }
        if (tranche != null ? tranche.getId() < 1 : true) {
            return true;
        }
        YvsBaseDepots d = (YvsBaseDepots) dao.loadOneByNameQueries("YvsBaseDepots.findById", new String[]{"id"}, new Object[]{depot.getId()});
        YvsGrhTrancheHoraire t = (YvsGrhTrancheHoraire) dao.loadOneByNameQueries("YvsGrhTrancheHoraire.findById", new String[]{"id"}, new Object[]{tranche.getId()});
        return verifyTranche(t, d, date);
    }

    public boolean verifyTranche(YvsGrhTrancheHoraire tranche, YvsBaseDepots depot, Date date) {
        if (date == null) {
            getErrorMessage("Vous devez precisez le date de traitement");
            return false;
        }
        if (depot != null ? depot.getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le dépôt");
            return false;
        }
        if (tranche != null ? tranche.getId() > 0 : false) {
            if (depot.getCrenau()) {
                Long y = (Long) dao.loadOneByNameQueries("YvsBaseMouvementStock.countMvtOtherTypeJ", new String[]{"depot", "dateDoc", "typeJ"},
                        new Object[]{depot, date, tranche.getTypeJournee()});
                if (y != null ? y > 0 : false) {
                    getErrorMessage("Vous ne pouvez pas enregistrer ce document à cette tranche", "car le dernier document est dans un autre type de tranche");
                    return false;
                }
            }
        }
        return true;
    }

    public List<YvsGrhTrancheHoraire> loadTranche(YvsBaseDepots y, Date date) {
        return loadTranche(null, y, date);
    }

    public List<YvsGrhTrancheHoraire> loadTranche(YvsUsers u, YvsBaseDepots y, Date date) {
        Long count = (Long) dao.loadObjectByNameQueries("YvsComCreneauDepot.countByDepot", new String[]{"depot"}, new Object[]{y});
        if (!((count != null ? count > 1 : false) && y.getCrenau())) {
            date = null;
        }
        if (date == null) {
            nameQueri = "YvsComCreneauDepot.findTrancheByDepot";
            champ = new String[]{"depot"};
            val = new Object[]{y};
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date debut = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, 2);
            Date fin = cal.getTime();

            nameQueri = "YvsComCreneauHoraireUsers.findTrancheByDepotDates";
            champ = new String[]{"depot", "debut", "fin"};
            val = new Object[]{y, debut, fin};
            if (u != null ? u.getId() > 0 : false) {
                nameQueri = "YvsComCreneauHoraireUsers.findTrancheByUsersDepotDates";
                champ = new String[]{"users", "depot", "debut", "fin"};
                val = new Object[]{u, y, debut, fin};
            }
        }
        List<YvsGrhTrancheHoraire> l = dao.loadNameQueries(nameQueri, champ, val);
        return l != null ? l : new ArrayList<YvsGrhTrancheHoraire>();
    }

    public List<YvsGrhTrancheHoraire> loadTranches(YvsBaseDepots y, Date date) {
        return loadTranche(null, y, date);
    }

    public List<YvsGrhTrancheHoraire> loadTranches(YvsUsers u, YvsBaseDepots y, Date date) {
        String jour = Util.getDay(date);
        nameQueri = "YvsComCreneauDepot.findTrancheCurrentByDepot";
        champ = new String[]{"depot", "jour"};
        val = new Object[]{y, jour};
        if (u != null ? u.getId() > 0 : false) {
            nameQueri = "YvsComCreneauHoraireUsers.findTrancheDepotByUsersDateDepot";
            champ = new String[]{"users", "depot", "date"};
            val = new Object[]{u, y, date};
        }
        List<YvsGrhTrancheHoraire> l = dao.loadNameQueries(nameQueri, champ, val);
        return l != null ? l : new ArrayList<YvsGrhTrancheHoraire>();
    }

    public boolean verifyDateExercice(Date date) {
        date = date != null ? date : new Date();
        champ = new String[]{"dateJour", "societe"};
        val = new Object[]{date, currentAgence.getSociete()};
        YvsMutPeriodeExercice periode = (YvsMutPeriodeExercice) dao.loadOneByNameQueries("YvsMutPeriodeExercice.findActifByDate", champ, val);
        if (periode != null ? periode.getId() > 0 : false) {
            if (periode.getCloture()) {
                getErrorMessage("Le document ne peut pas être enregistré dans une periode cloturée");
                return false;
            }
            if (periode.getExercice().getCloturer()) {
                getErrorMessage("Le document ne peut pas être enregistré dans un exercice cloturé");
                return false;
            }
        } else {
            getErrorMessage("Le document doit être enregistré dans un exercice actif");
            return false;
        }
//        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", champ, val);
//        if (exo != null ? exo.getId() < 1 : true) {
//            getErrorMessage("Le document doit être enregistré dans un exercice actif");
//            return false;
//        }
//        if (exo.getCloturer()) {
//            getErrorMessage("Le document ne peut pas être enregistré dans un exercice cloturé");
//            return false;
//        }
        return true;
    }

    public boolean verifyDate(Date date, int ecart) {
        date = date != null ? date : new Date();
        if (!verifyDateExercice(date)) {
            return false;
        }

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        now.add(Calendar.DATE, 1);

        Calendar d = Calendar.getInstance();
        d.setTime(date);
        d.set(Calendar.HOUR_OF_DAY, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.SECOND, 0);
        d.set(Calendar.MILLISECOND, 0);
        if (d.after(now)) {
            getErrorMessage("La date ne doit pas être superieur à la date du jour");
            return false;
        }
        if (ecart > 0) {
            now.add(Calendar.DATE, -ecart);
            if (d.before(now)) {
                if (autoriser("com_save_hors_limit")) {
                    return true;
                } else {
                    getErrorMessage("La date ne doit pas excedé le nombre de jour de retrait prévu");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean verifyDateAchat(Date date, boolean update) {
        int ecart = -1;
        if (!update) {
            List<YvsComParametreAchat> lp = dao.loadNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence}, 0, 1);
            if ((lp != null) ? !lp.isEmpty() : false) {
                ecart = lp.get(0).getJourAnterieur();
            }
        }
        return verifyDate(date, ecart);
    }

    public boolean verifyDateVente(Date date, boolean update) {
        int ecart = -1;
        if (!update) {
            List<YvsComParametreVente> lp = dao.loadNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence}, 0, 1);
            if ((lp != null) ? !lp.isEmpty() : false) {
                ecart = lp.get(0).getJourAnterieur();
            }
        }
        return verifyDate(date, ecart);
    }

    public boolean verifyDateStock(Date date, boolean update) {
        int ecart = -1;
        if (!update) {
            List<YvsComParametreStock> lp = dao.loadNameQueries("YvsComParametreStock.findByAgence", new String[]{"agence"}, new Object[]{currentAgence}, 0, 1);
            if ((lp != null) ? !lp.isEmpty() : false) {
                ecart = lp.get(0).getJourAnterieur();
            }
        }
        return verifyDate(date, ecart);
    }

    public boolean verifyCancelPieceCaisse(Date datePaiement) {
//        YvsComptaParametre param = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if (paramCompta != null ? paramCompta.getJourAnterieurCancel() > 0 : false) {
            return verifyCancelPieceCaisse(datePaiement, paramCompta.getJourAnterieurCancel());
        }
        return true;
    }

    public boolean verifyCancelPieceCaisse(Date datePaiement, int ecart) {
        Calendar date = Calendar.getInstance();
        if (datePaiement != null) {
            date.setTime(datePaiement);
            date.set(Calendar.HOUR, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.MILLISECOND, 0);

            Calendar now = Calendar.getInstance();
            now.add(Calendar.DATE, -ecart);
            now.set(Calendar.HOUR, 0);
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MINUTE, 0);
            now.set(Calendar.MILLISECOND, 0);
            if (ecart > 0 ? date.before(now) : false) {
                if (!autoriser("compta_virement_cancel_pass")) {
                    getErrorMessage("Vous n'avez pas le droit d'annuler une pièce qui est déjà passée");
                    return false;
                }
            }
        }
        return true;
    }

    public String buildReferenceElement(String element, Date date, long point, long depot) {
        ModeleReference modele = rechercheModeleReference(element);
        if ((modele != null) ? modele.getId() > 0 : false) {
            String inter = modele.getPrefix();
            if (modele.isCodePoint()) {
                if (point > 0) {
                    champ = new String[]{"id"};
                    val = new Object[]{point};
                    YvsBasePointVente p = (YvsBasePointVente) dao.loadObjectByNameQueries("YvsBasePointVente.findById", champ, val);
                    if (p != null ? p.getId() > 0 : false) {
                        if (p.getCode().length() > modele.getLongueurCodePoint()) {
                            modele.setCodePointvente(p.getCode().substring(0, modele.getLongueurCodePoint()));
                        } else {
                            modele.setCodePointvente(p.getCode());
                        }
                    } else {
                        if (currentPoint != null ? currentPoint.getCode() != null : false) {
                            if (currentPoint.getCode().length() > modele.getLongueurCodePoint()) {
                                modele.setCodePointvente(currentPoint.getCode().substring(0, modele.getLongueurCodePoint()));
                            } else {
                                modele.setCodePointvente(currentPoint.getCode());
                            }
                        }
                    }
                } else if (currentPoint != null ? currentPoint.getCode() != null : false) {
                    if (currentPoint.getCode().length() > modele.getLongueurCodePoint()) {
                        modele.setCodePointvente(currentPoint.getCode().substring(0, modele.getLongueurCodePoint()));
                    } else {
                        modele.setCodePointvente(currentPoint.getCode());
                    }
                } else {
                    if (depot > 0) {
                        champ = new String[]{"id"};
                        val = new Object[]{depot};
                        YvsBaseDepots p = (YvsBaseDepots) dao.loadObjectByNameQueries("YvsBaseDepots.findById", champ, val);
                        if (p != null ? p.getId() > 0 : false) {
                            if (p.getAbbreviation().length() > modele.getLongueurCodePoint()) {
                                modele.setCodePointvente(p.getAbbreviation().substring(0, modele.getLongueurCodePoint()));
                            } else {
                                modele.setCodePointvente(p.getAbbreviation());
                            }
                        }
                    } else {
                        if (currentAgence != null ? currentAgence.getAbbreviation() != null : false) {
                            if (currentAgence.getAbbreviation().length() > modele.getLongueurCodePoint()) {
                                modele.setCodePointvente(currentAgence.getAbbreviation().substring(0, modele.getLongueurCodePoint()));
                            } else {
                                modele.setCodePointvente(currentAgence.getAbbreviation());
                            }
                        } else {
                            if (currentAgence.getSociete() != null ? currentAgence.getSociete().getCodeAbreviation() != null : false) {
                                if (currentAgence.getSociete().getCodeAbreviation().length() > modele.getLongueurCodePoint()) {
                                    modele.setCodePointvente(currentAgence.getSociete().getCodeAbreviation().substring(0, modele.getLongueurCodePoint()));
                                } else {
                                    modele.setCodePointvente(currentAgence.getSociete().getCodeAbreviation());
                                }
                            }
                        }
                    }
                }
                inter += modele.getCodePointvente();
            }
            inter += modele.getSeparateur();
            return inter;
        } else {
            getErrorMessage("Cet élément n'a pas de modele de reference!");
            return "";
        }
    }

    public String buildReferenceElement(ModeleReference modele, Date date, long point, long depot) {
        String inter = modele.getPrefix();
        Calendar cal = Util.dateToCalendar(date);
        if (modele.isCodePoint()) {
            inter += ".";
            if (point > 0) {
                champ = new String[]{"id"};
                val = new Object[]{point};
                YvsBasePointVente p = (YvsBasePointVente) dao.loadObjectByNameQueries("YvsBasePointVente.findById", champ, val);
                if (p != null ? p.getId() > 0 : false) {
                    if (p.getCode().length() > modele.getLongueurCodePoint()) {
                        modele.setCodePointvente(p.getCode().substring(0, modele.getLongueurCodePoint()));
                    } else {
                        modele.setCodePointvente(p.getCode());
                    }
                } else {
                    if (currentPoint != null ? currentPoint.getCode() != null : false) {
                        if (currentPoint.getCode().length() > modele.getLongueurCodePoint()) {
                            modele.setCodePointvente(currentPoint.getCode().substring(0, modele.getLongueurCodePoint()));
                        } else {
                            modele.setCodePointvente(currentPoint.getCode());
                        }
                    }
                }
            } else if (currentPoint != null ? currentPoint.getCode() != null : false) {
                if (currentPoint.getCode().length() > modele.getLongueurCodePoint()) {
                    modele.setCodePointvente(currentPoint.getCode().substring(0, modele.getLongueurCodePoint()));
                } else {
                    modele.setCodePointvente(currentPoint.getCode());
                }
            } else {
                if (depot > 0) {
                    champ = new String[]{"id"};
                    val = new Object[]{depot};
                    YvsBaseDepots p = (YvsBaseDepots) dao.loadObjectByNameQueries("YvsBaseDepots.findById", champ, val);
                    if (p != null ? p.getId() > 0 : false) {
                        if (p.getAbbreviation().length() > modele.getLongueurCodePoint()) {
                            modele.setCodePointvente(p.getAbbreviation().substring(0, modele.getLongueurCodePoint()));
                        } else {
                            modele.setCodePointvente(p.getAbbreviation());
                        }
                    }
                } else {
                    if (currentAgence != null ? currentAgence.getAbbreviation() != null : false) {
                        if (currentAgence.getAbbreviation().length() > modele.getLongueurCodePoint()) {
                            modele.setCodePointvente(currentAgence.getAbbreviation().substring(0, modele.getLongueurCodePoint()));
                        } else {
                            modele.setCodePointvente(currentAgence.getAbbreviation());
                        }
                    } else {
                        if (currentAgence.getSociete() != null ? currentAgence.getSociete().getCodeAbreviation() != null : false) {
                            if (currentAgence.getSociete().getCodeAbreviation().length() > modele.getLongueurCodePoint()) {
                                modele.setCodePointvente(currentAgence.getSociete().getCodeAbreviation().substring(0, modele.getLongueurCodePoint()));
                            } else {
                                modele.setCodePointvente(currentAgence.getSociete().getCodeAbreviation());
                            }
                        }
                    }
                }
            }
            inter += modele.getCodePointvente();
        }
        inter += modele.getSeparateur();
        if (modele.isJour()) {
            if (cal.get(Calendar.DATE) > 9) {
                inter += Integer.toString(cal.get(Calendar.DATE));
            }
            if (cal.get(Calendar.DATE) < 10) {
                inter += ("0" + Integer.toString(cal.get(Calendar.DATE)));
            }
        }
        if (modele.isMois()) {
            if (cal.get(Calendar.MONTH) + 1 > 9) {
                inter += Integer.toString(cal.get(Calendar.MONTH) + 1);
            }
            if (cal.get(Calendar.MONTH) + 1 < 10) {
                inter += ("0" + Integer.toString(cal.get(Calendar.MONTH) + 1));
            }
        }
        if (modele.isAnnee()) {
            inter += Integer.toString(cal.get(Calendar.YEAR)).substring(2);
        }
        inter += modele.getSeparateur();
        return inter;
    }

    public double quantiteContenu(DocAchat doc) {
        return quantiteContenu(new YvsComDocAchats(doc.getId()));
    }

    public double quantiteContenu(YvsComDocAchats doc) {
        nameQueri = "YvsComContenuDocAchat.findByDocAchatQ";
        champ = new String[]{"docAchat"};
        val = new Object[]{doc};
        Double q = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        return q != null ? q : 0;
    }

    public double findLastPr(long article, long depot, Date date, long conditionnement) {
        nameQueri = "SELECT public.get_pr(?,?,?,?,?)";
        Double q = (Double) dao.loadObjectBySqlQuery(nameQueri, new yvs.dao.Options[]{new yvs.dao.Options(article, 1), new yvs.dao.Options(depot, 2), new yvs.dao.Options(0, 3),
            new yvs.dao.Options(date, 4), new yvs.dao.Options(conditionnement, 5)});
        return q != null ? q : 0;
    }

    public List<Integer> decomposeSelection(String selection) {
        List<Integer> re = new ArrayList<>();
        List<String> l = new ArrayList<>();
        if (selection != null) {
            String numroLine[] = selection.split("-");
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    re.add(index);
                    l.add(numroLine1);
                }
            } catch (NumberFormatException ex) {
                selection = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        selection += numroLine1 + "-";      //mémorise la selection
                    }
                }
                getErrorMessage("Impossible de terminer cette opération !", "des élément de votre sélection doivent être encore en liaison");
            }
        }
        return re;
    }

    public List<Long> decomposeIdSelection(String selection) {
        List<Long> re = new ArrayList<>();
        List<String> l = new ArrayList<>();
        if (selection != null) {
            String numroLine[] = selection.split("-");
            try {
                long id;
                for (String numroLine1 : numroLine) {
                    id = Long.valueOf(numroLine1);
                    re.add(id);
                    l.add(numroLine1);
                }
            } catch (NumberFormatException ex) {
                selection = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        selection += numroLine1 + "-";      //mémorise la selection
                    }
                }
                getErrorMessage("Impossible de terminer cette opération !", "des élément de votre sélection doivent être encore en liaison");
            }
        }
        return re;
    }
    /**/

    /**
     * *DEBUT DU BLOC DE CODE A ENLEVER PLUS TARD**
     */
    private String buildRequete(List<ParametreRequete> lp) {
        String re = "";
        lp.get(lp.size() - 1).setLastParam(true);
        for (ParametreRequete p : lp) {
            re += decomposeRequete(p) + "" + ((p.isLastParam()) ? "" : " " + p.getPredicat()) + " ";
        }
        return re;
    }

    private String decomposeRequete(ParametreRequete p) {
        String re = "";
        if (p.getAttribut() != null) {
            re += concateneParam(p);
        }
        if (!p.getOtherExpression().isEmpty()) {
            p.getOtherExpression().get(p.getOtherExpression().size() - 1).setLastParam(true);
            if (p.getAttribut() != null) {
                re += " " + p.getPredicat() + " (";
            } else {
                re += "(";
            }
            for (ParametreRequete p1 : p.getOtherExpression()) {
                re += decomposeRequete(p1) + "" + ((p1.isLastParam()) ? "" : " " + p1.getPredicat()) + " ";
            }
            re += ")";
        }
        return re;
    }
    /**/

    private String concateneParam(ParametreRequete p) {
        String re = "";
        if (p.getOperation().equals("BETWEEN")) {
            re += "(y." + p.getAttribut() + " " + p.getOperation() + " :" + p.getParamNome() + " AND :" + p.getParamNome() + "" + 1 + ")";
        } else {
            re += "y." + p.getAttribut() + "" + p.getOperation() + ":" + p.getParamNome();
        }
        return re;
    }

    public String buildDynamicQuery(List<ParametreRequete> params, String basicQuery) {
        StringBuilder sb = new StringBuilder(basicQuery);
        int i = 0;
        for (ParametreRequete p : params) {
            if (i != (params.size() - 1)) {     //cas du dernier paramètre
                if (!p.getOperation().equals("BETWEEN")) {
                    sb.append(" y.").append(p.getAttribut()).append(p.getOperation()).append(" :").append(p.getParamNome()).append(" ").append(p.getPredicat()).append(" ");
                } else {
                    sb.append(" (y.").append(p.getAttribut()).append(" BETWEEN :").append(p.getParamNome()).append(" AND ").append(":").append(p.getParamNome()).append("1) ").append(p.getPredicat()).append(" ");
                }
            } else {
                if (!p.getOperation().equals("BETWEEN")) {
                    sb.append(" y.").append(p.getAttribut()).append(p.getOperation()).append(" :").append(p.getParamNome());
                } else {
                    sb.append(" (y.").append(p.getAttribut()).append(" BETWEEN :").append(p.getParamNome()).append(" AND ").append(":").append(p.getParamNome()).append("1) ");
                }
            }
            i++;
        }
        buildDinamycParam(params);
        return sb.toString();
//        return basicQuery + " " + buildRequete(params);
    }

    public void buildDinamycParam(List<ParametreRequete> params) {
        List<ParametreRequete> lp = new ArrayList<>();
        for (ParametreRequete p : params) {
            if (!p.getOperation().equals("BETWEEN")) {
                lp.add(p);
            } else {
                lp.add(p);
                p = new ParametreRequete(p.getAttribut(), p.getParamNome() + "1", p.getOtherObjet());
                lp.add(p);
            }
//            ajouteParam(p, lp);
        }
        int i = 0;
        champ = new String[lp.size()];
        val = new Object[lp.size()];
        for (ParametreRequete p : lp) {
            champ[i] = p.getParamNome();
            val[i] = p.getObjet();
            i++;
        }
    }

    /**
     * * FIN BLOC DE CODE A ENLEVER PLUS TARD
     *
     **
     * @param date
     * @return
     */
    public YvsBaseExercice getExerciceActif(Date date) {
        if (date == null) {
            date = new Date();
        }
        if (currentExo != null) {
            if ((currentExo.getDateDebut().before(date) || currentExo.getDateDebut().equals(Utilitaire.giveOnlyDate(date)))
                    && (currentExo.getDateFin().after(date) || currentExo.getDateFin().equals(Utilitaire.giveOnlyDate(date)))) {
                return currentExo;
            }
        }
        if (currentAgence != null) {
            return (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentAgence.getSociete(), date});
        } else {
            return null;
        }

    }

    public YvsBaseExercice giveExerciceActif(Date date) {
        if (date == null) {
            date = new Date();
        }
        if (currentExo != null) {
            if ((currentExo.getDateDebut().before(date) || currentExo.getDateDebut().equals(Utilitaire.giveOnlyDate(date)))
                    && (currentExo.getDateFin().after(date) || currentExo.getDateFin().equals(Utilitaire.giveOnlyDate(date)))) {
                return currentExo;
            }
        }
        if (currentUser != null) {
            currentExo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findByActif", new String[]{"actif", "societe", "date"}, new Object[]{true, currentUser.getAgence().getSociete(), date});
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("exo", currentExo);
            return currentExo;
        } else {
            return null;
        }

    }

    public YvsBaseExercice giveExerciceActif() {
        return giveExerciceActif(new Date());

    }

    public List<YvsBaseExercice> giveListExerciceActif() {
        if (currentUser != null) {
            return dao.loadNameQueries("YvsBaseExercice.findAll", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
        } else {
            return null;
        }

    }

    public String resumeText(String str) {
        return resumeText(str, 50);
    }

    public String resumeText(String str, int length) {
        if (str != null) {
            if (str.length() > length) {
                return str.substring(0, (length > 5 ? (length - 5) : length)).concat("...");
            } else {
                return str;
            }
        }
        return "";
    }

    public String doubleLine(String str) {
        return doubleLine(str, 50);
    }

    public String doubleLine(String str, int length) {
        if (str != null) {
            if (str.length() > length) {
                String result = null;
                while (true) {
                    if (result == null) {
                        result = str.substring(0, length);
                    } else {
                        if (str.length() > length) {
                            result += " \n " + str.substring(0, length);
                        } else {
                            result += " \n " + str;
                            break;
                        }
                    }
                    str = str.substring(length, str.length() - length);
                }
                System.err.println("result " + result);
                return result;
            } else {
                return str;
            }
        }
        return "";
    }

    public void addParam(ParametreRequete prq) {
        paginator.addParam(prq);
    }

    public List<S> executeDynamicQuery(String entyty, String paramsOrder, boolean avance, boolean init) {
        return executeDynamicQuery("y", "y", entyty, paramsOrder, avance, init);
    }

    public List<S> executeDynamicQuery(String field, String fieldCount, String entity, String paramsOrder, boolean avance, boolean init) {
        List<S> list = paginator.executeDynamicQuery(field, fieldCount, entity, paramsOrder, avance, init, (int) imax, dao);
        return list;
    }

    public List<ParamDate> giveSeriesDate(Date debut, Date fin, String periode, int nbPeriode) {
        List<ParamDate> re = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        ParamDate param;
        String pe = "";
        if (debut != null && fin != null) {
            cal.setTime(debut);
            while (debut.before(fin) || debut.equals(fin)) {
                param = new ParamDate();
                switch (periode) {
                    case "DAY":
                        param.setDebut(debut);
                        if (nbPeriode == 1) {
                            param.setPeriode(formatDayM.format(debut));
                        } else {
                            pe = "(" + formatDay.format(debut) + "-";
                        }
                        cal.add(Calendar.DAY_OF_MONTH, nbPeriode > 1 ? (nbPeriode - 1) : nbPeriode);
                        debut = cal.getTime();
                        param.setFin(debut);
                        if (nbPeriode > 1) {
                            pe += formatDay.format(debut) + ")";
                        }
                        param.setPeriode(pe);
                        break;
                    case "WEEK":
                        param.setDebut(debut);
                        pe = "(" + formatDayM.format(debut) + "-";
                        param.setPeriode(formatDayM.format(debut));
                        cal.add(Calendar.WEEK_OF_MONTH, nbPeriode > 1 ? (nbPeriode - 1) : nbPeriode);
                        debut = cal.getTime();
                        param.setFin(debut);
                        pe += formatDayM.format(debut) + ")";
                        param.setPeriode(pe);
                        break;
                    case "MONTH":
                        param.setDebut(debut);
                        param.setPeriode(formatMonth.format(debut));
                        cal.add(Calendar.MONTH, nbPeriode > 1 ? (nbPeriode - 1) : nbPeriode);
                        debut = cal.getTime();
                        param.setFin(debut);
                        break;
                    case "YEAR":
                        param.setDebut(debut);
                        param.setPeriode(formatYear.format(debut));
                        cal.add(Calendar.YEAR, nbPeriode);
                        debut = cal.getTime();
                        param.setFin(debut);
                        break;
                    default:
                        return null;
                }
                re.add(param);
            }
        }
        return re;
    }

    public static double arrondi(double d, int l) {
//        BigDecimal bd = new BigDecimal(d);
//        bd = bd.setScale(l, BigDecimal.ROUND_DOWN);
//        double r = bd.doubleValue();
//        return r;
        return Constantes.arrondi(d, l);
    }

    public double arrondi(double d) {
        return arrondi(d, currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0, dao);
    }

    public double arrondi(double d, long societe, DaoInterfaceLocal dao) {
        try {
            return dao.arrondi(societe, d);
        } catch (Exception ex) {
            return 0;
        }
    }

    public String giveStringStatut(char statut) {
        switch (statut) {
            case Constantes.STATUT_DOC_ATTENTE:
                return Constantes.ETAT_ATTENTE;
            case Constantes.STATUT_DOC_ANNULE:
                return Constantes.ETAT_ANNULE;
            case Constantes.STATUT_DOC_EDITABLE:
                return Constantes.ETAT_EDITABLE;
            case Constantes.STATUT_DOC_ENCOUR:
                return Constantes.ETAT_ENCOURS;
            case Constantes.STATUT_DOC_PAYER:
                return Constantes.ETAT_REGLE;
            case Constantes.STATUT_DOC_SUSPENDU:
                return Constantes.ETAT_SUSPENDU;
            case Constantes.STATUT_DOC_TERMINE:
                return Constantes.ETAT_TERMINE;
            case Constantes.STATUT_DOC_VALIDE:
                return Constantes.ETAT_VALIDE;
            case Constantes.STATUT_DOC_RENVOYE:
                return Constantes.ETAT_RENVOYE;
            default:
                return Constantes.ETAT_EDITABLE;
        }
    }

    public char giveCharStatut(String statut) {
        switch (statut) {
            case Constantes.ETAT_ATTENTE:
                return Constantes.STATUT_DOC_ATTENTE;
            case Constantes.ETAT_ANNULE:
                return Constantes.STATUT_DOC_ANNULE;
            case Constantes.ETAT_RENVOYE:
                return Constantes.STATUT_DOC_RENVOYE;
            case Constantes.ETAT_EDITABLE:
                return Constantes.STATUT_DOC_EDITABLE;
            case Constantes.ETAT_ENCOURS:
                return Constantes.STATUT_DOC_ENCOUR;
            case Constantes.ETAT_REGLE:
                return Constantes.STATUT_DOC_PAYER;
            case Constantes.ETAT_SUSPENDU:
                return Constantes.STATUT_DOC_SUSPENDU;
            case Constantes.ETAT_TERMINE:
                return Constantes.STATUT_DOC_TERMINE;
            case Constantes.ETAT_VALIDE:
                return Constantes.STATUT_DOC_VALIDE;
            default:
                return Constantes.STATUT_DOC_EDITABLE;
        }
    }

    public String giveNameCategorie(String statut) {
        switch (statut) {
            case Constantes.CAT_MARCHANDISE:
                return Constantes.CAT_MARCHANDISE_NAME;
            case Constantes.CAT_PSF:
                return Constantes.CAT_PSF_NAME;
            case Constantes.CAT_PF:
                return Constantes.CAT_PF_NAME;
            case Constantes.CAT_MP:
                return Constantes.CAT_MP_NAME;
            case Constantes.CAT_FOURNITURE:
                return Constantes.CAT_FOURNITURE_NAME;
            case Constantes.CAT_SERVICE:
                return Constantes.CAT_SERVICE_NAME;
            case Constantes.PIECE_DE_RECHANGE:
                return Constantes.PIECE_DE_RECHANGE_NAME;
            case Constantes.CAT_EMBALLAGE:
                return Constantes.CAT_EMBALLAGE_NAME;
            default:
                return Constantes.CAT_MARCHANDISE_NAME;
        }
    }

    public String giveNameStatut(String statut) {
        switch (statut) {
            case Constantes.ETAT_ATTENTE:
                return "EN ATTENTE";
            case Constantes.ETAT_RENVOYE:
                return "RENVOYE";
            case Constantes.ETAT_ANNULE:
                return "ANNULE";
            case Constantes.ETAT_ENCOURS:
                return "EN COURS";
            case Constantes.ETAT_REGLE:
                return "REGLE";
            case Constantes.ETAT_SUSPENDU:
                return "SUSPENDU";
            case Constantes.ETAT_TERMINE:
                return "TERMINE";
            case Constantes.ETAT_VALIDE:
                return "VALIDE";
            case Constantes.ETAT_SOUMIS:
                return "TRANSMIS";
            case Constantes.ETAT_LIVRE:
                return "LIVRE";
            case Constantes.ETAT_CLOTURE:
                return "CLOTURE";
            default:
                return "EDITABLE";
        }
    }

    public String giveNameTime(String statut) {
        switch (statut) {
            case Constantes.UNITE_TIERCE_:
                return Constantes.UNITE_TIERCE;
            case Constantes.UNITE_SECONDE_:
                return Constantes.UNITE_SECONDE;
            case Constantes.UNITE_MINUTE_:
                return Constantes.UNITE_MINUTE;
            case Constantes.UNITE_HEURE_:
                return Constantes.UNITE_HEURE;
            case Constantes.UNITE_JOUR_:
                return Constantes.UNITE_JOUR;
            case Constantes.UNITE_SEMAINE_:
                return Constantes.UNITE_SEMAINE;
            case Constantes.UNITE_MOIS_:
                return Constantes.UNITE_MOIS;
            case Constantes.UNITE_TRIMESTRE_:
                return Constantes.UNITE_TRIMESTRE;
            case Constantes.UNITE_SEMESTRE_:
                return Constantes.UNITE_SEMESTRE;
            case Constantes.UNITE_ANNEE_:
                return Constantes.UNITE_ANNEE;
            default:
                return Constantes.UNITE_JOUR;
        }
    }

    public String giveNameType(String type) {
        switch (type) {
            case Constantes.TYPE_FA:
                return Constantes.TYPE_FA_NAME;
            case Constantes.TYPE_FAA:
                return Constantes.TYPE_FAA_NAME;
            case Constantes.TYPE_BCA:
                return Constantes.TYPE_BCA_NAME;
            case Constantes.TYPE_BLA:
                return Constantes.TYPE_BLA_NAME;
            case Constantes.TYPE_FRA:
                return Constantes.TYPE_FRA_NAME;
            case Constantes.TYPE_FV:
                return Constantes.TYPE_FV_NAME;
            case Constantes.TYPE_BCV:
                return Constantes.TYPE_BCV_NAME;
            case Constantes.TYPE_BLV:
                return Constantes.TYPE_BLV_NAME;
            case Constantes.TYPE_FRV:
                return Constantes.TYPE_FRV_NAME;
            case Constantes.TYPE_FAV:
                return Constantes.TYPE_FAV_NAME;
            case Constantes.TYPE_ES:
                return Constantes.TYPE_ES_NAME;
            case Constantes.TYPE_SS:
                return Constantes.TYPE_SS_NAME;
            case Constantes.TYPE_FT:
                return Constantes.TYPE_FT_NAME;
            case Constantes.TYPE_IN:
                return Constantes.TYPE_IN_NAME;
            case Constantes.TYPE_OD:
                return Constantes.TYPE_OD_NAME;
            case Constantes.TYPE_PT:
                return Constantes.TYPE_PT_NAME;
            default:
                return Constantes.TYPE_FV_NAME;
        }
    }

    public String giveNameModule(String type) {
        switch (type) {
            case Constantes.MOD_GRH:
                return Constantes.MOD_GRH_NAME;
            case Constantes.MOD_MUT:
                return Constantes.MOD_MUT_NAME;
            case Constantes.MOD_COM:
                return Constantes.MOD_COM_NAME;
            case Constantes.MOD_MSG:
                return Constantes.MOD_MSG_NAME;
            case Constantes.MOD_REL:
                return Constantes.MOD_REL_NAME;
            case Constantes.MOD_COFI:
                return Constantes.MOD_COFI_NAME;
            case Constantes.MOD_PROD:
                return Constantes.MOD_PROD_NAME;
            default:
                return Constantes.MOD_GRH_NAME;
        }
    }

    public String giveNameCouts(String type) {
        switch (type) {
            case Constantes.COUT_OD:
                return Constantes.COUT_OD_NAME;
            case Constantes.COUT_VIREMENT:
                return Constantes.COUT_VIREMENT_NAME;
            case Constantes.COUT_MISSION:
                return Constantes.COUT_MISSION_NAME;
            case Constantes.COUT_FORMATION:
                return Constantes.COUT_FORMATION_NAME;
            case Constantes.COUT_VENTE:
                return Constantes.COUT_VENTE_NAME;
            case Constantes.COUT_ACHAT:
                return Constantes.COUT_ACHAT_NAME;
            case Constantes.COUT_TRANSFERT:
                return Constantes.COUT_TRANSFERT_NAME;
            case Constantes.COUT_CREDIT:
                return Constantes.COUT_CREDIT_NAME;
            default:
                return Constantes.COUT_MIXTE_NAME;
        }
    }

    public String giveNameUnite(String type) {
        switch (type) {
            case Constantes.UNITE_QUANTITE:
                return Constantes.UNITE_QUANTITE_NAME;
            case Constantes.UNITE_TEMPS:
                return Constantes.UNITE_TEMPS_NAME;
            case Constantes.UNITE_OEUVRE:
                return Constantes.UNITE_OEUVRE_NAME;
            default:
                return Constantes.UNITE_AUTRE;
        }
    }

    public String giveUniteConditionnement(String type) {
        switch (type) {
            case Constantes.UNITE_ACHAT:
                return Constantes.UNITE_ACHAT_NAME;
            case Constantes.UNITE_ENTREE:
                return Constantes.UNITE_ENTREE_NAME;
            case Constantes.UNITE_SORTIE:
                return Constantes.UNITE_SORTIE_NAME;
            case Constantes.UNITE_TRANSFERT:
                return Constantes.UNITE_TRANSFERT_NAME;
            case Constantes.UNITE_TRANSPORT:
                return Constantes.UNITE_TRANSPORT_NAME;
            case Constantes.UNITE_STOCKAGE:
                return Constantes.UNITE_STOCKAGE_NAME;
            default:
                return Constantes.UNITE_VENTE_NAME;
        }
    }

    /**
     * Notification
     *
     *
     * @return
     */
    public void actualisedAccueil() {
        giveExerciceActif();
        countUsersConnect();
        countWorkFlowToValid();
        countWarningToValid();
        countInfosToValid();
    }

    public long countUsersConnect() {
        Long re = (Long) dao.loadObjectByNameQueries("YvsConnection.findNBUser", new String[]{}, new Object[]{});
        NBRE_ONLINE = (re != null) ? (re > 0) ? re - 1 : re : 0;
        return NBRE_ONLINE;
    }

    public long countWorkFlowToValid() {
        if (currentUser != null) {
            if (currentNiveau != null) {
                String rq = "SELECT * FROM workflow(?,?,?,?)";
                WORKFLOWS = dao.loadDataByNativeQuery(rq, new Object[]{(currentAgence.getSociete() != null ? currentAgence.getSociete().getId().intValue() : 0), (currentAgence != null ? currentAgence.getId().intValue() : 0), (currentNiveau != null ? currentNiveau.getId().intValue() : 0), currentUser.getUsers().getId().intValue()});
            }
        }
        return WORKFLOWS != null ? WORKFLOWS.size() : 0;
    }

    public long countWarningToValid() {
        WARNINGS = new ArrayList<>();
        if (currentUser != null && currentAgence != null) {
            List<Object> params = new ArrayList<>();
            String query = "SELECT COALESCE(y.description, m.description), COUNT(y.id), m.titre_doc, y.nature_alerte, m.id, m.nature "
                    + "FROM yvs_workflow_alertes y INNER JOIN yvs_workflow_model_doc m ON y.model_doc = m.id "
                    + "INNER JOIN yvs_agences a ON y.agence = a.id "
                    + "INNER JOIN yvs_warning_model_doc w ON w.societe = a.societe AND w.model = m.id "
                    + "LEFT JOIN yvs_workflow_alertes_users s ON (s.alerte = y.id AND s.users = ?) "
                    + "WHERE (COALESCE((current_date - COALESCE(y.date_doc, current_date)), 0) > COALESCE(w.ecart, 0)) AND COALESCE(y.actif, TRUE) IS TRUE "
                    + "AND  m.nature != 'I' AND (s.id IS NULL OR (s.id IS NOT NULL AND (s.voir IS NULL OR s.voir IS TRUE))) "
                    + "AND m.id IN (SELECT DISTINCT u.document_type FROM yvs_user_view_alertes u WHERE u.users = ? AND u.actif = true AND (u.voir IS NULL OR u.voir IS TRUE)) ";
            params.add((currentUser.getUsers() != null ? currentUser.getUsers().getId() : 0));
            params.add((currentUser.getUsers() != null ? currentUser.getUsers().getId() : 0));
            if (!autoriser("param_warning_view_all")) {
                params.add(currentAgence.getId());
                query += "AND a.id = ? ";
            } else {
                params.add((currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0));
                query += "AND a.societe = ? ";
            }
            query += "GROUP BY m.id, y.nature_alerte, y.description ORDER BY m.description";
            WARNINGS = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
        }
        return WARNINGS != null ? WARNINGS.size() : 0;
    }

    public long countInfosToValid() {
        INFORMATIONS = new ArrayList<>();
        if (currentUser != null && currentAgence != null) {
            List<Object> params = new ArrayList<>();
            String query = "SELECT COALESCE(y.description, m.description), COUNT(y.id), m.titre_doc, y.nature_alerte, m.id, m.nature "
                    + "FROM yvs_workflow_alertes y INNER JOIN yvs_workflow_model_doc m ON y.model_doc = m.id "
                    + "INNER JOIN yvs_agences a ON y.agence = a.id "
                    + "LEFT JOIN yvs_workflow_alertes_users s ON (s.alerte = y.id AND s.users = ?) "
                    + "INNER JOIN yvs_warning_model_doc w ON w.societe = a.societe AND w.model = m.id "
                    + "WHERE (COALESCE((current_date - COALESCE(y.date_doc, current_date)), 0) <= COALESCE(w.ecart, 0)) AND COALESCE(y.actif, TRUE) IS TRUE "
                    + "AND  m.nature = 'I' AND (s.id IS NULL OR (s.id IS NOT NULL AND (s.voir IS NULL OR s.voir IS TRUE))) "
                    + "AND m.id IN (SELECT DISTINCT u.document_type FROM yvs_user_view_alertes u WHERE u.users = ? AND u.actif = true AND (u.voir IS NULL OR u.voir IS TRUE)) ";
            params.add((currentUser.getUsers() != null ? currentUser.getUsers().getId() : 0));
            params.add((currentUser.getUsers() != null ? currentUser.getUsers().getId() : 0));
            if (!autoriser("param_warning_view_all")) {
                params.add(currentAgence.getId());
                query += "AND a.id = ? ";
            } else {
                params.add((currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0));
                query += "AND a.societe = ? ";
            }
            query += "GROUP BY m.id, y.nature_alerte, y.description ORDER BY m.description";
            INFORMATIONS = dao.loadDataByNativeQuery(query, params.toArray(new Object[params.size()]));
        }
        return INFORMATIONS != null ? INFORMATIONS.size() : 0;
    }

    public long countWarningToValid_OLD() {
        if (currentUser != null && currentNiveau != null) {
            String query = "SELECT d.id FROM yvs_grh_employes e INNER JOIN yvs_grh_poste_de_travail p ON p.id=e.poste_actif "
                    + "INNER JOIN yvs_grh_departement d ON d.id=p.departement WHERE e.code_users=? LIMIT 1";
            Integer idService = (Integer) dao.loadObjectBySqlQuery(query, new yvs.dao.Options[]{new yvs.dao.Options(currentUser.getUsers().getId(), 1)});
            String rq = "SELECT *, ARRAY(SELECT 0), null::character varying, 0::bigint FROM warning(?,?,?,?,?,?,?,?)";
            Object[] params = new Object[]{(currentUser != null ? currentUser.getId() : 0), (currentDepot != null ? currentDepot.getId() : 0),
                (currentPoint != null ? currentPoint.getId() : 0), (0), (idService != null ? idService : 0), (currentAgence != null ? currentAgence.getId() : 0),
                (currentAgence.getSociete() != null ? currentAgence.getSociete().getId() : 0), (currentNiveau != null ? currentNiveau.getId() : 0)};
            WARNINGS = dao.loadDataByNativeQuery(rq, params);
        }
        return WARNINGS != null ? WARNINGS.size() : 0;
    }

    public long totalWorkFlowToValid() {
        long value = 0;
        if (WORKFLOWS != null ? WORKFLOWS.size() > 0 : false) {
            for (Object[] o : WORKFLOWS) {
                value += Long.valueOf(o[1].toString());
            }
        }
        return value;
    }

    public long totalWarningToValid() {
        long value = 0;
        if (WARNINGS != null ? WARNINGS.size() > 0 : false) {
            for (Object[] o : WARNINGS) {
                value += Long.valueOf(o[1].toString());
            }
        }
        return value;
    }

    public long totalInformationToValid() {
        long value = 0;
        if (INFORMATIONS != null ? INFORMATIONS.size() > 0 : false) {
            for (Object[] o : INFORMATIONS) {
                value += Long.valueOf(o[1].toString());
            }
        }
        return value;
    }

    public String getRandomString(int length) {
        String characters = "ABCDEFGHIJLMNOPQRSTUVWXYZ";
        String chiffres = "1234567890";
        int tailleLettre = 1;
        int tailleChiffre = 4;
        if (length > tailleChiffre) {
            tailleLettre = length - tailleChiffre;
        }
        String result = getRandomString(tailleLettre, characters) + getRandomString(tailleChiffre, chiffres);
        return result;
    }

    public String getRandomString(int length, String characters) {
        StringBuilder result = new StringBuilder();
        while (length > 0) {
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            length--;
        }
        return result.toString();
    }

    public Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }

    public Date timestampToDate(Date date) {
        try {
            Calendar c_date = dateToCalendar(date);
            c_date.set(Calendar.HOUR_OF_DAY, 0);
            c_date.set(Calendar.MINUTE, 0);
            c_date.set(Calendar.SECOND, 0);
            c_date.set(Calendar.MILLISECOND, 0);
            return c_date.getTime();
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public Date timestampToDate(Date date, Date time) {
        try {
            Calendar c_date = dateToCalendar(date);
            Calendar c_time = dateToCalendar(time);
            c_date.set(Calendar.HOUR_OF_DAY, c_time.get(Calendar.HOUR_OF_DAY));
            c_date.set(Calendar.MINUTE, c_time.get(Calendar.MINUTE));
            c_date.set(Calendar.SECOND, 0);
            c_date.set(Calendar.MILLISECOND, 0);
            return c_date.getTime();
        } catch (Exception ex) {
            Logger.getLogger(Managed.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public String giveNameSousModule(String module) {
        String re = "";
        switch (module) {
            case Constantes.DOCUMENT_STOCK_ARTICLE:
            case Constantes.DOCUMENT_ARTICLE_NON_MOUVEMENTE:
            case Constantes.DOCUMENT_ARTICLE:
            case Constantes.DOCUMENT_CLIENT:
            case Constantes.DOCUMENT_FOURNISSEUR:
            case Constantes.DOCUMENT_USERS:
            case Constantes.DOCUMENT_CAISSE:
            case Constantes.DOCUMENT_DEPOT:
                re = "modDonneBase";
                break;
            case Constantes.DOCUMENT_FORMATION:
            case Constantes.DOCUMENT_PERMISSION_COURTE_DUREE:
            case Constantes.DOCUMENT_MISSION:
            case Constantes.DOCUMENT_CONGES:
            case Constantes.DOCUMENT_EMPLOYE:
                re = "modRh";
                break;
            case Constantes.DOCUMENT_RETOUR_ACHAT:
            case Constantes.DOCUMENT_RETOUR_VENTE:
            case Constantes.DOCUMENT_FACTURE_VENTE:
            case Constantes.DOCUMENT_APPROVISIONNEMENT:
            case Constantes.DOCUMENT_FACTURE_ACHAT:
            case Constantes.DOCUMENT_TRANSFERT:
            case Constantes.DOCUMENT_SORTIE:
            case Constantes.DOCUMENT_ENTREE:
            case Constantes.DOCUMENT_BON_LIVRAISON_ACHAT:
            case Constantes.DOCUMENT_BON_LIVRAISON_VENTE:
            case Constantes.DOCUMENT_LOWER_MARGIN:
            case Constantes.DOCUMENT_FACTURE_ACHAT_LIVRE:
            case Constantes.DOCUMENT_FACTURE_ACHAT_REGLE:
            case Constantes.DOCUMENT_ACHAT_NON_COMPTABILISE:
            case Constantes.DOCUMENT_FACTURE_VENTE_LIVRE:
            case Constantes.DOCUMENT_FACTURE_VENTE_REGLE:
            case Constantes.DOCUMENT_VENTE_NON_COMPTABILISE:
            case Constantes.DOCUMENT_INVENTAIRE_STOCK:
            case Constantes.DOCUMENT_RECONDITIONNEMENT_STOCK:
            case Constantes.DOCUMENT_HIGH_PR_ARTICLE:
            case Constantes.DOCUMENT_POINTVENTE:
                re = "modGescom";
                break;
            case Constantes.DOCUMENT_AVOIR_VENTE:
            case Constantes.DOCUMENT_AVOIR_ACHAT:
            case Constantes.DOCUMENT_DOC_DIVERS_CAISSE:
            case Constantes.DOCUMENT_BON_DIVERS_CAISSE:
            case Constantes.DOCUMENT_PIECE_CAISSE:
            case Constantes.DOCUMENT_PHASE_CAISSE_SALAIRE:
            case Constantes.DOCUMENT_PHASE_CAISSE_DIVERS:
            case Constantes.DOCUMENT_PHASE_CAISSE_VENTE:
            case Constantes.DOCUMENT_PHASE_CAISSE_ACHAT:
            case Constantes.DOCUMENT_PHASE_ACOMPTE_VENTE:
            case Constantes.DOCUMENT_PHASE_ACOMPTE_ACHAT:
            case Constantes.DOCUMENT_PHASE_CREDIT_VENTE:
            case Constantes.DOCUMENT_PHASE_CREDIT_ACHAT:
            case Constantes.DOCUMENT_DOC_DIVERS_DEPENSE:
            case Constantes.DOCUMENT_DOC_DIVERS_RECETTE:
            case Constantes.DOCUMENT_OD_NON_PLANNIFIE:
            case Constantes.DOCUMENT_OD_NON_COMPTABILISE:
                re = "modCompta";
                break;
            case Constantes.DOCUMENT_CP_UPPER_PR:
            case Constantes.DOCUMENT_ORDRE_FABRICATION_TERMINE:
            case Constantes.DOCUMENT_ORDRE_FABRICATION_DECLARE:
                re = "modProduction";
                break;
        }
        return re;
    }

    public String giveNameSousPage(String module) {
        String re = "";
        switch (module) {
            case Constantes.DOCUMENT_ARTICLE:
                re = "Articles";
                break;
            case Constantes.DOCUMENT_CLIENT:
                re = "Clients";
                break;
            case Constantes.DOCUMENT_FOURNISSEUR:
                re = "Fournisseurs";
                break;
            case Constantes.DOCUMENT_USERS:
                re = "Gestion des utilisateurs";
                break;
            case Constantes.DOCUMENT_EMPLOYE:
                re = "Employés";
                break;
            case Constantes.DOCUMENT_CAISSE:
                re = "Caisses";
                break;
            case Constantes.DOCUMENT_DEPOT:
                re = "Dépôts";
                break;
            case Constantes.DOCUMENT_POINTVENTE:
                re = "Points Ventes";
                break;
            case Constantes.DOCUMENT_MISSION:
                re = "Missions";
                break;
            case Constantes.DOCUMENT_PERMISSION_COURTE_DUREE:
            case Constantes.DOCUMENT_CONGES:
                re = "Congés";
                break;
            case Constantes.DOCUMENT_AVOIR_ACHAT:
                re = "Avoir Achat";
                break;
            case Constantes.DOCUMENT_RETOUR_ACHAT:
                re = "Retour Achat";
                break;
            case Constantes.DOCUMENT_AVOIR_VENTE:
                re = "Avoir Vente";
                break;
            case Constantes.DOCUMENT_RETOUR_VENTE:
                re = "Retour Vente";
                break;
            case Constantes.DOCUMENT_COMMANDE_ACHAT:
                re = "Commande Achat";
                break;
            case Constantes.DOCUMENT_LOWER_MARGIN:
            case Constantes.DOCUMENT_FACTURE_VENTE_REGLE:
            case Constantes.DOCUMENT_FACTURE_VENTE_LIVRE:
            case Constantes.DOCUMENT_FACTURE_VENTE:
            case Constantes.DOCUMENT_VENTE_NON_COMPTABILISE:
                re = "Factures Vente";
                break;
            case Constantes.DOCUMENT_FACTURE_ACHAT_REGLE:
            case Constantes.DOCUMENT_FACTURE_ACHAT_LIVRE:
            case Constantes.DOCUMENT_FACTURE_ACHAT:
            case Constantes.DOCUMENT_ACHAT_NON_COMPTABILISE:
                re = "Factures Achat";
                break;
            case Constantes.DOCUMENT_FORMATION:
                re = "Formations";
                break;
            case Constantes.DOCUMENT_TRANSFERT:
                re = "Transferts Stock";
                break;
            case Constantes.DOCUMENT_SORTIE:
                re = "Sorties Stock";
                break;
            case Constantes.DOCUMENT_PHASE_CAISSE_SALAIRE:
            case Constantes.DOCUMENT_PHASE_CAISSE_DIVERS:
            case Constantes.DOCUMENT_PHASE_CAISSE_VENTE:
            case Constantes.DOCUMENT_PHASE_CAISSE_ACHAT:
            case Constantes.DOCUMENT_PHASE_ACOMPTE_VENTE:
            case Constantes.DOCUMENT_PHASE_ACOMPTE_ACHAT:
            case Constantes.DOCUMENT_PHASE_CREDIT_VENTE:
            case Constantes.DOCUMENT_PHASE_CREDIT_ACHAT:
                re = "Suivi des chèques";
                break;
            case Constantes.DOCUMENT_DOC_DIVERS_DEPENSE:
            case Constantes.DOCUMENT_DOC_DIVERS_RECETTE:
            case Constantes.DOCUMENT_DOC_DIVERS_CAISSE:
            case Constantes.DOCUMENT_OD_NON_PLANNIFIE:
            case Constantes.DOCUMENT_OD_NON_COMPTABILISE:
                re = "Opérations Divers";
                break;
            case Constantes.DOCUMENT_BON_DIVERS_CAISSE:
                re = "Bon Provisoire";
                break;
            case Constantes.DOCUMENT_APPROVISIONNEMENT:
                re = "Approvissionnements";
                break;
            case Constantes.DOCUMENT_PIECE_CAISSE:
                re = "Pièces de caisse";
                break;
            case Constantes.DOCUMENT_HIGH_PR_ARTICLE:
                re = "Mouvements de Stock";
                break;
            case Constantes.DOCUMENT_STOCK_ARTICLE:
            case Constantes.DOCUMENT_ARTICLE_NON_MOUVEMENTE:
                re = "Articles";
                break;
            case Constantes.DOCUMENT_BON_LIVRAISON_ACHAT:
                re = "Receptions Achat";
                break;
            case Constantes.DOCUMENT_BON_LIVRAISON_VENTE:
                re = "Livraisons Vente";
                break;
            case Constantes.DOCUMENT_ENTREE:
                re = "Entrées Stock";
                break;
            case Constantes.DOCUMENT_INVENTAIRE_STOCK:
                re = "Inventaires";
                break;
            case Constantes.DOCUMENT_RECONDITIONNEMENT_STOCK:
                re = "Reconditionnement";
                break;
            case Constantes.DOCUMENT_CP_UPPER_PR:
            case Constantes.DOCUMENT_ORDRE_FABRICATION_TERMINE:
            case Constantes.DOCUMENT_ORDRE_FABRICATION_DECLARE:
                re = "Ordres de fabrication";
                break;
        }
        return re;
    }

    public String giveNameSousMenu(String module) {
        String re = "";
        switch (module) {
            case Constantes.DOCUMENT_ARTICLE:
                re = "smenProduit";
                break;
            case Constantes.DOCUMENT_CLIENT:
                re = "smenClientCom";
                break;
            case Constantes.DOCUMENT_FOURNISSEUR:
                re = "smenFournisseurCom";
                break;
            case Constantes.DOCUMENT_USERS:
                re = "smenUser";
                break;
            case Constantes.DOCUMENT_EMPLOYE:
                re = "smenEmploye";
                break;
            case Constantes.DOCUMENT_CAISSE:
                re = "smenCaisses";
                break;
            case Constantes.DOCUMENT_DEPOT:
                re = "smenDepotCom";
                break;
            case Constantes.DOCUMENT_POINTVENTE:
                re = "smenPointVente";
                break;
            case Constantes.DOCUMENT_MISSION:
                re = "smenMission";
                break;
            case Constantes.DOCUMENT_PERMISSION_COURTE_DUREE:
            case Constantes.DOCUMENT_CONGES:
                re = "smenConge";
                break;
            case Constantes.DOCUMENT_AVOIR_ACHAT:
                re = "smenFactureAvoirAchat";
                break;
            case Constantes.DOCUMENT_RETOUR_ACHAT:
                re = "smenFactureRetourAchat";
                break;
            case Constantes.DOCUMENT_AVOIR_VENTE:
                re = "smenFactureAvoirVente";
                break;
            case Constantes.DOCUMENT_RETOUR_VENTE:
                re = "smenFactureRetourVente";
                break;
            case Constantes.DOCUMENT_LOWER_MARGIN:
            case Constantes.DOCUMENT_FACTURE_VENTE_REGLE:
            case Constantes.DOCUMENT_FACTURE_VENTE_LIVRE:
            case Constantes.DOCUMENT_FACTURE_VENTE:
            case Constantes.DOCUMENT_VENTE_NON_COMPTABILISE:
                re = "smenFactureVente";
                break;
            case Constantes.DOCUMENT_FACTURE_ACHAT_REGLE:
            case Constantes.DOCUMENT_FACTURE_ACHAT_LIVRE:
            case Constantes.DOCUMENT_FACTURE_ACHAT:
            case Constantes.DOCUMENT_ACHAT_NON_COMPTABILISE:
                re = "smenFactureAchat";
                break;
            case Constantes.DOCUMENT_FORMATION:
                re = "smenFormation";
                break;
            case Constantes.DOCUMENT_TRANSFERT:
                re = "smenTransfert";
                break;
            case Constantes.DOCUMENT_SORTIE:
                re = "smenSortie";
                break;
            case Constantes.DOCUMENT_PHASE_CAISSE_SALAIRE:
            case Constantes.DOCUMENT_PHASE_CAISSE_DIVERS:
            case Constantes.DOCUMENT_PHASE_CAISSE_VENTE:
            case Constantes.DOCUMENT_PHASE_CAISSE_ACHAT:
            case Constantes.DOCUMENT_PHASE_ACOMPTE_VENTE:
            case Constantes.DOCUMENT_PHASE_ACOMPTE_ACHAT:
            case Constantes.DOCUMENT_PHASE_CREDIT_VENTE:
            case Constantes.DOCUMENT_PHASE_CREDIT_ACHAT:
                re = "smenSuiviRegVente";
                break;
            case Constantes.DOCUMENT_DOC_DIVERS_RECETTE:
            case Constantes.DOCUMENT_DOC_DIVERS_DEPENSE:
            case Constantes.DOCUMENT_DOC_DIVERS_CAISSE:
            case Constantes.DOCUMENT_OD_NON_PLANNIFIE:
            case Constantes.DOCUMENT_OD_NON_COMPTABILISE:
                re = "smenOperationDivers";
                break;
            case Constantes.DOCUMENT_BON_DIVERS_CAISSE:
                re = "smenBonProvisoire";
                break;
            case Constantes.DOCUMENT_APPROVISIONNEMENT:
                re = "smenFicheAppro";
                break;
            case Constantes.DOCUMENT_PIECE_CAISSE:
                re = "smenPieceCaiss";
                break;
            case Constantes.DOCUMENT_HIGH_PR_ARTICLE:
                re = "smenValorisationStock";
                break;
            case Constantes.DOCUMENT_STOCK_ARTICLE:
            case Constantes.DOCUMENT_ARTICLE_NON_MOUVEMENTE:
                re = "smenProduit";
                break;
            case Constantes.DOCUMENT_BON_LIVRAISON_ACHAT:
                re = "smenBonLivraisonAchat";
                break;
            case Constantes.DOCUMENT_BON_LIVRAISON_VENTE:
                re = "smenBonLivraisonVente";
                break;
            case Constantes.DOCUMENT_ENTREE:
                re = "smenEntree";
                break;
            case Constantes.DOCUMENT_INVENTAIRE_STOCK:
                re = "smenInventaire";
                break;
            case Constantes.DOCUMENT_RECONDITIONNEMENT_STOCK:
                re = "smenReconditionnement";
                break;
            case Constantes.DOCUMENT_CP_UPPER_PR:
            case Constantes.DOCUMENT_ORDRE_FABRICATION_TERMINE:
            case Constantes.DOCUMENT_ORDRE_FABRICATION_DECLARE:
                re = "smenOfSuivi2";
                break;
        }
        return re;
    }

    public String giveNameExport(String module) {
        String re = "";
        switch (module) {
            case Constantes.EXPORT_CONTENU_JOURNAL:
                re = Constantes.EXPORT_CONTENU_JOURNAL_NAME;
                break;
            case Constantes.EXPORT_ARTICLE:
                re = Constantes.EXPORT_ARTICLE_NAME;
                break;
            case Constantes.EXPORT_CLIENT:
                re = Constantes.EXPORT_CLIENT_NAME;
                break;
            case Constantes.EXPORT_SALAIRE:
                re = Constantes.EXPORT_SALAIRE_NAME;
                break;
            case Constantes.EXPORT_PLAN_COMPTABLE:
                re = Constantes.EXPORT_PLAN_COMPTABLE_NAME;
                break;
        }
        return re;
    }

    public String SUBREPORT_DIR() {
        return SUBREPORT_DIR(currentAgence.getSociete() != null ? currentAgence.getSociete().getPrintWithEntete() : true);
    }

    public String SUBREPORT_DIR(boolean withHeader) {
        String result = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR;
        if (!withHeader) {
            result += "empty" + FILE_SEPARATOR;
        }
        return result;
    }

    public String returnLogo() {
        String logo = (currentAgence.getSociete().getLogo() != null) ? currentAgence.getSociete().getLogo() : "";
        return returnLogo(logo);
    }

    public static String returnLogo(String logo) {
        try {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminLogos().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminLogos().length());
            String file = repDest + "" + Initialisation.FILE_SEPARATOR + "default.png";
            if (logo != null ? logo.trim().length() > 0 : false) {
                file = repDest + "" + Initialisation.FILE_SEPARATOR + "" + logo;
                if (!new File(file).exists()) {
                    file = repDest + "" + Initialisation.FILE_SEPARATOR + "default.png";
                }
            }
            return FacesContext.getCurrentInstance().getExternalContext().getRealPath(file);
        } catch (Exception ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String photoUsers(YvsUsers u) {
        if (u != null) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/docUsers/") + FILE_SEPARATOR + u.getPhoto();
            if (new File(path).exists()) {
                return u.getPhoto();
            } else {
                if (!"M".equals(u.getCivilite())) {
                    return "user2.jpg";
                }
            }
        }
        return "user1.png";
    }

    public static String photoUsersEmployes(YvsUsers u) {
        if (u != null) {
            return photoEmployes(u.getEmploye());
        }
        return "user1.png";
    }

    public static String photoEmployes(YvsGrhEmployes u) {
        if (u != null) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/docEmps/perso/photo/") + FILE_SEPARATOR + u.getPhotos();
            if (new File(path).exists()) {
                return u.getPhotos();
            } else {
                if (!"M.".equals(u.getCivilite()) && !"M".equals(u.getCivilite())) {
                    return "user2.jpg";
                }
            }
        }
        return "user1.png";
    }

    public static String photoEmploye(Employe u) {
        if (u != null) {
            if (u.getPhotos() != null) {
                ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
                String path = ext.getRealPath("resources/lymytz/documents/docEmps/perso/photo/") + FILE_SEPARATOR + u.getPhotos();
                if (new File(path).exists()) {
                    return u.getPhotos();
                } else {
                    if (!"M.".equals(u.getCivilite())) {
                        return "user2.jpg";
                    } else {
                        return "user1.png";
                    }
                }
            }
        }
        return "user1.png";
    }

    public static String[] photoArticles(YvsBaseArticles u) {
        String[] result = new String[]{Constantes.DEFAULT_PHOTO(), "png"};
        if (u != null) {
            if (u.getPhoto1() != null ? u.getPhoto1().trim().length() > 0 : false) {
                result[0] = u.getPhoto1();
                result[1] = u.getPhoto1Extension();
            } else if (u.getPhoto2() != null ? u.getPhoto2().trim().length() > 0 : false) {
                result[0] = u.getPhoto2();
                result[1] = u.getPhoto2Extension();
            } else if (u.getPhoto3() != null ? u.getPhoto3().trim().length() > 0 : false) {
                result[0] = u.getPhoto3();
                result[1] = u.getPhoto3Extension();
            }
        }
        return result;
    }

    public static String[] photoArticle(Articles u) {
        String[] result = new String[]{Constantes.DEFAULT_PHOTO(), "png"};
        if (u != null) {
            if (u.getPhoto1() != null ? u.getPhoto1().trim().length() > 0 : false) {
                result[0] = u.getPhoto1();
                result[1] = u.getPhoto1Extension();
            } else if (u.getPhoto2() != null ? u.getPhoto2().trim().length() > 0 : false) {
                result[0] = u.getPhoto2();
                result[1] = u.getPhoto2Extension();
            } else if (u.getPhoto3() != null ? u.getPhoto3().trim().length() > 0 : false) {
                result[0] = u.getPhoto3();
                result[1] = u.getPhoto3Extension();
            }
        }
        return result;
    }

    public static String photoUnites(YvsBaseConditionnement u) {
        if (u != null) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/docArticle/") + FILE_SEPARATOR + u.getPhoto();
            if (new File(path).exists()) {
                return u.getPhoto();
            }
        }
        return "1.png";
    }

    public static String photoUnite(Conditionnement u) {
        if (u != null) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/docArticle/") + FILE_SEPARATOR + u.getPhoto();
            if (new File(path).exists()) {
                return u.getPhoto();
            }
        }
        return "1.png";
    }

    public static String photoPack(ArticlePack u) {
        if (u != null) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/docArticle/") + FILE_SEPARATOR + u.getPhoto();
            if (new File(path).exists()) {
                return u.getPhoto();
            }
        }
        return "1.png";
    }

    public double convertirUnite(UniteMesure entree, UniteMesure sortie, double qte) {
        if ((sortie != null ? sortie.getId() > 0 : false) && (entree != null ? entree.getId() > 0 : false)) {
            return convertirUnites(new YvsBaseUniteMesure(entree.getId()), new YvsBaseUniteMesure(sortie.getId()), qte);
        }
        return 0;
    }

    public double convertirUnites(YvsBaseUniteMesure entree, YvsBaseUniteMesure sortie, double qte) {
        if ((sortie != null ? sortie.getId() > 0 : false) && (entree != null ? entree.getId() > 0 : false)) {
            if (entree.equals(sortie)) {
                return 1;
            }
            YvsBaseTableConversion tc = (YvsBaseTableConversion) dao.loadOneByNameQueries("YvsBaseTableConversion.findUniteCorrespondance", new String[]{"unite", "uniteE"}, new Object[]{entree, sortie});
            return convertirUnites(entree, sortie, qte, (tc != null ? tc.getTauxChange() : 0));
        }
        return 0;
    }

    public double convertirUnites(YvsBaseUniteMesure entree, YvsBaseUniteMesure sortie, double qte, double coeficient) {
        if ((sortie != null ? sortie.getId() > 0 : false) && (entree != null ? entree.getId() > 0 : false)) {
            if (entree.equals(sortie)) {
                return 1;
            } else {
                return (qte * coeficient);
            }
        }
        return 0;
    }

    public void reloadUser() {
        if (currentUser != null) {
            currentUser = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{currentUser.getId()});
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", currentUser);
        }
    }

    public double valueContentDuree(List<ContentDuree> list, long objet, String elt) {
        double valeur = 0;
        if (objet > 0) {
            int idx = list.indexOf(new ContentDuree(objet, elt));
            if (idx > -1) {
                valeur = list.get(idx).getValeur();
            }
        }
        return valeur;
    }

    public void load(long users) {
//        System.err.println("dao " + dao);
        currentUser = (YvsUsersAgence) dao.loadOneByNameQueries("YvsUsersAgence.findById", new String[]{"id"}, new Object[]{users});
        currentAgence = currentUser.getAgence();
//        currentAgence.getSociete() = currentAgence.getSociete();
        dao.loadInfos(currentAgence.getSociete(), currentAgence, currentUser, currentDepot, currentPoint, currentExo);
    }

    public boolean controleStock(YvsBaseArticles art, YvsBaseDepots dep) {
        Boolean b = (Boolean) dao.loadObjectByNameQueries("YvsBaseArticleDepot.findIfSuivi", new String[]{"article", "depot"}, new Object[]{art, dep});
        return (b != null) ? b : true;
    }

    public boolean controleAccesCaisse(YvsBaseCaisse caisse, boolean msg) {
        if (caisse != null ? caisse.getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez précisez la caisse");
            }
            return false;
        }
        if (currentUser.getUsers() != null ? currentUser.getUsers().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous ne pouvez pas effectuer cette opération.. car vous n'etes pas utilisateur");
            }
            return false;
        }
        String error = null;
        if (caisse.getCaissier() != null ? caisse.getCaissier().getId() > 0 : false) {
            if (!caisse.getCaissier().equals(currentUser.getUsers())) {
                error = "car vous n'etes pas le caissier de cette caisse";
            } else {
                return true;
            }
        }
        if (caisse.getCodeAcces() != null ? caisse.getCodeAcces().getId() < 1 : true) {
            if (!autoriser("caiss_create_piece")) {
                if (msg) {
                    error = error != null ? error : "car vous n'avez pas le droit";
                    getErrorMessage("Vous ne pouvez pas effectuer cette opération.. " + error);
                }
                return false;
            }
        } else {
            YvsBaseUsersAcces acces = (YvsBaseUsersAcces) dao.loadOneByNameQueries("YvsBaseUsersAcces.findOne", new String[]{"users", "code"}, new Object[]{currentUser.getUsers(), caisse.getCodeAcces()});
            if (acces != null ? acces.getId() < 1 : true) {
                if (msg) {
                    getErrorMessage("Vous ne pouvez pas effectuer cette opération.. car vous n'avez pas le droit d'action sur cette caisse");
                }
                return false;
            }
        }
        return true;
    }

    public YvsBaseCaisse findByResponsable(YvsUsers responsable) {
        YvsBaseCaisse caisse = null;
        if (responsable != null) {
            caisse = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findByCaissier", new String[]{"caissier", "societe", "responsable"}, new Object[]{responsable, currentAgence.getSociete(), responsable.getEmploye()});
        }
        return caisse;
    }

    public YvsBaseModeReglement modeEspece() {
        if (dao != null ? modeByEspece == null : false) {
            nameQueri = "YvsBaseModeReglement.findByDefault";
            champ = new String[]{"societe", "actif", "type", "defaut"};
            val = new Object[]{currentAgence.getSociete(), true, Constantes.MODE_PAIEMENT_ESPECE, true};
            modeByEspece = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (modeByEspece != null ? (modeByEspece.getId() != null ? modeByEspece.getId() < 1 : true) : true) {
                nameQueri = "YvsBaseModeReglement.findByTypeActif";
                champ = new String[]{"societe", "actif", "type"};
                val = new Object[]{currentAgence.getSociete(), true, Constantes.MODE_PAIEMENT_ESPECE};
                modeByEspece = (YvsBaseModeReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
            }
        }
        return modeByEspece;
    }

    public YvsBaseCodeAcces returnCodeAcces(String code) {
        return returnCodeAcces(code, true);
    }

    public YvsBaseCodeAcces returnCodeAcces(String code, boolean save) {
        YvsBaseCodeAcces y = (YvsBaseCodeAcces) dao.loadOneByNameQueries("YvsBaseCodeAcces.findByCode", new String[]{"societe", "code"}, new Object[]{currentAgence.getSociete(), code});
        if (save) {
            if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
                y = new YvsBaseCodeAcces();
                y.setCode(code);
                y.setAuthor(currentUser);
                y.setDateSave(new Date());
                y.setDateUpdate(new Date());
                y.setSociete(currentAgence.getSociete());

                y = (YvsBaseCodeAcces) dao.save1(y);
            }
        }
        return y;
    }

    public boolean acces(YvsBaseCodeAcces code) {
        if (code != null ? (code.getId() != null ? code.getId() < 1 : true) : true) {
            return true;
        }
        return acces(code.getCode());
    }

    public boolean acces(String code) {
        if (dao != null) {
            if (code != null ? code.trim().length() < 1 : true) {
                return true;
            }
            nameQueri = "YvsBaseUsersAcces.findIdAcces";
            champ = new String[]{"societe", "users", "code"};
            val = new Object[]{currentAgence.getSociete(), currentUser.getUsers(), code};
            Long y = (Long) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (y != null ? y > 0 : false) {
                return true;
            }
        }
        return false;
    }

    public String periodeAbbreviation(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date != null ? date : new Date());
        int m = c.get(Calendar.MONTH);
        int y = c.get(Calendar.YEAR);
        String ref = Constantes.tabMois[m];
        ref = ref.length() < 5 ? ref : ref.substring(0, 4) + ".";
        return (ref + " " + y);
    }

    public double abs(double valeur) {
        return Math.abs(valeur);
    }

    public String writeEtape(String etape, String regex) {
        if (etape != null) {
            if (regex != null ? regex.trim().length() > 0 : false) {
                if (Util.isMajuscule(regex)) {
                    return etape.toUpperCase();
                } else if (Util.isCapitalise(regex)) {
                    return Util.capitalise(etape);
                } else {
                    return etape.toLowerCase();
                }
            }
            return etape;
        }
        return "";
    }

    public String DN(Double valeur) {
        String value = valeur != null ? String.valueOf(valeur) : "";
        return DN(value);
    }

    public String DNS(Double valeur) {
        String value = valeur != null ? String.valueOf(valeur) : "";
        return DNS(value);
    }

    public String DNA(Double valeur) {
        String value = valeur != null ? String.valueOf(valeur) : "";
        return DNA(value);
    }

    public String DN(String value) {
        double d = ("".equals(value) ? 0 : Double.parseDouble(value));
        DecimalFormat df = new DecimalFormat("#,##0");
        String num = df.format(d) != null ? df.format(d) : "";
        return num;
    }

    public String DNS(String value) {
        double d = ("".equals(value) ? 0 : Double.parseDouble(value));
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String num = df.format(d) != null ? df.format(d) : "";
        return num;
    }

    public String DNA(String value) {
        double d = ("".equals(value) ? 0 : Double.parseDouble(value));
        DecimalFormat df = new DecimalFormat("#,##0.000000000000000");
        String num = df.format(d) != null ? df.format(d) : "";
        while (num.charAt(num.length() - 1) == '0') {
            num = num.substring(0, num.length() - 1);
        }
        if (num.charAt(num.length() - 1) == '.'
                || num.charAt(num.length() - 1) == ',') {
            num = num.substring(0, num.length() - 1);
        }
        return num;
    }

    public Converter getConverterCoutStock() {
        return getConverter(Constantes.CONVERTER_COUT_STOCK);
    }

    public Converter getConverter() {
        return getConverter(Constantes.CONVERTER_MONNAIE);
    }

    public Converter getConverterStock() {
        return getConverter(Constantes.CONVERTER_STOCK);
    }

    public Converter getConverted(int converter) {
        return new ConverterNumber(converter);
    }

    public Converter getConverter(String type) {
        int converter = 0;
        if (type != null ? type.trim().length() > 0 : false) {
            switch (type) {
                case Constantes.CONVERTER_MONNAIE: {
                    if (paramCompta != null ? (paramCompta.getId() != null ? paramCompta.getId() > 0 : false) : false) {
                        converter = paramCompta.getConverter();
                    }
                    break;
                }
                case Constantes.CONVERTER_STOCK: {
                    if (paramCommercial != null ? (paramCommercial.getId() != null ? paramCommercial.getId() > 0 : false) : false) {
                        converter = paramCommercial.getConverter();
                    }
                    break;
                }
                case Constantes.CONVERTER_PRODUCTION: {
                    if (paramProduction != null ? (paramProduction.getId() != null ? paramProduction.getId() > 0 : false) : false) {
                        converter = paramProduction.getConverter();
                    }
                    break;
                }
                case Constantes.CONVERTER_PRODUCTIONPF: {
                    if (paramProduction != null ? (paramProduction.getId() != null ? paramProduction.getId() > 0 : false) : false) {
                        converter = paramProduction.getConverterPF();
                    }
                    break;
                }
                case Constantes.CONVERTER_COUT_STOCK: {
                    if (paramCommercial != null ? (paramCommercial.getId() != null ? paramCommercial.getId() > 0 : false) : false) {
                        converter = paramCommercial.getConverter();
                    }
                    break;
                }
                default:
                    break;
            }
        }
        return new ConverterNumber(converter);
    }

    public Converter getCproduction() {
        return getConverter(Constantes.CONVERTER_PRODUCTION);
    }

    public Converter converterProd(String categorie) {
        if (categorie != null ? categorie.equals(Constantes.CAT_PF) : false) {
            return getCproductionPF();
        } else {
            return getCproduction();
        }
    }

    public Converter getCproductionPF() {
        return getConverter(Constantes.CONVERTER_PRODUCTIONPF);
    }

    public long employesEnconge() {
        if (currentUser != null) {
            nbCongeEnCours = (long) dao.loadObjectByNameQueries("YvsGrhCongeEmps.FindEnCongeC", new String[]{"date", "statut", "societe"}, new Object[]{Utilitaire.giveOnlyDate(new Date()), 'V', currentUser.getAgence().getSociete()});
            return nbCongeEnCours;
        } else {
            return 0;
        }
    }

    public long employesMissions() {
        if (currentUser != null) {
            return nbMissionEnCours = (long) dao.loadObjectByNameQueries("YvsMissions.findEnMissionC", new String[]{"date", "statut", "societe"}, new Object[]{Utilitaire.giveOnlyDate(new Date()), 'V', currentUser.getAgence().getSociete()});
        }
        return 0;
    }

    /**
     * Opérations mutuelle
     *
     *
     * @param idMutualiste
     * @return
     */
    public double findMontantTotalEpargne(long idMutualiste) {
        return findSoldeMutualisteByNature(idMutualiste, Constantes.MUT_TYPE_COMPTE_EPARGNE);
    }

//    public double findMontantTotalEpargnePeriode(long idPeriode) {
//        Double soeE = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSoldeEpargneMutualisteAll", new String[]{"mutualiste", "nature"}, new Object[]{new YvsMutMutualiste(idMutualiste), Constantes.MUT_TYPE_COMPTE_EPARGNE});
//        return (soeE != null) ? soeE : 0;
//    }
    public double findSoldeCompte(long idCompte) {
        return dao.getSoldeCompteMutualiste(idCompte);
    }

    public double findSoldeMutualisteByNature(long mutualiste, String nature) {
        champ = new String[]{"mutualiste", "nature", "mouvement"};
        val = new Object[]{new YvsMutMutualiste(mutualiste), nature, Constantes.MUT_SENS_OP_DEPOT};
        Double d = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSoldeTypeCompteMutualiste", champ, val);
        val = new Object[]{new YvsMutMutualiste(mutualiste), nature, Constantes.MUT_SENS_OP_RETRAIT};
        Double r = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSoldeTypeCompteMutualiste", champ, val);
        return ((d != null) ? d : 0) - ((r != null) ? r : 0);
    }

    public double findSoldeMutualisteByTypeCompteAtDate(long mutualiste, String nature, Date date) {
        champ = new String[]{"mutualiste", "nature", "date", "mouvement"};
        val = new Object[]{new YvsMutMutualiste(mutualiste), nature, date, Constantes.MUT_SENS_OP_DEPOT};
        Double d = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSumCompteDate", champ, val);
        val = new Object[]{new YvsMutMutualiste(mutualiste), nature, date, Constantes.MUT_SENS_OP_RETRAIT};
        Double r = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSumCompteDate", champ, val);
        double solde = (d != null ? d : 0) - (r != null ? r : 0);
        return solde;
    }

    public double findSoldeByOperationMutualistePeriode(long mutualiste, String nature, Date debut, Date fin) {
        champ = new String[]{"mutualiste", "nature", "mouvement", "debut", "fin"};
        val = new Object[]{new YvsMutMutualiste(mutualiste), nature, Constantes.MUT_SENS_OP_DEPOT, debut, fin};
        Double d = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSoldeTypeOperationByNaturePeriode", champ, val);
        val = new Object[]{new YvsMutMutualiste(mutualiste), nature, Constantes.MUT_SENS_OP_RETRAIT, debut, fin};
        Double r = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSoldeTypeOperationByNaturePeriode", champ, val);
        double solde = (d != null ? d : 0) - (r != null ? r : 0);
        return solde;
    }

    public double findSoldeByOperationMutuellePeriode(long idMutuelle, String nature, Date debut, Date fin, String mouvement) {
        if (mouvement == null) {
            champ = new String[]{"mutuelle", "nature", "mouvement", "date"};
            val = new Object[]{new YvsMutMutuelle(idMutuelle), nature, Constantes.MUT_SENS_OP_DEPOT, fin};
            Double d = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findCumulTypeOperationByNaturePeriodeAll", champ, val);
            val = new Object[]{new YvsMutMutuelle(idMutuelle), nature, Constantes.MUT_SENS_OP_RETRAIT, fin};
            Double r = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findCumulTypeOperationByNaturePeriodeAll", champ, val);
            double solde = (d != null ? d : 0) - (r != null ? r : 0);
            return solde;
        } else {
            //Donne le total des mouvement d'entrée ou sortie dans la mutuelle
            champ = new String[]{"mutuelle", "nature", "mouvement", "date"};
            val = new Object[]{new YvsMutMutuelle(idMutuelle), nature, mouvement, fin};
            Double d = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findCumulTypeOperationByNaturePeriodeAll", champ, val);
            return d != null ? d : 0;
        }
    }

    public double findSoldeMutuellePeriodeADistribuer(long idPeriode) {
        String rq = "SELECT mut_calcul_solde_caisse_a_distribuer(?)";
        Double d = (Double) dao.loadObjectBySqlQuery(rq, new yvs.dao.Options[]{new yvs.dao.Options(idPeriode, 1)});
        return d != null ? d : 0;
    }

    public double findTotalOperationPeriode(String type, String mvt, Date debut, Date fin) {
        Double re;
        champ = new String[]{"mutuelle", "nature", "mouvement", "debut", "fin"};
        val = new Object[]{currentMutuel, type, mvt, debut, fin};
        re = (Double) dao.loadObjectByNameQueries("YvsMutOperationCompte.findSommeTypeOperationByNaturePeriodeAll", champ, val);
        return (re != null) ? re : 0;
    }

    public YvsMutCompte getComptesByType(List<YvsMutCompte> l, String nature) {
        if (l != null) {
            for (YvsMutCompte c : l) {
                if (c.getTypeCompte() != null) {
                    if (c.getTypeCompte().getNature().equals(nature)) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    public void loadInfosWarning(boolean disableWarning) {
        try {
            if (disableWarning) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isWarning", false);
            } else {
                isWarning = ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isWarning"));
                modelWarning = ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("modelWarning"));
                debutWarning = ((Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("debutWarning"));
                natureWarning = ((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("natureWarning"));
                idsWarning = ((Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idsWarning"));
                idsSearch = "";
                if (idsWarning != null) {
                    for (Long id : (List<Long>) idsWarning) {
                        if (!Util.asString(idsSearch)) {
                            idsSearch = id + "";
                        } else {
                            idsSearch += ", " + id;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addParamIds(boolean is_long) {
        int max = 100;
        ParametreRequete p = new ParametreRequete("y.id", "ids_warning", null, "IN", "AND");
        if (Util.asString(idsSearch) ? !idsSearch.equals("0") : false) {
            if (is_long) {
                List<Long> ids = new ArrayList<>();
                for (String id : idsSearch.split(",")) {
                    ids.add(Util.isNumeric(id) ? Long.valueOf(id.trim()) : 0);
                    if (ids.size() >= max) {
                        break;
                    }
                }
                p = new ParametreRequete("y.id", "ids_warning", ids, "IN", "AND");
            } else {
                List<Integer> ids = new ArrayList<>();
                for (String id : idsSearch.trim().split(",")) {
                    ids.add(Util.isNumeric(id) ? Integer.valueOf(id.trim()) : 0);
                    if (ids.size() >= max) {
                        break;
                    }
                }
                p = new ParametreRequete("y.id", "ids_warning", ids, "IN", "AND");
            }
        }
        paginator.addParam(p);
    }

    public int getEcartModelWarning(String modelWarning) {
        if (modelWarning != null ? modelWarning.trim().length() > 0 : false) {
            Integer ecart = (Integer) dao.loadObjectByNameQueries("YvsWarningModelDoc.findEcartModel", new String[]{"model", "societe"}, new Object[]{modelWarning, currentAgence.getSociete()});
            return ecart != null ? ecart : currentAgence.getSociete().getEcartDocument();
        }
        return currentAgence.getSociete().getEcartDocument();
    }

    public String nameTiers(long id, String table, String type) {
        return dao.nameTiers(id, table, type);
    }

    public Profil findOneProfil(Tiers tiers, String code) {
        int count = 0;
        Profil pp = null;
        for (Profil p : tiers.getProfils()) {
            if (p.getCode().startsWith(code) || p.getNomPrenom().startsWith(code)) {
                count++;
                pp = p;
            }
        }
        return (count == 1) ? pp : null;
    }
    /*
     *   action="INSERT OU UPDATE"
     *   mouvement="ENTREE OU SORTIE"
     **/

    public String controleStock(long article, long conditionnement, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return dao.controleStock(article, conditionnement, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }

    public String controleStock(long article, long conditionnement, long oldCond, long depot, long tranche, double newQte, double oldQte, String action, String mouvement, Date date, long lot) {
        return dao.controleStock(article, conditionnement, oldCond, depot, tranche, newQte, oldQte, action, mouvement, date, lot);
    }

    public void actionSearch() {
        actionSearch(search, null);
    }

    public void actionSearch(String reference, String type) {
        if (Util.asString(reference)) {
            Object[] result = asString(type) ? returnInfosByReferenceAndtype(reference, type) : returnInfosReference(null, reference);
            if (result != null ? result.length > 0 : false) {
//                Class classe = (Class) result[6];
                Object entity = dao.loadOneByNameQueries(result[3].toString(), (String[]) result[4], (Object[]) result[5]);
                if (entity != null) {
                    navigue(result[7].toString());
                    Managed managed = (Managed) giveManagedBean(result[8].toString());
                    if (managed != null) {
                        managed.onSelectObject((Serializable) entity);
                    }
                }
            }
        }
    }

    public static boolean isWindows() {
        return (Initialisation.OS.contains("win"));
    }

    public static boolean isMac() {
        return (Initialisation.OS.contains("mac"));
    }

    public static boolean isUnix() {
        return (Initialisation.OS.contains("nix") || Initialisation.OS.contains("nux") || Initialisation.OS.indexOf("aix") > 0);
    }

    public static boolean isSolaris() {
        return (Initialisation.OS.contains("sunos"));
    }

    public void controlConnexion() {
        if (currentAgence == null || currentUser == null) {
            if (!Initialisation.INITIALISATION) {
                redirectTohome();
            }
        } else if (currentUser != null) {
            if (currentUser.getUsers() == null) {
                redirectTohome();
            }
        }
    }

    public void redirectTohome() {
        try {
            //effacer le lien de navigation
            if (currentUser != null) {
                String req = "delete from yvs_connection where users=?";
                dao.requeteLibre(req, new yvs.dao.Options[]{new yvs.dao.Options(currentUser.getId(), 1)});
            }
            currentUser = new YvsUsersAgence();
            ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + Constantes.ADRESSE + "/");
        } catch (IOException ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createActionMethodeOnButton(CommandButton action, String methodeExpression) {
        try {
            MethodExpression method = null;
            try {
                method = context.getApplication().getExpressionFactory().createMethodExpression(
                        context.getELContext(), methodeExpression, null, new Class[]{ActionEvent.class});
            } catch (Exception ex) {
                Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (method != null) {
                MethodExpressionActionListener expression = new MethodExpressionActionListener(method);
                action.removeActionListener(expression);
                action.addActionListener(expression);
            }
        } catch (Exception ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void controlListAgence() {
        try {
            ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
            if (w != null) {
                for (YvsAgences a : w.getListAgence()) {
                    if (!listIdAgences.contains(a.getId())) {
                        listIdAgences.add(a.getId());
                    }
                }
            }
            if (!listIdAgences.contains(currentAgence.getId())) {
                listIdAgences.add(currentAgence.getId());
            }
        } catch (Exception ex) {
            getException("controlListAgence", ex);
        }
    }

    public void majInventaire(YvsComDocStocks inventaire, YvsBaseArticles article, YvsBaseConditionnement conditionnement, double quantite, String courant) {
        try {
            double reste = 0;
            champ = new String[]{"conditionnement", "document", "statut"};
            val = new Object[]{conditionnement, inventaire, Constantes.ETAT_VALIDE};
            YvsComContenuDocStock mouvement = (YvsComContenuDocStock) dao.loadOneByNameQueries("YvsComContenuDocStock.findOneByInventaire", champ, val);
            if (mouvement != null ? mouvement.getId() > 0 : false) {
                if (courant.equals(Constantes.MOUV_SORTIE) ? mouvement.getDocStock().getTypeDoc().equals(Constantes.TYPE_SS) : mouvement.getDocStock().getTypeDoc().equals(Constantes.TYPE_ES)) {
                    if (quantite >= mouvement.getQuantite()) {
                        reste = quantite - mouvement.getQuantite();
                        dao.delete(mouvement);
                    } else {
                        mouvement.setQuantite(mouvement.getQuantite() - quantite);
                        mouvement.setAuthor(currentUser);
                        mouvement.setDateUpdate(new Date());
                        dao.update(mouvement);
                    }
                } else {
                    reste = quantite;
                }
            } else {
                reste = quantite;
            }
            if (reste > 0) {
                boolean update = false;
                if (mouvement != null ? mouvement.getId() > 0 : false) {
                    update = courant.equals(Constantes.MOUV_SORTIE) ? mouvement.getDocStock().getTypeDoc().equals(Constantes.TYPE_ES) : mouvement.getDocStock().getTypeDoc().equals(Constantes.TYPE_SS);
                }
                if (update) {
                    mouvement.setQuantite(mouvement.getQuantite() + reste);
                    mouvement.setAuthor(currentUser);
                    mouvement.setDateUpdate(new Date());
                    dao.update(mouvement);
                } else {
                    champ = new String[]{"type", "document", "statut"};
                    val = new Object[]{(courant.equals(Constantes.MOUV_SORTIE) ? Constantes.TYPE_ES : Constantes.TYPE_SS), inventaire, Constantes.ETAT_VALIDE};
                    YvsComDocStocks document = (YvsComDocStocks) dao.loadOneByNameQueries("YvsComDocStocks.findByParentTypeStatut", champ, val);
                    if (document != null ? document.getId() < 1 : true) {
                        String reference;
                        if (courant.equals(Constantes.MOUV_SORTIE)) {
                            reference = genererReference(Constantes.TYPE_ES_NAME, inventaire.getDateDoc(), inventaire.getDestination().getId());
                        } else {
                            reference = genererReference(Constantes.TYPE_SS_NAME, inventaire.getDateDoc(), inventaire.getSource().getId());
                        }
                        if (!Util.asString(reference)) {
                            return;
                        }
                        document = new YvsComDocStocks();
                        document.setNumDoc(reference);
                        document.setNumPiece(inventaire.getNumDoc());
                        document.setDateDoc(inventaire.getDateDoc());
                        document.setHeureDoc(inventaire.getHeureDoc());
                        document.setTypeDoc(courant.equals(Constantes.MOUV_SORTIE) ? Constantes.TYPE_ES : Constantes.TYPE_SS);
                        document.setDestination(inventaire.getDestination());
                        document.setSource(inventaire.getSource());
                        document.setCreneauDestinataire(inventaire.getCreneauDestinataire());
                        document.setCreneauSource(inventaire.getCreneauSource());
                        document.setEditeur(inventaire.getEditeur());
                        document.setDocumentLie(inventaire);
                        document.setNature(Constantes.OP_AJUSTEMENT_STOCK);
                        document.setStatut(Constantes.ETAT_VALIDE);
                        document.setSociete(currentAgence.getSociete());
                        document.setAuthor(currentUser);
                        document.setId(null);
                        document = (YvsComDocStocks) dao.save1(document);
                    }
                    mouvement = new YvsComContenuDocStock(null);
                    mouvement.setConditionnement(conditionnement);
                    mouvement.setConditionnementEntree(conditionnement);
                    mouvement.setArticle(article);
                    mouvement.setQuantite(reste);
                    mouvement.setQuantiteEntree(reste);
                    mouvement.setDocStock(document);
                    mouvement.setPrix(conditionnement.getPrix());
                    mouvement.setPrixEntree(conditionnement.getPrix());
                    mouvement.setAuthor(currentUser);
                    mouvement.setId(null);
                    dao.save(mouvement);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Loggin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean asString(String value) {
        return value != null ? value.trim().length() > 0 : false;
    }

    public boolean asBoolean(String value) {
        try {
            if (value != null ? value.toLowerCase().equals("true") || value.toLowerCase().equals("false") : false) {
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public boolean asNumeric(String value) {
        try {
            if (value != null) {
                Float.valueOf(value);
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }
}
