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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LENOVO
 */
@Entity
@Table(name = "yvs_grh_frais_mission")
@NamedQueries({
    @NamedQuery(name = "YvsGrhFraisMission.findAll", query = "SELECT y FROM YvsGrhFraisMission y"),
    @NamedQuery(name = "YvsGrhFraisMission.findById", query = "SELECT y FROM YvsGrhFraisMission y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhFraisMission.findSumByMission", query = "SELECT SUM(y.montant) FROM YvsGrhFraisMission y WHERE y.mission = :mission"),
    @NamedQuery(name = "YvsGrhFraisMission.findByMontant", query = "SELECT y FROM YvsGrhFraisMission y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsGrhFraisMission.findByMission", query = "SELECT y FROM YvsGrhFraisMission y JOIN FETCH y.typeCout WHERE y.mission = :mission"),
    @NamedQuery(name = "YvsGrhFraisMission.sumBySociete", query = "SELECT SUM(y.montant) FROM YvsGrhFraisMission y WHERE y.mission.employe.agence.societe = :societe AND y.mission.dateDebut BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhFraisMission.sumByAgence", query = "SELECT SUM(y.montant) FROM YvsGrhFraisMission y WHERE y.mission.employe.agence = :agence AND y.mission.dateDebut BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhFraisMission.sumByEmploye", query = "SELECT SUM(y.montant) FROM YvsGrhFraisMission y WHERE y.mission.employe = :employe AND y.mission.dateDebut BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhFraisMission.sumByMission", query = "SELECT SUM(y.montant) FROM YvsGrhFraisMission y WHERE y.mission = :mission")})
public class YvsGrhFraisMission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_frais_mission_id_seq", name = "yvs_grh_frais_mission_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_frais_mission_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "montant")
    private double montant;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "type_cout", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout typeCout;
    @JoinColumn(name = "mission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhMissions mission;
    @Column(name = "note")
    private String note;
    @Transient
    private boolean proportionelDuree;

    public YvsGrhFraisMission() {
    }

    public YvsGrhFraisMission(Long id) {
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

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

  
    public YvsGrhTypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(YvsGrhTypeCout typeCout) {
        this.typeCout = typeCout;
    }

    @XmlTransient  @JsonIgnore
    public YvsGrhMissions getMission() {
        return mission;
    }

    public void setMission(YvsGrhMissions mission) {
        this.mission = mission;
    }

    public boolean isProportionelDuree() {
        return proportionelDuree;
    }

    public void setProportionelDuree(boolean proportionelDuree) {
        this.proportionelDuree = proportionelDuree;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean onComment() {
        return (note != null) ? note.trim().length() > 1 : false;
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
        if (!(object instanceof YvsGrhFraisMission)) {
            return false;
        }
        YvsGrhFraisMission other = (YvsGrhFraisMission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsGrhFraisMission[ id=" + id + " ]";
    }

}
