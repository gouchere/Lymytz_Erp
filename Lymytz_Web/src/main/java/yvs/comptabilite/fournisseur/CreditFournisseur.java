/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.fournisseur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Journaux;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.entity.compta.achat.YvsComptaReglementCreditFournisseur;
import yvs.grh.bean.TypeCout;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class CreditFournisseur implements Serializable {

    private long id;
    private String numReference;
    private String motif;
    private boolean comptabilise;
    private Date dateCredit = new Date();
    private Date dateSave = new Date();
    private char statut = Constantes.STATUT_DOC_ATTENTE;
    private double montant, reste, solde;
    private Fournisseur fournisseur = new Fournisseur();
    private Journaux journal = new Journaux();
    private TypeCout typeCredit = new TypeCout();
    private List<YvsComptaReglementCreditFournisseur> reglements;

    public CreditFournisseur() {
        reglements = new ArrayList<>();
    }

    public CreditFournisseur(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public String getNumReference() {
        return numReference;
    }

    public void setNumReference(String numReference) {
        this.numReference = numReference;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Date getDateCredit() {
        return dateCredit != null ? dateCredit : new Date();
    }

    public void setDateCredit(Date dateCredit) {
        this.dateCredit = dateCredit;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Journaux getJournal() {
        return journal;
    }

    public void setJournal(Journaux journal) {
        this.journal = journal;
    }

    public TypeCout getTypeCredit() {
        return typeCredit;
    }

    public void setTypeCredit(TypeCout typeCredit) {
        this.typeCredit = typeCredit;
    }

    public List<YvsComptaReglementCreditFournisseur> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaReglementCreditFournisseur> reglements) {
        this.reglements = reglements;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public char getStatut() {
        return Character.valueOf(statut) != null ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final CreditFournisseur other = (CreditFournisseur) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public boolean canEditable() {
        for (YvsComptaReglementCreditFournisseur c : reglements) {
            if (c.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                return false;
            }
        }
        return true;
    }
}
