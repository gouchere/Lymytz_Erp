/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author GOUCHERE YVES
 */
public class Options implements Serializable {

    private long id;
    private String valeur;
    private String libelle;
    private String titre;
    private List<String> titres;

    public Options() {
        titres = new ArrayList<>();
    }

    public Options(String libelle) {
        this();
        this.libelle = libelle;
    }

    public Options(String valeur, String libelle) {
        this(libelle);
        this.valeur = valeur;
    }

    public Options(long id, String valeur, String libelle) {
        this(valeur, libelle);
        this.id = id;
    }

    public Options(String valeur, String libelle, String titre) {
        this(valeur, libelle);
        this.titre = titre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public List<String> getTitres() {
        return titres;
    }

    public void setTitres(List<String> titres) {
        this.titres = titres;
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
