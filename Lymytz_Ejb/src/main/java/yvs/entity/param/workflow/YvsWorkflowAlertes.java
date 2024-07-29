/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_workflow_alertes", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowAlertes.findAll", query = "SELECT y FROM YvsWorkflowAlertes y"),
    @NamedQuery(name = "YvsWorkflowAlertes.findById", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.id = :id"),
    @NamedQuery(name = "YvsWorkflowAlertes.findByNatureAlerte", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.natureAlerte = :natureAlerte"),
    @NamedQuery(name = "YvsWorkflowAlertes.findByIdElement", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.idElement = :idElement"),
    @NamedQuery(name = "YvsWorkflowAlertes.findByDateSave", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsWorkflowAlertes.findByDateUpdate", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsWorkflowAlertes.findByNiveau", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.niveau = :niveau"),
    @NamedQuery(name = "YvsWorkflowAlertes.findByDateDoc", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.dateDoc = :dateDoc"),
    @NamedQuery(name = "YvsWorkflowAlertes.findByExecuteTrigger", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.executeTrigger = :executeTrigger"),
    @NamedQuery(name = "YvsWorkflowAlertes.findByDescription", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.description = :description"),
    @NamedQuery(name = "YvsWorkflowAlertes.findByActif", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.actif = :actif"),
    
    @NamedQuery(name = "YvsWorkflowAlertes.findByElement", query = "SELECT y FROM YvsWorkflowAlertes y WHERE y.idElement = :element AND y.natureAlerte = :nature AND y.modelDoc.titreDoc = :titre")})
public class YvsWorkflowAlertes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "yvs_workflow_alertes_id_seq", name = "yvs_workflow_alertes_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_alertes_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "nature_alerte")
    private String natureAlerte;
    @Column(name = "id_element")
    private Long idElement;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "niveau")
    private Integer niveau;
    @Column(name = "date_doc")
    @Temporal(TemporalType.DATE)
    private Date dateDoc;
    @Size(max = 2147483647)
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "model_doc", referencedColumnName = "id")
    @ManyToOne
    private YvsWorkflowModelDoc modelDoc;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne
    private YvsAgences agence;

    public YvsWorkflowAlertes() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsWorkflowAlertes(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNatureAlerte() {
        return natureAlerte;
    }

    public void setNatureAlerte(String natureAlerte) {
        this.natureAlerte = natureAlerte;
    }

    public Long getIdElement() {
        return idElement;
    }

    public void setIdElement(Long idElement) {
        this.idElement = idElement;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Integer getNiveau() {
        return niveau;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public Date getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsWorkflowModelDoc getModelDoc() {
        return modelDoc;
    }

    public void setModelDoc(YvsWorkflowModelDoc modelDoc) {
        this.modelDoc = modelDoc;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
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
        if (!(object instanceof YvsWorkflowAlertes)) {
            return false;
        }
        YvsWorkflowAlertes other = (YvsWorkflowAlertes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWorkflowAlertes[ id=" + id + " ]";
    }

}
