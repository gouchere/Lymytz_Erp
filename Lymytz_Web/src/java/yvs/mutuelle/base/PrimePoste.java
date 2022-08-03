/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.base;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PrimePoste implements Serializable, Comparator<PrimePoste> {

    private long id;
    private double montant;
    private TypePrime type = new TypePrime();
    private boolean selectActif, new_;
    private Date datePaiement = new Date();
    private Date dateSave = new Date();
    private Poste poste = new Poste();
    private boolean payePasse, paye;

    public PrimePoste() {
    }

    public PrimePoste(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
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

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public TypePrime getType() {
        return type;
    }

    public void setType(TypePrime type) {
        this.type = type;
    }

    public boolean isPayePasse() {
        return payePasse;
    }

    public void setPayePasse(boolean payePasse) {
        this.payePasse = payePasse;
    }

    public boolean isPaye() {
        return paye;
    }

    public void setPaye(boolean paye) {
        this.paye = paye;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final PrimePoste other = (PrimePoste) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compare(PrimePoste o1, PrimePoste o2) {
        if (o1.getDatePaiement() == null) {
            return -1;
        } else if (o2.getDatePaiement() == null) {
            return 1;
        } else if (o1.getDatePaiement().after(o2.getDatePaiement())) {
            return 1;
        } else if (o1.getDatePaiement().before(o2.getDatePaiement())) {
            return -1;
        } else {
            return 0;
        }

    }

}
