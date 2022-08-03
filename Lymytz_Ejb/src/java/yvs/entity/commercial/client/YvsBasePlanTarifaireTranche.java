/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.client;

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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_plan_tarifaire_tranche")
@NamedQueries({
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findAll", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.plan.categorie.societe = :societe"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByPlan", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.plan = :plan"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findById", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByPuv", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.puv = :puv"),
    @NamedQuery(name = "YvsBasePlanTarifaireTranche.findByRemise", query = "SELECT y FROM YvsBasePlanTarifaireTranche y WHERE y.remise = :remise")})
public class YvsBasePlanTarifaireTranche implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_plan_tarifaire_tranche_id_seq", name = "yvs_base_plan_tarifaire_tranche_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_plan_tarifaire_tranche_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "valeur_min")
    private Double valeurMin;
    @Column(name = "valeur_max")
    private Double valeurMax;
    @Column(name = "remise")
    private Double remise;
    @Column(name = "base")
    private String base;
    @Column(name = "nature_remise")
    private String natureRemise;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanTarifaire plan;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsBasePlanTarifaireTranche() {
    }

    public YvsBasePlanTarifaireTranche(Long id) {
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNatureRemise() {
        return natureRemise != null ? (natureRemise.trim().length() > 0 ? natureRemise : Constantes.NATURE_MTANT) : Constantes.NATURE_MTANT;
    }

    public void setNatureRemise(String natureRemise) {
        this.natureRemise = natureRemise;
    }

    public String getBase() {
        return base != null ? (base.trim().length() > 0 ? base : Constantes.BASE_QTE) : Constantes.BASE_QTE;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Double getValeurMin() {
        return valeurMin != null ? valeurMin : 0;
    }

    public void setValeurMin(Double valeurMin) {
        this.valeurMin = valeurMin;
    }

    public Double getValeurMax() {
        return valeurMax != null ? valeurMax : 0;
    }

    public void setValeurMax(Double valeurMax) {
        this.valeurMax = valeurMax;
    }

    public YvsBasePlanTarifaire getPlan() {
        return plan;
    }

    public void setPlan(YvsBasePlanTarifaire plan) {
        this.plan = plan;
    }

    public Double getPuv() {
        return puv != null ? puv : 0;
    }

    public void setPuv(Double puv) {
        this.puv = puv;
    }

    public Double getRemise() {
        return remise != null ? remise : 0;
    }

    public void setRemise(Double remise) {
        this.remise = remise;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsBasePlanTarifaireTranche)) {
            return false;
        }
        YvsBasePlanTarifaireTranche other = (YvsBasePlanTarifaireTranche) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.client.YvsBasePlanTarifaire[ id=" + id + " ]";
    }

}
