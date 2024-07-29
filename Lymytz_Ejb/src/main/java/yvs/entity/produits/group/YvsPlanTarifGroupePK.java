/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.produits.group;

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
public class YvsPlanTarifGroupePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "categorie")
    private long categorie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "groupe")
    private long groupe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_agence")
    private long idAgence;

    public YvsPlanTarifGroupePK() {
    }

    public YvsPlanTarifGroupePK(long categorie, long groupe, long idAgence) {
        this.categorie = categorie;
        this.groupe = groupe;
        this.idAgence = idAgence;
    }

    public long getCategorie() {
        return categorie;
    }

    public void setCategorie(long categorie) {
        this.categorie = categorie;
    }

    public long getGroupe() {
        return groupe;
    }

    public void setGroupe(long groupe) {
        this.groupe = groupe;
    }

    public long getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(long idAgence) {
        this.idAgence = idAgence;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) categorie;
        hash += (int) groupe;
        hash += (int) idAgence;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsPlanTarifGroupePK)) {
            return false;
        }
        YvsPlanTarifGroupePK other = (YvsPlanTarifGroupePK) object;
        if (this.categorie != other.categorie) {
            return false;
        }
        if (this.groupe != other.groupe) {
            return false;
        }
        if (this.idAgence != other.idAgence) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.group.YvsPlanTarifGroupePK[ categorie=" + categorie + ", groupe=" + groupe + ", idAgence=" + idAgence + " ]";
    }
    
}
