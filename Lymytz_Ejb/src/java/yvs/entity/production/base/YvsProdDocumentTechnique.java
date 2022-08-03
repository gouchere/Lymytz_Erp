/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

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
import javax.validation.constraints.Size;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_document_technique")
@NamedQueries({
    @NamedQuery(name = "YvsProdDocumentTechnique.findAll", query = "SELECT y FROM YvsProdDocumentTechnique y"),
    @NamedQuery(name = "YvsProdDocumentTechnique.findById", query = "SELECT y FROM YvsProdDocumentTechnique y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdDocumentTechnique.findByReference", query = "SELECT y FROM YvsProdDocumentTechnique y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsProdDocumentTechnique.findByDesignation", query = "SELECT y FROM YvsProdDocumentTechnique y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsProdDocumentTechnique.findByDescription", query = "SELECT y FROM YvsProdDocumentTechnique y WHERE y.description = :description"),
    @NamedQuery(name = "YvsProdDocumentTechnique.findByFichier", query = "SELECT y FROM YvsProdDocumentTechnique y WHERE y.fichier = :fichier")})
public class YvsProdDocumentTechnique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_document_technique_id_seq", name = "yvs_prod_document_technique_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_document_technique_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "fichier")
    private String fichier;
    @JoinColumn(name = "phase_gamme", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdOperationsGamme phaseGamme;
    @Transient
    private boolean selectActif;

    public YvsProdDocumentTechnique() {
    }

    public YvsProdDocumentTechnique(Integer id) {
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
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public YvsProdOperationsGamme getPhaseGamme() {
        return phaseGamme;
    }

    public void setPhaseGamme(YvsProdOperationsGamme phaseGamme) {
        this.phaseGamme = phaseGamme;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
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
        if (!(object instanceof YvsProdDocumentTechnique)) {
            return false;
        }
        YvsProdDocumentTechnique other = (YvsProdDocumentTechnique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdDocumentTechnique[ id=" + id + " ]";
    }

}
