///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package yvs.entity.production.pilotage;
//
//import java.io.Serializable;
//import java.util.Date;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import yvs.entity.production.base.YvsProdGammeArticle;
//import yvs.entity.production.base.YvsProdNomenclature;
//import yvs.entity.produits.YvsBaseArticles;
//import yvs.entity.users.YvsUsersAgence;
//
///**
// *
// * @author hp Elite 8300
// */
//@Entity
//@Table(name = "yvs_prod_ordre_articles")
//@NamedQueries({
//    @NamedQuery(name = "YvsProdOrdreArticles.findAll", query = "SELECT y FROM YvsProdOrdreArticles y"),
//    @NamedQuery(name = "YvsProdOrdreArticles.findById", query = "SELECT y FROM YvsProdOrdreArticles y WHERE y.id = :id"),
//    @NamedQuery(name = "YvsProdOrdreArticles.findByQuantite", query = "SELECT y FROM YvsProdOrdreArticles y WHERE y.quantite = :quantite"),
//    @NamedQuery(name = "YvsProdOrdreArticles.findByDateSave", query = "SELECT y FROM YvsProdOrdreArticles y WHERE y.dateSave = :dateSave"),
//    @NamedQuery(name = "YvsProdOrdreArticles.findByDateUpdate", query = "SELECT y FROM YvsProdOrdreArticles y WHERE y.dateUpdate = :dateUpdate")})
//public class YvsProdOrdreArticles implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @Column(name = "id")
//    private Long id;
//    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Column(name = "quantite")
//    private Double quantite;
//    @Column(name = "date_save")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateSave;
//    @Column(name = "date_update")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateUpdate;
//    @JoinColumn(name = "article", referencedColumnName = "id")
//    @ManyToOne
//    private YvsBaseArticles article;
//    @JoinColumn(name = "gamme", referencedColumnName = "id")
//    @ManyToOne
//    private YvsProdGammeArticle gamme;
//    @JoinColumn(name = "nomenclature", referencedColumnName = "id")
//    @ManyToOne
//    private YvsProdNomenclature nomenclature;
//    @JoinColumn(name = "ordre", referencedColumnName = "id")
//    @ManyToOne
//    private YvsProdOrdreFabrication ordre;
//    @JoinColumn(name = "author", referencedColumnName = "id")
//    @ManyToOne
//    private YvsUsersAgence author;
//
//    public YvsProdOrdreArticles() {
//    }
//
//    public YvsProdOrdreArticles(Long id) {
//        this.id = id;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Double getQuantite() {
//        return quantite;
//    }
//
//    public void setQuantite(Double quantite) {
//        this.quantite = quantite;
//    }
//
//    public Date getDateSave() {
//        return dateSave;
//    }
//
//    public void setDateSave(Date dateSave) {
//        this.dateSave = dateSave;
//    }
//
//    public Date getDateUpdate() {
//        return dateUpdate;
//    }
//
//    public void setDateUpdate(Date dateUpdate) {
//        this.dateUpdate = dateUpdate;
//    }
//
//    public YvsBaseArticles getArticle() {
//        return article;
//    }
//
//    public void setArticle(YvsBaseArticles article) {
//        this.article = article;
//    }
//
//    public YvsProdGammeArticle getGamme() {
//        return gamme;
//    }
//
//    public void setGamme(YvsProdGammeArticle gamme) {
//        this.gamme = gamme;
//    }
//
//    public YvsProdNomenclature getNomenclature() {
//        return nomenclature;
//    }
//
//    public void setNomenclature(YvsProdNomenclature nomenclature) {
//        this.nomenclature = nomenclature;
//    }
//
//    public YvsProdOrdreFabrication getOrdre() {
//        return ordre;
//    }
//
//    public void setOrdre(YvsProdOrdreFabrication ordre) {
//        this.ordre = ordre;
//    }
//
//    public YvsUsersAgence getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(YvsUsersAgence author) {
//        this.author = author;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof YvsProdOrdreArticles)) {
//            return false;
//        }
//        YvsProdOrdreArticles other = (YvsProdOrdreArticles) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.production.pilotage.YvsProdOrdreArticles[ id=" + id + " ]";
//    }
//    
//}
