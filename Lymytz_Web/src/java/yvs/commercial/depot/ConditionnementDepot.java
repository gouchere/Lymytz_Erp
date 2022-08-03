/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.depot;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Conditionnement;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ConditionnementDepot implements Serializable {

    private long id;
    private DepotOperation operation = new DepotOperation();
    private Conditionnement conditionnement = new Conditionnement();
    private ArticleDepot article = new ArticleDepot();

    public ConditionnementDepot() {
    }

    public ConditionnementDepot(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DepotOperation getOperation() {
        return operation;
    }

    public void setOperation(DepotOperation operation) {
        this.operation = operation;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public ArticleDepot getArticle() {
        return article;
    }

    public void setArticle(ArticleDepot article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ConditionnementDepot other = (ConditionnementDepot) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
