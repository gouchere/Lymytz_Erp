/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Columns implements Serializable, Comparable {

    private Object valeur;//column_1
    private String code;//column_2
    private String libelle;//column_3
    private String titre;//column_4
    private int position;//column_5
    private double quantite;//column_6
    private List<String> titres;

    public Columns() {
        titres = new ArrayList<>();
    }

    public Columns(Object valeur, int position) {
        this();
        this.valeur = valeur;
        this.position = position;
    }

    public Columns(Object valeur, String libelle) {
        this(valeur, 0);
        this.libelle = libelle;
    }

    public Columns(Object valeur, String libelle, int position) {
        this(valeur, position);
        this.libelle = libelle;
    }

    public Columns(Object valeur, String libelle, int position, double quantite) {
        this(valeur, libelle, position);
        this.quantite = quantite;
    }

    public Columns(Object valeur, String code, String libelle, String titre, double quantite) {
        this(code, libelle, titre);
        this.valeur = valeur;
        this.quantite = quantite;
    }

    public Columns(String code, String libelle, String titre) {
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
        final Columns other = (Columns) obj;
        if (this.position != other.position) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        Columns c = (Columns) o;
        if (Integer.valueOf(position).equals(c.position)) {
            return String.valueOf(valeur).compareTo(c.valeur.toString());
        }
        return Integer.valueOf(position).compareTo(c.position);
    }

    public Object get(String row) {
        switch (row) {
            case "column_1": {
                return valeur;
            }
            case "column_2": {
                return code;
            }
            case "column_3": {
                return libelle;
            }
            case "column_4": {
                return titre;
            }
            case "column_5": {
                return position;
            }
            case "column_6": {
                return quantite;
            }
        }
        return "";
    }

}
