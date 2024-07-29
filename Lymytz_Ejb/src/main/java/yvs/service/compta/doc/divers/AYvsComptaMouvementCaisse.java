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
import yvs.entity.compta.YvsComptaMouvementCaisse;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaMouvementCaisse extends AbstractEntity {

    public AYvsComptaMouvementCaisse() {
    }

    public AYvsComptaMouvementCaisse(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComptaMouvementCaisse> controle(YvsComptaMouvementCaisse entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getAgence() != null ? entity.getAgence().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Aucune agence n'a été trouvé !");
            }
            if (entity.getMontant() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le montant !");
            }
            if (entity.getCaisse() != null ? entity.getCaisse().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "La caisse ne peut pas être null");
           }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaMouvementCaisse.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComptaMouvementCaisse> save(YvsComptaMouvementCaisse entity) {

        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_compta_mouvement_caisse", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComptaMouvementCaisse) dao.loadOneByNameQueries("YvsComptaMouvementCaisse.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComptaMouvementCaisse) dao.save1(entity);
                    if (entity != null ? entity.getId() > 0 : false) {
                        return new ResultatAction(true, entity, entity.getId(), "Succès");
                    }
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaMouvementCaisse.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaMouvementCaisse> update(YvsComptaMouvementCaisse entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaMouvementCaisse) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaMouvementCaisse.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaMouvementCaisse> delete(YvsComptaMouvementCaisse entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaMouvementCaisse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
