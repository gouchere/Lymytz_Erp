/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.entity.base.YvsBaseArticleCategorieComptableTaxe;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ArticlesCatComptable implements Serializable {

    private long id;
    private boolean actif;
    private Comptes compte = new Comptes();
    private CategorieComptable categorie = new CategorieComptable();
    private Articles article = new Articles();
    private TemplateArticles template = new TemplateArticles();
    private List<YvsBaseArticleCategorieComptableTaxe> taxes;
    private boolean selectActif, new_, update;

    public ArticlesCatComptable() {
        taxes = new ArrayList<>();
    }

    public ArticlesCatComptable(long id) {
        this.id = id;
        taxes = new ArrayList<>();
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

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

    public CategorieComptable getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieComptable categorie) {
        this.categorie = categorie;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public TemplateArticles getTemplate() {
        return template;
    }

    public void setTemplate(TemplateArticles template) {
        this.template = template;
    }

    public List<YvsBaseArticleCategorieComptableTaxe> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsBaseArticleCategorieComptableTaxe> taxes) {
        this.taxes = taxes;
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
        int hash = 7;
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
        final ArticlesCatComptable other = (ArticlesCatComptable) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
