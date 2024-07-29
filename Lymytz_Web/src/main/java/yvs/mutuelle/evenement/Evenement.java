/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.evenement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultScheduleEvent;
import yvs.entity.mutuelle.evenement.YvsMutActivite;
import yvs.entity.mutuelle.evenement.YvsMutContributionEvenement;
import yvs.entity.mutuelle.evenement.YvsMutCoutEvenement;
import yvs.entity.mutuelle.evenement.YvsMutFinancementActivite;
import yvs.entity.mutuelle.evenement.YvsMutParticipantEvenement;
import yvs.mutuelle.CaisseMutuelle;
import yvs.mutuelle.Mutualiste;
import yvs.parametrage.dico.Dictionnaire;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Evenement extends DefaultScheduleEvent implements Serializable, Comparable {

    private long idEvt;
    private Date dateEvenement = new Date();
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Date heureDebut = new Date();
    private Date heureFin = new Date();
    private Date dateSave = new Date();
    private String descriptionEvt;
    private String description;
    private String etat = Constantes.ETAT_EDITABLE;
    private double montantObligatoire;
    private TypeEvenement type = new TypeEvenement();
    private Mutualiste mutualiste = new Mutualiste();
    private CaisseMutuelle caisse = new CaisseMutuelle();
    private List<YvsMutParticipantEvenement> Participants;
    private Date dateOuvertureContribution = new Date();
    private Date dateClotureContribution = new Date();
    private Dictionnaire lieu = new Dictionnaire();
    private List<YvsMutContributionEvenement> contributions;
    private List<YvsMutFinancementActivite> financements;
    private List<YvsMutCoutEvenement> couts;
    private List<YvsMutActivite> activites_all, activites;
    private double montantAttendu, montantRecu, coutTotal;
    private boolean selectActif, new_;

    public Evenement() {
        Participants = new ArrayList<>();
        financements = new ArrayList<>();
        activites_all = new ArrayList<>();
        activites = new ArrayList<>();
        contributions = new ArrayList<>();
        couts = new ArrayList<>();
    }

    public Evenement(long id) {
        this();
        this.idEvt = id;
    }

    public Evenement(long id, String description, Date dateDebut, Date heureDebut, Date dateFin, Date heureFin) {
        this(id);
        this.descriptionEvt = description;
        this.dateDebut = dateDebut;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.dateFin = dateFin;
    }

    public Evenement(Evenement evt) {
        this(evt.getIdEvt());
        this.dateEvenement = evt.getDateEvenement();
        this.dateDebut = evt.getDateDebut();
        this.dateFin = evt.getDateFin();
        this.heureFin = evt.getHeureFin();
        this.descriptionEvt = evt.getDescriptionEvt();
        this.type = evt.getType();
        this.mutualiste = evt.getMutualiste();
        this.heureDebut = evt.getHeureDebut();
        this.dateClotureContribution = evt.getDateClotureContribution();
        this.dateOuvertureContribution = evt.getDateOuvertureContribution();
        this.lieu = evt.getLieu();
        this.setDescription(evt.getDescription());
        this.setStartDate(evt.getStartDate());
        this.setEndDate(evt.getEndDate());
        this.setTitle(evt.getTitle());
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsMutFinancementActivite> getFinancements() {
        return financements;
    }

    public void setFinancements(List<YvsMutFinancementActivite> financements) {
        this.financements = financements;
    }

    public List<YvsMutActivite> getActivites() {
        return activites;
    }

    public void setActivites(List<YvsMutActivite> activites) {
        this.activites = activites;
    }

    public List<YvsMutActivite> getActivites_all() {
        return activites_all;
    }

    public void setActivites_all(List<YvsMutActivite> activites_all) {
        this.activites_all = activites_all;
    }

    public double getMontantObligatoire() {
        return montantObligatoire;
    }

    public void setMontantObligatoire(double montantObligatoire) {
        this.montantObligatoire = montantObligatoire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public double getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(double coutTotal) {
        this.coutTotal = coutTotal;
    }

    public List<YvsMutCoutEvenement> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsMutCoutEvenement> couts) {
        this.couts = couts;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public double getMontantRecu() {
        return montantRecu;
    }

    public double getMontantAttendu() {
        return montantAttendu;
    }

    public void setMontantRecu(double montantRecu) {
        this.montantRecu = montantRecu;
    }

    public void setMontantAttendu(double montantAttendu) {
        this.montantAttendu = montantAttendu;
    }

    public List<YvsMutContributionEvenement> getContributions() {
        return contributions;
    }

    public void setContributions(List<YvsMutContributionEvenement> contributions) {
        this.contributions = contributions;
    }

    public Date getDateOuvertureContribution() {
        return dateOuvertureContribution;
    }

    public void setDateOuvertureContribution(Date dateOuvertureContribution) {
        this.dateOuvertureContribution = dateOuvertureContribution;
    }

    public Date getDateClotureContribution() {
        return dateClotureContribution;
    }

    public void setDateClotureContribution(Date dateClotureContribution) {
        this.dateClotureContribution = dateClotureContribution;
    }

    public Dictionnaire getLieu() {
        return lieu;
    }

    public void setLieu(Dictionnaire lieu) {
        this.lieu = lieu;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public TypeEvenement getType() {
        return type;
    }

    public void setType(TypeEvenement type) {
        this.type = type;
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

    public long getIdEvt() {
        return idEvt;
    }

    public void setIdEvt(long id) {
        this.idEvt = id;
    }

    public Date getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(Date dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public String getDescriptionEvt() {
        return descriptionEvt;
    }

    public void setDescriptionEvt(String description) {
        this.descriptionEvt = description;
    }

    public Mutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(Mutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public List<YvsMutParticipantEvenement> getParticipants() {
        return Participants;
    }

    public void setParticipants(List<YvsMutParticipantEvenement> Participants) {
        this.Participants = Participants;
    }

    public CaisseMutuelle getCaisse() {
        return caisse;
    }

    public void setCaisse(CaisseMutuelle caisse) {
        this.caisse = caisse;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (int) (this.idEvt ^ (this.idEvt >>> 32));
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
        final Evenement other = (Evenement) obj;
        if (this.idEvt != other.idEvt) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        Evenement c = (Evenement) o;
        if (dateEvenement.equals(c.dateEvenement)) {
            Long.valueOf(idEvt).compareTo(c.idEvt);
        }
        return dateEvenement.compareTo(c.dateEvenement);
    }

}
