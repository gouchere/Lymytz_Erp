/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.proj;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.proj.YvsProjDepartement;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsProjDepartement extends AbstractEntity {

    public AYvsProjDepartement() {
    }

    public AYvsProjDepartement(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsProjDepartement> controle(YvsProjDepartement entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getService() != null ? entity.getService().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "vous devez entrer le service");
            }
            YvsProjDepartement t = (YvsProjDepartement) dao.loadOneByNameQueries("YvsProjDepartement.findByService", new String[]{"service"}, new Object[]{entity.getService()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(false, entity, 0L, "Vous avez déja crée ce departement");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjDepartement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsProjDepartement> save(YvsProjDepartement entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_proj_departement", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsProjDepartement) dao.loadOneByNameQueries("YvsProjDepartement.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsProjDepartement) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjDepartement.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsProjDepartement> update(YvsProjDepartement entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsProjDepartement) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjDepartement.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsProjDepartement> delete(YvsProjDepartement entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjDepartement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
