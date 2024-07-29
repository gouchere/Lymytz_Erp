/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.tresorerie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.Comptes;
import yvs.base.compta.ModelReglement;
import yvs.comptabilite.caisse.PlanDecoupage;
import yvs.base.tiers.Tiers;
import yvs.commercial.param.TypeDocDivers;
import yvs.comptabilite.caisse.Caisses;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.YvsComptaAbonementDocDivers;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaisseDocDiversTiers;
import yvs.entity.compta.divers.YvsComptaCentreDocDivers;
import yvs.entity.compta.divers.YvsComptaCoutSupDocDivers;
import yvs.entity.compta.divers.YvsComptaTaxeDocDivers;
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;
import yvs.parametrage.agence.Agence;
import yvs.users.Users;
import yvs.util.Constantes;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class DocCaissesDivers implements Serializable, Comparable {

    private long id;
    private String numPiece;
    private String mouvement = Constantes.COMPTA_DEPENSE;
    private String description, reference, numeroExterne, beneficiaire, libelleComptable;
    private char statutRegle = Constantes.STATUT_DOC_ATTENTE;
    private boolean actif = true, montantTtc = true, comptabilise = false;
    private Date dateDoc = new Date(), dateDebutPlan = new Date();
    private Date datePaiementPrevu = new Date(), dateCloturer;
    private double montant, taxe, cout, montantHt, montantTotal, montantWithCout;
    private double totalPlanifie;   //montant total des pièces de caisses généré
    private int etapeValide=0, etapeTotal;
    private Tiers tiers = new Tiers();
    private Caisses caisseDefaut = new Caisses();
    private Caisses caisse = new Caisses();
    private long idTiers;
    private String tableTiers;
    private ModelReglement planReglement = new ModelReglement();
    private PlanDecoupage planAbonement = new PlanDecoupage();
    private Comptes compte = new Comptes();
    private Agence agence = new Agence();
    private TypeDocDivers typeDocDiv = new TypeDocDivers();
    private DocCaissesDivers parent;
    private String statutDoc = Constantes.ETAT_EDITABLE;
    private List<YvsComptaCaissePieceDivers> reglements;
    private List<YvsComptaAbonementDocDivers> abonnements;
    private List<YvsComptaCaisseDocDivers> documents;
    private Date dateSave = new Date();
    private Users author = new Users();
    private List<YvsWorkflowValidDocCaisse> etapesValidations;
    private List<YvsComptaCoutSupDocDivers> couts;
    private List<YvsComptaTaxeDocDivers> taxes;
    private List<YvsComptaCentreDocDivers> sections;
    private List<YvsComptaCaisseDocDiversTiers> listTiers;
    private String firstEtape = "VALIDER";

    public DocCaissesDivers() {
        couts = new ArrayList<>();
        taxes = new ArrayList<>();
        reglements = new ArrayList<>();
        etapesValidations = new ArrayList<>();
        abonnements = new ArrayList<>();
        documents = new ArrayList<>();
        sections = new ArrayList<>();
        listTiers = new ArrayList<>();
    }

    public DocCaissesDivers(long id) {
        this();
        this.id = id;
    }

    public DocCaissesDivers(String numDoc) {
        this();
        this.numPiece = numDoc;
    }

    public DocCaissesDivers(long id, String numDoc) {
        this(id);
        this.numPiece = numDoc;
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public Date getDateDebutPlan() {
        return dateDebutPlan != null ? dateDebutPlan : new Date();
    }

    public void setDateDebutPlan(Date dateDebutPlan) {
        this.dateDebutPlan = dateDebutPlan;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstEtape() {
        return firstEtape;
    }

    public void setFirstEtape(String firstEtape) {
        this.firstEtape = firstEtape;
    }

    public int getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(int etapeValide) {
        this.etapeValide = etapeValide;
    }

    public int getEtapeTotal() {
        return etapeTotal;
    }

    public void setEtapeTotal(int etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public String getNumeroExterne() {
        return numeroExterne;
    }

    public void setNumeroExterne(String numeroExterne) {
        this.numeroExterne = numeroExterne;
    }

    public List<YvsComptaCentreDocDivers> getSections() {
        return sections;
    }

    public void setSections(List<YvsComptaCentreDocDivers> sections) {
        this.sections = sections;
    }

    public double getMontantHt() {
        return montantHt;
    }

    public void setMontantHt(double montantHt) {
        this.montantHt = montantHt;
    }

    public void setTotalPlanifie(double totalPlanifie) {
        this.totalPlanifie = totalPlanifie;
    }

    public double getTotalPlanifie() {
        return totalPlanifie;
    }

    public Date getDateCloturer() {
        return dateCloturer;
    }

    public void setDateCloturer(Date dateCloturer) {
        this.dateCloturer = dateCloturer;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getMouvement() {
        return mouvement != null ? mouvement.trim().length() > 0 ? mouvement : "D" : "D";
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public List<YvsComptaCaissePieceDivers> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceDivers> reglements) {
        this.reglements = reglements;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public String getStatutDoc() {
        return statutDoc != null ? statutDoc.trim().length() > 0 ? statutDoc : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatutDoc(String statutDoc) {
        this.statutDoc = statutDoc;
    }

    public List<YvsComptaAbonementDocDivers> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(List<YvsComptaAbonementDocDivers> abonnements) {
        this.abonnements = abonnements;
    }

    public Date getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    public Caisses getCaisseDefaut() {
        return caisseDefaut;
    }

    public void setCaisseDefaut(Caisses caisseDefaut) {
        this.caisseDefaut = caisseDefaut;
    }

    public boolean isMontantTtc() {
        return true;
    }

    public void setMontantTtc(boolean montantTtc) {
        this.montantTtc = montantTtc;
    }

    public char getStatutRegle() {
        return statutRegle;
    }

    public void setStatutRegle(char statutRegle) {
        this.statutRegle = statutRegle;
    }

    public double getResteAPlanifier() {
        return (montant + getCout()) - totalPlanifie;
    }

    public double getMontantTotal() {
        return (montant + getCout());
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantWithCout() {
        cout = 0;
        for (YvsComptaCoutSupDocDivers t : couts) {
            if (t.getLierTiers()) {
                cout += t.getMontant();
            }
        }
        return montant + cout;
    }

    public void setMontantWithCout(double montantWithCout) {
        this.montantWithCout = montantWithCout;
    }

    public String getLibelleTypeDoc(String codeType) {
        if (codeType != null) {
            switch (codeType) {
                case "RD":
                    return "RECETTES DIVERSES";
                case "RTD":
                    return "APPROVISSIONNEMENT TIERS";
                case "CD":
                    return "CHARGES DIVERSES";
                case "CTD":
                    return "CHARGES TIERS";
            }
        }
        return "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Date getDatePaiementPrevu() {
        return datePaiementPrevu != null ? datePaiementPrevu : new Date();
    }

    public void setDatePaiementPrevu(Date datePaiementPrevu) {
        this.datePaiementPrevu = datePaiementPrevu;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public Comptes getCompte() {
        return compte;
    }

    public void setCompte(Comptes compte) {
        this.compte = compte;
    }

    public PlanDecoupage getPlanAbonement() {
        return planAbonement;
    }

    public void setPlanAbonement(PlanDecoupage planAbonement) {
        this.planAbonement = planAbonement;
    }

    public ModelReglement getPlanReglement() {
        return planReglement;
    }

    public void setPlanReglement(ModelReglement planReglement) {
        this.planReglement = planReglement;
    }

    public String giveTexteStatut(String statut) {
        if (!"".equals(statut)) {
            switch (statut) {
                case Constantes.ETAT_ATTENTE:
                    return "En attente";
                case Constantes.ETAT_CLOTURE:
                    return "Cloturé";
                case Constantes.ETAT_EDITABLE:
                    return "Editable";
                case Constantes.ETAT_ENCOURS:
                    return "En cours";
                case Constantes.ETAT_REGLE:
                    return "Payé";
                case Constantes.ETAT_SUSPENDU:
                case Constantes.ETAT_ANNULE:
                    return "Suspendu";
                case Constantes.ETAT_TERMINE:
                    return "Terminé";
                case Constantes.ETAT_VALIDE:
                    return "Validé";
            }
        }
        return "";
    }

    public List<YvsWorkflowValidDocCaisse> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidDocCaisse> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public double getTaxe() {
        taxe = 0;
        for (YvsComptaTaxeDocDivers t : taxes) {
            taxe += t.getMontant();
        }
        return taxe;
    }

    public void setTaxe(double taxe) {
        this.taxe = taxe;
    }

    public double getCout() {
        cout = 0;
        for (YvsComptaCoutSupDocDivers t : couts) {
            if (!t.getLierTiers()) {
                cout += t.getMontant();
            }
        }
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public List<YvsComptaCoutSupDocDivers> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsComptaCoutSupDocDivers> couts) {
        this.couts = couts;
    }

    public List<YvsComptaTaxeDocDivers> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<YvsComptaTaxeDocDivers> taxes) {
        this.taxes = taxes;
    }

    public DocCaissesDivers getParent() {
        return parent;
    }

    public void setParent(DocCaissesDivers parent) {
        this.parent = parent;
    }

    public List<YvsComptaCaisseDocDivers> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComptaCaisseDocDivers> documents) {
        this.documents = documents;
    }

    public double getTotalCentre() {
        double re = 0;
        for (YvsComptaCentreDocDivers cdd : sections) {
            re += cdd.getMontant();
        }
        return re;
    }

    public TypeDocDivers getTypeDocDiv() {
        return typeDocDiv;
    }

    public void setTypeDocDiv(TypeDocDivers typeDocDiv) {
        this.typeDocDiv = typeDocDiv;
    }

    public String getLibelleComptable() {
        return libelleComptable;
    }

    public void setLibelleComptable(String libelleComptable) {
        this.libelleComptable = libelleComptable;
    }

    public long getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(long idTiers) {
        this.idTiers = idTiers;
    }

    public String getTableTiers() {
        return tableTiers;
    }

    public void setTableTiers(String tableTiers) {
        this.tableTiers = tableTiers;
    }

    public List<YvsComptaCaisseDocDiversTiers> getListTiers() {
        return listTiers;
    }

    public void setListTiers(List<YvsComptaCaisseDocDiversTiers> listTiers) {
        this.listTiers = listTiers;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final DocCaissesDivers other = (DocCaissesDivers) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }    

    @Override
    public int compareTo(Object o) {
        DocCaissesDivers d = (DocCaissesDivers) o;
        if (datePaiementPrevu.equals(d.datePaiementPrevu)) {
            Long.valueOf(id).compareTo(d.id);
        }
        return datePaiementPrevu.compareTo(d.datePaiementPrevu);
    }

    public boolean canEditable() {
        return statutDoc.equals(Constantes.ETAT_ATTENTE) || statutDoc.equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return statutDoc.equals(Constantes.ETAT_ATTENTE) || statutDoc.equals(Constantes.ETAT_EDITABLE) || statutDoc.equals(Constantes.ETAT_SUSPENDU) || statutDoc.equals(Constantes.ETAT_ANNULE);
    }

}
