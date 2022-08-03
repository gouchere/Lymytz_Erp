/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PeriodePlanification implements Serializable {

    private long id;
    private String reference;
    private int indicatif;
    private Date dateDebut;
    private Date dateFin;
    private Planification plan = new Planification();
    private boolean selectActif;

    public PeriodePlanification() {
    }

    public PeriodePlanification(int id) {
        this.id = id;
    }

    public PeriodePlanification(PeriodePlanification p) {
        this.id = p.id;
        this.reference = p.reference;
        this.indicatif = p.indicatif;
        this.dateDebut = p.dateDebut;
        this.dateFin = p.dateFin;
        this.selectActif = p.selectActif;
    }
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIndicatif() {
        return indicatif;
    }

    public void setIndicatif(int indicatif) {
        this.indicatif = indicatif;
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

    public Planification getPlan() {
        return plan;
    }

    public void setPlan(Planification plan) {
        this.plan = plan;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PeriodePlanification other = (PeriodePlanification) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
