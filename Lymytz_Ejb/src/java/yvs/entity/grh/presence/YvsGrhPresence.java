/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.presence;

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
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.salaire.YvsGrhValorisationRetard;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_presence")
@NamedQueries({
    @NamedQuery(name = "YvsGrhPresence.findAll", query = "SELECT y FROM YvsGrhPresence y"),
    @NamedQuery(name = "YvsGrhPresence.findById", query = "SELECT y FROM YvsGrhPresence y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhPresence.findFicheToNotif", query = "SELECT y.id FROM YvsGrhPresence y WHERE y.submitTo=:user AND y.validerHs=false AND y.valider=true AND y.totalHeureSup>0 AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhPresence.findByCodeValidation", query = "SELECT COUNT(y) FROM YvsGrhPresence y WHERE y.employe = :employe AND y.dateDebut BETWEEN :date1 AND :date2 AND y.valider=true AND  y.typeValidation.code=:code"),
    @NamedQuery(name = "YvsGrhPresence.findFichePointe", query = "SELECT COUNT(y) FROM YvsGrhPresence y WHERE y.employe = :employe AND y.dateDebut BETWEEN :debut AND :fin AND y.valider=true"),
    @NamedQuery(name = "YvsGrhPresence.findByTotalPresence", query = "SELECT y FROM YvsGrhPresence y WHERE y.totalPresence = :totalPresence"),
    @NamedQuery(name = "YvsGrhPresence.findByTauxJournee", query = "SELECT y FROM YvsGrhPresence y WHERE y.tauxJournee = :tauxJournee"),
    @NamedQuery(name = "YvsGrhPresence.findByMargeApprouve", query = "SELECT y FROM YvsGrhPresence y WHERE y.margeApprouve = :margeApprouve"),
    @NamedQuery(name = "YvsGrhPresence.findOneFiche", query = "SELECT y FROM YvsGrhPresence y JOIN FETCH y.employe LEFT JOIN FETCH y.employe.contrat LEFT JOIN FETCH y.typeValidation LEFT JOIN FETCH y.pointages WHERE y.employe = :employe AND y.dateDebut=:date"),
    @NamedQuery(name = "YvsGrhPresence.findSimpleOneFiche", query = "SELECT y FROM YvsGrhPresence y JOIN FETCH y.employe LEFT JOIN FETCH y.employe.contrat LEFT JOIN FETCH y.typeValidation WHERE y.employe = :employe AND y.dateDebut=:date"),
    @NamedQuery(name = "YvsGrhPresence.findOneFiches", query = "SELECT y FROM YvsGrhPresence y JOIN FETCH y.employe JOIN FETCH y.employe.contrat LEFT JOIN FETCH y.typeValidation LEFT JOIN FETCH y.pointages WHERE y.employe = :employe AND y.dateDebut=:dateDebut AND y.dateFin=:dateFin"),
    @NamedQuery(name = "YvsGrhPresence.findByDate", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date OR y.dateFin=:date) AND y.employe.agence=:agence ORDER BY y.employe.nom"),
    @NamedQuery(name = "YvsGrhPresence.findByDateD", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date) AND y.employe.agence=:agence ORDER BY y.employe.nom"),
    @NamedQuery(name = "YvsGrhPresence.findByDateS", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date OR y.dateFin=:date) AND y.employe.agence.societe=:societe ORDER BY y.employe.nom"),
    @NamedQuery(name = "YvsGrhPresence.findByTwoDates", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date1 OR y.dateDebut=:date2) AND y.employe=:employe ORDER BY y.dateDebut"),
    //fiche d'une société à une date
    @NamedQuery(name = "YvsGrhPresence.findByDateSD", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date) AND y.employe.agence.societe=:societe ORDER BY y.employe.nom ASC"),
    //fiche d'un départment à une date
    @NamedQuery(name = "YvsGrhPresence.findByDateDepart", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date) AND y.employe.posteActif.departement=:departement ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhPresence.findByDateDepartIN", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date) AND y.employe.posteActif.departement.id IN :departement ORDER BY y.employe.nom ASC"),
    //fiche d'une Equipe à une date
    @NamedQuery(name = "YvsGrhPresence.findByDateEquipe", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date) AND y.employe.equipe=:equipe ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhPresence.findByDateSDAndTV", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date) AND y.employe.agence.societe=:societe AND y.typeValidation=:typeV ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhPresence.findByDateAGAndTV", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date) AND y.employe.agence=:agence AND y.typeValidation=:typeV ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhPresence.findByDateDEPAndTV", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date) AND y.employe.posteActif.departement.id IN :departements AND y.typeValidation=:typeV ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhPresence.findByDateEquipeTV", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date) AND y.employe.equipe=:equipe AND y.typeValidation=:typeV ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhPresence.findByDateSDAndNotValid", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date) AND y.employe.agence.societe=:societe AND y.typeValidation IS null ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhPresence.findLIKE", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date OR y.dateFin=:date) AND y.employe.agence=:agence AND (y.employe.nom LIKE :code OR y.employe.matricule LIKE :code OR y.employe.prenom LIKE :code) ORDER BY y.employe.nom"),
    @NamedQuery(name = "YvsGrhPresence.findLIKES", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date OR y.dateFin=:date) AND y.employe.agence.societe=:societe AND (y.employe.nom LIKE :code OR y.employe.matricule LIKE :code OR y.employe.prenom LIKE :code) ORDER BY y.employe.nom"),
    @NamedQuery(name = "YvsGrhPresence.findByDateAndEmp", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date OR y.dateFin=:date) AND y.employe.nom LIKE :nameEmp ORDER BY y.employe.nom"),
    @NamedQuery(name = "YvsGrhPresence.findBetweenDateAndEmp", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut BETWEEN :debut AND :fin) AND y.employe.matricule=:employe ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsGrhPresence.findByDateAndEmpB", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut BETWEEN :debut AND :fin) AND y.employe=:employe ORDER BY y.dateDebut DESC"),
    @NamedQuery(name = "YvsGrhPresence.findByEmployeAndDate", query = "SELECT y FROM YvsGrhPresence y WHERE (y.dateDebut = :date OR y.dateFin=:date) AND (y.employe.nom LIKE :employe OR y.employe.prenom LIKE :employe) ORDER BY y.employe.prenom"),
    @NamedQuery(name = "YvsGrhPresence.findByDateFin", query = "SELECT y FROM YvsGrhPresence y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsGrhPresence.findByHeureDebut", query = "SELECT y FROM YvsGrhPresence y WHERE y.heureDebut = :heureDebut"),
    @NamedQuery(name = "YvsGrhPresence.countTotalHS", query = "SELECT SUM(y.totalHeureSup) FROM YvsGrhPresence y WHERE y.valider=true AND y.employe=:employe AND (y.dateDebut BETWEEN :date1 AND :date2)"),
    @NamedQuery(name = "YvsGrhPresence.countTotalHSDIM", query = "SELECT SUM(y.totalHeureSup) FROM YvsGrhPresence y WHERE y.valider=true AND y.employe=:employe AND (y.dateDebut BETWEEN :date1 AND :date2)"),
    @NamedQuery(name = "YvsGrhPresence.findByHeureFin", query = "SELECT y FROM YvsGrhPresence y WHERE y.heureFin = :heureFin"),

    @NamedQuery(name = "YvsGrhPresence.findByDatesAndEmploye", query = "SELECT y FROM YvsGrhPresence y WHERE  y.dateDebut = :dateDebut AND y.dateFin = :dateFin AND y.employe = :employe ORDER BY y.heureDebut DESC"),
    @NamedQuery(name = "YvsGrhPresence.findByDatesTimesAndEmploye", query = "SELECT y FROM YvsGrhPresence y WHERE  y.dateDebut = :dateDebut AND y.dateFin = :dateFin AND y.employe = :employe AND y.heureDebut = :heureDebut AND y.heureFin = :heureFin ORDER BY y.heureDebut DESC"),
    @NamedQuery(name = "YvsGrhPresence.findLastByDateDebutAndEmploye", query = "SELECT y FROM YvsGrhPresence y WHERE  y.dateDebut = :dateDebut AND y.employe = :employe ORDER BY y.heureDebut DESC"),
    @NamedQuery(name = "YvsGrhPresence.findByDateDebutAndEmploye", query = "SELECT y FROM YvsGrhPresence y WHERE  y.dateDebut = :dateDebut AND y.employe = :employe ORDER BY y.dateDebut, y.heureDebut"),
    @NamedQuery(name = "YvsGrhPresence.findByDateAndEmploye", query = "SELECT y FROM YvsGrhPresence y WHERE :date BETWEEN y.dateDebut AND y.dateFin AND y.employe = :employe ORDER BY y.dateDebut, y.heureDebut"),
    @NamedQuery(name = "YvsGrhPresence.findByDateAndDatePrevuAndEmploye", query = "SELECT y FROM YvsGrhPresence y WHERE :date BETWEEN y.dateDebut AND y.dateFinPrevu AND y.employe = :employe ORDER BY y.dateDebut, y.heureDebut"),
    @NamedQuery(name = "YvsGrhPresence.findWithRetard", query = "SELECT y.id, y.totalRetard FROM YvsGrhPresence y WHERE y.employe=:employe AND y.dateDebut<=:date AND y.valider=true AND y.totalRetard>0"),
    @NamedQuery(name = "YvsGrhPresence.findByDateAndEmployeValid", query = "SELECT y FROM YvsGrhPresence y WHERE :date BETWEEN y.dateDebut AND y.dateFin AND y.employe = :employe AND y.valider = :valider ORDER BY y.dateDebut, y.heureDebut")})
