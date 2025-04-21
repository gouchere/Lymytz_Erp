/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_base_nature_compte")
@NamedQueries({
    @NamedQuery(name = "YvsBaseNatureCompte.findDesignation", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.societe=:societe AND (y.designation = :designation)"),
    @NamedQuery(name = "YvsBaseNatureCompte.findAll", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.societe=:societe ORDER by y.designation"),
    @NamedQuery(name = "YvsBaseNatureCompte.findAllC", query = "SELECT COUNT(y) FROM YvsBaseNatureCompte y WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsBaseNatureCompte.findByNature", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.societe=:societe AND y.nature = :nature"),
    @NamedQuery(name = "YvsBaseNatureCompte.findById", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseNatureCompte.findByDesignation", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsBaseNatureCompte.findByTypeReport", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.typeReport = :typeReport"),
    @NamedQuery(name = "YvsBaseNatureCompte.findBySaisieAnal", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.saisieAnal = :saisieAnal"),
    @NamedQuery(name = "YvsBaseNatureCompte.findBySaisieEcheance", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.saisieEcheance = :saisieEcheance"),
    @NamedQuery(name = "YvsBaseNatureCompte.findBySaisieCompteTier", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.saisieCompteTier = :saisieCompteTier"),
    @NamedQuery(name = "YvsBaseNatureCompte.findByLettrable", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.lettrable = :lettrable"),
    @NamedQuery(name = "YvsBaseNatureCompte.findByAuthor", query = "SELECT y FROM YvsBaseNatureCompte y WHERE y.author = :author")})
public class YvsBaseNatureCompte extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_nature_compte_id_seq")
    @SequenceGenerator(sequenceName = "yvs_nature_compte_id_seq", allocationSize = 1, name = "yvs_nature_compte_id_seq")
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "type_report")
    private String typeReport;
    @Column(name = "nature")
    private String nature;
    @Column(name = "saisie_anal")
    private Boolean saisieAnal;
    @Column(name = "saisie_echeance")
    private Boolean saisieEcheance;
    @Column(name = "saisie_compte_tier")
    private Boolean saisieCompteTier;
    @Column(name = "lettrable")
    private Boolean lettrable;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "sens_compte")
    private String sensCompte;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @OneToMany(mappedBy = "natureCompte")
    private List<YvsBaseRadicalCompte> radicalsComptes;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsBaseNatureCompte() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        radicalsComptes = new ArrayList<>();
    }

    public YvsBaseNatureCompte(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseNatureCompte(Long id, String designation) {
        this(id);
        this.designation = designation;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNature() {
        return nature != null ? nature.trim().length() > 0 ? nature : "AUTRE" : "AUTRE";
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(String typeReport) {
        this.typeReport = typeReport;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsBaseRadicalCompte> getRadicalsComptes() {
        return radicalsComptes;
    }

    public void setRadicalsComptes(List<YvsBaseRadicalCompte> radicalsComptes) {
        this.radicalsComptes = radicalsComptes;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getSaisieAnal() {
        return saisieAnal != null ? saisieAnal : false;
    }

    public void setSaisieAnal(Boolean saisieAnal) {
        this.saisieAnal = saisieAnal;
    }

    public Boolean getSaisieEcheance() {
        return saisieEcheance != null ? saisieEcheance : false;
    }

    public void setSaisieEcheance(Boolean saisieEcheance) {
        this.saisieEcheance = saisieEcheance;
    }

    public Boolean getSaisieCompteTier() {
        return saisieCompteTier != null ? saisieCompteTier : false;
    }

    public void setSaisieCompteTier(Boolean saisieCompteTier) {
        this.saisieCompteTier = saisieCompteTier;
    }

    public Boolean getLettrable() {
        return lettrable != null ? lettrable : false;
    }

    public void setLettrable(Boolean lettrable) {
        this.lettrable = lettrable;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getSensCompte() {
        return sensCompte;
    }

    public void setSensCompte(String sensCompte) {
        this.sensCompte = sensCompte;
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
        if (!(object instanceof YvsBaseNatureCompte)) {
            return false;
        }
        YvsBaseNatureCompte other = (YvsBaseNatureCompte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsNatureCompte[ id=" + id + " ]";
    }
    }
