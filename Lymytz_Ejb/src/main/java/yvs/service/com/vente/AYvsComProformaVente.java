/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.Util;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComProformaVente;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComProformaVente extends AbstractEntity {

    public AYvsComProformaVente() {
    }

    public AYvsComProformaVente(DaoInterfaceWs dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComProformaVente> controle(YvsComProformaVente entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getAgence() != null) ? entity.getAgence().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer l'agence");
            }
            if (!asString(entity.getClient())) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer le client");
            }
            if (entity.getDateDoc() == null) {
                return new ResultatAction<>(false, entity, 0L, "La date du proformat ne peut pas etre null");
            }
            if (entity.getDateExpiration() == null) {
                return new ResultatAction<>(false, entity, 0L, "La date d'expiration ne peut pas etre null");
            }
            if (!asString(entity.getNumero())) {
                String reference = dao.genererReference(Constantes.TYPE_PFV_NAME, entity.getDateDoc(), 0, entity.getAgence().getSociete(), entity.getAgence());
                if (!Util.asString(reference)) {
                    return new ResultatAction<>(false, entity, 0L, dao.getRESULT());
                }
                entity.setNumero(reference);
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComProformaVente.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComProformaVente> save(YvsComProformaVente entity) {

        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_proformat_vente", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComProformaVente) dao.loadOneByNameQueries("YvsComProformaVente.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComProformaVente) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComProformaVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComProformaVente> update(YvsComProformaVente entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComProformaVente) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComProformaVente.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComProformaVente> delete(YvsComProformaVente entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComProformaVente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
