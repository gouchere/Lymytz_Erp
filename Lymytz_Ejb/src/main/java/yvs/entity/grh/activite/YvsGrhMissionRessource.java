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
import yvs.entity.produits.YvsBaseArticles;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_mission_ressource")
@NamedQueries({
    @NamedQuery(name = "YvsGrhMissionRessource.findAll", query = "SELECT y FROM YvsGrhMissionRessource y"),
    @NamedQuery(name = "YvsGrhMissionRessource.findById", query = "SELECT y FROM YvsGrhMissionRessource y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhMissionRessource.findByMission", query = "SELECT y FROM YvsGrhMissionRessource y WHERE y.mission.id = :id"),
    @NamedQuery(name = "YvsGrhMissionRessource.findByRessource", query = "SELECT y FROM YvsGrhMissionRessource y WHERE y.ressource.groupe.societe = :societe"),
    @NamedQuery(name = "YvsGrhMissionRessource.findByQuantite", query = "SELECT y FROM YvsGrhMissionRessource y WHERE y.quantite = :quantite")})
public class YvsGrhMissionRessource implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_mission_ressource_id_seq", name = "yvs_grh_mission_ressource_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_mission_ressource_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "quantite")
    private Integer quantite;
    @JoinColumn(name = "mission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhMissions mission;
    @JoinColumn(name = "ressource", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles ressource;

    public YvsGrhMissionRessource() {
    }

    public YvsGrhMissionRessource(Long id) {
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

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public YvsGrhMissions getMission() {
        return mission;
    }

    public void setMission(YvsGrhMissions mission) {
        this.mission = mission;
    }

    public YvsBaseArticles getRessource() {
        return ressource;
    }

    public void setRessource(YvsBaseArticles ressource) {
        this.ressource = ressource;
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
        if (!(object instanceof YvsGrhMissionRessource)) {
            return false;
        }
        YvsGrhMissionRessource other = (YvsGrhMissionRessource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsGrhMissionRessource[ id=" + id + " ]";
    }

}
