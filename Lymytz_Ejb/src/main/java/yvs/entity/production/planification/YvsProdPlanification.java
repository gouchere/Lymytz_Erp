/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.planification;

import java.io.Serializable;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_planification")
@NamedQueries({
    @NamedQuery(name = "YvsProdPlanification.findAll", query = "SELECT y FROM YvsProdPlanification y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsProdPlanification.findAllC", query = "SELECT COUNT(y) FROM YvsProdPlanification y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsProdPlanification.findById", query = "SELECT y FROM YvsProdPlanification y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdPlanification.findByReference", query = "SELECT y FROM YvsProdPlanification y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsProdPlanification.findByTypeC", query = "SELECT COUNT(y) FROM YvsProdPlanification y WHERE y.typePlan = :typePlan AND y.societe = :societe"),
    @NamedQuery(name = "YvsProdPlanification.findByType", query = "SELECT y FROM YvsProdPlanification y WHERE y.typePlan = :typePlan AND y.societe = :societe"),
    @NamedQuery(name = "YvsProdPlanification.findByDatePlanification", query = "SELECT y FROM YvsProdPlanification y WHERE y.datePlanification = :datePlanification"),
    @NamedQuery(name = "YvsProdPlanification.findByHorizon", query = "SELECT y FROM YvsProdPlanification y WHERE y.horizon = :horizon"),
    @NamedQuery(name = "YvsProdPlanification.findByPeriode", query = "SELECT y FROM YvsProdPlanification y WHERE y.periode = :periode"),
    @NamedQuery(name = "YvsProdPlanification.findByAmplitude", query = "SELECT y FROM YvsProdPlanification y WHERE y.amplitude = :amplitude"),
    @NamedQuery(name = "YvsProdPlanification.findByDateDebut", query = "SELECT y FROM YvsProdPlanification y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsProdPlanification.findByDateFin", query = "SELECT y FROM YvsProdPlanification y WHERE y.dateFin = :dateFin")})
public class YvsProdPlanification implements Serializable {

    @Column(name = "methode_planif")
    private Short methodePlanif;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_planification_id_seq", name = "yvs_prod_planification_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_planification_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "type_plan")
    private String typePlan;
    @Column(name = "date_planification")
    @Temporal(TemporalType.DATE)
    private Date datePlanification;
    @Column(name = "horizon")
    private Integer horizon;
    @Column(name = "periodicite")
    private Integer periodicite;
    @Size(max = 2147483647)
    @Column(name = "periode")
    private String periode;
    @Column(name = "amplitude")
    private Integer amplitude;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @OneToMany(mappedBy = "plan")
    private List<YvsProdPeriodePlan> yvsProdPeriodePlanList;
    @OneToMany(mappedBy = "planAJour")
    private List<YvsProdPlanification> yvsProdPlanificationList;
    @JoinColumn(name = "plan_a_jour", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPlanification planAJour;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "societe", referencedColumnName = "id")
    private YvsSocietes societe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProdPlanification() {
    }

    public YvsProdPlanification(Long id) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public Integer getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(Integer periodicite) {
        this.periodicite = periodicite;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTypePlan() {
        return typePlan;
    }

    public void setTypePlan(String typePlan) {
        this.typePlan = typePlan;
    }

    public Date getDatePlanification() {
        return datePlanification;
    }

    public void setDatePlanification(Date datePlanification) {
        this.datePlanification = datePlanification;
    }

    public Integer getHorizon() {
        return horizon;
    }

    public void setHorizon(Integer horizon) {
        this.horizon = horizon;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Integer getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(Integer amplitude) {
        this.amplitude = amplitude;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public List<YvsProdPeriodePlan> getYvsProdPeriodePlanList() {
        return yvsProdPeriodePlanList;
    }

    public void setYvsProdPeriodePlanList(List<YvsProdPeriodePlan> yvsProdPeriodePlanList) {
        this.yvsProdPeriodePlanList = yvsProdPeriodePlanList;
    }

    public List<YvsProdPlanification> getYvsProdPlanificationList() {
        return yvsProdPlanificationList;
    }

    public void setYvsProdPlanificationList(List<YvsProdPlanification> yvsProdPlanificationList) {
        this.yvsProdPlanificationList = yvsProdPlanificationList;
    }

    public YvsProdPlanification getPlanAJour() {
        return planAJour;
    }

    public void setPlanAJour(YvsProdPlanification planAJour) {
        this.planAJour = planAJour;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsSocietes getSociete() {
        return societe;
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
        if (!(object instanceof YvsProdPlanification)) {
            return false;
        }
        YvsProdPlanification other = (YvsProdPlanification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdPlanification[ id=" + id + " ]";
    }

    public Short getMethodePlanif() {
        return methodePlanif;
    }

    public void setMethodePlanif(Short methodePlanif) {
        this.methodePlanif = methodePlanif;
    }
}
