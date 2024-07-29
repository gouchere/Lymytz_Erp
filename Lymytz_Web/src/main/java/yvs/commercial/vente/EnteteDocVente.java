/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates 
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.commercial.creneau.CreneauUsers;
import yvs.commercial.depot.PointVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.entrepot.Depots;
import yvs.users.Users;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class EnteteDocVente implements Serializable, Comparable {

    private long id;
    private String etat = Constantes.ETAT_EDITABLE, statutLivre, statutRegle, reference, numero;
    private Date dateEntete = new Date();
    private Date dateCloturer = new Date();
    private Date dateValider = new Date();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private List<YvsComDocVentes> documents;
    private CreneauUsers crenauHoraire = new CreneauUsers();
    private TrancheHoraire tranche = new TrancheHoraire();
    private TrancheHoraire tranchePoint = new TrancheHoraire();
    private PointVente point = new PointVente();
    private Depots depot = new Depots();
    private Agence agence = new Agence();
    private Users users = new Users();
    private Users cloturerBy = new Users();
    private Users validerBy = new Users();
    private boolean comptabilise;
    private double versementAttendu, versementReel, versementEncours;
    private double montantAvance, avanceCommande, totalTTC;
    private double montantHT, montantTaxe, montantTTC, montantRemise, montantRemises, montantTotal, montantRistourne, montantCommission, montantCoutSup, montantNetApayer;
    private double montantTaxeR;    //contient le montant de la taxe des article dont le prix unitaire est un prix ttc.
    private boolean new_, update;

    public EnteteDocVente() {
        documents = new ArrayList<>();
    }

    public EnteteDocVente(long id) {
        this.id = id;
        documents = new ArrayList<>();
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDateCloturer() {
        return dateCloturer != null ? dateCloturer : new Date();
    }

    public void setDateCloturer(Date dateCloturer) {
        this.dateCloturer = dateCloturer;
    }

    public Date getDateValider() {
        return dateValider != null ? dateValider : new Date();
    }

    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public Users getCloturerBy() {
        return cloturerBy;
    }

    public void setCloturerBy(Users cloturerBy) {
        this.cloturerBy = cloturerBy;
    }

    public Users getValiderBy() {
        return validerBy;
    }

    public void setValiderBy(Users validerBy) {
        this.validerBy = validerBy;
    }

    public double getTotalTTC() {
        return totalTTC;
    }

    public void setTotalTTC(double totalTTC) {
        this.totalTTC = totalTTC;
    }

    public double getMontantRistourne() {
        return montantRistourne;
    }

    public void setMontantRistourne(double montantRistourne) {
        this.montantRistourne = montantRistourne;
    }

    public double getMontantCommission() {
        return montantCommission;
    }

    public void setMontantCommission(double montantCommission) {
        this.montantCommission = montantCommission;
    }

    public double getMontantAvance() {
        return montantAvance;
    }

    public void setMontantAvance(double montantAvance) {
        this.montantAvance = montantAvance;
    }

    public double getAvanceCommande() {
        return avanceCommande;
    }

    public void setAvanceCommande(double avanceCommande) {
        this.avanceCommande = avanceCommande;
    }

    public double getMontantCoutSup() {
        return montantCoutSup;
    }

    public void setMontantCoutSup(double montantCoutSup) {
        this.montantCoutSup = montantCoutSup;
    }

    public double getMontantRemises() {
        return montantRemises;
    }

    public void setMontantRemises(double montantRemises) {
        this.montantRemises = montantRemises;
    }

    public double getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(double montantRemise) {
        this.montantRemise = montantRemise;
    }

    public double getMontantTaxeR() {
        return montantTaxeR;
    }

    public void setMontantTaxeR(double montantTaxeR) {
        this.montantTaxeR = montantTaxeR;
    }

    public double getMontantTaxe() {
        return montantTaxe;
    }

    public void setMontantTaxe(double montantTaxe) {
        this.montantTaxe = montantTaxe;
    }

    public double getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(double montantHT) {
        this.montantHT = montantHT;
    }

    public double getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        this.montantTTC = montantTTC;
    }

    public double getMontantTotal() {
        montantTotal = getMontantTTC() + getMontantCoutSup() - getMontantRemises();
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantNetApayer() {
        montantNetApayer = getMontantTotal() - getMontantAvance();
        return montantNetApayer;
    }

    public void setMontantNetApayer(double montantNetApayer) {
        this.montantNetApayer = montantNetApayer;
    }

    public double getVersementAttendu() {
        return versementAttendu;
    }

    public void setVersementAttendu(double versementAttendu) {
        this.versementAttendu = versementAttendu;
    }

    public double getVersementReel() {
        return versementReel;
    }

    public void setVersementReel(double versementReel) {
        this.versementReel = versementReel;
    }

    public double getVersementEncours() {
        return versementEncours;
    }

    public void setVersementEncours(double versementEncours) {
        this.versementEncours = versementEncours;
    }

    public TrancheHoraire getTranchePoint() {
        return tranchePoint;
    }

    public void setTranchePoint(TrancheHoraire tranchePoint) {
        this.tranchePoint = tranchePoint;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public TrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(TrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public PointVente getPoint() {
        return point;
    }

    public void setPoint(PointVente point) {
        this.point = point;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatutLivre() {
        return statutLivre != null ? statutLivre.trim().length() > 0 ? statutLivre : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutLivre(String statutLivre) {
        this.statutLivre = statutLivre;
    }

    public String getStatutRegle() {
        return statutRegle != null ? statutRegle.trim().length() > 0 ? statutRegle : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutRegle(String statutRegle) {
        this.statutRegle = statutRegle;
    }

    public String getEtat() {
        return etat != null ? etat.trim().length() > 0 ? etat : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getDateEntete() {
        return dateEntete != null ? dateEntete : new Date();
    }

    public void setDateEntete(Date dateEntete) {
        this.dateEntete = dateEntete;
    }

    public List<YvsComDocVentes> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocVentes> documents) {
        this.documents = documents;
    }

    public CreneauUsers getCrenauHoraire() {
        return crenauHoraire;
    }

    public void setCrenauHoraire(CreneauUsers crenauHoraire) {
        this.crenauHoraire = crenauHoraire;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final EnteteDocVente other = (EnteteDocVente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        EnteteDocVente d = (EnteteDocVente) o;
        if (dateEntete.equals(d.dateEntete)) {
            return Long.valueOf(id).compareTo(d.id);
        }
        return dateEntete.compareTo(d.dateEntete);
    }

    public boolean canEditable() {
        return etat.equals(Constantes.ETAT_ATTENTE) || etat.equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return etat.equals(Constantes.ETAT_ATTENTE) || etat.equals(Constantes.ETAT_EDITABLE) || etat.equals(Constantes.ETAT_SUSPENDU) || etat.equals(Constantes.ETAT_ANNULE);
    }

}
