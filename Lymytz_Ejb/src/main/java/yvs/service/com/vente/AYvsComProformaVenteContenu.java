/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.com.vente;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.vente.YvsComProformaVenteContenu;
import yvs.service.AbstractEntity;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComProformaVenteContenu extends AbstractEntity {

    public AYvsComProformaVenteContenu() {
    }

    public AYvsComProformaVenteContenu(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComProformaVenteContenu> controle(YvsComProformaVenteContenu entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getProforma() != null) ? entity.getProforma().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer le proformat");
            }
            if ((entity.getConditionnement() != null) ? entity.getConditionnement().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer l'article");
            }
            if (entity.getQuantite() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer la quanttée");
            }
            if (entity.getPrix() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez indiquer le prix");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComProformaVenteContenu.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComProformaVenteContenu> save(YvsComProformaVenteContenu entity) {

        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                Long local = getLocalCurrent(entity.getIdDistant(), "yvs_com_proformat_vente_contenu", entity.getAdresseServeur());
                if (local != null ? local > 0 : false) {
                    entity = (YvsComProformaVenteContenu) dao.loadOneByNameQueries("YvsComProformatVenteContenu.findById", new String[]{"id"}, new Object[]{local});
                } else {
                    entity.setId(null);
                    entity = (YvsComProformaVenteContenu) dao.save1(entity);
                }
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComProformaVenteContenu.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComProformaVenteContenu> update(YvsComProformaVenteContenu entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComProformaVenteContenu) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComProformaVenteContenu.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComProformaVenteContenu> delete(YvsComProformaVenteContenu entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComProformaVenteContenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
