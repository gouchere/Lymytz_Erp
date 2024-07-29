/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.comptabilite;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsBaseLiaisonCaisse;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseLiaisonCaisse extends AbstractEntity {

    public AYvsBaseLiaisonCaisse() {
    }

    public AYvsBaseLiaisonCaisse(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseLiaisonCaisse> controle(YvsBaseLiaisonCaisse entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getCaisseCible() != null ? entity.getCaisseCible().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "La caisse cible ne peut pas etre null");
            }

            if (entity.getCaisseSource() != null ? entity.getCaisseSource().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "La caisse source ne peut pas etre null");
            }
            if (entity.getCaisseSource().equals(entity.getCaisseCible())) {
                return new ResultatAction<>(false, entity, 0L, "La caisse source ne peut pas etre la caisse cible");
            }
            YvsBaseLiaisonCaisse a = (YvsBaseLiaisonCaisse) dao.loadOneByNameQueries("YvsBaseLiaisonCaisse.findByCaisse", new String[]{"caisseCible", "caisseSource"}, new Object[]{entity.getCaisseCible(), entity.getCaisseSource()});
            if (a != null ? (a.getId() > 0 ? !a.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(true, a, a.getId(), "Succès", false);
            }

            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseLiaisonCaisse.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseLiaisonCaisse> save(YvsBaseLiaisonCaisse entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_liaison_caisse", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseLiaisonCaisse) dao.loadOneByNameQueries("YvsBaseLiaisonCaisse.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseLiaisonCaisse) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseLiaisonCaisse.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseLiaisonCaisse> update(YvsBaseLiaisonCaisse entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseLiaisonCaisse) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseLiaisonCaisse.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseLiaisonCaisse> delete(YvsBaseLiaisonCaisse entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseLiaisonCaisse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
