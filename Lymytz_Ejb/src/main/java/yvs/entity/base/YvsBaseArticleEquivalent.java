/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_article_equivalent")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleEquivalent.findAll", query = "SELECT y FROM YvsBaseArticleEquivalent y"),
    @NamedQuery(name = "YvsBaseArticleEquivalent.findById", query = "SELECT y FROM YvsBaseArticleEquivalent y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleEquivalent.findByEquivalence", query = "SELECT y FROM YvsBaseArticleEquivalent y WHERE y.article = :article OR y.articleEquivalent =:article"),
    @NamedQuery(name = "YvsBaseArticleEquivalent.findByArticle", query = "SELECT y FROM YvsBaseArticleEquivalent y JOIN FETCH y.articleEquivalent WHERE y.article = :article AND y.articleEquivalent =:articleEquivalent"),
    @NamedQuery(name = "YvsBaseArticleEquivalent.findByArticle1", query = "SELECT y FROM YvsBaseArticleEquivalent y JOIN FETCH y.articleEquivalent WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleEquivalent.findByPourcentage", query = "SELECT y FROM YvsBaseArticleEquivalent y WHERE y.pourcentage = :pourcentage")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseArticleEquivalent extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_article_equivalent_id_seq", name = "yvs_prod_article_equivalent_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_article_equivalent_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "article_equivalent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles articleEquivalent;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsBaseArticleEquivalent() {
    }

    public YvsBaseArticleEquivalent(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Double pourcentage) {
        this.pourcentage = pourcentage;
    }

    public YvsBaseArticles getArticleEquivalent() {
        return articleEquivalent;
    }

    public void setArticleEquivalent(YvsBaseArticles articleEquivalent) {
        this.articleEquivalent = articleEquivalent;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
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
        if (!(object instanceof YvsBaseArticleEquivalent)) {
            return false;
        }
        YvsBaseArticleEquivalent other = (YvsBaseArticleEquivalent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.prod.YvsBaseArticleEquivalent[ id=" + id + " ]";
    }

}
