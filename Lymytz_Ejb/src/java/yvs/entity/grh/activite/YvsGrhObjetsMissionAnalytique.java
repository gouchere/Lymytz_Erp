/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

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
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_grh_objets_mission_analytique")
@NamedQueries({
    @NamedQuery(name = "YvsGrhObjetsMissionAnalytique.findAll", query = "SELECT y FROM YvsGrhObjetsMissionAnalytique y"),
    @NamedQuery(name = "YvsGrhObjetsMissionAnalytique.findById", query = "SELECT y FROM YvsGrhObjetsMissionAnalytique y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhObjetsMissionAnalytique.findByObjectCentre", query = "SELECT y FROM YvsGrhObjetsMissionAnalytique y WHERE y.objetMission = :objetMission OR y.centre =:centre"),
    @NamedQuery(name = "YvsGrhObjetsMissionAnalytique.findByObject", query = "SELECT y FROM YvsGrhObjetsMissionAnalytique y WHERE y.objetMission = :objetMission"),
    @NamedQuery(name = "YvsGrhObjetsMissionAnalytique.findByCentre", query = "SELECT y FROM YvsGrhObjetsMissionAnalytique y WHERE y.centre =:centre"),
    @NamedQuery(name = "YvsGrhObjetsMissionAnalytique.findByPourcentage", query = "SELECT y FROM YvsGrhObjetsMissionAnalytique y WHERE y.coefficient = :coefficient")})
public class YvsGrhObjetsMissionAnalytique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_objets_mission_analytique_id_seq", name = "yvs_grh_objets_mission_analytique_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_objets_mission_analytique_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coefficient")
    private Double coefficient;
    @JoinColumn(name = "centre", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCentreAnalytique centre;
    @JoinColumn(name = "objet_mission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhObjetsMission objetMission;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean new_;

    public YvsGrhObjetsMissionAnalytique() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhObjetsMissionAnalytique(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
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

    public Double getCoefficient() {
        return coefficient != null ? coefficient : 0;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    @XmlTransient  @JsonIgnore
    public YvsComptaCentreAnalytique getCentre() {
        return centre;
    }

    public void setCentre(YvsComptaCentreAnalytique centre) {
        this.centre = centre;
    }

    @XmlTransient  @JsonIgnore
    public YvsGrhObjetsMission getObjetMission() {
        return objetMission;
    }

    public void setObjetMission(YvsGrhObjetsMission objetMission) {
        this.objetMission = objetMission;
    }

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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
        if (!(object instanceof YvsGrhObjetsMissionAnalytique)) {
            return false;
        }
        YvsGrhObjetsMissionAnalytique other = (YvsGrhObjetsMissionAnalytique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.prod.YvsGrhObjetsMissionAnalytique[ id=" + id + " ]";
    }

}
