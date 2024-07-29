///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.entity.produits;
//
//import java.io.Serializable;
//import java.util.Objects;
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import yvs.entity.produits.group.YvsTranches;
//
///**
// *
// * @author GOUCHERE YVES
// */
//@Entity
//@Table(name = "yvs_base_plan_tarifaire_article")
//@NamedQueries({
//    @NamedQuery(name = "YvsPlanTarif.findAll", query = "SELECT y FROM YvsBasePlanTarifaireArticle y"),
////    @NamedQuery(name = "YvsPlanTarif.findByCategorie", query = "SELECT y FROM YvsBasePlanTarifaireArticle y WHERE y.categorie = :categorie"),
////    @NamedQuery(name = "YvsPlanTarif.findOne", query = "SELECT y FROM YvsBasePlanTarifaireArticle y WHERE y.categorie = :categorie AND y.article = :article"),
////    @NamedQuery(name = "YvsPlanTarif.findByArticle", query = "SELECT y.prix,y.remise,y.tranche.id, y.tranche.modelTranche,y.categorie.id,y.categorie.designation "
////            + "FROM YvsBasePlanTarifaireArticle y "
////            + "left join fetch y.tranche left join fetch y.categorie "
////            + "left join fetch y.article WHERE y.article = :article"),
//    @NamedQuery(name = "YvsPlanTarif.findByPrix", query = "SELECT y FROM YvsBasePlanTarifaireArticle y WHERE y.prix = :prix"),
//    @NamedQuery(name = "YvsPlanTarif.findByRemise", query = "SELECT y FROM YvsBasePlanTarifaireArticle y WHERE y.remise = :remise")})
//public class YvsBasePlanTarifaireArticle implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//    @Id
//    protected Long id;
//    @Basic(optional = false)
//    @Column(name = "prix")
//    private double prix;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "remise")
//    private double remise;
//    @JoinColumn(name = "id_tranche", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsTranches tranche;
////    @JoinColumn(name = "categorie", referencedColumnName = "id")
////    @ManyToOne(fetch = FetchType.LAZY)
////    private Yvs categorie;
//    @JoinColumn(name = "article", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsBaseArticles article;
//
//    public YvsBasePlanTarifaireArticle() {
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public YvsBasePlanTarifaireArticle(Long id, double prix, double remise) {
//        this.id = id;
//        this.prix = prix;
//        this.remise = remise;
//    }
//
//    public double getPrix() {
//        return prix;
//    }
//
//    public void setPrix(double prix) {
//        this.prix = prix;
//    }
//
//    public double getRemise() {
//        return remise;
//    }
//
//    public void setRemise(double remise) {
//        this.remise = remise;
//    }
//
//    public YvsTranches getTranche() {
//        return tranche;
//    }
//
//    public void setTranche(YvsTranches tranche) {
//        this.tranche = tranche;
//    }
////
////    public YvsCatTarif getCategorie() {
////        return categorie;
////    }
////
////    public void setCategorie(YvsCatTarif categorie) {
////        this.categorie = categorie;
////    }
//
//    public YvsBaseArticles getArticle() {
//        return article;
//    }
//
//    public void setArticle(YvsBaseArticles article) {
//        this.article = article;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final YvsBasePlanTarifaireArticle other = (YvsBasePlanTarifaireArticle) obj;
//        if (!Objects.equals(this.id, other.id)) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "YvsBasePlanTarifaireArticle{" + "id=" + id + '}';
//    }
//
//}
