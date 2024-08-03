/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaCaissePieceVente extends AbstractEntity {

    public AYvsComptaCaissePieceVente() {
    }

    public AYvsComptaCaissePieceVente(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComptaCaissePieceVente> controle(YvsComptaCaissePieceVente entity) {
        try {
            YvsComptaCaissePieceVente current = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{entity.getId()});
            if (entity.getMontant() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Le montant est incorrecte !");
            }
            if (entity.getId() > 0 && (entity.getVerouille() && (current != null ? current.getVerouille() : false))) {
                return new ResultatAction<>(false, entity, 0L, "La pièce de règlement est vérouillé !");
            }
            if (entity.getId() > 0 && (entity.getStatutPiece().equals(Constantes.STATUT_DOC_ANNULE) && (current != null ? current.equals(Constantes.STATUT_DOC_ANNULE) : false))) {
                return new ResultatAction<>(false, entity, 0L, "Cette pièce est annulée");
            }
            if (entity.getDatePaimentPrevu() == null) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser la date de paiement !");
            }
            if (entity.getId() > 0 && (entity.getStatutPiece().equals(Constantes.STATUT_DOC_PAYER) && (current != null ? current.equals(Constantes.STATUT_DOC_PAYER) : false))) {
                return new ResultatAction<>(false, entity, 0L, "La pièce en cours est déjà payé !");
            }
            if ((entity.getModel() != null) ? (entity.getModel().getId() != null ? entity.getModel().getId() < 1 : true) : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser indiquer le moyen de paiement !");
            }
            if ((entity.getVente() != null) ? (entity.getVente().getId() != null ? entity.getVente().getId() < 1 : true) : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez  indiquer la facture à regler !");
            }
            if (entity.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                if (entity.getReferenceExterne() != null ? entity.getReferenceExterne().trim().length() < 1 : true) {
                    return new ResultatAction<>(false, entity, 0L, "Vous devez préciser la reference externe !");
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaCaissePieceVente.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComptaCaissePieceVente> save(YvsComptaCaissePieceVente entity) {
        entity.setId(null);
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_compta_caisse_piece_vente", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    YvsBaseCaisse caisse = (YvsBaseCaisse) dao.loadOneByNameQueries("YvsBaseCaisse.findById", new String[]{"id"}, new Object[]{entity.getCaisse().getId()});
                    String reference = dao.genererReference(Constantes.TYPE_PC_VENTE_NAME, entity.getDatePiece(), entity.getCaisse().getId(), caisse.getJournal().getAgence().getSociete(), caisse.getJournal().getAgence());
                    if (!Util.asString(reference)) {
                        return new ResultatAction<>(false, entity, 0L, dao.getRESULT());
                    }
                    entity.setReferenceExterne(entity.getNumeroPiece());
                    entity.setNumeroPiece(reference);
                    entity.setId(null);
                    entity = (YvsComptaCaissePieceVente) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaCaissePieceVente> update(YvsComptaCaissePieceVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaCaissePieceVente) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaCaissePieceVente> delete(YvsComptaCaissePieceVente entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceVente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
