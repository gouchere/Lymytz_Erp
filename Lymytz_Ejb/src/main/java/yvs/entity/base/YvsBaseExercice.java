/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.XmlTransient;
import yvs.dao.YvsEntity;
import yvs.entity.mutuelle.YvsMutPeriodeExercice;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_exercice")
@NamedQueries({
    @NamedQuery(name = "YvsBaseExercice.findAll", query = "SELECT y FROM YvsBaseExercice y WHERE y.societe = :societe ORDER BY y.dateDebut DESC, y.dateFin DESC"),
    @NamedQuery(name = "YvsBaseExercice.findById", query = "SELECT y FROM YvsBaseExercice y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseExercice.findActifByDate", query = "SELECT y FROM YvsBaseExercice y WHERE y.actif = true AND y.societe=:societe AND :dateJour BETWEEN y.dateDebut AND y.dateFin"),
    @NamedQuery(name = "YvsBaseExercice.findForLast", query = "SELECT y FROM YvsBaseExercice y WHERE y.societe=:societe ORDER BY y.dateFin"),
    @NamedQuery(name = "YvsBaseExercice.findByReference", query = "SELECT y FROM YvsBaseExercice y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsBaseExercice.findByDateDebut", query = "SELECT y FROM YvsBaseExercice y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsBaseExercice.findByDateFin", query = "SELECT y FROM YvsBaseExercice y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsBaseExercice.findByDate", query = "SELECT y FROM YvsBaseExercice y WHERE y.societe=:societe AND (:date BETWEEN y.dateDebut AND y.dateFin)"),
    @NamedQuery(name = "YvsBaseExercice.findByActifs", query = "SELECT y FROM YvsBaseExercice y WHERE y.societe=:societe AND y.actif=:actif ORDER BY y.dateDebut DESC "),
    @NamedQuery(name = "YvsBaseExercice.findByActif", query = "SELECT y FROM YvsBaseExercice y WHERE y.actif = :actif AND y.societe=:societe AND (:date BETWEEN y.dateDebut AND y.dateFin)"),
    @NamedQuery(name = "YvsBaseExercice.findNext", query = "SELECT y FROM YvsBaseExercice y WHERE y.societe=:societe AND y.dateDebut >= :dateFin ORDER BY y.dateDebut")
})
public class YvsBaseExercice extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_exercice_id_seq", name = "yvs_mut_exercice_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_exercice_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference")
    private String reference;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "cloturer")
    private Boolean cloturer;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "exercice", fetch = FetchType.LAZY)
    private List<YvsMutPeriodeExercice> periodesMutuelles;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private Object valeur;
    @Transient
    private double ca;
    @Transient
    private double avance;
    @Transient
    private double acompte;
    @Transient
    private double creance;
    @Transient
    private double solde;

    public YvsBaseExercice() {
        periodesMutuelles = new ArrayList<>();
    }

    public YvsBaseExercice(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseExercice(Long id, String reference) {
        this(id);
        this.reference = reference;
    }

    public YvsBaseExercice(Long id, String reference, Date dateDebut, Date dateFin, Boolean actif, Boolean cloturer) {
        this(id, reference);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.actif = actif;
        this.cloturer = cloturer;
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

    public double getCa() {
        return ca;
    }

    public void setCa(double ca) {
        this.ca = ca;
    }

    public double getAvance() {
        return avance;
    }

    public void setAvance(double avance) {
        this.avance = avance;
    }

    public double getAcompte() {
        return acompte;
    }

    public void setAcompte(double acompte) {
        this.acompte = acompte;
    }

    public double getCreance() {
        return creance;
    }

    public void setCreance(double creance) {
        this.creance = creance;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Object getValeur() {
        return valeur;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDebut() {
        return dateDebut != null ? dateDebut : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateFin() {
        return dateFin != null ? dateFin : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsMutPeriodeExercice> getPeriodesMutuelles() {
        return periodesMutuelles;
    }

    public void setPeriodesMutuelles(List<YvsMutPeriodeExercice> periodesMutuelles) {
        this.periodesMutuelles = periodesMutuelles;
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
        if (!(object instanceof YvsBaseExercice)) {
            return false;
        }
        YvsBaseExercice other = (YvsBaseExercice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.YvsBaseExercice[ id=" + id + " ]";
    }

}
