///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package yvs.entity.commercial;
//
//import yvs.entity.commercial.achat.YvsComContenuDocAchat;
//import java.io.Serializable;
//import java.util.List;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity; import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//import yvs.entity.commercial.client.YvsBasePlanTarifaire;
//import yvs.entity.commercial.rrr.YvsComPlanRemise;
//import yvs.entity.commercial.rrr.YvsComPlanRistourne;
//import yvs.entity.commercial.stock.YvsComContenuDocStock;
//import yvs.entity.commercial.vente.YvsComContenuDocVente;
//import yvs.entity.produits.YvsBaseArticles;
//
///**
// *
// * @author lymytz
// */
//@Entity
//@Table(name = "yvs_com_article")
//@NamedQueries({
//    @NamedQuery(name = "YvsComArticle.findAll", query = "SELECT y FROM YvsComArticle y WHERE y.article.famille.societe = :societe"),
//    @NamedQuery(name = "YvsComArticle.findByArticle", query = "SELECT y FROM YvsComArticle y WHERE y.article = :article"),
//    @NamedQuery(name = "YvsComArticle.findById", query = "SELECT y FROM YvsComArticle y WHERE y.id = :id")})
//public class YvsComArticle implements Serializable {
//
//    @OneToMany(mappedBy = "article")
//    private List<YvsComPlanRemise> yvsComPlanRemiseList;
//    @OneToMany(mappedBy = "article")
//    private List<YvsComPlanRistourne> yvsComPlanRistourneList;
//    @OneToMany(mappedBy = "article")
//    private List<YvsComGrilleCommission> yvsComCommissionList;
//    @OneToMany(mappedBy = "article")
//    private List<YvsComContenuDocVente> yvsComContenuDocVenteList;
//    @OneToMany(mappedBy = "article")
//    private List<YvsComContenuDocAchat> yvsComContenuDocAchatList;
//    @OneToMany(mappedBy = "article")
//    private List<YvsComContenuDocStock> yvsComContenuDocStockList;
//    @OneToMany(mappedBy = "article")
//    private List<YvsBasePlanTarifaire> yvsComCategorieTarifaireClientList;
//    @OneToMany(mappedBy = "article")
//    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Basic(optional = false)
//    @Column(name = "id")
//       private Long id;      @Column(name = "date_update")     @Temporal(TemporalType.TIMESTAMP)     private Date dateUpdate; @Column(name = "date_save") @Temporal(TemporalType.DATE) private Date dateSave; 
//    @JoinColumn(name = "article", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsBaseArticles article;
//    @Transient
//    private double stock;
//
//    public YvsComArticle() {
//    }
//
//    public YvsComArticle(Long id) {
//        this.id = id;
//    }
//
//    public YvsComArticle(Long id, YvsBaseArticles article) {
//        this.id = id;
//        this.article = article;
//    }
//
//    public YvsComArticle(Long id, YvsBaseArticles article, double stock) {
//        this.id = id;
//        this.article = article;
//        this.stock = stock;
//    }
//
//    public double getStock() {
//        return stock;
//    }
//
//    public void setStock(double stock) {
//        this.stock = stock;
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
//        int hash = 0;
//        hash += (id != null ? id.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the id fields are not set
//        if (!(object instanceof YvsComArticle)) {
//            return false;
//        }
//        YvsComArticle other = (YvsComArticle) object;
//        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "yvs.entity.commercia.YvsComArticle[ id=" + id + " ]";
//    }
//
//    public List<YvsBasePlanTarifaire> getYvsComCategorieTarifaireClientList() {
//        return yvsComCategorieTarifaireClientList;
//    }
//
//    public void setYvsComCategorieTarifaireClientList(List<YvsBasePlanTarifaire> yvsComCategorieTarifaireClientList) {
//        this.yvsComCategorieTarifaireClientList = yvsComCategorieTarifaireClientList;
//    }
//
//    public List<YvsComContenuDocAchat> getYvsComContenuDocAchatList() {
//        return yvsComContenuDocAchatList;
//    }
//
//    public void setYvsComContenuDocAchatList(List<YvsComContenuDocAchat> yvsComContenuDocAchatList) {
//        this.yvsComContenuDocAchatList = yvsComContenuDocAchatList;
//    }
//
//    public List<YvsComContenuDocStock> getYvsComContenuDocStockList() {
//        return yvsComContenuDocStockList;
//    }
//
//    public void setYvsComContenuDocStockList(List<YvsComContenuDocStock> yvsComContenuDocStockList) {
//        this.yvsComContenuDocStockList = yvsComContenuDocStockList;
//    }
//
//    public List<YvsComContenuDocVente> getYvsComContenuDocVenteList() {
//        return yvsComContenuDocVenteList;
//    }
//
//    public void setYvsComContenuDocVenteList(List<YvsComContenuDocVente> yvsComContenuDocVenteList) {
//        this.yvsComContenuDocVenteList = yvsComContenuDocVenteList;
//    }
//
//    public List<YvsComGrilleCommission> getYvsComCommissionList() {
//        return yvsComCommissionList;
//    }
//
//    public void setYvsComCommissionList(List<YvsComGrilleCommission> yvsComCommissionList) {
//        this.yvsComCommissionList = yvsComCommissionList;
//    }
//
//    public List<YvsComPlanRemise> getYvsComPlanRemiseList() {
//        return yvsComPlanRemiseList;
//    }
//
//    public void setYvsComPlanRemiseList(List<YvsComPlanRemise> yvsComPlanRemiseList) {
//        this.yvsComPlanRemiseList = yvsComPlanRemiseList;
//    }
//
//    public List<YvsComPlanRistourne> getYvsComPlanRistourneList() {
//        return yvsComPlanRistourneList;
//    }
//
//    public void setYvsComPlanRistourneList(List<YvsComPlanRistourne> yvsComPlanRistourneList) {
//        this.yvsComPlanRistourneList = yvsComPlanRistourneList;
//    }
//
//}
