/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.fournisseur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Journaux;
import yvs.base.compta.ManagedJournaux;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.UtilCompta;
import yvs.base.tiers.Tiers;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.DocAchat;
import yvs.commercial.achat.ManagedFactureAchat;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.caisse.ManagedReglementAchat;
import yvs.comptabilite.caisse.ManagedReglementVente;
import yvs.comptabilite.client.ReglementCredit;
import yvs.comptabilite.tresorerie.DocCaissesDivers;
import yvs.comptabilite.tresorerie.ManagedDocDivers;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaNotifReglementDocDivers;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.achat.YvsComptaCreditFournisseur;
import yvs.entity.compta.achat.YvsComptaNotifReglementAchat;
import yvs.entity.compta.achat.YvsComptaPhaseAcompteAchat;
import yvs.entity.compta.achat.YvsComptaReglementCreditFournisseur;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.fournisseur.YvsComptaPhaseReglementCreditFournisseur;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.param.YvsAgences;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ManagedTypeCout;
import yvs.grh.bean.TypeCout;
import yvs.service.compta.doc.divers.AYvsComptaAcompteFournisseur;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedOperationFournisseur extends Managed<AcompteFournisseur, YvsComptaAcompteFournisseur> implements Serializable {

    private AcompteFournisseur compte = new AcompteFournisseur();
    private List<YvsComptaAcompteFournisseur> acomptes, versements;
    private YvsComptaAcompteFournisseur selectCompte = new YvsComptaAcompteFournisseur();
    private PieceTresorerie piece = new PieceTresorerie();
    private YvsComptaNotifReglementAchat liaison = new YvsComptaNotifReglementAchat();
    private YvsComptaNotifReglementDocDivers liaison_doc = new YvsComptaNotifReglementDocDivers();
    private YvsComptaPhaseAcompteAchat etapeCompte;
    private YvsComptaPhaseAcompteAchat currentPhaseAcompteAchat = new YvsComptaPhaseAcompteAchat();
    boolean suspendCompte;
    public boolean date_up = false;
    private Date dateDebut_ = new Date(), dateFin_ = new Date();

    private PaginatorResult<YvsComptaCreditFournisseur> paginators = new PaginatorResult<>();
    private CreditFournisseur credit = new CreditFournisseur();
    private List<YvsComptaCreditFournisseur> credits, redevances;
    private YvsComptaCreditFournisseur selectCredit = new YvsComptaCreditFournisseur();
    private ReglementCredit reglement = new ReglementCredit();
    private YvsComptaReglementCreditFournisseur selectReglement = new YvsComptaReglementCreditFournisseur();
    YvsComptaPhaseReglementCreditFournisseur etapeCredit;
    private YvsComptaPhaseReglementCreditFournisseur currentPhaseCreditAchat = new YvsComptaPhaseReglementCreditFournisseur();
    boolean suspendReglementCredit;
    private boolean suspendCredit;
    private boolean needConfirmationCredit;
    private boolean trouberOD = true;

    private String operation = "C";
    private boolean addDate;
    private Date dateDebut = new Date(), dateFin = new Date();
    private String codeFournisseur, statutSearch, notifSearch, numSearch, modeSearch;
    private String natureSearch;
    private long caisseSearch, agenceSearch;
    private Boolean comptaSearch;
    private long nbrComptaSearch;

    private PaginatorResult<YvsComptaNotifReglementAchat> pAchat = new PaginatorResult<>();
    private PaginatorResult<YvsComptaNotifReglementDocDivers> pDivers = new PaginatorResult<>();
    private long max = 10;
    private String numeroSearchNotif;
    private boolean addDateNotif;
    private Date dateDebutSearchNotif = new Date(), dateFinSearchNotif = new Date();

    private String tabIds;
    private boolean memory_choix_delete_acompte, memory_choix_delete_credit, memory_choix_delete_reglement;
    YvsComptaParametre currentParam;
    private boolean isFacture = true;
    private boolean displayConfirm = true;
    private long agenceRegle = 0;

    private Long countEditable, countEncours, countValide, countAnnuler;
    private Double valueEditable, valueEncours, valueValide, valueAnnuler;

    private AcomptesAchatDivers selectNotif;
    private List<AcomptesAchatDivers> selectNotifs;

    public ManagedOperationFournisseur() {
        acomptes = new ArrayList();
        credits = new ArrayList();
        versements = new ArrayList();
        redevances = new ArrayList();
        selectNotifs = new ArrayList();
    }

    public PaginatorResult<YvsComptaNotifReglementAchat> getpAchat() {
        return pAchat;
    }

    public void setpAchat(PaginatorResult<YvsComptaNotifReglementAchat> pAchat) {
        this.pAchat = pAchat;
    }

    public PaginatorResult<YvsComptaNotifReglementDocDivers> getpDivers() {
        return pDivers;
    }

    public void setpDivers(PaginatorResult<YvsComptaNotifReglementDocDivers> pDivers) {
        this.pDivers = pDivers;
    }

    public String getNumeroSearchNotif() {
        return numeroSearchNotif;
    }

    public void setNumeroSearchNotif(String numeroSearchNotif) {
        this.numeroSearchNotif = numeroSearchNotif;
    }

    public boolean isAddDateNotif() {
        return addDateNotif;
    }

    public void setAddDateNotif(boolean addDateNotif) {
        this.addDateNotif = addDateNotif;
    }

    public Date getDateDebutSearchNotif() {
        return dateDebutSearchNotif;
    }

    public void setDateDebutSearchNotif(Date dateDebutSearchNotif) {
        this.dateDebutSearchNotif = dateDebutSearchNotif;
    }

    public Date getDateFinSearchNotif() {
        return dateFinSearchNotif;
    }

    public void setDateFinSearchNotif(Date dateFinSearchNotif) {
        this.dateFinSearchNotif = dateFinSearchNotif;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getAgenceRegle() {
        return agenceRegle;
    }

    public void setAgenceRegle(long agenceRegle) {
        this.agenceRegle = agenceRegle;
    }

    public List<AcomptesAchatDivers> getSelectNotifs() {
        return selectNotifs;
    }

    public long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public void setSelectNotifs(List<AcomptesAchatDivers> selectNotifs) {
        this.selectNotifs = selectNotifs;
    }

    public AcomptesAchatDivers getSelectNotif() {
        return selectNotif;
    }

    public void setSelectNotif(AcomptesAchatDivers selectNotif) {
        this.selectNotif = selectNotif;
    }

    public boolean isDate_up() {
        return date_up;
    }

    public void setDate_up(boolean date_up) {
        this.date_up = date_up;
    }

    public Date getDateDebut_() {
        return dateDebut_;
    }

    public void setDateDebut_(Date dateDebut_) {
        this.dateDebut_ = dateDebut_;
    }

    public Date getDateFin_() {
        return dateFin_;
    }

    public void setDateFin_(Date dateFin_) {
        this.dateFin_ = dateFin_;
    }

    public YvsComptaNotifReglementDocDivers getLiaison_doc() {
        return liaison_doc;
    }

    public void setLiaison_doc(YvsComptaNotifReglementDocDivers liaison_doc) {
        this.liaison_doc = liaison_doc;
    }

    public boolean isSuspendCompte() {
        return suspendCompte;
    }

    public void setSuspendCompte(boolean suspendCompte) {
        this.suspendCompte = suspendCompte;
    }

    public YvsComptaPhaseReglementCreditFournisseur getEtapeCredit() {
        return etapeCredit;
    }

    public void setEtapeCredit(YvsComptaPhaseReglementCreditFournisseur etapeCredit) {
        this.etapeCredit = etapeCredit;
    }

    public boolean isSuspendReglementCredit() {
        return suspendReglementCredit;
    }

    public void setSuspendReglementCredit(boolean suspendReglementCredit) {
        this.suspendReglementCredit = suspendReglementCredit;
    }

    public Long getCountEditable() {
        return countEditable;
    }

    public void setCountEditable(Long countEditable) {
        this.countEditable = countEditable;
    }

    public Long getCountEncours() {
        return countEncours;
    }

    public void setCountEncours(Long countEncours) {
        this.countEncours = countEncours;
    }

    public Long getCountValide() {
        return countValide;
    }

    public void setCountValide(Long countValide) {
        this.countValide = countValide;
    }

    public Long getCountAnnuler() {
        return countAnnuler;
    }

    public void setCountAnnuler(Long countAnnuler) {
        this.countAnnuler = countAnnuler;
    }

    public Double getValueEditable() {
        return valueEditable;
    }

    public void setValueEditable(Double valueEditable) {
        this.valueEditable = valueEditable;
    }

    public Double getValueEncours() {
        return valueEncours;
    }

    public void setValueEncours(Double valueEncours) {
        this.valueEncours = valueEncours;
    }

    public Double getValueValide() {
        return valueValide;
    }

    public void setValueValide(Double valueValide) {
        this.valueValide = valueValide;
    }

    public Double getValueAnnuler() {
        return valueAnnuler;
    }

    public void setValueAnnuler(Double valueAnnuler) {
        this.valueAnnuler = valueAnnuler;
    }

    public long getNbrComptaSearch() {
        return nbrComptaSearch;
    }

    public void setNbrComptaSearch(long nbrComptaSearch) {
        this.nbrComptaSearch = nbrComptaSearch;
    }

    public String getNatureSearch() {
        return natureSearch;
    }

    public void setNatureSearch(String natureSearch) {
        this.natureSearch = natureSearch;
    }

    public boolean isSuspendCredit() {
        return suspendCredit;
    }

    public void setSuspendCredit(boolean suspendCredit) {
        this.suspendCredit = suspendCredit;
    }

    public boolean isNeedConfirmationCredit() {
        return needConfirmationCredit;
    }

    public void setNeedConfirmationCredit(boolean needConfirmationCredit) {
        this.needConfirmationCredit = needConfirmationCredit;
    }

    public YvsComptaParametre getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsComptaParametre currentParam) {
        this.currentParam = currentParam;
    }

    public String getNotifSearch() {
        return notifSearch;
    }

    public void setNotifSearch(String notifSearch) {
        this.notifSearch = notifSearch;
    }

    public boolean isDisplayConfirm() {
        return displayConfirm;
    }

    public void setDisplayConfirm(boolean displayConfirm) {
        this.displayConfirm = displayConfirm;
    }

    public String getModeSearch() {
        return modeSearch;
    }

    public void setModeSearch(String modeSearch) {
        this.modeSearch = modeSearch;
    }

    public YvsComptaPhaseAcompteAchat getEtapeCompte() {
        return etapeCompte;
    }

    public void setEtapeCompte(YvsComptaPhaseAcompteAchat etapeCompte) {
        this.etapeCompte = etapeCompte;
    }

    public YvsComptaPhaseReglementCreditFournisseur getCurrentPhaseCreditAchat() {
        return currentPhaseCreditAchat;
    }

    public void setCurrentPhaseCreditAchat(YvsComptaPhaseReglementCreditFournisseur currentPhaseCreditAchat) {
        this.currentPhaseCreditAchat = currentPhaseCreditAchat;
    }

    public YvsComptaPhaseAcompteAchat getCurrentPhaseAcompteAchat() {
        return currentPhaseAcompteAchat;
    }

    public void setCurrentPhaseAcompteAchat(YvsComptaPhaseAcompteAchat currentPhaseAcompteAchat) {
        this.currentPhaseAcompteAchat = currentPhaseAcompteAchat;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public boolean isMemory_choix_delete_acompte() {
        return memory_choix_delete_acompte;
    }

    public void setMemory_choix_delete_acompte(boolean memory_choix_delete_acompte) {
        this.memory_choix_delete_acompte = memory_choix_delete_acompte;
    }

    public boolean isMemory_choix_delete_credit() {
        return memory_choix_delete_credit;
    }

    public void setMemory_choix_delete_credit(boolean memory_choix_delete_credit) {
        this.memory_choix_delete_credit = memory_choix_delete_credit;
    }

    public boolean isMemory_choix_delete_reglement() {
        return memory_choix_delete_reglement;
    }

    public void setMemory_choix_delete_reglement(boolean memory_choix_delete_reglement) {
        this.memory_choix_delete_reglement = memory_choix_delete_reglement;
    }

    public YvsComptaReglementCreditFournisseur getSelectReglement() {
        return selectReglement;
    }

    public void setSelectReglement(YvsComptaReglementCreditFournisseur selectReglement) {
        this.selectReglement = selectReglement;
    }

    public YvsComptaNotifReglementAchat getLiaison() {
        return liaison;
    }

    public void setLiaison(YvsComptaNotifReglementAchat liaison) {
        this.liaison = liaison;
    }

    public List<YvsComptaAcompteFournisseur> getVersements() {
        return versements;
    }

    public void setVersements(List<YvsComptaAcompteFournisseur> versements) {
        this.versements = versements;
    }

    public List<YvsComptaCreditFournisseur> getRedevances() {
        return redevances;
    }

    public void setRedevances(List<YvsComptaCreditFournisseur> redevances) {
        this.redevances = redevances;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getNumSearch() {
        return numSearch;
    }

    public void setNumSearch(String numSearch) {
        this.numSearch = numSearch;
    }

    public String getStatutSearch() {
        return statutSearch;
    }

    public void setStatutSearch(String statutSearch) {
        this.statutSearch = statutSearch;
    }

    public boolean isAddDate() {
        return addDate;
    }

    public void setAddDate(boolean addDate) {
        this.addDate = addDate;
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

    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public long getCaisseSearch() {
        return caisseSearch;
    }

    public void setCaisseSearch(long caisseSearch) {
        this.caisseSearch = caisseSearch;
    }

    public PieceTresorerie getPiece() {
        return piece;
    }

    public void setPiece(PieceTresorerie piece) {
        this.piece = piece;
    }

    public ReglementCredit getReglement() {
        return reglement;
    }

    public void setReglement(ReglementCredit reglement) {
        this.reglement = reglement;
    }

    public YvsComptaAcompteFournisseur getSelectCompte() {
        return selectCompte;
    }

    public void setSelectCompte(YvsComptaAcompteFournisseur selectCompte) {
        this.selectCompte = selectCompte;
    }

    public YvsComptaCreditFournisseur getSelectCredit() {
        return selectCredit;
    }

    public void setSelectCredit(YvsComptaCreditFournisseur selectCredit) {
        this.selectCredit = selectCredit;
    }

    public PaginatorResult<YvsComptaCreditFournisseur> getPaginators() {
        return paginators;
    }

    public void setPaginators(PaginatorResult<YvsComptaCreditFournisseur> paginators) {
        this.paginators = paginators;
    }

    public AcompteFournisseur getCompte() {
        return compte;
    }

    public void setCompte(AcompteFournisseur compte) {
        this.compte = compte;
    }

    public List<YvsComptaAcompteFournisseur> getAcomptes() {
        return acomptes;
    }

    public void setAcomptes(List<YvsComptaAcompteFournisseur> acomptes) {
        this.acomptes = acomptes;
    }

    public CreditFournisseur getCredit() {
        return credit;
    }

    public void setCredit(CreditFournisseur credit) {
        this.credit = credit;
    }

    public List<YvsComptaCreditFournisseur> getCredits() {
        return credits;
    }

    public void setCredits(List<YvsComptaCreditFournisseur> credits) {
        this.credits = credits;
    }

    public String getOperation() {
        return operation != null ? operation.trim().length() > 0 ? operation : "C" : "C";
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public void loadAll() {
        loadAll(null);
    }

    public void loadAll(String operation) {
        if (operation != null ? operation.trim().length() > 0 : false) {
            this.operation = operation;
        }
        if (reglement != null ? reglement.getId() < 1 : true) {
            reglement = new ReglementCredit();
        }
        if (reglement.getCaisse() != null ? reglement.getCaisse().getId() < 1 : true) {
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service != null) {
                reglement.setCaisse(UtilCompta.buildBeanCaisse(service.findByResponsable(currentUser.getUsers())));
            } else {
                reglement.setCaisse(new Caisses());
            }
        }
        if (reglement.getMode() != null ? reglement.getMode().getId() < 1 : true) {
            reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        }

        if (piece != null ? piece.getId() < 1 : true) {
            piece = new PieceTresorerie();
        }
        if (piece.getCaisse() != null ? piece.getCaisse().getId() < 1 : true) {
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service != null) {
                piece.setCaisse(UtilCompta.buildBeanCaisse(service.findByResponsable(currentUser.getUsers())));
            } else {
                piece.setCaisse(new Caisses());
            }
        }
        if (piece.getMode() != null ? piece.getMode().getId() < 1 : true) {
            piece.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        }
        currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if (currentParam == null) {
            currentParam = new YvsComptaParametre();
        }
        if (operation != null ? operation.equals("A") : true) {
            loadAcompte(true, true);
        }
        if (operation != null ? operation.equals("C") : true) {
            loadCredit(true, true);
        }
    }

    public void loadAllByStatut(String operation, String statut) {
        this.operation = operation;
        paginator.getParams().clear();
        statutSearch = statut;
        addParamStatut();
    }

    public void loadAcompte(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.fournisseur.tiers.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        acomptes = paginator.executeDynamicQuery("y", "y", "YvsComptaAcompteFournisseur y JOIN FETCH y.fournisseur JOIN FETCH y.caisse JOIN FETCH y.model JOIN FETCH y.fournisseur.tiers",
                "y.dateAcompte DESC", avance, init, (int) imax, dao);
    }

    public void loadCredit(boolean avance, boolean init) {
        paginators.addParam(new ParametreRequete("y.fournisseur.tiers.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        credits = paginators.executeDynamicQuery("YvsComptaCreditFournisseur", "y.dateCredit DESC", avance, init, (int) imax, dao);
    }

    public void loadAll(boolean avance, boolean init) {
        if (operation.equals("A")) {
            loadAcompte(avance, init);
            update("data_acompte_fournisseur");
        } else {
            loadCredit(avance, init);
            update("data_credit_fournisseur");
        }
    }

    public void loadNotifAcompte(boolean avance, boolean init) {
        compte.getAchatsEtDivers().clear();
        if (selectCompte != null ? selectCompte.getId() > 0 : false) {
            loadNotifAchatAcompte(avance, init);
            loadNotifDiversAcompte(avance, init);
            for (YvsComptaNotifReglementAchat c : selectCompte.getNotifs()) {
                compte.getAchatsEtDivers().add(UtilCompta.buildBeanAcomptesAchatDivers(c));
            }
            for (YvsComptaNotifReglementDocDivers c : selectCompte.getNotifsDivers()) {
                compte.getAchatsEtDivers().add(UtilCompta.buildBeanAcomptesAchatDivers(c));
            }
        } else {
            getErrorMessage("Vous devez selectionner un acompte");
        }
    }

    public void loadNotifAchatAcompte(boolean avance, boolean init) {
        if (selectCompte != null ? selectCompte.getId() > 0 : false) {
            ParametreRequete p = new ParametreRequete("y.acompte", "acompte", selectCompte, "=", "AND");
            pAchat.addParam(p);
            selectCompte.setNotifs(pAchat.executeDynamicQuery("y", "y", "YvsComptaNotifReglementAchat y JOIN FETCH y.pieceAchat JOIN FETCH y.pieceAchat.achat", "y.pieceAchat.datePiece", avance, init, (int) max, dao));
        } else {
            getErrorMessage("Vous devez selectionner un acompte");
        }
    }

    public void loadNotifDiversAcompte(boolean avance, boolean init) {
        if (selectCompte != null ? selectCompte.getId() > 0 : false) {
            ParametreRequete p = new ParametreRequete("y.acompteFournisseur", "acompte", selectCompte, "=", "AND");
            pDivers.addParam(p);
            selectCompte.setNotifsDivers(pDivers.executeDynamicQuery("y", "y", "YvsComptaNotifReglementDocDivers y JOIN FETCH y.pieceDocDivers JOIN FETCH y.pieceDocDivers.docDivers", "y.pieceDocDivers.datePiece", avance, init, (int) max, dao));
        } else {
            getErrorMessage("Vous devez selectionner un acompte");
        }
    }

    public void findByDatesNotif() {
        ParametreRequete pV = new ParametreRequete("y.pieceAchat.datePiece", "dates", null);
        ParametreRequete pD = new ParametreRequete("y.pieceDocDivers.datePiece", "dates", null);
        if (addDateNotif) {
            pV = new ParametreRequete(null, "dates", dateDebutSearchNotif, dateFinSearchNotif, "BETWEEN", "AND");
            pV.getOtherExpression().add(new ParametreRequete("y.pieceAchat.datePiece", "dates", dateDebutSearchNotif, dateFinSearchNotif, "BETWEEN", "OR"));
            pV.getOtherExpression().add(new ParametreRequete("y.pieceAchat.achat.dateDoc", "dates", dateDebutSearchNotif, dateFinSearchNotif, "BETWEEN", "OR"));

            pD = new ParametreRequete(null, "dates", dateDebutSearchNotif, dateFinSearchNotif, "BETWEEN", "AND");
            pD.getOtherExpression().add(new ParametreRequete("y.pieceDocDivers.datePiece", "dates", dateDebutSearchNotif, dateFinSearchNotif, "BETWEEN", "OR"));
            pD.getOtherExpression().add(new ParametreRequete("y.pieceDocDivers.docDivers.dateDoc", "dates", dateDebutSearchNotif, dateFinSearchNotif, "BETWEEN", "OR"));
        }
        pAchat.addParam(pV);
        pDivers.addParam(pD);
        loadNotifAcompte(true, true);
    }

    public void findByNumeroNotif() {
        if (numeroSearchNotif != null ? numeroSearchNotif.trim().length() > 0 : false) {
            ParametreRequete pV = new ParametreRequete(null, "numero", numeroSearchNotif.toUpperCase() + "%", " LIKE ", "AND");
            pV.getOtherExpression().add(new ParametreRequete("UPPER(y.pieceAchat.numeroPiece)", "numero", numeroSearchNotif.toUpperCase() + "%", " LIKE ", "OR"));
            pV.getOtherExpression().add(new ParametreRequete("UPPER(y.pieceAchat.achat.numDoc)", "numero", numeroSearchNotif.toUpperCase() + "%", " LIKE ", "OR"));
            pAchat.addParam(pV);

            ParametreRequete pD = new ParametreRequete(null, "numero", numeroSearchNotif.toUpperCase() + "%", " LIKE ", "AND");
            pD.getOtherExpression().add(new ParametreRequete("UPPER(y.pieceDocDivers.numPiece)", "numero", numeroSearchNotif.toUpperCase() + "%", " LIKE ", "OR"));
            pD.getOtherExpression().add(new ParametreRequete("UPPER(y.pieceDocDivers.docDivers.numPiece)", "numero", numeroSearchNotif.toUpperCase() + "%", " LIKE ", "OR"));
            pDivers.addParam(pD);
        } else {
            pAchat.addParam(new ParametreRequete("y.pieceAchat.numeroPiece", "numero", null));
            pDivers.addParam(new ParametreRequete("y.pieceDocDivers.numeroPiece", "numero", null));
        }
        loadNotifAcompte(true, true);
    }

    public void choosePaginatorNotif(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            long v = (long) ev.getNewValue();
            max = v;
            loadNotifAcompte(true, true);
        }
    }

    public void historiqueCompte(YvsBaseFournisseur y) {
        champ = new String[]{"fournisseur"};
        val = new Object[]{y};
        nameQueri = "YvsComptaAcompteFournisseur.findByFournisseur";
        versements = dao.loadNameQueries(nameQueri, champ, val);

        champ = new String[]{"fournisseur", "statut"};
        val = new Object[]{y, Constantes.STATUT_DOC_PAYER};
        nameQueri = "YvsComptaAcompteFournisseur.findSumByFournisseur";
        Double depot = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);

        champ = new String[]{"fournisseur"};
        val = new Object[]{y};
        nameQueri = "YvsComptaNotifReglementAchat.findSumByFournisseur";
        Double avance = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        compte.setSolde((depot != null ? depot : 0) - (avance != null ? avance : 0));

        update("txt_solde_acompte_fournisseur");
        update("data_historique_acompte_fournisseur");
    }

    public void historiqueCredit(YvsBaseFournisseur y) {
        champ = new String[]{"fournisseur"};
        val = new Object[]{y};
        nameQueri = "YvsComptaCreditFournisseur.findByFournisseur";
        redevances = dao.loadNameQueries(nameQueri, champ, val);

        nameQueri = "YvsComptaCreditFournisseur.findSumByFournisseur";
        Double depot = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);

        nameQueri = "YvsComptaReglementCreditFournisseur.findSumByFournisseur";
        Double avance = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        credit.setSolde((depot != null ? depot : 0) - (avance != null ? avance : 0));

        update("txt_solde_credit_fournisseur");
        update("data_historique_credit_fournisseur");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (operation.equals("A") ? paginator.getNbResult() : paginators.getNbResult())) {
            setOffset(0);
        }
        if (operation.equals("A")) {
            List<YvsComptaAcompteFournisseur> re = paginator.parcoursDynamicData("YvsComptaAcompteFournisseur", "y", "y.dateAcompte DESC", getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectObject(re.get(0));
            }
        } else {
            List<YvsComptaCreditFournisseur> re = paginators.parcoursDynamicData("YvsComptaCreditFournisseur", "y", "y.dateCredit DESC", getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectObject(re.get(0));
            }
        }
    }

    public void paginer(boolean next) {
        loadAll(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAll(true, true);
    }

    public ReglementCredit recopieViewReglement() {
        if (reglement != null) {
            reglement.setCredit(credit.getId());
            if (reglement.getMode() != null ? reglement.getMode().getId() > 0 : false) {
                ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (m != null) {
                    YvsBaseModeReglement o = m.getModes().get(m.getModes().indexOf(new YvsBaseModeReglement((long) reglement.getMode().getId())));
                    reglement.setMode(new ModeDeReglement(o.getId().intValue(), o.getDesignation(), o.getTypeReglement()));
                }
            }
        }
        return reglement;
    }

    @Override
    public boolean controleFiche(AcompteFournisseur bean) {
        if (bean.getFournisseur() != null ? bean.getFournisseur().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le fournisseur");
            return false;
        }
        if (bean.getCaisse() != null ? bean.getCaisse().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez la caisse");
            return false;
        }
        if (bean.getMontant() < 1) {
            getErrorMessage("Vous devez precisez le montant");
            return false;
        }
        if ((selectCompte != null ? (selectCompte.getId() > 0 ? (!selectCompte.getDateAcompte().equals(bean.getDateAcompte())) : true) : true)
                || (bean.getNumRefrence() == null || bean.getNumRefrence().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_PT_AVANCE_ACHAT, bean.getDateAcompte());
            if ((ref != null) ? ref.trim().equals("") : true) {
                return false;
            }
            bean.setNumRefrence(ref);
        }
        return true;
    }

    public boolean controleFiche(CreditFournisseur bean) {
        if (bean.getFournisseur() != null ? bean.getFournisseur().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le fournisseur");
            return false;
        }
        if (bean.getTypeCredit() != null ? bean.getTypeCredit().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le type de crédit");
            return false;
        }
        if (bean.getJournal() != null ? bean.getJournal().getId() < 1 : true) {
            getWarningMessage("Vous devez preciser le journal");
        }
        if (bean.getMontant() < 1) {
            getErrorMessage("Vous devez preciser le montant");
            return false;
        }
        if (bean.getStatut() == Constantes.STATUT_DOC_VALIDE) {
            getErrorMessage("Ce crédit est déjà validé");
            return false;
        }
        if ((selectCredit != null ? (selectCredit.getId() > 0 ? (!selectCredit.getDateCredit().equals(bean.getDateCredit())) : true) : true)
                || (bean.getNumReference() == null || bean.getNumReference().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_PT_CREDIT_ACHAT, bean.getDateCredit());
            if ((ref != null) ? ref.trim().equals("") : true) {
                return false;
            }
            bean.setNumReference(ref);
        }
        return true;
    }

    public boolean controleFiche(ReglementCredit bean) {
        if (bean.getCredit() < 1) {
            getErrorMessage("Vous devez precisez le crédit");
            return false;
        }
        if (bean.getCaisse() != null ? bean.getCaisse().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez la caisse");
            return false;
        }
        if (bean.getMode() != null ? bean.getMode().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez la caisse");
            return false;
        }
        if (bean.getValeur() < 1) {
            getErrorMessage("Vous devez precisez le montant");
            return false;
        }
        if (bean.getStatut() == Constantes.STATUT_DOC_PAYER) {
            getErrorMessage("Ce reglement est déjà payé");
            return false;
        }
        if ((selectReglement != null ? (selectReglement.getId() > 0 ? (!selectReglement.getDateReg().equals(bean.getDateReg())) : true) : true)
                || (bean.getNumero() == null || bean.getNumero().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_PC_NAME, bean.getDateReg());
            if ((ref != null) ? ref.trim().equals("") : true) {
                return false;
            }
            bean.setNumero(ref);
        }
        return true;
    }

    @Override
    public void populateView(AcompteFournisseur bean) {
        cloneObject(compte, bean);
        compte.setPhasesReglement(ordonnePhase(compte.getPhasesReglement()));
        if (bean.getPhasesReglement() != null ? !bean.getPhasesReglement().isEmpty() : false) {
            compte.setFirstEtape(bean.getPhasesReglement().get(0).getPhaseReg().getPhase());
        }
        piece.setMode(compte.getMode());
    }

    public void populateView(CreditFournisseur bean) {
        cloneObject(credit, bean);
    }

    public void populateView(ReglementCredit bean) {
        cloneObject(reglement, bean);
    }

    public void populateView(PieceTresorerie bean) {
        cloneObject(piece, bean);
    }

    @Override
    public void resetFiche() {
        if (operation.equals("A")) {
            Date date = compte.getDateAcompte();
            char nature = compte.getNature();
            Caisses caisse = new Caisses();
            cloneObject(caisse, compte.getCaisse());
            ModeDeReglement mode = new ModeDeReglement();
            cloneObject(mode, compte.getMode());

            compte = new AcompteFournisseur();
            compte.setDateAcompte(date);
            compte.setNature(nature);
            compte.setCaisse(caisse);
            compte.setMode(mode);

            selectCompte = new YvsComptaAcompteFournisseur();
            update("blog_acompte_fournisseur");
            update("blog_entete_acompte_fournisseur");
        } else {
            Date date = credit.getDateCredit();
            Journaux journal = new Journaux();
            cloneObject(journal, credit.getJournal());
            TypeCout typeCredit = new TypeCout();
            cloneObject(typeCredit, credit.getTypeCredit());

            credit = new CreditFournisseur();
            credit.setDateCredit(date);
            credit.setJournal(journal);
            credit.setTypeCredit(typeCredit);

            selectCredit = new YvsComptaCreditFournisseur();
            redevances.clear();
            resetReglement();
            update("blog_credit_fournisseur");
        }
    }

    public void resetReglement() {
        reglement = new ReglementCredit();
        if (credit != null) {
            reglement.setValeur(credit.getReste());
        }
        reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        update("form_reglement_credit_fa_fournisseur");
    }

    public void resetFicheReglement() {
        piece = new PieceTresorerie();
        if (compte != null) {
            piece.setMontant(compte.getReste());
        }
        piece.setMode(compte.getMode());
        liaison = new YvsComptaNotifReglementAchat();
        update("form_reglement_acompte_fa");
    }

    @Override
    public boolean saveNew() {
        if (operation.equals("A")) {
            YvsComptaAcompteFournisseur y = saveAcompte(compte);
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                selectCompte = y;
                update("data_acompte_fournisseur");
                update("blog_entete_acompte_fournisseur");
                actionOpenOrResetAfter(this);
                piece.setMode(compte.getMode());
                update("txt_mode_acompte_reglement_achat");
                return true;
            }
        } else {
            YvsComptaCreditFournisseur y = saveCredit(credit);
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                selectCredit = y;
                update("data_credit_fournisseur");
                update("blog_entete_credit_fournisseur");
                actionOpenOrResetAfter(this);
                return true;
            }
        }
        return false;
    }

    public YvsComptaAcompteFournisseur saveAcompte(AcompteFournisseur bean) {
        String action = bean.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean)) {
                selectCompte = UtilCompta.buildAcompteFournisseur(bean, currentUser);
                if (bean.getId() > 0) {
                    dao.update(selectCompte);
                } else {
                    selectCompte.setId(null);
                    selectCompte = (YvsComptaAcompteFournisseur) dao.save1(selectCompte);
                    bean.setId(selectCompte.getId());
                    bean.setReste(bean.getMontant());
                }
                if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    //si on veux valider le paiement

                } else {
                    for (YvsComptaPhaseAcompteAchat p : bean.getPhasesReglement()) {
                        dao.delete(p);
                    }
                    selectCompte.getPhasesReglement().clear();
                    bean.getPhasesReglement().clear();
                }
                ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                if (w != null) {
                    int idx = w.getCaisses().indexOf(new YvsBaseCaisse(compte.getCaisse().getId()));
                    if (idx > -1) {
                        YvsBaseCaisse y = w.getCaisses().get(idx);
                        selectCompte.setCaisse(y);
                    }
                }
                int idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                } else {
                    acomptes.add(0, selectCompte);
                }
                succes();
                return selectCompte;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error", ex);
        }
        return null;
    }

    public YvsComptaCreditFournisseur saveCredit(CreditFournisseur bean) {
        String action = bean.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean)) {
                YvsComptaCreditFournisseur y = UtilCompta.buildCreditFournisseur(bean, currentUser);
                if (bean.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsComptaCreditFournisseur) dao.save1(y);
                    bean.setId(y.getId());
                }
                int idx = credits.indexOf(y);
                if (idx > -1) {
                    credits.set(idx, y);
                } else {
                    credits.add(0, y);
                }
                succes();
                return y;
            }
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error", ex);
        }
        return null;
    }

    public void saveReglement() {
        String action = reglement.getId() > 0 ? "Modification" : "Insertion";
        try {
            ReglementCredit bean = recopieViewReglement();
            if (controleFiche(bean)) {
                selectReglement = UtilCompta.buildReglementCreditF(bean, currentUser);
                if (bean.getId() > 0) {
                    dao.update(selectReglement);
                } else {
                    selectReglement.setId(null);
                    selectReglement = (YvsComptaReglementCreditFournisseur) dao.save1(selectReglement);
                    reglement.setId(selectReglement.getId());
                    System.err.println("selectReglement.getId() " + selectReglement.getId());
                }
                int idx = credit.getReglements().indexOf(selectReglement);
                if (idx > -1) {
                    credit.getReglements().set(idx, selectReglement);
                } else {
                    credit.getReglements().add(0, selectReglement);
                }
                idx = selectCredit.getReglements().indexOf(selectReglement);
                if (idx > -1) {
                    selectCredit.getReglements().set(idx, selectReglement);
                } else {
                    selectCredit.getReglements().add(0, selectReglement);
                }
                idx = credits.indexOf(selectCredit);
                if (idx > -1) {
                    credits.set(idx, selectCredit);
                }
                resetReglement();
                update("data_reglement_credit_fa");
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error", ex);
        }
    }

    public void savePiece() {
        boolean update = piece.getId() > 0;
        try {
            if (selectCompte != null ? (selectCompte.getId() != null ? selectCompte.getId() < 1 : true) : true) {
                getErrorMessage("Vous devez selectionner l'acompte");
                return;
            }
            if (isFacture) {
                ManagedReglementAchat m = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                if (m != null) {
                    piece.setCaisse(compte.getCaisse());
                    piece.setMode(compte.getMode());
                    if (!update) {
                        piece.setStatutPiece(compte.getStatut());
                    }
                    YvsComptaCaissePieceAchat r = UtilCom.buildPieceAchat(piece, currentUser);
                    r = m.createNewPieceCaisse(piece.getDocAchat(), r, false);
                    if (r != null ? r.getId() > 0 : false) {
                        champ = new String[]{"piece", "acompte"};
                        val = new Object[]{r, selectCompte};
                        nameQueri = "YvsComptaNotifReglementAchat.findOne";
                        YvsComptaNotifReglementAchat y = (YvsComptaNotifReglementAchat) dao.loadOneByNameQueries(nameQueri, champ, val);
                        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                            y.setAuthor(currentUser);
                            dao.update(y);
                        } else {
                            y = new YvsComptaNotifReglementAchat(r, selectCompte, currentUser);
                            y.setId(null);
                            y = (YvsComptaNotifReglementAchat) dao.save1(y);
                        }
                        AcomptesAchatDivers a = UtilCompta.buildBeanAcomptesAchatDivers(y);
                        int idx = compte.getAchatsEtDivers().indexOf(a);
                        if (idx > -1) {
                            compte.getAchatsEtDivers().set(idx, a);
                        } else {
                            compte.getAchatsEtDivers().add(0, a);
                        }
                        idx = selectCompte.getNotifs().indexOf(y);
                        if (idx > -1) {
                            selectCompte.getNotifs().set(idx, y);
                        } else {
                            selectCompte.getNotifs().add(0, y);
                        }
                        idx = acomptes.indexOf(selectCompte);
                        if (idx > -1) {
                            acomptes.set(idx, selectCompte);
                        }
                        if (piece.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                            equilibre(selectCompte);
                            compte.setReste(compte.getReste() - piece.getMontant());
                            if (dao.isComptabilise(piece.getDocAchat().getId(), Constantes.SCR_ACHAT)) {
                                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                                if (w != null) {
                                    w.comptabiliserCaisseAchat(r, false, false);
                                }
                            }
                        }
                        succes();
                    }

                }
            } else {
                if (!piece.getDocDivers().getStatutDoc().equals("V")) {
                    getErrorMessage("Le document doit être validé");
                    return;
                }

                ManagedDocDivers m = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                if (m != null) {
                    piece.setCaisse(compte.getCaisse());
                    piece.setMode(compte.getMode());
                    if (!update) {
                        piece.setStatutPiece(compte.getStatut());
                    }
                    YvsComptaCaissePieceDivers r = UtilCompta.buildPieceCaisse(piece, currentUser);

                    r = m.createNewPieceCaisse(piece.getDocDivers(), r, false);
                    if (r != null ? r.getId() > 0 : false) {
                        champ = new String[]{"piece", "acompte"};
                        val = new Object[]{r, selectCompte};
                        nameQueri = "YvsComptaNotifReglementDocDivers.findOne_fa";
                        YvsComptaNotifReglementDocDivers y = (YvsComptaNotifReglementDocDivers) dao.loadOneByNameQueries(nameQueri, champ, val);
                        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                            y.setAuthor(currentUser);
                            y.setDateUpdate(new Date());
                            dao.update(y);
                        } else {
                            y = new YvsComptaNotifReglementDocDivers(r, selectCompte, currentUser);
                            y.setId(null);
                            y.setDateSave(new Date());
                            y = (YvsComptaNotifReglementDocDivers) dao.save1(y);
                        }
                        int idx = 0;

                        if (compte.getAchatsEtDivers() != null ? !compte.getAchatsEtDivers().isEmpty() : false) {
                            AcomptesAchatDivers a = UtilCompta.buildBeanAcomptesAchatDivers(y);
                            idx = compte.getAchatsEtDivers().indexOf(a);
                            if (idx > -1) {
                                compte.getAchatsEtDivers().set(idx, a);

                            } else {
                                compte.getAchatsEtDivers().add(0, a);
                            }
                        }
                        if (selectCompte.getNotifsDivers() != null ? !selectCompte.getNotifsDivers().isEmpty() : false) {
                            idx = selectCompte.getNotifsDivers().indexOf(y);
                            if (idx > -1) {
                                selectCompte.getNotifsDivers().set(idx, y);
                            } else {
                                selectCompte.getNotifsDivers().add(0, y);
                            }
                            idx = acomptes.indexOf(selectCompte);
                            if (idx > -1) {
                                acomptes.set(idx, selectCompte);
                            }
                        }

                        if (piece.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                            equilibre(selectCompte);
                            compte.setReste(compte.getReste() - piece.getMontant());
                            if (dao.isComptabilise(piece.getDocVente().getId(), Constantes.SCR_VENTE)) {
                                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                                if (w != null) {
                                    w.comptabiliserCaisseDivers(r, false, false);
                                }
                            }
                        }
                        m.equilibreOne(UtilCompta.buildDocDivers(piece.getDocDivers()));
                        succes();
                    }

                }
            }
            update("data_reglement_acompte_fa");
            resetFicheReglement();

        } catch (Exception ex) {
            getErrorMessage(update ? "Modification" : "Insertion" + " Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void buildAndSavePiece(double montant) {
        buildAndSavePiece(montant, true);
    }

    public void buildAndSavePiece(double montant, boolean succes) {
        piece.setCaisse(compte.getCaisse());
        piece.setMode(compte.getMode());
        piece.setDatePaiement(compte.getDateAcompte());
        piece.setDatePiece(compte.getDateAcompte());
        piece.setDatePaiementPrevu(compte.getDateAcompte());
        if (isFacture) {
            YvsComptaCaissePieceAchat r = UtilCom.buildPieceAchat(piece, currentUser);
            r.setStatutPiece(compte.getStatut());
            ManagedReglementAchat service = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
            r = service.createNewPieceCaisse(piece.getDocAchat(), r, false);
            if (r != null ? r.getId() > 0 : false) {
                champ = new String[]{"piece", "acompte"};
                val = new Object[]{r, selectCompte};
                nameQueri = "YvsComptaNotifReglementAchat.findOne";
                YvsComptaNotifReglementAchat y = (YvsComptaNotifReglementAchat) dao.loadOneByNameQueries(nameQueri, champ, val);
                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                    y.setAuthor(currentUser);
                    dao.update(y);
                } else {
                    y = new YvsComptaNotifReglementAchat(r, selectCompte, currentUser);
                    y.setId(null);
                    y = (YvsComptaNotifReglementAchat) dao.save1(y);
                }
                AcomptesAchatDivers a = UtilCompta.buildBeanAcomptesAchatDivers(y);
                int idx = compte.getAchatsEtDivers().indexOf(a);
                if (idx > -1) {
                    compte.getAchatsEtDivers().set(idx, a);
                } else {
                    compte.getAchatsEtDivers().add(0, a);
                }
                idx = selectCompte.getNotifs().indexOf(y);
                if (idx > -1) {
                    selectCompte.getNotifs().set(idx, y);
                } else {
                    selectCompte.getNotifs().add(0, y);
                }
                idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                }
                if (piece.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                    equilibre(selectCompte);
                    compte.setReste(compte.getReste() - piece.getMontant());
                    if (dao.isComptabilise(piece.getDocAchat().getId(), Constantes.SCR_ACHAT)) {
                        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                        if (w != null) {
                            w.comptabiliserCaisseAchat(r, false, false);
                        }
                    }
                }
                if (succes) {
                    succes();
                }
            }
        } else {
            YvsComptaCaissePieceDivers r = UtilCompta.buildPieceCaisse(piece, currentUser);
            r.setStatutPiece(Constantes.STATUT_DOC_PAYER);
            ManagedDocDivers service = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            r = service.createNewPieceCaisse(piece.getDocDivers(), r, true);
            if (r != null ? r.getId() > 0 : false) {
                champ = new String[]{"piece", "acompte"};
                val = new Object[]{r, selectCompte};
                nameQueri = "YvsComptaNotifReglementDocDivers.findOne_fa";
                YvsComptaNotifReglementDocDivers y = (YvsComptaNotifReglementDocDivers) dao.loadOneByNameQueries(nameQueri, champ, val);
                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                    y.setAuthor(currentUser);
                    y.setDateUpdate(new Date());
                    dao.update(y);
                } else {
                    y = new YvsComptaNotifReglementDocDivers(r, selectCompte, currentUser);
                    y.setId(null);
                    y.setDateSave(new Date());
                    y = (YvsComptaNotifReglementDocDivers) dao.save1(y);
                }
                int idx = 0;

                if (compte.getAchatsEtDivers() != null ? !compte.getAchatsEtDivers().isEmpty() : false) {
                    AcomptesAchatDivers a = UtilCompta.buildBeanAcomptesAchatDivers(y);
                    idx = compte.getAchatsEtDivers().indexOf(a);
                    if (idx > -1) {
                        compte.getAchatsEtDivers().set(idx, a);

                    } else {
                        compte.getAchatsEtDivers().add(0, a);
                    }
                }
                if (selectCompte.getNotifsDivers() != null ? !selectCompte.getNotifsDivers().isEmpty() : false) {
                    idx = selectCompte.getNotifsDivers().indexOf(y);
                    if (idx > -1) {
                        selectCompte.getNotifsDivers().set(idx, y);
                    } else {
                        selectCompte.getNotifsDivers().add(0, y);
                    }
                    idx = acomptes.indexOf(selectCompte);
                    if (idx > -1) {
                        acomptes.set(idx, selectCompte);
                    }
                }

                if (piece.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                    equilibre(selectCompte);
                    compte.setReste(compte.getReste() - piece.getMontant());
                    if (dao.isComptabilise(piece.getDocDivers().getId(), Constantes.SCR_DIVERS)) {
                        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                        if (w != null) {
                            w.comptabiliserCaisseDivers(r, false, false);
                        }
                    }
                }
                service.equilibreOne(UtilCompta.buildDocDivers(piece.getDocDivers()));
            }
            if (succes) {
                succes();
            }
        }
        update("data_reglement_acompte_fa");
    }

    public void openOrfindAndRegleFacture() {
        if (displayConfirm) {
            if (agenceRegle < 1) {
                agenceRegle = currentAgence.getId();
                update("main-notif_reg_achat");
            }
            openDialog("dlgConfirNotifRegAchat");
        } else {
            findAndRegleFacture();
        }
    }

    public void findAndRegleFacture() {
        if (isFacture) {
            if (compte.getId() > 0 && compte.getFournisseur().getId() > 0) {
                //Montant restant sur l'acompte
                Double reste = AYvsComptaAcompteFournisseur.findResteForAcompte(selectCompte, dao);
                // (reste != null ? reste : 0);
                double montantResteAcompte = (reste != null ? reste : 0);
                double montantResteAPayer = 0;
                if (montantResteAcompte > 0) {
                    //Récupère les factures non payé du client
                    List<YvsComDocAchats> factures;
                    if (agenceRegle < 1) {
                        factures = dao.loadNameQueries("YvsComDocAchats.findByFournisseurNonRegle", new String[]{"fournisseur", "statutRegle"}, new Object[]{selectCompte.getFournisseur(), Constantes.ETAT_REGLE});
                    } else {
                        factures = dao.loadNameQueries("YvsComDocAchats.findByFournisseurNonRegleByAgence", new String[]{"fournisseur", "statutRegle", "agence"}, new Object[]{selectCompte.getFournisseur(), Constantes.ETAT_REGLE, new YvsAgences(agenceRegle)});
                    }
                    double montant = 0;
                    DocAchat doc;
                    if (!factures.isEmpty()) {
                        for (YvsComDocAchats d : factures) {
                            if (montantResteAcompte > 0) {
                                doc = UtilCom.buildBeanDocAchat(d);
                                setMontantTotalDoc(doc);
                                montantResteAPayer = doc.getMontantNetApayer();
                                if (montantResteAPayer > 0) {
                                    piece = new PieceTresorerie();
                                    cloneObject(piece.getDocAchat(), doc);
                                    piece.setNumRefExterne(d.getNumDoc());
                                    if (montantResteAcompte > montantResteAPayer) {
                                        montant = montantResteAPayer;
                                    } else {
                                        montant = montantResteAcompte;
                                    }
                                    piece.setMontant(montant);
                                    buildAndSavePiece(montant, false);
                                    if (compte.getStatut() == Constantes.STATUT_DOC_PAYER) {
                                        d.setStatutRegle(montant >= montantResteAPayer ? Constantes.ETAT_REGLE : Constantes.ETAT_ENCOURS);
                                    }
                                    dao.update(d);
                                }
                            } else {
                                break;
                            }
                            montantResteAcompte = montantResteAcompte - montant;
                        }
                        succes();
                    } else {
                        getInfoMessage("Aucune facture en attente de paiement n'a été trouvé !");
                    }
                    update("data_reglement_acompte");
                }
            }
        } else {
            if (compte.getId() > 0 && compte.getFournisseur().getId() > 0) {
                //Montant restant sur l'acompte
                Double reste = AYvsComptaAcompteFournisseur.findResteForAcompte(selectCompte, dao);
                // (reste != null ? reste : 0);
                double montantResteAcompte = (reste != null ? reste : 0);
                double montantResteAPayer = 0;
                if (montantResteAcompte > 0) {
                    //Récupère les factures non payé du client
                    List<YvsComptaCaisseDocDivers> docDivers;
                    if (agenceRegle < 1) {
                        docDivers = dao.loadNameQueries("YvsComptaCaisseDocDivers.findByFournisseurNonRegle", new String[]{"idTiers", "tableTiers", "statutRegler"}, new Object[]{selectCompte.getFournisseur().getId(), Constantes.BASE_TIERS_FOURNISSEUR, Constantes.ETAT_REGLE.charAt(0)});
                    } else {
                        docDivers = dao.loadNameQueries("YvsComptaCaisseDocDivers.findByFournisseurNonRegleByAgence", new String[]{"idTiers", "tableTiers", "statutRegler", "agence"}, new Object[]{selectCompte.getFournisseur().getId(), Constantes.BASE_TIERS_FOURNISSEUR, Constantes.ETAT_REGLE.charAt(0), new YvsAgences(agenceRegle)});
                    }
                    double montant = 0;
                    DocCaissesDivers doc;
                    if (!docDivers.isEmpty()) {
                        for (YvsComptaCaisseDocDivers d : docDivers) {
                            if (montantResteAcompte > 0) {
                                doc = UtilCompta.buildBeanDocCaisse(d);
                                Double montant_payer = 0.0;
                                List<YvsComptaCaissePieceDivers> pieces = dao.loadNameQueries("YvsComptaCaissePieceDivers.findByDocDiversStatut", new String[]{"docDivers", "statut"}, new Object[]{new YvsComptaCaisseDocDivers(doc.getId()), Constantes.ETAT_REGLE.charAt(0)});
                                if (!pieces.isEmpty()) {
                                    for (YvsComptaCaissePieceDivers p : pieces) {
                                        montant_payer += p.getMontant();
                                    }
                                }
                                montantResteAPayer = doc.getMontant() - montant_payer;
                                if (montantResteAPayer > 0) {
                                    piece = new PieceTresorerie();
                                    cloneObject(piece.getDocDivers(), doc);
                                    piece.setNumRefExterne(d.getNumPiece());
                                    if (montantResteAcompte > montantResteAPayer) {
                                        montant = montantResteAPayer;
                                    } else {
                                        montant = montantResteAcompte;
                                    }
                                    piece.setMontant(montant);
                                    buildAndSavePiece(montant, false);
                                }
                            }
                        }
                        succes();
                    }
                }
            }
        }

    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteBean(Object object) {
        if (operation.equals("A")) {
            selectCompte = (YvsComptaAcompteFournisseur) object;
            deleteBeanCompte();
        } else {
            selectCredit = (YvsComptaCreditFournisseur) object;
            deleteBeanCredit();
        }
    }

    public void deleteBeanCompte(YvsComptaAcompteFournisseur y) {
        selectCompte = y;
        if (!memory_choix_delete_acompte) {
            openDialog("dlgConfirmDelete");
        } else {
            deleteBeanCompte();
        }
    }

    public void deleteBeanCompte() {
        try {
            if (selectCompte != null ? selectCompte.getId() > 0 : false) {
                if (selectCompte.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    getErrorMessage("Cet acompte est déjà payé");
                    return;
                }
                dao.delete(selectCompte);
                acomptes.remove(selectCompte);
                if (selectCompte.getId().equals(compte.getId())) {
                    resetFiche();
                }
                update("data_acompte_fournisseur");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error", ex);
        }
    }

    public void deleteBeanCredit(YvsComptaCreditFournisseur y) {
        selectCredit = y;
        if (!memory_choix_delete_credit) {
            openDialog("dlgConfirmDelete");
        } else {
            deleteBeanCredit();
        }
    }

    public void deleteBeanCredit() {
        try {
            if (selectCredit != null ? selectCredit.getId() > 0 : false) {
                if (selectCredit.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                    getErrorMessage("Ce crédit est déjà validé");
                    return;
                }
                dao.delete(selectCredit);
                credits.remove(selectCredit);
                if (selectCredit.getId().equals(credit.getId())) {
                    resetFiche();
                }
                update("data_credit_fournisseur");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    public void deleteBeanReglement(YvsComptaReglementCreditFournisseur y) {
        selectReglement = y;
        if (!memory_choix_delete_reglement) {
            openDialog("dlgConfirmDeletePiece");
        } else {
            deleteBeanReglement();
        }
    }

    public void deleteBeanReglement() {
        try {
            if (selectReglement != null ? selectReglement.getId() > 0 : false) {
                if (selectReglement.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    getErrorMessage("Ce reglement est déjà payé");
                    return;
                }
                dao.delete(selectReglement);
                credit.getReglements().remove(selectReglement);
                selectCredit.getReglements().remove(selectReglement);
                int idx = credits.indexOf(selectCredit);
                if (idx > -1) {
                    credits.set(idx, selectCredit);
                }
                if (selectReglement.getId().equals(reglement.getId())) {
                    resetReglement();
                }
                update("data_reglement_credit_fa");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    public void deleteBeanPiece(YvsComptaNotifReglementAchat y) {
        liaison = y;
    }

    public void deleteBeanPieces(AcomptesAchatDivers a) {
        selectNotif = a;
        if (a.getType().equals("ACHAT")) {
            deleteBeanPiece(a.getNotifs());
        } else {
            liaison_doc = a.getNotif_divers();
        }

    }

    public void confirmDeletePieces(boolean so_piece) {
        try {
            for (AcomptesAchatDivers a : selectNotifs) {
                if (a.getType().equals("ACHAT")) {
                    confirmDeletePiece(a.getNotifs(), so_piece);
                } else {
                    confirmDeletePiece(a.getNotif_divers(), so_piece);
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    public void confirmDeletePiece(boolean so_piece) {
        try {
            if (liaison != null ? liaison.getId() > 0 : false) {
                confirmDeletePiece(liaison, so_piece);
            } else if (liaison_doc != null ? liaison_doc.getId() > 0 : false) {
                confirmDeletePiece(liaison_doc, so_piece);
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    private void confirmDeletePiece(YvsComptaNotifReglementAchat y, boolean so_piece) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (so_piece && y.getPieceAchat().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    getErrorMessage("Ce reglement est déjà payé");
                    return;
                }
                if (so_piece) {
                    dao.delete(y.getPieceAchat());
                } else {
                    dao.delete(y);
                }

                AcomptesAchatDivers b = UtilCompta.buildBeanAcomptesAchatDivers(y);
                compte.getAchatsEtDivers().remove(b);
                selectCompte.getNotifs().remove(y);

                int idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                }
                if (y.getId().equals(piece.getId())) {
                    resetFicheReglement();
                }
                ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                if (w != null) {
                    if (!so_piece) {
                        w.getPieceAchat().setNotifs(null);
                        idx = w.getSelectedDoc().getReglements().indexOf(w.getPieceAchat());
                        if (idx > -1) {
                            w.getSelectedDoc().getReglements().set(idx, w.getPieceAchat());
                        }
                    } else {
                        w.getSelectedDoc().getReglements().remove(y.getPieceAchat());
                    }
                    update("table_regFA");
                }
                update("data_reglement_acompte_fa");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    private void confirmDeletePiece(YvsComptaNotifReglementDocDivers y, boolean so_piece) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (so_piece && y.getPieceDocDivers().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    getErrorMessage("Ce reglement est déjà payé");
                    return;
                }
                dao.delete(y);
                if (so_piece) {
                    dao.delete(y.getPieceDocDivers());
                }

                AcomptesAchatDivers b = UtilCompta.buildBeanAcomptesAchatDivers(y);
                compte.getAchatsEtDivers().remove(b);
                selectCompte.getNotifsDivers().remove(y);

                int idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                }
                if (y.getId().equals(piece.getId())) {
                    resetFicheReglement();
                }
                update("data_reglement_acompte_fa");
            }

        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectDistant(YvsComptaAcompteFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            operation = "A";
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Acompte Fournisseur", "modCompta", "smenAcompteFsseur", true);
            }
        }
    }

    public void onSelectDistant(YvsComptaCreditFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            operation = "C";
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Crédit Fournisseur", "modCompta", "smenCreditFsseur", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsComptaAcompteFournisseur y) {
        selectCompte = y;
        populateView(UtilCompta.buildBeanAcompteFournisseur(y, dao));
        loadNotifAcompte(true, true);
        historiqueCompte(y.getFournisseur());
        update("blog_acompte_fournisseur");
        update("blog_entete_acompte_fournisseur");
    }

    public void onSelectObject(YvsComptaCreditFournisseur y) {
        selectCredit = y;
        populateView(UtilCompta.buildBeanCreditFournisseur(y));
        historiqueCredit(y.getFournisseur());
        update("blog_credit_fournisseur");
    }

    public void onSelectObjectByFournisseur(YvsBaseFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            codeFournisseur = y.getCodeFsseur();
            addParamFournisseur();
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            if (operation.equals("A")) {
                YvsComptaAcompteFournisseur y = (YvsComptaAcompteFournisseur) ev.getObject();
                onSelectObject(y);
            } else {
                YvsComptaCreditFournisseur y = (YvsComptaCreditFournisseur) ev.getObject();
                onSelectObject(y);
            }
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewReglement(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectReglement = (YvsComptaReglementCreditFournisseur) ev.getObject();
            populateView(UtilCompta.buildBeanReglementCredit(selectReglement));
            update("form_reglement_credit_fa_fournisseur");
        }
    }

    public void unLoadOnViewReglement(UnselectEvent ev) {
        resetReglement();
    }

    public void loadOnViewPiece(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            AcomptesAchatDivers y = (AcomptesAchatDivers) ev.getObject();
            populateView(UtilCompta.buildBeanTresoreri(y.getNotifs().getPieceAchat()));
            update("form_reglement_acompte_fa");
        }
    }

    public void unLoadOnViewPiece(UnselectEvent ev) {
        resetFicheReglement();
    }

    public void loadOnViewFournisseur(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsBaseFournisseur y = (YvsBaseFournisseur) ev.getObject();
            chooseFournisseur(UtilCom.buildBeanFournisseur(y));
        }
    }

    public void loadOnViewFacture(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComDocAchats y = (YvsComDocAchats) ev.getObject();
            chooseFacture(UtilCom.buildBeanDocAchat(y));
        }
    }

    public void chooseTypeCredit() {
        ManagedTypeCout w = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
        if (w != null) {
            int idx = w.getTypes().indexOf(new YvsGrhTypeCout(credit.getTypeCredit().getId()));
            if (idx > -1) {
                YvsGrhTypeCout y = w.getTypes().get(idx);
                credit.setTypeCredit(UtilGrh.buildBeanTypeCout(y));
            }
        }
    }

    public void chooseJournalCredit() {
        ManagedJournaux w = (ManagedJournaux) giveManagedBean(ManagedJournaux.class);
        if (w != null) {
            int idx = w.getJournaux().indexOf(new YvsComptaJournaux(credit.getJournal().getId()));
            if (idx > -1) {
                YvsComptaJournaux y = w.getJournaux().get(idx);
                credit.setJournal(UtilCompta.buildBeanJournaux(y));
            }
        }
    }

    public void chooseModeCompte() {
        ManagedModeReglement w = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
        if (w != null) {
            int idx = w.getModes().indexOf(new YvsBaseModeReglement((long) compte.getMode().getId()));
            if (idx > -1) {
                YvsBaseModeReglement y = w.getModes().get(idx);
                compte.setMode(UtilCompta.buildBeanModeReglement(y));
                cloneObject(piece.getMode(), compte.getMode());
            }
        }
    }

    public void chooseCaisseCompte() {
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            int idx = w.getCaisses().indexOf(new YvsBaseCaisse(compte.getCaisse().getId()));
            if (idx > -1) {
                YvsBaseCaisse y = w.getCaisses().get(idx);
                compte.setCaisse(UtilCompta.buildBeanCaisse(y));
            }
        }
    }

    public void chooseCaisseReglement() {
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            int idx = w.getCaisses().indexOf(new YvsBaseCaisse(reglement.getCaisse().getId()));
            if (idx > -1) {
                YvsBaseCaisse y = w.getCaisses().get(idx);
                reglement.setCaisse(UtilCompta.buildBeanCaisse(y));
            }
        }
    }

    public void chooseCaissePiece() {
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            int idx = w.getCaisses().indexOf(new YvsBaseCaisse(piece.getCaisse().getId()));
            if (idx > -1) {
                YvsBaseCaisse y = w.getCaisses().get(idx);
                piece.setCaisse(UtilCompta.buildBeanCaisse(y));
            }
        }
    }

    public void chooseFournisseur(Fournisseur d) {
        if (d != null ? d.getId() > 0 : false) {
            if (operation.equals("A")) {
                cloneObject(compte.getFournisseur(), d);
                historiqueCompte(new YvsBaseFournisseur(d.getId()));
                update("select_fournisseur_acompte_fournisseur");
            } else {
                cloneObject(credit.getFournisseur(), d);
                historiqueCredit(new YvsBaseFournisseur(d.getId()));
                update("select_fournisseur_credit_fournisseur");
            }
        }
    }

    public void chooseFacture(DocAchat d) {
        if (d != null ? d.getId() > 0 : false) {
            setMontantTotalDoc(d);
            cloneObject(piece.getDocAchat(), d);
            piece.setNumRefExterne(d.getNumDoc());
            double montant = 0;
            if (compte.getReste() > d.getMontantNetApayer()) {
                montant = d.getMontantNetApayer();
            } else {
                montant = compte.getReste();
            }
            piece.setMontant(montant);
            update("select_facture_reglement_achat");
            update("txt_montant_reglement_achat");
        }
    }

    public void loadAllReste() {
        for (YvsComptaAcompteFournisseur y : acomptes) {
            Double reste = AYvsComptaAcompteFournisseur.findResteForAcompte(y, dao);
            y.setReste((reste != null ? reste : 0));
            Double resteUnBind = AYvsComptaAcompteFournisseur.findResteUnBindForAcompte(y, dao);
            y.setResteUnBind((resteUnBind != null ? resteUnBind : 0));
        }
    }

    public void verifyComptabilisation() {
        for (AcomptesAchatDivers a : compte.getAchatsEtDivers()) {
            isComptabilisePiece(a);
        }
    }

    public boolean encaisserAcompte() {
        if (compte.getCaisse().getId() > 0 && compte.getMode().getId() > 0 && compte.getDateAcompte() != null) {
            selectCompte.setDatePaiement(compte.getDateAcompte());
            return encaisserAcompte(compte, selectCompte);
        } else {
            if (compte.getCaisse().getId() <= 0) {
                getErrorMessage("Vous devez choisir une caisse !");
            }
            if (compte.getMode().getId() <= 0) {
                getErrorMessage("Vous devez selectionner un mode d'encaissement");
            }
            if (compte.getDateAcompte() == null) {
                getErrorMessage("La date est incorrecte");
            }
            return false;
        }
    }

    public boolean encaisserAcompte(YvsComptaAcompteFournisseur y) {
        if (y.getCaisse() != null && y.getModel() != null && y.getDatePaiement() != null) {
            return encaisserAcompte(null, y);
        } else {
            if (y.getCaisse() == null) {
                getErrorMessage("Vous devez choisir une caisse !");
            }
            if (y.getModel() == null) {
                getErrorMessage("Vous devez selectionner un mode d'encaissement");
            }
            if (y.getDateAcompte() == null) {
                getErrorMessage("La date est incorrecte");
            }
            return false;
        }
    }

    public boolean encaisserAcompte(AcompteFournisseur acompte, YvsComptaAcompteFournisseur y) {
        try {
            boolean succes = true;
            if (!y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                if (!autoriser("p_caiss_payer") || !autoriser("encais_piece_espece")) {
                    openNotAcces();
                    return false;
                }
                y.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{y.getCaisse().getId()}));
                if (!controleAccesCaisse(y.getCaisse(), true)) {
                    return false;
                }
                succes = changeStatutAcompte(acompte, y, Constantes.STATUT_DOC_PAYER);
                if (succes) {
                    if (currentParam != null ? currentParam.getMajComptaAutoDivers() : true) {
                        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                        if (w != null) {
                            w.comptabiliserAcompteFournisseur(y, false, false);
                        }
                    }
                }
                if (y.getRepartirAutomatique()) {
                    if (acompte == null) {
                        selectCompte = y;
                        compte = UtilCompta.buildBeanAcompteFournisseur(y, dao);
                    }
                    openOrfindAndRegleFacture();
                }
            } else {
                //Valide et génère les phases du chèque.
                if (y.getPhasesReglement().isEmpty()) {
                    if (!autoriser("encais_piece_cheque")) {
                        openNotAcces();
                    }
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{y.getModel(), true});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhaseAcompteAchat pp;
                    if (y.getPhasesReglement() == null) {
                        y.setPhasesReglement(new ArrayList<YvsComptaPhaseAcompteAchat>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhaseAcompteAchat(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        pp.setPieceAchat(y);
                        pp.setCaisse(y.getCaisse());
                        pp.setDateSave(new Date());
                        pp.setDateUpdate(new Date());
                        pp = (YvsComptaPhaseAcompteAchat) dao.save1(pp);
                        y.getPhasesReglement().add(pp);
                    }
                    y.setPhasesReglement(ordonnePhase(y.getPhasesReglement()));
                    if (acompte != null) {
                        acompte.setPhasesReglement(y.getPhasesReglement());
                    }
                }
                if (y.getPhasesReglement().isEmpty()) {
                    succes = changeStatutAcompte(acompte, y, Constantes.STATUT_DOC_PAYER);
                    if (succes) {
                        if (currentParam != null ? currentParam.getMajComptaAutoDivers() : true) {
                            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                            if (w != null) {
                                w.comptabiliserAcompteFournisseur(y, false, false);
                            }
                        }
                    }
                }
            }
            return succes;
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
        return false;
    }

    public boolean annulerAcompte(boolean suspend) {
        suspendCompte = suspend;
        if (dao.isComptabilise(selectCompte.getId(), Constantes.SCR_ACOMPTE_ACHAT)) {
            openDialog("dlgConfirmAnnuleDoc");
            etapeCompte = null;
            return false;
        }
        return annulerAcompte(compte, selectCompte, suspend);
    }

    public boolean annulerAcompte(YvsComptaAcompteFournisseur y, boolean suspend) {
        return annulerAcompte(null, y, suspend);
    }

    public boolean annulerAcompte(AcompteFournisseur acompte, YvsComptaAcompteFournisseur y, boolean suspend) {
        try {
            if (!verifyCancelPieceCaisse(y.getDatePaiement())) {
                return false;
            }
            for (YvsComptaNotifReglementAchat n : y.getNotifs()) {
                if (n.getPieceAchat().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    getErrorMessage("Vous ne pouvez pas annuler ce paiement car l'acompte est rattaché à des pieces payées");
                    return false;
                }
            }
            if (dao.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_ACHAT)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return false;
                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    if (!w.unComptabiliserAcompteFournisseur(y, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return false;
                    }
                }
            }
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                if (!cancelAllEtapesAcompte(y, false)) {
                    return false;
                }
            }
            return changeStatutAcompte(acompte, y, (suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE));
        } catch (Exception ex) {

        }
        return false;
    }

    public boolean changeStatutAcompte(AcompteFournisseur acompte, YvsComptaAcompteFournisseur y, char statut) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                y.setStatut(statut);
                if (acompte != null) {
                    y.setCaisse(UtilCompta.buildBeanCaisse(acompte.getCaisse()));
                    ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                    if (w != null) {
                        int idx = w.getCaisses().indexOf(y.getCaisse());
                        if (idx > -1) {
                            YvsBaseCaisse c = w.getCaisses().get(idx);
                            y.setCaisse(c);
                        }
                    }
                    y.setDateAcompte(acompte.getDateAcompte());
                    acompte.setStatut(statut);
                }
                dao.update(y);
                int idx = acomptes.indexOf(y);
                if (idx > -1) {
                    acomptes.set(idx, y);
                    update("data_acompte_fournisseur");
                }
                idx = versements.indexOf(y);
                if (idx > -1) {
                    versements.set(idx, y);
                    update("data_historique_acompte_fournisseur");
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
        return false;
    }

    public boolean validerCredit() {
        return validerCredit(credit, selectCredit);
    }

    public boolean validerCredit(YvsComptaCreditFournisseur y) {
        return validerCredit(null, y);
    }

    public boolean validerCredit(CreditFournisseur credit, YvsComptaCreditFournisseur y) {
        boolean succes = changeStatutCredit(credit, y, Constantes.STATUT_DOC_VALIDE);
        if (succes) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                w.comptabiliserCreditFournisseur(y, false, false);
                update("data_reglement_credit_fa");
            }
        }
        return succes;
    }

    public void editableCredit() {
        if (!credit.canEditable()) {
            getErrorMessage("Vous ne pouvez pas modifier ce document. Il est rattaché à des reglements payés");
            return;
        }
        this.suspendCredit = false;
        if (dao.isComptabilise(credit.getId(), Constantes.SCR_CREDIT_ACHAT)) {
            openDialog("dlgConfirmAnnuleDoc");
            return;
        }
        if (!needConfirmationCredit) {
            openDialog("dlgAnnulerDoc");
            return;
        }
        annulerCredit(false);
    }

    public void refuserCredit() {
        if (!credit.canEditable()) {
            getErrorMessage("Vous ne pouvez pas modifier ce document. Il est rattaché à des reglements payés");
            return;
        }
        this.suspendCredit = true;
        if (dao.isComptabilise(credit.getId(), Constantes.SCR_CREDIT_ACHAT)) {
            openDialog("dlgConfirmAnnuleDoc");
            return;
        }
        if (!needConfirmationCredit) {
            openDialog("dlgRefuserDoc");
            return;
        }
        annulerCredit(true);
    }

    public boolean annulerCredit(boolean suspend) {
        return annulerCredit(credit, selectCredit, suspend);
    }

    public boolean annulerCredit(YvsComptaCreditFournisseur y, boolean suspend) {
        return annulerCredit(null, y, suspend);
    }

    public boolean annulerCredit(CreditFournisseur credit, YvsComptaCreditFournisseur y, boolean suspend) {
        if (!credit.canEditable()) {
            getErrorMessage("Vous ne pouvez pas modifier ce document. Il est rattaché à des reglements payés");
            return false;
        }
        if (dao.isComptabilise(y.getId(), Constantes.SCR_CREDIT_ACHAT)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return false;
            }
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                if (!w.unComptabiliserCreditFournisseur(y, false)) {
                    getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                    return false;
                }
            }
        }
        return changeStatutCredit(credit, y, (suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE));
    }

    public boolean changeStatutCredit(CreditFournisseur credit, YvsComptaCreditFournisseur y, char statut) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                y.setDateCredit(credit.getDateCredit());
                y.setStatut(statut);
                //evité la mise à jour en cascade
                List<YvsComptaReglementCreditFournisseur> list = new ArrayList<>();
                list.addAll(y.getReglements());
                if (y.getReglements() != null) {
                    y.getReglements().clear();
                }
                dao.update(y);
                y.setReglements(list);
                if (credit != null) {
                    credit.setStatut(statut);
                    if (credit.getReglements() != null ? credit.getReglements().isEmpty() : true) {
                        credit.setReglements(list);
                    }
                }
                int idx = credits.indexOf(y);
                if (idx > -1) {
                    credits.set(idx, y);
                    update("data_credit_fournisseur");
                }
                idx = redevances.indexOf(y);
                if (idx > -1) {
                    redevances.set(idx, y);
                    update("data_historique_credit_fournisseur");
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
        return false;
    }

    public boolean encaisserReglement(YvsComptaReglementCreditFournisseur bean) {
        bean.setDatePaiement(bean.getDateReg());
        return encaisserReglement(selectCredit, bean);
    }

    public boolean encaisserReglement(YvsComptaCreditFournisseur credit, YvsComptaReglementCreditFournisseur y) {
        if (y.getCaisse() == null) {
            getErrorMessage("Vous devez choisir une caisse !");
            return false;
        }
        if (y.getModel() == null) {
            getErrorMessage("Vous devez selectionner un mode d'encaissement");
            return false;
        }
        if (y.getDatePaiement() == null) {
            getErrorMessage("La date est incorrecte");
            return false;
        }
        if (credit.getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
            double montant = 0;
            for (YvsComptaReglementCreditFournisseur r : credit.getReglements()) {
                if (!r.getId().equals(y.getId())) {
                    if (r.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                        montant += r.getValeur();
                    }
                }
            }
            if ((montant + y.getValeur()) > credit.getMontant()) {
                getErrorMessage("Vous ne pouvez pas valider ce reglement car la somme des reglements sera superieure au montant du crédit");
                return false;
            }
            if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                //si on veux valider le paiement
                if (y.getPhasesReglement().isEmpty()) {
                    if (!autoriser("encais_piece_cheque")) {
                        openNotAcces();
                        return false;
                    }
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{y.getModel(), true});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhaseReglementCreditFournisseur pp;
                    if (y.getPhasesReglement() == null) {
                        y.setPhasesReglement(new ArrayList<YvsComptaPhaseReglementCreditFournisseur>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhaseReglementCreditFournisseur(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        pp.setReglement(y);
                        pp.setCaisse(y.getCaisse());
                        pp = (YvsComptaPhaseReglementCreditFournisseur) dao.save1(pp);
                        y.getPhasesReglement().add(pp);
                    }
                    return true;
                } else {
                    getWarningMessage("Les phases de ce règlement ont déjà été générées !");
                }
            } else {
                y.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{y.getCaisse().getId()}));
                if (!autoriser("p_caiss_payer") || !autoriser("encais_piece_espece")) {
                    openNotAcces();
                    return false;
                }
                if (!controleAccesCaisse(y.getCaisse(), true)) {
                    return false;
                }
                boolean succes = changeStatutReglement(credit, y, Constantes.STATUT_DOC_PAYER);
                if (succes) {
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (w != null) {
                        w.comptabiliserCaisseCreditAchat(y, false, false);
                    }
                }
                return succes;
            }
        } else {
            getErrorMessage("Le crédit n'est pas validé");
        }
        return false;
    }

    public boolean annulerReglement(YvsComptaReglementCreditFournisseur y, boolean suspend) {
        suspendReglementCredit = suspend;
        if (dao.isComptabilise(y.getId(), Constantes.SCR_CAISSE_CREDIT_ACHAT)) {
            openDialog("dlgConfirmAnnulePiece");
            selectReglement = y;
            etapeCredit = null;
            return false;
        }
        return annulerReglement(selectCredit, y, suspend);
    }

    public boolean annulerReglement(YvsComptaCreditFournisseur credit, YvsComptaReglementCreditFournisseur y, boolean suspend) {
        try {
            if (!verifyCancelPieceCaisse(y.getDatePaiement())) {
                return false;
            }
            if (dao.isComptabilise(y.getId(), Constantes.SCR_CAISSE_CREDIT_ACHAT)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return false;
                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    if (!w.unComptabiliserCaisseCreditAchat(y, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return false;
                    }
                }
            }
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                if (!cancelAllEtapesCredit()) {
                    return false;
                }
            }
            y.setDatePaiement(null);
            return changeStatutReglement(credit, y, (suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE));
        } catch (Exception ex) {

        }
        return false;
    }

    public boolean changeStatutReglement(YvsComptaCreditFournisseur credit, YvsComptaReglementCreditFournisseur y, char statut) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setStatut(statut);
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                dao.update(y);
                int idx = credit.getReglements().indexOf(y);
                if (idx > -1) {
                    credit.getReglements().set(idx, y);
                }
                idx = credits.indexOf(credit);
                if (idx > -1) {
                    credits.set(idx, credit);
                }
                update("data_reglement_credit_fa");
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
        return false;
    }

    public void encaisserPiece(YvsComptaNotifReglementAchat y) {
        if (selectCompte.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
            Double totalPayer = (Double) dao.loadObjectByNameQueries("YvsComptaNotifReglementAchat.findSumByAcompte", new String[]{"acompte", "statut"}, new Object[]{selectCompte, Constantes.STATUT_DOC_PAYER});
            // (reste != null ? reste : 0);
            double montant = y.getPieceAchat().getMontant() + (totalPayer != null ? totalPayer : 0);
            if (montant > compte.getMontant()) {
                getErrorMessage("Vous ne pouvez pas valider ce montant.. car la somme des pièces excedera le montant de l'acompte");
                return;
            }
            changeStatutPiece(y, Constantes.STATUT_DOC_PAYER);
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (y.getPieceAchat().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                if (dao.isComptabilise(y.getPieceAchat().getAchat().getId(), Constantes.SCR_VENTE)) {
                    if (w != null) {
                        w.comptabiliserCaisseAchat(y.getPieceAchat(), false, false);
                    }
                }
            }
            Map<String, String> statuts = dao.getEquilibreAchat(y.getPieceAchat().getAchat().getId());
            if (statuts != null) {
                y.getPieceAchat().getAchat().setStatutLivre(statuts.get("statut_livre"));
                y.getPieceAchat().getAchat().setStatutRegle(statuts.get("statut_regle"));
            }
        } else {
            getErrorMessage("Cette acompte n'est pas encore encaissé");
        }
    }

    public void encaisserPieces(AcomptesAchatDivers a) {
        if (a.getType().equals("ACHAT")) {
            YvsComptaNotifReglementAchat y = new YvsComptaNotifReglementAchat();
            y = a.getNotifs();
            encaisserPiece(y);
        } else {
            if (a.getType().equals("OD_A")) {
                YvsComptaNotifReglementDocDivers z = a.getNotif_divers();
                if (selectCompte.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    Double totalPayer = (Double) dao.loadObjectByNameQueries("YvsComptaNotifReglementAchat.findSumByAcompte", new String[]{"acompte", "statut"}, new Object[]{selectCompte, Constantes.STATUT_DOC_PAYER});
                    double montant = z.getPieceDocDivers().getMontant() + (totalPayer != null ? totalPayer : 0);
                    if (montant > compte.getMontant()) {
                        getErrorMessage("Vous ne pouvez pas valider ce montant.. car la somme des pièces excedera le montant de l'acompte");
                        return;
                    }
                    z.getPieceDocDivers().setDatePaimentPrevu(z.getPieceDocDivers().getDatePaimentPrevu());
                    z.getPieceDocDivers().setValiderBy(currentUser.getUsers());
                    z.getPieceDocDivers().setDateValider(new Date());
                    z.getPieceDocDivers().setMouvement(Constantes.MOUV_CAISS_ENTREE);
                    changeStatutPiece(z, Constantes.STATUT_DOC_PAYER);
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    ManagedDocDivers d = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                    if (z.getPieceDocDivers().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                        if (dao.isComptabilise(z.getPieceDocDivers().getDocDivers().getId(), Constantes.SCR_VENTE)) {
                            if (w != null) {
                                w.comptabiliserCaisseDivers(z.getPieceDocDivers(), false, false);
                            }
                            if (d != null) {
                                d.equilibreOne(z.getPieceDocDivers().getDocDivers());
                            }
                        }
                    }
                    AcomptesAchatDivers ad = UtilCompta.buildBeanAcomptesAchatDivers(z);
                    int index = compte.getAchatsEtDivers().indexOf(ad);
                    if (index > -1) {
                        compte.getAchatsEtDivers().set(index, ad);
                    }
                    update("data_reglement_acompte_fa");

                } else {
                    getErrorMessage("Cette acompte n'est pas encore encaissé");
                }
            }
        }

    }

    public void encaisserPiecesAll() {
        for (AcomptesAchatDivers a : selectNotifs) {
            encaisserPieces(a);
        }
    }

    public void annulerPiecesAll(boolean suspend) {
        for (AcomptesAchatDivers a : selectNotifs) {
            annulerPieces(a, suspend);
        }
    }

    public void annulerPiece(YvsComptaNotifReglementAchat y, boolean suspend) {
        changeStatutPiece(y, suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE);
    }

    public void annulerPieces(AcomptesAchatDivers a, boolean suspend) {
        if (a.getType().equals("ACHAT")) {
            YvsComptaNotifReglementAchat y = new YvsComptaNotifReglementAchat();
            y = a.getNotifs();
            annulerPiece(y, suspend);
        } else {
            if (a.getType().equals("OD_A")) {
                YvsComptaNotifReglementDocDivers z = a.getNotif_divers();
                changeStatutPiece(z, suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE);
            }
        }
    }

    public void changeStatutPiece(YvsComptaNotifReglementDocDivers y, char statut) {
        try {
            if (y != null ? (y.getId() > 0 ? (y.getPieceDocDivers() != null ? y.getPieceDocDivers().getId() > 0 : false) : false) : false) {
                y.getPieceDocDivers().setStatutPiece(statut);
                dao.update(y.getPieceDocDivers());
                int idx = selectCompte.getNotifsDivers().indexOf(y);
                if (idx > -1) {
                    selectCompte.getNotifsDivers().set(idx, y);
                }
                idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                }
                ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                if (w != null ? w.getSelectDoc() != null : false) {
                    idx = w.getSelectDoc().getReglements().indexOf(y.getPieceDocDivers());
                    if (idx > -1) {
                        w.getSelectDoc().getReglements().set(idx, y.getPieceDocDivers());
                        update("table_regFV");
                    }
                }
                // (reste != null ? reste : 0);
                Double reste = AYvsComptaAcompteFournisseur.findResteForAcompte(selectCompte, dao);
                Double resteUnBind = AYvsComptaAcompteFournisseur.findResteUnBindForAcompte(selectCompte, dao);
                compte.setReste((reste != null ? reste : 0));
                compte.setResteUnBlind((resteUnBind != null ? resteUnBind : 0));
                w.equilibreOne(y.getPieceDocDivers().getDocDivers());
                equilibre(selectCompte);
                AcomptesAchatDivers a = UtilCompta.buildBeanAcomptesAchatDivers(y);
                int index = compte.getAchatsEtDivers().indexOf(a);
                if (index > -1) {
                    compte.getAchatsEtDivers().set(index, a);
                }
                update("data_reglement_acompte_fa");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    public void changeStatutPiece(YvsComptaNotifReglementAchat y, char statut) {
        try {
            if (y != null ? (y.getId() > 0 ? (y.getPieceAchat() != null ? y.getPieceAchat().getId() > 0 : false) : false) : false) {
                y.getPieceAchat().setStatutPiece(statut);
                dao.update(y.getPieceAchat());
                int idx = selectCompte.getNotifs().indexOf(y);
                if (idx > -1) {
                    selectCompte.getNotifs().set(idx, y);
                }
                idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                }
                //modifie la vue
                for (AcomptesAchatDivers a : compte.getAchatsEtDivers()) {
                    if (a.getNotifs().equals(y)) {
                        a.setNotifs(y);
                        a.setStatutPiece(y.getPieceAchat().getStatutPiece().toString());
                        break;
                    }
                }
                ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                if (w != null ? w.getSelectedDoc() != null : false) {
                    idx = w.getSelectedDoc().getReglements().indexOf(y.getPieceAchat());
                    if (idx > -1) {
                        w.getSelectedDoc().getReglements().set(idx, y.getPieceAchat());
                        update("table_regFA");
                    }
                }
                // (reste != null ? reste : 0);
                Double reste = AYvsComptaAcompteFournisseur.findResteForAcompte(selectCompte, dao);
                Double resteUnBind = AYvsComptaAcompteFournisseur.findResteUnBindForAcompte(selectCompte, dao);
                compte.setReste((reste != null ? reste : 0));
                compte.setResteUnBlind((resteUnBind != null ? resteUnBind : 0));
                Map<String, String> statuts = dao.getEquilibreAchat(y.getPieceAchat().getAchat().getId());
                if (statuts != null) {
                    y.getPieceAchat().getAchat().setStatutLivre(statuts.get("statut_livre"));
                    y.getPieceAchat().getAchat().setStatutRegle(statuts.get("statut_regle"));
                }
                equilibre(selectCompte);
                update("data_reglement_acompte_fa");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    public void searchFournisseur() {
        String num;
        if (operation.equals("A")) {
            num = compte.getFournisseur().getCodeFsseur();
            compte.getFournisseur().setId(0);
            compte.getFournisseur().setError(true);
            compte.getFournisseur().setTiers(new Tiers());
        } else {
            num = credit.getFournisseur().getCodeFsseur();
            credit.getFournisseur().setId(0);
            credit.getFournisseur().setError(true);
            credit.getFournisseur().setTiers(new Tiers());
        }
        ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (m != null) {
            Fournisseur y = m.searchFsseur(num, true);
            if (m.getFournisseurs() != null ? !m.getFournisseurs().isEmpty() : false) {
                if (m.getFournisseurs().size() > 1) {
                    update("data_fournisseur_acompte_fournisseur");
                } else {
                    chooseFournisseur(y);
                }
                if (operation.equals("A")) {
                    compte.getFournisseur().setError(false);
                } else {
                    credit.getFournisseur().setError(false);
                }
            }
        }
    }

    public void searchFacture() {
        if (isFacture) {
            String num = piece.getDocAchat().getNumDoc();
            piece.getDocAchat().setId(0);
            piece.getDocAchat().setError(true);
            ManagedFactureAchat m = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (m != null) {
                DocAchat y = m.searchFacture(num, compte.getFournisseur(), true);
                if (m.getDocuments() != null ? !m.getDocuments().isEmpty() : false) {
                    if (m.getDocuments().size() > 1) {
                        update("data_facture_reglement_achat");
                    } else {
                        chooseFacture(y);
                    }
                    piece.getDocAchat().setError(false);
                }
            }
        } else {
            String num = piece.getDocDivers().getNumPiece();
            piece.getDocDivers().setId(0);
            ManagedDocDivers d = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            if (d != null) {
                d.clearParams();
                d.setCodeTiers(compte.getFournisseur().getTiers().getCodeTiers());
                d.searchByNumAndType(num, "D");
                DocCaissesDivers y;
                if (d.getDocuments() != null ? !d.getDocuments().isEmpty() : false) {
                    if (d.getDocuments().size() > 1) {
                        update("data_facture_reglement_achat");
                        openDialog("dlgListDoc");
                    } else {
                        y = UtilCompta.buildBeanDocCaisse(d.getDocuments().get(0));
                        chooseDocDivers(y);
                    }

                } else {
                    trouberOD = false;
                }
            }

        }
    }

    public void loadOnViewDocDivers(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaisseDocDivers y = (YvsComptaCaisseDocDivers) ev.getObject();
            chooseDocDivers(UtilCompta.buildBeanDocCaisse(y));
        }
    }

    public void chooseDocDivers(DocCaissesDivers d) {
        if (d != null ? d.getId() > 0 : false) {
            cloneObject(piece.getDocDivers(), d);

            piece.setNumRefExterne(d.getNumPiece());
            double montant = d.getMontant();
            System.err.println("compte.getReste() :" + compte.getReste());
            YvsComptaCaisseDocDivers doc = new YvsComptaCaisseDocDivers(d.getId());
            ManagedDocDivers m = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            m.setSelectDoc(doc);
            Double montant_reste = 0.0;
            montant_reste = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceDivers.findSumMontantByDoc", new String[]{"docDivers"}, new Object[]{doc});
            montant_reste = montant_reste != null ? montant_reste : 0;
            System.err.println("montant :" + montant_reste);
            if (d.getMontant() <= montant_reste) {
                getErrorMessage("Ce document est entièrement réglé !");
                return;
            }
            montant_reste = d.getMontant() - montant_reste;
            if (compte.getReste() > montant_reste) {

                montant = montant_reste;
            } else {
                montant = compte.getReste();
            }
            piece.setMontant(montant);

            update("select_facture_reglement_achat");
            update("txt_montant_reglement_achat");
//            } else {
//                getErrorMessage("Ce document est entièrement réglé !");
//            }

        }
    }

    public void initFournisseurs() {
        ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (m != null) {
            m.initFsseurs(compte.getFournisseur());
        }
        update("data_fournisseur_acompte_fournisseur");
    }

    public void initFacture() {
        if (isFacture) {
            ManagedFactureAchat m = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (m != null) {
                m.initFacture(piece.getDocAchat(), compte.getFournisseur());
            }
            update("data_facture_reglement_achat");
        } else {
            ManagedDocDivers d = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            if (d != null) {
                d.clearParams();
                d.setCodeTiers(compte.getFournisseur().getTiers().getCodeTiers());
                d.addParamTiers("D");

            }
            update("data_facture_reglement_doc_divers");
        }

    }

    public void cleanParams() {
        if (operation.equals("A")) {
            paginator.getParams().clear();
        } else {
            paginators.getParams().clear();
        }
        loadAll(true, true);
    }

    public void addParamDate() {
        if (operation.equals("A")) {
            ParametreRequete p = new ParametreRequete("y.dateAcompte", "dates", null);
            if (addDate) {
                p = new ParametreRequete("y.dateAcompte", "dates", dateDebut, dateFin, "BETWEEN", "AND");
            }
            paginator.addParam(p);
        } else {
            ParametreRequete p = new ParametreRequete("y.dateCredit", "dates", null);
            if (addDate) {
                p = new ParametreRequete("y.dateCredit", "dates", dateDebut, dateFin, "BETWEEN", "AND");
            }
            paginators.addParam(p);
        }
        loadAll(true, true);
    }

    public void addParamAgence() {
        if (operation.equals("A")) {
            ParametreRequete p = new ParametreRequete("y.caisse.journal.agence", "agence", null, "=", "AND");
            if (agenceSearch > 0) {
                p = new ParametreRequete("y.caisse.journal.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
            }
            paginator.addParam(p);
        } else {
            ParametreRequete p = new ParametreRequete("y.journal.agence", "agence", null, "=", "AND");
            if (agenceSearch > 0) {
                p = new ParametreRequete("y.journal.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
            }
            paginators.addParam(p);
        }
        loadAll(true, true);
    }

    public void addParamDates(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            addParamDate();
        }
    }

    public void addParamStatut() {
        ParametreRequete p = new ParametreRequete("y.statut", "statut", null);
        if (statutSearch != null ? statutSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statut", "statut", statutSearch.charAt(0), "=", "AND");
        }
        if (operation.equals("A")) {
            paginator.addParam(p);
        } else {
            paginators.addParam(p);
        }
        loadAll(true, true);
    }

    public void addParamNature() {
        ParametreRequete p = new ParametreRequete("y.nature", "nature", null);
        if (natureSearch != null ? natureSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.nature", "nature", natureSearch.charAt(0), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamNotif() {
        ParametreRequete p = new ParametreRequete("y.statutNotif", "notifier", null);
        if (notifSearch != null ? notifSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutNotif", "notifier", notifSearch.charAt(0), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamTypeMode() {
        ParametreRequete p = new ParametreRequete("y.model.typeReglement", "typemode", null);
        if (modeSearch != null ? modeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.model.typeReglement", "typemode", modeSearch, "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamFournisseur() {
        ParametreRequete p = new ParametreRequete("y.fournisseur", "fournisseur", null);
        if (codeFournisseur != null ? codeFournisseur.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "fournisseur", "%" + codeFournisseur + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fournisseur.codeFsseur)", "fournisseur", codeFournisseur.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fournisseur.nom)", "fournisseur", codeFournisseur.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.fournisseur.prenom)", "fournisseur", codeFournisseur.toUpperCase() + "%", "LIKE", "OR"));
        }
        if (operation.equals("A")) {
            paginator.addParam(p);
        } else {
            paginators.addParam(p);
        }
        loadAll(true, true);
    }

    public void addParamCaisse() {
        ParametreRequete p = new ParametreRequete("y.caisse", "caisse", null);
        if (caisseSearch > 0) {
            p = new ParametreRequete("y.caisse", "caisse", new YvsBaseCaisse(caisseSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query;
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (operation.equals("A")) {
                query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_acompte_fournisseur c RIGHT JOIN yvs_compta_acompte_fournisseur y ON c.acompte = y.id "
                        + "INNER JOIN yvs_base_caisse e ON y.caisse = e.id INNER JOIN yvs_compta_journaux h ON e.journal = h.id "
                        + "INNER JOIN yvs_agences a ON h.agence = a.id WHERE y.statut = 'P' AND a.societe = ? "
                        + "AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
                if (addDate) {
                    query += " AND y.date_acompte BETWEEN ? AND ?";
                    param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)};
                }
            } else {
                query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_credit_fournisseur c RIGHT JOIN yvs_compta_credit_fournisseur y ON c.credit = y.id "
                        + "INNER JOIN yvs_base_fournisseur h ON y.fournisseur = h.id "
                        + "INNER JOIN yvs_base_tiers a ON h.tiers = a.id WHERE y.statut = 'V' AND a.societe = ? "
                        + "AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
                if (addDate) {
                    query += " AND y.date_credit BETWEEN ? AND ?";
                    param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)};
                }
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, param);
            nbrComptaSearch = count != null ? count : 0;
        }
        if (operation.equals("A")) {
            paginator.addParam(p);
        } else {
            paginators.addParam(p);
        }
        loadAll(true, true);
    }

    public void addParamReference() {
        if (operation.equals("A")) {
            ParametreRequete p = new ParametreRequete("y.numRefrence", "reference", null);
            if (numSearch != null ? numSearch.trim().length() > 0 : false) {
                p = new ParametreRequete(null, "reference", "%" + numSearch + "%", "LIKE", "AND");
                p.getOtherExpression().add(new ParametreRequete("y.numRefrence", "reference", "%" + numSearch + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("y.referenceExterne", "reference", "%" + numSearch + "%", "LIKE", "OR"));
            }
            paginator.addParam(p);
        } else {
            ParametreRequete p = new ParametreRequete("y.numReference", "reference", null);
            if (numSearch != null ? numSearch.trim().length() > 0 : false) {
                p = new ParametreRequete("y.numReference", "reference", "%" + numSearch + "%", "LIKE", "AND");
            }
            paginators.addParam(p);
        }
        loadAll(true, true);
    }

    //charge les pièces encaisser et non encore lié à une facture
    public void addParamNotBind(String fournisseur) {
        statutSearch = Constantes.ETAT_REGLE;
        paginator.addParam(new ParametreRequete("y.statut", "statut", Constantes.STATUT_DOC_PAYER, "=", "AND"));
        codeFournisseur = fournisseur;
        addParamFournisseur();
        update("table_acompte_fsseurs");
        openDialog("dlgSaveAvance");
    }

    public YvsComptaNotifReglementAchat confirmBind(YvsComptaAcompteFournisseur y, YvsComptaCaissePieceAchat c) {
        if ((y != null ? y.getId() > 0 : false) && (c != null ? c.getId() > 0 : false)) {
            champ = new String[]{"piece", "acompte"};
            val = new Object[]{c, y};
            nameQueri = "YvsComptaNotifReglementAchat.findOne";
            YvsComptaNotifReglementAchat e = (YvsComptaNotifReglementAchat) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (e != null ? e.getId() < 1 : true) {
                e = new YvsComptaNotifReglementAchat();
                e.setAcompte(y);
                e.setAuthor(currentUser);
                e.setDateSave(new Date());
                e.setDateUpdate(new Date());
                e.setPieceAchat(c);
            }
            if (e != null) {
                if (e.getId() > 0) {
                    e.setAuthor(currentUser);
                    e.setDateUpdate(new Date());
                    dao.update(e);
                } else {
                    e.setId(null);
                    e = (YvsComptaNotifReglementAchat) dao.save1(e);
                }
                int idx = y.getNotifs().indexOf(e);
                if (idx > -1) {
                    y.getNotifs().set(idx, e);
                } else {
                    y.getNotifs().add(0, e);
                }
                idx = acomptes.indexOf(y);
                if (idx > -1) {
                    acomptes.set(idx, y);
                }
                succes();
                return e;
            }
        }
        return null;
    }

    private boolean controleValidation(YvsComptaPhaseAcompteAchat pp) {
        if ((pp.getPieceAchat().getCaisse() != null) ? pp.getPieceAchat().getCaisse().getId() <= 0 : true) {
            getErrorMessage("Aucune banque n'a été trouvé !");
            return false;
        }
        return true;
    }

    private boolean controleValidation(YvsComptaPhaseReglementCreditFournisseur pp) {
        if ((pp.getReglement().getCaisse() != null) ? pp.getReglement().getCaisse().getId() <= 0 : true) {
            getErrorMessage("Aucune banque n'a été trouvé !");
            return false;
        }
        return true;
    }

    public void comptabiliserPhaseAcompteAchat(YvsComptaPhaseAcompteAchat pp) {
        etapeCompte = pp;
        if (compte.getPhasesReglement() != null ? !compte.getPhasesReglement().isEmpty() : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (w
                    != null) {
                int idx = compte.getPhasesReglement().indexOf(pp);
                if (idx > -1) {
                    if (idx == 0) {
                        w.comptabiliserPhaseAcompteAchat(pp, true, true);
                    } else {
                        YvsComptaPhaseAcompteAchat prec = compte.getPhasesReglement().get(idx - 1);
                        if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT)) {
                            w.comptabiliserPhaseAcompteAchat(pp, true, true);
                        } else {
                            openDialog("dlgComptabilisePhaseByForce");
                        }
                    }
                }
            }
        }
    }

    public void validEtapesAcompte(YvsComptaPhaseAcompteAchat pp) {
        pp.setDateValider(new Date());
        validEtapesAcompte(selectCompte, pp);
    }

    public boolean validEtapesAcompte(YvsComptaAcompteFournisseur selectCompte, YvsComptaPhaseAcompteAchat pp) {
        if (!pp.getPhaseOk()) {
            if (!asDroitValidePhase(pp.getPhaseReg())) {
                openNotAcces();
                return false;
            }
            if (controleValidation(pp)) {
                if (pp.getPhaseReg().getActionInBanque()) {
                    if (!pp.getCaisse().getTypeCaisse().equals("BANQUE")) {
                        getErrorMessage("La validation de cette phase doit se passer dans une banque");
                        return false;
                    }
                }
                pp.setPhaseOk(true);
                pp.setDateUpdate(new Date());
                pp.setAuthor(currentUser);
                pp.setStatut(Constantes.STATUT_DOC_VALIDE);
                pp.setDateValider(pp.getDateValider() != null ? pp.getDateValider() : (selectCompte.getDatePaiement() != null ? selectCompte.getDatePaiement() : new Date()));
                pp.getPieceAchat().setCaisse(selectCompte.getCaisse());
                if (pp.getPhaseReg().getReglementOk()) {
                    pp.getPieceAchat().setStatut(Constantes.STATUT_DOC_PAYER);
                    pp.getPieceAchat().setDatePaiement(compte.getDateAcompte());
                } else {
                    if (pp.getPieceAchat().getStatut() != Constantes.STATUT_DOC_PAYER) {
                        pp.getPieceAchat().setStatut(Constantes.STATUT_DOC_ENCOUR);
                        pp.getPieceAchat().setDatePaiement(null);
                    }
                }
                dao.update(pp.getPieceAchat());
                dao.update(pp);
                selectCompte.setStatut(pp.getPieceAchat().getStatut());
                selectCompte.setDatePaiement(pp.getDateValider());
                compte.setStatut(pp.getPieceAchat().getStatut());
                pp.setEtapeActive(false);
                int idx = selectCompte.getPhasesReglement().indexOf(pp);
                if (idx >= 0 && (idx + 1) < selectCompte.getPhasesReglement().size()) {
                    selectCompte.getPhasesReglement().get(idx + 1).setEtapeActive(true);
                    currentPhaseAcompteAchat = selectCompte.getPhasesReglement().get(idx + 1);
                } else if (idx == (selectCompte.getPhasesReglement().size() - 1)) {
                    selectCompte.getPhasesReglement().get(idx).setEtapeActive(true);
                }
                idx = selectCompte.getPhasesReglement().indexOf(pp);
                if (idx >= 0) {
                    selectCompte.getPhasesReglement().set(idx, pp);
                }
                currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                if (currentParam == null) {
                    currentParam = new YvsComptaParametre();

                }
                if (currentParam.getMajComptaAutoDivers()) {
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                    );
                    if (w
                            != null) {
                        w.comptabiliserPhaseAcompteAchat(pp, false, false);
                    }
                }
                idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                    update("data_acompte_fournisseur");
                }
                idx = versements.indexOf(selectCompte);
                if (idx > -1) {
                    versements.set(idx, selectCompte);
                    update("data_historique_acompte_fournisseur");
                }
                return true;
            }
        } else {
            getWarningMessage("Phase déjà valide! ");
        }
        update("head_form_suivi_paa");
        return false;
    }

    public boolean validEtapesCredit(YvsComptaPhaseReglementCreditFournisseur pp) {
        return validEtapesCredit(selectReglement, pp);
    }

    public boolean validEtapesCredit(YvsComptaReglementCreditFournisseur selectReglement, YvsComptaPhaseReglementCreditFournisseur pp) {
        if (!pp.getPhaseOk()) {
            if (!asDroitValidePhase(pp.getPhaseReg())) {
                openNotAcces();
                return false;
            }
            if (controleValidation(pp)) {
                if (pp.getPhaseReg().getActionInBanque()) {
                    if (!pp.getCaisse().getTypeCaisse().equals("BANQUE")) {
                        getErrorMessage("La validation de cette phase doit se passer dans une banque");
                        return false;
                    }
                }
                pp.setPhaseOk(true);
                pp.setDateUpdate(new Date());
                pp.setAuthor(currentUser);
                pp.setStatut(Constantes.STATUT_DOC_VALIDE);
                pp.setDateValider(pp.getDateValider() != null ? pp.getDateValider() : (selectReglement.getDatePaiement() != null ? selectReglement.getDatePaiement() : new Date()));
                dao.update(pp);
                pp.setEtapeActive(false);

                if (pp.getPhaseReg().getReglementOk()) {
                    pp.getReglement().setStatut(Constantes.STATUT_DOC_PAYER);
                    pp.getReglement().setDatePaiement(pp.getDateValider());
                } else {
                    if (pp.getReglement().getStatut() != Constantes.STATUT_DOC_PAYER) {
                        pp.getReglement().setStatut(Constantes.STATUT_DOC_ENCOUR);
                        pp.getReglement().setDatePaiement(null);
                    }
                }
                pp.getReglement().setDateUpdate(new Date());
                pp.getReglement().setAuthor(currentUser);
                dao.update(pp.getReglement());

                selectReglement.setStatut(pp.getReglement().getStatut());
                selectReglement.setDatePaiement(pp.getReglement().getDatePaiement());
                int idx = selectReglement.getPhasesReglement().indexOf(pp);
                if (idx >= 0) {
                    selectReglement.getPhasesReglement().set(idx, pp);
                }
                idx = selectReglement.getPhasesReglement().indexOf(pp);
                if (idx >= 0 && (idx + 1) < selectReglement.getPhasesReglement().size()) {
                    selectReglement.getPhasesReglement().get(idx + 1).setEtapeActive(true);
                    currentPhaseCreditAchat = selectReglement.getPhasesReglement().get(idx + 1);
                } else if (idx == (selectReglement.getPhasesReglement().size() - 1)) {
                    selectReglement.getPhasesReglement().get(idx).setEtapeActive(true);

                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                );
                if (w
                        != null) {
                    w.comptabiliserPhaseCaisseCreditAchat(pp, false, false);
                }

                return true;
            }
        } else {
            getWarningMessage("Phase déjà valide! ");
        }
        update("head_form_suivi_pca");
        return false;
    }

    public
            void chooseCaissePhaseAcompte() {
        if (currentPhaseAcompteAchat.getCaisse() != null ? currentPhaseAcompteAchat.getCaisse().getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class
            );
            if (w
                    != null) {
                int idx = w.getCaisses().indexOf(currentPhaseAcompteAchat.getCaisse());
                if (idx > -1) {
                    currentPhaseAcompteAchat.setCaisse(new YvsBaseCaisse(w.getCaisses().get(idx)));
                }
            }
        }
    }

    public
            void chooseCaissePhaseCredit() {
        if (currentPhaseCreditAchat.getCaisse() != null ? currentPhaseCreditAchat.getCaisse().getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class
            );
            if (w
                    != null) {
                int idx = w.getCaisses().indexOf(currentPhaseCreditAchat.getCaisse());
                if (idx > -1) {
                    currentPhaseCreditAchat.setCaisse(new YvsBaseCaisse(w.getCaisses().get(idx)));
                }
            }
        }
    }

    public void forceCancelCompte() {
        if (operation.equals("A")) {
            if (etapeCompte != null ? etapeCompte.getId() > 0 : false) {
                cancelValidEtapesAcompte(selectCompte, etapeCompte, false);
            } else {
                annulerAcompte(compte, selectCompte, suspendCompte);
            }
            etapeCompte = new YvsComptaPhaseAcompteAchat();
        } else {
            if (etapeCredit != null ? etapeCredit.getId() > 0 : false) {
                cancelValidEtapesCredit(selectReglement, etapeCredit, false);
            } else {
                annulerReglement(selectCredit, selectReglement, suspendReglementCredit);
            }
            etapeCredit = new YvsComptaPhaseReglementCreditFournisseur();
        }
    }

    public void cancelValidEtapesAcompte(YvsComptaPhaseAcompteAchat pp) {
        etapeCompte = pp;
        if (pp.getPhaseReg().getReglementOk()) {
            if (dao.isComptabilise(selectCompte.getId(), Constantes.SCR_ACOMPTE_ACHAT)) {
                openDialog("dlgConfirmAnnuleDoc");
                return;
            }
        }
        cancelValidEtapesAcompte(selectCompte, pp, false);
    }

    public void cancelValidEtapesCredit(YvsComptaPhaseReglementCreditFournisseur pp) {
        etapeCredit = pp;
        if (pp.getPhaseReg().getReglementOk()) {
            if (dao.isComptabilise(selectReglement.getId(), Constantes.SCR_ACOMPTE_ACHAT)) {
                openDialog("dlgConfirmAnnuleDoc");
                return;
            }
        }
        cancelValidEtapesCredit(selectReglement, pp, false);
    }

    public boolean cancelValidEtapesAcompte(YvsComptaAcompteFournisseur selectCompte, YvsComptaPhaseAcompteAchat pp, boolean retour) {
        //l'étape suivante ne doit pas être validé
        if (!asDroitValidePhase(pp.getPhaseReg())) {
            openNotAcces();
            return false;
        }
        int idx = selectCompte.getPhasesReglement().indexOf(pp);
        YvsComptaPhaseAcompteAchat pSvt = null;
        if (idx >= 0 && (idx + 1) < selectCompte.getPhasesReglement().size()) {
            pSvt = selectCompte.getPhasesReglement().get(idx + 1);
        } else if (idx == (selectCompte.getPhasesReglement().size() - 1) || idx == 0) {
            pSvt = selectCompte.getPhasesReglement().get(idx);
        }
        if (pSvt != null) {
            if (!pSvt.isEtapeActive() ? !pSvt.equals(pp) : false) {
                getErrorMessage("Vous ne pouvez annuler cette étape !");
                return false;
            }
            pSvt.setEtapeActive(false);
            idx = selectCompte.getPhasesReglement().indexOf(pSvt);
            if (idx >= 0) {
                selectCompte.getPhasesReglement().set(idx, pSvt);
            }
        }
        if (dao.isComptabilise(pp.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return false;
            }
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                if (!w.unComptabiliserPhaseAcompteAchat(pp, false)) {
                    getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                    return false;
                }
            }
        }
        pp.setAuthor(currentUser);
        pp.setDateUpdate(new Date());
        pp.setDateValider(null);
        pp.setEtapeActive(true);
        pp.setPhaseOk(false);
        pp.setStatut(Constantes.STATUT_DOC_ATTENTE);
        dao.update(pp);
        currentPhaseAcompteAchat = pp;
        currentPhaseAcompteAchat.setDateValider(new Date());

        if (pp.getPhaseReg().getReglementOk()) {
            pp.getPieceAchat().setStatut(Constantes.STATUT_DOC_ATTENTE);
            dao.update(pp.getPieceAchat());
            selectCompte.setStatut(Constantes.STATUT_DOC_ATTENTE);
        }

        YvsComptaAcompteFournisseur pc = pp.getPieceAchat();
        idx = pc.getPhasesReglement().indexOf(pp);
        if (idx >= 0) {
            pc.getPhasesReglement().set(idx, pp);
        }
        idx = selectCompte.getPhasesReglement().indexOf(pp);
        if (idx >= 0) {
            selectCompte.getPhasesReglement().set(idx, pp);

        }
        idx = acomptes.indexOf(selectCompte);
        if (idx
                > -1) {
            acomptes.set(idx, selectCompte);
            update("data_acompte_fournisseur");
        }
        idx = versements.indexOf(selectCompte);
        if (idx
                > -1) {
            versements.set(idx, selectCompte);
            update("data_historique_acompte_fournisseur");
        }

        return true;
    }

    public void cancelValidEtapesCredit(YvsComptaPhaseReglementCreditFournisseur pp, boolean retour) {
        cancelValidEtapesCredit(selectReglement, pp, retour);
    }

    public boolean cancelValidEtapesCredit(YvsComptaReglementCreditFournisseur selectReglement, YvsComptaPhaseReglementCreditFournisseur pp, boolean retour) {
        //l'étape suivante ne doit pas être validé
        if (!asDroitValidePhase(pp.getPhaseReg())) {
            openNotAcces();
            return false;
        }
        int idx = selectReglement.getPhasesReglement().indexOf(pp);
        YvsComptaPhaseReglementCreditFournisseur pSvt = null;
        if (idx >= 0 && (idx + 1) < selectReglement.getPhasesReglement().size()) {
            pSvt = selectReglement.getPhasesReglement().get(idx + 1);
        } else if (idx == (selectReglement.getPhasesReglement().size() - 1) || idx == 0) {
            pSvt = selectReglement.getPhasesReglement().get(idx);
        }
        if (pSvt != null) {
            if (!pSvt.isEtapeActive() ? !pSvt.equals(pp) : false) {
                getErrorMessage("Vous ne pouvez annuler cette étape !");
                return false;
            }
            pSvt.setEtapeActive(false);
            idx = selectReglement.getPhasesReglement().indexOf(pSvt);
            if (idx >= 0) {
                selectReglement.getPhasesReglement().set(idx, pSvt);
            }
        }
        if (dao.isComptabilise(pp.getId(), Constantes.SCR_PHASE_CREDIT_ACHAT)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return false;
            }
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                if (!w.unComptabiliserPhaseCaisseCreditAchat(pp, false)) {
                    getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                    return false;
                }
            }
        }
        pp.setDateValider(null);
        pp.setEtapeActive(false);
        pp.setPhaseOk(false);
        pp.setEtapeActive(true);
        dao.update(pp);
        currentPhaseCreditAchat = pp;
        currentPhaseCreditAchat.setDateValider(new Date());

        if (pp.getPhaseReg().getReglementOk()) {
            pp.getReglement().setStatut(Constantes.STATUT_DOC_ATTENTE);
            dao.update(pp.getReglement());
            selectReglement.setStatut(Constantes.STATUT_DOC_ATTENTE);

        }
        YvsComptaReglementCreditFournisseur pc = pp.getReglement();
        idx = pc.getPhasesReglement().indexOf(pp);
        if (idx
                >= 0) {
            pc.getPhasesReglement().set(idx, pp);
        }
        idx = selectReglement.getPhasesReglement().indexOf(pp);
        if (idx
                >= 0) {
            selectReglement.getPhasesReglement().set(idx, pp);
        }

        update(
                "header_form_suivi_pca");

        return true;
    }

    public boolean cancelAllEtapesAcompte() {
        return cancelAllEtapesAcompte(selectCompte, false);
    }

    public boolean cancelAllEtapesAcompte(boolean force) {
        return cancelAllEtapesAcompte(selectCompte, force);
    }

    public boolean cancelAllEtapesAcompte(YvsComptaAcompteFournisseur y, boolean force) {
        if (!autoriser("compta_cancel_piece_valide")) {
            openNotAcces();
            return false;
        }
        if (y != null ? y.getId() != null ? y.getId() > 0 : false : false) {
            //vérifie le droit:
            if (y.getStatut() != Constantes.STATUT_DOC_PAYER || force) {
                try {
                    int i = 0;
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                    );
                    for (YvsComptaPhaseAcompteAchat ph
                            : y.getPhasesReglement()) {
                        ph.setPhaseOk(false);
                        ph.setEtapeActive(i == 0);
                        ph.setAuthor(currentUser);
                        ph.setDateValider(null);
                        ph.setDateUpdate(new Date());
                        dao.update(ph);
                        if (w != null) {
                            w.unComptabiliserPhaseAcompteAchat(ph, false);
                        }
                        i++;
                    }
                    boolean succes = changeStatutAcompte(null, y, Constantes.STATUT_DOC_ATTENTE);
                    if (succes) {
                        if (w != null) {
                            w.unComptabiliserAcompteFournisseur(y);
                        }
                    }
                    if (succes && force) {
                        succes();
                    }

                    return true;
                } catch (Exception ex) {
                    getErrorMessage("Impossible d'annuler les phases!");
                    log.log(Level.SEVERE, null, ex);
                }
            } else {
                selectCompte = y;
                openDialog("dlgConfirmCancel");
            }
        }
        update("head_form_suivi_paa");
        return false;
    }

    public boolean cancelAllEtapesCredit() {
        return cancelAllEtapesCredit(selectReglement);
    }

    public boolean cancelAllEtapesCredit(YvsComptaReglementCreditFournisseur selectReglement) {
        if (!autoriser("compta_cancel_piece_valide")) {
            openNotAcces();
            return false;
        }
        if (selectReglement != null ? selectReglement.getId() != null ? selectReglement.getId() > 0 : false : false) {
            //vérifie le droit:
            if (selectReglement.getStatut() != Constantes.STATUT_DOC_PAYER) {
                try {
                    int i = 0;
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                    );
                    for (YvsComptaPhaseReglementCreditFournisseur ph
                            : selectReglement.getPhasesReglement()) {
                        ph.setPhaseOk(false);
                        ph.setDateValider(null);
                        ph.setEtapeActive(i == 0);
                        ph.setAuthor(currentUser);
                        dao.update(ph);
                        if (w != null) {
                            w.unComptabiliserPhaseCaisseCreditAchat(ph, false);
                        }
                        i++;
                    }

                    selectReglement.setStatut(Constantes.STATUT_DOC_ATTENTE);

                    dao.update(selectReglement);

                    succes();

                    return true;
                } catch (Exception ex) {
                    getErrorMessage("Impossible d'annuler les phases!");
                    log.log(Level.SEVERE, null, ex);
                }
            } else {
                openDialog("dlgConfirmCancel");
            }

        }
        update("header_form_suivi_pca");
        return false;
    }

    private void ordonnePhase(List<YvsComptaPhaseAcompteAchat> l, YvsComptaPhaseAcompteAchat p) {
        int idx = l.indexOf(p);
        if (idx >= 0) {
            if (!p.getPhaseOk()) {
                if ((idx - 1) >= 0) {
                    if (l.get(idx - 1).getPhaseOk()) {
                        l.get(idx).setEtapeActive(true);
                    } else {
                        p.setEtapeActive(false);
                    }
                } else {
                    l.get(idx).setEtapeActive(true);
                }
            } else {
                p.setEtapeActive(false);
            }
        }
    }

    private List<YvsComptaPhaseAcompteAchat> ordonnePhase(List<YvsComptaPhaseAcompteAchat> l) {
        Collections.sort(l, new YvsComptaPhaseAcompteAchat());
        int idx = 0;
        while (idx < l.size()) {
            ordonnePhase(l, l.get(idx));
            idx++;
        }
        return l;
    }

    public void onSelectDistantFacture(YvsComDocAchats y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureAchat s = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class
            );
            if (s != null) {
                s.onSelectObject(y);
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Factures Achat", "modGescom", "smenFactureAchat", true);
                }
            }
        }
    }

    public void onSelectDistantFactures(AcomptesAchatDivers a) {
        YvsComDocAchats y = new YvsComDocAchats();

        if (a.getNotifs() != null ? a.getNotifs().getId() > 0 : false) {
            y = a.getNotifs().getPieceAchat().getAchat();
            onSelectDistantFacture(y);
        } else {
            if (a.getNotif_divers() != null ? a.getNotif_divers().getId() > 0 : false) {
                YvsComptaCaisseDocDivers z = new YvsComptaCaisseDocDivers();
                z = a.getNotif_divers().getPieceDocDivers().getDocDivers();
                ManagedDocDivers m = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                if (m != null) {
                    m.onSelectObject(z);
                    Navigations n = (Navigations) giveManagedBean(Navigations.class);
                    if (n != null) {
                        n.naviguationView("Opérations Divers", "modCompta", "smenOperationDivers", true);
                    }
                }
            }
        }

    }

    public
            void onSelectDistantCheque(YvsComptaAcompteFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedReglementAchat s = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class
            );
            if (s
                    != null) {
                s.onSelectAcompteForCheque(y);
                ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                if (w != null) {
                    w.setSelectPiece((YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findByExterne", new String[]{"idExterne", "table"}, new Object[]{y.getId(), Constantes.SCR_ACOMPTE_ACHAT}));
                    Navigations n = (Navigations) giveManagedBean(Navigations.class);
                    if (n != null) {
                        n.naviguationView("Suivi des chèques", "modCompta", "smenSuiviRegVente", true);
                    }
                }
            }
        }
    }

    public
            void onSelectDistantCreditCheque(YvsComptaReglementCreditFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedReglementAchat s = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class
            );
            if (s
                    != null) {
                s.onSelectCreditForCheque(y);
                ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                if (w != null) {
                    w.setSelectPiece((YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findByExterne", new String[]{"idExterne", "table"}, new Object[]{y.getId(), Constantes.SCR_CREDIT_ACHAT}));
                    Navigations n = (Navigations) giveManagedBean(Navigations.class);
                    if (n != null) {
                        n.naviguationView("Suivi des chèques", "modCompta", "smenSuiviRegVente", true);
                    }
                }
            }
        }
    }

    public void verifyComptabilised(Boolean comptabilised) {
        loadAll(true, true);

        if (comptabilised != null) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (w
                    != null) {
                if (operation.equals("A")) {
                    List<YvsComptaAcompteFournisseur> list = new ArrayList<>();
                    list.addAll(acomptes);
                    for (YvsComptaAcompteFournisseur y : list) {
                        y.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_ACHAT));
                        if (comptabilised ? !y.isComptabilised() : y.isComptabilised()) {
                            acomptes.remove(y);
                        }
                    }
                } else {
                    List<YvsComptaCreditFournisseur> list = new ArrayList<>();
                    list.addAll(credits);
                    for (YvsComptaCreditFournisseur y : list) {
                        y.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_CREDIT_ACHAT));
                        if (comptabilised ? !y.isComptabilised() : y.isComptabilised()) {
                            credits.remove(y);
                        }
                    }
                }
            }
        }
        update("data_acompte_fournisseur");
        update("data_credit_fournisseur");
    }

    @Override
    public AcompteFournisseur recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void generatedPhaseAcompte(YvsComptaAcompteFournisseur selectAcompte) {
        if (selectAcompte != null && !selectAcompte.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
            if (selectAcompte.getPhasesReglement() != null) {
                try {
                    if (selectAcompte.getCaisse() != null ? selectAcompte.getCaisse().getId() < 1 : true) {
                        getErrorMessage("Vous devez precisez la caisse qui a été mouvementé");
                        return;
                    }
                    for (YvsComptaPhaseAcompteAchat ph : selectAcompte.getPhasesReglement()) {
                        ph.setAuthor(currentUser);
                        dao.delete(ph);
                    }
                    selectAcompte.getPhasesReglement().clear();
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{selectAcompte.getModel(), true});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhaseAcompteAchat pp;
                    if (selectAcompte.getPhasesReglement() == null) {
                        selectAcompte.setPhasesReglement(new ArrayList<YvsComptaPhaseAcompteAchat>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhaseAcompteAchat(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        if (selectAcompte.getCaisse() != null ? selectAcompte.getCaisse().getId() > 0 : false) {
                            pp.setCaisse(selectAcompte.getCaisse());
                        }
                        pp.setPieceAchat(selectAcompte);
                        pp = (YvsComptaPhaseAcompteAchat) dao.save1(pp);
                        selectAcompte.getPhasesReglement().add(pp);
                    }
                    update("table_list_piece_cheque_achat");
                    succes();
                } catch (Exception ex) {
                    getErrorMessage("Impossible de réaliser cette action !");
                }
            }
        } else {
            if (selectAcompte.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce est déjà payé !");
            }
            if (selectAcompte == null) {
                getErrorMessage("Aucune pièce de règlement n'a été selectionné !");
            }
        }
    }

    public void generatedPhaseCredit(YvsComptaReglementCreditFournisseur selectCredit) {
        if (selectCredit != null && !selectCredit.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
            if (selectCredit.getPhasesReglement() != null) {
                try {
                    if (selectCredit.getCaisse() != null ? selectCredit.getCaisse().getId() < 1 : true) {
                        getErrorMessage("Vous devez precisez la caisse qui a été mouvementé");
                        return;
                    }
                    for (YvsComptaPhaseReglementCreditFournisseur ph : selectCredit.getPhasesReglement()) {
                        ph.setAuthor(currentUser);
                        dao.delete(ph);
                    }
                    selectCredit.getPhasesReglement().clear();
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{selectCredit.getModel(), true});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhaseReglementCreditFournisseur pp;
                    if (selectCredit.getPhasesReglement() == null) {
                        selectCredit.setPhasesReglement(new ArrayList<YvsComptaPhaseReglementCreditFournisseur>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhaseReglementCreditFournisseur(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        if (selectCredit.getCaisse() != null ? selectCredit.getCaisse().getId() > 0 : false) {
                            pp.setCaisse(selectCredit.getCaisse());
                        }
                        pp.setReglement(selectCredit);
                        pp = (YvsComptaPhaseReglementCreditFournisseur) dao.save1(pp);
                        selectCredit.getPhasesReglement().add(pp);
                    }
                    update("table_list_piece_cheque_achat");
                    succes();
                } catch (Exception ex) {
                    getErrorMessage("Impossible de réaliser cette action !");
                }
            }
        } else {
            if (selectCredit.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce est déjà payé !");
            }
            if (selectCredit == null) {
                getErrorMessage("Aucune pièce de règlement n'a été selectionné !");
            }
        }
    }

    public void equilibreAll() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Integer> ids = decomposeSelection(tabIds);
                for (Integer index : ids) {
                    YvsComptaAcompteFournisseur bean = acomptes.get(index);
                    equilibre(bean, false);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void equilibre(YvsComptaAcompteFournisseur y) {
        equilibre(y, true);
    }

    public void equilibre(YvsComptaAcompteFournisseur y, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            y.setStatutNotif(equilibreAcompte(y.getId()));
            int idx = acomptes.indexOf(y);
            if (idx > -1) {
                acomptes.set(idx, y);
                update("data_acompte_fournisseur");
            }
        }
    }

    public char equilibreAcompte(long id) {
        char statut = Constantes.STATUT_DOC_ATTENTE;
        if (id > 0) {
            String query = "SELECT equilibre_acompte_fournisseur(?)";
            String result = (String) dao.loadObjectBySqlQuery(query, new Options[]{new Options(id, 1)});
            if (Util.asString(result)) {
                statut = result.charAt(0);
            }
        }
        return statut;
    }

    public
            void lettrer(YvsComptaAcompteFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (w
                    != null) {
                boolean comptabilise = w.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_ACHAT);
                if (!comptabilise) {
                    getInfoMessage("Cet acompte n'est pas comptabilisée");
                    return;
                }
                w.setContenusLettrer(w.lettrerAcompteFournisseur(y));
                if (w.getContenusLettrer() != null ? !w.getContenusLettrer().isEmpty() : false) {
                    openDialog("dlgLettrage");
                    update("data_contenu_journal");
                }
            }
        }
    }

    public boolean isIsFacture() {
        return isFacture;
    }

    public void setIsFacture(boolean isFacture) {
        this.isFacture = isFacture;
    }

    public void changeLabel(boolean t) {
        isFacture = t;
        update("form_reglement_acompte_fa");
    }

    public void comptabilisePieceAll() {
        try {
            for (AcomptesAchatDivers piece : compte.getAchatsEtDivers()) {
                comptabilisePiece(piece, false);
            }
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                w.lettrerAcompteFournisseur(selectCompte);
            }
            succes();
        } catch (Exception ex) {
            getException("comptabilisePieceAll", ex);
        }
    }

    public void comptabilisePiece(AcomptesAchatDivers piece, boolean msg) {
        try {
            if (piece != null ? piece.getId() > 0 : false) {
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    boolean succes = false;
                    if (selectNotif.getType().equals("ACHAT")) {
                        succes = w.comptabiliserCaisseAchat(piece.getNotifs().getPieceAchat(), msg, msg);
                    } else {
                        succes = w.comptabiliserCaisseDivers(piece.getNotif_divers().getPieceDocDivers(), msg, msg);
                    }
                    piece.setComptabilise(succes);
                    int index = compte.getAchatsEtDivers().indexOf(piece);
                    if (index > -1) {
                        compte.getAchatsEtDivers().set(index, piece);
                    }
                }
            }
        } catch (Exception ex) {
            getException("comptabilisePiece", ex);
        }
    }

    public void unComptabilisePiece(AcomptesAchatDivers piece, boolean msg) {
        try {
            if (piece != null ? piece.getId() > 0 : false) {
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    boolean succes = false;
                    if (selectNotif.getType().equals("ACHAT")) {
                        succes = w.unComptabiliserCaisseAchat(piece.getNotifs().getPieceAchat(), msg);
                    } else {
                        succes = w.unComptabiliserCaisseDivers(piece.getNotif_divers().getPieceDocDivers(), msg);
                    }
                    if (succes) {
                        piece.setComptabilise(false);
                        int index = compte.getAchatsEtDivers().indexOf(piece);
                        if (index > -1) {
                            compte.getAchatsEtDivers().set(index, piece);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getException("unComptabilisePiece", ex);
        }
    }

    public boolean isComptabilisePiece(AcomptesAchatDivers y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                boolean comptabilise = false;
                if (y.getType().equals("ACHAT")) {
                    comptabilise = w.isComptabilise(y.getNotifs().getPieceAchat().getId(), Constantes.SCR_CAISSE_ACHAT);
                } else {
                    comptabilise = w.isComptabilise(y.getNotif_divers().getPieceDocDivers().getId(), Constantes.SCR_CAISSE_DIVERS);
                }
                y.setComptabilise(comptabilise);
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseBeanCredit(CreditFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CREDIT_ACHAT));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseCredit(YvsComptaCreditFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CREDIT_ACHAT));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseCaisseCredit(YvsComptaReglementCreditFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CAISSE_CREDIT_ACHAT));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseBeanAcompte(AcompteFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_ACHAT));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseAcompte(YvsComptaAcompteFournisseur y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_ACHAT));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isComptabilisePhaseAcompte(YvsComptaPhaseAcompteAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isTrouberOD() {
        return trouberOD;
    }

    public void setTrouberOD(boolean trouberOD) {
        this.trouberOD = trouberOD;
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void changeNatureAcompte(YvsComptaAcompteFournisseur y, boolean all) {
        if (isComptabiliseAcompte(y)) {
            if (!all) {
                getErrorMessage("Le document d'acompte est déjà comptabilisé !");
            }
            return;
        } else {
            if (y.getNature().equals('A')) {
                y.setNature('D');
            } else {
                y.setNature('A');
            }
            y.setDateUpdate(new Date());
            y.setAuthor(currentUser);
            dao.update(y);
            if (!all) {
                succes();
            }
        }
    }

    public void changeNatureMany() {
        List<Integer> keys = decomposeSelection(tabIds);
        if (!keys.isEmpty()) {
            YvsComptaAcompteFournisseur y;
            for (Integer i : keys) {
                y = acomptes.get(i);
                changeNatureAcompte(y, true);
            }
            getInfoMessage("Opération terminé avec succès !");
        }
    }

}
