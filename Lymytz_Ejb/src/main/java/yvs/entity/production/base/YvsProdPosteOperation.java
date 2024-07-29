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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_poste_operation")
@NamedQueries({
    @NamedQuery(name = "YvsProdPosteOperation.findAll", query = "SELECT y FROM YvsProdPosteOperation y"),
    @NamedQuery(name = "YvsProdPosteOperation.findById", query = "SELECT y FROM YvsProdPosteOperation y WHERE y.id = :id")})
public class YvsProdPosteOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_poste_phase_id_seq", name = "yvs_prod_poste_phase_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_poste_phase_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private Integer nombre;
    @Column(name = "type_charge")
    private String typeCharge; //Machine ou MO
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "poste_charge", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPosteCharge posteCharge;
    @JoinColumn(name = "operations", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdOperationsGamme operations;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence users;

    public YvsProdPosteOperation() {

    }

    public YvsProdPosteOperation(Integer id) {
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

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeCharge() {
        return typeCharge;
    }

    public void setTypeCharge(String typeCharge) {
        this.typeCharge = typeCharge;
    }

    public YvsProdPosteCharge getPosteCharge() {
        return posteCharge;
    }

    public void setPosteCharge(YvsProdPosteCharge posteCharge) {
        this.posteCharge = posteCharge;
    }

    public YvsProdOperationsGamme getOperations() {
        return operations;
    }

    public void setOperations(YvsProdOperationsGamme operations) {
        this.operations = operations;
    }

    public YvsUsersAgence getUsers() {
        return users;
    }

    public void setUsers(YvsUsersAgence users) {
        this.users = users;
    }

    public Integer getNombre() {
        return nombre != null ? nombre : 0;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof YvsProdPosteOperation)) {
            return false;
        }
        YvsProdPosteOperation other = (YvsProdPosteOperation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdPostePhase[ id=" + id + " ]";
    }

}
