/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.compta;

import java.util.Date;
import java.util.List;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.local.UtilsBean;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.GenericService;
import yvs.dao.salaire.service.ResultatAction;
import yvs.dao.services.commercial.ServiceClotureVente;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.vente.YvsComptaNotifVersementVente;
import yvs.entity.users.YvsBaseUsersAcces;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
public class ServiceMvtCaisse extends GenericService {

    public ServiceMvtCaisse() {
    }

    public ServiceMvtCaisse(DaoInterfaceLocal dao, YvsNiveauAcces niveau, YvsUsersAgence user) {
        this.dao = dao;
        this.niveau = niveau;
        this.currentUser = user;
    }

    public ResultatAction controleFiche(YvsComptaCaissePieceVirement bean) {
        if (bean.getSource() != null ? bean.getSource().getId() <= 0 : true) {
            return result.emptyCaisse(true);
        }
        if (bean.getCible() != null ? bean.getCible().getId() <= 0 : true) {
            return result.emptyCaisse(false);
        }
        if (bean.getModel() != null ? bean.getModel().getId() <= 0 : true) {
            return result.emptyModeReglement();
        }
        if (bean.getMontant() <= 0) {
            return result.errorMontant();
        }
        if (bean.getSource().equals(bean.getCible())) {
            return result.errorCaisse();
        }
//        if (bean.get().isGiveBilletage()) {
//            double piece = soeBielletage(bean.getBielletagePiece());
//            double billet = soeBielletage(bean.getBielletageBillet());
//            if (piece + billet <= 0) {
//                return result.requireBielletage();
//            } else {
//                if (bean.getMontant() != (billet + piece)) {
//                    return result.errorEquilibreBielletage();
//                }
//            }
//        }
        if ((bean != null ? bean.getId() > 0 ? !bean.getDatePaimentPrevu().equals(bean.getDatePaimentPrevu()) : false : false)
                || (bean.getNumeroPiece() == null || bean.getNumeroPiece().trim().length() < 1)) {
            UtilsBean util = new UtilsBean(null, dao);
            String ref = util.genererReference(Constantes.TYPE_PT_NAME, bean.getDatePaimentPrevu(), bean.getSource().getId(), Constantes.CAISSE, "", currentUser.getAgence());
            if ((ref != null) ? ref.trim().equals("") : true) {
                return result.numDocNotGenerated();
            }
            bean.setNumeroPiece(ref);
        }
        return result.succes();
    }

    public ResultatAction controleAccesCaisse(YvsBaseCaisse caisse, boolean msg) {
        if (caisse != null ? caisse.getId() < 1 : true) {
            return result.emptyCaisse(true);
        }
        caisse = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{caisse.getId()});
        if (currentUser.getUsers() != null ? currentUser.getUsers().getId() < 1 : true) {
            return result.emptyUser();
        }
        String error = null;
        if (currentUser.getUsers().getEmploye() != null ? currentUser.getUsers().getEmploye().getId() > 0 : false) {
            if (caisse.getResponsable() != null ? caisse.getResponsable().getId() > 0 : false) {
                if (!caisse.getResponsable().equals(currentUser.getUsers().getEmploye())) {
                    error = result.notAccesCaisse().getMessage();
                } else {
                    return result.succes();
                }
            }
        }
        if (caisse.getCaissier() != null ? caisse.getCaissier().getId() > 0 : false) {
            if (!caisse.getCaissier().equals(currentUser.getUsers())) {
                error = result.notAccesCaisse().getMessage();
            } else {
                return result.succes();
            }
        }
        if (caisse.getCodeAcces() != null ? caisse.getCodeAcces().getId() < 1 : true) {
            if (!autoriser("caiss_create_piece")) {
                return result.notAccesCaisse();
            }
        } else {
            YvsBaseUsersAcces acces = (YvsBaseUsersAcces) dao.loadOneByNameQueries("YvsBaseUsersAcces.findOne", new String[]{"users", "code"}, new Object[]{currentUser.getUsers(), caisse.getCodeAcces()});
            if (acces != null ? acces.getId() < 1 : true) {
                return result.notAccesCaisse();
            }
        }
        return result.succes();
    }

    public ResultatAction saveNewVirement(YvsComptaCaissePieceVirement entity, YvsComEnteteDocVente header) {
        ResultatAction ra = controleFiche(entity);
        if (ra.isResult()) {
            if (header == null) {
                return result.emptyDoc();
            }
            ra = controleAccesCaisse(entity.getSource(), true);
            if (!ra.isResult()) {
                return new ResultatAction().fail("Enregistrement impossible");
            }
            if (entity.getId() <= 0) {
                List<YvsComptaParametre> l = dao.loadNameQueries("YvsComptaParametre.findAll", new String[]{"societe"}, new Object[]{niveau.getSociete()}, 0, 1);
                YvsComptaParametre param = l != null ? !l.isEmpty() ? l.get(0) : null : null;
                entity.setId(null);
                entity = (YvsComptaCaissePieceVirement) dao.save1(entity);
                YvsComptaNotifVersementVente notif = new YvsComptaNotifVersementVente();
                notif.setAuthor(currentUser);
                notif.setDateSave(new Date());
                notif.setDateUpdate(new Date());
                notif.setEnteteDoc(header);
                notif.setPiece(entity);

                entity.setStatutPiece(Constantes.STATUT_DOC_SOUMIS);
                entity.setAuthor(currentUser);
                entity.setDateUpdate(new Date());
                dao.update(entity);
                ServiceComptabilite w = new ServiceComptabilite(niveau, currentUser, dao);
                if (w != null) {
                    w.comptabiliserCaisseVirement(entity, param != null ? param.getComptaPartielVirement() : true);
                }

                dao.save(notif);
            } else {
                dao.update(entity);
            }
            result.setIdEntity(entity.getId());
        }
        return result.succes();
    }

    public ResultatAction saveNewVirement(YvsComptaCaissePieceVirement entity, YvsComEnteteDocVente header, double versementAttendu) {
        ResultatAction ra = saveNewVirement(entity, header);
        if (ra.isResult()) {
            ServiceClotureVente sc = new ServiceClotureVente(dao, niveau, currentUser, currentUser.getAgence().getSociete());
//            ra = sc.saveNew(header, header.getDateEntete(), entity.getSource().getId(), versementAttendu);
//            YvsComEcartEnteteVente ecart = new YvsComEcartEnteteVente();
//            ecart.setAuthor(currentUser);
//            ecart.setDateEcart(entity.getDatePaimentPrevu());
//            ecart.setDateSave(new Date());
//            ecart.setDateUpdate(new Date());
//            ecart.setEnteteDoc(header);
//            ecart.setMontant(versementAttendu - entity.getMontant());
//            ecart.setStatut(Constantes.STATUT_DOC_ATTENTE);
//            ecart.setUsers(header.getCreneau().getUsers());
//            
//            ecart = (YvsComEcartEnteteVente) dao.save1(ecart);
//            //Place le manquant
//            YvsComptaCaissePieceEcartVente piece = new YvsComptaCaissePieceEcartVente();
//             piece.setAuthor(currentUser);
//            piece.setCaisse(entity.getSource());
//            piece.setDateSave(new Date());
//            piece.setDateUpdate(new Date());
//            piece.setModel(piece.getModel());
//            piece.setPiece(ecart);
//            dao.save(piece);
            //Equilibrer les ecarts 
            return ra;
        } else {
            return ra;
        }
    }
}
