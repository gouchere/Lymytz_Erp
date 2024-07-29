/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaBielletage;
import yvs.service.AbstractEntity;
import yvs.service.IEntitySax;

/**
 *
 * @author Lymytz-pc
 */
public class AYvsComptaBielletage extends AbstractEntity {

    public AYvsComptaBielletage() {
    }

    public AYvsComptaBielletage(DaoInterfaceWs dao) {
        this.dao = dao;
    }
    IEntitySax IEntitiSax = new IEntitySax();

    public ResultatAction<YvsComptaBielletage> controle(YvsComptaBielletage entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getPieceVirement().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Aucune piece n'a été trouvé !");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaBielletage.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }
    
      public ResultatAction<YvsComptaBielletage> save(YvsComptaBielletage entity) {
        
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity.setId(null);
                entity = (YvsComptaBielletage) dao.save1(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaBielletage.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }
      
       public ResultatAction<YvsComptaBielletage> update(YvsComptaBielletage entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaBielletage) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaBielletage.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }
      
       public ResultatAction<YvsComptaBielletage> delete(YvsComptaBielletage entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaBielletage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
