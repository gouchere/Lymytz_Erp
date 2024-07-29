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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.commercial.stock.YvsComNatureDoc;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_workflow_etape_validation")
@NamedQueries({ 
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findAll", query = "SELECT y FROM YvsWorkflowEtapeValidation y"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findById", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModelDoc", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel = :model AND y.societe=:societe ORDER BY y.firstEtape DESC"),  
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModel", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel.titreDoc = :titre AND y.societe=:societe ORDER BY y.typeDocDivers.libelle, y.nature, y.firstEtape DESC"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModelActif", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel.titreDoc = :titre AND y.societe=:societe AND y.actif=true ORDER BY y.nature, y.firstEtape DESC, y.ordreEtape"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByTitreModel", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.actif=true AND y.documentModel.titreDoc = :titre AND y.societe=:societe AND y.typeDocDivers IS NULL ORDER BY y.ordreEtape ASC"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByTitreModelActif", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.actif=true AND y.documentModel.titreDoc = :titre AND y.societe=:societe AND y.typeDocDivers IS NULL ORDER BY y.ordreEtape ASC"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModelNature", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel.titreDoc = :titre AND y.nature = :nature AND y.societe=:societe AND y.typeDocDivers IS NULL ORDER BY y.ordreEtape ASC"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModelNatureActif", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel.titreDoc = :titre AND y.nature = :nature AND y.societe=:societe AND y.typeDocDivers IS NULL AND y.actif=true ORDER BY y.ordreEtape ASC"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModelType", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel.titreDoc = :titre AND y.typeDocDivers=:typeDoc AND y.societe=:societe ORDER BY y.ordreEtape ASC"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModelTypeActif", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel.titreDoc = :titre AND y.typeDocDivers=:typeDoc AND y.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModelNatureDocSortie", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel.titreDoc = :titre AND y.natureDoc = :nature AND y.societe=:societe AND y.actif=true ORDER BY y.ordreEtape ASC"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModelNatureDocSortieAll", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel.titreDoc = :titre AND y.natureDoc = :nature AND y.societe=:societe ORDER BY y.ordreEtape ASC"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModelNatureAndType", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel.titreDoc = :titre AND y.nature = :nature AND y.typeDocDivers=:typeDoc AND y.societe=:societe ORDER BY y.ordreEtape ASC"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByModelNatureAndTypeActif", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.documentModel.titreDoc = :titre AND y.nature = :nature AND y.typeDocDivers=:typeDoc AND y.societe=:societe AND y.actif=true ORDER BY y.ordreEtape ASC"),
    @NamedQuery(name = "YvsWorkflowEtapeValidation.findByLabelStatut", query = "SELECT y FROM YvsWorkflowEtapeValidation y WHERE y.labelStatut = :labelStatut")})
