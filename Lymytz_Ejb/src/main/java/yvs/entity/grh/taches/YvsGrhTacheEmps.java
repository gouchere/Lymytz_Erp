/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.taches;

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
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_grh_tache_emps")
@NamedQueries({
    @NamedQuery(name = "YvsTacheEmps.findAll", query = "SELECT y FROM YvsGrhTacheEmps y"),
    @NamedQuery(name = "YvsTacheEmps.findTache", query = "SELECT DISTINCT y.tache.taches FROM YvsGrhTacheEmps y WHERE y.tache.regleTache.agence.societe = :societe"),
    @NamedQuery(name = "YvsTacheEmps.findByDateFin", query = "SELECT y FROM YvsGrhTacheEmps y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsTacheEmps.findByEmploye", query = "SELECT y FROM YvsGrhTacheEmps y WHERE y.tache.id = :idTache AND (y.employe.matricule LIKE :employe OR y.employe.nom LIKE :employe OR y.employe.prenom LIKE :employe)"),
    @NamedQuery(name = "YvsTacheEmps.findByTache", query = "SELECT y FROM YvsGrhTacheEmps y WHERE y.tache.id = :idTache ORDER BY y.employe.nom"),
    @NamedQuery(name = "YvsTacheEmps.findByActif", query = "SELECT y FROM YvsGrhTacheEmps y WHERE y.actif = :actif")})
public class YvsGrhTacheEmps implements Serializable {
//    @Column(name = "date_debut")
//    @Temporal(TemporalType.DATE)
//    private Date dateDebut;

    @Column(name = "supp")
    private Boolean supp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Column(name = "quantite")
//    private Double quantite;
//    @Column(name = "statut_tache")
//    private Character statutTache;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_tache_emps_id_seq", name = "yvs_tache_emps_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_tache_emps_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    private Date datePlanification;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "planification_permanente")
    private Boolean planificationPermanente;
    @JoinColumn(name = "tache", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhMontantTache tache;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhTacheEmps() {
    }

    public YvsGrhTacheEmps(Long id) {
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

    public Boolean getPlanificationPermanente() {
        return planificationPermanente != null ? planificationPermanente : false;
    }

    public void setPlanificationPermanente(Boolean planificationPermanente) {
        this.planificationPermanente = planificationPermanente;
    }

    public Boolean isActif() {
        return actif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatePlanification() {
        return datePlanification;
    }

    public void setDatePlanification(Date datePlanification) {
        this.datePlanification = datePlanification;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhMontantTache getTache() {
        return tache;
    }

    public void setTache(YvsGrhMontantTache tache) {
        this.tache = tache;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsGrhTacheEmps)) {
            return false;
        }
        YvsGrhTacheEmps other = (YvsGrhTacheEmps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsTacheEmps[ id=" + id + " ]";
    }

//    public Date getDateDebut() {
//        return dateDebut;
//    }
//
//    public void setDateDebut(Date dateDebut) {
//        this.dateDebut = dateDebut;
//    }
    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

//    public Double getQuantite() {
//        return quantite;
//    }
//
//    public void setQuantite(Double quantite) {
//        this.quantite = quantite;
//    }
//
//    public Character getStatutTache() {
//        return statutTache;
//    }
//
//    public void setStatutTache(Character statutTache) {
//        this.statutTache = statutTache;
//    }

}
