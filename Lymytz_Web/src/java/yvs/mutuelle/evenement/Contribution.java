/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.evenement;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Contribution implements Serializable {

    private long id;
    private double montant;
    private TypeContribution typeContibution = new TypeContribution();
    private TypeEvenement evenement = new TypeEvenement();
    private boolean selectActif, new_;

    public Contribution() {
    }

    public Contribution(long id) {
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

    public TypeContribution getTypeContibution() {
        return typeContibution;
    }

    public void setTypeContibution(TypeContribution typeContibution) {
        this.typeContibution = typeContibution;
    }

    public TypeEvenement getEvenement() {
        return evenement;
    }

    public void setEvenement(TypeEvenement evenement) {
        this.evenement = evenement;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.id);
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
        final Contribution other = (Contribution) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
