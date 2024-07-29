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
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseTypeDocDivers extends AbstractEntity {

    public AYvsBaseTypeDocDivers() {
    }

    public AYvsBaseTypeDocDivers(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseTypeDocDivers> controle(YvsBaseTypeDocDivers entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getCodeAcces() != null ? entity.getCodeAcces().getId() > 0 : false) {
                return new ResultatAction<>(false, entity, 0L, "Le code d'accès ne peut pas etre null");
            }

            if (!asString(entity.getLibelle())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner le libelle");
            }

            Long id = (Long) dao.loadOneByNameQueries("YvsBaseTypeDocDivers.findByIdByCode", new String[]{"libelle", "societe"}, new Object[]{entity.getLibelle(), entity.getSociete()});
            if (id != null ? id > 0 ? !id.equals(entity.getId()) : false : false) {
                return new ResultatAction<>(false, entity, 0L, "Il existe deja un type de document avec ce libelle");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseTypeDocDivers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }
    
     public ResultatAction<YvsBaseTypeDocDivers> save(YvsBaseTypeDocDivers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
               Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_type_doc_divers", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseTypeDocDivers) dao.loadOneByNameQueries("YvsBaseTypeDocDivers.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseTypeDocDivers) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, result.getMessage());
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseTypeDocDivers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;

    }

    public ResultatAction<YvsBaseTypeDocDivers> update(YvsBaseTypeDocDivers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseTypeDocDivers) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseTypeDocDivers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseTypeDocDivers> delete(YvsBaseTypeDocDivers entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseTypeDocDivers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
