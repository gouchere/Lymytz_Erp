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
import yvs.entity.param.workflow.YvsWorkflowValidDocCaisse;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsWorkflowValidDocCaisse extends AbstractEntity {

    public AYvsWorkflowValidDocCaisse() {
    }

    public AYvsWorkflowValidDocCaisse(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsWorkflowValidDocCaisse> controle(YvsWorkflowValidDocCaisse entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getDocCaisse() == null ? true : entity.getDocCaisse().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez spécifier une facture ");
            }
            if (entity.getEtape() == null ? true : entity.getEtape().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez spécifier une étape ");
            }
            YvsWorkflowValidDocCaisse t = (YvsWorkflowValidDocCaisse) dao.loadOneByNameQueries("YvsWorkflowValidDocCaisse.findByEtapeFacture", new String[]{"facture", "etape"}, new Object[]{entity.getDocCaisse(), entity.getEtape()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(false, entity, 0L, "Cet élément existe déjà !");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsWorkflowValidDocCaisse.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }
    
     public ResultatAction<YvsWorkflowValidDocCaisse> save(YvsWorkflowValidDocCaisse entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity.setId(null);
                entity = (YvsWorkflowValidDocCaisse) dao.save1(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidDocCaisse.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsWorkflowValidDocCaisse> update(YvsWorkflowValidDocCaisse entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsWorkflowValidDocCaisse) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidDocCaisse.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsWorkflowValidDocCaisse> delete(YvsWorkflowValidDocCaisse entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidDocCaisse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
