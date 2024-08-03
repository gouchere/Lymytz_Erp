/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.caisse;

import java.io.Serializable;
import java.util.ArrayList;
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
import yvs.base.compta.ManagedCentreAnalytique;
import yvs.base.compta.UtilCompta;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.commercial.vente.ManagedVente;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.analytique.CentreCoutVirement;
import yvs.comptabilite.tresorerie.BielletagePc;
import yvs.comptabilite.tresorerie.CoutSupVirement;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaBielletage;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaCoutSupPieceVirement;
import yvs.entity.compta.YvsComptaPhasePieceVirement;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.analytique.YvsComptaCentreCoutVirement;
import yvs.entity.compta.vente.YvsComptaNotifVersementVente;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsers;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ManagedTypeCout;
import yvs.grh.bean.TypeCout;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.service.compta.doc.divers.IYvsComptaCaissePieceVirement;
import yvs.users.UtilUsers;
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
public class ManagedVirement extends Managed<PieceTresorerie, YvsComptaCaissePieceVirement> implements Serializable {

    private PieceTresorerie pieceCaisse = new PieceTresorerie();
    private List<YvsComptaCaissePieceVirement> listAllVirement;
    private YvsComptaCaissePieceVirement entity = new YvsComptaCaissePieceVirement();
    private YvsComptaPhasePieceVirement selectPhaseVirement;
    private YvsComptaPhasePieceVirement currentPhaseVirement = new YvsComptaPhasePieceVirement();
    private boolean initForm = true;
    private List<YvsBaseCaisse> caisseCible;
    private List<YvsUsers> caissiers;
    private List<String> statutsVirement;

    private CoutSupVirement cout = new CoutSupVirement();
    private YvsComptaCoutSupPieceVirement selectCout = new YvsComptaCoutSupPieceVirement();

    private CentreCoutVirement analytique = new CentreCoutVirement();
    private YvsComptaCentreCoutVirement selectAnalytique = new YvsComptaCentreCoutVirement();

    private String tabIds;

    private Boolean comptaSearch;
    private long nbrComptaSearch;

    public ManagedVirement() {
        listAllVirement = new ArrayList<>();
        caisseCible = new ArrayList<>();
        caissiers = new ArrayList<>();
        statutsVirement = new ArrayList<>();
        statutsVirement.add(Constantes.ETAT_ATTENTE);
        statutsVirement.add(Constantes.ETAT_SOUMIS);
        statutsVirement.add(Constantes.ETAT_REGLE);
    }

    public List<String> getStatutsVirement() {
        return statutsVirement;
    }

