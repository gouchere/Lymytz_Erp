/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.evenement;

import java.io.Serializable;import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.mutuelle.CaisseMutuelle;
import yvs.mutuelle.Compte;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ContributionEvenement implements Serializable, Comparable {

    private long id;
    private Date dateContribution = new Date();
    private double montant, montantVerse;
    private Compte compte = new Compte();
    private Evenement evenement = new Evenement();
    private boolean selectActif, new_, regle, afficheAll;
    private Date dateSave = new Date();

    public ContributionEvenement() {
    }

    public ContributionEvenement(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public boolean isAfficheAll() {
        return afficheAll;
    }

    public void setAfficheAll(boolean afficheAll) {
        this.afficheAll = afficheAll;
    }

    public boolean isRegle() {
        return regle;
    }

    public void setRegle(boolean regle) {
        this.regle = regle;
    }

    public double getMontantVerse() {
        return montantVerse;
    }

    public void setMontantVerse(double montantVerse) {
        this.montantVerse = montantVerse;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateContribution() {
        return dateContribution;
    }

    public void setDateContribution(Date dateContribution) {
        this.dateContribution = dateContribution;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ContributionEvenement other = (ContributionEvenement) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        ContributionEvenement c = (ContributionEvenement) o;
        if (dateContribution.equals(c.dateContribution)) {
            Long.valueOf(id).compareTo(c.id);
        }
        return dateContribution.compareTo(c.dateContribution);
    }

}
