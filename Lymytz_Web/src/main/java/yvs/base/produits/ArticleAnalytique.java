/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.comptabilite.analytique.SectionAnalytique;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class ArticleAnalytique extends SectionAnalytique {

    private Articles article = new Articles();

    public ArticleAnalytique() {
        super();
    }

    public ArticleAnalytique(long id) {
        super(id);
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return "ArticleAnalytique{" + "id=" + id + ", article=" + article + ", centre=" + centre + '}';
    }

}