    public void setStatutsVirement(List<String> statutsVirement) {
        this.statutsVirement = statutsVirement;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public long getNbrComptaSearch() {
        return nbrComptaSearch;
    }

    public void setNbrComptaSearch(long nbrComptaSearch) {
        this.nbrComptaSearch = nbrComptaSearch;
    }

    public YvsComptaCoutSupPieceVirement getSelectCout() {
        return selectCout;
    }

    public void setSelectCout(YvsComptaCoutSupPieceVirement selectCout) {
        this.selectCout = selectCout;
    }

    public CentreCoutVirement getAnalytique() {
        return analytique;
    }

    public void setAnalytique(CentreCoutVirement analytique) {
        this.analytique = analytique;
    }

    public YvsComptaCentreCoutVirement getSelectAnalytique() {
        return selectAnalytique;
    }

    public void setSelectAnalytique(YvsComptaCentreCoutVirement selectAnalytique) {
        this.selectAnalytique = selectAnalytique;
    }

    public List<YvsUsers> getCaissiers() {
        return caissiers;
    }

    public void setCaissiers(List<YvsUsers> caissiers) {
        this.caissiers = caissiers;
    }

    public YvsComptaPhasePieceVirement getCurrentPhaseVirement() {
        return currentPhaseVirement;
    }

    public void setCurrentPhaseVirement(YvsComptaPhasePieceVirement currentPhaseVirement) {
        this.currentPhaseVirement = currentPhaseVirement;
    }

    public YvsComptaPhasePieceVirement getSelectPhaseVirement() {
        return selectPhaseVirement;
    }

    public void setSelectPhaseVirement(YvsComptaPhasePieceVirement selectPhaseVirement) {
        this.selectPhaseVirement = selectPhaseVirement;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public YvsComptaCaissePieceVirement getEntity() {
        return entity;
    }

    public void setEntity(YvsComptaCaissePieceVirement entity) {
        this.entity = entity;
    }

    public PieceTresorerie getPieceCaisse() {
        return pieceCaisse;
    }

    public void setPieceCaisse(PieceTresorerie pieceCaisse) {
        this.pieceCaisse = pieceCaisse;
    }

    public List<YvsComptaCaissePieceVirement> getListAllVirement() {
        return listAllVirement;
    }

    public void setListAllVirement(List<YvsComptaCaissePieceVirement> listAllVirement) {
        this.listAllVirement = listAllVirement;
    }

    public List<YvsBaseCaisse> getCaisseCible() {
        return caisseCible;
    }

    public void setCaisseCible(List<YvsBaseCaisse> caisseCible) {
        this.caisseCible = caisseCible;
    }

    public CoutSupVirement getCout() {
        return cout;
    }

    public void setCout(CoutSupVirement cout) {
        this.cout = cout;
    }

    @Override
    public boolean controleFiche(PieceTresorerie bean) {
        if (bean.getCaisse() != null ? bean.getCaisse().getId() <= 0 : true) {
            getErrorMessage("Vous devez selectionner la caisse source !");
            return false;
        }
        if (bean.getOtherCaisse() != null ? bean.getOtherCaisse().getId() <= 0 : true) {
            getErrorMessage("Vous devez selectionner la caisse cible !");
            return false;
        }
        if (bean.getCaissier() != null ? bean.getCaissier().getId() <= 0 : true) {
            getErrorMessage("Vous devez spécifier le caissier !");
            return false;
        }
        if (bean.getMode() != null ? bean.getMode().getId() <= 0 : true) {
            getErrorMessage("Vous devez selectionner le mode de reglement !");
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Le montant du virement est incorrecte !");
            return false;
        }
        if (bean.getSource().equals(bean.getOtherCaisse())) {
            getErrorMessage("Redondance cyclique de caisses !");
            return false;
        }
        if (bean.getOtherCaisse().isGiveBilletage()) {
            double piece = soeBielletage(bean.getBielletagePiece());
            double billet = soeBielletage(bean.getBielletageBillet());
            if (piece + billet <= 0) {
                getErrorMessage("Vous devez specifier le billetage");
                return false;
            } else {
                if (bean.getMontant() != (billet + piece)) {
                    getErrorMessage("Le billetage n'est pas équilibré au montant du virement");
                    return false;
                }
            }
        }
        if ((entity != null ? entity.getId() > 0 ? !entity.getDatePaimentPrevu().equals(bean.getDatePaiementPrevu()) : false : false)
                || (bean.getNumPiece() == null || bean.getNumPiece().trim().length() < 1)) {
            String ref = genererReference(Constantes.TYPE_PT_NAME, bean.getDatePaiementPrevu());
            if ((ref != null) ? ref.trim().equals("") : true) {
                return false;
            }
            bean.setNumPiece(ref);
        }
        if (!controleEcartVente(bean.getCaissier().getId(), bean.getDatePaiementPrevu(), true)) {
            return false;
        }
        return true;
    }

    private boolean controleFiche(CoutSupVirement bean) {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            if (w.isComptabilise(pieceCaisse.getId(), Constantes.SCR_VIREMENT)) {
                getErrorMessage("Cette pièce est déjà comptabilisée");
                return false;
            }
        }
        if (bean == null) {
            return false;
        }
        if (bean.getVirement() != null ? bean.getVirement().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le virement");
            return false;
        }
        if (bean.getType() != null ? bean.getType().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le type de coût");
            return false;
        }
        return true;
    }

    public boolean controleFiche(CentreCoutVirement bean) {
        if (bean.getCout() != null ? bean.getCout().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez renseigner le cout");
            return false;
        }
        if (bean.getCentre() != null ? bean.getCentre().getId() < 1 : true) {
            getErrorMessage("Formulaire invalide", "Vous devez selectionner le centre analytique");
            return false;
        }
        double taux = bean.getTaux();
        for (YvsComptaCentreCoutVirement a : selectCout.getAnalytiques()) {
            if (!a.getId().equals(bean.getId()) ? a.getCentre().getId().equals(bean.getCentre().getId()) : false) {
                getErrorMessage("Formulaire invalide", "Vous avez deja associé ce centre analytique");
                return false;
            }
            if (!a.getId().equals(bean.getId())) {
                taux += a.getCoefficient();
            }
        }
        if (taux > 100) {
            getErrorMessage("Formulaire invalide", "La repartition analytique ne peut pas etre supérieur à 100%");
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Integer> ids = decomposeSelection(tabIds);
                for (Integer index : ids) {
                    YvsComptaCaissePieceVirement bean = listAllVirement.get(index);
                    if (bean.getStatutPiece() != Constantes.STATUT_DOC_ANNULE && bean.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && bean.getStatutPiece() != Constantes.STATUT_DOC_ATTENTE) {
                        getErrorMessage("Le statut de cette pièce n'autorise pas la modification !");
                        continue;
                    }
                    dao.delete(bean);
                    listAllVirement.remove(bean);
                    if (bean.getId().equals(pieceCaisse.getId())) {
                        resetFiche();
                        update("form_edit_pieceCaiss_virement");
                    }
                }
                succes();
                update("table_all_pieceCaisse_virement");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteOneBean() {
        if (pieceCaisse.getId() > 0) {
            try {
                if (pieceCaisse.getStatutPiece() != Constantes.STATUT_DOC_ANNULE && pieceCaisse.getStatutPiece() != Constantes.STATUT_DOC_SUSPENDU && pieceCaisse.getStatutPiece() != Constantes.STATUT_DOC_ATTENTE) {
                    getErrorMessage("Le statut de cette pièce n'autorise pas la modification !");
                    return;
                }
                //vérification par prudence
//                Character source = (pc.getStatutPiece().equals(Constantes.STATUT_DOC_ATTENTE) || pc.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS)) ? yvs.dao.salaire.service.Constantes.MOUV_CAISS_SORTIE.charAt(0) : yvs.dao.salaire.service.Constantes.MOUV_CAISS_ENTREE.charAt(0);
//                if (dao.isComptabilise(pc.getId(), Constantes.SCR_VIREMENT, true, source)) {
//                    if (!autoriser("compta_od_annul_comptabilite")) {
//                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
//                        return ;
//                    }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    if (!w.unComptabiliserCaisseVirement(new YvsComptaCaissePieceVirement(pieceCaisse.getId()), false)) {
                        getErrorMessage("Suppression de la pièce comptable Impossible!!!");
                        return;
                    }
                }
//                }
                YvsComptaCaissePieceVirement v = new YvsComptaCaissePieceVirement(pieceCaisse.getId());
                v.setAuthor(currentUser);
                v.setDateUpdate(new Date());
                dao.delete(v);
                if (listAllVirement.contains(v)) {
                    listAllVirement.remove(v);
                }
            } catch (Exception ex) {
                getErrorMessage("Impossible de supprimer cette opération !");
            }
        }
    }

    public void deleteBeanCout(YvsComptaCoutSupPieceVirement y) {
        if (y != null) {
            if (pieceCaisse.getStatutPiece() == Constantes.STATUT_DOC_PAYER) {
                getErrorMessage("Ce cout est déjà payé");
                return;
            }
            dao.delete(y);
            pieceCaisse.getCouts().remove(y);
            int idx1 = listAllVirement.indexOf(entity);
            if (idx1 >= 0) {
                listAllVirement.get(idx1).getCouts().remove(y);
            }
            if (cout.getId() == y.getId()) {
                cout = new CoutSupVirement();
                update("form_cout_virement");
            }
            update("txt_couts_virement");
            update("txt_total_virement");
            update("date_cout_virement");
            succes();
        }
    }

    public void deleteAnalytique(YvsComptaCentreCoutVirement y, boolean delete) {
        try {
            if (y != null) {
                if (!delete) {
                    selectAnalytique = y;
                    openDialog("dlgConfirmDeleteAnalytique");
                    return;
                }
                dao.delete(y);
                selectCout.getAnalytiques().remove(y);
                if (analytique.getId() == y.getId()) {
                    resetFicheAnalytique();
                }
                update("data-analytique_virement");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedFactureAchat (deleteAnalytique)", ex);
        }
    }

    public void changeStatutPiece() {
        if (pieceCaisse.getId() > 0) {
//            YvsComptaCaissePieceVirement entity = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{pieceCaisse.getId()});
            if (entity != null) {
                changeStatutPiece(entity, true);
            }
        }
    }

    public boolean changeStatutPiece(YvsComptaCaissePieceVirement pc, boolean msg) {
        if (pc != null ? pc.getId() > 0 : false) {
            if (!verifyCancelPieceCaisse(pc.getDatePaiement())) {
                return false;
            }
            char statut = (pc.getStatutPiece() == Constantes.STATUT_DOC_ATTENTE) ? Constantes.STATUT_DOC_ANNULE : Constantes.STATUT_DOC_ATTENTE;
            Character source = (pc.getStatutPiece().equals(Constantes.STATUT_DOC_ATTENTE) || pc.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS)) ? yvs.dao.salaire.service.Constantes.MOUV_CAISS_SORTIE.charAt(0) : yvs.dao.salaire.service.Constantes.MOUV_CAISS_ENTREE.charAt(0);
            if (dao.isComptabilise(pc.getId(), Constantes.SCR_VIREMENT, true, source)) {
                if (!autoriser("compta_od_annul_comptabilite")) {
                    getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                    return false;
                }
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    if (!w.unComptabiliserCaisseVirement(pc, false)) {
                        getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                        return false;
                    }
                }
            }
            pc.setStatutPiece(statut);
            pc.setAuthor(currentUser);
            pc.setDateUpdate(new Date());
            pc.setDatePaiement(null);
            dao.update(pc);
            pc.setNew_(true);
            int idx = listAllVirement.indexOf(pc);
            if (idx >= 0) {
                listAllVirement.set(idx, pc);
            } else {
                listAllVirement.add(0, pc);
            }
            if (pieceCaisse != null) {
                pieceCaisse.setStatutPiece(statut);
                pieceCaisse.setStatutExterne(statut + "");
            }
            update("zone_txt_resteDD");
            update("table_all_pieceCaisse_virement");
            if (msg) {
                succes();
            }
            return true;
        } else {
            getErrorMessage("Aucune pièce n'a été selectionné !");
        }
        return false;
    }

