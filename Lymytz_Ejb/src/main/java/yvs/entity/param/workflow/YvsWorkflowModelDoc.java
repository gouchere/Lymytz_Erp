/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.stat.dashboard.YvsWorkflowEtatsSignatures;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_workflow_model_doc")
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowModelDoc.findAll", query = "SELECT y FROM YvsWorkflowModelDoc y ORDER BY y.titreDoc"),
    @NamedQuery(name = "YvsWorkflowModelDoc.findAllW", query = "SELECT y FROM YvsWorkflowModelDoc y WHERE y.nature='W' ORDER BY y.titreDoc"),
    @NamedQuery(name = "YvsWorkflowModelDoc.findAllA", query = "SELECT y FROM YvsWorkflowModelDoc y WHERE y.nature='A' ORDER BY y.titreDoc"),
    @NamedQuery(name = "YvsWorkflowModelDoc.findAllI", query = "SELECT y FROM YvsWorkflowModelDoc y WHERE y.nature='I' ORDER BY y.titreDoc"),
    @NamedQuery(name = "YvsWorkflowModelDoc.findById", query = "SELECT y FROM YvsWorkflowModelDoc y WHERE y.id = :id"),
    @NamedQuery(name = "YvsWorkflowModelDoc.findByTitreDoc", query = "SELECT y FROM YvsWorkflowModelDoc y WHERE y.titreDoc = :titre ORDER BY y.titreDoc"),
    @NamedQuery(name = "YvsWorkflowModelDoc.findByNameTable", query = "SELECT y FROM YvsWorkflowModelDoc y WHERE y.nameTable = :nameTable ORDER BY y.titreDoc"),
    @NamedQuery(name = "YvsWorkflowModelDoc.findByNature", query = "SELECT y FROM YvsWorkflowModelDoc y WHERE y.nature = :nature ORDER BY y.titreDoc"),
    @NamedQuery(name = "YvsWorkflowModelDoc.findByNatures", query = "SELECT y FROM YvsWorkflowModelDoc y WHERE y.nature IN :natures ORDER BY y.titreDoc")})
public class YvsWorkflowModelDoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_workflow_model_doc_id_seq", name = "yvs_workflow_model_doc_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_model_doc_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "titre_doc")
    private String titreDoc;
    @Size(max = 2147483647)
    @Column(name = "name_table")
    private String nameTable;
    @Size(max = 2147483647)
    @Column(name = "nature")
    private String nature = "W";
    @Column(name = "defined_livraison")
    private Boolean definedLivraison;
    @Column(name = "defined_reglement")
    private Boolean definedReglement;
    @Column(name = "defined_update")
    private Boolean definedUpdate;
    @Column(name = "workflow")
    private Boolean workflow;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    private List<YvsWarningModelDoc> yvsWarningModelDocList;
    @OneToMany(mappedBy = "documentModel", fetch = FetchType.LAZY)
    private List<YvsWorkflowEtapeValidation> etapesValidations;

    @OneToOne(mappedBy = "modelDoc")
    private YvsWorkflowEtatsSignatures signatures;

    public YvsWorkflowModelDoc() {
    }

    public YvsWorkflowModelDoc(Integer id) {
        this.id = id;
    }

    public YvsWorkflowModelDoc(Integer id, String titreDoc, String nameTable) {
        this.id = id;
        this.titreDoc = titreDoc;
        this.nameTable = nameTable;
    }

    public Boolean getDefinedLivraison() {
        return definedLivraison != null ? definedLivraison : false;
    }

    public void setDefinedLivraison(Boolean definedLivraison) {
        this.definedLivraison = definedLivraison;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitreDoc() {
        return titreDoc;
    }

    public void setTitreDoc(String titreDoc) {
        this.titreDoc = titreDoc;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public Boolean getWorkflow() {
        return workflow != null ? workflow : false;
    }

    public void setWorkflow(Boolean workflow) {
        this.workflow = workflow;
    }

    public String getNature() {
        return nature != null ? nature.trim().length() > 0 ? nature : "W" : "W";
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsWorkflowEtapeValidation> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowEtapeValidation> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getDefinedUpdate() {
        return definedUpdate != null ? definedUpdate : false;
    }

    public void setDefinedUpdate(Boolean definedUpdate) {
        this.definedUpdate = definedUpdate;
    }

    public Boolean getDefinedReglement() {
        return definedReglement != null ? definedReglement : false;
    }

    public void setDefinedReglement(Boolean definedReglement) {
        this.definedReglement = definedReglement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<YvsWarningModelDoc> getYvsWarningModelDocList() {
        return yvsWarningModelDocList;
    }

    public void setYvsWarningModelDocList(List<YvsWarningModelDoc> yvsWarningModelDocList) {
        this.yvsWarningModelDocList = yvsWarningModelDocList;
    }

    public YvsWorkflowEtatsSignatures getSignatures() {
        return signatures;
    }

    public void setSignatures(YvsWorkflowEtatsSignatures signatures) {
        this.signatures = signatures;
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
        if (!(object instanceof YvsWorkflowModelDoc)) {
            return false;
        }
        YvsWorkflowModelDoc other = (YvsWorkflowModelDoc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWorkflowModelDoc[ id=" + id + " ]";
    }

}
