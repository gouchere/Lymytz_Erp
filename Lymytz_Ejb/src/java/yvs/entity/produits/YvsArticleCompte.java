/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.produits;

import java.io.Serializable;
import javax.persistence.*;
import yvs.entity.compta.YvsBasePlanComptable;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_article_compte")
@NamedQueries({
    @NamedQuery(name = "YvsArticleCompte.findAll", query = "SELECT y FROM YvsArticleCompte y WHERE y.article.id=:gr")
})
public class YvsArticleCompte implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private YvsArticleComptePk id;
    @JoinColumn(name = "article", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "compte", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;

    public YvsArticleCompte() {
    }

    public YvsArticleCompte(YvsArticleComptePk id) {
        this.id = id;
    }

    public YvsArticleComptePk getId() {
        return id;
    }

    public void setId(YvsArticleComptePk id) {
        this.id = id;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsArticleCompte)) {
            return false;
        }
        YvsArticleCompte other = (YvsArticleCompte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.group.YvsGroupeArticleCompte[ id=" + id + " ]";
    }
}
