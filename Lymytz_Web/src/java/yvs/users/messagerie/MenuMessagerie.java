/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Objects;

/**
 *
 * @author LYMYTZ
 */
public class MenuMessagerie implements Serializable {

    private int id;
    private String libelle;
    private String data;
    private String iconeBefore;
    private String iconeAfter;
    private String titleIconeAfter;
    private String title;

    public MenuMessagerie() {
    }

    public MenuMessagerie(int id) {
        this.id = id;
    }

    public MenuMessagerie(int id, String data) {
        this.id = id;
        this.data = data;
    }

    public MenuMessagerie(int id, String libelle, String data) {
        this.id = id;
        this.libelle = libelle;
        this.data = data;
        this.iconeBefore = "empty.png";
        this.iconeAfter = "empty.png";
        this.titleIconeAfter = "";
        this.title = "";
    }

    public MenuMessagerie(int id, String libelle, String data, String iconeBefore) {
        this.id = id;
        this.libelle = libelle;
        this.data = data;
        this.iconeBefore = iconeBefore;
        this.iconeAfter = "empty.png";
        this.titleIconeAfter = "";
        this.title = "";
    }

    public MenuMessagerie(int id, String libelle, String data, String iconeBefore, String iconeAfter) {
        this.id = id;
        this.libelle = libelle;
        this.data = data;
        this.iconeBefore = iconeBefore;
        this.iconeAfter = iconeAfter;
        this.titleIconeAfter = "";
        this.title = "";
    }

    public MenuMessagerie(int id, String libelle, String data, String iconeBefore, String iconeAfter, String titleIconeAfter) {
        this.id = id;
        this.libelle = libelle;
        this.data = data;
        this.iconeBefore = iconeBefore;
        this.iconeAfter = iconeAfter;
        this.titleIconeAfter = titleIconeAfter;
        this.title = "";
    }

    public MenuMessagerie(int id, String libelle, String data, String iconeBefore, String iconeAfter, String titleIconeAfter, String title) {
        this.id = id;
        this.libelle = libelle;
        this.data = data;
        this.iconeBefore = iconeBefore;
        this.iconeAfter = iconeAfter;
        this.titleIconeAfter = titleIconeAfter;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconeBefore() {
        return iconeBefore;
    }

    public void setIconeBefore(String iconeBefore) {
        this.iconeBefore = iconeBefore;
    }

    public String getIconeAfter() {
        return iconeAfter;
    }

    public void setIconeAfter(String iconeAfter) {
        this.iconeAfter = iconeAfter;
    }

    public String getTitleIconeAfter() {
        return titleIconeAfter;
    }

    public void setTitleIconeAfter(String titleIconeAfter) {
        this.titleIconeAfter = titleIconeAfter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.id;
        hash = 23 * hash + Objects.hashCode(this.data);
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
        final MenuMessagerie other = (MenuMessagerie) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }

}
