/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.base;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Objects;

/**
 *
 * @author Lymytz
 */
public class TypeValeur implements Serializable {

    private String libelle;
    private String description;

    public TypeValeur() {
    }

    public TypeValeur(String libelle) {
        this.libelle = libelle;
    }

    public TypeValeur(String libelle, String description) {
        this.libelle = libelle;
        this.description = description;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.libelle);
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
        final TypeValeur other = (TypeValeur) obj;
        if (!Objects.equals(this.libelle, other.libelle)) {
            return false;
        }
        return true;
    }
}