public class YvsGrhPresence implements Serializable {
    @Column(name = "total_heure_sup")
    private Double totalHeureSup;
    
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_presence_id_seq", name = "yvs_grh_presence_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_presence_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_presence")
    private Double totalPresence;
    @Column(name = "total_retard")
    private Double totalRetard;
    @Column(name = "total_heure_compensation")
    private Double totalHeureCompensation;
    @Column(name = "taux_journee")
    private Double tauxJournee;
    @Column(name = "duree_pause")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date dureePause;
    @Column(name = "marge_approuve")
    @Temporal(TemporalType.TIME)
    private Date margeApprouve;
    @Column(name = "valider")
    private Boolean valider;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "date_fin_prevu")
    @Temporal(TemporalType.DATE)
    private Date dateFinPrevu;
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    @Column(name = "heure_fin_prevu")
    @Temporal(TemporalType.TIME)
    private Date heureFinPrevu;
    @Column(name = "valider_hs")
    private Boolean validerHs;

    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "type_validation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeValidation typeValidation;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "submit_to", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers submitTo;

    @OneToMany(mappedBy = "presence")
    private List<YvsGrhPointage> pointages;
    @Transient
    private boolean supplementaire;

    public YvsGrhPresence() {
    }

    public YvsGrhPresence(Long id) {
        this.id = id;
    }

    public YvsGrhPresence(YvsGrhEmployes employe, Date dateDebut) {
        this.employe = employe;
        this.dateDebut = dateDebut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateFinPrevu() {
        return dateFinPrevu != null ? dateFinPrevu : new Date();
    }

    public void setDateFinPrevu(Date dateFinPrevu) {
        this.dateFinPrevu = dateFinPrevu;
    }

    public Date getHeureFinPrevu() {
        return heureFinPrevu != null ? heureFinPrevu : new Date();
    }

    public void setHeureFinPrevu(Date heureFinPrevu) {
        this.heureFinPrevu = heureFinPrevu;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isSupplementaire() {
        return supplementaire;
    }

    public void setSupplementaire(boolean supplementaire) {
        this.supplementaire = supplementaire;
    }

    public Double getTotalPresence() {
        return totalPresence != null ? totalPresence : 0;
    }

    public void setTotalPresence(Double totalPresence) {
        this.totalPresence = totalPresence;
    }

    public Double getTauxJournee() {
        return tauxJournee;
    }

    public void setTauxJournee(Double tauxJournee) {
        this.tauxJournee = tauxJournee;
    }

    public Date getMargeApprouve() {
        return margeApprouve;
    }

    public void setMargeApprouve(Date margeApprouve) {
        this.margeApprouve = margeApprouve;
    }

    public Boolean getValider() {
        return valider != null ? valider : false;
    }

    public void setValider(Boolean valider) {
        this.valider = valider;
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

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public List<YvsGrhPointage> getPointages() {
        return pointages;
    }

    public void setPointages(List<YvsGrhPointage> pointages) {
        this.pointages = pointages;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public YvsGrhTypeValidation getTypeValidation() {
        return typeValidation;
    }

    public void setTypeValidation(YvsGrhTypeValidation typeValidation) {
        this.typeValidation = typeValidation;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Double getTotalHeureCompensation() {
        return totalHeureCompensation != null ? totalHeureCompensation : 0;
    }

    public double getTotalHeureSup() {
        return totalHeureSup;
    }

    public void setTotalHeureCompensation(Double totalHeureCompensation) {
        this.totalHeureCompensation = totalHeureCompensation;
    }

    public void setTotalHeureSup(double totalHeureSup) {
        this.totalHeureSup = totalHeureSup;
    }

    public void setDureePause(Date dureePause) {
        this.dureePause = dureePause;
    }

    public Date getDureePause() {
        return dureePause;
    }

    public Boolean getValiderHs() {
        return validerHs != null ? validerHs : false;
    }

    public void setValiderHs(Boolean validerHs) {
        this.validerHs = validerHs;
    }

    public YvsUsers getSubmitTo() {
        return submitTo;
    }

    public void setSubmitTo(YvsUsers submitTo) {
        this.submitTo = submitTo;
    }

    public void setTotalRetard(Double totalRetard) {
        this.totalRetard = totalRetard;
    }

    public Double getTotalRetard() {
        return totalRetard;
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
        if (!(object instanceof YvsGrhPresence)) {
            return false;
        }
        YvsGrhPresence other = (YvsGrhPresence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.presence.YvsGrhPresence[ id=" + id + " ]";
    }


}
