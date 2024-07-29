/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CentreAnalytique;
import yvs.base.compta.Comptes;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class SectionDeValorisation implements Serializable { 

    private int id;
    private String reference;
    private Comptes compte = new Comptes();
    private String designation;
    private String description;
    private char typeValeur = Constantes.DIRECT;
    private boolean actif = true;
    private Date dateSave = new Date();
    private CentreAnalytique centre = new CentreAnalytique();

    public SectionDeValorisation() {
    }

    public SectionDeValorisation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public CentreAnalytique getCentre() {
        return centre;
    }

    public void setCentre(CentreAnalytique centre) {
        this.centre = centre;
    }

    public char getTypeValeur() {
        return typeValeur;
    }

    public void setTypeValeur(char typeValeur) {
        this.typeValeur = typeValeur;
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
        hash = 19 * hash + this.id;
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
        final SectionDeValorisation other = (SectionDeValorisation) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
