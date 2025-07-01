/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.caisse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.UtilCompta;
import yvs.base.tiers.Tiers;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.commercial.vente.DocVente;
import yvs.commercial.vente.ManagedFactureVenteV2;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.client.AcompteClient;
import yvs.comptabilite.client.ManagedOperationClient;
import yvs.comptabilite.fournisseur.ManagedOperationFournisseur;
import yvs.comptabilite.tresorerie.ManagedDocDivers;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseTrancheReglement;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaNotifReglementVente;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.compta.YvsComptaPhasePieceAchat;
import yvs.entity.compta.YvsComptaPhasePieceDivers;
import yvs.entity.compta.YvsComptaPhasePieceVirement;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.compta.YvsComptaPiecesComptable;
import yvs.entity.compta.YvsComptaReglementCreditClient;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.achat.YvsComptaPhaseAcompteAchat;
import yvs.entity.compta.achat.YvsComptaReglementCreditFournisseur;
import yvs.entity.compta.client.YvsComptaPhaseReglementCreditClient;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.fournisseur.YvsComptaPhaseReglementCreditFournisseur;
import yvs.entity.compta.vente.YvsComptaPhaseAcompteVente;
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.entity.users.YvsUsers;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ContratEmploye;
import yvs.grh.paie.ManagedRetenue;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.service.com.vente.AYvsComDocVentes;
import yvs.service.compta.doc.divers.AYvsComptaAcompteClient;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Util;
import yvs.util.enume.Nombre;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedReglementVente extends Managed implements Serializable {

//    private DocVente selectedDoc = new DocVente();
    private YvsComptaCaissePieceVente pieceVente = newPieceCaiss();
    private boolean displayBtnSave;
    private PieceTresorerie pieceAvance = new PieceTresorerie();
    private YvsComptaAcompteClient selectAcompte = new YvsComptaAcompteClient();
    private YvsComptaReglementCreditClient selectCredit = new YvsComptaReglementCreditClient();
    private YvsComptaPhaseAcompteVente selectPhaseAcompteVente;
    private YvsComptaPhaseReglementCreditClient selectPhaseCreditVente;

    private YvsComParametreVente currentParam;
    private boolean objectPhaseOk;

    private List<YvsComptaCaissePieceVente> reglements;
    private List<YvsUsers> caissiers;
    private boolean dateSearch, dateValidePhase;
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();
    private Date dateDebutPhaseSearch = new Date(), dateFinPhaseSearch = new Date();
    private Date dateDebutCorrection = new Date(), dateFinCorrection = new Date();

    private PieceTresorerie pieceReglement = new PieceTresorerie();
//    private List<YvsComptaCaissePieceVente> cheques;
    private YvsComptaCaissePieceVente selectedPiece, selectExtourne;
    PaginatorResult<YvsComptaCaissePieceVente> paginatrorData = new PaginatorResult<>();

    private List<YvsComptaMouvementCaisse> mouvements;
    private YvsComptaMouvementCaisse selectPiece = new YvsComptaMouvementCaisse();
    private YvsComptaPhasePiece currentPhaseVente = new YvsComptaPhasePiece();
    private YvsComptaPhasePiece selectPhaseVente = new YvsComptaPhasePiece();

    PaginatorResult<YvsComptaMouvementCaisse> paginatorData = new PaginatorResult<>();
    private String reference = "";

    @ManagedProperty(value = "#{managedReglementAchat}")
    private ManagedReglementAchat managedReglementAchat;

    @ManagedProperty(value = "#{managedVirement}")
    private ManagedVirement managedVirement;

    @ManagedProperty(value = "#{managedDocDivers}")
    private ManagedDocDivers managedDocDivers;

    private double montantRetour = 0;
    private boolean needConfirmation = false;

    private Boolean comptaSearch;
    private String clientF, numPieceF, typeSearch, statutFC, modeSearch;
    private long nbrComptaSearch;
    private long caisseF, agenceSearch;
    private Character statutF = Constantes.STATUT_DOC_PAYER;
    private String operateurStatut = "!=";
    private String operateurMode = "=";
    private String operateurType = "=";
    private String operateurMontant = "=";
    private int modeFind;
    private double montantF, montantF1;
    private Long agenceFind = 0L;

    public ManagedReglementVente() {
        reglements = new ArrayList<>();
        mouvements = new ArrayList<>();
        caissiers = new ArrayList<>();
//        cheques = new ArrayList<>();
    }

    public Date getDateDebutCorrection() {
        return dateDebutCorrection;
    }

    public void setDateDebutCorrection(Date dateDebutCorrection) {
        this.dateDebutCorrection = dateDebutCorrection;
    }

    public Date getDateFinCorrection() {
        return dateFinCorrection;
    }

    public void setDateFinCorrection(Date dateFinCorrection) {
        this.dateFinCorrection = dateFinCorrection;
    }

    public long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public long getNbrComptaSearch() {
        return nbrComptaSearch;
    }

    public void setNbrComptaSearch(long nbrComptaSearch) {
        this.nbrComptaSearch = nbrComptaSearch;
    }

    public List<YvsUsers> getCaissiers() {
        return caissiers;
    }

    public void setCaissiers(List<YvsUsers> caissiers) {
        this.caissiers = caissiers;
    }

    public boolean isDateValidePhase() {
        return dateValidePhase;
    }

    public void setDateValidePhase(boolean dateValidePhase) {
        this.dateValidePhase = dateValidePhase;
    }

    public Date getDateDebutPhaseSearch() {
        return dateDebutPhaseSearch;
    }

    public void setDateDebutPhaseSearch(Date dateDebutPhaseSearch) {
        this.dateDebutPhaseSearch = dateDebutPhaseSearch;
    }

    public Date getDateFinPhaseSearch() {
        return dateFinPhaseSearch;
    }

    public void setDateFinPhaseSearch(Date dateFinPhaseSearch) {
        this.dateFinPhaseSearch = dateFinPhaseSearch;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isObjectPhaseOk() {
        return objectPhaseOk;
    }

    public void setObjectPhaseOk(boolean objectPhaseOk) {
        this.objectPhaseOk = objectPhaseOk;
    }

    public YvsComParametreVente getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsComParametreVente currentParam) {
        this.currentParam = currentParam;
    }

    public YvsComptaPhaseReglementCreditClient getSelectPhaseCreditVente() {
        return selectPhaseCreditVente;
    }

    public void setSelectPhaseCreditVente(YvsComptaPhaseReglementCreditClient selectPhaseCreditVente) {
        this.selectPhaseCreditVente = selectPhaseCreditVente;
    }

    public YvsComptaPhasePiece getCurrentPhaseVente() {
        return currentPhaseVente;
    }

    public void setCurrentPhaseVente(YvsComptaPhasePiece currentPhaseVente) {
        this.currentPhaseVente = currentPhaseVente;
    }

    public YvsComptaCaissePieceVente getSelectExtourne() {
        return selectExtourne;
    }

    public void setSelectExtourne(YvsComptaCaissePieceVente selectExtourne) {
        this.selectExtourne = selectExtourne;
    }

    public ManagedDocDivers getManagedDocDivers() {
        return managedDocDivers;
    }

    public void setManagedDocDivers(ManagedDocDivers managedDocDivers) {
        this.managedDocDivers = managedDocDivers;
    }

    public YvsComptaPhaseAcompteVente getSelectPhaseAcompteVente() {
        return selectPhaseAcompteVente;
    }

    public void setSelectPhaseAcompteVente(YvsComptaPhaseAcompteVente selectPhaseAcompteVente) {
        this.selectPhaseAcompteVente = selectPhaseAcompteVente;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public YvsComptaReglementCreditClient getSelectCredit() {
        return selectCredit;
    }

    public void setSelectCredit(YvsComptaReglementCreditClient selectCredit) {
        this.selectCredit = selectCredit;
    }

    public double getMontantRetour() {
        return montantRetour;
    }

    public void setMontantRetour(double montantRetour) {
        this.montantRetour = montantRetour;
    }

    public String getOperateurType() {
        return operateurType;
    }

    public void setOperateurType(String operateurType) {
        this.operateurType = operateurType;
    }

    public String getModeSearch() {
        return modeSearch;
    }

    public void setModeSearch(String modeSearch) {
        this.modeSearch = modeSearch;
    }

    public ManagedVirement getManagedVirement() {
        return managedVirement;
    }

    public void setManagedVirement(ManagedVirement managedVirement) {
        this.managedVirement = managedVirement;
    }

    public YvsComptaPhasePiece getSelectPhaseVente() {
        return selectPhaseVente;
    }

    public void setSelectPhaseVente(YvsComptaPhasePiece selectPhaseVente) {
        this.selectPhaseVente = selectPhaseVente;
    }

    public ManagedReglementAchat getManagedReglementAchat() {
        return managedReglementAchat;
    }

    public void setManagedReglementAchat(ManagedReglementAchat managedReglementAchat) {
        this.managedReglementAchat = managedReglementAchat;
    }

    public String getTypeSearch() {
        return typeSearch;
    }

    public void setTypeSearch(String typeSearch) {
        this.typeSearch = typeSearch;
    }

    public List<YvsComptaMouvementCaisse> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<YvsComptaMouvementCaisse> mouvements) {
        this.mouvements = mouvements;
    }

    public YvsComptaMouvementCaisse getSelectPiece() {
        return selectPiece;
    }

    public void setSelectPiece(YvsComptaMouvementCaisse selectPiece) {
        this.selectPiece = selectPiece;
    }

    public PaginatorResult<YvsComptaMouvementCaisse> getPaginatorData() {
        return paginatorData;
    }

    public void setPaginatorData(PaginatorResult<YvsComptaMouvementCaisse> paginatorData) {
        this.paginatorData = paginatorData;
    }

    public YvsComptaAcompteClient getSelectAcompte() {
        return selectAcompte;
    }

    public void setSelectAcompte(YvsComptaAcompteClient selectAcompte) {
        this.selectAcompte = selectAcompte;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public PaginatorResult<YvsComptaCaissePieceVente> getPaginatrorData() {
        return paginatrorData;
    }

    public void setPaginatrorData(PaginatorResult<YvsComptaCaissePieceVente> paginatrorData) {
        this.paginatrorData = paginatrorData;
    }

    public long getCaisseF() {
        return caisseF;
    }

    public void setCaisseF(long caisseF) {
        this.caisseF = caisseF;
    }

    public String getOperateurMode() {
        return operateurMode;
    }

    public void setOperateurMode(String operateurMode) {
        this.operateurMode = operateurMode;
    }

    public int getModeFind() {
        return modeFind;
    }

    public void setModeFind(int modeFind) {
        this.modeFind = modeFind;
    }

    public String getOperateurMontant() {
        return operateurMontant;
    }

    public void setOperateurMontant(String operateurMontant) {
        this.operateurMontant = operateurMontant;
    }

    public double getMontantF() {
        return montantF;
    }

    public void setMontantF(double montantF) {
        this.montantF = montantF;
    }

    public double getMontantF1() {
        return montantF1;
    }

    public void setMontantF1(double montantF1) {
        this.montantF1 = montantF1;
    }

    public Long getAgenceFind() {
        return agenceFind;
    }

    public void setAgenceFind(Long agenceFind) {
        this.agenceFind = agenceFind;
    }

    public boolean isDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(boolean dateSearch) {
        this.dateSearch = dateSearch;
    }

    public Date getDateDebutSearch() {
        return dateDebutSearch;
    }

    public void setDateDebutSearch(Date dateDebutSearch) {
        this.dateDebutSearch = dateDebutSearch;
    }

    public Date getDateFinSearch() {
        return dateFinSearch;
    }

    public void setDateFinSearch(Date dateFinSearch) {
        this.dateFinSearch = dateFinSearch;
    }

    public List<YvsComptaCaissePieceVente> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceVente> reglements) {
        this.reglements = reglements;
    }

    public YvsComptaCaissePieceVente getPieceVente() {
        return pieceVente;
    }

    public void setPieceVente(YvsComptaCaissePieceVente pieceVente) {
        this.pieceVente = pieceVente;
    }

    public boolean isDisplayBtnSave() {
        return displayBtnSave;
    }

    public void setDisplayBtnSave(boolean displayBtnSave) {
        this.displayBtnSave = displayBtnSave;
    }

    public PieceTresorerie getPieceAvance() {
        return pieceAvance;
    }

    public void setPieceAvance(PieceTresorerie pieceAvance) {
        this.pieceAvance = pieceAvance;
    }

    public String getNumPieceF() {
        return numPieceF;
    }

    public void setNumPieceF(String numPieceF) {
        this.numPieceF = numPieceF;
    }

    public String getClientF() {
        return clientF;
    }

    public void setClientF(String clientF) {
        this.clientF = clientF;
    }

    public PieceTresorerie getPieceReglement() {
        return pieceReglement;
    }

    public void setPieceReglement(PieceTresorerie pieceReglement) {
        this.pieceReglement = pieceReglement;
    }

    public YvsComptaCaissePieceVente getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(YvsComptaCaissePieceVente selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public boolean isNeedConfirmation() {
        return needConfirmation;
    }

    public void setNeedConfirmation(boolean needConfirmation) {
        this.needConfirmation = needConfirmation;
    }

    public Character getStatutF() {
        return statutF;
    }

    public void setStatutF(Character statutF) {
        this.statutF = statutF;
    }

    public String getOperateurStatut() {
        return operateurStatut;
    }

    public void setOperateurStatut(String operateurStatut) {
        this.operateurStatut = operateurStatut;
    }

    public String getStatutFC() {
        return statutFC;
    }

    public void setStatutFC(String statutFC) {
        this.statutFC = statutFC;
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean controleFiche(Date date) {
        date = date != null ? date : new Date();
        champ = new String[]{"dateJour", "societe"};
        val = new Object[]{date, currentAgence.getSociete()};
        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", champ, val);
        if (exo != null ? exo.getId() < 1 : true) {
            getErrorMessage("Le document doit etre enregistré dans un exercice actif");
            return false;
        }
        if (exo.getCloturer()) {
            getErrorMessage("Le document ne peut être enregistré dans un exercice clôturé");
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteBeanExtourne() {
        try {
            if (selectExtourne != null ? selectExtourne.getId() > 0 : false) {
                for (YvsComptaCaissePieceVente p : selectExtourne.getSousVentes()) {
                    if (p.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                        getErrorMessage("Le reglement est déjà payé");
                        return;
                    }
                }
                dao.delete(selectExtourne);
                pieceReglement.getSousVentes().remove(selectExtourne);
                succes();
                update("data_extourne_caisse_vente");
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Serializable recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void findOneFacture(String reference) {
        ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        if (service != null) {
            if (reference != null ? reference.trim().length() > 0 : false) {
                service.setNumSearch_(reference);
                service.setOperateurRef("LIKE");
                service.findFactureByNumAndNotRegle();
                if (service.getDocuments().size() == 1) {
                    onSelectedFacture(service.getDocuments().get(0));
                } else if (service.getDocuments().size() > 1) {
                    openDialog("dlgFacture");
                    update("data_facture_vente_reg");
                } else {
                    pieceAvance.setDocVente(new DocVente());
                }
            } else {
                pieceAvance.setDocVente(new DocVente());
            }
        }

    }

    public void findByClient(String reference) {
        if (reference != null ? reference.trim().length() > 0 : false) {
            ManagedClient w = (ManagedClient) giveManagedBean(ManagedClient.class);
            pieceAvance.getClients().setId(0);
            pieceAvance.getClients().setError(true);
            pieceAvance.getClients().setTiers(new Tiers());
            if (w != null) {
                Client y = w.searchClient(reference, true);
                if (y != null ? y.getId() > 0 : false) {
                    pieceAvance.setClients(y);
                    pieceAvance.getClients().setError(false);

                    ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (service != null) {
                        service.setCodeClient_(reference);
                        service.setOperateuClt("LIKE");
                        service.searchByClient();
                        if (service.getDocuments() != null ? !service.getDocuments().isEmpty() : false) {
                            onSelectedFacture(service.getDocuments().get(0));
                        }
                        update("data_facture_vente_reg");
                    }
                }
            }
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            onSelectedFacture((YvsComDocVentes) ev.getObject());
        }
    }

    public void onSelectedFacture(YvsComDocVentes dv) {
        loadDataAcompteClient(dv.getClient());
        ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        if (service != null) {
            service.setDocVente(UtilCom.buildBeanDocVente(dv));
            service.setMontantTotalDoc(service.getDocVente());
            pieceAvance.setDocVente(service.getDocVente());
        }
        pieceAvance.setClients(pieceAvance.getDocVente().getClient());
        displayBtnSave = false;
        pieceVente = newPieceCaiss();
        pieceAvance.setMontant(pieceVente.getMontant());
        update("all_bloc_info_reg");
        update("zone_pcv_edit_pieceFV");
    }

    public void loadDataAcompteClient(YvsComClient bean) {
        List<YvsComptaAcompteClient> acomptes = new ArrayList<>();
        String query = "SELECT y.id FROM yvs_compta_acompte_client y WHERE y.montant > COALESCE((SELECT SUM(r.montant) FROM yvs_compta_caisse_piece_vente r INNER JOIN yvs_compta_notif_reglement_vente n ON n.piece_vente = r.id WHERE n.acompte = y.id AND r.statut_piece = 'P'), 0) AND y.statut = 'P' AND y.client = ?";
        List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(bean.getId(), 1)});
        if (ids != null ? !ids.isEmpty() : false) {
            acomptes = dao.loadNameQueries("YvsComptaAcompteClient.findByIds", new String[]{"ids"}, new Object[]{ids});
        }
        bean.setAcompte(0);
        for (YvsComptaAcompteClient f : acomptes) {
            Double reste = AYvsComptaAcompteClient.findResteForAcompte(f, dao);
            f.setReste(reste);
            bean.setAcompte(bean.getAcompte() + f.getReste());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void chooseModelReglement() {
        ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        if (service.getDocVente().getModeReglement() != null ? service.getDocVente().getModeReglement().getId() > 0 : false) {
            ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
            if (m != null) {
                int idx = m.getModels().indexOf(new YvsBaseModelReglement(service.getDocVente().getModeReglement().getId()));
                if (idx >= 0) {
                    YvsBaseModelReglement y = m.getModels().get(idx);
                    service.getDocVente().setModeReglement(UtilCom.buildBeanModelReglement(y));
                }
            }
        }
    }

    @Override
    public void doNothing() {

    }

    public void initView() {
        agenceFind = (agenceFind <= 0) ? currentAgence != null ? currentAgence.getId() : 0 : 0;
    }

    @Override
    public void loadAll() {
        if (giveExerciceActif() != null) {
//            dao.getEquilibreVente(giveExerciceActif().getDateDebut(), giveExerciceActif().getDateFin());
        }
        if (pieceVente != null ? (pieceVente.getId() != null ? pieceVente.getId() < 1 : true) : true) {
            pieceVente = new YvsComptaCaissePieceVente();
        }
        if (pieceVente.getCaisse() != null ? pieceVente.getCaisse().getId() < 1 : true) {
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service != null ? currentUser != null : false) {
                pieceVente.setCaisse(service.findByResponsable(currentUser.getUsers()));
            } else {
                pieceVente.setCaisse(new YvsBaseCaisse());
            }
        }
        if (pieceVente.getModel() != null ? pieceVente.getModel().getId() < 1 : true) {
            pieceVente.setModel(modeEspece());
        }

        if (pieceAvance != null ? pieceAvance.getId() < 1 : true) {
            pieceAvance = new PieceTresorerie();
        }
        if (pieceAvance.getCaisse() != null ? pieceAvance.getCaisse().getId() < 1 : true) {
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service != null ? currentUser != null : false) {
                pieceAvance.setCaisse(UtilCompta.buildBeanCaisse(service.findByResponsable(currentUser.getUsers())));
            } else {
                pieceAvance.setCaisse(new Caisses());
            }
        }
        if (pieceAvance.getMode() != null ? pieceAvance.getMode().getId() < 1 : true) {
            pieceAvance.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        }
    }

    public boolean controleCreateOrUpdatePiece(PieceTresorerie bean) {
        if (!bean.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
            //si on est en train de modifier un règlement, on vérifie si l'enregistrement précédent avait des phases de règlement bancaire
            for (YvsComptaPhasePiece pp : bean.getPhases()) {
                if (pp.getPhaseOk()) {
                    //si une phase a déjà fait l'objet d'une validation, alors plus la peine de contnuer la modification
                    openDialog("dlgConfirmChangeMode");
                    return false;
                }
            }
            if (!bean.getPhases().isEmpty()) {
                openDialog("dlgConfirmDeletePhase");
                return false;
            }
        }
        return true;
    }

    public YvsComptaCaissePieceVente buildPieceFromModel(long id, YvsBaseModeReglement mode, DocVente d, YvsBaseCaisse caisse, Date date, double montant) {
        return buildPieceFromModel(id, mode, d, caisse, date, montant, Constantes.MOUV_CAISS_ENTREE.charAt(0));
    }

    public YvsComptaCaissePieceVente buildPieceFromModel(long id, YvsBaseModeReglement mode, DocVente d, YvsBaseCaisse caisse, Date date, double montant, char mouvement) {
        return buildPieceFromModel(id, mode, UtilCom.buildDocVente(d, currentUser), caisse, date, montant, mouvement);
    }

    public YvsComptaCaissePieceVente buildPieceFromModel(long id, YvsBaseModeReglement mode, YvsComDocVentes d, YvsBaseCaisse caisse, Date date, double montant, char mouvement) {
        YvsComptaCaissePieceVente piece = new YvsComptaCaissePieceVente(id);
        piece.setAuthor(currentUser);
        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        piece.setMontant(montant);
        piece.setMontantRecu(montant);
        piece.setDatePaimentPrevu(date);
        piece.setMouvement(mouvement);
        piece.setDatePiece(new Date());
        String ref = genererReference(Constantes.TYPE_PC_VENTE_NAME, piece.getDatePiece());
        if (ref != null ? ref.trim().length() < 1 : true) {
            return null;
        }
        piece.setNumeroPiece(ref);
        if (!Util.asString(piece.getReferenceExterne())) {
            piece.setReferenceExterne(d.getNumDoc());
        }
        piece.setVente(d);
        if ((mode != null) ? mode.getId() > 0 : false) {
            piece.setModel(mode);
        }
        if ((caisse != null) ? caisse.getId() > 0 : false) {
            piece.setCaisse(caisse);
        }
        piece.setComptabilise(false);
        piece.setNew_(true);
        return piece;
    }

    public List<YvsComptaCaissePieceVente> generetedPiecesFromModel(YvsBaseModelReglement model, DocVente d, YvsBaseCaisse caisse) {
        List<YvsComptaCaissePieceVente> re = new ArrayList<>();
        List<YvsBaseTrancheReglement> lt = dao.loadNameQueries("YvsBaseTrancheReglement.findByModel", new String[]{"model"}, new Object[]{model});
        if (d.getMontantResteAPlanifier() > 0) {
            long id = -1000;
            YvsComptaCaissePieceVente piece;
            Calendar cal = Calendar.getInstance();
            cal.setTime(d.getEnteteDoc().getDateEntete());  //date de la facturation
            double totalTaux = 0, sommeMontant = 0;
            YvsBaseModeReglement espece = modeEspece(), mode = null;
            YvsBaseTrancheReglement trch;
            if (lt != null ? !lt.isEmpty() : false) {
                for (int i = 0; i < lt.size() - 1; i++) {
                    trch = lt.get(i);
                    cal.add(Calendar.DAY_OF_MONTH, trch.getIntervalJour());
                    double montant = arrondi(d.getMontantResteAPlanifier() * trch.getTaux() / 100);
                    sommeMontant += montant;
                    if (trch.getMode() != null ? trch.getMode().getId() != null ? trch.getMode().getId() > 0 : false : false) {
                        mode = trch.getMode();
                    } else {
                        mode = espece;
                    }
                    piece = buildPieceFromModel(id++, mode, d, caisse, cal.getTime(), montant);
                    re.add(piece);
                    totalTaux += trch.getTaux();
                }
                trch = lt.get(lt.size() - 1);
                if (trch.getMode() != null ? trch.getMode().getId() != null ? trch.getMode().getId() > 0 : false : false) {
                    mode = trch.getMode();
                } else {
                    mode = espece;
                }
                totalTaux += trch.getTaux();
                cal.add(Calendar.DAY_OF_MONTH, trch.getIntervalJour());
                if (totalTaux > 100) {
                    getWarningMessage("Les tranches du model de règlement sont supérieures à 100% !");
                } else if (totalTaux < 100) {
                    getWarningMessage("Les tranches du model de règlement sont inférieure à 100% !");
                }
            }
            if (mode != null ? mode.getId() != null ? mode.getId() < 1 : true : true) {
                mode = espece;
            }
            double montant = d.getMontantResteAPlanifier() - sommeMontant;
            if (montant > 0) {
                piece = buildPieceFromModel(id++, mode, d, caisse, cal.getTime(), montant);
                re.add(piece);
            }
        } else {
            getWarningMessage("Le montant du document n'a pas été trouvé !");
        }
        return re;
    }

    public void generatedAllpiece() {
        ManagedFactureVenteV2 servicev = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        if (!servicev.getDocVente().getReglements().isEmpty()) {
            for (YvsComptaCaissePieceVente c : servicev.getDocVente().getReglements()) {
                if (c.getId() > 0) {
                    getErrorMessage("Impossible de générer un nouveau modèle de règlement !", "Un plan existe déjà");
                    return;
                }
            }
            servicev.getDocVente().getReglements().clear();
        }
        if (servicev.getDocVente().getModeReglement() != null) {
            if (servicev.getDocVente().getModeReglement().getId() > 0) {
                ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                YvsBaseCaisse c = (service != null) ? service.findByResponsable(new YvsUsers(servicev.getDocVente().getEnteteDoc().getUsers().getId())) : null;
                servicev.getDocVente().setReglements(generetedPiecesFromModel(new YvsBaseModelReglement(servicev.getDocVente().getModeReglement().getId()), servicev.getDocVente(), c));
                displayBtnSave = true;
            } else {
                getErrorMessage("La facture de vente n'est rattaché à aucun model de règlement !");
            }
        } else {
            getErrorMessage("La facture de vente n'est rattaché à aucun model de règlement !");
        }
    }

    public YvsComptaCaissePieceVente newPieceCaiss() {
        YvsComptaCaissePieceVente pc = new YvsComptaCaissePieceVente((long) 0);
        ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        pc.setModel(new YvsBaseModeReglement(0l));
        pc.setVente(new YvsComDocVentes((long) 0));
        pc.setCaisse(new YvsBaseCaisse((long) 0));
        pc.setDatePaimentPrevu(new Date());
        pc.setModel(modeEspece());
        pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        double montant = 0;
        if (service != null ? service.getDocVente() != null : false) {
            montant = service.getDocVente().getMontantTotal();
            if (service.getDocVente().getReglements() != null ? !service.getDocVente().getReglements().isEmpty() : false) {
                montant -= giveTotalPT(service.getDocVente().getReglements());
            }
        }
        pc.setMontant(montant);
        if (pc.getMontant() < 0) {
            pc.setMontant(0.0);
        }
        caissiers = new ArrayList<>();
        return pc;
    }

    public YvsComptaCaissePieceVente createNewPieceCaisse(DocVente d, YvsComptaCaissePieceVente pt, boolean delete) {
        return createNewPieceCaisse(d, pt, delete, false);
    }

    public YvsComptaCaissePieceVente createNewPieceCaisse(DocVente d, YvsComptaCaissePieceVente pt, boolean delete, boolean validePhase) {
        return createNewPieceCaisse(d, pt, delete, validePhase, Constantes.MOUV_CAISS_ENTREE.charAt(0));
    }

    public YvsComptaCaissePieceVente createNewPieceCaisse(DocVente d, YvsComptaCaissePieceVente pt, boolean delete, boolean validePhase, char mouvement) {
        if (controleAddPiece(d, pt)) {
            YvsComptaCaissePieceVente piece = new YvsComptaCaissePieceVente(pt.getId());
            piece.setStatutPiece(pt.getStatutPiece());
            piece.setMontant(pt.getMontant());
            piece.setDatePaimentPrevu(pt.getDatePaimentPrevu());
            piece.setDatePiece(pt.getDatePiece());
            piece.setDatePaiement(pt.getDatePaiement());
            piece.setVente(UtilCom.buildDocVente(d, currentUser));
            piece.setReferenceExterne(pt.getReferenceExterne());
            piece.setDateUpdate(new Date());
            piece.setDateSave(pt.getDateSave());
            piece.setAuthor(currentUser);
            piece.setNumeroPiece(pt.getNumeroPiece());
            piece.setMouvement(mouvement);
            piece.setVerouille(false);
            if (pt.getCaisse() != null) {
                piece.setCaisse((pt.getCaisse().getId() > 0) ? pt.getCaisse() : null);
            }
            if (pt.getCaissier() != null) {
                piece.setCaissier((pt.getCaissier().getId() > 0) ? pt.getCaissier() : null);
            }
            if (piece.getId() <= 0 || (piece.getNumeroPiece() != null ? piece.getNumeroPiece().trim().length() < 1 : true)) {
                String numero = genererReference(Constantes.TYPE_PC_VENTE_NAME, pt.getDatePiece(), pt.getCaisse().getId());
                if (numero != null ? numero.trim().length() < 1 : true) {
                    return null;
                }
                piece.setNumeroPiece(numero);
            }
            if (pt.getModel() != null) {
                piece.setModel((pt.getModel().getId() > 0) ? pt.getModel() : null);
            }
            if ((pt.getId() == null) ? true : pt.getId() <= 0) {
                piece.setDateSave(new Date());
                piece.setId(null);
                piece = (YvsComptaCaissePieceVente) dao.save1(piece);
            } else {
                piece.setId(pt.getId());
                dao.update(piece);
                piece.setPhasesReglement(dao.loadNameQueries("YvsComptaPhasePiece.findByPiece", new String[]{"piece"}, new Object[]{pt}));
            }
            piece.setNew_(true);
            if (!validePhase) {
                if (!pt.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) { // Si le paiement n'est pas par banque, on se rassure qu'aucune phases n'est rattaché. auquel cas, on les supprime
                    boolean ph_ok = false;
                    if (!delete) {
                        for (YvsComptaPhasePiece pp : piece.getPhasesReglement()) {
                            if (pp.getPhaseOk()) {
                                ph_ok = true;
                                break;
                            }
                        }
                    }
                    if (!ph_ok) {
                        for (YvsComptaPhasePiece pp : piece.getPhasesReglement()) {
                            try {
                                pp.setAuthor(currentUser);
                                dao.delete(pp);
                            } catch (Exception ex) {
                                log.log(Level.SEVERE, null, ex);
                            }
                        }
                        piece.getPhasesReglement().clear();
                    }
                } else if (!pieceAvance.isAcompte()) {
                    /*efface les notifs si on est dans le cadre d'une pièce lié à une avance/acompte 
                     puisque l'acompte a dejà les phases de règlements qu'il faut*/
                    if (pt.getNotifs() != null) {
                        dao.delete(pt.getNotifs());
                    }
                }
            } else {
                for (YvsComptaPhasePiece pp : piece.getPhasesReglement()) {
                    pp.setAuthor(currentUser);
                    pp.setDateUpdate(new Date());
                    pp.setPhaseOk(true);
                    dao.update(pp);
                }
            }
            if (pieceAvance.getMode().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION)) {
                ManagedCompensation service = (ManagedCompensation) giveManagedBean(ManagedCompensation.class);
                if (service != null) {
                    service.geratedPiecesCompensation(piece);
                    update("table_cmpens_p_achat");
                }
            }
            return piece;
        } else {
            pieceVente = newPieceCaiss();
        }
        return null;
    }

    public void createOnePieceCaisse(YvsComDocVentes d) {
        //build pièce trésorerie
        ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        service.setDocVente(UtilCom.buildBeanDocVente(d));
        if (service != null) {
            service.setMontantTotalDoc(service.getDocVente());
        }
        YvsComptaCaissePieceVente pt = new YvsComptaCaissePieceVente();
        pt.setAuthor(currentUser);
        ManagedCaisses service_ = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service_ != null) {
            pt.setCaisse(service_.findByResponsable(currentUser.getUsers()));
        }//trouve la caisse par défaut du vendeur
        pt.setDatePaiement(null);
        pt.setDatePaimentPrevu(new Date());
        pt.setDatePiece(new Date());
        pt.setId(null);
        pt.setMontant(arrondi(service.getDocVente().getMontantResteApayer()));
        pt.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        pt.setVente(d);
        pt.setModel(modeEspece());
        createNewPieceCaisse(service.getDocVente(), pt, false);
        service.getDocVente().setReglements(dao.loadNameQueries("YvsComptaCaissePieceVente.findByFacture", new String[]{"facture"}, new Object[]{d}));
        displayBtnSave = false;
    }

    public double giveTotalPT(List<YvsComptaCaissePieceVente> l) {
        double sum = 0;
        if (l != null) {
            for (YvsComptaCaissePieceVente p : l) {
                if (p.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && p.getStatutPiece() != Constantes.STATUT_DOC_ANNULE) {
                    sum += p.getMontant();
                }
            }
        }
        return sum;
    }

    private boolean controleAddPiece(DocVente d, YvsComptaCaissePieceVente pt) {
        if (pt.getMontant() <= 0) {
            getErrorMessage("Le montant est incorrecte !");
            return false;
        }
        if (pt.getId() > 0 && pt.getVerouille()) {
            getErrorMessage("La pièce de règlement est vérouillé !");
            return false;
        }
        if (pt.getId() > 0 && pt.getStatutPiece().equals(Constantes.STATUT_DOC_ANNULE)) {
            getErrorMessage("Vous ne pouvez modifier cette pièce de règlement,", "son statut n'est pas éditable");
            return false;
        }
        if (d != null) {
            double mtn;
            if (d.getReglements().contains(pt)) {//en cas de modification d'une pièce(isoler le montantF à comparer)
                mtn = (-d.getReglements().get(d.getReglements().indexOf(pt)).getMontant() + pt.getMontant());
            } else {
                mtn = pt.getMontant();
            }
            if (d.getMontantNetAPayer() < (giveTotalPT(d.getReglements()) + mtn) && pt.getVente() != null) {
                getErrorMessage("Le montant de la facture ne doit pas être inférieure aux règlements planifiés");
                return false;
            }
        }
        if (pt.getDatePaimentPrevu() == null) {
            getErrorMessage("Vous devez préciser la date de paiement !");
            return false;
        }
        if (pt.getStatutPiece() == Constantes.STATUT_DOC_PAYER && (pt.getId() != null) ? pt.getId() > 0 : false) {
            getErrorMessage("La pièce en cours est déjà payé !");
            return false;
        }
        if ((pt.getModel() != null) ? (pt.getModel().getId() != null ? pt.getModel().getId() < 1 : true) : true) {
            getErrorMessage("Vous devez préciser indiquer le moyen de paiement !");
            return false;
        }
        if (pt.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
            if (pt.getReferenceExterne() != null ? pt.getReferenceExterne().trim().length() < 1 : true) {
                getErrorMessage("Vous devez préciser la reference externe !");
                return false;
            }
        }
        if (!verifyDateExercice(pt.getDatePaiement())) {
            return false;
        }
        return true;
    }

    public void chooseModeReglement(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            Integer id = (int) ev.getNewValue();
            if (id != null ? id > 0 : false) {
                ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (m != null) {
                    int idx = m.getModes().indexOf(new YvsBaseModeReglement((long) id));
                    if (idx > -1) {
                        YvsBaseModeReglement o = m.getModes().get(idx);
                        pieceAvance.setMode(UtilCompta.buildBeanModeReglement(o));
                    }
                }
            }
        }
    }

    private void loadCaissiers(YvsBaseCaisse y) {
        caissiers.clear();
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            caissiers = w.loadCaissiers(y);
        }
        if (y != null ? y.getId() > 0 : false) {
            if (pieceAvance.getCaissier() != null ? pieceAvance.getCaissier().getId() < 1 : true) {
                pieceAvance.setCaissier(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
            }
            if ((pieceAvance.getCaissier() != null ? pieceAvance.getCaissier().getId() < 1 : true) && caissiers.contains(currentUser.getUsers())) {
                pieceAvance.setCaissier(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
            }
        }
        update("chmp_caissier_reglement_pv");
    }

    public void chooseCaisses(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            // trouve les caisses parent d'une caisse données
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            long id = (long) ev.getNewValue();

            caissiers.clear();
            pieceAvance.setCaissier(new Users());
            if (service != null) {
                if (id == -1) {
                    service.loadAll(true, 0);
                } else if (id > 0) {
                    int idx = service.getCaisses().indexOf(new YvsBaseCaisse(id));
                    if (idx >= 0) {
                        YvsBaseCaisse y = service.getCaisses().get(idx);
                        pieceAvance.setCaisse(UtilCompta.buildBeanCaisse(y));
                        loadCaissiers(y);
                    }
                }
            }
            update("chmp_caissier_reglement_pv");
        }
    }

    public void chooseCaisse(PieceTresorerie piece) {
        if (piece.getCaisse() != null ? piece.getCaisse().getId() > 0 : false) {
            ManagedCaisses m = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (m != null) {
                int idx = m.getCaisses().indexOf(new YvsBaseCaisse(piece.getCaisse().getId()));
                if (idx > -1) {
                    YvsBaseCaisse o = m.getCaisses().get(idx);
                    piece.setCaisse(new Caisses(o.getId(), o.getIntitule()));
                }
            }
        }
    }

    public void chooseCaisses(YvsComptaAcompteFournisseur acompte) {
        if (acompte.getCaisse() != null ? acompte.getCaisse().getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (w != null) {
                int idx = w.getCaisses().indexOf(new YvsBaseCaisse(acompte.getCaisse().getId()));
                if (idx > -1) {
                    acompte.setCaisse(w.getCaisses().get(idx));
                }
            }
        }
    }

    public void chooseCaissePhase(YvsBaseCaisse caisse) {
        if (caisse != null ? caisse.getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (w != null) {
                int idx = w.getCaisses().indexOf(new YvsBaseCaisse(caisse.getId()));
                if (idx > -1) {
                    caisse = w.getCaisses().get(idx);
                }
            }
        }
    }

    public void chooseCaissePhaseVente() {
        if (currentPhaseVente.getCaisse() != null ? currentPhaseVente.getCaisse().getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (w != null) {
                int idx = w.getCaisses().indexOf(currentPhaseVente.getCaisse());
                if (idx > -1) {
                    currentPhaseVente.setCaisse(new YvsBaseCaisse(w.getCaisses().get(idx)));
                }
            }
        }
    }

    public void saveNew(PieceTresorerie bean) {
        if (bean != null) {
            if ((bean.getMode() != null) ? bean.getMode().getId() < 1 : true) {
                getErrorMessage("Vous devez préciser indiquer le moyen de paiement !");
                return;
            }
            if ((bean.getCaisse() != null) ? bean.getCaisse().getId() < 1 : true) {
                getErrorMessage("Vous devez préciser indiquer la caisse / banque !");
                return;
            }
            YvsComptaCaissePieceVente r = UtilCom.buildPieceVente(bean, currentUser);
            if (bean.getId() < 1) {
                r.setId(null);
                dao.save1(r);
            } else {
                dao.update(r);
            }
            YvsComptaMouvementCaisse y = (YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findByExterne", new String[]{"idExterne", "table"}, new Object[]{bean.getId(), selectPiece.getTableExterne()});
            int idx = mouvements.indexOf(y);
            if (idx > -1) {
                mouvements.set(idx, y);
            }
            succes();
        }
    }

    public void createNewPiece(boolean reset) {
        if (pieceAvance.getDocVente().getId() > 0) {
            List<YvsComptaCaissePieceVente> l = new ArrayList<>();
            if (!pieceAvance.isAcompte()) {
                pieceVente = createNewPieceCaisse(pieceAvance.getDocVente(), UtilCompta.buildTresoreriVente(pieceAvance, currentUser), true);
                if (pieceVente != null ? pieceVente.getId() > 0 : false) {
                    pieceAvance.setId(pieceVente.getId());
                    l.add(pieceVente);
                }
            } else {
                l.addAll(bindPieceAndAcompte());
            }
            for (YvsComptaCaissePieceVente pcv : l) {
                if (pcv != null) {
                    //si le Mode de règlement existe, récupérons sont libellé
                    if (pcv.getModel() != null) {
                        ManagedModeReglement serviceMdr = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                        if (serviceMdr != null) {
                            int idx = serviceMdr.getModes().indexOf(pcv.getModel());
                            if (idx >= 0) {
                                pcv.setModel(serviceMdr.getModes().get(idx));
                            }
                        }
                    }
                    if (pcv.getCaisse() != null) {
                        ManagedCaisses s = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                        if (s != null) {
                            int idx = s.getCaisses().indexOf(pcv.getCaisse());
                            if (idx > -1) {
                                pcv.setCaisse(s.getCaisses().get(idx));
                            }
                        }
                    }
                    int idx0 = reglements.indexOf(pcv);
                    int idx1 = pieceAvance.getDocVente().getReglements().indexOf(pcv);
                    if (idx1 > -1) {
                        pieceAvance.getDocVente().getReglements().set(idx1, pcv);
                    } else {
                        pieceAvance.getDocVente().getReglements().add(0, pcv);
                    }
                    if (idx0 > -1) {
                        reglements.set(idx0, pcv);
                    } else {
                        reglements.add(0, pcv);
                    }
                    update("table_reg_facture");
                    ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (service != null) {
                        service.setMontantTotalDoc(pieceAvance.getDocVente());
                        int i = service.getDocuments().indexOf(new YvsComDocVentes(pieceAvance.getDocVente().getId()));
                        if (i >= 0) {
                            int j = service.getDocuments().get(i).getReglements().indexOf(pcv);
                            if (j > -1) {
                                service.getDocuments().get(i).getReglements().set(j, pcv);
                            } else {
                                service.getDocuments().get(i).getReglements().add(0, pcv);
                            }
                            update("data_facture_vente_reg");
                        }
                    }
                    if (reset) {
                        pieceVente = newPieceCaiss();
                    }
                    succes();
                }
            }
        } else {
            if (pieceAvance.getClients().getId() > 0) {
                ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                if (w != null) {
                    AcompteClient compte = new AcompteClient();
                    compte.setClient(pieceAvance.getClients());
                    compte.setDateAcompte(pieceAvance.getDatePaiementPrevu());
                    compte.setMontant(pieceAvance.getMontant());
                    compte.setCaisse(pieceAvance.getCaisse());
                    compte.setMode(pieceAvance.getMode());
                    compte.setReferenceExterne(pieceAvance.getNumRefExterne());
                    YvsComptaAcompteClient y = w.saveAcompte(compte);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        pieceVente = newPieceCaiss();
                        pieceAvance = new PieceTresorerie();
                    }
                }
            } else {
                getErrorMessage("Vous devez precisez un client pour créer un acompre ou précisez une facture pour créer un reglement");
            }
        }
    }

    public void saveModelreglement() {

        ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        if (service.getDocVente().getId() > 0) {
            for (YvsComptaCaissePieceVente c : service.getDocVente().getReglements()) {
                c.setId(Long.valueOf(0));
                c = createNewPieceCaisse(service.getDocVente(), c, false);
                int idx = service.getDocuments().indexOf(new YvsComDocVentes(service.getDocVente().getId()));
                if (idx > -1) {
                    int index = service.getDocuments().get(idx).getReglements().indexOf(c);
                    if (index > -1) {
                        service.getDocuments().get(idx).getReglements().set(index, c);
                    } else {
                        service.getDocuments().get(idx).getReglements().add(c);
                    }
                }
            }
            service.getDocVente().setReglements(dao.loadNameQueries("YvsComptaCaissePieceVente.findByFacture", new String[]{"facture"}, new Object[]{new YvsComDocVentes(service.getDocVente().getId())}));
            if (service != null) {
                service.setMontantTotalDoc(service.getDocVente());
            }
            displayBtnSave = false;
        } else {
            getErrorMessage("Aucune facture n'a été selectionné !");
        }
    }

    private void recopieObject(YvsComptaCaissePieceVente pc) {
        pieceVente = new YvsComptaCaissePieceVente(pc);
        pieceVente.setMontantRest(pc.getMontantRest());
        pieceVente.setNew_(pc.isNew_());
        pieceVente.setAuthor(pc.getAuthor());
        pieceVente.setDateSave(pc.getDateSave());
        pieceVente.setDateUpdate(pc.getDateUpdate());
        pieceAvance = UtilCompta.buildBeanTresoreri(pc);
        loadCaissiers(pc.getCaisse());
        List<YvsComptaCaissePieceVente> list = new ArrayList<>();
        list.addAll(pc.getSousVentes());
        for (YvsComptaCaissePieceVente y : list) {
            for (YvsComptaCaissePieceVente p : y.getSousVentes()) {
                pieceAvance.getSousVentes().add(p);
            }
        }
        setMontantTotalDoc(pieceAvance.getDocVente());
    }

    public void selectOnePiece(SelectEvent ev) {
        onSelectObject((YvsComptaCaissePieceVente) ev.getObject());
        selectedPiece = (YvsComptaCaissePieceVente) ev.getObject();
    }

    public void onSelectDistant(YvsComptaCaissePieceVente y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Règlements ventes", "modCompta", "smenRegVente", true);
            }
        }
    }

    public void onSelectObject(YvsComptaCaissePieceVente y) {
        recopieObject(y);
        if (pieceVente.getModel() == null) {
            pieceVente.setModel(modeEspece());
        }
        if (pieceVente.getCaisse() == null) {
            pieceVente.setCaisse(new YvsBaseCaisse(-(long) 10));
        }
    }

    public void unselectOnePiece(UnselectEvent ev) {
        pieceVente = newPieceCaiss();
    }

    public void deletePieceCaisse(boolean all) {
        if (selectedPiece.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
            try {
                boolean comptabilise = dao.isComptabilise(selectedPiece.getId(), Constantes.SCR_CAISSE_VENTE);
                if (comptabilise) {
                    if (!all) {
                        getErrorMessage("Cette piece est déja comptabilisée");
                    }
                    return;
                }
                selectedPiece.setAuthor(currentUser);
                selectedPiece.setDateUpdate(new Date());
                dao.delete(selectedPiece);
                int idx0 = reglements.indexOf(selectedPiece);
                int idx = pieceAvance.getDocVente().getReglements().indexOf(selectedPiece);
                if (idx0 >= 0) {
                    reglements.remove(idx0);
                    update("table_regFV");
                }
                if (idx >= 0) {
                    pieceAvance.getDocVente().getReglements().remove(idx);
                }
                if (!all) {
                    succes();
                }
                selectedPiece = newPieceCaiss();
                resetFiche();
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cet élément !");
            }
        } else {
            getErrorMessage("Cette pièce est déjà encaissé !");
        }
    }

    public void openConfirmDelePC(YvsComptaCaissePieceVente pc) {
        if (pc.getParent() != null ? pc.getParent().getId() > 0 : false) {
            getErrorMessage("Vous ne pouvez pas supprimer cette pièce ici.. car elle est liée aux coût de retour de chèque");
            return;
        }
        selectedPiece = pc;
        if (pc.getModel() == null) {
            pc.setModel(new YvsBaseModeReglement(0l));
        }
        if (pc.getCaisse() == null) {
            pc.setCaisse(new YvsBaseCaisse((long) 0));
        }
        openDialog("dlgConfirmDeletePC");
    }

    public void openConfirmDeleCoutCheque(YvsComptaCaissePieceVente pc) {
        if (!autoriser("compta_del_reg_cout_cheque")) {
            openNotAcces();
            return;
        }
        try {
            if (pc.getSousVentes() != null ? !pc.getSousVentes().isEmpty() : false) {
                getErrorMessage("Vous devez d'abord supprimer le cout liée au reglement par le tiers");
                return;
            }
            dao.delete(pc);
            YvsComptaCaissePieceVente y = pc.getParent();
            if (y != null) {
                y.getSousVentes().remove(pc);
                int idx = pieceAvance.getSousVentes().indexOf(y);
                if (idx > -1) {
                    pieceAvance.getSousVentes().set(idx, y);
                }
            }
            succes();
            update("table_cout_phase_vente");
        } catch (Exception ex) {
            getException("Impossible de supprimer", ex);
            getErrorMessage("Action impossible");
        }
    }

    public void dissocierNotif() {
        dissocierNotif(selectedPiece);
    }

    public void dissocierNotif(YvsComptaCaissePieceVente pc) {
        if (pc != null ? pc.getId() > 0 : false) {
            if (pc.getNotifs() != null ? pc.getNotifs().getId() > 0 : false) {
                if (!pc.getComptabilise()) {
                    pc.setComptabilise(false);
                    dao.delete(pc.getNotifs());
                    pc.setNotifs(null);
                } else {
                    getErrorMessage("La pièce de règlement est déjà comptabilisé !");
                }
            }
        }
    }

    public void deleteAllPiece() {
        ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        List<YvsComptaCaissePieceVente> l = new ArrayList<>(service.getDocVente().getReglements());
        for (YvsComptaCaissePieceVente cp : l) {
            if ((cp.getId() != null)) {
                pieceVente = cp;
                deletePieceCaisse(true);
            }
        }
        succes();
    }

    public YvsComptaCaissePieceVente openConfirmPaiement() {
        this.pc = pieceVente;
        this.source = "F";
        this.emission = false;
        if (pc.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_VENTE)) {
            openDialog("dlgConfirmAnnulePiece");
            return pc;
        }
        return openConfirmPaiement(pieceVente, "F", false);
    }

    public YvsComptaCaissePieceVente openConfirmPaiement(YvsComptaCaissePieceVente pc, String source, boolean emission) {
        return openConfirmPaiement(pc, source, true, true, emission);
    }

    YvsComptaCaissePieceVente pc;
    boolean emission;

    public YvsComptaCaissePieceVente openConfirmrPaiement() {
        return openConfirmPaiement(pc, source, true, true, emission);
    }

    public YvsComptaCaissePieceVente openConfirmrPaiement(YvsComptaCaissePieceVente pc, String source, boolean emission) {
        this.pc = pc;
        this.source = source;
        this.emission = emission;
        if (pc.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_VENTE)) {
            openDialog("dlgConfirmAnnulePiece");
            return pc;
        }
        return openConfirmPaiement(pc, source, true, true, emission);
    }

    public YvsComptaCaissePieceVente openConfirmPaiement(YvsComptaCaissePieceVente pc, String source, boolean open, boolean msg, boolean emission) {
        return openConfirmPaiement(pc, source, open, msg, emission, true);
    }

    public YvsComptaCaissePieceVente openConfirmPaiement(YvsComptaCaissePieceVente pc, String source, boolean open, boolean msg, boolean emission, boolean load) {
        this.source = source;
        if (currentParam == null) {
            currentParam = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        if (load) {
            pc = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{pc.getId()});
        }
        if (pc != null ? pc.getId() > 0 : false) {
            String modeNotif = Constantes.MODE_PAIEMENT_ESPECE;
            if (!pc.isNew_()) { //lorsque la pièce est généré, elle ne peut pas être une notification de paiement
                if (pc.getNotifs() != null ? pc.getNotifs().getId() > 0 : false) {
                    if (!autoriser("p_caiss_payer_acompte")) {
                        openNotAcces();
                        return pc;
                    }
                    modeNotif = pc.getNotifs().getAcompte().getModel().getTypeReglement();
                    //s'il s'agit d'une notification de règlement par un acompte et que l'acompte en lui même n'est pas encore payé, on quitte
                    if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                        if (!pc.getNotifs().getAcompte().getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                            getErrorMessage("L'acompte lié à ce reglement n'est pas encore encaissé");
                            return pc;
                        }
                        double montant = pc.getMontant();
                        for (YvsComptaNotifReglementVente n : pc.getNotifs().getAcompte().getNotifs()) {
                            if (n.getPieceVente().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                montant += n.getPieceVente().getMontant();
                            }
                        }
                        if (montant > pc.getNotifs().getAcompte().getMontant()) {
                            getErrorMessage("Vous ne pouvez pas valider ce montant.. car la somme des pièces excedera le montant de l'acompte");
                            return pc;
                        }
                    }
                }
            }
            if (currentParam != null ? !currentParam.getPaieWithoutValide() ? (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER && !pc.getVente().getStatut().equals(Constantes.ETAT_VALIDE)) : false : false) {
                getErrorMessage("La facture doit être au préalable validé");
                return pc;
            }
            selectedPiece = pc;
            if (pc.getModel() != null ? pc.getModel().getId() > 0 : false) {
                switch (pc.getModel().getTypeReglement()) {
                    case Constantes.MODE_PAIEMENT_SALAIRE:
                        //si on veux valider le paiement
                        if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                            if (pc.getVente().getStatut().equals(Constantes.ETAT_VALIDE)) {
                                //vérifie le lien entre le client et l'employé     
                                if (!autoriser("encais_piece_salaire")) {
                                    openNotAcces();
                                    return pc;
                                }
                                YvsGrhEmployes emp = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCompteTiers", new String[]{"tiers", "societe"},
                                        new Object[]{pc.getVente().getClient().getTiers(), currentAgence.getSociete()});
                                if (emp != null) {
                                    //ouvre la boite de dialogue pour choisir le type de retenu
                                    ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
                                    if (service != null) {
//                                        service.getElementAdd().setContrat(UtilGrh.buildBeanContratEmploye(emp.getContrat()));
                                        service.getElementAdd().setContrat(new ContratEmploye(emp.getContrat().getId(), emp.getContrat().getReferenceContrat()));
                                        //charge les retenue déjà rattaché à cette facture
                                        service.getElementAdd().setListPrelevement(dao.loadNameQueries("YvsGrhDetailPrelevementEmps.findByDocVente", new String[]{"docVente"}, new Object[]{pc.getVente()}));
                                        service.loadAllTypeElementAddActif();
                                        service.getElementAdd().getTypeElt().setId(0);
                                        service.getElementAdd().getPlan().setId(0);
                                        pieceVente = pc;
                                        if (open) {
                                            openDialog("dlgChoixTypeRet");
                                        }
                                        update("zone_choix_retenu_vente");
                                        update("zone_choix_retenu_vente_reg");
                                    }
                                } else if (msg) {
                                    getErrorMessage("Impossible de trouver l'employé représentant  ce client", "Veuillez revoir votre paramétrage");
                                }
                            } else if (msg) {
                                getErrorMessage("Votre facture dois être validé pour utiliser ce mode de règlement !");
                            }
                        } else if (open) {
                            //on veux annuler le paiement par salaire
                            openDialog("dlgCancelPcv2");
                        }
                        break;
                    case Constantes.MODE_PAIEMENT_BANQUE:
                        //si on veux valider le paiement
                        if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                            if (pc.getPhasesReglement().isEmpty()) {
                                if (!autoriser("encais_piece_cheque")) {
                                    openNotAcces();
                                    return pc;
                                }
                                // Si le paiement se fait à partir d'une notification, il faut que les montants soient coherent
                                if (pc.getNotifs() != null) {
                                    Double reste = AYvsComptaAcompteClient.findResteForAcompte(pc.getNotifs().getAcompte(), dao);
                                    // (reste != null ? reste : 0);
                                    if (pc.getMontant() > (reste != null ? reste : 0)) {
                                        getErrorMessage("Le montant restant de l'acompte " + pc.getNotifs().getAcompte().getNumRefrence() + " Ne permet pas de régler la pièce de caisse !");
                                        return pc;
                                    }
                                }
                                if (!modeNotif.equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{pc.getModel(), emission});
                                    //lié les phases à la pièce de règlements
                                    YvsComptaPhasePiece pp;
                                    if (pc.getPhasesReglement() == null) {
                                        pc.setPhasesReglement(new ArrayList<YvsComptaPhasePiece>());
                                    }
                                    for (YvsComptaPhaseReglement ph : phases) {
                                        pp = new YvsComptaPhasePiece(null);
                                        pp.setAuthor(currentUser);
                                        pp.setPhaseOk(pc.getNotifs() != null);// Si la pièce est lié à une Acompte, On valide immédiatements les phases
                                        pp.setPhaseReg(ph);
                                        pp.setPieceVente(pc);
                                        pp.setCaisse(pc.getCaisse());
                                        pp.setDateSave(new Date());
                                        pp.setDateUpdate(new Date());
                                        pp = (YvsComptaPhasePiece) dao.save1(pp);
                                        pc.getPhasesReglement().add(pp);
                                    }
                                }
                                ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                                if (service != null) {
                                    int idx = service.getDocVente().getReglements().indexOf(pc);
                                    if (idx >= 0) {
                                        service.getDocVente().getReglements().set(idx, pc);
                                        update("tabview_facture_vente:data_mensualite_facture_vente");
                                    }
                                    pc.getVente().setStatutRegle(Constantes.ETAT_ENCOURS);
                                    pc.getVente().setDateUpdate(new Date());
                                    pc.getVente().setAuthor(currentUser);
                                    dao.update(pc.getVente());
                                    idx = service.getDocuments().indexOf(pc.getVente());
                                    if (idx >= 0) {
                                        service.getDocuments().set(idx, pc.getVente());
                                        update("data_facture_vente");
                                    }
                                    if (service.getDocVente().getId() > 0 ? pc.getVente().getId().equals(service.getDocVente().getId()) : false) {
                                        service.getDocVente().setStatutRegle(Constantes.ETAT_ENCOURS);
                                    }
                                }
                                boolean succes = true;
                                if (pc.getNotifs() != null) {
                                    Double reste = AYvsComptaAcompteClient.findResteForAcompte(pc.getNotifs().getAcompte(), dao);
                                    if (pc.getMontant() <= (reste != null ? reste : 0)) {
                                        pieceVente = pc;
                                        reglerPieceTresorerie(true);
                                        succes = false;
                                    }
                                }
                                if (succes) {
                                    succes();
                                }
                            } else {
                                if (pc.getStatutPiece() == Constantes.STATUT_DOC_ANNULE) {
                                    //si le 
                                    ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                                    if (service != null) {
                                        service.equilibre(pc.getVente());
                                        if (!pc.getVente().getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                                            pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                                            pc.setDateUpdate(new Date());
                                            pc.setAuthor(currentUser);
                                            dao.update(pc);
                                        } else {
                                            getErrorMessage("La facture est déjà réglé !");
                                        }
                                    }
                                }
                                if (msg) {
                                    getWarningMessage("Les phases de ce règlement ont déjà été générés !");
                                }
                            }

                        } else if (open) {
                            //annulser le paiement de la pièce par banque
                            openDialog("dlgCancelPcv1");
                        }
                        break;
                    case Constantes.MODE_PAIEMENT_COMPENSATION:
                        if (!autoriser("encais_piece_comp")) {
                            openNotAcces();
                            return pc;
                        }
                        ManagedCompensation w = (ManagedCompensation) giveManagedBean(ManagedCompensation.class);
                        if (w != null) {
                            if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                                pc.setDatePaiement(pc.getDatePaimentPrevu());
                                YvsComptaCaissePieceVente y = w.payerOrAnnuler(pc, msg);
                                if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                                    ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                                    if (service != null) {
                                        int idx = service.getDocVente().getReglements().indexOf(y);
                                        if (idx >= 0) {
                                            service.getDocVente().getReglements().set(idx, y);
                                            update("tabview_facture_vente:data_mensualite_facture_vente");
                                        }
                                        Map<String, String> statuts = dao.getEquilibreVente(y.getVente().getId());
                                        if (statuts != null) {
                                            y.getVente().setStatutLivre(statuts.get("statut_livre"));
                                            y.getVente().setStatutRegle(statuts.get("statut_regle"));
                                        }
                                        service.resetView(y.getVente());
                                    }
                                    int idx = reglements.indexOf(y);
                                    if (idx >= 0) {
                                        reglements.set(idx, y);
                                    }
                                    return y;
                                }
                            } else if (open) {
                                //on veux annuler le paiement par compensation
                                openDialog("dlgCancelPcv3");
                            }
                        }
                        break;
                    default:
                        if (!autoriser("encais_piece_espece")) {
                            openNotAcces();
                            return pc;
                        }
                        if (pc.getModel() == null) {
                            pc.setModel(modeEspece());
                        }
                        if (pc.getCaisse() == null) {
                            pc.setCaisse(new YvsBaseCaisse(0L));
                        }
                        if (!needConfirmation) {
                            if (open) {
                                openDialog("dlgConfirmPaye");
                            }
                        } else {
                            pieceVente = pc;
                            reglerPieceTresorerie(true);
                        }
                        break;
                }
            } else {
                if (!autoriser("encais_piece_espece")) {
                    openNotAcces();
                    return pc;
                }
                if (pc.getModel() == null) {
                    pc.setModel(modeEspece());
                }
                if (!needConfirmation) {
                    if (open) {
                        openDialog("dlgConfirmPaye");
                    }
                } else {
                    pieceVente = pc;
                    reglerPieceTresorerie(true);
                }
            }
        }
        update("data_mensualite_facture_vente");
        if (!needConfirmation) {
            pieceVente = pc;
        }
        return pc;
    }

    public boolean controlToPaiement(YvsComptaCaissePieceVente pc, String source, boolean open, boolean msg, boolean emission, boolean load) {
        try {
            if (pc != null ? pc.getId() > 0 : false) {
                if (currentParam == null) {
                    currentParam = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
                }
                String modeNotif = Constantes.MODE_PAIEMENT_ESPECE;
                if (pc.getNotifs() != null ? pc.getNotifs().getId() > 0 : false) {
                    if (!autoriser("p_caiss_payer_acompte")) {
                        openNotAcces();
                        return false;
                    }
                    modeNotif = pc.getNotifs().getAcompte().getModel().getTypeReglement();
                    if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                        if (!pc.getNotifs().getAcompte().getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                            getErrorMessage("L'acompte lié à ce reglement n'est pas encore encaissé");
                            return false;
                        }
                        double montant = pc.getMontant();
                        for (YvsComptaNotifReglementVente n : pc.getNotifs().getAcompte().getNotifs()) {
                            if (n.getPieceVente().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                montant += n.getPieceVente().getMontant();
                            }
                        }
                        if (montant > pc.getNotifs().getAcompte().getMontant()) {
                            getErrorMessage("Vous ne pouvez pas valider ce montant.. car la somme des pièces excedera le montant de l'acompte");
                            return false;
                        }
                    }
                }
                if (currentParam != null ? !currentParam.getPaieWithoutValide() ? !pc.getVente().getStatut().equals(Constantes.ETAT_VALIDE) : false : false) {
                    getErrorMessage("La facture doit être au préalable validée");
                    return false;
                }
                if (pc.getModel() != null ? pc.getModel().getId() > 0 : false) {
                    switch (pc.getModel().getTypeReglement()) {
                        case Constantes.MODE_PAIEMENT_SALAIRE:
                            //si on veux valider le paiement
                            if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                                if (!pc.getVente().getStatut().equals(Constantes.ETAT_VALIDE)) {
                                    if (msg) {
                                        getErrorMessage("Votre facture dois être validé pour utiliser ce mode de règlement !");
                                    }
                                    return false;
                                }
                                //vérifie le lien entre le client et l'employé     
                                if (!autoriser("encais_piece_salaire")) {
                                    openNotAcces();
                                    return false;
                                }
                                YvsGrhEmployes emp = (YvsGrhEmployes) dao.loadOneByNameQueries("YvsGrhEmployes.findByCompteTiers", new String[]{"tiers", "societe"},
                                        new Object[]{pc.getVente().getClient().getTiers(), currentAgence.getSociete()});
                                if (emp != null ? emp.getId() < 1 : true) {
                                    if (msg) {
                                        getErrorMessage("Impossible de trouver l'employé représentant  ce client", "Veuillez revoir votre paramétrage");
                                    }
                                    return false;
                                }
                            }
                            break;
                        case Constantes.MODE_PAIEMENT_BANQUE:
                            //si on veux valider le paiement
                            if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                                if (pc.getPhasesReglement().isEmpty()) {
                                    if (!autoriser("encais_piece_cheque")) {
                                        openNotAcces();
                                        return false;
                                    }
                                    // Si le paiement se fait à partir d'une notification, il faut que les montants soient coherent
                                    if (pc.getNotifs() != null) {
                                        Double reste = AYvsComptaAcompteClient.findResteForAcompte(pc.getNotifs().getAcompte(), dao);
                                        // (reste != null ? reste : 0);
                                        if (pc.getMontant() > (reste != null ? reste : 0)) {
                                            getErrorMessage("Le montant restant de l'acompte " + pc.getNotifs().getAcompte().getNumRefrence() + " Ne permet pas de régler la pièce de caisse !");
                                            return false;
                                        }
                                    }
                                } else {
                                    if (pc.getStatutPiece() == Constantes.STATUT_DOC_ANNULE) {
                                        //si le 
                                        ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                                        if (service != null) {
                                            service.equilibre(pc.getVente());
                                            if (pc.getVente().getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                                                getErrorMessage("La facture est déjà réglé !");
                                            }
                                        }
                                    }
                                    if (msg) {
                                        getWarningMessage("Les phases de ce règlement ont déjà été générés !");
                                    }
                                }

                            }
                            break;
                        case Constantes.MODE_PAIEMENT_COMPENSATION:
                            if (!autoriser("encais_piece_comp")) {
                                openNotAcces();
                                return false;
                            }
                            break;
                        default:
                            if (!autoriser("encais_piece_espece")) {
                                openNotAcces();
                                return false;
                            }
                            break;
                    }
                } else {
                    if (!autoriser("encais_piece_espece")) {
                        openNotAcces();
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception ex) {

        }
        return false;
    }

    public void verouillerPieceReglementVente() {
        if (selectedPiece != null) {
            verouillerPieceReglementVente(selectedPiece);
        }
    }

    public void verouillerPieceReglementVente(YvsComptaCaissePieceVente piece) {
        if (piece != null) {
            try {
                piece.setAuthor(currentUser);
                piece.setDateUpdate(new Date());
                piece.setVerouille(!piece.getVerouille());
                dao.update(piece);
                update("data_mensualite_facture_vente");
            } catch (Exception ex) {
                getErrorMessage("Impossible de terminer cette opération !");
                getException("Erreur modif pièce !", ex);
            }
        }
    }

    public void suspendreReglementVente() {
        if (selectedPiece != null) {
            try {
                if (selectedPiece.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    if (!autoriser("compta_cancel_piece_valide")) {
                        openNotAcces();
                        return;
                    }
                }
                if (selectedPiece.getVerouille()) {
                    getErrorMessage("La pièce de règlement est verouillée !");
                    return;
                }
                selectedPiece.setAuthor(currentUser);
                selectedPiece.setDateUpdate(new Date());
                selectedPiece.setStatutPiece(Constantes.STATUT_DOC_ANNULE);
                dao.update(selectedPiece);
                Map<String, String> statuts = dao.getEquilibreVente(selectedPiece.getVente().getId());
                if (statuts != null) {
                    selectedPiece.getVente().setStatutLivre(statuts.get("statut_livre"));
                    selectedPiece.getVente().setStatutRegle(statuts.get("statut_regle"));
                }
                update("data_mensualite_facture_vente");
            } catch (Exception ex) {
                getErrorMessage("Impossible de terminer cette opération !");
            }
        }
    }

    public void cancelStatutPayeOfPCV() {
        if (pieceVente.getRetenue() != null) {
            for (YvsGrhDetailPrelevementEmps d : pieceVente.getRetenue().getRetenues()) {
                if (d.getStatutReglement() == Constantes.STATUT_DOC_PAYER) {
                    getErrorMessage("des traites de cette retenue ont déjà été payés. veuillez vous référer aux ressources humaines !");
                    return;
                }
            }
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (dao.isComptabilise(pieceVente.getId(), Constantes.SCR_CAISSE_VENTE)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return;
                }
                if (w != null) {
                    if (w.unComptabiliserCaisseVente(pieceVente, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                }
            }
            try {
                pieceVente.getRetenue().setAuthor(currentUser);
                dao.delete(pieceVente.getRetenue());
                pieceVente.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                pieceVente.setAuthor(currentUser);
                pieceVente.setDatePaiement(null);
                pieceVente.setValideBy(null);
                dao.update(pieceVente);
                Map<String, String> statuts = dao.getEquilibreVente(pieceVente.getVente().getId());
                if (statuts != null) {
                    pieceVente.getVente().setStatutLivre(statuts.get("statut_livre"));
                    pieceVente.getVente().setStatutRegle(statuts.get("statut_regle"));
                }
            } catch (Exception ex) {
                getErrorMessage("Impossible de terminer cette opération !");
            }
        }
    }

    public void cancelStatutPayeOfPCVForBanque() {
        try {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (dao.isComptabilise(pieceVente.getId(), Constantes.SCR_CAISSE_VENTE)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return;
                }
                if (w != null ? (pieceVente.getPhasesReglement() != null ? pieceVente.getPhasesReglement().isEmpty() : true) : false) {
                    if (w.unComptabiliserCaisseVente(pieceVente, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                }
            }
            for (YvsComptaPhasePiece pp : pieceVente.getPhasesReglement()) {
                if (w != null) {
                    if (w.unComptabiliserPhaseCaisseVente(pp, false, true)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                }
                dao.delete(pp);
            }
            pieceVente.getPhasesReglement().clear();
            pieceVente.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            pieceVente.setAuthor(currentUser);
            pieceVente.setDatePaiement(null);
            pieceVente.setValideBy(null);
            dao.update(pieceVente);
            ManagedFactureVenteV2 ws = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
            if (ws != null) {
                int idx = ws.getDocVente().getReglements().indexOf(pieceVente);
                if (idx >= 0) {
                    ws.getDocVente().getReglements().set(idx, pieceVente);
                    update("tabview_facture_vente:data_mensualite_facture_vente");
                }
                pieceVente.getVente().setStatutRegle(Constantes.ETAT_ATTENTE);
                pieceVente.getVente().setDateUpdate(new Date());
                pieceVente.getVente().setAuthor(currentUser);
                dao.update(pieceVente.getVente());
                ws.resetView(pieceVente.getVente());
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de terminer cette opération !");
        }
    }

    public void cancelStatutPayeOfPCVBySalaire() {
        if (pieceVente.getRetenue() != null) {
            ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
            if (service != null) {
                if (autoriser("compta_del_reg_retenue")) {
                    try {
                        boolean delete = true;
//                        for (YvsGrhElementAdditionel pp : pieceVente.getRetenues()) {
//                            if (!service.canDeleteRetenue(pp)) {
//                                delete = false;
//                                break;
//                            }
//                        }
                        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                        if (dao.isComptabilise(pieceVente.getId(), Constantes.SCR_CAISSE_VENTE)) {
                            if (!autoriser("compta_od_annul_comptabilite")) {
                                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                                return;
                            }
                            if (w != null) {
                                if (w.unComptabiliserCaisseVente(pieceVente, false)) {
                                    getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                                    return;
                                }
                            }
                        }
                        if (!service.canDeleteRetenue(pieceVente.getRetenue())) {
                            service.deleteOneRetenueForFacture(pieceVente);
                            pieceVente.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            pieceVente.setAuthor(currentUser);
                            pieceVente.setDatePaiement(null);
                            pieceVente.setValideBy(null);
                            dao.update(pieceVente);
                            ManagedFactureVenteV2 service_ = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                            if (service_ != null) {
                                int idx = service_.getDocVente().getReglements().indexOf(pieceVente);
                                if (idx >= 0) {
                                    service_.getDocVente().getReglements().set(idx, pieceVente);
                                    update("tabview_facture_vente:data_mensualite_facture_vente");
                                }
                            }
                        }
                    } catch (Exception ex) {
                        getErrorMessage("Impossible de terminer cette opération !");
                    }
                } else {
                    openNotAcces();
                }
            } else {
                getErrorMessage("Aucune retenue selectionné !");
            }
        }
    }

    public void cancelStatutPayeOfPCVByCompensation1() {
        ManagedCompensation w = (ManagedCompensation) giveManagedBean(ManagedCompensation.class);
        if (w != null) {
            YvsComptaCaissePieceVente y = w.payerOrAnnuler(selectedPiece, true);
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                int idx = reglements.indexOf(y);
                if (idx >= 0) {
                    reglements.set(idx, y);
                    update("tabview_facture_vente:data_mensualite_facture_vente");
                }
                Map<String, String> statuts = dao.getEquilibreVente(y.getVente().getId());
                if (statuts != null) {
                    y.getVente().setStatutLivre(statuts.get("statut_livre"));
                    y.getVente().setStatutRegle(statuts.get("statut_regle"));
                }
                pieceAvance.setStatutPiece(y.getStatutPiece());
                pieceAvance.setCompensations(y.getCompensations());
            }
        }
    }

    // Appeler à partir de la vue des factures de vente
    public void cancelStatutPayeOfPCVByCompensation() {
        ManagedCompensation w = (ManagedCompensation) giveManagedBean(ManagedCompensation.class);
        if (w != null) {
            YvsComptaCaissePieceVente y = w.payerOrAnnuler(selectedPiece, true);
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                if (service != null) {
                    int idx = service.getDocVente().getReglements().indexOf(y);
                    if (idx >= 0) {
                        service.getDocVente().getReglements().set(idx, y);
                        update("tabview_facture_vente:data_mensualite_facture_vente");
                    }
                    Map<String, String> statuts = dao.getEquilibreVente(y.getVente().getId());
                    if (statuts != null) {
                        y.getVente().setStatutLivre(statuts.get("statut_livre"));
                        y.getVente().setStatutRegle(statuts.get("statut_regle"));
                    }
                    service.resetView(y.getVente());
                }
            }
        }
    }

    public void valideReglementFacture() {
        valideReglementFacture(pieceVente, true);
    }

    public void valideReglementFacture(YvsComptaCaissePieceVente pieceVente, boolean msg) {
        try {
            ManagedRetenue service = (ManagedRetenue) giveManagedBean(ManagedRetenue.class);
            if (service != null) {
                if ((service.getElementAdd().getPlan().getId()) <= 0 || service.getElementAdd().getTypeElt().getId() <= 0) {
                    getErrorMessage("Formulaire incorrecte !");
                    return;
                }
                //enretistre la retenu
                YvsGrhElementAdditionel ela = service.buildElementAdditionel(service.getElementAdd());
                if (ela != null) {
                    ela.setId(null);
                    ela.setPiceReglement(pieceVente);
                    ela.setPlanifier(true);
                    ela.setContrat(UtilGrh.buildBeanContratEmploye(service.getElementAdd().getContrat()));
                    ela.setPlanPrelevement(new YvsGrhPlanPrelevement(service.getElementAdd().getPlan().getId()));
                    ela.setTypeElement(new YvsGrhTypeElementAdditionel(service.getElementAdd().getTypeElt().getId()));
                    ela.setAuthor(currentUser);
                    ela.setPermanent(false);
                    ela.setDateUpdate(new Date());
                    ela = (YvsGrhElementAdditionel) dao.save1(ela);
                    for (YvsGrhDetailPrelevementEmps d : service.getElementAdd().getListPrelevement()) {
                        d.setAuthor(currentUser);
                        d.setId(null);
                        d.setRetenue(ela);
                        d.setRetenuFixe(false);
                        d.setDateSave(new Date());
                        d.setDateUpdate(new Date());
                        d = (YvsGrhDetailPrelevementEmps) dao.save1(d);
                    }
                    //marque la pièce réglé
                    pieceVente.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                    if (pieceVente.getCaissier() != null ? pieceVente.getCaissier().getId() < 1 : true) {
                        pieceVente.setCaissier(currentUser.getUsers());
                    }
                    pieceVente.setDatePaiement(new Date());
                    pieceVente.setCaisse(null);
                    dao.update(pieceVente);
                    if (pieceVente.getVente() != null) {
                        Map<String, String> statuts = dao.getEquilibreVente(pieceVente.getVente().getId());
                        if (statuts != null) {
                            pieceVente.getVente().setStatutLivre(statuts.get("statut_livre"));
                            pieceVente.getVente().setStatutRegle(statuts.get("statut_regle"));
                        }
                        ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                        if (w != null) {
                            w.resetView(pieceVente.getVente());
                            update("data_facture_vente_reg");
                            w.setMontantTotalDoc(w.getDocVente());
                            update("zone_form_regFV");
                            int idx = w.getDocVente().getReglements().indexOf(pieceVente);
                            if (idx >= 0) {
                                w.getDocVente().getReglements().set(idx, pieceVente);
                                update("tabview_facture_vente:data_mensualite_facture_vente");
                            }
                        }
//                        ManagedFactureVenteV2 w1 = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
//                        if (w1 != null) {
//                            w1.resetView((YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{pieceVente.getVente().getId()}));
//                            update("data_facture_vente_reg");
//                            w1.setMontantTotalDoc(w1.getDocVente());
//                            update("zone_form_regFV");
//                            int idx = w1.getDocVente().getReglements().indexOf(pieceVente);
//                            if (idx >= 0) {
//                                w1.getDocVente().getReglements().set(idx, pieceVente);
//                                update("tabview_facture_vente:data_mensualite_facture_vente");
//                            }
//                        }
                    }
                    if (msg) {
                        succes();
                    }
                } else {
                    getErrorMessage("Retenue non enregistré !");
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible!!!");
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void toogleSuspendPieceCaisse(YvsComptaCaissePieceVente pc) {
        if (pc != null) {
            //si c'est une suspension, on controle juste le droit
            if (pc.getId() > 0) {
                pc.setDateUpdate(new Date());
                pc.setAuthor(currentUser);
                if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) { //la pièce ne doit pas être déjà payé
                    switch (pc.getStatutPiece()) {
                        case Constantes.STATUT_DOC_ANNULE:
                        case Constantes.STATUT_DOC_SUSPENDU: //cas d'une remise en état d'attente (on controle la cohérence des montantF)                        
                            pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            dao.update(pc);
                            break;
                        case Constantes.STATUT_DOC_ATTENTE:
                        case Constantes.STATUT_DOC_EDITABLE:
                            pc.setStatutPiece(Constantes.STATUT_DOC_SUSPENDU);
                            dao.update(pc);
                            break;
                    }
                } else {
                    getErrorMessage("Impossible de modifier cette pièce! ", "La confirmation du règlement a déjà été enregistré");
                }
            } else {
                getErrorMessage("La pièce n'est pas enregistré !");
            }
        }
    }
    private String source;

    public boolean reglerPieceTresorerie(boolean avance) {
        return reglerPieceTresorerie(avance, true);
    }

    public boolean reglerPieceTresorerie(boolean avance, boolean msg) {
        pieceVente.setDatePaiement(pieceVente.getDatePaimentPrevu());
        return reglerPieceTresorerie(pieceVente, source, msg);
    }

    private int giveAction(YvsComptaCaissePieceVente pc) {
        if (pc != null) {
            if (pc.getStatutPiece() == Constantes.STATUT_DOC_ATTENTE) {
                return 2;   //valider
            } else if (pc.getStatutPiece() == Constantes.STATUT_DOC_VALIDE) {
                return 2;//Encaisser
            } else {
                return 3; //En attente
            }
        }
        return 0;
    }

    //Le paramètre source permet de préciser la source de l'action(Tableau des acomptes clients ou des avance sur facture)
//    private boolean reglerPieceTresorerie(YvsComptaCaissePieceVente pc, String source) {
//        return reglerPieceTresorerie(pc, source, true);
//    }
    public boolean reglerPieceTresorerie(YvsComptaCaissePieceVente pc, String source, boolean msg) {
        ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        if (service != null) {
//            return reglerPieceTresorerie(selectedDoc, pc, source, msg);-
            return reglerPieceTresorerie(service.getDocVente(), pc, source, msg);
        }
        return false;
    }

    public boolean reglerPieceTresorerie(DocVente vente, YvsComptaCaissePieceVente pc, String source, boolean msg) {
        if (pc != null ? pc.getId() > 0 : false) {//si c'est une suspension, on controle juste le droit
            pc.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{pc.getCaisse().getId()}));
            if (controleAccesCaisse(pc.getCaisse(), true)) {
                if (pc.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU) { //la pièce ne doit pas être déjà payé
                    //la pièce de caisse doit avoir une caisse
                    int action = giveAction(pc);
                    pc.setAuthor(currentUser);
                    pc.setNew_(true);
                    boolean update = false;
                    if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                        //Vérifie s'il s'agit d'une compensation de la cohérence des montants
                        if (pc.getNotifs() != null) {
                            if (action != 3) {
                                if (pc.getNotifs().getAcompte().getDatePaiement().after(pc.getDatePaiement())) {
                                    pc.setDatePaiement(pc.getNotifs().getAcompte().getDatePaiement());
                                }
                            }
                            Double reste = AYvsComptaAcompteClient.findResteForAcompte(pc.getNotifs().getAcompte(), dao);
                            // (reste != null ? reste : 0);
                            if ((reste != null ? reste : 0) < pc.getMontant()) {
                                if (msg) {
                                    getErrorMessage("Le montant de l'acompte ne permet pas le règlement totale de cette pièce !");
                                    return false;
                                }
                            }
                        }
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    boolean regler;
                    switch (action) {
                        case 1://valider la pièce de vente
//                            pc.setDatePaiement(null);
//                            pc.setStatutPiece(Constantes.STATUT_DOC_VALIDE);
//                            pc.setValideBy(currentUser.getUsers());
//                            pc.setDateValide(new Date());
//                            update = true;
                            break;
                        case 3://Mettre la pièce en attente    
                            if (!verifyCancelPieceCaisse(pc.getDatePaiement())) {
                                return false;
                            }
                            if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_VENTE)) {
                                if (!autoriser("compta_od_annul_comptabilite")) {
                                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                                    return false;
                                }
                                if (w != null) {
                                    if (!w.unComptabiliserCaisseVente(pc, false)) {
                                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                                        return false;
                                    }
                                }
                            }
                            pc.setDatePaiement(null);
                            pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            pc.setValideBy(null);
                            pc.setDateValide(null);
                            update = true;
                            regler = false;
                            break;
                        case 2://Encaisser la pièce
                            if ((pc.getCaisse() != null) ? (pc.getCaisse().getId() > 0 && pc.getVente() != null) : false) {
//                                YvsComDocVentes d = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{pc.getVente().getId()});
                                if ("F".equals(source)) {
//                                    if ((vente != null && pc.getVente() != null) ? vente.getId() != pc.getVente().getId() : true) {
//                                        vente = UtilCom.buildBeanDocVente(d);
//                                        setMontantTotalDoc(vente);
//                                    }
                                }
                                if (controleAddPiece(("F".equals(source)) ? vente : null, pc)) {
                                    if (pc.getCaissier() != null ? pc.getCaissier().getId() < 1 : true) {
                                        pc.setCaissier(currentUser.getUsers());
                                    }
                                    pc.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                    pc.setValideBy(currentUser.getUsers());
                                    pc.setDateValide(new Date());
                                    pc.setDatePaiement(pc.getDatePaimentPrevu());
                                    update = true;
                                } else {
                                    update = false;
                                }
                            } else if (msg) {
                                if (pc.getVente() == null) {
                                    getErrorMessage("Cette pièce n'est rattachée à aucune facture");
                                } else {
                                    getErrorMessage("Aucune caisse n'a été trouvé pour ce règlement !");
                                }
                            }
                            regler = true;
                            break;
                        default:
                            break;
                    }
                    if (update) {
                        if (pieceReglement != null ? pc.getId().equals(pieceReglement.getId()) : false) {
                            pieceReglement.setStatutPiece(pc.getStatutPiece());
                        }
                        pc.setDateUpdate(new Date());
                        pc.setAuthor(currentUser);
                        dao.update(pc);
                        if (pc.getVente() != null ? pc.getVente().getId() > 0 : false) {
                            if (pc.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                if (w != null ? dao.isComptabilise(pc.getVente().getId(), Constantes.SCR_VENTE) : false) {
                                    w.comptabiliserCaisseVente(pc, false, false);
                                }
                            }
                            ManagedFactureVenteV2 service = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                            if (service != null) {
                                String query = "SELECT d.statut_regle FROM yvs_com_doc_ventes d WHERE d.id=? ";
                                String statut = (String) dao.loadObjectBySqlQuery(query, new Options[]{new Options(vente.getId(), 1)});
                                vente.setStatutRegle(statut);
                                update("grp_btn_etat_facture_vente");
                                int idx = service.getDocuments().indexOf(new YvsComDocVentes(vente.getId()));
                                if (idx >= 0) {
                                    service.getDocuments().get(idx).setStatutRegle(statut);
                                    YvsComDocVentes entity = service.getDocuments().get(idx);
                                    service.majMontantTotalDocAfterReg(entity);
                                    service.getDocVente().setMontantAvance(entity.getMontantAvance());
//                                    service.getDocVente().setMontantResteApayer(service.getDocuments().get(idx).getMontantResteApayer());
                                    update("data_facture_vente");
                                    update("data_facture_vente_reg");
                                }
                                idx = vente.getReglements().indexOf(pc);
                                if (idx >= 0) {
                                    vente.getReglements().set(idx, pc);
                                    update("chp_fv_net_a_payer");
                                    update("tabview_facture_vente:data_mensualite_facture_vente");
                                }
                                update("zone_form_regFV");
                            }
                        }
                        if (vente != null ? vente.getReglements() != null : false) {
                            int idx = vente.getReglements().indexOf(pc);
                            if (idx >= 0) {
                                vente.getReglements().set(idx, pc);
                                update("table_reg_facture");
                            }
                        }
                        if (reglements != null) {
                            int idx = reglements.indexOf(pc);
                            if (idx >= 0) {
                                reglements.set(idx, pc);
                                update("table_regFV");
                            }
                        }
                        if (pieceAvance != null ? pc.getId().equals(pieceAvance.getId()) : false) {
                            pieceAvance.setStatutPiece(pc.getStatutPiece());
                        }
                        if (pc.getNotifs() != null ? pc.getNotifs().getAcompte() != null : false) {
                            ManagedOperationClient wa = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                            if (wa != null) {
                                wa.equilibre(pc.getNotifs().getAcompte());
                                update("table_cmpens_p_achat");
                            }
                        }
                        if (msg) {
                            succes();
                        }
                        return true;
                    }

                }
            }
        } else if (msg) {
            getErrorMessage("La pièce n'a pas été sauvegardé !");
        }
        return false;
    }

    public YvsComptaCaissePieceVente validePieceCaisse(YvsComptaCaissePieceVente y) {

        return y;
    }

    public void ecouteSaisieClient() {
        String num = pieceAvance.getClients().getCodeClient();
        pieceAvance.getClients().setId(0);
        pieceAvance.getClients().setError(true);
        pieceAvance.getClients().setTiers(new Tiers());
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            Client y = m.searchClient(num, true);
            if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                if (m.getClients().size() > 1) {
                    update("data_client_acomptes");
                } else {
                    chooseClient(y);
                }
                pieceAvance.getClients().setError(false);
            }
        }
    }

    public void chooseClient(Client d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(pieceAvance.getClients(), d);
        }
    }

    public void chooseOneCLient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient y = (YvsComClient) ev.getObject();
            chooseClient(UtilCom.buildBeanClient(y));
        }
    }

    public boolean controlePiece() {
        //controle la conformité du formulaire
        if (pieceAvance.getClients().getId() <= 0) {
            getErrorMessage("Vous devez entrer un clients !");
            return false;
        }
        if (pieceAvance.getMode() != null ? pieceAvance.getMode().getId() < 1 : true) {
            getErrorMessage("Vous precisez le mode de paiement !");
            return false;
        }
        if (pieceAvance.getMontant() <= 0) {
            getErrorMessage("Vous devez entrer un montant valide !");
            return false;
        }
        if (pieceAvance.getStatutPiece() != Constantes.STATUT_DOC_ATTENTE) {
            getErrorMessage("L'Etat de cette pièce n'autorise aucune modification !");
            return false;
        }
        String str = genererReference(Constantes.TYPE_PT_AVANCE_VENTE, new Date());
        if (str == null) {
            return false;
        } else {
            if (str.isEmpty()) {
                return false;
            }
            pieceAvance.setNumPiece(str);
        }
        return true;
    }

    public void loadOnViewPiece(SelectEvent ev) {
        if (ev != null) {
            ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
            if (w.getDocVente() != null ? w.getDocVente().getId() > 0 : false) {
                YvsComptaCaissePieceVente pc = (YvsComptaCaissePieceVente) ev.getObject();
                loadPcOnViewPiece(pc);
            }
        }
    }

    public void loadPcOnViewPiece(YvsComptaCaissePieceVente ev) {
        if (ev != null) {
            pieceAvance = UtilCompta.buildBeanTresoreri(ev);
        }
    }

    public void resetViewPc() {
        pieceAvance = new PieceTresorerie();
        pieceAvance.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        pieceAvance.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        pieceAvance.setDateValide(null);
    }

    public void openTodeleteOneLinePc(YvsComptaCaissePieceVente p) {
        loadPcOnViewPiece(p);
        openDialog("dlgConfirmDeletePC1");
    }

    public void deleteOneLinePc() {
        if (pieceAvance.getId() > 0) {
            if (pieceAvance.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                try {
                    YvsComptaCaissePieceVente pc = new YvsComptaCaissePieceVente(pieceAvance.getId());
                    pc.setAuthor(currentUser);
                    dao.delete(pc);
                    reglements.remove(pc);
                    resetViewPc();
                    succes();
                } catch (Exception ex) {
                    getErrorMessage("Impossible de supprimer !");
                    log.log(Level.SEVERE, null, ex);
                }
            } else {
                getErrorMessage("Cette pièce est déjà encaisssé !");
            }
        } else {
            getErrorMessage("Aucune pièce n'a été selectionné !");
        }
    }

    public void openDlgToBindDv(YvsComptaAcompteClient pc) {
        if (pc != null) {
            ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
            if (w.getDocVente() != null ? w.getDocVente().getId() > 0 : false) {
                selectAcompte = pc;
                update("form_confirm_bind_pc");
                openDialog("dlgConfirmBind");
            } else {
                getErrorMessage("Aucune facture n'a été selectionné !");
            }
        }
    }

    private List<YvsComptaCaissePieceVente> bindPieceAndAcompte() {
        List<YvsComptaCaissePieceVente> re = new ArrayList<>();
        List<Long> ids;
        if (pieceAvance.getId() <= 0) {
            ids = findIdAcomptesEnCours(pieceAvance.getDocVente().getClient().getId(), false);
        } else {
            ids = findIdAcomptesEnCours(pieceAvance.getDocVente().getClient().getId(), true);
            if (pieceAvance.getNotifRegVente() != null ? pieceAvance.getNotifRegVente().getId() != null ? pieceAvance.getNotifRegVente().getId() > 0 : false : false) {
                ids.add(pieceAvance.getNotifRegVente().getAcompte().getId());
            }
        }
        if (!ids.isEmpty()) {
            List<YvsComptaAcompteClient> list = dao.loadNameQueries("YvsComptaAcompteClient.findByIds", new String[]{"ids"}, new Object[]{ids});
            if (pieceAvance.getNotifRegVente() != null ? pieceAvance.getNotifRegVente().getId() != null ? pieceAvance.getNotifRegVente().getId() > 0 : false : false) {
                list.remove(pieceAvance.getNotifRegVente().getAcompte());
                list.add(0, pieceAvance.getNotifRegVente().getAcompte());
            }
            re = createAndBindPieceAcompte(pieceAvance.getMontant(), list);
        } else {
            if (pieceAvance.getId() <= 0) {
                getWarningMessage("Aucun acompte trouvé pour ce tiers !");
            } else {
                getErrorMessage("Impossible de modifier", "Aucun acompte correspondant à la pièce n'a été trouvé !");
            }
        }
        return re;
    }

    private List<YvsComptaCaissePieceVente> createAndBindPieceAcompte(Double montant, List<YvsComptaAcompteClient> list) {
        double reste;
        YvsComptaCaissePieceVente piece;
        List<YvsComptaCaissePieceVente> re = new ArrayList<>();
        for (YvsComptaAcompteClient c : list) {
            if (montant > 0) {
                Double resteUnBind = AYvsComptaAcompteClient.findResteUnBindForAcompteAndNotPiece(c, pieceAvance.getId(), dao);
                reste = (resteUnBind != null ? resteUnBind : 0);
                if (reste <= montant) {
                    // génère la pièce de règlement qui correspond au reste
                    piece = buildPiece(reste, c);
                    piece = createNewPieceCaisse(pieceAvance.getDocVente(), piece, true, true);
                    if (piece != null) {
                        bindPieceAcompte(c, piece);
                        re.add(piece);
                    }
                    montant = montant - reste;
                    pieceAvance.setId(-c.getId());
                } else {
                    // génère la pièce de règlement qui correspond au montantF
                    piece = buildPiece(montant, c);
                    piece = createNewPieceCaisse(pieceAvance.getDocVente(), piece, true, true);
                    if (piece != null) {
                        bindPieceAcompte(c, piece);
                        re.add(piece);
                    }
                    break;
                }
            } else {
                break;
            }
        }
        return re;
    }

    private YvsComptaCaissePieceVente buildPiece(double montant, YvsComptaAcompteClient a) {
        YvsComptaCaissePieceVente p = new YvsComptaCaissePieceVente(pieceAvance.getId());
        p.setCaisse(a.getCaisse());
        p.setNumeroPiece(pieceAvance.getNumPiece());
        p.setDatePaiement(pieceAvance.getDatePaiement());
        p.setDatePaimentPrevu(pieceAvance.getDatePaiementPrevu());
        p.setDatePiece(pieceAvance.getDatePiece());
        p.setModel(a.getModel());
        p.setMontant(montant);
        p.setMontantRecu(montant);
        p.setNote("Paiement par Acompte client");
        p.setReferenceExterne(a.getNumRefrence());
        p.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        p.setVente(UtilCom.buildDocVente(pieceAvance.getDocVente(), currentUser));
        return p;
    }

    private void bindPieceAcompte(YvsComptaAcompteClient a, YvsComptaCaissePieceVente p) {
        ManagedOperationClient service = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (service != null) {
            YvsComptaNotifReglementVente y = service.confirmBind(a, p);
        }
    }

    public void bindPieceAndFacture() {
        ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
        //controle        
        if (!selectAcompte.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
            getErrorMessage("La pièce n'a pas encore été encaissé !");
            return;
        }
        if (w.getDocVente() != null ? w.getDocVente().getId() < 1 : true) {
            getErrorMessage("Aucune facture n'a été selectionné !");
            return;
        }
        if (pieceVente != null ? pieceVente.getId() < 1 : true) {
            getErrorMessage("Aucune reglement n'a été selectionné !");
            return;
        }
        //controle l'identité du client'
        if (w.getDocVente().getClient().getId() != selectAcompte.getClient().getId()) {
            getErrorMessage("...Il s'agit de la facture d'un autre Client !");
            return;
        }
        Double resteUnBind = AYvsComptaAcompteClient.findResteUnBindForAcompte(selectAcompte, dao);
        // (resteUnBind != resteUnBind ? reste : 0);
        if ((resteUnBind != null ? resteUnBind : 0) < pieceVente.getMontant()) {
            getErrorMessage("Cet acompte ne peut pas couvrir ce reglement");
            return;
        }
        Double reste = AYvsComptaAcompteClient.findResteForAcompte(selectAcompte, dao);
        // (reste != null ? reste : 0);
        if ((reste != null ? reste : 0) < pieceVente.getMontant()) {
            getErrorMessage("Le reste sur cet acompte ne peut pas couvrir ce reglement");
            return;
        }
        ManagedOperationClient service = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (service != null) {
            YvsComptaNotifReglementVente y = service.confirmBind(selectAcompte, pieceVente);
            if (y != null ? y.getId() > 0 : false) {
                pieceVente.setNotifs(y);
                int idx = w.getDocVente().getReglements().indexOf(pieceVente);
                if (idx > -1) {
                    w.getDocVente().getReglements().set(idx, pieceVente);
                }
                update("table_regFV");
            }
        }
    }

    public void openDlgToUnbindPiece(YvsComptaCaissePieceVente pv) {
        pieceVente = pv;
        openDialog("dlgConfirmUnBind");
        update("form_confirm_unbind_pc");
    }

    public void duplicatePiece(YvsComptaCaissePieceVente pv, double montant) {
        YvsComptaCaissePieceVente newEn = new YvsComptaCaissePieceVente(pv);
        newEn.setId(null);
        newEn.setMontant(montant);
        newEn.setVente(null);
        newEn.setNumeroPiece(newEn.getNumeroPiece() + "°");
        if (newEn.getModel() != null ? newEn.getModel().getId() < 1 : true) {
            newEn.setModel(modeEspece());
        }
        newEn = (YvsComptaCaissePieceVente) dao.save1(newEn);
        newEn.setNew_(true);
        reglements.add(0, newEn);
    }

    public void openConfirmEncaiss(YvsComptaCaissePieceVente p) {
        pieceVente = p;
        openDialog("dlgConfirmPaye");
    }

    public void cleanParametrePiece() {
        paginator.getParams().clear();
        dateSearch = false;
        dateDebutSearch = new Date();
        dateFinSearch = new Date();
        statutF = null;
        modeSearch = null;
        modeFind = 0;
        caisseF = 0;
        montantF = 0;
        montantF1 = 0;
        clientF = "";
        numPieceF = "";
        initForm = true;
        loadAllPiece(true, initForm);
    }

    public void loadAllPiece(boolean avancer, boolean init) {
        ParametreRequete p = new ParametreRequete("y.vente.enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        paginator.addParam(p);
        reglements = paginator.executeDynamicQuery("YvsComptaCaissePieceVente", "y.datePaimentPrevu DESC", avancer, init, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        parcoursInAllResult(avancer, true);
    }

    public void parcoursInAllResult(boolean avancer, boolean complet) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }

        List<YvsComDocVentes> re = paginator.parcoursDynamicData("YvsComptaCaissePieceVente", "y", "y.datePaimentPrevu DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllPiece(true, initForm);
    }

    public void loadPieceByStatut(String statut) {
        loadPieceByStatut(statut, null, "!=");
    }

    public void loadPieceByStatut(String statut, String typeMode, String operateur) {
        paginator.getParams().clear();
        modeSearch = typeMode;
        operateurType = operateur;
        paginator.addParam(new ParametreRequete("y.model.typeReglement", "typemode", modeSearch, operateurType, "AND"));
        statut_ = statut;
        addParamStatut();
    }

    public void addParamStatut() {
        ParametreRequete p = new ParametreRequete("y.statutPiece", "statut", null, operateurStatut, "AND");
        if (statutF != null) {
            p = new ParametreRequete("y.statutPiece", "statut", statutF, operateurStatut, "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true, initForm);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.numeroPiece", "numeroPiece", null, "=", "AND");
        if (numPieceF != null ? numPieceF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numeroPiece", numPieceF + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numeroPiece)", "numeroPiece", numPieceF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.referenceExterne)", "numeroPiece", numPieceF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.vente.numDoc)", "numeroPiece", numPieceF.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true, initForm);
    }

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_piece_vente c RIGHT JOIN yvs_compta_caisse_piece_vente y ON c.reglement = y.id "
                    + "INNER JOIN yvs_base_caisse e ON y.caisse = e.id INNER JOIN yvs_compta_journaux h ON e.journal = h.id "
                    + "INNER JOIN yvs_agences a ON h.agence = a.id WHERE y.statut_piece = 'P' AND a.societe = ? "
                    + "AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (dateSearch) {
                query += " AND y.date_paiement BETWEEN ? AND ?";
                param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebutSearch, 2), new Options(dateFinSearch, 3)};
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, param);
            nbrComptaSearch = count != null ? count : 0;
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true, initForm);
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.vente.enteteDoc.creneau.creneauPoint.point.agence", "agence", null, "=", "AND");
        if (agenceSearch > 0) {
            p = new ParametreRequete("y.vente.enteteDoc.creneau.creneauPoint.point.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true, initForm);
    }

    public void addParamDates() {
        addParaDate(dateSearch);
    }

    public void addParamDate1(SelectEvent ev) {
        addParaDate(dateSearch);
    }

    private void addParaDate(boolean b) {
        ParametreRequete p = new ParametreRequete("y.datePaiement", "datePaiement", null, "BETWEEN", "AND");
        if (b) {
            if (dateDebutSearch != null && dateFinSearch != null) {
                if (dateDebutSearch.before(dateFinSearch) || dateDebutSearch.equals(dateFinSearch)) {
                    p.setObjet(dateDebutSearch);
                    p.setOtherObjet(dateFinSearch);
                }
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true, initForm);

    }

    public void addParamClient() {
        ParametreRequete p;
        if (clientF != null ? clientF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "client", clientF.toUpperCase() + "%", " LIKE ", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.vente.nomClient)", "client", clientF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.vente.client.codeClient)", "client", clientF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.vente.client.nom)", "client", clientF.toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p = new ParametreRequete("y.client.codeClient", "client", null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true, initForm);
    }

    public void addParamTypeMode() {
        ParametreRequete p = new ParametreRequete("y.model.typeReglement", "typemode", null);
        if (modeSearch != null ? modeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.model.typeReglement", "typemode", modeSearch, operateurType, "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true, initForm);
    }

    public void addParamModePaiement(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.model", "model", null, "=", "AND");
        if (ev.getNewValue() != null) {
            modeFind = (int) ev.getNewValue();
            if (modeFind > 0) {
                p.setObjet(new YvsBaseModeReglement((long) modeFind));
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true, initForm);

    }

    public void addParamCaisse(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.caisse", "caisse", null, "=", "AND");
        if (ev.getNewValue() != null) {
            caisseF = (long) ev.getNewValue();
            if (caisseF > 0) {
                p.setObjet(new YvsBaseCaisse(caisseF));
            } else if (caisseF == -2) {
                p = new ParametreRequete("y.caisse", "caisse", "IS NULL", "IS NULL", "AND");
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true, initForm);

    }

    public void addParamMontant() {
        ParametreRequete p = new ParametreRequete("y.montant", "montant", null, operateurMontant, "AND");
        if (montantF > 0) {
            if (!operateurMontant.equals("BETWEEN")) {
                p.setObjet(montantF);
            } else {
                if (montantF1 > 0 && montantF1 > montantF) {
                    p.setObjet(montantF);
                    p.setOtherObjet(montantF1);
                } else {
                    p.setObjet(null);
                }
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPiece(true, initForm);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        initForm = true;
        loadAllPiece(true, initForm);
    }

    public void print(YvsComptaCaissePieceVente y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + "/icones/" + (y.getStatutPiece() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getMontant()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                param.put("LOGO", returnLogo());
                executeReport("pc_vente", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedReglementVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Gestion du suivi des règlements*/
    boolean initForm;

    public void onSelectObjectForCheque(YvsComptaCaissePieceVente y) {
        selectedPiece = y;
        pieceReglement = UtilCompta.buildSimpleBeanTresoreri(y);
        pieceReglement.setPhases(ordonnePhase(dao.loadNameQueries("YvsComptaPhasePiece.findByPiece", new String[]{"piece"}, new Object[]{y})));
        if (pieceReglement.getPhases() != null ? !pieceReglement.getPhases().isEmpty() : false) {
            for (YvsComptaPhasePiece r : pieceReglement.getPhases()) {
                if (r.isEtapeActive()) {
                    currentPhaseVente = r;
                    break;
                }
            }
        }
        if (currentPhaseVente.getCaisse() == null) {
            currentPhaseVente.setCaisse(new YvsBaseCaisse(y.getCaisse()));
        }
        currentPhaseVente.setDateValider(new Date());
    }

    public void onSelectAcompteForCheque(YvsComptaAcompteClient y) {
        selectAcompte = y;
        selectAcompte.setPhasesReglement(dao.loadNameQueries("YvsComptaPhaseAcompteVente.findByPiece", new String[]{"piece"}, new Object[]{y}));
        selectAcompte.setPhasesReglement(ordonnePhases(selectAcompte.getPhasesReglement()));
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            if (selectAcompte.getPhasesReglement() != null ? !selectAcompte.getPhasesReglement().isEmpty() : false) {
                for (YvsComptaPhaseAcompteVente r : selectAcompte.getPhasesReglement()) {
                    if (r.isEtapeActive()) {
                        w.setCurrentPhaseAcompteVente(r);
                        break;
                    }
                }
            }
            if (w.getCurrentPhaseAcompteVente().getCaisse() == null) {
                w.getCurrentPhaseAcompteVente().setCaisse(new YvsBaseCaisse(y.getCaisse()));
            }
            w.getCurrentPhaseAcompteVente().setDateValider(new Date());
        }
    }

    public void onSelectCreditForCheque(YvsComptaReglementCreditClient y) {
        selectCredit = y;
        selectCredit.setPhasesReglement(ordonnePhasec(selectCredit.getPhasesReglement()));
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            if (selectCredit.getPhasesReglement() != null ? !selectCredit.getPhasesReglement().isEmpty() : false) {
                for (YvsComptaPhaseReglementCreditClient r : selectCredit.getPhasesReglement()) {
                    if (r.isEtapeActive()) {
                        w.setCurrentPhaseCreditVente(r);
                        break;
                    }
                }
            }
            if (w.getCurrentPhaseCreditVente().getCaisse() == null) {
                w.getCurrentPhaseCreditVente().setCaisse(new YvsBaseCaisse(y.getCaisse()));
            }
            w.getCurrentPhaseCreditVente().setDateValider(new Date());
        }
    }

    public void choixLinePiece(SelectEvent ev) {
        onSelectObjectForCheque((YvsComptaCaissePieceVente) ev.getObject());
    }

    public void onSelectDistantCheque(long id, String table) {
        if (table != null ? id > 0 : false) {
            YvsComptaMouvementCaisse y = (YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findByExterne", new String[]{"idExterne", "table"}, new Object[]{id, table});
            onSelectCheque(y);
            typeSearch = table;
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Suivi des chèques", "modCompta", "smenSuiviRegVente", true);
            }
        }
    }

    public void onSelectCheque(YvsComptaMouvementCaisse bean) {
        if (bean != null ? bean.getId() != null : false) {
            selectPiece = bean;
            reference = bean.getNumeroExterne();
            switch (selectPiece.getTableExterne()) {
                case Constantes.SCR_DIVERS: {
                    if (managedDocDivers != null) {
                        YvsComptaCaissePieceDivers y = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                        managedDocDivers.onSelectObjectForCheque(y);
                    }
                    break;
                }
                case Constantes.SCR_ACHAT: {
                    if (managedReglementAchat != null) {
                        YvsComptaCaissePieceAchat y = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                        managedReglementAchat.onSelectObjectForCheque(y);
                    }
                    break;
                }
                case Constantes.SCR_VENTE: {
                    YvsComptaCaissePieceVente y = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findByIdAll", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                    onSelectObjectForCheque(y);
                    break;
                }
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    if (managedReglementAchat != null) {
                        YvsComptaAcompteFournisseur y = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                        managedReglementAchat.onSelectAcompteForCheque(y);
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_VENTE: {
                    YvsComptaAcompteClient y = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                    onSelectAcompteForCheque(y);
                    break;
                }
                case Constantes.SCR_CREDIT_ACHAT: {
                    if (managedReglementAchat != null) {
                        YvsComptaReglementCreditFournisseur y = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                        managedReglementAchat.onSelectCreditForCheque(y);
                    }
                    break;
                }
                case Constantes.SCR_CREDIT_VENTE: {
                    YvsComptaReglementCreditClient r = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                    onSelectCreditForCheque(r);
                    break;
                }
                case Constantes.SCR_VIREMENT: {
                    if (managedVirement != null) {
                        YvsComptaCaissePieceVirement y = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                        managedVirement.onSelectObject(y);
                    }
                    break;
                }
            }
            if (!mouvements.contains(bean)) {
                mouvements.add(0, bean);
            } else {
                mouvements.remove(bean);
                mouvements.add(0, bean);
            }
        }
    }

    public void loadOnViewMouvement(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectCheque((YvsComptaMouvementCaisse) ev.getObject());
        }
    }

    public void validePiece() {
        selectedPiece.setDatePaiement(new Date());
        validePiece(selectedPiece, true);
    }

    public boolean validePiece(YvsComptaCaissePieceVente pv, boolean succes) {
        if (pv != null ? pv.getId() > 0 : false) {
            if (pv.getVerouille()) {
                getErrorMessage("Le chèque concerné par cette phase à été vérouillé !");
                return false;
            }
            if (currentParam == null) {
                currentParam = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if ((pv.getCaisse() != null) ? pv.getCaisse().getId() > 0 : false) {
                if (!controleAccesCaisse(pv.getCaisse(), true)) {
                    return false;
                }
                if (pv.getNotifs() != null ? pv.getNotifs().getId() > 0 : false) {
                    if (!autoriser("p_caiss_payer_acompte")) {
                        openNotAcces();
                        return false;
                    }
                    if (!pv.getNotifs().getAcompte().getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                        getErrorMessage("L'acompte lié à ce reglement n'est pas encore encaissé");
                        return false;
                    }
                    double montant = pv.getMontant();
                    for (YvsComptaNotifReglementVente n : pv.getNotifs().getAcompte().getNotifs()) {
                        if (n.getPieceVente().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                            montant += n.getPieceVente().getMontant();
                        }
                    }
                    if (montant > pv.getNotifs().getAcompte().getMontant()) {
                        getErrorMessage("Vous ne pouvez pas valider ce montant.. car la somme des pièces excedera le montant de l'acompte");
                        return false;
                    }
                    if (pv.getNotifs().getAcompte().getDatePaiement().after(pv.getDatePaiement())) {
                        pv.setDatePaiement(pv.getNotifs().getAcompte().getDatePaiement());
                    }
                }
                if (currentParam != null ? !currentParam.getPaieWithoutValide() ? !pv.getVente().getStatut().equals(Constantes.ETAT_VALIDE) : false : false) {
                    getErrorMessage("La facture doit etre au préalable validée");
                    return false;
                }
                pv.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                pv.setAuthor(currentUser);
                if (pv.getCaissier() != null ? pv.getCaissier().getId() < 1 : true) {
                    pv.setCaissier(currentUser.getUsers());
                }
                pv.setDateValide(new Date());
                pv.setDateUpdate(new Date());
                pv.setValideBy(currentUser.getUsers());
                dao.update(pv);
                if (pv.getVente() != null ? pv.getVente().getId() > 0 : false) {
                    Map<String, String> statuts = dao.getEquilibreVente(pv.getVente().getId());
                    if (statuts != null) {
                        pv.getVente().setStatutLivre(statuts.get("statut_livre"));
                        pv.getVente().setStatutRegle(statuts.get("statut_regle"));
                    }
                    ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
                    if (w != null) {
                        w.resetView(pv.getVente());
                        int idx = w.getDocVente().getReglements().indexOf(pv);
                        if (idx > -1) {
                            w.getDocVente().getReglements().set(idx, pv);
                            update("data_mensualite_facture_vente");
                        }
                    }
                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    w.comptabiliserCaisseVente(pv, false, false);
                }
                pieceReglement.setStatutPiece(pv.getStatutPiece());
                update("head_corp_form_suivi_prv");
                update("head_form_suivi_prv");
                if (succes) {
                    succes();
                }
                return true;
            } else {
                getErrorMessage("Aucune caisse n'a été trouvé !");
            }
        }
        return false;
    }

    public void valideAcompte() {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            if ((selectAcompte.getCaisse() != null ? selectAcompte.getCaisse().getId() > 0 : false) && selectAcompte.getDatePaiement() != null) {
                w.encaisserAcompte(selectAcompte);
            } else if (selectAcompte.getDatePaiement() == null) {
                getErrorMessage("Veuillez entrer une date de validation !");
            } else {
                getErrorMessage("Veuillez choisir une caisse ou banque !");
            }
        }
        update("head_corp_form_suivi_pav");
    }

    public void valideCredit() {
        if (selectCredit != null ? selectCredit.getId() > 0 : false) {
            if ((selectCredit.getCaisse() != null) ? selectCredit.getId() > 0 : false) {
                selectCredit.setStatut(Constantes.STATUT_DOC_PAYER);
                selectCredit.setAuthor(currentUser);
                selectCredit.setDateUpdate(new Date());
                dao.update(selectCredit);
                update("head_form_suivi_pcv");
                succes();
            } else {
                getErrorMessage("Aucune caisse n'a été trouvé !");
            }
        }
    }

    private boolean controleValidation(YvsComptaPhasePiece pp) {
        if ((pp.getPieceVente().getCaisse() != null) ? pp.getPieceVente().getCaisse().getId() <= 0 : true) {
            getErrorMessage("Aucune banque n'a été trouvé !");
            return false;
        }
        if ((pp.getPieceVente().getVente() != null) ? !pp.getPieceVente().getVente().getStatut().equals(Constantes.ETAT_VALIDE) : true) {
            getErrorMessage("Le document de vente n'a pas été validé");
            return false;
        }
        return true;
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

    public void comptabiliserPhaseCaisseVente(YvsComptaPhasePiece pp, boolean comptabilise) {
        selectPhaseVente = pp;
        if (pp.getPieceVente().getVerouille()) {
            getErrorMessage("Le chèque concerné par cette phase à été vérouillé !");
            return;
        }
        if (pieceReglement.getPhases() != null ? !pieceReglement.getPhases().isEmpty() : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                int idx = pieceReglement.getPhases().indexOf(pp);
                if (idx > -1) {
                    if (idx == 0) {
                        if (comptabilise) {
                            w.comptabiliserPhaseCaisseVente(pp, true, true);
                        } else {
                            openDialog("dlgComptabilisePhase");
                        }
                    } else {
                        YvsComptaPhasePiece prec = pieceReglement.getPhases().get(idx - 1);
                        if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_CAISSE_VENTE)) {
                            if (comptabilise) {
                                w.comptabiliserPhaseCaisseVente(pp, true, true);
                            } else {
                                openDialog("dlgComptabilisePhase");
                            }
                        } else {
                            openDialog("dlgComptabilisePhaseByForce");
                        }
                    }
                }
            }
        }
    }

    public void printPhase(YvsComptaPhasePiece y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getPieceVente().getStatutPiece() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getPieceVente().getMontant()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                executeReport("phase_pc_vente", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedReglementVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void validEtapesPieces(YvsComptaPhasePiece pp) {
        if (!pp.getPhaseOk()) {
            if (!asDroitValidePhase(pp.getPhaseReg())) {
                openNotAcces();
                return;
            }
            if (currentParam == null) {
                currentParam = (YvsComParametreVente) dao.loadOneByNameQueries("YvsComParametreVente.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (!pp.getPieceVente().getVente().getNumDoc().equals("")) {
                if (currentParam != null ? !currentParam.getPaieWithoutValide() ? !pp.getPieceVente().getVente().getStatut().equals(Constantes.ETAT_VALIDE) : false : false) {
                    getErrorMessage("La facture doit etre au préalable validée");
                    return;
                }
            }
            if (controleValidation(pp)) {
                if (pp.getPhaseReg().getActionInBanque()) {
                    if (!pp.getCaisse().getTypeCaisse().equals("BANQUE")) {
                        getErrorMessage("La validation de cette phase doit se passer dans une banque");
                        return;
                    }
                }
                afterValidePhaseCheque(pp);
                //Comptabilise la pièce 
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    w.comptabiliserPhaseCaisseVente(pp, false, false);
                }
            }
        } else {
            getWarningMessage("Phase déjà validé! ");
        }
//        update("head_corp_form_suivi_prv");
//        update("head_form_suivi_prv");
    }

    private void afterValidePhaseCheque(YvsComptaPhasePiece pp) {
        pp.setPhaseOk(true);
        pp.setDateUpdate(new Date());
        pp.setStatut(Constantes.STATUT_DOC_VALIDE);
        pp.setAuthor(currentUser);
        pp.setEtapeActive(false);
        dao.update(pp);
        if (pp.getPieceVente().getVente() != null ? pp.getPieceVente().getVente().getId() > 0 : false) {
            if (pp.getPhaseReg().getReglementOk()) {
                pp.getPieceVente().setStatutPiece(Constantes.STATUT_DOC_PAYER);
                pp.getPieceVente().setDatePaiement(pp.getDateValider());
            } else {
                if (pp.getPieceVente().getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                    pp.getPieceVente().setStatutPiece(Constantes.STATUT_DOC_ENCOUR);
                }
            }
            pp.getPieceVente().setAuthor(currentUser);
            pp.getPieceVente().setDateUpdate(new Date());
            dao.update(pp.getPieceVente());
            Map<String, String> statuts = dao.getEquilibreVente(pp.getPieceVente().getVente().getId());
            if (statuts != null) {
                pp.getPieceVente().getVente().setStatutLivre(statuts.get("statut_livre"));
                pp.getPieceVente().getVente().setStatutRegle(statuts.get("statut_regle"));
            }
            ManagedFactureVenteV2 w = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
            if (w != null) {
                w.resetView(pp.getPieceVente().getVente());
                int idx = w.getDocVente().getReglements().indexOf(pp.getPieceVente());
                if (idx > -1) {
                    w.getDocVente().getReglements().set(idx, pp.getPieceVente());
                    update("data_mensualite_facture_vente");
                }
            }
            pieceReglement.setStatutPiece(pp.getPieceVente().getStatutPiece());
        }

        int idx = pieceReglement.getPhases().indexOf(pp);
        if (idx >= 0 && (idx + 1) < pieceReglement.getPhases().size()) {
            pieceReglement.getPhases().get(idx + 1).setEtapeActive(true);
            currentPhaseVente = pieceReglement.getPhases().get(idx + 1);
        } else if (idx == (pieceReglement.getPhases().size() - 1)) {
            pieceReglement.getPhases().get(idx).setEtapeActive(true);
        }
        idx = pieceReglement.getPhases().indexOf(pp);
        if (idx > -1) {
            pieceReglement.getPhases().set(idx, pp);
        }
        update("head_corp_form_suivi_prv");
        update("head_form_suivi_prv");
    }

    public void comptabiliserPhaseAcompteVente(YvsComptaPhaseAcompteVente pp, boolean comptabilise) {
        selectPhaseAcompteVente = pp;
        if (selectAcompte.getPhasesReglement() != null ? !selectAcompte.getPhasesReglement().isEmpty() : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                int idx = selectAcompte.getPhasesReglement().indexOf(pp);
                if (idx > -1) {
                    if (idx == 0) {
                        if (comptabilise) {
                            w.comptabiliserPhaseAcompteVente(pp, true, true);
                        } else {
                            openDialog("dlgComptabilisePhase");
                        }
                    } else {
                        YvsComptaPhaseAcompteVente prec = selectAcompte.getPhasesReglement().get(idx - 1);
                        if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE)) {
                            if (comptabilise) {
                                w.comptabiliserPhaseAcompteVente(pp, true, true);
                            } else {
                                openDialog("dlgComptabilisePhase");
                            }
                        } else {
                            openDialog("dlgComptabilisePhaseByForce");
                        }
                    }
                }
            }
        }
    }

    public void printPhaseAcompte(YvsComptaPhaseAcompteVente y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getPieceVente().getStatut() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getPieceVente().getMontant()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                executeReport("phase_pc_acompte_vente", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedReglementVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void validEtapesAcompte(YvsComptaPhaseAcompteVente pp) {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            if ((pp.getCaisse() != null ? pp.getCaisse().getId() > 0 : false) && pp.getDateValider() != null) {
                w.validEtapesAcompte(selectAcompte, pp);
            } else if (pp.getDateValider() == null) {
                getErrorMessage("Veuillez entrer une date de validation !");
            } else {
                getErrorMessage("Veuillez choisir une caisse ou banque !");
            }
        }
        update("head_form_suivi_pav");
    }

    public void comptabiliserPhaseCreditVente(YvsComptaPhaseReglementCreditClient pp, boolean comptabilise) {
        selectPhaseCreditVente = pp;
        if (selectCredit.getPhasesReglement() != null ? !selectCredit.getPhasesReglement().isEmpty() : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                int idx = selectCredit.getPhasesReglement().indexOf(pp);
                if (idx > -1) {
                    if (idx == 0) {
                        if (comptabilise) {
                            w.comptabiliserPhaseCaisseCreditVente(pp, true, true);
                        } else {
                            openDialog("dlgComptabilisePhase");
                        }
                    } else {
                        YvsComptaPhaseReglementCreditClient prec = selectCredit.getPhasesReglement().get(idx - 1);
                        if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_CREDIT_VENTE)) {
                            if (comptabilise) {
                                w.comptabiliserPhaseCaisseCreditVente(pp, true, true);
                            } else {
                                openDialog("dlgComptabilisePhase");
                            }
                        } else {
                            openDialog("dlgComptabilisePhaseByForce");
                        }
                    }
                }
            }
        }
    }

    public void printPhaseCredit(YvsComptaPhaseReglementCreditClient y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getReglement().getStatut() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getReglement().getValeur()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                executeReport("phase_pc_credit_vente", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedReglementVente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void validEtapesCredit(YvsComptaPhaseReglementCreditClient pp) {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            if ((pp.getCaisse() != null ? pp.getCaisse().getId() > 0 : false) && pp.getDateValider() != null) {
                w.validEtapesCredit(selectCredit, pp);
            } else if (pp.getDateValider() == null) {
                getErrorMessage("Veuillez entrer une date de validation !");
            } else {
                getErrorMessage("Veuillez choisir une caisse ou banque !");
            }
        }
        update("head_form_suivi_pcv");
    }

    //Supprimer les étapes de validation d'un chèque et ramener la pièce au statut en cours
    public void cancelAllEtapesPieces() {
        if (!autoriser("compta_cancel_piece_valide")) {
            openNotAcces();
            return;
        }
        YvsComptaCaissePieceVente pc = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{pieceReglement.getId()});
        if (pc != null ? pc.getId() != null ? pc.getId() > 0 : false : false) {
            //vérifie le droit:
            if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                try {
                    int i = 0;
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (pc.getPhasesReglement() != null ? !pc.getPhasesReglement().isEmpty() : false) {
                        for (YvsComptaPhasePiece ph : pc.getPhasesReglement()) {
                            ph.setPhaseOk(false);
                            ph.setEtapeActive(i == 0);
                            ph.setAuthor(currentUser);
                            dao.update(ph);
                            if (w != null) {
                                w.unComptabiliserPhaseCaisseVente(ph, false, true);
                            }
                            i++;
                            int idx = pieceReglement.getPhases().indexOf(ph);
                            if (idx > -1) {
                                pieceReglement.getPhases().set(idx, ph);
                            }
                        }
                    } else {
                        if (w != null) {
                            w.unComptabiliserCaisseVente(pc, false);
                        }
                    }
                    pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    pc.setDateUpdate(new Date());
                    pc.setAuthor(currentUser);
                    dao.update(pc);

                } catch (Exception ex) {
                    getErrorMessage("Impossible d'annuler les phases!");
                    log.log(Level.SEVERE, null, ex);
                }
            } else {
                openDialog("dlgConfirmCancel");
            }
        }
        update("head_form_suivi_prv");
    }

    public void cancelAllEtapesAcompte() {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            w.cancelAllEtapesAcompte(selectAcompte, true);
        }
        update("head_form_suivi_pav");
    }

    public void cancelAllEtapesCredit() {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            w.cancelAllEtapesCredit(selectCredit);
        }
        update("head_form_suivi_pcv");
    }

    public void cancelValidEtapesPieces(YvsComptaPhasePiece pp, boolean retour) {
        //l'étape suivante ne doit pas être validé
        if (!asDroitValidePhase(pp.getPhaseReg())) {
            openNotAcces();
            return;
        }
        int idx = pieceReglement.getPhases().indexOf(pp);
        YvsComptaPhasePiece pSvt = null;
        if (idx >= 0 && (idx + 1) < pieceReglement.getPhases().size()) {
            pSvt = pieceReglement.getPhases().get(idx + 1);
        } else if (idx == (pieceReglement.getPhases().size() - 1) || idx == 0) {
            pSvt = pieceReglement.getPhases().get(idx);
        }
        if (pSvt != null) {
            if (!pSvt.isEtapeActive() ? !pSvt.equals(pp) : false) {
                getErrorMessage("Vous ne pouvez annuler cette étape !");
                return;
            }
            pSvt.setEtapeActive(false);
            idx = pieceReglement.getPhases().indexOf(pSvt);
            if (idx >= 0) {
                pieceReglement.getPhases().set(idx, pSvt);
            }
        }
        if (dao.isComptabilise(pp.getId(), Constantes.SCR_PHASE_CAISSE_VENTE)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return;
            }
            //Annuler Comptabilise la pièce 
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                if (retour) {
                    if (!w.unComptabiliserPhaseCaisseVente(pp, true, false, true)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                } else {
                    if (!w.unComptabiliserPhaseCaisseVente(pp, true, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                }
            }
        }
        pp.setAuthor(currentUser);
        pp.setDateUpdate(new Date());
        pp.setPhaseOk(false);
        pp.setEtapeActive(true);
        pp.setStatut(retour ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE);
        //Si c'est la phase qui marque la facture réglé
        if (pp.getPhaseReg().getReglementOk()) {
            pp.getPieceVente().setStatutPiece(retour ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE);
            pp.getPieceVente().setDatePaiement(null);
            dao.update(pp.getPieceVente());
            pieceReglement.setStatutPiece(retour ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE);
        }
        dao.update(pp);
        currentPhaseVente = pp;
        currentPhaseVente.setDateValider(new Date());
        YvsComptaCaissePieceVente pc = pp.getPieceVente();
        idx = pc.getPhasesReglement().indexOf(pp);
        if (idx >= 0) {
            pc.getPhasesReglement().set(idx, pp);
        }
        idx = pieceReglement.getPhases().indexOf(pp);
        if (idx >= 0) {
            pieceReglement.getPhases().set(idx, pp);
        }
//        try {
//            if (retour) {
//                if (montantRetour > 0) {
//                    YvsBaseModeReglement mode = modeEspece();
//                    YvsComptaCaissePieceVente y = buildPieceFromModel(-1, mode, pieceReglement.getDocVente(), pp.getCaisse(), new Date(), montantRetour);
//                    y.setParent(new YvsComptaCaissePieceVente(pieceReglement.getId()));
//                    y.setStatutPiece(Constantes.STATUT_DOC_PAYER);
//                    y.setDatePaiement(pieceReglement.getDatePaiement());
//                    y.setDateValide(new Date());
//                    y.setCaissier(currentUser.getUsers());
//                    y.setValideBy(currentUser.getUsers());
//                    y.setMouvement(Constantes.MOUV_CAISS_SORTIE.charAt(0));
//                    y.setNote("Pénalité bancaire");
//                    y.setId(null);
//                    y = (YvsComptaCaissePieceVente) dao.save1(y);
//
//                    YvsComptaCaissePieceVente s = buildPieceFromModel(-2, mode, pieceReglement.getDocVente(), pp.getCaisse(), new Date(), montantRetour);
//                    s.setParent(new YvsComptaCaissePieceVente(pieceReglement.getId()));
//                    s.setNote("Règlement pénalité bancaire");
//                    s.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
//                    s.setId(null);
//                    s = (YvsComptaCaissePieceVente) dao.save1(s);
//                    y.getSousVentes().add(s);
//                    pieceReglement.getSousVentes().add(y);
//                    update("corps_form_suivi_prv");
//                }
//            }
//        } catch (Exception ex) {
//            getException("Action impossible", ex);
//            getErrorMessage("Action impossible");
//        }
        updateMouvement();
        update("head_form_suivi_prv");
    }

    public void cancelValidEtapesAcompte(YvsComptaPhaseAcompteVente pp) {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            w.cancelValidEtapesAcompte(selectAcompte, pp);
        }
        updateMouvement();
        update("head_form_suivi_pav");
    }

    public void cancelValidEtapesCredit(YvsComptaPhaseReglementCreditClient pp) {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            w.cancelValidEtapesCredit(selectCredit, pp, false);
        }
        updateMouvement();
        update("head_form_suivi_pcv");
    }

    private void ordonnePhase(List<YvsComptaPhasePiece> l, YvsComptaPhasePiece p) {
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

    private List<YvsComptaPhasePiece> ordonnePhase(List<YvsComptaPhasePiece> l) {
//        Collections.sort(l, new YvsComptaPhasePiece());
//        for (YvsComptaPhasePiece p : l) {
//            System.err.println(p.getPhaseReg().getNumeroPhase() + " - " + p.getPhaseReg().getPhase());
//        }
//        l.sort(new Comparator<YvsComptaPhasePiece>() {
//
//            @Override
//            public int compare(YvsComptaPhasePiece o1, YvsComptaPhasePiece o2) {
//                if (o1 == null && o2 != null) {
//                    if (o2.getPhaseReg() != null) {
//                        return -1;
//                    }
//                }
//                if (o2 == null && o1 != null) {
//                    if (o1.getPhaseReg() != null) {
//                        return 1;
//                    }
//                }
//                return (o1.getPhaseReg().getNumeroPhase().compareTo(o2.getPhaseReg().getNumeroPhase()));
//            }
//        });
//        for (YvsComptaPhasePiece p : l) {
//            System.err.println(p.getPhaseReg().getNumeroPhase() + " - " + p.getPhaseReg().getPhase());
//        }
        int idx = 0;
        while (idx < l.size()) {
            ordonnePhase(l, l.get(idx));
            idx++;
        }
        return l;
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

    private List<YvsComptaPhaseAcompteVente> ordonnePhases(List<YvsComptaPhaseAcompteVente> l) {
//        Collections.sort(l, new YvsComptaPhaseAcompteVente());
        int idx = 0;
        while (idx < l.size()) {
            ordonnePhase(l, l.get(idx));
            idx++;
        }
        return l;
    }

    private void ordonnePhase(List<YvsComptaPhaseReglementCreditClient> l, YvsComptaPhaseReglementCreditClient p) {
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

    private List<YvsComptaPhaseReglementCreditClient> ordonnePhasec(List<YvsComptaPhaseReglementCreditClient> l) {
        Collections.sort(l, new YvsComptaPhaseReglementCreditClient());
        int idx = 0;
        while (idx < l.size()) {
            ordonnePhase(l, l.get(idx));
            idx++;
        }
        return l;
    }

    private List<Long> findIdAcomptesEnCours(long idCLient, boolean sup) {
        List<Long> ids = new ArrayList<>();
        String query = " SELECT y.id FROM yvs_compta_acompte_client y "
                + " WHERE y.statut='P' AND y.client=?"
                + " AND ((y.montant-(COALESCE((SELECT SUM(p.montant) FROM yvs_compta_notif_reglement_vente n INNER JOIN yvs_compta_caisse_piece_vente p ON p.id=n.piece_vente WHERE n.acompte=y.id),0)))>0) ";
        Options[] ops = new Options[1];
        ops[0] = new Options(idCLient, 1);
        if (sup) {
//            query = query + " LIMIT 1";
        }
        ids = dao.loadListBySqlQuery(query, ops);
        return ids;
    }

//    public void loadAllCheque() {
//        initForm = true;
//        loadAllPieceReglement(true);
//    }
//    public void loadAllPieceReglement(boolean avance) {
//        paginatrorData.addParam(new ParametreRequete("y.model.typeReglement", "typeReglement", Constantes.MODE_PAIEMENT_BANQUE, "=", "AND"));
//        cheques = paginatrorData.executeDynamicQuery("YvsComptaCaissePieceVente", "y.datePaiement", avance, initForm, (int) imax, dao);
//    }
    public void loadMouvements() {
        initForm = true;
        loadAllMouvement(true);
    }

    public void loadAllMouvement(boolean avance) {
        if (currentAgence != null) {
            agenceFind = (agenceFind <= 0) ? currentAgence.getId() : 0L;
            List<Long> ids = dao.loadNameQueries("YvsComptaNotifReglementVente.findIdPiece", new String[]{"typeReglement", "societe"}, new Object[]{Constantes.MODE_PAIEMENT_BANQUE, currentAgence.getSociete()});
            if ((ids != null ? !ids.isEmpty() : false)) {
                ParametreRequete p = new ParametreRequete(null, "idVente", "id", "=", "AND");
                ParametreRequete p1 = new ParametreRequete(null, "idVente1", "id1", "=", "OR");
                p1.getOtherExpression().add(new ParametreRequete("y.tableExterne", "table1", Constantes.SCR_VENTE, "!=", "AND"));
                p1.getOtherExpression().add(new ParametreRequete("y.idExterne", "id1", "0", "IS NOT NULL", "AND"));
                p.getOtherExpression().add(p1);
                ParametreRequete p2 = new ParametreRequete(null, "idVente2", "id2", "=", "OR");
                p2.getOtherExpression().add(new ParametreRequete("y.tableExterne", "table2", Constantes.SCR_VENTE, "=", "AND"));
                p2.getOtherExpression().add(new ParametreRequete("y.idExterne", "id2", ids, "NOT IN", "AND"));
                p.getOtherExpression().add(p2);
                paginatorData.addParam(p);
            }
            ids = dao.loadNameQueries("YvsComptaNotifReglementAchat.findIdPiece", new String[]{"typeReglement", "societe"}, new Object[]{Constantes.MODE_PAIEMENT_BANQUE, currentAgence.getSociete()});
            if ((ids != null ? !ids.isEmpty() : false)) {
                ParametreRequete p = new ParametreRequete(null, "idAchat", "id", "=", "AND");
                ParametreRequete p1 = new ParametreRequete(null, "idAchat1", "id1", "=", "OR");
                p1.getOtherExpression().add(new ParametreRequete("y.tableExterne", "table1", Constantes.SCR_ACHAT, "!=", "AND"));
                p1.getOtherExpression().add(new ParametreRequete("y.idExterne", "id1", "0", "IS NOT NULL", "AND"));
                p.getOtherExpression().add(p1);
                ParametreRequete p2 = new ParametreRequete(null, "idAchat2", "id2", "=", "OR");
                p2.getOtherExpression().add(new ParametreRequete("y.tableExterne", "table2", Constantes.SCR_ACHAT, "=", "AND"));
                p2.getOtherExpression().add(new ParametreRequete("y.idExterne", "id2", ids, "NOT IN", "AND"));
                p.getOtherExpression().add(p2);
                paginatorData.addParam(p);
            }
            //Exclus les ligne de notification de règlement
            paginatorData.addParam(new ParametreRequete("y.tableExterne", "tableAchat", Constantes.SCR_NOTIF_ACHAT, "!=", "AND"));
            paginatorData.addParam(new ParametreRequete("y.tableExterne", "tableVente", Constantes.SCR_NOTIF_VENTE, "!=", "AND"));
            paginatorData.addParam(new ParametreRequete("y.model.typeReglement", "typeReglement", Constantes.MODE_PAIEMENT_BANQUE, "=", "AND"));
            if (!autoriser("p_caiss_view_all_societe")) {
                paginatorData.addParam(new ParametreRequete("y.agence", "agence", new YvsAgences(agenceFind), "=", "AND"));
            } else {
                paginatorData.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
            }
            if (dateValidePhase) {

            }
            String query = "YvsComptaMouvementCaisse y JOIN FETCH y.caisse LEFT JOIN FETCH y.caissier LEFT JOIN FETCH y.tiersExterne ";
            mouvements = paginatorData.executeDynamicQuery("y", "y", query, "y.dateMvt", avance, initForm, (int) imax, dao);
        }
    }

    public void paginer(boolean next) {
        initForm = false;
        loadAllMouvement(next);
    }

//    public void paginerCheque(boolean next) {
//        initForm = false;
//        loadAllPieceReglement(next);
//    }
//
//    public void choosePaginatorCheque(ValueChangeEvent ev) {
//        imax = (long) ev.getNewValue();
//        initForm = true;
//        loadAllPieceReglement(true);
//    }
    public void choosePaginators(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        initForm = true;
        loadAllMouvement(true);
    }

//    public void addParamDateOnCheque() {
//        ParametreRequete p = new ParametreRequete("y.datePaiement", "dates", null, "BETWEEN", "AND");
//        if (dateSearch ? dateDebutSearch != null && dateFinSearch != null && !dateFinSearch.before(dateDebutSearch) : false) {
//            p = new ParametreRequete("y.datePaiement", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
//        }
//        paginatrorData.addParam(p);
//        initForm = true;
//        loadAllPieceReglement(true);
//    }
//
//    public void addParamCodeOnCheque() {
//        ParametreRequete p = new ParametreRequete("y.vente.numDoc", "numero", null, "=", "AND");
//        if (numPieceF != null ? numPieceF.trim().length() > 0 : false) {
//            p = new ParametreRequete(null, "numero", numPieceF.toUpperCase() + "%", "=", "AND");
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numeroPiece)", "numero", "%" + numPieceF.toUpperCase() + "%", "LIKE", "OR"));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.referenceExterne)", "numero", "%" + numPieceF.toUpperCase() + "%", "LIKE", "OR"));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.vente.numDoc)", "numero", "%" + numPieceF.toUpperCase() + "%", "LIKE", "OR"));
//        }
//        paginatrorData.addParam(p);
//        initForm = true;
//        loadAllPieceReglement(true);
//    }
//
//    public void addParamClientOnCheque() {
//        ParametreRequete p = new ParametreRequete("y.vente.nomClient", "client", null, "=", "AND");
//        if (clientF != null ? clientF.trim().length() > 0 : false) {
//            p = new ParametreRequete(null, "client", clientF.toUpperCase() + "%", "=", "AND");
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.vente.nomClient)", "client", clientF.toUpperCase() + "%", "LIKE", "OR"));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.vente.client.codeClient)", "client", clientF.toUpperCase() + "%", "LIKE", "OR"));
//            p.getOtherExpression().add(new ParametreRequete("UPPER(y.vente.client.nom)", "client", clientF.toUpperCase() + "%", "LIKE", "OR"));
//        }
//        paginatrorData.addParam(p);
//        initForm = true;
//        loadAllPieceReglement(true);
//    }
//    public void addParamCaisseOnCheque() {
//        ParametreRequete p = new ParametreRequete("y.caisse", "caisse", null, "=", "AND");
//        if (caisseF > 0) {
//            p = new ParametreRequete("y.caisse", "caisse", new YvsBaseCaisse(caisseF), "=", "AND");
//        }
//        paginatrorData.addParam(p);
//        initForm = true;
//        loadAllPieceReglement(true);
//    }
    public void addParamDateValidePhase() {
        ParametreRequete p = new ParametreRequete("y.idExterne", "idExterne", null, "IN", "AND");
        if (dateValidePhase ? dateDebutPhaseSearch != null && dateFinPhaseSearch != null && !dateFinPhaseSearch.before(dateDebutPhaseSearch) : false) {
            p = new ParametreRequete(null, "idExterne", "idExterne", "IN", "AND");
            Options[] params = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebutPhaseSearch, 2), new Options(dateFinPhaseSearch, 3)};
            String where = " INNER JOIN yvs_base_caisse c ON y.caisse = c.id INNER JOIN yvs_compta_journaux j ON c.journal = j.id INNER JOIN yvs_agences a ON j.agence = a.id WHERE a.societe = ? AND y.date_valider BETWEEN ? AND ?";
            //Phase Acompte fournisseur
            String query = "SELECT DISTINCT y.piece_achat FROM yvs_compta_phase_acompte_achat y";
            List<Long> ids = dao.loadListBySqlQuery((query += where), params);
            boolean add = false;
            if (ids != null ? !ids.isEmpty() : false) {
                ParametreRequete s = new ParametreRequete(null, "idAcompteA", null, "IN", "OR");
                s.getOtherExpression().add(new ParametreRequete("y.idExterne", "idAcompteA", ids, "IN", "AND"));
                s.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableAcompteA", Constantes.SCR_ACOMPTE_ACHAT, "=", "AND"));
                p.getOtherExpression().add(s);
                add = true;
            }
            //Phase Acompte client
            query = "SELECT DISTINCT y.piece_vente FROM yvs_compta_phase_acompte_vente y";
            ids = dao.loadListBySqlQuery((query += where), params);
            if (ids != null ? !ids.isEmpty() : false) {
                ParametreRequete s = new ParametreRequete(null, "idAcompteV", null, "IN", "OR");
                s.getOtherExpression().add(new ParametreRequete("y.idExterne", "idAcompteV", ids, "IN", "AND"));
                s.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableAcompteV", Constantes.SCR_ACOMPTE_VENTE, "=", "AND"));
                p.getOtherExpression().add(s);
                add = true;
            }
            //Phase facture achat
            query = "SELECT DISTINCT y.piece_achat FROM yvs_compta_phase_piece_achat y";
            ids = dao.loadListBySqlQuery((query += where), params);
            if (ids != null ? !ids.isEmpty() : false) {
                ParametreRequete s = new ParametreRequete(null, "idAchat", null, "IN", "OR");
                s.getOtherExpression().add(new ParametreRequete("y.idExterne", "idAchat", ids, "IN", "AND"));
                s.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableFA", Constantes.SCR_ACHAT, "=", "AND"));
                p.getOtherExpression().add(s);
                add = true;
            }
            //Phase facture vente
            query = "SELECT DISTINCT y.piece_vente FROM yvs_compta_phase_piece y";
            ids = dao.loadListBySqlQuery((query += where), params);
            if (ids != null ? !ids.isEmpty() : false) {
                ParametreRequete s = new ParametreRequete(null, "idVente", null, "IN", "OR");
                s.getOtherExpression().add(new ParametreRequete("y.idExterne", "idVente", ids, "IN", "AND"));
                s.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableFV", Constantes.SCR_VENTE, "=", "AND"));
                p.getOtherExpression().add(s);
                add = true;
            }
            //Phase opération diverse
            query = "SELECT DISTINCT y.piece_divers FROM yvs_compta_phase_piece_divers y";
            ids = dao.loadListBySqlQuery((query += where), params);
            if (ids != null ? !ids.isEmpty() : false) {
                ParametreRequete s = new ParametreRequete(null, "idOthers", null, "IN", "OR");
                s.getOtherExpression().add(new ParametreRequete("y.idExterne", "idOthers", ids, "IN", "AND"));
                s.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableOthers", Constantes.SCR_DIVERS, "=", "AND"));
                p.getOtherExpression().add(s);
                add = true;
            }
            //Phase ordre salaire
            query = "SELECT DISTINCT y.piece FROM yvs_compta_phase_piece_salaire y";
            ids = dao.loadListBySqlQuery((query += where), params);
            if (ids != null ? !ids.isEmpty() : false) {
                ParametreRequete s = new ParametreRequete(null, "idSalaire", null, "IN", "OR");
                s.getOtherExpression().add(new ParametreRequete("y.idExterne", "idSalaire", ids, "IN", "AND"));
                s.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableSalaire", Constantes.SCR_SALAIRE, "=", "AND"));
                p.getOtherExpression().add(s);
                add = true;
            }
            //Phase virement
            query = "SELECT DISTINCT y.virement FROM yvs_compta_phase_piece_virement y";
            ids = dao.loadListBySqlQuery((query += where), params);
            if (ids != null ? !ids.isEmpty() : false) {
                ParametreRequete s = new ParametreRequete(null, "idVirement", null, "IN", "OR");
                s.getOtherExpression().add(new ParametreRequete("y.idExterne", "idVirement", ids, "IN", "AND"));
                s.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableVirement", Constantes.SCR_VIREMENT, "=", "AND"));
                p.getOtherExpression().add(s);
                add = true;
            }
            //Phase reglement credit fournisseur
            query = "SELECT DISTINCT y.reglement FROM yvs_compta_phase_reglement_credit_fournisseur y";
            ids = dao.loadListBySqlQuery((query += where), params);
            if (ids != null ? !ids.isEmpty() : false) {
                ParametreRequete s = new ParametreRequete(null, "idCreditA", null, "IN", "OR");
                s.getOtherExpression().add(new ParametreRequete("y.idExterne", "idCreditA", ids, "IN", "AND"));
                s.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableCreditA", Constantes.SCR_CREDIT_ACHAT, "=", "AND"));
                p.getOtherExpression().add(s);
                add = true;
            }
            //Phase reglement credit client
            query = "SELECT DISTINCT y.reglement FROM yvs_compta_phase_reglement_credit_client y";
            ids = dao.loadListBySqlQuery((query += where), params);
            if (ids != null ? !ids.isEmpty() : false) {
                ParametreRequete s = new ParametreRequete(null, "idCreditV", null, "IN", "OR");
                s.getOtherExpression().add(new ParametreRequete("y.idExterne", "idCreditV", ids, "IN", "AND"));
                s.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableCreditV", Constantes.SCR_CREDIT_VENTE, "=", "AND"));
                p.getOtherExpression().add(s);
                add = true;
            }
            if (!add) {
                p = new ParametreRequete("y.idExterne", "idExterne", -0, "=", "AND");
            }
        }
        paginatorData.addParam(p);
        initForm = true;
        loadAllMouvement(true);
    }

    public void addParamDateOnMouvement() {
        ParametreRequete p = new ParametreRequete("y.datePaye", "dates", null, "BETWEEN", "AND");
        if (dateSearch ? dateDebutSearch != null && dateFinSearch != null && !dateFinSearch.before(dateDebutSearch) : false) {
            p = new ParametreRequete(null, "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.datePaye", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.dateMvt", "dates", dateDebutSearch, dateFinSearch, "BETWEEN", "OR"));
        }
        paginatorData.addParam(p);
        initForm = true;
        loadAllMouvement(true);
    }

    public void addParamCodeOnMouvement() {
        ParametreRequete p = new ParametreRequete("y.numero", "numero", null, "=", "AND");
        if (numPieceF != null ? numPieceF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numero", numPieceF.toUpperCase() + "%", "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numero)", "numero", "%" + numPieceF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.referenceExterne)", "numero", "%" + numPieceF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(COALESCE(y.numeroExterne, ''))", "numero", "%" + numPieceF.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginatorData.addParam(p);
        initForm = true;
        loadAllMouvement(true);
    }

    public void addParamSourceOnMouvement() {
        ParametreRequete p = new ParametreRequete("y.tableExterne", "table", null, "=", "AND");
        if (typeSearch != null ? typeSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.tableExterne", "table", typeSearch, "=", "AND");
        }
        paginatorData.addParam(p);
        initForm = true;
        loadAllMouvement(true);
    }

    public void addParamClientOnMouvement() {
        ParametreRequete p = new ParametreRequete("y.tiersExterne.codeTiers", "tiers", null, "=", "AND");
        if (clientF != null ? clientF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "tiers", clientF.toUpperCase() + "%", "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.tiersExterne.codeTiers)", "tiers", clientF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.tiersExterne.prenom)", "tiers", clientF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.tiersExterne.nom)", "tiers", clientF.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginatorData.addParam(p);
        initForm = true;
        loadAllMouvement(true);
    }

    public void addParamCaisseOnMouvement() {
        ParametreRequete p = new ParametreRequete("y.caisse", "caisse", null, "=", "AND");
        if (caisseF > 0) {
            p = new ParametreRequete("y.caisse", "caisse", new YvsBaseCaisse(caisseF), "=", "AND");
        }
        paginatorData.addParam(p);
        initForm = true;
        loadAllMouvement(true);
    }

    public void changeOperateurStatut(ValueChangeEvent ev) {
        operateurStatut = (String) ev.getNewValue();
        addParamStatutMouvement_();
    }

    public void addParamStatutMouvement(ValueChangeEvent ev) {
        statutF = (Character) ev.getNewValue();
        addParamStatutMouvement_();
    }

    public void addParamStatutMouvement_() {
        ParametreRequete p = new ParametreRequete("y.statutPiece", "statutPiece", null, operateurStatut, "AND");
        if (statutF != null) {
            p.setObjet(statutF);
        }
        paginatorData.addParam(p);
        initForm = true;
        loadAllMouvement(true);
    }

    public void addStatutCheque(ValueChangeEvent ev) {
        String stat = (String) ev.getNewValue();
        if (stat != null) {
            addParamStatutCheque(stat.charAt(0), true);
        } else {
            addParamStatutCheque(null, true); //        p0.setCompareAttribut(true);
        }
    }

    public void addParamAgenceCheque(ValueChangeEvent ev) {
        Long agence = (Long) ev.getNewValue();
        if (agence != null) {
            agenceFind = agence;
        } else {
            if (!autoriser("p_caiss_view_all_societe")) {
                agenceFind = currentAgence != null ? currentAgence.getId() : 0;
                getWarningMessage("Vous devez selectionner une agence !");
            }
        }
        loadAllMouvement(true);
    }

    public void addParamStatutCheque(Character stat, boolean load) {
        if (stat != null) {
            statutFC = stat.toString();
        }
        ParametreRequete p00 = new ParametreRequete(null, "etapes", stat, "=", "AND");
        if (stat != null) {
            if (stat.equals(Constantes.STATUT_DOC_ENCOUR)) {
                ParametreRequete p0 = new ParametreRequete(null, "etapes", stat, "=", "OR");
                ParametreRequete p01 = new ParametreRequete("y.etapeTotal", "etapes11", 0, "=", "AND");
                ParametreRequete p02 = new ParametreRequete("y.statutPiece", "statutPiece11", Constantes.STATUT_DOC_PAYER, "!=", "AND");
                ParametreRequete p1 = new ParametreRequete(null, "etapes", stat, "=", "OR");
                ParametreRequete p11 = new ParametreRequete("y.etapeTotal", "etapes12", 0, ">", "AND");
                ParametreRequete p12 = new ParametreRequete("y.etapeTotal", "statutPiece12", "y.etapeValide", "!=", "AND");
                p12.setCompareAttribut(true);
                p0.getOtherExpression().add(p01);
                p0.getOtherExpression().add(p02);
                p1.getOtherExpression().add(p11);
                p1.getOtherExpression().add(p12);
                p00.getOtherExpression().add(p0);
                p00.getOtherExpression().add(p1);
            } else if (stat.equals(Constantes.STATUT_DOC_TERMINE)) {
                ParametreRequete p0 = new ParametreRequete(null, "etapes", stat, "=", "OR");
                ParametreRequete p01 = new ParametreRequete("y.etapeTotal", "etapes01", 0, "=", "AND");
                ParametreRequete p02 = new ParametreRequete("y.statutPiece", "statutPiece1", Constantes.STATUT_DOC_PAYER, "=", "AND");
                ParametreRequete p1 = new ParametreRequete(null, "etapes", stat, "=", "AND");
                ParametreRequete p11 = new ParametreRequete("y.etapeTotal", "etapes02", 0, ">", "AND");
                ParametreRequete p13 = new ParametreRequete("y.statutPiece", "statutPiece03", Constantes.STATUT_DOC_PAYER, "=", "AND");
                ParametreRequete p12 = new ParametreRequete("y.etapeTotal", "statutPiece2", "y.etapeValide", "=", "AND");
                p12.setCompareAttribut(true);
                p0.getOtherExpression().add(p01);
                p0.getOtherExpression().add(p02);
                p1.getOtherExpression().add(p11);
                p1.getOtherExpression().add(p12);
                p1.getOtherExpression().add(p13);
                p00.getOtherExpression().add(p0);
                p00.getOtherExpression().add(p1);
            }
        }
        paginatorData.addParam(p00);
        if (load) {
            initForm = true;
            loadAllMouvement(true);
        }
    }

    public void _onSelectDistantFacture(YvsComDocVentes dv) {
        if (dv != null ? dv.getId() > 0 : false) {
            ManagedFactureVenteV2 s = (ManagedFactureVenteV2) giveManagedBean(ManagedFactureVenteV2.class);
            if (s != null) {
                s.onSelectDistant(dv);
            }
        }
    }

    public void _onSelectDistantAcompte(YvsComptaAcompteClient da) {
        if (da != null ? da.getId() > 0 : false) {
            ManagedOperationClient service = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
            if (service != null) {
                service.onSelectDistant(da);
            }
        }
    }

    public void _onSelectDistantCredit(YvsComptaReglementCreditClient da) {
        if (da != null ? da.getId() > 0 : false) {
            ManagedOperationClient service = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
            if (service != null) {
                service.onSelectDistant(da.getCredit());
            }
        }
    }

    public void onSelectDistantAchat(YvsComptaCaissePieceAchat y) {
        ManagedReglementAchat s = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
        if (s != null) {
            s.onSelectDistant(y);
        }
    }

    public void openTogeneratedPhaseReglement(YvsComptaCaissePieceVente pc) {
        String modeNotif = Constantes.MODE_PAIEMENT_ESPECE;
        if (pc.getNotifs() != null ? pc.getNotifs().getId() > 0 : false) {
            modeNotif = pc.getNotifs().getAcompte().getModel().getTypeReglement();
        }
        if (modeNotif.equals(Constantes.MODE_PAIEMENT_BANQUE)) {
            getInfoMessage("Cette pièce ne peut pas etre rattachée à des phases de reglement");
            return;
        }
        this.selectedPiece = pc;
        openDialog("dlgInitPhase");
    }

    public void openTogeneratedPhaseAcompte(YvsComptaAcompteClient pc) {
        this.selectAcompte = pc;
        openDialog("dlgInitPhase");
    }

    public void openTogeneratedPhaseCredit(YvsComptaReglementCreditClient pc) {
        this.selectCredit = pc;
        openDialog("dlgInitPhase");
    }

    public void onSelectedDistantFacture(YvsComptaMouvementCaisse y) {
        switch (y.getTableExterne()) {
            case Constantes.SCR_DIVERS: {
                if (managedDocDivers != null) {
                    YvsComptaCaissePieceDivers pc = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                    managedDocDivers.onSelectDistant(pc.getDocDivers());
                }
                break;
            }
            case Constantes.SCR_ACHAT: {
                if (managedReglementAchat != null) {
                    YvsComptaCaissePieceAchat pc = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                    managedReglementAchat._onSelectDistantFacture(pc.getAchat());
                }
                break;
            }
            case Constantes.SCR_VENTE: {
                YvsComptaCaissePieceVente pc = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                _onSelectDistantFacture(pc.getVente());
                break;
            }
            case Constantes.SCR_ACOMPTE_ACHAT: {
                if (managedReglementAchat != null) {
                    YvsComptaAcompteFournisseur pc = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                    managedReglementAchat._onSelectDistantAcompte(pc);
                }
                break;
            }
            case Constantes.SCR_ACOMPTE_VENTE: {
                YvsComptaAcompteClient pc = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                _onSelectDistantAcompte(pc);
                break;
            }
            case Constantes.SCR_CREDIT_ACHAT: {
                if (managedReglementAchat != null) {
                    YvsComptaReglementCreditFournisseur pc = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                    managedReglementAchat._onSelectDistantCredit(pc);
                }
                break;
            }
            case Constantes.SCR_CREDIT_VENTE: {
                YvsComptaReglementCreditClient pc = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                _onSelectDistantCredit(pc);
                break;
            }
            case Constantes.SCR_VIREMENT: {
                if (managedVirement != null) {
                    YvsComptaCaissePieceVirement pv = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                    managedVirement.onSelectDistant(pv);
                }
                break;
            }
        }
    }

    public void openToGenerePhaseReglement(YvsComptaMouvementCaisse y) {
        if (y == null) {
            getErrorMessage("Veuillez selectionner le document");
            return;
        }
        if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
            getErrorMessage("Vous ne pouvez pas générer les phases d'un document déjà reglé");
            return;
        }
        if (y.getCaisse() != null ? y.getCaisse().getId() < 1 : true) {
            getErrorMessage("Ce chéque n'est pas rattaché à une caisse");
            return;
        }
        selectPiece = y;
        switch (y.getTableExterne()) {
            case Constantes.SCR_DIVERS: {
                if (managedDocDivers != null) {
                    managedDocDivers.openTogeneratedPhaseDivers((YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{y.getIdExterne()}));
                }
                break;
            }
            case Constantes.SCR_ACHAT: {
                if (managedReglementAchat != null) {
                    managedReglementAchat.openTogeneratedPhaseReglement((YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{y.getIdExterne()}));
                }
                break;
            }
            case Constantes.SCR_VENTE: {
                openTogeneratedPhaseReglement((YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{y.getIdExterne()}));
                break;
            }
            case Constantes.SCR_ACOMPTE_ACHAT: {
                if (managedReglementAchat != null) {
                    managedReglementAchat.openTogeneratedPhaseAcompte((YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{y.getIdExterne()}));
                }
                break;
            }
            case Constantes.SCR_ACOMPTE_VENTE: {
                openTogeneratedPhaseAcompte((YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{y.getIdExterne()}));
                break;
            }
            case Constantes.SCR_CREDIT_ACHAT: {
                if (managedReglementAchat != null) {
                    managedReglementAchat.openTogeneratedPhaseCredit((YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{y.getIdExterne()}));
                }
                break;
            }
            case Constantes.SCR_CREDIT_VENTE: {
                openTogeneratedPhaseCredit((YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{y.getIdExterne()}));
                break;
            }
            case Constantes.SCR_VIREMENT: {
                if (managedVirement != null) {
                    managedVirement.openTogeneratedPhaseVirement((YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{y.getIdExterne()}));
                }
                break;
            }
        }
    }

    public String libPhases(YvsComptaMouvementCaisse y) {
        switch (y.getTableExterne()) {
            case Constantes.SCR_ACHAT: {
                YvsComptaCaissePieceAchat pc = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                return pc != null ? pc.getLibphases() : "";
            }
            case Constantes.SCR_VENTE: {
                YvsComptaCaissePieceVente pc = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                return pc != null ? pc.getLibphases() : "";
            }
            case Constantes.SCR_ACOMPTE_ACHAT: {
                YvsComptaAcompteFournisseur pc = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                return pc != null ? pc.getLibphases() : "";
            }
            case Constantes.SCR_ACOMPTE_VENTE: {
                YvsComptaAcompteClient pc = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                return pc != null ? pc.getLibphases() : "";
            }
            case Constantes.SCR_VIREMENT: {
                YvsComptaCaissePieceVirement pv = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                return pv != null ? pv.getLibphases() : "";
            }
        }
        return "0/0";
    }

    public void generePhaseReglement() {
        switch (selectPiece.getTableExterne()) {
            case Constantes.SCR_DIVERS: {
                if (managedDocDivers != null) {
                    managedDocDivers.generatedPhaseDivers();
                }
                break;
            }
            case Constantes.SCR_ACHAT: {
                if (managedReglementAchat != null) {
                    managedReglementAchat.generatedPhaseReglement();
                }
                break;
            }
            case Constantes.SCR_VENTE: {
                generatedPhaseReglement();
                break;
            }
            case Constantes.SCR_ACOMPTE_ACHAT: {
                if (managedReglementAchat != null) {
                    managedReglementAchat.generatedPhaseAcompte();
                }
                break;
            }
            case Constantes.SCR_ACOMPTE_VENTE: {
                generatedPhaseAcompte();
                break;
            }
            case Constantes.SCR_CREDIT_ACHAT: {
                if (managedReglementAchat != null) {
                    managedReglementAchat.generatedPhaseCredit();
                }
                break;
            }
            case Constantes.SCR_CREDIT_VENTE: {
                generatedPhaseCredit();
                break;
            }
            case Constantes.SCR_VIREMENT: {
                if (managedVirement != null) {
                    managedVirement.generatedPhaseVirement();
                }
                break;
            }
        }
        selectPiece = (YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findById", new String[]{"id"}, new Object[]{selectPiece.getId()});
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
            update("data_list_piece_cheque");
        }
    }

    public void annulePaiement() {
        switch (selectPiece.getTableExterne()) {
            case Constantes.SCR_DIVERS: {
                if (managedDocDivers != null) {
                    if (managedDocDivers.getPieceCD().getModePaiement().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                        if (managedDocDivers.getPieceCD().getPhasesReglement() != null ? !managedDocDivers.getPieceCD().getPhasesReglement().isEmpty() : false) {
                            getErrorMessage("L'annulation de cette pièce passe par l'annulation des phases");
                            return;
                        }
                    }
                    managedDocDivers.suspendOnePieceCaisse(managedDocDivers.getPieceCD().getDocDivers(), managedDocDivers.getPieceCD(), true);
                    selectPiece.setStatutPiece(managedDocDivers.getPieceCD().getStatutPiece());
                    int idx = mouvements.indexOf(selectPiece);
                    if (idx > -1) {
                        mouvements.set(idx, selectPiece);
                    }
                    update("head_form_suivi_prd");
                    update("head_corp_form_suivi_prd");
                }
                break;
            }
            case Constantes.SCR_ACHAT: {
                if (managedReglementAchat != null) {
                    if (managedReglementAchat.getPieceAchat().getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                        if (managedReglementAchat.getPieceAchat().getPhasesReglement() != null ? !managedReglementAchat.getPieceAchat().getPhasesReglement().isEmpty() : false) {
                            getErrorMessage("L'annulation de cette pièce passe par l'annulation des phases");
                            return;
                        }
                    }
                    managedReglementAchat.reglerPieceTresorerie(null, managedReglementAchat.getPieceAchat(), "", true);
                    selectPiece.setStatutPiece(managedReglementAchat.getPieceAchat().getStatutPiece());
                    int idx = mouvements.indexOf(selectPiece);
                    if (idx > -1) {
                        mouvements.set(idx, selectPiece);
                    }
                    update("head_form_suivi_pra");
                    update("head_corp_form_suivi_pra");
                }
                break;
            }
            case Constantes.SCR_VENTE: {
                if (selectedPiece.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    if (selectedPiece.getPhasesReglement() != null ? !selectedPiece.getPhasesReglement().isEmpty() : false) {
                        getErrorMessage("L'annulation de cette pièce passe par l'annulation des phases");
                        return;
                    }
                }
                reglerPieceTresorerie(null, selectedPiece, "", true);
                selectPiece.setStatutPiece(selectedPiece.getStatutPiece());
                int idx = mouvements.indexOf(selectPiece);
                if (idx > -1) {
                    mouvements.set(idx, selectPiece);
                }
                update("head_form_suivi_prv");
                update("head_corp_form_suivi_prv");
                break;
            }
            case Constantes.SCR_ACOMPTE_ACHAT: {
                if (managedReglementAchat != null) {
                    if (managedReglementAchat.getSelectAcompte().getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                        if (managedReglementAchat.getSelectAcompte().getPhasesReglement() != null ? !managedReglementAchat.getSelectAcompte().getPhasesReglement().isEmpty() : false) {
                            getErrorMessage("L'annulation de cette pièce passe par l'annulation des phases");
                            return;
                        }
                    }
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        w.annulerAcompte(managedReglementAchat.getSelectAcompte(), false);
                    }
                    selectPiece.setStatutPiece(managedReglementAchat.getSelectAcompte().getStatut());
                    int idx = mouvements.indexOf(selectPiece);
                    if (idx > -1) {
                        mouvements.set(idx, selectPiece);
                    }
                    update("head_form_suivi_paa");
                    update("head_corp_form_suivi_paa");
                }
                break;
            }
            case Constantes.SCR_ACOMPTE_VENTE: {
                if (getSelectAcompte().getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    if (getSelectAcompte().getPhasesReglement() != null ? !getSelectAcompte().getPhasesReglement().isEmpty() : false) {
                        getErrorMessage("L'annulation de cette pièce passe par l'annulation des phases");
                        return;
                    }
                }
                ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                if (w != null) {
                    w.annulerAcompte(getSelectAcompte(), false);
                }
                selectPiece.setStatutPiece(getSelectAcompte().getStatut());
                int idx = mouvements.indexOf(selectPiece);
                if (idx > -1) {
                    mouvements.set(idx, selectPiece);
                }
                update("head_form_suivi_pav");
                update("head_corp_form_suivi_pav");
                break;
            }
            case Constantes.SCR_CREDIT_ACHAT: {
                if (managedReglementAchat != null) {
                    if (managedReglementAchat.getSelectCredit().getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                        if (managedReglementAchat.getSelectCredit().getPhasesReglement() != null ? !managedReglementAchat.getSelectCredit().getPhasesReglement().isEmpty() : false) {
                            getErrorMessage("L'annulation de cette pièce passe par l'annulation des phases");
                            return;
                        }
                    }
                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                    if (w != null) {
                        w.annulerReglement(managedReglementAchat.getSelectCredit().getCredit(), managedReglementAchat.getSelectCredit(), false);
                    }
                    selectPiece.setStatutPiece(managedReglementAchat.getSelectCredit().getStatut());
                    int idx = mouvements.indexOf(selectPiece);
                    if (idx > -1) {
                        mouvements.set(idx, selectPiece);
                    }
                    update("head_form_suivi_pca");
                    update("head_corp_form_suivi_pca");
                }
                break;
            }
            case Constantes.SCR_CREDIT_VENTE: {
                if (getSelectCredit().getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    if (getSelectCredit().getPhasesReglement() != null ? !getSelectCredit().getPhasesReglement().isEmpty() : false) {
                        getErrorMessage("L'annulation de cette pièce passe par l'annulation des phases");
                        return;
                    }
                }
                ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                if (w != null) {
                    w.annulerReglement(getSelectCredit().getCredit(), getSelectCredit(), false);
                }
                selectPiece.setStatutPiece(getSelectCredit().getStatut());
                int idx = mouvements.indexOf(selectPiece);
                if (idx > -1) {
                    mouvements.set(idx, selectPiece);
                }
                update("head_form_suivi_pcv");
                update("head_corp_form_suivi_pcv");
                break;
            }
            case Constantes.SCR_VIREMENT: {
                if (managedVirement != null) {
                    if (managedVirement.getEntity().getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                        if (managedVirement.getEntity().getPhasesReglement() != null ? !managedVirement.getEntity().getPhasesReglement().isEmpty() : false) {
                            getErrorMessage("L'annulation de cette pièce passe par l'annulation des phases");
                            return;
                        }
                    }
                    managedVirement.changeStatutPiece(managedVirement.getEntity(), true);
                    selectPiece.setStatutPiece(managedVirement.getEntity().getStatutPiece());
                    int idx = mouvements.indexOf(selectPiece);
                    if (idx > -1) {
                        mouvements.set(idx, selectPiece);
                    }
                    update("corps_form_suivi_pvir");
                    update("header_form_suiviphaseVirement");
                }
                break;
            }
        }
    }

    public boolean isComptabilise() {
        switch (selectPiece.getTableExterne()) {
            case Constantes.SCR_DIVERS: {
                if (managedDocDivers != null) {
                    if (managedDocDivers.getSelectPhaseDivers() != null ? managedDocDivers.getSelectPhaseDivers().getId() > 0 : false) {
                        return dao.isComptabilise(managedDocDivers.getSelectPhaseDivers().getId(), Constantes.SCR_PHASE_CAISSE_DIVERS);
                    } else {
                        if (managedDocDivers.getPieceCD() != null ? managedDocDivers.getPieceCD().getId() > 0 : false) {
                            return dao.isComptabilise(managedDocDivers.getPieceCD().getId(), Constantes.SCR_CAISSE_DIVERS);
                        }
                    }
                }
                break;
            }
            case Constantes.SCR_ACHAT: {
                if (managedReglementAchat != null) {
                    if (managedReglementAchat.getSelectPhaseAchat() != null ? managedReglementAchat.getSelectPhaseAchat().getId() > 0 : false) {
                        return dao.isComptabilise(managedReglementAchat.getSelectPhaseAchat().getId(), Constantes.SCR_PHASE_CAISSE_ACHAT);
                    } else {
                        if (managedReglementAchat.getPieceAchat() != null ? managedReglementAchat.getPieceAchat().getId() > 0 : false) {
                            return dao.isComptabilise(managedReglementAchat.getPieceAchat().getId(), Constantes.SCR_CAISSE_ACHAT);
                        }
                    }
                }
                break;
            }
            case Constantes.SCR_VENTE: {
                if (selectPhaseVente != null ? selectPhaseVente.getId() > 0 : false) {
                    return dao.isComptabilise(selectPhaseVente.getId(), Constantes.SCR_PHASE_CAISSE_VENTE);
                } else {
                    if (selectedPiece != null ? selectedPiece.getId() > 0 : false) {
                        return dao.isComptabilise(selectedPiece.getId(), Constantes.SCR_CAISSE_VENTE);
                    }
                }
            }
            case Constantes.SCR_ACOMPTE_ACHAT: {
                if (managedReglementAchat != null) {
                    if (managedReglementAchat.getSelectPhaseAcompteAchat() != null ? managedReglementAchat.getSelectPhaseAcompteAchat().getId() > 0 : false) {
                        return dao.isComptabilise(managedReglementAchat.getSelectPhaseAcompteAchat().getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT);
                    } else {
                        if (managedReglementAchat.getSelectAcompte() != null ? managedReglementAchat.getSelectAcompte().getId() > 0 : false) {
                            return dao.isComptabilise(managedReglementAchat.getSelectAcompte().getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT);
                        }
                    }
                }
                break;
            }
            case Constantes.SCR_ACOMPTE_VENTE: {
                if (selectPhaseAcompteVente != null ? selectPhaseAcompteVente.getId() > 0 : false) {
                    return dao.isComptabilise(selectPhaseAcompteVente.getId(), Constantes.SCR_PHASE_ACOMPTE_VENTE);
                } else {
                    if (selectAcompte != null ? selectAcompte.getId() > 0 : false) {
                        return dao.isComptabilise(selectAcompte.getId(), Constantes.SCR_ACOMPTE_VENTE);
                    }
                }
            }
            case Constantes.SCR_CREDIT_ACHAT: {
                if (managedReglementAchat != null) {
                    if (managedReglementAchat.getSelectPhaseCreditAchat() != null ? managedReglementAchat.getSelectPhaseCreditAchat().getId() > 0 : false) {
                        return dao.isComptabilise(managedReglementAchat.getSelectPhaseCreditAchat().getId(), Constantes.SCR_PHASE_CREDIT_ACHAT);
                    } else {
                        if (managedReglementAchat.getSelectCredit() != null ? managedReglementAchat.getSelectCredit().getId() > 0 : false) {
                            return dao.isComptabilise(managedReglementAchat.getSelectCredit().getId(), Constantes.SCR_PHASE_CREDIT_ACHAT);
                        }
                    }
                }
                break;
            }
            case Constantes.SCR_CREDIT_VENTE: {
                if (selectPhaseCreditVente != null ? selectPhaseCreditVente.getId() > 0 : false) {
                    return dao.isComptabilise(selectPhaseCreditVente.getId(), Constantes.SCR_PHASE_CREDIT_VENTE);
                } else {
                    if (selectCredit != null ? selectCredit.getId() > 0 : false) {
                        return dao.isComptabilise(selectCredit.getId(), Constantes.SCR_CREDIT_VENTE);
                    }
                }
            }
            case Constantes.SCR_VIREMENT: {
                if (managedVirement != null) {
                    if (managedVirement.getSelectPhaseVirement() != null ? managedVirement.getSelectPhaseVirement().getId() > 0 : false) {
                        return dao.isComptabilise(managedVirement.getSelectPhaseVirement().getId(), Constantes.SCR_PHASE_VIREMENT);
                    } else {
                        if (managedVirement.getEntity() != null ? managedVirement.getEntity().getId() > 0 : false) {
                            return dao.isComptabilise(managedVirement.getEntity().getId(), Constantes.SCR_VIREMENT);
                        }
                    }
                }
                break;
            }
        }
        return false;
    }

    public void setCurrentPhase(Object phase) {
        switch (selectPiece.getTableExterne()) {
            case Constantes.SCR_DIVERS: {
                objectPhaseOk = ((YvsComptaPhasePieceDivers) phase).getPhaseOk();
                if (managedDocDivers != null) {
                    managedDocDivers.setSelectPhaseDivers((YvsComptaPhasePieceDivers) phase);
                }
                break;
            }
            case Constantes.SCR_ACHAT: {
                objectPhaseOk = ((YvsComptaPhasePieceAchat) phase).getPhaseOk();
                if (managedReglementAchat != null) {
                    managedReglementAchat.setSelectPhaseAchat((YvsComptaPhasePieceAchat) phase);
                }
                break;
            }
            case Constantes.SCR_VENTE: {
                objectPhaseOk = ((YvsComptaPhasePiece) phase).getPhaseOk();
                setSelectPhaseVente((YvsComptaPhasePiece) phase);
                break;
            }
            case Constantes.SCR_ACOMPTE_ACHAT: {
                objectPhaseOk = ((YvsComptaPhaseAcompteAchat) phase).getPhaseOk();
                if (managedReglementAchat != null) {
                    managedReglementAchat.setSelectPhaseAcompteAchat((YvsComptaPhaseAcompteAchat) phase);
                }
                break;
            }
            case Constantes.SCR_ACOMPTE_VENTE: {
                objectPhaseOk = ((YvsComptaPhaseAcompteVente) phase).getPhaseOk();
                setSelectPhaseAcompteVente((YvsComptaPhaseAcompteVente) phase);
                break;
            }
            case Constantes.SCR_CREDIT_VENTE: {
                objectPhaseOk = ((YvsComptaPhaseReglementCreditClient) phase).getPhaseOk();
                setSelectPhaseCreditVente((YvsComptaPhaseReglementCreditClient) phase);
                break;
            }
            case Constantes.SCR_CREDIT_ACHAT: {
                objectPhaseOk = ((YvsComptaPhaseReglementCreditFournisseur) phase).getPhaseOk();
                if (managedReglementAchat != null) {
                    managedReglementAchat.setSelectPhaseCreditAchat((YvsComptaPhaseReglementCreditFournisseur) phase);
                }
                break;
            }
            case Constantes.SCR_VIREMENT: {
                objectPhaseOk = ((YvsComptaPhasePieceVirement) phase).getPhaseOk();
                if (managedVirement != null) {
                    managedVirement.setSelectPhaseVirement((YvsComptaPhasePieceVirement) phase);
                }
                break;
            }
        }
    }

    public void comptabiliserPhase() {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            switch (selectPiece.getTableExterne()) {
                case Constantes.SCR_DIVERS: {
                    if (managedDocDivers != null) {
                        if (managedDocDivers.getSelectPhaseDivers() != null ? managedDocDivers.getSelectPhaseDivers().getId() > 0 : false) {
                            w.comptabiliserPhaseCaisseDivers(managedDocDivers.getSelectPhaseDivers(), true, true);
                        } else {
                            w.comptabiliserCaisseDivers(managedDocDivers.getPieceCD(), true, true);
                        }
                        update("head_form_suivi_prd");
                        update("head_corp_form_suivi_prd");
                        update("table_ppte_valid_phase_od");
                    }
                    break;
                }
                case Constantes.SCR_ACHAT: {
                    if (managedReglementAchat != null) {
                        if (managedReglementAchat.getSelectPhaseAchat() != null ? managedReglementAchat.getSelectPhaseAchat().getId() > 0 : false) {
                            w.comptabiliserPhaseCaisseAchat(managedReglementAchat.getSelectPhaseAchat(), true, true);
                        } else {
                            w.comptabiliserCaisseAchat(managedReglementAchat.getPieceAchat(), true, true);
                        }
                        update("head_form_suivi_pra");
                        update("head_corp_form_suivi_pra");
                        update("table_ppte_valid_phase_fa");
                    }
                    break;
                }
                case Constantes.SCR_VENTE: {
                    if (selectPhaseVente != null ? selectPhaseVente.getId() > 0 : false) {
                        w.comptabiliserPhaseCaisseVente(selectPhaseVente, true, true);
                    } else {
                        w.comptabiliserCaisseVente(selectedPiece, true, true);
                    }
                    update("head_form_suivi_prv");
                    update("head_corp_form_suivi_prv");
                    update("table_ppte_valid_phase_fv");
                    break;
                }
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    if (managedReglementAchat != null) {
                        if (managedReglementAchat.getSelectPhaseAcompteAchat() != null ? managedReglementAchat.getSelectPhaseAcompteAchat().getId() > 0 : false) {
                            w.comptabiliserPhaseAcompteAchat(managedReglementAchat.getSelectPhaseAcompteAchat(), true, true);
                        } else {
                            w.comptabiliserAcompteFournisseur(managedReglementAchat.getSelectAcompte(), true, true);
                        }
                        update("head_form_suivi_paa");
                        update("head_corp_form_suivi_paa");
                        update("table_ppte_valid_phase_aa");
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_VENTE: {
                    if (selectPhaseAcompteVente != null ? selectPhaseAcompteVente.getId() > 0 : false) {
                        w.comptabiliserPhaseAcompteVente(selectPhaseAcompteVente, true, true);
                    } else {
                        w.comptabiliserAcompteClient(selectAcompte, true, true);
                    }
                    update("head_form_suivi_pav");
                    update("head_corp_form_suivi_pav");
                    update("table_ppte_valid_phase_av");
                    break;
                }
                case Constantes.SCR_CREDIT_ACHAT: {
                    if (managedReglementAchat != null) {
                        if (managedReglementAchat.getSelectPhaseCreditAchat() != null ? managedReglementAchat.getSelectPhaseCreditAchat().getId() > 0 : false) {
                            w.comptabiliserPhaseCaisseCreditAchat(managedReglementAchat.getSelectPhaseCreditAchat(), true, true);
                        } else {
                            w.comptabiliserCaisseCreditAchat(managedReglementAchat.getSelectCredit(), true, true);
                        }
                        update("head_form_suivi_pca");
                        update("head_corp_form_suivi_pca");
                        update("table_ppte_valid_phase_ca");
                    }
                    break;
                }
                case Constantes.SCR_CREDIT_VENTE: {
                    if (selectPhaseCreditVente != null ? selectPhaseCreditVente.getId() > 0 : false) {
                        w.comptabiliserPhaseCaisseCreditVente(selectPhaseCreditVente, true, true);
                    } else {
                        w.comptabiliserCaisseCreditVente(selectCredit, true, true);
                    }
                    update("head_form_suivi_pcv");
                    update("head_corp_form_suivi_pcv");
                    update("table_ppte_valid_phase_cv");
                    break;
                }
                case Constantes.SCR_VIREMENT: {
                    if (managedVirement != null) {
                        if (managedVirement.getSelectPhaseVirement() != null ? managedVirement.getSelectPhaseVirement().getId() > 0 : false) {
                            w.comptabiliserPhaseCaisseVirement(managedVirement.getSelectPhaseVirement(), true, true);
                        } else {
                            w.comptabiliserCaisseVirement(managedVirement.getEntity(), true, true);
                        }
                        update("corps_form_suivi_pvir");
                        update("header_form_suiviphaseVirement");
                        update("table_ppte_valid_phase_vi");
                    }
                    break;
                }
            }
        }
    }

    public void unComptabiliserPhase() {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            switch (selectPiece.getTableExterne()) {
                case Constantes.SCR_DIVERS: {
                    if (managedDocDivers != null) {
                        if (managedDocDivers.getSelectPhaseDivers() != null ? managedDocDivers.getSelectPhaseDivers().getId() > 0 : false) {
                            w.unComptabiliserPhaseCaisseDivers(managedDocDivers.getSelectPhaseDivers(), true, true);
                        } else {
                            w.unComptabiliserCaisseDivers(managedDocDivers.getPieceCD(), true);
                        }
                        update("head_form_suivi_prd");
                        update("head_corp_form_suivi_prd");
                    }
                    break;
                }
                case Constantes.SCR_ACHAT: {
                    if (managedReglementAchat != null) {
                        if (managedReglementAchat.getSelectPhaseAchat() != null ? managedReglementAchat.getSelectPhaseAchat().getId() > 0 : false) {
                            w.unComptabiliserPhaseCaisseAchat(managedReglementAchat.getSelectPhaseAchat(), true, true);
                        } else {
                            w.unComptabiliserCaisseAchat(managedReglementAchat.getPieceAchat(), true);
                        }
                        update("head_form_suivi_pra");
                        update("head_corp_form_suivi_pra");
                    }
                    break;
                }
                case Constantes.SCR_VENTE: {
                    if (selectPhaseVente != null ? selectPhaseVente.getId() > 0 : false) {
                        w.unComptabiliserPhaseCaisseVente(selectPhaseVente, true, true);
                    } else {
                        w.unComptabiliserCaisseVente(selectedPiece, true);
                    }
                    update("head_form_suivi_prv");
                    update("head_corp_form_suivi_prv");
                    break;
                }
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    if (managedReglementAchat != null) {
                        if (managedReglementAchat.getSelectPhaseAcompteAchat() != null ? managedReglementAchat.getSelectPhaseAcompteAchat().getId() > 0 : false) {
                            w.unComptabiliserPhaseAcompteAchat(managedReglementAchat.getSelectPhaseAcompteAchat(), true);
                        } else {
                            w.unComptabiliserAcompteFournisseur(managedReglementAchat.getSelectAcompte(), true);
                        }
                        update("head_form_suivi_paa");
                        update("head_corp_form_suivi_paa");
                    }
                    break;
                }
                case Constantes.SCR_ACOMPTE_VENTE: {
                    if (selectPhaseAcompteVente != null ? selectPhaseAcompteVente.getId() > 0 : false) {
                        w.unComptabiliserPhaseAcompteVente(selectPhaseAcompteVente, true);
                    } else {
                        w.unComptabiliserAcompteClient(selectAcompte, true);
                    }
                    update("head_form_suivi_pav");
                    update("head_corp_form_suivi_pav");
                    break;
                }
                case Constantes.SCR_CREDIT_ACHAT: {
                    if (managedReglementAchat != null) {
                        if (managedReglementAchat.getSelectPhaseCreditAchat() != null ? managedReglementAchat.getSelectPhaseCreditAchat().getId() > 0 : false) {
                            w.unComptabiliserPhaseCaisseCreditAchat(managedReglementAchat.getSelectPhaseCreditAchat(), true);
                        } else {
                            w.unComptabiliserCaisseCreditAchat(managedReglementAchat.getSelectCredit(), true);
                        }
                        update("head_form_suivi_paa");
                        update("head_corp_form_suivi_paa");
                    }
                    break;
                }
                case Constantes.SCR_CREDIT_VENTE: {
                    if (selectPhaseAcompteVente != null ? selectPhaseAcompteVente.getId() > 0 : false) {
                        w.unComptabiliserPhaseCaisseCreditVente(selectPhaseCreditVente, true);
                    } else {
                        w.unComptabiliserCaisseCreditVente(selectCredit, true);
                    }
                    update("head_form_suivi_pcv");
                    update("head_corp_form_suivi_pcv");
                    break;
                }
                case Constantes.SCR_VIREMENT: {
                    if (managedVirement != null) {
                        if (managedVirement.getSelectPhaseVirement() != null ? managedVirement.getSelectPhaseVirement().getId() > 0 : false) {
                            w.unComptabiliserPhaseCaisseVirement(managedVirement.getSelectPhaseVirement(), true);
                        } else {
                            w.unComptabiliserCaisseVirement(managedVirement.getEntity(), true);
                        }
                        update("corps_form_suivi_pvir");
                        update("header_form_suiviphaseVirement");
                    }
                    break;
                }
            }
        }
    }

    public void generatedPhaseReglement() {
        if (selectedPiece == null) {
            return;
        }
        if (selectedPiece.getVente() == null) {
            return;
        }
        if (selectedPiece != null && !selectedPiece.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && selectedPiece.getVente().getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (selectedPiece.getPhasesReglement() != null) {
                try {
                    if (selectedPiece.getCaisse() != null ? selectedPiece.getCaisse().getId() < 1 : true) {
                        getErrorMessage("Vous devez precisez la caisse qui a été mouvementé");
                        return;
                    }
                    for (YvsComptaPhasePiece ph : selectedPiece.getPhasesReglement()) {
                        ph.setAuthor(currentUser);
                        dao.delete(ph);
                    }
                    selectedPiece.getPhasesReglement().clear();
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{selectedPiece.getModel(), false});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhasePiece pp;
                    if (selectedPiece.getPhasesReglement() == null) {
                        selectedPiece.setPhasesReglement(new ArrayList<YvsComptaPhasePiece>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhasePiece(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        pp.setStatut(Constantes.STATUT_DOC_ATTENTE);
                        pp.setPieceVente(selectedPiece);
                        if (selectedPiece.getCaisse() != null ? selectedPiece.getCaisse().getId() > 0 : false) {
                            pp.setCaisse(selectedPiece.getCaisse());
                        }
                        pp = (YvsComptaPhasePiece) dao.save1(pp);
                        selectedPiece.getPhasesReglement().add(pp);
                    }
                    update("table_list_piece_cheque");
                    succes();
                } catch (Exception ex) {
                    getErrorMessage("Impossible de réaliser cette action !");
                }
            }
        } else {
            if (selectedPiece.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce est déjà payé !");
            }
            if (!selectedPiece.getVente().getStatut().equals(Constantes.ETAT_VALIDE)) {
                getErrorMessage("Le document de vente n'a pas été validé!");
            }
            if (selectedPiece == null) {
                getErrorMessage("Aucune pièce de règlement n'a été selectionné !");

            }
        }
    }

    public void generatedPhaseAcompte() {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            w.generatedPhaseAcompte(selectAcompte);
        }
    }

    public void generatedPhaseCredit() {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            w.generatedPhaseCredit(selectCredit);
        }
    }

    private YvsBaseModeReglement giveModeReg(int id) {
        if (id > 0) {
            ManagedModeReglement service = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class
            );
            if (service
                    != null) {
                int idx = service.getModes().indexOf(new YvsBaseModeReglement((long) id));
                if (idx >= 0) {
                    return service.getModes().get(idx);
                }
            }
        }
        return null;
    }

    public void saveNewPc() {
        //controle la conformité du formulaire
        if (controlePiece()) {
            YvsComptaCaissePieceVente pc = new YvsComptaCaissePieceVente();
            pc.setAuthor(currentUser);

            if (pieceAvance.getCaisse().getId() > 0) {
                ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class
                );
                if (service
                        != null) {
                    int idx = service.getCaisses().indexOf(new YvsBaseCaisse(pieceAvance.getCaisse().getId()));
                    if (idx >= 0) {
                        pc.setCaisse(service.getCaisses().get(idx));
                    }
                }
            }
//            pc.setClient(new YvsComClient(pieceAvance.getClients().getId(), pieceAvance.getClients().getCodeClient(), pieceAvance.getClients().getNom(), pieceAvance.getClients().getPrenom()));
            pc.setDatePaiement(null);
            pc.setDatePaimentPrevu(pieceAvance.getDatePaiementPrevu());
            pc.setDatePiece(new Date());
            pc.setMontant(pieceAvance.getMontant());
            pc.setNew_(true);
            pc.setNumeroPiece(pieceAvance.getNumPiece());
            pc.setNote(pieceAvance.getDescription());
            pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            pc.setReferenceExterne(pieceAvance.getNumRefExterne());
            pc.setModel(giveModeReg(pieceAvance.getMode().getId()));
            if (pieceAvance.getId() <= 0) {
                pc.setId(null);
                pc = (YvsComptaCaissePieceVente) dao.save1(pc);
                pc.setNew_(true);
                pieceAvance.setId(pc.getId());
                reglements.add(0, pc);
            } else {
                pc.setId(pieceAvance.getId());
                dao.update(pc);
                if (reglements.contains(pc)) {
                    reglements.set(reglements.indexOf(pc), pc);
                }
            }
            succes();
        }
    }

    public void cancelByExtournEtape() {
        switch (selectPiece.getTableExterne()) {
            case Constantes.SCR_DIVERS: {
                if (managedDocDivers != null) {
                    managedDocDivers.setMontantRetour(montantRetour);
                    cancelValidEtapesDivers(managedDocDivers.getSelectPhaseDivers(), true);
                }
                break;
            }
            case Constantes.SCR_ACHAT: {
                if (managedReglementAchat != null) {
                    managedReglementAchat.setMontantRetour(montantRetour);
                    cancelValidEtapesAchat(managedReglementAchat.getSelectPhaseAchat(), true);
                }
                break;
            }
            case Constantes.SCR_VENTE: {
                cancelValidEtapesVente(selectPhaseVente, true);
                break;
            }
            case Constantes.SCR_ACOMPTE_VENTE: {
                cancelValidEtapesAcompteVente(selectPhaseAcompteVente);
                break;
            }
        }
    }

    public void cancelExtourne() {
        if (selectPhaseVente != null) {
            if (selectPhaseVente.getPieceVente().getVerouille()) {
                getErrorMessage("Le chèque concerné par cette phase à été vérouillé !");
                return;
            }
            champ = new String[]{"id", "table"};
            val = new Object[]{selectPhaseVente.getId(), Constantes.SCR_PHASE_CAISSE_VENTE};
            nameQueri = "YvsComptaContentJournal.findRevertPieceByExterneOrder";
            YvsComptaPiecesComptable p = (YvsComptaPiecesComptable) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (p != null) {
                try {
                    dao.delete(p);
                    afterValidePhaseCheque(selectPhaseVente);
                    getInfoMessage("La dernière extourne a été annulé avec succès !");
                } catch (Exception ex) {
                    getErrorMessage("Suppression de la pièce impossible !");
                    getException("Erreur delete piece", ex);
                }
            } else {
                getWarningMessage("Aucune extourne de ce chèque n'a été trouvé !");
            }
        }
    }

    public void cancelValidEtapesVente(YvsComptaPhasePiece pp, boolean retour) {
        if (!controleFiche(pp.getDateValider())) {
            return;
        }
        if (pp.getPieceVente().getVerouille()) {
            getErrorMessage("Le chèque concerné par cette phase à été vérouillé !");
            return;
        }
        pp.setDateValider(null);
        cancelValidEtapesPieces(pp, retour);
        selectPiece.setStatutPiece(pieceReglement.getStatutPiece());
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
        update("head_corp_form_suivi_prv");
    }

    public void cancelValidEtapesAcompteVente(YvsComptaPhaseAcompteVente pp) {
        if (!controleFiche(pp.getDateValider())) {
            return;
        }
        cancelValidEtapesAcompte(pp);
        selectPiece.setStatutPiece(selectAcompte.getStatut());
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
        update("head_corp_form_suivi_pav");
    }

    public void cancelValidEtapesCreditVente(YvsComptaPhaseReglementCreditClient pp) {
        if (!controleFiche(pp.getDateValider())) {
            return;
        }
        cancelValidEtapesCredit(pp);
        selectPiece.setStatutPiece(selectCredit.getStatut());
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
    }

    public void cancelValidEtapesAchat(YvsComptaPhasePieceAchat pp, boolean retour) {
        if (managedReglementAchat != null) {
            if (!controleFiche(pp.getDateValider())) {
                return;
            }
            managedReglementAchat.cancelValidEtapesPieces(pp, retour);
            selectPiece.setStatutPiece(managedReglementAchat.getPieceAchat().getStatutPiece());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
            updateMouvement();
        }
        update("head_corp_form_suivi_pra");
    }

    public void cancelValidEtapesAcompteAchat(YvsComptaPhaseAcompteAchat pp, boolean retour) {
        if (managedReglementAchat != null) {
            if (!controleFiche(pp.getDateValider())) {
                return;
            }
            managedReglementAchat.cancelValidEtapesAcompte(pp, retour);
            updateMouvement();
            selectPiece.setStatutPiece(managedReglementAchat.getSelectAcompte().getStatut());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
        update("head_corp_form_suivi_paa");
    }

    public void cancelValidEtapesCreditAchat(YvsComptaPhaseReglementCreditFournisseur pp, boolean retour) {
        if (managedReglementAchat != null) {
            if (!controleFiche(pp.getDateValider())) {
                return;
            }
            managedReglementAchat.cancelValidEtapesCredit(pp, retour);
            selectPiece.setStatutPiece(managedReglementAchat.getSelectCredit().getStatut());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
    }

    public void cancelValidEtapesVirement(YvsComptaPhasePieceVirement pp) {
        if (managedVirement != null) {
            if (!controleFiche(pp.getDateValider())) {
                return;
            }
            managedVirement.cancelValidEtapesPieces(pp);
            selectPiece.setStatutPiece(managedVirement.getEntity().getStatutPiece());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
    }

    public void cancelValidEtapesDivers(YvsComptaPhasePieceDivers pp, boolean retour) {
        if (managedDocDivers != null) {
            if (!controleFiche(pp.getDateValider())) {
                return;
            }
            managedDocDivers.cancelValidEtapesPieces(pp, retour);
            selectPiece.setStatutPiece(managedDocDivers.getPieceCD().getStatutPiece());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
            updateMouvement();
        }
        update("head_corp_form_suivi_prd");
    }

    public void cancelAllEtapesVente() {
        cancelAllEtapesPieces();
        selectPiece.setStatutPiece(pieceReglement.getStatutPiece());
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
        update("head_corp_form_suivi_prv");
    }

    public void cancelAllEtapesAcompteVente() {
        cancelAllEtapesAcompte();
        selectPiece.setStatutPiece(selectAcompte.getStatut());
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
        update("head_corp_form_suivi_pav");
    }

    public void cancelAllEtapesCreditVente() {
        cancelAllEtapesCredit();
        selectPiece.setStatutPiece(selectCredit.getStatut());
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
    }

    public void cancelAllEtapesAchat() {
        if (managedReglementAchat != null) {
            managedReglementAchat.cancelAllEtapesPieces();
            selectPiece.setStatutPiece(managedReglementAchat.getPieceAchat().getStatutPiece());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
        update("head_corp_form_suivi_pra");
    }

    public void cancelAllEtapesAcompteAchat() {
        if (managedReglementAchat != null) {
            managedReglementAchat.cancelAllEtapesAcompte();
            selectPiece.setStatutPiece(managedReglementAchat.getSelectAcompte().getStatut());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
        update("head_corp_form_suivi_paa");
    }

    public void cancelAllEtapesCreditAchat() {
        if (managedReglementAchat != null) {
            managedReglementAchat.cancelAllEtapesCredit();
            selectPiece.setStatutPiece(managedReglementAchat.getSelectCredit().getStatut());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
    }

    public void cancelAllEtapesVirement() {
        if (managedVirement != null) {
            managedVirement.cancelAllEtapesPieces();
            selectPiece.setStatutPiece(managedVirement.getEntity().getStatutPiece());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
    }

    public void cancelAllEtapesDivers() {
        if (managedDocDivers != null) {
            managedDocDivers.cancelAllEtapesPieces();
            selectPiece.setStatutPiece(managedDocDivers.getPieceCD().getStatutPiece());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
    }

    public boolean checkIfRetourEtape(YvsComptaPhasePiece pp) {
        if (pp.getPhaseOk()) {
            return false;
        }
        if (pp.getStatut().equals(Constantes.STATUT_DOC_ANNULE)) {
            return false;
        }
        champ = new String[]{"mode", "numero"};
        val = new Object[]{pp.getPhaseReg().getModeReglement(), pp.getPhaseReg().getNumeroPhase()};
        nameQueri = "YvsComptaPhaseReglement.findPrecByMode";
        YvsComptaPhaseReglement phase = (YvsComptaPhaseReglement) dao.loadOneByNameQueries(nameQueri, champ, val);
        if (phase != null ? phase.getId() > 0 : false) {
            champ = new String[]{"piece", "phase"};
            val = new Object[]{pp.getPieceVente(), phase};
            nameQueri = "YvsComptaPhasePiece.findByPhasePiece";
            YvsComptaPhasePiece prec = (YvsComptaPhasePiece) dao.loadOneByNameQueries(nameQueri, champ, val);
            if (prec != null ? prec.getId() > 0 : false) {
                if (!prec.getPhaseOk()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateMouvement() {
        //actualise la liste des phase
        selectPiece = (YvsComptaMouvementCaisse) dao.loadObjectByNameQueries("YvsComptaMouvementCaisse.findById", new String[]{"id"}, new Object[]{selectPiece.getId()});
        int idx = mouvements.indexOf(selectPiece);
        if (idx >= 0) {
            mouvements.set(idx, selectPiece);
        } else {
            mouvements.add(0, selectPiece);
        }
        update("data_list_piece_cheque");
    }

    public void validePhaseVente(YvsComptaPhasePiece pp) {
        selectPhaseVente = pp;
        if (pp.getStatut().equals(Constantes.STATUT_DOC_ANNULE)) {
            openDialog("dlgrecomptabilise");
        } else {
            validEtapesVente(pp);
        }
    }

    public void validEtapesVente(YvsComptaPhasePiece pp) {
        if (!controleFiche(currentPhaseVente.getDateValider())) {
            return;
        }
        if (pp.getPieceVente().getStatutPiece().equals(Constantes.STATUT_DOC_ANNULE)) {
            getErrorMessage("Le chèque concerné par cette phase à été annulé !");
            return;
        }
        if (pp.getPieceVente().getVerouille()) {
            getErrorMessage("Le chèque concerné par cette phase à été vérouillé !");
            return;
        }
        pp.setCaisse(currentPhaseVente.getCaisse());
        pp.setDateValider(currentPhaseVente.getDateValider());
        if ((pp.getCaisse() != null ? pp.getCaisse().getId() > 0 : false) && pp.getDateValider() != null) {
            validEtapesPieces(pp);
            if (!selectPiece.getReferenceExterne().equals(reference)) {
                pp.getPieceVente().setReferenceExterne(reference);
                pp.getPieceVente().setDateUpdate(new Date());
                pp.getPieceVente().setAuthor(currentUser);

                dao.update(pp.getPieceVente());
            }
            selectPiece.setStatutPiece(pieceReglement.getStatutPiece());
            selectPiece.setNumeroExterne(reference);
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
            updateMouvement();
        } else if (pp.getDateValider() == null) {
            getErrorMessage("Veuillez entrer une date de validation !");
        } else {
            getErrorMessage("Veuillez choisir une caisse ou banque !");
        }
        update("head_corp_form_suivi_prv");
    }

    public void validEtapesAcompteVente(YvsComptaPhaseAcompteVente pp) {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            if (!controleFiche(w.getCurrentPhaseAcompteVente().getDateValider())) {
                return;
            }
            pp.setCaisse(w.getCurrentPhaseAcompteVente().getCaisse());
            pp.setDateValider(w.getCurrentPhaseAcompteVente().getDateValider());
        }
        validEtapesAcompte(pp);
        if (!selectPiece.getReferenceExterne().equals(reference)) {
            pp.getPieceVente().setReferenceExterne(reference);
            pp.getPieceVente().setDateUpdate(new Date());
            pp.getPieceVente().setAuthor(currentUser);

            dao.update(pp.getPieceVente());
        }
        selectPiece.setStatutPiece(selectAcompte.getStatut());
        selectPiece.setNumeroExterne(reference);
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
        updateMouvement();
        update("head_corp_form_suivi_pav");
    }

    public void validEtapesCreditVente(YvsComptaPhaseReglementCreditClient pp) {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            if (!controleFiche(w.getCurrentPhaseCreditVente().getDateValider())) {
                return;
            }
            pp.setCaisse(w.getCurrentPhaseCreditVente().getCaisse());
            pp.setDateValider(w.getCurrentPhaseCreditVente().getDateValider());
        }
        validEtapesCredit(pp);
        if (!selectPiece.getReferenceExterne().equals(reference)) {
            pp.getReglement().setReferenceExterne(reference);
            pp.getReglement().setDateUpdate(new Date());
            pp.getReglement().setAuthor(currentUser);

            dao.update(pp.getReglement());
        }
        selectPiece.setStatutPiece(selectCredit.getStatut());
        selectPiece.setNumeroExterne(reference);
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
        updateMouvement();
    }

    public void validEtapesAchat(YvsComptaPhasePieceAchat pp) {
        if (managedReglementAchat != null) {
            if (!controleFiche(managedReglementAchat.getCurrentPhaseAchat().getDateValider())) {
                return;
            }
            pp.setCaisse(managedReglementAchat.getCurrentPhaseAchat().getCaisse());
            pp.setDateValider(managedReglementAchat.getCurrentPhaseAchat().getDateValider());
            if ((pp.getCaisse() != null ? pp.getCaisse().getId() > 0 : false) && pp.getDateValider() != null) {
                managedReglementAchat.validEtapesPieces(pp);
                if (!selectPiece.getReferenceExterne().equals(reference)) {
                    pp.getPieceAchat().setReferenceExterne(reference);
                    pp.getPieceAchat().setDateUpdate(new Date());
                    pp.getPieceAchat().setAuthor(currentUser);

                    dao.update(pp.getPieceAchat());
                }
                selectPiece.setStatutPiece(managedReglementAchat.getPieceAchat().getStatutPiece());
                selectPiece.setNumeroExterne(reference);
                int idx = mouvements.indexOf(selectPiece);
                if (idx > -1) {
                    mouvements.set(idx, selectPiece);
                }
                updateMouvement();
            } else if (pp.getDateValider() == null) {
                getErrorMessage("Veuillez entrer une date de validation !");
            } else {
                getErrorMessage("Veuillez choisir une caisse ou banque !");
            }
        }
        update("head_corp_form_suivi_pra");
    }

    public void validEtapesAcompteAchat(YvsComptaPhaseAcompteAchat pp) {
        if (managedReglementAchat != null) {
            ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (w != null) {
                if (!controleFiche(w.getCurrentPhaseAcompteAchat().getDateValider())) {
                    return;
                }
                pp.setCaisse(w.getCurrentPhaseAcompteAchat().getCaisse());
                pp.setDateValider(w.getCurrentPhaseAcompteAchat().getDateValider());
            }
            managedReglementAchat.validEtapesAcompte(pp);
            if (!selectPiece.getReferenceExterne().equals(reference)) {
                pp.getPieceAchat().setReferenceExterne(reference);
                pp.getPieceAchat().setDateUpdate(new Date());
                pp.getPieceAchat().setAuthor(currentUser);

                dao.update(pp.getPieceAchat());
            }
            selectPiece.setStatutPiece(managedReglementAchat.getSelectAcompte().getStatut());
            selectPiece.setNumeroExterne(reference);
            selectPiece.setCaisse(managedReglementAchat.getSelectAcompte().getCaisse());
            selectPiece.setDatePaye(managedReglementAchat.getSelectAcompte().getDatePaiement());
            selectPiece.setEtapeValide(pp.getPieceAchat().getPhaseValide());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
        update("head_corp_form_suivi_paa");
        updateMouvement();
    }

    public void validEtapesCreditAchat(YvsComptaPhaseReglementCreditFournisseur pp) {
        if (managedReglementAchat != null) {
            ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (w != null) {
                if (!controleFiche(w.getCurrentPhaseCreditAchat().getDateValider())) {
                    return;
                }
                pp.setCaisse(w.getCurrentPhaseCreditAchat().getCaisse());
                pp.setDateValider(w.getCurrentPhaseCreditAchat().getDateValider());
            }
            managedReglementAchat.validEtapesCredit(pp);
            if (!selectPiece.getReferenceExterne().equals(reference)) {
                pp.getReglement().setReferenceExterne(reference);
                pp.getReglement().setDateUpdate(new Date());
                pp.getReglement().setAuthor(currentUser);

                dao.update(pp.getReglement());
            }
            selectPiece.setStatutPiece(managedReglementAchat.getSelectCredit().getStatut());
            selectPiece.setNumeroExterne(reference);
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
        updateMouvement();
    }

    public void validEtapesVirement(YvsComptaPhasePieceVirement pp) {
        if (managedVirement != null) {
            if (!controleFiche(managedVirement.getCurrentPhaseVirement().getDateValider())) {
                return;
            }
            pp.setCaisse(managedVirement.getCurrentPhaseVirement().getCaisse());
            pp.setDateValider(managedVirement.getCurrentPhaseVirement().getDateValider());
            managedVirement.validEtapesPieces(pp);
            if (!selectPiece.getReferenceExterne().equals(reference)) {
//                pp.getVirement().set
                pp.getVirement().setDateUpdate(new Date());
                pp.getVirement().setAuthor(currentUser);

//                dao.update(pp.getVirement());
            }
            selectPiece.setStatutPiece(managedVirement.getEntity().getStatutPiece());
            selectPiece.setNumeroExterne(reference);
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
            updateMouvement();
        }
    }

    public void validEtapesDivers(YvsComptaPhasePieceDivers pp) {
        if (managedDocDivers != null) {
            if (!controleFiche(managedDocDivers.getCurrentPhaseDivers().getDateValider())) {
                return;
            }
            pp.setCaisse(managedDocDivers.getCurrentPhaseDivers().getCaisse());
            pp.setDateValider(managedDocDivers.getCurrentPhaseDivers().getDateValider());
            if ((pp.getCaisse() != null ? pp.getCaisse().getId() > 0 : false) && pp.getDateValider() != null) {
                if (!selectPiece.getNumeroExterne().equals(reference)) {
                    pp.getPieceDivers().setReferenceExterne(reference);
                    pp.getPieceDivers().setDateUpdate(new Date());
                    pp.getPieceDivers().setAuthor(currentUser);
                }
                managedDocDivers.validEtapesPieces(pp);
                selectPiece.setStatutPiece(managedDocDivers.getPieceCD().getStatutPiece());
                selectPiece.setNumeroExterne(reference);
                int idx = mouvements.indexOf(selectPiece);
                if (idx > -1) {
                    mouvements.set(idx, selectPiece);
                }
                updateMouvement();
            } else if (pp.getDateValider() == null) {
                getErrorMessage("Veuillez entrer une date de validation !");
            } else {
                getErrorMessage("Veuillez choisir une caisse ou banque !");
            }
        }
        update("head_corp_form_suivi_prd");
    }

    public void valideVente() {
        selectedPiece.setCaisse(currentPhaseVente.getCaisse());
        selectedPiece.setDatePaiement(currentPhaseVente.getDateValider());
        validePiece(selectedPiece, true);

        selectPiece.setStatutPiece(pieceReglement.getStatutPiece());
        selectPiece.setDatePaye(selectedPiece.getDatePaiement());
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
        update("head_corp_form_suivi_prv");
    }

    public void valideAcompteVente() {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            selectAcompte.setCaisse(w.getCurrentPhaseAcompteVente().getCaisse());
            selectAcompte.setDatePaiement(w.getCurrentPhaseAcompteVente().getDateValider());
        }
        valideAcompte();

        selectPiece.setStatutPiece(selectAcompte.getStatut());
        selectPiece.setDatePaye(selectAcompte.getDatePaiement());
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
    }

    public void valideCreditVente() {
        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
        if (w != null) {
            selectCredit.setCaisse(w.getCurrentPhaseCreditVente().getCaisse());
            selectCredit.setDatePaiement(w.getCurrentPhaseCreditVente().getDateValider());
        }
        valideCredit();
        selectPiece.setDatePaye(selectCredit.getDatePaiement());
        selectPiece.setStatutPiece(selectCredit.getStatut());
        int idx = mouvements.indexOf(selectPiece);
        if (idx > -1) {
            mouvements.set(idx, selectPiece);
        }
    }

    public void valideAchat() {
        if (managedReglementAchat != null) {
            managedReglementAchat.getPieceAchat().setCaisse(managedReglementAchat.getCurrentPhaseAchat().getCaisse());
            managedReglementAchat.getPieceAchat().setDatePaiement(managedReglementAchat.getCurrentPhaseAchat().getDateValider());
            managedReglementAchat.validePiece(managedReglementAchat.getPieceAchat(), true);

            selectPiece.setStatutPiece(managedReglementAchat.getPieceAchat().getStatutPiece());
            selectPiece.setDatePaye(managedReglementAchat.getPieceAchat().getDatePaiement());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
    }

    public void valideAcompteAchat() {
        if (managedReglementAchat != null) {
            ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (w != null) {
                managedReglementAchat.getSelectAcompte().setCaisse(w.getCurrentPhaseAcompteAchat().getCaisse());
                managedReglementAchat.getSelectAcompte().setDatePaiement(w.getCurrentPhaseAcompteAchat().getDateValider());
            }
            managedReglementAchat.valideAcompte();

            selectPiece.setStatutPiece(managedReglementAchat.getSelectAcompte().getStatut());
            selectPiece.setDatePaye(managedReglementAchat.getSelectAcompte().getDatePaiement());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
    }

    public void valideCreditAchat() {
        if (managedReglementAchat != null) {
            ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (w != null) {
                managedReglementAchat.getSelectCredit().setCaisse(w.getCurrentPhaseCreditAchat().getCaisse());
                managedReglementAchat.getSelectCredit().setDatePaiement(w.getCurrentPhaseCreditAchat().getDateValider());
            }
            managedReglementAchat.valideCredit();

            selectPiece.setStatutPiece(managedReglementAchat.getSelectCredit().getStatut());
            selectPiece.setDatePaye(managedReglementAchat.getSelectCredit().getDatePaiement());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
    }

    public void valideVirement() {
        if (managedVirement != null) {
            managedVirement.getEntity().setCible(managedVirement.getCurrentPhaseVirement().getCaisse());
            managedVirement.getEntity().setDatePaiement(managedVirement.getCurrentPhaseVirement().getDateValider());
            managedVirement.validePiece(managedVirement.getEntity(), true);

            selectPiece.setStatutPiece(managedVirement.getEntity().getStatutPiece());
            selectPiece.setDatePaye(managedVirement.getEntity().getDatePaiement());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
    }

    public void valideDivers() {
        if (managedDocDivers != null) {
            System.err.println("managedDocDivers.getCurrentPhaseDivers().getDateValider() " + managedDocDivers.getCurrentPhaseDivers().getDateValider());
            managedDocDivers.getPieceCD().setCaisse(managedDocDivers.getCurrentPhaseDivers().getCaisse());
            managedDocDivers.getPieceCD().setDateValider(managedDocDivers.getCurrentPhaseDivers().getDateValider());
            managedDocDivers.getPieceCD().setNumeroExterne(reference);
            managedDocDivers.validePc(managedDocDivers.getPieceCD(), managedDocDivers.getPieceCD().getDocDivers(), true);

            selectPiece.setStatutPiece(managedDocDivers.getPieceCD().getStatutPiece());
            selectPiece.setDatePaye(managedDocDivers.getPieceCD().getDateValider());
            int idx = mouvements.indexOf(selectPiece);
            if (idx > -1) {
                mouvements.set(idx, selectPiece);
            }
        }
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onSelectDistantReglement(YvsComptaCaissePieceVente y) {
        if (y != null ? y.getId() > 0 : false) {
            switch (y.getModel().getTypeReglement()) {
                case Constantes.MODE_PAIEMENT_BANQUE: {
                    onSelectObjectForCheque(y);
                    Navigations n = (Navigations) giveManagedBean(Navigations.class);
                    if (n != null) {
                        n.naviguationView("Suivi des chèques", "modCompta", "smenSuiviRegVente", true);
                    }
                    break;
                }
            }
        }
    }

    public void verifyComptabilised(Boolean comptabilised) {
        initForm = true;
        comptaSearch = comptabilised;
        paginator.addParam(new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptabilised, "=", "AND"));
        loadAllPiece(true, initForm);
        update("table_regFV");
    }

    public boolean isComptabiliseBean(PieceTresorerie y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CAISSE_VENTE));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsComptaCaissePieceVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CAISSE_VENTE));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public void correctionNumero() {
        try {
            List<YvsComptaCaissePieceVente> list = dao.loadNameQueries("YvsComptaCaissePieceVente.findByNotNumero", new String[]{"societe", "debut", "fin"}, new Object[]{currentAgence.getSociete(), dateDebutCorrection, dateFinCorrection});
            if (list != null ? !list.isEmpty() : false) {
                for (YvsComptaCaissePieceVente y : list) {
                    String numero = genererReference(Constantes.TYPE_PC_VENTE_NAME, y.getDatePiece(), y.getCaisse().getId());
                    if (numero != null ? numero.trim().length() < 1 : true) {
                        return;
                    }
                    y.setNumeroPiece(numero);
                    dao.update(y);
                }
                succes();
            }
        } catch (Exception ex) {
            getException("correctionNumero", ex);
        }
    }

    /**
     * *GESTION DES EXTOURNES
     */
//    public double countSousPieces(YvsComptaMouvementCaisse bean) {
//        Long nb = 0l;
//        if (bean != null ? bean.getId() != null : false) {
//            switch (bean.getTableExterne()) {
//                case Constantes.SCR_DIVERS: {
//                    nb = (Long) dao.loadObjectByNameQueries("YvsComptaCaissePieceDivers.countByParent", new String[]{"piece"}, new Object[]{new YvsComptaCaissePieceDivers(bean.getIdExterne())});
//                    break;
//                }
//                case Constantes.SCR_ACHAT: {
//                    nb = (Long) dao.loadObjectByNameQueries("YvsComptaCaissePieceAchat.countByParent", new String[]{"piece"}, new Object[]{new YvsComptaCaissePieceAchat(bean.getIdExterne())});
//                    break;
//                }
//                case Constantes.SCR_VENTE: {
//                    nb = (Long) dao.loadObjectByNameQueries("YvsComptaCaissePieceVente.countByParent", new String[]{"piece"}, new Object[]{new YvsComptaCaissePieceVente(bean.getIdExterne())});
//                    break;
//                }
//                case Constantes.SCR_ACOMPTE_ACHAT: {
//                    nb = (Long) dao.loadObjectByNameQueries("YvsComptaAcompteFournisseur.countByParent", new String[]{"piece"}, new Object[]{new YvsComptaAcompteFournisseur(bean.getIdExterne())});
//                    break;
//                }
//                case Constantes.SCR_ACOMPTE_VENTE: {
//                    nb = (Long) dao.loadObjectByNameQueries("YvsComptaCaissePieceVente.countByParent", new String[]{"piece"}, new Object[]{new YvsComptaAcompteClient(bean.getIdExterne())});
//                    break;
//                }
//                case Constantes.SCR_CREDIT_ACHAT: {
//                    if (managedReglementAchat != null) {
//                        YvsComptaReglementCreditFournisseur y = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
//                        managedReglementAchat.onSelectCreditForCheque(y);
//                    }
//                    break;
//                }
//                case Constantes.SCR_CREDIT_VENTE: {
//                    YvsComptaReglementCreditClient r = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
//                    onSelectCreditForCheque(r);
//                    break;
//                }
//                case Constantes.SCR_VIREMENT: {
//                    if (managedVirement != null) {
//                        YvsComptaCaissePieceVirement y = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
//                        managedVirement.onSelectObject(y);
//                    }
//                    break;
//                }
//            }
//        }
//        return nb;
//    }
//
//    private boolean remindDelSubPiece;
//    private PieceTresorerie avance = new PieceTresorerie();
//
//    public boolean isRemindDelSubPiece() {
//        return remindDelSubPiece;
//    }
//
//    public void setRemindDelSubPiece(boolean remindDelSubPiece) {
//        this.remindDelSubPiece = remindDelSubPiece;
//    }
//
//    public PieceTresorerie getAvance() {
//        return avance;
//    }
//
//    public void setAvance(PieceTresorerie avance) {
//        this.avance = avance;
//    }
//
//    public void loadSousPieces(YvsComptaCaissePieceVente y) {
//        if (y != null) {
//            ManagedReglementVente svice = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
//            if (svice != null) {
//                svice.setSelectedPiece(y);
//                update("data_mensualite_facture_vente");
//                update("data_sous_pieces_facture_vente");
//                openDialog("dlgotherP");
//            }
//        }
//    }
//
//    public void chooseLineSubPiece(SelectEvent ev) {
//        if (ev != null ? ev.getObject() != null : false) {
//            avance = UtilCompta.buildBeanTresoreri((YvsComptaCaissePieceVente) ev.getObject());
//            update("form_edit_sub_piece");
//        }
//    }
//
//    public void openConfirDeleteSubPiece(YvsComptaCaissePieceVente ev) {
//        if (ev != null) {
//            avance = UtilCompta.buildBeanTresoreri(ev);
//            update("form_edit_sub_piece");
//            if (!remindDelSubPiece) {
//                openDialog("dlgConfirmDelSubPiece");
//            } else {
//                deleteSubPieceCaisse();
//            }
//        }
//    }
//
//    public void deleteSubPieceCaisse() {
//        ManagedReglementVente svice = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
//        if (svice != null) {
//            if (svice.getSelectedPiece() != null && avance.getId() > 0 && avance.getIdParent() > 0) {
//                try {
//                    YvsComptaCaissePieceVente pv = UtilCompta.buildTresoreriVente(avance, currentUser);
//                    dao.delete(pv);
//                    svice.getSelectedPiece().getOthersReglements().remove(pv);
//                    avance = new PieceTresorerie();
//                    update("data_sous_pieces_facture_vente");
//                    update("form_edit_sub_piece");
//                } catch (Exception ex) {
//                    getErrorMessage("Suppression de la pièce impossible !");
//                    getException("Erreur suppression piece caisse !", ex);
//                }
//            }
//        }
//    }
//
//    public void updateSubPiece() {
//        ManagedReglementVente svice = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
//        if (svice != null) {
//            if (svice.getSelectedPiece() != null && avance.getId() > 0 && avance.getIdParent() > 0) {
//                if (avance.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
//                    getErrorMessage("Cette pièce est déjà réglé !");
//                    return;
//                }
//                if (!autoriser("compta_extourne_cheque")) {
//                    openNotAcces();
//                }
//                YvsComptaCaissePieceVente pv = UtilCompta.buildTresoreriVente(avance, currentUser);
//                pv.setParent(new YvsComptaCaissePieceVente(avance.getIdParent()));
//                dao.update(pv);
//                int idx = svice.getSelectedPiece().getOthersReglements().indexOf(pv);
//                if (idx >= 0) {
//                    svice.getSelectedPiece().getOthersReglements().set(idx, pv);
//                    update("data_sous_pieces_facture_vente");
//                }
//            }
//        }
//    }
//
//    public void valideReglementSubPiece(YvsComptaCaissePieceVente pv) {
//        if (pv != null) {
//
//        }
//    }
}
