/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.grh.taches.YvsGrhTaches;
import yvs.entity.grh.param.YvsGrhIntervalPrimeTache;
import yvs.entity.grh.param.YvsGrhPrimeTache;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class PrimeTache implements Serializable {

    private int id;
    private double quantite;
    private double montant;
    private Date datePrime;
    private boolean taux;
    private boolean actif = true;
    private String reference;
    private List<YvsGrhIntervalPrimeTache> listTranches;
    private List<YvsGrhTaches> listTache;
    private boolean selectActif;

    public PrimeTache() {
    }

    public PrimeTache(int id) {
        this.id = id;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Date getDatePrime() {
        return datePrime;
    }

    public boolean isTaux() {
        return taux;
    }

    public void setTaux(boolean taux) {
        this.taux = taux;
    }

    public void setDatePrime(Date datePrime) {
        this.datePrime = datePrime;
    }

    public List<YvsGrhTaches> getListTache() {
        return listTache;
    }

    public void setListTache(List<YvsGrhTaches> listTache) {
        this.listTache = listTache;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getQuantite() {
        return quantite;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setListTranches(List<YvsGrhIntervalPrimeTache> listTranches) {
        this.listTranches = listTranches;
    }

    public List<YvsGrhIntervalPrimeTache> getListTranches() {
        return listTranches;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
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
        final PrimeTache other = (PrimeTache) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
