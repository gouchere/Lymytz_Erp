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
import yvs.base.compta.ModeDeReglement;
import yvs.base.tiers.Tiers;
import yvs.comptabilite.caisse.Caisses;
import yvs.commercial.achat.DocAchat;
import yvs.commercial.client.Client;
import yvs.commercial.commission.CommissionCommerciaux;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.vente.DocVente;
import yvs.commercial.vente.EnteteDocVente;
import yvs.entity.compta.YvsComptaBielletage;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceCompensation;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCoutSupPieceVirement;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaNotifReglementDocDivers;
import yvs.entity.compta.YvsComptaNotifReglementVente;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.compta.YvsComptaPhasePieceAchat;
import yvs.entity.compta.YvsComptaPhasePieceDivers;
import yvs.entity.compta.achat.YvsComptaNotifReglementAchat;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.divers.YvsComptaJustificatifBon;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.bean.mission.Mission;
import yvs.users.Users;
import yvs.util.Constantes;
import yvs.workflow.EtapesValidationX;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class PieceTresorerie implements Serializable, Comparable {

    private long id;
    private String description;
    private String numPiece;
    private String numRef;
    private String numRefExterne;
    private Date datePiece = new Date();
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private Date datePaiement = new Date();
    private Date datePaiementPrevu = new Date();
    private String mouvement = Constantes.MOUV_CAISS_ENTREE, nameTiers;
    private double montant;
    private char statutPiece = Constantes.STATUT_DOC_ATTENTE;
    private long idExterne;
    private long idParent;
    private String tableExterne, statutExterne;
    private String source = Constantes.SCR_ACHAT, nameTable = Constantes.SCR_ACHAT_NAME;
    private String beneficiaire;
    private ModeDeReglement mode = new ModeDeReglement();
    private Users caissier = new Users();
    private Users caissierCible = new Users();
    private Users valideBy = new Users();
    private Client clients = new Client();
    private Fournisseur fournisseur = new Fournisseur();
    private EnteteDocVente enteteDoc = new EnteteDocVente();
    private Date dateValide = new Date();
    private Caisses caisse = new Caisses();
    private Caisses otherCaisse = new Caisses();
    private Tiers tiers = new Tiers();
    private double montantTotal;
    private double montantAvance;
    private double montantCout;
    private double montantPlanifie;
    private boolean error, acompte, comptabilise;
    private boolean new_, update, bon, justificatif;
    private DocVente docVente = new DocVente();
    private DocAchat docAchat = new DocAchat();
    private Mission mission = new Mission();
    private CommissionCommerciaux commission = new CommissionCommerciaux();
    private DocCaissesDivers docDivers = new DocCaissesDivers();
    private BonProvisoire bonProvisoire = new BonProvisoire();
    private YvsComptaJustificatifBon justify = new YvsComptaJustificatifBon();
    private YvsComptaNotifReglementVente notifRegVente = new YvsComptaNotifReglementVente();
    private YvsComptaNotifReglementAchat notifRegAchat = new YvsComptaNotifReglementAchat();
    private YvsComptaNotifReglementDocDivers notifRegDivers = new YvsComptaNotifReglementDocDivers();
    private YvsUsersAgence author = new YvsUsersAgence();

    private List<YvsComptaMouvementCaisse> listOthersPiece;  //pièces de caisses provenant de la même source
    private List<EtapesValidationX> etapesValidation;
    private List<YvsComptaPhasePiece> phases;
    private List<YvsComptaPhasePieceAchat> phasesAchat;
    private List<YvsComptaPhasePieceDivers> phasesDivers;
    private List<YvsComptaCaissePieceDivers> sousDivers;
    private List<YvsComptaCaissePieceVente> sousVentes;
    private List<YvsComptaCaissePieceAchat> sousAchats;
    private List<YvsComptaJustificatifBon> justificatifs;
    private Users tiersInterne = new Users();
    private List<YvsComptaCaissePieceCompensation> compensations;
    private List<YvsComptaBielletage> billetages;
    private List<YvsComptaCoutSupPieceVirement> couts;

    private double sommeBillet, sommePiece;
    private List<BielletagePc> bielletageBillet;
    private List<BielletagePc> bielletagePiece;

    public PieceTresorerie() {
        phases = new ArrayList<>();
        listOthersPiece = new ArrayList<>();
        etapesValidation = new ArrayList<>();
        bielletagePiece = new ArrayList<>();
        bielletageBillet = new ArrayList<>();
        compensations = new ArrayList<>();
        justificatifs = new ArrayList<>();
        billetages = new ArrayList<>();
        phasesAchat = new ArrayList<>();
        phasesDivers = new ArrayList<>();
        sousVentes = new ArrayList<>();
        sousDivers = new ArrayList<>();
        sousAchats = new ArrayList<>();
        couts = new ArrayList<>();

        //initialise les bielletage
        initBielletage();
    }

    public PieceTresorerie(long id) {
        this();
        this.id = id;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    private void initBielletage() {
        bielletageBillet.add(new BielletagePc(0, "10 000", 0, 10000));
        bielletageBillet.add(new BielletagePc(0, "5000", 0, 5000));
        bielletageBillet.add(new BielletagePc(0, "2000", 0, 2000));
        bielletageBillet.add(new BielletagePc(0, "1000", 0, 1000));
        bielletageBillet.add(new BielletagePc(0, "500", 0, 500));
        bielletagePiece.add(new BielletagePc(0, "500", 0, 500));
        bielletagePiece.add(new BielletagePc(0, "100", 0, 100));
        bielletagePiece.add(new BielletagePc(0, "50", 0, 50));
        bielletagePiece.add(new BielletagePc(0, "25", 0, 25));
        bielletagePiece.add(new BielletagePc(0, "10", 0, 10));
        bielletagePiece.add(new BielletagePc(0, "5", 0, 5));
    }

    public EnteteDocVente getEnteteDoc() {
        return enteteDoc;
    }

    public void setEnteteDoc(EnteteDocVente enteteDoc) {
        this.enteteDoc = enteteDoc;
    }

    public List<YvsComptaPhasePieceDivers> getPhasesDivers() {
        return phasesDivers;
    }

    public void setPhasesDivers(List<YvsComptaPhasePieceDivers> phasesDivers) {
        this.phasesDivers = phasesDivers;
    }

    public List<YvsComptaCaissePieceAchat> getSousAchats() {
        return sousAchats;
    }

    public void setSousAchats(List<YvsComptaCaissePieceAchat> sousAchats) {
        this.sousAchats = sousAchats;
    }

    public List<YvsComptaCaissePieceVente> getSousVentes() {
        return sousVentes;
    }

    public void setSousVentes(List<YvsComptaCaissePieceVente> sousVentes) {
        this.sousVentes = sousVentes;
    }

    public List<YvsComptaCaissePieceDivers> getSousDivers() {
        return sousDivers;
    }

    public void setSousDivers(List<YvsComptaCaissePieceDivers> sousDivers) {
        this.sousDivers = sousDivers;
    }

    public List<YvsComptaCoutSupPieceVirement> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsComptaCoutSupPieceVirement> couts) {
        this.couts = couts;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public String getNameTiers() {
        return nameTiers;
    }

    public void setNameTiers(String nameTiers) {
        this.nameTiers = nameTiers;
    }

    public boolean isBon() {
        return bon;
    }

    public void setBon(boolean bon) {
        this.bon = bon;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public boolean isAcompte() {
        return acompte;
    }

    public void setAcompte(boolean acompte) {
        this.acompte = acompte;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
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

    public CommissionCommerciaux getCommission() {
        return commission;
    }

    public void setCommission(CommissionCommerciaux commission) {
        this.commission = commission;
    }

    public List<YvsComptaPhasePiece> getPhases() {
        return phases;
    }

    public void setPhases(List<YvsComptaPhasePiece> phases) {
        this.phases = phases;
    }

    public List<BielletagePc> getBielletageBillet() {
        return bielletageBillet;
    }

    public void setBielletageBillet(List<BielletagePc> bielletageBillet) {
        this.bielletageBillet = bielletageBillet;
    }

    public List<BielletagePc> getBielletagePiece() {
        return bielletagePiece;
    }

    public void setBielletagePiece(List<BielletagePc> bielletagePiece) {
        this.bielletagePiece = bielletagePiece;
    }

    public String getStatutExterne() {
        return statutExterne;
    }

    public void setStatutExterne(String statutExterne) {
        this.statutExterne = statutExterne;
    }

    public Users getCaissier() {
        return caissier;
    }

    public void setCaissier(Users caissier) {
        this.caissier = caissier;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public String getNumRef() {
        return numRef;
    }

    public void setNumRef(String numRef) {
        this.numRef = numRef;
    }

    public String getNameTable() {
        nameTable = (getSource().equals(Constantes.SCR_VENTE) ? Constantes.SCR_VENTE_NAME : (getSource().equals(Constantes.SCR_DIVERS) ? Constantes.SCR_DIVERS_NAME : Constantes.SCR_ACHAT_NAME));
        return nameTable;
    }

    public String getNameTable_() {
        nameTable = (getSource_().equals(Constantes.SCR_VENTE) ? Constantes.SCR_VENTE_NAME : (getSource_().equals(Constantes.SCR_DIVERS) ? Constantes.SCR_DIVERS_NAME : Constantes.SCR_ACHAT_NAME));
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public ModeDeReglement getMode() {
        return mode;
    }

    public void setMode(ModeDeReglement mode) {
        this.mode = mode;
    }

    public String getSource() {
        return source;
    }

    public String getSource_() {
        source = tableExterne != null ? (tableExterne.equals(Constantes.yvs_com_mensualite_facture_vente) ? Constantes.SCR_VENTE : (tableExterne.equals(Constantes.yvs_base_mensualite_doc_divers) ? Constantes.SCR_DIVERS : Constantes.SCR_ACHAT)) : Constantes.SCR_ACHAT;
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Date getDatePaiementPrevu() {
        return datePaiementPrevu != null ? datePaiementPrevu : new Date();
    }

    public void setDatePaiementPrevu(Date datePaiementPrevu) {
        this.datePaiementPrevu = datePaiementPrevu;
    }

    public char getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(char statutPiece) {
        this.statutPiece = statutPiece;
    }

    public DocVente getDocVente() {
        return docVente;
    }

    public void setDocVente(DocVente docVente) {
        this.docVente = docVente;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatePiece() {
        return datePiece != null ? datePiece : new Date();
    }

    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    public String getMouvement() {
        return mouvement != null ? mouvement.trim().length() > 0 ? mouvement : Constantes.MOUV_CAISS_ENTREE : Constantes.MOUV_CAISS_ENTREE;
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public long getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(long idExterne) {
        this.idExterne = idExterne;
    }

    public String getTableExterne() {
        return tableExterne;
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
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

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantAvance() {
        return montantAvance;
    }

    public void setMontantAvance(double montantAvance) {
        this.montantAvance = montantAvance;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public DocAchat getDocAchat() {
        return docAchat;
    }

    public void setDocAchat(DocAchat docAchat) {
        this.docAchat = docAchat;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public DocCaissesDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(DocCaissesDivers docDivers) {
        this.docDivers = docDivers;
    }

    public BonProvisoire getBonProvisoire() {
        return bonProvisoire;
    }

    public void setBonProvisoire(BonProvisoire bonProvisoire) {
        this.bonProvisoire = bonProvisoire;
    }

    public List<EtapesValidationX> getEtapesValidation() {
        return etapesValidation;
    }

    public void setEtapesValidation(List<EtapesValidationX> etapesValidation) {
        this.etapesValidation = etapesValidation;
    }

    public List<YvsComptaMouvementCaisse> getListOthersPiece() {
        return listOthersPiece;
    }

    public void setListOthersPiece(List<YvsComptaMouvementCaisse> listOthersPiece) {
        this.listOthersPiece = listOthersPiece;
    }

    public double getMontantPlanifie() {
        return montantPlanifie;
    }

    public void setMontantPlanifie(double montantPlanifie) {
        this.montantPlanifie = montantPlanifie;
    }

    public Caisses getOtherCaisse() {
        return otherCaisse;
    }

    public void setOtherCaisse(Caisses otherCaisse) {
        this.otherCaisse = otherCaisse;
    }

    public Users getTiersInterne() {
        return tiersInterne;
    }

    public void setTiersInterne(Users tiersInterne) {
        this.tiersInterne = tiersInterne;
    }

    public String getNumRefExterne() {
        return numRefExterne;
    }

    public void setNumRefExterne(String numRefExterne) {
        this.numRefExterne = numRefExterne;
    }

    public Users getValideBy() {
        return valideBy;
    }

    public void setValideBy(Users valideBy) {
        this.valideBy = valideBy;
    }

    public Date getDateValide() {
        return dateValide != null ? dateValide : new Date();
    }

    public void setDateValide(Date dateValide) {
        this.dateValide = dateValide;
    }

    public Client getClients() {
        return clients;
    }

    public void setClients(Client clients) {
        this.clients = clients;
    }

    public Tiers getTiers() {
        return tiers;
    }

    public void setTiers(Tiers tiers) {
        this.tiers = tiers;
    }

    public List<YvsComptaCaissePieceCompensation> getCompensations() {
        return compensations;
    }

    public void setCompensations(List<YvsComptaCaissePieceCompensation> compensations) {
        this.compensations = compensations;
    }

    public List<YvsComptaJustificatifBon> getJustificatifs() {
        return justificatifs;
    }

    public void setJustificatifs(List<YvsComptaJustificatifBon> justificatifs) {
        this.justificatifs = justificatifs;
    }

    public List<YvsComptaBielletage> getBilletages() {
        return billetages;
    }

    public void setBilletages(List<YvsComptaBielletage> billetages) {
        this.billetages = billetages;
    }

    public double getSommeBillet() {
        sommeBillet = 0;
        for (BielletagePc r : bielletageBillet) {
            sommeBillet += (r.getQuantite() * r.getValeur());
        }
        return sommeBillet;
    }

    public void setSommeBillet(double sommeBillet) {
        this.sommeBillet = sommeBillet;
    }

    public double getSommePiece() {
        sommePiece = 0;
        for (BielletagePc r : bielletagePiece) {
            sommePiece += (r.getQuantite() * r.getValeur());
        }
        return sommePiece;
    }

    public void setSommePiece(double sommePiece) {
        this.sommePiece = sommePiece;
    }

    public List<YvsComptaPhasePieceAchat> getPhasesAchat() {
        return phasesAchat;
    }

    public void setPhasesAchat(List<YvsComptaPhasePieceAchat> phasesAchat) {
        this.phasesAchat = phasesAchat;
    }

    public Users getCaissierCible() {
        return caissierCible;
    }

    public void setCaissierCible(Users caissierCible) {
        this.caissierCible = caissierCible;
    }

    public boolean isJustificatif() {
        justificatif = getBonProvisoire() != null ? getBonProvisoire().getId() > 0 : false;
        return justificatif;
    }

    public void setJustificatif(boolean justificatif) {
        this.justificatif = justificatif;
    }

    public YvsComptaJustificatifBon getJustify() {
        return justify;
    }

    public void setJustify(YvsComptaJustificatifBon justify) {
        this.justify = justify;
    }

    public YvsComptaNotifReglementVente getNotifRegVente() {
        return notifRegVente;
    }

    public void setNotifRegVente(YvsComptaNotifReglementVente notifRegVente) {
        this.notifRegVente = notifRegVente;
    }

    public YvsComptaNotifReglementAchat getNotifRegAchat() {
        return notifRegAchat;
    }

    public void setNotifRegAchat(YvsComptaNotifReglementAchat notifRegAchat) {
        this.notifRegAchat = notifRegAchat;
    }

    public YvsComptaNotifReglementDocDivers getNotifRegDivers() {
        return notifRegDivers;
    }

    public void setNotifRegDivers(YvsComptaNotifReglementDocDivers notifRegDivers) {
        this.notifRegDivers = notifRegDivers;
    }
    
    

    public double getMontantCout() {
        montantCout = 0;
        for (YvsComptaCoutSupPieceVirement c : couts) {
            montantCout += c.getMontant();
        }
        return montantCout;
    }

    public void setMontantCout(double montantCout) {
        this.montantCout = montantCout;
    }

    public long getIdParent() {
        return idParent;
    }

    public void setIdParent(long idParent) {
        this.idParent = idParent;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final PieceTresorerie other = (PieceTresorerie) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        PieceTresorerie p = (PieceTresorerie) o;
        if (datePiece.equals(p.datePiece)) {
            return Long.valueOf(id).compareTo(p.id);
        }
        return datePiece.compareTo(p.datePiece);
    }

}
