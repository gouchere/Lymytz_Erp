/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.salaire;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.Exercice;
import yvs.mutuelle.Mutualiste;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Interet implements Serializable {

    private long id;
    private Date dateInteret = new Date();
    private double montant;
    private Mutualiste mutualiste = new Mutualiste();
    private boolean selectActif, new_;
    private Exercice exo = new Exercice();

    public Interet() {
    }

    public Interet(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateInteret() {
        return dateInteret;
    }

    public void setDateInteret(Date dateInteret) {
        this.dateInteret = dateInteret;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
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

    public void setExo(Exercice exo) {
        this.exo = exo;
    }

    public Exercice getExo() {
        return exo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Interet other = (Interet) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
