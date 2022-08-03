/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Conditionnement;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.production.base.EquipeProduction;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@SessionScoped
@ManagedBean
public class DeclarationProduction implements Serializable {

    private long id;
    private double quantite;
    private double cout;
    private boolean calculPr;
    private char statut = Constantes.STATUT_DOC_VALIDE;
    private OrdreFabrication ordre = new OrdreFabrication();
    private Conditionnement conditionnement = new Conditionnement();
    private SessionProd sessionOf = new SessionProd();
    private Date dateSave = new Date();
    private boolean terminer;
    private boolean recalculCoutOf = true;

    public DeclarationProduction() {
    }

    public DeclarationProduction(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCalculPr() {
        return calculPr;
    }

    public void setCalculPr(boolean calculPr) {
        this.calculPr = calculPr;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public char getStatut() {
        return statut != ' ' ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public OrdreFabrication getOrdre() {
        return ordre;
    }

    public void setOrdre(OrdreFabrication ordre) {
        this.ordre = ordre;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

//    public EquipeProduction getEquipe() {
//        return equipe;
//    }
//
//    public void setEquipe(EquipeProduction equipe) {
//        this.equipe = equipe;
//    }
//
//    public TrancheHoraire getTranche() {
//        return tranche;
//    }
//
//    public void setTranche(TrancheHoraire tranche) {
//        this.tranche = tranche;
//    }
    public void setCout(double cout) {
        this.cout = cout;
    }

    public double getCout() {
        return cout;
    }

    public boolean isTerminer() {
        return terminer;
    }

    public void setTerminer(boolean terminer) {
        this.terminer = terminer;
    }

    public SessionProd getSessionOf() {
        return sessionOf;
    }

    public void setSessionOf(SessionProd sessionOf) {
        this.sessionOf = sessionOf;
    }

    public boolean isRecalculCoutOf() {
        return recalculCoutOf;
    }

    public void setRecalculCoutOf(boolean recalculCoutOf) {
        this.recalculCoutOf = recalculCoutOf;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final DeclarationProduction other = (DeclarationProduction) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
