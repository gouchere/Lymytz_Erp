/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class TacheEmps implements Serializable {

    private long id;
    private Date datePlanification, dateDebut, dateFin;
    private double quantite;
    private boolean actif, affectationPermanante = true, attribuer = false;
    private Taches taches = new Taches();

    public TacheEmps(long id) {
        this.id = id;
    }

    public TacheEmps() {        
    }

    public boolean isAttribuer() {
        return attribuer;
    }

    public void setAttribuer(boolean attribuer) {
        this.attribuer = attribuer;
    }

    public boolean isAffectationPermanante() {
        return affectationPermanante;
    }

    public void setAffectationPermanante(boolean affectationPermanante) {
        this.affectationPermanante = affectationPermanante;
    }

    public Taches getTaches() {
        return taches;
    }

    public void setTaches(Taches taches) {
        this.taches = taches;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDatePlanification() {
        return datePlanification;
    }

    public void setDatePlanification(Date datePlanification) {
        this.datePlanification = datePlanification;
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

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final TacheEmps other = (TacheEmps) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
