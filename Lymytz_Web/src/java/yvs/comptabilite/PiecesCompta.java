/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Journaux;
import yvs.entity.compta.YvsComptaContentJournal;
import yvs.mutuelle.Exercice;
import yvs.util.Constantes;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class PiecesCompta implements Serializable {

    private long id;
    private String numPiece;
    private String reference;
    private Journaux journal = new Journaux();
    private Date mois;
    private Date dateSaise, datePiece = new Date();
    private char statutPiece = Constantes.STATUT_DOC_EDITABLE;
    private Exercice exercice = new Exercice();
    private ModelesSasie model = new ModelesSasie();
    private List<YvsComptaContentJournal> contentsPieces;
    private double solde, credits, debits;

    public PiecesCompta() {
        contentsPieces = new ArrayList<>();
    }

    public ModelesSasie getModel() {
        return model;
    }

    public void setModel(ModelesSasie model) {
        this.model = model;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public double getDebits() {
        return debits;
    }

    public void setDebits(double debits) {
        this.debits = debits;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public Date getDateSaise() {
        return dateSaise;
    }

    public void setDateSaise(Date dateSaise) {
        this.dateSaise = dateSaise;
    }

    public Date getDatePiece() {
        return datePiece;
    }

    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    public char getStatutPiece() {
        return !String.valueOf(statutPiece).trim().equals("") ? statutPiece : Constantes.STATUT_DOC_EDITABLE;
    }

    public void setStatutPiece(char statut_piece) {
        this.statutPiece = statut_piece;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Journaux getJournal() {
        return journal;
    }

    public void setJournal(Journaux journal) {
        this.journal = journal;
    }

    public List<YvsComptaContentJournal> getContentsPieces() {
        if (contentsPieces != null) {
            Collections.sort(contentsPieces);
        }
        return contentsPieces;
    }

    public void setContentsPieces(List<YvsComptaContentJournal> contentsPieces) {
        this.contentsPieces = contentsPieces;
    }

    public Date getMois() {
        return mois;
    }

    public void setMois(Date mois) {
        this.mois = mois;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PiecesCompta other = (PiecesCompta) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public boolean isAutomatique() {
        if (contentsPieces != null ? !contentsPieces.isEmpty() : false) {
            for (YvsComptaContentJournal c : contentsPieces) {
                if (c.getRefExterne() != null ? c.getRefExterne() > 0 : false) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canEditable() {
        return getStatutPiece() == Constantes.STATUT_DOC_ATTENTE || getStatutPiece() == Constantes.STATUT_DOC_EDITABLE;
    }

    public boolean canDelete() {
        return getStatutPiece() == Constantes.STATUT_DOC_ATTENTE || getStatutPiece() == Constantes.STATUT_DOC_EDITABLE || getStatutPiece() == Constantes.STATUT_DOC_SUSPENDU || getStatutPiece() == Constantes.STATUT_DOC_ANNULE;
    }

}
