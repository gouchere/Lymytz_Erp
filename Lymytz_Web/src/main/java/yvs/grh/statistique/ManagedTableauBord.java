/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.statistique;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.etats.Dashboards;
import yvs.etats.Rows;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Conges;
import yvs.grh.bean.ContratEmploye;
import yvs.parametrage.poste.Departements;
import yvs.grh.bean.Employe;
import yvs.grh.bean.FormationEmps;
import yvs.grh.bean.mission.Mission;
import yvs.parametrage.poste.PosteDeTravail;
import yvs.grh.bean.Taches;
import yvs.grh.presence.PointageEmploye;
import yvs.mutuelle.Exercice;
import yvs.parametrage.societe.Societe;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.agence.UtilAgence;
import yvs.parametrage.societe.UtilSte;
import yvs.util.Managed;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class ManagedTableauBord extends Managed<TableauBord, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{tableauBord}")
    public TableauBord tableauBord;

    private Agence agenceSelect = new Agence();
    private Departements departementSelect = new Departements();

    private List<YvsSocietes> listSociete;
    private List<YvsAgences> listAgence;
    private List<YvsGrhDepartement> listService;
    private List<YvsBaseExercice> exercices;

    private List<ContratEmploye> listContrat, listContratTache, listContratForfait;
    private List<Conges> listConge;
    private List<Mission> listMission;
    private List<FormationEmps> listFormation;

    private boolean selectAgence, selectDepartement, vueDepartement, VueAgence;
    private long currentIndex, totalElement;
    private String numSearch;
    Boolean onEmploye;

    private Dashboards progress = new Dashboards();
    private List<Object[]> presences;
    private List<Object[]> recapitulatifs;
    private List<Object[]> hebdomadaires;

    private Date dateDebut = new Date(), dateFin = new Date();
    private String periode = "M";
    private Dashboards tabMission = new Dashboards("O");
    private Dashboards progression = new Dashboards();
    private long agence;

    public ManagedTableauBord() {
        listSociete = new ArrayList<>();
        listAgence = new ArrayList<>();
        listContrat = new ArrayList<>();
        listContratTache = new ArrayList<>();
        listContratForfait = new ArrayList<>();
        listMission = new ArrayList<>();
        listConge = new ArrayList<>();
        listFormation = new ArrayList<>();
        exercices = new ArrayList<>();
        presences = new ArrayList<>();
        listService = new ArrayList<>();
        recapitulatifs = new ArrayList<>();
        hebdomadaires = new ArrayList<>();

        tabMission.setNature("O");
    }

    public Dashboards getProgression() {
        return progression;
    }

    public void setProgression(Dashboards progression) {
        this.progression = progression;
    }

    public List<Object[]> getHebdomadaires() {
        return hebdomadaires;
    }

    public void setHebdomadaires(List<Object[]> hebdomadaires) {
        this.hebdomadaires = hebdomadaires;
    }

    public long getAgence() {
        return agence;
    }

    public void setAgence(long agence) {
        this.agence = agence;
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

    public String getPeriode() {
        return periode != null ? periode.trim().length() > 0 ? periode : "M" : "M";
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Dashboards getTabMission() {
        return tabMission;
    }

    public void setTabMission(Dashboards tabMission) {
        this.tabMission = tabMission;
    }

    public Dashboards getProgress() {
        return progress;
    }

    public void setProgress(Dashboards progress) {
        this.progress = progress;
    }

    public List<Object[]> getRecapitulatifs() {
        return recapitulatifs;
    }

    public void setRecapitulatifs(List<Object[]> recapitulatifs) {
        this.recapitulatifs = recapitulatifs;
    }

    public List<YvsGrhDepartement> getListService() {
        return listService;
    }

    public void setListService(List<YvsGrhDepartement> listService) {
        this.listService = listService;
    }

    public Boolean isOnEmploye() {
        return onEmploye;
    }

    public void setOnEmploye(Boolean onEmploye) {
        this.onEmploye = onEmploye;
    }

    public List<Object[]> getPresences() {
        return presences;
    }

    public void setPresences(List<Object[]> presences) {
        this.presences = presences;
    }

    public List<YvsBaseExercice> getExercices() {
        return exercices;
    }

    public void setExercices(List<YvsBaseExercice> exercices) {
        this.exercices = exercices;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public long getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(long currentIndex) {
        this.currentIndex = currentIndex;
    }

    public long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(long totalElement) {
        this.totalElement = totalElement;
    }

    public List<Conges> getListConge() {
        return listConge;
    }

    public void setListConge(List<Conges> listConge) {
        this.listConge = listConge;
    }

    public List<Mission> getListMission() {
        return listMission;
    }

    public void setListMission(List<Mission> listMission) {
        this.listMission = listMission;
    }

    public List<FormationEmps> getListFormation() {
        return listFormation;
    }

    public void setListFormation(List<FormationEmps> listFormation) {
        this.listFormation = listFormation;
    }

    public List<ContratEmploye> getListContratTache() {
        return listContratTache;
    }

    public void setListContratTache(List<ContratEmploye> listContratTache) {
        this.listContratTache = listContratTache;
    }

    public List<ContratEmploye> getListContratForfait() {
        return listContratForfait;
    }

    public void setListContratForfait(List<ContratEmploye> listContratForfait) {
        this.listContratForfait = listContratForfait;
    }

    public List<ContratEmploye> getListContrat() {
        return listContrat;
    }

    public void setListContrat(List<ContratEmploye> listContrat) {
        this.listContrat = listContrat;
    }

    public Agence getAgenceSelect() {
        return agenceSelect;
    }

    public boolean isSelectDepartement() {
        return selectDepartement;
    }

    public Departements getDepartementSelect() {
        return departementSelect;
    }

    public void setDepartementSelect(Departements departementSelect) {
        this.departementSelect = departementSelect;
    }

    public void setSelectDepartement(boolean selectDepartement) {
        this.selectDepartement = selectDepartement;
    }

    public boolean isVueDepartement() {
        return vueDepartement;
    }

    public void setVueDepartement(boolean vueDepartement) {
        this.vueDepartement = vueDepartement;
    }

    public boolean isVueAgence() {
        return VueAgence;
    }

    public void setVueAgence(boolean VueAgence) {
        this.VueAgence = VueAgence;
    }

    public void setAgenceSelect(Agence agenceSelect) {
        this.agenceSelect = agenceSelect;
    }

    public boolean isSelectAgence() {
        return selectAgence;
    }

    public void setSelectAgence(boolean selectAgence) {
        this.selectAgence = selectAgence;
    }

    public List<YvsAgences> getListAgence() {
        return listAgence;
    }

    public void setListAgence(List<YvsAgences> listAgence) {
        this.listAgence = listAgence;
    }

    public TableauBord getTableauBord() {
        return tableauBord;
    }

    public void setTableauBord(TableauBord tableauBord) {
        this.tableauBord = tableauBord;
    }

    public List<YvsSocietes> getListSociete() {
        return listSociete;
    }

    public void setListSociete(List<YvsSocietes> listSociete) {
        this.listSociete = listSociete;
    }

    @Override
    public boolean controleFiche(TableauBord bean) {
        if (bean.getSociete().getId() == 0) {
            getMessage("Vous devez selectionner une societe !", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            Agence agence = (Agence) ev.getObject();
            if (agence.getId() != 0) {
                cloneObject(agenceSelect, agence);
                setSelectAgence(true);
                update("mainTableauBordGrh");
            }
        }
    }

    public void unLoadOnView() {
        resetFiche(agenceSelect);
        agenceSelect.setListTaches(new ArrayList<Taches>());
        agenceSelect.setListDepartement(new ArrayList<Departements>());
        agenceSelect.setListEmploye(new ArrayList<Employe>());
        agenceSelect.setListFichePresence(new ArrayList<PointageEmploye>());
        agenceSelect.setListPosteTravailByAgence(new ArrayList<PosteDeTravail>());
        setSelectAgence(false);
        update("mainTableauBordGrh");
    }

    public void loadOnViewDepartement(SelectEvent ev) {
        if (ev != null) {
            Departements dep = (Departements) ev.getObject();
            if (dep.getId() != 0) {
                cloneObject(departementSelect, dep);
                setSelectDepartement(true);
                update("blog-row-departement-00");
            }
        }
    }

    public void unLoadOnViewDepartement() {
        resetFiche(departementSelect);
        departementSelect.setListPosteTravail(new ArrayList<PosteDeTravail>());
        setSelectDepartement(false);
        update("blog-row-departement-00");
    }

    @Override
    public void loadAll() {
        listSociete = dao.loadNameQueries("YvsSocietes.findAll", new String[]{}, new Object[]{});
        if (tableauBord.getSociete() != null ? tableauBord.getSociete().getId() < 1 : true) {
            tableauBord.setSociete(UtilSte.buildBeanSociete(currentAgence.getSociete()));
        }
    }

    public void load(Boolean onEmploye) {
        this.onEmploye = onEmploye;
        loadAll();
    }

    public void loadAllAgenceForSociete() {
        champ = new String[]{};
        val = new Object[]{};
        int idx = listSociete.indexOf(new YvsSocietes(tableauBord.getSociete().getId()));
        if (idx > -1) {
            YvsSocietes ste = listSociete.get(idx);
            tableauBord.setSociete(UtilSte.buildBeanSociete(ste));
            loadExercieBySociete();
            loadAgenceForSociete();
        }

        update("mainTableauBordGrh");
    }

    public void loadAgenceForSociete() {
        listAgence.clear();
        int idx = listSociete.indexOf(new YvsSocietes(tableauBord.getSociete().getId()));
        if (idx > -1) {
            YvsSocietes ste = listSociete.get(idx);
            listAgence.addAll(ste.getAgences());
            if (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() < 1 : true) {
                Long r;
                if (onEmploye) {
                    r = (Long) dao.loadObjectByNameQueries("YvsGrhEmployes.countAll", new String[]{"societe"}, new Object[]{ste});
                } else {
                    r = (Long) dao.loadObjectByNameQueries("YvsAgences.countAll", new String[]{"societe"}, new Object[]{ste});
                }
                totalElement = r != null ? r : 0;
            }
        }
    }

    public void loadExercieBySociete() {
        exercices = dao.loadNameQueries("YvsBaseExercice.findAll", new String[]{"societe"}, new Object[]{new YvsSocietes(tableauBord.getSociete().getId())});
        if ((tableauBord.getExercice() != null ? tableauBord.getExercice().getId() < 1 : true) && !exercices.isEmpty()) {
            YvsBaseExercice exo = exercices.get(0);
            tableauBord.setExercice(new Exercice(exo.getId()));
            tableauBord.setDatePrec(exo.getDateDebut());
            tableauBord.setDateSuiv(exo.getDateFin());
        }
    }

    public void loadServiceBySociete() {
        listService.clear();
        int idx = listSociete.indexOf(new YvsSocietes(tableauBord.getSociete().getId()));
        if (idx > -1) {
            YvsSocietes ste = listSociete.get(idx);
            listService.addAll(ste.getServices());
        }
    }

    public void chooseSociete() {
        int idx = listSociete.indexOf(new YvsSocietes(tableauBord.getSociete().getId()));
        if (idx > -1) {
            YvsSocietes ste = listSociete.get(idx);
            tableauBord.setSociete(UtilSte.buildSimpleBeanSociete(ste));
        }
        loadAgenceForSociete();
        loadServiceBySociete();
        loadExercieBySociete();
    }

    public void loadAllContract(YvsSocietes ste) {
        listContrat.clear();
        listContratForfait.clear();
        listContratTache.clear();
        listContrat = UtilGrh.buildBeanListContratEmploye(dao.loadNameQueries("YvsGrhContratEmps.findAllScte", new String[]{"societe"}, new Object[]{ste}), false);
//        if (listContrat != null) {
//            if (!listContrat.isEmpty()) {
//                for (ContratEmploye co : listContrat) {
//                    if (co.isSalaireTache()) {
//                        listContratTache.add(co);
//                    } else {
//                        listContratForfait.add(co);
//                    }
//                }
//            }
//        }
//        System.err.println("listContrat : " + listContrat.size());
    }

    public void loadAllCongeEncours(YvsSocietes ste) {
        listConge.clear();
        String[] champ = new String[]{"societe", "date"};
        Object[] val = new Object[]{ste, new Date()};
        listConge = UtilGrh.builBeanListCongeEmps(dao.loadNameQueries("YvsGrhCongeEmps.findAllSte", champ, val));
    }

    public void loadAllFormationEncours(YvsSocietes ste) {
        listFormation.clear();
        String[] champ = new String[]{"societe", "date"};
        Object[] val = new Object[]{ste, new Date()};
        listFormation = UtilGrh.buildBeanFormationEmp(dao.loadNameQueries("YvsFormationEmps.findByDate", champ, val));
    }

    public void loadAllMissionEncours(YvsSocietes ste) {
        listMission.clear();
        String[] champ = new String[]{"societe", "date"};
        Object[] val = new Object[]{ste, new Date()};
        listMission = UtilGrh.buildBeanListeMission1(dao.loadNameQueries("YvsGrhMissions.findAll6", champ, val));
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TableauBord recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(TableauBord bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        resetFiche(tableauBord);
        tableauBord.setAgence(new Agence());
        tableauBord.setEmploye(new Employe());
        tableauBord.setService(new Departements());
        tableauBord.setSociete(new Societe());
        tableauBord.setExercice(new Exercice());
        tableauBord.setRetenues(new ArrayList<YvsGrhElementAdditionel>());
        tableauBord.setContents(new ArrayList<ContentDuree>());
    }

    public void loadInfos(boolean load) {
        if (onEmploye) {
            if (tableauBord.getEmploye() != null ? tableauBord.getEmploye().getId() < 1 : true) {
                pagine(true);
            }
        } else {
            if (!load || (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() < 1 : true)) {
                if (!load) {
                    recolteAllInfos(0, tableauBord.getAgence().getId(), tableauBord.getSociete().getId(), tableauBord.getDatePrec(), tableauBord.getDateSuiv());
                } else if (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() < 1 : true) {
                    pagine(true);
                } else {
                    recolteAllInfos(0, tableauBord.getAgence().getId(), tableauBord.getSociete().getId(), tableauBord.getDatePrec(), tableauBord.getDateSuiv());
                }
            }
        }
    }

    public void pagine(boolean avance) {
        if (onEmploye) {
            if (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() > 0 : false) {
                champ = new String[]{"nom_prenom", "agence"};
                val = new Object[]{tableauBord.getEmploye().getNom_prenom(), new YvsAgences(tableauBord.getAgence().getId())};
                if (avance) {
                    nameQueri = "YvsGrhEmployes.findNextByNomAgence";
                } else {
                    nameQueri = "YvsGrhEmployes.findPrevByNomAgence";
                }
            } else {
                champ = new String[]{"nom_prenom", "societe"};
                val = new Object[]{tableauBord.getEmploye().getNom_prenom(), new YvsSocietes(tableauBord.getSociete().getId())};
                if (avance) {
                    nameQueri = "YvsGrhEmployes.findNextByNom";
                } else {
                    nameQueri = "YvsGrhEmployes.findPrevByNom";
                }
            }
            List<YvsGrhEmployes> y = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
            if (y != null ? !y.isEmpty() : false) {
                recolteAllInfos(y.get(0).getId(), tableauBord.getAgence().getId(), tableauBord.getSociete().getId(), tableauBord.getDatePrec(), tableauBord.getDateSuiv());
                if (avance && currentIndex < totalElement + 1) {
                    currentIndex += 1;
                } else if (currentIndex > 1) {
                    currentIndex -= 1;
                }
            }
        } else {
            tableauBord.setEmploye(new Employe());
            champ = new String[]{"societe", "designation"};
            val = new Object[]{new YvsSocietes(tableauBord.getSociete().getId()), tableauBord.getAgence().getDesignation()};
            if (avance) {
                nameQueri = "YvsAgences.findNextByNom";
            } else {
                nameQueri = "YvsAgences.findPrevByNom";
            }
            List<YvsAgences> y = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
            if (y != null ? !y.isEmpty() : false) {
                recolteAllInfos(0, y.get(0).getId(), tableauBord.getSociete().getId(), tableauBord.getDatePrec(), tableauBord.getDateSuiv());
                if (avance && currentIndex < totalElement + 1) {
                    currentIndex += 1;
                } else if (currentIndex > 1) {
                    currentIndex -= 1;
                }
            }
        }
    }

    public void recolteAllInfos(long employe, long agence, long societe, Date debut, Date fin) {
        tableauBord.getRetenues().clear();
        tableauBord.getContents().clear();
        if (employe > 0) {
            YvsGrhEmployes y = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findById", new String[]{"id"}, new Object[]{employe});
            if (y != null ? y.getId() > 0 : false) {
                tableauBord.setEmploye(UtilGrh.buildAllEMploye(y));
            }
        } else if (agence > 0) {
            YvsAgences y = (YvsAgences) dao.loadOneByNameQueries("YvsAgences.findById", new String[]{"id"}, new Object[]{agence});
            if (y != null ? y.getId() > 0 : false) {
                tableauBord.setAgence(UtilAgence.buildBeanAgence(y));
            }
        } else {
            YvsSocietes y = (YvsSocietes) dao.loadOneByNameQueries("YvsSocietes.findById", new String[]{"id"}, new Object[]{societe});
            if (y != null ? y.getId() > 0 : false) {
                tableauBord.setSociete(UtilSte.buildBeanSociete(y));
            }
        }
        tableauBord.setAllMasseSalarial(0);
        tableauBord.setNbreBulletin(0);
        tableauBord.setMasseSalariale(0);
        tableauBord.setRetenuEncours(0);
        tableauBord.setRetenuRegle(0);

        tableauBord.setChargeCotisation(0);
        tableauBord.setContratSuspendu(0);
        tableauBord.setContratNouveau(0);
    }

    public void loadDataPresence() {
        Options[] param = new Options[]{new Options(onEmploye ? tableauBord.getEmploye().getId() : 0, 1), new Options(tableauBord.getAgence().getId(), 2), new Options(tableauBord.getSociete().getId(), 3), new Options(tableauBord.getDatePrec(), 4), new Options(tableauBord.getDateSuiv(), 5)};
        List<Object[]> durees = dao.loadListBySqlQuery("select * from grh_presence_durees(?, ?, ?, ?, ?) order by element", param);
        for (Object[] data : durees) {
            tableauBord.getContents().add(new ContentDuree(data));
        }
        if (tableauBord.getEmploye() != null ? tableauBord.getEmploye().getId() > 0 : false) {

        } else if (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() > 0 : false) {

        } else {
            tableauBord.setContents(tableauBord.contentsCumulByList());
        }
    }

    public void loadDataRemuneration() {
        if (tableauBord.getEmploye() != null ? tableauBord.getEmploye().getId() > 0 : false) {
            if (tableauBord.getEmploye().getContrat() != null) {
                YvsGrhEmployes y = new YvsGrhEmployes(tableauBord.getEmploye().getId());
                //Masse salariale
                champ = new String[]{"contrat"};
                val = new Object[]{new YvsGrhContratEmps(tableauBord.getEmploye().getContrat().getId())};
                nameQueri = "YvsGrhMontanSalaire.findMasseByContrat";
                Double r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                tableauBord.setAllMasseSalarial(r != null ? r : 0);

                //Nombre Bulletin
                champ = new String[]{"contrat", "dateDebut", "dateFin"};
                val = new Object[]{new YvsGrhContratEmps(tableauBord.getEmploye().getContrat().getId()), tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
                nameQueri = "YvsGrhBulletins.countByContratDates";
                Long n = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
                tableauBord.setNbreBulletin(n != null ? n : 0);

                //Salaire cumulée
                nameQueri = "YvsGrhMontanSalaire.findNetAPayerByContrat";
                r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                tableauBord.setMasseSalariale(r != null ? r : 0);
                //Retenues en cours
                champ = new String[]{"contrat", "statut", "dateDebut", "dateFin"};
                val = new Object[]{new YvsGrhContratEmps(tableauBord.getEmploye().getContrat().getId()), 'E', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
                nameQueri = "YvsGrhDetailPrelevementEmps.sumByContrat";
                r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                tableauBord.setRetenuEncours(r != null ? r : 0);
                //Retenues reglées
                val = new Object[]{new YvsGrhContratEmps(tableauBord.getEmploye().getContrat().getId()), 'P', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
                nameQueri = "YvsGrhDetailPrelevementEmps.sumByContrat";
                r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                tableauBord.setRetenuRegle(r != null ? r : 0);
                //Retenues du contrat
                champ = new String[]{"contrat", "dateDebut", "dateFin"};
                val = new Object[]{new YvsGrhContratEmps(tableauBord.getEmploye().getContrat().getId()), tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
                nameQueri = "YvsGrhDetailPrelevementEmps.findTypeByContract";
                List<YvsGrhTypeElementAdditionel> l = dao.loadNameQueries(nameQueri, champ, val);
                YvsGrhElementAdditionel e;
                for (YvsGrhTypeElementAdditionel t : l) {
                    e = new YvsGrhElementAdditionel((long) tableauBord.getRetenues().size() + 1);
                    e.setTypeElement(t);
                    //Montant total de la retenue
                    champ = new String[]{"contrat", "type"};
                    val = new Object[]{new YvsGrhContratEmps(tableauBord.getEmploye().getContrat().getId()), t};
                    nameQueri = "YvsGrhElementAdditionel.sumByTypeContrat";
                    r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                    e.setMontantElement(r != null ? r : 0);
                    //Montant total en cours de la retenue
                    champ = new String[]{"contrat", "type", "statut", "dateDebut", "dateFin"};
                    val = new Object[]{new YvsGrhContratEmps(tableauBord.getEmploye().getContrat().getId()), t, 'E', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
                    nameQueri = "YvsGrhDetailPrelevementEmps.sumByTypeContrat";
                    r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                    e.setMontantEncours(r != null ? r : 0);
                    //Montant total reglé de la retenue
                    champ = new String[]{"contrat", "type", "statut", "dateDebut", "dateFin"};
                    val = new Object[]{new YvsGrhContratEmps(tableauBord.getEmploye().getContrat().getId()), t, 'P', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
                    nameQueri = "YvsGrhDetailPrelevementEmps.sumByTypeContrat";
                    r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                    e.setMontantRegle(r != null ? r : 0);

                    tableauBord.getRetenues().add(e);
                }
            }
        } else if (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() > 0 : false) {
            YvsAgences y = new YvsAgences(tableauBord.getAgence().getId());
            //Nombre Bulletin
            champ = new String[]{"agence", "dateDebut", "dateFin"};
            val = new Object[]{y, tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
            nameQueri = "YvsGrhBulletins.countByAgenceDates";
            Long n = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setNbreBulletin(n != null ? n : 0);

            //Salaire cumulée
            nameQueri = "YvsGrhMontanSalaire.findNetAPayerByAgence";
            Double r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setMasseSalariale(r != null ? r : 0);

            //Cotisation patronale
            nameQueri = "YvsGrhMontanSalaire.findCotisationByAgence";
            r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setChargeCotisation(r != null ? r : 0);

            //Contrat suspendu
            nameQueri = "YvsGrhContratSuspendu.countByAgence";
            n = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setContratSuspendu(n != null ? n : 0);

            //Nouveau contrat
            nameQueri = "YvsGrhContratEmps.countNewByAgence";
            n = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setContratNouveau(n != null ? n : 0);

            //Nouveau contrat
            nameQueri = "YvsGrhFraisMission.sumByAgence";
            r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setChargeMission(r != null ? r : 0);

            //Masse salariale
            champ = new String[]{"agence"};
            val = new Object[]{y};
            nameQueri = "YvsGrhMontanSalaire.findMasseByAgence";
            r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setAllMasseSalarial(r != null ? r : 0);

            //Retenues en cours
            champ = new String[]{"agence", "statut", "dateDebut", "dateFin"};
            val = new Object[]{y, 'E', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
            nameQueri = "YvsGrhDetailPrelevementEmps.sumByAgence";
            r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setRetenuEncours(r != null ? r : 0);
            //Retenues reglées
            val = new Object[]{y, 'P', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
            nameQueri = "YvsGrhDetailPrelevementEmps.sumByAgence";
            r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setRetenuRegle(r != null ? r : 0);
            //Retenues du contrat
            champ = new String[]{"agence", "dateDebut", "dateFin"};
            val = new Object[]{y, tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
            nameQueri = "YvsGrhDetailPrelevementEmps.findTypeByAgence";
            List<YvsGrhTypeElementAdditionel> l = dao.loadNameQueries(nameQueri, champ, val);
            YvsGrhElementAdditionel e;
            for (YvsGrhTypeElementAdditionel t : l) {
                e = new YvsGrhElementAdditionel((long) tableauBord.getRetenues().size() + 1);
                e.setTypeElement(t);
                //Montant total de la retenue
                champ = new String[]{"agence", "type"};
                val = new Object[]{y, t};
                nameQueri = "YvsGrhElementAdditionel.sumByTypeAgence";
                r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                e.setMontantElement(r != null ? r : 0);
                //Montant total en cours de la retenue
                champ = new String[]{"agence", "type", "statut", "dateDebut", "dateFin"};
                val = new Object[]{y, t, 'E', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
                nameQueri = "YvsGrhDetailPrelevementEmps.sumByTypeAgence";
                r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                e.setMontantEncours(r != null ? r : 0);
                //Montant total reglé de la retenue
                champ = new String[]{"agence", "type", "statut", "dateDebut", "dateFin"};
                val = new Object[]{y, t, 'P', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
                nameQueri = "YvsGrhDetailPrelevementEmps.sumByTypeAgence";
                r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                e.setMontantRegle(r != null ? r : 0);

                tableauBord.getRetenues().add(e);
            }
        } else {
            YvsSocietes y = new YvsSocietes(tableauBord.getSociete().getId());
            //Nombre Bulletin
            champ = new String[]{"societe", "dateDebut", "dateFin"};
            val = new Object[]{y, tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
            nameQueri = "YvsGrhBulletins.countBySocieteDates";
            Long n = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setNbreBulletin(n != null ? n : 0);

            //Salaire cumulée
            nameQueri = "YvsGrhMontanSalaire.findNetAPayerBySociete";
            Double r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setMasseSalariale(r != null ? r : 0);

            //Cotisation patronale
            nameQueri = "YvsGrhMontanSalaire.findCotisationBySociete";
            r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setChargeCotisation(r != null ? r : 0);

            //Contrat suspendu
            nameQueri = "YvsGrhContratSuspendu.countAll";
            n = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setContratSuspendu(n != null ? n : 0);

            //Nouveau contrat
            nameQueri = "YvsGrhContratEmps.countNew";
            n = (Long) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setContratNouveau(n != null ? n : 0);

            //Nouveau contrat
            nameQueri = "YvsGrhFraisMission.sumBySociete";
            r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setChargeMission(r != null ? r : 0);

            //Masse salariale
            champ = new String[]{"societe"};
            val = new Object[]{y};
            nameQueri = "YvsGrhMontanSalaire.findMasseBySociete";
            r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setAllMasseSalarial(r != null ? r : 0);

            //Retenues en cours
            champ = new String[]{"societe", "statut", "dateDebut", "dateFin"};
            val = new Object[]{y, 'E', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
            nameQueri = "YvsGrhDetailPrelevementEmps.sumBySociete";
            r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setRetenuEncours(r != null ? r : 0);
            //Retenues reglées
            val = new Object[]{y, 'P', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
            nameQueri = "YvsGrhDetailPrelevementEmps.sumBySociete";
            r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
            tableauBord.setRetenuRegle(r != null ? r : 0);
            //Retenues du contrat
            champ = new String[]{"societe", "dateDebut", "dateFin"};
            val = new Object[]{y, tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
            nameQueri = "YvsGrhDetailPrelevementEmps.findTypeBySociete";
            List<YvsGrhTypeElementAdditionel> l = dao.loadNameQueries(nameQueri, champ, val);
            YvsGrhElementAdditionel e;
            for (YvsGrhTypeElementAdditionel t : l) {
                e = new YvsGrhElementAdditionel((long) tableauBord.getRetenues().size() + 1);
                e.setTypeElement(t);
                //Montant total de la retenue
                champ = new String[]{"societe", "type"};
                val = new Object[]{y, t};
                nameQueri = "YvsGrhElementAdditionel.sumByTypeSociete";
                r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                e.setMontantElement(r != null ? r : 0);
                //Montant total en cours de la retenue
                champ = new String[]{"societe", "type", "statut", "dateDebut", "dateFin"};
                val = new Object[]{y, t, 'E', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
                nameQueri = "YvsGrhDetailPrelevementEmps.sumByTypeSociete";
                r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                e.setMontantEncours(r != null ? r : 0);
                //Montant total reglé de la retenue
                champ = new String[]{"societe", "type", "statut", "dateDebut", "dateFin"};
                val = new Object[]{y, t, 'P', tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
                nameQueri = "YvsGrhDetailPrelevementEmps.sumByTypeSociete";
                r = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
                e.setMontantRegle(r != null ? r : 0);

                tableauBord.getRetenues().add(e);
            }
        }
    }

    public void loadDataAffectation() {
        if (tableauBord.getEmploye() != null ? tableauBord.getEmploye().getId() > 0 : false) {
            YvsGrhEmployes y = new YvsGrhEmployes(tableauBord.getEmploye().getId());
            champ = new String[]{"employe", "dateDebut", "dateFin"};
            val = new Object[]{y, tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
            nameQueri = "YvsPosteEmployes.findByEmployeDates";
            tableauBord.setAffectations(dao.loadNameQueries(nameQueri, champ, val));
        } else if (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() > 0 : false) {

        } else {

        }
    }

    public void loadDataProgression() {
        if (tableauBord.getEmploye() != null ? tableauBord.getEmploye().getId() > 0 : false) {
            YvsGrhEmployes y = new YvsGrhEmployes(tableauBord.getEmploye().getId());
            champ = new String[]{"employe", "dateDebut", "dateFin"};
            val = new Object[]{y, tableauBord.getDatePrec(), tableauBord.getDateSuiv()};
            nameQueri = "YvsConventionEmploye.findByEmployeDates";
            tableauBord.setConventions(dao.loadNameQueries(nameQueri, champ, val));
        } else if (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() > 0 : false) {

        } else {

        }
    }

    public void loadDataProgressionSalarial() {
        progression.setSociete(currentAgence.getSociete().getId());
        progression.setAgence(currentAgence.getId());
        progression.setEmploye(tableauBord.getEmploye().getId());
        int idx = exercices.indexOf(new YvsBaseExercice(tableauBord.getExercice().getId()));
        if (idx > -1) {
            progression.setDateDebut(exercices.get(idx).getDateDebut());
            progression.setDateFin(exercices.get(idx).getDateFin());
        }
        progression.loadProgressSalaire(dao);
    }

    public double returnValue(String elt) {
        double valeur = 0;
        if (tableauBord.getEmploye().getId() > 0) {
            int idx = tableauBord.getContents().indexOf(new ContentDuree(tableauBord.getEmploye().getId(), elt));
            if (idx > -1) {
                valeur = tableauBord.getContents().get(idx).getValeur();
            }
        } else if (tableauBord.getAgence().getId() > 0) {
            int idx = tableauBord.getContents().indexOf(new ContentDuree(elt));
            if (idx > -1) {
                valeur = tableauBord.getContents().get(idx).getValeur();
            }
        } else if (tableauBord.getSociete().getId() > 0) {
            int idx = tableauBord.getContents().indexOf(new ContentDuree(elt));
            if (idx > -1) {
                valeur = tableauBord.getContents().get(idx).getValeur();
            }
        }
        return valeur;
    }

    public double returnTauxValue(String elt) {
        double valeur = 0;
        if (elt != null) {
            switch (elt) {
                case ContentDuree.JOUR_EFFECTIF: {
                    double jr = returnValue(ContentDuree.JOUR_REQUIS);
                    double je = returnValue(ContentDuree.JOUR_NORMAL) + returnValue(ContentDuree.MISSION) + returnValue(ContentDuree.FORMATION);
                    valeur = (je / jr) < 1 ? (je / jr) : 1;
                    break;
                }
                default:
                    break;
            }
        }
        return valeur;
    }

    public void chooseAgence() {
        tableauBord.setEmploye(new Employe());
        if (onEmploye) {
            Long r;
            if (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() > 0 : false) {
                r = (Long) dao.loadObjectByNameQueries("YvsGrhEmployes.findCountAll", new String[]{"agence"}, new Object[]{new YvsAgences(tableauBord.getAgence().getId())});
            } else {
                r = (Long) dao.loadObjectByNameQueries("YvsGrhEmployes.countAll", new String[]{"societe"}, new Object[]{new YvsSocietes(tableauBord.getSociete().getId())});
            }
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                if (tableauBord.getSociete().getId() > 0) {
                    service.addParamAgence_(null);
                } else {
                    service.addParamAgence_(tableauBord.getAgence().getId());
                }
            }
            totalElement = r != null ? r : 0;
        }
        if (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() < 1 : true) {
            tableauBord.setAgence(new Agence());
        }
        loadInfos(onEmploye);
    }

    public void changeDateByExercice() {
        if (tableauBord.getExercice() != null ? tableauBord.getExercice().getId() > 0 : false) {
            int idx = exercices.indexOf(new YvsBaseExercice(tableauBord.getExercice().getId()));
            if (idx > -1) {
                YvsBaseExercice exo = exercices.get(idx);
                tableauBord.setDatePrec(exo.getDateDebut());
                tableauBord.setDateSuiv(exo.getDateFin());
            }
        } else {
            if (!exercices.isEmpty()) {
                YvsBaseExercice exo = exercices.get(0);
                tableauBord.setDateSuiv(exo.getDateFin());
                exo = exercices.get(exercices.size() - 1);
                tableauBord.setDatePrec(exo.getDateDebut());
            } else {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, 1);
                tableauBord.setDatePrec(c.getTime());
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                tableauBord.setDateSuiv(c.getTime());
            }
        }
    }

    public void chooseExercice() {
        changeDateByExercice();
        recolteAllInfos(tableauBord.getEmploye().getId(), tableauBord.getAgence().getId(), tableauBord.getSociete().getId(), tableauBord.getDatePrec(), tableauBord.getDateSuiv());
    }

    public void recolteAllInfos() {
        recolteAllInfos(tableauBord.getEmploye().getId(), tableauBord.getAgence().getId(), tableauBord.getSociete().getId(), tableauBord.getDatePrec(), tableauBord.getDateSuiv());
    }

    public void chooseDates() {
        recolteAllInfos(tableauBord.getEmploye().getId(), tableauBord.getAgence().getId(), tableauBord.getSociete().getId(), tableauBord.getDatePrec(), tableauBord.getDateSuiv());
    }

    public void loadOnViewEmploye(SelectEvent ev) {
        if (ev != null) {
            YvsGrhEmployes y = (YvsGrhEmployes) ev.getObject();
            numSearch = y.getMatricule();
            choixEmploye(y);
        }
    }

    public void choixEmploye(YvsGrhEmployes ev) {
        recolteAllInfos(ev.getId(), tableauBord.getAgence().getId(), tableauBord.getSociete().getId(), tableauBord.getDatePrec(), tableauBord.getDateSuiv());
        update("chEmploye-dash");
    }

    public void findOneEmploye(String matricule) {
        if (matricule != null) {
            ManagedEmployes service = (ManagedEmployes) giveManagedBean("MEmps");
            if (service != null) {
                service.findEmploye(matricule);
                if (service.getListEmployes().size() == 1) {
                    choixEmploye(service.getListEmployes().get(0));
                } else if (!service.getListEmployes().isEmpty()) {
                    openDialog("dlgEmploye");
                    update("tabEmployes-dash");
                } else {
                }
            }
        }
    }
    /*
     BEGIN DASHBOARD PRESENCE 
     */

    public void initPresence() {
        if (tableauBord.getDatePrec() == null) {
            tableauBord.setDatePrec(new Date());
        }
        if (tableauBord.getDateSuiv() == null) {
            tableauBord.setDateSuiv(new Date());
        }
        if (tableauBord.getAgence() != null ? tableauBord.getAgence().getId() < 1 : true) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);
            tableauBord.setDatePrec(c.getTime());
        }
        if (presences.isEmpty()) {
            loadFichePresence();
        }
    }

    public void loadFichePresence() {
        loadFichePresence(tableauBord.getAgence().getId(), tableauBord.getService().getId(), tableauBord.getEmploye().getMatricule(), tableauBord.getDatePrec(), tableauBord.getDateSuiv());
    }

    public void loadFichePresence(Date debut, Date fin, String matricule, long service) {
        loadFichePresence(tableauBord.getAgence().getId(), service, matricule, debut, fin);
    }

    public void loadFichePresence(long agence, long service, String employe, Date debut, Date fin) {
        Options[] param = new Options[]{new Options(debut, 1), new Options(fin, 2), new Options(agence, 3), new Options(service, 4), new Options(employe, 5)};
        presences = dao.loadListBySqlQuery("select * from grh_et_recap_presence_absence(?, ?, ?, ?, ?) order by nom_prenom", param);
    }

    public void chooseExercicePresence() {
        changeDateByExercice();
    }

    public void loadPresenceEmploye() {
        loadPresenceEmploye(tableauBord.getEmploye().getId(), tableauBord.getDatePrec(), tableauBord.getDateSuiv());
    }

    public void loadPresenceEmploye(long employe, Date debut, Date fin) {
        Options[] param = new Options[]{new Options(employe, 1), new Options(debut, 2), new Options(fin, 3)};
        recapitulatifs = dao.loadListBySqlQuery("select * from grh_et_recap_presence_employe(?, ?, ?) order by date_presence", param);
    }

    public void loadRecapHebdoEmploye(long employe, Date debut, Date fin) {
        hebdomadaires.clear();
        Options[] param = new Options[]{new Options(employe, 1)};
        String query = "SELECT jo.jour, (SELECT convert_jourweek_to_integer(jo.jour)) FROM yvs_jours_ouvres jo INNER JOIN yvs_grh_contrat_emps co ON co.calendrier = jo.calendrier WHERE co.employe = ?";
        List<Object[]> result = dao.loadListBySqlQuery(query, param);
        query = "SELECT ?, find_disponibilite_employe(?, pe.date_debut), pe.total_heure_compensation, pe.total_heure_sup, pe.total_presence, pe.date_debut, pe.valider_hs, pe.type_validation, va.code, va.libelle"
                + " FROM yvs_grh_type_validation va INNER JOIN yvs_grh_presence pe ON va.id = pe.type_validation "
                + " WHERE pe.employe = ? AND (SELECT EXTRACT('DOW' FROM pe.date_debut) = ?) AND (pe.date_debut BETWEEN ? AND ?)";
        List<Object[]> list;
        for (Object[] value : result) {
            String jour = value[0].toString();
            Integer num_day = Integer.valueOf(value[1].toString());
            param = new Options[]{new Options(jour, 1), new Options(employe, 2), new Options(employe, 3), new Options(num_day, 4), new Options(debut, 5), new Options(fin, 6)};
            list = dao.loadListBySqlQuery(query, param);
            if (list != null ? !list.isEmpty() : false) {
                // 0- jour, 1-disponibilite, 2-thc, 3-ths, 4-tp, 5-ddebut, 6-vhs, 7-tva, 8-codva, 9-libva
                hebdomadaires.addAll(list);
            }
        }
    }
    /*
     END DASHBOARD PRESENCE 
     */

    /*
     BEGIN PROGRESSION
     */
    public void loadProgression(boolean nw) {
        tableauBord.setBarModel(new CartesianChartModel());
        if (nw || progress.getLignes().isEmpty()) {
            String query = "select y.employe, y.code, y.nom, y.periode, y.rang, y.salaire, y.presence, y.conge, y.permission, y.is_total, y.is_footer from public.grh_et_progression_all(?,?,?,?,?,?,?) y where y.employe != 0 order by y.is_total, y.agence, y.nom, y.code, y.rang";
            if (!onEmploye) {
                tableauBord.getService().setId(0);
                tableauBord.getEmploye().setId(0);
                query = "select y.agence, y.code, y.nom, y.periode, y.rang, y.salaire, y.presence, y.conge, y.permission, y.is_total, y.is_footer from public.grh_et_progression_all(?,?,?,?,?,?,?) y where y.employe = 0 order by y.is_total, y.agence, y.nom, y.code, y.rang";
            }
            System.err.println("query " + query);
            progress.returnProgressionEmploye(currentAgence.getSociete().getId(), tableauBord.getAgence().getId(), tableauBord.getService().getId(), tableauBord.getEmploye().getId(), tableauBord.getDatePrec(), tableauBord.getDateSuiv(), tableauBord.getPeriode(), dao);
        }
        ChartSeries serie;
        for (int r = 0; r < progress.getLignes().size() - 1; r++) {
            Rows row = (Rows) progress.getLignes().get(r);
            serie = new ChartSeries(row.getValeur().toString());
            for (int c = 0; c < progress.getPeriodes().size() - 1; c++) {
                String col = progress.getPeriodes().get(c);
                serie.set(col, Double.valueOf(progress.valeur(r, c, tableauBord.getType()).toString()));
            }
            tableauBord.getBarModel().addSeries(serie);
        }
    }
    /*
     END PROGRESSION
     */

    public void downloadFraisMission() {

    }

    public void loadFraisMission() {
        tabMission.returnFraisMission(currentAgence.getSociete().getId(), agenceSelect.getId(), dateDebut, dateFin, tabMission.getNature(), periode, dao);
    }
}
