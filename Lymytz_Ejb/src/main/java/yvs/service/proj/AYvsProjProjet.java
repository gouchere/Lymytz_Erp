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
import yvs.entity.proj.projet.YvsProjProjet;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsProjProjet extends AbstractEntity {

    public AYvsProjProjet() {
    }

    public AYvsProjProjet(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsProjProjet> controle(YvsProjProjet entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getSociete() != null ? entity.getSociete().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "vous devez entrer la societe");
            }
            if (!asString(entity.getCode())) {
                return new ResultatAction<>(false, entity, 0L, "vous devez entrer le code du projet");
            }
            if (!asString(entity.getLibelle())) {
                return new ResultatAction<>(false, entity, 0L, "vous devez entrer le libelle du projet");
            }
            YvsProjProjet t = (YvsProjProjet) dao.loadOneByNameQueries("YvsProjProjet.findByCode", new String[]{"code", "societe"}, new Object[]{entity.getCode(), entity.getSociete()});
            if (t != null ? (t.getId() != null ? !t.getId().equals(entity.getId()) : false) : false) {
                if (t.getLibelle().equals(entity.getLibelle())) {
                    return new ResultatAction<>(true, t, t.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Vous avez déja crée ce projet");
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjProjet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsProjProjet> save(YvsProjProjet entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_proj_projet", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsProjProjet) dao.loadOneByNameQueries("YvsProjProjet.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsProjProjet) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjProjet.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsProjProjet> update(YvsProjProjet entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsProjProjet) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjProjet.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsProjProjet> delete(YvsProjProjet entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsProjProjet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
