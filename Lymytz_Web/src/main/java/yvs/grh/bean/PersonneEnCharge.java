/*
 * To change this template, choose Tools | Templates
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
public class PersonneEnCharge implements Serializable { 

    private long id;
    private String nom;
    private Date dateNaissance;
    private boolean scolarise, infirme, epouse;

    public PersonneEnCharge() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public PersonneEnCharge(long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public boolean isEpouse() {
        return epouse;
    }

    public void setEpouse(boolean epouse) {
        this.epouse = epouse;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public long getId() {
        return id;
    }

    public boolean isInfirme() {
        return infirme;
    }

    public void setInfirme(boolean infirme) {
        this.infirme = infirme;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isScolarise() {
        return scolarise;
    }

    public void setScolarise(boolean scolarise) {
        this.scolarise = scolarise;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonneEnCharge other = (PersonneEnCharge) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