    public void openDlgToCancel() {
        pc = entity;
        Character source = (pc.getStatutPiece().equals(Constantes.STATUT_DOC_ATTENTE) || pc.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS)) ? yvs.dao.salaire.service.Constantes.MOUV_CAISS_SORTIE.charAt(0) : yvs.dao.salaire.service.Constantes.MOUV_CAISS_ENTREE.charAt(0);
        if (dao.isComptabilise(pc.getId(), Constantes.SCR_VIREMENT, true, source)) {
            openDialog("dlgConfirmAnnulePiece");
            return;
        }
        if (!controleEcartVente(entity.getCaissierSource().getId(), entity.getDatePaiement(), true)) {
            return;
        }
        openDialog("dlgCancelPiece");
    }

    YvsComptaCaissePieceVirement pc;

    public void openConfirmrPaiement() {
        openDlgToCancel(pc);
    }

    public void openConfirmrPaiement(YvsComptaCaissePieceVirement y) {
        pc = y;
        populateView(UtilCompta.buildBeanTresoreri(y));
        Character source = (y.getStatutPiece().equals(Constantes.STATUT_DOC_ATTENTE) || y.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS)) ? yvs.dao.salaire.service.Constantes.MOUV_CAISS_SORTIE.charAt(0) : yvs.dao.salaire.service.Constantes.MOUV_CAISS_ENTREE.charAt(0);
        if (dao.isComptabilise(y.getId(), Constantes.SCR_VIREMENT, true, source)) {
            openDialog("dlgConfirmAnnulePiece");
            return;
        }
        openDlgToCancel(y);
    }

    public void openDlgToCancel(YvsComptaCaissePieceVirement p) {
        Character source = (p.getStatutPiece().equals(Constantes.STATUT_DOC_ATTENTE) || p.getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS)) ? yvs.dao.salaire.service.Constantes.MOUV_CAISS_SORTIE.charAt(0) : yvs.dao.salaire.service.Constantes.MOUV_CAISS_ENTREE.charAt(0);
        if (dao.isComptabilise(p.getId(), Constantes.SCR_VIREMENT, true, source)) {
            if (!autoriser("compta_od_annul_comptabilite")) {
                getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                return;
            }
        }
        openDialog("dlgCancelPiece");
    }

    public void openDlgToDelete(YvsComptaCaissePieceVirement p) {
        if (!controleEcartVente(p.getCaissierSource().getId(), p.getDatePaiement(), true)) {
            return;
        }
        populateView(UtilCompta.buildBeanTresoreri(p));
        openDialog("dlgDelPiece");
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PieceTresorerie recopieView() {
        return null;
    }

    @Override
    public void populateView(PieceTresorerie bean) {
        cloneObject(pieceCaisse, bean);
        if (pieceCaisse != null ? pieceCaisse.getMode() != null ? pieceCaisse.getMode().getId() < 1 : true : false) {
            pieceCaisse.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        }
        update("zone_solde_caiss_source");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            YvsComptaCaissePieceVirement vir = (YvsComptaCaissePieceVirement) ev.getObject();
            onSelectObject(vir);
            tabIds = listAllVirement.indexOf(vir) + "";
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadOnViewCout(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaCoutSupPieceVirement y = (YvsComptaCoutSupPieceVirement) ev.getObject();
            cout = UtilCompta.buildBeanCoutSupVirement(y);
        }
    }

    public void unLoadOnViewCout(UnselectEvent ev) {
        cout = new CoutSupVirement();
    }

    public void loadOnViewHeader(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComEnteteDocVente y = (YvsComEnteteDocVente) ev.getObject();
            pieceCaisse.setEnteteDoc(UtilCom.buildSimpleBeanEnteteDocVente(y));
            update("txt-header_virement_caisse");
        }
    }

    public void loadOnViewAnalytique(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            selectAnalytique = (YvsComptaCentreCoutVirement) ev.getObject();
            analytique = UtilCompta.buildBeanCentreVirement(selectAnalytique);
            update("form-analytique_virement");
        }
    }

    public void unLoadOnViewAnalytique(UnselectEvent ev) {
        resetFicheAnalytique();
    }

    public void initHeaders() {
        if (pieceCaisse.getCaissier() != null ? pieceCaisse.getCaissier().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner le caissier");
            return;
        }
        ManagedVente w = (ManagedVente) giveManagedBean(ManagedVente.class);
        if (w != null) {
            w.setCodeVendeur_(pieceCaisse.getCaissier().getCodeUsers());
            w.addParamVendeur();
            update("data-header_virement_caisse");
        }
        openDialog("dlgListHeaders");
    }

    private YvsBaseCaisse giveCaisse(YvsBaseCaisse c) {
//        if (pieceCaisse.getCaisse().getId() > 0) {
        if (c != null ? c.getId() > 0 : false) {
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (service != null) {
                int idx = service.getCaisses().indexOf(c);
                if (idx >= 0) {
                    return service.getCaisses().get(idx);
                } else {
                    return service.findOneCaisse(c.getId());
                }
            }
        }
        return null;
    }

    private void loadCaissiers(YvsBaseCaisse y) {
        caissiers.clear();
        if (y != null ? y.getId() > 0 : false) {
            caissiers = dao.loadNameQueries("YvsBaseUsersAcces.findUsersByCode", new String[]{"code"}, new Object[]{y.getCodeAcces()});
            if (caissiers == null) {
                caissiers = new ArrayList<>();
            }
            if (y.getResponsable() != null ? y.getResponsable().getCodeUsers() != null : false) {
                if (!caissiers.contains(y.getResponsable().getCodeUsers())) {
                    caissiers.add(y.getResponsable().getCodeUsers());
                }
            }
            if (y.getCaissier() != null) {
                if (!caissiers.contains(y.getCaissier())) {
                    caissiers.add(y.getCaissier());
                }
            }
            if (caissiers.contains(currentUser.getUsers())) {
                pieceCaisse.setCaissier(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
            }
            if (pieceCaisse.getCaissier() != null ? pieceCaisse.getCaissier().getId() < 1 : true) {
                pieceCaisse.setCaissier(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
            }
            if ((pieceCaisse.getCaissier() != null ? pieceCaisse.getCaissier().getId() < 1 : true) && caissiers.contains(currentUser.getUsers())) {
                pieceCaisse.setCaissier(UtilUsers.buildSimpleBeanUsers(currentUser.getUsers()));
            }
        }
        update("chmp_caissier_source");
    }

    public void buildCaisses(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            // trouve les caisses parent d'une caisse données
            ManagedCaisses service = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            long id = (long) ev.getNewValue();
            caisseCible.clear();
            caissiers.clear();
            if (service != null && id > 0) {
                int idx = service.getCaisses().indexOf(new YvsBaseCaisse((long) ev.getNewValue()));
                if (idx >= 0) {
                    YvsBaseCaisse y = service.getCaisses().get(idx);
                    pieceCaisse.setCaisse(UtilCompta.buildBeanCaisse(y));
                    List<YvsBaseCaisse> list = dao.loadNameQueries("YvsBaseLiaisonCaisse.findCaisseCibleByCaisse", new String[]{"caisseSource"}, new Object[]{y});
                    if (list != null ? !list.isEmpty() : false) {
                        caisseCible.addAll(list);
                    }
                    pieceCaisse.getCaisse().setSolde(y.getSoldeCaisse(dao));
                    loadCaissiers(y);
                    update("zone_solde_caiss_source");
                }
            }
        }
    }

    public void chooseCible(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                int idx = caisseCible.indexOf(new YvsBaseCaisse((long) ev.getNewValue()));
                if (idx >= 0) {
                    YvsBaseCaisse y = caisseCible.get(idx);
                    pieceCaisse.setOtherCaisse(UtilCompta.buildBeanCaisse(y));
                    if (y.getResponsable() != null ? y.getResponsable().getCodeUsers() != null : false) {
                        pieceCaisse.setCaissierCible(UtilUsers.buildSimpleBeanUsers(y.getResponsable().getCodeUsers()));
                    }
                    if (y.getCaissier() != null ? y.getCaissier().getId() > 0 : false) {
                        pieceCaisse.setCaissierCible(UtilUsers.buildSimpleBeanUsers(y.getCaissier()));
                    }
                }
            }
        }
    }

    public void chooseCaissier(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                int idx = caissiers.indexOf(new YvsUsers((long) ev.getNewValue()));
                if (idx >= 0) {
                    pieceCaisse.setCaissier(UtilUsers.buildSimpleBeanUsers(caissiers.get(idx)));
                }
            }
        }
    }

    public void chooseCentre() {
        if (analytique.getCentre() != null ? analytique.getCentre().getId() > 0 : false) {
            ManagedCentreAnalytique service = (ManagedCentreAnalytique) giveManagedBean(ManagedCentreAnalytique.class);
            if (service != null) {
                int idx = service.getCentres().indexOf(new YvsComptaCentreAnalytique(analytique.getCentre().getId()));
                if (idx > -1) {
                    YvsComptaCentreAnalytique y = service.getCentres().get(idx);
                    analytique.setCentre(UtilCompta.buildBeanCentreAnalytique(y));
                }
            }
        }
    }

    @Override
    public boolean saveNew() {
        YvsComptaCaissePieceVirement y = saveNew(pieceCaisse);
        if (y != null ? y.getId() > 0 : false) {
            entity = y;
            actionOpenOrResetAfter(this);
            succes();
            return true;
        }
        return false;
    }

    public YvsComptaCaissePieceVirement saveNew(PieceTresorerie bean) {
        if (controleFiche(bean)) {
            YvsBaseCaisse source = giveCaisse(new YvsBaseCaisse(bean.getCaisse().getId()));
            YvsBaseCaisse cible = giveCaisse(new YvsBaseCaisse(bean.getOtherCaisse().getId()));
            if (source != null && cible != null) {
                if (!controleAccesCaisse(source, true)) {
                    return null;
                }
                YvsComptaCaissePieceVirement entity = UtilCompta.buildTresoreri(bean, source, cible, currentUser);
                IYvsComptaCaissePieceVirement service = (IYvsComptaCaissePieceVirement) IEntitiSax.createInstance("IYvsComptaCaissePieceVirement", dao);
                System.err.println("service : " + service);
                if (service != null) {
                    ResultatAction<YvsComptaCaissePieceVirement> result;
                    if (bean.getId() <= 0) {
                        result = service.save(entity);
                    } else {
                        result = service.update(entity);
                    }
                    if (result != null ? result.isResult() : false) {
                        entity = (YvsComptaCaissePieceVirement) result.getData();
                    } else {
                        getErrorMessage(result != null ? result.getMessage() : "Action impossible!!!");
                    }
                    bean.setId(entity.getId());
                }
                //Enregistre les phases si le mode de paiements un mode banque

                //enregistre le bielletage
                saveBielletage(entity);
                return entity;
            }
        }
        return null;
    }

    public void saveNewCout() {
        String action = cout.getId() > 0 ? "Modification" : "insertion";
        try {
            cout.setVirement(pieceCaisse);
            if (controleFiche(cout)) {
                YvsComptaCoutSupPieceVirement y = UtilCompta.buildCoutSupVirement(cout, currentUser);
                if (cout.getId() < 1) {
                    y.setId(null);
                    y = (YvsComptaCoutSupPieceVirement) dao.save1(y);
                } else {
                    dao.update(y);
                }
                int idx = pieceCaisse.getCouts().indexOf(y);
                if (idx > -1) {
                    pieceCaisse.getCouts().set(idx, y);
                } else {
                    pieceCaisse.getCouts().add(y);
                }
                idx = entity.getCouts().indexOf(y);
                if (idx > -1) {
                    entity.getCouts().set(idx, y);
                } else {
                    entity.getCouts().add(y);
                }
                idx = listAllVirement.indexOf(entity);
                if (idx >= 0) {
                    listAllVirement.set(idx, entity);
                }
                cout = new CoutSupVirement();
                succes();
                update("txt_couts_virement");
                update("txt_total_virement");
                update("date_cout_virement");
                update("form_cout_virement");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException("Error " + action, ex);
        }
    }

    public void saveNewAnalytique() {
        try {
            analytique.setCout(new CoutSupVirement(selectCout != null ? selectCout.getId() : 0));
            if (controleFiche(analytique)) {
                selectAnalytique = UtilCompta.buildCentreVirement(analytique, currentUser);
                if (analytique.getId() < 1) {
                    selectAnalytique = (YvsComptaCentreCoutVirement) dao.save1(selectAnalytique);
                } else {
                    dao.update(selectAnalytique);
                }
                int idx = selectCout.getAnalytiques().indexOf(selectAnalytique);
                if (idx < 0) {
                    selectCout.getAnalytiques().add(0, selectAnalytique);
                } else {
                    selectCout.getAnalytiques().set(idx, selectAnalytique);
                }
                resetFicheAnalytique();
                succes();
                update("data-analytique_virement");
            }
        } catch (Exception ex) {
            getErrorMessage("Action Impossible");
            getException("ManagedArticles (saveNewAnalytique)", ex);
        }
    }

    public void chooseCaissePhaseVirement() {
        if (currentPhaseVirement.getCaisse() != null ? currentPhaseVirement.getCaisse().getId() > 0 : false) {
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (w != null) {
                int idx = w.getCaisses().indexOf(currentPhaseVirement.getCaisse());
                if (idx > -1) {
                    currentPhaseVirement.setCaisse(new YvsBaseCaisse(w.getCaisses().get(idx)));
                }
            }
        }
    }

    public void chooseTypeCout(ValueChangeEvent ev) {
        if (ev != null ? ev.getNewValue() != null : false) {
            long id = (long) ev.getNewValue();
            ManagedTypeCout w = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
            if (w != null) {
                if (id > 0) {
                    cout.getType().setId(id);
                    int idx = w.getTypes().indexOf(new YvsGrhTypeCout(cout.getType().getId()));
                    if (idx > -1) {
                        cout.setType(UtilGrh.buildBeanTypeCout(w.getTypes().get(idx)));
                    }
                } else {
                    if (id == -1) {
                        w.setTypeCout(new TypeCout(Constantes.COUT_VIREMENT));
                        openDialog("dlgAddTypeCout");
                    }
                    cout.getType().setId(0);
                }
            } else {
                cout.getType().setId(id);
            }
        }
    }

    @Override
    public void onSelectDistant(YvsComptaCaissePieceVirement y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Virements caisses", "modCompta", "smenRegVirement", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsComptaCaissePieceVirement y) {
        entity = y;
        entity.setPhasesReglement(ordonnePhase(entity.getPhasesReglement()));
        if (entity.getPhasesReglement() != null ? !entity.getPhasesReglement().isEmpty() : false) {
            for (YvsComptaPhasePieceVirement r : entity.getPhasesReglement()) {
                if (r.isEtapeActive()) {
                    currentPhaseVirement = r;
                    break;
                }
            }
        }
        if (currentPhaseVirement.getCaisse() == null) {
            currentPhaseVirement.setCaisse(new YvsBaseCaisse(y.getCible()));
        }
        currentPhaseVirement.setDateValider(new Date());
        PieceTresorerie pt = UtilCompta.buildBeanTresoreri(y);
        pt.getCaisse().setSolde(y.getSource().getSoldeCaisse(dao));
        if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
            pt.setDatePaiement(y.getDatePaimentPrevu());
        }
        populateView(pt);
        ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
        if (w != null) {
            int idx = w.getCaisses().indexOf(y.getSource());
            if (idx < 0) {
                w.getCaisses().add(0, y.getSource());
            }
        }
        List<YvsBaseCaisse> list = dao.loadNameQueries("YvsBaseLiaisonCaisse.findCaisseCibleByCaisse", new String[]{"caisseSource"}, new Object[]{y.getSource()});
        if (list != null ? !list.isEmpty() : false) {
            caisseCible.clear();
            caisseCible.addAll(list);
        }
        int idx = caisseCible.indexOf(y.getCible());
        if (idx < 0) {
            caisseCible.add(0, y.getCible());
        }
        loadCaissiers(y.getSource());
        update("main_form_virement_caiss");
    }

    public void saveBielletage(PieceTresorerie piece) {
        try {
            YvsComptaCaissePieceVirement pv = new YvsComptaCaissePieceVirement(piece.getIdExterne());
            for (BielletagePc b : piece.getBielletageBillet()) {
                if (b.getId() > 0) {
                    if (b.getQuantite() <= 0) {
                        dao.delete(new YvsComptaBielletage(b.getId()));
                    } else {
                        YvsComptaBielletage enB = buildBielletage(b, pv);
                        dao.update(enB);
                    }
                } else {
                    if (b.getQuantite() > 0) {
                        YvsComptaBielletage enB = buildBielletage(b, pv);
                        enB.setId(null);
                        enB.setBillet(true);
                        enB = (YvsComptaBielletage) dao.save1(enB);
                        b.setId(enB.getId());
                    }
                }
            }
            for (BielletagePc b : piece.getBielletagePiece()) {
                if (b.getId() > 0) {
                    if (b.getQuantite() <= 0) {
                        dao.delete(new YvsComptaBielletage(b.getId()));
                    } else {
                        YvsComptaBielletage enB = buildBielletage(b, pv);
                        dao.update(enB);
                    }
                } else {
                    if (b.getQuantite() > 0) {
                        YvsComptaBielletage enB = buildBielletage(b, pv);
                        enB.setId(null);
                        enB = (YvsComptaBielletage) dao.save1(enB);
                        b.setId(enB.getId());
                    }

                }
            }
            succes();
        } catch (Exception ex) {
            getErrorMessage("Action impossible!");
        }
    }

    private void saveBielletage(YvsComptaCaissePieceVirement pv) {
        if (pv != null ? pv.getId() < 1 : true) {
            return;
        }
        for (BielletagePc b : pieceCaisse.getBielletageBillet()) {
            if (b.getId() > 0) {
                if (b.getQuantite() <= 0) {
                    dao.delete(new YvsComptaBielletage(b.getId()));
                    pv.getBielletages().remove(new YvsComptaBielletage(b.getId()));
                } else {
                    YvsComptaBielletage enB = buildBielletage(b, pv);
                    dao.update(enB);
                    int idx = pv.getBielletages().indexOf(enB);
                    if (idx > -1) {
                        pv.getBielletages().set(idx, enB);
                    }
                }
            } else {
                if (b.getQuantite() > 0) {
                    YvsComptaBielletage enB = buildBielletage(b, pv);
                    enB.setId(null);
                    enB.setBillet(true);
                    enB = (YvsComptaBielletage) dao.save1(enB);
                    b.setId(enB.getId());
                    pv.getBielletages().add(0, enB);
                }
            }
        }
        for (BielletagePc b : pieceCaisse.getBielletagePiece()) {
            if (b.getId() > 0) {
                if (b.getQuantite() <= 0) {
                    dao.delete(new YvsComptaBielletage(b.getId()));
                    pv.getBielletages().remove(new YvsComptaBielletage(b.getId()));
                } else {
                    YvsComptaBielletage enB = buildBielletage(b, pv);
                    dao.update(enB);
                    int idx = pv.getBielletages().indexOf(enB);
                    if (idx > -1) {
                        pv.getBielletages().set(idx, enB);
                    }
                }
            } else {
                if (b.getQuantite() > 0) {
                    YvsComptaBielletage enB = buildBielletage(b, pv);
                    enB.setId(null);
                    enB = (YvsComptaBielletage) dao.save1(enB);
                    b.setId(enB.getId());
                    pv.getBielletages().add(0, enB);
                }

            }
        }
        update("zone_bielletage");
    }

    private boolean saveVersement(PieceTresorerie bean, YvsComptaCaissePieceVirement entity, boolean msg) {
        if (bean != null ? bean.getId() > 0 : false) {
            if (bean.getEnteteDoc() != null ? bean.getEnteteDoc().getId() > 0 : false) {
                YvsComptaNotifVersementVente y = (YvsComptaNotifVersementVente) dao.loadOneByNameQueries("YvsComptaNotifVersementVente.findOne", new String[]{"piece", "enteteDoc"}, new Object[]{new YvsComptaCaissePieceVirement(bean.getId()), new YvsComEnteteDocVente(bean.getEnteteDoc().getId())});
                if (y != null ? y.getId() > 0 : false) {
                    if (msg) {
                        getErrorMessage("Vous avez deja rattaché ce virement a ce journal de vente");
                    }
                    return false;
                }
                y = new YvsComptaNotifVersementVente(new YvsComEnteteDocVente(bean.getEnteteDoc().getId()), new YvsComptaCaissePieceVirement(bean.getId()), currentUser);
                dao.save(y);
                entity.setVersement(y);
                int idx = listAllVirement.indexOf(entity);
                if (idx > -1) {
                    listAllVirement.set(idx, entity);
                } else {
                    listAllVirement.add(entity);
                }
                return true;
            }
        }
        return false;
    }

    public void removeVersement(YvsComptaCaissePieceVirement y) {
        if (y != null ? y.getId() > 0 : false) {
            entity = y;
        }
    }

    public void removeVersement() {
        if (entity != null ? entity.getId() > 0 : false) {
            dao.delete(entity.getVersement());
            entity.setVersement(null);
            int idx = listAllVirement.indexOf(entity);
            if (idx > -1) {
                listAllVirement.set(idx, entity);
            } else {
                listAllVirement.add(entity);
            }
            succes();
        }
    }

    public void print(YvsComptaCaissePieceVirement y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getStatutPiece() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getMontant() + y.getMontantCout()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                param.put("LOGO", returnLogo());
                executeReport("pc_virement", param);
            }
        } catch (Exception ex) {
            getException("ManagedVirement (print)", ex);
        }
    }

    public double soeBielletage(List<BielletagePc> l) {
        double re = 0;
        for (BielletagePc b : l) {
            re += (b.getQuantite() * b.getValeur());
        }
        return re;
    }

    private YvsComptaBielletage buildBielletage(BielletagePc b, YvsComptaCaissePieceVirement pv) {
        YvsComptaBielletage re = new YvsComptaBielletage(b.getId());
        re.setFormatMonai(b.getFormatMonai());
        re.setPieceVirement(pv);
        re.setQuantite(b.getQuantite());
        re.setValeur(b.getValeur());
        re.setBillet(false);
        if (b.getId() > 0) {
            re.setDateSave(new Date());
        }
        re.setDateUpdate(new Date());
        re.setAuthor(currentUser);
        return re;
    }

    @Override
    public void resetFiche() {
        pieceCaisse = new PieceTresorerie();
        pieceCaisse.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        cout = new CoutSupVirement();
        resetFicheAnalytique();
        caissiers.clear();
        caisseCible.clear();
        update("form_cout_virement");
        update("date_cout_virement");
        update("txt_couts_virement");
    }

    public void resetFicheAnalytique() {
        analytique = new CentreCoutVirement();
        selectAnalytique = new YvsComptaCentreCoutVirement();
        update("form-analytique_virement");
    }

    @Override
    public void loadAll() {
        //charge tous les virements
        loadAllVirement(true);
        if (pieceCaisse != null ? pieceCaisse.getMode() != null ? pieceCaisse.getMode().getId() < 1 : true : false) {
            pieceCaisse.setMode(UtilCompta.buildBeanModeReglement(modeEspece()));
        }
    }

    public void clearParams() {
        paginator.getParams().clear();
        paramDate = false;
        statut_ = null;
        forVersement = null;
        caisseSF = 0;
        idsSearch = "";
        caisseDF = 0;
        montantMinF = 0;
        montantMaxF = 0;
        caissierF = "";
        numPieceF = "";
        loadAllVirement(true);
    }

    public void loadAllVirement(boolean avancer) {
        if (!autoriser("compta_virement_view_historique")) {
            Date dateDebut = new Date();
            Date dateFin = new Date();
            ParametreRequete p = new ParametreRequete(null, "date", dateDebut, dateFin, "BETWEEN", "AND");
            ParametreRequete p1 = new ParametreRequete(null, "date", dateDebut, dateFin, "BETWEEN", "OR");
            p1.getOtherExpression().add(new ParametreRequete("y.datePaiement", "date", dateDebut, dateFin, "BETWEEN", "AND"));
            p1.getOtherExpression().add(new ParametreRequete("y.statutPiece", "statutPiece", Constantes.STATUT_DOC_PAYER, "=", "AND"));
            p.getOtherExpression().add(p1);
            p.getOtherExpression().add(new ParametreRequete("y.statutPiece", "statutPiece", Constantes.STATUT_DOC_PAYER, "!=", "AND"));
            paginator.addParam(p);
        }
        if (!autoriser("compta_virement_view_all_users")) {
            // ajoute le paramètre de restriction utilisateur  et dépôts                   
            ParametreRequete p = new ParametreRequete(null, "caissier", currentUser.getUsers(), "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.caissierSource", "caissier", currentUser.getUsers(), "=", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.caissierCible", "caissier", currentUser.getUsers(), "=", "OR"));
            paginator.addParam(p);
        }
        if (!autoriser("compta_virement_view_all_caisse")) {
            // ajoute le paramètre de restriction aux dépôts où l'utilisateur est planifié ou bien là où il est responsable    
            List<Long> ids = new ArrayList<>();
            ManagedCaisses w = (ManagedCaisses) giveManagedBean(ManagedCaisses.class);
            if (w != null) {
                ids = w.loadIdCaisses(currentUser.getUsers());
            }
            if (ids != null ? ids.isEmpty() : true) {
                ids.add(-1L);
            }
            ParametreRequete p = new ParametreRequete(null, "caisses", currentUser.getUsers(), "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.source.id", "caisses", ids, "IN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.cible.id", "caisses", ids, "IN", "OR"));
            paginator.addParam(p);
        } else {
            if (listIdAgences != null ? listIdAgences.isEmpty() : true) {
                listIdAgences.add(currentAgence.getId());
            }
            ParametreRequete p = new ParametreRequete(null, "caissier", currentUser.getUsers(), "=", "AND");
            p.getOtherExpression().add(new ParametreRequete("y.source.journal.agence", "agences", listIdAgences, "IN", "OR"));
            p.getOtherExpression().add(new ParametreRequete("y.cible.journal.agence", "agences", listIdAgences, "IN", "OR"));
            paginator.addParam(p);
        }
        paginator.addParam(new ParametreRequete("y.source.journal.agence.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND"));
        listAllVirement = paginator.executeDynamicQuery("YvsComptaCaissePieceVirement", "y.datePaimentPrevu DESC", avancer, initForm, (int) imax, dao);
        if (listAllVirement.size() == 1) {
            onSelectObject(listAllVirement.get(0));
            update("form_edit_pieceCaiss_virement");
            update("zone_bielletage");
            execute("collapseForm('pieceCaissVir')");
        } else {
            execute("collapseList('pieceCaissVir')");
        }
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComptaCaissePieceVirement> re = paginator.parcoursDynamicData("YvsComptaCaissePieceVirement", "y", "y.datePaimentPrevu DESC, y.numeroPiece DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadPieceByStatut(String statut) {
        statut_ = statut;
        addParamStatut();
    }

    public void loadPieceByStatutAndComptabilise(String statut, Boolean b) {
        statut_ = statut;
        addParamStatut();
        addParamComptabilise(b);
    }

    public void loadSections(YvsComptaCoutSupPieceVirement y) {
        selectCout = y;
        selectCout.setAnalytiques(dao.loadNameQueries("YvsComptaCentreCoutVirement.findByCout", new String[]{"cout"}, new Object[]{y}));
        update("blog-analytique_virement");
    }

    public void addParamStatut() {
        ParametreRequete p = new ParametreRequete("y.statutPiece", "statut", null, "=", "AND");
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.statutPiece", "statut", statut_.charAt(0), "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void addParamComptabilise(Boolean comptabilise) {
        ParametreRequete p = new ParametreRequete("y.comptabilised", "comptabilise", comptabilise, "=", "AND");
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void paginer(boolean avancer) {
        initForm = false;
        loadAllVirement(avancer);
    }

    public void changeMaxResult(ValueChangeEvent ev) {
        imax = (long) ev.getNewValue();
        initForm = true;
        loadAllVirement(true);
    }

    public boolean canValide() {
        return canValide(entity);
    }

    public boolean canValide(YvsComptaCaissePieceVirement y) {
        if (y != null ? y.getId() > 0 : false) {
            if (!y.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                if (y.getCible() != null ? y.getCible().getId() > 0 : false) {
                    y.setCible(giveCaisse(new YvsBaseCaisse(y.getCible().getId())));
                    return controleAccesCaisse(y.getCible(), false);
                }
            }
        }
        return false;
    }

    public void chooseModeReglement() {
        if (pieceCaisse.getMode() != null ? pieceCaisse.getMode().getId() > 0 : false) {
            ManagedModeReglement w = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
            if (w != null) {
                int idx = w.getModes().indexOf(new YvsBaseModeReglement((long) pieceCaisse.getMode().getId()));
                if (idx > -1) {
                    YvsBaseModeReglement y = w.getModes().get(idx);
                    pieceCaisse.setMode(UtilCompta.buildBeanModeReglement(y));
                }
            }
        }
    }

    public void reBuildNumero(YvsComptaCaissePieceVirement y, boolean save, boolean msg) {
        if (y != null ? y.getId() > 0 : false) {
            String ref = genererReference(Constantes.TYPE_PT_NAME, y.getDatePaimentPrevu());
            if ((ref != null) ? ref.trim().equals("") : true) {
                return;
            }
            y.setNumeroPiece(ref);
            if (save) {
                dao.update(y);
            }
            int idx = listAllVirement.indexOf(y);
            if (idx > -1) {
                listAllVirement.set(idx, y);
            }
            if (msg) {
                succes();
            }
        }
    }

    /*Gestiond e la recherche*/
    private char statutF;
    private long caisseSF, caisseDF, agenceSearch;
    private String caissierF, numPieceF;
    private double montantMinF, montantMaxF;
    private boolean paramDate;
    private Date debutF = new Date(), finF = new Date();
    private Boolean forVersement;

    public Boolean getForVersement() {
        return forVersement;
    }

    public long getAgenceSearch() {
        return agenceSearch;
    }

    public void setAgenceSearch(long agenceSearch) {
        this.agenceSearch = agenceSearch;
    }

    public void setForVersement(Boolean forVersement) {
        this.forVersement = forVersement;
    }

    public boolean isInitForm() {
        return initForm;
    }

    public void setInitForm(boolean initForm) {
        this.initForm = initForm;
    }

    public char getStatutF() {
        return statutF;
    }

    public void setStatutF(char statutF) {
        this.statutF = statutF;
    }

    public long getCaisseSF() {
        return caisseSF;
    }

    public void setCaisseSF(long caisseSF) {
        this.caisseSF = caisseSF;
    }

    public long getCaisseDF() {
        return caisseDF;
    }

    public void setCaisseDF(long caisseDF) {
        this.caisseDF = caisseDF;
    }

    public String getCaissierF() {
        return caissierF;
    }

    public void setCaissierF(String caissierF) {
        this.caissierF = caissierF;
    }

    public String getNumPieceF() {
        return numPieceF;
    }

    public void setNumPieceF(String numPieceF) {
        this.numPieceF = numPieceF;
    }

    public double getMontantMinF() {
        return montantMinF;
    }

    public void setMontantMinF(double montantMinF) {
        this.montantMinF = montantMinF;
    }

    public double getMontantMaxF() {
        return montantMaxF;
    }

    public void setMontantMaxF(double montantMaxF) {
        this.montantMaxF = montantMaxF;
    }

    public boolean isParamDate() {
        return paramDate;
    }

    public void setParamDate(boolean paramDate) {
        this.paramDate = paramDate;
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

    public void addParamStatut(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.statutPiece", "statutPiece", null, "=", "AND");
        if (ev.getNewValue() != null) {
            p.setObjet((char) ev.getNewValue());
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void addParamCaisseS(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.source", "source", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsBaseCaisse(id));
            } else if (id == -1) {
                openDialog("dlgAgenceFind");
                return;
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void addParamCaisseD(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.cible", "cible", null, "=", "AND");
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                p.setObjet(new YvsBaseCaisse(id));
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    private void findByParamDate() {
        ParametreRequete p = new ParametreRequete("y.datePaimentPrevu", "datePaimentPrevu", null, " BETWEEN ", "AND");
        if (debutF != null && finF != null) {
            if (debutF.before(finF) || debutF.equals(finF)) {
                p.setObjet(debutF);
                p.setOtherObjet(finF);
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void addParamDate(ValueChangeEvent ev) {
        boolean b = (boolean) ev.getNewValue();
        if (b) {
            findByParamDate();
        } else {
            debutF = finF = null;
        }
        findByParamDate();
    }

    public void addParamAgence() {
        ParametreRequete p = new ParametreRequete("y.source.journal.agence", "agence", null, "=", "AND");
        if (agenceSearch > 0) {
            p = new ParametreRequete("y.source.journal.agence", "agence", new YvsAgences(agenceSearch), "=", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void addParamDateF(SelectEvent ev) {
        findByParamDate();
    }

    public void addParamMontant() {
        ParametreRequete p = new ParametreRequete("y.montant", "montant", null, " BETWEEN ", "AND");
        if (montantMaxF != 0 && montantMinF != 0 && montantMinF <= montantMaxF) {
            p.setObjet(montantMinF);
            p.setOtherObjet(montantMaxF);
        } else {
            if (montantMinF != 0) {
                p.setOperation("=");
                p.setObjet(montantMinF);
            } else if (montantMaxF != 0) {
                p.setOperation("=");
                p.setObjet(montantMaxF);
            }
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void addParamCaissier(String caissier) {
        ParametreRequete p = new ParametreRequete(null, "caissier", "XX", " LIKE ", "AND");
        if ((caissier != null) ? !caissier.isEmpty() : false) {
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.caissierSource.codeUsers)", "codeCS", "%" + caissier.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.caissierSource.nomUsers)", "nomUsersS", "%" + caissier.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.caissierCible.codeUsers)", "codeCC", "%" + caissier.trim().toUpperCase() + "%", " LIKE ", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.caissierCible.nomUsers)", "nomUsersC", "%" + caissier.trim().toUpperCase() + "%", " LIKE ", "OR"));
        } else {
            p.setObjet(null);
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void addParamNumeroPiece() {
        addParamNumeroPiece(numPieceF);
    }

    public void addParamNumeroPiece(String numPiece) {
        ParametreRequete p = new ParametreRequete("y.numeroPiece", "numeroPiece", null, " LIKE ", "AND");
        if ((numPiece != null) ? !numPiece.isEmpty() : false) {
            p.setObjet("%" + numPiece.trim().toUpperCase() + "%");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void addParamVersement() {
        ParametreRequete p = new ParametreRequete("y.id", "versements", null, "IN", "AND");
        if (forVersement != null) {
            List<Long> ids = dao.loadNameQueries("YvsComptaNotifVersementVente.findIdPiece", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
            if (ids == null) {
                ids = new ArrayList<>();
            }
            ids.add(-1L);
            p = new ParametreRequete("y.id", "versements", ids, forVersement ? "IN" : "NOT IN", "AND");
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void verifyComptabilised(Boolean comptabilised) {
        initForm = true;
        loadAllVirement(true);
        if (comptabilised != null) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                List<YvsComptaCaissePieceVirement> list = new ArrayList<>();
                list.addAll(listAllVirement);
                for (YvsComptaCaissePieceVirement y : list) {
                    y.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_VIREMENT));
                    if (comptabilised ? !y.getComptabilise() : y.getComptabilise()) {
                        listAllVirement.remove(y);
                    }
                }
            }
        }
        update("table_all_pieceCaisse_virement");
    }

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_piece_virement c RIGHT JOIN yvs_compta_caisse_piece_virement y ON c.reglement = y.id "
                    + "INNER JOIN yvs_base_caisse e ON y.source = e.id INNER JOIN yvs_compta_journaux h ON e.journal = h.id "
                    + "INNER JOIN yvs_agences a ON h.agence = a.id WHERE y.statut_piece = 'P' AND a.societe = ? "
                    + "AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (paramDate) {
                query += " AND y.date_paiement BETWEEN ? AND ?";
                param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(debutF, 2), new Options(finF, 3)};
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, param);
            nbrComptaSearch = count != null ? count : 0;
        }
        paginator.addParam(p);
        initForm = true;
        loadAllVirement(true);
    }

    public void validePiece() {
        entity.setDatePaiement(new Date());
        validePiece(entity, true);

    }

    public void soumettreCaisse() {
        soumettreVirement(entity, true);
    }

    public boolean soumettreVirement(YvsComptaCaissePieceVirement y, boolean succes) {
        if (y != null ? y.getId() > 0 : false) {
            if ((y.getSource() != null) ? y.getSource().getId() > 0 : false) {
                y.setSource(giveCaisse(new YvsBaseCaisse(y.getSource().getId())));
                if (!controleAccesCaisse(y.getSource(), true)) {
                    return false;
                }
                if (!controleEcartVente(y.getCaissierSource().getId(), y.getDatePaiement(), true)) {
                    return false;
                }
                y.setStatutPiece(Constantes.STATUT_DOC_SOUMIS);
                y.setAuthor(currentUser);
                y.setDateUpdate(new Date());
                dao.update(y);
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    w.comptabiliserCaisseVirement(y, false, false);
                    update("head_corp_form_suivi_virement");
                }
                if (pieceCaisse != null) {
                    pieceCaisse.setStatutPiece(y.getStatutPiece());
                    pieceCaisse.setStatutExterne(String.valueOf(Constantes.STATUT_DOC_SOUMIS));
                }
                int idx = listAllVirement.indexOf(y);
                if (idx >= 0) {
                    listAllVirement.set(idx, y);
                } else {
                    listAllVirement.add(0, y);
                }
                update("zone_txt_resteDD");
                update("header_form_suiviphaseVirement");
                update("table_all_pieceCaisse_virement");
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

    public boolean validePiece(YvsComptaCaissePieceVirement y, boolean succes) {
        if (y != null ? y.getId() > 0 : false) {
            if ((y.getCible() != null) ? y.getCible().getId() > 0 : false) {
                y.setCible(giveCaisse(new YvsBaseCaisse(y.getCible().getId())));
                if (!controleAccesCaisse(y.getCible(), true)) {
                    return false;
                }
                if (!controleEcartVente(y.getCaissierSource().getId(), y.getDatePaiement(), true)) {
                    return false;
                }
                y.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                y.setAuthor(currentUser);
                y.setDateUpdate(new Date());
                dao.update(y);
                ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                if (w != null) {
                    w.comptabiliserCaisseVirement(y, false, false);
                    update("head_corp_form_suivi_virement");
                }
                if (pieceCaisse != null) {
                    pieceCaisse.setStatutPiece(y.getStatutPiece());
                    pieceCaisse.setStatutExterne(Constantes.ETAT_REGLE);
                }
                int idx = listAllVirement.indexOf(y);
                if (idx >= 0) {
                    listAllVirement.set(idx, y);
                } else {
                    listAllVirement.add(0, y);
                }
                update("zone_txt_resteDD");
                update("header_form_suiviphaseVirement");
                update("table_all_pieceCaisse_virement");
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

    public void payerPieceCaisse() {
        payerPieceCaisse(entity);
    }

    public void payerPieceCaisse(YvsComptaCaissePieceVirement y) {
        if (y != null ? y.getId() > 0 : false) {
            y.setDatePaiement(y.getDatePaiement() != null ? y.getDatePaiement() : y.getDatePaimentPrevu());
            if (y.getModel() == null) {
                y.setModel(modeEspece());
            }
            validePiece(y, true);
        }
    }

    private void ordonnePhase(List<YvsComptaPhasePieceVirement> l, YvsComptaPhasePieceVirement p) {
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

    private List<YvsComptaPhasePieceVirement> ordonnePhase(List<YvsComptaPhasePieceVirement> l) {
        Collections.sort(l, new YvsComptaPhasePieceVirement());
        int idx = 0;
        while (idx < l.size()) {
            ordonnePhase(l, l.get(idx));
            idx++;
        }
        return l;
    }

    private boolean controleValidation(YvsComptaPhasePieceVirement pp) {
        if ((pp.getVirement().getSource() != null) ? pp.getVirement().getSource().getId() <= 0 : true) {
            getErrorMessage("Aucune banque source n'a été trouvé !");
            return false;
        }
        if ((pp.getVirement().getCible() != null) ? pp.getVirement().getCible().getId() <= 0 : true) {
            getErrorMessage("Aucune banque cible n'a été trouvé !");
            return false;
        }
        return true;
    }

    public void comptabiliserPhaseVirement(YvsComptaPhasePieceVirement pp, boolean comptabilise) {
        selectPhaseVirement = pp;
        if (entity.getPhasesReglement() != null ? !entity.getPhasesReglement().isEmpty() : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                int idx = entity.getPhasesReglement().indexOf(pp);
                if (idx > -1) {
                    if (idx == 0) {
                        if (comptabilise) {
                            w.comptabiliserPhaseCaisseVirement(pp, true, true);
                        } else {
                            openDialog("dlgComptabilisePhase");
                        }
                    } else {
                        YvsComptaPhasePieceVirement prec = entity.getPhasesReglement().get(idx - 1);
                        if (dao.isComptabilise(prec.getId(), Constantes.SCR_PHASE_VIREMENT)) {
                            if (comptabilise) {
                                w.comptabiliserPhaseCaisseVirement(pp, true, true);
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

    public void printPhase(YvsComptaPhasePieceVirement y) {
        try {
            if (y != null ? y.getId() > 0 : false) {
                Map<String, Object> param = new HashMap<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
                param.put("ID", y.getId().intValue());
                param.put("IMG_PAYE", path + FILE_SEPARATOR + "icones" + FILE_SEPARATOR + (y.getVirement().getStatutPiece() == Constantes.STATUT_DOC_PAYER ? "solde.png" : "empty.png"));
                param.put("MONTANT", Nombre.CALCULATE.getValue(y.getVirement().getMontant()));
                param.put("AUTEUR", currentUser.getUsers().getNomUsers());
                param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
                param.put("LOGO", returnLogo());
                executeReport("phase_pc_virement", param);
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedVirement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void validEtapesPieces(YvsComptaPhasePieceVirement pp) {
        if (!pp.getPhaseOk()) {
            if (!asDroitValidePhase(pp.getPhaseReg())) {
                openNotAcces();
                return;
            }
            if (controleValidation(pp)) {
                if (pp.getPhaseReg().getActionInBanque()) {
                    if (!pp.getCaisse().getTypeCaisse().equals("BANQUE")) {
                        getErrorMessage("La validation de cette phase doit se passer dans une banque");
                        return;
                    }
                }

                pp.setPhaseOk(true);
                pp.setAuthor(currentUser);
                if (pp.getPhaseReg().getReglementOk()) {
                    pp.getVirement().setStatutPiece(Constantes.STATUT_DOC_PAYER);
                } else {
                    if (pp.getVirement().getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                        pp.getVirement().setStatutPiece(Constantes.STATUT_DOC_ENCOUR);
                    }
                }
                dao.update(pp.getVirement());
                entity.setStatutPiece(pp.getVirement().getStatutPiece());
                int index = listAllVirement.indexOf(pp.getVirement());
                if (index > -1) {
                    listAllVirement.set(index, pp.getVirement());
                }

                dao.update(pp);
                pp.setEtapeActive(false);
                int idx = entity.getPhasesReglement().indexOf(pp);
                if (idx >= 0 && (idx + 1) < entity.getPhasesReglement().size()) {
                    entity.getPhasesReglement().get(idx + 1).setEtapeActive(true);
                    currentPhaseVirement = entity.getPhasesReglement().get(idx + 1);
                } else if (idx == (entity.getPhasesReglement().size() - 1)) {
                    entity.getPhasesReglement().get(idx).setEtapeActive(true);
                }
            }
        } else {
            getWarningMessage("Phase déjà valide! ");
        }
        update("corps_form_suivi_pvir");
        update("header_form_suiviphaseVirement");
    }

    //Supprimer les étapes de validation d'un chèque et ramener la pièce au statut en cours
    public void cancelAllEtapesPieces() {
        if (!autoriser("compta_cancel_piece_valide")) {
            openNotAcces();
            return;
        }
        YvsComptaCaissePieceVirement pc = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{entity.getId()});
        if (pc != null ? pc.getId() != null ? pc.getId() > 0 : false : false) {
            //vérifie le droit:
            if (pc.getStatutPiece() != Constantes.STATUT_DOC_PAYER) {
                try {
                    int i = 0;
                    ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
                    if (pc.getPhasesReglement() != null ? !pc.getPhasesReglement().isEmpty() : false) {
                        for (YvsComptaPhasePieceVirement ph : pc.getPhasesReglement()) {
                            ph.setPhaseOk(false);
                            ph.setEtapeActive(i == 0);
                            ph.setAuthor(currentUser);
                            dao.update(ph);
                            if (w != null) {
                                w.unComptabiliserPhaseCaisseVirement(ph, false);
                            }
                            i++;
                            int idx = entity.getPhasesReglement().indexOf(ph);
                            if (idx > -1) {
                                entity.getPhasesReglement().set(idx, ph);
                            }
                        }
                    } else {
                        if (w != null) {
                            w.unComptabiliserCaisseVirement(pc, false);
                        }
                    }
                    pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    if (entity != null) {
                        entity.setStatutPiece(pc.getStatutPiece());
                    }
                    dao.update(pc);
                    int idx = listAllVirement.indexOf(pc);
                    if (idx > 0) {
                        listAllVirement.set(idx, pc);
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
        update("corps_form_suivi_pvir");
        update("header_form_suiviphaseVirement");
    }

    public void cancelValidEtapesPieces(YvsComptaPhasePieceVirement pp) {

        //l'étape suivante ne doit pas être validé
        if (!asDroitValidePhase(pp.getPhaseReg())) {
            openNotAcces();
            return;
        }
        int idx = entity.getPhasesReglement().indexOf(pp);
        YvsComptaPhasePieceVirement pSvt = null;
        if (idx >= 0 && (idx + 1) < entity.getPhasesReglement().size()) {
            pSvt = entity.getPhasesReglement().get(idx + 1);
        } else if (idx == (entity.getPhasesReglement().size() - 1) || idx == 0) {
            pSvt = entity.getPhasesReglement().get(idx);
        }
        if (pSvt != null) {
            if (!pSvt.isEtapeActive() ? !pSvt.equals(pp) : false) {
                getErrorMessage("Vous ne pouvez annuler cette étape !");
                return;
            }
            pSvt.setEtapeActive(false);
            idx = entity.getPhasesReglement().indexOf(pSvt);
            if (idx >= 0) {
                entity.getPhasesReglement().set(idx, pSvt);
            }
        }
        pp.setEtapeActive(false);
        pp.setPhaseOk(false);
        pp.setStatut(Constantes.STATUT_DOC_ATTENTE);
        pp.setAuthor(currentUser);
        if (pp.getPhaseReg().getReglementOk()) {
            pp.getVirement().setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
            dao.update(pp.getVirement());
            entity.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        }
        pp.setEtapeActive(true);
        dao.update(pp);
        currentPhaseVirement = pp;
        currentPhaseVirement.setDateValider(new Date());

        YvsComptaCaissePieceVirement pc = pp.getVirement();
        idx = pc.getPhasesReglement().indexOf(pp);
        if (idx >= 0) {
            pc.getPhasesReglement().set(idx, pp);
        }
        idx = listAllVirement.indexOf(pp.getVirement());
        if (idx >= 0) {
            listAllVirement.set(idx, pc);
        }
        //Annuler Comptabilise la pièce 
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            w.unComptabiliserPhaseCaisseVirement(pp, false);
        }
        update("corps_form_suivi_pvir");
        update("header_form_suiviphaseVirement");
    }

    public void openTogeneratedPhaseVirement(YvsComptaCaissePieceVirement pc) {
        this.entity = pc;
        openDialog("dlgInitPhase");
    }

    public void generatedPhaseVirement() {
        if (entity != null && !entity.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
            if (entity.getPhasesReglement() != null) {
                try {
                    if (entity.getCible() != null ? entity.getCible().getId() < 1 : true) {
                        getErrorMessage("Vous devez precisez la caisse qui a été mouvementé");
                        return;
                    }
                    for (YvsComptaPhasePieceVirement ph : entity.getPhasesReglement()) {
                        ph.setAuthor(currentUser);
                        dao.delete(ph);
                    }
                    entity.getPhasesReglement().clear();
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByMode", new String[]{"mode"}, new Object[]{entity.getModel()});
                    //lié les phases à la pièce de règlements
                    YvsComptaPhasePieceVirement pp;
                    if (entity.getPhasesReglement() == null) {
                        entity.setPhasesReglement(new ArrayList<YvsComptaPhasePieceVirement>());
                    }
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhasePieceVirement(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setPhaseReg(ph);
                        pp.setVirement(entity);
                        if (entity.getCible() != null ? entity.getCible().getId() > 0 : false) {
                            pp.setCaisse(entity.getCible());
                        }
                        pp = (YvsComptaPhasePieceVirement) dao.save1(pp);
                        entity.getPhasesReglement().add(pp);
                    }
                    update("table_list_piece_cheque_achat");
                    succes();
                } catch (Exception ex) {
                    getErrorMessage("Impossible de réaliser cette action !");
                }
            }
        } else {
            if (entity.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce est déjà payé !");
            }
            if (entity == null) {
                getErrorMessage("Aucune pièce de règlement n'a été selectionné !");
            }
        }
    }

    public boolean isComptabiliseBean(PieceTresorerie y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_VIREMENT));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsComptaCaissePieceVirement y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_VIREMENT));
            }
            return y.getComptabilise();
        }
        return false;
    }

    public void changeStatuVirements() {
        List<Integer> ids = decomposeSelection(tabIds);
        for (Integer idx : ids) {
            if (listAllVirement.get(idx).getStatutPiece().equals(Constantes.STATUT_DOC_SOUMIS)) {
                System.err.println(" --- Disabled ....");
                changeStatutPiece(listAllVirement.get(idx), false);
            }
        }
        getInfoMessage("Opération terminé avec succès !");
    }
}
