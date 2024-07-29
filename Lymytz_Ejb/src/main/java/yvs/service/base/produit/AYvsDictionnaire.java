/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.produit;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.param.YvsDictionnaire;
import yvs.service.AbstractEntity;
import yvs.service.base.utilisateur.AYvsUsers;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsDictionnaire extends AbstractEntity {

    public AYvsDictionnaire() {
    }

    public AYvsDictionnaire(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsDictionnaire> controle(YvsDictionnaire entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getTitre() != null ? entity.getTitre().trim().isEmpty() : true) {
                return new ResultatAction<>(false, entity, 0L, "Chosissez un titre");
            }
            if (entity.getLibele() != null ? entity.getLibele().trim().isEmpty() : true) {
                return new ResultatAction<>(false, entity, 0L, "Chosissez un libelle");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsDictionnaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsDictionnaire> save(YvsDictionnaire entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                 Long local = getLocalCurrent(entity.getIdDistant(), "yvs_dictionnaire", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsDictionnaire) dao.loadOneByNameQueries("YvsDictionnaire.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsDictionnaire) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsDictionnaire.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsDictionnaire> update(YvsDictionnaire entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsDictionnaire) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsDictionnaire.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsDictionnaire> delete(YvsDictionnaire entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsDictionnaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
