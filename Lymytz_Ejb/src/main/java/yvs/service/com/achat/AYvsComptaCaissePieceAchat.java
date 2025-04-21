/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.achat;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaCaissePieceAchat;
import yvs.service.AbstractEntity;
import yvs.service.com.vente.AYvsComptaCaissePieceVente;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaCaissePieceAchat extends AbstractEntity {
    
    public AYvsComptaCaissePieceAchat() {
    }
    
    public AYvsComptaCaissePieceAchat(DaoInterfaceLocal dao) {
        this.dao = dao;
    }
    
    public ResultatAction<YvsComptaCaissePieceAchat> controle(YvsComptaCaissePieceAchat entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            
            if (entity.getId() > 0 && entity.getStatutPiece().equals(Constantes.STATUT_DOC_ANNULE)) {
                return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez modifier cette pièce de règlement,");
                
            }
            
            if (entity.getDatePaimentPrevu() == null) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser la date de paiement !");
                
            }
            if (entity.getStatutPiece() == Constantes.STATUT_DOC_PAYER && (entity.getId() != null) ? entity.getId() > 0 : false) {
                return new ResultatAction<>(false, entity, 0L, "La pièce en cours est déjà payé !");
                
            }
            if ((entity.getModel() != null) ? (entity.getModel().getId() != null ? entity.getModel().getId() < 1 : true) : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser indiquer le moyen de paiement !");
                
            }
            if ((entity.getAchat() != null) ? (entity.getAchat().getId() != null ? entity.getAchat().getId() < 1 : true) : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez  indiquer la facture à regler !");
                
            }
            if (entity.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                if (entity.getReferenceExterne() != null ? entity.getReferenceExterne().trim().length() < 1 : true) {
                    return new ResultatAction<>(false, entity, 0L, "Vous devez préciser la reference externe !");
                    
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaCaissePieceAchat.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }
    
    public ResultatAction<YvsComptaCaissePieceAchat> save(YvsComptaCaissePieceAchat entity) {
        entity.setId(null);
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_compta_caisse_piece_achat", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComptaCaissePieceAchat) dao.loadOneByNameQueries("YvsComptaCaissePieceAchat.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComptaCaissePieceAchat) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceAchat.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }
    
    public ResultatAction<YvsComptaCaissePieceAchat> update(YvsComptaCaissePieceAchat entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaCaissePieceAchat) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceAchat.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }
    
    public ResultatAction<YvsComptaCaissePieceAchat> delete(YvsComptaCaissePieceAchat entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
}
