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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.commercial.rrr.YvsComRabais;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_conditionnement_point")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsBaseConditionnementPoint.findAll", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.article.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findById", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByPuv", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.puv = :puv"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByPrixMin", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.prixMin = :prixMin"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByNaturePrixMin", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.naturePrixMin = :naturePrixMin"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByRemise", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.remise = :remise"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByNatureRemise", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.natureRemise = :natureRemise"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByArticlePoint", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.article = :article"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findByArticlePointActive", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.article.article = :article AND y.conditionnement.actif = TRUE"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findVenteByArticle", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.article.article = :article AND y.conditionnement.byVente = TRUE"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findOne", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.article = :article AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findArticlePointUnite", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.article.article = :article AND y.article.point = :point AND y.conditionnement = :unite"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findConditionnementAgence", query = "SELECT y FROM YvsBaseConditionnementPoint y WHERE y.article.point.agence = :agence AND y.conditionnement = :unite"),

    @NamedQuery(name = "YvsBaseConditionnementPoint.countVenteByArticle", query = "SELECT COUNT(y) FROM YvsBaseConditionnementPoint y WHERE y.article.article = :article AND y.conditionnement.byVente = TRUE"),

    @NamedQuery(name = "YvsBaseConditionnementPoint.findPointByArticle", query = "SELECT DISTINCT y.article.point FROM YvsBaseConditionnementPoint y WHERE y.article.article = :article"),

    @NamedQuery(name = "YvsBaseConditionnementPoint.findCritereVenteByArticle", query = "SELECT DISTINCT y.conditionnement FROM YvsBaseConditionnementPoint y WHERE y.article.article = :article AND y.conditionnement.byVente = TRUE"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findConditionnement", query = "SELECT DISTINCT(y.conditionnement) FROM YvsBaseConditionnementPoint y JOIN FETCH y.conditionnement.article JOIN FETCH y.conditionnement.unite WHERE y.article.article = :article AND y.article.point = :point"),
    @NamedQuery(name = "YvsBaseConditionnementPoint.findConditionnementByPoint", query = "SELECT DISTINCT(y.conditionnement) FROM YvsBaseConditionnementPoint y JOIN FETCH y.conditionnement.article JOIN FETCH y.conditionnement.unite WHERE y.conditionnement.byVente = TRUE AND y.article.point = :point ORDER BY y.article.article.designation  ")})
public class YvsBaseConditionnementPoint extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_conditionnement_point_id_seq", name = "yvs_base_conditionnement_point_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_conditionnement_point_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "puv")
    private Double puv;
    @Column(name = "prix_min")
    private Double prixMin;
    @Size(max = 2147483647)
    @Column(name = "nature_prix_min")
    private String naturePrixMin = Constantes.NATURE_MTANT;
    @Column(name = "remise")
    private Double remise;
    @Size(max = 2147483647)
    @Column(name = "nature_remise")
    private String natureRemise = Constantes.NATURE_MTANT;
    @Column(name = "avance_commance")
    private Double avanceCommance;
    @Column(name = "taux_pua")
    private Double tauxPua;
    @Column(name = "proportion_pua")
    private Boolean proportionPua = false;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticlePoint article;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private List<YvsComRabais> rabais;
    @Column(name = "change_prix")
    private Boolean changePrix = false;
    @Column(name = "actif")
    private Boolean actif;
    @Transient
    private Double pr;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    public static long ids = -1;

    public YvsBaseConditionnementPoint() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        this.rabais = new ArrayList<>();
        this.actif = true;
    }

    public YvsBaseConditionnementPoint(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseConditionnementPoint(YvsBaseArticlePoint article, YvsBaseConditionnement conditionnement) {
        this();
        this.article = article;
        this.conditionnement = conditionnement;
    }

    public YvsBaseConditionnementPoint(Long id, YvsBaseArticlePoint article, YvsBaseConditionnement conditionnement) {
        this(id);
        this.article = article;
        this.conditionnement = conditionnement;
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPuv() {
        return puv != null ? puv : 0;
    }

    public void setPuv(Double puv) {
        this.puv = puv;
    }

    public Double getPr() {
        return pr != null ? pr : 0;
    }

    public void setPr(Double pr) {
        this.pr = pr;
    }

    public Double getPrixMin() {
        return prixMin != null ? prixMin : 0;
    }

    public void setPrixMin(Double prixMin) {
        this.prixMin = prixMin;
    }

    public Double getAvanceCommance() {
        return avanceCommance != null ? avanceCommance : 0;
    }

    public void setAvanceCommance(Double avanceCommance) {
        this.avanceCommance = avanceCommance;
    }

    public String getNaturePrixMin() {
        return naturePrixMin != null ? (naturePrixMin.trim().length() > 0 ? naturePrixMin : yvs.dao.salaire.service.Constantes.NATURE_MTANT) : yvs.dao.salaire.service.Constantes.NATURE_MTANT;
    }

    public void setNaturePrixMin(String naturePrixMin) {
        this.naturePrixMin = naturePrixMin;
    }

    public Double getRemise() {
        return remise != null ? remise : 0;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public String getNatureRemise() {
        return natureRemise != null ? (natureRemise.trim().length() > 0 ? naturePrixMin : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getChangePrix() {
        return changePrix != null ? changePrix : false;
    }

    public void setChangePrix(Boolean changePrix) {
        this.changePrix = changePrix;
    }

    public Double getTauxPua() {
        return tauxPua != null ? tauxPua : 0;
    }

    public void setTauxPua(Double tauxPua) {
        this.tauxPua = tauxPua;
    }

    public Boolean getProportionPua() {
        return proportionPua != null ? proportionPua : false;
    }

    public void setProportionPua(Boolean proportionPua) {
        this.proportionPua = proportionPua;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsBaseArticlePoint getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticlePoint article) {
        this.article = article;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComRabais> getRabais() {
        return rabais;
    }

    public void setRabais(List<YvsComRabais> rabais) {
        this.rabais = rabais;
    }

    @XmlTransient
    @JsonIgnore
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

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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
        if (!(object instanceof YvsBaseConditionnementPoint)) {
            return false;
        }
        YvsBaseConditionnementPoint other = (YvsBaseConditionnementPoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseConditionnementPoint[ id=" + id + " ]";
    }

}
