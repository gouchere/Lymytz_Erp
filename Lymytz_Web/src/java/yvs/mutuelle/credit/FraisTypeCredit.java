/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.credit;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.TypeCout;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class FraisTypeCredit implements Serializable {

    private long id;
    private double montant;
    private TypeCredit credit = new TypeCredit();
    private TypeCout type = new TypeCout();

    public FraisTypeCredit() {
    }

    public FraisTypeCredit(long id) {
        this.id = id;
    }

    public FraisTypeCredit(long id, double montant) {
        this.id = id;
        this.montant = montant;
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

    public TypeCredit getCredit() {
        return credit;
    }

    public void setCredit(TypeCredit credit) {
        this.credit = credit;
    }

    public TypeCout getType() {
        return type;
    }

    public void setType(TypeCout type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final FraisTypeCredit other = (FraisTypeCredit) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
