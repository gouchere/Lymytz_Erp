/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats;

import java.io.Serializable;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class LibelleJournal implements Serializable, Comparable {

    private int position = 0;
    private long primaire;
    private String titre = "";
    private String libelle = "";
    private String autres = "";
    private boolean footer;

    public LibelleJournal() {
    }

    public LibelleJournal(String libelle) {
        this.libelle = libelle;
    }

    public LibelleJournal(int position, String libelle) {
        this(libelle);
        this.position = position;
    }

    public LibelleJournal(String titre, String libelle) {
        this(libelle);
        this.titre = titre;
    }

    public LibelleJournal(int position, String libelle, String autres) {
        this(position, libelle);
        this.autres = autres;
    }

    public LibelleJournal(String titre, String libelle, String autres) {
        this(titre, libelle);
        this.autres = autres;
    }

    public LibelleJournal(int position, String libelle, String autres, boolean footer) {
        this(position, libelle, autres);
        this.footer = footer;
    }

    public LibelleJournal(String titre, String libelle, String autres, boolean footer) {
        this(titre, libelle, autres);
        this.footer = footer;
    }

    public LibelleJournal(int position, long primaire, String libelle, String autres, boolean footer) {
        this(position, libelle, autres, footer);
        this.primaire = primaire;
    }

    public LibelleJournal(long primaire, String titre, String libelle) {
        this(titre, libelle);
        this.primaire = primaire;
    }

    public LibelleJournal(long primaire, String titre, String libelle, String autres, boolean footer) {
        this(titre, libelle, autres, footer);
        this.primaire = primaire;
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
        final LibelleJournal other = (LibelleJournal) obj;
        if (this.position != other.position) {
            return false;
        }
        if (!Objects.equals(this.titre, other.titre)) {
            return false;
        }
        if (!Objects.equals(this.libelle, other.libelle)) {
            return false;
        }
        if (!Objects.equals(this.autres, other.autres)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        LibelleJournal c = (LibelleJournal) o;
        return Boolean.valueOf(c.footer).compareTo(footer);
    }

}
