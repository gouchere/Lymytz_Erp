/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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
import javax.validation.constraints.Size;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_permis_de_coduire")
@NamedQueries({
    @NamedQuery(name = "YvsPermisDeCoduire.findAll", query = "SELECT y FROM YvsPermisDeCoduire y"),
    @NamedQuery(name = "YvsPermisDeCoduire.findById", query = "SELECT y FROM YvsPermisDeCoduire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPermisDeCoduire.findByCategorie", query = "SELECT y FROM YvsPermisDeCoduire y WHERE y.categorie = :categorie"),
    @NamedQuery(name = "YvsPermisDeCoduire.findByNumero", query = "SELECT y FROM YvsPermisDeCoduire y WHERE y.numero = :numero"),
    @NamedQuery(name = "YvsPermisDeCoduire.findByDateObtention", query = "SELECT y FROM YvsPermisDeCoduire y WHERE y.dateObtention = :dateObtention"),
    @NamedQuery(name = "YvsPermisDeCoduire.findByEmploye", query = "SELECT y FROM YvsPermisDeCoduire y WHERE y.employe = :employe"),
    @NamedQuery(name = "YvsPermisDeCoduire.findByDateExpiration", query = "SELECT y FROM YvsPermisDeCoduire y WHERE y.dateExpiration = :dateExpiration")})
public class YvsPermisDeCoduire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_permis_de_coduire_id_seq")
    @SequenceGenerator(sequenceName = "yvs_permis_de_coduire_id_seq", allocationSize = 1, name = "yvs_permis_de_coduire_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "categorie")
    private String categorie;
    @Size(max = 2147483647)
    @Column(name = "numero")
    private String numero;
    @Column(name = "date_obtention")
    @Temporal(TemporalType.DATE)
    private Date dateObtention;
    @Column(name = "date_expiration")
    @Temporal(TemporalType.DATE)
    private Date dateExpiration;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;

    public YvsPermisDeCoduire() {
    }

    public YvsPermisDeCoduire(Long id) {
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDateObtention() {
        return dateObtention;
    }

    public void setDateObtention(Date dateObtention) {
        this.dateObtention = dateObtention;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
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
        if (!(object instanceof YvsPermisDeCoduire)) {
            return false;
        }
        YvsPermisDeCoduire other = (YvsPermisDeCoduire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsPermisDeCoduire[ id=" + id + " ]";
    }

}
