/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

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
import javax.persistence.Transient;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.param.YvsBaseCentreValorisation;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_centre_poste_charge")
@NamedQueries({
    @NamedQuery(name = "YvsProdCentrePosteCharge.findAll", query = "SELECT y FROM YvsProdCentrePosteCharge y"),
    @NamedQuery(name = "YvsProdCentrePosteCharge.findById", query = "SELECT y FROM YvsProdCentrePosteCharge y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdCentrePosteCharge.findByValeur", query = "SELECT y FROM YvsProdCentrePosteCharge y WHERE y.valeur = :valeur"),
    @NamedQuery(name = "YvsProdCentrePosteCharge.findByDirect", query = "SELECT y FROM YvsProdCentrePosteCharge y WHERE y.direct = :direct"),
    @NamedQuery(name = "YvsProdCentrePosteCharge.findByPeriode", query = "SELECT y FROM YvsProdCentrePosteCharge y WHERE y.periode = :periode"),

    @NamedQuery(name = "YvsProdCentrePosteCharge.findByPosteCentre", query = "SELECT y FROM YvsProdCentrePosteCharge y WHERE y.centreValorisation = :centre AND y.posteCharge = :poste")})
public class YvsProdCentrePosteCharge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_centre_poste_charge_id_seq", name = "yvs_prod_centre_poste_charge_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_centre_poste_charge_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "direct")
    private Boolean direct;
    @JoinColumn(name = "periode", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure periode;
    @JoinColumn(name = "poste_charge", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPosteCharge posteCharge;
    @JoinColumn(name = "centre_valorisation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCentreValorisation centreValorisation;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsProdCentrePosteCharge() {
    }

    public YvsProdCentrePosteCharge(Integer id) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValeur() {
        return valeur != null ? valeur : 0.0;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Boolean getDirect() {
        return direct != null ? direct : false;
    }

    public void setDirect(Boolean direct) {
        this.direct = direct;
    }

    public YvsBaseUniteMesure getPeriode() {
        return periode;
    }

    public void setPeriode(YvsBaseUniteMesure periode) {
        this.periode = periode;
    }

    public YvsProdPosteCharge getPosteCharge() {
        return posteCharge;
    }

    public void setPosteCharge(YvsProdPosteCharge posteCharge) {
        this.posteCharge = posteCharge;
    }

    public YvsBaseCentreValorisation getCentreValorisation() {
        return centreValorisation;
    }

    public void setCentreValorisation(YvsBaseCentreValorisation centreValorisation) {
        this.centreValorisation = centreValorisation;
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
        if (!(object instanceof YvsProdCentrePosteCharge)) {
            return false;
        }
        YvsProdCentrePosteCharge other = (YvsProdCentrePosteCharge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdCentrePosteCharge[ id=" + id + " ]";
    }

}
