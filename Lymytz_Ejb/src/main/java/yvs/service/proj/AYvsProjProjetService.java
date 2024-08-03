/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.proj;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.proj.projet.YvsProjProjetService;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsProjProjetService extends AbstractEntity {

    public AYvsProjProjetService() {
    }

    public AYvsProjProjetService(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsProjProjetService> controle(YvsProjProjetService entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getService() != null ? entity.getService().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "vous devez entrer le service");
            }
            if (entity.getProjet() != null ? entity.getProjet().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "vous devez entrer le projet");
            }
            YvsProjProjetService t = (YvsProjProjetService) dao.loadOneByNameQueries("YvsProjProjetService.findOne", new String[]{"service", "projet"}, new Object[]{entity.getService(), entity.getProjet()});
            if (t != null ? (t.getId() != null ? !t.getId().equals(entity.getId()) : false) : false) {
                 return new ResultatAction<>(false, entity, 0L, "Vous avez déja crée ce service dans le projet");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjProjetService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsProjProjetService> save(YvsProjProjetService entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_proj_projet_service", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsProjProjetService) dao.loadOneByNameQueries("YvsProjProjetService.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsProjProjetService) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjProjetService.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsProjProjetService> update(YvsProjProjetService entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsProjProjetService) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjProjetService.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsProjProjetService> delete(YvsProjProjetService entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjProjetService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
