/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

import yvs.base.produits.Articles;

/**
 *
 * @author hp Elite 8300
 */
public class PlanifArticles extends DataLigne {

    private long id;
    private Articles article;

    public PlanifArticles() {

    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PlanifArticles other = (PlanifArticles) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
