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
public class YvsAutorisationPageModulePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "niveau_acces")
    private int niveauAcces;
    @Basic(optional = false)
    @NotNull
    @Column(name = "page_module")
    private int pageModule;

    public YvsAutorisationPageModulePK() {
    }

    public YvsAutorisationPageModulePK(int niveauAcces, int pageModule) {
        this.niveauAcces = niveauAcces;
        this.pageModule = pageModule;
    }

    public int getNiveauAcces() {
        return niveauAcces;
    }

    public void setNiveauAcces(int niveauAcces) {
        this.niveauAcces = niveauAcces;
    }

    public int getPageModule() {
        return pageModule;
    }

    public void setPageModule(int pageModule) {
        this.pageModule = pageModule;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) niveauAcces;
        hash += (int) pageModule;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsAutorisationPageModulePK)) {
            return false;
        }
        YvsAutorisationPageModulePK other = (YvsAutorisationPageModulePK) object;
        if (this.niveauAcces != other.niveauAcces) {
            return false;
        }
        if (this.pageModule != other.pageModule) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsAutorisationPageModulePK[ niveauAcces=" + niveauAcces + ", pageModule=" + pageModule + " ]";
    }
    
}
