/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.utilisateur;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.users.YvsNiveauAcces;

import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsNiveauAcces extends AbstractEntity  {

    public AYvsNiveauAcces() {
    }

    public AYvsNiveauAcces(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsNiveauAcces> controle(YvsNiveauAcces entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getDesignation())) {
                return new ResultatAction<>(false, entity, 0L, "La désignation ne peut pas etre vide");
            }
            if ((entity.getSociete() != null) ? entity.getSociete().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "La societe ne peut pas etre null");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsNiveauAcces.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }
    
     public ResultatAction<YvsNiveauAcces> save(YvsNiveauAcces entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_niveau_acces", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsNiveauAcces) dao.loadOneByNameQueries("YvsNiveauAcces.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsNiveauAcces) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsNiveauAcces.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsNiveauAcces> update(YvsNiveauAcces entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsNiveauAcces) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsNiveauAcces.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsNiveauAcces> delete(YvsNiveauAcces entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsNiveauAcces.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
