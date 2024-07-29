/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.param.workflow;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.workflow.YvsWorkflowValidDocStock;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsWorkflowValidDocStock extends AbstractEntity {

    public AYvsWorkflowValidDocStock() {
    }

    public AYvsWorkflowValidDocStock(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsWorkflowValidDocStock> controle(YvsWorkflowValidDocStock entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getDocStock() == null ? true : entity.getDocStock().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez spécifier une facture ");
            }
            if (entity.getEtape() == null ? true : entity.getEtape().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez spécifier une étape ");
            }

            YvsWorkflowValidDocStock t = (YvsWorkflowValidDocStock) dao.loadOneByNameQueries("YvsWorkflowValidDocStock.findByEtapeFacture", new String[]{"facture", "etape"}, new Object[]{entity.getDocStock(), entity.getEtape()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(false, entity, 0L, "Cet élément existe déjà !");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsWorkflowValidDocStock.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsWorkflowValidDocStock> save(YvsWorkflowValidDocStock entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity.setId(null);
                entity = (YvsWorkflowValidDocStock) dao.save1(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidDocStock.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsWorkflowValidDocStock> update(YvsWorkflowValidDocStock entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsWorkflowValidDocStock) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidDocStock.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsWorkflowValidDocStock> delete(YvsWorkflowValidDocStock entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidDocStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
