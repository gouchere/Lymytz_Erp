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
public class YvsLangueEmpsPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "langue")
    private int langue;
    @Basic(optional = false)
    @NotNull
    @Column(name = "employe")
    private int employe;

    public YvsLangueEmpsPK() {
    }

    public YvsLangueEmpsPK(int langue, int employe) {
        this.langue = langue;
        this.employe = employe;
    }

    public int getLangue() {
        return langue;
    }

    public void setLangue(int langue) {
        this.langue = langue;
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
        hash += (int) langue;
        hash += (int) employe;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsLangueEmpsPK)) {
            return false;
        }
        YvsLangueEmpsPK other = (YvsLangueEmpsPK) object;
        if (this.langue != other.langue) {
            return false;
        }
        if (this.employe != other.employe) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsLangueEmpsPK[ langue=" + langue + ", employe=" + employe + " ]";
    }
    
}
