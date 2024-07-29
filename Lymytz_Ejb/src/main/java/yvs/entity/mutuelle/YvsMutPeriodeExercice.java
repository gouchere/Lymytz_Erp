/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle;

import yvs.entity.base.YvsBaseExercice;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter; 
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_mut_periode_exercice")
@NamedQueries({
    @NamedQuery(name = "YvsMutPeriodeExercice.findAll", query = "SELECT y FROM YvsMutPeriodeExercice y"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findBySociete", query = "SELECT y FROM YvsMutPeriodeExercice y WHERE y.exercice.societe=:societe ORDER BY y.dateDebut"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findById", query = "SELECT y FROM YvsMutPeriodeExercice y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findByExercice", query = "SELECT y FROM YvsMutPeriodeExercice y WHERE y.exercice = :exercice ORDER BY y.dateDebut"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findPeriode", query = "SELECT y FROM YvsMutPeriodeExercice y WHERE y.exercice = :exo AND :date BETWEEN y.dateDebut AND y.dateFin"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findPeriode_", query = "SELECT y FROM YvsMutPeriodeExercice y WHERE y.exercice.societe=:societe AND :date BETWEEN y.dateDebut AND y.dateFin"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findEtatPeriode", query = "SELECT y.cloture FROM YvsMutPeriodeExercice y WHERE y.exercice.societe = :societe AND :dates BETWEEN y.dateDebut AND y.dateFin"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findPeriodeSuivant", query = "SELECT y FROM YvsMutPeriodeExercice y WHERE y.exercice.societe=:societe AND y.dateDebut >= :date"),
    @NamedQuery(name = "YvsMutPeriodeExercice.countPeriodeSuivant", query = "SELECT COUNT(y) FROM YvsMutPeriodeExercice y WHERE y.exercice.societe=:societe AND y.dateFin >= :date"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findPeriodePrecedent", query = "SELECT y FROM YvsMutPeriodeExercice y WHERE y.exercice.societe=:societe AND y.dateDebut < :date"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findByDateDebut", query = "SELECT y FROM YvsMutPeriodeExercice y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findByDateFin", query = "SELECT y FROM YvsMutPeriodeExercice y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findActifByDate", query = "SELECT y FROM YvsMutPeriodeExercice y JOIN FETCH y.exercice WHERE y.actif = true AND y.exercice.societe=:societe AND :dateJour BETWEEN y.dateDebut AND y.dateFin"),
    @NamedQuery(name = "YvsMutPeriodeExercice.findByCloture", query = "SELECT y FROM YvsMutPeriodeExercice y WHERE y.cloture = :cloture")})
public class YvsMutPeriodeExercice implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_periode_exercice_id_seq", name = "yvs_mut_periode_exercice_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_periode_exercice_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "cloture")
    private Boolean cloture;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "reference_periode")
    private String referencePeriode;
    @JoinColumn(name = "exercice", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseExercice exercice;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @Transient
    private double epargneTotal;
    @Transient
    private double interetTotal;
    @Transient
    private double montantTotal;
    @Transient
    private double amortissementTotal;
    @Transient
    private double penaliteTotal;
    @Transient
    private double primeTotal;

    public YvsMutPeriodeExercice() {
    }

    public YvsMutPeriodeExercice(Long id) {
        this.id = id;
    }

    public YvsMutPeriodeExercice(Long id, String referencePeriode, Date dateDebut, Date dateFin, Boolean cloture, YvsBaseExercice exercice) {
        this(id);
        this.referencePeriode = referencePeriode;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.cloture = cloture;
        this.exercice = exercice;
    }

    public YvsMutPeriodeExercice(Long id, String referencePeriode, Date dateDebut, Date dateFin, Boolean actif, Boolean cloture, YvsBaseExercice exercice) {
        this(id, referencePeriode, dateDebut, dateFin, cloture, exercice);
        this.actif = actif;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDebut() {
        return dateDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateFin() {
        return dateFin;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getCloture() {
        return cloture != null ? cloture : false;
    }

    public void setCloture(Boolean cloture) {
        this.cloture = cloture;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBaseExercice getExercice() {
        return exercice;
    }

    public void setExercice(YvsBaseExercice exercice) {
        this.exercice = exercice;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getReferencePeriode() {
        if (referencePeriode == null && dateFin != null) {
            referencePeriode = Constantes.dfML.format(dateFin);
        }
        return referencePeriode != null ? referencePeriode : "";
    }

    public void setReferencePeriode(String referencePeriode) {
        this.referencePeriode = referencePeriode;
    }

    public double getEpargneTotal() {
        return epargneTotal;
    }

    public void setEpargneTotal(double epargneTotal) {
        this.epargneTotal = epargneTotal;
    }

    public double getInteretTotal() {
        return interetTotal;
    }

    public void setInteretTotal(double interetTotal) {
        this.interetTotal = interetTotal;
    }

    public double getPrimeTotal() {
        return primeTotal;
    }

    public void setPrimeTotal(double primeTotal) {
        this.primeTotal = primeTotal;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getAmortissementTotal() {
        return amortissementTotal;
    }

    public void setAmortissementTotal(double amortissementTotal) {
        this.amortissementTotal = amortissementTotal;
    }

    public double getPenaliteTotal() {
        return penaliteTotal;
    }

    public void setPenaliteTotal(double penaliteTotal) {
        this.penaliteTotal = penaliteTotal;
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
        if (!(object instanceof YvsMutPeriodeExercice)) {
            return false;
        }
        YvsMutPeriodeExercice other = (YvsMutPeriodeExercice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.YvsMutPeriodeExercice[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        YvsMutPeriodeExercice y = (YvsMutPeriodeExercice) o;
        if (y.getDateFin().equals(dateFin)) {
            if (y.getDateDebut().equals(dateDebut)) {
                return y.getId().compareTo(id);
            }
            return y.getDateDebut().compareTo(dateDebut);
        }
        return y.getDateFin().compareTo(dateFin);
    }

}
