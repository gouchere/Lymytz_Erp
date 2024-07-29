/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.produits.group.YvsBaseGroupesArticle;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class GroupeArticle implements Serializable {

    private long id;
    private String reference;
    private String designation;
    private String description;
    private String photo;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private boolean actif = true;
    private GroupeArticle parent;
    private List<YvsBaseGroupesArticle> sous;

    public GroupeArticle() {
        sous = new ArrayList<>();
    }

    public GroupeArticle(long id) {
        this();
        this.id = id;
    }

    public GroupeArticle(long id, String reference, String designation) {
        this(id);
        this.reference = reference;
        this.designation = designation;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public List<YvsBaseGroupesArticle> getSous() {
        return sous;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setSous(List<YvsBaseGroupesArticle> sous) {
        this.sous = sous;
    }

    public GroupeArticle getParent() {
        return parent;
    }

    public void setParent(GroupeArticle parent) {
        this.parent = parent;
    }
        

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final GroupeArticle other = (GroupeArticle) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
