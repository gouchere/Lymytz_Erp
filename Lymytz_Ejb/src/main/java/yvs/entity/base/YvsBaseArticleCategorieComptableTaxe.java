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
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_article_categorie_comptable_taxe", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findAll", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.articleCategorie.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findAllCount", query = "SELECT COUNT(y) FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.articleCategorie.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByArticleCategorie", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.articleCategorie = :articleCategorie"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByCategorie", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.articleCategorie.categorie = :categorie"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByCategorieArticle", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.articleCategorie.categorie = :categorie AND y.articleCategorie.article = :article"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByArticle", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.articleCategorie.article = :article"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByArticleTaxe", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.articleCategorie = :article AND y.taxe = :taxe"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findById", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByAppRemise", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.appRemise = :appRemise"),
    @NamedQuery(name = "YvsBaseArticleCategorieComptableTaxe.findByActif", query = "SELECT y FROM YvsBaseArticleCategorieComptableTaxe y WHERE y.actif = :actif")})
public class YvsBaseArticleCategorieComptableTaxe extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_taxe_article_id_seq", name = "yvs_base_taxe_article_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_taxe_article_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "app_remise")
    private Boolean appRemise;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "taxe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTaxes taxe;
    @JoinColumn(name = "article_categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticleCategorieComptable articleCategorie;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;

    public YvsBaseArticleCategorieComptableTaxe() {
    }

    public YvsBaseArticleCategorieComptableTaxe(Long id) {
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

    @XmlTransient  @JsonIgnore
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAppRemise() {
        return appRemise != null ? appRemise : false;
    }

    public void setAppRemise(Boolean appRemise) {
        this.appRemise = appRemise;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBaseTaxes getTaxe() {
        return taxe;
    }

    public void setTaxe(YvsBaseTaxes taxe) {
        this.taxe = taxe;
    }

    @XmlTransient  @JsonIgnore
    public YvsBaseArticleCategorieComptable getArticleCategorie() {
        return articleCategorie;
    }

    public void setArticleCategorie(YvsBaseArticleCategorieComptable articleCategorie) {
        this.articleCategorie = articleCategorie;
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
        if (!(object instanceof YvsBaseArticleCategorieComptableTaxe)) {
            return false;
        }
        YvsBaseArticleCategorieComptableTaxe other = (YvsBaseArticleCategorieComptableTaxe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseArticleCategorieComptableTaxe[ id=" + id + " ]";
    }

}
