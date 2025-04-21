/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.creneau;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultScheduleEvent;
import static yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers.ecartOnDate;
import yvs.users.Users;
import static yvs.util.Util.dateToCalendar;
import static yvs.util.Util.getTimeStamp;
import static yvs.util.Util.getTimeToString;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class CreneauUsers extends DefaultScheduleEvent implements Serializable {

    private long id_;
    private Date dateTravail = new Date();
    private String jour;
    private String description;
    private String type;
    private boolean actif = true;
    private Users personnel = new Users();
    private Creneau creneauDepot = new Creneau();
    private Creneau creneauPoint = new Creneau();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private boolean in;
    private boolean selectActif, new_, update, permanent;

    public CreneauUsers() {
    }

    public CreneauUsers(long id) {
        this.id_ = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getType() {
        return type != null ? type.trim().length() > 0 ? type : "V" : "V";
    }

    public void setType(String type) {
        this.type = type;
    }

    public Creneau getCreneauPoint() {
        return creneauPoint;
    }

    public void setCreneauPoint(Creneau creneauPoint) {
        this.creneauPoint = creneauPoint;
    }

    public boolean isUpdate() {
        return id_ > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public boolean isIn() {
        return in;
    }

    public void setIn(boolean in) {
        this.in = in;
    }

    public Date getDateTravail() {
        return dateTravail != null ? dateTravail : new Date();
    }

    public void setDateTravail(Date dateTravail) {
        this.dateTravail = dateTravail;
    }

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Users getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Users personnel) {
        this.personnel = personnel;
    }

    public Creneau getCreneauDepot() {
        return creneauDepot;
    }

    public void setCreneauDepot(Creneau creneauDepot) {
        this.creneauDepot = creneauDepot;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    @Override
    public String getStyleClass() {
        if (!isActif()) {
            return "utilisateur1-event";
        }
        return "";
    }

    @Override
    public Date getStartDate() {
        Calendar d_ = Calendar.getInstance();
        if (getCreneauPoint() != null ? getCreneauPoint().getTranche() != null : false) {
            d_ = dateToCalendar(getTimeStamp(getDateTravail(), getCreneauPoint().getTranche().getHeureDebut()));
        } else {
            if (getCreneauDepot() != null ? getCreneauDepot().getTranche() != null : false) {
                d_ = dateToCalendar(getTimeStamp(getDateTravail(), getCreneauDepot().getTranche().getHeureDebut()));
            }
        }
        return d_.getTime();
    }

    @Override
    public Date getEndDate() {
        Calendar d_ = dateToCalendar(getStartDate());
        int heure = 0;
        if (getCreneauPoint() != null ? getCreneauPoint().getTranche() != null : false) {
            heure = ecartOnDate(getCreneauPoint().getTranche().getHeureDebut(), getCreneauPoint().getTranche().getHeureFin());
        } else {
            if (getCreneauDepot() != null ? getCreneauDepot().getTranche() != null : false) {
                heure = ecartOnDate(getCreneauDepot().getTranche().getHeureDebut(), getCreneauDepot().getTranche().getHeureFin());
            }
        }
        d_.add(Calendar.HOUR, heure);
        return d_.getTime();
    }

    @Override
    public String getTitle() {
        String titre = "";
        if (getPersonnel() != null ? getPersonnel() != null : false) {
            if (getCreneauDepot() != null ? getCreneauDepot().getTranche() != null : false) {
                titre = " " + getPersonnel().getNomUsers() + " : De " + getTimeToString(getCreneauDepot().getTranche().getHeureDebut()) + " à " + getTimeToString(getCreneauDepot().getTranche().getHeureFin());
            } else {
                if (getCreneauPoint() != null ? getCreneauPoint().getTranche() != null : false) {
                    titre = " " + getPersonnel().getNomUsers() + " : De " + getTimeToString(getCreneauPoint().getTranche().getHeureDebut()) + " à " + getTimeToString(getCreneauPoint().getTranche().getHeureFin());
                }
            }
        }
        return titre != null ? titre : "";
    }

    public String getDescription() {
        String titre = getTitle();
        if (titre == null || titre.trim().equals("")) {
            return "";
        }
        if (getCreneauPoint() != null) {
            titre += " (" + getCreneauPoint().getReference() + ")";
        } else {
            if (getCreneauDepot() != null) {
                titre += " (" + getCreneauDepot().getReference() + ")";
            }
        }
        return titre;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (int) (this.id_ ^ (this.id_ >>> 32));
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
        final CreneauUsers other = (CreneauUsers) obj;
        if (this.id_ != other.id_) {
            return false;
        }
        return true;
    }

}
