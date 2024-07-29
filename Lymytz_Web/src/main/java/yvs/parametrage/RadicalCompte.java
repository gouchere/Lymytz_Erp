/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Objects;

/**
 *
 * @author GOUCHERE YVES
 */
public class RadicalCompte implements Serializable {

    private long id;
    private String radical;
    private int numero;

    public RadicalCompte() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRadical() {
        return radical;
    }

    public void setRadical(String radical) {
        this.radical = radical;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RadicalCompte other = (RadicalCompte) obj;
        if (!Objects.equals(this.radical, other.radical)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.radical);
        return hash;
    }
}
