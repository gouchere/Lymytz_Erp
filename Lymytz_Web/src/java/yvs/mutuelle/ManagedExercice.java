/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedExercice extends Managed<Exercice, YvsBaseExercice> implements Serializable {

    @ManagedProperty(value = "#{beanExercice}")
    private Exercice exercice;
    private List<YvsBaseExercice> exercices;

    private String tabIds, input_reset;
    private boolean updateExercice;

    private PeriodeExercice periode = new PeriodeExercice();

    public ManagedExercice() {
        exercices = new ArrayList<>();
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public List<YvsBaseExercice> getExercices() {
        return exercices;
    }

    public void setExercices(List<YvsBaseExercice> exercices) {
        this.exercices = exercices;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getInput_reset() {
        return input_reset;
    }

    public void setInput_reset(String input_reset) {
        this.input_reset = input_reset;
    }

    public boolean isUpdateExercice() {
        return updateExercice;
    }

    public void setUpdateExercice(boolean updateExercice) {
        this.updateExercice = updateExercice;
    }

    public PeriodeExercice getPeriode() {
        return periode;
    }

    public void setPeriode(PeriodeExercice periode) {
        this.periode = periode;
    }

    @Override
    public Exercice recopieView() {
        Exercice e = new Exercice();
        e.setActif(exercice.isActif());
        e.setDateDebut((exercice.getDateDebut() != null) ? exercice.getDateDebut() : new Date());
        e.setDateFin((exercice.getDateFin() != null) ? exercice.getDateFin() : new Date());
        e.setId(exercice.getId());
        e.setReference(exercice.getReference());
        return e;
    }

    @Override
    public boolean controleFiche(Exercice bean) {
        if (bean.getDateDebut() == null) {
            getErrorMessage("Vous devez specifier la date de debut");
            return false;
        }
        if (bean.getDateFin() == null) {
            getErrorMessage("Vous devez specifier la date de fin");
            return false;
        }
        if (bean.getReference() == null || bean.getReference().equals("")) {
            getErrorMessage("Vous devez entrer la reference");
            return false;
        }
        if (Util.dateToCalendar(bean.getDateDebut()).after(Util.dateToCalendar(bean.getDateFin()))) {
            getErrorMessage("La date de fin doit preceder la date de debut");
            return false;
        }
        if (bean.getIntervalle() <= 0) {
            getErrorMessage("Veuillez entrer la durée d'une période de l'exercice");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(Exercice bean) {
        cloneObject(exercice, bean);
        Collections.sort(exercice.getPeriodes());
        setUpdateExercice(true);
    }

    @Override
    public void resetFiche() {
        resetFiche(exercice);
        exercice.setIntervalle(1);
        exercice.getPeriodes().clear();
        setUpdateExercice(false);
        periode = new PeriodeExercice();
        input_reset = "";
        tabIds = "";
        update("blog_form_exercice");
    }

    @Override
    public boolean saveNew() {
        if (input_reset != null && input_reset.equals("reset")) {
            setUpdateExercice(false);
            input_reset = "";
        }
        String action = isUpdateExercice() ? "Modification" : "Insertion";
        try {
            Exercice bean = recopieView();
            if (controleFiche(bean)) {
                YvsBaseExercice entity = UtilMut.buildExercice(bean, currentAgence.getSociete(), currentUser);
                if (!isUpdateExercice()) {
                    entity.setId(null);
                    entity = (YvsBaseExercice) dao.save1(entity);
                    bean.setId(entity.getId());
                    exercice.setId(entity.getId());
                    exercices.add(entity);
                    //enregistre les période  de cet exercice
                    savePeriodeExo(entity);
                } else {
                    dao.update(entity);
                    exercices.set(exercices.indexOf(entity), entity);
                }
                succes();
                setUpdateExercice(true);
                actionOpenOrResetAfter(this);
                update("data_exercice");
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    private void savePeriodeExo(YvsBaseExercice ex) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(ex.getDateDebut());
        YvsMutPeriodeExercice p;
        if (exercice.getIntervalle() <= 0) {
            exercice.setIntervalle(1);
        }
        if (exercice.getIntervalle() > 0) {
            while (cd.getTime().before(ex.getDateFin())) {
                p = new YvsMutPeriodeExercice();
                p.setCloture(false);
                p.setDateDebut(cd.getTime());
                cd.add(Calendar.MONTH, exercice.getIntervalle());
                cd.add(Calendar.DAY_OF_MONTH, -1);
                p.setDateFin(cd.getTime());
                p.setActif(ex.getActif());
                p.setAuthor(currentUser);
                p.setDateSave(new Date());
                p.setDateUpdate(new Date());
                p.setReferencePeriode(Constantes.dfML.format(cd.getTime()));
                p.setExercice(ex);
                p.setId(null);
                dao.save(p);
                cd.add(Calendar.DAY_OF_MONTH, 1);
                exercice.getPeriodes().add(p);
            }
            update("exo_table_periode");
        } else {
            getErrorMessage("la périodicité de l'Exercice est mal choisit");
        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsBaseExercice> list = new ArrayList<>();
                YvsBaseExercice bean;
                for (Long ids : l) {
                    bean = exercices.get(exercices.indexOf(ids.intValue()));
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                    if (exercice.getId() == bean.getId()) {
                        resetFiche();
                        update("blog_form_exercice");
                    }
                }
                exercices.removeAll(list);
                succes();
                update("data_exercice");

            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression exercice : " + ex.getMessage());
        }
    }

    public void deleteBean(YvsBaseExercice y) {
        try {
            if (y != null) {
                for (YvsMutPeriodeExercice p : y.getPeriodesMutuelles()) {
                    dao.delete(p);
                }
                dao.delete(y);
                exercices.remove(y);
                if (exercice.getId() == y.getId()) {
                    resetFiche();
                    update("blog_form_exercice");
                }
                succes();
                update("data_exercice");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression exercice : " + ex.getMessage());
        }
    }

    public void deletePeriode(YvsMutPeriodeExercice y) {
        try {
            if (y != null) {
                dao.delete(y);
                exercice.getPeriodes().remove(y);
                if (periode.getId() == y.getId()) {
                    periode = new PeriodeExercice();
                    update("exo_form_periode");
                }
                succes();
                update("exo_table_periode");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression exercice : " + ex.getMessage());
        }
    }

    public void activeExercice(YvsBaseExercice y) {
        try {
            if (y != null) {
                y.setActif(!y.getActif());
                dao.update(y);
                int idx = exercices.indexOf(y);
                if (idx > -1) {
                    exercices.set(idx, y);
                }
                succes();
                update("data_exercice");
            }
        } catch (Exception ex) {
            getErrorMessage("Activation Impossible !");
            System.err.println("error suppression exercice : " + ex.getMessage());
        }
    }

    public void cloturerExercice(YvsBaseExercice y) {
        try {
            if (y != null) {
                if (y.getCloturer() ? !autoriser("base_uncloture_exo") : !autoriser("base_cloture_exo")) {
                    openNotAcces();
                    return;
                }
                y.setCloturer(!y.getCloturer());
                dao.update(y);
                int idx = exercices.indexOf(y);
                if (idx > -1) {
                    exercices.set(idx, y);
                }
                succes();
                update("data_exercice");
            }
        } catch (Exception ex) {
            getErrorMessage("Activation Impossible !");
            System.err.println("error suppression exercice : " + ex.getMessage());
        }
    }

    public void cloturerPeriode(YvsMutPeriodeExercice y) {
        try {
            if (y != null) {
                y.setCloture(!y.getCloture());
                dao.update(y);
                int idx = exercice.getPeriodes().indexOf(y);
                if (idx > -1) {
                    exercice.getPeriodes().set(idx, y);
                }
                succes();
                update("exo_table_periode");
            }
        } catch (Exception ex) {
            getErrorMessage("Activation Impossible !");
            System.err.println("error suppression exercice : " + ex.getMessage());
        }
    }

    public void activerPeriode(YvsMutPeriodeExercice y) {
        try {
            if (y != null) {
                y.setActif(!y.getActif());
                dao.update(y);
                int idx = exercice.getPeriodes().indexOf(y);
                if (idx > -1) {
                    exercice.getPeriodes().set(idx, y);
                }
                succes();
                update("exo_table_periode");
            }
        } catch (Exception ex) {
            getErrorMessage("Activation Impossible !");
            System.err.println("error suppression exercice : " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            setUpdateExercice((ids != null) ? ids.length > 0 : false);
            if (isUpdateExercice()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsBaseExercice bean = exercices.get(exercices.indexOf(new YvsBaseExercice(id)));
                populateView(UtilMut.buildBeanExercice(bean));
                tabIds = "";
                input_reset = "";
                update("blog_form_exercice");
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseExercice bean = (YvsBaseExercice) ev.getObject();
            populateView(UtilMut.buildBeanExercice(bean));
            execute("collapseForm('exercice')");
            update("blog_form_exercice");
            tabIds = exercices.indexOf(bean) + "";
//            execute("onselectLine(" + tabIds + ",'exercice')");
            execute("oncollapsesForm('exercice')");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    Parametre parametreMutuelle;

    @Override
    public void loadAll() {
        loadAllExrecice();
        //paramètre mutuelle
        if (currentMutuel != null ? currentMutuel.getParamsMutuelle() != null : false) {
            parametreMutuelle = UtilMut.buildBeanParametre(currentMutuel.getParamsMutuelle());
            Date debut = (parametreMutuelle.getDebutMois() != null) ? parametreMutuelle.getDebutMois() : currentAgence.getSociete().getYvsParametreGrh().get(0).getDateDebutTraitementSalaire();
            Calendar c0 = Calendar.getInstance();
            c0.setTime(debut);
            //initialise les date
            if (!exercices.isEmpty()) {
                Calendar c = Calendar.getInstance();
                c.setTime(exercices.get(0).getDateFin());
                c.set(Calendar.DAY_OF_MONTH, c0.get(Calendar.DAY_OF_MONTH));
                exercice.setDateDebut(c.getTime());
                c.add(Calendar.MONTH, 12);
                c.add(Calendar.DAY_OF_MONTH, -1);
                exercice.setDateFin(c.getTime());
            } else {
                Calendar c = Calendar.getInstance();
                exercice.setDateDebut(c0.getTime());
                c.add(Calendar.MONTH, 12);
                exercice.setDateFin(c.getTime());
                c.add(Calendar.DAY_OF_MONTH, -1);
            }
        }
        tabIds = "";
        input_reset = "";
    }

    public void loadAllExrecice() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        exercices = dao.loadNameQueries("YvsBaseExercice.findAll", champ, val);
    }

    public void loadAllExreciceActif(Boolean actif) {
        if (actif == null) {
            loadAllExrecice();
        } else {
            champ = new String[]{"societe", "actif"};
            val = new Object[]{currentAgence.getSociete(), actif};
            exercices = dao.loadNameQueries("YvsBaseExercice.findByActifs", champ, val);
        }
    }

    public void loadPeriodeExoOnView(SelectEvent ev) {
        if (ev.getObject() != null) {
            YvsMutPeriodeExercice pe = (YvsMutPeriodeExercice) ev.getObject();
            periode.setActif(pe.getActif());
            periode.setCloture(pe.getCloture());
            periode.setDateDebut(pe.getDateDebut());
            periode.setDateFin(pe.getDateFin());
            periode.setId(pe.getId());
            periode.setReference(pe.getReferencePeriode());
        }
    }

    public void unLoadPeriodeExoOnView(UnselectEvent ev) {
        periode = new PeriodeExercice();
    }

    public void saveOrUpdatePeriode() {
        if (exercice != null ? exercice.getId() > 0 : false) {
            YvsMutPeriodeExercice y = new YvsMutPeriodeExercice(periode.getId());
            y.setActif(periode.isActif());
            y.setCloture(periode.isCloture());
            y.setDateDebut(periode.getDateDebut());
            y.setDateFin(periode.getDateFin());
            y.setReferencePeriode(periode.getReference());
            y.setExercice(new YvsBaseExercice(exercice.getId()));
            y.setDateUpdate(new Date());
            y.setAuthor(currentUser);
            if (periode.getId() <= 0) {
                y.setDateSave(new Date());
                y.setId(null);
                dao.save1(y);
                exercice.getPeriodes().add(0, y);
            } else {
                dao.update(y);
                int idx = exercice.getPeriodes().indexOf(y);
                if (idx >= 0) {
                    exercice.getPeriodes().set(idx, y);
                }
            }
            periode = new PeriodeExercice();
            succes();
        } else {
            getErrorMessage("Vous devez enregistrer l'exercice");
        }
    }

}
