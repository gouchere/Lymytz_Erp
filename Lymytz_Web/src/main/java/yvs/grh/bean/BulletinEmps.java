/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.util.Date;

/**
 *
 * @author GOUCHERE YVES
 */
public class BulletinEmps {

    private long id;
    private String modePaiement;
    private double salaireMens;
    private double salaireHoraire;
    private double horaire;
    private double horaireHebdo;
    private boolean actif; //précise si c'est le bulletin actuellement en cours d'utilisation
    private Date dateActiv;

    public BulletinEmps() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public double getSalaireMens() {
        return salaireMens;
    }

    public void setSalaireMens(double salaireMens) {
        this.salaireMens = salaireMens;
    }

    public double getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(double salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public double getHoraire() {
        return horaire;
    }

    public void setHoraire(double horaire) {
        this.horaire = horaire;
    }

    public double getHoraireHebdo() {
        return horaireHebdo;
    }

    public void setHoraireHebdo(double horaireHebdo) {
        this.horaireHebdo = horaireHebdo;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Date getDateActiv() {
        return dateActiv;
    }

    public void setDateActiv(Date dateActiv) {
        this.dateActiv = dateActiv;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final BulletinEmps other = (BulletinEmps) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
