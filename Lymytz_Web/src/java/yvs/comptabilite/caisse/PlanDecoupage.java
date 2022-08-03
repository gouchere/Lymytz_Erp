/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.caisse;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Comptes;
import yvs.mutuelle.Compte;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class PlanDecoupage implements Serializable {

    private long id;
    private String reference;
    private double valeur;
    private char basePlan = 'T'; //T=taux, D=Durée, V=Valeur fixe
    private char periodicite = 'M';
    private boolean actif = true;
    private Comptes compte = new Comptes();
    private Date dateSave = new Date();

    public PlanDecoupage() {
    }

    public PlanDecoupage(long id) {
        this.id = id;
    }

    public char getBasePlan() {
        return basePlan != ' ' ? basePlan : 'T';
    }

    public void setBasePlan(char basePlan) {
        this.basePlan = basePlan;
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

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public char getPeriodicite() {
        return periodicite != ' ' ? periodicite : 'M';
    }

    public void setPeriodicite(char periodicite) {
        this.periodicite = periodicite;
    }

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getRefbase(Character base) {
        if (base != null) {
            switch (base) {
                case 'T':
                    return "Taux";
                case 'D':
                    return "Durée";
                case 'V':
                    return "Valeur Fixe";
            }
        }
        return "";
    }

    public String getRefPeriodicite(Character base) {
        if (base != null) {
            switch (base) {
                case 'M':
                    return "Mensuelle";
                case 'A':
                    return "Annuelle";
            }
        }
        return "";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PlanDecoupage other = (PlanDecoupage) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
