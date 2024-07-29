/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_article_point")
@NamedQueries({
    @NamedQuery(name = "YvsBaseArticlePoint.findAll", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.point.agence.societe = :societe"),
    @NamedQuery(name = "YvsBaseArticlePoint.findAllByDepot", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.point = :point ORDER BY y.article.refArt"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByDepot", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.point = :point AND y.actif=true AND y.article.actif=true ORDER BY y.article.refArt"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByArticle", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByArticle_", query = "SELECT y FROM YvsBaseArticlePoint y JOIN FETCH y.conditionnements WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByArticleEmplacement", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.article = :article AND y.point = :emplacement"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByArticlePoint", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.article = :article AND y.point = :point"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByPr", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.puv = :puv"),
    @NamedQuery(name = "YvsBaseArticlePoint.findBySupp", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByActif", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByAuthor", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.author = :author"),
    @NamedQuery(name = "YvsBaseArticlePoint.findById", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseArticlePoint.findByArt", query = "SELECT y.point FROM YvsBaseArticlePoint y WHERE y.article = :article"),

    @NamedQuery(name = "YvsBaseArticlePoint.findArticleByRefartDepotActif", query = "SELECT y.article FROM YvsBaseArticlePoint y WHERE (UPPER(y.article.refArt) LIKE UPPER(:code) OR UPPER(y.article.designation) LIKE UPPER(:code)) AND y.point = :point AND y.article.actif = true"),
    @NamedQuery(name = "YvsBaseArticlePoint.findArticleById", query = "SELECT y.article FROM YvsBaseArticlePoint y WHERE y.point = :point"),
    @NamedQuery(name = "YvsBaseArticlePoint.findArticlePoint", query = "SELECT y FROM YvsBaseArticlePoint y WHERE y.point = :point AND y.article = :article"),
    @NamedQuery(name = "YvsBaseArticlePoint.findIdArticleByPoint", query = "SELECT y.article.id FROM YvsBaseArticlePoint y WHERE y.point = :point"),
    @NamedQuery(name = "YvsBaseArticlePoint.findArticleByDepotActif", query = "SELECT y.article FROM YvsBaseArticlePoint y WHERE y.point = :point AND y.article.actif = true")})
public class YvsBaseArticlePoint extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_article_point_id_seq", name = "yvs_base_article_point_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_article_point_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "puv_min")
    private Double puvMin;
    @Column(name = "nature_prix_min")
    private String naturePrixMin;
    @Column(name = "puv")
    private Double puv;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "nature_remise")
    private String natureRemise;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "change_prix")
    private Boolean changePrix = false;
    @Column(name = "prioritaire")
    private Boolean prioritaire = false;
    
    
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "point", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePointVente point;
    @JoinColumn(name = "conditionement_vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionementVente;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsBaseConditionnementPoint> conditionnements;
    
    
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsBaseArticlePoint() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.conditionnements = new ArrayList<>();
        this.actif = true;
    }

    public YvsBaseArticlePoint(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseArticlePoint(Long id, YvsBaseArticles article) {
        this(id);
        this.article = article;
    }

    public YvsBaseArticlePoint(YvsBaseArticles article, YvsBasePointVente point) {
        this();
        this.article = article;
        this.point = point;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
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

    public Double getRemise() {
        return remise != null ? remise : 0;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public String getNatureRemise() {
        return natureRemise != null ? (natureRemise.trim().length() > 0 ? natureRemise : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
    }

    public String getNaturePrixMin() {
        return naturePrixMin != null ? (naturePrixMin.trim().length() > 0 ? naturePrixMin : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public Double getPuvMin() {
        return puvMin != null ? puvMin : 0;
    }

    public void setPuvMin(Double puvMin) {
        this.puvMin = puvMin;
    }

    public Boolean getChangePrix() {
        return changePrix != null ? changePrix : false;
    }

    public void setChangePrix(Boolean changePrix) {
        this.changePrix = changePrix;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsBasePointVente getPoint() {
        return point;
    }

    public void setPoint(YvsBasePointVente point) {
        this.point = point;
    }

    public Double getPuv() {
        return puv != null ? puv : 0;
    }

    public void setPuv(Double puv) {
        this.puv = puv;
    }

    public Boolean getPrioritaire() {
        return prioritaire != null ? prioritaire : false;
    }

    public void setPrioritaire(Boolean prioritaire) {
        this.prioritaire = prioritaire;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseConditionnementPoint> getConditionnements() {
        return conditionnements;
    }

    public void setConditionnements(List<YvsBaseConditionnementPoint> conditionnements) {
        this.conditionnements = conditionnements;
    }

    @XmlTransient
    @JsonIgnore
    public YvsBaseConditionnement getConditionementVente() {
        return conditionementVente;
    }

    public void setConditionementVente(YvsBaseConditionnement conditionementVente) {
        this.conditionementVente = conditionementVente;
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
        if (!(object instanceof YvsBaseArticlePoint)) {
            return false;
        }
        YvsBaseArticlePoint other = (YvsBaseArticlePoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseArticlePoint[ id=" + id + " ]";
    }

}
