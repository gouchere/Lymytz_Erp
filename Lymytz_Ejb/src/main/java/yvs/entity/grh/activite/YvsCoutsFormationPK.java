/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author LYMYTZ-PC
 */
@Embeddable
public class YvsCoutsFormationPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "formation")
    private int formation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "type_cout")
    private int typeCout;

    public YvsCoutsFormationPK() {
    }

    public YvsCoutsFormationPK(int formation, int typeCout) {
        this.formation = formation;
        this.typeCout = typeCout;
    }

    public int getFormation() {
        return formation;
    }

    public void setFormation(int formation) {
        this.formation = formation;
    }

    public int getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(int typeCout) {
        this.typeCout = typeCout;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) formation;
        hash += (int) typeCout;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsCoutsFormationPK)) {
            return false;
        }
        YvsCoutsFormationPK other = (YvsCoutsFormationPK) object;
        if (this.formation != other.formation) {
            return false;
        }
        if (this.typeCout != other.typeCout) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsCoutsFormationPK[ formation=" + formation + ", typeCout=" + typeCout + " ]";
    }

}
