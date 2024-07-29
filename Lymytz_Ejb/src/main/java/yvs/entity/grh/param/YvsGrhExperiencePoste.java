/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
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

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_experience_poste")
@NamedQueries({
    @NamedQuery(name = "YvsExperiencePoste.findAll", query = "SELECT y FROM YvsGrhExperiencePoste y"),
    @NamedQuery(name = "YvsExperiencePoste.findById", query = "SELECT y FROM YvsGrhExperiencePoste y WHERE y.id = :id"),
    @NamedQuery(name = "YvsExperiencePoste.findByPoste", query = "SELECT y FROM YvsGrhExperiencePoste y WHERE y.posteTravail.id = :poste"),
    @NamedQuery(name = "YvsExperiencePoste.findByDuree", query = "SELECT y FROM YvsGrhExperiencePoste y WHERE y.duree = :duree")})
public class YvsGrhExperiencePoste implements Serializable {

    @Column(name = "supp")
    private Boolean supp;
    @JoinColumn(name = "poste_travail", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPosteDeTravail posteTravail;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_experience_poste_id_seq")
    @SequenceGenerator(sequenceName = "yvs_experience_poste_id_seq", allocationSize = 1, name = "yvs_experience_poste_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "duree")
    private Integer duree;
    private String poste;

    public YvsGrhExperiencePoste() {
    }

    public YvsGrhExperiencePoste(Long id) {
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
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
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
        if (!(object instanceof YvsGrhExperiencePoste)) {
            return false;
        }
        YvsGrhExperiencePoste other = (YvsGrhExperiencePoste) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsExperiencePoste[ id=" + id + " ]";
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public YvsGrhPosteDeTravail getPosteTravail() {
        return posteTravail;
    }

    public void setPosteTravail(YvsGrhPosteDeTravail posteTravail) {
        this.posteTravail = posteTravail;
    }

}
