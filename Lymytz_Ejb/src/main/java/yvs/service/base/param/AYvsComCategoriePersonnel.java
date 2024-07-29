/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.YvsComCategoriePersonnel;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComCategoriePersonnel extends AbstractEntity {

    public AYvsComCategoriePersonnel() {
    }

    public AYvsComCategoriePersonnel(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComCategoriePersonnel> controle(YvsComCategoriePersonnel entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getCode())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le code");
            }
            if (entity.getLibelle() == null || entity.getLibelle().equals("")) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le libelle");
            }
            YvsComCategoriePersonnel t = (YvsComCategoriePersonnel) dao.loadOneByNameQueries("YvsComCategoriePersonnel.findByCurrentCode", new String[]{"code", "societe"}, new Object[]{entity.getCode(), entity.getSociete()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                if (t.getLibelle().equals(entity.getLibelle())) {
                    return new ResultatAction<>(true, t, t.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Vous avez déja crée cette catégorie");
                }

            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCategoriePersonnel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");

    }

    public ResultatAction<YvsComCategoriePersonnel> save(YvsComCategoriePersonnel entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_categorie_personnel", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComCategoriePersonnel) dao.loadOneByNameQueries("YvsComCategoriePersonnel.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComCategoriePersonnel) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCategoriePersonnel.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsComCategoriePersonnel> update(YvsComCategoriePersonnel entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComCategoriePersonnel) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCategoriePersonnel.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComCategoriePersonnel> delete(YvsComCategoriePersonnel entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComCategoriePersonnel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
