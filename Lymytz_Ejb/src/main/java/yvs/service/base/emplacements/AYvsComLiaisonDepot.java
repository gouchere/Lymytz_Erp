/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.emplacements;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.YvsComLiaisonDepot;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComLiaisonDepot extends AbstractEntity {

    public AYvsComLiaisonDepot() {
    }

    public AYvsComLiaisonDepot(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComLiaisonDepot> controle(YvsComLiaisonDepot entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getDepot() != null ? entity.getDepot().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez sélectionner un dépot");
            }
            if (entity.getDepotLier() != null ? entity.getDepotLier().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez sélectionner un dépot lier");
            }
            YvsComLiaisonDepot t = (YvsComLiaisonDepot) dao.loadOneByNameQueries("YvsComLiaisonDepot.findByOne", new String[]{"depotLier", "depot"}, new Object[]{entity.getDepotLier(), entity.getDepot()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(true, t, t.getId(), "Succès", false);
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsComLiaisonDepot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComLiaisonDepot> save(YvsComLiaisonDepot entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_liaison_depot", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComLiaisonDepot) dao.loadOneByNameQueries("YvsComLiaisonDepot.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComLiaisonDepot) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComLiaisonDepot.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsComLiaisonDepot> update(YvsComLiaisonDepot entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComLiaisonDepot) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComLiaisonDepot.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComLiaisonDepot> delete(YvsComLiaisonDepot entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComLiaisonDepot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
