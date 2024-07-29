/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.ration;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_param_ration")
@NamedQueries({
    @NamedQuery(name = "YvsComParamRation.findAll", query = "SELECT y FROM YvsComParamRation y WHERE y.personnel.societe = :societe"),
    @NamedQuery(name = "YvsComParamRation.findOne", query = "SELECT y FROM YvsComParamRation y WHERE y.personnel = :personnel AND y.article=:article AND y.conditionnement=:conditionnement"),
    @NamedQuery(name = "YvsComParamRation.findByArticle", query = "SELECT y FROM YvsComParamRation y WHERE y.personnel.societe = :societe AND y.article=:article"),
    @NamedQuery(name = "YvsComParamRation.findByArticleDate", query = "SELECT y FROM YvsComParamRation y WHERE y.personnel.societe = :societe AND y.article=:article AND y.datePriseEffet <= :date"),
    @NamedQuery(name = "YvsComParamRation.findByArticleDates", query = "SELECT y FROM YvsComParamRation y WHERE y.personnel.societe = :societe AND y.article=:article AND y.datePriseEffet <= :date AND y.id NOT IN :ids"),
    @NamedQuery(name = "YvsComParamRation.findById", query = "SELECT y FROM YvsComParamRation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComParamRation.findByQuantite", query = "SELECT y FROM YvsComParamRation y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsComParamRation.findByDateSave", query = "SELECT y FROM YvsComParamRation y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComParamRation.findByDateUpdate", query = "SELECT y FROM YvsComParamRation y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComParamRation.findSum", query = "SELECT SUM(y.quantite) FROM YvsComParamRation y WHERE y.personnel.societe = :societe AND y.actif = true"),
    @NamedQuery(name = "YvsComParamRation.findQteByTiers", query = "SELECT y.quantite FROM YvsComParamRation y WHERE y.personnel= :personnel AND y.actif = true"),
    @NamedQuery(name = "YvsComParamRation.findQteByTiersArticle", query = "SELECT y.quantite FROM YvsComParamRation y WHERE y.personnel= :personnel AND y.article = :article AND y.actif = true"),
    @NamedQuery(name = "YvsComParamRation.findSumDate", query = "SELECT SUM(y.quantite) FROM YvsComParamRation y WHERE y.personnel.societe = :societe AND y.actif = true AND y.id NOT IN :ids"),
    @NamedQuery(name = "YvsComParamRation.findSumByArticle", query = "SELECT SUM(y.quantite) FROM YvsComParamRation y WHERE y.article = :article AND y.actif = true"),
    @NamedQuery(name = "YvsComParamRation.findArticle", query = "SELECT DISTINCT(y.article) FROM YvsComParamRation y WHERE y.personnel.societe = :societe AND y.actif = true")})
public class YvsComParamRation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_param_ration_id_seq", name = "yvs_com_param_ration_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_param_ration_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "periode")
    private Integer periode;
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "proportionnel")
    private Boolean proportionnel;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_prise_effet")
    @Temporal(TemporalType.DATE)
    private Date datePriseEffet;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "personnel", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTiers personnel;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;

    @OneToMany(mappedBy = "personnel")
    private List<YvsComParamRationSuspension> suspensions;

    @Transient
    private boolean suspendu;

    public YvsComParamRation() {
        suspensions = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComParamRation(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPeriode() {
        return periode != null ? periode : 30;
    }

    public void setPeriode(Integer periode) {
        this.periode = periode;
    }

    public Boolean getProportionnel() {
        return proportionnel != null ? proportionnel : false;
    }

    public void setProportionnel(Boolean proportionnel) {
        this.proportionnel = proportionnel;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDatePriseEffet() {
        return datePriseEffet != null ? datePriseEffet : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDatePriseEffet(Date datePriseEffet) {
        this.datePriseEffet = datePriseEffet;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseTiers getPersonnel() {
        return personnel;
    }

    public void setPersonnel(YvsBaseTiers personnel) {
        this.personnel = personnel;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public boolean isSuspendu() {
//        return suspensions != null ? !suspensions.isEmpty() : false;
        return this.suspendu;
    }

    public void setSuspendu(boolean suspendu) {
        this.suspendu = suspendu;
    }

    public String etat() {
        return etat(new Date());
    }

    public String etat(Date date) {
        for (YvsComParamRationSuspension s : suspensions) {
            if (!s.getDebutSuspension().after(date) && !s.getFinSuspension().before(date)) {
                return "A";
            }
        }
        return "E";
    }

    public String etat(Date debut, Date fin) {
        for (YvsComParamRationSuspension s : suspensions) {
            if ((!s.getDebutSuspension().after(debut) && !s.getFinSuspension().before(debut)) || (!s.getDebutSuspension().after(fin) && !s.getFinSuspension().before(fin))) {
                return "A";
            }
        }
        return "E";
    }

    public List<YvsComParamRationSuspension> getSuspensions() {
        return suspensions;
    }

    public void setSuspensions(List<YvsComParamRationSuspension> suspensions) {
        this.suspensions = suspensions;
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
        if (!(object instanceof YvsComParamRation)) {
            return false;
        }
        YvsComParamRation other = (YvsComParamRation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.ration.YvsComParamRation[ id=" + id + " ]";
    }

}
