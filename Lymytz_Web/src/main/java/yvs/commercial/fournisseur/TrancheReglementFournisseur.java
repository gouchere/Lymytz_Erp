/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.fournisseur;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class TrancheReglementFournisseur implements Serializable, Comparable {

    private long id;
    private int numero;
    private double taux;
    private int intervalJour;
    private PlanReglementFournisseur plan = new PlanReglementFournisseur();
    private boolean selectActif, new_, update;

    public TrancheReglementFournisseur() {
    }

    public TrancheReglementFournisseur(long id) {
        this.id = id;
    }

    public PlanReglementFournisseur getPlan() {
        return plan;
    }

    public void setPlan(PlanReglementFournisseur plan) {
        this.plan = plan;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public int getIntervalJour() {
        return intervalJour;
    }

    public void setIntervalJour(int intervalJour) {
        this.intervalJour = intervalJour;
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

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final TrancheReglementFournisseur other = (TrancheReglementFournisseur) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        TrancheReglementFournisseur t = (TrancheReglementFournisseur) o;
        if (numero == t.numero) {
            return Long.valueOf(id).compareTo(t.id);
        }
        return Integer.valueOf(numero).compareTo(t.numero);
    }

}
