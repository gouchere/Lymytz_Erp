/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.caisse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import yvs.commercial.UtilCom;
import yvs.commercial.achat.DocAchat;
import yvs.commercial.achat.ManagedFactureAchat;
import yvs.commercial.commission.ManagedCommission;
import yvs.comptabilite.tresorerie.ManagedDocDivers;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.commercial.vente.DocVente;
import yvs.commercial.vente.EnteteDocVente;
import yvs.commercial.vente.ManagedFactureVente;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.client.ManagedOperationClient;
import yvs.comptabilite.fournisseur.ManagedOperationFournisseur;
import yvs.comptabilite.tresorerie.ManagedBonProvisoire;
import yvs.dao.Util;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.compta.divers.YvsComptaCaissePieceDivers;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaAcompteClient;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceCommission;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaJustifBonMission;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaReglementCreditClient;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.achat.YvsComptaReglementCreditFournisseur;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaJustificatifBon;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowEtapeValidation;
import yvs.entity.param.workflow.YvsWorkflowValidPcMission;
import yvs.grh.UtilGrh;
import yvs.grh.bean.mission.ManagedMission;
import yvs.grh.bean.mission.Mission;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.enume.Nombre;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedPieceCaisse extends Managed<PieceTresorerie, YvsComptaMouvementCaisse> implements Serializable {

    private List<YvsComptaMouvementCaisse> mouvements, selectPieces;
    private PieceTresorerie pieceCaisse = new PieceTresorerie();
    private YvsComptaMouvementCaisse selectPiece = new YvsComptaMouvementCaisse();
    private YvsComptaParametre currentParam;

    private boolean date_up = false;
    private Date dateDebut_ = new Date(), dateFin_ = new Date();

    private String note;
    private double prevSolde;
    private double soldeInit, soldePeriode;

    @ManagedProperty(value = "#{mission}")
    private Mission mission;

    private String table, tabIds;
    private List<String> tables;
    private List<String> soldesCaisse;

    /*Gerer la recherche*/
    private Character statutF;
    private String sourceF;
    private Date debutF = new Date(), finF = new Date();
    private String caissierF, tiersF;
    private String numRefF, comparer = ">=", typeModeF;
    private double montantDF, montantFF;
    private long caisseF, agenceF;
    private Integer modeF;
    private boolean addDateF, addPrix, orderDesc = true;
    private String colOrder = "datePaimentPrevu";
    private String colDate = "datePaimentPrevu";

    public ManagedPieceCaisse() {
        tables = new ArrayList<>();
        selectPieces = new ArrayList<>();
        soldesCaisse = new ArrayList<>();
        soldesCaisse.add("Bonjour");
        tables.add(Constantes.SCR_ACOMPTE_ACHAT);
        tables.add(Constantes.SCR_ACOMPTE_VENTE);
        tables.add(Constantes.SCR_BON_PROVISOIRE);
        tables.add(Constantes.SCR_CREDIT_ACHAT);
        tables.add(Constantes.SCR_CREDIT_VENTE);
        tables.add(Constantes.SCR_ACHAT);
        tables.add(Constantes.SCR_DIVERS);
        tables.add(Constantes.SCR_VENTE);
        tables.add(Constantes.SCR_VIREMENT);
        tables.add(Constantes.SCR_MISSIONS);
        tables.add(Constantes.SCR_ECART_VENTE);
        tables.add(Constantes.SCR_NOTE_FRAIS);
        mouvements = new ArrayList<>();
    }

    public List<YvsComptaMouvementCaisse> getSelectPieces() {
        return selectPieces;
    }

    public void setSelectPieces(List<YvsComptaMouvementCaisse> selectPieces) {
        this.selectPieces = selectPieces;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
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

    public double getSoldePeriode() {
        return soldePeriode;
    }

    public void setSoldePeriode(double soldePeriode) {
        this.soldePeriode = soldePeriode;
    }

    public String getTypeModeF() {
        return typeModeF;
    }

    public void setTypeModeF(String typeModeF) {
        this.typeModeF = typeModeF;
    }

    public long getAgenceF() {
        return agenceF;
    }

    public void setAgenceF(long agenceF) {
        this.agenceF = agenceF;
    }

    public boolean isOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(boolean orderDesc) {
        this.orderDesc = orderDesc;
    }

    public double getPrevSolde() {
        return prevSolde;
    }

    public void setPrevSolde(double prevSolde) {
        this.prevSolde = prevSolde;
    }

    public boolean isAddPrix() {
        return addPrix;
    }

    public void setAddPrix(boolean addPrix) {
        this.addPrix = addPrix;
    }

    public String getComparer() {
        return comparer != null ? comparer.trim().length() > 0 ? comparer : ">=" : ">=";
    }

    public void setComparer(String comparer) {
        this.comparer = comparer;
    }

    PaginatorResult<YvsComptaMouvementCaisse> pagineHistorique = new PaginatorResult<>();

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTiersF() {
        return tiersF;
    }

    public void setTiersF(String tiersF) {
        this.tiersF = tiersF;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Character getStatutF() {
        return statutF;
    }

    public void setStatutF(Character statutF) {
        this.statutF = statutF;
    }

    public String getSourceF() {
        return sourceF;
    }

    public void setSourceF(String sourceF) {
        this.sourceF = sourceF;
    }

    public Date getDebutF() {
        return debutF;
    }

    public void setDebutF(Date debutF) {
        this.debutF = debutF;
    }

    public Date getFinF() {
        return finF;
    }

    public void setFinF(Date finF) {
        this.finF = finF;
    }

    public String getCaissierF() {
        return caissierF;
    }

    public void setCaissierF(String caissierF) {
        this.caissierF = caissierF;
    }

    public String getNumRefF() {
        return numRefF;
    }

    public void setNumRefF(String numRefF) {
        this.numRefF = numRefF;
    }

    public double getMontantDF() {
        return montantDF;
    }

    public void setMontantDF(double montantDF) {
        this.montantDF = montantDF;
    }

    public double getMontantFF() {
        return montantFF;
    }

    public void setMontantFF(double montantFF) {
        this.montantFF = montantFF;
    }

    public long getCaisseF() {
        return caisseF;
    }

    public void setCaisseF(long caisseF) {
        this.caisseF = caisseF;
    }

    public Integer getModeF() {
        return modeF;
    }

    public void setModeF(Integer modeF) {
        this.modeF = modeF;
    }

    public String getColDate() {
        return colDate;
    }

    public void setColDate(String colDate) {
        this.colDate = colDate;
    }

    public String getColOrder() {
        return colOrder;
    }

    public void setColOrder(String colOrder) {
        this.colOrder = colOrder;
    }

    public boolean isAddDateF() {
        return addDateF;
    }

    public void setAddDateF(boolean addDateF) {
        this.addDateF = addDateF;
    }

    public YvsComptaMouvementCaisse getSelectPiece() {
        return selectPiece;
    }

    public void setSelectPiece(YvsComptaMouvementCaisse selectPiece) {
        this.selectPiece = selectPiece;
    }

    public PieceTresorerie getPieceCaisse() {
        return pieceCaisse;
    }

    public void setPieceCaisse(PieceTresorerie pieceCaisse) {
        this.pieceCaisse = pieceCaisse;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public List<YvsComptaMouvementCaisse> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<YvsComptaMouvementCaisse> mouvements) {
        this.mouvements = mouvements;
    }

    public void saisieNumeroPieceExterne() {
        if (pieceCaisse.getSource() != null && pieceCaisse.getNumPiece() != null) {
            switch (pieceCaisse.getSource()) {
                case Constantes.SCR_MISSIONS:
                    //trouve la mission correspondant à la référence entré                    
                    findOneMissionByNum();
                    break;
                case Constantes.SCR_ACHAT:
                    break;
                case Constantes.SCR_DIVERS:
                    break;
                case Constantes.SCR_VENTE:
                    break;
                case Constantes.SCR_NOTE_FRAIS:
                    break;
                case Constantes.SCR_AUTRES:
                    break;
                case Constantes.SCR_VIREMENT:
                    break;
            }
        } else {
            getErrorMessage("Vous devez saisir le  numéro de pièce !");
        }
    }

    public void findOneMissionByNum() {
        ManagedMission serviceM = (ManagedMission) giveManagedBean(ManagedMission.class);
        if (serviceM != null) {
//            serviceM.findMissionByNum(pieceCaisse.getNumeroPiece());
            if (serviceM.getListMission().size() > 1) {
                //ouvre la liste des missions
//                openDialog("dlgMissionPc");
//                update("listeDlgmissionPC");
                pieceCaisse.setError(false);
            } else if (serviceM.getListMission().size() == 1) {
                chooseOneMission(serviceM.getListMission().get(0));
                pieceCaisse.setError(false);
            } else {
                pieceCaisse.setError(true);
            }
        }
    }

    public void choisirMission(SelectEvent ev) {
        if (ev != null) {
            YvsGrhMissions m = (YvsGrhMissions) ev.getObject();
            chooseOneMission(m);
        }
    }

    public void saveNewPieceCaisseMission() {
//        if (pieceCaisse.getObjetSource() != null) {
//            if (((YvsGrhMissions) pieceCaisse.getObjetSource()).getId() > 0) {
//                if (pieceCaisse.getMontant() > 0) {
//                    //construit l'objet piece caisse mission
//                    YvsComptaCaissePieceMission pcm = generedPieceCaisseMission((YvsGrhMissions) pieceCaisse.getObjetSource(), 'V');
//                    //save les étapes de validation
//                    pcm.setEtapesValidations(saveEtapesValidation(pcm));
//                    listAllMvtCaisse.add(0, convertPieceMissionToMvtCaisse(pcm));
//                    succes();
//                }
//            }
//        }
    }

    public void resetFichePC() {
        pieceCaisse = new PieceTresorerie();
        ManagedMission serviceM = (ManagedMission) giveManagedBean(ManagedMission.class);
        serviceM.getListMission().clear();
    }

    private YvsBaseCaisse giveCaisse(YvsBaseCaisse c) {
        if (c.getId() > 0) {
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service != null) {
                int idx = service.getCaisses().indexOf(c);
                if (idx >= 0) {
                    return service.getCaisses().get(idx);
                } else {
                    return service.findOneCaisse(c.getId());
                }
            }
            return c;
        }
        return null;
    }

    public void justfierBonProvisoire() {
        if (pieceCaisse.getId() > 0) {
            if (pieceCaisse.getTableExterne().equals(Constantes.SCR_BON_PROVISOIRE)) {
                ManagedBonProvisoire w = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                if (w != null) {
                    YvsComptaBonProvisoire pb = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (pb != null ? pb.getId() > 0 : false) {
                        if (!(pb.getStatut().equals(Constantes.STATUT_DOC_ANNULE) || pb.getStatut().equals(Constantes.STATUT_DOC_SUSPENDU))) {
                            if (w.justifyBon(pb, true)) {
                                mouvements.remove(new YvsComptaMouvementCaisse(pieceCaisse.getId()));
                                pieceCaisse.getListOthersPiece().remove(new YvsComptaMouvementCaisse(pieceCaisse.getId()));
                                update("table_PCLie");
                                update("table_all_pieceCaisse");
                            }
                        } else {
                            getErrorMessage("Le bon provisoire d'origine a été annulée !");
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                    }
                }
            }
        }
    }

    public void changeStaturPayePieceCaisse() {
        payerPieceCaisse(pieceCaisse);
    }

    public void payerPieceCaisse() {
        payerPieceCaisseEntity(selectPiece, true);
    }

    public void payerPieceCaisseAll() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                YvsComptaMouvementCaisse entity;
                long succes = 0;
                for (Long ids : l) {
                    entity = mouvements.get(ids.intValue());
                    if (!entity.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                        payerPieceCaisseEntity(entity, false);
                    }
                }
                succes();

            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public boolean payerPieceCaisseEntity(YvsComptaMouvementCaisse selectPiece, boolean succes) {
        if (selectPiece != null ? selectPiece.getId() > 0 : false) {
            PieceTresorerie bean = UtilCompta.buildBeanTresoreri(selectPiece);
            if (bean.getDatePaiement() == null) {
                bean.setDatePaiement(bean.getDatePaiementPrevu());
            }
            return payerPieceCaisse(bean, succes);
        }
        return false;
    }

    public void payerPieceCaisse(PieceTresorerie bean) {
        payerPieceCaisse(bean, true);
    }

    public boolean payerPieceCaisse(PieceTresorerie bean, boolean succes) {
        if (!autoriser("p_caiss_payer") || !autoriser("encais_piece_espece")) {
            openNotAcces();
            return false;
        }
        if (!verifyDate(bean.getDatePaiement(), currentParam.getJourAntidaterPaiement())) {
            return false;
        }
        //controle le solde de la caisse        
        if (bean.getId() > 0) {
            if (bean.getMontant() > 0 && bean.getCaisse().getId() > 0 && ((((bean.getMontantTotal() - bean.getMontantPlanifie()) >= 0) && !bean.getTableExterne().equals(Constantes.SCR_VIREMENT)) || (bean.getTableExterne().equals(Constantes.SCR_VIREMENT)))) {
                YvsBaseCaisse caiss = giveCaisse(new YvsBaseCaisse(bean.getCaisse().getId()));
                if (caiss != null) {
                    Double soldeCaisse = caiss.getSoldeCaisse(dao);
                    if ((soldeCaisse - bean.getMontant()) < 0 && bean.getMouvement().equals(Constantes.MOUV_CAISS_SORTIE) && !caiss.getCanNegative()) {
                        getErrorMessage("Le solde de la caisse est insuffisant pour réaliser cette opération !");
                        return false;
                    }
                    if (bean.getStatutPiece() == Constantes.STATUT_DOC_ATTENTE || bean.getStatutPiece() == Constantes.STATUT_DOC_EDITABLE || bean.getStatutPiece() == Constantes.STATUT_DOC_SOUMIS) {
                        switch (bean.getSource()) {
                            case Constantes.SCR_BON_PROVISOIRE:
                                YvsComptaBonProvisoire pb = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (pb != null ? pb.getId() > 0 : false) {
                                    if (!(pb.getStatut().equals(Constantes.ETAT_ANNULE) || pb.getStatut().equals(Constantes.ETAT_SUSPENDU))) {
                                        ManagedBonProvisoire w = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                                        if (w != null) {
                                            pb.setCaisse(caiss);
                                            pb.setCaissier(currentUser.getUsers());
                                            pb.setAuthor(currentUser);
                                            pb.setDatePayer(pieceCaisse.getDatePaiement());
                                            if (!w.valideBon(pb, true)) {
                                                return true;
                                            }
                                        }
//                                        if (controleAccesCaisse(caiss, true)) {
//                                            pb.setCaisse(caiss);
//                                            pb.setStatutPaiement(Constantes.ETAT_REGLE);
//                                            pb.setCaissier(currentUser.getUsers());
//                                            pb.setAuthor(currentUser);
//                                            dao.update(pb);
//                                        }
                                    } else {
                                        getErrorMessage("Le bon provisoire d'origine a été annulée !");
                                        return false;
                                    }
                                } else {
                                    getErrorMessage("Cette pièce n'a pas de source");
                                    return false;
                                }
                                break;
                            case Constantes.SCR_ACHAT:
                                YvsComptaCaissePieceAchat pa = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (pa != null ? pa.getId() > 0 : false) {
                                    if (!(pa.getAchat().getStatut().equals(Constantes.ETAT_ANNULE) || pa.getAchat().getStatut().equals(Constantes.ETAT_SUSPENDU))) {
                                        pa.setCaisse(caiss);
                                        pa.setDatePaiement(pieceCaisse.getDatePaiement());
                                        pa.setModel((pa.getModel() == null) ? caiss.getModeRegDefaut() : pa.getModel());
                                        ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                                        if (w != null) {
                                            if (!w.validePiece(pa, false)) {
                                                return true;
                                            }
                                        }
                                    } else {
                                        getErrorMessage("La facture d'achat d'origine a été annulée !");
                                        return false;
                                    }
                                } else {
                                    getErrorMessage("Cette pièce n'a pas de source");
                                    return false;
                                }
                                break;
                            case Constantes.SCR_VENTE:
                                YvsComptaCaissePieceVente pv = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (pv != null ? pv.getId() > 0 : false) {
                                    pv.setCaisse(caiss);
                                    pv.setDatePaiement(pieceCaisse.getDatePaiement());
                                    pv.setModel((pv.getModel() == null) ? caiss.getModeRegDefaut() : pv.getModel());
                                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                                    if (w != null) {
                                        if (!w.validePiece(pv, false)) {
                                            return true;
                                        }
                                    }
                                } else {
                                    getErrorMessage("Cette pièce n'a pas de source");
                                    return false;
                                }
                                break;
                            case Constantes.SCR_DIVERS:
                                YvsComptaCaissePieceDivers pd = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (pd != null ? pd.getId() > 0 : false) {
                                    pd.setCaisse(caiss);
                                    pd.setDateValider(pieceCaisse.getDatePaiement());
                                    pd.setModePaiement((pd.getModePaiement() == null) ? caiss.getModeRegDefaut() : pd.getModePaiement());
                                    ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                                    if (w != null) {
                                        if (!w.validePc(pd, pd.getDocDivers(), true)) {
                                            return true;
                                        }
                                    }
                                } else {
                                    getErrorMessage("Cette pièce n'a pas de source");
                                    return false;
                                }
                                break;
                            case Constantes.SCR_MISSIONS:
                                YvsComptaCaissePieceMission mi = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (mi != null ? mi.getId() > 0 : false) {
                                    mi.setCaisse(caiss);
                                    mi.setDatePaiement(pieceCaisse.getDatePaiement());
                                    mi.setModel((mi.getModel() == null) ? caiss.getModeRegDefaut() : mi.getModel());
                                    ManagedReglementMission w = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                                    if (w != null) {
                                        if (!w.payerMission(mi, true)) {
                                            return true;
                                        }
                                    }
                                } else {
                                    getErrorMessage("Cette pièce n'a pas de source");
                                    return false;
                                }
                                break;
                            case Constantes.SCR_VIREMENT:
                                YvsComptaCaissePieceVirement vi = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (vi != null ? vi.getId() > 0 : false) {
//                                    vi.setCible(caiss);
                                    vi.setDatePaiement(pieceCaisse.getDatePaiement());
                                    vi.setModel((vi.getModel() == null) ? caiss.getModeRegDefaut() : vi.getModel());
                                    ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class);
                                    if (w != null) {
                                        if (!w.validePiece(vi, false)) {
                                            return true;
                                        }
                                    }
                                    for (YvsComptaMouvementCaisse m : pieceCaisse.getListOthersPiece()) {
                                        m.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                        int idx = mouvements.indexOf(m);
                                        if (idx >= 0) {
                                            mouvements.set(idx, m);
                                        }
                                    }
                                    update("table_PCLie");
                                } else {
                                    getErrorMessage("Cette pièce n'a pas de source");
                                    return false;
                                }
                                break;
                            case Constantes.SCR_ACOMPTE_VENTE: {
                                YvsComptaAcompteClient y = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (y != null ? y.getId() > 0 : false) {
                                    y.setCaisse(caiss);
                                    y.setModel((y.getModel() == null) ? caiss.getModeRegDefaut() : y.getModel());
                                    y.setDatePaiement(pieceCaisse.getDatePaiement());
                                    boolean continu = true;
                                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                                    if (w != null) {
                                        continu = w.encaisserAcompte(y);
                                    }
                                    if (continu) {
                                        for (YvsComptaMouvementCaisse m : pieceCaisse.getListOthersPiece()) {
                                            m.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                            int idx = mouvements.indexOf(m);
                                            if (idx >= 0) {
                                                mouvements.set(idx, m);
                                            }
                                        }
                                    } else {
                                        return true;
                                    }
                                } else {
                                    getErrorMessage("Cette pièce n'a pas de source");
                                    return false;
                                }
                                break;
                            }
                            case Constantes.SCR_ACOMPTE_ACHAT: {
                                YvsComptaAcompteFournisseur y = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (y != null ? y.getId() > 0 : false) {
                                    y.setCaisse(caiss);
                                    y.setModel((y.getModel() == null) ? caiss.getModeRegDefaut() : y.getModel());
                                    y.setDatePaiement(pieceCaisse.getDatePaiement());
                                    boolean continu = true;
                                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                                    if (w != null) {
                                        continu = w.encaisserAcompte(y);
                                    }
                                    if (continu) {
                                        for (YvsComptaMouvementCaisse m : pieceCaisse.getListOthersPiece()) {
                                            m.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                            int idx = mouvements.indexOf(m);
                                            if (idx >= 0) {
                                                mouvements.set(idx, m);
                                            }
                                        }
                                    } else {
                                        return true;
                                    }
                                } else {
                                    getErrorMessage("Cette pièce n'a pas de source");
                                    return false;
                                }
                                break;
                            }
                            case Constantes.SCR_CREDIT_VENTE: {
                                YvsComptaReglementCreditClient y = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (y != null ? y.getId() > 0 : false) {
                                    if (!y.getCredit().getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                                        getErrorMessage("Le crédit d'origine n'est pas validé");
                                        return false;
                                    }
                                    y.setCaisse(caiss);
                                    y.setDatePaiement(pieceCaisse.getDatePaiement());
                                    y.setModel((y.getModel() == null) ? caiss.getModeRegDefaut() : y.getModel());
                                    boolean continu = true;
                                    ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                                    if (w != null) {
                                        continu = w.encaisserReglement(y.getCredit(), y);
                                    }
                                    if (continu) {
                                        for (YvsComptaMouvementCaisse m : pieceCaisse.getListOthersPiece()) {
                                            m.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                            int idx = mouvements.indexOf(m);
                                            if (idx >= 0) {
                                                mouvements.set(idx, m);
                                            }
                                        }
                                    } else {
                                        return true;
                                    }
                                } else {
                                    getErrorMessage("Cette pièce n'a pas de source");
                                    return false;
                                }
                                break;
                            }
                            case Constantes.SCR_CREDIT_ACHAT: {
                                YvsComptaReglementCreditFournisseur y = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (y != null ? y.getId() > 0 : false) {
                                    if (!y.getCredit().getStatut().equals(Constantes.STATUT_DOC_VALIDE)) {
                                        getErrorMessage("Le crédit d'origine n'est pas validé");
                                        return false;
                                    }
                                    y.setCaisse(caiss);
                                    y.setDatePaiement(pieceCaisse.getDatePaiement());
                                    y.setModel((y.getModel() == null) ? caiss.getModeRegDefaut() : y.getModel());
                                    boolean continu = true;
                                    ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                                    if (w != null) {
                                        continu = w.encaisserReglement(y.getCredit(), y);
                                    }
                                    if (continu) {
                                        for (YvsComptaMouvementCaisse m : pieceCaisse.getListOthersPiece()) {
                                            m.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                            int idx = mouvements.indexOf(m);
                                            if (idx >= 0) {
                                                mouvements.set(idx, m);
                                            }
                                        }
                                    } else {
                                        return true;
                                    }
                                } else {
                                    getErrorMessage("Cette pièce n'a pas de source");
                                    return false;
                                }
                                break;
                            }
                            case Constantes.SCR_COMMISSION: {
                                YvsComptaCaissePieceCommission y = (YvsComptaCaissePieceCommission) dao.loadOneByNameQueries("YvsComptaCaissePieceCommission.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                                if (y != null ? y.getId() > 0 : false) {
                                    if (!y.getCommission().getStatut().equals(Constantes.ETAT_VALIDE)) {
                                        getErrorMessage("La commission d'origine n'est pas validée");
                                        return true;
                                    }
                                    y.setCaisse(caiss);
                                    y.setDatePaiement(pieceCaisse.getDatePaiement());
                                    y.setModel((y.getModel() == null) ? caiss.getModeRegDefaut() : y.getModel());
                                    boolean continu = true;
                                    ManagedCommission w = (ManagedCommission) giveManagedBean(ManagedCommission.class);
                                    if (w != null) {
                                        continu = w.reglerPieceTresorerie(y.getCommission(), y, true);
                                    }
                                }
                                break;
                            }
                            default:
                                return false;
                        }
                        //update la vu
                        bean.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                        bean.setDatePaiement(pieceCaisse.getDatePaiement());
                        int idx = mouvements.indexOf(new YvsComptaMouvementCaisse(bean.getId()));
                        if (idx >= 0) {
                            YvsComptaMouvementCaisse mvt = mouvements.get(idx);
                            mvt.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                            mvt.setDatePaye(pieceCaisse.getDatePaiement());
                            mvt.setCaisse(caiss);
                            mouvements.set(idx, mvt);
                            update("table_all_pieceCaisse");
                        }
                        idx = pieceCaisse.getListOthersPiece().indexOf(new YvsComptaMouvementCaisse(bean.getId()));
                        if (idx >= 0) {
                            YvsComptaMouvementCaisse mvt = pieceCaisse.getListOthersPiece().get(idx);
                            mvt.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                            mvt.setDatePaye(pieceCaisse.getDatePaiement());
                            mvt.setCaisse(caiss);
                            pieceCaisse.getListOthersPiece().set(idx, mvt);
                            update("table_PCLie");
                        }
                        update("tab_recent_pc");
                        if (succes) {
                            succes();
                        }
                    } else {
                        getErrorMessage("Cette pièce est en attente de validation ! !");
                    }
                } else {
                    getErrorMessage("Vous devez selectionner une caisse !");
                }
            } else if (bean.getCaisse().getId() <= 0) {
                getErrorMessage("Vous devez selectionner une caisse !");
            } else if ((bean.getMontantTotal() - bean.getMontantPlanifie()) < 0) {
                getErrorMessage("Les pièces sont incohérente par-rapport au document d'origine !");
            } else {
                getErrorMessage("Cette pièce est en attente de validation !");
            }
        } else {
            getErrorMessage("Aucune piece n'a été selectionné !");
        }
        return false;
    }

    public void justifyDifference() {
        if (pieceCaisse.getId() > 0) {
            if (pieceCaisse.getBonProvisoire().getId() > 0) {
                YvsComptaCaissePieceDivers pd = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                if (pd != null) {
                    ManagedBonProvisoire w = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                    if (w != null) {
                        w.justifyBon(pd.getJustify().getBon());
                    }
                }
            } else {
                getErrorMessage("Cette action ne concerne que les ordre de dépenses rattaché aux bon provisoire !");
            }
        } else {
            getErrorMessage("Aucune pièce n'a été selectionné !");

        }
    }

    public void tooglePayePieceCaisse(YvsComptaMouvementCaisse mvt) {
        selectPiece = mvt;
        onSelectObject(mvt);
        if (mvt.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
            payerPieceCaisse();
        } else {
            //ouvre la boite de dialogue confirm annulle paiement
            if (autoriser("p_caiss_cancel_already_pay")) {
                openDialog("dlgConfirmCancel");
            } else {
                openNotAcces();
            }
        }
    }

    public void addNotePieceCaisse(YvsComptaMouvementCaisse mvt) {
        selectPiece = mvt;
        note = mvt.getNote();
        update("txt_note_piece_caisse");
    }

    public boolean isComptabilised(YvsComptaMouvementCaisse mvt) {
        if (mvt != null ? mvt.getId() > 0 : false) {
            boolean comptabilise = false;
            if (mvt.getIdExterne() > 0 && (mvt.getTableExterne() != null ? mvt.getTableExterne().trim().length() > 0 : false)) {
                switch (mvt.getTableExterne()) {
                    case Constantes.SCR_BON_PROVISOIRE:
                        comptabilise = dao.isComptabilise(mvt.getIdExterne(), Constantes.SCR_BON_PROVISOIRE);
                        break;
                    case Constantes.SCR_ACHAT:
                        comptabilise = dao.isComptabilise(mvt.getIdExterne(), Constantes.SCR_CAISSE_ACHAT);
                        break;
                    case Constantes.SCR_DIVERS:
                        comptabilise = dao.isComptabilise(mvt.getIdExterne(), Constantes.SCR_CAISSE_DIVERS);
                        break;
                    case Constantes.SCR_VENTE:
                        comptabilise = dao.isComptabilise(mvt.getIdExterne(), Constantes.SCR_CAISSE_VENTE);
                        break;
                    case Constantes.SCR_MISSIONS:
                        comptabilise = dao.isComptabilise(mvt.getIdExterne(), Constantes.SCR_FRAIS_MISSIONS);
                        break;
                    case Constantes.SCR_CREDIT_ACHAT:
                        comptabilise = dao.isComptabilise(mvt.getIdExterne(), Constantes.SCR_CREDIT_ACHAT);
                        break;
                    case Constantes.SCR_CREDIT_VENTE:
                        comptabilise = dao.isComptabilise(mvt.getIdExterne(), Constantes.SCR_CREDIT_VENTE);
                        break;
                    case Constantes.SCR_ACOMPTE_ACHAT:
                        comptabilise = dao.isComptabilise(mvt.getIdExterne(), Constantes.SCR_ACOMPTE_ACHAT);
                        break;
                    case Constantes.SCR_ACOMPTE_VENTE:
                        comptabilise = dao.isComptabilise(mvt.getIdExterne(), Constantes.SCR_ACOMPTE_VENTE);
                        break;
                    case Constantes.SCR_VIREMENT:
                        comptabilise = dao.isComptabilise(mvt.getIdExterne(), Constantes.SCR_VIREMENT);
                        break;
                }
            }
            mvt.setComptabilise(comptabilise);
            return mvt.isComptabilise();
        }
        return false;
    }

    public void actionComptabiliser(boolean comptabilise) {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            switch (selectPiece.getTableExterne()) {
                case Constantes.SCR_ACHAT:
                    YvsComptaCaissePieceAchat pa = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                    if (pa != null ? pa.getId() > 0 : false) {
                        if (comptabilise) {
                            w.comptabiliserCaisseAchat(pa, true, true);
                        } else {
                            w.unComptabiliserCaisseAchat(pa, true);
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                    }
                    break;
                case Constantes.SCR_DIVERS:
                    YvsComptaCaissePieceDivers pd = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                    if (pd != null ? pd.getId() > 0 : false) {
                        if (comptabilise) {
                            w.comptabiliserCaisseDivers(pd, true, true);
                        } else {
                            w.unComptabiliserCaisseDivers(pd, true);
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                    }
                    break;
                case Constantes.SCR_VENTE:
                    YvsComptaCaissePieceVente pv = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                    if (pv != null ? pv.getId() > 0 : false) {
                        if (comptabilise) {
                            w.comptabiliserCaisseVente(pv, true, true);
                        } else {
                            w.unComptabiliserCaisseVente(pv, true);
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                    }
                    break;
                case Constantes.SCR_MISSIONS:
                    YvsComptaCaissePieceMission mi = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                    if (mi != null ? mi.getId() > 0 : false) {
                        if (comptabilise) {
                            w.comptabiliserCaisseMission(mi, true, true);
                        } else {
                            w.unComptabiliserCaisseMission(mi, true);
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                    }
                    break;
                case Constantes.SCR_VIREMENT:
                    YvsComptaCaissePieceVirement vi = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                    if (vi != null ? vi.getId() > 0 : false) {
                        if (comptabilise) {
                            w.comptabiliserCaisseVirement(vi, true, true);
                        } else {
                            w.unComptabiliserCaisseVirement(vi, true);
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                    }
                    break;
            }
        }
    }

    public void addNotePieceCaisse() {
        switch (selectPiece.getTableExterne()) {
            case Constantes.SCR_ACHAT:
                YvsComptaCaissePieceAchat pa = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                if (pa != null ? pa.getId() > 0 : false) {
                    pa.setNote(note);
                    pa.setAuthor(currentUser);
                    dao.update(pa);
                } else {
                    getErrorMessage("Cette pièce n'a pas de source");
                    return;
                }
                break;
            case Constantes.SCR_DIVERS:
                YvsComptaCaissePieceDivers pd = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                if (pd != null ? pd.getId() > 0 : false) {
                    pd.setNote(note);
                    pd.setAuthor(currentUser);
                    dao.update(pd);
                } else {
                    getErrorMessage("Cette pièce n'a pas de source");
                    return;
                }
                break;
            case Constantes.SCR_VENTE:
                YvsComptaCaissePieceVente pv = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                if (pv != null ? pv.getId() > 0 : false) {
                    pv.setNote(note);
                    pv.setAuthor(currentUser);
                    dao.update(pv);
                } else {
                    getErrorMessage("Cette pièce n'a pas de source");
                    return;
                }
                break;
            case Constantes.SCR_MISSIONS:
                YvsComptaCaissePieceMission mi = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                if (mi != null ? mi.getId() > 0 : false) {
                    mi.setNote(note);
                    mi.setAuthor(currentUser);
                    dao.update(mi);
                } else {
                    getErrorMessage("Cette pièce n'a pas de source");
                    return;
                }
                break;
            case Constantes.SCR_VIREMENT:
                YvsComptaCaissePieceVirement vi = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                if (vi != null ? vi.getId() > 0 : false) {
                    vi.setNote(note);
                    vi.setAuthor(currentUser);
                    dao.update(vi);
                } else {
                    getErrorMessage("Cette pièce n'a pas de source");
                    return;
                }
                break;
        }
        selectPiece.setNote(note);
        int idx = mouvements.indexOf(selectPiece);
        if (idx >= 0) {
            mouvements.set(idx, selectPiece);
            update("table_all_pieceCaisse");
        }
        idx = pieceCaisse.getListOthersPiece().indexOf(selectPiece);
        if (idx >= 0) {
            pieceCaisse.getListOthersPiece().set(idx, selectPiece);
            update("table_PCLie");
        }
//        idx = historiques.indexOf(selectPiece);
//        if (idx >= 0) {
//            historiques.set(idx, selectPiece);
        update("tab_recent_pc");
//        }
        succes();
    }

    public void cofirmCancelPaiement() {
        if (pieceCaisse.getId() > 0) {
            switch (pieceCaisse.getSource()) {
                case Constantes.SCR_BON_PROVISOIRE:
                    YvsComptaBonProvisoire pb = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (pb != null ? pb.getId() > 0 : false) {
                        ManagedBonProvisoire w = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                        if (w != null) {
                            w.suspendBon(pb);
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                        return;
                    }
                    break;
                case Constantes.SCR_ACHAT:
                    YvsComptaCaissePieceAchat pa = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (pa != null ? pa.getId() > 0 : false) {
                        ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                        if (w != null) {
                            w.reglerPieceTresorerie(pa, "", false);
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                        return;
                    }
                    break;
                case Constantes.SCR_DIVERS:
                    //mise à jour de la pièce divers
                    YvsComptaCaissePieceDivers pd = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (pd != null ? pd.getId() > 0 : false) {
                        ManagedDocDivers w = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                        if (w != null) {
                            w.setSelectDoc(pd.getDocDivers());
                            w.suspendOnePieceCaisse(pd, true);
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                        return;
                    }
                    break;
                case Constantes.SCR_VENTE:
                    YvsComptaCaissePieceVente pv = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (pv != null ? pv.getId() > 0 : false) {
                        ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                        if (w != null) {
                            w.reglerPieceTresorerie(pv, "", true);
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                        return;
                    }
                    break;
                case Constantes.SCR_CREDIT_VENTE:
                    YvsComptaReglementCreditClient cv = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (cv != null ? cv.getId() > 0 : false) {
                        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                        if (w != null) {
                            if (!w.annulerReglement(cv.getCredit(), cv, false)) {
                                return;
                            }
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                        return;
                    }
                    break;
                case Constantes.SCR_CREDIT_ACHAT:
                    YvsComptaReglementCreditFournisseur ca = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (ca != null ? ca.getId() > 0 : false) {
                        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                        if (w != null) {
                            if (!w.annulerReglement(ca.getCredit(), ca, false)) {
                                return;
                            }
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                        return;
                    }
                    break;
                case Constantes.SCR_ACOMPTE_VENTE:
                    YvsComptaAcompteClient av = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (av != null ? av.getId() > 0 : false) {
                        ManagedOperationClient w = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                        if (w != null) {
                            if (!w.annulerAcompte(av, false)) {
                                return;
                            }
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                        return;
                    }
                    break;
                case Constantes.SCR_ACOMPTE_ACHAT:
                    YvsComptaAcompteFournisseur aa = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (aa != null ? aa.getId() > 0 : false) {
                        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                        if (w != null) {
                            if (!w.annulerAcompte(aa, false)) {
                                return;
                            }
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                        return;
                    }
                    break;
                case Constantes.SCR_MISSIONS:
                    YvsComptaCaissePieceMission mi = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (mi != null ? mi.getId() > 0 : false) {
                        ManagedReglementMission w = (ManagedReglementMission) giveManagedBean(ManagedReglementMission.class);
                        if (w != null) {
                            if (!w.annulerPayeMission(mi)) {
                                return;
                            }
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                        return;
                    }
                    break;
                case Constantes.SCR_VIREMENT:
                    YvsComptaCaissePieceVirement vi = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                    if (vi != null ? vi.getId() > 0 : false) {
                        ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class);
                        if (w != null) {
                            if (!w.changeStatutPiece(vi, true)) {
                                return;
                            }
                        }
                    } else {
                        getErrorMessage("Cette pièce n'a pas de source");
                        return;
                    }
                    break;
            }
            //update la vu
            pieceCaisse.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            pieceCaisse.setDatePaiement(new Date());
            int idx = mouvements.indexOf(new YvsComptaMouvementCaisse(pieceCaisse.getId()));
            if (idx >= 0) {
                YvsComptaMouvementCaisse mvt = mouvements.get(idx);
                mvt.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                mouvements.set(idx, mvt);
                update("table_all_pieceCaisse");
            }
            idx = pieceCaisse.getListOthersPiece().indexOf(new YvsComptaMouvementCaisse(pieceCaisse.getId()));
            if (idx >= 0) {
                YvsComptaMouvementCaisse mvt = pieceCaisse.getListOthersPiece().get(idx);
                mvt.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                pieceCaisse.getListOthersPiece().set(idx, mvt);
                update("table_PCLie");
            }
            // Si la pièce est un ordre de virement, changer le statut de la pièce lié
            if (pieceCaisse.getSource().equals(Constantes.SCR_VIREMENT)) {
                for (YvsComptaMouvementCaisse cv : pieceCaisse.getListOthersPiece()) {
                    idx = mouvements.indexOf(cv);
                    if (idx >= 0) {
                        YvsComptaMouvementCaisse mvt = mouvements.get(idx);
                        mvt.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                        cv.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                        mouvements.set(idx, mvt);
                        update("table_all_pieceCaisse");
                        update("table_PCLie");
                    }
                }
            }
            update("tab_recent_pc");//            }
            succes();
        } else {
            getErrorMessage("Aucune piece n'a été selectionné !");
        }
    }

    public void openConfirmDelete(YvsComptaMouvementCaisse mvt) {
        selectPiece = mvt;
        if (mvt.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
            if (autoriser("p_caiss_delete_payer")) {
                openDialog("dlgConfirmDelPC");
            } else {
                openNotAcces();
            }
        } else {
            getErrorMessage("Impossible de supprimer cette pièce !", "Sont statut est payé !!");
        }
    }

    public void deleteOnePieceCaisse() {
        try {
            if (selectPiece.getId() > 0) {
                if (selectPiece.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                    boolean correct = true;
                    switch (selectPiece.getTableExterne()) {
                        case Constantes.SCR_BON_PROVISOIRE:
                            YvsComptaBonProvisoire pb = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                            if (pb != null ? pb.getId() > 0 : false) {
                                pb.setAuthor(currentUser);
                                dao.delete(pb);
                            } else {
                                correct = false;
                            }
                            break;
                        case Constantes.SCR_ACHAT:
                            YvsComptaCaissePieceAchat pa = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                            if (pa != null ? pa.getId() > 0 : false) {
                                pa.setAuthor(currentUser);
                                dao.delete(pa);
                            } else {
                                correct = false;
                            }
                            break;
                        case Constantes.SCR_DIVERS:
                            //mise à jour de la pièce divers
                            YvsComptaCaissePieceDivers pd = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                            if (pd != null ? pd.getId() > 0 : false) {
                                pd.setAuthor(currentUser);
                                dao.delete(pd);
                            } else {
                                correct = false;
                            }
                            break;
                        case Constantes.SCR_VENTE:
                            YvsComptaCaissePieceVente pv = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                            if (pv != null ? pv.getId() > 0 : false) {
                                pv.setAuthor(currentUser);
                                dao.delete(pv);
                            } else {
                                correct = false;
                            }
                            break;
                        case Constantes.SCR_CREDIT_VENTE:
                            YvsComptaReglementCreditClient cv = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                            if (cv != null ? cv.getId() > 0 : false) {
                                cv.setAuthor(currentUser);
                                dao.delete(cv);
                            } else {
                                correct = false;
                            }
                            break;
                        case Constantes.SCR_CREDIT_ACHAT:
                            YvsComptaReglementCreditFournisseur ca = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                            if (ca != null ? ca.getId() > 0 : false) {
                                ca.setAuthor(currentUser);
                                dao.delete(ca);
                            } else {
                                correct = false;
                            }
                            break;
                        case Constantes.SCR_ACOMPTE_VENTE:
                            YvsComptaAcompteClient av = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                            if (av != null ? av.getId() > 0 : false) {
                                av.setAuthor(currentUser);
                                dao.delete(av);
                            } else {
                                correct = false;
                            }
                            break;
                        case Constantes.SCR_ACOMPTE_ACHAT:
                            YvsComptaAcompteFournisseur aa = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                            if (aa != null ? aa.getId() > 0 : false) {
                                aa.setAuthor(currentUser);
                                dao.delete(aa);
                            } else {
                                correct = false;
                            }
                            break;
                        case Constantes.SCR_MISSIONS:
                            YvsComptaCaissePieceMission mi = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{selectPiece.getIdExterne()});
                            if (mi != null ? mi.getId() > 0 : false) {
                                mi.setAuthor(currentUser);
                                dao.delete(mi);
                            } else {
                                correct = false;
                            }
                            break;
                        case Constantes.SCR_VIREMENT:
                            YvsComptaCaissePieceVirement vi = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                            if (vi != null ? vi.getId() > 0 : false) {
                                vi.setAuthor(currentUser);
                                dao.delete(vi);
                            } else {
                                correct = false;
                            }
                            break;
                    }
                    int idx = mouvements.indexOf(selectPiece);
                    if (idx >= 0) {
                        mouvements.remove(idx);
                        update("table_all_pieceCaisse");
                    }
                    idx = pieceCaisse.getListOthersPiece().indexOf(selectPiece);
                    if (idx >= 0) {
                        pieceCaisse.getListOthersPiece().remove(idx);
                        update("table_PCLie");
                    }
//                    idx = historiques.indexOf(selectPiece);
//                    if (idx >= 0) {
//                        historiques.remove(idx);
                    update("tab_recent_pc");
//                    }
                    if (correct) {
                        succes();
                    } else {
                        dao.delete(selectPiece);
                        getErrorMessage("Cette pièce n'a pas de source");
                    }
                } else {
                    getErrorMessage("Vous ne pouvez pas supprimer cette pièce car elle est déja payée!");
                }
            } else {
                getErrorMessage("Aucune pièce n'a été selectionné !");
            }
        } catch (Exception ex) {
            getErrorMessage("Immpossible de supprimer cette ligne !");
        }
    }

    public double totalPayeMission(long idMission) {
        //récupère le total de paye d'une mission
        String query = "SELECT SUM(y.montant) FROM YvsComptaCaissePieceMission y WHERE y.mission=:mission AND y.statutPiece=:statut";
        Double d = (Double) dao.loadObjectByEntity(query, new String[]{"mission", "statut"}, new Object[]{new YvsGrhMissions(idMission), 'P'});
//        Double d = (Double) dao.loadObjectByEntity("YvsComptaCaissePieceMission.findByMissPaye", new String[]{"mission","statut"}, new Object[]{new YvsGrhMissions(idMission),'P'});
        mission.setTotalPaye((d != null) ? d : 0);
        pieceCaisse.setMontant(mission.getTotalPaye());
        return mission.getTotalPaye();
    }

    private double totalFraisMission(YvsGrhMissions m) {
        ManagedMission serviceM = (ManagedMission) giveManagedBean(ManagedMission.class);
        pieceCaisse.setMontantTotal(serviceM.giveTotalFraisM(m.getFraisMissions()));
        return pieceCaisse.getMontantTotal();
    }

    private double totalDocVente(DocVente doc) {
        ManagedFactureVente service = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
        if (service != null) {
            service.setMontantTotalDoc(doc);
        }
        return doc.getMontantTotal();
    }

    private double totalDocAchat(DocAchat doc) {
        ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
        if (service != null) {
            service.setMontantTotalDoc(doc);
        }
        return doc.getMontantTotal();
    }

    private double totalPaye(List<YvsComptaMouvementCaisse> lpt, boolean addBon) {
        double re = 0;
        double tp = 0;
        for (YvsComptaMouvementCaisse pt : lpt) {
            if (!pt.getTableExterne().equals(Constantes.SCR_BON_PROVISOIRE) || addBon) {
                if (pt.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                    re += pt.getMontant();
                    tp += pt.getMontant();
                } else if (pt.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && pt.getStatutPiece() != Constantes.STATUT_DOC_ANNULE) {
                    tp += pt.getMontant();
                }
            }
        }
        pieceCaisse.setMontantPlanifie(tp);
        return re;
    }

    private void chooseOneMission(YvsGrhMissions m) {
        ManagedMission serviceM = (ManagedMission) giveManagedBean(ManagedMission.class);
//        pieceCaisse.setObjetSource((YvsGrhMissions) m);
        pieceCaisse.setMontantTotal(serviceM.giveTotalFraisM(m.getFraisMissions()));
        pieceCaisse.setMontantAvance(totalPayeMission(m.getId()));
//        pieceCaisse.setNumeroPiece(m.getNumeroMission());
        pieceCaisse.setMontant(pieceCaisse.getMontantTotal() - pieceCaisse.getMontantAvance());
    }

    @Override
    public boolean controleFiche(PieceTresorerie bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PieceTresorerie recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(PieceTresorerie bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        pieceCaisse = new PieceTresorerie();
        selectPiece = new YvsComptaMouvementCaisse();
    }

    @Override
    public void onSelectDistant(YvsComptaMouvementCaisse y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Pièces de caisse", "modCompta", "smenPieceCaiss", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsComptaMouvementCaisse y) {
        selectPiece = y;
        loadPieceOnView(selectPiece);
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            onSelectObject((YvsComptaMouvementCaisse) ev.getObject());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void onCallObjectDistant() {
        if (pieceCaisse != null ? pieceCaisse.getId() > 0 : false) {
            if (pieceCaisse.getIdExterne() > 0 && (pieceCaisse.getTableExterne() != null ? pieceCaisse.getTableExterne().trim().length() > 0 : false)) {
                switch (pieceCaisse.getTableExterne()) {
                    case Constantes.SCR_BON_PROVISOIRE:
                        YvsComptaBonProvisoire pb = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (pb != null ? pb.getId() > 0 : false) {
                            ManagedBonProvisoire s = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                            if (s != null) {
                                s.onSelectDistant(pb);
                            }
                        }
                        break;
                    case Constantes.SCR_ACHAT:
                        YvsComptaCaissePieceAchat pa = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (pa != null ? pa.getId() > 0 : false) {
                            ManagedFactureAchat s = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                            if (s != null) {
                                s.onSelectDistant(pa.getAchat());
                            }
                        }
                        break;
                    case Constantes.SCR_DIVERS:
                        //récupère la piece doc divers
                        YvsComptaCaissePieceDivers pd = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (pd != null ? pd.getId() > 0 : false) {
                            ManagedDocDivers s = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
                            if (s != null) {
                                s.onSelectDistant(pd.getDocDivers());
                            }
                        }
                        break;
                    case Constantes.SCR_VENTE:
                        YvsComptaCaissePieceVente pv = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (pv != null ? pv.getId() > 0 : false) {
                            ManagedFactureVente s = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
                            if (s != null) {
                                s.onSelectDistant(pv.getVente());
                            }
                        }
                        break;
                    case Constantes.SCR_MISSIONS:
                        YvsComptaCaissePieceMission pm = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (pm != null ? pm.getId() > 0 : false) {
                            ManagedMission s = (ManagedMission) giveManagedBean(ManagedMission.class);
                            if (s != null) {
                                s.onSelectDistant(pm.getMission());
                            }
                        }
                        break;
                    case Constantes.SCR_CREDIT_ACHAT:
                        YvsComptaReglementCreditFournisseur ca = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (ca != null ? ca.getId() > 0 : false) {
                            ManagedOperationFournisseur s = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                            if (s != null) {
                                s.onSelectDistant(ca.getCredit());
                            }
                        }
                        break;
                    case Constantes.SCR_CREDIT_VENTE:
                        YvsComptaReglementCreditClient cv = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (cv != null ? cv.getId() > 0 : false) {
                            ManagedOperationClient s = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                            if (s != null) {
                                s.onSelectDistant(cv.getCredit());
                            }
                        }
                        break;
                    case Constantes.SCR_ACOMPTE_ACHAT:
                        YvsComptaAcompteFournisseur aa = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (aa != null ? aa.getId() > 0 : false) {
                            ManagedOperationFournisseur s = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                            if (s != null) {
                                s.onSelectDistant(aa);
                            }
                        }
                        break;
                    case Constantes.SCR_ACOMPTE_VENTE:
                        YvsComptaAcompteClient av = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (av != null ? av.getId() > 0 : false) {
                            ManagedOperationClient s = (ManagedOperationClient) giveManagedBean(ManagedOperationClient.class);
                            if (s != null) {
                                s.onSelectDistant(av);
                            }
                        }
                        break;
                    case Constantes.SCR_VIREMENT:
                        YvsComptaCaissePieceVirement vi = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (vi != null ? vi.getId() > 0 : false) {
                            ManagedVirement s = (ManagedVirement) giveManagedBean(ManagedVirement.class);
                            if (s != null) {
                                s.onSelectDistant(vi);
                            }
                        }
                        break;
                    case Constantes.SCR_COMMISSION:
                        YvsComptaCaissePieceCommission com = (YvsComptaCaissePieceCommission) dao.loadOneByNameQueries("YvsComptaCaissePieceCommission.findById", new String[]{"id"}, new Object[]{pieceCaisse.getIdExterne()});
                        if (com != null ? com.getId() > 0 : false) {
                            ManagedCommission s = (ManagedCommission) giveManagedBean(ManagedCommission.class);
                            if (s != null) {
                                s.onSelectDistantCommercial(com.getCommission());
                            }
                        }
                        break;
                }
            }
        }
    }

    private void loadPieceOnView(YvsComptaMouvementCaisse mvt) {
        if (mvt != null) {
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service != null) {
                if (!service.getCaisses().contains(mvt.getCaisse())) {
                    service.getCaisses().add(0, mvt.getCaisse());
                }
            }
//            pieceCaisse = new PieceTresorerie();
            pieceCaisse.setBonProvisoire(null);
            pieceCaisse.setCaisse(UtilCompta.buildSimpleBeanCaisse(mvt.getCaisse()));
            pieceCaisse.setCaissier(UtilUsers.buildSimpleBeanUsers(mvt.getCaissier()));
            pieceCaisse.setTiersInterne(UtilUsers.buildSimpleBeanUsers(mvt.getCaissier()));
            pieceCaisse.setMode(UtilCom.buildBeanModeReglement(mvt.getModel()));
//            pieceCaisse.setDatePaiement(mvt.getDatePaye() != null ? mvt.getDatePaye() : mvt.getDatePaimentPrevu());            
            pieceCaisse.setStatutPiece(mvt.getStatutPiece());
            if (pieceCaisse.getStatutPiece() != ' ' ? pieceCaisse.getStatutPiece() == Constantes.STATUT_DOC_PAYER : false) {
                pieceCaisse.setDatePaiement(mvt.getDatePaye());
            } else {
                pieceCaisse.setDatePaiement(new Date());
            }
            pieceCaisse.setDatePaiementPrevu(mvt.getDatePaimentPrevu());
            pieceCaisse.setDatePiece(mvt.getDateMvt() != null ? mvt.getDateMvt() : mvt.getDatePaimentPrevu());
            pieceCaisse.setDescription(mvt.getNote());
            pieceCaisse.setError(true);
            pieceCaisse.setId(mvt.getId());
            pieceCaisse.setIdExterne(mvt.getIdExterne());
            pieceCaisse.setSource(mvt.getTableExterne());
            pieceCaisse.setMontant(mvt.getMontant());
            pieceCaisse.setMouvement(mvt.getMouvement());
            pieceCaisse.setNameTable(mvt.getTableExterne());
            pieceCaisse.setNumPiece(mvt.getNumero());
            pieceCaisse.setNumRef(mvt.getReferenceExterne());
            pieceCaisse.setNameTiers(nomTiers(mvt));
            pieceCaisse.setBeneficiaire(mvt.getNameTiers());
            pieceCaisse.setTableExterne(mvt.getTableExterne());
            pieceCaisse.setAuthor(mvt.getAuthor());
            pieceCaisse.setDocAchat(null);
            pieceCaisse.setDocVente(null);
            pieceCaisse.setDocDivers(null);
            pieceCaisse.setMission(null);
            pieceCaisse.getBielletageBillet().clear();
            pieceCaisse.getBielletagePiece().clear();
            pieceCaisse.setEnteteDoc(new EnteteDocVente());

            switch (mvt.getTableExterne()) {
                case Constantes.SCR_BON_PROVISOIRE:
                    YvsComptaBonProvisoire pb = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{mvt.getIdExterne()});
                    if (pb != null ? pb.getId() > 0 : false) {
                        pieceCaisse.setBonProvisoire(UtilCompta.buildBeanBonProvisoire(pb));
                        pieceCaisse.setStatutExterne(pb.getStatut().toString());
                        pieceCaisse.setError(false);
                        pieceCaisse.setDescription(pb.getDescription());
                        pieceCaisse.setMontantTotal(pb.getMontant());
                        pieceCaisse.getListOthersPiece().clear();
                        pieceCaisse.setDateUpdate(pb.getDateUpdate());
                        pieceCaisse.setDateSave(pb.getDateSave());
                        pieceCaisse.setAuthor(pb.getAuthor());
                        for (YvsComptaJustificatifBon j : pb.getJustificatifs()) {
                            pieceCaisse.getListOthersPiece().addAll(dao.loadNameQueries("YvsComptaMouvementCaisse.findByExterne", new String[]{"table", "idExterne"}, new Object[]{Constantes.SCR_DIVERS, j.getPiece().getId()}));
                        }
                    }
                    break;
                case Constantes.SCR_ACHAT:
                    YvsComptaCaissePieceAchat pa = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{mvt.getIdExterne()});
                    if (pa != null ? pa.getId() > 0 : false) {
                        if (pa.getAchat() != null) {
                            pieceCaisse.setDocAchat(UtilCom.buildSimpleBeanDocAchat(pa.getAchat()));
                            pieceCaisse.setStatutExterne(pa.getAchat().getStatut());
                            //cherche les autres pièces de ce document                    
                            pieceCaisse.setListOthersPiece((dao.loadNameQueries("YvsComptaMouvementCaisse.findPieceLies", new String[]{"table", "listIds"}, new Object[]{mvt.getTableExterne(), giveIdsPiecesDocAchat(pa.getAchat().getReglements())})));
                            pieceCaisse.setMontantTotal(totalDocAchat(pieceCaisse.getDocAchat()));
                            pieceCaisse.setError(false);
                            pieceCaisse.setDescription(pieceCaisse.getDocAchat().getDescription());

                            pieceCaisse.setDateUpdate(pa.getDateUpdate());
                            pieceCaisse.setDateSave(pa.getDateSave());
                            pieceCaisse.setAuthor(pa.getAuthor());
                        } else {
                            getErrorMessage("Cette pièce n'est rattaché à aucun document d'achat !");
                        }
                    }
                    break;
                case Constantes.SCR_DIVERS:
                    System.err.println(" Ce cas.....");
                    //récupère la piece doc divers
                    YvsComptaCaissePieceDivers pd = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{mvt.getIdExterne()});
                    if (pd != null ? pd.getId() > 0 : false) {
                        pieceCaisse.setDocDivers(UtilCompta.buildSimpleBeanDocCaisse(pd.getDocDivers()));
                        pieceCaisse.setStatutExterne(String.valueOf(pd.getDocDivers().getStatutDoc()));
                        //cherche les autres pièces de ce document                    
                        pieceCaisse.setListOthersPiece((dao.loadNameQueries("YvsComptaMouvementCaisse.findPieceLies", new String[]{"table", "listIds"}, new Object[]{mvt.getTableExterne(), giveIdsPiecesDocDivers(pd.getDocDivers().getReglements())})));
                        pieceCaisse.setMontantTotal(pieceCaisse.getDocDivers().getMontantTotal());
                        pieceCaisse.setError(false);
                        pieceCaisse.setDescription(pieceCaisse.getDocDivers().getDescription());
                        pieceCaisse.setDateUpdate(pd.getDateUpdate());
                        pieceCaisse.setDateSave(pd.getDateSave());
                        pieceCaisse.setAuthor(pd.getAuthor());
                    }
                    break;
                case Constantes.SCR_VENTE:
                    YvsComptaCaissePieceVente pv = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findByIdAll", new String[]{"id"}, new Object[]{mvt.getIdExterne()});
                    if (pv != null ? pv.getId() > 0 : false) {
                        pieceCaisse.setDocVente(UtilCom.buildInfosBaseDocVente(pv.getVente()));
                        pieceCaisse.setStatutExterne(pv.getVente().getStatut());
                        //cherche les autres pièces de ce document                    
                        pieceCaisse.setListOthersPiece((dao.loadNameQueries("YvsComptaMouvementCaisse.findPieceLies", new String[]{"table", "listIds"}, new Object[]{mvt.getTableExterne(), giveIdsPiecesDocVente(pv.getVente().getReglements())})));
                        pieceCaisse.setMontantTotal(totalDocVente(pieceCaisse.getDocVente()));
                        pieceCaisse.setError(false);
                        pieceCaisse.setDateUpdate(pv.getDateUpdate());
                        pieceCaisse.setDateSave(pv.getDateSave());
                        pieceCaisse.setAuthor(pv.getAuthor());
                    }
                    break;
                case Constantes.SCR_MISSIONS:
                    YvsComptaCaissePieceMission pm = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{mvt.getIdExterne()});
                    if (pm != null ? pm.getId() > 0 : false) {
                        pm.setMission(setMontantTotalMission(pm.getMission(), new Mission()));
                        pieceCaisse.setMission(UtilGrh.buildBeanMission(pm.getMission()));
                        pieceCaisse.setStatutExterne(String.valueOf(pm.getMission().getStatutMission()));
                        //cherche les autres pièces de ce document                    
                        pieceCaisse.setListOthersPiece((dao.loadNameQueries("YvsComptaMouvementCaisse.findPieceLies", new String[]{"table", "listIds"}, new Object[]{mvt.getTableExterne(), giveIdsPiecesMissions(pm.getMission().getReglements())})));
                        pieceCaisse.setMontantTotal(pm.getMission().getTotalFraisMission());
                        pieceCaisse.setError(false);

                        pieceCaisse.setDateUpdate(pm.getDateUpdate());
                        pieceCaisse.setDateSave(pm.getDateSave());
                        pieceCaisse.setAuthor(pm.getAuthor());
                        List<Long> ids = dao.loadNameQueries("YvsComptaJustifBonMission.findIdBonByMission", new String[]{"mission"}, new Object[]{pm.getMission()});
                        if (ids.isEmpty()) {
                            ids.add(-1L);
                        }
                        List<YvsComptaMouvementCaisse> list = dao.loadNameQueries("YvsComptaMouvementCaisse.findPieceLies", new String[]{"table", "listIds"}, new Object[]{Constantes.SCR_BON_PROVISOIRE, ids});
                        if (list != null) {
                            pieceCaisse.getListOthersPiece().addAll(list);
                        }
                    }
                    break;
                case Constantes.SCR_VIREMENT:
                    YvsComptaCaissePieceVirement pvi = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{mvt.getIdExterne()});
                    if (pvi != null ? pvi.getId() > 0 : false) {
                        //cherche les autres pièces de ce document                    
                        pieceCaisse.setMontantTotal(pvi.getMontant());
                        pieceCaisse.setError(false);
                        PieceTresorerie y = UtilCompta.buildBeanTresoreri(pvi);
                        pieceCaisse.setBielletageBillet(new ArrayList<>(y.getBielletageBillet()));
                        pieceCaisse.setBielletagePiece(new ArrayList<>(y.getBielletagePiece()));
                        if (pvi.getVersement() != null ? pvi.getVersement().getEnteteDoc() != null : false) {
                            pieceCaisse.setEnteteDoc(UtilCom.buildSimpleBeanEnteteDocVente(pvi.getVersement().getEnteteDoc()));
                            pieceCaisse.getEnteteDoc().setVersementAttendu(dao.loadVersementAttendu(pieceCaisse.getEnteteDoc().getUsers().getId(), pieceCaisse.getEnteteDoc().getDateEntete(), pieceCaisse.getEnteteDoc().getDateEntete()));
                        }
                        List<YvsComptaMouvementCaisse> list = dao.loadNameQueries("YvsComptaMouvementCaisse.findPieceLieNoId", new String[]{"table", "externe", "id"}, new Object[]{Constantes.SCR_VIREMENT, pvi.getId(), mvt.getId()});
                        if (list != null) {
                            pieceCaisse.setListOthersPiece(list);
                        }
                        pieceCaisse.setDateUpdate(pvi.getDateUpdate());
                        pieceCaisse.setDateSave(pvi.getDateSave());
                        pieceCaisse.setAuthor(pvi.getAuthor());

                    }
                case Constantes.SCR_CREDIT_ACHAT:
                    YvsComptaReglementCreditFournisseur crf = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{mvt.getIdExterne()});
                    if (crf != null ? crf.getId() > 0 : false) {
                        //cherche les autres pièces de ce document                    
                        pieceCaisse.setMontantTotal(crf.getCredit().getMontant());
                        //cherche les autres pièces de ce document                    
                        pieceCaisse.setListOthersPiece((dao.loadNameQueries("YvsComptaMouvementCaisse.findPieceLies", new String[]{"table", "listIds"}, new Object[]{mvt.getTableExterne(), giveIdsPiecesCreditFseur(crf.getCredit().getReglements())})));
                        pieceCaisse.setError(false);
                        pieceCaisse.setDateUpdate(crf.getDateUpdate());
                        pieceCaisse.setDateSave(crf.getDateSave());
                        pieceCaisse.setAuthor(crf.getAuthor());
                    }
                case Constantes.SCR_CREDIT_VENTE:
                    YvsComptaReglementCreditClient crc = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{mvt.getIdExterne()});
                    if (crc != null ? crc.getId() > 0 : false) {
                        //cherche les autres pièces de ce document                    
                        pieceCaisse.setMontantTotal(crc.getCredit().getMontant());
                        pieceCaisse.setListOthersPiece((dao.loadNameQueries("YvsComptaMouvementCaisse.findPieceLies", new String[]{"table", "listIds"}, new Object[]{mvt.getTableExterne(), giveIdsPiecesCreditClient(crc.getCredit().getReglements())})));
                        pieceCaisse.setError(false);
                        pieceCaisse.setDateUpdate(crc.getDateUpdate());
                        pieceCaisse.setDateSave(crc.getDateSave());
                        pieceCaisse.setAuthor(crc.getAuthor());

                    }
                    break;
                case Constantes.SCR_COMMISSION:
                    YvsComptaCaissePieceCommission com = (YvsComptaCaissePieceCommission) dao.loadOneByNameQueries("YvsComptaCaissePieceCommission.findById", new String[]{"id"}, new Object[]{mvt.getIdExterne()});
                    if (com != null ? com.getId() > 0 : false) {
                        //cherche les autres pièces de ce document                    
                        pieceCaisse.setMontantTotal(com.getCommission().getMontant());
                        pieceCaisse.setError(false);
                        pieceCaisse.setDateUpdate(com.getDateUpdate());
                        pieceCaisse.setDateSave(com.getDateSave());
                        pieceCaisse.setAuthor(com.getAuthor());

                    }
                    break;
                case Constantes.SCR_ACOMPTE_ACHAT:
                case Constantes.SCR_ACOMPTE_VENTE:
                    //cherche les autres pièces de ce document                    
                    pieceCaisse.setMontantTotal(mvt.getMontant());
                    pieceCaisse.setError(false);
                    break;
            }
            pieceCaisse.setMontantAvance(totalPaye(pieceCaisse.getListOthersPiece(), false)); //avance correspondant à l'ensemble des pièces caisses payé            
            pieceCaisse.getListOthersPiece().remove(new YvsComptaMouvementCaisse(pieceCaisse.getId()));
            table = pieceCaisse.getSource();
            chooseTable(true);
            //calcule les soe                        
            if (pieceCaisse.isError()) {
                getErrorMessage("Cette pièce n'est pas rattachée à une source");
            }
        }
    }

    private List<Long> giveIdsPiecesBonProvisoire(List<YvsComptaJustifBonMission> l) {
        List<Long> re = new ArrayList<>();
        if (l != null ? !l.isEmpty() : false) {
            for (YvsComptaJustifBonMission c : l) {
                re.add(c.getPiece().getId());
            }
        } else {
            re.add(-1L);
        }
        return re;
    }

    private List<Long> giveIdsPiecesDocDivers(List<YvsComptaCaissePieceDivers> l) {
        List<Long> re = new ArrayList<>();
        if (l != null ? !l.isEmpty() : false) {
            for (YvsComptaCaissePieceDivers c : l) {
                re.add(c.getId());
            }
        } else {
            re.add(-1L);
        }
        return re;
    }

    private List<Long> giveIdsPiecesDocVente(List<YvsComptaCaissePieceVente> l) {
        List<Long> re = new ArrayList<>();
        if (l != null ? !l.isEmpty() : false) {
            for (YvsComptaCaissePieceVente c : l) {
                re.add(c.getId());
            }
        } else {
            re.add(-1L);
        }
        return re;
    }

    private List<Long> giveIdsPiecesDocAchat(List<YvsComptaCaissePieceAchat> l) {
        List<Long> re = new ArrayList<>();
        if (l != null ? !l.isEmpty() : false) {
            for (YvsComptaCaissePieceAchat c : l) {
                re.add(c.getId());
            }
        } else {
            re.add(-1L);
        }
        return re;
    }

    private List<Long> giveIdsPiecesMissions(List<YvsComptaCaissePieceMission> l) {
        List<Long> re = new ArrayList<>();
        if (l != null ? !l.isEmpty() : false) {
            for (YvsComptaCaissePieceMission c : l) {
                re.add(c.getId());
            }
        } else {
            re.add(-1L);
        }
        return re;
    }

    private List<Long> giveIdsPiecesCreditFseur(List<YvsComptaReglementCreditFournisseur> l) {
        List<Long> re = new ArrayList<>();
        if (l != null ? !l.isEmpty() : false) {
            for (YvsComptaReglementCreditFournisseur c : l) {
                re.add(c.getId());
            }
        } else {
            re.add(-1L);
        }
        return re;
    }

    private List<Long> giveIdsPiecesCreditClient(List<YvsComptaReglementCreditClient> l) {
        List<Long> re = new ArrayList<>();
        if (l != null ? !l.isEmpty() : false) {
            for (YvsComptaReglementCreditClient c : l) {
                re.add(c.getId());
            }
        } else {
            re.add(-1L);
        }
        return re;
    }

    @Override
    public void loadAll() {
        currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        loadMouvement(true);
//        loadHistorique(table, true);
    }

    public void loadAllWaiting() {
        currentParam = (YvsComptaParametre) dao.loadOneByNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        loadInfosWarning(false);
        if (isWarning != null ? isWarning : false) {
            loadByWarning();
        } else {
            addParamStatut_(Constantes.STATUT_DOC_ATTENTE, false);
        }
    }

    private void loadByWarning() {
        paginator.clear();
        loadInfosWarning(true);
        addParamIds(true);
        loadMouvement(true, true);
    }

    boolean initForm = true;

    private void addParamInit() {
        if (!autoriser("p_caiss_view_all_user")) {
            if (currentUser.getUsers().getEmploye() == null) {
                currentUser.getUsers().setEmploye(new YvsGrhEmployes((long) -1));
            }
            ParametreRequete p = new ParametreRequete(null, "responsable", currentUser.getUsers(), "=", "AND");
            if (currentUser.getUsers().getEmploye() != null) {
                p.getOtherExpression().add(new ParametreRequete("y.caisse.responsable", "responsable", currentUser.getUsers().getEmploye(), "=", "OR"));
            }
            p.getOtherExpression().add(new ParametreRequete("y.caisse.caissier", "caissier", currentUser.getUsers(), "=", "OR"));
            paginator.addParam(p);
            pagineHistorique.addParam(p);
        }
        if (!autoriser("p_caiss_view_all_limit_time")) { //limite l'accès aux seules pièces de caisse du jour
            ParametreRequete p = new ParametreRequete("y.dateMvt", "dateMvt", new Date(), new Date(), "BETWEEN", "AND");
            paginator.addParam(p);
            pagineHistorique.addParam(p);
        }
        if (!autoriser("p_caiss_view_all")) { //limite l'accès aux pièces jusqu'à une date donnée
            if (currentParam != null) {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.DATE, -currentParam.getJourAnterieur());
                ParametreRequete p = new ParametreRequete("y.dateMvt", "dateMvt", date.getTime(), new Date(), "BETWEEN", "AND");
                paginator.addParam(p);
                pagineHistorique.addParam(p);
            }
        }
    }

    public void loadMouvement(boolean avance) {
        loadMouvement(avance, false);
    }

    public void loadMouvement(boolean avance, boolean vieAllMvt) {
        if (!vieAllMvt) {
            addParamInit();
//            ParametreRequete p0 = new ParametreRequete(null, "mode", "XXX", "=", "AND");
//            p0.getOtherExpression().add(new ParametreRequete("y.model.typeReglement", "type", Constantes.MODE_PAIEMENT_ESPECE, "=", "OR"));
//            p0.getOtherExpression().add(new ParametreRequete("y.id", "", "IS NULL", "IS NULL", "OR"));
//            paginator.addParam(p0);
        }
        paginator.addParam(new ParametreRequete("y.agence.societe", "societe", currentAgence.getSociete(), "=", "AND"));
        if (!autoriser("p_caiss_view_all_societe")) {
            paginator.addParam(new ParametreRequete("y.agence", "agence", currentAgence, "=", "AND"));
        }
        if (!orderDesc) {
            // s'il y a pas de filtrage sur la caisse
            if (!paginator.getParams().contains(new ParametreRequete("caisse"))) {
                return;
            }
            // s'il y a pas de filtrage sur la période
            if (!paginator.getParams().contains(new ParametreRequete("datePaimentPrevu"))) {
                return;
            }
        }
        List<String> tableExterne = new ArrayList<>();
        tableExterne.add("NOTIF_VENTE");
        tableExterne.add("NOTIF_ACHAT");
        paginator.addParam(new ParametreRequete("y.tableExterne", "tableExternes", tableExterne, "NOT IN", "AND"));
        List<Long> ventes = new ArrayList<>();
        List<Long> achats = new ArrayList<>();
        if (addDateF) {
            ventes = dao.loadNameQueries("YvsComptaNotifReglementVente.findIdsPieceByDates", new String[]{"societe", "dateDebut", "dateFin"}, new Object[]{currentAgence.getSociete(), debutF, finF});
            achats = dao.loadNameQueries("YvsComptaNotifReglementAchat.findIdsPieceByDates", new String[]{"societe", "dateDebut", "dateFin"}, new Object[]{currentAgence.getSociete(), debutF, finF});
        }
        if (ventes.isEmpty()) {
            ventes.add(-1L);
        }
        if (achats.isEmpty()) {
            achats.add(-1L);
        }
        List<String> tableAV = new ArrayList<>();
        tableAV.add("DOC_VENTE");
        tableAV.add("DOC_ACHAT");
        ParametreRequete p = new ParametreRequete(null, "tableAV", tableAV, "NOT IN", "AND");
        p.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableAV", tableAV, "NOT IN", "OR"));
        ParametreRequete p1 = new ParametreRequete(null, "not_vente", "DOC_VENTE", "=", "OR");
        p1.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableVente", "DOC_VENTE", "=", "AND"));
        p1.getOtherExpression().add(new ParametreRequete("y.idExterne", "idsVente", ventes, "NOT IN", "AND"));
        p.getOtherExpression().add(p1);
        ParametreRequete p2 = new ParametreRequete(null, "not_achat", "DOC_ACHAT", "=", "OR");
        p2.getOtherExpression().add(new ParametreRequete("y.tableExterne", "tableAchat", "DOC_ACHAT", "=", "AND"));
        p2.getOtherExpression().add(new ParametreRequete("y.idExterne", "idsAchat", achats, "NOT IN", "AND"));
        p.getOtherExpression().add(p2);
        paginator.addParam(p);
        String query = "YvsComptaMouvementCaisse y JOIN FETCH y.caisse LEFT JOIN FETCH y.caissier LEFT JOIN FETCH y.tiersExterne ";
        mouvements = paginator.executeDynamicQuery("y", "y", query, "y." + colOrder + " " + (orderDesc ? "DESC" : "ASC") + ", y.dateSave, y.id", avance, initForm, (orderDesc ? (int) imax : 0), dao);
        prevSolde = giveSoldeInit(!mouvements.isEmpty() ? mouvements.get(0) : null);
        if (!orderDesc) {
            YvsComptaMouvementCaisse mv = mouvements.isEmpty() ? null : mouvements.get(0);
            Calendar c = Calendar.getInstance();
            Date d = (mv != null) ? mv.getDatePaimentPrevu() : (debutF != null) ? debutF : new Date();
            if (d != null) {
                c = Calendar.getInstance();
                c.setTime(d);
                c.add(Calendar.DAY_OF_MONTH, -1);
            }
            YvsComptaMouvementCaisse y = new YvsComptaMouvementCaisse(-1L);
            y.setNumero("S.I");
            y.setTableExterne("SOLDE INITIAL");
            y.setDateMvt(c.getTime());
            y.setDatePaye(c.getTime());
            y.setDatePaimentPrevu(c.getTime());
            mouvements.add(0, y);
        } else {
            YvsComptaMouvementCaisse y = new YvsComptaMouvementCaisse(-1L);
            int idx = mouvements.indexOf(y);
            if (idx >= 0) {
                mouvements.remove(idx);
            }
        }
        try {
            soldePeriode = 0;
            for (YvsComptaMouvementCaisse y : mouvements) {
                if (!y.getNumero().equals("S.I")) {
                    soldePeriode += y.getMouvement().equals(Constantes.MOUV_CAISS_ENTREE) ? y.getMontant() : -y.getMontant();
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedPieceCaisse.class.getName()).log(Level.SEVERE, null, ex);
        }
        update("table_all_pieceCaisse");
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComptaMouvementCaisse> re = paginator.parcoursDynamicData("YvsComptaMouvementCaisse y LEFT JOIN y.model m", "y", "", "y." + colOrder + " " + (orderDesc ? "DESC" : "ASC") + ", y.dateSave, y.id", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void chooseTable(boolean load) {
        if (table != null ? table.trim().length() > 0 : false) {
//            loadHistorique(table, load);
        }
    }

    public void chooseTable_(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            pagineHistorique.addParam(new ParametreRequete("y.tableExterne", "tableExterne", (String) ev.getNewValue(), "=", "AND"));
//            loadHistorique((String) ev.getNewValue(), false);
        }
    }

    public YvsComptaMouvementCaisse convertPieceMissionToMvtCaisse(YvsComptaCaissePieceMission pc) {
        //récupère par une requête l'objet mouvement caisse enregistré
        return (YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findByExterne", new String[]{"idExterne", "table"}, new Object[]{pc.getId(), Constantes.SCR_MISSIONS});
    }

    public List<PieceCaissesMission> convertPieceMissionToMvtCaisse(List<YvsComptaCaissePieceMission> lpc) {
        //récupère par une requête l'objet mouvement caisse enregistré
        PieceCaissesMission mvt;
        List<PieceCaissesMission> re = new ArrayList<>();
        for (YvsComptaCaissePieceMission c : lpc) {
            mvt = new PieceCaissesMission();
            mvt.setId(c.getId());
            mvt.setCaissier(c.getCaissier());
            mvt.setDatePiece(c.getDatePiece());
            mvt.setDatePaiement(c.getDatePaiement());
            mvt.setId(c.getId());
            mvt.setMontant(c.getMontant());
            mvt.setNumeroPiece(c.getNumeroPiece());
            mvt.setStatutPiece(c.getStatutPiece());
            re.add(mvt);
        }
        return re;
    }

    public void validEtapePieceCaisseMission(YvsWorkflowValidPcMission etape) {
        //vérifier que la personne qui valide l'étape a le droit 
        if (!asDroitValideEtape(etape.getEtape())) {
            openNotAcces();
        } else {
        }
    }

    /**
     * ******************************************
     */
    private List<YvsWorkflowValidPcMission> saveEtapesValidation(YvsComptaCaissePieceMission pc) {
        //charge les étape de vailidation
        List<YvsWorkflowValidPcMission> re = new ArrayList<>();
        champ = new String[]{"titre", "societe"};
        val = new Object[]{Constantes.DOCUMENT_PC_MISSION, currentAgence.getSociete()};
        List<YvsWorkflowEtapeValidation> model = dao.loadNameQueries("YvsWorkflowEtapeValidation.findByTitreModel", champ, val);
        if (!model.isEmpty()) {
            YvsWorkflowValidPcMission vm;
            for (YvsWorkflowEtapeValidation et : model) {
                if (et.getActif()) {
                    vm = new YvsWorkflowValidPcMission();
                    vm.setAuthor(currentUser);
                    vm.setEtape(et);
                    vm.setEtapeValid(false);
                    vm.setPiece(pc);
                    vm.setOrdreEtape(et.getOrdreEtape());
                    vm = (YvsWorkflowValidPcMission) dao.save1(vm);
                    re.add(vm);
                }
            }
            pc.setStatutPiece('W');
        } else {
            pc.setStatutPiece('V');
        }
        dao.update(pc);
        return ordonneEtapes(re);
    }

    private List<YvsWorkflowValidPcMission> ordonneEtapes(List<YvsWorkflowValidPcMission> l) {
        return YvsWorkflowValidPcMission.ordonneEtapes(l);
    }

    public void paginer(boolean next) {
        initForm = false;
        loadMouvement(next);
    }

    public void addParamStatut(ValueChangeEvent ev) {
        addParamStatut_((Character) ev.getNewValue(), false);
    }

    public void addParamStatutViewAll(ValueChangeEvent ev) {
        addParamStatut_((Character) ev.getNewValue(), true);
    }

    public void addParamStatut_(Character statut, boolean viewAllMv) {
        statutF = statut;
        ParametreRequete pr = new ParametreRequete("y.statutPiece", "statutPiece", statut, "=", "AND");
        if (statut != null ? statut.equals(Constantes.STATUT_DOC_ATTENTE) || statut.equals(Constantes.STATUT_DOC_EDITABLE) : false) {
            pr = new ParametreRequete(null, "statutPiece", statut, "=", "AND");
            pr.getOtherExpression().add(new ParametreRequete("y.statutPiece", "statutPiece1", Constantes.STATUT_DOC_EDITABLE, "=", "OR"));
            pr.getOtherExpression().add(new ParametreRequete("y.statutPiece", "statutPiece2", Constantes.STATUT_DOC_ATTENTE, "=", "OR"));
            pr.getOtherExpression().add(new ParametreRequete("y.statutPiece", "statutPiece3", Constantes.STATUT_DOC_SOUMIS, "=", "OR"));
        }
        paginator.addParam(pr);
        initForm = true;
        loadMouvement(true, viewAllMv);
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.agence", "agence", null, "=", "AND");
        if (agenceF > 0) {
            p = new ParametreRequete("y.agence", "agence", new YvsAgences(agenceF), "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadMouvement(true);
    }

    public void addParamSourceRapide(ValueChangeEvent ev) {
        ParametreRequete pr = new ParametreRequete("y.tableExterne", "tableExterne", (String) ev.getNewValue());
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator.addParam(pr);
        //Ajoute aussi le filtre sur le statut
        pr = new ParametreRequete("y.statutPiece", "statutPiece", Constantes.STATUT_DOC_ATTENTE);
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator.addParam(pr);
        initForm = true;
        loadMouvement(true);
    }

    public void addParamSource(ValueChangeEvent ev) {
        addParamSource((String) ev.getNewValue(), false);
    }

    public void addParamSourceAll(ValueChangeEvent ev) {
        addParamSource((String) ev.getNewValue(), true);
    }

    public void addParamSource(String source, boolean viewAll) {
        ParametreRequete pr = new ParametreRequete("y.tableExterne", "tableExterne", source);
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator.addParam(pr);
        initForm = true;
        loadMouvement(true, viewAll);
    }

    public void clearParams() {
        clearParams(false);
    }

    public void clearParams(boolean viewAllMvt) {
        addDateF = false;
        debutF = finF = null;
        statutF = null;
        sourceF = null;
        caisseF = 0;
        tiersF = null;
        caissierF = null;
        idsSearch = "";
        numRefF = null;
        paginator.getParams().clear();
        loadMouvement(true, viewAllMvt);
        update("table_all_pieceCaisse");
    }

    public void addParamDate() {
        addParamDate(false);
    }

    public void addParamDate(boolean viewAllMv) {
        ParametreRequete pr = new ParametreRequete("y." + colDate, "datePaimentPrevu", null);
        if (addDateF ? (finF != null && debutF != null) ? !finF.before(debutF) : false : false) {
            pr = new ParametreRequete("y." + colDate, "datePaimentPrevu", debutF, finF, "BETWEEN", "AND");
        }
        paginator.addParam(pr);
        initForm = true;
        loadMouvement(true, viewAllMv);
    }

    public void addParamMontant() {
        ParametreRequete p = new ParametreRequete("y.montant", "montant", null, "=", "AND");
        if (addPrix) {
            if (comparer.equals("BETWEEN")) {
                if (montantDF <= montantFF) {
                    p = new ParametreRequete("y.montant", "montant", montantDF, montantFF, comparer, "AND");
                }
            } else {
                double prix = comparer.equals(Constantes.SYMBOLE_SUP_EGALE) ? montantDF : montantFF;
                p = new ParametreRequete("y.montant", "montant", prix, comparer, "AND");
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadMouvement(true);
    }

    public void addParamCaisse(ValueChangeEvent ev) {
        Long id = (Long) ev.getNewValue();
        addParamCaisse(id, false);
    }

    public void addParamCaisseAll(ValueChangeEvent ev) {
        Long id = (Long) ev.getNewValue();
        addParamCaisse(id, true);
    }

    public void addParamCaisse(Long id, boolean viewAll) {
        if (id != null && id == -1) {
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service != null) {
                service.loadAll(true, 0);
                update("chmp_caisse_piece_divers");
            }
            return;
        }
        ParametreRequete pr = new ParametreRequete("y.caisse", "caisse", (id != null) ? ((id > 0) ? new YvsBaseCaisse(id) : null) : null);
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator.addParam(pr);
        initForm = true;
        loadMouvement(true, viewAll);
    }

    public void addParamModePaiement(ValueChangeEvent ev) {
        Integer id = (Integer) ev.getNewValue();
        modeF = id;
        addParamModePaiement(id, false);
    }

    public void addParamModePaiementAll(ValueChangeEvent ev) {
        Integer id = (Integer) ev.getNewValue();
        modeF = id;
        addParamModePaiement(id, true);
    }

    public void addParamModePaiement(Integer id, boolean viewAll) {
        ParametreRequete pr = new ParametreRequete("y.model", "mode", (id != null) ? ((id > 0) ? new YvsBaseModeReglement(id.longValue()) : null) : null);
        pr.setOperation("=");
        pr.setPredicat("AND");
        paginator.addParam(pr);
        initForm = true;
        loadMouvement(true, viewAll);
    }

    public void addParamTypeModePaiement() {
        ParametreRequete pr = new ParametreRequete("y.model.typeReglement", "typeReglement", null);
        if (Util.asString(typeModeF)) {
            pr = new ParametreRequete("y.model.typeReglement", "typeReglement", typeModeF, "=", "AND");
        }
        paginator.addParam(pr);
        initForm = true;
        loadMouvement(true, true);
    }

    public void addParamTiers() {
        addParamTiers(false);
    }

    public void addParamTiers(boolean viewAll) {
        ParametreRequete p = new ParametreRequete(null, "tiers", null, "LIKE", "AND");
        if (tiersF != null ? tiersF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "tiers", tiersF, "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.nameTiers)", "tiers", tiersF.toUpperCase() + "%", "LIKE", "OR"));

            ParametreRequete p1 = new ParametreRequete(null, "tiersExterne", tiersF, "=", "OR");
            p1.getOtherExpression().add(new ParametreRequete("y.tiersExterne", "tiersExterne", "IS NOT NULL", "IS NOT NULL", "AND"));
            p1.getOtherExpression().add(new ParametreRequete("UPPER(y.tiersExterne.nom)", "tiersExterne1", tiersF.toUpperCase() + "%", "LIKE", "AND"));
//            p.getOtherExpression().add(p1);

            ParametreRequete p2 = new ParametreRequete(null, "tiersInterne", tiersF, "=", "OR");
            p2.getOtherExpression().add(new ParametreRequete("y.tiersInterne", "tiersInterne", "IS NOT NULL", "IS NOT NULL", "AND"));
            p2.getOtherExpression().add(new ParametreRequete("UPPER(y.tiersInterne.nomUsers)", "tiersInterne1", tiersF.toUpperCase() + "%", "LIKE", "AND"));
//            p.getOtherExpression().add(p2);

            ParametreRequete p3 = new ParametreRequete(null, "tiersEmploye", tiersF, "=", "OR");
            p3.getOtherExpression().add(new ParametreRequete("y.tiersEmploye", "tiersEmploye", "IS NOT NULL", "IS NOT NULL", "AND"));
            p3.getOtherExpression().add(new ParametreRequete("UPPER(y.tiersEmploye.nom)", "tiersEmploye1", tiersF.toUpperCase() + "%", "LIKE", "AND"));
//            p.getOtherExpression().add(p3);
        }
        paginator.addParam(p);
        initForm = true;
        loadMouvement(true, viewAll);
    }

    public void addParamCaissier() {
        addParamCaissier(false);
    }

    public void addParamCaissier(boolean viewAll) {
        ParametreRequete p = new ParametreRequete(null, "caissier", "XXX", "LIKE", "AND");
        if (caissierF != null ? caissierF.trim().length() > 0 : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.caissier.codeUsers)", "caissier", "%" + caissierF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.caissier.nomUsers)", "caissier", "%" + caissierF.toUpperCase() + "%", "LIKE", "AND"));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        initForm = true;
        loadMouvement(true, viewAll);
    }

    public void addParamIds() {
        addParamIds(true);
        loadMouvement(true, true);
    }

    public void addParamReference() {
        addParamReference(false);
    }

    public void addParamReference(boolean viewAll) {
        ParametreRequete p = new ParametreRequete(null, "referenceExterne", "XXX", "LIKE", "AND");
        if (numRefF != null ? numRefF.trim().length() > 0 : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.referenceExterne)", "referenceExterne", "%" + numRefF.toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numeroExterne)", "numeroExterne", "%" + numRefF.toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numero)", "numero", "%" + numRefF.toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        initForm = true;
        loadMouvement(true, viewAll);
    }

    public void printBrouillard() {
        printBrouillard("defaut");
    }

    public void printBrouillard(String type) {
        if (caisseF < 1) {
            getErrorMessage("Vous devez precisez une caisse");
            return;
        }
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            w.printBrouillard(new YvsBaseCaisse(caisseF), debutF, finF, type);
        }
    }

    public void chooseCaisse() {
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service != null) {
            long id = pieceCaisse.getCaisse().getId();
            if (id == -1) {
                service.loadAll(true, 0);
            } else {
                int idx = service.getCaisses().indexOf(new YvsBaseCaisse(id));
                if (idx >= 0) {
                    YvsBaseCaisse y = service.getCaisses().get(idx);
                    pieceCaisse.setCaisse(new Caisses(y.getId(), y.getIntitule()));
                }
            }
        }
    }

    public boolean forBonProvisoire(YvsComptaMouvementCaisse y) {
        try {
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                switch (y.getTableExterne()) {
                    case Constantes.SCR_DIVERS: {
                        //mise à jour de la pièce divers
                        YvsComptaCaissePieceDivers pd = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (pd != null ? pd.getId() > 0 : false) {
                            return pd.getJustify() != null ? pd.getJustify().getId() > 0 : false;
                        }
                        break;
                    }
                    case Constantes.SCR_MISSIONS: {
                        //mise à jour de la pièce mission
                        YvsComptaCaissePieceMission pd = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (pd != null ? pd.getId() > 0 : false) {
                            return pd.getJustify() != null ? pd.getJustify().getId() > 0 : false;
                        }
                        break;
                    }
                    case Constantes.SCR_ACHAT: {
                        //mise à jour de la pièce achat
                        YvsComptaCaissePieceAchat pd = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (pd != null ? pd.getId() > 0 : false) {
                            return pd.getJustify() != null ? pd.getJustify().getId() > 0 : false;
                        }
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedPieceCaisse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void print(YvsComptaMouvementCaisse y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                String file = "";
                double montant = y.getMontant();
                switch (y.getTableExterne()) {
                    case Constantes.SCR_ACHAT:
                        YvsComptaCaissePieceAchat pa = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (pa != null ? pa.getId() > 0 : false) {
                            file = "pc_achat";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    case Constantes.SCR_DIVERS:
                        //mise à jour de la pièce divers
                        YvsComptaCaissePieceDivers pd = (YvsComptaCaissePieceDivers) dao.loadOneByNameQueries("YvsComptaCaissePieceDivers.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (pd != null ? pd.getId() > 0 : false) {
                            file = "pc_divers";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    case Constantes.SCR_BON_PROVISOIRE:
                        //mise à jour de la pièce divers
                        YvsComptaBonProvisoire bp = (YvsComptaBonProvisoire) dao.loadOneByNameQueries("YvsComptaBonProvisoire.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (bp != null ? bp.getId() > 0 : false) {
                            file = "pc_bon_provisoire";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    case Constantes.SCR_VENTE:
                        YvsComptaCaissePieceVente pv = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (pv != null ? pv.getId() > 0 : false) {
                            file = "pc_vente";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    case Constantes.SCR_MISSIONS:
                        YvsComptaCaissePieceMission mi = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (mi != null ? mi.getId() > 0 : false) {
                            file = "pc_mission";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    case Constantes.SCR_VIREMENT:
                        YvsComptaCaissePieceVirement vir = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (vir != null ? vir.getId() > 0 : false) {
                            montant = vir.getMontant() + vir.getMontantCout();
                            file = "pc_virement";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    case Constantes.SCR_ACOMPTE_ACHAT:
                        YvsComptaAcompteFournisseur acf = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (acf != null ? acf.getId() > 0 : false) {
                            file = "pc_acompte_achat";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    case Constantes.SCR_ACOMPTE_VENTE:
                        YvsComptaAcompteClient acc = (YvsComptaAcompteClient) dao.loadOneByNameQueries("YvsComptaAcompteClient.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (acc != null ? acc.getId() > 0 : false) {
                            file = "pc_acompte_vente";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    case Constantes.SCR_CREDIT_ACHAT:
                        YvsComptaReglementCreditFournisseur crf = (YvsComptaReglementCreditFournisseur) dao.loadOneByNameQueries("YvsComptaReglementCreditFournisseur.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (crf != null ? crf.getId() > 0 : false) {
                            file = "pc_credit_achat";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    case Constantes.SCR_CREDIT_VENTE:
                        YvsComptaReglementCreditClient crc = (YvsComptaReglementCreditClient) dao.loadOneByNameQueries("YvsComptaReglementCreditClient.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (crc != null ? crc.getId() > 0 : false) {
                            file = "pc_credit_vente";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    case Constantes.SCR_COMMISSION:
                        YvsComptaCaissePieceCommission com = (YvsComptaCaissePieceCommission) dao.loadOneByNameQueries("YvsComptaCaissePieceCommission.findById", new String[]{"id"}, new Object[]{y.getIdExterne()});
                        if (com != null ? com.getId() > 0 : false) {
                            file = "pc_commission";
                        } else {
                            getErrorMessage("Cette pièce n'a pas de source");
                            return;
                        }
                        break;
                    default:
                        break;
                }
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getIdExterne().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutPiece() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(montant));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                if (file != null ? file.trim().length() > 0 : false) {
                    executeReport(file, param);
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedPieceCaisse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getSoldesCaisse() {
        return soldesCaisse;
    }

    public void setSoldesCaisse(List<String> soldesCaisse) {
        this.soldesCaisse = soldesCaisse;
    }

    public double findSoldeCaisse(long idCaisse) {
        return dao.getRecetteCaisse(idCaisse, null, Constantes.STATUT_DOC_PAYER) - dao.getDepenseCaisse(idCaisse, null, Constantes.STATUT_DOC_PAYER);
    }

    public double findSoldeCaisse(long idCaisse, String mode) {
        if (mode.equals(Constantes.MODE_PAIEMENT_ESPECE)) {
            return dao.getTotalCaisse(idCaisse, 0, "", mode, Constantes.STATUT_DOC_PAYER, new Date());
        } else {
            String modes = Constantes.MODE_PAIEMENT_BANQUE + "," + Constantes.MODE_PAIEMENT_SALAIRE + "," + Constantes.MODE_PAIEMENT_COMPENSATION;
            return dao.getTotalCaisse(idCaisse, 0, "", modes, Constantes.STATUT_DOC_PAYER, new Date());
        }
    }

    public String nomTiers(YvsComptaMouvementCaisse mvt) {
        if (mvt != null) {
            String nom = mvt.getNameTiers();
            switch (mvt.getTableExterne()) {
                case Constantes.SCR_VENTE: {
                    YvsComptaCaissePieceVente y = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{mvt.getIdExterne()});
                    if (y != null ? y.getId() > 0 : false) {
                        nom = y.getVente().getNomClient();
                    }
                    break;
                }
                default: {
                    if (mvt.getTiersEmploye() != null ? mvt.getTiersEmploye().getId() > 0 : false) {
                        nom = mvt.getTiersEmploye().getNom_prenom();
                    } else if (mvt.getTiersExterne() != null ? mvt.getTiersExterne().getId() > 0 : false) {
                        nom = mvt.getTiersExterne().getNom_prenom();
                    } else if (mvt.getTiersInterne() != null ? mvt.getTiersInterne().getId() > 0 : false) {
                        nom = mvt.getTiersInterne().getNomUsers();
                    }
                    break;
                }
            }
            return nom;
        }
        return "";
    }

    public double giveSoldeInit(YvsComptaMouvementCaisse mv) {
        if (mv != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(debutF != null ? debutF : new Date());
            c.add(Calendar.DAY_OF_MONTH, -1);
            if (c != null) {
                long ste = currentAgence.getSociete().getId();
                long css = mv.getCaisse() != null ? mv.getCaisse().getId() : 0;
                long mdl = modeF != null ? modeF > 0 ? modeF : 0 : 0;
                return soldeInit = dao.getTotalCaisse(ste, css, mdl, null, typeModeF, Constantes.STATUT_DOC_PAYER, c.getTime());
            } else {
                return 0;
            }
        }
        return 0;
    }

    public double previousSolde(int index) {
        return prevSolde += (mouvements.get(index).getMouvement().equals("R")) ? mouvements.get(index).getMontant() : -mouvements.get(index).getMontant();
    }

    public boolean isComptabiliseBean(YvsComptaMouvementCaisse bean) {
        if (bean != null ? bean.getId() != null : false) {
            switch (selectPiece.getTableExterne()) {
                case Constantes.SCR_DIVERS: {
                    bean.setComptabilise(dao.isComptabilise(bean.getIdExterne(), Constantes.SCR_CAISSE_DIVERS));
                    break;
                }
                case Constantes.SCR_ACHAT: {
                    bean.setComptabilise(dao.isComptabilise(bean.getIdExterne(), Constantes.SCR_CAISSE_ACHAT));
                    break;
                }
                case Constantes.SCR_VENTE: {
                    bean.setComptabilise(dao.isComptabilise(bean.getIdExterne(), Constantes.SCR_CAISSE_VENTE));
                    break;
                }
                case Constantes.SCR_ACOMPTE_ACHAT: {
                    bean.setComptabilise(dao.isComptabilise(bean.getIdExterne(), Constantes.SCR_ACOMPTE_ACHAT));
                    break;
                }
                case Constantes.SCR_ACOMPTE_VENTE: {
                    bean.setComptabilise(dao.isComptabilise(bean.getIdExterne(), Constantes.SCR_ACOMPTE_VENTE));
                    break;
                }
                case Constantes.SCR_CREDIT_ACHAT: {
                    bean.setComptabilise(dao.isComptabilise(bean.getIdExterne(), Constantes.SCR_CAISSE_CREDIT_ACHAT));
                    break;
                }
                case Constantes.SCR_CREDIT_VENTE: {
                    bean.setComptabilise(dao.isComptabilise(bean.getIdExterne(), Constantes.SCR_CAISSE_CREDIT_VENTE));
                    break;
                }
                case Constantes.SCR_VIREMENT: {
                    bean.setComptabilise(dao.isComptabilise(bean.getIdExterne(), Constantes.SCR_VIREMENT));
                    break;
                }
            }
            return bean.isComptabilise();
        }
        return false;
    }

    public void validerAndPayer() {
        ManagedDocDivers service = (ManagedDocDivers) giveManagedBean(ManagedDocDivers.class);
        if (service != null) {
            if (service.validerOrder(true)) {
                changeStaturPayePieceCaisse();
            }
        }
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll();

    }
}
