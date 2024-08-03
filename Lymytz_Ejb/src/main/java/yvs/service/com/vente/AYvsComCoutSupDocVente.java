/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComCoutSupDocVente;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComCoutSupDocVente extends AbstractEntity {

    public AYvsComCoutSupDocVente() {
    }

    public AYvsComCoutSupDocVente(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComCoutSupDocVente> controle(YvsComCoutSupDocVente entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getDocVente() != null) ? entity.getDocVente().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "La facture ne peut pas etre null");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComCoutSupDocVente.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComCoutSupDocVente> save(YvsComCoutSupDocVente entity) {

        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity.setId(null);
                entity = (YvsComCoutSupDocVente) dao.save1(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCoutSupDocVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComCoutSupDocVente> update(YvsComCoutSupDocVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComCoutSupDocVente) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCoutSupDocVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComCoutSupDocVente> delete(YvsComCoutSupDocVente entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCoutSupDocVente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
