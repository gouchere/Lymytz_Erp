/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class Valeurs implements Serializable {

    private String periode;
    private double valeurAttendu;
    private double valeurReelle;
    private String indicateur;
    private String comerciale;
    private String objectif;

    public Valeurs() {
    }

    public Valeurs(String periode,String indicateur, String comerciale, String objectif) {
        this.periode = periode;
        this.indicateur = indicateur;
        this.comerciale = comerciale;
        this.objectif = objectif;
    }
    public Valeurs(String periode, double valeurAttendu, double valeurReelle, String indicateur, String comerciale, String objectif) {        
        this.valeurAttendu = valeurAttendu;
        this.valeurReelle = valeurReelle;
        this.indicateur = indicateur;
        this.comerciale = comerciale;
        this.objectif = objectif;
        this.periode = periode;
    }
    public Valeurs(double valeurAttendu, double valeurReelle) {        
        this.valeurAttendu = valeurAttendu;
        this.valeurReelle = valeurReelle;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public double getValeurAttendu() {
        return valeurAttendu;
    }

    public void setValeurAttendu(double valeurAttendu) {
        this.valeurAttendu = valeurAttendu;
    }

    public double getValeurReelle() {
        return valeurReelle;
    }

    public void setValeurReelle(double valeurReelle) {
        this.valeurReelle = valeurReelle;
    }

    public String getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(String indicateur) {
        this.indicateur = indicateur;
    }

    public String getComerciale() {
        return comerciale;
    }

    public void setComerciale(String comerciale) {
        this.comerciale = comerciale;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.periode);
        hash = 71 * hash + Objects.hashCode(this.indicateur);
        hash = 71 * hash + Objects.hashCode(this.comerciale);
        hash = 71 * hash + Objects.hashCode(this.objectif);
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
        final Valeurs other = (Valeurs) obj;
        if (!Objects.equals(this.periode, other.periode)) {
            return false;
        }
        if (!Objects.equals(this.indicateur, other.indicateur)) {
            return false;
        }
        if (!Objects.equals(this.comerciale, other.comerciale)) {
            return false;
        }
        if (!Objects.equals(this.objectif, other.objectif)) {
            return false;
        }
        return true;
    }

}
