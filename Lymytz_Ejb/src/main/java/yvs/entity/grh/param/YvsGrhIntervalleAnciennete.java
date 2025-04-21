/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_intervalle_anciennete")
@NamedQueries({
    @NamedQuery(name = "YvsGrhIntervalleAnciennete.findAll", query = "SELECT y FROM YvsGrhIntervalleAnciennete y WHERE y.societe=:societe ORDER BY y.ancienneteMin ASC"),
    @NamedQuery(name = "YvsGrhIntervalleAnciennete.findById", query = "SELECT y FROM YvsGrhIntervalleAnciennete y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhIntervalleAnciennete.findByAncienneteMin", query = "SELECT y FROM YvsGrhIntervalleAnciennete y WHERE y.ancienneteMin = :ancienneteMin"),
    @NamedQuery(name = "YvsGrhIntervalleAnciennete.findByAncienneteMax", query = "SELECT y FROM YvsGrhIntervalleAnciennete y WHERE y.ancienneteMax = :ancienneteMax")})
public class YvsGrhIntervalleAnciennete implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_intervalle_anciennete_id_seq", name = "yvs_grh_intervalle_anciennete_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_intervalle_anciennete_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "anciennete_min")
    private Integer ancienneteMin;
    @Column(name = "anciennete_max")
    private Integer ancienneteMax;
    
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhIntervalleAnciennete() {
    }

    public YvsGrhIntervalleAnciennete(Integer id) {
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

    public Integer getAncienneteMin() {
        return ancienneteMin;
    }

    public void setAncienneteMin(Integer ancienneteMin) {
        this.ancienneteMin = ancienneteMin;
    }

    public Integer getAncienneteMax() {
        return ancienneteMax;
    }

    public void setAncienneteMax(Integer ancienneteMax) {
        this.ancienneteMax = ancienneteMax;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsGrhIntervalleAnciennete)) {
            return false;
        }
        YvsGrhIntervalleAnciennete other = (YvsGrhIntervalleAnciennete) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhIntervalleAnciennete[ id=" + id + " ]";
    }

}
