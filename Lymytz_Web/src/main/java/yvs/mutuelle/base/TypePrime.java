/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.base;

import java.io.Serializable;import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.Mutuelle;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class TypePrime implements Serializable {

    private long id;
    private String designation;
    private double montantMaximal;
    private String natureMontant = Constantes.MUT_TYPE_MONTANT_FIXE;
    private String suffixeMontant = "Fcfa";
    private Mutuelle mutuelle = new Mutuelle();
    private boolean selectActif, new_;
    private Date dateSave = new Date();
    private String periodeRemuneration;

    public TypePrime() {
    }

    public TypePrime(long id) {
        this.id = id;
    }

    public String getSuffixeMontant() {
        return suffixeMontant;
    }

    public void setSuffixeMontant(String suffixeMontant) {
        this.suffixeMontant = suffixeMontant;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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

    public double getMontantMaximal() {
        return montantMaximal;
    }

    public void setMontantMaximal(double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public String getNatureMontant() {
        return natureMontant;
    }

    public void setNatureMontant(String natureMontant) {
        this.natureMontant = natureMontant;
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

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public String getPeriodeRemuneration() {
        return periodeRemuneration;
    }

    public void setPeriodeRemuneration(String periodeRemuneration) {
        this.periodeRemuneration = periodeRemuneration;
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
        final TypePrime other = (TypePrime) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
