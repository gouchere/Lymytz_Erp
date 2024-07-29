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
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz
 */
@Entity
@Table(name = "yvs_base_depot_operation")
@NamedQueries({
    @NamedQuery(name = "YvsBaseDepotOperation.findAll", query = "SELECT y FROM YvsBaseDepotOperation y"),
    @NamedQuery(name = "YvsBaseDepotOperation.findById", query = "SELECT y FROM YvsBaseDepotOperation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseDepotOperation.findByDepot", query = "SELECT y FROM YvsBaseDepotOperation y WHERE y.depot = :depot"),
    @NamedQuery(name = "YvsBaseDepotOperation.findDepotByOperation", query = "SELECT DISTINCT (y.depot) FROM YvsBaseDepotOperation y WHERE y.type=:type AND y.operation=:operation"),
    @NamedQuery(name = "YvsBaseDepotOperation.findDepotActifByOperation", query = "SELECT DISTINCT (y.depot) FROM YvsBaseDepotOperation y WHERE y.type=:type AND y.operation=:operation AND y.depot.actif = true AND y.depot.agence.societe=:societe"),
    @NamedQuery(name = "YvsBaseDepotOperation.findIdsDepotActifByOperation", query = "SELECT DISTINCT (y.depot.id) FROM YvsBaseDepotOperation y WHERE y.type=:type AND y.operation=:operation AND y.depot.actif = true AND y.depot.agence.societe=:societe"),
    @NamedQuery(name = "YvsBaseDepotOperation.findByDepotType", query = "SELECT y FROM YvsBaseDepotOperation y WHERE y.depot = :depot AND y.type = :type"),
    @NamedQuery(name = "YvsBaseDepotOperation.countByDepotType", query = "SELECT COUNT(y) FROM YvsBaseDepotOperation y WHERE y.depot = :depot AND y.type = :type"),
    @NamedQuery(name = "YvsBaseDepotOperation.findByDepotTypeOperation", query = "SELECT y FROM YvsBaseDepotOperation y WHERE y.depot = :depot AND y.type = :type AND y.operation = :operation"),
    @NamedQuery(name = "YvsBaseDepotOperation.cByDepotTypeOperation", query = "SELECT COUNT(y) FROM YvsBaseDepotOperation y WHERE y.depot = :depot AND y.type = :type AND y.operation = :operation"),
    @NamedQuery(name = "YvsBaseDepotOperation.countByDepotTypeOperation", query = "SELECT COUNT(y) FROM YvsBaseDepotOperation y WHERE y.depot = :depot AND y.type = :type AND y.operation = :operation"),
    @NamedQuery(name = "YvsBaseDepotOperation.findByOperation", query = "SELECT y FROM YvsBaseDepotOperation y WHERE y.operation = :operation"),
    @NamedQuery(name = "YvsBaseDepotOperation.findByType", query = "SELECT y FROM YvsBaseDepotOperation y WHERE y.type = :type")})
public class YvsBaseDepotOperation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_depot_operation_id_seq", name = "yvs_base_depot_operation_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_depot_operation_id_seq_name", strategy = GenerationType.SEQUENCE) 
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
    @Column(name = "operation")
    private String operation;
    @Size(max = 2147483647)
    @Column(name = "type")
    private String type;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsBaseDepotOperation() {
    }

    public YvsBaseDepotOperation(Integer id) {
        this.id = id;
    }

    public YvsBaseDepotOperation(Integer id, String operation, String type) {
        this.id = id;
        this.operation = operation;
        this.type = type;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsBaseDepotOperation)) {
            return false;
        }
        YvsBaseDepotOperation other = (YvsBaseDepotOperation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseDepotOperation[ id=" + id + " ]";
    }

}
