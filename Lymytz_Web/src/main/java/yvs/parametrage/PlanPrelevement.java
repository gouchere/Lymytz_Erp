/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class PlanPrelevement implements Serializable {

    private long id;
    private String reference;
    private char base = 'S'; //S=salaire D=Dette
    private String nameBaseInterval;
    private double valeur;
    private Date dateSave = new Date();
    private char basePlan = 'T'; //T=taux, D=nombre de mois, F=fixe
    private boolean defaut = true, actif, visibleEnGescom;

    /*propriétés utiles sur la vue*/
    private int nombreMois;
    private double pourcentage;

    public PlanPrelevement() {
    }

    public PlanPrelevement(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public char getBasePlan() {
        return basePlan;
    }

    public void setBasePlan(char basePlan) {
        this.basePlan = basePlan;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNameBaseInterval() {
        return nameBaseInterval;
    }

    public void setNameBaseInterval(String nameBaseInterval) {
        this.nameBaseInterval = nameBaseInterval;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public char getBase() {
        return base;
    }

    public void setBase(char base) {
        this.base = base;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public int getNombreMois() {
        return nombreMois;
    }

    public void setNombreMois(int nombreMois) {
        this.nombreMois = nombreMois;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public boolean isVisibleEnGescom() {
        return visibleEnGescom;
    }

    public void setVisibleEnGescom(boolean visibleEnGescom) {
        this.visibleEnGescom = visibleEnGescom;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final PlanPrelevement other = (PlanPrelevement) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
