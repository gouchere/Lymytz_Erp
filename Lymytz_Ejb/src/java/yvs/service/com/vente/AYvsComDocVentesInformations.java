/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComDocVentesInformations;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComDocVentesInformations extends AbstractEntity {

    public AYvsComDocVentesInformations() {
    }

    public AYvsComDocVentesInformations(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComDocVentesInformations> controle(YvsComDocVentesInformations entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getFacture() != null) ? entity.getFacture().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "La facture ne peut pas etre null");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComDocVentesInformations.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComDocVentesInformations> save(YvsComDocVentesInformations entity) {

        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity.setId(null);
                entity = (YvsComDocVentesInformations) dao.save1(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocVentesInformations.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComDocVentesInformations> update(YvsComDocVentesInformations entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComDocVentesInformations) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocVentesInformations.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComDocVentesInformations> delete(YvsComDocVentesInformations entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComDocVentesInformations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
