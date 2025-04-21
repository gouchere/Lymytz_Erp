/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.tiers.YvsBaseTiersDocument;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsBaseTiersDocument extends AbstractEntity {

    public AYvsBaseTiersDocument() {
    }

    public AYvsBaseTiersDocument(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsBaseTiersDocument> controle(YvsBaseTiersDocument entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (!asString(entity.getTitre())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez entrer le titre du document");
            }
            if (entity.getTiers() != null ? entity.getTiers().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez préciser le tiers");
            }
            YvsBaseTiersDocument t = (YvsBaseTiersDocument) dao.loadOneByNameQueries("YvsBaseTiersDocument.findByTitre", new String[]{"titre", "tiers"}, new Object[]{entity.getTitre(), entity.getTiers()});
            if (t != null ? (t.getId() > 0 ? !t.getId().equals(entity.getId()) : false) : false) {
                return new ResultatAction<>(false, entity, 0L, "Vous avez deja rattaché ce document à ce tiers");
            }
            return new ResultatAction<>(true, entity, entity.getId(), "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsBaseTiersDocument.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsBaseTiersDocument> save(YvsBaseTiersDocument entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult() ? result.isContinu() : false) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_base_tiers_document", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsBaseTiersDocument) dao.loadOneByNameQueries("YvsBaseTiersDocument.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsBaseTiersDocument) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseTiersDocument.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseTiersDocument> update(YvsBaseTiersDocument entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsBaseTiersDocument) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseTiersDocument.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsBaseTiersDocument> delete(YvsBaseTiersDocument entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsBaseTiersDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
