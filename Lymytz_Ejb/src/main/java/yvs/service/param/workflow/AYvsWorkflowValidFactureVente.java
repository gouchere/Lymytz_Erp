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
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsWorkflowValidFactureVente extends AbstractEntity {

    public AYvsWorkflowValidFactureVente() {
    }

    public AYvsWorkflowValidFactureVente(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsWorkflowValidFactureVente> controle(YvsWorkflowValidFactureVente entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }

            if (entity.getFactureVente() == null ? true : entity.getFactureVente().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez spécifier une facture ");
            }
            if (entity.getEtape() == null ? true : entity.getEtape().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez spécifier une étape ");
            }

            YvsWorkflowValidFactureVente t = (YvsWorkflowValidFactureVente) dao.loadOneByNameQueries("YvsWorkflowValidFactureVente.findByEtapeFacture", new String[]{"facture", "etape"}, new Object[]{entity.getFactureVente(), entity.getEtape()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(false, entity, 0L, "Cet élément existe déjà !");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsWorkflowValidFactureVente.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsWorkflowValidFactureVente> save(YvsWorkflowValidFactureVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_workflow_valid_facture_vente", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsWorkflowValidFactureVente) dao.loadOneByNameQueries("YvsWorkflowValidFactureVente.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsWorkflowValidFactureVente) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidFactureVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsWorkflowValidFactureVente> update(YvsWorkflowValidFactureVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsWorkflowValidFactureVente) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidFactureVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsWorkflowValidFactureVente> delete(YvsWorkflowValidFactureVente entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsWorkflowValidFactureVente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
