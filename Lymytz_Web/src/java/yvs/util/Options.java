/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author GOUCHERE YVES
 */
public class Options implements Serializable {

    private String valeur;
    private String libelle;

    public Options() {
    }

    public Options(String libele) {
        this.libelle = libele;
    }

    public Options(String valeur, String libele) {
        this.valeur = valeur;
        this.libelle = libele;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.libelle);
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
        final Options other = (Options) obj;
        if (!Objects.equals(this.libelle, other.libelle)) {
            return false;
        }
        return true;
    }

}
