/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.param;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.param.YvsAgences;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBasePointVente extends AbstractEntity {

    public AYvsBasePointVente() {
    }

    public AYvsBasePointVente(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBasePointVente> controle(YvsBasePointVente entity) {
        try {
            if (entity.getCode() == null || entity.getCode().trim().equals("")) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer un code!");
            }
            if (entity.getLibelle() == null || entity.getLibelle().trim().equals("")) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer un libelle!");
            }
            if (entity.getAgence() != null ? entity.getAgence().getSociete() != null : false) {
                YvsBasePointVente t = (YvsBasePointVente) dao.loadOneByNameQueries("YvsBasePointVente.findByCode", new String[]{"code", "societe"}, new Object[]{entity.getCode(), entity.getAgence().getSociete()});
                if (t != null ? t.getId() > 0 : false) {
                    return new ResultatAction<YvsBasePointVente>(true, entity, 0L, "Succès", false);
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePointVente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsBasePointVente> save(YvsBasePointVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_point_vente", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBasePointVente) dao.loadOneByNameQueries("YvsBasePointVente.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBasePointVente) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePointVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBasePointVente> update(YvsBasePointVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBasePointVente) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePointVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBasePointVente> delete(YvsBasePointVente entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBasePointVente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
