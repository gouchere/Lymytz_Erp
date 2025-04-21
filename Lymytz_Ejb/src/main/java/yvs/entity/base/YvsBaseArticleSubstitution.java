/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_article_substitution")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleSubstitution.findAll", query = "SELECT y FROM YvsBaseArticleSubstitution y"),
    @NamedQuery(name = "YvsBaseArticleSubstitution.findById", query = "SELECT y FROM YvsBaseArticleSubstitution y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleSubstitution.findBySubstituance", query = "SELECT y FROM YvsBaseArticleSubstitution y WHERE y.article = :article OR y.articleSubstitution =:article"),
    @NamedQuery(name = "YvsBaseArticleSubstitution.findByArticle", query = "SELECT y FROM YvsBaseArticleSubstitution y JOIN FETCH y.articleSubstitution WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleSubstitution.findByArticles", query = "SELECT y FROM YvsBaseArticleSubstitution y WHERE y.article = :article AND y.articleSubstitution =:articleSubstitution"),
    @NamedQuery(name = "YvsBaseArticleSubstitution.findByPourcentage", query = "SELECT y FROM YvsBaseArticleSubstitution y WHERE y.pourcentage = :pourcentage")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseArticleSubstitution extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_article_substitution_id_seq", name = "yvs_prod_article_substitution_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_article_substitution_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pourcentage")
    private Double pourcentage;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "article_substitution", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles articleSubstitution;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsBaseArticleSubstitution() {
    }

    public YvsBaseArticleSubstitution(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsBaseArticles getArticleSubstitution() {
        return articleSubstitution;
    }

    public void setArticleSubstitution(YvsBaseArticles articleSubstitution) {
        this.articleSubstitution = articleSubstitution;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsBaseArticleSubstitution)) {
            return false;
        }
        YvsBaseArticleSubstitution other = (YvsBaseArticleSubstitution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.prod.YvsBaseArticleSubstitution[ id=" + id + " ]";
    }

}
