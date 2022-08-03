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
import yvs.entity.base.YvsBaseArticleDepot;

@ManagedBean(name = "articleDepotService", eager = true)
@SessionScoped
public class ArticleDepotService implements Serializable {

    private List<YvsBaseArticleDepot> articles;

    public ArticleDepotService() {
        articles = new ArrayList<>();
    }

    public ArticleDepotService(List<YvsBaseArticleDepot> articles) {
        this.articles = articles;
    }

    public List<YvsBaseArticleDepot> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticleDepot> articles) {
        this.articles = articles;
    }
}
