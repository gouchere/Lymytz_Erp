/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.commission;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean 
@SessionScoped
public class Periodicite implements Serializable {

    private int id;
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private char periodicite;
    private FacteurTaux facteur = new FacteurTaux();

    public Periodicite() {
    }

    public Periodicite(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
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

    public char getPeriodicite() {
        return Character.valueOf(periodicite) != null ? String.valueOf(periodicite).trim().length() > 0 ? periodicite : 'P' : 'P';
    }

    public void setPeriodicite(char periodicite) {
        this.periodicite = periodicite;
    }

    public FacteurTaux getFacteur() {
        return facteur;
    }

    public void setFacteur(FacteurTaux facteur) {
        this.facteur = facteur;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.id;
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
        final Periodicite other = (Periodicite) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
