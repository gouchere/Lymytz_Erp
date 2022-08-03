/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie.noteDeFrais;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class TypeDepense implements Serializable {

    private long id;
    private String typeDepense;
    private double montant, marge;
    private boolean selectActif;

    public TypeDepense() {
    }

    public TypeDepense(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public String getTypeDepense() {
        return typeDepense;
    }

    public void setTypeDepense(String typeDepense) {
        this.typeDepense = typeDepense;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getMarge() {
        return marge;
    }

    public void setMarge(double marge) {
        this.marge = marge;
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
        final TypeDepense other = (TypeDepense) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
