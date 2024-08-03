/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.analytique;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CentreAnalytique;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class SectionAnalytique implements Serializable {

    protected long id;
    private double montant, taux;
    private Date dateUpdate = new Date();
    private Date dateSave = new Date();
    protected CentreAnalytique centre = new CentreAnalytique();

    public SectionAnalytique() {
    }

    public SectionAnalytique(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public CentreAnalytique getCentre() {
        return centre;
    }

    public void setCentre(CentreAnalytique centre) {
        this.centre = centre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final SectionAnalytique other = (SectionAnalytique) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
