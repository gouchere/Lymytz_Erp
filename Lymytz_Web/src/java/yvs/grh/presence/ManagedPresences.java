/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.presence;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.progressbar.ProgressBar;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.extensions.component.timepicker.TimePicker;
import org.primefaces.extensions.event.TimeSelectEvent;
import org.primefaces.model.UploadedFile;
import yvs.dao.Options;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.param.YvsJoursFeries;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhPlanningEmploye;
import yvs.entity.grh.presence.YvsGrhIoemDevice;
import yvs.entity.grh.presence.YvsGrhPointage;
import yvs.entity.grh.presence.YvsGrhPresence;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.grh.presence.YvsGrhTypeValidation;
import yvs.entity.grh.presence.YvsPointeuse;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsers;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.statistique.ManagedTableauBord;
import yvs.init.Initialisation;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.service.IEntitySax;
import yvs.service.grh.param.IYvsJoursOuvres;
import yvs.service.grh.presence.IYvsGrhPointage;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.SpecificDate;
import yvs.util.Util;
import yvs.util.Utilitaire;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class ManagedPresences extends Managed<PointageEmploye, YvsGrhPresence> implements Serializable {

    private long idLine = 0;
    private List<String> motifsPointage;

    private PointageEmploye pointageSortie = new PointageEmploye();

    private Date dateFiche = new Date();
    //contrôle la modification des fiche de pointage
    private boolean validFiche, applyInvalid = true; //pour afficher tous les employé de la société
    private boolean displayBtnCompens, displayBtnvalid;
    /*paramètre pour la modification*/
    private PresenceEmploye currentEmploye = new PresenceEmploye();

    private PointageEmploye pointage = new PointageEmploye(), pointageM = new PointageEmploye();

//    private boolean filterByScte = true;
    private String displayFerie;
    private int numPage = 0;

//    private long typeValidation;
    private long idDepartementFiter, agenceFilter, employeFilter;
    private Integer progress;

    private List<YvsGrhIoemDevice> ioemdevises;
    private YvsGrhIoemDevice selectIoemdevise;
    private YvsPointeuse selectPointeuse;
    private List<YvsPointeuse> pointeuses;
    private List<YvsGrhTrancheHoraire> tranchesHoraire;
    private List<YvsGrhPresence> fichesPresences, presencesResult;
    private YvsGrhPresence selectPresence = new YvsGrhPresence();
    private PresenceEmploye presenceResulSelect = new PresenceEmploye();
    private List<YvsGrhEmployes> employesDisplay;
    private boolean selectionAll = true;
    private int actionChange = 0;
    private String statut_result = "";

//    private Date dateToFind = new Date();//utile pour rechercher la fiche de présence à une date
    //charge les employés en paginant la vue
    int offset, position = 0;
    private boolean disPrev, disNext;
    private String codeEmploye;
    private String matriculeSearch;
    private Date debutSearch = new Date(), endSearch = new Date();

    private boolean displayPhotos;

    private int countNotif; //dis le nombre de fiche en attente de validation des heures sup.
    private long submitTo;

    private List<YvsGrhPointage> pointagesUpload;
    private UploadedFile file;
    private boolean deleteAllFiche;
    private String searchEmployeUpload;
    private boolean filterUpload, addFilterDateUpload;
    private YvsGrhEmployes filterEmployeUpload = new YvsGrhEmployes();
    private Date filterDateDebutUpload = new Date(), filterDateFinUpload = new Date();

    private YvsGrhEmployes employeRecapPeriod, employeRecapHebdo;
    private Date debutFiche = new Date(), finFiche = new Date(),
            debutRecapPeriod = new Date(), finRecapPeriod = new Date(),
            debutRecapHebdo = new Date(), finRecapHebdo = new Date(),
            dateDebut = new Date(), dateFin = new Date();

    private String operateurDuree = "=";
    private boolean paramDuree = false;
    private double searchDuree;
    private String tabIds;
    private Date searchTimeDebut, searchTimeFin;

    private TimePicker tp;
    private IEntitySax IEntitiSax = new IEntitySax();

    private boolean createTranche;

    public ManagedPresences() {
        pointeuses = new ArrayList<>();
        pointagesUpload = new ArrayList<>();
        motifsPointage = new ArrayList<>();
        ioemdevises = new ArrayList<>();
        employesDisplay = new ArrayList<>();
        presencesResult = new ArrayList<>();
        motifsPointage.add("En Service");
        motifsPointage.add("Absent");
        motifsPointage.add("En pause");
//        listRecapPresence = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(debutSearch);
        c.add(Calendar.DAY_OF_MONTH, -7);
        debutSearch = c.getTime();
        tranchesHoraire = new ArrayList<>();
        tp = new TimePicker();
    }

    public PresenceEmploye getPresenceResulSelect() {
        return presenceResulSelect;
    }

    public void setPresenceResulSelect(PresenceEmploye presenceResulSelect) {
        this.presenceResulSelect = presenceResulSelect;
    }

    public String getStatut_result() {
        return statut_result;
    }

    public void setStatut_result(String statut_result) {
        this.statut_result = statut_result;
    }

    public List<YvsGrhPresence> getPresencesResult() {
        return presencesResult;
    }

    public void setPresencesResult(List<YvsGrhPresence> presencesResult) {
        this.presencesResult = presencesResult;
    }

    public YvsGrhIoemDevice getSelectIoemdevise() {
        return selectIoemdevise;
    }

    public void setSelectIoemdevise(YvsGrhIoemDevice selectIoemdevise) {
        this.selectIoemdevise = selectIoemdevise;
    }

    public int getActionChange() {
        return actionChange;
    }

    public void setActionChange(int actionChange) {
        this.actionChange = actionChange;
    }

    public boolean isSelectionAll() {
        return selectionAll;
    }

    public void setSelectionAll(boolean selectionAll) {
        this.selectionAll = selectionAll;
    }

    public List<YvsGrhEmployes> getEmployesDisplay() {
        return employesDisplay;
    }

    public void setEmployesDisplay(List<YvsGrhEmployes> employesDisplay) {
        this.employesDisplay = employesDisplay;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public List<YvsGrhIoemDevice> getIoemdevises() {
        return ioemdevises;
    }

    public void setIoemdevises(List<YvsGrhIoemDevice> ioemdevises) {
        this.ioemdevises = ioemdevises;
    }

    public YvsPointeuse getSelectPointeuse() {
        return selectPointeuse;
    }

    public void setSelectPointeuse(YvsPointeuse selectPointeuse) {
        this.selectPointeuse = selectPointeuse;
    }

    public void selectedPointeuse(YvsPointeuse selectPointeuse) {
        setSelectPointeuse(selectPointeuse);
    }

    public List<YvsPointeuse> getPointeuses() {
        return pointeuses;
    }

    public void setPointeuses(List<YvsPointeuse> pointeuses) {
        this.pointeuses = pointeuses;
    }

    public long getEmployeFilter() {
        return employeFilter;
    }

    public void setEmployeFilter(long employeFilter) {
        this.employeFilter = employeFilter;
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

    public boolean isAddDate() {
        return addDate;
    }

    public void setAddDate(boolean addDate) {
        this.addDate = addDate;
    }

    public long getAgenceFilter() {
        return agenceFilter;
    }

    public void setAgenceFilter(long agenceFilter) {
        this.agenceFilter = agenceFilter;
    }

    public YvsGrhEmployes getEmployeRecapPeriod() {
        return employeRecapPeriod;
    }

    public void setEmployeRecapPeriod(YvsGrhEmployes employeRecapPeriod) {
        this.employeRecapPeriod = employeRecapPeriod;
    }

    public YvsGrhEmployes getEmployeRecapHebdo() {
        return employeRecapHebdo;
    }

    public void setEmployeRecapHebdo(YvsGrhEmployes employeRecapHebdo) {
        this.employeRecapHebdo = employeRecapHebdo;
    }

    public Date getDebutRecapPeriod() {
        return debutRecapPeriod;
    }

    public void setDebutRecapPeriod(Date debutRecapPeriod) {
        this.debutRecapPeriod = debutRecapPeriod;
    }

    public Date getFinRecapPeriod() {
        return finRecapPeriod;
    }

    public void setFinRecapPeriod(Date finRecapPeriod) {
        this.finRecapPeriod = finRecapPeriod;
    }

    public Date getDebutRecapHebdo() {
        return debutRecapHebdo;
    }

    public void setDebutRecapHebdo(Date debutRecapHebdo) {
        this.debutRecapHebdo = debutRecapHebdo;
    }

    public Date getFinRecapHebdo() {
        return finRecapHebdo;
    }

    public void setFinRecapHebdo(Date finRecapHebdo) {
        this.finRecapHebdo = finRecapHebdo;
    }

    public Date getDebutFiche() {
        return debutFiche;
    }

    public void setDebutFiche(Date debutFiche) {
        this.debutFiche = debutFiche;
    }

    public Date getFinFiche() {
        return finFiche;
    }

    public void setFinFiche(Date finFiche) {
        this.finFiche = finFiche;
    }

    public boolean isDeleteAllFiche() {
        return deleteAllFiche;
    }

    public void setDeleteAllFiche(boolean deleteAllFiche) {
        this.deleteAllFiche = deleteAllFiche;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getSearchEmployeUpload() {
        return searchEmployeUpload;
    }

    public void setSearchEmployeUpload(String searchEmployeUpload) {
        this.searchEmployeUpload = searchEmployeUpload;
    }

    public boolean isFilterUpload() {
        return filterUpload;
    }

    public void setFilterUpload(boolean filterUpload) {
        this.filterUpload = filterUpload;
    }

    public boolean isAddFilterDateUpload() {
        return addFilterDateUpload;
    }

    public void setAddFilterDateUpload(boolean addFilterDateUpload) {
        this.addFilterDateUpload = addFilterDateUpload;
    }

    public YvsGrhEmployes getFilterEmployeUpload() {
        return filterEmployeUpload;
    }

    public void setFilterEmployeUpload(YvsGrhEmployes filterEmployeUpload) {
        this.filterEmployeUpload = filterEmployeUpload;
    }

    public Date getFilterDateDebutUpload() {
        return filterDateDebutUpload;
    }

    public void setFilterDateDebutUpload(Date filterDateDebutUpload) {
        this.filterDateDebutUpload = filterDateDebutUpload;
    }

    public Date getFilterDateFinUpload() {
        return filterDateFinUpload;
    }

    public void setFilterDateFinUpload(Date filterDateFinUpload) {
        this.filterDateFinUpload = filterDateFinUpload;
    }

    public List<YvsGrhPointage> getPointagesUpload() {
        return pointagesUpload;
    }

    public void setPointagesUpload(List<YvsGrhPointage> pointagesUpload) {
        this.pointagesUpload = pointagesUpload;
    }

    public int getNumPage() {
        return numPage;
    }

    public void setNumPage(int numPage) {
        this.numPage = numPage;
    }

    public YvsGrhPresence getSelectPresence() {
        return selectPresence;
    }

    public void setSelectPresence(YvsGrhPresence selectPresence) {
        this.selectPresence = selectPresence;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isDisPrev() {
        return disPrev;
    }

    public void setDisPrev(boolean disPrev) {
        this.disPrev = disPrev;
    }

    public boolean isDisNext() {
        return disNext;
    }

    public void setDisNext(boolean disNext) {
        this.disNext = disNext;
    }

    public List<YvsGrhPointage> getPointages() {
        return pointages;
    }

    public void setPointages(List<YvsGrhPointage> pointages) {
        this.pointages = pointages;
    }

    public long getDureeTotalSevice() {
        return dureeTotalSevice;
    }

    public void setDureeTotalSevice(long dureeTotalSevice) {
        this.dureeTotalSevice = dureeTotalSevice;
    }

    public long getDureeToTalAbsence() {
        return dureeToTalAbsence;
    }

    public void setDureeToTalAbsence(long dureeToTalAbsence) {
        this.dureeToTalAbsence = dureeToTalAbsence;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public YvsGrhPresence getEntityPresence() {
        return entityPresence;
    }

    public void setEntityPresence(YvsGrhPresence entityPresence) {
        this.entityPresence = entityPresence;
    }

    public YvsGrhEmployes getEmp() {
        return emp;
    }

    public void setEmp(YvsGrhEmployes emp) {
        this.emp = emp;
    }

    public boolean isUpdateTypeV() {
        return updateTypeV;
    }

    public void setUpdateTypeV(boolean updateTypeV) {
        this.updateTypeV = updateTypeV;
    }

    public YvsGrhPresence getFicheToDel() {
        return ficheToDel;
    }

    public void setFicheToDel(YvsGrhPresence ficheToDel) {
        this.ficheToDel = ficheToDel;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public List<Long> getListExlusion1() {
        return listExlusion1;
    }

    public void setListExlusion1(List<Long> listExlusion1) {
        this.listExlusion1 = listExlusion1;
    }

    public int getNumroDroit() {
        return numroDroit;
    }

    public void setNumroDroit(int numroDroit) {
        this.numroDroit = numroDroit;
    }

    public void setDisplayPhotos(boolean displayPhotos) {
        this.displayPhotos = displayPhotos;
    }

    public boolean isDisplayPhotos() {
        return displayPhotos;
    }

    public List<YvsGrhPresence> getFichesPresences() {
        return fichesPresences;
    }

    public void setFichesPresences(List<YvsGrhPresence> fichesPresences) {
        this.fichesPresences = fichesPresences;
    }

    public List<YvsGrhTrancheHoraire> getTranchesHoraire() {
        return tranchesHoraire;
    }

    public void setTranchesHoraire(List<YvsGrhTrancheHoraire> tranchesHoraire) {
        this.tranchesHoraire = tranchesHoraire;
    }

    public long getIdDepartementFiter() {
        return idDepartementFiter;
    }

    public void setIdDepartementFiter(long idDepartementFiter) {
        this.idDepartementFiter = idDepartementFiter;
    }

    public int getCountNotif() {
        return countNotif;
    }

    public void setCountNotif(int countNotif) {
        this.countNotif = countNotif;
    }

    public String getDisplayFerie() {
        return displayFerie;
    }

    public void setDisplayFerie(String displayFerie) {
        this.displayFerie = displayFerie;
    }

    public PointageEmploye getPointageM() {
        return pointageM;
    }

    public void setPointageM(PointageEmploye pointageM) {
        this.pointageM = pointageM;
    }

    public PointageEmploye getPointageSortie() {
        return pointageSortie;
    }

    public void setPointageSortie(PointageEmploye pointageSortie) {
        this.pointageSortie = pointageSortie;
    }

    public String getMatriculeSearch() {
        return matriculeSearch;
    }

    public void setMatriculeSearch(String matriculeSearch) {
        this.matriculeSearch = matriculeSearch;
    }

    public Date getDebutSearch() {
        return debutSearch;
    }

    public void setDebutSearch(Date debutSearch) {
        this.debutSearch = debutSearch;
    }

    public Date getEndSearch() {
        return endSearch;
    }

    public void setEndSearch(Date endSearch) {
        this.endSearch = endSearch;
    }

    @Override
    public int getCurrentPage() {
        return numPage;
    }

    public void setCurrentPagePage(int numPage) {
        this.numPage = numPage;
    }

    public PointageEmploye getPointage() {
        return pointage;
    }

    public void setPointage(PointageEmploye pointage) {
        this.pointage = pointage;
    }

    public PresenceEmploye getCurrentEmploye() {
        return currentEmploye;
    }

    public void setCurrentEmploye(PresenceEmploye currentEmploye) {
        this.currentEmploye = currentEmploye;
    }

    public boolean isDisplayBtnvalid() {
        return displayBtnvalid;
    }

    public void setDisplayBtnvalid(boolean displayBtnvalid) {
        this.displayBtnvalid = displayBtnvalid;
    }

    public boolean isDisplayBtnCompens() {
        return displayBtnCompens;
    }

    public void setDisplayBtnCompens(boolean displayBtnCompens) {
        this.displayBtnCompens = displayBtnCompens;
    }

    public boolean isValidFiche() {
        return validFiche;
    }

    public void setValidFiche(boolean validFiche) {
        this.validFiche = validFiche;
    }

    public long getIdLine() {
        return idLine;
    }

    public void setIdLine(long idLine) {
        this.idLine = idLine;
    }

    public List<String> getMotifsPointage() {
        return motifsPointage;
    }

    public void setMotifsPointage(List<String> motifsPointage) {
        this.motifsPointage = motifsPointage;
    }

    public Date getDateFiche() {
        return dateFiche;
    }

    public void setDateFiche(Date dateFiche) {
        this.dateFiche = dateFiche;
    }

    public String getCodeEmploye() {
        return codeEmploye;
    }

    public void setCodeEmploye(String condeEmploye) {
        this.codeEmploye = condeEmploye;
    }

    public long getSubmitTo() {
        return submitTo;
    }

    public void setSubmitTo(long submitTo) {
        this.submitTo = submitTo;
    }

    public String getOperateurDuree() {
        return operateurDuree;
    }

    public void setOperateurDuree(String operateurDuree) {
        this.operateurDuree = operateurDuree;
    }

    public boolean isParamDuree() {
        return paramDuree;
    }

    public void setParamDuree(boolean paramDuree) {
        this.paramDuree = paramDuree;
    }

    public double getSearchDuree() {
        return searchDuree;
    }

    public void setSearchDuree(double searchDuree) {
        this.searchDuree = searchDuree;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public Date getSearchTimeDebut() {
        return searchTimeDebut;
    }

    public void setSearchTimeDebut(Date searchTimeDebut) {
        this.searchTimeDebut = searchTimeDebut;
    }

    public Date getSearchTimeFin() {
        return searchTimeFin;
    }

    public void setSearchTimeFin(Date searchTimeFin) {
        this.searchTimeFin = searchTimeFin;
    }

    @Override
    public boolean controleFiche(PointageEmploye bean) {
        if (dateFiche == null) {
            getMessage("Vous devez remplir la date de la fiche", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        if (currentUser == null) {
            getMessage("vous devez être connecté en tant qu'utilisateur avant d'effectuer cette opération", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }
//controle l'ajout d'une ligne de pointage

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        //charge la cofig général des ressources humaines
        List<YvsParametreGrh> lparam = (dao.loadNameQueries("YvsParametreGrh.findAll", champ, val, 0, 1));
        if (!lparam.isEmpty()) {
            paramGrh = lparam.get(0);
        }
    }

    public void loadPointeuses() {
        pointeuses = dao.loadNameQueries("YvsPointeuse.findAgence", new String[]{"societe", "agence"}, new Object[]{currentAgence.getSociete(), currentAgence});
    }

    public void loadFicheDate_(Date d, String fil) {
        String requete = null;
        champ = new String[]{"date", "agence"};
        val = new Object[]{d, currentAgence};
        if (fil.equals("S")) {
            requete = "YvsGrhPointage.findLastPointageAGSO";
        } else if (fil.equals("E")) {
            requete = "YvsGrhPointage.findLastPointageAGEN";
        }
        List<YvsGrhPresence> lpoi = dao.loadNameQueries(requete, champ, val);
        for (YvsGrhPresence poi : lpoi) {
            buildPresence(poi);
        }
    }

    public void navigateDate(boolean next) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFiche);
        if (next) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } else {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        dateFiche = cal.getTime();
    }

    //charge les fiches de pointage de cette date
    public void loadFicheDate(SelectEvent ev) {
        if (!autoriser("point_changeDateForm")) {
            openNotAcces();
            dateFiche = new Date();
            return;
        }
        if (ev != null) {
            dateFiche = (Date) ev.getObject();
            if (currentEmploye.getEmploye() != null) {
                if (currentEmploye.getEmploye().getId() > 0) {
                    loadDataPresenceEmploye(currentEmploye.getEmploye());
                    return;
                }
                currentEmploye.getPointages().clear();
            }
        }
    }

    private PresenceEmploye buildPresence(YvsGrhPresence p) {
        PresenceEmploye fi = new PresenceEmploye();
        double marge, duree, retard;
        if (p.getId() > 0) {
            fi.setId(p.getId());
            fi.setEmploye(p.getEmploye());
            fi.setTauxJournee((p.getTauxJournee() != null) ? p.getTauxJournee() : 0);
            fi.setTotalPresence((p.getTotalPresence() != null) ? p.getTotalPresence() : 0);
            fi.setPause(p.getDureePause());
            fi.setTotalHs(p.getTotalHeureSup());
            fi.setTotalHc(p.getTotalHeureCompensation());
            fi.setThs(Utilitaire.doubleToHour(p.getTotalHeureSup()));
            fi.setThc(Utilitaire.doubleToHour(p.getTotalHeureCompensation()));
            fi.setPointages(loadPointageFicheEmps(p)); //ce mode de chargement est choisi à pour mettre à profit le tri coté bd
            fi.setValider(p.getValider());
            fi.setDebut(p.getDateDebut());
            fi.setDateSave(p.getDateSave());
            fi.setDateUpdate(p.getDateUpdate());
            fi.setFin(p.getDateFin());
            fi.setFinPrevu(p.getDateFinPrevu());
            fi.setHeureDebut(p.getHeureDebut());
            fi.setHeureFin(p.getHeureFin());
            fi.setHeureFinPrevu(p.getHeureFinPrevu());
            fi.setTypeValidation(new TypeValidation(p.getId()));
            //compare le total de présence avec le nombre d'heure requis dans le contrat
            duree = (p.getTotalPresence() != null) ? p.getTotalPresence() : 0;
            retard = (p.getTotalRetard() != null) ? p.getTotalRetard() : 0;
            marge = (p.getMargeApprouve() != null) ? Utilitaire.timeToDouble(p.getMargeApprouve()) : 0;
            fi.setTpresence(Utilitaire.doubleToHour(duree));
            fi.setTretards(Utilitaire.doubleToHour(retard));
            double d = ((p.getDateFin().getTime() + p.getHeureFin().getTime()) - (p.getDateDebut().getTime() + p.getHeureDebut().getTime()) - ((p.getDureePause() != null) ? (p.getDureePause().getTime() + Constantes.HOUR) : 0)) / 3600000;
            fi.setTabsence(((d - (duree + marge)) > 0) ? Utilitaire.doubleToHour(d - (duree + marge)) : "0");
            fi.setAuteur((p.getAuthor() != null) ? p.getAuthor().getUsers().getNomUsers() : "");
        }
        fi.setEmploye(p.getEmploye());
        champ = new String[]{"date", "employe"};
        val = new Object[]{dateFiche, p.getEmploye()};
//CHERCHE si l'employé est présent
        //1.congé
        String lib = null;
        YvsGrhEmployes e_;
        YvsGrhCongeEmps c_ = (YvsGrhCongeEmps) dao.loadOneByNameQueries("YvsGrhCongeEmps.findByOneCongeDate", champ, val);
        e_ = (c_ != null) ? c_.getEmploye() : null;
        if (e_ == null) {
            //mission
            e_ = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhMissions.findByOneEmployeDate", champ, val);
            if (e_ == null) {
                //form ation
                e_ = (YvsGrhEmployes) (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhFormationEmps.findByOneEmployeDate", champ, val);
                if (e_ != null) {
                    lib = "En formation";
                }
            } else {
                lib = "En Mission";
            }
        } else {
            lib = (c_.getNature() == 'C') ? "En Congé" : "En Permission (" + (c_.getDureePermission()) + ")";
            fi.setRequisit2(!(c_.getNature() == 'P' && c_.getDureePermission() == 'C'));
        }
        fi.setRequisit(e_ != null);
        fi.setMotifAbsence(lib);
        return fi;
    }

    private void buildListEmploye(int limit, List<YvsGrhEmployes> l, List<YvsGrhPresence> lp) {
//        listeEmploye.clear();
        champ = new String[]{"date", "societe"};
        val = new Object[]{dateFiche, currentUser.getAgence().getSociete()};
        List<YvsGrhEmployes> listAbsent = dao.loadNameQueries("YvsMissions.findByEmployesDate", champ, val);
        List<YvsGrhEmployes> listAbsent1 = (dao.loadNameQueries("YvsFormationEmps.findByEmployesDate", champ, val));
        List<YvsGrhEmployes> listAbsent2 = (dao.loadNameQueries("YvsGrhCongeEmps.findByEmployeDate", champ, val));
        for (YvsGrhPresence p : lp) {
            if (l.contains(p.getEmploye())) {
                buildPresence(p);
                l.remove(p.getEmploye());
            }
        }
        //employée non encore  pointé
        for (YvsGrhEmployes e : l) {
            PresenceEmploye fi = new PresenceEmploye();
            fi.setId(e.getId());
            fi.setEmploye(e);
            if (listAbsent.contains(e)) {
                fi.setRequisit(true);
                fi.setMotifAbsence("En mission");
            }
            if (listAbsent1.contains(e)) {
                fi.setRequisit(true);
                fi.setMotifAbsence("En formation");
            }
            if (listAbsent2.contains(e)) {
                fi.setRequisit(true);
                fi.setMotifAbsence("En congé");
            }
        }
        //pour la pagination
        int n = l.size();
        disPrev = offset <= 0;
        if (n < limit) {
            disNext = true;
        } else if (n == 0) {
            disNext = true;
            disPrev = true;
        } else if (n > 0) {
            disNext = false;
            disPrev = false;
        }
        navigue(0);
    }

    private boolean dayeIn(Date dd, Date hd, Date df, Date hf, Date day) {
        return ((day.after(fabriqueTimeStamp(dd, hd)) && day.before(fabriqueTimeStamp(df, hf))) || day.equals(fabriqueTimeStamp(dd, hd)) || day.equals(fabriqueTimeStamp(df, hf)));
    }

    private YvsGrhPresence findGoodFiche(char statut, YvsGrhEmployes e, Date day) {
        champ = new String[]{"employe", "date"};
        val = new Object[]{e, Utilitaire.giveOnlyDate(day)}; //envoie la date sans les heures
        //contrôle la fiche de présence de l'employé pour cette journée    (s'il existe déjà une fiche de pointage de cette journée)     
        System.err.println("   Date pointage " + dft.format(day));
        List<YvsGrhPresence> l = dao.loadNameQueries("YvsGrhPresence.findOneFiche", champ, val);
        if (l.size() > 1) {//si un employé se retrouve avec deux  fiches (eventuellement) de présence dans une journée
            YvsGrhPresence p1 = l.get(0);
            //récupère le dernier pointage
            if (!p1.getPointages().isEmpty()) {
                YvsGrhPointage p = p1.getPointages().get(p1.getPointages().size() - 1);
                /*si le dernier pointage est une entrée, et que le pointage courant est une sortie, on suppose que la fiche utilisé est la fiche courante*/
                if (p.getHeureSortie() == null && statut == 'S') {
                    return p1;
                } else {
                    return l.get(1);
                }
            }
        } else if (l.size() == 1) {
            //si on trouve une fiche de présence à une date, ce n'est pas forcément le quart dans lequel l'employé doit travailler
            //on regarde le dernier pointage
            if (e.getHoraireDynamique()) {
                //Si le dernier pointage est une sortie et le pointage courant est une entrée et l'heure du pointage courant + marge est supérieure à l'heure de fin de cette fiche de présence, alors on considère que l'employé à eu des tranche qui se chevauchent                
                if (!l.get(0).getPointages().isEmpty()) {
                    YvsGrhPresence pr = l.get(0);
                    YvsGrhPointage p = pr.getPointages().get(pr.getPointages().size() - 1);
                    SpecificDate sp = new SpecificDate(day);
                    //il faut d'abord s'assurer que le pointage ne soit pas compris dans la plage de temps de la fiche courante et que la date du pointage soit différent de la date de début de la fiche     
                    if (!dayeIn(pr.getDateDebut(), pr.getHeureDebut(), pr.getDateFin(), pr.getHeureFin(), day) && fabriqueTimeStamp(day, null) != pr.getDateDebut()) {
                        if (paramGrh.getTimeMargeAvance() != null) {
                            if (p.getHeureSortie() != null && statut == 'E' && sp.addDate(paramGrh.getTimeMargeAvance()).after(l.get(0).getHeureFin())) {
                                return null;
                            }
                        }
                    }
                }
            }
            return l.get(0);
        }
        return null;
    }

    //enregistre la fiche de pointage d'une journée, et revoi null si la fiche n'a pas été créée par le fait qu'un employé ne dispose pas de planning pour ce jour par exemple
    private YvsGrhPresence createFicheEmploye(char statut, YvsGrhEmployes e, Date timePoint) {
        if (e != null) {
            //contrôle la fiche de présence de l'employé pour cette journée            
            YvsGrhPresence pr = findGoodFiche(statut, e, timePoint);
            //c'est probalement le premièr pointage de la journée
            if (pr == null) {
                pr = new YvsGrhPresence();
                pr.setEmploye(e);
                pr.setValider(false);
                pr.setMargeApprouve(null);
                pr.setTotalPresence(0.0);
                pr.setTotalHeureSup(0.0);
                pr.setTotalHeureCompensation(0.0);
                if (!e.getHoraireDynamique()) {
                    pr.setDateDebut(timePoint);
                    pr.setDateFin(timePoint);
                    pr.setDateFinPrevu(timePoint);
                    //si l'utilisateur n'a pas de calendrier, on utilise le calendrier par défaut de la société
                    if (e.getContrat() != null) {
                        YvsCalendrier calendar;
                        if (e.getContrat().getCalendrier() != null) {
                            calendar = e.getContrat().getCalendrier();
                            //Verifie si le calandrier a une entrée pour ce jour                           
                        } else {
                            calendar = paramGrh.getCalendrier();
                        }
                        boolean b = false;
                        if (calendar != null) {
                            Calendar now = Calendar.getInstance();
                            now.setTime(timePoint);
                            for (YvsJoursOuvres jo : calendar.getJoursOuvres()) {
                                if (jo.getJour().toUpperCase().equals(Utilitaire.getDay(now).toUpperCase())) {
                                    pr.setHeureDebut(jo.getHeureDebutTravail());
                                    pr.setHeureFin(jo.getHeureFinTravail());
                                    pr.setHeureFinPrevu(jo.getHeureFinTravail());
                                    pr.setDureePause(jo.getDureePause());
                                    pr.setAuthor(currentUser);
                                    b = true;
                                    break;
                                }
                            }
                            if (!b) {
                                getErrorMessage("Le calendrier de travail de cet employé n'est pas configuré ou est mal configuré !");
                                return null;
                            }
                            return (YvsGrhPresence) dao.save1(pr);
                        } else {
                            getErrorMessage("Cet employé ne peut être pointé. Problème avec le calendrier de travail ");
                        }
                    } else {
                        getErrorMessage("L'empoyé doit d'abord disposer d'un contrat");
                    }
                } else {
                    List<YvsGrhPlanningEmploye> l = dao.loadNameQueries("YvsGrhPlanningEmploye.findByEmployeDay", champ, val);
                    YvsGrhPlanningEmploye pe = null;
                    if (l.size() > 1) {
                        YvsGrhPlanningEmploye p1 = l.get(0);
                        if (extratHour(timePoint, p1.getTranche().getHeureDebut(), p1.getTranche().getHeureFin())) {
                            pe = p1;
                        } else {
                            pe = l.get(1);
                        }
                    } else if (l.size() == 1) {
                        pe = l.get(0);
                    }
                    if (pe != null) {
                        pr.setDateDebut(pe.getDateDebut());
                        pr.setDateFin(pe.getDateFin());
                        pr.setHeureDebut(pe.getTranche().getHeureDebut());
                        pr.setHeureFin(pe.getTranche().getHeureFin());
                        pr.setHeureFinPrevu(pe.getTranche().getHeureFin());
                        pr.setDureePause(pe.getDureePause());
                        pr.setMargeApprouve(null);
                        pr.setTotalPresence(0.0);
                        pr.setTotalHeureSup(0.0);
                        pr.setTotalHeureCompensation(0.0);
                        pr.setAuthor(currentUser);
                        pr.setId(null);
                        return (YvsGrhPresence) dao.save1(pr);
                    } else {
                        if (paramGrh.getCalculPlaningDynamique()) {
                            //récupère les tranches horaires
                            long timeMarge = (paramGrh.getTimeMargeAvance() != null) ? paramGrh.getTimeMargeAvance().getTime() : 0;
                            if (e.getContrat() == null) {
                                getErrorMessage("Aucun contrat n'a été trouvé pour cet employé !!");
                                return null;
                            }
                            List<YvsGrhTrancheHoraire> lt;
                            if (e.getContrat().getTypeTranche() != null) {
                                champ = new String[]{"societe", "typeJ"};
                                val = new Object[]{currentUser.getAgence().getSociete(), e.getContrat().getTypeTranche()};
                                lt = dao.loadNameQueries("YvsGrhTrancheHoraire.findAllByType", champ, val);
                            } else {
                                champ = new String[]{"societe"};
                                val = new Object[]{currentUser.getAgence().getSociete()};
                                lt = dao.loadNameQueries("YvsGrhTrancheHoraire.findAll", champ, val);
                            }
                            if (lt.isEmpty()) {
                                getErrorMessage("Impossible de continuer", "Vous devez paramétrer les tranche horaire de travail !");
                                return null;
                            } else {
                                int i = 0;
                                Calendar dayPointage = Calendar.getInstance();
                                dayPointage.setTime(timePoint);
                                for (YvsGrhTrancheHoraire t : lt) {
                                    if (t.getHeureDebut().after(t.getHeureFin())) {
                                        dayPointage.add(Calendar.DAY_OF_MONTH, 1);
                                    }
                                    if (timePoint.getTime() <= fabriqueTimeStamp(timePoint, t.getHeureDebut()).getTime()) {
                                        return savePlanningAndPresence(t, e);
                                    } else if (timePoint.getTime() + timeMarge <= fabriqueTimeStamp(dayPointage.getTime(), t.getHeureFin()).getTime()) {
                                        return savePlanningAndPresence(t, e);
                                    }
                                }
                            }
                        } else {
                            getErrorMessage("Cet employé ne peut être pointé. Il ne dispose pas de planning pour cette journée");
                        }
                    }
                }
            } else {
                return pr;
            }
        }
        return null;
    }

    private YvsGrhPresence savePlanningAndPresence(YvsGrhTrancheHoraire tr, YvsGrhEmployes e) {
        YvsGrhPlanningEmploye pe = new YvsGrhPlanningEmploye();
        pe.setActif(true);
        pe.setAuthor(currentUser);
        pe.setDateDebut(dateFiche);
        pe.setDateFin(calculDateFinPlaning(dateFiche, tr));
        pe.setEmploye(new YvsGrhEmployes(e.getId()));
        pe.setTranche(tr);
        pe = (YvsGrhPlanningEmploye) dao.save1(pe);
        YvsGrhPresence pr = new YvsGrhPresence();
        pr.setEmploye(new YvsGrhEmployes(e.getId()));
        pr.setValider(false);
        pr.setTotalPresence(0.0);
        pr.setTotalHeureSup(0.0);
        pr.setTotalHeureCompensation(0.0);
        pr.setDateDebut(pe.getDateDebut());
        pr.setDateFin(pe.getDateFin());
        pr.setDateFinPrevu(pe.getDateFin());
        pr.setHeureDebut(pe.getTranche().getHeureDebut());
        pr.setHeureFin(pe.getTranche().getHeureFin());
        pr.setHeureFinPrevu(pe.getTranche().getHeureFin());
        pr.setDureePause((pe.getDureePause() == null) ? paramGrh.getCalendrier().getJoursOuvres().get(0).getDureePause() : pe.getDureePause());
        pr.setAuthor(currentUser);
        return (YvsGrhPresence) dao.save1(pr);
    }

    private Date calculDateFinPlaning(Date db, YvsGrhTrancheHoraire tr) {
        Calendar c = Calendar.getInstance();
        c.setTime(db);
        if (tr.getHeureDebut().after(tr.getHeureFin())) {
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        return c.getTime();
    }

    private YvsGrhPresence findOtherFichePresence(YvsGrhEmployes e, Date day, YvsGrhPresence pr, String mvt) {
        champ = new String[]{"employe", "date"};
        val = new Object[]{new YvsGrhEmployes(e.getId()), day};
        List<YvsGrhPlanningEmploye> l = dao.loadNameQueries("YvsGrhPlanningEmploye.findByEmployeDay", champ, val);
        if (l.size() > 1) {
            YvsGrhPlanningEmploye p1 = l.get(0);
            if (p1.getDateDebut().equals(pr.getDateDebut()) && p1.getDateFin().equals(pr.getDateFin()) && mvt.equals("S")) {
                return pr;
            } else {
                return factoryNewPresencePresence(e, p1);
            }
        } else if (l.size() == 1) {
            return pr;
        }
        return pr;
    }

    private YvsGrhPresence factoryNewPresencePresence(YvsGrhEmployes e, YvsGrhPlanningEmploye pl) {
        YvsGrhPresence pr = new YvsGrhPresence();
        pr.setEmploye(new YvsGrhEmployes(e.getId()));
        pr.setValider(false);
        pr.setTotalPresence((double) 0);
        pr.setDateDebut(pl.getDateDebut());
        pr.setDateFin(pl.getDateFin());
        pr.setDateFinPrevu(pl.getDateFin());
        pr.setHeureDebut(pl.getTranche().getHeureDebut());
        pr.setHeureFin(pl.getTranche().getHeureFin());
        pr.setHeureFinPrevu(pl.getTranche().getHeureFin());
        pr.setDureePause(pl.getDureePause());
        pr.setAuthor(currentUser);
        return (YvsGrhPresence) dao.save1(pr);
    }

    public void addLinePointageEmps(char statut) {
        if (currentEmploye != null) {
            if (currentEmploye.getEmploye().getId() > 0) {
                if (!currentEmploye.isValider()) {
                    if (paramGrh != null) {
                        savePointage(statut, fabriqueTimeStamp(dateFiche, new Date()), currentEmploye.getEmploye());
                        currentEmploye.setPointages(loadFicheEmps(currentEmploye.getEmploye()));
                        if (!pointages.isEmpty()) {
                            currentEmploye.setTpresence(Utilitaire.doubleToHour((pointages.get(0).getPresence().getTotalPresence() == null) ? 0 : pointages.get(0).getPresence().getTotalPresence()));
                        }
                        update("AllPointage-tab1-e");
                        update("AllPointage-tab-e");
                    } else {
                        getErrorMessage("Votre module GRH n'est pas bien configuré !");
                    }
                } else {
                    getErrorMessage("La fiche de présence de ce jour est déjà validé !");
                }
            } else {
                getWarningMessage("Aucun employé n'a été selectionné !");
            }
        }
    }

    private void saveSimplePointageSortie(YvsGrhPointage entityPoi, boolean pValid, YvsGrhPresence fich, Date heure) {
        entityPoi = buildPointageNormale(entityPoi, pValid, heure);
        entityPoi.setHeureSortie(heure);
        entityPoi.setId(null);
        entityPoi.setHeureEntree(null);
        entityPoi.setPresence(fich);
        entityPoi.setDateSaveSortie(new Date());
        entityPoi.setHeurePointage(new Date());
        entityPoi.setOperateurSortie(currentUser.getUsers());
        dao.save(entityPoi);
    }

    private void saveLinePointage(YvsGrhPresence fich, Date debut, Date sortie, boolean valid) {
        YvsGrhPointage entityPoi = new YvsGrhPointage();
        entityPoi.setHeurePointage(new Date());
        entityPoi.setValider(valid);
        entityPoi.setHoraireNormale(valid);
        entityPoi.setActif(true);
        entityPoi.setAuthor(currentUser);
        entityPoi.setPointageAutomatique(false);
        entityPoi.setHeureSortie(sortie);
        entityPoi.setId(null);
        entityPoi.setHeureEntree(debut);
        entityPoi.setPresence(fich);
        entityPoi.setDateSaveEntree(new Date());
        entityPoi.setDateSaveSortie(new Date());
        entityPoi.setHeurePointage(new Date());
        entityPoi.setOperateurSortie(currentUser.getUsers());
        entityPoi.setOperateurEntree(currentUser.getUsers());
        dao.save(entityPoi);
    }

    private void savePointage(char statut, Date heure, YvsGrhEmployes e) {
        YvsGrhPresence currentFiche = createFicheEmploye(statut, e, heure);
        if (currentFiche != null) {
            if (currentFiche.getValider()) {
                getErrorMessage("Impossible de continuer !", "Cette fiche a déjà été validé !");
                return;
            }
            currentEmploye = buildPresence(currentFiche);
            boolean pValid; //permet de vérifier que le pointage se fait dans la borne requise de travail            
            //3.compare l'heure de pointage aux bornes limites du calendrier
            pValid = pointageValid(currentFiche.getDateDebut(), currentFiche.getHeureDebut(), currentFiche.getDateFin(), currentFiche.getHeureFin(), heure, e.getContrat().getCalendrier());
            //récupère le dernier pointage
            YvsGrhPointage last = giveLastPointage(currentFiche);
            if (last != null) {
                if (!pointageOk(last, heure)) {
                    getErrorMessage("Le dernier pointage supérieure à été effectué à " + dft.format((last.getHeureSortie() != null) ? last.getHeureSortie() : last.getHeureEntree()));
                    return;
                }
            }
            YvsGrhPointage entityPoi = giveEntityPointage();
            entityPoi.setPresence(currentFiche);
            if (!positionOk(currentFiche, heure, null)) { //vérifie si le l'heure de pontage indiqué ne se trouve pas dans un intervalle de pointage déjà effectué
                getErrorMessage("Le pontage est mal positionné !");
                return;
            }
            if (statut == 'S') {
                if (!pValid && e.getHoraireDynamique()) {
                    System.err.println(" Pointage invalide ");
                    //2. s'il existe un second planning
                    //2.1 puisque c'est une sortie , on insère dans la fiche de présence en cours
                    YvsGrhPresence fiche = findOtherFichePresence(e, heure, currentFiche, "S");
                    if (!fiche.equals(currentFiche)) {
                        last = null;
                    }
                }
                if (last == null) {
                    //insère une sortie valide à l'heure donnée
                    saveSimplePointageSortie(entityPoi, pValid, currentFiche, heure);
                } else {
                    if (last.getHeureEntree() != null && last.getHeureSortie() == null) {
                        if (pointageValidG(last.getHeureEntree(), currentFiche)) {
                            if (pointageValidG(heure, currentFiche)) {
                                if (pointageValidD(currentFiche.getDateFin(), currentFiche.getHeureFin(), heure)) {
                                    last.setHeureSortie(heure);
                                    last.setValider(pValid);
                                    last.setHoraireNormale(last.getValider());
                                    last.setAuthor(currentUser);
                                    last.setDateSaveSortie(new Date());
                                    last.setHeurePointage(new Date());
                                    if (pointageOk(last)) {
                                        dao.update(last);
                                    } else {
                                        getErrorMessage("Pointage non valide !", "la correspondance entre les heure est incorrect");
                                    }
                                } else {
                                    last.setHeureSortie(fabriqueTimeStamp(heure, currentFiche.getHeureFin()));
                                    last.setValider(pointageJourOuvrable(currentFiche.getDateDebut(), (e.getContrat().getCalendrier() != null) ? e.getContrat().getCalendrier() : paramGrh.getCalendrier()));
                                    last.setHoraireNormale(last.getValider());
                                    last.setAuthor(currentUser);
                                    last.setDateSaveSortie(new Date());
                                    last.setHeurePointage(new Date());
                                    dao.update(last);
                                    //insère un nouveau pointage 
                                    saveLinePointage(currentFiche, fabriqueTimeStamp(currentFiche.getDateFin(), currentFiche.getHeureFin()), heure, pValid);
                                }
                            } else {
                                //le pointage de la sortie est avant l'heure de début
                                saveSimplePointageSortie(entityPoi, pValid, currentFiche, heure);
                            }
                        } else {
                            //le dernier pointage n'est pas valide à gauche. ie que si c'est le premier pointage par exemple, il a été effectué avant l'heure de début
                            /**
                             * *********************
                             */
                            if (pointageValidG(heure, currentFiche)) {
                                //insère la sortie à l'heure de debut
                                last.setHeureSortie(fabriqueTimeStamp(currentFiche.getDateDebut(), currentFiche.getHeureDebut()));
                                last.setValider(false);
                                last.setHoraireNormale(last.getValider());
                                last.setAuthor(currentUser);
                                last.setDateSaveSortie(new Date());
                                last.setHeurePointage(new Date());
                                dao.update(last);
                                if (pointageValidD(currentFiche.getDateFin(), currentFiche.getHeureFin(), heure)) {
                                    saveLinePointage(currentFiche, fabriqueTimeStamp(currentFiche.getDateDebut(), currentFiche.getHeureDebut()), heure, pointageJourOuvrable(currentFiche.getDateDebut(), (e.getContrat().getCalendrier() != null) ? e.getContrat().getCalendrier() : paramGrh.getCalendrier()));
                                } else {
                                    saveLinePointage(currentFiche, fabriqueTimeStamp(currentFiche.getDateDebut(), currentFiche.getHeureDebut()), fabriqueTimeStamp(currentFiche.getDateFin(), currentFiche.getHeureFin()), pointageJourOuvrable(currentFiche.getDateDebut(), (e.getContrat().getCalendrier() != null) ? e.getContrat().getCalendrier() : paramGrh.getCalendrier()));
                                    saveLinePointage(currentFiche, fabriqueTimeStamp(currentFiche.getDateFin(), currentFiche.getHeureFin()), heure, pValid);
                                }
                            } else {
                                //le pointage de la sortie est avant l'heure de début
                                saveSimplePointageSortie(entityPoi, pValid, currentFiche, heure);
                            }
                            /**
                             * *********************
                             */
                        }
                    } else {
                        //si l'entrée est nul ou la sortie n'est pas null ou E!=null et S!=null
                        //insère une sortie valide à l'heure donnée
                        saveSimplePointageSortie(entityPoi, pValid, currentFiche, heure);
                    }
                }
            } else {
                //le pointage est une entrée
                //si retour de commission
                if (last != null) {
                    if (last.getHeureEntree() != null && last.getMotifSortie().equals(Constantes.GRH_EN_COMMISSION)) {
                        saveRetourComission(last);
                        return;
                    }
                }
                if (!pValid && e.getHoraireDynamique()) {
                    //2. s'il existe un second planning
                    //2.1 puisque c'est une entrée , on insère dans la fiche de présence en cours
                    YvsGrhPresence fiche = findOtherFichePresence(e, heure, currentFiche, "E");
                    if (!fiche.equals(currentFiche)) {
                        last = null;
                    }
                }
//                entityPoi = giveEntityPointage();
                entityPoi.setHeureEntree(heure);
                entityPoi.setValider(pValid);
                entityPoi.setHoraireNormale(pValid);
                entityPoi.setHeurePointage(new Date());
                entityPoi.setPresence(currentFiche);
                //prenons en compte la marge lors du premiers pointage (Heure d'arrivée le matin)
                if (last == null && e.getContrat() != null) {
                    //si je pointe la première fois à une heure supérieure à l'heure de début de mon calendrie
                    Calendar hour = Calendar.getInstance();
                    hour.setTime(heure);
                    double now = (double) hour.get(Calendar.HOUR_OF_DAY) + ((double) hour.get(Calendar.MINUTE) / 60.0);
                    hour.setTime(currentFiche.getHeureDebut());
                    double heureDebutFiche = (double) hour.get(Calendar.HOUR_OF_DAY) + ((double) hour.get(Calendar.MINUTE) / 60.0);
                    if (now > heureDebutFiche) {
                        YvsCalendrier calendar;
                        if (e.getContrat().getCalendrier() != null) {
                            calendar = e.getContrat().getCalendrier();
                            //Verifie si le calandrier a une entrée pour ce jour                           
                        } else {
                            calendar = paramGrh.getCalendrier();
                        }
                        Calendar c = Calendar.getInstance();
                        c.setTime(currentFiche.getHeureDebut());
                        Calendar marg = Calendar.getInstance();
                        if (calendar.getTempsMarge() != null) {
                            marg.setTime(calendar.getTempsMarge());  //on défini le temps marge par calendrier
                            c.add(Calendar.HOUR_OF_DAY, marg.get(Calendar.HOUR_OF_DAY));
                            c.add(Calendar.MINUTE, marg.get(Calendar.MINUTE));
                            //modifie la fiche pour persister la marge si l'heure d'entrée - la marge est inférieur ou égale à l'heure de début de la fiche                            
                            if (fabriqueTimeStamp(dateFiche, heure).before(c.getTime())) {
                                currentFiche.setMargeApprouve(marg.getTime());
                                dao.update(currentFiche);
                            }
                        }
                    }
                }
                entityPoi.setOperateurEntree(currentUser.getUsers());
                entityPoi.setId(null);
                dao.save(entityPoi);
                succes();
            }
        } else {
            getErrorMessage("Aucne fiche n'a été crée pour cet employé !");
        }
    }

    private YvsGrhPointage buildPointageNormale(YvsGrhPointage p, boolean valid, Date h) {
        p.setHeureSortie(h);
        p.setHeurePointage(new Date());
        p.setValider(valid);
        p.setHoraireNormale(valid);
        p.setActif(true);
        p.setAuthor(currentUser);
        p.setPointageAutomatique(false);
        return p;
    }

    public YvsGrhPointage giveLastPointage(YvsGrhPresence fiche) {
        champ = new String[]{"fiche"};
        val = new Object[]{fiche};
        return (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findLastPointage", champ, val);
    }

    //vérifie que l'heure du pointage en cours est tjrs supérieur à l'heure du dernier pointage
    private boolean pointageOk(YvsGrhPointage p) {
        if (p.getPresence() != null) {
            if (p.getHeureSortie() != null && p.getHeureEntree() != null) {
                if (p.getPresence().getHeureDebut().before(p.getPresence().getHeureFin())) {
                    return (p.getHeureSortie().after(p.getHeureEntree()) || p.getHeureEntree().equals(p.getHeureSortie()));
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean pointageOk(YvsGrhPointage last, Date newD) {
        if (last.getPresence() != null) {
            if (last.getHeureSortie() != null) {
                return (last.getHeureSortie().before(newD) || last.getHeureSortie().equals(newD));
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean pointageValid(Date dd, Date hd, Date df_, Date hf, Date day, YvsCalendrier cal) {
        Date deb = fabriqueTimeStamp(dd, hd);
        Date fin_ = fabriqueTimeStamp(df_, hf);
        day = fabriqueTimeStamp(day, null);
        //trouve si le jour de pointage est valide
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        String jour = Utilitaire.getDay(c);
        champ = new String[]{"jour", "calendrier"};
        val = new Object[]{jour, cal};
        YvsJoursOuvres jo = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", champ, val);
        boolean ok = (jo != null) ? jo.getOuvrable() : false;
        return (((day.after(deb) || day.equals(deb)) && (day.before(fin_) || day.equals(fin_))) && ok);
    }

    private boolean pointageJourOuvrable(Date day, YvsCalendrier cal) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        String jour = Utilitaire.getDay(c);
        champ = new String[]{"jour", "calendrier"};
        val = new Object[]{jour, cal};
        YvsJoursOuvres jo = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", champ, val);
        boolean ok = (jo != null) ? jo.getOuvrable() : false;
        return ok;
    }

    //vérifie si le pointage est valide à gauche: ie que l'heure de pointage est supérieure à l'heure de début du travail
    private boolean pointageValidG(Date day, YvsGrhPresence fiche) {
        Date dd = fiche.getDateDebut();
        Date hd = fiche.getHeureDebut();
        Date df = fiche.getDateFin();
        Date hf = fiche.getHeureFin();
        Date deb = fabriqueTimeStamp(dd, hd);
        if (df.after(dd) || hf.before(hd)) {
            Calendar c = Calendar.getInstance();
            c.setTime(dd);
            c.add(Calendar.DAY_OF_MONTH, 1);
            day = fabriqueTimeStamp(c.getTime(), day);
        }
        return (day.after(deb) || day.equals(deb));
    }

    private boolean pointageValidD(Date end, Date hf, Date day) {
        Date deb = fabriqueTimeStamp(end, hf);
        return (day.before(deb) || day.equals(deb));
    }
    //charge la fiche de présence d'un employé
    List<YvsGrhPointage> pointages;

    long dureeTotalSevice = 0, dureeToTalAbsence = 0;
//charge les pointages d'un employé à une date donnée

    private List<PointageEmploye> loadFicheEmps(YvsGrhEmployes e) {
        champ = new String[]{"employe", "date"};
        val = new Object[]{e, dateFiche};
        //récupère les pointage du jour de l'employé        
        pointages = dao.loadNameQueries("YvsGrhPointage.findPointageEmps", champ, val);
        return UtilGrh.buildBeanPointage(pointages);
    }

    private List<PointageEmploye> loadPointageFicheEmps(YvsGrhPresence e) {
        champ = new String[]{"fiche"};
        val = new Object[]{e};
        //récupère les pointage du jour de l'employé        
        pointages = dao.loadNameQueries("YvsGrhPointage.findByFiche", champ, val);
        return UtilGrh.buildBeanPointage(pointages);
    }
    int row;
//modifie une ligne de pointage

    public void changePointage(RowEditEvent ev) {
        if (!autoriser("point_modifPointage")) {
            openDialog("dlgNotAcces");
            return;
        }
        if (!currentEmploye.isValider()) {
            PointageEmploye f = (PointageEmploye) ev.getObject();
            DataTable tab = (DataTable) ev.getSource();
            row = tab.getRowIndex();
            YvsGrhPointage p = findPointage(f.getId());
            if (intervalleOk(p.getPresence().getDateDebut(), p.getPresence().getDateFin(), f.getEntree(), f.getSortie())) {
//                updateEntree(f.getEntree(), p);
//                updateSortie(f.getSortie(), p);
            } else {
                getErrorMessage("Intervalle incorrecte !");
                f.setEntree(p.getHeureEntree());
                f.setSortie(p.getHeureSortie());
            }
            update("tab-presence-emps1");
        } else {
            getErrorMessage("Impossible de modifier cette fiche de pointage elle est déjà valide !");
        }
    }

    private boolean intervalleOk(Date dd, Date df, Date hd, Date hf) {
        if (hd == null || hf == null) {
            return true;
        }
        Calendar d = Calendar.getInstance();
        d.setTime(dd);
        Calendar h = Calendar.getInstance();
        h.setTime(hd);
        d.set(Calendar.HOUR_OF_DAY, h.get(Calendar.HOUR_OF_DAY));
        d.set(Calendar.MINUTE, h.get(Calendar.MINUTE));
        d.set(Calendar.SECOND, h.get(Calendar.SECOND));
        d.set(Calendar.MILLISECOND, h.get(Calendar.MILLISECOND));
        Calendar d1 = Calendar.getInstance();
        if (hd.after(hf)) {
            d1.setTime(df);
        } else {
            d1.setTime(dd);
        }
        Calendar h1 = Calendar.getInstance();
        h1.setTime(hf);
        d1.set(Calendar.HOUR_OF_DAY, h1.get(Calendar.HOUR_OF_DAY));
        d1.set(Calendar.MINUTE, h1.get(Calendar.MINUTE));
        d1.set(Calendar.SECOND, h1.get(Calendar.SECOND));
        return d.before(d1);
    }

    private boolean positionOk(YvsGrhPresence presence, Date h, YvsGrhPointage pointage) {
        champ = new String[]{"fiche", "date"};
        val = new Object[]{presence, h};
        List<YvsGrhPointage> l = dao.loadNameQueries("YvsGrhPointage.findTrue", champ, val);
        if (l.size() == 1) {
            if (pointage != null) {
                return pointage.equals(l.get(0));
            } else {
                return true;
            }
        } else {
            return l.isEmpty();
        }
    }

    public void selectLinePointage(SelectEvent ev) {
        PointageEmploye f = (PointageEmploye) ev.getObject();
        pointage = f;
        displayBtnCompens = !f.isHoraireNormale();
        displayBtnvalid = !f.isValider();
        row = currentEmploye.getPointages().indexOf(f);
        idLine = f.getId();
    }

    public void deleteLine(PointageEmploye po) {
        if (!autoriser("point_delLinePointage")) {
            openNotAcces();
            return;
        }
        if (po != null) {
            if (!currentEmploye.isValider()) {
                champ = new String[]{"id"};
                val = new Object[]{po.getId()};
                YvsGrhPointage p = (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findById", champ, val);
                p.setValider(pointage.isValider());
                p.setHeureEntree(pointage.getEntree());
                p.setHeureSortie(pointage.getSortie());
                p.setActif(true);
                p.setAuthor(currentUser);
                p.setOperateurEntree(currentUser.getUsers());
                p.setOperateurSortie(currentUser.getUsers());
                dao.delete(p);
                currentEmploye.getPointages().remove(po);
                update("AllPointage-tab-e");
            } else {
                getErrorMessage("Impossible de modifier cette fiche de pointage elle est déjà valide !");
            }
        }
    }

    public boolean invalideLine(PointageEmploye po) {
        if (!autoriser("point_invalidPointage")) {
            openNotAcces();
            return false;
        }
        if (!currentEmploye.isValider()) {
            if (po != null) {
                champ = new String[]{"id"};
                val = new Object[]{po.getId()};
                YvsGrhPointage p = (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findById", champ, val);
                p.setValider(false);
                p.setHeureSupplementaire(false);
                p.setCompensationHeure(false);
//                p.setHeureEntree(pointage.getEntree());
//                p.setHeureSortie(pointage.getSortie());
                p.setActif(true);
                p.setAuthor(currentUser);
                dao.update(p);
                po.setValider(false);
                update("AllPointage-tab-e");
            }
        } else {
            getErrorMessage("Cette fiche de présence est déjà validé !");
            return false;
        }
        return true;
    }

    public void initDatePointage() {
        pointageM.setDateDebut(currentEmploye.getDebut());
        update("form_saisieM");
    }

    public void insertionManuelle() {
        if (!autoriser("point_pointageManuel")) {
            openNotAcces();
            return;
        }
        if (currentEmploye.getEmploye().getId() > 0) {
//            if (currentEmploye.isRequisit()) {
//                getErrorMessage("Impossible de pointer cet employé !!", currentEmploye.getMotifAbsence());
//                return;
//            }
            if (pointageM.getEntree() != null) {
                pointage.setStatut('E');
//                insertPointage(pointageM.getEntree());
//                savePointage('E', fabriqueTimeStamp(dateFiche, pointageM.getEntree()), currentEmploye.getEmploye());
            }
            if (pointageM.getSortie() != null) {
                pointage.setStatut('S');
                insertPointage(fabriqueTimeStamp(pointageM.getDateDebut(), pointageM.getSortie()));
//                savePointage('S', fabriqueTimeStamp(dateFiche, pointageM.getSortie()), currentEmploye.getEmploye());
            }
            //si le pointage c'est bien déroulé reload pour affichage
            if (currentEmploye != null) {
                currentEmploye.setPointages(loadFicheEmps(currentEmploye.getEmploye()));
                if (!pointages.isEmpty()) {
                    currentEmploye.setTpresence(Utilitaire.doubleToHour((pointages.get(0).getPresence().getTotalPresence() == null) ? 0 : pointages.get(0).getPresence().getTotalPresence()));
                }
                update("AllPointage-tab1-e");
                update("AllPointage-tab-e");
            }
        } else {
            getWarningMessage("Aucun employé n'a été sélectionné !");
        }
    }
    private int numberPage;

    public int getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(int numberPage) {
        this.numberPage = numberPage;
    }

    public void goTo(int dir) {
        numPage = dir;
        if (numPage >= 0) {
            List<YvsGrhEmployes> l = new ArrayList<>();
            switch (numroDroit) {
                case 0:
                    champ = new String[]{"societe"};
                    val = new Object[]{currentUser.getAgence().getSociete()};
                    l = dao.loadNameQueries("YvsGrhEmployes.findAlls", champ, val, numPage, 1);
                    break;
                case 1:
                    champ = new String[]{"agence"};
                    val = new Object[]{currentUser.getAgence()};
                    l = dao.loadNameQueries("YvsGrhEmployes.findAll", champ, val, numPage, 1);
                    break;
                case 2:
                    if (currentUser.getUsers().getEmploye() != null) {
                        if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
                            List<Integer> re = giveAllSupDepartement(currentUser.getUsers().getEmploye().getPosteActif().getDepartement());
                            champ = new String[]{"departement"};
                            val = new Object[]{re};
                            l = dao.loadNameQueries("YvsGrhEmployes.findByServiceIN", champ, val, numPage, 1);
                        }
                    }
                    break;
                case 3:
                    if (currentUser.getUsers().getEmploye() != null) {
                        champ = new String[]{"equipe"};
                        val = new Object[]{currentUser.getUsers().getEmploye().getEquipe()};
                        l = dao.loadNameQueries("YvsGrhEmployes.findByEquipe", champ, val, numPage, 1);
                    }
                    break;
                default:
                    getErrorMessage("Aucun employé trouvé à cette page !");
                    return;
            }
            if (!l.isEmpty()) {
                loadDataPresenceEmploye(l.get(0));
            } else {
                numPage = numPage - dir;
            }
            validFiche = currentEmploye.isValider();
            displayBtnCompens = false;
            displayBtnvalid = false;
        } else {
            numPage = 0;
        }

    }

    private List<YvsGrhEmployes> buildEmployeByFilter(long idDepartement) {
        if (idDepartement > 0) {
            listIdSubDepartement.clear();
            giveAllSupDepartement((YvsGrhDepartement) dao.loadOneByNameQueries("YvsGrhDepartement.findById", new String[]{"id"}, new Object[]{idDepartement}));
            champ = new String[]{"departement"};
            val = new Object[]{listIdSubDepartement};
            return dao.loadNameQueries("YvsGrhEmployes.findByServiceIN", champ, val, (numPage - 1), 1);
        }
        return null;
    }

    public void navigue(int dir) {
        if (numPage < 0) {
            numPage = 0;
        }
        numPage = numPage + dir;
        if (numPage > 0) {
            //cherche le prochain employé
            List<YvsGrhEmployes> l = null;
            switch (numroDroit) {
                case 0:
                    if (idDepartementFiter <= 0) {
                        champ = new String[]{"societe"};
                        val = new Object[]{currentUser.getAgence().getSociete()};
                        l = dao.loadNameQueries("YvsGrhEmployes.findAlls", champ, val, (numPage - 1), 1);
                    } else {
                        l = buildEmployeByFilter(idDepartementFiter);
                    }
                    break;
                case 1:
                    if (idDepartementFiter <= 0) {
                        champ = new String[]{"agence"};
                        val = new Object[]{currentAgence};
                        l = dao.loadNameQueries("YvsGrhEmployes.findAll", champ, val, (numPage - 1), 1);
                    } else {
                        l = buildEmployeByFilter(idDepartementFiter);
                    }
                    break;
                case 2:
                    if (idDepartementFiter <= 0) {
                        if (currentUser.getUsers().getEmploye() != null) {
                            if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
                                listIdSubDepartement.clear();
                                giveAllSupDepartement(currentUser.getUsers().getEmploye().getPosteActif().getDepartement());
                                champ = new String[]{"departement"};
                                val = new Object[]{listIdSubDepartement};
                                l = dao.loadNameQueries("YvsGrhEmployes.findByServiceIN", champ, val, (numPage - 1), 1);
                            }
                        }
                    } else {
                        l = buildEmployeByFilter(idDepartementFiter);
                    }
                    break;
                case 3:
                    if (currentUser.getUsers().getEmploye() != null) {
                        champ = new String[]{"equipe"};
                        val = new Object[]{currentUser.getUsers().getEmploye().getEquipe()};
                        l = dao.loadNameQueries("YvsGrhEmployes.findByEquipe", champ, val, numPage, 1);
                    }
                    break;
            }
            if ((l != null) ? !l.isEmpty() : false) {
                loadDataPresenceEmploye(l.get(0));
            } else {
                currentEmploye.setDebut(dateFiche);
                numPage = 0;
                navigue(1);
            }
        } else {
            numPage = 0;
            navigue(1);
        }
    }
    YvsGrhPresence entityPresence;

    private void loadDataPresenceEmploye(YvsGrhEmployes emp) {
        if (emp != null) {
            champ = new String[]{"date", "employe"};
            val = new Object[]{dateFiche, emp};
            entityPresence = (YvsGrhPresence) dao.loadOneByNameQueries("YvsGrhPresence.findOneFiche", champ, val);
            if (entityPresence != null) {
                currentEmploye = buildPresence(entityPresence);
                validFiche = currentEmploye.isValider();
                displayBtnCompens = false;
                displayBtnvalid = false;
            } else {
                entityPresence = new YvsGrhPresence();
                entityPresence.setEmploye(emp);
                entityPresence.setId((long) -1);
                currentEmploye = buildPresence(entityPresence);
            }
        }
    }

    private void loadDataPresenceEmploye(YvsGrhPresence p) {
        if (p != null) {
            entityPresence = p;
            if (entityPresence != null) {
                currentEmploye = buildPresence(entityPresence);
                validFiche = currentEmploye.isValider();
                displayBtnCompens = false;
                displayBtnvalid = false;
            } else {
                entityPresence = new YvsGrhPresence();
                entityPresence.setEmploye(emp);
                entityPresence.setId((long) -1);
                currentEmploye = buildPresence(entityPresence);
            }
        }
    }

    //fabrique l'entity pointage à partir des données des beans
    private YvsGrhPointage giveEntityPointage() {
        YvsGrhPointage p = new YvsGrhPointage();
        p.setId(null);
        p.setHeureEntree(null);
        p.setHeureSortie(null);
        p.setActif(true);
        p.setCompensationHeure(false);
        p.setDescriptionCommission(pointage.getDescriptionMotif());
        p.setHeureSupplementaire(false);
        p.setHoraireNormale(true);
        p.setMotifSortie(pointage.getMotif());
        if (pointage.getStatut() == 'E') {
            p.setOperateurEntree(currentUser.getUsers());
            p.setDateSaveEntree(new Date());
        } else {
            p.setOperateurSortie(currentUser.getUsers());
            p.setDateSaveSortie(new Date());
        }
        p.setPointageAutomatique(false);
        p.setValider(true);
        p.setMotifSortie(Constantes.GRH_ABSENT);
        p.setAuthor(currentUser);
        return p;
    }

    /**
     * Cas de la pause pour un employé
     */
    private String timePause;

    public String getTimePause() {
        return timePause;
    }

    public void setTimePause(String timePause) {
        this.timePause = timePause;
    }

    public void confirmPause() {
        timePause = Managed.dft.format(new Date());
        openDialog("dlgconfirmpause");
    }
//la validation de l'heure de pause consiste en l'insertion d'une ligne de pointage dans la fiche de présence

    public void validPause() {
        //construire l'objet métier et l'inserer et enregistrer en précisant le motif EN PAUSE
//        String[] champ = new String[]{"codeUsers", "agence"};
//        Object[] val = new Object[]{currentUser.getCodeUsers(), currentAgence};
//        YvsGrhEmployes userEmp = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCodeUsers1", champ, val);
        if (emp != null) {
            YvsGrhPresence currentFiche = createFicheEmploye('S', emp, new Date());
            if (currentFiche != null) {
                //recherche l'employé à partir de son matricule ou de son code utilisateur       
                //s'assurer que l'employé n'a pas déjà pris une pause se jour
                champ = new String[]{"motif", "fiche"};
                val = new Object[]{Constantes.GRH_EN_PAUSE, currentFiche};
                YvsGrhPointage p = (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findByMotif", champ, val);
                if (p == null) {
                    p = giveLastPointage(currentFiche);
                    if (p != null) {
                        if (p.getHeureSortie() == null) {
                            p.setMotifSortie(Constantes.GRH_EN_PAUSE);
                            p.setHeureSortie(new Date());
                            p.setActif(true);
                            p.setOperateurSortie(currentUser.getUsers());
                            p.setAuthor(currentUser);
                            dao.update(p);
                        } else {
                            //s'il n ya pas de d'entrée
                            p = giveEntityPointage();
                            p.setMotifSortie(Constantes.GRH_EN_PAUSE);
                            p.setPresence(currentFiche);
                            p.setHeureSortie(new Date());
                            p.setOperateurSortie(currentUser.getUsers());
                            p.setValider(true);
                            p.setActif(true);
                            p.setHoraireNormale(true);
                            p.setAuthor(currentUser);
                            p.setId(null);
                            dao.save(p);
                        }
                        succes();
                    }
                    displayLinkInOut();
                    update("head-top-template");
                } else {
                    getMessage("Un pointage pour votre pause a déjà été efectué !", FacesMessage.SEVERITY_WARN);
                }
            }
        }
    }

    private String motifSortie, descriptionMotif;
    boolean ValidMany;

    public String getMotifSortie() {
        return motifSortie;
    }

    public void setMotifSortie(String motifSortie) {
        this.motifSortie = motifSortie;
    }

    public String getDescriptionMotif() {
        return descriptionMotif;
    }

    public void setDescriptionMotif(String descriptionMotif) {
        this.descriptionMotif = descriptionMotif;
    }

    public void openValidFiche(boolean validMany) {
        openDialog("dlgConfirmValid");
        this.ValidMany = validMany;
    }

    public void toogleOpenValidFiche(ValueChangeEvent ev) {
        if (ev != null) {
            Boolean b = (Boolean) ev.getNewValue();
            if (b) {
                openDialog("dlgConfirmValid");
            } else {
                openDialog("dlgConfirmInValid");
            }
        }
        this.ValidMany = false;
    }

    public void valideFiche() {
        if (!ValidMany) {
            _valideFiche(currentEmploye, dateFiche, true);
        } else {
            List<Integer> ids = decomposeSelection(tabIds);
            if (ids.isEmpty()) {
                getWarningMessage("Aucune selection n'a été trouvé !");
                return;
            }
            for (Integer i : ids) {
                if (i >= 0 && i < fichesPresences.size()) {
                    _valideFiche(buildPresence(fichesPresences.get(i)), fichesPresences.get(i).getDateDebut(), false);
                }
            }
            succes();
            update("tab_recap_presence_list");
        }
    }

    private void _valideFiche(PresenceEmploye _currentPresence, Date _dateFiche, boolean single) {
        if (!autoriser("point_valide_fiche")) {
            openNotAcces();
            _currentPresence.setValider(false);
            return;
        }
        //Si la fiche à valider est la mienne, je dois disposer du droit de me valider moi même
        if (currentUser.getUsers().getEmploye() != null) {
            if (currentUser.getUsers().getEmploye().equals(_currentPresence.getEmploye())) {
                if (!autoriser("point_valideMe")) {
                    openNotAcces();
                    getErrorMessage("Vous ne disposez pas du droit de valider votre propre fiche !");
                    _currentPresence.setValider(false);
                    return;
                }
            }
        }
        //controle la date de validation (si la date de validation n'est pas la date en cours, vérifier si le user à le droit de valider dans le passé)        
        if (!_currentPresence.getDebut().equals(Utilitaire.getIniTializeDate(new Date()).getTime())) {
            if (!autoriser("point_savePointageInPast")) {
                getErrorMessage("Vous n'avez pas accès à la validation différé des fiche de présence !");
                openNotAcces();
                _currentPresence.setValider(false);
                return;
            } else {
                //évaluer le décalage en jour pour savoir s'il est dans la marge de jour requise
                if (Utilitaire.countDayBetweenDate(_currentPresence.getDebut(), new Date()) > paramGrh.getDelaisValidationPointage()) {
                    if (!autoriser("point_savePointageInLongPast")) {
                        getErrorMessage("Vous n'avez pas le droit de valider une fiche au delà de " + paramGrh.getDelaisValidationPointage());
                        openNotAcces();
                        _currentPresence.setValider(false);
                        return;
                    }
                }
            }
        }
        champ = new String[]{"employe", "date"};
        val = new Object[]{new YvsGrhEmployes(_currentPresence.getEmploye().getId()), _currentPresence.getDebut()};
        //contrôle la fiche de présence de l'employé pour cette journée            
        YvsGrhPresence pr = (YvsGrhPresence) dao.loadOneByNameQueries("YvsGrhPresence.findSimpleOneFiche", champ, val);
        if (pr != null) {
            //on s'assure que la fiche a au moins un pointage valide
            if (!fichePresenceOk(pr.getPointages())) {
                getErrorMessage("Impossible de valider cette fiche ", "au moins un pointage doit être valide !");
                _currentPresence.setValider(false);
                return;
            }
            if (_currentPresence.getTypeValidation().getId() > 0) {
                //on s'assure que le total des présence est supérieur au total minimale d'heures de travail requise
                if (!totalHeureFicheOk(pr, _currentPresence.getMargeApprouve())) {
                    //si le quotas d'heure n'est pas atteint, on contrôle si c'est un jour non ouvrable et que les heures sup sont supérieure à zero et que l'utilisateur à le droit de validation d'une fiche ne comprenant que les heures sup
                    YvsJoursOuvres jo = findJourOuvre(pr.getEmploye().getContrat(), _currentPresence.getDebut());
                    if (jo != null) {
                        if ((!jo.getOuvrable() || jo.getJourDeRepos()) && pr.getTotalHeureSup() > 0) {
                            //droit de validation de ce type de fiche
                            if (!autoriser("point_valid_fiche_onlyHs")) {
                                getErrorMessage("Vous ne disposez pas de privilèges pour ce type de validation", "Le quotas d'heures requis n'a pas été atteint");
                                openNotAcces();
                                _currentPresence.setValider(false);
                                return;
                            }
                        } else {
                            getErrorMessage("Impossible de valider cette fiche ", "Le quota d'heures requis n'a pas été atteint");
                            _currentPresence.setValider(false);
                            return;
                        }
                    } else {
                        getErrorMessage("Impossible de valider cette fiche ", "Cette journée n'a pas été trouvée dans le calendrier de l'employé");
                        _currentPresence.setValider(false);
                        return;
                    }
                }
                //mise à jour de la marge apprové
                long marge = ((pr.getMargeApprouve() != null) ? pr.getMargeApprouve().getTime() : 0) + ((currentEmploye.getMargeApprouve() != null) ? currentEmploye.getMargeApprouve().getTime() : 0);
                pr.setMargeApprouve(new Date(marge));
                pr.setTauxJournee(_currentPresence.getTauxJournee());
                pr.setValider(true);
                pr.setTypeValidation(new YvsGrhTypeValidation(currentEmploye.getTypeValidation().getId()));
                pr.setAuthor(currentUser);
                pr.setTotalHeureSup(currentEmploye.getTotalHs());
                pr.setValiderHs(autoriser("point_update_hs"));
                if (submitTo > 0) {
                    pr.setSubmitTo(new YvsUsers(submitTo));
                }
                pr = (YvsGrhPresence) dao.update(pr); //pour permettre la prise encompte des heures sup
                //récupère 
                _currentPresence.setValider(true);
                int idx = fichesPresences.indexOf(pr);
                if (idx >= 0) {
                    fichesPresences.set(idx, pr);
                }
                if (single) {
                    succes();
                    update("img_valid_fiche");
                    update("tab_recap_presence_list");
                    closeDialog("dlgConfirmValid");
                }
            } else {
                _currentPresence.setValider(false);
                getErrorMessage("Vous devez choisir un type mode de validation");
            }
        } else {
            _currentPresence.setValider(false);
            getErrorMessage("Cet employé ne dispose d'aucune fiche de pointage!");
        }
    }

    public void openToValidHs(YvsGrhPresence p) {
        loadDataPresenceEmploye(p);
        update("zone_edit_valid_hs");
        openDialog("dlgConfirmInValidHs");
    }

    public void confirmValidHs() {
        if (entityPresence != null) {
            if (entityPresence.getValider() && entityPresence.getTotalPresence() > 0) {
                if (autoriser("point_update_hs")) {
                    entityPresence.setMargeApprouve(currentEmploye.getMargeApprouve());
                    entityPresence.setTotalHeureSup(currentEmploye.getTotalHs());
                    entityPresence.setAuthor(currentUser);
                    entityPresence.setValiderHs(true);
                    dao.update(entityPresence);
                    succes();
                } else {
                    openNotAcces();
                }
            } else {
                getErrorMessage("La fiche ne requière aucune validation en heures supplémentaire !");
            }
        }
    }

    private YvsJoursOuvres findJourOuvre(YvsGrhContratEmps co, Date day) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(day);
        if (co.getCalendrier() != null && day != null) {
            for (YvsJoursOuvres j : co.getCalendrier().getJoursOuvres()) {
                if (j.getJour().trim().toLowerCase().equals(Utilitaire.getDay(ca).trim().toLowerCase())) {
                    return j;
                }
            }
        }
        return null;
    }

    public void inValideFiche() {
        if (!autoriser("point_invalide_fiche")) {
            openNotAcces();
            return;
        }
        champ = new String[]{"employe", "date"};
        val = new Object[]{new YvsGrhEmployes(currentEmploye.getEmploye().getId()), dateFiche};
        YvsGrhPresence pr = (YvsGrhPresence) dao.loadOneByNameQueries("YvsGrhPresence.findOneFiche", champ, val);
        if (pr != null) {
            //mise à jour de la marge apprové
            pr.setMargeApprouve(null);
            pr.setTauxJournee(0.0);
            pr.setValider(false);
            pr.setTypeValidation(null);
            pr.setAuthor(currentUser);
            dao.update(pr);
            currentEmploye.setValider(false);
            succes();
            update("img_valid_fiche");
            closeDialog("dlgConfirmValid");

        } else {
            getErrorMessage("Cet employé ne dispose d'aucune fiche de pointage!");
        }
    }

    private boolean fichePresenceOk(List<YvsGrhPointage> l) {
        for (YvsGrhPointage p : l) {
            if (p.getValider()) {
                return true;
            }
        }
        return false;
    }

    private boolean totalHeureFicheOk(YvsGrhPresence pr, Date marg) {
        if (currentEmploye.getTypeValidation().getId() > 0) {
            int idx = typesValidation.indexOf(new YvsGrhTypeValidation(currentEmploye.getTypeValidation().getId()));
            //si c'est un jour Férié, on ne contrôle plus
            if (isFerie(pr.getDateDebut())) {
                return true;
            }
            if (idx >= 0) {
                double marge = Utilitaire.timeToDouble(pr.getMargeApprouve());
                marge = marge + Utilitaire.timeToDouble(marg);
                if (typesValidation.get(idx).getTempsMinimal() <= 0) {
                    return (pr.getTotalPresence() + marge) >= paramGrh.getHeureMinimaleRequise();
                } else {
                    return (pr.getTotalPresence() + marge) >= typesValidation.get(idx).getTempsMinimal();
                }
            }

        }
        return false;
    }

    public void findEmps() {
        if (codeEmploye != null) {
            champ = new String[]{"codeUsers", "agence"};
            val = new Object[]{"%" + codeEmploye + "%", currentAgence};
            List<YvsGrhEmployes> l = dao.loadNameQueries("YvsGrhEmployes.findByCodeUsers", champ, val);
            champ = new String[]{"date", "employe"};
            val = new Object[]{dateFiche, "%" + codeEmploye + "%"};
            List<YvsGrhPresence> lp = dao.loadNameQueries("YvsGrhPresence.findByEmployeAndDate", champ, val);
            buildListEmploye(100, l, lp);
        }
    }

    private YvsGrhEmployes findEmployeByDroit(YvsGrhEmployes emp) {
        switch (numroDroit) {
            case 0: //société
                return emp;
            case 1: //agence
                if (currentUser.getAgence().equals(emp.getAgence())) {
                    return emp;
                }
            case 2: //Département
                if (currentUser.getUsers().getEmploye() != null) {
                    if (currentUser.getUsers().getEmploye().getPosteActif() != null && emp.getPosteActif() != null) {
                        if (currentUser.getUsers().getEmploye().getPosteActif().getDepartement().equals(emp.getPosteActif().getDepartement())) {
                            return emp;
                        }
                    } else {
                        getErrorMessage("Votre profil employé n'est pas rattaché à un poste !");
                    }
                } else {
                    getErrorMessage("Aucun profil employé n'est rattaché à ce compte");
                }
                break;
            case 3: //Equipe
                if (currentUser.getUsers().getEmploye() != null) {
                    if (currentUser.getUsers().getEmploye().getEquipe().equals(emp.getEquipe())) {
                        return emp;
                    }
                } else {
                    getErrorMessage("Vous devez être dans une Equipe !");
                }
                break;
            default: //Moi seul
                return null;
        }
        return null;
    }

    public void findOneEmploye() {
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            service.findEmploye(codeEmploye);
            if (!service.getListEmployes().isEmpty()) {
                loadDataPresenceEmploye(service.getListEmployes().get(0));
            } else {
                currentEmploye = new PresenceEmploye();
                displayBtnCompens = false;
                displayBtnvalid = false;
                getWarningMessage("Aucun employé trouvé !");
            }
        }
        initDatePointage();
        numPage = 0;
    }

    //valder une ligne de pointage
    public void valideLine() {
        if (row > 0) {
//            YvsGrhPresence p = pointages.get(row);
//            p.setValider(true);
//            p.setGenerateur('U');
//            dao.update(p);
        }
    }

    public void validHs(boolean continueValid) {
        if (!autoriser("point_validPointageHs")) {
            openNotAcces();
            return;
        }
        if (row >= 0) {
            if (currentEmploye.isValider()) {
                getErrorMessage("Cette fiche de présence est déjà validé !");
                return;
            }
            YvsGrhPointage p = findPointage(currentEmploye.getPointages().get(row).getId());
            if (p.getHeureSupplementaire()) {
                if (!continueValid) {
                    openDialog("dlgConfirmChange");
                    return;
                }
            }
            //compare le nombre d'heure supplémentaire à la limite autorisé
            double hour = 0;
            if (p.getHeureEntree() != null && p.getHeureSortie() != null) {
                if (p.getHeureSortie().after(p.getHeureEntree())) {
                    hour = ((double) p.getHeureSortie().getTime() - (double) p.getHeureEntree().getTime()) / 3600000;
                } else {
                    hour = 24 + ((double) p.getHeureSortie().getTime() - (double) p.getHeureEntree().getTime()) / 3600000;
                }
            }
            hour = currentEmploye.getTotalHs() + hour;
            if (hour >= paramGrh.getLimitHeureSup()) {
                getErrorMessage("Le quotas quotidien d'heures supplémentaires ne peut être dépassé !");
                return;
            }
            p.setHeureSupplementaire(true);
            p.setCompensationHeure(false);
            p.setValider(true);
            dao.update(p);
            currentEmploye.getPointages().get(row).setValider(true);
            update("AllPointage-tab-e");
        } else {
            getMessage("Aucune ligne n'est sélectionné", FacesMessage.SEVERITY_WARN);
        }
    }

    private YvsGrhPointage findPointage(Long id) {
        champ = new String[]{"id"};
        val = new Object[]{id};
        return (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findById", champ, val);
    }

    //heure de compensation     
    public void validHc(boolean continueValid) {
        if (!autoriser("point_validPointageHc")) {
            openNotAcces();
            return;
        }
//        row=pointages.indexOf(new YvsGrhPointage(idLine));
        if (row >= 0) {
            if (currentEmploye.isValider()) {
                getErrorMessage("Cette fiche de présence est déjà validé !");
                return;
            }
            YvsGrhPointage p = findPointage(currentEmploye.getPointages().get(row).getId());
            if (p.getHeureSupplementaire()) {
                if (!continueValid) {
                    openDialog("dlgConfirmChange1");
                    return;
                }
            }
            p.setCompensationHeure(true);
            p.setHeureSupplementaire(false);
            p.setValider(true);
            dao.update(p);
            currentEmploye.getPointages().get(row).setValider(true);
            update("AllPointage-tab-e");
        } else {
            getMessage("Aucune ligne n'est sélectionné", FacesMessage.SEVERITY_WARN);
        }
    }

    public void validePointage() {
        if (!autoriser("point_validPointage")) {
            openNotAcces();
            return;
        }
        if (row >= 0) {
            YvsGrhPointage p = findPointage(currentEmploye.getPointages().get(row).getId());
            p.setCompensationHeure(false);
            p.setHeureSupplementaire(false);
            p.setValider(true);
            dao.update(p);
            //recalcul le total présence
            YvsGrhPresence pr = (YvsGrhPresence) dao.loadOneByNameQueries("YvsGrhPresence.findById", new String[]{"id"}, new Object[]{p.getPresence().getId()});
            currentEmploye = buildPresence(pr);
            currentEmploye = buildPresence(pr);
            if (fichesPresences != null) {
                int idx = fichesPresences.indexOf(pr);
                if (idx >= 0) {
                    fichesPresences.set(idx, p.getPresence());
                }
            }
        } else {
            getMessage("Aucune ligne n'est sélectionné", FacesMessage.SEVERITY_WARN);
        }
    }

    public void inValiderPointage() {
        if (row >= 0) {
            if (invalideLine(currentEmploye.getPointages().get(row))) {
                YvsGrhPointage p = findPointage(currentEmploye.getPointages().get(row).getId());
                currentEmploye.getPointages().get(row).setValider(false);
                currentEmploye = buildPresence(p.getPresence());
            }
        } else {
            getMessage("Aucune ligne n'est sélectionné", FacesMessage.SEVERITY_WARN);
        }
    }

    public void naviguIndate(int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateFiche);
        c.add(Calendar.DAY_OF_MONTH, day);
        dateFiche = c.getTime();
        if (modeValidF <= 0) {
            loadFichePresenceAllEmps();
        } else {
            loadRecap(dao.loadNameQueries("YvsGrhPresence.findByDateSDAndTV", new String[]{"date", "societe", "typeV"}, new Object[]{dateFiche, currentUser.getAgence().getSociete(), new YvsGrhTypeValidation(modeValidF)}), true);
        }
    }

    public void loadFichePresenceAllEmps() {
        //filtre enn fonction des drois de vue des employé      
        droitViewEmploye();
        List<YvsGrhPresence> lp = null;
        switch (numroDroit) {
            case 0: //société
                lp = dao.loadNameQueries("YvsGrhPresence.findByDateD", new String[]{"date", "agence"}, new Object[]{dateFiche, currentUser.getAgence()});
                break;
            case 1: //agence
                lp = dao.loadNameQueries("YvsGrhPresence.findByDateD", new String[]{"date", "agence"}, new Object[]{dateFiche, currentUser.getAgence()});
                break;
            case 2: //Département
                if (currentUser.getUsers().getEmploye() != null) {
                    if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
                        List<Integer> re = giveAllSupDepartement(currentUser.getUsers().getEmploye().getPosteActif().getDepartement());
                        lp = dao.loadNameQueries("YvsGrhPresence.findByDateDepartIN", new String[]{"date", "departement"}, new Object[]{dateFiche, re});
                    } else {
                        getErrorMessage("Votre profil employé n'est pas rattaché à un poste !");
                    }
                } else {
                    getErrorMessage("Aucun profil employé n'est rattaché à ce compte");
                }
                break;
            case 3: //Equipe
                if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
                    lp = dao.loadNameQueries("YvsGrhPresence.findByDateEquipe", new String[]{"date", "equipe"}, new Object[]{dateFiche, currentUser.getAgence()});
                } else {
                    getErrorMessage("Vous devez être dans une Equipe !");
                }
                break;
            default: //Moi seul
                lp = dao.loadNameQueries("YvsGrhPresence.findOneFiche", new String[]{"date", "employe"}, new Object[]{dateFiche, currentUser.getAgence()});
                break;
        }
        loadRecap(lp, true);
    }

    private void loadRecap(List<YvsGrhPresence> lp, boolean all) {
        if (lp != null) {
            champ = new String[]{"date", "societe"};
            val = new Object[]{dateFiche, currentUser.getAgence().getSociete()};
            List<YvsGrhEmployes> employesEnMission = dao.loadNameQueries("YvsMissions.findByEmployesDate", champ, val);
            List<YvsGrhEmployes> employesEnFormations = (dao.loadNameQueries("YvsFormationEmps.findByEmployesDate", champ, val));
            List<YvsGrhEmployes> employesEnConges = (dao.loadNameQueries("YvsGrhCongeEmps.findByEmployeDate", champ, val));
//            listRecapPresence.clear();
            double duree, marge;
            List<YvsGrhEmployes> le = new ArrayList<>();
            if (all) {
                ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
                if (service != null) {
                    service.addParamActif(true);
                    le.addAll(service.getListEmployes());
                }
            }
            for (YvsGrhPresence pe : lp) {
                PresenceEmploye e = new PresenceEmploye();
                e.setId(pe.getId());
                e.setEmploye(pe.getEmploye());
                duree = pe.getTotalPresence();
                marge = Utilitaire.timeToDouble(pe.getMargeApprouve());
                e.setTpresence(Utilitaire.doubleToHour(duree + marge));
                double d = ((pe.getDateFin().getTime() + pe.getHeureFin().getTime()) - (pe.getDateDebut().getTime() + pe.getHeureDebut().getTime()) - ((pe.getDureePause() != null) ? (pe.getDureePause().getTime() + Constantes.HOUR) : 0)) / 3600000;
                e.setTabsence(((d - (duree + marge)) > 0) ? Utilitaire.doubleToHour(d - (duree + marge)) : "0");
                e.setTypeValidation(UtilGrh.buildBeanTypeValidation(pe.getTypeValidation()));
                e.setPointages(UtilGrh.buildBeanPointage(pe.getPointages()));
                e.setAuteur((pe.getAuthor() != null) ? pe.getAuthor().getUsers().getNomUsers() : "");
                if (employesEnMission.contains(pe.getEmploye())) {
                    e.setRequisit(true);
                    e.setMotifAbsence("En mission");
                }
                if (employesEnFormations.contains(pe.getEmploye())) {
                    e.setRequisit(true);
                    e.setMotifAbsence("En formation");
                }
                if (employesEnConges.contains(pe.getEmploye())) {
                    e.setRequisit(true);
                    e.setMotifAbsence("En congé");
                }
                le.remove(pe.getEmploye());
//                listRecapPresence.add(e);
            }
            if (all) {
                for (YvsGrhEmployes em : le) {
                    PresenceEmploye e = new PresenceEmploye();
                    e.setEmploye(em);
//                    listRecapPresence.add(e);
                }
            }
        } else {
            getErrorMessage("Aucune fiche n'a été trouvé !");
        }
    }

    public void findRecapByEtat() {
        switch (etatF) {
            case 0:
                loadFichePresenceAllEmps();
                break;
            case 1: //les employé absent
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    @Override
    public void resetFiche() {
        setDisableSave(false);
        dateFiche = new Date();
    }

    /**
     * Cas d'une sortie autorisé par un supérieure
     */
    public void validSortie() {
        //construire l'objet métier et l'inserer et enregistrer en précisant le motif EN PAUSE
//        if (pointageSortie.getSuperieur().getMatricule() != null) {
//            //récupère l'employé par son matricule
//            champ = new String[]{"matricule"};
//            val = new Object[]{pointageSortie.getSuperieur().getMatricule()};
//            YvsGrhEmployes emp_ = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByMatricule", champ, val);
//            if (currentUser != null) {
//                //rechercher l'employé lié à un utilisateur  
//                champ = new String[]{"codeUsers", "agence"};
//                val = new Object[]{currentUser.getUsers().getCodeUsers(), currentAgence};
//                YvsGrhEmployes userEmp = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCodeUsers1", champ, val);
//                if (userEmp != null) {
//                    YvsGrhPresence currentFiche = createFicheEmploye('S', userEmp, new Date());
//                    if (currentFiche != null) {
//                        //récupère la dernière ligne de pointage                    
//                        YvsGrhPointage last = giveLastPointage(currentFiche);
//                        if (last != null) {
//                            if (last.getHeureEntree() != null && last.getHeureSortie() == null) {
//                                last.setHeureSortie(new Date());
//                                dao.update(last);
//                            }
//                            //insère une ligne de service pour la commission
//                            YvsGrhPointage p = giveEntityPointage();
//                            p.setHeureEntree(new Date());
//                            p.setHeureSortie(null);
//                            p.setPresence(currentFiche);
//                            p.setMotifSortie(Constantes.GRH_EN_COMMISSION);
//                            p.setDescriptionCommission(pointageSortie.getDescriptionMotif());
//                            p.setDureeCommission(pointageSortie.getSortie());
//                            p.setValider(false);
//                            p.setActif(false);
//                            p.setOperateurEntree(currentUser.getUsers());
//                            p = (YvsGrhPointage) dao.save1(p);
//                            YvsGrhDestDmdeCommission dest = new YvsGrhDestDmdeCommission();
//                            dest.setEmploye(emp_);
//                            dest.setPointage(null);
//                            dest.setPointage(p);
//                            dest.setDescription(pointage.getDescriptionMotif());
//                            dao.save(dest);
//                            succes();
//                        }
//                    }
//                } else {
//                    getErrorMessage("Votre profil n'est rattaché à aucun employé");
//                }
//            }
//        } else {
//            getErrorMessage("Veuillez remplir tous les champs");
//        }
    }

    private void saveRetourComission(YvsGrhPointage p) {
        //compare l'heure de retour avec l'heure prévue
        Date heurePrevu = fabriqueTimeStamp(new Date(), p.getDureeCommission());
        if (heurePrevu.before(new Date())) { //s'il revient avant l'heure prévu
            //positionne la sortie et 
            p.setHeureSortie(new Date());
        } else {
            p.setHeureSortie(heurePrevu);
        }
        p.setOperateurSortie(currentUser.getUsers());
        dao.update(p);
        //insère une entrée à l'heure de présente
        YvsGrhPointage po = giveEntityPointage();
        po.setHeureEntree(new Date());
        po.setHeurePointage(new Date());
        po.setActif(true);
        po.setOperateurEntree(currentUser.getUsers());
        po.setHoraireNormale(pointageValid(p.getPresence().getDateDebut(), p.getPresence().getHeureDebut(), p.getPresence().getDateFin(), p.getPresence().getHeureFin(), new Date(), p.getPresence().getEmploye().getContrat().getCalendrier()));
        po.setValider(pointageValid(p.getPresence().getDateDebut(), p.getPresence().getHeureDebut(), p.getPresence().getDateFin(), p.getPresence().getHeureFin(), new Date(), p.getPresence().getEmploye().getContrat().getCalendrier()));
        po.setPresence(p.getPresence());
        po.setId(null);
        dao.save(po);
        succes();
    }

    private Date fabriqueTimeStamp(Date date, Date heure) {
        Calendar d = Calendar.getInstance();
        Calendar h = Calendar.getInstance();
        d.setTime(date);
        if (heure != null) {
            h.setTime(heure);
            d.set(Calendar.HOUR_OF_DAY, h.get(Calendar.HOUR_OF_DAY));
            d.set(Calendar.MINUTE, h.get(Calendar.MINUTE));
            d.set(Calendar.SECOND, h.get(Calendar.SECOND));
            d.set(Calendar.MILLISECOND, h.get(Calendar.MILLISECOND));
        }
        return d.getTime();
    }

    private boolean in, viewButonPause;

    public boolean isViewButonPause() {
        return viewButonPause;
    }

    public void setViewButonPause(boolean viewButonPause) {
        this.viewButonPause = viewButonPause;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    private YvsGrhEmployes emp;

    public void displayLinkInOut() {
        //l'utilisateur dois être connecté à un employé
        if (currentUser != null) {
            champ = new String[]{"codeUsers", "agence"};
            val = new Object[]{currentUser.getUsers().getCodeUsers(), currentAgence};
            emp = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCodeUsers1", champ, val);
            if (emp != null) {
                //recherche le dernier mouvement de l'employé
                champ = new String[]{"employe", "date"};
                val = new Object[]{new YvsGrhEmployes(emp.getId()), new Date()};
                //contrôle la fiche de présence de l'employé pour cette journée            
                YvsGrhPresence pr = (YvsGrhPresence) dao.loadOneByNameQueries("YvsGrhPresence.findOneFiche", champ, val);
                if (pr != null) {
                    champ = new String[]{"fiche"};
                    val = new Object[]{pr};
                    YvsGrhPointage poi = (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findLastPointage", champ, val);
                    if (poi != null) {
                        //l'employé est en service si poi.sortie==null et poi.entree!=null
                        in = !(poi.getHeureSortie() == null && poi.getHeureEntree() != null);
                        //vérifie si l'enployé à déjà pri sa pause 
                        if (!in) {
                            for (YvsGrhPointage p : pr.getPointages()) {
                                viewButonPause = p.getMotifSortie().equals(Constantes.GRH_EN_PAUSE);
                                if (viewButonPause) {
                                    viewButonPause = false;
                                    break;
                                }
                                viewButonPause = true;
                            }
                        } else {
                            viewButonPause = false;
                        }
                    } else {
                        in = true;
                        viewButonPause = false;
                    }
                } else {
                    in = true;
                    viewButonPause = false;
                }
            } else {
                in = viewButonPause = false;
            }
        }
        //cherche si le jour courant est un jour férié
        String rq = "SELECT j.titre FROM yvs_jours_feries j WHERE ((SELECT EXTRACT ('DAY' FROM j.jour))=?"
                + " AND (SELECT EXTRACT ('MONTH' FROM j.jour))=? AND ((SELECT EXTRACT ('YEAR' FROM j.jour))=? OR j.all_year=true))";
        Calendar c = Calendar.getInstance();
        val = new Object[]{c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)};
        List<String> l = dao.loadDataByNativeQuery(rq, val);
        if (!l.isEmpty()) {
            displayFerie = l.get(0);
        } else {
            displayFerie = null;
        }
    }

    private boolean isFerie(Date date) {
        //cherche si le jour courant est un jour férié
        String rq = "SELECT j.titre FROM yvs_jours_feries j WHERE ((SELECT EXTRACT ('DAY' FROM j.jour))=?"
                + " AND (SELECT EXTRACT ('MONTH' FROM j.jour))=? AND ((SELECT EXTRACT ('YEAR' FROM j.jour))=? OR j.all_year=true))";
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        val = new Object[]{c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)};
        List<String> l = dao.loadDataByNativeQuery(rq, val);
        if (!l.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    ///*** Pointage deduis l'êntete
    public void pointageInOut() {
        if (emp != null) {
            if (in) {
                savePointage('E', new Date(), emp);
                in = false;
            } else {
                savePointage('S', new Date(), emp);
                in = true;
            }
            update("head-top-template");
        }
    }
    /**
     * *
     * TYPE DE VALIDATION DES FICHES DE PRESENCES
     */

    private List<YvsGrhTypeValidation> typesValidation = new ArrayList<>();
    private TypeValidation newTypeV = new TypeValidation(), selectedTypeV;
    private boolean updateTypeV;

    public List<YvsGrhTypeValidation> getTypesValidation() {
        return typesValidation;
    }

    public void setTypesValidation(List<YvsGrhTypeValidation> typesValidation) {
        this.typesValidation = typesValidation;
    }

    public TypeValidation getNewTypeV() {
        return newTypeV;
    }

    public void setNewTypeV(TypeValidation newTypeV) {
        this.newTypeV = newTypeV;
    }

    public TypeValidation getSelectedTypeV() {
        return selectedTypeV;
    }

    public void setSelectedTypeV(TypeValidation selectedTypeV) {
        this.selectedTypeV = selectedTypeV;
    }

    //les types de validations d'une fiche de présence
    public void loadAllTypeValidation() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        typesValidation = dao.loadListTableByNameQueries("YvsGrhTypeValidation.findAll", champ, val);
        droitViewEmploye();
        loadDataNotification();
    }

    public void addNewTypeV() {
        if (!autoriser("point_cr_mode_validation")) {
            openNotAcces();
            return;
        }
        if (newTypeV.getCode() != null && newTypeV.getLibelle() != null) {
            //le code ne doit pas contenir d'espace
            if ((Utilitaire.containsSpace(newTypeV.getCode()))) {
                YvsGrhTypeValidation tv = UtilGrh.buildBeanTypeContrat(newTypeV);
                tv.setSociete(currentAgence.getSociete());
                tv.setAuthor(currentUser);
                tv.setTauxJournee(newTypeV.getTaux());
                tv.setTempsMinimal(newTypeV.getTempsMinimal());
                if (!updateTypeV) {
                    tv.setId(null);
                    tv = (YvsGrhTypeValidation) dao.save1(tv);
                    newTypeV.setId(tv.getId());
                    typesValidation.add(0, tv);
                    currentEmploye.setTypeValidation(newTypeV);
                } else {
                    dao.update(tv);
                    typesValidation.set(typesValidation.indexOf(tv), tv);
                }
                newTypeV = new TypeValidation();
                updateTypeV = false;
                update("grid-valid-tv");
            } else {
                getErrorMessage("Formulaire invalide. le code ne doit pas contenir d'espaces");
            }
        } else {
            getErrorMessage("Formulaire invalide. le code n'est pas indiqué");
        }
    }
//

    public void selectionTypeV(SelectEvent ev) {
        if (ev != null) {
            YvsGrhTypeValidation tv = (YvsGrhTypeValidation) ev.getObject();
            newTypeV = UtilGrh.buildBeanTypeValidation(tv);
            updateTypeV = true;
        }
    }

    public void deleteTypeV(YvsGrhTypeValidation tv) {
        if (!autoriser("point_del_mode_validation")) {
            openNotAcces();
            return;
        }
        try {
            tv.setAuthor(currentUser);
            dao.delete(tv);
            typesValidation.remove(tv);
            update("grid-valid-tv");
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cet élément !");
            log.log(Level.SEVERE, null, ex);
        }
    }

//    public void loadHistoriqueByUser(YvsGrhEmployes emp) {
//        if (debutSearch != null && endSearch != null) {
//            champ = new String[]{"debut", "fin", "employe"};
//            val = new Object[]{debutSearch, endSearch, emp.getMatricule()};
//            List<YvsGrhPresence> lp = dao.loadNameQueries("YvsGrhPresence.findBetweenDateAndEmp", champ, val);
//            listRecapPresence.clear();
//            for (YvsGrhPresence pe : lp) {
//                PresenceEmploye e = new PresenceEmploye();
//                e.setId(pe.getId());
//                e.setEmploye(pe.getEmploye());
//                e.setTpresence(Utilitaire.doubleToHour((pe.getTotalPresence() != null) ? pe.getTotalPresence() : 0));
//                double d = ((pe.getDateFin().getTime() + pe.getHeureFin().getTime()) - (pe.getDateDebut().getTime() + pe.getHeureDebut().getTime())) / 3600000;
//                e.setTabsence((pe.getTotalPresence() != null) ? (Utilitaire.doubleToHour(d - pe.getTotalPresence())) : "0");
//                e.setDebut(pe.getDateDebut());
//                e.setFin(pe.getDateFin());
//                e.setPointages(UtilGrh.buildBeanPointage(pe.getPointages()));
//                listRecapPresence.add(e);
//            }
//            update("part_historique_presence");
//        } else {
//            getErrorMessage("Veiullez entrer un intervalle de date valide !");
//        }
//    }
//    public void callHistorique() {
//        YvsGrhEmployes e = new YvsGrhEmployes();
//        e.setId((long) 0);
//        e.setMatricule(matriculeSearch);
//        loadHistoriqueByUser(e);
//    }
    //*Partie Rapports*/
//    private Date debut = new Date(), fin = new Date();
//    public Date getDebut() {
//        return debut;
//    }
//
//    public void setDebut(Date debut) {
//        this.debut = debut;
//    }
//
//    public void setFin(Date fin) {
//        this.fin = fin;
//    }
//
//    public Date getFin() {
//        return fin;
//    }
//    private void buidDatePeriode(PresenceEmploye p) {
//        Calendar c;
//        BeanDate b;
//        BeanDate.listParamDate.clear();
//        int i = 0;
//        while (debut.before(fin)) {
//            c = Utilitaire.getIniTializeDate(debut);
//            b = new BeanDate(1, c.getTime(), Utilitaire.getDay(c));
//            BeanDate.listParamDate.add(b);
//            c.add(Calendar.DAY_OF_MONTH, 1);
//            debut = c.getTime();
//        }
//    }
    public String callReportPresencePeriode() {
        if (employeRecapPeriod != null ? employeRecapPeriod.getId() < 1 : true) {
            employeRecapPeriod = currentEmploye.getEmploye();
        }
        if (employeRecapPeriod != null) {
            Map param_ = new HashMap();
            param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
            param_.put("AGENCE", currentUser.getAgence().getId().intValue());
            param_.put("DATEDEBUT", debutRecapPeriod);
            param_.put("DATEFIN", finRecapPeriod);
            param_.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param_.put("EMPLOYE", employeRecapPeriod.getId().intValue());
            param_.put("TITLE", "RAPPORT DE PRESENCE PERIODIQUE");
            param_.put("LOGO", returnLogo());
//            param_.put("IMG_SIEGE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "siege.png"));
//            param_.put("IMG_PHONE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "phone.png"));            
            executeReport("recap_presence_emps", param_);
        }
        return null;
    }

    public void callReportPresenceHebdo() {
        Map param_ = new HashMap();
        if (employeRecapHebdo != null ? employeRecapHebdo.getId() < 1 : true) {
            employeRecapHebdo = currentEmploye.getEmploye();
        }
        if (employeRecapHebdo != null) {
            param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
            param_.put("employe", employeRecapHebdo.getId());
            param_.put("debut", debutRecapHebdo);
            param_.put("fin", finRecapHebdo);
            executeReport("feuille_presence", param_, employeRecapHebdo.getMatricule());
        }
    }

    public void loadPresenceEmploye() {
        loadDateRecap(currentEmploye.getDebut());
        employeRecapPeriod = currentEmploye.getEmploye();
        getPresenceEmploye();
    }

    public void loadPresenceEmploye(YvsGrhPresence pe) {
        loadDateRecap(pe.getDateDebut());
        employeRecapPeriod = pe.getEmploye();
        getPresenceEmploye();
    }

    public void getPresenceEmploye() {
        ManagedTableauBord s = (ManagedTableauBord) giveManagedBean(ManagedTableauBord.class);
        if (s != null) {
            s.loadPresenceEmploye(employeRecapPeriod.getId(), debutRecapPeriod, finRecapPeriod);
        }
    }

    public void loadRecapHebdoEmploye() {
        loadDateHebdo(currentEmploye.getDebut());
        employeRecapHebdo = currentEmploye.getEmploye();
        getRecapHebdoEmploye();
    }

    public void loadRecapHebdoEmploye(YvsGrhPresence pe) {
        loadDateHebdo(pe.getDateDebut());
        employeRecapHebdo = pe.getEmploye();
        getRecapHebdoEmploye();
    }

    public void getRecapHebdoEmploye() {
        ManagedTableauBord s = (ManagedTableauBord) giveManagedBean(ManagedTableauBord.class);
        if (s != null) {
            s.loadRecapHebdoEmploye(employeRecapHebdo.getId(), debutRecapHebdo, finRecapHebdo);
        }
    }

    private void loadDateRecap(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        debutRecapPeriod = c.getTime();
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        finRecapPeriod = c.getTime();
    }

    private void loadDateHebdo(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        debutRecapHebdo = c.getTime();
        c.add(Calendar.DAY_OF_MONTH, 6);
        finRecapHebdo = c.getTime();
    }

    private String codeEmps;
    private int optionSearch = 2;

    public String getCodeEmps() {
        return codeEmps;
    }

    public void setCodeEmps(String codeEmps) {
        this.codeEmps = codeEmps;
    }

    private boolean extratHour(Date d, Date debut, Date fin) {
        Date dt = extratDate(d);
        long binf = dt.getTime() + debut.getTime();
        long bSup = dt.getTime() + fin.getTime();
        long time_ = d.getTime();
        return (time_ > binf && time_ <= bSup);
    }

    private Date extratDate(Date dt) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public void updateFichePresence() {
        if (!autoriser("point_update_fiche_presence")) {
            openNotAcces();
            return;
        }
        if (currentEmploye.getId() > 0) {
            if (!currentEmploye.isValider()) {
                if (currentEmploye.getId() == entityPresence.getId()) {
                    if (fabriqueTimeStamp(currentEmploye.getHeureDebut(), currentEmploye.getHeureDebut()).getTime() < fabriqueTimeStamp(currentEmploye.getFin(), currentEmploye.getHeureFin()).getTime()) {
                        entityPresence.setDateDebut(currentEmploye.getDebut());
                        entityPresence.setDateFin(currentEmploye.getFin());
                        entityPresence.setHeureFin(currentEmploye.getHeureFin());
                        entityPresence.setHeureDebut(currentEmploye.getHeureDebut());
                        entityPresence.setAuthor(currentUser);
                        entityPresence.setDateUpdate(new Date());
                        dao.update(entityPresence);
                    } else {
                        getErrorMessage("Erreur de cohérence des dates !");
                    }
                } else {
                    getErrorMessage("Impossible de modifier !", " vous devez selectionner une fiche");
                }
            } else {
                getErrorMessage("Impossible de modifier", "Cette fiche a déjà été validé !");
            }
        } else {
            getErrorMessage("Impossible de modifier", "Cette fiche a déjà été validé !");
        }
    }

    public void deleteFichePresence() {
        if (!autoriser("point_delete_fiche_presence")) {
            openNotAcces();
            return;
        }
        //la fiche ne doit pas être validé
        if (currentEmploye.getId() > 0 && !currentEmploye.isValider()) {
            if (currentEmploye.getId() == entityPresence.getId()) {
                entityPresence.setAuthor(currentUser);
                dao.delete(entityPresence);
                //supprime également toute les fiches qui ont la même date de début
                List<YvsGrhPresence> l = dao.loadNameQueries("YvsGrhPresence.findByDateAndEmpB", new String[]{"debut", "fin", "employe"}, new Object[]{entityPresence.getDateDebut(), entityPresence.getDateDebut(), entityPresence.getEmploye()});
                for (YvsGrhPresence p : l) {
                    p.setAuthor(currentUser);
                    dao.delete(p);
                }
                YvsGrhEmployes e = currentEmploye.getEmploye();
                currentEmploye = new PresenceEmploye();
                currentEmploye.setEmploye(e);
                succes();
                update("AllPointage-tab1-e");
            } else {
                getErrorMessage("erreur sur le formulaire !");
            }
        } else {
            getErrorMessage("Vous ne pouvez supprimer cette fiche ", "elle est déjà validé ou aucun employé n'a été selectionné!");
        }
    }

    private YvsGrhPresence ficheToDel;

    public void openDeleteOtherFicheP(YvsGrhPresence p) {
        openDialog("dlgConfirmDelF");
        ficheToDel = p;
    }

    public void deleteOtherFicheP() {
        if (!autoriser("point_delete_fiche_presence")) {
            openNotAcces();
            return;
        }
        if (ficheToDel != null) {
            try {
                dao.delete(ficheToDel);
                fichesPresences.remove(ficheToDel);
                ficheToDel = null;
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cette fiche !");
            }
        }
    }

    @Override
    public void onSelectObject(YvsGrhPresence p) {
        selectPresence = p;
        if (p != null) {
            dateFiche = p.getDateDebut();
            pointageM.setDateDebut(dateFiche);
            loadDataPresenceEmploye(p);
            update("AllgetPointageform");
        }
    }

    public void loadDataFicheP(SelectEvent ev) {
        if (ev != null) {
            YvsGrhPresence p = (YvsGrhPresence) ev.getObject();
            onSelectObject(p);
        }
    }

    @Override
    public void updateBean() {
    }

    @Override
    public PointageEmploye recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(PointageEmploye bean) {
    }

    @Override
    public void loadOnView(SelectEvent ev) {

    }

    @Override
    public void onSelectDistant(YvsGrhPresence y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Présences", "modRh", "smenPresence", true);
            }
        }
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*Recherche dans le recapitulatif*/
    private long modeValidF;
    private int etatF;   //0=present 1=Absent  2=tout
    private String codeEmployeF;

    public long getModeValidF() {
        return modeValidF;
    }

    public void setModeValidF(long modeValidF) {
        this.modeValidF = modeValidF;
    }

    public int getEtatF() {
        return etatF;
    }

    public void setEtatF(int etatF) {
        this.etatF = etatF;
    }

    public String getCodeEmployeF() {
        return codeEmployeF;
    }

    public void setCodeEmployeF(String codeEmployeF) {
        this.codeEmployeF = codeEmployeF;
    }

    public void loadAllTrancheHoraire() {
        tranchesHoraire = dao.loadNameQueries("YvsGrhTrancheHoraire.findAllActif", new String[]{"societe"}, new Object[]{currentUser.getAgence().getSociete()});
    }

    public void applyNewTypeTranche(SelectEvent ev) {
        if (ev != null) {
            if (currentEmploye.getEmploye().getId() > 0) {
                if (currentEmploye.getEmploye().getContrat() != null) {
                    YvsGrhContratEmps ce = (YvsGrhContratEmps) dao.loadOneByNameQueries("YvsGrhContratEmps.findById", new String[]{"id"}, new Object[]{currentEmploye.getEmploye().getContrat().getId()});
                    if (ce != null) {
                        ce.setTypeTranche(((YvsGrhTrancheHoraire) ev.getObject()).getTypeJournee());
                        ce.setAuthor(currentUser);
                        currentEmploye.getEmploye().getContrat().setTypeTranche(ce.getTypeTranche());
                        dao.update(ce);
                        succes();
                    }
                } else {
                    getErrorMessage("Aucun contrat n'a été trouvé pour cet employé !");
                }
            } else {
                getErrorMessage("Aucun employé selectionné !");
            }
        }
    }

    public void openFicheEmploye() {
        if (currentEmploye.getEmploye() != null) {
            //calcule la période
            Calendar c = Calendar.getInstance();
            c.setTime(dateFiche);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            debutFiche = c.getTime();
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            finFiche = c.getTime();
            openAllFicheEmps();
            openDialog("dlgFicheP");

        } else {
            getErrorMessage("Aucun_ employé selectionné !");
        }
    }

    public void openAllFicheEmps() {
        fichesPresences = dao.loadNameQueries("YvsGrhPresence.findByDateAndEmpB", new String[]{"employe", "debut", "fin"}, new Object[]{currentEmploye.getEmploye(), debutFiche, finFiche});
    }

    public String callReportPresenceHebdo_() {
        Calendar c = Utilitaire.getIniTializeDate(dateFiche);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        callReportPresenceHebdo(currentEmploye, c.getTime());
        return null;
    }

    private void callReportPresenceHebdo(PresenceEmploye p, Date date) {
        Map param_ = new HashMap();
        param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report") + FILE_SEPARATOR);
        param_.put("employe", p.getEmploye().getId());
        Calendar c = Utilitaire.getIniTializeDate(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        param_.put("debut", c.getTime());
        c.add(Calendar.DAY_OF_MONTH, 6);
        param_.put("fin", c.getTime());
        executeReport("feuille_presence", param_, p.getEmploye().getMatricule());
    }

    public String callRecapPresencePeriode() {
        if (!autoriser("point_print_rappport_periodique")) {
            openNotAcces();
            return null;
        }
        if (agenceFilter < 1) {
            getErrorMessage("Vous devez selectionner une agence");
            return null;
        }
        Map param_ = new HashMap();
        param_.put("AGENCE", agenceFilter);
        param_.put("DEBUT", debutFiche);
        param_.put("FIN", finFiche);
        param_.put("AUTEUR", currentUser.getUsers().getNomUsers().concat(" "));
        param_.put("TITRE", "RAPPORT DE PRESENCE PERIODIQUE");
        param_.put("ALL", (modeValidF == -1 || modeValidF == 0));
        param_.put("TYPE_VALIDATION", modeValidF);
        param_.put("IMG_LOGO", returnLogo());
        param_.put("IMG_SIEGE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "siege.png"));
        param_.put("IMG_PHONE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "phone.png"));
        param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report" + FILE_SEPARATOR + ((true) ? "full" : "semi")) + FILE_SEPARATOR);
        executeReport("presence_periodique", param_, "");
        return null;
    }

    public String exportRecapJournalier() {
        if (!autoriser("point_print_rappport_periodique")) {
            openNotAcces();
            return null;
        }
        if (agenceFilter < 1) {
            getErrorMessage("Vous devez selectionner une agence");
            return null;
        }
        Map param_ = new HashMap();
        param_.put("AGENCE", (int) agenceFilter);
        param_.put("SERVICE", idDepartementFiter);
        param_.put("MATRICULE", codeEmployeF);
        param_.put("SOCIETE", currentAgence.getSociete().getId());
        param_.put("DEBUT", debutFiche);
        param_.put("FIN", finFiche);
        param_.put("AUTEUR", currentUser.getUsers().getNomUsers().concat(" "));
        param_.put("TITRE", "RAPPORT DE PRESENCE PERIODIQUE");
        param_.put("IMG_LOGO", returnLogo());
        param_.put("IMG_SIEGE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "siege.png"));
        param_.put("IMG_PHONE", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "resources" + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + "phone.png"));
        param_.put("SUBREPORT_DIR", FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report" + FILE_SEPARATOR + ((true) ? "full" : "semi")) + FILE_SEPARATOR);
        executeReport("recap_presence_mois", param_, "");
        return null;
    }

    public String exportRecapPresenceDetaille() {
        if (!autoriser("point_print_rappport_periodique")) {
            openNotAcces();
            return null;
        }
        if (agenceFilter < 1) {
            getErrorMessage("Vous devez selectionner une agence");
            return null;
        }
        Map param_ = new HashMap();
        param_.put("ID_AGENCE", (int) agenceFilter);
        param_.put("ID_SERVICE", (int) idDepartementFiter);
        param_.put("ID_EMPLOYE", (int) 0);
        param_.put("SOCIETE", currentAgence.getSociete().getId().intValue());
        param_.put("DATE_DEBUT", debutFiche);
        param_.put("DATE_FIN", finFiche);
        executeReport("recapitulatif_presence", param_, "");
        return null;
    }

    public String giveTime(double d) {
        return Utilitaire.doubleToHour(d);
    }
    /*rechercher et charger les fiches de présences*/
    private boolean paramaDate, addDate;
    private boolean paramMaintenance;
    private boolean initForm = true;
    private List<Long> listExlusion = new ArrayList<>();
    private List<Long> listExlusion1 = new ArrayList<>();   //exclu les employés en missions ou en congé

    public boolean isParamaDate() {
        return paramaDate;
    }

    public void setParamaDate(boolean paramaDate) {
        this.paramaDate = paramaDate;
    }

    public boolean isParamMaintenance() {
        return paramMaintenance;
    }

    public void setParamMaintenance(boolean paramMaintenance) {
        this.paramMaintenance = paramMaintenance;
    }

    public void setListExlusion(List<Long> listExlusion) {
        this.listExlusion = listExlusion;
    }

    public List<Long> getListExlusion() {
        return listExlusion;
    }

    int numroDroit;

    public void droitViewEmploye() {
        if (autoriser("point_validFicheAllScte")) {
            numroDroit = 0;
        } else if (autoriser("point_validFicheAgence")) {
            numroDroit = 1;
        } else if (autoriser("point_validFicheDepartement")) {
            numroDroit = 2;
        } else if (autoriser("point_validFicheEquipe")) {
            numroDroit = 3;
        } else {
            numroDroit = 4;
        }
    }

    public void loadRecapByTypeValidation(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.presence.typeValidation", "typeValidation", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long idMode = (long) ev.getNewValue();
            if (idMode > 0) {
                p.setObjet(new YvsGrhTypeValidation(idMode));
            } else if (idMode == -2) {
                p.setOperation("IS NULL");
                p.setObjet("XXX");
            }
        }
        paginator.addParam(p);
        initForm = true;
        addParamNotifation(false);
        loadAllfichePresence(true);
    }

    private void addParamDroitView() {
        droitViewEmploye();
        ParametreRequete p;
        switch (numroDroit) {
            case 0: //société
                p = new ParametreRequete("y.presence.employe.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 1: //agence
                p = new ParametreRequete("y.presence.employe.agence", "agence", currentUser.getAgence(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //Département
                if (currentUser.getUsers().getEmploye() != null) {
                    if (currentUser.getUsers().getEmploye().getPosteActif() != null) {
                        listIdSubDepartement.clear();
                        giveAllSupDepartement(currentUser.getUsers().getEmploye().getPosteActif().getDepartement());
                        if (!listIdSubDepartement.isEmpty()) {
                            p = new ParametreRequete("y.presence.employe.posteActif.departement.id", "departements", listIdSubDepartement, " IN ", "AND");
                            paginator.addParam(p);
                        }
                    } else {
                        getErrorMessage("Votre profil employé n'est pas rattaché à un poste !");
                    }
                } else {
                    getErrorMessage("Aucun profil employé n'est rattaché à ce compte");
                }
                break;
            case 3: //Equipe
                if (currentUser.getUsers().getEmploye() != null) {
                    p = new ParametreRequete("y.presence.employe.equipe", "equipe", currentUser.getUsers().getEmploye().getEquipe(), "=", "AND");
                    paginator.addParam(p);

                } else {
                    getErrorMessage("Aucun Département n'est spécifié pour l'employé de ce code utilisateur !");
                }
                break;
            default: //Moi seul
                p = new ParametreRequete("y.presence.employe.codeUsers", "users", currentUser.getUsers(), "=", "AND");
                paginator.addParam(p);
                break;
        }
    }

    public void findByEmploye(String str) {
        ParametreRequete p = new ParametreRequete(null, "employe", "XXX", " LIKE ", "AND");
        if ((str != null) ? !str.isEmpty() : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.presence.employe.matricule)", "matricule", "%" + str.trim().toUpperCase() + "%", " LIKE ", " OR "));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.presence.employe.nom)", "nom", "%" + str.trim().toUpperCase() + "%", " LIKE ", " OR "));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.presence.employe.prenom)", "prenom", "%" + str.trim().toUpperCase() + "%", " LIKE ", " OR "));
            p.getOtherExpression().add(new ParametreRequete("UPPER(CONCAT(y.presence.employe.nom,'',y.presence.employe.prenom))", "nom_prenom", "%" + str.trim().toUpperCase() + "%", " LIKE ", " OR "));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        initForm = true;
        addParamNotifation(false);
        loadAllfichePresence(true);
    }

    public void loadAllfichePresence(boolean avancer) {
        addParamDroitView();
        addParamExlude();
        fichesPresences = paginator.executeDynamicQuery("DISTINCT y.presence", "DISTINCT y.presence", "YvsGrhPointage y", "y.presence.dateDebut DESC", avancer, initForm, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getImax())) {
            setOffset(0);
        }
        List<YvsGrhPresence> re = paginator.parcoursDynamicData("YvsGrhPointage", "DISTINCT y.presence", "y", "y.presence.dateDebut DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void pagineData(boolean next) {
        initForm = false;
        loadAllfichePresence(next);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllfichePresence(true);
    }

    // Choosepaginator pour les articles actifs
    public void _choosePaginator(ValueChangeEvent ev) {
        if (ev != null) {
            long v = (long) ev.getNewValue();
            imax = v;
        }
        initForm = true;
        loadAllfichePresence(true);
    }

    private void addParamDate(Boolean date) {
        ParametreRequete p = new ParametreRequete("y.presence.dateDebut", "periode", null, "BETWEEN ", "AND");
        if (date) {
            if (debutFiche != null && finFiche != null) {
                if (debutFiche.before(finFiche) || debutFiche.equals(finFiche)) {
                    p.setObjet(debutFiche);
                    p.setOtherObjet(finFiche);
                }
            }
        }
        paginator.addParam(p);
        initForm = true;
        addParamNotifation(false);
        loadAllfichePresence(true);
    }

    public void addParamAgence(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.presence.employe.agence", "agence", null, "= ", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            Long id = (Long) ev.getNewValue();
            p.setObjet(id > 0 ? new YvsAgences(id) : null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllfichePresence(true);
    }

    public void addParamDepartement(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.presence.employe.posteActif.departement", "service", null, "= ", "AND");
        if (ev != null ? ev.getNewValue() != null : false) {
            Long id = (Long) ev.getNewValue();
            p.setObjet(id > 0 ? new YvsGrhDepartement(id.intValue()) : null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllfichePresence(true);
    }

    public void addParamDate(ValueChangeEvent ev) {
        paramaDate = (Boolean) ev.getNewValue();
        addParamDate(paramaDate);
    }

    public void selectParamDate(SelectEvent ev) {
        if (ev != null) {
            addParamDate(true);
        }
    }

    private void loadDataNotification() {
        addParamNotifation(true);
    }

    public void addParamNotification() {
        paginator.getParams().clear();
        addParamNotifation(true);
        initForm = true;
        loadAllfichePresence(true);
    }

    public void addParamNotifation(boolean addParam) {
        ParametreRequete p = new ParametreRequete("y.presence.id", "idsNotif", null, " IN ", "AND");
        if (addParam) {
            //récupère les ids des lignes qui concerne une notification
            List<Long> l = dao.loadNameQueries("YvsGrhPresence.findFicheToNotif", new String[]{"user", "societe"}, new Object[]{currentUser.getUsers(), currentAgence.getSociete()});
            if (!l.isEmpty()) {
                p.setObjet(l);
            } else {
                p.setObjet(null);
                return;
            }
            countNotif = l.size();
        }
        paginator.addParam(p);
    }

    public void filterMaintenance(ValueChangeEvent ev) {
        paramMaintenance = (boolean) ev.getNewValue();
        ParametreRequete p = new ParametreRequete(null, "filtre", "XXX", " LIKE ", "AND");
        ParametreRequete pp = new ParametreRequete(null, "filtre", "XXX", " LIKE ", "AND");
        ParametreRequete p0 = new ParametreRequete("y.presence.totalHeureSup", "heureSup", 0, "=", "AND");
        if ((boolean) ev.getNewValue()) {
            pp.getOtherExpression().add(new ParametreRequete("y.heureEntree", "heureEntree", null, " IS NULL ", "OR"));
            pp.getOtherExpression().add(new ParametreRequete("y.heureSortie", "heureSortie", null, " IS NULL ", "OR"));
            pp.getOtherExpression().add(new ParametreRequete("y.presence.totalPresence", "totalP", 5, "<", "OR"));
            pp.getOtherExpression().add(new ParametreRequete("y.presence.totalPresence", "totalP1", 12.5, ">", "OR"));
            p.getOtherExpression().add(pp);
            p.getOtherExpression().add(p0);
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        addParamNotifation(false);
        initForm = true;
        loadAllfichePresence(true);
    }

    private void addParamExlude() {
        ParametreRequete p1 = new ParametreRequete("y.presence.id", "idExclu", null, "NOT IN ", "AND");
        if (!listExlusion.isEmpty() && paramMaintenance) {
            p1.setObjet(listExlusion);
        }
        paginator.addParam(p1);
    }

    public void toogleParamDuree(ValueChangeEvent ev) {
        if (ev != null) {
            paramDuree = (Boolean) ev.getNewValue();
            if (!paramDuree) {
                addParamDuree();
            }
        }
    }

    public void addParamDuree() {
        String oper = "";
        switch (operateurDuree) {
            case "gt":
                oper = ">";
                break;
            case "lt":
                oper = "<";
                break;
            default:
                oper = "=";
                break;
        }
        ParametreRequete p = new ParametreRequete("y.presence.totalPresence", "totalPresence", searchDuree, oper, "AND");
        if (!paramDuree) {
            p.setObjet(null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllfichePresence(true);

    }

    public void addParamHeureDebutFiche(TimeSelectEvent ev) {
        Date heure = ev.getTime();
        ParametreRequete p = new ParametreRequete("y.presence.heureDebut", "heureDebut", heure, "=", "AND");
        paginator.addParam(p);
        initForm = true;
        loadAllfichePresence(true);
    }

    public void addParamHeureFinFiche(TimeSelectEvent ev) {
        Date heure = ev.getTime();
        ParametreRequete p = new ParametreRequete("y.presence.heureFin", "heureFin", heure, "=", "AND");
        paginator.addParam(p);
        initForm = true;
        loadAllfichePresence(true);
    }

    public void excludeElement(YvsGrhPresence p) {
        if (p != null) {
            listExlusion.add(p.getId());
        }
        fichesPresences.remove(p);
    }

    /*Gérer l'insertion des pointages*/
    private YvsGrhPresence findFichesPointage() {
        YvsGrhPresence p = (YvsGrhPresence) dao.loadOneByNameQueries("YvsGrhPresence.findOneFiche", new String[]{"date", "employe"}, new Object[]{dateFiche, currentEmploye.getEmploye()});
        if (p == null) {
            //trouve le planing
            YvsGrhPlanningEmploye pl = (YvsGrhPlanningEmploye) dao.loadOneByNameQueries("YvsGrhPlanningEmploye.findByOneEmploye", new String[]{"date", "employe"}, new Object[]{dateFiche, currentEmploye.getEmploye()});
            if (pl != null) {
                //fabrique la fiche et save là
                p = buildFichePresence(pl.getDateDebut(), pl.getTranche().getHeureDebut(), pl.getDateFin(), pl.getTranche().getHeureFin(), pl.getEmploye());
                p.setId(null);
                p = (YvsGrhPresence) dao.save1(p);
            } else {
                if (currentEmploye.getEmploye().getHoraireDynamique()) {
                    //ouvre la boite de dialogue pour choisir la tranche
                    return null;
                } else {
                    //utilisa le calendrier de l'employé pour construre la fiche
                    YvsJoursOuvres j = findJourOuvre(currentEmploye.getEmploye().getContrat(), dateFiche);
                    if (j != null) {
                        p = buildFichePresence(dateFiche, j.getHeureDebutTravail(), dateFiche, j.getHeureFinTravail(), currentEmploye.getEmploye());
                        p.setId(null);
                        p = (YvsGrhPresence) dao.save1(p);
                    } else {
                        return null;
                    }
                }
            }
        }
        return p;
    }

    //construit les pointages de la fiche
    private List<PointageEmploye> giveAllPointage(YvsGrhPresence pr) {
        List<PointageEmploye> re = new ArrayList<>();
        if (pr != null) {
            PointageEmploye pp;
            for (YvsGrhPointage p : pr.getPointages()) {
                if (p.getHeureEntree() != null) {
                    pp = UtilGrh.buildSimpleBeanPointage(p);
                    pp.setHeure(p.getHeureEntree());
                    pp.setPointeuse(p.getPointeuseIn());
                    re.add(pp);
                }
                if (p.getHeureSortie() != null) {
                    pp = UtilGrh.buildSimpleBeanPointage(p);
                    pp.setHeure(p.getHeureSortie());
                    pp.setPointeuse(p.getPointeuseOut());
                    re.add(pp);
                }
            }
            return re;
        }
        return null;
    }

    //construit les pointages de la fiche
    private List<PointageEmploye> giveReelPointage(YvsGrhPresence pr) {
        List<PointageEmploye> re = new ArrayList<>();
        if (pr != null) {
            PointageEmploye pp;
            for (YvsGrhPointage p : pr.getPointages()) {
                if (p.getHeureEntree() != null && !p.getSystemIn()) {
                    pp = UtilGrh.buildSimpleBeanPointage(p);
                    pp.setHeure(p.getHeureEntree());
                    pp.setPointeuse(p.getPointeuseIn());
                    re.add(pp);
                }
                if (p.getHeureSortie() != null && !p.getSystemOut()) {
                    pp = UtilGrh.buildSimpleBeanPointage(p);
                    pp.setHeure(p.getHeureSortie());
                    pp.setPointeuse(p.getPointeuseOut());
                    re.add(pp);
                }
            }
            return re;
        }
        return null;
    }

    private YvsGrhPresence buildFichePresence(Date debut, Date heureD, Date fin, Date heureF, YvsGrhEmployes e) {
        YvsGrhPresence pr = new YvsGrhPresence();
        pr.setAuthor(currentUser);
        pr.setDateDebut(debut);
        pr.setDateFin(fin);
        pr.setEmploye(e);
        pr.setHeureDebut(heureD);
        pr.setHeureFin(heureF);
        pr.setValider(false);
        return pr;
    }

    private YvsGrhPointage buildPointage(PointageEmploye p) {
        YvsGrhPointage po = new YvsGrhPointage(p.getId());
        po.setActif(true);
        po.setAuthor(currentUser);
        po.setCompensationHeure(p.isHeureCompensation());
        po.setDateSaveEntree(p.getHeure());
        po.setDateSaveSortie(p.getHeure());
        po.setHeureEntree(p.getHeure());
        po.setHeurePointage(p.getHeure());
        po.setHeureSortie(p.getHeure());
        po.setHeureSupplementaire(p.isHeureSupp());
        po.setMotifSortie(p.getMotif());
        if (p.getOperateurIn().getId() > 0) {
            po.setOperateurEntree(new YvsUsers(p.getOperateurIn().getId()));
        }
        if (p.getOperateurOut().getId() > 0) {
            po.setOperateurSortie(new YvsUsers(p.getOperateurOut().getId()));
        }
        po.setPointageAutomatique(p.isPointageAuto());
        return po;
    }

    public void insertNewPointage(Date time, YvsGrhPresence p) {
        //vérifie le dernier pointage de la fiche                
        try {
            if (p != null) {
                IYvsGrhPointage i_pointage = (IYvsGrhPointage) IEntitiSax.createInstance("IYvsGrhPointage", dao);
                if (i_pointage != null) {
                    List<PointageEmploye> re = giveAllPointage(p);
                    if (re != null) {
                        IYvsJoursOuvres i_jour = (IYvsJoursOuvres) IEntitiSax.createInstance("IYvsJoursOuvres", dao);
                        if (i_jour != null) {
                            Date date = timestampToDate(p.getDateDebut());
                            YvsJoursOuvres jour = i_jour.getJour(p.getEmploye(), date);
                            if (jour != null ? jour.getId() > 0 : false) {
                                p.setSupplementaire(paramGrh.getLimitHeureSup() > 0 ? (!jour.getOuvrable() || jour.getJourDeRepos()) : false);
                            }
                        }
                        if (!p.isSupplementaire()) {
                            Date date = timestampToDate(p.getDateDebut());
                            YvsJoursFeries jour = (YvsJoursFeries) dao.loadOneByNameQueries("YvsJoursFeries.findByJour", new String[]{"societe", "jour"}, new Object[]{currentAgence.getSociete(), date});
                            if (jour != null ? jour.getId() > 0 : false) {
                                p.setSupplementaire(true);
                            }
                        }
                        PointageEmploye po = new PointageEmploye();
                        po.setHeure(time);
                        po.setHoraireNormale(paramaDate);
                        po.setId(-1);
                        po.setLineTransient(true);
                        po.setOperateurIn(UtilUsers.buildBeanUsers(currentUser.getUsers()));
                        po.setOperateurOut(UtilUsers.buildBeanUsers(currentUser.getUsers()));
                        po.setPointageAuto(false);
                        po.setValider(false);
                        po.setPointeuse(null);
                        re.add(po);
                        Collections.sort(re, new PointageEmploye());
                        String query = "DELETE FROM yvs_grh_pointage WHERE presence = ?";
                        dao.requeteLibre(query, new Options[]{new Options(p.getId(), 1)});
                        p.getPointages().clear();
                        for (PointageEmploye pe : re) {
                            i_pointage.onSavePointage(p, pe.getHeure(), pe.getPointeuse(), 0, currentUser);
                        }
                        succes();
                    }
                }

//            List<PointageEmploye> re = giveAllPointage(p);
//            
//            if (re != null) {
//                PointageEmploye po = new PointageEmploye();
//                po.setHeure(time);
//                po.setHoraireNormale(paramaDate);
//                po.setId(-1);
//                po.setLineTransient(true);
//                po.setOperateurIn(UtilUsers.buildBeanUsers(currentUser.getUsers()));
//                po.setOperateurOut(UtilUsers.buildBeanUsers(currentUser.getUsers()));
//                po.setPointageAuto(false);
//                po.setValider(false);
//                re.add(po);
//                Collections.sort(re, new PointageEmploye());
//                // insert fiche
//                p.getPointages().clear();
//                YvsGrhPointage p_;
//                for (PointageEmploye pe : re) {
//                    int size = p.getPointages().size();
//                    size = (size == 0) ? 0 : (size - 1);
//                    if (p.getPointages().isEmpty()) {
//                        //insert une nvelle ligne
//                        p_ = buildPointage(pe);
//                        p_.setHoraireNormale(pointageValid(p.getDateDebut(), p.getHeureDebut(), p.getDateFin(), p.getHeureFin(), pe.getHeure(), p.getEmploye().getContrat().getCalendrier()));
//                        p_.setValider(p_.getHoraireNormale());
//                        p_.setHeureSortie(null);
//                        p_.setCommission(false);
//                        p.getPointages().add(p_);
//                    } else {
//                        if ((size == 0 && p.getPointages().get(size).getHeureEntree() == null) || (p.getPointages().get(size).getHeureEntree() != null && p.getPointages().get(size).getHeureSortie() != null)) {
//                            //insert une nvelle ligne
//                            p_ = buildPointage(pe);
//                            p_.setHoraireNormale(pointageValid(p.getDateDebut(), p.getHeureDebut(), p.getDateFin(), p.getHeureFin(), pe.getHeure(), p.getEmploye().getContrat().getCalendrier()));
//                            p_.setValider(p_.getHoraireNormale());
//                            p_.setCommission(false);
//                            p_.setHeureSortie(null);
//                            if (p.getPointages().contains(p_)) {
//                                p_.setId(-idLine++);
//                            }
//                            p.getPointages().add(p_);
//                        } else {
//                            if (p.getPointages().get(size).getHeureSortie() == null) {
//                                p.getPointages().get(size).setHeureSortie(pe.getHeure());
//                            }
//                        }
//                    }
//                }
//                //enregistre
//                for (YvsGrhPointage po_ : p.getPointages()) {
//                    po_.setPresence(p);
//                    if (po_.getId() != null ? po_.getId() <= 0 : false) {
//                        po_.setId(null);
//                    }
//                    po_ = (YvsGrhPointage) dao.update(po_);
//                }
//            } else {
//                getErrorMessage("Aucune fiche n'a été trouvé !");
//            }
            } else {
                getErrorMessage("Aucune fiche n'a été générer ce jour !");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            getException("insertNewPointage", ex);
        }
    }

    private void insertPointage(Date heure) {
        YvsGrhPresence p;
        if (currentEmploye.getId() <= 0) {
            p = findFichesPointage();
            if (p != null) {
                currentEmploye.setId(p.getId());
            } else {
                currentEmploye.setId(-1);
                if (currentEmploye.getEmploye().getContrat() != null) {
                    //ouvre la boite de selection de la tranche
                    createTranche = true;
                    openDialog("dlgTrancheHoraire_");
//                    closeDialog("dlgvalidpointage");
                } else {
                    getErrorMessage("L'employé selectionné ne dispose pas de contrat !");
                }
            }
        } else {
            //s'il y a déjà une fiche selectionné, on insère juste le pointage
            p = (YvsGrhPresence) dao.loadOneByNameQueries("YvsGrhPresence.findById", new String[]{"id"}, new Object[]{currentEmploye.getId()});
            p.setHeureDebut(Util.buildTimeStamp(p.getDateDebut(), p.getHeureDebut(), false, false));
            p.setHeureFin(Util.buildTimeStamp(p.getDateFin(), p.getHeureFin(), false, false));
            insertNewPointage(heure, p);
        }
    }

    public void selectTrancheAndCreateFiche(SelectEvent ev) {
        YvsGrhTrancheHoraire t = (YvsGrhTrancheHoraire) ev.getObject();
        if (createTranche) {
            Date dateFin = dateFiche;
            if (t.getHeureFin().before(t.getHeureDebut())) {
                dateFin = yvs.dao.salaire.service.Constantes.givePrevOrNextDate(dateFiche, 1);
            }
            YvsGrhPresence pr = buildFichePresence(dateFiche, t.getHeureDebut(), dateFin, t.getHeureFin(), currentEmploye.getEmploye());
            pr.setId(null);
            pr = (YvsGrhPresence) dao.save1(pr);
            currentEmploye.setId(pr.getId());
            currentEmploye = buildPresence(pr);
        } else {
            if (selectPresence != null) {
                Date dateFin = selectPresence.getDateDebut();
                if (t.getHeureFin().before(t.getHeureDebut())) {
                    dateFin = yvs.dao.salaire.service.Constantes.givePrevOrNextDate(selectPresence.getDateDebut(), 1);
                }
                selectPresence.setHeureDebut(t.getHeureDebut());
                selectPresence.setHeureFin(t.getHeureFin());
                selectPresence.setDateFin(dateFin);
                dao.update(selectPresence);
                IYvsJoursOuvres i_jour = (IYvsJoursOuvres) IEntitiSax.createInstance("IYvsJoursOuvres", dao);
                if (i_jour != null) {
                    Date date = timestampToDate(selectPresence.getDateDebut());
                    YvsJoursOuvres jour = i_jour.getJour(selectPresence.getEmploye(), date);
                    if (jour != null ? jour.getId() > 0 : false) {
                        selectPresence.setSupplementaire(paramGrh.getLimitHeureSup() > 0 ? (!jour.getOuvrable() || jour.getJourDeRepos()) : false);
                    }
                }
                if (!selectPresence.isSupplementaire()) {
                    Date date = timestampToDate(selectPresence.getDateDebut());
                    YvsJoursFeries jour = (YvsJoursFeries) dao.loadOneByNameQueries("YvsJoursFeries.findByJour", new String[]{"societe", "jour"}, new Object[]{currentAgence.getSociete(), date});
                    if (jour != null ? jour.getId() > 0 : false) {
                        selectPresence.setSupplementaire(true);
                    }
                }
                currentEmploye = buildPresence(selectPresence);
                update("zone_header_fiche_P");
                //recalculer les pointages
                IYvsGrhPointage i_pointage = (IYvsGrhPointage) IEntitiSax.createInstance("IYvsGrhPointage", dao);
                if (i_pointage != null) {
                    pointagesFiche = giveReelPointage(selectPresence);
                    if (pointagesFiche != null) {
                        Collections.sort(pointagesFiche, new PointageEmploye());
                        String query = "DELETE FROM yvs_grh_pointage WHERE presence = ?";
                        dao.requeteLibre(query, new Options[]{new Options(selectPresence.getId(), 1)});
                        selectPresence.getPointages().clear();
                        for (PointageEmploye pe : pointagesFiche) {
                            i_pointage.onSavePointage(selectPresence, pe.getHeure(), pe.getPointeuse(), 0, currentUser);
                        }
                    }
                }
                currentEmploye.setPointages(loadPointageFicheEmps(selectPresence));
//                if (!currentEmploye.getPointages().isEmpty()) {
//                    currentEmploye.setTpresence(Utilitaire.doubleToHour((pointages.get(0).getPresence().getTotalPresence() == null) ? 0 : pointages.get(0).getPresence().getTotalPresence()));
//                }
                update("AllPointage-tab1-e");
                update("AllPointage-tab-e");
            }
        }
    }

    /**
     * BEGIN IMPORTATION PRESENC
     *
     * @param event*
     */
    public void handleFileUpload(FileUploadEvent event) {
        processFileUpload(event.getFile());
    }

    public void FileUpload() {
        processFileUpload(file);
    }

    public void processFileUpload(UploadedFile file) {
        try {
            pointagesUpload.clear();
            if (file != null) {
                List<List<String>> result = Util.readFileCSV(file);
                if (result != null ? !result.isEmpty() : false) {
                    YvsGrhPresence presence;
                    YvsGrhEmployes employe;
                    Calendar date = Calendar.getInstance();
                    for (List<String> line : result) {
                        String id = line.get(2);
                        String year = line.get(8);
                        String month = line.get(9);
                        String day = line.get(10);
                        String hour = line.get(11);
                        String minute = line.get(12);
                        String second = line.get(13);

                        date.set(Calendar.YEAR, Integer.valueOf(year));
                        date.set(Calendar.MONTH, Integer.valueOf(month) - 1);
                        date.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
                        date.set(Calendar.HOUR_OF_DAY, 0);
                        date.set(Calendar.MINUTE, 0);
                        date.set(Calendar.SECOND, 0);

                        if (filterUpload) {
                            if (filterEmployeUpload != null ? filterEmployeUpload.getId() > 0 ? !filterEmployeUpload.getId().equals(Long.valueOf(id)) : false : false) {
                                continue;
                            }
                            if (addFilterDateUpload ? date.getTime().before(filterDateDebutUpload) || date.getTime().after(filterDateFinUpload) : false) {
                                continue;
                            }
                        }

                        employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findById", new String[]{"id"}, new Object[]{Long.valueOf(id)});
                        if (employe != null ? employe.getId() > 0 : false) {

                            presence = new YvsGrhPresence(employe, date.getTime());

                            date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour));
                            date.set(Calendar.MINUTE, Integer.valueOf(minute));
                            date.set(Calendar.SECOND, Integer.valueOf(second));

                            pointagesUpload.add(new YvsGrhPointage(presence, date.getTime(), 0));
                        }
                    }
                    Collections.sort(pointagesUpload, Collections.reverseOrder());
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadViewEmploye(SelectEvent ev) {
        if (ev != null) {
            YvsGrhEmployes e = (YvsGrhEmployes) ev.getObject();
            filterEmployeUpload = e;
        }
    }

    public void searchEmploye(String matricule) {
        filterEmployeUpload = new YvsGrhEmployes();
        if (matricule != null ? !matricule.trim().isEmpty() : false) {
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.addParamActif(true);
                service.findEmploye(matricule);
                if (service.getListEmployes().size() == 1) {
                    filterEmployeUpload = service.getListEmployes().get(0);
                } else if (service.getListEmployes().size() > 0) {
                    openDialog("dlgEmploye");
                    update("tabEmployes-uploadEmps");
                }
            }
        }
    }

    public void saveUploadPointage() {
        try {
            if (pointagesUpload != null ? !pointagesUpload.isEmpty() : false) {
                String query = "delete from yvs_grh_presence where employe in (select e.id from yvs_grh_employes e inner join yvs_agences a on e.agence = a.id where a.societe = " + currentAgence.getSociete().getId() + ")";
                if (!deleteAllFiche) {
                    query += " and valider = false";
                }
                if (filterUpload) {
                    if (filterEmployeUpload != null ? filterEmployeUpload.getId() > 0 : false) {
                        query += " and employe = " + filterEmployeUpload.getId();
                    }
                    if (addFilterDateUpload) {
                        query += " and date_debut between '" + formatDate.format(filterDateDebutUpload) + "' and '" + formatDate.format(filterDateFinUpload) + "' and date_fin between '" + formatDate.format(filterDateDebutUpload) + "' and '" + formatDate.format(filterDateFinUpload) + "'";
                    }
                }
                dao.requeteLibre(query, new Options[]{});
                for (YvsGrhPointage upload : pointagesUpload) {
                    if (insertionPointage(upload.getPresence().getEmploye(), upload.getPresence().getDateDebut(), upload.getHeureEntree(), upload.getAction())) {
                        upload.setUpload(true);
                    }
                }
                update("pointage-upload");
                succes();
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Construit la fiche de présence en fonction du planning et de l'employé
    public YvsGrhPresence getPresence(YvsGrhPlanningEmploye p, YvsGrhEmployes e) {
        YvsGrhPresence bean = new YvsGrhPresence();
        bean.setHeureDebut(p.getHeureDebut());
        bean.setHeureFin(p.getHeureFin());
        bean.setEmploye(e);
        bean.setDateDebut(p.getDateDebut());
        bean.setDateFin(p.getDateFin());
        bean.setDureePause(p.getDureePause());
        bean.setValider(p.isValide());
        bean.setSupplementaire(p.isSupplementaire());
        bean.setAuthor(currentUser);
        bean.setDateUpdate(new Date());
        bean.setDateSave(new Date());
        return bean;
    }

    public YvsGrhPointage getPointage(YvsGrhPresence pe, Date heureEntree, Date heureSortie, boolean systemIn, boolean systemOut, boolean heureNormale) {
        return getPointage(pe, null, heureEntree, heureSortie, false, systemIn, systemOut, heureNormale);
    }

    public YvsGrhPointage getPointage(YvsGrhPresence pe, Date heureEntree, Date heureSortie, boolean valider, boolean systemIn, boolean systemOut, boolean heureNormale) {
        return getPointage(pe, null, heureEntree, heureSortie, valider, systemIn, systemOut, heureNormale);
    }

    public YvsGrhPointage getPointage(YvsGrhPresence pe, YvsGrhPointage bean, Date heureEntree, Date heureSortie, boolean valider, boolean systemIn, boolean systemOut, boolean heureNormale) {
        if (bean == null) {
            bean = new YvsGrhPointage();
            bean.setDateSave(new Date());
            bean.setHoraireNormale(heureNormale);
        }
        bean.setPresence(pe);
        if (heureEntree != null) {
            bean.setHeureEntree(heureEntree);
            bean.setDateSaveEntree(new Date());
        }
        if (heureSortie != null) {
            bean.setHeureSortie(heureSortie);
            bean.setDateSaveSortie(new Date());
        }
        bean.setHeurePointage(new Date());
        bean.setCompensationHeure(false);
        bean.setMotifSortie("ABSENT");
        bean.setActif(true);
        bean.setPointageAutomatique(true);
        bean.setCommission(false);
        bean.setSystemOut(systemOut);
        bean.setSystemIn(systemIn);
        bean.setHeureSupplementaire(pe.isSupplementaire());
        bean.setValider((!bean.getHeureSupplementaire()) ? valider : true);
        bean.setAuthor(currentUser);
        bean.setDateUpdate(new Date());
        return bean;
    }

    public YvsGrhTrancheHoraire getTrancheHoraire(YvsGrhEmployes e, Date heure_) {
        // On cherche la tranche horaire correspondante
        String type = ((e.getContrat() != null) ? e.getContrat().getTypeTranche() : "JN");
        nameQueri = "YvsGrhTrancheHoraire.findByTypeJournee";
        champ = new String[]{"typeJournee", "societe"};
        val = new Object[]{type, currentAgence.getSociete()};
        YvsGrhTrancheHoraire t = getTrancheHoraire(heure_, nameQueri, champ, val);
        if (t != null ? t.getId() < 1 : true) {
            nameQueri = "YvsGrhTrancheHoraire.findAll";
            champ = new String[]{"societe"};
            val = new Object[]{currentAgence.getSociete()};
            t = getTrancheHoraire(heure_, nameQueri, champ, val);
        }
        return t;
    }

    public YvsGrhTrancheHoraire getTrancheHoraire(Date heure_, String query, String[] champ, Object[] val) {
        YvsGrhTrancheHoraire p_ = new YvsGrhTrancheHoraire();
        List<YvsGrhTrancheHoraire> lt = dao.loadNameQueries(query, champ, val);
        if (lt != null ? !lt.isEmpty() : false) {
            for (int i = 0; i < lt.size(); i++) {
                YvsGrhTrancheHoraire t = lt.get(i);
                Date heureDebut = Util.buildTimeStamp(heure_, t.getHeureDebut(), false);
                Date heureFin = Util.getTimeStamp(heureDebut, t.getHeureFin());
                if (heure_.before(heureDebut)) {
                    if (i == 0) {
                        p_.setId(t.getId());
                        p_.setHeureDebut(t.getHeureDebut());
                        p_.setHeureFin(t.getHeureFin());
                        p_.setDureePause(t.getDureePause());
                    } else {
                        YvsGrhTrancheHoraire t_ = lt.get(i - 1);
                        Date d = Util.addTimeInDate(heure_, paramGrh.getTimeMargeAvance());
                        Date hd = Util.buildTimeStamp(heure_, t.getHeureDebut(), true, false);
                        if (d.before(hd)) {
                            p_.setId(t_.getId());
                            p_.setHeureDebut(t_.getHeureDebut());
                            p_.setHeureFin(t_.getHeureFin());
                            p_.setDureePause(t_.getDureePause());
                        } else {
                            p_.setId(t.getId());
                            p_.setHeureDebut(t.getHeureDebut());
                            p_.setHeureFin(t.getHeureFin());
                            p_.setDureePause(t.getDureePause());
                        }
                    }
                    break;
                } else if ((heureDebut.before(heure_)) && (heure_.before(heureFin))) {
                    if (i < lt.size() - 1) {
                        YvsGrhTrancheHoraire t_ = lt.get(i + 1);
                        Date d = Util.addTimeInDate(heure_, paramGrh.getTimeMargeAvance());
                        Date hd = Util.buildTimeStamp(heure_, t.getHeureDebut(), true, false);
                        if (!d.before(hd)) {
                            p_.setId(t_.getId());
                            p_.setHeureDebut(t_.getHeureDebut());
                            p_.setHeureFin(t_.getHeureFin());
                            p_.setDureePause(t_.getDureePause());
                        } else {
                            p_.setId(t.getId());
                            p_.setHeureDebut(t.getHeureDebut());
                            p_.setHeureFin(t.getHeureFin());
                            p_.setDureePause(t.getDureePause());
                        }
                    } else {
                        p_.setId(t.getId());
                        p_.setHeureDebut(t.getHeureDebut());
                        p_.setHeureFin(t.getHeureFin());
                        p_.setDureePause(t.getDureePause());
                    }
                    break;
                }
            }
        }
        return p_;
    }

    public YvsGrhPresence getPresence(YvsGrhEmployes e, Date current_time, boolean search_only) {
        if (e != null ? e.getId() > 0 : false) {
            Calendar heure = Calendar.getInstance();
            heure.setTime(current_time);
            heure.set(Calendar.SECOND, 0);

            current_time = heure.getTime();
            //On recherche la fiche de presence a la date debut et la date de fin
            List<YvsGrhPresence> lr = dao.loadNameQueries("YvsGrhPresence.findByDateAndEmploye", new String[]{"date", "employe"}, new Object[]{current_time, e});
            if (lr != null ? !lr.isEmpty() : false)//Si elle existe
            {
                for (YvsGrhPresence pe : lr) {
                    boolean sortie = false;//Defini la nature du mouvement (entree ou sortie)
                    List<YvsGrhPointage> lo = dao.loadNameQueries("YvsGrhPointage.findLastPointage", new String[]{"fiche"}, new Object[]{pe});
                    if (lo != null ? !lo.isEmpty() : false) {
                        YvsGrhPointage last = lo.get(0);
                        if ((last != null) ? (last.getId() != 0 ? (last.getHeureSortie() != null ? formatHeure.format(last.getHeureSortie()).equals("00:00:00") : true) : false) : false) {
                            sortie = true;
                        }
                    }
                    Date heure_fin = Util.buildTimeStamp(pe.getDateFinPrevu(), pe.getHeureFinPrevu(), false);
                    //On Verifi si la date de pointage est egale à la date de début de la fiche
                    if (formatDate.format(pe.getDateDebut()).equals(formatDate.format(current_time))) {
                        if (!sortie) {//Si c'est uen entree 
                            if (current_time.after(heure_fin)) {//On controle si le pointage est hors des marges de la fiche
                                // On modifie l'heure et/ou la date de fin prevu de la fiche
                                YvsGrhTrancheHoraire t = getTrancheHoraire(e, current_time);
                                pe.setHeureFinPrevu(t.getHeureFin());
                                pe.setDateFinPrevu(Util.getTimeStamp(Util.buildTimeStamp(pe.getDateDebut(), pe.getHeureDebut()), pe.getHeureFinPrevu()));
                                if (!search_only) {
                                    dao.update(pe);
                                }
                            }
                        }
                        pe.setHeureDebut(Util.buildTimeStamp(pe.getDateDebut(), pe.getHeureDebut(), false));
                        pe.setHeureFin(Util.buildTimeStamp(pe.getDateFin(), pe.getHeureFin(), false));
                        pe.setHeureFinPrevu(Util.buildTimeStamp(pe.getDateFinPrevu(), pe.getHeureFinPrevu(), false));
                        return pe;
                    } else {
                        if (sortie) {//Si c'est une sortie on ajoute la marge a l'heure de fin
                            heure_fin = Util.addTimeInDate(heure_fin, paramGrh.getTimeMargeAvance());
                        }
                        if (!current_time.after(heure_fin)) {//On controle si le pointage est dans les marges de la fiche
                            pe.setHeureDebut(Util.buildTimeStamp(pe.getDateDebut(), pe.getHeureDebut(), false));
                            pe.setHeureFin(Util.buildTimeStamp(pe.getDateFin(), pe.getHeureFin(), false));
                            pe.setHeureFinPrevu(Util.buildTimeStamp(pe.getDateFinPrevu(), pe.getHeureFinPrevu(), false));
                            return pe;
                        }
                    }
                }
            }
        }
        return null;
    }

    public YvsGrhPresence getPresence(YvsGrhEmployes e, YvsGrhPlanningEmploye p) {
        try {
            if ((e != null) ? e.getId() > 0 : false) {
                if (p != null ? p.getId() > 0 : false) {
                    //On recherche s'il une fiche de presence n'existe pas a la date début et la date fin du planning
                    List<YvsGrhPresence> lr = dao.loadNameQueries("YvsGrhPresence.findByDatesAndEmploye", new String[]{"dateDebut", "dateFin", "employe"}, new Object[]{p.getDateDebut(), p.getDateFin(), e});
                    boolean deja = false;
                    if (lr != null ? !lr.isEmpty() : false) {
                        deja = true;
                    } else {
                        //On recherche s'il une fiche de presence n'existe pas a la date début du planning
                        lr = dao.loadNameQueries("YvsGrhPresence.findLastByDateDebutAndEmploye", new String[]{"dateDebut", "employe"}, new Object[]{p.getDateDebut(), e});
                        if (lr != null ? !lr.isEmpty() : false) {
                            deja = true;
                        }
                    }
                    if (deja) {
                        YvsGrhPresence presence = lr.get(0);
                        presence.setHeureDebut(Util.buildTimeStamp(presence.getDateDebut(), presence.getHeureDebut(), false));
                        presence.setHeureFin(Util.buildTimeStamp(presence.getDateFin(), presence.getHeureFin(), false));
                        YvsJoursOuvres jour;
                        if ((e.getContrat() != null) ? (e.getContrat().getId() != 0 ? ((e.getContrat().getCalendrier() != null) ? e.getContrat().getCalendrier().getId() != 0 : false) : false) : false) {
                            jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(p.getDateDebut()), e.getContrat().getCalendrier()});
                        } else {
                            YvsCalendrier y = (YvsCalendrier) dao.loadOneByNameQueries("YvsCalendrier.findByDefautSociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                            jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(p.getDateDebut()), y});
                        }
                        if (jour != null ? jour.getId() > 0 : false) {
                            presence.setSupplementaire(!jour.getOuvrable());
                        }
                        return presence;
                    }
                }
            } else {

            }
            return null;
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public YvsGrhPlanningEmploye getPlanningForJoursOuvres(YvsJoursOuvres jour, Date date) {
        YvsGrhPlanningEmploye bean = new YvsGrhPlanningEmploye();
        if (jour != null) {
            bean.setId(jour.getId());
            bean.setDateDebut(date);
            Date d = Util.buildTimeStamp(date, jour.getHeureDebutTravail());
            bean.setDateFin(Util.getTimeStamp(d, jour.getHeureFinTravail()));
            bean.setDureePause(jour.getDureePause());
            bean.setHeureDebut(jour.getHeureDebutTravail());
            bean.setHeureFin(jour.getHeureFinTravail());
            bean.setSupplementaire(!jour.getOuvrable());
        }
        return bean;
    }

    public YvsGrhPlanningEmploye getPlanningClean(Date heure_, boolean searchJour) {
        YvsGrhPlanningEmploye planning = new YvsGrhPlanningEmploye();
        Calendar heure = Calendar.getInstance();
        heure.setTime(heure_);
        planning.setId(1L);
        planning.setDateDebut(heure_);
        planning.setDateFin(heure_);
        heure.set(Calendar.HOUR_OF_DAY, 7);
        heure.set(Calendar.MINUTE, 30);
        heure.set(Calendar.SECOND, 0);
        planning.setHeureDebut(heure.getTime());

        heure.set(Calendar.HOUR_OF_DAY, 17);
        heure.set(Calendar.MINUTE, 00);
        heure.set(Calendar.SECOND, 0);
        planning.setHeureFin(heure.getTime());

        heure.set(Calendar.HOUR_OF_DAY, 1);
        heure.set(Calendar.MINUTE, 30);
        heure.set(Calendar.SECOND, 0);
        planning.setDureePause(heure.getTime());

        planning.setValide(false);
        if (searchJour) {
            YvsCalendrier y = (YvsCalendrier) dao.loadOneByNameQueries("YvsCalendrier.findByDefautSociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            YvsJoursOuvres jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(planning.getDateDebut()), y});
            if (jour != null ? jour.getId() > 0 : false) {
                planning.setSupplementaire(!jour.getOuvrable());
            }
        }
        return planning;
    }

    public YvsGrhPlanningEmploye getSimplePlanning(YvsGrhEmployes e, Date heure_) {
        try {
            YvsGrhPlanningEmploye planning = new YvsGrhPlanningEmploye();
            Calendar heure = Calendar.getInstance();
            // On verifie si l'employé a un horaire dynamique
            if (e.getHoraireDynamique()) {  // Si oui
                // On recherche le planning de l'employé a la date
                List<YvsGrhPlanningEmploye> lp = dao.loadNameQueries("YvsGrhPlanningEmploye.findByDateAndEmploye", new String[]{"employe", "date"}, new Object[]{e, heure_});
                if (!lp.isEmpty()) {// Si il a un planning
                    if (lp.size() > 1) {// Si il a plus d'un planning
                        int i = 0;
                        // On recherche le bon planning
                        for (YvsGrhPlanningEmploye p : lp) {
                            planning = p;

                            heure.setTime(p.getDateDebut());
                            heure.set(Calendar.HOUR_OF_DAY, 0);
                            heure.set(Calendar.MINUTE, 0);
                            heure.set(Calendar.SECOND, 0);
                            heure.set(Calendar.MILLISECOND, 0);
                            Date date_debut = heure.getTime();

                            Date heure_debut = Util.buildTimeStamp(date_debut, p.getHeureDebut(), false);

                            heure.setTime(p.getDateFin());
                            heure.set(Calendar.HOUR_OF_DAY, 0);
                            heure.set(Calendar.MINUTE, 0);
                            heure.set(Calendar.SECOND, 0);
                            heure.set(Calendar.MILLISECOND, 0);

                            Date heure_fin = Util.buildTimeStamp(heure.getTime(), p.getHeureFin(), false);

                            // On verifi si l'heure est compris dans le planning actuelle
                            if (!heure_.before(heure_debut) && !heure_.after(heure_fin)) { // Si l'heure est compris on sort
                                break;
                            } else if (heure_.before(heure_debut) && i == 0) { // Si l'heure est inferieur au 1er planning on sort
                                break;
                            } else if (heure_.after(heure_fin)) {
                                // On verifie s'il a une fiche de présence a ce planning
                                List<YvsGrhPresence> lr = dao.loadNameQueries("YvsGrhPresence.findByDateDebutAndEmploye", new String[]{"employe", "dateDebut"}, new Object[]{e, date_debut});
                                if (lr != null ? !lr.isEmpty() : false) {
                                    YvsGrhPresence pe = lr.get(0);
                                    // On verifie s'il le dernier pointage de la fiche est une entrée
                                    List<YvsGrhPointage> lo = dao.loadNameQueries("YvsGrhPointage.findLastPointage", new String[]{"fiche"}, new Object[]{pe});
                                    if (lo != null ? !lo.isEmpty() : false) {
                                        YvsGrhPointage po = lo.get(0);
                                        if ((po != null) ? po.getId() != 0 : false) {
                                            if ((po.getHeureSortie() != null) ? formatHeure.format(po.getHeureSortie()).equals("00:00:00") : true) {
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            ++i;
                        }
                        // On verifie si l'heure est dans le bon interval
                        if (planning != null ? planning.getId() > 0 : false) {
                            Date heure_fin = Util.buildTimeStamp(planning.getDateFin(), planning.getHeureFin(), false);
                            heure_fin = Util.addTimeInDate(heure_fin, paramGrh.getTimeMargeAvance());
                            if (heure_.after(heure_fin)) {
                                List<YvsGrhPointage> lo = dao.loadNameQueries("YvsGrhPointage.findLastPointage", new String[]{"fiche"}, new Object[]{planning});
                                if (lo != null ? !lo.isEmpty() : false) {
                                    YvsGrhPointage po = lo.get(0);
                                    if ((po != null) ? po.getId() != 0 : false) { // Le dernier pointage est une sortie
                                        if ((po.getHeureSortie() != null) ? !formatHeure.format(po.getHeureSortie()).equals("00:00:00") : false) {
                                            planning = null;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (Util.verifyDateHeure(lp.get(0), heure_, paramGrh)) {
                            planning = lp.get(0);
                        }
                    }
                }
                // On verifi si le jour est un jour ouvrable
                if ((planning != null) ? planning.getId() > 0 : false) {
                    YvsJoursOuvres jour;
                    if ((e.getContrat() != null) ? (e.getContrat().getId() != 0 ? ((e.getContrat().getCalendrier() != null) ? e.getContrat().getCalendrier().getId() != 0 : false) : false) : false) {
                        jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(planning.getDateDebut()), e.getContrat().getCalendrier()});
                    } else {
                        YvsCalendrier y = (YvsCalendrier) dao.loadOneByNameQueries("YvsCalendrier.findByDefautSociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                        jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(planning.getDateDebut()), y});
                    }
                    if (jour != null ? jour.getId() > 0 : false) {
                        planning.setSupplementaire(!jour.getOuvrable());
                    }
                }
            } else {
                if ((e.getContrat() != null) ? e.getContrat().getId() != 0 : false) {
                    if ((e.getContrat().getCalendrier() != null) ? e.getContrat().getCalendrier().getId() != 0 : false) {
                        boolean deja = false;
                        heure.setTime(heure_);
                        heure.add(Calendar.DAY_OF_YEAR, -1);
                        YvsJoursOuvres jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(heure.getTime()), e.getContrat().getCalendrier()});
                        if (jour != null ? jour.getId() > 0 : false) {
                            planning = getPlanningForJoursOuvres(jour, heure.getTime());
                        }
                        if (planning != null ? planning.getId() > 0 : false) {
                            if (planning.isChevauche()) {
                                Date d = Util.buildTimeStamp(planning.getDateFin(), planning.getHeureFin());
                                d = Util.addTimeInDate(d, paramGrh.getDureeRetardAutorise());
                                if (!heure_.after(d)) {
                                    deja = true;
                                }
                            }
                        }
                        if (!deja) {
                            planning = null;
                            jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(heure_), e.getContrat().getCalendrier()});
                            if (jour != null ? jour.getId() > 0 : false) {
                                planning = getPlanningForJoursOuvres(jour, heure_);
                            }
                        }
                    }
                }
            }
            planning.setHeureDebut(Util.buildTimeStamp(planning.getDateDebut(), planning.getHeureDebut(), false));
            planning.setHeureFin(Util.buildTimeStamp(planning.getDateFin(), planning.getHeureFin(), false));
            return planning;
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public YvsGrhPlanningEmploye getPlanning(YvsGrhEmployes e, Date heure_) {
        try {
            YvsGrhPlanningEmploye planning = new YvsGrhPlanningEmploye();
            Calendar heure = Calendar.getInstance();
            heure.setTime(heure_);
            if ((e != null) ? e.getId() > 0 : false) {
                planning = getSimplePlanning(e, heure_);
                if ((planning != null) ? planning.getId() < 1 : true) {
                    if (paramGrh.isPlanningDynamique()) { // On cherche la tranche horaire correspondante                       
                        YvsGrhTrancheHoraire t = getTrancheHoraire(e, heure_);
                        if (t != null ? t.getId() > 0 : false) {
                            planning.setId(t.getId());
                            planning.setDateDebut(Util.buildTimeStamp(heure_, t.getHeureDebut(), false));
                            planning.setDateFin(Util.getTimeStamp(planning.getDateDebut(), t.getHeureFin()));
                            planning.setTranche(t);
                            planning.setDureePause(t.getDureePause());
                            planning.setValide(false);
                        } else {
                            planning = getPlanningClean(heure_, false);
                        }
                    } else {
                        planning = getPlanningClean(heure_, false);
                    }
                    // On verifie si l'employé a un horaire dynamique
                    if (e.getHoraireDynamique()) { // Si oui
                        // On verifi si le jour est un jour ouvrable
                        if ((planning != null) ? planning.getId() > 0 : false) {
                            YvsJoursOuvres jour;
                            if ((e.getContrat() != null) ? (e.getContrat().getId() != 0 ? ((e.getContrat().getCalendrier() != null) ? e.getContrat().getCalendrier().getId() != 0 : false) : false) : false) {
                                jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(planning.getDateDebut()), e.getContrat().getCalendrier()});
                            } else {
                                YvsCalendrier y = (YvsCalendrier) dao.loadOneByNameQueries("YvsCalendrier.findByDefautSociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                                jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(planning.getDateDebut()), y});
                            }
                            if (jour != null ? jour.getId() > 0 : false) {
                                planning.setSupplementaire(!jour.getOuvrable());
                            }
                        }
                    } else {
                        if ((e.getContrat() != null) ? e.getContrat().getId() != 0 : false) {
                            if ((planning != null) ? planning.getId() < 1 : true) {
                                planning = getPlanningClean(heure_, true);
                            }
                        } else {
                            planning = getPlanningClean(heure_, true);
                        }
                    }
                }
            } else {
                planning = getPlanningClean(heure_, true);
            }
            planning.setHeureDebut(Util.buildTimeStamp(planning.getDateDebut(), planning.getHeureDebut(), false));
            planning.setHeureFin(Util.buildTimeStamp(planning.getDateFin(), planning.getHeureFin(), false));
            return planning;
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean insertionPointage(YvsGrhEmployes employe_, Date date_, Date heure_, int action) {
        Date h = Util.buildTimeStamp(date_, heure_, false);
        List<YvsGrhPointage> p = dao.loadNameQueries("YvsGrhPointage.findByHeureEmploye", new String[]{"employe", "heure"}, new Object[]{employe_, h});
        if (p != null ? p.isEmpty() : true) {
            return onSavePointage(employe_, heure_, date_, action);
        }
        return false;
    }

    public boolean onSavePointage(YvsGrhEmployes employe, Date current_time, Date current_date, int action) {
        Calendar heure = Calendar.getInstance();
        heure.setTime(current_time);
        heure.set(Calendar.SECOND, 0);
        current_time = heure.getTime();
        try {
            if ((employe != null) ? employe.getId() > 0 : false) {
                boolean have_supplementaire_ = false;
                YvsGrhPresence presence = getPresence(employe, current_time, false);
                if (presence != null ? presence.getId() < 1 : true)//Si elle n'existe pas
                {
                    have_supplementaire_ = true;
                    //On recherche le planning en fonction de l'heure courante
                    YvsGrhPlanningEmploye planning = getPlanning(employe, current_time);
                    //On recherche la fiche de présence en fonction du planning
                    presence = getPresence(employe, planning);
                    if ((presence != null) ? presence.getId() < 1 : true) {
                        presence = (YvsGrhPresence) dao.save1(getPresence(planning, employe));
                        if (presence != null ? presence.getId() > 0 : false) {
                            have_supplementaire_ = false;
                        }
                    }
                }
                if (presence != null ? presence.getId() < 1 : true) {
                    onSavePointage(employe, current_time, current_date, action);
                    return false;
                } else {
                    if (!have_supplementaire_) {
                        YvsJoursOuvres jour;
                        if ((employe.getContrat() != null) ? (employe.getContrat().getId() != 0 ? ((employe.getContrat().getCalendrier() != null) ? employe.getContrat().getCalendrier().getId() != 0 : false) : false) : false) {
                            jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(presence.getDateDebut()), employe.getContrat().getCalendrier()});
                        } else {
                            YvsCalendrier y = (YvsCalendrier) dao.loadOneByNameQueries("YvsCalendrier.findByDefautSociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                            jour = (YvsJoursOuvres) dao.loadOneByNameQueries("YvsJoursOuvres.findByJourConnu", new String[]{"jour", "calendrier"}, new Object[]{formatJour.format(presence.getDateDebut()), y});
                        }
                        if (jour != null ? jour.getId() > 0 : false) {
                            presence.setSupplementaire(!jour.getOuvrable());
                        }
                    }
                }
                if (!presence.getValider()) {
                    return onSavePointage(presence, current_time, action);
                }
            } else {
            }
            return false;
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean onSavePointage(YvsGrhPresence presence, Date current_time, int action) {
        // action  ----  0—Check-In (default value) 1—Check-Out 2—Break-Out 3—Break-In 4—OT-In 5—OT-Out
        try {//Recherche le dernier pointage
            List<YvsGrhPointage> lp = dao.loadNameQueries("YvsGrhPointage.findLastEntreeByPresence", new String[]{"presence"}, new Object[]{presence});
            if (lp != null ? lp.isEmpty() : true) { //S'il n'y'a pas de pointage
                //On insere une entrée
                switch (action) {
                    case 1:
                    case 2:
                    case 5:
                        return onSavePointage("S", null, presence, current_time);
                    case 3:
                    case 4:
                        return onSavePointage("E", null, presence, current_time);
                    default:
                        return onSavePointage("E", null, presence, current_time);
                }
            } else { //S'il existe on le recupère               
                YvsGrhPointage po = lp.get(0);
                //On verifi si le dernier pointage est une entrée
                System.err.println("po.getHeureSortie() " + po.getHeureSortie());
                if ((po.getHeureSortie() != null) ? formatHeure.format(po.getHeureSortie()).equals("00:00:00") : true) { //Si le dernier pointage etait une entrée
                    //On insere une entrée
                    switch (action) {
                        case 1:
                        case 2:
                        case 5:
                            return onSavePointage("S", po, presence, current_time);
                        case 3:
                        case 4:
                            return onSavePointage("E", null, presence, current_time);
                        default:
                            return onSavePointage("S", po, presence, current_time);
                    }
                } else { //Si le dernier pointage etait une sortie
                    switch (action) {
                        case 1:
                        case 2:
                        case 5:
                            return onSavePointage("S", null, presence, current_time);
                        case 3:
                        case 4:
                            return onSavePointage("E", po, presence, current_time);
                        default:
                            return onSavePointage("E", po, presence, current_time);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean onSavePointage(String mouv, YvsGrhPointage po, YvsGrhPresence pe, Date current_time) {
        try {
            switch (mouv) {
                case "S": {
                    if (po != null ? po.getId() > 0 : false) {
                        //On verifi si l'heure d'entrée etait inferieur a l'heure d'entree prevu
                        if (po.getHeureEntree().before(pe.getHeureDebut())) { //Si l'heure d'entree etait inferieur a l'heure d'entree prevu
                            //On verifi si l'heure actuelle est superieur a l'heure d'entree prevu
                            if (current_time.after(pe.getHeureDebut())) { //Si l'heure actuelle  est superieur a l'heure d'entree prevu
                                //On Complete la sortie du dernier pointage par l'heure d'entree prevu
                                dao.update(getPointage(pe, po, po.getHeureEntree(), pe.getHeureDebut(), false, true, true, false));
                                //On verifi si l'heure actuelle est superieur a l'heure de sortie prevu
                                if (current_time.after(pe.getHeureFin())) { //Si l'heure actuelle est superieur a l'heure de sortie prevu
                                    //On insert un pointage supplementaire qui va de l'heure d'entre prevu a l'heure de sortie prevu
                                    dao.save(getPointage(pe, pe.getHeureDebut(), pe.getHeureFin(), true, true, true, false));
                                    //On insert un pointage supplementaire qui va de l'heure de sortie prevu a l'heure actuelle
                                    dao.save(getPointage(pe, pe.getHeureFin(), current_time, false, true, false, true));
                                } else { //Si l'heure actuelle est infereiur a l'heure de sortie prevu
                                    //On insert un pointage supplementaire qui va de l'heure d'entree prevu a l'heure actuelle
                                    dao.save(getPointage(pe, pe.getHeureDebut(), current_time, true, true, false, true));
                                }
                            } else { //Si l'heure actuelle est inferieur a l'heure d'entree prevu
                                //On Complete la sortie du dernier pointage par l'heure actuelle
                                dao.update(getPointage(pe, po, po.getHeureEntree(), current_time, false, false, false, true));
                            }
                        }//On verifi si l'heure d'entre etait superieur a l'heure de sorti prevu
                        else if (!po.getHeureEntree().before(pe.getHeureFin())) {//Si l'heure d'entree etait superieur ou egale a l'heure de sortie prevu
                            //On Complete la sortie du dernier pointage par l'heure actuelle
                            dao.update(getPointage(pe, po, po.getHeureEntree(), current_time, false, false, false, true));
                        } else {//Si l'heure d'entree etait compris entre l'heure d'entree prevu et l'heure de sortie prevu
                            //On verifi si l'heure actuelle est superieur a l'heure de sortie prevu
                            if (current_time.after(pe.getHeureFin())) { //Si l'heure actuelle est superieur a l'heure de sortie prevu
                                //On Complete la sortie du dernier pointage par l'heure de sortie prevu
                                dao.update(getPointage(pe, po, po.getHeureEntree(), pe.getHeureFin(), true, false, true, false));
                                //On insert un pointage supplementaire qui va de l'heure de sortie prevu a l'heure actuelle
                                dao.save(getPointage(pe, pe.getHeureFin(), current_time, false, true, false));
                            } else {
                                //On Complete la sortie du dernier pointage par l'heure actuelle
                                dao.update(getPointage(pe, po, po.getHeureEntree(), current_time, true, false, false, true));
                            }
                        }
                    } else {
                        //On verifi si l'heure actuelle est superieur a l'heure de sortie prevu
                        if (current_time.after(pe.getHeureFin())) {//Si l'heure actuelle est superieur a l'heure de sortie prevu
                            //On Complete la sortie du dernier pointage par l'heure de sortie prevu
                            dao.save(getPointage(pe, null, pe.getHeureFin(), false, false, true, false));
                            //On insert un pointage supplementaire qui va de l'heure de sortie prevu a l'heure actuelle
                            dao.save(getPointage(pe, pe.getHeureFin(), current_time, false, true, false, true));
                        } else {
                            //On Complete la sortie du dernier pointage par l'heure actuelle
                            dao.save(getPointage(pe, null, current_time, false, false, false, true));
                        }
                    }
                    break;
                }
                case "E": {
                    //On insert une entrée
                    Date h_ = Util.addTimeInDate(pe.getHeureDebut(), paramGrh.getDureeRetardAutorise());
                    if (pe.getHeureDebut().before(current_time) && current_time.before(h_)) {
                        YvsGrhPresence p_ = new YvsGrhPresence();
                        p_.setId(pe.getId());
                        p_.setMargeApprouve(paramGrh.getDureeRetardAutorise());
                        //PresenceBLL.Update(p_);
                    } else {
                        YvsGrhPresence p_ = new YvsGrhPresence();
                        p_.setId(pe.getId());
                        p_.setMargeApprouve(pe.getDateFin());
                        //PresenceBLL.Update(p_);
                    }
                    dao.save(getPointage(pe, current_time, null, true, false, false, true));
                }
                default:
                    break;

            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void loadOnViewPointeuse(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectPointeuse = (YvsPointeuse) ev.getObject();
        }
    }

    public void unLoadOnViewPointeuse(UnselectEvent ev) {
        selectPointeuse = new YvsPointeuse();
    }

    public void chooseAgence() {
        try {
            ManagedEmployes w = (ManagedEmployes) giveManagedBean("MEmps");
            if (w != null) {
                if (agenceFilter > 0) {
                    w.loadAllEmploye(agenceFilter);
                } else {
                    w.loadAllEmploye();
                }
            }
            employeFilter = 0;
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void synchroniser() throws InterruptedException {
        try {
            String query = "delete from yvs_grh_presence where employe in (select e.id from yvs_grh_employes e where e.agence = " + currentAgence.getId();
            if (employeFilter > 0) {
                query += " and e.id = " + employeFilter;
            }
            query += ")";
            if (addDate) {
                query += " and date_debut between '" + formatDate.format(dateDebut) + "' and '" + formatDate.format(dateFin) + "' and date_fin between '" + formatDate.format(dateDebut) + "' and '" + formatDate.format(dateFin) + "'";
            }
            if (applyInvalid) {
                query += " and valider = false";
            }
            dao.requeteLibre(query, new Options[]{});
            ProgressBar pbar = (ProgressBar) giveObjectOnView(":main-search:progress");
            if (pbar != null) {

            }
            long count = 0;
            long max = ioemdevises.size();
            for (YvsGrhIoemDevice o : ioemdevises) {
                if (o.isInclure()) {
                    o.setIcorrect(insertionPointage(o.getEmployes(), o.getDateAction(), o.getTimeAction(), o.getPointeuse(), o.getInOutMode()));
                } else {
                    o.setIcorrect(false);
                }
                count++;
                progress = (int) ((count / max) * 100);
            }
            progress = 100;
            update("data-ioemdevises");
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertionPointage(YvsGrhEmployes employe, Date date, Date heure, YvsPointeuse pointeuse, int action) {
        IYvsGrhPointage i_pointage = (IYvsGrhPointage) IEntitiSax.createInstance("IYvsGrhPointage", dao);
        if (i_pointage != null) {
            return i_pointage.insertionPointage(employe, date, heure, pointeuse, action, currentUser);
        }
        return false;
    }

    public void handleFilePointage(FileUploadEvent ev) throws IOException {
        if (ev != null ? selectPointeuse != null : false) {
            try {
                UploadedFile input = ev.getFile();
                String extension = FilenameUtils.getExtension(input.getFileName());
                File file = null;
                String destination;
                if (extension.equals("dat")) {//cas ou le fichier est .dat. on crée d'abord un fichier .csv
                    destination = Initialisation.getCheminResource() + "" + Initialisation.FILE_SEPARATOR + formatDate.format(new Date()) + ".csv";
                    if (new File(destination).exists()) {
                        new File(destination).delete();
                    }
                    file = new File(destination);
                    try (FileWriter output = new FileWriter(file)) {
                        try (CSVWriter write = new CSVWriter(output, ';', CSVWriter.NO_QUOTE_CHARACTER)) {
                            try (DataInputStream in = new DataInputStream(input.getInputstream())) {
                                try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                                    String line;
                                    String[] tabs, part1, part2;
                                    String[] data;
                                    while ((line = br.readLine()) != null) {
                                        data = new String[10];
                                        tabs = line.trim().split(" ");
                                        part1 = tabs[0].trim().split("\t");
                                        part2 = tabs[1].trim().split("\t");
                                        data[0] = part2[1];//machine
                                        data[1] = part1[0];//employe
                                        data[2] = part2[3];//verify_mode
                                        data[3] = part2[4];//in_out_mode
                                        data[4] = "0";//work_code
                                        data[5] = "0";//reserved
                                        data[6] = formatDate.format(formatDateReverse.parse(part1[1]));//date_action
                                        data[7] = part2[0];//time_action
                                        data[8] = data[6] + " " + data[7];//date_time_action
                                        data[9] = selectPointeuse.getId() + "";//pointeuse
                                        write.writeNext(data);
                                    }
                                } catch (IOException ex) {
                                    Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {// cas ou le fichier est .csv
                    destination = Initialisation.getCheminResource() + "" + Initialisation.FILE_SEPARATOR + input.getFileName();
                    if (new File(destination).exists()) {
                        new File(destination).delete();
                    }
                    file = Util.convertInputStreamToFile(destination, input.getInputstream());
                }
                destination = file.getAbsolutePath();
                String query = "DELETE FROM yvs_grh_ioem_device WHERE pointeuse = " + selectPointeuse.getId();
                dao.requeteLibre(query, new Options[]{});
                query = "COPY yvs_grh_ioem_device(machine, employe, verify_mode, in_out_mode, work_code, reserved, date_action, time_action, date_time_action, pointeuse) "
                        + " FROM '" + destination + "' DELIMITER ';' CSV;";
                dao.requeteLibre(query, new Options[]{});
                succes();
            } catch (IOException ex) {
                Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void loadPointage() {
        try {
            if (selectPointeuse != null ? selectPointeuse.getId() < 1 : true) {
                getErrorMessage("Vous devez selectionner une pointeuse");
                return;
            }
            if (addDate) {
                if (employeFilter > 0) {
                    nameQueri = "YvsGrhIoemDevice.findByPointeuseEmployeDate";
                    champ = new String[]{"pointeuse", "employe", "dateDebut", "dateFin"};
                    val = new Object[]{selectPointeuse, employeFilter, dateDebut, dateFin};
                } else {
                    nameQueri = "YvsGrhIoemDevice.findByPointeuseDate";
                    champ = new String[]{"pointeuse", "dateDebut", "dateFin"};
                    val = new Object[]{selectPointeuse, dateDebut, dateFin};
                }
            } else {
                if (employeFilter > 0) {
                    nameQueri = "YvsGrhIoemDevice.findByPointeuseEmploye";
                    champ = new String[]{"pointeuse", "employe"};
                    val = new Object[]{selectPointeuse, employeFilter};
                } else {
                    nameQueri = "YvsGrhIoemDevice.findByPointeuse";
                    champ = new String[]{"pointeuse"};
                    val = new Object[]{selectPointeuse};
                }
            }
            ioemdevises = dao.loadNameQueries(nameQueri, champ, val);
            update("data-ioemdevises");
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String displayEmploye(YvsGrhIoemDevice entity) {
        try {
            YvsGrhEmployes employe;
            int index = employesDisplay.indexOf(new YvsGrhEmployes(entity.getEmploye()));
            if (index > -1) {
                employe = employesDisplay.get(index);
            } else {
                employe = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findById", new String[]{"id"}, new Object[]{entity.getEmploye()});
                employesDisplay.add(employe);
            }
            entity.setEmployes(employe);
        } catch (Exception ex) {
            Logger.getLogger(ManagedPresences.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entity.getEmployes() != null ? entity.getEmployes().getId() > 0 ? entity.getEmployes().getNom_prenom() : "---" : "---";
    }

    public void actionChangeSelection() {
        try {
            selectionAll = !selectionAll;
            for (YvsGrhIoemDevice i : ioemdevises) {
                i.setInclure(selectionAll);
            }
        } catch (Exception ex) {
            getException("ManagedPresences (actionChangeSelection)", ex);
        }
    }

    public void clearAllInserer() {
        try {
            List<YvsGrhIoemDevice> list = new ArrayList<>(ioemdevises);
            ioemdevises.clear();
            for (YvsGrhIoemDevice i : list) {
                if (!i.isIcorrect()) {
                    ioemdevises.add(i);
                }
            }
        } catch (Exception ex) {
            getException("ManagedPresences (clearAllInserer)", ex);
        }
    }

    public void changeActionForAll() {
        try {
            for (YvsGrhIoemDevice i : ioemdevises) {
                i.setInOutMode(actionChange);
            }
        } catch (Exception ex) {
            getException("ManagedPresences (changeActionForAll)", ex);
        }
    }

    public void changeAction() {
        try {
            selectIoemdevise.setInOutMode(actionChange);
        } catch (Exception ex) {
            getException("ManagedPresences (changeAction)", ex);
        }
    }

    public void analysePointage(YvsGrhIoemDevice y) {
        try {
            statut_result = "";
            presencesResult.clear();

            YvsGrhEmployes employe = new YvsGrhEmployes(y.getEmploye());
            Date current_time = Util.dateTimeWithOutSecond(Util.datesToTimestamp(y.getDateAction(), y.getTimeAction()));

            final YvsGrhPointage current = (YvsGrhPointage) dao.loadOneByNameQueries("YvsGrhPointage.findByHeureEmploye", new String[]{"employe", "heure"}, new Object[]{employe, current_time});
            if (current != null ? current.getId() > 0 : false) {
                presencesResult = new ArrayList<YvsGrhPresence>() {
                    {
                        add(current.getPresence());
                    }
                };
                statut_result = "Ce pointage à deja été inseré";
            } else {
                presencesResult = dao.loadNameQueries("YvsGrhPresence.findByDateAndEmployeValid", new String[]{"employe", "date", "valider"}, new Object[]{employe, current_time, true});
                if (presencesResult != null ? !presencesResult.isEmpty() : false) {
                    statut_result = "Ce pointage se trouve dans une fiche validée";
                } else {
                    presencesResult = dao.loadNameQueries("YvsGrhPresence.findByDateAndDatePrevuAndEmploye", new String[]{"employe", "date"}, new Object[]{employe, current_time});
                    if (presencesResult != null ? !presencesResult.isEmpty() : false) {
                        statut_result = "Ce pointage se trouve entre la date debut et la date de fin prevu d'une fiche déjà validée";
                    }
                }
            }
            if (presencesResult != null ? !presencesResult.isEmpty() : false) {
                presenceResulSelect = buildPresence(presencesResult.get(0));
            }
        } catch (Exception ex) {
            getException("ManagedPresences (analysePointage)", ex);
        }
    }

    public void loadOnPresenceResult(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsGrhPresence y = (YvsGrhPresence) ev.getObject();
            presenceResulSelect = buildPresence(y);
        }
    }

    public String action(int action) {
        switch (action) {
            case 1:
                return "check_out.png";
            case 2:
                return "break_out.png";
            case 3:
                return "break_in.png";
            case 4:
                return "override_in.png";
            case 5:
                return "override_out.png";
            default:
                return "check_in.png";
        }
    }

    /**
     * END IMPORTATION PRESENCE*
     */
    public String giveMotif(String code) {
        if (code != null) {
            switch (code) {
                case "P":
                    return "Présent";
                case "A":
                    return "Absent";
                case "M":
                    return "En mission";
                case "C":
                    return "En permission";
                case "F":
                    return "Jour férié";
                case "R":
                    return "Jour de repos";
            }
        }
        return null;
    }

    private List<YvsGrhPresence> presences = new ArrayList<>();
    private YvsGrhPresence ficheSelect;
    private List<PointageEmploye> pointagesFiche = new ArrayList<>();
    private PointageEmploye linePointage;

    public List<YvsGrhPresence> getPresences() {
        return presences;
    }

    public void setPresences(List<YvsGrhPresence> presences) {
        this.presences = presences;
    }

    public List<PointageEmploye> getPointagesFiche() {
        return pointagesFiche;
    }

    public void setPointagesFiche(List<PointageEmploye> pointagesFiche) {
        this.pointagesFiche = pointagesFiche;
    }

    public PointageEmploye getLinePointage() {
        return linePointage;
    }

    public void setLinePointage(PointageEmploye linePointage) {
        this.linePointage = linePointage;
    }

    public void loadFichesToMove(PointageEmploye line, boolean entree) {
        if (line != null && !selectPresence.getValider()) {
            linePointage = line;
            linePointage.setHeure((entree) ? line.getHeurePonitageIn() : line.getHeurePointageOut());
            // ouvre les fiches de la date antérieure à la date du pointage ou à la date suivante
            Date datePrev = yvs.dao.salaire.service.Constantes.givePrevOrNextDate(line.getPresence().getDebut(), -1);
            Date dateNext = yvs.dao.salaire.service.Constantes.givePrevOrNextDate(line.getPresence().getFin(), 1);
            presences = dao.loadNameQueries("YvsGrhPresence.findByTwoDates", new String[]{"date1", "date2", "employe"},
                    new Object[]{datePrev, dateNext, new YvsGrhEmployes(line.getPresence().getEmploye().getId())});
            pointagesFiche.clear();
            openDialog("dlgMyFiches");
            update("table_my_fiches");
            update("table_my_fiches_pointage");
        } else {
            if (selectPresence.getValider()) {
                getErrorMessage("Impossible de modifier cette fiche", "Elle est déjà validé !");
            }
        }
    }

    public void unselectionFiches(SelectEvent ev) {
        pointagesFiche.clear();
    }

    public void selectionFiches(SelectEvent ev) {
        if (ev != null) {
            ficheSelect = (YvsGrhPresence) ev.getObject();
            //récupère les pointages de la fiche selectionné
            List<YvsGrhPointage> list = dao.loadNameQueries("YvsGrhPointage.findByFiche", new String[]{"fiche"}, new Object[]{ficheSelect});
            //positionne le pointage selectionné sur cette fiche
            PointageEmploye temp;
            pointagesFiche.clear();
            for (YvsGrhPointage p : list) {
                if (p.getHeureEntree() != null && !p.getSystemIn()) {
                    temp = new PointageEmploye();
                    temp.setHeure(p.getHeureEntree());
                    temp.setPointeuse(p.getPointeuseIn());
                    pointagesFiche.add(temp);
                }
                if (p.getHeureSortie() != null && !p.getSystemOut()) {
                    temp = new PointageEmploye();
                    temp.setHeure(p.getHeureSortie());
                    temp.setPointeuse(p.getPointeuseOut());
                    pointagesFiche.add(temp);
                }
            }
            temp = new PointageEmploye();
            temp.setId(-1);
            temp.setHeure(linePointage.getHeure());
            temp.setPointeuse(linePointage.getPointeuse());
            pointagesFiche.add(temp);
            Collections.sort(pointagesFiche, new PointageEmploye());
        }
    }

    public void reinserePointageOnFiche() {
        reinserePointageOnFiche_(ficheSelect, true);
    }

    public void reinserePointageOnFiche_(YvsGrhPresence ficheSelect, boolean deleteLine) {
        if (ficheSelect != null) {
            //supprime les pointage de la fiche selectionnépour les reinsérer
            String query = "DELETE FROM yvs_grh_pointage WHERE presence=? ";
            dao.requeteLibre(query, new Options[]{new Options(ficheSelect.getId(), 1)});
            IYvsGrhPointage i_pointage = (IYvsGrhPointage) IEntitiSax.createInstance("IYvsGrhPointage", dao);
            if (i_pointage != null) {
                ficheSelect.setHeureDebut(Util.buildTimeStamp(ficheSelect.getDateDebut(), ficheSelect.getHeureDebut(), false, false));
                ficheSelect.setHeureFin(Util.buildTimeStamp(ficheSelect.getDateFin(), ficheSelect.getHeureFin(), false, false));
                for (PointageEmploye p : pointagesFiche) {
                    i_pointage.onSavePointage(ficheSelect, p.getHeure(), p.getPointeuse(), 0, currentUser);
                }
                if (deleteLine) {
                    query = "DELETE FROM yvs_grh_pointage WHERE id=? ";
                    dao.requeteLibre(query, new Options[]{new Options(linePointage.getId(), 1)});
                    //Réorganiser la fiche en cours
                    List<YvsGrhPointage> list = dao.loadNameQueries("YvsGrhPointage.findByFiche",
                            new String[]{"fiche"}, new Object[]{selectPresence});
                    //positionne le pointage selectionné sur cette fiche
                    PointageEmploye temp;
                    pointagesFiche.clear();
                    for (YvsGrhPointage p : list) {
                        if (p.getHeureEntree() != null && !p.getSystemIn()) {
                            temp = new PointageEmploye();
                            temp.setHeure(p.getHeureEntree());
                            temp.setPointeuse(p.getPointeuseIn());
                            pointagesFiche.add(temp);
                        }
                        if (p.getHeureSortie() != null && !p.getSystemOut()) {
                            temp = new PointageEmploye();
                            temp.setHeure(p.getHeureSortie());
                            temp.setPointeuse(p.getPointeuseOut());
                            pointagesFiche.add(temp);
                        }
                    }
                    Collections.sort(pointagesFiche, new PointageEmploye());
                    reinserePointageOnFiche_(selectPresence, false);
                }
            }
            succes();
        }
    }

    public void openListTrancheToChange() {
        if (currentEmploye.getId() > 0) {
            createTranche = false;
            openDialog("dlgTrancheHoraire_");
        } else {
            getErrorMessage("Aucune fiche n'a été selectionnée!");
        }
    }
}
