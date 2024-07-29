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
import yvs.entity.produits.YvsBaseArticleContenuPack;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ArticlePack implements Serializable {

    private long id;
    private String designation;
    private String photo;
    private double montant;
    private Conditionnement article = new Conditionnement();
    private Date dateSave = new Date();
    private List<YvsBaseArticleContenuPack> contenus;

    public ArticlePack() {
        contenus = new ArrayList<>();
    }

    public ArticlePack(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Conditionnement getArticle() {
        return article;
    }

    public void setArticle(Conditionnement article) {
        this.article = article;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsBaseArticleContenuPack> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsBaseArticleContenuPack> contenus) {
        this.contenus = contenus;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ArticlePack other = (ArticlePack) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