public class YvsWorkflowEtapeValidation extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_workflow_etape_validation_id_seq", name = "yvs_workflow_etape_validation_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_etape_validation_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "label_statut")
    private String labelStatut;
    @Column(name = "titre_etape")
    private String titreEtape;
    @Column(name = "nature")
    private String nature;
    @Column(name = "first_etape")
    private Boolean firstEtape;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "print_here")
    private Boolean printHere;
    @Column(name = "reglement_here")
    private Boolean reglementHere;
    @Column(name = "livraison_here")
    private Boolean livraisonHere;
    @Column(name = "can_update_here")
    private Boolean canUpdateHere;
    @Column(name = "can_edit_bp_here")
    private Boolean canEditBpHere;      //Utile pour les mission, et précise les étapes où il est permi de générer le bon provisoire
    @Column(name = "ordre_etape")
    private Integer ordreEtape;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "type_doc_divers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTypeDocDivers typeDocDivers;
    @JoinColumn(name = "nature_doc", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComNatureDoc natureDoc;

    @JoinColumn(name = "document_model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsWorkflowModelDoc documentModel;
    @JoinColumn(name = "etape_suivante", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsWorkflowEtapeValidation etapeSuivante;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "etapeValide")
    private List<YvsWorkflowAutorisationValidDoc> autorisations;

    public YvsWorkflowEtapeValidation() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsWorkflowEtapeValidation(Long id) {
        this();
        this.id = id;
    }

    public YvsWorkflowEtapeValidation(Long id, String labelStatut, YvsWorkflowEtapeValidation etapeSuivante) {
        this(id);
        this.labelStatut = labelStatut;
        this.etapeSuivante = etapeSuivante;
    }

    public YvsWorkflowEtapeValidation(Long id, String labelStatut, YvsWorkflowEtapeValidation etapeSuivante, boolean firstEtape) {
        this(id);
        this.labelStatut = labelStatut;
        this.etapeSuivante = etapeSuivante;
        this.firstEtape = firstEtape;
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

    public String getNature() {
        return nature != null ? nature.trim().length() > 0 ? nature : "DONS" : "DONS";
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLivraisonHere() {
        return livraisonHere != null ? livraisonHere : false;
    }

    public void setLivraisonHere(Boolean livraisonHere) {
        this.livraisonHere = livraisonHere;
    }

    public Boolean getReglementHere() {
        return reglementHere != null ? reglementHere : false;
    }

    public void setReglementHere(Boolean reglementHere) {
        this.reglementHere = reglementHere;
    }

    public String getLabelStatut() {
        return labelStatut;
    }

    public void setLabelStatut(String labelStatut) {
        this.labelStatut = labelStatut;
    }

    @XmlTransient
    public List<YvsWorkflowAutorisationValidDoc> getAutorisations() {
        return autorisations;
    }

    public void setAutorisations(List<YvsWorkflowAutorisationValidDoc> autorisations) {
        this.autorisations = autorisations;
    }

    public YvsWorkflowModelDoc getDocumentModel() {
        return documentModel;
    }

    public void setDocumentModel(YvsWorkflowModelDoc documentModel) {
        this.documentModel = documentModel;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getFirstEtape() {
        return (firstEtape != null) ? firstEtape : false;
    }

    public void setFirstEtape(Boolean firstEtape) {
        this.firstEtape = firstEtape;
    }

    public String getTitreEtape() {
        return titreEtape;
    }

    public void setTitreEtape(String titreEtape) {
        this.titreEtape = titreEtape;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Boolean getPrintHere() {
        return printHere != null ? printHere : false;
    }

    public void setPrintHere(Boolean printHere) {
        this.printHere = printHere;
    }

    public Boolean getCanUpdateHere() {
        return canUpdateHere != null ? canUpdateHere : false;
    }

    public void setCanUpdateHere(Boolean canUpdateHere) {
        this.canUpdateHere = canUpdateHere;
    }

    @XmlTransient
    public YvsWorkflowEtapeValidation getEtapeSuivante() {
        return etapeSuivante;
    }

    public void setEtapeSuivante(YvsWorkflowEtapeValidation etapeSuivante) {
        this.etapeSuivante = etapeSuivante;
    }

    public Integer getOrdreEtape() {
        return ordreEtape;
    }

    public void setOrdreEtape(Integer ordreEtape) {
        this.ordreEtape = ordreEtape;
    }

    public Boolean getCanEditBpHere() {
        return canEditBpHere != null ? canEditBpHere : false;
    }

    public void setCanEditBpHere(Boolean canEditBpHere) {
        this.canEditBpHere = canEditBpHere;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsBaseTypeDocDivers getTypeDocDivers() {
        return typeDocDivers;
    }

    public void setTypeDocDivers(YvsBaseTypeDocDivers typeDocDivers) {
        this.typeDocDivers = typeDocDivers;
    }

    public YvsComNatureDoc getNatureDoc() {
        return natureDoc;
    }

    public void setNatureDoc(YvsComNatureDoc natureDoc) {
        this.natureDoc = natureDoc;
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
        if (!(object instanceof YvsWorkflowEtapeValidation)) {
            return false;
        }
        YvsWorkflowEtapeValidation other = (YvsWorkflowEtapeValidation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWorkflowEtapeValidation[ id=" + id + " ]";
    }

}
