/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.salaire;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.Mutualiste;
import yvs.mutuelle.base.Poste;
import yvs.mutuelle.base.PrimePoste;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ReglementPrime implements Serializable {

    private long id;
    private Date datePrime = new Date();
    private PrimePoste prime = new PrimePoste();
    private Mutualiste mutualiste = new Mutualiste();
    private Poste poste = new Poste();
    private List<ReglementPrime> reglements;
    private double montant;
    private boolean selectActif, new_;

    public ReglementPrime() {
        reglements = new ArrayList<>();
    }

    public ReglementPrime(long id) {
        this.id = id;
        reglements = new ArrayList<>();
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public List<ReglementPrime> getReglements() {
        return reglements;
    }

    public void setReglements(List<ReglementPrime> reglements) {
        this.reglements = reglements;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDatePrime() {
        return datePrime;
    }

    public void setDatePrime(Date datePrime) {
        this.datePrime = datePrime;
    }

    public PrimePoste getPrime() {
        return prime;
    }

    public void setPrime(PrimePoste prime) {
        this.prime = prime;
    }

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ReglementPrime other = (ReglementPrime) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
