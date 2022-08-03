/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.credit;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.Mutualiste;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class VoteCredit implements Serializable {

    private long id;
    private Date dateValidation = new Date();
    private boolean accepte;
    private Credit credit = new Credit();
    private Mutualiste mutualiste = new Mutualiste();

    public VoteCredit() {
    }

    public VoteCredit(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public boolean isAccepte() {
        return accepte;
    }

    public void setAccepte(boolean accepte) {
        this.accepte = accepte;
    }

    public Credit getCredit() {
        return credit;
    } 

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final VoteCredit other = (VoteCredit) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
