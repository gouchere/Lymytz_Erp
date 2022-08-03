/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.caisse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.ModeDeReglement;
import yvs.base.compta.UtilCompta;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.commercial.achat.DocAchat;
import yvs.commercial.achat.ManagedFactureAchat;
import yvs.commercial.vente.DocVente;
import yvs.commercial.vente.ManagedFactureVente;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.entity.compta.YvsComptaCaissePieceCompensation;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedCompensation extends Managed<PieceTresorerie, YvsComptaCaissePieceCompensation> implements Serializable {
    
    private char sens = 'C';
    private long id = -1000000;
    private PieceTresorerie pieceVente = new PieceTresorerie();
    private PieceTresorerie pieceAchat = new PieceTresorerie();    
    
    private List<YvsComDocVentes> ventes;
    private List<YvsComDocAchats> achats;
    private YvsComDocVentes vente = new YvsComDocVentes();
    private YvsComDocAchats achat = new YvsComDocAchats();
    private List<YvsComptaMouvementCaisse> mouvements;
    
    private YvsComptaCaissePieceVente selectPieceVente = new YvsComptaCaissePieceVente();
    private YvsComptaCaissePieceAchat selectPieceAchat = new YvsComptaCaissePieceAchat();
    private YvsComptaCaissePieceCompensation selectCompensation = new YvsComptaCaissePieceCompensation();
    
    public ManagedCompensation() {
        mouvements = new ArrayList<>();
        ventes = new ArrayList<>();
        achats = new ArrayList<>();
    }
    
    public YvsComDocVentes getVente() {
        return vente;
    }
    
    public void setVente(YvsComDocVentes vente) {
        this.vente = vente;
    }
    
    public YvsComDocAchats getAchat() {
        return achat;
    }
    
    public void setAchat(YvsComDocAchats achat) {
        this.achat = achat;
    }
    
    public char getSens() {
        return sens;
    }
    
    public void setSens(char sens) {
        this.sens = sens;
    }
    
    public PieceTresorerie getPieceVente() {
        return pieceVente;
    }
    
    public void setPieceVente(PieceTresorerie pieceVente) {
        this.pieceVente = pieceVente;
    }
    
    public PieceTresorerie getPieceAchat() {
        return pieceAchat;
    }
    
    public void setPieceAchat(PieceTresorerie pieceAchat) {
        this.pieceAchat = pieceAchat;
    }
    
    public List<YvsComDocVentes> getVentes() {
        return ventes;
    }
    
    public void setVentes(List<YvsComDocVentes> ventes) {
        this.ventes = ventes;
    }
    
    public List<YvsComDocAchats> getAchats() {
        return achats;
    }
    
    public void setAchats(List<YvsComDocAchats> achats) {
        this.achats = achats;
    }
    
    public List<YvsComptaMouvementCaisse> getMouvements() {
        return mouvements;
    }
    
    public void setMouvements(List<YvsComptaMouvementCaisse> mouvements) {
        this.mouvements = mouvements;
    }
    
    public YvsComptaCaissePieceVente getSelectPieceVente() {
        return selectPieceVente;
    }
    
    public void setSelectPieceVente(YvsComptaCaissePieceVente selectPieceVente) {
        this.selectPieceVente = selectPieceVente;
    }
    
    public YvsComptaCaissePieceAchat getSelectPieceAchat() {
        return selectPieceAchat;
    }
    
    public void setSelectPieceAchat(YvsComptaCaissePieceAchat selectPieceAchat) {
        this.selectPieceAchat = selectPieceAchat;
    }
    
    @Override
    public void loadAll() {
        
    }
    
    @Override
    public boolean controleFiche(PieceTresorerie bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getMode() != null ? bean.getMode().getId() < 1 : true) {
            getErrorMessage("Vous devez precisez le mode de paiement !");
            return false;
        }
        return true;
    }
    
    @Override
    public void resetFiche() {
        switch (sens) {
            case 'C':
                pieceAchat = new PieceTresorerie();
                pieceAchat.setMode(UtilCompta.buildBeanModeReglement(selectPieceVente.getModel()));
                if (achat != null) {
                    if (achat.getMontantTotal() > 0) {
                        double m = achat.getMontantTotal();
                        for (YvsComptaCaissePieceAchat r : achat.getReglements()) {
                            m -= r.getMontant();
                        }
                        pieceAchat.setMontant(m > 0 ? m : 0);
                    }
                    pieceAchat.setNumRefExterne(achat.getNumDoc());
                }
                break;
            default:
                pieceVente = new PieceTresorerie();
                pieceVente.setMode(UtilCompta.buildBeanModeReglement(selectPieceAchat.getModel()));
                if (vente != null) {
                    if (vente.getMontantTotal() > 0) {
                        double m = vente.getMontantTotal();
                        for (YvsComptaCaissePieceVente r : vente.getReglements()) {
                            m -= r.getMontant();
                        }
                        pieceVente.setMontant(m > 0 ? m : 0);
                    }
                    pieceVente.setNumRefExterne(vente.getNumDoc());
                }
                break;
        }
        selectCompensation = new YvsComptaCaissePieceCompensation();
    }
    
    @Override
    public boolean saveNew() {
        try {
            selectCompensation.setSens(sens);
            switch (sens) {
                case 'C': {
                    ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
                    if (w != null) {
                        if (pieceAchat.getMode() != null ? pieceAchat.getMode().getId() > 0 : false) {
                            ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                            if (m != null) {
                                int idx = m.getModes().indexOf(new YvsBaseModeReglement((long) pieceAchat.getMode().getId()));
                                if (idx > -1) {
                                    YvsBaseModeReglement o = m.getModes().get(idx);
                                    pieceAchat.setMode(new ModeDeReglement(o.getId().intValue(), o.getDesignation(), o.getTypeReglement()));
                                }
                            }
                        }
                        pieceAchat.setMouvement(Constantes.MOUV_CREDIT);
                        if (controleFiche(pieceAchat)) {
                            DocAchat d = UtilCom.buildBeanDocAchat(achat);
                            pieceAchat.setDocAchat(d);
                            YvsComptaCaissePieceAchat p = UtilCompta.buildTresoreriAchat(pieceAchat, currentUser);
                            p.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            p.setDatePaiement(null);
                            p = w.createNewPieceCaisse(d, p, false);
                            if (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false) {
                                if (pieceAchat.getId() < 1) {
                                    pieceAchat.setId(p.getId());
                                    selectCompensation.setId((long) (id++));
                                }
                                selectCompensation.setAchat(p);
                                selectCompensation.setVente(selectPieceVente);
                                int idx = selectPieceVente.getCompensations().indexOf(selectCompensation);
                                if (idx > -1) {
                                    selectPieceVente.getCompensations().set(idx, selectCompensation);
                                } else {
                                    selectPieceVente.getCompensations().add(0, selectCompensation);
                                }
                                idx = achat.getReglements().indexOf(p);
                                if (idx > -1) {
                                    achat.getReglements().set(idx, p);
                                } else {
                                    achat.getReglements().add(0, p);
                                }
                                succes();
                            }
                        }
                    }
                    break;
                }
                default: {
                    ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
                    if (w != null) {
                        if (pieceVente.getMode() != null ? pieceVente.getMode().getId() > 0 : false) {
                            ManagedModeReglement m = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
                            if (m != null) {
                                int idx = m.getModes().indexOf(new YvsBaseModeReglement((long) pieceVente.getMode().getId()));
                                if (idx > -1) {
                                    YvsBaseModeReglement o = m.getModes().get(idx);
                                    pieceVente.setMode(new ModeDeReglement(o.getId().intValue(), o.getDesignation(), o.getTypeReglement()));
                                }
                            }
                        }
                        pieceVente.setMouvement(Constantes.MOUV_DEBIT);
                        if (controleFiche(pieceVente)) {
                            DocVente d = UtilCom.buildSimpleBeanDocVente(vente);
                            pieceVente.setDocVente(d);
                            YvsComptaCaissePieceVente p = UtilCompta.buildTresoreriVente(pieceVente, currentUser);
                            p.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                            p.setDatePaiement(null);
                            p = w.createNewPieceCaisse(d, p, false);
                            if (p != null ? (p.getId() != null ? p.getId() > 0 : false) : false) {
                                if (pieceVente.getId() < 1) {
                                    pieceVente.setId(p.getId());
                                    selectCompensation.setId((long) (id++));
                                }
                                selectCompensation.setVente(p);
                                selectCompensation.setAchat(selectPieceAchat);
                                int idx = selectPieceAchat.getCompensations().indexOf(selectCompensation);
                                if (idx > -1) {
                                    selectPieceAchat.getCompensations().set(idx, selectCompensation);
                                } else {
                                    selectPieceAchat.getCompensations().add(0, selectCompensation);
                                }
                                idx = vente.getReglements().indexOf(p);
                                if (idx > -1) {
                                    vente.getReglements().set(idx, p);
                                } else {
                                    vente.getReglements().add(0, p);
                                }
                                succes();
                            }
                        }
                    }
                    break;
                }
            }
            resetFiche();
            return true;
        } catch (Exception ex) {
            getErrorMessage("Action impossible !");
            getException("Error", ex);
        }
        return false;
    }
    
    public YvsComptaCaissePieceAchat payerOrAnnuler(YvsComptaCaissePieceAchat pc, boolean msg) {
        try {
            List<YvsComDocVentes> list = new ArrayList<>();
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (!pc.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                String ERROR = pc.canValide();
                if (ERROR != null ? ERROR.trim().length() > 0 : false) {
                    if (msg) {
                        getErrorMessage(ERROR);
                    }
                    return null;
                }
                pc.setAuthor(currentUser);
                pc.setDateUpdate(new Date());
                pc.setCaissier(currentUser.getUsers());
                pc.setDatePaiement(pc.getDatePaiement() != null ? pc.getDatePaiement() : pc.getDatePaimentPrevu());
                pc.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                dao.update(pc);
                
                for (YvsComptaCaissePieceCompensation y : pc.getCompensations()) {
                    y.getVente().setAuthor(currentUser);
                    y.getVente().setDateUpdate(new Date());
                    y.getVente().setCaissier(currentUser.getUsers());
                    y.getVente().setDatePaiement(y.getVente().getDatePaiement() != null ? y.getVente().getDatePaiement() : y.getVente().getDatePaimentPrevu());
                    y.getVente().setStatutPiece(Constantes.STATUT_DOC_PAYER);
                    dao.update(y.getVente());
                    if (!list.contains(y.getVente().getVente())) {
                        list.add(y.getVente().getVente());
                    }
                }
                if (dao.isComptabilise(pc.getAchat().getId(), Constantes.SCR_ACHAT)) {
                    if (w != null) {
                        w.comptabiliserCaisseAchat(pc, false, false);
                    }
                }
            } else {
                if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_ACHAT)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return null;
                    }
                    if (w != null) {
                        if (w.unComptabiliserCaisseAchat(pc, false)) {
                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                            return null;
                        }
                    }
                }
                pc.setAuthor(currentUser);
                pc.setDateUpdate(new Date());
                pc.setCaissier(null);
                pc.setDatePaiement(null);
                pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                dao.update(pc);
                
                for (YvsComptaCaissePieceCompensation y : pc.getCompensations()) {
                    if (w != null) {
                        if (w.unComptabiliserCaisseVente(y.getVente(), false)) {
                            continue;
                        }
                    }
                    y.getVente().setAuthor(currentUser);
                    y.getVente().setDateUpdate(new Date());
                    y.getVente().setCaissier(null);
                    y.getVente().setDatePaiement(null);
                    y.getVente().setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    dao.update(y.getVente());
                    
                    if (!list.contains(y.getVente().getVente())) {
                        list.add(y.getVente().getVente());
                    }
                    
                }
            }
            ManagedFactureVente ws = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            for (YvsComDocVentes d : list) {
                Map<String, String> statuts = dao.getEquilibreVente(d.getId());
                if (statuts != null) {
                    d.setStatutLivre(statuts.get("statut_livre"));
                    d.setStatutRegle(statuts.get("statut_regle"));
                }
                if (ws != null) {
                    ws.resetView(d);
                }
            }
            if (msg) {
                succes();
            }
            return pc;
        } catch (Exception ex) {
            getErrorMessage("Action impossible !");
            getException("Error", ex);
        }
        return null;
    }
    
    public YvsComptaCaissePieceVente payerOrAnnuler(YvsComptaCaissePieceVente pc, boolean msg) {
        try {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            List<YvsComDocAchats> list = new ArrayList<>();
            if (!pc.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                String ERROR = pc.canValide();
                if (ERROR != null ? ERROR.trim().length() > 0 : false) {
                    if (msg) {
                        getErrorMessage(ERROR);
                    }
                    return null;
                }
                pc.setAuthor(currentUser);
                pc.setDateUpdate(new Date());
                pc.setCaissier(currentUser.getUsers());
                pc.setStatutPiece(Constantes.STATUT_DOC_PAYER);
                dao.update(pc);
                
                for (YvsComptaCaissePieceCompensation y : pc.getCompensations()) {
                    y.getAchat().setAuthor(currentUser);
                    y.getAchat().setDateUpdate(new Date());
                    y.getAchat().setCaissier(currentUser.getUsers());
                    y.getAchat().setDatePaiement(y.getAchat().getDatePaiement() != null ? y.getAchat().getDatePaiement() : y.getAchat().getDatePaimentPrevu());
                    y.getAchat().setStatutPiece(Constantes.STATUT_DOC_PAYER);
                    dao.update(y.getAchat());
                    if (!list.contains(y.getAchat().getAchat())) {
                        list.add(y.getAchat().getAchat());
                    }
                }
                if (dao.isComptabilise(pc.getVente().getId(), Constantes.SCR_VENTE)) {
                    if (w != null) {
                        w.comptabiliserCaisseVente(pc, false, false);
                    }
                }
            } else {
                if (dao.isComptabilise(pc.getId(), Constantes.SCR_CAISSE_VENTE)) {
                    if (!autoriser("compta_od_annul_comptabilite")) {
                        getErrorMessage("Vous n'avez pas le droit d'effectuer cette action une fois que le document est comptabilisé");
                        return null;
                    }
                    if (w != null) {
                        if (w.unComptabiliserCaisseVente(pc, false)) {
                            getErrorMessage("Annulation de la comptabilisation Impossible!!!");
                            return null;
                        }
                    }
                }
                pc.setAuthor(currentUser);
                pc.setDateUpdate(new Date());
                pc.setCaissier(null);
                pc.setDatePaiement(null);
                pc.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                dao.update(pc);
                for (YvsComptaCaissePieceCompensation y : pc.getCompensations()) {
                    if (w != null) {
                        if (w.unComptabiliserCaisseAchat(y.getAchat(), false)) {
                            continue;
                        }
                    }
                    y.getAchat().setAuthor(currentUser);
                    y.getAchat().setDateUpdate(new Date());
                    y.getAchat().setCaissier(null);
                    y.getAchat().setDatePaiement(null);
                    y.getAchat().setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
                    dao.update(y.getAchat());
                    
                    if (!list.contains(y.getAchat().getAchat())) {
                        list.add(y.getAchat().getAchat());
                    }
                }
            }
            ManagedFactureAchat ws = (ManagedFactureAchat) giveManagedBean(ManagedFactureAchat.class);
            for (YvsComDocAchats d : list) {
                Map<String, String> statuts = dao.getEquilibreAchat(d.getId());
                if (statuts != null) {
                    d.setStatutLivre(statuts.get("statut_livre"));
                    d.setStatutRegle(statuts.get("statut_regle"));
                }
                if (ws != null) {
                    ws.resetView(d);
                }
            }
            if (msg) {
                succes();
            }
            return pc;
        } catch (Exception ex) {
            getErrorMessage("Action impossible !");
            getException("Error", ex);
        }
        return null;
    }
    
    public boolean payerOrAnnuler(boolean msg) {
        try {
            switch (sens) {
                case 'C': {
                    YvsComptaCaissePieceVente y = payerOrAnnuler(selectPieceVente, msg);
                    return y != null ? (y.getId() != null ? y.getId() > 0 : false) : false;
                }
                default: {
                    YvsComptaCaissePieceAchat y = payerOrAnnuler(selectPieceAchat, msg);
                    return y != null ? (y.getId() != null ? y.getId() > 0 : false) : false;
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Action impossible !");
            getException("Error", ex);
        }
        return false;
    }
    
    public void addComprensation(YvsComptaCaissePieceCompensation y) {
        if (y != null) {
            if (y.getVente().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce de vente est déjà payée");
                return;
            }
            if (y.getAchat().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                getErrorMessage("Cette pièce d'achat est déjà payée");
                return;
            }
            switch (sens) {
                case 'C': {
                    if (y.getAchat().getMontant() > selectPieceVente.getMontant()) {
                        getErrorMessage("Le montant d'achat ne peut pas etre supérieur au reglement");
                        return;
                    }
                    break;
                }
                default: {
                    if (y.getVente().getMontant() > selectPieceAchat.getMontant()) {
                        getErrorMessage("Le montant de vente ne peut pas etre supérieur au reglement");
                        return;
                    }
                    break;
                }
            }
            long id = y.getId();
            y.setAuthor(currentUser);
            y.setDateSave(new Date());
            y.setDateUpdate(new Date());
            if (y.getId() > 0) {
                dao.delete(y);
                y.setId((long) this.id++);
            } else {
                y.setId(null);
                y = (YvsComptaCaissePieceCompensation) dao.save1(y);
            }
            switch (sens) {
                case 'C': {
                    int idx = selectPieceVente.getCompensations().indexOf(new YvsComptaCaissePieceCompensation(id));
                    if (idx > -1) {
                        selectPieceVente.getCompensations().set(idx, y);
                    }
                    break;
                }
                default: {
                    int idx = selectPieceAchat.getCompensations().indexOf(new YvsComptaCaissePieceCompensation(id));
                    if (idx > -1) {
                        selectPieceAchat.getCompensations().set(idx, y);
                    }
                    break;
                }
            }
        }
    }
    
    public void deleteBean(YvsComptaCaissePieceCompensation y) {
        try {
            if (y != null) {
                dao.delete(y);
                switch (sens) {
                    case 'C':
                        dao.delete(y.getAchat());
                        selectPieceVente.getCompensations().remove(y);
                        break;
                    default:
                        dao.delete(y.getVente());
                        selectPieceAchat.getCompensations().remove(y);
                        break;
                }
                if (selectCompensation.getId().equals(y.getId())) {
                    resetFiche();
                }
                succes();
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression impossible !");
            getException("Error", ex);
        }
    }
    
    @Override
    public void onSelectObject(YvsComptaCaissePieceCompensation y) {
        selectCompensation = y;
        switch (sens) {
            case 'C': {
                pieceAchat = UtilCompta.buildBeanTresoreri(y.getAchat());
                achat = y.getAchat().getAchat();
                int idx = achats.indexOf(achat);
                if (idx > -1) {
                    achats.get(idx).setNew_(true);
                }
                setMontantTotalDoc(achat);
                break;
            }
            default: {
                pieceVente = UtilCompta.buildBeanTresoreri(y.getVente());
                vente = y.getVente().getVente();
                int idx = ventes.indexOf(vente);
                if (idx > -1) {
                    ventes.get(idx).setNew_(true);
                }
                setMontantTotalDoc(vente);
                break;
            }
        }
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            onSelectObject((YvsComptaCaissePieceCompensation) ev.getObject());
        }
    }
    
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }
    
    public void loadOnViewVente(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            vente = (YvsComDocVentes) ev.getObject();
//            List<YvsComptaCaissePieceVente> list = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFactureTypeStatut", new String[]{"vente", "type", "statut"}, new Object[]{vente, Constantes.MODE_PAIEMENT_COMPENSATION, Constantes.STATUT_DOC_ATTENTE});
//            if (list != null ? !list.isEmpty() : false) {
//                YvsComptaCaissePieceCompensation y;
//                for (YvsComptaCaissePieceVente p : list) {
//                    y = (YvsComptaCaissePieceCompensation) dao.loadOneByNameQueries("YvsComptaCaissePieceCompensation.findOne", new String[]{"achat", "vente"}, new Object[]{selectPieceAchat, p});
//                    if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
//                        y = new YvsComptaCaissePieceCompensation(id++);
//                        y.setAchat(selectPieceAchat);
//                        y.setAuthor(currentUser);
//                        y.setDateSave(new Date());
//                        y.setDateUpdate(new Date());
//                        y.setSens(sens);
//                        y.setVente(p);
//                    }
//                    boolean add = true;
//                    for (YvsComptaCaissePieceCompensation c : selectPieceAchat.getCompensations()) {
//                        if (c.getVente().equals(p)) {
//                            add = false;
//                            break;
//                        }
//                    }
//                    if (add) {
//                        selectPieceAchat.getCompensations().add(0, y);
//                    }
//                }
//            }
            setMontantTotalDoc(vente);
            resetFiche();
        }
    }
    
    public void loadOnViewAchat(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            achat = (YvsComDocAchats) ev.getObject();
//            List<YvsComptaCaissePieceAchat> list = dao.loadNameQueries("YvsComptaCaissePieceAchat.findByFactureTypeStatut", new String[]{"achat", "type", "statut"}, new Object[]{achat, Constantes.MODE_PAIEMENT_COMPENSATION, Constantes.STATUT_DOC_ATTENTE});
//            if (list != null ? !list.isEmpty() : false) {
//                YvsComptaCaissePieceCompensation y;
//                for (YvsComptaCaissePieceAchat p : list) {
//                    y = (YvsComptaCaissePieceCompensation) dao.loadOneByNameQueries("YvsComptaCaissePieceCompensation.findOne", new String[]{"achat", "vente"}, new Object[]{p, selectPieceVente});
//                    if (y != null ? (y.getId() != null ? y.getId() < 1 : true) : true) {
//                        y = new YvsComptaCaissePieceCompensation(id++);
//                        y.setAchat(p);
//                        y.setAuthor(currentUser);
//                        y.setDateSave(new Date());
//                        y.setDateUpdate(new Date());
//                        y.setSens(sens);
//                        y.setVente(selectPieceVente);
//                    }
//                    boolean add = true;
//                    for (YvsComptaCaissePieceCompensation c : selectPieceVente.getCompensations()) {
//                        if (c.getAchat().equals(p)) {
//                            add = false;
//                            break;
//                        }
//                    }
//                    if (add) {
//                        selectPieceVente.getCompensations().add(0, y);
//                    }
//                }
//            }
            setMontantTotalDoc(achat);
            resetFiche();
        }
    }
    
    public void onSelectVente(YvsComptaCaissePieceVente ev) {
        sens = 'C';
        if (ev != null ? (ev.getId() != null ? ev.getId() > 0 : false) : false) {
            ManagedReglementVente sr = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
            if (sr != null) {
                sr.setSelectedPiece(ev);
                update("data_mensualite_facture_vente");
            }
            achats.clear();
            if (ev.getCompensations() != null ? !ev.getCompensations().isEmpty() : false) {
                YvsComptaCaissePieceCompensation y = ev.getCompensations().get(0);
                if (!y.getSens().equals(sens)) {
                    getErrorMessage("Vous ne pouvez pas compenser ce reglement..car il est déjà associé à une autre compensation");
                    return;
                }
            }
            if (ev.getVente() != null ? (ev.getVente().getId() > 0 ? (ev.getVente().getClient() != null ? ev.getVente().getClient().getId() > 0 : false) : false) : false) {
                selectPieceVente = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{ev.getId()});
                champ = new String[]{"tiers", "typeDoc", "statut", "statutRegle"};
                val = new Object[]{ev.getVente().getClient().getTiers(), Constantes.TYPE_FA, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_VALIDE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                 ;}, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_ATTENTE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                ;
                }};
            achats = dao.loadNameQueries("YvsComDocAchats.findByTiersTypeStatuts", champ, val);
                //charge aussi toutes les pièces non encore réglé en rapport avec les factures d'achat ci-dessus
                champ = new String[]{"statutPiece", "tiers", "typeDoc", "statut", "statutRegle"};
                val = new Object[]{Constantes.STATUT_DOC_ATTENTE, ev.getVente().getClient().getTiers(), Constantes.TYPE_FA, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_VALIDE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                 ;}, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_ATTENTE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                ;
                }};
                List<YvsComptaCaissePieceAchat> pieces = dao.loadNameQueries("YvsComptaCaissePieceAchat.findByFacturesRegle", champ, val);
                for (YvsComptaCaissePieceCompensation pcc : selectPieceVente.getCompensations()) {
                    if (pieces.contains(pcc.getAchat())) {
                        pieces.remove(pcc.getAchat());
                    }
                }
                YvsComptaCaissePieceCompensation y;
                for (YvsComptaCaissePieceAchat p : pieces) {
                    if (p.getCompensations().isEmpty()) {
                        y = new YvsComptaCaissePieceCompensation(id++);
                        y.setAchat(p);
                        y.setAuthor(currentUser);
                        y.setDateSave(new Date());
                        y.setDateUpdate(new Date());
                        y.setSens(sens);
                        y.setVente(selectPieceVente);
                        
                        selectPieceVente.getCompensations().add(0, y);
                    } else {
                        for (YvsComptaCaissePieceCompensation c : p.getCompensations()) {
                            if (c.getVente().equals(ev)) {
                                selectPieceVente.getCompensations().add(c);
                            }
                        }
                    }
                }
                openDialog("dlgCompensation");
                update("select_fv_compensation");
            }
        }
    }
    
    public void onSelectAchat(YvsComptaCaissePieceAchat ev) {
        sens = 'F';
        if (ev != null ? (ev.getId() != null ? ev.getId() > 0 : false) : false) {
            ventes.clear();
            if (ev.getCompensations() != null ? !ev.getCompensations().isEmpty() : false) {
                YvsComptaCaissePieceCompensation y = ev.getCompensations().get(0);
                if (!y.getSens().equals(sens)) {
                    getErrorMessage("Vous ne pouvez pas compenser ce reglement..car il est déjà associé à une autre compensation");
                    return;
                }
            }
            if (ev.getAchat() != null ? (ev.getAchat().getId() > 0 ? (ev.getAchat().getFournisseur() != null ? ev.getAchat().getFournisseur().getId() > 0 : false) : false) : false) {
                selectPieceAchat = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{ev.getId()});
                champ = new String[]{"tiers", "typeDoc", "statut", "statutRegle"};
                val = new Object[]{ev.getAchat().getFournisseur().getTiers(), Constantes.TYPE_FV, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_VALIDE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                 ;}, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_ATTENTE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                ;
                }};
                ventes = dao.loadNameQueries("YvsComDocVentes.findByTiersTypeStatuts", champ, val);
                champ = new String[]{"statutPiece", "tiers", "typeDoc", "statut", "statutRegle"};
                val = new Object[]{Constantes.STATUT_DOC_ATTENTE, ev.getAchat().getFournisseur().getTiers(), Constantes.TYPE_FV, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_VALIDE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                 ;}, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_ATTENTE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                ;
                }};
                List<YvsComptaCaissePieceVente> pieces = dao.loadNameQueries("YvsComptaCaissePieceVente.findByFacturesRegle", champ, val);
                for (YvsComptaCaissePieceCompensation pcc : selectPieceAchat.getCompensations()) {
                    if (pieces.contains(pcc.getVente())) {
                        pieces.remove(pcc.getVente());
                    }
                }
                YvsComptaCaissePieceCompensation y;
                for (YvsComptaCaissePieceVente p : pieces) {
                    if (p.getCompensations().isEmpty()) {
                        y = new YvsComptaCaissePieceCompensation(id++);
                        y.setVente(p);
                        y.setAuthor(currentUser);
                        y.setDateSave(new Date());
                        y.setDateUpdate(new Date());
                        y.setSens(sens);
                        y.setAchat(selectPieceAchat);
                        
                        selectPieceAchat.getCompensations().add(0, y);
                    } else {
                        for (YvsComptaCaissePieceCompensation c : p.getCompensations()) {
                            if (c.getAchat().equals(ev)) {
                                selectPieceAchat.getCompensations().add(c);
                            }
                        }
                    }
                }
                openDialog("dlgCompensation");
                update("select_fa_compensation");
            }
        }
    }
    
    @Override
    public PieceTresorerie recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void geratedPiecesCompensation(YvsComptaCaissePieceAchat pv) {
        if (pv != null ? !pv.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) : false) {
            //Trouve les pièces d'achats non payé et non compensé en rapport avec le tiers de la facture de vente
            String query = "SELECT y.id FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_com_doc_ventes d ON d.id=y.vente INNER JOIN yvs_com_client f ON f.id=d.client "
                    + "WHERE y.statut_piece IN ('W','R') AND f.tiers=? AND y.id NOT IN (SELECT c.vente FROM yvs_compta_caisse_piece_compensation c) "
                    + "ORDER BY y.montant ASC";
            List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(pv.getAchat().getFournisseur().getTiers().getId(), 1)});
            double reste = 0;
            if (!ids.isEmpty()) {
                System.err.println(" --- Not pièce --- ");
                reste = equilibrePieceAchatAndPiecesVente(pv, dao.loadNameQueries("YvsComptaCaissePieceVente.findByIds", new String[]{"ids"}, new Object[]{ids}));
            } else {
                reste = pv.getMontantNonCompense();
            }
            if (reste > 0) {
                System.err.println(" --- begin generated pièce achat --- ");
                //trouve les factures non réglé du tiers et n'ayant pas encore de pièces de règlements généré 
                champ = new String[]{"tiers", "typeDoc", "statut", "statutRegle"};
                val = new Object[]{pv.getAchat().getFournisseur().getTiers(), Constantes.TYPE_FV, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_VALIDE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                 ;}, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_ATTENTE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                ;
                }};
            List<YvsComDocVentes> list = dao.loadNameQueries("YvsComDocVentes.findByTiersTypeStatuts", champ, val);
                equilibrePieceAchatAndPiecesVente(reste, pv, list);
            }
        } else {
            getWarningMessage("La pièce de vente n'a pas été généré !");
        }
    }
    
    public void geratedPiecesCompensation(YvsComptaCaissePieceVente pv) {
        if (pv != null ? !pv.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) : false) {
            //Trouve les pièces d'achats non payé et non compensé en rapport avec le tiers de la facture de vente
            String query = "SELECT y.id FROM yvs_compta_caisse_piece_achat y INNER JOIN yvs_com_doc_achats d ON d.id=y.achat INNER JOIN yvs_base_fournisseur f ON f.id=d.fournisseur "
                    + "WHERE y.statut_piece IN ('W','R') AND f.tiers=? AND y.id NOT IN (SELECT c.achat FROM yvs_compta_caisse_piece_compensation c) "
                    + "ORDER BY y.montant ASC";
            List<Long> ids = dao.loadListBySqlQuery(query, new Options[]{new Options(pv.getVente().getClient().getTiers().getId(), 1)});
            double reste = 0;
            if (!ids.isEmpty()) {
                reste = equilibrePieceVenteAndPiecesAchat(pv, dao.loadNameQueries("YvsComptaCaissePieceAchat.findByIds", new String[]{"ids"}, new Object[]{ids}));
            } else {
                reste = pv.getMontantNonCompense();
            }
            if (reste > 0) {
                //trouve les factures non réglé du tiers et n'ayant pas encore de pièces de règlements généré 
                champ = new String[]{"tiers", "typeDoc", "statut", "statutRegle"};
                val = new Object[]{pv.getVente().getClient().getTiers(), Constantes.TYPE_FA, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_VALIDE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                 ;}, new ArrayList<String>() {
                    {
                        add(Constantes.ETAT_ATTENTE);
                        add(Constantes.ETAT_ENCOURS);
                    }
                ;
                }};
            List<YvsComDocAchats> list = dao.loadNameQueries("YvsComDocAchats.findByTiersTypeStatuts", champ, val);
                equilibrePieceVenteAndPiecesAchat(reste, pv, list);
            }
        } else {
            getWarningMessage("La pièce de vente n'a pas été généré !");
        }
    }
    
    private double equilibrePieceVenteAndPiecesAchat(YvsComptaCaissePieceVente pv, List<YvsComptaCaissePieceAchat> l) {
        double montant = pv.getMontantNonCompense();
        for (YvsComptaCaissePieceAchat pc : l) {
            if (montant > 0) {
                if (pc.getMontant() <= montant) {
                    //mettre en liaison
                    pv.getCompensations().add(createLiaison(pc, pv));
                    montant = montant - pc.getMontant();
                }
            }
        }
        return montant;
    }
    
    private double equilibrePieceAchatAndPiecesVente(YvsComptaCaissePieceAchat pv, List<YvsComptaCaissePieceVente> l) {
        double montant = pv.getMontantNonCompense();
        for (YvsComptaCaissePieceVente pc : l) {
            if (montant > 0) {
                if (pc.getMontant() <= montant) {
                    //mettre en liaison
                    pv.getCompensations().add(createLiaison(pv, pc));
                    montant = montant - pc.getMontant();
                }
            }
        }
        return montant;
    }
    
    private YvsComptaCaissePieceAchat buildPieceAchat(double montant, YvsComptaCaissePieceVente pv, YvsComDocAchats y) {
        YvsComptaCaissePieceAchat re = new YvsComptaCaissePieceAchat();
        re.setAchat(y);
        re.setAuthor(currentUser);
        re.setCaisse(pv.getCaisse());
        re.setCaissier(pv.getCaissier());
        re.setDatePaiement(pv.getDatePaiement());
        re.setDatePaimentPrevu(pv.getDatePaimentPrevu());
        re.setDatePiece(pv.getDatePiece());
        re.setDateSave(new Date());
        re.setDateUpdate(new Date());
        re.setModel(pv.getModel());
        re.setMontant(montant);
        re.setNote("Règlement par compensation avec " + pv.getNumeroPiece());
        re.setReferenceExterne(pv.getNumeroPiece());
        re.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        return re;
        
    }
    
    private YvsComptaCaissePieceVente buildPieceVente(double montant, YvsComptaCaissePieceAchat pv, YvsComDocVentes y) {
        YvsComptaCaissePieceVente re = new YvsComptaCaissePieceVente();
        re.setVente(y);
        re.setAuthor(currentUser);
        re.setCaisse(pv.getCaisse());
        re.setCaissier(pv.getCaissier());
        re.setDatePaiement(pv.getDatePaiement());
        re.setDatePaimentPrevu(pv.getDatePaimentPrevu());
        re.setDatePiece(pv.getDatePiece());
        re.setDateSave(new Date());
        re.setDateUpdate(new Date());
        re.setModel(pv.getModel());
        re.setMontant(montant);
        re.setNote("Règlement par compensation avec " + pv.getNumeroPiece());
        re.setReferenceExterne(pv.getNumeroPiece());
        re.setStatutPiece(Constantes.STATUT_DOC_ATTENTE);
        return re;
        
    }
    
    private double equilibrePieceVenteAndPiecesAchat(Double montant, YvsComptaCaissePieceVente pv, List<YvsComDocAchats> l) {
        double reste;
        ManagedReglementAchat w = (ManagedReglementAchat) giveManagedBean(ManagedReglementAchat.class);
        ManagedReglementVente w1 = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
        for (YvsComDocAchats doc : l) {
            setMontantTotalDoc(doc);
            if (montant > 0) {
                reste = doc.getMontantResteAPlanifier();
                if (reste > 0) {
                    YvsComptaCaissePieceAchat p;
                    if (reste <= montant) {
                        // génère la pièce pour la valeur de @reste    
                        p = buildPieceAchat(reste, pv, doc);
                    } else {
                        // génère la pièce achat pour @montant
                        p = buildPieceAchat(montant, pv, doc);
                    }
                    p = w.createNewPieceCaisse(UtilCom.buildBeanDocAchat(doc), p, false);
                    if (p != null ? p.getId() != null ? p.getId() > 0 : false : false) {
                        w1.getPieceAvance().getCompensations().add(createLiaison(p, pv));
                    }
                }
                montant = montant - reste;
            } else {
                break;
            }
        }
        return montant;
    }
    
    private double equilibrePieceAchatAndPiecesVente(Double montant, YvsComptaCaissePieceAchat pv, List<YvsComDocVentes> l) {
        double reste;
        ManagedReglementVente w = (ManagedReglementVente) giveManagedBean(ManagedReglementVente.class);
        for (YvsComDocVentes doc : l) {
            setMontantTotalDoc(doc);
            if (montant > 0) {
                reste = doc.getMontantResteAPlanifier();
                System.err.println(" --- Num Vente  --- " + doc.getNumDoc() + "Reste " + reste);
                if (reste > 0) {
                    YvsComptaCaissePieceVente p;
                    if (reste <= montant) {
                        // génère la pièce pour la valeur de @reste    
                        p = buildPieceVente(reste, pv, doc);
                    } else {
                        // génère la pièce achat pour @montant
                        p = buildPieceVente(montant, pv, doc);
                    }
                    p = w.createNewPieceCaisse(UtilCom.buildBeanDocVente(doc), p, false);
                    if (p != null ? p.getId() != null ? p.getId() > 0 : false : false) {
                        System.err.println(" Create Liaison");
                        createLiaison(pv, p);
                    }
                }
                montant = montant - reste;
            } else {
                break;
            }
        }
        return montant;
    }
    
    private YvsComptaCaissePieceCompensation createLiaison(YvsComptaCaissePieceAchat achat, YvsComptaCaissePieceVente vente) {
        YvsComptaCaissePieceCompensation c = new YvsComptaCaissePieceCompensation();
        c.setAchat(achat);
        c.setAuthor(currentUser);
        c.setDateSave(new Date());
        c.setDateUpdate(new Date());
        c.setSens('C');
        c.setVente(vente);
        c = (YvsComptaCaissePieceCompensation) dao.save1(c);
        return c;
    }
    
}
