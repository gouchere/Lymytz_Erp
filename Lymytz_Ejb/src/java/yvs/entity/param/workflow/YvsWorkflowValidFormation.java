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
import yvs.entity.grh.activite.YvsGrhFormation;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_workflow_valid_formation")
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowValidFormation.findAll", query = "SELECT y FROM YvsWorkflowValidFormation y"),
    @NamedQuery(name = "YvsWorkflowValidFormation.findByEtapeValid", query = "SELECT y FROM YvsWorkflowValidFormation y WHERE y.etapeValid = :etapeValid"),
    @NamedQuery(name = "YvsWorkflowValidFormation.findById", query = "SELECT y FROM YvsWorkflowValidFormation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsWorkflowValidFormation.findByDocument", query = "SELECT y FROM YvsWorkflowValidFormation y WHERE y.formation = :formation ORDER BY y.ordreEtape")
})
public class YvsWorkflowValidFormation implements Serializable, Comparable<YvsWorkflowValidFormation> {

    private static final long serialVersionUID = 1L;
    @Column(name = "etape_valid")
    private Boolean etapeValid;
    @Id
    @SequenceGenerator(sequenceName = "yvs_workflow_valid_formation_id_seq", name = "yvs_workflow_valid_formation_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_valid_formation_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "ordre_etape")
    private Integer ordreEtape;
    @JoinColumn(name = "etape", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsWorkflowEtapeValidation etape;
    @JoinColumn(name = "formation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhFormation formation;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private YvsWorkflowValidFormation etapeSuivante;
    @Transient
    private boolean etapeActive = false;

    public YvsWorkflowValidFormation() {
    }

    public YvsWorkflowValidFormation(Long id) {
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

    public Boolean getEtapeValid() {
        return etapeValid;
    }

    public void setEtapeValid(Boolean etapeValid) {
        this.etapeValid = etapeValid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsWorkflowEtapeValidation getEtape() {
        return etape;
    }

    public void setEtape(YvsWorkflowEtapeValidation etape) {
        this.etape = etape;
    }

    public YvsGrhFormation getFormation() {
        return formation;
    }

    public void setFormation(YvsGrhFormation formation) {
        this.formation = formation;
    }

    public void setEtapeActive(boolean etapeActive) {
        this.etapeActive = etapeActive;
    }

    public boolean isEtapeActive() {
        return etapeActive;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Integer getOrdreEtape() {
        return ordreEtape != null ? ordreEtape : 0;
    }

    public void setOrdreEtape(Integer ordreEtape) {
        this.ordreEtape = ordreEtape;
    }

    public YvsWorkflowValidFormation getEtapeSuivante() {
        return etapeSuivante;
    }

    public void setEtapeSuivante(YvsWorkflowValidFormation etapeSuivante) {
        this.etapeSuivante = etapeSuivante;
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
        if (!(object instanceof YvsWorkflowValidFormation)) {
            return false;
        }
        YvsWorkflowValidFormation other = (YvsWorkflowValidFormation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWorkflowValidFormation[ id=" + id + " ]";
    }

    @Override
    public int compareTo(YvsWorkflowValidFormation o) {
        if (getOrdreEtape().equals(o.getOrdreEtape())) {
            return getId().compareTo(o.getId());
        }
        return getOrdreEtape().compareTo(o.getOrdreEtape());
    }

    public static List<YvsWorkflowValidFormation> ordonneEtapes(List<YvsWorkflowValidFormation> list) {
        if (list != null ? !list.isEmpty() : false) {
            Collections.sort(list);
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    list.get(i).setEtapeSuivante(list.get(i + 1));
                }
            }
            int i = 0;
            //toutes les étapes ont été construite
            YvsWorkflowValidFormation first = list.get(0);
            //si la première étape n'est pas validé, on l'active
            if (!first.getEtapeValid()) {
                list.get(0).setEtapeActive(true);
            }
            for (YvsWorkflowValidFormation vm : list) {
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
