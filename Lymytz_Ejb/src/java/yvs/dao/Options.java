/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GOUCHERE YVES
 */
public class Options implements Serializable, Comparable {

    private Object valeur;
    private String code;
    private String libelle;
    private String titre;
    private int position;
    private double quantite;
    private List<String> titres;

    public Options() {
        titres = new ArrayList<>();
    }

    public Options(Object valeur, int position) {
        this();
        this.valeur = valeur;
        this.position = position;
    }

    public Options(Object valeur, String libelle) {
        this(valeur, -1);
        this.libelle = libelle;
    }

    public Options(Object valeur, String libelle, int position) {
        this(valeur, position);
        this.libelle = libelle;
    }

    public Options(Object valeur, String code, String libelle, String titre, double quantite) {
        this(code, libelle, titre);
        this.valeur = valeur;
        this.quantite = quantite;
    }

    public Options(String code, String libelle, String titre) {
        this();
        this.code = code;
        this.libelle = libelle;
        this.titre = titre;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

    public Object getValeur() {
        return valeur;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<String> getTitres() {
        return titres;
    }

    public void setTitres(List<String> titres) {
        this.titres = titres;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.position;
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
        if (this.position != other.position) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        Options c = (Options) o;
        if (Integer.valueOf(position).equals(c.position)) {
            return String.valueOf(valeur).compareTo(c.valeur.toString());
        }
        return Integer.valueOf(position).compareTo(c.position);
    }

    @Override
    public String toString() {
        return "Options{" + "position=" + position + ", valeur=" + valeur + '}';
    }
}
