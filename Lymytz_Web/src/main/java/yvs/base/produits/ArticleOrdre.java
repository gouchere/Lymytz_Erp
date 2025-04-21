/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.produits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.YvsBaseArticles;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;

/**
 *
 * @author Lymytz-pc
 */
public class ArticleOrdre implements Serializable {

    private Integer niveau;
    private List<YvsBaseArticles> articles;
    PaginatorResult<YvsBaseArticles> paginator = new PaginatorResult<YvsBaseArticles>();

    public ArticleOrdre(int niveu, YvsSocietes societes) {
        this.niveau = niveu;
        articles = new ArrayList<>();
        paginator.addParam(new ParametreRequete("y.famille.societe", "societe", societes, "=", "AND"));

        paginator.addParam(new ParametreRequete("y.ordre", "ordre", niveu, "=", "AND"));
    }

    public Integer getNiveau() {
        return niveau;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public List<YvsBaseArticles> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticles> articles) {
        this.articles = articles;
    }

    public PaginatorResult<YvsBaseArticles> getPaginator() {
        return paginator;
    }

    public void setPaginator(PaginatorResult<YvsBaseArticles> paginator) {
        this.paginator = paginator;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.niveau);
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
        final ArticleOrdre other = (ArticleOrdre) obj;
        if (!Objects.equals(this.niveau, other.niveau)) {
            return false;
        }
        return true;
    }
    
    

}
