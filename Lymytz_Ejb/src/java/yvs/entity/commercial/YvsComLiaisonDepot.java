/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial;

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
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_liaison_depot")
@NamedQueries({
    @NamedQuery(name = "YvsComLiaisonDepot.findAll", query = "SELECT y FROM YvsComLiaisonDepot y WHERE y.depot.agence.societe = :societe"),
    @NamedQuery(name = "YvsComLiaisonDepot.findByAgence", query = "SELECT y FROM YvsComLiaisonDepot y WHERE y.depot.agence = :agence"),
    @NamedQuery(name = "YvsComLiaisonDepot.findByDepot", query = "SELECT y FROM YvsComLiaisonDepot y WHERE y.depot = :depot"),
    @NamedQuery(name = "YvsComLiaisonDepot.findById", query = "SELECT y FROM YvsComLiaisonDepot y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComLiaisonDepot.findByOne", query = "SELECT y FROM YvsComLiaisonDepot y WHERE y.depotLier = :depotLier AND y.depot = :depot"),
    
    @NamedQuery(name = "YvsComLiaisonDepot.findByDepotTransit", query = "SELECT DISTINCT(y.depotLier) FROM YvsComLiaisonDepot y WHERE y.depot = :depot AND y.transit = TRUE"),

    @NamedQuery(name = "YvsComLiaisonDepot.findDepotByDepot", query = "SELECT y.depotLier FROM YvsComLiaisonDepot y WHERE (y.depot = :depot OR y.depotLier = :depot)"),
    @NamedQuery(name = "YvsComLiaisonDepot.findDepotLierByDepot", query = "SELECT DISTINCT(y.depotLier) FROM YvsComLiaisonDepot y WHERE y.depot = :depot"),
    @NamedQuery(name = "YvsComLiaisonDepot.findDepotLierByDepot_", query = "SELECT DISTINCT(y.depotLier) FROM YvsComLiaisonDepot y WHERE y.depot = :depot AND y.depotLier.actif=true"),
    @NamedQuery(name = "YvsComLiaisonDepot.findDepotByDepotLier", query = "SELECT DISTINCT(y.depot) FROM YvsComLiaisonDepot y WHERE y.depotLier = :depot")})
public class YvsComLiaisonDepot extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_liaison_depot_id_seq", name = "yvs_com_liaison_depot_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_liaison_depot_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "transit")
    private Boolean transit;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "depot_lier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depotLier;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsComLiaisonDepot() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComLiaisonDepot(Long id) {
        this();
        this.id = id;
    }

    public Boolean getTransit() {
        return transit != null ? transit : false;
    }

    public void setTransit(Boolean transit) {
        this.transit = transit;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsBaseDepots getDepotLier() {
        return depotLier;
    }

    public void setDepotLier(YvsBaseDepots depotLier) {
        this.depotLier = depotLier;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
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
        if (!(object instanceof YvsComLiaisonDepot)) {
            return false;
        }
        YvsComLiaisonDepot other = (YvsComLiaisonDepot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.YvsComLiaisonDepot[ id=" + id + " ]";
    }

}
