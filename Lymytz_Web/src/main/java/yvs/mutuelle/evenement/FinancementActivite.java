/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.evenement;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.CaisseMutuelle;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class FinancementActivite implements Serializable {

    private long id;
    private double montantRecu;
    private Date dateFinancement;
    private Activite activite = new Activite();
    private CaisseMutuelle caisse = new CaisseMutuelle();

    public FinancementActivite() {
    }

    public FinancementActivite(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontantRecu() {
        return montantRecu;
    }

    public void setMontantRecu(double montantRecu) {
        this.montantRecu = montantRecu;
    }

    public Date getDateFinancement() {
        return dateFinancement != null ? dateFinancement : new Date();
    }

    public void setDateFinancement(Date dateFinancement) {
        this.dateFinancement = dateFinancement;
    }

    public Activite getActivite() {
        return activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public CaisseMutuelle getCaisse() {
        return caisse;
    }

    public void setCaisse(CaisseMutuelle caisse) {
        this.caisse = caisse;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final FinancementActivite other = (FinancementActivite) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
