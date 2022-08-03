/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.parametrage.societe.Societe;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ParametrageGRH implements Serializable {

    private long id;
    private int dureeCumulConge, totaCongePermis;
    private Date datePaiementSalaire;
    private Date dateDebutTraitementSalaire;
    private boolean echellonage, actif, supp;
    private Societe societe = new Societe();
    private Calendrier calendrier = new Calendrier();
    private Short periodeDavancement;
    private Short periodePremierAvancement;
    private int nbreJourMinPerMonth = 18;
    private int nombreMoisAvanceMaxRetenue = 5;
    private int nbreJourMoisRef = 24;   //détermine le nombre de jour d'un mois de réérence ! (pour le calcule de la période de référence)
    private short positionBaseSalaire = 1;    //1=Contrat, 2=Poste de travail
    private double totalHeureTravailHebdo;
    private double limitHeureSup; //limitHeureT indique le nombre limite d'heure supplémentaire accepté par jour
    private Date timeMargeAvance;    //marge d'avance autorisé sur l'heure d'arrivé au travail. utilisé pour calculer idynamiquement la tranche horaire d'un planning
    private Date dureeRetardAutorise;               //permet d'indiquer la marge de retard autorisé lors du premier pointage de la journée
    private Boolean calculPlaningDynamique;//permet d'indiquer si oui ou non on à besoin de plannifier les heures de travails d'un employé
    private Double heureMinimaleRequise;    //Nombre d'heures minimale requise pour valider une journée de travail
    private int ecartValideFiche;
    private int ecartSaisiPointage;
    private Date heureDebutTravail;
    private Date heureFinTravail;
    private Date heureDebutPause;
    private Date heureFinPause;
    private Date dateSave = new Date();
//    private boolean useRestrictionContrat;

    public ParametrageGRH() {
    }

    public ParametrageGRH(long id) {
        this.id = id;
    }

    public double getTotalHeureTravailHebdo() {
        return totalHeureTravailHebdo;
    }

    public void setTotalHeureTravailHebdo(double totalHeureTravailHebdo) {
        this.totalHeureTravailHebdo = totalHeureTravailHebdo;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(Calendrier calendrier) {
        this.calendrier = calendrier;
    }

    public Date getDateDebutTraitementSalaire() {
        return dateDebutTraitementSalaire;
    }

    public void setDateDebutTraitementSalaire(Date dateDebutTraitementSalaire) {
        this.dateDebutTraitementSalaire = dateDebutTraitementSalaire;
    }

    public Short getPeriodeDavancement() {
        return periodeDavancement;
    }

    public void setPeriodeDavancement(Short periodeDavancement) {
        this.periodeDavancement = periodeDavancement;
    }

    public int getNombreMoisAvanceMaxRetenue() {
        return nombreMoisAvanceMaxRetenue;
    }

    public void setNombreMoisAvanceMaxRetenue(int nombreMoisAvanceMaxRetenue) {
        this.nombreMoisAvanceMaxRetenue = nombreMoisAvanceMaxRetenue;
    }

    public Short getPeriodePremierAvancement() {
        return periodePremierAvancement;
    }

    public void setPeriodePremierAvancement(Short periodePremierAvancement) {
        this.periodePremierAvancement = periodePremierAvancement;
    }

    public long getId() {
        return id;
    }

    public int getNbreJourMinPerMonth() {
        return nbreJourMinPerMonth;
    }

    public void setNbreJourMinPerMonth(int nbreJourMinPerMonth) {
        this.nbreJourMinPerMonth = nbreJourMinPerMonth;
    }

    public int getNbreJourMoisRef() {
        return nbreJourMoisRef;
    }

    public void setNbreJourMoisRef(int nbreJourMoisRef) {
        this.nbreJourMoisRef = nbreJourMoisRef;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDureeCumulConge() {
        return dureeCumulConge;
    }

    public void setDureeCumulConge(int dureeCumulConge) {
        this.dureeCumulConge = dureeCumulConge;
    }

    public int getTotaCongePermis() {
        return totaCongePermis;
    }

    public void setTotaCongePermis(int totaCongePermis) {
        this.totaCongePermis = totaCongePermis;
    }

    public Date getDatePaiementSalaire() {
        return datePaiementSalaire;
    }

    public void setDatePaiementSalaire(Date datePaiementSalaire) {
        this.datePaiementSalaire = datePaiementSalaire;
    }
//
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

    public boolean isEchellonage() {
        return echellonage;
    }

    public void setEchellonage(boolean echellonage) {
        this.echellonage = echellonage;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public boolean isSupp() {
        return supp;
    }

    public void setSupp(boolean supp) {
        this.supp = supp;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public short getPositionBaseSalaire() {
        return positionBaseSalaire;
    }

    public void setPositionBaseSalaire(short positionBaseSalaire) {
        this.positionBaseSalaire = positionBaseSalaire;
    }

    public Double getLimitHeureSup() {
        return limitHeureSup;
    }

    public void setLimitHeureSup(Double limitHeureSup) {
        this.limitHeureSup = limitHeureSup;
    }

    public Date getTimeMargeAvance() {
        return timeMargeAvance;
    }

    public void setTimeMargeAvance(Date timeMargeAvance) {
        this.timeMargeAvance = timeMargeAvance;
    }

    public Date getDureeRetardAutorise() {
        return dureeRetardAutorise;
    }

    public void setDureeRetardAutorise(Date dureeRetardAutorise) {
        this.dureeRetardAutorise = dureeRetardAutorise;
    }

    public Boolean isCalculPlaningDynamique() {
        return calculPlaningDynamique;
    }

    public void setCalculPlaningDynamique(Boolean calculPlaningDynamique) {
        this.calculPlaningDynamique = calculPlaningDynamique;
    }

    public Double getHeureMinimaleRequise() {
        return heureMinimaleRequise;
    }

    public void setHeureMinimaleRequise(Double heureMinimaleRequise) {
        this.heureMinimaleRequise = heureMinimaleRequise;
    }

    public void setLimitHeureSup(double limitHeureSup) {
        this.limitHeureSup = limitHeureSup;
    }

    public int getEcartValideFiche() {
        return ecartValideFiche;
    }

    public void setEcartValideFiche(int ecartValideFiche) {
        this.ecartValideFiche = ecartValideFiche;
    }

    public int getEcartSaisiPointage() {
        return ecartSaisiPointage;
    }

    public void setEcartSaisiPointage(int ecartSaisiPointage) {
        this.ecartSaisiPointage = ecartSaisiPointage;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParametrageGRH other = (ParametrageGRH) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
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

}
