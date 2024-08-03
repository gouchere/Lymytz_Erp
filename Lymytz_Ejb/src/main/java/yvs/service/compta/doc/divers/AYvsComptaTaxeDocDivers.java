/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.commercial.achat.YvsComTaxeContenuAchat;
import yvs.entity.compta.divers.YvsComptaTaxeDocDivers;
import yvs.service.AbstractEntity;
import yvs.service.com.achat.AYvsComTaxeContenuAchat;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaTaxeDocDivers extends AbstractEntity {

    public AYvsComptaTaxeDocDivers() {
    }

    public AYvsComptaTaxeDocDivers(DaoInterfaceLocal dao) {
        this.dao = dao;
    }

    public ResultatAction<YvsComptaTaxeDocDivers> controle(YvsComptaTaxeDocDivers entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if ((entity.getDocDivers() != null) ? entity.getDocDivers().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Le document ne peut pas etre null");
            }
            if ((entity.getTaxe() != null) ? entity.getTaxe().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "La taxe ne peut pas etre null");
            }
            if (entity.getMontant() < 0) {
                return new ResultatAction<>(false, entity, 0L, "Le montant ne peut pas etre null");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaTaxeDocDivers.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }

    public ResultatAction<YvsComptaTaxeDocDivers> save(YvsComptaTaxeDocDivers entity) {

        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity.setId(null);
                entity = (YvsComptaTaxeDocDivers) dao.save1(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaTaxeDocDivers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }
    
     public ResultatAction<YvsComptaTaxeDocDivers> update(YvsComptaTaxeDocDivers entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaTaxeDocDivers) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaTaxeDocDivers.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }

    public ResultatAction<YvsComptaTaxeDocDivers> delete(YvsComptaTaxeDocDivers entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaTaxeDocDivers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
