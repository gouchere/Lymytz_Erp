/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.rrr;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_ristourne")
@NamedQueries({
    @NamedQuery(name = "YvsComRistourne.findAll", query = "SELECT y FROM YvsComRistourne y WHERE y.plan.societe = :societe"),
    @NamedQuery(name = "YvsComRistourne.findById", query = "SELECT y FROM YvsComRistourne y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComRistourne.findByArticle", query = "SELECT y FROM YvsComRistourne y WHERE y.article = :article"),
    @NamedQuery(name = "YvsComRistourne.findByFamille", query = "SELECT y FROM YvsComRistourne y WHERE y.famille = :famille"),
    @NamedQuery(name = "YvsComRistourne.findCountOneByPlan", query = "SELECT COUNT(y) FROM YvsComRistourne y WHERE y.conditionnement=:cond AND y.plan=:plan AND y.permanent=true AND y.nature=:nature"),
    @NamedQuery(name = "YvsComRistourne.findCountOneByPlan2", query = "SELECT COUNT(y) FROM YvsComRistourne y WHERE y.famille=:famille AND y.plan=:plan AND y.permanent=true AND y.nature=:nature"),
    @NamedQuery(name = "YvsComRistourne.findByPlan", query = "SELECT y FROM YvsComRistourne y WHERE y.plan = :plan"),
    @NamedQuery(name = "YvsComRistourne.findByPlanActif", query = "SELECT y FROM YvsComRistourne y WHERE y.plan = :plan AND y.actif = true"),
    @NamedQuery(name = "YvsComRistourne.findByPlanArticle", query = "SELECT y FROM YvsComRistourne y WHERE y.plan = :plan AND y.article = :article"),
    @NamedQuery(name = "YvsComRistourne.findByPlanFamille", query = "SELECT y FROM YvsComRistourne y WHERE y.plan = :plan AND y.famille = :famille"),
    @NamedQuery(name = "YvsComRistourne.findByPlanArticleActif", query = "SELECT y FROM YvsComRistourne y WHERE y.plan = :plan AND y.article = :article AND y.actif = true"),
    @NamedQuery(name = "YvsComRistourne.findByPlanFamilleActif", query = "SELECT y FROM YvsComRistourne y WHERE y.plan = :plan AND y.famille = :famille AND y.actif = true")})
public class YvsComRistourne implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_ristourne_id_seq", name = "yvs_com_ristourne_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_ristourne_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles article;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFamilleArticle famille;
    @JoinColumn(name = "conditionnement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseConditionnement conditionnement;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPlanRistourne plan;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "ristourne")
    private List<YvsComGrilleRistourne> tranches;
    @Column(name = "nature")
    private Character nature;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean update;

    public YvsComRistourne() {
    }

    public YvsComRistourne(Long id) {
        this.id = id;
    }

    public YvsBaseConditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(YvsBaseConditionnement conditionnement) {
        this.conditionnement = conditionnement;
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
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDebut() {
        return dateDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateFin() {
        return dateFin;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBaseArticles getArticle() {
        return article;
    }

    public void setArticle(YvsBaseArticles article) {
        this.article = article;
    }

    public YvsBaseFamilleArticle getFamille() {
        return famille;
    }

    public void setFamille(YvsBaseFamilleArticle famille) {
        this.famille = famille;
    }

    @XmlTransient  @JsonIgnore
    public YvsComPlanRistourne getPlan() {
        return plan;
    }

    public void setPlan(YvsComPlanRistourne plan) {
        this.plan = plan;
    }

    public List<YvsComGrilleRistourne> getTranches() {
        return tranches;
    }

    public void setTranches(List<YvsComGrilleRistourne> tranches) {
        this.tranches = tranches;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Character getNature() {
        return nature != null ? nature : 'R';
    }

    public void setNature(Character nature) {
        this.nature = nature;
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
        if (!(object instanceof YvsComRistourne)) {
            return false;
        }
        YvsComRistourne other = (YvsComRistourne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.rrr.YvsComRistourne[ id=" + id + " ]";
    }

}
