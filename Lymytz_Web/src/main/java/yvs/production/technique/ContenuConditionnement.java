/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.technique;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ContenuConditionnement implements Serializable {

    private long id;
    private Articles article = new Articles();
    private boolean calculPr;
    private FicheConditionnement fiche = new FicheConditionnement();
    private Conditionnement condition = new Conditionnement();
    private double quantite;
    private boolean consommable;
    private Date dateSave = new Date();

    public ContenuConditionnement() {
    }

    public ContenuConditionnement(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCalculPr() {
        return calculPr;
    }

    public void setCalculPr(boolean calculPr) {
        this.calculPr = calculPr;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public FicheConditionnement getFiche() {
        return fiche;
    }

    public void setFiche(FicheConditionnement fiche) {
        this.fiche = fiche;
    }

    public boolean isConsommable() {
        return consommable;
    }

    public void setConsommable(boolean consommable) {
        this.consommable = consommable;
    }

    public Conditionnement getCondition() {
        return condition;
    }

    public void setCondition(Conditionnement condition) {
        this.condition = condition;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final ContenuConditionnement other = (ContenuConditionnement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
