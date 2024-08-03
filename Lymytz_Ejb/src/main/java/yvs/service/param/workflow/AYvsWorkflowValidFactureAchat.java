/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.param.workflow;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.workflow.YvsWorkflowValidFactureAchat;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsWorkflowValidFactureAchat extends AbstractEntity {

    public AYvsWorkflowValidFactureAchat() {
    }

    public AYvsWorkflowValidFactureAchat(DaoInterfaceLocal dao) {
        this.dao = dao;
    }
    public ResultatAction<YvsWorkflowValidFactureAchat> controle(YvsWorkflowValidFactureAchat entity) {
        try {
             if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getFactureAchat()== null ? true : entity.getFactureAchat().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez spécifier une facture ");
            }
            if (entity.getEtape() == null ? true : entity.getEtape().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez spécifier une étape ");
            }

            YvsWorkflowValidFactureAchat t = (YvsWorkflowValidFactureAchat) dao.loadOneByNameQueries("YvsWorkflowValidFactureAchat.findByEtapeFacture", new String[]{"facture", "etape"}, new Object[]{entity.getFactureAchat(), entity.getEtape()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(false, entity, 0L, "Cet élément existe déjà !");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsWorkflowValidFactureVente.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }
    
      public ResultatAction<YvsWorkflowValidFactureAchat> save(YvsWorkflowValidFactureAchat entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity.setId(null);
                entity = (YvsWorkflowValidFactureAchat) dao.save1(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidFactureAchat.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsWorkflowValidFactureAchat> update(YvsWorkflowValidFactureAchat entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsWorkflowValidFactureAchat) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidFactureAchat.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsWorkflowValidFactureAchat> delete(YvsWorkflowValidFactureAchat entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidFactureAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
