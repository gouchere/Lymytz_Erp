/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.ration;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.commercial.achat.YvsComLotReception;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_ration")
@NamedQueries({
    @NamedQuery(name = "YvsComRation.findAll", query = "SELECT y FROM YvsComRation y"),
    @NamedQuery(name = "YvsComRation.findOne", query = "SELECT y FROM YvsComRation y WHERE y.personnel=:personnel AND y.article=:article AND y.docRation.periode=:periode"),
    @NamedQuery(name = "YvsComRation.findOneDay", query = "SELECT y FROM YvsComRation y WHERE y.personnel=:personnel AND y.article=:article AND y.docRation.periode=:periode AND y.docRation.dateFiche=:jour"),
    @NamedQuery(name = "YvsComRation.findById", query = "SELECT y FROM YvsComRation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComRation.findByArticle", query = "SELECT y FROM YvsComRation y WHERE y.article = :article"),
    @NamedQuery(name = "YvsComRation.findByQuantite", query = "SELECT y FROM YvsComRation y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsComRation.findByQuantitePris", query = "SELECT y.quantite FROM YvsComRation y WHERE y.personnel=:personnel AND y.article=:article AND y.dateRation=:date"),
    @NamedQuery(name = "YvsComRation.findByDateSave", query = "SELECT y FROM YvsComRation y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComRation.findByDateUpdate", query = "SELECT y FROM YvsComRation y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComRation.findSumByArticle", query = "SELECT SUM(y.quantite) FROM YvsComRation y WHERE y.article = :article"),
    @NamedQuery(name = "YvsComRation.findSumByFiche", query = "SELECT SUM(y.quantite) FROM YvsComRation y WHERE y.docRation = :docRation"),
    @NamedQuery(name = "YvsComRation.findSumByPeriode", query = "SELECT SUM(y.quantite) FROM YvsComRation y WHERE y.docRation.periode=:periode AND y.personnel=:tiers and y.article=:article"),
    @NamedQuery(name = "YvsComRation.findByFicheC", query = "SELECT COUNT(y) FROM YvsComRation y WHERE y.docRation = :docRation"),
    @NamedQuery(name = "YvsComRation.findSumByFicheArticle", query = "SELECT SUM(y.quantite) FROM YvsComRation y WHERE y.article = :article AND y.docRation = :docRation")})
public class YvsComRation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_ration_id_seq", name = "yvs_com_ration_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_ration_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_ration")
    @Temporal(TemporalType.DATE)
    private Date dateRation;
    @Column(name = "calcul_pr")
    private Boolean calculPr;
    @JoinColumn(name = "lot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComLotReception lot;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article = new YvsBaseArticles();
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "personnel", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTiers personnel = new YvsBaseTiers();
    @JoinColumn(name = "doc_ration", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocRation docRation;
    @Transient
    private YvsComDocRation last;
    @Transient
    private double stock_;
    @Transient
    private double qteMax;
    @Transient
    private double qtePrise;
    @Transient
    private boolean new_;
    @Transient
    private boolean actif = true;

    public YvsComRation() {
    }

    public YvsComRation(Long id) {
        this();
        this.id = id;
    }

    public YvsComRation(Long id, YvsUsersAgence author) {
        this(id);
        this.author = author;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCalculPr() {
        return calculPr != null ? calculPr : true;
    }

    public void setCalculPr(Boolean calculPr) {
        this.calculPr = calculPr;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0.0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseTiers getPersonnel() {
        return personnel != null ? personnel : new YvsBaseTiers();
    }

    public void setPersonnel(YvsBaseTiers personnel) {
        this.personnel = personnel;
    }

    public YvsComDocRation getDocRation() {
        return docRation;
    }

    public void setDocRation(YvsComDocRation docRation) {
        this.docRation = docRation;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public YvsComLotReception getLot() {
        return lot;
    }

    public void setLot(YvsComLotReception lot) {
        this.lot = lot;
    }

    public Date getDateRation() {
        return dateRation;
    }

    public void setDateRation(Date dateRation) {
        this.dateRation = dateRation;
    }

    public double getStock_() {
        return stock_;
    }

    public void setStock_(double stock_) {
        this.stock_ = stock_;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public double getQteMax() {
        return qteMax;
    }

    public void setQteMax(double qteMax) {
        this.qteMax = qteMax;
    }

    public double getQtePrise() {
        return qtePrise;
    }

    public void setQtePrise(double qtePrise) {
        this.qtePrise = qtePrise;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComDocRation getLast() {
        return last;
    }

    public void setLast(YvsComDocRation last) {
        this.last = last;
    }

    public double getMaxPeriode(DaoInterfaceLocal dao) {
        if (getPersonnel() != null ? getPersonnel().getId() > 0 : false) {
            Double re = (Double) dao.loadObjectByNameQueries("YvsComParamRation.findQteByTiers", new String[]{"personnel"}, new Object[]{getPersonnel()});
            return (re != null) ? re : 0;
        }
        return 0;
    }

    public double getQtePeriode(DaoInterfaceLocal dao) {
        if (getPersonnel() != null ? getPersonnel().getId() > 0 : false) {
            Double re = (Double) dao.loadObjectByNameQueries("YvsComRation.findSumByPeriode", new String[]{"periode", "tiers", "article"}, new Object[]{getDocRation().getPeriode(), getPersonnel(), getArticle()});
            return (re != null) ? re : 0;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsComRation other = (YvsComRation) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.ration.YvsComRation[ article=" + article + " ]";
    }

}
