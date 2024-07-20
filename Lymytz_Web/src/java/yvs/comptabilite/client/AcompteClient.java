/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.ModeDeReglement;
import yvs.commercial.client.Client;
import yvs.comptabilite.caisse.Caisses;
import yvs.entity.compta.vente.YvsComptaPhaseAcompteVente;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class AcompteClient implements Serializable {

    private long id;
    private double montant, solde, reste;
    private Date dateAcompte = new Date();
    private Date dateSave = new Date();
    private String numRefrence;
    private String commentaire;
    private boolean comptabilise;
    private double resteUnBlind;
    private char nature = 'A';
    private char statut = Constantes.STATUT_DOC_ATTENTE;
    private char statutNotif = Constantes.STATUT_DOC_ATTENTE;
    private boolean repartirAutomatique = true;
    private String referenceExterne;
    private Client client = new Client();
    private Caisses caisse = new Caisses();
    private ModeDeReglement mode = new ModeDeReglement();
    private List<YvsComptaPhaseAcompteVente> phasesReglement;
    private List<AcomptesVenteDivers> ventesEtDivers;
    private String firstEtape = "VALIDER";

    public AcompteClient() {
        phasesReglement = new ArrayList<>();
        ventesEtDivers = new ArrayList<>();
    }

    public AcompteClient(long id) {
        this();
        this.id = id;
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public String getFirstEtape() {
        return firstEtape;
    }

    public void setFirstEtape(String firstEtape) {
        this.firstEtape = firstEtape;
    }

    public double getResteUnBlind() {
        return resteUnBlind;
    }

    public void setResteUnBlind(double resteUnBlind) {
        this.resteUnBlind = resteUnBlind;
    }

    public ModeDeReglement getMode() {
        return mode;
    }

    public void setMode(ModeDeReglement mode) {
        this.mode = mode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDateAcompte() {
        return dateAcompte != null ? dateAcompte : new Date();
    }

    public void setDateAcompte(Date dateAcompte) {
        this.dateAcompte = dateAcompte;
    }

    public String getNumRefrence() {
        return numRefrence;
    }

    public void setNumRefrence(String numRefrence) {
        this.numRefrence = numRefrence;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public boolean isRepartirAutomatique() {
        return repartirAutomatique;
    }

    public void setRepartirAutomatique(boolean repartirAutomatique) {
        this.repartirAutomatique = repartirAutomatique;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
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

    public char getNature() {
        return Character.valueOf(nature) != null ? String.valueOf(nature).trim().length() > 0 ? nature : 'A' : 'A';
    }

    public void setNature(char nature) {
        this.nature = nature;
    }

    public char getStatut() {
        return Character.valueOf(statut) != null ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public char getStatutNotif() {
        return Character.valueOf(statutNotif) != null ? String.valueOf(statutNotif).trim().length() > 0 ? statutNotif : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutNotif(char statutNotif) {
        this.statutNotif = statutNotif;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsComptaPhaseAcompteVente> getPhasesReglement() {
        return phasesReglement;
    }

    public void setPhasesReglement(List<YvsComptaPhaseAcompteVente> phasesReglement) {
        this.phasesReglement = phasesReglement;
    }

    public List<AcomptesVenteDivers> getVentesEtDivers() {
        return ventesEtDivers;
    }

    public void setVentesEtDivers(List<AcomptesVenteDivers> ventesEtDivers) {
        this.ventesEtDivers = ventesEtDivers;
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
        final AcompteClient other = (AcompteClient) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
