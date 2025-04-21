/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.parametre;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import yvs.entity.grh.param.YvsJoursFeries;
import yvs.util.Managed;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ManagedJourFeries extends Managed<JourFeries, YvsBaseCaisse> implements Serializable {

    private long id;
    private ScheduleModel eventModel;
    private JourFeries event = new JourFeries();
    private boolean allYear;
    private boolean displayTable;
    private List<JourFeries> listValue;

    /**
     * Creates a new instance of JourFeries
     *
     * @param id
     */
    public ManagedJourFeries(long id) {
        this.id = id;
    }

    public ManagedJourFeries() {
        eventModel = new DefaultScheduleModel();
        listValue = new ArrayList<>();

    }

    public List<JourFeries> getListValue() {
        return listValue;
    }

    public void setListValue(List<JourFeries> listValue) {
        this.listValue = listValue;
    }

    public boolean isDisplayTable() {
        return displayTable;
    }

    public void setDisplayTable(boolean displayTable) {
        this.displayTable = displayTable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    } 

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public JourFeries getEvent() {
        return event;
    }

    public void setEvent(JourFeries event) {
        this.event = event;
    }

    public boolean isAllYear() {
        return allYear;
    }

    public void setAllYear(boolean allYear) {
        this.allYear = allYear;
    }

    YvsJoursFeries jourF;

    public void openToDeleteJF(JourFeries jf) {
        jourF = buildDay(jf);
        openDialog("dlgConfirmdelete");
    }

    public void deleteFerie() {
        if (jourF != null) {
            try {
                dao.delete(jourF);
                listValue.remove(new JourFeries(jourF.getId()));
                eventModel.deleteEvent(event);
                event = new JourFeries();
                 jourF = null;
                if (!displayTable) {
                    refresh("JF");
                } else {
                    update("tab-jf-list");
                }
                succes();
            } catch (Exception ex) {
                getMessage("on effectué !", FacesMessage.SEVERITY_ERROR);
            }
        }
    }

    public void addEvent(ActionEvent actionEvent) {
        if ((event.getTitle() == null) ? true : event.getTitle().trim().equals("")) {
            //supprime l'évènement
            if (jourF != null) {
                openDialog("dlgDelF");
            }
        } else {
            if (!controleFiche()) {
                //confirme le changement de date pour un titre
                openDialog("dlgConfirm2");
            } else if (!controleFicheDate(this)) {
                //confirme le changement de titre pour une date
                openDialog("dlgConfirm1");
            } else if (controleNochange(this)) {
                //save new 
                eventModel.addEvent(event);
                YvsJoursFeries j = (YvsJoursFeries) dao.save1(buildDay(event));
                eventModel.updateEvent(event);
                JourFeries jf = new JourFeries(j.getId());
                jf.setDescription(j.getCommentaire());
                jf.setEvenemet(event.getTitle());
                jf.setJournee(j.getJour());
                jf.setDateSave(j.getDateSave());
                listValue.add(jf);
                succes();
            }
        }
        update("global-View-JF");
        update("global-View-JF_data");
    }

    public void updateCalDay() {
        champ = new String[]{"titre", "societe"};
        val = new Object[]{event.getTitle(), currentAgence.getSociete()};
        dao.setEntityClass(YvsJoursFeries.class);
        YvsJoursFeries jf = (YvsJoursFeries) dao.getOne(champ, val);
        if (jf != null) {
            jf.setJour(event.getStartDate());
            dao.update(jf);
//            eventModel.updateEvent(event);
            loadAll();
            succes();
            update("global-View-JF");
        }
    }

    public void updateCalTitle() {
        System.err.println("UPDATE");
        champ = new String[]{"jour", "societe"};
        val = new Object[]{event.getStartDate(), currentAgence.getSociete()};
        dao.setEntityClass(YvsJoursFeries.class);
        YvsJoursFeries jf = (YvsJoursFeries) dao.getOne(champ, val);
        if (jf != null) {
            jf.setTitre(event.getTitle());
            dao.update(jf);
//            eventModel.updateEvent(event);
            loadAll();
            succes();
            update("global-View-JF");
        }
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (JourFeries) selectEvent.getObject();
        if (event.isAllDay()) {
        }
        getInfoMessage(event.getTitle());
//        loadDetails((Date) event.getEndDate());
        update("eventDetails");
    }

    public void onDateSelect(SelectEvent selectEvent) {
        //cherche s'il y a un jourférié à cette date
        Date e = (Date) selectEvent.getObject();
        loadDetails(e);
    }

    private void loadDetails(Date e) {
        champ = new String[]{"jour", "societe"};
        val = new Object[]{e, currentAgence.getSociete()};
        jourF = (YvsJoursFeries) dao.loadOneByNameQueries("YvsJoursFeries.findByJour", champ, val);
        if (jourF != null) { 
            event = new JourFeries(jourF.getTitre(), e);
            event.setDescription(jourF.getCommentaire());
            event.setDateSave(jourF.getDateSave());
            update("j-ferie-gridButon");
        } else {
            event = new JourFeries("", e);
        }
        update("eventDetails");
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        getInfoMessage("Event moved, Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private YvsJoursFeries buildDay(JourFeries ev) {
        YvsJoursFeries jf = new YvsJoursFeries();
        jf.setJour(ev.getStartDate());
        jf.setCommentaire(ev.getTitle());
        jf.setSociete(currentAgence.getSociete());
        jf.setTitre(ev.getTitle());
        jf.setAuthor(currentUser);
        jf.setAllYear(ev.isAllYear());
        Calendar c = Calendar.getInstance();
        c.setTime(ev.getStartDate());
        jf.setDateUpdate(new Date());
        jf.setDateUpdate(ev.getDateSave());
        return jf;
    }

    private YvsJoursFeries buildShedule(JourFeries ev) {
        YvsJoursFeries jf = new YvsJoursFeries();
        jf.setJour(ev.getStartDate());
        jf.setCommentaire(ev.getDescription());
        jf.setSociete(currentAgence.getSociete());
        jf.setTitre(ev.getTitle());
        jf.setDateSave(ev.getDateSave());
        return jf;
    }

    public boolean controleFiche() {
        //contrôle s'il exite djà un jour férié de même dénomination. de plus, on ne peut cumuler deux férié le même jour
        champ = new String[]{"titre", "jour", "societe"};
        val = new Object[]{event.getTitle(), event.getStartDate(), currentAgence.getSociete()};
        Long nbre = (Long) dao.loadObjectByNameQueries("YvsJoursFeries.countByTitre", champ, val);
        long d = (nbre != null) ? nbre : 0;
        return d == 0;
    }

    @Override
    public boolean controleFiche(JourFeries bean) {
//        //contrôle s'il exite djà un jour férié de même dénomination. de plus, on ne peut cumuler deux férié le même jour
//         champ = new String[]{"titre", "jour", "societe"};
//        val = new Object[]{event.getTitle(), event.getStartDate(), currentAgence.getSociete()};
//        Long nbre = (Long) dao.loadObjectByNameQueries("YvsJoursFeries.countByTitre", champ, val);
//        long d = (nbre != null) ? nbre : 0;
        return true;
    }

    public boolean controleFicheDate(ManagedJourFeries bean) {
        //contrôle s'il exite djà un jour férié de même dénomination. de plus, on ne peut cumuler deux férié le même jour
        champ = new String[]{"titre", "jour", "societe"};
        val = new Object[]{event.getTitle(), event.getStartDate(), currentAgence.getSociete()};
        Long nbre = (Long) dao.loadObjectByNameQueries("YvsJoursFeries.countByDate", champ, val);
        long d = (nbre != null) ? nbre : 0;
        return d == 0;
    }

    public boolean controleNochange(ManagedJourFeries bean) {
        //contrôle s'il exite djà un jour férié de même dénomination. de plus, on ne peut cumuler deux férié le même jour
        champ = new String[]{"titre", "jour", "societe"};
        val = new Object[]{event.getTitle(), event.getStartDate(), currentAgence.getSociete()};
        Long nbre = (Long) dao.loadObjectByNameQueries("YvsJoursFeries.countByDateAndTitre", champ, val);
        long d = (nbre != null) ? nbre : 0;
        return d == 0;
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JourFeries recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(JourFeries bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll() {
        //charge les jours fériés enregistré
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        List<YvsJoursFeries> l = dao.loadNameQueries("YvsJoursFeries.findAll", champ, val);
        eventModel.clear();
        listValue.clear();
        for (YvsJoursFeries d : l) {
            event = new JourFeries(d.getTitre(), d.getJour());
            event.setAllYear(d.isAllYear());
            event.setDateSave(d.getDateSave());
            eventModel.addEvent(event);
//            //si la journée est un férié permanent, ajouter l'évènement sur 10 ans avant et après la date courante
            if (d.isAllYear()) {
                Calendar c = Calendar.getInstance();
                c.setTime(d.getJour());
                int year = c.get(Calendar.YEAR);
                for (int i = year - 20; i <= year + 20; i++) {
                    if (i != year) {
                        c.set(Calendar.YEAR, i);
                        event = new JourFeries(d.getTitre(), c.getTime());
                        eventModel.addEvent(event);
                    }
                }
            }
            JourFeries jf = new JourFeries();
            jf.setJournee(d.getJour());
            jf.setEvenemet(d.getTitre());
            jf.setDescription(d.getCommentaire());
            jf.setIdJour(d.getId());
            jf.setAllYear(d.isAllYear());
            jf.setDateSave(d.getDateSave());
            listValue.add(jf);
        }
    }

    public void selectLineJF(SelectEvent ev) {
        JourFeries jf = (JourFeries) ev.getObject();
        event = new JourFeries(jf.getEvenemet(), jf.getJournee());
        jourF = buildShedule(event);
        jourF.setId(jf.getIdJour());
        event.setDescription(jf.getDescription());
        event.setDateSave(jf.getDateSave());
        event.setAllYear(jf.isAllYear());
        if (jourF != null) {
             event = new JourFeries(jourF.getTitre(), jf.getJournee());
            update("j-ferie-gridButon");
        } else {
            event = new JourFeries("", jf.getJournee());
        }
        update("eventDetails");
        update("global-View-JF");
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ManagedJourFeries other = (ManagedJourFeries) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
