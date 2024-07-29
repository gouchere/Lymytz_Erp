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
import yvs.parametrage.poste.SpecialiteDiplomes;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean 
@SessionScoped
public class Diplomes implements Serializable {

    private long id;
    private String codeInterne, designation;
    private String serie;
    private String cycle;   //Secondaire, Universitaire
    private Date dateObtention = new Date();
    private String mention = "Passable";
    private String ecole;
    private int niveau;
    private boolean newSave;    //marque les données qu'on peut modifier dans une session
    private boolean actif;
    private SpecialiteDiplomes specialite = new SpecialiteDiplomes();

    public Diplomes() {
    }

    public Diplomes(long id) {
        this.id = id;
    }

    public Diplomes(long id, String nom) {
        this.id = id;
        this.designation = nom;
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

    public Date getDateObtention() {
        return dateObtention;
    }

    public void setDateObtention(Date dateObtention) {
        this.dateObtention = dateObtention;
    }

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    public String getEcole() {
        return ecole;
    }

    public void setEcole(String ecole) {
        this.ecole = ecole;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public String getCodeInterne() {
        return codeInterne;
    }

    public void setCodeInterne(String codeInterne) {
        this.codeInterne = codeInterne;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public void setNewSave(boolean newSave) {
        this.newSave = newSave;
    }

    public boolean isNewSave() {
        return newSave;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public SpecialiteDiplomes getSpecialite() {
        return specialite;
    }

    public void setSpecialite(SpecialiteDiplomes specialite) {
        this.specialite = specialite;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Diplomes other = (Diplomes) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
