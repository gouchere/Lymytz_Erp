/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean()
@SessionScoped
public class ArticleSubstitution {

    private long id;
    private Articles article = new Articles();
    private Articles articleLie = new Articles();

    public ArticleSubstitution() {

    }

    public ArticleSubstitution(long id) {
        this.id = id;
    }

    public ArticleSubstitution(long id, Articles a1, Articles a2) {
        this.id = id;
        this.article = a1;
        this.articleLie = a2;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public Articles getArticleLie() {
        return articleLie;
    }

    public void setArticleLie(Articles articleLie) {
        this.articleLie = articleLie;
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
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ArticleSubstitution other = (ArticleSubstitution) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ArticleSubstitution{" + "id=" + id + ", article=" + article + ", articleLie=" + articleLie + '}';
    }

    
}
