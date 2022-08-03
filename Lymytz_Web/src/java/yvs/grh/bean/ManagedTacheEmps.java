/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.taches.YvsGrhTacheEmps;
import yvs.entity.grh.taches.YvsGrhTaches;
import yvs.entity.grh.taches.YvsGrhMontantTache;
import yvs.entity.grh.taches.YvsGrhRegleTache;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.taches.YvsGrhRealisationTache;
import yvs.grh.UtilGrh;
import yvs.grh.bean.taches.RealiseTache;
import yvs.util.Managed;

/**
 *
 * @author LYMYTZ
 */
@SessionScoped
@ManagedBean
public class ManagedTacheEmps extends Managed<TacheEmps, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{tacheEmps}")
    private TacheEmps tacheEmps;
    private RegleDeTache regles = new RegleDeTache();
    private YvsGrhEmployes employe;
    private List<YvsGrhTacheEmps> listTacheEmps, listTacheEmpsSelect;
    private List<YvsGrhRegleTache> reglesTaches;
    private YvsGrhTacheEmps tacheEmpsSelect;
    private List<YvsGrhTaches> listTaches;
    private List<YvsGrhRealisationTache> tachesArealiser;
    private YvsGrhRealisationTache selectRealiseTache;
    private int idTemp = 0, widthTabTable = 700;
    private boolean updatePlanifTache;
    private Date dateSearch = new Date(), datePrec = new Date(), dateSuiv = new Date();
    private RealiseTache realiseTache = new RealiseTache();
    private String findEmploye, textFind;

    public ManagedTacheEmps() {
        reglesTaches = new ArrayList<>();
        listTaches = new ArrayList<>();
        listTacheEmpsSelect = new ArrayList<>();
        listTacheEmps = new ArrayList<>();
        tachesArealiser = new ArrayList<>();

    }

    public YvsGrhRealisationTache getSelectRealiseTache() {
        return selectRealiseTache;
    }

    public void setSelectRealiseTache(YvsGrhRealisationTache selectRealiseTache) {
        this.selectRealiseTache = selectRealiseTache;
    }

    public List<YvsGrhRealisationTache> getTachesArealiser() {
        return tachesArealiser;
    }

    public void setTachesArealiser(List<YvsGrhRealisationTache> tachesArealiser) {
        this.tachesArealiser = tachesArealiser;
    }

    public RealiseTache getRealiseTache() {
        return realiseTache;
    }

    public void setRealiseTache(RealiseTache realiseTache) {
        this.realiseTache = realiseTache;
    }

    public List<YvsGrhRegleTache> getReglesTaches() {
        return reglesTaches;
    }

    public void setReglesTaches(List<YvsGrhRegleTache> reglesTaches) {
        this.reglesTaches = reglesTaches;
    }

    public Date getDatePrec() {
        return datePrec;
    }

    public void setDatePrec(Date datePrec) {
        this.datePrec = datePrec;
    }

    public Date getDateSuiv() {
        return dateSuiv;
    }

    public void setDateSuiv(Date dateSuiv) {
        this.dateSuiv = dateSuiv;
    }

    public int getWidthTabTable() {
        return widthTabTable;
    }

    public void setWidthTabTable(int widthTabTable) {
        this.widthTabTable = widthTabTable;
    }

    public int getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(int idTemp) {
        this.idTemp = idTemp;
    }

    public Date getDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(Date dateSearch) {
        this.dateSearch = dateSearch;
    }

    public RegleDeTache getRegles() {
        return regles;
    }

    public void setRegles(RegleDeTache regles) {
        this.regles = regles;
    }

    public List<YvsGrhTacheEmps> getListTacheEmpsSelect() {
        return listTacheEmpsSelect;
    }

    public void setListTacheEmpsSelect(List<YvsGrhTacheEmps> listTacheEmpsSelect) {
        this.listTacheEmpsSelect = listTacheEmpsSelect;
    }

    public List<YvsGrhTacheEmps> getListTacheEmps() {
        return listTacheEmps;
    }

    public void setListTacheEmps(List<YvsGrhTacheEmps> listTacheEmps) {
        this.listTacheEmps = listTacheEmps;
    }

    public List<YvsGrhTaches> getListTaches() {
        return listTaches;
    }

    public void setListTaches(List<YvsGrhTaches> listTaches) {
        this.listTaches = listTaches;
    }

    public YvsGrhTacheEmps getTacheEmpsSelect() {
        return tacheEmpsSelect;
    }

    public void setTacheEmpsSelect(YvsGrhTacheEmps tacheEmpsSelect) {
        this.tacheEmpsSelect = tacheEmpsSelect;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public TacheEmps getTacheEmps() {
        return tacheEmps;
    }

    public void setTacheEmps(TacheEmps tacheEmps) {
        this.tacheEmps = tacheEmps;
    }

    public String getFindEmploye() {
        return findEmploye;
    }

    public void setFindEmploye(String findEmploye) {
        this.findEmploye = findEmploye;
    }

    public String getTextFind() {
        return textFind;
    }

    public void setTextFind(String textFind) {
        this.textFind = textFind;
    }

    /**
     * ******************************************************************
     *
     * @param ev
     */
    public void choixRegleTache(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                regles = UtilGrh.buildBeanRegleDeTache(reglesTaches.get(reglesTaches.indexOf(new YvsGrhRegleTache(id))));
            }
        }
    }

    public void choixTachesEmps(ValueChangeEvent ev) {
        if (ev != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                YvsGrhMontantTache mt = regles.getListeTache().get(regles.getListeTache().indexOf(new YvsGrhMontantTache(id)));
                tacheEmps.setTaches(UtilGrh.buildBeanMontantTache(mt));
            }
        }
    }

    public void affecterEmploye_(SelectEvent ev) {
        if (ev != null) {
            affecterEmploye((YvsGrhEmployes) ev.getObject(), false);
        }
    }

    public void affecterEmploye(YvsGrhEmployes emp, boolean update) {
        if (emp != null) {
            YvsGrhTacheEmps te = new YvsGrhTacheEmps();
            te.setActif(true);
            te.setAuthor(currentUser);
            te.setDateFin(tacheEmps.getDateFin());
            te.setEmploye(emp);
            te.setPlanificationPermanente(tacheEmps.getDatePlanification() == null);
            if (!update) {
                te.setTache(new YvsGrhMontantTache(tacheEmps.getTaches().getId()));
                te.setDatePlanification(new Date());
                te = (YvsGrhTacheEmps) dao.save1(te);
                tacheEmps.setId(te.getId());
                tacheEmps.getTaches().getEmployes().add(te);
            } else {
                System.err.println(tacheEmpsSelect.getId() + " - " + tacheEmps.getId());
                te.setId(tacheEmps.getId());
                te.setDatePlanification(tacheEmpsSelect.getDatePlanification());
                te.setTache(tacheEmpsSelect.getTache());
                dao.update(te);
                tacheEmps.getTaches().getEmployes().set(tacheEmps.getTaches().getEmployes().indexOf(te), te);
            }
            tacheEmpsSelect = null;
            update("table_planif_tacheEmps");
            update("form_update_planif");
            updatePlanifTache = false;
        }
    }

    public void updatePlanifEmployeTache() {
        affecterEmploye(tacheEmpsSelect.getEmploye(), true);
    }

    public void removeEmployeToPlanif(YvsGrhTacheEmps te) {
        try {
            te.setAuthor(currentUser);
            dao.delete(te);
            tacheEmps.getTaches().getEmployes().remove(te);
        } catch (Exception ex) {
            getErrorMessage("Impossible de déplanifié cet employé !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void loadToUpdatePlanifTacheEmps(SelectEvent ev) {
        if (ev != null) {
            YvsGrhTacheEmps te = (YvsGrhTacheEmps) ev.getObject();
            tacheEmps.setId(te.getId());
            tacheEmps.setDateFin(te.getDateFin());
            updatePlanifTache = true;
            update("form_update_planif");
        }
    }

    public void unselectTacheEmps() {
        tacheEmpsSelect = null;
    }

    public void planifieRealisationTache() {
        if (tacheEmpsSelect != null || selectRealiseTache != null) {
            YvsGrhRealisationTache realise = new YvsGrhRealisationTache();
            if (realiseTache.getDateRealisation() != null) {
                realise.setDateRealisation(realiseTache.getDateRealisation());
            } else {
                realise.setDateRealisation(new Date());
            }
            realise.setAuthor(currentUser);
            realise.setDebutRealisation(realiseTache.getDebutRealisation());
            realise.setFinRealisation(realiseTache.getFinRealisation());
            realise.setPourcentageValidation(realiseTache.getPourcentageValidation());
            realise.setQuantiteRealise(realiseTache.getQuantite());
            if (realiseTache.getEtatRealisation() != ' ') {
                realise.setStatut(realiseTache.getEtatRealisation());
            }
            if (realiseTache.getEtatRealisation() != 'T') {
                realise.setTache(tacheEmpsSelect);
            } else {
                realise.setTache(selectRealiseTache.getTache());
            }
            if (realiseTache.getId() <= 0) {
                realise = (YvsGrhRealisationTache) dao.save1(realise);
                tachesArealiser.add(0, realise);
            } else {
                realise.setId(realiseTache.getId());
                dao.update(realise);
                tachesArealiser.set(tachesArealiser.indexOf(realise), realise);
            }
        } else {
            getErrorMessage("Aucune tâche planifié n'a été selectionné !");
        }

    }

    public void planifieRealisationTache_(YvsGrhTacheEmps te) {
        realiseTache = new RealiseTache();
        tacheEmpsSelect = te;
        openDialog("dlgPlanifTAR");
    }

    public void changeStatutRealisationTache(char statut) {
        if (statut == 'T') {
            realiseTache.setEtatRealisation(statut);
            realiseTache.setDateRealisation(selectRealiseTache.getDateRealisation());
            realiseTache.setDebutRealisation(selectRealiseTache.getDebutRealisation());
            realiseTache.setFinRealisation(selectRealiseTache.getFinRealisation());
            realiseTache.setId(selectRealiseTache.getId());
            realiseTache.setQuantite(selectRealiseTache.getQuantiteRealise());
            realiseTache.setTacheEmploye(selectRealiseTache.getTache());
            openDialog("dlgPlanifTAR");
        } else {
            selectRealiseTache.setAuthor(currentUser);
            selectRealiseTache.setStatut(statut);
            dao.update(selectRealiseTache);
        }
    }

    public void loadAllTaches() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        //charge les règles de tâches
        reglesTaches = dao.loadNameQueries("YvsRegleTache.findAll", champ, val);
        //charge les tâches à réaliser
        tachesArealiser = dao.loadNameQueries("YvsGrhRealisationTache.findAll", champ, val);
    }

    public void findEmployeFromTacheEmps() {
        if (findEmploye != null && tacheEmps.getTaches().getId() > 0) {
            champ = new String[]{"employe", "idTache"};
            val = new Object[]{"%" + findEmploye + "%", tacheEmps.getTaches().getId()};
            tacheEmps.getTaches().setEmployes(dao.loadNameQueries("YvsTacheEmps.findByEmploye", champ, val));
        } else if (tacheEmps.getTaches().getId() > 0) {
            champ = new String[]{"idTache"};
            val = new Object[]{tacheEmps.getTaches().getId()};
            tacheEmps.getTaches().setEmployes(dao.loadNameQueries("YvsTacheEmps.findByTache", champ, val));
        }
    }

    public void findEmployeFromTacheRealise() {
        if (textFind != null && tacheEmps.getTaches().getId() > 0) {
            champ = new String[]{"employe", "idTache"};
            val = new Object[]{"%" + findEmploye + "%", tacheEmps.getTaches().getId()};
            tacheEmps.getTaches().setEmployes(dao.loadEntity("YvsTacheEmps.findByEmploye", champ, val));
        } else if (tacheEmps.getTaches().getId() > 0) {
            champ = new String[]{"idTache"};
            val = new Object[]{tacheEmps.getTaches().getId()};
            tacheEmps.getTaches().setEmployes(dao.loadNameQueries("YvsTacheEmps.findByTache", champ, val));
        }
    }

    /**
     * recherche par critère libre*
     */
    private boolean findTahe, findEmps, findStatut, findDate;
    private long idTache;
    private char statutFind;
    private Date dateFind;

    public boolean isFindTahe() {
        return findTahe;
    }

    public void setFindTahe(boolean findTahe) {
        this.findTahe = findTahe;
    }

    public boolean isFindEmps() {
        return findEmps;
    }

    public void setFindEmps(boolean findEmps) {
        this.findEmps = findEmps;
    }

    public boolean isFindStatut() {
        return findStatut;
    }

    public void setFindStatut(boolean findStatut) {
        this.findStatut = findStatut;
    }

    public boolean isFindDate() {
        return findDate;
    }

    public void setFindDate(boolean findDate) {
        this.findDate = findDate;
    }

    public long getIdTache() {
        return idTache;
    }

    public void setIdTache(long idTache) {
        this.idTache = idTache;
    }

    public char getStatutFind() {
        return statutFind;
    }

    public void setStatutFind(char statutFind) {
        this.statutFind = statutFind;
    }

    public Date getDateFind() {
        return dateFind;
    }

    public void setDateFind(Date dateFind) {
        this.dateFind = dateFind;
    }

    public String buildQuery() {
        StringBuilder query = new StringBuilder("SELECT y FROM YvsGrhRealisationTache y WHERE ");
        textFind = (textFind == null) ? "" : "%" + textFind + "%";
        query.append((findTahe) ? "y.tache.tache.id=:idTache" : "");
        query.append((findTahe) ? " AND " : "").append((findEmps) ? "(y.tache.employe.nom LIKE :employe OR y.tache.employe.prenom LIKE :employe OR y.tache.employe.matricule LIKE :employe)" : "");
        query.append(((findTahe || findEmps) && findStatut) ? " AND " : "").append((findStatut) ? " y.statut=:statut " : "");
        query.append(((findTahe || findEmps || findStatut) && findDate) ? " AND " : "").append((findDate) ? " y.dateRealisation=:dateRealise " : "");
        if (!findTahe && (findEmps || findStatut || findDate)) {
            query.append(" AND y.author.agence.societe=:societe");
        } else if (!findTahe && !findEmps && !findStatut && !findDate) {
            return null;
        }
        //plassons les paramètres
        if (findTahe) {
            champ = new String[]{"idTache"};
            val = new Object[]{idTache};
        }
        if (findEmps) {
            if (findTahe) {
                champ = new String[]{"idTache", "employe"};
                val = new Object[]{idTache, textFind};
            } else {
                champ = new String[]{"societe", "employe"};
                val = new Object[]{currentUser.getAgence().getSociete(), textFind};
            }
        }
        if (findStatut) {
            if (findEmps) {
                if (findTahe) {
                    champ = new String[]{"idTache", "employe", "statut"};
                    val = new Object[]{idTache, textFind, statutFind};
                } else {
                    champ = new String[]{"societe", "employe", "statut"};
                    val = new Object[]{currentUser.getAgence().getSociete(), textFind, statutFind};
                }
            } else {
                champ = new String[]{"societe", "statut"};
                val = new Object[]{currentUser.getAgence().getSociete(), statutFind};
            }
        }
        if (findDate) {
            if (findStatut) {
                if (findEmps) {
                    if (findTahe) {
                        champ = new String[]{"idTache", "employe", "statut", "dateRealise"};
                        val = new Object[]{idTache, textFind, statutFind, dateFind};
                    } else {
                        champ = new String[]{"societe", "employe", "statut", "dateRealise"};
                        val = new Object[]{currentUser.getAgence().getSociete(), textFind, statutFind, dateFind};
                    }
                } else {
                    champ = new String[]{"societe", "statut", "dateRealise"};
                    val = new Object[]{currentUser.getAgence().getSociete(), statutFind, dateFind};
                }
            } else {
                champ = new String[]{"societe", "dateRealise"};
                val = new Object[]{currentUser.getAgence().getSociete(), dateFind};
            }
        }
        System.err.println("--- Query " + query.toString());
        System.err.println("-Param-- " + champ.toString());
        System.err.println("-Value-- " + val.toString());
        tachesArealiser = dao.loadEntity(query.toString(), champ, val);
        return query.toString();
    }

    @Override
    public boolean controleFiche(TacheEmps bean) {
//        if (bean.getEmploye().getMatricule() == null) {
//            getMessage("Vous associer un employe 1", FacesMessage.SEVERITY_ERROR);
//            return false;
//        }
        if (bean.getTaches().getCodeTache() == null) {
            getMessage("Vous impliquer une tache", FacesMessage.SEVERITY_ERROR);
            return false;
        }
        return true;
    }

    public Employe recopieViewEmploye(Employe bean) {
        Employe emp = new Employe(bean.getId(), bean.getNom());
        emp.setPrenom(bean.getPrenom());
        emp.setMatricule(bean.getMatricule());
        emp.setContrat(bean.getContrat());
        emp.getContrat().setRegleTache(bean.getContrat().getRegleTache());
        return emp;
    }

    public Taches recopieViewTacheEmps(YvsGrhMontantTache bean) {
        Taches t = new Taches(bean.getTaches().getId());
        t.setAttribuer(true);
        t.setCodeTache(bean.getTaches().getCodeTache());
        t.setDescription(bean.getTaches().getDescription());
        t.setDesignation(bean.getTaches().getModuleTache());
        t.setMontant(bean.getMontant());
        t.setPrimeTache((bean.getPrimeTache() != null && bean.getPrimeTache().getId() != 0) ? new PrimeTache(bean.getPrimeTache().getId()) : null);
        t.setSelectActif(false);
        return t;
    }

    @Override
    public TacheEmps recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
//    public YvsGrhTacheEmps buildTacheEmps(TacheEmps tache) {
//        YvsGrhTacheEmps t = new YvsGrhTacheEmps();
//        t.setActif(tache.isActif());
//        t.setDateRealisation((tache.getDateRealisation() != null) ? tache.getDateRealisation() : new Date());
//        t.setDateDebut((tache.getDateDebut() != null) ? tache.getDateDebut() : new Date());
//        t.setDateFin((tache.getDateFin() != null) ? tache.getDateFin() : new Date());
//        t.setEmploye(new YvsGrhEmployes(tache.getEmploye().getId()));
//        t.setStatutTache((tache.getEtat() != null) ? tache.getEtat() : "RAS");
//        t.setId(tache.getId());
//        t.setQuantite((tache.getQuantite() != 0) ? tache.getQuantite() : 1);
//        t.setSupp(tache.isSupp());
////        t.setTache(new YvsGrhMontantTache(tache.getTaches()..getId()));
//        return t;
//    }

    @Override
    public void populateView(TacheEmps bean) {
        if (bean != null) {
            cloneObject(tacheEmps, bean);
            tacheEmps.setDatePlanification(dateSearch);
        }
    }

    public void populateViewTache(Taches bean) {
        cloneObject(tacheEmpsSelect, bean);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        YvsGrhTacheEmps bean = (YvsGrhTacheEmps) ev.getObject();
        update("tab-tacheEmp2");
    }

    public void loadOnViewTache(SelectEvent ev) {
        populateViewTache((Taches) ev.getObject());

    }

    @Override
    public void loadAll() {
//        listTacheEmps.clear();
//        champ = new String[]{"pontuelle"};
//        val = new Object[]{true};
//        listTacheEmps = dao.loadNameQueries("YvsTacheEmps.findByPontuelle", champ, val);
        //charge les règles de tâches
    }

    public void loadAllPontuelle() {
//        listTacheEmps.clear();
//        champ = new String[]{"dateFin", "pontuelle"};
//        val = new Object[]{new Date(), true};
//        listTacheEmps = UtilGrh.buildBeanListTacheEmp(dao.loadNameQueries("YvsTacheEmps.findByDateFinO", champ, val));
    }

    public void loadAllJournalier() {
        listTacheEmps.clear();
        champ = new String[]{"pontuelle", "dateRealisation"};
        val = new Object[]{false, dateSearch};
        listTacheEmps = dao.loadNameQueries("YvsTacheEmps.findByDateRealisation", champ, val);

    }

    public void loadAllTacheOneEmploye() {
        if (employe.getMatricule() != null) {
            listTacheEmps.clear();
            champ = new String[]{"pontuelle", "employe"};
            val = new Object[]{true, new YvsGrhEmployes(employe.getId())};
//            listTacheEmps = UtilGrh.buildBeanListTacheEmp(dao.loadNameQueries("YvsGrhTacheEmps.findByEmploye", champ, val));
            listTacheEmps = dao.loadNameQueries("YvsGrhTacheEmps.findByEmploye", champ, val);
            update("tab-tacheEmp");
        }
    }

//    public void loadAllEmployes() {
//        if (tacheEmps.getDateRealisation() == null) {
//            if (dateSearch == null) {
//                dateSearch = new Date();
//            }
//            tacheEmps.setDateRealisation(dateSearch);
//        }
//        if (tacheEmps == null || tacheEmps.getTaches() == null) {
//            tacheEmps.setTaches(new Taches());
//        }
//        listEmployes.clear();
//        listTacheEmps.clear();
//        champ = new String[]{"agence"};
//        val = new Object[]{currentAgence};
//        listEmployes = UtilGrh.buildBeanListSimpleEmploye(dao.loadNameQueries("YvsGrhEmployes.findAll", champ, val));
//        String[] champ1 = new String[]{"tache"};
//        Object[] val1 = new Object[]{tacheEmps.getTaches().getId()};
//        List<Taches> list1 = UtilGrh.buildBeanListMontantTache(dao.loadNameQueries("YvsMontantTache.findByTache", champ1, val1));
//        for (Taches t : list1) {
//            for (Employe e : listEmployes) {
//                if (e.getContrat().getRegleTache().equals(t.getRegleTache())) {
//                    String[] champ2 = new String[]{"employe", "dateRealisation", "tache", "pontuelle"};
//                    Object[] val2 = new Object[]{new YvsGrhEmployes(e.getId()), dateSearch, new YvsGrhTaches(tacheEmps.getTaches().getId()), false};
//                    TacheEmps t1 = UtilGrh.buildBeanTacheEmps((YvsGrhTacheEmps) dao.loadOneByNameQueries("YvsTacheEmps.findByRealiser", champ2, val2));
//                    if (t1.getId() == 0) {
//                        idTemp += 1;
//                        t1.setId(idTemp);
//                        t1.setEmploye(e);
//                        t1.setTaches(tacheEmps.getTaches());
//                        t1.setAttribuer(false);
//                    } else {
//                        idTemp = (int) t1.getId() + 1;
//                        t1.setAttribuer(true);
//                    }
//                    listTacheEmps.add(t1);
//                }
//            }
//        }
//        update("blog-tache-E01");
//    }
    @Override
    public boolean saveNew() {
//        if (controleFiche(tacheEmps)) {
//            if (!updatePlanifTache) {
//                currentTacheEmp = (YvsGrhTacheEmps) dao.save1(currentTacheEmp);
//                tacheEmps.setId(currentTacheEmp.getId());
//                TacheEmps t = new TacheEmps();
//                cloneObject(t, tacheEmps);
//                listTacheEmps.add(0, currentTacheEmp);
//            } else {
//                dao.update(currentTacheEmp);
//                TacheEmps t = new TacheEmps();
//                cloneObject(t, tacheEmps);
//                listTacheEmps.set(listTacheEmps.indexOf(currentTacheEmp), currentTacheEmp);
//            }
//            updatePlanifTache = true;
//        }
        return true;
    }

    public void saveNewTacheJournalier() {
        for (YvsGrhTacheEmps t : listTacheEmpsSelect) {
            t.setDatePlanification(dateSearch);
            cloneObject(tacheEmps, t);
            saveNew();
            updatePlanifTache = false;
        }
        listTacheEmpsSelect.clear();
        succes();
        update("form-tache-E04");
    }

    @Override
    public void updateBean() {

    }

    @Override
    public void deleteBean() {
        if (listTacheEmpsSelect != null) {
            if (!listTacheEmpsSelect.isEmpty()) {
                for (YvsGrhTacheEmps t : listTacheEmpsSelect) {
                    t.setAuthor(currentUser);
                    dao.delete(t);
                    listTacheEmps.remove(t);
                }
            }
            succes();
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(tacheEmps);
        tacheEmps.setTaches(new Taches());
        listTacheEmpsSelect.clear();
        resetFiche(employe);
        update("form-tache-E03");
        update("form-tache-E04");
    }

    public boolean saveNewMontantTache() {

        return true;
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
