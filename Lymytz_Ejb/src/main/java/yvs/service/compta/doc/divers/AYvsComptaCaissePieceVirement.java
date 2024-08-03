/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.compta.vente.YvsComptaNotifVersementVente;
import yvs.service.AbstractEntity;
import yvs.service.IEntitySax;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaCaissePieceVirement extends AbstractEntity {

    public AYvsComptaCaissePieceVirement() {
    }

    public AYvsComptaCaissePieceVirement(DaoInterfaceLocal dao) {
        this.dao = dao;
    }
    IEntitySax IEntitiSax = new IEntitySax();
    String[] champ;
    Object[] val;

    public ResultatAction<YvsComptaCaissePieceVirement> controle(YvsComptaCaissePieceVirement entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getSource() != null ? entity.getSource().getId() <= 0 : true) {
                return new ResultatAction<>(false, entity, 0L, "Aucune caisse source n'a été trouvé !");
            }
            if (entity.getSource().getJournal() != null ? entity.getSource().getJournal().getId() <= 0 : true) {
                return new ResultatAction<>(false, entity, 0L, "Aucun journal de caisse source n'a été trouvé !");
            }
            if (entity.getSource().getJournal().getAgence() != null ? entity.getSource().getJournal().getAgence().getId() <= 0 : true) {
                return new ResultatAction<>(false, entity, 0L, "Aucune agence de caisse source n'a été trouvé !");
            }
            if (entity.getCible() != null ? entity.getCible().getId() <= 0 : true) {
                return new ResultatAction<>(false, entity, 0L, "Aucune caisse cible n'a été trouvé !");
            }
            if (entity.getCaissierSource() != null ? entity.getCaissierSource().getId() <= 0 : true) {
                return new ResultatAction<>(false, entity, 0L, "Aucun caissier source n'a été trouvé !");
            }
            if (entity.getModel() != null ? entity.getModel().getId() <= 0 : false) {
                return new ResultatAction<>(false, entity, 0L, "Aucun model de paiement n'a été trouvé !");
            }
            if (entity.getMontant() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le montant !");
            }
            if (entity.getNumeroPiece() == null || entity.getNumeroPiece().trim().length() < 1) {
                String ref = dao.genererReference(Constantes.TYPE_PT_NAME, entity.getDatePaimentPrevu(), entity.getSource().getId(), entity.getSource().getJournal().getAgence().getSociete(), entity.getSource().getJournal().getAgence());
                if ((ref != null) ? ref.trim().equals("") : true) {
                    return new ResultatAction<>(false, entity, 0L, "Le model de reference n'est pas paramètré!");
                }
                entity.setNumeroPiece(ref);
            }
            if (!dao.controleEcartVente(entity.getCaissierSource().getId(), entity.getDatePaimentPrevu())) {
                return new ResultatAction<>(false, entity, 0L, "Vous ne pouvez créer une fiche de vente à cette date car un ou plusieurs manquants ont déjà été réalisés après pour ce vendeur");
            }
            if (entity.getCaissierCible() != null ? entity.getCaissierCible().getId() <= 0 : true) {
                entity.setCaissierCible(entity.getCible().getCaissier());
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaCaissePieceVirement.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComptaCaissePieceVirement> save(YvsComptaCaissePieceVirement entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = null;
                if (Util.asString(entity.getAdresseServeur()) && !Util.isLocalhost(entity.getAdresseServeur())) {
                    local = getLocalCurrent(entity.getIdDistant(), "yvs_compta_caisse_piece_virement", entity.getAdresseServeur());
                }
                if (local != null ? local > 0 : false) {
                    entity = (YvsComptaCaissePieceVirement) dao.loadOneByNameQueries("YvsComptaCaissePieceVirement.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComptaCaissePieceVirement) dao.save1(entity);
                    if (entity != null ? entity.getId() > 0 : false) {
                        saveVersement(entity);
                    }
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceVirement.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaCaissePieceVirement> update(YvsComptaCaissePieceVirement entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaCaissePieceVirement) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    saveVersement(entity);
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCaissePieceVirement.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaCaissePieceVirement> delete(YvsComptaCaissePieceVirement entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaTaxeDocDivers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private boolean saveVersement(YvsComptaCaissePieceVirement entity) {
        try {
            if (entity != null ? entity.getId() > 0 : false) {
                if (entity.getHeader() != null ? entity.getHeader().getId() > 0 : false) {
                    YvsComptaNotifVersementVente y = (YvsComptaNotifVersementVente) dao.loadOneByNameQueries("YvsComptaNotifVersementVente.findOne", new String[]{"piece", "enteteDoc"}, new Object[]{entity, new YvsComEnteteDocVente(entity.getHeader().getId())});
                    if (y != null ? y.getId() > 0 : false) {
                        return true;
                    }
                    y = new YvsComptaNotifVersementVente(new YvsComEnteteDocVente(entity.getHeader().getId()), new YvsComptaCaissePieceVirement(entity.getId()), entity.getAuthor());
                    dao.save(y);
                    entity.setVersement(y);
                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaTaxeDocDivers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
