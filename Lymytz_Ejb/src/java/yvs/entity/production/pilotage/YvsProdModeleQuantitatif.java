/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_modele_quantitatif")
@NamedQueries({
    @NamedQuery(name = "YvsProdModeleQuantitatif.findAll", query = "SELECT y FROM YvsProdModeleQuantitatif y WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsProdModeleQuantitatif.findById", query = "SELECT y FROM YvsProdModeleQuantitatif y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdModeleQuantitatif.findByRefModel", query = "SELECT y FROM YvsProdModeleQuantitatif y WHERE y.refModel = :refModel AND y.societe=:societe"),
    @NamedQuery(name = "YvsProdModeleQuantitatif.findByDateSave", query = "SELECT y FROM YvsProdModeleQuantitatif y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdModeleQuantitatif.findByDateUpdate", query = "SELECT y FROM YvsProdModeleQuantitatif y WHERE y.dateUpdate = :dateUpdate")})
public class YvsProdModeleQuantitatif implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_modele_quantitatif_id_seq", name = "yvs_prod_modele_quantitatif_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_modele_quantitatif_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "ref_model")
    private String refModel;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;

    public YvsProdModeleQuantitatif() {
    }

    public YvsProdModeleQuantitatif(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefModel() {
        return refModel;
    }

    public void setRefModel(String refModel) {
        this.refModel = refModel;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsProdModeleQuantitatif)) {
            return false;
        }
        YvsProdModeleQuantitatif other = (YvsProdModeleQuantitatif) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdModeleQuantitatif[ id=" + id + " ]";
    }

}
