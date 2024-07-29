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
import yvs.entity.base.YvsBaseCaisseUser;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseCaisseUser extends AbstractEntity {

    public AYvsBaseCaisseUser() {
    }

    public AYvsBaseCaisseUser(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseCaisseUser> controle(YvsBaseCaisseUser entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getIdCaisse() != null ? entity.getIdCaisse().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "La caisse ne peut pas etre null");
            }

            if (entity.getIdUser() != null ? entity.getIdUser().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "L'utilisateur ne peut pas etre null");
            }

            YvsBaseCaisseUser a = (YvsBaseCaisseUser) dao.loadOneByNameQueries("YvsBaseCaisseUser.findByCaisseUser", new String[]{"caisse", "user"}, new Object[]{entity.getIdCaisse(), entity.getIdUser()});
            if (a != null ? (a.getId() > 0 ? !a.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(true, a, a.getId(), "Succès!", false);
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseCaisseUser.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseCaisseUser> save(YvsBaseCaisseUser entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_caisse_user", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseCaisseUser) dao.loadOneByNameQueries("YvsBaseCaisseUser.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseCaisseUser) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseCaisseUser.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseCaisseUser> update(YvsBaseCaisseUser entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseCaisseUser) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseCaisseUser.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseCaisseUser> delete(YvsBaseCaisseUser entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseCaisseUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
