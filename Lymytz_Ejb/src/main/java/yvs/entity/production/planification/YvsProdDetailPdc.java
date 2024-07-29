/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.planification;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import yvs.entity.production.base.YvsProdOperationsGamme;

/**
 *
 * @author Lymytz
 */
@Entity
@Table(name = "yvs_prod_detail_pdc")
@NamedQueries({
    @NamedQuery(name = "YvsProdDetailPdc.findAll", query = "SELECT y FROM YvsProdDetailPdc y "),
    @NamedQuery(name = "YvsProdDetailPdc.findSociete", query = "SELECT y FROM YvsProdDetailPdc y WHERE y.pdp.periode.plan.societe = :societe"),
    @NamedQuery(name = "YvsProdDetailPdc.findById", query = "SELECT y FROM YvsProdDetailPdc y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdDetailPdc.findByPhase", query = "SELECT y FROM YvsProdDetailPdc y WHERE y.phase = :phase"),
    @NamedQuery(name = "YvsProdDetailPdc.findByCapaciteH", query = "SELECT y FROM YvsProdDetailPdc y WHERE y.capaciteH = :capaciteH"),
    @NamedQuery(name = "YvsProdDetailPdc.findByCapaciteQ", query = "SELECT y FROM YvsProdDetailPdc y WHERE y.capaciteQ = :capaciteQ"),
    @NamedQuery(name = "YvsProdDetailPdc.findByChargeH", query = "SELECT y FROM YvsProdDetailPdc y WHERE y.chargeH = :chargeH"),
    @NamedQuery(name = "YvsProdDetailPdc.findByChargeQ", query = "SELECT y FROM YvsProdDetailPdc y WHERE y.chargeQ = :chargeQ"),
    @NamedQuery(name = "YvsProdDetailPdc.findByModsH", query = "SELECT y FROM YvsProdDetailPdc y WHERE y.modsH = :modsH"),
    @NamedQuery(name = "YvsProdDetailPdc.findByModsQ", query = "SELECT y FROM YvsProdDetailPdc y WHERE y.modsQ = :modsQ")})
public class YvsProdDetailPdc implements Serializable {

    @JoinColumn(name = "pdp", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdDetailPdp pdp;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_detail_pdc_id_seq", name = "yvs_prod_detail_pdc_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_detail_pdc_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "capacite_h")
    private Double capaciteH;
    @Column(name = "capacite_q")
    private Double capaciteQ;
    @Column(name = "charge_h")
    private Double chargeH;
    @Column(name = "charge_q")
    private Double chargeQ;
    @Column(name = "mods_h")
    private Double modsH;
    @Column(name = "mods_q")
    private Integer modsQ;
    @JoinColumn(name = "phase", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdOperationsGamme phase;

    public YvsProdDetailPdc() {
    }

    public YvsProdDetailPdc(Long id) {
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

    public Double getCapaciteH() {
        return capaciteH;
    }

    public void setCapaciteH(Double capaciteH) {
        this.capaciteH = capaciteH;
    }

    public Double getCapaciteQ() {
        return capaciteQ;
    }

    public void setCapaciteQ(Double capaciteQ) {
        this.capaciteQ = capaciteQ;
    }

    public Double getChargeH() {
        return chargeH;
    }

    public void setChargeH(Double chargeH) {
        this.chargeH = chargeH;
    }

    public Double getChargeQ() {
        return chargeQ;
    }

    public void setChargeQ(Double chargeQ) {
        this.chargeQ = chargeQ;
    }

    public Double getModsH() {
        return modsH;
    }

    public void setModsH(Double modsH) {
        this.modsH = modsH;
    }

    public Integer getModsQ() {
        return modsQ;
    }

    public void setModsQ(Integer modsQ) {
        this.modsQ = modsQ;
    }

    public YvsProdOperationsGamme getPhase() {
        return phase;
    }

    public void setPhase(YvsProdOperationsGamme phase) {
        this.phase = phase;
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
        if (!(object instanceof YvsProdDetailPdc)) {
            return false;
        }
        YvsProdDetailPdc other = (YvsProdDetailPdc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.planification.YvsProdDetailPdc[ id=" + id + " ]";
    }

    public YvsProdDetailPdp getPdp() {
        return pdp;
    }

    public void setPdp(YvsProdDetailPdp pdp) {
        this.pdp = pdp;
    }

}
