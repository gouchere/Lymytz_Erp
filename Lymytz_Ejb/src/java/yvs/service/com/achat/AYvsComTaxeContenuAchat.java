/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.achat;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.achat.YvsComTaxeContenuAchat;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComTaxeContenuAchat extends AbstractEntity {

    public AYvsComTaxeContenuAchat() {
    }

    public AYvsComTaxeContenuAchat(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComTaxeContenuAchat> controle(YvsComTaxeContenuAchat entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getContenu() != null) ? entity.getContenu().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Le contenu ne peut pas etre null");
            }
            if ((entity.getTaxe() != null) ? entity.getTaxe().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "La taxe ne peut pas etre null");
            }
            if (entity.getMontant() < 0) {
                return new ResultatAction<>(false, entity, 0L, "Le montant ne peut pas etre null");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComTaxeContenuAchat.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComTaxeContenuAchat> save(YvsComTaxeContenuAchat entity) {

        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_taxe_contenu_achat", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComTaxeContenuAchat) dao.loadOneByNameQueries("YvsComTaxeContenuAchat.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComTaxeContenuAchat) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComTaxeContenuAchat.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComTaxeContenuAchat> update(YvsComTaxeContenuAchat entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComTaxeContenuAchat) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComTaxeContenuAchat.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComTaxeContenuAchat> delete(YvsComTaxeContenuAchat entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComTaxeContenuAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
