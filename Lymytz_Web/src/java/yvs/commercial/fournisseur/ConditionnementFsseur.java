/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.fournisseur;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.ArticleFournisseur;
import yvs.base.produits.Conditionnement;
import yvs.base.produits.UniteMesure;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ConditionnementFsseur implements Serializable {

    private long id;
    private double pua;
    private boolean principal;
    private Conditionnement conditionnement = new Conditionnement();
    private UniteMesure unite = new UniteMesure();
    private ArticleFournisseur article = new ArticleFournisseur();

    public ConditionnementFsseur() {
    }

    public ConditionnementFsseur(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPua() {
        return pua;
    }

    public void setPua(double pua) {
        this.pua = pua;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public ArticleFournisseur getArticle() {
        return article;
    }

    public void setArticle(ArticleFournisseur article) {
        this.article = article;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public UniteMesure getUnite() {
        return unite;
    }

    public void setUnite(UniteMesure unite) {
        this.unite = unite;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ConditionnementFsseur other = (ConditionnementFsseur) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
