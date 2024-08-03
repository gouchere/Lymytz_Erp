/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean.mission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.ModelReglement;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.analytique.YvsComptaCentreMission;
import yvs.entity.grh.activite.YvsGrhFraisMission;
import yvs.entity.grh.activite.YvsGrhMissionRessource;
import yvs.entity.param.workflow.YvsWorkflowValidMission;
import yvs.grh.bean.Employe;
import yvs.parametrage.dico.Dictionnaire;
import yvs.util.Constantes;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped 
public class Mission implements Serializable {

    private long id;
    private String ordre;
    private String reference;
    private String transport;
    private String role;
    private Date dateRetour, dateDebut, dateFin;
    private Date heureDepart = new Date(), heureArrive = new Date();
    private Date dateValider;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private List<YvsGrhMissionRessource> ressources;
    private List<YvsGrhFraisMission> fraisMissions;
    private List<YvsComptaCaissePieceMission> reglements;
    private List<YvsComptaCentreMission> analytiques;
    private List<PieceTresorerie> piecesCaisses;
    private Employe employe = new Employe();
    private Dictionnaire lieu = new Dictionnaire();
    private ModelReglement modeReglement = new ModelReglement();
    private boolean actif, cloturer;
    private List<YvsWorkflowValidMission> etapesValidations;
    private GrilleFraisMission FraisMission = new GrilleFraisMission();
    private char statutMission = Constantes.STATUT_DOC_ATTENTE;   //E=Editable V=Validé C=cloturé  A=Annulé
    private char statutPaiement = Constantes.STATUT_DOC_ATTENTE;   //W=en attente R=en cours P=payé
    private double totalFraisMission, restPlanifier, totalBon, totalBonPaye;
    private String lieuEscale;
    private String author;
    private Date dateFinPrevu;
    private String motifReport;
    private String numeroMission;
    private double totalPaye;
    private double totalPiece, totalPiecePaye;
    private int dureeMission;
    private int etapeValide, etapeTotal;
    private ObjetMission objet = new ObjetMission();

    public Mission() {
        fraisMissions = new ArrayList<>();
        ressources = new ArrayList<>();
        etapesValidations = new ArrayList<>();
        reglements = new ArrayList<>();
        piecesCaisses = new ArrayList<>();
        analytiques = new ArrayList<>();
    }

    public Mission(long id) {
        this();
        this.id = id;
    }

    public double getTotalBon() {
        return totalBon;
    }

    public void setTotalBon(double totalBon) {
        this.totalBon = totalBon;
    }

    public double getTotalPiece() {
        return totalPiece;
    }

    public void setTotalPiece(double totalPiece) {
        this.totalPiece = totalPiece;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public Date getDateValider() {
        return dateValider;
    }

    public double getRestPlanifier() {
        return restPlanifier;
    }

    public void setRestPlanifier(double restPlanifier) {
        this.restPlanifier = restPlanifier;
    }

    public ModelReglement getModeReglement() {
        return modeReglement;
    }

    public void setModeReglement(ModelReglement modeReglement) {
        this.modeReglement = modeReglement;
    }

    public List<YvsComptaCaissePieceMission> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceMission> reglements) {
        this.reglements = reglements;
    }

    public Date getDateFinPrevu() {
        return dateFinPrevu;
    }

    public void setDateFinPrevu(Date dateFinPrevu) {
        this.dateFinPrevu = dateFinPrevu;
    }

    public String getMotifReport() {
        return motifReport;
    }

    public void setMotifReport(String motifReport) {
        this.motifReport = motifReport;
    }

    public char getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(char statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public double getTotalFraisMission() {
//        totalFraisMission = 0;
//        if (fraisMissions != null) {
//            for (YvsGrhFraisMission fm : fraisMissions) {
//                totalFraisMission += fm.getMontant();
//            }
//        }
        return totalFraisMission;
    }

    public void setTotalFraisMission(double totalFraisMission) {
        this.totalFraisMission = totalFraisMission;
    }

    public List<YvsGrhFraisMission> getFraisMissions() {
        return fraisMissions;
    }

    public void setFraisMissions(List<YvsGrhFraisMission> fraisMissions) {
        this.fraisMissions = fraisMissions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrdre() {
        return ordre;
    }

    public void setOrdre(String ordre) {
        this.ordre = ordre;
    }

    public Dictionnaire getLieu() {
        return lieu;
    }

    public void setLieu(Dictionnaire lieu) {
        this.lieu = lieu;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
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

    public char getStatutMission() {
        return statutMission;
    }

    public void setStatutMission(char statutMission) {
        this.statutMission = statutMission;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public List<YvsGrhMissionRessource> getRessources() {
        return ressources;
    }

    public void setRessources(List<YvsGrhMissionRessource> ressources) {
        this.ressources = ressources;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<YvsWorkflowValidMission> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidMission> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public GrilleFraisMission getFraisMission() {
        return FraisMission;
    }

    public void setFraisMission(GrilleFraisMission FraisMission) {
        this.FraisMission = FraisMission;
    }

    public boolean isCloturer() {
        return cloturer;
    }

    public void setCloturer(boolean cloturer) {
        this.cloturer = cloturer;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }

    public Date getHeureArrive() {
        return heureArrive;
    }

    public void setHeureArrive(Date heureArrive) {
        this.heureArrive = heureArrive;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLieuEscale() {
        return lieuEscale;
    }

    public void setLieuEscale(String lieuEscale) {
        this.lieuEscale = lieuEscale;
    }

    public String getNumeroMission() {
        return numeroMission;
    }

    public void setNumeroMission(String numeroMission) {
        this.numeroMission = numeroMission;
    }

    public double getTotalPaye() {
        return totalPaye;
    }

    public void setTotalPaye(double totalPaye) {
        this.totalPaye = totalPaye;
    }

    public int getDureeMission() {
        return dureeMission;
    }

    public void setDureeMission(int dureeMission) {
        this.dureeMission = dureeMission;
    }

    public ObjetMission getObjet() {
        return objet;
    }

    public void setObjet(ObjetMission objet) {
        this.objet = objet;
    }

    public int getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(int etapeValide) {
        this.etapeValide = etapeValide;
    }

    public void setEtapeTotal(int etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public int getEtapeTotal() {
        return etapeTotal;
    }

    public double getTotalBonPaye() {
        return totalBonPaye;
    }

    public void setTotalBonPaye(double totalBonPaye) {
        this.totalBonPaye = totalBonPaye;
    }

    public double getTotalPiecePaye() {
        return totalPiecePaye;
    }

    public void setTotalPiecePaye(double totalPiecePaye) {
        this.totalPiecePaye = totalPiecePaye;
    }

    public List<PieceTresorerie> getPiecesCaisses() {
        return piecesCaisses;
    }

    public void setPiecesCaisses(List<PieceTresorerie> piecesCaisses) {
        this.piecesCaisses = piecesCaisses;
    }

    public List<YvsComptaCentreMission> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsComptaCentreMission> analytiques) {
        this.analytiques = analytiques;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Mission other = (Mission) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public boolean canEditable() {
        return statutMission == Constantes.STATUT_DOC_ATTENTE || statutMission == Constantes.STATUT_DOC_EDITABLE;
    }

    public boolean canDelete() {
        return statutMission == Constantes.STATUT_DOC_ATTENTE || statutMission == Constantes.STATUT_DOC_EDITABLE || statutMission == Constantes.STATUT_DOC_SUSPENDU || statutMission == Constantes.STATUT_DOC_ANNULE;
    }

}
