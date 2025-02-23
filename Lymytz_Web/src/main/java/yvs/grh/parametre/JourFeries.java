/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.parametre;

import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class JourFeries extends DefaultScheduleEvent implements Serializable, ScheduleEvent {

    private long idJour;
    private Date journee = new Date();
    private String evenemet;
    private String description;
    private boolean allYear;
    private String labelBtnPage = "Création";
    private boolean displayButonOption, displayTable;
    private Date dateSave = new Date();

    /**
     * Creates a new instance of JourFeries
     *
     * @param id
     */
    public JourFeries(long id) {
        this.idJour = id;
    }

    public JourFeries(String titre, Date d) {
        setTitle(titre);
        setStartDate(d);
        setEndDate(d);
    }

    public JourFeries() {
    }

    public boolean isDisplayTable() {
        return displayTable;
    }

    public void setDisplayTable(boolean displayTable) {
        this.displayTable = displayTable;
    }

    public String getEvenemet() {
        return evenemet;
    }

    public void setEvenemet(String evenemet) {
        this.evenemet = evenemet;
    }

    @Override
    public String getTitle() {
        return super.getTitle(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getStartDate() {
        return super.getStartDate(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStartDate(Date startDate) {
        super.setStartDate(startDate); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Date getEndDate() {
        return super.getEndDate(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setEndDate(Date endDate) {
        super.setEndDate(endDate); //To change body of generated methods, choose Tools | Templates.
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdJour(long idJour) {
        this.idJour = idJour;
    }

    public long getIdJour() {
        return idJour;
    }

    public boolean isDisplayButonOption() {
        return displayButonOption;
    }

    public void setDisplayButonOption(boolean displayButonOption) {
        this.displayButonOption = displayButonOption;
    }

    public String getLabelBtnPage() {
        return labelBtnPage;
    }

    public void setLabelBtnPage(String labelBtnPage) {
        this.labelBtnPage = labelBtnPage;
    }

    public boolean isAllYear() {
        return allYear;
    }

    public void setAllYear(boolean allYear) {
        this.allYear = allYear;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public Date getJournee() {
        return journee;
    }

    public void setJournee(Date journee) {
        this.journee = journee;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (int) (this.idJour ^ (this.idJour >>> 32));
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
        final JourFeries other = (JourFeries) obj;
        if (this.idJour != other.idJour) {
            return false;
        }
        return true;
    }

}
