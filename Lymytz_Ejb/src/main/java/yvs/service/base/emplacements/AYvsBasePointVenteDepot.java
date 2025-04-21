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
import yvs.entity.base.YvsBasePointVenteDepot;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBasePointVenteDepot extends AbstractEntity {

    public AYvsBasePointVenteDepot() {
    }

    public AYvsBasePointVenteDepot(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBasePointVenteDepot> controle(YvsBasePointVenteDepot entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getDepot() != null ? entity.getDepot().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner un depot");
            }
            if (entity.getPointVente() != null ? entity.getPointVente().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez selectionner un point de vente");
            }
            YvsBasePointVenteDepot y = (YvsBasePointVenteDepot) dao.loadOneByNameQueries("YvsBasePointVenteDepot.findByOne", new String[]{"depot", "pointVente"}, new Object[]{entity.getDepot(), entity.getPointVente()});
            if (y != null ? y.getId() > 0 ? !y.getId().equals(entity.getId()) : false : false) {
                return new ResultatAction<>(true, y, y.getId(), "Succès", false);
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePointVenteDepot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsBasePointVenteDepot> save(YvsBasePointVenteDepot entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_point_vente_depot", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBasePointVenteDepot) dao.loadOneByNameQueries("YvsBasePointVenteDepot.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBasePointVenteDepot) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePointVenteDepot.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBasePointVenteDepot> update(YvsBasePointVenteDepot entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBasePointVenteDepot) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePointVenteDepot.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBasePointVenteDepot> delete(YvsBasePointVenteDepot entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePointVenteDepot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
