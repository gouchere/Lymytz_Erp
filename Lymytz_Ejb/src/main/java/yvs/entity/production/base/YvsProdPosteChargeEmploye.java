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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_prod_poste_charge_employe")
@NamedQueries({
    @NamedQuery(name = "YvsProdPosteChargeEmploye.findAll", query = "SELECT y FROM YvsProdPosteChargeEmploye y"),
    @NamedQuery(name = "YvsProdPosteChargeEmploye.findById", query = "SELECT y FROM YvsProdPosteChargeEmploye y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdPosteChargeEmploye.findOne", query = "SELECT y FROM YvsProdPosteChargeEmploye y WHERE y.ressource = :ressource"),
    @NamedQuery(name = "YvsProdPosteChargeEmploye.findByDateUpdate", query = "SELECT y FROM YvsProdPosteChargeEmploye y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsProdPosteChargeEmploye.findByDateSave", query = "SELECT y FROM YvsProdPosteChargeEmploye y WHERE y.dateSave = :dateSave")})
public class YvsProdPosteChargeEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_poste_charge_employe_id_seq", name = "yvs_prod_poste_charge_employe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_poste_charge_employe_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "ressource")
    @OneToOne
    private YvsProdPosteCharge ressource;

    public YvsProdPosteChargeEmploye() {
    }

    public YvsProdPosteChargeEmploye(Integer id) {
        this.id = id;
    }

    public YvsProdPosteChargeEmploye(YvsProdPosteCharge ressource, YvsGrhEmployes employe) {
        this.employe = employe;
        this.ressource = ressource;
    }

    public Integer getId() {
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProdPosteCharge getRessource() {
        return ressource;
    }

    public void setRessource(YvsProdPosteCharge ressource) {
        this.ressource = ressource;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
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
        if (!(object instanceof YvsProdPosteChargeEmploye)) {
            return false;
        }
        YvsProdPosteChargeEmploye other = (YvsProdPosteChargeEmploye) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdPosteChargeEmploye[ id=" + id + " ]";
    }

}
