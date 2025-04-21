/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_centre_charge")
@NamedQueries({
    @NamedQuery(name = "YvsProdCentreCharge.findAll", query = "SELECT y FROM YvsProdCentreCharge y WHERE y.siteProduction.agence.societe = :societe ORDER BY y.reference"),
    @NamedQuery(name = "YvsProdCentreCharge.findById", query = "SELECT y FROM YvsProdCentreCharge y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdCentreCharge.findByReference", query = "SELECT y FROM YvsProdCentreCharge y WHERE y.reference = :reference AND y.siteProduction.agence.societe = :societe"),
    @NamedQuery(name = "YvsProdCentreCharge.findByDesignation", query = "SELECT y FROM YvsProdCentreCharge y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsProdCentreCharge.findByDescription", query = "SELECT y FROM YvsProdCentreCharge y WHERE y.description = :description"),
    @NamedQuery(name = "YvsProdCentreCharge.findByActif", query = "SELECT y FROM YvsProdCentreCharge y WHERE y.actif = :actif")})
public class YvsProdCentreCharge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_centre_charge_id_seq", name = "yvs_prod_centre_charge_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_centre_charge_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "site_production", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdSiteProduction siteProduction;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsProdCentreCharge() {
    }

    public YvsProdCentreCharge(Integer id) {
        this.id = id;
    }

    public YvsProdCentreCharge(Integer id, String reference, String designation) {
        this.id = id;
        this.reference = reference;
        this.designation = designation;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsProdSiteProduction getSiteProduction() {
        return siteProduction;
    }

    public void setSiteProduction(YvsProdSiteProduction siteProduction) {
        this.siteProduction = siteProduction;
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
        if (!(object instanceof YvsProdCentreCharge)) {
            return false;
        }
        YvsProdCentreCharge other = (YvsProdCentreCharge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdCentreCharge[ id=" + id + " ]";
    }

}
