/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.grh.salaire;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user1
 */
@Embeddable
public class YvsGrhDepenseNotePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "note_frais")
    private long noteFrais;
    @Basic(optional = false)
    @NotNull
    @Column(name = "type_depense")
    private int typeDepense;

    public YvsGrhDepenseNotePK() {
    }

    public YvsGrhDepenseNotePK(long noteFrais, int typeDepense) {
        this.noteFrais = noteFrais;
        this.typeDepense = typeDepense;
    }

    public long getNoteFrais() {
        return noteFrais;
    }

    public void setNoteFrais(long noteFrais) {
        this.noteFrais = noteFrais;
    }

    public int getTypeDepense() {
        return typeDepense;
    }

    public void setTypeDepense(int typeDepense) {
        this.typeDepense = typeDepense;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) noteFrais;
        hash += (int) typeDepense;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsGrhDepenseNotePK)) {
            return false;
        }
        YvsGrhDepenseNotePK other = (YvsGrhDepenseNotePK) object;
        if (this.noteFrais != other.noteFrais) {
            return false;
        }
        if (this.typeDepense != other.typeDepense) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhDepenseNotePK[ noteFrais=" + noteFrais + ", typeDepense=" + typeDepense + " ]";
    }
    
}
