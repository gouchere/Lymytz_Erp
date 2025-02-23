/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.planification;

import yvs.entity.production.planification.YvsProdPlanification;
import yvs.entity.production.planification.YvsProdDetailPdp;
import yvs.entity.production.planification.YvsProdDetailPic;
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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_periode_plan")
@NamedQueries({
    @NamedQuery(name = "YvsProdPeriodePlan.findAll", query = "SELECT y FROM YvsProdPeriodePlan y"),
    @NamedQuery(name = "YvsProdPeriodePlan.findById", query = "SELECT y FROM YvsProdPeriodePlan y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdPeriodePlan.findByReference", query = "SELECT y FROM YvsProdPeriodePlan y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsProdPeriodePlan.findByIndicatif", query = "SELECT y FROM YvsProdPeriodePlan y WHERE y.indicatif = :indicatif"),
    @NamedQuery(name = "YvsProdPeriodePlan.findByDebutPeriode", query = "SELECT y FROM YvsProdPeriodePlan y WHERE y.debutPeriode = :debutPeriode"),
    @NamedQuery(name = "YvsProdPeriodePlan.findByFinPeriode", query = "SELECT y FROM YvsProdPeriodePlan y WHERE y.finPeriode = :finPeriode")})
public class YvsProdPeriodePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_periode_plan_id_seq", name = "yvs_prod_periode_plan_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_periode_plan_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "indicatif")
    private Integer indicatif;
    @Column(name = "debut_periode")
    @Temporal(TemporalType.DATE)
    private Date debutPeriode;
    @Column(name = "fin_periode")
    @Temporal(TemporalType.DATE)
    private Date finPeriode;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPlanification plan;
    @OneToMany(mappedBy = "periode")
    private List<YvsProdDetailPdp> yvsProdDetailPdpList;
    @OneToMany(mappedBy = "periode")
    private List<YvsProdDetailPic> yvsProdDetailPicList;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProdPeriodePlan() {
    }

    public YvsProdPeriodePlan(Long id) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getIndicatif() {
        return indicatif;
    }

    public void setIndicatif(Integer indicatif) {
        this.indicatif = indicatif;
    }

    public Date getDebutPeriode() {
        return debutPeriode;
    }

    public void setDebutPeriode(Date debutPeriode) {
        this.debutPeriode = debutPeriode;
    }

    public Date getFinPeriode() {
        return finPeriode;
    }

    public void setFinPeriode(Date finPeriode) {
        this.finPeriode = finPeriode;
    }

    public YvsProdPlanification getPlan() {
        return plan;
    }

    public void setPlan(YvsProdPlanification plan) {
        this.plan = plan;
    }

    public List<YvsProdDetailPdp> getYvsProdDetailPdpList() {
        return yvsProdDetailPdpList;
    }

    public void setYvsProdDetailPdpList(List<YvsProdDetailPdp> yvsProdDetailPdpList) {
        this.yvsProdDetailPdpList = yvsProdDetailPdpList;
    }

    public List<YvsProdDetailPic> getYvsProdDetailPicList() {
        return yvsProdDetailPicList;
    }

    public void setYvsProdDetailPicList(List<YvsProdDetailPic> yvsProdDetailPicList) {
        this.yvsProdDetailPicList = yvsProdDetailPicList;
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
        if (!(object instanceof YvsProdPeriodePlan)) {
            return false;
        }
        YvsProdPeriodePlan other = (YvsProdPeriodePlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdPeriodePlan[ id=" + id + " ]";
    }

}
