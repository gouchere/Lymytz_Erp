/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.recrutement.entretien;

import java.io.Serializable;
import yvs.grh.bean.Langues;

/**
 *
 * @author GOUCHERE YVES
 */
public class LangueCandidat extends Langues implements Serializable {

    private long idLangueCandidat;

    public LangueCandidat(int idLangueCandidat) {
        this.idLangueCandidat = idLangueCandidat;
    }

    public LangueCandidat() {
    }

    public long getIdLangueCandidat() {
        return idLangueCandidat;
    }

    public void setIdLangueCandidat(long idLangueCandidat) {
        this.idLangueCandidat = idLangueCandidat;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (this.idLangueCandidat ^ (this.idLangueCandidat >>> 32));
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
        final LangueCandidat other = (LangueCandidat) obj;
        if (this.idLangueCandidat != other.idLangueCandidat) {
            return false;
        }
        return true;
    }

}
