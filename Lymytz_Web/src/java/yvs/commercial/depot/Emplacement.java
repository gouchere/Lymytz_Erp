/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.depot;

import java.io.Serializable;import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.parametrage.entrepot.Depots;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Emplacement implements Serializable {

    private long id;
    private String code;
    private String designation;
    private String description;
    private boolean defaut;
    private boolean actif;
    private List<ArticleEmplacement> articles;
    private Depots depot = new Depots();
    private List<Emplacement> fils;
    private Emplacement parent;
    private long idExterne;
    private Date dateSave = new Date();
    private boolean selectActif, new_, update;

    public Emplacement() {
        articles = new ArrayList<>();
    }

    public Emplacement(long id) {
        this.id = id;
        articles = new ArrayList<>();
    }

    public long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(long idExterne) {
        this.idExterne = idExterne;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDefaut() {
        return defaut;
    }

    public void setDefaut(boolean defaut) {
        this.defaut = defaut;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<ArticleEmplacement> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleEmplacement> articles) {
        this.articles = articles;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public List<Emplacement> getFils() {
        return fils;
    }

    public void setFils(List<Emplacement> fils) {
        this.fils = fils;
    }

    public Emplacement getParent() {
        return parent != null ? parent : new Emplacement();
    }

    public void setParent(Emplacement parent) {
        this.parent = parent;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Emplacement other = (Emplacement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
