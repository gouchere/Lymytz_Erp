/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.sanction;

import yvs.grh.bean.*;
import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class SanctionEmps implements Serializable {

    private long id;
    private Boolean supp;
    private Boolean actif;
    private boolean SelectActif;
    private Date dateSanction;
    private Date dateDebut;
    private Date dateFin;
    private Sanction sanction = new Sanction();
    private Employe employe = new Employe();

    public SanctionEmps() {
    }

    public SanctionEmps(long id, Date dateSanction, Date dateFin) {
        this.id = id;
        this.dateSanction = dateSanction;
        this.dateFin = dateFin;
    }

    public SanctionEmps(long id, Date dateSanction, Date dateFin, Sanction sanction, Employe employe) {
        this.id = id;
        this.dateSanction = dateSanction;
        this.dateFin = dateFin;
        this.sanction = sanction;
        this.employe = employe;
    }

    public SanctionEmps(long id, Date dateSanction, Date dateDebut, Date dateFin) {
        this.id = id;
        this.dateSanction = dateSanction;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public SanctionEmps(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public boolean isSelectActif() {
        return SelectActif;
    }

    public void setSelectActif(boolean SelectActif) {
        this.SelectActif = SelectActif;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean isSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Date getDateSanction() {
        return dateSanction;
    }

    public void setDateSanction(Date dateSanction) {
        this.dateSanction = dateSanction;
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

    public Sanction getSanction() {
        return sanction;
    }

    public void setSanction(Sanction sanction) {
        this.sanction = sanction;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final SanctionEmps other = (SanctionEmps) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
