/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.achat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Conditionnement;
import yvs.commercial.depot.ArticleDepot;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ArticleApprovisionnement implements Serializable {

    private long id;
    private double quantite, quantiteRest;
    private Date dateLivraison = new Date();
    private Date dateSave = new Date();
    private ArticleDepot article = new ArticleDepot();
    private FicheApprovisionnement fiche = new FicheApprovisionnement();
    private List<ArticleFourniAchat> articles;
    private Conditionnement conditionnement = new Conditionnement();
    private boolean selectActif, new_, int_, update;
    private double stock;

    public ArticleApprovisionnement() {
        articles = new ArrayList<>();
    }

    public ArticleApprovisionnement(long id) {
        this.id = id;
        articles = new ArrayList<>();
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isInt_() {
        return int_;
    }

    public void setInt_(boolean int_) {
        this.int_ = int_;
    }

    public double getQuantiteRest() {
        return quantiteRest;
    }

    public void setQuantiteRest(double quantiteRest) {
        this.quantiteRest = quantiteRest;
    }

    public FicheApprovisionnement getFiche() {
        return fiche;
    }

    public void setFiche(FicheApprovisionnement fiche) {
        this.fiche = fiche;
    }

    public List<ArticleFourniAchat> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleFourniAchat> articles) {
        this.articles = articles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Date getDateLivraison() {
        return dateLivraison != null ? dateLivraison : new Date();
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public ArticleDepot getArticle() {
        return article;
    }

    public void setArticle(ArticleDepot article) {
        this.article = article;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
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

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ArticleApprovisionnement other = (ArticleApprovisionnement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
