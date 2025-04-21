/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.grh.bean;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author user1
 */
@ManagedBean
@SessionScoped
public class QualificationFormation implements Serializable{
    private Formation formation = new Formation();
    private Qualification qualif = new Qualification();
    private boolean supp, actif;

    public QualificationFormation() {
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public Qualification getQualif() {
        return qualif;
    }

    public void setQualif(Qualification qualif) {
        this.qualif = qualif;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.formation);
        hash = 41 * hash + Objects.hashCode(this.qualif);
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
        final QualificationFormation other = (QualificationFormation) obj;
        if (!Objects.equals(this.formation, other.formation)) {
            return false;
        }
        if (!Objects.equals(this.qualif, other.qualif)) {
            return false;
        }
        return true;
    }
    
    
}
