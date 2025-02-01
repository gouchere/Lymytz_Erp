/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.recrutement;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GOUCHERE YVES
 */
public class SpecialitesDiplomeCandidat {

    private long id;
    private String codeInterne, designation;
    private List<DiplomeCandidat> diplomes;

    public SpecialitesDiplomeCandidat() {
        diplomes = new ArrayList<>();
    }

    public SpecialitesDiplomeCandidat(long id, String codeInterne, String designation) {
        this.id = id;
        this.codeInterne = codeInterne;
        this.designation = designation;
        diplomes = new ArrayList<>();
    }

    public SpecialitesDiplomeCandidat(long id) {
        this.id = id;
    }

    public SpecialitesDiplomeCandidat(long id, String nom) {
        this.id = id;
        this.designation = nom;
        diplomes = new ArrayList<>();
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

    public String getCodeInterne() {
        return codeInterne;
    }

    public void setCodeInterne(String codeInterne) {
        this.codeInterne = codeInterne;
    }

    public List<DiplomeCandidat> getDiplomes() {
        return diplomes;
    }

    public void setDiplomes(List<DiplomeCandidat> diplomes) {
        this.diplomes = diplomes;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final SpecialitesDiplomeCandidat other = (SpecialitesDiplomeCandidat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
