/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class IntervalleAnciennete implements Serializable {

    private int id;
    private int ancienneteMin, ancienneteMax;   //en annéé
    private int dureePreavie;
    private String uniteJour;
    private String reference;

    public IntervalleAnciennete() {
    }

    public IntervalleAnciennete(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAncienneteMin() {
        return ancienneteMin;
    }

    public void setAncienneteMin(int ancienneteMin) {
        this.ancienneteMin = ancienneteMin;
    }

    public int getAncienneteMax() {
        return ancienneteMax;
    }

    public void setAncienneteMax(int ancienneteMax) {
        this.ancienneteMax = ancienneteMax;
    }

    public int getDureePreavie() {
        return dureePreavie;
    }

    public void setDureePreavie(int dureePreavie) {
        this.dureePreavie = dureePreavie;
    }

    public String getUniteJour() {
        return uniteJour;
    }

    public void setUniteJour(String uniteJour) {
        this.uniteJour = uniteJour;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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
        final IntervalleAnciennete other = (IntervalleAnciennete) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
