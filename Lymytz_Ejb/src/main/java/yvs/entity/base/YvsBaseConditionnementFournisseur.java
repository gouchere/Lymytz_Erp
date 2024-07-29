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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_conditionnement_fournisseur")
@NamedQueries({
    @NamedQuery(name = "YvsBaseConditionnementFournisseur.findAll", query = "SELECT y FROM YvsBaseConditionnementFournisseur y"),
    @NamedQuery(name = "YvsBaseConditionnementFournisseur.findById", query = "SELECT y FROM YvsBaseConditionnementFournisseur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseConditionnementFournisseur.findByPua", query = "SELECT y FROM YvsBaseConditionnementFournisseur y WHERE y.pua = :pua"),
    @NamedQuery(name = "YvsBaseConditionnementFournisseur.findByDateUpdate", query = "SELECT y FROM YvsBaseConditionnementFournisseur y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseConditionnementFournisseur.findByDateSave", query = "SELECT y FROM YvsBaseConditionnementFournisseur y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseConditionnementFournisseur.findOne", query = "SELECT y FROM YvsBaseConditionnementFournisseur y WHERE y.article = :article AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsBaseConditionnementFournisseur.findByArticleUnite", query = "SELECT y FROM YvsBaseConditionnementFournisseur y WHERE y.article.article = :article AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsBaseConditionnementFournisseur.findByPrincipal", query = "SELECT y FROM YvsBaseConditionnementFournisseur y WHERE y.article = :article AND y.principal = :principal"),
    @NamedQuery(name = "YvsBaseConditionnementFournisseur.findUniteByArticle", query = "SELECT DISTINCT(y.conditionnement) FROM YvsBaseConditionnementFournisseur y WHERE y.article = :article")})
public class YvsBaseConditionnementFournisseur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_conditionnement_fournisseur_id_seq", name = "yvs_base_conditionnement_fournisseur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_conditionnement_fournisseur_id_seq_name", strategy = GenerationType.SEQUENCE) 
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pua")
    private Double pua;
    @Column(name = "principal")
    private Boolean principal;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticleFournisseur article;
    @Transient
    private boolean new_;

    public YvsBaseConditionnementFournisseur() {
    }

    public YvsBaseConditionnementFournisseur(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPua() {
        return pua != null ? pua : 0;
    }

    public void setPua(Double pua) {
        this.pua = pua;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsBaseArticleFournisseur getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticleFournisseur article) {
        this.article = article;
    }

    public Boolean getPrincipal() {
        return principal != null ? principal : false;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
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
        if (!(object instanceof YvsBaseConditionnementFournisseur)) {
            return false;
        }
        YvsBaseConditionnementFournisseur other = (YvsBaseConditionnementFournisseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseConditionnementFournisseur[ id=" + id + " ]";
    }

}
