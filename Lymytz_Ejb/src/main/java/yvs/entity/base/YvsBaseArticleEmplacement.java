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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_article_emplacement")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleEmplacement.findAll", query = "SELECT y FROM YvsBaseArticleEmplacement y"),
    @NamedQuery(name = "YvsBaseArticleEmplacement.findById", query = "SELECT y FROM YvsBaseArticleEmplacement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleEmplacement.findByArticleEmplacement", query = "SELECT y FROM YvsBaseArticleEmplacement y WHERE y.article = :article AND y.emplacement = :emplacement"),
    @NamedQuery(name = "YvsBaseArticleEmplacement.findByArticle", query = "SELECT y FROM YvsBaseArticleEmplacement y WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleEmplacement.findByQuantite", query = "SELECT y FROM YvsBaseArticleEmplacement y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsBaseArticleEmplacement.findByEmplacement", query = "SELECT y FROM YvsBaseArticleEmplacement y WHERE y.emplacement = :emplacement"),
    @NamedQuery(name = "YvsBaseArticleEmplacement.findArticleByEmplacement", query = "SELECT y.article FROM YvsBaseArticleEmplacement y WHERE y.emplacement = :emplacement")})
public class YvsBaseArticleEmplacement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_article_emplacement_id_seq", name = "yvs_base_article_emplacement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_article_emplacement_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "quantite")
    private Double quantite;
    @JoinColumn(name = "emplacement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseEmplacementDepot emplacement;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticleDepot article;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsBaseArticleEmplacement() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseArticleEmplacement(Long id) {
        this();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public YvsBaseEmplacementDepot getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(YvsBaseEmplacementDepot emplacement) {
        this.emplacement = emplacement;
    }

    public YvsBaseArticleDepot getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticleDepot article) {
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
        if (!(object instanceof YvsBaseArticleEmplacement)) {
            return false;
        }
        YvsBaseArticleEmplacement other = (YvsBaseArticleEmplacement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseArticleEmplacement[ id=" + id + " ]";
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

}
