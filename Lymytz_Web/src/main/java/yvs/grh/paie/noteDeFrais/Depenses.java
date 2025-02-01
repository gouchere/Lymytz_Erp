/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie.noteDeFrais;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class Depenses implements Serializable {

    private TypeDepense typeDeDepense = new TypeDepense();
    private double montant, montantApprouve;

    public Depenses() {
    }

    public TypeDepense getTypeDeDepense() {
        return typeDeDepense;
    }

    public void setTypeDeDepense(TypeDepense typeDeDepense) {
        this.typeDeDepense = typeDeDepense;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getMontantApprouve() {
        return montantApprouve;
    }

    public void setMontantApprouve(double montantApprouve) {
        this.montantApprouve = montantApprouve;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.typeDeDepense);
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
        final Depenses other = (Depenses) obj;
        if (!Objects.equals(this.typeDeDepense, other.typeDeDepense)) {
            return false;
        }
        return true;
    }

}
