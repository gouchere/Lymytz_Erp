/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.theme;

/**
 *
 * @author LYMYTZ
 */
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.produits.YvsBaseArticles;

@ManagedBean(name = "articleComService", eager = true)
@SessionScoped
public class ArticleComService implements Serializable{

    private List<YvsBaseArticles> articles;

    public ArticleComService() {
        articles = new ArrayList<>();
    }

    public ArticleComService(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }
}
