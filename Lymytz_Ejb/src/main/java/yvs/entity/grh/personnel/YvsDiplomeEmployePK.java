/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.grh.personnel;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Lymytz Dowes
 */
@Embeddable
public class YvsDiplomeEmployePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "diplome")
    private int diplome;
    @Basic(optional = false)
    @NotNull
    @Column(name = "employe")
    private int employe;

    public YvsDiplomeEmployePK() {
    }

    public YvsDiplomeEmployePK(int diplome, int employe) {
        this.diplome = diplome;
        this.employe = employe;
    }

    public int getDiplome() {
        return diplome;
    }

    public void setDiplome(int diplome) {
        this.diplome = diplome;
    }

    public int getEmploye() {
        return employe;
    }

    public void setEmploye(int employe) {
        this.employe = employe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) diplome;
        hash += (int) employe;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsDiplomeEmployePK)) {
            return false;
        }
        YvsDiplomeEmployePK other = (YvsDiplomeEmployePK) object;
        if (this.diplome != other.diplome) {
            return false;
        }
        if (this.employe != other.employe) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsDiplomeEmployePK[ diplome=" + diplome + ", employe=" + employe + " ]";
    }
    
}
