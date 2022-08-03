/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Conditionnement;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class Publicites implements Serializable {

    private long id;
    private int priorite = 1;
    private String description;
    private String image;
    private String url;
    private boolean permanent = true;
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Date dateSave = new Date();
    private Conditionnement article = new Conditionnement();

    public Publicites() {
    }

    public Publicites(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPriorite() {
        return priorite;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Conditionnement getArticle() {
        return article;
    }

    public void setArticle(Conditionnement article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Publicites other = (Publicites) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Publicites{" + "id=" + id + '}';
    }

}
