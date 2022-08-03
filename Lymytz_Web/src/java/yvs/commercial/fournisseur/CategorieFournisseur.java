/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.fournisseur;

import java.io.Serializable;import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class CategorieFournisseur implements Serializable {

    private long id;
    private String code;
    private String libelle;
    private String description;
    private List<CategorieFournisseur> fils;
    private Date dateSave = new Date();
    private CategorieFournisseur parent;
    private boolean selectActif, new_;

    public CategorieFournisseur() {
        fils = new ArrayList<>();
    }

    public CategorieFournisseur(long id) {
        this.id = id;
        fils = new ArrayList<>();
    }

    public CategorieFournisseur(long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public CategorieFournisseur getParent() {
        return parent;
    }

    public void setParent(CategorieFournisseur parent) {
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

    public List<CategorieFournisseur> getFils() {
        return fils;
    }

    public void setFils(List<CategorieFournisseur> fils) {
        this.fils = fils;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final CategorieFournisseur other = (CategorieFournisseur) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
