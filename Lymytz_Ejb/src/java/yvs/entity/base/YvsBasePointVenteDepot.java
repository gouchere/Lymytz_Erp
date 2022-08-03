/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_point_vente_depot")
@NamedQueries({
    @NamedQuery(name = "YvsBasePointVenteDepot.findAll", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.pointVente.agence.societe = :societe"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findByAgence", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.pointVente.agence = :agence"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findActifByAgence", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.pointVente.agence = :agence AND y.actif = TRUE AND y.depot.actif = TRUE"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findByPointVente", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.pointVente = :pointVente"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findActifByPointVente", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.pointVente = :pointVente AND y.actif = TRUE AND y.depot.actif = TRUE"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findByPointPrincipal", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.pointVente = :point AND y.principal = true"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findByDepot", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.depot = :depot"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findByOne", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.depot = :depot AND y.pointVente = :pointVente"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findById", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findByActif", query = "SELECT y FROM YvsBasePointVenteDepot y WHERE y.actif = :actif"),

    @NamedQuery(name = "YvsBasePointVenteDepot.findIdPointByDepot", query = "SELECT y.pointVente.id FROM YvsBasePointVenteDepot y WHERE y.depot.id IN :ids AND y.pointVente IS NOT NULL"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findPointByDepot", query = "SELECT DISTINCT(y.pointVente) FROM YvsBasePointVenteDepot y WHERE y.depot = :depot AND y.actif = TRUE AND (y.pointVente IS NOT NULL AND y.pointVente.actif = TRUE)"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findPointByDepotPrincipal", query = "SELECT DISTINCT(y.pointVente) FROM YvsBasePointVenteDepot y WHERE y.depot = :depot AND y.principal = true AND y.pointVente IS NOT NULL"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findDepotByPoint", query = "SELECT DISTINCT(y.depot) FROM YvsBasePointVenteDepot y WHERE y.pointVente = :pointVente AND y.actif = TRUE AND (y.depot IS NOT NULL AND y.depot.actif = TRUE)"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findIdDepotByPoint", query = "SELECT y.depot.id FROM YvsBasePointVenteDepot y WHERE y.pointVente = :pointVente AND y.actif = TRUE AND y.depot.actif = TRUE"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findDepotByAgence", query = "SELECT DISTINCT(y.depot) FROM YvsBasePointVenteDepot y WHERE y.pointVente.agence = :agence AND y.actif = TRUE AND y.depot.actif = TRUE"),
    @NamedQuery(name = "YvsBasePointVenteDepot.findDepotByPointPrincipal", query = "SELECT DISTINCT(y.depot) FROM YvsBasePointVenteDepot y WHERE y.pointVente = :point AND y.principal = true")})
public class YvsBasePointVenteDepot extends YvsEntity implements Serializable {

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_point_vente_depot_id_seq", name = "yvs_base_point_vente_depot_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_point_vente_depot_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif = true;
    @Column(name = "principal")
    private Boolean principal = false;

    @JoinColumn(name = "point_vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePointVente pointVente;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsBasePointVenteDepot() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBasePointVenteDepot(Long id) {
        this();
        this.id = id;
    }

    public YvsBasePointVenteDepot(Long id, Boolean actif, Boolean principal, YvsBaseDepots depot) {
        this(id);
        this.actif = actif;
        this.principal = principal;
        this.depot = depot;
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

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Boolean getPrincipal() {
        return principal != null ? principal : false;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBasePointVente getPointVente() {
        return pointVente;
    }

    public void setPointVente(YvsBasePointVente pointVente) {
        this.pointVente = pointVente;
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
        if (!(object instanceof YvsBasePointVenteDepot)) {
            return false;
        }
        YvsBasePointVenteDepot other = (YvsBasePointVenteDepot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBasePointVenteDepot[ id=" + id + " ]";
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

}
