/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.achat.YvsComptaAcompteFournisseur;
import yvs.service.AbstractEntity;
import yvs.service.IEntitySax;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaAcompteFournisseur extends AbstractEntity {

    public AYvsComptaAcompteFournisseur() {
    }

    public AYvsComptaAcompteFournisseur(DaoInterfaceLocal dao) {
        this.dao = dao;
    }
    IEntitySax IEntitiSax = new IEntitySax();

    public ResultatAction<YvsComptaAcompteFournisseur> controle(YvsComptaAcompteFournisseur entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getFournisseur()!= null ? entity.getFournisseur().getId() <= 0 : true) {
                return new ResultatAction<>(false, entity, 0L, "Aucune fournisseur n'a été trouvé !");
            }
            if (entity.getCaisse() != null ? entity.getCaisse().getId() <= 0 : true) {
                return new ResultatAction<>(false, entity, 0L, "Aucune caisse n'a été trouvé !");
            }
            if (entity.getMontant() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous n'avez montant !");
            }
            if (entity.getModel() != null ? entity.getModel().getId() <= 0 : false) {
                return new ResultatAction<>(false, entity, 0L, "Aucun model de règlement n'a été trouvé!");
            }
            if ((entity.getStatut() == Constantes.STATUT_DOC_PAYER) && entity.getId() > 0) {
                return new ResultatAction<>(false, entity, 0L, "La pièce d'acompte est déjà payé !");
            }
            if (!entity.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE) && !entity.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_ESPECE)) {
                return new ResultatAction<>(false, entity, 0L, "les acompte ne peuvent  être payé qu'en espèce ou par un mode banque !");
            }
            if (entity.getId() > 0 || !Util.asString(entity.getNumRefrence())) {
                String ref = dao.genererReference(Constantes.TYPE_PT_AVANCE_VENTE, entity.getDateAcompte(), entity.getCaisse().getId(), entity.getModel().getSociete(), currentAgence);
                if ((ref != null) ? ref.trim().equals("") : true) {
                    return new ResultatAction<>(false, entity, 0L, "Le numéro de référence n'a pas été généré !");
                }
                entity.setNumRefrence(ref);
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaAcompteFournisseur.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComptaAcompteFournisseur> save(YvsComptaAcompteFournisseur entity) {

        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_compta_acompte_fournisseur", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComptaAcompteFournisseur) dao.loadOneByNameQueries("YvsComptaAcompteFournisseur.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity.getNotifs().clear();
                    entity.getPhasesReglement().clear();
                    entity = (YvsComptaAcompteFournisseur) dao.save1(entity);
                    if (entity != null ? entity.getId() > 0 : false) {
                        return new ResultatAction(true, entity, entity.getId(), "Succès");
                    }
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaAcompteFournisseur.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaAcompteFournisseur> update(YvsComptaAcompteFournisseur entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaAcompteFournisseur) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaAcompteFournisseur.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaAcompteFournisseur> delete(YvsComptaAcompteFournisseur entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaAcompteFournisseur.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static Double findResteForAcompte(YvsComptaAcompteFournisseur y, DaoInterfaceLocal dao) {
        String query = "SELECT a.montant - COALESCE(SUM(p.montant), 0) FROM yvs_compta_notif_reglement_achat y RIGHT JOIN yvs_compta_acompte_fournisseur a ON y.acompte = a.id LEFT JOIN yvs_compta_caisse_piece_achat p ON (y.piece_achat = p.id AND p.statut_piece = 'P') WHERE a.id = ? GROUP BY a.id";
        return (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(y.getId(), 1)});
    }

    public static Double findResteUnBindForAcompte(YvsComptaAcompteFournisseur y, DaoInterfaceLocal dao) {
        String query = "SELECT a.montant - COALESCE(SUM(p.montant), 0) FROM yvs_compta_notif_reglement_achat y RIGHT JOIN yvs_compta_acompte_fournisseur a ON y.acompte = a.id LEFT JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id WHERE a.id = ? GROUP BY a.id";
        return (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(y.getId(), 1)});
    }

    public static Double findResteUnBindForAcompteAndNotPiece(YvsComptaAcompteFournisseur y, Long piece, DaoInterfaceLocal dao) {
        String query = "SELECT a.montant - COALESCE(SUM(p.montant), 0) FROM yvs_compta_notif_reglement_achat y RIGHT JOIN yvs_compta_acompte_fournisseur a ON y.acompte = a.id LEFT JOIN yvs_compta_caisse_piece_achat p ON y.piece_achat = p.id WHERE a.id = ? AND p.id = ? GROUP BY a.id";
        return (Double) dao.loadObjectBySqlQuery(query, new Options[]{new Options(y.getId(), 1), new Options(piece, 2)});
    }

}
