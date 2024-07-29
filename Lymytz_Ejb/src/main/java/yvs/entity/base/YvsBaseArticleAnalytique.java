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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import yvs.dao.YvsEntity;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_article_analytique")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleAnalytique.findAll", query = "SELECT y FROM YvsBaseArticleAnalytique y"),
    @NamedQuery(name = "YvsBaseArticleAnalytique.findById", query = "SELECT y FROM YvsBaseArticleAnalytique y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleAnalytique.findByArticleCentre", query = "SELECT y FROM YvsBaseArticleAnalytique y WHERE y.article = :article OR y.centre =:centre"),
    @NamedQuery(name = "YvsBaseArticleAnalytique.findByArticleCentres", query = "SELECT y FROM YvsBaseArticleAnalytique y WHERE y.article = :article AND y.centre =:centre"),
    @NamedQuery(name = "YvsBaseArticleAnalytique.findByArticle", query = "SELECT y FROM YvsBaseArticleAnalytique y JOIN FETCH y.centre WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleAnalytique.findByCentre", query = "SELECT y FROM YvsBaseArticleAnalytique y WHERE y.centre =:centre"),
    @NamedQuery(name = "YvsBaseArticleAnalytique.findByPourcentage", query = "SELECT y FROM YvsBaseArticleAnalytique y WHERE y.coefficient = :coefficient")})
public class YvsBaseArticleAnalytique extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_article_analytique_id_seq", name = "yvs_base_article_analytique_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_article_analytique_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coefficient")
    private Double coefficient;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "centre", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCentreAnalytique centre;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean new_;

    public YvsBaseArticleAnalytique() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseArticleAnalytique(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Double getCoefficient() {
        return coefficient != null ? coefficient : 0;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaCentreAnalytique getCentre() {
        return centre;
    }

    public void setCentre(YvsComptaCentreAnalytique centre) {
        this.centre = centre;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsBaseArticleAnalytique)) {
            return false;
        }
        YvsBaseArticleAnalytique other = (YvsBaseArticleAnalytique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.prod.YvsBaseArticleAnalytique[ id=" + id + " ]";
    }

}
