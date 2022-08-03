/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.taches;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_realisation_tache")
@NamedQueries({
    @NamedQuery(name = "YvsGrhRealisationTache.findAll", query = "SELECT y FROM YvsGrhRealisationTache y WHERE y.author.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhRealisationTache.findByDateRealisation", query = "SELECT y FROM YvsGrhRealisationTache y WHERE y.dateRealisation = :dateRealisation"),
    @NamedQuery(name = "YvsGrhRealisationTache.findByQuantiteRealise", query = "SELECT y FROM YvsGrhRealisationTache y WHERE y.quantiteRealise = :quantiteRealise"),
    @NamedQuery(name = "YvsGrhRealisationTache.findByStatut", query = "SELECT y FROM YvsGrhRealisationTache y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsGrhRealisationTache.findByEmploye", query = "SELECT y FROM YvsGrhRealisationTache y WHERE y.tache.employe.nom = :nom"),
    @NamedQuery(name = "YvsGrhRealisationTache.findByPourcentageValidation", query = "SELECT y FROM YvsGrhRealisationTache y WHERE y.pourcentageValidation = :pourcentageValidation")})
public class YvsGrhRealisationTache implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_realisation_tache_id_seq", name = "yvs_grh_realisation_tache_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_realisation_tache_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_realisation")
    @Temporal(TemporalType.DATE)
    private Date dateRealisation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite_realise")
    private Double quantiteRealise;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "pourcentage_validation")
    private Double pourcentageValidation;
    @JoinColumn(name = "tache", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTacheEmps tache;
    @Column(name = "debut_realisation")
    @Temporal(TemporalType.DATE)
    private Date debutRealisation;
    @Column(name = "fin_realisation")
    @Temporal(TemporalType.DATE)
    private Date finRealisation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;

    public YvsGrhRealisationTache() {
    }

    public YvsGrhRealisationTache(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateRealisation() {
        return dateRealisation;
    }

    public void setDateRealisation(Date dateRealisation) {
        this.dateRealisation = dateRealisation;
    }

    public Double getQuantiteRealise() {
        return quantiteRealise;
    }

    public void setQuantiteRealise(Double quantiteRealise) {
        this.quantiteRealise = quantiteRealise;
    }

    public Character getStatut() {
        return statut;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public Double getPourcentageValidation() {
        return pourcentageValidation;
    }

    public void setPourcentageValidation(Double pourcentageValidation) {
        this.pourcentageValidation = pourcentageValidation;
    }

    public YvsGrhTacheEmps getTache() {
        return tache;
    }

    public void setTache(YvsGrhTacheEmps tache) {
        this.tache = tache;
    }

    public void setFinRealisation(Date finRealisation) {
        this.finRealisation = finRealisation;
    }

    public Date getFinRealisation() {
        return finRealisation;
    }

    public void setDebutRealisation(Date debutRealisation) {
        this.debutRealisation = debutRealisation;
    }

    public Date getDebutRealisation() {
        return debutRealisation;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsersAgence getAuthor() {
        return author;
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
        if (!(object instanceof YvsGrhRealisationTache)) {
            return false;
        }
        YvsGrhRealisationTache other = (YvsGrhRealisationTache) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.taches.YvsGrhRealisationTache[ id=" + id + " ]";
    }

}
