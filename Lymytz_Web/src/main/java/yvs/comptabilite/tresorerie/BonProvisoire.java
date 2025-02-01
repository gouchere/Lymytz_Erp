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
import yvs.base.tiers.Tiers;
import yvs.commercial.param.TypeDocDivers;
import yvs.comptabilite.caisse.Caisses;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaJustifBonMission;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.divers.YvsComptaJustifBonAchat;
import yvs.entity.compta.divers.YvsComptaJustificatifBon;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.param.workflow.YvsWorkflowValidBonProvisoire;
import yvs.users.Users;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class BonProvisoire implements Serializable {

    private long id;
    private String numero, numeroExterne, firstEtape = "VALIDER";
    private Date dateBon = new Date();
    private Date dateValider, datePayer, dateJustify;
    private char statut;
    private char statutPaiement;
    private char statutJustify;
    private double montant, justifier, attente, reste;
    private String description, bonFor = "O", oldBonFor = bonFor;
    private int etapeTotal;
    private int etapeValide;
    private boolean error, associed;
    private Date dateUpdate = new Date();
    private Date dateSave = new Date();
    private String beneficiaire;
    private Caisses caisse = new Caisses();
    private Users caissier = new Users();
    private Tiers tiers = new Tiers();
    private Users ordonnateur = new Users();
    private TypeDocDivers typeDoc = new TypeDocDivers();
//    private List<YvsComptaJustificatifBon> justificatifs;    
    private List<JustifierBon> justificatifs;
    private List<YvsWorkflowValidBonProvisoire> etapesValidations;

    private YvsComptaCaisseDocDivers divers = new YvsComptaCaisseDocDivers();
    private YvsComptaCaissePieceDivers pieceDivers = new YvsComptaCaissePieceDivers();
    private YvsComptaJustificatifBon bonDivers = new YvsComptaJustificatifBon();

    private YvsGrhMissions mission = new YvsGrhMissions();
    private YvsComptaCaissePieceMission pieceMission = new YvsComptaCaissePieceMission();
    private YvsComptaJustifBonMission bonMission = new YvsComptaJustifBonMission();
    
    private YvsComDocAchats achat = new YvsComDocAchats();
    private YvsComptaJustifBonAchat bonAchat = new YvsComptaJustifBonAchat();
    private YvsComptaCaissePieceAchat pieceAchat = new YvsComptaCaissePieceAchat();

    public BonProvisoire() {
        justificatifs = new ArrayList<>();
        etapesValidations = new ArrayList<>();
    }

    public BonProvisoire(long id) {
        this();
        this.id = id;
    }

    public BonProvisoire(long id, String numero) {
        this(id);
        this.numero = numero;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAssocied() {
        return associed;
    }

    public void setAssocied(boolean associed) {
        this.associed = associed;
    }

    public String getNumero() {
        return numero != null ? numero : "";
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroExterne() {
        return numeroExterne;
    }

    public void setNumeroExterne(String numeroExterne) {
        this.numeroExterne = numeroExterne;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateBon() {
        return dateBon;
    }

    public void setDateBon(Date dateBon) {
        this.dateBon = dateBon;
    }

    public Date getDateJustify() {
        return dateJustify;
    }

    public void setDateJustify(Date dateJustify) {
        this.dateJustify = dateJustify;
    }

    public char getStatut() {
        return statut != ' ' ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_EDITABLE : Constantes.STATUT_DOC_EDITABLE;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public char getStatutPaiement() {
        return statutPaiement != ' ' ? String.valueOf(statutPaiement).trim().length() > 0 ? statutPaiement : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutPaiement(char statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public char getStatutJustify() {
        return statutJustify != ' ' ? String.valueOf(statutJustify).trim().length() > 0 ? statutJustify : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutJustify(char statutJustify) {
        this.statutJustify = statutJustify;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEtapeTotal() {
        return etapeTotal;
    }

    public void setEtapeTotal(int etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public int getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(int etapeValide) {
        this.etapeValide = etapeValide;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getBeneficiaire() {
        return beneficiaire != null ? beneficiaire : "";
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    public Users getCaissier() {
        return caissier;
    }

    public void setCaissier(Users caissier) {
        this.caissier = caissier;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public Users getOrdonnateur() {
        return ordonnateur;
    }

    public void setOrdonnateur(Users ordonnateur) {
        this.ordonnateur = ordonnateur;
    }

    public String getBonFor() {
        return bonFor != null ? bonFor.trim().length() > 0 ? bonFor : "O" : "O";
    }

    public void setBonFor(String bonFor) {
        this.bonFor = bonFor;
    }

    public String getOldBonFor() {
        return oldBonFor != null ? oldBonFor.trim().length() > 0 ? oldBonFor : "O" : "O";
    }

    public void setOldBonFor(String oldBonFor) {
        this.oldBonFor = oldBonFor;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

//    public List<YvsComptaJustificatifBon> getJustificatifs() {
//        return justificatifs;
//    }
//
//    public void setJustificatifs(List<YvsComptaJustificatifBon> justificatifs) {
//        this.justificatifs = justificatifs;
//    }
    public List<JustifierBon> getJustificatifs() {
        return justificatifs;
    }

    public void setJustificatifs(List<JustifierBon> justificatifs) {
        this.justificatifs = justificatifs;
    }

    public List<YvsWorkflowValidBonProvisoire> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidBonProvisoire> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public YvsComptaJustifBonMission getBonMission() {
        return bonMission;
    }

    public void setBonMission(YvsComptaJustifBonMission bonMission) {
        this.bonMission = bonMission;
    }

    public YvsComptaJustifBonAchat getBonAchat() {
        return bonAchat;
    }

    public void setBonAchat(YvsComptaJustifBonAchat bonAchat) {
        this.bonAchat = bonAchat;
    }

    public YvsGrhMissions getMission() {
        return mission;
    }

    public void setMission(YvsGrhMissions mission) {
        this.mission = mission;
    }

    public YvsComDocAchats getAchat() {
        return achat;
    }

    public void setAchat(YvsComDocAchats achat) {
        this.achat = achat;
    }

    public YvsComptaCaissePieceMission getPieceMission() {
        return pieceMission;
    }

    public void setPieceMission(YvsComptaCaissePieceMission pieceMission) {
        this.pieceMission = pieceMission;
    }

    public YvsComptaCaissePieceAchat getPieceAchat() {
        return pieceAchat;
    }

    public void setPieceAchat(YvsComptaCaissePieceAchat pieceAchat) {
        this.pieceAchat = pieceAchat;
    }

    public String getFirstEtape() {
        return firstEtape;
    }

    public void setFirstEtape(String firstEtape) {
        this.firstEtape = firstEtape;
    }

    public double getJustifier() {
        return justifier;
    }

    public void setJustifier(double justifier) {
        this.justifier = justifier;
    }

    public double getAttente() {
        return attente;
    }

    public void setAttente(double attente) {
        this.attente = attente;
    }

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public Date getDateValider() {
        return dateValider;
    }

    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    public Date getDatePayer() {
        return datePayer;
    }

    public void setDatePayer(Date datePayer) {
        this.datePayer = datePayer;
    }

    public TypeDocDivers getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(TypeDocDivers typeDoc) {
        this.typeDoc = typeDoc;
    }

    public YvsComptaCaisseDocDivers getDivers() {
        return divers;
    }

    public void setDivers(YvsComptaCaisseDocDivers divers) {
        this.divers = divers;
    }

    public YvsComptaCaissePieceDivers getPieceDivers() {
        return pieceDivers;
    }

    public void setPieceDivers(YvsComptaCaissePieceDivers pieceDivers) {
        this.pieceDivers = pieceDivers;
    }

    public YvsComptaJustificatifBon getBonDivers() {
        return bonDivers;
    }

    public void setBonDivers(YvsComptaJustificatifBon bonDivers) {
        this.bonDivers = bonDivers;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final BonProvisoire other = (BonProvisoire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public boolean canEditable() {
        return getStatut() == Constantes.STATUT_DOC_ATTENTE || getStatut() == Constantes.STATUT_DOC_EDITABLE;
    }

    public boolean canDelete() {
        return getStatut() == Constantes.STATUT_DOC_ATTENTE || getStatut() == Constantes.STATUT_DOC_EDITABLE || getStatut() == Constantes.STATUT_DOC_SUSPENDU || getStatut() == Constantes.STATUT_DOC_ANNULE;
    }

    public boolean canJustify() {
        if (bonMission != null ? bonMission.getId() > 0 : false) {
            return true;
        }
        if (bonAchat != null ? bonAchat.getId() > 0 : false) {
            return true;
        }
        if (bonDivers != null ? bonDivers.getId() > 0 : false) {
            return true;
        }
        if (justificatifs != null ? !justificatifs.isEmpty() : false) {
            return true;
        }
        return false;
    }

    public boolean mustJustify() {
        if (pieceAchat != null ? pieceAchat.getId() > 0 : false) {
            return true;
        }
        if (pieceMission != null ? pieceMission.getId() > 0 : false) {
            return true;
        }
        if (pieceDivers != null ? pieceDivers.getId() > 0 : false) {
            return true;
        }
        return false;
    }

}
