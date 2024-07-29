/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.recrutement.entretien;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.grh.bean.Qualification;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class QualificationCandidat implements Serializable {

    private long id;
    private Qualification qualification = new Qualification();
    private int niveau;
    private boolean actif;

    public QualificationCandidat() {
    }

    public QualificationCandidat(long id) {
        this.id = id;
    }

    public QualificationCandidat(long id, Qualification qualification) {
        this.id = id;
        this.qualification = qualification;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public int getNiveau() {
        return niveau;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
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
        final QualificationCandidat other = (QualificationCandidat) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QualificationPoste{" + "id=" + id + '}';
    }

}
