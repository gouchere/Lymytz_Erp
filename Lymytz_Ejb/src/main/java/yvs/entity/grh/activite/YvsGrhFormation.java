/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.entity.grh.personnel.YvsDiplomes;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.param.workflow.YvsWorkflowValidFormation;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_formation")
@NamedQueries({
    @NamedQuery(name = "YvsFormation.findAll", query = "SELECT y FROM YvsGrhFormation y WHERE Y.societe = :societe"),
    @NamedQuery(name = "YvsFormation.findByIds", query = "SELECT y FROM YvsGrhFormation y WHERE Y.id in :ids"),
    @NamedQuery(name = "YvsFormation.findEnCours", query = "SELECT y FROM YvsGrhFormation y WHERE Y.societe = :societe AND y.dateFin>=:date"),
    @NamedQuery(name = "YvsFormation.findById", query = "SELECT y FROM YvsGrhFormation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsFormation.findByActif", query = "SELECT y FROM YvsGrhFormation y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsFormation.findByDateDebut", query = "SELECT y FROM YvsGrhFormation y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsFormation.findByDateFin", query = "SELECT y FROM YvsGrhFormation y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsFormation.findByReference", query = "SELECT y FROM YvsGrhFormation y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsFormation.findByLibelle", query = "SELECT y FROM YvsGrhFormation y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsFormation.findByDescription", query = "SELECT y FROM YvsGrhFormation y WHERE y.description = :description"),
    @NamedQuery(name = "YvsFormation.findByCoutFormation", query = "SELECT y FROM YvsGrhFormation y WHERE y.coutFormation = :coutFormation")})
public class YvsGrhFormation implements Serializable {

    @OneToMany(mappedBy = "formation")
    private List<YvsWorkflowValidFormation> etapesValidations;

    @JoinColumn(name = "formateur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhFormateur formateur;
    @JoinColumn(name = "lieu_defaut", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire lieuDefaut;
    @OneToMany(mappedBy = "formation")
    private List<YvsGrhQualificationFormation> qualifications;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_formation_id_seq1", name = "yvs_formation_id_seq1_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_formation_id_seq1_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cout_formation")
    private Double coutFormation;
    @OneToMany(mappedBy = "formation")
    private List<YvsGrhFormationEmps> employes;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "diplome", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDiplomes diplome;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Column(name = "statut_formation")
    private Character statutFormation;
    
    @Transient
    private String maDateSave;

    public YvsGrhFormation() {
    }

    public YvsGrhFormation(Long id) {
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

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCoutFormation() {
        return coutFormation;
    }

    public void setCoutFormation(Double coutFormation) {
        this.coutFormation = coutFormation;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsGrhFormationEmps> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsGrhFormationEmps> employes) {
        this.employes = employes;
    }

    @XmlTransient  @JsonIgnore
    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    @XmlTransient  @JsonIgnore
    public YvsDiplomes getDiplome() {
        return diplome;
    }

    public void setDiplome(YvsDiplomes diplome) {
        this.diplome = diplome;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Character getStatutFormation() {
        return (statutFormation != null) ? statutFormation : 'E';
    }

    public void setStatutFormation(Character statutFormation) {
        this.statutFormation = statutFormation;
    }

    public String getMaDateSave() {
        return getDateSave() != null ? dfs.format(getDateSave()) : "";
    }

    public void setMaDateSave(String maDateSave) {
        this.maDateSave = maDateSave;
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
        if (!(object instanceof YvsGrhFormation)) {
            return false;
        }
        YvsGrhFormation other = (YvsGrhFormation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsFormation[ id=" + id + " ]";
    }

    public List<YvsGrhQualificationFormation> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<YvsGrhQualificationFormation> qualifications) {
        this.qualifications = qualifications;
    }

    public YvsDictionnaire getLieuDefaut() {
        return lieuDefaut;
    }

    public void setLieuDefaut(YvsDictionnaire lieuDefaut) {
        this.lieuDefaut = lieuDefaut;
    }

    public YvsGrhFormateur getFormateur() {
        return formateur;
    }

    public void setFormateur(YvsGrhFormateur formateur) {
        this.formateur = formateur;
    }

    public List<YvsWorkflowValidFormation> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidFormation> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

}
