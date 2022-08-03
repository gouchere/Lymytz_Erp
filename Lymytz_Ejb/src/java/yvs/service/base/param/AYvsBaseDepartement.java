/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.param;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.base.YvsBaseDepartement;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseDepartement extends AbstractEntity {

    public AYvsBaseDepartement() {
    }

    public AYvsBaseDepartement(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseDepartement> controle(YvsBaseDepartement entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getCodeDepartement())) {
                return new ResultatAction<>(false, entity, 0L, "vous devez entrer le code du département");
            }
            if (entity.getDepartementParent() != null ? entity.getDepartementParent().getId() > 0 ? Objects.equals(entity.getDepartementParent().getId(), entity.getId()) : false : false) {
                return new ResultatAction<>(false, entity, 0L, "Le département ne peut pas etre sont propre parent");
            }
            YvsBaseDepartement t = (YvsBaseDepartement) dao.loadOneByNameQueries("YvsBaseDepartement.findByCodeDepartement", new String[]{"codeDepartement", "societe"}, new Object[]{entity.getCodeDepartement(), entity.getSociete()});
            if (t != null ? (t.getId() != null ? !t.getId().equals(entity.getId()) : false) : false) {
                if (t.getIntitule().equals(entity.getIntitule())) {
                    return new ResultatAction<>(true, t, t.getId(), "Succès", false);
                } else {
                    return new ResultatAction<>(false, entity, 0L, "Vous avez déja crée ce departement");
                }
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseDepartement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseDepartement> save(YvsBaseDepartement entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_departement", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseDepartement) dao.loadOneByNameQueries("YvsBaseDepartement.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseDepartement) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseDepartement.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseDepartement> update(YvsBaseDepartement entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseDepartement) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseDepartement.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseDepartement> delete(YvsBaseDepartement entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseDepartement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
