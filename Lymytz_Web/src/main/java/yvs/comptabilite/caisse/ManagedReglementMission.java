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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.UtilCompta;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.tresorerie.ManagedBonProvisoire;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseTrancheReglement;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceMission;
import yvs.entity.compta.YvsComptaJustifBonMission;
import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.grh.activite.YvsGrhMissions;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsers;
import yvs.grh.ManagedEmployes;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.grh.bean.mission.ManagedMission;
import yvs.grh.bean.mission.Mission;
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
public class ManagedReglementMission extends Managed implements Serializable {

    private Mission selectedDoc = new Mission();
    private YvsComptaCaissePieceMission pieceMission = newPieceCaiss();
    private boolean displayBtnSave;
    private PieceTresorerie pieceAvance = new PieceTresorerie();

    private String fournisseurF, numPieceF, statutSearch;
    private boolean dateSearch;
    private Date dateDebutSearch = new Date(), dateFinSearch = new Date();
    private Boolean comptaSearch;
    private long nbrComptaSearch;
    private Long agenceSearch;
    private List<YvsComptaCaissePieceMission> avances;

    public ManagedReglementMission() {
        avances = new ArrayList<>();
    }

    public Long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(Long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public long getNbrComptaSearch() {
        return nbrComptaSearch;
    }

    public void setNbrComptaSearch(long nbrComptaSearch) {
        this.nbrComptaSearch = nbrComptaSearch;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public String getFournisseurF() {
        return fournisseurF;
    }

    public void setFournisseurF(String fournisseurF) {
        this.fournisseurF = fournisseurF;
    }

    public String getStatutSearch() {
        return statutSearch;
    }

    public void setStatutSearch(String statutSearch) {
        this.statutSearch = statutSearch;
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

    public List<YvsComptaCaissePieceMission> getAvances() {
        return avances;
    }

    public void setAvances(List<YvsComptaCaissePieceMission> avances) {
        this.avances = avances;
    }

    public Mission getSelectedDoc() {
        return selectedDoc;
    }

    public void setSelectedDoc(Mission selectedDoc) {
        this.selectedDoc = selectedDoc;
    }

    public YvsComptaCaissePieceMission getPieceMission() {
        return pieceMission;
    }

    public void setPieceMission(YvsComptaCaissePieceMission pieceMission) {
        this.pieceMission = pieceMission;
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

    public String getEmployeF() {
        return fournisseurF;
    }

    public void setEmployeF(String fournisseurF) {
        this.fournisseurF = fournisseurF;
    }

    @Override
    public void doNothing() {
        if (pieceAvance != null ? pieceAvance.getId() < 1 : true) {
            pieceAvance = new PieceTresorerie();
        }
        if (pieceAvance.getMode() != null ? pieceAvance.getMode().getId() < 1 : true) {
            pieceAvance.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        }
    }

    @Override
    public void loadAll() {
        if (giveExerciceActif() != null) {
//            dao.getEquilibreAchat(currentAgence.getSociete().getId(), giveExerciceActif().getDateDebut(), giveExerciceActif().getDateFin());
        }
        if (pieceMission.getModel() == null) {
            pieceMission.setModel(modeEspece());
        }
        if (pieceMission.getCaisse() == null) {
            pieceMission.setCaisse(new YvsBaseCaisse((long) -1));
        }
        doNothing();
    }

    public void loadAllPiece(boolean avancer, boolean init) {
        paginator.addParam(new ParametreRequete("y.author.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND"));
        avances = paginator.executeDynamicQuery("YvsComptaCaissePieceMission", "y.datePaiement DESC", avancer, init, (int) imax, dao);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllPiece(true, true);
    }

    public void loadPieceByStatut(String statut) {
        paginator.getParams().clear();
        this.statutSearch = statut;
        addParamStatut();
    }

    public void loadPieceByStatutAndComptabilise(String statut, Boolean b) {
        paginator.getParams().clear();
        this.statutSearch = statut;
        addParamStatut();
        addParamComptabilise(b);
    }

    public void addParamComptabilise(Boolean b) {
        ParametreRequete p = new ParametreRequete("y.comptabilise", "comptabilise", b, "=", "AND");
        paginator.addParam(p);
        loadAllPiece(true, true);
    }

    public void addParamStatut() {
        ParametreRequete p = new ParametreRequete("y.statutPiece", "statut", null, "=", "AND");
        if (statutSearch != null ? statutSearch.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutPiece", "statut", statutSearch.charAt(0), "=", "AND");
        }
        paginator.addParam(p);
        loadAllPiece(true, true);
    }

    public void addParamReference() {
        ParametreRequete p = new ParametreRequete("y.numeroPiece", "reference", null, "=", "AND");
        if (numPieceF != null ? numPieceF.trim().length() > 0 : false) {
            p = new ParametreRequete("UPPER(y.numeroPiece)", "reference", "%" + numPieceF.toUpperCase() + "%", "LIKE", "AND");
        }
        paginator.addParam(p);
        loadAllPiece(true, true);
    }

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_piece_mission c RIGHT JOIN yvs_compta_caisse_piece_mission y ON c.reglement = y.id "
                    + "INNER JOIN yvs_base_caisse e ON y.caisse = e.id INNER JOIN yvs_compta_journaux h ON e.journal = h.id "
                    + "INNER JOIN yvs_agences a ON h.agence = a.id WHERE y.statut_piece = 'P' AND a.societe = ? "
                    + "AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (dateSearch) {
                query += " AND e.date_paiement BETWEEN ? AND ?";
                param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebutSearch, 2), new Options(dateFinSearch, 3)};
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, param);
            nbrComptaSearch = count != null ? count : 0;
        }
        paginator.addParam(p);
        loadAllPiece(true, true);
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.mission.agence", "agence", null, "=", "AND");
        if (agenceSearch != null ? agenceSearch > 0 : false) {
            p = new ParametreRequete("y.mission.employe.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        loadAllPiece(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.datePaiement", "datePaiement", null, "=", "AND");
        if (dateSearch) {
            p = new ParametreRequete("y.datePaiement", "datePaiement", dateDebutSearch, dateFinSearch, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAllPiece(true, true);
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean controlePiece() {
        //controle la conformité du formulaire
        if (pieceAvance.getMission().getEmploye().getId() <= 0) {
            getErrorMessage("Vous devez entrer un employe !");
            return false;
        }
        if (pieceAvance.getMode() != null ? pieceAvance.getMode().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le mode de paiement");
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

    @Override
    public Serializable recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void recopieObject(YvsComptaCaissePieceMission pc) {
        pieceMission = new YvsComptaCaissePieceMission(pc.getId());
        pieceMission.setAuthor(pc.getAuthor());
        pieceMission.setCaisse(pc.getCaisse());
        pieceMission.setCaissier(pc.getCaissier());
        pieceMission.setDatePaiement(pc.getDatePaiement());
        pieceMission.setDatePaimentPrevu(pc.getDatePaimentPrevu());
        pieceMission.setDatePiece(pc.getDatePiece());
        pieceMission.setModel(pc.getModel());
        pieceMission.setMontant(pc.getMontant());
        pieceMission.setMontantRest(pc.getMontantRest());
        pieceMission.setNew_(pc.isNew_());
        pieceMission.setNameMens(pc.getNameMens());
        pieceMission.setNote(pc.getNote());
        pieceMission.setNumeroPiece(pc.getNumeroPiece());
        pieceMission.setStatutPiece(pc.getStatutPiece());
        pieceMission.setMission(pc.getMission());
    }

    @Override
    public void populateView(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public YvsComptaCaissePieceMission newPieceCaiss() {
        YvsComptaCaissePieceMission pc = new YvsComptaCaissePieceMission((long) 0);
        pc.setModel(new YvsBaseModeReglement(0l));
        pc.setMission(new YvsGrhMissions((long) 0));
        pc.setCaisse(new YvsBaseCaisse((long) 0));
        pc.setDatePaimentPrevu(new Date());
        pc.setModel(modeEspece());
        pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        if (!selectedDoc.getReglements().isEmpty()) {
            pc.setMontant(selectedDoc.getTotalFraisMission() - giveTotalPT(selectedDoc.getReglements()));
            if (pc.getMontant() < 0) {
                pc.setMontant(0);
            }
        }
        return pc;
    }

    public void createNewPiece() {
        if (selectedDoc.getId() > 0) {
            pieceMission.setMission(new YvsGrhMissions(selectedDoc.getId()));
            YvsComptaCaissePieceMission pcv = createOnePieceCaisse(UtilGrh.buildMission(selectedDoc, currentUser), UtilCompta.buildBeanTresoreri(pieceMission), true, true);
            if (pcv != null) {
                //si le Mode de règlement existe, récupérons son libellé
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
                int idx = selectedDoc.getReglements().indexOf(pcv);
                if (idx > -1) {
                    selectedDoc.getReglements().set(idx, pcv);
                } else {
                    selectedDoc.getReglements().add(0, pcv);
                }
                ManagedMission service = (ManagedMission) giveManagedBean(ManagedMission.class);
                if (service != null) {
                    int i = service.getListMission().indexOf(new YvsGrhMissions(selectedDoc.getId()));
                    if (i >= 0) {
                        int j = service.getListMission().get(i).getReglements().indexOf(pcv);
                        if (j > -1) {
                            service.getListMission().get(i).getReglements().set(j, pcv);
                        } else {
                            service.getListMission().get(i).getReglements().add(0, pcv);
                        }
                        update("data_mission_reg");
                    }
                }
                pieceMission = newPieceCaiss();
                succes();
            }
        } else {
            getErrorMessage("Aucune Mission n'a été selectionné !");
        }
    }

    public void saveModelreglement() {
        if (selectedDoc.getId() > 0) {
            for (YvsComptaCaissePieceMission c : selectedDoc.getReglements()) {
                c.setId(Long.valueOf(0));
//                c = createNewPieceCaisse(selectedDoc, c, true);
                c = createOnePieceCaisse(c.getMission(), UtilCompta.buildBeanTresoreri(c), true, true);
            }
            selectedDoc.setReglements(dao.loadNameQueries("YvsComptaCaissePieceMission.findByMission", new String[]{"mission"}, new Object[]{new YvsGrhMissions(selectedDoc.getId())}));
            displayBtnSave = false;
        } else {
            getErrorMessage("Aucune facture n'a été selectionné !");
        }
    }

//    private boolean controleAddPiece(Mission mission, YvsComptaCaissePieceMission pt, boolean msg) {
//        if (pt.getId() != null ? pt.getId() < 0 : false) {
//            if (msg) {
//                getErrorMessage("Vous devez d'abord enregistrer la generation");
//            }
//            return false;
//        }
//        if (pt.getModel() != null ? pt.getModel().getId() < 1 : true) {
//            if (msg) {
//                getErrorMessage("Vous devez precisez le mode de paiement");
//            }
//            return false;
//        }
//        double mtn = 0;
//        if ((mission.getReglements() == null) ? true : mission.getReglements().isEmpty()) {
//            mission.setReglements(dao.loadNameQueries("YvsComptaCaissePieceMission.findByMission", new String[]{"mission"}, new Object[]{new YvsGrhMissions(mission.getId())}));
//        }
//        if (mission.getReglements().contains(pt)) {
//            mtn = (-mission.getReglements().get(mission.getReglements().indexOf(pt)).getMontant() + pt.getMontant());
//        } else {
//            mtn = pt.getMontant();
//        }
//        if (mission.getTotalFraisMission() < (giveTotalPT(mission.getReglements()) + mtn) && pt.getMission() != null) {
//            if (msg) {
//                getErrorMessage("Le montant des frais  ne doit pas être inférieure aux règlements planifié");
//            }
//            return false;
//        }
//        if (pt.getDatePaimentPrevu() == null) {
//            if (msg) {
//                getErrorMessage("Vous devez préciser la date de paiement !");
//            }
//            return false;
//        }
//        if (pt.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
//            if (msg) {
//                getErrorMessage("La pièce en cours est déjà payé !");
//            }
//            return false;
//        }
//        return true;
//    }
    private boolean controlePiece(PieceTresorerie pieceAvance, boolean msg, YvsGrhMissions d, boolean controleFrais, boolean regle) {
        ManagedMission service = (ManagedMission) giveManagedBean(ManagedMission.class);
        if (service != null) {
            d = service.setMontantTotalMission(d);
        }
        //build pièce trésorerie
        if (pieceAvance == null) {
            return false;
        }
        if (pieceAvance.getMontant() <= 0) {
            if (msg) {
                getErrorMessage("Vous devez precisez le montant !");
            }
            return false;
        }
        if (controleFrais) {
            if (pieceAvance.getMontant() > d.getTotalReste()) {
                if (msg) {
                    getErrorMessage("La valeur de la pièce de trésorerie ne doit pas être supérieure à la valeur total des frais de mission");
                }
                return false;
            }
        }
        if (pieceAvance.getDatePaiementPrevu() == null) {
            if (msg) {
                getWarningMessage("Aucune date de paiement n'a été indiqué");
            }
            return false;
        }
        if (pieceAvance.getMode() != null ? pieceAvance.getMode().getId() < 1 : true) {
            if (msg) {
                getErrorMessage("Vous devez precisez le mode de paiement !");
            }
            return false;
        }
        if (regle) {
            if (pieceAvance.getCaisse().getId() <= 0) {
                getErrorMessage("Aucune caisse de paiement n'a été trouvé !");
                return false;
            }
            //la mission doit être validé
            if (d.getStatutMission() != Constantes.STATUT_DOC_VALIDE && d.getStatutMission() != Constantes.STATUT_DOC_CLOTURE) {
                getErrorMessage("L'ordre de mission n'est pas confirmé !");
                return false;
            }
        }
        return true;
    }

    public YvsComptaCaissePieceMission createOnePieceCaisse(YvsGrhMissions d) {
        return createOnePieceCaisse(d, pieceAvance, true, true);
    }

    //Créée une pièce de caisse en attente de paiement
    public YvsComptaCaissePieceMission createOnePieceCaisse(YvsGrhMissions d, PieceTresorerie pieceAvance, boolean msg, boolean controleFrais) {
        return createOnePieceCaisse(d, pieceAvance, msg, controleFrais, false, null);
    }

    public YvsComptaCaissePieceMission createOnePieceCaisse(YvsGrhMissions d, PieceTresorerie piece, boolean msg, boolean controleFrais, boolean regle, YvsUsers caissier) {
        if (controlePiece(piece, msg, d, controleFrais, regle)) {
            selectedDoc = UtilGrh.buildBeanMission(d);
            YvsComptaCaissePieceMission pt = new YvsComptaCaissePieceMission();
            pt.setAuthor(currentUser);
            ManagedCaisses service_ = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service_ != null) {
                if (piece.getCaisse().getId() > 0) {
                    int idx = service_.getCaisses().indexOf(new YvsBaseCaisse(piece.getCaisse().getId()));
                    if (idx > -1) {
                        pt.setCaisse(service_.getCaisses().get(idx));
                    } else {
                        pt.setCaisse(new YvsBaseCaisse(piece.getCaisse().getId()));
                    }
                } else {
                    pt.setCaisse(service_.findByResponsable(currentUser.getUsers()));
                }
            }
            pt.setDatePaiement(null);
            pt.setDatePaimentPrevu(piece.getDatePaiementPrevu());
            pt.setDatePiece(piece.getDatePiece());
            pt.setId(null);
            pt.setMontant(arrondi(piece.getMontant()));
            pt.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            pt.setModel(new YvsBaseModeReglement((long)piece.getMode().getId()));
            pt.setMission(d);
            pt.setDateSave(new Date());
            pt.setDateUpdate(new Date());
            pt.setNote(piece.getDescription());
            if (piece.getBonProvisoire() != null ? piece.getBonProvisoire().getId() > 0 : false) {
                pt.setBonProvisoire(new YvsComptaBonProvisoire(piece.getBonProvisoire().getId(), piece.getBonProvisoire().getNumero()));
            }
            if (regle) {
                pt.setCaissier(caissier);
                pt.setDatePaiement(piece.getDatePaiement());
            }
            if ((pt.getId() != null) ? pt.getId() < 1 : true) {
                String numero = genererReference(Constantes.TYPE_PC_MISSION_NAME, pt.getDatePiece());
                if (numero != null ? numero.trim().length() < 1 : true) {
                    return null;
                }
                pt.setNumeroPiece(numero);
                pt = (YvsComptaCaissePieceMission) dao.save1(pt);
                if (pt.getBonProvisoire() != null ? pt.getBonProvisoire().getId() > 0 : false) {
                    YvsComptaJustifBonMission bon = new YvsComptaJustifBonMission(pt, pt.getBonProvisoire());
                    bon.setAuthor(currentUser);
                    bon = (YvsComptaJustifBonMission) dao.save1(bon);
                    pt.setJustify(bon);
                }
            } else {
                pt.setId(pt.getId());
                dao.update(pt);
            }
            if (regle) {
                payerMission(pt, false);
            }
            selectedDoc.setReglements(dao.loadNameQueries("YvsComptaCaissePieceMission.findByMission", new String[]{"mission"}, new Object[]{d}));
            displayBtnSave = false;
            return pt;
        } else {
            pieceMission = newPieceCaiss();
            return null;
        }
    }

    public boolean annulerPayeMission(YvsComptaCaissePieceMission mi) {
        if (mi != null ? mi.getId() > 0 : false) {
            if (!verifyCancelPieceCaisse(mi.getDatePaiement())) {
                return false;
            }
            if (dao.isComptabilise(mi.getId(), Constantes.SCR_FRAIS_MISSIONS)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return false;
                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    if (!w.unComptabiliserCaisseMission(mi, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return false;
                    }
                }
            }
            mi.setCaissier(currentUser.getUsers());
            mi.setDatePaiement(null);
            mi.setAuthor(currentUser);
            mi.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            dao.update(mi);
            return true;
        }
        return false;
    }

    public boolean payerMission(YvsComptaCaissePieceMission mi, boolean msg) {
        if (mi != null ? mi.getId() > 0 : false) {
            if (mi.getMission().getStatutMission() == Constantes.STATUT_DOC_VALIDE || mi.getMission().getStatutMission() == Constantes.STATUT_DOC_CLOTURE) {
                mi.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{mi.getCaisse().getId()}));
                if (!controleAccesCaisse(mi.getCaisse(), true)) {
                    return false;
                }
                if (mi.getJustify() != null ? mi.getJustify().getId() > 0 : false) {
                    if (mi.getJustify().getPiece().getStatutPaiement().equals(Constantes.ETAT_REGLE)) {
                        if (msg) {
                            getErrorMessage("Le bon provisoire rattaché à la mission n'est payé");
                        }
                        return false;
                    }
                }
                mi.setCaissier(currentUser.getUsers());
                mi.setAuthor(currentUser);
                mi.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                dao.update(mi);
                if (mi.getJustify() != null ? mi.getJustify().getId() > 0 : false) {
                    ManagedBonProvisoire w = (ManagedBonProvisoire) giveManagedBean(ManagedBonProvisoire.class);
                    if (w != null) {
                        w.verifyToJustify(mi.getMission());
                    }
                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    w.comptabiliserCaisseMission(mi, false, false);
                }
                return true;
            } else {
                if (msg) {
                    getErrorMessage("L'ordre de mission d'origine n'a pas été validé");
                }
            }
        } else {
            if (msg) {
                getErrorMessage("Cette pièce n'a pas de source");
            }
        }
        return false;
    }

//    public YvsComptaCaissePieceMission createNewPieceCaisse(Mission d, YvsComptaCaissePieceMission pt, boolean msg) {
//        if (controleAddPiece(d, pt, msg)) {
//            YvsComptaCaissePieceMission piece = new YvsComptaCaissePieceMission((long) -111);
//            piece.setAuthor(currentUser);
//            piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
//            piece.setMontant(arrondi(pt.getMontant()));
//            piece.setDatePaimentPrevu(pt.getDatePaimentPrevu());
//            piece.setNumeroPiece(findNumPiece("PC_M/" + d.getNumeroMission() + "" + (d.getReglements().size() + 1), d));
//            piece.setDatePiece(new Date());
//            piece.setDateUpdate(new Date());
//            piece.setNote(pt.getNote());
//            piece.setMission(UtilGrh.buildMission(d, currentUser));
//            if (pt.getModel() != null) {
//                piece.setModel((pt.getModel().getId() > 0) ? pt.getModel() : null);
//            }
//            piece.setNew_(true);
//            if (pt.getCaisse() != null) {
//                ManagedCaisses s = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
//                if (s != null) {
//                    int idx = s.getCaisses().indexOf(pt.getCaisse());
//                    if (idx > -1) {
//                        pt.setCaisse(s.getCaisses().get(idx));
//                    }
//                }
//                piece.setCaisse((pt.getCaisse().getId() > 0) ? pt.getCaisse() : null);
//            }
//            if ((pt.getId() != null) ? pt.getId() < 1 : true) {
//                piece.setDateSave(new Date());
//                piece.setId(null);
//                piece = (YvsComptaCaissePieceMission) dao.save1(piece);
//            } else {
//                piece.setId(pt.getId());
//                dao.update(piece);
//            }
//            return piece;
//        } else {
//            pieceMission = newPieceCaiss();
//        }
//        return null;
//    }
    public void saveNewPc() {
        //controle la conformité du formulaire
        if (controlePiece()) {
            YvsComptaCaissePieceMission pc = new YvsComptaCaissePieceMission();
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
                    int idx = service.getModes().indexOf(new YvsBaseModeReglement((long)pieceAvance.getMode().getId()));
                    if (idx >= 0) {
                        pc.setModel(service.getModes().get(idx));
                    }
                }
            }
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
                pc = (YvsComptaCaissePieceMission) dao.save1(pc);
                pc.setNew_(true);
                pieceAvance.setId(pc.getId());
                avances.add(0, pc);
            } else {
                pc.setId(pieceAvance.getId());
                dao.update(pc);
                if (avances.contains(pc)) {
                    avances.set(avances.indexOf(pc), pc);
                }
            }
            succes();
        }
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deletePieceCaisse(boolean all) {
        if (pieceMission.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
            try {
                pieceMission.setAuthor(currentUser);
                dao.delete(pieceMission);
                if (selectedDoc.getReglements().contains(pieceMission)) {
                    selectedDoc.getReglements().remove(pieceMission);
                }
                if (!all) {
                    succes();
                }
                pieceMission = newPieceCaiss();
                if (selectedDoc.getReglements().isEmpty()) {
                    displayBtnSave = false;
                }
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cet élément !");
            }
        } else {
            getErrorMessage("Cette pièce est déjà encaissé !");
        }
    }

    public void deleteAllPiece() {
        List<YvsComptaCaissePieceMission> l = new ArrayList<>(selectedDoc.getReglements());
        for (YvsComptaCaissePieceMission cp : l) {
            if ((cp.getId() != null)) {
                pieceMission = cp;
                deletePieceCaisse(true);
            }
        }
        succes();
    }

    public void deleteOneLinePc() {
        if (pieceAvance.getId() > 0) {
            if (pieceAvance.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                try {
                    YvsComptaCaissePieceMission pc = new YvsComptaCaissePieceMission(pieceAvance.getId());
                    pc.setAuthor(currentUser);
                    dao.delete(pc);
                    avances.remove(pc);
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

    public void loadOnViewPiece(SelectEvent ev) {
        if (ev != null) {
            if (selectedDoc != null) {
                YvsComptaCaissePieceMission pc = (YvsComptaCaissePieceMission) ev.getObject();
                loadPcOnViewPiece(pc);
            }
        }
    }

    public void loadPcOnViewPiece(YvsComptaCaissePieceMission ev) {
        if (ev != null) {
            pieceAvance = UtilCompta.buildBeanTresoreri(ev);
        }
    }

    public void onSelectDistant(YvsGrhMissions y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Règlements Missions", "modCompta", "smenRegMiss", true);
            }
        }
    }

    public void OnSelectObject(YvsGrhMissions y) {
        ManagedMission service = (ManagedMission) giveManagedBean(ManagedMission.class);
        if (service != null) {
            y = service.setMontantTotalMission(y);
        }
        selectedDoc = UtilGrh.buildBeanMission(y);
        selectedDoc.setReglements(dao.loadNameQueries("YvsComptaCaissePieceMission.findByMission", new String[]{"mission"}, new Object[]{y}));
        displayBtnSave = false;
        pieceMission = newPieceCaiss();
        update("zone_form_regMission");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            YvsGrhMissions y = (YvsGrhMissions) ev.getObject();
            OnSelectObject(y);
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void selectOnePiece(SelectEvent ev) {
        recopieObject((YvsComptaCaissePieceMission) ev.getObject());
        if (pieceMission.getModel() == null) {
            pieceMission.setModel(modeEspece());
        }
        if (pieceMission.getCaisse() == null) {
            pieceMission.setCaisse(new YvsBaseCaisse(-(long) 10));
        }
    }

    public void unselectOnePiece(UnselectEvent ev) {
        pieceMission = newPieceCaiss();
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private YvsComptaCaissePieceMission buildPieceFromModel(long id, YvsBaseModeReglement mode, Mission d, YvsBaseCaisse caisse, Date date, double montant, int numero) {
        YvsComptaCaissePieceMission piece = new YvsComptaCaissePieceMission(id);
        piece.setAuthor(currentUser);
        piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        piece.setMontant(montant);
        piece.setDatePaimentPrevu(date);
        piece.setNumeroPiece("PC_M/" + d.getNumeroMission() + "-0" + numero);
        piece.setDatePiece(new Date());
        piece.setMission(UtilGrh.buildMission(d, currentUser));
        piece.setModel(mode);
        if ((caisse != null) ? caisse.getId() > 0 : false) {
            piece.setCaisse(caisse);
        }
        return piece;
    }

    public List<YvsComptaCaissePieceMission> generetedPiecesFromModel(YvsBaseModelReglement model, Mission d) {
        ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        YvsBaseCaisse caisse = null;
        if (service != null) {
            caisse = service.findByResponsable(currentUser.getUsers());
        }
        return generetedPiecesFromModel(model, d, caisse);
    }

    public List<YvsComptaCaissePieceMission> generetedPiecesFromModel(YvsBaseModelReglement model, Mission d, YvsBaseCaisse caisse) {
        List<YvsComptaCaissePieceMission> re = new ArrayList<>();
        List<YvsBaseTrancheReglement> lt = dao.loadNameQueries("YvsBaseTrancheReglement.findByModel", new String[]{"model"}, new Object[]{model});
        if (d.getRestPlanifier() > 0) {
            long id = -1000;
            YvsComptaCaissePieceMission piece;
            Calendar cal = Calendar.getInstance();
            cal.setTime(d.getDateDebut());  //date de la facturation
            double totalTaux = 0, sommeMontant = 0;
            YvsBaseModeReglement espece = modeEspece(), mode = null;
            YvsBaseTrancheReglement trch;
            if (lt != null ? !lt.isEmpty() : false) {
                for (int i = 0; i < lt.size() - 1; i++) {
                    trch = lt.get(i);
                    cal.add(Calendar.DAY_OF_MONTH, trch.getIntervalJour());
                    double montant = arrondi(d.getRestPlanifier() * trch.getTaux() / 100);
                    sommeMontant += montant;
                    if (trch.getMode() != null ? trch.getMode().getId() != null ? trch.getMode().getId() > 0 : false : false) {
                        mode = trch.getMode();
                    } else {
                        mode = espece;
                    }
                    piece = buildPieceFromModel(id++, mode, d, caisse, cal.getTime(), montant, (i + 1));
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
            double montant = d.getRestPlanifier() - sommeMontant;
            piece = buildPieceFromModel(id++, mode, d, caisse, cal.getTime(), montant, (lt != null ? lt.size() : 1));
            re.add(piece);
        } else {
            getErrorMessage("Le montant du document n'a pas été trouvé !");
        }
        return re;
    }

    public List<YvsComptaCaissePieceMission> _generetedPiecesFromModel(YvsBaseModelReglement model, Mission d) {
        List<YvsComptaCaissePieceMission> re = new ArrayList<>();
        List<YvsBaseTrancheReglement> lt = dao.loadNameQueries("YvsBaseTrancheReglement.findByModel", new String[]{"model"}, new Object[]{model});
        long id = -1000;
        YvsComptaCaissePieceMission piece;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d.getDateDebut());  //date de la facturation
        if (d.getRestPlanifier() > 0) {
            int i = 1;
            double totalTaux = 0;
            YvsBaseModeReglement espece = modeEspece();
            for (YvsBaseTrancheReglement trch : lt) {
                cal.setTime(d.getDateDebut());  //date de la facturation
                piece = new YvsComptaCaissePieceMission(id++);
                piece.setAuthor(currentUser);
                piece.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                piece.setMontant(arrondi(d.getRestPlanifier() * trch.getTaux() / 100));
                cal.add(Calendar.DAY_OF_MONTH, trch.getIntervalJour());
                piece.setDatePaimentPrevu(cal.getTime());
                piece.setNumeroPiece("PC_M/" + d.getNumeroMission() + "-0" + i++);
                piece.setDatePiece(new Date());
                piece.setMission(UtilGrh.buildMission(d, currentUser));
                if (trch.getMode() != null ? trch.getMode().getId() != null ? trch.getMode().getId() > 0 : false : false) {
                    piece.setModel(trch.getMode());
                } else {
                    piece.setModel(espece);
                }
//                piece.setEmploye(new YvsGrhEmployes(d.getEmploye().getId()));
                //trouve la caisse de l'utilisateur si elle existe
                ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
                if (service != null) {
                    piece.setCaisse(service.findByResponsable(currentUser.getUsers()));
                }
                re.add(piece);
                totalTaux += trch.getTaux();
            }
            if (totalTaux > 100) {
                getWarningMessage("Les tranches du model de règlement sont supérieures à 100% !");
            } else if (totalTaux < 100) {
                getWarningMessage("Les tranches du model de règlement sont inférieure à 100% !");
            }
        } else {
            getErrorMessage("Le montant du document n'a pas été trouvé !");
        }
        return re;
    }

    public void generatedAllpiece() {
        if (!selectedDoc.getReglements().isEmpty()) {
            for (YvsComptaCaissePieceMission c : selectedDoc.getReglements()) {
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

    public void chooseFournisseur(Employe d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(pieceAvance.getMission().getEmploye(), d);
        }
    }

    public void chooseOneCLient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsGrhEmployes y = (YvsGrhEmployes) ev.getObject();
            chooseFournisseur(UtilGrh.buildBeanEmploye(y));
        }
    }

    public void choosePaginatorPc(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        loadAllPiece(true, true);
    }

    public void reglerPieceTresorerie(boolean avance) {
        reglerPieceTresorerie(pieceMission);
    }

    private void reglerPieceTresorerie(YvsComptaCaissePieceMission pc) {
        pc.setDatePaiement(pc.getDatePaimentPrevu());
        reglerPieceTresorerie(pc, selectedDoc, true);
    }

    public boolean reglerPieceTresorerie(YvsComptaCaissePieceMission pc, YvsGrhMissions selectedDoc, boolean msg) {
        return reglerPieceTresorerie(pc, new Mission(selectedDoc.getId()), msg);
    }

    public boolean reglerPieceTresorerie(YvsComptaCaissePieceMission pc, Mission selectedDoc, boolean msg) {
        if (pc != null) {
            if (pc.getId() != null ? pc.getId() > 0 : false) {
                if (pc.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && pc.getStatutPiece() != Constantes.STATUT_DOC_ANNULE) { //la pièce ne doit pas être annulé
                    //la pièce de caisse doit avoir une caisse
                    if (pc.getCaisse() != null ? pc.getCaisse().getId() > 0 : false) {
                        pc.setCaisse((YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{pc.getCaisse().getId()}));
                        if (controleAccesCaisse(pc.getCaisse(), true)) {
                            pc.setAuthor(currentUser);
                            if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                                if (pc.getMission().canDelete()) {
                                    if (msg) {
                                        getErrorMessage("La mission doit être validée");
                                    }
                                    return false;
                                }
                                pc.setCaissier(currentUser.getUsers());
                                if (pc.getDatePaiement() == null) {
                                    pc.setDatePaiement(pc.getDatePaimentPrevu());
                                }
                                pc.setNew_(true);
                                pc.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                            } else {
                                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                                if (dao.isComptabilise(pc.getId(), Constantes.SCR_FRAIS_MISSIONS)) {
                                    if (!autoriser("compta_od_annul_comptabilite")) {
                                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                                        return false;
                                    }
                                    if (w != null) {
                                        if (!w.unComptabiliserCaisseMission(pc, false)) {
                                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                                            return false;
                                        }
                                    }
                                }
                                pc.setDatePaiement(null);
                                pc.setNew_(true);
                                pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            }
                            dao.update(pc);
                            if (selectedDoc != null) {
                                ManagedMission service = (ManagedMission) giveManagedBean(ManagedMission.class);
                                if (service != null) {
                                    YvsGrhMissions d = (YvsGrhMissions) dao.loadOneByNameQueries("YvsMissions.findById", new String[]{"id"}, new Object[]{selectedDoc.getId()});
                                    if (d != null) {
                                        int idx = service.getListMission().indexOf(d);
                                        if (idx >= 0) {
                                            service.getListMission().set(idx, d);
                                            update("data_mission_reg");
                                        }
                                    }
                                    update("zone_form_regMission");
                                    return true;
                                }
                            }
                            if (msg) {
                                succes();
                            }
                        }
                        return true;
                    } else {
                        if (msg) {
                            getErrorMessage("Aucune caisse n'a été trouvé pour ce règlement !");
                        }
                        return false;
                    }
                }
            } else {
                if (msg) {
                    getErrorMessage("La pièce n'a pas été sauvegardé !");
                }
                return false;
            }
        } else {
            if (msg) {
                getErrorMessage("Aucune pièce n'a été trouvé !");
            }
        }
        return false;
    }

    public void openConfirmDelePC(YvsComptaCaissePieceMission pc) {
        pieceMission = pc;
        if (pc.getModel() == null) {
            pc.setModel(new YvsBaseModeReglement(0l));
        }
        if (pc.getCaisse() == null) {
            pc.setCaisse(new YvsBaseCaisse((long) 0));
        }
        openDialog("dlgConfirmDeletePC");
    }

    public void openAndVerifyConfirmPaiement(YvsComptaCaissePieceMission pc) {
        if (dao.isComptabilise(pc.getId(), Constantes.SCR_FRAIS_MISSIONS)) {
            if (pc.getModel() == null) {
                pc.setModel(modeEspece());
            }
            if (pc.getCaisse() == null) {
                pc.setCaisse(new YvsBaseCaisse((long) 0));
            }
            pieceMission = pc;
            openDialog("dlgConfirmAnnuleDoc");
            return;
        }
        openConfirmPaiement(pc);
    }

    public void openConfirmPaiement(YvsComptaCaissePieceMission pc) {
        if (pc.getModel() == null) {
            pc.setModel(modeEspece());
        }
        if (pc.getCaisse() == null) {
            pc.setCaisse(new YvsBaseCaisse((long) 0));
        }
        pieceMission = pc;
        openDialog("dlgConfirmPaye");
    }

    public void openConfirmEncaiss(YvsComptaCaissePieceMission p) {
        pieceMission = p;
        openDialog("dlgConfirmPaye");
    }

    public void openDlgToBindDv(YvsComptaCaissePieceMission pc) {
        if (pc != null) {
            if (selectedDoc != null) {
                pieceMission = pc;
                update("form_confirm_bind_pc");
                if (pc.getMission() == null) {
                    openDialog("dlgConfirmBind");
                } else {
                    openDlgToUnbindPiece(pc);
                }
            }
        }
    }

    public void openTodeleteOneLinePc(YvsComptaCaissePieceMission p) {
        loadPcOnViewPiece(p);
        openDialog("dlgConfirmDeletePC1");
    }

    public void openDlgToUnbindPiece(YvsComptaCaissePieceMission pv) {
        pieceMission = pv;
        openDialog("dlgConfirmUnBind");
        update("form_confirm_unbind_pc");
    }

    public void resetViewPc() {
        pieceAvance = new PieceTresorerie();
        pieceAvance.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
    }

    public void bindPieceAndFacture(boolean continuSave) {
        //controle        
        if (pieceMission.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
            if (pieceMission.getMission() != null ? pieceMission.getMission().getId() < 1 : true) {
                if (selectedDoc.getId() > 0) {
                    if (!continuSave) {
                        //contrôle la conformité des montants
                        if (selectedDoc.getTotalFraisMission() < pieceMission.getMontant()) {
                            //ouvre une boite de confirmation du paiement
                            openDialog("dlgConfirmContinuBind");
                            return;
                        }
                    }
                    long id = pieceMission.getId();
                    double reste = 0;
                    if (pieceMission.getMontant() <= selectedDoc.getTotalFraisMission()) {

                    } else {
                        if (selectedDoc.getTotalFraisMission() < 1) {
                            getErrorMessage("Le net à payer est déja atteint");
                            return;
                        }
                        reste = pieceMission.getMontant() - selectedDoc.getTotalFraisMission();
                        if (reste > 0) {
                            pieceMission.setMontant(arrondi(selectedDoc.getTotalFraisMission()));
                        }
                    }
                    pieceMission.setId(id);
                    pieceMission.setMission(UtilGrh.buildMission(selectedDoc, currentUser));
                    pieceMission.setAuthor(currentUser);
                    dao.update(pieceMission);
                    selectedDoc.getReglements().add(pieceMission);
                    update("table_regMission");
                    ManagedMission service = (ManagedMission) giveManagedBean(ManagedMission.class);
                    if (service != null) {
                        //equilibre les statut pour raffraichir le tableau des factures
                        dao.getEquilibreAchat(selectedDoc.getId());
                        YvsGrhMissions d = (YvsGrhMissions) dao.loadOneByNameQueries("YvsGrhMissions.findById", new String[]{"id"}, new Object[]{selectedDoc.getId()});
                        if (d != null) {
                            int idx = service.getListMission().indexOf(d);
                            if (idx >= 0) {
                                service.getListMission().set(idx, d);
                                update("data_mission_reg");
                            }
                        }
                        update("zone_form_regMission");
                    }
                    if (reste > 0) {
                        duplicatePiece(pieceMission, reste);
                        update("table_acompte_clts");
                    }
                } else {
                    getErrorMessage("Aucune facture n'a été selectionné !");
                }
            } else {
                getErrorMessage("Cette piece est rattachée à une facture");
            }
        } else {
            getErrorMessage("La pièce n'a pas encore été encaissé !");
        }
    }

    public void unbindLiasonCaisse() {
        if (pieceMission != null) {
            if (pieceMission.getMission() != null) {
                pieceMission.setMission(null);
                pieceMission.setAuthor(currentUser);
                selectedDoc.getReglements().remove(pieceMission);
                dao.update(pieceMission);
                update("table_regMission");
                ManagedMission service = (ManagedMission) giveManagedBean(ManagedMission.class);
                if (service != null) {
                    dao.getEquilibreAchat(selectedDoc.getId());
                    YvsGrhMissions d = (YvsGrhMissions) dao.loadOneByNameQueries("YvsGrhMissions.findById", new String[]{"id"}, new Object[]{selectedDoc.getId()});
                    if (d != null) {
                        int idx = service.getListMission().indexOf(d);
                        if (idx >= 0) {
                            service.getListMission().set(idx, d);
                            update("data_mission_reg");
                        }
                    }
                    update("zone_form_regMission");
                }
            } else {
                bindPieceAndFacture(true);
            }
        }
    }

    public void duplicatePiece(YvsComptaCaissePieceMission pv, double montant) {
        YvsComptaCaissePieceMission newEn = new YvsComptaCaissePieceMission(pv);
        newEn.setId(null);
        newEn.setMontant(montant);
        newEn.setMission(null);
        newEn.setNumeroPiece(newEn.getNumeroPiece() + "°");
        if (newEn.getModel() != null ? newEn.getModel().getId() < 1 : true) {
            newEn.setModel(modeEspece());
        }
        newEn = (YvsComptaCaissePieceMission) dao.save1(newEn);
        newEn.setNew_(true);
        avances.add(0, newEn);
    }

    public void cleanParametrePiece() {
        paginator.getParams().clear();
        loadAllPiece(true, true);
    }

    public void ecouteSaisieFournisseur() {
        String num = pieceAvance.getMission().getEmploye().getMatricule();
        pieceAvance.getMission().getEmploye().setId(0);
        pieceAvance.getMission().getEmploye().setError(true);
        ManagedEmployes m = (ManagedEmployes) giveManagedBean("MEmps");
        if (m != null) {
            Employe y = m.searchEmployeActif(num, true);
            if (m.getListEmployes() != null ? !m.getListEmployes().isEmpty() : false) {
                if (m.getListEmployes().size() > 1) {
                    update("data_mission_acomptes");
                } else {
                    chooseFournisseur(y);
                }
                pieceAvance.getMission().getEmploye().setError(false);
            }
        }
    }

    public void toogleSuspendPieceCaisse(YvsComptaCaissePieceMission pc) {
        if (pc != null) {
            //si c'est une suspension, on controle juste le droit
            if (pc.getId() > 0) {
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

    public double giveTotalPT(List<YvsComptaCaissePieceMission> l) {
        double sum = 0;
        for (YvsComptaCaissePieceMission p : l) {
            if (p.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && p.getStatutPiece() != Constantes.STATUT_DOC_ANNULE) {
                sum += p.getMontant();
            }
        }
        return sum;
    }

    //charge les pièces encaisser et non encore lié à une facture
    public void filterPcCanBind() {
        ParametreRequete p = new ParametreRequete("y.statutPiece", "statutPiece", Constantes.STATUT_DOC_PAYER, "=", "AND");
        paginator.addParam(p);
        paginator.addParam(new ParametreRequete("y.achat", "achat", Constantes.STATUT_DOC_PAYER, " IS NULL", "AND"));
        loadAllPiece(true, true);
        update("table_acompte_clts");
        openDialog("dlgSaveAvance");
    }

    private String findNumPiece(String num, Mission d) {
        YvsComptaCaissePieceMission pc = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findByReferencePiece", new String[]{"numeroPiece", "mission"}, new Object[]{num, new YvsGrhMissions(d.getId())});
        String suffix;
        while (pc != null) {
            String str = num.substring(("PC_M/" + d.getNumeroMission()).length(), num.length());
            try {
                int n = Integer.valueOf(str) + 1;
                if (n < 10) {
                    suffix = "0" + n;
                } else {
                    suffix = n + "";
                }
                num = "PC_M/" + d.getNumeroMission() + "/" + suffix;
                pc = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findByReferencePiece", new String[]{"numeroPiece", "mission"}, new Object[]{num, new YvsGrhMissions(d.getId())});
            } catch (NumberFormatException ex) {
                getErrorMessage("Erreur lors de la génération du numéro de la pière ");
                log.log(Level.SEVERE, null, ex);
            }
        }
        return num;
    }

    public void searchPieceByNum(String numP) {
        String val = (numP != null) ? (!numP.isEmpty() ? numP : null) : null;
        ParametreRequete p = new ParametreRequete("y.numeroPiece", "numeroPiece", val, " LIKE ", "AND");
        paginator.addParam(p);
        loadAllPiece(true, true);
        update("table_acompte_clts");
    }

    public void searchPieceByNumEmploye(String numero) {
        String val = (numero != null) ? (!numero.isEmpty() ? numero : null) : null;
        ParametreRequete p = new ParametreRequete("y.mission.employe.matricule", "code", val, " LIKE ", "AND");
        paginator.addParam(p);
        loadAllPiece(true, true);
        update("table_acompte_clts");
    }

    public void print(YvsComptaCaissePieceMission y) {
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
                executeReport("pc_mission", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedReglementMission.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printDoc(long id) {
        YvsComptaCaissePieceMission y = (YvsComptaCaissePieceMission) dao.loadOneByNameQueries("YvsComptaCaissePieceMission.findById", new String[]{"id"}, new Object[]{id});
        print(y);
        closeDialog("dlgValidOM");
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    boolean select = false;

    public void openListBon(YvsComptaCaissePieceMission y, boolean select) {
        if (y != null) {
            this.pieceMission = y;
        }
        this.select = select;
        openDialog("dlgListBons");
    }

    public void loadBonProvisoire(SelectEvent ev) {
        if (ev != null) {
            YvsComptaBonProvisoire y = (YvsComptaBonProvisoire) ev.getObject();
            if (y.canJustify()) {
                getErrorMessage("Ce bon provisoire est deja associé à une autre pièce");
                return;
            }
            if (!y.getStatutPaiement().equals(Constantes.ETAT_REGLE)) {
                getWarningMessage("Le bon provisoire selectionné n'est pas payé !");
            }
            if (!select) {
                pieceMission.setReferenceExterne(y.getNumero());
                pieceMission.setBonProvisoire(y);
                if (pieceMission.getId() < 1) {
                    pieceMission.setMontant(y.getReste());
                    pieceMission.setNumeroPiece(y.getNumero());
                    pieceMission.setModel(modeEspece());
                }
                if (pieceMission.getCaisse() != null ? pieceMission.getCaisse().getId() < 1 : true) {
                    pieceMission.setCaisse(y.getCaisse());
                }
                update("zone_pcv_edit_pieceMission");
            } else {
                if (pieceMission != null ? pieceMission.getId() < 1 : true) {
                    getErrorMessage("Vous devez selectionner une piece");
                    return;
                }
                if (pieceMission.getJustify() != null ? pieceMission.getJustify().getId() > 0 : false) {
                    getErrorMessage("Cette piece est deja rattachée à un bon provisoire");
                    return;
                }
                if (pieceMission.getMontant() != y.getMontant()) {
                    getErrorMessage("Les montants des pieces ne sont pas coherent");
                    return;
                }
                YvsComptaJustifBonMission bon = new YvsComptaJustifBonMission(pieceMission, y);
                bon.setAuthor(currentUser);
                bon = (YvsComptaJustifBonMission) dao.save1(bon);
                pieceMission.setJustify(bon);
                int idx = selectedDoc.getReglements().indexOf(pieceMission);
                if (idx > -1) {
                    selectedDoc.getReglements().set(idx, pieceMission);
                }
                update("table_regMission");
                succes();
            }
        }
    }

    public void verifyComptabilised(Boolean comptabilised) {
        ManagedMission wm = (ManagedMission) giveManagedBean(ManagedMission.class);
        if (wm != null) {
            wm.setInitForm(true);
            wm.loadAllMission(true);
            if (comptabilised != null) {
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    List<YvsGrhMissions> list = new ArrayList<>();
                    list.addAll(wm.getListMission());
                    for (YvsGrhMissions y : list) {
                        if (y.getReglements() != null ? !y.getReglements().isEmpty() : false) {
                            for (YvsComptaCaissePieceMission p : y.getReglements()) {
                                p.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_VENTE));
                                if (comptabilised ? !y.isComptabilised() : y.isComptabilised()) {
                                    wm.getListMission().remove(y);
                                }
                            }
                        } else {
                            if (comptabilised) {
                                wm.getListMission().remove(y);
                            }
                        }
                    }
                }
            }
            update("data_mission_reg");
        }
    }

    public boolean isComptabiliseBean(PieceTresorerie y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_FRAIS_MISSIONS));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsComptaCaissePieceMission y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_FRAIS_MISSIONS));
            }
            return y.getComptabilise();
        }
        return false;
    }
}
