/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.produits;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author GOUCHERE YVES
 */
@Embeddable
public class YvsArticleComptePk implements Serializable {

    private long article;
    private long categorie;

    public YvsArticleComptePk() {
    }

    public YvsArticleComptePk(long article, long categorie) {
        this.article = article;
        this.categorie = categorie;
    }

    public long getCategorie() {
        return categorie;
    }

    public void setCategorie(long categorie) {
        this.categorie = categorie;
    }

    public long getArticle() {
        return article;
    }

    public void setArticle(long article) {
        this.article = article;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsArticleComptePk other = (YvsArticleComptePk) obj;
        if (this.article != other.article) {
            return false;
        }
        if (this.categorie != other.categorie) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.article ^ (this.article >>> 32));
        hash = 29 * hash + (int) (this.categorie ^ (this.categorie >>> 32));
        return hash;
    }
}
