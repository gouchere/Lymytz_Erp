/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ContentModeleSaisie implements Serializable {

    private long id;
    private DataModele jour = new DataModele();
    private DataModele numPiece = new DataModele();
    private DataModele reference = new DataModele();
    private DataModele compteG = new DataModele();
    private DataModele compteT = new DataModele();
    private DataModele libelle = new DataModele();
    private DataModele debit = new DataModele();
    private DataModele credit = new DataModele();
    private DataModele echeance = new DataModele();

    public ContentModeleSaisie() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DataModele getJour() {
        return jour;
    }

    public void setJour(DataModele jour) {
        this.jour = jour;
    }

    public DataModele getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(DataModele numPiece) {
        this.numPiece = numPiece;
    }

    public DataModele getReference() {
        return reference;
    }

    public void setReference(DataModele reference) {
        this.reference = reference;
    }

    public DataModele getCompteG() {
        return compteG;
    }

    public void setCompteG(DataModele compteG) {
        this.compteG = compteG;
    }

    public DataModele getCompteT() {
        return compteT;
    }

    public void setCompteT(DataModele compteT) {
        this.compteT = compteT;
    }

    public DataModele getLibelle() {
        return libelle;
    }

    public void setLibelle(DataModele libelle) {
        this.libelle = libelle;
    }

    public DataModele getDebit() {
        return debit;
    }

    public void setDebit(DataModele debit) {
        this.debit = debit;
    }

    public DataModele getCredit() {
        return credit;
    }

    public void setCredit(DataModele credit) {
        this.credit = credit;
    }

    public DataModele getEcheance() {
        return echeance;
    }

    public void setEcheance(DataModele echeance) {
        this.echeance = echeance;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ContentModeleSaisie other = (ContentModeleSaisie) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
