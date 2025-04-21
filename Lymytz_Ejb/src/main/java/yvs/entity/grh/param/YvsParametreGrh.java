/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_parametre_grh")
@NamedQueries({
    @NamedQuery(name = "YvsParametreGrh.findAll", query = "SELECT y FROM YvsParametreGrh y LEFT JOIN FETCH y.calendrier WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsParametreGrh.findById", query = "SELECT y FROM YvsParametreGrh y WHERE y.id = :id"),
    @NamedQuery(name = "YvsParametreGrh.findByEchellonageAutomatique", query = "SELECT y FROM YvsParametreGrh y WHERE y.echellonageAutomatique = :auto"),
    @NamedQuery(name = "YvsParametreGrh.findByDureeCumuleConge", query = "SELECT y FROM YvsParametreGrh y WHERE y.dureeCumuleConge = :dureeCumuleConge"),
    @NamedQuery(name = "YvsParametreGrh.findByDatePaiementSalaire", query = "SELECT y FROM YvsParametreGrh y WHERE y.datePaiementSalaire = :datePaiementSalaire"),
    //@NamedQuery(name = "YvsParametreGrh.findByDateDebutExercice", query = "SELECT y FROM YvsParametreGrh y WHERE y.dateDebutExercice = :dateDebutExercice"),
    //@NamedQuery(name = "YvsParametreGrh.findByDateFinExercice", query = "SELECT y FROM YvsParametreGrh y WHERE y.dateFinExercice = :dateFinExercice"),
    @NamedQuery(name = "YvsParametreGrh.findByTotalCongePermis", query = "SELECT y FROM YvsParametreGrh y WHERE y.totalCongePermis = :totalCongePermis"),
    @NamedQuery(name = "YvsParametreGrh.findByTotalHeureTravailHebd", query = "SELECT y FROM YvsParametreGrh y WHERE y.totalHeureTravailHebd = :totalHeureTravailHebd"),
    @NamedQuery(name = "YvsParametreGrh.findByLimitHeureTravail", query = "SELECT y FROM YvsParametreGrh y WHERE y.limitHeureSup = :limitHeureTravail"),
    @NamedQuery(name = "YvsParametreGrh.findBySupp", query = "SELECT y FROM YvsParametreGrh y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsParametreGrh.findByDefaultHoraire", query = "SELECT y FROM YvsParametreGrh y WHERE y.defaultHoraire = true AND y.societe= :societe"),
    @NamedQuery(name = "YvsParametreGrh.findByActif", query = "SELECT y FROM YvsParametreGrh y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsParametreGrh.findByPeriodeDavancement", query = "SELECT y FROM YvsParametreGrh y WHERE y.periodeDavancement = :periodeDavancement"),
    @NamedQuery(name = "YvsParametreGrh.findByPeriodePremierAvancement", query = "SELECT y FROM YvsParametreGrh y WHERE y.periodePremierAvancement = :periodePremierAvancement")})
