/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.base.YvsMutPrimePoste;
import yvs.mutuelle.Mutuelle;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Poste implements Serializable {

    private long id;
    private String designation;
    private String description;
    private Mutuelle mutuelle = new Mutuelle();
    private List<YvsMutPrimePoste> primes;
    private double montantPrime = 0;
    private boolean selectActif, new_;
    private boolean canVoteCredit;
    private Date dateSave = new Date();

    public Poste() {
        primes = new ArrayList<>();
    }

    public Poste(long id) {
        this.id = id;
        primes = new ArrayList<>();
    }

    public boolean isCanVoteCredit() {
        return canVoteCredit;
    }

    public void setCanVoteCredit(boolean canVoteCredit) {
        this.canVoteCredit = canVoteCredit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMontantPrime() {
        return montantPrime;
    }

    public void setMontantPrime(double montantPrime) {
        this.montantPrime = montantPrime;
    }

    public List<YvsMutPrimePoste> getPrimes() {
        return primes;
    }

    public void setPrimes(List<YvsMutPrimePoste> primes) {
        this.primes = primes;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Poste other = (Poste) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
