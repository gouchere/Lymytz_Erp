/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;

/**
 *
 * @author lymytz
 */
public class DetailPlan implements Serializable {

    private long id;
    private String typeVal;
    private double valeur;
    private boolean pdp, selectActif;

    public DetailPlan() {

    }

    public DetailPlan(long id) {
        this.id = id;
    }

    public DetailPlan(long id, double valeur) {
        this.id = id;
        this.valeur = valeur;
    }

    public DetailPlan(long id, String typeVal, double valeur) {
        this.id = id;
        this.typeVal = typeVal;
        this.valeur = valeur;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public String getTypeVal() {
        return typeVal;
    }

    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
    }

    public boolean isPdp() {
        return pdp;
    }

    public void setPdp(boolean pdp) {
        this.pdp = pdp;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DetailPlan other = (DetailPlan) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
