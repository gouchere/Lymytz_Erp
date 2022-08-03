/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.vente.DocVente;
import yvs.commercial.vente.ManagedFactureVente;
import yvs.commercial.vente.ManagedFactureVenteV2;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.caisse.Caisses;
import yvs.comptabilite.caisse.ManagedCaisses;
import yvs.comptabilite.caisse.ManagedReglementVente;
import yvs.comptabilite.tresorerie.DocCaissesDivers;
import yvs.comptabilite.tresorerie.ManagedDocDivers;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCreditClient;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaNotifReglementDocDivers;
import yvs.entity.compta.YvsComptaNotifReglementVente;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.compta.YvsComptaReglementCreditClient;
import yvs.entity.compta.client.YvsComptaPhaseReglementCreditClient;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.vente.YvsComptaPhaseAcompteVente;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.param.YvsAgences;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ManagedTypeCout;
import yvs.grh.bean.TypeCout;
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
public class ManagedOperationClient extends Managed<AcompteClient, YvsComptaAcompteClient> implements Serializable {

    private AcompteClient compte = new AcompteClient();
    private List<YvsComptaAcompteClient> acomptes;
    private YvsComptaAcompteClient selectCompte = new YvsComptaAcompteClient();
    private PieceTresorerie piece = new PieceTresorerie();
    private YvsComptaNotifReglementVente liaison = new YvsComptaNotifReglementVente();
    private YvsComptaNotifReglementDocDivers liaison_doc = new YvsComptaNotifReglementDocDivers();
    private YvsComptaPhaseAcompteVente etapeCompte;
    private YvsComptaPhaseAcompteVente currentPhaseAcompteVente = new YvsComptaPhaseAcompteVente();
    boolean suspendCompte;
    private YvsComptaPhaseReglementCreditClient currentPhaseCreditVente = new YvsComptaPhaseReglementCreditClient();
    private List<YvsComptaCaisseDocDivers> docDiverses = new ArrayList<>();

    private PaginatorResult<YvsComptaCreditClient> paginators = new PaginatorResult<>();
    private CreditClient credit = new CreditClient();
    private List<YvsComptaCreditClient> credits, redevances;
    private YvsComptaCreditClient selectCredit = new YvsComptaCreditClient();
    private ReglementCredit reglement = new ReglementCredit();
    private YvsComptaReglementCreditClient selectReglement = new YvsComptaReglementCreditClient();
    YvsComptaPhaseReglementCreditClient etapeCredit;
    boolean suspendReglementCredit;
    private boolean needConfirmationCredit, suspendCredit;
    public String codeClient_;
    private String operateuClt = " LIKE ", operateurVend = " LIKE ", operateurRef = " LIKE ";
    private String operation = "C";
    private boolean addDate;
    private Date dateDebut = new Date(), dateFin = new Date();
    private String codeClient, statutSearch, notifSearch, numSearch, modeSearch;
    private String natureSearch;
    private long caisseSearch, agenceSearch;
    private Boolean comptaSearch;
    public boolean date_up = false;
    private Date dateDebut_ = new Date(), dateFin_ = new Date();

    private long nbrComptaSearch;

    private String tabIds;
    private boolean isFacture = true;
    private boolean memory_choix_delete_acompte, memory_choix_delete_credit, memory_choix_delete_reglement;
    YvsComptaParametre currentParam;

    private boolean displayConfirm = true;
    private long agenceRegle = 0;
    private List<Object[]> soldes;

    private Long countEditable, countEncours, countValide, countAnnuler;
    private Double valueEditable, valueEncours, valueValide, valueAnnuler;
    private boolean trouver = false;

    private AcomptesVenteDivers selectNotif;
    private List<AcomptesVenteDivers> selectNotifs;

    private char natureChange;

    public ManagedOperationClient() {
        acomptes = new ArrayList();
        credits = new ArrayList();
        soldes = new ArrayList();
        redevances = new ArrayList();
        selectNotifs = new ArrayList();
    }

    public long getAgenceRegle() {
        return agenceRegle;
    }

    public void setAgenceRegle(long agenceRegle) {
        this.agenceRegle = agenceRegle;
    }

    public char getNatureChange() {
        return natureChange;
    }

    public void setNatureChange(char natureChange) {
        this.natureChange = natureChange;
    }

    public long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public List<AcomptesVenteDivers> getSelectNotifs() {
        return selectNotifs;
    }

    public void setSelectNotifs(List<AcomptesVenteDivers> selectNotifs) {
        this.selectNotifs = selectNotifs;
    }

    public AcomptesVenteDivers getSelectNotif() {
        return selectNotif;
    }

