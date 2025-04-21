/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.presence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.grh.Calendrier;
import yvs.grh.JoursOuvres;
import yvs.grh.UtilGrh;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Utilitaire;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedCalendrier extends Managed<Calendrier, YvsCalendrier> implements Serializable {

    private Calendrier calendrier = new Calendrier();
    private JoursOuvres jourOuv = new JoursOuvres();
    private List<YvsCalendrier> calendriers;
    private YvsParametreGrh param = new YvsParametreGrh();
    private String module = Constantes.MOD_GRH;

    private boolean initForm = true;   //pour empecher la navigation au raffraichissement de la page

    public ManagedCalendrier() {
        calendriers = new ArrayList<>();
    }

    public String getModule() {
        return module != null ? (module.trim().length() > 0 ? module : Constantes.MOD_GRH) : Constantes.MOD_GRH;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier = calendrier;
    }

    public List<YvsCalendrier> getCalendriers() {
        return calendriers;
    }

    public void setCalendriers(List<YvsCalendrier> calendriers) {
        this.calendriers = calendriers;
    }

    public JoursOuvres getJourOuv() {
        return jourOuv;
    }

    public void setJourOuv(JoursOuvres jourOuv) {
        this.jourOuv = jourOuv;
    }

    @Override
    public boolean controleFiche(Calendrier bean) {
        if (calendrier.getReference() == null) {
            getErrorMessage("Vous devez indiquer une référence pour le calendrier !");
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        //supprime le calendrier
        if (calendrier.getId() > 0) {
            try {
                YvsCalendrier c = new YvsCalendrier(calendrier.getId());
                c.setAuthor(currentUser);
                dao.delete(c);
                if (calendriers.contains(c)) {
                    calendriers.remove(c);
                }
                succes();
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer !");
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    public void openDlgDelCal(YvsCalendrier c) {
        System.err.println(" ---- Calandrier "+c.getId());
        populateView(UtilGrh.buildBeanCalendrier(c));
//        openDialog("dlgDelCal");
    }

    @Override
    public void updateBean() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Calendrier recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Calendrier bean) {
        cloneObject(calendrier, bean);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        YvsCalendrier ca = (YvsCalendrier) ev.getObject();
        populateView(UtilGrh.buildBeanCalendrier(ca));
        calendrier.setListJoursOuvres(dao.loadNameQueries("YvsJoursOuvres.findByCalendrier", new String[]{"calendrier"}, new Object[]{ca}));
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        jourOuv = new JoursOuvres();
    }

    public void loadJourOnView(SelectEvent ev) {
        if (ev != null) {
            YvsJoursOuvres jo = (YvsJoursOuvres) ev.getObject();
            cloneObject(jourOuv, UtilGrh.buildBeanJoursOuvree(jo));
        }
    }

    @Override
    public void loadAll() {
        ParametreRequete p = new ParametreRequete("y.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        paginator.addParam(p);
        loadAllData(true, initForm);
        param = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findByDefaultHoraire", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});

    }

    public void loadAllActif(Boolean actif) {
        ParametreRequete p = new ParametreRequete("y.actif", "actif", actif, "=", "AND");
        paginator.addParam(p);
        loadAllData(true, initForm);
    }

    public void loadByModule(String module) {
        ParametreRequete p = new ParametreRequete("y.module", "module", null, "=", "AND");
        if (module != null ? module.trim().length() > 0 : false) {
            this.module = module;
            p = new ParametreRequete("y.module", "module", module, "=", "AND");
        }
        paginator.addParam(p);
        loadAll();
    }

    public void loadAllData(boolean avancer, boolean init) {
        calendriers = paginator.executeDynamicQuery("YvsCalendrier", "y.reference", avancer, init, (int) imax, dao);
    }

    public void pagineData(boolean avancer) {
        initForm = false;
        loadAllData(initForm, avancer);
    }

    public void changeMaxResult(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            imax = (long) ev.getNewValue();
            loadAllData(initForm, true);
        }
    }

    public boolean saveNew(boolean genrer) {
        saveNewCal(genrer);
        succes();
        return true;
    }

    public void resetForm() {
        calendrier = new Calendrier();
    }

    private YvsCalendrier saveNewCal(boolean generer) {
        if (controleFiche(calendrier)) {
            YvsCalendrier c = new YvsCalendrier();
            c.setActif(calendrier.isActif());
            c.setAuthor(currentUser);
            c.setDefaut(calendrier.isDefaut());
            c.setReference(calendrier.getReference());
            c.setSociete(currentAgence.getSociete());
            c.setTempsMarge(calendrier.getMarge());
            c.setModule(getModule());
            c.setDateUpdate(new Date());
            c.setDateSave(calendrier.getDateSave());
            if (calendrier.getId() <= 0) {
                c.setId(null);
                c.setDateSave(new Date());
                c = (YvsCalendrier) dao.save1(c);
                calendrier.setId(c.getId());
                calendriers.add(0, c);
                if (generer) {
                    saveCal(c);
                }
            } else {
                c.setId(calendrier.getId());
                dao.update(c);
                if (calendriers.contains(c)) {
                    calendriers.set(calendriers.indexOf(c), c);
                }
            }

            return c;
        }
        return null;
    }

    public void saveCal(YvsCalendrier cal) {
        try {
//            YvsParametreGrh p = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findByDefaultHoraire", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (param != null ? param.getId() > 0 : false) {
                List<String> days = new ArrayList<String>();
                days.add("Lundi");
                days.add("Mardi");
                days.add("Mercredi");
                days.add("Jeudi");
                days.add("Vendredi");
                days.add("Samedi");
                days.add("Dimanche");
                for (int i = 0; i < days.size(); i++) {
                    YvsJoursOuvres jo = new YvsJoursOuvres();
                    jo.setActif(true);
                    jo.setAuthor(currentUser);
                    jo.setCalendrier(cal);
                    jo.setDureePause(param.getTimeMargeRetard());
                    jo.setHeureDebutPause(param.getHeureDebutPause());
                    jo.setHeureFinTravail(param.getHeureFinTravail());
                    jo.setHeureDebutTravail(param.getHeureDebutTravail());
                    jo.setHeureFinPause(param.getHeureFinPause());
                    jo.setJour(days.get(i));
                    jo.setOrdre(UtilGrh.getOrdre(days.get(i)));
                    jo.setDateSave(new Date());
                    jo.setDateUpdate(new Date());
                    jo.setDateSave(new Date());
                    jo.setOuvrable(true);
                    jo.setJourDeRepos(false);
                    jo.setDureePause(Utilitaire.calculDureeBetweenDate(jo.getHeureDebutPause(), jo.getHeureFinPause()));
                    jo=(YvsJoursOuvres)dao.save1(jo);
                    cal.getJoursOuvres().add(jo);
                    calendrier.getListJoursOuvres().add(jo);
                }
            }

            update("tab_jours_ouvree");
        } catch (Exception e) {
            getErrorMessage("Erreur");
        }

    }

    public void saveHoraire() {
        try {
            param.setDateUpdate(new Date());
            param.setSociete(currentAgence.getSociete());
            if (param.getId() <= 0) {
                param.setId(null);
                param.setDefaultHoraire(true);
                param.setDateSave(new Date());
                dao.save(param);
            } else {
                dao.update(param);
            }
            succes();
        } catch (Exception e) {
            param = new YvsParametreGrh();
            e.printStackTrace();
            getErrorMessage("Erreur");
        }
    }

    public void addDayInCalndrier() {
        YvsCalendrier cal = saveNewCal(false);
        if (cal != null) {
            YvsJoursOuvres jo = new YvsJoursOuvres();
            jo.setActif(jourOuv.isActif());
            jo.setAuthor(currentUser);
            jo.setCalendrier(cal);            
            jo.setHeureDebutPause(jourOuv.getHeureDebutPause());
            jo.setHeureFinTravail(jourOuv.getHeureFinTravail());
            jo.setHeureDebutTravail(jourOuv.getHeureDebutTravail());
            jo.setHeureFinPause(jourOuv.getHeureFinPause());
            jo.setJour(jourOuv.getJour());
            jo.setJourDeRepos(jourOuv.isJourDerepos());
            jo.setOrdre(UtilGrh.getOrdre(jourOuv.getJour()));
            jo.setOuvrable(jourOuv.isOuvrable());
            jo.setDateSave(jourOuv.getDateSave());
            jo.setDateUpdate(new Date());
            jourOuv.setDureePause(Utilitaire.calculDureeBetweenDate(jourOuv.getHeureDebutPause(), jourOuv.getHeureFinPause()));
            jo.setDureePause(jourOuv.getDureePause());
            if (jourOuv.getId() <= 0) {
                jo.setId(null);
                jo.setDateSave(new Date());
                jo = (YvsJoursOuvres) dao.save1(jo);
                jourOuv.setId(jo.getId());
                calendrier.getListJoursOuvres().add(0, jo);
            } else {
                jo.setId(jourOuv.getId());
                dao.update(jo);
                int idx = calendrier.getListJoursOuvres().indexOf(jo);
                if (idx >= 0) {
                    calendrier.getListJoursOuvres().set(idx, jo);
                }
            }
            jourOuv = new JoursOuvres();
            succes();
        } else {
            getErrorMessage("Aucun calendrier selectionné !");
        }

    }

    public void toogleActiveCalendrier(YvsCalendrier c) {
        c.setAuthor(currentUser);
        c.setActif(!c.getActif());
        dao.update(c);
        if (calendriers.contains(c)) {
            calendriers.set(calendriers.indexOf(c), c);
        }
    }

    public void toogleActiveJour(YvsJoursOuvres j) {
        j.setAuthor(currentUser);
        j.setActif(!j.getActif());
        dao.update(j);
        int idx = calendrier.getListJoursOuvres().indexOf(new JoursOuvres(j.getId().intValue()));
        if (idx >= 0) {
            calendrier.getListJoursOuvres().get(idx).setActif(j.getActif());
        }
    }

    public void toogleActiveJourRepos(YvsJoursOuvres j) {
        j.setAuthor(currentUser);
        j.setJourDeRepos(!j.getJourDeRepos());
        dao.update(j);
        int idx = calendrier.getListJoursOuvres().indexOf(new JoursOuvres(j.getId().intValue()));
        if (idx >= 0) {
            calendrier.getListJoursOuvres().get(idx).setJourDeRepos(j.getJourDeRepos());
        }
    }

    public void toogleActiveJourOuvrable(YvsJoursOuvres j) {
        j.setAuthor(currentUser);
        j.setOuvrable(!j.getOuvrable());
        dao.update(j);
        int idx = calendrier.getListJoursOuvres().indexOf(new JoursOuvres(j.getId().intValue()));
        if (idx >= 0) {
            calendrier.getListJoursOuvres().get(idx).setOuvrable(j.getOuvrable());
        }
    }

    public void deleteJour(YvsJoursOuvres jo) {
        if (jo.getId() > 0) {
            try {
                jo.setAuthor(currentUser);
                dao.delete(jo);
                int idx = calendrier.getListJoursOuvres().indexOf(jo);
                if (idx >= 0) {
                    calendrier.getListJoursOuvres().remove(idx);
                }
                succes();
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer !");
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    public void eventCalculDureeTravail() {
        if (jourOuv != null) {
            if (jourOuv.getHeureFinTravail() != null && jourOuv.getHeureDebutTravail() != null) {
                calendrier.setTotalHeureHebdo(calendrier.getTotalHeureHebdo() + ((double) (jourOuv.getHeureFinTravail().getTime() - jourOuv.getHeureDebutTravail().getTime()) / (1000 * 60 * 60)));
            } else {
                calendrier.setTotalHeureHebdo(calendrier.getTotalHeureHebdo() + 0.0);
            }
        }
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public YvsParametreGrh getParam() {
        return param;
    }

    public void setParam(YvsParametreGrh param) {
        this.param = param;
    }

}
