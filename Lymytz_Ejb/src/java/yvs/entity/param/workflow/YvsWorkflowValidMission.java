/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.Transient;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_workflow_valid_mission")
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowValidMission.findAll", query = "SELECT y FROM YvsWorkflowValidMission y"),
    @NamedQuery(name = "YvsWorkflowValidMission.findById", query = "SELECT y FROM YvsWorkflowValidMission y WHERE y.id = :id"),
    @NamedQuery(name = "YvsWorkflowValidMission.findByMission", query = "SELECT y FROM YvsWorkflowValidMission y JOIN FETCH y.etape WHERE y.mission = :mission ORDER BY y.ordreEtape"),
    @NamedQuery(name = "YvsWorkflowValidMission.findByEtapeMission", query = "SELECT y FROM YvsWorkflowValidMission y JOIN FETCH y.etape WHERE y.mission = :mission AND y.etape = :etape"),
    @NamedQuery(name = "YvsWorkflowValidMission.findByAuthor", query = "SELECT y FROM YvsWorkflowValidMission y WHERE y.author = :author"),
    @NamedQuery(name = "YvsWorkflowValidMission.findByEtapeValid", query = "SELECT y FROM YvsWorkflowValidMission y WHERE y.etapeValid = :etapeValid")})
public class YvsWorkflowValidMission implements Serializable, Comparable<YvsWorkflowValidMission> {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "yvs_workflow_valid_mission_id_seq", name = "yvs_workflow_valid_mission_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_valid_mission_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "motif")
    private String motif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "etape_valid")
    private Boolean etapeValid;
    @Column(name = "ordre_etape")
    private Integer ordreEtape;
    
    @JoinColumn(name = "etape", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsWorkflowEtapeValidation etape;
    @JoinColumn(name = "mission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhMissions mission;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private YvsWorkflowValidMission etapeSuivante;    
    @Transient
    private boolean etapeActive = false;

    public YvsWorkflowValidMission() {
    }

    public YvsWorkflowValidMission(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
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

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public Boolean getEtapeValid() {
        return etapeValid!=null?etapeValid:false;
    }

    public void setEtapeValid(Boolean etapeValid) {
        this.etapeValid = etapeValid;
    }

    public YvsWorkflowEtapeValidation getEtape() {
        return etape;
    }

    public void setEtape(YvsWorkflowEtapeValidation etape) {
        this.etape = etape;
    }

    public YvsGrhMissions getMission() {
        return mission;
    }

    public void setMission(YvsGrhMissions mission) {
        this.mission = mission;
    }

    public boolean isEtapeActive() {
        return etapeActive;
    }

    public void setEtapeActive(boolean etapeActive) {
        this.etapeActive = etapeActive;
    }

    public Integer getOrdreEtape() {
        return ordreEtape != null ? ordreEtape : 0;
    }

    public void setOrdreEtape(Integer ordreEtape) {
        this.ordreEtape = ordreEtape;
    }

    public YvsWorkflowValidMission getEtapeSuivante() {
        return etapeSuivante;
    }

    public void setEtapeSuivante(YvsWorkflowValidMission etapeSuivante) {
        this.etapeSuivante = etapeSuivante;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsWorkflowValidMission other = (YvsWorkflowValidMission) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWorkflowValidMission[ id=" + id + " ]";
    }

    @Override
    public int compareTo(YvsWorkflowValidMission o) {
        if (getOrdreEtape().equals(o.getOrdreEtape())) {
            return getId().compareTo(o.getId());
        }
        return getOrdreEtape().compareTo(o.getOrdreEtape());
    }

    public static List<YvsWorkflowValidMission> ordonneEtapes(List<YvsWorkflowValidMission> list) {
        if (list != null ? !list.isEmpty() : false) {
            Collections.sort(list);
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    list.get(i).setEtapeSuivante(list.get(i + 1));
                }
            }
            int i = 0;
            //toutes les étapes ont été construite
            YvsWorkflowValidMission first = list.get(0);
            //si la première étape n'est pas validé, on l'active
            if (!first.getEtapeValid()) {
                list.get(0).setEtapeActive(true);
            }
            for (YvsWorkflowValidMission vm : list) {
                if (first.getEtapeSuivante() != null && vm.getEtapeValid()) {
                    i++;
                    if (list.size() > i) {
                        //active l'etape suivante
                        list.get(i).setEtapeActive(true);
                        first = list.get(i);
                    }
                }
                if (vm.getEtape().equals(list.get(list.size() - 1).getEtape()) && !vm.getEtapeValid()) {
                    if ((list.size() - 2) >= 0) {
                        if (list.get(list.size() - 2).getEtapeValid()) {
                            vm.setEtapeActive(true);
                        }
                    }
                }
            }
        }
        return list;
    }
}
