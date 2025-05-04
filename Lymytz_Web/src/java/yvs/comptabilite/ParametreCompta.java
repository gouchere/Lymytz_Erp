/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Comptes;
import yvs.base.compta.Journaux;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean()
@SessionScoped
public class ParametreCompta implements Serializable {

    private int id;
    private int tailleCompte = 8;
    private int converter = 0, jourAnterieurCancel, jourAnterieur, jourAntidaterPaiement = -1;
    private double multipleArrondi = 5, montantSeuilOd, montantSeuilRecetteOd, valeurLimiteArrondi;
    private double plafondBp;
    private int nbMaxBp;
    private boolean decimalArrondi = true;
    private int valeurArrondi = 2, ecartDaySoldeClient = 7, nombreLigneSoldeClient = 4;
    private boolean majComptaAutoDivers = false, reportByAgence = false, comptaPartielVirement = true;
    private char majComptaStatutDivers = Constantes.STATUT_DOC_PAYER;
    private String modeArrondi = "A";
    private Date dateSave = new Date();
    private Comptes compteBeneficeReport = new Comptes(), comptePerteReport = new Comptes();
    private Journaux journalReport = new Journaux();

    public ParametreCompta() {
    }

    public ParametreCompta(int id) {
        this.id = id;
    }

    public int getJourAnterieur() {
        return jourAnterieur;
    }

    public void setJourAnterieur(int jourAnterieur) {
        this.jourAnterieur = jourAnterieur;
    }

    public double getValeurLimiteArrondi() {
        return valeurLimiteArrondi;
    }

    public void setValeurLimiteArrondi(double valeurLimiteArrondi) {
        this.valeurLimiteArrondi = valeurLimiteArrondi;
    }

    public boolean isComptaPartielVirement() {
        return comptaPartielVirement;
    }

    public void setComptaPartielVirement(boolean comptaPartielVirement) {
        this.comptaPartielVirement = comptaPartielVirement;
    }

    public int getJourAntidaterPaiement() {
        return jourAntidaterPaiement;
    }

    public void setJourAntidaterPaiement(int jourAntidaterPaiement) {
        this.jourAntidaterPaiement = jourAntidaterPaiement;
    }

    public double getMontantSeuilRecetteOd() {
        return montantSeuilRecetteOd;
    }

    public void setMontantSeuilRecetteOd(double montantSeuilRecetteOd) {
        this.montantSeuilRecetteOd = montantSeuilRecetteOd;
    }

    public double getPlafondBp() {
        return plafondBp;
    }

    public void setPlafondBp(double plafondBp) {
        this.plafondBp = plafondBp;
    }

    public int getNbMaxBp() {
        return nbMaxBp;
    }

    public void setNbMaxBp(int nbMaxBp) {
        this.nbMaxBp = nbMaxBp;
    }

    public int getEcartDaySoldeClient() {
        return ecartDaySoldeClient;
    }

    public void setEcartDaySoldeClient(int ecartDaySoldeClient) {
        this.ecartDaySoldeClient = ecartDaySoldeClient;
    }

    public int getNombreLigneSoldeClient() {
        return nombreLigneSoldeClient;
    }

    public void setNombreLigneSoldeClient(int nombreLigneSoldeClient) {
        this.nombreLigneSoldeClient = nombreLigneSoldeClient;
    }

    public double getMontantSeuilOd() {
        return montantSeuilOd;
    }

    public void setMontantSeuilOd(double montantSeuilOd) {
        this.montantSeuilOd = montantSeuilOd;
    }

    public int getJourAnterieurCancel() {
        return jourAnterieurCancel;
    }

    public void setJourAnterieurCancel(int jourAnterieurCancel) {
        this.jourAnterieurCancel = jourAnterieurCancel;
    }

    public boolean isReportByAgence() {
        return reportByAgence;
    }

    public void setReportByAgence(boolean reportByAgence) {
        this.reportByAgence = reportByAgence;
    }

    public boolean isMajComptaAutoDivers() {
        return majComptaAutoDivers;
    }

    public void setMajComptaAutoDivers(boolean majComptaAutoDivers) {
        this.majComptaAutoDivers = majComptaAutoDivers;
    }

    public char getMajComptaStatutDivers() {
        return majComptaStatutDivers != ' ' ? String.valueOf(majComptaStatutDivers).trim().length() > 0 ? majComptaStatutDivers : Constantes.STATUT_DOC_PAYER : Constantes.STATUT_DOC_PAYER;
    }

    public void setMajComptaStatutDivers(char majComptaStatutDivers) {
        this.majComptaStatutDivers = majComptaStatutDivers;
    }

    public int getConverter() {
        return converter;
    }

    public void setConverter(int converter) {
        this.converter = converter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTailleCompte() {
        return tailleCompte;
    }

    public void setTailleCompte(int tailleCompte) {
        this.tailleCompte = tailleCompte;
    }

    public double getMultipleArrondi() {
        return multipleArrondi;
    }

    public void setMultipleArrondi(double multipleArrondi) {
        this.multipleArrondi = multipleArrondi;
    }

    public boolean isDecimalArrondi() {
        return decimalArrondi;
    }

    public void setDecimalArrondi(boolean decimalArrondi) {
        this.decimalArrondi = decimalArrondi;
    }

    public int getValeurArrondi() {
        return valeurArrondi;
    }

    public void setValeurArrondi(int valeurArrondi) {
        this.valeurArrondi = valeurArrondi;
    }

    public String getModeArrondi() {
        return modeArrondi;
    }

    public void setModeArrondi(String modeArrondi) {
        this.modeArrondi = modeArrondi;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Comptes getCompteBeneficeReport() {
        return compteBeneficeReport;
    }

    public void setCompteBeneficeReport(Comptes compteBeneficeReport) {
        this.compteBeneficeReport = compteBeneficeReport;
    }

    public Comptes getComptePerteReport() {
        return comptePerteReport;
    }

    public void setComptePerteReport(Comptes comptePerteReport) {
        this.comptePerteReport = comptePerteReport;
    }

    public Journaux getJournalReport() {
        return journalReport;
    }

    public void setJournalReport(Journaux journalReport) {
        this.journalReport = journalReport;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.id;
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
        final ParametreCompta other = (ParametreCompta) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