    public void setSelectNotif(AcomptesVenteDivers selectNotif) {
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

    public List<YvsComptaCaisseDocDivers> getDocDiverses() {
        return docDiverses;
    }

    public void setDocDiverses(List<YvsComptaCaisseDocDivers> docDiverses) {
        this.docDiverses = docDiverses;
    }

    public YvsComptaPhaseReglementCreditClient getEtapeCredit() {
        return etapeCredit;
    }

    public void setEtapeCredit(YvsComptaPhaseReglementCreditClient etapeCredit) {
        this.etapeCredit = etapeCredit;
    }

    public boolean isSuspendReglementCredit() {
        return suspendReglementCredit;
    }

    public void setSuspendReglementCredit(boolean suspendReglementCredit) {
        this.suspendReglementCredit = suspendReglementCredit;
    }

    public String getCodeClient_() {
        return codeClient_;
    }

    public void setCodeClient_(String codeClient_) {
        this.codeClient_ = codeClient_;
    }

    public String getOperateuClt() {
        return operateuClt;
    }

    public void setOperateuClt(String operateuClt) {
        this.operateuClt = operateuClt;
    }

    public String getOperateurVend() {
        return operateurVend;
    }

    public void setOperateurVend(String operateurVend) {
        this.operateurVend = operateurVend;
    }

    public String getOperateurRef() {
        return operateurRef;
    }

    public void setOperateurRef(String operateurRef) {
        this.operateurRef = operateurRef;
    }

    public YvsComptaParametre getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsComptaParametre currentParam) {
        this.currentParam = currentParam;
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

    public String getNotifSearch() {
        return notifSearch;
    }

    public boolean isNeedConfirmationCredit() {
        return needConfirmationCredit;
    }

    public void setNeedConfirmationCredit(boolean needConfirmationCredit) {
        this.needConfirmationCredit = needConfirmationCredit;
    }

    public boolean isSuspendCredit() {
        return suspendCredit;
    }

    public void setSuspendCredit(boolean suspendCredit) {
        this.suspendCredit = suspendCredit;
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

    public YvsComptaPhaseAcompteVente getEtapeCompte() {
        return etapeCompte;
    }

    public void setEtapeCompte(YvsComptaPhaseAcompteVente etapeCompte) {
        this.etapeCompte = etapeCompte;
    }

    public YvsComptaPhaseReglementCreditClient getCurrentPhaseCreditVente() {
        return currentPhaseCreditVente;
    }

    public void setCurrentPhaseCreditVente(YvsComptaPhaseReglementCreditClient currentPhaseCreditVente) {
        this.currentPhaseCreditVente = currentPhaseCreditVente;
    }

    public YvsComptaPhaseAcompteVente getCurrentPhaseAcompteVente() {
        return currentPhaseAcompteVente;
    }

    public void setCurrentPhaseAcompteVente(YvsComptaPhaseAcompteVente currentPhaseAcompteVente) {
        this.currentPhaseAcompteVente = currentPhaseAcompteVente;
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

    public YvsComptaReglementCreditClient getSelectReglement() {
        return selectReglement;
    }

    public void setSelectReglement(YvsComptaReglementCreditClient selectReglement) {
        this.selectReglement = selectReglement;
    }

    public YvsComptaNotifReglementVente getLiaison() {
        return liaison;
    }

    public void setLiaison(YvsComptaNotifReglementVente liaison) {
        this.liaison = liaison;
    }

    public List<Object[]> getSoldes() {
        return soldes;
    }

    public void setSoldes(List<Object[]> soldes) {
        this.soldes = soldes;
    }

    public List<YvsComptaCreditClient> getRedevances() {
        return redevances;
    }

    public void setRedevances(List<YvsComptaCreditClient> redevances) {
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

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
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

    public YvsComptaAcompteClient getSelectCompte() {
        return selectCompte;
    }

    public void setSelectCompte(YvsComptaAcompteClient selectCompte) {
        this.selectCompte = selectCompte;
    }

    public YvsComptaCreditClient getSelectCredit() {
        return selectCredit;
    }

    public void setSelectCredit(YvsComptaCreditClient selectCredit) {
        this.selectCredit = selectCredit;
    }

    public PaginatorResult<YvsComptaCreditClient> getPaginators() {
        return paginators;
    }

    public void setPaginators(PaginatorResult<YvsComptaCreditClient> paginators) {
        this.paginators = paginators;
    }

    public AcompteClient getCompte() {
        return compte;
    }

    public void setCompte(AcompteClient compte) {
        this.compte = compte;
    }

    public List<YvsComptaAcompteClient> getAcomptes() {
        return acomptes;
    }

    public void setAcomptes(List<YvsComptaAcompteClient> acomptes) {
        this.acomptes = acomptes;
    }

    public CreditClient getCredit() {
        return credit;
    }

    public void setCredit(CreditClient credit) {
        this.credit = credit;
    }

    public List<YvsComptaCreditClient> getCredits() {
        return credits;
    }

    public void setCredits(List<YvsComptaCreditClient> credits) {
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

        if (piece != null ? piece.getId() < 1 ? (piece.getDocVente() != null ? piece.getDocVente().getId() < 1 : true) : true : true) {
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
        paginator.addParam(new ParametreRequete("y.client.tiers.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        acomptes = paginator.executeDynamicQuery("y", "y", "YvsComptaAcompteClient y JOIN FETCH y.client JOIN FETCH y.model JOIN FETCH y.caisse", "y.dateAcompte DESC", avance, init, (int) imax, dao);
    }

    public void loadCredit(boolean avance, boolean init) {
        paginators.addParam(new ParametreRequete("y.client.tiers.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        credits = paginators.executeDynamicQuery("YvsComptaCreditClient", "y.dateCredit DESC", avance, init, (int) imax, dao);
    }

    public void loadAll(boolean avance, boolean init) {
        if (operation.equals("A")) {
            loadAcompte(avance, init);
            update("data_acompte_client");
        } else {
            loadCredit(avance, init);
            update("data_credit_client");
        }
    }

    public void historiqueCompte(YvsComClient y) {
        soldes.clear();

        champ = new String[]{"client", "statut"};
        val = new Object[]{y, Constantes.STATUT_DOC_PAYER};
        nameQueri = "YvsComptaAcompteClient.findSumByClient";
        Double depot = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);

        nameQueri = "YvsComptaNotifReglementVente.findSumByClient";
        Double avance = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        compte.setSolde((depot != null ? depot : 0) - (avance != null ? avance : 0));

        update("txt_solde_acompte_client");
        update("data_historique_acompte_client");
    }

    public void historiqueCredit(YvsComClient y) {
        champ = new String[]{"client"};
        val = new Object[]{y};
        nameQueri = "YvsComptaCreditClient.findByClient";
        redevances = dao.loadNameQueries(nameQueri, champ, val);

        nameQueri = "YvsComptaCreditClient.findSumByClient";
        Double depot = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);

        nameQueri = "YvsComptaReglementCreditClient.findSumByClient";
        Double avance = (Double) dao.loadObjectByNameQueries(nameQueri, champ, val);
        credit.setSolde((depot != null ? depot : 0) - (avance != null ? avance : 0));

        update("txt_solde_credit_client");
        update("data_historique_credit_client");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (operation.equals("A") ? paginator.getNbResult() : paginators.getNbResult())) {
            setOffset(0);
        }
        if (operation.equals("A")) {
            List<YvsComptaAcompteClient> re = paginator.parcoursDynamicData("YvsComptaAcompteClient", "y", "y.dateAcompte DESC", getOffset(), dao);
            if (!re.isEmpty()) {
                onSelectObject(re.get(0));
            }
        } else {
            List<YvsComptaCreditClient> re = paginators.parcoursDynamicData("YvsComptaCreditClient", "y", "y.dateCredit DESC", getOffset(), dao);
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
    public boolean controleFiche(AcompteClient bean) {
        if (bean.getClient() != null ? bean.getClient().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le client");
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
        // le mode de paiement d'une acompte   doit être espèce ou banque
        if (bean.getMode().getId() <= 0) {
            getErrorMessage("Vous devez préciser le mode de paiement !");
            return false;
        }
        if (bean.getStatut() == Constantes.STATUT_DOC_PAYER) {
            getErrorMessage("La pièce d'acompte est déjà payé !");
            return false;
        }
        if (!bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE) && !bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
            getErrorMessage("les acompte ne peuvent  être payé qu'en espèce ou par un mode banque");
            return false;
        }
        //Vérifier la cohérence des montant (Le reste doit être sup. à 0)
//        double mtn;
//        if (bean.getId() > 0 && selectCompte != null) {//en cas de modification d'une pièce(isoler le montantF à comparer)
//            mtn = (-selectCompte.getMontant() + bean.getMontant());
//        } else {
//            mtn = bean.getMontant();
//        }
        if ((selectCompte != null ? (selectCompte.getId() > 0 ? (!selectCompte.getDateAcompte().equals(bean.getDateAcompte())) : true) : true)
                || (bean.getNumRefrence() == null || bean.getNumRefrence().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_PT_AVANCE_VENTE, bean.getDateAcompte(), bean.getCaisse().getId());
            if ((ref != null) ? ref.trim().equals("") : true) {
                return false;
            }
            bean.setNumRefrence(ref);
        }
        return true;
    }

    public boolean controleFiche(CreditClient bean) {
        if (bean.getClient() != null ? bean.getClient().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le client");
            return false;
        }
        if (bean.getTypeCredit() != null ? bean.getTypeCredit().getId() < 1 : true) {
            getErrorMessage("Vous devez preciser le type de credit");
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
            String ref = genererReference(Constantes.TYPE_PT_CREDIT_VENTE, bean.getDateCredit());
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
    public void populateView(AcompteClient bean) {
        cloneObject(compte, bean);
        compte.setPhasesReglement(ordonnePhase(compte.getPhasesReglement()));
        if (bean.getPhasesReglement() != null ? !bean.getPhasesReglement().isEmpty() : false) {
            compte.setFirstEtape(bean.getPhasesReglement().get(0).getPhaseReg().getPhase());
        }
        piece.setMode(compte.getMode());
    }

    public void populateView(CreditClient bean) {
        cloneObject(credit, bean);
    }

    public void populateView(ReglementCredit bean) {
        cloneObject(reglement, bean);
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

            compte = new AcompteClient();
            compte.setDateAcompte(date);
            compte.setNature(nature);
            compte.setCaisse(caisse);
            compte.setMode(mode);

            selectCompte = new YvsComptaAcompteClient();
            soldes.clear();
            update("blog_acompte_client");
            update("blog_entete_acompte_client");
        } else {
            Date date = credit.getDateCredit();
            Journaux journal = new Journaux();
            cloneObject(journal, credit.getJournal());
            TypeCout typeCredit = new TypeCout();
            cloneObject(typeCredit, credit.getTypeCredit());

            credit = new CreditClient();
            credit.setDateCredit(date);
            credit.setJournal(journal);
            credit.setTypeCredit(typeCredit);

            selectCredit = new YvsComptaCreditClient();
            redevances.clear();
            resetReglement();
            update("blog_credit_client");
        }
    }

    public void resetReglement() {
        reglement = new ReglementCredit();
        if (credit != null) {
            reglement.setValeur(credit.getReste());
        }
        reglement.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        update("form_reglement_credit_client");
    }

    public void resetFicheReglement() {
        piece = new PieceTresorerie();
        if (compte != null) {
            piece.setMontant(compte.getReste());
        }
        piece.setMode(compte.getMode());
        liaison = new YvsComptaNotifReglementVente();
        update("form_reglement_acompte");
    }

    @Override
    public boolean saveNew() {
        if (operation.equals("A")) {
            YvsComptaAcompteClient y = saveAcompte(compte);
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                selectCompte = y;
                update("data_acompte_client");
                update("blog_entete_acompte_client");
                actionOpenOrResetAfter(this);
                piece.setMode(compte.getMode());
                update("txt_mode_acompte_reglement_vente");
                return true;
            }
        } else {
            YvsComptaCreditClient y = saveCredit(credit);
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                selectCredit = y;
                update("data_credit_client");
                update("blog_entete_credit_client");
                actionOpenOrResetAfter(this);
                return true;
            }
        }
        return false;
    }

    public YvsComptaAcompteClient saveAcompte(AcompteClient bean) {
        String action = bean.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean)) {
                selectCompte = UtilCompta.buildAcompteClient(bean, currentUser);
                selectCompte.setDateUpdate(new Date());
                if (bean.getId() > 0) {
                    dao.update(selectCompte);
                } else {
                    selectCompte.setDateSave(new Date());
                    selectCompte.setId(null);
                    selectCompte = (YvsComptaAcompteClient) dao.save1(selectCompte);
                    bean.setId(selectCompte.getId());
                    bean.setReste(bean.getMontant());
                }
                if (bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    //si on veux valider le paiement

                } else {
                    for (YvsComptaPhaseAcompteVente p : bean.getPhasesReglement()) {
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

    public YvsComptaCreditClient saveCredit(CreditClient bean) {
        String action = bean.getId() > 0 ? "Modification" : "Insertion";
        try {
            if (controleFiche(bean)) {
                YvsComptaCreditClient y = UtilCompta.buildCreditClient(bean, currentUser);
                if (bean.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsComptaCreditClient) dao.save1(y);
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
                selectReglement = UtilCompta.buildReglementCredit(bean, currentUser);
                if (bean.getId() > 0) {
                    dao.update(selectReglement);
                } else {
                    selectReglement.setId(null);
                    selectReglement = (YvsComptaReglementCreditClient) dao.save1(selectReglement);
                    reglement.setId(selectReglement.getId());
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
                update("data_reglement_credit");
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
                ManagedReglementVente m = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                if (m != null) {
                    piece.setCaisse(compte.getCaisse());
                    piece.setMode(compte.getMode());
                    if (!update) {
                        piece.setStatutPiece(compte.getStatut());
                    }
                    YvsComptaCaissePieceVente r = UtilCom.buildPieceVente(piece, currentUser);
                    r = m.createNewPieceCaisse(piece.getDocVente(), r, false);
                    if (r != null ? r.getId() > 0 : false) {
                        champ = new String[]{"piece", "acompte"};
                        val = new Object[]{r, selectCompte};
                        nameQueri = "YvsComptaNotifReglementVente.findOne";
                        YvsComptaNotifReglementVente y = (YvsComptaNotifReglementVente) dao.loadOneByNameQueries(nameQueri, champ, val);
                        if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                            y.setAuthor(currentUser);
                            dao.update(y);
                        } else {
                            y = new YvsComptaNotifReglementVente(r, selectCompte, currentUser);
                            y.setId(null);
                            y = (YvsComptaNotifReglementVente) dao.save1(y);
                        }
                        int idx = compte.getNotifs().indexOf(y);
                        if (idx > -1) {
                            compte.getNotifs().set(idx, y);
                            compte.getVenteDiverses().set(idx, UtilCompta.buildBeanAcomptesVenteDivers(y));
                        } else {
                            compte.getNotifs().add(0, y);
                            compte.getVenteDiverses().add(0, UtilCompta.buildBeanAcomptesVenteDivers(y));
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
                            if (compte.isComptabilise() || dao.isComptabilise(piece.getDocVente().getId(), Constantes.SCR_VENTE)) {
                                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                                if (w != null) {
                                    w.comptabiliserCaisseVente(r, false, false);
                                }
                            }
                        }
                        succes();
                    }

                }
            } else {
                ManagedDocDivers m = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                if (m != null) {
                    if (piece.getDocDivers().getStatutDoc().equals(Constantes.ETAT_VALIDE) && piece.getDocDivers().getStatutRegle() != Constantes.STATUT_DOC_PAYER) {
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
                            nameQueri = "YvsComptaNotifReglementDocDivers.findOne";
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
                            int idx = compte.getNotifs().indexOf(y);
                            if (idx > -1) {
                                compte.getNotifs_doc().set(idx, y);
                                compte.getVenteDiverses().set(idx, UtilCompta.buildBeanAcomptesVenteDivers(y));
                            } else {
                                compte.getNotifs_doc().add(0, y);
                                compte.getVenteDiverses().add(0, UtilCompta.buildBeanAcomptesVenteDivers(y));
                            }
                            idx = selectCompte.getNotifs().indexOf(y);
                            if (idx > -1) {
                                selectCompte.getNotifsDivers().set(idx, y);
                            } else {
                                selectCompte.getNotifsDivers().add(0, y);
                            }
                            idx = acomptes.indexOf(selectCompte);
                            if (idx > -1) {
                                acomptes.set(idx, selectCompte);
                            }
                            if (piece.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                                equilibre(selectCompte);
                                compte.setReste(compte.getReste() - piece.getMontant());
                                if (compte.isComptabilise() || dao.isComptabilise(piece.getDocDivers().getId(), Constantes.SCR_DIVERS)) {
                                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                                    if (w != null) {
                                        w.comptabiliserCaisseDivers(r, false, false);
                                    }
                                }
                            }
                            m.equilibreOne(UtilCompta.buildDocDivers(piece.getDocDivers()));
                            succes();
                        }

                    } else {
                        if (!piece.getDocDivers().getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                            getErrorMessage("Le document doit être validé !");
                        } else {
                            getErrorMessage("Ce document est déjà réglé !");
                        }
                    }
                }
            }
            update("data_reglement_acompte");
            resetFicheReglement();

        } catch (Exception ex) {
            getErrorMessage(update ? "Modification" : "Insertion" + " Impossible !");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public boolean buildAndSavePiece(double montant, boolean msg, boolean equilibre) {
        if (isFacture) {
            piece.setCaisse(compte.getCaisse());
            piece.setMode(compte.getMode());
            piece.setDatePaiement(compte.getDateAcompte());
            piece.setDatePiece(compte.getDateAcompte());
            piece.setDatePaiementPrevu(compte.getDateAcompte());
            piece.setStatutPiece(compte.getStatut());
            YvsComptaCaissePieceVente r = UtilCom.buildPieceVente(piece, currentUser);
            ManagedReglementVente service = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
            r = service.createNewPieceCaisse(piece.getDocVente(), r, false);
            if (r != null ? r.getId() > 0 : false) {
                champ = new String[]{"piece", "acompte"};
                val = new Object[]{r, selectCompte};
                nameQueri = "YvsComptaNotifReglementVente.findOne";
                YvsComptaNotifReglementVente y = (YvsComptaNotifReglementVente) dao.loadOneByNameQueries(nameQueri, champ, val);
                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                    y.setAuthor(currentUser);
                    dao.update(y);
                } else {
                    y = new YvsComptaNotifReglementVente(r, selectCompte, currentUser);
                    y.setId(null);
                    y = (YvsComptaNotifReglementVente) dao.save1(y);
                }
                int idx = compte.getNotifs().indexOf(y);
                if (idx > -1) {
                    compte.getNotifs().set(idx, y);
                    compte.getVenteDiverses().set(idx, UtilCompta.buildBeanAcomptesVenteDivers(y));
                } else {
                    compte.getNotifs().add(0, y);
                    compte.getVenteDiverses().add(0, UtilCompta.buildBeanAcomptesVenteDivers(y));
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
                    if (equilibre) {
                        equilibre(selectCompte);
                    }
                    compte.setReste(compte.getReste() - piece.getMontant());
                    if (dao.isComptabilise(piece.getDocVente().getId(), Constantes.SCR_VENTE)) {
                        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                        if (w != null) {
                            w.comptabiliserCaisseVente(r, false, false);
                        }
                    }
                }
                succes();
                update("data_reglement_acompte");
                return true;
            } else {
                return false;
            }
        } else {
            piece.setCaisse(compte.getCaisse());
            piece.setMode(compte.getMode());
            piece.setDatePaiement(compte.getDateAcompte());
            piece.setDatePiece(compte.getDateAcompte());
            piece.setDatePaiementPrevu(compte.getDateAcompte());

            YvsComptaCaissePieceDivers r = UtilCompta.buildPieceCaisse(piece, currentUser);
            ManagedDocDivers service = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class
            );

            r = service.createNewPieceCaisse(piece.getDocDivers(), r, true);
            if (r
                    != null ? r.getId()
                    > 0 : false) {
                champ = new String[]{"piece", "acompte"};
                val = new Object[]{r, selectCompte};
                nameQueri = "YvsComptaNotifReglementDocDivers.findOne";
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

                if (compte.getVenteDiverses() != null ? !compte.getVenteDiverses().isEmpty() : false) {
                    AcomptesVenteDivers a = UtilCompta.buildBeanAcomptesVenteDivers(y);
                    idx = compte.getVenteDiverses().indexOf(a);
                    if (idx > -1) {
                        compte.getVenteDiverses().set(idx, a);

                    } else {
                        compte.getVenteDiverses().add(0, a);
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

            succes();

            update(
                    "data_reglement_acompte");

            return true;
        }

    }

    public void openOrfindAndRegleFacture() {
        if (displayConfirm) {
            if (agenceRegle < 1) {
                agenceRegle = currentAgence.getId();
                update("main-notif_reg_vente");
            }
            openDialog("dlgConfirNotifRegVente");
        } else {
            findAndRegleFacture();
        }
    }

    private Double validePieceExist(Double montantReste) {
        if (montantReste > 0) {
            for (AcomptesVenteDivers a : compte.getVenteDiverses()) {
                setMontantTotalDoc(a.getNotifs().getPieceVente().getVente());
                double montantResteAPayer = a.getNotifs().getPieceVente().getVente().getMontantResteApayer();
                if (montantResteAPayer > 0) {
                    if (!a.getNotifs().getPieceVente().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                        a.getNotifs().getPieceVente().setStatutPiece(Constantes.STATUT_DOC_PAYER);
                        a.getNotifs().getPieceVente().setAuthor(currentUser);
                        a.getNotifs().getPieceVente().setDateUpdate(new Date());
                        a.setStatutPiece(a.getNotifs().getPieceVente().getStatutPiece().toString());
                        dao.update(a.getNotifs().getPieceVente());

                        if (dao.isComptabilise(a.getNotifs().getPieceVente().getVente().getId(), Constantes.SCR_VENTE)) {
                            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                            if (w
                                    != null) {
                                w.comptabiliserCaisseVente(a.getNotifs().getPieceVente(), false, false);
                            }
                        }
                    }
                    montantReste = (montantReste - a.getNotifs().getPieceVente().getMontant());
                }
            }
        }
        return montantReste;
    }

    public void findAndRegleFacture() {
        if (isFacture) {
            if (compte.getId() > 0 && compte.getClient().getId() > 0) {
                //Montant restant sur l'acompte
                double montantResteAcompte = selectCompte.getReste();
                double montantResteAPayer = 0;
                montantResteAcompte = validePieceExist(montantResteAcompte);
                if (montantResteAcompte > 0) {
                    //Récupère les factures non payé du client
                    List<YvsComDocVentes> factures;
                    if (agenceRegle < 1) {
                        factures = dao.loadNameQueries("YvsComDocVentes.findByClientNonRegle", new String[]{"client", "statutRegle"}, new Object[]{selectCompte.getClient(), Constantes.ETAT_REGLE});
                    } else {
                        factures = dao.loadNameQueries("YvsComDocVentes.findByClientNonRegleByAgence", new String[]{"client", "statutRegle", "agence"}, new Object[]{selectCompte.getClient(), Constantes.ETAT_REGLE, new YvsAgences(agenceRegle)});
                    }
                    double montant = 0;
                    DocVente doc;
                    if (!factures.isEmpty()) {
                        for (YvsComDocVentes d : factures) {
                            piece = new PieceTresorerie();
                            if (montantResteAcompte > 0) {
                                doc = UtilCom.buildBeanDocVente(d);
                                setMontantTotalDoc(doc);
                                montantResteAPayer = doc.getMontantResteApayer();
                                if (montantResteAPayer > 0) {
                                    cloneObject(piece.getDocVente(), doc);
                                    piece.setNumRefExterne(d.getNumDoc());
                                    if (montantResteAcompte > montantResteAPayer) {
                                        montant = montantResteAPayer;
                                    } else {
                                        montant = montantResteAcompte;
                                    }
                                    piece.setMontant(montant);
                                    if (buildAndSavePiece(montant, false, false)) {
                                        montantResteAcompte = montantResteAcompte - montant;
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                        succes();
                    } else {
                        getInfoMessage("Aucune facture en attente de paiement n'a été trouvé !");
                    }
                    update("data_reglement_acompte");
                }
            }
        } else {
            if (compte.getId() > 0 && compte.getClient().getId() > 0) {
                //Montant restant sur l'acompte
                double montantResteAcompte = selectCompte.getReste();
                double montantResteAPayer = 0;
                if (montantResteAcompte > 0) {
                    //Récupère les factures non payé du client
                    System.err.println("id tiers =" + selectCompte.getClient().getId());
                    List<YvsComptaCaisseDocDivers> docDivers;
                    if (agenceRegle < 1) {
                        docDivers = dao.loadNameQueries("YvsComptaCaisseDocDivers.findByClientNonRegle", new String[]{"idTiers", "tableTiers", "statutRegler"}, new Object[]{selectCompte.getClient().getId(), Constantes.BASE_TIERS_CLIENT, Constantes.ETAT_REGLE.charAt(0)});
                    } else {
                        docDivers = dao.loadNameQueries("YvsComptaCaisseDocDivers.findByClientNonRegleByAgence", new String[]{"idTiers", "tableTiers", "statutRegler", "agence"}, new Object[]{selectCompte.getClient().getId(), Constantes.BASE_TIERS_CLIENT, Constantes.ETAT_REGLE.charAt(0), new YvsAgences(agenceRegle)});
                    }
                    System.err.println("liste des documents =" + docDivers);

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
                                System.err.println("montant reste =" + montantResteAPayer);
                                if (montantResteAPayer > 0) {
                                    piece = new PieceTresorerie();
                                    cloneObject(piece.getDocDivers(), doc);
                                    piece.setNumRefExterne(d.getNumPiece());
                                    System.err.println("montant reste acompte=" + montantResteAcompte);
                                    if (montantResteAcompte > montantResteAPayer) {
                                        montant = montantResteAPayer;
                                    } else {
                                        montant = montantResteAcompte;
                                    }
                                    piece.setMontant(montant);
                                    buildAndSavePiece(montant, false, false);
                                }
                            }
                        }
                    }
                }
            }
        }
        equilibre(selectCompte);
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteBean(Object object) {
        if (operation.equals("A")) {
            selectCompte = (YvsComptaAcompteClient) object;
            deleteBeanCompte();
        } else {
            selectCredit = (YvsComptaCreditClient) object;
            deleteBeanCredit();
        }
    }

    public void deleteBeanCompte(YvsComptaAcompteClient y) {
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
                acomptes.remove(selectCompte);
                if (selectCompte.getId().equals(compte.getId())) {
                    resetFiche();
                }
                update("data_acompte_client");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible");
            getException("Error", ex);
        }
    }

    public void deleteBeanCredit(YvsComptaCreditClient y) {
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
                update("data_credit_client");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    public void deleteBeanReglement(YvsComptaReglementCreditClient y) {
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
                update("data_reglement_credit");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    public void deleteBeanPiece(YvsComptaNotifReglementVente y) {
        liaison = y;
    }

    public void deleteBeanPieces(AcomptesVenteDivers a) {
        selectNotif = a;
        if (a.getType().equals("VENTE")) {
            deleteBeanPiece(a.getNotifs());
        } else {
            liaison_doc = a.getNotif_divers();
        }

    }

    public void confirmDeletePieces(boolean so_piece) {
        try {
            for (AcomptesVenteDivers a : selectNotifs) {
                if (a.getType().equals("VENTE")) {
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

    private void confirmDeletePiece(YvsComptaNotifReglementVente y, boolean so_piece) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                if (so_piece && y.getPieceVente().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    getErrorMessage("Ce reglement est déjà payé");
                    return;
                }
                dao.delete(y);
                if (so_piece) {
                    dao.delete(y.getPieceVente());
                }

                AcomptesVenteDivers b = UtilCompta.buildBeanAcomptesVenteDivers(y);
                compte.getNotifs().remove(y);
                compte.getVenteDiverses().remove(b);
                selectCompte.getNotifs().remove(y);

                int idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                }
                if (y.getId().equals(reglement.getId())) {
                    resetFicheReglement();
                }
                ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                ManagedFactureVenteV2 wv = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                if (w != null && wv != null) {
                    if (!so_piece) {
                        w.getPieceVente().setNotifs(null);
                        idx = wv.getDocVente().getReglements().indexOf(w.getPieceVente());
                        if (idx > -1) {
                            wv.getDocVente().getReglements().set(idx, w.getPieceVente());
                        }
                    } else {
                        wv.getDocVente().getReglements().remove(y.getPieceVente());
                    }
                    update("table_regFV");
                }
                update("data_reglement_acompte");
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

                AcomptesVenteDivers b = UtilCompta.buildBeanAcomptesVenteDivers(y);
                compte.getNotifs_doc().remove(y);
                compte.getVenteDiverses().remove(b);
                selectCompte.getNotifsDivers().remove(y);

                int idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                }
                if (y.getId().equals(piece.getId())) {
                    resetFicheReglement();
                }
                update("data_reglement_acompte");
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
    public void onSelectDistant(YvsComptaAcompteClient y) {
        if (y != null ? y.getId() > 0 : false) {
            operation = "A";
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Acompte Client", "modCompta", "smenAcompteClient", true);
            }
        }
    }

    public void onSelectDistant(YvsComptaCreditClient y) {
        if (y != null ? y.getId() > 0 : false) {
            operation = "C";
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Crédit Client", "modCompta", "smenCreditClient", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsComptaAcompteClient y) {
        selectCompte = y;
        populateView(UtilCompta.buildBeanAcompteClient(y));
        historiqueCompte(y.getClient());
        resetReglement();
        update("blog_acompte_client");
        update("blog_entete_acompte_client");
    }

    public void onSelectObject(YvsComptaCreditClient y) {
        selectCredit = y;
        populateView(UtilCompta.buildBeanCreditClient(y));
        historiqueCredit(y.getClient());
        update("blog_credit_client");
    }

    public void onSelectObjectByClient(YvsComClient y) {
        if (y != null ? y.getId() > 0 : false) {
            codeClient = y.getCodeClient();
            addParamClient();
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            if (operation.equals("A")) {
                YvsComptaAcompteClient y = (YvsComptaAcompteClient) ev.getObject();
                onSelectObject(y);
                y.setNotifs(dao.loadNameQueries("YvsComptaNotifReglementVente.findByAcompte", new String[]{"acompte"}, new Object[]{y}));
            } else {
                YvsComptaCreditClient y = (YvsComptaCreditClient) ev.getObject();
                onSelectObject(y);
            }
            resetFicheReglement();
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void loadOnViewReglement(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectReglement = (YvsComptaReglementCreditClient) ev.getObject();
            populateView(UtilCompta.buildBeanReglementCredit(selectReglement));
            update("form_reglement_credit_client");
        }
    }

    public void unLoadOnViewReglement(UnselectEvent ev) {
        resetReglement();
    }

    public void loadOnViewPiece(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectNotif = (AcomptesVenteDivers) ev.getObject();
            piece = new PieceTresorerie();
            setIsFacture(selectNotif.getType().equals("VENTE"));
            if (isFacture) {
                piece.setDocVente(UtilCom.buildSimpleBeanDocVente(selectNotif.getVentes()));
                if (selectNotif.getVentes() != null) {
                    setMontantTotalDoc(piece.getDocVente());
                }
            } else {
                piece.setDocDivers(UtilCompta.buildSimpleBeanDocCaisse(selectNotif.getDivers()));
            }
            piece.setMontant(selectNotif.getMontant());
            piece.setDatePaiementPrevu(selectNotif.getDateReglement());
            update("form_reglement_acompte");
        }
    }

    public void unLoadOnViewPiece(UnselectEvent ev) {
        resetFicheReglement();
    }

    public void loadOnViewClient(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComClient y = (YvsComClient) ev.getObject();
            chooseClient(UtilCom.buildBeanClient(y));
        }
    }

    public void loadOnViewFacture(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
            chooseFacture(UtilCom.buildBeanDocVente(y));
        }
    }

    public void loadOnViewDocDivers(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCaisseDocDivers y = (YvsComptaCaisseDocDivers) ev.getObject();
            if (!y.getStatutDoc().equals(Constantes.ETAT_VALIDE) || y.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                if (!y.getStatutDoc().equals(Constantes.ETAT_VALIDE)) {
                    getErrorMessage("Le document doit être validé !");
                } else {
                    getErrorMessage("Ce document est déjà réglé !");
                }
            } else {
                chooseDocDivers(UtilCompta.buildBeanDocCaisse(y));
            }
        }
    }

    public
            void chooseTypeCredit() {
        ManagedTypeCout w = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class
        );
        if (w
                != null) {
            int idx = w.getTypes().indexOf(new YvsGrhTypeCout(credit.getTypeCredit().getId()));
            if (idx > -1) {
                YvsGrhTypeCout y = w.getTypes().get(idx);
                credit.setTypeCredit(UtilGrh.buildBeanTypeCout(y));
            }
        }
    }

    public
            void chooseJournalCredit() {
        ManagedJournaux w = (ManagedJournaux) giveManagedBean(ManagedJournaux.class
        );
        if (w
                != null) {
            int idx = w.getJournaux().indexOf(new YvsComptaJournaux(credit.getJournal().getId()));
            if (idx > -1) {
                YvsComptaJournaux y = w.getJournaux().get(idx);
                credit.setJournal(UtilCompta.buildBeanJournaux(y));
            }
        }
    }

    public
            void chooseModeCompte() {
        ManagedModeReglement w = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class
        );
        if (w
                != null) {
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

    public void chooseClient(Client d) {
        if (d != null ? d.getId() > 0 : false) {
            if (operation.equals("A")) {
                cloneObject(compte.getClient(), d);
                historiqueCompte(new YvsComClient(d.getId()));
                update("select_client_operation_acompte");
            } else {
                cloneObject(credit.getClient(), d);
                historiqueCredit(new YvsComClient(d.getId()));
                update("select_client_operation_credit");
            }
        }
    }

    public void chooseFacture(DocVente d) {
        if (d != null ? d.getId() > 0 : false) {
            setMontantTotalDoc(d);
            cloneObject(piece.getDocVente(), d);
            piece.setNumRefExterne(d.getNumDoc());
            double montant = 0;
            if (compte.getReste() > d.getMontantResteApayer()) {
                System.out.println("INT");
                montant = d.getMontantResteApayer();
            } else {
                montant = compte.getReste();
            }
            piece.setMontant(montant);
            System.err.println("montant :" + montant);
            update("select_facture_reglement_vente");
            update("txt_montant_reglement_vente");
            update("txt_mode_acompte_reglement_vente");
        }
    }

    public void chooseDocDivers(DocCaissesDivers d) {
        if (d != null ? d.getId() > 0 : false) {
            cloneObject(piece.getDocDivers(), d);
            piece.setNumRefExterne(d.getNumPiece());
            double montant = d.getMontant();
            YvsComptaCaisseDocDivers doc = new YvsComptaCaisseDocDivers(d.getId());
            ManagedDocDivers m = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class
            );
            m.setSelectDoc(doc);
            Double montant_reste = 0.0;
            montant_reste = (Double) dao.loadObjectByNameQueries("YvsComptaCaissePieceDivers.findSumMontantByDoc", new String[]{"docDivers"}, new Object[]{doc});
            montant_reste = montant_reste != null ? montant_reste : 0;

            if (d.getMontant()
                    <= montant_reste) {
                getErrorMessage("Ce document est entièrement réglé !");
                return;
            }
            montant_reste = d.getMontant() - montant_reste;

            if (compte.getReste()
                    > montant_reste) {

                montant = montant_reste;
            } else {
                montant = compte.getReste();
            }

            piece.setMontant(montant);

            update(
                    "select_facture_reglement_vente");
            update(
                    "txt_montant_reglement_vente");
            update(
                    "txt_mode_acompte_reglement_doc_divers");

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

    public boolean encaisserAcompte(YvsComptaAcompteClient y) {
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

    public boolean encaisserAcompte(AcompteClient acompte, YvsComptaAcompteClient y) {
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
                    if (currentParam == null) {
                        currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                    }
                    if (currentParam != null ? currentParam.getMajComptaAutoDivers() : false) {
                        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                        );
                        if (w
                                != null) {
                            w.comptabiliserAcompteClient(y, false, false);
                        }
                    }
                    if (y.getRepartirAutomatique()) {
                        if (acompte == null) {
                            selectCompte = y;
                            compte = UtilCompta.buildBeanAcompteClient(y);
                        }
                        openOrfindAndRegleFacture();
                    }
                }
                return succes;
            } else {
                //Valide et génère les phases du chèque.
                if (y.getPhasesReglement().isEmpty()) { // On se rassure qu'il ait pas déjà de phases
                    if (!autoriser("encais_piece_cheque")) {
                        openNotAcces();
                    }
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{y.getModel(), false});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhaseAcompteVente pp;
                    if (y.getPhasesReglement() == null) {
                        y.setPhasesReglement(new ArrayList<YvsComptaPhaseAcompteVente>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhaseAcompteVente(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        pp.setPieceVente(y);
                        pp.setCaisse(y.getCaisse());
                        pp.setDateSave(new Date());
                        pp.setDateUpdate(new Date());
                        pp = (YvsComptaPhaseAcompteVente) dao.save1(pp);
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
                            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                            );
                            if (w
                                    != null) {
                                w.comptabiliserAcompteClient(y, false, false);
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
        if (dao.isComptabilise(selectCompte.getId(), Constantes.SCR_ACOMPTE_VENTE)) {
            openDialog("dlgConfirmAnnuleDoc");
            etapeCompte = null;
            return false;
        }
        return annulerAcompte(compte, selectCompte, suspend);
    }

    public boolean annulerAcompte(YvsComptaAcompteClient y, boolean suspend) {
        return annulerAcompte(null, y, suspend);
    }

    public boolean annulerAcompte(AcompteClient acompte, YvsComptaAcompteClient y, boolean suspend) {
        try {
            if (!verifyCancelPieceCaisse(y.getDatePaiement())) {
                return false;
            }
            for (YvsComptaNotifReglementVente n : y.getNotifs()) {
                if (n.getPieceVente().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    getErrorMessage("Vous ne pouvez pas annuler ce paiement car l'acompte est rattaché à des pieces payées");
                    return false;
                }
            }
            if (dao.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_VENTE)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return false;

                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                );
                if (w
                        != null) {
                    if (!w.unComptabiliserAcompteClient(y)) {
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

    public boolean changeStatutAcompte(AcompteClient acompte, YvsComptaAcompteClient y, char statut) {
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
                    update("data_acompte_client");
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

    public boolean validerCredit(YvsComptaCreditClient y) {
        return validerCredit(null, y);
    }

    public boolean validerCredit(CreditClient credit, YvsComptaCreditClient y) {
        boolean succes = changeStatutCredit(credit, y, Constantes.STATUT_DOC_VALIDE);

        if (succes) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                w.comptabiliserCreditClient(y, false, false);
                update("data_reglement_credit");
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
        if (dao.isComptabilise(credit.getId(), Constantes.SCR_CREDIT_VENTE)) {
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
        if (dao.isComptabilise(credit.getId(), Constantes.SCR_CREDIT_VENTE)) {
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

    public boolean annulerCredit(YvsComptaCreditClient y, boolean suspend) {
        return annulerCredit(null, y, suspend);
    }

    public boolean annulerCredit(CreditClient credit, YvsComptaCreditClient y, boolean suspend) {
        if (!credit.canEditable()) {
            getErrorMessage("Vous ne pouvez pas modifier ce document. Il est rattaché à des reglements payés");
            return false;
        }
        if (dao.isComptabilise(y.getId(), Constantes.SCR_CREDIT_VENTE)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return false;

            }
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (w
                    != null) {
                if (!w.unComptabiliserCreditClient(y, false)) {
                    getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                    return false;
                }
            }
        }
        return changeStatutCredit(credit, y, (suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE));
    }

    public boolean changeStatutCredit(CreditClient credit, YvsComptaCreditClient y, char statut) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                y.setDateUpdate(new Date());
                y.setAuthor(currentUser);
                y.setDateCredit(credit.getDateCredit());
                y.setStatut(statut);
                //evité la mise à jour en cascade
                List<YvsComptaReglementCreditClient> list = new ArrayList<>();
                list.addAll(y.getReglements());
                if (y.getReglements() != null) {
                    y.getReglements().clear();
                }
                dao.update(y);
                if (credit != null) {
                    credit.setStatut(statut);
                    if (credit.getReglements() != null ? credit.getReglements().isEmpty() : true) {
                        credit.setReglements(list);
                    }
                }
                int idx = credits.indexOf(y);
                if (idx > -1) {
                    credits.set(idx, y);
                    update("data_credit_client");
                }
                idx = redevances.indexOf(y);
                if (idx > -1) {
                    redevances.set(idx, y);
                    update("data_historique_credit_client");
                }
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
        return false;
    }

    public boolean encaisserReglement(YvsComptaReglementCreditClient bean) {
        bean.setDatePaiement(bean.getDateReg());
        return encaisserReglement(selectCredit, bean);
    }

    public boolean encaisserReglement(YvsComptaCreditClient credit, YvsComptaReglementCreditClient y) {
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
            for (YvsComptaReglementCreditClient r : credit.getReglements()) {
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
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{y.getModel(), false});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhaseReglementCreditClient pp;
                    if (y.getPhasesReglement() == null) {
                        y.setPhasesReglement(new ArrayList<YvsComptaPhaseReglementCreditClient>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhaseReglementCreditClient(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        pp.setReglement(y);
                        pp.setCaisse(y.getCaisse());
                        pp = (YvsComptaPhaseReglementCreditClient) dao.save1(pp);
                        y.getPhasesReglement().add(pp);
                    }
                    int idx = credit.getReglements().indexOf(y);
                    if (idx > -1) {
                        credit.getReglements().set(idx, y);
                    }
                    idx = credits.indexOf(credit);
                    if (idx > -1) {
                        credits.set(idx, credit);
                    }
                    return true;
                } else {
                    getWarningMessage("Les phases de ce règlement ont déjà été générées !");
                }
            } else {
                if (!autoriser("p_caiss_payer") || !autoriser("encais_piece_espece")) {
                    openNotAcces();
                    return false;
                }
                y.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{y.getCaisse().getId()}));
                if (!controleAccesCaisse(y.getCaisse(), true)) {
                    return false;
                }
                boolean succes = changeStatutReglement(credit, y, Constantes.STATUT_DOC_PAYER);

                if (succes) {
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                    );
                    if (w
                            != null) {
                        w.comptabiliserCaisseCreditVente(y, false, false);
                    }
                }
                return succes;
            }
        } else {
            getErrorMessage("Le crédit n'est pas validé");
        }
        return false;
    }

    public boolean annulerReglement(YvsComptaReglementCreditClient y, boolean suspend) {
        suspendReglementCredit = suspend;
        if (dao.isComptabilise(y.getId(), Constantes.SCR_CAISSE_CREDIT_VENTE)) {
            openDialog("dlgConfirmAnnuleDoc");
            etapeCredit = null;
            selectReglement = y;
            return false;
        }
        return annulerReglement(selectCredit, y, suspend);
    }

    public boolean annulerReglement(YvsComptaCreditClient credit, YvsComptaReglementCreditClient y, boolean suspend) {
        try {
            if (!verifyCancelPieceCaisse(y.getDatePaiement())) {
                return false;
            }
            if (dao.isComptabilise(y.getId(), Constantes.SCR_CAISSE_CREDIT_VENTE)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return false;

                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                );
                if (w
                        != null) {
                    if (!w.unComptabiliserCaisseCreditVente(y, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return false;
                    }
                }
            }
            if (y.getPhasesReglement() != null ? !y.getPhasesReglement().isEmpty() : false) {
                if (cancelAllEtapesCredit()) {
                    return false;
                }
            }
            y.setDatePaiement(null);
            return changeStatutReglement(credit, y, (suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE));
        } catch (Exception ex) {

        }
        return false;
    }

    public boolean changeStatutReglement(YvsComptaCreditClient credit, YvsComptaReglementCreditClient y, char statut) {
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
                update("data_reglement_credit");
                return true;
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
        return false;
    }

    public void encaisserPiece(YvsComptaNotifReglementVente y) {
        if (selectCompte.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
            double montant = y.getPieceVente().getMontant();
            for (YvsComptaNotifReglementVente n : compte.getNotifs()) {
                if (n.getPieceVente().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    montant += n.getPieceVente().getMontant();
                }
            }
            if (montant > compte.getMontant()) {
                getErrorMessage("Vous ne pouvez pas valider ce montant.. car la somme des pièces excedera le montant de l'acompte");
                return;
            }
            y.getPieceVente().setDatePaiement(y.getPieceVente().getDatePaimentPrevu());
            y.getPieceVente().setValideBy(currentUser.getUsers());
            y.getPieceVente().setDateValide(new Date());
            y.getPieceVente().setMouvement(Constantes.MOUV_CAISS_ENTREE.charAt(0));
            changeStatutPiece(y, Constantes.STATUT_DOC_PAYER);
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (y.getPieceVente()
                    .getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                if (dao.isComptabilise(y.getPieceVente().getVente().getId(), Constantes.SCR_VENTE)) {
                    if (w != null) {
                        w.comptabiliserCaisseVente(y.getPieceVente(), false, false);
                    }
                }
            }
        } else {
            getErrorMessage("Cette acompte n'est pas encore encaissé");
        }
    }

    public void encaisserPieces(AcomptesVenteDivers a) {
        if (a.getType().equals("VENTE")) {
            YvsComptaNotifReglementVente y = new YvsComptaNotifReglementVente();
            y = a.getNotifs();
            encaisserPiece(y);
        } else {
            if (a.getType().equals("OD_V")) {
                YvsComptaNotifReglementDocDivers z = a.getNotif_divers();
                if (selectCompte.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                    double montant = z.getPieceDocDivers().getMontant();
                    for (YvsComptaNotifReglementVente n : compte.getNotifs()) {
                        if (n.getPieceVente().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                            montant += n.getPieceVente().getMontant();
                        }
                    }
                    if (montant > compte.getMontant()) {
                        getErrorMessage("Vous ne pouvez pas valider ce montant.. car la somme des pièces excedera le montant de l'acompte");
                        return;
                    }
                    z.getPieceDocDivers().setDatePaimentPrevu(z.getPieceDocDivers().getDatePaimentPrevu());
                    z.getPieceDocDivers().setValiderBy(currentUser.getUsers());
                    z.getPieceDocDivers().setDateValider(new Date());
                    z.getPieceDocDivers().setMouvement(Constantes.MOUV_CAISS_ENTREE);
                    changeStatutPiece(z, Constantes.STATUT_DOC_PAYER);
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                    );
                    ManagedDocDivers d = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);

                    if (z.getPieceDocDivers()
                            .getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                        if (dao.isComptabilise(z.getPieceDocDivers().getDocDivers().getId(), Constantes.SCR_VENTE)) {
                            if (w != null) {
                                w.comptabiliserCaisseDivers(z.getPieceDocDivers(), false, false);
                            }
                        }
                    }
                    if (d
                            != null) {
                        d.equilibreOne(z.getPieceDocDivers().getDocDivers());
                    }

                    AcomptesVenteDivers ad = UtilCompta.buildBeanAcomptesVenteDivers(z);
                    int index = compte.getVenteDiverses().indexOf(ad);
                    if (index
                            > -1) {
                        compte.getVenteDiverses().set(index, ad);
                    }

                    update(
                            "data_reglement_acompte");
                } else {
                    getErrorMessage("Cette acompte n'est pas encore encaissé");
                }
            }
        }

    }
    
    public void encaisserPiecesAll() {
        for(AcomptesVenteDivers a : selectNotifs){
            encaisserPieces(a);
        }      
    }

    public void annulerPiece(YvsComptaNotifReglementVente y, boolean suspend) {
        changeStatutPiece(y, suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE);
    }

    public void annulerPieces(AcomptesVenteDivers a, boolean suspend) {
        selectNotif = a;
        if (a.getType().equals("VENTE")) {
            YvsComptaNotifReglementVente y = a.getNotifs();
            annulerPiece(y, suspend);
        } else {
            if (a.getType().equals("OD_V")) {
                YvsComptaNotifReglementDocDivers z = a.getNotif_divers();
                changeStatutPiece(z, suspend ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE);
            }
        }

    }
    
    public void annulerPiecesAll(boolean suspend) {
        for(AcomptesVenteDivers a : selectNotifs){
            annulerPieces(a, suspend);
        }      
    }

    public void changeStatutPiece(YvsComptaNotifReglementVente y, char statut) {
        try {
            if (y != null ? (y.getId() > 0 ? (y.getPieceVente() != null ? y.getPieceVente().getId() > 0 : false) : false) : false) {
                y.getPieceVente().setStatutPiece(statut);
                dao.update(y.getPieceVente());
                int idx = selectCompte.getNotifs().indexOf(y);
                if (idx > -1) {
                    selectCompte.getNotifs().set(idx, y);
                }
                idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                }
                //modifie la vue
                for (AcomptesVenteDivers a : compte.getVenteDiverses()) {
                    if (a.getNotifs().equals(y)) {
                        a.setNotifs(y);
                        a.setStatutPiece(y.getPieceVente().getStatutPiece().toString());
                        break;

                    }
                }
                ManagedFactureVenteV2 wv = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                if (wv != null ? wv.getDocVente() != null : false) {
                    idx = wv.getDocVente().getReglements().indexOf(y.getPieceVente());
                    if (idx > -1) {
                        wv.getDocVente().getReglements().set(idx, y.getPieceVente());
                        update("table_regFV");
                    }
                }

                compte.setReste(selectCompte.getReste());
                compte.setResteUnBlind(selectCompte.getResteUnBind());
                Map<String, String> statuts = dao.getEquilibreVente(y.getPieceVente().getVente().getId());
                if (statuts != null) {
                    y.getPieceVente().getVente().setStatutLivre(statuts.get("statut_livre"));
                    y.getPieceVente().getVente().setStatutRegle(statuts.get("statut_regle"));
                }
                equilibre(selectCompte);
                update("data_reglement_acompte");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
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
                ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class
                );
                if (w
                        != null ? w.getSelectDoc()
                        != null : false) {
                    idx = w.getSelectDoc().getReglements().indexOf(y.getPieceDocDivers());
                    if (idx > -1) {
                        w.getSelectDoc().getReglements().set(idx, y.getPieceDocDivers());
                        update("table_regFV");
                    }
                }

                compte.setReste(selectCompte.getReste());
                compte.setResteUnBlind(selectCompte.getResteUnBind());
                w.equilibreOne(y.getPieceDocDivers().getDocDivers());
                equilibre(selectCompte);
                AcomptesVenteDivers a = UtilCompta.buildBeanAcomptesVenteDivers(y);
                int index = compte.getVenteDiverses().indexOf(a);
                if (index
                        > -1) {
                    compte.getVenteDiverses().set(index, a);
                }

                update(
                        "data_reglement_acompte");
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible");
            getException("Error", ex);
        }
    }

    public void searchClient() {
        String num;
        if (operation.equals("A")) {
            num = compte.getClient().getCodeClient();
            compte.getClient().setId(0);
            compte.getClient().setError(true);
            compte.getClient().setTiers(new Tiers());
        } else {
            num = credit.getClient().getCodeClient();
            credit.getClient().setId(0);
            credit.getClient().setError(true);
            credit.getClient().setTiers(new Tiers());

        }
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class
        );
        if (m
                != null) {
            if (num != null ? !num.isEmpty() : false) {
                Client y = m.searchClient(num, true);
                if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                    if (m.getClients().size() > 1) {
                        update("data_client_operation_acompte");
                    } else {
                        chooseClient(y);
                    }
                    if (operation.equals("A")) {
                        compte.getClient().setError(false);
                    } else {
                        credit.getClient().setError(false);
                    }
                }
            }
        }
    }

    public void searchFacture() {
        if (isFacture) {
            String num = piece.getDocVente().getNumDoc();
            piece.getDocVente().setId(0);
            piece.getDocVente().setError(true);
            ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            if (m != null) {
                DocVente y = m.searchFacture(num, compte.getClient(), true);
                if (m.getDocuments() != null ? !m.getDocuments().isEmpty() : false) {
                    if (m.getDocuments().size() > 1) {
                        update("data_facture_reglement_vente");
                    } else {
                        chooseFacture(y);
                    }
                    piece.getDocVente().setError(false);
                }
            }
        } else {
            String num = piece.getDocDivers().getNumPiece();
            piece.getDocDivers().setId(0);
            ManagedDocDivers d = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
            if (d != null) {
                d.clearParams();
                d.setCodeTiers(compte.getClient().getTiers().getCodeTiers());
                d.searchByNumAndType(num, "R");
                DocCaissesDivers y = new DocCaissesDivers();
                if (d.getDocuments() != null ? !d.getDocuments().isEmpty() : false) {
                    if (d.getDocuments().size() > 1) {
                        update("data_facture_reglement_vente");
                        openDialog("dlgListDoc");
                    } else {
                        y = UtilCompta.buildBeanDocCaisse(d.getDocuments().get(0));
                        chooseDocDivers(y);
                    }

                } else {
                    trouver = false;
                    update("data_facture_reglement_vente");
                }
            }
        }

    }

    public
            void initClients() {
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class
        );
        if (m
                != null) {
            m.initClients(compte.getClient());
        }

        update(
                "data_client_operation_acompte");
    }

    public
            void initFacture() {
        if (isFacture) {
            ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class
            );
            if (m
                    != null) {
                m.initFacture(piece.getDocVente(), compte.getClient());
            }

            update(
                    "data_facture_reglement_vente");
        } else {
            ManagedDocDivers d = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class
            );
            if (d
                    != null) {
                d.clearParams();
                d.setCodeTiers(compte.getClient().getTiers().getCodeTiers());
                d.addParamTiers("R");
            }

            update(
                    "data_facture_reglement_doc_divers");
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
            if (natureSearch.equals("A")) {
                p = new ParametreRequete(null, "nature", natureSearch, "=", "AND");
                p.getOtherExpression().add(new ParametreRequete("y.nature", "nature", natureSearch.charAt(0), "=", "OR"));
                p.getOtherExpression().add(new ParametreRequete("y.nature", "nature", "IS NULL", "IS NULL", "OR"));
            } else {
                p = new ParametreRequete("y.nature", "nature", natureSearch.charAt(0), "=", "AND");
            }
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

    public void addParamClient() {
        ParametreRequete p = new ParametreRequete("y.client", "client", null);
        if (codeClient != null ? codeClient.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "client", "%" + codeClient + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.codeClient)", "client", codeClient.toUpperCase().trim() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.nom)", "client", codeClient.toUpperCase().trim() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.client.prenom)", "client", codeClient.toUpperCase().trim() + "%", "LIKE", "OR"));
        }
        if (operation.equals("A")) {
            paginator.addParam(p);
        } else {
            paginators.addParam(p);
        }
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

    public void addParamClientById(long id) {
        ParametreRequete p = new ParametreRequete("y.client.id", "client", null, "=", "AND");
        if (id > 0) {
            p.setObjet(id);
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

    public void addParamReference() {
        if (operation.equals("A")) {
            ParametreRequete p = new ParametreRequete("y.numRefrence", "reference", null);
            if (numSearch != null ? numSearch.trim().length() > 0 : false) {
                p = new ParametreRequete(null, "reference", "%" + numSearch.trim() + "%", "LIKE", "AND");
                p.getOtherExpression().add(new ParametreRequete("y.numRefrence", "reference", "%" + numSearch.trim() + "%", "LIKE", "OR"));
                p.getOtherExpression().add(new ParametreRequete("y.referenceExterne", "reference", "%" + numSearch.trim() + "%", "LIKE", "OR"));
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

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query;
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (operation.equals("A")) {
                query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_acompte_client c RIGHT JOIN yvs_compta_acompte_client y ON c.acompte = y.id "
                        + "INNER JOIN yvs_base_caisse e ON y.caisse = e.id INNER JOIN yvs_compta_journaux h ON e.journal = h.id "
                        + "INNER JOIN yvs_agences a ON h.agence = a.id WHERE y.statut = 'P' AND a.societe = ? "
                        + "AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
                if (addDate) {
                    query += " AND y.date_acompte BETWEEN ? AND ?";
                    param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebut, 2), new Options(dateFin, 3)};
                }
            } else {
                query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_credit_client c RIGHT JOIN yvs_compta_credit_client y ON c.credit = y.id "
                        + "INNER JOIN yvs_com_client h ON y.client = h.id "
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

    //charge les pièces encaisser et non encore lié à une facture
    public void addParamNotBind(String client) {
        statutSearch = Constantes.ETAT_REGLE;
        paginator.addParam(new ParametreRequete("y.statut", "statut", Constantes.STATUT_DOC_PAYER, "=", "AND"));
        codeClient = client;
        addParamClient();
        update("table_acompte_clts");
        openDialog("dlgSaveAvance");
    }

    public YvsComptaNotifReglementVente confirmBind(YvsComptaAcompteClient y, YvsComptaCaissePieceVente c) {
        if ((y != null ? y.getId() > 0 : false) && (c != null ? c.getId() > 0 : false)) {
            champ = new String[]{"piece", "acompte"};
            val = new Object[]{c, y};
            nameQueri = "YvsComptaNotifReglementVente.findOne";
            YvsComptaNotifReglementVente e = (YvsComptaNotifReglementVente) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (e != null ? e.getId() < 1 : true) {
                e = new YvsComptaNotifReglementVente();
                e.setAcompte(y);
                e.setAuthor(currentUser);
                e.setDateSave(new Date());
                e.setDateUpdate(new Date());
                e.setPieceVente(c);
            }
            if (e != null) {
                if (e.getId() > 0) {
                    e.setAuthor(currentUser);
                    e.setDateUpdate(new Date());
                    dao.update(e);
                } else {
                    e.setId(null);
                    e = (YvsComptaNotifReglementVente) dao.save1(e);
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

    private boolean controleValidation(YvsComptaPhaseAcompteVente pp) {
        if ((pp.getPieceVente().getCaisse() != null) ? pp.getPieceVente().getCaisse().getId() <= 0 : true) {
            getErrorMessage("Aucune banque n'a été trouvé !");
            return false;
        }
        return true;
    }

    private boolean controleValidation(YvsComptaPhaseReglementCreditClient pp) {
        if ((pp.getReglement().getCaisse() != null) ? pp.getReglement().getCaisse().getId() <= 0 : true) {
            getErrorMessage("Aucune banque n'a été trouvé !");
            return false;
        }
        return true;
    }

    public void comptabiliserPhaseAcompteVente(YvsComptaPhaseAcompteVente pp) {
        etapeCompte = pp;

        if (compte.getPhasesReglement() != null ? !compte.getPhasesReglement().isEmpty() : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (w
                    != null) {
                int idx = compte.getPhasesReglement().indexOf(pp);
                if (idx > -1) {
                    if (idx == 0) {
                        w.comptabiliserPhaseAcompteVente(pp, true, true);
                    } else {
                        YvsComptaPhaseAcompteVente prec = compte.getPhasesReglement().get(idx - 1);
                        if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE)) {
                            w.comptabiliserPhaseAcompteVente(pp, true, true);
                        } else {
                            openDialog("dlgComptabilisePhaseByForce");
                        }
                    }
                }
            }
        }
    }

    public void validEtapesAcompte(YvsComptaPhaseAcompteVente pp) {
        pp.setDateValider(new Date());
        validEtapesAcompte(selectCompte, pp);
    }

    public boolean validEtapesAcompte(YvsComptaAcompteClient selectCompte, YvsComptaPhaseAcompteVente pp) {
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

                if (pp.getPhaseReg().getReglementOk()) {
                    pp.getPieceVente().setStatut(Constantes.STATUT_DOC_PAYER);
                    pp.getPieceVente().setDatePaiement(pp.getDateValider());
                } else {
                    if (pp.getPieceVente().getStatut() != Constantes.STATUT_DOC_PAYER) {
                        pp.getPieceVente().setStatut(Constantes.STATUT_DOC_ENCOUR);
                        pp.getPieceVente().setDatePaiement(null);
                    }
                }
                dao.update(pp.getPieceVente());
                selectCompte.setStatut(pp.getPieceVente().getStatut());
                dao.update(pp);
                pp.setEtapeActive(false);
                int idx = selectCompte.getPhasesReglement().indexOf(pp);
                if (idx >= 0 && (idx + 1) < selectCompte.getPhasesReglement().size()) {
                    selectCompte.getPhasesReglement().get(idx + 1).setEtapeActive(true);
                    currentPhaseAcompteVente = selectCompte.getPhasesReglement().get(idx + 1);
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
                        w.comptabiliserPhaseAcompteVente(pp, false, false);
                    }
                }
                idx = acomptes.indexOf(selectCompte);
                if (idx > -1) {
                    acomptes.set(idx, selectCompte);
                    update("data_acompte_client");
                }
                return true;
            }
        } else {
            getWarningMessage("Phase déjà valide! ");
        }
        update("head_form_suivi_pav");
        return false;
    }

    public boolean validEtapesCredit(YvsComptaPhaseReglementCreditClient pp) {
        return validEtapesCredit(selectReglement, pp);
    }

    public boolean validEtapesCredit(YvsComptaReglementCreditClient selectReglement, YvsComptaPhaseReglementCreditClient pp) {
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
                    currentPhaseCreditVente = selectReglement.getPhasesReglement().get(idx + 1);
                } else if (idx == (selectReglement.getPhasesReglement().size() - 1)) {
                    selectReglement.getPhasesReglement().get(idx).setEtapeActive(true);

                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                );
                if (w
                        != null) {
                    w.unComptabiliserPhaseCaisseCreditVente(pp, false);
                }

                return true;
            }
        } else {
            getWarningMessage("Phase déjà valide! ");
        }
        update("head_form_suivi_pcv");
        return false;
    }

    public
            void chooseCaissePhaseAcompte() {
        if (currentPhaseAcompteVente.getCaisse() != null ? currentPhaseAcompteVente.getCaisse().getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class
            );
            if (w
                    != null) {
                int idx = w.getCaisses().indexOf(currentPhaseAcompteVente.getCaisse());
                if (idx > -1) {
                    currentPhaseAcompteVente.setCaisse(new YvsBaseCaisse(w.getCaisses().get(idx)));
                }
            }
        }
    }

    public
            void chooseCaissePhaseCredit() {
        if (currentPhaseCreditVente.getCaisse() != null ? currentPhaseCreditVente.getCaisse().getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class
            );
            if (w
                    != null) {
                int idx = w.getCaisses().indexOf(currentPhaseCreditVente.getCaisse());
                if (idx > -1) {
                    currentPhaseCreditVente.setCaisse(new YvsBaseCaisse(w.getCaisses().get(idx)));
                    System.err.println("currentPhaseCreditVente.getCaisse() " + currentPhaseCreditVente.getCaisse());
                }
            }
        }
    }

    public void forceCancelCompte() {
        if (operation.equals("A")) {
            if (etapeCompte != null ? etapeCompte.getId() > 0 : false) {
                cancelValidEtapesAcompte(selectCompte, etapeCompte);
            } else {
                annulerAcompte(compte, selectCompte, suspendCompte);
            }
            etapeCompte = new YvsComptaPhaseAcompteVente();
        } else {
            if (etapeCredit != null ? etapeCredit.getId() > 0 : false) {
                cancelValidEtapesCredit(selectReglement, etapeCredit, false);
            } else {
                annulerReglement(selectCredit, selectReglement, suspendReglementCredit);
            }
            etapeCredit = new YvsComptaPhaseReglementCreditClient();
        }
    }

    public void cancelValidEtapesAcompte(YvsComptaPhaseAcompteVente pp) {
        etapeCompte = pp;
        if (pp.getPhaseReg().getReglementOk()) {
            if (dao.isComptabilise(selectCompte.getId(), Constantes.SCR_ACOMPTE_VENTE)) {
                openDialog("dlgConfirmAnnuleDoc");
                return;
            }
        }
        cancelValidEtapesAcompte(selectCompte, pp);
    }

    public void cancelValidEtapesCredit(YvsComptaPhaseReglementCreditClient pp) {
        etapeCredit = pp;
        if (pp.getPhaseReg().getReglementOk()) {
            if (dao.isComptabilise(selectReglement.getId(), Constantes.SCR_ACOMPTE_ACHAT)) {
                openDialog("dlgConfirmAnnuleDoc");
                return;
            }
        }
        cancelValidEtapesCredit(selectReglement, pp, false);
    }

    public boolean cancelValidEtapesAcompte(YvsComptaAcompteClient selectCompte, YvsComptaPhaseAcompteVente pp) {
        //l'étape suivante ne doit pas être validé
        if (!asDroitValidePhase(pp.getPhaseReg())) {
            openNotAcces();
            return false;
        }
        int idx = selectCompte.getPhasesReglement().indexOf(pp);
        YvsComptaPhaseAcompteVente pSvt = null;
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
        if (dao.isComptabilise(selectCompte.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return false;

            }
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (w
                    != null) {
                if (!w.unComptabiliserPhaseAcompteVente(pp, true)) {
                    getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                    return false;
                }
            }
        }
        pp.setAuthor(currentUser);
        pp.setDateUpdate(new Date());
        pp.setStatut(Constantes.STATUT_DOC_ATTENTE);
        pp.setDateValider(null);
        pp.setEtapeActive(true);
        pp.setPhaseOk(false);
        dao.update(pp);
        currentPhaseAcompteVente = pp;
        currentPhaseAcompteVente.setDateValider(new Date());
        if (pp.getPhaseReg().getReglementOk()) {
            pp.getPieceVente().setStatut(Constantes.STATUT_DOC_ATTENTE);
            dao.update(pp.getPieceVente());
            selectCompte.setStatut(Constantes.STATUT_DOC_ATTENTE);
        }

        YvsComptaAcompteClient pc = pp.getPieceVente();
        idx = pc.getPhasesReglement().indexOf(pp);
        if (idx >= 0) {
            pc.getPhasesReglement().set(idx, pp);
        }
        idx = selectCompte.getPhasesReglement().indexOf(pp);
        if (idx >= 0) {
            selectCompte.getPhasesReglement().set(idx, pp);
        }
        idx = acomptes.indexOf(selectCompte);
        if (idx > -1) {
            acomptes.set(idx, selectCompte);
            update("data_acompte_client");
        }
        return true;
    }

    public void cancelValidEtapesCredit(YvsComptaPhaseReglementCreditClient pp, boolean retour) {
        cancelValidEtapesCredit(selectReglement, pp, retour);
    }

    public boolean cancelValidEtapesCredit(YvsComptaReglementCreditClient selectReglement, YvsComptaPhaseReglementCreditClient pp, boolean retour) {
        //l'étape suivante ne doit pas être validé
        if (!asDroitValidePhase(pp.getPhaseReg())) {
            openNotAcces();
            return false;
        }
        int idx = selectReglement.getPhasesReglement().indexOf(pp);
        YvsComptaPhaseReglementCreditClient pSvt = null;
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
        if (dao.isComptabilise(pp.getId(), Constantes.SCR_PHASE_CREDIT_VENTE)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return false;

            }
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
            );
            if (w
                    != null) {
                if (w.unComptabiliserPhaseCaisseCreditVente(pp, false)) {
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
        currentPhaseCreditVente = pp;
        currentPhaseCreditVente.setDateValider(new Date());

        if (pp.getPhaseReg().getReglementOk()) {
            pp.getReglement().setStatut(Constantes.STATUT_DOC_ATTENTE);
            dao.update(pp.getReglement());
            selectReglement.setStatut(Constantes.STATUT_DOC_ATTENTE);
        }
        YvsComptaReglementCreditClient pc = pp.getReglement();
        idx = pc.getPhasesReglement().indexOf(pp);
        if (idx >= 0) {
            pc.getPhasesReglement().set(idx, pp);
        }
        idx = selectReglement.getPhasesReglement().indexOf(pp);
        if (idx >= 0) {
            selectReglement.getPhasesReglement().set(idx, pp);
        }
        update("header_form_suivi_pcv");
        return true;
    }

    public boolean cancelAllEtapesAcompte() {
        return cancelAllEtapesAcompte(selectCompte, false);
    }

    public boolean cancelAllEtapesAcompte(boolean force) {
        return cancelAllEtapesAcompte(selectCompte, force);
    }

    public boolean cancelAllEtapesAcompte(YvsComptaAcompteClient y, boolean force) {
        if (!autoriser("compta_cancel_piece_valide")) {
            openNotAcces();
            return false;
        }
        if (y != null ? y.getId() != null ? y.getId() > 0 : false : false) {
            //vérifie le droit:
            boolean succes = true;
            if (y.getStatut() != Constantes.STATUT_DOC_PAYER || force) {
                try {
                    int i = 0;
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class
                    );
                    for (YvsComptaPhaseAcompteVente ph
                            : y.getPhasesReglement()) {
                        ph.setPhaseOk(false);
                        ph.setEtapeActive(i == 0);
                        ph.setDateValider(null);
                        ph.setAuthor(currentUser);
                        dao.update(ph);
                        if (w != null) {
                            w.unComptabiliserPhaseAcompteVente(ph, false);
                        }
                        i++;
                    }
                    succes = changeStatutAcompte(null, y, Constantes.STATUT_DOC_ATTENTE);
                    if (succes) {
                        if (w != null) {
                            w.unComptabiliserAcompteClient(y);
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
        update("head_form_suivi_pav");
        return false;
    }

    public boolean cancelAllEtapesCredit() {
        return cancelAllEtapesCredit(selectReglement);
    }

    public boolean cancelAllEtapesCredit(YvsComptaReglementCreditClient selectReglement) {
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
                    for (YvsComptaPhaseReglementCreditClient ph
                            : selectReglement.getPhasesReglement()) {
                        ph.setPhaseOk(false);
                        ph.setDateValider(null);
                        ph.setEtapeActive(i == 0);
                        ph.setAuthor(currentUser);
                        dao.update(ph);
                        if (w != null) {
                            w.unComptabiliserPhaseCaisseCreditVente(ph, false);
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
        update("header_form_suivi_pcv");
        return false;
    }

    private void ordonnePhase(List<YvsComptaPhaseAcompteVente> l, YvsComptaPhaseAcompteVente p) {
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

    private List<YvsComptaPhaseAcompteVente> ordonnePhase(List<YvsComptaPhaseAcompteVente> l) {
        Collections.sort(l, new YvsComptaPhaseAcompteVente());
        int idx = 0;
        while (idx < l.size()) {
            ordonnePhase(l, l.get(idx));
            idx++;
        }
        return l;
    }

    public
            void onSelectDistantFacture(YvsComDocVentes y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedFactureVente s = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class
            );
            if (s
                    != null) {
                s.onSelectObject(y);
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Factures Vente", "modGescom", "smenFactureVente", true);
                }
            }
        }
    }

    public void onSelectDistantFactures(AcomptesVenteDivers a) {
        YvsComDocVentes y = new YvsComDocVentes();

        if (a.getNotifs() != null ? a.getNotifs().getId() > 0 : false) {
            y = a.getNotifs().getPieceVente().getVente();
            onSelectDistantFacture(y);
        } else {

            if (a.getNotif_divers() != null ? a.getNotif_divers().getId() > 0 : false) {
                YvsComptaCaisseDocDivers z = new YvsComptaCaisseDocDivers();
                z = a.getNotif_divers().getPieceDocDivers().getDocDivers();
                ManagedDocDivers m = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class
                );
                if (m
                        != null) {
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
            void onSelectDistantCheque(YvsComptaAcompteClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedReglementVente s = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class
            );
            if (s
                    != null) {
                s.onSelectAcompteForCheque(y);
                s.setSelectPiece((YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findByExterne", new String[]{"idExterne", "table"}, new Object[]{y.getId(), Constantes.SCR_ACOMPTE_VENTE}));
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Suivi des chèques", "modCompta", "smenSuiviRegVente", true);
                }
            }
        }
    }

    public
            void onSelectDistantCreditCheque(YvsComptaReglementCreditClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedReglementVente s = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class
            );
            if (s
                    != null) {
                s.onSelectCreditForCheque(y);
                s.setSelectPiece((YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findByExterne", new String[]{"idExterne", "table"}, new Object[]{y.getId(), Constantes.SCR_CREDIT_VENTE}));
                Navigations n = (Navigations) giveManagedBean(Navigations.class);
                if (n != null) {
                    n.naviguationView("Suivi des chèques", "modCompta", "smenSuiviRegVente", true);
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
                    List<YvsComptaAcompteClient> list = new ArrayList<>();
                    list.addAll(acomptes);
                    for (YvsComptaAcompteClient y : list) {
                        y.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_VENTE));
                        if (comptabilised ? !y.isComptabilised() : y.isComptabilised()) {
                            acomptes.remove(y);
                        }
                    }
                } else {
                    List<YvsComptaCreditClient> list = new ArrayList<>();
                    list.addAll(credits);
                    for (YvsComptaCreditClient y : list) {
                        y.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_CREDIT_VENTE));
                        if (comptabilised ? !y.isComptabilised() : y.isComptabilised()) {
                            credits.remove(y);
                        }
                    }
                }
            }
        }
        update("data_acompte_client");
        update("data_credit_client");
    }

    @Override
    public AcompteClient recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void generatedPhaseAcompte(YvsComptaAcompteClient selectAcompte) {
        if (selectAcompte != null && !selectAcompte.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
            if (selectAcompte.getPhasesReglement() != null) {
                try {
                    if (selectAcompte.getCaisse() != null ? selectAcompte.getCaisse().getId() < 1 : true) {
                        getErrorMessage("Vous devez precisez la caisse qui a été mouvementé");
                        return;
                    }
                    for (YvsComptaPhaseAcompteVente ph : selectAcompte.getPhasesReglement()) {
                        ph.setAuthor(currentUser);
                        dao.delete(ph);
                    }
                    selectAcompte.getPhasesReglement().clear();
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{selectAcompte.getModel(), false});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhaseAcompteVente pp;
                    if (selectAcompte.getPhasesReglement() == null) {
                        selectAcompte.setPhasesReglement(new ArrayList<YvsComptaPhaseAcompteVente>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhaseAcompteVente(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        pp.setPieceVente(selectAcompte);
                        if (selectAcompte.getCaisse() != null ? selectAcompte.getCaisse().getId() > 0 : false) {
                            pp.setCaisse(selectAcompte.getCaisse());
                        }
                        pp = (YvsComptaPhaseAcompteVente) dao.save1(pp);
                        selectAcompte.getPhasesReglement().add(pp);
                    }
                    update("table_list_piece_cheque");
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

    public void generatedPhaseCredit(YvsComptaReglementCreditClient selectCredit) {
        if (selectCredit == null) {
            getErrorMessage("Aucune pièce de règlement n'a été selectionné !");
            return;
        }
        if (selectCredit != null && !selectCredit.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
            if (selectCredit.getPhasesReglement() != null) {
                try {
                    if (selectCredit.getCaisse() != null ? selectCredit.getCaisse().getId() < 1 : true) {
                        getErrorMessage("Vous devez precisez la caisse qui a été mouvementé");
                        return;
                    }
                    for (YvsComptaPhaseReglementCreditClient ph : selectCredit.getPhasesReglement()) {
                        ph.setAuthor(currentUser);
                        dao.delete(ph);
                    }
                    selectCredit.getPhasesReglement().clear();
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{selectCredit.getModel(), false});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhaseReglementCreditClient pp;
                    if (selectCredit.getPhasesReglement() == null) {
                        selectCredit.setPhasesReglement(new ArrayList<YvsComptaPhaseReglementCreditClient>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhaseReglementCreditClient(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        pp.setReglement(selectCredit);
                        if (selectCredit.getCaisse() != null ? selectCredit.getCaisse().getId() > 0 : false) {
                            pp.setCaisse(selectCredit.getCaisse());
                        }
                        pp = (YvsComptaPhaseReglementCreditClient) dao.save1(pp);
                        selectCredit.getPhasesReglement().add(pp);
                    }
                    update("table_list_piece_cheque");
                    succes();
                } catch (Exception ex) {
                    getErrorMessage("Impossible de réaliser cette action !");
                }
            }
        } else {
            if (selectCredit.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce est déjà payé !");
            }
        }
    }

    public void equilibreAll() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Integer> ids = decomposeSelection(tabIds);
                for (Integer index : ids) {
                    YvsComptaAcompteClient bean = acomptes.get(index);
                    equilibre(bean, false);
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void equilibre(YvsComptaAcompteClient y) {
        equilibre(y, true);
    }

    public void equilibre(YvsComptaAcompteClient y, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            y.setStatutNotif(equilibreAcompte(y.getId()));
            int idx = acomptes.indexOf(y);
            if (idx > -1) {
                acomptes.set(idx, y);
                update("data_acompte_client");
            }
        }
    }

    public char equilibreAcompte(long id) {
        char statut = Constantes.STATUT_DOC_ATTENTE;
        if (id > 0) {
            String query = "SELECT equilibre_acompte_client(?)";
            String result = (String) dao.loadObjectBySqlQuery(query, new Options[]{new Options(id, 1)});
            if (Util.asString(result)) {
                statut = result.charAt(0);
            }
        }
        return statut;
    }

    public void lettrer(YvsComptaAcompteClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                boolean comptabilise = w.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_VENTE);
                if (!comptabilise) {
                    getInfoMessage("Cet acompte n'est pas comptabilisée");
                    return;
                }
                w.setContenusLettrer(w.lettrerAcompteClient(y));
                if (w.getContenusLettrer() != null ? !w.getContenusLettrer().isEmpty() : false) {
                    openDialog("dlgLettrage");
                    update("data_contenu_journal");
                }
            }
        }
    }

    public void calculerSoldes() {
        soldes.clear();
        currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (w != null) {
            int ecartDaySoldeClient = 7;
            int nombreLigneSoldeClient = 4;
            if (currentParam != null) {
                ecartDaySoldeClient = currentParam.getEcartDaySoldeClient();
                nombreLigneSoldeClient = currentParam.getNombreLigneSoldeClient();
            }
            if (ecartDaySoldeClient > 0) {
                YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findForLast", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
                if (exo != null ? exo.getId() < 1 : true) {
                    getErrorMessage("Vous n'avez pas d'exercice paramètré");
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                double solde;
                String libelle;
                Date fin;
                Date debut;
                for (int i = 0; i < nombreLigneSoldeClient - 1; i++) {
                    fin = calendar.getTime();
                    calendar.add(Calendar.DAY_OF_YEAR, -ecartDaySoldeClient);
                    debut = calendar.getTime();
                    solde = w.creance(new YvsComClient(compte.getClient().getId()), debut, fin, false);
                    if (ecartDaySoldeClient > 1) {
                        libelle = "Du " + formatDate.format(fin) + " au " + formatDate.format(debut);
                        calendar.add(Calendar.DAY_OF_YEAR, -1);
                    } else {
                        libelle = formatDate.format(fin);
                    }
                    soldes.add(new Object[]{libelle, solde});
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                fin = calendar.getTime();
                libelle = "Avant le " + formatDate.format(fin);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                fin = calendar.getTime();
                calendar.setTime(exo.getDateDebut());
                debut = calendar.getTime();
                solde = w.creance(new YvsComClient(compte.getClient().getId()), debut, fin, false);
                soldes.add(new Object[]{libelle, solde});
            }
        }
    }

    public boolean getIsFacture() {
        return isFacture;
    }

    public void setIsFacture(boolean isFacture) {
        this.isFacture = isFacture;
    }

    public void changeLabel(boolean t) {
        isFacture = t;
        update("form_reglement_acompte");
    }

    public void comptabilisePieceAll() {
        try {
            for (AcomptesVenteDivers piece : compte.getVenteDiverses()) {
                comptabilisePiece(piece, false);
            }
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                w.lettrerAcompteClient(selectCompte);
            }
            succes();
        } catch (Exception ex) {
            getException("comptabilisePieceAll", ex);
        }
    }

    public void comptabilisePiece(AcomptesVenteDivers piece, boolean msg) {
        try {
            if (piece != null ? piece.getId() > 0 : false) {
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    boolean succes = false;
                    if (selectNotif.getType().equals("VENTE")) {
                        succes = w.comptabiliserCaisseVente(piece.getNotifs().getPieceVente(), msg, msg);
                    } else {
                        succes = w.comptabiliserCaisseDivers(piece.getNotif_divers().getPieceDocDivers(), msg, msg);
                    }
                    piece.setComptabilise(succes);
                    int index = compte.getVenteDiverses().indexOf(piece);
                    if (index > -1) {
                        compte.getVenteDiverses().set(index, piece);
                    }
                }
            }
        } catch (Exception ex) {
            getException("comptabilisePiece", ex);
        }
    }

    public void unComptabilisePiece(AcomptesVenteDivers piece, boolean msg) {
        try {
            if (piece != null ? piece.getId() > 0 : false) {
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    boolean succes = false;
                    if (selectNotif.getType().equals("VENTE")) {
                        succes = w.unComptabiliserCaisseVente(piece.getNotifs().getPieceVente(), msg);
                    } else {
                        succes = w.unComptabiliserCaisseDivers(piece.getNotif_divers().getPieceDocDivers(), msg);
                    }
                    if (succes) {
                        piece.setComptabilise(false);
                        int index = compte.getVenteDiverses().indexOf(piece);
                        if (index > -1) {
                            compte.getVenteDiverses().set(index, piece);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getException("unComptabilisePiece", ex);
        }
    }

    public boolean isComptabilisePiece(AcomptesVenteDivers y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                boolean comptabilise = false;
                if (y.getType().equals("VENTE")) {
                    comptabilise = w.isComptabilise(y.getNotifs().getPieceVente().getId(), Constantes.SCR_CAISSE_VENTE);
                } else {
                    comptabilise = w.isComptabilise(y.getNotif_divers().getPieceDocDivers().getId(), Constantes.SCR_CAISSE_DIVERS);
                }
                y.setComptabilise(comptabilise);
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseBeanCredit(CreditClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CREDIT_VENTE));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseCredit(YvsComptaCreditClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CREDIT_VENTE));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseCaisseCredit(YvsComptaReglementCreditClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CAISSE_CREDIT_VENTE));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isComptabilisePhaseCaisseCredit(YvsComptaPhaseReglementCreditClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_PHASE_CREDIT_VENTE));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseBeanAcompte(AcompteClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_VENTE));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabiliseAcompte(YvsComptaAcompteClient y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_ACOMPTE_VENTE));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isComptabilisePhaseAcompte(YvsComptaPhaseAcompteVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public boolean isTrouver() {
        return trouver;
    }

    public void setTrouver(boolean trouver) {
        this.trouver = trouver;
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void changeNatureAcompte(YvsComptaAcompteClient y, boolean all) {
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
            YvsComptaAcompteClient y;
            for (Integer i : keys) {
                y = acomptes.get(i);
                changeNatureAcompte(y, true);
            }
            getInfoMessage("Opération terminé avec succès !");
        }
    }

}
