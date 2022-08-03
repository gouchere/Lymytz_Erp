/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class Convention implements Serializable {

    private long id; 
    private Secteurs secteur = new Secteurs();
    private Categories categorie = new Categories();
    private Echelons echelon = new Echelons();
    private double salaireHoraireMin;
    private Date dateChange = new Date();
    private double salaireMin;
    private boolean actif, supp;

    public Convention() {
    }

    public Convention(long id) {
        this.id = id;
    }

    public double getSalaireHoraireMin() {
        return salaireHoraireMin;
    }

    public void setSalaireHoraireMin(double salaireHoraireMin) {
        this.salaireHoraireMin = salaireHoraireMin;
    }

    public Date getDateChange() {
        return dateChange;
    }

    public void setDateChange(Date dateChange) {
        this.dateChange = dateChange;
    }

    public double getSalaireMin() {
        return salaireMin;
    }

    public void setSalaireMin(double salaireMin) {
        this.salaireMin = salaireMin;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public Secteurs getSecteur() {
        return secteur;
    }

    public void setSecteur(Secteurs secteur) {
        this.secteur = secteur;
    }

    public Categories getCategorie() {
        return categorie;
    }

    public void setCategorie(Categories categorie) {
        this.categorie = categorie;
    }

    public Echelons getEchelon() {
        return echelon;
    }

    public void setEchelon(Echelons echelon) {
        this.echelon = echelon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
