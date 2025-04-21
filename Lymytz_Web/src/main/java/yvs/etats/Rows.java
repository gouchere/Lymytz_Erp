/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Rows implements Serializable, Comparable {

    private long primaire;//row_1
    private String code;//row_2
    private String libelle;//row_3
    private long secondaire;//row_4
    private String titre;//row_5
    private Object valeur;//row_6
    private int position;//row_7
    private String autres = "";//row_8
    private double quantite;//row_9
    private double prix;//row_10
    private boolean footer;
    private List<String> titres;
    private List<Rows> unites;

    public Rows() {
        this.titres = new ArrayList<>();
        this.unites = new ArrayList<>();
    }

    public Rows(long primaire) {
        this();
        this.primaire = primaire;
    }

    public Rows(String libelle) {
        this();
        this.libelle = libelle;
    }

    public Rows(int position, String libelle) {
        this(libelle);
        this.position = position;
    }

    public Rows(double prix, String titre) {
        this();
        this.titre = titre;
        this.quantite = prix;
    }

    public Rows(String titre, String libelle) {
        this(libelle);
        this.titre = titre;
    }

    public Rows(int position, String libelle, String autres) {
        this(position, libelle);
        this.autres = autres;
    }

    public Rows(String titre, String libelle, String autres) {
        this(titre, libelle);
        this.autres = autres;
    }

    public Rows(int position, String libelle, String autres, boolean footer) {
        this(position, libelle, autres);
        this.footer = footer;
    }

    public Rows(String titre, String libelle, String autres, boolean footer) {
        this(titre, libelle, autres);
        this.footer = footer;
    }

    public Rows(int position, long primaire, String libelle, String autres, boolean footer) {
        this(position, libelle, autres, footer);
        this.primaire = primaire;
    }

    public Rows(long primaire, String titre, String libelle) {
        this(titre, libelle);
        this.primaire = primaire;
    }

    public Rows(long primaire, String titre, String libelle, double quantite, double prix) {
        this(primaire, titre, libelle);
        this.quantite = quantite;
        this.prix = prix;
    }

    public Rows(long primaire, String titre, String libelle, double quantite, double prix, boolean footer) {
        this(primaire, titre, libelle, quantite, prix);
        this.footer = footer;
    }

    public Rows(long primaire, String code, String libelle, String titre, double quantite, double prix, boolean footer) {
        this(primaire, titre, libelle, quantite, prix, footer);
        this.code = code;
    }

    public Rows(long primaire, String titre, String libelle, String autres) {
        this(titre, libelle, autres);
        this.primaire = primaire;
    }

    public Rows(long primaire, String titre, String libelle, String autres, boolean footer) {
        this(titre, libelle, autres, footer);
        this.primaire = primaire;
    }

    public Rows(long primaire, long secondaire, String titre, String libelle, String autres) {
        this(primaire, titre, libelle, autres);
        this.secondaire = secondaire;
    }

    public Rows(long primaire, long secondaire, String titre, String libelle, String autres, boolean footer) {
        this(primaire, titre, libelle, autres, footer);
        this.secondaire = secondaire;
    }

    public long getSecondaire() {
        return secondaire;
    }

    public void setSecondaire(long secondaire) {
        this.secondaire = secondaire;
    }

    public Object getValeur() {
        return valeur;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public List<String> getTitres() {
        return titres;
    }

    public void setTitres(List<String> titres) {
        this.titres = titres;
    }

    public long getPrimaire() {
        return primaire;
    }

    public void setPrimaire(long primaire) {
        this.primaire = primaire;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public String getAutres() {
        return autres;
    }

    public void setAutres(String autres) {
        this.autres = autres;
    }

    public boolean isFooter() {
        return footer;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }

    public List<Rows> getUnites() {
        return unites;
    }

    public void setUnites(List<Rows> unites) {
        this.unites = unites;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.position;
        hash = 53 * hash + Objects.hashCode(this.titre);
        hash = 53 * hash + Objects.hashCode(this.libelle);
        hash = 53 * hash + Objects.hashCode(this.autres);
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
        final Rows other = (Rows) obj;
        if (this.position != other.position) {
            return false;
        }
        if (!Objects.equals(this.titre, other.titre)) {
            return false;
        }
        if (!Objects.equals(this.libelle, other.libelle)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        Rows c = (Rows) o;
        if (Integer.valueOf(position).equals(c.position)) {
            return String.valueOf(valeur).compareTo(c.valeur.toString());
        }
        return Integer.valueOf(position).compareTo(c.position);
    }

    public Object get(String row) {
        switch (row) {
            case "row_1": {
                return primaire;
            }
            case "row_2": {
                return code;
            }
            case "row_3": {
                return libelle;
            }
            case "row_4": {
                return secondaire;
            }
            case "row_5": {
                return titre;
            }
            case "row_6": {
                return valeur;
            }
            case "row_7": {
                return position;
            }
            case "row_8": {
                return autres;
            }
            case "row_9": {
                return quantite;
            }
            case "row_10": {
                return prix;
            }
        }
        return "";
    }
}
