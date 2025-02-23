/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean(name = "parametrage")
@SessionScoped
public class Parametre implements Serializable {

    private long id;
    private double quotiteCessible;
    private int dureeMembre;
    private Date debutMois, dateSave = new Date();
    private String monnaie = Constantes.MONNAIE_FCFA;
    private double fractionMinimal;
    private int periodeSalaireMoyen;    //nombre de mois servant de base pour le calcul du salaire moyen
    private boolean souscriptionGeneral = true;
    private int arrondiValeur = 0;
    private int arrondiQte = 0;
    private boolean retainsEpargne = true; //indique si au paiement des salaire, l'epargnes doit être obligatoirement être  retenu
    private boolean acceptRetraitEpargne = false; //indique si La mutuelle autorise des retrait du compte epargne
    private boolean validCreditByVote;
    private int dureeEtudeCredit;
    private double tauxVoteValidCreditCorrect;
    private double tauxVoteValidCreditIncorrect;
    private double capaciteEndettement;
    private String baseCapaciteEndettement;
    private double tauxCouvertureCredit;
    private boolean paiementParCompteStrict = true;
    private boolean creditRetainsInteret;

    private long nombreVoteRequis; //transient; on enregistre pas

    private int debutEpargne;   //Période autorisé pour l'enregistrement d'une épargne
    private int finEpargne;

    private int retardSaisieEpargne;    //Nombre de jour de retard autorisé lors de la saisie d'une épargne

    private YvsMutCaisse caisseEpargne;
    private YvsMutCaisse caisseCredit;

    public Parametre() {
    }

    public Parametre(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isValidCreditByVote() {
        return validCreditByVote;
    }

    public void setValidCreditByVote(boolean validCreditByVote) {
        this.validCreditByVote = validCreditByVote;
    }

    public int getDureeEtudeCredit() {
        return dureeEtudeCredit;
    }

    public void setDureeEtudeCredit(int dureeEtudeCredit) {
        this.dureeEtudeCredit = dureeEtudeCredit;
    }

    public double getTauxVoteValidCreditCorrect() {
        return tauxVoteValidCreditCorrect;
    }

    public void setTauxVoteValidCreditCorrect(double tauxVoteValidCreditCorrect) {
        this.tauxVoteValidCreditCorrect = tauxVoteValidCreditCorrect;
    }

    public double getTauxVoteValidCreditIncorrect() {
        return tauxVoteValidCreditIncorrect;
    }

    public void setTauxVoteValidCreditIncorrect(double tauxVoteValidCreditIncorrect) {
        this.tauxVoteValidCreditIncorrect = tauxVoteValidCreditIncorrect;
    }

    public String getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }

    public double getFractionMinimal() {
        return fractionMinimal;
    }

    public void setFractionMinimal(double fractionMinimal) {
        this.fractionMinimal = fractionMinimal;
    }

    public boolean isSouscriptionGeneral() {
        return souscriptionGeneral;
    }

    public void setSouscriptionGeneral(boolean souscriptionGeneral) {
        this.souscriptionGeneral = souscriptionGeneral;
    }

    public double getQuotiteCessible() {
        return quotiteCessible;
    }

    public void setQuotiteCessible(double quotiteCessible) {
        this.quotiteCessible = quotiteCessible;
    }

    public long getId() {
        return id;
    }

    public int getDureeMembre() {
        return dureeMembre;
    }

    public void setDureeMembre(int dureeMembre) {
        this.dureeMembre = dureeMembre;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDebutMois() {
        return debutMois != null ? debutMois : new Date();
    }

    public void setDebutMois(Date debutMois) {
        this.debutMois = debutMois;
    }

    public int getPeriodeSalaireMoyen() {
        return periodeSalaireMoyen;
    }

    public void setPeriodeSalaireMoyen(int periodeSalaireMoyen) {
        this.periodeSalaireMoyen = periodeSalaireMoyen;
    }

    public int getArrondiQte() {
        return arrondiQte;
    }

    public void setArrondiQte(int arrondiQte) {
        this.arrondiQte = arrondiQte;
    }

    public void setArrondiValeur(int arrondiValeur) {
        this.arrondiValeur = arrondiValeur;
    }

    public int getArrondiValeur() {
        return arrondiValeur;
    }

    public boolean isRetainsEpargne() {
        return retainsEpargne;
    }

    public void setRetainsEpargne(boolean retainsEpargne) {
        this.retainsEpargne = retainsEpargne;
    }

    public YvsMutCaisse getCaisseEpargne() {
        return caisseEpargne;
    }

    public void setCaisseEpargne(YvsMutCaisse caisseEpargne) {
        this.caisseEpargne = caisseEpargne;
    }

    public YvsMutCaisse getCaisseCredit() {
        return caisseCredit;
    }

    public void setCaisseCredit(YvsMutCaisse caisseCredit) {
        this.caisseCredit = caisseCredit;
    }

    public double getTauxCouvertureCredit() {
        return tauxCouvertureCredit;
    }

    public void setTauxCouvertureCredit(double tauxCouvertureCredit) {
        this.tauxCouvertureCredit = tauxCouvertureCredit;
    }

    public boolean isPaiementParCompteStrict() {
        return paiementParCompteStrict;
    }

    public void setPaiementParCompteStrict(boolean paiementParCompteStrict) {
        this.paiementParCompteStrict = paiementParCompteStrict;
    }

    public boolean isAcceptRetraitEpargne() {
        return acceptRetraitEpargne;
    }

    public void setAcceptRetraitEpargne(boolean acceptRetraitEpargne) {
        this.acceptRetraitEpargne = acceptRetraitEpargne;
    }

    public int getDebutEpargne() {
        return debutEpargne;
    }

    public void setDebutEpargne(int debutEpargne) {
        this.debutEpargne = debutEpargne;
    }

    public int getFinEpargne() {
        return finEpargne;
    }

    public void setFinEpargne(int finEpargne) {
        this.finEpargne = finEpargne;
    }

    public int getRetardSaisieEpargne() {
        return retardSaisieEpargne;
    }

    public void setRetardSaisieEpargne(int retardSaisieEpargne) {
        this.retardSaisieEpargne = retardSaisieEpargne;
    }

    public boolean isCreditRetainsInteret() {
        return creditRetainsInteret;
    }

    public void setCreditRetainsInteret(boolean creditRetainsInteret) {
        this.creditRetainsInteret = creditRetainsInteret;
    }

    public long getNombreVoteRequis() {
        return nombreVoteRequis;
    }

    public void setNombreVoteRequis(long nombreVoteRequis) {
        this.nombreVoteRequis = nombreVoteRequis;
    }

    public String getBaseCapaciteEndettement() {
        return baseCapaciteEndettement;
    }

    public void setBaseCapaciteEndettement(String baseCapaciteEndettement) {
        this.baseCapaciteEndettement = baseCapaciteEndettement;
    }

    public double getCapaciteEndettement() {
        return capaciteEndettement;
    }

    public void setCapaciteEndettement(double capaciteEndettement) {
        this.capaciteEndettement = capaciteEndettement;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Parametre other = (Parametre) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
