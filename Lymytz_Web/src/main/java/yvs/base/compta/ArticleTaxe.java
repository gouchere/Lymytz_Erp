/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.ArticlesCatComptable;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ArticleTaxe implements Serializable {

    private long id;
    private boolean appRemise;
    private boolean actif;
    private Taxes taxe = new Taxes();
    private double montantTaux, montantTotal;
    private ArticlesCatComptable article = new ArticlesCatComptable();
    private boolean selectActif, new_, update;

    public ArticleTaxe() {
    }

    public ArticleTaxe(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontantTaux() {
        return montantTaux;
    }

    public void setMontantTaux(double montantTaux) {
        this.montantTaux = montantTaux;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public boolean isAppRemise() {
        return appRemise;
    }

    public void setAppRemise(boolean appRemise) {
        this.appRemise = appRemise;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Taxes getTaxe() {
        return taxe;
    }

    public void setTaxe(Taxes taxe) {
        this.taxe = taxe;
    }

    public ArticlesCatComptable getArticle() {
        return article;
    }

    public void setArticle(ArticlesCatComptable article) {
        this.article = article;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ArticleTaxe other = (ArticleTaxe) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
