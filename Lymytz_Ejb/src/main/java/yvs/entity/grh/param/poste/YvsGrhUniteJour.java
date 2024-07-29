/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param.poste;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_unite_jour")
@NamedQueries({
    @NamedQuery(name = "YvsGrhUniteJour.findAll", query = "SELECT y FROM YvsGrhUniteJour y"),
    @NamedQuery(name = "YvsGrhUniteJour.findById", query = "SELECT y FROM YvsGrhUniteJour y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhUniteJour.findByUnite", query = "SELECT y FROM YvsGrhUniteJour y WHERE y.unite = :unite"),
    @NamedQuery(name = "YvsGrhUniteJour.findByAuthor", query = "SELECT y FROM YvsGrhUniteJour y WHERE y.author = :author")})
public class YvsGrhUniteJour implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_unite_jour_id_seq", name = "yvs_grh_unite_jour_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_unite_jour_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "unite")
    private String unite;
    @Column(name = "author")
    private BigInteger author;
    @JoinColumn(name = "id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private YvsUsersAgence yvsUsersAgence;

    public YvsGrhUniteJour() {
    }

    public YvsGrhUniteJour(Integer id) {
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

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public BigInteger getAuthor() {
        return author;
    }

    public void setAuthor(BigInteger author) {
        this.author = author;
    }

    public YvsUsersAgence getYvsUsersAgence() {
        return yvsUsersAgence;
    }

    public void setYvsUsersAgence(YvsUsersAgence yvsUsersAgence) {
        this.yvsUsersAgence = yvsUsersAgence;
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
        if (!(object instanceof YvsGrhUniteJour)) {
            return false;
        }
        YvsGrhUniteJour other = (YvsGrhUniteJour) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.poste.YvsGrhUniteJour[ id=" + id + " ]";
    }

}
