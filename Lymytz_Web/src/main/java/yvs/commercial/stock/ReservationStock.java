/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.stock;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.produits.Articles;
import yvs.base.produits.Conditionnement;
import yvs.commercial.vente.DocVente;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ReservationStock implements Serializable {

    private long id;
    private String numReference;
    private String numExterne;
    private Date dateReservation;
    private Date dateEcheance;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private String statut;
    private boolean actif;
    private String description;
    private double quantite;
    private Depots depot = new Depots();
    private Articles article = new Articles();
    private DocVente vente = new DocVente();
    private Conditionnement conditionnement = new Conditionnement();

    public ReservationStock() {
    }

    public ReservationStock(String numExterne, Depots depot, Articles article, Conditionnement conditionnement, double quantite) {
        this.numExterne = numExterne;
        this.quantite = quantite;
        this.depot = depot;
        this.article = article;
        this.conditionnement = conditionnement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Conditionnement getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(Conditionnement conditionnement) {
        this.conditionnement = conditionnement;
    }

    public long getId() {
        return id;
    }

    public String getNumExterne() {
        return numExterne;
    }

    public void setNumExterne(String numExterne) {
        this.numExterne = numExterne;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateReservation() {
        return dateReservation != null ? dateReservation : new Date();
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Date getDateEcheance() {
        return dateEcheance != null ? dateEcheance : new Date();
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getNumReference() {
        return numReference;
    }

    public void setNumReference(String numReference) {
        this.numReference = numReference;
    }

    public String getStatut() {
        return statut != null ? (statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE) : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public DocVente getVente() {
        return vente;
    }

    public void setVente(DocVente vente) {
        this.vente = vente;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ReservationStock other = (ReservationStock) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public boolean canEditable() {
        return getStatut().equals(Constantes.ETAT_ATTENTE) || getStatut().equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return getStatut().equals(Constantes.ETAT_ATTENTE) || getStatut().equals(Constantes.ETAT_EDITABLE) || getStatut().equals(Constantes.ETAT_SUSPENDU) || getStatut().equals(Constantes.ETAT_ANNULE);
    }

}
