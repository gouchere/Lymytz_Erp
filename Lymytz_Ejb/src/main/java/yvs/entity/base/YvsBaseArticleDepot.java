/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_article_depot")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticleDepot.findAll", query = "SELECT y FROM YvsBaseArticleDepot y"),
    @NamedQuery(name = "YvsBaseArticleDepot.findAllByDepot", query = "SELECT y FROM YvsBaseArticleDepot y JOIN FETCH y.article LEFT JOIN FETCH y.depotPr WHERE y.depot = :depot ORDER BY y.article.refArt"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByDepot", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.depot = :depot AND y.actif=true AND y.article.actif=true ORDER BY y.article.refArt"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByDepotSuivi", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.depot = :depot AND y.actif=true AND y.article.actif=true AND y.suiviStock=true ORDER BY  y.article.refArt"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByDepotSuiviStock", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.depot = :depot AND y.actif=true AND y.article.actif=true AND y.suiviStock= :suiviStock ORDER BY  y.article.refArt"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByArticle", query = "SELECT y FROM YvsBaseArticleDepot y JOIN FETCH y.depot WHERE y.article = :article ORDER BY y.depot.code"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByArticleActif", query = "SELECT y FROM YvsBaseArticleDepot y JOIN FETCH y.depot WHERE y.article = :article AND y.depot.actif = TRUE ORDER BY y.depot.code"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByArticleNoDepot", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.article = :article AND y.depot != :depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByArticleEmplacement", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.article = :article AND y.depot = :emplacement"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByArticleDepot", query = "SELECT y FROM YvsBaseArticleDepot y JOIN FETCH y.article JOIN FETCH y.depot WHERE y.article = :article AND y.depot = :depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.countByArticleDepot", query = "SELECT COUNT(y) FROM YvsBaseArticleDepot y WHERE y.article = :article AND y.depot = :depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findOneByArticleDepot", query = "SELECT y FROM YvsBaseArticleDepot y JOIN FETCH y.article JOIN FETCH y.depot WHERE y.article = :article AND y.depot = :depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.counByArticleDepot", query = "SELECT COUNT(y.id) FROM YvsBaseArticleDepot y WHERE y.article = :article AND y.depot = :depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findLastDateByArticleDepot", query = "SELECT y.lastDateCalculPr FROM YvsBaseArticleDepot y WHERE y.article = :article AND y.depot = :depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByStockMax", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.stockMax = :stockMax"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByStockMin", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.stockMin = :stockMin"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByModeAppro", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.modeAppro = :modeAppro"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByModeReappro", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.modeReappro = :modeReappro"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByNombreJour", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.intervalApprov = :nombreJour"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByQuantiteStock", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.quantiteStock = :quantiteStock"),
    @NamedQuery(name = "YvsBaseArticleDepot.findBySupp", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByActif", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByAuthor", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.author = :author"),
    @NamedQuery(name = "YvsBaseArticleDepot.findById", query = "SELECT y FROM YvsBaseArticleDepot y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticleDepot.findDepotByArticle", query = "SELECT y.depot FROM YvsBaseArticleDepot y WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleDepot.findIfSuivi", query = "SELECT y.suiviStock FROM YvsBaseArticleDepot y WHERE y.article = :article AND y.depot=:depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findIfSellWithOutStock", query = "SELECT y.sellWithoutStock FROM YvsBaseArticleDepot y WHERE y.article = :article AND y.depot=:depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findDefaultCond", query = "SELECT y.defaultCond FROM YvsBaseArticleDepot y WHERE y.article = :article AND y.depot=:depot AND y.defaultCond.actif = TRUE"),

    @NamedQuery(name = "YvsBaseArticleDepot.findIdDepotByArticle", query = "SELECT y.depot.id FROM YvsBaseArticleDepot y WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticleDepot.findIdByArticlePr", query = "SELECT y.depot.id FROM YvsBaseArticleDepot y WHERE y.article = :article AND y.defaultPr = true AND y.depot.agence=:agence"),
    @NamedQuery(name = "YvsBaseArticleDepot.findByArticlePr", query = "SELECT y.depot FROM YvsBaseArticleDepot y WHERE y.article = :article AND y.defaultPr = true AND y.depot.agence=:agence "),
    @NamedQuery(name = "YvsBaseArticleDepot.findByPr", query = "SELECT y.depot FROM YvsBaseArticleDepot y WHERE y.defaultPr = true AND y.depot.agence=:agence "),

    @NamedQuery(name = "YvsBaseArticleDepot.findArticleByRefartDepotActif", query = "SELECT y.article FROM YvsBaseArticleDepot y WHERE (UPPER(y.article.refArt) LIKE UPPER(:code) OR UPPER(y.article.designation) LIKE UPPER(:code)) AND y.depot = :depot AND y.article.actif = true AND y.actif = true"),
    @NamedQuery(name = "YvsBaseArticleDepot.findArticleByRefartDepot", query = "SELECT y.article FROM YvsBaseArticleDepot y WHERE (UPPER(y.article.refArt) LIKE UPPER(:code) OR UPPER(y.article.designation) LIKE UPPER(:code)) AND y.depot = :depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findArticleById", query = "SELECT y.article FROM YvsBaseArticleDepot y WHERE y.depot = :depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findArticleByDepot", query = "SELECT y.article FROM YvsBaseArticleDepot y WHERE y.depot = :depot"),
    @NamedQuery(name = "YvsBaseArticleDepot.findArticleByDepots", query = "SELECT y.article FROM YvsBaseArticleDepot y WHERE y.depot.id IN :ids AND y.article.actif=TRUE AND y.article.categorie NOT IN :categories AND (UPPER(y.article.refArt) LIKE UPPER(:code) OR UPPER(y.article.designation) LIKE UPPER (:code))  ORDER BY y.article.refArt"),
    @NamedQuery(name = "YvsBaseArticleDepot.findArticleByDepotActif", query = "SELECT y.article FROM YvsBaseArticleDepot y WHERE y.depot = :depot AND y.article.actif = true AND y.actif=true"),
    @NamedQuery(name = "YvsBaseArticleDepot.findDepotActifByArt", query = "SELECT DISTINCT(y.depot) FROM YvsBaseArticleDepot y WHERE y.depot.actif =true AND y.article=:article AND y.actif=true"),
    @NamedQuery(name = "YvsBaseArticleDepot.findAllArticleDEpot", query = "SELECT y FROM YvsBaseArticleDepot y LEFT JOIN FETCH y.articles WHERE y.actif =true AND y.depot=:depot AND y.article.actif=true"),
    @NamedQuery(name = "YvsBaseArticleDepot.findDepotByArticleDepot", query = "SELECT DISTINCT(y.depot) FROM YvsBaseArticleDepot y WHERE y.depot = :depot AND y.article = :article")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseArticleDepot extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_article_depot_id_seq", name = "yvs_base_article_depot_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_article_depot_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Basic(optional = false)
    @Column(name = "stock_max")
    private Double stockMax;
    @Basic(optional = false)
    @Column(name = "stock_min")
    private Double stockMin;
    @Size(max = 2147483647)
    @Column(name = "mode_appro")
    private String modeAppro = "ACHT_PROD_EN";
    @Size(max = 2147483647)
    @Column(name = "mode_reappro")
    private String modeReappro = "RUPTURE";
    @Column(name = "categorie")
    private String categorie;
    @Column(name = "interval_approv")
    private Integer intervalApprov;
    @Column(name = "unite_interval")
    private String uniteInterval;
    @Column(name = "quantite_stock")
    private Double quantiteStock;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "stock_alert")
    private Double stockAlert;
    @Column(name = "stock_initial")
    private Double stockInitial;
    @Column(name = "date_appro")
    @Temporal(TemporalType.DATE)
    private Date dateAppro;
    @Column(name = "marg_stock_moyen")
    private Double margStockMoyen;
    @Column(name = "stock_net")
    private Double stockNet;
    @Column(name = "lot_livraison")
    private Integer lotLivraison;
    @Size(max = 255)
    @Column(name = "type_document_generer")
    private String typeDocumentGenerer = "FA";
    @Column(name = "generer_document")
    private Boolean genererDocument = false;
    @Column(name = "requiere_lot")
    private Boolean requiereLot = false;
    @Column(name = "suivi_stock")
    private Boolean suiviStock = false;
    @Column(name = "sell_without_stock")
    private Boolean sellWithoutStock = true;
    @Column(name = "default_pr")
    private Boolean defaultPr = false;
    @Column(name = "last_date_calcul_pr")
    @Temporal(TemporalType.DATE)
    private Date lastDateCalculPr;

    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @JoinColumn(name = "depot_pr", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depotPr;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "default_cond", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement defaultCond;

    @OneToMany(mappedBy = "article",fetch = FetchType.LAZY)
    private List<YvsBaseArticleEmplacement> articles;
    @OneToMany(mappedBy = "article",fetch = FetchType.LAZY)
    private List<YvsBaseConditionnementDepot> conditionnements;
    @Transient
    private boolean new_;
    @Transient
    private double prixRevient;
    @Transient
    private double prixEntree;
    @Transient
    private boolean selectActif;
    @Transient
    private YvsBaseConditionnement conditionnement = new YvsBaseConditionnement();
    @Transient
    private YvsComLotReception lot;
    @Transient
    private List<YvsComLotReception> lots;
    @Transient
    private double stock;
    @Transient
    private double resteALivrer;
    @Transient
    private double quantiteReserve;
    @Transient
    private String message_service;

    public YvsBaseArticleDepot() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        conditionnements = new ArrayList<>();
        articles = new ArrayList<>();
        lots = new ArrayList<>();
    }

    public YvsBaseArticleDepot(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseArticleDepot(Long id, YvsBaseArticles article) {
        this(id);
        this.article = article;
    }

    public YvsBaseArticleDepot(Long id, YvsBaseArticles article, YvsBaseDepots depot) {
        this(id, article);
        this.depot = depot;
    }

    public YvsBaseArticleDepot(Long id, Long idEmpl) {
        this(id);
    }

    public YvsBaseArticleDepot(Long id, Double stockMax, Double stockMin) {
        this(id);
        this.stockMax = stockMax;
        this.stockMin = stockMin;
    }

    public YvsBaseArticleDepot(YvsBaseArticleDepot y) {
        this.id = y.id;
        this.dateUpdate = y.dateUpdate;
        this.dateSave = y.dateSave;
        this.stockMax = y.stockMax;
        this.stockMin = y.stockMin;
        this.modeAppro = y.modeAppro;
        this.modeReappro = y.modeReappro;
        this.intervalApprov = y.intervalApprov;
        this.uniteInterval = y.uniteInterval;
        this.quantiteStock = y.quantiteStock;
        this.supp = y.supp;
        this.actif = y.actif;
        this.stockAlert = y.stockAlert;
        this.stockInitial = y.stockInitial;
        this.dateAppro = y.dateAppro;
        this.margStockMoyen = y.margStockMoyen;
        this.stockNet = y.stockNet;
        this.lotLivraison = y.lotLivraison;
        this.article = y.article;
        this.depot = y.depot;
        this.depotPr = y.depotPr;
        this.requiereLot = y.requiereLot;
        this.author = y.author;
        this.articles = y.articles;
        this.conditionnements = y.conditionnements;
        this.new_ = y.new_;
        this.selectActif = y.selectActif;
        this.resteALivrer = y.resteALivrer;
        this.quantiteReserve = y.quantiteReserve;
        this.prixRevient = y.prixRevient;
        this.prixEntree = y.prixEntree;
        this.sellWithoutStock = y.sellWithoutStock;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage_service() {
        return message_service;
    }

    public void setMessage_service(String message_service) {
        this.message_service = message_service;
    }

    public String getTypeDocumentGenerer() {
        return typeDocumentGenerer != null ? typeDocumentGenerer.trim().length() > 0 ? typeDocumentGenerer : "FA" : "FA";
    }

    public void setTypeDocumentGenerer(String typeDocumentGenerer) {
        this.typeDocumentGenerer = typeDocumentGenerer;
    }

    public Boolean getGenererDocument() {
        return genererDocument != null ? genererDocument : false;
    }

    public void setGenererDocument(Boolean genererDocument) {
        this.genererDocument = genererDocument;
    }

    public YvsBaseDepots getDepotPr() {
        return depotPr;
    }

    public void setDepotPr(YvsBaseDepots depotPr) {
        this.depotPr = depotPr;
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

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public String getUniteInterval() {
        return uniteInterval != null ? uniteInterval : "";
    }

    public void setUniteInterval(String uniteInterval) {
        this.uniteInterval = uniteInterval;
    }

    public Double getStockMax() {
        return stockMax != null ? stockMax : 0;
    }

    public void setStockMax(Double stockMax) {
        this.stockMax = stockMax;
    }

    public Double getStockMin() {
        return stockMin != null ? stockMin : 0;
    }

    public void setStockMin(Double stockMin) {
        this.stockMin = stockMin;
    }

    public String getModeAppro() {
        modeAppro = modeAppro != null ? modeAppro : "";
        return modeAppro.trim().length() > 0 ? modeAppro : "ACHT_PROD_EN";
    }

    public void setModeAppro(String modeAppro) {
        this.modeAppro = modeAppro;
    }

    public String getModeReappro() {
        modeReappro = modeReappro != null ? modeReappro : "";
        return modeReappro.trim().length() > 0 ? modeReappro : "RUPTURE";
    }

    public void setModeReappro(String modeReappro) {
        this.modeReappro = modeReappro;
    }

    public Integer getIntervalApprov() {
        return intervalApprov != null ? intervalApprov : 0;
    }

    public void setIntervalApprov(Integer intervalApprov) {
        this.intervalApprov = intervalApprov;
    }

    public Double getQuantiteStock() {
        return quantiteStock != null ? quantiteStock : 0;
    }

    public void setQuantiteStock(Double quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsComLotReception getLot() {
        return lot;
    }

    public void setLot(YvsComLotReception lot) {
        this.lot = lot;
    }

    public Date getDateAppro() {
        return dateAppro;
    }

    public void setDateAppro(Date dateAppro) {
        this.dateAppro = dateAppro;
    }

    public Boolean getSellWithoutStock() {
        return sellWithoutStock != null ? sellWithoutStock : true;
    }

    public void setSellWithoutStock(Boolean sellWithoutStock) {
        this.sellWithoutStock = sellWithoutStock;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    public Double getStockInitial() {
        return stockInitial != null ? stockInitial : 0;
    }

    public void setStockInitial(Double stockInitial) {
        this.stockInitial = stockInitial;
    }

    public Double getStockAlert() {
        return stockAlert != null ? stockAlert : 0;
    }

    public void setStockAlert(Double stockAlert) {
        this.stockAlert = stockAlert;
    }

    public List<YvsBaseArticleEmplacement> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsBaseArticleEmplacement> articles) {
        this.articles = articles;
    }

    public double getResteALivrer() {
        return resteALivrer;
    }

    public void setResteALivrer(double resteALivrer) {
        this.resteALivrer = resteALivrer;
    }

    public double getQuantiteReserve() {
        return quantiteReserve;
    }

    public void setQuantiteReserve(double quantiteReserve) {
        this.quantiteReserve = quantiteReserve;
    }

    public Boolean getRequiereLot() {
        return requiereLot != null ? requiereLot : false;
    }

    public void setRequiereLot(Boolean requiereLot) {
        this.requiereLot = requiereLot;
    }

    public Double getMargStockMoyen() {
        return margStockMoyen != null ? margStockMoyen : 0.0;
    }

    public void setMargStockMoyen(Double margStockMoyen) {
        this.margStockMoyen = margStockMoyen;
    }

    public Double getStockNet() {
        return stockNet != null ? stockNet : 0.0;
    }

    public void setStockNet(Double stockNet) {
        this.stockNet = stockNet;
    }

    public Integer getLotLivraison() {
        return lotLivraison != null ? lotLivraison : 0;
    }

    public void setLotLivraison(Integer lotLivraison) {
        this.lotLivraison = lotLivraison;
    }

    public List<YvsBaseConditionnementDepot> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnementDepot> conditionnements) {
        this.conditionnements = conditionnements;
    }

    public Boolean getSuiviStock() {
        return suiviStock != null ? suiviStock : true;
    }

    public void setSuiviStock(Boolean suiviStock) {
        this.suiviStock = suiviStock;
    }

    public YvsBaseConditionnement getDefaultCond() {
        return defaultCond;
    }

    public void setDefaultCond(YvsBaseConditionnement defaultCond) {
        this.defaultCond = defaultCond;
    }

    public double getPrixRevient() {
        return prixRevient;
    }

    public void setPrixRevient(double prixRevient) {
        this.prixRevient = prixRevient;
    }

    public double getPrixEntree() {
        return prixEntree;
    }

    public void setPrixEntree(double prixEntree) {
        this.prixEntree = prixEntree;
    }

    public Boolean getDefaultPr() {
        return defaultPr != null ? defaultPr : false;
    }

    public void setDefaultPr(Boolean defaultPr) {
        this.defaultPr = defaultPr;
    }

    public Date getLastDateCalculPr() {
        return lastDateCalculPr;
    }

    public void setLastDateCalculPr(Date lastDateCalculPr) {
        this.lastDateCalculPr = lastDateCalculPr;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<YvsComLotReception> getLots() {
        return lots;
    }

    public void setLots(List<YvsComLotReception> lots) {
        this.lots = lots;
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
        if (!(object instanceof YvsBaseArticleDepot)) {
            return false;
        }
        YvsBaseArticleDepot other = (YvsBaseArticleDepot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseArticleDepot[ id=" + id + " ]";
    }

}
