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
 * @author GOUCHERE YVES
 *
 * cette classe permet de modéliser le statut et le grade d'un employé
 */
@ManagedBean
@SessionScoped
public class Profils implements Serializable {

    private long id;
    private GradeEmploye grade = new GradeEmploye();
    private String statut = "PE";
    private boolean actif=true;

    public Profils() {
    }

    public Profils(long id, String statut) {
        this.id = id;
        this.statut = statut;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GradeEmploye getGrade() {
        return grade;
    }

    public void setGrade(GradeEmploye grade) {
        this.grade = grade;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Profils other = (Profils) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
