/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.mutuelle.salaire.YvsMutInteret;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_exercice")
@NamedQueries({
    @NamedQuery(name = "YvsMutExercice.findAll", query = "SELECT y FROM YvsMutExercice y WHERE y.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutExercice.findById", query = "SELECT y FROM YvsMutExercice y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutExercice.findByMutuelle", query = "SELECT y FROM YvsMutExercice y WHERE y.mutuelle = :mutuelle ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsMutExercice.findByMutuelleActif", query = "SELECT y FROM YvsMutExercice y WHERE y.mutuelle = :mutuelle AND y.actif = :actif"),
    @NamedQuery(name = "YvsMutExercice.findByMutuelleDate", query = "SELECT y FROM YvsMutExercice y WHERE y.actif = true AND y.mutuelle = :mutuelle AND :dateJour BETWEEN y.dateDebut AND y.dateFin"),
    @NamedQuery(name = "YvsMutExercice.findByLastMutuelle", query = "SELECT y FROM YvsMutExercice y WHERE y.mutuelle = :mutuelle ORDER BY y.dateFin"),
    @NamedQuery(name = "YvsMutExercice.findByReference", query = "SELECT y FROM YvsMutExercice y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsMutExercice.findByDateDebut", query = "SELECT y FROM YvsMutExercice y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsMutExercice.findByDateFin", query = "SELECT y FROM YvsMutExercice y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsMutExercice.findByActif", query = "SELECT y FROM YvsMutExercice y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsMutExercice.findActifByDate", query = "SELECT y FROM YvsMutExercice y WHERE y.actif = :actif AND :date BETWEEN y.dateDebut AND y.dateFin")})
public class YvsMutExercice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_exercice_id_seq", name = "yvs_base_exercice_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_exercice_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutExercice() {
    }

    public YvsMutExercice(Long id) {
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
        return cloturer;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
    }

    public Long getId() {
        return id;
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
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
        if (!(object instanceof YvsMutExercice)) {
            return false;
        }
        YvsMutExercice other = (YvsMutExercice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.YvsMutExercice[ id=" + id + " ]";
    }

}
