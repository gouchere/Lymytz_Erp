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
import javax.validation.constraints.Size;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_mission_poste")
@NamedQueries({
    @NamedQuery(name = "YvsGrhMissionPoste.findAll", query = "SELECT y FROM YvsGrhMissionPoste y"),
    @NamedQuery(name = "YvsGrhMissionPoste.findById", query = "SELECT y FROM YvsGrhMissionPoste y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhMissionPoste.findByLibelle", query = "SELECT y FROM YvsGrhMissionPoste y WHERE y.libelle = :libelle")})
public class YvsGrhMissionPoste implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_mission_poste_id_seq", name = "yvs_grh_mission_poste_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_mission_poste_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    @JoinColumn(name = "poste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPosteDeTravail poste;

    public YvsGrhMissionPoste() {
    }

    public YvsGrhMissionPoste(Long id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public YvsGrhPosteDeTravail getPoste() {
        return poste;
    }

    public void setPoste(YvsGrhPosteDeTravail poste) {
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
        if (!(object instanceof YvsGrhMissionPoste)) {
            return false;
        }
        YvsGrhMissionPoste other = (YvsGrhMissionPoste) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhMissionPoste[ id=" + id + " ]";
    }

}
