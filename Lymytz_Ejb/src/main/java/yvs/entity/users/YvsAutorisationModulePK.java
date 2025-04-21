/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.users;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author lymytz
 */
@Embeddable
public class YvsAutorisationModulePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "niveau_acces")
    private int niveauAcces;
    @Basic(optional = false)
    @NotNull
    @Column(name = "module")
    private int module;

    public YvsAutorisationModulePK() {
    }

    public YvsAutorisationModulePK(int niveauAcces, int module) {
        this.niveauAcces = niveauAcces;
        this.module = module;
    }

    public int getNiveauAcces() {
        return niveauAcces;
    }

    public void setNiveauAcces(int niveauAcces) {
        this.niveauAcces = niveauAcces;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) niveauAcces;
        hash += (int) module;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsAutorisationModulePK)) {
            return false;
        }
        YvsAutorisationModulePK other = (YvsAutorisationModulePK) object;
        if (this.niveauAcces != other.niveauAcces) {
            return false;
        }
        if (this.module != other.module) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsAutorisationModulePK[ niveauAcces=" + niveauAcces + ", module=" + module + " ]";
    }
    
}