public class YvsParametreGrh implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_parametre_grh_id_seq", name = "yvs_parametre_grh_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_parametre_grh_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "echellonage_automatique")
    private Boolean echellonageAutomatique;
    @Column(name = "duree_cumule_conge")
    private Integer dureeCumuleConge;
    @Column(name = "date_paiement_salaire")
    @Temporal(TemporalType.DATE)
    private Date datePaiementSalaire;
    @Column(name = "date_debut_traitement_salaire")
    @Temporal(TemporalType.DATE)
    private Date dateDebutTraitementSalaire;
    @Column(name = "total_conge_permis")
    private Integer totalCongePermis;
    @Column(name = "nombre_mois_avance_max_retenue")
    private Integer nombreMoisAvanceMaxRetenue;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_heure_travail_hebd")
    private Double totalHeureTravailHebd;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "periode_davancement")
    private Short periodeDavancement;
    @Column(name = "periode_premier_avancement")
    private Short periodePremierAvancement;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @Column(name = "base_conge")
    private int baseConge = 0;//sert de base pour le calcul de la duré de congé du
    @Column(name = "nbre_jour_mois_ref")
    private Integer nbreJourMoisRef;
    @Column(name = "position_bases_salaire")
    private Short positionBaseSalaire;  //1=Contrat, 2=Poste de travail
    /*Paramètres de la gestion des présences*/
    @Column(name = "limit_heure_sup")
    private Double limitHeureSup;
    @Column(name = "time_marge_avance")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date timeMargeAvance;    //marge d'avance autorisé sur l'heure d'arrivé au travail. utilisé pour calculer idynamiquement la tranche horaire d'un planning
    @Column(name = "time_marge_retard")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date timeMargeRetard;
    @Column(name = "duree_retard_autorise")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date dureeRetardAutorise;               //permet d'indiquer la marge de retard autorisé lors du premier pointage de la journée
    @Column(name = "calcul_planning_dynamique")
    private Boolean calculPlaningDynamique;//permet d'indiquer si oui ou non on à besoin de plannifier les heures de travails d'un employé
    @Column(name = "heure_minimale_requise")
    private Double heureMinimaleRequise;    //Nombre d'heures minimale requise pour valider une journée de travail
    @Column(name = "delais_validation_pointage")
    private Integer delaisValidationPointage;   //indique le nombre de jours de retard autorisé pour valider une fiche de présence
    @Column(name = "delais_sasie_pointage")
    private Integer delaisSasiePointage;   //indique le nombre de jours de retard autorisé pour valider une fiche de présence
    @Column(name = "quotite_cessible")
    private Double quotiteCessible;
    @JoinColumn(name = "calendrier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsCalendrier calendrier;

    @Column(name = "heure_debut_travail")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date heureDebutTravail;
    @Temporal(javax.persistence.TemporalType.TIME)
    @Column(name = "heure_fin_travail")
    private Date heureFinTravail;
    @Column(name = "heure_debut_pause")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date heureDebutPause;
    @Column(name = "heure_fin_pause")
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date heureFinPause;
    @Column(name = "default_horaire")
    private boolean defaultHoraire;

    public YvsParametreGrh() {
    }

    public YvsParametreGrh(Long id) {
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

    public Date getDureeRetardAutorise() {
        return dureeRetardAutorise;
    }

    public void setDureeRetardAutorise(Date dureeRetardAutorise) {
        this.dureeRetardAutorise = dureeRetardAutorise;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEchellonageAutomatique() {
        return echellonageAutomatique != null ? echellonageAutomatique : false;
    }

    public void setEchellonageAutomatique(Boolean echellonageAutomatique) {
        this.echellonageAutomatique = echellonageAutomatique;
    }

    public Integer getDureeCumuleConge() {
        return dureeCumuleConge != null ? dureeCumuleConge : 0;
    }

    public void setDureeCumuleConge(Integer dureeCumuleConge) {
        this.dureeCumuleConge = dureeCumuleConge;
    }

    public Date getDatePaiementSalaire() {
        return datePaiementSalaire;
    }

    public void setDatePaiementSalaire(Date datePaiementSalaire) {
        this.datePaiementSalaire = datePaiementSalaire;
    }

    public void setDateDebutTraitementSalaire(Date dateDebutTraitementSalaire) {
        this.dateDebutTraitementSalaire = dateDebutTraitementSalaire;
    }

    public Date getDateDebutTraitementSalaire() {
        return dateDebutTraitementSalaire;
    }

//    public Date getDateDebutExercice() {
//        return dateDebutExercice;
//    }
//
//    public void setDateDebutExercice(Date dateDebutExercice) {
//        this.dateDebutExercice = dateDebutExercice;
//    }
//
//    public Date getDateFinExercice() {
//        return dateFinExercice;
//    }
//
//    public void setDateFinExercice(Date dateFinExercice) {
//        this.dateFinExercice = dateFinExercice;
//    }
    public Integer getNombreMoisAvanceMaxRetenue() {
        return nombreMoisAvanceMaxRetenue != null ? nombreMoisAvanceMaxRetenue : 5;
    }

    public void setNombreMoisAvanceMaxRetenue(Integer nombreMoisAvanceMaxRetenue) {
        this.nombreMoisAvanceMaxRetenue = nombreMoisAvanceMaxRetenue;
    }

    public Integer getTotalCongePermis() {
        return totalCongePermis != null ? totalCongePermis : 0;
    }

    public void setTotalCongePermis(Integer totalCongePermis) {
        this.totalCongePermis = totalCongePermis;
    }

    public Double getTotalHeureTravailHebd() {
        return totalHeureTravailHebd != null ? totalHeureTravailHebd : 0;
    }

    public void setTotalHeureTravailHebd(Double totalHeureTravailHebd) {
        this.totalHeureTravailHebd = totalHeureTravailHebd;
    }

    public Double getLimitHeureSup() {
        return limitHeureSup != null ? limitHeureSup : 0;
    }

    public void setLimitHeureSup(Double limitHeureSup) {
        this.limitHeureSup = limitHeureSup;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Short getPeriodeDavancement() {
        return periodeDavancement != null ? periodeDavancement : 0;
    }

    public void setPeriodeDavancement(Short periodeDavancement) {
        this.periodeDavancement = periodeDavancement;
    }

    public Short getPeriodePremierAvancement() {
        return periodePremierAvancement != null ? periodeDavancement : 0;
    }

    public void setPeriodePremierAvancement(Short periodePremierAvancement) {
        this.periodePremierAvancement = periodePremierAvancement;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public int getBaseConge() {
        return baseConge;
    }

    public void setBaseConge(int baseConge) {
        this.baseConge = baseConge;
    }

    public Integer getNbreJourMoisRef() {
        return nbreJourMoisRef;
    }

    public void setNbreJourMoisRef(Integer nbreJourMoisRef) {
        this.nbreJourMoisRef = nbreJourMoisRef;
    }

    public Short getPositionBaseSalaire() {
        return positionBaseSalaire;
    }

    public void setPositionBaseSalaire(Short positionBaseSalaire) {
        this.positionBaseSalaire = positionBaseSalaire;
    }

    public Date getTimeMargeAvance() {
        return timeMargeAvance;
    }

    public void setTimeMargeAvance(Date timeMargeAvance) {
        this.timeMargeAvance = timeMargeAvance;
    }

    public Boolean getCalculPlaningDynamique() {
        return calculPlaningDynamique != null ? calculPlaningDynamique : true;
    }

    public void setCalculPlaningDynamique(Boolean calculPlaningDynamique) {
        this.calculPlaningDynamique = calculPlaningDynamique;
    }

    public Double getHeureMinimaleRequise() {
        return heureMinimaleRequise != null ? heureMinimaleRequise : 0;
    }

    public void setHeureMinimaleRequise(Double heureMinimaleRequise) {
        this.heureMinimaleRequise = heureMinimaleRequise;
    }

    public Integer getDelaisValidationPointage() {
        return delaisValidationPointage != null ? delaisValidationPointage : 0;
    }

    public void setDelaisValidationPointage(Integer delaisValidationPointage) {
        this.delaisValidationPointage = delaisValidationPointage;
    }

    public Integer getDelaisSasiePointage() {
        return delaisSasiePointage != null ? delaisSasiePointage : 0;
    }

    public void setDelaisSasiePointage(Integer delaisSasiePointage) {
        this.delaisSasiePointage = delaisSasiePointage;
    }

    public Double getQuotiteCessible() {
        return quotiteCessible;
    }

    public void setQuotiteCessible(Double quotiteCessible) {
        this.quotiteCessible = quotiteCessible;
    }

    public boolean isPlanningDynamique() {
        return getCalculPlaningDynamique();
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
        if (!(object instanceof YvsParametreGrh)) {
            return false;
        }
        YvsParametreGrh other = (YvsParametreGrh) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsParametreGrh[ id=" + id + " ]";
    }

    public YvsCalendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(YvsCalendrier calendrier) {
        this.calendrier = calendrier;
    }

    public Date getHeureDebutTravail() {
        return heureDebutTravail;
    }

    public void setHeureDebutTravail(Date heureDebutTravail) {
        this.heureDebutTravail = heureDebutTravail;
    }

    public Date getHeureFinTravail() {
        return heureFinTravail;
    }

    public void setHeureFinTravail(Date heureFinTravail) {
        this.heureFinTravail = heureFinTravail;
    }

    public Date getHeureDebutPause() {
        return heureDebutPause;
    }

    public void setHeureDebutPause(Date heureDebutPause) {
        this.heureDebutPause = heureDebutPause;
    }

    public Date getHeureFinPause() {
        return heureFinPause;
    }

    public void setHeureFinPause(Date heureFinPause) {
        this.heureFinPause = heureFinPause;
    }

    public boolean getDefaultHoraire() {
        return defaultHoraire;
    }

    public void setDefaultHoraire(boolean defaultHoraire) {
        this.defaultHoraire = defaultHoraire;
    }

    public Date getTimeMargeRetard() {
        return timeMargeRetard;
    }

    public void setTimeMargeRetard(Date timeMargeRetard) {
        this.timeMargeRetard = timeMargeRetard;
    }

}
