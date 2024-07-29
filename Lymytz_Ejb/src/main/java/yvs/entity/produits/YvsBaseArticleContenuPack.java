/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.produits;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_article_contenu_pack")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleContenuPack.findAll", query = "SELECT y FROM YvsBaseArticleContenuPack y ORDER BY y.article.article.designation"),
    @NamedQuery(name = "YvsBaseArticleContenuPack.findById", query = "SELECT y FROM YvsBaseArticleContenuPack y WHERE y.id = :id ORDER BY y.article.article.designation"),
    @NamedQuery(name = "YvsBaseArticleContenuPack.findByArticle", query = "SELECT y FROM YvsBaseArticleContenuPack y WHERE y.article = :article ORDER BY y.article.article.designation"),
    @NamedQuery(name = "YvsBaseArticleContenuPack.findByPack", query = "SELECT y FROM YvsBaseArticleContenuPack y WHERE y.pack = :pack ORDER BY y.article.article.designation"),
    @NamedQuery(name = "YvsBaseArticleContenuPack.findByDateSave", query = "SELECT y FROM YvsBaseArticleContenuPack y WHERE y.dateSave = :dateSave ORDER BY y.article.article.designation"),
    @NamedQuery(name = "YvsBaseArticleContenuPack.findByDateUpdate", query = "SELECT y FROM YvsBaseArticleContenuPack y WHERE y.dateUpdate = :dateUpdate ORDER BY y.article.article.designation")})
public class YvsBaseArticleContenuPack implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_article_contenu_pack_id_seq", name = "yvs_base_article_contenu_pack_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_article_contenu_pack_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "montant")
    private Double montant;
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "quantite_max")
    private Double quantiteMax;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "pack", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseArticlePack pack;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseConditionnement article;
    @Transient
    private boolean new_;

    public YvsBaseArticleContenuPack() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseArticleContenuPack(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 1;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getQuantiteMax() {
        return quantiteMax != null ? quantiteMax : 1;
    }

    public void setQuantiteMax(Double quantiteMax) {
        this.quantiteMax = quantiteMax;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseArticlePack getPack() {
        return pack;
    }

    public void setPack(YvsBaseArticlePack pack) {
        this.pack = pack;
    }

    public YvsBaseConditionnement getArticle() {
        return article;
    }

    public void setArticle(YvsBaseConditionnement article) {
        this.article = article;
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
        if (!(object instanceof YvsBaseArticleContenuPack)) {
            return false;
        }
        YvsBaseArticleContenuPack other = (YvsBaseArticleContenuPack) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.produits.YvsBaseArticleContenuPack[ id=" + id + " ]";
    }

}
