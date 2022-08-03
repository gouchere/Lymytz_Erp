/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.production.base;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.parametrage.TypeEtat;
import yvs.production.technique.PosteCharge;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class EtatRessource implements Serializable {
    
    private int id;
    private Date dateEtat;
    private double capaciteH;
    private double capaciteQ;
    private double chargeH;
    private double chargeQ;
    private double tauxObsolescence;
    private boolean actif;
    private PosteCharge ressource = new PosteCharge();
    private TypeEtat typeEtat = new TypeEtat();

    public EtatRessource() {
    }

    public EtatRessource(int id) {
        this.id = id;
    }

    public PosteCharge getRessource() {
        return ressource;
    }

    public void setRessource(PosteCharge ressource) {
        this.ressource = ressource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(Date dateEtat) {
        this.dateEtat = dateEtat;
    }

    public double getCapaciteH() {
        return capaciteH;
    }

    public void setCapaciteH(double capaciteH) {
        this.capaciteH = capaciteH;
    }

    public double getCapaciteQ() {
        return capaciteQ;
    }

    public void setCapaciteQ(double capaciteQ) {
        this.capaciteQ = capaciteQ;
    }

    public double getChargeH() {
        return chargeH;
    }

    public void setChargeH(double chargeH) {
        this.chargeH = chargeH;
    }

    public double getChargeQ() {
        return chargeQ;
    }

    public void setChargeQ(double chargeQ) {
        this.chargeQ = chargeQ;
    }

    public double getTauxObsolescence() {
        return tauxObsolescence;
    }

    public void setTauxObsolescence(double tauxObsolescence) {
        this.tauxObsolescence = tauxObsolescence;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public TypeEtat getTypeEtat() {
        return typeEtat;
    }

    public void setTypeEtat(TypeEtat typeEtat) {
        this.typeEtat = typeEtat;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.id;
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
        final EtatRessource other = (EtatRessource) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
}
