/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_article_site")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleSite.findAll", query = "SELECT y FROM YvsBaseArticleSite y WHERE y.site.agence.societe = :societe"),
    @NamedQuery(name = "YvsBaseArticleSite.findById", query = "SELECT y FROM YvsBaseArticleSite y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleSite.findBySite", query = "SELECT y FROM YvsBaseArticleSite y WHERE y.site = :site"),
    @NamedQuery(name = "YvsBaseArticleSite.findByArticle", query = "SELECT y FROM YvsBaseArticleSite y WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleSite.findByArticleSite", query = "SELECT y FROM YvsBaseArticleSite y WHERE y.site = :site AND y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleSite.findIdByArticleSite", query = "SELECT y.id FROM YvsBaseArticleSite y WHERE y.site = :site AND y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleSite.findArticleBySite", query = "SELECT DISTINCT y.article FROM YvsBaseArticleSite y WHERE y.site = :site")})
public class YvsBaseArticleSite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_article_site_id_seq", name = "yvs_base_article_site_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_article_site_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "site", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdSiteProduction site;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBaseArticleSite() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseArticleSite(Integer id) {
        this();
        this.id = id;
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

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsProdSiteProduction getSite() {
        return site;
    }

    public void setSite(YvsProdSiteProduction site) {
        this.site = site;
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
        if (!(object instanceof YvsBaseArticleSite)) {
            return false;
        }
        YvsBaseArticleSite other = (YvsBaseArticleSite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsBaseArticleSite[ id=" + id + " ]";
    }

}
