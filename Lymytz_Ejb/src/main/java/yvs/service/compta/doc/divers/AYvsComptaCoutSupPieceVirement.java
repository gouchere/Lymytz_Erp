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
import yvs.entity.compta.YvsComptaCoutSupPieceVirement;
import yvs.service.AbstractEntity;
import yvs.service.IEntitySax;

/**
 *
 * @author hp Elite 8300
 */
public class AYvsComptaCoutSupPieceVirement extends AbstractEntity {

    public AYvsComptaCoutSupPieceVirement() {
    }

    public AYvsComptaCoutSupPieceVirement(DaoInterfaceLocal dao) {
        this.dao = dao;
    }
    IEntitySax IEntitiSax = new IEntitySax();

    public ResultatAction<YvsComptaCoutSupPieceVirement> controle(YvsComptaCoutSupPieceVirement entity) {
        try {
            if (entity == null) {
                return new ResultatAction<>(false, entity, 0L, "L'élément ne peut pas etre null");
            }
            if (entity.getVirement().getId() <= 0) {
                return new ResultatAction<>(false, entity, 0L, "Aucune piece n'a été trouvé !");
            }
            if (entity.getTypeCout() != null ? entity.getTypeCout().getId() < 1 : true) {
                return new ResultatAction<>(false, entity, 0L, "Aucun type de cout n'a été trouvé !");
            }
            if (entity.getMontant() != null ? entity.getMontant() < 0 : true) {
                return new ResultatAction<>(false, entity, 0L, "Vous devez renseigner le montant !");
            }
            return new ResultatAction<>(true, entity, 0L, "Succès");
        } catch (Exception e) {
            Logger.getLogger(AYvsComptaCoutSupPieceVirement.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ResultatAction<>(false, entity, 0L, "Action Impossible");
    }
    
     public ResultatAction<YvsComptaCoutSupPieceVirement> save(YvsComptaCoutSupPieceVirement entity) {
        
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity.setId(null);
                entity = (YvsComptaCoutSupPieceVirement) dao.save1(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCoutSupPieceVirement.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }
     
      public ResultatAction<YvsComptaCoutSupPieceVirement> update(YvsComptaCoutSupPieceVirement entity) {
        ResultatAction result = controle(entity);
        try {
            if (result.isResult()) {
                entity = (YvsComptaCoutSupPieceVirement) dao.update(entity);
                if (entity != null ? entity.getId() > 0 : false) {
                    return new ResultatAction(true, entity, entity.getId(), "Succès");
                }
                result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCoutSupPieceVirement.class.getName()).log(Level.SEVERE, null, ex);
            result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        }
        return result;
    }
      
       public ResultatAction<YvsComptaCoutSupPieceVirement> delete(YvsComptaCoutSupPieceVirement entity) {
        ResultatAction result = new ResultatAction<>(false, entity, 0L, "Action Impossible");
        try {
            boolean succes = dao.delete(entity);
            if (succes) {
                result = new ResultatAction(true, entity, entity.getId(), "Succès");
            }
        } catch (Exception ex) {
            Logger.getLogger(AYvsComptaCoutSupPieceVirement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
