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
public class YvsAutorisationRessourcesPagePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "niveau_acces")
    private int niveauAcces;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ressource_page")
    private int ressourcePage;

    public YvsAutorisationRessourcesPagePK() {
    }

    public YvsAutorisationRessourcesPagePK(int niveauAcces, int ressourcePage) {
        this.niveauAcces = niveauAcces;
        this.ressourcePage = ressourcePage;
    }

    public int getNiveauAcces() {
        return niveauAcces;
    }

    public void setNiveauAcces(int niveauAcces) {
        this.niveauAcces = niveauAcces;
    }

    public int getRessourcePage() {
        return ressourcePage;
    }

    public void setRessourcePage(int ressourcePage) {
        this.ressourcePage = ressourcePage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) niveauAcces;
        hash += (int) ressourcePage;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsAutorisationRessourcesPagePK)) {
            return false;
        }
        YvsAutorisationRessourcesPagePK other = (YvsAutorisationRessourcesPagePK) object;
        if (this.niveauAcces != other.niveauAcces) {
            return false;
        }
        if (this.ressourcePage != other.ressourcePage) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.users.YvsAutorisationRessourcesPagePK[ niveauAcces=" + niveauAcces + ", ressourcePage=" + ressourcePage + " ]";
    }
    
}
