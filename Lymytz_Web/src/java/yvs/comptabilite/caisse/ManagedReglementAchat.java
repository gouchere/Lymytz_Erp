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
import yvs.commercial.achat.DocAchat;
import yvs.commercial.achat.ManagedFactureAchat;
import yvs.commercial.fournisseur.Fournisseur;
import yvs.commercial.fournisseur.ManagedFournisseur;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.fournisseur.AcompteFournisseur;
import yvs.comptabilite.fournisseur.ManagedOperationFournisseur;
import yvs.comptabilite.tresorerie.ManagedBonProvisoire;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseTrancheReglement;
import yvs.entity.commercial.YvsComParametreAchat;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaPhasePieceAchat;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.entity.compta.achat.YvsComptaNotifReglementAchat;
import yvs.entity.compta.achat.YvsComptaPhaseAcompteAchat;
import yvs.entity.compta.achat.YvsComptaReglementCreditFournisseur;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.compta.divers.YvsComptaJustifBonAchat;
import yvs.entity.compta.fournisseur.YvsComptaPhaseReglementCreditFournisseur;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.enume.Nombre;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedReglementAchat extends Managed implements Serializable {

    private DocAchat selectedDoc = new DocAchat();
    private YvsComptaCaissePieceAchat pieceAchat = newPieceCaiss();
    private boolean displayBtnSave;
    private YvsComptaCaissePieceAchat selectedPiece;
    private YvsComptaCaissePieceAchat selectExtourne;
    private PieceTresorerie pieceAvance = new PieceTresorerie();
    private YvsComptaPhasePieceAchat selectPhaseAchat = new YvsComptaPhasePieceAchat();
    private YvsComptaPhasePieceAchat currentPhaseAchat = new YvsComptaPhasePieceAchat();
    private YvsComptaAcompteFournisseur selectAcompte = new YvsComptaAcompteFournisseur();
    private YvsComptaReglementCreditFournisseur selectCredit = new YvsComptaReglementCreditFournisseur();
    private YvsComptaPhaseAcompteAchat selectPhaseAcompteAchat;
    private YvsComptaPhaseReglementCreditFournisseur selectPhaseCreditAchat;

    private YvsComParametreAchat currentParam;

    private Boolean comptaSearch;
    private long nbrComptaSearch;
    private boolean needConfirmation = false;
    private String fournisseurF, numPieceF;
    private List<YvsComptaCaissePieceAchat> piecesCaisses;
    private boolean dateSearch;
    private long caisseF, agenceSearch;
    private String operateurStatut = "!=";
    private String operateurMode = "=";
    private String operateurMontant = "=";
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();
    private int modeFind;
    private double montantF, montantF1, montantRetour;

    private boolean initForm = true;

    public ManagedReglementAchat() {
        piecesCaisses = new ArrayList<>();
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

    public YvsComParametreAchat getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(YvsComParametreAchat currentParam) {
        this.currentParam = currentParam;
    }

    public YvsComptaPhaseReglementCreditFournisseur getSelectPhaseCreditAchat() {
        return selectPhaseCreditAchat;
    }

    public void setSelectPhaseCreditAchat(YvsComptaPhaseReglementCreditFournisseur selectPhaseCreditAchat) {
        this.selectPhaseCreditAchat = selectPhaseCreditAchat;
    }

    public YvsComptaPhasePieceAchat getCurrentPhaseAchat() {
        return currentPhaseAchat;
    }

    public void setCurrentPhaseAchat(YvsComptaPhasePieceAchat currentPhaseAchat) {
        this.currentPhaseAchat = currentPhaseAchat;
    }

    public YvsComptaCaissePieceAchat getSelectExtourne() {
        return selectExtourne;
    }

    public void setSelectExtourne(YvsComptaCaissePieceAchat selectExtourne) {
        this.selectExtourne = selectExtourne;
    }

    public double getMontantRetour() {
        return montantRetour;
    }

    public void setMontantRetour(double montantRetour) {
        this.montantRetour = montantRetour;
    }

    public YvsComptaPhasePieceAchat getSelectPhaseAchat() {
        return selectPhaseAchat;
    }

    public void setSelectPhaseAchat(YvsComptaPhasePieceAchat selectPhaseAchat) {
        this.selectPhaseAchat = selectPhaseAchat;
    }

    public YvsComptaPhaseAcompteAchat getSelectPhaseAcompteAchat() {
        return selectPhaseAcompteAchat;
    }

    public void setSelectPhaseAcompteAchat(YvsComptaPhaseAcompteAchat selectPhaseAcompteAchat) {
        this.selectPhaseAcompteAchat = selectPhaseAcompteAchat;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public YvsComptaReglementCreditFournisseur getSelectCredit() {
        return selectCredit;
    }

    public void setSelectCredit(YvsComptaReglementCreditFournisseur selectCredit) {
        this.selectCredit = selectCredit;
    }

    public YvsComptaCaissePieceAchat getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(YvsComptaCaissePieceAchat selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    public boolean isNeedConfirmation() {
        return needConfirmation;
    }

    public void setNeedConfirmation(boolean needConfirmation) {
        this.needConfirmation = needConfirmation;
    }

    public long getCaisseF() {
        return caisseF;
    }

    public void setCaisseF(long caisseF) {
        this.caisseF = caisseF;
    }

    public String getOperateurStatut() {
        return operateurStatut;
    }

    public void setOperateurStatut(String operateurStatut) {
        this.operateurStatut = operateurStatut;
    }

    public String getOperateurMode() {
        return operateurMode;
    }

    public void setOperateurMode(String operateurMode) {
        this.operateurMode = operateurMode;
    }

    public String getOperateurMontant() {
        return operateurMontant;
    }

    public void setOperateurMontant(String operateurMontant) {
        this.operateurMontant = operateurMontant;
    }

    public int getModeFind() {
        return modeFind;
    }

    public void setModeFind(int modeFind) {
        this.modeFind = modeFind;
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

    public YvsComptaAcompteFournisseur getSelectAcompte() {
        return selectAcompte;
    }

    public void setSelectAcompte(YvsComptaAcompteFournisseur selectAcompte) {
        this.selectAcompte = selectAcompte;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public List<YvsComptaCaissePieceAchat> getPiecesCaisses() {
        return piecesCaisses;
    }

    public void setPiecesCaisses(List<YvsComptaCaissePieceAchat> piecesCaisses) {
        this.piecesCaisses = piecesCaisses;
    }

    public DocAchat getSelectedDoc() {
        return selectedDoc;
    }

    public void setSelectedDoc(DocAchat selectedDoc) {
        this.selectedDoc = selectedDoc;
    }

    public YvsComptaCaissePieceAchat getPieceAchat() {
        return pieceAchat;
    }

    public void setPieceAchat(YvsComptaCaissePieceAchat pieceAchat) {
        this.pieceAchat = pieceAchat;
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

    public String getFournisseurF() {
        return fournisseurF;
    }

    public void setFournisseurF(String fournisseurF) {
        this.fournisseurF = fournisseurF;
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteBeanExtourne() {
        try {
            if (selectExtourne != null ? selectExtourne.getId() > 0 : false) {
                if (selectExtourne.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                    getErrorMessage("Le reglement est déjà payé");
                    return;
                }
                dao.delete(selectExtourne);
                pieceAvance.getSousAchats().remove(selectExtourne);
                succes();
                update("data_extourne_caisse_achat");
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

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            onSelectedFacture((YvsComDocAchats) ev.getObject());
        }
    }

    public void onSelectedFacture(YvsComDocAchats dv) {
        loadDataAcompteFsseur(dv.getFournisseur());
        selectedDoc = UtilCom.buildBeanDocAchat(dv);
        ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
        if (service != null) {
            service.setMontantTotalDoc(selectedDoc);
        }
        pieceAvance.setDocAchat(selectedDoc);
        pieceAvance.setFournisseur(pieceAvance.getDocAchat().getFournisseur());
        displayBtnSave = false;
        pieceAchat = newPieceCaiss();
        pieceAvance.setMontant(pieceAchat.getMontant());
        update("all_bloc_info_reg_FA");
        update("zone_pcv_edit_pieceFA");
        update("txt_reference_facture_reg_FA");
    }

    public void loadDataAcompteFsseur(YvsBaseFournisseur bean) {
        List<YvsComptaAcompteFournisseur> acomptes = new ArrayList<>();
        String query = "SELECT y.id FROM yvs_compta_acompte_fournisseur y WHERE y.montant > COALESCE((SELECT SUM(r.montant) FROM yvs_compta_caisse_piece_achat r INNER JOIN yvs_compta_notif_reglement_achat n ON n.piece_achat = r.id WHERE n.acompte = y.id AND r.statut_piece = 'P'), 0) AND y.statut = 'P' AND y.fournisseur = ?";
        List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(bean.getId(), 1)});
        if (ids != null ? !ids.isEmpty() : false) {
            acomptes = dao.loadNameQueries("YvsComptaAcompteFournisseur.findByIds", new String[]{"ids"}, new Object[]{ids});
        }
        bean.setAcompte(0);
        for (YvsComptaAcompteFournisseur f : acomptes) {
            bean.setAcompte(bean.getAcompte() + f.getReste());
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void chooseModelReglement() {
        if (selectedDoc.getModeReglement() != null ? selectedDoc.getModeReglement().getId() > 0 : false) {
            ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
            if (m != null) {
                int idx = m.getModels().indexOf(new YvsBaseModelReglement(selectedDoc.getModeReglement().getId()));
                if (idx >= 0) {
                    YvsBaseModelReglement y = m.getModels().get(idx);
                    selectedDoc.setModeReglement(UtilCom.buildBeanModelReglement(y));
                }
            }
        }
    }

    @Override
    public void doNothing() {

    }

    @Override
    public void loadAll() {
        if (giveExerciceActif() != null) {
//            dao.getEquilibreAchat(giveExerciceActif().getDateDebut(), giveExerciceActif().getDateFin());
        }
        if (pieceAchat != null ? (pieceAchat.getId() != null ? pieceAchat.getId() < 1 : true) : true) {
            pieceAchat = new YvsComptaCaissePieceAchat();
        }
        if (pieceAchat.getCaisse() != null ? pieceAchat.getCaisse().getId() < 1 : true) {
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service != null) {
                pieceAchat.setCaisse(service.findByResponsable(currentUser.getUsers()));
            } else {
                pieceAchat.setCaisse(new YvsBaseCaisse());
            }
        }
        if (pieceAchat.getModel() != null ? pieceAchat.getModel().getId() < 1 : true) {
            pieceAchat.setModel(modeEspece());
        }

        if (pieceAvance != null ? pieceAvance.getId() < 1 : true) {
            pieceAvance = new PieceTresorerie();
        }
        if (pieceAvance.getCaisse() != null ? pieceAvance.getCaisse().getId() < 1 : true) {
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service != null) {
                pieceAvance.setCaisse(UtilCompta.buildBeanCaisse(service.findByResponsable(currentUser.getUsers())));
            } else {
                pieceAvance.setCaisse(new Caisses());
            }
        }
        if (pieceAvance.getMode() != null ? pieceAvance.getMode().getId() < 1 : true) {
            pieceAvance.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        }
    }

    private YvsComptaCaissePieceAchat buildPieceFromModel(long id, YvsBaseModeReglement mode, DocAchat d, YvsBaseCaisse caisse, Date date, double montant) {
        YvsComptaCaissePieceAchat piece = new YvsComptaCaissePieceAchat(id);
        piece.setAuthor(currentUser);
        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        piece.setMontant(montant);
        piece.setDatePaimentPrevu(date);
        piece.setDatePiece(new Date());
        String ref = genererReference(Constantes.TYPE_PC_ACHAT_NAME, piece.getDatePiece());
        if (ref != null ? ref.trim().length() < 1 : true) {
            return null;
        }
        piece.setNumeroPiece(ref);
        piece.setAchat(UtilCom.buildDocAchat(d, currentUser, currentAgence));
        piece.setModel(mode);
        piece.setFournisseur(new YvsBaseFournisseur(d.getFournisseur().getId()));
        if ((caisse != null) ? caisse.getId() > 0 : false) {
            piece.setCaisse(caisse);
        }
        return piece;
    }

    public List<YvsComptaCaissePieceAchat> generetedPiecesFromModel(YvsBaseModelReglement model, DocAchat d) {
        //trouve la caisse de l'utilisateur si elle existe
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        return generetedPiecesFromModel(model, d, (service != null) ? service.findByResponsable(currentUser.getUsers()) : null);
    }

    public List<YvsComptaCaissePieceAchat> generetedPiecesFromModel(YvsBaseModelReglement model, DocAchat d, YvsBaseCaisse caisse) {
        List<YvsComptaCaissePieceAchat> re = new ArrayList<>();
        List<YvsBaseTrancheReglement> lt = dao.loadNameQueries("YvsBaseTrancheReglement.findByModel", new String[]{"model"}, new Object[]{model});
        if (d.getMontantNetApayer() > 0) {
            long id = -1000;
            YvsComptaCaissePieceAchat piece;
            Calendar cal = Calendar.getInstance();
            cal.setTime(d.getDateDoc());  //date de la facturation
            double totalTaux = 0, sommeMontant = 0;
            YvsBaseModeReglement espece = modeEspece(), mode = null;
            YvsBaseTrancheReglement trch;
            if (lt != null ? !lt.isEmpty() : false) {
                for (int i = 0; i < lt.size() - 1; i++) {
                    trch = lt.get(i);
                    cal.add(Calendar.DAY_OF_MONTH, trch.getIntervalJour());
                    double montant = arrondi(d.getMontantNetApayer() * trch.getTaux() / 100);
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
            double montant = d.getMontantNetApayer() - sommeMontant;
            piece = buildPieceFromModel(id++, mode, d, caisse, cal.getTime(), montant);
            re.add(piece);
        } else {
            getErrorMessage("Le montant du document n'a pas été trouvé !");
        }
        return re;
    }

    public YvsComptaCaissePieceAchat newPieceCaiss() {
        YvsComptaCaissePieceAchat pc = new YvsComptaCaissePieceAchat((long) 0);
        pc.setAchat(new YvsComDocAchats((long) 0));
        pc.setCaisse(new YvsBaseCaisse((long) 0));
        pc.setDatePaimentPrevu(new Date());
        pc.setModel(modeEspece());
        pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        double montant = 0;
        if (selectedDoc != null) {
            montant = selectedDoc.getMontantTotal();
            if (selectedDoc.getReglements() != null ? !selectedDoc.getReglements().isEmpty() : false) {
                montant -= giveTotalPT(selectedDoc.getReglements());
            }
        }
        pc.setMontant(montant);
        if (pc.getMontant() < 0) {
            pc.setMontant(0.0);
        }
        return pc;
    }

    public YvsComptaCaissePieceAchat createNewPieceCaisse(DocAchat d, YvsComptaCaissePieceAchat pt, boolean delete) {
        return createNewPieceCaisse(d, pt, delete, false);
    }

    public YvsComptaCaissePieceAchat createNewPieceCaisse(DocAchat d, YvsComptaCaissePieceAchat pt, boolean delete, boolean validePhase) {
        return createNewPieceCaisse(d, pt, delete, validePhase, false);
    }

    public YvsComptaCaissePieceAchat createNewPieceCaisse(DocAchat d, YvsComptaCaissePieceAchat pt, boolean delete, boolean validePhase, boolean silence) {
        return createNewPieceCaisse(UtilCom.buildDocAchat(d, currentUser, currentAgence), pt, delete, validePhase, silence);
    }

    public YvsComptaCaissePieceAchat createNewPieceCaisse(YvsComDocAchats d, YvsComptaCaissePieceAchat pt, boolean delete, boolean validePhase, boolean silence) {
        if (controleAddPiece(d, pt, silence)) {
            YvsComptaCaissePieceAchat piece = new YvsComptaCaissePieceAchat();
            piece.setAuthor(currentUser);
            piece.setStatutPiece(pt.getStatutPiece());
            piece.setMontant(arrondi(pt.getMontant()));
            if (d.getFournisseur() != null ? d.getFournisseur().getId() > 0 : false) {
                piece.setFournisseur(new YvsBaseFournisseur(d.getFournisseur().getId()));
            }
            piece.setDatePaimentPrevu(pt.getDatePaimentPrevu());
            piece.setDatePaiement(pt.getDatePaiement());
            piece.setNumeroPiece(pt.getNumeroPiece());
            piece.setDatePiece(pt.getDatePiece());
            piece.setDateUpdate(new Date());
            piece.setAchat(d);
            piece.setReferenceExterne(pt.getReferenceExterne());

            if (pt.getCaisse() != null) {
                piece.setCaisse((pt.getCaisse().getId() > 0) ? pt.getCaisse() : null);
            }
            if (pt.getBonProvisoire() != null ? pt.getBonProvisoire().getId() > 0 : false) {
                piece.setBonProvisoire(new YvsComptaBonProvisoire(pt.getBonProvisoire().getId(), pt.getBonProvisoire().getNumero()));
            }
            if (piece.getId() <= 0 || (piece.getNumeroPiece() != null ? piece.getNumeroPiece().trim().length() < 1 : true)) {
                String numero = genererReference(Constantes.TYPE_PC_ACHAT_NAME, pt.getDatePiece(), pt.getCaisse().getId());
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
                piece = (YvsComptaCaissePieceAchat) dao.save1(piece);
                if (pt.getBonProvisoire() != null ? pt.getBonProvisoire().getId() > 0 : false) {
                    YvsComptaJustifBonAchat bon = new YvsComptaJustifBonAchat(piece, pt.getBonProvisoire());
                    bon.setAuthor(currentUser);
                    bon = (YvsComptaJustifBonAchat) dao.save1(bon);
                    piece.setJustify(bon);
                }
            } else {
                piece.setId(pt.getId());
                piece = (YvsComptaCaissePieceAchat) dao.update(piece);
            }
            piece.setNew_(true);
            piece.setPhasesReglement(dao.loadNameQueries("YvsComptaPhasePieceAchat.findByPiece", new String[]{"piece"}, new Object[]{pt}));
            if (!validePhase) {
                if (!pt.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) { // Si le paiement n'est pas par banque, on se rassure qu'aucune phases n'est rattaché. auquel cas, on les supprime
                    boolean ph_ok = false;
                    if (!delete) {
                        for (YvsComptaPhasePieceAchat pp : piece.getPhasesReglement()) {
                            if (pp.getPhaseOk()) {
                                ph_ok = true;
                                break;
                            }
                        }
                    }
                    if (!ph_ok) {
                        for (YvsComptaPhasePieceAchat pp : piece.getPhasesReglement()) {
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
                    //efface les notifs
                    if (pt.getNotifs() != null) {
                        dao.delete(pt.getNotifs());
                    }
                }
            } else {
                for (YvsComptaPhasePieceAchat pp : piece.getPhasesReglement()) {
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
                }
            }
            return piece;
        } else {
            pieceAchat = newPieceCaiss();
        }
        return null;
    }

    public void createOnePieceCaisse(YvsComDocAchats d) {
        //build pièce trésorerie
        selectedDoc = UtilCom.buildBeanDocAchat(d);
        ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
        if (service != null) {
            service.setMontantTotalDoc(selectedDoc);
        }
        YvsComptaCaissePieceAchat pt = new YvsComptaCaissePieceAchat();
        pt.setAuthor(currentUser);
        ManagedCaisses service_ = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (service_ != null) {
            pt.setCaisse(service_.findByResponsable(currentUser.getUsers()));
        }//trouve la caisse par défaut du vendeur
        pt.setDatePaiement(null);
        pt.setDatePaimentPrevu(new Date());
        pt.setDatePiece(new Date());
        pt.setId(null);
        pt.setMontant(arrondi(selectedDoc.getMontantNetApayer()));
        pt.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        pt.setAchat(d);
        pt.setModel(modeEspece());
        createNewPieceCaisse(selectedDoc, pt, false);
        selectedDoc.setReglements(dao.loadNameQueries("YvsComptaCaissePieceAchat.findByFacture", new String[]{"facture"}, new Object[]{d}));
        displayBtnSave = false;
    }

    public double giveTotalPT(List<YvsComptaCaissePieceAchat> l) {
        double sum = 0;
        if (l != null) {
            for (YvsComptaCaissePieceAchat p : l) {
                if (p.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && p.getStatutPiece() != Constantes.STATUT_DOC_ANNULE) {
                    sum += p.getMontant();
                }
            }
        }
        return sum;
    }

    private boolean controleAddPiece(YvsComDocAchats d, YvsComptaCaissePieceAchat pt, boolean silence) {
        return controleAddPiece(d, pt, true, silence);
    }

    private boolean controleAddPiece(YvsComDocAchats d, YvsComptaCaissePieceAchat pt, boolean valider, boolean silence) {
        if (pt.getId() != null ? pt.getId() < 0 : false) {
            if (!silence) {
                getErrorMessage("Vous devez d'abord enregistrer la generation");
            }
            return false;
        }
        if (pt.getModel() != null ? pt.getModel().getId() < 1 : true) {
            if (!silence) {
                getErrorMessage("Vous devez precisez le mode de paiement");
            }
            return false;
        }
        if (pt.getDatePaimentPrevu() == null) {
            if (!silence) {
                getErrorMessage("Vous devez préciser la date de paiement !");
            }
            return false;
        }
        System.out.println("valider : " + valider);
        System.out.println("d : " + d);
        if (d != null ? !pt.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) : false) {
            double mtn = 0;
            if (d.getReglements().contains(pt)) {
                mtn = (-d.getReglements().get(d.getReglements().indexOf(pt)).getMontant() + pt.getMontant());
            } else {
                mtn = pt.getMontant();
            }
            if (d.getMontantTotal() < (giveTotalPT(d.getReglements()) + mtn) && pt.getAchat() != null) {
                if (!silence) {
                    getErrorMessage("Le montant de la facture ne doit pas être inférieure aux règlements planifié");
                }
                return false;
            }
            if (valider) {
                boolean correct = d.getStatut().equals(Constantes.ETAT_VALIDE);
                if (!correct) {
                    if (d.getEtapesValidations() != null ? !d.getEtapesValidations().isEmpty() : false) {
                        for (YvsWorkflowValidFactureAchat w : d.getEtapesValidations()) {
                            if (w.getEtapeValid() ? w.getEtape().getReglementHere() : false) {
                                correct = true;
                                break;
                            }
                        }
                    }
                }
                if (!correct) {
                    if (!silence) {
                        getErrorMessage("La facture n'est pas encore validée !");
                    }
                    return false;
                }
            }
        }
        if (pt.getStatutPiece() == Constantes.STATUT_DOC_PAYER && (pt.getId() != null) ? pt.getId() > 0 : false) {
            if (!silence) {
                getErrorMessage("La pièce en cours est déjà payé !");
            }
            return false;
        }
        if (!verifyDateExercice(pt.getDatePaiement())) {
            return false;
        }
        return true;
    }

    public void generatedAllpiece() {
        if (!selectedDoc.getReglements().isEmpty()) {
            for (YvsComptaCaissePieceAchat c : selectedDoc.getReglements()) {
                if (c.getId() > 0) {
                    getErrorMessage("Impossible de générer un nouveau modèle de règlement !", "Un plan existe déjà");
                    return;
                }
            }
            selectedDoc.getReglements().clear();
        }
        if (selectedDoc.getModeReglement() != null) {
            if (selectedDoc.getModeReglement().getId() > 0) {
                selectedDoc.setReglements(generetedPiecesFromModel(new YvsBaseModelReglement(selectedDoc.getModeReglement().getId()), selectedDoc));
                displayBtnSave = true;
            } else {
                getErrorMessage("La facture de achat n'est rattaché à aucun model de règlement !");
            }
        } else {
            getErrorMessage("La facture de achat n'est rattaché à aucun model de règlement !");
        }
    }

    public void chooseModeReglement(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            Integer id = (int) ev.getNewValue();
            if (id != null ? id > 0 : false) {
                ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (m != null) {
                    int idx = m.getModes().indexOf(new YvsBaseModeReglement(id.longValue()));
                    if (idx > -1) {
                        YvsBaseModeReglement o = m.getModes().get(idx);
                        pieceAvance.setMode(UtilCompta.buildBeanModeReglement(o));
                    }
                }
            }
        }
    }

    public void createNewPiece(boolean reset) {
        if (pieceAvance.getDocAchat().getId() > 0) {
            List<YvsComptaCaissePieceAchat> l = new ArrayList<>();
            if (!pieceAvance.isAcompte()) {
                pieceAchat = createNewPieceCaisse(pieceAvance.getDocAchat(), UtilCompta.buildTresoreriAchat(pieceAvance, currentUser), true);
                if (pieceAchat != null ? pieceAchat.getId() > 0 : false) {
                    pieceAvance.setId(pieceAchat.getId());
                    l.add(pieceAchat);
                }
            } else {
                l.addAll(bindPieceAndAcompte());
            }
            for (YvsComptaCaissePieceAchat pcv : l) {
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
                    int idx = pieceAvance.getDocAchat().getReglements().indexOf(pcv);
                    if (idx > -1) {
                        pieceAvance.getDocAchat().getReglements().set(idx, pcv);
                    } else {
                        pieceAvance.getDocAchat().getReglements().add(0, pcv);
                    }
                    idx = piecesCaisses.indexOf(pcv);
                    if (idx > -1) {
                        piecesCaisses.set(idx, pcv);
                    } else {
                        piecesCaisses.add(0, pcv);
                    }
                    update("table_reg_facture_FA");
                    ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (service != null) {
                        service.setMontantTotalDoc(pieceAvance.getDocAchat());
                        int i = service.getDocuments().indexOf(new YvsComDocAchats(pieceAvance.getDocAchat().getId()));
                        if (i >= 0) {
                            int j = service.getDocuments().get(i).getReglements().indexOf(pcv);
                            if (j > -1) {
                                service.getDocuments().get(i).getReglements().set(j, pcv);
                            } else {
                                service.getDocuments().get(i).getReglements().add(0, pcv);
                            }
                            update("data_facture_achat_reg");
                        }
                    }
                    if (reset) {
                        pieceAchat = newPieceCaiss();
                    }
                    succes();
                }
            }
        } else {
            if (pieceAvance.getFournisseur().getId() > 0) {
                ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                if (w != null) {
                    AcompteFournisseur compte = new AcompteFournisseur();
                    compte.setFournisseur(pieceAvance.getFournisseur());
                    compte.setDateAcompte(pieceAvance.getDatePaiementPrevu());
                    compte.setMontant(pieceAvance.getMontant());
                    compte.setCaisse(pieceAvance.getCaisse());
                    compte.setMode(pieceAvance.getMode());
                    compte.setReferenceExterne(pieceAvance.getNumRefExterne());
                    YvsComptaAcompteFournisseur y = w.saveAcompte(compte);
                    if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                        pieceAchat = newPieceCaiss();
                        pieceAvance = new PieceTresorerie();
                    }
                }
            } else {
                getErrorMessage("Vous devez precisez un fournisseur pour créer un acompre ou précisez une facture pour créer un reglement");
            }
        }
    }

    public void chooseCaisse() {
        if (pieceAvance.getCaisse() != null) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (w != null) {
                long id = pieceAvance.getCaisse().getId();
                if (id == -1) {
                    w.loadAll(true, 0);
                } else {
                    int idx = w.getCaisses().indexOf(new YvsBaseCaisse(id));
                    if (idx >= 0) {
                        YvsBaseCaisse y = w.getCaisses().get(idx);
                    pieceAvance.setCaisse(UtilCompta.buildBeanCaisse(y));
                    }
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

    private List<YvsComptaCaissePieceAchat> bindPieceAndAcompte() {
        List<YvsComptaCaissePieceAchat> re = new ArrayList<>();
        List<Long> ids;
        if (pieceAvance.getId() <= 0) {
            ids = findIdAcomptesEnCours(pieceAvance.getDocAchat().getFournisseur().getId(), false);
        } else {
            ids = findIdAcomptesEnCours(pieceAvance.getDocAchat().getFournisseur().getId(), true);
            if (pieceAvance.getNotifRegVente() != null ? pieceAvance.getNotifRegVente().getId() != null ? pieceAvance.getNotifRegVente().getId() > 0 : false : false) {
                ids.add(pieceAvance.getNotifRegVente().getAcompte().getId());
            }
        }
        if (!ids.isEmpty()) {
            List<YvsComptaAcompteFournisseur> list = dao.loadNameQueries("YvsComptaAcompteFournisseur.findByIds", new String[]{"ids"}, new Object[]{ids});
            if (pieceAvance.getNotifRegAchat() != null ? pieceAvance.getNotifRegAchat().getId() != null ? pieceAvance.getNotifRegAchat().getId() > 0 : false : false) {
                list.remove(pieceAvance.getNotifRegAchat().getAcompte());
                list.add(0, pieceAvance.getNotifRegAchat().getAcompte());
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

    private List<Long> findIdAcomptesEnCours(long idFournisseur, boolean sup) {
        List<Long> ids = new ArrayList<>();
        String query = " SELECT y.id FROM yvs_compta_acompte_fournisseur y "
                + " WHERE y.statut='P' AND y.fournisseur=?"
                + " AND ((y.montant-(COALESCE((SELECT SUM(p.montant) FROM yvs_compta_notif_reglement_achat n INNER JOIN yvs_compta_caisse_piece_achat p ON p.id=n.piece_achat WHERE n.acompte=y.id),0)))>0) ";
        Options[] ops = new Options[1];
        ops[0] = new Options(idFournisseur, 1);
        if (sup) {
//            query = query + " LIMIT 1";
        }
        ids = dao.loadListBySqlQuery(query, ops);
        return ids;
    }

    private List<YvsComptaCaissePieceAchat> createAndBindPieceAcompte(Double montant, List<YvsComptaAcompteFournisseur> list) {
        double reste;
        YvsComptaCaissePieceAchat piece;
        List<YvsComptaCaissePieceAchat> re = new ArrayList<>();
        for (YvsComptaAcompteFournisseur c : list) {
            if (montant > 0) {
                reste = c.getResteUnBind(pieceAvance.getId());
                if (reste <= montant) {
                    // génère la pièce de règlement qui correspond au reste
                    piece = buildPiece(reste, c);
                    System.err.println(" Silence----");
                    piece = createNewPieceCaisse(pieceAvance.getDocAchat(), piece, true, true, true);
                    if (piece != null) {
                        bindPieceAcompte(c, piece);
                        re.add(piece);
                    }
                    montant = montant - reste;
                    pieceAvance.setId(-c.getId());
                } else {
                    // génère la pièce de règlement qui correspond au montantF
                    piece = buildPiece(montant, c);
                    piece = createNewPieceCaisse(pieceAvance.getDocAchat(), piece, true, true);
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

    private YvsComptaCaissePieceAchat buildPiece(double montant, YvsComptaAcompteFournisseur a) {
        YvsComptaCaissePieceAchat p = new YvsComptaCaissePieceAchat(pieceAvance.getId());
        p.setCaisse(a.getCaisse());
        p.setNumeroPiece(pieceAvance.getNumPiece());
        p.setDatePaiement(pieceAvance.getDatePiece());
        p.setDatePaimentPrevu(pieceAvance.getDatePiece());
        p.setDatePiece(pieceAvance.getDatePiece());
        p.setModel(a.getModel());
        p.setMontant(montant);
        p.setNote("Paiement par Acompte fournisseur");
        p.setReferenceExterne(a.getNumRefrence());
        p.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        p.setAchat(UtilCom.buildDocAchat(pieceAvance.getDocAchat(), currentUser, currentAgence));
        return p;
    }

    private void bindPieceAcompte(YvsComptaAcompteFournisseur a, YvsComptaCaissePieceAchat p) {
        ManagedOperationFournisseur service = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (service != null) {
            YvsComptaNotifReglementAchat y = service.confirmBind(a, p);
        }
    }

    public void saveModelreglement() {
        if (selectedDoc.getId() > 0) {
            ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            for (YvsComptaCaissePieceAchat c : selectedDoc.getReglements()) {
                c.setId(Long.valueOf(0));
                c = createNewPieceCaisse(selectedDoc, c, false);
                if (service != null) {
                    service.setMontantTotalDoc(selectedDoc);
                    int idx = service.getDocuments().indexOf(new YvsComDocAchats(selectedDoc.getId()));
                    if (idx > -1) {
                        int index = service.getDocuments().get(idx).getReglements().indexOf(c);
                        if (index > -1) {
                            service.getDocuments().get(idx).getReglements().set(index, c);
                        } else {
                            service.getDocuments().get(idx).getReglements().add(c);
                        }
                    }
                }
            }
            selectedDoc.setReglements(dao.loadNameQueries("YvsComptaCaissePieceAchat.findByFacture", new String[]{"facture"}, new Object[]{new YvsComDocAchats(selectedDoc.getId())}));

            if (service != null) {
                service.setMontantTotalDoc(selectedDoc);
            }
            displayBtnSave = false;
        } else {
            getErrorMessage("Aucune facture n'a été selectionné !");
        }
    }

    private void recopieObject(YvsComptaCaissePieceAchat pc) {
        pieceAchat = pc;
        if (pieceAchat.getModel() == null) {
            pieceAchat.setModel(modeEspece());
        }
        if (pieceAchat.getCaisse() == null) {
            pieceAchat.setCaisse(new YvsBaseCaisse(-(long) 10));
        }
        pieceAvance = UtilCompta.buildBeanTresoreri(pc);
        ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
        if (service != null) {
            service.setMontantTotalDoc(pieceAvance.getDocAchat());
        }
    }

    public void selectOnePiece(SelectEvent ev) {
        onSelectObject((YvsComptaCaissePieceAchat) ev.getObject());
        selectedPiece = (YvsComptaCaissePieceAchat) ev.getObject();
    }

    public void onSelectDistant(YvsComptaCaissePieceAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Règlements Achats", "modCompta", "smenRegAchat", true);
            }
        }
    }

    public void onSelectObject(YvsComptaCaissePieceAchat y) {
        recopieObject(y);
        if (pieceAchat.getModel() == null) {
            pieceAchat.setModel(modeEspece());
        }
        if (pieceAchat.getCaisse() == null) {
            pieceAchat.setCaisse(new YvsBaseCaisse(-(long) 10));
        }
    }

    public void unselectOnePiece(UnselectEvent ev) {
        pieceAchat = newPieceCaiss();
    }

    public void chooseCaissePhaseAchat() {
        if (currentPhaseAchat.getCaisse() != null ? currentPhaseAchat.getCaisse().getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (w != null) {
                int idx = w.getCaisses().indexOf(currentPhaseAchat.getCaisse());
                if (idx > -1) {
                    currentPhaseAchat.setCaisse(new YvsBaseCaisse(w.getCaisses().get(idx)));
                }
            }
        }
    }

    public void onSelectObjectForCheque(YvsComptaCaissePieceAchat y) {
        pieceAchat = y;
        pieceAchat.setPhasesReglement(dao.loadNameQueries("YvsComptaPhasePieceAchat.findByPiece", new String[]{"piece"}, new Object[]{y}));
        pieceAvance = UtilCompta.buildBeanTresoreri(y);
        pieceAvance.setPhasesAchat(ordonnePhase(pieceAvance.getPhasesAchat()));
        if (!piecesCaisses.contains(y)) {
            piecesCaisses.add(0, y);
        }
        if (pieceAvance.getPhasesAchat() != null ? !pieceAvance.getPhasesAchat().isEmpty() : false) {
            for (YvsComptaPhasePieceAchat r : pieceAvance.getPhasesAchat()) {
                if (r.isEtapeActive()) {
                    currentPhaseAchat = r;
                    break;
                }
            }
        }
        if (currentPhaseAchat.getCaisse() == null) {
            currentPhaseAchat.setCaisse(new YvsBaseCaisse(y.getCaisse()));
        }
        currentPhaseAchat.setDateValider(new Date());
    }

    public void onSelectAcompteForCheque(YvsComptaAcompteFournisseur y) {
        selectAcompte = y;
        selectAcompte.setPhasesReglement(dao.loadNameQueries("YvsComptaPhaseAcompteAchat.findByPiece", new String[]{"piece"}, new Object[]{y}));
        selectAcompte.setPhasesReglement(ordonnePhases(selectAcompte.getPhasesReglement()));
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            if (selectAcompte.getPhasesReglement() != null ? !selectAcompte.getPhasesReglement().isEmpty() : false) {
                for (YvsComptaPhaseAcompteAchat r : selectAcompte.getPhasesReglement()) {
                    if (r.isEtapeActive()) {
                        w.setCurrentPhaseAcompteAchat(r);
                        break;
                    }
                }
            }
            if (w.getCurrentPhaseAcompteAchat().getCaisse() == null) {
                w.getCurrentPhaseAcompteAchat().setCaisse(new YvsBaseCaisse(y.getCaisse()));
            }
            w.getCurrentPhaseAcompteAchat().setDateValider(new Date());
        }
    }

    public void onSelectCreditForCheque(YvsComptaReglementCreditFournisseur y) {
        selectCredit = y;
        selectCredit.setPhasesReglement(ordonnePhasec(selectCredit.getPhasesReglement()));
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            if (selectCredit.getPhasesReglement() != null ? !selectCredit.getPhasesReglement().isEmpty() : false) {
                for (YvsComptaPhaseReglementCreditFournisseur r : selectCredit.getPhasesReglement()) {
                    if (r.isEtapeActive()) {
                        w.setCurrentPhaseCreditAchat(r);
                        break;
                    }
                }
            }
            if (w.getCurrentPhaseCreditAchat().getCaisse() == null) {
                w.getCurrentPhaseCreditAchat().setCaisse(new YvsBaseCaisse(y.getCaisse()));
            }
            w.getCurrentPhaseCreditAchat().setDateValider(new Date());
        }
    }

    public void choixLinePiece(SelectEvent ev) {
        onSelectObjectForCheque((YvsComptaCaissePieceAchat) ev.getObject());
    }

    public void deletePieceCaisse(boolean all) {
        if (pieceAchat.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
            try {
                boolean comptabilise = dao.isComptabilise(pieceAchat.getId(), Constantes.SCR_CAISSE_ACHAT);
                if (comptabilise) {
                    if (!all) {
                        getErrorMessage("Cette piece est déja comptabilisée");
                    }
                    return;
                }
                pieceAchat.setAuthor(currentUser);
                dao.delete(pieceAchat);
                if (selectedDoc.getReglements().contains(pieceAchat)) {
                    selectedDoc.getReglements().remove(pieceAchat);
                }
                if (piecesCaisses != null) {
                    piecesCaisses.remove(pieceAchat);
                }
                if (!all) {
                    succes();
                }
                pieceAchat = newPieceCaiss();
                if (selectedDoc.getReglements().isEmpty()) {
                    displayBtnSave = false;
                }
                resetFicheAfterSave(true);
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cet élément !");
            }
        } else {
            getErrorMessage("Cette pièce est déjà encaissé !");
        }
    }

    public void validePiece() {
        pieceAchat.setDatePaiement(new Date());
        validePiece(pieceAchat, true);
    }

    public boolean validePiece(YvsComptaCaissePieceAchat pv, boolean succes) {
        if (pv != null ? pv.getId() > 0 : false) {
            if (currentParam == null) {
                currentParam = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if ((pv.getCaisse() != null) ? pv.getCaisse().getId() > 0 : false) {
                pv.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{pv.getCaisse().getId()}));
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
                    for (YvsComptaNotifReglementAchat n : pv.getNotifs().getAcompte().getNotifs()) {
                        if (n.getPieceAchat().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                            montant += n.getPieceAchat().getMontant();
                        }
                    }
                    if (montant > pv.getNotifs().getAcompte().getMontant()) {
                        getErrorMessage("Vous ne pouvez pas valider ce montant.. car la somme des pièces excedera le montant de l'acompte");
                        return false;
                    }
                }
                if (!currentParam.getPaieWithoutValide()) {
                    boolean correct = false;
                    if (pv.getAchat().getEtapesValidations() != null ? pv.getAchat().getEtapesValidations().isEmpty() : false) {
                        for (YvsWorkflowValidFactureAchat w : pv.getAchat().getEtapesValidations()) {
                            if (w.getEtapeValid() ? w.getEtape().getReglementHere() : false) {
                                correct = true;
                                break;
                            }
                        }
                    }
                    if (!correct ? !pv.getAchat().getStatut().equals(Constantes.ETAT_VALIDE) : false) {
                        getErrorMessage("La facture doit etre au préalable validée");
                        return false;
                    }
                }
                pv.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                pv.setAuthor(currentUser);
                pv.setDateUpdate(new Date());
                dao.update(pv);
                if (pv.getAchat() != null ? pv.getAchat().getId() > 0 : false) {
                    Map<String, String> statuts = dao.getEquilibreAchat(pv.getAchat().getId());
                    if (statuts != null) {
                        pv.getAchat().setStatutLivre(statuts.get("statut_livre"));
                        pv.getAchat().setStatutRegle(statuts.get("statut_regle"));
                    }
                    ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (w != null) {
                        w.resetView(pv.getAchat());
                        int idx = w.getDocAchat().getReglements().indexOf(pv);
                        if (idx > -1) {
                            w.getDocAchat().getReglements().set(idx, pv);
                            update("data_mensualite_facture_achat");
                        }
                    }
                }
                int index = piecesCaisses.indexOf(pv);
                if (index > -1) {
                    piecesCaisses.set(index, pv);
                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    w.comptabiliserCaisseAchat(pv, false, false);
                    update("head_corp_form_suivi_pra");
                }
                pieceAvance.setStatutPiece(pv.getStatutPiece());
                update("head_form_suivi_pra");
                if (succes) {
                    succes();
                }
                return true;
            } else {
                getErrorMessage("Aucune banque n'a été trouvé !");
            }
        }
        return false;
    }

    public void valideAcompte() {
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            if ((selectAcompte.getCaisse() != null ? selectAcompte.getCaisse().getId() > 0 : false) && selectAcompte.getDatePaiement() != null) {
                w.encaisserAcompte(selectAcompte);
            } else if (selectAcompte.getDatePaiement() == null) {
                getErrorMessage("Veuillez entrer une date de validation !");
            } else {
                getErrorMessage("Veuillez choisir une caisse ou banque !");
            }
        }
        update("head_form_suivi_paa");
        update("head_corp_form_suivi_paa");
    }

    public void valideCredit() {
        if (selectCredit != null ? selectCredit.getId() > 0 : false) {
            if ((selectCredit.getCaisse() != null) ? selectCredit.getCaisse().getId() > 0 : false) {
                selectCredit.setStatut(Constantes.STATUT_DOC_PAYER);
                selectCredit.setAuthor(currentUser);
                selectCredit.setDateUpdate(new Date());
                dao.update(selectCredit);
                update("header_form_suivi_pca");
                succes();
            } else {
                getErrorMessage("Aucune banque n'a été trouvé !");
            }
        }
    }

    public void openConfirmDelePC(YvsComptaCaissePieceAchat pc) {
        pieceAchat = pc;
        if (pc.getModel() == null) {
            pc.setModel(new YvsBaseModeReglement(0l));
        }
        if (pc.getCaisse() == null) {
            pc.setCaisse(new YvsBaseCaisse((long) 0));
        }
        openDialog("dlgConfirmDeletePC");
    }

    public void dissocierNotif(YvsComptaCaissePieceAchat pc) {
        if (pc != null ? pc.getId() > 0 : false) {
            if (pc.getNotifs() != null ? pc.getNotifs().getId() > 0 : false) {
                if (!pc.getComptabilise()) {
                    dao.delete(pc.getNotifs());
                    pc.setNotifs(null);
                } else {
                    getErrorMessage("La pièce de règlement est déjà comptabilisé !");
                }
            }
        }
    }

    public void deleteAllPiece() {
        List<YvsComptaCaissePieceAchat> l = new ArrayList<>(selectedDoc.getReglements());
        for (YvsComptaCaissePieceAchat cp : l) {
            if ((cp.getId() != null)) {
                pieceAchat = cp;
                deletePieceCaisse(true);
            }
        }
        succes();
    }
    private String source;

    public void cancelStatutPayeOfPCVByCompensation() {
        ManagedCompensation w = (ManagedCompensation) giveManagedBean(ManagedCompensation.class);
        if (w != null) {
            YvsComptaCaissePieceAchat y = w.payerOrAnnuler(pieceAchat, true);
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                if (service != null) {
                    int idx = service.getDocAchat().getReglements().indexOf(y);
                    if (idx >= 0) {
                        service.getDocAchat().getReglements().set(idx, y);
                        update("tabview_facture_achat:data_mensualite_facture_achat");
                    }
                    Map<String, String> statuts = dao.getEquilibreAchat(y.getAchat().getId());
                    if (statuts != null) {
                        y.getAchat().setStatutLivre(statuts.get("statut_livre"));
                        y.getAchat().setStatutRegle(statuts.get("statut_regle"));
                    }
                    service.resetView(y.getAchat());
                }
            }
        }
    }

    public YvsComptaCaissePieceAchat openConfirmPaiement() {
        this.pc = pieceAchat;
        this.source = "F";
        this.emission = true;
        if (pc.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_ACHAT)) {
            openDialog("dlgConfirmAnnulePiece");
            return pc;
        }
        return openConfirmPaiement(pieceAchat, "F", true);
    }

    public YvsComptaCaissePieceAchat openConfirmPaiement(YvsComptaCaissePieceAchat pc, String source) {
        return openConfirmPaiement(pc, source, true, true);
    }

    public YvsComptaCaissePieceAchat openConfirmPaiement(YvsComptaCaissePieceAchat pc, String source, boolean emission) {
        return openConfirmPaiement(pc, source, true, true, emission);
    }

    public YvsComptaCaissePieceAchat openConfirmPaiement(YvsComptaCaissePieceAchat pc, String source, boolean open, boolean msg) {
        return openConfirmPaiement(pc, source, open, msg, true);
    }

    YvsComptaCaissePieceAchat pc;
    boolean emission;

    public YvsComptaCaissePieceAchat openConfirmrPaiement() {
        return openConfirmPaiement(pc, source, emission);
    }

    public YvsComptaCaissePieceAchat openConfirmrPaiement(YvsComptaCaissePieceAchat pc, String source, boolean emission) {
        this.pc = pc;
        this.source = source;
        this.emission = emission;
        if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_ACHAT)) {
            openDialog("dlgConfirmAnnulePiece");
            return pc;
        }
        return openConfirmPaiement(pc, source, true, true, emission);
    }

    public YvsComptaCaissePieceAchat openConfirmPaiement(YvsComptaCaissePieceAchat pc, String source, boolean open, boolean msg, boolean emission) {
        this.source = source;
        if (currentParam == null) {
            currentParam = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        }
        pc = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{pc.getId()});
        if (pc != null ? pc.getId() > 0 : false) {
            String modeNotif = Constantes.MODE_PAIEMENT_ESPECE;
            if (pc.getNotifs() != null ? pc.getNotifs().getId() > 0 : false) {
                modeNotif = pc.getNotifs().getAcompte().getModel().getTypeReglement();
                if (!autoriser("p_caiss_payer_acompte")) {
                    openNotAcces();
                    return pc;
                }
                //la pièce n'est pas encore payé
                if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                    if (!pc.getNotifs().getAcompte().getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                        getErrorMessage("L'acompte lié à ce reglement n'est pas encore encaissé");
                        return pc;
                    }
                    double montant = pc.getMontant();
                    //lors d'un paiement par acompte
                    for (YvsComptaNotifReglementAchat n : pc.getNotifs().getAcompte().getNotifs()) {
                        if (n.getPieceAchat().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                            montant += n.getPieceAchat().getMontant();
                        }
                    }
                    if (montant > pc.getNotifs().getAcompte().getMontant()) {
                        getErrorMessage("Vous ne pouvez pas valider ce montant.. car la somme des pièces excedera le montant de l'acompte");
                        return pc;
                    }
                }
            }
            if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER ? (currentParam != null ? !currentParam.getPaieWithoutValide() : true) : false) {
                boolean correct = pc.getAchat().getStatut().equals(Constantes.ETAT_VALIDE);
                if (!correct) {
                    if (pc.getAchat().getEtapesValidations() != null ? !pc.getAchat().getEtapesValidations().isEmpty() : false) {
                        for (YvsWorkflowValidFactureAchat w : pc.getAchat().getEtapesValidations()) {
                            if (w.getEtapeValid() ? w.getEtape().getReglementHere() : false) {
                                correct = true;
                                break;
                            }
                        }
                    }
                }
                if (!correct) {
                    getErrorMessage("La facture doit etre au préalable validé");
                    return pc;
                }
            }

            selectedPiece = pc;
            if (pc.getModel() != null ? pc.getModel().getId() > 0 : false) {
                if (pc.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_SALAIRE)) {
                    //si on veux valider le paiement
                    getErrorMessage("Ce mode paiement n'est pas autorisé!");
                } else if (pc.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    //si on veux valider le paiement
                    if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                        if (!autoriser("encais_piece_cheque")) {
                            openNotAcces();
                            return pc;
                        }
                        // Si le paiement se fait à partir d'une notification, il faut que les montants soient coherent
                        if (pc.getNotifs() != null) {
                            if (pc.getMontant() > pc.getNotifs().getAcompte().getReste()) {
                                getErrorMessage("Le montant restant de l'acompte " + pc.getNotifs().getAcompte().getNumRefrence() + " Ne permet pas de régler la pièce de caisse !");
                                return pc;
                            }
                        }
                        if (pc.getPhasesReglement().isEmpty()) {
                            if (!modeNotif.equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                                List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{pc.getModel(), emission});
                                //lié les phases à la pièce de règlements
                                YvsComptaPhasePieceAchat pp;
                                if (pc.getPhasesReglement() == null) {
                                    pc.setPhasesReglement(new ArrayList<YvsComptaPhasePieceAchat>());
                                }
                                for (YvsComptaPhaseReglement ph : phases) {
                                    pp = new YvsComptaPhasePieceAchat(null);
                                    pp.setAuthor(currentUser);
                                    pp.setPhaseOk(false);
                                    pp.setPhaseReg(ph);
                                    pp.setPieceAchat(pc);
                                    pp.setCaisse(pc.getCaisse());
                                    pp.setDateSave(new Date());
                                    pp.setDateUpdate(new Date());
                                    pp = (YvsComptaPhasePieceAchat) dao.save1(pp);
                                    pc.getPhasesReglement().add(pp);
                                }
                            }
                            ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                            if (service != null) {
                                int idx = service.getDocAchat().getReglements().indexOf(pc);
                                if (idx >= 0) {
                                    service.getDocAchat().getReglements().set(idx, pc);
                                    update("tabview_facture_vente:data_mensualite_facture_vente");
                                }
                                pc.getAchat().setStatutRegle(Constantes.ETAT_ENCOURS);
                                pc.getAchat().setDateUpdate(new Date());
                                pc.getAchat().setAuthor(currentUser);
                                dao.update(pc.getAchat());
                                idx = service.getDocuments().indexOf(pc.getAchat());
                                if (idx >= 0) {
                                    service.getDocuments().set(idx, pc.getAchat());
                                    update("data_facture_vente");
                                }
                                if (service.getDocAchat().getId() > 0) {
                                    service.getDocAchat().setStatutRegle(Constantes.ETAT_ENCOURS);
                                }
                            }
                            boolean succes = true;
                            if (pc.getNotifs() != null) {
                                if (pc.getMontant() <= pc.getNotifs().getAcompte().getReste()) {
                                    pieceAchat = pc;
                                    reglerPieceTresorerie(true);
                                    succes = false;
                                }
                            }
                            if (succes) {
                                succes();
                            }
                        } else if (msg) {
                            getWarningMessage("Les phases de ce règlement ont déjà été générées !");//                            
                        }
                    } else if (open) {
                        // annulser le paiement de la pièce
                        if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_ACHAT)) {
                            if (!autoriser("compta_od_annul_comptabilite")) {
                                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                                return pc;
                            }
                        }
                        openDialog("dlgCancelPcv1");
                    }
                } else if (pc.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_COMPENSATION)) {
                    if (!autoriser("encais_piece_comp")) {
                        openNotAcces();
                        return pc;
                    }
                    ManagedCompensation w = (ManagedCompensation) giveManagedBean(ManagedCompensation.class);
                    if (w != null) {
                        if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                            YvsComptaCaissePieceAchat y = w.payerOrAnnuler(pc, msg);
                            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                                ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                                if (service != null) {
                                    int idx = service.getDocAchat().getReglements().indexOf(y);
                                    if (idx >= 0) {
                                        service.getDocAchat().getReglements().set(idx, y);
                                        update("tabview_facture_achat:data_mensualite_facture_achat");
                                    }
                                    Map<String, String> statuts = dao.getEquilibreAchat(y.getAchat().getId());
                                    if (statuts != null) {
                                        y.getAchat().setStatutLivre(statuts.get("statut_livre"));
                                        y.getAchat().setStatutRegle(statuts.get("statut_regle"));
                                    }
                                    service.resetView(y.getAchat());
                                }
                                return y;
                            }
                        } else if (open) {
                            //on veux annuler le paiement
                            openDialog("dlgCancelPcv3");
                        }
                    }
                } else if (open) {
                    if (!autoriser("encais_piece_espece")) {
                        openNotAcces();
                        return pc;
                    }
                    if (pc.getModel() == null) {
                        pc.setModel(modeEspece());
                    }
                    if (pc.getCaisse() == null) {
                        pc.setCaisse(new YvsBaseCaisse((long) 0));
                    }
                    if (!needConfirmation) {
                        openDialog("dlgConfirmPaye");
                    } else {
                        pieceAchat = pc;
                        reglerPieceTresorerie(true);
                    }
                }
            } else if (open) {
                if (!autoriser("encais_piece_espece")) {
                    openNotAcces();
                    return pc;
                }
                if (pc.getModel() == null) {
                    pc.setModel(modeEspece());
                }
                if (pc.getCaisse() == null) {
                    pc.setCaisse(new YvsBaseCaisse((long) 0));
                }
                if (!needConfirmation) {
                    openDialog("dlgConfirmPaye");
                } else {
                    pieceAchat = pc;
                    reglerPieceTresorerie(true);
                }
            }
        }
        update("data_mensualite_facture_achat");
        if (!needConfirmation) {
            pieceAchat = pc;
        }
        return pc;
    }

    public void cancelStatutPayeOfPCVForBanque() {
        try {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (dao.isComptabilise(pieceAchat.getId(), Constantes.SCR_CAISSE_ACHAT)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return;
                }
                if (w != null ? (pieceAchat.getPhasesReglement() != null ? pieceAchat.getPhasesReglement().isEmpty() : true) : false) {
                    if (w.unComptabiliserCaisseAchat(pieceAchat, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                }
            }
            if (w != null) {
                for (YvsComptaPhasePieceAchat pp : pieceAchat.getPhasesReglement()) {
                    if (w.unComptabiliserPhaseCaisseAchat(pp, false, true)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                    dao.delete(pp);
                }
            }
            pieceAchat.getPhasesReglement().clear();
            pieceAchat.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            pieceAchat.setAuthor(currentUser);
            pieceAchat.setDatePaiement(null);
            pieceAchat.setCaissier(null);
            dao.update(pieceAchat);
            ManagedFactureAchat ws = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (ws != null) {
                int idx = ws.getDocAchat().getReglements().indexOf(pieceAchat);
                if (idx >= 0) {
                    ws.getDocAchat().getReglements().set(idx, pieceAchat);
                    update("tabview_facture_achat:data_mensualite_facture_achat");
                }
                pieceAchat.getAchat().setStatutRegle(Constantes.ETAT_ATTENTE);
                pieceAchat.getAchat().setDateUpdate(new Date());
                pieceAchat.getAchat().setAuthor(currentUser);
                dao.update(pieceAchat.getAchat());
                ws.resetView(pieceAchat.getAchat());
            }
            int idx = piecesCaisses.indexOf(pieceAchat);
            if (idx >= 0) {
                piecesCaisses.set(idx, pieceAchat);
                update("table_regFA");
            }
        } catch (Exception ex) {
            getErrorMessage("Impossible de terminer cette opération !");
        }
    }

    public void cancelStatutPayeOfPCVByCompensation1() {
        ManagedCompensation w = (ManagedCompensation) giveManagedBean(ManagedCompensation.class);
        if (w != null) {
            YvsComptaCaissePieceAchat y = w.payerOrAnnuler(selectedPiece, true);
            if (y != null ? (y.getId() != null ? y.getId() > 0 : false) : false) {
                int idx = piecesCaisses.indexOf(y);
                if (idx >= 0) {
                    piecesCaisses.set(idx, y);
                    update("tabview_facture_achat:data_mensualite_facture_achat");
                    update("table_regFA");
                }
                Map<String, String> statuts = dao.getEquilibreAchat(y.getAchat().getId());
                if (statuts != null) {
                    y.getAchat().setStatutLivre(statuts.get("statut_livre"));
                    y.getAchat().setStatutRegle(statuts.get("statut_regle"));
                }
            }
        }
    }

    public void openConfirmPaiement(YvsComptaCaissePieceAchat pc) {
        if (pc.getModel() == null) {
            pc.setModel(modeEspece());
        }
        if (pc.getCaisse() == null) {
            pc.setCaisse(new YvsBaseCaisse((long) 0));
        }
        pieceAchat = pc;
        openDialog("dlgConfirmPaye");
    }

    public void toogleSuspendPieceCaisse(YvsComptaCaissePieceAchat pc) {
        if (pc != null) {
            //si c'est une suspension, on controle juste le droit
            if (pc.getId() > 0) {
                pc.setDateUpdate(new Date());
                pc.setAuthor(currentUser);
                if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) { //la pièce ne doit pas être déjà payé
                    switch (pc.getStatutPiece()) {
                        case Constantes.STATUT_DOC_ANNULE:
                        case Constantes.STATUT_DOC_SUSPENDU: //cas d'une remise en état d'attente (on controle la cohérence des montant)                        
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

    private int giveAction(YvsComptaCaissePieceAchat pc) {
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

    public void reglerPieceTresorerie(boolean avance) {
        reglerPieceTresorerie(avance, true);
    }

    public void reglerPieceTresorerie(boolean avance, boolean msg) {
        pieceAchat.setDatePaiement(pieceAchat.getDatePaimentPrevu());
        reglerPieceTresorerie(pieceAchat, source, msg);
    }

    public void reglerPieceTresorerie(YvsComptaCaissePieceAchat pc, String source, boolean msg) {
        reglerPieceTresorerie(selectedDoc, pc, source, msg);
    }

    public void reglerPieceTresorerie(DocAchat achat, YvsComptaCaissePieceAchat pc, String source, boolean msg) {
        if (pc != null ? pc.getId() > 0 : false) {//si c'est une suspension, on controle juste le droit
            pc.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{pc.getCaisse().getId()}));
            if (controleAccesCaisse(pc.getCaisse(), true)) {
                if (pc.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU) { //la pièce ne doit pas être déjà payé
                    //la pièce de caisse doit avoir une caisse
                    int action = giveAction(pc);
                    pc.setAuthor(currentUser);
                    pc.setNew_(true);
                    boolean update = false;
                    //Vérifie s'il s'agit d'une compensation de la cohérence des montants
                    if (pc.getNotifs() != null) {
                        if (pc.getNotifs().getAcompte().getReste() < pc.getMontant()) {
                            if (msg) {
                                getErrorMessage("Le montant de l'acompte ne permet pas le règlement totale de cette pièce !");
                                return;
                            }
                        }
                    }
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    switch (action) {
                        case 1://valider la pièce de vente
                            pc.setStatutPiece(Constantes.STATUT_DOC_VALIDE);
                            update = true;
                            break;
                        case 3://Mettre la pièce en attente
                            if (!verifyCancelPieceCaisse(pc.getDatePaiement())) {
                                return;
                            }
                            if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_ACHAT)) {
                                if (!autoriser("compta_od_annul_comptabilite")) {
                                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                                    return;
                                }
                                if (w != null) {
                                    if (!w.unComptabiliserCaisseAchat(pc, false)) {
                                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                                        return;
                                    }
                                }
                            }
                            pc.setDatePaiement(null);
                            pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            update = true;
                            break;
                        case 2://Encaisser la pièce
                            if (pc.getCaisse() != null ? (pc.getCaisse().getId() > 0 && pc.getAchat() != null) : false) {
                                YvsComDocAchats d = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{pc.getAchat().getId()});
                                if ("F".equals(source)) {
                                    setMontantTotalDoc(d);
                                    if ((achat != null && pc.getAchat() != null) ? achat.getId() != pc.getAchat().getId() : true) {
                                        achat = UtilCom.buildBeanDocAchat(d);
                                    }
                                }
                                boolean valider = (currentParam != null ? !currentParam.getPaieWithoutValide() : true);
                                if (controleAddPiece(d, pc, valider, false)) {
                                    if (pc.getAchat().canDelete() && msg) {
                                        getErrorMessage("La facture doit être validée");
                                        return;
                                    }
                                    Boolean canNegative = (Boolean) dao.loadObjectByNameQueries("YvsBaseCaisse.findCanNegative", new String[]{"id"}, new Object[]{pc.getCaisse().getId()});
                                    if (canNegative != null ? !canNegative : false) {
                                        double solde = dao.getTotalCaisse(pc.getCaisse().getId(), 0, null, null, Constantes.STATUT_DOC_PAYER, pc.getDatePiece());
                                        if ((solde - pc.getMontant()) < 0) {
                                            if (msg) {
                                                getErrorMessage("Cette caisse ne peut payer cette piéce car le solde de la caisse est inferieur à la pièce");
                                            }
                                            return;
                                        }
                                    }
                                    pc.setCaissier(currentUser.getUsers());
                                    pc.setNew_(true);
                                    pc.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                                    if (pc.getDatePaiement() == null) {
                                        pc.setDatePaiement(pc.getDatePaimentPrevu());
                                    }
                                    update = true;
                                } else {
                                    update = false;
                                }
                            } else if (msg) {
                                if (pc.getAchat() == null) {
                                    getErrorMessage("Cette pièce n'est rattachée à aucune facture");
                                } else {
                                    getErrorMessage("Aucune caisse n'a été trouvé pour ce règlement !");
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    if (update) {
                        if (pieceAvance != null ? pc.getId().equals(pieceAvance.getId()) : false) {
                            pieceAvance.setStatutPiece(pc.getStatutPiece());
                        }
                        pc.setDateUpdate(new Date());
                        pc.setAuthor(currentUser);
                        dao.update(pc);
                        if (pc.getAchat() != null ? pc.getAchat().getId() > 0 : false) {
                            if (pc.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                if (w != null ? dao.isComptabilise(pc.getAchat().getId(), Constantes.SCR_ACHAT) : false) {
                                    w.comptabiliserCaisseAchat(pc, false, false);
                                }
                            }
                            Map<String, String> statuts = dao.getEquilibreAchat(pc.getAchat().getId());
                            if (statuts != null) {
                                pc.getAchat().setStatutLivre(statuts.get("statut_livre"));
                                pc.getAchat().setStatutRegle(statuts.get("statut_regle"));
                            }
                            ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                            if (service != null) {
                                service.getDocAchat().setStatutRegle(pc.getAchat().getStatutRegle());
                                update("grp_btn_etat_facture_achat");
                                int idx = service.getDocuments().indexOf(pc.getAchat());
                                if (idx >= 0) {
                                    service.getDocuments().set(idx, pc.getAchat());
                                    update("data_facture_achat");
                                    update("data_facture_achat_reg");
                                }
                                idx = service.getDocAchat().getReglements().indexOf(pc);
                                if (idx >= 0) {
                                    service.getDocAchat().getReglements().set(idx, pc);
                                    service.setMontantTotalDoc(service.getDocAchat());
                                    update("chp_fa_net_a_payer");
                                    update("tabview_facture_achat:data_mensualite_facture_achat");
                                }
                                service.setMontantTotalDoc(pc.getAchat());
                                update("zone_form_regFA");
                            }
                            //En cas de bon rattaché
                            if (pc.getJustify() != null) {
                                if (pc.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                                    ManagedBonProvisoire serviceBp = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                                    serviceBp.justifyBon(pc.getJustify().getBon(), true);
                                } else {
                                    //Force l'exécution du trigger !
                                    dao.update(pc.getJustify().getBon());
                                }
                            }
                        }
                        if (achat != null ? achat.getReglements() != null : false) {
                            int idx = achat.getReglements().indexOf(pc);
                            if (idx >= 0) {
                                achat.getReglements().set(idx, pc);
                                update("table_reg_facture_FA");
                            }
                        }
                        if (piecesCaisses != null) {
                            int idx = piecesCaisses.indexOf(pc);
                            if (idx > -1) {
                                piecesCaisses.set(idx, pc);
                                update("table_regFA");
                            }
                        }
                        if (pc.getNotifs() != null ? pc.getNotifs().getAcompte() != null : false) {
                            ManagedOperationFournisseur wa = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
                            if (wa != null) {
                                wa.equilibre(pc.getNotifs().getAcompte());
                                update("table_cmpens_p_vente");
                            }
                        }
                        if (msg) {
                            succes();
                        }
                    }
                }
            }
        } else if (msg) {
            getErrorMessage("La pièce n'a pas été sauvegardé !");
        }
    }

    public void ecouteSaisieFournisseur() {
        String num = pieceAvance.getDocAchat().getFournisseur().getCodeFsseur();
        pieceAvance.getDocAchat().getFournisseur().setId(0);
        pieceAvance.getDocAchat().getFournisseur().setError(true);
        pieceAvance.getDocAchat().getFournisseur().setTiers(new Tiers());
        ManagedFournisseur m = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
        if (m != null) {
            Fournisseur y = m.searchFsseur(num, true);
            if (m.getFournisseurs() != null ? !m.getFournisseurs().isEmpty() : false) {
                if (m.getFournisseurs().size() > 1) {
                    update("data_fournisseur_acomptes");
                } else {
                    chooseFournisseur(y);
                }
                pieceAvance.getDocAchat().getFournisseur().setError(false);
            }
        }
    }

    public void chooseFournisseur(Fournisseur d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(pieceAvance.getDocAchat().getFournisseur(), d);
        }
    }

    public void chooseOneCLient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsBaseFournisseur y = (YvsBaseFournisseur) ev.getObject();
            chooseFournisseur(UtilCom.buildBeanFournisseur(y));
        }
    }

    public boolean controlePiece() {
        //controle la conformité du formulaire
        if (pieceAvance.getDocAchat().getFournisseur().getId() <= 0) {
            getErrorMessage("Vous devez entrer un fournisseurs !");
            return false;
        }
        if (pieceAvance.getMode() != null ? pieceAvance.getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le mode de paiement !");
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

    public void saveNewPc() {
        //controle la conformité du formulaire
        if (controlePiece()) {
            YvsComptaCaissePieceAchat pc = new YvsComptaCaissePieceAchat();
            pc.setAuthor(currentUser);
            if (pieceAvance.getCaisse().getId() > 0) {
                ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                if (service != null) {
                    int idx = service.getCaisses().indexOf(new YvsBaseCaisse(pieceAvance.getCaisse().getId()));
                    if (idx >= 0) {
                        pc.setCaisse(service.getCaisses().get(idx));
                    }
                }
            }
            if (pieceAvance.getMode().getId() > 0) {
                ManagedModeReglement service = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                if (service != null) {
                    int idx = service.getModes().indexOf(new YvsBaseModeReglement((long) pieceAvance.getMode().getId()));
                    if (idx >= 0) {
                        pc.setModel(service.getModes().get(idx));
                    }
                }
            }
            pc.setFournisseur(new YvsBaseFournisseur(pieceAvance.getDocAchat().getFournisseur().getId(), pieceAvance.getDocAchat().getFournisseur().getCodeFsseur(), pieceAvance.getDocAchat().getFournisseur().getNom(), pieceAvance.getDocAchat().getFournisseur().getPrenom()));
            pc.setDatePaiement(null);
            pc.setDatePaimentPrevu(pieceAvance.getDatePaiementPrevu());
            pc.setDatePiece(new Date());
            pc.setMontant(pieceAvance.getMontant());
            pc.setNew_(true);
            pc.setNumeroPiece(pieceAvance.getNumPiece());
            pc.setNote(pieceAvance.getDescription());
            pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            if (pieceAvance.getId() <= 0) {
                pc.setId(null);
                pc = (YvsComptaCaissePieceAchat) dao.save1(pc);
                pc.setNew_(true);
                pieceAvance.setId(pc.getId());
                piecesCaisses.add(0, pc);
            } else {
                pc.setId(pieceAvance.getId());
                dao.update(pc);
                if (piecesCaisses.contains(pc)) {
                    piecesCaisses.set(piecesCaisses.indexOf(pc), pc);
                }
            }
            succes();
        }
    }

    public void loadOnViewPiece(SelectEvent ev) {
        if (ev != null) {
            if (selectedDoc != null) {
                YvsComptaCaissePieceAchat pc = (YvsComptaCaissePieceAchat) ev.getObject();
                loadPcOnViewPiece(pc);
            }
        }
    }

    public void openDlgToBindDv(YvsComptaAcompteFournisseur pc) {
        if (pc != null) {
            if (selectedDoc != null) {
                selectAcompte = pc;
                update("form_confirm_bind_pcFA");
                openDialog("dlgConfirmBind");
            } else {
                getErrorMessage("Aucune facture n'a été selectionné !");
            }
        }
    }

    public void loadPcOnViewPiece(YvsComptaCaissePieceAchat ev) {
        if (ev != null) {
            pieceAvance = UtilCompta.buildBeanTresoreri(ev);
        }
    }

    public void resetViewPc() {
        pieceAvance = new PieceTresorerie();
    }

    public void openTodeleteOneLinePc(YvsComptaCaissePieceAchat p) {
        loadPcOnViewPiece(p);
        openDialog("dlgConfirmDeletePC1");
    }

    public void deleteOneLinePc() {
        if (pieceAvance.getId() > 0) {
            if (pieceAvance.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                try {
                    YvsComptaCaissePieceAchat pc = new YvsComptaCaissePieceAchat(pieceAvance.getId());
                    pc.setAuthor(currentUser);
                    dao.delete(pc);
                    piecesCaisses.remove(pc);
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

    public void bindPieceAndFacture() {
        //controle        
        if (!selectAcompte.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
            getErrorMessage("La pièce n'a pas encore été encaissé !");
            return;
        }
        if (selectedDoc != null ? selectedDoc.getId() < 1 : true) {
            getErrorMessage("Aucune facture n'a été selectionné !");
            return;
        }
        if (pieceAchat != null ? pieceAchat.getId() < 1 : true) {
            getErrorMessage("Aucune reglement n'a été selectionné !");
            return;
        }
        //controle l'identité du client'
        if (selectedDoc.getFournisseur().getId() != selectAcompte.getFournisseur().getId()) {
            getErrorMessage("...Il s'agit de la facture d'un autre Fournisseur !");
            return;
        }
        if (selectAcompte.getResteUnBind() < pieceAchat.getMontant()) {
            getErrorMessage("Cet acompte ne peut pas couvrir ce reglement");
            return;
        }
        if (selectAcompte.getReste() < pieceAchat.getMontant()) {
            getErrorMessage("Le reste sur cet acompte ne peut pas couvrir ce reglement");
            return;
        }
        ManagedOperationFournisseur service = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (service != null) {
            YvsComptaNotifReglementAchat y = service.confirmBind(selectAcompte, pieceAchat);
            if (y != null ? y.getId() > 0 : false) {
                pieceAchat.setNotifs(y);
                int idx = selectedDoc.getReglements().indexOf(pieceAchat);
                if (idx > -1) {
                    selectedDoc.getReglements().set(idx, pieceAchat);
                }
                update("table_regFA");
            }
        }
    }

    public void openDlgToUnbindPiece(YvsComptaCaissePieceAchat pv) {
        pieceAchat = pv;
        openDialog("dlgConfirmUnBind");
        update("form_confirm_unbind_pc");
    }

    public void _onSelectDistantFacture(YvsComDocAchats da) {
        if (da != null ? da.getId() > 0 : false) {
            ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            if (service != null) {
                service.onSelectDistant(da);
            }
        }
    }

    public void _onSelectDistantAcompte(YvsComptaAcompteFournisseur da) {
        if (da != null ? da.getId() > 0 : false) {
            ManagedOperationFournisseur service = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (service != null) {
                service.onSelectDistant(da);
            }
        }
    }

    public void _onSelectDistantCredit(YvsComptaReglementCreditFournisseur da) {
        if (da != null ? da.getId() > 0 : false) {
            ManagedOperationFournisseur service = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
            if (service != null) {
                service.onSelectDistant(da.getCredit());
            }
        }
    }

    public void onSelectDistantVente(YvsComptaCaissePieceVente y) {
        ManagedReglementVente s = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
        if (s != null) {
            s.onSelectDistant(y);
        }
    }

    public void openTogeneratedPhaseReglement(YvsComptaCaissePieceAchat pc) {
        String modeNotif = Constantes.MODE_PAIEMENT_ESPECE;
        if (pc.getNotifs() != null ? pc.getNotifs().getId() > 0 : false) {
            modeNotif = pc.getNotifs().getAcompte().getModel().getTypeReglement();
        }
        if (modeNotif.equals(Constantes.MODE_PAIEMENT_BANQUE)) {
            getInfoMessage("Cette pièce ne peut pas etre rattachée à des phases de reglement");
            return;
        }
        this.pieceAchat = pc;
        openDialog("dlgInitPhase");
    }

    public void openTogeneratedPhaseAcompte(YvsComptaAcompteFournisseur pc) {
        this.selectAcompte = pc;
        openDialog("dlgInitPhase");
    }

    public void openTogeneratedPhaseCredit(YvsComptaReglementCreditFournisseur pc) {
        this.selectCredit = pc;
        openDialog("dlgInitPhase");
    }

    public void generatedPhaseReglement() {
        if (pieceAchat == null) {
            return;
        }
        if (pieceAchat.getAchat() == null) {
            return;
        }
        if (pieceAchat != null && !pieceAchat.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && pieceAchat.getAchat().getStatut().equals(Constantes.ETAT_VALIDE)) {
            if (pieceAchat.getPhasesReglement() != null) {
                try {
                    if (pieceAchat.getCaisse() != null ? pieceAchat.getCaisse().getId() < 1 : true) {
                        getErrorMessage("Vous devez precisez la caisse qui a été mouvementé");
                        return;
                    }
                    for (YvsComptaPhasePieceAchat ph : pieceAchat.getPhasesReglement()) {
                        ph.setAuthor(currentUser);
                        dao.delete(ph);
                    }
                    pieceAchat.getPhasesReglement().clear();
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByModeEmission", new String[]{"mode", "emission"}, new Object[]{pieceAchat.getModel(), true});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhasePieceAchat pp;
                    if (pieceAchat.getPhasesReglement() == null) {
                        pieceAchat.setPhasesReglement(new ArrayList<YvsComptaPhasePieceAchat>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhasePieceAchat(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        if (pieceAchat.getCaisse() != null ? pieceAchat.getCaisse().getId() > 0 : false) {
                            pp.setCaisse(pieceAchat.getCaisse());
                        }
                        pp.setPieceAchat(pieceAchat);
                        pp = (YvsComptaPhasePieceAchat) dao.save1(pp);
                        pieceAchat.getPhasesReglement().add(pp);
                    }
                    update("table_list_piece_cheque_achat");
                    succes();
                } catch (Exception ex) {
                    getErrorMessage("Impossible de réaliser cette action !");
                }
            }
        } else {
            if (pieceAchat.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce est déjà payé !");
            }
            if (!pieceAchat.getAchat().getStatut().equals(Constantes.ETAT_VALIDE)) {
                getErrorMessage("Le document d'achat n'a pas été validé!");
            }
            if (pieceAchat == null) {
                getErrorMessage("Aucune pièce de règlement n'a été selectionné !");
            }
        }
    }

    public void generatedPhaseAcompte() {
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            w.generatedPhaseAcompte(selectAcompte);
        }
    }

    public void generatedPhaseCredit() {
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            w.generatedPhaseCredit(selectCredit);
        }
    }

    public void unbindLiasonCaisse() {
        if (pieceAchat != null) {
            if (pieceAchat.getAchat() != null) {
                pieceAchat.setAchat(null);
                pieceAchat.setAuthor(currentUser);
                selectedDoc.getReglements().remove(pieceAchat);
                dao.update(pieceAchat);
                update("table_regFA");
                ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                if (service != null) {
                    service.setMontantTotalDoc(selectedDoc);
                    Map<String, String> statuts = dao.getEquilibreAchat(selectedDoc.getId());
                    if (statuts != null) {
                        selectedDoc.setStatutLivre(statuts.get("statut_livre"));
                        selectedDoc.setStatutRegle(statuts.get("statut_regle"));
                    }
                    YvsComDocAchats d = (YvsComDocAchats) dao.loadOneByNameQueries("YvsComDocAchats.findById", new String[]{"id"}, new Object[]{selectedDoc.getId()});
                    if (d != null) {
                        int idx = service.getDocuments().indexOf(d);
                        if (idx >= 0) {
                            service.getDocuments().set(idx, d);
                            update("data_facture_achat_reg");
                        }
                    }
                    update("zone_form_regFA");
                }
            }
        }
    }

    public void duplicatePiece(YvsComptaCaissePieceAchat pv, double montant) {
        YvsComptaCaissePieceAchat newEn = new YvsComptaCaissePieceAchat(pv);
        newEn.setId(null);
        newEn.setMontant(montant);
        newEn.setAchat(null);
        newEn.setNumeroPiece(newEn.getNumeroPiece() + "°");
        if (newEn.getModel() != null ? newEn.getModel().getId() < 1 : true) {
            newEn.setModel(modeEspece());
        }
        newEn = (YvsComptaCaissePieceAchat) dao.save1(newEn);
        newEn.setNew_(true);
        piecesCaisses.add(0, newEn);
    }

    public void openConfirmEncaiss(YvsComptaCaissePieceAchat p) {
        pieceAchat = p;
        openDialog("dlgConfirmPaye");
    }

    public void cleanParametrePiece() {
        paginator.getParams().clear();
        dateSearch = false;
        dateDebutSearch = new Date();
        dateFinSearch = new Date();
        statut_ = null;
        modeFind = 0;
        caisseF = 0;
        montantF = 0;
        montantF1 = 0;
        fournisseurF = "";
        numPieceF = "";
        loadAllPieces(true, true);
    }

    public void loadAllPieces(boolean avancer, boolean init) {
        paginator.addParam(new ParametreRequete("y.author.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND"));
        piecesCaisses = paginator.executeDynamicQuery("YvsComptaCaissePieceAchat", "y.datePaimentPrevu DESC", avancer, init, (int) imax, dao);
    }

    public void paginerCheque(boolean next) {
        initForm = false;
        loadAllPieces(next, initForm);
    }

    public void gotoPagePaginator() {
        paginator.gotoPage((int) imax);
        initForm = true;
        loadAllPieces(true, initForm);
    }

    public void loadPieceByStatut(String statut) {
        paginator.getParams().clear();
        statut_ = statut;
        addParamStatut();
    }

    public void findOneFacture(String reference) {
        ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
        if (service != null) {
            if (reference != null ? reference.trim().length() > 0 : false) {
                service.setNumSearch_(reference);
                service.searchByNum();
                if (service.getDocuments().size() == 1) {
                    onSelectedFacture(service.getDocuments().get(0));
                } else if (service.getDocuments().size() > 1) {
                    openDialog("dlgFacture");
                    update("data_facture_achat_reg");
                } else {
                    pieceAvance.setDocAchat(new DocAchat());
                }
            } else {
                pieceAvance.setDocAchat(new DocAchat());
            }
        }
    }

    public void findByFournisseur(String reference) {
        if (reference != null ? reference.trim().length() > 0 : false) {
            ManagedFournisseur w = (ManagedFournisseur) giveManagedBean(ManagedFournisseur.class);
            pieceAvance.getFournisseur().setId(0);
            pieceAvance.getFournisseur().setError(true);
            pieceAvance.getFournisseur().setTiers(new Tiers());
            if (w != null) {
                Fournisseur y = w.searchFsseur(reference, true);
                if (y != null ? y.getId() > 0 : false) {
                    pieceAvance.setFournisseur(y);
                    pieceAvance.getFournisseur().setError(false);

                    ManagedFactureAchat service = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (service != null) {
                        service.setCodeFsseur_(reference);
                        service.addParamFsseur();
                        if (service.getDocuments() != null ? !service.getDocuments().isEmpty() : false) {
                            onSelectedFacture(service.getDocuments().get(0));
                        }
                        update("data_facture_achat_reg");
                    }
                }
            }
        }
    }

    private void ordonnePhase(List<YvsComptaPhasePieceAchat> l, YvsComptaPhasePieceAchat p) {
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

    private List<YvsComptaPhasePieceAchat> ordonnePhase(List<YvsComptaPhasePieceAchat> l) {
//        Collections.sort(l, new YvsComptaPhasePieceAchat());
        int idx = 0;
        while (idx < l.size()) {
            ordonnePhase(l, l.get(idx));
            idx++;
        }
        return l;
    }

    private List<YvsComptaPhaseAcompteAchat> ordonnePhases(List<YvsComptaPhaseAcompteAchat> l) {
        Collections.sort(l, new YvsComptaPhaseAcompteAchat());
        int idx = 0;
        while (idx < l.size()) {
            ordonnePhase(l, l.get(idx));
            idx++;
        }
        return l;
    }

    private void ordonnePhase(List<YvsComptaPhaseReglementCreditFournisseur> l, YvsComptaPhaseReglementCreditFournisseur p) {
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

    private List<YvsComptaPhaseReglementCreditFournisseur> ordonnePhasec(List<YvsComptaPhaseReglementCreditFournisseur> l) {
        Collections.sort(l, new YvsComptaPhaseReglementCreditFournisseur());
        int idx = 0;
        while (idx < l.size()) {
            ordonnePhase(l, l.get(idx));
            idx++;
        }
        return l;
    }

    private boolean controleValidation(YvsComptaPhasePieceAchat pp) {
        if ((pp.getPieceAchat().getCaisse() != null) ? pp.getPieceAchat().getCaisse().getId() <= 0 : true) {
            getErrorMessage("Aucune banque n'a été trouvé !");
            return false;
        }
        if ((pp.getPieceAchat().getAchat() != null) ? !pp.getPieceAchat().getAchat().getStatut().equals(Constantes.ETAT_VALIDE) : true) {
            getErrorMessage("Le document de d'achat n'a pas été validé");
            return false;
        }
        return true;
    }

    public void comptabiliserPhaseCaisseAchat(YvsComptaPhasePieceAchat pp, boolean comptabilise) {
        selectPhaseAchat = pp;
        if (pieceAvance.getPhasesAchat() != null ? !pieceAvance.getPhasesAchat().isEmpty() : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                int idx = pieceAvance.getPhasesAchat().indexOf(pp);
                if (idx > -1) {
                    if (idx == 0) {
                        if (comptabilise) {
                            w.comptabiliserPhaseCaisseAchat(pp, true, true);
                        } else {
                            openDialog("dlgComptabilisePhase");
                        }
                    } else {
                        YvsComptaPhasePieceAchat prec = pieceAvance.getPhasesAchat().get(idx - 1);
                        if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_CAISSE_ACHAT)) {
                            if (comptabilise) {
                                w.comptabiliserPhaseCaisseAchat(pp, true, true);
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

    public void printPhase(YvsComptaPhasePieceAchat y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getPieceAchat().getStatutPiece() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getPieceAchat().getMontant()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                executeReport("phase_pc_achat", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedReglementAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void validEtapesPieces(YvsComptaPhasePieceAchat pp) {
        if (!pp.getPhaseOk()) {
            if (!asDroitValidePhase(pp.getPhaseReg())) {
                openNotAcces();
                return;
            }
            if (currentParam == null) {
                currentParam = (YvsComParametreAchat) dao.loadOneByNameQueries("YvsComParametreAchat.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
            }
            if (!pp.getPieceAchat().getAchat().getNumDoc().equals("")) {
                if (!currentParam.getPaieWithoutValide() ? !pp.getPieceAchat().getAchat().getStatut().equals(Constantes.ETAT_VALIDE) : false) {
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
                pp.setPhaseOk(true);
                pp.setDateUpdate(new Date());
                pp.setStatut(Constantes.STATUT_DOC_VALIDE);
                pp.setAuthor(currentUser);
                dao.update(pp);
                pp.setEtapeActive(false);
                if (pp.getPieceAchat().getAchat() != null ? pp.getPieceAchat().getAchat().getId() > 0 : false) {
                    if (pp.getPhaseReg().getReglementOk()) {
                        if (!pp.getPieceAchat().getAchat().getStatut().equals(Constantes.ETAT_VALIDE)) {
                            getErrorMessage("La facture n'est pas encore validée !");
                            return;
                        }
                        pp.getPieceAchat().setStatutPiece(Constantes.STATUT_DOC_PAYER);
                        pp.getPieceAchat().setDatePaiement(pp.getDateValider());
                    } else {
                        if (pp.getPieceAchat().getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                            pp.getPieceAchat().setStatutPiece(Constantes.STATUT_DOC_ENCOUR);
                        }
                    }
                    dao.update(pp.getPieceAchat());
                    Map<String, String> statuts = dao.getEquilibreAchat(pp.getPieceAchat().getAchat().getId());
                    if (statuts != null) {
                        pp.getPieceAchat().getAchat().setStatutLivre(statuts.get("statut_livre"));
                        pp.getPieceAchat().getAchat().setStatutRegle(statuts.get("statut_regle"));
                    }
                    ManagedFactureAchat w = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
                    if (w != null) {
                        w.resetView(pp.getPieceAchat().getAchat());
                        int idx = w.getDocAchat().getReglements().indexOf(pp.getPieceAchat());
                        if (idx > -1) {
                            w.getDocAchat().getReglements().set(idx, pp.getPieceAchat());
                            update("data_mensualite_facture_achat");
                        }
                    }
                }
                pieceAvance.setStatutPiece(pp.getPieceAchat().getStatutPiece());
                int idx = pieceAvance.getPhasesAchat().indexOf(pp);
                if (idx >= 0 && (idx + 1) < pieceAvance.getPhasesAchat().size()) {
                    pieceAvance.getPhasesAchat().get(idx + 1).setEtapeActive(true);
                    currentPhaseAchat = pieceAvance.getPhasesAchat().get(idx + 1);
                } else if (idx == (pieceAvance.getPhasesAchat().size() - 1)) {
                    pieceAvance.getPhasesAchat().get(idx).setEtapeActive(true);
                }
                idx = piecesCaisses.indexOf(pp.getPieceAchat());
                if (idx > -1) {
                    piecesCaisses.set(idx, pp.getPieceAchat());
                }
                idx = pieceAvance.getPhasesAchat().indexOf(pp);
                if (idx >= 0) {
                    pieceAvance.getPhasesAchat().set(idx, pp);
                }
                //Comptabilise la pièce 
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    w.comptabiliserPhaseCaisseAchat(pp, false, false);
                }
            }
        } else {
            getWarningMessage("Phase déjà valide! ");
        }
        update("head_corp_form_suivi_pra");
        update("head_form_suivi_pra");
    }

    public void comptabiliserPhaseAcompteAchat(YvsComptaPhaseAcompteAchat pp, boolean comptabilise) {
        selectPhaseAcompteAchat = pp;
        if (selectAcompte.getPhasesReglement() != null ? !selectAcompte.getPhasesReglement().isEmpty() : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                int idx = selectAcompte.getPhasesReglement().indexOf(pp);
                if (idx > -1) {
                    if (idx == 0) {
                        if (comptabilise) {
                            w.comptabiliserPhaseAcompteAchat(pp, true, true);
                        } else {
                            openDialog("dlgComptabilisePhase");
                        }
                    } else {
                        YvsComptaPhaseAcompteAchat prec = selectAcompte.getPhasesReglement().get(idx - 1);
                        if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_ACOMPTE_ACHAT)) {
                            if (comptabilise) {
                                w.comptabiliserPhaseAcompteAchat(pp, true, true);
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

    public void printPhaseAcompte(YvsComptaPhaseAcompteAchat y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getPieceAchat().getStatut() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getPieceAchat().getMontant()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("LOGO", returnLogo());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                executeReport("phase_pc_acompte_achat", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedReglementAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void validEtapesAcompte(YvsComptaPhaseAcompteAchat pp) {
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            if ((pp.getCaisse() != null ? pp.getCaisse().getId() > 0 : false) && pp.getDateValider() != null) {
                w.validEtapesAcompte(selectAcompte, pp);
            } else if (pp.getDateValider() == null) {
                getErrorMessage("Veuillez entrer une date de validation !");
            } else {
                getErrorMessage("Veuillez choisir une caisse ou banque !");
            }
        }
        update("head_form_suivi_paa");
    }

    public void comptabiliserPhaseCreditAchat(YvsComptaPhaseReglementCreditFournisseur pp, boolean comptabilise) {
        selectPhaseCreditAchat = pp;
        if (selectCredit.getPhasesReglement() != null ? !selectCredit.getPhasesReglement().isEmpty() : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                int idx = selectCredit.getPhasesReglement().indexOf(pp);
                if (idx > -1) {
                    if (idx == 0) {
                        if (comptabilise) {
                            w.comptabiliserPhaseCaisseCreditAchat(pp, true, true);
                        } else {
                            openDialog("dlgComptabilisePhase");
                        }
                    } else {
                        YvsComptaPhaseReglementCreditFournisseur prec = selectCredit.getPhasesReglement().get(idx - 1);
                        if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_CREDIT_ACHAT)) {
                            if (comptabilise) {
                                w.comptabiliserPhaseCaisseCreditAchat(pp, true, true);
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

    public void printPhaseCredit(YvsComptaPhaseReglementCreditFournisseur y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getReglement().getStatut() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getReglement().getValeur()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                executeReport("phase_pc_credit_achat", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedReglementAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void validEtapesCredit(YvsComptaPhaseReglementCreditFournisseur pp) {
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            if ((pp.getCaisse() != null ? pp.getCaisse().getId() > 0 : false) && pp.getDateValider() != null) {
                w.validEtapesCredit(selectCredit, pp);
            } else if (pp.getDateValider() == null) {
                getErrorMessage("Veuillez entrer une date de validation !");
            } else {
                getErrorMessage("Veuillez choisir une caisse ou banque !");
            }
        }
        update("head_form_suivi_pca");
    }

    //Supprimer les étapes de validation d'un chèque et ramener la pièce au statut en cours
    public void cancelAllEtapesPieces() {
        if (!autoriser("compta_cancel_piece_valide")) {
            openNotAcces();
            return;
        }
        YvsComptaCaissePieceAchat pc = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{pieceAvance.getId()});
        if (pc != null ? pc.getId() != null ? pc.getId() > 0 : false : false) {
            //vérifie le droit:
            if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                try {
                    int i = 0;
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (pc.getPhasesReglement() != null ? !pc.getPhasesReglement().isEmpty() : false) {
                        for (YvsComptaPhasePieceAchat ph : pc.getPhasesReglement()) {
                            ph.setPhaseOk(false);
                            ph.setEtapeActive(i == 0);
                            ph.setAuthor(currentUser);
                            dao.update(ph);
                            if (w != null) {
                                w.unComptabiliserPhaseCaisseAchat(ph, false, true);
                            }
                            i++;
                            int idx = pieceAvance.getPhasesAchat().indexOf(ph);
                            if (idx > -1) {
                                pieceAvance.getPhasesAchat().set(idx, ph);
                            }
                        }
                    } else {
                        if (w != null) {
                            w.unComptabiliserCaisseAchat(pc, false);
                        }
                    }
                    pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    if (pieceAchat != null) {
                        pieceAchat.setStatutPiece(pc.getStatutPiece());
                    }
                    pc.setDateUpdate(new Date());
                    pc.setAuthor(currentUser);
                    dao.update(pc);
                    int idx = piecesCaisses.indexOf(pc);
                    if (idx > 0) {
                        piecesCaisses.set(idx, pc);
                        update("table_list_piece_cheque_achat");
                    }
                } catch (Exception ex) {
                    getErrorMessage("Impossible d'annuler les phases!");
                    log.log(Level.SEVERE, null, ex);
                }
            } else {
                openDialog("dlgConfirmCancel");
            }
        }
        update("head_corp_form_suivi_pra");
        update("head_form_suivi_pra");
    }

    //Supprimer les étapes de validation d'un chèque et ramener la pièce au statut en cours
    public void cancelAllEtapesAcompte() {
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            w.cancelAllEtapesAcompte(selectAcompte, true);
        }
        update("head_form_suivi_paa");
    }

    public void cancelAllEtapesCredit() {
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            w.cancelAllEtapesCredit(selectCredit);
        }
        update("header_form_suivi_pca");
    }

    public void cancelValidEtapesPieces(YvsComptaPhasePieceAchat pp, boolean retour) {
        //l'étape suivante ne doit pas être validé
        if (!asDroitValidePhase(pp.getPhaseReg())) {
            openNotAcces();
            return;
        }
        int idx = pieceAvance.getPhasesAchat().indexOf(pp);
        YvsComptaPhasePieceAchat pSvt = null;
        if (idx >= 0 && (idx + 1) < pieceAvance.getPhasesAchat().size()) {
            pSvt = pieceAvance.getPhasesAchat().get(idx + 1);
        } else if (idx == (pieceAvance.getPhasesAchat().size() - 1) || idx == 0) {
            pSvt = pieceAvance.getPhasesAchat().get(idx);
        }
        if (pSvt != null) {
            if (!pSvt.isEtapeActive() ? !pSvt.equals(pp) : false) {
                getErrorMessage("Vous ne pouvez annuler cette étape !");
                return;
            }
            pSvt.setEtapeActive(false);
            idx = pieceAvance.getPhasesAchat().indexOf(pSvt);
            if (idx >= 0) {
                pieceAvance.getPhasesAchat().set(idx, pSvt);
            }
        }
        if (dao.isComptabilise(pp.getId(), Constantes.SCR_PHASE_CREDIT_ACHAT)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return;
            }
            //Annuler Comptabilise la pièce 
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                if (retour) {
                    if (!w.comptabiliserPhaseCaisseAchat(pp, false, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                } else {
                    if (!w.unComptabiliserPhaseCaisseAchat(pp, false, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return;
                    }
                }
            }
        }
        pp.setEtapeActive(true);
        pp.setAuthor(currentUser);
        pp.setPhaseOk(false);
        pp.setStatut(retour ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE);
        if (pp.getPhaseReg().getReglementOk()) {
            pp.getPieceAchat().setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            pp.getPieceAchat().setDatePaiement(null);
            pp.getPieceAchat().setCaissier(null);
            dao.update(pp.getPieceAchat());
            pieceAvance.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        }
        dao.update(pp);
        currentPhaseAchat = pp;
        currentPhaseAchat.setDateValider(new Date());

        YvsComptaCaissePieceAchat pc = pp.getPieceAchat();
        idx = pc.getPhasesReglement().indexOf(pp);
        if (idx >= 0) {
            pc.getPhasesReglement().set(idx, pp);
        }
        idx = piecesCaisses.indexOf(pp.getPieceAchat());
        if (idx >= 0) {
            piecesCaisses.set(idx, pc);
        }
        try {
            if (retour) {
                YvsBaseModeReglement mode = modeEspece();
                YvsComptaCaissePieceAchat y = buildPieceFromModel(-1, mode, pieceAvance.getDocAchat(), pp.getCaisse(), new Date(), montantRetour);
                y.setParent(new YvsComptaCaissePieceAchat(pieceAvance.getId()));
                y.setNote("Paiement pour pénalité d'extourne");
                y.setId(null);
                y = (YvsComptaCaissePieceAchat) dao.save1(y);
                pieceAvance.getSousAchats().add(y);
                update("corps_form_suivi_pra");
            }
        } catch (Exception ex) {
            getException("Action impossible", ex);
            getErrorMessage("Action impossible");
        }
        update("head_form_suivi_pra");
    }

    public void cancelValidEtapesAcompte(YvsComptaPhaseAcompteAchat pp, boolean retour) {
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            w.cancelValidEtapesAcompte(selectAcompte, pp, retour);
        }
        update("head_form_suivi_paa");
    }

    public void cancelValidEtapesCredit(YvsComptaPhaseReglementCreditFournisseur pp, boolean retour) {
        ManagedOperationFournisseur w = (ManagedOperationFournisseur) giveManagedBean(ManagedOperationFournisseur.class);
        if (w != null) {
            w.cancelValidEtapesCredit(selectCredit, pp, retour);
        }
        update("header_form_suivi_pca");
    }

    public void addParamStatut() {
        ParametreRequete p = new ParametreRequete("y.statutPiece", "statut", null, operateurStatut, "AND");
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p.setObjet(statut_.charAt(0));
        }
        paginator.addParam(p);
        loadAllPieces(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.numeroPiece", "numeroPiece", null, "=", "AND");
        if (numPieceF != null ? numPieceF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "numeroPiece", numPieceF + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.numeroPiece)", "numeroPiece", numPieceF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.referenceExterne)", "numeroPiece", numPieceF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.achat.numDoc)", "numeroPiece", numPieceF.toUpperCase() + "%", "LIKE", "OR"));
        }
        paginator.addParam(p);
        loadAllPieces(true, true);
    }

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_piece_achat c RIGHT JOIN yvs_compta_caisse_piece_achat y ON c.reglement = y.id "
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
        loadAllPieces(true, true);
    }
    
    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.achat.agence", "agence", null, "=", "AND");
        if (agenceSearch > 0) {
            p = new ParametreRequete("y.achat.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllPieces(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.datePaiement", "datePaiement", null, "=", "AND");
        if (dateSearch) {
            p = new ParametreRequete("y.datePaiement", "datePaiement", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllPieces(true, true);
    }

    public void addParamReglementCheque() {
        ParametreRequete p = new ParametreRequete("y.model.typeReglement", "modeReg", Constantes.MODE_PAIEMENT_BANQUE, "=", "AND");
        paginator.addParam(p);
        loadAllPieces(true, true);
    }

    public void addParamFournisseur() {
        ParametreRequete p;
        if (fournisseurF != null ? fournisseurF.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "fournisseur", fournisseurF.toUpperCase() + "%", " LIKE ", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.achat.fournisseur.codeFsseur)", "fournisseur", fournisseurF.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.achat.fournisseur.nom)", "fournisseur", fournisseurF.toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p = new ParametreRequete("y.client.codeClient", "client", null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllPieces(true, initForm);
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
        loadAllPieces(true, true);

    }

    public void addParamCaisse(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.caisse", "caisse", null, "=", "AND");
        if (ev.getNewValue() != null) {
            caisseF = (long) ev.getNewValue();
            if (caisseF > 0) {
                p.setObjet(new YvsBaseCaisse(caisseF));
            }
        }
        paginator.addParam(p);
        loadAllPieces(true, true);

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
        loadAllPieces(true, true);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        initForm = true;
        loadAllPieces(true, true);
    }

//    public void choosePaginatorPc(ValueChangeEvent ev) {
//        imax = (long) ev.getNewValue();
//        loadAllPieces(true, true);
//    }
    //charge les pièces encaisser et non encore lié à une facture
    public void filterPcCanBind() {
        ParametreRequete p = new ParametreRequete("y.statutPiece", "statutPiece", Constantes.STATUT_DOC_PAYER, "=", "AND");
        paginator.addParam(p);
        paginator.addParam(new ParametreRequete("y.achat", "achat", Constantes.STATUT_DOC_PAYER, " IS NULL", "AND"));
        loadAllPieces(true, true);
        update("table_acompte_clts");
        openDialog("dlgSaveAvance");
    }

    public void searchPieceByNum(String numP) {
        String val = (numP != null) ? (!numP.isEmpty() ? numP : null) : null;
        ParametreRequete p = new ParametreRequete("y.numeroPiece", "numeroPiece", val, " LIKE ", "AND");
        paginator.addParam(p);
        loadAllPieces(true, true);
        update("table_acompte_clts");
    }

    public void searchPieceByNumFournisseur(String numFournisseur) {
        String val = (numFournisseur != null) ? (!numFournisseur.isEmpty() ? numFournisseur : null) : null;
        ParametreRequete p = new ParametreRequete("y.fournisseur.codeFournisseur", "codeCLient", val, " LIKE ", "AND");
        paginator.addParam(p);
        loadAllPieces(true, true);
        update("table_acompte_clts");
    }

    public void print(YvsComptaCaissePieceAchat y) {
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
                executeReport("pc_achat", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedReglementAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onSelectDistantReglement(YvsComptaCaissePieceAchat y) {
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
        comptaSearch = comptabilised;
        paginator.addParam(new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptabilised, "=", "AND"));
        loadAllPieces(true, true);
        update("table_regFA");
    }

    boolean select = false;
    boolean replaceBon = false;

    public void openListBon(YvsComptaCaissePieceAchat y, boolean select, boolean replace) {
        if (y != null) {
            this.pieceAchat = y;
        }
        this.select = select;
        this.replaceBon = replace;
        if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
            openDialog("dlgListBons");
        } else {
            getErrorMessage("La pièce de caisse  est déjà réglé !");
        }
    }

    public void mooveBon(YvsComptaCaissePieceAchat y) {
        if (y != null) {
            this.pieceAchat = y;
        }
        if (y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
            getErrorMessage("La pièce est déjà réglé !");
            return;
        }
        try {
            dao.delete(y.getJustify());
            y.setJustify(null);
        } catch (Exception ex) {
            getException("Erreur lors de la suppression du bon !", ex);
        }

    }

    public void loadBonProvisoire(SelectEvent ev) {
        if (ev != null) {
            YvsComptaBonProvisoire y = (YvsComptaBonProvisoire) ev.getObject();
            if (y.getStatutJustify().equals(Constantes.ETAT_JUSTIFIE)) {
                getErrorMessage("Ce bon provisoire est dejà justfifié !");
                return;
            }
            if (!y.getStatutPaiement().equals(Constantes.ETAT_REGLE)) {
                getWarningMessage("Le bon provisoire selectionné n'est pas payé !");
            }
            if (!select) {
                YvsBaseModeReglement espece = modeEspece();
                pieceAvance.setNumRefExterne(y.getNumero());
                pieceAvance.setBonProvisoire(UtilCompta.buildBeanBonProvisoire(y));
                if (pieceAvance.getId() < 1) {
                    pieceAvance.setMontant(y.getReste());
                    pieceAvance.setNumRef(y.getNumero());
                    pieceAvance.setMode(UtilCom.buildBeanModeReglement(espece));
                }
                if (pieceAvance.getCaisse() != null ? pieceAvance.getCaisse().getId() < 1 : true) {
                    pieceAvance.setCaisse(UtilCompta.buildSimpleBeanCaisse(y.getCaisse()));
                }
                update("zone_pcv_edit_pieceFA");
            } else {
                if (pieceAchat != null ? pieceAchat.getId() < 1 : true) {
                    getErrorMessage("Vous devez selectionner une piece");
                    return;
                }
                if (pieceAchat.getJustify() != null ? pieceAchat.getJustify().getId() > 0 : false) {
                    if (!replaceBon) {
                        getErrorMessage("Cette piece est deja rattachée à un bon provisoire");
                        return;
                    } else {
                        try {
                            dao.delete(pieceAchat.getJustify());
                        } catch (Exception ex) {
                            getErrorMessage("Changement du Bon impossible !");
                            getException("Erreur suppression Bon achat", ex);
                            return;
                        }
                    }
                }
                YvsComptaJustifBonAchat bon = new YvsComptaJustifBonAchat(pieceAchat, y);
                bon.setAuthor(currentUser);
                bon = (YvsComptaJustifBonAchat) dao.save1(bon);
                pieceAchat.setJustify(bon);
                int idx = piecesCaisses.indexOf(pieceAchat);
                if (idx > -1) {
                    piecesCaisses.set(idx, pieceAchat);
                }
                update("table_regFA");
                succes();
            }
        }
    }

    public boolean isComptabiliseBean(PieceTresorerie y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CAISSE_ACHAT));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsComptaCaissePieceAchat y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_CAISSE_ACHAT));
            }
            return y.getComptabilise();
        }
        return false;
    }
}
