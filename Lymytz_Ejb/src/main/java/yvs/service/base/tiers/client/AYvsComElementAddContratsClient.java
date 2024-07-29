/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.base.tiers.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.client.YvsComElementAddContratsClient;

import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public abstract class AYvsComElementAddContratsClient extends AbstractEntity {

    public String[] champ;
    public Object[] val;
    public String nameQueri;

    public AYvsComElementAddContratsClient() {
    }

    public AYvsComElementAddContratsClient(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComElementAddContratsClient> controle(YvsComElementAddContratsClient entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getContrat() != null) ? entity.getContrat().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le contrat");
            }
            if ((entity.getTypeCout() != null) ? entity.getTypeCout().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez specifier le type de cout");
            }
            if (entity.getMontant() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez précisez le montant");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComElementAddContratsClient.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComElementAddContratsClient> save(YvsComElementAddContratsClient entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                 Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_element_add_contrats_client", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComElementAddContratsClient) dao.loadOneByNameQueries("YvsComElementAddContratsClient.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComElementAddContratsClient) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComElementAddContratsClient.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComElementAddContratsClient> update(YvsComElementAddContratsClient entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComElementAddContratsClient) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComElementAddContratsClient.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComElementAddContratsClient> delete(YvsComElementAddContratsClient entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComElementAddContratsClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
