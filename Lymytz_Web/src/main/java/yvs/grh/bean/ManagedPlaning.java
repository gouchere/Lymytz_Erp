/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import yvs.parametrage.poste.Departements;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.personnel.YvsGrhPlanningEmploye;
import yvs.entity.grh.presence.YvsGrhEquipeEmploye;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import static yvs.grh.UtilGrh.buildBeanSimplePartialEmploye;
import static yvs.grh.UtilGrh.buildTrancheHoraire;
import yvs.grh.presence.TrancheHoraire;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class ManagedPlaning extends Managed<PlanningWork, YvsGrhPlanningEmploye> implements Serializable {

    @ManagedProperty(value = "#{planningWork}")
    private PlanningWork planning;
    private List<YvsGrhPlanningEmploye> listPlanning;
//    private Date dateFind = new Date();
//    private List<YvsGrhEmployes> listEmploye;
    private List<Departements> selectionDepartement;
    private String strDate;
    private boolean displayButonOption, displayNew = true, hideComposant;
    private boolean updatePlan;
    private List<YvsGrhTrancheHoraire> tranchesHoraire, tranchesActive;
    private YvsGrhTrancheHoraire selectTranche;
    private TrancheHoraire newTranche = new TrancheHoraire();
    private List<YvsGrhEquipeEmploye> equipesEmployes, equipesEmployeActif;

    private EquipeEmploye equipe = new EquipeEmploye(), newEquipe = new EquipeEmploye();
    /**/

    private Date debut = new Date(), fin = new Date();
    /**/

    /**/
    private boolean displayListEmps;
    private boolean date;
    /**/

    private long idTrancheSearch;
    private int idChoixEquipe, idEquipeSearch;
    private Long idAgence;

    // variable search planning
    private Long pagence, ptranche;
    private String pemploye;
    private Date pdebut = new Date(), pfin = new Date();

    public ManagedPlaning() {
        listPlanning = new ArrayList<>();
        tranchesHoraire = new ArrayList<>();
        equipesEmployes = new ArrayList<>();
        equipesEmployeActif = new ArrayList<>();
        tranchesActive = new ArrayList<>();
    }

    public Long getPagence() {
        return pagence;
    }

    public void setPagence(Long pagence) {
        this.pagence = pagence;
    }

    public Long getPtranche() {
        return ptranche;
    }

    public void setPtranche(Long ptranche) {
        this.ptranche = ptranche;
    }

    public String getPemploye() {
        return pemploye;
    }

    public void setPemploye(String pemploye) {
        this.pemploye = pemploye;
    }

    public Date getPdebut() {
        return pdebut;
    }

    public void setPdebut(Date pdebut) {
        this.pdebut = pdebut;
    }

    public Date getPfin() {
        return pfin;
    }

    public void setPfin(Date pfin) {
        this.pfin = pfin;
    }

    public long getIdTrancheSearch() {
        return idTrancheSearch;
    }

    public void setIdTrancheSearch(long idTrancheSearch) {
        this.idTrancheSearch = idTrancheSearch;
    }

    public List<YvsGrhTrancheHoraire> getTranchesActive() {
        return tranchesActive;
    }

    public void setTranchesActive(List<YvsGrhTrancheHoraire> tranchesActive) {
        this.tranchesActive = tranchesActive;
    }

    public List<YvsGrhEquipeEmploye> getEquipesEmployes() {
        return equipesEmployes;
    }

    public void setEquipesEmployes(List<YvsGrhEquipeEmploye> equipesEmployes) {
        this.equipesEmployes = equipesEmployes;
    }

    public List<YvsGrhPlanningEmploye> getListPlanning() {
        return listPlanning;
    }

    public void setListPlanning(List<YvsGrhPlanningEmploye> listPlanning) {
        this.listPlanning = listPlanning;
    }

    public boolean isHideComposant() {
        return hideComposant;
    }

    public void setHideComposant(boolean hideComposant) {
        this.hideComposant = hideComposant;
    }

    public boolean isDisplayNew() {
        return displayNew;
    }

    public void setDisplayNew(boolean displayNew) {
        this.displayNew = displayNew;
    }

    public List<Departements> getSelectionDepartement() {
        return selectionDepartement;
    }

    public void setSelectionDepartement(List<Departements> selectionDepartement) {
        this.selectionDepartement = selectionDepartement;
    }

    public PlanningWork getPlanning() {
        return planning;
    }

    public void setPlanning(PlanningWork planning) {
        this.planning = planning;
    }

//    public Date getDateFind() {
//        return dateFind;
//    }
//
//    public void setDateFind(Date dateFind) {
//        this.dateFind = dateFind;
//    }
    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public boolean isDisplayListEmps() {
        return displayListEmps;
    }

    public void setDisplayListEmps(boolean displayListEmps) {
        this.displayListEmps = displayListEmps;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public TrancheHoraire getNewTranche() {
        return newTranche;
    }

    public void setNewTranche(TrancheHoraire newTranche) {
        this.newTranche = newTranche;
    }

    public List<YvsGrhEquipeEmploye> getEquipesEmployeActif() {
        return equipesEmployeActif;
    }

    public void setEquipesEmployeActif(List<YvsGrhEquipeEmploye> equipesEmployeActif) {
        this.equipesEmployeActif = equipesEmployeActif;
    }

    public boolean isDate() {
        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
    }

    List<PlanningTravail> listPlanningTemp = new ArrayList<>();

    private void stateGrille() {
        displayNew = true;
        displayButonOption = false;
        hideComposant = true;
    }

    private void stateForm() {
        hideComposant = updatePlan;
        displayNew = false;
        displayButonOption = false;
    }

    public EquipeEmploye getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeEmploye equipe) {
        this.equipe = equipe;
    }

    public EquipeEmploye getNewEquipe() {
        return newEquipe;
    }

    public void setNewEquipe(EquipeEmploye newEquipe) {
        this.newEquipe = newEquipe;
    }

    public void setDisplayButonOption(boolean displayButonOption) {
        this.displayButonOption = displayButonOption;
    }

    public boolean isDisplayButonOption() {
        return displayButonOption;
    }

    public int getIdChoixEquipe() {
        return idChoixEquipe;
    }

    public void setIdChoixEquipe(int idChoixEquipe) {
        this.idChoixEquipe = idChoixEquipe;
    }

    public int getIdEquipeSearch() {
        return idEquipeSearch;
    }

    public void setIdEquipeSearch(int idEquipeSearch) {
        this.idEquipeSearch = idEquipeSearch;
    }

    public Long getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(Long idAgence) {
        this.idAgence = idAgence;
    }

//    public void choixEmploye(YvsGrhEmployes e) {
//        if (e != null) {
//            if (e.getActif()) {
//                if (!listEmploye.contains(e)) {
//                    listEmploye.add(e);
//                }
//            } else {
//                if (listEmploye.contains(e)) {
//                    listEmploye.remove(e);
//                }
//            }
//        }
//    }
    //récupère dans une liste les jours du planning qui ne sont plus dans l'intervale 
    public void updatePlaning() {

    }
    /**
     * Employé charge les employé en gérant la pagination
     */
    int offsetEmps;
    private boolean disPrevEmps = true, disNextEmps;

    public boolean isDisNextEmps() {
        return disNextEmps;
    }

    public boolean isDisPrevEmps() {
        return disPrevEmps;
    }

    public void setDisPrevEmps(boolean disPrevEmps) {
        this.disPrevEmps = disPrevEmps;
    }

    public void setDisNextEmps(boolean disNextEmps) {
        this.disNextEmps = disNextEmps;
    }

    public void changeViewListe() {
        displayListEmps = !displayListEmps;
        update("employe-main-panel");
    }

    public List<YvsGrhTrancheHoraire> getTranchesHoraire() {
        return tranchesHoraire;
    }

    public void setTranchesHoraire(List<YvsGrhTrancheHoraire> tranchesHoraire) {
        this.tranchesHoraire = tranchesHoraire;
    }

//    public void loadAllEmployes() {
//        ManagedEmployes service = (ManagedEmployes) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("MEmps");
//        if (service != null) {
//            service.loadAllEmployesByAgence(true, true);
//            listEmploye.clear();
//            for (YvsGrhEmployes e : service.getListEmployes()) {
//                if (e.getHoraireDynamique() && e.getActif()) {
//                    listEmploye.add(e);
//                }
//            }
//            templateEmployes = new ArrayList<>(listEmploye);
//        }
//    }
    //chercher et charger les planning d'une journé (en fait, il s'agit de trouver les employés plannifié pour une journée)
//    public void loadOnePlanning() {
//        champ = new String[]{"date", "agence"};
//        val = new Object[]{dateFind, currentUser.getAgence()};
//        List<YvsGrhPlanningEmploye> l = dao.loadNameQueries("YvsGrhPlanningEmploye.findByDate", champ, val);
//        //charge les détails
//        loadPlanningEmploye(l);
//        stateGrille();
//    }
//    public PlanningTravail buildPlanningTravail(YvsGrhPlanningEmploye dp) {
//        Calendar ca = Calendar.getInstance();
//        ca.setTime(dp.getDateDebut());
//        PlanningTravail pt = new PlanningTravail();
//        pt.setEmploye(UtilGrh.buildBeanPartialEmploye(dp.getYvsEmployes()));
//        pt
//        pt.setJour(dp.getDateDebut());
//        pt.setDateFin(dp.getDateFin());
//        pt.setStrDay(Utilitaire.getDay(ca));
//        pt.setId(dp.getId());
//        strDate = Utilitaire.getDay(ca);
//        return pt;
//    }
    private void loadPlanningEmploye(List<YvsGrhPlanningEmploye> l) {
//        for (YvsGrhPlanningEmploye dp : l) {
//            PlanningTravail pt = buildPlanningTravail(dp);
//            listPlanningEmps.add(pt);
//        }
//        listPlanning = loadListAllPlanning(listPlanningEmps);
//        update("planing-tab");
//        update("btn-palnif-01");
    }

    public List<PlanningWork> loadListAllPlanning(List<PlanningTravail> l) {
        List<PlanningWork> result = new ArrayList<>();
        if (l != null) {
            List<PlanningTravail> list = new ArrayList<>();
            for (PlanningTravail p : l) {
                boolean trouv = false;
                for (PlanningTravail m : list) {
                    if (m.getJour().equals(p.getJour())) {
                        trouv = true;
                        break;
                    }
                }
                if (!trouv) {
                    list.add(p);
                }
            }

            for (PlanningTravail p1 : list) {
                PlanningWork plan = new PlanningWork();
                List<PlanningTravail> list1 = new ArrayList<>();
                for (PlanningTravail m : l) {
                    if (m.getJour().equals(p1.getJour())) {
                        list1.add(m);
                    }
                }
                plan.setJour(p1.getJour());
                plan.getListPlanningTravail().addAll(list1);
                result.add(plan);
            }
        }
        return result;
    }

    public List<PlanningTravail> loadListOneByOnePlanning(List<PlanningTravail> l) {
        List<PlanningTravail> listPl = new ArrayList<>();
        if (l != null) {
            List<PlanningTravail> list = new ArrayList<>();
            for (PlanningTravail p : l) {
                boolean trouv = false;
                for (PlanningTravail m : list) {
                    if (m.getJour().equals(p.getJour())) {
                        trouv = true;
                        break;
                    }
                }
                if (!trouv) {
                    list.add(p);
                }
            }

            for (PlanningTravail p1 : list) {
                int nbremp = 0;
                for (PlanningTravail m : l) {
                    if (m.getJour().equals(p1.getJour())) {
                        nbremp += 1;
                    }
                }
                p1.setNbreEmps(nbremp);
                listPl.add(p1);
            }
        }
        return listPl;
    }

    @Override
    public boolean saveNew() {

        return true;
    }

    private boolean controlePrensentInList(Date d, YvsGrhTrancheHoraire tranche, YvsGrhEmployes emp) {
        //recherche un planning de l'employé à cette la tranche
        YvsGrhPlanningEmploye p = (YvsGrhPlanningEmploye) dao.loadOneByNameQueries("YvsGrhPlanningEmploye.findOnePlan", new String[]{"date", "employe", "tranche"}, new Object[]{d, emp, tranche});
        return p != null;
    }

    public boolean savePlaningEmps(YvsGrhEmployes em) {
        if (!autoriser("planing_save")) {
            openDialog("dlgNotAcces");
            return false;
        }
        if (planning.getTranche().getId() > 0) {
            planning.setEmploye(UtilGrh.buildBeanSimplePartialEmploye(em));
            if (!controlePrensentInList(planning.getJour(), new YvsGrhTrancheHoraire(planning.getTranche().getId()), em)) {
                YvsGrhPlanningEmploye pe = buildEntityPlanning(planning);
                pe.setId(null);
                pe.setAuthor(currentUser);
                pe.setDateSave(new Date());
                pe.setDateUpdate(new Date());
                pe.setActif(true);
                pe.setEmploye(em);
                pe = (YvsGrhPlanningEmploye) dao.save1(pe);
                planning.setId(pe.getId());
                listPlanning.add(0, pe);
                succes();
            } else {
                getErrorMessage("Cet employé a déjà été planifié !");
            }
            return true;
        } else {
            getErrorMessage("Vous devez spécifier une tranche horaire !");
            return false;
        }
    }

    public void addEmployeInPlanning(boolean repos, YvsGrhEmployes employe) {
        YvsGrhPlanningEmploye pe;
        selectTranche = null;
        int idx = (planning.getTranche().getId() > 0) ? tranchesHoraire.indexOf(new YvsGrhTrancheHoraire(planning.getTranche().getId())) : -1;
        if (idx >= 0) {
            selectTranche = tranchesHoraire.get(idx);
        }
        //trouve s'i l'employé a un planning ce jour
        boolean create = false;
        if (employe != null) {
            if (employe.getPlaning() == null) {
                create = (selectTranche != null);
            } else if (selectTranche != null) {
                if (!employe.getPlaning().getTranche().equals(selectTranche)) {
                    if (employe.getPlaning().getTranche().getTypeJournee().equals(selectTranche.getTypeJournee())) {
                        create = true;
                    }
                }
            }
        }
        if (create || repos) {
            pe = new YvsGrhPlanningEmploye();
            pe.setEmploye(employe);
            pe.setActif(true);
            pe.setAuthor(currentUser);
            pe.setChevauche((selectTranche != null) ? selectTranche.getHeureDebut().after(selectTranche.getHeureFin()) : false);
            pe.setDateDebut(planning.getJour());
            pe.setDateFin(((selectTranche != null) ? (selectTranche.getHeureDebut().after(selectTranche.getHeureFin()) ? yvs.dao.salaire.service.Constantes.givePrevOrNextDate(planning.getJour(), 1) : planning.getJour()) : planning.getJour()));
            pe.setDateSave(new Date());
            pe.setDateUpdate(new Date());
            pe.setHeureDebut((selectTranche != null) ? selectTranche.getHeureDebut() : null);
            pe.setHeureFin((selectTranche != null) ? selectTranche.getHeureFin() : null);
            pe.setTranche(selectTranche);
            pe.setValide(true);
            pe.setRepos(repos);
            pe = (YvsGrhPlanningEmploye) dao.save1(pe);
            employe.setPlaning(pe);
            succes();
        } else {
            getErrorMessage("Impossible de créer ce planning verifier votre formulaire !");
        }
    }

    public void activeJourRepos(YvsGrhEmployes employe) {
        if (employe != null ? employe.getPlaning() == null : false) {
            addEmployeInPlanning(true, employe);
        } else if (employe != null) {
            employe.getPlaning().setRepos(!employe.getPlaning().getRepos());
            employe.getPlaning().setAuthor(currentUser);
            employe.getPlaning().setDateUpdate(new Date());
            dao.update(employe.getPlaning());
        }
    }

    public void activeJourReposByPlan(YvsGrhPlanningEmploye planing) {
        if (planing != null) {
            planing.setRepos(!planing.getRepos());
            planing.setAuthor(currentUser);
            planing.setDateUpdate(new Date());
            dao.update(planing);
        }
    }

    public void updateTranche(YvsGrhPlanningEmploye pe) {
        if (!autoriser("planing_update_tranche")) {
            openDialog("dlgNotAcces");
            return;
        }
        if (planning.getTranche().getId() > 0) {
            int idx = tranchesHoraire.indexOf(new YvsGrhTrancheHoraire(planning.getTranche().getId()));
            if (idx >= 0) {
                pe.setAuthor(currentUser);
                pe.setDateUpdate(new Date());
                pe.setTranche(tranchesHoraire.get(idx));
                dao.update(pe);
//                listPlanning.set(listPlanning.indexOf(pe), pe);
                succes();
                return;
            }
        }
        getErrorMessage("Aucune tranche horaire n'a été selectionné !");
    }

    public void desactiverPlan(YvsGrhPlanningEmploye pe, boolean activer) {
        if (!autoriser("planing_active")) {
            openDialog("dlgNotAcces");
            return;
        }
        if (pe.getTranche().getId() > 0) {
            pe.setAuthor(currentUser);
            pe.setDateUpdate(new Date());
            pe.setActif(activer);
            dao.update(pe);
//            pe.setActif(activer);
//            listPlanning.set(listPlanning.indexOf(pe), pe);
            succes();
        }
    }

    public void deletePnaling(YvsGrhPlanningEmploye pw) {
        if (!autoriser("planing_delete_emp")) {
            openDialog("dlgNotAcces");
            return;
        }
        if (pw.getTranche().getId() > 0) {
            try {
                YvsGrhPlanningEmploye en = new YvsGrhPlanningEmploye(pw.getId());
                en.setAuthor(currentUser);
                dao.delete(en);
                listPlanning.remove(en);
            } catch (Exception ex) {
                getFatalMessage("Impossible de supprimer cet élément !");
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    private YvsGrhPlanningEmploye buildEntityPlanning(PlanningWork p) {
        YvsGrhPlanningEmploye pe = new YvsGrhPlanningEmploye();
        pe.setTranche(tranchesHoraire.get(tranchesHoraire.indexOf(new YvsGrhTrancheHoraire(p.getTranche().getId()))));
        pe.setDateDebut(p.getJour());
        if (pe.getTranche().getHeureDebut().after(pe.getTranche().getHeureFin())) {
            Calendar ca1 = Calendar.getInstance();
            ca1.setTime(p.getJour());
            ca1.add(Calendar.DAY_OF_MONTH, 1);
            pe.setDateFin(ca1.getTime());
        } else {
            pe.setDateFin(p.getJour());
        }
        return pe;
    }

    @Override
    public boolean controleFiche(PlanningWork bean) {
        if (debut == null) {
            getMessage("Veuillez saisir la date à planifier", FacesMessage.SEVERITY_ERROR);
            return false;
        } else if (debut.after(fin)) {
            getMessage("Intervalle de date incorrecte", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PlanningWork recopieView() {
        return null;
    }

    @Override
    public void populateView(PlanningWork bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //charge la liste des plannings
    @Override
    public void loadAll() {
//        loadAllEmployes();
        //charge les tranches horaire
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        tranchesHoraire = dao.loadNameQueries("YvsGrhTrancheHoraire.findAllActif", champ, val);
        tranchesActive.clear();
        for (YvsGrhTrancheHoraire t : tranchesHoraire) {
            if (t.getActif()) {
                tranchesActive.add(t);
            }
        }
        loadPlanningEmployeByDate(planning.getJour());
//        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
//        if (service != null) {
//            service.setImax(1000);
//            service.addParamActif(true);
//            //charge les 
//            loadPlanningEmployeByDate(planning.getJour());
//        }
    }

    public void loadAllPlaning(boolean next, boolean init) {
        paginator.addParam(new ParametreRequete("y.employe.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        if (!autoriser("grh_view_all_employe")) {
            paginator.addParam(new ParametreRequete("y.employe.agence", "agence", currentAgence, "=", "AND"));
        }
        String query = "YvsGrhPlanningEmploye y JOIN FETCH y.employe JOIN FETCH y.employe.agence JOIN FETCH y.tranche";
        listPlanning = paginator.executeDynamicQuery("y", "y", query, "y.dateDebut DESC, y.employe.agence.id, y.employe.matricule", next, init, (int) imax, dao);
    }

    public void loadPlanningEmployeByDate(Date date) {
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            List<YvsGrhPlanningEmploye> l = dao.loadNameQueries("YvsGrhPlanningEmploye.findEmployeByDate", new String[]{"societe", "date"},
                    new Object[]{currentAgence.getSociete(), date});
            List<YvsGrhEmployes> temps = new ArrayList<>();
            int idx;
            for (YvsGrhPlanningEmploye pe : l) {
                idx = service.getListEmployes().indexOf(pe.getEmploye());
                if (idx > -1) {
                    pe.getEmploye().setPlaning(pe);
                    temps.add(pe.getEmploye());
                }
            }
            service.getListEmployes().removeAll(temps);
            for (YvsGrhEmployes e : service.getListEmployes()) {
                e.setPlaning(null);
            }
            service.getListEmployes().addAll(temps);
        }
    }

    public void loadAllEquipe() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        equipesEmployes = dao.loadNameQueries("YvsGrhEquipeEmploye.findAll", champ, val);
        equipesEmployeActif.clear();
        for (YvsGrhEquipeEmploye e : equipesEmployes) {
            if (e.getActif()) {
                equipesEmployeActif.add(e);
            }
        }
    }

    @Override
    public void resetFiche() {
        planning.setEmploye(new Employe());
        debut = fin = new Date();
        updatePlan = false;
        stateForm();
        update("form-planing00");
    }

//    public void choixDepartement() {
//        //charger les employé actif d'un département
//        champ = new String[]{"dep"};
//        listEmploye.clear();
//        for (Departements d : selectionDepartement) {
//            val = new Object[]{new YvsGrhDepartement(d.getId())};
//            List<YvsGrhPosteEmployes> l = dao.loadListTableByNameQueries("YvsPosteEmployes.findByDepartement", champ, val);
//            for (YvsGrhPosteEmployes pe : l) {
//                if (pe.getEmploye().getHoraireDynamique()) {
//                    listEmploye.add(pe.getEmploye());
//                }
//            }
//        }
//        closeDialog("dlgDepartement");
//    }
    private Date dateD = new Date(), dateF = new Date();

    public Date getDateF() {
        return dateF;
    }

    public void setDateF(Date dateF) {
        this.dateF = dateF;
    }

    public Date getDateD() {
        return dateD;
    }

    public void setDateD(Date dateD) {
        this.dateD = dateD;
    }

//    public void findPlaning1() {
//        champ = new String[]{"d1", "d2", "agence"};
//        val = new Object[]{dateD, dateF, currentAgence};
//        List<YvsGrhPlanningEmploye> l = dao.loadNameQueries("YvsGrhPlanningEmploye.findByDatesAgence", champ, val);
//        loadPlanningEmploye(l);
//        stateGrille();
//        displayButonOption = false;
//        closeDialog("dlgFind");
//        update("planing-tab");
//        update("planning-work-gridButon");
//    }
//    public void findPlaning() {
//        List<Long> l = new ArrayList<>();
//        for (YvsGrhEmployes e : listEmploye) {
//            l.add(e.getId());
//        }
//        champ = new String[]{"d1", "d2", "liste"};
//        val = new Object[]{dateD, dateF, l};
//        List<YvsGrhPlanningEmploye> lp = dao.loadNameQueries("YvsGrhPlanningEmploye.findByDatesANDEmployes", champ, val);
//        loadPlanningEmploye(lp);
//        stateGrille();
//        closeDialog("dlgFind");
//        update("planing-tab");
//    }
    @Override
    public void deleteBean() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //enregistrer les tranches horaire
    public void saveNewTrancheH() {
        if (newTranche.getHeureDebut() != null && newTranche.getHeureFin() != null) {
            //fabrique le titre de la tranche
            newTranche.setTitre(fatoryTitre(newTranche));
            YvsGrhTrancheHoraire entity = new YvsGrhTrancheHoraire(null);
            entity.setAuthor(currentUser);
            entity.setHeureDebut(newTranche.getHeureDebut());
            entity.setHeureFin(newTranche.getHeureFin());
            entity.setTitre(newTranche.getTitre());
            entity.setTypeJournee(newTranche.getTypeJournee());
            entity.setDateSave(new Date());
            entity.setDateUpdate(new Date());
            entity = (YvsGrhTrancheHoraire) dao.save1(entity);
            entity.setActif(true);
            newTranche.setId(entity.getId());
            tranchesHoraire.add(0, entity);
            tranchesActive.add(0, entity);
            planning.setTranche(newTranche);
            succes();
        } else {
            getErrorMessage("L'intervalle de la tranche est mal défini !");
        }
    }

    private String fatoryTitre(TrancheHoraire tr) {
        String re = null;
        if (tr.getHeureDebut() != null && tr.getHeureFin() != null && tr.getTypeJournee() != null) {
            re = tr.getTypeJournee().concat("_").concat(time.format(tr.getHeureDebut())).concat("-").concat(time.format(tr.getHeureFin()));
        }
        return re;
    }

    public void selectTranche(SelectEvent ev) {
        if (ev != null) {
            planning.setTranche((TrancheHoraire) ev.getObject());
        }
    }

    public void resetTrancheH() {
        newTranche = new TrancheHoraire();
    }

    public void deleteTranche(YvsGrhTrancheHoraire t) {
        try {
            t.setAuthor(currentUser);
            dao.delete(t);
            tranchesHoraire.remove(t);
            tranchesActive.remove(t);
            succes();
        } catch (Exception ex) {
            getFatalMessage("Impossible de supprimer cette tranche", "Elle est déjà lié à des plannings");
            Logger.getLogger(ManagedPlaning.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void toglleActifTranche(YvsGrhTrancheHoraire t) {
        try {
            t.setAuthor(currentUser);
            t.setActif(!t.getActif());
            dao.update(t);
            if (t.getActif()) {
                if (!tranchesActive.contains(t)) {
                    tranchesActive.add(0, t);
                }
            } else {
                tranchesActive.remove(t);
            }
            succes();
        } catch (Exception ex) {
            getFatalMessage("Impossible de modifier cette tranche");
            Logger.getLogger(ManagedPlaning.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void loadPlaningAtDate(Date date) {
//        if (date != null) {
//            champ = new String[]{"date", "agence"};
//            val = new Object[]{date, currentUser.getAgence()};
//            listPlanning = dao.loadNameQueries("YvsGrhPlanningEmploye.findByDate", champ, val);
//            templatePlanings = new ArrayList<>(listPlanning);
//        }
//    }
    public PlanningWork buildplanning(YvsGrhPlanningEmploye pl) {
        PlanningWork re = new PlanningWork();
        if (pl != null) {
            re.setActif(pl.getActif());
            re.setEmploye(buildBeanSimplePartialEmploye(pl.getEmploye()));
            re.setId(pl.getId());
            re.setJour(pl.getDateDebut());
            re.setDateFin(pl.getDateFin());
            re.setTranche(buildTrancheHoraire(pl.getTranche()));
        }
        return re;
    }

//    public List<PlanningWork> buildplanning(List<YvsGrhPlanningEmploye> le) {
//        List<PlanningWork> re = new ArrayList<>();
//        if (le != null) {
//            for (YvsGrhPlanningEmploye sp : le) {
////                if (listEmploye.contains(new Employe(sp.getYvsEmployes().getId()))) {
////                    listEmploye.remove(new Employe(sp.getYvsEmployes().getId()));
////                }
//                re.add(buildplanning(sp));
//            }
//        }
//        return re;
//    }
    /**
     * Zone de recherche
     *
     *
     * @param str
     */
    public void findEmployes(String str) {
        ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
        if (service != null) {
            service.findEmploye(str);
            loadPlanningEmployeByDate(planning.getJour());
        }

    }

    public void findPlanningByEmployes(String matricule) {
        ParametreRequete p0 = new ParametreRequete(null, "employe", "Emps", "LIKE", "AND");
        if (Util.asString(matricule)) {
            ParametreRequete p01 = new ParametreRequete("UPPER(y.employe.nom)", "nom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p02 = new ParametreRequete("UPPER(y.employe.prenom)", "prenom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p03 = new ParametreRequete("UPPER(y.employe.matricule)", "matricule", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p04 = new ParametreRequete("UPPER(concat(y.employe.nom,' ',y.employe.prenom))", "nomPrenom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            ParametreRequete p05 = new ParametreRequete("UPPER(concat(y.employe.prenom,' ',y.employe.nom))", "prenomNom", "%" + matricule.toUpperCase() + "%", "LIKE", "OR");
            p0.getOtherExpression().add(p01);
            p0.getOtherExpression().add(p02);
            p0.getOtherExpression().add(p03);
            p0.getOtherExpression().add(p04);
            p0.getOtherExpression().add(p05);
        } else {
            p0.setObjet(null);
        }
        paginator.addParam(p0);
        loadAllPlaning(true, true);

    }
//    private List<YvsGrhEmployes> templateEmployes;
//    private List<YvsGrhPlanningEmploye> templatePlanings;

    public void clearParamPlanning() {
        paginator.clear();
        loadAllPlaning(true, true);
    }

    public void loadEmployeByAgence(ValueChangeEvent ev) {
        if (ev != null) {
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.addParamAgence(ev);
                loadPlanningEmployeByDate(planning.getJour());
            }
        }
    }

    public void loadPlaningByAgence(ValueChangeEvent ev) {
        if (ev != null) {
            Long id = (Long) ev.getNewValue();
            if (id != null ? id > 0 : false) {
                paginator.addParam(new ParametreRequete("y.employe.agence", "agence", new YvsAgences(id), "=", "AND"));
            }
            loadAllPlaning(true, true);
        }
    }

    public void loadPlaningAtDate(SelectEvent ev) {
        if (ev != null) {
            Date date = (Date) ev.getObject();
//            p.setObjet(date);
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.addParamActif(true);
            }
            loadPlanningEmployeByDate(date);
        }
    }

    public void displayEmployePlanifieByEquipe(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.employe.equipe", "dateD", null, "=", "AND");
        if (ev != null) {
            int idEq = (int) ev.getNewValue();
            if (idEq > 0) {
                p.setObjet(new YvsGrhEquipeEmploye(idEq));
            }
        }
        paginator.addParam(p);
        loadAllPlaning(true, true);
    }

    public void addParamTranche(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.tranche", "tranche", null, "=", "AND");
        if (ev != null) {
            long id = (int) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsGrhTrancheHoraire(id));
            }
        }
        paginator.addParam(p);
        loadAllPlaning(true, true);
    }

    public void addParamTranchePlanning(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.tranche", "tranche", null, "=", "AND");
        if (ev != null) {
            Long id = (Long) ev.getNewValue();
            if (id != null ? id > 0 : false) {
                p.setObjet(new YvsGrhTrancheHoraire(id));
            }
        }
        paginator.addParam(p);
        loadAllPlaning(true, true);
    }

    public void findEmployeInListPlanning(String str) {
        ParametreRequete p = new ParametreRequete(null, "employe", "XXX", " LIKE ", "AND");
        if ((str != null) ? str.length() > 0 : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.matricule)", "matricule", "%" + str.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.nom)", "matricule", "%" + str.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.employe.prenom)", "matricule", "%" + str.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(CONCAT(y.employe.nom,' ', y.employe.prenom))", "matricule", "%" + str.trim().toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        loadAllPlaning(true, true);
    }

//    public void loadEmployeInPlanning(boolean avancer, boolean init) {
//        ParametreRequete p = new ParametreRequete("y.employe.agence", "agence", currentUser.getAgence(), "=", "AND");
//        paginator.addParam(p);
//        listPlanning = paginator.executeDynamicQuery("YvsGrhPlanningEmploye", "y.dateDebut DESC", avancer, init, (int) imax, dao);
//    }
    public void changePage(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        loadAllPlaning(true, true);
    }

    public void pagineResult_(boolean avancer) {
        loadAllPlaning(avancer, false);
    }
//    public void findEmployeInListEmploye(String str) {
//        if (str.length() <= 0) {
////            loadAllEmployes();
//        } else {
//            if (templateEmployes == null) {
//                templateEmployes = new ArrayList<>(listEmploye);
//            } else {
//                listEmploye.clear();
//            }
//            List<YvsGrhEmployes> result = new ArrayList<>();
//            switch (optionSearch) {
//                case 2:
//                    for (YvsGrhEmployes e : templateEmployes) {
//                        if (e.getPosteActif().getDepartement().getIntitule().toLowerCase().startsWith(str)) {
//                            result.add(e);
//                        }
//                    }
//                    break;
//                case 1:
//                    for (YvsGrhEmployes e : templateEmployes) {
//                        if (compare(e, str)) {
//                            result.add(e);
//                        }
//                    }
//                    break;
//                default:
//                    break;
//            }
//            listEmploye.addAll(result);
//        }
//    }

    private boolean compare(YvsGrhEmployes e, String query) {
        if (query != null) {
            query = query.toLowerCase();
        }
        return (e.getMatricule().toLowerCase().startsWith(query) || e.getNom().toLowerCase().startsWith(query) || e.getPrenom().toLowerCase().startsWith(query));
    }

    public void addEmployeInEquipe(SelectEvent ev) {
        if (ev != null) {
            YvsGrhEquipeEmploye emp = (YvsGrhEquipeEmploye) ev.getObject();
            selectEmploye.setEquipe(emp);
            dao.update(selectEmploye);
            openDialog("dlgEquipe");
        }
    }

    private YvsGrhEmployes selectEmploye;

    public void openVieawToChangeEquipe(YvsGrhEmployes employe) {
        selectEmploye = employe;
        openDialog("dlgEquipe");
    }

    private boolean updateEquipe;

    public void createNewEquipe() {
        if (newEquipe.getTitreEquipe() != null && newEquipe.getGroupeService() != null) {
            if (!newEquipe.getTitreEquipe().trim().equals("") && !newEquipe.getGroupeService().trim().equals("")) {
                YvsGrhEquipeEmploye entity = new YvsGrhEquipeEmploye();
                entity.setActif(newEquipe.isActif());
                entity.setAuthor(currentUser);
                entity.setGroupeService(newEquipe.getGroupeService());
                entity.setTitreEquipe(newEquipe.getTitreEquipe());
                if (updateEquipe) {
                    entity.setId(newEquipe.getId());
                    dao.update(entity);
                    equipesEmployes.set(equipesEmployes.indexOf(entity), entity);
                    if (!entity.getActif()) {
                        equipesEmployeActif.remove(entity);
                    } else {
                        if (!equipesEmployeActif.contains(entity)) {
                            equipesEmployeActif.add(0, entity);
                        } else {
                            equipesEmployeActif.set(equipesEmployeActif.indexOf(entity), entity);
                        }
                    }
                } else {
                    entity.setId(null);
                    entity = (YvsGrhEquipeEmploye) dao.save1(entity);
                    equipesEmployes.add(0, entity);
                    if (entity.getActif()) {
                        equipesEmployeActif.add(0, entity);
                    }
                }
            } else {
                getErrorMessage("Formulaire incorrecte !");
            }
        } else {
            getErrorMessage("Formulaire incorrecte !");
        }
    }

    public void deleteEquipe(YvsGrhEquipeEmploye eq) {
        try {
            eq.setAuthor(currentUser);
            dao.delete(eq);
            equipesEmployes.remove(eq);
            equipesEmployeActif.remove(eq);
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette equipe !");
        }
    }

    public void toggleActifEquipe(YvsGrhEquipeEmploye eq) {
        try {
            eq.setAuthor(currentUser);
            eq.setActif(!eq.getActif());
            dao.update(eq);
            if (!eq.getActif()) {
                equipesEmployeActif.remove(eq);
            } else {
                if (!equipesEmployeActif.contains(eq)) {
                    equipesEmployeActif.add(0, eq);
                } else {
                    equipesEmployeActif.set(equipesEmployeActif.indexOf(eq), eq);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de supprimer cette equipe !");
        }
    }
    YvsGrhEquipeEmploye selectEquipe;

    public void choisirEquipe(ValueChangeEvent ev) {
        if (ev != null) {
            int id = (int) ev.getNewValue();
            if (id > 0) {
                selectEquipe = equipesEmployes.get(equipesEmployes.indexOf(new YvsGrhEquipeEmploye(id)));
                openDialog("dlgConfirmAddEquipe");
            } else if (id == -1) {
                openDialog("dlgEquipe");
            }
        }
    }

    public void addEquipeInPlanning() {
        if (!autoriser("planing_save")) {
            openDialog("dlgNotAcces");
            return;
        }
        if (selectEquipe != null) {
            for (YvsGrhEmployes e : selectEquipe.getYvsGrhEmployesList()) {
                savePlaningEmps(e);
            }
            succes();
        } else {
            getErrorMessage("Aucune selection d'équipe trouvé !");
        }
    }

    public void loadPlaningByDate(SelectEvent ev) {
        if (ev != null) {
            Date date = (Date) ev.getObject();
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.addParamActif(true);
            }
            loadPlanningEmployeByDate(date);
        }
    }

    public void addParamDate() {
        ParametreRequete p = new ParametreRequete("y.dateDebut", "date", null);
        if (date && (debut != null && fin != null)) {
            p = new ParametreRequete("y.dateDebut", "date", debut, fin, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllPlaning(true, true);
    }
}
