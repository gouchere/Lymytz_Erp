/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_mouvement_stock")
@NamedQueries({
    @NamedQuery(name = "YvsBaseMouvementStock.findAll", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByAgence", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot.agence = :agence"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepot", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot ORDER BY y.dateDoc DESC"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByArticle", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseMouvementStock.findById", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByQuantite", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDateDoc", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.dateDoc = :dateDoc"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByMouvement", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.mouvement = :mouvement"),
    @NamedQuery(name = "YvsBaseMouvementStock.findBySupp", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByActif", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByIdExterne", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.idExterne = :idExterne"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByTableExterne", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.tableExterne = :tableExterne"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByTableExterne", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.tableExterne = :tableExterne"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDescription", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.description = :description"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDate", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.article.famille.societe = :societe AND y.dateDoc = :dateDoc"),

    @NamedQuery(name = "YvsBaseMouvementStock.findPrixByArticle", query = "SELECT y.coutStock FROM YvsBaseMouvementStock y WHERE y.article = :article AND y.conditionnement = :unite AND y.dateMouvement<=:date ORDER BY y.dateMouvement DESC, y.id DESC"),
    @NamedQuery(name = "YvsBaseMouvementStock.findLastPrixEntree", query = "SELECT y.coutEntree FROM YvsBaseMouvementStock y WHERE y.article = :article AND y.conditionnement = :unite AND y.depot = :depot AND y.mouvement = 'E' ORDER BY y.dateMouvement DESC, y.id DESC"),
    @NamedQuery(name = "YvsBaseMouvementStock.findLastPrixEntreeByArticle", query = "SELECT y.coutEntree FROM YvsBaseMouvementStock y WHERE y.article = :article AND y.conditionnement = :unite AND y.mouvement = 'E' ORDER BY y.dateMouvement DESC, y.id DESC"),
    
    @NamedQuery(name = "YvsBaseMouvementStock.findCountByExterne", query = "SELECT COUNT(y) FROM YvsBaseMouvementStock y WHERE y.idExterne = :externe AND y.tableExterne = :table"),

    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotMouvement", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.mouvement = :mouvement"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotArticle", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.article = :article"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotTrancheArticle", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.tranche = :tranche AND y.article = :article"),
    @NamedQuery(name = "YvsBaseMouvementStock.countMvtOtherTypeJ", query = "SELECT COUNT(y) FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.dateDoc = :dateDoc AND y.tranche.typeJournee!=:typeJ "),
    
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotDate", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.dateDoc = :dateDoc ORDER BY y.id DESC"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotDateArticle", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.dateDoc = :dateDoc AND y.article = :article"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotDates", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.dateDoc BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotDatesArticle", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.article = :article"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByArticleDates", query = "SELECT y FROM YvsBaseMouvementStock y JOIN FETCH y.article WHERE y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.article = :article"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByArticleDate", query = "SELECT y FROM YvsBaseMouvementStock y JOIN FETCH y.article WHERE y.dateDoc=:date AND y.article = :article AND y.tableExterne=:table"),
    @NamedQuery(name = "YvsBaseMouvementStock.findIdExtByArticleDate", query = "SELECT y.idExterne FROM YvsBaseMouvementStock y JOIN FETCH y.article WHERE y.dateDoc=:date AND y.article = :article AND y.tableExterne=:table"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDates", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot.agence = :agence AND y.dateDoc BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotMouvementArticle", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.mouvement = :mouvement AND y.article = :article"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotMouvementDate", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.mouvement = :mouvement AND y.dateDoc = :dateDoc"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotMouvementDateArticle", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.mouvement = :mouvement AND y.dateDoc = :dateDoc AND y.article = :article"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotMouvementDates", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.mouvement = :mouvement AND y.dateDoc BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsBaseMouvementStock.findByDepotMouvementDatesArticle", query = "SELECT y FROM YvsBaseMouvementStock y WHERE y.depot = :depot AND y.mouvement = :mouvement AND y.dateDoc BETWEEN :dateDebut AND :dateFin AND y.article = :article")})
public class YvsBaseMouvementStock extends YvsEntity implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mouvement_stock_id_seq", name = "yvs_mouvement_stock_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mouvement_stock_id_seq_name", strategy = GenerationType.SEQUENCE) 
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
    @Column(name = "date_doc")
    @Temporal(TemporalType.DATE)
    private Date dateDoc;
    @Size(max = 2147483647)
    @Column(name = "mouvement")
    private String mouvement;
    @Size(max = 2147483647)
    @Column(name = "type_doc")
    private String typeDoc;
    @Size(max = 2147483647)
    @Column(name = "num_doc")
    private String numDoc;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "id_externe")
    private Long idExterne;
    @Size(max = 2147483647)
    @Column(name = "table_externe")
    private String tableExterne;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "cout_entree")
    private Double coutEntree;
    @Column(name = "date_mouvement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateMouvement;
    @Column(name = "cout_stock")
    private Double coutStock;
    @Column(name = "calcul_pr")
    private Boolean calculPr;
    
    @OneToOne(mappedBy = "mouvement", fetch = FetchType.LAZY)
    private YvsBaseMouvementStockLot lot;
    
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseMouvementStock parent;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "tranche", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTrancheHoraire tranche;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<YvsBaseMouvementStock> mouvements;
    
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private double reste;
    @Transient
    private double cout;
    @Transient
    private double stock;

    public YvsBaseMouvementStock() {
        mouvements = new ArrayList<>();
    }

    public YvsBaseMouvementStock(Long id) {
        this.id = id;
        mouvements = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public Boolean getCalculPr() {
        return calculPr != null ? calculPr : true;
    }

    public void setCalculPr(Boolean calculPr) {
        this.calculPr = calculPr;
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

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public YvsGrhTrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(YvsGrhTrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
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

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDoc() {
        return dateDoc;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public String getMouvement() {
        return mouvement != null ? mouvement.trim().length() > 0 ? mouvement : "E" : "E";
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    public Boolean getSupp() {
        return supp;
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

    public Long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(Long idExterne) {
        this.idExterne = idExterne;
    }

    public String getTableExterne() {
        return tableExterne;
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public Double getCoutEntree() {
        return coutEntree != null ? coutEntree : 0;
    }

    public void setCoutEntree(Double coutEntree) {
        this.coutEntree = coutEntree;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateMouvement() {
        return dateMouvement;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateMouvement(Date dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public Double getCoutStock() {
        return coutStock != null ? coutStock : 0;
    }

    public void setCoutStock(Double coutStock) {
        this.coutStock = coutStock;
    }

    public List<YvsBaseMouvementStock> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<YvsBaseMouvementStock> mouvements) {
        this.mouvements = mouvements;
    }

    public YvsBaseMouvementStock getParent() {
        return parent;
    }

    public void setParent(YvsBaseMouvementStock parent) {
        this.parent = parent;
    }

    public YvsBaseMouvementStockLot getLot() {
        return lot;
    }

    public void setLot(YvsBaseMouvementStockLot lot) {
        this.lot = lot;
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
        if (!(object instanceof YvsBaseMouvementStock)) {
            return false;
        }
        YvsBaseMouvementStock other = (YvsBaseMouvementStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseMouvementStock[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        YvsBaseMouvementStock m = (YvsBaseMouvementStock) o;
        if (dateDoc.equals(m.dateDoc)) {
            if (article.equals(m.article)) {
                if (mouvement.equals(m.mouvement)) {
                    if (tableExterne.equals(m.tableExterne)) {
                        if (idExterne.equals(m.idExterne)) {
                            return id.compareTo(m.id);
                        }
                        return idExterne.compareTo(m.idExterne);
                    }
                    return tableExterne.compareTo(m.tableExterne);
                }
                return mouvement.compareTo(m.mouvement);
            }
//            return article.compareTo(m.article);
        }
        return dateDoc.compareTo(m.dateDoc);
    }

}
