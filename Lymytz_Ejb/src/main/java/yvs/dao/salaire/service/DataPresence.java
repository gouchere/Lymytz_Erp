/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

/**
 *
 * @author hp Elite 8300
 */
public class DataPresence {

    private String employe;
    private double dureePresenceEffectif;
    private double dureePresenceNormal;
    private double dureePresenceRequis;
    private double dureeAbsence;
    private double jourSupFerie;
    private double jourSup;
    private double nbFerie; //nombre de jour férié dans la période
    private double jourCompensation;
    private double nbMission;
    private double nbFormation;
    private double nbRepos;
    private double nbReposRequis;
    private double nbCongeTechnique;
    private double nbCongeAnnuel;
    private double nbCongeMaladie;
    private double nbPermissionCD;
    private double nbPermissionLD;

    public DataPresence() {
    }

    public String getEmploye() {
        return employe;
    }

    public void setEmploye(String employe) {
        this.employe = employe;
    }

    public double getDureePresenceNormal() {
        return dureePresenceNormal;
    }

    public void setDureePresenceNormal(double dureePresenceNormal) {
        this.dureePresenceNormal = dureePresenceNormal;
    }

    public double getDureePresenceRequis() {
        return dureePresenceRequis;
    }

    public void setDureePresenceRequis(double dureePresenceRequis) {
        this.dureePresenceRequis = dureePresenceRequis;
    }

    public double getDureeAbsence() {
        return dureeAbsence;
    }

    public void setDureeAbsence(double dureeAbsence) {
        this.dureeAbsence = dureeAbsence;
    }

    public double getJourSupFerie() {
        return jourSupFerie;
    }

    public void setJourSupFerie(double jourSupFerie) {
        this.jourSupFerie = jourSupFerie;
    }

    public double getJourSup() {
        return jourSup;
    }

    public void setJourSup(double jourSup) {
        this.jourSup = jourSup;
    }

    public double getNbFerie() {
        return nbFerie;
    }

    public void setNbFerie(double nbFerie) {
        this.nbFerie = nbFerie;
    }

    public double getJourCompensation() {
        return jourCompensation;
    }

    public void setJourCompensation(double jourCompensation) {
        this.jourCompensation = jourCompensation;
    }

    public double getNbMission() {
        return nbMission;
    }

    public void setNbMission(double nbMission) {
        this.nbMission = nbMission;
    }

    public double getNbFormation() {
        return nbFormation;
    }

    public void setNbFormation(double nbFormation) {
        this.nbFormation = nbFormation;
    }

    public double getNbRepos() {
        return nbRepos;
    }

    public void setNbRepos(double nbRepos) {
        this.nbRepos = nbRepos;
    }

    public double getNbReposRequis() {
        return nbReposRequis;
    }

    public void setNbReposRequis(double nbReposRequis) {
        this.nbReposRequis = nbReposRequis;
    }

    public double getNbCongeTechnique() {
        return nbCongeTechnique;
    }

    public void setNbCongeTechnique(double nbCongeTechnique) {
        this.nbCongeTechnique = nbCongeTechnique;
    }

    public double getNbCongeAnnuel() {
        return nbCongeAnnuel;
    }

    public void setNbCongeAnnuel(double nbCongeAnnuel) {
        this.nbCongeAnnuel = nbCongeAnnuel;
    }

    public double getNbCongeMaladie() {
        return nbCongeMaladie;
    }

    public void setNbCongeMaladie(double nbCongeMaladie) {
        this.nbCongeMaladie = nbCongeMaladie;
    }

    public double getNbPermissionCD() {
        return nbPermissionCD;
    }

    public void setNbPermissionCD(double nbPermissionCD) {
        this.nbPermissionCD = nbPermissionCD;
    }

    public double getNbPermissionLD() {
        return nbPermissionLD;
    }

    public void setNbPermissionLD(double nbPermissionLD) {
        this.nbPermissionLD = nbPermissionLD;
    }

    public double getDureePresenceEffectif() {
        return dureePresenceEffectif;
    }

    public void setDureePresenceEffectif(double dureePresenceEffectif) {
        this.dureePresenceEffectif = dureePresenceEffectif;
    }

}
