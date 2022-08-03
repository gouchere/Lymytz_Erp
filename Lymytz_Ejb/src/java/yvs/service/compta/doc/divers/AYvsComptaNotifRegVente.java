/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaNotifReglementVente;
import yvs.service.AbstractEntity;
import yvs.service.IEntitySax;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaNotifRegVente extends AbstractEntity {

    public AYvsComptaNotifRegVente() {
    }

    public AYvsComptaNotifRegVente(DaoInterfaceWs dao) {
        this.dao = dao;
    }
    IEntitySax IEntitiSax = new IEntitySax();

    public ResultatAction<YvsComptaNotifReglementVente> controle(YvsComptaNotifReglementVente entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getAcompte() != null ? entity.getAcompte().getId() <= 0 : true) {
                return new ResultatAction<>(false, entity, 0L, "L'acompte n'a pas été trouvé");
            }
            if (entity.getPieceVente() != null ? entity.getPieceVente().getId() <= 0 : true) {
                return new ResultatAction<>(false, entity, 0L, "La pièce de vente n'existe pas");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaNotifRegVente.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComptaNotifReglementVente> save(YvsComptaNotifReglementVente entity) {

        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_compta_notif_reglement_vente", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComptaNotifReglementVente) dao.loadOneByNameQueries("YvsComptaNotifReglementVente.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComptaNotifReglementVente) dao.save1(entity);
                    if (entity != null ? entity.getId() > 0 : false) {
                        return new ResultatAction(true, entity, entity.getId(), "Succès");
                    }
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaNotifRegVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaNotifReglementVente> update(YvsComptaNotifReglementVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaNotifReglementVente) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaNotifRegVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaNotifReglementVente> delete(YvsComptaNotifReglementVente entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaNotifRegVente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
