/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.paie;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author LYMYTZ-PC
 */
@ManagedBean
@SessionScoped
public class DetailsBulletin implements Serializable {

    private long id;
    private double quantite, //quantité ou taux
            base;
    private double montantSalaire, montantEmployeur, montantRetenueSalarial;
    private ElementSalaire elementSalaire = new ElementSalaire();  //permet de garder les différents taux (taux total, patronal, et salarial)
    private double trancheMin, trancheMax;
    private Date dateSave = new Date();
//    private String codeElt, descriptionElt;

    public DetailsBulletin() {
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public double getMontantRetenueSalarial() {
        return montantRetenueSalarial;
    }

    public void setMontantRetenueSalarial(double montantRetenueSalarial) {
        this.montantRetenueSalarial = montantRetenueSalarial;
    }

    public double getMontantEmployeur() {
        return montantEmployeur;
    }

    public void setMontantEmployeur(double montantEmployeur) {
        this.montantEmployeur = montantEmployeur;
    }

    public double getMontantSalaire() {
        return montantSalaire;
    }

    public void setMontantSalaire(double montantSalaire) {
        this.montantSalaire = montantSalaire;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ElementSalaire getElementSalaire() {
        return elementSalaire;
    }

    public void setElementSalaire(ElementSalaire elementSalaire) {
        this.elementSalaire = elementSalaire;
    }

    public double getBase() {
        return base;
    }

    public void setBase(double base) {
        this.base = base;
    }

    public double getTrancheMax() {
        return trancheMax;
    }

    public void setTrancheMax(double trancheMax) {
        this.trancheMax = trancheMax;
    }

    public double getTrancheMin() {
        return trancheMin;
    }

    public void setTrancheMin(double trancheMin) {
        this.trancheMin = trancheMin;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DetailsBulletin other = (DetailsBulletin) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
